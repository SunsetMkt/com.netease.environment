package com.netease.pharos.qos;

import android.os.Looper;
import com.netease.pharos.PharosProxy;
import com.netease.pharos.util.LogUtil;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes4.dex */
public class Qos4GProxy {
    private static final String TAG = "Qos4GProxy";
    private static Qos4GProxy sQos4GProxy;

    private Qos4GProxy() {
    }

    public static Qos4GProxy getInstance() {
        if (sQos4GProxy == null) {
            sQos4GProxy = new Qos4GProxy();
        }
        return sQos4GProxy;
    }

    public void pharosqosexec(final JSONArray jSONArray, final long j) {
        LogUtil.i(TAG, "Qos4GProxy [pharosqosexec]");
        if (jSONArray == null || jSONArray.length() < 0 || j <= 0) {
            LogUtil.i(TAG, "PharosProxy [pharosqosexec] \u53c2\u6570\u9519\u8bef");
            return;
        }
        LogUtil.i(TAG, "Qos4GProxy [pharosqosexec] ips=" + jSONArray + ", duration=" + j);
        if (Looper.myLooper() == Looper.getMainLooper()) {
            new Thread(new Runnable() { // from class: com.netease.pharos.qos.Qos4GProxy.1
                @Override // java.lang.Runnable
                public void run() throws JSONException {
                    Qos4GProxy.this.pharosqosexec2(jSONArray, j);
                }
            }).start();
        } else {
            pharosqosexec2(jSONArray, j);
        }
    }

    public void pharosqosexec2(JSONArray jSONArray, long j) throws JSONException {
        QosStatus.getInstance().getAllIp();
        if (jSONArray != null && jSONArray.length() > 0) {
            LogUtil.i(TAG, "Qos4GProxy [pharosqosexec2] ips=" + jSONArray);
            new Qos().pharosqosexec(jSONArray, j);
            return;
        }
        LogUtil.i(TAG, "Qos4GProxy [pharosqosexec2] \u8be5ip\u5df2\u5728\u52a0\u901f\u4e2d");
    }

    public void cancel(final JSONArray jSONArray) {
        if (jSONArray == null || jSONArray.length() <= 0) {
            return;
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            new Thread(new Runnable() { // from class: com.netease.pharos.qos.Qos4GProxy.2
                @Override // java.lang.Runnable
                public void run() throws JSONException {
                    Qos4GProxy.this.cancel2(jSONArray);
                }
            }).start();
        } else {
            cancel2(jSONArray);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancel2(JSONArray jSONArray) throws JSONException {
        if (jSONArray == null || jSONArray.length() < 0) {
            LogUtil.i(TAG, "Qos4GProxy [cancel2] param error");
            return;
        }
        ArrayList<String> allIp = QosStatus.getInstance().getAllIp();
        if (jSONArray.length() > 0) {
            new Qos().pharosqoscancel(jSONArray);
            PharosProxy.getInstance().pharosqosstatus();
            LogUtil.i(TAG, "Qos4GProxy [cancel] \u53d6\u6d88\u540e ipList=" + allIp.toString() + ", ips=" + jSONArray);
            return;
        }
        LogUtil.i(TAG, "Qos4GProxy [cancel] \u5c06\u8981\u53d6\u6d88\u7684ip\u4e0d\u5728\u52a0\u901f\u5217\u8868\u4e2d ipList=" + allIp.toString() + ", ips=" + jSONArray);
    }

    public JSONObject getResult() {
        JSONObject jSONObject = new JSONObject();
        LogUtil.i(TAG, "Qos4GProxy [getResult] \u603b\u7ed3\u679c=" + QosStatus.getInstance().getResult());
        LogUtil.i(TAG, "Qos4GProxy [getResult] QosProxy.getInstance().getQosResult()=" + QosProxy.getInstance().getQosResult());
        JSONObject qosResult = QosProxy.getInstance().getQosResult();
        LogUtil.i(TAG, "Qos4GProxy [getResult] qosResult=" + qosResult);
        if (qosResult != null && qosResult.has("qos")) {
            try {
                jSONObject = qosResult.getJSONObject("qos");
            } catch (JSONException e) {
                LogUtil.i(TAG, "Qos4GProxy [getResult] JSONException1=" + e);
            }
        }
        new JSONObject();
        try {
            jSONObject.put("qos_status", QosStatus.getInstance().getResult());
        } catch (JSONException e2) {
            LogUtil.i(TAG, "Qos4GProxy [getResult] JSONException2=" + e2);
        }
        LogUtil.i(TAG, "Qos4GProxy [getResult] \u8fc7\u6ee4ip\uff0c\u83b7\u53d6\u7684\u7ed3\u679c=" + jSONObject);
        return jSONObject;
    }

    public JSONObject getResult(JSONArray jSONArray) {
        LogUtil.i(TAG, "Qos4GProxy [getResult]");
        JSONObject jSONObject = new JSONObject();
        if (jSONArray != null) {
            try {
                if (jSONArray.length() >= 0) {
                    LogUtil.i(TAG, "Qos4GProxy [getResult] ips=" + jSONArray);
                    JSONObject result = QosStatus.getInstance().getResult();
                    LogUtil.i(TAG, "Qos4GProxy [getResult] \u603b\u7ed3\u679c=" + result);
                    LogUtil.i(TAG, "Qos4GProxy [getResult] QosProxy.getInstance().getQosResult()=" + QosProxy.getInstance().getQosResult());
                    JSONObject jSONObject2 = new JSONObject();
                    for (int i = 0; i < jSONArray.length(); i++) {
                        String strOptString = jSONArray.optString(i);
                        if (result.has(strOptString)) {
                            jSONObject2.put(strOptString, result.optJSONObject(strOptString));
                        }
                    }
                    if (jSONObject2.length() > 0) {
                        JSONObject qosResult = QosProxy.getInstance().getQosResult();
                        LogUtil.i(TAG, "Qos4GProxy [getResult] qosResult=" + qosResult);
                        LogUtil.i(TAG, "Qos4GProxy [getResult] newQosStatusResult=" + jSONObject2);
                        if (qosResult != null && qosResult.has("qos")) {
                            try {
                                jSONObject = qosResult.getJSONObject("qos");
                            } catch (JSONException e) {
                                LogUtil.i(TAG, "Qos4GProxy [getResult] JSONException1=" + e);
                            }
                        }
                        new JSONObject();
                        try {
                            jSONObject.put("qos_status", jSONObject2);
                        } catch (JSONException e2) {
                            LogUtil.i(TAG, "Qos4GProxy [getResult] JSONException2=" + e2);
                        }
                    } else {
                        LogUtil.i(TAG, "Qos4GProxy [getResult] \u603b\u7ed3\u679c\u4e2d\u4e0d\u5305\u542b\u8be5ips\uff0cips=" + jSONArray);
                    }
                    LogUtil.i(TAG, "Qos4GProxy [getResult] \u8fc7\u6ee4ip\uff0c\u83b7\u53d6\u7684\u7ed3\u679c=" + jSONObject + ", ips=" + jSONArray);
                }
            } catch (Exception e3) {
                LogUtil.i(TAG, "Qos4GProxy [getResult] Exception=" + e3);
            }
        }
        return jSONObject;
    }
}