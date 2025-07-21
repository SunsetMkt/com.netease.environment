package com.netease.ntunisdk.base.function;

import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.GamerInterface;
import com.netease.ntunisdk.base.SdkBase;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.base.utils.LifeCycleChecker;
import com.netease.ntunisdk.base.utils.clientlog.ClientLog;
import com.netease.ntunisdk.base.utils.clientlog.ClientLogUtils;
import com.netease.ntunisdk.unilogger.global.Const;
import com.xiaomi.gamecenter.sdk.statistics.OneTrackParams;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: ClientEnter.java */
/* loaded from: classes4.dex */
public final class b {
    public static void a(SdkBase sdkBase, int i) throws JSONException {
        boolean zHasFeature = sdkBase.hasFeature(ConstProp.ENABLE_CLIENT_CHECK_REALNAME);
        boolean zHasFeature2 = sdkBase.hasFeature(ConstProp.REQUIRE_AI_DETECT);
        UniSdkUtils.i("ClientEnter", "isNoah:" + zHasFeature + ", shouldAiDetect: " + zHasFeature2);
        if (!zHasFeature2 || zHasFeature) {
            b(sdkBase, i);
            return;
        }
        ((SdkBase) SdkMgr.getInst()).saveClientLog(null, ClientLogUtils.objArgs2Json(new String[]{OneTrackParams.XMSdkParams.STEP, ClientLog.Step.AIDETECT_START}).toString());
        LifeCycleChecker.getInst().start(LifeCycleChecker.LibTag.AIDETECT);
        try {
            GamerInterface inst = SdkMgr.getInst();
            StringBuilder sb = new StringBuilder();
            sb.append(System.currentTimeMillis());
            String propStr = inst.getPropStr(ConstProp.AI_GLDT_TIMESTAMP, sb.toString());
            JSONObject jSONObject = new JSONObject();
            jSONObject.putOpt("methodId", "aiDetect4Login");
            jSONObject.putOpt("token", SdkMgr.getInst().getPropStr(ConstProp.AI_GLDT_TOKEN));
            jSONObject.putOpt("timestamp", Long.valueOf(Long.parseLong(propStr)));
            jSONObject.putOpt(Const.CONFIG_KEY.ALL, new JSONObject(SdkMgr.getInst().getPropStr(ConstProp.AI_GLDT_ALL)));
            jSONObject.putOpt("loginDoneCode", Integer.valueOf(i));
            SdkMgr.getInst().ntExtendFunc(jSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void b(SdkBase sdkBase, int i) throws JSONException {
        if (!sdkBase.hasFeature("checkRealname")) {
            sdkBase.jfCheckRealNameDone(i);
            return;
        }
        LifeCycleChecker.getInst().start(LifeCycleChecker.LibTag.CHECK_ENTER);
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.putOpt("methodId", "checkRealnameWithSn");
            jSONObject.putOpt("clientLoginSn", sdkBase.getPropStr(ConstProp.CLIENT_LOGIN_SN));
            jSONObject.putOpt("aid", Integer.valueOf(sdkBase.getPropInt(ConstProp.USERINFO_AID, 0)));
            jSONObject.putOpt("sdkUid", sdkBase.getPropStr(ConstProp.UID));
            jSONObject.putOpt("roleId", sdkBase.getPropStr(ConstProp.USERINFO_UID, "0"));
            jSONObject.putOpt("hostId", Integer.valueOf(sdkBase.getPropInt(ConstProp.USERINFO_HOSTID, 0)));
            jSONObject.putOpt("username", sdkBase.getPropStr(ConstProp.FULL_UID));
            jSONObject.putOpt("platform", sdkBase.getPlatform());
            sdkBase.ntExtendFunc(jSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}