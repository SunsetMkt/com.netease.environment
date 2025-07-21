package com.netease.pharos.qos;

import android.text.TextUtils;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import com.netease.ntunisdk.unilogger.global.Const;
import com.netease.pharos.util.LogUtil;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes4.dex */
public class QosStatus {
    private static final String TAG = "QosStatus";
    private static QosStatus sQosStatus;
    private JSONObject mResult = new JSONObject();

    private QosStatus() {
    }

    public static QosStatus getInstance() {
        if (sQosStatus == null) {
            sQosStatus = new QosStatus();
        }
        return sQosStatus;
    }

    public JSONObject getResult() {
        return this.mResult;
    }

    public JSONObject getResult(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            return this.mResult.getJSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean has(String str) {
        if (TextUtils.isEmpty(str)) {
            LogUtil.w(TAG, "QosStatus [has] param error");
            return false;
        }
        return this.mResult.has(str);
    }

    public boolean has(JSONArray jSONArray) {
        if (jSONArray == null || jSONArray.length() <= 0) {
            LogUtil.w(TAG, "QosStatus [has] param error");
            return true;
        }
        if (this.mResult.length() <= 0) {
            LogUtil.w(TAG, "QosStatus [has] mResult is null");
            return false;
        }
        for (int i = 0; i < jSONArray.length(); i++) {
            if (!this.mResult.has(jSONArray.optString(i))) {
                return false;
            }
        }
        return true;
    }

    public void setIp(String str) throws JSONException {
        if (TextUtils.isEmpty(str)) {
            LogUtil.w(TAG, "QosStatus [setIp] param error");
            return;
        }
        LogUtil.w(TAG, "QosStatus [setIp] mResult1=" + this.mResult + ", ip=" + str);
        try {
            this.mResult.put(str, new JSONObject());
            setId(str, "");
            setExpire(str, "0");
            setStatus(str, -11);
            setValidity(str, 0L);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.w(TAG, "QosStatus [setIp] mResult2=" + this.mResult);
    }

    public long getValidity(String str) throws JSONException {
        if (TextUtils.isEmpty(str)) {
            LogUtil.w(TAG, "QosStatus [getValidity] param error");
            return -1L;
        }
        if (!this.mResult.has(str)) {
            LogUtil.w(TAG, "QosStatus [getValidity] mResult \u4e0d\u5305\u542b " + str);
            return -1L;
        }
        try {
            JSONObject jSONObject = this.mResult.getJSONObject(str);
            if (jSONObject == null || !jSONObject.has("validity")) {
                return -1L;
            }
            return jSONObject.getLong("validity");
        } catch (JSONException e) {
            LogUtil.w(TAG, "QosStatus [getValidity] JSONException=" + e);
            return -1L;
        }
    }

    public void setValidity(String str, long j) throws JSONException {
        if (TextUtils.isEmpty(str)) {
            LogUtil.w(TAG, "QosStatus [setValidity] param error");
            return;
        }
        if (!this.mResult.has(str)) {
            LogUtil.w(TAG, "QosStatus [setValidity] mResult \u4e0d\u5305\u542b " + str);
            return;
        }
        try {
            JSONObject jSONObject = this.mResult.getJSONObject(str);
            if (jSONObject != null) {
                jSONObject.put("validity", j);
            }
        } catch (JSONException e) {
            LogUtil.w(TAG, "QosStatus [setValidity] JSONException=" + e);
        }
    }

    public int getStatus(String str) throws JSONException {
        if (TextUtils.isEmpty(str)) {
            LogUtil.w(TAG, "QosStatus [getStatus] param error");
            return -100;
        }
        if (!this.mResult.has(str)) {
            LogUtil.w(TAG, "QosStatus [getStatus] mResult \u4e0d\u5305\u542b " + str);
            return -100;
        }
        try {
            JSONObject jSONObject = this.mResult.getJSONObject(str);
            if (jSONObject == null || !jSONObject.has("status")) {
                return -100;
            }
            return jSONObject.getInt("status");
        } catch (JSONException e) {
            LogUtil.w(TAG, "QosStatus [getStatus] JSONException=" + e);
            return -100;
        }
    }

    public void setStatus(String str, int i) throws JSONException {
        if (TextUtils.isEmpty(str)) {
            LogUtil.w(TAG, "QosStatus [setStatus] param error");
            return;
        }
        if (!this.mResult.has(str)) {
            LogUtil.w(TAG, "QosStatus [setStatus] mResult \u4e0d\u5305\u542b " + str);
            return;
        }
        try {
            JSONObject jSONObject = this.mResult.getJSONObject(str);
            if (jSONObject != null) {
                jSONObject.put("status", i);
            }
        } catch (JSONException e) {
            LogUtil.w(TAG, "QosStatus [setStatus] JSONException=" + e);
        }
    }

    public String getExpire(String str) throws JSONException {
        if (TextUtils.isEmpty(str)) {
            LogUtil.w(TAG, "QosStatus [getExpire] param error");
            return null;
        }
        if (!this.mResult.has(str)) {
            LogUtil.w(TAG, "QosStatus [getExpire] mResult \u4e0d\u5305\u542b " + str);
            return null;
        }
        try {
            JSONObject jSONObject = this.mResult.getJSONObject(str);
            if (jSONObject == null || !jSONObject.has(Const.CONFIG_KEY.EXPIRE)) {
                return null;
            }
            return jSONObject.getString(Const.CONFIG_KEY.EXPIRE);
        } catch (JSONException e) {
            LogUtil.w(TAG, "QosStatus [getExpire] JSONException=" + e);
            return null;
        }
    }

    public void setExpire(String str, String str2) throws JSONException {
        if (TextUtils.isEmpty(str)) {
            LogUtil.w(TAG, "QosStatus [setExpire] param error");
            return;
        }
        if (!this.mResult.has(str)) {
            LogUtil.w(TAG, "QosStatus [setExpire] mResult \u4e0d\u5305\u542b " + str);
            return;
        }
        try {
            JSONObject jSONObject = this.mResult.getJSONObject(str);
            if (jSONObject != null) {
                jSONObject.put(Const.CONFIG_KEY.EXPIRE, str2);
            }
        } catch (JSONException e) {
            LogUtil.w(TAG, "QosStatus [setExpire] JSONException=" + e);
        }
    }

    public String getId(String str) throws JSONException {
        if (TextUtils.isEmpty(str)) {
            LogUtil.w(TAG, "QosStatus [getId] param error");
            return null;
        }
        if (!this.mResult.has(str)) {
            LogUtil.w(TAG, "QosStatus [getId] mResult \u4e0d\u5305\u542b " + str);
            return null;
        }
        try {
            JSONObject jSONObject = this.mResult.getJSONObject(str);
            if (jSONObject == null || !jSONObject.has(ResIdReader.RES_TYPE_ID)) {
                return null;
            }
            return jSONObject.getString(ResIdReader.RES_TYPE_ID);
        } catch (JSONException e) {
            LogUtil.w(TAG, "QosStatus [getExpire] JSONException=" + e);
            return null;
        }
    }

    public void setId(String str, String str2) throws JSONException {
        if (TextUtils.isEmpty(str)) {
            LogUtil.w(TAG, "QosStatus [setId] param error");
            return;
        }
        if (!this.mResult.has(str)) {
            LogUtil.w(TAG, "QosStatus [setId] mResult \u4e0d\u5305\u542b " + str);
            return;
        }
        try {
            JSONObject jSONObject = this.mResult.getJSONObject(str);
            if (jSONObject != null) {
                jSONObject.put(ResIdReader.RES_TYPE_ID, str2);
            }
        } catch (JSONException e) {
            LogUtil.w(TAG, "QosStatus [setId] JSONException=" + e);
        }
    }

    public void clean() {
        this.mResult = new JSONObject();
    }

    public void cleanIp(String str) {
        if (TextUtils.isEmpty(str)) {
            LogUtil.w(TAG, "QosStatus [cleanIp] param error");
        } else {
            if (!this.mResult.has(str)) {
                LogUtil.w(TAG, "QosStatus [setId] mResult \u4e0d\u5305\u542b " + str);
                return;
            }
            this.mResult.remove(str);
        }
    }

    public void setTestData() throws JSONException {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(ResIdReader.RES_TYPE_ID, "1111");
            jSONObject.put(Const.CONFIG_KEY.EXPIRE, System.currentTimeMillis());
            jSONObject.put("status", 0);
            jSONObject.put("validity", System.currentTimeMillis());
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put(ResIdReader.RES_TYPE_ID, "222");
            jSONObject2.put(Const.CONFIG_KEY.EXPIRE, System.currentTimeMillis());
            jSONObject2.put("status", 0);
            jSONObject2.put("validity", System.currentTimeMillis());
            this.mResult.put("8.8.8.8", jSONObject);
            this.mResult.put("4.4.4.4", jSONObject2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getAllIp() {
        ArrayList<String> arrayList = new ArrayList<>();
        LogUtil.i(TAG, "QosStatus [getAllIp] mResult " + this.mResult.toString());
        JSONObject jSONObject = this.mResult;
        if (jSONObject != null) {
            Iterator<String> itKeys = jSONObject.keys();
            while (itKeys.hasNext()) {
                arrayList.add(itKeys.next());
            }
        }
        LogUtil.i(TAG, "QosStatus [getAllIp] result " + arrayList);
        return arrayList;
    }

    public void cleanTimeOutIps() throws JSONException {
        ArrayList<String> allIp = getAllIp();
        LogUtil.i(TAG, "QosStatus [cleanTimeOutIps] \u6392\u67e5\u8fc7\u671fip\u524d\uff0c\u7ed3\u679c " + this.mResult.toString());
        Iterator<String> it = allIp.iterator();
        while (it.hasNext()) {
            String next = it.next();
            long validity = getValidity(next);
            long jCurrentTimeMillis = System.currentTimeMillis();
            LogUtil.i(TAG, "QosStatus [cleanTimeOutIps] validity=" + validity + ", currentTimeMillis=" + jCurrentTimeMillis);
            if (validity < jCurrentTimeMillis) {
                LogUtil.i(TAG, "QosStatus [cleanTimeOutIps] \u79fb\u9664\u8fc7\u671fip =  " + next);
                this.mResult.remove(next);
            }
        }
        LogUtil.i(TAG, "QosStatus [cleanTimeOutIps] \u6392\u67e5\u8fc7\u671fip\u540e\uff0c\u7ed3\u679c " + this.mResult.toString());
    }

    public long getMinExpire() throws NumberFormatException {
        ArrayList<String> allIp = getAllIp();
        long jCurrentTimeMillis = System.currentTimeMillis();
        Iterator<String> it = allIp.iterator();
        long j = 3600000;
        while (it.hasNext()) {
            long j2 = Long.parseLong(getExpire(it.next()));
            LogUtil.i(TAG, "QosStatus [getMinExpire] expire_long= " + j2 + ", currentTimeMillis=" + jCurrentTimeMillis);
            long j3 = (j2 * 1000) - jCurrentTimeMillis;
            if (j3 < j && j3 > 0) {
                j = j3;
            }
        }
        LogUtil.i(TAG, "QosStatus [getMinExpire] \u6700\u5c0f\u65f6\u95f4= " + j);
        return j;
    }

    public boolean isOverExpire(String str) throws NumberFormatException {
        if (TextUtils.isEmpty(str)) {
            LogUtil.i(TAG, "QosStatus [isOverExpire] param error");
            return false;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        long j = Long.parseLong(getExpire(str));
        LogUtil.i(TAG, "QosStatus [isOverExpire] expire_long= " + j + ", currentTimeMillis=" + jCurrentTimeMillis);
        boolean z = j == 0 || j * 1000 < jCurrentTimeMillis;
        LogUtil.i(TAG, "QosStatus [isOverExpire] ip= " + str + ", result=" + z);
        return z;
    }
}