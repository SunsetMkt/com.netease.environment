package com.netease.ntunisdk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import com.netease.ntunisdk.ApiRecorder;
import com.netease.ntunisdk.CommonUI;
import com.netease.ntunisdk.base.AccountInfo;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.OnFinishInitListener;
import com.netease.ntunisdk.base.OrderInfo;
import com.netease.ntunisdk.base.QueryRankInfo;
import com.netease.ntunisdk.base.SdkBase;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.ShareInfo;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.base.utils.HTTPCallback;
import com.netease.ntunisdk.base.utils.HTTPCallbackExt;
import com.netease.ntunisdk.base.utils.HTTPQueue;
import com.netease.ntunisdk.base.utils.HashUtil;
import com.netease.ntunisdk.base.utils.NetUtil;
import com.netease.ntunisdk.base.utils.WgetDoneCallback;
import com.netease.ntunisdk.external.protocol.data.User;
import com.netease.ntunisdk.unilogger.global.Const;
import com.xiaomi.onetrack.OneTrack;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class SdkCommon extends SdkBase {
    private static final int CODE = Math.abs(-492869247) & 65535;
    private static final String SP_FILE_NAME = "ntcommonlogin";
    private static final String TAG = "UniSDK common";
    private static final int UNI_REQ_CODE_APIS = 13146;
    private static final int UNI_REQ_CODE_EXIT = 13145;
    private static final int UNI_REQ_CODE_LOGIN = 13141;
    private static final int UNI_REQ_CODE_MANAGER = 13143;
    private static final int UNI_REQ_CODE_ORDERINFO = 13148;
    private static final int UNI_REQ_CODE_PAY = 13142;
    private static final int UNI_REQ_CODE_PROTOCOL = 13144;
    private static final int UNI_REQ_CODE_USRINFO = 13147;
    private String SESSION;
    private String UID;
    private final LinkedList<String> cachedOrderId;
    private CommonUI commonUI;
    private String commonUrl;
    private boolean isCanTest;
    private boolean isInitSuccess;
    private boolean isLoginSuccess;
    private final HashMap<String, OrderInfo> orderHistory;
    private String reqBaseJson;
    private SharedPreferences sp;

    private static String emptyIfNull(String str) {
        return str == null ? "" : str;
    }

    public static String getChannelSts() {
        return "common";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendOuternetCheckApiData(String str) {
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    protected void doSepcialConfigVal(JSONObject jSONObject) {
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public String getSDKVersion() {
        return "1.9.3";
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public boolean hasPlatform(String str) {
        return true;
    }

    public SdkCommon(Context context) {
        super(context);
        this.commonUrl = "";
        this.UID = "%s_uid";
        this.SESSION = "%s_session";
        this.isInitSuccess = false;
        this.isLoginSuccess = false;
        this.isCanTest = true;
        this.orderHistory = new HashMap<>();
        this.cachedOrderId = new LinkedList<>();
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void init(final OnFinishInitListener onFinishInitListener) throws NumberFormatException {
        UniSdkUtils.d(TAG, "init...");
        setPropInt(ConstProp.MODE_HAS_LOGOUT, 1);
        setPropInt(ConstProp.MODE_HAS_MANAGER, 1);
        setPropInt(ConstProp.MODE_EXIT_VIEW, 1);
        setPropInt(ConstProp.MODE_HAS_GUEST, 1);
        setPropInt(ConstProp.MODE_HAS_GUEST_BIND, 1);
        setPropInt(ConstProp.MODE_HAS_FRIEND, 1);
        setPropInt(ConstProp.MODE_HAS_SHARE, 1);
        setPropInt(ConstProp.MODE_HAS_RANK, 1);
        setPropInt(ConstProp.MODE_HAS_QUERYSKUDETAILS, 1);
        this.commonUI = new CommonUI(this.myCtx);
        int appVersionCode = UniSdkUtils.getAppVersionCode(this.myCtx);
        if (TextUtils.isEmpty(getPropStr("JF_GAMEID"))) {
            UniSdkUtils.e(TAG, "common_data JF_GAMEID\u6ca1\u586b\u5199\uff0c\u503c\u4e3a\u6e38\u620f\u672c\u8eab\u4ee3\u53f7\uff0c\u5982\uff1ag1\u3001h1");
            onFinishInitListener.finishInit(1);
            this.hasInit = false;
            return;
        }
        this.commonUrl = String.format("%s?appid=%s&gameid=%s&packagename=%s&versioncode=%s&common_version=%s", CommonTips.intranetApiCheckUrl, getPropStr("JF_GAMEID"), getPropStr("JF_GAMEID"), UniSdkUtils.getAppPackageName(this.myCtx), Integer.valueOf(appVersionCode), getSDKVersion());
        sendCheckApiData("handleOnCreate", 1);
        this.sp = this.myCtx.getSharedPreferences(SP_FILE_NAME, 0);
        this.UID = String.format(this.UID, getPropStr("JF_GAMEID"));
        this.SESSION = String.format(this.SESSION, getPropStr("JF_GAMEID"));
        if (!this.isCanTest) {
            this.commonUI.show("\u521d\u59cb\u5316", CommonTips.OK_BTN, CommonTips.FAIL_BTN, "", "", new CommonUI.UICallback() { // from class: com.netease.ntunisdk.SdkCommon.1
                @Override // com.netease.ntunisdk.CommonUI.UICallback
                public void success(int i, String str) {
                    UniSdkUtils.d(SdkCommon.TAG, "sdk\u521d\u59cb\u5316\u6210\u529f...");
                    onFinishInitListener.finishInit(0);
                }

                @Override // com.netease.ntunisdk.CommonUI.UICallback
                public void fail(int i, String str) {
                    UniSdkUtils.d(SdkCommon.TAG, "sdk\u521d\u59cb\u5316\u5931\u8d25...");
                    onFinishInitListener.finishInit(1);
                    SdkCommon.this.hasInit = false;
                }
            });
            return;
        }
        ApiRecorder.init(this.sp);
        ApiRecorder.append(ApiRecorder.Type.init);
        this.isInitSuccess = true;
        int i = this.sp.getInt("suc_order_cnt", 0);
        if (i > 0) {
            for (int i2 = i - 1; i2 >= 0; i2 += -1) {
                this.cachedOrderId.add(this.sp.getString("order_index_" + i2, "ERROR"));
            }
        }
        if (onFinishInitListener != null) {
            onFinishInitListener.finishInit(0);
        }
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void sdkOnCreate(Bundle bundle) {
        super.sdkOnCreate(bundle);
        if (this.isCanTest) {
            ApiRecorder.append(ApiRecorder.Type.onCreate);
        }
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void sdkOnStart() {
        sendCheckApiData("handleOnStart", 1);
        if (this.isCanTest) {
            ApiRecorder.append(ApiRecorder.Type.onStart);
        }
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void sdkOnRestart() {
        sendCheckApiData("handleOnRestart", 1);
        if (this.isCanTest) {
            ApiRecorder.append(ApiRecorder.Type.onRestart);
        }
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void sdkOnResume() {
        sendCheckApiData("handleOnResume", 1);
        if (this.isCanTest) {
            ApiRecorder.append(ApiRecorder.Type.onResume);
        }
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void sdkOnPause() {
        sendCheckApiData("handleOnPause", 1);
        if (this.isCanTest) {
            ApiRecorder.append(ApiRecorder.Type.onPause);
        }
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void sdkOnStop() {
        sendCheckApiData("handleOnStop", 1);
        if (this.isCanTest) {
            ApiRecorder.append(ApiRecorder.Type.onStop);
        }
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void sdkOnSaveInstanceState(Bundle bundle) {
        sendCheckApiData("handleOnSaveInstanceState", 1);
        if (this.isCanTest) {
            ApiRecorder.append(ApiRecorder.Type.onSaveInstanceState);
        }
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void sdkOnActivityResult(int i, int i2, Intent intent) throws JSONException {
        sendCheckApiData("handleOnActivityResult", 1);
        if (this.isCanTest) {
            ApiRecorder.append(ApiRecorder.Type.onActivityResult);
            String stringExtra = intent != null ? intent.getStringExtra("extras") : "";
            switch (i) {
                case UNI_REQ_CODE_LOGIN /* 13141 */:
                    if (TextUtils.equals(stringExtra, "login_cancel")) {
                        resetCommonProp();
                        loginDone(1);
                        break;
                    } else if (TextUtils.equals(stringExtra, "do_login")) {
                        String stringExtra2 = intent != null ? intent.getStringExtra("account") : "";
                        serverLoginApi(stringExtra2, HashUtil.calculateStrHash("MD5", intent != null ? intent.getStringExtra("psw") : ""));
                        this.sp.edit().putString("account", stringExtra2).commit();
                        break;
                    }
                    break;
                case UNI_REQ_CODE_PAY /* 13142 */:
                    OrderInfo orderInfoJsonStr2Obj = OrderInfo.jsonStr2Obj(intent != null ? intent.getStringExtra(OneTrack.Event.ORDER) : "");
                    OrderInfo orderInfo = this.orderHistory.get(orderInfoJsonStr2Obj.getOrderId());
                    if (orderInfo != null) {
                        orderInfoJsonStr2Obj = orderInfo;
                    }
                    if (TextUtils.equals(stringExtra, "order_suc")) {
                        serverPayApi(orderInfoJsonStr2Obj);
                        break;
                    } else {
                        if (TextUtils.equals(stringExtra, "order_fail")) {
                            orderInfoJsonStr2Obj.setOrderStatus(3);
                            orderInfoJsonStr2Obj.setOrderErrReason("the fail button clicked");
                        } else if (TextUtils.equals(stringExtra, "order_unknown")) {
                            orderInfoJsonStr2Obj.setOrderStatus(1);
                            orderInfoJsonStr2Obj.setOrderErrReason("the unknown button clicked");
                        } else if (TextUtils.equals(stringExtra, "pay_cancel")) {
                            orderInfoJsonStr2Obj.setOrderStatus(11);
                            orderInfoJsonStr2Obj.setOrderErrReason("cancel");
                        }
                        checkOrderDone(orderInfoJsonStr2Obj);
                        break;
                    }
                case UNI_REQ_CODE_MANAGER /* 13143 */:
                    if (TextUtils.equals(stringExtra, User.USER_NAME_LOGOUT)) {
                        logout();
                        break;
                    } else if (TextUtils.equals(stringExtra, "on_new_intent")) {
                        try {
                            Method declaredMethod = Activity.class.getDeclaredMethod("onNewIntent", Intent.class);
                            declaredMethod.setAccessible(true);
                            Intent intent2 = new Intent();
                            intent2.putExtra("intintint", CODE);
                            declaredMethod.invoke(this.myCtx, intent2);
                            break;
                        } catch (Throwable th) {
                            th.printStackTrace();
                            return;
                        }
                    } else if (TextUtils.equals(stringExtra, "on_req_perm_result")) {
                        ((Activity) this.myCtx).onRequestPermissionsResult(CODE, new String[]{"android.permission.INTERNET"}, new int[]{0});
                        break;
                    } else if (TextUtils.equals(stringExtra, "orders")) {
                        showOrderInfo();
                        break;
                    }
                    break;
                case UNI_REQ_CODE_PROTOCOL /* 13144 */:
                    if (TextUtils.equals(stringExtra, "protocol_reject")) {
                        exitDone();
                        break;
                    }
                    break;
                case UNI_REQ_CODE_EXIT /* 13145 */:
                    if (TextUtils.equals(stringExtra, "exit_done")) {
                        exitDone();
                        break;
                    }
                    break;
            }
        }
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void sdkOnNewIntent(Intent intent) {
        sendCheckApiData("handleOnNewIntent", 1);
        if (this.isCanTest) {
            ApiRecorder.append(ApiRecorder.Type.onNewIntent);
            if (CODE == intent.getIntExtra("intintint", 0)) {
                Toast.makeText(this.myCtx, "onNewIntent \u6d4b\u8bd5\u6210\u529f", 1).show();
            }
        }
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void sdkOnRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (this.isCanTest) {
            ApiRecorder.append(ApiRecorder.Type.onRequestPermissionsResult);
            if (CODE == i) {
                Toast.makeText(this.myCtx, "onRequestPermissionsResult \u6d4b\u8bd5\u6210\u529f", 1).show();
            }
        }
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void sdkOnWindowFocusChanged(boolean z) {
        if (this.isCanTest) {
            ApiRecorder.append(ApiRecorder.Type.onWindowFocusChanged);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getSpString(String str) {
        return this.sp.getString(str, "");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSpString(String str, String str2) {
        this.sp.edit().putString(str, str2).commit();
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void login() throws JSONException {
        if (this.isInitSuccess) {
            if (!this.isCanTest) {
                this.commonUI.show("\u767b\u5f55", CommonTips.loginTips, new CommonUI.UICallback() { // from class: com.netease.ntunisdk.SdkCommon.2
                    @Override // com.netease.ntunisdk.CommonUI.UICallback
                    public void success(int i, String str) {
                        UniSdkUtils.d(SdkCommon.TAG, "\u8c03\u7528sdk\u767b\u5f55\u6210\u529f...");
                        SdkCommon sdkCommon = SdkCommon.this;
                        String spString = sdkCommon.getSpString(sdkCommon.UID);
                        SdkCommon sdkCommon2 = SdkCommon.this;
                        String spString2 = sdkCommon2.getSpString(sdkCommon2.SESSION);
                        if (TextUtils.isEmpty(spString) || TextUtils.isEmpty(spString2)) {
                            spString = SdkCommon.this.getPropStr("JF_GAMEID") + "_" + UUID.randomUUID().toString();
                            spString2 = SdkCommon.this.getPropStr("JF_GAMEID") + "_" + UUID.randomUUID().toString();
                            SdkCommon sdkCommon3 = SdkCommon.this;
                            sdkCommon3.setSpString(sdkCommon3.UID, spString);
                            SdkCommon sdkCommon4 = SdkCommon.this;
                            sdkCommon4.setSpString(sdkCommon4.SESSION, spString2);
                        }
                        SdkCommon.this.setPropStr(ConstProp.UID, spString);
                        SdkCommon.this.setPropStr(ConstProp.SESSION, spString2);
                        SdkCommon.this.setPropInt(ConstProp.LOGIN_STAT, 1);
                        SdkCommon.this.loginDone(0);
                    }

                    @Override // com.netease.ntunisdk.CommonUI.UICallback
                    public void fail(int i, String str) {
                        UniSdkUtils.d(SdkCommon.TAG, "\u8c03\u7528sdk\u767b\u5f55\u5931\u8d25...");
                        SdkCommon.this.resetCommonProp();
                        SdkCommon.this.loginDone(10);
                    }
                });
                confirmCheckApi();
            } else {
                ApiRecorder.append(ApiRecorder.Type.login);
                String string = this.sp.getString("account", null);
                String string2 = this.sp.getString("pswMd5", null);
                if (!TextUtils.isEmpty(string) && !TextUtils.isEmpty(string2)) {
                    serverLoginApi(string, string2);
                    return;
                }
                CommonSdkProxyActivity.login((Activity) this.myCtx, UNI_REQ_CODE_LOGIN, string);
            }
            sendCheckApiData("ntLogin", 1);
            return;
        }
        UniSdkUtils.e(TAG, "\u8bf7\u5728\u8c03\u7528ntInit\u6536\u5230finishInit\u6210\u529f\u56de\u8c03\u540e\uff0c\u63a5\u7740\u8c03\u7528\u63a5\u53e3ntCallbackSuccess(\"finishInit\")\uff0c\u7136\u540e\u518d\u8c03\u7528ntLogin\u63a5\u53e3");
        warnTips("\u8bf7\u5728\u8c03\u7528ntInit\u6536\u5230finishInit\u6210\u529f\u56de\u8c03\u540e\uff0c\u63a5\u7740\u8c03\u7528\u63a5\u53e3ntCallbackSuccess(\"finishInit\")\uff0c\u7136\u540e\u518d\u8c03\u7528ntLogin\u63a5\u53e3");
        resetCommonProp();
        loginDone(10);
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void getAnnouncementInfo() {
        sendCheckApiData("ntGetAnnouncementInfo", 1);
        if (this.isCanTest) {
            ApiRecorder.append(ApiRecorder.Type.getAnnouncementInfo);
        }
    }

    private void confirmCheckApi() {
        final String[] strArr = {"handleOnActivityResult", "handleOnNewIntent", "handleOnSaveInstanceState", "handleOnStop", "exitapi"};
        final boolean[] zArr = {false, false, false, false, false};
        new AlertDialog.Builder(this.myCtx).setTitle("\u96be\u4ee5\u81ea\u52a8\u89e6\u53d1\uff0c\u4e3b\u52a8\u68c0\u67e5\u662f\u5426\u8c03\u7528\u4ee5\u4e0b\u63a5\u53e3").setMultiChoiceItems(new String[]{"SdkMgr.getInst().handleOnActivityResult(requestCode, resultCode, data)", "SdkMgr.getInst().handleOnNewIntent(intent)", "SdkMgr.getInst().handleOnSaveInstanceState(outState);", "SdkMgr.getInst().handleOnStop()", "SdkMgr.getInst().exit()"}, zArr, new DialogInterface.OnMultiChoiceClickListener() { // from class: com.netease.ntunisdk.SdkCommon.4
            @Override // android.content.DialogInterface.OnMultiChoiceClickListener
            public void onClick(DialogInterface dialogInterface, int i, boolean z) {
                zArr[i] = z;
            }
        }).setPositiveButton(CommonTips.OK_BTN, new DialogInterface.OnClickListener() { // from class: com.netease.ntunisdk.SdkCommon.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                int i2 = 0;
                while (true) {
                    boolean[] zArr2 = zArr;
                    if (i2 >= zArr2.length) {
                        return;
                    }
                    if (zArr2[i2]) {
                        SdkCommon.this.sendCheckApiData(strArr[i2], 1);
                    }
                    i2++;
                }
            }
        }).show();
    }

    private void warnTips(String str) {
        new AlertDialog.Builder(this.myCtx).setTitle("\u63d0\u793a").setMessage(str).setPositiveButton(CommonTips.OK_BTN, new DialogInterface.OnClickListener() { // from class: com.netease.ntunisdk.SdkCommon.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).show();
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    protected void gameLoginSuccess() {
        UniSdkUtils.d(TAG, "\u5fc5\u987b\u8c03\u7528 ntGameLoginSuccess \u63a5\u53e3");
        sendCheckApiData("ntGameLoginSuccess", 1);
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public String getLoginSession() {
        return hasLogin() ? getPropStr(ConstProp.SESSION) : ConstProp.S_NOT_LOGIN_SESSION;
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public String getLoginUid() {
        return hasLogin() ? getPropStr(ConstProp.UID) : "";
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void checkOrder(final OrderInfo orderInfo) {
        if (!this.isCanTest) {
            this.commonUI.show("\u652f\u4ed8", CommonTips.checkOrderTips, new CommonUI.UICallback() { // from class: com.netease.ntunisdk.SdkCommon.6
                @Override // com.netease.ntunisdk.CommonUI.UICallback
                public void success(int i, String str) {
                    UniSdkUtils.d(SdkCommon.TAG, "\u8c03\u7528sdk\u652f\u4ed8\u6210\u529f...");
                    orderInfo.setOrderStatus(2);
                    SdkCommon.this.checkOrderDone(orderInfo);
                }

                @Override // com.netease.ntunisdk.CommonUI.UICallback
                public void fail(int i, String str) {
                    UniSdkUtils.d(SdkCommon.TAG, "\u8c03\u7528sdk\u652f\u4ed8\u5931\u8d25...");
                    orderInfo.setOrderStatus(3);
                    SdkCommon.this.checkOrderDone(orderInfo);
                }
            });
        } else if (!this.isInitSuccess) {
            orderInfo.setOrderStatus(3);
            orderInfo.setOrderErrReason("sdk not init");
            checkOrderDone(orderInfo);
        } else if (!hasLogin()) {
            orderInfo.setOrderStatus(3);
            orderInfo.setOrderErrReason("not login yet");
            checkOrderDone(orderInfo);
        } else if (TextUtils.isEmpty(orderInfo.getOrderId())) {
            orderInfo.setOrderStatus(3);
            orderInfo.setOrderErrReason("invalid orderId");
            checkOrderDone(orderInfo);
        } else {
            ApiRecorder.append(ApiRecorder.Type.inAppPurchase);
            this.orderHistory.put(orderInfo.getOrderId(), orderInfo);
            CommonSdkProxyActivity.pay((Activity) this.myCtx, UNI_REQ_CODE_PAY, OrderInfo.obj2Json(orderInfo).toString());
        }
        sendCheckApiData("ntCheckOrder", 1);
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void logout() {
        UniSdkUtils.d(TAG, "\u8c03\u7528\u9000\u51fa\u63a5\u53e3");
        if (!this.isCanTest) {
            this.commonUI.show("\u9000\u51fa", CommonTips.logoutTips, new CommonUI.UICallback() { // from class: com.netease.ntunisdk.SdkCommon.7
                @Override // com.netease.ntunisdk.CommonUI.UICallback
                public void success(int i, String str) {
                    UniSdkUtils.d(SdkCommon.TAG, "\u8c03\u7528sdk\u9000\u51fa\u6210\u529f...");
                    SdkCommon.this.resetCommonProp();
                    SdkCommon.this.logoutDone(0);
                }

                @Override // com.netease.ntunisdk.CommonUI.UICallback
                public void fail(int i, String str) {
                    UniSdkUtils.d(SdkCommon.TAG, "\u8c03\u7528sdk\u9000\u51fa\u5931\u8d25...");
                    SdkCommon.this.logoutDone(1);
                }
            });
        } else {
            ApiRecorder.append(ApiRecorder.Type.logout);
            resetCommonProp();
            logoutDone(0);
            this.sp.edit().putString("pswMd5", null).commit();
        }
        sendCheckApiData("ntLogout", 1);
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void setFloatBtnVisible(boolean z) {
        UniSdkUtils.d(TAG, "\u8c03\u7528\u6d6e\u6807\u63a5\u53e3");
        if (this.isLoginSuccess) {
            this.commonUI.show("\u6d6e\u6807", CommonTips.OK_BTN, CommonTips.floatBtnTips, null);
        } else {
            UniSdkUtils.e(TAG, "\u8bf7\u5728\u8c03\u7528ntLogin\u6536\u5230loginDone\u6210\u529f\u56de\u8c03\u540e\uff0c\u63a5\u7740\u8c03\u7528\u63a5\u53e3ntCallbackSuccess(\"loginDone\")\uff0c\u7136\u540e\u518d\u8c03\u7528ntSetFloatBtnVisible\u63a5\u53e3");
            warnTips("\u8bf7\u5728\u8c03\u7528ntLogin\u6536\u5230loginDone\u6210\u529f\u56de\u8c03\u540e\uff0c\u63a5\u7740\u8c03\u7528\u63a5\u53e3ntCallbackSuccess(\"loginDone\")\uff0c\u7136\u540e\u518d\u8c03\u7528ntSetFloatBtnVisible\u63a5\u53e3");
        }
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void openManager() throws JSONException {
        UniSdkUtils.d(TAG, "\u8c03\u7528\u7528\u6237\u4e2d\u5fc3\u63a5\u53e3");
        if (!this.isCanTest) {
            this.commonUI.show("\u7528\u6237\u4e2d\u5fc3", CommonTips.OK_BTN, CommonTips.openManagerTips, null);
        } else {
            ApiRecorder.append(ApiRecorder.Type.manager);
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.putOpt("apis", new JSONObject(getApiStateJson()));
                if (!ApiRecorder.has(ApiRecorder.Type.onNewIntent)) {
                    jSONObject.putOpt("todo", "on_new_intent");
                } else if (!ApiRecorder.has(ApiRecorder.Type.onRequestPermissionsResult)) {
                    jSONObject.putOpt("todo", "on_req_perm_result");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            CommonSdkProxyActivity.manager((Activity) this.myCtx, UNI_REQ_CODE_MANAGER, jSONObject.toString());
        }
        sendCheckApiData("ntOpenManager", 1);
    }

    @Override // com.netease.ntunisdk.base.SdkBase, com.netease.ntunisdk.base.GamerInterface
    public String getChannel() {
        return getChannelSts();
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void upLoadUserInfo() {
        UniSdkUtils.d(TAG, "\u8c03\u7528\u4e0a\u4f20\u7528\u6237\u4fe1\u606f\u63a5\u53e3");
        StringBuilder sb = new StringBuilder("\n\u4e0a\u4f20\u7c7b\u578b USERINFO_STAGE = ");
        sb.append(getPropStr(ConstProp.USERINFO_STAGE));
        sb.append("\n\u89d2\u8272id USERINFO_UID = ");
        sb.append(getPropStr(ConstProp.USERINFO_UID));
        sb.append("\n\u89d2\u8272\u6635\u79f0 USERINFO_NAME = ");
        sb.append(getPropStr(ConstProp.USERINFO_NAME));
        sb.append("\n\u89d2\u8272\u7b49\u7ea7 USERINFO_GRADE = ");
        sb.append(getPropStr(ConstProp.USERINFO_GRADE));
        sb.append("\n\u89d2\u8272vip\u7b49\u7ea7 USERINFO_VIP = ");
        sb.append(getPropStr(ConstProp.USERINFO_VIP));
        sb.append("\n\u89d2\u8272\u6240\u5c5e\u670d\u52a1\u5668id USERINFO_HOSTID = ");
        sb.append(getPropStr(ConstProp.USERINFO_HOSTID));
        sb.append("\n\u89d2\u8272\u6240\u5c5e\u670d\u52a1\u5668\u540d\u79f0 USERINFO_HOSTNAME = ");
        sb.append(getPropStr(ConstProp.USERINFO_HOSTNAME));
        sb.append("\n\u89d2\u8272\u7c7b\u578bid USERINFO_ROLE_TYPE_ID = ");
        sb.append(getPropStr(ConstProp.USERINFO_ROLE_TYPE_ID));
        sb.append("\n\u89d2\u8272\u7c7b\u578b\u540d\u79f0 USERINFO_ROLE_TYPE_NAME = ");
        sb.append(getPropStr(ConstProp.USERINFO_ROLE_TYPE_NAME));
        sb.append("\n\u95e8\u6d3e/\u804c\u4e1aid USERINFO_MENPAI_ID = ");
        sb.append(getPropStr(ConstProp.USERINFO_MENPAI_ID));
        sb.append("\n\u95e8\u6d3e/\u804c\u4e1a\u540d\u5b57 USERINFO_MENPAI_NAME = ");
        sb.append(getPropStr(ConstProp.USERINFO_MENPAI_NAME));
        sb.append("\n\u89d2\u8272\u6218\u529b USERINFO_CAPABILITY = ");
        sb.append(getPropStr(ConstProp.USERINFO_CAPABILITY));
        sb.append("\n\u89d2\u8272\u7684\u5e2e\u6d3eid USERINFO_GANG_ID = ");
        sb.append(getPropStr(ConstProp.USERINFO_GANG_ID));
        sb.append("\n\u89d2\u8272\u7684\u5e2e\u6d3e\u540d\u79f0 USERINFO_ORG = ");
        sb.append(getPropStr(ConstProp.USERINFO_ORG));
        sb.append("\n\u670d\u52a1\u5668\u533a\u57dfid USERINFO_REGION_ID = ");
        sb.append(getPropStr(ConstProp.USERINFO_REGION_ID));
        sb.append("\n\u670d\u52a1\u5668\u533a\u57df\u540d\u5b57 USERINFO_REGION_NAME = ");
        sb.append(getPropStr(ConstProp.USERINFO_REGION_NAME));
        sb.append("\n");
        UniSdkUtils.d(TAG, "ntUpLoadUserInfo stage:" + getPropStr(ConstProp.USERINFO_STAGE));
        String string = sb.toString();
        StringBuilder sb2 = new StringBuilder("stage:");
        boolean z = true;
        boolean z2 = false;
        if (ConstProp.USERINFO_STAGE_CREATE_ROLE.equals(getPropStr(ConstProp.USERINFO_STAGE))) {
            sendCheckApiData("ntUpLoadUserInfo_create_role", 1);
            if (this.isCanTest) {
                ApiRecorder.append(ApiRecorder.Stage.createRole);
                sb2.append("\u521b\u5efa\u89d2\u8272");
            } else {
                z = z2;
            }
        } else {
            if (ConstProp.USERINFO_STAGE_LEVEL_UP.equals(getPropStr(ConstProp.USERINFO_STAGE))) {
                sendCheckApiData("ntUpLoadUserInfo_level_up", 1);
                if (this.isCanTest) {
                    ApiRecorder.append(ApiRecorder.Stage.levelUp);
                    sb2.append("\u89d2\u8272\u5347\u7ea7");
                }
            } else if (ConstProp.USERINFO_STAGE_PAY.equals(getPropStr(ConstProp.USERINFO_STAGE))) {
                sendCheckApiData("ntUpLoadUserInfo_pay", 1);
            } else if (ConstProp.USERINFO_STAGE_PAY_SUCESS.equals(getPropStr(ConstProp.USERINFO_STAGE))) {
                sendCheckApiData("ntUpLoadUserInfo_pay_sucess", 1);
            } else if (ConstProp.USERINFO_STAGE_LEAVE_SERVER.equals(getPropStr(ConstProp.USERINFO_STAGE))) {
                sendCheckApiData("ntUpLoadUserInfo_leave_server", 1);
            } else {
                if (!this.isCanTest) {
                    this.commonUI.show("\u4e0a\u4f20\u7528\u6237\u4fe1\u606f", "\u786e\u5b9a(\u8bf7\u68c0\u67e5\u4ee5\u4e0b\u503c\u662f\u5426\u7b26\u5408\u6587\u6863\u8981\u6c42)", "", string, CommonTips.upLoadUserInfoTips, null);
                } else {
                    ApiRecorder.append(ApiRecorder.Stage.enterGame);
                    sb2.append("\u8fdb\u5165\u6e38\u620f");
                    z2 = true;
                }
                sendCheckApiData("ntUpLoadUserInfo_enter_server", 1);
            }
            z = z2;
        }
        if (this.isCanTest && z) {
            ApiRecorder.append(ApiRecorder.Type.uploadUserInfo);
            String string2 = sb2.toString();
            CommonSdkProxyActivity.userinfo((Activity) this.myCtx, UNI_REQ_CODE_USRINFO, string2 + "\n" + string);
        }
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public boolean openExitView() {
        if (!this.isCanTest) {
            this.commonUI.show("\u9000\u51fa\u9875", "\u6e20\u9053\u6709\u9000\u51fa\u9875", "\u6e20\u9053\u65e0\u9000\u51fa\u9875", "", CommonTips.openExitViewTips, new CommonUI.UICallback() { // from class: com.netease.ntunisdk.SdkCommon.8
                @Override // com.netease.ntunisdk.CommonUI.UICallback
                public void success(int i, String str) {
                    Toast.makeText(SdkCommon.this.myCtx, "\u7531\u6e20\u9053\u63d0\u4f9b\u7684\u754c\u9762\u9000\u51fa", 0).show();
                    new AlertDialog.Builder(SdkCommon.this.myCtx).setTitle("\u6e20\u9053\u63d0\u4f9b\u7684\u9000\u51fa\u754c\u9762").setMessage("\u786e\u5b9a\u9000\u51fa\u6e38\u620f\u4e48\uff1f").setPositiveButton(CommonTips.OK_BTN, new DialogInterface.OnClickListener() { // from class: com.netease.ntunisdk.SdkCommon.8.2
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i2) {
                            SdkCommon.this.exitDone();
                        }
                    }).setNegativeButton("\u53d6\u6d88", new DialogInterface.OnClickListener() { // from class: com.netease.ntunisdk.SdkCommon.8.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i2) {
                        }
                    }).create().show();
                }

                @Override // com.netease.ntunisdk.CommonUI.UICallback
                public void fail(int i, String str) {
                    Toast.makeText(SdkCommon.this.myCtx, "\u7531\u6e38\u620f\u63d0\u4f9b\u7684\u754c\u9762\u9000\u51fa", 0).show();
                    SdkCommon.this.openExitViewFailed();
                }
            });
        } else {
            ApiRecorder.append(ApiRecorder.Type.openExitView);
            CommonSdkProxyActivity.exit((Activity) this.myCtx, UNI_REQ_CODE_EXIT);
        }
        sendCheckApiData("ntOpenExitView", 1);
        return true;
    }

    @Override // com.netease.ntunisdk.base.SdkBase, com.netease.ntunisdk.base.GamerInterface
    public void exit() {
        sendCheckApiData("exitapi", 1);
        if (this.isCanTest) {
            ApiRecorder.append(ApiRecorder.Type.exit);
        }
        super.exit();
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void queryFriendList() {
        Toast.makeText(this.myCtx, "\u8c03\u7528sdk\u597d\u53cb\u5217\u8868\u6210\u529f...", 0).show();
        ArrayList arrayList = new ArrayList();
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setAccountId("accountId");
        arrayList.add(accountInfo);
        queryFriendListFinished(arrayList);
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void queryAvailablesInvitees() {
        Toast.makeText(this.myCtx, "\u8c03\u7528sdk\u53ef\u4ee5\u6dfb\u52a0\u7684\u597d\u53cb\u5217\u8868\u6210\u529f...", 0).show();
        ArrayList arrayList = new ArrayList();
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setAccountId("accountId_apply");
        arrayList.add(accountInfo);
        queryAvailablesInviteesFinished(arrayList);
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void applyFriend(String str) {
        Toast.makeText(this.myCtx, "\u8c03\u7528sdk\u52a0\u597d\u53cb\u6210\u529f...", 0).show();
        applyFriendFinished(true);
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void queryMyAccount() {
        Toast.makeText(this.myCtx, "\u8c03\u7528sdk\u83b7\u53d6\u81ea\u5df1\u7684\u8d26\u6237\u4fe1\u606f\u6210\u529f...", 0).show();
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setAccountId("myAccount");
        accountInfo.setNickname("myNickname");
        queryMyAccountFinished(accountInfo);
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void queryRank(QueryRankInfo queryRankInfo) {
        Toast.makeText(this.myCtx, "\u8c03\u7528sdk\u83b7\u53d6\u6392\u884c\u699c\u4fe1\u606f\u6210\u529f...", 0).show();
        ArrayList arrayList = new ArrayList();
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setAccountId("my");
        accountInfo.setRankScore(100.0d);
        accountInfo.setRank(1L);
        arrayList.add(accountInfo);
        queryRankFinished(arrayList);
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void updateRank(String str, double d) {
        Toast.makeText(this.myCtx, "\u8c03\u7528sdk\u4e0a\u4f20\u672c\u73a9\u5bb6\u7684\u5206\u6570\u5230\u6392\u884c\u699c\u6210\u529f...", 0).show();
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void share(ShareInfo shareInfo) {
        this.commonUI.show("\u5206\u4eab", CommonTips.shareTips, new CommonUI.UICallback() { // from class: com.netease.ntunisdk.SdkCommon.9
            @Override // com.netease.ntunisdk.CommonUI.UICallback
            public void success(int i, String str) {
                SdkCommon.this.shareFinished(true);
            }

            @Override // com.netease.ntunisdk.CommonUI.UICallback
            public void fail(int i, String str) {
                SdkCommon.this.shareFinished(false);
            }
        });
        sendCheckApiData("ntShare", 1);
    }

    @Override // com.netease.ntunisdk.base.SdkBase, com.netease.ntunisdk.base.GamerInterface
    public void ntShowCompactView(final boolean z) {
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.SdkCommon.10
            @Override // java.lang.Runnable
            public void run() {
                SdkCommon.this.showCompactView(z);
            }
        });
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void showCompactView(boolean z) {
        if (this.isCanTest) {
            ApiRecorder.append(ApiRecorder.Type.userProtocol);
            CommonSdkProxyActivity.protocol((Activity) this.myCtx, UNI_REQ_CODE_PROTOCOL, z);
        }
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    protected String getUniSDKVersion() {
        return getSDKVersion();
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void callbackSuccess(String str) {
        UniSdkUtils.d(TAG, "ntCallbackSuccess " + str);
        if ("finishInit".equals(str)) {
            this.isInitSuccess = true;
        } else if ("loginDone".equals(str)) {
            this.isLoginSuccess = true;
        }
        sendCheckApiData(str + "_cb_success", 1);
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void callbackFail(String str) {
        UniSdkUtils.d(TAG, "ntCallbackFail " + str);
        sendCheckApiData(str + "_cb_fail", 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendCheckApiData(final String str, int i) {
        final String str2 = String.format("%s&action=%s&actionValue=%s", this.commonUrl, str, Integer.valueOf(i));
        UniSdkUtils.d(TAG, "sendCheckApiData url:" + str2);
        NetUtil.wget(str2, new WgetDoneCallback() { // from class: com.netease.ntunisdk.SdkCommon.11
            @Override // com.netease.ntunisdk.base.utils.WgetDoneCallback
            public void ProcessResult(String str3) {
                if (str3 == null) {
                    SdkCommon.this.sendOuternetCheckApiData(str2);
                    return;
                }
                UniSdkUtils.d(SdkCommon.TAG, "sendCheckApiData " + str + ",result:" + str3);
            }
        });
    }

    private void showOrderInfo() {
        if (this.cachedOrderId.isEmpty()) {
            UniSdkUtils.e(TAG, "no marked orders");
        } else {
            final String[] strArr = (String[]) this.cachedOrderId.toArray(new String[0]);
            new AlertDialog.Builder(this.myCtx).setTitle("\u5df2\u5b8c\u6210\u652f\u4ed8\u8ba2\u5355").setItems(strArr, new DialogInterface.OnClickListener() { // from class: com.netease.ntunisdk.SdkCommon.12
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    String str = strArr[i];
                    String string = SdkCommon.this.sp.getString(str, null);
                    if (string != null) {
                        CommonSdkProxyActivity.orderinfo((Activity) SdkCommon.this.myCtx, SdkCommon.UNI_REQ_CODE_ORDERINFO, string);
                    } else if (SdkCommon.this.hasLogin()) {
                        SdkCommon.this.serverOrderInfo(str.substring(str.indexOf(")") + 1));
                    } else {
                        UniSdkUtils.e(SdkCommon.TAG, "not login yet");
                    }
                }
            }).create().show();
        }
    }

    private String getApiStateJson() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        for (ApiRecorder.Type type : ApiRecorder.Type.values()) {
            StringBuilder sb3 = ApiRecorder.has(type) ? sb : sb2;
            if (sb3.length() > 0) {
                sb3.append(" / ");
            }
            sb3.append(type);
        }
        String string = sb.toString();
        String string2 = sb2.toString();
        StringBuilder sb4 = new StringBuilder();
        StringBuilder sb5 = new StringBuilder();
        for (ApiRecorder.Stage stage : ApiRecorder.Stage.values()) {
            StringBuilder sb6 = ApiRecorder.has(stage) ? sb4 : sb5;
            if (sb6.length() > 0) {
                sb6.append(" / ");
            }
            sb6.append(stage);
        }
        try {
            jSONObject.putOpt("ed", "\u5df2\u8c03\u7528\u8fc7:\n" + string + "\n[stage]" + sb4.toString());
            jSONObject.putOpt("un", "\u672a\u8c03\u7528\u8fc7:\n" + string2 + "\n[stage]" + sb5.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jSONObject.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runOnUiThread(Runnable runnable) {
        ((Activity) this.myCtx).runOnUiThread(runnable);
    }

    private String getReqBaseJson() throws JSONException {
        if (this.reqBaseJson == null) {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.putOpt("cp", "a");
                jSONObject.putOpt("udid", getUdid());
                jSONObject.putOpt("cv", SdkMgr.getBaseVersion());
                this.reqBaseJson = jSONObject.toString();
            } catch (Exception unused) {
            }
        }
        return this.reqBaseJson;
    }

    private String getReqUrlPrefix() {
        return getPropStr(ConstProp.DOMAIN, "https://basechannel.unisdk.netease.com/noah-common");
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x005a  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x005f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void serverLoginApi(final java.lang.String r5, final java.lang.String r6) throws org.json.JSONException {
        /*
            r4 = this;
            java.lang.String r0 = r4.getReqUrlPrefix()
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r0)
            java.lang.String r2 = "/"
            boolean r0 = r0.endsWith(r2)
            if (r0 == 0) goto L16
            java.lang.String r2 = ""
        L16:
            r1.append(r2)
            java.lang.String r0 = "api/login"
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            r1 = 0
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch: java.lang.Exception -> L43
            java.lang.String r3 = r4.getReqBaseJson()     // Catch: java.lang.Exception -> L43
            r2.<init>(r3)     // Catch: java.lang.Exception -> L43
            java.lang.String r1 = "username"
            r2.putOpt(r1, r5)     // Catch: java.lang.Exception -> L42
            java.lang.String r1 = "password"
            r2.putOpt(r1, r6)     // Catch: java.lang.Exception -> L42
            java.lang.String r1 = "gameid"
            java.lang.String r3 = "JF_GAMEID"
            java.lang.String r3 = r4.getPropStr(r3)     // Catch: java.lang.Exception -> L42
            r2.putOpt(r1, r3)     // Catch: java.lang.Exception -> L42
            goto L44
        L42:
            r1 = r2
        L43:
            r2 = r1
        L44:
            com.netease.ntunisdk.base.utils.HTTPQueue$QueueItem r1 = com.netease.ntunisdk.base.utils.HTTPQueue.NewQueueItem()
            java.lang.String r3 = "POST"
            r1.method = r3
            r1.url = r0
            r0 = 1
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r0)
            r1.bSync = r0
            r0 = 0
            r1.leftRetry = r0
            if (r2 == 0) goto L5f
            java.lang.String r0 = r2.toString()
            goto L61
        L5f:
            java.lang.String r0 = "{}"
        L61:
            r1.setBody(r0)
            r0 = 4000(0xfa0, float:5.605E-42)
            r1.connectionTimeout = r0
            r1.soTimeout = r0
            java.lang.String r0 = "servLoginApi"
            r1.transParam = r0
            com.netease.ntunisdk.SdkCommon$13 r0 = new com.netease.ntunisdk.SdkCommon$13
            r0.<init>()
            com.netease.ntunisdk.base.utils.HTTPCallback r5 = getHttpReqCb(r0)
            r1.callback = r5
            java.lang.String r5 = "PAY"
            com.netease.ntunisdk.base.utils.HTTPQueue r5 = com.netease.ntunisdk.base.utils.HTTPQueue.getInstance(r5)
            r5.addItem(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.SdkCommon.serverLoginApi(java.lang.String, java.lang.String):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x007e  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0083  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void serverPayApi(final com.netease.ntunisdk.base.OrderInfo r8) throws org.json.JSONException {
        /*
            r7 = this;
            java.lang.String r0 = r7.getReqUrlPrefix()
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r0)
            java.lang.String r2 = "/"
            boolean r0 = r0.endsWith(r2)
            java.lang.String r3 = ""
            if (r0 == 0) goto L17
            r2 = r3
        L17:
            r1.append(r2)
            java.lang.String r0 = "api/finish_order"
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            java.lang.String r1 = r8.getSdkOrderId()
            java.lang.String r1 = emptyIfNull(r1)
            java.lang.String r2 = "SESSION"
            java.lang.String r4 = "not_login"
            java.lang.String r2 = r7.getPropStr(r2, r4)
            java.lang.String r4 = "UIN"
            java.lang.String r3 = r7.getPropStr(r4, r3)
            r4 = 0
            org.json.JSONObject r5 = new org.json.JSONObject     // Catch: java.lang.Exception -> L67
            java.lang.String r6 = r7.getReqBaseJson()     // Catch: java.lang.Exception -> L67
            r5.<init>(r6)     // Catch: java.lang.Exception -> L67
            java.lang.String r4 = "consumesn"
            r5.putOpt(r4, r1)     // Catch: java.lang.Exception -> L66
            java.lang.String r4 = "token"
            r5.putOpt(r4, r2)     // Catch: java.lang.Exception -> L66
            int r2 = java.lang.Integer.parseInt(r3)     // Catch: java.lang.Exception -> L66
            java.lang.String r3 = "userid"
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch: java.lang.Exception -> L66
            r5.putOpt(r3, r2)     // Catch: java.lang.Exception -> L66
            java.lang.String r2 = "gameid"
            java.lang.String r3 = "JF_GAMEID"
            java.lang.String r3 = r7.getPropStr(r3)     // Catch: java.lang.Exception -> L66
            r5.putOpt(r2, r3)     // Catch: java.lang.Exception -> L66
            goto L68
        L66:
            r4 = r5
        L67:
            r5 = r4
        L68:
            com.netease.ntunisdk.base.utils.HTTPQueue$QueueItem r2 = com.netease.ntunisdk.base.utils.HTTPQueue.NewQueueItem()
            java.lang.String r3 = "POST"
            r2.method = r3
            r2.url = r0
            r0 = 1
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r0)
            r2.bSync = r0
            r0 = 0
            r2.leftRetry = r0
            if (r5 == 0) goto L83
            java.lang.String r0 = r5.toString()
            goto L85
        L83:
            java.lang.String r0 = "{}"
        L85:
            r2.setBody(r0)
            r0 = 5000(0x1388, float:7.006E-42)
            r2.connectionTimeout = r0
            r2.soTimeout = r0
            java.lang.String r0 = "serverPayApi"
            r2.transParam = r0
            com.netease.ntunisdk.SdkCommon$14 r0 = new com.netease.ntunisdk.SdkCommon$14
            r0.<init>()
            com.netease.ntunisdk.base.utils.HTTPCallback r8 = getHttpReqCb(r0)
            r2.callback = r8
            java.lang.String r8 = "PAY"
            com.netease.ntunisdk.base.utils.HTTPQueue r8 = com.netease.ntunisdk.base.utils.HTTPQueue.getInstance(r8)
            r8.addItem(r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.SdkCommon.serverPayApi(com.netease.ntunisdk.base.OrderInfo):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void serverOrderInfo(final String str) {
        String reqUrlPrefix = getReqUrlPrefix();
        StringBuilder sb = new StringBuilder();
        sb.append(reqUrlPrefix);
        sb.append(reqUrlPrefix.endsWith("/") ? "" : "/");
        sb.append("api/order_info?consumesn=%s&userid=%s&token=%s&gameid=%s&cp=a&cv=%s&udid=%s");
        String str2 = String.format(sb.toString(), str, getPropStr(ConstProp.UID, ""), getPropStr(ConstProp.SESSION, ConstProp.S_NOT_LOGIN_SESSION), getPropStr("JF_GAMEID"), SdkMgr.getBaseVersion(), getUdid());
        HTTPQueue.QueueItem queueItemNewQueueItem = HTTPQueue.NewQueueItem();
        queueItemNewQueueItem.method = "GET";
        queueItemNewQueueItem.url = str2;
        queueItemNewQueueItem.bSync = true;
        queueItemNewQueueItem.leftRetry = 0;
        queueItemNewQueueItem.connectionTimeout = 5000;
        queueItemNewQueueItem.soTimeout = 5000;
        queueItemNewQueueItem.transParam = "serverOrderInfo";
        queueItemNewQueueItem.callback = getHttpReqCb(new HTTPCallback() { // from class: com.netease.ntunisdk.SdkCommon.15
            @Override // com.netease.ntunisdk.base.utils.HTTPCallback
            public boolean processResult(String str3, String str4) {
                UniSdkUtils.i(SdkCommon.TAG, "serverOrderInfo transParam: " + str4);
                UniSdkUtils.i(SdkCommon.TAG, "serverOrderInfo result: " + str3);
                if (TextUtils.isEmpty(str3)) {
                    UniSdkUtils.e(SdkCommon.TAG, "/api/order_info response error");
                    return false;
                }
                try {
                    JSONObject jSONObject = new JSONObject(str3);
                    int iOptInt = jSONObject.optInt("code");
                    String strOptString = jSONObject.optString("status");
                    if (iOptInt != 0) {
                        UniSdkUtils.e(SdkCommon.TAG, "/api/order_info : " + iOptInt + "/" + strOptString);
                        return false;
                    }
                    JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject(OneTrack.Event.ORDER);
                    if (jSONObjectOptJSONObject == null) {
                        return false;
                    }
                    String strOptString2 = jSONObjectOptJSONObject.optString("consumesn");
                    if (!TextUtils.equals(strOptString2, strOptString2)) {
                        UniSdkUtils.e(SdkCommon.TAG, "consumesn not identical");
                        return false;
                    }
                    StringBuilder sb2 = new StringBuilder("\u6e38\u620fid : ");
                    String strValueOf = String.valueOf(jSONObjectOptJSONObject.optInt("money"));
                    sb2.append(jSONObjectOptJSONObject.optString("gameid"));
                    sb2.append("\n\u670d\u52a1\u5668id : ");
                    sb2.append(jSONObjectOptJSONObject.optString("hostid"));
                    sb2.append("\n\u7528\u6237id : ");
                    sb2.append(jSONObjectOptJSONObject.optInt("userid"));
                    sb2.append("\n\u89d2\u8272id : ");
                    sb2.append(jSONObjectOptJSONObject.optString(Const.CONFIG_KEY.ROLEID));
                    sb2.append("\n\u5546\u54c1\u5355\u4ef7 : ");
                    sb2.append(strValueOf.substring(0, strValueOf.length() - 2));
                    sb2.append(".");
                    sb2.append(strValueOf.substring(strValueOf.length() - 2));
                    sb2.append("\n\u6e38\u620f\u8ba2\u5355id : ");
                    sb2.append(jSONObjectOptJSONObject.optString("gamesn"));
                    sb2.append("\n\u5e73\u53f0\u8ba2\u5355id : ");
                    sb2.append(jSONObjectOptJSONObject.optString("consumesn"));
                    sb2.append("\n\u5546\u54c1id : ");
                    sb2.append(jSONObjectOptJSONObject.optString("goodsid"));
                    sb2.append("\n\u5546\u54c1\u6570\u91cf : ");
                    sb2.append(jSONObjectOptJSONObject.optInt("goodscount"));
                    sb2.append("\n\u5546\u54c1\u540d : ");
                    sb2.append(jSONObjectOptJSONObject.optString("goodsname"));
                    sb2.append("\n\u8ba2\u5355\u72b6\u6001 : ");
                    sb2.append(1 == jSONObjectOptJSONObject.optInt("orderstatus") ? "\u652f\u4ed8\u6210\u529f" : "\u672a\u652f\u4ed8");
                    sb2.append("\n");
                    final String string = sb2.toString();
                    try {
                        SdkCommon.this.sp.edit().putString(str, string).commit();
                        SdkCommon.this.runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.SdkCommon.15.1
                            @Override // java.lang.Runnable
                            public void run() {
                                CommonSdkProxyActivity.orderinfo((Activity) SdkCommon.this.myCtx, SdkCommon.UNI_REQ_CODE_ORDERINFO, string);
                            }
                        });
                        return false;
                    } catch (Exception e) {
                        e = e;
                        e.printStackTrace();
                        return false;
                    }
                } catch (Exception e2) {
                    e = e2;
                }
            }
        });
        HTTPQueue.getInstance(HTTPQueue.HTTPQUEUE_PAY).addItem(queueItemNewQueueItem);
    }

    private static HTTPCallback getHttpReqCb(final HTTPCallback hTTPCallback) {
        return SdkMgr.getBaseVersion().compareToIgnoreCase("1.6.2") >= 0 ? new HTTPCallbackExt() { // from class: com.netease.ntunisdk.SdkCommon.16
            @Override // com.netease.ntunisdk.base.utils.HTTPCallback
            public boolean processResult(String str, String str2) {
                return hTTPCallback.processResult(str, str2);
            }
        } : hTTPCallback;
    }
}