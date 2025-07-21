package com.netease.mpay.ps.codescanner.widget;

import android.text.TextUtils;
import com.google.zxing.pdf417.PDF417Common;
import com.netease.mpay.ps.codescanner.utils.DataUtils;
import com.netease.ntunisdk.external.protocol.view.AlerterEx;
import com.netease.push.proto.ProtoClientWrapper;
import com.xiaomi.hy.dj.config.ResultCode;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import kotlin.jvm.internal.ByteCompanionObject;
import org.jose4j.jwx.HeaderParameterNames;
import org.jose4j.keys.AesKey;

/* loaded from: classes5.dex */
public class Crypto {

    /* renamed from: a, reason: collision with root package name */
    private static volatile byte[] f1599a;
    private static volatile byte[] b;
    private static volatile byte[] c;
    private static volatile String d;
    private static volatile String e;

    private static String a(int i) {
        String[] strArr = {AlerterEx.TAG, "cbc", "ing", "CS", "a/", "b/", "c/", "d/", "e/", "CB", "EC", "s/", "pk", "add", "sub", String.valueOf(new Random().nextInt(100))};
        if (i == 0) {
            if (d == null) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(strArr[0]);
                stringBuffer.append(strArr[11]);
                stringBuffer.append(strArr[9]);
                stringBuffer.append(strArr[6]);
                stringBuffer.append(strArr[12]);
                stringBuffer.append(strArr[3]);
                StringBuffer stringBuffer2 = new StringBuffer(stringBuffer.toString().toUpperCase());
                stringBuffer2.append(5);
                stringBuffer2.append(strArr[12].substring(0, 1).toUpperCase());
                stringBuffer2.append(strArr[13]);
                stringBuffer2.append(strArr[2]);
                d = stringBuffer2.toString();
            }
            return d;
        }
        if (i != 1) {
            return null;
        }
        if (e == null) {
            StringBuffer stringBuffer3 = new StringBuffer();
            stringBuffer3.append(strArr[0]);
            stringBuffer3.append(strArr[11]);
            stringBuffer3.append(strArr[10]);
            stringBuffer3.append(strArr[5]);
            stringBuffer3.append(strArr[12]);
            stringBuffer3.append(strArr[3]);
            StringBuffer stringBuffer4 = new StringBuffer(stringBuffer3.toString().toUpperCase());
            stringBuffer4.append(7);
            stringBuffer4.append(strArr[12].substring(0, 1).toUpperCase());
            stringBuffer4.append(strArr[13]);
            stringBuffer4.append(strArr[2]);
            e = stringBuffer4.toString();
        }
        return e;
    }

    private static byte[] a() {
        if (c != null) {
            return f1599a;
        }
        try {
            f1599a = ("viewin.out").substring(32).getBytes();
        } catch (Throwable unused) {
            f1599a = null;
        }
        return f1599a;
    }

    private static byte[] a(byte[] bArr, byte[] bArr2, byte[] bArr3, String str) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(bArr, AesKey.ALGORITHM);
            Cipher cipher = Cipher.getInstance(str);
            if (bArr3 == null) {
                cipher.init(1, secretKeySpec);
            } else {
                cipher.init(1, secretKeySpec, new IvParameterSpec(bArr3));
            }
            return cipher.doFinal(bArr2);
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private static byte[] b() {
        if (c != null) {
            return c;
        }
        StringBuffer stringBuffer = new StringBuffer("page;1a8b");
        stringBuffer.append(stringBuffer.indexOf(HeaderParameterNames.INITIALIZATION_VECTOR) > 0 ? "2192" : "3292");
        stringBuffer.append(stringBuffer.indexOf("pt") <= 0 ? "l1w0" : "pt");
        stringBuffer.append(stringBuffer.length() > 15 ? "8fe2;view-" : "view");
        stringBuffer.append(new Random().nextLong());
        byte[] bytes = stringBuffer.toString().split(";")[1].getBytes();
        c = bytes;
        return bytes;
    }

    private static byte[] b(byte[] bArr, byte[] bArr2, byte[] bArr3, String str) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(bArr, AesKey.ALGORITHM);
            Cipher cipher = Cipher.getInstance(str);
            if (bArr3 == null) {
                cipher.init(2, secretKeySpec);
            } else {
                cipher.init(2, secretKeySpec, new IvParameterSpec(bArr3));
            }
            return cipher.doFinal(bArr2);
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private static byte[] c() {
        if (b != null) {
            return b;
        }
        byte[] bArr = new byte[16];
        byte[] bArr2 = {115, 107, 99, 97, 110, 108, 100, ProtoClientWrapper.RESET_TYPE, 100, 49, 104, 110, 101, 115, 107, 59};
        byte[] bArr3 = new byte[8];
        new Random().nextBytes(bArr3);
        byte[] bArr4 = {0, 0, 0, 0, 0, 0, 0, 0, (byte) (((byte) "sdk1".charAt(0)) - bArr2[0]), (byte) (((byte) "sdk1".charAt(1)) - bArr2[8]), (byte) (((byte) "sdk1".charAt(2)) - bArr2[1]), (byte) (((byte) "sdk1".charAt(3)) - bArr2[9]), (byte) (((byte) "channel".charAt(0)) - bArr2[2]), (byte) (((byte) "channel".charAt(1)) - bArr2[10]), (byte) (((byte) "channel".charAt(2)) - bArr2[3]), (byte) (((byte) "channel".charAt(3)) - bArr2[11]), (byte) (((byte) "channel".charAt(4)) - bArr2[4]), (byte) (((byte) "channel".charAt(5)) - bArr2[12]), (byte) (((byte) "channel".charAt(6)) - bArr2[5]), (byte) (((byte) "sdk3".charAt(0)) - bArr2[13]), (byte) (((byte) "sdk3".charAt(1)) - bArr2[6]), (byte) (((byte) "sdk3".charAt(2)) - bArr2[14]), (byte) (((byte) "sdk3".charAt(3)) - bArr2[7]), (byte) (((byte) ";".charAt(0)) - bArr2[15]), (byte) (((byte) "sdk1".charAt(0)) - bArr2[8]), (byte) (((byte) "channel".charAt(1)) - bArr2[7]), (byte) (((byte) "sdk3".charAt(2)) - bArr2[9]), (byte) (((byte) "channel".charAt(3)) - bArr2[15])};
        System.arraycopy(bArr3, 0, bArr4, 0, 8);
        System.arraycopy(bArr4, 8, bArr, 0, 16);
        b = bArr;
        return bArr;
    }

    public static byte[] decrypt(byte[] bArr, String str) {
        if (bArr == null || TextUtils.isEmpty(str)) {
            return null;
        }
        return b(getKey(str), bArr, c(), a(0));
    }

    public static byte[] decrypt2(byte[] bArr, String str) {
        if (bArr == null || TextUtils.isEmpty(str)) {
            return null;
        }
        return b(getKey(str), bArr, b(), a(0));
    }

    public static byte[] encrypt(byte[] bArr, String str) {
        if (bArr == null || TextUtils.isEmpty(str)) {
            return null;
        }
        return a(getKey(str), bArr, c(), a(0));
    }

    public static byte[] encrypt2(byte[] bArr, String str) {
        if (bArr == null || TextUtils.isEmpty(str)) {
            return null;
        }
        return a(getKey(str), bArr, b(), a(0));
    }

    public static byte[] getKey(String str) {
        Random random = new Random();
        int[] iArr = {random.nextInt(1000), PDF417Common.MAX_CODEWORDS_IN_BARCODE, ResultCode.REPOR_QQWAP_SUCCESS, random.nextInt(100), 103};
        byte[] bArrUnhexlify = DataUtils.unhexlify(str);
        byte b2 = ByteCompanionObject.MAX_VALUE;
        for (int i = 0; i < bArrUnhexlify.length; i++) {
            bArrUnhexlify[i] = (byte) (b2 ^ (bArrUnhexlify[i] * (((iArr[1] << 16) | (iArr[2] << 8)) | iArr[4])));
            b2 = bArrUnhexlify[i];
        }
        return bArrUnhexlify;
    }

    public static byte[] ursDec(byte[] bArr, byte[] bArr2) {
        if (bArr == null || bArr2 == null) {
            return null;
        }
        return b(bArr2, bArr, a(), a(1));
    }

    public static byte[] ursEnc(byte[] bArr, byte[] bArr2) {
        if (bArr == null || bArr2 == null) {
            return null;
        }
        return a(bArr2, bArr, a(), a(1));
    }
}