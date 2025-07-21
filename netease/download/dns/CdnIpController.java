package com.netease.download.dns;

import android.text.TextUtils;
import com.netease.download.config.ConfigParams;
import com.netease.download.dns.DnsParams;
import com.netease.download.downloader.TaskHandleOp;
import com.netease.download.util.LogUtil;
import com.netease.download.util.Util;
import com.xiaomi.gamecenter.sdk.robust.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes5.dex */
public class CdnIpController {
    private static final String TAG = "CdnIpController";
    private static CdnIpController sCndIpController;
    public HashMap<String, CndIpControllerUnit> mOriginalMap = new HashMap<>();
    public HashMap<String, CndIpControllerUnit> mActualTimeMap = new HashMap<>();

    private CdnIpController() {
    }

    public static CdnIpController getInstances() {
        if (sCndIpController == null) {
            sCndIpController = new CdnIpController();
        }
        return sCndIpController;
    }

    public void init(Map<String, ConfigParams.CdnUnit> map) {
        this.mActualTimeMap.putAll(createData(map));
        this.mOriginalMap.putAll(createData(map));
    }

    public void add(ArrayList<DnsParams.Unit> arrayList) {
        if (arrayList == null || arrayList.size() <= 0) {
            return;
        }
        for (int i = 0; i < arrayList.size(); i++) {
            DnsParams.Unit unit = arrayList.get(i);
            ArrayList<String> arrayList2 = unit.ipArrayList;
            String str = unit.domain;
            String str2 = unit.port;
            if (!this.mOriginalMap.containsKey(str)) {
                CndIpControllerUnit cndIpControllerUnit = new CndIpControllerUnit(str, arrayList2, 0, str2);
                this.mOriginalMap.put(unit.domain, cndIpControllerUnit);
                this.mActualTimeMap.put(unit.domain, cndIpControllerUnit);
            }
        }
    }

    private HashMap<String, CndIpControllerUnit> createData(Map<String, ConfigParams.CdnUnit> map) {
        HashMap<String, CndIpControllerUnit> map2 = new HashMap<>();
        if (map == null || map.size() <= 0) {
            LogUtil.w(TAG, "CdnIpController [CdnIpController] param error");
            return null;
        }
        LogUtil.w(TAG, "CdnIpController [CdnIpController] cdnUnit=" + map.toString());
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            ArrayList<ConfigParams.CdnUrlUnit> arrayList = map.get(it.next()).getmCdnList();
            if (arrayList != null && arrayList.size() > 0) {
                Iterator<ConfigParams.CdnUrlUnit> it2 = arrayList.iterator();
                while (it2.hasNext()) {
                    ConfigParams.CdnUrlUnit next = it2.next();
                    ArrayList<String> arrayList2 = next.getmIpList();
                    int i = next.getmWeight();
                    String domainFromUrl = Util.getDomainFromUrl(next.getmUrl());
                    map2.put(domainFromUrl, new CndIpControllerUnit(domainFromUrl, arrayList2, i, next.getmPort()));
                }
            }
        }
        return map2;
    }

    public void removeUnit(String str) {
        if (this.mActualTimeMap.containsKey(str)) {
            this.mActualTimeMap.remove(str);
        }
    }

    public CndIpControllerUnit nextUnit(String str) {
        LogUtil.i(TAG, "nextUnit \u9891\u9053=" + str);
        int i = 0;
        CndIpControllerUnit cndIpControllerUnit = null;
        for (CndIpControllerUnit cndIpControllerUnit2 : this.mActualTimeMap.values()) {
            if (Util.getCdnChannel(cndIpControllerUnit2.mDomain).equals(str)) {
                CndIpControllerUnit cndIpControllerUnit3 = this.mActualTimeMap.get(cndIpControllerUnit2.mDomain);
                if (!cndIpControllerUnit3.mIpLinkUnitList.isEmpty() && i < cndIpControllerUnit2.mWeight) {
                    i = cndIpControllerUnit2.mWeight;
                    cndIpControllerUnit = cndIpControllerUnit3;
                }
            }
        }
        if (cndIpControllerUnit != null) {
            LogUtil.i(TAG, "CdnIpController [nextUnit] \u6743\u91cd\u6700\u5927\u7684\u5355\u5143=" + cndIpControllerUnit.toString());
        } else {
            LogUtil.i(TAG, "CdnIpController [nextUnit] \u6ca1\u6709\u53ef\u4ee5ip\uff0c\u6ca1\u6709\u53ef\u7528\u6e90");
        }
        return cndIpControllerUnit;
    }

    public boolean hasNextUnit(String str) {
        LogUtil.i(TAG, "CdnIpController [hasNextUnit] \u9891\u9053=" + str);
        boolean z = false;
        if (TextUtils.isEmpty(str)) {
            LogUtil.i(TAG, "CdnIpController [hasNextUnit] \u53c2\u6570\u9519\u8bef");
            return false;
        }
        Iterator<CndIpControllerUnit> it = this.mActualTimeMap.values().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            CndIpControllerUnit next = it.next();
            if (Util.getCdnChannel(next.mDomain).equals(str)) {
                LogUtil.i(TAG, "CdnIpController [hasNextUnit] unit=" + next.toString());
                if (next.mIpLinkUnitList.size() > 0) {
                    z = true;
                    break;
                }
            }
        }
        LogUtil.i(TAG, "CdnIpController [hasNextUnit] result=" + z);
        return z;
    }

    public void removeIp(String str, String str2) {
        CndIpControllerUnit cndIpControllerUnit;
        TaskHandleOp.getInstance().getTaskHandle().setIsRemoveIp(true);
        TaskHandleOp.getInstance().addRemoveIpsMap(str, str2);
        if (!this.mActualTimeMap.containsKey(str) || (cndIpControllerUnit = this.mActualTimeMap.get(str)) == null) {
            return;
        }
        ArrayList<IpLinkUnit> arrayList = cndIpControllerUnit.mIpLinkUnitList;
        for (int i = 0; i < arrayList.size(); i++) {
            if (str2.contains(arrayList.get(i).mIp)) {
                arrayList.remove(i);
            }
        }
    }

    public String getPort(String str) {
        if (TextUtils.isEmpty(str)) {
            LogUtil.i(TAG, "CdnIpController [getPort] param error");
            return "";
        }
        if (this.mActualTimeMap.containsKey(str)) {
            return this.mActualTimeMap.get(str).mPort;
        }
        LogUtil.i(TAG, "CdnIpController [getPort] param error");
        return "";
    }

    public String nextIp(String str) {
        IpLinkUnit ipLinkUnit;
        String str2;
        ArrayList<IpLinkUnit> arrayList;
        CndIpControllerUnit cndIpControllerUnit = this.mActualTimeMap.get(str);
        String str3 = cndIpControllerUnit.mPort;
        if (cndIpControllerUnit == null || (arrayList = cndIpControllerUnit.mIpLinkUnitList) == null || arrayList.size() <= 0) {
            ipLinkUnit = null;
        } else {
            ipLinkUnit = arrayList.get((int) ((Math.random() * arrayList.size()) + 0.0d));
            int i = ipLinkUnit.mLinkCount;
            Iterator<IpLinkUnit> it = arrayList.iterator();
            while (it.hasNext()) {
                IpLinkUnit next = it.next();
                int i2 = next.mLinkCount;
                if (i2 < i) {
                    ipLinkUnit = next;
                    i = i2;
                }
            }
        }
        if (ipLinkUnit != null) {
            ipLinkUnit.mLinkCount++;
            str2 = ipLinkUnit.mIp;
        } else {
            str2 = "";
        }
        if (!TextUtils.isEmpty(str3)) {
            if (Util.isIpv6(str2)) {
                str2 = Constants.C + str2 + "]:" + str3;
            } else {
                str2 = str2 + ":" + str3;
            }
        }
        LogUtil.i(TAG, "CdnIpController [nextIp] result=" + str2);
        return str2;
    }

    public boolean containDomain(String str) {
        LogUtil.i(TAG, "CdnIpController [hasNextIp] \u53c2\u6570 domain=" + str);
        if (TextUtils.isEmpty(str)) {
            LogUtil.i(TAG, "CdnIpController [hasNextIp] domain is null");
            return false;
        }
        HashMap<String, CndIpControllerUnit> map = this.mActualTimeMap;
        return map != null && map.size() > 0 && this.mActualTimeMap.containsKey(str);
    }

    public boolean hasNextIp(String str) {
        CndIpControllerUnit cndIpControllerUnit;
        LogUtil.i(TAG, "CdnIpController [hasNextIp] \u53c2\u6570 domain=" + str);
        if (TextUtils.isEmpty(str)) {
            LogUtil.i(TAG, "CdnIpController [hasNextIp] domain is null");
            return false;
        }
        if (this.mActualTimeMap == null) {
            LogUtil.i(TAG, "CdnIpController [hasNextIp] mActualTimeMap is null");
        } else {
            LogUtil.i(TAG, "CdnIpController [hasNextIp] mActualTimeMap=" + this.mActualTimeMap);
        }
        HashMap<String, CndIpControllerUnit> map = this.mActualTimeMap;
        if (map == null || map.size() <= 0 || (cndIpControllerUnit = this.mActualTimeMap.get(str)) == null) {
            return false;
        }
        ArrayList<IpLinkUnit> arrayList = cndIpControllerUnit.mIpLinkUnitList;
        LogUtil.i(TAG, "domain=" + str + ", list\u5217\u8868=" + arrayList.toString() + ", list\u5927\u5c0f=" + arrayList.size());
        return arrayList.size() > 0;
    }

    public boolean hasNextIp() {
        this.mActualTimeMap.size();
        return true;
    }

    public boolean isLastIp(String str) {
        int size;
        if (this.mActualTimeMap.size() > 0) {
            size = 0;
            for (CndIpControllerUnit cndIpControllerUnit : this.mActualTimeMap.values()) {
                if (cndIpControllerUnit.mDomain.contains(str)) {
                    size += cndIpControllerUnit.mIpLinkUnitList.size();
                }
            }
        } else {
            size = 0;
        }
        return size == 1;
    }

    public int getChannelWeight(String str) {
        int i = 0;
        for (CndIpControllerUnit cndIpControllerUnit : this.mOriginalMap.values()) {
            if (Util.getCdnChannel(cndIpControllerUnit.mDomain).equals(str)) {
                i += cndIpControllerUnit.mWeight;
            }
        }
        return i;
    }

    public ArrayList<Integer> getWeights(String str) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (CndIpControllerUnit cndIpControllerUnit : this.mOriginalMap.values()) {
            if (Util.getCdnChannel(cndIpControllerUnit.mDomain).equals(str)) {
                arrayList.add(Integer.valueOf(cndIpControllerUnit.mWeight));
            }
        }
        return arrayList;
    }

    public int getCdnCount(String str) {
        Iterator<CndIpControllerUnit> it = this.mOriginalMap.values().iterator();
        int i = 0;
        while (it.hasNext()) {
            if (Util.getCdnChannel(it.next().mDomain).equals(str)) {
                i++;
            }
        }
        return i;
    }

    public ArrayList<String> getHost(String str) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (CndIpControllerUnit cndIpControllerUnit : this.mOriginalMap.values()) {
            if (Util.getCdnChannel(cndIpControllerUnit.mDomain).equals(str)) {
                arrayList.add(cndIpControllerUnit.mDomain);
            }
        }
        return arrayList;
    }

    public class CndIpControllerUnit {
        public String mDomain;
        public ArrayList<IpLinkUnit> mIpLinkUnitList = new ArrayList<>();
        public String mPort;
        public int mWeight;

        public CndIpControllerUnit(String str, ArrayList<String> arrayList, int i, String str2) {
            this.mDomain = str;
            Iterator<String> it = arrayList.iterator();
            while (it.hasNext()) {
                this.mIpLinkUnitList.add(CdnIpController.this.new IpLinkUnit(it.next()));
            }
            this.mWeight = i;
            this.mPort = str2;
        }

        public String toString() {
            return "mDomain=" + this.mDomain + ", mIpArrayList=" + this.mIpLinkUnitList.toString() + ", mWeight=" + this.mWeight;
        }
    }

    public class IpLinkUnit {
        public String mIp;
        public int mLinkCount = 0;

        public IpLinkUnit(String str) {
            this.mIp = str;
        }

        public String toString() {
            return "mIp=" + this.mIp + ", mLinkCount=" + this.mLinkCount;
        }
    }

    public void clean() {
        this.mOriginalMap.clear();
        this.mActualTimeMap.clear();
        sCndIpController = null;
    }
}