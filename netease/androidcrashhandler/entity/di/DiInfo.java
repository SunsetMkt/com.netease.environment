package com.netease.androidcrashhandler.entity.di;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.netease.androidcrashhandler.AndroidCrashHandler;
import com.netease.androidcrashhandler.NTCrashHunterKit;
import com.netease.androidcrashhandler.entity.Extension.ExtensionInfo;
import com.netease.androidcrashhandler.init.InitProxy;
import com.netease.androidcrashhandler.so.SystemSoHandler;
import com.netease.androidcrashhandler.thirdparty.lifecycle.Lifecycle;
import com.netease.androidcrashhandler.thirdparty.unilogger.CUniLogger;
import com.netease.androidcrashhandler.unknownCrash.MemoryManager;
import com.netease.androidcrashhandler.unknownCrash.MemoryStatus;
import com.netease.androidcrashhandler.util.CEmulatorDetector;
import com.netease.androidcrashhandler.util.CUtil;
import com.netease.androidcrashhandler.util.LogUtils;
import com.netease.androidcrashhandler.util.RomNameUtil;
import com.netease.ntunisdk.modules.api.ModulesManager;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import com.netease.ntunisdk.unilogger.global.Const;
import com.xiaomi.gamecenter.sdk.web.webview.webkit.h;
import com.xiaomi.mipush.sdk.Constants;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Marker;

/* loaded from: classes4.dex */
public class DiInfo {
    private static final String TAG = "DiInfo";
    private static JSONObject mChannelInfosJson = null;
    public static String sCurFileName = "crashhunter.di";
    public static String sPreFileName = "crashhunter_pre.di";
    private Context mContext;
    private int QUEUE_MAX = 100;
    private JSONObject mDiInfoJson = new JSONObject();
    private BlockingQueue<DiInfoUnit> mBatteryLevelInfo = new LinkedBlockingQueue(this.QUEUE_MAX);
    private BlockingQueue<DiInfoUnit> mBatteryTemperatureInfo = new LinkedBlockingQueue(this.QUEUE_MAX);
    private BlockingQueue<DiInfoUnit> mAppMem = new LinkedBlockingQueue(this.QUEUE_MAX);
    private BlockingQueue<DiInfoUnit> mAppFreeMem = new LinkedBlockingQueue(this.QUEUE_MAX);
    private BlockingQueue<DiInfoUnit> mAppAvlMem = new LinkedBlockingQueue(this.QUEUE_MAX);
    private BlockingQueue<DiInfoUnit> mTotalMem = new LinkedBlockingQueue(this.QUEUE_MAX);
    private BlockingQueue<DiInfoUnit> mAvlMem = new LinkedBlockingQueue(this.QUEUE_MAX);
    private JSONObject mTInfoJson = new JSONObject();
    private boolean isFirstFresh = true;
    private boolean mHasSetLaunchTime = false;
    private boolean mIsFreshing = false;
    private ViewGroup mViewGroup = null;
    private GLSurfaceView mGlView = null;
    private boolean mInitExecTopProcess = false;
    private double mProcessCpuRate = 0.0d;
    private String mSoLoadingType = "";

    public DiInfo(Context context) {
        this.mContext = context;
    }

    public synchronized void writeToLocalFile() {
        LogUtils.i(LogUtils.TAG, "DiInfo [freshToLocalFile] start");
        LogUtils.i(LogUtils.TAG, "DiInfo [freshToLocalFile] mParamsJson=" + this.mDiInfoJson.toString());
        CUtil.str2File(this.mDiInfoJson.toString(), InitProxy.sUploadFilePath, sCurFileName);
    }

    public synchronized void freshSecureData() {
        LogUtils.i(LogUtils.TAG, "DiInfo [freshSecureData] start");
        try {
            setTime();
            setVersionInfo();
            setBasicInfo();
            setNetInfo();
            setGPUInfo();
            setScreenInfo();
            setBatteryInfo();
            setExtensionInfos();
            setOtherDynamicInfo();
            LogUtils.i(LogUtils.TAG, "DiInfo [freshSecureData] finish");
        } catch (Exception e) {
            LogUtils.w(LogUtils.TAG, "DiInfo [freshSecureData] Exception=" + e.toString());
            e.printStackTrace();
        }
    }

    public synchronized void fresh() {
        LogUtils.i(LogUtils.TAG, "DiInfo [fresh] start");
        try {
            LogUtils.i(LogUtils.TAG, "DiInfo [fresh] mIsFreshing=" + this.mIsFreshing);
            if (!this.mIsFreshing) {
                this.mIsFreshing = true;
                LogUtils.i(LogUtils.TAG, "DiInfo [fresh] isFirstFresh=" + this.isFirstFresh);
                if (this.isFirstFresh) {
                    setVersionInfo();
                    setBasicInfo();
                    setUniqueId();
                    setMemoryInfo();
                    setNetInfo();
                    setGPUInfo();
                    setScreenInfo();
                    setBatteryInfo();
                    setOtherDynamicInfo();
                    setExtensionInfos();
                    this.isFirstFresh = false;
                } else {
                    setMemoryInfo();
                    setScreenInfo();
                    setNetInfo();
                    setTime();
                    setBatteryInfo();
                    setOtherDynamicInfo();
                    setExtensionInfos();
                    SystemSoHandler.getInstance().uploadSystemSo(this.mContext);
                }
                this.mIsFreshing = false;
            } else {
                LogUtils.i(LogUtils.TAG, "DiInfo [fresh] \u5df2\u7ecf\u5904\u4e8e\u5237\u65b0\u8fc7\u7a0b\u4e2d");
            }
        } catch (Exception e) {
            LogUtils.w(LogUtils.TAG, "DiInfo [fresh] Exception=" + e.toString());
            e.printStackTrace();
        }
    }

    private void setExtensionInfos() throws JSONException {
        LogUtils.i(LogUtils.TAG, "DiInfo [setExtensionInfos] start");
        JSONObject result = ExtensionInfo.getInstance().getResult();
        if (result == null || result.length() <= 0) {
            LogUtils.i(LogUtils.TAG, "DiInfo [setExtensionInfos] param error");
            this.mDiInfoJson.remove("filter_pipe");
        } else {
            LogUtils.i(LogUtils.TAG, "DiInfo [setExtensionInfos] extensionInfo=" + result.toString());
            putDiInfo("filter_pipe", ExtensionInfo.getInstance().getResult());
        }
    }

    public void putDiInfo(String str, Object obj) throws JSONException {
        LogUtils.i(LogUtils.TAG, "DiInfo [putDiInfo] start");
        if (TextUtils.isEmpty(str) || obj == null) {
            LogUtils.i(LogUtils.TAG, "DiInfo [putDiInfo] param error");
            return;
        }
        try {
            this.mDiInfoJson.put(str, obj);
        } catch (JSONException e) {
            LogUtils.w(LogUtils.TAG, "DiInfo [putDiInfo] JSONException=" + e.toString());
            e.printStackTrace();
        } catch (Exception e2) {
            LogUtils.w(LogUtils.TAG, "DiInfo [putDiInfo] Exception=" + e2.toString());
            e2.printStackTrace();
        }
    }

    private String getBundleIdentifier() {
        LogUtils.i(LogUtils.TAG, "DiInfo [getBundleIdentifier] start");
        Context context = this.mContext;
        if (context == null) {
            LogUtils.i(LogUtils.TAG, "DiInfo [getBundleIdentifier] context error");
            return "unknown";
        }
        return context.getPackageName();
    }

    public static String getBundleVersion(Context context) throws PackageManager.NameNotFoundException {
        LogUtils.i(LogUtils.TAG, "DiInfo [getBundleVersion] start");
        if (context == null) {
            LogUtils.i(LogUtils.TAG, "DiInfo [getBundleVersion] context error");
            return "unknown";
        }
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 1);
            return (packageInfo == null || packageInfo.versionName == null) ? "unknown" : packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.w(LogUtils.TAG, "DiInfo [getBundleVersion] NameNotFoundException=" + e.toString());
            e.printStackTrace();
            return "unknown";
        } catch (Exception e2) {
            LogUtils.w(LogUtils.TAG, "DiInfo [getBundleVersion] Exception=" + e2.toString());
            e2.printStackTrace();
            return "unknown";
        }
    }

    public String getPackageName() {
        LogUtils.i(LogUtils.TAG, "DiInfo [getPackageName] start");
        Context context = this.mContext;
        if (context == null) {
            LogUtils.i(LogUtils.TAG, "DiInfo [getBundleVersion] context error");
            return "unknown";
        }
        String packageName = context.getPackageName();
        return TextUtils.isEmpty(packageName) ? packageName : "unknown";
    }

    public static String getVersionCode(Context context) throws PackageManager.NameNotFoundException {
        LogUtils.i(LogUtils.TAG, "DiInfo [getVersionCode] start");
        if (context == null) {
            LogUtils.i(LogUtils.TAG, "DiInfo [getVersionCode] context error");
            return "unknown";
        }
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 16384).versionCode + "";
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.w(LogUtils.TAG, "DiInfo [getVersionCode] NameNotFoundException=" + e.toString());
            e.printStackTrace();
            return "unknown";
        } catch (Exception e2) {
            LogUtils.w(LogUtils.TAG, "DiInfo [getVersionCode] NameNotFoundException=" + e2.toString());
            e2.printStackTrace();
            return "unknown";
        }
    }

    public String getVersionName() throws PackageManager.NameNotFoundException {
        LogUtils.i(LogUtils.TAG, "DiInfo [getVersionName] start");
        Context context = this.mContext;
        if (context == null) {
            LogUtils.i(LogUtils.TAG, "DiInfo [getVersionName] context error");
            return "unknown";
        }
        try {
            return context.getPackageManager().getPackageInfo(this.mContext.getPackageName(), 16384).versionName + "";
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.w(LogUtils.TAG, "DiInfo [getVersionName] NameNotFoundException=" + e.toString());
            e.printStackTrace();
            return "unknown";
        } catch (Exception e2) {
            LogUtils.w(LogUtils.TAG, "DiInfo [getVersionName] Exception=" + e2.toString());
            e2.printStackTrace();
            return "unknown";
        }
    }

    public static String getRoBoardPlatform() {
        try {
            String str = (String) Class.forName("android.os.SystemProperties").getMethod(h.c, String.class).invoke(null, "ro.board.platform");
            if (str == null) {
                return "unknown";
            }
            return str;
        } catch (Exception e) {
            LogUtils.w(TAG, "DiInfo [getRoBoardPlatform] Exception=" + e.toString());
            e.printStackTrace();
            return "unknown";
        }
    }

    public List<String> getCpuInfo() throws IOException {
        LogUtils.i(LogUtils.TAG, "DiInfo [getCpuInfo] start");
        ArrayList arrayList = new ArrayList();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/cpuinfo"), 8192);
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                arrayList.add(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            LogUtils.w(LogUtils.TAG, "DiInfo [getCpuInfo] IOException=" + e.toString());
            e.printStackTrace();
        } catch (Exception e2) {
            LogUtils.w(LogUtils.TAG, "DiInfo [getCpuInfo] Exception=" + e2.toString());
            e2.printStackTrace();
        }
        return arrayList;
    }

    private long getTotalMemory() throws IOException {
        LogUtils.i(LogUtils.TAG, "DiInfo [getTotalMemory] start");
        long jLongValue = 0;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/meminfo"), 8192);
            jLongValue = Integer.valueOf(bufferedReader.readLine().split("\\s+")[1]).longValue() * 1024;
            bufferedReader.close();
            return jLongValue;
        } catch (IOException e) {
            LogUtils.w(LogUtils.TAG, "DiInfo [getTotalMemory] IOException=" + e.toString());
            e.toString();
            return jLongValue;
        } catch (Exception e2) {
            LogUtils.w(LogUtils.TAG, "DiInfo [getTotalMemory] Exception=" + e2.toString());
            e2.toString();
            return jLongValue;
        }
    }

    public long getFreeMem(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        if (memoryInfo.availMem > 0) {
            return (memoryInfo.availMem / 1024) / 1024;
        }
        return -1L;
    }

    private boolean isRooted() {
        LogUtils.i(LogUtils.TAG, "DiInfo [isRooted] start");
        try {
            if (!new File("/system/bin/su").exists()) {
                if (!new File("/system/xbin/su").exists()) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            LogUtils.w(LogUtils.TAG, "DiInfo [isRooted] Exceptio=" + e.toString());
            e.printStackTrace();
            return false;
        }
    }

    public static String intIP2StringIP(int i) {
        return (i & 255) + "." + ((i >> 8) & 255) + "." + ((i >> 16) & 255) + "." + ((i >> 24) & 255);
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
                        LogUtils.w(TAG, "DiInfo [readELFHeadrIndentArray] Exception=" + e.toString());
                        e.printStackTrace();
                    }
                    return bArr;
                }
                LogUtils.e(TAG, "DiInfo [readELFHeadrIndentArray] Error: e_indent lenght should be 16, but actual is " + i);
                try {
                    fileInputStream.close();
                } catch (Exception e2) {
                    e = e2;
                    sb = new StringBuilder("DiInfo [readELFHeadrIndentArray] Exception=");
                    sb.append(e.toString());
                    LogUtils.w(TAG, sb.toString());
                    e.printStackTrace();
                    return null;
                }
            } catch (Throwable th2) {
                th = th2;
                try {
                    LogUtils.w(TAG, "DiInfo [readELFHeadrIndentArray] Throwable=" + th.toString());
                    th.printStackTrace();
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (Exception e3) {
                            e = e3;
                            sb = new StringBuilder("DiInfo [readELFHeadrIndentArray] Exception=");
                            sb.append(e.toString());
                            LogUtils.w(TAG, sb.toString());
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
                            LogUtils.w(TAG, "DiInfo [readELFHeadrIndentArray] Exception=" + e4.toString());
                            e4.printStackTrace();
                        }
                    }
                    throw th3;
                }
            }
        }
        return null;
    }

    private boolean isLibc64() {
        byte[] eLFHeadrIndentArray;
        byte[] eLFHeadrIndentArray2;
        File file = new File("/system/lib/libc.so");
        if (file.exists() && (eLFHeadrIndentArray2 = readELFHeadrIndentArray(file)) != null && eLFHeadrIndentArray2[4] == 2) {
            LogUtils.i(TAG, "DiInfo [isLibc64] /system/lib/libc.so is 64bit");
            return true;
        }
        File file2 = new File("/system/lib64/libc.so");
        if (!file2.exists() || (eLFHeadrIndentArray = readELFHeadrIndentArray(file2)) == null || eLFHeadrIndentArray.length <= 4 || eLFHeadrIndentArray[4] != 2) {
            return false;
        }
        LogUtils.i(TAG, "DiInfo [isLibc64] /system/lib64/libc.so is 64bit");
        return true;
    }

    private boolean isCPUInfo64() throws IOException {
        FileInputStream fileInputStream;
        Throwable th;
        BufferedReader bufferedReader;
        StringBuilder sb;
        File file = new File("/proc/cpuinfo");
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
                LogUtils.i(TAG, "DiInfo [isCPUInfo64] /proc/cpuinfo is not arch64");
                try {
                    bufferedReader.close();
                } catch (Exception e) {
                    LogUtils.w(TAG, "DiInfo [isCPUInfo64] Exception = " + e.toString());
                    e.printStackTrace();
                }
                try {
                    fileInputStream.close();
                    return false;
                } catch (Exception e2) {
                    e = e2;
                    sb = new StringBuilder("DiInfo [isCPUInfo64] Exception2 = ");
                    sb.append(e.toString());
                    LogUtils.w(TAG, sb.toString());
                    e.printStackTrace();
                    return false;
                }
            }
            LogUtils.i(TAG, "DiInfo [isCPUInfo64] /proc/cpuinfo contains is arch64");
            try {
                bufferedReader.close();
            } catch (Exception e3) {
                LogUtils.w(TAG, "DiInfo [isCPUInfo64] Exception = " + e3.toString());
                e3.printStackTrace();
            }
            try {
                fileInputStream.close();
                return true;
            } catch (Exception e4) {
                LogUtils.w(TAG, "DiInfo [isCPUInfo64] Exception2 = " + e4.toString());
                e4.printStackTrace();
                return true;
            }
        } catch (Throwable th4) {
            th = th4;
            try {
                LogUtils.w(TAG, "DiInfo [isCPUInfo64] /proc/cpuinfo error = " + th.toString());
                th.printStackTrace();
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (Exception e5) {
                        LogUtils.w(TAG, "DiInfo [isCPUInfo64] Exception = " + e5.toString());
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
                    sb = new StringBuilder("DiInfo [isCPUInfo64] Exception2 = ");
                    sb.append(e.toString());
                    LogUtils.w(TAG, sb.toString());
                    e.printStackTrace();
                    return false;
                }
            } finally {
            }
        }
    }

    private String getSystemProperty(String str, String str2) throws ClassNotFoundException {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            str2 = (String) cls.getMethod(h.c, String.class, String.class).invoke(cls, str, "");
        } catch (Exception e) {
            LogUtils.w(TAG, "DiInfo [getSystemProperty] Exception=" + e.toString());
            e.printStackTrace();
        }
        LogUtils.i(TAG, "DiInfo [getSystemProperty] " + str + " = " + str2);
        return str2;
    }

    private int getArchType(Context context) {
        if (context == null) {
            return -1;
        }
        if (getSystemProperty("ro.product.cpu.abilist64", "").length() > 0) {
            LogUtils.i(TAG, "DiInfo [getArchType] CPU arch is 64bit");
        } else if (isCPUInfo64()) {
            LogUtils.i(TAG, "DiInfo [getArchType] CPU arch isCPUInfo64");
        } else if (isLibc64()) {
            LogUtils.i(TAG, "DiInfo [getArchType] CPU arch isLibc64");
        } else {
            LogUtils.i(TAG, "DiInfo [getArchType] return cpu DEFAULT 32bit!");
            return 32;
        }
        return 64;
    }

    private void setTime() throws JSONException {
        String[] strArrSplit;
        LogUtils.i(LogUtils.TAG, "DiInfo [setTime] start");
        try {
            String str = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH).format(new Date());
            LogUtils.i(LogUtils.TAG, "DiInfo [setTime] zone=" + str);
            if (!TextUtils.isEmpty(str)) {
                if (str.contains(Marker.ANY_NON_NULL_MARKER)) {
                    String[] strArrSplit2 = str.split("\\+");
                    if (strArrSplit2 != null && strArrSplit2.length > 0) {
                        str = Marker.ANY_NON_NULL_MARKER + strArrSplit2[strArrSplit2.length - 1];
                    }
                } else if (str.contains("-") && (strArrSplit = str.split("\\-")) != null && strArrSplit.length > 0) {
                    str = "-" + strArrSplit[strArrSplit.length - 1];
                }
            }
            LogUtils.i(LogUtils.TAG, "DiInfo [setTime] timeZone=" + str);
            String str2 = new SimpleDateFormat(ClientLogConstant.DATA_FORMAT, Locale.ENGLISH).format(new Date(System.currentTimeMillis()));
            this.mDiInfoJson.put("time", str2 + " " + str);
            if (this.mHasSetLaunchTime) {
                return;
            }
            this.mHasSetLaunchTime = true;
            LogUtils.w(LogUtils.TAG, "DiInfo [setTime] \u91cd\u7f6e\u4e86\u542f\u52a8\u65f6\u95f4");
            String str3 = new SimpleDateFormat(ClientLogConstant.DATA_FORMAT, Locale.ENGLISH).format(new Date(InitProxy.getInstance().mStartTime));
            this.mDiInfoJson.put("launch_time", str3 + " " + str);
        } catch (Exception e) {
            LogUtils.w(LogUtils.TAG, "DiInfo [setTime] Exception=" + e.toString());
            e.printStackTrace();
        }
    }

    private void setGPUInfo() throws JSONException {
        LogUtils.i(LogUtils.TAG, "DiInfo [setGPUInfo] start");
        setGPUInfoFromApplication();
    }

    private void setGPUInfoFromApplication() throws JSONException {
        if (!this.mDiInfoJson.has("gl_renderer") || "unknown".equals(this.mDiInfoJson.optString("gl_renderer")) || !this.mDiInfoJson.has("gl_vendor") || "unknown".equals(this.mDiInfoJson.optString("gl_vendor")) || !this.mDiInfoJson.has("gl_version") || "unknown".equals(this.mDiInfoJson.optString("gl_version"))) {
            LogUtils.i(LogUtils.TAG, "DiInfo [setGPUInfoFromApplication] start2");
            int[] iArr = {12325, 0, 12326, 0, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12344};
            int[] iArr2 = {12375, 100, 12374, 100, 12344};
            try {
                this.mDiInfoJson.put("gl_renderer", "unknown");
                this.mDiInfoJson.put("gl_vendor", "unknown");
                this.mDiInfoJson.put("gl_version", "unknown");
                this.mDiInfoJson.put("gpu", "unknown");
                EGL10 egl10 = (EGL10) EGLContext.getEGL();
                EGLDisplay eGLDisplayEglGetDisplay = egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
                if (eGLDisplayEglGetDisplay == EGL10.EGL_NO_DISPLAY) {
                    LogUtils.i(LogUtils.TAG, "DiInfo [setGPUInfoFromApplication] eglGetDisplay failed");
                    return;
                }
                if (!egl10.eglInitialize(eGLDisplayEglGetDisplay, new int[2])) {
                    LogUtils.i(LogUtils.TAG, "DiInfo [setGPUInfoFromApplication] eglInitialize failed");
                    return;
                }
                int[] iArr3 = new int[1];
                if (!egl10.eglChooseConfig(eGLDisplayEglGetDisplay, iArr, null, 0, iArr3)) {
                    LogUtils.i(LogUtils.TAG, "DiInfo [setGPUInfoFromApplication] eglChooseConfig failed");
                    return;
                }
                int i = iArr3[0];
                if (i <= 0) {
                    return;
                }
                EGLConfig[] eGLConfigArr = new EGLConfig[i];
                if (!egl10.eglChooseConfig(eGLDisplayEglGetDisplay, iArr, eGLConfigArr, i, iArr3)) {
                    LogUtils.i(LogUtils.TAG, "DiInfo [setGPUInfoFromApplication] eglChooseConfig#2 failed");
                    return;
                }
                EGLConfig eGLConfig = eGLConfigArr[0];
                int[] iArr4 = {12440, 2, 12344};
                EGLContext eGLContextEglGetCurrentContext = egl10.eglGetCurrentContext();
                if (eGLContextEglGetCurrentContext == EGL10.EGL_NO_CONTEXT) {
                    LogUtils.i(LogUtils.TAG, "DiInfo [setGPUInfoFromApplication] mEGLContext= EGL_NO_CONTEXT");
                    eGLContextEglGetCurrentContext = egl10.eglCreateContext(eGLDisplayEglGetDisplay, eGLConfig, EGL10.EGL_NO_CONTEXT, iArr4);
                    EGLSurface eGLSurfaceEglCreatePbufferSurface = egl10.eglCreatePbufferSurface(eGLDisplayEglGetDisplay, eGLConfig, iArr2);
                    if (eGLSurfaceEglCreatePbufferSurface == EGL10.EGL_NO_SURFACE) {
                        egl10.eglDestroyContext(eGLDisplayEglGetDisplay, eGLContextEglGetCurrentContext);
                        LogUtils.i(LogUtils.TAG, "DiInfo [setGPUInfoFromApplication] mEGLSurface=" + EGL10.EGL_NO_SURFACE + ", call eglDestroyContext");
                        return;
                    }
                    if (!egl10.eglMakeCurrent(eGLDisplayEglGetDisplay, eGLSurfaceEglCreatePbufferSurface, eGLSurfaceEglCreatePbufferSurface, eGLContextEglGetCurrentContext)) {
                        LogUtils.i(LogUtils.TAG, "DiInfo [setGPUInfoFromApplication] egl error=" + egl10.eglGetError());
                        return;
                    }
                }
                GL10 gl10 = (GL10) eGLContextEglGetCurrentContext.getGL();
                String strGlGetString = gl10.glGetString(7937);
                String strGlGetString2 = gl10.glGetString(7936);
                String strGlGetString3 = gl10.glGetString(7938);
                this.mDiInfoJson.put("gl_renderer", strGlGetString);
                this.mDiInfoJson.put("gl_vendor", strGlGetString2);
                this.mDiInfoJson.put("gl_version", strGlGetString3);
                this.mDiInfoJson.put("gpu", strGlGetString);
                LogUtils.i(LogUtils.TAG, "DiInfo [setGPUInfoFromApplication] onSurfaceCreated glRenderer=" + strGlGetString + ", glVendor=" + strGlGetString2 + ", glVersion=" + strGlGetString3 + ", gpu=" + strGlGetString3);
            } catch (Exception e) {
                LogUtils.w(LogUtils.TAG, "DiInfo [setGPUInfoFromApplication] Exception=" + e.toString());
                e.printStackTrace();
            }
        }
    }

    public void setBasicInfo() throws JSONException {
        LogUtils.i(LogUtils.TAG, "DiInfo [setBasicInfo] start");
        if (this.mContext == null) {
            LogUtils.i(LogUtils.TAG, "DiInfo [setBasicInfo] context error");
            return;
        }
        try {
            this.mDiInfoJson.put("model", Build.MODEL);
            this.mDiInfoJson.put(Constants.PHONE_BRAND, Build.BRAND);
            this.mDiInfoJson.put("mfr", Build.MANUFACTURER);
            this.mDiInfoJson.put("board", Build.BOARD);
            if (TextUtils.isEmpty(this.mSoLoadingType)) {
                this.mSoLoadingType = AndroidCrashHandler.getInstance().getSoLoadingType();
            }
            this.mDiInfoJson.put("cpu_abi", this.mSoLoadingType);
            this.mDiInfoJson.put("cpu_abi2", Build.CPU_ABI2);
            this.mDiInfoJson.put("arch_type", getArchType(this.mContext));
            this.mDiInfoJson.put("rom_name", RomNameUtil.getRomName());
            this.mDiInfoJson.put("is_rooted", String.valueOf(isRooted()));
            ArrayList arrayList = (ArrayList) getCpuInfo();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                if (str.contains("Hardware")) {
                    String[] strArrSplit = str.split(":");
                    if (strArrSplit.length >= 2) {
                        this.mDiInfoJson.put("hardware", strArrSplit[1]);
                    }
                }
            }
            if (arrayList.size() > 0) {
                this.mDiInfoJson.put("cpu", getRoBoardPlatform());
            }
            this.mDiInfoJson.put("is_emulator", CEmulatorDetector.detectLocal(this.mContext) + "");
            this.mDiInfoJson.put("package_fingerprint", CUtil.getCertificateSHA1Fingerprint(this.mContext));
            LogUtils.i(LogUtils.TAG, "DiInfo [setBasicInfo] mDiInfoJson=" + this.mDiInfoJson.toString());
        } catch (Exception e) {
            LogUtils.w(LogUtils.TAG, "DiInfo [setBasicInfo] Exception=" + e.toString());
            e.printStackTrace();
        }
    }

    public void setUniqueId() throws JSONException {
        LogUtils.i(LogUtils.TAG, "DiInfo [setUniqueId] start");
        try {
            this.mDiInfoJson.put("unisdk_device_id", InitProxy.getInstance().getmUnisdkDeviceId());
            this.mDiInfoJson.put("imei", InitProxy.getInstance().getmImei());
            this.mDiInfoJson.put(Const.CONFIG_KEY.LOCAL_IP, InitProxy.getInstance().getmLocalIp());
        } catch (Exception e) {
            LogUtils.w(LogUtils.TAG, "DiInfo [setUniqueId] Exception=" + e.toString());
            e.printStackTrace();
        }
    }

    public void setMemoryInfo() throws JSONException {
        LogUtils.i(LogUtils.TAG, "DiInfo [setMemoryInfo] start");
        long jCurrentTimeMillis = (System.currentTimeMillis() - InitProxy.getInstance().mStartTime) / 1000;
        if (this.mContext == null) {
            LogUtils.i(LogUtils.TAG, "DiInfo [setMemoryInfo] context error");
            return;
        }
        try {
            this.mDiInfoJson.put("with_sd_card", isContainSD(Environment.getExternalStorageState()));
            StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
            double totalSize = getTotalSize(statFs, Environment.getExternalStorageState(), false);
            this.mDiInfoJson.put("ex_size", (totalSize * 1024.0d) + "MB");
            double availableSize = getAvailableSize(statFs, Environment.getExternalStorageState(), false);
            this.mDiInfoJson.put("ex_avl_size", (availableSize * 1024.0d) + "MB");
            StatFs statFs2 = new StatFs(Environment.getRootDirectory().getAbsolutePath());
            double totalSize2 = getTotalSize(statFs2, Environment.getRootDirectory().getAbsolutePath(), true);
            this.mDiInfoJson.put("total_size", (totalSize2 * 1024.0d) + "MB");
            double availableSize2 = getAvailableSize(statFs2, Environment.getRootDirectory().getAbsolutePath(), true);
            this.mDiInfoJson.put("avl_size", (availableSize2 * 1024.0d) + "MB");
            this.mDiInfoJson.put("sys_mem", ((getTotalMemory() / 1024) / 1024) + "MB");
            MemoryStatus status = MemoryManager.getInstance().getStatus();
            if (this.mAvlMem.size() < this.QUEUE_MAX) {
                this.mAvlMem.offer(new DiInfoUnit(jCurrentTimeMillis + "", getFreeMem(this.mContext) + "MB"));
            } else {
                this.mAvlMem.poll();
                if (this.mAvlMem.size() < this.QUEUE_MAX) {
                    this.mAvlMem.offer(new DiInfoUnit(jCurrentTimeMillis + "", getFreeMem(this.mContext) + "MB"));
                }
            }
            this.mDiInfoJson.put("sys_avl_mem_info", parseQueue(this.mAvlMem));
            this.mDiInfoJson.put("threshold_mem", ((status.systemThreshold / 1024) / 1024) + "MB");
            this.mDiInfoJson.put("is_low_mem", String.valueOf(status.systemLowMemory));
            long j = status.systemTotalPss / 1024;
            long j2 = status.systemFreePss / 1024;
            long pssMemory = MemoryManager.getInstance().getPssMemory() / 1024;
            this.mDiInfoJson.put("app_max_mem", j + "MB");
            LogUtils.i(LogUtils.TAG, "max=" + j + ", avl=" + j2 + ", use=" + pssMemory);
            if (this.mAppMem.size() < this.QUEUE_MAX) {
                this.mAppMem.offer(new DiInfoUnit(jCurrentTimeMillis + "", pssMemory + "MB"));
            } else {
                this.mAppMem.poll();
                if (this.mAppMem.size() < this.QUEUE_MAX) {
                    this.mAppMem.offer(new DiInfoUnit(jCurrentTimeMillis + "", pssMemory + "MB"));
                }
            }
            this.mDiInfoJson.put("used_mem_info", parseQueue(this.mAppMem));
            if (this.mAppFreeMem.size() < this.QUEUE_MAX) {
                this.mAppFreeMem.offer(new DiInfoUnit(jCurrentTimeMillis + "", j2 + "MB"));
            } else {
                this.mAppFreeMem.poll();
                if (this.mAppFreeMem.size() < this.QUEUE_MAX) {
                    this.mAppFreeMem.offer(new DiInfoUnit(jCurrentTimeMillis + "", j2 + "MB"));
                }
            }
            this.mDiInfoJson.put("avl_mem_info", parseQueue(this.mAppFreeMem));
        } catch (Exception e) {
            LogUtils.w(LogUtils.TAG, "DiInfo [setMemoryInfo] Exception=" + e.toString());
            e.printStackTrace();
        }
    }

    private String isContainSD(String str) {
        LogUtils.i(LogUtils.TAG, "DiInfo [isContainSD] start");
        return "mounted".equals(str) ? "true" : com.facebook.hermes.intl.Constants.CASEFIRST_FALSE;
    }

    private double getTotalSize(StatFs statFs, String str, boolean z) {
        LogUtils.i(LogUtils.TAG, "DiInfo [getTotalSize] start");
        if (!"mounted".equals(str) && !z) {
            return -1.0d;
        }
        return new BigDecimal((((statFs.getBlockCountLong() * statFs.getBlockSizeLong()) / 1024.0d) / 1024.0d) / 1024.0d).setScale(3, 4).doubleValue();
    }

    private double getAvailableSize(StatFs statFs, String str, boolean z) {
        LogUtils.i(LogUtils.TAG, "DiInfo [getAvailableSize] start");
        if (!"mounted".equals(str) && !z) {
            return -1.0d;
        }
        return new BigDecimal((((statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong()) / 1024.0d) / 1024.0d) / 1024.0d).setScale(3, 4).doubleValue();
    }

    /* JADX WARN: Removed duplicated region for block: B:140:0x00c7 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:147:0x0106 A[Catch: all -> 0x0353, TRY_LEAVE, TryCatch #10 {, blocks: (B:118:0x002e, B:122:0x0063, B:124:0x0067, B:137:0x00ad, B:141:0x00c9, B:144:0x00d4, B:146:0x00ee, B:147:0x0106, B:151:0x011f, B:154:0x012a, B:156:0x0144, B:157:0x015c, B:159:0x0168, B:161:0x0171, B:163:0x017f, B:167:0x01cd, B:179:0x0214, B:180:0x021b, B:186:0x025a, B:187:0x0268, B:189:0x0294, B:193:0x030c, B:199:0x0351, B:190:0x02c9, B:192:0x02d8, B:196:0x031b, B:198:0x0337, B:183:0x0228, B:185:0x0242, B:164:0x019f, B:166:0x01ae, B:175:0x01e1, B:178:0x01fc, B:132:0x0076, B:135:0x0092), top: B:204:0x002e, inners: #11, #13, #17, #15, #12 }] */
    /* JADX WARN: Removed duplicated region for block: B:150:0x011d A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:157:0x015c A[Catch: all -> 0x0353, TRY_LEAVE, TryCatch #10 {, blocks: (B:118:0x002e, B:122:0x0063, B:124:0x0067, B:137:0x00ad, B:141:0x00c9, B:144:0x00d4, B:146:0x00ee, B:147:0x0106, B:151:0x011f, B:154:0x012a, B:156:0x0144, B:157:0x015c, B:159:0x0168, B:161:0x0171, B:163:0x017f, B:167:0x01cd, B:179:0x0214, B:180:0x021b, B:186:0x025a, B:187:0x0268, B:189:0x0294, B:193:0x030c, B:199:0x0351, B:190:0x02c9, B:192:0x02d8, B:196:0x031b, B:198:0x0337, B:183:0x0228, B:185:0x0242, B:164:0x019f, B:166:0x01ae, B:175:0x01e1, B:178:0x01fc, B:132:0x0076, B:135:0x0092), top: B:204:0x002e, inners: #11, #13, #17, #15, #12 }] */
    /* JADX WARN: Removed duplicated region for block: B:163:0x017f A[Catch: Exception -> 0x01db, JSONException -> 0x01dd, all -> 0x0353, TryCatch #10 {, blocks: (B:118:0x002e, B:122:0x0063, B:124:0x0067, B:137:0x00ad, B:141:0x00c9, B:144:0x00d4, B:146:0x00ee, B:147:0x0106, B:151:0x011f, B:154:0x012a, B:156:0x0144, B:157:0x015c, B:159:0x0168, B:161:0x0171, B:163:0x017f, B:167:0x01cd, B:179:0x0214, B:180:0x021b, B:186:0x025a, B:187:0x0268, B:189:0x0294, B:193:0x030c, B:199:0x0351, B:190:0x02c9, B:192:0x02d8, B:196:0x031b, B:198:0x0337, B:183:0x0228, B:185:0x0242, B:164:0x019f, B:166:0x01ae, B:175:0x01e1, B:178:0x01fc, B:132:0x0076, B:135:0x0092), top: B:204:0x002e, inners: #11, #13, #17, #15, #12 }] */
    /* JADX WARN: Removed duplicated region for block: B:164:0x019f A[Catch: Exception -> 0x01db, JSONException -> 0x01dd, all -> 0x0353, TryCatch #10 {, blocks: (B:118:0x002e, B:122:0x0063, B:124:0x0067, B:137:0x00ad, B:141:0x00c9, B:144:0x00d4, B:146:0x00ee, B:147:0x0106, B:151:0x011f, B:154:0x012a, B:156:0x0144, B:157:0x015c, B:159:0x0168, B:161:0x0171, B:163:0x017f, B:167:0x01cd, B:179:0x0214, B:180:0x021b, B:186:0x025a, B:187:0x0268, B:189:0x0294, B:193:0x030c, B:199:0x0351, B:190:0x02c9, B:192:0x02d8, B:196:0x031b, B:198:0x0337, B:183:0x0228, B:185:0x0242, B:164:0x019f, B:166:0x01ae, B:175:0x01e1, B:178:0x01fc, B:132:0x0076, B:135:0x0092), top: B:204:0x002e, inners: #11, #13, #17, #15, #12 }] */
    /* JADX WARN: Removed duplicated region for block: B:189:0x0294 A[Catch: Exception -> 0x031a, JSONException -> 0x0336, all -> 0x0353, TryCatch #11 {JSONException -> 0x0336, blocks: (B:187:0x0268, B:189:0x0294, B:193:0x030c, B:190:0x02c9, B:192:0x02d8), top: B:206:0x0268, outer: #10 }] */
    /* JADX WARN: Removed duplicated region for block: B:190:0x02c9 A[Catch: Exception -> 0x031a, JSONException -> 0x0336, all -> 0x0353, TryCatch #11 {JSONException -> 0x0336, blocks: (B:187:0x0268, B:189:0x0294, B:193:0x030c, B:190:0x02c9, B:192:0x02d8), top: B:206:0x0268, outer: #10 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void setBatteryInfo() {
        /*
            Method dump skipped, instructions count: 854
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.androidcrashhandler.entity.di.DiInfo.setBatteryInfo():void");
    }

    public void setScreenInfo() throws JSONException {
        LogUtils.i(LogUtils.TAG, "DiInfo [setScreenInfo] start");
        if (this.mContext == null) {
            LogUtils.i(LogUtils.TAG, "DiInfo [setScreenInfo] context error");
            return;
        }
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
            int i = displayMetrics.widthPixels;
            int i2 = displayMetrics.heightPixels;
            this.mDiInfoJson.put("screen_width", String.valueOf(i));
            this.mDiInfoJson.put("screen_height", String.valueOf(i2));
        } catch (JSONException e) {
            LogUtils.w(LogUtils.TAG, "DiInfo [setScreenInfo] JSONException=" + e.toString());
            e.printStackTrace();
        } catch (Exception e2) {
            LogUtils.w(LogUtils.TAG, "DiInfo [Exception] JSONException=" + e2.toString());
            e2.toString();
        }
        try {
            String str = "unknow";
            int i3 = this.mContext.getResources().getConfiguration().orientation;
            if (i3 == 2) {
                str = "LANDSCAPE";
            } else if (i3 == 1) {
                str = "PORTRAIT";
            }
            this.mDiInfoJson.put("ori", str);
        } catch (JSONException e3) {
            LogUtils.w(LogUtils.TAG, "DiInfo [setScreenInfo] JSONException=" + e3.toString());
            e3.printStackTrace();
        } catch (Exception e4) {
            LogUtils.w(LogUtils.TAG, "DiInfo [setScreenInfo] Exception=" + e4.toString());
            e4.toString();
        }
    }

    public void setNetInfo() throws JSONException {
        LogUtils.i(LogUtils.TAG, "DiInfo [setNetInfo] start");
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "getNetworkInfoJson");
            String strExtendFunc = ModulesManager.getInst().extendFunc(CUniLogger.source, "deviceInfo", jSONObject.toString());
            LogUtils.i(LogUtils.TAG, "CUtil [setNetInfo] networkInfoJson=" + strExtendFunc);
            JSONObject jSONObject2 = new JSONObject(strExtendFunc);
            String str = "Not_Available";
            if (jSONObject2.has("getDetailedState")) {
                String strOptString = jSONObject2.optString("getDetailedState");
                if (!TextUtils.isEmpty(strOptString) && !"null".equals(strOptString)) {
                    str = strOptString;
                }
            }
            int iOptInt = jSONObject2.has("getType") ? jSONObject2.optInt("getType") : -1;
            String strOptString2 = jSONObject2.has("getSubtypeName") ? jSONObject2.optString("getSubtypeName") : "Unknown";
            this.mDiInfoJson.put("net_state", str);
            if (iOptInt == 1) {
                this.mDiInfoJson.put("net_type", "WIFI");
                if (this.mDiInfoJson.has("net_pto")) {
                    this.mDiInfoJson.remove("net_pto");
                    return;
                }
                return;
            }
            if (iOptInt == 0) {
                this.mDiInfoJson.put("net_type", "radio");
                this.mDiInfoJson.put("net_pto", strOptString2);
            } else {
                this.mDiInfoJson.put("net_type", "Unknown");
                this.mDiInfoJson.put("net_pto", "Unknown");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(LogUtils.TAG, "CUtil [setNetInfo] Exception=" + e.toString());
        }
    }

    private String getBaseVersion() throws ClassNotFoundException {
        LogUtils.i(LogUtils.TAG, "DiInfo [getBaseVersion] start");
        String str = "unknown";
        try {
            Class<?> cls = Class.forName("com.netease.ntunisdk.base.SdkMgr");
            String str2 = (String) cls.getDeclaredMethod("getBaseVersion", new Class[0]).invoke(cls, new Object[0]);
            try {
                LogUtils.i(LogUtils.TAG, "DiInfo [startCrashHandle] baseVersion =" + str2);
                return str2;
            } catch (ClassNotFoundException e) {
                e = e;
                str = str2;
                LogUtils.w(LogUtils.TAG, "DiInfo [setBaseVersion] ClassNotFoundException=" + e);
                e.printStackTrace();
                return str;
            } catch (IllegalAccessException e2) {
                e = e2;
                str = str2;
                LogUtils.w(LogUtils.TAG, "DiInfo [setBaseVersion] IllegalAccessException=" + e);
                e.printStackTrace();
                return str;
            } catch (IllegalArgumentException e3) {
                e = e3;
                str = str2;
                LogUtils.w(LogUtils.TAG, "DiInfo [setBaseVersion] IllegalArgumentException=" + e);
                e.printStackTrace();
                return str;
            } catch (NoSuchMethodException e4) {
                e = e4;
                str = str2;
                LogUtils.w(LogUtils.TAG, "DiInfo [setBaseVersion] NoSuchMethodException=" + e);
                e.printStackTrace();
                return str;
            } catch (InvocationTargetException e5) {
                e = e5;
                str = str2;
                LogUtils.w(LogUtils.TAG, "DiInfo [setBaseVersion] InvocationTargetException=" + e);
                e.printStackTrace();
                return str;
            } catch (Exception e6) {
                e = e6;
                str = str2;
                LogUtils.w(LogUtils.TAG, "DiInfo [setBaseVersion] Exception=" + e);
                e.printStackTrace();
                return str;
            }
        } catch (ClassNotFoundException e7) {
            e = e7;
        } catch (IllegalAccessException e8) {
            e = e8;
        } catch (IllegalArgumentException e9) {
            e = e9;
        } catch (NoSuchMethodException e10) {
            e = e10;
        } catch (InvocationTargetException e11) {
            e = e11;
        } catch (Exception e12) {
            e = e12;
        }
    }

    public static JSONObject getChannelInfos() {
        JSONObject jSONObjectOptJSONObject;
        LogUtils.i(LogUtils.TAG, "DiInfo [getChannelInfos] start");
        if (mChannelInfosJson == null) {
            try {
                String assetFileContent = CUtil.getAssetFileContent(NTCrashHunterKit.sharedKit().getContext(), "channel_infos_data");
                LogUtils.i(LogUtils.TAG, "DiInfo [getChannelInfos] channelInfos =" + assetFileContent);
                if (!TextUtils.isEmpty(assetFileContent)) {
                    JSONObject jSONObject = new JSONObject(assetFileContent);
                    if (jSONObject.has("main_channel") && (jSONObjectOptJSONObject = jSONObject.optJSONObject("main_channel")) != null && jSONObjectOptJSONObject.length() > 0) {
                        Iterator<String> itKeys = jSONObjectOptJSONObject.keys();
                        while (itKeys.hasNext()) {
                            mChannelInfosJson = jSONObjectOptJSONObject.optJSONObject(itKeys.next());
                        }
                    }
                }
            } catch (Exception e) {
                LogUtils.i(LogUtils.TAG, "DiInfo [getChannelInfos] Exception=" + e.toString());
                e.printStackTrace();
            }
        }
        if (mChannelInfosJson != null) {
            LogUtils.i(LogUtils.TAG, "DiInfo [getChannelInfos] mChannelInfosJson=" + mChannelInfosJson.toString());
        }
        return mChannelInfosJson;
    }

    private JSONObject parseQueue(BlockingQueue<DiInfoUnit> blockingQueue) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        for (DiInfoUnit diInfoUnit : blockingQueue) {
            try {
                jSONObject.put(diInfoUnit.mKey, diInfoUnit.mValue);
            } catch (Exception e) {
                LogUtils.w(LogUtils.TAG, "DiInfo [parseQueue] Exception=" + e);
            }
        }
        return jSONObject;
    }

    public void add(BlockingQueue<DiInfoUnit> blockingQueue, DiInfoUnit diInfoUnit) {
        if (blockingQueue == null || diInfoUnit == null) {
            return;
        }
        if (blockingQueue.size() > this.QUEUE_MAX - 10) {
            blockingQueue.poll();
        }
        blockingQueue.add(diInfoUnit);
    }

    public void setOtherDynamicInfo() throws JSONException {
        LogUtils.i(LogUtils.TAG, "DiInfo [setOtherDynamicInfo] start");
        try {
            this.mDiInfoJson.put("is_foreground", Lifecycle.getInstence().isForeground());
        } catch (Exception e) {
            LogUtils.i(LogUtils.TAG, "DiInfo [setOtherDynamicInfo] Exception=" + e.toString());
            e.toString();
        }
    }

    public void setVersionInfo() throws JSONException {
        String strOptString;
        LogUtils.i(LogUtils.TAG, "DiInfo [setVersionInfo] start");
        try {
            this.mDiInfoJson.put("system_version", Build.VERSION.RELEASE);
            this.mDiInfoJson.put("system_api_level", String.valueOf(Build.VERSION.SDK_INT));
            this.mDiInfoJson.put("bundle_version", getBundleVersion(this.mContext));
            this.mDiInfoJson.put("version_code", getVersionCode(this.mContext));
            this.mDiInfoJson.put("bundle_identifier", getBundleIdentifier());
            this.mDiInfoJson.put("base_version", getBaseVersion());
            this.mDiInfoJson.put("crashhunter_version", "3.12.4");
            JSONObject channelInfos = getChannelInfos();
            String strOptString2 = "unknown";
            if (channelInfos != null) {
                strOptString = channelInfos.has("version") ? channelInfos.optString("version", "unknown") : "unknown";
                if (channelInfos.has("channel_id")) {
                    strOptString2 = channelInfos.optString("channel_id", "unknown");
                }
            } else {
                strOptString = "unknown";
            }
            this.mDiInfoJson.put("channel", strOptString2);
            this.mDiInfoJson.put(Const.CONFIG_KEY.UNISDK_VERSION, strOptString);
        } catch (JSONException e) {
            LogUtils.i(LogUtils.TAG, "DiInfo [setVersionInfo] JSONException=" + e.toString());
            e.printStackTrace();
        } catch (Exception e2) {
            LogUtils.i(LogUtils.TAG, "DiInfo [setVersionInfo] Exception=" + e2.toString());
            e2.toString();
        }
    }

    private void addTDiInfo() {
        JSONObject jSONObject = this.mTInfoJson;
        if (jSONObject != null) {
            jSONObject.length();
        }
    }

    private void deleteTDiInfo() {
        JSONObject jSONObject = this.mTInfoJson;
        if (jSONObject != null) {
            jSONObject.length();
        }
    }

    public JSONObject getDiInfoJson() {
        return this.mDiInfoJson;
    }
}