package com.netease.unisdk.ngvoice.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;

/* loaded from: classes6.dex */
public class FileUtil {
    public static File createFile(File file) {
        if (file == null) {
            return null;
        }
        if (file.exists()) {
            return file;
        }
        try {
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (file.createNewFile()) {
            return file;
        }
        return null;
    }

    public static File createFile(File file, String str) {
        return createFile(new File(file.getAbsolutePath() + File.separator + str));
    }

    public static File createDir(File file) {
        if (file == null) {
            return null;
        }
        if (file.exists()) {
            return file;
        }
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file.mkdirs()) {
            return file;
        }
        return null;
    }

    public static String fileMD5(String str) throws Throwable {
        FileInputStream fileInputStream;
        Throwable th;
        DigestInputStream digestInputStream;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            fileInputStream = new FileInputStream(str);
            try {
                digestInputStream = new DigestInputStream(fileInputStream, messageDigest);
                try {
                    while (digestInputStream.read(new byte[262144]) > 0) {
                    }
                    String strByteArrayToHex = byteArrayToHex(digestInputStream.getMessageDigest().digest());
                    try {
                        digestInputStream.close();
                        fileInputStream.close();
                    } catch (Exception unused) {
                    }
                    return strByteArrayToHex;
                } catch (Exception unused2) {
                    try {
                        digestInputStream.close();
                        fileInputStream.close();
                    } catch (Exception unused3) {
                    }
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    try {
                        digestInputStream.close();
                        fileInputStream.close();
                    } catch (Exception unused4) {
                    }
                    throw th;
                }
            } catch (Exception unused5) {
                digestInputStream = null;
            } catch (Throwable th3) {
                th = th3;
                digestInputStream = null;
            }
        } catch (Exception unused6) {
            digestInputStream = null;
            fileInputStream = null;
        } catch (Throwable th4) {
            fileInputStream = null;
            th = th4;
            digestInputStream = null;
        }
    }

    public static String byteArrayToHex(byte[] bArr) {
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

    private void copyInputStreamToFile(InputStream inputStream, File file) throws IOException {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] bArr = new byte[1024];
            while (true) {
                int i = inputStream.read(bArr);
                if (i > 0) {
                    fileOutputStream.write(bArr, 0, i);
                } else {
                    fileOutputStream.close();
                    inputStream.close();
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}