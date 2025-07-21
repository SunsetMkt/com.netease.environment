package com.netease.environment.OIIO00I.OIIO00I.OIIO00I;

import java.io.IOException;
import java.io.OutputStream;

/* compiled from: OutWindow.java */
/* loaded from: classes6.dex */
public class OIIO00I {

    /* renamed from: OIIO00I, reason: collision with root package name */
    byte[] f1548OIIO00I;
    OutputStream OIIO0I0;
    int OIIO0O0;
    int OIIO0OI;
    int OIIO0OO = 0;

    public void OIIO00I(int i) {
        if (this.f1548OIIO00I == null || this.OIIO0OO != i) {
            this.f1548OIIO00I = new byte[i];
        }
        this.OIIO0OO = i;
        this.OIIO0O0 = 0;
        this.OIIO0OI = 0;
    }

    public void OIIO0O0() throws IOException {
        OIIO00I();
        this.OIIO0I0 = null;
    }

    public byte OIIO0O0(int i) {
        int i2 = (this.OIIO0O0 - i) - 1;
        if (i2 < 0) {
            i2 += this.OIIO0OO;
        }
        return this.f1548OIIO00I[i2];
    }

    public void OIIO00I(OutputStream outputStream) throws IOException {
        OIIO0O0();
        this.OIIO0I0 = outputStream;
    }

    public void OIIO00I(boolean z) {
        if (z) {
            return;
        }
        this.OIIO0OI = 0;
        this.OIIO0O0 = 0;
    }

    public void OIIO00I() throws IOException {
        int i = this.OIIO0O0;
        int i2 = this.OIIO0OI;
        int i3 = i - i2;
        if (i3 == 0) {
            return;
        }
        this.OIIO0I0.write(this.f1548OIIO00I, i2, i3);
        if (this.OIIO0O0 >= this.OIIO0OO) {
            this.OIIO0O0 = 0;
        }
        this.OIIO0OI = this.OIIO0O0;
    }

    public void OIIO00I(int i, int i2) throws IOException {
        int i3 = (this.OIIO0O0 - i) - 1;
        if (i3 < 0) {
            i3 += this.OIIO0OO;
        }
        while (i2 != 0) {
            int i4 = this.OIIO0OO;
            if (i3 >= i4) {
                i3 = 0;
            }
            byte[] bArr = this.f1548OIIO00I;
            int i5 = this.OIIO0O0;
            int i6 = i5 + 1;
            this.OIIO0O0 = i6;
            int i7 = i3 + 1;
            bArr[i5] = bArr[i3];
            if (i6 >= i4) {
                OIIO00I();
            }
            i2--;
            i3 = i7;
        }
    }

    public void OIIO00I(byte b) throws IOException {
        byte[] bArr = this.f1548OIIO00I;
        int i = this.OIIO0O0;
        int i2 = i + 1;
        this.OIIO0O0 = i2;
        bArr[i] = b;
        if (i2 >= this.OIIO0OO) {
            OIIO00I();
        }
    }
}