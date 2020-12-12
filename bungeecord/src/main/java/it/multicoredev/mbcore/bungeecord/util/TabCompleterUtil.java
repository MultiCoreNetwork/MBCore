package it.multicoredev.mbcore.bungeecord.util;

import de.myzelyam.api.vanish.BungeeVanishAPI;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
public class TabCompleterUtil {

    /**
     * Get the list of player names starting with the searched characters.
     *
     * @param search       The starting characters of the names searched.
     *                     If null or empty all player names will be returned.
     * @param showVanished Choose to show vanished players (support Premiumvanish)
     * @return A list of player names.
     */
    public static Set<String> getPlayers(@Nullable String search, boolean showVanished) {
        Set<String> players = new HashSet<>();

        if (search == null || search.trim().isEmpty()) {
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                if (isVanished(player) && !showVanished) continue;
                players.add(player.getName());
            }
        } else {
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                if (isVanished(player) && !showVanished) continue;
                if (player.getName().toLowerCase().startsWith(search.toLowerCase())) players.add(player.getName());
            }
        }

        return players;
    }

    /**
     * Get the list of player names starting with the searched characters.
     *
     * @param search The starting characters of the names searched.
     *               If null or empty all player names will be returned.
     * @return A list of player names.
     */
    public static Set<String> getPlayers(@Nullable String search) {
        return getPlayers(search, true);
    }

    /**
     * Get the list of player display names starting with the searched characters.
     *
     * @param search       The starting characters of the names searched.
     *                     If null or empty all player names will be returned.
     * @param showVanished Choose to show vanished players (support Premiumvanish)
     * @return A list of player display names.
     */
    public static Set<String> getDisplayNames(@Nullable String search, boolean showVanished) {
        Set<String> players = new HashSet<>();

        if (search == null || search.isEmpty()) {
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                if (isVanished(player) && !showVanished) continue;
                players.add(player.getDisplayName());
            }
        } else {
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                if (isVanished(player) && !showVanished) continue;
                if (player.getDisplayName().toLowerCase().startsWith(search.toLowerCase()))
                    players.add(player.getDisplayName());
            }
        }

        return players;
    }

    /**
     * Get the list of player display names starting with the searched characters.
     *
     * @param search The starting characters of the names searched.
     *               If null or empty all player names will be returned.
     * @return A list of player display names.
     */
    public static Set<String> getDisplayNames(@Nullable String search) {
        return getDisplayNames(search, true);
    }

    /**
     * Get the list of servers starting with the searched characters.
     *
     * @param search The starting characters of the names of the server.
     *               If null or empty all server names will be returned.
     * @return A list of server names.
     */
    public static Set<String> getServers(@Nullable String search) {
        Set<String> servers = new HashSet<>();

        if (search == null || search.trim().isEmpty()) {
            servers.addAll(ProxyServer.getInstance().getServers().keySet());
        } else {
            for (String server : ProxyServer.getInstance().getServers().keySet()) {
                if (server.toLowerCase().startsWith(search.toLowerCase())) servers.add(server);
            }
        }

        return servers;
    }

    /**
     * Get a list of completions starting with the searched characters.
     *
     * @param search      The starting characters of the completions searched.
     *                    If null or empty all completions will be returned.
     * @param completions A list of completions.
     * @return A list of completions.
     */
    public static Set<String> getCompletions(@Nullable String search, @NotNull String... completions) {
        Objects.requireNonNull(completions);
        if (search == null || search.trim().isEmpty()) return new HashSet<>(Arrays.asList(completions));
        Set<String> matches = new HashSet<>();

        for (String completion : completions) {
            if (!completion.toLowerCase().startsWith(search.toLowerCase())) continue;
            matches.add(completion);
        }

        return matches;
    }

    /**
     * Check if a player is vanished using Premiumvanish.
     *
     * @param player The player to check.
     * @return true if the player is vanished, false if he's not.
     */
    public static boolean isVanished(@NotNull ProxiedPlayer player) {
        Objects.requireNonNull(player);
        return BungeeVanishAPI.isInvisible(player);
    }
}
