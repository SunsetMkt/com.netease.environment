package com.netease.environment.OIIO00I.OIIO00I.OIIO0O0;

/* compiled from: Decoder.java */
/* loaded from: classes5.dex */
public class OIIO0O0 {
    int OIIOOI0;

    /* renamed from: OIIO00I, reason: collision with root package name */
    com.netease.environment.OIIO00I.OIIO00I.OIIO00I.OIIO00I f1549OIIO00I = new com.netease.environment.OIIO00I.OIIO00I.OIIO00I.OIIO00I();
    com.netease.environment.OIIO00I.OIIO00I.OIIO0OO.OIIO0O0 OIIO0O0 = new com.netease.environment.OIIO00I.OIIO00I.OIIO0OO.OIIO0O0();
    short[] OIIO0OO = new short[192];
    short[] OIIO0OI = new short[12];
    short[] OIIO0I0 = new short[12];
    short[] OIIO0I = new short[12];
    short[] OIIO0IO = new short[12];
    short[] OIIO0II = new short[192];
    com.netease.environment.OIIO00I.OIIO00I.OIIO0OO.OIIO00I[] OIIO = new com.netease.environment.OIIO00I.OIIO00I.OIIO0OO.OIIO00I[4];
    short[] OIIOO0 = new short[114];
    com.netease.environment.OIIO00I.OIIO00I.OIIO0OO.OIIO00I OIIOO0O = new com.netease.environment.OIIO00I.OIIO00I.OIIO0OO.OIIO00I(4);
    OIIO00I OIIOO0I = new OIIO00I(this);
    OIIO00I OIIOOO0 = new OIIO00I(this);
    C0062OIIO0O0 OIIOOO = new C0062OIIO0O0(this);
    int OIIOOOO = -1;
    int OIIOOOI = -1;

    public OIIO0O0() {
        for (int i = 0; i < 4; i++) {
            this.OIIO[i] = new com.netease.environment.OIIO00I.OIIO00I.OIIO0OO.OIIO00I(6);
        }
    }

    boolean OIIO00I(int i) {
        if (i < 0) {
            return false;
        }
        if (this.OIIOOOO != i) {
            this.OIIOOOO = i;
            int iMax = Math.max(i, 1);
            this.OIIOOOI = iMax;
            this.f1549OIIO00I.OIIO00I(Math.max(iMax, 4096));
        }
        return true;
    }

    /* compiled from: Decoder.java */
    class OIIO00I {

        /* renamed from: OIIO00I, reason: collision with root package name */
        short[] f1550OIIO00I = new short[2];
        com.netease.environment.OIIO00I.OIIO00I.OIIO0OO.OIIO00I[] OIIO0O0 = new com.netease.environment.OIIO00I.OIIO00I.OIIO0OO.OIIO00I[16];
        com.netease.environment.OIIO00I.OIIO00I.OIIO0OO.OIIO00I[] OIIO0OO = new com.netease.environment.OIIO00I.OIIO00I.OIIO0OO.OIIO00I[16];
        com.netease.environment.OIIO00I.OIIO00I.OIIO0OO.OIIO00I OIIO0OI = new com.netease.environment.OIIO00I.OIIO00I.OIIO0OO.OIIO00I(8);
        int OIIO0I0 = 0;

        OIIO00I(OIIO0O0 oiio0o0) {
        }

        public void OIIO00I(int i) {
            while (true) {
                int i2 = this.OIIO0I0;
                if (i2 >= i) {
                    return;
                }
                this.OIIO0O0[i2] = new com.netease.environment.OIIO00I.OIIO00I.OIIO0OO.OIIO00I(3);
                this.OIIO0OO[this.OIIO0I0] = new com.netease.environment.OIIO00I.OIIO00I.OIIO0OO.OIIO00I(3);
                this.OIIO0I0++;
            }
        }

        public void OIIO00I() {
            com.netease.environment.OIIO00I.OIIO00I.OIIO0OO.OIIO0O0.OIIO00I(this.f1550OIIO00I);
            for (int i = 0; i < this.OIIO0I0; i++) {
                this.OIIO0O0[i].OIIO00I();
                this.OIIO0OO[i].OIIO00I();
            }
            this.OIIO0OI.OIIO00I();
        }

        public int OIIO00I(com.netease.environment.OIIO00I.OIIO00I.OIIO0OO.OIIO0O0 oiio0o0, int i) {
            int iOIIO00I;
            if (oiio0o0.OIIO00I(this.f1550OIIO00I, 0) == 0) {
                return this.OIIO0O0[i].OIIO00I(oiio0o0);
            }
            if (oiio0o0.OIIO00I(this.f1550OIIO00I, 1) == 0) {
                iOIIO00I = this.OIIO0OO[i].OIIO00I(oiio0o0);
            } else {
                iOIIO00I = this.OIIO0OI.OIIO00I(oiio0o0) + 8;
            }
            return iOIIO00I + 8;
        }
    }

    boolean OIIO00I(int i, int i2, int i3) {
        if (i > 8 || i2 > 4 || i3 > 4) {
            return false;
        }
        this.OIIOOO.OIIO00I(i2, i);
        int i4 = 1 << i3;
        this.OIIOO0I.OIIO00I(i4);
        this.OIIOOO0.OIIO00I(i4);
        this.OIIOOI0 = i4 - 1;
        return true;
    }

    /* compiled from: Decoder.java */
    /* renamed from: com.netease.environment.OIIO00I.OIIO00I.OIIO0O0.OIIO0O0$OIIO0O0, reason: collision with other inner class name */
    class C0062OIIO0O0 {

        /* renamed from: OIIO00I, reason: collision with root package name */
        OIIO00I[] f1551OIIO00I;
        int OIIO0O0;
        int OIIO0OI;
        int OIIO0OO;

        /* compiled from: Decoder.java */
        /* renamed from: com.netease.environment.OIIO00I.OIIO00I.OIIO0O0.OIIO0O0$OIIO0O0$OIIO00I */
        class OIIO00I {

            /* renamed from: OIIO00I, reason: collision with root package name */
            short[] f1552OIIO00I = new short[768];

            OIIO00I(C0062OIIO0O0 c0062oiio0o0) {
            }

            public void OIIO00I() {
                com.netease.environment.OIIO00I.OIIO00I.OIIO0OO.OIIO0O0.OIIO00I(this.f1552OIIO00I);
            }

            public byte OIIO00I(com.netease.environment.OIIO00I.OIIO00I.OIIO0OO.OIIO0O0 oiio0o0) {
                int iOIIO00I = 1;
                do {
                    iOIIO00I = oiio0o0.OIIO00I(this.f1552OIIO00I, iOIIO00I) | (iOIIO00I << 1);
                } while (iOIIO00I < 256);
                return (byte) iOIIO00I;
            }

            public byte OIIO00I(com.netease.environment.OIIO00I.OIIO00I.OIIO0OO.OIIO0O0 oiio0o0, byte b) {
                int iOIIO00I = 1;
                while (true) {
                    int i = (b >> 7) & 1;
                    b = (byte) (b << 1);
                    int iOIIO00I2 = oiio0o0.OIIO00I(this.f1552OIIO00I, ((i + 1) << 8) + iOIIO00I);
                    iOIIO00I = (iOIIO00I << 1) | iOIIO00I2;
                    if (i != iOIIO00I2) {
                        while (iOIIO00I < 256) {
                            iOIIO00I = (iOIIO00I << 1) | oiio0o0.OIIO00I(this.f1552OIIO00I, iOIIO00I);
                        }
                    } else if (iOIIO00I >= 256) {
                        break;
                    }
                }
                return (byte) iOIIO00I;
            }
        }

        C0062OIIO0O0(OIIO0O0 oiio0o0) {
        }

        public void OIIO00I(int i, int i2) {
            if (this.f1551OIIO00I != null && this.OIIO0O0 == i2 && this.OIIO0OO == i) {
                return;
            }
            this.OIIO0OO = i;
            this.OIIO0OI = (1 << i) - 1;
            this.OIIO0O0 = i2;
            int i3 = 1 << (i2 + i);
            this.f1551OIIO00I = new OIIO00I[i3];
            for (int i4 = 0; i4 < i3; i4++) {
                this.f1551OIIO00I[i4] = new OIIO00I(this);
            }
        }

        public void OIIO00I() {
            int i = 1 << (this.OIIO0O0 + this.OIIO0OO);
            for (int i2 = 0; i2 < i; i2++) {
                this.f1551OIIO00I[i2].OIIO00I();
            }
        }

        OIIO00I OIIO00I(int i, byte b) {
            OIIO00I[] oiio00iArr = this.f1551OIIO00I;
            int i2 = i & this.OIIO0OI;
            int i3 = this.OIIO0O0;
            return oiio00iArr[(i2 << i3) + ((b & 255) >>> (8 - i3))];
        }
    }

    void OIIO00I() {
        this.f1549OIIO00I.OIIO00I(false);
        com.netease.environment.OIIO00I.OIIO00I.OIIO0OO.OIIO0O0.OIIO00I(this.OIIO0OO);
        com.netease.environment.OIIO00I.OIIO00I.OIIO0OO.OIIO0O0.OIIO00I(this.OIIO0II);
        com.netease.environment.OIIO00I.OIIO00I.OIIO0OO.OIIO0O0.OIIO00I(this.OIIO0OI);
        com.netease.environment.OIIO00I.OIIO00I.OIIO0OO.OIIO0O0.OIIO00I(this.OIIO0I0);
        com.netease.environment.OIIO00I.OIIO00I.OIIO0OO.OIIO0O0.OIIO00I(this.OIIO0I);
        com.netease.environment.OIIO00I.OIIO00I.OIIO0OO.OIIO0O0.OIIO00I(this.OIIO0IO);
        com.netease.environment.OIIO00I.OIIO00I.OIIO0OO.OIIO0O0.OIIO00I(this.OIIOO0);
        this.OIIOOO.OIIO00I();
        for (int i = 0; i < 4; i++) {
            this.OIIO[i].OIIO00I();
        }
        this.OIIOO0I.OIIO00I();
        this.OIIOOO0.OIIO00I();
        this.OIIOO0O.OIIO00I();
        this.OIIO0O0.OIIO00I();
    }

    /* JADX WARN: Code restructure failed: missing block: B:43:0x011e, code lost:
    
        r17.f1549OIIO00I.OIIO00I();
        r17.f1549OIIO00I.OIIO0O0();
        r17.OIIO0O0.OIIO0O0();
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x012d, code lost:
    
        return true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean OIIO00I(java.io.InputStream r18, java.io.OutputStream r19, long r20) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 338
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.environment.OIIO00I.OIIO00I.OIIO0O0.OIIO0O0.OIIO00I(java.io.InputStream, java.io.OutputStream, long):boolean");
    }

    public boolean OIIO00I(byte[] bArr) {
        if (bArr.length < 5) {
            return false;
        }
        int i = bArr[0] & 255;
        int i2 = i % 9;
        int i3 = i / 9;
        int i4 = i3 % 5;
        int i5 = i3 / 5;
        int i6 = 0;
        int i7 = 0;
        while (i6 < 4) {
            int i8 = i6 + 1;
            i7 += (bArr[i8] & 255) << (i6 * 8);
            i6 = i8;
        }
        if (OIIO00I(i2, i4, i5)) {
            return OIIO00I(i7);
        }
        return false;
    }
}