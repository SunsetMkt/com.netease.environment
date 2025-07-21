package com.netease.androidcrashhandler.entity.param;

import android.text.TextUtils;
import com.netease.androidcrashhandler.init.InitProxy;
import com.netease.androidcrashhandler.thirdparty.unilogger.CUniLogger;
import com.netease.androidcrashhandler.util.CUtil;
import com.netease.androidcrashhandler.util.LogUtils;
import com.netease.ntunisdk.unilogger.global.Const;
import java.io.File;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class ParamsInfo {
    private JSONObject mParamsJson = new JSONObject();
    private String mFileName = "crashhunter.param";
    private boolean mAutoStorage = false;
    private JSONObject mConditionsJson = new JSONObject();

    public synchronized JSONObject getmParamsJson() {
        JSONObject jSONObject;
        try {
            jSONObject = new JSONObject(this.mParamsJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
            jSONObject = null;
        }
        return jSONObject;
    }

    public void setmParamsJson(JSONObject jSONObject) {
        this.mParamsJson = jSONObject;
    }

    public synchronized void putParam(String str, String str2) {
        String string;
        boolean z;
        LogUtils.i(LogUtils.TAG, "ParamsInfo [putParam] key=" + str + ", value=" + str2);
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            if (str.equals("udid")) {
                CUniLogger.setUdid(str2);
            }
            if (str.equals(Const.CONFIG_KEY.ROLEID)) {
                CUniLogger.setRoleId(str2);
            }
            if (str.equals("gameid")) {
                CUniLogger.setGameid(str2);
            }
            if (str.equals("uid")) {
                CUniLogger.setUid(str2);
            }
            try {
                boolean z2 = true;
                if (this.mParamsJson.has(str)) {
                    string = this.mParamsJson.getString(str);
                    z = false;
                } else {
                    string = null;
                    z = true;
                }
                if (z || TextUtils.isEmpty(string) || string.equals(str2)) {
                    z2 = z;
                }
                if (z2) {
                    this.mParamsJson.put(str, str2);
                    if (this.mAutoStorage) {
                        LogUtils.i(LogUtils.TAG, "ParamsInfo [putParam] mParamsJson=" + this.mParamsJson.toString());
                        CUtil.str2File(this.mParamsJson.toString(), InitProxy.sUploadFilePath, this.mFileName);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x00cf A[Catch: all -> 0x00ec, TRY_LEAVE, TryCatch #1 {, blocks: (B:4:0x0003, B:6:0x0023, B:8:0x0029, B:10:0x0031, B:13:0x0039, B:15:0x0042, B:38:0x00cf, B:17:0x0052, B:19:0x005a, B:22:0x0067, B:24:0x006d, B:30:0x007f, B:31:0x0088, B:27:0x0079, B:20:0x0061, B:34:0x0091, B:36:0x00b0, B:41:0x00e3), top: B:47:0x0003, inners: #3 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized boolean addTag(java.lang.String r6, java.lang.String r7) {
        /*
            Method dump skipped, instructions count: 239
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.androidcrashhandler.entity.param.ParamsInfo.addTag(java.lang.String, java.lang.String):boolean");
    }

    public synchronized boolean removeTag(String str, String str2) {
        LogUtils.i(LogUtils.TAG, "ParamsInfo [removeTag] key=" + str + ", value=" + str2);
        boolean z = false;
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            LogUtils.i(LogUtils.TAG, "ParamsInfo [removeTag] param error");
            return false;
        }
        try {
            try {
                if (this.mConditionsJson.has(str)) {
                    if ("branch".equals(str)) {
                        this.mConditionsJson.remove(str);
                    } else {
                        JSONArray jSONArray = this.mConditionsJson.getJSONArray(str);
                        JSONArray jSONArray2 = new JSONArray();
                        for (int i = 0; i < jSONArray.length(); i++) {
                            if (!str2.equals(jSONArray.get(i))) {
                                jSONArray2.put(jSONArray.get(i));
                            }
                        }
                        if (jSONArray2.length() != jSONArray.length()) {
                            if (jSONArray.length() == 1) {
                                this.mConditionsJson.remove(str);
                            } else {
                                this.mConditionsJson.put(str, jSONArray2);
                            }
                        } else {
                            LogUtils.i(LogUtils.TAG, "ParamsInfo [removeTag] \u4e0d\u5305\u542b\u8be5value");
                        }
                    }
                    z = true;
                } else {
                    LogUtils.i(LogUtils.TAG, "ParamsInfo [removeTag] \u4e0d\u5305\u542b\u8be5key");
                }
            } catch (Exception e) {
                LogUtils.w(LogUtils.TAG, "ParamsInfo [removeTag] Exception=" + e.toString());
                e.printStackTrace();
            }
        } catch (JSONException e2) {
            LogUtils.w(LogUtils.TAG, "ParamsInfo [removeTag] JSONException=" + e2.toString());
            e2.printStackTrace();
        }
        if (z) {
            LogUtils.i(LogUtils.TAG, "ParamsInfo [removeTag] save to file");
            putParam("conditions", this.mConditionsJson.toString());
        }
        return z;
    }

    public void writeToLocalFile() {
        LogUtils.i(LogUtils.TAG, "ParamsInfo [freshToLocalFile] start");
        try {
            File file = new File(InitProxy.sUploadFilePath, this.mFileName);
            if (!file.exists()) {
                LogUtils.i(LogUtils.TAG, "ParamsInfo [freshToLocalFile] create new file = " + file.getAbsolutePath());
                file.createNewFile();
            }
            this.mAutoStorage = true;
            LogUtils.i(LogUtils.TAG, "ParamsInfo [freshToLocalFile] mParamsJson=" + this.mParamsJson.toString());
            CUtil.str2File(this.mParamsJson.toString(), InitProxy.sUploadFilePath, this.mFileName);
        } catch (IOException e) {
            LogUtils.i(LogUtils.TAG, "ParamsInfo [freshToLocalFile] IOException=" + e.toString());
            e.printStackTrace();
        } catch (Exception e2) {
            LogUtils.i(LogUtils.TAG, "ParamsInfo [freshToLocalFile] Exception=" + e2.toString());
            e2.printStackTrace();
        }
    }

    public synchronized void getParamFromLoaclFile() {
        LogUtils.i(LogUtils.TAG, "ParamsInfo [getParamFromLoaclFile] start sUploadFilePath=" + InitProxy.sUploadFilePath);
        String strFile2Str = CUtil.file2Str(InitProxy.sUploadFilePath, this.mFileName);
        if (TextUtils.isEmpty(strFile2Str)) {
            LogUtils.i(LogUtils.TAG, "ParamsInfo [getParamFromLoaclFile] params error");
            return;
        }
        try {
            this.mParamsJson = new JSONObject(strFile2Str);
        } catch (JSONException e) {
            LogUtils.i(LogUtils.TAG, "ParamsInfo [getParamFromLoaclFile] JSONException=" + e.toString());
            e.printStackTrace();
        }
        LogUtils.i(LogUtils.TAG, "ParamsInfo [getParamFromLoaclFile] mParamsJson=" + this.mParamsJson.toString());
    }

    public void deleteParamFile() {
        LogUtils.i(LogUtils.TAG, "ParamsInfo [deleteParamFile] start");
        File file = new File(InitProxy.sUploadFilePath, this.mFileName);
        if (file.exists()) {
            file.delete();
        }
    }

    public String toString() {
        return this.mParamsJson.toString();
    }
}