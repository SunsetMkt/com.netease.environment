package com.netease.ntunisdk.base.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import com.xiaomi.gamecenter.sdk.web.webview.webkit.h;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;

/* loaded from: classes3.dex */
public final class EmulatorDetector {
    private static final String[] EXT_FILES;
    private static final String[] LD_DEFAULT_APK;
    private static final int MIN_PROPERTIES_THRESHOLD = 5;
    private static final String[] NOX_DEFAULT_APK;
    private static final String[] NOX_FILES;
    private static final Property[] PROPERTIES;
    private static final String TAG = "UniSDK EmulatorDetector";
    private static boolean checkEmulatorApk;
    private static boolean installEmulatorApk;
    private static final String[] GENY_FILES = {"/dev/socket/genyd", "/dev/socket/baseband_genyd"};
    private static final String[] QEMU_DRIVERS = {"goldfish"};
    private static final String[] PIPES = {"/dev/socket/qemud", "/dev/qemu_pipe"};
    private static final String[] X86_FILES = {"ueventd.android_x86.rc", "x86.prop", "ueventd.ttVM_x86.rc", "init.ttVM_x86.rc", "fstab.ttVM_x86", "fstab.vbox86", "init.vbox86.rc", "ueventd.vbox86.rc", "init.x86.rc"};
    private static final String[] ANDY_FILES = {"fstab.andy", "ueventd.andy.rc"};
    private static final String[] LD_FILES = {"/system/app/EmuCoreService/EmuCoreService.apk"};

    static {
        String str;
        String[] strArr = new String[6];
        strArr[0] = "fstab.nox";
        strArr[1] = "init.nox.rc";
        strArr[2] = "ueventd.nox.rc";
        String str2 = "/system/bin/nox-prop";
        strArr[3] = "/system/bin/nox-prop";
        if (Environment.getExternalStorageState().equals("mounted")) {
            str = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "BigNoxHD";
        } else {
            str = "/system/bin/nox-prop";
        }
        strArr[4] = str;
        if (Environment.getExternalStorageState().equals("mounted")) {
            str2 = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "BigNoxGameHD";
        }
        strArr[5] = str2;
        NOX_FILES = strArr;
        NOX_DEFAULT_APK = new String[]{"com.vphone.helper", "com.vphone.launcher"};
        LD_DEFAULT_APK = new String[]{"com.android.flysilkworm", "com.android.coreservice", "com.cyanogenmod.filemanager"};
        EXT_FILES = new String[]{"/sys/kernel/debug/x86/"};
        PROPERTIES = new Property[]{new Property("init.svc.qemud", null), new Property("init.svc.qemu-props", null), new Property("qemu.hw.mainkeys", null), new Property("qemu.sf.fake_camera", null), new Property("qemu.sf.lcd_density", null), new Property("ro.bootloader", "unknown"), new Property("ro.bootmode", "unknown"), new Property("ro.hardware", "goldfish"), new Property("ro.kernel.android.qemud", null), new Property("ro.kernel.qemu.gles", null), new Property("ro.kernel.qemu", "1"), new Property("ro.product.device", "generic"), new Property("ro.product.model", ClientLogConstant.SDK), new Property("ro.product.name", ClientLogConstant.SDK)};
    }

    public static boolean detect(Context context) {
        boolean zCheckBasic = checkBasic();
        UniSdkUtils.d(TAG, "Check basic ".concat(String.valueOf(zCheckBasic)));
        if (!zCheckBasic) {
            zCheckBasic = checkAdvanced(context);
            UniSdkUtils.d(TAG, "Check Advanced ".concat(String.valueOf(zCheckBasic)));
        }
        if (zCheckBasic) {
            return zCheckBasic;
        }
        boolean zCheckInstallApk = checkInstallApk(context);
        UniSdkUtils.d(TAG, "Check checkInstallApk ".concat(String.valueOf(zCheckInstallApk)));
        return zCheckInstallApk;
    }

    private static boolean checkBasic() {
        if (Build.FINGERPRINT.startsWith("generic") || Build.MODEL.contains("google_sdk") || Build.MODEL.toLowerCase(Locale.ROOT).contains("droid4x") || Build.MODEL.contains("Emulator") || Build.MODEL.contains("Android SDK built for x86") || Build.MANUFACTURER.contains("Genymotion") || Build.HARDWARE.equals("goldfish") || Build.HARDWARE.equals("vbox86") || Build.PRODUCT.equals(ClientLogConstant.SDK) || Build.PRODUCT.equals("google_sdk") || Build.PRODUCT.equals("sdk_x86") || Build.PRODUCT.equals("vbox86p") || Build.BOARD.toLowerCase(Locale.ROOT).contains("nox") || Build.BOOTLOADER.toLowerCase(Locale.ROOT).contains("nox") || Build.HARDWARE.toLowerCase(Locale.ROOT).contains("nox") || Build.PRODUCT.toLowerCase(Locale.ROOT).contains("nox") || (Build.BRAND.equalsIgnoreCase("windows") && Build.MODEL.toLowerCase(Locale.ROOT).contains("subsystem for android")) || "V417IR".equalsIgnoreCase(Build.ID) || "DS314A".equalsIgnoreCase(Build.ID)) {
            return true;
        }
        if ((Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")) || false) {
            return true;
        }
        return "google_sdk".equals(Build.PRODUCT) | false;
    }

    private static boolean checkAdvanced(Context context) {
        if (checkFiles(GENY_FILES, "Geny") || checkFiles(ANDY_FILES, "Andy") || checkFiles(EXT_FILES, "ext_files(emu)") || checkFiles(NOX_FILES, "Nox") || checkQEmuDrivers() || checkFiles(PIPES, "Pipes") || checkFiles(LD_FILES, "Ld")) {
            return true;
        }
        return checkQEmuProps(context) && checkFiles(X86_FILES, "X86");
    }

    private static boolean checkInstallApk(Context context) {
        if (!checkEmulatorApk) {
            installEmulatorApk = checkDefaultInstallApk(context, NOX_DEFAULT_APK) || checkDefaultInstallApk(context, LD_DEFAULT_APK);
            checkEmulatorApk = true;
        }
        return installEmulatorApk;
    }

    private static boolean checkDefaultInstallApk(Context context, String[] strArr) {
        int length = strArr.length;
        int i = 0;
        boolean z = false;
        while (i < length) {
            try {
                UniSdkUtils.d(TAG, "checkDefaultInstallApk packageInfo:".concat(String.valueOf(context.getPackageManager().getPackageInfo(strArr[i], 0))));
                i++;
                z = true;
            } catch (Exception e) {
                UniSdkUtils.d(TAG, "checkDefaultInstallApk exception:" + e.getMessage());
                return false;
            }
        }
        return z;
    }

    private static boolean checkQEmuDrivers() throws IOException {
        File[] fileArr = {new File("/proc/tty/drivers"), new File("/proc/cpuinfo")};
        for (int i = 0; i < 2; i++) {
            File file = fileArr[i];
            if (file.exists() && file.canRead()) {
                byte[] bArr = new byte[1024];
                try {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    fileInputStream.read(bArr);
                    fileInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String str = new String(bArr);
                for (String str2 : QEMU_DRIVERS) {
                    if (str.contains(str2)) {
                        UniSdkUtils.d(TAG, "Check QEmuDrivers is detected");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean checkFiles(String[] strArr, String str) {
        for (String str2 : strArr) {
            if (new File(str2).exists()) {
                UniSdkUtils.d(TAG, "Check " + str + " is detected");
                return true;
            }
        }
        return false;
    }

    private static boolean checkQEmuProps(Context context) throws ClassNotFoundException {
        int i = 0;
        for (Property property : PROPERTIES) {
            String prop = getProp(context, property.name);
            if (property.seek_value == null && prop != null) {
                i++;
            }
            if (property.seek_value != null && prop != null && prop.contains(property.seek_value)) {
                i++;
            }
        }
        if (i < 5) {
            return false;
        }
        UniSdkUtils.d(TAG, "Check QEmuProps is detected");
        return true;
    }

    private static String getProp(Context context, String str) throws ClassNotFoundException {
        try {
            Class<?> clsLoadClass = context.getClassLoader().loadClass("android.os.SystemProperties");
            return (String) clsLoadClass.getMethod(h.c, String.class).invoke(clsLoadClass, str);
        } catch (Exception unused) {
            return null;
        }
    }
}