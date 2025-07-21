package com.netease.androidcrashhandler.unknownCrash;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ApplicationExitInfo;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Debug;
import android.os.Process;
import android.text.TextUtils;
import com.alipay.sdk.m.u.b;
import com.netease.androidcrashhandler.AndroidCrashHandler;
import com.netease.androidcrashhandler.Const;
import com.netease.androidcrashhandler.NTCrashHunterKit;
import com.netease.androidcrashhandler.init.InitProxy;
import com.netease.androidcrashhandler.thirdparty.lifecycle.Lifecycle;
import com.netease.androidcrashhandler.unknownCrash.MemoryManager;
import com.netease.androidcrashhandler.util.BatteryUtil;
import com.netease.androidcrashhandler.util.CEmulatorDetector;
import com.netease.androidcrashhandler.util.CUtil;
import com.netease.androidcrashhandler.util.LogUtils;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import com.netease.ntunisdk.unilogger.utils.Utils;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class CheckNormalExitManager extends Thread implements AppLifeCallback, MemoryManager.MemoryInterface {
    private static final CheckNormalExitManager MANAGER = new CheckNormalExitManager();
    public static final String TAG = "[unexpected_crash] ";
    private boolean isInit;
    private Context mContext;
    private String mPath;
    private boolean mRunning;
    private CheckNormalExitModel mStatus = new CheckNormalExitModel();
    private boolean isInitMemoryAdvice = false;
    private final byte[] mLock = new byte[0];
    private String mLastTimeDir = "";
    private String mLastTimeErrorType = "";

    @Override // com.netease.androidcrashhandler.unknownCrash.AppLifeCallback
    public void onActivityCreate() {
    }

    @Override // com.netease.androidcrashhandler.unknownCrash.AppLifeCallback
    public void onActivityStart() {
    }

    @Override // com.netease.androidcrashhandler.unknownCrash.AppLifeCallback
    public void onActivityStop() {
    }

    public static CheckNormalExitManager getInstance() {
        return MANAGER;
    }

    private CheckNormalExitManager() {
    }

    public String getLastTimeDir() {
        return this.mLastTimeDir;
    }

    public void setLastTimeDir(String str) {
        this.mLastTimeDir = str;
    }

    public String getLastTimeErrorType() {
        return this.mLastTimeErrorType;
    }

    public void setLastTimeErrorType(String str) {
        this.mLastTimeErrorType = str;
    }

    public void init(Context context) {
        if (this.isInit) {
            return;
        }
        this.mContext = context.getApplicationContext();
        boolean zIsDebugModel = isDebugModel();
        boolean zDetectLocal = CEmulatorDetector.detectLocal(this.mContext);
        LogUtils.i(LogUtils.TAG, "CheckNormalExitManager [init] isDebug or isEmulator:" + zIsDebugModel + "_" + zDetectLocal);
        if (zDetectLocal || zIsDebugModel) {
            return;
        }
        File file = new File(InitProxy.sUploadFilePath, Const.FileNameTag.DIR_UNDEFINED_EXCEPTION + Process.myPid() + "_" + System.currentTimeMillis());
        if (!file.exists()) {
            file.mkdirs();
        }
        this.mPath = file.getAbsolutePath();
        File file2 = new File(this.mPath, Const.FileNameTag.CHECK_NORMAL_EXIT_FILE_TEMP);
        getReadyThisTime();
        try {
            file2.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.mContext.registerReceiver(new ShutdownBroadcastReceiver(), new IntentFilter("android.intent.action.ACTION_SHUTDOWN"));
        this.isInit = true;
        LogUtils.i(LogUtils.TAG, "CheckNormalExitManager [init] finish");
    }

    public synchronized void initMemoryAdvice(Context context) {
        if (this.isInit && !this.isInitMemoryAdvice) {
            Activity activityFindActivity = findActivity(context);
            if (activityFindActivity != null) {
                if (MemoryManager.memoryAdviceInit(activityFindActivity) == 0 && MemoryManager.getInstance().registerMemoryWater() == 0) {
                    MemoryManager.getInstance().registerMemoryState(this);
                    this.isInitMemoryAdvice = true;
                    this.mStatus.memoryState = 0;
                    writeInfo();
                }
            } else {
                LogUtils.i(LogUtils.TAG, "CheckNormalExitManager [initMemoryAdvice] activity is null");
            }
        }
    }

    private Activity findActivity(Context context) {
        try {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            if (context instanceof ContextWrapper) {
                return findActivity(((ContextWrapper) context).getBaseContext());
            }
            return null;
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    private void getReadyThisTime() {
        this.mStatus.exitType = 0;
        this.mStatus.lastTime = System.currentTimeMillis();
        this.mStatus.startTime = "";
        LogUtils.i(LogUtils.TAG, "[unexpected_crash] CheckNormalExitManager [getReadyThisTime] finish");
        writeInfo();
    }

    private CheckNormalExitModel buildData(String str) {
        CheckNormalExitModel checkNormalExitModel = new CheckNormalExitModel();
        try {
            JSONObject jSONObject = new JSONObject(str);
            LogUtils.i(LogUtils.TAG, "[unexpected_crash] CheckNormalExitManager [buildData] start");
            checkNormalExitModel.startTime = jSONObject.optString(CheckNormalExitModel.JSON_STARTTIME);
            checkNormalExitModel.isAppForeground = jSONObject.optBoolean(CheckNormalExitModel.JSON_ISAPPFOREGROUND);
            checkNormalExitModel.exitTime = jSONObject.optString(CheckNormalExitModel.JSON_EXITTIME);
            checkNormalExitModel.exitType = jSONObject.optInt(CheckNormalExitModel.JSON_EXITTYPE);
            JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject(CheckNormalExitModel.JSON_MEMORYLIST);
            Iterator<String> itKeys = jSONObjectOptJSONObject.keys();
            while (itKeys.hasNext()) {
                String next = itKeys.next();
                Long lValueOf = Long.valueOf(Long.parseLong(next));
                JSONArray jSONArray = jSONObjectOptJSONObject.getJSONArray(next);
                ArrayList arrayList = new ArrayList();
                for (int i = 0; i < jSONArray.length(); i++) {
                    arrayList.add(Long.valueOf(jSONArray.getLong(i)));
                }
                checkNormalExitModel.memoryList.put(lValueOf, arrayList);
            }
            checkNormalExitModel.systemTotalMemory = jSONObject.optLong(CheckNormalExitModel.JSON_SYSTEMTOTALMEMORY);
            checkNormalExitModel.lastTime = jSONObject.optLong("lasttime");
            checkNormalExitModel.isCharge = jSONObject.optBoolean(CheckNormalExitModel.JSON_ISCHARGE);
            checkNormalExitModel.power = jSONObject.optDouble(CheckNormalExitModel.JSON_POWER);
            checkNormalExitModel.memoryState = jSONObject.optInt(CheckNormalExitModel.JSON_MEMORYSTATE);
            checkNormalExitModel.memoryCriticalTime = jSONObject.optLong(CheckNormalExitModel.JSON_MEMORYCRITICALTIME);
            checkNormalExitModel.memoryCriticalCount = jSONObject.optInt(CheckNormalExitModel.JSON_MEMORYCRITICALCOUNT);
            checkNormalExitModel.pid = jSONObject.optLong(CheckNormalExitModel.JSON_PID);
            checkNormalExitModel.systemState = jSONObject.optInt(CheckNormalExitModel.JSON_SYSTEM_STATE);
            checkNormalExitModel.versionName = jSONObject.optString("app_version");
            LogUtils.i(LogUtils.TAG, "CheckNormalExitManager [initData] finish");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return checkNormalExitModel;
    }

    private boolean isDebugModel() {
        return Debug.isDebuggerConnected();
    }

    public void setNormalExit() {
        if (this.isInit) {
            String str = new SimpleDateFormat(ClientLogConstant.DATA_FORMAT, Locale.ENGLISH).format(new Date(System.currentTimeMillis()));
            String str2 = new SimpleDateFormat("Z", Locale.ENGLISH).format(new Date());
            this.mStatus.exitTime = str + " " + str2;
            this.mStatus.exitType = 1;
            LogUtils.i(LogUtils.TAG, "CheckNormalExitManager [refreshStartTime ] app normal exit");
            writeInfo();
        }
    }

    public synchronized void refreshStartTime() {
        LogUtils.i(LogUtils.TAG, "CheckNormalExitManager [refreshStartTime] isInit_mRunning: " + this.isInit + "_" + this.mRunning);
        if (this.isInit) {
            if (!this.mRunning) {
                this.mRunning = true;
                AndroidCrashHandler.hookAppExit(this.mPath);
                String str = new SimpleDateFormat(ClientLogConstant.DATA_FORMAT, Locale.ENGLISH).format(new Date());
                String str2 = new SimpleDateFormat("Z", Locale.ENGLISH).format(new Date());
                this.mStatus.versionName = CUtil.getVersionName(this.mContext);
                this.mStatus.pid = Process.myPid();
                this.mStatus.startTime = str + " " + str2;
                this.mStatus.memoryState = this.isInitMemoryAdvice ? 0 : -1;
                LogUtils.i(LogUtils.TAG, "CheckNormalExitManager [refreshStartTime ] app start:" + this.mStatus.startTime);
                writeInfo();
                setName("CheckNormalExit");
                start();
                LogUtils.i(LogUtils.TAG, "CheckNormalExitManager [refreshStartTime] finish");
            }
        }
    }

    public synchronized void terminal() {
        LogUtils.i(LogUtils.TAG, "CheckNormalExitManager [terminal ] ");
        if (this.mRunning) {
            this.mRunning = false;
            interrupt();
            LogUtils.i(LogUtils.TAG, "CheckNormalExitManager [terminal ] mRunning stop ");
        }
        if (this.isInit) {
            this.mStatus.startTime = "";
            writeInfo();
            LogUtils.i(LogUtils.TAG, "CheckNormalExitManager [terminal ] reset ");
        }
    }

    public CheckNormalExitModel buildUndefinedException(String str, String str2) {
        CheckNormalExitModel checkNormalExitModelBuildData = buildData(Utils.file2Str(str, str2));
        LogUtils.d(LogUtils.TAG, "CheckNormalExitManager [checkLastTimeExit] start");
        try {
            if (!TextUtils.isEmpty(checkNormalExitModelBuildData.startTime)) {
                if (checkNormalExitModelBuildData.isAppForeground) {
                    File file = new File(str + "/app_exit.temp");
                    if (file.exists()) {
                        checkNormalExitModelBuildData.exitTime = new SimpleDateFormat(ClientLogConstant.DATA_FORMAT, Locale.ENGLISH).format(new Date(file.lastModified())) + " " + new SimpleDateFormat("Z", Locale.ENGLISH).format(new Date());
                        checkNormalExitModelBuildData.exitType = 1;
                        LogUtils.d(LogUtils.TAG, "CheckNormalExitManager [checkLastTimeExit] by app exit");
                        file.delete();
                    }
                    if (NTCrashHunterKit.sharedKit().isLastTimeCrash()) {
                        LogUtils.d(LogUtils.TAG, "CheckNormalExitManager [checkLastTimeExit] JAVA_CRASH_EXIT_TYPE");
                        checkNormalExitModelBuildData.exitType = 3;
                    }
                    if (checkNormalExitModelBuildData.exitType == 0 && NTCrashHunterKit.sharedKit().isLastTimeAnr()) {
                        LogUtils.d(LogUtils.TAG, "CheckNormalExitManager [checkLastTimeExit] ANR_EXIT_TYPE");
                        checkNormalExitModelBuildData.exitType = 2;
                    }
                    if (checkNormalExitModelBuildData.exitType == 0 && checkMemoryWarn(checkNormalExitModelBuildData)) {
                        checkNormalExitModelBuildData.exitType = 9;
                        checkNormalExitModelBuildData.errorType = CheckNormalExitModel.REPORT_ERROR_TYPE_NATIVE_OOM;
                    }
                    if (checkNormalExitModelBuildData.exitType == 0 && !checkNormalExitModelBuildData.isCharge && checkNormalExitModelBuildData.power < 0.1d) {
                        checkNormalExitModelBuildData.exitType = 5;
                        checkNormalExitModelBuildData.errorType = CheckNormalExitModel.REPORT_ERROR_TYPE_UNDEFINED_EXCEPTION;
                        LogUtils.d(LogUtils.TAG, "CheckNormalExitManager [checkLastTimeExit] REBOOT_EXIT_BY_POWER_TYPE");
                    }
                    int historyProcessExitReason = getHistoryProcessExitReason(checkNormalExitModelBuildData.pid, checkNormalExitModelBuildData.lastTime);
                    checkNormalExitModelBuildData.systemState = historyProcessExitReason;
                    if (checkNormalExitModelBuildData.exitType == 0 && historyProcessExitReason > 0) {
                        if (historyProcessExitReason == 1 || historyProcessExitReason == 11 || historyProcessExitReason == 10) {
                            checkNormalExitModelBuildData.exitType = 1;
                        } else if (historyProcessExitReason == 3) {
                            checkNormalExitModelBuildData.exitType = 9;
                            checkNormalExitModelBuildData.errorType = CheckNormalExitModel.REPORT_ERROR_TYPE_NATIVE_OOM;
                        } else {
                            checkNormalExitModelBuildData.exitType = 6;
                            checkNormalExitModelBuildData.errorType = CheckNormalExitModel.REPORT_ERROR_TYPE_UNDEFINED_EXCEPTION;
                        }
                    }
                    if (checkNormalExitModelBuildData.exitType == 0) {
                        String versionName = CUtil.getVersionName(this.mContext);
                        if (!TextUtils.isEmpty(checkNormalExitModelBuildData.versionName) && !versionName.equals(checkNormalExitModelBuildData.versionName)) {
                            checkNormalExitModelBuildData.exitType = 10;
                        }
                    }
                    if (checkNormalExitModelBuildData.exitType == 0) {
                        LogUtils.d(LogUtils.TAG, "CheckNormalExitManager [checkLastTimeExit] UNKNOWN_EXIT_TYPE");
                        checkNormalExitModelBuildData.exitType = 7;
                        checkNormalExitModelBuildData.errorType = CheckNormalExitModel.REPORT_ERROR_TYPE_UNDEFINED_EXCEPTION;
                    }
                } else {
                    checkNormalExitModelBuildData.exitType = 8;
                    LogUtils.d(LogUtils.TAG, "CheckNormalExitManager [checkLastTimeExit] USER_EXIT_BY_BACKGROUND_TYPE");
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        LogUtils.d(LogUtils.TAG, "CheckNormalExitManager [checkLastTimeExit] exitType:" + checkNormalExitModelBuildData.exitType);
        return checkNormalExitModelBuildData;
    }

    private int getHistoryProcessExitReason(long j, long j2) {
        if (Build.VERSION.SDK_INT < 30) {
            return -1;
        }
        List<ApplicationExitInfo> historicalProcessExitReasons = ((ActivityManager) NTCrashHunterKit.sharedKit().getContext().getSystemService("activity")).getHistoricalProcessExitReasons(NTCrashHunterKit.sharedKit().getContext().getPackageName(), (int) j, 0);
        for (int i = 0; i < historicalProcessExitReasons.size(); i++) {
            ApplicationExitInfo applicationExitInfo = historicalProcessExitReasons.get(i);
            if (Math.abs(applicationExitInfo.getTimestamp() - j2) < 30000) {
                LogUtils.d(LogUtils.TAG, "CheckNormalExitManager [getHistoryProcessExitReason] Reason===" + applicationExitInfo.getReason());
                return applicationExitInfo.getReason();
            }
        }
        return -1;
    }

    private boolean checkMemoryWarn(CheckNormalExitModel checkNormalExitModel) {
        if (checkNormalExitModel.memoryState != 3) {
            return false;
        }
        LogUtils.d(LogUtils.TAG, "CheckNormalExitManager [checkMemoryWarn] memory advice===" + checkNormalExitModel.lastTime + "===" + checkNormalExitModel.memoryCriticalTime);
        return checkNormalExitModel.lastTime - checkNormalExitModel.memoryCriticalTime < 5000;
    }

    public boolean checkUndefinedException(CheckNormalExitModel checkNormalExitModel) {
        return (checkNormalExitModel == null || TextUtils.isEmpty(checkNormalExitModel.startTime) || TextUtils.isEmpty(checkNormalExitModel.errorType)) ? false : true;
    }

    public String buildMainFile(String str, String str2, CheckNormalExitModel checkNormalExitModel) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("error_type", str2);
            JSONObject jSONObject2 = new JSONObject();
            try {
                jSONObject2.put(CheckNormalExitModel.JSON_STARTTIME, checkNormalExitModel.startTime);
                jSONObject2.put(CheckNormalExitModel.JSON_ISAPPFOREGROUND, checkNormalExitModel.isAppForeground);
                jSONObject2.put(CheckNormalExitModel.JSON_EXITTIME, checkNormalExitModel.exitTime);
                jSONObject2.put(CheckNormalExitModel.JSON_EXITTYPE, checkNormalExitModel.exitType);
                jSONObject2.put(CheckNormalExitModel.JSON_ERRORTYPE, checkNormalExitModel.errorType);
                if (checkNormalExitModel.memoryList != null) {
                    HashMap map = new HashMap();
                    for (Map.Entry<Long, ArrayList<Long>> entry : checkNormalExitModel.memoryList.entrySet()) {
                        map.put(String.valueOf(entry.getKey()), entry.getValue());
                    }
                    jSONObject2.put(CheckNormalExitModel.JSON_MEMORYLIST, new JSONObject(map));
                }
                jSONObject2.put(CheckNormalExitModel.JSON_SYSTEMTOTALMEMORY, checkNormalExitModel.systemTotalMemory);
                jSONObject2.put("lasttime", checkNormalExitModel.lastTime);
                jSONObject2.put(CheckNormalExitModel.JSON_ISCHARGE, checkNormalExitModel.isCharge);
                jSONObject2.put(CheckNormalExitModel.JSON_POWER, checkNormalExitModel.power);
                jSONObject2.put(CheckNormalExitModel.JSON_MEMORYSTATE, checkNormalExitModel.memoryState);
                jSONObject2.put(CheckNormalExitModel.JSON_MEMORYCRITICALCOUNT, checkNormalExitModel.memoryCriticalCount);
                jSONObject2.put(CheckNormalExitModel.JSON_MEMORYCRITICALTIME, checkNormalExitModel.memoryCriticalTime);
                jSONObject2.put(CheckNormalExitModel.JSON_PID, checkNormalExitModel.pid);
                jSONObject2.put(CheckNormalExitModel.JSON_SYSTEM_STATE, checkNormalExitModel.systemState);
                jSONObject2.put("app_version", checkNormalExitModel.versionName);
                LogUtils.i(LogUtils.TAG, "[unexpected_crash]  CheckNormalExitManager [writeInfo ] finish:" + jSONObject2.toString());
            } catch (Throwable th) {
                th.printStackTrace();
            }
            jSONObject.put("data", jSONObject2);
            jSONObject.put("time", checkNormalExitModel.setTime(checkNormalExitModel.lastTime));
        } catch (Throwable th2) {
            th2.printStackTrace();
        }
        String string = jSONObject.toString();
        LogUtils.i(LogUtils.TAG, "CheckNormalExitManager [upload]  \u4e0a\u4f20\u6570\u636e result=" + string);
        String str3 = System.currentTimeMillis() + "_undefined_exception.exc";
        CUtil.str2File(string, str, str3);
        return str3;
    }

    private void writeInfo() {
        if (this.mStatus == null) {
            return;
        }
        synchronized (this.mLock) {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put(CheckNormalExitModel.JSON_STARTTIME, this.mStatus.startTime);
                jSONObject.put(CheckNormalExitModel.JSON_ISAPPFOREGROUND, this.mStatus.isAppForeground);
                jSONObject.put(CheckNormalExitModel.JSON_EXITTIME, this.mStatus.exitTime);
                jSONObject.put(CheckNormalExitModel.JSON_EXITTYPE, this.mStatus.exitType);
                HashMap map = new HashMap();
                for (Map.Entry<Long, ArrayList<Long>> entry : this.mStatus.memoryList.entrySet()) {
                    map.put(String.valueOf(entry.getKey()), entry.getValue());
                }
                jSONObject.put(CheckNormalExitModel.JSON_MEMORYLIST, new JSONObject(map));
                jSONObject.put(CheckNormalExitModel.JSON_SYSTEMTOTALMEMORY, this.mStatus.systemTotalMemory);
                jSONObject.put("lasttime", this.mStatus.lastTime);
                jSONObject.put(CheckNormalExitModel.JSON_ISCHARGE, this.mStatus.isCharge);
                jSONObject.put(CheckNormalExitModel.JSON_POWER, this.mStatus.power);
                jSONObject.put(CheckNormalExitModel.JSON_MEMORYSTATE, this.mStatus.memoryState);
                jSONObject.put(CheckNormalExitModel.JSON_MEMORYCRITICALCOUNT, this.mStatus.memoryCriticalCount);
                jSONObject.put(CheckNormalExitModel.JSON_MEMORYCRITICALTIME, this.mStatus.memoryCriticalTime);
                jSONObject.put(CheckNormalExitModel.JSON_PID, this.mStatus.pid);
                jSONObject.put(CheckNormalExitModel.JSON_SYSTEM_STATE, this.mStatus.systemState);
                jSONObject.put("app_version", this.mStatus.versionName);
                LogUtils.i(LogUtils.TAG, "[unexpected_crash]  CheckNormalExitManager [writeInfo ] finish:" + jSONObject.toString());
            } catch (Throwable th) {
                th.printStackTrace();
            }
            Utils.str2File(jSONObject.toString(), this.mPath, Const.FileNameTag.CHECK_NORMAL_EXIT_FILE_TEMP);
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.mRunning && !interrupted()) {
            this.mStatus.lastTime = System.currentTimeMillis();
            this.mStatus.power = BatteryUtil.getBatteryLevelInfo(this.mContext);
            this.mStatus.isCharge = BatteryUtil.getBatteryIsCharge(this.mContext);
            this.mStatus.isAppForeground = Lifecycle.getInstence().isForeground();
            writeInfo();
            try {
                sleep(b.f1465a);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.netease.androidcrashhandler.unknownCrash.AppLifeCallback
    public void onActivityDestroy() {
        LogUtils.e(LogUtils.TAG, "CheckNormalExitManager [onActivityDestroy ]");
    }

    @Override // com.netease.androidcrashhandler.unknownCrash.AppLifeCallback
    public void isAppForeground(boolean z) {
        if (!this.isInit) {
            LogUtils.i(LogUtils.TAG, "CheckNormalExitManager [isAppForeground] not init");
            return;
        }
        if (z) {
            CheckNormalExitModel checkNormalExitModel = this.mStatus;
            if (checkNormalExitModel == null) {
                return;
            }
            checkNormalExitModel.isAppForeground = true;
            LogUtils.i(LogUtils.TAG, "CheckNormalExitManager [onForeground ]");
        } else {
            CheckNormalExitModel checkNormalExitModel2 = this.mStatus;
            if (checkNormalExitModel2 == null) {
                return;
            }
            checkNormalExitModel2.isAppForeground = false;
            LogUtils.i(LogUtils.TAG, "CheckNormalExitManager [onBackground ]");
        }
        writeInfo();
    }

    @Override // com.netease.androidcrashhandler.unknownCrash.MemoryManager.MemoryInterface
    public void onMemoryStateChanged(int i, int i2) {
        this.mStatus.memoryState = i;
        if (i >= 2) {
            this.mStatus.memoryCriticalCount = i2;
            this.mStatus.memoryCriticalTime = System.currentTimeMillis();
            MemoryStatus status = MemoryManager.getInstance().getStatus();
            if (this.mStatus.systemTotalMemory <= 0) {
                this.mStatus.systemTotalMemory = status.systemTotalPss / 1024;
            }
            ArrayList arrayList = new ArrayList();
            arrayList.add(Long.valueOf(status.totalPss / 1024));
            arrayList.add(Long.valueOf(status.systemFreePss / 1024));
            this.mStatus.memoryList.put(Long.valueOf(this.mStatus.memoryCriticalTime), arrayList);
        }
        LogUtils.i(LogUtils.TAG, "[unexpected_crash] onMemoryStateChanged state:" + i + "_" + i2);
        writeInfo();
    }
}