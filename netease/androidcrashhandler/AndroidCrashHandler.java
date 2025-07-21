package com.netease.androidcrashhandler;

import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.text.TextUtils;
import com.netease.androidcrashhandler.Const;
import com.netease.androidcrashhandler.anr.ANRWatchDogProxy;
import com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager;
import com.netease.androidcrashhandler.anr.messageQueue.MessageProxy;
import com.netease.androidcrashhandler.anr.messageQueue.StackManager;
import com.netease.androidcrashhandler.callback.IPrePostCallBack;
import com.netease.androidcrashhandler.callback.NTEventOccurCallBack;
import com.netease.androidcrashhandler.config.ConfigCore;
import com.netease.androidcrashhandler.entity.di.DiInfo;
import com.netease.androidcrashhandler.entity.di.DiProxy;
import com.netease.androidcrashhandler.init.InitProxy;
import com.netease.androidcrashhandler.javacrash.JavaCrashCallBack;
import com.netease.androidcrashhandler.so.SoUuidCore;
import com.netease.androidcrashhandler.thirdparty.clientLogModule.ClientLog;
import com.netease.androidcrashhandler.util.CEmulatorDetector;
import com.netease.androidcrashhandler.util.CUtil;
import com.netease.androidcrashhandler.util.LogUtils;
import com.netease.androidcrashhandler.util.StorageToFileProxy;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class AndroidCrashHandler {
    private static AndroidCrashHandler sAndroidCrashHandler;
    private String mTargetNativeCrashDir = "";
    private String mLastTimeCrashDir = "";
    private MyCrashCallBack mCrashCallBack = null;
    private MyCHListener mMyCHListener = null;
    private IPrePostCallBack mIPrePostCallBack = null;

    private native void detectSigaction(String str, boolean z);

    public static native void hookAppExit(String str);

    public static native void initSoCheck(String str, String str2);

    public native void NCCrashHandler(String str, String str2, boolean z);

    public native void NCSetCfgInfo(String str, String str2);

    public String getCrashIdentity() {
        return null;
    }

    public native String getSoBuildId(String str);

    public native String getSoLoadingType();

    public native String getThreadFullUnwind();

    public void handleNCCrash(String str) {
    }

    public native void openTouchEventHook();

    public native void setAnrTracePath(String str);

    public native void setDumpModule(String str, int i, boolean z);

    public native void setHookEnable(boolean z);

    public native void setLogFilePath(String str);

    public native void setTargetThreadUnwind(boolean z);

    public native void setThrowable(boolean z);

    public native void writeFdInfoToLocalFile(String str);

    static {
        try {
            System.loadLibrary("AndroidCrashHandler");
        } catch (Throwable th) {
            LogUtils.i(LogUtils.TAG, "CrashHunterProxy [static ] load AndroidCrashHandler so Exception=" + th.toString());
            th.printStackTrace();
        }
    }

    private AndroidCrashHandler() {
        if ((Build.VERSION.SDK_INT == 24 || Build.VERSION.SDK_INT == 25) && CEmulatorDetector.detectLocal(NTCrashHunterKit.sharedKit().getContext())) {
            setHookEnable(false);
        }
    }

    public static AndroidCrashHandler getInstance() {
        if (sAndroidCrashHandler == null) {
            LogUtils.checkDebug();
            sAndroidCrashHandler = new AndroidCrashHandler();
        }
        return sAndroidCrashHandler;
    }

    public void start(Context context) {
        LogUtils.i(LogUtils.TAG, "CrashHunterProxy [start] start");
        if (InitProxy.getInstance().isOpenBreakpad()) {
            SoUuidCore.getInstance().storageSoUuidInfosToSdkDir(context);
            StringBuilder sb = new StringBuilder("CrashHunterProxy [start] sUploadFilePath = ");
            InitProxy.getInstance();
            sb.append(InitProxy.sUploadFilePath);
            LogUtils.i(LogUtils.TAG, sb.toString());
            LogUtils.i(LogUtils.TAG, "CrashHunterProxy [start] Native\u5d29\u6e83\u6355\u6349\u673a\u5236\u542f\u52a8");
            NCSetCfgInfo(Const.ParamKey.CALLBACK_SO_PATH, InitProxy.getInstance().getCallbackSoPath());
            NCSetCfgInfo(Const.ParamKey.CALLBACK_METHOD_NAME, InitProxy.getInstance().getCallbackMethodName());
            CUtil.runOnMainThread(new Runnable() { // from class: com.netease.androidcrashhandler.AndroidCrashHandler.1
                final /* synthetic */ Context val$mContext;

                AnonymousClass1(Context context2) {
                    context = context2;
                }

                @Override // java.lang.Runnable
                public void run() {
                    File file = new File(InitProxy.sUploadFilePath, Const.FileNameTag.DIR_NATIVE_CRASH + Process.myPid() + "_" + System.currentTimeMillis());
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    AndroidCrashHandler.this.NCCrashHandler(file.getAbsolutePath(), context.getPackageName(), ConfigCore.getInstance().isAppCrashThrowable());
                    AndroidCrashHandler.initSoCheck(context.getApplicationInfo().nativeLibraryDir, InitProxy.sFilesDir + File.separator + "ntunisdk_so_uuids");
                    AndroidCrashHandler.this.mTargetNativeCrashDir = file.getAbsolutePath();
                }
            });
            return;
        }
        LogUtils.i(LogUtils.TAG, "CrashHunterProxy [start] \u4e0d\u542f\u52a8Native\u5d29\u6e83\u6355\u6349\u673a\u5236");
    }

    /* renamed from: com.netease.androidcrashhandler.AndroidCrashHandler$1 */
    class AnonymousClass1 implements Runnable {
        final /* synthetic */ Context val$mContext;

        AnonymousClass1(Context context2) {
            context = context2;
        }

        @Override // java.lang.Runnable
        public void run() {
            File file = new File(InitProxy.sUploadFilePath, Const.FileNameTag.DIR_NATIVE_CRASH + Process.myPid() + "_" + System.currentTimeMillis());
            if (!file.exists()) {
                file.mkdirs();
            }
            AndroidCrashHandler.this.NCCrashHandler(file.getAbsolutePath(), context.getPackageName(), ConfigCore.getInstance().isAppCrashThrowable());
            AndroidCrashHandler.initSoCheck(context.getApplicationInfo().nativeLibraryDir, InitProxy.sFilesDir + File.separator + "ntunisdk_so_uuids");
            AndroidCrashHandler.this.mTargetNativeCrashDir = file.getAbsolutePath();
        }
    }

    public static void nativeInputEventLag(boolean z, String str) {
        LogUtils.i(LogUtils.TAG, "AndroidCrashHandler [nativeInputEventLag] isLagTag:" + z);
        if (z) {
            LooperMessageLoggingManager.getInstance().sendInputEventLagMessage(str);
        } else {
            LooperMessageLoggingManager.getInstance().sendInputEventLagFinish();
        }
    }

    public static void nativeSignalCallback(int i) throws JSONException {
        LogUtils.i(LogUtils.TAG, "AndroidCrashHandler [nativeSignalCallback] start");
        LogUtils.i(LogUtils.TAG, "AndroidCrashHandler [nativeSignalCallback] signal=" + i);
        if (i == 3) {
            handleAnr();
        } else {
            handleNativeCrash();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:64:0x0054  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void handleAnr() {
        /*
            java.lang.String r0 = "AndroidCrashHandler [nativeSignalCallback] create file:"
            com.netease.androidcrashhandler.thirdparty.lifecycle.Lifecycle r1 = com.netease.androidcrashhandler.thirdparty.lifecycle.Lifecycle.getInstence()
            boolean r1 = r1.isForeground()
            java.io.File r2 = new java.io.File
            java.lang.String r3 = com.netease.androidcrashhandler.anr.ANRWatchDogProxy.sAnrUploadFilePath
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            java.lang.String r5 = "anr_"
            r4.<init>(r5)
            int r5 = android.os.Process.myPid()
            r4.append(r5)
            java.lang.String r5 = ".trace"
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            r2.<init>(r3, r4)
            r3 = 1
            java.lang.String r4 = "trace"
            if (r1 != 0) goto L54
            java.lang.String r1 = "AnrProxy [storageAnrContextInfo] background"
            com.netease.androidcrashhandler.util.LogUtils.i(r4, r1)
            android.os.MessageQueue r1 = com.netease.androidcrashhandler.anr.messageQueue.HookMessage.getMessageQueue()
            boolean r1 = com.netease.androidcrashhandler.anr.messageQueue.HookMessage.isMainThreadBlocked(r1)
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            java.lang.String r6 = "AnrProxy [storageAnrContextInfo] notBlock:"
            r5.<init>(r6)
            r5.append(r1)
            java.lang.String r5 = r5.toString()
            com.netease.androidcrashhandler.util.LogUtils.i(r4, r5)
            if (r1 != 0) goto L54
            java.lang.String r1 = "AnrProxy [storageAnrContextInfo] not_my_anr"
            com.netease.androidcrashhandler.util.LogUtils.i(r4, r1)
            r1 = r3
            goto L55
        L54:
            r1 = 0
        L55:
            r5 = 0
            boolean r6 = r2.createNewFile()     // Catch: java.lang.Throwable -> L94
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L94
            r7.<init>(r0)     // Catch: java.lang.Throwable -> L94
            r7.append(r6)     // Catch: java.lang.Throwable -> L94
            java.lang.String r0 = r7.toString()     // Catch: java.lang.Throwable -> L94
            com.netease.androidcrashhandler.util.LogUtils.i(r4, r0)     // Catch: java.lang.Throwable -> L94
            if (r6 == 0) goto L86
            java.io.FileWriter r0 = new java.io.FileWriter     // Catch: java.lang.Throwable -> L94
            r0.<init>(r2, r3)     // Catch: java.lang.Throwable -> L94
            java.lang.String r2 = "com.netease.androidcrashhandler.anr.ANRError: Application Not Responding for at least 5000 ms.\n"
            if (r1 == 0) goto L79
            java.lang.String r1 = "------may_not_my_process_anr------\n"
            r0.write(r1)     // Catch: java.lang.Throwable -> L84
        L79:
            r0.write(r2)     // Catch: java.lang.Throwable -> L84
            java.lang.String r1 = com.netease.androidcrashhandler.anr.ANRError.allStackTrace()     // Catch: java.lang.Throwable -> L84
            r0.write(r1)     // Catch: java.lang.Throwable -> L84
            goto L87
        L84:
            r1 = move-exception
            goto L96
        L86:
            r0 = r5
        L87:
            if (r0 == 0) goto La6
            r0.close()     // Catch: java.io.IOException -> L8d
            goto La6
        L8d:
            r0 = move-exception
            java.lang.RuntimeException r1 = new java.lang.RuntimeException
            r1.<init>(r0)
            throw r1
        L94:
            r1 = move-exception
            r0 = r5
        L96:
            r1.printStackTrace()     // Catch: java.lang.Throwable -> Lb2
            if (r0 == 0) goto La6
            r0.close()     // Catch: java.io.IOException -> L9f
            goto La6
        L9f:
            r0 = move-exception
            java.lang.RuntimeException r1 = new java.lang.RuntimeException
            r1.<init>(r0)
            throw r1
        La6:
            handleMessageContextInfo()
            com.netease.androidcrashhandler.AndroidCrashHandler$2 r0 = new com.netease.androidcrashhandler.AndroidCrashHandler$2
            r0.<init>()
            com.netease.androidcrashhandler.util.CUtil.runOnNewChildThread(r0, r5)
            return
        Lb2:
            r1 = move-exception
            if (r0 == 0) goto Lc0
            r0.close()     // Catch: java.io.IOException -> Lb9
            goto Lc0
        Lb9:
            r0 = move-exception
            java.lang.RuntimeException r1 = new java.lang.RuntimeException
            r1.<init>(r0)
            throw r1
        Lc0:
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.androidcrashhandler.AndroidCrashHandler.handleAnr():void");
    }

    /* renamed from: com.netease.androidcrashhandler.AndroidCrashHandler$2 */
    class AnonymousClass2 implements CUtil.ThreadTask {
        AnonymousClass2() {
        }

        @Override // com.netease.androidcrashhandler.util.CUtil.ThreadTask
        public void run() throws JSONException {
            DiProxy.getInstance().updateDiInfoToLocalFile();
            CUtil.copyFile(InitProxy.sUploadFilePath + "/" + DiInfo.sCurFileName, ANRWatchDogProxy.sAnrUploadFilePath + "/" + DiInfo.sCurFileName);
            AndroidCrashHandler.callbackToUser(6, "OCCUR ANR");
            ClientLog.getInstence().sendClientLog("OCCUR ANR");
        }
    }

    private static void handleNativeCrash() throws JSONException {
        LogUtils.i(LogUtils.TAG, "AndroidCrashHandler [handleNativeCrash]");
        DiProxy.getInstance().updateDiInfoToLocalFile();
        CUtil.copyFile(InitProxy.sFilesDir + File.separator + "ntunisdk_so_uuids", getInstance().getTargetNativeCrashDir() + File.separator + "ntunisdk_so_uuids");
        CUtil.copyFile(InitProxy.sUploadFilePath + "/" + DiInfo.sCurFileName, getInstance().getTargetNativeCrashDir() + "/" + DiInfo.sCurFileName);
        StorageToFileProxy.getInstances().finish();
        callbackToUser(8, "OCCUR NATIVE CRASH");
        ClientLog.getInstence().sendClientLog("OCCUR NATIVE CRASH");
    }

    private static void handleMessageContextInfo() {
        LogUtils.i(LogUtils.TAG, "AndroidCrashHandler [handleMessageContextInfo] MessageEnabled = " + ConfigCore.getInstance().ismMessageEnabled());
        if (ConfigCore.getInstance().ismMessageEnabled()) {
            LogUtils.i(LogUtils.TAG, "AndroidCrashHandler [handleMessageContextInfo] \u5b58\u50a8Meesage\u53cacpu\u4fe1\u606f");
            MessageProxy.getInstance().storageMessageContextInfo();
        } else {
            LogUtils.i(LogUtils.TAG, "AndroidCrashHandler [handleMessageContextInfo] \u65e0\u9700\u5b58\u50a8Meesage\u53cacpu\u4fe1\u606f");
        }
    }

    private static void storageJavaMainThreadStackTrackToFile() {
        CUtil.str2File(StackManager.getInstance().getJavaMainThreadStackTrack(), getInstance().getTargetNativeCrashDir(), "javaStackTrace.txt");
    }

    public String getTargetNativeCrashDir() {
        return this.mTargetNativeCrashDir;
    }

    public static void callbackToUser(int i, String str) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("event_info", str);
            callbackToUser(i, jSONObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void callbackToUser(int i, JSONObject jSONObject) {
        NTEventOccurCallBack nTEventOccurCallBack = NTCrashHunterKit.sharedKit().getNTEventOccurCallBack();
        if (nTEventOccurCallBack != null) {
            try {
                nTEventOccurCallBack.onNTEventOccurCallBack(i, jSONObject.toString());
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    public MyNetworkUtils getNetworkUtils() {
        return MyNetworkUtils.getInstance();
    }

    public void setCallBack(MyCrashCallBack myCrashCallBack) {
        this.mCrashCallBack = myCrashCallBack;
    }

    public void startCrashHandle(Context context) throws IOException, NumberFormatException {
        LogUtils.i(LogUtils.TAG, "AndroidCrashHandler [startCrashHandle] \u4ece\u65e7\u63a5\u53e3\u542f\u52a8crashhunter");
        NTCrashHunterKit.sharedKit().init(context);
        HashMap<String, String> params = getNetworkUtils().getDefaultPostEntity().getParams();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                LogUtils.i(LogUtils.TAG, "AndroidCrashHandler [startCrashHandle] key=" + key + ", value=" + value);
                if (Const.ParamKey.CALLBACK_SO_PATH.equals(key) || Const.ParamKey.CALLBACK_METHOD_NAME.equals(key)) {
                    LogUtils.i(LogUtils.TAG, "AndroidCrashHandler [startCrashHandle] setSoParam");
                    NTCrashHunterKit.sharedKit().setSoParam(key, value);
                } else {
                    NTCrashHunterKit.sharedKit().setParam(key, value);
                }
            }
        }
        NTCrashHunterKit.sharedKit().setJavaCrashCallBack(new JavaCrashCallBack() { // from class: com.netease.androidcrashhandler.AndroidCrashHandler.3
            AnonymousClass3() {
            }

            @Override // com.netease.androidcrashhandler.javacrash.JavaCrashCallBack
            public void crashCallBack(Throwable th) {
                LogUtils.i(LogUtils.TAG, "AndroidCrashHandler [startCrashHandle] [JavaCrashCallBack] \u901a\u8fc7\u65e7\u65b9\u5f0f\u8f6c\u63a5\uff0cjava\u5d29\u6e83\u56de\u8c03");
                if (AndroidCrashHandler.this.mCrashCallBack != null) {
                    AndroidCrashHandler.this.mCrashCallBack.crashCallBack();
                }
            }
        });
        LogUtils.i(LogUtils.TAG, "AndroidCrashHandler [startCrashHandle] \u901a\u8fc7\u65e7\u65b9\u5f0f\u8f6c\u63a5\uff0c\u8c03\u7528setIPrePostCallBack");
        NTCrashHunterKit.sharedKit().setIPrePostCallBack(this.mIPrePostCallBack);
        NTCrashHunterKit.sharedKit().startHuntingCrash();
    }

    /* renamed from: com.netease.androidcrashhandler.AndroidCrashHandler$3 */
    class AnonymousClass3 implements JavaCrashCallBack {
        AnonymousClass3() {
        }

        @Override // com.netease.androidcrashhandler.javacrash.JavaCrashCallBack
        public void crashCallBack(Throwable th) {
            LogUtils.i(LogUtils.TAG, "AndroidCrashHandler [startCrashHandle] [JavaCrashCallBack] \u901a\u8fc7\u65e7\u65b9\u5f0f\u8f6c\u63a5\uff0cjava\u5d29\u6e83\u56de\u8c03");
            if (AndroidCrashHandler.this.mCrashCallBack != null) {
                AndroidCrashHandler.this.mCrashCallBack.crashCallBack();
            }
        }
    }

    public void setResVersion(String str) throws NumberFormatException {
        LogUtils.i(LogUtils.TAG, "AndroidCrashHandler [setResVersion] version=" + str);
        if (TextUtils.isEmpty(str)) {
            LogUtils.i(LogUtils.TAG, "AndroidCrashHandler [setResVersion] version is null");
        } else {
            NTCrashHunterKit.sharedKit().setParam(Const.ParamKey.RES_VERSION, str);
        }
    }

    public void setEngineVersion(String str) throws NumberFormatException {
        LogUtils.i(LogUtils.TAG, "AndroidCrashHandler [setEngineVersion] version=" + str);
        if (TextUtils.isEmpty(str)) {
            LogUtils.i(LogUtils.TAG, "AndroidCrashHandler [setEngineVersion] version is null");
        } else {
            NTCrashHunterKit.sharedKit().setParam(Const.ParamKey.ENGINE_VERSION, str);
        }
    }

    public void setMyCHListener(MyCHListener myCHListener) {
        this.mMyCHListener = myCHListener;
    }

    public void setIPrePostCallBack(IPrePostCallBack iPrePostCallBack) {
        this.mIPrePostCallBack = iPrePostCallBack;
    }

    public String getLastTimeCrashDir() {
        return this.mLastTimeCrashDir;
    }

    public void setLastTimeCrashDir(String str) {
        this.mLastTimeCrashDir = str;
    }
}