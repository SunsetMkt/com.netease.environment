package com.netease.ntunisdk.base.model;

import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.SdkMgr;

/* loaded from: classes5.dex */
public class SdkStage {
    public String stage;

    public SdkStage(String str) {
        this.stage = str;
    }

    public void wrap() {
        if (SdkMgr.getInst() != null) {
            if ("createRole".equalsIgnoreCase(this.stage)) {
                SdkMgr.getInst().setPropStr(ConstProp.USERINFO_STAGE, ConstProp.USERINFO_STAGE_CREATE_ROLE);
            } else if ("enterGame".equalsIgnoreCase(this.stage)) {
                SdkMgr.getInst().setPropStr(ConstProp.USERINFO_STAGE, ConstProp.USERINFO_STAGE_ENTER_SERVER);
            } else if ("levelUp".equalsIgnoreCase(this.stage)) {
                SdkMgr.getInst().setPropStr(ConstProp.USERINFO_STAGE, ConstProp.USERINFO_STAGE_LEVEL_UP);
            }
        }
    }

    public String toString() {
        return "SdkStage{stage='" + this.stage + "'}";
    }
}