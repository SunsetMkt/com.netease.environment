package com.netease.ntunisdk;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import com.facebook.react.uimanager.ViewProps;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.OnFinishInitListener;
import com.netease.ntunisdk.base.OrderInfo;
import com.netease.ntunisdk.base.SdkBase;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.cutout.RespUtil;
import com.netease.ntunisdk.util.cutout.CutoutUtil;
import com.netease.ntunisdk.util.cutout.SingleScreenFoldingUtil;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class SdkCutout extends SdkBase {
    private static final String CHANNEL = "cutout";
    private static final String DISPLAY_NOTCH_STATUS = "display_notch_status";
    private static final String METHOD_GET_CUTOUT_INFO = "getCutoutInfo";
    private static final String METHOD_GET_HUAWEI_NOTCH_STATE = "getHwNotchStatus";
    private static final String METHOD_GET_WATERFALL_INFO = "getWaterfallInfo";
    private static final String TAG = "SdkCutout";
    private static final String VER = "1.9";
    private static final Object globalResultLock = new Object();
    private volatile String getCutoutInfoJsonCache;
    private volatile Boolean globalResult;

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
        return VER;
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

    public SdkCutout(Context context) {
        super(context);
        this.globalResult = null;
        setFeature(ConstProp.INNER_MODE_NO_PAY, true);
        setFeature(ConstProp.INNER_MODE_SECOND_CHANNEL, true);
    }

    public Boolean getGlobalResult(String str) {
        Boolean bool;
        synchronized (globalResultLock) {
            if (this.globalResult == null) {
                this.getCutoutInfoJsonCache = str;
            }
            bool = this.globalResult;
        }
        return bool;
    }

    public String setGlobalResult(Boolean bool) {
        String str;
        synchronized (globalResultLock) {
            this.globalResult = bool;
            str = this.getCutoutInfoJsonCache;
            this.getCutoutInfoJsonCache = null;
        }
        return str;
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void init(OnFinishInitListener onFinishInitListener) {
        UniSdkUtils.i(TAG, "init");
        initCutoutUtil();
        if (onFinishInitListener != null) {
            onFinishInitListener.finishInit(0);
        }
    }

    private void initCutoutUtil() {
        CutoutUtil.initUtil((Activity) this.myCtx, new SingleScreenFoldingUtil.OnInitFinishLister() { // from class: com.netease.ntunisdk.SdkCutout.1
            @Override // com.netease.ntunisdk.util.cutout.SingleScreenFoldingUtil.OnInitFinishLister
            public void onFinish(boolean z) throws JSONException {
                String globalResult = SdkCutout.this.setGlobalResult(Boolean.valueOf(z));
                if (globalResult != null) {
                    SdkCutout.this.extendFunc(globalResult);
                }
            }
        });
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    protected String getUniSDKVersion() {
        return getSDKVersion();
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void extendFunc(String str) throws JSONException {
        UniSdkUtils.i(TAG, "extendFunc: " + str);
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (CHANNEL.equals(jSONObject.optString("channel"))) {
                String strOptString = jSONObject.optString("methodId");
                Activity activity = (Activity) this.myCtx;
                int i = 0;
                jSONObject.put(RespUtil.UniSdkField.RESP_CODE, 0);
                jSONObject.put(RespUtil.UniSdkField.RESP_MSG, RespUtil.parseRespCode(0));
                if (METHOD_GET_CUTOUT_INFO.equalsIgnoreCase(strOptString)) {
                    Boolean globalResult = getGlobalResult(str);
                    if (globalResult == null) {
                        UniSdkUtils.d(TAG, "extendFunc -> need to wait for the result of init");
                        return;
                    }
                    jSONObject.putOpt("hasCutout", Boolean.valueOf(CutoutUtil.hasCutout(activity)));
                    int[] cutoutWidthHeight = CutoutUtil.getCutoutWidthHeight(activity);
                    jSONObject.putOpt("initResult", globalResult);
                    jSONObject.putOpt("width", Integer.valueOf(cutoutWidthHeight[0]));
                    jSONObject.putOpt("height", Integer.valueOf(cutoutWidthHeight[1]));
                    int[] cutoutPosition = CutoutUtil.getCutoutPosition(activity);
                    jSONObject.putOpt(ViewProps.LEFT, Integer.valueOf(cutoutPosition[0]));
                    jSONObject.putOpt(ViewProps.TOP, Integer.valueOf(cutoutPosition[1]));
                    jSONObject.putOpt(ViewProps.RIGHT, Integer.valueOf(cutoutPosition[2]));
                    jSONObject.putOpt(ViewProps.BOTTOM, Integer.valueOf(cutoutPosition[3]));
                } else if (METHOD_GET_HUAWEI_NOTCH_STATE.equalsIgnoreCase(strOptString)) {
                    try {
                        i = Settings.Secure.getInt(this.myCtx.getContentResolver(), DISPLAY_NOTCH_STATUS, 0);
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                    jSONObject.putOpt("result", Integer.valueOf(i));
                } else if (METHOD_GET_WATERFALL_INFO.equalsIgnoreCase(strOptString)) {
                    jSONObject.putOpt("hasWaterfall", Boolean.valueOf(CutoutUtil.hasWaterfall(activity)));
                    int[] safeInset = CutoutUtil.getSafeInset(activity);
                    jSONObject.putOpt("safeLeft", Integer.valueOf(safeInset[0]));
                    jSONObject.putOpt("safeTop", Integer.valueOf(safeInset[1]));
                    jSONObject.putOpt("safeRight", Integer.valueOf(safeInset[2]));
                    jSONObject.putOpt("safeBottom", Integer.valueOf(safeInset[3]));
                } else {
                    jSONObject.put(RespUtil.UniSdkField.RESP_CODE, 1);
                    jSONObject.put(RespUtil.UniSdkField.RESP_MSG, RespUtil.parseRespCode(1));
                }
                extendFuncCall(jSONObject.toString());
            }
        } catch (Throwable th2) {
            th2.printStackTrace();
            try {
                JSONObject jSONObject2 = new JSONObject(str);
                jSONObject2.put(RespUtil.UniSdkField.RESP_CODE, RespUtil.RespCode.EXCEPTION);
                jSONObject2.put(RespUtil.UniSdkField.RESP_MSG, th2.getMessage());
                extendFuncCall(jSONObject2.toString());
            } catch (JSONException e) {
                e.printStackTrace();
                UniSdkUtils.e(TAG, "extendFunc -> e: " + e.getMessage());
            }
        }
    }
}