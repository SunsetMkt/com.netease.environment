package com.netease.environment.OIIO0II;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/* compiled from: CryptoUtil.java */
/* loaded from: classes5.dex */
public class OIIO0OI {
    public static String OIIO0O0(byte[] bArr, String str) throws BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        long jCurrentTimeMillis = System.currentTimeMillis();
        byte[] bArrOIIO00I = OIIO0O0.OIIO00I(bArr);
        OIIO.OIIO0O0("EncryptUtils", "base64 decode\u8017\u65f6:" + (System.currentTimeMillis() - jCurrentTimeMillis) + "ms");
        long jCurrentTimeMillis2 = System.currentTimeMillis();
        byte[] bArrOIIO00I2 = OIIO00I.OIIO00I(bArrOIIO00I, str);
        OIIO.OIIO0O0("EncryptUtils", "decrypt\u8017\u65f6:" + (System.currentTimeMillis() - jCurrentTimeMillis2) + "ms");
        try {
            long jCurrentTimeMillis3 = System.currentTimeMillis();
            byte[] bArrOIIO00I3 = OIIO00I(bArrOIIO00I2);
            OIIO.OIIO0O0("EncryptUtils", "lzma decode\u8017\u65f6:" + (System.currentTimeMillis() - jCurrentTimeMillis3) + "ms");
            return new String(bArrOIIO00I3, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] OIIO00I(byte[] bArr) throws IOException {
        byte[] byteArray = new byte[0];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            com.netease.environment.OIIO00I.OIIO00I.OIIO0O0.OIIO0O0 oiio0o0 = new com.netease.environment.OIIO00I.OIIO00I.OIIO0O0.OIIO0O0();
            byte[] bArr2 = new byte[5];
            byteArrayInputStream.read(bArr2, 0, 5);
            oiio0o0.OIIO00I(bArr2);
            long j = 0;
            for (int i = 0; i < 8; i++) {
                j |= byteArrayInputStream.read() << (i * 8);
            }
            oiio0o0.OIIO00I(byteArrayInputStream, byteArrayOutputStream, j);
            byteArray = byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            byteArrayInputStream.close();
            byteArrayOutputStream.close();
        }
        return byteArray;
    }

    public static String OIIO00I(byte[] bArr, String str) throws BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        if (bArr != null && bArr.length != 0) {
            long jCurrentTimeMillis = System.currentTimeMillis();
            byte[] bArrOIIO00I = OIIO0O0.OIIO00I(bArr);
            OIIO.OIIO0O0("EncryptUtils", "base64 decode\u8017\u65f6:" + (System.currentTimeMillis() - jCurrentTimeMillis) + "ms");
            long jCurrentTimeMillis2 = System.currentTimeMillis();
            byte[] bArrOIIO00I2 = OIIO00I.OIIO00I(bArrOIIO00I, str);
            OIIO.OIIO0O0("EncryptUtils", "decrypt\u8017\u65f6:" + (System.currentTimeMillis() - jCurrentTimeMillis2) + "ms");
            try {
                return new String(bArrOIIO00I2, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}