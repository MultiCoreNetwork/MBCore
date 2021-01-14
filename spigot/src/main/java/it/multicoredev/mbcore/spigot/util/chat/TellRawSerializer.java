package it.multicoredev.mbcore.spigot.util.chat;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.lang.reflect.Type;

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
public class TellRawSerializer implements JsonSerializer<TextComponent> {

    public JsonElement serialize(TextComponent component, Type type, JsonSerializationContext ctx) {
        JsonObject json = new JsonObject();
        json.addProperty("text", component.getText());
        json.addProperty("color", component.getColor().getName());
        json.addProperty("font", component.getFont());
        json.addProperty("bold", component.isBold());
        json.addProperty("italic", component.isItalic());
        json.addProperty("underlined", component.isUnderlined());
        json.addProperty("strikethrough", component.isStrikethrough());
        json.addProperty("obfuscated", component.isObfuscated());
        json.addProperty("insertion", component.getInsertion());


        return json;
    }

    public static class ClickSerializer implements JsonSerializer<ClickEvent> {

        public JsonElement serialize(ClickEvent event, Type type, JsonSerializationContext ctx) {
            JsonObject json = new JsonObject();
            json.addProperty("action", event.getAction().name().toLowerCase());
            json.addProperty("value", event.getValue());

            return json;
        }
    }

    public static class HoverSerializer implements JsonSerializer<HoverEvent> {

        public JsonElement serialize(HoverEvent event, Type type, JsonSerializationContext ctx) {
            JsonObject json = new JsonObject();
            json.addProperty("action", event.getAction().name().toLowerCase());

            return json;
        }
    }
}
