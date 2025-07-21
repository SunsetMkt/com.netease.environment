package com.netease.ntunisdk.base.model;

import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.SdkMgr;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class SdkInit extends SdkState {
    public String appchannel;
    public String appid;
    public String gameid;

    public SdkInit() {
    }

    public SdkInit(String str) {
        super(str);
    }

    @Override // com.netease.ntunisdk.base.model.SdkState, com.netease.ntunisdk.base.model.SdkModel
    public void fromJson(JSONObject jSONObject) {
        super.fromJson(jSONObject);
        this.appid = jSONObject.optString("appid");
        this.appchannel = jSONObject.optString("appchannel");
        this.gameid = jSONObject.optString("gameid");
    }

    @Override // com.netease.ntunisdk.base.model.SdkState, com.netease.ntunisdk.base.model.SdkModel
    public JSONObject toJson() throws JSONException {
        JSONObject json = super.toJson();
        try {
            json.putOpt("appid", this.appid);
            json.putOpt("appchannel", this.appchannel);
            json.putOpt("gameid", this.gameid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override // com.netease.ntunisdk.base.model.SdkState, com.netease.ntunisdk.base.model.SdkModel
    public Object wrapTo() {
        return super.wrapTo();
    }

    @Override // com.netease.ntunisdk.base.model.SdkState, com.netease.ntunisdk.base.model.SdkModel
    public void wrapFrom(Object obj) {
        super.wrapFrom(obj);
        if (SdkMgr.getInst() != null) {
            this.appid = SdkMgr.getInst().getPropStr(ConstProp.APPID);
            this.appchannel = SdkMgr.getInst().getAppChannel();
            this.gameid = SdkMgr.getInst().getPropStr(ConstProp.GAMEID, SdkMgr.getInst().getPropStr("JF_GAMEID"));
        }
    }
}