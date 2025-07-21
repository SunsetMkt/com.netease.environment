package com.netease.ntunisdk.base.utils.cps;

import java.io.IOException;
import java.io.RandomAccessFile;

/* loaded from: classes5.dex */
public class ECDR {
    byte[] contentBytes;
    private int offset;

    long offset() {
        return this.offset & 4294967295L;
    }

    void offsetIncre(int i) {
        this.offset += i;
    }

    ECDR(RandomAccessFile randomAccessFile, int i) throws IOException {
        try {
            randomAccessFile.seek(randomAccessFile.length() - i);
            byte[] bArr = new byte[i];
            this.contentBytes = bArr;
            randomAccessFile.readFully(bArr);
            byte[] bArr2 = this.contentBytes;
            this.offset = a.a(bArr2[16], bArr2[17], bArr2[18], bArr2[19]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}