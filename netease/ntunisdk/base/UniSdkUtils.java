package com.netease.ntunisdk.base;

import android.app.ActivityManager;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import com.facebook.hermes.intl.Constants;
import com.netease.androidcrashhandler.util.ShellAdbUtils;
import com.netease.ntunisdk.base.utils.CachedThreadPoolUtil;
import com.netease.ntunisdk.base.utils.EmulatorDetector;
import com.netease.ntunisdk.base.utils.GaidUtils;
import com.netease.ntunisdk.base.utils.NetConnectivity;
import com.netease.ntunisdk.modules.api.ModulesManager;
import com.netease.ntunisdk.modules.deviceinfo.DeviceUtils;
import com.netease.unisdk.ngvoice.log.NgLog;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class UniSdkUtils {

    /* renamed from: a */
    private static String f1809a = null;
    public static int appVersionCode = -1;
    private static String d = "default";
    private static String e = "default";
    private static String f = "default";
    private static String g = "default";
    private static String h = "default";
    private static String i = "default";
    public static boolean isDebug;
    private static String j;
    private static final Pattern b = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
    private static int k = -1;
    private static int l = -1;
    private static final LocationListener c = new LocationListener() { // from class: com.netease.ntunisdk.base.UniSdkUtils.2
        AnonymousClass2() {
        }

        @Override // android.location.LocationListener
        public final void onLocationChanged(Location location) {
            UniSdkUtils.d("UniSDK UniSdkUtils", "onLocationChanged: ".concat(String.valueOf(location)));
        }

        @Override // android.location.LocationListener
        public final void onStatusChanged(String str, int i2, Bundle bundle) {
            UniSdkUtils.d("UniSDK UniSdkUtils", "onStatusChanged: " + str + "/" + i2);
        }

        @Override // android.location.LocationListener
        public final void onProviderEnabled(String str) {
            UniSdkUtils.d("UniSDK UniSdkUtils", "onProviderEnabled: ".concat(String.valueOf(str)));
        }

        @Override // android.location.LocationListener
        public final void onProviderDisabled(String str) {
            UniSdkUtils.d("UniSDK UniSdkUtils", "onProviderDisabled: ".concat(String.valueOf(str)));
        }
    };

    public interface GaidCallback extends GaidUtils.Callback {
    }

    public static void d(String str, String str2) {
        if (isDebug) {
            Log.d(str, str2);
        }
    }

    public static void v(String str, String str2) {
        if (isDebug) {
            Log.v(str, str2);
        }
    }

    public static void i(String str, String str2) {
        if (isDebug) {
            Log.i(str, str2);
        }
    }

    public static void w(String str, String str2) {
        Log.w(str, str2);
    }

    public static void e(String str, String str2) {
        Log.e(str, str2);
    }

    public static void d2(String str, String str2) {
        if (isDebug) {
            Log.d(str, str2);
        }
    }

    public static void e2(String str, String str2) {
        Log.e(str, str2);
    }

    private static boolean isDebug() throws IllegalAccessException, IllegalArgumentException {
        if (c()) {
            return true;
        }
        SdkBase sdkBase = (SdkBase) SdkMgr.getInst();
        if (sdkBase == null) {
            Log.e("UniSDK UniSdkUtils", "please call SdkMgr.init(this) first");
            return false;
        }
        if (sdkBase.myCtx == null) {
            Log.e("UniSDK UniSdkUtils", "sdkBase.myCtx is null");
            return false;
        }
        int propInt = sdkBase.getPropInt(ConstProp.DEBUG_MODE, 0);
        int propInt2 = sdkBase.getPropInt(ConstProp.DEBUG_LOG, 0);
        if (1 == propInt || 1 == propInt2) {
            return true;
        }
        try {
            boolean z = Class.forName(getAppPackageName(sdkBase.myCtx) + ".BuildConfig").getDeclaredField("DEBUG").getBoolean(null);
            if (z) {
                return z;
            }
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException | NullPointerException unused) {
        }
        return 1 == Settings.System.getInt(sdkBase.myCtx.getContentResolver(), NgLog.NT_UNISDK_DEBUG_KEY);
    }

    private static boolean c() {
        try {
            if (!Environment.getExternalStorageState().equals("mounted")) {
                return false;
            }
            String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            StringBuilder sb = new StringBuilder();
            sb.append(absolutePath);
            sb.append(File.separator);
            sb.append(".data");
            sb.append(File.separator);
            sb.append("ntUniSDK");
            sb.append(File.separator);
            sb.append(Constants.SENSITIVITY_BASE);
            sb.append(File.separator);
            sb.append("debug_log");
            return new File(sb.toString()).exists();
        } catch (Exception unused) {
            return false;
        }
    }

    static void a() {
        isDebug = isDebug();
        Log.d("UniSDK UniSdkUtils", "UniSdkUtils log:" + isDebug);
    }

    public static String getCpuName() {
        return ntGetCpuName();
    }

    public static String getTransid(Context context) {
        boolean zHasFeature;
        d("UniSDK UniSdkUtils", "UniSdkUtils [getTransid]");
        if (SdkMgr.getInst() != null) {
            zHasFeature = SdkMgr.getInst().hasFeature("NO_ANDROIDID");
        } else {
            ModulesManager.getInst().init(context);
            zHasFeature = false;
        }
        String strExtendFunc = ModulesManager.getInst().extendFunc(ConstProp.UNISDKBASE, "deviceInfo", String.format("{\"methodId\":\"getTransId\",\"isNoAndroidId\":%s}", Boolean.valueOf(zHasFeature)));
        return (!TextUtils.isEmpty(strExtendFunc) || ModulesManager.getInst().hasModule("deviceInfo") || SdkMgr.getInst() == null) ? strExtendFunc : DeviceUtils.getTransid(context, SdkMgr.getInst().hasFeature("NO_ANDROIDID"), SdkMgr.getInst().hasFeature("ENABLE_FAKE_ABOUT_ID"));
    }

    public static String getSystemLanguage() {
        return ModulesManager.getInst().extendFunc(ConstProp.UNISDKBASE, "deviceInfo", "{\"methodId\":\"getSystemLanguage\"}");
    }

    public static String getSimCountryIso() {
        return ModulesManager.getInst().extendFunc(ConstProp.UNISDKBASE, "deviceInfo", "{\"methodId\":\"getSimCountryIso\"}");
    }

    public static String getWifiListJson() {
        return ModulesManager.getInst().extendFunc(ConstProp.UNISDKBASE, "deviceInfo", "{\"methodId\":\"getWifiListJson\"}");
    }

    public static String getSurveyPaperLanguage() {
        String systemLanguage;
        i("UniSDK UniSdkUtils", "StrUtil [getLocalLanguage] system version = " + Build.VERSION.SDK_INT);
        if (SdkMgr.getInst() != null) {
            String propStr = SdkMgr.getInst().getPropStr(ConstProp.GAME_SELECT_LANGUAGE);
            String propStr2 = SdkMgr.getInst().getPropStr(ConstProp.GAME_SUPPORT_LANGUAGE);
            i("UniSDK UniSdkUtils", "StrUtil [getLocalLanguage] param gameLanguage=" + propStr + ", supportLanguage=" + propStr2);
            systemLanguage = getSystemLanguage();
            i("UniSDK UniSdkUtils", "StrUtil [getLocalLanguage] systemLanguage=".concat(String.valueOf(systemLanguage)));
            if (TextUtils.isEmpty(propStr)) {
                propStr = systemLanguage;
            } else {
                i("UniSDK UniSdkUtils", "StrUtil [getLocalLanguage] use gameLanguage");
            }
            if (TextUtils.isEmpty(systemLanguage) || TextUtils.isEmpty(propStr2) || propStr2.contains(systemLanguage)) {
                systemLanguage = propStr;
            } else {
                i("UniSDK UniSdkUtils", "StrUtil [getLocalLanguage] use systemLanguage");
            }
        } else {
            systemLanguage = null;
        }
        i("UniSDK UniSdkUtils", "StrUtil [getLocalLanguage] finalLanguage=".concat(String.valueOf(systemLanguage)));
        return systemLanguage;
    }

    /* JADX WARN: Removed duplicated region for block: B:125:0x0062 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:129:0x006c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:142:? A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String getCpuMhz() throws java.lang.Throwable {
        /*
            r0 = 0
            java.io.FileReader r1 = new java.io.FileReader     // Catch: java.lang.Throwable -> L3c java.lang.Exception -> L41
            java.lang.String r2 = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"
            r1.<init>(r2)     // Catch: java.lang.Throwable -> L3c java.lang.Exception -> L41
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L32 java.lang.Exception -> L37
            r2.<init>(r1)     // Catch: java.lang.Throwable -> L32 java.lang.Exception -> L37
            java.lang.String r0 = r2.readLine()     // Catch: java.lang.Exception -> L30 java.lang.Throwable -> L5f
            java.lang.String r0 = r0.trim()     // Catch: java.lang.Exception -> L30 java.lang.Throwable -> L5f
            int r0 = java.lang.Integer.parseInt(r0)     // Catch: java.lang.Exception -> L30 java.lang.Throwable -> L5f
            int r0 = r0 / 1000
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch: java.lang.Exception -> L30 java.lang.Throwable -> L5f
            r1.close()     // Catch: java.io.IOException -> L23
            goto L27
        L23:
            r1 = move-exception
            r1.printStackTrace()
        L27:
            r2.close()     // Catch: java.io.IOException -> L2b
            goto L5e
        L2b:
            r1 = move-exception
            r1.printStackTrace()
            goto L5e
        L30:
            r0 = move-exception
            goto L45
        L32:
            r2 = move-exception
            r3 = r2
            r2 = r0
            r0 = r3
            goto L60
        L37:
            r2 = move-exception
            r3 = r2
            r2 = r0
            r0 = r3
            goto L45
        L3c:
            r1 = move-exception
            r2 = r0
            r0 = r1
            r1 = r2
            goto L60
        L41:
            r1 = move-exception
            r2 = r0
            r0 = r1
            r1 = r2
        L45:
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L5f
            if (r1 == 0) goto L52
            r1.close()     // Catch: java.io.IOException -> L4e
            goto L52
        L4e:
            r0 = move-exception
            r0.printStackTrace()
        L52:
            if (r2 == 0) goto L5c
            r2.close()     // Catch: java.io.IOException -> L58
            goto L5c
        L58:
            r0 = move-exception
            r0.printStackTrace()
        L5c:
            java.lang.String r0 = "0"
        L5e:
            return r0
        L5f:
            r0 = move-exception
        L60:
            if (r1 == 0) goto L6a
            r1.close()     // Catch: java.io.IOException -> L66
            goto L6a
        L66:
            r1 = move-exception
            r1.printStackTrace()
        L6a:
            if (r2 == 0) goto L74
            r2.close()     // Catch: java.io.IOException -> L70
            goto L74
        L70:
            r1 = move-exception
            r1.printStackTrace()
        L74:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.UniSdkUtils.getCpuMhz():java.lang.String");
    }

    /* JADX WARN: Removed duplicated region for block: B:125:0x0062 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:129:0x006c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:142:? A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String getCpuCore() throws java.lang.Throwable {
        /*
            r0 = 0
            java.io.FileReader r1 = new java.io.FileReader     // Catch: java.lang.Throwable -> L3c java.lang.Exception -> L41
            java.lang.String r2 = "/sys/devices/system/cpu/kernel_max"
            r1.<init>(r2)     // Catch: java.lang.Throwable -> L3c java.lang.Exception -> L41
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L32 java.lang.Exception -> L37
            r2.<init>(r1)     // Catch: java.lang.Throwable -> L32 java.lang.Exception -> L37
            java.lang.String r0 = r2.readLine()     // Catch: java.lang.Exception -> L30 java.lang.Throwable -> L5f
            java.lang.String r0 = r0.trim()     // Catch: java.lang.Exception -> L30 java.lang.Throwable -> L5f
            int r0 = java.lang.Integer.parseInt(r0)     // Catch: java.lang.Exception -> L30 java.lang.Throwable -> L5f
            int r0 = r0 + 1
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch: java.lang.Exception -> L30 java.lang.Throwable -> L5f
            r1.close()     // Catch: java.io.IOException -> L23
            goto L27
        L23:
            r1 = move-exception
            r1.printStackTrace()
        L27:
            r2.close()     // Catch: java.io.IOException -> L2b
            goto L5e
        L2b:
            r1 = move-exception
            r1.printStackTrace()
            goto L5e
        L30:
            r0 = move-exception
            goto L45
        L32:
            r2 = move-exception
            r3 = r2
            r2 = r0
            r0 = r3
            goto L60
        L37:
            r2 = move-exception
            r3 = r2
            r2 = r0
            r0 = r3
            goto L45
        L3c:
            r1 = move-exception
            r2 = r0
            r0 = r1
            r1 = r2
            goto L60
        L41:
            r1 = move-exception
            r2 = r0
            r0 = r1
            r1 = r2
        L45:
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L5f
            if (r1 == 0) goto L52
            r1.close()     // Catch: java.io.IOException -> L4e
            goto L52
        L4e:
            r0 = move-exception
            r0.printStackTrace()
        L52:
            if (r2 == 0) goto L5c
            r2.close()     // Catch: java.io.IOException -> L58
            goto L5c
        L58:
            r0 = move-exception
            r0.printStackTrace()
        L5c:
            java.lang.String r0 = "0"
        L5e:
            return r0
        L5f:
            r0 = move-exception
        L60:
            if (r1 == 0) goto L6a
            r1.close()     // Catch: java.io.IOException -> L66
            goto L6a
        L66:
            r1 = move-exception
            r1.printStackTrace()
        L6a:
            if (r2 == 0) goto L74
            r2.close()     // Catch: java.io.IOException -> L70
            goto L74
        L70:
            r1 = move-exception
            r1.printStackTrace()
        L74:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.UniSdkUtils.getCpuCore():java.lang.String");
    }

    public static String getMobileModel() {
        return Build.MODEL;
    }

    public static String getMobileModel2() {
        return Build.MANUFACTURER + "#" + Build.MODEL + "#" + getCpuName() + "#" + getCpuCore() + "#" + getCpuMhz();
    }

    public static int getMobileSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static String getMobileVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String getMobileManufacturer() {
        return Build.MANUFACTURER;
    }

    public static String getMobildBrand() {
        return Build.BRAND;
    }

    public static String getAppPackageName(Context context) {
        return context != null ? context.getPackageName() : "";
    }

    public static int getAppVersionCode(Context context) {
        if (SdkMgr.getInst() == null) {
            ModulesManager.getInst().init(context);
        }
        try {
            return Integer.parseInt(ModulesManager.getInst().extendFunc(ConstProp.UNISDKBASE, "deviceInfo", "{\"methodId\":\"getAppVersionCode\"}"));
        } catch (Exception e2) {
            e2.printStackTrace();
            return -1;
        }
    }

    public static String getAppVersionName(Context context) {
        if (SdkMgr.getInst() == null) {
            ModulesManager.getInst().init(context);
        }
        return ModulesManager.getInst().extendFunc(ConstProp.UNISDKBASE, "deviceInfo", "{\"methodId\":\"getAppVersionName\"}");
    }

    public static String getMacAddress(Context context) {
        return ModulesManager.getInst().extendFunc(ConstProp.UNISDKBASE, "deviceInfo", "{\"methodId\":\"getMacAddress\"}");
    }

    public static String getMobileIMSI(Context context) {
        return ModulesManager.getInst().extendFunc(ConstProp.UNISDKBASE, "deviceInfo", "{\"methodId\":\"getImsi\"}");
    }

    public static boolean isIPv4(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return b.matcher(str).matches();
    }

    public static String getMobileIMEI(Context context) {
        return ModulesManager.getInst().extendFunc(ConstProp.UNISDKBASE, "deviceInfo", "{\"methodId\":\"getImei\"}");
    }

    public static boolean isEmulator(Context context) {
        int propInt = SdkMgr.getInst() == null ? -1 : SdkMgr.getInst().getPropInt("EmulatorDetectResult", -1);
        if (-1 != propInt) {
            return 1 == propInt;
        }
        if (SdkMgr.getInst() == null || "-1".equals(SdkMgr.getInst().getPropStr("IS_EMULATOR", "-1"))) {
            return EmulatorDetector.detect(context);
        }
        return SdkMgr.getInst().getPropInt("IS_EMULATOR", 0) == 1;
    }

    public static boolean isMuMu() {
        String str = Build.ID;
        if (str != null) {
            return str.equalsIgnoreCase("V417IR") || str.equalsIgnoreCase("W528JS") || str.equalsIgnoreCase("X639KT");
        }
        return false;
    }

    public static boolean isDsEmulator() {
        String str = Build.ID;
        return str != null && str.equalsIgnoreCase("DS314A");
    }

    /* renamed from: com.netease.ntunisdk.base.UniSdkUtils$1 */
    static class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            Process processExec = null;
            try {
                processExec = Runtime.getRuntime().exec(new String[]{"/system/xbin/which", ShellAdbUtils.COMMAND_SU});
                i = new BufferedReader(new InputStreamReader(processExec.getInputStream())).readLine() != null ? 1 : 0;
            } catch (Exception unused) {
                if (processExec != null) {
                }
            } catch (Throwable th) {
                if (processExec != null) {
                    processExec.destroy();
                }
                throw th;
            }
            if (processExec != null) {
                processExec.destroy();
            }
            if (SdkMgr.getInst() != null) {
                SdkMgr.getInst().setPropInt(ConstProp.UNISDKUTILS_IS_DEVICEROOTED3, i);
            }
        }
    }

    static void b() {
        CachedThreadPoolUtil.getInstance().exec(new Runnable() { // from class: com.netease.ntunisdk.base.UniSdkUtils.1
            AnonymousClass1() {
            }

            @Override // java.lang.Runnable
            public final void run() {
                Process processExec = null;
                try {
                    processExec = Runtime.getRuntime().exec(new String[]{"/system/xbin/which", ShellAdbUtils.COMMAND_SU});
                    i = new BufferedReader(new InputStreamReader(processExec.getInputStream())).readLine() != null ? 1 : 0;
                } catch (Exception unused) {
                    if (processExec != null) {
                    }
                } catch (Throwable th) {
                    if (processExec != null) {
                        processExec.destroy();
                    }
                    throw th;
                }
                if (processExec != null) {
                    processExec.destroy();
                }
                if (SdkMgr.getInst() != null) {
                    SdkMgr.getInst().setPropInt(ConstProp.UNISDKUTILS_IS_DEVICEROOTED3, i);
                }
            }
        });
    }

    public static boolean isNetworkAvailable(Context context) {
        return context != null && NetConnectivity.isConnected(context);
    }

    public static boolean isWifiConnect(Context context) {
        if (context != null) {
            return NetConnectivity.isConnectedWifi(context);
        }
        return false;
    }

    public static String getAppName(Context context) {
        if (context == null) {
            return "";
        }
        try {
            return context.getString(context.getApplicationInfo().labelRes);
        } catch (Exception e2) {
            e2.printStackTrace();
            return "";
        }
    }

    public static int getAppIconResId(Context context) {
        if (context != null) {
            return context.getApplicationInfo().labelRes;
        }
        return 0;
    }

    public static int[] getDisplayPixels(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (context == null) {
            return new int[]{0, 0};
        }
        try {
            ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getRealMetrics(displayMetrics);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return new int[]{displayMetrics.widthPixels, displayMetrics.heightPixels};
    }

    static synchronized String a(Context context, int i2, int i3) {
        int iIntValue = 0;
        if (context != null) {
            try {
                if (i2 == 0) {
                    if (i3 == 0) {
                        if (-1 != k) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(k);
                            return sb.toString();
                        }
                        iIntValue = ((Integer) Class.forName("com.netease.ntunisdk.base.Checker").getDeclaredMethod("getRandom", Object.class, Integer.TYPE).invoke(null, context, 0)).intValue();
                        k = iIntValue;
                    } else if (1 == i3) {
                        iIntValue = ((Integer) Class.forName("com.netease.ntunisdk.base.Checker").getDeclaredMethod("getRandom", Object.class, Integer.TYPE).invoke(null, context, 1)).intValue();
                    }
                } else if (1 == i2) {
                    if (-1 != l) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(l);
                        return sb2.toString();
                    }
                    iIntValue = ((Integer) Class.forName("com.random.getrandom").getDeclaredMethod("getran", Context.class).invoke(null, context)).intValue();
                    l = iIntValue;
                }
            } catch (Throwable th) {
                w("UniSDK UniSdkUtils", "getSeed: " + th.getMessage());
            }
        }
        return String.valueOf(iIntValue);
    }

    public static boolean isTablet(Context context) {
        if (context == null) {
            return false;
        }
        return context.getResources().getBoolean(context.getResources().getIdentifier("unisdk_common_isTablet", "bool", context.getPackageName()));
    }

    /* renamed from: com.netease.ntunisdk.base.UniSdkUtils$2 */
    static class AnonymousClass2 implements LocationListener {
        AnonymousClass2() {
        }

        @Override // android.location.LocationListener
        public final void onLocationChanged(Location location) {
            UniSdkUtils.d("UniSDK UniSdkUtils", "onLocationChanged: ".concat(String.valueOf(location)));
        }

        @Override // android.location.LocationListener
        public final void onStatusChanged(String str, int i2, Bundle bundle) {
            UniSdkUtils.d("UniSDK UniSdkUtils", "onStatusChanged: " + str + "/" + i2);
        }

        @Override // android.location.LocationListener
        public final void onProviderEnabled(String str) {
            UniSdkUtils.d("UniSDK UniSdkUtils", "onProviderEnabled: ".concat(String.valueOf(str)));
        }

        @Override // android.location.LocationListener
        public final void onProviderDisabled(String str) {
            UniSdkUtils.d("UniSDK UniSdkUtils", "onProviderDisabled: ".concat(String.valueOf(str)));
        }
    }

    public static String getDeviceUDID(Context context) {
        return a(context, SdkMgr.getInst());
    }

    static String a(Context context, GamerInterface gamerInterface) {
        boolean z = gamerInterface != null && gamerInterface.hasFeature("NO_ANDROIDID");
        i("UniSDK UniSdkUtils", "toGetGaid: ".concat(String.valueOf(z)));
        if (gamerInterface == null) {
            ModulesManager.getInst().init(context);
        }
        String strExtendFunc = ModulesManager.getInst().extendFunc(ConstProp.UNISDKBASE, "deviceInfo", String.format("{\"methodId\":\"getUDID\",\"isNoAndroidId\":%s}", Boolean.valueOf(z)));
        return (!TextUtils.isEmpty(strExtendFunc) || ModulesManager.getInst().hasModule("deviceInfo") || SdkMgr.getInst() == null) ? strExtendFunc : DeviceUtils.getDeviceUDID(context, SdkMgr.getInst().hasFeature("NO_ANDROIDID"), SdkMgr.getInst().hasFeature("ENABLE_FAKE_ABOUT_ID"));
    }

    static String b(Context context, GamerInterface gamerInterface) {
        boolean z = gamerInterface != null && gamerInterface.hasFeature("NO_ANDROIDID");
        i("UniSDK UniSdkUtils", "toGetGaid: ".concat(String.valueOf(z)));
        if (gamerInterface == null) {
            ModulesManager.getInst().init(context);
        }
        String strExtendFunc = ModulesManager.getInst().extendFunc(ConstProp.UNISDKBASE, "deviceInfo", String.format("{\"methodId\":\"getUDID\",\"isNoAndroidId\":%s,\"isFake\":%s}", Boolean.valueOf(z), Boolean.FALSE));
        return (!TextUtils.isEmpty(strExtendFunc) || ModulesManager.getInst().hasModule("deviceInfo") || SdkMgr.getInst() == null) ? strExtendFunc : DeviceUtils.getDeviceUDID(context, z, false);
    }

    public static String getAndroidId(Context context) {
        return ModulesManager.getInst().extendFunc(ConstProp.UNISDKBASE, "deviceInfo", "{\"methodId\":\"getAndroidId\"}");
    }

    public static String getFirstDeviceId(Context context) {
        if (SdkMgr.getInst() == null) {
            ModulesManager.getInst().init(context);
        }
        return ModulesManager.getInst().extendFunc(ConstProp.UNISDKBASE, "deviceInfo", "{\"methodId\":\"getFirstDeviceId\"}");
    }

    public static String getUnisdkDeviceId(Context context, GaidCallback gaidCallback) {
        GamerInterface inst = SdkMgr.getInst();
        boolean z = inst != null && inst.hasFeature("NO_ANDROIDID");
        if (inst == null) {
            ModulesManager.getInst().init(context);
        }
        String strExtendFunc = ModulesManager.getInst().extendFunc(ConstProp.UNISDKBASE, "deviceInfo", String.format("{\"methodId\":\"getUnisdkDeviceId\",\"isNoAndroidId\":%s}", Boolean.valueOf(z)));
        return (!TextUtils.isEmpty(strExtendFunc) || ModulesManager.getInst().hasModule("deviceInfo") || SdkMgr.getInst() == null) ? strExtendFunc : DeviceUtils.getUnisdkDeviceId(context, z, SdkMgr.getInst().hasFeature("ENABLE_FAKE_ABOUT_ID"));
    }

    static String c(Context context, GamerInterface gamerInterface) {
        boolean z = gamerInterface != null && gamerInterface.hasFeature("NO_ANDROIDID");
        if (gamerInterface == null) {
            ModulesManager.getInst().init(context);
        }
        String strExtendFunc = ModulesManager.getInst().extendFunc(ConstProp.UNISDKBASE, "deviceInfo", String.format("{\"methodId\":\"getUnisdkDeviceId\",\"isNoAndroidId\":%s,\"isFake\":%s}", Boolean.valueOf(z), Boolean.FALSE));
        return (!TextUtils.isEmpty(strExtendFunc) || ModulesManager.getInst().hasModule("deviceInfo") || gamerInterface == null) ? strExtendFunc : DeviceUtils.getUnisdkDeviceId(context, z, false);
    }

    public static String getUnisdkDeviceId(Context context) {
        return getUnisdkDeviceId(context, null);
    }

    public static String getOAID(Context context) {
        if (SdkMgr.getInst() == null) {
            return "";
        }
        if (!SdkMgr.getInst().hasFeature("EB")) {
            return SdkMgr.getInst().getPropStr(ConstProp.MSA_OAID, "");
        }
        if (SdkMgr.getInst() == null) {
            ModulesManager.getInst().init(context);
        }
        return ModulesManager.getInst().extendFunc(ConstProp.UNISDKBASE, "deviceInfo", "{\"methodId\":\"getUDID\",\"isNoAndroidId\":true}");
    }

    public static String ntGetNetworktype(Context context) {
        return ModulesManager.getInst().extendFunc(ConstProp.UNISDKBASE, "deviceInfo", "{\"methodId\":\"ntGetNetworktype\"}");
    }

    public static String[] getRamMemory(Context context) {
        String[] strArr = {"0.0", "0.0"};
        try {
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        if (context == null) {
            d("DCTOOL", "\u68c0\u67e5\u4e0b\u521d\u59cb\u5316\u65f6\u4f20\u5165\u7684AppContext\u662f\u5426\u4e3anull\uff0c\u4e00\u822c\u662f\u5728\u8c03\u7528\u65f6\u95f4\u8fc7\u65e9\u5bfc\u81f4AppContext\u4e3anull\u5bfc\u81f4\u7684\u3002\u8fd9\u79cd\u60c5\u51b5\u53ef\u4ee5\u5ffd\u7565\uff1b\u6216\u8005\u8c03\u6574\u4e0b\u521d\u59cb\u5316\u7684\u8c03\u7528\u65f6\u673a\uff1b\u6216\u8005\u589e\u52a0\u5224\u65ad\uff0c\u5728AppContext\u4e3anull\u7684\u65f6\u5019\uff0c\u4e0d\u8981\u8c03\u7528\u8bca\u65ad\u3002");
            return strArr;
        }
        ((ActivityManager) context.getSystemService("activity")).getMemoryInfo(new ActivityManager.MemoryInfo());
        float f2 = (r2.totalMem / 1024.0f) / 1024.0f;
        strArr[0] = String.format(Locale.US, "%.1f", Float.valueOf(f2));
        strArr[1] = String.format(Locale.US, "%.1f", Float.valueOf((r2.availMem / 1024.0f) / 1024.0f));
        return strArr;
    }

    /* JADX WARN: Code restructure failed: missing block: B:120:0x0027, code lost:
    
        r0 = r0[1];
     */
    /* JADX WARN: Code restructure failed: missing block: B:121:0x002a, code lost:
    
        r1.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:123:0x002e, code lost:
    
        r1 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:124:0x002f, code lost:
    
        r1.printStackTrace();
     */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:171:0x008f -> B:190:0x0092). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String ntGetCpuName() throws java.lang.Throwable {
        /*
            r0 = 0
            java.io.FileReader r1 = new java.io.FileReader     // Catch: java.lang.Throwable -> L5b java.io.IOException -> L60 java.io.FileNotFoundException -> L77
            java.lang.String r2 = "/proc/cpuinfo"
            r1.<init>(r2)     // Catch: java.lang.Throwable -> L5b java.io.IOException -> L60 java.io.FileNotFoundException -> L77
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L4c java.io.IOException -> L51 java.io.FileNotFoundException -> L56
            r2.<init>(r1)     // Catch: java.lang.Throwable -> L4c java.io.IOException -> L51 java.io.FileNotFoundException -> L56
        Ld:
            java.lang.String r0 = r2.readLine()     // Catch: java.io.IOException -> L48 java.io.FileNotFoundException -> L4a java.lang.Throwable -> L95
            if (r0 == 0) goto L3b
            java.lang.String r3 = ":\\s+"
            r4 = 2
            java.lang.String[] r0 = r0.split(r3, r4)     // Catch: java.io.IOException -> L48 java.io.FileNotFoundException -> L4a java.lang.Throwable -> L95
            r3 = 0
            r3 = r0[r3]     // Catch: java.io.IOException -> L48 java.io.FileNotFoundException -> L4a java.lang.Throwable -> L95
            if (r3 == 0) goto Ld
            java.lang.String r4 = "Hardware"
            boolean r3 = r3.startsWith(r4)     // Catch: java.io.IOException -> L48 java.io.FileNotFoundException -> L4a java.lang.Throwable -> L95
            if (r3 == 0) goto Ld
            r3 = 1
            r0 = r0[r3]     // Catch: java.io.IOException -> L48 java.io.FileNotFoundException -> L4a java.lang.Throwable -> L95
            r1.close()     // Catch: java.io.IOException -> L2e
            goto L32
        L2e:
            r1 = move-exception
            r1.printStackTrace()
        L32:
            r2.close()     // Catch: java.io.IOException -> L36
            goto L3a
        L36:
            r1 = move-exception
            r1.printStackTrace()
        L3a:
            return r0
        L3b:
            r1.close()     // Catch: java.io.IOException -> L3f
            goto L43
        L3f:
            r0 = move-exception
            r0.printStackTrace()
        L43:
            r2.close()     // Catch: java.io.IOException -> L8e
            goto L92
        L48:
            r0 = move-exception
            goto L64
        L4a:
            r0 = move-exception
            goto L7b
        L4c:
            r2 = move-exception
            r5 = r2
            r2 = r0
            r0 = r5
            goto L96
        L51:
            r2 = move-exception
            r5 = r2
            r2 = r0
            r0 = r5
            goto L64
        L56:
            r2 = move-exception
            r5 = r2
            r2 = r0
            r0 = r5
            goto L7b
        L5b:
            r1 = move-exception
            r2 = r0
            r0 = r1
            r1 = r2
            goto L96
        L60:
            r1 = move-exception
            r2 = r0
            r0 = r1
            r1 = r2
        L64:
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L95
            if (r1 == 0) goto L71
            r1.close()     // Catch: java.io.IOException -> L6d
            goto L71
        L6d:
            r0 = move-exception
            r0.printStackTrace()
        L71:
            if (r2 == 0) goto L92
            r2.close()     // Catch: java.io.IOException -> L8e
            goto L92
        L77:
            r1 = move-exception
            r2 = r0
            r0 = r1
            r1 = r2
        L7b:
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L95
            if (r1 == 0) goto L88
            r1.close()     // Catch: java.io.IOException -> L84
            goto L88
        L84:
            r0 = move-exception
            r0.printStackTrace()
        L88:
            if (r2 == 0) goto L92
            r2.close()     // Catch: java.io.IOException -> L8e
            goto L92
        L8e:
            r0 = move-exception
            r0.printStackTrace()
        L92:
            java.lang.String r0 = "unknown"
            return r0
        L95:
            r0 = move-exception
        L96:
            if (r1 == 0) goto La0
            r1.close()     // Catch: java.io.IOException -> L9c
            goto La0
        L9c:
            r1 = move-exception
            r1.printStackTrace()
        La0:
            if (r2 == 0) goto Laa
            r2.close()     // Catch: java.io.IOException -> La6
            goto Laa
        La6:
            r1 = move-exception
            r1.printStackTrace()
        Laa:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.UniSdkUtils.ntGetCpuName():java.lang.String");
    }

    public static String getCurCpuFreq() throws Throwable {
        BufferedReader bufferedReader;
        FileReader fileReader;
        BufferedReader bufferedReader2;
        IOException e2;
        FileNotFoundException e3;
        try {
            try {
                try {
                    fileReader = new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
                } catch (FileNotFoundException e4) {
                    bufferedReader2 = null;
                    e3 = e4;
                    fileReader = null;
                } catch (IOException e5) {
                    bufferedReader2 = null;
                    e2 = e5;
                    fileReader = null;
                } catch (Throwable th) {
                    bufferedReader = null;
                    th = th;
                    fileReader = null;
                }
                try {
                    bufferedReader2 = new BufferedReader(fileReader);
                    try {
                        String strTrim = bufferedReader2.readLine().trim();
                        try {
                            fileReader.close();
                        } catch (IOException e6) {
                            e6.printStackTrace();
                        }
                        try {
                            bufferedReader2.close();
                            return strTrim;
                        } catch (IOException e7) {
                            e7.printStackTrace();
                            return strTrim;
                        }
                    } catch (FileNotFoundException e8) {
                        e3 = e8;
                        e3.printStackTrace();
                        if (fileReader != null) {
                            try {
                                fileReader.close();
                            } catch (IOException e9) {
                                e9.printStackTrace();
                            }
                        }
                        if (bufferedReader2 != null) {
                            bufferedReader2.close();
                        }
                        return "0";
                    } catch (IOException e10) {
                        e2 = e10;
                        e2.printStackTrace();
                        if (fileReader != null) {
                            try {
                                fileReader.close();
                            } catch (IOException e11) {
                                e11.printStackTrace();
                            }
                        }
                        if (bufferedReader2 != null) {
                            bufferedReader2.close();
                        }
                        return "0";
                    }
                } catch (FileNotFoundException e12) {
                    bufferedReader2 = null;
                    e3 = e12;
                } catch (IOException e13) {
                    bufferedReader2 = null;
                    e2 = e13;
                } catch (Throwable th2) {
                    bufferedReader = null;
                    th = th2;
                    if (fileReader != null) {
                        try {
                            fileReader.close();
                        } catch (IOException e14) {
                            e14.printStackTrace();
                        }
                    }
                    if (bufferedReader == null) {
                        throw th;
                    }
                    try {
                        bufferedReader.close();
                        throw th;
                    } catch (IOException e15) {
                        e15.printStackTrace();
                        throw th;
                    }
                }
            } catch (IOException e16) {
                e16.printStackTrace();
                return "0";
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }

    public static String getSystemTimeZone() {
        try {
            return TimeZone.getDefault().getID();
        } catch (Exception e2) {
            e2.printStackTrace();
            return "unknown";
        }
    }

    public static boolean isDomestic(Context context) {
        return !GaidUtils.hasInstalledGooglePlayServices(context);
    }

    private static void a(JSONObject jSONObject, List<JSONObject> list, List<String> list2) throws Exception {
        Iterator<String> itKeys = jSONObject.keys();
        while (itKeys.hasNext()) {
            String next = itKeys.next();
            Object obj = jSONObject.get(next);
            if (obj instanceof JSONObject) {
                a((JSONObject) obj, list, list2);
            } else if ("ip".equalsIgnoreCase(next) || "aim_info".equalsIgnoreCase(next)) {
                d("UniSDK UniSdkUtils", "remove " + next + " = " + obj);
                list.add(jSONObject);
                list2.add(next);
            }
        }
    }

    public static void traverseJSONObject2removeIP(JSONObject jSONObject) throws JSONException {
        try {
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            a(jSONObject, arrayList, arrayList2);
            int size = arrayList.size();
            for (int i2 = 0; i2 < size; i2++) {
                JSONObject jSONObject2 = (JSONObject) arrayList.get(i2);
                String str = (String) arrayList2.get(i2);
                if ("aim_info".equalsIgnoreCase(str)) {
                    JSONObject jSONObject3 = new JSONObject(jSONObject2.getString(str));
                    if (jSONObject3.has("aim")) {
                        jSONObject3.remove("aim");
                        d("UniSDK UniSdkUtils", "remove aim in aim_info");
                    }
                    jSONObject2.put("aim_info", jSONObject3.toString());
                } else {
                    jSONObject2.remove(str);
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static boolean showAASDialog(String str, String str2) {
        if (SdkMgr.getInst() == null || !(SdkMgr.getInst() instanceof SdkBase)) {
            return false;
        }
        return ((SdkBase) SdkMgr.getInst()).showAASDialog(str, str2);
    }

    public static String getSimOperator(Context context) {
        TelephonyManager telephonyManager;
        if (context == null || (telephonyManager = (TelephonyManager) context.getApplicationContext().getSystemService("phone")) == null) {
            return null;
        }
        return telephonyManager.getSimOperator();
    }

    public static boolean isVpnRunning(Context context) {
        ConnectivityManager connectivityManager;
        NetworkInfo networkInfo;
        return (context == null || (connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService("connectivity")) == null || (networkInfo = connectivityManager.getNetworkInfo(17)) == null || !networkInfo.isConnected()) ? false : true;
    }

    /* JADX WARN: Removed duplicated region for block: B:54:0x0046  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean isDeviceRooted() {
        /*
            java.lang.String r0 = android.os.Build.TAGS
            r1 = 1
            r2 = 0
            if (r0 == 0) goto L10
            java.lang.String r3 = "test-keys"
            boolean r0 = r0.contains(r3)
            if (r0 == 0) goto L10
            r0 = r1
            goto L11
        L10:
            r0 = r2
        L11:
            if (r0 != 0) goto L46
            java.lang.String r3 = "/system/app/Superuser.apk"
            java.lang.String r4 = "/sbin/su"
            java.lang.String r5 = "/system/bin/su"
            java.lang.String r6 = "/system/xbin/su"
            java.lang.String r7 = "/data/local/xbin/su"
            java.lang.String r8 = "/data/local/bin/su"
            java.lang.String r9 = "/system/sd/xbin/su"
            java.lang.String r10 = "/system/bin/failsafe/su"
            java.lang.String r11 = "/data/local/su"
            java.lang.String[] r0 = new java.lang.String[]{r3, r4, r5, r6, r7, r8, r9, r10, r11}
            r3 = r2
        L2a:
            r4 = 9
            if (r3 >= r4) goto L40
            r4 = r0[r3]
            java.io.File r5 = new java.io.File
            r5.<init>(r4)
            boolean r4 = r5.exists()
            if (r4 == 0) goto L3d
            r0 = r1
            goto L41
        L3d:
            int r3 = r3 + 1
            goto L2a
        L40:
            r0 = r2
        L41:
            if (r0 == 0) goto L44
            goto L46
        L44:
            r0 = r2
            goto L47
        L46:
            r0 = r1
        L47:
            com.netease.ntunisdk.base.GamerInterface r3 = com.netease.ntunisdk.base.SdkMgr.getInst()
            if (r3 == 0) goto L5b
            com.netease.ntunisdk.base.GamerInterface r3 = com.netease.ntunisdk.base.SdkMgr.getInst()
            java.lang.String r4 = "UNISDKUTILS_IS_DEVICEROOTED3"
            int r3 = r3.getPropInt(r4, r2)
            if (r1 != r3) goto L5b
            r3 = r1
            goto L5c
        L5b:
            r3 = r2
        L5c:
            if (r0 != 0) goto L62
            if (r3 == 0) goto L61
            goto L62
        L61:
            return r2
        L62:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.UniSdkUtils.isDeviceRooted():boolean");
    }
}