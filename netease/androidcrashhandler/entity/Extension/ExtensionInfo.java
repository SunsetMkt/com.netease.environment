package com.netease.androidcrashhandler.entity.Extension;

import android.text.TextUtils;
import com.netease.androidcrashhandler.entity.di.DiProxy;
import com.netease.androidcrashhandler.util.LogUtils;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes6.dex */
public class ExtensionInfo {
    public static final String TAG = "Info";
    private static ExtensionInfo mInfo;
    private JSONArray mObfuFileNameArray = new JSONArray();
    private JSONObject mExtensionInfos = new JSONObject();

    private ExtensionInfo() {
    }

    public static ExtensionInfo getInstance() {
        if (mInfo == null) {
            mInfo = new ExtensionInfo();
        }
        return mInfo;
    }

    public void addObfuFileName(String str) {
        LogUtils.i(LogUtils.TAG, "ExtensionInfo [addObfuFileName] start");
        if (TextUtils.isEmpty(str) || this.mObfuFileNameArray == null) {
            LogUtils.i(LogUtils.TAG, "ExtensionInfo [addObfuFileName] param error");
            return;
        }
        LogUtils.i(LogUtils.TAG, "ExtensionInfo [addObfuFileName] fileName=" + str);
        if (str.contains(",")) {
            for (String str2 : str.split(",")) {
                this.mObfuFileNameArray.put(str2);
            }
        } else {
            this.mObfuFileNameArray.put(str);
        }
        DiProxy.getInstance().updateDiInfoToLocalFile();
    }

    public void addExtensionInfo(JSONObject jSONObject) {
        LogUtils.i(LogUtils.TAG, "ExtensionInfo [addExtensionInfo] start");
        if (jSONObject == null || this.mExtensionInfos == null) {
            LogUtils.i(LogUtils.TAG, "ExtensionInfo [addExtensionInfo] param error");
            return;
        }
        LogUtils.i(LogUtils.TAG, "ExtensionInfo [addExtensionInfo] infos=" + jSONObject.toString());
        try {
            Iterator<String> itKeys = jSONObject.keys();
            while (itKeys.hasNext()) {
                String next = itKeys.next();
                this.mExtensionInfos.put(next, jSONObject.get(next));
            }
        } catch (Exception e) {
            LogUtils.i(LogUtils.TAG, "ExtensionInfo [start] Exception=" + e.toString());
            e.printStackTrace();
        }
        DiProxy.getInstance().updateDiInfoToLocalFile();
    }

    public JSONObject getResult() {
        LogUtils.i(LogUtils.TAG, "ExtensionInfo [getResult] start");
        JSONObject jSONObject = new JSONObject();
        try {
            LogUtils.i(LogUtils.TAG, "ExtensionInfo [getResult] mObfuFileNameArray length=" + this.mObfuFileNameArray.length());
            JSONArray jSONArray = this.mObfuFileNameArray;
            if (jSONArray != null && jSONArray.length() > 0) {
                jSONObject.put("obfu", this.mObfuFileNameArray);
            }
            LogUtils.i(LogUtils.TAG, "ExtensionInfo [getResult] mExtensionInfos length=" + this.mExtensionInfos.length());
            JSONObject jSONObject2 = this.mExtensionInfos;
            if (jSONObject2 != null && jSONObject2.length() > 0) {
                Iterator<String> itKeys = this.mExtensionInfos.keys();
                while (itKeys.hasNext()) {
                    String next = itKeys.next();
                    jSONObject.put(next, this.mExtensionInfos.get(next));
                }
            }
        } catch (Exception e) {
            LogUtils.i(LogUtils.TAG, "ExtensionInfo [getResult] Exception=" + e.toString());
            e.printStackTrace();
        }
        return jSONObject;
    }

    public void clean() {
        LogUtils.i(LogUtils.TAG, "ExtensionInfo [clean] start");
        this.mObfuFileNameArray = null;
        this.mObfuFileNameArray = new JSONArray();
        this.mExtensionInfos = null;
        this.mExtensionInfos = new JSONObject();
        DiProxy.getInstance().updateDiInfoToLocalFile();
    }
}