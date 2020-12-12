package it.multicoredev.mbcore.spigot.util.chat;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ItemTag;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Item;
import net.md_5.bungee.api.chat.hover.content.Text;

import java.util.Arrays;

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
public class ChatHoverEvent {
    private String action;
    private JsonObject contents;

    public HoverEvent.Action getAction() {
        if (action.equalsIgnoreCase("show_text")) return HoverEvent.Action.SHOW_TEXT;
        else if (action.equalsIgnoreCase("show_item")) return HoverEvent.Action.SHOW_ITEM;
        else if (action.equalsIgnoreCase("show_entity")) return HoverEvent.Action.SHOW_ENTITY;
        else return null;
    }

    public Content getContents() {
        if (action.equalsIgnoreCase("show_text")) {
            return new Text(RawMessage.fromJson("{\"message\":[" + contents.toString() + "]}").toTextComponent());
        } else if (action.equalsIgnoreCase("show_item")) {
            return null;
            /*System.out.println(contents.toString());
            String id = contents.get("id").getAsString();
            JsonElement countElement = contents.get("Count");
            byte count = countElement != null ? countElement.getAsByte() : 1;
            JsonElement tagElement = contents.get("tag");
            System.out.println();
            return new Item(id, count, ItemTag.ofNbt(tagElement.getAsString()));*/
        } else if (action.equalsIgnoreCase("show_entity")) {
            return null;
        } else {
            return null;
        }
    }
}
