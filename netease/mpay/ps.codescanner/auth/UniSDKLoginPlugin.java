package com.netease.mpay.ps.codescanner.auth;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import com.alipay.sdk.m.u.b;
import com.netease.mc.mi.R;
import com.netease.mpay.auth.SDKContext;
import com.netease.mpay.auth.plugins.LifeCyclePlugin;
import com.netease.mpay.auth.plugins.PluginResult;
import com.netease.mpay.ps.codescanner.auth.DefaultAuthRules;
import com.netease.mpay.ps.codescanner.widget.Alerters;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.OnFinishInitListener;
import com.netease.ntunisdk.base.OnLoginDoneListener;
import com.netease.ntunisdk.base.SdkBase;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.UniSdkUtils;
import java.lang.reflect.Field;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class UniSDKLoginPlugin extends LifeCyclePlugin {
    private static final String TAG = "UniSDK AuthLogin";
    private String lastUid;
    private boolean blockLogin = false;
    private boolean needLoginOnResume = true;

    public void onLoginDone(int i, SdkBase sdkBase, OnLoginDoneListener onLoginDoneListener) {
    }

    @Override // com.netease.mpay.auth.plugins.DefaultPlugin, com.netease.mpay.auth.plugins.Plugin
    public boolean isNeedSuccessBeforeExecute() {
        return true;
    }

    @Override // com.netease.mpay.auth.plugins.LifeCyclePlugin, com.netease.mpay.auth.plugins.Plugin
    public void execute() throws NoSuchFieldException {
        if (!SDKContext.getInstance().isColdLaunch()) {
            DefaultAuthRules.getInstance().setHasLogin(SdkMgr.getInst().hasLogin());
            this.lastUid = SdkMgr.getInst().getPropStr(ConstProp.WEB_UID);
            DefaultAuthRules.getInstance().setLastUid(this.lastUid);
            login();
            return;
        }
        if ("nearme_vivo".equals(SdkMgr.getInst().getChannel())) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { // from class: com.netease.mpay.ps.codescanner.auth.UniSDKLoginPlugin.1
                AnonymousClass1() {
                }

                @Override // java.lang.Runnable
                public void run() {
                    UniSDKLoginPlugin.this.initUniSDK();
                }
            }, b.f1465a);
            return;
        }
        if ("dangle".equals(SdkMgr.getInst().getChannel()) || "baidu".equals(SdkMgr.getInst().getChannel()) || "baidu_sdk".equals(SdkMgr.getInst().getChannel())) {
            new Alerters(this.mActivity).alert(this.mActivity.getString(R.string.netease_mpay_ps_codescanner__login_cannot_support_cold_start), this.mActivity.getString(R.string.netease_mpay_ps_codescanner__confirmed), new DialogInterface.OnClickListener() { // from class: com.netease.mpay.ps.codescanner.auth.UniSDKLoginPlugin.2
                AnonymousClass2() {
                }

                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    UniSdkUtils.d(UniSDKLoginPlugin.TAG, "this channel can not support cold start\uff01\uff01");
                    UniSDKLoginPlugin.this.getCallback().onFinish(PluginResult.newInstance(PluginResult.RESULT_FAILED, UniSDKLoginPlugin.this.mPreTaskResult.data, UniSDKLoginPlugin.this));
                }
            }, null, null, false);
        } else {
            initUniSDK();
        }
    }

    /* renamed from: com.netease.mpay.ps.codescanner.auth.UniSDKLoginPlugin$1 */
    class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public void run() {
            UniSDKLoginPlugin.this.initUniSDK();
        }
    }

    /* renamed from: com.netease.mpay.ps.codescanner.auth.UniSDKLoginPlugin$2 */
    class AnonymousClass2 implements DialogInterface.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            UniSdkUtils.d(UniSDKLoginPlugin.TAG, "this channel can not support cold start\uff01\uff01");
            UniSDKLoginPlugin.this.getCallback().onFinish(PluginResult.newInstance(PluginResult.RESULT_FAILED, UniSDKLoginPlugin.this.mPreTaskResult.data, UniSDKLoginPlugin.this));
        }
    }

    @Override // com.netease.mpay.auth.plugins.LifeCyclePlugin, com.netease.mpay.auth.plugins.Plugin
    public String getName() {
        return UniSDKLoginPlugin.class.getName();
    }

    @Override // com.netease.mpay.auth.plugins.LifeCyclePlugin, com.netease.mpay.auth.plugins.Lifecycle
    public synchronized void onResume() {
        Log.d(TAG, "onResume");
        if (DefaultAuthRules.getInstance().isGameActivityPaused()) {
            return;
        }
        if (this.blockLogin) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { // from class: com.netease.mpay.ps.codescanner.auth.UniSDKLoginPlugin.3
                AnonymousClass3() {
                }

                @Override // java.lang.Runnable
                public void run() {
                    if (UniSDKLoginPlugin.this.needLoginOnResume) {
                        DefaultAuthRules.getInstance().setStartLoginByScanner(true);
                        SdkMgr.getInst().ntLogin();
                        UniSDKLoginPlugin.this.needLoginOnResume = false;
                    }
                }
            }, 800L);
            this.blockLogin = false;
        }
    }

    /* renamed from: com.netease.mpay.ps.codescanner.auth.UniSDKLoginPlugin$3 */
    class AnonymousClass3 implements Runnable {
        AnonymousClass3() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (UniSDKLoginPlugin.this.needLoginOnResume) {
                DefaultAuthRules.getInstance().setStartLoginByScanner(true);
                SdkMgr.getInst().ntLogin();
                UniSDKLoginPlugin.this.needLoginOnResume = false;
            }
        }
    }

    @Override // com.netease.mpay.auth.plugins.LifeCyclePlugin, com.netease.mpay.auth.plugins.Lifecycle
    public void onPause() {
        super.onPause();
    }

    /* renamed from: com.netease.mpay.ps.codescanner.auth.UniSDKLoginPlugin$4 */
    class AnonymousClass4 implements OnFinishInitListener {
        AnonymousClass4() {
        }

        @Override // com.netease.ntunisdk.base.OnFinishInitListener
        public void finishInit(int i) {
            UniSdkUtils.d(UniSDKLoginPlugin.TAG, String.format("finishInit, code = %s", Integer.valueOf(i)));
            if (i == 0 || i == 2 || i == 3) {
                DefaultAuthRules.getInstance().setHasLogin(SdkMgr.getInst().hasLogin());
                UniSDKLoginPlugin.this.loginOnResume();
            } else {
                UniSdkUtils.d(UniSDKLoginPlugin.TAG, "UniSDK ntInit failed, scan qrcode ignore");
                UniSDKLoginPlugin.this.getCallback().onFinish(PluginResult.newInstance(PluginResult.RESULT_FAILED, UniSDKLoginPlugin.this.mPreTaskResult.data, UniSDKLoginPlugin.this));
            }
        }
    }

    public void initUniSDK() {
        Log.d(TAG, "Enter initUniSDK");
        SdkMgr.getInst().ntInit(new OnFinishInitListener() { // from class: com.netease.mpay.ps.codescanner.auth.UniSDKLoginPlugin.4
            AnonymousClass4() {
            }

            @Override // com.netease.ntunisdk.base.OnFinishInitListener
            public void finishInit(int i) {
                UniSdkUtils.d(UniSDKLoginPlugin.TAG, String.format("finishInit, code = %s", Integer.valueOf(i)));
                if (i == 0 || i == 2 || i == 3) {
                    DefaultAuthRules.getInstance().setHasLogin(SdkMgr.getInst().hasLogin());
                    UniSDKLoginPlugin.this.loginOnResume();
                } else {
                    UniSdkUtils.d(UniSDKLoginPlugin.TAG, "UniSDK ntInit failed, scan qrcode ignore");
                    UniSDKLoginPlugin.this.getCallback().onFinish(PluginResult.newInstance(PluginResult.RESULT_FAILED, UniSDKLoginPlugin.this.mPreTaskResult.data, UniSDKLoginPlugin.this));
                }
            }
        });
    }

    /* renamed from: com.netease.mpay.ps.codescanner.auth.UniSDKLoginPlugin$5 */
    class AnonymousClass5 implements Runnable {
        AnonymousClass5() {
        }

        @Override // java.lang.Runnable
        public void run() throws NoSuchFieldException {
            UniSDKLoginPlugin.this.login();
        }
    }

    public void loginOnResume() {
        postOnResume(new Runnable() { // from class: com.netease.mpay.ps.codescanner.auth.UniSDKLoginPlugin.5
            AnonymousClass5() {
            }

            @Override // java.lang.Runnable
            public void run() throws NoSuchFieldException {
                UniSDKLoginPlugin.this.login();
            }
        });
    }

    /* renamed from: com.netease.mpay.ps.codescanner.auth.UniSDKLoginPlugin$6 */
    class AnonymousClass6 implements DefaultAuthRules.Callback {
        AnonymousClass6() {
        }

        @Override // com.netease.mpay.ps.codescanner.auth.DefaultAuthRules.Callback
        public void onFinish(int i, JSONObject jSONObject) {
            if (i == 0) {
                UniSDKLoginPlugin.this.getCallback().onFinish(PluginResult.newInstance(PluginResult.RESULT_SUCCESS, UniSDKLoginPlugin.this.mPreTaskResult.data, UniSDKLoginPlugin.this));
            } else {
                UniSDKLoginPlugin.this.getCallback().onFinish(PluginResult.newInstance(PluginResult.RESULT_FAILED, UniSDKLoginPlugin.this.mPreTaskResult.data, UniSDKLoginPlugin.this));
            }
        }
    }

    public void login() throws NoSuchFieldException {
        DefaultAuthRules.getInstance().setLoginCallback(new DefaultAuthRules.Callback() { // from class: com.netease.mpay.ps.codescanner.auth.UniSDKLoginPlugin.6
            AnonymousClass6() {
            }

            @Override // com.netease.mpay.ps.codescanner.auth.DefaultAuthRules.Callback
            public void onFinish(int i, JSONObject jSONObject) {
                if (i == 0) {
                    UniSDKLoginPlugin.this.getCallback().onFinish(PluginResult.newInstance(PluginResult.RESULT_SUCCESS, UniSDKLoginPlugin.this.mPreTaskResult.data, UniSDKLoginPlugin.this));
                } else {
                    UniSDKLoginPlugin.this.getCallback().onFinish(PluginResult.newInstance(PluginResult.RESULT_FAILED, UniSDKLoginPlugin.this.mPreTaskResult.data, UniSDKLoginPlugin.this));
                }
            }
        });
        if (SdkMgr.getInst() instanceof SdkBase) {
            SdkBase sdkBase = (SdkBase) SdkMgr.getInst();
            try {
                Field declaredField = SdkBase.class.getDeclaredField("loginListener");
                declaredField.setAccessible(true);
                sdkBase.setLoginListener(new OnLoginDoneListener() { // from class: com.netease.mpay.ps.codescanner.auth.UniSDKLoginPlugin.7
                    final /* synthetic */ OnLoginDoneListener val$origin;
                    final /* synthetic */ SdkBase val$sdkBase;

                    AnonymousClass7(SdkBase sdkBase2, OnLoginDoneListener onLoginDoneListener) {
                        sdkBase = sdkBase2;
                        onLoginDoneListener = onLoginDoneListener;
                    }

                    @Override // com.netease.ntunisdk.base.OnLoginDoneListener
                    public synchronized void loginDone(int i) {
                        UniSdkUtils.d(UniSDKLoginPlugin.TAG, String.format("loginDone, code = %s", Integer.valueOf(i)));
                        UniSDKLoginPlugin.this.needLoginOnResume = false;
                        if (i != 0) {
                            if (!UniSDKLoginPlugin.this.blockLogin) {
                                UniSDKLoginPlugin.this.onLoginDoneFailed(sdkBase, onLoginDoneListener);
                            } else {
                                if (onLoginDoneListener != null) {
                                    onLoginDoneListener.loginDone(i);
                                }
                                new Alerters(UniSDKLoginPlugin.this.mActivity).alert(UniSDKLoginPlugin.this.mActivity.getString(R.string.netease_mpay_ps_codescanner__login_retry), UniSDKLoginPlugin.this.mActivity.getString(R.string.netease_mpay_ps_codescanner__retry), new DialogInterface.OnClickListener() { // from class: com.netease.mpay.ps.codescanner.auth.UniSDKLoginPlugin.7.2
                                    AnonymousClass2() {
                                    }

                                    @Override // android.content.DialogInterface.OnClickListener
                                    public void onClick(DialogInterface dialogInterface, int i2) {
                                        UniSDKLoginPlugin.this.blockLogin = false;
                                        SdkMgr.getInst().ntLogin();
                                    }
                                }, UniSDKLoginPlugin.this.mActivity.getString(R.string.netease_mpay_ps_codescanner__cancel), new DialogInterface.OnClickListener() { // from class: com.netease.mpay.ps.codescanner.auth.UniSDKLoginPlugin.7.3
                                    AnonymousClass3() {
                                    }

                                    @Override // android.content.DialogInterface.OnClickListener
                                    public void onClick(DialogInterface dialogInterface, int i2) throws JSONException {
                                        UniSDKLoginPlugin.this.onLoginDoneFailed(sdkBase, onLoginDoneListener);
                                    }
                                }, false);
                            }
                        } else {
                            UniSDKLoginPlugin.this.getCallback().onFinish(PluginResult.newInstance(PluginResult.RESULT_SUCCESS, UniSDKLoginPlugin.this.mPreTaskResult.data, UniSDKLoginPlugin.this));
                            sdkBase.setLoginListener(onLoginDoneListener, 1);
                            if (UniSDKLoginPlugin.this.blockLogin) {
                                sdkBase.loginDone(i);
                            } else {
                                String propStr = SdkMgr.getInst().getPropStr(ConstProp.WEB_UID);
                                UniSdkUtils.d(UniSDKLoginPlugin.TAG, String.format(Locale.ROOT, "loginDone: %d, lastUid:%s, currentUid:%s", Integer.valueOf(i), UniSDKLoginPlugin.this.lastUid, propStr));
                                if (!TextUtils.isEmpty(UniSDKLoginPlugin.this.lastUid) && !UniSDKLoginPlugin.this.lastUid.equalsIgnoreCase(propStr)) {
                                    UniSdkUtils.d(UniSDKLoginPlugin.TAG, String.format(Locale.ROOT, "loginDone: account changed:true, lastUid:%s, currentUid:%s", UniSDKLoginPlugin.this.lastUid, propStr));
                                    if (onLoginDoneListener != null) {
                                        new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.netease.mpay.ps.codescanner.auth.UniSDKLoginPlugin.7.1
                                            AnonymousClass1() {
                                            }

                                            @Override // java.lang.Runnable
                                            public void run() throws JSONException {
                                                sdkBase.loginDone(12);
                                            }
                                        });
                                    }
                                } else if (!DefaultAuthRules.getInstance().isHasLogin()) {
                                    SdkMgr.getInst().setPropStr("WEB_UNISDK_JF_ACCESS_TOKEN", SdkMgr.getInst().getPropStr(ConstProp.UNISDK_JF_ACCESS_TOKEN));
                                    ((SdkBase) SdkMgr.getInst()).logout();
                                    ((SdkBase) SdkMgr.getInst()).resetCommonProp();
                                }
                            }
                            UniSDKLoginPlugin.this.blockLogin = false;
                            UniSDKLoginPlugin.this.lastUid = null;
                            DefaultAuthRules.getInstance().setLastUid(null);
                        }
                    }

                    /* renamed from: com.netease.mpay.ps.codescanner.auth.UniSDKLoginPlugin$7$1 */
                    class AnonymousClass1 implements Runnable {
                        AnonymousClass1() {
                        }

                        @Override // java.lang.Runnable
                        public void run() throws JSONException {
                            sdkBase.loginDone(12);
                        }
                    }

                    /* renamed from: com.netease.mpay.ps.codescanner.auth.UniSDKLoginPlugin$7$2 */
                    class AnonymousClass2 implements DialogInterface.OnClickListener {
                        AnonymousClass2() {
                        }

                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i2) {
                            UniSDKLoginPlugin.this.blockLogin = false;
                            SdkMgr.getInst().ntLogin();
                        }
                    }

                    /* renamed from: com.netease.mpay.ps.codescanner.auth.UniSDKLoginPlugin$7$3 */
                    class AnonymousClass3 implements DialogInterface.OnClickListener {
                        AnonymousClass3() {
                        }

                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i2) throws JSONException {
                            UniSDKLoginPlugin.this.onLoginDoneFailed(sdkBase, onLoginDoneListener);
                        }
                    }
                }, 1);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (!SDKContext.getInstance().isColdLaunch() && DefaultAuthRules.getInstance().isGameActivityPaused()) {
            this.needLoginOnResume = true;
            this.blockLogin = true;
        } else {
            if (DefaultAuthRules.getInstance().isStartLoginByScanner()) {
                return;
            }
            DefaultAuthRules.getInstance().setStartLoginByScanner(true);
            SdkMgr.getInst().ntLogin();
            this.needLoginOnResume = false;
        }
    }

    /* renamed from: com.netease.mpay.ps.codescanner.auth.UniSDKLoginPlugin$7 */
    class AnonymousClass7 implements OnLoginDoneListener {
        final /* synthetic */ OnLoginDoneListener val$origin;
        final /* synthetic */ SdkBase val$sdkBase;

        AnonymousClass7(SdkBase sdkBase2, OnLoginDoneListener onLoginDoneListener) {
            sdkBase = sdkBase2;
            onLoginDoneListener = onLoginDoneListener;
        }

        @Override // com.netease.ntunisdk.base.OnLoginDoneListener
        public synchronized void loginDone(int i) {
            UniSdkUtils.d(UniSDKLoginPlugin.TAG, String.format("loginDone, code = %s", Integer.valueOf(i)));
            UniSDKLoginPlugin.this.needLoginOnResume = false;
            if (i != 0) {
                if (!UniSDKLoginPlugin.this.blockLogin) {
                    UniSDKLoginPlugin.this.onLoginDoneFailed(sdkBase, onLoginDoneListener);
                } else {
                    if (onLoginDoneListener != null) {
                        onLoginDoneListener.loginDone(i);
                    }
                    new Alerters(UniSDKLoginPlugin.this.mActivity).alert(UniSDKLoginPlugin.this.mActivity.getString(R.string.netease_mpay_ps_codescanner__login_retry), UniSDKLoginPlugin.this.mActivity.getString(R.string.netease_mpay_ps_codescanner__retry), new DialogInterface.OnClickListener() { // from class: com.netease.mpay.ps.codescanner.auth.UniSDKLoginPlugin.7.2
                        AnonymousClass2() {
                        }

                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i2) {
                            UniSDKLoginPlugin.this.blockLogin = false;
                            SdkMgr.getInst().ntLogin();
                        }
                    }, UniSDKLoginPlugin.this.mActivity.getString(R.string.netease_mpay_ps_codescanner__cancel), new DialogInterface.OnClickListener() { // from class: com.netease.mpay.ps.codescanner.auth.UniSDKLoginPlugin.7.3
                        AnonymousClass3() {
                        }

                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i2) throws JSONException {
                            UniSDKLoginPlugin.this.onLoginDoneFailed(sdkBase, onLoginDoneListener);
                        }
                    }, false);
                }
            } else {
                UniSDKLoginPlugin.this.getCallback().onFinish(PluginResult.newInstance(PluginResult.RESULT_SUCCESS, UniSDKLoginPlugin.this.mPreTaskResult.data, UniSDKLoginPlugin.this));
                sdkBase.setLoginListener(onLoginDoneListener, 1);
                if (UniSDKLoginPlugin.this.blockLogin) {
                    sdkBase.loginDone(i);
                } else {
                    String propStr = SdkMgr.getInst().getPropStr(ConstProp.WEB_UID);
                    UniSdkUtils.d(UniSDKLoginPlugin.TAG, String.format(Locale.ROOT, "loginDone: %d, lastUid:%s, currentUid:%s", Integer.valueOf(i), UniSDKLoginPlugin.this.lastUid, propStr));
                    if (!TextUtils.isEmpty(UniSDKLoginPlugin.this.lastUid) && !UniSDKLoginPlugin.this.lastUid.equalsIgnoreCase(propStr)) {
                        UniSdkUtils.d(UniSDKLoginPlugin.TAG, String.format(Locale.ROOT, "loginDone: account changed:true, lastUid:%s, currentUid:%s", UniSDKLoginPlugin.this.lastUid, propStr));
                        if (onLoginDoneListener != null) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.netease.mpay.ps.codescanner.auth.UniSDKLoginPlugin.7.1
                                AnonymousClass1() {
                                }

                                @Override // java.lang.Runnable
                                public void run() throws JSONException {
                                    sdkBase.loginDone(12);
                                }
                            });
                        }
                    } else if (!DefaultAuthRules.getInstance().isHasLogin()) {
                        SdkMgr.getInst().setPropStr("WEB_UNISDK_JF_ACCESS_TOKEN", SdkMgr.getInst().getPropStr(ConstProp.UNISDK_JF_ACCESS_TOKEN));
                        ((SdkBase) SdkMgr.getInst()).logout();
                        ((SdkBase) SdkMgr.getInst()).resetCommonProp();
                    }
                }
                UniSDKLoginPlugin.this.blockLogin = false;
                UniSDKLoginPlugin.this.lastUid = null;
                DefaultAuthRules.getInstance().setLastUid(null);
            }
        }

        /* renamed from: com.netease.mpay.ps.codescanner.auth.UniSDKLoginPlugin$7$1 */
        class AnonymousClass1 implements Runnable {
            AnonymousClass1() {
            }

            @Override // java.lang.Runnable
            public void run() throws JSONException {
                sdkBase.loginDone(12);
            }
        }

        /* renamed from: com.netease.mpay.ps.codescanner.auth.UniSDKLoginPlugin$7$2 */
        class AnonymousClass2 implements DialogInterface.OnClickListener {
            AnonymousClass2() {
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                UniSDKLoginPlugin.this.blockLogin = false;
                SdkMgr.getInst().ntLogin();
            }
        }

        /* renamed from: com.netease.mpay.ps.codescanner.auth.UniSDKLoginPlugin$7$3 */
        class AnonymousClass3 implements DialogInterface.OnClickListener {
            AnonymousClass3() {
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) throws JSONException {
                UniSDKLoginPlugin.this.onLoginDoneFailed(sdkBase, onLoginDoneListener);
            }
        }
    }

    public void onLoginDoneFailed(SdkBase sdkBase, OnLoginDoneListener onLoginDoneListener) throws JSONException {
        getCallback().onFinish(PluginResult.newInstance(PluginResult.RESULT_FAILED, this.mPreTaskResult.data, this));
        sdkBase.setLoginListener(onLoginDoneListener, 1);
        this.blockLogin = false;
        this.lastUid = null;
        DefaultAuthRules.getInstance().setLastUid(null);
    }

    private void onLoginRetry(int i, SdkBase sdkBase, OnLoginDoneListener onLoginDoneListener) {
        new Alerters(this.mActivity).alert(this.mActivity.getString(R.string.netease_mpay_ps_codescanner__login_retry), this.mActivity.getString(R.string.netease_mpay_ps_codescanner__retry), new DialogInterface.OnClickListener() { // from class: com.netease.mpay.ps.codescanner.auth.UniSDKLoginPlugin.8
            AnonymousClass8() {
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                SdkMgr.getInst().ntLogin();
            }
        }, this.mActivity.getString(R.string.netease_mpay_ps_codescanner__cancel), new DialogInterface.OnClickListener() { // from class: com.netease.mpay.ps.codescanner.auth.UniSDKLoginPlugin.9
            final /* synthetic */ int val$code;
            final /* synthetic */ OnLoginDoneListener val$origin;
            final /* synthetic */ SdkBase val$sdkBase;

            AnonymousClass9(int i2, SdkBase sdkBase2, OnLoginDoneListener onLoginDoneListener2) {
                i = i2;
                sdkBase = sdkBase2;
                onLoginDoneListener = onLoginDoneListener2;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                UniSDKLoginPlugin.this.onLoginDone(i, sdkBase, onLoginDoneListener);
            }
        }, false);
    }

    /* renamed from: com.netease.mpay.ps.codescanner.auth.UniSDKLoginPlugin$8 */
    class AnonymousClass8 implements DialogInterface.OnClickListener {
        AnonymousClass8() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i2) {
            SdkMgr.getInst().ntLogin();
        }
    }

    /* renamed from: com.netease.mpay.ps.codescanner.auth.UniSDKLoginPlugin$9 */
    class AnonymousClass9 implements DialogInterface.OnClickListener {
        final /* synthetic */ int val$code;
        final /* synthetic */ OnLoginDoneListener val$origin;
        final /* synthetic */ SdkBase val$sdkBase;

        AnonymousClass9(int i2, SdkBase sdkBase2, OnLoginDoneListener onLoginDoneListener2) {
            i = i2;
            sdkBase = sdkBase2;
            onLoginDoneListener = onLoginDoneListener2;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i2) {
            UniSDKLoginPlugin.this.onLoginDone(i, sdkBase, onLoginDoneListener);
        }
    }
}