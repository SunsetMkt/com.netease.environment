package com.netease.ntunisdk.base.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.webkit.WebView;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.GamerInterface;
import com.netease.ntunisdk.base.SdkBase;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.tencent.connect.common.Constants;
import com.xiaomi.gamecenter.sdk.web.webview.webkit.h;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Marker;

/* loaded from: classes3.dex */
public class DeviceDataCenter {
    private static final String CACHE_FILE_NAME = "unisdk_devices_infos_file";
    private static final String CPU_ARCHITECTURE_KEY_64 = "ro.product.cpu.abilist64";
    private static final String CPU_ARCHITECTURE_TYPE_32 = "32";
    private static final String CPU_ARCHITECTURE_TYPE_64 = "64";
    private static final int EI_CLASS = 4;
    private static final int ELFCLASS32 = 1;
    private static final int ELFCLASS64 = 2;
    private static final String PROC_CPU_INFO_PATH = "/proc/cpuinfo";
    private static final String PROC_MEM_INFO_PATH = "/proc/meminfo";
    private static final String SYM_DEVICES_SYSTEM_CPU = "/sys/devices/system/cpu/";
    private static final String SYSTEM_BIN_SU = "/system/bin/su";
    private static final String SYSTEM_LIB_C_PATH = "/system/lib/libc.so";
    private static final String SYSTEM_LIB_C_PATH_64 = "/system/lib64/libc.so";
    private static final String TAG = "DataCenter";
    private static DeviceDataCenter sDataCenter;
    private SharedPreferences sharedPreferences;
    private HashMap<Integer, String> mSensorsInfoMap = new HashMap<>();
    private boolean mHasInit = false;
    private boolean mInitSuccess = false;
    private String mHasPostData = "0";
    private boolean mPostDataInInit = false;
    private final String UNKNOW = "unknow";
    private ViewGroup mViewGroup = null;
    private GLSurfaceView mGlView = null;
    private FileFilter CPU_FILTER = new FileFilter() { // from class: com.netease.ntunisdk.base.utils.DeviceDataCenter.1
        @Override // java.io.FileFilter
        public boolean accept(File file) {
            if (file != null && file.exists()) {
                String name = file.getName();
                if (!TextUtils.isEmpty(name) && name.startsWith("cpu")) {
                    for (int i = 3; i < name.length(); i++) {
                        if (name.charAt(i) < '0' || name.charAt(i) > '9') {
                            return false;
                        }
                    }
                    return true;
                }
            }
            return false;
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public void initSensorsInfo(Context context) {
    }

    private DeviceDataCenter() {
    }

    public static DeviceDataCenter getInstance() {
        if (sDataCenter == null) {
            UniSdkUtils.i(TAG, "DataCenter [DeviceDataCenter] start");
            sDataCenter = new DeviceDataCenter();
        }
        return sDataCenter;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getManufacturer() {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_MANUFACTURER);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            stringCacheInfo = Build.MANUFACTURER;
            if (!TextUtils.isEmpty(stringCacheInfo)) {
                setStringCacheInfo(ConstProp.DEVICE_INFO_MANUFACTURER, stringCacheInfo);
            }
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getModel() {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_MODEL);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            stringCacheInfo = Build.MODEL;
            if (!TextUtils.isEmpty(stringCacheInfo)) {
                setStringCacheInfo(ConstProp.DEVICE_INFO_MODEL, stringCacheInfo);
            }
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getAndroidRelease() {
        return Build.VERSION.RELEASE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSdkInt() {
        return Build.VERSION.SDK_INT;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getFingerprint() {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_FINGERPRINT);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            stringCacheInfo = Build.FINGERPRINT;
            if (!TextUtils.isEmpty(stringCacheInfo)) {
                setStringCacheInfo(ConstProp.DEVICE_INFO_FINGERPRINT, stringCacheInfo);
            }
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getHardware() {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_HARDWARE);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            stringCacheInfo = Build.HARDWARE;
            if (!TextUtils.isEmpty(stringCacheInfo)) {
                setStringCacheInfo(ConstProp.DEVICE_INFO_HARDWARE, stringCacheInfo);
            }
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getRomVersion(String str) {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_ROM_VERSION);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            if (TextUtils.isEmpty(str)) {
                return "unknow";
            }
            String[] strArrSplit = str.split("/");
            if (strArrSplit != null && strArrSplit.length >= 5) {
                String str2 = strArrSplit[4];
                if (!TextUtils.isEmpty(str2) && str2.contains(":")) {
                    stringCacheInfo = str2.substring(0, str2.indexOf(":"));
                }
            }
            if (!TextUtils.isEmpty(stringCacheInfo)) {
                setStringCacheInfo(ConstProp.DEVICE_INFO_ROM_VERSION, stringCacheInfo);
            }
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getArchType(Context context) {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_ARCH_TYPE);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            if (context == null) {
                return "unknow";
            }
            int length = getSystemProperty(CPU_ARCHITECTURE_KEY_64, "").length();
            stringCacheInfo = CPU_ARCHITECTURE_TYPE_64;
            if (length > 0) {
                UniSdkUtils.i(TAG, "DataCenter [getArchType] CPU arch is 64bit");
            } else if (isCPUInfo64()) {
                UniSdkUtils.i(TAG, "DataCenter [getArchType] CPU arch isCPUInfo64");
            } else if (isLibc64()) {
                UniSdkUtils.i(TAG, "DataCenter [getArchType] CPU arch isLibc64");
            } else {
                UniSdkUtils.i(TAG, "DataCenter [getArchType] return cpu DEFAULT 32bit!");
                stringCacheInfo = CPU_ARCHITECTURE_TYPE_32;
            }
            if (!TextUtils.isEmpty(stringCacheInfo)) {
                setStringCacheInfo(ConstProp.DEVICE_INFO_ARCH_TYPE, stringCacheInfo);
            }
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getCPUType() {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_CPU_TYPE);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            stringCacheInfo = getSystemProperty("ro.product.cpu.abi", "arm").contains("x86") ? "x86" : "arm";
            setStringCacheInfo(ConstProp.DEVICE_INFO_CPU_TYPE, stringCacheInfo);
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    private boolean isCPUInfo64() throws IOException {
        FileInputStream fileInputStream;
        Throwable th;
        BufferedReader bufferedReader;
        StringBuilder sb;
        File file = new File(PROC_CPU_INFO_PATH);
        if (!file.exists()) {
            return false;
        }
        try {
            fileInputStream = new FileInputStream(file);
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream), 512);
            } catch (Throwable th2) {
                th = th2;
                bufferedReader = null;
            }
        } catch (Throwable th3) {
            fileInputStream = null;
            th = th3;
            bufferedReader = null;
        }
        try {
            String line = bufferedReader.readLine();
            if (TextUtils.isEmpty(line) || !line.toLowerCase(Locale.US).contains("arch64")) {
                UniSdkUtils.i(TAG, "DataCenter [isCPUInfo64] /proc/cpuinfo is not arch64");
                try {
                    bufferedReader.close();
                } catch (Exception e) {
                    UniSdkUtils.w(TAG, "DataCenter [isCPUInfo64] Exception = " + e.toString());
                    e.printStackTrace();
                }
                try {
                    fileInputStream.close();
                    return false;
                } catch (Exception e2) {
                    e = e2;
                    sb = new StringBuilder("DataCenter [isCPUInfo64] Exception2 = ");
                    sb.append(e.toString());
                    UniSdkUtils.w(TAG, sb.toString());
                    e.printStackTrace();
                    return false;
                }
            }
            UniSdkUtils.i(TAG, "DataCenter [isCPUInfo64] /proc/cpuinfo contains is arch64");
            try {
                bufferedReader.close();
            } catch (Exception e3) {
                UniSdkUtils.w(TAG, "DataCenter [isCPUInfo64] Exception = " + e3.toString());
                e3.printStackTrace();
            }
            try {
                fileInputStream.close();
                return true;
            } catch (Exception e4) {
                UniSdkUtils.w(TAG, "DataCenter [isCPUInfo64] Exception2 = " + e4.toString());
                e4.printStackTrace();
                return true;
            }
        } catch (Throwable th4) {
            th = th4;
            try {
                UniSdkUtils.w(TAG, "DataCenter [isCPUInfo64] /proc/cpuinfo error = " + th.toString());
                th.printStackTrace();
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (Exception e5) {
                        UniSdkUtils.w(TAG, "DataCenter [isCPUInfo64] Exception = " + e5.toString());
                        e5.printStackTrace();
                    }
                }
                if (fileInputStream == null) {
                    return false;
                }
                try {
                    fileInputStream.close();
                    return false;
                } catch (Exception e6) {
                    e = e6;
                    sb = new StringBuilder("DataCenter [isCPUInfo64] Exception2 = ");
                    sb.append(e.toString());
                    UniSdkUtils.w(TAG, sb.toString());
                    e.printStackTrace();
                    return false;
                }
            } finally {
            }
        }
    }

    private boolean isLibc64() {
        byte[] eLFHeadrIndentArray;
        byte[] eLFHeadrIndentArray2;
        File file = new File(SYSTEM_LIB_C_PATH);
        if (file.exists() && (eLFHeadrIndentArray2 = readELFHeadrIndentArray(file)) != null && eLFHeadrIndentArray2[4] == 2) {
            UniSdkUtils.i(TAG, "DataCenter [isLibc64] /system/lib/libc.so is 64bit");
            return true;
        }
        File file2 = new File(SYSTEM_LIB_C_PATH_64);
        if (!file2.exists() || (eLFHeadrIndentArray = readELFHeadrIndentArray(file2)) == null || eLFHeadrIndentArray.length <= 4 || eLFHeadrIndentArray[4] != 2) {
            return false;
        }
        UniSdkUtils.i(TAG, "DataCenter [isLibc64] /system/lib64/libc.so is 64bit");
        return true;
    }

    private byte[] readELFHeadrIndentArray(File file) throws IOException {
        FileInputStream fileInputStream;
        StringBuilder sb;
        if (file != null && file.exists()) {
            try {
                fileInputStream = new FileInputStream(file);
            } catch (Throwable th) {
                th = th;
                fileInputStream = null;
            }
            try {
                byte[] bArr = new byte[16];
                int i = fileInputStream.read(bArr, 0, 16);
                if (i == 16) {
                    try {
                        fileInputStream.close();
                    } catch (Exception e) {
                        UniSdkUtils.w(TAG, "DataCenter [readELFHeadrIndentArray] Exception=" + e.toString());
                        e.printStackTrace();
                    }
                    return bArr;
                }
                UniSdkUtils.e(TAG, "DataCenter [readELFHeadrIndentArray] Error: e_indent lenght should be 16, but actual is ".concat(String.valueOf(i)));
                try {
                    fileInputStream.close();
                } catch (Exception e2) {
                    e = e2;
                    sb = new StringBuilder("DataCenter [readELFHeadrIndentArray] Exception=");
                    sb.append(e.toString());
                    UniSdkUtils.w(TAG, sb.toString());
                    e.printStackTrace();
                    return null;
                }
            } catch (Throwable th2) {
                th = th2;
                try {
                    UniSdkUtils.w(TAG, "DataCenter [readELFHeadrIndentArray] Throwable=" + th.toString());
                    th.printStackTrace();
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (Exception e3) {
                            e = e3;
                            sb = new StringBuilder("DataCenter [readELFHeadrIndentArray] Exception=");
                            sb.append(e.toString());
                            UniSdkUtils.w(TAG, sb.toString());
                            e.printStackTrace();
                            return null;
                        }
                    }
                    return null;
                } catch (Throwable th3) {
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (Exception e4) {
                            UniSdkUtils.w(TAG, "DataCenter [readELFHeadrIndentArray] Exception=" + e4.toString());
                            e4.printStackTrace();
                        }
                    }
                    throw th3;
                }
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00d7  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0105 A[ORIG_RETURN, RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:62:0x00e3 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:70:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String getTotalMemory() throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 264
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.utils.DeviceDataCenter.getTotalMemory():java.lang.String");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getFreeMem(Context context) {
        ActivityManager activityManager;
        if (context == null || (activityManager = (ActivityManager) context.getSystemService("activity")) == null) {
            return -1L;
        }
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        if (memoryInfo.availMem > 0) {
            return (memoryInfo.availMem / 1024) / 1024;
        }
        return -1L;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0039  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String getTotalInternalMemorySize() {
        /*
            r8 = this;
            java.lang.String r0 = "DEVICE_INFO_TOTAL_INTERNAL_MEMORY"
            java.lang.String r1 = r8.getStringCacheInfo(r0)
            boolean r2 = android.text.TextUtils.isEmpty(r1)
            if (r2 == 0) goto L45
            int r2 = r8.getSdkInt()
            r3 = 18
            r4 = -4616189618054758400(0xbff0000000000000, double:-1.0)
            if (r2 < r3) goto L39
            java.io.File r2 = android.os.Environment.getDataDirectory()
            android.os.StatFs r3 = new android.os.StatFs
            java.lang.String r2 = r2.getPath()
            r3.<init>(r2)
            long r6 = r3.getBlockSizeLong()
            long r2 = r3.getBlockCountLong()
            long r2 = r2 * r6
            r6 = 0
            int r6 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r6 <= 0) goto L39
            double r2 = (double) r2
            r6 = 4652218415073722368(0x4090000000000000, double:1024.0)
            double r2 = r2 / r6
            double r2 = r2 / r6
            double r2 = r2 / r6
            goto L3a
        L39:
            r2 = r4
        L3a:
            int r4 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
            if (r4 == 0) goto L45
            java.lang.String r1 = java.lang.String.valueOf(r2)
            r8.setStringCacheInfo(r0, r1)
        L45:
            boolean r0 = android.text.TextUtils.isEmpty(r1)
            if (r0 == 0) goto L4d
            java.lang.String r1 = "unknow"
        L4d:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.utils.DeviceDataCenter.getTotalInternalMemorySize():java.lang.String");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public double getAvailableInternalMemorySize() {
        if (getSdkInt() >= 18) {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
            long availableBlocksLong = statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong();
            if (availableBlocksLong > 0) {
                return ((availableBlocksLong / 1024.0d) / 1024.0d) / 1024.0d;
            }
        }
        return -1.0d;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getNumberOfCPUCores() {
        int intCacheInfo = getIntCacheInfo(ConstProp.DEVICE_INFO_CPU_CORES_COUNT);
        if (-1 == intCacheInfo) {
            try {
                intCacheInfo = new File(SYM_DEVICES_SYSTEM_CPU).listFiles(this.CPU_FILTER).length;
            } catch (NullPointerException e) {
                UniSdkUtils.w(TAG, "DataCenter [getNumberOfCPUCores] NullPointerException=" + e.toString());
                e.printStackTrace();
            } catch (SecurityException e2) {
                UniSdkUtils.w(TAG, "DataCenter [getNumberOfCPUCores] SecurityException=" + e2.toString());
                e2.printStackTrace();
            } catch (Exception e3) {
                UniSdkUtils.w(TAG, "DataCenter [getNumberOfCPUCores] Exception=" + e3.toString());
                e3.printStackTrace();
            }
            if (-1 != intCacheInfo) {
                setIntCacheInfo(ConstProp.DEVICE_INFO_CPU_CORES_COUNT, intCacheInfo);
            }
        }
        return intCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00de  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int getCPUMinFreqKHz() throws java.io.IOException {
        /*
            r13 = this;
            java.lang.String r0 = "DataCenter"
            java.lang.String r1 = "DEVICE_INFO_CPU_MIN_FREQ_KHZ"
            int r2 = r13.getIntCacheInfo(r1)
            r3 = -1
            if (r3 != r2) goto Le1
            int r4 = r13.getNumberOfCPUCores()     // Catch: java.lang.Exception -> La8 java.io.IOException -> Lc2
            if (r4 <= 0) goto Ldc
            r5 = 0
            r6 = r5
        L13:
            if (r6 >= r4) goto Ldc
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> La8 java.io.IOException -> Lc2
            java.lang.String r8 = "/sys/devices/system/cpu/"
            r7.<init>(r8)     // Catch: java.lang.Exception -> La8 java.io.IOException -> Lc2
            r7.append(r6)     // Catch: java.lang.Exception -> La8 java.io.IOException -> Lc2
            java.lang.String r8 = "/cpufreq/cpuinfo_min_freq"
            r7.append(r8)     // Catch: java.lang.Exception -> La8 java.io.IOException -> Lc2
            java.lang.String r7 = r7.toString()     // Catch: java.lang.Exception -> La8 java.io.IOException -> Lc2
            java.io.File r8 = new java.io.File     // Catch: java.lang.Exception -> La8 java.io.IOException -> Lc2
            r8.<init>(r7)     // Catch: java.lang.Exception -> La8 java.io.IOException -> Lc2
            boolean r7 = r8.exists()     // Catch: java.lang.Exception -> La8 java.io.IOException -> Lc2
            if (r7 == 0) goto La4
            r7 = 128(0x80, float:1.8E-43)
            byte[] r9 = new byte[r7]     // Catch: java.lang.Exception -> La8 java.io.IOException -> Lc2
            java.io.FileInputStream r10 = new java.io.FileInputStream     // Catch: java.lang.Exception -> La8 java.io.IOException -> Lc2
            r10.<init>(r8)     // Catch: java.lang.Exception -> La8 java.io.IOException -> Lc2
            r10.read(r9)     // Catch: java.lang.Throwable -> L6a java.lang.Exception -> L6c java.lang.NumberFormatException -> L86
            r8 = r5
        L40:
            r11 = r9[r8]     // Catch: java.lang.Throwable -> L6a java.lang.Exception -> L6c java.lang.NumberFormatException -> L86
            r12 = 48
            if (r11 < r12) goto L4f
            r12 = 57
            if (r11 > r12) goto L4f
            if (r8 >= r7) goto L4f
            int r8 = r8 + 1
            goto L40
        L4f:
            java.lang.String r7 = new java.lang.String     // Catch: java.lang.Throwable -> L6a java.lang.Exception -> L6c java.lang.NumberFormatException -> L86
            r7.<init>(r9, r5, r8)     // Catch: java.lang.Throwable -> L6a java.lang.Exception -> L6c java.lang.NumberFormatException -> L86
            int r7 = java.lang.Integer.parseInt(r7)     // Catch: java.lang.Throwable -> L6a java.lang.Exception -> L6c java.lang.NumberFormatException -> L86
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)     // Catch: java.lang.Throwable -> L6a java.lang.Exception -> L6c java.lang.NumberFormatException -> L86
            int r8 = r7.intValue()     // Catch: java.lang.Throwable -> L6a java.lang.Exception -> L6c java.lang.NumberFormatException -> L86
            if (r8 <= r2) goto L66
            int r2 = r7.intValue()     // Catch: java.lang.Throwable -> L6a java.lang.Exception -> L6c java.lang.NumberFormatException -> L86
        L66:
            r10.close()     // Catch: java.lang.Exception -> La8 java.io.IOException -> Lc2
            goto La4
        L6a:
            r2 = move-exception
            goto La0
        L6c:
            r7 = move-exception
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L6a
            java.lang.String r9 = "DataCenter [getCPUMinFreqKHz] Exception1="
            r8.<init>(r9)     // Catch: java.lang.Throwable -> L6a
            java.lang.String r9 = r7.toString()     // Catch: java.lang.Throwable -> L6a
            r8.append(r9)     // Catch: java.lang.Throwable -> L6a
            java.lang.String r8 = r8.toString()     // Catch: java.lang.Throwable -> L6a
            com.netease.ntunisdk.base.UniSdkUtils.w(r0, r8)     // Catch: java.lang.Throwable -> L6a
            r7.printStackTrace()     // Catch: java.lang.Throwable -> L6a
            goto L66
        L86:
            r7 = move-exception
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L6a
            java.lang.String r9 = "DataCenter [getCPUMinFreqKHz] NumberFormatException="
            r8.<init>(r9)     // Catch: java.lang.Throwable -> L6a
            java.lang.String r9 = r7.toString()     // Catch: java.lang.Throwable -> L6a
            r8.append(r9)     // Catch: java.lang.Throwable -> L6a
            java.lang.String r8 = r8.toString()     // Catch: java.lang.Throwable -> L6a
            com.netease.ntunisdk.base.UniSdkUtils.w(r0, r8)     // Catch: java.lang.Throwable -> L6a
            r7.printStackTrace()     // Catch: java.lang.Throwable -> L6a
            goto L66
        La0:
            r10.close()     // Catch: java.lang.Exception -> La8 java.io.IOException -> Lc2
            throw r2     // Catch: java.lang.Exception -> La8 java.io.IOException -> Lc2
        La4:
            int r6 = r6 + 1
            goto L13
        La8:
            r2 = move-exception
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            java.lang.String r5 = "DataCenter [getCPUMinFreqKHz] Exception2="
            r4.<init>(r5)
            java.lang.String r5 = r2.toString()
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            com.netease.ntunisdk.base.UniSdkUtils.w(r0, r4)
            r2.printStackTrace()
            goto Ldb
        Lc2:
            r2 = move-exception
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            java.lang.String r5 = "DataCenter [getCPUMinFreqKHz] IOException="
            r4.<init>(r5)
            java.lang.String r5 = r2.toString()
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            com.netease.ntunisdk.base.UniSdkUtils.w(r0, r4)
            r2.printStackTrace()
        Ldb:
            r2 = r3
        Ldc:
            if (r3 == r2) goto Le1
            r13.setIntCacheInfo(r1, r2)
        Le1:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.utils.DeviceDataCenter.getCPUMinFreqKHz():int");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00de  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int getCPUMaxFreqKHz() throws java.io.IOException {
        /*
            r13 = this;
            java.lang.String r0 = "DataCenter"
            java.lang.String r1 = "DEVICE_INFO_CPU_MAX_FREQ_KHZ"
            int r2 = r13.getIntCacheInfo(r1)
            r3 = -1
            if (r3 != r2) goto Le1
            int r4 = r13.getNumberOfCPUCores()     // Catch: java.lang.Exception -> La8 java.io.IOException -> Lc2
            if (r4 <= 0) goto Ldc
            r5 = 0
            r6 = r5
        L13:
            if (r6 >= r4) goto Ldc
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> La8 java.io.IOException -> Lc2
            java.lang.String r8 = "/sys/devices/system/cpu/cpu"
            r7.<init>(r8)     // Catch: java.lang.Exception -> La8 java.io.IOException -> Lc2
            r7.append(r6)     // Catch: java.lang.Exception -> La8 java.io.IOException -> Lc2
            java.lang.String r8 = "/cpufreq/cpuinfo_max_freq"
            r7.append(r8)     // Catch: java.lang.Exception -> La8 java.io.IOException -> Lc2
            java.lang.String r7 = r7.toString()     // Catch: java.lang.Exception -> La8 java.io.IOException -> Lc2
            java.io.File r8 = new java.io.File     // Catch: java.lang.Exception -> La8 java.io.IOException -> Lc2
            r8.<init>(r7)     // Catch: java.lang.Exception -> La8 java.io.IOException -> Lc2
            boolean r7 = r8.exists()     // Catch: java.lang.Exception -> La8 java.io.IOException -> Lc2
            if (r7 == 0) goto La4
            r7 = 128(0x80, float:1.8E-43)
            byte[] r9 = new byte[r7]     // Catch: java.lang.Exception -> La8 java.io.IOException -> Lc2
            java.io.FileInputStream r10 = new java.io.FileInputStream     // Catch: java.lang.Exception -> La8 java.io.IOException -> Lc2
            r10.<init>(r8)     // Catch: java.lang.Exception -> La8 java.io.IOException -> Lc2
            r10.read(r9)     // Catch: java.lang.Throwable -> L6a java.lang.Exception -> L6c java.lang.NumberFormatException -> L86
            r8 = r5
        L40:
            r11 = r9[r8]     // Catch: java.lang.Throwable -> L6a java.lang.Exception -> L6c java.lang.NumberFormatException -> L86
            r12 = 48
            if (r11 < r12) goto L4f
            r12 = 57
            if (r11 > r12) goto L4f
            if (r8 >= r7) goto L4f
            int r8 = r8 + 1
            goto L40
        L4f:
            java.lang.String r7 = new java.lang.String     // Catch: java.lang.Throwable -> L6a java.lang.Exception -> L6c java.lang.NumberFormatException -> L86
            r7.<init>(r9, r5, r8)     // Catch: java.lang.Throwable -> L6a java.lang.Exception -> L6c java.lang.NumberFormatException -> L86
            int r7 = java.lang.Integer.parseInt(r7)     // Catch: java.lang.Throwable -> L6a java.lang.Exception -> L6c java.lang.NumberFormatException -> L86
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)     // Catch: java.lang.Throwable -> L6a java.lang.Exception -> L6c java.lang.NumberFormatException -> L86
            int r8 = r7.intValue()     // Catch: java.lang.Throwable -> L6a java.lang.Exception -> L6c java.lang.NumberFormatException -> L86
            if (r8 <= r2) goto L66
            int r2 = r7.intValue()     // Catch: java.lang.Throwable -> L6a java.lang.Exception -> L6c java.lang.NumberFormatException -> L86
        L66:
            r10.close()     // Catch: java.lang.Exception -> La8 java.io.IOException -> Lc2
            goto La4
        L6a:
            r2 = move-exception
            goto La0
        L6c:
            r7 = move-exception
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L6a
            java.lang.String r9 = "DataCenter [getCPUMaxFreqKHz] Exception="
            r8.<init>(r9)     // Catch: java.lang.Throwable -> L6a
            java.lang.String r9 = r7.toString()     // Catch: java.lang.Throwable -> L6a
            r8.append(r9)     // Catch: java.lang.Throwable -> L6a
            java.lang.String r8 = r8.toString()     // Catch: java.lang.Throwable -> L6a
            com.netease.ntunisdk.base.UniSdkUtils.w(r0, r8)     // Catch: java.lang.Throwable -> L6a
            r7.printStackTrace()     // Catch: java.lang.Throwable -> L6a
            goto L66
        L86:
            r7 = move-exception
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L6a
            java.lang.String r9 = "DataCenter [getCPUMaxFreqKHz] NumberFormatException="
            r8.<init>(r9)     // Catch: java.lang.Throwable -> L6a
            java.lang.String r9 = r7.toString()     // Catch: java.lang.Throwable -> L6a
            r8.append(r9)     // Catch: java.lang.Throwable -> L6a
            java.lang.String r8 = r8.toString()     // Catch: java.lang.Throwable -> L6a
            com.netease.ntunisdk.base.UniSdkUtils.w(r0, r8)     // Catch: java.lang.Throwable -> L6a
            r7.printStackTrace()     // Catch: java.lang.Throwable -> L6a
            goto L66
        La0:
            r10.close()     // Catch: java.lang.Exception -> La8 java.io.IOException -> Lc2
            throw r2     // Catch: java.lang.Exception -> La8 java.io.IOException -> Lc2
        La4:
            int r6 = r6 + 1
            goto L13
        La8:
            r2 = move-exception
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            java.lang.String r5 = "DataCenter [getCPUMaxFreqKHz] Exception2="
            r4.<init>(r5)
            java.lang.String r5 = r2.toString()
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            com.netease.ntunisdk.base.UniSdkUtils.w(r0, r4)
            r2.printStackTrace()
            goto Ldb
        Lc2:
            r2 = move-exception
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            java.lang.String r5 = "DataCenter [getCPUMaxFreqKHz] IOException="
            r4.<init>(r5)
            java.lang.String r5 = r2.toString()
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            com.netease.ntunisdk.base.UniSdkUtils.w(r0, r4)
            r2.printStackTrace()
        Ldb:
            r2 = r3
        Ldc:
            if (r3 == r2) goto Le1
            r13.setIntCacheInfo(r1, r2)
        Le1:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.utils.DeviceDataCenter.getCPUMaxFreqKHz():int");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getSupportedAbis() {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_SUPPORTED_ABIS);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            if (getSdkInt() >= 21) {
                try {
                    String[] strArr = Build.SUPPORTED_ABIS;
                    if (strArr != null && strArr.length > 0) {
                        for (String str : strArr) {
                            stringCacheInfo = stringCacheInfo + str + " ";
                        }
                    }
                } catch (Exception e) {
                    UniSdkUtils.w(TAG, "DataCenter [getSupportedAbis] Exception=" + e.toString());
                    e.printStackTrace();
                }
            }
            if (!TextUtils.isEmpty(stringCacheInfo)) {
                setStringCacheInfo(ConstProp.DEVICE_INFO_SUPPORTED_ABIS, stringCacheInfo);
            }
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getSupported32Abis() {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_SUPPORTED_32_BIT_ABIS);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            if (getSdkInt() >= 21) {
                try {
                    String[] strArr = Build.SUPPORTED_32_BIT_ABIS;
                    if (strArr != null && strArr.length > 0) {
                        for (String str : strArr) {
                            stringCacheInfo = stringCacheInfo + str + " ";
                        }
                    }
                } catch (Exception e) {
                    UniSdkUtils.w(TAG, "DataCenter [getSupported32Abis] Exception=" + e.toString());
                    e.printStackTrace();
                }
            }
            if (!TextUtils.isEmpty(stringCacheInfo)) {
                setStringCacheInfo(ConstProp.DEVICE_INFO_SUPPORTED_32_BIT_ABIS, stringCacheInfo);
            }
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getSupported64Abis() {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_SUPPORTED_64_BIT_ABIS);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            try {
                String[] strArr = Build.SUPPORTED_64_BIT_ABIS;
                if (strArr != null && strArr.length > 0) {
                    for (String str : strArr) {
                        stringCacheInfo = stringCacheInfo + str + " ";
                    }
                }
            } catch (Exception e) {
                UniSdkUtils.w(TAG, "DataCenter [getSupported64Abis] Exception=" + e.toString());
                e.printStackTrace();
            }
            if (!TextUtils.isEmpty(stringCacheInfo)) {
                setStringCacheInfo(ConstProp.DEVICE_INFO_SUPPORTED_64_BIT_ABIS, stringCacheInfo);
            }
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getBoard() {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_BOARD);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            stringCacheInfo = Build.BOARD;
            setStringCacheInfo(ConstProp.DEVICE_INFO_BOARD, stringCacheInfo);
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getRoBoardPlatform() {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_RO_BOARD_PLATFORM);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            try {
                String str = (String) Class.forName("android.os.SystemProperties").getMethod(h.c, String.class).invoke(null, "ro.board.platform");
                if (str != null) {
                    stringCacheInfo = str;
                }
            } catch (Exception e) {
                UniSdkUtils.w(TAG, "DataCenter [getPlatform] Exception=" + e.toString());
                e.printStackTrace();
            }
            if (!TextUtils.isEmpty(stringCacheInfo) && "unknow".equals(stringCacheInfo)) {
                setStringCacheInfo(ConstProp.DEVICE_INFO_RO_BOARD_PLATFORM, stringCacheInfo);
            }
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getGlRenderer() {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_GL_RENDERER);
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getGlVendor() {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_GL_VENDOR);
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getGlVersion() {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_GL_VERSION);
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initGPUInfo(Context context) {
        UniSdkUtils.i(TAG, "DataCenter [initGPUInfo] start");
        if (context == null) {
            return;
        }
        boolean zDetect = EmulatorDetector.detect(context);
        UniSdkUtils.i(TAG, "DataCenter [initGPUInfo] mIsEmulator=".concat(String.valueOf(zDetect)));
        if (zDetect) {
            return;
        }
        int[] iArr = {12325, 0, 12326, 0, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12344};
        int[] iArr2 = {12375, 100, 12374, 100, 12344};
        try {
            EGL10 egl10 = (EGL10) EGLContext.getEGL();
            EGLDisplay eGLDisplayEglGetDisplay = egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
            if (eGLDisplayEglGetDisplay == EGL10.EGL_NO_DISPLAY) {
                UniSdkUtils.i(TAG, "DataCenter [initGPUInfo] eglGetDisplay failed");
                return;
            }
            if (!egl10.eglInitialize(eGLDisplayEglGetDisplay, new int[2])) {
                UniSdkUtils.i(TAG, "DataCenter [initGPUInfo] eglInitialize failed");
                return;
            }
            int[] iArr3 = new int[1];
            if (!egl10.eglChooseConfig(eGLDisplayEglGetDisplay, iArr, null, 0, iArr3)) {
                UniSdkUtils.i(TAG, "DataCenter [initGPUInfo] eglChooseConfig failed");
                return;
            }
            int i = iArr3[0];
            if (i <= 0) {
                return;
            }
            EGLConfig[] eGLConfigArr = new EGLConfig[i];
            if (!egl10.eglChooseConfig(eGLDisplayEglGetDisplay, iArr, eGLConfigArr, i, iArr3)) {
                UniSdkUtils.i(TAG, "DataCenter [initGPUInfo] eglChooseConfig#2 failed");
                return;
            }
            EGLConfig eGLConfig = eGLConfigArr[0];
            int[] iArr4 = {12440, 2, 12344};
            EGLContext eGLContextEglGetCurrentContext = egl10.eglGetCurrentContext();
            if (eGLContextEglGetCurrentContext == EGL10.EGL_NO_CONTEXT) {
                UniSdkUtils.i(TAG, "DataCenter [initGPUInfo] mEGLContext= EGL_NO_CONTEXT");
                eGLContextEglGetCurrentContext = egl10.eglCreateContext(eGLDisplayEglGetDisplay, eGLConfig, EGL10.EGL_NO_CONTEXT, iArr4);
                EGLSurface eGLSurfaceEglCreatePbufferSurface = egl10.eglCreatePbufferSurface(eGLDisplayEglGetDisplay, eGLConfig, iArr2);
                if (eGLSurfaceEglCreatePbufferSurface == EGL10.EGL_NO_SURFACE) {
                    egl10.eglDestroyContext(eGLDisplayEglGetDisplay, eGLContextEglGetCurrentContext);
                    UniSdkUtils.i(TAG, "DataCenter [initGPUInfo] mEGLSurface=" + EGL10.EGL_NO_SURFACE + ", call eglDestroyContext");
                    return;
                }
                if (!egl10.eglMakeCurrent(eGLDisplayEglGetDisplay, eGLSurfaceEglCreatePbufferSurface, eGLSurfaceEglCreatePbufferSurface, eGLContextEglGetCurrentContext)) {
                    UniSdkUtils.i(TAG, "DataCenter [initGPUInfo] egl error=" + egl10.eglGetError());
                    return;
                }
            }
            GL10 gl10 = (GL10) eGLContextEglGetCurrentContext.getGL();
            String strGlGetString = gl10.glGetString(7937);
            String strGlGetString2 = gl10.glGetString(7936);
            String strGlGetString3 = gl10.glGetString(7938);
            GamerInterface inst = SdkMgr.getInst();
            if (inst == null) {
                return;
            }
            if (!TextUtils.isEmpty(strGlGetString)) {
                setStringCacheInfo(ConstProp.DEVICE_INFO_GL_RENDERER, strGlGetString);
                inst.setPropStr(ConstProp.DEVICE_INFO_GL_RENDERER, strGlGetString);
            }
            if (!TextUtils.isEmpty(strGlGetString2)) {
                setStringCacheInfo(ConstProp.DEVICE_INFO_GL_VENDOR, strGlGetString2);
                inst.setPropStr(ConstProp.DEVICE_INFO_GL_VENDOR, strGlGetString2);
            }
            if (!TextUtils.isEmpty(strGlGetString3)) {
                setStringCacheInfo(ConstProp.DEVICE_INFO_GL_VERSION, strGlGetString3);
                inst.setPropStr(ConstProp.DEVICE_INFO_GL_VERSION, strGlGetString3);
            }
            UniSdkUtils.i(TAG, "DataCenter [initGPUInfo] onSurfaceCreated glRenderer=" + strGlGetString + ", glVendor=" + strGlGetString2 + ", glVersion=" + strGlGetString3);
        } catch (Exception e) {
            UniSdkUtils.i(TAG, "DataCenter [initGPUInfo] Exception=" + e.toString());
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getScreenResolution(Context context) {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_SCREEN_RESOLUTION);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            if (context == null) {
                return "unknow";
            }
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            if (displayMetrics != null) {
                stringCacheInfo = displayMetrics.widthPixels + Marker.ANY_MARKER + displayMetrics.heightPixels;
            }
            if (!TextUtils.isEmpty(stringCacheInfo)) {
                setStringCacheInfo(ConstProp.DEVICE_INFO_SCREEN_RESOLUTION, stringCacheInfo);
            }
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:20:0x007d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String getScreenInch(android.content.Context r10) {
        /*
            r9 = this;
            java.lang.String r0 = "DEVICE_INFO_SCREEN_INCH"
            java.lang.String r1 = r9.getStringCacheInfo(r0)
            boolean r2 = android.text.TextUtils.isEmpty(r1)
            java.lang.String r3 = "unknow"
            if (r2 == 0) goto L89
            if (r10 != 0) goto L11
            return r3
        L11:
            r4 = -4616189618054758400(0xbff0000000000000, double:-1.0)
            android.app.Activity r10 = (android.app.Activity) r10     // Catch: java.lang.Exception -> L62
            android.view.WindowManager r10 = r10.getWindowManager()     // Catch: java.lang.Exception -> L62
            android.view.Display r10 = r10.getDefaultDisplay()     // Catch: java.lang.Exception -> L62
            if (r10 == 0) goto L7d
            android.util.DisplayMetrics r2 = new android.util.DisplayMetrics     // Catch: java.lang.Exception -> L62
            r2.<init>()     // Catch: java.lang.Exception -> L62
            r10.getMetrics(r2)     // Catch: java.lang.Exception -> L62
            android.graphics.Point r6 = new android.graphics.Point     // Catch: java.lang.Exception -> L62
            r6.<init>()     // Catch: java.lang.Exception -> L62
            r10.getRealSize(r6)     // Catch: java.lang.Exception -> L62
            int r10 = r6.x     // Catch: java.lang.Exception -> L62
            int r6 = r6.y     // Catch: java.lang.Exception -> L62
            if (r10 <= 0) goto L7d
            float r7 = r2.xdpi     // Catch: java.lang.Exception -> L62
            r8 = 0
            int r7 = (r7 > r8 ? 1 : (r7 == r8 ? 0 : -1))
            if (r7 <= 0) goto L7d
            if (r6 <= 0) goto L7d
            float r7 = r2.ydpi     // Catch: java.lang.Exception -> L62
            int r7 = (r7 > r8 ? 1 : (r7 == r8 ? 0 : -1))
            if (r7 <= 0) goto L7d
            float r10 = (float) r10     // Catch: java.lang.Exception -> L62
            float r7 = r2.xdpi     // Catch: java.lang.Exception -> L62
            float r7 = r10 / r7
            float r8 = r2.xdpi     // Catch: java.lang.Exception -> L62
            float r10 = r10 / r8
            float r7 = r7 * r10
            float r10 = (float) r6     // Catch: java.lang.Exception -> L62
            float r6 = r2.ydpi     // Catch: java.lang.Exception -> L62
            float r6 = r10 / r6
            float r2 = r2.ydpi     // Catch: java.lang.Exception -> L62
            float r10 = r10 / r2
            float r6 = r6 * r10
            float r7 = r7 + r6
            double r6 = (double) r7     // Catch: java.lang.Exception -> L62
            double r6 = java.lang.Math.sqrt(r6)     // Catch: java.lang.Exception -> L62
            r10 = 1
            double r6 = r9.formatDouble(r6, r10)     // Catch: java.lang.Exception -> L62
            goto L7e
        L62:
            r10 = move-exception
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r6 = "DataCenter [getScreenInch] Exception="
            r2.<init>(r6)
            java.lang.String r6 = r10.toString()
            r2.append(r6)
            java.lang.String r2 = r2.toString()
            java.lang.String r6 = "DataCenter"
            com.netease.ntunisdk.base.UniSdkUtils.w(r6, r2)
            r10.printStackTrace()
        L7d:
            r6 = r4
        L7e:
            int r10 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r10 == 0) goto L89
            java.lang.String r1 = java.lang.String.valueOf(r6)
            r9.setStringCacheInfo(r0, r1)
        L89:
            boolean r10 = android.text.TextUtils.isEmpty(r1)
            if (r10 == 0) goto L90
            goto L91
        L90:
            r3 = r1
        L91:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.utils.DeviceDataCenter.getScreenInch(android.content.Context):java.lang.String");
    }

    private double formatDouble(double d, int i) {
        try {
            return new BigDecimal(d).setScale(i, 4).doubleValue();
        } catch (Exception e) {
            UniSdkUtils.w(TAG, "DataCenter [formatDouble] Exception=" + e.toString());
            e.printStackTrace();
            return -1.0d;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getScreenDpi(Context context) {
        int intCacheInfo = getIntCacheInfo(ConstProp.DEVICE_INFO_SCREEN_DPI);
        if (-1 != intCacheInfo || context == null) {
            return intCacheInfo;
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int i = displayMetrics.densityDpi;
        setIntCacheInfo(ConstProp.DEVICE_INFO_SCREEN_DPI, i);
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0047  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x004c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String getScreenPixel(android.content.Context r13) throws java.lang.NumberFormatException {
        /*
            r12 = this;
            java.lang.String r0 = "DEVICE_INFO_SCREEN_PIXEL"
            java.lang.String r1 = r12.getStringCacheInfo(r0)
            boolean r2 = android.text.TextUtils.isEmpty(r1)
            java.lang.String r3 = "unknow"
            if (r2 == 0) goto L53
            if (r13 != 0) goto L11
            return r3
        L11:
            android.content.res.Resources r2 = r13.getResources()
            android.util.DisplayMetrics r2 = r2.getDisplayMetrics()
            int r4 = r2.widthPixels
            int r2 = r2.heightPixels
            java.lang.String r13 = r12.getScreenInch(r13)
            boolean r5 = android.text.TextUtils.isEmpty(r13)
            r6 = -4616189618054758400(0xbff0000000000000, double:-1.0)
            if (r5 != 0) goto L32
            double r8 = java.lang.Double.parseDouble(r13)     // Catch: java.lang.Exception -> L2e
            goto L33
        L2e:
            r13 = move-exception
            r13.printStackTrace()
        L32:
            r8 = r6
        L33:
            if (r4 <= 0) goto L47
            if (r2 <= 0) goto L47
            r10 = 0
            int r13 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r13 <= 0) goto L47
            int r4 = r4 * r4
            int r2 = r2 * r2
            int r4 = r4 + r2
            double r4 = (double) r4
            double r4 = java.lang.Math.sqrt(r4)
            double r4 = r4 / r8
            goto L48
        L47:
            r4 = r6
        L48:
            int r13 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1))
            if (r13 == 0) goto L53
            java.lang.String r1 = java.lang.String.valueOf(r4)
            r12.setStringCacheInfo(r0, r1)
        L53:
            boolean r13 = android.text.TextUtils.isEmpty(r1)
            if (r13 == 0) goto L5a
            goto L5b
        L5a:
            r3 = r1
        L5b:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.utils.DeviceDataCenter.getScreenPixel(android.content.Context):java.lang.String");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getXdpi_Ydpi(Context context) {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_XDPI_YDPI);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            if (context == null) {
                return "unknow";
            }
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            stringCacheInfo = displayMetrics.xdpi + " / " + displayMetrics.ydpi;
            setStringCacheInfo(ConstProp.DEVICE_INFO_XDPI_YDPI, stringCacheInfo);
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String isRooted() {
        boolean zExists;
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_IS_ROOT);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            try {
                zExists = new File(SYSTEM_BIN_SU).exists();
            } catch (Exception e) {
                UniSdkUtils.w(TAG, "DataCenter [isRooted] Exception=" + e.toString());
                e.printStackTrace();
                zExists = false;
            }
            stringCacheInfo = String.valueOf(zExists);
            setStringCacheInfo(ConstProp.DEVICE_INFO_IS_ROOT, stringCacheInfo);
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String isSupportNFC(Context context) {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_FEATURE_NFC);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            if (context == null) {
                return "unknow";
            }
            stringCacheInfo = String.valueOf(context.getPackageManager().hasSystemFeature("android.hardware.nfc"));
            setStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_FEATURE_NFC, stringCacheInfo);
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String isSupportHCE(Context context) {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_FEATURE_NFC_HOST_CARD_EMULATION);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            if (context == null) {
                return "unknow";
            }
            stringCacheInfo = String.valueOf(context.getPackageManager().hasSystemFeature("android.hardware.nfc.hce"));
            setStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_FEATURE_NFC_HOST_CARD_EMULATION, stringCacheInfo);
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    private String getWebviewInfo(Context context) {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_WEBVIEW_INFO);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            if (context == null) {
                return "unknow";
            }
            stringCacheInfo = new WebView(context).getSettings().getUserAgentString();
            setStringCacheInfo(ConstProp.DEVICE_INFO_WEBVIEW_INFO, stringCacheInfo);
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String isSupportAccelerometer() {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_ACCELEROMETER);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            stringCacheInfo = String.valueOf(this.mSensorsInfoMap.containsKey(1));
            setStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_ACCELEROMETER, stringCacheInfo);
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String isSupportMagneticField() {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_MAGNETIC_FIELD);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            stringCacheInfo = String.valueOf(this.mSensorsInfoMap.containsKey(2));
            setStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_MAGNETIC_FIELD, stringCacheInfo);
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String isSupportOrientation() {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_ORIENTATION);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            stringCacheInfo = String.valueOf(this.mSensorsInfoMap.containsKey(3));
            setStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_ORIENTATION, stringCacheInfo);
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String isSupportGyroscope() {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_GYROSCOPE);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            stringCacheInfo = String.valueOf(this.mSensorsInfoMap.containsKey(4));
            setStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_GYROSCOPE, stringCacheInfo);
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String isSupportLight() {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_LIGHT);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            stringCacheInfo = String.valueOf(this.mSensorsInfoMap.containsKey(5));
            setStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_LIGHT, stringCacheInfo);
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String isSupportPressure() {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_PRESSURE);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            stringCacheInfo = String.valueOf(this.mSensorsInfoMap.containsKey(6));
            setStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_PRESSURE, stringCacheInfo);
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String isSupportProximity() {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_PROXIMITY);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            stringCacheInfo = String.valueOf(this.mSensorsInfoMap.containsKey(8));
            setStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_PROXIMITY, stringCacheInfo);
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String isSupportGravity() {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_GRAVITY);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            stringCacheInfo = String.valueOf(this.mSensorsInfoMap.containsKey(9));
            setStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_GRAVITY, stringCacheInfo);
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String isSupportLinearAcceleration() {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_LINEAR_ACCELERATION);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            stringCacheInfo = String.valueOf(this.mSensorsInfoMap.containsKey(10));
            setStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_LINEAR_ACCELERATION, stringCacheInfo);
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String isSupportRotationVector() {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_ROTATION_VECTOR);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            stringCacheInfo = String.valueOf(this.mSensorsInfoMap.containsKey(11));
            setStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_ROTATION_VECTOR, stringCacheInfo);
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String isSupportRelativeHumidity() {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_RELATIVE_HUMIDITY);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            stringCacheInfo = String.valueOf(this.mSensorsInfoMap.containsKey(12));
            setStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_RELATIVE_HUMIDITY, stringCacheInfo);
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String isSupportAmbientTemperature() {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_AMBIENT_TEMPERATURE);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            stringCacheInfo = String.valueOf(this.mSensorsInfoMap.containsKey(13));
            setStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_AMBIENT_TEMPERATURE, stringCacheInfo);
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String isSupportMagneticFieldUncalibrated() {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_MAGNETIC_FIELD_UNCALIBRATED);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            stringCacheInfo = String.valueOf(this.mSensorsInfoMap.containsKey(14));
            setStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_MAGNETIC_FIELD_UNCALIBRATED, stringCacheInfo);
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String isSupportGameRotationVector() {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_GAME_ROTATION_VECTOR);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            stringCacheInfo = String.valueOf(this.mSensorsInfoMap.containsKey(15));
            setStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_GAME_ROTATION_VECTOR, stringCacheInfo);
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String isSupportGyroscopeUncalibrated() {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_GYROSCOPE_UNCALIBRATED);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            stringCacheInfo = String.valueOf(this.mSensorsInfoMap.containsKey(16));
            setStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_GYROSCOPE_UNCALIBRATED, stringCacheInfo);
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String isSupportSignificantMotion() {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_SIGNIFICANT_MOTION);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            stringCacheInfo = String.valueOf(this.mSensorsInfoMap.containsKey(17));
            setStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_SIGNIFICANT_MOTION, stringCacheInfo);
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String isSupportStepDetector() {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_STEP_DETECTOR);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            stringCacheInfo = String.valueOf(this.mSensorsInfoMap.containsKey(18));
            setStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_STEP_DETECTOR, stringCacheInfo);
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String isSupportStepCounter() {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_STEP_COUNTER);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            stringCacheInfo = String.valueOf(this.mSensorsInfoMap.containsKey(19));
            setStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_STEP_COUNTER, stringCacheInfo);
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String isSupportGeomagneticRotationVector() {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_GEOMAGNETIC_ROTATION_VECTOR);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            stringCacheInfo = String.valueOf(this.mSensorsInfoMap.containsKey(20));
            setStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_GEOMAGNETIC_ROTATION_VECTOR, stringCacheInfo);
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String isSupportHeartRate() {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_HEART_RATE);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            stringCacheInfo = String.valueOf(this.mSensorsInfoMap.containsKey(21));
            setStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_HEART_RATE, stringCacheInfo);
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String isSupportTiltDetector() {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_TILT_DETECTOR);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            stringCacheInfo = String.valueOf(this.mSensorsInfoMap.containsKey(22));
            setStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_TILT_DETECTOR, stringCacheInfo);
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String isSupportWakeGesture() {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_WAKE_GESTURE);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            stringCacheInfo = String.valueOf(this.mSensorsInfoMap.containsKey(23));
            setStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_WAKE_GESTURE, stringCacheInfo);
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String isSupportPickUpGesture() {
        String stringCacheInfo = getStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_PICK_UP_GESTURE);
        if (TextUtils.isEmpty(stringCacheInfo)) {
            stringCacheInfo = String.valueOf(this.mSensorsInfoMap.containsKey(25));
            setStringCacheInfo(ConstProp.DEVICE_INFO_IS_SUPPORT_PICK_UP_GESTURE, stringCacheInfo);
        }
        return TextUtils.isEmpty(stringCacheInfo) ? "unknow" : stringCacheInfo;
    }

    private String getSystemProperty(String str, String str2) throws ClassNotFoundException {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            str2 = (String) cls.getMethod(h.c, String.class, String.class).invoke(cls, str, "");
        } catch (Exception e) {
            UniSdkUtils.w(TAG, "DataCenter [getSystemProperty] Exception=" + e.toString());
            e.printStackTrace();
        }
        UniSdkUtils.i(TAG, "DataCenter [getSystemProperty] " + str + " = " + str2);
        return str2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setStringCacheInfo(String str, String str2) {
        if (this.sharedPreferences == null || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return;
        }
        SharedPreferences.Editor editorEdit = this.sharedPreferences.edit();
        editorEdit.putString(str, str2);
        editorEdit.commit();
    }

    private String getStringCacheInfo(String str) {
        if (this.sharedPreferences == null || TextUtils.isEmpty(str)) {
            return null;
        }
        return this.sharedPreferences.getString(str, null);
    }

    private void setIntCacheInfo(String str, int i) {
        if (this.sharedPreferences == null) {
            UniSdkUtils.w(TAG, "DataCenter [getDevicePerformanceScoreByCpuName] sharedPreferences == null");
        }
        if (this.sharedPreferences == null || TextUtils.isEmpty(str)) {
            return;
        }
        SharedPreferences.Editor editorEdit = this.sharedPreferences.edit();
        editorEdit.putInt(str, i);
        editorEdit.commit();
    }

    private int getIntCacheInfo(String str) {
        if (this.sharedPreferences == null || TextUtils.isEmpty(str)) {
            return -1;
        }
        return this.sharedPreferences.getInt(str, -1);
    }

    private boolean getBooleanCacheInfo(String str) {
        if (this.sharedPreferences == null || TextUtils.isEmpty(str)) {
            return false;
        }
        return this.sharedPreferences.getBoolean(str, false);
    }

    private void setBooleanCacheInfo(String str, boolean z) {
        if (this.sharedPreferences == null || TextUtils.isEmpty(str)) {
            return;
        }
        SharedPreferences.Editor editorEdit = this.sharedPreferences.edit();
        editorEdit.putBoolean(str, z);
        editorEdit.commit();
    }

    private long getLongCacheInfo(String str) {
        if (this.sharedPreferences == null || TextUtils.isEmpty(str)) {
            return -1L;
        }
        return this.sharedPreferences.getLong(str, -1L);
    }

    private String getUrl(String str) {
        String string;
        if (TextUtils.isEmpty(str) || str.length() < 2) {
            return null;
        }
        String str2 = str.substring(0, 2) + ".json";
        String propStr = SdkMgr.getInst().getPropStr(ConstProp.SDK_UNI_UPDATE_URL);
        if (TextUtils.isEmpty(propStr)) {
            string = SdkMgr.getInst().hasFeature("EB") ? ConstProp.DEVICE_INFO_CDN_OVERSEA_URL : ConstProp.DEVICE_INFO_CDN_URL;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(propStr);
            sb.append(propStr.endsWith("/") ? "" : "/");
            sb.append("ngdevice/");
            string = sb.toString();
        }
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        return string + str2;
    }

    private void setLongCacheInfo(String str, long j) {
        if (this.sharedPreferences == null || TextUtils.isEmpty(str)) {
            return;
        }
        SharedPreferences.Editor editorEdit = this.sharedPreferences.edit();
        editorEdit.putLong(str, j);
        editorEdit.commit();
    }

    public void initDeviceInfos(final Context context) {
        UniSdkUtils.i(TAG, "DataCenter [initDeviceInfos] start");
        final GamerInterface inst = SdkMgr.getInst();
        if (context == null || inst == null) {
            UniSdkUtils.w(TAG, "DataCenter [initDeviceInfos] param error");
            initSuccessCallBack(-1, "init fail");
            return;
        }
        if (this.mHasInit) {
            UniSdkUtils.w(TAG, "DataCenter [initDeviceInfos] has init");
            if (this.mInitSuccess) {
                initSuccessCallBack(0, "init success");
                return;
            } else {
                initSuccessCallBack(-1, "init fail");
                return;
            }
        }
        if (this.sharedPreferences == null) {
            UniSdkUtils.w(TAG, "DataCenter [initDeviceInfos] init sharedPreferences");
            this.sharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME, 0);
        }
        this.mHasInit = true;
        Thread thread = new Thread(new Runnable() { // from class: com.netease.ntunisdk.base.utils.DeviceDataCenter.2
            @Override // java.lang.Runnable
            public void run() throws Throwable {
                UniSdkUtils.w(DeviceDataCenter.TAG, "DataCenter [initDeviceInfos] thread start");
                boolean zDetect = EmulatorDetector.detect(context);
                UniSdkUtils.i(DeviceDataCenter.TAG, "DataCenter [initDeviceInfos] mIsEmulator=".concat(String.valueOf(zDetect)));
                DeviceDataCenter.this.initSensorsInfo(context);
                if (!"unknow".equals(DeviceDataCenter.this.getManufacturer())) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_MANUFACTURER, DeviceDataCenter.this.getManufacturer());
                }
                String model = DeviceDataCenter.this.getModel();
                if (!"unknow".equals(model)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_MODEL, model);
                }
                inst.setPropStr(ConstProp.DEVICE_INFO_RELEASE, DeviceDataCenter.this.getAndroidRelease());
                GamerInterface gamerInterface = inst;
                StringBuilder sb = new StringBuilder();
                sb.append(DeviceDataCenter.this.getSdkInt());
                gamerInterface.setPropStr(ConstProp.DEVICE_INFO_SDK_INT, sb.toString());
                String totalMemory = DeviceDataCenter.this.getTotalMemory();
                if (!"unknow".equals(totalMemory)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_TOTAL_MEMORY, totalMemory);
                }
                long freeMem = DeviceDataCenter.this.getFreeMem(context);
                if (-1 != freeMem) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_FREE_MEMORY, String.valueOf(freeMem));
                }
                String totalInternalMemorySize = DeviceDataCenter.this.getTotalInternalMemorySize();
                if (!"unknow".equals(totalInternalMemorySize)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_TOTAL_INTERNAL_MEMORY, totalInternalMemorySize);
                }
                double availableInternalMemorySize = DeviceDataCenter.this.getAvailableInternalMemorySize();
                if (-1.0d != availableInternalMemorySize) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_AVAILABLE_INTERNAL_MEMORY, String.valueOf(availableInternalMemorySize));
                }
                String fingerprint = DeviceDataCenter.this.getFingerprint();
                if (!"unknow".equals(fingerprint)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_FINGERPRINT, fingerprint);
                }
                String romVersion = DeviceDataCenter.this.getRomVersion(fingerprint);
                if (!"unknow".equals(romVersion)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_ROM_VERSION, romVersion);
                }
                String archType = DeviceDataCenter.this.getArchType(context);
                if (!"unknow".equals(romVersion)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_ARCH_TYPE, archType);
                }
                String cPUType = DeviceDataCenter.this.getCPUType();
                if (!"unknow".equals(cPUType)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_CPU_TYPE, cPUType);
                }
                int numberOfCPUCores = DeviceDataCenter.this.getNumberOfCPUCores();
                if (-1 != numberOfCPUCores) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_CPU_CORES_COUNT, String.valueOf(numberOfCPUCores));
                }
                int cPUMinFreqKHz = DeviceDataCenter.this.getCPUMinFreqKHz();
                if (-1 != cPUMinFreqKHz) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_CPU_MIN_FREQ_KHZ, String.valueOf(cPUMinFreqKHz));
                }
                int cPUMaxFreqKHz = DeviceDataCenter.this.getCPUMaxFreqKHz();
                if (-1 != cPUMaxFreqKHz) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_CPU_MAX_FREQ_KHZ, String.valueOf(cPUMaxFreqKHz));
                }
                if (!EmulatorDetector.detect(context) && DeviceDataCenter.this.getSdkInt() >= 21) {
                    String supportedAbis = DeviceDataCenter.this.getSupportedAbis();
                    if (!"unknow".equals(supportedAbis)) {
                        inst.setPropStr(ConstProp.DEVICE_INFO_SUPPORTED_ABIS, supportedAbis);
                    }
                    String supported32Abis = DeviceDataCenter.this.getSupported32Abis();
                    if (!"unknow".equals(supported32Abis)) {
                        inst.setPropStr(ConstProp.DEVICE_INFO_SUPPORTED_32_BIT_ABIS, supported32Abis);
                    }
                    String supported64Abis = DeviceDataCenter.this.getSupported64Abis();
                    if (!"unknow".equals(supported64Abis)) {
                        inst.setPropStr(ConstProp.DEVICE_INFO_SUPPORTED_64_BIT_ABIS, supported64Abis);
                    }
                }
                String board = DeviceDataCenter.this.getBoard();
                if (!"unknow".equals(board)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_BOARD, String.valueOf(board));
                }
                inst.setPropStr(ConstProp.DEVICE_INFO_RO_BOARD_PLATFORM, DeviceDataCenter.this.getRoBoardPlatform());
                inst.setPropStr(ConstProp.DEVICE_INFO_HARDWARE, DeviceDataCenter.this.getHardware());
                String screenResolution = DeviceDataCenter.this.getScreenResolution(context);
                if (!"unknow".equals(screenResolution)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_SCREEN_RESOLUTION, screenResolution);
                }
                String screenInch = DeviceDataCenter.this.getScreenInch(context);
                if (!"unknow".equals(screenInch)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_SCREEN_INCH, screenInch);
                }
                int screenDpi = DeviceDataCenter.this.getScreenDpi(context);
                if (-1 == screenDpi) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_SCREEN_DPI, String.valueOf(screenDpi));
                }
                String screenPixel = DeviceDataCenter.this.getScreenPixel(context);
                if (!"unknow".equals(screenPixel)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_SCREEN_PIXEL, screenPixel);
                }
                String xdpi_Ydpi = DeviceDataCenter.this.getXdpi_Ydpi(context);
                if (!"unknow".equals(xdpi_Ydpi)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_XDPI_YDPI, xdpi_Ydpi);
                }
                String strIsRooted = DeviceDataCenter.this.isRooted();
                if (!"unknow".equals(strIsRooted)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_IS_ROOT, strIsRooted);
                }
                String strIsSupportAccelerometer = DeviceDataCenter.this.isSupportAccelerometer();
                if (!"unknow".equals(strIsSupportAccelerometer)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_ACCELEROMETER, strIsSupportAccelerometer);
                }
                String strIsSupportMagneticField = DeviceDataCenter.this.isSupportMagneticField();
                if (!"unknow".equals(strIsSupportMagneticField)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_MAGNETIC_FIELD, strIsSupportMagneticField);
                }
                String strIsSupportOrientation = DeviceDataCenter.this.isSupportOrientation();
                if (!"unknow".equals(strIsSupportOrientation)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_ORIENTATION, strIsSupportOrientation);
                }
                String strIsSupportGyroscope = DeviceDataCenter.this.isSupportGyroscope();
                if (!"unknow".equals(strIsSupportGyroscope)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_GYROSCOPE, strIsSupportGyroscope);
                }
                String strIsSupportLight = DeviceDataCenter.this.isSupportLight();
                if (!"unknow".equals(strIsSupportLight)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_LIGHT, strIsSupportLight);
                }
                String strIsSupportPressure = DeviceDataCenter.this.isSupportPressure();
                if (!"unknow".equals(strIsSupportPressure)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_PRESSURE, strIsSupportPressure);
                }
                String strIsSupportProximity = DeviceDataCenter.this.isSupportProximity();
                if (!"unknow".equals(strIsSupportProximity)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_PROXIMITY, strIsSupportProximity);
                }
                String strIsSupportGravity = DeviceDataCenter.this.isSupportGravity();
                if (!"unknow".equals(strIsSupportGravity)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_GRAVITY, strIsSupportGravity);
                }
                String strIsSupportLinearAcceleration = DeviceDataCenter.this.isSupportLinearAcceleration();
                if (!"unknow".equals(strIsSupportLinearAcceleration)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_LINEAR_ACCELERATION, strIsSupportLinearAcceleration);
                }
                String strIsSupportRotationVector = DeviceDataCenter.this.isSupportRotationVector();
                if (!"unknow".equals(strIsSupportRotationVector)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_ROTATION_VECTOR, strIsSupportRotationVector);
                }
                String strIsSupportRelativeHumidity = DeviceDataCenter.this.isSupportRelativeHumidity();
                if (!"unknow".equals(strIsSupportRelativeHumidity)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_RELATIVE_HUMIDITY, strIsSupportRelativeHumidity);
                }
                String strIsSupportAmbientTemperature = DeviceDataCenter.this.isSupportAmbientTemperature();
                if (!"unknow".equals(strIsSupportAmbientTemperature)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_AMBIENT_TEMPERATURE, strIsSupportAmbientTemperature);
                }
                String strIsSupportMagneticFieldUncalibrated = DeviceDataCenter.this.isSupportMagneticFieldUncalibrated();
                if (!"unknow".equals(strIsSupportMagneticFieldUncalibrated)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_MAGNETIC_FIELD_UNCALIBRATED, strIsSupportMagneticFieldUncalibrated);
                }
                String strIsSupportGameRotationVector = DeviceDataCenter.this.isSupportGameRotationVector();
                if (!"unknow".equals(strIsSupportGameRotationVector)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_GAME_ROTATION_VECTOR, strIsSupportGameRotationVector);
                }
                String strIsSupportGyroscopeUncalibrated = DeviceDataCenter.this.isSupportGyroscopeUncalibrated();
                if (!"unknow".equals(strIsSupportGyroscopeUncalibrated)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_GYROSCOPE_UNCALIBRATED, strIsSupportGyroscopeUncalibrated);
                }
                String strIsSupportSignificantMotion = DeviceDataCenter.this.isSupportSignificantMotion();
                if (!"unknow".equals(strIsSupportSignificantMotion)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_SIGNIFICANT_MOTION, strIsSupportSignificantMotion);
                }
                String strIsSupportStepDetector = DeviceDataCenter.this.isSupportStepDetector();
                if (!"unknow".equals(strIsSupportStepDetector)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_STEP_DETECTOR, strIsSupportStepDetector);
                }
                String strIsSupportStepCounter = DeviceDataCenter.this.isSupportStepCounter();
                if (!"unknow".equals(strIsSupportStepCounter)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_STEP_COUNTER, strIsSupportStepCounter);
                }
                String strIsSupportGeomagneticRotationVector = DeviceDataCenter.this.isSupportGeomagneticRotationVector();
                if (!"unknow".equals(strIsSupportGeomagneticRotationVector)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_GEOMAGNETIC_ROTATION_VECTOR, strIsSupportGeomagneticRotationVector);
                }
                String strIsSupportHeartRate = DeviceDataCenter.this.isSupportHeartRate();
                if (!"unknow".equals(strIsSupportHeartRate)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_HEART_RATE, strIsSupportHeartRate);
                }
                String strIsSupportTiltDetector = DeviceDataCenter.this.isSupportTiltDetector();
                if (!"unknow".equals(strIsSupportTiltDetector)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_TILT_DETECTOR, strIsSupportTiltDetector);
                }
                String strIsSupportWakeGesture = DeviceDataCenter.this.isSupportWakeGesture();
                if (!"unknow".equals(strIsSupportWakeGesture)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_WAKE_GESTURE, strIsSupportWakeGesture);
                }
                String strIsSupportPickUpGesture = DeviceDataCenter.this.isSupportPickUpGesture();
                if (!"unknow".equals(strIsSupportPickUpGesture)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_PICK_UP_GESTURE, strIsSupportPickUpGesture);
                }
                String strIsSupportNFC = DeviceDataCenter.this.isSupportNFC(context);
                if (!"unknow".equals(strIsSupportNFC)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_FEATURE_NFC, strIsSupportNFC);
                }
                String strIsSupportHCE = DeviceDataCenter.this.isSupportHCE(context);
                if (!"unknow".equals(strIsSupportHCE)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_FEATURE_NFC_HOST_CARD_EMULATION, strIsSupportHCE);
                }
                String glRenderer = DeviceDataCenter.this.getGlRenderer();
                if (!"unknow".equals(glRenderer)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_GL_RENDERER, glRenderer);
                }
                String glVendor = DeviceDataCenter.this.getGlVendor();
                if (!"unknow".equals(glVendor)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_GL_VENDOR, glVendor);
                }
                String glVersion = DeviceDataCenter.this.getGlVersion();
                if (!"unknow".equals(glVersion)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_GL_VERSION, glVersion);
                }
                UniSdkUtils.w(DeviceDataCenter.TAG, "DataCenter [initDeviceInfos] glRenderer=" + glRenderer + ", glVendor=" + glVendor + ", glVersion=" + glVersion);
                if (!zDetect && ("unknow".equals(glRenderer) || "unknow".equals(glVendor) || "unknow".equals(glVersion))) {
                    UniSdkUtils.w(DeviceDataCenter.TAG, "DataCenter [initDeviceInfos] gl infol error, reset gl info");
                    DeviceDataCenter.this.initGPUInfo(context);
                }
                UniSdkUtils.i(DeviceDataCenter.TAG, "DataCenter [initDeviceInfos] finish");
                DeviceDataCenter.this.initSuccessCallBack(0, "init success");
                if (zDetect) {
                    UniSdkUtils.i(DeviceDataCenter.TAG, "DataCenter [initDeviceInfos] dont call getDevicePerformanceScore, call extendFuncCall device_performance_score");
                    try {
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put("methodId", "device_performance_score");
                        jSONObject.put("code", 0);
                        jSONObject.put("cpu", "VM");
                        jSONObject.put("score", "0");
                        jSONObject.put("score_range", "0:6200");
                        jSONObject.put("msg", "success");
                        ((SdkBase) inst).extendFuncCall(jSONObject.toString());
                        return;
                    } catch (Exception e) {
                        UniSdkUtils.w(DeviceDataCenter.TAG, "DataCenter [getDevicePerformanceScore] Exception2=" + e.toString());
                        e.printStackTrace();
                        return;
                    }
                }
                DeviceDataCenter.this.getDevicePerformanceScore();
            }
        });
        thread.setName("GetDeviceInfoThread");
        thread.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initSuccessCallBack(int i, String str) throws JSONException {
        UniSdkUtils.w(TAG, "DataCenter [initCallBack]");
        GamerInterface inst = SdkMgr.getInst();
        if (inst == null) {
            UniSdkUtils.w(TAG, "DataCenter [initCallBack] param error");
            return;
        }
        UniSdkUtils.i(TAG, "DataCenter [initCallBack] code=" + i + ", info=" + str);
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "device_module_init");
            jSONObject.put("code", i);
            jSONObject.put("msg", str);
            ((SdkBase) inst).extendFuncCall(jSONObject.toString());
        } catch (Exception e) {
            UniSdkUtils.w(TAG, "DataCenter [initCallBack] Exception=" + e.toString());
            e.printStackTrace();
        }
        if (i == 0) {
            this.mInitSuccess = true;
        }
        UniSdkUtils.i(TAG, "DataCenter [initCallBack] mPostDataInInit=" + this.mPostDataInInit);
        if (i == 0 && this.mPostDataInInit) {
            UniSdkUtils.i(TAG, "DataCenter [initCallBack] call postDeviceData");
            postDeviceData();
        }
    }

    public void getDevicePerformanceScore() throws JSONException {
        UniSdkUtils.i(TAG, "DataCenter [getDevicePerformanceScore] get my phone score");
        String str = getRoBoardPlatform() + "___" + getHardware();
        if (TextUtils.isEmpty(str)) {
            UniSdkUtils.i(TAG, "DataCenter [getDevicePerformanceScore] param error");
        } else {
            UniSdkUtils.i(TAG, "DataCenter [getDevicePerformanceScore] get my phone score, platform=".concat(String.valueOf(str)));
            getDevicePerformanceScore(str);
        }
    }

    public void getDevicePerformanceScore(final String str) throws JSONException {
        UniSdkUtils.i(TAG, "DataCenter [getDevicePerformanceScore] start");
        if (TextUtils.isEmpty(str) || str.length() < 2) {
            UniSdkUtils.w(TAG, "DataCenter [getDevicePerformanceScore] param error");
            return;
        }
        final String str2 = getRoBoardPlatform() + "___" + getHardware();
        final GamerInterface inst = SdkMgr.getInst();
        UniSdkUtils.i(TAG, "DataCenter [getDevicePerformanceScore] deviceInfo=".concat(String.valueOf(str)));
        if (inst != null) {
            String stringCacheInfo = getStringCacheInfo("DEVICE_INFO_".concat(String.valueOf(str)));
            String stringCacheInfo2 = getStringCacheInfo("DEVICE_INFO_RANGE");
            String stringCacheInfo3 = getStringCacheInfo(ConstProp.DEVICE_INFO_CPU_NAME);
            UniSdkUtils.i(TAG, "DataCenter [getDevicePerformanceScore] get score from cache, score=" + stringCacheInfo + ", scoreRange=" + stringCacheInfo2 + ", cpu=" + stringCacheInfo3);
            if (TextUtils.isEmpty(stringCacheInfo) || TextUtils.isEmpty(stringCacheInfo2) || "-1".equals(stringCacheInfo) || "unknow".equals(stringCacheInfo2)) {
                String url = getUrl(str);
                if (TextUtils.isEmpty(url)) {
                    UniSdkUtils.i(TAG, "null or empty url, device service will not go on.");
                    return;
                }
                String str3 = url + "?gameid=" + SdkMgr.getInst().getPropStr("JF_GAMEID");
                UniSdkUtils.i(TAG, "DataCenter [getDevicePerformanceScore] url=".concat(String.valueOf(str3)));
                NetUtil.wget(str3, new WgetDoneCallback() { // from class: com.netease.ntunisdk.base.utils.DeviceDataCenter.3
                    /* JADX WARN: Removed duplicated region for block: B:38:0x00d2 A[Catch: Exception -> 0x0183, TRY_ENTER, TryCatch #2 {Exception -> 0x0183, blocks: (B:35:0x00ab, B:38:0x00d2, B:40:0x00d8, B:42:0x010b, B:43:0x0114, B:45:0x0177, B:44:0x0140), top: B:54:0x00ab }] */
                    /* JADX WARN: Removed duplicated region for block: B:44:0x0140 A[Catch: Exception -> 0x0183, TryCatch #2 {Exception -> 0x0183, blocks: (B:35:0x00ab, B:38:0x00d2, B:40:0x00d8, B:42:0x010b, B:43:0x0114, B:45:0x0177, B:44:0x0140), top: B:54:0x00ab }] */
                    @Override // com.netease.ntunisdk.base.utils.WgetDoneCallback
                    /*
                        Code decompiled incorrectly, please refer to instructions dump.
                        To view partially-correct code enable 'Show inconsistent code' option in preferences
                    */
                    public void ProcessResult(java.lang.String r13) throws org.json.JSONException {
                        /*
                            Method dump skipped, instructions count: 413
                            To view this dump change 'Code comments level' option to 'DEBUG'
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.utils.DeviceDataCenter.AnonymousClass3.ProcessResult(java.lang.String):void");
                    }
                });
                return;
            }
            UniSdkUtils.i(TAG, "DataCenter [getDevicePerformanceScore] cpu=" + stringCacheInfo3 + ", score=" + stringCacheInfo + ", scoreRange=" + stringCacheInfo2);
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("methodId", "device_performance_score");
                jSONObject.put("code", 0);
                jSONObject.put("cpu", stringCacheInfo3);
                jSONObject.put("score", stringCacheInfo);
                jSONObject.put("score_range", stringCacheInfo2);
                jSONObject.put("msg", "success");
                ((SdkBase) inst).extendFuncCall(jSONObject.toString());
                if (str.equals(str2)) {
                    inst.setPropStr(ConstProp.DEVICE_INFO_PERFORMANCE_SCORE, String.valueOf(stringCacheInfo));
                }
                inst.setPropStr(ConstProp.DEVICE_INFO_PERFORMANCE_SCORE_.concat(String.valueOf(str)), String.valueOf(stringCacheInfo));
                inst.setPropStr(ConstProp.DEVICE_INFO_PERFORMANCE_SCORE_RANGE, String.valueOf(stringCacheInfo2));
                inst.setPropStr(ConstProp.DEVICE_INFO_CPU_NAME, String.valueOf(stringCacheInfo3));
                return;
            } catch (Exception e) {
                UniSdkUtils.w(TAG, "DataCenter [getDevicePerformanceScore] Exception2=" + e.toString());
                e.printStackTrace();
                return;
            }
        }
        UniSdkUtils.w(TAG, "DataCenter [getDevicePerformanceScore] sdkmgr is null");
    }

    public void getDevicePerformanceScoreRange() throws JSONException {
        String string;
        UniSdkUtils.i(TAG, "DataCenter [getDevicePerformanceScoreRange] start");
        final GamerInterface inst = SdkMgr.getInst();
        if (inst != null) {
            String stringCacheInfo = getStringCacheInfo("DEVICE_INFO_RANGE");
            UniSdkUtils.i(TAG, "DataCenter [getDevicePerformanceScoreRange] get scoreRange from cache, scoreRange=".concat(String.valueOf(stringCacheInfo)));
            if (TextUtils.isEmpty(stringCacheInfo)) {
                String propStr = SdkMgr.getInst().getPropStr(ConstProp.SDK_UNI_UPDATE_URL);
                if (TextUtils.isEmpty(propStr)) {
                    string = SdkMgr.getInst().hasFeature("EB") ? ConstProp.DEVICE_INFO_CDN_OVERSEA_URL : ConstProp.DEVICE_INFO_CDN_URL;
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append(propStr);
                    sb.append(propStr.endsWith("/") ? "" : "/");
                    sb.append("ngdevice/");
                    string = sb.toString();
                }
                if (TextUtils.isEmpty(string)) {
                    UniSdkUtils.i(TAG, "null or empty url, get device performance will not go on.");
                    return;
                }
                String str = string + "score_range.json?gameid=" + SdkMgr.getInst().getPropStr("JF_GAMEID");
                UniSdkUtils.i(TAG, "DataCenter [getDevicePerformanceScoreRange] url=".concat(String.valueOf(str)));
                NetUtil.wget(str, new WgetDoneCallback() { // from class: com.netease.ntunisdk.base.utils.DeviceDataCenter.4
                    @Override // com.netease.ntunisdk.base.utils.WgetDoneCallback
                    public void ProcessResult(String str2) throws JSONException {
                        UniSdkUtils.i(DeviceDataCenter.TAG, "DataCenter [getDevicePerformanceScoreRange] result=".concat(String.valueOf(str2)));
                        String string2 = null;
                        if (!TextUtils.isEmpty(str2)) {
                            try {
                                JSONObject jSONObject = new JSONObject(str2);
                                if (jSONObject.has("score_range")) {
                                    string2 = jSONObject.getString("score_range");
                                }
                            } catch (JSONException e) {
                                UniSdkUtils.w(DeviceDataCenter.TAG, "DataCenter [getDevicePerformanceScoreRange] JSONException=" + e.toString());
                                e.printStackTrace();
                            }
                        }
                        UniSdkUtils.i(DeviceDataCenter.TAG, "DataCenter [getDevicePerformanceScoreRange] score_range=".concat(String.valueOf(string2)));
                        try {
                            JSONObject jSONObject2 = new JSONObject();
                            jSONObject2.put("methodId", "device_performance_score_range");
                            jSONObject2.put("score_range", string2);
                            if (!TextUtils.isEmpty(string2)) {
                                jSONObject2.put("code", 0);
                                jSONObject2.put("msg", "success");
                                DeviceDataCenter.this.setStringCacheInfo("DEVICE_INFO_RANGE", string2);
                                inst.setPropStr(ConstProp.DEVICE_INFO_PERFORMANCE_SCORE_RANGE, String.valueOf(string2));
                            } else {
                                jSONObject2.put("code", -1);
                                jSONObject2.put("msg", "fail");
                            }
                            ((SdkBase) inst).extendFuncCall(jSONObject2.toString());
                        } catch (Exception e2) {
                            UniSdkUtils.w(DeviceDataCenter.TAG, "DataCenter [getDevicePerformanceScoreRange] Exception1=" + e2.toString());
                            e2.printStackTrace();
                        }
                    }
                });
                return;
            }
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("methodId", "device_performance_score_range");
                jSONObject.put("code", 0);
                jSONObject.put("score_range", stringCacheInfo);
                jSONObject.put("msg", "success");
                ((SdkBase) inst).extendFuncCall(jSONObject.toString());
                inst.setPropStr(ConstProp.DEVICE_INFO_PERFORMANCE_SCORE_RANGE, String.valueOf(stringCacheInfo));
                return;
            } catch (Exception e) {
                UniSdkUtils.w(TAG, "DataCenter [getDevicePerformanceScoreRange] Exception2=" + e.toString());
                e.printStackTrace();
                return;
            }
        }
        UniSdkUtils.w(TAG, "DataCenter [getDevicePerformanceScoreRange] sdkmgr is null=");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postDeviceData() {
        UniSdkUtils.i(TAG, "DataCenter [postDeviceData] start");
        this.mHasPostData = getStringCacheInfo(ConstProp.DEVICE_HAS_POST_DATA);
        UniSdkUtils.i(TAG, "DataCenter [postDeviceData] mHasPostData=" + this.mHasPostData);
        if (TextUtils.isEmpty(this.mHasPostData) || "0".equals(this.mHasPostData)) {
            String propStr = SdkMgr.getInst().getPropStr(ConstProp.SDK_DUMP_URL);
            if (TextUtils.isEmpty(propStr)) {
                propStr = SdkMgr.getInst().hasFeature("EB") ? ConstProp.DEVICE_INFO_POST_OVERSEA_URL : ConstProp.DEVICE_INFO_POST_URL;
            }
            if (TextUtils.isEmpty(propStr)) {
                UniSdkUtils.i(TAG, "null or empty url, post device data will not go on");
            } else {
                UniSdkUtils.i(TAG, "DataCenter [postDeviceData] url=".concat(String.valueOf(propStr)));
                NetUtil.wpost_http_https(propStr, getDeviceData().toString(), new WgetDoneCallback() { // from class: com.netease.ntunisdk.base.utils.DeviceDataCenter.5
                    @Override // com.netease.ntunisdk.base.utils.WgetDoneCallback
                    public void ProcessResult(String str) throws JSONException {
                        UniSdkUtils.i(DeviceDataCenter.TAG, "DataCenter [postDeviceData] result=".concat(String.valueOf(str)));
                        DeviceDataCenter.this.setStringCacheInfo(ConstProp.DEVICE_HAS_POST_DATA, "1");
                        DeviceDataCenter.this.mHasPostData = "1";
                        GamerInterface inst = SdkMgr.getInst();
                        if (inst != null) {
                            try {
                                JSONObject jSONObject = new JSONObject();
                                jSONObject.put("methodId", "post_device_data");
                                jSONObject.put("result", str);
                                ((SdkBase) inst).extendFuncCall(jSONObject.toString());
                            } catch (Exception e) {
                                UniSdkUtils.w(DeviceDataCenter.TAG, "DataCenter [postDeviceData] Exception=" + e.toString());
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        }
    }

    private JSONObject getDeviceData() throws JSONException {
        String str;
        UniSdkUtils.i(TAG, "DataCenter [getDeviceData] start");
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(Constants.PARAM_SCOPE, "release");
            jSONObject.put(ConstProp.DEVICE_INFO_MANUFACTURER, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_MANUFACTURER, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_MODEL, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_MODEL, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_RELEASE, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_RELEASE));
            jSONObject.put(ConstProp.DEVICE_INFO_SDK_INT, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_SDK_INT));
            jSONObject.put(ConstProp.DEVICE_INFO_TOTAL_MEMORY, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_TOTAL_MEMORY, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_TOTAL_INTERNAL_MEMORY, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_TOTAL_INTERNAL_MEMORY, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_FINGERPRINT, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_FINGERPRINT, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_ROM_VERSION, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_ROM_VERSION, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_ARCH_TYPE, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_ARCH_TYPE, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_CPU_TYPE, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_CPU_TYPE, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_CPU_CORES_COUNT, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_CPU_CORES_COUNT, "-1"));
            jSONObject.put(ConstProp.DEVICE_INFO_CPU_MIN_FREQ_KHZ, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_CPU_MIN_FREQ_KHZ, "-1"));
            jSONObject.put(ConstProp.DEVICE_INFO_CPU_MAX_FREQ_KHZ, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_CPU_MAX_FREQ_KHZ, "-1"));
            if (getSdkInt() >= 21) {
                jSONObject.put(ConstProp.DEVICE_INFO_SUPPORTED_ABIS, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_SUPPORTED_ABIS, "unknow"));
                jSONObject.put(ConstProp.DEVICE_INFO_SUPPORTED_32_BIT_ABIS, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_SUPPORTED_32_BIT_ABIS, "unknow"));
                jSONObject.put(ConstProp.DEVICE_INFO_SUPPORTED_64_BIT_ABIS, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_SUPPORTED_64_BIT_ABIS, "unknow"));
            }
            jSONObject.put(ConstProp.DEVICE_INFO_BOARD, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_BOARD, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_RO_BOARD_PLATFORM, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_RO_BOARD_PLATFORM, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_HARDWARE, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_HARDWARE, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_GL_RENDERER, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_GL_RENDERER, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_GL_VENDOR, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_GL_VENDOR, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_GL_VERSION, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_GL_VERSION, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_SCREEN_RESOLUTION, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_SCREEN_RESOLUTION, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_SCREEN_INCH, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_SCREEN_INCH, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_SCREEN_DPI, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_SCREEN_DPI, "-1"));
            jSONObject.put(ConstProp.DEVICE_INFO_SCREEN_PIXEL, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_SCREEN_PIXEL, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_XDPI_YDPI, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_XDPI_YDPI, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_IS_ROOT, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_IS_ROOT, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_WEBVIEW_INFO, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_WEBVIEW_INFO, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_IS_SUPPORT_ACCELEROMETER, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_ACCELEROMETER, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_IS_SUPPORT_MAGNETIC_FIELD, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_MAGNETIC_FIELD, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_IS_SUPPORT_ORIENTATION, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_ORIENTATION, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_IS_SUPPORT_GYROSCOPE, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_GYROSCOPE, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_IS_SUPPORT_LIGHT, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_LIGHT, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_IS_SUPPORT_PRESSURE, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_PRESSURE, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_IS_SUPPORT_PROXIMITY, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_PROXIMITY, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_IS_SUPPORT_GRAVITY, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_GRAVITY, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_IS_SUPPORT_LINEAR_ACCELERATION, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_LINEAR_ACCELERATION, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_IS_SUPPORT_ROTATION_VECTOR, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_ROTATION_VECTOR, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_IS_SUPPORT_RELATIVE_HUMIDITY, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_RELATIVE_HUMIDITY, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_IS_SUPPORT_AMBIENT_TEMPERATURE, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_AMBIENT_TEMPERATURE, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_IS_SUPPORT_MAGNETIC_FIELD_UNCALIBRATED, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_MAGNETIC_FIELD_UNCALIBRATED, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_IS_SUPPORT_GAME_ROTATION_VECTOR, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_GAME_ROTATION_VECTOR, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_IS_SUPPORT_GYROSCOPE_UNCALIBRATED, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_GYROSCOPE_UNCALIBRATED, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_IS_SUPPORT_SIGNIFICANT_MOTION, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_SIGNIFICANT_MOTION, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_IS_SUPPORT_STEP_DETECTOR, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_STEP_DETECTOR, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_IS_SUPPORT_STEP_COUNTER, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_STEP_COUNTER, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_IS_SUPPORT_GEOMAGNETIC_ROTATION_VECTOR, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_GEOMAGNETIC_ROTATION_VECTOR, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_IS_SUPPORT_HEART_RATE, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_HEART_RATE, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_IS_SUPPORT_TILT_DETECTOR, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_TILT_DETECTOR, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_IS_SUPPORT_WAKE_GESTURE, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_WAKE_GESTURE, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_IS_SUPPORT_PICK_UP_GESTURE, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_PICK_UP_GESTURE, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_IS_SUPPORT_FEATURE_NFC, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_FEATURE_NFC, "unknow"));
            jSONObject.put(ConstProp.DEVICE_INFO_IS_SUPPORT_FEATURE_NFC_HOST_CARD_EMULATION, SdkMgr.getInst().getPropStr(ConstProp.DEVICE_INFO_IS_SUPPORT_FEATURE_NFC_HOST_CARD_EMULATION, "unknow"));
            str = TAG;
        } catch (JSONException e) {
            str = TAG;
            UniSdkUtils.w(str, "DataCenter [getDeviceData] JSONException =" + e.toString());
            e.printStackTrace();
        } catch (Exception e2) {
            String str2 = "DataCenter [getDeviceData] Exception =" + e2.toString();
            str = TAG;
            UniSdkUtils.w(str, str2);
            e2.printStackTrace();
        }
        UniSdkUtils.i(str, "DataCenter [getDeviceData] data=" + jSONObject.toString());
        return jSONObject;
    }

    public String getDefaultScore() throws Throwable {
        UniSdkUtils.i(TAG, "DataCenter [getDefaultScore]");
        String totalMemory = getTotalMemory();
        if (TextUtils.isEmpty(totalMemory)) {
            return "0";
        }
        try {
            long j = Long.parseLong(totalMemory);
            UniSdkUtils.i(TAG, "DataCenter [getDefaultScore] totalMemoryInt=".concat(String.valueOf(j)));
            return j >= 7000 ? "5999" : (j < 4000 || j >= 7000) ? "0" : "4500";
        } catch (Exception e) {
            UniSdkUtils.i(TAG, "DataCenter [getDefaultScore] Exception=" + e.toString());
            e.printStackTrace();
            return "0";
        }
    }

    public void setmPostDataInInit(boolean z) {
        this.mPostDataInInit = z;
    }
}