package com.netease.environment.OIIO0II;

import android.content.Context;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/* compiled from: FileUtils.java */
/* loaded from: classes5.dex */
public class OIIO0I0 {

    /* renamed from: OIIO00I, reason: collision with root package name */
    private static final String f1559OIIO00I = "OIIO0I0";

    public static String OIIO00I(Context context) {
        if (context == null) {
            return null;
        }
        return context.getFilesDir() + "/com/netease/environment/file/";
    }

    public static String OIIO0O0(Context context) {
        return OIIO00I(context) + OIIO00I();
    }

    public static String OIIO0OI(Context context) {
        return OIIO0OO(context) + OIIO0O0();
    }

    public static String OIIO0OO(Context context) {
        if (context == null) {
            return null;
        }
        return context.getFilesDir() + "/com/netease/environment/temp/";
    }

    public static String OIIO00I() {
        return com.netease.environment.OIIO0OO.OIIO0OI.OIIO0I() + "_regex.txt";
    }

    public static String OIIO0O0() {
        return com.netease.environment.OIIO0OO.OIIO0OI.OIIO0I() + "_temp.txt";
    }

    public static byte[] OIIO00I(String str) throws IOException {
        byte[] bArr = null;
        if (str != null && !str.isEmpty()) {
            File file = new File(str);
            if (file.exists() && !file.isDirectory()) {
                bArr = new byte[(int) file.length()];
                try {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    try {
                        fileInputStream.read(bArr);
                        fileInputStream.close();
                    } catch (Throwable th) {
                        try {
                            fileInputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                } catch (FileNotFoundException unused) {
                    OIIO.OIIO0O0(f1559OIIO00I, "The file doesn't not exist.");
                } catch (IOException unused2) {
                    OIIO.OIIO0O0(f1559OIIO00I, "read file failed : IOException");
                } catch (Exception unused3) {
                    OIIO.OIIO0O0(f1559OIIO00I, "read file failed : Exception");
                }
            } else {
                OIIO.OIIO0O0(f1559OIIO00I, "The file doesn't not exist:" + str);
            }
        }
        return bArr;
    }

    public static String OIIO0O0(String str) throws IOException {
        String str2 = "";
        if (str != null && !str.isEmpty()) {
            File file = new File(str);
            if (file.exists() && !file.isDirectory()) {
                try {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                    while (true) {
                        String line = bufferedReader.readLine();
                        if (line == null) {
                            break;
                        }
                        str2 = str2 + line;
                    }
                    fileInputStream.close();
                } catch (FileNotFoundException unused) {
                    OIIO.OIIO0O0(f1559OIIO00I, "The file doesn't not exist.");
                } catch (IOException unused2) {
                    OIIO.OIIO0O0(f1559OIIO00I, "read file failed : IOException");
                } catch (Exception unused3) {
                    OIIO.OIIO0O0(f1559OIIO00I, "read file failed : Exception");
                }
            } else {
                OIIO.OIIO0O0(f1559OIIO00I, "The file doesn't not exist:" + str);
            }
        }
        return str2;
    }

    public static boolean OIIO00I(String str, String str2) throws Throwable {
        IOException e;
        FileWriter fileWriter = null;
        try {
            try {
                File file = new File(str);
                if (file.getParentFile() != null && !file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                FileWriter fileWriter2 = new FileWriter(file);
                try {
                    fileWriter2.write(str2);
                    fileWriter2.flush();
                    try {
                        fileWriter2.close();
                        return true;
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        return true;
                    }
                } catch (IOException e3) {
                    e = e3;
                    fileWriter = fileWriter2;
                    e.printStackTrace();
                    if (fileWriter == null) {
                        return false;
                    }
                    try {
                        fileWriter.close();
                        return false;
                    } catch (IOException e4) {
                        e4.printStackTrace();
                        return false;
                    }
                } catch (Throwable th) {
                    fileWriter = fileWriter2;
                    th = th;
                    if (fileWriter != null) {
                        try {
                            fileWriter.close();
                        } catch (IOException e5) {
                            e5.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (IOException e6) {
                e = e6;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }
}