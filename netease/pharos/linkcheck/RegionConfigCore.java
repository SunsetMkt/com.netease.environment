package com.netease.pharos.linkcheck;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes4.dex */
public class RegionConfigCore {
    private static final String TAG = "RegionConfigCore";
    private final NetworkDealer<Integer> dealer = new NetworkDealer<Integer>() { // from class: com.netease.pharos.linkcheck.RegionConfigCore.1
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
            String string = sb.toString();
            LogUtil.stepLog("RegionConfigCore [dealer] [processHeader]etag:" + string);
            Storage.getInstance().saveTag(str, string);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.netease.pharos.network.NetworkDealer
        public Integer processContent(InputStream inputStream, int i, Map<String, String> map) throws Exception {
            int i2;
            LogUtil.stepLog("RegionConfigCore [dealer] [processContent] \u94fe\u8def\u63a2\u6d4b\u6a21\u5757---\u4e0b\u8f7d\u914d\u7f6e\u6587\u4ef6---\u89e3\u6790\u5185\u5bb9");
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
            if (TextUtils.isEmpty(string)) {
                i2 = 11;
            } else {
                Context context = PharosProxy.getInstance().getContext();
                if (context != null) {
                    Util.info2File(sb.toString(), context.getFileStreamPath(com.netease.pharos.Const.LAST_REGION_LOCAL_CONFIG), true);
                    Storage.getInstance().saveHash(map.get("url"), Util.getFileMd5(context, com.netease.pharos.Const.LAST_REGION_LOCAL_CONFIG));
                }
                i2 = 0;
            }
            LogUtil.i(RegionConfigCore.TAG, "RegionConfigCore [dealer] [processContent]  \u94fe\u8def\u63a2\u6d4b\u6a21\u5757---\u4e0b\u8f7d\u914d\u7f6e\u6587\u4ef6---\u89e3\u6790\u7ed3\u679c=" + string);
            RegionConfigInfo.getInstance().init(string);
            RegionConfigInfo.getInstance().parse();
            return Integer.valueOf(i2);
        }
    };
    private String mUrl = null;

    public void init(String str) {
        this.mUrl = str;
    }

    public int start() throws IOException {
        String str = this.mUrl;
        boolean z = true;
        try {
            String hash = Storage.getInstance().getHash(str);
            boolean zIsEmpty = TextUtils.isEmpty(hash);
            if (zIsEmpty) {
                z = zIsEmpty;
            } else {
                String fileMd5 = Util.getFileMd5(PharosProxy.getInstance().getContext(), com.netease.pharos.Const.LAST_REGION_LOCAL_CONFIG);
                if (!TextUtils.isEmpty(fileMd5)) {
                    if (TextUtils.equals(hash, fileMd5)) {
                        z = false;
                    }
                }
            }
        } catch (Throwable unused) {
        }
        if (z) {
            Storage.getInstance().clearTag(str);
        }
        int iStart = start(this.mUrl, null, z);
        LogUtil.i(TAG, "RegionConfigCore [dealer]  isNeedDownloadFile =" + z + ",result:" + iStart);
        if (iStart == 0 && z) {
            return iStart;
        }
        if (iStart != 0) {
            LogUtil.stepLog("RegionConfigCore [start] \u83b7\u53d6\u5931\u8d25\uff0c\u91c7\u7528\u4e0a\u4e00\u6b21\u6570\u636e");
        } else {
            LogUtil.stepLog("RegionConfigCore [start] \u65e0\u9700\u4e0b\u8f7d\uff0c\u91c7\u7528\u4e0a\u4e00\u6b21\u6570\u636e");
        }
        Context context = PharosProxy.getInstance().getContext();
        if (context == null) {
            return iStart;
        }
        String strFile2Info = Util.file2Info(context.getFileStreamPath(com.netease.pharos.Const.LAST_REGION_LOCAL_CONFIG));
        LogUtil.i(TAG, "RegionConfigCore [dealer] [processContent]  \u94fe\u8def\u63a2\u6d4b\u6a21\u5757---\u672c\u5730\u914d\u7f6e\u6587\u4ef6---\u89e3\u6790\u7ed3\u679c=" + strFile2Info);
        if (TextUtils.isEmpty(strFile2Info)) {
            return iStart;
        }
        try {
            RegionConfigInfo.getInstance().init(strFile2Info);
            RegionConfigInfo.getInstance().parse();
            return iStart;
        } catch (Exception unused2) {
            return 18;
        }
    }

    public int start(String str, String str2, boolean z) {
        int iIntValue;
        LogUtil.stepLog("RegionConfigCore [dealer] [processContent]  \u94fe\u8def\u63a2\u6d4b\u6a21\u5757---\u4e0b\u8f7d\u914d\u7f6e\u6587\u4ef6");
        if (TextUtils.isEmpty(this.mUrl)) {
            return 1;
        }
        HashMap map = new HashMap();
        if (!TextUtils.isEmpty(str2)) {
            map.put("Host-Type", str2);
            map.put("Host", str2);
        }
        if (!z) {
            String tag = Storage.getInstance().getTag(str);
            if (!TextUtils.isEmpty(tag)) {
                map.put("If-None-Match", tag);
                LogUtil.stepLog("RegionConfigCore [header] etag:" + tag);
            }
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
        LogUtil.stepLog("RegionConfigCore [dealer] [processContent]  \u63a2\u6d4b\u7528\u6237\u8bbe\u5907\u7684\u57fa\u672c\u4fe1\u606f---\u7ed3\u679c=" + iIntValue);
        return iIntValue;
    }
}