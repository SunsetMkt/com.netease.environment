package com.netease.ntunisdk.base.function;

import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.SdkBase;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.base.utils.LifeCycleChecker;
import com.netease.ntunisdk.base.utils.clientlog.ClientLog;
import com.netease.ntunisdk.base.utils.clientlog.ClientLogUtils;
import com.netease.rnccplayer.VideoViewManager;
import com.xiaomi.gamecenter.sdk.statistics.OneTrackParams;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: LoginProtocol.java */
/* loaded from: classes4.dex */
public final class j {
    public static void a(SdkBase sdkBase, int i) throws JSONException {
        boolean zHasFeature = sdkBase.hasFeature(ConstProp.SHOW_PROTOCOL_IN_LOGIN);
        UniSdkUtils.i("LoginProtocol", "login protocol: ".concat(String.valueOf(zHasFeature)));
        if (zHasFeature) {
            sdkBase.saveClientLog(null, ClientLogUtils.objArgs2Json(new String[]{OneTrackParams.XMSdkParams.STEP, ClientLog.Step.PROTOCOL_START}).toString());
            LifeCycleChecker.getInst().start("protocol");
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.putOpt("methodId", "showProtocolInLogin");
                jSONObject.putOpt(VideoViewManager.PROP_SRC, "login");
                jSONObject.putOpt("uid", sdkBase.getPropStr(ConstProp.UID, ""));
                sdkBase.setPropStr(ConstProp.PROTOCOL_IN_LOGIN_SRC, "login");
                SdkMgr.getInst().ntExtendFunc(jSONObject.toString());
                return;
            } catch (Exception unused) {
                return;
            }
        }
        b.a(sdkBase, i);
    }
}