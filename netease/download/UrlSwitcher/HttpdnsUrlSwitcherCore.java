package com.netease.download.UrlSwitcher;

import android.text.TextUtils;
import com.netease.download.httpdns.HttpdnsDomain2IpParams;
import com.netease.download.util.LogUtil;
import com.netease.download.util.Util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: classes6.dex */
public class HttpdnsUrlSwitcherCore {
    private static final String TAG = "HttpdnsUrlSwitcherCore";
    private static HttpdnsUrlSwitcherCore sHttpdnsUrlSwitcherCore;
    public HashMap<String, KeyHttpdnsUrlSwitcherCoreUnit> mHttpdnsUrlUnitMap = new HashMap<>();

    private HttpdnsUrlSwitcherCore() {
    }

    public static HttpdnsUrlSwitcherCore getInstances() {
        if (sHttpdnsUrlSwitcherCore == null) {
            sHttpdnsUrlSwitcherCore = new HttpdnsUrlSwitcherCore();
        }
        return sHttpdnsUrlSwitcherCore;
    }

    public void init(String str, ArrayList<HttpdnsDomain2IpParams.Unit> arrayList) {
        if (this.mHttpdnsUrlUnitMap.containsKey(str)) {
            return;
        }
        ArrayList arrayList2 = new ArrayList();
        Iterator<HttpdnsDomain2IpParams.Unit> it = arrayList.iterator();
        while (it.hasNext()) {
            HttpdnsDomain2IpParams.Unit next = it.next();
            ArrayList<String> arrayList3 = next.ipArrayList;
            String str2 = next.domain;
            for (int i = 0; i < arrayList3.size(); i++) {
                arrayList2.add(new HttpdnsUrlSwitcherCoreUnit(str2, arrayList3.get(i)));
            }
        }
        this.mHttpdnsUrlUnitMap.put(str, new KeyHttpdnsUrlSwitcherCoreUnit(arrayList2));
    }

    public static class KeyHttpdnsUrlSwitcherCoreUnit {
        public ArrayList<HttpdnsUrlSwitcherCoreUnit> mHttpdnsUrlUnitList;
        public int mIndex = 0;

        public KeyHttpdnsUrlSwitcherCoreUnit(ArrayList<HttpdnsUrlSwitcherCoreUnit> arrayList) {
            new ArrayList();
            this.mHttpdnsUrlUnitList = arrayList;
        }

        public boolean hasNext() {
            LogUtil.i(HttpdnsUrlSwitcherCore.TAG, "mIndex=" + this.mIndex + ", mHttpdnsUrlUnitList.size()=" + this.mHttpdnsUrlUnitList.size());
            return this.mHttpdnsUrlUnitList.size() > 0;
        }

        public HttpdnsUrlSwitcherCoreUnit next(String str) {
            LogUtil.i(HttpdnsUrlSwitcherCore.TAG, "HttpdnsUrlSwitcherCore [HttpdnsUrlSwitcherCoreUnit] \u9009\u62e9\u524d=" + this.mHttpdnsUrlUnitList.toString());
            Iterator<HttpdnsUrlSwitcherCoreUnit> it = this.mHttpdnsUrlUnitList.iterator();
            HttpdnsUrlSwitcherCoreUnit httpdnsUrlSwitcherCoreUnit = null;
            int i = -1;
            while (it.hasNext()) {
                HttpdnsUrlSwitcherCoreUnit next = it.next();
                LogUtil.i(HttpdnsUrlSwitcherCore.TAG, "host=" + Util.getCdnChannel(next.host) + ", channel=" + str);
                if (next != null && Util.getCdnChannel(next.host).equals(str)) {
                    int i2 = next.mLinkCount;
                    if (-1 == i || i2 <= i) {
                        httpdnsUrlSwitcherCoreUnit = next;
                        i = i2;
                    }
                }
            }
            if (httpdnsUrlSwitcherCoreUnit != null) {
                httpdnsUrlSwitcherCoreUnit.mLinkCount++;
                LogUtil.i(HttpdnsUrlSwitcherCore.TAG, "HttpdnsUrlSwitcherCore [HttpdnsUrlSwitcherCoreUnit]  result=" + httpdnsUrlSwitcherCoreUnit.toString());
            }
            LogUtil.i(HttpdnsUrlSwitcherCore.TAG, "HttpdnsUrlSwitcherCore [HttpdnsUrlSwitcherCoreUnit]  \u9009\u62e9\u540e =" + this.mHttpdnsUrlUnitList.toString());
            return httpdnsUrlSwitcherCoreUnit;
        }

        public void remove(String str) {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            for (int i = 0; i < this.mHttpdnsUrlUnitList.size(); i++) {
                if (this.mHttpdnsUrlUnitList.get(i).ip.equals(str)) {
                    this.mHttpdnsUrlUnitList.remove(i);
                }
            }
        }

        public ArrayList<HttpdnsUrlSwitcherCoreUnit> getHttpdnsUrlUnitList() {
            return this.mHttpdnsUrlUnitList;
        }

        public String toString() {
            return "mIndex=" + this.mIndex + ", mHttpdnsUrlUnitList=" + this.mHttpdnsUrlUnitList.toString();
        }
    }

    public static class HttpdnsUrlSwitcherCoreUnit {
        public String host;
        public String ip;
        public int mLinkCount = 0;

        public HttpdnsUrlSwitcherCoreUnit(String str, String str2) {
            this.host = str;
            this.ip = str2;
        }

        public String toString() {
            return "host=" + this.host + ", ip=" + this.ip + ", mLinkCount=" + this.mLinkCount;
        }
    }
}