package com.netease.pharos.qos;

import android.content.Context;
import android.text.TextUtils;
import com.netease.ntunisdk.external.protocol.Const;
import com.netease.pharos.PharosProxy;
import com.netease.pharos.network.NetUtil;
import com.netease.pharos.network.NetworkDealer;
import com.netease.pharos.util.LogUtil;
import com.netease.pharos.util.Storage;
import com.netease.pharos.util.Util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes4.dex */
public class HighSpeedListCore {
    private static final String TAG = "HighSpeedListCore";
    private String mUrl = null;
    private int mStatus = 0;
    private CheckHighSpeedListCore checkHighSpeedListCore = null;
    private final NetworkDealer<Integer> dealer = new NetworkDealer<Integer>() { // from class: com.netease.pharos.qos.HighSpeedListCore.1
        @Override // com.netease.pharos.network.NetworkDealer
        public /* bridge */ /* synthetic */ Integer processContent(InputStream inputStream, int i, Map map) throws Exception {
            return processContent(inputStream, i, (Map<String, String>) map);
        }

        @Override // com.netease.pharos.network.NetworkDealer
        public void processHeader(Map<String, List<String>> map, int i, Map<String, String> map2) {
            String str = map2.get("url");
            List<String> list = map.get(Const.ETAG);
            StringBuilder sb = new StringBuilder();
            if (list != null) {
                for (String str2 : list) {
                    if (sb.length() > 0) {
                        sb.append(" ");
                    }
                    sb.append(str2);
                }
            }
            Storage.getInstance().saveTag(str, sb.toString());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.netease.pharos.network.NetworkDealer
        public Integer processContent(InputStream inputStream, int i, Map<String, String> map) throws Exception {
            LogUtil.stepLog("HighSpeedListCore \u83b7\u53d6\u9ad8\u901f\u5217\u8868---\u89e3\u6790\u5185\u5bb9");
            long jCurrentTimeMillis = System.currentTimeMillis();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            ArrayList arrayList = new ArrayList();
            String str = PharosProxy.getInstance().getmIp();
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line);
                sb.append("\n");
                arrayList.add(line);
                if (str != null && line.startsWith(str)) {
                    HighSpeedListInfo.getInstance().add(line);
                } else {
                    arrayList.add(line);
                }
            }
            Context context = PharosProxy.getInstance().getContext();
            if (context != null) {
                Util.info2File(sb.toString(), context.getFileStreamPath(com.netease.pharos.Const.LAST_LIGHTEN_LOCAL_CONFIG), true);
                Storage.getInstance().saveHash(map.get("url"), Util.getFileMd5(context, com.netease.pharos.Const.LAST_LIGHTEN_LOCAL_CONFIG));
            }
            HighSpeedListInfo.getInstance().addList(arrayList);
            HighSpeedListCore.this.mStatus = 1;
            int i2 = HighSpeedListCore.this.parse();
            LogUtil.stepLog("HighSpeedListCore [parse#1 cost]:" + (System.currentTimeMillis() - jCurrentTimeMillis));
            return Integer.valueOf(i2);
        }
    };
    private boolean mShouldSwitch2HttpDns = false;

    /* JADX INFO: Access modifiers changed from: private */
    public int parse() throws JSONException {
        LogUtil.i(TAG, "HighSpeedListCore [harbor\u6a21\u5757] [parse] start");
        long jCurrentTimeMillis = System.currentTimeMillis();
        String str = PharosProxy.getInstance().getmIp();
        JSONArray jSONArray = PharosProxy.getInstance().getmPorts();
        if (jSONArray == null) {
            jSONArray = new JSONArray();
            String str2 = PharosProxy.getInstance().getmPort();
            if (!TextUtils.isEmpty(str2)) {
                LogUtil.i(TAG, "HighSpeedListCore [harbor\u6a21\u5757] [parse] \u83b7\u53d6\u9ad8\u901f\u5217\u8868---\u89e3\u6790\u7ed3\u679c \u4f7f\u7528\u5355\u7aef\u53e3\u65b9\u5f0f");
                jSONArray = new JSONArray();
                jSONArray.put(str2);
            }
        }
        LogUtil.stepLog("HighSpeedListCore [parameters cost]:" + (System.currentTimeMillis() - jCurrentTimeMillis));
        if (!TextUtils.isEmpty(str) && jSONArray.length() != 0) {
            LogUtil.i(TAG, "HighSpeedListCore \u83b7\u53d6\u9ad8\u901f\u5217\u8868---\u89e3\u6790\u7ed3\u679c \u7aef\u53e3\u5217\u8868=" + jSONArray);
            JSONObject jSONObject = HighSpeedListInfo.getInstance().parse(str);
            LogUtil.stepLog("HighSpeedListCore [query cost]:" + (System.currentTimeMillis() - jCurrentTimeMillis));
            this.checkHighSpeedListCore.setData(jSONObject);
            int iStart = this.checkHighSpeedListCore.start();
            LogUtil.i(TAG, "HighSpeedListCore \u83b7\u53d6\u9ad8\u901f\u5217\u8868---\u89e3\u6790\u7ed3\u679c=" + iStart);
            return iStart;
        }
        LogUtil.w(TAG, "HighSpeedListCore \u83b7\u53d6\u9ad8\u901f\u5217\u8868---\u89e3\u6790\u7ed3\u679c, ip \u6216\u8005 port \u4e3a\u7a7a");
        return 14;
    }

    /* JADX WARN: Removed duplicated region for block: B:42:0x0188  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0239 A[PHI: r0
  0x0239: PHI (r0v26 int) = (r0v19 int), (r0v25 int), (r0v19 int) binds: [B:56:0x01f2, B:67:0x0227, B:54:0x01e3] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized int start() {
        /*
            Method dump skipped, instructions count: 699
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.pharos.qos.HighSpeedListCore.start():int");
    }

    private int start(String str, String str2) {
        LogUtil.stepLog("HighSpeedListCore [start] \u83b7\u53d6\u9ad8\u901f\u5217\u8868");
        long jCurrentTimeMillis = System.currentTimeMillis();
        HashMap map = new HashMap();
        if (!TextUtils.isEmpty(str2)) {
            map.put("Host", str2);
        }
        String tag = Storage.getInstance().getTag(str);
        if (!TextUtils.isEmpty(tag)) {
            map.put("If-None-Match", tag);
            LogUtil.stepLog("HighSpeedListCore [header] etag:" + tag);
        }
        int iIntValue = 11;
        if (!TextUtils.isEmpty(str)) {
            try {
                iIntValue = ((Integer) NetUtil.doHttpReq(str, null, "GET", map, this.dealer)).intValue();
            } catch (IOException e) {
                LogUtil.w(TAG, "HighSpeedListCore [start] IOException=" + e);
            }
            LogUtil.stepLog("HighSpeedListCore [http parse cost]:" + (System.currentTimeMillis() - jCurrentTimeMillis));
        }
        LogUtil.w(TAG, "HighSpeedListCore [start] \u83b7\u53d6\u9ad8\u901f\u5217\u8868---\u7ed3\u679c=" + iIntValue);
        if (iIntValue != 0) {
            LogUtil.w(TAG, "HighSpeedListCore [start] \u83b7\u53d6\u9ad8\u901f\u5217\u8868\u5931\u8d25 \u8bbe\u4e3a\u5931\u8d25\u72b6\u6001");
            this.mStatus = -1;
        }
        return iIntValue;
    }

    public void clean() {
        LogUtil.i(TAG, "HighSpeedListCore [clean] start");
        this.mStatus = 0;
    }
}