package com.netease.pharos.deviceCheck;

import android.text.TextUtils;
import com.facebook.common.util.UriUtil;
import com.netease.pharos.PharosProxy;
import com.netease.pharos.httpdns.HttpdnsProxy;
import com.netease.pharos.httpdns.HttpdnsUrlSwitcherCore;
import com.netease.pharos.network.NetUtil;
import com.netease.pharos.network.NetworkDealer;
import com.netease.pharos.util.LogUtil;
import com.netease.pharos.util.Util;
import com.xiaomi.onetrack.api.b;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class NetDnsCore {
    private static final String TAG = "NetDnsCore";
    private static NetDnsCore sNetDnsCore;
    private String mUrl = "https://nstool.netease.com/jsonify/";
    private final NetworkDealer<Integer> dealer = new NetworkDealer<Integer>() { // from class: com.netease.pharos.deviceCheck.NetDnsCore.1
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
            LogUtil.stepLog("Dns \u67e5\u8be2 net_dns---\u89e3\u6790\u5185\u5bb9");
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
            NetDnsCore.this.parse(string);
            LogUtil.i(NetDnsCore.TAG, "Dns \u67e5\u8be2 net_dns---\u89e3\u6790\u7ed3\u679c=" + string);
            return Integer.valueOf(i2);
        }
    };

    private NetDnsCore() {
    }

    public static NetDnsCore getInstances() {
        if (sNetDnsCore == null) {
            sNetDnsCore = new NetDnsCore();
        }
        return sNetDnsCore;
    }

    public int start() {
        String strReplaceDomainWithIpAddr = this.mUrl;
        if (PharosProxy.getInstance().ismEB()) {
            this.mUrl = "https://dl.nstool.easebar.com/jsonify/";
        }
        int iStart = start(this.mUrl, null);
        LogUtil.i(TAG, "\u666e\u901a\u8bf7\u6c42\u7ed3\u679c=" + iStart);
        if (iStart != 0) {
            String domainFromUrl = Util.getDomainFromUrl(this.mUrl);
            if (TextUtils.isEmpty(domainFromUrl)) {
                LogUtil.i(TAG, "domain\u4e3a\u7a7a");
                return iStart;
            }
            LogUtil.i(TAG, "\u8d70Httpdns");
            HttpdnsProxy.getInstances().synStart("Pharos_nstool", new String[]{domainFromUrl});
            HttpdnsUrlSwitcherCore.KeyHttpdnsUrlSwitcherCoreUnit httpdnsUrlSwitcherCore = HttpdnsProxy.getInstances().getHttpdnsUrlSwitcherCore("Pharos_nstool");
            if (httpdnsUrlSwitcherCore != null) {
                LogUtil.i(TAG, "httpdns\u7ed3\u679c=" + httpdnsUrlSwitcherCore);
                Iterator<HttpdnsUrlSwitcherCore.HttpdnsUrlSwitcherCoreUnit> it = httpdnsUrlSwitcherCore.getHttpdnsUrlUnitList().iterator();
                while (it.hasNext()) {
                    HttpdnsUrlSwitcherCore.HttpdnsUrlSwitcherCoreUnit next = it.next();
                    String str = next.ip;
                    String str2 = next.host;
                    LogUtil.i(TAG, "\u539furl=" + strReplaceDomainWithIpAddr);
                    strReplaceDomainWithIpAddr = Util.replaceDomainWithIpAddr(strReplaceDomainWithIpAddr, str, "/");
                    LogUtil.i(TAG, "\u65b0url=" + strReplaceDomainWithIpAddr);
                    iStart = start(strReplaceDomainWithIpAddr, str2);
                    LogUtil.i(TAG, "Httpdns \uff0c\u8fd4\u56de\u7801=" + iStart + ", ip=" + str);
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

    public int start(String str, String str2) {
        int iIntValue;
        LogUtil.stepLog("Dns \u67e5\u8be2 net_dns");
        char[] cArr = {'P', 'F', 't'};
        String[] strArr = {"Tg", "Rb", "Vr", "j43"};
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < 3; i++) {
            stringBuffer.append(cArr[i]);
        }
        for (int i2 = 0; i2 < 4; i2++) {
            stringBuffer.append(strArr[i2]);
        }
        HashMap map = new HashMap();
        map.put("X-AUTH-PROJECT", "impression");
        map.put("X-AUTH-TOKEN", stringBuffer.toString());
        if (!TextUtils.isEmpty(str2)) {
            map.put("Host", str2);
        }
        if (TextUtils.isEmpty(str)) {
            iIntValue = 11;
        } else {
            try {
                iIntValue = ((Integer) NetUtil.doHttpReq(str, null, "GET", map, this.dealer)).intValue();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LogUtil.stepLog("Dns \u67e5\u8be2 net_dns---\u7ed3\u679c=" + iIntValue);
        return iIntValue;
    }

    public void parse(String str) throws JSONException {
        LogUtil.i(TAG, "\u89e3\u6790\u5185\u5bb9---" + str);
        if (TextUtils.isEmpty(str)) {
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.has("dns_province")) {
                jSONObject.getString("dns_province");
            }
            if (jSONObject.has("ip_city")) {
                jSONObject.getString("ip_city");
            }
            if (jSONObject.has("ip")) {
                jSONObject.getString("ip");
            }
            if (jSONObject.has("ip_province")) {
                jSONObject.getString("ip_province");
            }
            if (jSONObject.has("ip_isp")) {
                jSONObject.getString("ip_isp");
            }
            if (jSONObject.has(UriUtil.LOCAL_RESOURCE_SCHEME)) {
                jSONObject.getString(UriUtil.LOCAL_RESOURCE_SCHEME);
            }
            if (jSONObject.has("dns_city")) {
                jSONObject.getString("dns_city");
            }
            if (jSONObject.has("dns_isp")) {
                jSONObject.getString("dns_isp");
            }
            String string = jSONObject.has(b.P) ? jSONObject.getString(b.P) : "";
            if (jSONObject.has("msg")) {
                jSONObject.getString("msg");
            }
            DeviceInfo.getInstance().setNameserver(string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}