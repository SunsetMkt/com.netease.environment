package com.netease.download.storage;

import android.os.Build;
import android.os.StatFs;
import com.netease.download.util.LogUtil;
import java.io.File;

/* loaded from: classes5.dex */
public class StorageUtil {
    private static final String Tag = "StorageUtil";

    public static int canStore(String str, long j, double d) {
        if (0 != getFreeSpaceSize(str)) {
            return ((double) getFreeSpaceSize(str)) > ((double) j) * d ? 1 : 0;
        }
        return -1;
    }

    public static long getFreeSpaceSize(String str) {
        long blockSize;
        long availableBlocks;
        File file = new File(str);
        if (!file.isDirectory()) {
            file = file.getParentFile();
        }
        if (file != null && !file.exists()) {
            file.mkdirs();
        }
        try {
            StatFs statFs = new StatFs(file.getPath());
            if (isVersionOrGreaterThan(18)) {
                blockSize = statFs.getBlockSizeLong();
                availableBlocks = statFs.getAvailableBlocksLong();
            } else {
                blockSize = statFs.getBlockSize();
                availableBlocks = statFs.getAvailableBlocks();
            }
            return blockSize * availableBlocks;
        } catch (Exception e) {
            LogUtil.w(Tag, "StorageUtil [getFreeSpaceSize] Exception=" + e.toString());
            e.printStackTrace();
            return 0L;
        }
    }

    private static boolean isVersionOrGreaterThan(int i) {
        return Build.VERSION.SDK_INT >= i;
    }
}