package com.netease.unisdk.ngvoice.utils;

import android.content.Context;
import android.os.Environment;
import java.io.File;

/* loaded from: classes6.dex */
public class StorageUtil {
    public static boolean isSDCardAvailable() {
        String externalStorageState = Environment.getExternalStorageState();
        return externalStorageState != null && externalStorageState.equals("mounted");
    }

    public static long getUsableSpace(File file) {
        if (file == null) {
            return -1L;
        }
        return file.getUsableSpace();
    }

    public static File getExternalFileDir(Context context) {
        File externalFilesDir = context.getExternalFilesDir("");
        if (externalFilesDir != null) {
            return externalFilesDir;
        }
        return new File(Environment.getExternalStorageDirectory().getPath() + ("/Android/data/" + context.getPackageName() + "/files/"));
    }
}