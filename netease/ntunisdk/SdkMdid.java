package com.netease.ntunisdk;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import com.bun.miitmdid.core.ErrorCode;
import com.bun.miitmdid.core.MdidSdkHelper;
import com.bun.miitmdid.interfaces.IIdentifierListener;
import com.bun.miitmdid.interfaces.IdSupplier;
import com.mojang.minecraftpe.SdkCallback;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.OnFinishInitListener;
import com.netease.ntunisdk.base.OrderInfo;
import com.netease.ntunisdk.base.SdkBase;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.modules.deviceinfo.GaidUtils;
import com.xiaomi.onetrack.OneTrack;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class SdkMdid extends SdkBase implements IIdentifierListener {
    private static final String CHANNEL = "mdid";
    private static final String TAG = "SdkMdid";
    private static final String VERSION = "1.0.25";

    @Override // com.netease.ntunisdk.base.SdkBase
    public void checkOrder(OrderInfo orderInfo) {
    }

    @Override // com.netease.ntunisdk.base.SdkBase, com.netease.ntunisdk.base.GamerInterface
    public String getChannel() {
        return CHANNEL;
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public String getLoginSession() {
        return null;
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public String getLoginUid() {
        return null;
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public String getSDKVersion() {
        return VERSION;
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    protected String getUniSDKVersion() {
        return "1.0.25(4)";
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void login() {
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void logout() {
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void openManager() {
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void upLoadUserInfo() {
    }

    public SdkMdid(Context context) {
        super(context);
        setFeature(ConstProp.INNER_MODE_NO_PAY, true);
        setFeature(ConstProp.INNER_MODE_SECOND_CHANNEL, true);
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void init(OnFinishInitListener onFinishInitListener) throws JSONException {
        UniSdkUtils.i(TAG, "init");
        String propStr = SdkMgr.getInst().getPropStr("JF_GAMEID", "");
        UniSdkUtils.i(TAG, "buildId: " + Build.MODEL + " ,JF_GAMEID: " + propStr);
        if (SdkCallback.JF_GAMEID.equalsIgnoreCase(propStr) && Build.VERSION.SDK_INT == 23) {
            UniSdkUtils.i(TAG, "Android 6 and x19 device not support");
            extendFuncFail(-1, "device not support");
        } else {
            int iCallFromReflect = CallFromReflect(this.myCtx);
            switch (iCallFromReflect) {
                case ErrorCode.INIT_ERROR_MANUFACTURER_NOSUPPORT /* 1008611 */:
                    UniSdkUtils.i(TAG, "manufacturer  not support");
                    extendFuncFail(iCallFromReflect, "manufacturer  not support");
                    break;
                case ErrorCode.INIT_ERROR_DEVICE_NOSUPPORT /* 1008612 */:
                    UniSdkUtils.i(TAG, "device not support");
                    extendFuncFail(iCallFromReflect, "device not support");
                    break;
                case ErrorCode.INIT_ERROR_LOAD_CONFIGFILE /* 1008613 */:
                    UniSdkUtils.i(TAG, "fail to load config");
                    extendFuncFail(iCallFromReflect, "fail to load config");
                    break;
                case ErrorCode.INIT_ERROR_RESULT_DELAY /* 1008614 */:
                    UniSdkUtils.i(TAG, "result delay(async)");
                    break;
                case ErrorCode.INIT_HELPER_CALL_ERROR /* 1008615 */:
                    UniSdkUtils.e(TAG, "call error");
                    extendFuncFail(iCallFromReflect, "call error");
                    break;
                default:
                    UniSdkUtils.i(TAG, "I don't know what the f__k it is. Not support??");
                    extendFuncFail(iCallFromReflect, "unknown");
                    break;
            }
        }
        if (onFinishInitListener != null) {
            onFinishInitListener.finishInit(0);
        }
    }

    private int CallFromReflect(Context context) {
        try {
            return MdidSdkHelper.InitSdk(context, true, this);
        } catch (Throwable th) {
            th.printStackTrace();
            return 0;
        }
    }

    @Override // com.bun.miitmdid.interfaces.IIdentifierListener
    public void OnSupport(boolean z, IdSupplier idSupplier) {
        try {
            UniSdkUtils.i(TAG, "OnSupport: " + z + "/" + idSupplier);
            if (z && idSupplier != null) {
                final String oaid = idSupplier.getOAID();
                String vaid = idSupplier.getVAID();
                String aaid = idSupplier.getAAID();
                UniSdkUtils.i(TAG, "OAID: " + oaid + "\nVAID: " + vaid + "\nAAID: " + aaid);
                SdkMgr.getInst().setPropStr(ConstProp.MSA_OAID, oaid);
                SdkMgr.getInst().setPropStr("VAID", vaid);
                SdkMgr.getInst().setPropStr("AAID", aaid);
                setPropStr(ConstProp.MSA_OAID, oaid);
                setPropStr("VAID", vaid);
                setPropStr("AAID", aaid);
                try {
                    if (SdkMgr.getInst().hasFeature("EB") && !TextUtils.isEmpty(oaid) && TextUtils.isEmpty(GaidUtils.getCachedGaid(new GaidUtils.Callback() { // from class: com.netease.ntunisdk.SdkMdid.1
                        @Override // com.netease.ntunisdk.modules.deviceinfo.GaidUtils.Callback
                        public void done(String str) {
                            if (!TextUtils.isEmpty(str) || TextUtils.isEmpty(oaid)) {
                                return;
                            }
                            SdkMdid.this.resetCachedOaid(oaid);
                        }
                    }))) {
                        resetCachedOaid(oaid);
                    }
                } catch (Throwable th) {
                    th.printStackTrace();
                    SdkMgr.getInst().setPropStr("OAID", oaid);
                    setPropStr("OAID", oaid);
                }
                try {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.putOpt("methodId", "oaidCallback");
                    jSONObject.putOpt("suc", true);
                    jSONObject.putOpt(OneTrack.Param.OAID, oaid);
                    jSONObject.putOpt("vaid", vaid);
                    jSONObject.putOpt("aaid", aaid);
                    SdkMgr.getInst().ntExtendFunc(jSONObject.toString());
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            extendFuncFail(-1, "api not support");
        } catch (Throwable th2) {
            th2.printStackTrace();
        }
    }

    private void extendFuncFail(int i, String str) throws JSONException {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.putOpt("methodId", "oaidCallback");
            jSONObject.putOpt("suc", false);
            jSONObject.putOpt("code", Integer.valueOf(i));
            jSONObject.putOpt("msg", str);
            SdkMgr.getInst().ntExtendFunc(jSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetCachedOaid(String str) {
        this.myCtx.getSharedPreferences("GaidUtils", 0).edit().putString("gaid_cached", str).commit();
    }
}