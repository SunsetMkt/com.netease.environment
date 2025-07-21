package com.netease.environment.OIIO00I.OIIO00I.OIIO0OO;

import java.io.InputStream;

/* compiled from: Decoder.java */
/* loaded from: classes6.dex */
public class OIIO0O0 {

    /* renamed from: OIIO00I, reason: collision with root package name */
    int f1554OIIO00I;
    int OIIO0O0;
    InputStream OIIO0OO;

    public final void OIIO00I(InputStream inputStream) {
        this.OIIO0OO = inputStream;
    }

    public final void OIIO0O0() {
        this.OIIO0OO = null;
    }

    public final void OIIO00I() {
        this.OIIO0O0 = 0;
        this.f1554OIIO00I = -1;
        for (int i = 0; i < 5; i++) {
            this.OIIO0O0 = (this.OIIO0O0 << 8) | this.OIIO0OO.read();
        }
    }

    public final int OIIO00I(int i) {
        int i2 = 0;
        while (i != 0) {
            int i3 = this.f1554OIIO00I >>> 1;
            this.f1554OIIO00I = i3;
            int i4 = this.OIIO0O0;
            int i5 = (i4 - i3) >>> 31;
            int i6 = i4 - ((i5 - 1) & i3);
            this.OIIO0O0 = i6;
            i2 = (i2 << 1) | (1 - i5);
            if ((i3 & (-16777216)) == 0) {
                this.OIIO0O0 = (i6 << 8) | this.OIIO0OO.read();
                this.f1554OIIO00I <<= 8;
            }
            i--;
        }
        return i2;
    }

    public int OIIO00I(short[] sArr, int i) {
        short s = sArr[i];
        int i2 = this.f1554OIIO00I;
        int i3 = (i2 >>> 11) * s;
        int i4 = this.OIIO0O0;
        if ((i4 ^ Integer.MIN_VALUE) < (Integer.MIN_VALUE ^ i3)) {
            this.f1554OIIO00I = i3;
            sArr[i] = (short) (s + ((2048 - s) >>> 5));
            if ((i3 & (-16777216)) != 0) {
                return 0;
            }
            this.OIIO0O0 = (i4 << 8) | this.OIIO0OO.read();
            this.f1554OIIO00I <<= 8;
            return 0;
        }
        int i5 = i2 - i3;
        this.f1554OIIO00I = i5;
        int i6 = i4 - i3;
        this.OIIO0O0 = i6;
        sArr[i] = (short) (s - (s >>> 5));
        if ((i5 & (-16777216)) != 0) {
            return 1;
        }
        this.OIIO0O0 = (i6 << 8) | this.OIIO0OO.read();
        this.f1554OIIO00I <<= 8;
        return 1;
    }

    public static void OIIO00I(short[] sArr) {
        for (int i = 0; i < sArr.length; i++) {
            sArr[i] = 1024;
        }
    }
}