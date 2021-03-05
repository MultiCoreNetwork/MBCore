package it.multicoredev.mbcore.bungeecord.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

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
public class ConfigManager {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    /**
     * Load the configuration from file.
     *
     * @param file    The file to load.
     * @param typeOff The type of the configuration.
     * @param <T>     The type of the configuration.
     * @return The configuration objects saved in the file.
     * @throws Exception IOException or JsonSyntaxException when the gson fails to load the file or when it fails to parse the file.
     */
    public static <T> T loadConfig(File file, Type typeOff) throws Exception {
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            return gson.fromJson(reader, typeOff);
        }
    }

    /**
     * Save the configuration to file.
     *
     * @param file   The file to be saved.
     * @param config The config to be saved.
     * @throws IOException Thrown when the config cannot be saved to file.
     */
    public static synchronized void saveConfig(File file, Object config) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write(gson.toJson(config));
            writer.flush();
        }
    }

    /**
     * Save the configuration to file asynchronously.
     *
     * @param plugin The instance of your plugin.
     * @param file   The file to be saved.
     * @param config The config to be saved.
     */
    public static void saveConfigAsync(Plugin plugin, File file, Object config) {
        ProxyServer.getInstance().getScheduler().runAsync(plugin, () -> {
            try {
                saveConfig(file, config);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
