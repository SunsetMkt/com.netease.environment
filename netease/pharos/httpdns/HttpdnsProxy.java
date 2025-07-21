package com.netease.pharos.httpdns;

import android.text.TextUtils;
import com.netease.pharos.httpdns.HttpdnsUrlSwitcherCore;
import com.netease.pharos.threadManager.ThreadPoolManager;
import com.netease.pharos.util.LogUtil;
import com.netease.pharos.util.Util;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/* loaded from: classes5.dex */
public class HttpdnsProxy {
    private static final String TAG = "HttpdnsProxy";
    private static HttpdnsProxy sHttpdnsProxy;
    private final boolean mHttpdnsResolved = false;

    private HttpdnsProxy() {
    }

    public static HttpdnsProxy getInstances() {
        if (sHttpdnsProxy == null) {
            synchronized (HttpdnsProxy.class) {
                if (sHttpdnsProxy == null) {
                    sHttpdnsProxy = new HttpdnsProxy();
                }
            }
        }
        return sHttpdnsProxy;
    }

    public synchronized void synStart(String str, String[] strArr) {
        if (getInstances().getHttpdnsUrlSwitcherCore(str) == null) {
            LogUtil.stepLog("Httpdns\u73af\u8282--\u5f00\u59cbhttpdns\u6d41\u7a0b");
            getInstances().start(str, strArr);
            LogUtil.stepLog("Httpdns\u73af\u8282--\u7ed3\u675fhttpdns\u6d41\u7a0b");
        } else {
            LogUtil.i(TAG, "Httpdns\u73af\u8282--" + str + " \u5df2\u7ecf\u8bf7\u6c42\u8fc7httpdns");
        }
    }

    private int start(String str, String[] strArr) {
        if (!Util.isZoneEast8()) {
            LogUtil.i(TAG, "Httpdns\u73af\u8282--\u4e0d\u5728\u4e1c\u516b\u533a");
            return 17;
        }
        if (TextUtils.isEmpty(str) || strArr == null || strArr.length <= 0) {
            LogUtil.i(TAG, "Httpdns\u73af\u8282--\u53c2\u6570\u9519\u8bef");
            return 14;
        }
        LogUtil.i(TAG, "Httpdns\u73af\u8282--\u8bf7\u6c42SA\u81ea\u5efa\u7684Htttpdns\u670d\u52a1\u5668ip\uff0c\u8bf7\u6c42\u8fd4\u56de\u503c=11");
        LogUtil.i(TAG, "===============================================");
        ArrayList arrayList = new ArrayList();
        LogUtil.stepLog("Httpdns\u73af\u8282--\u901a\u8fc7Httpdns\u89e3\u6790\u57df\u540d");
        for (int i = 0; i < strArr.length; i++) {
            LogUtil.i(TAG, "Httpdns\u73af\u8282-- i=" + i + ", \u57df\u540d=" + strArr[i]);
            HttpdnsDomain2IpCore httpdnsDomain2IpCore = new HttpdnsDomain2IpCore();
            httpdnsDomain2IpCore.init(strArr[i]);
            try {
                arrayList.add(ThreadPoolManager.getInstance().getFixedThreadPool().submit(httpdnsDomain2IpCore));
            } catch (Exception unused) {
            }
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            try {
                LogUtil.i(TAG, "Httpdns\u73af\u8282--\u8bf7\u6c42httpdns\u670d\u52a1\u5668\uff0c\u89e3\u6790\u57df\u540d\uff0c\u83b7\u53d6\u7ed3\u679c=" + ((Future) it.next()).get());
            } catch (InterruptedException e) {
                LogUtil.i(TAG, "Httpdns\u73af\u8282--\u8bf7\u6c42httpdns\u670d\u52a1\u5668\uff0c\u89e3\u6790\u57df\u540d InterruptedException=" + e);
            } catch (ExecutionException e2) {
                LogUtil.i(TAG, "Httpdns\u73af\u8282--\u8bf7\u6c42httpdns\u670d\u52a1\u5668\uff0c\u89e3\u6790\u57df\u540d ExecutionException=" + e2);
            } catch (Exception e3) {
                LogUtil.i(TAG, "Httpdns\u73af\u8282--\u8bf7\u6c42httpdns\u670d\u52a1\u5668\uff0c\u89e3\u6790\u57df\u540d Exception=" + e3);
            }
        }
        LogUtil.i(TAG, "Httpdns\u73af\u8282--\u901a\u8fc7Httpdns\u89e3\u6790\u57df\u540d, \u89e3\u6790\u8fd4\u56de\u503c=11");
        try {
            LogUtil.i(TAG, "Httpdns\u73af\u8282--\u7ed3\u679c\u6570\u636e\uff0chttpdns\u89e3\u6790\u57df\u540d\u83b7\u53d6ip\u6570\u636e=" + HttpdnsDomain2IpParams.getInstances().getHttpdnsDomain2IpUnitList().toString());
        } catch (Exception e4) {
            LogUtil.i(TAG, "Httpdns\u73af\u8282--\u901a\u8fc7Httpdns\u89e3\u6790\u57df\u540d, \u89e3\u6790\u8fd4\u56de\u503c Exception=" + e4);
        }
        HttpdnsUrlSwitcherCore.getInstances().init(str, HttpdnsDomain2IpParams.getInstances().getHttpdnsDomain2IpUnitList());
        return 11;
    }

    public HttpdnsUrlSwitcherCore.KeyHttpdnsUrlSwitcherCoreUnit getHttpdnsUrlSwitcherCore(String str) {
        if (HttpdnsUrlSwitcherCore.getInstances().mHttpdnsUrlUnitMap.containsKey(str)) {
            return HttpdnsUrlSwitcherCore.getInstances().mHttpdnsUrlUnitMap.get(str);
        }
        return null;
    }
}