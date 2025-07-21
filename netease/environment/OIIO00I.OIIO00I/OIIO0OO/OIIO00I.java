package com.netease.environment.OIIO00I.OIIO00I.OIIO0OO;

/* compiled from: BitTreeDecoder.java */
/* loaded from: classes6.dex */
public class OIIO00I {

    /* renamed from: OIIO00I, reason: collision with root package name */
    short[] f1553OIIO00I;
    int OIIO0O0;

    public OIIO00I(int i) {
        this.OIIO0O0 = i;
        this.f1553OIIO00I = new short[1 << i];
    }

    public void OIIO00I() {
        OIIO0O0.OIIO00I(this.f1553OIIO00I);
    }

    public int OIIO0O0(OIIO0O0 oiio0o0) {
        int i = 1;
        int i2 = 0;
        for (int i3 = 0; i3 < this.OIIO0O0; i3++) {
            int iOIIO00I = oiio0o0.OIIO00I(this.f1553OIIO00I, i);
            i = (i << 1) + iOIIO00I;
            i2 |= iOIIO00I << i3;
        }
        return i2;
    }

    public int OIIO00I(OIIO0O0 oiio0o0) {
        int iOIIO00I = 1;
        for (int i = this.OIIO0O0; i != 0; i--) {
            iOIIO00I = oiio0o0.OIIO00I(this.f1553OIIO00I, iOIIO00I) + (iOIIO00I << 1);
        }
        return iOIIO00I - (1 << this.OIIO0O0);
    }

    public static int OIIO00I(short[] sArr, int i, OIIO0O0 oiio0o0, int i2) {
        int i3 = 1;
        int i4 = 0;
        for (int i5 = 0; i5 < i2; i5++) {
            int iOIIO00I = oiio0o0.OIIO00I(sArr, i + i3);
            i3 = (i3 << 1) + iOIIO00I;
            i4 |= iOIIO00I << i5;
        }
        return i4;
    }
}