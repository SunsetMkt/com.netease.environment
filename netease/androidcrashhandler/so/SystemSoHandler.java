package com.netease.androidcrashhandler.so;

import android.content.Context;
import android.text.TextUtils;
import com.netease.androidcrashhandler.NTCrashHunterKit;
import com.netease.androidcrashhandler.config.ConfigCore;
import com.netease.androidcrashhandler.init.InitProxy;
import com.netease.androidcrashhandler.net.RequestCallback;
import com.netease.androidcrashhandler.net.SystemSoTokenRequest;
import com.netease.androidcrashhandler.net.UploadSystemSoRequest;
import com.netease.androidcrashhandler.processCenter.TaskProxy;
import com.netease.androidcrashhandler.util.CUtil;
import com.netease.androidcrashhandler.util.HashUtil;
import com.netease.androidcrashhandler.util.LogUtils;
import com.netease.androidcrashhandler.util.WifiUtil;
import com.netease.download.Const;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class SystemSoHandler {
    public static final int EXCEPTION = 2;
    public static final int REQUEST_TOKEN_SUCESS = 4;
    public static String SYSTEM_SO_PATH_32 = "/system/lib/";
    public static String SYSTEM_SO_PATH_64 = "/system/lib64/";
    private static String TAG = "SystemSoHandler";
    public static final int UPLOADING = 3;
    private static String UPLOAD_SUCCESS_SYSTEM_SO_FILE = "uploadSuccessSystemSo.txt";
    public static final int UPLOAD_SUCESS = 1;
    private static SystemSoHandler sSystemSoHandler;
    private List<Unit> mUnUploadSystemSoList = null;
    private boolean mHasInit = false;
    private String mCrashhunterSystemSoDir = null;
    private boolean mAllSystemSoUploadSucess = false;
    private boolean mUploading = false;
    private int mIndex = 0;

    public class Unit {
        public String mParentDir;
        public String mSoMd5;
        public String mSoName;
        public long mSoSize;
        public String mSoUuid;

        public Unit(String str, String str2, String str3, long j, String str4) {
            this.mParentDir = str;
            this.mSoName = str2;
            this.mSoUuid = str3;
            this.mSoSize = j;
            this.mSoMd5 = str4;
        }

        public Unit(String str, String str2, String str3) {
            this.mSoSize = -1L;
            this.mSoMd5 = "";
            this.mSoName = str2;
            this.mSoUuid = str3;
        }

        public String toString() {
            return "Unit{mParentDir='" + this.mParentDir + "', mSoName='" + this.mSoName + "', mSoUuid='" + this.mSoUuid + "', mSoSize=" + this.mSoSize + ", mSoMd5='" + this.mSoMd5 + "'}";
        }
    }

    public class UpLoadUnit {
        public int mCode;
        public long mExpireIn;
        public long mStartTime;
        public String mToken;
        public JSONArray mUploadSoArray;

        public UpLoadUnit(int i, String str, long j, long j2, JSONArray jSONArray) {
            this.mCode = i;
            this.mToken = str;
            this.mStartTime = j;
            this.mExpireIn = j2;
            this.mUploadSoArray = jSONArray;
        }

        public UpLoadUnit(int i) {
            this.mCode = i;
            this.mToken = null;
            this.mStartTime = 0L;
            this.mExpireIn = 0L;
            this.mUploadSoArray = null;
        }

        public String toString() {
            return "UpLoadUnit{code=" + this.mCode + ", mToken='" + this.mToken + "', mStartTime=" + this.mStartTime + ", mExpireIn=" + this.mExpireIn + ", mUploadSoArray=" + this.mUploadSoArray + '}';
        }
    }

    private SystemSoHandler() {
    }

    public static SystemSoHandler getInstance() {
        if (sSystemSoHandler == null) {
            sSystemSoHandler = new SystemSoHandler();
        }
        return sSystemSoHandler;
    }

    private void init(Context context) throws IOException {
        if (this.mHasInit) {
            return;
        }
        LogUtils.i(LogUtils.TAG, "SystemSoHandler [init] start");
        this.mHasInit = true;
        this.mCrashhunterSystemSoDir = InitProxy.sUploadFilePath + "_system_so";
        try {
            File file = new File(this.mCrashhunterSystemSoDir, UPLOAD_SUCCESS_SYSTEM_SO_FILE);
            if (file.exists()) {
                return;
            }
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.i(LogUtils.TAG, "SystemSoHandler [init] Exception = " + e.toString());
        }
    }

    private void addUploadSuccessSystemSo2File(String str, String str2) throws Throwable {
        LogUtils.i(LogUtils.TAG, "SystemSoHandler [addUploadSuccessSystemSo2File] start");
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            LogUtils.i(LogUtils.TAG, "SystemSoHandler [addUploadSuccessSystemSo2File] params error");
            return;
        }
        JSONObject uploadSuccessSystemSo = getUploadSuccessSystemSo();
        try {
            if (uploadSuccessSystemSo.has("systemSoName")) {
                return;
            }
            uploadSuccessSystemSo.put(str, str2);
            CUtil.str2File(uploadSuccessSystemSo.toString(), this.mCrashhunterSystemSoDir, UPLOAD_SUCCESS_SYSTEM_SO_FILE, true);
            LogUtils.i(LogUtils.TAG, "SystemSoHandler [addUploadSuccessSystemSo2File] \u6700\u65b0\u672c\u5730\u6587\u4ef6\u8bb0\u5f55\u4e2d\uff0c\u4e0a\u4f20\u8fc7\u7684\u7cfb\u7edf\u5e93so=" + uploadSuccessSystemSo.toString());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.w(LogUtils.TAG, "SystemSoHandler [addUploadSuccessSystemSo2File] Exception = " + e.toString());
        }
    }

    private void addUploadSuccessSystemSo2File(JSONArray jSONArray) throws Throwable {
        LogUtils.i(LogUtils.TAG, "SystemSoHandler [addUploadSuccessSystemSo2File] start");
        if (jSONArray == null || jSONArray.length() == 0) {
            LogUtils.i(LogUtils.TAG, "SystemSoHandler [addUploadSuccessSystemSo2File] param error");
            return;
        }
        JSONObject uploadSuccessSystemSo = getUploadSuccessSystemSo();
        for (int i = 0; i < jSONArray.length(); i++) {
            try {
                JSONObject jSONObjectOptJSONObject = jSONArray.optJSONObject(i);
                LogUtils.i(LogUtils.TAG, "SystemSoHandler [addUploadSuccessSystemSo2File] info=" + jSONObjectOptJSONObject.toString());
                if (jSONObjectOptJSONObject != null && jSONObjectOptJSONObject.has("name") && jSONObjectOptJSONObject.has("uuid")) {
                    String strOptString = jSONObjectOptJSONObject.optString("name");
                    String strOptString2 = jSONObjectOptJSONObject.optString("uuid");
                    String parentDirPath = getParentDirPath(strOptString2);
                    LogUtils.i(LogUtils.TAG, "SystemSoHandler [addUploadSuccessSystemSo2File] parentDir=" + parentDirPath + ", soName=" + strOptString);
                    if (!TextUtils.isEmpty(parentDirPath) && !TextUtils.isEmpty(strOptString)) {
                        if (!uploadSuccessSystemSo.has(parentDirPath + strOptString)) {
                            uploadSuccessSystemSo.put(parentDirPath + strOptString, strOptString2);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.w(LogUtils.TAG, "SystemSoHandler [addUploadSuccessSystemSo2File] Exception = " + e.toString());
                return;
            }
        }
        CUtil.str2File(uploadSuccessSystemSo.toString(), this.mCrashhunterSystemSoDir, UPLOAD_SUCCESS_SYSTEM_SO_FILE, true);
        LogUtils.i(LogUtils.TAG, "SystemSoHandler [addUploadSuccessSystemSo2File] \u6700\u65b0\u672c\u5730\u6587\u4ef6\u8bb0\u5f55\u4e2d\uff0c\u4e0a\u4f20\u8fc7\u7684\u7cfb\u7edf\u5e93so=" + uploadSuccessSystemSo.toString());
    }

    private JSONObject getUploadSuccessSystemSo() throws Throwable {
        JSONObject jSONObject;
        LogUtils.i(LogUtils.TAG, "SystemSoHandler [getUploadSuccessSystemSo] start");
        try {
            String strFile2Str = CUtil.file2Str(this.mCrashhunterSystemSoDir, UPLOAD_SUCCESS_SYSTEM_SO_FILE);
            if (!TextUtils.isEmpty(strFile2Str)) {
                jSONObject = new JSONObject(strFile2Str);
            } else {
                jSONObject = new JSONObject();
            }
            return jSONObject;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.i(LogUtils.TAG, "SystemSoHandler [getUploadSuccessSystemSo] Exception = " + e.toString());
            return null;
        }
    }

    private List<Unit> getUnUploadSystemSo(JSONArray jSONArray) throws Throwable {
        Unit unitCreateUnit;
        Unit unitCreateUnit2;
        LogUtils.i(LogUtils.TAG, "SystemSoHandler [getUnUploadSystemSo] start");
        ArrayList arrayList = new ArrayList();
        JSONObject uploadSuccessSystemSo = getUploadSuccessSystemSo();
        LogUtils.i(LogUtils.TAG, "SystemSoHandler [getUnUploadSystemSo] \u672c\u5730\u6587\u4ef6\u8bb0\u5f55\u4e2d\uff0c\u4e0a\u4f20\u8fc7\u7684\u7cfb\u7edf\u5e93so =" + uploadSuccessSystemSo.toString());
        if (jSONArray == null || jSONArray.length() <= 0 || uploadSuccessSystemSo == null) {
            LogUtils.i(LogUtils.TAG, "SystemSoHandler [getUnUploadSystemSo] params error");
            return arrayList;
        }
        for (int i = 0; i < jSONArray.length(); i++) {
            String strOptString = jSONArray.optString(i);
            if (!TextUtils.isEmpty(strOptString)) {
                if (!uploadSuccessSystemSo.has(SYSTEM_SO_PATH_32 + strOptString) && (unitCreateUnit2 = createUnit(SYSTEM_SO_PATH_32, strOptString)) != null) {
                    arrayList.add(unitCreateUnit2);
                }
                if (!uploadSuccessSystemSo.has(SYSTEM_SO_PATH_64 + strOptString) && (unitCreateUnit = createUnit(SYSTEM_SO_PATH_64, strOptString)) != null) {
                    arrayList.add(unitCreateUnit);
                }
            }
        }
        LogUtils.i(LogUtils.TAG, "SystemSoHandler [getUnUploadSystemSo] unUploadSystemSoList=" + arrayList.toString());
        return arrayList;
    }

    private Unit createUnit(String str, String str2) {
        String strCalculateFileHash;
        long j;
        LogUtils.i(LogUtils.TAG, "SystemSoHandler [createUnit] start");
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            String soBuildId = NTCrashHunterKit.sharedKit().getSoBuildId(str + str2);
            File file = new File(str + str2);
            if (file.exists()) {
                long length = file.length();
                strCalculateFileHash = HashUtil.calculateFileHash("MD5", file.getAbsolutePath());
                j = length;
            } else {
                strCalculateFileHash = null;
                j = 0;
            }
            if (0 != j && !TextUtils.isEmpty(strCalculateFileHash)) {
                return new Unit(str, str2, soBuildId, j, strCalculateFileHash);
            }
        }
        return null;
    }

    private boolean reuqestUploadSystemSo() throws Throwable {
        if (this.mUnUploadSystemSoList == null) {
            LogUtils.i(LogUtils.TAG, "SystemSoHandler [reuqestUploadSystemSo] start");
            JSONArray jSONArray = ConfigCore.getInstance().getmSystemSoArray();
            if (jSONArray == null || jSONArray.length() <= 0) {
                LogUtils.i(LogUtils.TAG, "SystemSoHandler [reuqestUploadSystemSo] \u672c\u6b21\u65e0\u9700\u4e0a\u4f20\u7cfb\u7edf\u5e93so");
                return false;
            }
            LogUtils.i(LogUtils.TAG, "SystemSoHandler [reuqestUploadSystemSo] \u914d\u7f6e\u6587\u4ef6\u5185\u5bb9\u4e2d\uff0c\u9700\u8981\u4e0a\u4f20\u7684\u7cfb\u7edf\u5e93so=" + jSONArray.toString());
            List<Unit> unUploadSystemSo = getUnUploadSystemSo(jSONArray);
            this.mUnUploadSystemSoList = unUploadSystemSo;
            if (unUploadSystemSo == null || unUploadSystemSo.size() <= 0) {
                LogUtils.i(LogUtils.TAG, "SystemSoHandler [reuqestUploadSystemSo] \u914d\u7f6e\u6587\u4ef6\u8981\u6c42\u4e0a\u4f20\u7684\u7cfb\u7edf\u5e93so, \u672c\u673a\u5df2\u4e0a\u4f20\u8fc7\uff0c\u65e0\u6cd5\u518d\u6b21\u4e0a\u4f20");
                return false;
            }
            LogUtils.i(LogUtils.TAG, "SystemSoHandler [reuqestUploadSystemSo] \u7ecf\u8fc7\u672c\u5730\u5bf9\u6bd4\u540e\uff0c\u9700\u8981\u4e0a\u4f20\u7684so=" + this.mUnUploadSystemSoList.toString());
            return true;
        }
        LogUtils.i(LogUtils.TAG, "SystemSoHandler [reuqestUploadSystemSo] \u7ecf\u8fc7\u672c\u5730\u5bf9\u6bd4\u540e\uff0c\u9700\u8981\u4e0a\u4f20\u7684so=" + this.mUnUploadSystemSoList.toString());
        return true;
    }

    public void uploadSystemSo(Context context) throws JSONException, IOException {
        if (this.mAllSystemSoUploadSucess || this.mUploading) {
            return;
        }
        LogUtils.i(LogUtils.TAG, "SystemSoHandler [uploadSystemSo] start");
        if (!WifiUtil.isConnectNet() || !WifiUtil.isConnectedWifi()) {
            LogUtils.i(LogUtils.TAG, "SystemSoHandler [uploadSystemSo] \u7f51\u7edc\u672a\u8fde\u63a5 \u6216 \u975e\u5904\u4e8ewifi\u73af\u5883\uff0c\u65e0\u9700\u4e0a\u4f20");
            return;
        }
        this.mUploading = true;
        if (context == null) {
            LogUtils.i(LogUtils.TAG, "SystemSoHandler [uploadSystemSo] param error");
            this.mUploading = false;
            return;
        }
        init(context);
        if (!reuqestUploadSystemSo()) {
            this.mAllSystemSoUploadSucess = true;
            LogUtils.i(LogUtils.TAG, "SystemSoHandler [uploadSystemSo] \u6ca1\u6709\u9700\u8981\u4e0a\u4f20\u7684\u7cfb\u7edfso");
            this.mUploading = false;
            return;
        }
        requestToken(context);
    }

    private Unit nextUnUploadSystemSo() {
        Unit unit = this.mIndex < this.mUnUploadSystemSoList.size() ? this.mUnUploadSystemSoList.get(this.mIndex) : null;
        this.mIndex++;
        return unit;
    }

    private void requestToken(final Context context) throws JSONException {
        Exception e;
        JSONArray jSONArray;
        LogUtils.i(LogUtils.TAG, "SystemSoHandler [requestToken] start");
        List<Unit> list = this.mUnUploadSystemSoList;
        JSONArray jSONArray2 = null;
        if (list != null && list.size() > 0) {
            LogUtils.i(LogUtils.TAG, "SystemSoHandler [requestToken] mUnUploadSystemSoList=" + this.mUnUploadSystemSoList.toString());
            Unit unitNextUnUploadSystemSo = nextUnUploadSystemSo();
            if (unitNextUnUploadSystemSo != null) {
                LogUtils.i(LogUtils.TAG, "SystemSoHandler [requestToken] unit=" + unitNextUnUploadSystemSo.toString());
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.put("name", unitNextUnUploadSystemSo.mSoName);
                    jSONObject.put("uuid", unitNextUnUploadSystemSo.mSoUuid);
                    jSONObject.put("size", unitNextUnUploadSystemSo.mSoSize);
                    jSONObject.put(Const.KEY_MD5, unitNextUnUploadSystemSo.mSoMd5);
                    jSONArray = new JSONArray();
                    try {
                        jSONArray.put(jSONObject);
                    } catch (Exception e2) {
                        e = e2;
                        LogUtils.i(LogUtils.TAG, "SystemSoHandler [requestToken] Exception=" + e.toString());
                        jSONArray2 = jSONArray;
                        if (jSONArray2 == null) {
                        }
                        LogUtils.i(LogUtils.TAG, "SystemSoHandler [requestToken] \u6ca1\u6709\u672a\u4e0a\u4f20\u7684\u7cfb\u7edf\u5e93so");
                    }
                } catch (Exception e3) {
                    e = e3;
                    jSONArray = null;
                }
                jSONArray2 = jSONArray;
            }
        }
        if (jSONArray2 == null && jSONArray2.length() > 0) {
            LogUtils.i(LogUtils.TAG, "SystemSoHandler [requestToken] \u5ba2\u6237\u7aef\u5c06\u8981\u83b7\u53d6token\u7684so\u5217\u8868=" + jSONArray2.toString());
            SystemSoTokenRequest systemSoTokenRequest = new SystemSoTokenRequest(jSONArray2);
            systemSoTokenRequest.registerRequestCallback(new RequestCallback() { // from class: com.netease.androidcrashhandler.so.SystemSoHandler.1
                @Override // com.netease.androidcrashhandler.net.RequestCallback
                public void onResponse(int i, String str) throws Throwable {
                    LogUtils.i(LogUtils.TAG, "SystemSoHandler [requestToken] \u83b7\u53d6token\u7ed3\u679c = " + i + ", info=" + str);
                    UpLoadUnit reponse = SystemSoHandler.this.parseReponse(i, str);
                    if (reponse == null) {
                        SystemSoHandler.this.mUploading = false;
                        return;
                    }
                    if (4 == reponse.mCode) {
                        LogUtils.i(LogUtils.TAG, "SystemSoHandler [requestToken] \u6267\u884c\u4e0a\u4f20");
                        SystemSoHandler.this.upload(reponse);
                    } else if (2 == reponse.mCode || 3 == reponse.mCode) {
                        LogUtils.i(LogUtils.TAG, "SystemSoHandler [requestToken] \u5904\u7406\u4e0b\u4e00\u4e2aso");
                        SystemSoHandler.this.uploadSystemSo(context);
                        SystemSoHandler.this.mUploading = false;
                    } else {
                        LogUtils.i(LogUtils.TAG, "SystemSoHandler [requestToken] \u8be5so\u5df2\u4e0a\u4f20");
                        SystemSoHandler.this.mUploading = false;
                    }
                }
            });
            TaskProxy.getInstances().put(systemSoTokenRequest);
            return;
        }
        LogUtils.i(LogUtils.TAG, "SystemSoHandler [requestToken] \u6ca1\u6709\u672a\u4e0a\u4f20\u7684\u7cfb\u7edf\u5e93so");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public UpLoadUnit parseReponse(int i, String str) throws Throwable {
        JSONArray jSONArrayOptJSONArray;
        LogUtils.i(LogUtils.TAG, "SystemSoHandler [parseReponse] start");
        if (TextUtils.isEmpty(str)) {
            LogUtils.i(LogUtils.TAG, "SystemSoHandler [parseReponse] params is null");
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.has("data")) {
                JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("data");
                if (jSONObjectOptJSONObject == null) {
                    LogUtils.i(LogUtils.TAG, "SystemSoHandler [parseReponse] dataJson is null");
                    return null;
                }
                if (jSONObjectOptJSONObject.has("infos") && (jSONArrayOptJSONArray = jSONObjectOptJSONObject.optJSONArray("infos")) != null && jSONArrayOptJSONArray.length() > 0) {
                    JSONObject jSONObjectOptJSONObject2 = jSONArrayOptJSONArray.optJSONObject(0);
                    JSONArray jSONArrayOptJSONArray2 = jSONObjectOptJSONObject2.has("so_list") ? jSONObjectOptJSONObject2.optJSONArray("so_list") : null;
                    if (jSONArrayOptJSONArray2 != null && jSONArrayOptJSONArray2.length() > 0) {
                        int iOptInt = jSONObjectOptJSONObject2.has("state") ? jSONObjectOptJSONObject2.optInt("state") : -1;
                        LogUtils.i(LogUtils.TAG, "SystemSoHandler [parseReponse] state = " + iOptInt);
                        if (1 == iOptInt) {
                            LogUtils.i(LogUtils.TAG, "SystemSoHandler [parseReponse] \u8be5so\u5df2\u4e0a\u4f20, soListArray=" + jSONArrayOptJSONArray2.toString());
                            addUploadSuccessSystemSo2File(jSONArrayOptJSONArray2);
                            return new UpLoadUnit(iOptInt);
                        }
                        if (2 == iOptInt) {
                            LogUtils.i(LogUtils.TAG, "SystemSoHandler [parseReponse] \u83b7\u53d6token\u5f02\u5e38\uff0c\u9700\u91cd\u8bd5, soListArray=" + jSONArrayOptJSONArray2.toString());
                            return new UpLoadUnit(iOptInt);
                        }
                        if (3 == iOptInt) {
                            LogUtils.i(LogUtils.TAG, "SystemSoHandler [parseReponse] \u5176\u4ed6\u5ba2\u6237\u7aef\u4e0a\u4f20\u4e2d\uff0c\u9700\u91cd\u8bd5\uff0csoListArray=" + jSONArrayOptJSONArray2.toString());
                            return new UpLoadUnit(iOptInt);
                        }
                        if (4 == iOptInt) {
                            LogUtils.i(LogUtils.TAG, "SystemSoHandler [parseReponse] \u7533\u8bf7\u6210\u529f\uff0c\u4e0a\u4f20, soListArray=" + jSONArrayOptJSONArray2.toString());
                            if (jSONObjectOptJSONObject2.has("token") && jSONObjectOptJSONObject2.has("expire_in")) {
                                String strOptString = jSONObjectOptJSONObject2.optString("token");
                                long jOptLong = jSONObjectOptJSONObject2.optLong("expire_in");
                                if (!TextUtils.isEmpty(strOptString) && 0 <= jOptLong) {
                                    return new UpLoadUnit(iOptInt, strOptString, System.currentTimeMillis(), jOptLong, jSONArrayOptJSONArray2);
                                }
                            }
                        }
                    }
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.i(LogUtils.TAG, "SystemSoHandler [parseReponse] Exception = " + e.toString());
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void upload(UpLoadUnit upLoadUnit) {
        LogUtils.i(LogUtils.TAG, "SystemSoHandler [upload] start");
        if (upLoadUnit == null) {
            LogUtils.i(LogUtils.TAG, "SystemSoHandler [upload] upLoadUnit is null");
            return;
        }
        LogUtils.i(LogUtils.TAG, "SystemSoHandler [upload] start upLoadUnit=" + upLoadUnit.toString());
        UploadSystemSoRequest uploadSystemSoRequest = new UploadSystemSoRequest(upLoadUnit, this.mUnUploadSystemSoList, this.mCrashhunterSystemSoDir);
        uploadSystemSoRequest.registerRequestCallback(new RequestCallback() { // from class: com.netease.androidcrashhandler.so.SystemSoHandler.2
            @Override // com.netease.androidcrashhandler.net.RequestCallback
            public void onResponse(int i, String str) throws Throwable {
                LogUtils.i(LogUtils.TAG, "SystemSoHandler [upload1] \u4e0a\u4f20\u7ed3\u679c = " + i + ", info=" + str);
                UpLoadUnit reponse = SystemSoHandler.this.parseReponse(i, str);
                if (reponse == null) {
                    SystemSoHandler.this.mUploading = false;
                    return;
                }
                if (2 == reponse.mCode || 3 == reponse.mCode) {
                    LogUtils.i(LogUtils.TAG, "SystemSoHandler [upload1] \u5904\u7406\u4e0b\u4e00\u4e2aso");
                    SystemSoHandler.this.mUploading = false;
                } else {
                    LogUtils.i(LogUtils.TAG, "SystemSoHandler [upload1] \u8be5so\u5df2\u4e0a\u4f20");
                    SystemSoHandler.this.mUploading = false;
                }
            }
        });
        TaskProxy.getInstances().put(uploadSystemSoRequest);
    }

    private String getParentDirPath(String str) {
        LogUtils.i(LogUtils.TAG, "SystemSoHandler [getParentDirPath] start");
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        for (int i = 0; i < this.mUnUploadSystemSoList.size(); i++) {
            Unit unit = this.mUnUploadSystemSoList.get(i);
            if (unit != null && str.equalsIgnoreCase(unit.mSoUuid)) {
                return unit.mParentDir;
            }
        }
        return null;
    }
}