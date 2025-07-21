package com.netease.environment.OIIO0II;

import android.util.Base64;

/* compiled from: Base64Utils.java */
/* loaded from: classes5.dex */
public class OIIO0O0 {
    public static byte[] OIIO00I(byte[] bArr) {
        try {
            return Base64.decode(bArr, 2);
        } catch (Exception e) {
            OIIO.OIIO00I("Base64Utils", e.toString());
            return null;
        }
    }

    public static String OIIO0O0(byte[] bArr) {
        if (bArr != null) {
            try {
                return new String(Base64.encode(bArr, 2));
            } catch (Exception e) {
                OIIO.OIIO00I("Base64Utils", e.toString());
            }
        }
        return null;
    }
}