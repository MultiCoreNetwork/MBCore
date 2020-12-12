package it.multicoredev.mbcore.spigot.util.chat;

import net.md_5.bungee.api.chat.ClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
public class ChatClickEvent {
    private String action;
    private String value;

    /**
     * A json parsable representation of the {@link ClickEvent}.
     *
     * @param action The name of the action to be performed.
     * @param value  The value of the action.
     */
    public ChatClickEvent(@NotNull String action, @NotNull String value) {
        this.action = action;
        this.value = value;
    }

    /**
     * A json parsable representation of the {@link ClickEvent}.
     *
     * @param action The {@link net.md_5.bungee.api.chat.ClickEvent.Action} to be performed.
     * @param value  The value of the action.
     */
    public ChatClickEvent(@NotNull ClickEvent.Action action, @NotNull String value) {
        this.action = action.name().toLowerCase();
        this.value = value;
    }

    /**
     * Get the {@link net.md_5.bungee.api.chat.ClickEvent.Action} to be performed.
     *
     * @return The {@link net.md_5.bungee.api.chat.ClickEvent.Action} to be performed.
     */
    @Nullable
    public ClickEvent.Action getAction() {
        if (action.equalsIgnoreCase("open_url")) return ClickEvent.Action.OPEN_URL;
        else if (action.equalsIgnoreCase("run_command")) return ClickEvent.Action.RUN_COMMAND;
        else if (action.equalsIgnoreCase("suggest_command")) return ClickEvent.Action.SUGGEST_COMMAND;
        else if (action.equalsIgnoreCase("copy_to_clipboard")) return ClickEvent.Action.COPY_TO_CLIPBOARD;
        else return null;
    }

    /**
     * Get the value of the action.
     *
     * @return The value of the action.
     */
    public String getValue() {
        return value;
    }
}
