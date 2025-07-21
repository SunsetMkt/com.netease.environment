package com.netease.androidcrashhandler;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.netease.androidcrashhandler.Const;
import com.netease.androidcrashhandler.anr.ANRWatchDogProxy;
import com.netease.androidcrashhandler.anr.messageQueue.StackManager;
import com.netease.androidcrashhandler.callback.IPrePostCallBack;
import com.netease.androidcrashhandler.callback.NTEventOccurCallBack;
import com.netease.androidcrashhandler.config.ConfigCore;
import com.netease.androidcrashhandler.entity.Extension.ExtensionInfo;
import com.netease.androidcrashhandler.entity.di.DiProxy;
import com.netease.androidcrashhandler.entity.param.ParamsInfo;
import com.netease.androidcrashhandler.init.InitProxy;
import com.netease.androidcrashhandler.javacrash.JavaCrashCallBack;
import com.netease.androidcrashhandler.javacrash.JavaCrashCore;
import com.netease.androidcrashhandler.other.NTAssociatedFile;
import com.netease.androidcrashhandler.other.OtherCore;
import com.netease.androidcrashhandler.other.OtherProxy;
import com.netease.androidcrashhandler.processCenter.TaskProxy;
import com.netease.androidcrashhandler.so.SystemSoHandler;
import com.netease.androidcrashhandler.thirdparty.clientLogModule.ClientLog;
import com.netease.androidcrashhandler.thirdparty.deviceInfoModule.DeviceInfoProxy;
import com.netease.androidcrashhandler.thirdparty.lifecycle.Lifecycle;
import com.netease.androidcrashhandler.thirdparty.unilogger.CUniLogger;
import com.netease.androidcrashhandler.unknownCrash.CheckNormalExitManager;
import com.netease.androidcrashhandler.unknownCrash.CheckNormalExitModel;
import com.netease.androidcrashhandler.util.CUtil;
import com.netease.androidcrashhandler.util.LogUtils;
import com.netease.androidcrashhandler.zip.ZipProxy;
import com.wali.gamecenter.report.ReportOrigin;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class NTCrashHunterKit {
    private static final String TAG = "CrashHunterProxy";
    private static boolean mIsStart = false;
    public static boolean mIsThroughUserAgreement = true;
    private static NTCrashHunterKit sProxy;
    private Context mContext = null;
    private ParamsInfo mLastTimeParamsInfo = null;
    private ParamsInfo mCurrentParamsInfo = null;
    private boolean mIsInit = false;
    private boolean mHasStartCrashhunterBottomHalf = false;
    private IPrePostCallBack mIPrePostCallBack = null;
    private JavaCrashCallBack mJavaCrashCallBack = null;
    private NTEventOccurCallBack mNTEventOccurCallBack = null;

    private NTCrashHunterKit() {
    }

    public static NTCrashHunterKit sharedKit() {
        if (sProxy == null) {
            sProxy = new NTCrashHunterKit();
        }
        return sProxy;
    }

    public void init(Context context) throws IOException {
        LogUtils.i(LogUtils.TAG, "CrashHunterProxy [init] start");
        LogUtils.checkContainLogFile(context);
        if (this.mIsInit) {
            LogUtils.w(LogUtils.TAG, "CrashHunterProxy [init] \u5df2\u7ecf\u521d\u59cb\u5316\uff0c\u65e0\u9700\u91cd\u65b0\u521d\u59cb\u5316");
            return;
        }
        this.mIsInit = true;
        this.mContext = context;
        LogUtils.checkDebug();
        InitProxy.getInstance().init(this.mContext);
        storageLastTimeParamsInfo();
        LogUtils.i(LogUtils.TAG, "CrashHunterProxy [handleParamsInfo] [\u7528\u6237\u534f\u8bae\u524d] mLastTimeParamsInfo=" + this.mLastTimeParamsInfo.toString());
        DeviceInfoProxy.initDeviceInfoModule(context);
        Lifecycle.getInstence().initLifecycle(context);
        ClientLog.getInstence().addModuleCallback();
        ClientLog.getInstence().sendClientLog("call method: [init]");
        LogUtils.i(LogUtils.TAG, "CrashHunterProxy [init] finish");
    }

    private void storageLastTimeParamsInfo() {
        LogUtils.i(LogUtils.TAG, "CrashHunterProxy [storageLastTimeParamsInfo] [\u7528\u6237\u534f\u8bae\u524d] \u5c06\u4e0a\u6b21\u542f\u52a8\u7684\u53c2\u6570\u6587\u4ef6\u8bfb\u53d6\u5230mLastTimeParamsInfo");
        if (this.mLastTimeParamsInfo == null) {
            ParamsInfo paramsInfo = new ParamsInfo();
            this.mLastTimeParamsInfo = paramsInfo;
            paramsInfo.getParamFromLoaclFile();
            LogUtils.i(LogUtils.TAG, "CrashHunterProxy [storageLastTimeParamsInfo] [\u7528\u6237\u534f\u8bae\u524d] \u5220\u9664\u4e0a\u6b21\u542f\u52a8\u7684\u53c2\u6570\u6587\u4ef6");
            this.mLastTimeParamsInfo.deleteParamFile();
        }
    }

    private void createCurrentParamsInfo() {
        if (this.mCurrentParamsInfo == null) {
            this.mCurrentParamsInfo = new ParamsInfo();
        }
        LogUtils.i(LogUtils.TAG, "CrashHunterProxy [createCurrentParamsInfo] [\u7528\u6237\u534f\u8bae\u524d] \u6784\u5efa\u5f53\u6b21\u542f\u52a8\u53c2\u6570\u6587\u4ef6");
        this.mCurrentParamsInfo.writeToLocalFile();
    }

    private void startAllMonitor() {
        LogUtils.i(LogUtils.TAG, "CrashHunterProxy [startAllMonitor] ANR\u76d1\u63a7\u673a\u5236\u542f\u52a8");
        ANRWatchDogProxy.getInstance().start();
        LogUtils.i(LogUtils.TAG, "CrashHunterProxy [startAllMonitor] Java\u5d29\u6e83\u6355\u6349\u673a\u5236\u542f\u52a8");
        JavaCrashCore.getInstance().setJavaCrashCallBack(this.mJavaCrashCallBack);
        JavaCrashCore.getInstance().start();
        LogUtils.i(LogUtils.TAG, "CrashHunterProxy [startAllMonitor] Native\u5d29\u6e83\u6355\u6349\u673a\u5236\u542f\u52a8");
        AndroidCrashHandler.getInstance().setLogFilePath(InitProxy.sUploadFilePath);
        AndroidCrashHandler.getInstance().start(this.mContext);
    }

    public void startHuntingCrash() {
        LogUtils.i(LogUtils.TAG, "CrashHunterProxy [startHuntingCrash] [\u7528\u6237\u534f\u8bae\u524d] start");
        ClientLog.getInstence().sendClientLog("call method: [startHuntingCrash]");
        if (mIsStart) {
            LogUtils.i(LogUtils.TAG, "CrashHunterProxy [startHuntingCrash] [\u7528\u6237\u534f\u8bae\u524d] \u5df2\u7ecf\u542f\u52a8\uff0c\u65e0\u9700\u91cd\u65b0\u542f\u52a8");
            return;
        }
        mIsStart = true;
        ConfigCore.getInstance().readLocalConfig();
        DiProxy.getInstance().init(this.mContext);
        LogUtils.i(LogUtils.TAG, "CrashHunterProxy [startHuntingCrash] [\u7528\u6237\u534f\u8bae\u524d] ismEnable=" + ConfigCore.getInstance().ismEnable());
        if (ConfigCore.getInstance().ismEnable()) {
            createCurrentParamsInfo();
            LogUtils.i(LogUtils.TAG, "CrashHunterProxy [handleParamsInfo] [\u7528\u6237\u534f\u8bae\u524d] mCurrentParamsInfo=" + this.mCurrentParamsInfo.toString());
            TaskProxy.getInstances().start();
            OtherProxy.getInstance().start();
            if (this.mIPrePostCallBack != null) {
                LogUtils.i(LogUtils.TAG, "CrashHunterProxy [start_t] [\u7528\u6237\u534f\u8bae\u524d] \u542f\u52a8\u540e\uff0c\u5728\u538b\u7f29\u4e0a\u4f20\u524d\uff0c\u56de\u8c03\u7528\u6237\u65b9\u6cd5");
                this.mIPrePostCallBack.prePostHandle();
            }
            LogUtils.i(LogUtils.TAG, "CrashHunterProxy [start_t] [\u7528\u6237\u534f\u8bae\u524d] \u5c06\u6563\u6587\u4ef6\u5c01\u88c5\u6210zip\u5165\u961f\u5217");
            new File(InitProxy.sUploadFilePath, Const.FileNameTag.UNISDK_LOG_FILE_TEMP).renameTo(new File(InitProxy.sUploadFilePath, Const.FileNameTag.UNISDK_LOG_FILE));
            sendLastTimeExceptionEvent();
            ZipProxy.getInstance().lunchZipAsync();
            DiProxy.getInstance().freshPreUserAgreement();
            startAllMonitor();
            if (mIsThroughUserAgreement) {
                throughUserAgreement();
            }
        }
        LogUtils.i(LogUtils.TAG, "CrashHunterProxy [startHuntingCrash] [\u7528\u6237\u534f\u8bae\u524d] finish");
    }

    public synchronized void throughUserAgreement() {
        LogUtils.i(LogUtils.TAG, "CrashHunterProxy [throughUserAgreement] start");
        if (!this.mHasStartCrashhunterBottomHalf) {
            LogUtils.i(LogUtils.TAG, "CrashHunterProxy [throughUserAgreement] \u5df2\u540c\u610f\u7528\u6237\u534f\u8bae\uff0c\u542f\u52a8crashhunter\u540e\u534a\u90e8");
            this.mHasStartCrashhunterBottomHalf = true;
            LogUtils.i(LogUtils.TAG, "CrashHunterProxy [throughUserAgreement] start");
            ClientLog.getInstence().sendClientLog("pass user agreement");
            DeviceInfoProxy.getUniqueData(sharedKit().getContext());
            InitProxy.getInstance().initAfterThroughUserAgreement(this.mContext);
            SystemSoHandler.getInstance().uploadSystemSo(this.mContext);
            LogUtils.i(LogUtils.TAG, "CrashHunterProxy [throughUserAgreement] \u914d\u7f6e\u6587\u4ef6\u6a21\u5757\u542f\u52a8");
            ConfigCore.getInstance().fresh();
            LogUtils.i(LogUtils.TAG, "CrashHunterProxy [throughUserAgreement] start, enable=" + ConfigCore.getInstance().ismEnable());
            ZipProxy.getInstance().dispatch();
            if (ConfigCore.getInstance().ismEnable()) {
                LogUtils.i(LogUtils.TAG, "CrashHunterProxy [throughUserAgreement] di\u6587\u4ef6\u6a21\u5757\u542f\u52a8");
                DiProxy.getInstance().start();
            }
        } else {
            LogUtils.i(LogUtils.TAG, "CrashHunterProxy [throughUserAgreement] \u5df2\u542f\u52a8crashhunter\u540e\u534a\u90e8\uff0c\u65e0\u9700\u91cd\u590d\u542f\u52a8");
        }
    }

    public boolean isInit() {
        return this.mIsInit;
    }

    public void setParam(String str, String str2) throws NumberFormatException {
        if (this.mCurrentParamsInfo == null) {
            this.mCurrentParamsInfo = new ParamsInfo();
        }
        LogUtils.i(LogUtils.TAG, "key=" + str + ", value=" + str2);
        if (DeviceInfoProxy.setUniqueData(str, str2)) {
            LogUtils.i(LogUtils.TAG, "NTCrashHunterKit [setParam] \u8bbe\u7f6e\u4e86DeviceInfoModule\u7684id");
            return;
        }
        if (Const.ParamKey.CLIENT_V.equals(str)) {
            CUtil.checkAndReset(str2);
            setParam(Const.ParamKey.RES_VERSION, InitProxy.getInstance().getResVersion());
            setParam(Const.ParamKey.ENGINE_VERSION, InitProxy.getInstance().getEngineVersion());
        } else if (Const.ParamKey.RES_VERSION.equals(str)) {
            InitProxy.getInstance().setResVersion(str2);
        } else if (Const.ParamKey.ENGINE_VERSION.equals(str)) {
            InitProxy.getInstance().setEngineVersion(str2);
        } else {
            if (Const.ParamKey.PROCOTOL_STATE.equals(str)) {
                SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(CUniLogger.source, 0);
                boolean z = sharedPreferences.getBoolean(Const.ParamKey.PROCOTOL_STATE, false);
                if ("2".equals(str2) && !z) {
                    mIsThroughUserAgreement = false;
                }
                if ("1".equals(str2)) {
                    mIsThroughUserAgreement = true;
                    sharedPreferences.edit().putBoolean(Const.ParamKey.PROCOTOL_STATE, true).apply();
                    return;
                }
                return;
            }
            if (Const.ParamKey.TARGET_THREAD.equals(str)) {
                int i = Integer.parseInt(str2);
                if (i <= 0) {
                    StackManager.getInstance().disableNativeStackTrace();
                } else if (i > 1) {
                    StackManager.getInstance().enableNativeStackTrace();
                } else {
                    CUtil.runOnMainThread(new Runnable() { // from class: com.netease.androidcrashhandler.NTCrashHunterKit.1
                        AnonymousClass1() {
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            StackManager.getInstance().enableNativeStackTrace();
                        }
                    });
                }
            } else if ("eb".equals(str)) {
                InitProxy.getInstance().setEB(str2);
            } else if ("project".equals(str)) {
                InitProxy.getInstance().setProject(str2);
            }
        }
        this.mCurrentParamsInfo.putParam(str, str2);
    }

    /* renamed from: com.netease.androidcrashhandler.NTCrashHunterKit$1 */
    class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public void run() {
            StackManager.getInstance().enableNativeStackTrace();
        }
    }

    public void setBranch(String str) {
        LogUtils.i(LogUtils.TAG, "NTCrashHunterKit [setBranch] branch=" + str);
        this.mCurrentParamsInfo.addTag("branch", str);
        InitProxy.getInstance().setmBranch(str);
    }

    public boolean safelyBindCondition(String str, String str2) {
        LogUtils.i(LogUtils.TAG, "NTCrashHunterKit [safelyBindCondition] key=" + str + ", value=" + str2);
        if (this.mCurrentParamsInfo == null) {
            this.mCurrentParamsInfo = new ParamsInfo();
        }
        return this.mCurrentParamsInfo.addTag(str, str2);
    }

    public boolean safelyUnbindCondition(String str, String str2) {
        LogUtils.i(LogUtils.TAG, "NTCrashHunterKit [safelyUnbindCondition] key=" + str + ", value=" + str2);
        if (this.mCurrentParamsInfo == null) {
            this.mCurrentParamsInfo = new ParamsInfo();
        }
        return this.mCurrentParamsInfo.removeTag(str, str2);
    }

    public void setSoParam(String str, String str2) {
        LogUtils.i(LogUtils.TAG, "CrashHunterProxy [setSoParam] key=" + str + ", value=" + str2);
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            LogUtils.i(LogUtils.TAG, "CrashHunterProxy [setSoParam] param error");
        } else if (Const.ParamKey.CALLBACK_SO_PATH.equals(str)) {
            InitProxy.getInstance().setCallbackSoPath(str2);
        } else if (Const.ParamKey.CALLBACK_METHOD_NAME.equals(str)) {
            InitProxy.getInstance().setCallbackMethodName(str2);
        }
    }

    public ParamsInfo getmLastTimeParamsInfo() {
        return this.mLastTimeParamsInfo;
    }

    public ParamsInfo getmCurrentParamsInfo() {
        return this.mCurrentParamsInfo;
    }

    public void setIPrePostCallBack(IPrePostCallBack iPrePostCallBack) {
        this.mIPrePostCallBack = iPrePostCallBack;
    }

    public void setJavaCrashCallBack(JavaCrashCallBack javaCrashCallBack) {
        this.mJavaCrashCallBack = javaCrashCallBack;
    }

    public NTEventOccurCallBack getNTEventOccurCallBack() {
        return this.mNTEventOccurCallBack;
    }

    public void setNTEventOccurCallBack(NTEventOccurCallBack nTEventOccurCallBack) {
        this.mNTEventOccurCallBack = nTEventOccurCallBack;
    }

    public void setUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        InitProxy.getInstance().setUrl(str);
    }

    public void setHost(String str) {
        LogUtils.i(LogUtils.TAG, "CrashHunterProxy [setHost] start");
        LogUtils.i(LogUtils.TAG, "CrashHunterProxy [setHost] host=" + str);
        if (TextUtils.isEmpty(str)) {
            LogUtils.i(LogUtils.TAG, "CrashHunterProxy [setHost] param error");
        } else {
            InitProxy.getInstance().setHost(str);
        }
    }

    public NTAssociatedFile initWithFile(String str, String str2, String str3) {
        return new NTAssociatedFile(str3, str2, str, "");
    }

    public NTAssociatedFile initWithFile(String str, String str2, String str3, String str4) {
        NTAssociatedFile nTAssociatedFile = new NTAssociatedFile(str3, str2, str, str4);
        if (!TextUtils.isEmpty(str4) && "obfu".equals(str4)) {
            ExtensionInfo.getInstance().addObfuFileName(str);
        }
        return nTAssociatedFile;
    }

    private OtherCore createOtherCore(NTAssociatedFile nTAssociatedFile, ArrayList<NTAssociatedFile> arrayList, String str) {
        String srcFilePath = nTAssociatedFile.getSrcFilePath();
        String srcContent = nTAssociatedFile.getSrcContent();
        String desFileName = nTAssociatedFile.getDesFileName();
        String fileFeature = nTAssociatedFile.getFileFeature();
        LogUtils.i(LogUtils.TAG, "CrashHunterProxy [postFile] mainFileSrcPath=" + srcFilePath + ", mainFileDesName =" + desFileName + ", mainFileSrcContent isEmpty =" + TextUtils.isEmpty(srcContent));
        if ((TextUtils.isEmpty(srcFilePath) && TextUtils.isEmpty(srcContent)) || TextUtils.isEmpty(desFileName)) {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("copySuccessFilePath", new JSONArray());
                JSONArray jSONArray = new JSONArray();
                if (TextUtils.isEmpty(srcFilePath)) {
                    if (TextUtils.isEmpty(desFileName)) {
                        jSONArray.put("null");
                    } else {
                        jSONArray.put(desFileName);
                    }
                } else {
                    jSONArray.put(srcFilePath);
                }
                jSONObject.put("copyFailFilePath", jSONArray);
                AndroidCrashHandler.callbackToUser(3, jSONObject);
                return null;
            } catch (Throwable unused) {
                return null;
            }
        }
        OtherCore otherCore = new OtherCore();
        if (!TextUtils.isEmpty(srcFilePath)) {
            otherCore.addMainFile(srcFilePath, desFileName);
        } else if (!TextUtils.isEmpty(srcContent)) {
            otherCore.addMainInfo(srcContent, desFileName);
        }
        if (Const.FileNameTag.FEATURE_MODULE_INFO.equals(fileFeature)) {
            File file = new File(InitProxy.sFilesDir + File.separator + "ntunisdk_so_uuids");
            if (file.exists()) {
                otherCore.addMinorFile(file.getAbsolutePath(), "ntunisdk_so_uuids");
            } else {
                String assetFileContent = CUtil.getAssetFileContent(sharedKit().getContext(), "ntunisdk_so_uuids");
                if (!TextUtils.isEmpty(assetFileContent)) {
                    otherCore.addInfo(assetFileContent, "ntunisdk_so_uuids");
                }
            }
        }
        if (arrayList != null && arrayList.size() > 0) {
            Iterator<NTAssociatedFile> it = arrayList.iterator();
            while (it.hasNext()) {
                NTAssociatedFile next = it.next();
                String srcFilePath2 = next.getSrcFilePath();
                String srcContent2 = next.getSrcContent();
                String desFileName2 = next.getDesFileName();
                if (!TextUtils.isEmpty(srcFilePath2) || !TextUtils.isEmpty(srcContent2)) {
                    if (!TextUtils.isEmpty(desFileName2)) {
                        if (!TextUtils.isEmpty(srcFilePath2)) {
                            otherCore.addMinorFile(srcFilePath2, desFileName2);
                        } else if (!TextUtils.isEmpty(srcContent2)) {
                            otherCore.addInfo(srcContent2, desFileName2);
                        }
                    }
                }
            }
        }
        if (!TextUtils.isEmpty(str)) {
            otherCore.setErrorType(str);
        }
        return otherCore;
    }

    public void postFile(NTAssociatedFile nTAssociatedFile, ArrayList<NTAssociatedFile> arrayList, String str) {
        LogUtils.i(LogUtils.TAG, "CrashHunterProxy [postFile] start");
        if (nTAssociatedFile == null || this.mContext == null) {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("copySuccessFilePath", new JSONArray());
                jSONObject.put("copyFailFilePath", new JSONArray());
                AndroidCrashHandler.callbackToUser(3, jSONObject);
                return;
            } catch (Throwable unused) {
                return;
            }
        }
        OtherCore otherCoreCreateOtherCore = createOtherCore(nTAssociatedFile, arrayList, str);
        if (otherCoreCreateOtherCore == null || OtherProxy.getInstance().put(otherCoreCreateOtherCore)) {
            return;
        }
        try {
            JSONObject jSONObject2 = new JSONObject();
            JSONArray jSONArray = new JSONArray();
            jSONArray.put(nTAssociatedFile.getDesFileName());
            jSONObject2.put("copySuccessFilePath", new JSONArray());
            jSONObject2.put("copyFailFilePath", jSONArray);
            AndroidCrashHandler.callbackToUser(3, jSONObject2);
        } catch (Throwable unused2) {
        }
    }

    public void addFiles(ArrayList<NTAssociatedFile> arrayList) {
        LogUtils.i(LogUtils.TAG, "CrashHunterProxy [addFiles] start");
        if (arrayList == null || arrayList.size() <= 0 || this.mContext == null) {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("copySuccessFilePath", new JSONArray());
                jSONObject.put("copyFailFilePath", new JSONArray());
                AndroidCrashHandler.callbackToUser(3, jSONObject);
                return;
            } catch (Throwable unused) {
                return;
            }
        }
        OtherCore otherCore = new OtherCore();
        Iterator<NTAssociatedFile> it = arrayList.iterator();
        while (it.hasNext()) {
            NTAssociatedFile next = it.next();
            String srcFilePath = next.getSrcFilePath();
            String srcContent = next.getSrcContent();
            String desFileName = next.getDesFileName();
            if (!TextUtils.isEmpty(srcFilePath) || !TextUtils.isEmpty(srcContent)) {
                if (!TextUtils.isEmpty(desFileName)) {
                    if (!TextUtils.isEmpty(srcFilePath)) {
                        otherCore.addMinorFile(srcFilePath, desFileName);
                    } else if (!TextUtils.isEmpty(srcContent)) {
                        otherCore.addInfo(srcContent, desFileName);
                    }
                }
            }
        }
        JSONArray jSONArray = new JSONArray();
        JSONArray jSONArray2 = new JSONArray();
        String str = InitProxy.sUploadFilePath;
        if (TextUtils.isEmpty(str)) {
            try {
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("copySuccessFilePath", new JSONArray());
                jSONObject2.put("copyFailFilePath", new JSONArray());
                AndroidCrashHandler.callbackToUser(3, jSONObject2);
                return;
            } catch (Throwable unused2) {
                return;
            }
        }
        otherCore.stroageAssistFile(str, new OtherCore.CopyListener() { // from class: com.netease.androidcrashhandler.NTCrashHunterKit.2
            final /* synthetic */ JSONArray val$tCopyFailFileNameArray;
            final /* synthetic */ JSONArray val$tCopySuccessFileNameArray;

            AnonymousClass2(JSONArray jSONArray3, JSONArray jSONArray22) {
                jSONArray = jSONArray3;
                jSONArray = jSONArray22;
            }

            @Override // com.netease.androidcrashhandler.other.OtherCore.CopyListener
            public void onFinish(boolean z, String str2) {
                if (z) {
                    jSONArray.put(str2);
                } else {
                    jSONArray.put(str2);
                }
            }
        });
        try {
            JSONObject jSONObject3 = new JSONObject();
            jSONObject3.put("copySuccessFilePath", jSONArray3);
            jSONObject3.put("copyFailFilePath", jSONArray22);
            AndroidCrashHandler.callbackToUser(3, jSONObject3);
        } catch (Throwable unused3) {
        }
    }

    /* renamed from: com.netease.androidcrashhandler.NTCrashHunterKit$2 */
    class AnonymousClass2 implements OtherCore.CopyListener {
        final /* synthetic */ JSONArray val$tCopyFailFileNameArray;
        final /* synthetic */ JSONArray val$tCopySuccessFileNameArray;

        AnonymousClass2(JSONArray jSONArray3, JSONArray jSONArray22) {
            jSONArray = jSONArray3;
            jSONArray = jSONArray22;
        }

        @Override // com.netease.androidcrashhandler.other.OtherCore.CopyListener
        public void onFinish(boolean z, String str2) {
            if (z) {
                jSONArray.put(str2);
            } else {
                jSONArray.put(str2);
            }
        }
    }

    public Context getContext() {
        return this.mContext;
    }

    public void addExtensionInfo(JSONObject jSONObject) {
        ExtensionInfo.getInstance().addExtensionInfo(jSONObject);
    }

    public String getUploadFileDir() {
        String str = InitProxy.sUploadFilePath;
        return TextUtils.isEmpty(str) ? "" : str;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private boolean checkLastTimeEventByType(int i) {
        String[] strArr;
        String str;
        boolean z;
        int i2 = 3;
        String str2 = Const.FileNameTag.CHECK_NORMAL_EXIT_FILE_TEMP;
        int i3 = 1;
        int i4 = 2;
        int i5 = 0;
        try {
            if (i == 0) {
                strArr = new String[]{Const.FileNameTag.ACI_FILE};
            } else if (i == 1) {
                strArr = new String[]{Const.FileNameTag.DMP_FILE};
            } else if (i == 2) {
                strArr = new String[]{Const.FileNameTag.TRACE_FILE};
            } else {
                strArr = i != 3 ? null : new String[]{Const.FileNameTag.CHECK_NORMAL_EXIT_FILE_TEMP};
            }
            try {
                File[] fileArrListFiles = new File(InitProxy.sUploadFilePath).listFiles(new FilenameFilter() { // from class: com.netease.androidcrashhandler.NTCrashHunterKit.3
                    final /* synthetic */ int val$type;

                    AnonymousClass3(int i6) {
                        i = i6;
                    }

                    @Override // java.io.FilenameFilter
                    public boolean accept(File file, String str3) {
                        int i6 = i;
                        if (i6 == 0) {
                            return str3.startsWith(Const.FileNameTag.DIR_JAVA_CRASH);
                        }
                        if (i6 == 1) {
                            return str3.startsWith(Const.FileNameTag.DIR_NATIVE_CRASH);
                        }
                        if (i6 == 2) {
                            return str3.startsWith(Const.FileNameTag.DIR_ANR);
                        }
                        if (i6 != 3) {
                            return false;
                        }
                        return str3.startsWith(Const.FileNameTag.DIR_UNDEFINED_EXCEPTION);
                    }
                });
                if (fileArrListFiles != null && fileArrListFiles.length > 0) {
                    int length = fileArrListFiles.length;
                    int i6 = 0;
                    while (i5 < length) {
                        try {
                            File file = fileArrListFiles[i5];
                            boolean zIsContainSpecialFile = CUtil.isContainSpecialFile(strArr, file.getAbsolutePath());
                            if (zIsContainSpecialFile == 0) {
                                str = str2;
                                if (i6 == i4) {
                                    ParamsInfo paramsInfo = sharedKit().getmLastTimeParamsInfo();
                                    JSONObject jSONObject = paramsInfo != null ? paramsInfo.getmParamsJson() : null;
                                    if (jSONObject != null && jSONObject.has("signal") && "3".equals(jSONObject.optString("signal"))) {
                                        zIsContainSpecialFile = CUtil.isContainSpecialFile(new String[]{Const.FileNameTag.ANR_FILE}, file.getAbsolutePath());
                                    }
                                }
                            } else if (i6 == 0) {
                                str = str2;
                                JavaCrashCore.getInstance().setLastTimeCrashDir(file.getAbsolutePath());
                            } else if (i6 == i3) {
                                str = str2;
                                AndroidCrashHandler.getInstance().setLastTimeCrashDir(file.getAbsolutePath());
                            } else if (i6 == i4) {
                                str = str2;
                                ANRWatchDogProxy.getInstance().setAnrLastTimeFileName(file.getAbsolutePath());
                            } else if (i6 != i2) {
                                str = str2;
                            } else {
                                CheckNormalExitModel checkNormalExitModelBuildUndefinedException = CheckNormalExitManager.getInstance().buildUndefinedException(file.getAbsolutePath(), str2);
                                boolean zCheckUndefinedException = CheckNormalExitManager.getInstance().checkUndefinedException(checkNormalExitModelBuildUndefinedException);
                                if (zCheckUndefinedException) {
                                    str = str2;
                                    z = zCheckUndefinedException;
                                    new File(file.getAbsolutePath(), str2).renameTo(new File(file.getAbsolutePath(), Const.FileNameTag.CHECK_NORMAL_EXIT_FILE));
                                    CheckNormalExitManager.getInstance().setLastTimeDir(file.getAbsolutePath());
                                    CheckNormalExitManager.getInstance().setLastTimeErrorType(checkNormalExitModelBuildUndefinedException.errorType);
                                } else {
                                    str = str2;
                                    z = zCheckUndefinedException;
                                }
                                zIsContainSpecialFile = z;
                            }
                            i6 |= zIsContainSpecialFile;
                            i5++;
                            str2 = str;
                            i2 = 3;
                            i3 = 1;
                            i4 = 2;
                        } catch (Throwable th) {
                            th = th;
                            i5 = i6;
                            th.printStackTrace();
                            return i5;
                        }
                    }
                    i5 = i6;
                }
                boolean zIsContainSpecialFile2 = CUtil.isContainSpecialFile(strArr, InitProxy.sOldUploadFilePath);
                if (i6 == 2 && zIsContainSpecialFile2 == 0) {
                    ParamsInfo paramsInfo2 = sharedKit().getmLastTimeParamsInfo();
                    JSONObject jSONObject2 = paramsInfo2 != null ? paramsInfo2.getmParamsJson() : null;
                    if (jSONObject2 != null && jSONObject2.has("signal") && "3".equals(jSONObject2.optString("signal"))) {
                        zIsContainSpecialFile2 = CUtil.isContainSpecialFile(new String[]{Const.FileNameTag.ANR_FILE}, InitProxy.sOldUploadFilePath);
                    }
                }
                return i5 | zIsContainSpecialFile2;
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }

    /* renamed from: com.netease.androidcrashhandler.NTCrashHunterKit$3 */
    class AnonymousClass3 implements FilenameFilter {
        final /* synthetic */ int val$type;

        AnonymousClass3(int i6) {
            i = i6;
        }

        @Override // java.io.FilenameFilter
        public boolean accept(File file, String str3) {
            int i6 = i;
            if (i6 == 0) {
                return str3.startsWith(Const.FileNameTag.DIR_JAVA_CRASH);
            }
            if (i6 == 1) {
                return str3.startsWith(Const.FileNameTag.DIR_NATIVE_CRASH);
            }
            if (i6 == 2) {
                return str3.startsWith(Const.FileNameTag.DIR_ANR);
            }
            if (i6 != 3) {
                return false;
            }
            return str3.startsWith(Const.FileNameTag.DIR_UNDEFINED_EXCEPTION);
        }
    }

    public boolean isLastTimeCrash() {
        LogUtils.i(LogUtils.TAG, "CrashHunterProxy [isLastTimeCrash] start");
        if (InitProxy.getInstance().getIsLastTimeCrash() == 0) {
            boolean z = checkLastTimeEventByType(0) || checkLastTimeEventByType(1);
            LogUtils.i(LogUtils.TAG, "CrashHunterProxy [isLastTimeCrash] result=" + z);
            if (z) {
                InitProxy.getInstance().setIsLastTimeCrash(1);
                return z;
            }
            InitProxy.getInstance().setIsLastTimeCrash(-1);
            return z;
        }
        if (InitProxy.getInstance().getIsLastTimeCrash() == 1) {
            return true;
        }
        InitProxy.getInstance().getIsLastTimeCrash();
        return false;
    }

    public boolean isLastTimeAnr() {
        LogUtils.i(LogUtils.TAG, "CrashHunterProxy [isLastTimeAnr] start");
        if (InitProxy.getInstance().getIsLastTimeAnr() == 0) {
            boolean zCheckLastTimeEventByType = checkLastTimeEventByType(2);
            LogUtils.i(LogUtils.TAG, "CrashHunterProxy [isLastTimeAnr] result=" + zCheckLastTimeEventByType);
            if (zCheckLastTimeEventByType) {
                InitProxy.getInstance().setIsLastTimeAnr(1);
            } else {
                InitProxy.getInstance().setIsLastTimeAnr(-1);
            }
            return zCheckLastTimeEventByType;
        }
        if (InitProxy.getInstance().getIsLastTimeAnr() == 1) {
            return true;
        }
        InitProxy.getInstance().getIsLastTimeAnr();
        return false;
    }

    public boolean isLastTimeUndefinedException() {
        LogUtils.i(LogUtils.TAG, "CrashHunterProxy [isLastTimeUnknownException] start");
        if (InitProxy.getInstance().getIsLastTimeUnKnownException() == 0) {
            boolean zCheckLastTimeEventByType = checkLastTimeEventByType(3);
            LogUtils.i(LogUtils.TAG, "CrashHunterProxy [isLastTimeUnknownException] result=" + zCheckLastTimeEventByType);
            if (zCheckLastTimeEventByType) {
                InitProxy.getInstance().setIsLastTimeUnKnownException(1);
            } else {
                InitProxy.getInstance().setIsLastTimeUnKnownException(-1);
            }
            return zCheckLastTimeEventByType;
        }
        if (InitProxy.getInstance().getIsLastTimeUnKnownException() == 1) {
            return true;
        }
        InitProxy.getInstance().getIsLastTimeUnKnownException();
        return false;
    }

    public void isOpenBreakpad(boolean z) {
        InitProxy.getInstance().setmIsOpenBreakpad(z);
    }

    public String getSoBuildId(String str) {
        LogUtils.i(LogUtils.TAG, "CrashHunterProxy [getSoBuildId] start");
        LogUtils.i(LogUtils.TAG, "CrashHunterProxy [getSoBuildId] soPath=" + str);
        return AndroidCrashHandler.getInstance().getSoBuildId(str);
    }

    public int unseal(Context context) {
        LogUtils.i(LogUtils.TAG, "CrashHunterProxy [unseal] not support");
        return -1;
    }

    public void setDiFreshTimeInterval(int i, boolean z) {
        DiProxy.getInstance().setDiFreshTimeInterval(i, z);
    }

    public static void handleException(Throwable th) {
        LogUtils.i(LogUtils.TAG, "CrashHunterProxy [handleException] start");
        JavaCrashCore.getInstance().handleException(Thread.currentThread(), th);
    }

    public void registerMemoryAdvice(Activity activity) {
        CheckNormalExitManager.getInstance().initMemoryAdvice(activity);
    }

    private void sendLastTimeExceptionEvent() {
        if (isLastTimeUndefinedException()) {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("file_path", CheckNormalExitManager.getInstance().getLastTimeDir());
                jSONObject.put(ReportOrigin.ORIGIN_CATEGORY, CheckNormalExitManager.getInstance().getLastTimeErrorType());
                AndroidCrashHandler.callbackToUser(12, jSONObject);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }
}