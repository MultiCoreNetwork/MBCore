package it.multicoredev.mbcore.spigot.socket.server;

import com.google.gson.Gson;
import it.multicoredev.mbcore.spigot.socket.DefLogger;
import it.multicoredev.mbcore.spigot.socket.Disconnect;
import it.multicoredev.mbcore.spigot.socket.ILogger;
import it.multicoredev.mbcore.spigot.socket.server.events.ServerSockStartedEvent;
import it.multicoredev.mbcore.spigot.socket.server.events.ServerSockStoppedEvent;
import it.multicoredev.mbcore.spigot.util.JsonValidator;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
public class ServerSocket implements Runnable {
    private final String host;
    private final int port;
    private final ILogger logger;
    private final AsynchronousServerSocketChannel server;
    private final Gson gson = new Gson();
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final List<Client> clients = new CopyOnWriteArrayList<>();
    private boolean running = false;

    public ServerSocket(@NotNull String host, int port, @NotNull ILogger logger) throws IOException {
        if (host == null || host.trim().isEmpty()) throw new IllegalArgumentException("Host cannot be null or empty");
        if (port < 0 || port > 65535) throw new IllegalArgumentException("Port must be between 0 and 65535");
        if (logger == null) throw new IllegalArgumentException("Logger cannot be null");

        this.host = host;
        this.port = port;
        this.logger = logger;

        this.server = AsynchronousServerSocketChannel.open();
        server.setOption(StandardSocketOptions.SO_REUSEADDR, true);
    }

    public ServerSocket(@NotNull String host, int port) throws IOException {
        this(host, port, DefLogger.getInstance());
    }

    public ServerSocket(int port, @NotNull ILogger logger) throws IOException {
        this("127.0.0.1", port, logger);
    }

    public ServerSocket(int port) throws IOException {
        this("127.0.0.1", port);
    }

    public void run() {
        try {
            server.bind(new InetSocketAddress(host, port));
        } catch (IOException e) {
            logger.exception(e);
            return;
        }

        running = true;
        logger.info(String.format("ServerSocket listening at %s:%d.", host, port));
        Bukkit.getPluginManager().callEvent(new ServerSockStartedEvent());

        while (running) {
            try {
                Client client = new Client(server.accept(), this);
                clients.add(client);
                executor.submit(client);
            } catch (Exception e) {
                logger.exception(e);
            }
        }

        shutdown();
    }

    public boolean isRunning() {
        return running;
    }

    public void stop() {
        running = false;
    }

    public void broadcast(@NotNull String json) {
        if (json == null || json.trim().isEmpty()) throw new IllegalArgumentException("Argument cannot be null or empty.");
        if (!JsonValidator.validateJson(json)) throw new IllegalArgumentException("Argument is not a valid json string.");

        clients.forEach(client -> {
            try {
                client.write(json);
            } catch (ExecutionException | InterruptedException e) {
                logger.exception(e);
            }
        });
    }

    public void broadcast(@NotNull Object obj) {
        String json;

        try {
            json = gson.toJson(obj);
        } catch (Exception ignored) {
            throw new IllegalArgumentException("Cannot serialize your object to json. Try adding a custom serializer using Gson '@JsonAdapter' annotation");
        }

        broadcast(json);
    }

    ILogger logger() {
        return logger;
    }

    Gson gson() {
        return gson;
    }

    void removeClient(Client client) {
        clients.remove(client);
    }

    private void shutdown() {
        logger.info("ServerSocket is shutting down...");

        executor.shutdown();

        Disconnect disconnect = new Disconnect("shutdown");
        clients.forEach(client -> client.disconnect(disconnect));
        clients.clear();

        while (!executor.isTerminated()) {
        }

        try {
            server.close();
        } catch (IOException e) {
            logger.exception(e);
        }

        logger.info("ServerSocket closed.");
        Bukkit.getPluginManager().callEvent(new ServerSockStoppedEvent());
    }
}
