package com.netease.ntunisdk.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import com.netease.ntunisdk.base.callback.ExtendFuncCallback;
import com.netease.ntunisdk.base.callback.InitCallback;
import com.netease.ntunisdk.base.callback.LoginCallback;
import com.netease.ntunisdk.base.callback.LogoutCallback;
import com.netease.ntunisdk.base.callback.OrderCallback;
import com.netease.ntunisdk.base.callback.ShareCallback;
import com.netease.ntunisdk.base.constant.a;
import com.netease.ntunisdk.base.constant.b;
import com.netease.ntunisdk.base.model.SdkAccount;
import com.netease.ntunisdk.base.model.SdkFeature;
import com.netease.ntunisdk.base.model.SdkInit;
import com.netease.ntunisdk.base.model.SdkModelMerger;
import com.netease.ntunisdk.base.model.SdkOrder;
import com.netease.ntunisdk.base.model.SdkRole;
import com.netease.ntunisdk.base.model.SdkShare;
import com.netease.ntunisdk.base.model.SdkStage;
import com.netease.ntunisdk.base.model.SdkState;
import com.netease.ntunisdk.base.utils.ResUtils;
import com.netease.ntunisdk.base.view.Alerter;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class SDKManager implements OnExitListener {
    private static final String TAG = "SDKManager";
    private static SDKManager instance;
    private WeakReference<Bundle> bundleRef;
    private WeakReference<Context> contextRef;
    private ExtendFuncCallback extendFuncCallback;
    private InitCallback initCallback;
    private LoginCallback loginCallback;
    private LogoutCallback logoutCallback;
    private OrderCallback orderCallback;
    private ShareCallback shareCallback;
    private final HashMap<String, String> tmpKeyValues = new HashMap<>();

    private SDKManager() {
    }

    public static SDKManager getInstance() {
        if (instance == null) {
            instance = new SDKManager();
        }
        return instance;
    }

    public void setInitCallback(InitCallback initCallback) {
        this.initCallback = initCallback;
    }

    public void sdkInit(Context context) throws IOException, SecurityException {
        this.contextRef = new WeakReference<>(context);
        SdkMgr.init(context);
        SdkMgr.getInst().setPropInt(ConstProp.UNISDK_JF_GAS3, 1);
        SdkMgr.getInst().setPropInt(ConstProp.ENABLE_CLIENT_CHECK_REALNAME, 1);
        for (Map.Entry<String, String> entry : this.tmpKeyValues.entrySet()) {
            SdkMgr.getInst().setPropStr(entry.getKey(), entry.getValue());
        }
        this.tmpKeyValues.clear();
        SdkMgr.getInst().ntInit(new OnFinishInitListener() { // from class: com.netease.ntunisdk.base.SDKManager.1
            @Override // com.netease.ntunisdk.base.OnFinishInitListener
            public void finishInit(int i) {
                SdkMgr.getInst().setExitListener(SDKManager.this, 1);
                if (SDKManager.this.initCallback != null) {
                    SdkInit sdkInit = new SdkInit();
                    sdkInit.wrapFrom(null);
                    sdkInit.code = (i == 0 ? a.Suc : a.Fail).ordinal();
                    sdkInit.subcode = i;
                    sdkInit.message = (i == 0 ? a.Suc : a.Fail).d;
                    SDKManager.this.initCallback.onInitFinished(sdkInit.code, sdkInit.message, sdkInit.toString());
                }
            }
        });
        WeakReference<Bundle> weakReference = this.bundleRef;
        if (weakReference != null) {
            onCreate(weakReference.get());
        }
    }

    public String sdkChannel() {
        return SdkMgr.getInst() == null ? "sdk not init" : SdkMgr.getInst().getChannel();
    }

    public String appChannel() {
        return SdkMgr.getInst() == null ? "sdk not init" : SdkMgr.getInst().getAppChannel();
    }

    public String udid() {
        return SdkMgr.getInst() == null ? "sdk not init" : SdkMgr.getInst().getUdid();
    }

    public String sdkVersion() {
        return SdkMgr.getInst() == null ? "sdk not init" : SdkMgr.getInst().getSDKVersion(sdkChannel());
    }

    public boolean hasFeature(String str) {
        if (SdkMgr.getInst() == null) {
            UniSdkUtils.e(TAG, "sdk not init");
            return false;
        }
        String str2 = (String) new SdkFeature(str).wrapTo();
        return str2 != null && SdkMgr.getInst().hasFeature(str2);
    }

    public void setProp(String str, String str2) {
        if (SdkMgr.getInst() != null) {
            SdkMgr.getInst().setPropStr(str, str2);
        } else {
            this.tmpKeyValues.put(str, str2);
        }
    }

    public void setLoginCallback(LoginCallback loginCallback) {
        this.loginCallback = loginCallback;
        SdkMgr.getInst().setLoginListener(new OnLoginDoneListener() { // from class: com.netease.ntunisdk.base.SDKManager.2
            @Override // com.netease.ntunisdk.base.OnLoginDoneListener
            public void loginDone(int i) {
                if (SDKManager.this.loginCallback != null) {
                    SdkAccount sdkAccount = new SdkAccount();
                    sdkAccount.wrapFrom(Integer.valueOf(i));
                    SDKManager.this.loginCallback.onLoginDone(sdkAccount.code, sdkAccount.message, sdkAccount.toString());
                }
            }
        }, 1);
    }

    public void login() {
        if (SdkMgr.getInst() == null) {
            if (this.loginCallback != null) {
                SdkState sdkState = new SdkState();
                sdkState.code = a.Fail.ordinal();
                sdkState.message = "sdk not init";
                this.loginCallback.onLoginDone(sdkState.code, sdkState.message, sdkState.toString());
                return;
            }
            return;
        }
        SdkMgr.getInst().ntLogin();
    }

    public void setLogoutCallback(LogoutCallback logoutCallback) {
        this.logoutCallback = logoutCallback;
        SdkMgr.getInst().setLogoutListener(new OnLogoutDoneListener() { // from class: com.netease.ntunisdk.base.SDKManager.3
            @Override // com.netease.ntunisdk.base.OnLogoutDoneListener
            public void logoutDone(int i) {
                if (SDKManager.this.logoutCallback != null) {
                    SdkState sdkState = new SdkState();
                    if (i == 0) {
                        sdkState.code = a.Suc.ordinal();
                        sdkState.message = a.Suc.d;
                    } else if (3 == i) {
                        sdkState.code = a.NeedRelogin.ordinal();
                        sdkState.message = a.NeedRelogin.d;
                    } else {
                        sdkState.code = a.Fail.ordinal();
                        sdkState.message = a.Fail.d;
                    }
                    SDKManager.this.logoutCallback.onLogoutDone(sdkState.code, sdkState.message, sdkState.toString());
                }
            }
        }, 1);
    }

    public void logout() {
        if (SdkMgr.getInst() == null) {
            if (this.logoutCallback != null) {
                SdkState sdkState = new SdkState();
                sdkState.code = a.Fail.ordinal();
                sdkState.message = "sdk not init";
                this.logoutCallback.onLogoutDone(sdkState.code, sdkState.message, sdkState.toString());
                return;
            }
            return;
        }
        SdkMgr.getInst().ntLogout();
    }

    public void openManager() {
        if (SdkMgr.getInst() == null) {
            UniSdkUtils.e(TAG, "sdk not init");
        } else {
            SdkMgr.getInst().ntOpenManager();
        }
    }

    public void uploadUserInfoStage(String str, String str2) {
        uploadUserInfo(str, str2);
    }

    public void uploadUserInfo(String str, String str2) {
        if (SdkMgr.getInst() == null) {
            UniSdkUtils.e(TAG, "sdk not init");
            return;
        }
        new SdkRole(str2).wrapTo();
        new SdkStage(str).wrap();
        SdkMgr.getInst().ntUpLoadUserInfo();
    }

    public void setOrderCallback(OrderCallback orderCallback) {
        this.orderCallback = orderCallback;
        SdkMgr.getInst().setOrderListener(new OnOrderCheckListener() { // from class: com.netease.ntunisdk.base.SDKManager.4
            @Override // com.netease.ntunisdk.base.OnOrderCheckListener
            public void orderConsumeDone(OrderInfo orderInfo) {
            }

            @Override // com.netease.ntunisdk.base.OnOrderCheckListener
            public void orderCheckDone(OrderInfo orderInfo) {
                if (SDKManager.this.orderCallback != null) {
                    SdkOrder sdkOrder = new SdkOrder();
                    sdkOrder.wrapFrom(orderInfo);
                    SDKManager.this.orderCallback.onOrderPurchased(sdkOrder.code, sdkOrder.message, sdkOrder.toString());
                }
            }
        }, 1);
    }

    public void inAppPurchase(String str, String str2) {
        if (SdkMgr.getInst() == null) {
            callbackFailOrder("sdk not init");
            return;
        }
        if (!SdkMgr.getInst().hasLogin()) {
            callbackFailOrder("not login yet");
            return;
        }
        SdkOrder sdkOrder = new SdkOrder(str);
        SdkRole sdkRole = new SdkRole(str2);
        if (!sdkOrder.check()) {
            callbackFailOrder("invalid json of order info, please check your order json");
        } else {
            if (!sdkRole.check()) {
                callbackFailOrder("invalid json of role info, please check your game role json");
                return;
            }
            OrderInfo orderInfo = (OrderInfo) sdkOrder.wrapTo();
            sdkRole.wrapTo();
            SdkMgr.getInst().ntCheckOrder(orderInfo);
        }
    }

    private void callbackFailOrder(String str) {
        if (this.orderCallback != null) {
            SdkState sdkState = new SdkState();
            sdkState.code = a.Fail.ordinal();
            sdkState.message = str;
            this.orderCallback.onOrderPurchased(sdkState.code, sdkState.message, sdkState.toString());
        }
    }

    public void userProtocolWithUrl(String str, boolean z) {
        userProtocol(str, z);
    }

    public void userProtocol(String str, boolean z) {
        if (SdkMgr.getInst() != null) {
            if (!TextUtils.isEmpty(str)) {
                SdkMgr.getInst().setPropStr(ConstProp.NT_COMPACT_URL, str);
            }
            SdkMgr.getInst().setPropStr("NT_COMPACT_URL_TYPE", "1");
            SdkMgr.getInst().ntShowCompactView(z);
        }
    }

    public void setShareCallback(ShareCallback shareCallback) {
        this.shareCallback = shareCallback;
        SdkMgr.getInst().setShareListener(new OnShareListener() { // from class: com.netease.ntunisdk.base.SDKManager.5
            @Override // com.netease.ntunisdk.base.OnShareListener
            public void onShareFinished(boolean z) {
                SdkShare sdkShare = new SdkShare();
                sdkShare.wrapFrom(Boolean.valueOf(z));
                if (SDKManager.this.shareCallback != null) {
                    SDKManager.this.shareCallback.onShareDone(sdkShare.code, sdkShare.message, sdkShare.toString());
                }
            }
        }, 1);
    }

    public void share(String str) {
        if (SdkMgr.getInst() == null) {
            if (this.shareCallback != null) {
                SdkState sdkState = new SdkState();
                sdkState.code = a.Fail.ordinal();
                sdkState.message = "sdk not init";
                this.shareCallback.onShareDone(sdkState.code, sdkState.message, sdkState.toString());
                return;
            }
            return;
        }
        SdkMgr.getInst().ntShare((ShareInfo) new SdkShare(str).wrapTo());
    }

    public void setExtendFuncCallback(ExtendFuncCallback extendFuncCallback) {
        this.extendFuncCallback = extendFuncCallback;
        SdkMgr.getInst().setExtendFuncListener(new OnExtendFuncListener() { // from class: com.netease.ntunisdk.base.SDKManager.6
            @Override // com.netease.ntunisdk.base.OnExtendFuncListener
            public void onExtendFuncCall(String str) {
                if (SDKManager.this.extendFuncCallback != null) {
                    SDKManager.this.extendFuncCallback.onFuncCalled(str);
                }
            }
        }, 1);
    }

    public void extendFunc(String str) {
        if (SdkMgr.getInst() == null) {
            if (this.extendFuncCallback != null) {
                SdkState sdkState = new SdkState();
                sdkState.code = a.Fail.ordinal();
                sdkState.message = "sdk not init";
                this.extendFuncCallback.onFuncCalled(SdkModelMerger.merge(sdkState, str));
                return;
            }
            return;
        }
        SdkMgr.getInst().ntExtendFunc(str);
    }

    public void extendFunc(String str, Object... objArr) {
        if (SdkMgr.getInst() == null) {
            if (this.extendFuncCallback != null) {
                SdkState sdkState = new SdkState();
                sdkState.code = a.Fail.ordinal();
                sdkState.message = "sdk not init";
                this.extendFuncCallback.onFuncCalled(SdkModelMerger.merge(sdkState, str));
                return;
            }
            return;
        }
        SdkMgr.getInst().ntExtendFunc(str, objArr);
    }

    public void onCreate(Bundle bundle) {
        if (SdkMgr.getInst() == null) {
            UniSdkUtils.w(TAG, "sdk not init, sdk will do it when sdkInit finish");
            this.bundleRef = new WeakReference<>(bundle);
        } else {
            SdkMgr.getInst().handleOnCreate(bundle);
            this.bundleRef = null;
        }
    }

    public void onStart() {
        if (SdkMgr.getInst() == null) {
            UniSdkUtils.e(TAG, "sdk not init");
        } else {
            SdkMgr.getInst().handleOnStart();
        }
    }

    public void onRestart() {
        if (SdkMgr.getInst() == null) {
            UniSdkUtils.e(TAG, "sdk not init");
        } else {
            SdkMgr.getInst().handleOnRestart();
        }
    }

    public void onResume() {
        if (SdkMgr.getInst() == null) {
            UniSdkUtils.e(TAG, "sdk not init");
        } else {
            SdkMgr.getInst().handleOnResume();
        }
    }

    public void onPause() {
        if (SdkMgr.getInst() == null) {
            UniSdkUtils.e(TAG, "sdk not init");
        } else {
            SdkMgr.getInst().handleOnPause();
        }
    }

    public void onStop() {
        if (SdkMgr.getInst() == null) {
            UniSdkUtils.e(TAG, "sdk not init");
        } else {
            SdkMgr.getInst().handleOnStop();
        }
    }

    public void onDestroy() {
        if (SdkMgr.getInst() == null) {
            UniSdkUtils.e(TAG, "sdk not init");
            return;
        }
        WeakReference<Context> weakReference = this.contextRef;
        if (weakReference == null || weakReference.get() == null || !((Activity) this.contextRef.get()).isFinishing()) {
            return;
        }
        SdkMgr.destroyInst();
        System.exit(0);
    }

    public void onNewIntent(Intent intent) {
        if (SdkMgr.getInst() == null) {
            UniSdkUtils.e(TAG, "sdk not init");
        } else {
            SdkMgr.getInst().handleOnNewIntent(intent);
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (SdkMgr.getInst() == null) {
            UniSdkUtils.e(TAG, "sdk not init");
        } else {
            SdkMgr.getInst().handleOnActivityResult(i, i2, intent);
        }
    }

    public void onWindowFocusChanged(boolean z) {
        if (SdkMgr.getInst() == null) {
            UniSdkUtils.e(TAG, "sdk not init");
        } else {
            SdkMgr.getInst().handleOnWindowFocusChanged(z);
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (SdkMgr.getInst() == null) {
            UniSdkUtils.e(TAG, "sdk not init");
        } else {
            SdkMgr.getInst().handleOnRequestPermissionsResult(i, strArr, iArr);
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        if (SdkMgr.getInst() == null) {
            UniSdkUtils.e(TAG, "sdk not init");
        } else {
            SdkMgr.getInst().handleOnConfigurationChanged(configuration);
        }
    }

    public void exit() {
        if (SdkMgr.getInst() == null) {
            UniSdkUtils.e(TAG, "sdk not init");
            return;
        }
        if (hasFeature(b.Exit.e)) {
            SdkMgr.getInst().ntOpenExitView();
            return;
        }
        SdkMgr.getInst().exit();
        WeakReference<Context> weakReference = this.contextRef;
        if (weakReference == null || weakReference.get() == null) {
            return;
        }
        ((Activity) this.contextRef.get()).finish();
    }

    @Override // com.netease.ntunisdk.base.OnExitListener
    public void exitApp() {
        SdkMgr.getInst().exit();
        WeakReference<Context> weakReference = this.contextRef;
        if (weakReference == null || weakReference.get() == null) {
            return;
        }
        ((Activity) this.contextRef.get()).finish();
    }

    @Override // com.netease.ntunisdk.base.OnExitListener
    public void onOpenExitViewFailed() {
        UniSdkUtils.e(TAG, "onOpenExitViewFailed");
        WeakReference<Context> weakReference = this.contextRef;
        if (weakReference == null || weakReference.get() == null) {
            return;
        }
        new Alerter(this.contextRef.get()).showDialog(this.contextRef.get().getString(ResUtils.getResId(this.contextRef.get(), "export_ef_alert_title", ResIdReader.RES_TYPE_STRING)), this.contextRef.get().getString(ResUtils.getResId(this.contextRef.get(), "export_ef_alert_message", ResIdReader.RES_TYPE_STRING)), this.contextRef.get().getString(ResUtils.getResId(this.contextRef.get(), "export_ef_alert_confirm", ResIdReader.RES_TYPE_STRING)), new DialogInterface.OnClickListener() { // from class: com.netease.ntunisdk.base.SDKManager.7
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                SDKManager.this.exitApp();
            }
        }, this.contextRef.get().getString(ResUtils.getResId(this.contextRef.get(), "export_ef_alert_cancel", ResIdReader.RES_TYPE_STRING)), null, true, true, null);
    }
}