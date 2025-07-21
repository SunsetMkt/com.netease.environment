package com.netease.pharos.httpdns;

import android.text.TextUtils;
import com.netease.pharos.Const;
import com.netease.pharos.PharosProxy;
import com.netease.pharos.network.NetUtil;
import com.netease.pharos.network.NetworkDealer;
import com.netease.pharos.util.LogUtil;
import com.netease.pharos.util.Util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class HttpdnsDomain2IpCore implements Callable<Integer> {
    private static final String TAG = "HttpdnsDomain2IpCore";
    private String mDomain;
    private final NetworkDealer<Integer> mDomainDealer = new NetworkDealer<Integer>() { // from class: com.netease.pharos.httpdns.HttpdnsDomain2IpCore.1
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
            System.currentTimeMillis();
            long unused = HttpdnsDomain2IpCore.this.mStartTime;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                String line = bufferedReader.readLine();
                if (line != null) {
                    stringBuffer.append(line);
                } else {
                    LogUtil.i(HttpdnsDomain2IpCore.TAG, "Httpdns\u73af\u8282--\u901a\u8fc7httpdns\u670d\u52a1\u5668\u89e3\u6790\u57df\u540d\uff0c\u8bf7\u6c42\u7ed3\u679c\u6570\u636e=" + ((Object) stringBuffer));
                    HttpdnsDomain2IpParams.getInstances().init(new JSONObject(stringBuffer.toString()).toString());
                    return 0;
                }
            }
        }
    };
    private long mStartTime;

    public void init(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if (str.contains("/")) {
            this.mDomain = Util.getDomainFromUrl(str);
        } else {
            this.mDomain = str;
        }
    }

    public int start() {
        LogUtil.stepLog("Httpdns\u73af\u8282--\u901a\u8fc7httpdns\u670d\u52a1\u5668\u89e3\u6790\u57df\u540d\uff0c\u5f00\u59cb");
        if (!Util.isZoneEast8()) {
            return 17;
        }
        String str = PharosProxy.getInstance().ismEB() ? Const.HTTP_DNS_SERVER_OVERSEA : Const.HTTP_DNS_SERVER_MAINLAND;
        LogUtil.stepLog("Httpdns\u73af\u8282--httpdns\u670d\u52a1\u5668:".concat(str));
        return reqCdnTargetIp(Util.getHttpDnsDomain2IpUrl(str, this.mDomain));
    }

    public synchronized int reqCdnTargetIp(String str) {
        int iIntValue;
        LogUtil.stepLog("Httpdns\u73af\u8282--\u901a\u8fc7httpdns\u670d\u52a1\u5668\u89e3\u6790\u57df\u540d\uff0c\u521d\u59cb\u5316");
        HashMap map = new HashMap();
        iIntValue = 0;
        try {
            LogUtil.i(TAG, "Httpdns\u73af\u8282--\u901a\u8fc7httpdns\u670d\u52a1\u5668\u89e3\u6790\u57df\u540d\uff0curl=" + str);
            char[] cArr = {'o', '5', 'b', 'W', 'e', '6', 'h', 'n'};
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < 8; i++) {
                stringBuffer.append(cArr[i]);
            }
            map.put("AUTH-TOKEN", stringBuffer.toString());
            map.put("AUTH-PROJECT", "impression");
            this.mStartTime = System.currentTimeMillis();
            iIntValue = ((Integer) NetUtil.doHttpReq(str, null, "GET", map, this.mDomainDealer)).intValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogUtil.i(TAG, "Httpdns\u73af\u8282--\u901a\u8fc7httpdns\u670d\u52a1\u5668\u89e3\u6790\u57df\u540d,\u8bf7\u6c42\u7ed3\u679c=" + iIntValue);
        return iIntValue;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.concurrent.Callable
    public Integer call() throws Exception {
        return Integer.valueOf(start());
    }
}