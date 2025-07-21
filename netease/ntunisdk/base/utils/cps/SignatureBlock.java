package com.netease.ntunisdk.base.utils.cps;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.LinkedList;

/* loaded from: classes5.dex */
public class SignatureBlock {
    LinkedList<IdValue> idValuesList = new LinkedList<>();
    byte[] magic = new byte[16];
    long size1;
    long size2;

    SignatureBlock(RandomAccessFile randomAccessFile, long j) throws IOException {
        try {
            randomAccessFile.seek(j);
            long jA = a.a(randomAccessFile);
            this.size1 = jA;
            long j2 = jA - 24;
            while (j2 > 0) {
                IdValue idValue = new IdValue(randomAccessFile);
                j2 -= idValue.size + 8;
                this.idValuesList.add(idValue);
            }
            this.size2 = a.a(randomAccessFile);
            randomAccessFile.readFully(this.magic);
            if (this.size1 == this.size2) {
            } else {
                throw new IllegalStateException("size1 != size2");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String getChannel() throws UnsupportedEncodingException {
        Iterator<IdValue> it = this.idValuesList.iterator();
        while (it.hasNext()) {
            IdValue next = it.next();
            if (next.id == -15322781) {
                return next.getValue();
            }
        }
        return null;
    }

    boolean checkV2() {
        Iterator<IdValue> it = this.idValuesList.iterator();
        while (it.hasNext()) {
            if (it.next().hasV2Mark()) {
                return true;
            }
        }
        return false;
    }

    static class IdValue {
        static final int CUSTOM_CHANNEL_ID = -15322781;
        static final int V2_MARK = 1896449818;
        int id;
        long size;
        byte[] value;

        IdValue(RandomAccessFile randomAccessFile) throws IOException {
            this.size = a.a(randomAccessFile);
            byte[] bArr = new byte[4];
            randomAccessFile.readFully(bArr);
            this.id = a.a(bArr[0], bArr[1], bArr[2], bArr[3]);
            byte[] bArr2 = new byte[(int) (this.size - 4)];
            this.value = bArr2;
            randomAccessFile.readFully(bArr2);
        }

        String getValue() throws UnsupportedEncodingException {
            return new String(this.value, "UTF-8");
        }

        boolean hasV2Mark() {
            return V2_MARK == this.id;
        }
    }
}