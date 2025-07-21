package com.netease.ntunisdk.external.protocol.utils;

import android.content.Context;
import android.os.Build;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/* loaded from: classes.dex */
public class FileUtil {
    private static final String TAG = "U";

    public static String readAssetsFileAsString(Context context, String str) throws IOException {
        BufferedReader bufferedReader;
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader2 = null;
        try {
            try {
                if (Build.VERSION.SDK_INT >= 19) {
                    bufferedReader = new BufferedReader(new InputStreamReader(context.getAssets().open(str), StandardCharsets.UTF_8));
                } else {
                    bufferedReader = new BufferedReader(new InputStreamReader(context.getAssets().open(str), "UTF-8"));
                }
                bufferedReader2 = bufferedReader;
            } catch (Exception e) {
                L.d(TAG, "read <" + str + "> exception : " + e.getMessage());
                sb = new StringBuilder();
                if (bufferedReader2 != null) {
                    break;
                }
            }
            while (true) {
                String line = bufferedReader2.readLine();
                if (line != null) {
                    sb.append(line);
                }
                try {
                    break;
                } catch (Exception unused) {
                }
            }
            bufferedReader2.close();
            return sb.toString();
        } catch (Throwable th) {
            if (bufferedReader2 != null) {
                try {
                    bufferedReader2.close();
                } catch (Exception unused2) {
                }
            }
            throw th;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:106:0x0149 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:129:? A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:96:0x0153 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean copyAssetsFile(android.content.Context r7, java.lang.String r8, java.lang.String r9, java.lang.String r10, boolean r11) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 350
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.external.protocol.utils.FileUtil.copyAssetsFile(android.content.Context, java.lang.String, java.lang.String, java.lang.String, boolean):boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:112:? A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:88:0x00e3 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:96:0x00d9 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:63:0x00d2 -> B:81:0x00d5). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean unzipFile(java.io.File r8) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 238
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.external.protocol.utils.FileUtil.unzipFile(java.io.File):boolean");
    }

    public static boolean isAssetsFileExist(Context context, String str) throws IOException {
        try {
            context.getAssets().open(str);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    /* JADX WARN: Not initialized variable reg: 2, insn: 0x0058: MOVE (r0 I:??[OBJECT, ARRAY]) = (r2 I:??[OBJECT, ARRAY]), block:B:24:0x0058 */
    /* JADX WARN: Removed duplicated region for block: B:35:0x005b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String readFile(java.lang.String r5, java.lang.String r6) throws java.lang.Throwable {
        /*
            r0 = 0
            java.io.File r1 = new java.io.File     // Catch: java.lang.Throwable -> L35 java.lang.Exception -> L37
            r1.<init>(r5)     // Catch: java.lang.Throwable -> L35 java.lang.Exception -> L37
            boolean r5 = r1.exists()     // Catch: java.lang.Throwable -> L35 java.lang.Exception -> L37
            if (r5 != 0) goto Ld
            return r0
        Ld:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L35 java.lang.Exception -> L37
            r5.<init>()     // Catch: java.lang.Throwable -> L35 java.lang.Exception -> L37
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L35 java.lang.Exception -> L37
            java.io.InputStreamReader r3 = new java.io.InputStreamReader     // Catch: java.lang.Throwable -> L35 java.lang.Exception -> L37
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L35 java.lang.Exception -> L37
            r4.<init>(r1)     // Catch: java.lang.Throwable -> L35 java.lang.Exception -> L37
            r3.<init>(r4, r6)     // Catch: java.lang.Throwable -> L35 java.lang.Exception -> L37
            r2.<init>(r3)     // Catch: java.lang.Throwable -> L35 java.lang.Exception -> L37
        L21:
            java.lang.String r6 = r2.readLine()     // Catch: java.lang.Exception -> L33 java.lang.Throwable -> L57
            if (r6 == 0) goto L2b
            r5.append(r6)     // Catch: java.lang.Exception -> L33 java.lang.Throwable -> L57
            goto L21
        L2b:
            java.lang.String r0 = r5.toString()     // Catch: java.lang.Exception -> L33 java.lang.Throwable -> L57
        L2f:
            r2.close()     // Catch: java.io.IOException -> L56
            goto L56
        L33:
            r5 = move-exception
            goto L39
        L35:
            r5 = move-exception
            goto L59
        L37:
            r5 = move-exception
            r2 = r0
        L39:
            java.lang.String r6 = "U"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L57
            r1.<init>()     // Catch: java.lang.Throwable -> L57
            java.lang.String r3 = "readFile exception : "
            r1.append(r3)     // Catch: java.lang.Throwable -> L57
            java.lang.String r5 = r5.getMessage()     // Catch: java.lang.Throwable -> L57
            r1.append(r5)     // Catch: java.lang.Throwable -> L57
            java.lang.String r5 = r1.toString()     // Catch: java.lang.Throwable -> L57
            com.netease.ntunisdk.external.protocol.utils.L.e(r6, r5)     // Catch: java.lang.Throwable -> L57
            if (r2 == 0) goto L56
            goto L2f
        L56:
            return r0
        L57:
            r5 = move-exception
            r0 = r2
        L59:
            if (r0 == 0) goto L5e
            r0.close()     // Catch: java.io.IOException -> L5e
        L5e:
            goto L60
        L5f:
            throw r5
        L60:
            goto L5f
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.external.protocol.utils.FileUtil.readFile(java.lang.String, java.lang.String):java.lang.String");
    }

    public static boolean writeFile(String str, String str2) throws Throwable {
        FileWriter fileWriter = null;
        try {
            try {
                File file = new File(str);
                FileWriter fileWriter2 = new FileWriter(file);
                try {
                    fileWriter2.write(str2);
                    fileWriter2.flush();
                    boolean zExists = file.exists();
                    try {
                        fileWriter2.close();
                    } catch (IOException unused) {
                    }
                    return zExists;
                } catch (Exception e) {
                    e = e;
                    fileWriter = fileWriter2;
                    L.e(TAG, "writeFile exception : " + e.getMessage());
                    if (fileWriter == null) {
                        return false;
                    }
                    try {
                        fileWriter.close();
                        return false;
                    } catch (IOException unused2) {
                        return false;
                    }
                } catch (Throwable th) {
                    th = th;
                    fileWriter = fileWriter2;
                    if (fileWriter != null) {
                        try {
                            fileWriter.close();
                        } catch (IOException unused3) {
                        }
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    public static void deleteFileTree(File file) {
        File[] fileArrListFiles;
        if (file != null && file.exists()) {
            if (file.isDirectory() && (fileArrListFiles = file.listFiles()) != null) {
                for (File file2 : fileArrListFiles) {
                    if (file2.isDirectory()) {
                        deleteFileTree(file2);
                    } else {
                        file2.delete();
                    }
                }
            }
            file.delete();
        }
    }
}