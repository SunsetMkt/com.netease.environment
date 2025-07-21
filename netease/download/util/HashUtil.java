package com.netease.download.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.CRC32;

/* loaded from: classes5.dex */
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

    public static int getCrc(String... strArr) {
        if (strArr == null) {
            return 0;
        }
        CRC32 crc32 = new CRC32();
        for (String str : strArr) {
            crc32.update(str.getBytes());
        }
        return (int) crc32.getValue();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v0, types: [java.util.Map, java.util.Map<java.lang.String, java.security.MessageDigest>] */
    public static synchronized String calculateFileHash(String str, String str2) {
        FileInputStream fileInputStream;
        int i;
        FileInputStream fileInputStream2 = null;
        if (str == null) {
            return null;
        }
        ?? r2 = sDigestAlgorithm;
        MessageDigest messageDigest = (MessageDigest) r2.get(str.toUpperCase());
        try {
            if (messageDigest == null) {
                return null;
            }
            try {
                fileInputStream = new FileInputStream(str2);
            } catch (IOException e) {
                e = e;
                fileInputStream = null;
            } catch (Throwable th) {
                th = th;
                if (fileInputStream2 != null) {
                    try {
                        fileInputStream2.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                throw th;
            }
            try {
                byte[] bArr = new byte[32768];
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
                String string = sb.toString();
                try {
                    fileInputStream.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
                return string;
            } catch (IOException e4) {
                e = e4;
                e.printStackTrace();
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e5) {
                        e5.printStackTrace();
                    }
                }
                return null;
            }
        } catch (Throwable th2) {
            th = th2;
            fileInputStream2 = r2;
        }
    }

    public static synchronized String calculateFileHash(String str, byte[] bArr) {
        if (str == null) {
            return null;
        }
        MessageDigest messageDigest = sDigestAlgorithm.get(str.toUpperCase());
        if (messageDigest == null) {
            return null;
        }
        messageDigest.update(bArr);
        byte[] bArrDigest = messageDigest.digest();
        StringBuilder sb = new StringBuilder("");
        for (byte b : bArrDigest) {
            sb.append(Integer.toString((b & 255) + 256, 16).substring(1));
        }
        return sb.toString();
    }

    public static String calculateStrHash(String str) {
        try {
            MessageDigest messageDigest = sDigestAlgorithm.get("MD5");
            messageDigest.update(str.getBytes());
            return new BigInteger(1, messageDigest.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}