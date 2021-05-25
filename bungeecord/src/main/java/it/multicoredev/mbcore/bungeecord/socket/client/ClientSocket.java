package it.multicoredev.mbcore.bungeecord.socket.client;

import com.google.gson.Gson;
import it.multicoredev.mbcore.bungeecord.socket.DefLogger;
import it.multicoredev.mbcore.bungeecord.socket.Disconnect;
import it.multicoredev.mbcore.bungeecord.socket.ILogger;
import it.multicoredev.mbcore.bungeecord.socket.client.events.ClientSockConnectedEvent;
import it.multicoredev.mbcore.bungeecord.socket.client.events.ClientSockDisconnectedEvent;
import it.multicoredev.mbcore.bungeecord.socket.client.events.SockMessageReceivedEvent;
import it.multicoredev.mbcore.bungeecord.socket.client.events.SockMessageSentEvent;
import it.multicoredev.mbcore.bungeecord.util.Utils;
import net.md_5.bungee.api.ProxyServer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

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
public class ClientSocket implements Runnable {
    private final String host;
    private final int port;
    private final ILogger logger;
    private final boolean autoreconnect;
    private final long reconnectPeriod;
    private final Gson gson = new Gson();
    private AsynchronousSocketChannel client;
    private boolean connected = false;
    private boolean firstTime = true;

    public ClientSocket(@NotNull String host, int port, @NotNull ILogger logger, boolean autoreconnect, long reconnectPeriod) throws IOException {
        if (host == null || host.trim().isEmpty()) throw new IllegalArgumentException("Host cannot be null or empty");
        if (port < 0 || port > 65535) throw new IllegalArgumentException("Port must be between 0 and 65535");
        if (logger == null) throw new IllegalArgumentException("Logger cannot be null");

        this.host = host;
        this.port = port;
        this.logger = logger;
        this.autoreconnect = autoreconnect;
        this.reconnectPeriod = reconnectPeriod;
    }

    public ClientSocket(@NotNull String host, int port, @NotNull ILogger logger, boolean autoreconnect) throws IOException {
        this(host, port, logger, autoreconnect, autoreconnect ? 2000 : -1);
    }

    public ClientSocket(@NotNull String host, int port, @NotNull ILogger logger) throws IOException {
        this(host, port, logger, false, -1);
    }

    public ClientSocket(@NotNull String host, int port, boolean autoreconnect, long reconnectPeriod) throws IOException {
        this(host, port, DefLogger.getInstance(), autoreconnect, reconnectPeriod);
    }

    public ClientSocket(@NotNull String host, int port, boolean autoreconnect) throws IOException {
        this(host, port, DefLogger.getInstance(), autoreconnect, autoreconnect ? 2000 : -1);
    }

    public ClientSocket(@NotNull String host, int port) throws IOException {
        this(host, port, DefLogger.getInstance());
    }

    @Override
    public void run() {
        if (!connect(firstTime)) {
            if (autoreconnect) reconnect();
            return;
        }

        try {
            ByteBuffer buffer = ByteBuffer.allocate(2048);
            CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
            while (client.read(buffer).get() != -1) {
                buffer.flip();

                try {
                    if (!connected) return;

                    String in = decoder.decode(buffer).toString();
                    ProxyServer.getInstance().getPluginManager().callEvent(new SockMessageReceivedEvent(this, in));
                } catch (IOException e) {
                    logger.exception(e);
                }

                if (buffer.hasRemaining()) buffer.compact();
                else buffer.clear();
            }
        } catch (InterruptedException e) {
            logger.exception(e);
        } catch (ExecutionException ignored) {
        } finally {
            boolean c = connected;
            if (connected) close();
            if (c && autoreconnect) {
                logger.info(String.format("Trying to connect to %s:%d", host, port));
                reconnect();
            }
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public void write(@NotNull String json) throws ExecutionException, InterruptedException {
        if (!connected) throw new IllegalStateException("Connection not ready");
        if (json == null || json.trim().isEmpty()) throw new IllegalArgumentException("Argument cannot be null or empty.");
        if (!Utils.validateJson(json)) throw new IllegalArgumentException("Argument is not a valid json string.");

        try {
            ByteBuffer outputBuffer = ByteBuffer.wrap(json.getBytes());
            client.write(outputBuffer).get();

            ProxyServer.getInstance().getPluginManager().callEvent(new SockMessageSentEvent(this, json));
        } catch (ExecutionException e) {
            connected = false;
            throw e;
        }
    }

    public void write(@NotNull Object obj) throws ExecutionException, InterruptedException {
        String json;

        try {
            json = gson.toJson(obj);
        } catch (Exception ignored) {
            throw new IllegalArgumentException("Cannot serialize your object to json. Try adding a custom serializer using Gson '@JsonAdapter' annotation");
        }

        write(json);
    }

    public void disconnect(@NotNull Disconnect disconnect) {
        if (disconnect == null) throw new IllegalArgumentException("Disconnect reason cannot be null");

        logger.info(String.format("Disconnecting client from %s:%d...", host, port));

        try {
            write(disconnect);
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }

        close();
    }

    private boolean connect(boolean errors) {
        firstTime = false;

        try {
            client = AsynchronousSocketChannel.open();
            client.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            client.connect(new InetSocketAddress(host, port)).get();

            connected = true;
            logger.info(String.format("ClientSocket connected to %s:%d", host, port));
            ProxyServer.getInstance().getPluginManager().callEvent(new ClientSockConnectedEvent());
        } catch (IOException | ExecutionException | InterruptedException e) {
            if (errors) logger.severe("Cannot connect to ServerSocket.");
        }

        return connected;
    }

    private void reconnect() {
        try {
            Thread.sleep(reconnectPeriod);
        } catch (InterruptedException ignored) {
        }

        run();
    }

    private void close() {
        connected = false;

        try {
            logger.info("ClientSocket disconnected.");
            ProxyServer.getInstance().getPluginManager().callEvent(new ClientSockDisconnectedEvent());
            client.close();
        } catch (IOException ignored) {
        }
    }
}
