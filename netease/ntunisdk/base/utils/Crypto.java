package com.netease.ntunisdk.base.utils;

import android.util.Base64;
import com.netease.ntunisdk.base.UniSdkUtils;
import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Locale;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.jose4j.keys.AesKey;
import org.jose4j.mac.MacUtil;

/* loaded from: classes3.dex */
public class Crypto {
    private static final String TAG = "UniSDK_Crypto";

    public static String genAESKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AesKey.ALGORITHM);
            keyGenerator.init(256);
            return Base64.encodeToString(keyGenerator.generateKey().getEncoded(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            return "kPPuJVCnRXFOM3eNhKfiPQF+Sibk/pb6iWwjJ4ngTO4=";
        }
    }

    public static byte[] aesEncrypt(byte[] bArr, String str) throws Exception {
        byte[] bArrDecode = Base64.decode(str, 0);
        byte[] bArr2 = new byte[16];
        new SecureRandom().nextBytes(bArr2);
        SecretKeySpec secretKeySpec = new SecretKeySpec(bArrDecode, AesKey.ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(bArr2);
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        cipher.init(1, secretKeySpec, ivParameterSpec);
        byte[] bArrDoFinal = cipher.doFinal(bArr);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(bArr2);
        byteArrayOutputStream.write(bArrDoFinal);
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] aesDecrypt(byte[] bArr, String str) throws Exception {
        byte[] bArrDecode = Base64.decode(str, 0);
        byte[] bArr2 = new byte[16];
        System.arraycopy(bArr, 0, bArr2, 0, 16);
        SecretKeySpec secretKeySpec = new SecretKeySpec(bArrDecode, AesKey.ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(bArr2);
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        cipher.init(2, secretKeySpec, ivParameterSpec);
        return cipher.doFinal(StrUtil.copyOfRange(bArr, 16, bArr.length));
    }

    public static String rsaEncrypt(String str, String str2) throws Exception {
        return rsaEncrypt(str.getBytes("UTF-8"), str2);
    }

    public static String rsaEncrypt(byte[] bArr, String str) throws Exception {
        PublicKey publicKeyGeneratePublic = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decode(str.replaceAll("(-+BEGIN PUBLIC KEY-+\\r?\\n|-+END PUBLIC KEY-+\\r?\\n?)", ""), 0)));
        Cipher cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding");
        cipher.init(1, publicKeyGeneratePublic);
        return Base64.encodeToString(blockCipher(cipher, bArr, 1), 0);
    }

    public static byte[] rsaDecrypt(String str, String str2) throws Exception {
        PrivateKey privateKeyGeneratePrivate = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(str2, 0)));
        Cipher cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding");
        cipher.init(2, privateKeyGeneratePrivate);
        return blockCipher(cipher, Base64.decode(str, 0), 2);
    }

    public static String rsaSign(byte[] bArr, String str) throws Exception {
        PrivateKey privateKeyGeneratePrivate = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(str, 0)));
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initSign(privateKeyGeneratePrivate);
        signature.update(bArr);
        return Base64.encodeToString(signature.sign(), 0);
    }

    public static boolean rsaVerify(byte[] bArr, String str, String str2) throws Exception {
        PublicKey publicKeyGeneratePublic = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decode(str2, 0)));
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initVerify(publicKeyGeneratePublic);
        signature.update(bArr);
        return signature.verify(Base64.decode(str, 0));
    }

    private static byte[] blockCipher(Cipher cipher, byte[] bArr, int i) throws BadPaddingException, IllegalBlockSizeException {
        int i2 = 0;
        byte[] bArrAppend = new byte[0];
        int blockSize = cipher.getBlockSize();
        int length = bArr.length;
        while (i2 < length) {
            int i3 = i2 + blockSize;
            if (i3 > length) {
                i3 = length;
            }
            bArrAppend = append(bArrAppend, cipher.doFinal(StrUtil.copyOfRange(bArr, i2, i3)));
            i2 = i3;
        }
        return bArrAppend;
    }

    public static byte[] append(byte[] bArr, byte[] bArr2) {
        byte[] bArr3 = new byte[bArr.length + bArr2.length];
        for (int i = 0; i < bArr.length; i++) {
            bArr3[i] = bArr[i];
        }
        for (int i2 = 0; i2 < bArr2.length; i2++) {
            bArr3[bArr.length + i2] = bArr2[i2];
        }
        return bArr3;
    }

    public static String getSignSrc(String str, String str2, String str3) {
        String strReplace = str2.replace("://", "");
        String strSubstring = -1 != strReplace.indexOf("/") ? strReplace.substring(strReplace.indexOf("/")) : "";
        UniSdkUtils.d(TAG, "pathQs:".concat(String.valueOf(strSubstring)));
        return str.toUpperCase(Locale.ROOT) + strSubstring + str3;
    }

    public static String hmacSHA256Signature(String str, String str2) throws Exception {
        Mac mac = Mac.getInstance(MacUtil.HMAC_SHA256);
        mac.init(new SecretKeySpec(str.getBytes(), MacUtil.HMAC_SHA256));
        return byteArrayToHex(mac.doFinal(str2.getBytes()));
    }

    private static String byteArrayToHex(byte[] bArr) {
        char[] cArr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] cArr2 = new char[bArr.length * 2];
        int i = 0;
        for (byte b : bArr) {
            int i2 = i + 1;
            cArr2[i] = cArr[(b >>> 4) & 15];
            i = i2 + 1;
            cArr2[i2] = cArr[b & 15];
        }
        return new String(cArr2);
    }
}