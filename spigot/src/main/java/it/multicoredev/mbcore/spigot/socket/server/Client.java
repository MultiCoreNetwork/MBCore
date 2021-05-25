package it.multicoredev.mbcore.spigot.socket.server;

import it.multicoredev.mbcore.spigot.socket.Disconnect;
import it.multicoredev.mbcore.spigot.socket.server.events.ClientSockConnectedEvent;
import it.multicoredev.mbcore.spigot.socket.server.events.ClientSockDisconnectedEvent;
import it.multicoredev.mbcore.spigot.socket.server.events.SockMessageReceivedEvent;
import it.multicoredev.mbcore.spigot.socket.server.events.SockMessageSentEvent;
import it.multicoredev.mbcore.spigot.util.Utils;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Copyright © 2021 by Lorenzo Magni
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
public class Client implements Runnable {
    private final AsynchronousSocketChannel client;
    private final ServerSocket server;
    private String address;

    Client(Future<AsynchronousSocketChannel> futureClient, ServerSocket server) throws InterruptedException, ExecutionException {
        this.server = server;
        this.client = futureClient.get();
    }

    @Override
    public void run() {
        Bukkit.getPluginManager().callEvent(new ClientSockConnectedEvent(this));

        try {
            address = client.getRemoteAddress().toString();
            server.logger().info(String.format("ClientSocket connected: %s", address));
        } catch (IOException ignored) {
            address = "unknown";
        }

        try {
            ByteBuffer buffer = ByteBuffer.allocate(2048);
            CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
            while (client.read(buffer).get() != -1) {
                buffer.flip();

                try {
                    String in = decoder.decode(buffer).toString();
                    Bukkit.getPluginManager().callEvent(new SockMessageReceivedEvent(this, in));
                } catch (IOException e) {
                    server.logger().exception(e);
                }

                if (buffer.hasRemaining()) buffer.compact();
                else buffer.clear();
            }
        } catch (InterruptedException e) {
            server.logger().exception(e);
        } catch (ExecutionException ignored) {
        } finally {
            close();
        }
    }

    public void write(@NotNull String json) throws ExecutionException, InterruptedException {
        if (json == null || json.trim().isEmpty()) throw new IllegalArgumentException("Argument cannot be null or empty.");
        if (!Utils.validateJson(json)) throw new IllegalArgumentException("Argument is not a valid json string.");

        client.write(ByteBuffer.wrap(json.getBytes(StandardCharsets.UTF_8))).get();

        Bukkit.getPluginManager().callEvent(new SockMessageSentEvent(this, json));
    }

    public void write(@NotNull Object obj) throws ExecutionException, InterruptedException {
        String json;

        try {
            json = server.gson().toJson(obj);
        } catch (Exception ignored) {
            throw new IllegalArgumentException("Cannot serialize your object to json. Try adding a custom serializer using Gson '@JsonAdapter' annotation");
        }

        write(json);
    }

    public void disconnect(@NotNull Disconnect disconnect) {
        if (disconnect == null) throw new IllegalArgumentException("Disconnect reason cannot be null");

        server.logger().info(String.format("Disconnecting ClientSocket %s...", address));

        try {
            write(disconnect);
        } catch (Exception e) {
            server.logger().severe(e.getMessage());
        }

        close();
    }

    private void close() {
        try {
            server.logger().info(String.format("ClientSocket %s disconnected.", address));
            server.removeClient(this);

            SocketAddress a = null;
            try {
                a = client.getRemoteAddress();
            } catch (Exception ignored) {
            }

            Bukkit.getPluginManager().callEvent(new ClientSockDisconnectedEvent(a));

            client.close();
        } catch (IOException ignored) {
        }
    }
}