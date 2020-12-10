package it.multicoredev.mbcore.bungeecord.pmc;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
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
@SuppressWarnings("UnstableApiUsage")
public class CustomMessage {
    private final String channel;
    private final ByteArrayDataOutput out;

    public CustomMessage(@NotNull String channel) {
        Objects.requireNonNull(channel);
        this.channel = channel.toLowerCase();
        out = ByteStreams.newDataOutput();
    }

    public String getChannel() {
        return channel;
    }

    public byte[] toByteArray() {
        return out.toByteArray();
    }

    public void writeByte(byte b) {
        out.write(b);
    }

    public void writeByte(int b) {
        out.write(b);
    }

    public void writeByteArray(byte[] bytes) {
        out.write(bytes, 0, bytes.length);
    }

    public void writeBoolean(boolean b) {
        out.writeBoolean(b);
    }

    public void writeShort(short s) {
        out.writeShort(s);
    }

    public void writeShort(int s) {
        out.writeShort(s);
    }

    public void writeChar(char c) {
        out.writeChar(c);
    }

    public void writeChar(int c) {
        out.writeChar(c);
    }

    public void writeInt(int i) {
        out.writeInt(i);
    }

    public void writeLong(long l) {
        out.writeLong(l);
    }

    public void writeFloat(float f) {
        out.writeFloat(f);
    }

    public void writeDouble(double d) {
        out.writeDouble(d);
    }

    public void writeChars(String s) {
        out.writeChars(s);
    }

    public void writeUTF(String s) {
        out.writeUTF(s);
    }
}
