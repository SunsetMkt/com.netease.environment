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
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class Ipv6Verify {
    private static final String TAG = "whoami6";
    private static final String WHOAMI6_URL_MAINLAND = "https://who6.nie.netease.com";
    private static final String WHOAMI6_URL_OVERSEA = "https://who6.nie.easebar.com";
    private static Ipv6Verify instance;
    private final NetworkDealer<Integer> dealer = new NetworkDealer<Integer>() { // from class: com.netease.pharos.deviceCheck.Ipv6Verify.1
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
            if (Thread.currentThread().isInterrupted()) {
                return 12;
            }
            LogUtil.stepLog("Ipv6Verify---\u89e3\u6790\u5185\u5bb9");
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
            Ipv6Verify.this.parse(string);
            LogUtil.i(Ipv6Verify.TAG, "Ipv6Verify---\u89e3\u6790\u7ed3\u679c=" + string);
            return Integer.valueOf(i2);
        }
    };

    private Ipv6Verify() {
    }

    public static Ipv6Verify getInstance() {
        if (instance == null) {
            synchronized (Ipv6Verify.class) {
                if (instance == null) {
                    instance = new Ipv6Verify();
                }
            }
        }
        return instance;
    }

    public int start() {
        String name;
        String str = PharosProxy.getInstance().isOversea() ? WHOAMI6_URL_OVERSEA : WHOAMI6_URL_MAINLAND;
        try {
            name = Thread.currentThread().getName();
        } catch (Exception unused) {
            name = "unknown";
        }
        LogUtil.i(TAG, String.format("[pharos] request who6#%s, %s", name, str));
        int iStart = start(str, null);
        LogUtil.i(TAG, String.format("[pharos] result who6 %s", Integer.valueOf(iStart)));
        return iStart;
    }

    public int start(String str, String str2) {
        int iIntValue;
        LogUtil.stepLog("[pharos] start Ipv6Verify");
        HashMap map = new HashMap();
        map.put("X-AUTH-PRODUCT", "impression");
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
        LogUtil.stepLog("[pharos] finish Ipv6Verify---result=" + iIntValue);
        return iIntValue;
    }

    public void parse(String str) {
        LogUtil.i(TAG, String.format("[pharos] who6 response: %s", str));
        if (TextUtils.isEmpty(str)) {
            return;
        }
        try {
            String strOptString = new JSONObject(str).optString("ipv6", "");
            LogUtil.i(TAG, String.format("[pharos] who6 parse--> ipv6:%s", strOptString));
            DeviceInfo.getInstance().setIpaddrV6(strOptString);
        } catch (JSONException e) {
            LogUtil.e(TAG, e.toString());
            e.printStackTrace();
        }
    }
}