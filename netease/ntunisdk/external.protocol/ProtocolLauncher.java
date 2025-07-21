package com.netease.ntunisdk.external.protocol;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import com.alipay.sdk.m.s.a;
import com.facebook.hermes.intl.Constants;
import com.netease.mpay.httpdns.HttpDns;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.SdkBase;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.external.protocol.data.Config;
import com.netease.ntunisdk.external.protocol.data.ProtocolInfo;
import com.netease.ntunisdk.external.protocol.data.ProtocolProp;
import com.netease.ntunisdk.external.protocol.data.SDKRuntime;
import com.netease.ntunisdk.external.protocol.data.Store;
import com.netease.ntunisdk.external.protocol.data.User;
import com.netease.ntunisdk.external.protocol.plugins.PluginManager;
import com.netease.ntunisdk.external.protocol.plugins.Result;
import com.netease.ntunisdk.external.protocol.utils.AsyncTask;
import com.netease.ntunisdk.external.protocol.utils.CommonUtils;
import com.netease.ntunisdk.external.protocol.utils.FileUtil;
import com.netease.ntunisdk.external.protocol.utils.L;
import com.netease.ntunisdk.external.protocol.utils.ResUtils;
import com.netease.ntunisdk.external.protocol.utils.SysHelper;
import com.netease.ntunisdk.external.protocol.utils.TextCompat;
import com.netease.ntunisdk.external.protocol.view.AlerterEx;
import com.netease.ntunisdk.external.protocol.view.DefaultWebViewCallback;
import com.netease.ntunisdk.external.protocol.view.EventCallback;
import com.netease.ntunisdk.external.protocol.view.ProgressImpl;
import com.netease.ntunisdk.external.protocol.view.UniWebView;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import com.netease.ntunisdk.protocollib.R;
import com.netease.ntunisdk.unilogger.global.Const;
import com.netease.unisdk.ngvoice.log.NgLog;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class ProtocolLauncher extends Activity {
    private static final String TAG = "L";
    private String mBaseUrl;
    private FrameLayout mContentView;
    private Intent mIntent;
    private View mLoadingView;
    private ProtocolCallback mProtocolCallback;
    private ProtocolManager mProtocolManager;
    private FrameLayout mRootView;
    private UniWebView mWebView;
    private final ProtocolProp mProp = new ProtocolProp();
    private boolean hasStart = false;
    private int mPublishArea = -1;

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) throws JSONException, PackageManager.NameNotFoundException, Settings.SettingNotFoundException {
        super.onCreate(bundle);
        if (SDKRuntime.getInstance().isProtocolLauncherShowing()) {
            finish();
            return;
        }
        SDKRuntime.getInstance().setProtocolLauncher(true);
        SDKRuntime.getInstance().setProtocolLauncherShowing(true);
        Store.getInstance().init(getApplicationContext());
        boolean zIsDebug = isDebug();
        L.setDebug(true);
        readConfig();
        if (SysHelper.isSupportHttpDNS()) {
            HttpDns.getInstance().init(this, SDKRuntime.getInstance().getJFGameId(), this.mPublishArea, zIsDebug);
        }
        try {
            hidSysNavigation();
            SysHelper.hideSystemUI(getWindow());
        } catch (Throwable unused) {
        }
        this.mIntent = getIntent();
        Log.d("deepLink", "ProtocolLauncher#onCreate");
        dealDeepLink(this.mIntent);
        if (openGameByHot()) {
            return;
        }
        new PreLoadTask() { // from class: com.netease.ntunisdk.external.protocol.ProtocolLauncher.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.netease.ntunisdk.external.protocol.utils.AsyncTask
            public void onPostExecute(Boolean bool) {
                ProtocolLauncher.this.initView();
                if (ProtocolLauncher.this.mProtocolManager.hasAcceptLaunchProtocol()) {
                    ProtocolLauncher.this.mProtocolManager.notShowCallback(ProtocolLauncher.this, Situation.LAUNCHER, null);
                    if (ProtocolLauncher.this.mRootView != null) {
                        ProtocolLauncher.this.mRootView.postDelayed(new Runnable() { // from class: com.netease.ntunisdk.external.protocol.ProtocolLauncher.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                ProtocolLauncher.this.openGameByCold();
                            }
                        }, 200L);
                        return;
                    } else {
                        ProtocolLauncher.this.openGameByCold();
                        return;
                    }
                }
                ProtocolLauncher.this.showProtocol();
            }
        }.execute(new Void[0]);
    }

    @Override // android.app.Activity
    protected void onNewIntent(Intent intent) throws JSONException {
        super.onNewIntent(intent);
        Log.d("deepLink", "ProtocolLauncher#onNewIntent");
        dealDeepLink(intent);
    }

    public void showProtocol() {
        this.mProtocolCallback = new ProtocolCallback() { // from class: com.netease.ntunisdk.external.protocol.ProtocolLauncher.2
            @Override // com.netease.ntunisdk.external.protocol.ProtocolCallback
            public void onFinish(int i) {
                L.d(ProtocolLauncher.TAG, "onFinish, code = " + i);
                SDKRuntime.getInstance().setProtocolShowing(false);
                ProtocolManager.getInstance().removeCallback(this);
                if (i == 2) {
                    ProtocolLauncher.this.exitGame();
                    return;
                }
                ProtocolManager.getInstance().mHasAcceptProtocolByLauncher = true;
                if (ProtocolLauncher.this.hasAppRunning()) {
                    L.d(ProtocolLauncher.TAG, "null != SdkMgr.getInst()");
                    SDKRuntime.getInstance().setProtocolLauncherShowing(false);
                    ProtocolLauncher.this.finish();
                    return;
                }
                ProtocolLauncher.this.runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.external.protocol.ProtocolLauncher.2.1
                    @Override // java.lang.Runnable
                    public void run() {
                        ProtocolLauncher.this.openGameByCold();
                    }
                });
            }

            @Override // com.netease.ntunisdk.external.protocol.ProtocolCallback
            public void onOpen() {
                L.d(ProtocolLauncher.TAG, "onOpen");
            }
        };
        if (SDKRuntime.getInstance().isPublishMainLand()) {
            if (TextUtils.isEmpty(Const.PROTOCOL_DEFAULT)) {
                L.d(TAG, "compact url is null. no need show protocol");
                this.mProtocolCallback.onFinish(3);
                return;
            } else if (!SDKRuntime.getInstance().isShowContentByTextView() && Build.VERSION.SDK_INT > 22) {
                SDKRuntime.getInstance().setProtocolShowing(true);
                initWebView(Const.PROTOCOL_DEFAULT);
                return;
            } else {
                SDKRuntime.getInstance().setProtocolShowing(true);
                showProtocolByDialog(null, this.mProtocolCallback);
                return;
            }
        }
        L.d(TAG, "start load config");
        AsyncTask.concurrentExecute(new Runnable() { // from class: com.netease.ntunisdk.external.protocol.ProtocolLauncher.3
            @Override // java.lang.Runnable
            public void run() {
                final String ipCountry = CommonUtils.getIpCountry();
                ProtocolLauncher.this.runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.external.protocol.ProtocolLauncher.3.1
                    @Override // java.lang.Runnable
                    public void run() {
                        ProtocolLauncher.this.openProtocolByCountry(ipCountry, ProtocolLauncher.this.mProtocolCallback);
                    }
                });
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openProtocolByCountry(String str, ProtocolCallback protocolCallback) {
        Config config = SDKRuntime.getInstance().getConfig();
        if (config != null) {
            String language = Locale.getDefault().getLanguage();
            if (!TextUtils.isEmpty(language) && language.equalsIgnoreCase("zh")) {
                language = Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry();
            }
            if (!TextUtils.isEmpty(language)) {
                language = language.toUpperCase(Locale.ROOT);
            }
            Config.ProtocolConfig protocolConfig = config.getProtocolConfig(str, language, Const.PROTOCOL_DEFAULT);
            if (protocolConfig != null) {
                ProtocolProp prop = this.mProtocolManager.getProp();
                prop.setLanguage(protocolConfig.language);
                prop.setProtocolConfig(protocolConfig);
                this.mProtocolManager.setProp(prop);
            }
            if (protocolConfig != null && protocolConfig.isLauncherShow) {
                this.mProtocolManager.setCallback(protocolCallback);
                this.mProtocolManager.showProtocol(this, Situation.LAUNCHER, Const.CONFIG_KEY.ALL, User.USER_NAME_LAUNCHER);
                return;
            }
            this.mProtocolManager.notShowCallback(this, Situation.LAUNCHER, null);
            FrameLayout frameLayout = this.mRootView;
            if (frameLayout != null) {
                frameLayout.postDelayed(new Runnable() { // from class: com.netease.ntunisdk.external.protocol.ProtocolLauncher.4
                    @Override // java.lang.Runnable
                    public void run() {
                        ProtocolLauncher.this.openGameByCold();
                    }
                }, 200L);
                return;
            } else {
                openGameByCold();
                return;
            }
        }
        this.mProtocolManager.notShowCallback(this, Situation.LAUNCHER, null);
        FrameLayout frameLayout2 = this.mRootView;
        if (frameLayout2 != null) {
            frameLayout2.postDelayed(new Runnable() { // from class: com.netease.ntunisdk.external.protocol.ProtocolLauncher.5
                @Override // java.lang.Runnable
                public void run() {
                    ProtocolLauncher.this.openGameByCold();
                }
            }, 200L);
        } else {
            openGameByCold();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initView() {
        setContentView(R.layout.uni_p_webview);
        this.mRootView = (FrameLayout) findViewById(ResUtils.getResId(this, "uni_p_root", ResIdReader.RES_TYPE_ID));
        this.mContentView = (FrameLayout) findViewById(ResUtils.getResId(this, "uni_p_content", ResIdReader.RES_TYPE_ID));
        setBgImage();
    }

    private void setWindowBackground() {
        try {
            getWindow().setBackgroundDrawable(new ColorDrawable(getBgColor()));
        } catch (Throwable unused) {
        }
    }

    private int getBgColor() {
        int resId = ResUtils.getResId(this, "launcher_bg_color", ResIdReader.RES_TYPE_STRING);
        if (resId <= 0) {
            return -16777216;
        }
        String string = getString(resId);
        if (TextUtils.isEmpty(string)) {
            return -16777216;
        }
        if (!string.startsWith("#")) {
            string = "#" + string;
        }
        try {
            return Color.parseColor(string);
        } catch (Exception unused) {
            return -16777216;
        }
    }

    private boolean initWebView(String str) {
        this.mBaseUrl = getProtocolHtmlUrl(str);
        try {
            if (this.mWebView == null) {
                UniWebView uniWebView = new UniWebView(getApplicationContext());
                this.mWebView = uniWebView;
                uniWebView.initWebView();
                this.mWebView.getSettings().setUseWideViewPort(true);
                this.mWebView.setSupportClearFocus(false);
                this.mWebView.getSettings().setSupportZoom(false);
                this.mWebView.getSettings().setTextZoom(100);
            }
            this.mWebView.setOnShowListener(new UniWebView.OnShowListener() { // from class: com.netease.ntunisdk.external.protocol.ProtocolLauncher.6
                @Override // com.netease.ntunisdk.external.protocol.view.UniWebView.OnShowListener
                public synchronized void onShow() {
                    ProtocolLauncher.this.dismissProgress();
                }
            });
            this.mWebView.setWebBridgeCallback(new DefaultWebViewCallback(new EventCallback() { // from class: com.netease.ntunisdk.external.protocol.ProtocolLauncher.7
                @Override // com.netease.ntunisdk.external.protocol.view.EventCallback
                public void next(Situation situation, ProtocolInfo protocolInfo, int i) {
                }

                @Override // com.netease.ntunisdk.external.protocol.view.EventCallback
                public void openBrowser(String str2) {
                    if (TextUtils.isEmpty(str2)) {
                        return;
                    }
                    if (SDKRuntime.getInstance().isSupportOpenBrowser()) {
                        L.d(ProtocolLauncher.TAG, "openBrowser:" + str2);
                        SysHelper.openBrowser(ProtocolLauncher.this, str2);
                        return;
                    }
                    ProtocolInfo protocolInfo = new ProtocolInfo(Const.PROTOCOL_TYPE_HTML, TextCompat.wrapperUrl(str2));
                    if (ProtocolLauncher.this.mProtocolManager != null) {
                        protocolInfo.globalInfo = ProtocolLauncher.this.mProtocolManager.getCurrentBaseProtocol() != null ? ProtocolLauncher.this.mProtocolManager.getCurrentBaseProtocol().globalInfo : null;
                    }
                    ProtocolLauncher.this.showProtocolByDialog(protocolInfo, new ProtocolCallback() { // from class: com.netease.ntunisdk.external.protocol.ProtocolLauncher.7.1
                        @Override // com.netease.ntunisdk.external.protocol.ProtocolCallback
                        public void onFinish(int i) {
                            L.d("onFinish:");
                            ProtocolLauncher.this.mWebView.setVisibility(0);
                            ProtocolLauncher.this.mWebView.requestFocus();
                            ProtocolLauncher.this.mWebView.reload();
                            ProtocolLauncher.this.hasStart = false;
                            ProtocolManager.getInstance().removeCallback(this);
                        }

                        @Override // com.netease.ntunisdk.external.protocol.ProtocolCallback
                        public void onOpen() {
                            ProtocolLauncher.this.mWebView.setVisibility(4);
                        }
                    });
                }

                @Override // com.netease.ntunisdk.external.protocol.view.EventCallback
                public void back(String str2) {
                    ProtocolLauncher.this.runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.external.protocol.ProtocolLauncher.7.2
                        @Override // java.lang.Runnable
                        public void run() {
                            if (ProtocolLauncher.this.mWebView == null || !ProtocolLauncher.this.mWebView.canGoBack()) {
                                return;
                            }
                            ProtocolLauncher.this.mWebView.goBack();
                        }
                    });
                }

                @Override // com.netease.ntunisdk.external.protocol.view.EventCallback
                public void finish(final int i, final JSONObject jSONObject) {
                    if (i != 0) {
                        if (i == 1) {
                            ProtocolManager.getInstance().mHasAcceptProtocolByLauncher = true;
                        } else if (i == 2) {
                            ProtocolLauncher.this.runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.external.protocol.ProtocolLauncher.7.3
                                @Override // java.lang.Runnable
                                public void run() throws Resources.NotFoundException {
                                    ProtocolLauncher.this.rejectProtocolConfirm(ProtocolLauncher.this);
                                }
                            });
                            return;
                        } else if (i != 3) {
                            if (i != 4) {
                                return;
                            }
                            ProtocolManager.getInstance().mHasAcceptProtocolByLauncher = true;
                        }
                    }
                    if (ProtocolLauncher.this.mRootView != null) {
                        ProtocolLauncher.this.mRootView.postDelayed(new Runnable() { // from class: com.netease.ntunisdk.external.protocol.ProtocolLauncher.7.4
                            @Override // java.lang.Runnable
                            public void run() {
                                ProtocolLauncher.this.removeWebView();
                                if (ProtocolLauncher.this.mProtocolManager != null) {
                                    if (jSONObject != null) {
                                        ProtocolLauncher.this.mProtocolManager.setLaunchProtocolId(jSONObject.optInt("protocolId", 0));
                                        ProtocolLauncher.this.mProtocolManager.setLaunchProtocolVersion(jSONObject.optInt("protocolVersion", 0));
                                    }
                                    ProtocolLauncher.this.mProtocolManager.getCallback().onFinish(i, jSONObject);
                                }
                                ProtocolLauncher.this.openGameByCold();
                            }
                        }, 200L);
                    } else {
                        ProtocolLauncher.this.runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.external.protocol.ProtocolLauncher.7.5
                            @Override // java.lang.Runnable
                            public void run() {
                                ProtocolLauncher.this.removeWebView();
                                if (ProtocolLauncher.this.mProtocolManager != null) {
                                    if (jSONObject != null) {
                                        ProtocolLauncher.this.mProtocolManager.setLaunchProtocolId(jSONObject.optInt("protocolId", 0));
                                        ProtocolLauncher.this.mProtocolManager.setLaunchProtocolVersion(jSONObject.optInt("protocolVersion", 0));
                                    }
                                    ProtocolLauncher.this.mProtocolManager.getCallback().onFinish(i, jSONObject);
                                }
                                ProtocolLauncher.this.openGameByCold();
                            }
                        });
                    }
                }
            }) { // from class: com.netease.ntunisdk.external.protocol.ProtocolLauncher.8
                @Override // com.netease.ntunisdk.external.protocol.view.DefaultWebViewCallback
                public void onPageError(final String str2) {
                    L.d("onPageError:" + str2);
                    ProtocolLauncher.this.runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.external.protocol.ProtocolLauncher.8.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (TextUtils.isEmpty(str2) || !str2.startsWith(ProtocolLauncher.this.mBaseUrl)) {
                                if (ProtocolLauncher.this.mWebView == null || !ProtocolLauncher.this.mWebView.canGoBack()) {
                                    return;
                                }
                                ProtocolLauncher.this.mWebView.goBack();
                                return;
                            }
                            if (ProtocolLauncher.this.mWebView != null) {
                                ProtocolLauncher.this.mWebView.stopLoading();
                            }
                            ProtocolLauncher.this.removeWebView();
                            ProtocolLauncher.this.showProtocolByDialog(null, ProtocolLauncher.this.mProtocolCallback);
                        }
                    });
                }
            });
            this.mWebView.setFocusable(true);
            this.mWebView.requestFocus();
            this.mWebView.setBaseUrl(this.mBaseUrl);
            this.mWebView.loadUrl(this.mBaseUrl);
            this.mWebView.loadingAlarm(this.mBaseUrl);
            showWebView();
            return true;
        } catch (Throwable unused) {
            removeWebView();
            return false;
        }
    }

    private String getProtocolHtmlUrl(String str) {
        if (!str.endsWith(".json")) {
            return str;
        }
        int iLastIndexOf = str.lastIndexOf("json");
        StringBuilder sb = new StringBuilder();
        sb.append(str.substring(0, iLastIndexOf));
        sb.append("html");
        sb.append("?");
        sb.append(Const.BASE_PROTOCOL);
        sb.append("=");
        sb.append(1);
        if (this.mProtocolManager == null) {
            this.mProtocolManager = getProtocolManager();
        }
        if (SDKRuntime.getInstance().isHideLogo()) {
            sb.append(a.l);
            sb.append(Const.HIDE_LOGO);
            sb.append("=");
            sb.append(1);
        }
        return TextCompat.wrapperUrl(sb.toString());
    }

    private void showWebView() {
        showProgress();
        this.mContentView.addView(this.mWebView, new FrameLayout.LayoutParams(-1, -1));
        this.mContentView.setVisibility(0);
        this.mWebView.setVisibility(4);
        this.mWebView.requestFocus();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissProgress() {
        try {
            View view = this.mLoadingView;
            if (view != null) {
                view.setVisibility(8);
            }
        } catch (Throwable unused) {
        }
    }

    private void showProgress() {
        try {
            View view = this.mLoadingView;
            if (view == null) {
                View viewFindViewById = findViewById(ResUtils.getResId(this, "uni_p_loaging", ResIdReader.RES_TYPE_ID));
                this.mLoadingView = viewFindViewById;
                viewFindViewById.setVisibility(0);
            } else {
                view.setVisibility(0);
            }
        } catch (Throwable th) {
            th.printStackTrace();
            this.mLoadingView = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeWebView() {
        try {
            FrameLayout frameLayout = this.mContentView;
            if (frameLayout != null) {
                frameLayout.setVisibility(8);
            }
        } catch (Throwable unused) {
        }
        UniWebView uniWebView = this.mWebView;
        if (uniWebView != null) {
            try {
                uniWebView.stopLoading();
                this.mWebView.setVisibility(8);
                this.mWebView.destroy();
            } catch (Throwable unused2) {
            }
        }
    }

    private boolean isSupportShortcut() {
        if (this.mProtocolManager == null) {
            this.mProtocolManager = getProtocolManager();
        }
        return this.mProtocolManager.isSupportShortcut();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasAppRunning() {
        if (this.mProtocolManager == null) {
            this.mProtocolManager = getProtocolManager();
        }
        return this.mProtocolManager.hasAppRunning();
    }

    private boolean isIntentEmpty(Intent intent) {
        Bundle extras;
        if (intent == null || (extras = intent.getExtras()) == null || extras.isEmpty()) {
            return true;
        }
        Set<String> setKeySet = extras.keySet();
        if (setKeySet.size() == 1 && (setKeySet.contains("startFromLauncher") || setKeySet.contains("profile"))) {
            return true;
        }
        return setKeySet.isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized ProtocolManager getProtocolManager() {
        ProtocolManager protocolManager;
        L.d(TAG, " gameLauncherActivity : " + SDKRuntime.getInstance().getGameLauncherActivity());
        String string = ResUtils.getString(this, "protocol_issuer_name");
        if (!TextUtils.isEmpty(string)) {
            this.mProp.setIssuer(string);
        }
        if (TextUtils.isEmpty(this.mProp.getUrl())) {
            this.mProp.setUrl(Const.PROTOCOL_DEFAULT);
        }
        String string2 = ResUtils.getString(this, "protocol_hide_logo", "0");
        String string3 = ResUtils.getString(this, "protocol_hide_all_logo", "0");
        String string4 = ResUtils.getString(this, "protocol_disable_webview", "0");
        String string5 = ResUtils.getString(this, "protocol_disable_browser", "0");
        String string6 = ResUtils.getString(this, "protocol_hide_close", "0");
        String string7 = ResUtils.getString(this, "protocol_not_exit", "0");
        protocolManager = ProtocolManager.getInstance();
        protocolManager.init(this);
        protocolManager.setHideWebViewLogo("1".equals(string2));
        protocolManager.setProp(this.mProp);
        if (Build.VERSION.SDK_INT > 22) {
            SDKRuntime.getInstance().setShowContentByTextView("1".equals(string4));
        } else {
            SDKRuntime.getInstance().setShowContentByTextView(true);
        }
        SDKRuntime.getInstance().setHideLogo("1".equals(string3));
        SDKRuntime.getInstance().setSupportOpenBrowser("0".equals(string5));
        SDKRuntime.getInstance().setHiddenClose("1".equals(string6));
        SDKRuntime.getInstance().setNotExitProcess("1".equals(string7));
        return protocolManager;
    }

    @Deprecated
    private void loadLocalConfig() {
        try {
            String assetsFileAsString = FileUtil.readAssetsFileAsString(this, Const.LAUNCHER_LOCAL_DEFAULT_PROP);
            L.d(TAG, "config : " + assetsFileAsString);
            if (TextUtils.isEmpty(assetsFileAsString)) {
                return;
            }
            JSONObject jSONObject = new JSONObject(assetsFileAsString);
            String strOptString = jSONObject.optString("url");
            if (!TextUtils.isEmpty(strOptString)) {
                this.mProp.setUrl(strOptString);
            }
            String strOptString2 = jSONObject.optString("urlType");
            if (!TextUtils.isEmpty(strOptString2)) {
                this.mProp.setUrlType(strOptString2);
            }
            String strOptString3 = jSONObject.optString("offlineGameFlag");
            if (!TextUtils.isEmpty(strOptString3)) {
                this.mProp.setOfflineGameFlag(strOptString3);
            }
            String strOptString4 = jSONObject.optString("newOfflineFlag");
            if (!TextUtils.isEmpty(strOptString4)) {
                this.mProp.setNewOfflineFlag(strOptString4);
            }
            String strOptString5 = jSONObject.optString("showTitleFlag");
            if (!TextUtils.isEmpty(strOptString5)) {
                this.mProp.setShowTitleFlag(strOptString5);
            }
            String strOptString6 = jSONObject.optString("showAgreeLineFlag");
            if (!TextUtils.isEmpty(strOptString6)) {
                this.mProp.setShowAgreeLineFlag(strOptString6);
            }
            String strOptString7 = jSONObject.optString("agreeLineText");
            if (!TextUtils.isEmpty(strOptString7)) {
                this.mProp.setAgreeLineText(strOptString7);
            }
            String strOptString8 = jSONObject.optString(Constants.LOCALE);
            if (!TextUtils.isEmpty(strOptString8)) {
                this.mProp.setLocale(strOptString8);
            }
            String strOptString9 = jSONObject.optString("issuer");
            if (!TextUtils.isEmpty(strOptString9)) {
                this.mProp.setIssuer(strOptString9);
            }
            L.d(TAG, "Launch issuer : " + this.mProp.getIssuer());
        } catch (Throwable unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void showProtocolByDialog(ProtocolInfo protocolInfo, ProtocolCallback protocolCallback) {
        if (this.hasStart) {
            return;
        }
        this.hasStart = true;
        String gameLauncherActivity = SDKRuntime.getInstance().getGameLauncherActivity();
        if (TextUtils.isEmpty(gameLauncherActivity) && TextUtils.isEmpty(gameLauncherActivity)) {
            SDKRuntime.getInstance().setProtocolShowing(false);
            Toast.makeText(this, "\u53c2\u6570gameLauncherActivity\u9519\u8bef", 1).show();
            exitGame();
        } else {
            if (this.mProtocolManager == null) {
                this.mProtocolManager = getProtocolManager();
            }
            L.d(TAG, "Show Protocol WebView Failed\u3002 Start showing native dialog");
            this.mProtocolManager.setCallback(protocolCallback);
            this.mProtocolManager.showProtocolWhenLaunch(this, protocolInfo);
        }
    }

    private void readConfig() throws PackageManager.NameNotFoundException {
        if (this.mPublishArea == -1) {
            try {
                ActivityInfo activityInfo = getApplicationContext().getPackageManager().getActivityInfo(getComponentName(), 128);
                this.mPublishArea = activityInfo.metaData.getInt(Const.PROTOCOL_PUBLISH);
                SDKRuntime.getInstance().setPublishArea(this.mPublishArea);
                Tracker.getInstance().setPublishArea(this.mPublishArea);
                String string = activityInfo.metaData.getString(Const.PROTOCOL_APP_CHANNEL);
                if (!Const.EMPTY.equals(string)) {
                    SDKRuntime.getInstance().setAppChannel(string);
                }
                String string2 = activityInfo.metaData.getString(Const.PROTOCOL_JF_GAME_ID);
                if (Const.EMPTY.equals(string2)) {
                    return;
                }
                SDKRuntime.getInstance().setJFGameId(string2);
            } catch (Exception e) {
                e.printStackTrace();
                this.mPublishArea = -1;
            }
        }
    }

    @Override // android.app.Activity
    public void onBackPressed() throws Resources.NotFoundException {
        UniWebView uniWebView = this.mWebView;
        if (uniWebView != null && uniWebView.canGoBack()) {
            this.mWebView.goBack();
        } else {
            rejectProtocolConfirm(this);
        }
    }

    private void setBgImage() {
        int resId = ResUtils.getResId(this, "protocol_launcher_bg", ResIdReader.RES_TYPE_DRAWABLE);
        try {
            if (resId <= 0) {
                L.d(TAG, "no res/protocol_launcher_bg");
                this.mRootView.setBackgroundDrawable(getWindow().getDecorView().getBackground());
            } else {
                setWindowBackground();
                ImageView imageView = (ImageView) findViewById(ResUtils.getResId(this, "uni_p_background", ResIdReader.RES_TYPE_ID));
                imageView.setVisibility(0);
                imageView.setImageResource(resId);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                L.d(TAG, "set protocol_launcher_bg");
            }
        } catch (Throwable unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void exitGame() {
        PluginManager.getInstance().exit(this, new com.netease.ntunisdk.external.protocol.plugins.Callback() { // from class: com.netease.ntunisdk.external.protocol.ProtocolLauncher.9
            @Override // com.netease.ntunisdk.external.protocol.plugins.Callback
            public void onFinish(Result result) {
                ProgressImpl.getInstance().dismissProgress();
                SDKRuntime.getInstance().setProtocolLauncherShowing(false);
                ProtocolLauncher.this.finish();
                SysHelper.exitProcessInLaunch(ProtocolLauncher.this);
            }
        });
    }

    private boolean openGameByHot() {
        Intent intent;
        if (!hasAppRunning() && !ProtocolManager.getInstance().isUniSdkRunning()) {
            return false;
        }
        L.d(TAG, "null != SdkMgr.getInst()");
        String gameLauncherActivity = SDKRuntime.getInstance().getGameLauncherActivity();
        if (TextUtils.isEmpty(gameLauncherActivity)) {
            CommonUtils.readGameLauncherActivity(this);
            gameLauncherActivity = SDKRuntime.getInstance().getGameLauncherActivity();
        }
        if (this.mIntent != null) {
            intent = new Intent(this.mIntent);
            intent.setAction(null);
            intent.removeCategory("android.intent.category.LAUNCHER");
            intent.setFlags(0);
        } else {
            intent = new Intent();
        }
        if (((!isSupportShortcut() && (!isIntentEmpty(intent) || intent.getData() != null)) || (ProtocolManager.getInstance().hasSetTaskAffinity && getTaskId() != ProtocolManager.getInstance().activityTaskId)) && !TextUtils.isEmpty(gameLauncherActivity)) {
            L.d(TAG, "start Protocol Launcher Activity[\u70ed\u542f\u52a8]:" + gameLauncherActivity);
            intent.setComponent(new ComponentName(this, gameLauncherActivity));
            intent.addFlags(608174080);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
        SDKRuntime.getInstance().setProtocolLauncherShowing(false);
        finish();
        return true;
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        try {
            SysHelper.hideSystemUI(getWindow());
        } catch (Throwable unused) {
        }
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (z) {
            try {
                SysHelper.hideSystemUI(getWindow());
            } catch (Throwable unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean openGameByCold() {
        Intent intent;
        String gameLauncherActivity = SDKRuntime.getInstance().getGameLauncherActivity();
        if (TextUtils.isEmpty(gameLauncherActivity)) {
            Toast.makeText(this, "\u53c2\u6570gameLauncherActivity\u9519\u8bef", 1).show();
            exitGame();
            return true;
        }
        ProtocolManager protocolManager = this.mProtocolManager;
        if (protocolManager != null) {
            protocolManager.onDestroy(this);
        }
        if (this.mIntent != null) {
            intent = new Intent(this.mIntent);
            intent.setAction(null);
            intent.removeCategory("android.intent.category.LAUNCHER");
            intent.setFlags(0);
        } else {
            intent = new Intent();
        }
        intent.addFlags(603979776);
        L.d(TAG, "start Protocol Launcher Activity[\u51b7\u542f\u52a8]:" + gameLauncherActivity);
        intent.setComponent(new ComponentName(this, gameLauncherActivity));
        startActivity(intent);
        overridePendingTransition(0, 0);
        ProtocolManager.getInstance().mHasAcceptProtocolByLauncher = true;
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { // from class: com.netease.ntunisdk.external.protocol.ProtocolLauncher.10
            @Override // java.lang.Runnable
            public void run() {
                SDKRuntime.getInstance().setProtocolLauncherShowing(false);
                ProtocolLauncher.this.finish();
            }
        }, 4000L);
        return true;
    }

    private boolean isDebug() throws Settings.SettingNotFoundException {
        try {
            if (Class.forName(getPackageName() + ".BuildConfig").getDeclaredField("DEBUG").getBoolean(null)) {
                return true;
            }
        } catch (Exception unused) {
        }
        try {
            int i = Settings.System.getInt(getContentResolver(), NgLog.NT_UNISDK_DEBUG_KEY);
            return i == 1 || i == 1;
        } catch (Exception unused2) {
            return false;
        }
    }

    private void hidSysNavigation() {
        try {
            View.class.getMethod("setSystemUiVisibility", Integer.TYPE).invoke(getWindow().getDecorView(), 2);
        } catch (Throwable unused) {
        }
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        L.d(TAG, "onDestroy");
        ProtocolManager protocolManager = this.mProtocolManager;
        if (protocolManager != null) {
            protocolManager.onDestroy(this);
        }
        UniWebView uniWebView = this.mWebView;
        if (uniWebView != null) {
            uniWebView.destroy();
            this.mWebView = null;
        }
    }

    @Override // android.app.Activity
    public void finish() {
        super.finish();
        Log.d("deepLink", "ProtocolLauncher#finish");
        overridePendingTransition(0, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void rejectProtocolConfirm(Context context) throws Resources.NotFoundException {
        AlerterEx alerterEx = new AlerterEx(context);
        Resources resources = context.getResources();
        Locale protocolLocale = TextCompat.getProtocolLocale(this.mProp);
        if (protocolLocale != null) {
            Configuration configuration = resources.getConfiguration();
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            configuration.locale = protocolLocale;
            resources.updateConfiguration(configuration, displayMetrics);
        }
        alerterEx.alert("  ", resources.getString(ResUtils.getResId(context, Const.UNISDK_PROTOCOL_REJECT_CONFIRM_MSG, ResIdReader.RES_TYPE_STRING)), resources.getString(ResUtils.getResId(context, Const.UNISDK_PROTOCOL_REJECT_CONFIRM_OK, ResIdReader.RES_TYPE_STRING)), new DialogInterface.OnClickListener() { // from class: com.netease.ntunisdk.external.protocol.ProtocolLauncher.11
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                ProtocolLauncher.this.exitGame();
            }
        }, resources.getString(ResUtils.getResId(context, Const.UNISDK_PROTOCOL_REJECT_CONFIRM_BACK, ResIdReader.RES_TYPE_STRING)), new DialogInterface.OnClickListener() { // from class: com.netease.ntunisdk.external.protocol.ProtocolLauncher.12
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        System.out.println("onKeyDown:" + i);
        return super.onKeyDown(i, keyEvent);
    }

    @Override // android.app.Activity
    public boolean onTouchEvent(MotionEvent motionEvent) {
        System.out.println("onTouchEvent:" + motionEvent.getAction());
        return super.onTouchEvent(motionEvent);
    }

    public class PreLoadTask extends AsyncTask<Void, Void, Boolean> {
        public PreLoadTask() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.netease.ntunisdk.external.protocol.utils.AsyncTask
        public Boolean doInBackground(Void... voidArr) throws ClassNotFoundException {
            CommonUtils.readGameLauncherActivity(ProtocolLauncher.this);
            UniWebView.getConfig().setLoadLocalJS(true);
            UniWebView.preload(ProtocolLauncher.this);
            if (ProtocolLauncher.this.mProtocolManager == null) {
                ProtocolLauncher protocolLauncher = ProtocolLauncher.this;
                protocolLauncher.mProtocolManager = protocolLauncher.getProtocolManager();
            }
            try {
                SDKRuntime.getInstance().init(ProtocolLauncher.this);
                ProtocolLauncher.this.mProtocolManager.readLocalData(ProtocolLauncher.this);
                ProtocolLauncher.this.mProtocolManager.getProvider().loadLocalProtocolFromFiles();
            } catch (Exception unused) {
            }
            try {
                String gameLauncherActivity = SDKRuntime.getInstance().getGameLauncherActivity();
                if (!TextUtils.isEmpty(gameLauncherActivity)) {
                    Class.forName(gameLauncherActivity);
                }
            } catch (Exception unused2) {
            }
            return true;
        }
    }

    private void dealDeepLink(Intent intent) throws JSONException {
        Uri data = intent.getData();
        Log.d(TAG, "uri=" + data);
        boolean booleanExtra = intent.getBooleanExtra("shouldCallback", true);
        Log.d(TAG, "shouldCallback: " + booleanExtra);
        DeepLinkPref.clearKeyValues();
        if (data != null) {
            DeepLinkPref.appendKeyValue(ConstProp.DEEP_LINK_WHOLE_URI, data.toString());
        }
        if (data != null && !TextUtils.isEmpty(data.getQuery())) {
            for (String str : data.getQuery().split(a.l)) {
                if (!TextUtils.isEmpty(str) && !str.endsWith("=") && !str.startsWith("=")) {
                    String[] strArrSplit = str.split("=");
                    if (2 == strArrSplit.length && strArrSplit[0] != null && strArrSplit[1] != null) {
                        DeepLinkPref.appendKeyValue(strArrSplit[0], strArrSplit[1]);
                    }
                }
            }
        }
        if (DeepLinkPref.getAllKeyValues().isEmpty()) {
            String stringExtra = intent.getStringExtra("launchfrom");
            if (TextUtils.isEmpty(intent.getStringExtra("platform")) && TextUtils.isEmpty(stringExtra)) {
                return;
            }
            DeepLinkPref.appendKeyValue(ConstProp.DEEP_LINK_WHOLE_URI, stringExtra);
            Bundle extras = intent.getExtras();
            if (extras == null) {
                return;
            }
            for (String str2 : extras.keySet()) {
                Object obj = extras.get(str2);
                try {
                    DeepLinkPref.appendKeyValue(str2, String.valueOf(obj));
                    Log.d("deepLink", "Key: " + str2 + ", Value: " + obj);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        JSONObject jSONObject = new JSONObject();
        try {
            for (Map.Entry<String, String> entry : DeepLinkPref.getAllKeyValues().entrySet()) {
                jSONObject.put(entry.getKey(), entry.getValue());
            }
            jSONObject.put("methodId", "fromDeepLink");
        } catch (Exception unused) {
        }
        DeepLinkPref.setStartIntent(intent);
        if (SdkMgr.getInst() == null) {
            L.d(TAG, "null SdkMgr.getInst()");
            DeepLinkPref.appendKeyValue(ConstProp.DEEP_LINK_FROM_LAUNCH, jSONObject.toString());
            L.d(TAG, "deeplink[code]:" + jSONObject.toString());
            return;
        }
        if (DeepLinkPref.getAllKeyValues() == null || !booleanExtra) {
            return;
        }
        L.d(TAG, "valid SdkMgr.getInst()");
        for (Map.Entry<String, String> entry2 : DeepLinkPref.getAllKeyValues().entrySet()) {
            SdkMgr.getInst().setPropStr(entry2.getKey(), entry2.getValue());
        }
        L.d(TAG, "deeplink[hot]:" + jSONObject.toString());
        ((SdkBase) SdkMgr.getInst()).extendFuncCall(jSONObject.toString());
    }
}