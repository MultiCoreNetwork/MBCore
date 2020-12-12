package it.multicoredev.mbcore.bungeecord.util.chat;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
public class RawMessage {
    private List<ChatComponent> message;

    /**
     * Container of a raw message.
     *
     * @param message One or more {@link ChatComponent} that compose the message.
     */
    public RawMessage(ChatComponent... message) {
        this.message = new LinkedList<>(Arrays.asList(message));
    }

    /**
     * Container of a raw message.
     */
    public RawMessage() {
        message = new LinkedList<>();
    }

    /**
     * Append a {@link ChatComponent} to the end of the message.
     *
     * @param component The component to be appended.
     * @return This object.
     */
    public RawMessage append(ChatComponent component) {
        message.add(component);
        return this;
    }

    /**
     * Convert this object to an array of {@link net.md_5.bungee.api.chat.TextComponent}.
     *
     * @return An array of {@link net.md_5.bungee.api.chat.TextComponent}.
     */
    public BaseComponent[] toTextComponent() {
        ComponentBuilder builder = new ComponentBuilder();
        for (ChatComponent component : message) {
            builder.append(component.getTextComponent());
        }
        return builder.create();
    }

    /**
     * Get an instance of this class from a json string.
     * Keep in mind that the Minecraft format used for example in tellraws is not a valid json.
     * Look {@link it.multicoredev.mbcore.bungeecord.Chat#sendRaw(String, ProxiedPlayer)} for more info on how to convert
     * Minecraft format to valid json.
     *
     * @param jsonMsg The json representing the RawMessage.
     * @return An instance of this class.
     * @throws JsonSyntaxException Thrown when the passed json is invalid.
     */
    public static RawMessage fromJson(String jsonMsg) throws JsonSyntaxException {
        return new Gson().fromJson(jsonMsg, RawMessage.class);
    }
}
