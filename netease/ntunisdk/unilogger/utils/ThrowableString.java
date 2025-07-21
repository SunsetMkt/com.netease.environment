package com.netease.ntunisdk.unilogger.utils;

import java.io.OutputStream;
import java.io.PrintStream;

/* loaded from: classes5.dex */
public class ThrowableString {
    public static String get(Throwable th) {
        StringBuilder sb = new StringBuilder();
        th.printStackTrace(new PrintStream(new TSTOutputStream(sb)));
        return sb.toString();
    }

    private static class TSTOutputStream extends OutputStream {
        private final StringBuilder sb;

        TSTOutputStream(StringBuilder sb) {
            this.sb = sb;
        }

        @Override // java.io.OutputStream
        public void write(int i) {
            this.sb.append(Character.toChars(i));
        }
    }
}