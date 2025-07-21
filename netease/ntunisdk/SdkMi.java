package com.netease.ntunisdk;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import com.alipay.sdk.m.x.d;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.OnFinishInitListener;
import com.netease.ntunisdk.base.OrderInfo;
import com.netease.ntunisdk.base.SdkBase;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.cutout.RespUtil;
import com.xiaomi.gamecenter.sdk.MiCommplatform;
import com.xiaomi.gamecenter.sdk.OnExitListner;
import com.xiaomi.gamecenter.sdk.OnLoginProcessListener;
import com.xiaomi.gamecenter.sdk.OnPayProcessListener;
import com.xiaomi.gamecenter.sdk.entry.MiAccountInfo;
import com.xiaomi.gamecenter.sdk.entry.MiBuyInfo;
import com.xiaomi.gamecenter.sdk.entry.MiReportOrder;
import com.xiaomi.gamecenter.sdk.statistics.OneTrackParams;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class SdkMi extends SdkBase {
    private static final String CHANNEL = "xiaomi_app";
    private static final String TAG = "UniSDK xiaomi_app";
    private static final String VERSION = "3.3.0.7";
    private MiCommplatform sdkInstance;

    @Override // com.netease.ntunisdk.base.SdkBase, com.netease.ntunisdk.base.GamerInterface
    public String getChannel() {
        return "xiaomi_app";
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public String getSDKVersion() {
        return "3.3.0.7";
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public String getUniSDKVersion() {
        return "3.3.0.7";
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void openManager() {
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void upLoadUserInfo() {
    }

    public SdkMi(Context context) {
        super(context);
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void init(OnFinishInitListener onFinishInitListener) throws JSONException {
        boolean z;
        UniSdkUtils.d(TAG, "begin init ... ");
        setFeature(ConstProp.MODE_EXIT_VIEW, true);
        setFeature(ConstProp.MODE_NEED_UNITED_LOGIN, true);
        JSONObject jSONObject = new JSONObject();
        this.sdkInstance = MiCommplatform.getInstance();
        MiCommplatform miCommplatform = this.sdkInstance;
        if (miCommplatform != null) {
            miCommplatform.onUserAgreed((Activity) this.myCtx);
            onFinishInitListener.finishInit(0);
            z = true;
        } else {
            onFinishInitListener.finishInit(1);
            z = false;
        }
        try {
            jSONObject.putOpt(OneTrackParams.XMSdkParams.STEP, "onInitDone");
            jSONObject.putOpt("func", "init");
            jSONObject.putOpt("unisdk_code", Integer.valueOf(z ? 0 : 1));
        } catch (JSONException e) {
            UniSdkUtils.d(TAG, "extraJson:" + e.getMessage());
        }
        saveClientLog(null, jSONObject.toString());
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void login() throws JSONException {
        UniSdkUtils.d(TAG, "begin login ... ");
        MiCommplatform miCommplatform = this.sdkInstance;
        if (miCommplatform == null) {
            UniSdkUtils.e(TAG, "SDK is uninitialized!");
            return;
        }
        miCommplatform.miLogin((Activity) this.myCtx, new OnLoginProcessListener() { // from class: com.netease.ntunisdk.SdkMi.1
            @Override // com.xiaomi.gamecenter.sdk.OnLoginProcessListener
            public void finishLoginProcess(int i, MiAccountInfo miAccountInfo) throws JSONException {
                UniSdkUtils.d(SdkMi.TAG, "finishLoginProcess,code = " + i);
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.putOpt("func", "finishLoginProcess");
                    jSONObject.putOpt(OneTrackParams.XMSdkParams.STEP, "loginDone");
                    jSONObject.putOpt("raw_code", Integer.valueOf(i));
                    if (i != -18006) {
                        if (i == -12) {
                            SdkMi.this.loginDone(1);
                            jSONObject.putOpt("unisdk_code", 1);
                            SdkMi.this.saveClientLog(null, jSONObject.toString());
                            return;
                        }
                        if (i == 0) {
                            String strValueOf = String.valueOf(miAccountInfo.getUid());
                            String sessionId = miAccountInfo.getSessionId();
                            UniSdkUtils.i(SdkMi.TAG, "uid : " + strValueOf);
                            UniSdkUtils.i(SdkMi.TAG, "session : " + sessionId);
                            SdkMi.this.setPropStr(ConstProp.UID, strValueOf);
                            SdkMi.this.setPropStr(ConstProp.SESSION, sessionId);
                            String nikename = miAccountInfo.getNikename();
                            UniSdkUtils.i(SdkMi.TAG, "nikename : " + nikename);
                            SdkMi.this.setPropStr("MI_DISPLAY_NAME", nikename);
                            SdkMi.this.setPropStr(ConstProp.USR_NAME, nikename);
                            SdkMi.this.setLoginStat(1);
                            JSONObject jSONObject2 = new JSONObject();
                            try {
                                jSONObject2.put("methodId", "unisdkChannelAASRoleQuit");
                                jSONObject2.put("code", 12);
                                jSONObject2.put(OneTrackParams.XMSdkParams.STEP, "loginDone");
                                SdkMi.this.ntExtendFunc(jSONObject2.toString());
                            } catch (JSONException e) {
                                UniSdkUtils.d(SdkMi.TAG, "unisdkChannelAASRoleQuit JSONException:" + e.getMessage());
                            }
                            SdkMi.this.loginDone(0);
                            jSONObject.putOpt("unisdk_code", 0);
                            SdkMi.this.saveClientLog(null, jSONObject.toString());
                            return;
                        }
                        SdkMi.this.resetCommonProp();
                        SdkMi.this.loginDone(10);
                        jSONObject.putOpt("unisdk_code", 10);
                        SdkMi.this.saveClientLog(null, jSONObject.toString());
                    }
                } catch (Exception e2) {
                    UniSdkUtils.d(SdkMi.TAG, "extraJson:" + e2.getMessage());
                }
            }
        });
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.putOpt(OneTrackParams.XMSdkParams.STEP, "login_start");
            jSONObject.putOpt("func", "sdkInstance.miLogin");
        } catch (JSONException e) {
            UniSdkUtils.d(TAG, "extraJson:" + e.getMessage());
        }
        saveClientLog(null, jSONObject.toString());
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public String getLoginSession() {
        return hasLogin() ? getPropStr(ConstProp.SESSION) : ConstProp.S_NOT_LOGIN_SESSION;
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public String getLoginUid() {
        return !hasLogin() ? "" : getPropStr(ConstProp.UID);
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void checkOrder(final OrderInfo orderInfo) throws JSONException {
        UniSdkUtils.d(TAG, "begin checkOrder ... ");
        if (this.sdkInstance == null) {
            UniSdkUtils.e(TAG, "SDK is uninitialized!");
            return;
        }
        MiBuyInfo miBuyInfo = new MiBuyInfo();
        String propStr = getPropStr("MI_PAY_TYPE");
        UniSdkUtils.d(TAG, "pay_type = " + propStr);
        if ("1".equals(propStr)) {
            miBuyInfo.setCpOrderId(orderInfo.getOrderId());
            String productId = orderInfo.getSdkPids().get("xiaomi_app");
            if (TextUtils.isEmpty(productId)) {
                productId = orderInfo.getProductId();
            }
            miBuyInfo.setProductCode(productId);
            miBuyInfo.setCount(orderInfo.getCount());
        } else {
            miBuyInfo.setCpOrderId(orderInfo.getOrderId());
            miBuyInfo.setCpUserInfo(orderInfo.getOrderId());
            int count = (int) (orderInfo.getCount() * orderInfo.getProductCurrentPrice());
            UniSdkUtils.d(TAG, "amount = " + count);
            miBuyInfo.setAmount(count);
        }
        this.sdkInstance.miUniPay((Activity) this.myCtx, miBuyInfo, new OnPayProcessListener() { // from class: com.netease.ntunisdk.SdkMi.2
            @Override // com.xiaomi.gamecenter.sdk.OnPayProcessListener
            public void finishPayProcess(int i) throws JSONException {
                SdkMi.this.handlePayResult(orderInfo, i);
            }
        });
        String str = "pay_type=" + propStr;
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.putOpt(OneTrackParams.XMSdkParams.STEP, "Check_start");
            jSONObject.putOpt("func", "sdkInstance.miUniPay");
            jSONObject.putOpt("raw_msg", str);
        } catch (JSONException e) {
            UniSdkUtils.d(TAG, "extraJson:" + e.getMessage());
        }
        saveClientLog(orderInfo, jSONObject.toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePayResult(OrderInfo orderInfo, int i) throws JSONException {
        UniSdkUtils.d(TAG, "finishPayProcess,code : " + i);
        if (i == -18006) {
            orderInfo.setOrderStatus(1);
        } else if (i == -18004) {
            orderInfo.setOrderStatus(11);
        } else if (i == 0) {
            orderInfo.setOrderStatus(2);
        } else {
            orderInfo.setOrderStatus(3);
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.putOpt("func", "OnPayProcessListener.finishPayProcess");
            jSONObject.putOpt(OneTrackParams.XMSdkParams.STEP, "checkOrderDone");
            jSONObject.putOpt("unisdk_code", "" + orderInfo.getOrderStatus());
            jSONObject.putOpt("raw_code", "" + i);
            jSONObject.putOpt("raw_msg", orderInfo.getOrderErrReason());
        } catch (JSONException e) {
            UniSdkUtils.d(TAG, "extraJson:" + e.getMessage());
        }
        saveClientLog(orderInfo, jSONObject.toString());
        checkOrderDone(orderInfo);
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void logout() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("methodId", "unisdkChannelAASRoleQuit");
            jSONObject.put("code", 0);
            jSONObject.put(OneTrackParams.XMSdkParams.STEP, "logoutDone");
            ntExtendFunc(jSONObject.toString());
        } catch (JSONException e) {
            UniSdkUtils.d(TAG, "unisdkChannelAASRoleQuit JSONException:" + e.getMessage());
        }
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public boolean openExitView() throws JSONException {
        UniSdkUtils.d(TAG, "openExitView");
        this.sdkInstance.miAppExit((Activity) this.myCtx, new OnExitListner() { // from class: com.netease.ntunisdk.SdkMi.3
            @Override // com.xiaomi.gamecenter.sdk.OnExitListner
            public void onExit(int i) throws JSONException {
                UniSdkUtils.d(SdkMi.TAG, "openExitView,onExit = " + i);
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.putOpt(OneTrackParams.XMSdkParams.STEP, "openExitViewDone");
                    jSONObject.putOpt("func", d.r);
                    jSONObject.putOpt("raw_code", Integer.valueOf(i));
                } catch (JSONException e) {
                    UniSdkUtils.d(SdkMi.TAG, "extraJson:" + e.getMessage());
                }
                SdkMi.this.saveClientLog(null, jSONObject.toString());
                if (i == 10001) {
                    JSONObject jSONObject2 = new JSONObject();
                    try {
                        jSONObject2.put("methodId", "unisdkChannelAASRoleQuit");
                        jSONObject2.put("code", 0);
                        jSONObject2.put(OneTrackParams.XMSdkParams.STEP, "logoutDone");
                        SdkMi.this.ntExtendFunc(jSONObject2.toString());
                    } catch (JSONException e2) {
                        UniSdkUtils.d(SdkMi.TAG, "unisdkChannelAASRoleQuit JSONException:" + e2.getMessage());
                    }
                    SdkMi.this.exitDone();
                }
            }
        });
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.putOpt(OneTrackParams.XMSdkParams.STEP, "openExitView_start");
            jSONObject.putOpt("func", "sdkInstance.miAppExit");
        } catch (JSONException e) {
            UniSdkUtils.d(TAG, "extraJson:" + e.getMessage());
        }
        saveClientLog(null, jSONObject.toString());
        return true;
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void extendFunc(String str) throws JSONException {
        JSONObject jSONObject;
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject = new JSONObject(str);
        } catch (JSONException e) {
            e = e;
            jSONObject = jSONObject2;
        }
        try {
            String strOptString = jSONObject.optString("methodId");
            String strOptString2 = jSONObject.optString("channel");
            if (TextUtils.isEmpty(strOptString2) || getChannel().equalsIgnoreCase(strOptString2)) {
                UniSdkUtils.d(TAG, "extendFunc:" + strOptString);
                if ("miReportOrder".equals(strOptString)) {
                    String strOptString3 = jSONObject.optString("cpOrderId");
                    boolean zOptBoolean = jSONObject.optBoolean("delivery");
                    String strOptString4 = jSONObject.optString("errMsg");
                    MiReportOrder miReportOrder = new MiReportOrder();
                    miReportOrder.setCpOrderId(strOptString3);
                    miReportOrder.setDelivery(zOptBoolean);
                    miReportOrder.setErrMsg(strOptString4);
                    MiCommplatform.getInstance().miReportOrder(miReportOrder);
                    return;
                }
                if (TextUtils.isEmpty(strOptString2)) {
                    return;
                }
                jSONObject.put(RespUtil.UniSdkField.RESP_CODE, 1);
                jSONObject.put(RespUtil.UniSdkField.RESP_MSG, "methodId not exist");
                extendFuncCall(jSONObject.toString());
            }
        } catch (JSONException e2) {
            e = e2;
            UniSdkUtils.d(TAG, "extendFunc JSONException:" + e.getMessage());
            if (TextUtils.isEmpty("")) {
                return;
            }
            try {
                jSONObject.put(RespUtil.UniSdkField.RESP_CODE, 10000);
                jSONObject.put(RespUtil.UniSdkField.RESP_MSG, "unknow error:" + e.getMessage());
                extendFuncCall(jSONObject.toString());
            } catch (JSONException unused) {
            }
        }
    }
}