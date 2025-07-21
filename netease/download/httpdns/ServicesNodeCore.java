package com.netease.download.httpdns;

import android.text.TextUtils;
import android.util.Base64;
import com.netease.download.Const;
import com.netease.download.config.ConfigParams;
import com.netease.download.config.ConfigProxy;
import com.netease.download.dns.DnsCore;
import com.netease.download.dns.DnsParams;
import com.netease.download.downloader.TaskHandleOp;
import com.netease.download.lvsip.Lvsip;
import com.netease.download.network.OkHttpProxy;
import com.netease.download.util.LogUtil;
import com.netease.download.util.Util;
import com.netease.ntunisdk.okhttp3.Call;
import com.netease.ntunisdk.okhttp3.Callback;
import com.netease.ntunisdk.okhttp3.Request;
import com.netease.ntunisdk.okhttp3.Response;
import com.xiaomi.gamecenter.sdk.robust.Constants;
import com.xiaomi.gamecenter.sdk.utils.RSASignature;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONException;

/* loaded from: classes5.dex */
public class ServicesNodeCore {
    private static final String TAG = "HttpDnsCore";
    private String mHost = null;
    private Callback okhttpCallback = new Callback() { // from class: com.netease.download.httpdns.ServicesNodeCore.1
        @Override // com.netease.ntunisdk.okhttp3.Callback
        public void onFailure(Call call, IOException iOException) {
            LogUtil.stepLog("ServicesNodeCore [okhttpCallback] [onFailure] start");
        }

        @Override // com.netease.ntunisdk.okhttp3.Callback
        public void onResponse(Call call, Response response) throws JSONException, IOException {
            LogUtil.stepLog("ServicesNodeCore [okhttpCallback] [onResponse] start");
            if (call == null || response == null) {
                return;
            }
            LogUtil.i(ServicesNodeCore.TAG, "ServicesNodeCore [okhttpCallback] [onResponse] Call Header=" + call.request().headers().toMultimap().toString() + ", request = " + call.request().toString());
            LogUtil.i(ServicesNodeCore.TAG, "ServicesNodeCore [okhttpCallback] [onResponse] Response Header=" + response.headers().toMultimap().toString() + ", hashCode=" + response.code() + ", resUrl=" + response.request().url() + ", protocol=" + response.protocol() + ", " + response.request().toString());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.body().byteStream(), RSASignature.c));
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                try {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    } else {
                        stringBuffer.append(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            String str = new String(Base64.decode(stringBuffer.toString().getBytes(), 0));
            LogUtil.i(ServicesNodeCore.TAG, "Httpdns\u73af\u8282--\u8bf7\u6c42SA\u81ea\u5efa\u7684Httpdns\u670d\u52a1\u5668\uff0c\u83b7\u53d6\u7ed3\u679c = ".concat(str));
            ServicesNodeParams.getInstances().init(str);
        }
    };

    public void init() {
    }

    public synchronized int start() {
        int iReqServicesNodeIp;
        String strReplaceDomainWithIpAddr;
        String strReplaceDomainWithIpAddr2;
        LogUtil.stepLog("Httpdns\u73af\u8282--\u8bf7\u6c42SA\u81ea\u5efa\u7684Httpdns\u670d\u52a1\u5668ip");
        String overSea = TaskHandleOp.getInstance().getTaskHandle().getOverSea();
        LogUtil.i(TAG, "Httpdns\u73af\u8282--\u8bf7\u6c42SA\u81ea\u5efa\u7684Httpdns\u670d\u52a1\u5668ip\uff0c\u5148\u5bf9\u94fe\u63a5\u505aDNS\u89e3\u6790\uff0coversea=" + overSea);
        String httpdnsServicesIp = HttpDnsUtil.getHttpdnsServicesIp();
        if (!TextUtils.isEmpty(httpdnsServicesIp) && "2".equals(overSea)) {
            httpdnsServicesIp = Util.replaceDomain(httpdnsServicesIp, new String[]{"netease.com", "163.com"}, "easebar.com");
        }
        LogUtil.i(TAG, "Httpdns\u73af\u8282--\u8bf7\u6c42SA\u81ea\u5efa\u7684Httpdns\u670d\u52a1\u5668ip\uff0c\u5148\u5bf9\u94fe\u63a5\u505aDNS\u89e3\u6790,\u8bf7\u6c42DNS url=" + httpdnsServicesIp);
        DnsCore.getInstances().init(httpdnsServicesIp);
        ArrayList<DnsParams.Unit> arrayListStart = DnsCore.getInstances().start();
        LogUtil.i(TAG, "Httpdns\u73af\u8282--\u8bf7\u6c42SA\u81ea\u5efa\u7684Httpdns\u670d\u52a1\u5668ip\uff0c\u94fe\u63a5\u505aDNS\u89e3\u6790\uff0cDNS\u7ed3\u679c=" + arrayListStart.toString());
        iReqServicesNodeIp = 11;
        if (arrayListStart != null && arrayListStart.size() > 0) {
            Iterator<DnsParams.Unit> it = arrayListStart.iterator();
            while (it.hasNext()) {
                DnsParams.Unit next = it.next();
                Iterator<String> it2 = next.ipArrayList.iterator();
                while (it2.hasNext()) {
                    String next2 = it2.next();
                    if (Util.isIpv6(next2)) {
                        strReplaceDomainWithIpAddr2 = Util.replaceDomainWithIpAddr(httpdnsServicesIp, Constants.C + next2 + "]", "/");
                    } else {
                        strReplaceDomainWithIpAddr2 = Util.replaceDomainWithIpAddr(httpdnsServicesIp, next2, "/");
                    }
                    httpdnsServicesIp = strReplaceDomainWithIpAddr2;
                    iReqServicesNodeIp = reqServicesNodeIp(httpdnsServicesIp, next.domain);
                    if (iReqServicesNodeIp == 0) {
                        break;
                    }
                }
                if (iReqServicesNodeIp == 0) {
                    break;
                }
            }
        }
        if (iReqServicesNodeIp != 0) {
            LogUtil.stepLog("Httpdns\u73af\u8282--\u8bf7\u6c42SA\u81ea\u5efa\u7684Httpdns\u670d\u52a1\u5668ip, \u91c7\u7528lvsip, \u662f\u5426\u521b\u5efa\u8fc7lvsip\u5217\u8868=" + Lvsip.getInstance().isCteateIp());
            this.mHost = Util.getDomainFromUrl(Const.WS_HTTP_DNS_REQ_URL);
            if (!Lvsip.getInstance().isCteateIp()) {
                ConfigParams configParams = ConfigProxy.getInstances().getmConfigParams();
                String[] lvsipArray = configParams != null ? configParams.getLvsipArray() : null;
                if (lvsipArray == null || lvsipArray.length <= 0) {
                    String overSea2 = TaskHandleOp.getInstance().getTaskHandle().getOverSea();
                    if ("1".equals(overSea2)) {
                        lvsipArray = Const.REQ_IPS_WS_OVERSEA;
                        this.mHost = Const.REQ_URL_FOR_WS;
                    } else if ("2".equals(overSea2)) {
                        lvsipArray = Const.REQ_IPS_WS_OVERSEA;
                        this.mHost = "mbdl.update.easebar.com";
                    } else if ("0".equals(overSea2) || "-1".equals(overSea2)) {
                        lvsipArray = Const.REQ_IPS_WS_CHINA;
                        this.mHost = Const.REQ_URL_FOR_WS;
                    } else {
                        lvsipArray = Const.REQ_IPS_WS;
                        this.mHost = Const.REQ_URL_FOR_WS;
                    }
                }
                Lvsip.getInstance().init(lvsipArray);
                Lvsip.getInstance().createLvsip();
            }
            while (true) {
                if (!Lvsip.getInstance().hasNext() || iReqServicesNodeIp == 0) {
                    break;
                }
                String newIpFromArray = Lvsip.getInstance().getNewIpFromArray();
                LogUtil.i(TAG, "Httpdns\u73af\u8282--\u8bf7\u6c42SA\u81ea\u5efa\u7684Httpdns\u670d\u52a1\u5668ip, \u91c7\u7528lvsip\uff0c\u5c06\u8981\u4f7f\u7528\u7684ip=" + newIpFromArray);
                if (!TextUtils.isEmpty(newIpFromArray)) {
                    if (Util.isIpv6(newIpFromArray)) {
                        strReplaceDomainWithIpAddr = Util.replaceDomainWithIpAddr(httpdnsServicesIp, Constants.C + newIpFromArray + "]", "/");
                    } else {
                        strReplaceDomainWithIpAddr = Util.replaceDomainWithIpAddr(httpdnsServicesIp, newIpFromArray, "/");
                    }
                    LogUtil.i(TAG, "Httpdns\u73af\u8282--\u8bf7\u6c42SA\u81ea\u5efa\u7684Httpdns\u670d\u52a1\u5668ip, \u91c7\u7528lvsip\uff0c\u5c06\u8981\u4f7f\u7528\u7684host=" + this.mHost);
                    int iReqServicesNodeIp2 = reqServicesNodeIp(strReplaceDomainWithIpAddr, this.mHost);
                    if (iReqServicesNodeIp2 == 0) {
                        iReqServicesNodeIp = iReqServicesNodeIp2;
                        break;
                    }
                    httpdnsServicesIp = strReplaceDomainWithIpAddr;
                    iReqServicesNodeIp = iReqServicesNodeIp2;
                }
            }
        }
        return iReqServicesNodeIp;
    }

    public synchronized int reqServicesNodeIp(String str, String str2) {
        int iExecute_syn;
        LogUtil.stepLog("Httpdns\u73af\u8282--\u8bf7\u6c42SA\u81ea\u5efa\u7684Htttpdns\u670d\u52a1\u5668ip");
        iExecute_syn = 11;
        try {
            HashMap map = new HashMap();
            if (!TextUtils.isEmpty(str2)) {
                map.put("Host", str2);
                LogUtil.i(TAG, "Httpdns\u73af\u8282--\u8bf7\u6c42SA\u81ea\u5efa\u7684Htttpdns\u670d\u52a1\u5668ip\uff0chost=" + str2);
            }
            LogUtil.i(TAG, "Httpdns\u73af\u8282--\u8bf7\u6c42SA\u81ea\u5efa\u7684Htttpdns\u670d\u52a1\u5668ip\uff0curl=" + str);
            if (!TextUtils.isEmpty(str)) {
                Request.Builder builderUrl = new Request.Builder().url(str);
                if (!TextUtils.isEmpty(str2)) {
                    builderUrl.addHeader("Host", str2);
                }
                LogUtil.i(TAG, "ServicesNodeCore [reqServicesNodeIp] use okhttp");
                iExecute_syn = OkHttpProxy.getInstance().execute_syn(builderUrl, this.okhttpCallback);
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "Exception=" + e.toString() + ", url=" + str.toString());
            e.printStackTrace();
        }
        return iExecute_syn;
    }
}