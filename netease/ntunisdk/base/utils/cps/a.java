package com.netease.ntunisdk.base.utils.cps;

import java.io.IOException;
import java.io.RandomAccessFile;

/* compiled from: TransUtil.java */
/* loaded from: classes5.dex */
public final class a {
    static int a(byte b, byte b2, byte b3, byte b4) {
        return (b & 255) | ((b2 & 255) << 8) | ((b3 & 255) << 16) | ((b4 & 255) << 24);
    }

    static short a(byte b, byte b2) {
        return (short) ((b & 255) | ((b2 & 255) << 8));
    }

    static long a(RandomAccessFile randomAccessFile) throws IOException {
        randomAccessFile.readFully(new byte[8]);
        return (r1[0] & 255) | ((r1[1] & 255) << 8) | ((r1[2] & 255) << 16) | ((r1[3] & 255) << 24) | ((r1[4] & 255) << 32) | ((r1[5] & 255) << 40) | ((r1[6] & 255) << 48) | ((r1[7] & 255) << 56);
    }
}