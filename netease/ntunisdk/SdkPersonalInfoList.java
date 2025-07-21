package com.netease.ntunisdk;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.OnFinishInitListener;
import com.netease.ntunisdk.base.OrderInfo;
import com.netease.ntunisdk.base.SdkBase;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.base.function.ExtendFunc;
import com.netease.ntunisdk.external.protocol.Const;
import com.netease.ntunisdk.modules.api.ModulesManager;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import com.netease.ntunisdk.modules.personalinfolist.ClientLogReporter;
import com.netease.ntunisdk.modules.personalinfolist.PILConstant;
import com.netease.unisdk.biometric.BiometricClient;
import com.netease.unisdk.biometric.Callback;
import com.tencent.connect.common.Constants;
import com.xiaomi.onetrack.OneTrack;
import com.xiaomi.onetrack.api.b;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class SdkPersonalInfoList extends SdkBase {
    private static final String CHANNEL = "personal_info_list";
    private static final long PERIOD = 600000;
    private static final String TAG = "UniSDK SdkPersonalInfoList";
    private static boolean isLoginDone;
    private static long startTime;
    private String access_token;
    private long biometricTime;
    private boolean isUploadAlready;
    private String refresh_token;

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
        return "1.1.0";
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

    public SdkPersonalInfoList(Context context) {
        super(context);
        this.isUploadAlready = false;
        this.access_token = "";
        this.refresh_token = "";
        this.biometricTime = 0L;
        setFeature(ConstProp.INNER_MODE_SECOND_CHANNEL, true);
        setFeature(ConstProp.INNER_MODE_NO_PAY, true);
        setPropInt(ConstProp.LOGIN_PLUGIN_PRIORITY, 1);
        setPropInt("MODE_HAS_PERSONALINFOLIST", 1);
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void init(OnFinishInitListener onFinishInitListener) {
        try {
            ExtendFunc.register("loginPlugin", this);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        if ("1.6.4".compareToIgnoreCase(SdkMgr.getBaseVersion()) > 0) {
            UniSdkUtils.e(TAG, "this sdk should use unisdk base version >= 1.6.4");
        }
        PILConstant.isReport = !SdkMgr.getInst().hasFeature(PILConstant.DISABLE_PIL_REPORT);
        try {
            ((SdkBase) SdkMgr.getInst()).getJfSauthChannelMap().put("get_access_token", "1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (onFinishInitListener != null) {
            onFinishInitListener.finishInit(0);
        }
        UniSdkUtils.d(TAG, "SdkPersonalInfoList.init: OK");
    }

    public static void hookLogout() {
        UniSdkUtils.e(TAG, "hookLogout");
        ClientLogReporter.isInit = false;
        isLoginDone = false;
        startTime = 0L;
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    protected String getUniSDKVersion() {
        return getSDKVersion() + "";
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    protected void gameLoginSuccess() throws JSONException {
        isLoginDone = true;
        initClientLogReporter();
        uploadDeviceInfo();
        parseLoginJsonB64();
        this.isUploadAlready = false;
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void extendFunc(String str) throws JSONException {
        UniSdkUtils.i(TAG, "extendFunc: " + str);
        try {
            JSONObject jSONObject = new JSONObject(str);
            String strOptString = jSONObject.optString("methodId");
            String strOptString2 = jSONObject.optString("channel");
            if (TextUtils.isEmpty(strOptString2) || getChannel().equalsIgnoreCase(strOptString2)) {
                PILConstant.isReport = !SdkMgr.getInst().hasFeature(PILConstant.DISABLE_PIL_REPORT);
                if ("loginPlugin".equalsIgnoreCase(strOptString)) {
                    isLoginDone = true;
                    initClientLogReporter();
                    uploadDeviceInfo();
                    parseLoginJsonB64();
                    extCallbackSuc(jSONObject.optInt("loginDoneCode"));
                    return;
                }
                if ("uploadPersonalInfoEvent".equalsIgnoreCase(strOptString)) {
                    PILConstant.PILExtendCode.formatResult(jSONObject, PILConstant.PILExtendCode.SUCCESS);
                    if (isLbs(jSONObject) && !canReportLbs()) {
                        jSONObject.put("errorCode", 1);
                        jSONObject.put("errorMsg", "lbs can only be reported every 10 minutes after login");
                        UniSdkUtils.d(TAG, jSONObject.toString());
                        if (isEventFromGame(jSONObject)) {
                            extendFuncCall(jSONObject.toString());
                            return;
                        }
                        return;
                    }
                    ClientLogReporter.report(str);
                    if (isEventFromGame(jSONObject)) {
                        extendFuncCall(jSONObject.toString());
                    }
                    UniSdkUtils.d(TAG, jSONObject.toString());
                    return;
                }
                if ("showPersonalInfoList".equalsIgnoreCase(strOptString)) {
                    biometricClient(jSONObject, null);
                } else {
                    if (TextUtils.isEmpty(strOptString2)) {
                        return;
                    }
                    PILConstant.PILExtendCode.formatResult(jSONObject, PILConstant.PILExtendCode.NO_EXIST_METHOD);
                    extendFuncCall(jSONObject.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            UniSdkUtils.d(TAG, "extendFunc JSONException:" + e.getMessage());
            if (TextUtils.isEmpty("")) {
                return;
            }
            try {
                JSONObject jSONObject2 = new JSONObject(str);
                PILConstant.PILExtendCode.formatResult(jSONObject2, PILConstant.PILExtendCode.UNKNOWN_ERROR);
                extendFuncCall(jSONObject2.toString());
            } catch (JSONException unused) {
            }
        }
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void extendFunc(String str, Object... objArr) {
        UniSdkUtils.i(TAG, "extendFunc: " + str);
        try {
            JSONObject jSONObject = new JSONObject(str);
            String strOptString = jSONObject.optString("methodId");
            String strOptString2 = jSONObject.optString("channel");
            if (TextUtils.isEmpty(strOptString2) || getChannel().equalsIgnoreCase(strOptString2)) {
                PILConstant.isReport = !SdkMgr.getInst().hasFeature(PILConstant.DISABLE_PIL_REPORT);
                if ("showPersonalInfoList".equalsIgnoreCase(strOptString)) {
                    biometricClient(jSONObject, objArr);
                } else {
                    if (TextUtils.isEmpty(strOptString2)) {
                        return;
                    }
                    PILConstant.PILExtendCode.formatResult(jSONObject, PILConstant.PILExtendCode.NO_EXIST_METHOD);
                    extendFuncCall(jSONObject.toString());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            UniSdkUtils.d(TAG, "extendFunc JSONException:" + e.getMessage());
            if (TextUtils.isEmpty("")) {
                return;
            }
            try {
                JSONObject jSONObject2 = new JSONObject(str);
                PILConstant.PILExtendCode.formatResult(jSONObject2, PILConstant.PILExtendCode.UNKNOWN_ERROR);
                extendFuncCall(jSONObject2.toString());
            } catch (JSONException unused) {
            }
        }
    }

    private boolean isLbs(JSONObject jSONObject) {
        JSONObject jSONObjectOptJSONObject;
        try {
            if (!jSONObject.has("event") || (jSONObjectOptJSONObject = jSONObject.optJSONObject("event")) == null) {
                return false;
            }
            return "lbs".equalsIgnoreCase(jSONObjectOptJSONObject.optString("name"));
        } catch (Exception unused) {
            return false;
        }
    }

    private boolean isEventFromGame(JSONObject jSONObject) {
        try {
            if (jSONObject.has("upload_type")) {
                return "game".equalsIgnoreCase(jSONObject.optString("upload_type"));
            }
            return false;
        } catch (Exception unused) {
            return false;
        }
    }

    private boolean canReportLbs() {
        if (!isLoginDone || System.currentTimeMillis() - startTime < PERIOD) {
            return false;
        }
        startTime = System.currentTimeMillis();
        return true;
    }

    private void biometricClient(final JSONObject jSONObject, Object[] objArr) throws JSONException {
        PILConstant.PILExtendCode.formatResult(jSONObject, PILConstant.PILExtendCode.SUCCESS);
        Object obj = this.myCtx;
        if (objArr != null && objArr.length > 0) {
            try {
                obj = (Activity) objArr[0];
            } catch (Exception e) {
                e.printStackTrace();
                obj = this.myCtx;
            }
        }
        if (obj != null) {
            BiometricClient.getInstance().verify((Activity) obj, "\u8eab\u4efd\u9a8c\u8bc1", "\u4e3a\u4fdd\u969c\u4e2a\u4eba\u4fe1\u606f\u5b89\u5168\uff0c\u8bf7\u5148\u9a8c\u8bc1\u8eab\u4efd\u540e\u67e5\u770b\u3002", new Callback() { // from class: com.netease.ntunisdk.SdkPersonalInfoList.1
                @Override // com.netease.unisdk.biometric.Callback
                public void onFinish(final int i) {
                    UniSdkUtils.d(SdkPersonalInfoList.TAG, "biometricClient code = " + i);
                    if (SdkPersonalInfoList.this.handleBiometricCode(i)) {
                        SdkPersonalInfoList.this.showPersonalInfoList(jSONObject);
                    } else {
                        SdkPersonalInfoList.this.runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.SdkPersonalInfoList.1.1
                            @Override // java.lang.Runnable
                            public void run() throws JSONException {
                                String str = i == 1 ? "\u6388\u6743\u5931\u8d25" : "\u4e0d\u652f\u6301";
                                try {
                                    jSONObject.put("errorCode", 1);
                                    jSONObject.put("errorMsg", str);
                                } catch (JSONException unused) {
                                }
                                SdkPersonalInfoList.this.extendFuncCall(jSONObject.toString());
                            }
                        });
                    }
                }
            });
            return;
        }
        jSONObject.put("errorCode", 1);
        jSONObject.put("errorMsg", "myCtx == null");
        extendFuncCall(jSONObject.toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean handleBiometricCode(int i) {
        if (i == 3) {
            return true;
        }
        if (i != 0 || System.currentTimeMillis() - this.biometricTime < 200) {
            return false;
        }
        this.biometricTime = System.currentTimeMillis();
        return true;
    }

    private void initClientLogReporter() throws JSONException {
        UniSdkUtils.d(TAG, "initClientLogReporter");
        try {
            JSONObject jSONObject = new JSONObject();
            String propStr = SdkMgr.getInst().getPropStr(ConstProp.JF_CLIENT_LOG_URL);
            if (!TextUtils.isEmpty(propStr)) {
                jSONObject.putOpt(ConstProp.JF_CLIENT_LOG_URL, propStr);
            }
            String propStr2 = SdkMgr.getInst().getPropStr(ConstProp.JF_LOG_KEY);
            if (!TextUtils.isEmpty(propStr2)) {
                jSONObject.putOpt(ConstProp.JF_LOG_KEY, propStr2);
            }
            jSONObject.putOpt("gameid", SdkMgr.getInst().getPropStr("JF_GAMEID"));
            jSONObject.putOpt("username", SdkMgr.getInst().getPropStr(ConstProp.FULL_UID));
            jSONObject.putOpt("aid", SdkMgr.getInst().getPropStr(ConstProp.USERINFO_AID));
            jSONObject.putOpt(ClientLogConstant.TRANSID, SdkMgr.getInst().getPropStr(ConstProp.TRANS_ID));
            jSONObject.putOpt(ConstProp.UDID, SdkMgr.getInst().getPropStr(ConstProp.UDID));
            jSONObject.putOpt("channel", SdkMgr.getInst().getChannel());
            jSONObject.putOpt(Const.APP_CHANNEL, SdkMgr.getInst().getAppChannel());
            jSONObject.putOpt("version", SdkMgr.getInst().getSDKVersion(SdkMgr.getInst().getChannel()));
            ClientLogReporter.init(jSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadDeviceInfo() throws JSONException {
        UniSdkUtils.d(TAG, "uploadDeviceInfo");
        if (this.isUploadAlready) {
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("os_version", Build.VERSION.RELEASE);
            jSONObject.put("os_model", Build.MODEL);
            jSONObject.put("os_brand", Build.BRAND);
            jSONObject.put("androidid", ModulesManager.getInst().extendFunc(ConstProp.UNISDKBASE, "deviceInfo", "{\"methodId\":\"getAndroidId\"}"));
            jSONObject.put(OneTrack.Param.OAID, SdkMgr.getInst().getPropStr("OAID"));
            jSONObject.put(b.B, SdkMgr.getInst().getPropStr(ConstProp.SDC_LOG_MAC_ADDR));
            jSONObject.put("imei", ModulesManager.getInst().extendFunc(ConstProp.UNISDKBASE, "deviceInfo", "{\"methodId\":\"getImei\"}"));
            jSONObject.put("imsi", ModulesManager.getInst().extendFunc(ConstProp.UNISDKBASE, "deviceInfo", "{\"methodId\":\"getImsi\"}"));
            jSONObject.put("device_name", Build.MODEL);
            jSONObject.put("udid", SdkMgr.getInst().getPropStr(ConstProp.UDID));
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("name", "device_info");
            jSONObject2.putOpt("spec", jSONObject);
            JSONObject jSONObject3 = new JSONObject();
            jSONObject3.putOpt("event", jSONObject2);
            jSONObject3.put("upload_type", "unisdk");
            ClientLogReporter.report(jSONObject3.toString());
            this.isUploadAlready = true;
        } catch (Exception e) {
            e.printStackTrace();
            this.isUploadAlready = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:100:0x01f4  */
    /* JADX WARN: Removed duplicated region for block: B:103:0x01fe  */
    /* JADX WARN: Removed duplicated region for block: B:104:0x020b  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0175  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0185  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0193  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x01a1  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x01b3  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x01c0  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x01ca  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x01dd  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x01e7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void showPersonalInfoList(final org.json.JSONObject r29) {
        /*
            Method dump skipped, instructions count: 929
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.SdkPersonalInfoList.showPersonalInfoList(org.json.JSONObject):void");
    }

    private void extCallbackSuc(int i) throws JSONException {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.putOpt("methodId", "loginPluginCallback");
            jSONObject.putOpt("loginDoneCode", Integer.valueOf(i));
            SdkMgr.getInst().ntExtendFunc(jSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runOnUiThread(Runnable runnable) {
        ((Activity) this.myCtx).runOnUiThread(runnable);
    }

    public void parseLoginJsonB64() {
        try {
            String propStr = SdkMgr.getInst().getPropStr(ConstProp.UNISDK_LOGIN_JSON);
            UniSdkUtils.d(TAG, "loginJsonB64 = " + propStr);
            if (TextUtils.isEmpty(propStr)) {
                return;
            }
            JSONObject jSONObject = new JSONObject(new String(Base64.decode(propStr, 0), "UTF-8"));
            String strOptString = jSONObject.optString("gas_token", jSONObject.optString("sdk_token"));
            JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("oauth2");
            if (jSONObjectOptJSONObject != null) {
                String strOptString2 = jSONObjectOptJSONObject.optString(Constants.PARAM_ACCESS_TOKEN);
                String strOptString3 = jSONObjectOptJSONObject.optString("refresh_token");
                UniSdkUtils.d(TAG, "oauthAccessToken = " + strOptString2);
                UniSdkUtils.d(TAG, "oauthRefreshToken = " + strOptString3);
                if (!TextUtils.isEmpty(strOptString2)) {
                    if (!strOptString2.equals("gas_token") && !strOptString2.equals("sdk_token")) {
                        this.access_token = strOptString2;
                        this.refresh_token = strOptString3;
                    } else {
                        this.access_token = strOptString;
                        this.refresh_token = strOptString3;
                    }
                }
                UniSdkUtils.d(TAG, "access_token = " + this.access_token);
                UniSdkUtils.d(TAG, "refresh_token = " + this.refresh_token);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}