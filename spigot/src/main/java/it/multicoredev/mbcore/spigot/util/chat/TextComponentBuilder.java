package it.multicoredev.mbcore.spigot.util.chat;

import it.multicoredev.mbcore.spigot.Chat;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

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
public class TextComponentBuilder {
    private TextComponent component;

    public TextComponentBuilder(String text) {
        component = new TextComponent(TextComponent.fromLegacyText(Chat.getTranslated(text)));
    }

    public TextComponentBuilder() {
        this("");
    }

    public TextComponentBuilder setText(@NotNull String text) {
        Objects.requireNonNull(text);
        component.setText(Chat.getTranslated(text));
        return this;
    }

    public TextComponentBuilder setColor(@NotNull ChatColor color) {
        Objects.requireNonNull(color);
        component.setColor(color);
        return this;
    }

    public TextComponentBuilder setColor(@NotNull String color) {
        Objects.requireNonNull(color);
        component.setColor(ChatColor.of(color));
        return this;
    }

    public TextComponentBuilder setFont(@NotNull String font) {
        Objects.requireNonNull(font);
        component.setFont(font);
        return this;
    }

    public TextComponentBuilder setBold(boolean bold) {
        component.setBold(bold);
        return this;
    }

    public TextComponentBuilder setItalic(boolean italic) {
        component.setItalic(italic);
        return this;
    }

    public TextComponentBuilder setUnderlined(boolean underlined) {
        component.setUnderlined(underlined);
        return this;
    }

    public TextComponentBuilder setStrikethrough(boolean strikethrough) {
        component.setStrikethrough(strikethrough);
        return this;
    }

    public TextComponentBuilder setObfuscated(boolean obfuscated) {
        component.setObfuscated(obfuscated);
        return this;
    }

    public TextComponentBuilder setInsertion(String insertion) {
        component.setInsertion(insertion);
        return this;
    }

    public TextComponentBuilder addExtra(BaseComponent extra) {
        component.addExtra(extra);
        return this;
    }

    public TextComponentBuilder addExtra(String extra) {
        component.addExtra(extra);
        return this;
    }

    public TextComponentBuilder setClickEvent(ClickEvent event) {
        component.setClickEvent(event);
        return this;
    }

    public TextComponentBuilder setHoverEvent(HoverEvent event) {
        component.setHoverEvent(event);
        return this;
    }

    public TextComponent get() {
        return component;
    }
}
