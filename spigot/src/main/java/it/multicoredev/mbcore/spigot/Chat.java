package it.multicoredev.mbcore.spigot;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class Chat {
    private final static Pattern hexColorPattern = Pattern.compile("#([A-Fa-f0-9]{6})");
    private static Boolean useAllColors = null;

    private static String translateHex(String msg) {
        Validate.notNull(msg, "Cannot translate null text");

        if (useAllColors == null) {
            useAllColors = Integer.parseInt(Bukkit.getBukkitVersion().split("\\.")[1]) >= 16;
        }

        if (!useAllColors) return msg;

        Matcher matcher = hexColorPattern.matcher(msg);
        while (matcher.find()) {
            String match = matcher.group(0);
            msg = msg.replace(match, net.md_5.bungee.api.ChatColor.of(match) + "");
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
    public static String getTranslated(String msg, Player sender, String... permissions) {
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
        if (sender instanceof Player) return getTranslated(msg, (Player) sender, permissions);
        return getTranslated(msg);
    }

    /**
     * Remove all '&amp;' color codes.
     *
     * @param msg The message to convert.
     * @return The converted message.
     */
    public static String getDiscolored(String msg) {
        Preconditions.checkNotNull(msg, "Cannot translate null text");

        msg = msg.replaceAll("&[0-9a-fA-FkKlLmMnNoOrR]", "");

        Matcher matcher = hexColorPattern.matcher(msg);
        while (matcher.find()) {
            String match = matcher.group(0);
            msg = msg.replace(match, "");
        }

        return msg;
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
    public static void send(String msg, Player receiver, boolean translate) {
        receiver.sendMessage(translate ? getTranslated(msg) : msg);
    }

    /**
     * Send a message to a player.
     *
     * @param msg      The message to be sent.
     * @param receiver The receiver of the message.
     */
    public static void send(String msg, Player receiver) {
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
    public static void send(String msg, Player receiver, Player sender, String... permissions) {
        receiver.sendMessage(getTranslated(msg, sender, permissions));
    }

    /**
     * Send a message to a player.
     *
     * @param msg         The message to be sent.
     * @param receiver    The receiver of the message.
     * @param sender      The sender of the message.
     * @param permissions Convert the color codes if the sender has this permissions.
     */
    public static void send(String msg, Player receiver, CommandSender sender, String... permissions) {
        receiver.sendMessage(getTranslated(msg, sender, permissions));
    }

    /**
     * Send a message to a player.
     *
     * @param msg       The message to be sent.
     * @param receiver  The receiver of the message.
     * @param translate Convert the color codes.
     */
    public static void send(String msg, CommandSender receiver, boolean translate) {
        receiver.sendMessage(translate ? getTranslated(msg) : msg);
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
    public static void send(String msg, CommandSender receiver, Player sender, String... permissions) {
        receiver.sendMessage(getTranslated(msg, sender, permissions));
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
        receiver.sendMessage(getTranslated(msg, sender, permissions));
    }

    /**
     * Broadcast a message to all players.
     *
     * @param msg       The message to be broadcast.
     * @param translate Convert the color codes.
     */
    public static void broadcast(String msg, boolean translate) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            send(msg, player, translate);
        }
    }

    /**
     * Broadcast a message to all players.
     *
     * @param msg The message to be broadcast.
     */
    public static void broadcast(String msg) {
        broadcast(msg, true);
    }

    /**
     * Broadcast a message to all players.
     *
     * @param msg         The message to be broadcast.
     * @param sender      The sender of the message.
     * @param permissions Convert the color codes if the sender has this permissions.
     */
    public static void broadcast(String msg, Player sender, String... permissions) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            send(msg, player, sender, permissions);
        }
    }

    /**
     * Broadcast a message to all players.
     *
     * @param msg         The message to be broadcast.
     * @param sender      The sender of the message.
     * @param permissions Convert the color codes if the sender has this permissions.
     */
    public static void broadcast(String msg, CommandSender sender, String... permissions) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            send(msg, player, sender, permissions);
        }
    }

    /**
     * Broadcast a message to all players except blacklisted ones.
     *
     * @param msg       The message to be broadcast.
     * @param translate Convert the color codes.
     * @param blacklist Players that will not receive the message.
     */
    public static void broadcast(String msg, boolean translate, Player... blacklist) {
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        for (Player player : blacklist) {
            players.removeIf(p -> p.getUniqueId().equals(player.getUniqueId()));
        }

        for (Player player : players) {
            send(msg, player, translate);
        }
    }

    /**
     * Broadcast a message to all players except blacklisted ones.
     *
     * @param msg       The message to be broadcast.
     * @param blacklist Players that will not receive the message.
     */
    public static void broadcast(String msg, Player... blacklist) {
        broadcast(msg, true, blacklist);
    }

    /**
     * Broadcast a message to all players except blacklisted ones.
     *
     * @param msg         The message to be broadcast.
     * @param translate   Convert the color codes.
     * @param permissions Players with this perm will receive the message.
     */
    public static void broadcast(String msg, boolean translate, String... permissions) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (hasPermission(player, permissions)) {
                send(msg, player, translate);
            }
        }
    }

    /**
     * Broadcast a message to all players except blacklisted ones.
     *
     * @param msg         The message to be broadcast.
     * @param permissions Players with this perm will receive the message.
     */
    public static void broadcast(String msg, String... permissions) {
        broadcast(msg, true, permissions);
    }

    /**
     * Log a message to the console.
     *
     * @param msg   The message to log.
     * @param level The log level.
     */
    public static void log(String msg, Level level) {
        Bukkit.getLogger().log(level, getTranslated(msg));
    }

    /**
     * Log a message to the console.
     *
     * @param msg The message to log.
     */
    public static void info(String msg) {
        Bukkit.getLogger().info(getTranslated(msg));
    }

    /**
     * Log a message to the console.
     *
     * @param msg The message to log.
     */
    public static void warning(String msg) {
        Bukkit.getLogger().warning(getTranslated(msg));
    }

    /**
     * Log a message to the console.
     *
     * @param msg The message to log.
     */
    public static void severe(String msg) {
        Bukkit.getLogger().severe(getTranslated(msg));
    }

    private static boolean hasPermission(Player player, String... permissions) {
        for (String perm : permissions) {
            if (player.hasPermission(perm)) return true;
        }

        return false;
    }
}
