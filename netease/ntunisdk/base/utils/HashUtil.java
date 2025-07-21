package com.netease.ntunisdk.base.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/* loaded from: classes3.dex */
public class HashUtil {
    private static Map<String, MessageDigest> sDigestAlgorithm;

    public static class Algorithm {
        public static final String MD5 = "MD5";
        public static final String SHA1 = "SHA1";
        public static final String SHA256 = "SHA256";
    }

    static {
        HashMap map = new HashMap();
        sDigestAlgorithm = map;
        try {
            map.put("MD5", MessageDigest.getInstance("MD5"));
            sDigestAlgorithm.put("SHA1", MessageDigest.getInstance("SHA1"));
            sDigestAlgorithm.put("SHA256", MessageDigest.getInstance("SHA256"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String calculateHash(String str, String str2) throws IOException {
        MessageDigest messageDigest;
        int i;
        if (str == null || (messageDigest = sDigestAlgorithm.get(str.toUpperCase(Locale.ROOT))) == null) {
            return null;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(str2);
            byte[] bArr = new byte[1024];
            while (true) {
                int i2 = fileInputStream.read(bArr);
                if (i2 == -1) {
                    break;
                }
                messageDigest.update(bArr, 0, i2);
            }
            byte[] bArrDigest = messageDigest.digest();
            StringBuilder sb = new StringBuilder("");
            for (byte b : bArrDigest) {
                sb.append(Integer.toString((b & 255) + 256, 16).substring(1));
            }
            fileInputStream.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String calculateStrHash(String str, String str2) {
        MessageDigest messageDigest;
        if (str == null || (messageDigest = sDigestAlgorithm.get(str.toUpperCase(Locale.ROOT))) == null) {
            return null;
        }
        byte[] bytes = str2.getBytes();
        messageDigest.update(bytes, 0, bytes.length);
        byte[] bArrDigest = messageDigest.digest();
        StringBuilder sb = new StringBuilder("");
        for (byte b : bArrDigest) {
            sb.append(Integer.toString((b & 255) + 256, 16).substring(1));
        }
        return sb.toString();
    }
}