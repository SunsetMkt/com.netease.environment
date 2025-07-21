package com.netease.pharos.report;

import android.text.TextUtils;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import com.netease.pharos.httpdns.HttpdnsProxy;
import com.netease.pharos.httpdns.HttpdnsUrlSwitcherCore;
import com.netease.pharos.network.NetUtil;
import com.netease.pharos.network.NetworkDealer;
import com.netease.pharos.util.LogUtil;
import com.netease.pharos.util.Util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes5.dex */
public class ReportCore {
    private static final String TAG = "ReportCore";
    private String mUrl = null;
    private final NetworkDealer<Integer> dealer = new NetworkDealer<Integer>() { // from class: com.netease.pharos.report.ReportCore.1
        @Override // com.netease.pharos.network.NetworkDealer
        public void processHeader(Map<String, List<String>> map, int i, Map<String, String> map2) {
        }

        @Override // com.netease.pharos.network.NetworkDealer
        public /* bridge */ /* synthetic */ Integer processContent(InputStream inputStream, int i, Map map) throws Exception {
            return processContent(inputStream, i, (Map<String, String>) map);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.netease.pharos.network.NetworkDealer
        public Integer processContent(InputStream inputStream, int i, Map<String, String> map) throws Exception {
            LogUtil.stepLog("\u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u89e3\u6790\u5185\u5bb9");
            LogUtil.i(ReportCore.TAG, "\u4e0a\u4f20\u7ed3\u679c=" + i);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line);
            }
            String string = sb.toString();
            int i2 = !TextUtils.isEmpty(string) ? 0 : 11;
            LogUtil.i(ReportCore.TAG, "\u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u89e3\u6790\u7ed3\u679c=" + string);
            return Integer.valueOf(i2);
        }
    };

    public void init(String str) {
        this.mUrl = str;
    }

    public int start(String str, String str2) {
        String strReplaceDomainWithIpAddr = this.mUrl;
        int iStart = start(str, strReplaceDomainWithIpAddr, null);
        LogUtil.i(TAG, "\u666e\u901a\u4e0a\u4f20\u7ed3\u679c=" + iStart);
        if (iStart != 0) {
            String domainFromUrl = Util.getDomainFromUrl(this.mUrl);
            if (TextUtils.isEmpty(domainFromUrl)) {
                LogUtil.i(TAG, "domain\u4e3a\u7a7a");
                return iStart;
            }
            LogUtil.i(TAG, "\u8d70Httpdns");
            HttpdnsProxy.getInstances().synStart("Pharos", new String[]{domainFromUrl});
            HttpdnsUrlSwitcherCore.KeyHttpdnsUrlSwitcherCoreUnit httpdnsUrlSwitcherCore = HttpdnsProxy.getInstances().getHttpdnsUrlSwitcherCore("Pharos_sigma");
            if (httpdnsUrlSwitcherCore != null) {
                LogUtil.i(TAG, "httpdns\u7ed3\u679c=" + httpdnsUrlSwitcherCore);
                Iterator<HttpdnsUrlSwitcherCore.HttpdnsUrlSwitcherCoreUnit> it = httpdnsUrlSwitcherCore.getHttpdnsUrlUnitList().iterator();
                while (it.hasNext()) {
                    HttpdnsUrlSwitcherCore.HttpdnsUrlSwitcherCoreUnit next = it.next();
                    String str3 = next.ip;
                    String str4 = next.host;
                    LogUtil.i(TAG, "\u539furl=" + strReplaceDomainWithIpAddr);
                    strReplaceDomainWithIpAddr = Util.replaceDomainWithIpAddr(strReplaceDomainWithIpAddr, str3, "/");
                    LogUtil.i(TAG, "\u65b0url=" + strReplaceDomainWithIpAddr);
                    iStart = start(str, strReplaceDomainWithIpAddr, str4);
                    LogUtil.i(TAG, "Httpdns \u4e0a\u4f20\uff0c\u8fd4\u56de\u7801=" + iStart + ", ip=" + str3);
                    if (iStart == 0) {
                        break;
                    }
                }
            } else {
                LogUtil.i(TAG, "httpdns\u7ed3\u679c\u4e3a\u7a7a");
            }
        }
        return iStart;
    }

    public int start(String str, String str2, String str3) {
        int iIntValue;
        LogUtil.stepLog("\u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757");
        if (TextUtils.isEmpty(this.mUrl)) {
            LogUtil.i(TAG, "\u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---url\u4e3a\u7a7a");
            return 1;
        }
        if (TextUtils.isEmpty(str)) {
            LogUtil.i(TAG, "\u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u4e0a\u4f20\u4fe1\u606f\u4e3a\u7a7a");
            return 1;
        }
        LogUtil.i(TAG, "\u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u4e0a\u4f20\u4fe1\u606f1=" + str);
        HashMap map = new HashMap();
        map.put("Content-Type", ClientLogConstant.NORMAL_TYPE_VALUE);
        if (!TextUtils.isEmpty(str3)) {
            map.put("Host-Type", str3);
            map.put("Host", str3);
        }
        HashMap map2 = new HashMap();
        map2.put("post_content", str);
        if (TextUtils.isEmpty(str2)) {
            iIntValue = 11;
        } else {
            try {
                iIntValue = ((Integer) NetUtil.doHttpReq(str2, map2, "POST", map, this.dealer)).intValue();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LogUtil.stepLog("\u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u7ed3\u679c=" + iIntValue);
        return iIntValue;
    }

    public int start(String str) {
        int iIntValue;
        LogUtil.stepLog("\u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757");
        if (TextUtils.isEmpty(this.mUrl)) {
            LogUtil.i(TAG, "\u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---url\u4e3a\u7a7a");
            return 1;
        }
        if (TextUtils.isEmpty(str)) {
            LogUtil.i(TAG, "\u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u4e0a\u4f20\u4fe1\u606f\u4e3a\u7a7a");
            return 1;
        }
        LogUtil.i(TAG, "\u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u4e0a\u4f20\u4fe1\u606f2=" + str);
        HashMap map = new HashMap();
        map.put("Content-Type", ClientLogConstant.NORMAL_TYPE_VALUE);
        HashMap map2 = new HashMap();
        map2.put("post_content", str);
        if (TextUtils.isEmpty(this.mUrl)) {
            iIntValue = 11;
        } else {
            try {
                iIntValue = ((Integer) NetUtil.doHttpReq(this.mUrl, map2, "POST", map, this.dealer)).intValue();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LogUtil.stepLog("\u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u7ed3\u679c=" + iIntValue);
        return iIntValue;
    }
}