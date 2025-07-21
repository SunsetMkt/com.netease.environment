package com.netease.androidcrashhandler.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.CRC32;

/* loaded from: classes4.dex */
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
    /* JADX WARN: Type inference failed for: r1v0 */
    /* JADX WARN: Type inference failed for: r1v1 */
    /* JADX WARN: Type inference failed for: r1v13, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r1v17 */
    /* JADX WARN: Type inference failed for: r1v18 */
    /* JADX WARN: Type inference failed for: r1v19 */
    /* JADX WARN: Type inference failed for: r1v2, types: [java.io.FileInputStream] */
    /* JADX WARN: Type inference failed for: r1v4 */
    /* JADX WARN: Type inference failed for: r1v5 */
    /* JADX WARN: Type inference failed for: r2v1, types: [java.lang.StringBuilder] */
    public static synchronized String calculateFileHash(String str, String str2) {
        FileInputStream fileInputStream;
        int i;
        int i2;
        int i3;
        ?? r1 = 0;
        FileInputStream fileInputStream2 = null;
        if (str == null) {
            return null;
        }
        MessageDigest messageDigest = sDigestAlgorithm.get(str.toUpperCase());
        if (messageDigest == null) {
            return null;
        }
        ?? sb = new StringBuilder("");
        try {
            try {
                fileInputStream = new FileInputStream(str2);
                try {
                    byte[] bArr = new byte[32768];
                    while (true) {
                        i = fileInputStream.read(bArr);
                        i2 = 0;
                        if (i == -1) {
                            break;
                        }
                        messageDigest.update(bArr, 0, i);
                    }
                    byte[] bArrDigest = messageDigest.digest();
                    int length = bArrDigest.length;
                    i3 = i;
                    while (i2 < length) {
                        ?? Substring = Integer.toString((bArrDigest[i2] & 255) + 256, 16).substring(1);
                        sb.append(Substring);
                        i2++;
                        i3 = Substring;
                    }
                } catch (IOException e) {
                    e = e;
                    fileInputStream2 = fileInputStream;
                    e.printStackTrace();
                    r1 = fileInputStream2;
                    if (fileInputStream2 != null) {
                        try {
                            fileInputStream2.close();
                            r1 = fileInputStream2;
                        } catch (IOException e2) {
                            e = e2;
                            e.printStackTrace();
                            return sb.toString();
                        }
                    }
                    return sb.toString();
                } catch (Throwable th) {
                    th = th;
                    r1 = fileInputStream;
                    if (r1 != 0) {
                        try {
                            r1.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (IOException e4) {
                e = e4;
            }
            try {
                fileInputStream.close();
                r1 = i3;
            } catch (IOException e5) {
                e = e5;
                e.printStackTrace();
                return sb.toString();
            }
            return sb.toString();
        } catch (Throwable th2) {
            th = th2;
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