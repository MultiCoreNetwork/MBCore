package it.multicoredev.mbcore.spigot;

import com.google.common.base.Preconditions;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
public class Text {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer legacySerializer = LegacyComponentSerializer.legacyAmpersand();
    private static Text instance;
    private final BukkitAudiences audiences;

    private Text(Plugin plugin) {
        audiences = BukkitAudiences.create(plugin);
    }

    /**
     * Retrieves the existing instance of the Text class, if it has been created.
     *
     * @return The existing Text instance, or null if no instance has been created yet.
     */
    @Nullable
    public static Text get() {
        return instance;
    }

    /**
     * Creates an instance of the Text class with the provided Plugin.
     * WARNING! Remember to destroy the instance of this class when the plugin is disabled or reloaded with {@link Text#destroy()}.
     *
     * @param plugin The {@link Plugin} to associate with the Text instance.
     * @return The created Text instance.
     * @throws IllegalStateException if a Text instance has already been created. To create a new instance,
     *                               you must destroy the previous instance first.
     */
    public static Text create(Plugin plugin) {
        if (instance == null) throw new IllegalStateException("Text instance already created. Destroy the previous instance first.");
        instance = new Text(plugin);
        return instance;
    }

    /**
     * Destroys the existing instance of the Text class, if it has been created.
     * This method releases any associated resources and sets the instance to null, making it eligible for garbage collection.
     *
     * @throws IllegalStateException if no Text instance has been created to destroy.
     */
    public static void destroy() {
        if (instance == null) throw new IllegalStateException("Text instance not created.");

        if (instance.audiences != null) {
            instance.audiences.close();
        }

        instance = null;
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

        Iterator<String> iterator = collection.iterator();
        while (iterator.hasNext()) {
            String text = iterator.next();
            iterator.remove();
            collection.add(stripFormatting(text, tagResolver));
        }

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
     * Serialize a text to a legacy ampersand string.
     *
     * @param text        The text to serialize.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @return The serialized text.
     * null if the input text is null.
     */
    public static String toLegacyText(String text, TagResolver tagResolver) {
        if (text == null) return null;

        return legacySerializer.serialize(miniMessage.deserialize(text, tagResolver));
    }

    /**
     * Serialize a text to a legacy ampersand string.
     *
     * @param text The text to serialize.
     * @return The serialized text.
     * null if the input text is null.
     */
    public static String toLegacyText(String text) {
        return toLegacyText(text, null);
    }

    /**
     * Serialize texts to a legacy ampersand string.
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
     * Serialize texts to a legacy ampersand string.
     *
     * @param texts The texts to serialize.
     * @return An array of serialized texts or an empty array if no texts are provided.
     * null if the input texts array is null.
     */
    public static String[] toLegacyText(String[] texts) {
        return toLegacyText(texts, null);
    }

    /**
     * Serialize texts to a legacy ampersand string.
     *
     * @param collection  The collection of texts to serialize.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param <C>         The type of the collection.
     * @return A collection of serialized texts or an empty iterable if no texts are provided.
     * null if the input iterable is null.
     */
    public static <C extends Collection<String>> C toLegacyText(C collection, TagResolver tagResolver) {
        if (collection == null) return null;

        Iterator<String> iterator = collection.iterator();
        while (iterator.hasNext()) {
            String text = iterator.next();
            iterator.remove();
            collection.add(toLegacyText(text, tagResolver));
        }

        return collection;
    }

    /**
     * Serialize texts to a legacy ampersand string.
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
     * Deserialize a MiniMessage text.
     *
     * @param text        The text to deserialize.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @return The deserialized text.
     * null if the input text is null.
     */
    public static Component deserialize(String text, TagResolver tagResolver) {
        if (text == null) return null;
        return miniMessage.deserialize(text, tagResolver);
    }

    /**
     * Deserialize a MiniMessage text.
     *
     * @param text The text to deserialize.
     * @return The deserialized text.
     * null if the input text is null.
     */
    public static Component deserialize(String text) {
        return deserialize(text, null);
    }

    /**
     * Deserialize MiniMessage texts.
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
     * Deserialize MiniMessage texts.
     *
     * @param texts The texts to deserialize.
     * @return An array of deserialized texts or an empty array if no texts are provided.
     * null if the input texts array is null.
     */
    public static Component[] deserialize(String[] texts) {
        return deserialize(texts, null);
    }

    /**
     * Deserialize MiniMessage texts.
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
     * Deserialize MiniMessage texts.
     *
     * @param collection The collection of texts to deserialize.
     * @param <C>        The type of the collection.
     * @return A collection of deserialized texts or an empty iterable if no texts are provided.
     * null if the input iterable is null.
     */
    public static <C extends Collection<String>> List<Component> deserialize(C collection) {
        return deserialize(collection, null);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    SEND    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */


    /**
     * Sends a text to a {@link CommandSender} or a {@link Player}.
     * The text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param receiver    The receiver of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException if the text or the receiver is null.
     */
    public void send(@NotNull String text, @NotNull CommandSender receiver, TagResolver tagResolver) {
        Preconditions.checkNotNull(text, "Text cannot be null");
        Preconditions.checkNotNull(receiver, "Receiver cannot be null");

        Audience audience;
        if (receiver instanceof Player) audience = audiences.player((Player) receiver);
        else audience = audiences.sender(receiver);

        audience.sendMessage(deserialize(text, tagResolver));
    }

    /**
     * Sends a text to a {@link CommandSender} or a {@link Player}.
     * The text is deserialized before being sent.
     *
     * @param text     The text to send.
     * @param receiver The receiver of the text.
     * @throws NullPointerException if the text or the receiver is null.
     */
    public void send(@NotNull String text, @NotNull CommandSender receiver) {
        send(text, receiver, null);
    }

    /**
     * Sends a text to a {@link CommandSender} or a {@link Player}.
     *
     * @param text            The text to send.
     * @param receiver        The receiver of the text.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text or the receiver is null.
     */
    public void send(@NotNull String text, @NotNull CommandSender receiver, TagResolver tagResolver, boolean stripFormatting) {
        if (stripFormatting) {
            send(stripFormatting(text, tagResolver), receiver, tagResolver);
        } else {
            send(text, receiver, tagResolver);
        }
    }

    /**
     * Sends a text to a {@link CommandSender} or a {@link Player}.
     *
     * @param text            The text to send.
     * @param receiver        The receiver of the text.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text or the receiver is null.
     */
    public void send(@NotNull String text, @NotNull CommandSender receiver, boolean stripFormatting) {
        send(text, receiver, null, stripFormatting);
    }

    /**
     * Sends a text to a {@link CommandSender} or a {@link Player}.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param receiver    The receiver of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the receiver, the sender or the permissions is null.
     */
    public void send(@NotNull String text, @NotNull CommandSender receiver, TagResolver tagResolver, @NotNull CommandSender sender, @NotNull String... permissions) {
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
     * Sends a text to a {@link CommandSender} or a {@link Player}.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param receiver    The receiver of the text.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the receiver or the sender or the permissions is null.
     */
    public void send(@NotNull String text, @NotNull CommandSender receiver, @NotNull CommandSender sender, @NotNull String... permissions) {
        send(text, receiver, null, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    SEND TO MULTIPLE RECEIVERS (ARRAY)    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Sends a text to a group of {@link CommandSender} or {@link Player}.
     * The text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param receivers   The receivers of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException if the text or the receivers is null.
     */
    public void send(@NotNull String text, @NotNull CommandSender[] receivers, TagResolver tagResolver) {
        Preconditions.checkNotNull(receivers, "Receivers cannot be null");

        for (CommandSender receiver : receivers) {
            if (receiver == null) continue;
            send(text, receiver, tagResolver);
        }
    }

    /**
     * Sends a text to a group of {@link CommandSender} or {@link Player}.
     * The text is deserialized before being sent.
     *
     * @param text      The text to send.
     * @param receivers The receivers of the text.
     * @throws NullPointerException if the text or the receivers is null.
     */
    public void send(@NotNull String text, @NotNull CommandSender[] receivers) {
        send(text, receivers, null);
    }

    /**
     * Sends a text to a group of {@link CommandSender} or {@link Player}.
     *
     * @param text            The text to send.
     * @param receivers       The receivers of the text.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text or the receivers is null.
     */
    public void send(@NotNull String text, @NotNull CommandSender[] receivers, TagResolver tagResolver, boolean stripFormatting) {
        if (stripFormatting) {
            send(stripFormatting(text, tagResolver), receivers, tagResolver);
        } else {
            send(text, receivers, tagResolver);
        }
    }

    /**
     * Sends a text to a group of {@link CommandSender} or {@link Player}.
     *
     * @param text            The text to send.
     * @param receivers       The receivers of the text.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text or the receivers is null.
     */
    public void send(@NotNull String text, @NotNull CommandSender[] receivers, boolean stripFormatting) {
        send(text, receivers, null, stripFormatting);
    }

    /**
     * Sends a text to a group of {@link CommandSender} or {@link Player}.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param receivers   The receivers of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the receivers or the sender or the permissions is null.
     */
    public void send(@NotNull String text, @NotNull CommandSender[] receivers, TagResolver tagResolver, @NotNull CommandSender sender, @NotNull String... permissions) {
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
     * Sends a text to a group of {@link CommandSender} or {@link Player}.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param receivers   The receivers of the text.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the receivers, the sender or the permissions is null.
     */
    public void send(@NotNull String text, @NotNull CommandSender[] receivers, @NotNull CommandSender sender, @NotNull String... permissions) {
        send(text, receivers, null, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    SEND TO MULTIPLE RECEIVERS (COLLECTION)    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Sends a text to a group of {@link CommandSender} or {@link Player}.
     * The text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param receivers   The receivers of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException if the text or the receivers is null.
     */
    public <R extends Collection<? extends CommandSender>> void send(@NotNull String text, @NotNull R receivers, TagResolver tagResolver) {
        Preconditions.checkNotNull(receivers, "Receivers cannot be null");

        for (CommandSender receiver : receivers) {
            if (receiver == null) continue;
            send(text, receiver, tagResolver);
        }
    }

    /**
     * Sends a text to a group of {@link CommandSender} or {@link Player}.
     * The text is deserialized before being sent.
     *
     * @param text      The text to send.
     * @param receivers The receivers of the text.
     * @throws NullPointerException if the text or the receivers is null.
     */
    public <R extends Collection<? extends CommandSender>> void send(@NotNull String text, @NotNull R receivers) {
        send(text, receivers, null);
    }

    /**
     * Sends a text to a group of {@link CommandSender} or {@link Player}.
     *
     * @param text            The text to send.
     * @param receivers       The receivers of the text.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text or the receivers is null.
     */
    public <R extends Collection<? extends CommandSender>> void send(@NotNull String text, @NotNull R receivers, TagResolver tagResolver, boolean stripFormatting) {
        if (stripFormatting) {
            send(stripFormatting(text, tagResolver), receivers, tagResolver);
        } else {
            send(text, receivers, tagResolver);
        }
    }

    /**
     * Sends a text to a group of {@link CommandSender} or {@link Player}.
     *
     * @param text            The text to send.
     * @param receivers       The receivers of the text.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text or the receivers is null.
     */
    public <R extends Collection<? extends CommandSender>> void send(@NotNull String text, @NotNull R receivers, boolean stripFormatting) {
        send(text, receivers, null, stripFormatting);
    }

    /**
     * Sends a text to a group of {@link CommandSender} or {@link Player}.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param receivers   The receivers of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the receivers, the sender or the permissions is null.
     */
    public <R extends Collection<? extends CommandSender>> void send(@NotNull String text, @NotNull R receivers, TagResolver tagResolver, @NotNull CommandSender sender, @NotNull String... permissions) {
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
     * Sends a text to a group of {@link CommandSender} or {@link Player}.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param receivers   The receivers of the text.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the receivers, the sender or the permissions is null.
     */
    public <R extends Collection<? extends CommandSender>> void send(@NotNull String text, @NotNull R receivers, @NotNull CommandSender sender, @NotNull String... permissions) {
        send(text, receivers, null, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    SEND MULTIPLE TEXTS (ARRAY)    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Sends a list texts to a {@link CommandSender} or a {@link Player}.
     * The texts are deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param receiver    The receiver of the texts.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException if the texts or the receiver is null.
     */
    public void send(@NotNull String[] texts, @NotNull CommandSender receiver, TagResolver tagResolver) {
        Preconditions.checkNotNull(texts, "Texts cannot be null");
        Preconditions.checkNotNull(receiver, "Receiver cannot be null");

        if (texts.length == 0) return;

        Audience audience;
        if (receiver instanceof Player) audience = audiences.player((Player) receiver);
        else audience = audiences.sender(receiver);

        for (String text : texts) {
            if (text == null) continue;
            audience.sendMessage(deserialize(text, tagResolver));
        }
    }

    /**
     * Sends a list texts to a {@link CommandSender} or a {@link Player}.
     * The texts are deserialized before being sent.
     *
     * @param texts    The texts to send.
     * @param receiver The receiver of the text.
     * @throws NullPointerException if the texts or the receiver is null.
     */
    public void send(@NotNull String[] texts, @NotNull CommandSender receiver) {
        send(texts, receiver, null);
    }

    /**
     * Sends a list texts to a {@link CommandSender} or a {@link Player}.
     *
     * @param texts           The texts to send.
     * @param receiver        The receiver of the text.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param stripFormatting Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts or the receiver is null.
     */
    public void send(@NotNull String[] texts, @NotNull CommandSender receiver, TagResolver tagResolver, boolean stripFormatting) {
        if (stripFormatting) {
            send(stripFormatting(texts, tagResolver), receiver, tagResolver);
        } else {
            send(texts, receiver, tagResolver);
        }
    }

    /**
     * Sends a list texts to a {@link CommandSender} or a {@link Player}.
     *
     * @param texts           The texts to send.
     * @param receiver        The receiver of the text.
     * @param stripFormatting Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts or the receiver is null.
     */
    public void send(@NotNull String[] texts, @NotNull CommandSender receiver, boolean stripFormatting) {
        send(texts, receiver, null, stripFormatting);
    }

    /**
     * Sends a list texts to a {@link CommandSender} or a {@link Player}.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param receiver    The receiver of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the texts, the receiver, the sender or the permissions is null.
     */
    public void send(@NotNull String[] texts, @NotNull CommandSender receiver, TagResolver tagResolver, @NotNull CommandSender sender, @NotNull String... permissions) {
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
     * Sends a list texts to a {@link CommandSender} or a {@link Player}.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param receiver    The receiver of the text.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the texts, the receiver, the sender or the permissions is null.
     */
    public void send(@NotNull String[] texts, @NotNull CommandSender receiver, @NotNull CommandSender sender, @NotNull String... permissions) {
        send(texts, receiver, null, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    SEND MULTIPLE TEXTS (ARRAY) TO MULTIPLE RECEIVERS (ARRAY)    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Sends a list texts to a group of {@link CommandSender} or {@link Player}.
     * The texts are deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param receivers   The receivers of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException if the texts or the receivers is null.
     */
    public void send(@NotNull String[] texts, @NotNull CommandSender[] receivers, TagResolver tagResolver) {
        Preconditions.checkNotNull(receivers, "Receivers cannot be null");

        for (CommandSender receiver : receivers) {
            send(texts, receiver, tagResolver);
        }
    }

    /**
     * Sends a list texts to a group of {@link CommandSender} or {@link Player}.
     * The texts are deserialized before being sent.
     *
     * @param texts     The texts to send.
     * @param receivers The receivers of the text.
     * @throws NullPointerException if the texts or the receivers is null.
     */
    public void send(@NotNull String[] texts, @NotNull CommandSender[] receivers) {
        send(texts, receivers, null);
    }

    /**
     * Sends a list texts to a group of {@link CommandSender} or {@link Player}.
     *
     * @param texts           The texts to send.
     * @param receivers       The receivers of the text.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param stripFormatting Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts or the receivers is null.
     */
    public void send(@NotNull String[] texts, @NotNull CommandSender[] receivers, TagResolver tagResolver, boolean stripFormatting) {
        if (stripFormatting) {
            send(stripFormatting(texts, tagResolver), receivers, tagResolver);
        } else {
            send(texts, receivers, tagResolver);
        }
    }

    /**
     * Sends a list texts to a group of {@link CommandSender} or {@link Player}.
     *
     * @param texts           The texts to send.
     * @param receivers       The receivers of the text.
     * @param stripFormatting Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts or the receivers is null.
     */
    public void send(@NotNull String[] texts, @NotNull CommandSender[] receivers, boolean stripFormatting) {
        send(texts, receivers, null, stripFormatting);
    }

    /**
     * Sends a list texts to a group of {@link CommandSender} or {@link Player}.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param receivers   The receivers of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the receivers, the sender or the permissions is null.
     */
    public void send(@NotNull String[] texts, @NotNull CommandSender[] receivers, TagResolver tagResolver, @NotNull CommandSender sender, @NotNull String... permissions) {
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
     * Sends a list texts to a group of {@link CommandSender} or {@link Player}.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param receivers   The receivers of the text.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the receivers, the sender or the permissions is null.
     */
    public void send(@NotNull String[] texts, @NotNull CommandSender[] receivers, @NotNull CommandSender sender, @NotNull String... permissions) {
        send(texts, receivers, null, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    SEND MULTIPLE TEXTS (ARRAY) TO MULTIPLE RECEIVERS (COLLECTION)    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Sends a list texts to a group of {@link CommandSender} or {@link Player}.
     * The texts are deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param receivers   The receivers of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException if the texts or the receivers is null.
     */
    public <R extends Collection<? extends CommandSender>> void send(@NotNull String[] texts, @NotNull R receivers, TagResolver tagResolver) {
        Preconditions.checkNotNull(receivers, "Receivers cannot be null");

        for (CommandSender receiver : receivers) {
            if (receiver == null) continue;
            send(texts, receiver, tagResolver);
        }
    }

    /**
     * Sends a list texts to a group of {@link CommandSender} or {@link Player}.
     * The texts are deserialized before being sent.
     *
     * @param texts     The texts to send.
     * @param receivers The receivers of the text.
     * @throws NullPointerException if the texts or the receivers is null.
     */
    public <R extends Collection<? extends CommandSender>> void send(@NotNull String[] texts, @NotNull R receivers) {
        send(texts, receivers, null);
    }

    /**
     * Sends a list texts to a group of {@link CommandSender} or {@link Player}.
     *
     * @param texts           The texts to send.
     * @param receivers       The receivers of the text.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param stripFormatting Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts or the receivers is null.
     */
    public <R extends Collection<? extends CommandSender>> void send(@NotNull String[] texts, @NotNull R receivers, TagResolver tagResolver, boolean stripFormatting) {
        if (stripFormatting) {
            send(stripFormatting(texts, tagResolver), receivers, tagResolver);
        } else {
            send(texts, receivers, tagResolver);
        }
    }

    /**
     * Sends a list texts to a group of {@link CommandSender} or {@link Player}.
     *
     * @param texts           The texts to send.
     * @param receivers       The receivers of the text.
     * @param stripFormatting Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts or the receivers is null.
     */
    public <R extends Collection<? extends CommandSender>> void send(@NotNull String[] texts, @NotNull R receivers, boolean stripFormatting) {
        send(texts, receivers, null, stripFormatting);
    }

    /**
     * Sends a list texts to a group of {@link CommandSender} or {@link Player}.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param receivers   The receivers of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the receivers, the sender or the permissions is null.
     */
    public <R extends Collection<? extends CommandSender>> void send(@NotNull String[] texts, @NotNull R receivers, TagResolver tagResolver, @NotNull CommandSender sender, @NotNull String... permissions) {
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
     * Sends a list texts to a group of {@link CommandSender} or {@link Player}.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param receivers   The receivers of the text.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the receivers, the sender or the permissions is null.
     */
    public <R extends Collection<? extends CommandSender>> void send(@NotNull String[] texts, @NotNull R receivers, @NotNull CommandSender sender, @NotNull String... permissions) {
        send(texts, receivers, null, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    SEND MULTIPLE TEXTS (COLLECTION)    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Sends a list texts to a {@link CommandSender} or a {@link Player}.
     * The texts are deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param receiver    The receiver of the texts.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException if the texts or the receiver is null.
     */
    public <T extends Collection<String>> void send(@NotNull T texts, @NotNull CommandSender receiver, TagResolver tagResolver) {
        Preconditions.checkNotNull(texts, "Texts cannot be null");
        Preconditions.checkNotNull(receiver, "Receiver cannot be null");

        if (texts.isEmpty()) return;

        Audience audience;
        if (receiver instanceof Player) audience = audiences.player((Player) receiver);
        else audience = audiences.sender(receiver);

        for (String text : texts) {
            if (text == null) continue;
            audience.sendMessage(deserialize(text, tagResolver));
        }
    }

    /**
     * Sends a list texts to a {@link CommandSender} or a {@link Player}.
     * The texts are deserialized before being sent.
     *
     * @param texts    The texts to send.
     * @param receiver The receiver of the text.
     * @throws NullPointerException if the texts or the receiver is null.
     */
    public <T extends Collection<String>> void send(@NotNull T texts, @NotNull CommandSender receiver) {
        send(texts, receiver, null);
    }

    /**
     * Sends a list texts to a {@link CommandSender} or a {@link Player}.
     *
     * @param texts           The texts to send.
     * @param receiver        The receiver of the text.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param stripFormatting Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts or the receiver is null.
     */
    public <T extends Collection<String>> void send(@NotNull T texts, @NotNull CommandSender receiver, TagResolver tagResolver, boolean stripFormatting) {
        if (stripFormatting) {
            send(stripFormatting(texts, tagResolver), receiver, tagResolver);
        } else {
            send(texts, receiver, tagResolver);
        }
    }

    /**
     * Sends a list texts to a {@link CommandSender} or a {@link Player}.
     *
     * @param texts           The texts to send.
     * @param receiver        The receiver of the text.
     * @param stripFormatting Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts or the receiver is null.
     */
    public <T extends Collection<String>> void send(@NotNull T texts, @NotNull CommandSender receiver, boolean stripFormatting) {
        send(texts, receiver, null, stripFormatting);
    }

    /**
     * Sends a list texts to a {@link CommandSender} or a {@link Player}.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param receiver    The receiver of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the texts, the receiver, the sender or the permissions is null.
     */
    public <T extends Collection<String>> void send(@NotNull T texts, @NotNull CommandSender receiver, TagResolver tagResolver, @NotNull CommandSender sender, @NotNull String... permissions) {
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
     * Sends a list texts to a {@link CommandSender} or a {@link Player}.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param receiver    The receiver of the text.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the texts, the receiver, the sender or the permissions is null.
     */
    public <T extends Collection<String>> void send(@NotNull T texts, @NotNull CommandSender receiver, @NotNull CommandSender sender, @NotNull String... permissions) {
        send(texts, receiver, null, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    SEND MULTIPLE TEXTS (COLLECTION) TO MULTIPLE RECEIVERS (ARRAY)    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Sends a list texts to a group of {@link CommandSender} or {@link Player}.
     * The texts are deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param receivers   The receivers of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException if the texts or the receivers is null.
     */
    public <T extends Collection<String>> void send(@NotNull T texts, @NotNull CommandSender[] receivers, TagResolver tagResolver) {
        Preconditions.checkNotNull(receivers, "Receivers cannot be null");

        for (CommandSender receiver : receivers) {
            if (receiver == null) continue;
            send(texts, receiver, tagResolver);
        }
    }

    /**
     * Sends a list texts to a group of {@link CommandSender} or {@link Player}.
     * The texts are deserialized before being sent.
     *
     * @param texts     The texts to send.
     * @param receivers The receivers of the text.
     * @throws NullPointerException if the texts or the receivers is null.
     */
    public <T extends Collection<String>> void send(@NotNull T texts, @NotNull CommandSender[] receivers) {
        send(texts, receivers, null);
    }

    /**
     * Sends a list texts to a group of {@link CommandSender} or {@link Player}.
     *
     * @param texts           The texts to send.
     * @param receivers       The receivers of the text.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param stripFormatting Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts or the receivers is null.
     */
    public <T extends Collection<String>> void send(@NotNull T texts, @NotNull CommandSender[] receivers, TagResolver tagResolver, boolean stripFormatting) {
        if (stripFormatting) {
            send(stripFormatting(texts, tagResolver), receivers, tagResolver);
        } else {
            send(texts, receivers, tagResolver);
        }
    }

    /**
     * Sends a list texts to a group of {@link CommandSender} or {@link Player}.
     *
     * @param texts           The texts to send.
     * @param receivers       The receivers of the text.
     * @param stripFormatting Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts or the receivers is null.
     */
    public <T extends Collection<String>> void send(@NotNull T texts, @NotNull CommandSender[] receivers, boolean stripFormatting) {
        send(texts, receivers, null, stripFormatting);
    }

    /**
     * Sends a list texts to a group of {@link CommandSender} or {@link Player}.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param receivers   The receivers of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the receivers, the sender or the permissions is null.
     */
    public <T extends Collection<String>> void send(@NotNull T texts, @NotNull CommandSender[] receivers, TagResolver tagResolver, @NotNull CommandSender sender, @NotNull String... permissions) {
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
     * Sends a list texts to a group of {@link CommandSender} or {@link Player}.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param receivers   The receivers of the text.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the receivers, the sender or the permissions is null.
     */
    public <T extends Collection<String>> void send(@NotNull T texts, @NotNull CommandSender[] receivers, @NotNull CommandSender sender, @NotNull String... permissions) {
        send(texts, receivers, null, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    SEND MULTIPLE TEXTS (COLLECTION) TO MULTIPLE RECEIVERS (COLLECTION)    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Sends a list texts to a group of {@link CommandSender} or {@link Player}.
     * The texts are deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param receivers   The receivers of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @throws NullPointerException if the texts or the receivers is null.
     */
    public <T extends Collection<String>, R extends Collection<? extends CommandSender>> void send(@NotNull T texts, @NotNull R receivers, TagResolver tagResolver) {
        Preconditions.checkNotNull(receivers, "Receivers cannot be null");

        for (CommandSender receiver : receivers) {
            if (receiver == null) continue;
            send(texts, receiver, tagResolver);
        }
    }

    /**
     * Sends a list texts to a group of {@link CommandSender} or {@link Player}.
     * The texts are deserialized before being sent.
     *
     * @param texts     The texts to send.
     * @param receivers The receivers of the text.
     * @throws NullPointerException if the texts or the receivers is null.
     */
    public <T extends Collection<String>, R extends Collection<? extends CommandSender>> void send(@NotNull T texts, @NotNull R receivers) {
        send(texts, receivers, null);
    }

    /**
     * Sends a list texts to a group of {@link CommandSender} or {@link Player}.
     *
     * @param texts           The texts to send.
     * @param receivers       The receivers of the text.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param stripFormatting Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts or the receivers is null.
     */
    public <T extends Collection<String>, R extends Collection<? extends CommandSender>> void send(@NotNull T texts, @NotNull R receivers, TagResolver tagResolver, boolean stripFormatting) {
        if (stripFormatting) {
            send(stripFormatting(texts, tagResolver), receivers, tagResolver);
        } else {
            send(texts, receivers, tagResolver);
        }
    }

    /**
     * Sends a list texts to a group of {@link CommandSender} or {@link Player}.
     *
     * @param texts           The texts to send.
     * @param receivers       The receivers of the text.
     * @param stripFormatting Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts or the receivers is null.
     */
    public <T extends Collection<String>, R extends Collection<? extends CommandSender>> void send(@NotNull T texts, @NotNull R receivers, boolean stripFormatting) {
        send(texts, receivers, null, stripFormatting);
    }

    /**
     * Sends a list texts to a group of {@link CommandSender} or {@link Player}.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param receivers   The receivers of the text.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the receivers, the sender or the permissions is null.
     */
    public <T extends Collection<String>, R extends Collection<? extends CommandSender>> void send(@NotNull T texts, @NotNull R receivers, TagResolver tagResolver, @NotNull CommandSender sender, @NotNull String... permissions) {
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
     * Sends a list texts to a group of {@link CommandSender} or {@link Player}.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param receivers   The receivers of the text.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the receivers, the sender or the permissions is null.
     */
    public <T extends Collection<String>, R extends Collection<? extends CommandSender>> void send(@NotNull T texts, @NotNull R receivers, @NotNull CommandSender sender, @NotNull String... permissions) {
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

        audiences.players().sendMessage(deserialize(text, tagResolver));
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
    public void broadcast(@NotNull String text, TagResolver tagResolver, @NotNull CommandSender sender, @NotNull String... permissions) {
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
    public void broadcast(@NotNull String text, @NotNull CommandSender sender, @NotNull String... permissions) {
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

        Audience audience = audiences.players();
        for (String text : texts) {
            if (text == null) continue;
            audience.sendMessage(deserialize(text, tagResolver));
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
    public void broadcast(@NotNull String[] texts, TagResolver tagResolver, @NotNull CommandSender sender, @NotNull String... permissions) {
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
    public void broadcast(@NotNull String[] texts, @NotNull CommandSender sender, @NotNull String... permissions) {
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

        Audience audience = audiences.players();
        for (String text : texts) {
            if (text == null) continue;
            audience.sendMessage(deserialize(text, tagResolver));
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
    public <T extends Collection<String>> void broadcast(@NotNull T texts, TagResolver tagResolver, @NotNull CommandSender sender, @NotNull String... permissions) {
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
    public <T extends Collection<String>> void broadcast(@NotNull T texts, @NotNull CommandSender sender, @NotNull String... permissions) {
        broadcast(texts, (TagResolver) null, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    WORLD BROADCAST    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a text to all {@link Player}s in a world.
     * The text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param world       The {@link World} to send the text to.
     * @throws NullPointerException if the text or the world is null.
     */
    @SuppressWarnings("PatternValidation")
    public void broadcast(@NotNull String text, TagResolver tagResolver, @NotNull World world) {
        Preconditions.checkNotNull(text, "Text cannot be null");
        Preconditions.checkNotNull(world, "World cannot be null");

        audiences.world(Key.key(world.getKey().toString())).sendMessage(deserialize(text, tagResolver));
    }

    /**
     * Broadcasts a text to all {@link Player}s on the server.
     * The text is deserialized before being sent.
     *
     * @param text  The text to send.
     * @param world The {@link World} to send the text to.
     * @throws NullPointerException if the text or the world is null.
     */
    public void broadcast(@NotNull String text, @NotNull World world) {
        broadcast(text, null, world);
    }

    /**
     * Broadcasts a text to all {@link Player}s on the server.
     *
     * @param text            The text to send.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param world           The {@link World} to send the text to.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text or the world is null.
     */
    public void broadcast(@NotNull String text, TagResolver tagResolver, @NotNull World world, boolean stripFormatting) {
        if (stripFormatting) {
            broadcast(stripFormatting(text, tagResolver), tagResolver, world);
        } else {
            broadcast(text, tagResolver, world);
        }
    }

    /**
     * Broadcasts a text to all {@link Player}s on the server.
     *
     * @param text            The text to send.
     * @param world           The {@link World} to send the text to.
     * @param stripFormatting Whether to strip formatting from the text before sending it.
     * @throws NullPointerException if the text or the world is null.
     */
    public void broadcast(@NotNull String text, @NotNull World world, boolean stripFormatting) {
        broadcast(text, null, world, stripFormatting);
    }

    /**
     * Broadcasts a text to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param world       The {@link World} to send the text to.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the world, the sender or the permissions is null.
     */
    public void broadcast(@NotNull String text, TagResolver tagResolver, @NotNull World world, @NotNull CommandSender sender, @NotNull String... permissions) {
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

        broadcast(text, tagResolver, world, !hasPermission);
    }

    /**
     * Broadcasts a text to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text        The text to send.
     * @param world       The {@link World} to send the text to.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the text, the world, the sender or the permissions is null.
     */
    public void broadcast(@NotNull String text, @NotNull World world, @NotNull CommandSender sender, @NotNull String... permissions) {
        broadcast(text, null, world, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    WORLD BROADCAST MULTIPLE TEXTS (ARRAY)    */
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
    public void broadcast(@NotNull String[] texts, TagResolver tagResolver, @NotNull World world) {
        Preconditions.checkNotNull(texts, "Texts cannot be null");
        Preconditions.checkNotNull(world, "World cannot be null");

        if (texts.length == 0) return;

        Audience audience = audiences.world(Key.key(world.getKey().toString()));
        for (String text : texts) {
            if (text == null) continue;
            audience.sendMessage(deserialize(text, tagResolver));
        }
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     * The texts are deserialized before being sent.
     *
     * @param texts The texts to send.
     * @param world The {@link World} to send the text to.
     * @throws NullPointerException if the texts or the receiver is null.
     */
    public void broadcast(@NotNull String[] texts, @NotNull World world) {
        broadcast(texts, null, world);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     *
     * @param texts           The texts to send.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param world           The {@link World} to send the text to.
     * @param stripFormatting Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts or the world is null.
     */
    public void broadcast(@NotNull String[] texts, TagResolver tagResolver, @NotNull World world, boolean stripFormatting) {
        if (stripFormatting) {
            broadcast(stripFormatting(texts, tagResolver), tagResolver, world);
        } else {
            broadcast(texts, tagResolver, world);
        }
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     *
     * @param texts           The texts to send.
     * @param world           The {@link World} to send the text to.
     * @param stripFormatting Whether to strip formatting from the texts before sending it.
     */
    public void broadcast(@NotNull String[] texts, @NotNull World world, boolean stripFormatting) {
        broadcast(texts, null, world, stripFormatting);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param world       The {@link World} to send the text to.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the texts, the world, the sender or the permissions is null.
     */
    public void broadcast(@NotNull String[] texts, TagResolver tagResolver, @NotNull World world, @NotNull CommandSender sender, @NotNull String... permissions) {
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

        broadcast(texts, tagResolver, world, !hasPermission);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the texts, the world, the sender or the permissions is null.
     */
    public void broadcast(@NotNull String[] texts, @NotNull World world, @NotNull CommandSender sender, @NotNull String... permissions) {
        broadcast(texts, null, world, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    WORLD BROADCAST MULTIPLE TEXTS (COLLECTION)    */
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
    public <T extends Collection<String>> void broadcast(@NotNull T texts, TagResolver tagResolver, @NotNull World world) {
        Preconditions.checkNotNull(texts, "Texts cannot be null");
        Preconditions.checkNotNull(world, "World cannot be null");

        if (texts.isEmpty()) return;

        Audience audience = audiences.world(Key.key(world.getKey().toString()));
        for (String text : texts) {
            if (text == null) continue;
            audience.sendMessage(deserialize(text, tagResolver));
        }
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     * The texts are deserialized before being sent.
     *
     * @param texts The texts to send.
     * @param world The {@link World} to send the text to.
     * @throws NullPointerException if the texts or the receiver is null.
     */
    public <T extends Collection<String>> void broadcast(@NotNull T texts, @NotNull World world) {
        broadcast(texts, null, world);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     *
     * @param texts           The texts to send.
     * @param tagResolver     The {@link TagResolver} for any additional tags to handle.
     * @param world           The {@link World} to send the text to.
     * @param stripFormatting Whether to strip formatting from the texts before sending it.
     * @throws NullPointerException if the texts or the world is null.
     */
    public <T extends Collection<String>> void broadcast(@NotNull T texts, TagResolver tagResolver, @NotNull World world, boolean stripFormatting) {
        if (stripFormatting) {
            broadcast(stripFormatting(texts, tagResolver), tagResolver, world);
        } else {
            broadcast(texts, tagResolver, world);
        }
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     *
     * @param texts           The texts to send.
     * @param world           The {@link World} to send the text to.
     * @param stripFormatting Whether to strip formatting from the texts before sending it.
     */
    public <T extends Collection<String>> void broadcast(@NotNull T texts, @NotNull World world, boolean stripFormatting) {
        broadcast(texts, null, world, stripFormatting);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param tagResolver The {@link TagResolver} for any additional tags to handle.
     * @param world       The {@link World} to send the text to.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the texts, the world, the sender or the permissions is null.
     */
    public <T extends Collection<String>> void broadcast(@NotNull T texts, TagResolver tagResolver, @NotNull World world, @NotNull CommandSender sender, @NotNull String... permissions) {
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

        broadcast(texts, tagResolver, world, !hasPermission);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the texts, the world, the sender or the permissions is null.
     */
    public <T extends Collection<String>> void broadcast(@NotNull T texts, @NotNull World world, @NotNull CommandSender sender, @NotNull String... permissions) {
        broadcast(texts, null, world, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    PERMISSION BROADCAST    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a text to all {@link Player}s with a specific permission.
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

        audiences.permission(neededPermission).sendMessage(deserialize(text, tagResolver));
    }

    /**
     * Broadcasts a text to all {@link Player}s on the server.
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
     * Broadcasts a text to all {@link Player}s on the server.
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
     * Broadcasts a text to all {@link Player}s on the server.
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
     * Broadcasts a text to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text             The text to send.
     * @param tagResolver      The {@link TagResolver} for any additional tags to handle.
     * @param neededPermission The permission needed to receive the broadcast.
     * @param sender           The sender of the text.
     * @param permissions      The permissions to check.
     * @throws NullPointerException if the text, the neededPermission, the sender or the permissions is null.
     */
    public void broadcast(@NotNull String text, TagResolver tagResolver, @NotNull String neededPermission, @NotNull CommandSender sender, @NotNull String... permissions) {
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
     * Broadcasts a text to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text             The text to send.
     * @param neededPermission The permission needed to receive the broadcast.
     * @param sender           The sender of the text.
     * @param permissions      The permissions to check.
     * @throws NullPointerException if the text, the neededPermission, the sender or the permissions is null.
     */
    public void broadcast(@NotNull String text, @NotNull String neededPermission, @NotNull CommandSender sender, @NotNull String... permissions) {
        broadcast(text, null, neededPermission, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    PERMISSION BROADCAST MULTIPLE TEXTS (ARRAY)    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
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

        Audience audience = audiences.permission(neededPermission);
        for (String text : texts) {
            if (text == null) continue;
            audience.sendMessage(deserialize(text, tagResolver));
        }
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
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
     * Broadcasts a list of texts to all {@link Player}s on the server.
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
     * Broadcasts a list of texts to all {@link Player}s on the server.
     *
     * @param texts            The texts to send.
     * @param neededPermission The permission needed to receive the broadcast.
     * @param stripFormatting  Whether to strip formatting from the texts before sending it.
     */
    public void broadcast(@NotNull String[] texts, @NotNull String neededPermission, boolean stripFormatting) {
        broadcast(texts, null, neededPermission, stripFormatting);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts            The texts to send.
     * @param tagResolver      The {@link TagResolver} for any additional tags to handle.
     * @param neededPermission The permission needed to receive the broadcast.
     * @param sender           The sender of the text.
     * @param permissions      The permissions to check.
     * @throws NullPointerException if the texts, the neededPermission, the sender or the permissions is null.
     */
    public void broadcast(@NotNull String[] texts, TagResolver tagResolver, @NotNull String neededPermission, @NotNull CommandSender sender, @NotNull String... permissions) {
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
     * Broadcasts a list of texts to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the texts, the neededPermission, the sender or the permissions is null.
     */
    public void broadcast(@NotNull String[] texts, @NotNull String neededPermission, @NotNull CommandSender sender, @NotNull String... permissions) {
        broadcast(texts, null, neededPermission, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    PERMISSION BROADCAST MULTIPLE TEXTS (COLLECTION)    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
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

        Audience audience = audiences.permission(neededPermission);
        for (String text : texts) {
            if (text == null) continue;
            audience.sendMessage(deserialize(text, tagResolver));
        }
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
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
     * Broadcasts a list of texts to all {@link Player}s on the server.
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
     * Broadcasts a list of texts to all {@link Player}s on the server.
     *
     * @param texts            The texts to send.
     * @param neededPermission The permission needed to receive the broadcast.
     * @param stripFormatting  Whether to strip formatting from the texts before sending it.
     */
    public <T extends Collection<String>> void broadcast(@NotNull T texts, @NotNull String neededPermission, boolean stripFormatting) {
        broadcast(texts, null, neededPermission, stripFormatting);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts            The texts to send.
     * @param tagResolver      The {@link TagResolver} for any additional tags to handle.
     * @param neededPermission The permission needed to receive the broadcast.
     * @param sender           The sender of the text.
     * @param permissions      The permissions to check.
     * @throws NullPointerException if the texts, the neededPermission, the sender or the permissions is null.
     */
    public <T extends Collection<String>> void broadcast(@NotNull T texts, TagResolver tagResolver, @NotNull String neededPermission, @NotNull CommandSender sender, @NotNull String... permissions) {
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
     * Broadcasts a list of texts to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the texts, the neededPermission, the sender or the permissions is null.
     */
    public <T extends Collection<String>> void broadcast(@NotNull T texts, @NotNull String neededPermission, @NotNull CommandSender sender, @NotNull String... permissions) {
        broadcast(texts, null, neededPermission, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    PERMISSIONS (ARRAY) BROADCAST    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a text to all {@link Player}s with a specific permission.
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
            audiences.permission(permission).sendMessage(deserialize(text, tagResolver));
        }
    }

    /**
     * Broadcasts a text to all {@link Player}s on the server.
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
     * Broadcasts a text to all {@link Player}s on the server.
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
     * Broadcasts a text to all {@link Player}s on the server.
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
     * Broadcasts a text to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text              The text to send.
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param sender            The sender of the text.
     * @param permissions       The permissions to check.
     * @throws NullPointerException if the text, the neededPermissions, the sender or The permissions is null.
     */
    public void broadcast(@NotNull String text, TagResolver tagResolver, @NotNull String[] neededPermissions, @NotNull CommandSender sender, @NotNull String... permissions) {
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
     * Broadcasts a text to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text              The text to send.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param sender            The sender of the text.
     * @param permissions       The permissions to check.
     * @throws NullPointerException if the text, the neededPermissions, the sender or The permissions is null.
     */
    public void broadcast(@NotNull String text, @NotNull String[] neededPermissions, @NotNull CommandSender sender, @NotNull String... permissions) {
        broadcast(text, null, neededPermissions, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    PERMISSIONS (ARRAY) BROADCAST MULTIPLE TEXTS (ARRAY)    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
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

            Audience audience = audiences.permission(permission);
            for (String text : texts) {
                if (text == null) continue;
                audience.sendMessage(deserialize(text, tagResolver));
            }
        }
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
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
     * Broadcasts a list of texts to all {@link Player}s on the server.
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
     * Broadcasts a list of texts to all {@link Player}s on the server.
     *
     * @param texts             The texts to send.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param stripFormatting   Whether to strip formatting from the texts before sending it.
     */
    public void broadcast(@NotNull String[] texts, @NotNull String[] neededPermissions, boolean stripFormatting) {
        broadcast(texts, null, neededPermissions, stripFormatting);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts             The texts to send.
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param sender            The sender of the text.
     * @param permissions       The permissions to check.
     * @throws NullPointerException if the texts, the neededPermissions, the sender or The permissions is null.
     */
    public void broadcast(@NotNull String[] texts, TagResolver tagResolver, @NotNull String[] neededPermissions, @NotNull CommandSender sender, @NotNull String... permissions) {
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
     * Broadcasts a list of texts to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the texts, the neededPermissions, the sender or The permissions is null.
     */
    public void broadcast(@NotNull String[] texts, @NotNull String[] neededPermissions, @NotNull CommandSender sender, @NotNull String... permissions) {
        broadcast(texts, null, neededPermissions, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    PERMISSIONS (ARRAY) BROADCAST MULTIPLE TEXTS (COLLECTION)    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
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

            Audience audience = audiences.permission(permission);
            for (String text : texts) {
                if (text == null) continue;
                audience.sendMessage(deserialize(text, tagResolver));
            }
        }
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
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
     * Broadcasts a list of texts to all {@link Player}s on the server.
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
     * Broadcasts a list of texts to all {@link Player}s on the server.
     *
     * @param texts             The texts to send.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param stripFormatting   Whether to strip formatting from the texts before sending it.
     */
    public <T extends Collection<String>> void broadcast(@NotNull T texts, @NotNull String[] neededPermissions, boolean stripFormatting) {
        broadcast(texts, null, neededPermissions, stripFormatting);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts             The texts to send.
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param sender            The sender of the text.
     * @param permissions       The permissions to check.
     * @throws NullPointerException if the texts, the neededPermissions, the sender or The permissions is null.
     */
    public <T extends Collection<String>> void broadcast(@NotNull T texts, TagResolver tagResolver, @NotNull String[] neededPermissions, @NotNull CommandSender sender, @NotNull String... permissions) {
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
     * Broadcasts a list of texts to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the texts, the neededPermissions, the sender or The permissions is null.
     */
    public <T extends Collection<String>> void broadcast(@NotNull T texts, @NotNull String[] neededPermissions, @NotNull CommandSender sender, @NotNull String... permissions) {
        broadcast(texts, null, neededPermissions, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    PERMISSIONS (COLLECTION) BROADCAST    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a text to all {@link Player}s with a specific permission.
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
            audiences.permission(permission).sendMessage(deserialize(text, tagResolver));
        }
    }

    /**
     * Broadcasts a text to all {@link Player}s on the server.
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
     * Broadcasts a text to all {@link Player}s on the server.
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
     * Broadcasts a text to all {@link Player}s on the server.
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
     * Broadcasts a text to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text              The text to send.
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param sender            The sender of the text.
     * @param permissions       The permissions to check.
     * @throws NullPointerException if the text, the neededPermissions, the sender or The permissions is null.
     */
    public <P extends Collection<String>> void broadcast(@NotNull String text, TagResolver tagResolver, @NotNull P neededPermissions, @NotNull CommandSender sender, @NotNull String... permissions) {
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
     * Broadcasts a text to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the text is deserialized before being sent.
     *
     * @param text              The text to send.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param sender            The sender of the text.
     * @param permissions       The permissions to check.
     * @throws NullPointerException if the text, the neededPermissions, the sender or The permissions is null.
     */
    public <P extends Collection<String>> void broadcast(@NotNull String text, @NotNull P neededPermissions, @NotNull CommandSender sender, @NotNull String... permissions) {
        broadcast(text, null, neededPermissions, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    PERMISSIONS (ARRAY) BROADCAST MULTIPLE TEXTS (ARRAY)    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
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

            Audience audience = audiences.permission(permission);
            for (String text : texts) {
                if (text == null) continue;
                audience.sendMessage(deserialize(text, tagResolver));
            }
        }
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
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
     * Broadcasts a list of texts to all {@link Player}s on the server.
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
     * Broadcasts a list of texts to all {@link Player}s on the server.
     *
     * @param texts             The texts to send.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param stripFormatting   Whether to strip formatting from the texts before sending it.
     */
    public <P extends Collection<String>> void broadcast(@NotNull String[] texts, @NotNull P neededPermissions, boolean stripFormatting) {
        broadcast(texts, null, neededPermissions, stripFormatting);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts             The texts to send.
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param sender            The sender of the text.
     * @param permissions       The permissions to check.
     * @throws NullPointerException if the texts, the neededPermissions, the sender or The permissions is null.
     */
    public <P extends Collection<String>> void broadcast(@NotNull String[] texts, TagResolver tagResolver, @NotNull P neededPermissions, @NotNull CommandSender sender, @NotNull String... permissions) {
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
     * Broadcasts a list of texts to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the texts, the neededPermissions, the sender or The permissions is null.
     */
    public <P extends Collection<String>> void broadcast(@NotNull String[] texts, @NotNull P neededPermissions, @NotNull CommandSender sender, @NotNull String... permissions) {
        broadcast(texts, null, neededPermissions, sender, permissions);
    }

    /* -------------------------------------------------------------------------------------------------------------------------------------------- */
    /*    PERMISSIONS (ARRAY) BROADCAST MULTIPLE TEXTS (COLLECTION)    */
    /* -------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
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

            Audience audience = audiences.permission(permission);
            for (String text : texts) {
                if (text == null) continue;
                audience.sendMessage(deserialize(text, tagResolver));
            }
        }
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
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
     * Broadcasts a list of texts to all {@link Player}s on the server.
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
     * Broadcasts a list of texts to all {@link Player}s on the server.
     *
     * @param texts             The texts to send.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param stripFormatting   Whether to strip formatting from the texts before sending it.
     */
    public <P extends Collection<String>, T extends Collection<String>> void broadcast(@NotNull T texts, @NotNull P neededPermissions, boolean stripFormatting) {
        broadcast(texts, null, neededPermissions, stripFormatting);
    }

    /**
     * Broadcasts a list of texts to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts             The texts to send.
     * @param tagResolver       The {@link TagResolver} for any additional tags to handle.
     * @param neededPermissions The receiver must have one of these permissions to receive the broadcast.
     * @param sender            The sender of the text.
     * @param permissions       The permissions to check.
     * @throws NullPointerException if the texts, the neededPermissions, the sender or The permissions is null.
     */
    public <P extends Collection<String>, T extends Collection<String>> void broadcast(@NotNull T texts, TagResolver tagResolver, @NotNull P neededPermissions, @NotNull CommandSender sender, @NotNull String... permissions) {
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
     * Broadcasts a list of texts to all {@link Player}s on the server.
     * If the sender has at least one of the provided permissions, the texts is deserialized before being sent.
     *
     * @param texts       The texts to send.
     * @param sender      The sender of the text.
     * @param permissions The permissions to check.
     * @throws NullPointerException if the texts, the neededPermissions, the sender or The permissions is null.
     */
    public <P extends Collection<String>, T extends Collection<String>> void broadcast(@NotNull T texts, @NotNull P neededPermissions, @NotNull CommandSender sender, @NotNull String... permissions) {
        broadcast(texts, null, neededPermissions, sender, permissions);
    }
}
