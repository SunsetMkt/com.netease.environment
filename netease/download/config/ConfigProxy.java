package com.netease.download.config;

import android.content.Context;
import android.text.TextUtils;
import com.netease.download.Const;
import com.netease.download.dns.DnsCore;
import com.netease.download.dns.DnsParams;
import com.netease.download.downloader.DownloadProxy;
import com.netease.download.downloader.TaskHandleOp;
import com.netease.download.listener.DownloadListenerProxy;
import com.netease.download.lvsip.Lvsip;
import com.netease.download.util.LogUtil;
import com.netease.download.util.Util;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes5.dex */
public class ConfigProxy {
    private static final String TAG = "ConfigProxy";
    private static ConfigProxy sConfigProxy;
    private ConfigParams mConfigParams;
    private String mProjeceId = null;
    private Context mContext = null;
    private boolean mNeedRefresh = false;
    private boolean mHasDownloadConfig = false;

    private ConfigProxy() {
        this.mConfigParams = null;
        this.mConfigParams = new ConfigParams();
    }

    public static ConfigProxy getInstances() {
        if (sConfigProxy == null) {
            sConfigProxy = new ConfigProxy();
        }
        return sConfigProxy;
    }

    public void init(Context context, String str) {
        this.mProjeceId = str;
        this.mContext = context;
    }

    public int start() throws Throwable {
        LogUtil.i(TAG, "ConfigProxy [start] start");
        if (TextUtils.isEmpty(this.mProjeceId) || this.mContext == null) {
            return 11;
        }
        LogUtil.i(TAG, "ConfigProxy [start] mNeedRefresh=" + this.mNeedRefresh);
        if (!this.mHasDownloadConfig || this.mNeedRefresh) {
            LogUtil.i(TAG, "ConfigProxy [start] \u4e0b\u8f7d\u914d\u7f6e\u6587\u4ef6");
            int config = getConfig();
            if (config != 0) {
                return config;
            }
            this.mHasDownloadConfig = true;
            return config;
        }
        LogUtil.i(TAG, "ConfigProxy [start] \u914d\u7f6e\u6587\u4ef6\u5df2\u7ecf\u5b58\u5728");
        return 0;
    }

    public int getConfig() throws Throwable {
        ArrayList<String> arrayList;
        LogUtil.i(TAG, "ConfigProxy [getConfig] start");
        int iStart = 11;
        if (TextUtils.isEmpty(this.mProjeceId) || this.mContext == null) {
            return 11;
        }
        if (!TextUtils.isEmpty(TaskHandleOp.getInstance().getTaskHandle().getConfigurl())) {
            LogUtil.i(TAG, "ConfigProxy [getConfig] use param configUrl");
            DnsCore.getInstances().init(TaskHandleOp.getInstance().getTaskHandle().getConfigurl());
        } else {
            LogUtil.i(TAG, "ConfigProxy [getConfig] use project");
            String overSea = TaskHandleOp.getInstance().getTaskHandle().getOverSea();
            boolean zIsEmpty = TextUtils.isEmpty(overSea);
            String strReplaceDomain = Const.URL_CONFIG_FORMAT;
            if (!zIsEmpty && "2".equals(overSea)) {
                strReplaceDomain = Util.replaceDomain(Const.URL_CONFIG_FORMAT, new String[]{"netease.com", "163.com"}, "easebar.com");
            }
            DnsCore.getInstances().init(strReplaceDomain);
        }
        ArrayList<DnsParams.Unit> arrayListStart = DnsCore.getInstances().start();
        LogUtil.i(TAG, "ConfigProxy [getConfig] \u914d\u7f6e\u6587\u4ef6\u505aDNS\u89e3\u6790\uff0cDNS\u7ed3\u679c=" + arrayListStart.toString());
        if (arrayListStart != null && arrayListStart.size() > 0) {
            DownloadListenerProxy.getInstances();
            DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsg(0, 0L, 0L, "__DOWNLOAD_DNS_RESOLVED__", "__DOWNLOAD_DNS_RESOLVED__", "".getBytes(), TaskHandleOp.getInstance().getTaskHandle().getSessionid(), TaskHandleOp.getInstance().getTaskHandle().getNtesOrbitId());
        } else {
            DownloadListenerProxy.getInstances();
            DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsg(11, 0L, 0L, "__DOWNLOAD_DNS_RESOLVED__", "__DOWNLOAD_DNS_RESOLVED__", "".getBytes(), TaskHandleOp.getInstance().getTaskHandle().getSessionid(), TaskHandleOp.getInstance().getTaskHandle().getNtesOrbitId());
        }
        String string = ConfigParams.getDefaultConfing().toString();
        LogUtil.i(TAG, "ConfigProxy [getConfig] \u9ed8\u8ba4\u914d\u7f6e\u6587\u4ef6\u5185\u5bb9\uff0c\u5199\u5165\u5230\u672c\u5730\u914d\u7f6e\u6587\u4ef6=" + string);
        StringBuilder sb = new StringBuilder();
        DownloadProxy.getInstance();
        sb.append(DownloadProxy.mContext.getFilesDir().getAbsolutePath());
        sb.append("/download_config.txt");
        Util.info2File(sb.toString(), string, false);
        ConfigCore configCore = new ConfigCore();
        if (arrayListStart != null && arrayListStart.size() > 0 && (arrayList = arrayListStart.get(0).ipArrayList) != null && arrayList.size() > 0) {
            String str = arrayList.get(0);
            LogUtil.i(TAG, "ConfigProxy [getConfig] dnsIpNodeUnitList" + arrayListStart.toString() + ", ip=" + str);
            if (!TextUtils.isEmpty(str)) {
                LogUtil.i(TAG, "ConfigProxy [getConfig] \u4e0b\u8f7d\u914d\u7f6e\u6587\u4ef6=" + string);
                iStart = configCore.start(this.mContext, this.mProjeceId, str, true);
            }
        }
        if (iStart != 0) {
            LogUtil.i(TAG, "ConfigProxy [getConfig] \u5f00\u542f\u65b0\u7ebf\u7a0b\uff0c\u4e0b\u8f7d\u914d\u7f6e\u6587\u4ef6");
            downloadConfig(this.mContext, this.mProjeceId, arrayListStart);
        }
        if (iStart == 0) {
            return iStart;
        }
        LogUtil.i(TAG, "ConfigProxy [getConfig] \u91c7\u7528\u672c\u5730\u914d\u7f6e\u6587\u4ef6");
        return useLocalFileConfig();
    }

    private void downloadConfig(final Context context, final String str, final ArrayList<DnsParams.Unit> arrayList) {
        LogUtil.i(TAG, "ConfigProxy [downloadConfig] start");
        new Thread(new Runnable() { // from class: com.netease.download.config.ConfigProxy.1
            @Override // java.lang.Runnable
            public void run() {
                ConfigCore configCore = new ConfigCore();
                ArrayList arrayList2 = arrayList;
                int iStart = 11;
                if (arrayList2 != null && arrayList2.size() > 0) {
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        Iterator<String> it2 = ((DnsParams.Unit) it.next()).ipArrayList.iterator();
                        while (it2.hasNext()) {
                            String next = it2.next();
                            LogUtil.i(ConfigProxy.TAG, "ConfigProxy [downloadConfig] dnsIpNodeUnitList=" + arrayList.toString() + ", ip=" + next);
                            iStart = configCore.start(context, str, next, false);
                            if (iStart == 0) {
                                break;
                            }
                        }
                        if (iStart == 0) {
                            break;
                        }
                    }
                }
                LogUtil.stepLog("ConfigProxy [downloadConfig] \u8bf7\u6c42\u914d\u7f6e\u6587\u4ef6\uff0c\u91c7\u7528dns\u8bf7\u6c42\uff0c\u8bf7\u6c42\u7ed3\u679c=" + iStart);
                if (iStart != 0) {
                    LogUtil.stepLog("ConfigProxy [downloadConfig] \u8bf7\u6c42\u914d\u7f6e\u6587\u4ef6\uff0c\u91c7\u7528lvsip, \u662f\u5426\u521b\u5efa\u8fc7ip=" + Lvsip.getInstance().isCteateIp());
                    if (!Lvsip.getInstance().isCteateIp()) {
                        String overSea = TaskHandleOp.getInstance().getTaskHandle().getOverSea();
                        LogUtil.i(ConfigProxy.TAG, "ConfigProxy [downloadConfig] \u6d77\u5916=" + overSea);
                        if ("1".equals(overSea) || "2".equals(overSea)) {
                            Lvsip.getInstance().init(Const.REQ_IPS_WS_OVERSEA);
                        } else if ("0".equals(overSea) || "-1".equals(overSea)) {
                            Lvsip.getInstance().init(Const.REQ_IPS_WS_CHINA);
                        } else {
                            Lvsip.getInstance().init(Const.REQ_IPS_WS);
                        }
                        Lvsip.getInstance().createLvsip();
                    }
                    while (Lvsip.getInstance().hasNext() && iStart != 0) {
                        String newIpFromArray = Lvsip.getInstance().getNewIpFromArray();
                        LogUtil.i(ConfigProxy.TAG, "ConfigProxy [downloadConfig] \u8bf7\u6c42\u914d\u7f6e\u6587\u4ef6\u73af\u8282--\u91c7\u7528lvsip\uff0c\u5c06\u8981\u4f7f\u7528\u7684ip=" + newIpFromArray);
                        if (!TextUtils.isEmpty(newIpFromArray)) {
                            int iStart2 = configCore.start(context, str, newIpFromArray, false);
                            if (iStart2 == 0) {
                                return;
                            } else {
                                iStart = iStart2;
                            }
                        }
                    }
                }
            }
        }).start();
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0092  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int useLocalFileConfig() throws org.json.JSONException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 280
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.download.config.ConfigProxy.useLocalFileConfig():int");
    }

    public ConfigParams getmConfigParams() {
        return this.mConfigParams;
    }

    public void setmConfigParams(ConfigParams configParams) {
        this.mConfigParams = configParams;
    }

    public boolean isNeedRefresh() {
        return this.mNeedRefresh;
    }

    public void setNeedRefresh(String str) {
        this.mNeedRefresh = false;
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if ("true".equals(str) || "1".equals(str)) {
            this.mNeedRefresh = true;
        }
    }
}