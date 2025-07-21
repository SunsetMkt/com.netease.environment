package com.netease.ntunisdk.base.model;

import android.text.TextUtils;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.constant.a;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class SdkAccount extends SdkState {
    public String sdkuid;
    public String token;

    public SdkAccount() {
    }

    public SdkAccount(String str) {
        super(str);
    }

    @Override // com.netease.ntunisdk.base.model.SdkState, com.netease.ntunisdk.base.model.SdkModel
    public void fromJson(JSONObject jSONObject) {
        super.fromJson(jSONObject);
        this.sdkuid = jSONObject.optString("sdkuid");
        this.token = jSONObject.optString("token");
    }

    @Override // com.netease.ntunisdk.base.model.SdkState, com.netease.ntunisdk.base.model.SdkModel
    public JSONObject toJson() throws JSONException {
        JSONObject json = super.toJson();
        try {
            json.putOpt("sdkuid", this.sdkuid);
            json.putOpt("token", this.token);
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
            this.subcode = SdkMgr.getInst().getPropInt(ConstProp.UNISDK_LOGIN_CHANNEL_RAW_CODE, 0);
            if (obj instanceof Integer) {
                int iIntValue = ((Integer) obj).intValue();
                if (iIntValue != 0) {
                    if (iIntValue == 1) {
                        this.code = a.Cancel.ordinal();
                        this.message = a.Cancel.d;
                        return;
                    }
                    if (iIntValue == 12) {
                        this.code = a.NeedRelogin.ordinal();
                        this.message = a.NeedRelogin.d;
                        return;
                    } else if (iIntValue != 13 && iIntValue != 130) {
                        this.code = a.Fail.ordinal();
                        String propStr = SdkMgr.getInst().getPropStr(ConstProp.UNISDK_LOGIN_ERR_MSG, null);
                        if (!TextUtils.isEmpty(propStr)) {
                            this.message = propStr;
                            return;
                        } else {
                            this.message = a.Fail.d;
                            return;
                        }
                    }
                }
                this.code = a.Suc.ordinal();
                this.message = a.Suc.d;
                this.sdkuid = SdkMgr.getInst().getPropStr(ConstProp.UID);
                this.token = SdkMgr.getInst().getPropStr(ConstProp.UNISDK_JF_GAS_TOKEN);
            }
        }
    }
}