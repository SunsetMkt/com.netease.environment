package com.netease.download.dns;

import com.netease.download.config.ConfigParams;
import com.netease.download.dns.DnsParams;
import com.netease.download.downloader.TaskHandleOp;
import com.netease.download.util.LogUtil;
import com.netease.download.util.Util;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes5.dex */
public class DnsCore {
    private static final String TAG = "DnsCore";
    private static DnsCore sDnsCore;
    private ArrayList<String> mDomainList;

    private DnsCore() {
    }

    public static DnsCore getInstances() {
        if (sDnsCore == null) {
            sDnsCore = new DnsCore();
        }
        return sDnsCore;
    }

    public void init(String[] strArr) {
        if (strArr == null || strArr.length <= 0) {
            LogUtil.w(TAG, "DnsCore [init] param error");
            return;
        }
        this.mDomainList = new ArrayList<>();
        for (String str : strArr) {
            this.mDomainList.add(str);
        }
        LogUtil.i(TAG, "DnsCore [start] mDomainList=" + this.mDomainList.toString());
    }

    public void init(String str) {
        if (str.isEmpty()) {
            LogUtil.w(TAG, "DnsCore [init] param error");
            return;
        }
        ArrayList<String> arrayList = new ArrayList<>();
        this.mDomainList = arrayList;
        arrayList.add(str);
        LogUtil.i(TAG, "DnsCore [start] mDomainList=" + this.mDomainList.toString());
    }

    public void init(Map<String, ConfigParams.CdnUnit> map) {
        if (map == null || map.size() <= 0) {
            LogUtil.w(TAG, "DnsCore [init] param error");
            return;
        }
        LogUtil.i(TAG, "DnsCore [start] cdnUnitMap=" + map.toString());
        this.mDomainList = new ArrayList<>();
        for (String str : map.keySet()) {
            LogUtil.i(TAG, "DnsCore [start] channel=" + str);
            ConfigParams.CdnUnit cdnUnit = map.get(str);
            if (cdnUnit != null) {
                LogUtil.i(TAG, "DnsCore [start] cdnUnit=" + cdnUnit.toString());
                ArrayList<ConfigParams.CdnUrlUnit> arrayList = cdnUnit.getmCdnList();
                if (arrayList != null && arrayList.size() > 0) {
                    Iterator<ConfigParams.CdnUrlUnit> it = arrayList.iterator();
                    while (it.hasNext()) {
                        ConfigParams.CdnUrlUnit next = it.next();
                        String str2 = next.getmUrl();
                        String str3 = next.getmPort();
                        this.mDomainList.add(str2 + ":" + str3);
                    }
                }
            }
        }
        LogUtil.i(TAG, "DnsCore [start] mDomainList=" + this.mDomainList.toString());
    }

    public ArrayList<DnsParams.Unit> start() {
        DnsParams dnsParams = new DnsParams();
        Iterator<String> it = this.mDomainList.iterator();
        while (it.hasNext()) {
            String next = it.next();
            LogUtil.i(TAG, "DnsCore [start] url=" + next);
            ArrayList<String> arrayList = new ArrayList<>();
            String portFromUrl = Util.getPortFromUrl(next);
            String domainFromUrl = Util.getDomainFromUrl(next);
            try {
                LogUtil.i(TAG, "DnsCore [start] domain=" + domainFromUrl + ", port=" + portFromUrl);
                long jCurrentTimeMillis = System.currentTimeMillis();
                InetAddress[] allByName = InetAddress.getAllByName(domainFromUrl);
                TaskHandleOp.getInstance().addChannelDnsCostTimeMap(domainFromUrl, System.currentTimeMillis() - jCurrentTimeMillis);
                LogUtil.i(TAG, "DnsCore [start] returnStr.length=" + allByName.length);
                for (InetAddress inetAddress : allByName) {
                    String hostAddress = inetAddress.getHostAddress();
                    LogUtil.i(TAG, "DnsCore [start] dns ip=" + hostAddress);
                    arrayList.add(hostAddress);
                    TaskHandleOp.getInstance().addChannelDnsIpMap(domainFromUrl, hostAddress);
                }
            } catch (UnknownHostException e) {
                LogUtil.i(TAG, "DnsCore [start] UnknownHostException=" + e);
            } catch (Exception e2) {
                LogUtil.i(TAG, "DnsCore [start] Exception=" + e2);
            }
            dnsParams.add(domainFromUrl, arrayList, portFromUrl);
        }
        return dnsParams.getDnsIpNodeUnitList();
    }
}