package com.netease.ntunisdk;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Toast;
import com.alipay.sdk.m.s.a;
import com.netease.mpay.ps.codescanner.CodeScannerApi;
import com.netease.mpay.ps.codescanner.CodeScannerCallback;
import com.netease.mpay.ps.codescanner.CodeScannerConst;
import com.netease.mpay.ps.codescanner.CodeScannerExtCallback;
import com.netease.mpay.ps.codescanner.CodeScannerRetCode;
import com.netease.mpay.ps.codescanner.Configs;
import com.netease.mpay.ps.codescanner.Consts;
import com.netease.mpay.ps.codescanner.auth.DefaultAuthRules;
import com.netease.mpay.ps.codescanner.module.QRCodeLoginRaw;
import com.netease.mpay.ps.codescanner.module.QRCodePayRaw;
import com.netease.mpay.ps.codescanner.module.QRCodeRaw;
import com.netease.mpay.ps.codescanner.widget.Alerters;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.OnCodeScannerListener;
import com.netease.ntunisdk.base.OnFinishInitListener;
import com.netease.ntunisdk.base.OnLoginDoneListener;
import com.netease.ntunisdk.base.OnOrderCheckListener;
import com.netease.ntunisdk.base.OrderInfo;
import com.netease.ntunisdk.base.SdkBase;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.base.utils.ApiRequestUtil;
import com.netease.ntunisdk.base.utils.Crypto;
import com.netease.ntunisdk.base.utils.HTTPCallback;
import com.netease.ntunisdk.base.utils.HTTPQueue;
import com.netease.ntunisdk.base.utils.NetUtil;
import com.netease.ntunisdk.base.utils.WgetDoneCallback;
import com.netease.ntunisdk.codescanner.CodeScannerBase;
import com.netease.ntunisdk.codescanner.OnPauseStateChangeListener;
import com.netease.ntunisdk.external.protocol.Const;
import com.tencent.connect.common.Constants;
import com.xiaomi.gamecenter.sdk.statistics.OneTrackParams;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class SdkNeteaseCodeScanner extends CodeScannerBase {
    private static final String CURRENT_QUICK_QR_MODE = "CURRENT_QUICK_QR_MODE";
    private static final String DISABLE_QUICK_QR_PAY = "DISABLE_QUICK_QR_PAY";
    private static final String ENABLE_CHANNEL_QRCODESCANNER = "ENABLE_CHANNEL_QRCODESCANNER";
    private static final String SCANNER_SDK_VERSION = "1.7.0";
    private static final String SCANNER_UNISDK_VERSION = "1.7.0";
    private static final String TAG = "UniSDK netease_codescanner";
    private boolean debugMode;
    private CodeScannerApi scannerApi;

    @Override // com.netease.ntunisdk.codescanner.CodeScannerBase, com.netease.ntunisdk.base.SdkBase
    public void checkOrder(OrderInfo orderInfo) {
    }

    @Override // com.netease.ntunisdk.codescanner.CodeScannerBase, com.netease.ntunisdk.base.SdkBase, com.netease.ntunisdk.base.GamerInterface
    public String getChannel() {
        return "netease_codescanner";
    }

    @Override // com.netease.ntunisdk.codescanner.CodeScannerBase, com.netease.ntunisdk.base.SdkBase
    public String getLoginSession() {
        return null;
    }

    @Override // com.netease.ntunisdk.codescanner.CodeScannerBase, com.netease.ntunisdk.base.SdkBase
    public String getLoginUid() {
        return null;
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public String getSDKVersion() {
        return Consts.SDK_VERSION;
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    protected String getUniSDKVersion() {
        return Consts.SDK_VERSION;
    }

    @Override // com.netease.ntunisdk.codescanner.CodeScannerBase, com.netease.ntunisdk.base.SdkBase
    public void login() {
    }

    @Override // com.netease.ntunisdk.codescanner.CodeScannerBase, com.netease.ntunisdk.base.SdkBase
    public void logout() {
    }

    @Override // com.netease.ntunisdk.codescanner.CodeScannerBase, com.netease.ntunisdk.base.SdkBase
    public void openManager() {
    }

    @Override // com.netease.ntunisdk.codescanner.CodeScannerBase, com.netease.ntunisdk.base.SdkBase
    public void upLoadUserInfo() {
    }

    public SdkNeteaseCodeScanner(Context context) {
        super(context);
        this.debugMode = false;
        setPropInt(ConstProp.INNER_MODE_SECOND_CHANNEL, 1);
        setPropInt(ConstProp.INNER_MODE_NO_PAY, 1);
    }

    @Override // com.netease.ntunisdk.codescanner.CodeScannerBase, com.netease.ntunisdk.base.SdkBase
    public void init(OnFinishInitListener onFinishInitListener) {
        UniSdkUtils.d(TAG, "init SdkNeteaseCodeScanner");
        DefaultAuthRules.getInstance().attachActivity((Activity) this.myCtx);
        String propStr = getPropStr(ConstProp.APPID);
        if (TextUtils.isEmpty(propStr)) {
            UniSdkUtils.e(TAG, "netease_codescanner_data\u6ca1\u6709\u914d\u7f6eAPPID\u4e3a \u7f51\u6613\u5b98\u65b9\u6e20\u9053\u7533\u8bf7\u7684GAME_ID");
            onFinishInitListener.finishInit(1);
            this.hasInit = false;
        } else {
            this.scannerApi = new CodeScannerApi((Activity) this.myCtx, propStr, SdkMgr.getInst().getChannel(), SdkMgr.getInst().getUdid(), SdkMgr.getInst().getAppChannel(), SdkMgr.getInst().getSDKVersion(SdkMgr.getInst().getChannel()));
            this.scannerApi.setDebugMode(this.debugMode);
            onFinishInitListener.finishInit(0);
        }
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void extendFunc(String str) {
        System.out.println("-------extendFunc:--------:" + str);
        UniSdkUtils.i(TAG, "extendFunc: " + str);
        try {
            JSONObject jSONObject = new JSONObject(str);
            String strOptString = jSONObject.optString("methodId");
            UniSdkUtils.d(TAG, "methodId: " + strOptString);
            if ("openAuthLogin".equals(strOptString)) {
                try {
                    qrCodeScanner(0, null, new String(Base64.decode(jSONObject.optString("data"), 8)) + "&scene=game_auth", true);
                } catch (Exception unused) {
                    qrCodeScanner(10, null, null, true);
                }
            }
        } catch (Exception unused2) {
        }
    }

    private final void onLoginDone(int i) throws JSONException {
        try {
            DefaultAuthRules.getInstance().setStartLoginByScanner(false);
            UniSdkUtils.d(TAG, "onLoginDone:" + i);
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "loginPluginCallback");
            jSONObject.put("loginDoneCode", i);
            SdkMgr.getInst().ntExtendFunc(jSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.netease.ntunisdk.base.SdkBase, com.netease.ntunisdk.base.GamerInterface
    public void setDebugMode(boolean z) {
        UniSdkUtils.i(TAG, "setDebugMode to " + z);
        this.debugMode = z;
    }

    private String getJFSauth(JSONObject jSONObject) throws UnsupportedEncodingException {
        String propStr = SdkMgr.getInst().getPropStr("JF_GAMEID");
        String propStr2 = SdkMgr.getInst().getPropStr(ConstProp.WEB_SESSION);
        String sDKVersion = SdkMgr.getInst().getSDKVersion(SdkMgr.getInst().getChannel());
        StringBuilder sb = new StringBuilder();
        try {
            Object[] objArr = new Object[8];
            objArr[0] = propStr;
            objArr[1] = SdkMgr.getInst().getChannel();
            objArr[2] = SdkMgr.getInst().getAppChannel();
            objArr[3] = SdkMgr.getInst().getPlatform();
            objArr[4] = SdkMgr.getInst().getPropStr(ConstProp.WEB_UID) + "@" + SdkMgr.getInst().getPlatform() + "." + SdkMgr.getInst().getChannel() + ".win.163.com";
            objArr[5] = SdkMgr.getInst().getUdid();
            String strEncode = "null";
            objArr[6] = propStr2 == null ? "null" : URLEncoder.encode(propStr2, "UTF-8");
            if (sDKVersion != null) {
                strEncode = URLEncoder.encode(sDKVersion, "UTF-8");
            }
            objArr[7] = strEncode;
            sb.append(String.format("gameid=%s&login_channel=%s&app_channel=%s&platform=%s&username=%s&udid=%s&sessionid=%s&sdk_version=%s", objArr));
            String propStr3 = SdkMgr.getInst().getPropStr(ConstProp.LOCAL_IP, "127.0.0.1");
            sb.append("&ip=");
            sb.append(URLEncoder.encode(propStr3, "UTF-8"));
            if (!TextUtils.isEmpty(SdkMgr.getInst().getPropStr(ConstProp.JF_AIM_INFO))) {
                sb.append("&aim_info=");
                sb.append(URLEncoder.encode(SdkMgr.getInst().getPropStr(ConstProp.JF_AIM_INFO), "UTF-8"));
            }
            Map<String, String> jfSauthChannelMap = ((SdkBase) SdkMgr.getInst()).getJfSauthChannelMap();
            for (String str : jfSauthChannelMap.keySet()) {
                sb.append(a.l);
                sb.append(str);
                sb.append("=");
                sb.append(URLEncoder.encode(jfSauthChannelMap.get(str), "UTF-8"));
            }
            if (hasGuestLogined()) {
                sb.append("&is_unisdk_guest=1");
            }
            if ("coolpad_sdk".equals(SdkMgr.getInst().getChannel())) {
                sb.append(a.l);
                sb.append("cpid");
                sb.append("=");
                sb.append("web_login");
            }
            if (jSONObject != null) {
                try {
                    String strOptString = jSONObject.optString("gas_token");
                    String strOptString2 = jSONObject.optString("sdk_token");
                    if (TextUtils.isEmpty(strOptString2) && !TextUtils.isEmpty(strOptString)) {
                        strOptString2 = strOptString;
                    }
                    if (!TextUtils.isEmpty(strOptString)) {
                        sb.append(a.l);
                        sb.append("gas_token");
                        sb.append("=");
                        sb.append(strOptString);
                    }
                    if (!TextUtils.isEmpty(strOptString2)) {
                        sb.append(a.l);
                        sb.append("sdk_token");
                        sb.append("=");
                        sb.append(strOptString2);
                    }
                } catch (Exception unused) {
                }
            }
        } catch (UnsupportedEncodingException e) {
            UniSdkUtils.d(TAG, "UnsupportedEncodingException" + e.getMessage());
        }
        UniSdkUtils.d(TAG, "NeteaseCodeScanner SAUTH_STR:" + sb.toString());
        try {
            return URLEncoder.encode(Base64.encodeToString(sb.toString().getBytes(), 0), "UTF-8");
        } catch (UnsupportedEncodingException e2) {
            UniSdkUtils.d(TAG, "URLEncoder.encode Base64.encodeToString exception:" + e2.getMessage());
            return "";
        }
    }

    private String getJFSauthJson(JSONObject jSONObject) throws JSONException {
        String propStr = SdkMgr.getInst().getPropStr("JF_GAMEID");
        String propStr2 = SdkMgr.getInst().getPropStr(ConstProp.WEB_SESSION);
        String sDKVersion = SdkMgr.getInst().getSDKVersion(SdkMgr.getInst().getChannel());
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject2.put("gameid", propStr);
            jSONObject2.put("login_channel", SdkMgr.getInst().getChannel());
            jSONObject2.put(Const.APP_CHANNEL, SdkMgr.getInst().getAppChannel());
            jSONObject2.put("platform", SdkMgr.getInst().getPlatform());
            jSONObject2.put("sdkuid", SdkMgr.getInst().getPropStr(ConstProp.WEB_UID));
            jSONObject2.put("udid", SdkMgr.getInst().getUdid());
            jSONObject2.put("sessionid", propStr2);
            jSONObject2.put("sdk_version", sDKVersion);
            jSONObject2.putOpt("is_unisdk_guest", Integer.valueOf(hasGuestLogined() ? 1 : 0));
            jSONObject2.put("ip", SdkMgr.getInst().getPropStr(ConstProp.LOCAL_IP, "127.0.0.1"));
            jSONObject2.put("aim_info", SdkMgr.getInst().getPropStr(ConstProp.JF_AIM_INFO));
            if (jSONObject != null) {
                try {
                    String strOptString = jSONObject.optString("gas_token");
                    String strOptString2 = jSONObject.optString("sdk_token");
                    if (TextUtils.isEmpty(strOptString2) && !TextUtils.isEmpty(strOptString)) {
                        strOptString2 = strOptString;
                    }
                    if (!TextUtils.isEmpty(strOptString)) {
                        jSONObject2.put("gas_token", strOptString);
                    }
                    if (!TextUtils.isEmpty(strOptString2)) {
                        jSONObject2.putOpt("sdk_token", strOptString2);
                    }
                } catch (Exception unused) {
                }
            }
            jSONObject2.put(Constants.PARAM_ACCESS_TOKEN, SdkMgr.getInst().getPropStr(ConstProp.UNISDK_JF_ACCESS_TOKEN));
            Map<String, String> jfSauthChannelMap = ((SdkBase) SdkMgr.getInst()).getJfSauthChannelMap();
            for (String str : jfSauthChannelMap.keySet()) {
                jSONObject2.put(str, jfSauthChannelMap.get(str));
            }
            if ("coolpad_sdk".equals(SdkMgr.getInst().getChannel())) {
                jSONObject2.put("cpid", "web_login");
            }
        } catch (JSONException e) {
            UniSdkUtils.e(TAG, "sauthJson JSONException:" + e.getMessage());
            e.printStackTrace();
        }
        UniSdkUtils.d(TAG, "NeteaseCodeScanner SAUTH_JSON:" + jSONObject2.toString());
        try {
            return URLEncoder.encode(Base64.encodeToString(jSONObject2.toString().getBytes(), 0), "UTF-8");
        } catch (UnsupportedEncodingException e2) {
            UniSdkUtils.d(TAG, "URLEncoder.encode Base64.encodeToString exception:" + e2.getMessage());
            return "";
        }
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void presentQRCodeScanner(String str, int i) throws NoSuchFieldException {
        UniSdkUtils.d(TAG, "presentQRCodeScanner extra: " + str);
        String propStr = SdkMgr.getInst().getPropStr("QRCODE_HOST", "");
        Configs.setHost(propStr);
        UniSdkUtils.d(TAG, "set qrcode host:" + propStr + ", final :" + Configs.getHost());
        unisdkQrCodeScanner(str, i);
    }

    public void unisdkQrCodeScanner(final String str, final int i) throws NoSuchFieldException {
        Map<String, SdkBase> secSdkInstMap = getSecSdkInstMap();
        if (secSdkInstMap != null && secSdkInstMap.containsKey("unisdk_qrcode")) {
            UniSdkUtils.d(TAG, "start unisdk_qrcode");
            SdkQRCode sdkQRCode = (SdkQRCode) secSdkInstMap.get("unisdk_qrcode");
            if (sdkQRCode == null) {
                codeScannerDone(10, "");
                return;
            }
            if (isQuickQr() && !isColdLaunch() && this.mHasPaused) {
                UniSdkUtils.d(TAG, "schedule Timer");
                final Timer timer = new Timer();
                timer.schedule(new TimerTask() { // from class: com.netease.ntunisdk.SdkNeteaseCodeScanner.1
                    @Override // java.util.TimerTask, java.lang.Runnable
                    public void run() {
                        UniSdkUtils.d(SdkNeteaseCodeScanner.TAG, "run TimerTask");
                        SdkNeteaseCodeScanner.this.mOnPauseStateChangeListener = null;
                        ((Activity) SdkNeteaseCodeScanner.this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.SdkNeteaseCodeScanner.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(SdkNeteaseCodeScanner.this.myCtx, "\u8bf7\u56de\u5230\u6e38\u620f\u754c\u9762\u518d\u6267\u884c\u5feb\u6377\u626b\u7801", 1).show();
                            }
                        });
                    }
                }, 1000L);
                this.mOnPauseStateChangeListener = new OnPauseStateChangeListener() { // from class: com.netease.ntunisdk.SdkNeteaseCodeScanner.2
                    @Override // com.netease.ntunisdk.codescanner.OnPauseStateChangeListener
                    public void onPauseStateChanged(boolean z) throws NoSuchFieldException {
                        UniSdkUtils.d(SdkNeteaseCodeScanner.TAG, "onPauseStateChanged: " + z);
                        if (z) {
                            return;
                        }
                        SdkNeteaseCodeScanner.this.presentQuickQRCodeScanner(str, i);
                        SdkNeteaseCodeScanner.this.mOnPauseStateChangeListener = null;
                        timer.cancel();
                    }
                };
                setCurrentQuickQrMode(0);
                return;
            }
            if (QrCodeScanFlow.isInQrCodeScanFlow()) {
                UniSdkUtils.d(TAG, "in ScanFlow, ignore");
                Toast.makeText(this.myCtx, "\u5f53\u524d\u5df2\u5904\u4e8e\u626b\u7801\u6d41\u7a0b\uff0c\u8bf7\u52ff\u91cd\u590d\u626b\u7801", 1).show();
                setCurrentQuickQrMode(0);
                return;
            } else {
                sdkQRCode.setUniSDKCodeScannerListener(new OnCodeScannerListener() { // from class: com.netease.ntunisdk.SdkNeteaseCodeScanner.3
                    @Override // com.netease.ntunisdk.base.OnCodeScannerListener
                    public void codeScannerFinish(int i2, String str2) throws JSONException {
                        UniSdkUtils.d(SdkNeteaseCodeScanner.TAG, "unisdkQrCodeScanner, code:" + i2 + ", result:" + str2);
                        if ((i2 == 0 || i2 == 21) && !TextUtils.isEmpty(str2)) {
                            if (SdkNeteaseCodeScanner.isMpayQrCode(str2)) {
                                UniSdkUtils.d(SdkNeteaseCodeScanner.TAG, "mpay qrcode");
                                if (SdkNeteaseCodeScanner.enableChannelQrCodeScanner()) {
                                    SdkNeteaseCodeScanner.this.mpayQRCodeScanner(i2, str, str2);
                                    return;
                                } else {
                                    SdkNeteaseCodeScanner.this.codeScannerDone(31, "\u8be5\u6e20\u9053\u4e0d\u652f\u6301\u626b\u7801\u767b\u5f55");
                                    return;
                                }
                            }
                            UniSdkUtils.d(SdkNeteaseCodeScanner.TAG, "not mpay qrcode");
                            SdkNeteaseCodeScanner.this.codeScannerDone(21, str2);
                            return;
                        }
                        SdkNeteaseCodeScanner.this.codeScannerDone(i2, str2);
                    }
                });
                sdkQRCode.scannerQRCode("");
                QrCodeScanFlow.setInQrCodeScanFlow(true);
                return;
            }
        }
        UniSdkUtils.e(TAG, "not pack unisdk_qrcode library");
        codeScannerDone(10, "");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void presentQuickQRCodeScanner(String str, int i) throws NoSuchFieldException {
        setCurrentQuickQrMode(1);
        presentQRCodeScanner(str, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean enableChannelQrCodeScanner() {
        boolean z = 1 == SdkMgr.getInst().getPropInt("ENABLE_CHANNEL_QRCODESCANNER", 1);
        UniSdkUtils.d(TAG, "ENABLE_CHANNEL_QRCODESCANNER " + z);
        return z;
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    protected void codeScannerDone(int i, String str) {
        boolean zIsQuickQr = isQuickQr();
        QrCodeScanFlow.setInQrCodeScanFlow(false);
        UniSdkUtils.d(TAG, "codeScannerDone, code:" + i + ", result:" + str);
        if (zIsQuickQr && (i == 21 || i == 5)) {
            alertScanLoginQrCode(false);
        } else {
            super.codeScannerDone(i, str);
        }
    }

    private boolean isColdLaunch() {
        return this.myCtx != null && "com.netease.ntunisdk.shortcuts.ShortcutMainActivity".equals(this.myCtx.getClass().getCanonicalName());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void setCurrentQuickQrMode(int i) {
        UniSdkUtils.d(TAG, "currentQuickQrMode = " + i);
        SdkMgr.getInst().setPropInt("CURRENT_QUICK_QR_MODE", i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void alertScanLoginQrCode(boolean z) {
        QrCodeScanFlow.setInQrCodeScanFlow(false);
        String string = this.myCtx.getString(com.netease.mc.mi.R.string.netease_mpay_ps_codescanner__login_qrcode_scan_retry);
        if (z) {
            string = this.myCtx.getString(com.netease.mc.mi.R.string.netease_mpay_ps_codescanner__login_qrcode_scan_nopay);
        }
        new Alerters(this.myCtx).alert(string, this.myCtx.getString(com.netease.mc.mi.R.string.netease_mpay_ps_codescanner__login_qrcode_confirm), new DialogInterface.OnClickListener() { // from class: com.netease.ntunisdk.SdkNeteaseCodeScanner.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) throws NoSuchFieldException {
                SdkNeteaseCodeScanner.this.presentQuickQRCodeScanner(null, 0);
            }
        }, null, null, false);
    }

    public static class QrCodeScanFlow {
        private static volatile boolean mIsInQrCodeScanFlow;

        public static boolean isInQrCodeScanFlow() {
            return mIsInQrCodeScanFlow;
        }

        public static synchronized void setInQrCodeScanFlow(boolean z) {
            UniSdkUtils.d(SdkNeteaseCodeScanner.TAG, "setInQrCodeScanFlow: " + z);
            if (!z) {
                SdkNeteaseCodeScanner.setCurrentQuickQrMode(0);
            }
            mIsInQrCodeScanFlow = z;
        }
    }

    private Map<String, SdkBase> getSecSdkInstMap() throws NoSuchFieldException {
        try {
            SdkBase sdkBase = (SdkBase) SdkMgr.getInst();
            Field declaredField = sdkBase.getClass().getSuperclass().getDeclaredField("sdkInstMap");
            declaredField.setAccessible(true);
            return (Map) declaredField.get(sdkBase);
        } catch (IllegalAccessException e) {
            UniSdkUtils.e(TAG, "getSecSdkInstMap\uff1a" + e.getMessage());
            try {
                return ((SdkBase) SdkMgr.getInst()).getSdkInstMap();
            } catch (Exception e2) {
                UniSdkUtils.e(TAG, "getSecSdkInstMap\uff1a" + e2.getMessage());
                return null;
            }
        } catch (IllegalArgumentException e3) {
            UniSdkUtils.e(TAG, "getSecSdkInstMap\uff1a" + e3.getMessage());
            return ((SdkBase) SdkMgr.getInst()).getSdkInstMap();
        } catch (NoSuchFieldException e4) {
            UniSdkUtils.e(TAG, "getSecSdkInstMap\uff1a" + e4.getMessage());
            return ((SdkBase) SdkMgr.getInst()).getSdkInstMap();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isMpayQrCode(String str) {
        QRCodeRaw qRCodeRawDecode = QRCodeRaw.decode(str);
        return (qRCodeRawDecode instanceof QRCodeLoginRaw) || (qRCodeRawDecode instanceof QRCodePayRaw);
    }

    private static boolean isMpayLoginQrCode(String str) {
        return QRCodeRaw.decode(str) instanceof QRCodeLoginRaw;
    }

    public void mpayQRCodeScanner(int i, final String str, final String str2) throws JSONException {
        UniSdkUtils.d(TAG, "mpayQRCodeScanner extra: " + str);
        if (isQuickQr() && disableQuickQrPay() && !isMpayLoginQrCode(str2)) {
            alertScanLoginQrCode(true);
            return;
        }
        if (!SdkMgr.getInst().hasLogin()) {
            UniSdkUtils.d(TAG, "\u6ca1\u6709\u767b\u5f55");
            codeScannerDone(1, "\u6ca1\u6709\u767b\u5f55");
        } else {
            ((SdkBase) SdkMgr.getInst()).setWebLoginByCodeScannerListener(new OnLoginDoneListener() { // from class: com.netease.ntunisdk.SdkNeteaseCodeScanner.5
                @Override // com.netease.ntunisdk.base.OnLoginDoneListener
                public void loginDone(int i2) {
                    UniSdkUtils.d(SdkNeteaseCodeScanner.TAG, "login finished");
                    SdkNeteaseCodeScanner.this.qrCodeScanner(i2, str, str2, false);
                }
            }, 1);
            UniSdkUtils.d(TAG, "start login");
            ((SdkBase) SdkMgr.getInst()).webLoginByCodeScanner();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isQuickQr() {
        return 1 == SdkMgr.getInst().getPropInt("CURRENT_QUICK_QR_MODE", 0);
    }

    private boolean disableQuickQrPay() {
        return 1 == SdkMgr.getInst().getPropInt("DISABLE_QUICK_QR_PAY", 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void qrCodeScanner(final int i, final String str, final String str2, final boolean z) {
        UniSdkUtils.d(TAG, "mobile uid:" + SdkMgr.getInst().getPropStr(ConstProp.UID));
        UniSdkUtils.d(TAG, "login code:" + i);
        if (i == 0) {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.SdkNeteaseCodeScanner.6
                @Override // java.lang.Runnable
                public void run() throws JSONException, UnsupportedEncodingException {
                    UniSdkUtils.i(SdkNeteaseCodeScanner.TAG, "runOnUiThread unisdkQrcodeDone");
                    SdkNeteaseCodeScanner.this.unisdkQrcodeDone(i, str, str2, z);
                }
            });
            return;
        }
        if (i == 1) {
            UniSdkUtils.d(TAG, "\u7528\u6237\u53d6\u6d88\u767b\u5f55");
            if (z) {
                DefaultAuthRules.getInstance().notifyAuthDone(i);
            }
            codeScannerDone(3, "\u7528\u6237\u53d6\u6d88\u767b\u5f55");
            return;
        }
        UniSdkUtils.d(TAG, "\u767b\u5f55\u5931\u8d25");
        if (z) {
            DefaultAuthRules.getInstance().notifyAuthDone(i);
        }
        codeScannerDone(3, "\u767b\u5f55\u5931\u8d25");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unisdkQrcodeDone(int i, final String str, String str2, final boolean z) throws JSONException, UnsupportedEncodingException {
        String strEncode;
        JSONObject jSONObject;
        final String propStr = SdkMgr.getInst().getPropStr(ConstProp.WEB_UID);
        final String propStr2 = SdkMgr.getInst().getPropStr(ConstProp.WEB_SESSION);
        UniSdkUtils.d(TAG, "uid:" + propStr + ", token:" + propStr2);
        if (TextUtils.isEmpty(propStr2)) {
            strEncode = propStr2;
        } else {
            try {
                strEncode = URLEncoder.encode(Base64.encodeToString(propStr2.getBytes(), 0), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        Map<String, String> jfSauthChannelMap = ((SdkBase) SdkMgr.getInst()).getJfSauthChannelMap();
        JSONObject jSONObject2 = new JSONObject();
        if (jfSauthChannelMap != null) {
            try {
                if (!jfSauthChannelMap.isEmpty()) {
                    for (String str3 : jfSauthChannelMap.keySet()) {
                        jSONObject2.put(str3, jfSauthChannelMap.get(str3));
                    }
                }
            } catch (JSONException e2) {
                UniSdkUtils.e(TAG, "extraUniData JSONException:" + e2.getMessage());
                e2.printStackTrace();
            }
        }
        try {
            jSONObject = new JSONObject(SdkMgr.getInst().getPropStr(ConstProp.SAUTH_JSON));
            try {
                jSONObject.put("hostid", SdkMgr.getInst().getPropInt(ConstProp.USERINFO_HOSTID, 0));
                String propStr3 = SdkMgr.getInst().getPropStr(ConstProp.LOCAL_IP);
                if (TextUtils.isEmpty(propStr3)) {
                    propStr3 = "127.0.0.1";
                }
                jSONObject.put("ip", propStr3);
                String strOptString = jSONObject.optString("gas_token");
                if (TextUtils.isEmpty(jSONObject.optString("sdk_token")) && !TextUtils.isEmpty(strOptString)) {
                    jSONObject.putOpt("sdk_token", strOptString);
                }
            } catch (Exception unused) {
            }
        } catch (Exception unused2) {
            jSONObject = null;
        }
        jSONObject2.put(ConstProp.SAUTH_STR, getJFSauth(jSONObject));
        jSONObject2.put(ConstProp.SAUTH_JSON, getJFSauthJson(jSONObject));
        String string = jSONObject2.toString();
        UniSdkUtils.d(TAG, "extraUniData:" + string);
        HashMap<String, String> map = new HashMap<>();
        map.put(CodeScannerConst.JF_GAME_ID, SdkMgr.getInst().getPropStr("JF_GAMEID"));
        String channel = SdkMgr.getInst().getChannel();
        String payChannel = SdkMgr.getInst().getPayChannel();
        Hashtable<String, OrderInfo.ProductInfo> productList = OrderInfo.getProductList();
        if (!productList.isEmpty()) {
            String next = productList.keySet().iterator().next();
            String payChannelByPid = SdkMgr.getInst().getPayChannelByPid(next);
            UniSdkUtils.d(TAG, "pId:" + next);
            channel = payChannelByPid;
        } else if (!TextUtils.isEmpty(payChannel) && payChannel.contains("google_play")) {
            channel = "google_play";
        }
        UniSdkUtils.d(TAG, "payChannel:" + channel);
        map.put(CodeScannerConst.PAY_CHANNEL, channel);
        UniSdkUtils.d(TAG, "urlencode base64 token:" + strEncode);
        this.scannerApi.presentQRCodeScanner(str2, propStr, strEncode, map, str, string, new CodeScannerCallback() { // from class: com.netease.ntunisdk.SdkNeteaseCodeScanner.7
            @Override // com.netease.mpay.ps.codescanner.CodeScannerCallback
            public void onScanPaymentSuccess(String str4, String str5) throws JSONException {
                UniSdkUtils.d(SdkNeteaseCodeScanner.TAG, "\u83b7\u53d6" + str4 + "\u7684\u4fe1\u606f\uff1a " + str5);
                if ("g18".equals(SdkMgr.getInst().getPropStr("JF_GAMEID"))) {
                    JSONObject jSONObject3 = new JSONObject();
                    try {
                        jSONObject3.putOpt(OneTrackParams.XMSdkParams.STEP, "getPropStr_USERINFO_AID");
                        jSONObject3.putOpt(ConstProp.USERINFO_AID, SdkMgr.getInst().getPropStr(ConstProp.USERINFO_AID, "-1"));
                    } catch (JSONException e3) {
                        UniSdkUtils.d(SdkNeteaseCodeScanner.TAG, "extraJson:" + e3.getMessage());
                    }
                    SdkNeteaseCodeScanner.this.saveClientLog(null, jSONObject3.toString());
                }
                SdkNeteaseCodeScanner sdkNeteaseCodeScanner = SdkNeteaseCodeScanner.this;
                sdkNeteaseCodeScanner.codeScannerDone(0, sdkNeteaseCodeScanner.callbackResult(str4, str5, str));
                String propStr4 = SdkMgr.getInst().getPropStr(ConstProp.UNISDK_JF_GAS3_URL);
                if (TextUtils.isEmpty(propStr4)) {
                    propStr4 = SdkNeteaseCodeScanner.this.getPropStr(ConstProp.UNISDK_JF_GAS3_URL);
                }
                if (!TextUtils.isEmpty(propStr4)) {
                    JSONObject jSONObject4 = new JSONObject();
                    try {
                        jSONObject4.put("index", str5);
                    } catch (JSONException e4) {
                        UniSdkUtils.e(SdkNeteaseCodeScanner.TAG, "indexJson JSONException:" + e4.getMessage());
                        e4.printStackTrace();
                    }
                    StringBuilder sb = new StringBuilder(propStr4);
                    if (propStr4.endsWith("/")) {
                        sb.append("query_index");
                    } else {
                        sb.append("/query_index");
                    }
                    UniSdkUtils.d(SdkNeteaseCodeScanner.TAG, "post json index, queryIndexUrl:" + sb.toString());
                    HTTPQueue.QueueItem queueItemNewQueueItem = HTTPQueue.NewQueueItem();
                    queueItemNewQueueItem.method = "POST";
                    queueItemNewQueueItem.url = sb.toString();
                    queueItemNewQueueItem.bSync = true;
                    queueItemNewQueueItem.leftRetry = 0;
                    queueItemNewQueueItem.setBody(jSONObject4.toString());
                    queueItemNewQueueItem.transParam = "UNISD_JF_GAS3_QUERY_INDEX";
                    queueItemNewQueueItem.callback = SdkNeteaseCodeScanner.this.new IndexCallback2(str4, str5);
                    String propStr5 = SdkMgr.getInst().getPropStr(ConstProp.JF_LOG_KEY);
                    if (!TextUtils.isEmpty(propStr5)) {
                        HashMap map2 = new HashMap();
                        try {
                            if (SdkMgr.getInst() != null && (SdkMgr.getInst().hasFeature(ConstProp.MODE_HAS_INTERFACE_PROTECTION) || "1.8.5".equalsIgnoreCase(SdkMgr.getBaseVersion()))) {
                                ApiRequestUtil.addSecureHeader(map2, propStr5, queueItemNewQueueItem.method, queueItemNewQueueItem.url, jSONObject4.toString(), true);
                            } else {
                                map2.put("X-Client-Sign", Crypto.hmacSHA256Signature(propStr5, SdkNeteaseCodeScanner.this.getSignSrc(queueItemNewQueueItem.method, queueItemNewQueueItem.url, jSONObject4.toString())));
                            }
                        } catch (Exception e5) {
                            UniSdkUtils.d(SdkNeteaseCodeScanner.TAG, "hmacSHA256Signature exception:" + e5.getMessage());
                        }
                        queueItemNewQueueItem.setHeaders(map2);
                    } else {
                        UniSdkUtils.d(SdkNeteaseCodeScanner.TAG, "JF_CLIENT_KEY empty");
                    }
                    HTTPQueue.getInstance(HTTPQueue.HTTPQUEUE_PAY).addItem(queueItemNewQueueItem);
                    return;
                }
                String propStr6 = SdkMgr.getInst().getPropStr(ConstProp.CODE_SCANNER_PAY_URL);
                if (TextUtils.isEmpty(propStr6)) {
                    propStr6 = SdkNeteaseCodeScanner.this.getPropStr(ConstProp.CODE_SCANNER_PAY_URL);
                }
                StringBuilder sb2 = new StringBuilder(propStr6);
                if (propStr6.endsWith("/")) {
                    sb2.append(String.format("queryindex?index=%s", str5));
                } else {
                    sb2.append(String.format("/queryindex?index=%s", str5));
                }
                UniSdkUtils.d(SdkNeteaseCodeScanner.TAG, "get index,queryIndexUrl:" + sb2.toString());
                NetUtil.wget(sb2.toString(), SdkNeteaseCodeScanner.this.new IndexCallback(str4, str5));
            }

            @Override // com.netease.mpay.ps.codescanner.CodeScannerCallback
            public void onFinish(CodeScannerRetCode codeScannerRetCode, String str4) throws JSONException {
                if (z) {
                    DefaultAuthRules.getInstance().notifyAuthDone(codeScannerRetCode.ordinal());
                }
                int i2 = AnonymousClass10.$SwitchMap$com$netease$mpay$ps$codescanner$CodeScannerRetCode[codeScannerRetCode.ordinal()];
                if (i2 == 1) {
                    UniSdkUtils.d(SdkNeteaseCodeScanner.TAG, propStr + "\u767b\u5f55\u6210\u529f\uff0c\u8fd4\u56de\u6e38\u620f");
                    SdkNeteaseCodeScanner sdkNeteaseCodeScanner = SdkNeteaseCodeScanner.this;
                    sdkNeteaseCodeScanner.codeScannerDone(0, sdkNeteaseCodeScanner.callbackResult("", "", str4));
                    onQrCodeScanLoginSuccess(propStr, propStr2);
                    return;
                }
                if (i2 == 2) {
                    UniSdkUtils.d(SdkNeteaseCodeScanner.TAG, "\u8bf7\u6c42\u53c2\u6570\u9519\u8bef\uff0c\u8fd4\u56de\u6e38\u620f");
                    SdkNeteaseCodeScanner sdkNeteaseCodeScanner2 = SdkNeteaseCodeScanner.this;
                    sdkNeteaseCodeScanner2.codeScannerDone(1, sdkNeteaseCodeScanner2.callbackResult("", "", str4));
                    return;
                }
                if (i2 == 3) {
                    UniSdkUtils.d(SdkNeteaseCodeScanner.TAG, "\u7528\u6237\u4e0d\u5339\u914d\uff0c\u8ba2\u5355\u5c5e\u4e8e" + str4 + "\uff0c\u8fd4\u56de\u6e38\u620f");
                    SdkNeteaseCodeScanner sdkNeteaseCodeScanner3 = SdkNeteaseCodeScanner.this;
                    sdkNeteaseCodeScanner3.codeScannerDone(2, sdkNeteaseCodeScanner3.callbackResult("", "", str4));
                    return;
                }
                if (i2 != 4) {
                    if (i2 != 5) {
                        return;
                    }
                    UniSdkUtils.d(SdkNeteaseCodeScanner.TAG, "\u4e8c\u7ef4\u7801\u65e0\u6548");
                    SdkNeteaseCodeScanner sdkNeteaseCodeScanner4 = SdkNeteaseCodeScanner.this;
                    sdkNeteaseCodeScanner4.codeScannerDone(5, sdkNeteaseCodeScanner4.callbackResult("", "", str4));
                    return;
                }
                UniSdkUtils.d(SdkNeteaseCodeScanner.TAG, "\u8fd4\u56de\u6e38\u620f");
                if (SdkNeteaseCodeScanner.this.isQuickQr()) {
                    SdkNeteaseCodeScanner.this.alertScanLoginQrCode(false);
                } else {
                    SdkNeteaseCodeScanner sdkNeteaseCodeScanner5 = SdkNeteaseCodeScanner.this;
                    sdkNeteaseCodeScanner5.codeScannerDone(3, sdkNeteaseCodeScanner5.callbackResult("", "", str4));
                }
            }

            private void onQrCodeScanLoginSuccess(String str4, String str5) throws JSONException {
                UniSdkUtils.d(SdkNeteaseCodeScanner.TAG, "onQrCodeScanLoginSuccess");
                JSONObject jSONObject3 = new JSONObject();
                try {
                    jSONObject3.put("methodId", "onQrCodeScanLoginSuccess");
                    jSONObject3.put("webUid", str4);
                    jSONObject3.put("webToken", str5);
                    SdkNeteaseCodeScanner.this.extendFuncCall(jSONObject3.toString());
                } catch (JSONException e3) {
                    UniSdkUtils.d(SdkNeteaseCodeScanner.TAG, "onQrCodeScanLoginSuccess JSONException:" + e3.getMessage());
                }
            }
        }, new CodeScannerExtCallback() { // from class: com.netease.ntunisdk.SdkNeteaseCodeScanner.8
            @Override // com.netease.mpay.ps.codescanner.CodeScannerExtCallback
            public void onFetchQrCode(String str4) {
                UniSdkUtils.d(SdkNeteaseCodeScanner.TAG, "CodeScannerExtCallback onFetchQrCode:" + str4);
                if (z) {
                    DefaultAuthRules.getInstance().notifyAuthDone(CodeScannerRetCode.PARAM_INVALID.ordinal());
                }
                SdkNeteaseCodeScanner.this.codeScannerDone(21, str4);
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.SdkNeteaseCodeScanner$10, reason: invalid class name */
    static /* synthetic */ class AnonymousClass10 {
        static final /* synthetic */ int[] $SwitchMap$com$netease$mpay$ps$codescanner$CodeScannerRetCode = new int[CodeScannerRetCode.values().length];

        static {
            try {
                $SwitchMap$com$netease$mpay$ps$codescanner$CodeScannerRetCode[CodeScannerRetCode.SUCCESS.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$netease$mpay$ps$codescanner$CodeScannerRetCode[CodeScannerRetCode.PARAM_INVALID.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$netease$mpay$ps$codescanner$CodeScannerRetCode[CodeScannerRetCode.UID_MISMATCH.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$netease$mpay$ps$codescanner$CodeScannerRetCode[CodeScannerRetCode.RETURN_GAME.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$netease$mpay$ps$codescanner$CodeScannerRetCode[CodeScannerRetCode.QR_CODE_INVALID.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    private static String stringMD5(byte[] bArr) throws NoSuchAlgorithmException {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(bArr);
            return byteArrayToHex(messageDigest.digest());
        } catch (NoSuchAlgorithmException unused) {
            return "";
        }
    }

    private static String byteArrayToHex(byte[] bArr) {
        char[] cArr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] cArr2 = new char[bArr.length * 2];
        int i = 0;
        for (byte b : bArr) {
            int i2 = i + 1;
            cArr2[i] = cArr[(b >>> 4) & 15];
            i = i2 + 1;
            cArr2[i2] = cArr[b & 15];
        }
        return new String(cArr2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String callbackResult(String str, String str2, String str3) throws JSONException {
        JSONObject jSONObject;
        try {
            if (TextUtils.isEmpty(str3)) {
                jSONObject = new JSONObject();
            } else {
                jSONObject = new JSONObject(str3);
            }
            jSONObject.put("uid", str);
            jSONObject.put("data_id", str2);
            return jSONObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            UniSdkUtils.e(TAG, "\u6570\u636ejson\u89e3\u6790\u9519\u8bef, extra:" + str3);
            return str3;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getSignSrc(String str, String str2, String str3) {
        String strReplace = str2.replace("://", "");
        String strSubstring = strReplace.contains("/") ? strReplace.substring(strReplace.indexOf("/")) : "";
        UniSdkUtils.d(TAG, "pathQs:" + strSubstring);
        return str.toUpperCase() + strSubstring + str3;
    }

    class IndexCallback implements WgetDoneCallback {
        private String dataId;
        private String uid;

        public IndexCallback(String str, String str2) {
            this.uid = str;
            this.dataId = str2;
        }

        @Override // com.netease.ntunisdk.base.utils.WgetDoneCallback
        public void ProcessResult(String str) throws JSONException {
            SdkNeteaseCodeScanner.this.handleProcessResult(this.uid, this.dataId, str);
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(14:18|19|123|20|(11:129|22|(4:24|(5:26|(6:28|(1:30)(5:33|34|119|35|(1:37)(2:38|(4:40|32|133|51)(2:41|(2:43|44))))|31|32|133|51)(1:49)|50|132|51)|131|52)|117|58|112|59|60|64|(1:66)(1:67)|68)|57|117|58|112|59|60|64|(0)(0)|68) */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x02c1, code lost:
    
        com.netease.ntunisdk.base.UniSdkUtils.e(r28, "aid parseInt exception");
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:66:0x02f3 A[Catch: UnsupportedEncodingException -> 0x037b, JSONException -> 0x0385, TryCatch #16 {UnsupportedEncodingException -> 0x037b, JSONException -> 0x0385, blocks: (B:64:0x02c4, B:66:0x02f3, B:68:0x02fc, B:67:0x02f7, B:63:0x02c1, B:82:0x0346), top: B:120:0x006c }] */
    /* JADX WARN: Removed duplicated region for block: B:67:0x02f7 A[Catch: UnsupportedEncodingException -> 0x037b, JSONException -> 0x0385, TryCatch #16 {UnsupportedEncodingException -> 0x037b, JSONException -> 0x0385, blocks: (B:64:0x02c4, B:66:0x02f3, B:68:0x02fc, B:67:0x02f7, B:63:0x02c1, B:82:0x0346), top: B:120:0x006c }] */
    /* JADX WARN: Type inference failed for: r1v0, types: [com.netease.ntunisdk.SdkNeteaseCodeScanner] */
    /* JADX WARN: Type inference failed for: r1v1 */
    /* JADX WARN: Type inference failed for: r1v10 */
    /* JADX WARN: Type inference failed for: r1v12 */
    /* JADX WARN: Type inference failed for: r1v13 */
    /* JADX WARN: Type inference failed for: r1v14 */
    /* JADX WARN: Type inference failed for: r1v17 */
    /* JADX WARN: Type inference failed for: r1v18 */
    /* JADX WARN: Type inference failed for: r1v2 */
    /* JADX WARN: Type inference failed for: r1v25, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r1v26 */
    /* JADX WARN: Type inference failed for: r1v27 */
    /* JADX WARN: Type inference failed for: r1v3, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r1v6, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r1v9 */
    /* JADX WARN: Type inference failed for: r3v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r3v1 */
    /* JADX WARN: Type inference failed for: r3v10 */
    /* JADX WARN: Type inference failed for: r3v11 */
    /* JADX WARN: Type inference failed for: r3v12 */
    /* JADX WARN: Type inference failed for: r3v13 */
    /* JADX WARN: Type inference failed for: r3v14 */
    /* JADX WARN: Type inference failed for: r3v15 */
    /* JADX WARN: Type inference failed for: r3v16 */
    /* JADX WARN: Type inference failed for: r3v19 */
    /* JADX WARN: Type inference failed for: r3v2 */
    /* JADX WARN: Type inference failed for: r3v20 */
    /* JADX WARN: Type inference failed for: r3v3 */
    /* JADX WARN: Type inference failed for: r3v34 */
    /* JADX WARN: Type inference failed for: r3v35 */
    /* JADX WARN: Type inference failed for: r3v4 */
    /* JADX WARN: Type inference failed for: r3v5, types: [com.netease.ntunisdk.SdkNeteaseCodeScanner] */
    /* JADX WARN: Type inference failed for: r3v52 */
    /* JADX WARN: Type inference failed for: r3v53 */
    /* JADX WARN: Type inference failed for: r3v57 */
    /* JADX WARN: Type inference failed for: r3v58 */
    /* JADX WARN: Type inference failed for: r3v6, types: [com.netease.ntunisdk.SdkNeteaseCodeScanner] */
    /* JADX WARN: Type inference failed for: r3v7 */
    /* JADX WARN: Type inference failed for: r3v8 */
    /* JADX WARN: Type inference failed for: r9v5, types: [java.lang.String] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void handleProcessResult(java.lang.String r30, java.lang.String r31, java.lang.String r32) throws org.json.JSONException {
        /*
            Method dump skipped, instructions count: 1035
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.SdkNeteaseCodeScanner.handleProcessResult(java.lang.String, java.lang.String, java.lang.String):void");
    }

    class IndexCallback2 implements HTTPCallback {
        private String dataId;
        private String uid;

        public IndexCallback2(String str, String str2) {
            this.uid = str;
            this.dataId = str2;
        }

        @Override // com.netease.ntunisdk.base.utils.HTTPCallback
        public boolean processResult(String str, String str2) throws JSONException {
            SdkNeteaseCodeScanner.this.handleProcessResult(this.uid, this.dataId, str);
            return false;
        }
    }

    class WebOrderCheckListener implements OnOrderCheckListener {
        private String dataId;
        private String uid;

        @Override // com.netease.ntunisdk.base.OnOrderCheckListener
        public void orderConsumeDone(OrderInfo orderInfo) {
        }

        public WebOrderCheckListener(String str, String str2) {
            this.uid = str;
            this.dataId = str2;
        }

        @Override // com.netease.ntunisdk.base.OnOrderCheckListener
        public void orderCheckDone(OrderInfo orderInfo) throws JSONException {
            SdkNeteaseCodeScanner.this.notifyOrderFinish(this.uid, this.dataId, orderInfo);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyOrderFinish(String str, String str2, OrderInfo orderInfo) throws JSONException {
        UniSdkUtils.d(TAG, "notifyOrderFinish, dataId:" + str2);
        int i = 1;
        if (2 == orderInfo.getOrderStatus() || 10 == orderInfo.getOrderStatus()) {
            i = 0;
        } else if (3 != orderInfo.getOrderStatus()) {
            orderInfo.getOrderStatus();
            i = 2;
        }
        this.scannerApi.notifyOrderFinish(str, str2, orderInfo.getSdkOrderId(), i);
        onQrCodeScanIndexPayFinish(orderInfo.getOrderStatus());
    }

    private void onQrCodeScanIndexPayFinish(int i) throws JSONException {
        UniSdkUtils.d(TAG, "onQrCodeScanIndexPayFinish");
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("methodId", "onQrCodeScanIndexPayFinish");
            jSONObject.put("orderStatus", i);
            extendFuncCall(jSONObject.toString());
        } catch (JSONException e) {
            UniSdkUtils.d(TAG, "onQrCodeScanIndexPayFinish JSONException:" + e.getMessage());
        }
    }
}