package com.netease.ntunisdk.base.utils;

import android.content.Context;
import android.content.res.Resources;
import com.netease.ntunisdk.base.UniSdkUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/* loaded from: classes3.dex */
public class FileUtil {
    private static final String TAG = "UniSDK#FileUtil";

    public static String readAssetsFileAsString(Context context, String str) throws IOException {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getAssets().open(str), "UTF-8"));
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line);
            }
            bufferedReader.close();
        } catch (Exception e) {
            UniSdkUtils.e(TAG, "read <" + str + "> exception : " + e.getMessage());
        }
        return sb.toString();
    }

    public static boolean isAssetsFileExist(Context context, String str) throws IOException {
        try {
            context.getAssets().open(str);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public static String readRawFileAsString(Context context, String str) throws IOException {
        StringBuilder sb = new StringBuilder();
        try {
            Resources resources = context.getResources();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resources.openRawResource(resources.getIdentifier(str, "raw", context.getPackageName())), "UTF-8"));
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line);
            }
            bufferedReader.close();
        } catch (Exception e) {
            UniSdkUtils.e(TAG, "read <" + str + "> exception : " + e.getMessage());
        }
        return sb.toString();
    }

    /* JADX WARN: Not initialized variable reg: 2, insn: 0x0055: MOVE (r0 I:??[OBJECT, ARRAY]) = (r2 I:??[OBJECT, ARRAY]), block:B:24:0x0055 */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0058 A[EXC_TOP_SPLITTER, SYNTHETIC] */
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
            java.lang.String r6 = r2.readLine()     // Catch: java.lang.Exception -> L33 java.lang.Throwable -> L54
            if (r6 == 0) goto L2b
            r5.append(r6)     // Catch: java.lang.Exception -> L33 java.lang.Throwable -> L54
            goto L21
        L2b:
            java.lang.String r0 = r5.toString()     // Catch: java.lang.Exception -> L33 java.lang.Throwable -> L54
        L2f:
            r2.close()     // Catch: java.io.IOException -> L53
            goto L53
        L33:
            r5 = move-exception
            goto L39
        L35:
            r5 = move-exception
            goto L56
        L37:
            r5 = move-exception
            r2 = r0
        L39:
            java.lang.String r6 = "UniSDK#FileUtil"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L54
            java.lang.String r3 = "readFile exception : "
            r1.<init>(r3)     // Catch: java.lang.Throwable -> L54
            java.lang.String r5 = r5.getMessage()     // Catch: java.lang.Throwable -> L54
            r1.append(r5)     // Catch: java.lang.Throwable -> L54
            java.lang.String r5 = r1.toString()     // Catch: java.lang.Throwable -> L54
            com.netease.ntunisdk.base.UniSdkUtils.e(r6, r5)     // Catch: java.lang.Throwable -> L54
            if (r2 == 0) goto L53
            goto L2f
        L53:
            return r0
        L54:
            r5 = move-exception
            r0 = r2
        L56:
            if (r0 == 0) goto L5b
            r0.close()     // Catch: java.io.IOException -> L5b
        L5b:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.utils.FileUtil.readFile(java.lang.String, java.lang.String):java.lang.String");
    }

    public static boolean writeFile(String str, String str2) {
        try {
            File file = new File(str);
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(str2);
            fileWriter.close();
            return file.exists();
        } catch (Exception e) {
            UniSdkUtils.e(TAG, "writeFile exception : " + e.getMessage());
            return false;
        }
    }
}