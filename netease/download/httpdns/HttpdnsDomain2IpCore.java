package com.netease.download.httpdns;

import com.netease.download.downloader.TaskHandleOp;
import com.netease.download.httpdns.ServicesNodeParams;
import com.netease.download.network.OkHttpProxy;
import com.netease.download.util.LogUtil;
import com.netease.download.util.Util;
import com.netease.ntunisdk.okhttp3.Call;
import com.netease.ntunisdk.okhttp3.Callback;
import com.netease.ntunisdk.okhttp3.Request;
import com.netease.ntunisdk.okhttp3.Response;
import com.netease.pharos.Const;
import com.xiaomi.gamecenter.sdk.utils.RSASignature;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class HttpdnsDomain2IpCore implements Callable<Integer> {
    private static final String TAG = "HttpdnsDomain2IpCore";
    private String mDomain;
    private long mStartTime;
    private String mZone;
    private ArrayList<String> mHttpdnsServicesIpList = new ArrayList<>();
    private int mIndex = 0;
    private Callback okhttpCallback = new Callback() { // from class: com.netease.download.httpdns.HttpdnsDomain2IpCore.1
        @Override // com.netease.ntunisdk.okhttp3.Callback
        public void onFailure(Call call, IOException iOException) {
            LogUtil.stepLog("HttpdnsDomain2IpCore [okhttpCallback] [onFailure] start");
            if (call == null) {
                return;
            }
            LogUtil.i(HttpdnsDomain2IpCore.TAG, "HttpdnsDomain2IpCore [okhttpCallback] [onFailure] Call Header=" + call.request().headers().toMultimap().toString() + ", request = " + call.request().toString());
        }

        @Override // com.netease.ntunisdk.okhttp3.Callback
        public void onResponse(Call call, Response response) throws IOException {
            LogUtil.stepLog("HttpdnsDomain2IpCore [okhttpCallback] [onResponse] start");
            if (call == null || response == null) {
                return;
            }
            LogUtil.i(HttpdnsDomain2IpCore.TAG, "HttpdnsDomain2IpCore [okhttpCallback] [onResponse] Call Header=" + call.request().headers().toMultimap().toString() + ", request = " + call.request().toString());
            LogUtil.i(HttpdnsDomain2IpCore.TAG, "HttpdnsDomain2IpCore [okhttpCallback] [onResponse] Response Header=" + response.headers().toMultimap().toString() + ", hashCode=" + response.code() + ", resUrl=" + response.request().url() + ", protocol=" + response.protocol() + ", " + response.request().toString());
            System.currentTimeMillis();
            long unused = HttpdnsDomain2IpCore.this.mStartTime;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.body().byteStream(), RSASignature.c));
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                String line = bufferedReader.readLine();
                if (line != null) {
                    stringBuffer.append(line);
                } else {
                    LogUtil.i(HttpdnsDomain2IpCore.TAG, "Httpdns\u73af\u8282--\u901a\u8fc7httpdns\u670d\u52a1\u5668\u89e3\u6790\u57df\u540d\uff0c\u8bf7\u6c42\u7ed3\u679c\u6570\u636e=" + stringBuffer.toString());
                    try {
                        HttpdnsDomain2IpParams.getInstances().init(new JSONObject(stringBuffer.toString()).toString());
                        return;
                    } catch (JSONException e) {
                        LogUtil.stepLog("HttpdnsDomain2IpCore [okhttpCallback] [onResponse] JSONException=" + e.toString());
                        e.printStackTrace();
                        throw new IOException();
                    }
                }
            }
        }
    };

    public void init(ServicesNodeParams.HttpdnsServicesUnit httpdnsServicesUnit, String str) {
        if (httpdnsServicesUnit != null) {
            this.mZone = httpdnsServicesUnit.zone;
            this.mHttpdnsServicesIpList = httpdnsServicesUnit.ipArrayList;
            if (str.contains("/")) {
                this.mDomain = Util.getDomainFromUrl(str);
            } else {
                this.mDomain = str;
            }
        }
    }

    private boolean hasNext() {
        ArrayList<String> arrayList = this.mHttpdnsServicesIpList;
        return arrayList != null && arrayList.size() > 0 && this.mIndex < this.mHttpdnsServicesIpList.size();
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0015  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.String next() {
        /*
            r2 = this;
            java.util.ArrayList<java.lang.String> r0 = r2.mHttpdnsServicesIpList
            if (r0 == 0) goto L15
            int r0 = r0.size()
            int r1 = r2.mIndex
            if (r0 <= r1) goto L15
            java.util.ArrayList<java.lang.String> r0 = r2.mHttpdnsServicesIpList
            java.lang.Object r0 = r0.get(r1)
            java.lang.String r0 = (java.lang.String) r0
            goto L16
        L15:
            r0 = 0
        L16:
            int r1 = r2.mIndex
            int r1 = r1 + 1
            r2.mIndex = r1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.download.httpdns.HttpdnsDomain2IpCore.next():java.lang.String");
    }

    public int start() {
        LogUtil.stepLog("Httpdns\u73af\u8282--\u901a\u8fc7httpdns\u670d\u52a1\u5668\u89e3\u6790\u57df\u540d\uff0c\u5f00\u59cb");
        if (hasNext()) {
            return reqCdnTargetIp(HttpDnsUtil.getHttpdnsDomain2IpUrl(next(), this.mDomain));
        }
        return 11;
    }

    public synchronized int reqCdnTargetIp(String str) {
        int iExecute_syn;
        LogUtil.stepLog("Httpdns\u73af\u8282--\u901a\u8fc7httpdns\u670d\u52a1\u5668\u89e3\u6790\u57df\u540d\uff0c\u521d\u59cb\u5316");
        new HashMap();
        LogUtil.i(TAG, "Httpdns\u73af\u8282--\u901a\u8fc7httpdns\u670d\u52a1\u5668\u89e3\u6790\u57df\u540d\uff0curl=" + str);
        this.mStartTime = System.currentTimeMillis();
        Request.Builder builderUrl = new Request.Builder().url(str);
        String overSea = TaskHandleOp.getInstance().getTaskHandle().getOverSea();
        if ("1".equals(overSea) || "2".equals(overSea)) {
            LogUtil.i(TAG, "Httpdns\u73af\u8282--\u6d77\u5916");
            builderUrl.addHeader("Host", Const.HTTP_DNS_SERVER_OVERSEA);
        } else {
            LogUtil.i(TAG, "Httpdns\u73af\u8282--\u5927\u9646");
            builderUrl.addHeader("Host", Const.HTTP_DNS_SERVER_MAINLAND);
        }
        iExecute_syn = OkHttpProxy.getInstance().execute_syn(builderUrl, this.okhttpCallback);
        LogUtil.i(TAG, "Httpdns\u73af\u8282--\u901a\u8fc7httpdns\u670d\u52a1\u5668\u89e3\u6790\u57df\u540d,\u8bf7\u6c42\u7ed3\u679c=" + iExecute_syn);
        if (iExecute_syn != 0 && hasNext()) {
            iExecute_syn = start();
        }
        return iExecute_syn;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.concurrent.Callable
    public Integer call() throws Exception {
        return Integer.valueOf(start());
    }
}