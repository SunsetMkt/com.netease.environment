package com.netease.ntunisdk.base.utils.clientlog;

import android.content.Context;
import com.netease.mcount.ClientLogAgent;
import com.netease.mcount.DRPFAgent;
import com.netease.ntunisdk.base.SdkMgr;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class MCountProxy {
    private static final boolean IS_MC_CL_EXIST = ClientLogUtils.isClassInstalled("com.netease.mcount.ClientLogAgent");
    private static final boolean IS_MC_DRPF_EXIST = ClientLogUtils.isClassInstalled("com.netease.mcount.DRPFAgent");
    private static volatile MCountProxy sMCountProxy;

    private MCountProxy() {
    }

    public static MCountProxy getInst() {
        if (sMCountProxy == null) {
            synchronized (MCountProxy.class) {
                if (sMCountProxy == null) {
                    sMCountProxy = new MCountProxy();
                }
            }
        }
        return sMCountProxy;
    }

    public void init(Context context) {
        if (IS_MC_DRPF_EXIST) {
            DRPFAgent.getInst().init(context);
        }
    }

    public boolean sendDrpf(JSONObject jSONObject, String str, String str2, String str3) {
        if (!hasMCDRPFEnabled()) {
            return false;
        }
        DRPFAgent.getInst().sendDrpf(jSONObject, str, str2, str3, false);
        return true;
    }

    private boolean hasMCDRPFEnabled() {
        return IS_MC_DRPF_EXIST && !DRPFAgent.getInst().isDisabled();
    }

    public boolean saveClientLog(Context context, String str, boolean z) {
        if (!hasMCClientLogEnabled()) {
            return false;
        }
        ClientLogAgent.getInst().saveClientLog(context, str, z);
        return true;
    }

    private boolean hasMCClientLogEnabled() {
        return hasMCClientLogInited() && !ClientLogAgent.getInst().isDisabled();
    }

    private boolean hasMCClientLogInited() {
        if (IS_MC_CL_EXIST) {
            return ClientLogUtils.isClassInstalled("com.netease.ntunisdk.SdkNetease") || SdkMgr.getInst().hasFeature("MODE_HAS_MCOUNT_CLIENT_LOG_INITED");
        }
        return false;
    }
}