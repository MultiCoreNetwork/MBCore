package it.multicoredev.mbcore.velocity;

import com.google.common.base.Preconditions;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * BSD 3-Clause License
 * <p>
 * Copyright (c) 2016 - 2024, Lorenzo Magni
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
public class Text {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer legacySerializer = LegacyComponentSerializer.legacyAmpersand();
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)[§&][0-9A-FK-ORX]");

    private static Text instance = null;
    private final ProxyServer server;

    private Text(ProxyServer server) {
        this.server = server;
    }

    /**
     * Retrieves the existing instance of the Text class, if it has been created.
     *
     * @return The existing Text instance, or null if no instance has been created yet.
     */
    public static Text get() {
        return instance;
    }

    /**
     * Creates an instance of the Text class with the provided Plugin.
     *
     * @param server The {@link ProxyServer} instance.
     * @return The created Text instance.
     * @throws IllegalStateException if a Text instance has already been created. To create a new instance,
     *                               you must destroy the previous instance first.
     */
    public static Text create(ProxyServer server) {
        if (instance != null) throw new IllegalStateException("Text instance already created. Destroy the previous instance first.");
        instance = new Text(server);
        return instance;
    }

    /**
     * Concatenates elements of a string array into a single string starting from a specific offset.
     *
     * @param args   The array of strings to concatenate.
     * @param offset The starting index for concatenation.
     * @return A string containing elements of the array concatenated from the specified offset, separated by a space.
     * null if the array is null, or an empty string if the array is empty or the offset is greater than or equal to the array's length.
     * @throws IllegalArgumentException if the offset is negative.
     */
    public static String join(String[] args, int offset) {
        if (offset < 0) throw new IllegalArgumentException("Offset cannot be negative");
        if (args == null) return null;
        if (offset > args.length) return "";
        if (args.length == 0) return "";

        StringBuilder builder = new StringBuilder();
        for (; offset < args.length; offset++) {
            builder.append(args[offset]);
            if (offset < args.length - 1) builder.append(" ");
        }

        return builder.toString();
    }

    /**
     * Concatenates elements of a string array into a single string.
     *
     * @param args The array of strings to concatenate.
     * @return A string containing elements of the array concatenated from the specified offset, separated by a space.
     * null if the array is null, or an empty string if the array is empty.
     */
    public static String join(String[] args) {
        return join(args, 0);
    }

    /**
     * Removes all known tags in the input MiniMessage text, so that they are ignored in deserialization.
     *
     * @param text        The text with tags to remove.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @return The text without tags. null if the input text is null.
     */
    public static String stripFormatting(String text, TagResolver tagResolver) {
        if (text == null) return null;
        if (tagResolver == null) return miniMessage.stripTags(text);
        return miniMessage.stripTags(text, tagResolver);
    }

    /**
     * Removes all known tags in the input MiniMessage text, so that they are ignored in deserialization.
     *
     * @param text The text with tags to remove.
     * @return The text without tags. null if the input text is null.
     */
    public static String stripFormatting(String text) {
        return stripFormatting(text, null);
    }

    /**
     * Removes all known tags in the input MiniMessage texts, so that they are ignored in deserialization.
     *
     * @param texts       The texts with tags to remove.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @return An array of texts without tags or an empty array if no texts are provided.
     * null if the input texts array is null.
     */
    public static String[] stripFormatting(String[] texts, TagResolver tagResolver) {
        if (texts == null) return null;
        if (texts.length == 0) return new String[]{};

        for (int i = 0; i < texts.length; i++) {
            texts[i] = stripFormatting(texts[i], tagResolver);
        }

        return texts;
    }

    /**
     * Removes all known tags in the input MiniMessage texts, so that they are ignored in deserialization.
     *
     * @param texts The texts with tags to remove.
     * @return An array of texts without tags or an empty array if no texts are provided.
     * null if the input texts array is null.
     */
    public static String[] stripFormatting(String[] texts) {
        return stripFormatting(texts, null);
    }

    /**
     * Removes all known tags in the input MiniMessage texts, so that they are ignored in deserialization.
     *
     * @param collection  The collection of texts with tags to remove.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param <C>         The type of the collection.
     * @return A collection of texts without tags or an empty iterable if no texts are provided.
     * null if the input iterable is null.
     */
    public static <C extends Collection<String>> C stripFormatting(C collection, TagResolver tagResolver) {
        if (collection == null) return null;

        List<String> texts = new ArrayList<>();
        for (String text : collection) {
            texts.add(stripFormatting(text, tagResolver));
        }

        collection.clear();
        collection.addAll(texts);

        return collection;
    }

    /**
     * Removes all known tags in the input MiniMessage texts, so that they are ignored in deserialization.
     *
     * @param collection The collection of texts with tags to remove.
     * @param <C>        The type of the collection.
     * @return A collection of texts without tags or an empty iterable if no texts are provided.
     * null if the input iterable is null.
     */
    public static <C extends Collection<String>> C stripFormatting(C collection) {
        return stripFormatting(collection, null);
    }

    /**
     * Removes all legacy formatting tag in the input text, so that they are ignored in deserialization.
     *
     * @param text The text with tags to remove.
     * @return The text without tags. null if the input text is null.
     */
    public static String stripLegacyFormatting(String text) {
        if (text == null) return null;

        Matcher matcher = STRIP_COLOR_PATTERN.matcher(text);
        return matcher.replaceAll("");
    }

    /**
     * Removes all legacy formatting tag in the input texts, so that they are ignored in deserialization.
     *
     * @param texts The texts with tags to remove.
     * @return An array of texts without tags or an empty array if no texts are provided.
     * null if the input texts array is null.
     */
    public static String[] stripLegacyFormatting(String[] texts) {
        if (texts == null) return null;

        for (int i = 0; i < texts.length; i++) {
            texts[i] = stripLegacyFormatting(texts[i]);
        }

        return texts;
    }

    /**
     * Removes all legacy formatting tag in the input texts, so that they are ignored in deserialization.
     *
     * @param collection The collection of texts with tags to remove.
     * @param <C>        The type of the collection.
     * @return A collection of texts without tags or an empty iterable if no texts are provided.
     * null if the input iterable is null.
     */
    public static <C extends Collection<String>> C stripLegacyFormatting(C collection) {
        if (collection == null) return null;

        List<String> texts = new ArrayList<>();

        for (String text : collection) {
            texts.add(stripLegacyFormatting(text));
        }

        collection.clear();
        collection.addAll(texts);

        return collection;
    }

    /**
     * Serializes a text to a legacy ampersand string.
     *
     * @param text        The text to serialize.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @return The serialized text.
     * null if the input text is null.
     */
    public static String toLegacyText(String text, TagResolver tagResolver) {
        if (text == null) return null;

        if (tagResolver == null) return legacySerializer.serialize(miniMessage.deserialize(text));
        return legacySerializer.serialize(miniMessage.deserialize(text, tagResolver));
    }

    /**
     * Serializes a text to a legacy ampersand string.
     *
     * @param text The text to serialize.
     * @return The serialized text.
     * null if the input text is null.
     */
    public static String toLegacyText(String text) {
        return toLegacyText(text, null);
    }

    /**
     * Serializes texts to a legacy ampersand string.
     *
     * @param texts       The texts to serialize.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @return An array of serialized texts or an empty array if no texts are provided.
     * null if the input texts array is null.
     */
    public static String[] toLegacyText(String[] texts, TagResolver tagResolver) {
        if (texts == null) return null;

        for (int i = 0; i < texts.length; i++) {
            texts[i] = toLegacyText(texts[i], tagResolver);
        }

        return texts;
    }

    /**
     * Serializes texts to a legacy ampersand string.
     *
     * @param texts The texts to serialize.
     * @return An array of serialized texts or an empty array if no texts are provided.
     * null if the input texts array is null.
     */
    public static String[] toLegacyText(String[] texts) {
        return toLegacyText(texts, null);
    }

    /**
     * Serializes texts to a legacy ampersand string.
     *
     * @param collection  The collection of texts to serialize.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param <C>         The type of the collection.
     * @return A collection of serialized texts or an empty iterable if no texts are provided.
     * null if the input iterable is null.
     */
    public static <C extends Collection<String>> C toLegacyText(C collection, TagResolver tagResolver) {
        if (collection == null) return null;

        List<String> texts = new ArrayList<>();

        for (String text : collection) {
            texts.add(toLegacyText(text, tagResolver));
        }

        collection.clear();
        collection.addAll(texts);

        return collection;
    }

    /**
     * Serializes texts to a legacy ampersand string.
     *
     * @param collection The collection of texts to serialize.
     * @param <C>        The type of the collection.
     * @return A collection of serialized texts or an empty iterable if no texts are provided.
     * null if the input iterable is null.
     */
    public static <C extends Collection<String>> C toLegacyText(C collection) {
        return toLegacyText(collection, null);
    }

    /**
     * Converts a legacy text to a MiniMessage text.
     *
     * @param text The text to convert.
     * @return the converted text or null if the input text is null.
     */
    public static String toMiniMessage(String text) {
        if (text == null) return null;
        return miniMessage.serialize(legacySerializer.deserialize(text));
    }

    /**
     * Deserializes a MiniMessage text.
     *
     * @param text        The text to deserialize.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @return The deserialized text.
     * null if the input text is null.
     */
    public static Component deserialize(String text, TagResolver tagResolver) {
        if (text == null) return null;
        if (tagResolver == null) return miniMessage.deserialize(text);
        return miniMessage.deserialize(text, tagResolver);
    }

    /**
     * Deserializes a MiniMessage text.
     *
     * @param text The text to deserialize.
     * @return The deserialized text.
     * null if the input text is null.
     */
    public static Component deserialize(String text) {
        return deserialize(text, null);
    }

    /**
     * Deserializes MiniMessage texts.
     *
     * @param texts       The texts to deserialize.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @return An array of deserialized texts or an empty array if no texts are provided.
     * null if the input texts array is null.
     */
    public static Component[] deserialize(String[] texts, TagResolver tagResolver) {
        if (texts == null) return null;

        Component[] components = new Component[texts.length];

        for (int i = 0; i < texts.length; i++) {
            components[i] = deserialize(texts[i], tagResolver);
        }

        return components;
    }

    /**
     * Deserializes MiniMessage texts.
     *
     * @param texts The texts to deserialize.
     * @return An array of deserialized texts or an empty array if no texts are provided.
     * null if the input texts array is null.
     */
    public static Component[] deserialize(String[] texts) {
        return deserialize(texts, null);
    }

    /**
     * Deserializes MiniMessage texts.
     *
     * @param collection  The collection of texts to deserialize.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param <C>         The type of the collection.
     * @return A collection of deserialized texts or an empty iterable if no texts are provided.
     * null if the input iterable is null.
     */
    public static <C extends Collection<String>> List<Component> deserialize(C collection, TagResolver tagResolver) {
        if (collection == null) return null;

        List<Component> components = new ArrayList<>();
        for (String text : collection) {
            components.add(deserialize(text, tagResolver));
        }

        return components;
    }

    /**
     * Deserializes MiniMessage texts.
     *
     * @param collection The collection of texts to deserialize.
     * @param <C>        The type of the collection.
     * @return A collection of deserialized texts or an empty iterable if no texts are provided.
     * null if the input iterable is null.
     */
    public static <C extends Collection<String>> List<Component> deserialize(C collection) {
        return deserialize(collection, null);
    }

    /**
     * Deserializes a legacy formatted text.
     *
     * @param text The text to deserialize.
     * @return The deserialized text.
     * null if the input text is null.
     */
    public static Component deserializeLegacy(String text) {
        if (text == null) return null;
        return legacySerializer.deserialize(text);
    }

    /**
     * Deserializes legacy formatted texts.
     *
     * @param texts The texts to deserialize.
     * @return An array of deserialized texts or an empty array if no texts are provided.
     * null if the input texts array is null.
     */
    public static Component[] deserializeLegacy(String[] texts) {
        if (texts == null) return null;

        Component[] components = new Component[texts.length];

        for (int i = 0; i < texts.length; i++) {
            components[i] = deserializeLegacy(texts[i]);
        }

        return components;
    }

    /**
     * Deserializes legacy formatted texts.
     *
     * @param collection The collection of texts to deserialize.
     * @param <C>        The type of the collection.
     * @return A collection of deserialized texts or an empty iterable if no texts are provided.
     * null if the input iterable is null.
     */
    public static <C extends Collection<String>> List<Component> deserializeLegacy(C collection) {
        if (collection == null) return null;

        List<Component> components = new ArrayList<>();
        for (String text : collection) {
            components.add(deserializeLegacy(text));
        }

        return components;
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    SEND    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Sends a text to a {@link CommandSource} or a {@link Player}.
     * The text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param receiver    The receiver of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException if the text or the receiver is null.
     */
    public void send(@NotNull String text, @NotNull CommandSource receiver, TagResolver tagResolver) {
        Preconditions.checkNotNull(text, "Text cannot be null");
        Preconditions.checkNotNull(receiver, "Receiver cannot be null");

        receiver.sendMessage(deserialize(text, tagResolver));
    }

    /**
     * Sends a text to a {@link CommandSource} or a {@link Player}.
     * The text is deserialized before being sent.
     *
     * @param text     The text to send.
     * @param receiver The receiver of the text.
     * @throws NullPointerException if the text or the receiver is null.
     */
    public void send(@NotNull String text, @NotNull CommandSource receiver) {
        send(text, receiver, null);
    }

    /**
     * Sends a text to a {@link CommandSource} or a {@link Player}.
     *
     * @param text            The text to send.
     * @param receiver        The receiver of the text.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text or the receiver is null.
     */
    public void send(@NotNull String text, @NotNull CommandSource receiver, TagResolver tagResolver, boolean stripFormatting) {
        if (stripFormatting) {
            send(stripFormatting(text, tagResolver), receiver, tagResolver);
        } else {
            send(text, receiver, tagResolver);
        }
    }

    /**
     * Sends a text to a {@link CommandSource} or a {@link Player}.
     *
     * @param text            The text to send.
     * @param receiver        The receiver of the text.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text or the receiver is null.
     */
    public void send(@NotNull String text, @NotNull CommandSource receiver, boolean stripFormatting) {
        send(text, receiver, null, stripFormatting);
    }

    /**
     * Sends a text to a {@link CommandSource} or a {@link Player}.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param receiver    The receiver of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the receiver, the sender or the permissions is null.
     */
    public void send(@NotNull String text, @NotNull CommandSource receiver, TagResolver tagResolver, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        send(text, receiver, tagResolver, !hasPermission);
    }

    /**
     * Sends a text to a {@link CommandSource} or a {@link Player}.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param receiver    The receiver of the text.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the receiver or the sender or the permissions is null.
     */
    public void send(@NotNull String text, @NotNull CommandSource receiver, @NotNull CommandSource sender, @NotNull String... permissions) {
        send(text, receiver, null, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    SEND TO MULTIPLE RECEIVERS (ARRAY)    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Sends a text to a group of {@link CommandSource} or {@link Player}.
     * The text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param receivers   The receivers of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException if the text or the receivers is null.
     */
    public void send(@NotNull String text, @NotNull CommandSource[] receivers, TagResolver tagResolver) {
        Preconditions.checkNotNull(receivers, "Receivers cannot be null");

        for (CommandSource receiver : receivers) {
            if (receiver == null) continue;
            send(text, receiver, tagResolver);
        }
    }

    /**
     * Sends a text to a group of {@link CommandSource} or {@link Player}.
     * The text is deserialized before being sent.
     *
     * @param text      The text to send.
     * @param receivers The receivers of the text.
     * @throws NullPointerException if the text or the receivers is null.
     */
    public void send(@NotNull String text, @NotNull CommandSource[] receivers) {
        send(text, receivers, null);
    }

    /**
     * Sends a text to a group of {@link CommandSource} or {@link Player}.
     *
     * @param text            The text to send.
     * @param receivers       The receivers of the text.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text or the receivers is null.
     */
    public void send(@NotNull String text, @NotNull CommandSource[] receivers, TagResolver tagResolver, boolean stripFormatting) {
        if (stripFormatting) {
            send(stripFormatting(text, tagResolver), receivers, tagResolver);
        } else {
            send(text, receivers, tagResolver);
        }
    }

    /**
     * Sends a text to a group of {@link CommandSource} or {@link Player}.
     *
     * @param text            The text to send.
     * @param receivers       The receivers of the text.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text or the receivers is null.
     */
    public void send(@NotNull String text, @NotNull CommandSource[] receivers, boolean stripFormatting) {
        send(text, receivers, null, stripFormatting);
    }

    /**
     * Sends a text to a group of {@link CommandSource} or {@link Player}.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param receivers   The receivers of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the receivers or the sender or the permissions is null.
     */
    public void send(@NotNull String text, @NotNull CommandSource[] receivers, TagResolver tagResolver, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        send(text, receivers, tagResolver, !hasPermission);
    }

    /**
     * Sends a text to a group of {@link CommandSource} or {@link Player}.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param receivers   The receivers of the text.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the receivers, the sender or the permissions is null.
     */
    public void send(@NotNull String text, @NotNull CommandSource[] receivers, @NotNull CommandSource sender, @NotNull String... permissions) {
        send(text, receivers, null, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    SEND TO MULTIPLE RECEIVERS (COLLECTION)    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Sends a text to a group of {@link CommandSource} or {@link Player}.
     * The text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param receivers   The receivers of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException if the text or the receivers is null.
     */
    public <R extends Collection<? extends CommandSource>> void send(@NotNull String text, @NotNull R receivers, TagResolver tagResolver) {
        Preconditions.checkNotNull(receivers, "Receivers cannot be null");

        for (CommandSource receiver : receivers) {
            if (receiver == null) continue;
            send(text, receiver, tagResolver);
        }
    }

    /**
     * Sends a text to a group of {@link CommandSource} or {@link Player}.
     * The text is deserialized before being sent.
     *
     * @param text      The text to send.
     * @param receivers The receivers of the text.
     * @throws NullPointerException if the text or the receivers is null.
     */
    public <R extends Collection<? extends CommandSource>> void send(@NotNull String text, @NotNull R receivers) {
        send(text, receivers, null);
    }

    /**
     * Sends a text to a group of {@link CommandSource} or {@link Player}.
     *
     * @param text            The text to send.
     * @param receivers       The receivers of the text.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text or the receivers is null.
     */
    public <R extends Collection<? extends CommandSource>> void send(@NotNull String text, @NotNull R receivers, TagResolver tagResolver, boolean stripFormatting) {
        if (stripFormatting) {
            send(stripFormatting(text, tagResolver), receivers, tagResolver);
        } else {
            send(text, receivers, tagResolver);
        }
    }

    /**
     * Sends a text to a group of {@link CommandSource} or {@link Player}.
     *
     * @param text            The text to send.
     * @param receivers       The receivers of the text.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text or the receivers is null.
     */
    public <R extends Collection<? extends CommandSource>> void send(@NotNull String text, @NotNull R receivers, boolean stripFormatting) {
        send(text, receivers, null, stripFormatting);
    }

    /**
     * Sends a text to a group of {@link CommandSource} or {@link Player}.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param receivers   The receivers of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the receivers, the sender or the permissions is null.
     */
    public <R extends Collection<? extends CommandSource>> void send(@NotNull String text, @NotNull R receivers, TagResolver tagResolver, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        send(text, receivers, tagResolver, !hasPermission);
    }

    /**
     * Sends a text to a group of {@link CommandSource} or {@link Player}.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param receivers   The receivers of the text.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the receivers, the sender or the permissions is null.
     */
    public <R extends Collection<? extends CommandSource>> void send(@NotNull String text, @NotNull R receivers, @NotNull CommandSource sender, @NotNull String... permissions) {
        send(text, receivers, null, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    SEND MULTIPLE TEXTS (ARRAY)    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Sends a list texts to a {@link CommandSource} or a {@link Player}.
     * The texts are deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param receiver    The receiver of the texts.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException if the texts or the receiver is null.
     */
    public void send(@NotNull String[] texts, @NotNull CommandSource receiver, TagResolver tagResolver) {
        Preconditions.checkNotNull(texts, "Texts cannot be null");
        Preconditions.checkNotNull(receiver, "Receiver cannot be null");

        if (texts.length == 0) return;

        for (String text : texts) {
            if (text == null) continue;
            receiver.sendMessage(deserialize(text, tagResolver));
        }
    }

    /**
     * Sends a list texts to a {@link CommandSource} or a {@link Player}.
     * The texts are deserialized before being sent.
     *
     * @param texts    The texts to send.
     * @param receiver The receiver of the text.
     * @throws NullPointerException if the texts or the receiver is null.
     */
    public void send(@NotNull String[] texts, @NotNull CommandSource receiver) {
        send(texts, receiver, null);
    }

    /**
     * Sends a list texts to a {@link CommandSource} or a {@link Player}.
     *
     * @param texts           The texts to send.
     * @param receiver        The receiver of the text.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param stripFormatting Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts or the receiver is null.
     */
    public void send(@NotNull String[] texts, @NotNull CommandSource receiver, TagResolver tagResolver, boolean stripFormatting) {
        if (stripFormatting) {
            send(stripFormatting(texts, tagResolver), receiver, tagResolver);
        } else {
            send(texts, receiver, tagResolver);
        }
    }

    /**
     * Sends a list texts to a {@link CommandSource} or a {@link Player}.
     *
     * @param texts           The texts to send.
     * @param receiver        The receiver of the text.
     * @param stripFormatting Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts or the receiver is null.
     */
    public void send(@NotNull String[] texts, @NotNull CommandSource receiver, boolean stripFormatting) {
        send(texts, receiver, null, stripFormatting);
    }

    /**
     * Sends a list texts to a {@link CommandSource} or a {@link Player}.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param receiver    The receiver of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the texts, the receiver, the sender or the permissions is null.
     */
    public void send(@NotNull String[] texts, @NotNull CommandSource receiver, TagResolver tagResolver, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        send(texts, receiver, tagResolver, !hasPermission);
    }

    /**
     * Sends a list texts to a {@link CommandSource} or a {@link Player}.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param receiver    The receiver of the text.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the texts, the receiver, the sender or the permissions is null.
     */
    public void send(@NotNull String[] texts, @NotNull CommandSource receiver, @NotNull CommandSource sender, @NotNull String... permissions) {
        send(texts, receiver, null, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    SEND MULTIPLE TEXTS (ARRAY) TO MULTIPLE RECEIVERS (ARRAY)    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Sends a list texts to a group of {@link CommandSource} or {@link Player}.
     * The texts are deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param receivers   The receivers of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException if the texts or the receivers is null.
     */
    public void send(@NotNull String[] texts, @NotNull CommandSource[] receivers, TagResolver tagResolver) {
        Preconditions.checkNotNull(receivers, "Receivers cannot be null");

        for (CommandSource receiver : receivers) {
            send(texts, receiver, tagResolver);
        }
    }

    /**
     * Sends a list texts to a group of {@link CommandSource} or {@link Player}.
     * The texts are deserialized before being sent.
     *
     * @param texts     The texts to send.
     * @param receivers The receivers of the text.
     * @throws NullPointerException if the texts or the receivers is null.
     */
    public void send(@NotNull String[] texts, @NotNull CommandSource[] receivers) {
        send(texts, receivers, null);
    }

    /**
     * Sends a list texts to a group of {@link CommandSource} or {@link Player}.
     *
     * @param texts           The texts to send.
     * @param receivers       The receivers of the text.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param stripFormatting Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts or the receivers is null.
     */
    public void send(@NotNull String[] texts, @NotNull CommandSource[] receivers, TagResolver tagResolver, boolean stripFormatting) {
        if (stripFormatting) {
            send(stripFormatting(texts, tagResolver), receivers, tagResolver);
        } else {
            send(texts, receivers, tagResolver);
        }
    }

    /**
     * Sends a list texts to a group of {@link CommandSource} or {@link Player}.
     *
     * @param texts           The texts to send.
     * @param receivers       The receivers of the text.
     * @param stripFormatting Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts or the receivers is null.
     */
    public void send(@NotNull String[] texts, @NotNull CommandSource[] receivers, boolean stripFormatting) {
        send(texts, receivers, null, stripFormatting);
    }

    /**
     * Sends a list texts to a group of {@link CommandSource} or {@link Player}.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param receivers   The receivers of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the receivers, the sender or the permissions is null.
     */
    public void send(@NotNull String[] texts, @NotNull CommandSource[] receivers, TagResolver tagResolver, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        send(texts, receivers, tagResolver, !hasPermission);
    }

    /**
     * Sends a list texts to a group of {@link CommandSource} or {@link Player}.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param receivers   The receivers of the text.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the receivers, the sender or the permissions is null.
     */
    public void send(@NotNull String[] texts, @NotNull CommandSource[] receivers, @NotNull CommandSource sender, @NotNull String... permissions) {
        send(texts, receivers, null, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    SEND MULTIPLE TEXTS (ARRAY) TO MULTIPLE RECEIVERS (COLLECTION)    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Sends a list texts to a group of {@link CommandSource} or {@link Player}.
     * The texts are deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param receivers   The receivers of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException if the texts or the receivers is null.
     */
    public <R extends Collection<? extends CommandSource>> void send(@NotNull String[] texts, @NotNull R receivers, TagResolver tagResolver) {
        Preconditions.checkNotNull(receivers, "Receivers cannot be null");

        for (CommandSource receiver : receivers) {
            if (receiver == null) continue;
            send(texts, receiver, tagResolver);
        }
    }

    /**
     * Sends a list texts to a group of {@link CommandSource} or {@link Player}.
     * The texts are deserialized before being sent.
     *
     * @param texts     The texts to send.
     * @param receivers The receivers of the text.
     * @throws NullPointerException if the texts or the receivers is null.
     */
    public <R extends Collection<? extends CommandSource>> void send(@NotNull String[] texts, @NotNull R receivers) {
        send(texts, receivers, null);
    }

    /**
     * Sends a list texts to a group of {@link CommandSource} or {@link Player}.
     *
     * @param texts           The texts to send.
     * @param receivers       The receivers of the text.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param stripFormatting Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts or the receivers is null.
     */
    public <R extends Collection<? extends CommandSource>> void send(@NotNull String[] texts, @NotNull R receivers, TagResolver tagResolver, boolean stripFormatting) {
        if (stripFormatting) {
            send(stripFormatting(texts, tagResolver), receivers, tagResolver);
        } else {
            send(texts, receivers, tagResolver);
        }
    }

    /**
     * Sends a list texts to a group of {@link CommandSource} or {@link Player}.
     *
     * @param texts           The texts to send.
     * @param receivers       The receivers of the text.
     * @param stripFormatting Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts or the receivers is null.
     */
    public <R extends Collection<? extends CommandSource>> void send(@NotNull String[] texts, @NotNull R receivers, boolean stripFormatting) {
        send(texts, receivers, null, stripFormatting);
    }

    /**
     * Sends a list texts to a group of {@link CommandSource} or {@link Player}.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param receivers   The receivers of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the receivers, the sender or the permissions is null.
     */
    public <R extends Collection<? extends CommandSource>> void send(@NotNull String[] texts, @NotNull R receivers, TagResolver tagResolver, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        send(texts, receivers, tagResolver, !hasPermission);
    }

    /**
     * Sends a list texts to a group of {@link CommandSource} or {@link Player}.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param receivers   The receivers of the text.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the receivers, the sender or the permissions is null.
     */
    public <R extends Collection<? extends CommandSource>> void send(@NotNull String[] texts, @NotNull R receivers, @NotNull CommandSource sender, @NotNull String... permissions) {
        send(texts, receivers, null, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    SEND MULTIPLE TEXTS (COLLECTION)    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Sends a list texts to a {@link CommandSource} or a {@link Player}.
     * The texts are deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param receiver    The receiver of the texts.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException if the texts or the receiver is null.
     */
    public <T extends Collection<String>> void send(@NotNull T texts, @NotNull CommandSource receiver, TagResolver tagResolver) {
        Preconditions.checkNotNull(texts, "Texts cannot be null");
        Preconditions.checkNotNull(receiver, "Receiver cannot be null");

        if (texts.isEmpty()) return;

        for (String text : texts) {
            if (text == null) continue;
            receiver.sendMessage(deserialize(text, tagResolver));
        }
    }

    /**
     * Sends a list texts to a {@link CommandSource} or a {@link Player}.
     * The texts are deserialized before being sent.
     *
     * @param texts    The texts to send.
     * @param receiver The receiver of the text.
     * @throws NullPointerException if the texts or the receiver is null.
     */
    public <T extends Collection<String>> void send(@NotNull T texts, @NotNull CommandSource receiver) {
        send(texts, receiver, null);
    }

    /**
     * Sends a list texts to a {@link CommandSource} or a {@link Player}.
     *
     * @param texts           The texts to send.
     * @param receiver        The receiver of the text.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param stripFormatting Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts or the receiver is null.
     */
    public <T extends Collection<String>> void send(@NotNull T texts, @NotNull CommandSource receiver, TagResolver tagResolver, boolean stripFormatting) {
        if (stripFormatting) {
            send(stripFormatting(texts, tagResolver), receiver, tagResolver);
        } else {
            send(texts, receiver, tagResolver);
        }
    }

    /**
     * Sends a list texts to a {@link CommandSource} or a {@link Player}.
     *
     * @param texts           The texts to send.
     * @param receiver        The receiver of the text.
     * @param stripFormatting Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts or the receiver is null.
     */
    public <T extends Collection<String>> void send(@NotNull T texts, @NotNull CommandSource receiver, boolean stripFormatting) {
        send(texts, receiver, null, stripFormatting);
    }

    /**
     * Sends a list texts to a {@link CommandSource} or a {@link Player}.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param receiver    The receiver of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the texts, the receiver, the sender or the permissions is null.
     */
    public <T extends Collection<String>> void send(@NotNull T texts, @NotNull CommandSource receiver, TagResolver tagResolver, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        send(texts, receiver, tagResolver, !hasPermission);
    }

    /**
     * Sends a list texts to a {@link CommandSource} or a {@link Player}.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param receiver    The receiver of the text.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the texts, the receiver, the sender or the permissions is null.
     */
    public <T extends Collection<String>> void send(@NotNull T texts, @NotNull CommandSource receiver, @NotNull CommandSource sender, @NotNull String... permissions) {
        send(texts, receiver, null, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    SEND MULTIPLE TEXTS (COLLECTION) TO MULTIPLE RECEIVERS (ARRAY)    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Sends a list texts to a group of {@link CommandSource} or {@link Player}.
     * The texts are deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param receivers   The receivers of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException if the texts or the receivers is null.
     */
    public <T extends Collection<String>> void send(@NotNull T texts, @NotNull CommandSource[] receivers, TagResolver tagResolver) {
        Preconditions.checkNotNull(receivers, "Receivers cannot be null");

        for (CommandSource receiver : receivers) {
            if (receiver == null) continue;
            send(texts, receiver, tagResolver);
        }
    }

    /**
     * Sends a list texts to a group of {@link CommandSource} or {@link Player}.
     * The texts are deserialized before being sent.
     *
     * @param texts     The texts to send.
     * @param receivers The receivers of the text.
     * @throws NullPointerException if the texts or the receivers is null.
     */
    public <T extends Collection<String>> void send(@NotNull T texts, @NotNull CommandSource[] receivers) {
        send(texts, receivers, null);
    }

    /**
     * Sends a list texts to a group of {@link CommandSource} or {@link Player}.
     *
     * @param texts           The texts to send.
     * @param receivers       The receivers of the text.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param stripFormatting Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts or the receivers is null.
     */
    public <T extends Collection<String>> void send(@NotNull T texts, @NotNull CommandSource[] receivers, TagResolver tagResolver, boolean stripFormatting) {
        if (stripFormatting) {
            send(stripFormatting(texts, tagResolver), receivers, tagResolver);
        } else {
            send(texts, receivers, tagResolver);
        }
    }

    /**
     * Sends a list texts to a group of {@link CommandSource} or {@link Player}.
     *
     * @param texts           The texts to send.
     * @param receivers       The receivers of the text.
     * @param stripFormatting Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts or the receivers is null.
     */
    public <T extends Collection<String>> void send(@NotNull T texts, @NotNull CommandSource[] receivers, boolean stripFormatting) {
        send(texts, receivers, null, stripFormatting);
    }

    /**
     * Sends a list texts to a group of {@link CommandSource} or {@link Player}.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param receivers   The receivers of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the receivers, the sender or the permissions is null.
     */
    public <T extends Collection<String>> void send(@NotNull T texts, @NotNull CommandSource[] receivers, TagResolver tagResolver, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        send(texts, receivers, tagResolver, !hasPermission);
    }

    /**
     * Sends a list texts to a group of {@link CommandSource} or {@link Player}.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param receivers   The receivers of the text.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the receivers, the sender or the permissions is null.
     */
    public <T extends Collection<String>> void send(@NotNull T texts, @NotNull CommandSource[] receivers, @NotNull CommandSource sender, @NotNull String... permissions) {
        send(texts, receivers, null, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    SEND MULTIPLE TEXTS (COLLECTION) TO MULTIPLE RECEIVERS (COLLECTION)    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Sends a list texts to a group of {@link CommandSource} or {@link Player}.
     * The texts are deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param receivers   The receivers of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException if the texts or the receivers is null.
     */
    public <T extends Collection<String>, R extends Collection<? extends CommandSource>> void send(@NotNull T texts, @NotNull R receivers, TagResolver tagResolver) {
        Preconditions.checkNotNull(receivers, "Receivers cannot be null");

        for (CommandSource receiver : receivers) {
            if (receiver == null) continue;
            send(texts, receiver, tagResolver);
        }
    }

    /**
     * Sends a list texts to a group of {@link CommandSource} or {@link Player}.
     * The texts are deserialized before being sent.
     *
     * @param texts     The texts to send.
     * @param receivers The receivers of the text.
     * @throws NullPointerException if the texts or the receivers is null.
     */
    public <T extends Collection<String>, R extends Collection<? extends CommandSource>> void send(@NotNull T texts, @NotNull R receivers) {
        send(texts, receivers, null);
    }

    /**
     * Sends a list texts to a group of {@link CommandSource} or {@link Player}.
     *
     * @param texts           The texts to send.
     * @param receivers       The receivers of the text.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param stripFormatting Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts or the receivers is null.
     */
    public <T extends Collection<String>, R extends Collection<? extends CommandSource>> void send(@NotNull T texts, @NotNull R receivers, TagResolver tagResolver, boolean stripFormatting) {
        if (stripFormatting) {
            send(stripFormatting(texts, tagResolver), receivers, tagResolver);
        } else {
            send(texts, receivers, tagResolver);
        }
    }

    /**
     * Sends a list texts to a group of {@link CommandSource} or {@link Player}.
     *
     * @param texts           The texts to send.
     * @param receivers       The receivers of the text.
     * @param stripFormatting Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts or the receivers is null.
     */
    public <T extends Collection<String>, R extends Collection<? extends CommandSource>> void send(@NotNull T texts, @NotNull R receivers, boolean stripFormatting) {
        send(texts, receivers, null, stripFormatting);
    }

    /**
     * Sends a list texts to a group of {@link CommandSource} or {@link Player}.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param receivers   The receivers of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the receivers, the sender or the permissions is null.
     */
    public <T extends Collection<String>, R extends Collection<? extends CommandSource>> void send(@NotNull T texts, @NotNull R receivers, TagResolver tagResolver, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        send(texts, receivers, tagResolver, !hasPermission);
    }

    /**
     * Sends a list texts to a group of {@link CommandSource} or {@link Player}.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param receivers   The receivers of the text.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the receivers, the sender or the permissions is null.
     */
    public <T extends Collection<String>, R extends Collection<? extends CommandSource>> void send(@NotNull T texts, @NotNull R receivers, @NotNull CommandSource sender, @NotNull String... permissions) {
        send(texts, receivers, null, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    BROADCAST    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a text to all {@link Player}s on the server.
     * The text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException if the text is null.
     */
    public void broadcast(@NotNull String text, TagResolver tagResolver) {
        Preconditions.checkNotNull(text, "Text cannot be null");

        server.getAllPlayers().forEach(p -> p.sendMessage(deserialize(text, tagResolver)));
    }

    /**
     * Broadcasts a text to all {@link Player}s on the server.
     * The text is deserialized before being sent.
     *
     * @param text The text to send.
     * @throws NullPointerException if the text is null.
     */
    public void broadcast(@NotNull String text) {
        broadcast(text, (TagResolver) null);
    }

    /**
     * Broadcasts a text to all {@link Player}s on the server.
     *
     * @param text            The text to send.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text is null.
     */
    public void broadcast(@NotNull String text, TagResolver tagResolver, boolean stripFormatting) {
        if (stripFormatting) {
            broadcast(stripFormatting(text, tagResolver), tagResolver);
        } else {
            broadcast(text, tagResolver);
        }
    }

    /**
     * Broadcasts a text to all {@link Player}s on the server.
     *
     * @param text            The text to send.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text is null.
     */
    public void broadcast(@NotNull String text, boolean stripFormatting) {
        broadcast(text, (TagResolver) null, stripFormatting);
    }

    /**
     * Broadcasts a text to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the sender or the permissions is null.
     */
    public void broadcast(@NotNull String text, TagResolver tagResolver, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        broadcast(text, tagResolver, !hasPermission);
    }

    /**
     * Broadcasts a text to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the sender or the permissions is null.
     */
    public void broadcast(@NotNull String text, @NotNull CommandSource sender, @NotNull String... permissions) {
        broadcast(text, (TagResolver) null, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    BROADCAST MULTIPLE TEXTS (ARRAY)    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     * The texts are deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException if the texts is null.
     */
    public void broadcast(@NotNull String[] texts, TagResolver tagResolver) {
        Preconditions.checkNotNull(texts, "Texts cannot be null");

        if (texts.length == 0) return;

        for (String text : texts) {
            if (text == null) continue;
            server.getAllPlayers().forEach(p -> p.sendMessage(deserialize(text, tagResolver)));
        }
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     * The texts are deserialized before being sent.
     *
     * @param texts The texts to send.
     * @throws NullPointerException if the texts is null.
     */
    public void broadcast(@NotNull String[] texts) {
        broadcast(texts, (TagResolver) null);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     *
     * @param texts           The texts to send.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param stripFormatting Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts is null.
     */
    public void broadcast(@NotNull String[] texts, TagResolver tagResolver, boolean stripFormatting) {
        if (stripFormatting) {
            broadcast(stripFormatting(texts, tagResolver), tagResolver);
        } else {
            broadcast(texts, tagResolver);
        }
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     *
     * @param texts           The texts to send.
     * @param stripFormatting Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts is null.
     */
    public void broadcast(@NotNull String[] texts, boolean stripFormatting) {
        broadcast(texts, (TagResolver) null, stripFormatting);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the texts, the sender or the permissions is null.
     */
    public void broadcast(@NotNull String[] texts, TagResolver tagResolver, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        broadcast(texts, tagResolver, !hasPermission);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the texts, the sender or the permissions is null.
     */
    public void broadcast(@NotNull String[] texts, @NotNull CommandSource sender, @NotNull String... permissions) {
        broadcast(texts, (TagResolver) null, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    BROADCAST MULTIPLE TEXTS (COLLECTION)    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     * The texts are deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException if the texts is null.
     */
    public <T extends Collection<String>> void broadcast(@NotNull T texts, TagResolver tagResolver) {
        Preconditions.checkNotNull(texts, "Texts cannot be null");

        if (texts.isEmpty()) return;

        for (String text : texts) {
            if (text == null) continue;
            server.getAllPlayers().forEach(p -> p.sendMessage(deserialize(text, tagResolver)));
        }
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     * The texts are deserialized before being sent.
     *
     * @param texts The texts to send.
     * @throws NullPointerException if the texts is null.
     */
    public <T extends Collection<String>> void broadcast(@NotNull T texts) {
        broadcast(texts, (TagResolver) null);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     *
     * @param texts           The texts to send.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param stripFormatting Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts is null.
     */
    public <T extends Collection<String>> void broadcast(@NotNull T texts, TagResolver tagResolver, boolean stripFormatting) {
        if (stripFormatting) {
            broadcast(stripFormatting(texts, tagResolver), tagResolver);
        } else {
            broadcast(texts, tagResolver);
        }
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     *
     * @param texts           The texts to send.
     * @param stripFormatting Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts is null.
     */
    public <T extends Collection<String>> void broadcast(@NotNull T texts, boolean stripFormatting) {
        broadcast(texts, (TagResolver) null, stripFormatting);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the texts, the sender or the permissions is null.
     */
    public <T extends Collection<String>> void broadcast(@NotNull T texts, TagResolver tagResolver, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        broadcast(texts, tagResolver, !hasPermission);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the texts, the sender or the permissions is null.
     */
    public <T extends Collection<String>> void broadcast(@NotNull T texts, @NotNull CommandSource sender, @NotNull String... permissions) {
        broadcast(texts, (TagResolver) null, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    SERVER BROADCAST    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a text to all {@link Player}s in a server.
     * The text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param server      The {@link RegisteredServer} to send the text to.
     * @throws NullPointerException if the text or the server is null.
     */
    @SuppressWarnings("PatternValidation")
    public void broadcast(@NotNull String text, TagResolver tagResolver, @NotNull RegisteredServer server) {
        Preconditions.checkNotNull(text, "Text cannot be null");
        Preconditions.checkNotNull(server, "World cannot be null");

        server.getPlayersConnected().forEach(p -> p.sendMessage(deserialize(text, tagResolver)));
    }

    /**
     * Broadcasts a text to all {@link Player}s on the server.
     * The text is deserialized before being sent.
     *
     * @param text   The text to send.
     * @param server The {@link RegisteredServer} to send the text to.
     * @throws NullPointerException if the text or the server is null.
     */
    public void broadcast(@NotNull String text, @NotNull RegisteredServer server) {
        broadcast(text, null, server);
    }

    /**
     * Broadcasts a text to all {@link Player}s on the server.
     *
     * @param text            The text to send.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param server          The {@link RegisteredServer} to send the text to.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text or the server is null.
     */
    public void broadcast(@NotNull String text, TagResolver tagResolver, @NotNull RegisteredServer server, boolean stripFormatting) {
        if (stripFormatting) {
            broadcast(stripFormatting(text, tagResolver), tagResolver, server);
        } else {
            broadcast(text, tagResolver, server);
        }
    }

    /**
     * Broadcasts a text to all {@link Player}s on the server.
     *
     * @param text            The text to send.
     * @param server          The {@link RegisteredServer} to send the text to.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text or the server is null.
     */
    public void broadcast(@NotNull String text, @NotNull RegisteredServer server, boolean stripFormatting) {
        broadcast(text, null, server, stripFormatting);
    }

    /**
     * Broadcasts a text to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param server      The {@link RegisteredServer} to send the text to.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the server, the sender or the permissions is null.
     */
    public void broadcast(@NotNull String text, TagResolver tagResolver, @NotNull RegisteredServer server, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        broadcast(text, tagResolver, server, !hasPermission);
    }

    /**
     * Broadcasts a text to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param server      The {@link RegisteredServer} to send the text to.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the server, the sender or the permissions is null.
     */
    public void broadcast(@NotNull String text, @NotNull RegisteredServer server, @NotNull CommandSource sender, @NotNull String... permissions) {
        broadcast(text, null, server, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    SERVER BROADCAST MULTIPLE TEXTS (ARRAY)    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     * The texts are deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException if the texts or the receiver is null.
     */
    @SuppressWarnings("PatternValidation")
    public void broadcast(@NotNull String[] texts, TagResolver tagResolver, @NotNull RegisteredServer server) {
        Preconditions.checkNotNull(texts, "Texts cannot be null");
        Preconditions.checkNotNull(server, "World cannot be null");

        if (texts.length == 0) return;

        for (String text : texts) {
            if (text == null) continue;
            server.getPlayersConnected().forEach(p -> p.sendMessage(deserialize(text, tagResolver)));
        }
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     * The texts are deserialized before being sent.
     *
     * @param texts  The texts to send.
     * @param server The {@link RegisteredServer} to send the text to.
     * @throws NullPointerException if the texts or the receiver is null.
     */
    public void broadcast(@NotNull String[] texts, @NotNull RegisteredServer server) {
        broadcast(texts, null, server);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     *
     * @param texts           The texts to send.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param server          The {@link RegisteredServer} to send the text to.
     * @param stripFormatting Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts or the server is null.
     */
    public void broadcast(@NotNull String[] texts, TagResolver tagResolver, @NotNull RegisteredServer server, boolean stripFormatting) {
        if (stripFormatting) {
            broadcast(stripFormatting(texts, tagResolver), tagResolver, server);
        } else {
            broadcast(texts, tagResolver, server);
        }
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     *
     * @param texts           The texts to send.
     * @param server          The {@link RegisteredServer} to send the text to.
     * @param stripFormatting Whether to strip formatting from the texts before sending it.
     */
    public void broadcast(@NotNull String[] texts, @NotNull RegisteredServer server, boolean stripFormatting) {
        broadcast(texts, null, server, stripFormatting);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param server      The {@link RegisteredServer} to send the text to.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the texts, the server, the sender or the permissions is null.
     */
    public void broadcast(@NotNull String[] texts, TagResolver tagResolver, @NotNull RegisteredServer server, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        broadcast(texts, tagResolver, server, !hasPermission);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the texts, the server, the sender or the permissions is null.
     */
    public void broadcast(@NotNull String[] texts, @NotNull RegisteredServer server, @NotNull CommandSource sender, @NotNull String... permissions) {
        broadcast(texts, null, server, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    SERVER BROADCAST MULTIPLE TEXTS (COLLECTION)    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     * The texts are deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException if the texts or the receiver is null.
     */
    @SuppressWarnings("PatternValidation")
    public <T extends Collection<String>> void broadcast(@NotNull T texts, TagResolver tagResolver, @NotNull RegisteredServer server) {
        Preconditions.checkNotNull(texts, "Texts cannot be null");
        Preconditions.checkNotNull(server, "World cannot be null");

        if (texts.isEmpty()) return;

        for (String text : texts) {
            if (text == null) continue;
            server.getPlayersConnected().forEach(p -> p.sendMessage(deserialize(text, tagResolver)));
        }
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     * The texts are deserialized before being sent.
     *
     * @param texts  The texts to send.
     * @param server The {@link RegisteredServer} to send the text to.
     * @throws NullPointerException if the texts or the receiver is null.
     */
    public <T extends Collection<String>> void broadcast(@NotNull T texts, @NotNull RegisteredServer server) {
        broadcast(texts, null, server);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     *
     * @param texts           The texts to send.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param server          The {@link RegisteredServer} to send the text to.
     * @param stripFormatting Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts or the server is null.
     */
    public <T extends Collection<String>> void broadcast(@NotNull T texts, TagResolver tagResolver, @NotNull RegisteredServer server, boolean stripFormatting) {
        if (stripFormatting) {
            broadcast(stripFormatting(texts, tagResolver), tagResolver, server);
        } else {
            broadcast(texts, tagResolver, server);
        }
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     *
     * @param texts           The texts to send.
     * @param server          The {@link RegisteredServer} to send the text to.
     * @param stripFormatting Whether to strip formatting from the texts before sending it.
     */
    public <T extends Collection<String>> void broadcast(@NotNull T texts, @NotNull RegisteredServer server, boolean stripFormatting) {
        broadcast(texts, null, server, stripFormatting);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param server      The {@link RegisteredServer} to send the text to.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the texts, the server, the sender or the permissions is null.
     */
    public <T extends Collection<String>> void broadcast(@NotNull T texts, TagResolver tagResolver, @NotNull RegisteredServer server, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        broadcast(texts, tagResolver, server, !hasPermission);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the texts, the server, the sender or the permissions is null.
     */
    public <T extends Collection<String>> void broadcast(@NotNull T texts, @NotNull RegisteredServer server, @NotNull CommandSource sender, @NotNull String... permissions) {
        broadcast(texts, null, server, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    PERMISSION BROADCAST    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a text to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param text             The text to send.
     * @param tagResolver      The {@link TagResolver} for any additional tags to handle.
     * @param neededPermission The permission needed to receive the broadcast.
     * @throws NullPointerException if the text or the neededPermission is null.
     */
    public void broadcast(@NotNull String text, TagResolver tagResolver, @NotNull String neededPermission) {
        Preconditions.checkNotNull(text, "Text cannot be null");
        Preconditions.checkNotNull(neededPermission, "Needed permission cannot be null");

        server.getAllPlayers().stream()
                .filter(p -> p.hasPermission(neededPermission))
                .forEach(p -> p.sendMessage(deserialize(text, tagResolver)));

    }

    /**
     * Broadcasts a text to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param text             The text to send.
     * @param neededPermission The permission needed to receive the broadcast.
     * @throws NullPointerException if the text or the neededPermission is null.
     */
    public void broadcast(@NotNull String text, @NotNull String neededPermission) {
        broadcast(text, null, neededPermission);
    }

    /**
     * Broadcasts a text to all {@link Player}s with some permissions.
     *
     * @param text             The text to send.
     * @param tagResolver      The {@link TagResolver} for any additional tags to handle.
     * @param neededPermission The permission needed to receive the broadcast.
     * @param stripFormatting  Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text or the neededPermission is null.
     */
    public void broadcast(@NotNull String text, TagResolver tagResolver, @NotNull String neededPermission, boolean stripFormatting) {
        if (stripFormatting) {
            broadcast(stripFormatting(text, tagResolver), tagResolver, neededPermission);
        } else {
            broadcast(text, tagResolver, neededPermission);
        }
    }

    /**
     * Broadcasts a text to all {@link Player}s with some permissions.
     *
     * @param text             The text to send.
     * @param neededPermission The permission needed to receive the broadcast.
     * @param stripFormatting  Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text or the neededPermission is null.
     */
    public void broadcast(@NotNull String text, @NotNull String neededPermission, boolean stripFormatting) {
        broadcast(text, null, neededPermission, stripFormatting);
    }

    /**
     * Broadcasts a text to all {@link Player}s with some permissions.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text             The text to send.
     * @param tagResolver      The {@link TagResolver} for any additional tags to handle.
     * @param neededPermission The permission needed to receive the broadcast.
     * @param sender           The sender of the text.
     * @param permissions      The permissions to check.
     * @throws NullPointerException if the text, the neededPermission, the sender or the permissions is null.
     */
    public void broadcast(@NotNull String text, TagResolver tagResolver, @NotNull String neededPermission, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        broadcast(text, tagResolver, neededPermission, !hasPermission);
    }

    /**
     * Broadcasts a text to all {@link Player}s with some permissions.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text             The text to send.
     * @param neededPermission The permission needed to receive the broadcast.
     * @param sender           The sender of the text.
     * @param permissions      The permissions to check.
     * @throws NullPointerException if the text, the neededPermission, the sender or the permissions is null.
     */
    public void broadcast(@NotNull String text, @NotNull String neededPermission, @NotNull CommandSource sender, @NotNull String... permissions) {
        broadcast(text, null, neededPermission, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    PERMISSION BROADCAST MULTIPLE TEXTS (ARRAY)    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a list of texts to all {@link Player}s with some permissions.
     * The texts are deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException if the texts or the receiver is null.
     */
    public void broadcast(@NotNull String[] texts, TagResolver tagResolver, @NotNull String neededPermission) {
        Preconditions.checkNotNull(texts, "Texts cannot be null");
        Preconditions.checkNotNull(neededPermission, "Needed permission cannot be null");

        if (texts.length == 0) return;

        for (String text : texts) {
            if (text == null) continue;

            server.getAllPlayers().stream()
                    .filter(p -> p.hasPermission(neededPermission))
                    .forEach(p -> p.sendMessage(deserialize(text, tagResolver)));
        }
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s with some permissions.
     * The texts are deserialized before being sent.
     *
     * @param texts            The texts to send.
     * @param neededPermission The permission needed to receive the broadcast.
     * @throws NullPointerException if the texts or the receiver is null.
     */
    public void broadcast(@NotNull String[] texts, @NotNull String neededPermission) {
        broadcast(texts, null, neededPermission);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s with some permissions.
     *
     * @param texts            The texts to send.
     * @param tagResolver      The {@link TagResolver} for any additional tags to handle.
     * @param neededPermission The permission needed to receive the broadcast.
     * @param stripFormatting  Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts or the neededPermission is null.
     */
    public void broadcast(@NotNull String[] texts, TagResolver tagResolver, @NotNull String neededPermission, boolean stripFormatting) {
        if (stripFormatting) {
            broadcast(stripFormatting(texts, tagResolver), tagResolver, neededPermission);
        } else {
            broadcast(texts, tagResolver, neededPermission);
        }
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s with some permissions.
     *
     * @param texts            The texts to send.
     * @param neededPermission The permission needed to receive the broadcast.
     * @param stripFormatting  Whether to strip formatting from the texts before sending it.
     */
    public void broadcast(@NotNull String[] texts, @NotNull String neededPermission, boolean stripFormatting) {
        broadcast(texts, null, neededPermission, stripFormatting);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s with some permissions.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts            The texts to send.
     * @param tagResolver      The {@link TagResolver} for any additional tags to handle.
     * @param neededPermission The permission needed to receive the broadcast.
     * @param sender           The sender of the text.
     * @param permissions      The permissions to check.
     * @throws NullPointerException if the texts, the neededPermission, the sender or the permissions is null.
     */
    public void broadcast(@NotNull String[] texts, TagResolver tagResolver, @NotNull String neededPermission, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        broadcast(texts, tagResolver, neededPermission, !hasPermission);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s with some permissions.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the texts, the neededPermission, the sender or the permissions is null.
     */
    public void broadcast(@NotNull String[] texts, @NotNull String neededPermission, @NotNull CommandSource sender, @NotNull String... permissions) {
        broadcast(texts, null, neededPermission, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    PERMISSION BROADCAST MULTIPLE TEXTS (COLLECTION)    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a list of texts to all {@link Player}s with some permissions.
     * The texts are deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException if the texts or the receiver is null.
     */
    public <T extends Collection<String>> void broadcast(@NotNull T texts, TagResolver tagResolver, @NotNull String neededPermission) {
        Preconditions.checkNotNull(texts, "Texts cannot be null");
        Preconditions.checkNotNull(neededPermission, "Needed permission cannot be null");

        if (texts.isEmpty()) return;

        for (String text : texts) {
            if (text == null) continue;

            server.getAllPlayers().stream()
                    .filter(p -> p.hasPermission(neededPermission))
                    .forEach(p -> p.sendMessage(deserialize(text, tagResolver)));
        }
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s with some permissions.
     * The texts are deserialized before being sent.
     *
     * @param texts            The texts to send.
     * @param neededPermission The permission needed to receive the broadcast.
     * @throws NullPointerException if the texts or the receiver is null.
     */
    public <T extends Collection<String>> void broadcast(@NotNull T texts, @NotNull String neededPermission) {
        broadcast(texts, null, neededPermission);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s with some permissions.
     *
     * @param texts            The texts to send.
     * @param tagResolver      The {@link TagResolver} for any additional tags to handle.
     * @param neededPermission The permission needed to receive the broadcast.
     * @param stripFormatting  Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts or the neededPermission is null.
     */
    public <T extends Collection<String>> void broadcast(@NotNull T texts, TagResolver tagResolver, @NotNull String neededPermission, boolean stripFormatting) {
        if (stripFormatting) {
            broadcast(stripFormatting(texts, tagResolver), tagResolver, neededPermission);
        } else {
            broadcast(texts, tagResolver, neededPermission);
        }
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s with some permissions.
     *
     * @param texts            The texts to send.
     * @param neededPermission The permission needed to receive the broadcast.
     * @param stripFormatting  Whether to strip formatting from the texts before sending it.
     */
    public <T extends Collection<String>> void broadcast(@NotNull T texts, @NotNull String neededPermission, boolean stripFormatting) {
        broadcast(texts, null, neededPermission, stripFormatting);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s with some permissions.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts            The texts to send.
     * @param tagResolver      The {@link TagResolver} for any additional tags to handle.
     * @param neededPermission The permission needed to receive the broadcast.
     * @param sender           The sender of the text.
     * @param permissions      The permissions to check.
     * @throws NullPointerException if the texts, the neededPermission, the sender or the permissions is null.
     */
    public <T extends Collection<String>> void broadcast(@NotNull T texts, TagResolver tagResolver, @NotNull String neededPermission, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        broadcast(texts, tagResolver, neededPermission, !hasPermission);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s with some permissions.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the texts, the neededPermission, the sender or the permissions is null.
     */
    public <T extends Collection<String>> void broadcast(@NotNull T texts, @NotNull String neededPermission, @NotNull CommandSource sender, @NotNull String... permissions) {
        broadcast(texts, null, neededPermission, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    PERMISSIONS (ARRAY) BROADCAST    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a text to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param text              The text to send.
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @throws NullPointerException if the text or the neededPermissions is null.
     */
    public void broadcast(@NotNull String text, TagResolver tagResolver, @NotNull String[] neededPermissions) {
        Preconditions.checkNotNull(text, "Text cannot be null");
        Preconditions.checkNotNull(neededPermissions, "Needed permission cannot be null");

        for (String permission : neededPermissions) {
            if (permission == null) continue;

            server.getAllPlayers().stream()
                    .filter(p -> p.hasPermission(permission))
                    .forEach(p -> p.sendMessage(deserialize(text, tagResolver)));
        }
    }

    /**
     * Broadcasts a text to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param text              The text to send.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @throws NullPointerException if the text or the neededPermissions is null.
     */
    public void broadcast(@NotNull String text, @NotNull String[] neededPermissions) {
        broadcast(text, (TagResolver) null, neededPermissions);
    }

    /**
     * Broadcasts a text to all {@link Player}s with some permissions.
     *
     * @param text              The text to send.
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param stripFormatting   Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text or the neededPermissions is null.
     */
    public void broadcast(@NotNull String text, TagResolver tagResolver, @NotNull String[] neededPermissions, boolean stripFormatting) {
        if (stripFormatting) {
            broadcast(stripFormatting(text, tagResolver), tagResolver, neededPermissions);
        } else {
            broadcast(text, tagResolver, neededPermissions);
        }
    }

    /**
     * Broadcasts a text to all {@link Player}s with some permissions.
     *
     * @param text              The text to send.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param stripFormatting   Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text or the neededPermissions is null.
     */
    public void broadcast(@NotNull String text, @NotNull String[] neededPermissions, boolean stripFormatting) {
        broadcast(text, null, neededPermissions, stripFormatting);
    }

    /**
     * Broadcasts a text to all {@link Player}s with some permissions.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text              The text to send.
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param sender            The sender of the text.
     * @param permissions       The permissions to check.
     * @throws NullPointerException if the text, the neededPermissions, the sender or The permissions is null.
     */
    public void broadcast(@NotNull String text, TagResolver tagResolver, @NotNull String[] neededPermissions, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        broadcast(text, tagResolver, neededPermissions, !hasPermission);
    }

    /**
     * Broadcasts a text to all {@link Player}s with some permissions.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text              The text to send.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param sender            The sender of the text.
     * @param permissions       The permissions to check.
     * @throws NullPointerException if the text, the neededPermissions, the sender or The permissions is null.
     */
    public void broadcast(@NotNull String text, @NotNull String[] neededPermissions, @NotNull CommandSource sender, @NotNull String... permissions) {
        broadcast(text, null, neededPermissions, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    PERMISSIONS (ARRAY) BROADCAST MULTIPLE TEXTS (ARRAY)    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a list of texts to all {@link Player}s with some permissions.
     * The texts are deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException if the texts or the receiver is null.
     */
    public void broadcast(@NotNull String[] texts, TagResolver tagResolver, @NotNull String[] neededPermissions) {
        Preconditions.checkNotNull(texts, "Texts cannot be null");
        Preconditions.checkNotNull(neededPermissions, "Needed permission cannot be null");

        if (texts.length == 0) return;

        for (String permission : neededPermissions) {
            if (permission == null) continue;

            for (String text : texts) {
                if (text == null) continue;

                server.getAllPlayers().stream()
                        .filter(p -> p.hasPermission(permission))
                        .forEach(p -> p.sendMessage(deserialize(text, tagResolver)));
            }
        }
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s with some permissions.
     * The texts are deserialized before being sent.
     *
     * @param texts             The texts to send.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @throws NullPointerException if the texts or the receiver is null.
     */
    public void broadcast(@NotNull String[] texts, @NotNull String[] neededPermissions) {
        broadcast(texts, (TagResolver) null, neededPermissions);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s with some permissions.
     *
     * @param texts             The texts to send.
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param stripFormatting   Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts or the neededPermissions is null.
     */
    public void broadcast(@NotNull String[] texts, TagResolver tagResolver, @NotNull String[] neededPermissions, boolean stripFormatting) {
        if (stripFormatting) {
            broadcast(stripFormatting(texts, tagResolver), tagResolver, neededPermissions);
        } else {
            broadcast(texts, tagResolver, neededPermissions);
        }
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s with some permissions.
     *
     * @param texts             The texts to send.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param stripFormatting   Whether to strip formatting from the texts before sending it.
     */
    public void broadcast(@NotNull String[] texts, @NotNull String[] neededPermissions, boolean stripFormatting) {
        broadcast(texts, null, neededPermissions, stripFormatting);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s with some permissions.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts             The texts to send.
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param sender            The sender of the text.
     * @param permissions       The permissions to check.
     * @throws NullPointerException if the texts, the neededPermissions, the sender or The permissions is null.
     */
    public void broadcast(@NotNull String[] texts, TagResolver tagResolver, @NotNull String[] neededPermissions, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        broadcast(texts, tagResolver, neededPermissions, !hasPermission);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s with some permissions.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the texts, the neededPermissions, the sender or The permissions is null.
     */
    public void broadcast(@NotNull String[] texts, @NotNull String[] neededPermissions, @NotNull CommandSource sender, @NotNull String... permissions) {
        broadcast(texts, null, neededPermissions, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    PERMISSIONS (ARRAY) BROADCAST MULTIPLE TEXTS (COLLECTION)    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a list of texts to all {@link Player}s with some permissions.
     * The texts are deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException if the texts or the receiver is null.
     */
    public <T extends Collection<String>> void broadcast(@NotNull T texts, TagResolver tagResolver, @NotNull String[] neededPermissions) {
        Preconditions.checkNotNull(texts, "Texts cannot be null");
        Preconditions.checkNotNull(neededPermissions, "Needed permission cannot be null");

        if (texts.isEmpty()) return;

        for (String permission : neededPermissions) {
            if (permission == null) continue;


            for (String text : texts) {
                if (text == null) continue;

                server.getAllPlayers().stream()
                        .filter(p -> p.hasPermission(permission))
                        .forEach(p -> p.sendMessage(deserialize(text, tagResolver)));
            }
        }
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s with some permissions.
     * The texts are deserialized before being sent.
     *
     * @param texts             The texts to send.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @throws NullPointerException if the texts or the receiver is null.
     */
    public <T extends Collection<String>> void broadcast(@NotNull T texts, @NotNull String[] neededPermissions) {
        broadcast(texts, (TagResolver) null, neededPermissions);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s with some permissions.
     *
     * @param texts             The texts to send.
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param stripFormatting   Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts or the neededPermissions is null.
     */
    public <T extends Collection<String>> void broadcast(@NotNull T texts, TagResolver tagResolver, @NotNull String[] neededPermissions, boolean stripFormatting) {
        if (stripFormatting) {
            broadcast(stripFormatting(texts, tagResolver), tagResolver, neededPermissions);
        } else {
            broadcast(texts, tagResolver, neededPermissions);
        }
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s with some permissions.
     *
     * @param texts             The texts to send.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param stripFormatting   Whether to strip formatting from the texts before sending it.
     */
    public <T extends Collection<String>> void broadcast(@NotNull T texts, @NotNull String[] neededPermissions, boolean stripFormatting) {
        broadcast(texts, null, neededPermissions, stripFormatting);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s with some permissions.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts             The texts to send.
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param sender            The sender of the text.
     * @param permissions       The permissions to check.
     * @throws NullPointerException if the texts, the neededPermissions, the sender or The permissions is null.
     */
    public <T extends Collection<String>> void broadcast(@NotNull T texts, TagResolver tagResolver, @NotNull String[] neededPermissions, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        broadcast(texts, tagResolver, neededPermissions, !hasPermission);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s with some permissions.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the texts, the neededPermissions, the sender or The permissions is null.
     */
    public <T extends Collection<String>> void broadcast(@NotNull T texts, @NotNull String[] neededPermissions, @NotNull CommandSource sender, @NotNull String... permissions) {
        broadcast(texts, null, neededPermissions, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    PERMISSIONS (COLLECTION) BROADCAST    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a text to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param text              The text to send.
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @throws NullPointerException if the text or the neededPermissions is null.
     */
    public <P extends Collection<String>> void broadcast(@NotNull String text, TagResolver tagResolver, @NotNull P neededPermissions) {
        Preconditions.checkNotNull(text, "Text cannot be null");
        Preconditions.checkNotNull(neededPermissions, "Needed permission cannot be null");

        for (String permission : neededPermissions) {
            if (permission == null) continue;

            server.getAllPlayers().stream()
                    .filter(p -> p.hasPermission(permission))
                    .forEach(p -> p.sendMessage(deserialize(text, tagResolver)));
        }
    }

    /**
     * Broadcasts a text to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param text              The text to send.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @throws NullPointerException if the text or the neededPermissions is null.
     */
    public <P extends Collection<String>> void broadcast(@NotNull String text, @NotNull P neededPermissions) {
        broadcast(text, (TagResolver) null, neededPermissions);
    }

    /**
     * Broadcasts a text to all {@link Player}s with some permissions.
     *
     * @param text              The text to send.
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param stripFormatting   Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text or the neededPermissions is null.
     */
    public <P extends Collection<String>> void broadcast(@NotNull String text, TagResolver tagResolver, @NotNull P neededPermissions, boolean stripFormatting) {
        if (stripFormatting) {
            broadcast(stripFormatting(text, tagResolver), tagResolver, neededPermissions);
        } else {
            broadcast(text, tagResolver, neededPermissions);
        }
    }

    /**
     * Broadcasts a text to all {@link Player}s with some permissions.
     *
     * @param text              The text to send.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param stripFormatting   Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text or the neededPermissions is null.
     */
    public <P extends Collection<String>> void broadcast(@NotNull String text, @NotNull P neededPermissions, boolean stripFormatting) {
        broadcast(text, null, neededPermissions, stripFormatting);
    }

    /**
     * Broadcasts a text to all {@link Player}s with some permissions.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text              The text to send.
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param sender            The sender of the text.
     * @param permissions       The permissions to check.
     * @throws NullPointerException if the text, the neededPermissions, the sender or The permissions is null.
     */
    public <P extends Collection<String>> void broadcast(@NotNull String text, TagResolver tagResolver, @NotNull P neededPermissions, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        broadcast(text, tagResolver, neededPermissions, !hasPermission);
    }

    /**
     * Broadcasts a text to all {@link Player}s with some permissions.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text              The text to send.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param sender            The sender of the text.
     * @param permissions       The permissions to check.
     * @throws NullPointerException if the text, the neededPermissions, the sender or The permissions is null.
     */
    public <P extends Collection<String>> void broadcast(@NotNull String text, @NotNull P neededPermissions, @NotNull CommandSource sender, @NotNull String... permissions) {
        broadcast(text, null, neededPermissions, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    PERMISSIONS (ARRAY) BROADCAST MULTIPLE TEXTS (ARRAY)    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a list of texts to all {@link Player}s with some permissions.
     * The texts are deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException if the texts or the receiver is null.
     */
    public <P extends Collection<String>> void broadcast(@NotNull String[] texts, TagResolver tagResolver, @NotNull P neededPermissions) {
        Preconditions.checkNotNull(texts, "Texts cannot be null");
        Preconditions.checkNotNull(neededPermissions, "Needed permission cannot be null");

        if (texts.length == 0) return;

        for (String permission : neededPermissions) {
            if (permission == null) continue;

            for (String text : texts) {
                if (text == null) continue;

                server.getAllPlayers().stream()
                        .filter(p -> p.hasPermission(permission))
                        .forEach(p -> p.sendMessage(deserialize(text, tagResolver)));
            }
        }
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s with some permissions.
     * The texts are deserialized before being sent.
     *
     * @param texts             The texts to send.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @throws NullPointerException if the texts or the receiver is null.
     */
    public <P extends Collection<String>> void broadcast(@NotNull String[] texts, @NotNull P neededPermissions) {
        broadcast(texts, (TagResolver) null, neededPermissions);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s with some permissions.
     *
     * @param texts             The texts to send.
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param stripFormatting   Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts or the neededPermissions is null.
     */
    public <P extends Collection<String>> void broadcast(@NotNull String[] texts, TagResolver tagResolver, @NotNull P neededPermissions, boolean stripFormatting) {
        if (stripFormatting) {
            broadcast(stripFormatting(texts, tagResolver), tagResolver, neededPermissions);
        } else {
            broadcast(texts, tagResolver, neededPermissions);
        }
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s with some permissions.
     *
     * @param texts             The texts to send.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param stripFormatting   Whether to strip formatting from the texts before sending it.
     */
    public <P extends Collection<String>> void broadcast(@NotNull String[] texts, @NotNull P neededPermissions, boolean stripFormatting) {
        broadcast(texts, null, neededPermissions, stripFormatting);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s with some permissions.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts             The texts to send.
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param sender            The sender of the text.
     * @param permissions       The permissions to check.
     * @throws NullPointerException if the texts, the neededPermissions, the sender or The permissions is null.
     */
    public <P extends Collection<String>> void broadcast(@NotNull String[] texts, TagResolver tagResolver, @NotNull P neededPermissions, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        broadcast(texts, tagResolver, neededPermissions, !hasPermission);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s with some permissions.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the texts, the neededPermissions, the sender or The permissions is null.
     */
    public <P extends Collection<String>> void broadcast(@NotNull String[] texts, @NotNull P neededPermissions, @NotNull CommandSource sender, @NotNull String... permissions) {
        broadcast(texts, null, neededPermissions, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    PERMISSIONS (ARRAY) BROADCAST MULTIPLE TEXTS (COLLECTION)    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a list of texts to all {@link Player}s with some permissions.
     * The texts are deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException if the texts or the receiver is null.
     */
    public <P extends Collection<String>, T extends Collection<String>> void broadcast(@NotNull T texts, TagResolver tagResolver, @NotNull P neededPermissions) {
        Preconditions.checkNotNull(texts, "Texts cannot be null");
        Preconditions.checkNotNull(neededPermissions, "Needed permission cannot be null");

        if (texts.isEmpty()) return;

        for (String permission : neededPermissions) {
            if (permission == null) continue;

            for (String text : texts) {
                if (text == null) continue;

                server.getAllPlayers().stream()
                        .filter(p -> p.hasPermission(permission))
                        .forEach(p -> p.sendMessage(deserialize(text, tagResolver)));
            }
        }
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s with some permissions.
     * The texts are deserialized before being sent.
     *
     * @param texts             The texts to send.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @throws NullPointerException if the texts or the receiver is null.
     */
    public <P extends Collection<String>, T extends Collection<String>> void broadcast(@NotNull T texts, @NotNull P neededPermissions) {
        broadcast(texts, (TagResolver) null, neededPermissions);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s with some permissions.
     *
     * @param texts             The texts to send.
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param stripFormatting   Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts or the neededPermissions is null.
     */
    public <P extends Collection<String>, T extends Collection<String>> void broadcast(@NotNull T texts, TagResolver tagResolver, @NotNull P neededPermissions, boolean stripFormatting) {
        if (stripFormatting) {
            broadcast(stripFormatting(texts, tagResolver), tagResolver, neededPermissions);
        } else {
            broadcast(texts, tagResolver, neededPermissions);
        }
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s with some permissions.
     *
     * @param texts             The texts to send.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param stripFormatting   Whether to strip formatting from the texts before sending it.
     */
    public <P extends Collection<String>, T extends Collection<String>> void broadcast(@NotNull T texts, @NotNull P neededPermissions, boolean stripFormatting) {
        broadcast(texts, null, neededPermissions, stripFormatting);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s with some permissions.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts             The texts to send.
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param sender            The sender of the text.
     * @param permissions       The permissions to check.
     * @throws NullPointerException if the texts, the neededPermissions, the sender or The permissions is null.
     */
    public <P extends Collection<String>, T extends Collection<String>> void broadcast(@NotNull T texts, TagResolver tagResolver, @NotNull P neededPermissions, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        broadcast(texts, tagResolver, neededPermissions, !hasPermission);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s with some permissions.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the texts, the neededPermissions, the sender or The permissions is null.
     */
    public <P extends Collection<String>, T extends Collection<String>> void broadcast(@NotNull T texts, @NotNull P neededPermissions, @NotNull CommandSource sender, @NotNull String... permissions) {
        broadcast(texts, null, neededPermissions, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    ACTION BAR    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Sends an action bar text to a {@link Player}.
     * The text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param receiver    The receiver of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException if the text or the receiver is null.
     */
    public void sendActionBar(@NotNull String text, @NotNull Player receiver, TagResolver tagResolver) {
        Preconditions.checkNotNull(text, "Text cannot be null");
        Preconditions.checkNotNull(receiver, "Receiver cannot be null");

        receiver.sendActionBar(deserialize(text, tagResolver));
    }

    /**
     * Sends an action bar text to a {@link Player}.
     * The text is deserialized before being sent.
     *
     * @param text     The text to send.
     * @param receiver The receiver of the text.
     * @throws NullPointerException if the text or the receiver is null.
     */
    public void sendActionBar(@NotNull String text, @NotNull Player receiver) {
        sendActionBar(text, receiver, null);
    }

    /**
     * Sends an action bar text to a {@link Player}.
     *
     * @param text            The text to send.
     * @param receiver        The receiver of the text.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text or the receiver is null.
     */
    public void sendActionBar(@NotNull String text, @NotNull Player receiver, TagResolver tagResolver, boolean stripFormatting) {
        if (stripFormatting) {
            sendActionBar(stripFormatting(text, tagResolver), receiver, tagResolver);
        } else {
            sendActionBar(text, receiver, tagResolver);
        }
    }

    /**
     * Sends an action bar text to a {@link Player}.
     *
     * @param text            The text to send.
     * @param receiver        The receiver of the text.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text or the receiver is null.
     */
    public void sendActionBar(@NotNull String text, @NotNull Player receiver, boolean stripFormatting) {
        sendActionBar(text, receiver, null, stripFormatting);
    }

    /**
     * Sends an action bar text to a {@link Player}.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param receiver    The receiver of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the receiver, the sender or the permissions is null.
     */
    public void sendActionBar(@NotNull String text, @NotNull Player receiver, TagResolver tagResolver, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        sendActionBar(text, receiver, tagResolver, !hasPermission);
    }

    /**
     * Sends an action bar text to a {@link Player}.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param receiver    The receiver of the text.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the receiver or the sender or the permissions is null.
     */
    public void sendActionBar(@NotNull String text, @NotNull Player receiver, @NotNull CommandSource sender, @NotNull String... permissions) {
        sendActionBar(text, receiver, null, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    ACTION BAR TO MULTIPLE RECEIVERS (ARRAY)    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Sends an action bar text to a group of {@link Player}s.
     * The text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param receivers   The receivers of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException if the text or the receivers is null.
     */
    public void sendActionBar(@NotNull String text, @NotNull Player[] receivers, TagResolver tagResolver) {
        Preconditions.checkNotNull(receivers, "Receivers cannot be null");

        for (Player receiver : receivers) {
            if (receiver == null) continue;
            sendActionBar(text, receiver, tagResolver);
        }
    }

    /**
     * Sends an action bar text to a group of {@link Player}s.
     * The text is deserialized before being sent.
     *
     * @param text      The text to send.
     * @param receivers The receivers of the text.
     * @throws NullPointerException if the text or the receivers is null.
     */
    public void sendActionBar(@NotNull String text, @NotNull Player[] receivers) {
        sendActionBar(text, receivers, null);
    }

    /**
     * Sends an action bar text to a group of {@link Player}s.
     *
     * @param text            The text to send.
     * @param receivers       The receivers of the text.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text or the receivers is null.
     */
    public void sendActionBar(@NotNull String text, @NotNull Player[] receivers, TagResolver tagResolver, boolean stripFormatting) {
        if (stripFormatting) {
            sendActionBar(stripFormatting(text, tagResolver), receivers, tagResolver);
        } else {
            sendActionBar(text, receivers, tagResolver);
        }
    }

    /**
     * Sends an action bar text to a group of {@link Player}s.
     *
     * @param text            The text to send.
     * @param receivers       The receivers of the text.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text or the receivers is null.
     */
    public void sendActionBar(@NotNull String text, @NotNull Player[] receivers, boolean stripFormatting) {
        sendActionBar(text, receivers, null, stripFormatting);
    }

    /**
     * Sends an action bar text to a group of {@link Player}s.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param receivers   The receivers of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the receivers or the sender or the permissions is null.
     */
    public void sendActionBar(@NotNull String text, @NotNull Player[] receivers, TagResolver tagResolver, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        sendActionBar(text, receivers, tagResolver, !hasPermission);
    }

    /**
     * Sends an action bar text to a group of {@link Player}s.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param receivers   The receivers of the text.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the receivers, the sender or the permissions is null.
     */
    public void sendActionBar(@NotNull String text, @NotNull Player[] receivers, @NotNull CommandSource sender, @NotNull String... permissions) {
        sendActionBar(text, receivers, null, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    ACTION BAR TO MULTIPLE RECEIVERS (COLLECTION)    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Sends an action bar text to a group of {@link Player}s.
     * The text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param receivers   The receivers of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException if the text or the receivers is null.
     */
    public <R extends Collection<Player>> void sendActionBar(@NotNull String text, @NotNull R receivers, TagResolver tagResolver) {
        Preconditions.checkNotNull(receivers, "Receivers cannot be null");

        for (Player receiver : receivers) {
            if (receiver == null) continue;
            sendActionBar(text, receiver, tagResolver);
        }
    }

    /**
     * Sends an action bar text to a group of {@link Player}s.
     * The text is deserialized before being sent.
     *
     * @param text      The text to send.
     * @param receivers The receivers of the text.
     * @throws NullPointerException if the text or the receivers is null.
     */
    public <R extends Collection<Player>> void sendActionBar(@NotNull String text, @NotNull R receivers) {
        sendActionBar(text, receivers, null);
    }

    /**
     * Sends an action bar text to a group of {@link Player}s.
     *
     * @param text            The text to send.
     * @param receivers       The receivers of the text.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text or the receivers is null.
     */
    public <R extends Collection<Player>> void sendActionBar(@NotNull String text, @NotNull R receivers, TagResolver tagResolver, boolean stripFormatting) {
        if (stripFormatting) {
            sendActionBar(stripFormatting(text, tagResolver), receivers, tagResolver);
        } else {
            sendActionBar(text, receivers, tagResolver);
        }
    }

    /**
     * Sends an action bar text to a group of {@link Player}s.
     *
     * @param text            The text to send.
     * @param receivers       The receivers of the text.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text or the receivers is null.
     */
    public <R extends Collection<Player>> void sendActionBar(@NotNull String text, @NotNull R receivers, boolean stripFormatting) {
        sendActionBar(text, receivers, null, stripFormatting);
    }

    /**
     * Sends an action bar text to a group of {@link Player}s.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param receivers   The receivers of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the receivers, the sender or the permissions is null.
     */
    public <R extends Collection<Player>> void sendActionBar(@NotNull String text, @NotNull R receivers, TagResolver tagResolver, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        sendActionBar(text, receivers, tagResolver, !hasPermission);
    }

    /**
     * Sends an action bar text to a group of {@link Player}s.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param receivers   The receivers of the text.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the receivers, the sender or the permissions is null.
     */
    public <R extends Collection<Player>> void sendActionBar(@NotNull String text, @NotNull R receivers, @NotNull CommandSource sender, @NotNull String... permissions) {
        sendActionBar(text, receivers, null, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    BROADCAST    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts an action bar text to all {@link Player}s on the server.
     * The text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException if the text is null.
     */
    public void broadcastActionBar(@NotNull String text, TagResolver tagResolver) {
        Preconditions.checkNotNull(text, "Text cannot be null");

        server.getAllPlayers().forEach(p -> p.sendActionBar(deserialize(text, tagResolver)));
    }

    /**
     * Broadcasts an action bar text to all {@link Player}s on the server.
     * The text is deserialized before being sent.
     *
     * @param text The text to send.
     * @throws NullPointerException if the text is null.
     */
    public void broadcastActionBar(@NotNull String text) {
        broadcastActionBar(text, (TagResolver) null);
    }

    /**
     * Broadcasts an action bar text to all {@link Player}s on the server.
     *
     * @param text            The text to send.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text is null.
     */
    public void broadcastActionBar(@NotNull String text, TagResolver tagResolver, boolean stripFormatting) {
        if (stripFormatting) {
            broadcastActionBar(stripFormatting(text, tagResolver), tagResolver);
        } else {
            broadcastActionBar(text, tagResolver);
        }
    }

    /**
     * Broadcasts an action bar text to all {@link Player}s on the server.
     *
     * @param text            The text to send.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text is null.
     */
    public void broadcastActionBar(@NotNull String text, boolean stripFormatting) {
        broadcastActionBar(text, (TagResolver) null, stripFormatting);
    }

    /**
     * Broadcasts an action bar text to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the sender or the permissions is null.
     */
    public void broadcastActionBar(@NotNull String text, TagResolver tagResolver, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        broadcastActionBar(text, tagResolver, !hasPermission);
    }

    /**
     * Broadcasts an action bar text to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the sender or the permissions is null.
     */
    public void broadcastActionBar(@NotNull String text, @NotNull CommandSource sender, @NotNull String... permissions) {
        broadcastActionBar(text, (TagResolver) null, sender, permissions);
    }


    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    SERVER BROADCAST    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts an action bar text to all {@link Player}s in a server.
     * The text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param server      The {@link RegisteredServer} to send the text to.
     * @throws NullPointerException if the text or the server is null.
     */
    public void broadcastActionBar(@NotNull String text, TagResolver tagResolver, @NotNull RegisteredServer server) {
        Preconditions.checkNotNull(text, "Text cannot be null");
        Preconditions.checkNotNull(server, "World cannot be null");

        server.getPlayersConnected().forEach(p -> p.sendActionBar(deserialize(text, tagResolver)));
    }

    /**
     * Broadcasts an action bar text to all {@link Player}s on the server.
     * The text is deserialized before being sent.
     *
     * @param text   The text to send.
     * @param server The {@link RegisteredServer} to send the text to.
     * @throws NullPointerException if the text or the server is null.
     */
    public void broadcastActionBar(@NotNull String text, @NotNull RegisteredServer server) {
        broadcastActionBar(text, null, server);
    }

    /**
     * Broadcasts an action bar text to all {@link Player}s on the server.
     *
     * @param text            The text to send.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param server          The {@link RegisteredServer} to send the text to.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text or the server is null.
     */
    public void broadcastActionBar(@NotNull String text, TagResolver tagResolver, @NotNull RegisteredServer server, boolean stripFormatting) {
        if (stripFormatting) {
            broadcastActionBar(stripFormatting(text, tagResolver), tagResolver, server);
        } else {
            broadcastActionBar(text, tagResolver, server);
        }
    }

    /**
     * Broadcasts an action bar text to all {@link Player}s on the server.
     *
     * @param text            The text to send.
     * @param server          The {@link RegisteredServer} to send the text to.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text or the server is null.
     */
    public void broadcastActionBar(@NotNull String text, @NotNull RegisteredServer server, boolean stripFormatting) {
        broadcastActionBar(text, null, server, stripFormatting);
    }

    /**
     * Broadcasts an action bar text to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param server      The {@link RegisteredServer} to send the text to.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the server, the sender or the permissions is null.
     */
    public void broadcastActionBar(@NotNull String text, TagResolver tagResolver, @NotNull RegisteredServer server, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        broadcastActionBar(text, tagResolver, server, !hasPermission);
    }

    /**
     * Broadcasts an action bar text to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param server      The {@link RegisteredServer} to send the text to.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the server, the sender or the permissions is null.
     */
    public void broadcastActionBar(@NotNull String text, @NotNull RegisteredServer server, @NotNull CommandSource sender, @NotNull String... permissions) {
        broadcastActionBar(text, null, server, sender, permissions);
    }


    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    PERMISSION BROADCAST    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts an action bar text to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param text             The text to send.
     * @param tagResolver      The {@link TagResolver} for any additional tags to handle.
     * @param neededPermission The permission needed to receive the broadcast.
     * @throws NullPointerException if the text or the neededPermission is null.
     */
    public void broadcastActionBar(@NotNull String text, TagResolver tagResolver, @NotNull String neededPermission) {
        Preconditions.checkNotNull(text, "Text cannot be null");
        Preconditions.checkNotNull(neededPermission, "Needed permission cannot be null");

        server.getAllPlayers().stream()
                .filter(p -> p.hasPermission(neededPermission))
                .forEach(p -> p.sendActionBar(deserialize(text, tagResolver)));

    }

    /**
     * Broadcasts an action bar text to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param text             The text to send.
     * @param neededPermission The permission needed to receive the broadcast.
     * @throws NullPointerException if the text or the neededPermission is null.
     */
    public void broadcastActionBar(@NotNull String text, @NotNull String neededPermission) {
        broadcastActionBar(text, null, neededPermission);
    }

    /**
     * Broadcasts an action bar text to all {@link Player}s with some permissions.
     *
     * @param text             The text to send.
     * @param tagResolver      The {@link TagResolver} for any additional tags to handle.
     * @param neededPermission The permission needed to receive the broadcast.
     * @param stripFormatting  Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text or the neededPermission is null.
     */
    public void broadcastActionBar(@NotNull String text, TagResolver tagResolver, @NotNull String neededPermission, boolean stripFormatting) {
        if (stripFormatting) {
            broadcastActionBar(stripFormatting(text, tagResolver), tagResolver, neededPermission);
        } else {
            broadcastActionBar(text, tagResolver, neededPermission);
        }
    }

    /**
     * Broadcasts an action bar text to all {@link Player}s with some permissions.
     *
     * @param text             The text to send.
     * @param neededPermission The permission needed to receive the broadcast.
     * @param stripFormatting  Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text or the neededPermission is null.
     */
    public void broadcastActionBar(@NotNull String text, @NotNull String neededPermission, boolean stripFormatting) {
        broadcastActionBar(text, null, neededPermission, stripFormatting);
    }

    /**
     * Broadcasts an action bar text to all {@link Player}s with some permissions.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text             The text to send.
     * @param tagResolver      The {@link TagResolver} for any additional tags to handle.
     * @param neededPermission The permission needed to receive the broadcast.
     * @param sender           The sender of the text.
     * @param permissions      The permissions to check.
     * @throws NullPointerException if the text, the neededPermission, the sender or the permissions is null.
     */
    public void broadcastActionBar(@NotNull String text, TagResolver tagResolver, @NotNull String neededPermission, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        broadcastActionBar(text, tagResolver, neededPermission, !hasPermission);
    }

    /**
     * Broadcasts an action bar text to all {@link Player}s with some permissions.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text             The text to send.
     * @param neededPermission The permission needed to receive the broadcast.
     * @param sender           The sender of the text.
     * @param permissions      The permissions to check.
     * @throws NullPointerException if the text, the neededPermission, the sender or the permissions is null.
     */
    public void broadcastActionBar(@NotNull String text, @NotNull String neededPermission, @NotNull CommandSource sender, @NotNull String... permissions) {
        broadcastActionBar(text, null, neededPermission, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    PERMISSIONS (ARRAY) BROADCAST    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts an action bar text to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param text              The text to send.
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @throws NullPointerException if the text or the neededPermissions is null.
     */
    public void broadcastActionBar(@NotNull String text, TagResolver tagResolver, @NotNull String[] neededPermissions) {
        Preconditions.checkNotNull(text, "Text cannot be null");
        Preconditions.checkNotNull(neededPermissions, "Needed permission cannot be null");

        for (String permission : neededPermissions) {
            if (permission == null) continue;
            server.getAllPlayers().stream()
                    .filter(p -> p.hasPermission(permission))
                    .forEach(p -> p.sendActionBar(deserialize(text, tagResolver)));
        }
    }

    /**
     * Broadcasts an action bar text to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param text              The text to send.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @throws NullPointerException if the text or the neededPermissions is null.
     */
    public void broadcastActionBar(@NotNull String text, @NotNull String[] neededPermissions) {
        broadcastActionBar(text, (TagResolver) null, neededPermissions);
    }

    /**
     * Broadcasts an action bar text to all {@link Player}s with some permissions.
     *
     * @param text              The text to send.
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param stripFormatting   Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text or the neededPermissions is null.
     */
    public void broadcastActionBar(@NotNull String text, TagResolver tagResolver, @NotNull String[] neededPermissions, boolean stripFormatting) {
        if (stripFormatting) {
            broadcastActionBar(stripFormatting(text, tagResolver), tagResolver, neededPermissions);
        } else {
            broadcastActionBar(text, tagResolver, neededPermissions);
        }
    }

    /**
     * Broadcasts an action bar text to all {@link Player}s with some permissions.
     *
     * @param text              The text to send.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param stripFormatting   Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text or the neededPermissions is null.
     */
    public void broadcastActionBar(@NotNull String text, @NotNull String[] neededPermissions, boolean stripFormatting) {
        broadcastActionBar(text, null, neededPermissions, stripFormatting);
    }

    /**
     * Broadcasts an action bar text to all {@link Player}s with some permissions.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text              The text to send.
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param sender            The sender of the text.
     * @param permissions       The permissions to check.
     * @throws NullPointerException if the text, the neededPermissions, the sender or The permissions is null.
     */
    public void broadcastActionBar(@NotNull String text, TagResolver tagResolver, @NotNull String[] neededPermissions, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        broadcastActionBar(text, tagResolver, neededPermissions, !hasPermission);
    }

    /**
     * Broadcasts an action bar text to all {@link Player}s with some permissions.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text              The text to send.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param sender            The sender of the text.
     * @param permissions       The permissions to check.
     * @throws NullPointerException if the text, the neededPermissions, the sender or The permissions is null.
     */
    public void broadcastActionBar(@NotNull String text, @NotNull String[] neededPermissions, @NotNull CommandSource sender, @NotNull String... permissions) {
        broadcastActionBar(text, null, neededPermissions, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    PERMISSIONS (COLLECTION) BROADCAST    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts an action bar text to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param text              The text to send.
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @throws NullPointerException if the text or the neededPermissions is null.
     */
    public <P extends Collection<String>> void broadcastActionBar(@NotNull String text, TagResolver tagResolver, @NotNull P neededPermissions) {
        Preconditions.checkNotNull(text, "Text cannot be null");
        Preconditions.checkNotNull(neededPermissions, "Needed permission cannot be null");

        for (String permission : neededPermissions) {
            if (permission == null) continue;

            server.getAllPlayers().stream()
                    .filter(p -> p.hasPermission(permission))
                    .forEach(p -> p.sendActionBar(deserialize(text, tagResolver)));
        }
    }

    /**
     * Broadcasts an action bar text to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param text              The text to send.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @throws NullPointerException if the text or the neededPermissions is null.
     */
    public <P extends Collection<String>> void broadcastActionBar(@NotNull String text, @NotNull P neededPermissions) {
        broadcastActionBar(text, (TagResolver) null, neededPermissions);
    }

    /**
     * Broadcasts an action bar text to all {@link Player}s with some permissions.
     *
     * @param text              The text to send.
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param stripFormatting   Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text or the neededPermissions is null.
     */
    public <P extends Collection<String>> void broadcastActionBar(@NotNull String text, TagResolver tagResolver, @NotNull P neededPermissions, boolean stripFormatting) {
        if (stripFormatting) {
            broadcastActionBar(stripFormatting(text, tagResolver), tagResolver, neededPermissions);
        } else {
            broadcastActionBar(text, tagResolver, neededPermissions);
        }
    }

    /**
     * Broadcasts an action bar text to all {@link Player}s with some permissions.
     *
     * @param text              The text to send.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param stripFormatting   Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text or the neededPermissions is null.
     */
    public <P extends Collection<String>> void broadcastActionBar(@NotNull String text, @NotNull P neededPermissions, boolean stripFormatting) {
        broadcastActionBar(text, null, neededPermissions, stripFormatting);
    }

    /**
     * Broadcasts an action bar text to all {@link Player}s with some permissions.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text              The text to send.
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param sender            The sender of the text.
     * @param permissions       The permissions to check.
     * @throws NullPointerException if the text, the neededPermissions, the sender or The permissions is null.
     */
    public <P extends Collection<String>> void broadcastActionBar(@NotNull String text, TagResolver tagResolver, @NotNull P neededPermissions, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        broadcastActionBar(text, tagResolver, neededPermissions, !hasPermission);
    }

    /**
     * Broadcasts an action bar text to all {@link Player}s with some permissions.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text              The text to send.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param sender            The sender of the text.
     * @param permissions       The permissions to check.
     * @throws NullPointerException if the text, the neededPermissions, the sender or The permissions is null.
     */
    public <P extends Collection<String>> void broadcastActionBar(@NotNull String text, @NotNull P neededPermissions, @NotNull CommandSource sender, @NotNull String... permissions) {
        broadcastActionBar(text, null, neededPermissions, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    TITLE    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Sends a title to a {@link Player}.
     * The text is deserialized before being sent.
     *
     * @param title       The title to send.
     * @param subtitle    The subtitle to send.
     * @param fadeIn      The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay        The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut     The time in milliseconds for the title to fade out. (default: 1000)
     * @param receiver    The receiver of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void sendTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, @NotNull Player receiver, TagResolver tagResolver) {
        Preconditions.checkArgument((title == null && subtitle != null) || (title != null && subtitle == null), "Both title and subtitle cannot be null simultaneously");
        Preconditions.checkNotNull(receiver, "Receiver cannot be null");

        if (title != null) receiver.sendTitlePart(TitlePart.TITLE, deserialize(title, tagResolver));
        if (subtitle != null) receiver.sendTitlePart(TitlePart.SUBTITLE, deserialize(subtitle, tagResolver));
        receiver.sendTitlePart(TitlePart.TIMES, Title.Times.times(
                Duration.of(fadeIn != null ? fadeIn : 1000, ChronoUnit.MILLIS),
                Duration.of(stay != null ? stay : 3000, ChronoUnit.MILLIS),
                Duration.of(fadeOut != null ? fadeOut : 1000, ChronoUnit.MILLIS)
        ));
    }

    /**
     * Sends a title to a {@link Player}.
     * The text is deserialized before being sent.
     *
     * @param title    The title to send.
     * @param subtitle The subtitle to send.
     * @param fadeIn   The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay     The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut  The time in milliseconds for the title to fade out. (default: 1000)
     * @param receiver The receiver of the text.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void sendTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, @NotNull Player receiver) {
        sendTitle(title, subtitle, fadeIn, stay, fadeOut, receiver, null);
    }

    /**
     * Sends a title to a {@link Player}.
     * The text is deserialized before being sent.
     *
     * @param title       The title to send.
     * @param subtitle    The subtitle to send.
     * @param receiver    The receiver of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void sendTitle(String title, String subtitle, @NotNull Player receiver, TagResolver tagResolver) {
        sendTitle(title, subtitle, null, null, null, receiver, tagResolver);
    }

    /**
     * Sends a title to a {@link Player}.
     * The text is deserialized before being sent.
     *
     * @param title    The title to send.
     * @param subtitle The subtitle to send.
     * @param receiver The receiver of the text.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void sendTitle(String title, String subtitle, @NotNull Player receiver) {
        sendTitle(title, subtitle, null, null, null, receiver, null);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    TITLE STRIP FORMATTING    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Sends a title to a {@link Player}.
     * The text is deserialized before being sent.
     *
     * @param title           The title to send.
     * @param subtitle        The subtitle to send.
     * @param fadeIn          The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay            The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut         The time in milliseconds for the title to fade out. (default: 1000)
     * @param receiver        The receiver of the text.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void sendTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, @NotNull Player receiver, TagResolver tagResolver, boolean stripFormatting) {
        if (stripFormatting) {
            sendTitle(stripFormatting(title, tagResolver), stripFormatting(subtitle, tagResolver), fadeIn, stay, fadeOut, receiver, tagResolver);
        } else {
            sendTitle(title, subtitle, fadeIn, stay, fadeOut, receiver, tagResolver);
        }
    }

    /**
     * Sends a title to a {@link Player}.
     * The text is deserialized before being sent.
     *
     * @param title           The title to send.
     * @param subtitle        The subtitle to send.
     * @param fadeIn          The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay            The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut         The time in milliseconds for the title to fade out. (default: 1000)
     * @param receiver        The receiver of the text.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void sendTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, @NotNull Player receiver, boolean stripFormatting) {
        sendTitle(title, subtitle, fadeIn, stay, fadeOut, receiver, null, stripFormatting);
    }

    /**
     * Sends a title to a {@link Player}.
     * The text is deserialized before being sent.
     *
     * @param title           The title to send.
     * @param subtitle        The subtitle to send.
     * @param receiver        The receiver of the text.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void sendTitle(String title, String subtitle, @NotNull Player receiver, TagResolver tagResolver, boolean stripFormatting) {
        sendTitle(title, subtitle, null, null, null, receiver, tagResolver, stripFormatting);
    }

    /**
     * Sends a title to a {@link Player}.
     * The text is deserialized before being sent.
     *
     * @param title           The title to send.
     * @param subtitle        The subtitle to send.
     * @param receiver        The receiver of the text.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void sendTitle(String title, String subtitle, @NotNull Player receiver, boolean stripFormatting) {
        sendTitle(title, subtitle, null, null, null, receiver, null, stripFormatting);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    TITLE PERMISSION BASED FORMATTING    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Sends a title to a {@link Player}.
     * The text is deserialized before being sent.
     *
     * @param title       The title to send.
     * @param subtitle    The subtitle to send.
     * @param fadeIn      The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay        The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut     The time in milliseconds for the title to fade out. (default: 1000)
     * @param receiver    The receiver of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void sendTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, @NotNull Player receiver, TagResolver tagResolver, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        sendTitle(title, subtitle, fadeIn, stay, fadeOut, receiver, tagResolver, !hasPermission);
    }

    /**
     * Sends a title to a {@link Player}.
     * The text is deserialized before being sent.
     *
     * @param title       The title to send.
     * @param subtitle    The subtitle to send.
     * @param fadeIn      The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay        The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut     The time in milliseconds for the title to fade out. (default: 1000)
     * @param receiver    The receiver of the text.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void sendTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, @NotNull Player receiver, @NotNull CommandSource sender, @NotNull String... permissions) {
        sendTitle(title, subtitle, fadeIn, stay, fadeOut, receiver, null, sender, permissions);
    }

    /**
     * Sends a title to a {@link Player}.
     * The text is deserialized before being sent.
     *
     * @param title       The title to send.
     * @param subtitle    The subtitle to send.
     * @param receiver    The receiver of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void sendTitle(String title, String subtitle, @NotNull Player receiver, TagResolver tagResolver, @NotNull CommandSource sender, @NotNull String... permissions) {
        sendTitle(title, subtitle, null, null, null, receiver, tagResolver, sender, permissions);
    }

    /**
     * Sends a title to a {@link Player}.
     * The text is deserialized before being sent.
     *
     * @param title       The title to send.
     * @param subtitle    The subtitle to send.
     * @param receiver    The receiver of the text.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void sendTitle(String title, String subtitle, @NotNull Player receiver, @NotNull CommandSource sender, @NotNull String... permissions) {
        sendTitle(title, subtitle, null, null, null, receiver, null, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    TITLE TO MULTIPLE RECEIVERS (ARRAY)    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Sends a title to a group of {@link Player}s.
     * The text is deserialized before being sent.
     *
     * @param title       The title to send.
     * @param subtitle    The subtitle to send.
     * @param fadeIn      The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay        The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut     The time in milliseconds for the title to fade out. (default: 1000)
     * @param receivers   The receivers of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException     if the receivers is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void sendTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, @NotNull Player[] receivers, TagResolver tagResolver) {
        Preconditions.checkArgument((title == null && subtitle != null) || (title != null && subtitle == null), "Both title and subtitle cannot be null simultaneously");
        Preconditions.checkNotNull(receivers, "Receiver cannot be null");

        for (Player receiver : receivers) {
            if (receiver == null) continue;
            sendTitle(title, subtitle, fadeIn, stay, fadeOut, receiver, tagResolver);
        }
    }

    /**
     * Sends a title to a group of {@link Player}s.
     * The text is deserialized before being sent.
     *
     * @param title     The title to send.
     * @param subtitle  The subtitle to send.
     * @param fadeIn    The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay      The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut   The time in milliseconds for the title to fade out. (default: 1000)
     * @param receivers The receivers of the text.
     * @throws NullPointerException     if the receivers is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void sendTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, @NotNull Player[] receivers) {
        sendTitle(title, subtitle, fadeIn, stay, fadeOut, receivers, null);
    }

    /**
     * Sends a title to a group of {@link Player}s.
     * The text is deserialized before being sent.
     *
     * @param title       The title to send.
     * @param subtitle    The subtitle to send.
     * @param receivers   The receivers of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException     if the receivers is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void sendTitle(String title, String subtitle, @NotNull Player[] receivers, TagResolver tagResolver) {
        sendTitle(title, subtitle, null, null, null, receivers, tagResolver);
    }

    /**
     * Sends a title to a group of {@link Player}s.
     * The text is deserialized before being sent.
     *
     * @param title     The title to send.
     * @param subtitle  The subtitle to send.
     * @param receivers The receivers of the text.
     * @throws NullPointerException     if the receivers is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void sendTitle(String title, String subtitle, @NotNull Player[] receivers) {
        sendTitle(title, subtitle, null, null, null, receivers, null);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    TITLE TO MULTIPLE RECEIVERS (ARRAY) STRIP FORMATTING    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Sends a title to a group of {@link Player}s.
     * The text is deserialized before being sent.
     *
     * @param title           The title to send.
     * @param subtitle        The subtitle to send.
     * @param fadeIn          The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay            The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut         The time in milliseconds for the title to fade out. (default: 1000)
     * @param receivers       The receivers of the text.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException     if the receivers is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void sendTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, @NotNull Player[] receivers, TagResolver tagResolver, boolean stripFormatting) {
        if (stripFormatting) {
            sendTitle(stripFormatting(title, tagResolver), stripFormatting(subtitle, tagResolver), fadeIn, stay, fadeOut, receivers, tagResolver);
        } else {
            sendTitle(title, subtitle, fadeIn, stay, fadeOut, receivers, tagResolver);
        }
    }

    /**
     * Sends a title to a group of {@link Player}s.
     * The text is deserialized before being sent.
     *
     * @param title           The title to send.
     * @param subtitle        The subtitle to send.
     * @param fadeIn          The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay            The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut         The time in milliseconds for the title to fade out. (default: 1000)
     * @param receivers       The receivers of the text.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException     if the receivers is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void sendTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, @NotNull Player[] receivers, boolean stripFormatting) {
        sendTitle(title, subtitle, fadeIn, stay, fadeOut, receivers, null, stripFormatting);
    }

    /**
     * Sends a title to a group of {@link Player}s.
     * The text is deserialized before being sent.
     *
     * @param title           The title to send.
     * @param subtitle        The subtitle to send.
     * @param receivers       The receivers of the text.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException     if the receivers is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void sendTitle(String title, String subtitle, @NotNull Player[] receivers, TagResolver tagResolver, boolean stripFormatting) {
        sendTitle(title, subtitle, null, null, null, receivers, tagResolver, stripFormatting);
    }

    /**
     * Sends a title to a group of {@link Player}s.
     * The text is deserialized before being sent.
     *
     * @param title           The title to send.
     * @param subtitle        The subtitle to send.
     * @param receivers       The receivers of the text.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException     if the receivers is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void sendTitle(String title, String subtitle, @NotNull Player[] receivers, boolean stripFormatting) {
        sendTitle(title, subtitle, null, null, null, receivers, null, stripFormatting);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    TITLE TO MULTIPLE RECEIVERS (ARRAY) PERMISSION BASED FORMATTING    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Sends a title to a group of {@link Player}s.
     * The text is deserialized before being sent.
     *
     * @param title       The title to send.
     * @param subtitle    The subtitle to send.
     * @param fadeIn      The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay        The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut     The time in milliseconds for the title to fade out. (default: 1000)
     * @param receivers   The receivers of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException     if the receivers is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void sendTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, @NotNull Player[] receivers, TagResolver tagResolver, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        sendTitle(title, subtitle, fadeIn, stay, fadeOut, receivers, tagResolver, !hasPermission);
    }

    /**
     * Sends a title to a group of {@link Player}s.
     * The text is deserialized before being sent.
     *
     * @param title       The title to send.
     * @param subtitle    The subtitle to send.
     * @param fadeIn      The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay        The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut     The time in milliseconds for the title to fade out. (default: 1000)
     * @param receivers   The receivers of the text.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException     if the receivers is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void sendTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, @NotNull Player[] receivers, @NotNull CommandSource sender, @NotNull String... permissions) {
        sendTitle(title, subtitle, fadeIn, stay, fadeOut, receivers, null, sender, permissions);
    }

    /**
     * Sends a title to a group of {@link Player}s.
     * The text is deserialized before being sent.
     *
     * @param title       The title to send.
     * @param subtitle    The subtitle to send.
     * @param receivers   The receivers of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException     if the receivers is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void sendTitle(String title, String subtitle, @NotNull Player[] receivers, TagResolver tagResolver, @NotNull CommandSource sender, @NotNull String... permissions) {
        sendTitle(title, subtitle, null, null, null, receivers, tagResolver, sender, permissions);
    }

    /**
     * Sends a title to a group of {@link Player}s.
     * The text is deserialized before being sent.
     *
     * @param title       The title to send.
     * @param subtitle    The subtitle to send.
     * @param receivers   The receivers of the text.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException     if the receivers is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void sendTitle(String title, String subtitle, @NotNull Player[] receivers, @NotNull CommandSource sender, @NotNull String... permissions) {
        sendTitle(title, subtitle, null, null, null, receivers, null, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    TITLE TO MULTIPLE RECEIVERS (COLLECTION)    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Sends a title to a group of {@link Player}s.
     * The text is deserialized before being sent.
     *
     * @param title       The title to send.
     * @param subtitle    The subtitle to send.
     * @param fadeIn      The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay        The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut     The time in milliseconds for the title to fade out. (default: 1000)
     * @param receivers   The receivers of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException     if the receivers is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public <R extends Collection<Player>> void sendTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, @NotNull R receivers, TagResolver tagResolver) {
        Preconditions.checkArgument((title == null && subtitle != null) || (title != null && subtitle == null), "Both title and subtitle cannot be null simultaneously");
        Preconditions.checkNotNull(receivers, "Receiver cannot be null");

        for (Player receiver : receivers) {
            if (receiver == null) continue;
            sendTitle(title, subtitle, fadeIn, stay, fadeOut, receiver, tagResolver);
        }
    }

    /**
     * Sends a title to a group of {@link Player}s.
     * The text is deserialized before being sent.
     *
     * @param title     The title to send.
     * @param subtitle  The subtitle to send.
     * @param fadeIn    The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay      The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut   The time in milliseconds for the title to fade out. (default: 1000)
     * @param receivers The receivers of the text.
     * @throws NullPointerException     if the receivers is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public <R extends Collection<Player>> void sendTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, @NotNull R receivers) {
        sendTitle(title, subtitle, fadeIn, stay, fadeOut, receivers, null);
    }

    /**
     * Sends a title to a group of {@link Player}s.
     * The text is deserialized before being sent.
     *
     * @param title       The title to send.
     * @param subtitle    The subtitle to send.
     * @param receivers   The receivers of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException     if the receivers is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public <R extends Collection<Player>> void sendTitle(String title, String subtitle, @NotNull R receivers, TagResolver tagResolver) {
        sendTitle(title, subtitle, null, null, null, receivers, tagResolver);
    }

    /**
     * Sends a title to a group of {@link Player}s.
     * The text is deserialized before being sent.
     *
     * @param title     The title to send.
     * @param subtitle  The subtitle to send.
     * @param receivers The receivers of the text.
     * @throws NullPointerException     if the receivers is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public <R extends Collection<Player>> void sendTitle(String title, String subtitle, @NotNull R receivers) {
        sendTitle(title, subtitle, null, null, null, receivers, null);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    TITLE TO MULTIPLE RECEIVERS (COLLECTION) STRIP FORMATTING    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Sends a title to a group of {@link Player}s.
     * The text is deserialized before being sent.
     *
     * @param title           The title to send.
     * @param subtitle        The subtitle to send.
     * @param fadeIn          The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay            The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut         The time in milliseconds for the title to fade out. (default: 1000)
     * @param receivers       The receivers of the text.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException     if the receivers is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public <R extends Collection<Player>> void sendTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, @NotNull R receivers, TagResolver tagResolver, boolean stripFormatting) {
        if (stripFormatting) {
            sendTitle(stripFormatting(title, tagResolver), stripFormatting(subtitle, tagResolver), fadeIn, stay, fadeOut, receivers, tagResolver);
        } else {
            sendTitle(title, subtitle, fadeIn, stay, fadeOut, receivers, tagResolver);
        }
    }

    /**
     * Sends a title to a group of {@link Player}s.
     * The text is deserialized before being sent.
     *
     * @param title           The title to send.
     * @param subtitle        The subtitle to send.
     * @param fadeIn          The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay            The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut         The time in milliseconds for the title to fade out. (default: 1000)
     * @param receivers       The receivers of the text.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException     if the receivers is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public <R extends Collection<Player>> void sendTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, @NotNull R receivers, boolean stripFormatting) {
        sendTitle(title, subtitle, fadeIn, stay, fadeOut, receivers, null, stripFormatting);
    }

    /**
     * Sends a title to a group of {@link Player}s.
     * The text is deserialized before being sent.
     *
     * @param title           The title to send.
     * @param subtitle        The subtitle to send.
     * @param receivers       The receivers of the text.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException     if the receivers is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public <R extends Collection<Player>> void sendTitle(String title, String subtitle, @NotNull R receivers, TagResolver tagResolver, boolean stripFormatting) {
        sendTitle(title, subtitle, null, null, null, receivers, tagResolver, stripFormatting);
    }

    /**
     * Sends a title to a group of {@link Player}s.
     * The text is deserialized before being sent.
     *
     * @param title           The title to send.
     * @param subtitle        The subtitle to send.
     * @param receivers       The receivers of the text.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException     if the receivers is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public <R extends Collection<Player>> void sendTitle(String title, String subtitle, @NotNull R receivers, boolean stripFormatting) {
        sendTitle(title, subtitle, null, null, null, receivers, null, stripFormatting);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    TITLE TO MULTIPLE RECEIVERS (COLLECTION) PERMISSION BASED FORMATTING    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Sends a title to a group of {@link Player}s.
     * The text is deserialized before being sent.
     *
     * @param title       The title to send.
     * @param subtitle    The subtitle to send.
     * @param fadeIn      The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay        The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut     The time in milliseconds for the title to fade out. (default: 1000)
     * @param receivers   The receivers of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException     if the receivers is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public <R extends Collection<Player>> void sendTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, @NotNull R receivers, TagResolver tagResolver, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        sendTitle(title, subtitle, fadeIn, stay, fadeOut, receivers, tagResolver, !hasPermission);
    }

    /**
     * Sends a title to a group of {@link Player}s.
     * The text is deserialized before being sent.
     *
     * @param title       The title to send.
     * @param subtitle    The subtitle to send.
     * @param fadeIn      The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay        The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut     The time in milliseconds for the title to fade out. (default: 1000)
     * @param receivers   The receivers of the text.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException     if the receivers is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public <R extends Collection<Player>> void sendTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, @NotNull R receivers, @NotNull CommandSource sender, @NotNull String... permissions) {
        sendTitle(title, subtitle, fadeIn, stay, fadeOut, receivers, null, sender, permissions);
    }

    /**
     * Sends a title to a group of {@link Player}s.
     * The text is deserialized before being sent.
     *
     * @param title       The title to send.
     * @param subtitle    The subtitle to send.
     * @param receivers   The receivers of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException     if the receivers is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public <R extends Collection<Player>> void sendTitle(String title, String subtitle, @NotNull R receivers, TagResolver tagResolver, @NotNull CommandSource sender, @NotNull String... permissions) {
        sendTitle(title, subtitle, null, null, null, receivers, tagResolver, sender, permissions);
    }

    /**
     * Sends a title to a group of {@link Player}s.
     * The text is deserialized before being sent.
     *
     * @param title       The title to send.
     * @param subtitle    The subtitle to send.
     * @param receivers   The receivers of the text.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException     if the receivers is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public <R extends Collection<Player>> void sendTitle(String title, String subtitle, @NotNull R receivers, @NotNull CommandSource sender, @NotNull String... permissions) {
        sendTitle(title, subtitle, null, null, null, receivers, null, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    BROADCAST    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a title to all {@link Player}s on the server.
     * The text is deserialized before being sent.
     *
     * @param title       The title to send.
     * @param subtitle    The subtitle to send.
     * @param fadeIn      The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay        The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut     The time in milliseconds for the title to fade out. (default: 1000)
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, TagResolver tagResolver) {
        Preconditions.checkArgument((title == null && subtitle != null) || (title != null && subtitle == null), "Both title and subtitle cannot be null simultaneously");

        if (title != null) server.getAllPlayers().forEach(p -> p.sendTitlePart(TitlePart.TITLE, deserialize(title, tagResolver)));
        if (subtitle != null) server.getAllPlayers().forEach(p -> p.sendTitlePart(TitlePart.SUBTITLE, deserialize(subtitle, tagResolver)));
        server.getAllPlayers().forEach(p -> p.sendTitlePart(TitlePart.TIMES, Title.Times.times(
                Duration.of(fadeIn != null ? fadeIn : 1000, ChronoUnit.MILLIS),
                Duration.of(stay != null ? stay : 3000, ChronoUnit.MILLIS),
                Duration.of(fadeOut != null ? fadeOut : 1000, ChronoUnit.MILLIS)
        )));
    }

    /**
     * Broadcasts a title to all {@link Player}s on the server.
     * The text is deserialized before being sent.
     *
     * @param title    The title to send.
     * @param subtitle The subtitle to send.
     * @param fadeIn   The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay     The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut  The time in milliseconds for the title to fade out. (default: 1000)
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut) {
        broadcastTitle(title, subtitle, fadeIn, stay, fadeOut, (TagResolver) null);
    }

    /**
     * Broadcasts a title to all {@link Player}s on the server.
     * The text is deserialized before being sent.
     *
     * @param title       The title to send.
     * @param subtitle    The subtitle to send.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, TagResolver tagResolver) {
        broadcastTitle(title, subtitle, null, null, null, tagResolver);
    }

    /**
     * Broadcasts a title to all {@link Player}s on the server.
     * The text is deserialized before being sent.
     *
     * @param title    The title to send.
     * @param subtitle The subtitle to send.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle) {
        broadcastTitle(title, subtitle, null, null, null, (TagResolver) null);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    BROADCAST STRIP FORMATTING    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a title to all {@link Player}s on the server.
     * The text is deserialized before being sent.
     *
     * @param title           The title to send.
     * @param subtitle        The subtitle to send.
     * @param fadeIn          The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay            The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut         The time in milliseconds for the title to fade out. (default: 1000)
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, TagResolver tagResolver, boolean stripFormatting) {
        if (stripFormatting) {
            broadcastTitle(stripFormatting(title, tagResolver), stripFormatting(subtitle, tagResolver), fadeIn, stay, fadeOut, tagResolver);
        } else {
            broadcastTitle(title, subtitle, fadeIn, stay, fadeOut, tagResolver);
        }
    }

    /**
     * Broadcasts a title to all {@link Player}s on the server.
     * The text is deserialized before being sent.
     *
     * @param title           The title to send.
     * @param subtitle        The subtitle to send.
     * @param fadeIn          The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay            The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut         The time in milliseconds for the title to fade out. (default: 1000)
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, boolean stripFormatting) {
        broadcastTitle(title, subtitle, fadeIn, stay, fadeOut, (TagResolver) null, stripFormatting);
    }

    /**
     * Broadcasts a title to all {@link Player}s on the server.
     * The text is deserialized before being sent.
     *
     * @param title           The title to send.
     * @param subtitle        The subtitle to send.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, TagResolver tagResolver, boolean stripFormatting) {
        broadcastTitle(title, subtitle, null, null, null, tagResolver, stripFormatting);
    }

    /**
     * Broadcasts a title to all {@link Player}s on the server.
     * The text is deserialized before being sent.
     *
     * @param title           The title to send.
     * @param subtitle        The subtitle to send.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, boolean stripFormatting) {
        broadcastTitle(title, subtitle, null, null, null, (TagResolver) null, stripFormatting);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    BROADCAST PERMISSIONS    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a title to all {@link Player}s on the server.
     * The text is deserialized before being sent.
     *
     * @param title       The title to send.
     * @param subtitle    The subtitle to send.
     * @param fadeIn      The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay        The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut     The time in milliseconds for the title to fade out. (default: 1000)
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, TagResolver tagResolver, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        broadcastTitle(title, subtitle, fadeIn, stay, fadeOut, tagResolver, !hasPermission);
    }

    /**
     * Broadcasts a title to all {@link Player}s on the server.
     * The text is deserialized before being sent.
     *
     * @param title       The title to send.
     * @param subtitle    The subtitle to send.
     * @param fadeIn      The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay        The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut     The time in milliseconds for the title to fade out. (default: 1000)
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, @NotNull CommandSource sender, @NotNull String... permissions) {
        broadcastTitle(title, subtitle, fadeIn, stay, fadeOut, (TagResolver) null, sender, permissions);
    }

    /**
     * Broadcasts a title to all {@link Player}s on the server.
     * The text is deserialized before being sent.
     *
     * @param title       The title to send.
     * @param subtitle    The subtitle to send.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, TagResolver tagResolver, @NotNull CommandSource sender, @NotNull String... permissions) {
        broadcastTitle(title, subtitle, null, null, null, tagResolver, sender, permissions);
    }

    /**
     * Broadcasts a title to all {@link Player}s on the server.
     * The text is deserialized before being sent.
     *
     * @param title       The title to send.
     * @param subtitle    The subtitle to send.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, @NotNull CommandSource sender, @NotNull String... permissions) {
        broadcastTitle(title, subtitle, null, null, null, (TagResolver) null, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    WORLD BROADCAST    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a title to all {@link Player}s in a server.
     * The text is deserialized before being sent.
     *
     * @param title       The title to send.
     * @param subtitle    The subtitle to send.
     * @param fadeIn      The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay        The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut     The time in milliseconds for the title to fade out. (default: 1000)
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param server      The {@link RegisteredServer} to send the title to.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    @SuppressWarnings("PatternValidation")
    public void broadcastTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, TagResolver tagResolver, @NotNull RegisteredServer server) {
        Preconditions.checkArgument((title == null && subtitle != null) || (title != null && subtitle == null), "Both title and subtitle cannot be null simultaneously");
        Preconditions.checkNotNull(server, "World cannot be null");

        if (title != null) server.getPlayersConnected().forEach(p -> p.sendTitlePart(TitlePart.TITLE, deserialize(title, tagResolver)));
        if (subtitle != null) server.getPlayersConnected().forEach(p -> p.sendTitlePart(TitlePart.SUBTITLE, deserialize(subtitle, tagResolver)));
        server.getPlayersConnected().forEach(p -> p.sendTitlePart(TitlePart.TIMES, Title.Times.times(
                Duration.of(fadeIn != null ? fadeIn : 1000, ChronoUnit.MILLIS),
                Duration.of(stay != null ? stay : 3000, ChronoUnit.MILLIS),
                Duration.of(fadeOut != null ? fadeOut : 1000, ChronoUnit.MILLIS)
        )));
    }

    /**
     * Broadcasts a title to all {@link Player}s in a server.
     * The text is deserialized before being sent.
     *
     * @param title    The title to send.
     * @param subtitle The subtitle to send.
     * @param fadeIn   The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay     The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut  The time in milliseconds for the title to fade out. (default: 1000)
     * @param server   The {@link RegisteredServer} to send the title to.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, @NotNull RegisteredServer server) {
        broadcastTitle(title, subtitle, fadeIn, stay, fadeOut, null, server);
    }

    /**
     * Broadcasts a title to all {@link Player}s in a server.
     * The text is deserialized before being sent.
     *
     * @param title       The title to send.
     * @param subtitle    The subtitle to send.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param server      The {@link RegisteredServer} to send the title to.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, TagResolver tagResolver, @NotNull RegisteredServer server) {
        broadcastTitle(title, subtitle, null, null, null, tagResolver, server);
    }

    /**
     * Broadcasts a title to all {@link Player}s in a server.
     * The text is deserialized before being sent.
     *
     * @param title    The title to send.
     * @param subtitle The subtitle to send.
     * @param server   The {@link RegisteredServer} to send the title to.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, @NotNull RegisteredServer server) {
        broadcastTitle(title, subtitle, null, null, null, null, server);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    WORLD BROADCAST STRIP FORMATTING    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a title to all {@link Player}s in a server.
     * The text is deserialized before being sent.
     *
     * @param title           The title to send.
     * @param subtitle        The subtitle to send.
     * @param fadeIn          The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay            The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut         The time in milliseconds for the title to fade out. (default: 1000)
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param server          The {@link RegisteredServer} to send the title to.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, TagResolver tagResolver, @NotNull RegisteredServer server, boolean stripFormatting) {
        if (stripFormatting) {
            broadcastTitle(stripFormatting(title, tagResolver), stripFormatting(subtitle, tagResolver), fadeIn, stay, fadeOut, tagResolver, server);
        } else {
            broadcastTitle(title, subtitle, fadeIn, stay, fadeOut, tagResolver, server);
        }
    }

    /**
     * Broadcasts a title to all {@link Player}s in a server.
     * The text is deserialized before being sent.
     *
     * @param title           The title to send.
     * @param subtitle        The subtitle to send.
     * @param fadeIn          The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay            The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut         The time in milliseconds for the title to fade out. (default: 1000)
     * @param server          The {@link RegisteredServer} to send the title to.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, @NotNull RegisteredServer server, boolean stripFormatting) {
        broadcastTitle(title, subtitle, fadeIn, stay, fadeOut, null, server, stripFormatting);
    }

    /**
     * Broadcasts a title to all {@link Player}s in a server.
     * The text is deserialized before being sent.
     *
     * @param title           The title to send.
     * @param subtitle        The subtitle to send.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param server          The {@link RegisteredServer} to send the title to.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, TagResolver tagResolver, @NotNull RegisteredServer server, boolean stripFormatting) {
        broadcastTitle(title, subtitle, null, null, null, tagResolver, server, stripFormatting);
    }

    /**
     * Broadcasts a title to all {@link Player}s in a server.
     * The text is deserialized before being sent.
     *
     * @param title           The title to send.
     * @param subtitle        The subtitle to send.
     * @param server          The {@link RegisteredServer} to send the title to.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, @NotNull RegisteredServer server, boolean stripFormatting) {
        broadcastTitle(title, subtitle, null, null, null, null, server, stripFormatting);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    WORLD BROADCAST PERMISSIONS    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a title to all {@link Player}s in a server.
     * The text is deserialized before being sent.
     *
     * @param title       The title to send.
     * @param subtitle    The subtitle to send.
     * @param fadeIn      The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay        The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut     The time in milliseconds for the title to fade out. (default: 1000)
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param server      The {@link RegisteredServer} to send the title to.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, TagResolver tagResolver, @NotNull RegisteredServer server, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        broadcastTitle(title, subtitle, fadeIn, stay, fadeOut, tagResolver, server, !hasPermission);
    }

    /**
     * Broadcasts a title to all {@link Player}s in a server.
     * The text is deserialized before being sent.
     *
     * @param title       The title to send.
     * @param subtitle    The subtitle to send.
     * @param fadeIn      The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay        The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut     The time in milliseconds for the title to fade out. (default: 1000)
     * @param server      The {@link RegisteredServer} to send the title to.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, @NotNull RegisteredServer server, @NotNull CommandSource sender, @NotNull String... permissions) {
        broadcastTitle(title, subtitle, fadeIn, stay, fadeOut, null, server, sender, permissions);
    }

    /**
     * Broadcasts a title to all {@link Player}s in a server.
     * The text is deserialized before being sent.
     *
     * @param title       The title to send.
     * @param subtitle    The subtitle to send.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param server      The {@link RegisteredServer} to send the title to.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, TagResolver tagResolver, @NotNull RegisteredServer server, @NotNull CommandSource sender, @NotNull String... permissions) {
        broadcastTitle(title, subtitle, null, null, null, tagResolver, server, sender, permissions);
    }

    /**
     * Broadcasts a title to all {@link Player}s in a server.
     * The text is deserialized before being sent.
     *
     * @param title       The title to send.
     * @param subtitle    The subtitle to send.
     * @param server      The {@link RegisteredServer} to send the title to.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, @NotNull RegisteredServer server, @NotNull CommandSource sender, @NotNull String... permissions) {
        broadcastTitle(title, subtitle, null, null, null, null, server, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    PERMISSION BROADCAST    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a title to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param title            The title to send.
     * @param subtitle         The subtitle to send.
     * @param fadeIn           The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay             The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut          The time in milliseconds for the title to fade out. (default: 1000)
     * @param tagResolver      The {@link TagResolver} for any additional tags to handle.
     * @param neededPermission The permission needed to receive the broadcast.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, TagResolver tagResolver, @NotNull String neededPermission) {
        Preconditions.checkArgument((title == null && subtitle != null) || (title != null && subtitle == null), "Both title and subtitle cannot be null simultaneously");
        Preconditions.checkNotNull(neededPermission, "Needed permission cannot be null");

        if (title != null) server.getAllPlayers().forEach(p -> p.sendTitlePart(TitlePart.TITLE, deserialize(title, tagResolver)));
        if (subtitle != null) server.getAllPlayers().forEach(p -> p.sendTitlePart(TitlePart.SUBTITLE, deserialize(subtitle, tagResolver)));
        server.getAllPlayers().forEach(p -> p.sendTitlePart(TitlePart.TIMES, Title.Times.times(
                Duration.of(fadeIn != null ? fadeIn : 1000, ChronoUnit.MILLIS),
                Duration.of(stay != null ? stay : 3000, ChronoUnit.MILLIS),
                Duration.of(fadeOut != null ? fadeOut : 1000, ChronoUnit.MILLIS)
        )));
    }

    /**
     * Broadcasts a title to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param title            The title to send.
     * @param subtitle         The subtitle to send.
     * @param fadeIn           The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay             The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut          The time in milliseconds for the title to fade out. (default: 1000)
     * @param neededPermission The permission needed to receive the broadcast.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, @NotNull String neededPermission) {
        broadcastTitle(title, subtitle, fadeIn, stay, fadeOut, null, neededPermission);
    }

    /**
     * Broadcasts a title to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param title            The title to send.
     * @param subtitle         The subtitle to send.
     * @param tagResolver      The {@link TagResolver} for any additional tags to handle.
     * @param neededPermission The permission needed to receive the broadcast.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, TagResolver tagResolver, @NotNull String neededPermission) {
        broadcastTitle(title, subtitle, null, null, null, tagResolver, neededPermission);
    }

    /**
     * Broadcasts a title to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param title            The title to send.
     * @param subtitle         The subtitle to send.
     * @param neededPermission The permission needed to receive the broadcast.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, @NotNull String neededPermission) {
        broadcastTitle(title, subtitle, null, null, null, null, neededPermission);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    PERMISSION BROADCAST STRIP FORMATTING    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a title to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param title            The title to send.
     * @param subtitle         The subtitle to send.
     * @param fadeIn           The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay             The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut          The time in milliseconds for the title to fade out. (default: 1000)
     * @param tagResolver      The {@link TagResolver} for any additional tags to handle.
     * @param neededPermission The permission needed to receive the broadcast.
     * @param stripFormatting  Whether to strip formatting from the text before sending it.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, TagResolver tagResolver, @NotNull String neededPermission, boolean stripFormatting) {
        if (stripFormatting) {
            broadcastTitle(stripFormatting(title, tagResolver), stripFormatting(subtitle, tagResolver), fadeIn, stay, fadeOut, tagResolver, neededPermission);
        } else {
            broadcastTitle(title, subtitle, fadeIn, stay, fadeOut, tagResolver, neededPermission);
        }
    }

    /**
     * Broadcasts a title to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param title            The title to send.
     * @param subtitle         The subtitle to send.
     * @param fadeIn           The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay             The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut          The time in milliseconds for the title to fade out. (default: 1000)
     * @param neededPermission The permission needed to receive the broadcast.
     * @param stripFormatting  Whether to strip formatting from the text before sending it.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, @NotNull String neededPermission, boolean stripFormatting) {
        broadcastTitle(title, subtitle, fadeIn, stay, fadeOut, null, neededPermission, stripFormatting);
    }

    /**
     * Broadcasts a title to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param title            The title to send.
     * @param subtitle         The subtitle to send.
     * @param tagResolver      The {@link TagResolver} for any additional tags to handle.
     * @param neededPermission The permission needed to receive the broadcast.
     * @param stripFormatting  Whether to strip formatting from the text before sending it.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, TagResolver tagResolver, @NotNull String neededPermission, boolean stripFormatting) {
        broadcastTitle(title, subtitle, null, null, null, tagResolver, neededPermission, stripFormatting);
    }

    /**
     * Broadcasts a title to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param title            The title to send.
     * @param subtitle         The subtitle to send.
     * @param neededPermission The permission needed to receive the broadcast.
     * @param stripFormatting  Whether to strip formatting from the text before sending it.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, @NotNull String neededPermission, boolean stripFormatting) {
        broadcastTitle(title, subtitle, null, null, null, null, neededPermission, stripFormatting);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    PERMISSION BROADCAST PERMISSIONS    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a title to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param title            The title to send.
     * @param subtitle         The subtitle to send.
     * @param fadeIn           The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay             The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut          The time in milliseconds for the title to fade out. (default: 1000)
     * @param tagResolver      The {@link TagResolver} for any additional tags to handle.
     * @param neededPermission The permission needed to receive the broadcast.
     * @param sender           The sender of the text.
     * @param permissions      The permissions to check.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, TagResolver tagResolver, @NotNull String neededPermission, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        broadcastTitle(title, subtitle, fadeIn, stay, fadeOut, tagResolver, neededPermission, !hasPermission);
    }

    /**
     * Broadcasts a title to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param title            The title to send.
     * @param subtitle         The subtitle to send.
     * @param fadeIn           The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay             The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut          The time in milliseconds for the title to fade out. (default: 1000)
     * @param neededPermission The permission needed to receive the broadcast.
     * @param sender           The sender of the text.
     * @param permissions      The permissions to check.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, @NotNull String neededPermission, @NotNull CommandSource sender, @NotNull String... permissions) {
        broadcastTitle(title, subtitle, fadeIn, stay, fadeOut, null, neededPermission, sender, permissions);
    }

    /**
     * Broadcasts a title to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param title            The title to send.
     * @param subtitle         The subtitle to send.
     * @param tagResolver      The {@link TagResolver} for any additional tags to handle.
     * @param neededPermission The permission needed to receive the broadcast.
     * @param sender           The sender of the text.
     * @param permissions      The permissions to check.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, TagResolver tagResolver, @NotNull String neededPermission, @NotNull CommandSource sender, @NotNull String... permissions) {
        broadcastTitle(title, subtitle, null, null, null, tagResolver, neededPermission, sender, permissions);
    }

    /**
     * Broadcasts a title to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param title            The title to send.
     * @param subtitle         The subtitle to send.
     * @param neededPermission The permission needed to receive the broadcast.
     * @param sender           The sender of the text.
     * @param permissions      The permissions to check.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, @NotNull String neededPermission, @NotNull CommandSource sender, @NotNull String... permissions) {
        broadcastTitle(title, subtitle, null, null, null, null, neededPermission, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    PERMISSIONS (ARRAY) BROADCAST    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a title to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param title             The title to send.
     * @param subtitle          The subtitle to send.
     * @param fadeIn            The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay              The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut           The time in milliseconds for the title to fade out. (default: 1000)
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, TagResolver tagResolver, @NotNull String[] neededPermissions) {
        Preconditions.checkArgument((title == null && subtitle != null) || (title != null && subtitle == null), "Both title and subtitle cannot be null simultaneously");
        Preconditions.checkNotNull(neededPermissions, "Needed permission cannot be null");

        for (String permission : neededPermissions) {
            if (permission == null) continue;

            if (title != null) server.getAllPlayers().stream().filter(p -> p.hasPermission(permission)).forEach(p -> p.sendTitlePart(TitlePart.TITLE, deserialize(title, tagResolver)));
            if (subtitle != null) server.getAllPlayers().stream().filter(p -> p.hasPermission(permission)).forEach(p -> p.sendTitlePart(TitlePart.SUBTITLE, deserialize(subtitle, tagResolver)));
            server.getAllPlayers().stream().filter(p -> p.hasPermission(permission)).forEach(p -> p.sendTitlePart(TitlePart.TIMES, Title.Times.times(
                    Duration.of(fadeIn != null ? fadeIn : 1000, ChronoUnit.MILLIS),
                    Duration.of(stay != null ? stay : 3000, ChronoUnit.MILLIS),
                    Duration.of(fadeOut != null ? fadeOut : 1000, ChronoUnit.MILLIS)
            )));
        }
    }

    /**
     * Broadcasts a title to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param title             The title to send.
     * @param subtitle          The subtitle to send.
     * @param fadeIn            The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay              The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut           The time in milliseconds for the title to fade out. (default: 1000)
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, @NotNull String[] neededPermissions) {
        broadcastTitle(title, subtitle, fadeIn, stay, fadeOut, (TagResolver) null, neededPermissions);
    }

    /**
     * Broadcasts a title to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param title             The title to send.
     * @param subtitle          The subtitle to send.
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, TagResolver tagResolver, @NotNull String[] neededPermissions) {
        broadcastTitle(title, subtitle, null, null, null, tagResolver, neededPermissions);
    }

    /**
     * Broadcasts a title to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param title             The title to send.
     * @param subtitle          The subtitle to send.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, @NotNull String[] neededPermissions) {
        broadcastTitle(title, subtitle, null, null, null, (TagResolver) null, neededPermissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    PERMISSIONS (ARRAY) BROADCAST STRIP FORMATTING    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a title to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param title             The title to send.
     * @param subtitle          The subtitle to send.
     * @param fadeIn            The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay              The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut           The time in milliseconds for the title to fade out. (default: 1000)
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param stripFormatting   Whether to strip formatting from the text before sending it.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, TagResolver tagResolver, @NotNull String[] neededPermissions, boolean stripFormatting) {
        if (stripFormatting) {
            broadcastTitle(stripFormatting(title, tagResolver), stripFormatting(subtitle, tagResolver), fadeIn, stay, fadeOut, tagResolver, neededPermissions);
        } else {
            broadcastTitle(title, subtitle, fadeIn, stay, fadeOut, tagResolver, neededPermissions);
        }
    }

    /**
     * Broadcasts a title to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param title             The title to send.
     * @param subtitle          The subtitle to send.
     * @param fadeIn            The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay              The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut           The time in milliseconds for the title to fade out. (default: 1000)
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param stripFormatting   Whether to strip formatting from the text before sending it.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, @NotNull String[] neededPermissions, boolean stripFormatting) {
        broadcastTitle(title, subtitle, fadeIn, stay, fadeOut, null, neededPermissions, stripFormatting);
    }

    /**
     * Broadcasts a title to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param title             The title to send.
     * @param subtitle          The subtitle to send.
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param stripFormatting   Whether to strip formatting from the text before sending it.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, TagResolver tagResolver, @NotNull String[] neededPermissions, boolean stripFormatting) {
        broadcastTitle(title, subtitle, null, null, null, tagResolver, neededPermissions, stripFormatting);
    }

    /**
     * Broadcasts a title to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param title             The title to send.
     * @param subtitle          The subtitle to send.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param stripFormatting   Whether to strip formatting from the text before sending it.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, @NotNull String[] neededPermissions, boolean stripFormatting) {
        broadcastTitle(title, subtitle, null, null, null, null, neededPermissions, stripFormatting);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    PERMISSIONS (ARRAY) BROADCAST PERMISSION    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a title to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param title             The title to send.
     * @param subtitle          The subtitle to send.
     * @param fadeIn            The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay              The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut           The time in milliseconds for the title to fade out. (default: 1000)
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param sender            The sender of the text.
     * @param permissions       The permissions to check.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, TagResolver tagResolver, @NotNull String[] neededPermissions, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        broadcastTitle(title, subtitle, fadeIn, stay, fadeOut, tagResolver, neededPermissions, !hasPermission);
    }

    /**
     * Broadcasts a title to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param title             The title to send.
     * @param subtitle          The subtitle to send.
     * @param fadeIn            The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay              The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut           The time in milliseconds for the title to fade out. (default: 1000)
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param sender            The sender of the text.
     * @param permissions       The permissions to check.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, @NotNull String[] neededPermissions, @NotNull CommandSource sender, @NotNull String... permissions) {
        broadcastTitle(title, subtitle, fadeIn, stay, fadeOut, null, neededPermissions, sender, permissions);
    }

    /**
     * Broadcasts a title to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param title             The title to send.
     * @param subtitle          The subtitle to send.
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param sender            The sender of the text.
     * @param permissions       The permissions to check.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, TagResolver tagResolver, @NotNull String[] neededPermissions, @NotNull CommandSource sender, @NotNull String... permissions) {
        broadcastTitle(title, subtitle, null, null, null, tagResolver, neededPermissions, sender, permissions);
    }

    /**
     * Broadcasts a title to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param title             The title to send.
     * @param subtitle          The subtitle to send.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param sender            The sender of the text.
     * @param permissions       The permissions to check.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public void broadcastTitle(String title, String subtitle, @NotNull String[] neededPermissions, @NotNull CommandSource sender, @NotNull String... permissions) {
        broadcastTitle(title, subtitle, null, null, null, null, neededPermissions, sender, permissions);
    }
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    PERMISSIONS (COLLECTION) BROADCAST    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a title to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param title             The title to send.
     * @param subtitle          The subtitle to send.
     * @param fadeIn            The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay              The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut           The time in milliseconds for the title to fade out. (default: 1000)
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public <P extends Collection<String>> void broadcastTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, TagResolver tagResolver, @NotNull P neededPermissions) {
        Preconditions.checkArgument((title == null && subtitle != null) || (title != null && subtitle == null), "Both title and subtitle cannot be null simultaneously");
        Preconditions.checkNotNull(neededPermissions, "Needed permission cannot be null");

        for (String permission : neededPermissions) {
            if (permission == null) continue;
            if (title != null) server.getAllPlayers().stream().filter(p -> p.hasPermission(permission)).forEach(p -> p.sendTitlePart(TitlePart.TITLE, deserialize(title, tagResolver)));
            if (subtitle != null) server.getAllPlayers().stream().filter(p -> p.hasPermission(permission)).forEach(p -> p.sendTitlePart(TitlePart.SUBTITLE, deserialize(subtitle, tagResolver)));
            server.getAllPlayers().stream().filter(p -> p.hasPermission(permission)).forEach(p -> p.sendTitlePart(TitlePart.TIMES, Title.Times.times(
                    Duration.of(fadeIn != null ? fadeIn : 1000, ChronoUnit.MILLIS),
                    Duration.of(stay != null ? stay : 3000, ChronoUnit.MILLIS),
                    Duration.of(fadeOut != null ? fadeOut : 1000, ChronoUnit.MILLIS)
            )));
        }
    }

    /**
     * Broadcasts a title to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param title             The title to send.
     * @param subtitle          The subtitle to send.
     * @param fadeIn            The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay              The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut           The time in milliseconds for the title to fade out. (default: 1000)
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public <P extends Collection<String>> void broadcastTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, @NotNull P neededPermissions) {
        broadcastTitle(title, subtitle, fadeIn, stay, fadeOut, (TagResolver) null, neededPermissions);
    }

    /**
     * Broadcasts a title to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param title             The title to send.
     * @param subtitle          The subtitle to send.
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public <P extends Collection<String>> void broadcastTitle(String title, String subtitle, TagResolver tagResolver, @NotNull P neededPermissions) {
        broadcastTitle(title, subtitle, null, null, null, tagResolver, neededPermissions);
    }

    /**
     * Broadcasts a title to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param title             The title to send.
     * @param subtitle          The subtitle to send.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public <P extends Collection<String>> void broadcastTitle(String title, String subtitle, @NotNull P neededPermissions) {
        broadcastTitle(title, subtitle, null, null, null, (TagResolver) null, neededPermissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    PERMISSIONS (COLLECTION) BROADCAST STRIP FORMATTING    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a title to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param title             The title to send.
     * @param subtitle          The subtitle to send.
     * @param fadeIn            The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay              The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut           The time in milliseconds for the title to fade out. (default: 1000)
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param stripFormatting   Whether to strip formatting from the text before sending it.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public <P extends Collection<String>> void broadcastTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, TagResolver tagResolver, @NotNull P neededPermissions, boolean stripFormatting) {
        if (stripFormatting) {
            broadcastTitle(stripFormatting(title, tagResolver), stripFormatting(subtitle, tagResolver), fadeIn, stay, fadeOut, tagResolver, neededPermissions);
        } else {
            broadcastTitle(title, subtitle, fadeIn, stay, fadeOut, tagResolver, neededPermissions);
        }
    }

    /**
     * Broadcasts a title to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param title             The title to send.
     * @param subtitle          The subtitle to send.
     * @param fadeIn            The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay              The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut           The time in milliseconds for the title to fade out. (default: 1000)
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param stripFormatting   Whether to strip formatting from the text before sending it.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public <P extends Collection<String>> void broadcastTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, @NotNull P neededPermissions, boolean stripFormatting) {
        broadcastTitle(title, subtitle, fadeIn, stay, fadeOut, null, neededPermissions, stripFormatting);
    }

    /**
     * Broadcasts a title to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param title             The title to send.
     * @param subtitle          The subtitle to send.
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param stripFormatting   Whether to strip formatting from the text before sending it.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public <P extends Collection<String>> void broadcastTitle(String title, String subtitle, TagResolver tagResolver, @NotNull P neededPermissions, boolean stripFormatting) {
        broadcastTitle(title, subtitle, null, null, null, tagResolver, neededPermissions, stripFormatting);
    }

    /**
     * Broadcasts a title to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param title             The title to send.
     * @param subtitle          The subtitle to send.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param stripFormatting   Whether to strip formatting from the text before sending it.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public <P extends Collection<String>> void broadcastTitle(String title, String subtitle, @NotNull P neededPermissions, boolean stripFormatting) {
        broadcastTitle(title, subtitle, null, null, null, null, neededPermissions, stripFormatting);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    PERMISSIONS (COLLECTION) BROADCAST PERMISSION    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a title to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param title             The title to send.
     * @param subtitle          The subtitle to send.
     * @param fadeIn            The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay              The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut           The time in milliseconds for the title to fade out. (default: 1000)
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param sender            The sender of the text.
     * @param permissions       The permissions to check.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public <P extends Collection<String>> void broadcastTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, TagResolver tagResolver, @NotNull P neededPermissions, @NotNull CommandSource sender, @NotNull String... permissions) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(permissions, "Permissions cannot be null");

        boolean hasPermission = false;
        for (String permission : permissions) {
            if (permission == null) continue;
            if (sender.hasPermission(permission)) {
                hasPermission = true;
                break;
            }
        }

        broadcastTitle(title, subtitle, fadeIn, stay, fadeOut, tagResolver, neededPermissions, !hasPermission);
    }

    /**
     * Broadcasts a title to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param title             The title to send.
     * @param subtitle          The subtitle to send.
     * @param fadeIn            The time in milliseconds for the title to fade in. (default: 1000)
     * @param stay              The time in milliseconds for the title to stay on screen. (default: 3000)
     * @param fadeOut           The time in milliseconds for the title to fade out. (default: 1000)
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param sender            The sender of the text.
     * @param permissions       The permissions to check.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public <P extends Collection<String>> void broadcastTitle(String title, String subtitle, Long fadeIn, Long stay, Long fadeOut, @NotNull P neededPermissions, @NotNull CommandSource sender, @NotNull String... permissions) {
        broadcastTitle(title, subtitle, fadeIn, stay, fadeOut, null, neededPermissions, sender, permissions);
    }

    /**
     * Broadcasts a title to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param title             The title to send.
     * @param subtitle          The subtitle to send.
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param sender            The sender of the text.
     * @param permissions       The permissions to check.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public <P extends Collection<String>> void broadcastTitle(String title, String subtitle, TagResolver tagResolver, @NotNull P neededPermissions, @NotNull CommandSource sender, @NotNull String... permissions) {
        broadcastTitle(title, subtitle, null, null, null, tagResolver, neededPermissions, sender, permissions);
    }

    /**
     * Broadcasts a title to all {@link Player}s with some permissions.
     * The text is deserialized before being sent.
     *
     * @param title             The title to send.
     * @param subtitle          The subtitle to send.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param sender            The sender of the text.
     * @param permissions       The permissions to check.
     * @throws NullPointerException     if the receiver is null.
     * @throws IllegalArgumentException if both title and subtitle are null.
     */
    public <P extends Collection<String>> void broadcastTitle(String title, String subtitle, @NotNull P neededPermissions, @NotNull CommandSource sender, @NotNull String... permissions) {
        broadcastTitle(title, subtitle, null, null, null, null, neededPermissions, sender, permissions);
    }
}
