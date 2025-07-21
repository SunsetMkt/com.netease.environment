package com.netease.ntunisdk.modules.ngwebviewgeneral.ui;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.CCMsgSdk.ControlCmdType;
import com.alipay.sdk.m.u.h;
import com.facebook.hermes.intl.Constants;
import com.google.android.material.badge.BadgeDrawable;
import com.netease.ntunisdk.base.SdkBase;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.SdkU3d;
import com.netease.ntunisdk.base.WebViewProxy;
import com.netease.ntunisdk.external.protocol.Const;
import com.netease.ntunisdk.modules.api.ModulesCallback;
import com.netease.ntunisdk.modules.api.ModulesManager;
import com.netease.ntunisdk.modules.ngwebviewgeneral.R;
import com.netease.ntunisdk.modules.ngwebviewgeneral.WebviewParams;
import com.netease.ntunisdk.modules.ngwebviewgeneral.aidl.NGRemoteService;
import com.netease.ntunisdk.modules.ngwebviewgeneral.callback.RequestPermissionCallBack;
import com.netease.ntunisdk.modules.ngwebviewgeneral.log.NgWebviewLog;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView;
import com.netease.ntunisdk.modules.permission.common.PermissionConstant;
import com.netease.ntunisdk.modules.personalinfolist.HookManager;
import com.tencent.open.SocialConstants;
import com.xiaomi.gamecenter.sdk.report.SDefine;
import com.xiaomi.gamecenter.sdk.utils.RSASignature;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class NgWebviewActivity extends Activity {
    public static final String ACTION_CLOSEWEBVIEW = "closeWebView";
    public static final String ACTION_COPE2PASTEBOARD = "copyToPasteboard";
    public static final String ACTION_DEBUG_JS_LOG = "debug_js_log";
    public static final String ACTION_EXECUTE_EXTEND_FUNC = "execute_extend_func";
    public static final String ACTION_NOTIFY_NATIVE = "ngwebview_notify_native";
    public static final String ACTION_OPENBROWSER = "openBrowser";
    public static final String ACTION_SAVEWEBIMAGE = "saveWebImage";
    public static final String ACTION_SHAREMODULE = "shareModule";
    public static final String ACTION_SURVEYSTATE = "surveyState";
    public static final String ACTION_SURVEYUSERACTION = "surveyUserAction";
    public static int ORIENTATION = 0;
    public static NgWebviewActivity mInstance;
    public RequestPermissionCallBack RPCallBack;
    private boolean isSensorChanged;
    private LinearLayout mBlackBorderRight;
    private LinearLayout mBlackBorderTop;
    private RelativeLayout mContentView;
    protected boolean mFullscreenVideoViewShowing;
    private String mIdentifier;
    private String mIimageURL;
    private ImageView mNgwebviewClose;
    private RelativeLayout mRightNavigationBar;
    private int mScreenHeight;
    private int mScreenWidth;
    private SensorManager mSensorManager;
    private boolean mSetContentMarginFlag;
    private RelativeLayout mTopNavigationBar;
    private FrameLayout mUniWvContainer;
    private FrameLayout mUniWvRootView;
    private ModulesManager modulesManager;
    private Intent onActivityResultIntent;
    private boolean orientationVar;
    private RelativeLayout qstLoadingView;
    private int rotation;
    private SdkBase sdkBase;
    private UniWebView uniWv;
    private WebviewParams wvParams;
    private static final String TAG = "NgWebviewActivity";
    private static final int REQUEST_CODE_ASK_PERMISSIONS = Math.abs(TAG.hashCode()) & 65535;
    private String brand = Build.BRAND;
    private boolean isHasUnisdk = true;
    private int originalLayoutInDisplayCutoutMode = -1;
    private int mTargetSdkVersion = 0;
    SensorEventListener mSensorEventListener = new SensorEventListener() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivity.15
        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        @Override // android.hardware.SensorEventListener
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (NgWebviewActivity.this.rotation == ((WindowManager) NgWebviewActivity.this.getApplicationContext().getSystemService("window")).getDefaultDisplay().getRotation() || NgWebviewActivity.this.mFullscreenVideoViewShowing) {
                return;
            }
            NgWebviewLog.d(NgWebviewActivity.TAG, "onSensorChanged success ", new Object[0]);
            NgWebviewActivity.this.isSensorChanged = true;
            NgWebviewActivity ngWebviewActivity = NgWebviewActivity.this;
            ngWebviewActivity.rotation = ((WindowManager) ngWebviewActivity.getApplicationContext().getSystemService("window")).getDefaultDisplay().getRotation();
            if (NgWebviewActivity.this.rotation == 3) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) NgWebviewActivity.this.mBlackBorderRight.getLayoutParams();
                layoutParams.addRule(9, 0);
                layoutParams.addRule(11, R.id.black_border_right);
                NgWebviewActivity.this.mBlackBorderRight.setLayoutParams(layoutParams);
                RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) NgWebviewActivity.this.mRightNavigationBar.getLayoutParams();
                layoutParams2.addRule(11, 0);
                layoutParams2.addRule(9, R.id.right_navigation_bar);
                NgWebviewActivity.this.mRightNavigationBar.setLayoutParams(layoutParams2);
                RelativeLayout.LayoutParams layoutParams3 = (RelativeLayout.LayoutParams) NgWebviewActivity.this.mUniWvContainer.getLayoutParams();
                layoutParams3.addRule(0, R.id.black_border_right);
                layoutParams3.addRule(1, R.id.right_navigation_bar);
                NgWebviewActivity.this.mUniWvContainer.setLayoutParams(layoutParams3);
                return;
            }
            if (NgWebviewActivity.this.rotation == 1) {
                RelativeLayout.LayoutParams layoutParams4 = (RelativeLayout.LayoutParams) NgWebviewActivity.this.mBlackBorderRight.getLayoutParams();
                layoutParams4.addRule(11, 0);
                layoutParams4.addRule(9, R.id.black_border_right);
                NgWebviewActivity.this.mBlackBorderRight.setLayoutParams(layoutParams4);
                RelativeLayout.LayoutParams layoutParams5 = (RelativeLayout.LayoutParams) NgWebviewActivity.this.mRightNavigationBar.getLayoutParams();
                layoutParams5.addRule(9, 0);
                layoutParams5.addRule(11, R.id.right_navigation_bar);
                NgWebviewActivity.this.mRightNavigationBar.setLayoutParams(layoutParams5);
                RelativeLayout.LayoutParams layoutParams6 = (RelativeLayout.LayoutParams) NgWebviewActivity.this.mUniWvContainer.getLayoutParams();
                layoutParams6.addRule(0, R.id.right_navigation_bar);
                layoutParams6.addRule(1, R.id.black_border_right);
                NgWebviewActivity.this.mUniWvContainer.setLayoutParams(layoutParams6);
            }
        }
    };

    public interface KeyboardListener {
        void down();

        void up();
    }

    public static NgWebviewActivity getInstance() {
        return mInstance;
    }

    public void setRPCallBack(RequestPermissionCallBack requestPermissionCallBack) {
        this.RPCallBack = requestPermissionCallBack;
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) throws JSONException, IllegalAccessException, IllegalArgumentException {
        super.onCreate(bundle);
        mInstance = this;
        this.rotation = 999;
        Intent intent = getIntent();
        if (intent != null) {
            this.wvParams = (WebviewParams) intent.getSerializableExtra("WebviewParams");
            ORIENTATION = this.wvParams.getOrientation();
        }
        if (this.wvParams.isSingleProcess() || this.wvParams.isSingleInstance()) {
            ModulesManager.getInst().reInit(this);
        } else {
            ModulesManager.getInst().init(this);
        }
        ModulesManager.getInst().onCreate(bundle);
        ModulesManager.getInst().addModuleCallback("ngWebViewGeneral", PermissionConstant.PERMISSION_KEY, new ModulesCallback() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivity.1
            @Override // com.netease.ntunisdk.modules.api.ModulesCallback
            public void extendFuncCallback(String str, String str2, String str3) {
                NgWebviewLog.d(NgWebviewActivity.TAG, "Source: " + str, new Object[0]);
                NgWebviewLog.d(NgWebviewActivity.TAG, "Target: " + str2, new Object[0]);
                NgWebviewLog.d(NgWebviewActivity.TAG, "permission extendFuncCallback: " + str3, new Object[0]);
                try {
                    JSONObject jSONObject = new JSONObject(str3);
                    String strOptString = jSONObject.optString("result");
                    String strOptString2 = jSONObject.optString("permissionName");
                    String[] strArrSplit = strOptString.split(",");
                    if (strOptString2.contains("android.permission.ACCESS_COARSE_LOCATION") && strOptString2.contains("android.permission.ACCESS_FINE_LOCATION")) {
                        if (strArrSplit.length == 2 && "0".equals(strArrSplit[0]) && "0".equals(strArrSplit[1])) {
                            NgWebviewLog.d(NgWebviewActivity.TAG, "request location Permission success...", new Object[0]);
                            NgWebviewActivity.this.RPCallBack.result(true);
                        } else {
                            NgWebviewLog.d(NgWebviewActivity.TAG, "request location Permission fail...", new Object[0]);
                            NgWebviewActivity.this.RPCallBack.result(false);
                        }
                    } else if (strOptString2.contains("android.permission.WRITE_EXTERNAL_STORAGE")) {
                        if (strArrSplit.length == 1 && "0".equals(strArrSplit[0])) {
                            NgWebviewLog.d(NgWebviewActivity.TAG, "request storage Permission success...", new Object[0]);
                            NgWebviewActivity.this.saveWebImg(NgWebviewActivity.this.mIdentifier, NgWebviewActivity.this.mIimageURL, true);
                        } else {
                            NgWebviewLog.d(NgWebviewActivity.TAG, "request storage Permission fail...", new Object[0]);
                            NgWebviewActivity.this.saveWebImg(NgWebviewActivity.this.mIdentifier, NgWebviewActivity.this.mIimageURL, false);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        if (Build.VERSION.SDK_INT >= 14) {
            this.mSensorManager = (SensorManager) getSystemService("sensor");
            SensorManager sensorManager = this.mSensorManager;
            HookManager.registerSensorListener(sensorManager, this.mSensorEventListener, sensorManager.getDefaultSensor(1), 1);
        }
        NgWebviewLog.d(TAG, "ORIENTATION = " + ORIENTATION, new Object[0]);
        int i = getApplicationInfo().targetSdkVersion;
        NgWebviewLog.d(TAG, "sdk_init=" + Build.VERSION.SDK_INT + ", targetSdkVersion=" + i, new Object[0]);
        if (Build.VERSION.SDK_INT == 26 && i >= 27) {
            NgWebviewLog.d(TAG, "never call setScreenOrientation", new Object[0]);
        } else {
            setScreenOrientation();
        }
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        this.mScreenWidth = displayMetrics.widthPixels;
        this.mScreenHeight = displayMetrics.heightPixels;
        NgWebviewLog.d(TAG, "mScreenWidth=" + this.mScreenWidth + ", mScreenHeight=" + this.mScreenHeight, new Object[0]);
        setContentView(ResIdReader.getLayoutId(this, "ng_wv_main_act"));
        initLoadingView();
        this.mContentView = (RelativeLayout) findViewById(ResIdReader.getId(this, "content_view"));
        this.mUniWvRootView = (FrameLayout) findViewById(ResIdReader.getId(this, "wv_root_view"));
        this.mUniWvContainer = (FrameLayout) findViewById(ResIdReader.getId(this, "webview_Container"));
        initBlackBorder();
        if (this.wvParams.getWidth() != 0 || this.wvParams.getHeight() != 0) {
            if (this.wvParams.isFullScreenNoAdaptive()) {
                NgWebviewLog.e(TAG, "isFullScreen is 2 and width and height cannot be set at the same time");
            }
            Window window = getWindow();
            window.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams attributes = window.getAttributes();
            if (Build.VERSION.SDK_INT >= 28 && this.wvParams.isHasCutout()) {
                if (Build.VERSION.SDK_INT >= 16) {
                    this.mContentView.setSystemUiVisibility(1536);
                }
                if (Build.VERSION.SDK_INT >= 30) {
                    attributes.layoutInDisplayCutoutMode = 3;
                } else {
                    attributes.layoutInDisplayCutoutMode = 1;
                }
            }
            if ((this.wvParams.isSurvey() && !this.wvParams.isSetSurveyXY()) || (this.wvParams.getOriginY() == this.mScreenHeight / 10 && this.wvParams.getOriginX() == this.mScreenWidth / 10)) {
                NgWebviewLog.d(TAG, "Centered by default", new Object[0]);
                attributes.gravity = 17;
            } else {
                NgWebviewLog.d(TAG, "Centered by game", new Object[0]);
                attributes.x = this.wvParams.getOriginX();
                attributes.y = this.wvParams.getOriginY();
                attributes.gravity = BadgeDrawable.TOP_START;
            }
            attributes.width = this.wvParams.getWidth();
            attributes.height = this.wvParams.getHeight();
            window.setAttributes(attributes);
        } else if (this.wvParams.isFullScreenNoAdaptive()) {
            if (Build.VERSION.SDK_INT >= 16) {
                this.mContentView.setSystemUiVisibility(1536);
            }
            WindowManager.LayoutParams attributes2 = getWindow().getAttributes();
            try {
                attributes2.getClass().getField("layoutInDisplayCutoutMode").setInt(attributes2, 1);
            } catch (Exception unused) {
            }
            getWindow().setAttributes(attributes2);
        } else if (this.wvParams.isFullScreen()) {
            NgWebviewLog.d(TAG, "isHasCutout=" + this.wvParams.isHasCutout(), new Object[0]);
            if (!this.wvParams.isHasCutout() || Build.VERSION.SDK_INT < 28) {
                this.mSetContentMarginFlag = false;
            } else {
                this.mSetContentMarginFlag = false;
                WindowManager.LayoutParams attributes3 = getWindow().getAttributes();
                this.originalLayoutInDisplayCutoutMode = attributes3.layoutInDisplayCutoutMode;
                NgWebviewLog.d(TAG, "brand: " + this.brand, new Object[0]);
                if (Build.VERSION.SDK_INT >= 16) {
                    this.mContentView.setSystemUiVisibility(1536);
                }
                if (Build.VERSION.SDK_INT >= 30) {
                    attributes3.layoutInDisplayCutoutMode = 3;
                } else {
                    attributes3.layoutInDisplayCutoutMode = 1;
                }
                getWindow().setAttributes(attributes3);
            }
            NgWebviewLog.d(TAG, "SetContentMarginFlag = " + this.mSetContentMarginFlag, new Object[0]);
        }
        initWebview();
        initNgwebviewCloseBtn();
        this.mTopNavigationBar = (RelativeLayout) findViewById(ResIdReader.getId(this, "top_navigation_bar"));
        this.mRightNavigationBar = (RelativeLayout) findViewById(ResIdReader.getId(this, "right_navigation_bar"));
        initNavigationView();
        if (this.wvParams.getOrientation() != 0 && this.wvParams.getOrientation() != 4) {
            this.orientationVar = isLandscape();
        } else {
            this.orientationVar = this.mScreenWidth > this.mScreenHeight;
        }
        if (this.orientationVar) {
            NgWebviewLog.d(TAG, "LANDSCAPE", new Object[0]);
            if (this.wvParams.isFullScreen() && !this.wvParams.isSurvey()) {
                showRightBlackBorder();
            }
            showRightNavigationBar();
        } else {
            NgWebviewLog.d(TAG, "PORTRAIT", new Object[0]);
            if (this.wvParams.isFullScreen() && !this.wvParams.isSurvey()) {
                showTopBlackBorder();
            }
            showTopNavigationBar();
        }
        AndroidBug5497Workaround.assistActivity(this, new KeyboardListener() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivity.2
            @Override // com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivity.KeyboardListener
            public void up() {
                if (NgWebviewActivity.this.isSensorChanged) {
                    return;
                }
                NgWebviewActivity.this.isSensorChanged = false;
                NgWebviewLog.d(NgWebviewActivity.TAG, "up()...", new Object[0]);
                NgWebviewActivity ngWebviewActivity = NgWebviewActivity.this;
                ((ImageView) ngWebviewActivity.findViewById(ResIdReader.getId(ngWebviewActivity, "r_close"))).setVisibility(8);
            }

            @Override // com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivity.KeyboardListener
            public void down() {
                if (NgWebviewActivity.this.isSensorChanged) {
                    return;
                }
                NgWebviewActivity.this.isSensorChanged = false;
                NgWebviewLog.d(NgWebviewActivity.TAG, "down()...", new Object[0]);
                NgWebviewActivity ngWebviewActivity = NgWebviewActivity.this;
                ((ImageView) ngWebviewActivity.findViewById(ResIdReader.getId(ngWebviewActivity, "r_close"))).setVisibility(0);
                NgWebviewActivity.this.hideVirtualNavigationBar();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideVirtualNavigationBar() {
        NgWebviewLog.d(TAG, "hideVirtualNavigationBar...", new Object[0]);
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            getWindow().getDecorView().setSystemUiVisibility(8);
        } else if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(4102);
        }
    }

    private void initLoadingView() {
        NgWebviewLog.d(TAG, "initLoadingView...", new Object[0]);
        this.wvParams.getUrl();
        if (this.wvParams.isSurvey()) {
            this.qstLoadingView = (RelativeLayout) findViewById(ResIdReader.getId(this, "qst_loading_view"));
            if (this.qstLoadingView != null) {
                NgWebviewLog.d(TAG, "qstLoadingView: VISIBLE", new Object[0]);
                this.qstLoadingView.setVisibility(0);
                ((ImageView) findViewById(ResIdReader.getId(this, "qst_loading_close"))).setOnClickListener(new View.OnClickListener() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivity.3
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) throws JSONException, ClassNotFoundException {
                        JSONObject jSONObject = new JSONObject();
                        try {
                            jSONObject.put("methodId", SdkU3d.CALLBACKTYPE_OnWebViewNativeCall);
                            jSONObject.put("action", ControlCmdType.CLOSE);
                            jSONObject.put("data", "manual close");
                            if (NgWebviewActivity.this.wvParams.isSingleProcess() || NgWebviewActivity.this.wvParams.isSingleInstance()) {
                                NgWebviewActivity.this.handleExtendFunc(jSONObject.toString());
                            } else {
                                NgWebviewActivity.this.handleIPC(jSONObject.toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        NgWebviewActivity.this.closeWebview(SDefine.cj);
                    }
                });
            }
        }
    }

    private void initWebview() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        this.uniWv = new UniWebView(this, this.mUniWvRootView, this.wvParams);
        this.uniWv.setLayoutParams(layoutParams);
        this.mUniWvContainer.addView(this.uniWv, 0);
        if ("1".equals(this.wvParams.getWebviewBackgroundColor())) {
            this.uniWv.setBackgroundColor(0);
        }
        if (Build.VERSION.SDK_INT >= 29) {
            this.uniWv.setForceDarkAllowed(false);
        }
        String yYGameID = this.wvParams.getYYGameID();
        String appVersionName = this.wvParams.getAppVersionName();
        String scriptVersion = this.wvParams.getScriptVersion();
        String str = TextUtils.isEmpty(scriptVersion) ? appVersionName : scriptVersion;
        String channelID = this.wvParams.getChannelID();
        this.uniWv.setUserAgent(yYGameID, appVersionName, str, channelID);
        this.uniWv.setUserAgent(yYGameID, appVersionName, str, channelID, this.wvParams.getAdditionalUserAgent());
        this.uniWv.clearCache(true);
        this.uniWv.getSettings().setCacheMode(2);
        this.uniWv.getSettings().setAllowFileAccess(true);
        this.uniWv.getSettings().setTextZoom(100);
        this.uniWv.getSettings().setAllowFileAccessFromFileURLs(false);
        this.uniWv.getSettings().setAllowUniversalAccessFromFileURLs(false);
        this.uniWv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        this.uniWv.getSettings().setJavaScriptEnabled(true);
        this.uniWv.getSettings().setSavePassword(false);
        this.uniWv.addJavascriptInterface(this, "AndroidJSBridge");
        this.uniWv.addJavascriptInterface(this, "$CallbackInterface");
        this.uniWv.setUniWebViewCallback(new UniWebView.UniWebViewCallback() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivity.4
            @Override // com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView.UniWebViewCallback
            public void callback(String str2, String str3) throws JSONException, ClassNotFoundException {
                boolean z = false;
                NgWebviewLog.d(NgWebviewActivity.TAG, "UniWebview callback, json=" + str2 + ", jsMethod=" + str3, new Object[0]);
                String intercept_schemes = NgWebviewActivity.this.wvParams.getIntercept_schemes();
                try {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("methodId", "NGWebViewOpenURL");
                    JSONObject jSONObject2 = new JSONObject(str2);
                    String strOptString = jSONObject2.optString("func");
                    if (strOptString.contains("GameMusic")) {
                        String strOptString2 = jSONObject2.optString("params");
                        jSONObject.put("func", strOptString);
                        jSONObject.put("params", strOptString2);
                        jSONObject.put("callback", str3);
                        if (!NgWebviewActivity.this.wvParams.isSingleProcess() && !NgWebviewActivity.this.wvParams.isSingleInstance()) {
                            NgWebviewActivity.this.handleIPC(str2);
                            return;
                        }
                        NgWebviewActivity.this.handleExtendFunc(str2);
                        return;
                    }
                    if (!strOptString.contains("interceptUrl") || TextUtils.isEmpty(intercept_schemes)) {
                        return;
                    }
                    String[] strArrSplit = intercept_schemes.split(",");
                    String strOptString3 = jSONObject2.optString("params");
                    int length = strArrSplit.length;
                    int i = 0;
                    while (true) {
                        if (i >= length) {
                            break;
                        }
                        if (strOptString3.startsWith(strArrSplit[i])) {
                            z = true;
                            break;
                        }
                        i++;
                    }
                    if (z) {
                        if (strOptString3.startsWith(UniWebView.IDV_PREFIX)) {
                            jSONObject.put("gameHandleURL", strOptString3);
                            if (!NgWebviewActivity.this.wvParams.isSingleProcess() && !NgWebviewActivity.this.wvParams.isSingleInstance()) {
                                NgWebviewActivity.this.handleIPC(jSONObject.toString());
                                return;
                            }
                            NgWebviewActivity.this.handleExtendFunc(jSONObject.toString());
                            return;
                        }
                        if (strOptString3.startsWith(UniWebView.UNI_JSBRIDGE_PREFIX)) {
                            NgWebviewActivity.this.nativeCall(strOptString3.substring(strOptString3.indexOf("://msg/") + 7, strOptString3.indexOf("?data=")), strOptString3.substring(strOptString3.indexOf("?data=") + 6, strOptString3.indexOf("&random=")));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        if (Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 17) {
            this.uniWv.removeJavascriptInterface("searchBoxJavaBridge_");
            this.uniWv.removeJavascriptInterface("accessibility");
            this.uniWv.removeJavascriptInterface("accessibilityTraversal");
        }
        if (Build.VERSION.SDK_INT >= 19 && (getApplicationInfo().flags & 2) != 0) {
            UniWebView.setWebContentsDebuggingEnabled(true);
        }
        this.uniWv.loadUrl(this.wvParams.getUrl());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleIPC(String str) throws JSONException {
        NgWebviewLog.d(TAG, "handleIPC...", new Object[0]);
        NgWebviewLog.d(TAG, "json: " + str, new Object[0]);
        if (NGRemoteService.callback != null) {
            try {
                NgWebviewLog.d(TAG, "isModule: " + this.wvParams.isModule(), new Object[0]);
                JSONObject jSONObject = new JSONObject(str);
                if (this.wvParams.isModule()) {
                    jSONObject.put(SocialConstants.PARAM_SOURCE, this.wvParams.getSource());
                    NGRemoteService.callback.call(jSONObject.toString());
                } else {
                    NGRemoteService.callback.call(str);
                }
            } catch (RemoteException | JSONException e) {
                NgWebviewLog.e(TAG, "RemoteException >> " + e.getMessage());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleExtendFunc(String str) throws JSONException, ClassNotFoundException {
        NgWebviewLog.d(TAG, "handleExtendFunc...", new Object[0]);
        NgWebviewLog.d(TAG, "json: " + str, new Object[0]);
        try {
            Class.forName("com.netease.ntunisdk.base.SdkBase");
        } catch (ClassNotFoundException unused) {
            NgWebviewLog.e(TAG, "Didn't find unisdkClass , check the name again !");
            this.isHasUnisdk = false;
        }
        try {
            this.modulesManager = ModulesManager.getInst();
            if (this.isHasUnisdk) {
                this.sdkBase = (SdkBase) SdkMgr.getInst();
            }
            if (this.modulesManager == null && this.sdkBase == null) {
                return;
            }
            JSONObject jSONObject = new JSONObject(str);
            String strOptString = jSONObject.optString("methodId");
            String strOptString2 = jSONObject.optString(SocialConstants.PARAM_SOURCE);
            if (ACTION_NOTIFY_NATIVE.equals(strOptString)) {
                String strOptString3 = jSONObject.optJSONObject("reqData").optString("methodId");
                jSONObject.put("callback_id", jSONObject.optString("identifier"));
                if (!TextUtils.isEmpty(strOptString3)) {
                    if (TextUtils.isEmpty(strOptString2) && this.sdkBase != null) {
                        this.sdkBase.ntExtendFunc(jSONObject.toString());
                        return;
                    } else {
                        this.modulesManager.extendFunc(strOptString2, "ngWebViewGeneral", jSONObject.toString());
                        return;
                    }
                }
                if (TextUtils.isEmpty(strOptString2) && this.sdkBase != null) {
                    this.sdkBase.extendFuncCall(jSONObject.toString());
                    return;
                } else {
                    this.modulesManager.callback(strOptString2, "ngWebViewGeneral", jSONObject.toString());
                    return;
                }
            }
            if (ACTION_EXECUTE_EXTEND_FUNC.equals(strOptString)) {
                JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("reqData");
                String strOptString4 = jSONObjectOptJSONObject.optString("methodId");
                String strOptString5 = jSONObjectOptJSONObject.optString("channel");
                jSONObjectOptJSONObject.put("callback_id", jSONObject.optString("identifier"));
                if (TextUtils.isEmpty(strOptString4)) {
                    return;
                }
                if (TextUtils.isEmpty(strOptString2) && this.sdkBase != null) {
                    if ("ngshare".equals(strOptString5)) {
                        this.sdkBase.ntExtendFunc(jSONObjectOptJSONObject.toString(), getInstance());
                        return;
                    } else {
                        this.sdkBase.ntExtendFunc(jSONObjectOptJSONObject.toString());
                        return;
                    }
                }
                this.modulesManager.extendFunc(strOptString2, "ngWebViewGeneral", jSONObjectOptJSONObject.toString());
                return;
            }
            if (SdkU3d.CALLBACKTYPE_OnWebViewNativeCall.equals(strOptString)) {
                String strOptString6 = jSONObject.optString("action");
                String strOptString7 = jSONObject.optString("data");
                if (this.isHasUnisdk) {
                    WebViewProxy.getInstance().getCallbackInterface().nativeCall(strOptString6, strOptString7);
                    return;
                }
                return;
            }
            if ("handleOnActivityResult".equals(strOptString)) {
                this.sdkBase.handleOnActivityResult(jSONObject.optInt("requestCode"), jSONObject.optInt("resultCode"), this.onActivityResultIntent);
                return;
            }
            if ("ModuleBaseReInit".equals(strOptString)) {
                if (this.isHasUnisdk) {
                    this.sdkBase.ntExtendFunc(str);
                }
            } else if (TextUtils.isEmpty(strOptString2) && this.sdkBase != null) {
                this.sdkBase.extendFuncCall(str);
            } else {
                this.modulesManager.callback(strOptString2, "ngWebViewGeneral", str);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initNavigationView() {
        initNavigationItem((ImageView) findViewById(ResIdReader.getId(this, "r_close")), (ImageView) findViewById(ResIdReader.getId(this, "r_back")), (ImageView) findViewById(ResIdReader.getId(this, "r_forward")), (ImageView) findViewById(ResIdReader.getId(this, "r_refresh")));
        initNavigationItem((ImageView) findViewById(ResIdReader.getId(this, "t_close")), (ImageView) findViewById(ResIdReader.getId(this, "t_back")), (ImageView) findViewById(ResIdReader.getId(this, "t_forward")), (ImageView) findViewById(ResIdReader.getId(this, "t_refresh")));
    }

    private void initBlackBorder() {
        this.mBlackBorderRight = (LinearLayout) findViewById(ResIdReader.getId(this, "black_border_right"));
        this.mBlackBorderTop = (LinearLayout) findViewById(ResIdReader.getId(this, "black_border_top"));
        boolean z = false;
        NgWebviewLog.d(TAG, "wvParams.getCutoutWidth(): " + this.wvParams.getCutoutWidth(), new Object[0]);
        NgWebviewLog.d(TAG, "wvParams.getCutoutHeight(): " + this.wvParams.getCutoutHeight(), new Object[0]);
        int cutoutWidth = this.wvParams.getCutoutWidth() < this.wvParams.getCutoutHeight() ? this.wvParams.getCutoutWidth() : this.wvParams.getCutoutHeight();
        if (cutoutWidth == 0 && this.wvParams.isHasCutout()) {
            z = true;
        }
        if (!TextUtils.isEmpty(this.wvParams.getBlackBorderColor())) {
            try {
                this.mBlackBorderRight.setBackgroundColor(Color.parseColor(this.wvParams.getBlackBorderColor()));
                this.mBlackBorderTop.setBackgroundColor(Color.parseColor(this.wvParams.getBlackBorderColor()));
            } catch (Exception e) {
                e.printStackTrace();
                NgWebviewLog.e(TAG, "Failed to set color for BlackBorder, params: " + this.wvParams.getBlackBorderColor());
            }
        }
        this.mBlackBorderRight.setLayoutParams(new RelativeLayout.LayoutParams(z ? 90 : cutoutWidth, -1));
        LinearLayout linearLayout = this.mBlackBorderTop;
        if (z) {
            cutoutWidth = 90;
        }
        linearLayout.setLayoutParams(new RelativeLayout.LayoutParams(-1, cutoutWidth));
    }

    private void initNavigationItem(ImageView imageView, ImageView imageView2, ImageView imageView3, ImageView imageView4) {
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) throws JSONException, ClassNotFoundException {
                NgWebviewActivity.this.closeWebview(SDefine.cj);
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivity.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (NgWebviewActivity.this.uniWv != null && NgWebviewActivity.this.uniWv.canGoBack() && (!NgWebviewActivity.this.wvParams.isHasPdfView() || NgWebviewActivity.this.uniWv.mPdfViewRoot.getVisibility() != 0)) {
                    NgWebviewActivity.this.uniWv.goBack();
                }
                if (!NgWebviewActivity.this.wvParams.isHasPdfView() || NgWebviewActivity.this.uniWv == null) {
                    return;
                }
                NgWebviewActivity.this.uniWv.showWebview();
                NgWebviewActivity.this.uniWv.mPdfViewRoot.setVisibility(8);
            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivity.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (NgWebviewActivity.this.uniWv != null && NgWebviewActivity.this.uniWv.canGoForward() && (!NgWebviewActivity.this.wvParams.isHasPdfView() || NgWebviewActivity.this.uniWv.mPdfViewRoot.getVisibility() != 0)) {
                    NgWebviewActivity.this.uniWv.goForward();
                }
                if (!NgWebviewActivity.this.wvParams.isHasPdfView() || NgWebviewActivity.this.uniWv == null) {
                    return;
                }
                NgWebviewActivity.this.uniWv.showWebview();
                NgWebviewActivity.this.uniWv.mPdfViewRoot.setVisibility(8);
            }
        });
        imageView4.setOnClickListener(new View.OnClickListener() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivity.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                NgWebviewLog.d(NgWebviewActivity.TAG, "networkType: " + NgWebviewActivity.getNetworkType(), new Object[0]);
                if (NgWebviewActivity.this.uniWv != null && (!NgWebviewActivity.this.wvParams.isHasPdfView() || NgWebviewActivity.this.uniWv.mPdfViewRoot.getVisibility() != 0)) {
                    NgWebviewActivity.this.uniWv.reload();
                }
                if (!NgWebviewActivity.this.wvParams.isHasPdfView() || NgWebviewActivity.this.uniWv == null) {
                    return;
                }
                NgWebviewActivity.this.uniWv.showWebview();
                NgWebviewActivity.this.uniWv.mPdfViewRoot.setVisibility(8);
            }
        });
    }

    public void closeWebview(final String str) throws JSONException, ClassNotFoundException {
        NgWebviewLog.d(TAG, "wvParams.isSingleProcess(): " + this.wvParams.isSingleProcess(), new Object[0]);
        if ((this.wvParams.isSingleProcess() || this.wvParams.isSingleInstance()) && "OverrideClose".equals(str)) {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("methodId", "ModuleBaseReInit");
                handleExtendFunc(jSONObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivity.9
            @Override // java.lang.Runnable
            public void run() throws JSONException, ClassNotFoundException {
                JSONObject jSONObject2 = new JSONObject();
                try {
                    jSONObject2.put("methodId", "NGWebViewClose");
                    jSONObject2.put("func", "webview_close");
                    jSONObject2.put("URL", NgWebviewActivity.this.uniWv.getUrl());
                    jSONObject2.put("closeSource", str);
                    jSONObject2.put("webviewCtx", NgWebviewActivity.this.wvParams.getWebviewCtx());
                    if (NgWebviewActivity.this.wvParams.isSingleProcess() || NgWebviewActivity.this.wvParams.isSingleInstance()) {
                        NgWebviewActivity.this.handleExtendFunc(jSONObject2.toString());
                    } else {
                        NgWebviewActivity.this.handleIPC(jSONObject2.toString());
                    }
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
            }
        });
        try {
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("methodId", "NtCloseWebView");
            jSONObject2.put("result", "0");
            if (this.wvParams.isSingleProcess() || this.wvParams.isSingleInstance()) {
                handleExtendFunc(jSONObject2.toString());
            } else {
                handleIPC(jSONObject2.toString());
            }
        } catch (JSONException e2) {
            NgWebviewLog.i(TAG, "jsonException=" + e2.toString());
            e2.printStackTrace();
        }
        try {
            JSONObject jSONObject3 = new JSONObject();
            jSONObject3.put("methodId", ACTION_NOTIFY_NATIVE);
            JSONObject jSONObject4 = new JSONObject();
            jSONObject4.put("methodId", ACTION_CLOSEWEBVIEW);
            jSONObject4.put("closeSource", str);
            jSONObject4.put("webviewCtx", this.wvParams.getWebviewCtx());
            jSONObject3.put("reqData", jSONObject4);
            if (this.wvParams.isSingleProcess() || this.wvParams.isSingleInstance()) {
                handleExtendFunc(jSONObject3.toString());
            } else {
                handleIPC(jSONObject3.toString());
            }
        } catch (JSONException e3) {
            NgWebviewLog.i(TAG, "jsonException=" + e3.toString());
            e3.printStackTrace();
        }
        if (Build.VERSION.SDK_INT < 21) {
            CookieManager.getInstance().removeSessionCookie();
        } else {
            CookieManager.getInstance().removeSessionCookies(null);
        }
        finish();
    }

    private void showTopNavigationBar() {
        if (this.wvParams.isNavigationBarVisible()) {
            this.mTopNavigationBar.setVisibility(0);
        } else {
            this.mTopNavigationBar.setVisibility(8);
        }
        this.mRightNavigationBar.setVisibility(8);
    }

    private void showRightNavigationBar() {
        if (this.wvParams.isNavigationBarVisible()) {
            this.mRightNavigationBar.setVisibility(0);
        } else {
            this.mRightNavigationBar.setVisibility(8);
        }
        this.mTopNavigationBar.setVisibility(8);
    }

    private void showRightBlackBorder() {
        this.mBlackBorderRight.setVisibility(0);
        this.mBlackBorderTop.setVisibility(8);
    }

    private void showTopBlackBorder() {
        this.mBlackBorderTop.setVisibility(0);
        this.mBlackBorderRight.setVisibility(8);
    }

    private void setScreenOrientation() {
        NgWebviewLog.d(TAG, "setScreenOrientation, ORIENTATION = " + ORIENTATION, new Object[0]);
        int i = ORIENTATION;
        if (1 == i) {
            setRequestedOrientation(1);
            return;
        }
        if (7 == i) {
            setRequestedOrientation(7);
            return;
        }
        if (2 == i) {
            setRequestedOrientation(0);
            return;
        }
        if (4 == i) {
            setRequestedOrientation(4);
            return;
        }
        if (5 == i) {
            setRequestedOrientation(6);
        } else if (3 == i) {
            setRequestedOrientation(9);
        } else if (6 == i) {
            setRequestedOrientation(8);
        }
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        hideVirtualNavigationBar();
        UniWebView uniWebView = this.uniWv;
        if (uniWebView != null) {
            uniWebView.onResume();
        }
    }

    @Override // android.app.Activity
    protected void onStop() {
        super.onStop();
        UniWebView uniWebView = this.uniWv;
        if (uniWebView != null) {
            uniWebView.onStop();
        }
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        NgWebviewLog.d(TAG, "onConfigurationChanged, orientation = " + configuration.orientation, new Object[0]);
        View decorView = getWindow().getDecorView();
        if (!this.wvParams.isNavigationBarVisible() && Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            decorView.setSystemUiVisibility(8);
            return;
        }
        if (!this.wvParams.isNavigationBarVisible() && Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 30) {
            decorView.setSystemUiVisibility(5894);
        } else {
            if (this.wvParams.isNavigationBarVisible() || Build.VERSION.SDK_INT < 30) {
                return;
            }
            WindowInsetsController windowInsetsController = decorView.getWindowInsetsController();
            windowInsetsController.hide(WindowInsets.Type.navigationBars());
            windowInsetsController.hide(WindowInsets.Type.statusBars());
        }
    }

    @Override // android.app.Activity
    public void onBackPressed() throws JSONException, ClassNotFoundException {
        UniWebView uniWebView;
        NgWebviewLog.d(TAG, "onBackPressed...", new Object[0]);
        NgWebviewLog.d(TAG, "wvParams.isSurvey(): " + this.wvParams.isSurvey(), new Object[0]);
        NgWebviewLog.d(TAG, "wvParams.isSupportBackKey(): " + this.wvParams.isSupportBackKey(), new Object[0]);
        WebviewParams webviewParams = this.wvParams;
        if (webviewParams != null) {
            if ((!webviewParams.isSurvey() || this.wvParams.isSupportBackKey()) && !this.wvParams.isSecureVerify()) {
                NgWebviewLog.d(TAG, "uniWv.canGoBack(): " + this.uniWv.canGoBack(), new Object[0]);
                if (this.uniWv.canGoBack()) {
                    UniWebView uniWebView2 = this.uniWv;
                    if (uniWebView2 != null && uniWebView2.canGoBack() && (!this.wvParams.isHasPdfView() || this.uniWv.mPdfViewRoot.getVisibility() != 0)) {
                        this.uniWv.goBack();
                    }
                    if (!this.wvParams.isHasPdfView() || (uniWebView = this.uniWv) == null) {
                        return;
                    }
                    uniWebView.showWebview();
                    this.uniWv.mPdfViewRoot.setVisibility(8);
                    return;
                }
                closeWebview(SDefine.cj);
            }
        }
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        NgWebviewLog.d(TAG, "onWindowFocusChanged, hasFocus = " + z, new Object[0]);
        View decorView = getWindow().getDecorView();
        if (!this.wvParams.isNavigationBarVisible() && z && Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            decorView.setSystemUiVisibility(8);
            return;
        }
        if (!this.wvParams.isNavigationBarVisible() && z && Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 30) {
            decorView.setSystemUiVisibility(5894);
            return;
        }
        if (this.wvParams.isNavigationBarVisible() || !z || Build.VERSION.SDK_INT < 30) {
            return;
        }
        WindowInsetsController windowInsetsController = decorView.getWindowInsetsController();
        windowInsetsController.hide(WindowInsets.Type.navigationBars());
        windowInsetsController.hide(WindowInsets.Type.statusBars());
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        SensorManager sensorManager;
        NgWebviewLog.d(TAG, "onDestroy...", new Object[0]);
        if (Build.VERSION.SDK_INT >= 14 && (sensorManager = this.mSensorManager) != null) {
            sensorManager.unregisterListener(this.mSensorEventListener);
        }
        if (this.originalLayoutInDisplayCutoutMode != -1 && Build.VERSION.SDK_INT >= 28) {
            WindowManager.LayoutParams attributes = getWindow().getAttributes();
            attributes.layoutInDisplayCutoutMode = this.originalLayoutInDisplayCutoutMode;
            getWindow().setAttributes(attributes);
        }
        UniWebView uniWebView = this.uniWv;
        if (uniWebView != null) {
            uniWebView.loadDataWithBaseURL(null, "", Const.HTML_CONTENT_TYPE, RSASignature.c, null);
            this.uniWv.clearHistory();
            ((ViewGroup) this.uniWv.getParent()).removeView(this.uniWv);
            this.uniWv.destroy();
            this.uniWv = null;
        }
        super.onDestroy();
        mInstance = null;
    }

    @Override // android.app.Activity
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        NgWebviewLog.d(TAG, "sdkOnRequestPermissionsResult, requestCode = " + i, new Object[0]);
        super.onRequestPermissionsResult(i, strArr, iArr);
        ModulesManager.getInst().onRequestPermissionsResult(i, strArr, iArr);
    }

    @Override // android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) throws JSONException, ClassNotFoundException {
        NgWebviewLog.d(TAG, "onActivityResult...", new Object[0]);
        super.onActivityResult(i, i2, intent);
        ModulesManager.getInst().onActivityResult(i, i2, intent);
        if (this.wvParams.isSingleProcess() || this.wvParams.isSingleInstance()) {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("methodId", "handleOnActivityResult");
                jSONObject.put("requestCode", i);
                jSONObject.put("resultCode", i2);
                this.onActivityResultIntent = intent;
                handleExtendFunc(jSONObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        UniWebView uniWebView = this.uniWv;
        if (uniWebView != null) {
            uniWebView.onActivityResult(i, i2, intent);
        }
    }

    @JavascriptInterface
    public void nativeCall(final String str, final String str2) {
        NgWebviewLog.d(TAG, "$NGRemoteService.callback.call2, action:" + str + ", data:" + str2, new Object[0]);
        runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivity.10
            @Override // java.lang.Runnable
            public void run() throws JSONException, ClassNotFoundException {
                if (str.equals(ControlCmdType.CLOSE)) {
                    NgWebviewActivity.this.closeWebview("js");
                }
                if (str.equals("loaded") && NgWebviewActivity.this.qstLoadingView != null) {
                    NgWebviewActivity.this.qstLoadingView.setVisibility(8);
                }
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.put("methodId", SdkU3d.CALLBACKTYPE_OnWebViewNativeCall);
                    jSONObject.put("action", str);
                    jSONObject.put("data", str2);
                    if (NgWebviewActivity.this.wvParams.isSingleProcess() || NgWebviewActivity.this.wvParams.isSingleInstance()) {
                        NgWebviewActivity.this.handleExtendFunc(jSONObject.toString());
                    } else {
                        NgWebviewActivity.this.handleIPC(jSONObject.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @JavascriptInterface
    public void postMsgToNative(String str) throws JSONException, ClassNotFoundException {
        NgWebviewLog.d(TAG, "postMsgToNative, json=" + str, new Object[0]);
        try {
            JSONObject jSONObject = new JSONObject(str);
            String strOptString = jSONObject.optString("methodId");
            JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("reqData");
            String strOptString2 = jSONObject.optString("identifier");
            JSONObject jSONObject2 = new JSONObject();
            if (ACTION_CLOSEWEBVIEW.equalsIgnoreCase(strOptString)) {
                runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivity.11
                    @Override // java.lang.Runnable
                    public void run() throws JSONException, ClassNotFoundException {
                        NgWebviewActivity.this.closeWebview("js");
                    }
                });
                return;
            }
            if (ACTION_COPE2PASTEBOARD.equalsIgnoreCase(strOptString)) {
                String strOptString3 = jSONObjectOptJSONObject.optString("copyText");
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService("clipboard");
                clipboardManager.setPrimaryClip(ClipData.newPlainText(TAG, strOptString3));
                jSONObject2.put("result", clipboardManager.hasPrimaryClip() ? "success" : h.j);
                onJsCallback(strOptString2, jSONObject2.toString());
                return;
            }
            if (ACTION_OPENBROWSER.equalsIgnoreCase(strOptString)) {
                String strOptString4 = jSONObjectOptJSONObject.optString(Const.WEB_URL);
                if (!strOptString4.startsWith("http://") && !strOptString4.startsWith("https://")) {
                    strOptString4 = "http://" + strOptString4;
                }
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse(strOptString4)));
                return;
            }
            if (ACTION_SAVEWEBIMAGE.equalsIgnoreCase(strOptString)) {
                this.mIimageURL = jSONObjectOptJSONObject.optString("imageURL");
                this.mIdentifier = strOptString2;
                this.mTargetSdkVersion = getTargetSdkVersion(this);
                if (Build.VERSION.SDK_INT < 23 || this.mTargetSdkVersion < 23) {
                    return;
                }
                JSONObject jSONObject3 = new JSONObject();
                try {
                    jSONObject3.put("methodId", "requestPermission");
                    jSONObject3.put("permissionName", "android.permission.WRITE_EXTERNAL_STORAGE");
                    jSONObject3.put("firstText", getResources().getString(R.string.ng_wv_storage_description));
                    jSONObject3.put("retryText", getResources().getString(R.string.ng_wv_storage_description));
                    jSONObject3.put("positiveText", getResources().getString(R.string.ng_wv_continue));
                    jSONObject3.put("negativeText", getResources().getString(R.string.ng_wv_cancel));
                    jSONObject3.put("firstTwoBtn", "true");
                    jSONObject3.put("shouldRetry", Constants.CASEFIRST_FALSE);
                    jSONObject3.put("showDialog", "true");
                    jSONObject3.put("gotoSetting", "true");
                    jSONObject3.put("gotoSettingReason", getResources().getString(R.string.ng_wv_open_storage_tips));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ModulesManager.getInst().extendFunc("ngWebViewGeneral", PermissionConstant.PERMISSION_KEY, jSONObject3.toString());
                return;
            }
            if (ACTION_SHAREMODULE.equalsIgnoreCase(strOptString)) {
                return;
            }
            if (ACTION_SURVEYUSERACTION.equalsIgnoreCase(strOptString)) {
                nativeCall(jSONObjectOptJSONObject.optString("action"), jSONObjectOptJSONObject.optString("data"));
                return;
            }
            if (ACTION_SURVEYSTATE.equalsIgnoreCase(strOptString)) {
                nativeCall(jSONObjectOptJSONObject.optString("state"), jSONObjectOptJSONObject.optString("data"));
                return;
            }
            if (ACTION_NOTIFY_NATIVE.equalsIgnoreCase(strOptString)) {
                if (!this.wvParams.isSingleProcess() && !this.wvParams.isSingleInstance()) {
                    handleIPC(str);
                    return;
                }
                handleExtendFunc(str);
                return;
            }
            if (ACTION_DEBUG_JS_LOG.equalsIgnoreCase(strOptString)) {
                NgWebviewLog.d(TAG, "jsLogStr: " + jSONObjectOptJSONObject.optString("reqData"), new Object[0]);
                return;
            }
            if (ACTION_EXECUTE_EXTEND_FUNC.equalsIgnoreCase(strOptString)) {
                if (!this.wvParams.isSingleProcess() && !this.wvParams.isSingleInstance()) {
                    handleIPC(str);
                    return;
                }
                handleExtendFunc(str);
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
    }

    public void onJsCallback(final String str, final String str2) {
        runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivity.12
            @Override // java.lang.Runnable
            public void run() {
                NgWebviewActivity.this.uniWv.evaluateJavascript("window.UniSDKNativeCallback", "'" + str + "', '" + str2 + "'");
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveWebImg(final String str, final String str2, final boolean z) {
        new Thread(new Runnable() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivity.13
            /* JADX WARN: Removed duplicated region for block: B:7:0x001b  */
            @Override // java.lang.Runnable
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public void run() throws org.json.JSONException {
                /*
                    r3 = this;
                    boolean r0 = r2
                    if (r0 == 0) goto L1b
                    com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivity r0 = com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivity.this
                    java.lang.String r1 = r3
                    android.graphics.Bitmap r0 = com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivity.access$1000(r0, r1)
                    com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivity r1 = com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivity.this
                    java.lang.String r0 = com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivity.access$1100(r1, r1, r0)
                    boolean r0 = android.text.TextUtils.isEmpty(r0)
                    if (r0 != 0) goto L1b
                    java.lang.String r0 = "success"
                    goto L1d
                L1b:
                    java.lang.String r0 = "failed"
                L1d:
                    org.json.JSONObject r1 = new org.json.JSONObject
                    r1.<init>()
                    java.lang.String r2 = "result"
                    r1.put(r2, r0)     // Catch: org.json.JSONException -> L33
                    com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivity r0 = com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivity.this     // Catch: org.json.JSONException -> L33
                    java.lang.String r2 = r4     // Catch: org.json.JSONException -> L33
                    java.lang.String r1 = r1.toString()     // Catch: org.json.JSONException -> L33
                    r0.onJsCallback(r2, r1)     // Catch: org.json.JSONException -> L33
                    goto L37
                L33:
                    r0 = move-exception
                    r0.printStackTrace()
                L37:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivity.AnonymousClass13.run():void");
            }
        }).start();
    }

    private static int getTargetSdkVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private boolean isLandscape() {
        NgWebviewLog.d(TAG, "isLandscape() orientation: " + this.wvParams.getOrientation(), new Object[0]);
        if (this.wvParams.getOrientation() == 2 || this.wvParams.getOrientation() == 5 || this.wvParams.getOrientation() == 6) {
            return true;
        }
        if (this.wvParams.getOrientation() == 1 || this.wvParams.getOrientation() == 7 || this.wvParams.getOrientation() == 3) {
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String saveImageToPhotos(Context context, Bitmap bitmap) throws IOException {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "ngwebview");
        if (!file.exists()) {
            file.mkdir();
        }
        File file2 = new File(file, System.currentTimeMillis() + ".jpg");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(Uri.fromFile(file2));
        context.sendBroadcast(intent);
        return file2.getPath();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Bitmap getRemoteBitmap(String str) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();
            return BitmapFactory.decodeStream(httpURLConnection.getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private void initNgwebviewCloseBtn() {
        this.mNgwebviewClose = (ImageView) findViewById(ResIdReader.getId(this, "ngwebview_close"));
        if (this.wvParams.isCloseButtonVisible()) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mNgwebviewClose.getLayoutParams();
            layoutParams.width = this.wvParams.getCloseBtnWidth();
            layoutParams.height = this.wvParams.getCloseBtnHeight();
            layoutParams.setMargins(this.wvParams.getCloseBtnOriginX(), this.wvParams.getCloseBtnOriginY(), 0, 0);
            this.mNgwebviewClose.setLayoutParams(layoutParams);
            this.mNgwebviewClose.setVisibility(0);
        } else {
            this.mNgwebviewClose.setVisibility(8);
        }
        this.mNgwebviewClose.setOnClickListener(new View.OnClickListener() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivity.14
            @Override // android.view.View.OnClickListener
            public void onClick(View view) throws JSONException, ClassNotFoundException {
                NgWebviewActivity.this.closeWebview(SDefine.cj);
            }
        });
    }

    public static String getNetworkType() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("methodId", "ntGetNetworktype");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ModulesManager.getInst().extendFunc("ngWebViewGeneral", "deviceInfo", jSONObject.toString());
    }
}