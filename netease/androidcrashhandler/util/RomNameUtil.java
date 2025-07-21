package com.netease.androidcrashhandler.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import com.alipay.sdk.m.c.a;
import com.xiaomi.gamecenter.sdk.web.webview.webkit.h;

/* loaded from: classes4.dex */
public final class RomNameUtil {
    private static String ROM_NAME = "";
    private static String TAG = "RomNameUtil";

    private RomNameUtil() {
    }

    public static String getRomName() {
        String str;
        if (!TextUtils.isEmpty(ROM_NAME)) {
            return ROM_NAME;
        }
        try {
            String strSysProperty = sysProperty("ro.build.version.release", "");
            String strSysProperty2 = sysProperty("ro.build.display.id", "");
            if (isHuawei()) {
                str = "HUAWEI";
            } else if (isHonor()) {
                str = "HONOR";
            } else if (isOppo()) {
                str = "OPPO";
            } else if (isRealMe()) {
                str = "REALME";
            } else if (isVivo()) {
                str = "VIVO";
            } else if (isXiaomi() || isRedMi()) {
                str = "MIUI";
            } else if (isBlackShark()) {
                str = "JOYUI";
            } else if (isOnePlus()) {
                str = "ColorOS";
            } else if (isSamsung()) {
                str = "Samsung";
            } else if (isMeizu()) {
                str = "Flyme";
            } else if (isSmartisan()) {
                str = "Smartisan";
            } else if (isLenovo()) {
                str = "Lenovo";
            } else if (isNubia()) {
                str = "Nubia";
            } else if (isZTE()) {
                str = "ZTE";
            } else if (isMotolora()) {
                str = "Motolora";
            } else {
                String str2 = Build.MANUFACTURER;
                String str3 = Build.BRAND;
                str = TextUtils.isEmpty(str2) ? "UNKNOW" : str2;
                if (!TextUtils.isEmpty(str3)) {
                    str = str3;
                }
            }
            ROM_NAME = str + "_" + strSysProperty + "_" + strSysProperty2;
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return ROM_NAME;
    }

    private static String sysProperty(String str, String str2) {
        String str3;
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            str3 = (String) cls.getMethod(h.c, String.class, String.class).invoke(cls, str, str2);
        } catch (Throwable th) {
            LogUtils.e(LogUtils.TAG, "System property invoke error: " + th);
            str3 = null;
        }
        return str3 == null ? "" : str3;
    }

    private static boolean isHuawei() {
        return Build.MANUFACTURER.equalsIgnoreCase("HUAWEI") || Build.BRAND.equalsIgnoreCase("HUAWEI") || !TextUtils.isEmpty(sysProperty(a.f1377a, ""));
    }

    private static boolean isHonor() {
        return Build.BRAND.equalsIgnoreCase("HONOR");
    }

    private static boolean isOppo() {
        return Build.MANUFACTURER.equalsIgnoreCase("OPPO") || Build.BRAND.equalsIgnoreCase("OPPO") || !TextUtils.isEmpty(sysProperty("ro.build.version.opporom", ""));
    }

    private static boolean isRealMe() {
        return Build.BRAND.equalsIgnoreCase("REALME");
    }

    private static boolean isVivo() {
        return Build.MANUFACTURER.equalsIgnoreCase("VIVO") || Build.BRAND.equalsIgnoreCase("VIVO") || !TextUtils.isEmpty(sysProperty("ro.vivo.os.version", ""));
    }

    private static boolean isXiaomi() {
        return Build.MANUFACTURER.equalsIgnoreCase("XIAOMI") || Build.BRAND.equalsIgnoreCase("XIAOMI") || !TextUtils.isEmpty(sysProperty("ro.miui.ui.version.name", ""));
    }

    private static boolean isRedMi() {
        return Build.BRAND.equalsIgnoreCase("REDMI");
    }

    public static boolean isBlackShark() {
        return Build.MANUFACTURER.equalsIgnoreCase("BLACKSHARK") || Build.BRAND.equalsIgnoreCase("BLACKSHARK");
    }

    public static boolean isOnePlus() {
        return Build.MANUFACTURER.equalsIgnoreCase("ONEPLUS") || Build.BRAND.equalsIgnoreCase("ONEPLUS");
    }

    public static boolean isSamsung() {
        return Build.MANUFACTURER.equalsIgnoreCase("SAMSUNG") || Build.BRAND.equalsIgnoreCase("SAMSUNG");
    }

    public static boolean isMeizu() {
        return Build.MANUFACTURER.equalsIgnoreCase("MEIZU") || Build.BRAND.equalsIgnoreCase("MEIZU") || Build.DISPLAY.toUpperCase().contains("FLYME") || !TextUtils.isEmpty(sysProperty("ro.flyme.published", "")) || !TextUtils.isEmpty(sysProperty("ro.meizu.setupwizard.flyme", ""));
    }

    private static boolean isSmartisan() {
        return !TextUtils.isEmpty(sysProperty("ro.smartisan.version", ""));
    }

    public static boolean isLenovo() {
        return Build.MANUFACTURER.equalsIgnoreCase("LENOVO") || Build.BRAND.equalsIgnoreCase("LENOVO") || Build.BRAND.equalsIgnoreCase("ZUK");
    }

    public static boolean isNubia() {
        return Build.MANUFACTURER.equalsIgnoreCase("NUBIA") || Build.BRAND.equalsIgnoreCase("NUBIA");
    }

    public static boolean isASUS() {
        return Build.MANUFACTURER.equalsIgnoreCase("ASUS") || Build.BRAND.equalsIgnoreCase("ASUS");
    }

    public static boolean isZTE() {
        return Build.MANUFACTURER.equalsIgnoreCase("ZTE") || Build.BRAND.equalsIgnoreCase("ZTE");
    }

    public static boolean isMotolora() {
        return Build.MANUFACTURER.equalsIgnoreCase("MOTOLORA") || Build.BRAND.equalsIgnoreCase("MOTOLORA");
    }

    public static boolean isFreeme() {
        return !TextUtils.isEmpty(sysProperty("ro.build.freeme.label", ""));
    }

    public static boolean isCoolpad(Context context) throws PackageManager.NameNotFoundException {
        try {
            context.getPackageManager().getPackageInfo("com.coolpad.deviceidsupport", 0);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean isCoosea() {
        return sysProperty("ro.odm.manufacturer", "").equalsIgnoreCase("PRIZE");
    }
}