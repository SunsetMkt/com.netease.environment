package com.netease.ntunisdk.base;

import android.text.TextUtils;
import com.netease.ntunisdk.base.utils.ApiRequestUtil;
import com.netease.ntunisdk.base.utils.CachedThreadPoolUtil;
import com.netease.ntunisdk.base.utils.HTTPCallback;
import com.netease.ntunisdk.base.utils.HTTPQueue;
import java.util.HashMap;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class PayChannelManager {

    /* renamed from: a */
    private SdkBase f1611a;
    private boolean b;
    private boolean c;

    static /* synthetic */ boolean b(PayChannelManager payChannelManager) {
        payChannelManager.c = true;
        return true;
    }

    public PayChannelManager(SdkBase sdkBase) {
        this.f1611a = sdkBase;
    }

    /* renamed from: com.netease.ntunisdk.base.PayChannelManager$1 */
    final class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public final void run() throws Throwable {
            PayChannelManager.a(PayChannelManager.this);
        }
    }

    public void initAsync() {
        UniSdkUtils.d("UniSDK PCM", "initAsync");
        CachedThreadPoolUtil.getInstance().exec(new Runnable() { // from class: com.netease.ntunisdk.base.PayChannelManager.1
            AnonymousClass1() {
            }

            @Override // java.lang.Runnable
            public final void run() throws Throwable {
                PayChannelManager.a(PayChannelManager.this);
            }
        });
    }

    public boolean allyPayEnabled() {
        if (a()) {
            return true;
        }
        return this.b;
    }

    public boolean payDisabled() {
        if (a()) {
            return false;
        }
        return this.c;
    }

    /* renamed from: com.netease.ntunisdk.base.PayChannelManager$2 */
    final class AnonymousClass2 implements HTTPCallback {

        /* renamed from: a */
        final /* synthetic */ String f1613a;

        AnonymousClass2(String str) {
            str = str;
        }

        @Override // com.netease.ntunisdk.base.utils.HTTPCallback
        public final boolean processResult(String str, String str2) {
            UniSdkUtils.d("UniSDK PCM", "getPayChannelFromJF Result : ".concat(String.valueOf(str)));
            try {
                JSONObject jSONObject = new JSONObject(str);
                if (jSONObject.optInt("code") == 200) {
                    String strOptString = jSONObject.optString("ff_channel");
                    if ("allysdk".equals(strOptString)) {
                        PayChannelManager.this.b = true;
                    } else if (str.equals(strOptString)) {
                        PayChannelManager.this.b = false;
                    } else {
                        PayChannelManager.b(PayChannelManager.this);
                    }
                } else {
                    PayChannelManager.b(PayChannelManager.this);
                }
            } catch (Exception unused) {
                PayChannelManager.b(PayChannelManager.this);
            }
            return false;
        }
    }

    private boolean a() {
        return "allysdk".equals(this.f1611a.getPropStr("FF_CHANNEL_BY_GAME"));
    }

    static /* synthetic */ void a(PayChannelManager payChannelManager) throws Throwable {
        String propStr = payChannelManager.f1611a.getPropStr(ConstProp.UNISDK_JF_GAS3_URL);
        if (TextUtils.isEmpty(propStr)) {
            UniSdkUtils.d("UniSDK PCM", "UNISDK_JF_GAS3_URL empty");
            return;
        }
        StringBuilder sb = new StringBuilder(propStr);
        if (propStr.endsWith("/")) {
            sb.append("get_ff_channel?login_channel=%s&support_ff_channels=%s,allysdk");
        } else {
            sb.append("/get_ff_channel?login_channel=%s&support_ff_channels=%s,allysdk");
        }
        String channel = payChannelManager.f1611a.getChannel();
        String str = String.format(sb.toString(), channel, channel);
        UniSdkUtils.d("UniSDK PCM", "getPayChannelFromJF url = ".concat(String.valueOf(str)));
        HTTPQueue.QueueItem queueItemNewQueueItem = HTTPQueue.NewQueueItem();
        queueItemNewQueueItem.method = "GET";
        queueItemNewQueueItem.url = str;
        queueItemNewQueueItem.callback = new HTTPCallback() { // from class: com.netease.ntunisdk.base.PayChannelManager.2

            /* renamed from: a */
            final /* synthetic */ String f1613a;

            AnonymousClass2(String channel2) {
                str = channel2;
            }

            @Override // com.netease.ntunisdk.base.utils.HTTPCallback
            public final boolean processResult(String str2, String str22) {
                UniSdkUtils.d("UniSDK PCM", "getPayChannelFromJF Result : ".concat(String.valueOf(str2)));
                try {
                    JSONObject jSONObject = new JSONObject(str2);
                    if (jSONObject.optInt("code") == 200) {
                        String strOptString = jSONObject.optString("ff_channel");
                        if ("allysdk".equals(strOptString)) {
                            PayChannelManager.this.b = true;
                        } else if (str.equals(strOptString)) {
                            PayChannelManager.this.b = false;
                        } else {
                            PayChannelManager.b(PayChannelManager.this);
                        }
                    } else {
                        PayChannelManager.b(PayChannelManager.this);
                    }
                } catch (Exception unused) {
                    PayChannelManager.b(PayChannelManager.this);
                }
                return false;
            }
        };
        String propStr2 = SdkMgr.getInst().getPropStr(ConstProp.JF_LOG_KEY);
        if (!TextUtils.isEmpty(propStr2)) {
            HashMap map = new HashMap();
            try {
                ApiRequestUtil.addSecureHeader(map, propStr2, queueItemNewQueueItem.method, str, "", false);
            } catch (Exception e) {
                UniSdkUtils.d("UniSDK PCM", "hmacSHA256Signature exception:" + e.getMessage());
            }
            queueItemNewQueueItem.setHeaders(map);
        } else {
            UniSdkUtils.d("UniSDK PCM", "JF_CLIENT_KEY empty");
        }
        HTTPQueue.getInstance(HTTPQueue.HTTPQUEUE_PAY).addItem(queueItemNewQueueItem);
    }
}