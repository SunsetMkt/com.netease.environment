package com.netease.androidcrashhandler.util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import com.netease.androidcrashhandler.util.ShellAdbUtils;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import com.xiaomi.gamecenter.sdk.web.webview.webkit.h;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/* loaded from: classes4.dex */
public class CEmulatorDetector {
    private static final int MIN_PROPERTIES_THRESHOLD = 5;
    private static final String[] NOX_FILES;
    private static final String[] PIPES;
    private static final Property[] PROPERTIES;
    private static final String[] QEMU_DRIVERS;
    public static final String TAG = "CEmulatorDetector";
    private static final String[] X86_FILES;
    private static final String[] GENY_FILES = {"/dev/socket/genyd", "/dev/socket/baseband_genyd"};
    private static final String[] ANDY_FILES = {"fstab.andy", "ueventd.andy.rc"};
    private static final String[] EXT_FILES = {"/sys/kernel/debug/x86/"};

    static {
        String str;
        String[] strArr = new String[5];
        strArr[0] = "fstab.nox";
        strArr[1] = "init.nox.rc";
        strArr[2] = "ueventd.nox.rc";
        String str2 = "/system/bin/nox-prop";
        if (Environment.getExternalStorageState().equals("mounted")) {
            str = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "BigNoxHD";
        } else {
            str = "/system/bin/nox-prop";
        }
        strArr[3] = str;
        if (Environment.getExternalStorageState().equals("mounted")) {
            str2 = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "BigNoxGameHD";
        }
        strArr[4] = str2;
        NOX_FILES = strArr;
        PROPERTIES = new Property[]{new Property("init.svc.qemud", null), new Property("init.svc.qemu-props", null), new Property("qemu.hw.mainkeys", null), new Property("qemu.sf.fake_camera", null), new Property("qemu.sf.lcd_density", null), new Property("ro.bootloader", "unknown"), new Property("ro.bootmode", "unknown"), new Property("ro.hardware", "goldfish"), new Property("ro.kernel.android.qemud", null), new Property("ro.kernel.qemu.gles", null), new Property("ro.kernel.qemu", "1"), new Property("ro.product.device", "generic"), new Property("ro.product.model", ClientLogConstant.SDK), new Property("ro.product.name", ClientLogConstant.SDK)};
        QEMU_DRIVERS = new String[]{"goldfish"};
        X86_FILES = new String[]{"ueventd.android_x86.rc", "x86.prop", "ueventd.ttVM_x86.rc", "init.ttVM_x86.rc", "fstab.ttVM_x86", "fstab.vbox86", "init.vbox86.rc", "ueventd.vbox86.rc", "init.x86.rc"};
        PIPES = new String[]{"/dev/socket/qemud", "/dev/qemu_pipe"};
    }

    public static boolean detectLocal(Context context) {
        boolean zCheckBasic;
        try {
            zCheckBasic = checkBasic();
        } catch (Throwable th) {
            th = th;
            zCheckBasic = false;
        }
        try {
            LogUtils.i(LogUtils.TAG, "[detect] checkBasic result = " + zCheckBasic);
            if (!zCheckBasic) {
                zCheckBasic = checkAdvanced(context);
                LogUtils.i(LogUtils.TAG, "[detect] checkBasic checkAdvanced = " + zCheckBasic);
            }
            if ("x86".contains(CUtil.getCPUType())) {
                zCheckBasic = true;
            }
            if (isEmulatorFromCpu()) {
                return true;
            }
        } catch (Throwable th2) {
            th = th2;
            th.printStackTrace();
            return zCheckBasic;
        }
        return zCheckBasic;
    }

    private static boolean isEmulatorFromCpu() {
        ShellAdbUtils.CommandResult commandResultExecCommand = ShellAdbUtils.execCommand("cat /proc/cpuinfo", false);
        String str = commandResultExecCommand == null ? "" : commandResultExecCommand.successMsg;
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return str.toLowerCase().contains("intel") || str.toLowerCase().contains("amd");
    }

    private static boolean checkBasic() {
        boolean z = false;
        boolean z2 = Build.FINGERPRINT.startsWith("generic") || Build.MODEL.contains("google_sdk") || Build.MODEL.toLowerCase().contains("droid4x") || Build.MODEL.contains("Emulator") || Build.MODEL.contains("Android SDK built for x86") || Build.MANUFACTURER.contains("Genymotion") || Build.HARDWARE.equals("goldfish") || Build.HARDWARE.equals("vbox86") || Build.PRODUCT.equals(ClientLogConstant.SDK) || Build.PRODUCT.equals("google_sdk") || Build.PRODUCT.equals("sdk_x86") || Build.PRODUCT.equals("vbox86p") || Build.BOARD.toLowerCase().contains("nox") || Build.BOOTLOADER.toLowerCase().contains("nox") || Build.HARDWARE.toLowerCase().contains("nox") || Build.PRODUCT.toLowerCase().contains("nox") || "V417IR".equals(Build.ID);
        if (z2) {
            return true;
        }
        if (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")) {
            z = true;
        }
        boolean z3 = z2 | z;
        if (z3) {
            return true;
        }
        return z3 | "google_sdk".equals(Build.PRODUCT);
    }

    private static boolean checkAdvanced(Context context) {
        return checkFiles(GENY_FILES, "Geny") || checkFiles(ANDY_FILES, "Andy") || checkFiles(EXT_FILES, "ext_files(emu)") || checkFiles(NOX_FILES, "Nox") || checkQEmuDrivers() || checkFiles(PIPES, "Pipes") || (checkQEmuProps(context) && checkFiles(X86_FILES, "X86"));
    }

    private static boolean checkFiles(String[] strArr, String str) {
        for (String str2 : strArr) {
            if (new File(str2).exists()) {
                LogUtils.i(LogUtils.TAG, "[checkFiles] Check " + str + " is detected");
                return true;
            }
        }
        return false;
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
                        LogUtils.i(LogUtils.TAG, "Check QEmuDrivers is detected");
                        return true;
                    }
                }
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
        LogUtils.i(LogUtils.TAG, "[checkQEmuProps] Check QEmuProps is detected");
        return true;
    }

    static class Property {
        public String name;
        public String seek_value;

        public Property(String str, String str2) {
            this.name = str;
            this.seek_value = str2;
        }
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