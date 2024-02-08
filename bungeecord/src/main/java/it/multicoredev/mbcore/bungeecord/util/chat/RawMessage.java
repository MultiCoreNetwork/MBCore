package it.multicoredev.mbcore.bungeecord.util.chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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
public class RawMessage {
    public static final Gson PARSER = new GsonBuilder()
            .registerTypeAdapterFactory(ArrayTypeAdapter.FACTORY)
            .registerTypeAdapter(TextComponent.class, new TellRawDeserializer())
            .registerTypeAdapter(TextComponent.class, new TellRawSerializer())
            .registerTypeAdapter(ClickEvent.class, new TellRawDeserializer.ClickDeserializer())
            .registerTypeAdapter(ClickEvent.class, new TellRawSerializer.ClickSerializer())
            .registerTypeAdapter(HoverEvent.class, new TellRawDeserializer.HoverDeserializer())
            .registerTypeAdapter(HoverEvent.class, new TellRawSerializer.HoverSerializer())
            .create();

    private final String json;
    private final List<TextComponent> components = new LinkedList<>();

    /**
     * Representation of a raw message.
     *
     * @param json Json representing the message using the Minecraft tellraw formatting.
     */
    public RawMessage(@NotNull String json) {
        Objects.requireNonNull(json);
        this.json = json;

        if (json.startsWith("[") && json.endsWith("]")) {
            components.addAll(Arrays.asList(PARSER.fromJson(json, TextComponent[].class)));
        } else {
            components.add(PARSER.fromJson(json, TextComponent.class));
        }
    }

    /**
     * Representation of a raw message.
     *
     * @param components One or more {@link TextComponent} composing the message.
     */
    public RawMessage(@NotNull TextComponent... components) {
        Objects.requireNonNull(components);
        this.components.addAll(Arrays.asList(components));

        if (components.length == 0) {
            json = "{}";
        } else if (components.length == 1) {
            json = PARSER.toJson(components[0]);
        } else {
            json = PARSER.toJson(components);
        }
    }

    /**
     * Append a {@link TextComponent} at the end of the message.
     *
     * @param component The component to append.
     * @return This class.
     */
    public RawMessage append(@NotNull TextComponent component) {
        Objects.requireNonNull(component);
        components.add(component);
        return this;
    }

    /**
     * Convert this object to a list of {@link TextComponent}.
     *
     * @return The list of {@link TextComponent}.
     */
    public TextComponent[] toTextComponents() {
        TextComponent[] tComponents = new TextComponent[components.size()];
        components.toArray(tComponents);

        return tComponents;
    }

    /**
     * Get this message as a json string.
     *
     * @return The json string representing this message.
     */
    public String getJson() {
        return json;
    }
}
