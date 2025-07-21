package com.netease.download.network;

import com.netease.download.util.LogUtil;
import com.netease.ntunisdk.okhttp3.OkHttpClient;

/* loaded from: classes5.dex */
public class OkHttpProxy {
    private static final String TAG = "OkHttpProxy";
    private static OkHttpProxy sOkHttpProxy;
    private OkHttpClient sOkHttpClient = null;

    private OkHttpProxy() {
    }

    public static OkHttpProxy getInstance() {
        if (sOkHttpProxy == null) {
            LogUtil.i(TAG, "OkHttpProxy [getInstance] sOkHttpProxy create");
            sOkHttpProxy = new OkHttpProxy();
        }
        return sOkHttpProxy;
    }

    /* JADX WARN: Removed duplicated region for block: B:111:0x0112  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x01e9  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x021c A[Catch: Exception -> 0x022d, all -> 0x02be, TRY_LEAVE, TryCatch #2 {Exception -> 0x022d, blocks: (B:122:0x01b0, B:127:0x01ee, B:128:0x021c, B:121:0x0173), top: B:155:0x0173 }] */
    /* JADX WARN: Removed duplicated region for block: B:131:0x0229 A[PHI: r13 r14
  0x0229: PHI (r13v3 int) = (r13v0 int), (r13v7 int) binds: [B:139:0x026e, B:130:0x0227] A[DONT_GENERATE, DONT_INLINE]
  0x0229: PHI (r14v2 com.netease.ntunisdk.okhttp3.Response) = (r14v1 com.netease.ntunisdk.okhttp3.Response), (r14v11 com.netease.ntunisdk.okhttp3.Response) binds: [B:139:0x026e, B:130:0x0227] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:142:0x0273  */
    /* JADX WARN: Removed duplicated region for block: B:143:0x0286  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int execute_syn(com.netease.ntunisdk.okhttp3.Request.Builder r20, com.netease.ntunisdk.okhttp3.Callback r21) {
        /*
            Method dump skipped, instructions count: 710
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.download.network.OkHttpProxy.execute_syn(com.netease.ntunisdk.okhttp3.Request$Builder, com.netease.ntunisdk.okhttp3.Callback):int");
    }

    public void clean() {
        LogUtil.i(TAG, "OkHttpProxy [clean] start");
        this.sOkHttpClient = null;
    }
}