package com.netease.pharos.locationCheck;

import android.content.Context;
import android.text.TextUtils;
import com.netease.ntunisdk.external.protocol.Const;
import com.netease.ntunisdk.unilogger.global.Const;
import com.netease.pharos.PharosProxy;
import com.netease.pharos.ServerProxy;
import com.netease.pharos.deviceCheck.DeviceInfo;
import com.netease.pharos.network.NetUtil;
import com.netease.pharos.network.NetworkDealer;
import com.netease.pharos.util.LogUtil;
import com.netease.pharos.util.Storage;
import com.netease.pharos.util.Util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes5.dex */
public class NetAreaCore {
    private static final String TAG = "NetAreaCore";
    private static NetAreaCore sLocationCore;
    private final NetworkDealer<Integer> dealer = new NetworkDealer<Integer>() { // from class: com.netease.pharos.locationCheck.NetAreaCore.1
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
            LogUtil.stepLog("NetAreaCore [dealer][processContent] \u4e0b\u8f7d\u5173\u7cfb\u6620\u5c04\u8868---\u89e3\u6790\u5185\u5bb9");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line);
            }
            int i2 = !TextUtils.isEmpty(sb.toString()) ? 0 : 11;
            LogUtil.i(NetAreaCore.TAG, "NetAreaCore [dealer][processContent] \u4e0b\u8f7d\u5173\u7cfb\u6620\u5c04\u8868---\u89e3\u6790\u7ed3\u679c=" + ((Object) sb));
            Context context = PharosProxy.getInstance().getContext();
            if (context != null) {
                Util.info2File(sb.toString(), context.getFileStreamPath(com.netease.pharos.Const.LAST_NET_DECISION_CONFIG), true);
                Storage.getInstance().saveHash(map.get("url"), Util.getFileMd5(context, com.netease.pharos.Const.LAST_NET_DECISION_CONFIG));
            }
            NetAreaInfo.getInstances().init(sb.toString());
            return Integer.valueOf(i2);
        }
    };

    private NetAreaCore() {
    }

    public static NetAreaCore getInstances() {
        if (sLocationCore == null) {
            synchronized (NetAreaCore.class) {
                if (sLocationCore == null) {
                    sLocationCore = new NetAreaCore();
                }
            }
        }
        return sLocationCore;
    }

    public int start() throws Throwable {
        String str = String.format(ServerProxy.getInstance().getNetAreaUrl(), Util.getUpperVersion(), Const.CONFIG_KEY.ALL);
        LogUtil.i(TAG, "NetAreaCore [start] \u666e\u901a\u8bf7\u6c42\u7ed3\u679c decision=" + PharosProxy.getInstance().getmDecision());
        if (1 == PharosProxy.getInstance().getmDecision()) {
            str = String.format(ServerProxy.getInstance().getNetAreaUrl(), Util.getUpperVersion(), PharosProxy.getInstance().getProjectId());
        }
        String hash = Storage.getInstance().getHash(str);
        boolean zIsEmpty = TextUtils.isEmpty(hash);
        if (!zIsEmpty) {
            String fileMd5 = Util.getFileMd5(PharosProxy.getInstance().getContext(), com.netease.pharos.Const.LAST_NET_DECISION_CONFIG);
            boolean z = TextUtils.isEmpty(fileMd5) || !TextUtils.equals(hash, fileMd5);
            LogUtil.i(TAG, "NetAreaCore [start] checkLocal md5=" + hash + ", fileMd5:" + fileMd5 + ",isNeedDownloadFile:" + z);
            zIsEmpty = z;
        }
        if (zIsEmpty) {
            Storage.getInstance().clearTag(str);
        }
        DeviceInfo.getInstance().setmDecisionTag(Util.getDecisionTag(str));
        int iStart = start(str, null);
        LogUtil.i(TAG, "NetAreaCore [start] \u666e\u901a\u8bf7\u6c42\u7ed3\u679c=" + iStart);
        LogUtil.i(TAG, "NetAreaCore [start] \u666e\u901a\u8bf7\u6c42\u7ed3\u679c DeviceInfo.getInstances().getmProbeRegion()=" + DeviceInfo.getInstance().getmProbeRegion());
        if (iStart != 0 || !zIsEmpty) {
            if (iStart != 0) {
                LogUtil.stepLog("NetAreaCore [start] \u4e0b\u8f7d\u5173\u7cfb\u6620\u5c04\u8868---\u83b7\u53d6\u5931\u8d25\uff0c\u91c7\u7528\u4e0a\u4e00\u6b21\u6570\u636e");
            } else {
                LogUtil.stepLog("NetAreaCore [start] \u65e0\u9700\u4e0b\u8f7d\u5173\u7cfb\u6620\u5c04\u8868\uff0c\u91c7\u7528\u4e0a\u4e00\u6b21\u6570\u636e");
            }
            String lastNetDecisionConfigContent = NetAreaInfo.getLastNetDecisionConfigContent();
            if (!TextUtils.isEmpty(lastNetDecisionConfigContent)) {
                LogUtil.stepLog("NetAreaCore [start] \u4e0b\u8f7d\u5173\u7cfb\u6620\u5c04\u8868---\u4e0a\u4e00\u6b21\u6570\u636e=" + lastNetDecisionConfigContent);
                NetAreaInfo.getInstances().init(lastNetDecisionConfigContent);
            } else {
                LogUtil.stepLog("NetAreaCore [start] \u4e0b\u8f7d\u5173\u7cfb\u6620\u5c04\u8868---\u4e0a\u4e00\u6b21\u6570\u636e \u8bfb\u53d6\u5931\u8d25\uff0c\u91c7\u7528\u9ed8\u8ba4\u6570\u636e");
                String defaultNetDecisionConfigContent = NetAreaInfo.getDefaultNetDecisionConfigContent();
                if (!TextUtils.isEmpty(defaultNetDecisionConfigContent)) {
                    LogUtil.stepLog("NetAreaCore [start] \u4e0b\u8f7d\u5173\u7cfb\u6620\u5c04\u8868---\u9ed8\u8ba4\u6570\u636e=" + defaultNetDecisionConfigContent);
                    NetAreaInfo.getInstances().init(defaultNetDecisionConfigContent);
                } else {
                    LogUtil.stepLog("NetAreaCore [start] \u4e0b\u8f7d\u5173\u7cfb\u6620\u5c04\u8868---\u9ed8\u8ba4\u6570\u636e \u8bfb\u53d6\u5931\u8d25");
                }
            }
        }
        return iStart;
    }

    public int start(String str, String str2) {
        int iIntValue;
        LogUtil.stepLog("NetAreaCore [start] \u4e0b\u8f7d\u5173\u7cfb\u6620\u5c04\u8868");
        LogUtil.i(TAG, "[Pharos] Config Refresh url=" + str);
        HashMap map = new HashMap();
        if (!TextUtils.isEmpty(str2)) {
            map.put("Host", str2);
        }
        String tag = Storage.getInstance().getTag(str);
        if (!TextUtils.isEmpty(tag)) {
            map.put("If-None-Match", tag);
            LogUtil.stepLog("NetAreaCore [header] etag:" + tag);
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
        LogUtil.stepLog("NetAreaCore [start] \u4e0b\u8f7d\u5173\u7cfb\u6620\u5c04\u8868---\u7ed3\u679c=" + iIntValue);
        return iIntValue;
    }
}