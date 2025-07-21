package com.netease.ntunisdk.unilogger.global;

import android.content.Context;
import com.netease.ntunisdk.unilogger.utils.LogUtils;
import com.netease.ntunisdk.unilogger.utils.Utils;
import java.io.File;

/* loaded from: classes6.dex */
public class GlobalPrarm {
    public static String EB = null;
    public static final String UNILOGGER_DIR = "unilogger";
    public static final String UNILOGGER_DIR_FILES = "files";
    public static String channelId;
    public static String channelVersion;
    public static String gameId;
    public static String localIp;
    public static String model;
    public static String osVer;
    public static String packageName;
    public static String privateDirPath;
    public static String realGameId;
    public static String region;
    public static String roleId;
    public static String sdkVersion;
    public static String udid;
    public static String uid;
    public static String uniLoggerDirPath;
    public static String uniLoggerLogDirPath;
    public static String unisdkVersion;

    public static void init(Context context) {
        privateDirPath = context.getFilesDir().getAbsolutePath();
        uniLoggerDirPath = privateDirPath + File.separator + UNILOGGER_DIR;
        uniLoggerLogDirPath = uniLoggerDirPath + File.separator + UNILOGGER_DIR_FILES;
        File file = new File(uniLoggerDirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(uniLoggerLogDirPath);
        if (!file2.exists()) {
            file2.mkdirs();
        }
        getInfo(context);
    }

    private static void getInfo(Context context) {
        packageName = Utils.getPackageName(context);
        model = Utils.getModel().trim();
        osVer = Utils.getOsVersion();
        sdkVersion = "1.0.0";
        EB = Utils.getEBFromAssetsFile(context);
        LogUtils.i_inner("GlobalPrarm [getInfo] packageName=" + packageName + ", model=" + model + ", osVer=" + osVer + ", sdkVersion=" + sdkVersion);
    }
}