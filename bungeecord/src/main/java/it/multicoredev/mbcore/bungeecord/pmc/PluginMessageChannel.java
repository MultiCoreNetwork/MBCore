package it.multicoredev.mbcore.bungeecord.pmc;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import it.multicoredev.mbcore.bungeecord.pmc.events.ConsoleCommandEvent;
import it.multicoredev.mbcore.bungeecord.pmc.events.PlayerCommandEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Copyright © 2020 by Lorenzo Magni
 * This file is part of MBCore.
 * MBCore is under "The 3-Clause BSD License", you can find a copy <a href="https://opensource.org/licenses/BSD-3-Clause">here</a>.
 * <p>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
@SuppressWarnings("UnstableApiUsage")
public class PluginMessageChannel implements Listener {
    private final Plugin plugin;
    private final List<String> channels = new ArrayList<>();

    /**
     * Implementation of BungeeCord Custom Plugin Message API. An easy to use API to send and receive default and custom plugin
     * messages.
     * Keep in mind that you can't send plugin messages directly after a player joins (Eg. in PlayerJoinEvent).
     * You have to include a slight delay.
     *
     * @param plugin The {@link Plugin} that is registering this MessageChannel.
     */
    public PluginMessageChannel(Plugin plugin) {
        this.plugin = plugin;
        registerChannel("mbcore:default");
        ProxyServer.getInstance().getPluginManager().registerListener(plugin, this);
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

        ProxyServer.getInstance().registerChannel(channel);
        return true;
    }

    /**
     * Unregister a channel.
     *
     * @param channel The name of the channel to unregister.
     */
    public void unregisterChannel(String channel) {
        channels.remove(channel.toLowerCase());

        ProxyServer.getInstance().unregisterChannel(channel);
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
     * Default listener for Plugin Messages.
     *
     * @param event The {@link PluginMessageEvent}.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPluginMessageReceived(PluginMessageEvent event) {
        if (!event.getTag().equals("mbcore:default")) return;

        ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
        String subchannel = in.readUTF();

        try {
            switch (subchannel) {
                case "ConsoleCommand": {
                    ConsoleCommandEvent e = new ConsoleCommandEvent(in.readUTF(), in.readUTF());
                    ProxyServer.getInstance().getPluginManager().callEvent(e);
                    if (e.isCancelled()) break;

                    if (e.getServer().equals("BUNGEECORD")) {
                        ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), e.getCommand());
                    } else if (e.getServer().equals("ALL")) {
                        CustomMessage message = new CustomMessage("mbcore:default");
                        message.writeUTF(e.getServer());
                        message.writeUTF(e.getCommand());

                        for (ServerInfo target : ProxyServer.getInstance().getServers().values())
                            sendCustomMessage(target, message);
                    } else {
                        ServerInfo target = ProxyServer.getInstance().getServerInfo(e.getServer());
                        if (target == null) return;

                        CustomMessage message = new CustomMessage("mbcore:default");
                        message.writeUTF(e.getServer());
                        message.writeUTF(e.getCommand());
                        sendCustomMessage(target, message);
                    }
                    break;
                }
                case "PlayerCommand": {
                    PlayerCommandEvent e = new PlayerCommandEvent(in.readUTF(), in.readUTF());
                    ProxyServer.getInstance().getPluginManager().callEvent(e);
                    if (e.isCancelled()) break;

                    ProxiedPlayer player = ProxyServer.getInstance().getPlayer(e.getPlayer());
                    if (player == null) return;

                    ProxyServer.getInstance().getPluginManager().dispatchCommand(player, e.getCommand());
                    break;
                }
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * Send a custom message.
     *
     * @param player  The {@link ProxiedPlayer} delivering the message.
     * @param message The {@link CustomMessage} to deliver.
     */
    public void sendCustomMessage(@NotNull ProxiedPlayer player, @NotNull CustomMessage message) {
        Objects.requireNonNull(player);
        Objects.requireNonNull(message);

        player.getServer().getInfo().sendData(message.getChannel(), message.toByteArray());
    }

    /**
     * Send a custom message.
     *
     * @param server  The {@link ServerInfo} target of the message.
     * @param message The {@link CustomMessage} to deliver.
     * @return false if the target server doesn't have any player to deliver the message.
     */
    public boolean sendCustomMessage(@NotNull ServerInfo server, @NotNull CustomMessage message) {
        if (server.getPlayers().isEmpty()) return false;
        sendCustomMessage(new ArrayList<>(server.getPlayers()).get(0), message);
        return true;
    }

    /**
     * Run a command on one or more servers.
     *
     * @param server  The name of the server where the command should run, as defined in BungeeCord config.yml,
     *                ALL to run in every server.
     * @param command The command to run.
     */
    public void consoleCommand(@NotNull String server, @NotNull String command) {
        CustomMessage message = new CustomMessage("mbcore:default");
        message.writeUTF("ConsoleCommand");
        message.writeUTF(server);
        message.writeUTF(command);

        if (server.equals("ALL")) {
            for (ServerInfo s : ProxyServer.getInstance().getServers().values()) {
                sendCustomMessage(s, message);
            }
        } else {
            ServerInfo s = ProxyServer.getInstance().getServerInfo(server);
            if (s == null) return;

            sendCustomMessage(s, message);
        }
    }

    /**
     * Run a command on s Spigot server as a player.
     *
     * @param player  The {@link ProxiedPlayer} that run the command.
     * @param command The command to run.
     */
    public void playerCommand(@NotNull ProxiedPlayer player, @NotNull String command) {
        CustomMessage message = new CustomMessage("mbcore:default");
        message.writeUTF("PlayerCommand");
        message.writeUTF(player.getName());
        message.writeUTF(command);

        sendCustomMessage(player.getServer().getInfo(), message);
    }

    /**
     * Run a command on s Spigot server as a player.
     *
     * @param player  The name of the player that run the command.
     * @param command The command to run.
     */
    public void playerCommand(@NotNull String player, @NotNull String command) {
        CustomMessage message = new CustomMessage("mbcore:default");
        message.writeUTF("PlayerCommand");
        message.writeUTF(player);
        message.writeUTF(command);

        ProxiedPlayer p = ProxyServer.getInstance().getPlayer(player);
        if (p == null) return;

        sendCustomMessage(p.getServer().getInfo(), message);
    }
}
