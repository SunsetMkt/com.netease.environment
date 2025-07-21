package com.netease.cloud.nos.android.utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;

/* loaded from: classes5.dex */
public class FileDigest {
    private static String byteArrayToHex(byte[] bArr) {
        String str = "";
        for (byte b : bArr) {
            str = str + Integer.toString((b & 255) + 256, 16).substring(1);
        }
        return str.toLowerCase();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r5v2 */
    /* JADX WARN: Type inference failed for: r5v9 */
    public static String getFileMD5(File file) throws Throwable {
        FileInputStream fileInputStream;
        Throwable th;
        DigestInputStream digestInputStream;
        MessageDigest messageDigest;
        try {
            try {
                messageDigest = MessageDigest.getInstance("MD5");
                fileInputStream = new FileInputStream(file);
            } catch (Exception e) {
                e = e;
                digestInputStream = null;
                fileInputStream = null;
            } catch (Throwable th2) {
                fileInputStream = null;
                th = th2;
                file = 0;
            }
            try {
                digestInputStream = new DigestInputStream(fileInputStream, messageDigest);
            } catch (Exception e2) {
                e = e2;
                digestInputStream = null;
            } catch (Throwable th3) {
                th = th3;
                file = 0;
                if (file != 0) {
                    try {
                        file.close();
                    } catch (Exception e3) {
                        e3.printStackTrace();
                        throw th;
                    }
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                throw th;
            }
            try {
                while (digestInputStream.read(new byte[524288]) > 0) {
                }
                String strByteArrayToHex = byteArrayToHex(digestInputStream.getMessageDigest().digest());
                try {
                    digestInputStream.close();
                    fileInputStream.close();
                } catch (Exception e4) {
                    e4.printStackTrace();
                }
                return strByteArrayToHex;
            } catch (Exception e5) {
                e = e5;
                e.printStackTrace();
                if (digestInputStream != null) {
                    try {
                        digestInputStream.close();
                    } catch (Exception e6) {
                        e6.printStackTrace();
                        return null;
                    }
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                return null;
            }
        } catch (Throwable th4) {
            th = th4;
        }
    }
}