package com.netease.download.config;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import com.facebook.hermes.intl.Constants;
import com.netease.download.Const;
import com.netease.download.downloader.DownloadInitInfo;
import com.netease.download.downloader.DownloadProxy;
import com.netease.download.downloader.TaskHandleOp;
import com.netease.download.network.OkHttpProxy;
import com.netease.download.util.LogUtil;
import com.netease.download.util.Util;
import com.netease.ntunisdk.okhttp3.Call;
import com.netease.ntunisdk.okhttp3.Callback;
import com.netease.ntunisdk.okhttp3.Request;
import com.netease.ntunisdk.okhttp3.Response;
import com.xiaomi.gamecenter.sdk.utils.RSASignature;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.regex.Pattern;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class ConfigCore {
    private static final String TAG = "ConfigCore";
    private HashMap<String, String> mLogData = new HashMap<>();
    private String mHost = null;
    private boolean mIsFirst = true;
    private Callback okhttpCallback = new Callback() { // from class: com.netease.download.config.ConfigCore.1
        AnonymousClass1() {
        }

        @Override // com.netease.ntunisdk.okhttp3.Callback
        public void onResponse(Call call, Response response) throws Throwable {
            boolean zUpdateLoaclConfigFile;
            LogUtil.stepLog("ConfigCore [okhttpCallback] [onResponse] start");
            if (call == null || response == null) {
                return;
            }
            LogUtil.i(ConfigCore.TAG, "ConfigCore [okhttpCallback] [onResponse] Call Header=" + call.request().headers().toMultimap().toString() + ", request = " + call.request().toString());
            LogUtil.i(ConfigCore.TAG, "ConfigCore [okhttpCallback] [onResponse] Response Header=" + response.headers().toMultimap().toString() + ", hashCode=" + response.code() + ", resUrl=" + response.request().url() + ", protocol=" + response.protocol() + ", " + response.request().toString());
            LogUtil.stepLog("ConfigCore [okhttpCallback] [onResponse] \u4e0b\u8f7d\u914d\u7f6e\u6587\u4ef6\uff0c\u89e3\u6790");
            InputStream inputStreamByteStream = response.body().byteStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStreamByteStream, RSASignature.c);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                } else {
                    sb.append(line);
                }
            }
            String string = sb.toString();
            LogUtil.i(ConfigCore.TAG, "ConfigCore [okhttpCallback] [onResponse] \u8bf7\u6c42\u5185\u5bb9=" + string);
            if (TextUtils.isEmpty(string)) {
                return;
            }
            LogUtil.i(ConfigCore.TAG, "ConfigCore [okhttpCallback] [onResponse] \u4e0b\u8f7d\u914d\u7f6e\u6587\u4ef6\uff0c\u89e3\u6790 mIsFirst=" + ConfigCore.this.mIsFirst);
            if (ConfigCore.this.mIsFirst) {
                zUpdateLoaclConfigFile = ConfigProxy.getInstances().getmConfigParams().parse(string, true);
                LogUtil.i(ConfigCore.TAG, "ConfigCore [okhttpCallback] [onResponse] \u914d\u7f6e\u6587\u4ef6\u5185\u5bb9=" + ConfigProxy.getInstances().getmConfigParams().toString());
                if (zUpdateLoaclConfigFile) {
                    zUpdateLoaclConfigFile = ConfigCore.this.updateLoaclConfigFile(string);
                }
            } else {
                LogUtil.i(ConfigCore.TAG, "ConfigCore [okhttpCallback] [onResponse] \u5355\u72ec\u66f4\u65b0\u914d\u7f6e\u6587\u4ef6");
                zUpdateLoaclConfigFile = ConfigCore.this.updateLoaclConfigFile(string);
            }
            inputStreamByteStream.close();
            inputStreamReader.close();
            bufferedReader.close();
            if (zUpdateLoaclConfigFile) {
                return;
            }
            TaskHandleOp.getInstance().getTaskHandle().setStatus(16);
            throw new IOException();
        }

        @Override // com.netease.ntunisdk.okhttp3.Callback
        public void onFailure(Call call, IOException iOException) {
            LogUtil.stepLog("ConfigCore [okhttpCallback] [onFailure] start");
            if (call == null) {
                return;
            }
            LogUtil.i(ConfigCore.TAG, "ConfigCore [okhttpCallback] [onFailure] Call Header=" + call.request().headers().toMultimap().toString() + ", request = " + call.request().toString());
            Util.getDomainFromUrl(call.request().url().toString());
        }
    };

    /* renamed from: com.netease.download.config.ConfigCore$1 */
    class AnonymousClass1 implements Callback {
        AnonymousClass1() {
        }

        @Override // com.netease.ntunisdk.okhttp3.Callback
        public void onResponse(Call call, Response response) throws Throwable {
            boolean zUpdateLoaclConfigFile;
            LogUtil.stepLog("ConfigCore [okhttpCallback] [onResponse] start");
            if (call == null || response == null) {
                return;
            }
            LogUtil.i(ConfigCore.TAG, "ConfigCore [okhttpCallback] [onResponse] Call Header=" + call.request().headers().toMultimap().toString() + ", request = " + call.request().toString());
            LogUtil.i(ConfigCore.TAG, "ConfigCore [okhttpCallback] [onResponse] Response Header=" + response.headers().toMultimap().toString() + ", hashCode=" + response.code() + ", resUrl=" + response.request().url() + ", protocol=" + response.protocol() + ", " + response.request().toString());
            LogUtil.stepLog("ConfigCore [okhttpCallback] [onResponse] \u4e0b\u8f7d\u914d\u7f6e\u6587\u4ef6\uff0c\u89e3\u6790");
            InputStream inputStreamByteStream = response.body().byteStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStreamByteStream, RSASignature.c);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                } else {
                    sb.append(line);
                }
            }
            String string = sb.toString();
            LogUtil.i(ConfigCore.TAG, "ConfigCore [okhttpCallback] [onResponse] \u8bf7\u6c42\u5185\u5bb9=" + string);
            if (TextUtils.isEmpty(string)) {
                return;
            }
            LogUtil.i(ConfigCore.TAG, "ConfigCore [okhttpCallback] [onResponse] \u4e0b\u8f7d\u914d\u7f6e\u6587\u4ef6\uff0c\u89e3\u6790 mIsFirst=" + ConfigCore.this.mIsFirst);
            if (ConfigCore.this.mIsFirst) {
                zUpdateLoaclConfigFile = ConfigProxy.getInstances().getmConfigParams().parse(string, true);
                LogUtil.i(ConfigCore.TAG, "ConfigCore [okhttpCallback] [onResponse] \u914d\u7f6e\u6587\u4ef6\u5185\u5bb9=" + ConfigProxy.getInstances().getmConfigParams().toString());
                if (zUpdateLoaclConfigFile) {
                    zUpdateLoaclConfigFile = ConfigCore.this.updateLoaclConfigFile(string);
                }
            } else {
                LogUtil.i(ConfigCore.TAG, "ConfigCore [okhttpCallback] [onResponse] \u5355\u72ec\u66f4\u65b0\u914d\u7f6e\u6587\u4ef6");
                zUpdateLoaclConfigFile = ConfigCore.this.updateLoaclConfigFile(string);
            }
            inputStreamByteStream.close();
            inputStreamReader.close();
            bufferedReader.close();
            if (zUpdateLoaclConfigFile) {
                return;
            }
            TaskHandleOp.getInstance().getTaskHandle().setStatus(16);
            throw new IOException();
        }

        @Override // com.netease.ntunisdk.okhttp3.Callback
        public void onFailure(Call call, IOException iOException) {
            LogUtil.stepLog("ConfigCore [okhttpCallback] [onFailure] start");
            if (call == null) {
                return;
            }
            LogUtil.i(ConfigCore.TAG, "ConfigCore [okhttpCallback] [onFailure] Call Header=" + call.request().headers().toMultimap().toString() + ", request = " + call.request().toString());
            Util.getDomainFromUrl(call.request().url().toString());
        }
    }

    public static String replaceBlank(String str, String str2, String str3) {
        return str != null ? Pattern.compile(str2).matcher(str).replaceAll(str3) : "";
    }

    public boolean updateLoaclConfigFile(String str) throws Throwable {
        String str2 = new String(Base64.decode(str.getBytes(), 0));
        LogUtil.i(TAG, "ConfigCore [updateLoaclConfigFile] \u66ff\u6362\u524d\uff0c\u914d\u7f6e\u6587\u4ef6\u5185\u5bb9=".concat(str2));
        String strReplaceBlank = replaceBlank(replaceBlank(str2, "\\s*|\t|\r|\n", ""), "\\\\\"", "\"");
        String projectId = DownloadInitInfo.getInstances().getProjectId();
        if (!TextUtils.isEmpty(projectId)) {
            strReplaceBlank = strReplaceBlank.replaceAll(projectId, "<\\$gameid>");
        }
        LogUtil.i(TAG, "ConfigCore [updateLoaclConfigFile] \u66ff\u6362\u540e\uff0c\u914d\u7f6e\u6587\u4ef6\u5185\u5bb9=" + strReplaceBlank);
        try {
            JSONObject jSONObject = new JSONObject(strReplaceBlank);
            StringBuilder sb = new StringBuilder();
            DownloadProxy.getInstance();
            sb.append(DownloadProxy.mContext.getFilesDir().getAbsolutePath());
            sb.append("/download_config.txt");
            String strFile2Info = Util.file2Info(sb.toString());
            if (TextUtils.isEmpty(strFile2Info)) {
                return false;
            }
            LogUtil.i(TAG, "ConfigCore [updateLoaclConfigFile] \u914d\u7f6e\u6587\u4ef6\u5185\u5bb9 config=" + strFile2Info);
            String overSea = TaskHandleOp.getInstance().getTaskHandle().getOverSea();
            LogUtil.i(TAG, "ConfigCore [updateLoaclConfigFile] oversea = " + overSea);
            try {
                if (!TextUtils.isEmpty(strFile2Info)) {
                    JSONObject jSONObject2 = new JSONObject(strFile2Info);
                    if ("2".equals(overSea)) {
                        if (jSONObject2.has("taiwan")) {
                            jSONObject2.put("taiwan", jSONObject);
                        }
                    } else if (jSONObject2.has("guonei")) {
                        jSONObject2.put("guonei", jSONObject);
                    }
                    LogUtil.i(TAG, "ConfigCore [updateLoaclConfigFile] \u975e\u9996\u6b21\u4e0b\u8f7d\u914d\u7f6e\u6587\u4ef6\uff0c\u5199\u5165\u5230\u672c\u5730\u914d\u7f6e\u6587\u4ef6");
                    StringBuilder sb2 = new StringBuilder();
                    DownloadProxy.getInstance();
                    sb2.append(DownloadProxy.mContext.getFilesDir().getAbsolutePath());
                    sb2.append("/download_config.txt");
                    Util.info2File(sb2.toString(), jSONObject2.toString(), true);
                    LogUtil.i(TAG, "ConfigCore [updateLoaclConfigFile] \u975e\u9996\u6b21\u4e0b\u8f7d\u914d\u7f6e\u6587\u4ef6\uff0c\u5199\u5165\u5230\u672c\u5730\u914d\u7f6e\u6587\u4ef6\uff0c\u5199\u5165\u5185\u5bb9 = " + jSONObject2.toString());
                }
                return true;
            } catch (Exception e) {
                LogUtil.i(TAG, "ConfigCore [updateLoaclConfigFile] Exception = " + e);
                return false;
            }
        } catch (Exception e2) {
            LogUtil.i(TAG, "ConfigCore [updateLoaclConfigFile] Exception = " + e2);
            return false;
        }
    }

    public int start(Context context, String str, String str2, boolean z) {
        this.mLogData.put("lvsip", Constants.CASEFIRST_FALSE);
        this.mIsFirst = z;
        return downloadConfig(context, str, str2, z);
    }

    private int downloadConfig(Context context, String str, String str2, boolean z) {
        String strReplaceDomain;
        LogUtil.stepLog("ConfigCore [downloadConfig] \u4e0b\u8f7d\u914d\u7f6e\u6587\u4ef6");
        this.mLogData.put("state", "start");
        this.mLogData.put("filetype", "CFG");
        LogUtil.i(TAG, "ConfigCore [downloadConfig] \u63a5\u5165\u65b9\u8bbe\u7f6e\u7684config=" + TaskHandleOp.getInstance().getTaskHandle().getConfigurl());
        if (!TextUtils.isEmpty(TaskHandleOp.getInstance().getTaskHandle().getConfigurl())) {
            strReplaceDomain = TaskHandleOp.getInstance().getTaskHandle().getConfigurl();
        } else {
            strReplaceDomain = String.format(Const.URL_CONFIG_FORMAT, str);
            String overSea = TaskHandleOp.getInstance().getTaskHandle().getOverSea();
            LogUtil.i(TAG, "ConfigCore [downloadConfig] oversea=" + overSea);
            if (!TextUtils.isEmpty(overSea) && "2".equals(overSea)) {
                strReplaceDomain = Util.replaceDomain(strReplaceDomain, new String[]{"netease.com", "163.com"}, "easebar.com");
            }
        }
        LogUtil.i(TAG, "ConfigCore [downloadConfig] configUrl=" + strReplaceDomain);
        String domainFromUrl = Util.getDomainFromUrl(strReplaceDomain);
        new HashMap();
        if (!TextUtils.isEmpty(str2)) {
            LogUtil.i(TAG, "ipAddr=" + str2);
            String strReplaceDomainWithIpAddr = Util.replaceDomainWithIpAddr(strReplaceDomain, str2, "/");
            if (Util.isIpv6(str2)) {
                strReplaceDomain = Util.replaceDomainWithIpAddr(strReplaceDomainWithIpAddr, com.xiaomi.gamecenter.sdk.robust.Constants.C + str2 + "]", "/");
            } else {
                strReplaceDomain = Util.replaceDomainWithIpAddr(strReplaceDomainWithIpAddr, str2, "/");
            }
            this.mHost = domainFromUrl;
        }
        Request.Builder builderUrl = new Request.Builder().url(strReplaceDomain);
        builderUrl.addHeader("Host", this.mHost);
        LogUtil.i(TAG, "ConfigCore [downloadConfig] \u8bf7\u6c42\u94fe\u63a5=" + strReplaceDomain + "\uff0c\u57df\u540d=" + domainFromUrl);
        if (z) {
            builderUrl.addHeader(Const.KEY_CONNECT_TIMEOUT_TIME, "5000");
            builderUrl.addHeader(Const.KEY_READ_TIMEOUT_TIME, "5000");
        } else {
            builderUrl.addHeader(Const.KEY_CONNECT_TIMEOUT_TIME, "15000");
            builderUrl.addHeader(Const.KEY_READ_TIMEOUT_TIME, "15000");
        }
        try {
            if (TextUtils.isEmpty(str2)) {
                return 11;
            }
            LogUtil.i(TAG, "[ORBIT] Config Refresh url='" + strReplaceDomain + "'");
            int iExecute_syn = OkHttpProxy.getInstance().execute_syn(builderUrl, this.okhttpCallback);
            LogUtil.i(TAG, "ConfigCore [downloadConfig] \u4e0b\u8f7d\u7ed3\u679c=" + iExecute_syn + "\uff0c\u8bf7\u6c42\u94fe\u63a5=" + strReplaceDomain);
            return iExecute_syn;
        } catch (Exception e) {
            LogUtil.i(TAG, "ConfigCore2 [downloadConfig] Exception = " + e.toString());
            e.printStackTrace();
            return 11;
        }
    }
}