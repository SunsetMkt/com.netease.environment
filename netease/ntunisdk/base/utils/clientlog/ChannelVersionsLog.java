package com.netease.ntunisdk.base.utils.clientlog;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.facebook.hermes.intl.Constants;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.SdkBase;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.base.utils.HTTPCallback;
import com.xiaomi.gamecenter.sdk.statistics.OneTrackParams;
import java.util.Iterator;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class ChannelVersionsLog {
    private Context context;
    private SdkBase sdkBase;
    private SharedPreferences sp;
    private final String TAG = "ChannelVersionsLog";
    private final String ALL_CHANNEL_VERSIONS_CLIENT_LOG = "ALL_CHANNEL_VERSIONS_CLIENT_LOG";

    public ChannelVersionsLog(Context context) {
        this.context = context;
    }

    public void send() {
        try {
            SdkBase sdkBase = (SdkBase) SdkMgr.getInst();
            this.sdkBase = sdkBase;
            if (sdkBase == null || this.context == null || sdkBase.getPropInt(ConstProp.SEND_UNISDK_VERSION, -1) == 0) {
                return;
            }
            if (this.sp == null) {
                this.sp = this.context.getSharedPreferences(this.TAG, 0);
            }
            final JSONObject jSONObject = new JSONObject();
            jSONObject.put(Constants.SENSITIVITY_BASE, SdkMgr.getBaseVersion());
            jSONObject.put(SdkMgr.getInst().getChannel(), SdkMgr.getInst().getSDKVersion(SdkMgr.getInst().getChannel()));
            Iterator<String> it = this.sdkBase.getLoginSdkInstMap().keySet().iterator();
            while (it.hasNext()) {
                SdkBase sdkBase2 = this.sdkBase.getLoginSdkInstMap().get(it.next());
                jSONObject.put(sdkBase2.getChannel(), sdkBase2.getSDKVersion());
            }
            Iterator<String> it2 = this.sdkBase.getSdkInstMap().keySet().iterator();
            while (it2.hasNext()) {
                SdkBase sdkBase3 = this.sdkBase.getSdkInstMap().get(it2.next());
                jSONObject.put(sdkBase3.getChannel(), sdkBase3.getSDKVersion());
            }
            if (this.sp.getString("ALL_CHANNEL_VERSIONS_CLIENT_LOG", "").equals(jSONObject.toString())) {
                return;
            }
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.putOpt(OneTrackParams.XMSdkParams.STEP, "get_channelversions");
            jSONObject2.putOpt("channelversions", jSONObject);
            this.sdkBase.saveClientLog(null, jSONObject2.toString(), new HTTPCallback() { // from class: com.netease.ntunisdk.base.utils.clientlog.ChannelVersionsLog.1
                @Override // com.netease.ntunisdk.base.utils.HTTPCallback
                public boolean processResult(String str, String str2) {
                    UniSdkUtils.i(ChannelVersionsLog.this.TAG, "ChannelVersionsLog result: ".concat(String.valueOf(str)));
                    if (TextUtils.isEmpty(str)) {
                        return false;
                    }
                    try {
                        if (200 == new JSONObject(str).optInt("code")) {
                            ((Activity) ChannelVersionsLog.this.context).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.utils.clientlog.ChannelVersionsLog.1.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    SharedPreferences.Editor editorEdit = ChannelVersionsLog.this.sp.edit();
                                    editorEdit.putString("ALL_CHANNEL_VERSIONS_CLIENT_LOG", jSONObject.toString());
                                    editorEdit.commit();
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}