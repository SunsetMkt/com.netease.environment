package com.netease.pharos.deviceCheck;

import android.text.TextUtils;
import com.netease.pharos.PharosProxy;
import com.netease.pharos.network.NetUtil;
import com.netease.pharos.network.NetworkDealer;
import com.netease.pharos.util.LogUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes5.dex */
public class IpInfoCore {
    private static final String TAG = "IpInfoCore";
    private static final String WHOAMI_URL_MAINLAND = "https://whoami.nie.netease.com/v2";
    private static final String WHOAMI_URL_OVERSEA = "https://whoami.nie.easebar.com/v2";
    private static IpInfoCore sDevicesCore;
    private final NetworkDealer<Integer> dealer = new NetworkDealer<Integer>() { // from class: com.netease.pharos.deviceCheck.IpInfoCore.1
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
            LogUtil.stepLog("\u63a2\u6d4b\u7528\u6237\u8bbe\u5907\u7684\u57fa\u672c\u4fe1\u606f---\u89e3\u6790\u5185\u5bb9");
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
            IpInfoCore.this.parse(string);
            LogUtil.i(IpInfoCore.TAG, "\u63a2\u6d4b\u7528\u6237\u8bbe\u5907\u7684\u57fa\u672c\u4fe1\u606f---\u89e3\u6790\u7ed3\u679c=" + string);
            return Integer.valueOf(i2);
        }
    };

    private IpInfoCore() {
    }

    public static IpInfoCore getInstances() {
        if (sDevicesCore == null) {
            sDevicesCore = new IpInfoCore();
        }
        return sDevicesCore;
    }

    public int start() {
        String name;
        String str = PharosProxy.getInstance().isOversea() ? "https://whoami.nie.easebar.com/v2" : "https://whoami.nie.netease.com/v2";
        try {
            name = Thread.currentThread().getName();
        } catch (Exception unused) {
            name = "unknown";
        }
        LogUtil.i(TAG, String.format("[pharos] request whoami#%s, %s", name, str));
        int iStart = start(str, null);
        LogUtil.i(TAG, "\u666e\u901a\u8bf7\u6c42\u7ed3\u679c=" + iStart);
        return iStart;
    }

    public int start(String str, String str2) {
        int iIntValue;
        LogUtil.stepLog("\u63a2\u6d4b\u7528\u6237\u8bbe\u5907\u7684\u57fa\u672c\u4fe1\u606f");
        char[] cArr = {'t', 'o', 'k', 'e', 'n', '.'};
        String[] strArr = {"e8s", "UKK", "Msw", "YmL"};
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            stringBuffer.append(cArr[i]);
        }
        for (int i2 = 0; i2 < 4; i2++) {
            stringBuffer.append(strArr[i2]);
        }
        HashMap map = new HashMap();
        map.put("X-AUTH-PRODUCT", "impression");
        map.put("X-AUTH-TOKEN", stringBuffer.toString());
        map.put("X-IPDB-LOCALE", "en");
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
        LogUtil.stepLog("\u63a2\u6d4b\u7528\u6237\u8bbe\u5907\u7684\u57fa\u672c\u4fe1\u606f---\u7ed3\u679c=" + iIntValue);
        return iIntValue;
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0090  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00b0  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00d0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void parse(java.lang.String r17) throws org.json.JSONException {
        /*
            Method dump skipped, instructions count: 389
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.pharos.deviceCheck.IpInfoCore.parse(java.lang.String):void");
    }
}