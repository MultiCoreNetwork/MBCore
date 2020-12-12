package it.multicoredev.mbcore.spigot.util.chat;

import it.multicoredev.mbcore.spigot.Chat;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

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
public class ChatComponent {
    private String text;
    private String color = null;
    private Boolean bold = null;
    private Boolean italic = null;
    private Boolean underlined = null;
    private Boolean strikethrough = null;
    private Boolean obfuscated = null;
    private ChatClickEvent clickEvent = null;
    private ChatHoverEvent hoverEvent = null;

    /**
     * Container of a {@link RawMessage} section defined by a text, color, alterations and events.
     *
     * @param text The text of this component. (Accepts color codes)
     */
    public ChatComponent(String text) {
        this.text = text;
    }

    /**
     * Set the text of the component. (Accepts color codes)
     *
     * @param text The text of the component.
     * @return This object.
     */
    public ChatComponent setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * Set the color of the component.
     *
     * @param color The color of the component.
     * @return This object.
     */
    public ChatComponent setColor(String color) {
        this.color = color;
        return this;
    }

    /**
     * Set if the text is bold.
     *
     * @param bold True if the text should be bold.
     * @return This object.
     */
    public ChatComponent setBold(Boolean bold) {
        this.bold = bold;
        return this;
    }

    /**
     * Set if the text is italic.
     *
     * @param italic True if the text should be italic.
     * @return This object.
     */
    public ChatComponent setItalic(Boolean italic) {
        this.italic = italic;
        return this;
    }

    /**
     * Set if the text is underlined.
     *
     * @param underlined True if the text should be underlined.
     * @return This object.
     */
    public ChatComponent setUnderlined(Boolean underlined) {
        this.underlined = underlined;
        return this;
    }

    /**
     * Set if the text is strikethrough.
     *
     * @param strikethrough True if the text should be strikethrough.
     * @return This object.
     */
    public ChatComponent setStrikethrough(Boolean strikethrough) {
        this.strikethrough = strikethrough;
        return this;
    }

    /**
     * Set if the text is obfuscated.
     *
     * @param obfuscated True if the text should be obfuscated.
     * @return This object.
     */
    public ChatComponent setObfuscated(Boolean obfuscated) {
        this.obfuscated = obfuscated;
        return this;
    }

    /**
     * Set a {@link ClickEvent} for this component.
     *
     * @param clickEvent The {@link ChatClickEvent}.
     * @return This object.
     */
    public ChatComponent setClickEvent(ChatClickEvent clickEvent) {
        this.clickEvent = clickEvent;
        return this;
    }

    /**
     * Set a {@link HoverEvent} for this component.
     *
     * @param hoverEvent The {@link ChatHoverEvent}.
     * @return This object.
     */
    public ChatComponent setHoverEvent(ChatHoverEvent hoverEvent) {
        this.hoverEvent = hoverEvent;
        return this;
    }

    /**
     * Convert this component to a {@link TextComponent}.
     *
     * @return The {@link TextComponent} containing this properties.
     */
    public TextComponent getTextComponent() {
        TextComponent component = new TextComponent(TextComponent.fromLegacyText(Chat.getTranslated(text)));
        if (color != null) component.setColor(ChatColor.of(color));
        if (bold != null) component.setBold(bold);
        if (italic != null) component.setItalic(italic);
        if (underlined != null) component.setUnderlined(underlined);
        if (strikethrough != null) component.setUnderlined(strikethrough);
        if (obfuscated != null) component.setObfuscated(obfuscated);

        if (clickEvent != null && clickEvent.getAction() != null && clickEvent.getValue() != null) {
            component.setClickEvent(new ClickEvent(clickEvent.getAction(), clickEvent.getValue()));
        }

        if (hoverEvent != null && hoverEvent.getAction() != null && hoverEvent.getContents() != null) {
            component.setHoverEvent(new HoverEvent(hoverEvent.getAction(), hoverEvent.getContents()));
        }

        return component;
    }
}
