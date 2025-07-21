package com.netease.androidcrashhandler.init;

import android.content.Context;
import android.text.TextUtils;
import com.netease.androidcrashhandler.Const;
import com.netease.androidcrashhandler.NTCrashHunterKit;
import com.netease.androidcrashhandler.util.CUtil;
import com.netease.androidcrashhandler.util.LogUtils;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import java.io.File;

/* loaded from: classes5.dex */
public class InitProxy {
    private static final String TAG = "Init";
    public static String sConfigFilePath = null;
    public static String sFilesDir = null;
    private static InitProxy sInitProxy = null;
    public static String sOldUploadFilePath = "";
    public static String sUniTraceDir;
    public static String sUploadFilePath;
    public String mConfigUrl;
    public String mUploadUrl;
    public String mPacakageName = null;
    public long mStartTime = 0;
    public String mCallbackSoPath = "";
    public String mCallbackMethodName = "";
    public String mResVersion = null;
    public String mEngineVersion = null;
    public String mBranch = null;
    private boolean mIsOpenBreakpad = true;
    private boolean mIsDetectJavaCrash = true;
    public String mEB = "-1";
    public int mIsLastTimeCrash = 0;
    public int mIsLastTimeAnr = 0;
    public int mIsLastTimeUnKnownException = 0;
    public String mImei = null;
    private String mTransid = "";
    private String mUnisdkDeviceId = null;
    private String mLocalIp = null;
    private String mUrl = "";
    private String mHost = "";
    private String mProject = "";

    private InitProxy() {
    }

    public static InitProxy getInstance() {
        if (sInitProxy == null) {
            LogUtils.i(LogUtils.TAG, "InitProxy [getInstance] start");
            sInitProxy = new InitProxy();
        }
        return sInitProxy;
    }

    public void initAfterThroughUserAgreement(Context context) throws NumberFormatException {
        NTCrashHunterKit.sharedKit().setParam(ClientLogConstant.TRANSID, getInstance().getmTransid());
    }

    public void init(Context context) {
        LogUtils.i(LogUtils.TAG, "InitProxy [init] start");
        if (context == null) {
            LogUtils.i(LogUtils.TAG, "InitProxy [init] params error");
            return;
        }
        NTCrashHunterKit.sharedKit().setParam("os_type", "Android");
        this.mPacakageName = context.getPackageName();
        sFilesDir = context.getFilesDir().getAbsolutePath();
        sOldUploadFilePath = sFilesDir + "/crashhunter";
        sUniTraceDir = sFilesDir + "/uniTrace";
        sUploadFilePath = sUniTraceDir + "/crashhunter";
        sConfigFilePath = sFilesDir + "/crashhunter_config";
        File file = new File(sUniTraceDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(sUploadFilePath);
        if (!file2.exists()) {
            file2.mkdirs();
        }
        File file3 = new File(sConfigFilePath);
        if (!file3.exists()) {
            file3.mkdirs();
        }
        this.mEB = CUtil.getEB(context);
        this.mStartTime = System.currentTimeMillis();
        String versionName = CUtil.getVersionName(context);
        this.mResVersion = versionName;
        if (!TextUtils.isEmpty(versionName)) {
            this.mEngineVersion = this.mResVersion;
        }
        NTCrashHunterKit.sharedKit().isLastTimeCrash();
        NTCrashHunterKit.sharedKit().isLastTimeAnr();
        NTCrashHunterKit.sharedKit().isLastTimeUndefinedException();
        LogUtils.i(LogUtils.TAG, "InitProxy [init] sPacakageName=" + this.mPacakageName + ", sUploadFilePath=" + sUploadFilePath);
    }

    public String getCallbackSoPath() {
        return TextUtils.isEmpty(this.mCallbackSoPath) ? "" : this.mCallbackSoPath;
    }

    public void setCallbackSoPath(String str) {
        LogUtils.i(LogUtils.TAG, "InitProxy [setCallbackSoPath] start");
        this.mCallbackSoPath = str;
        if (!TextUtils.isEmpty(str)) {
            if (!this.mCallbackSoPath.contains(File.separator) && NTCrashHunterKit.sharedKit().getContext() != null) {
                String str2 = NTCrashHunterKit.sharedKit().getContext().getApplicationInfo().nativeLibraryDir;
                if (!TextUtils.isEmpty(str2)) {
                    this.mCallbackSoPath = str2 + "/" + this.mCallbackSoPath;
                }
            }
            LogUtils.i(LogUtils.TAG, "InitProxy [setCallbackSoPath] callbackSoPath=" + this.mCallbackSoPath);
            return;
        }
        LogUtils.i(LogUtils.TAG, "InitProxy [setCallbackSoPath] callbackSoPath \u4e3a\u7a7a");
    }

    public String getCallbackMethodName() {
        return TextUtils.isEmpty(this.mCallbackMethodName) ? "" : this.mCallbackMethodName;
    }

    public void setCallbackMethodName(String str) {
        this.mCallbackMethodName = str;
    }

    public String getResVersion() {
        return this.mResVersion;
    }

    public void setResVersion(String str) {
        this.mResVersion = str;
    }

    public String getEngineVersion() {
        return this.mEngineVersion;
    }

    public void setEngineVersion(String str) {
        this.mEngineVersion = str;
    }

    public String getmBranch() {
        return this.mBranch;
    }

    public void setmBranch(String str) {
        this.mBranch = str;
    }

    public boolean isOpenBreakpad() {
        return this.mIsOpenBreakpad;
    }

    public void setmIsOpenBreakpad(boolean z) {
        this.mIsOpenBreakpad = z;
    }

    public boolean isDetectJavaCrash() {
        return this.mIsDetectJavaCrash;
    }

    public void setmIsDetectJavaCrash(boolean z) {
        this.mIsDetectJavaCrash = z;
    }

    public String getEB() {
        return this.mEB;
    }

    public void setEB(String str) {
        this.mEB = str;
    }

    public String getUploadUrl() {
        if (TextUtils.isEmpty(this.mUploadUrl)) {
            if (!TextUtils.isEmpty(this.mUrl)) {
                this.mUploadUrl = this.mUrl;
            } else if (!TextUtils.isEmpty(this.mHost)) {
                this.mUploadUrl = String.format("https://%s/upload", this.mHost);
            } else if (TextUtils.isEmpty(getProject())) {
                this.mUploadUrl = Const.URL.DEFAULT_UPLOAD_URL;
            } else {
                this.mUploadUrl = String.format("https://%s.appdump.nie.netease.com/upload", getProject());
            }
        }
        LogUtils.i(LogUtils.TAG, "getUploadUrl " + this.mUploadUrl);
        return this.mUploadUrl;
    }

    public String getConfigUrl() {
        if (TextUtils.isEmpty(this.mConfigUrl)) {
            if (!TextUtils.isEmpty(this.mHost)) {
                this.mConfigUrl = String.format("https://%s/config", this.mHost);
            } else if (TextUtils.isEmpty(getProject())) {
                this.mConfigUrl = Const.URL.DEFAULT_CONFIG_URL;
            } else {
                this.mConfigUrl = String.format("https://%s.appdump.nie.netease.com/config", getProject());
            }
        }
        LogUtils.i(LogUtils.TAG, "getConfigUrl " + this.mConfigUrl);
        return this.mConfigUrl;
    }

    public int getIsLastTimeCrash() {
        return this.mIsLastTimeCrash;
    }

    public void setIsLastTimeCrash(int i) {
        this.mIsLastTimeCrash = i;
    }

    public int getIsLastTimeAnr() {
        return this.mIsLastTimeAnr;
    }

    public void setIsLastTimeAnr(int i) {
        this.mIsLastTimeAnr = i;
    }

    public int getIsLastTimeUnKnownException() {
        return this.mIsLastTimeUnKnownException;
    }

    public void setIsLastTimeUnKnownException(int i) {
        this.mIsLastTimeUnKnownException = i;
    }

    public String getmImei() {
        return this.mImei;
    }

    public void setmImei(String str) {
        this.mImei = str;
    }

    public String getmTransid() {
        return this.mTransid;
    }

    public void setmTransid(String str) {
        this.mTransid = str;
    }

    public String getmUnisdkDeviceId() {
        return this.mUnisdkDeviceId;
    }

    public void setmUnisdkDeviceId(String str) {
        this.mUnisdkDeviceId = str;
    }

    public String getmLocalIp() {
        return this.mLocalIp;
    }

    public void setmLocalIp(String str) {
        this.mLocalIp = str;
    }

    public void setUrl(String str) {
        this.mUrl = str;
        if (TextUtils.isEmpty(str) || !str.contains(".easebar.")) {
            return;
        }
        setEB("1");
    }

    public void setHost(String str) {
        this.mHost = str;
        if (TextUtils.isEmpty(str) || !str.contains(".easebar.")) {
            return;
        }
        setEB("1");
    }

    public String getProject() {
        return this.mProject;
    }

    public void setProject(String str) {
        this.mProject = str;
    }
}