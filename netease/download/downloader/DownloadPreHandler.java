package com.netease.download.downloader;

import android.content.Context;
import com.netease.download.Const;
import com.netease.download.config.ConfigParams;
import com.netease.download.config.ConfigProxy;
import com.netease.download.dns.CdnIpController;
import com.netease.download.dns.DnsCore;
import com.netease.download.dns.DnsParams;
import com.netease.download.httpdns.HttpdnsProxy;
import com.netease.download.listener.DownloadListenerProxy;
import com.netease.download.reporter.ReportUtil;
import com.netease.download.util.LogUtil;
import com.netease.download.util.SpUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/* loaded from: classes3.dex */
public class DownloadPreHandler implements Callable<Integer> {
    private static final String TAG = "Pre";
    private static DownloadPreHandler sPre;
    private Context mContext;
    private String mOverSea;
    private String mProjectId;

    private DownloadPreHandler() {
    }

    public static synchronized DownloadPreHandler getInstatnces() {
        if (sPre == null) {
            sPre = new DownloadPreHandler();
        }
        return sPre;
    }

    public void init(Context context, String str) {
        LogUtil.i(TAG, "Pre [init] \u9884\u5904\u7406---\u521d\u59cb\u5316---\u5f00\u59cb");
        this.mProjectId = str;
        this.mContext = context;
        SpUtil.initialize(context);
        LogUtil.i(TAG, "Pre [init] \u9884\u5904\u7406---\u521d\u59cb\u5316---\u7ed3\u675f");
    }

    public int start() {
        LogUtil.i(TAG, "Pre [start] \u9884\u5904\u7406---\u5f00\u59cb");
        ExecutorService executorServiceNewSingleThreadExecutor = Executors.newSingleThreadExecutor();
        ArrayList arrayList = new ArrayList();
        arrayList.add(executorServiceNewSingleThreadExecutor.submit(sPre));
        Iterator it = arrayList.iterator();
        int iIntValue = 11;
        while (it.hasNext()) {
            try {
                iIntValue = ((Integer) ((Future) it.next()).get()).intValue();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e2) {
                e2.printStackTrace();
            }
        }
        LogUtil.i(TAG, "Pre [start] \u9884\u5904\u7406---\u5f00\u59cb\uff0c\u7ed3\u679c=" + iIntValue);
        return iIntValue;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.concurrent.Callable
    public Integer call() throws Exception {
        ConfigProxy.getInstances().init(this.mContext, this.mProjectId);
        TaskHandleOp.getInstance().getTaskHandle().setTimeToStartDownloadConfig(System.currentTimeMillis());
        int iStart = ConfigProxy.getInstances().start();
        TaskHandleOp.getInstance().getTaskHandle().setTimeToFinishDownloadConfig(System.currentTimeMillis());
        DownloadListenerProxy.getInstances();
        DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsg(iStart, 0L, 0L, "__DOWNLOAD_CONFIG__", "__DOWNLOAD_CONFIG__", "".getBytes(), TaskHandleOp.getInstance().getTaskHandle().getSessionid(), TaskHandleOp.getInstance().getTaskHandle().getNtesOrbitId());
        ConfigParams configParams = ConfigProxy.getInstances().getmConfigParams();
        if (configParams != null) {
            LogUtil.i(TAG, "[QAQA] Pre [call] \u9884\u5904\u7406\uff0c\u914d\u7f6e\u6587\u4ef6\u7ed3\u679c=" + configParams.toString());
            ReportUtil.getInstances().getQuery();
            Map<String, ConfigParams.CdnUnit> map = configParams.getmCdnMap();
            if (map != null && map.size() > 0) {
                DnsCore.getInstances().init(configParams.getmCdnMap());
                ArrayList<DnsParams.Unit> arrayListStart = DnsCore.getInstances().start();
                LogUtil.i(TAG, "Pre [call] \u9884\u5904\u7406\uff0cDNS\u7ed3\u679c=" + arrayListStart);
                StringBuffer stringBuffer = new StringBuffer("[ORBIT] DNS Result ");
                Iterator<DnsParams.Unit> it = arrayListStart.iterator();
                while (it.hasNext()) {
                    DnsParams.Unit next = it.next();
                    stringBuffer.append(next.domain);
                    stringBuffer.append("=");
                    if (next.ipArrayList != null) {
                        stringBuffer.append(next.ipArrayList.toString());
                    }
                    stringBuffer.append(" ");
                }
                LogUtil.i(TAG, stringBuffer.toString());
                if (arrayListStart != null && arrayListStart.size() > 0) {
                    DownloadListenerProxy.getInstances();
                    DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsg(0, 0L, 0L, "__DOWNLOAD_DNS_RESOLVED__", "__DOWNLOAD_DNS_RESOLVED__", "".getBytes(), TaskHandleOp.getInstance().getTaskHandle().getSessionid(), TaskHandleOp.getInstance().getTaskHandle().getNtesOrbitId());
                } else {
                    DownloadListenerProxy.getInstances();
                    DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsg(11, 0L, 0L, "__DOWNLOAD_DNS_RESOLVED__", "__DOWNLOAD_DNS_RESOLVED__", "".getBytes(), TaskHandleOp.getInstance().getTaskHandle().getSessionid(), TaskHandleOp.getInstance().getTaskHandle().getNtesOrbitId());
                }
                if (arrayListStart == null || arrayListStart.size() <= 0) {
                    LogUtil.i(TAG, "Pre [call] \u9884\u5904\u7406\uff0cDNS\u89e3\u6790\u5931\u8d25\uff0c\u8fdb\u5165Httpdns\u89e3\u6790\u6d41\u7a0b");
                    HttpdnsProxy.getInstances().synStart(Const.HTTPDNS_CONFIG_CND, configParams.getmCdnMap());
                    if (HttpdnsProxy.getInstances().getHttpdnsUrlSwitcherCore(Const.HTTPDNS_CONFIG_CND) != null) {
                        LogUtil.i(TAG, "Pre [call] \u9884\u5904\u7406\uff0cHttpdns\u7ed3\u679c=" + HttpdnsProxy.getInstances().getHttpdnsUrlSwitcherCore(Const.HTTPDNS_CONFIG_CND).toString());
                    } else {
                        LogUtil.i(TAG, "Pre [call] \u9884\u5904\u7406\uff0cHttpdns\u7ed3\u679c\u4e3anull");
                    }
                }
                LogUtil.i(TAG, "Pre [call] DnsParams.getInstances().getDnsIpNodeUnitList()=" + arrayListStart.toString());
                LogUtil.i(TAG, "Pre [call] ConfigParams2.getInstance().getWeights()=" + configParams.getWeights());
                CdnIpController.getInstances().init(configParams.getmCdnMap());
                LogUtil.i(TAG, "Pre [call] mOriginalMap=" + CdnIpController.getInstances().mOriginalMap.toString());
                LogUtil.i(TAG, "Pre [call] DNS\u89e3\u6790\u51fa\u7684cdn ip\u7ed3\u679c\u96c6\u5408  mActualTimeMap=" + CdnIpController.getInstances().mActualTimeMap.toString());
            }
        } else {
            LogUtil.i(TAG, "[QAQA] Pre [call] \u9884\u5904\u7406\uff0c\u914d\u7f6e\u6587\u4ef6\u7ed3\u679c = null");
            TaskHandleOp.getInstance().getTaskHandle().setStatus(16);
        }
        LogUtil.i(TAG, "[QAQA] Pre [call] \u9884\u5904\u7406\uff0c\u8fd4\u56de\u503c=" + iStart);
        return Integer.valueOf(iStart);
    }
}