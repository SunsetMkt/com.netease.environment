package com.netease.download.httpdns;

import android.text.TextUtils;
import com.netease.download.Const;
import com.netease.download.downloader.TaskHandleOp;
import com.netease.download.util.LogUtil;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class HttpdnsDomain2IpParams {
    private static final String TAG = "HttpdnsDomain2IpParams";
    private static HttpdnsDomain2IpParams sHttpdnsDomain2IpParams;
    private static volatile ArrayList<Unit> sHttpdnsDomain2IpUnitList = new ArrayList<>();

    public static HttpdnsDomain2IpParams getInstances() {
        if (sHttpdnsDomain2IpParams == null) {
            sHttpdnsDomain2IpParams = new HttpdnsDomain2IpParams();
        }
        return sHttpdnsDomain2IpParams;
    }

    private boolean isContainCdn(String str) {
        Iterator<Unit> it = sHttpdnsDomain2IpUnitList.iterator();
        while (it.hasNext()) {
            if (it.next().domain.equals(str)) {
                return true;
            }
        }
        return false;
    }

    public synchronized ArrayList<Unit> getHttpdnsDomain2IpUnitList() {
        return sHttpdnsDomain2IpUnitList;
    }

    public synchronized boolean init(String str) {
        LogUtil.stepLog("Httpdns\u73af\u8282--\u901a\u8fc7httpdns\u670d\u52a1\u5668\u89e3\u6790\u57df\u540d\uff0c\u7ed3\u679c\u53c2\u6570\u89e3\u6790\u5668\uff0c\u521d\u59cb\u5316\u6570\u636e");
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        ArrayList arrayList = new ArrayList();
        try {
            JSONObject jSONObject = new JSONObject(str);
            String strOptString = jSONObject.optString(Const.NT_PARAM_DOMAIN);
            JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("addrs");
            if (jSONArrayOptJSONArray != null && jSONArrayOptJSONArray.length() > 0) {
                for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
                    arrayList.add(jSONArrayOptJSONArray.optString(i));
                    TaskHandleOp.getInstance().addChannelHttpdnsIpMap(strOptString, jSONArrayOptJSONArray.optString(i));
                }
            }
            int iOptInt = jSONObject.optInt("ttl");
            if (!isContainCdn(strOptString)) {
                sHttpdnsDomain2IpUnitList.add(new Unit(strOptString, arrayList, iOptInt));
            }
            LogUtil.i(TAG, "Httpdns\u73af\u8282--\u901a\u8fc7httpdns\u670d\u52a1\u5668\u89e3\u6790\u57df\u540d\uff0c\u7ed3\u679c\u53c2\u6570\u89e3\u6790\u5668, \u89e3\u6790\u7ed3\u679c=" + sHttpdnsDomain2IpUnitList.toString());
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static class Unit {
        public String domain;
        public ArrayList<String> ipArrayList;
        public int ttl;

        public Unit(String str, ArrayList<String> arrayList, int i) {
            this.domain = str;
            this.ipArrayList = arrayList;
            this.ttl = i;
        }

        public String toString() {
            return "domain=" + this.domain + ", ipArrayList=" + this.ipArrayList.toString() + ", ttl=" + this.ttl;
        }
    }
}