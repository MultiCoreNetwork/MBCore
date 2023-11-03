package it.multicoredev.mbcore.spigot.pmc;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
public final class ForwardMessage {
    private final ByteArrayOutputStream bytes;
    private final DataOutputStream out;

    public ForwardMessage() {
        bytes = new ByteArrayOutputStream();
        out = new DataOutputStream(bytes);
    }

    public int size() {
        return bytes.toByteArray().length;
    }

    public byte[] toByteArray() {
        return bytes.toByteArray();
    }

    public void writeByte(byte b) {
        try {
            out.write(b);
        } catch (IOException ignored) {
        }
    }

    public void writeByte(int b) {
        try {
            out.write(b);
        } catch (IOException ignored) {
        }
    }

    public void writeByteArray(byte[] bytes) {
        try {
            out.write(bytes, 0, bytes.length);
        } catch (IOException ignored) {
        }
    }

    public void writeBoolean(boolean b) {
        try {
            out.writeBoolean(b);
        } catch (IOException ignored) {
        }
    }

    public void writeShort(short s) {
        try {
            out.writeShort(s);
        } catch (IOException ignored) {
        }
    }

    public void writeShort(int s) {
        try {
            out.writeShort(s);
        } catch (IOException ignored) {
        }
    }
    
    public void writeChar(char c) {
        try {
            out.writeChar(c);
        } catch (IOException ignored) {
        }
    }

    public void writeChar(int c) {
        try {
            out.writeChar(c);
        } catch (IOException ignored) {
        }
    }
    
    public void writeInt(int i) {
        try {
            out.writeInt(i);
        } catch (IOException ignored) {
        }
    }
    
    public void writeLong(long l) {
        try {
            out.writeLong(l);
        } catch (IOException ignored) {
        }
    }
    
    public void writeFloat(float f) {
        try {
            out.writeFloat(f);
        } catch (IOException ignored) {
        }
    }

    public void writeDouble(double d) {
        try {
            out.writeDouble(d);
        } catch (IOException ignored) {
        }
    }

    public void writeBytes(String s) {
        try {
            out.writeBytes(s);
        } catch (IOException ignored) {
        }
    }

    public void writeChars(String s) {
        try {
            out.writeChars(s);
        } catch (IOException ignored) {
        }
    }

    public void writeUTF(String s) {
        try {
            out.writeUTF(s);
        } catch (IOException ignored) {
        }
    }
}