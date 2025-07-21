package com.netease.ntunisdk.base.utils.clientlog;

import android.content.Context;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.modules.deviceinfo.DeviceUtils;
import java.util.Locale;
import java.util.Random;

/* loaded from: classes5.dex */
public class ClientLog {
    public static final String TAG = "UniSDK ClientLog";
    private static volatile ClientLog sClientLog;
    private String mUniTransactionId = "";

    public static class Step {
        public static final String AIDETECT_DONE = "aidetect_done";
        public static final String AIDETECT_START = "aidetect_start";
        public static final String LOGIN_DONE_BASE = "loginDone_base";
        public static final String PROTOCOL_DONE = "protocol_done";
        public static final String PROTOCOL_START = "protocol_start";
        public static final String UNI_SAUTH = "uni_sauth";
    }

    private ClientLog() {
    }

    public static ClientLog getInst() {
        if (sClientLog == null) {
            synchronized (ClientLog.class) {
                if (sClientLog == null) {
                    sClientLog = new ClientLog();
                }
            }
        }
        return sClientLog;
    }

    public void startUniTransaction(Context context) {
        if (context == null) {
            return;
        }
        this.mUniTransactionId = genUniTransactionId(context);
        UniSdkUtils.d(TAG, "startUniTransaction: " + this.mUniTransactionId);
    }

    public String getUniTransactionId() {
        return this.mUniTransactionId;
    }

    public String genUniTransactionId(Context context) {
        return String.format("%s_%s_%s", DeviceUtils.getDeviceUDID(context, SdkMgr.getInst().hasFeature("NO_ANDROIDID"), SdkMgr.getInst().hasFeature("ENABLE_FAKE_ABOUT_ID")), Long.valueOf(System.currentTimeMillis()), String.format(Locale.US, "%09d", Integer.valueOf(new Random().nextInt(1000000000))));
    }
}