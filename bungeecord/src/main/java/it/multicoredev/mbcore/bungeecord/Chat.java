package it.multicoredev.mbcore.bungeecord;

import com.google.common.base.Preconditions;
import com.google.gson.JsonSyntaxException;
import it.multicoredev.mbcore.bungeecord.util.chat.RawMessage;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * BSD 3-Clause License
 * <p>
 * Copyright (c) 2016 - 2023, Lorenzo Magni
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
public class Chat {
    private final static Pattern hexColorPattern = Pattern.compile("#([A-Fa-f0-9]{6})");

    private static String translateHex(String msg) {
        Preconditions.checkNotNull(msg, "Cannot translate null text");

        msg = msg.replace("&g", "#2196F3")
                .replace("&h", "#2962FF");

        Matcher matcher = hexColorPattern.matcher(msg);
        while (matcher.find()) {
            String match = matcher.group(0);
            msg = msg.replace(match, ChatColor.of(match) + "");
        }

        return msg;
    }

    /**
     * Convert '&amp;' color codes to Minecraft understandable color codes.
     *
     * @param msg The message to convert.
     * @return The converted message.
     */
    public static String getTranslated(String msg) {
        if (msg == null) return null;
        msg = ChatColor.translateAlternateColorCodes('&', msg);
        msg = translateHex(msg);
        return msg;
    }

    /**
     * Convert '&amp;' color codes to Minecraft understandable color codes if the sender has one or more permissions.
     *
     * @param msg         The message to convert.
     * @param sender      The sender of the message.
     * @param permissions One or more permissions to check.
     * @return The converted message.
     */
    public static String getTranslated(String msg, ProxiedPlayer sender, String... permissions) {
        if (hasPermission(sender, permissions)) return getTranslated(msg);
        return msg;
    }

    /**
     * Convert '&amp;' color codes to Minecraft understandable color codes if the sender has one or more permissions.
     *
     * @param msg         The message to convert.
     * @param sender      The sender of the message.
     * @param permissions One or more permissions to check.
     * @return The converted message.
     */
    public static String getTranslated(String msg, CommandSender sender, String... permissions) {
        if (sender instanceof ProxiedPlayer) return getTranslated(msg, (ProxiedPlayer) sender, permissions);
        return getTranslated(msg);
    }

    /**
     * Convert '&amp;' color codes to Minecraft understandable color codes.
     *
     * @param msgs The messages to convert.
     * @return The converted messages.
     */
    public static String[] getTranslated(String[] msgs) {
        for (int i = 0; i < msgs.length; i++) {
            String msg = msgs[i];
            if (msg == null) return null;
            msg = ChatColor.translateAlternateColorCodes('&', msg);
            msg = translateHex(msg);
            msgs[i] = msg;
        }

        return msgs;
    }

    /**
     * Convert '&amp;' color codes to Minecraft understandable color codes if the sender has one or more permissions.
     *
     * @param msgs        The messages to convert.
     * @param sender      The sender of the message.
     * @param permissions One or more permissions to check.
     * @return The converted messages.
     */
    public static String[] getTranslated(String[] msgs, ProxiedPlayer sender, String... permissions) {
        if (hasPermission(sender, permissions)) return getTranslated(msgs);
        return msgs;
    }

    /**
     * Convert '&amp;' color codes to Minecraft understandable color codes if the sender has one or more permissions.
     *
     * @param msgs        The messages to convert.
     * @param sender      The sender of the message.
     * @param permissions One or more permissions to check.
     * @return The converted messages.
     */
    public static String[] getTranslated(String[] msgs, CommandSender sender, String... permissions) {
        if (sender instanceof ProxiedPlayer) return getTranslated(msgs, (ProxiedPlayer) sender, permissions);
        return getTranslated(msgs);
    }

    /**
     * Convert '&amp;' color codes to Minecraft understandable color codes.
     *
     * @param msgs The messages to convert.
     * @return The converted messages.
     */
    public static List<String> getTranslated(List<String> msgs) {
        List<String> out = new ArrayList<>();
        for (String msg : msgs) {
            if (msg == null) return null;
            msg = ChatColor.translateAlternateColorCodes('&', msg);
            msg = translateHex(msg);
            out.add(msg);
        }

        return out;
    }

    /**
     * Convert '&amp;' color codes to Minecraft understandable color codes if the sender has one or more permissions.
     *
     * @param msgs        The messages to convert.
     * @param sender      The sender of the message.
     * @param permissions One or more permissions to check.
     * @return The converted messages.
     */
    public static List<String> getTranslated(List<String> msgs, ProxiedPlayer sender, String... permissions) {
        if (hasPermission(sender, permissions)) return getTranslated(msgs);
        return msgs;
    }

    /**
     * Convert '&amp;' color codes to Minecraft understandable color codes if the sender has one or more permissions.
     *
     * @param msgs        The messages to convert.
     * @param sender      The sender of the message.
     * @param permissions One or more permissions to check.
     * @return The converted messages.
     */
    public static List<String> getTranslated(List<String> msgs, CommandSender sender, String... permissions) {
        if (sender instanceof ProxiedPlayer) return getTranslated(msgs, (ProxiedPlayer) sender, permissions);
        return getTranslated(msgs);
    }

    /**
     * Remove all '&amp;' color codes.
     *
     * @param msg The message to convert.
     * @return The converted message.
     */
    public static String getDiscolored(String msg) {
        Preconditions.checkNotNull(msg, "Cannot translate null text");

        msg = msg.replaceAll("&[0-9a-hA-HkKlLmMnNoOrR]", "");
        msg = msg.replaceAll("§[0-9a-hA-HkKlLmMnNoOrR]", "");

        Matcher matcher = hexColorPattern.matcher(msg);
        while (matcher.find()) {
            String match = matcher.group(0);
            msg = msg.replace(match, "");
        }

        return msg;
    }

    /**
     * Remove all '&amp;' color codes.
     *
     * @param msgs The messages to convert.
     * @return The converted messages.
     */
    public static String[] getDiscolored(String[] msgs) {
        if (msgs == null) return null;

        for (int i = 0; i < msgs.length; i++) {
            String msg = msgs[i];
            msg = msg.replaceAll("&[0-9a-hA-HkKlLmMnNoOrR]", "");
            msg = msg.replaceAll("§[0-9a-hA-HkKlLmMnNoOrR]", "");

            Matcher matcher = hexColorPattern.matcher(msg);
            while (matcher.find()) {
                String match = matcher.group(0);
                msg = msg.replace(match, "");
            }

            msgs[i] = msg;
        }

        return msgs;
    }

    /**
     * Remove all '&amp;' color codes.
     *
     * @param msgs The messages to convert.
     * @return The converted messages.
     */
    public static List<String> getDiscolored(List<String> msgs) {
        if (msgs == null) return null;

        for (String msg : msgs) {
            msg = msg.replaceAll("&[0-9a-hA-HkKlLmMnNoOrR]", "");
            msg = msg.replaceAll("§[0-9a-hA-HkKlLmMnNoOrR]", "");

            Matcher matcher = hexColorPattern.matcher(msg);
            while (matcher.find()) {
                String match = matcher.group(0);
                msg = msg.replace(match, "");
            }
        }

        return msgs;
    }

    /**
     * Join string arguments starting from an offset.
     *
     * @param args   The arguments to join.
     * @param offset The offset.
     * @return The joined string.
     */
    public static String builder(String[] args, int offset) {
        StringBuilder builder = new StringBuilder();
        for (; offset < args.length; offset++) builder.append(args[offset]).append(" ");
        return builder.toString();
    }

    /**
     * Join string arguments.
     *
     * @param args The arguments to join.
     * @return The joined string.
     */
    public static String builder(String[] args) {
        return builder(args, 0);
    }

    /**
     * Send a message to a player.
     *
     * @param msg       The message to be sent.
     * @param receiver  The receiver of the message.
     * @param translate Convert the color codes.
     */
    public static void send(String msg, ProxiedPlayer receiver, boolean translate) {
        if (msg.startsWith("!j")) {
            sendRaw(msg.substring(2), receiver);
            return;
        }

        receiver.sendMessage(TextComponent.fromLegacyText(translate ? getTranslated(msg) : msg));
    }

    /**
     * Send a message to a player.
     *
     * @param msg      The message to be sent.
     * @param receiver The receiver of the message.
     */
    public static void send(String msg, ProxiedPlayer receiver) {
        send(msg, receiver, true);
    }

    /**
     * Send a message to a player.
     *
     * @param msg         The message to be sent.
     * @param receiver    The receiver of the message.
     * @param sender      The sender of the message.
     * @param permissions Convert the color codes if the sender has this permissions.
     */
    public static void send(String msg, ProxiedPlayer receiver, ProxiedPlayer sender, String... permissions) {
        if (msg.startsWith("!j")) {
            sendRaw(msg.substring(2), receiver);
            return;
        }

        receiver.sendMessage(TextComponent.fromLegacyText(getTranslated(msg, sender, permissions)));
    }

    /**
     * Send a message to a player.
     *
     * @param msg         The message to be sent.
     * @param receiver    The receiver of the message.
     * @param sender      The sender of the message.
     * @param permissions Convert the color codes if the sender has this permissions.
     */
    public static void send(String msg, ProxiedPlayer receiver, CommandSender sender, String... permissions) {
        if (msg.startsWith("!j")) {
            sendRaw(msg.substring(2), receiver);
            return;
        }

        receiver.sendMessage(TextComponent.fromLegacyText(getTranslated(msg, sender, permissions)));
    }

    /**
     * Send a message to a player.
     *
     * @param msg       The message to be sent.
     * @param receiver  The receiver of the message.
     * @param translate Convert the color codes.
     */
    public static void send(String msg, CommandSender receiver, boolean translate) {
        if (msg.startsWith("!j") && (receiver instanceof ProxiedPlayer)) {
            sendRaw(msg.substring(2), (ProxiedPlayer) receiver);
            return;
        }

        receiver.sendMessage(TextComponent.fromLegacyText(translate ? getTranslated(msg) : msg));
    }

    /**
     * Send a message to a player.
     *
     * @param msg      The message to be sent.
     * @param receiver The receiver of the message.
     */
    public static void send(String msg, CommandSender receiver) {
        send(msg, receiver, true);
    }

    /**
     * Send a message to a player.
     *
     * @param msg         The message to be sent.
     * @param receiver    The receiver of the message.
     * @param sender      The sender of the message.
     * @param permissions Convert the color codes if the sender has this permissions.
     */
    public static void send(String msg, CommandSender receiver, ProxiedPlayer sender, String... permissions) {
        if (msg.startsWith("!j") && (receiver instanceof ProxiedPlayer)) {
            sendRaw(msg.substring(2), (ProxiedPlayer) receiver);
            return;
        }

        receiver.sendMessage(TextComponent.fromLegacyText(getTranslated(msg, sender, permissions)));
    }

    /**
     * Send a message to a player.
     *
     * @param msg         The message to be sent.
     * @param receiver    The receiver of the message.
     * @param sender      The sender of the message.
     * @param permissions Convert the color codes if the sender has this permissions.
     */
    public static void send(String msg, CommandSender receiver, CommandSender sender, String... permissions) {
        if (msg.startsWith("!j") && (receiver instanceof ProxiedPlayer)) {
            sendRaw(msg.substring(2), (ProxiedPlayer) receiver);
            return;
        }

        receiver.sendMessage(TextComponent.fromLegacyText(getTranslated(msg, sender, permissions)));
    }

    /**
     * Send a message to a player.
     *
     * @param msgs      The messages to be sent.
     * @param receiver  The receiver of the message.
     * @param translate Convert the color codes.
     */
    public static void send(String[] msgs, ProxiedPlayer receiver, boolean translate) {
        for (String msg : msgs) {
            if (msg.startsWith("!j")) {
                sendRaw(msg.substring(2), receiver);
                return;
            }

            receiver.sendMessage(TextComponent.fromLegacyText(translate ? getTranslated(msg) : msg));
        }
    }

    /**
     * Send a message to a player.
     *
     * @param msgs     The messages to be sent.
     * @param receiver The receiver of the message.
     */
    public static void send(String[] msgs, ProxiedPlayer receiver) {
        send(msgs, receiver, true);
    }

    /**
     * Send a message to a player.
     *
     * @param msgs        The messages to be sent.
     * @param receiver    The receiver of the message.
     * @param sender      The sender of the message.
     * @param permissions Convert the color codes if the sender has this permissions.
     */
    public static void send(String[] msgs, ProxiedPlayer receiver, ProxiedPlayer sender, String... permissions) {
        for (String msg : msgs) {
            if (msg.startsWith("!j")) {
                sendRaw(msg.substring(2), receiver);
                return;
            }

            receiver.sendMessage(TextComponent.fromLegacyText(getTranslated(msg, sender, permissions)));
        }
    }

    /**
     * Send a message to a player.
     *
     * @param msgs        The messages to be sent.
     * @param receiver    The receiver of the message.
     * @param sender      The sender of the message.
     * @param permissions Convert the color codes if the sender has this permissions.
     */
    public static void send(String[] msgs, ProxiedPlayer receiver, CommandSender sender, String... permissions) {
        for (String msg : msgs) {
            if (msg.startsWith("!j")) {
                sendRaw(msg.substring(2), receiver);
                return;
            }

            receiver.sendMessage(TextComponent.fromLegacyText(getTranslated(msg, sender, permissions)));
        }
    }

    /**
     * Send a message to a player.
     *
     * @param msgs      The messages to be sent.
     * @param receiver  The receiver of the message.
     * @param translate Convert the color codes.
     */
    public static void send(String[] msgs, CommandSender receiver, boolean translate) {
        for (String msg : msgs) {
            if (msg.startsWith("!j") && (receiver instanceof ProxiedPlayer)) {
                sendRaw(msg.substring(2), (ProxiedPlayer) receiver);
                return;
            }

            receiver.sendMessage(TextComponent.fromLegacyText(translate ? getTranslated(msg) : msg));
        }
    }

    /**
     * Send a message to a player.
     *
     * @param msgs     The messages to be sent.
     * @param receiver The receiver of the message.
     */
    public static void send(String[] msgs, CommandSender receiver) {
        send(msgs, receiver, true);
    }

    /**
     * Send a message to a player.
     *
     * @param msgs        The messages to be sent.
     * @param receiver    The receiver of the message.
     * @param sender      The sender of the message.
     * @param permissions Convert the color codes if the sender has this permissions.
     */
    public static void send(String[] msgs, CommandSender receiver, ProxiedPlayer sender, String... permissions) {
        for (String msg : msgs) {
            if (msg.startsWith("!j") && (receiver instanceof ProxiedPlayer)) {
                sendRaw(msg.substring(2), (ProxiedPlayer) receiver);
                return;
            }

            receiver.sendMessage(TextComponent.fromLegacyText(getTranslated(msg, sender, permissions)));
        }

    }

    /**
     * Send a message to a player.
     *
     * @param msgs        The messages to be sent.
     * @param receiver    The receiver of the message.
     * @param sender      The sender of the message.
     * @param permissions Convert the color codes if the sender has this permissions.
     */
    public static void send(String[] msgs, CommandSender receiver, CommandSender sender, String... permissions) {
        for (String msg : msgs) {
            if (msg.startsWith("!j") && (receiver instanceof ProxiedPlayer)) {
                sendRaw(msg.substring(2), (ProxiedPlayer) receiver);
                return;
            }

            receiver.sendMessage(TextComponent.fromLegacyText(getTranslated(msg, sender, permissions)));
        }
    }

    /**
     * Send a message to a player.
     *
     * @param msgs      The messages to be sent.
     * @param receiver  The receiver of the message.
     * @param translate Convert the color codes.
     */
    public static void send(List<String> msgs, ProxiedPlayer receiver, boolean translate) {
        for (String msg : msgs) {
            if (msg.startsWith("!j")) {
                sendRaw(msg.substring(2), receiver);
                return;
            }

            receiver.sendMessage(TextComponent.fromLegacyText(translate ? getTranslated(msg) : msg));
        }
    }

    /**
     * Send a message to a player.
     *
     * @param msgs     The messages to be sent.
     * @param receiver The receiver of the message.
     */
    public static void send(List<String> msgs, ProxiedPlayer receiver) {
        send(msgs, receiver, true);
    }

    /**
     * Send a message to a player.
     *
     * @param msgs        The messages to be sent.
     * @param receiver    The receiver of the message.
     * @param sender      The sender of the message.
     * @param permissions Convert the color codes if the sender has this permissions.
     */
    public static void send(List<String> msgs, ProxiedPlayer receiver, ProxiedPlayer sender, String... permissions) {
        for (String msg : msgs) {
            if (msg.startsWith("!j")) {
                sendRaw(msg.substring(2), receiver);
                return;
            }

            receiver.sendMessage(TextComponent.fromLegacyText(getTranslated(msg, sender, permissions)));
        }
    }

    /**
     * Send a message to a player.
     *
     * @param msgs        The messages to be sent.
     * @param receiver    The receiver of the message.
     * @param sender      The sender of the message.
     * @param permissions Convert the color codes if the sender has this permissions.
     */
    public static void send(List<String> msgs, ProxiedPlayer receiver, CommandSender sender, String... permissions) {
        for (String msg : msgs) {
            if (msg.startsWith("!j")) {
                sendRaw(msg.substring(2), receiver);
                return;
            }

            receiver.sendMessage(TextComponent.fromLegacyText(getTranslated(msg, sender, permissions)));
        }
    }

    /**
     * Send a message to a player.
     *
     * @param msgs      The messages to be sent.
     * @param receiver  The receiver of the message.
     * @param translate Convert the color codes.
     */
    public static void send(List<String> msgs, CommandSender receiver, boolean translate) {
        for (String msg : msgs) {
            if (msg.startsWith("!j") && (receiver instanceof ProxiedPlayer)) {
                sendRaw(msg.substring(2), (ProxiedPlayer) receiver);
                return;
            }
            receiver.sendMessage(TextComponent.fromLegacyText(translate ? getTranslated(msg) : msg));
        }
    }

    /**
     * Send a message to a player.
     *
     * @param msgs     The messages to be sent.
     * @param receiver The receiver of the message.
     */
    public static void send(List<String> msgs, CommandSender receiver) {
        send(msgs, receiver, true);
    }

    /**
     * Send a message to a player.
     *
     * @param msgs        The messages to be sent.
     * @param receiver    The receiver of the message.
     * @param sender      The sender of the message.
     * @param permissions Convert the color codes if the sender has this permissions.
     */
    public static void send(List<String> msgs, CommandSender receiver, ProxiedPlayer sender, String... permissions) {
        for (String msg : msgs) {
            if (msg.startsWith("!j") && (receiver instanceof ProxiedPlayer)) {
                sendRaw(msg.substring(2), (ProxiedPlayer) receiver);
                return;
            }

            receiver.sendMessage(TextComponent.fromLegacyText(getTranslated(msg, sender, permissions)));
        }

    }

    /**
     * Send a message to a player.
     *
     * @param msgs        The messages to be sent.
     * @param receiver    The receiver of the message.
     * @param sender      The sender of the message.
     * @param permissions Convert the color codes if the sender has this permissions.
     */
    public static void send(List<String> msgs, CommandSender receiver, CommandSender sender, String... permissions) {
        for (String msg : msgs) {
            if (msg.startsWith("!j") && (receiver instanceof ProxiedPlayer)) {
                sendRaw(msg.substring(2), (ProxiedPlayer) receiver);
                return;
            }

            receiver.sendMessage(TextComponent.fromLegacyText(getTranslated(msg, sender, permissions)));
        }
    }

    /**
     * Send a raw message like a tellraw.
     *
     * @param msg      The message to be sent.
     * @param receiver The receiver of the message.
     */
    public static void sendRaw(RawMessage msg, ProxiedPlayer receiver) {
        receiver.sendMessage(msg.toTextComponents());
    }

    /**
     * Send a raw message like a tellraw.
     *
     * @param jsonMsg  The message to be sent.
     * @param receiver The receiver of the message.
     */
    public static void sendRaw(String jsonMsg, ProxiedPlayer receiver) throws JsonSyntaxException {
        sendRaw(new RawMessage(jsonMsg), receiver);
    }

    /**
     * Send a raw message like a tellraw.
     *
     * @param msgs     The messages to be sent.
     * @param receiver The receiver of the message.
     */
    public static void sendRaw(RawMessage[] msgs, ProxiedPlayer receiver) {
        for (RawMessage msg : msgs) {
            receiver.sendMessage(msg.toTextComponents());
        }
    }

    /**
     * Send a raw message like a tellraw.
     *
     * @param jsonMsgs The messages to be sent.
     * @param receiver The receiver of the message.
     */
    public static void sendRaw(String[] jsonMsgs, ProxiedPlayer receiver) throws JsonSyntaxException {
        for (String jsonMsg : jsonMsgs) {
            sendRaw(new RawMessage(jsonMsg), receiver);
        }
    }

    /**
     * Send a raw message like a tellraw.
     *
     * @param msgs     The messages to be sent.
     * @param receiver The receiver of the message.
     */
    public static void sendRaw(List<RawMessage> msgs, ProxiedPlayer receiver) {
        for (RawMessage msg : msgs) {
            receiver.sendMessage(msg.toTextComponents());
        }
    }

    /**
     * Broadcasts a message to all players.
     *
     * @param msg       The message to be broadcast.
     * @param translate Convert the color codes.
     */
    public static void broadcast(String msg, boolean translate) {
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            send(msg, player, translate);
        }
    }

    /**
     * Broadcasts a message to all players.
     *
     * @param msg The message to be broadcast.
     */
    public static void broadcast(String msg) {
        broadcast(msg, true);
    }

    /**
     * Broadcasts a message to all players.
     *
     * @param msg         The message to be broadcast.
     * @param sender      The sender of the message.
     * @param permissions Convert the color codes if the sender has this permissions.
     */
    public static void broadcast(String msg, ProxiedPlayer sender, String... permissions) {
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            send(msg, player, sender, permissions);
        }
    }

    /**
     * Broadcasts a message to all players.
     *
     * @param msg         The message to be broadcast.
     * @param sender      The sender of the message.
     * @param permissions Convert the color codes if the sender has this permissions.
     */
    public static void broadcast(String msg, CommandSender sender, String... permissions) {
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            send(msg, player, sender, permissions);
        }
    }

    /**
     * Broadcasts a message to all players except blacklisted ones.
     *
     * @param msg       The message to be broadcast.
     * @param translate Convert the color codes.
     * @param blacklist Players that will not receive the message.
     */
    public static void broadcast(String msg, boolean translate, ProxiedPlayer... blacklist) {
        List<ProxiedPlayer> players = new ArrayList<>(ProxyServer.getInstance().getPlayers());
        for (ProxiedPlayer player : blacklist) {
            players.removeIf(p -> p.getUniqueId().equals(player.getUniqueId()));
        }

        for (ProxiedPlayer player : players) {
            send(msg, player, translate);
        }
    }

    /**
     * Broadcasts a message to all players except blacklisted ones.
     *
     * @param msg       The message to be broadcast.
     * @param blacklist Players that will not receive the message.
     */
    public static void broadcast(String msg, ProxiedPlayer... blacklist) {
        broadcast(msg, true, blacklist);
    }

    /**
     * Broadcasts a message to all players in a server.
     *
     * @param msg       The message to be broadcast.
     * @param translate Convert the color codes.
     * @param server    The server target of the broadcast.
     */
    public static void broadcast(String msg, boolean translate, ServerInfo server) {
        for (ProxiedPlayer player : server.getPlayers()) {
            send(msg, player, translate);
        }
    }

    /**
     * Broadcasts a message to all players in a server.
     *
     * @param msg    The message to be broadcast.
     * @param server The server target of the broadcast.
     */
    public static void broadcast(String msg, ServerInfo server) {
        broadcast(msg, true, server);
    }

    /**
     * Broadcasts a message to all players except blacklisted ones.
     *
     * @param msg         The message to be broadcast.
     * @param translate   Convert the color codes.
     * @param permissions Players with this perm will receive the message.
     */
    public static void broadcast(String msg, boolean translate, String... permissions) {
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            if (hasPermission(player, permissions)) {
                send(msg, player, translate);
            }
        }
    }

    /**
     * Broadcasts a message to all players except blacklisted ones.
     *
     * @param msg         The message to be broadcast.
     * @param permissions Players with this perm will receive the message.
     */
    public static void broadcast(String msg, String... permissions) {
        broadcast(msg, true, permissions);
    }

    /**
     * Broadcasts a message to all players.
     *
     * @param msgs      The messages to be broadcast.
     * @param translate Convert the color codes.
     */
    public static void broadcast(String[] msgs, boolean translate) {
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            send(msgs, player, translate);
        }
    }

    /**
     * Broadcasts a message to all players.
     *
     * @param msgs The messages to be broadcast.
     */
    public static void broadcast(String[] msgs) {
        broadcast(msgs, true);
    }

    /**
     * Broadcasts a message to all players.
     *
     * @param msgs        The messages to be broadcast.
     * @param sender      The sender of The messages.
     * @param permissions Convert the color codes if the sender has this permissions.
     */
    public static void broadcast(String[] msgs, ProxiedPlayer sender, String... permissions) {
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            send(msgs, player, sender, permissions);
        }
    }

    /**
     * Broadcasts a message to all players.
     *
     * @param msgs        The messages to be broadcast.
     * @param sender      The sender of The messages.
     * @param permissions Convert the color codes if the sender has this permissions.
     */
    public static void broadcast(String[] msgs, CommandSender sender, String... permissions) {
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            send(msgs, player, sender, permissions);
        }
    }

    /**
     * Broadcasts a message to all players except blacklisted ones.
     *
     * @param msgs      The messages to be broadcast.
     * @param translate Convert the color codes.
     * @param blacklist Players that will not receive The messages.
     */
    public static void broadcast(String[] msgs, boolean translate, ProxiedPlayer... blacklist) {
        List<ProxiedPlayer> players = ProxyServer.getInstance().getPlayers().stream().filter(p -> {
            for (ProxiedPlayer player : blacklist) {
                if (player.getUniqueId().equals(p.getUniqueId())) return false;
            }

            return true;
        }).collect(Collectors.toList());

        for (ProxiedPlayer player : players) {
            send(msgs, player, translate);
        }
    }

    /**
     * Broadcasts a message to all players except blacklisted ones.
     *
     * @param msgs      The messages to be broadcast.
     * @param blacklist Players that will not receive The messages.
     */
    public static void broadcast(String[] msgs, ProxiedPlayer... blacklist) {
        broadcast(msgs, true, blacklist);
    }

    /**
     * Broadcasts a message to all players in a server.
     *
     * @param msgs      The messages to be broadcast.
     * @param translate Convert the color codes.
     * @param server    The server target of the broadcast.
     */
    public static void broadcast(String[] msgs, boolean translate, ServerInfo server) {
        for (ProxiedPlayer player : server.getPlayers()) {
            send(msgs, player, translate);
        }
    }

    /**
     * Broadcasts a message to all players in a server.
     *
     * @param msgs   The messages to be broadcast.
     * @param server The server target of the broadcast.
     */
    public static void broadcast(String[] msgs, ServerInfo server) {
        broadcast(msgs, true, server);
    }

    /**
     * Broadcasts a message to all players except blacklisted ones.
     *
     * @param msgs        The messages to be broadcast.
     * @param translate   Convert the color codes.
     * @param permissions Players with this perm will receive The messages.
     */
    public static void broadcast(String[] msgs, boolean translate, String... permissions) {
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            if (hasPermission(player, permissions)) {
                send(msgs, player, translate);
            }
        }
    }

    /**
     * Broadcasts a message to all players except blacklisted ones.
     *
     * @param msgs        The messages to be broadcast.
     * @param permissions Players with this perm will receive The messages.
     */
    public static void broadcast(String[] msgs, String... permissions) {
        broadcast(msgs, true, permissions);
    }

    /**
     * Broadcasts a message to all players.
     *
     * @param msgs      The messages to be broadcast.
     * @param translate Convert the color codes.
     */
    public static void broadcast(List<String> msgs, boolean translate) {
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            send(msgs, player, translate);
        }
    }

    /**
     * Broadcasts a message to all players.
     *
     * @param msgs The messages to be broadcast.
     */
    public static void broadcast(List<String> msgs) {
        broadcast(msgs, true);
    }

    /**
     * Broadcasts a message to all players.
     *
     * @param msgs        The messages to be broadcast.
     * @param sender      The sender of The messages.
     * @param permissions Convert the color codes if the sender has this permissions.
     */
    public static void broadcast(List<String> msgs, ProxiedPlayer sender, String... permissions) {
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            send(msgs, player, sender, permissions);
        }
    }

    /**
     * Broadcasts a message to all players.
     *
     * @param msgs        The messages to be broadcast.
     * @param sender      The sender of The messages.
     * @param permissions Convert the color codes if the sender has this permissions.
     */
    public static void broadcast(List<String> msgs, CommandSender sender, String... permissions) {
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            send(msgs, player, sender, permissions);
        }
    }

    /**
     * Broadcasts a message to all players except blacklisted ones.
     *
     * @param msgs      The messages to be broadcast.
     * @param translate Convert the color codes.
     * @param blacklist Players that will not receive The messages.
     */
    public static void broadcast(List<String> msgs, boolean translate, ProxiedPlayer... blacklist) {
        List<ProxiedPlayer> players = ProxyServer.getInstance().getPlayers().stream().filter(p -> {
            for (ProxiedPlayer player : blacklist) {
                if (player.getUniqueId().equals(p.getUniqueId())) return false;
            }

            return true;
        }).collect(Collectors.toList());

        for (ProxiedPlayer player : players) {
            send(msgs, player, translate);
        }
    }

    /**
     * Broadcasts a message to all players except blacklisted ones.
     *
     * @param msgs      The messages to be broadcast.
     * @param blacklist Players that will not receive The messages.
     */
    public static void broadcast(List<String> msgs, ProxiedPlayer... blacklist) {
        broadcast(msgs, true, blacklist);
    }

    /**
     * Broadcasts a message to all players in a server.
     *
     * @param msgs      The messages to be broadcast.
     * @param translate Convert the color codes.
     * @param server    The server target of the broadcast.
     */
    public static void broadcast(List<String> msgs, boolean translate, ServerInfo server) {
        for (ProxiedPlayer player : server.getPlayers()) {
            send(msgs, player, translate);
        }
    }

    /**
     * Broadcasts a message to all players in a server.
     *
     * @param msgs   The messages to be broadcast.
     * @param server The server target of the broadcast.
     */
    public static void broadcast(List<String> msgs, ServerInfo server) {
        broadcast(msgs, true, server);
    }

    /**
     * Broadcasts a message to all players except blacklisted ones.
     *
     * @param msgs        The messages to be broadcast.
     * @param translate   Convert the color codes.
     * @param permissions Players with this perm will receive The messages.
     */
    public static void broadcast(List<String> msgs, boolean translate, String... permissions) {
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            if (hasPermission(player, permissions)) {
                send(msgs, player, translate);
            }
        }
    }

    /**
     * Broadcasts a message to all players except blacklisted ones.
     *
     * @param msgs        The messages to be broadcast.
     * @param permissions Players with this perm will receive The messages.
     */
    public static void broadcast(List<String> msgs, String... permissions) {
        broadcast(msgs, true, permissions);
    }

    /**
     * Log a message to the console.
     *
     * @param msg   The message to log.
     * @param level The log level.
     */
    public static void log(String msg, Level level) {
        ProxyServer.getInstance().getLogger().log(level, getTranslated(msg));
    }

    /**
     * Log a message to the console.
     *
     * @param msg The message to log.
     */
    public static void info(String msg) {
        ProxyServer.getInstance().getLogger().info(getTranslated(msg));
    }

    /**
     * Log a message to the console.
     *
     * @param msg The message to log.
     */
    public static void warning(String msg) {
        ProxyServer.getInstance().getLogger().warning(getTranslated(msg));
    }

    /**
     * Log a message to the console.
     *
     * @param msg The message to log.
     */
    public static void severe(String msg) {
        ProxyServer.getInstance().getLogger().severe(getTranslated(msg));
    }

    private static boolean hasPermission(ProxiedPlayer player, String... permissions) {
        for (String perm : permissions) {
            if (player.hasPermission(perm)) return true;
        }

        return false;
    }
}
