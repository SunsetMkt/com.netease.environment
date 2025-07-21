package com.netease.download.reporter;

import android.os.Looper;
import android.text.TextUtils;
import com.netease.download.Const;
import com.netease.download.UrlSwitcher.HttpdnsUrlSwitcherCore;
import com.netease.download.config.ConfigParams;
import com.netease.download.config.ConfigProxy;
import com.netease.download.downloader.TaskHandleOp;
import com.netease.download.httpdns.HttpdnsProxy;
import com.netease.download.network.OkHttpProxy;
import com.netease.download.reporter.ReportUrlController;
import com.netease.download.util.LogUtil;
import com.netease.download.util.Util;
import com.netease.ntunisdk.okhttp3.Call;
import com.netease.ntunisdk.okhttp3.Callback;
import com.netease.ntunisdk.okhttp3.MediaType;
import com.netease.ntunisdk.okhttp3.Request;
import com.netease.ntunisdk.okhttp3.RequestBody;
import com.netease.ntunisdk.okhttp3.Response;
import com.xiaomi.gamecenter.sdk.robust.Constants;
import java.io.IOException;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes4.dex */
public class ReportNet {
    private static final String TAG = "ReportNet";
    private static ReportNet sReportNet;
    private Callback okhttpCallback = new Callback() { // from class: com.netease.download.reporter.ReportNet.3
        @Override // com.netease.ntunisdk.okhttp3.Callback
        public void onResponse(Call call, Response response) throws IOException {
            LogUtil.stepLog("ReportNet [okhttpCallback] [onResponse] start");
            if (call == null || response == null) {
                return;
            }
            LogUtil.i(ReportNet.TAG, "ReportNet [okhttpCallback] [onResponse] Call Header=" + call.request().headers().toMultimap().toString() + ", request = " + call.request().toString());
            LogUtil.i(ReportNet.TAG, "ReportNet [okhttpCallback] [onResponse] Response Header=" + response.headers().toMultimap().toString() + ", hashCode=" + response.code() + ", resUrl=" + response.request().url() + ", protocol=" + response.protocol() + ", " + response.request().toString());
            int iCode = response.code();
            LogUtil.stepLog("ReportNet [okhttpCallback] [onResponse] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u4e0a\u4f20\u65e5\u5fd7\uff0c\u8bf7\u6c42\u7ed3\u679c\u89e3\u6790");
            String str = (String) call.request().tag();
            if (!TextUtils.isEmpty(str) && !"sss".equals(str)) {
                LogUtil.stepLog("ReportNet [okhttpCallback] [onResponse] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u4e0a\u4f20\u65e5\u5fd7 \u4e0a\u4f20\u6210\u529f\uff0c\u5220\u9664\u6587\u4ef6\uff0c\u6587\u4ef6\u8def\u5f84=" + str);
                ReportFile.getInstances().deleteFile(str);
            }
            LogUtil.i(ReportNet.TAG, "ReportNet [okhttpCallback] [onResponse] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u4e0a\u4f20\u65e5\u5fd7\uff0c\u8bf7\u6c42\u8fd4\u56de\u7801=" + iCode);
            ReportCallBack reportCallBack = ReportProxy.getInstance().getmReportCallBack();
            if (reportCallBack != null) {
                reportCallBack.reportFinish();
            }
        }

        @Override // com.netease.ntunisdk.okhttp3.Callback
        public void onFailure(Call call, IOException iOException) {
            ReportCallBack reportCallBack;
            LogUtil.stepLog("ReportNet [okhttpCallback] [onFailure] start");
            if (call == null || (reportCallBack = ReportProxy.getInstance().getmReportCallBack()) == null) {
                return;
            }
            reportCallBack.reportFinish();
        }
    };

    private ReportNet() {
    }

    public static ReportNet getInstances() {
        if (sReportNet == null) {
            sReportNet = new ReportNet();
        }
        return sReportNet;
    }

    public void report(final String str, final int i) {
        try {
            ConfigParams configParams = ConfigProxy.getInstances().getmConfigParams();
            if (configParams != null && !configParams.isReport()) {
                LogUtil.i(TAG, "ReportNet [report] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u4e0a\u4f20\u65e5\u5fd7,\u53c2\u6570\u9519\u8bef");
            } else if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() { // from class: com.netease.download.reporter.ReportNet.1
                    @Override // java.lang.Runnable
                    public void run() throws JSONException {
                        ReportNet.this.reportControl(str, false, i);
                    }
                }).start();
            } else {
                reportControl(str, false, i);
            }
        } catch (Exception e) {
            LogUtil.w(TAG, "ReportNet [report] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u4e0a\u4f20\u65e5\u5fd7 Exception=" + e);
        }
    }

    public void reportFile(final String str, final int i) {
        String reportUrl;
        String[] reportIpArray;
        LogUtil.i(TAG, "ReportNet [reportFile] start");
        if (TextUtils.isEmpty(str)) {
            LogUtil.i(TAG, "ReportNet [reportFile] \u53c2\u6570\u9519\u8bef1");
            return;
        }
        ConfigParams configParams = ConfigProxy.getInstances().getmConfigParams();
        LogUtil.i(TAG, "ReportProxy [reportFile] \u91c7\u7528\u914d\u7f6e\u6587\u4ef6 ip");
        if (configParams != null) {
            reportUrl = configParams.getReportUrl();
            reportIpArray = configParams.getReportIpArray();
        } else {
            reportUrl = null;
            reportIpArray = null;
        }
        if (TextUtils.isEmpty(reportUrl) || reportIpArray == null || reportIpArray.length <= 0) {
            LogUtil.i(TAG, "ReportProxy [reportFile] \u91c7\u7528hardcode ip");
            reportIpArray = Const.REQ_IPS_FOR_LOG;
            String overSea = TaskHandleOp.getInstance().getTaskHandle().getOverSea();
            LogUtil.i(TAG, "ReportProxy [report] \u6d77\u5916=" + overSea);
            String str2 = "https://sigma-orbit-impression.proxima.nie.easebar.com/sdk";
            if ("1".equals(overSea) || "2".equals(overSea)) {
                reportIpArray = Const.REQ_IPS_FOR_LOG_OVERSEA;
            } else {
                str2 = "https://sigma-orbit-impression.proxima.nie.netease.com/sdk";
                if ("0".equals(overSea)) {
                    reportIpArray = Const.REQ_IPS_FOR_LOG_CHINA;
                } else if ("-1".equals(overSea)) {
                    reportIpArray = Const.REQ_IPS_FOR_LOG_GLOBAL;
                }
            }
            reportUrl = str2;
        }
        LogUtil.i(TAG, "ReportProxy [reportFile] url=" + reportUrl);
        ReportUrlController.getInstance().init(reportUrl, reportIpArray);
        LogUtil.i(TAG, "ReportProxy [reportFile] ReportUrlController=" + ReportUrlController.getInstance().geturls().toString());
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() { // from class: com.netease.download.reporter.ReportNet.2
                    @Override // java.lang.Runnable
                    public void run() throws JSONException {
                        ReportNet.this.reportControl(str, true, i);
                    }
                }).start();
            } else {
                reportControl(str, true, i);
            }
        } catch (Exception e) {
            LogUtil.w(TAG, "ReportNet [reportFile] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u4e0a\u4f20\u65e5\u5fd7 Exception=" + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int reportControl(String str, boolean z, int i) throws JSONException {
        String file;
        String str2;
        String strReplaceDomainWithIpAddr;
        String str3;
        String str4;
        LogUtil.i(TAG, "ReportNet [reportControl] start");
        if (TextUtils.isEmpty(str)) {
            LogUtil.i(TAG, "ReportNet [reportControl] param error");
            return 14;
        }
        HashMap map = new HashMap();
        if (z) {
            file = ReportFile.getInstances().readFile(str);
            map.put("filepath", str);
            try {
                JSONObject jSONObject = new JSONObject(file);
                jSONObject.put("log_delay", i);
                file = jSONObject.toString();
            } catch (Exception e) {
                LogUtil.i(TAG, "ReportNet [reportControl] Exception=" + e.toString());
                e.printStackTrace();
            }
            str2 = str;
        } else {
            file = str;
            str2 = null;
        }
        int iPostOkhttp = 11;
        if (!TextUtils.isEmpty(file)) {
            try {
                if (TaskHandleOp.getInstance().getTaskHandle().isLogOpen()) {
                    LogUtil.i(TAG, "ReportNet [reportControl] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u4e0a\u4f20\u65e5\u5fd7\uff0cUrls\u4fe1\u606f\u603b\u89c8=" + ReportUrlController.getInstance().geturls().toString());
                }
                if (ReportUrlController.getInstance().hasNext()) {
                    ReportUrlController.ReportUrlControllerUnit next = ReportUrlController.getInstance().next();
                    str3 = next.mDomain;
                    strReplaceDomainWithIpAddr = next.mUrl;
                } else {
                    strReplaceDomainWithIpAddr = null;
                    str3 = null;
                }
                for (int i2 = 3; iPostOkhttp != 0 && i2 > 0 && !TextUtils.isEmpty(strReplaceDomainWithIpAddr); i2 += -1) {
                    LogUtil.i(TAG, "ReportNet [reportControl] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u4e0a\u4f20\u65e5\u5fd7\uff0c\u5177\u4f53\u4f7f\u7528\u7684domain=" + str3 + ", url=" + strReplaceDomainWithIpAddr);
                    LogUtil.i(TAG, "ReportNet [reportControl use okhttp");
                    iPostOkhttp = postOkhttp(strReplaceDomainWithIpAddr, str3, file, str2);
                }
                LogUtil.i(TAG, "ReportNet [reportControl] \u6b63\u5e38\u4e0a\u4f20\u7ed3\u679c result=" + iPostOkhttp);
                if (iPostOkhttp != 0) {
                    ReportUrlController.getInstance().removeUnit();
                    LogUtil.i(TAG, "ReportNet [reportControl] \u8fdb\u5165httpdns\u903b\u8f91");
                    if (!HttpdnsProxy.getInstances().containKey(Const.HTTPDNS_CONFIG_MODULE)) {
                        LogUtil.i(TAG, "ReportNet [reportControl] \u65e5\u5fd7\u6a21\u5757\u672a\u8bf7\u6c42\u8fc7httpdns\uff0c\u53d1\u8d77httpdns url=" + strReplaceDomainWithIpAddr);
                        HttpdnsProxy.getInstances().synStart(Const.HTTPDNS_CONFIG_MODULE, new String[]{Util.getDomainFromUrl(strReplaceDomainWithIpAddr)});
                    }
                    if (HttpdnsProxy.getInstances().containKey(Const.HTTPDNS_CONFIG_MODULE)) {
                        LogUtil.i(TAG, "ReportNet [reportControl] \u65e5\u5fd7\u6a21\u5757\u5df2\u53d1\u8d77httpdns url=" + strReplaceDomainWithIpAddr);
                        String cdnChannel = Util.getCdnChannel(strReplaceDomainWithIpAddr);
                        LogUtil.i(TAG, "ReportNet [reportControl] channel=" + cdnChannel);
                        while (HttpdnsProxy.getInstances().hasNext(Const.HTTPDNS_CONFIG_MODULE) && iPostOkhttp != 0) {
                            LogUtil.i(TAG, "ReportNet [reportControl] httpdns\u5b58\u5728\u672a\u4f7f\u7528\u7684ip");
                            HttpdnsUrlSwitcherCore.HttpdnsUrlSwitcherCoreUnit next2 = HttpdnsProxy.getInstances().next(Const.HTTPDNS_CONFIG_MODULE, cdnChannel);
                            if (next2 != null) {
                                String str5 = next2.host;
                                str4 = next2.ip;
                                strReplaceDomainWithIpAddr = Util.isIpv6(str4) ? Util.replaceDomainWithIpAddr(strReplaceDomainWithIpAddr, Constants.C + str4 + "]", "/") : Util.replaceDomainWithIpAddr(strReplaceDomainWithIpAddr, str4, "/");
                                LogUtil.i(TAG, "ReportNet [reportControl] mHost=" + str5 + ", mIp=" + str4 + ", url=" + strReplaceDomainWithIpAddr);
                                LogUtil.i(TAG, "ReportNet [reportControl] use okhttp");
                                iPostOkhttp = postOkhttp(strReplaceDomainWithIpAddr, str5, file, str);
                                StringBuilder sb = new StringBuilder();
                                sb.append("ReportNet [reportControl] httpdns \u4e0a\u4f20\u7ed3\u679c result=");
                                sb.append(iPostOkhttp);
                                LogUtil.i(TAG, sb.toString());
                            } else {
                                LogUtil.i(TAG, "ReportNet [reportControl] unit is null");
                                str4 = null;
                            }
                            if (iPostOkhttp != 0) {
                                LogUtil.i(TAG, "ReportNet [reportControl] \u5220\u9664ip=" + str4 + ", \u6240\u5c5echannel=" + cdnChannel);
                                HttpdnsProxy.getInstances().remove(Const.HTTPDNS_CONFIG_MODULE, str4, cdnChannel);
                            }
                            LogUtil.i(TAG, "ReportNet [reportControl] \u662f\u5426\u8fd8\u6709\u672a\u4f7f\u7528\u7684\u4e0a\u4f20ip=" + HttpdnsProxy.getInstances().hasNext(Const.HTTPDNS_CONFIG_MODULE));
                        }
                    }
                }
            } catch (Exception e2) {
                LogUtil.w(TAG, "ReportNet [reportControl] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u4e0a\u4f20\u65e5\u5fd7 Exception=" + e2);
            }
            LogUtil.i(TAG, "ReportNet [reportControl] \u4e0a\u4f20\u603b\u7ed3\u679c=" + iPostOkhttp);
        } else {
            ReportProxy.getInstance().getmReportCallBack().reportFinish();
        }
        return iPostOkhttp;
    }

    private int postOkhttp(String str, String str2, String str3, String str4) {
        LogUtil.e(TAG, "ReportNet [postOkhttp] start");
        Integer.valueOf(11);
        LogUtil.e(TAG, "ReportNet [postOkhttp] configUrl=" + str + ", infos=" + str3 + ", filePath=" + str4);
        RequestBody requestBodyCreate = RequestBody.create(MediaType.parse("text/plain;charset=utf-8"), str3);
        Request.Builder builderUrl = new Request.Builder().url(str);
        builderUrl.post(requestBodyCreate);
        if (!TextUtils.isEmpty(str4)) {
            builderUrl.tag(str4);
        } else {
            builderUrl.tag("sss");
        }
        if (!TextUtils.isEmpty(str2)) {
            builderUrl.addHeader("Host", str2);
        }
        builderUrl.url(str);
        return Integer.valueOf(OkHttpProxy.getInstance().execute_syn(builderUrl, this.okhttpCallback)).intValue();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:44:0x012b  */
    /* JADX WARN: Type inference failed for: r14v0, types: [com.netease.download.network.NetworkDealer] */
    /* JADX WARN: Type inference failed for: r14v10, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r14v11 */
    /* JADX WARN: Type inference failed for: r14v12 */
    /* JADX WARN: Type inference failed for: r14v13 */
    /* JADX WARN: Type inference failed for: r14v3 */
    /* JADX WARN: Type inference failed for: r14v4 */
    /* JADX WARN: Type inference failed for: r14v5 */
    /* JADX WARN: Type inference failed for: r14v6 */
    /* JADX WARN: Type inference failed for: r14v7, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r14v8, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r14v9 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int postHttps(java.lang.String r11, java.lang.String r12, java.lang.String r13, com.netease.download.network.NetworkDealer r14, java.util.Map<java.lang.String, java.lang.String> r15) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 351
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.download.reporter.ReportNet.postHttps(java.lang.String, java.lang.String, java.lang.String, com.netease.download.network.NetworkDealer, java.util.Map):int");
    }
}