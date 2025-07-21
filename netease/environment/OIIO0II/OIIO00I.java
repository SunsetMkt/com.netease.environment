package com.netease.environment.OIIO0II;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.jose4j.keys.AesKey;

/* compiled from: AESEUtils.java */
/* loaded from: classes5.dex */
public class OIIO00I {
    public static String OIIO00I(String str) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        try {
            byte[] bytes = com.netease.environment.OIIO0OO.OIIO0OI.OIIOO0().getBytes();
            OIIO.OIIO0O0("AESEUtils", "encryptKey: " + com.netease.environment.OIIO0OO.OIIO0OI.OIIOO0());
            OIIO.OIIO0O0("AESEUtils", "encryptKey \u957f\u5ea6\u662f: " + bytes.length);
            SecretKeySpec secretKeySpec = new SecretKeySpec(bytes, AesKey.ALGORITHM);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(1, secretKeySpec, new IvParameterSpec(bytes));
            OIIO.OIIO0O0("AESEUtils", "content: " + str);
            byte[] bytes2 = str.getBytes();
            OIIO.OIIO0O0("AESEUtils", "content \u957f\u5ea6\u662f: " + bytes2.length);
            str = OIIO0O0.OIIO0O0(cipher.doFinal(bytes2));
            OIIO.OIIO0O0("AESEUtils", "base64: " + str);
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            OIIO.OIIO00I("AESEUtils", e.toString());
            return str;
        }
    }

    public static byte[] OIIO0O0(String str) {
        int length = str.length();
        byte[] bArr = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            bArr[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
        }
        return bArr;
    }

    public static String OIIO0OO(String str) {
        if (str == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            sb.append(Integer.toHexString(c));
        }
        return sb.toString();
    }

    public static byte[] OIIO00I(byte[] bArr, String str) throws BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        OIIO.OIIO0O0("AESEUtils", "hexKey:" + str);
        try {
            String str2 = OIIO0OO(str) + OIIO0OO(str);
            byte[] bArrOIIO0O0 = OIIO0O0(str2);
            OIIO.OIIO0O0("AESEUtils", "encryptKey: " + str2);
            OIIO.OIIO0O0("AESEUtils", "encryptKey \u957f\u5ea6\u662f: " + bArrOIIO0O0.length);
            SecretKeySpec secretKeySpec = new SecretKeySpec(bArrOIIO0O0, AesKey.ALGORITHM);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(2, secretKeySpec, new IvParameterSpec(Arrays.copyOfRange(bArr, 0, 16)));
            byte[] bArrCopyOfRange = Arrays.copyOfRange(bArr, 16, bArr.length);
            OIIO.OIIO0O0("AESEUtils", "inputData \u957f\u5ea6\u662f: " + bArrCopyOfRange.length);
            byte[] bArrDoFinal = cipher.doFinal(bArrCopyOfRange);
            OIIO.OIIO0O0("AESEUtils", "decrypted \u957f\u5ea6\u662f: " + bArrDoFinal.length);
            return bArrDoFinal;
        } catch (Exception e) {
            e.printStackTrace();
            OIIO.OIIO00I("AESEUtils", e.toString());
            return new byte[0];
        }
    }

    public static byte[] OIIO00I(String str, String str2) throws BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        byte[] bArr = new byte[0];
        try {
            OIIO.OIIO0O0("AESEUtils", "hexKey: " + str2);
            String str3 = OIIO0OO(str2) + OIIO0OO(str2);
            byte[] bArrOIIO0O0 = OIIO0O0(str3);
            OIIO.OIIO0O0("AESEUtils", "encryptKey: " + str3);
            OIIO.OIIO0O0("AESEUtils", "encryptKey \u957f\u5ea6\u662f: " + bArrOIIO0O0.length);
            SecretKeySpec secretKeySpec = new SecretKeySpec(bArrOIIO0O0, AesKey.ALGORITHM);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] bytes = com.netease.environment.OIIO0OO.OIIO0OI.OIIOO0().getBytes();
            cipher.init(1, secretKeySpec, new IvParameterSpec(bytes));
            OIIO.OIIO0O0("AESEUtils", "content: " + str);
            byte[] bytes2 = str.getBytes();
            OIIO.OIIO0O0("AESEUtils", "content \u957f\u5ea6\u662f: " + bytes2.length);
            byte[] bArrDoFinal = cipher.doFinal(bytes2);
            bArr = new byte[bytes.length + bArrDoFinal.length];
            System.arraycopy(bytes, 0, bArr, 0, bytes.length);
            System.arraycopy(bArrDoFinal, 0, bArr, bytes.length, bArrDoFinal.length);
            return bArr;
        } catch (Exception e) {
            e.printStackTrace();
            OIIO.OIIO00I("AESEUtils", e.toString());
            return bArr;
        }
    }
}