package com.netease.download.httpdns;

import android.text.TextUtils;
import com.netease.download.UrlSwitcher.HttpdnsUrlSwitcherCore;
import com.netease.download.config.ConfigParams;
import com.netease.download.downloader.TaskHandleOp;
import com.netease.download.httpdns.ServicesNodeParams;
import com.netease.download.taskManager.TaskExecutor;
import com.netease.download.util.LogUtil;
import com.netease.download.util.Util;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/* loaded from: classes5.dex */
public class HttpdnsProxy {
    private static final String TAG = "HttpdnsProxy";
    private static HttpdnsProxy sHttpdnsProxy;
    private boolean mHttpdnsResolved = false;

    private HttpdnsProxy() {
    }

    public static HttpdnsProxy getInstances() {
        if (sHttpdnsProxy == null) {
            sHttpdnsProxy = new HttpdnsProxy();
        }
        return sHttpdnsProxy;
    }

    public synchronized void synStart(String str, Map<String, ConfigParams.CdnUnit> map) {
        ArrayList<ConfigParams.CdnUrlUnit> arrayList;
        if (map != null) {
            if (map.size() > 0) {
                ArrayList<String> arrayList2 = new ArrayList<>();
                Iterator<String> it = map.keySet().iterator();
                while (it.hasNext()) {
                    ConfigParams.CdnUnit cdnUnit = map.get(it.next());
                    if (cdnUnit != null && (arrayList = cdnUnit.getmCdnList()) != null && arrayList.size() > 0) {
                        Iterator<ConfigParams.CdnUrlUnit> it2 = arrayList.iterator();
                        while (it2.hasNext()) {
                            String str2 = it2.next().getmUrl();
                            if (!TextUtils.isEmpty(str2)) {
                                arrayList2.add(str2);
                            }
                        }
                    }
                }
                synStart(str, arrayList2);
                return;
            }
        }
        LogUtil.i(TAG, "HttpdnsProxy [start] param error");
    }

    public synchronized void synStart(String str, String[] strArr) {
        if (strArr != null) {
            if (strArr.length > 0) {
                ArrayList<String> arrayList = new ArrayList<>();
                for (String str2 : strArr) {
                    LogUtil.i(TAG, "HttpdnsProxy [start] domain=" + str2);
                    arrayList.add(str2);
                }
                synStart(str, arrayList);
                return;
            }
        }
        LogUtil.i(TAG, "HttpdnsProxy [start] param error");
    }

    public synchronized void synStart(String str, ArrayList<String> arrayList) {
        if (getInstances().getHttpdnsUrlSwitcherCore(str) == null) {
            TaskHandleOp.getInstance().getTaskHandle().setTimeToStartHTTPDNS(System.currentTimeMillis());
            LogUtil.stepLog("Httpdns\u73af\u8282--\u5f00\u59cbhttpdns\u6d41\u7a0b");
            getInstances().start(str, arrayList);
            TaskHandleOp.getInstance().getTaskHandle().setIsHttpdns(true);
            LogUtil.stepLog("Httpdns\u73af\u8282--\u7ed3\u675fhttpdns\u6d41\u7a0b");
            TaskHandleOp.getInstance().getTaskHandle().setTimeToFinishHTTPDNS(System.currentTimeMillis());
        } else {
            LogUtil.i(TAG, "Httpdns\u73af\u8282--" + str + " \u5df2\u7ecf\u8bf7\u6c42\u8fc7httpdns");
        }
    }

    private int start(String str, ArrayList<String> arrayList) {
        ServicesNodeParams.HttpdnsServicesUnit httpdnsServicesUnit;
        if (TextUtils.isEmpty(str) || arrayList == null || arrayList.size() <= 0) {
            LogUtil.i(TAG, "Httpdns\u73af\u8282--\u53c2\u6570\u9519\u8bef");
            return 14;
        }
        ServicesNodeCore servicesNodeCore = new ServicesNodeCore();
        servicesNodeCore.init();
        int iStart = servicesNodeCore.start();
        LogUtil.i(TAG, "Httpdns\u73af\u8282--\u8bf7\u6c42SA\u81ea\u5efa\u7684Htttpdns\u670d\u52a1\u5668ip\uff0c\u8bf7\u6c42\u8fd4\u56de\u503c=" + iStart);
        LogUtil.i(TAG, "===============================================");
        ArrayList arrayList2 = new ArrayList();
        if (iStart == 0) {
            LogUtil.stepLog("Httpdns\u73af\u8282--\u901a\u8fc7Httpdns\u89e3\u6790\u57df\u540d");
            ServicesNodeParams.getInstances().getHttpdnsServicesUnitList();
            String overSea = TaskHandleOp.getInstance().getTaskHandle().getOverSea();
            if ("1".equals(overSea) || "2".equals(overSea)) {
                LogUtil.i(TAG, "Httpdns\u73af\u8282--\u6d77\u5916");
                httpdnsServicesUnit = ServicesNodeParams.getInstances().get("oversea");
            } else {
                LogUtil.i(TAG, "Httpdns\u73af\u8282--\u5927\u9646");
                httpdnsServicesUnit = ServicesNodeParams.getInstances().get("mainland");
            }
            for (int i = 0; i < arrayList.size(); i++) {
                LogUtil.i(TAG, "Httpdns\u73af\u8282-- i=" + i + ", unit=" + httpdnsServicesUnit.toString() + ", \u57df\u540d=" + arrayList.get(i));
                String str2 = arrayList.get(i);
                StringBuilder sb = new StringBuilder("Httpdns\u73af\u8282--\u6700\u7ec8\u521d\u59cb\u5316\u4f7f\u7528\u57df\u540d=");
                sb.append(str2);
                LogUtil.i(TAG, sb.toString());
                HttpdnsDomain2IpCore httpdnsDomain2IpCore = new HttpdnsDomain2IpCore();
                httpdnsDomain2IpCore.init(httpdnsServicesUnit, str2);
                arrayList2.add(TaskExecutor.getInstance().getFixedThreadPool().submit(httpdnsDomain2IpCore));
            }
            Iterator it = arrayList2.iterator();
            while (it.hasNext()) {
                try {
                    LogUtil.i(TAG, "Httpdns\u73af\u8282--\u8bf7\u6c42httpdns\u670d\u52a1\u5668\uff0c\u89e3\u6790\u57df\u540d\uff0c\u83b7\u53d6\u7ed3\u679c=" + ((Future) it.next()).get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e2) {
                    e2.printStackTrace();
                }
            }
            LogUtil.i(TAG, "Httpdns\u73af\u8282--\u901a\u8fc7Httpdns\u89e3\u6790\u57df\u540d, \u89e3\u6790\u8fd4\u56de\u503c=" + iStart);
            LogUtil.i(TAG, "Httpdns\u73af\u8282--\u7ed3\u679c\u6570\u636e\uff0chttpdns\u89e3\u6790\u57df\u540d\u83b7\u53d6ip\u6570\u636e=" + HttpdnsDomain2IpParams.getInstances().getHttpdnsDomain2IpUnitList().toString());
            HttpdnsUrlSwitcherCore.getInstances().init(str, HttpdnsDomain2IpParams.getInstances().getHttpdnsDomain2IpUnitList());
        }
        if (TaskHandleOp.getInstance().getTaskHandle().isAutoFree()) {
            LogUtil.i(TAG, "Httpdns\u73af\u8282  freeThreadPool");
            TaskExecutor.getInstance().closeFixedThreadPool();
        }
        return iStart;
    }

    public boolean containKey(String str) {
        return HttpdnsUrlSwitcherCore.getInstances().mHttpdnsUrlUnitMap.containsKey(str);
    }

    public void removeKey(String str) {
        if (HttpdnsUrlSwitcherCore.getInstances().mHttpdnsUrlUnitMap.containsKey(str)) {
            HttpdnsUrlSwitcherCore.getInstances().mHttpdnsUrlUnitMap.remove(str);
        }
    }

    public HttpdnsUrlSwitcherCore.KeyHttpdnsUrlSwitcherCoreUnit getHttpdnsUrlSwitcherCore(String str) {
        if (HttpdnsUrlSwitcherCore.getInstances().mHttpdnsUrlUnitMap.containsKey(str)) {
            return HttpdnsUrlSwitcherCore.getInstances().mHttpdnsUrlUnitMap.get(str);
        }
        return null;
    }

    public boolean hasNext(String str) {
        boolean zHasNext;
        HttpdnsUrlSwitcherCore.KeyHttpdnsUrlSwitcherCoreUnit httpdnsUrlSwitcherCore;
        LogUtil.i(TAG, "HttpdnsProxy [hasNext] start");
        LogUtil.i(TAG, "HttpdnsProxy [hasNext] identify=" + str);
        if (TextUtils.isEmpty(str) || (httpdnsUrlSwitcherCore = getHttpdnsUrlSwitcherCore(str)) == null) {
            zHasNext = false;
        } else {
            LogUtil.i(TAG, "HttpdnsProxy [hasNext] keyHttpdnsUrlSwitcherCoreUnit=" + httpdnsUrlSwitcherCore.toString());
            zHasNext = httpdnsUrlSwitcherCore.hasNext();
        }
        LogUtil.i(TAG, "HttpdnsProxy [hasNext] result=" + zHasNext);
        return zHasNext;
    }

    public boolean isLast(String str, String str2) {
        int i;
        HttpdnsUrlSwitcherCore.KeyHttpdnsUrlSwitcherCoreUnit httpdnsUrlSwitcherCore;
        if (TextUtils.isEmpty(str) || (httpdnsUrlSwitcherCore = getHttpdnsUrlSwitcherCore(str)) == null) {
            i = 0;
        } else {
            Iterator<HttpdnsUrlSwitcherCore.HttpdnsUrlSwitcherCoreUnit> it = httpdnsUrlSwitcherCore.mHttpdnsUrlUnitList.iterator();
            i = 0;
            while (it.hasNext()) {
                if (str2.equals(Util.getCdnChannel(it.next().host))) {
                    i++;
                }
            }
        }
        return i == 1;
    }

    public void clean() {
        HttpdnsUrlSwitcherCore.getInstances().mHttpdnsUrlUnitMap.clear();
    }

    public HttpdnsUrlSwitcherCore.HttpdnsUrlSwitcherCoreUnit next(String str, String str2) {
        HttpdnsUrlSwitcherCore.KeyHttpdnsUrlSwitcherCoreUnit httpdnsUrlSwitcherCore;
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str) || (httpdnsUrlSwitcherCore = getHttpdnsUrlSwitcherCore(str)) == null || httpdnsUrlSwitcherCore.mHttpdnsUrlUnitList.size() <= 0) {
            return null;
        }
        return httpdnsUrlSwitcherCore.next(str2);
    }

    public void remove(String str, String str2, String str3) {
        HttpdnsUrlSwitcherCore.KeyHttpdnsUrlSwitcherCoreUnit httpdnsUrlSwitcherCore;
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || (httpdnsUrlSwitcherCore = getHttpdnsUrlSwitcherCore(str)) == null) {
            return;
        }
        for (int i = 0; i < httpdnsUrlSwitcherCore.mHttpdnsUrlUnitList.size(); i++) {
            HttpdnsUrlSwitcherCore.HttpdnsUrlSwitcherCoreUnit httpdnsUrlSwitcherCoreUnit = httpdnsUrlSwitcherCore.mHttpdnsUrlUnitList.get(i);
            String str4 = httpdnsUrlSwitcherCoreUnit.ip;
            String cdnChannel = Util.getCdnChannel(httpdnsUrlSwitcherCoreUnit.host);
            if (str4.equals(str2) && cdnChannel.equals(str3)) {
                httpdnsUrlSwitcherCore.mHttpdnsUrlUnitList.remove(i);
            }
        }
    }
}