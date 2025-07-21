package com.netease.download.httpdns;

import android.text.TextUtils;
import com.netease.download.util.LogUtil;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class ServicesNodeParams {
    private static final String TAG = "ServicesNodeParams";
    private static ServicesNodeParams sServicesNodeParams;
    private ArrayList<HttpdnsServicesUnit> mHttpdnsServicesUnitList = new ArrayList<>();

    public static ServicesNodeParams getInstances() {
        if (sServicesNodeParams == null) {
            sServicesNodeParams = new ServicesNodeParams();
        }
        return sServicesNodeParams;
    }

    public boolean contain(String str) {
        ArrayList<HttpdnsServicesUnit> arrayList = this.mHttpdnsServicesUnitList;
        boolean z = false;
        if (arrayList != null && arrayList.size() > 0) {
            Iterator<HttpdnsServicesUnit> it = this.mHttpdnsServicesUnitList.iterator();
            while (it.hasNext()) {
                if (it.next().zone.equals(str)) {
                    z = true;
                }
            }
        }
        return z;
    }

    public HttpdnsServicesUnit get(String str) {
        ArrayList<HttpdnsServicesUnit> arrayList = this.mHttpdnsServicesUnitList;
        if (arrayList != null && arrayList.size() > 0) {
            Iterator<HttpdnsServicesUnit> it = this.mHttpdnsServicesUnitList.iterator();
            while (it.hasNext()) {
                HttpdnsServicesUnit next = it.next();
                if (next.zone.equals(str)) {
                    return next;
                }
            }
        }
        return null;
    }

    public ArrayList<HttpdnsServicesUnit> getHttpdnsServicesUnitList() {
        return this.mHttpdnsServicesUnitList;
    }

    public boolean init(String str) throws JSONException {
        LogUtil.stepLog("Httpdns\u73af\u8282--\u8bf7\u6c42SA\u81ea\u5efa\u7684Httpdns\u670d\u52a1\u5668ip\uff0c\u7ed3\u679c\u53c2\u6570\u89e3\u6790\u5668\uff0c\u521d\u59cb\u5316\u6570\u636e");
        if (TextUtils.isEmpty(str)) {
            LogUtil.i(TAG, "Httpdns\u73af\u8282--\u8bf7\u6c42SA\u81ea\u5efa\u7684Httpdns\u670d\u52a1\u5668ip\uff0c\u7ed3\u679c\u53c2\u6570\u89e3\u6790\u5668\uff0c\u6570\u636e\u4e3a\u7a7a");
            return false;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            Iterator<String> itKeys = jSONObject.keys();
            while (itKeys.hasNext()) {
                String next = itKeys.next();
                ArrayList arrayList = new ArrayList();
                if (!contain(next)) {
                    JSONArray jSONArray = jSONObject.getJSONArray(next);
                    for (int i = 0; i < jSONArray.length(); i++) {
                        arrayList.add(jSONArray.getString(i));
                    }
                    this.mHttpdnsServicesUnitList.add(new HttpdnsServicesUnit(next, arrayList));
                }
            }
            LogUtil.i(TAG, "Httpdns\u73af\u8282--\u8bf7\u6c42SA\u81ea\u5efa\u7684Httpdns\u670d\u52a1\u5668ip\uff0c\u7ed3\u679c\u53c2\u6570\u89e3\u6790\u5668 , \u89e3\u6790\u7ed3\u679c=" + this.mHttpdnsServicesUnitList.toString());
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static class HttpdnsServicesUnit {
        public ArrayList<String> ipArrayList;
        public String zone;

        public HttpdnsServicesUnit(String str, ArrayList<String> arrayList) {
            this.zone = str;
            this.ipArrayList = arrayList;
        }

        public String toString() {
            return "zone=" + this.zone + ", ipArrayList=" + this.ipArrayList.toString();
        }
    }
}