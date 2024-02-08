package it.multicoredev.mbcore.bungeecord.util.chat;

import com.google.gson.*;
import it.multicoredev.mbcore.bungeecord.Chat;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.lang.reflect.Type;

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
public class TellRawDeserializer implements JsonDeserializer<TextComponent> {

    public TextComponent deserialize(JsonElement json, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        if (!json.isJsonObject()) return new TextComponent(json.getAsJsonPrimitive().getAsString());

        JsonObject obj = json.getAsJsonObject();

        TextComponent component = new TextComponent(TextComponent.fromLegacyText(Chat.getTranslated(obj.get("text").getAsString())));
        if (obj.has("color")) component.setColor(ChatColor.of(obj.get("color").getAsString()));
        if (obj.has("font")) component.setFont(obj.get("font").getAsString());
        if (obj.has("bold")) component.setBold(obj.get("bold").getAsBoolean());
        if (obj.has("italic")) component.setItalic(obj.get("italic").getAsBoolean());
        if (obj.has("underlined")) component.setUnderlined(obj.get("underlined").getAsBoolean());
        if (obj.has("strikethrough")) component.setStrikethrough(obj.get("strikethrough").getAsBoolean());
        if (obj.has("obfuscated")) component.setObfuscated(obj.get("obfuscated").getAsBoolean());
        if (obj.has("insertion")) component.setInsertion(obj.get("insertion").getAsString());

        if (obj.has("clickEvent")) {
            JsonObject event = obj.getAsJsonObject("clickEvent");
            component.setClickEvent(ctx.deserialize(event, ClickEvent.class));
        }

        if (obj.has("hoverEvent")) {
            JsonObject event = obj.getAsJsonObject("hoverEvent");
            component.setHoverEvent(ctx.deserialize(event, HoverEvent.class));
        }

        if (obj.has("extra")) {
            JsonArray extras = obj.getAsJsonArray("extra");
            extras.forEach(extra -> component.addExtra((BaseComponent) ctx.deserialize(extra, TextComponent.class)));
        }

        return component;
    }

    public static class ClickDeserializer implements JsonDeserializer<ClickEvent> {

        public ClickEvent deserialize(JsonElement json, Type type, JsonDeserializationContext ctx) throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            JsonPrimitive action = obj.getAsJsonPrimitive("action");
            JsonPrimitive value = obj.getAsJsonPrimitive("value");

            return new ClickEvent(ClickEvent.Action.valueOf(action.getAsString().toUpperCase()), value.getAsString());
        }
    }

    @SuppressWarnings("deprecation")
    public static class HoverDeserializer implements JsonDeserializer<HoverEvent> {

        public HoverEvent deserialize(JsonElement json, Type type, JsonDeserializationContext ctx) throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            JsonPrimitive action = obj.getAsJsonPrimitive("action");
            TextComponent value = ctx.deserialize(obj.getAsJsonObject("contents"), TextComponent.class);

            BaseComponent[] extras = new BaseComponent[value.getExtra().size()];
            value.getExtra().toArray(extras);

            return new HoverEvent(HoverEvent.Action.valueOf(action.getAsString().toUpperCase()), extras);
        }
    }
}
