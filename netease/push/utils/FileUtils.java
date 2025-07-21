package com.netease.push.utils;

import android.content.Context;
import com.netease.ntunisdk.base.PatchPlaceholder;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes3.dex */
public class FileUtils {
    private static final String PUSH_DIR = "unisdk_push";
    private static final String TAG = "NGPush_" + FileUtils.class.getSimpleName();

    private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, PatchPlaceholder.class.getSimpleName());
    }

    public static boolean write(Context context, String str, String str2) throws IllegalAccessException, IOException, IllegalArgumentException, InvocationTargetException {
        try {
            File file = new File(context.getFilesDir(), PUSH_DIR);
            if (!file.isDirectory()) {
                file.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(new File(file, str), false);
            fileOutputStream.write(str2.getBytes("UTF-8"));
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            PushLog.e(TAG, "write exception:" + e.toString());
            return false;
        }
    }

    public static String read(Context context, String str, String str2) throws IllegalAccessException, IOException, IllegalArgumentException, InvocationTargetException {
        try {
            File file = new File(context.getFilesDir(), PUSH_DIR);
            if (!file.isDirectory()) {
                file.mkdir();
            }
            File file2 = new File(file, str);
            if (!file2.exists()) {
                return str2;
            }
            byte[] bArr = new byte[(int) file2.length()];
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file2));
            bufferedInputStream.read(bArr, 0, bArr.length);
            bufferedInputStream.close();
            return new String(bArr, "UTF-8");
        } catch (Exception e) {
            PushLog.e(TAG, "read exception:" + e.toString());
            return str2;
        }
    }

    public static boolean exists(Context context, String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        File file;
        try {
            file = new File(context.getFilesDir(), PUSH_DIR);
        } catch (Exception e) {
            PushLog.e(TAG, "exists exception:" + e.toString());
        }
        if (file.isDirectory()) {
            return new File(file, str).exists();
        }
        return false;
    }

    public static boolean delete(Context context, String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            File file = new File(context.getFilesDir(), PUSH_DIR);
            if (file.isDirectory()) {
                return new File(file, str).delete();
            }
            return false;
        } catch (Exception e) {
            PushLog.e(TAG, "delete exception:" + e.toString());
            return false;
        }
    }
}