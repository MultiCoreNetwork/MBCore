package it.multicoredev.mbcore.spigot.pmc;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import it.multicoredev.mbcore.spigot.pmc.events.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * BSD 3-Clause License
 * <p>
 * Copyright (c) 2016 - 2024, Lorenzo Magni
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * <p>
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * <p>
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * <p>
 * 3. Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
@SuppressWarnings("UnstableApiUsage")
public class PluginMessageChannel implements PluginMessageListener {
    private final Plugin plugin;
    private final BukkitScheduler scheduler;
    private final List<String> channels = new CopyOnWriteArrayList<>();
    private boolean defListener = false;

    /**
     * Implementation of Bukkit Custom Plugin Message API. An easy to use API to send and receive default and custom plugin
     * messages.
     * Keep in mind that you can't send plugin messages directly after a player joins (Eg. in PlayerJoinEvent).
     * You have to include a slight delay.
     *
     * @param plugin The {@link Plugin} that is registering this MessageChannel.
     */
    public PluginMessageChannel(Plugin plugin) {
        this.plugin = plugin;

        registerChannel("BungeeCord");

        scheduler = Bukkit.getScheduler();
    }

    /**
     * Register a channel.
     *
     * @param channel The name of the channel to register.
     * @return true if channel has been registered, false if the channel is already registered.
     */
    public boolean registerChannel(String channel) {
        if (channels.contains(channel.toLowerCase())) return false;
        channels.add(channel.toLowerCase());

        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(plugin, channel);
        Bukkit.getServer().getMessenger().registerIncomingPluginChannel(plugin, channel, this);
        return true;
    }

    /**
     * Unregister a channel.
     *
     * @param channel The name of the channel to unregister.
     */
    public void unregisterChannel(String channel) {
        channels.remove(channel.toLowerCase());

        Bukkit.getServer().getMessenger().unregisterIncomingPluginChannel(plugin, channel);
        Bukkit.getServer().getMessenger().unregisterOutgoingPluginChannel(plugin, channel);
    }

    /**
     * Register the default channel.
     */
    public void registerDefault() {
        registerChannel("mbcore:default");
    }

    /**
     * Unregister the default channel.
     */
    public void unregisterDefault() {
        unregisterChannel("mbcore:default");
    }

    /**
     * Unregister all channels.
     * Keep in mind that this will unregister also BungeeCord and mbcore:default channels.
     */
    public void unregisterAllChannels() {
        for (String channel : channels) {
            unregisterChannel(channel);
        }
    }

    /**
     * Enable the default channel listener.
     */
    public void enableDefaultListener() {
        defListener = true;
    }

    /**
     * Disable the default channel listener.
     */
    public void disableDefaultListener() {
        defListener = false;
    }

    /**
     * Called when a message is received from the Message Channel.
     *
     * @param channel The name of the channel.
     * @param player  The player that is delivering the message.
     * @param message The content of the message.
     */
    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {
        ByteArrayDataInput in = ByteStreams.newDataInput(message);

        if (channel.equals("BungeeCord")) {
            String subchannel = in.readUTF();

            switch (subchannel) {
                case "IP":
                    scheduler.runTaskAsynchronously(plugin, () -> callEvent(new IPResponseEvent(in.readUTF(), in.readInt())));
                    break;
                case "IPOther":
                    scheduler.runTaskAsynchronously(plugin, () -> callEvent(new IPOtherResponseEvent(in.readUTF(), in.readUTF(), in.readInt())));
                    break;
                case "PlayerCount":
                    scheduler.runTaskAsynchronously(plugin, () -> callEvent(new PlayerCountResponseEvent(in.readUTF(), in.readInt())));
                                        break;
                case "PlayerList":
                    scheduler.runTaskAsynchronously(plugin, () -> callEvent(new PlayerListResponseEvent(in.readUTF(), in.readUTF().split(", "))));
                    break;
                case "GetServers":
                    scheduler.runTaskAsynchronously(plugin, () -> callEvent(new GetServersResponseEvent(in.readUTF().split(", "))));
                    break;
                case "GetServer":
                    scheduler.runTaskAsynchronously(plugin, () -> callEvent(new GetServerResponseEvent(in.readUTF())));
                    break;
                case "Forward": {
                    String subCh = in.readUTF();
                    short len = in.readShort();
                    byte[] bytes = new byte[len];
                    in.readFully(bytes);
                    DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));

                    scheduler.runTaskAsynchronously(plugin, () -> callEvent(new ForwardResponseEvent(subCh, len, dis)));
                    break;
                }
                case "ForwardToPlayer": {
                    String subCh = in.readUTF();
                    short len = in.readShort();
                    byte[] bytes = new byte[len];
                    in.readFully(bytes);
                    DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));

                    scheduler.runTaskAsynchronously(plugin, () -> callEvent(new ForwardToPlayerResponseEvent(subCh, len, dis)));
                    break;
                }
            }
        } else if (channel.equals("mbcore:default") && defListener) {
            String subchannel = in.readUTF();

            switch (subchannel) {
                case "ConsoleCommand": {
                    ConsoleCommandEvent event = new ConsoleCommandEvent(in.readUTF(), in.readUTF());
                    callEvent(event);
                    if (event.isCancelled()) break;

                    Bukkit.getScheduler().callSyncMethod(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), event.getCommand()));
                    break;
                }
                case "PlayerCommand": {
                    PlayerCommandEvent event = new PlayerCommandEvent(in.readUTF(), in.readUTF());
                    callEvent(event);
                    if (event.isCancelled()) break;

                    Player p = Bukkit.getPlayer(event.getPlayer());
                    if (p == null) return;

                    Bukkit.getScheduler().callSyncMethod(plugin, () -> Bukkit.dispatchCommand(p, event.getCommand()));
                }
            }
        }
    }

    /**
     * Connects a {@link Player} to said subserver.
     *
     * @param player The player you want to connect.
     * @param server The name of the server to connect to, as defined in BungeeCord config.yml.
     */
    public void connect(@NotNull Player player, @NotNull String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        sendMessage(player, out);
    }

    /**
     * Connect a named player to said subserver.
     *
     * @param player The name of the player you want to connect.
     * @param server The name of the server to connect to, as defined in BungeeCord config.yml.
     */
    public void connectOther(@NotNull String player, @NotNull String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ConnectOther");
        out.writeUTF(player);
        out.writeUTF(server);
        sendMessage(out);
    }

    /**
     * Get the (real) IP of a {@link Player}.
     *
     * @param player The player you wish to get the IP of.
     */
    public void ip(@NotNull Player player) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("IP");
        sendMessage(player, out);
    }

    /**
     * Get the (real) IP of another player.
     *
     * @param player The name of the player you wish to get the IP of.
     */
    public void ipOther(@NotNull String player) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("IPOther");
        out.writeUTF(player);
        sendMessage(out);
    }

    /**
     * Get the amount of players on a certain server, or on ALL the servers.
     *
     * @param server The name of the server to get the player count of, as defined in BungeeCord config.yml
     *               or ALL to get the global player count.
     */
    public void playerCount(@NotNull String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerCount");
        out.writeUTF(server);
        sendMessage(out);
    }

    /**
     * Get a list of players connected on a certain server, or on ALL the servers.
     *
     * @param server The name of the server to get the player list of, as defined in BungeeCord config.yml
     *               or ALL to get the global player list.
     */
    public void playerList(@NotNull String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerList");
        out.writeUTF(server);
        sendMessage(out);
    }

    /**
     * Get a list of server name strings, as defined in BungeeCord's config.yml
     */
    public void getServers() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("GetServers");
        sendMessage(out);
    }

    /**
     * Send a message (as in, a chat message) to the specified player.
     *
     * @param target              The name of the player to send the chat message or All to send to all players.
     * @param message             The message to send to the player.
     * @param translateChatColors Convert chat color codes.
     */
    public void message(@NotNull String target, @NotNull String message, boolean translateChatColors) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Message");
        out.writeUTF(target);
        out.writeUTF(/*translateChatColors ? Chat.getTranslated(message) :*/ message);
        sendMessage(out);
    }

    /**
     * Send a message (as in, a chat message) to the specified player.
     * Color codes are converted by default.
     *
     * @param target  The name of the player to send the chat message or All to send to all players.
     * @param message The message to send to the player.
     */
    public void message(@NotNull String target, @NotNull String message) {
        message(target, message, true);
    }

    /**
     * Send a raw message (as in, a chat message) to the specified player.
     * The advantage of this method over Message is that you can include click events and hover events.
     *
     * @param target  The name of the player to send the chat message or All to send to all players.
     * @param message The message to send to the player.
     */
    public void messageRaw(@NotNull String target, @NotNull String message) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("MessageRaw");
        out.writeUTF(target);
        out.writeUTF(message);
    }

    /**
     * Get this server's name, as defined in BungeeCord's config.yml
     */
    public void getServer() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("GetServer");
        sendMessage(out);
    }

    /**
     * Send a custom plugin message to said server.
     * Remember, the sending and receiving server(s) need to have a player online.
     *
     * @param server  The name of the server to send to, as defined in BungeeCord config.yml,
     *                ALL to send to every server except the one sending the message
     *                or ONLINE to send to every online server except the one sending the message.
     * @param channel The name of the subchannel you are using.
     * @param message The custom message to send.
     */
    public void forward(@NotNull String server, @NotNull String channel, @NotNull ForwardMessage message) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Forward");
        out.writeUTF(server);
        out.writeUTF(channel);
        out.writeShort(message.size());
        out.write(message.toByteArray());
        sendMessage(out);
    }

    /**
     * Send a custom plugin message to specific player.
     *
     * @param player  The name of the player to send to.
     * @param channel The name of the subchannel you are using.
     * @param message The custom message to send.
     */
    public void forwardToPlayer(@NotNull String player, @NotNull String channel, @NotNull ForwardMessage message) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ForwardToPlayer");
        out.writeUTF(player);
        out.writeUTF(channel);
        out.writeShort(message.size());
        out.write(message.toByteArray());
        sendMessage(out);
    }

    /**
     * Request the UUID of a {@link Player}.
     *
     * @param player The player whose UUID you requested.
     */
    public void uuid(@NotNull Player player) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("UUID");
        sendMessage(player, out);
    }

    /**
     * Request the UUID of any player connected to the BungeeCord proxy.
     *
     * @param player The name of the player whose UUID you would like.
     */
    public void uuidOther(@NotNull String player) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("UUIDOther");
        out.writeUTF(player);
        sendMessage(out);
    }

    /**
     * Request the IP of any server on this proxy.
     *
     * @param server The name of the server to get the player list of, as defined in BungeeCord config.yml.
     */
    public void serverIP(@NotNull String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ServerIP");
        out.writeUTF(server);
        sendMessage(out);
    }

    /**
     * Kick any player on this proxy.
     *
     * @param player              The name of the player.
     * @param reason              The reason the player is kicked with.
     * @param translateChatColors Convert chat color codes.
     */
    public void kickPlayer(@NotNull String player, @NotNull String reason, boolean translateChatColors) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("KickPlayer");
        out.writeUTF(player);
        out.writeUTF(/*translateChatColors ? Chat.getTranslated(reason) : */reason);
        sendMessage(out);
    }

    /**
     * Kick any player on this proxy.
     * Color codes are converted by default.
     *
     * @param player The name of the player.
     * @param reason The reason the player is kicked with.
     */
    public void kickPlayer(@NotNull String player, @NotNull String reason) {
        kickPlayer(player, reason, true);
    }

    /**
     * Send a custom message.
     *
     * @param player  The {@link Player} delivering the message.
     * @param message The {@link CustomMessage} message to deliver.
     */
    public void sendCustomMessage(@NotNull Player player, @NotNull CustomMessage message) {
        Objects.requireNonNull(player);
        Objects.requireNonNull(message);

        player.sendPluginMessage(plugin, message.getChannel(), message.toByteArray());
    }

    /**
     * Send a custom message.
     *
     * @param message The {@link CustomMessage} message to deliver.
     * @return false if the target server doesn't have any player to deliver the message.
     */
    public boolean sendCustomMessage(@NotNull CustomMessage message) {
        if (Bukkit.getOnlinePlayers().isEmpty()) return false;

        sendCustomMessage(new ArrayList<>(Bukkit.getOnlinePlayers()).get(0), message);
        return true;
    }

    /**
     * Run a command on one or more servers.
     *
     * @param server  The name of the server where the command should run, as defined in BungeeCord config.yml,
     *                ALL to run in every server or BUNGEECORD to run in BungeeCord.
     * @param command The command to run.
     */
    public void consoleCommand(@NotNull String server, @NotNull String command) {
        CustomMessage message = new CustomMessage("mbcore:default");
        message.writeUTF("ConsoleCommand");
        message.writeUTF(server);
        message.writeUTF(command);

        sendCustomMessage(message);
    }

    /**
     * Run a command on BungeeCord as a player.
     *
     * @param player  The {@link Player} that run the command.
     * @param command The command to run.
     */
    public void playerCommand(@NotNull Player player, @NotNull String command) {
        CustomMessage message = new CustomMessage("mbcore:default");
        message.writeUTF("PlayerCommand");
        message.writeUTF(player.getName());
        message.writeUTF(command);

        sendCustomMessage(message);
    }

    /**
     * Run a command on BungeeCord as a player.
     *
     * @param player  The name of the player that run the command.
     * @param command The command to run.
     */
    public void playerCommand(@NotNull String player, @NotNull String command) {
        CustomMessage message = new CustomMessage("mbcore:default");
        message.writeUTF("PlayerCommand");
        message.writeUTF(player);
        message.writeUTF(command);

        sendCustomMessage(message);
    }

    private void callEvent(Event event) {
        Bukkit.getPluginManager().callEvent(event);
    }

    private void sendMessage(@NotNull Player player, @NotNull ByteArrayDataOutput out) {
        Objects.requireNonNull(player);
        Objects.requireNonNull(out);
        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }

    private void sendMessage(@NotNull ByteArrayDataOutput out) {
        if (Bukkit.getOnlinePlayers().isEmpty()) return;

        sendMessage(new ArrayList<>(Bukkit.getOnlinePlayers()).get(0), out);
    }
}
