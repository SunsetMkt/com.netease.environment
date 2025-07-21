package com.netease.ntunisdk.base;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.CCMsgSdk.ControlCmdType;
import com.netease.ntunisdk.CommonTips;
import com.netease.ntunisdk.base.utils.CachedThreadPoolUtil;
import com.netease.ntunisdk.base.utils.NetConnectivity;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView;
import com.netease.ntunisdk.modules.personalinfolist.HookManager;
import org.jose4j.jwx.HeaderParameterNames;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class WebViewProxy {
    private static String b = "UniSDK WebViewProxy";
    private static volatile WebViewProxy c;
    private static boolean d;
    private static OnWebViewListener e;
    private int q;
    private Context f = null;
    private WebView g = null;
    private Dialog h = null;
    private ProgressDialog i = null;
    private int j = 0;
    private Button k = null;
    private String l = null;
    private RelativeLayout m = null;
    private int n = 0;
    private int o = 0;
    private int p = 0;
    private int r = 1;

    /* renamed from: a */
    Handler f1810a = new Handler() { // from class: com.netease.ntunisdk.base.WebViewProxy.1
        AnonymousClass1() {
        }

        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            if (message.what != 1) {
                return;
            }
            try {
                int i = message.arg1;
                if (WebViewProxy.this.g != null && WebViewProxy.this.k != null && WebViewProxy.this.i != null && WebViewProxy.this.g.getProgress() < 100 && i == WebViewProxy.this.r) {
                    UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [Handler] [MSG_PAGE_TIMEOUT] timeout, show close button");
                    WebViewProxy.this.k.setVisibility(0);
                }
                if (WebViewProxy.this.i == null || !WebViewProxy.this.i.isShowing()) {
                    return;
                }
                WebViewProxy.this.i.dismiss();
            } catch (Exception e2) {
                UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [Handler] [MSG_PAGE_TIMEOUT] Exception=".concat(String.valueOf(e2)));
                if (WebViewProxy.this.i == null || !WebViewProxy.this.i.isShowing()) {
                    return;
                }
                WebViewProxy.this.i.dismiss();
            }
        }
    };

    static /* synthetic */ boolean a(int i) {
        return i == 200 || i == 301 || i == 302;
    }

    static /* synthetic */ Dialog g(WebViewProxy webViewProxy) {
        webViewProxy.h = null;
        return null;
    }

    static /* synthetic */ WebView h(WebViewProxy webViewProxy) {
        webViewProxy.g = null;
        return null;
    }

    static /* synthetic */ int i(WebViewProxy webViewProxy) {
        webViewProxy.j = 0;
        return 0;
    }

    /* renamed from: com.netease.ntunisdk.base.WebViewProxy$1 */
    final class AnonymousClass1 extends Handler {
        AnonymousClass1() {
        }

        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            if (message.what != 1) {
                return;
            }
            try {
                int i = message.arg1;
                if (WebViewProxy.this.g != null && WebViewProxy.this.k != null && WebViewProxy.this.i != null && WebViewProxy.this.g.getProgress() < 100 && i == WebViewProxy.this.r) {
                    UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [Handler] [MSG_PAGE_TIMEOUT] timeout, show close button");
                    WebViewProxy.this.k.setVisibility(0);
                }
                if (WebViewProxy.this.i == null || !WebViewProxy.this.i.isShowing()) {
                    return;
                }
                WebViewProxy.this.i.dismiss();
            } catch (Exception e2) {
                UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [Handler] [MSG_PAGE_TIMEOUT] Exception=".concat(String.valueOf(e2)));
                if (WebViewProxy.this.i == null || !WebViewProxy.this.i.isShowing()) {
                    return;
                }
                WebViewProxy.this.i.dismiss();
            }
        }
    }

    public static WebViewProxy getInstance() {
        if (c == null) {
            synchronized (WebViewProxy.class) {
                if (c == null) {
                    c = new WebViewProxy();
                }
            }
        }
        return c;
    }

    public CallbackInterface getCallbackInterface() {
        return new CallbackInterface();
    }

    public void setWebViewListener(OnWebViewListener onWebViewListener) {
        e = onWebViewListener;
    }

    /* renamed from: com.netease.ntunisdk.base.WebViewProxy$2 */
    final class AnonymousClass2 extends WebViewClient {
        AnonymousClass2() {
        }

        @Override // android.webkit.WebViewClient
        public final boolean shouldOverrideUrlLoading(WebView webView, String str) throws JSONException {
            UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [init] [shouldOverrideUrlLoading] url=".concat(String.valueOf(str)));
            if (str.startsWith("weixin://")) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(str));
                try {
                    HookManager.startActivity(WebViewProxy.this.f, intent);
                } catch (Exception unused) {
                    ((Activity) WebViewProxy.this.f).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.WebViewProxy.2.1
                        AnonymousClass1() {
                        }

                        @Override // java.lang.Runnable
                        public final void run() {
                            new AlertDialog.Builder(WebViewProxy.this.f).setMessage("\u672a\u68c0\u6d4b\u5230\u5ba2\u6237\u7aef\uff0c\u8bf7\u5b89\u88c5\u540e\u91cd\u8bd5\u3002").setPositiveButton(CommonTips.OK_BTN, new DialogInterface.OnClickListener() { // from class: com.netease.ntunisdk.base.WebViewProxy.2.1.1
                                @Override // android.content.DialogInterface.OnClickListener
                                public final void onClick(DialogInterface dialogInterface, int i) {
                                }

                                DialogInterfaceOnClickListenerC00711() {
                                }
                            }).setNegativeButton("\u53d6\u6d88", (DialogInterface.OnClickListener) null).show();
                        }

                        /* renamed from: com.netease.ntunisdk.base.WebViewProxy$2$1$1 */
                        final class DialogInterfaceOnClickListenerC00711 implements DialogInterface.OnClickListener {
                            @Override // android.content.DialogInterface.OnClickListener
                            public final void onClick(DialogInterface dialogInterface, int i) {
                            }

                            DialogInterfaceOnClickListenerC00711() {
                            }
                        }
                    });
                }
                return true;
            }
            if (str.startsWith("alipays://")) {
                Intent intent2 = new Intent();
                intent2.setAction("android.intent.action.VIEW");
                intent2.setData(Uri.parse(str));
                try {
                    HookManager.startActivity(WebViewProxy.this.f, intent2);
                } catch (Exception e) {
                    e.printStackTrace();
                    UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [init] [shouldOverrideUrlLoading] Exception=" + e.toString());
                    if (WebViewProxy.this.i != null && WebViewProxy.this.i.isShowing()) {
                        WebViewProxy.this.i.dismiss();
                    }
                }
                return true;
            }
            if (str.contains("http://") || str.contains("https://") || !str.contains("://")) {
                UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [init] [shouldOverrideUrlLoading] \u5c5e\u4e8ehttp/https\u534f\u8bae");
                CachedThreadPoolUtil.getInstance().exec(new Runnable() { // from class: com.netease.ntunisdk.base.WebViewProxy.2.2

                    /* renamed from: a */
                    final /* synthetic */ String f1815a;

                    RunnableC00722(String str2) {
                        str = str2;
                    }

                    @Override // java.lang.Runnable
                    public final void run() throws Throwable {
                        int iB = WebViewProxy.b(str);
                        UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [init] [shouldOverrideUrlLoading] status=".concat(String.valueOf(iB)));
                        if (WebViewProxy.a(iB)) {
                            return;
                        }
                        UniSdkUtils.e(WebViewProxy.b, "WebViewProxy [init] [shouldOverrideUrlLoading] fail to open url:" + str + ", status:" + iB);
                        WebViewProxy.this.new CallbackInterface().nativeCall(ControlCmdType.CLOSE, "error");
                    }
                });
                return false;
            }
            UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [init] [shouldOverrideUrlLoading] \u5c5e\u4e8e\u81ea\u5b9a\u4e49\u534f\u8bae");
            String propStr = SdkMgr.getInst().getPropStr(ConstProp.WEBVIEW_UNKNOWN_PROTOCOL_HANDLE_MODE, "1");
            UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [init] [shouldOverrideUrlLoading] unknownProtocolHandleMode=".concat(String.valueOf(propStr)));
            if (propStr.equals("1")) {
                Intent intent3 = new Intent();
                intent3.setAction("android.intent.action.VIEW");
                intent3.setData(Uri.parse(str2));
                try {
                    HookManager.startActivity(WebViewProxy.this.f, intent3);
                } catch (Exception e2) {
                    e2.printStackTrace();
                    UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [init] [shouldOverrideUrlLoading] Exception=" + e2.toString());
                    if (WebViewProxy.this.i != null && WebViewProxy.this.i.isShowing()) {
                        WebViewProxy.this.i.dismiss();
                    }
                }
            } else if (propStr.equals("0")) {
                WebViewProxy.this.new CallbackInterface().nativeCall(ControlCmdType.CLOSE, "error");
            }
            return true;
        }

        /* renamed from: com.netease.ntunisdk.base.WebViewProxy$2$1 */
        final class AnonymousClass1 implements Runnable {
            AnonymousClass1() {
            }

            @Override // java.lang.Runnable
            public final void run() {
                new AlertDialog.Builder(WebViewProxy.this.f).setMessage("\u672a\u68c0\u6d4b\u5230\u5ba2\u6237\u7aef\uff0c\u8bf7\u5b89\u88c5\u540e\u91cd\u8bd5\u3002").setPositiveButton(CommonTips.OK_BTN, new DialogInterface.OnClickListener() { // from class: com.netease.ntunisdk.base.WebViewProxy.2.1.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                    }

                    DialogInterfaceOnClickListenerC00711() {
                    }
                }).setNegativeButton("\u53d6\u6d88", (DialogInterface.OnClickListener) null).show();
            }

            /* renamed from: com.netease.ntunisdk.base.WebViewProxy$2$1$1 */
            final class DialogInterfaceOnClickListenerC00711 implements DialogInterface.OnClickListener {
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                }

                DialogInterfaceOnClickListenerC00711() {
                }
            }
        }

        /* renamed from: com.netease.ntunisdk.base.WebViewProxy$2$2 */
        final class RunnableC00722 implements Runnable {

            /* renamed from: a */
            final /* synthetic */ String f1815a;

            RunnableC00722(String str2) {
                str = str2;
            }

            @Override // java.lang.Runnable
            public final void run() throws Throwable {
                int iB = WebViewProxy.b(str);
                UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [init] [shouldOverrideUrlLoading] status=".concat(String.valueOf(iB)));
                if (WebViewProxy.a(iB)) {
                    return;
                }
                UniSdkUtils.e(WebViewProxy.b, "WebViewProxy [init] [shouldOverrideUrlLoading] fail to open url:" + str + ", status:" + iB);
                WebViewProxy.this.new CallbackInterface().nativeCall(ControlCmdType.CLOSE, "error");
            }
        }

        @Override // android.webkit.WebViewClient
        public final void onReceivedError(WebView webView, int i, String str, String str2) throws JSONException {
            UniSdkUtils.e(WebViewProxy.b, "WebViewProxy [init] [onReceivedError], errorCode: " + str2 + ", description: " + str + ", failingUrl: " + str2);
            WebViewProxy.this.new CallbackInterface().nativeCall(ControlCmdType.CLOSE, "error");
        }

        @Override // android.webkit.WebViewClient
        public final void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [init] [onPageStarted]");
            WebViewProxy.this.i.show();
            CachedThreadPoolUtil.getInstance().exec(new Runnable() { // from class: com.netease.ntunisdk.base.WebViewProxy.2.3
                AnonymousClass3() {
                }

                @Override // java.lang.Runnable
                public final void run() {
                    Message message = new Message();
                    message.arg1 = WebViewProxy.this.r;
                    message.what = 1;
                    WebViewProxy.this.f1810a.sendMessageDelayed(message, 6000L);
                }
            });
        }

        /* renamed from: com.netease.ntunisdk.base.WebViewProxy$2$3 */
        final class AnonymousClass3 implements Runnable {
            AnonymousClass3() {
            }

            @Override // java.lang.Runnable
            public final void run() {
                Message message = new Message();
                message.arg1 = WebViewProxy.this.r;
                message.what = 1;
                WebViewProxy.this.f1810a.sendMessageDelayed(message, 6000L);
            }
        }

        @Override // android.webkit.WebViewClient
        public final void onPageFinished(WebView webView, String str) {
            if (WebViewProxy.this.i == null || !WebViewProxy.this.i.isShowing()) {
                return;
            }
            WebViewProxy.this.i.dismiss();
        }
    }

    /* renamed from: com.netease.ntunisdk.base.WebViewProxy$3 */
    final class AnonymousClass3 extends WebChromeClient {
        AnonymousClass3() {
        }

        @Override // android.webkit.WebChromeClient
        public final boolean onJsAlert(WebView webView, String str, String str2, JsResult jsResult) {
            return super.onJsAlert(webView, str, str2, jsResult);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.WebViewProxy$4 */
    final class AnonymousClass4 implements View.OnClickListener {
        AnonymousClass4() {
        }

        @Override // android.view.View.OnClickListener
        public final void onClick(View view) {
            if (WebViewProxy.this.h == null || !WebViewProxy.this.h.isShowing()) {
                return;
            }
            SdkMgr.getInst().setPropStr(ConstProp.WEBVIEW_FULLFIT, "0");
            SdkMgr.getInst().setPropStr(ConstProp.WEBVIEW_CLBTN, "0");
            WebViewProxy.this.closeWebView();
        }
    }

    /* renamed from: com.netease.ntunisdk.base.WebViewProxy$5 */
    final class AnonymousClass5 implements DialogInterface.OnCancelListener {
        AnonymousClass5() {
        }

        @Override // android.content.DialogInterface.OnCancelListener
        public final void onCancel(DialogInterface dialogInterface) throws JSONException {
            boolean unused = WebViewProxy.d = false;
            WebViewProxy.g(WebViewProxy.this);
            WebViewProxy.h(WebViewProxy.this);
            WebViewProxy.i(WebViewProxy.this);
            try {
                UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [init] [onCancel] [closeWebView] callback start");
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("methodId", "NtCloseWebView");
                jSONObject.put("result", "0");
                ((SdkBase) SdkMgr.getInst()).extendFuncCall(jSONObject.toString());
            } catch (Exception e) {
                UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [init] [onCancel] [closeWebView] callback Exception=" + e.toString());
                e.printStackTrace();
            }
        }
    }

    private void d() throws IllegalAccessException, IllegalArgumentException {
        UniSdkUtils.d(b, "WebViewProxy [adjustSize]");
        Context context = this.f;
        Activity activity = (Activity) context;
        int i = context.getResources().getConfiguration().orientation;
        UniSdkUtils.d(b, "WebViewProxy [adjustSize] tOrientation=" + i + ", mOrientation=" + this.j);
        if (i != this.j) {
            UniSdkUtils.d(b, "WebViewProxy [adjustSize] need refresh");
            this.p = 1;
        }
        UniSdkUtils.d(b, "WebViewProxy [adjustSize] mNeedRefreshSizeStuts=" + this.p);
        if (1 != this.p) {
            UniSdkUtils.d(b, "WebViewProxy [adjustSize] is always the same, stop adjustSize");
            return;
        }
        this.p = 2;
        this.j = i;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int i2 = displayMetrics.widthPixels;
        int i3 = displayMetrics.heightPixels;
        UniSdkUtils.d(b, "WebViewProxy [adjustSize] width:" + i2 + "height:" + i3);
        String str = b;
        StringBuilder sb = new StringBuilder("WebViewProxy [adjustSize] isFullFit:");
        sb.append(this.l);
        UniSdkUtils.d(str, sb.toString());
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(this.h.getWindow().getAttributes());
        if ("0".equals(this.l)) {
            if (UniSdkUtils.isTablet(this.f)) {
                UniSdkUtils.d(b, "WebViewProxy [adjustSize] UniSdkUtils.isTablet true");
                if (this.f.getResources().getConfiguration().orientation == 2) {
                    layoutParams.width = (int) (i2 * 0.55d);
                    layoutParams.height = (int) (i3 * 0.5d);
                } else if (this.f.getResources().getConfiguration().orientation == 1) {
                    layoutParams.width = (int) (i2 * 0.7d);
                    layoutParams.height = (int) (i3 * 0.4d);
                }
            } else {
                UniSdkUtils.d(b, "WebViewProxy [adjustSize] UniSdkUtils.isTablet false");
                layoutParams.width = (int) (i2 * 0.8d);
                layoutParams.height = (int) (i3 * 0.8d);
            }
        }
        try {
            layoutParams.getClass().getField("layoutInDisplayCutoutMode").setInt(layoutParams, 1);
        } catch (Exception unused) {
        }
        this.h.getWindow().setAttributes(layoutParams);
        UniSdkUtils.d(b, "WebViewProxy [adjustSize] m_webView.requestFocus():".concat(String.valueOf(this.g.requestFocus(130))));
        ProgressDialog progressDialog = this.i;
        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }
        this.i.dismiss();
    }

    public void openWebView(Context context, String str) {
        this.f = context;
        int propInt = SdkMgr.getInst().getPropInt("WEBVIEW_MODE", 0);
        this.q = propInt;
        if (propInt == 1) {
            this.l = SdkMgr.getInst().getPropStr(ConstProp.WEBVIEW_FULLFIT, "0");
            String propStr = SdkMgr.getInst().getPropStr(ConstProp.WEBVIEW_CLBTN, "0");
            String propStr2 = SdkMgr.getInst().getPropStr(ConstProp.WEBVIEW_SUPPORT_BACK_KEY, "0");
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("methodId", "NGWebViewOpenURL");
                jSONObject.put("URLString", str);
                jSONObject.put("intercept_schemes", UniWebView.UNI_JSBRIDGE);
                jSONObject.put("additionalUserAgent", " Unisdk/2.0 NetType/" + NetConnectivity.getNetworkType(this.f));
                if ("1".equals(propStr)) {
                    jSONObject.put("qstn_close_btn", "1");
                }
                if ("1".equals(propStr2)) {
                    jSONObject.put("supportBackKey", "1");
                }
                if ("1".equals(this.l)) {
                    jSONObject.put("isFullScreen", "1");
                } else {
                    DisplayMetrics displayMetrics = this.f.getResources().getDisplayMetrics();
                    int i = displayMetrics.widthPixels;
                    int i2 = displayMetrics.heightPixels;
                    jSONObject.put("origin_x", i / 10);
                    jSONObject.put("origin_y", i2 / 10);
                    jSONObject.put("width", (i * 8) / 10);
                    jSONObject.put("height", (i2 * 8) / 10);
                }
                SdkMgr.getInst().ntExtendFunc(jSONObject.toString());
                return;
            } catch (JSONException e2) {
                e2.printStackTrace();
                return;
            }
        }
        CachedThreadPoolUtil.getInstance().exec(new Runnable() { // from class: com.netease.ntunisdk.base.WebViewProxy.6

            /* renamed from: a */
            final /* synthetic */ Context f1820a;
            final /* synthetic */ String b;

            AnonymousClass6(Context context2, String str2) {
                context = context2;
                str = str2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                WebViewProxy.this.doOpenWebView(context, str);
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.WebViewProxy$6 */
    final class AnonymousClass6 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ Context f1820a;
        final /* synthetic */ String b;

        AnonymousClass6(Context context2, String str2) {
            context = context2;
            str = str2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            WebViewProxy.this.doOpenWebView(context, str);
        }
    }

    public synchronized void onResume() {
        if (e()) {
            UniSdkUtils.d(b, "WebViewProxy [onResume]");
            if (this.h.isShowing()) {
                d();
            }
            this.g.onResume();
        }
    }

    public synchronized void onPause() {
        if (e()) {
            UniSdkUtils.d(b, "WebViewProxy [onPause]");
            this.g.onPause();
        }
    }

    public synchronized void onConfigChange(Configuration configuration) {
        UniSdkUtils.d(b, "WebViewProxy [onConfigChange] start");
        if (e()) {
            UniSdkUtils.d(b, "WebViewProxy [onConfigChange] has isInitialized");
            if (configuration.orientation == 2) {
                UniSdkUtils.d(b, "WebViewProxy [onConfigChange] Configuration.ORIENTATION_LANDSCAPE...");
                if (this.h.isShowing()) {
                    d();
                }
            } else if (configuration.orientation == 1) {
                UniSdkUtils.d(b, "WebViewProxy [onConfigChange] Configuration.ORIENTATION_PORTRAIT...");
                UniSdkUtils.d(b, "WebViewProxy [onConfigChange] mScreenWidthDp=" + this.o + ", config.screenHeightDp=" + configuration.screenHeightDp);
                UniSdkUtils.d(b, "WebViewProxy [onConfigChange] mScreenHeightDp=" + this.n + ", config.screenHeightDp=" + configuration.screenHeightDp);
                if (this.o != configuration.screenHeightDp || this.n != configuration.screenHeightDp) {
                    UniSdkUtils.d(b, "WebViewProxy [onConfigChange] call adjustSize");
                    this.p = 1;
                    d();
                }
            }
        }
        this.o = configuration.screenWidthDp;
        this.n = configuration.screenHeightDp;
        UniSdkUtils.d(b, "WebViewProxy [onConfigChange] reset size, mScreenWidthDp=" + this.o + ", mScreenHeightDp=" + this.n);
    }

    public synchronized void doOpenWebView(Context context, String str) {
        this.r++;
        ((Activity) context).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.WebViewProxy.7

            /* renamed from: a */
            final /* synthetic */ Context f1821a;
            final /* synthetic */ String b;

            AnonymousClass7(Context context2, String str2) {
                context = context2;
                str = str2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                synchronized (WebViewProxy.class) {
                    if (!WebViewProxy.d) {
                        if (!WebViewProxy.this.e()) {
                            WebViewProxy.a(WebViewProxy.this, context);
                        }
                        String strD = str;
                        SdkBase sdkBase = (SdkBase) SdkMgr.getInst();
                        if (!TextUtils.isEmpty(str) && (str.contains("survey.163.com") || str.contains("survey.netease.com") || str.contains("survey.easebar.com") || str.contains("research.163.com") || str.contains("research.easebar.com"))) {
                            strD = WebViewProxy.d(str);
                        }
                        boolean unused = WebViewProxy.d = true;
                        int propInt = sdkBase.getPropInt("webview_post", 0);
                        UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [doOpenWebView] final url=".concat(String.valueOf(strD)));
                        if (propInt != 1) {
                            UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [doOpenWebView] loadUrl");
                            WebViewProxy.this.g.loadUrl(strD);
                        } else {
                            sdkBase.setPropInt("webview_post", 0);
                            UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [doOpenWebView] postUrl");
                            String propStr = sdkBase.getPropStr("webview_body");
                            UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [doOpenWebView] postUrl postBody=".concat(String.valueOf(propStr)));
                            WebViewProxy.this.g.postUrl(strD, propStr.getBytes());
                        }
                        WebViewProxy.this.h.show();
                        return;
                    }
                    UniSdkUtils.e(WebViewProxy.b, "WebViewProxy [doOpenWebView] cannot call openWebView twice at the same time");
                }
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.WebViewProxy$7 */
    final class AnonymousClass7 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ Context f1821a;
        final /* synthetic */ String b;

        AnonymousClass7(Context context2, String str2) {
            context = context2;
            str = str2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            synchronized (WebViewProxy.class) {
                if (!WebViewProxy.d) {
                    if (!WebViewProxy.this.e()) {
                        WebViewProxy.a(WebViewProxy.this, context);
                    }
                    String strD = str;
                    SdkBase sdkBase = (SdkBase) SdkMgr.getInst();
                    if (!TextUtils.isEmpty(str) && (str.contains("survey.163.com") || str.contains("survey.netease.com") || str.contains("survey.easebar.com") || str.contains("research.163.com") || str.contains("research.easebar.com"))) {
                        strD = WebViewProxy.d(str);
                    }
                    boolean unused = WebViewProxy.d = true;
                    int propInt = sdkBase.getPropInt("webview_post", 0);
                    UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [doOpenWebView] final url=".concat(String.valueOf(strD)));
                    if (propInt != 1) {
                        UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [doOpenWebView] loadUrl");
                        WebViewProxy.this.g.loadUrl(strD);
                    } else {
                        sdkBase.setPropInt("webview_post", 0);
                        UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [doOpenWebView] postUrl");
                        String propStr = sdkBase.getPropStr("webview_body");
                        UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [doOpenWebView] postUrl postBody=".concat(String.valueOf(propStr)));
                        WebViewProxy.this.g.postUrl(strD, propStr.getBytes());
                    }
                    WebViewProxy.this.h.show();
                    return;
                }
                UniSdkUtils.e(WebViewProxy.b, "WebViewProxy [doOpenWebView] cannot call openWebView twice at the same time");
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:103:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0095  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x00b6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String d(java.lang.String r9) throws java.io.UnsupportedEncodingException {
        /*
            java.lang.String r0 = "UTF-8"
            java.lang.String r1 = ""
            com.netease.ntunisdk.base.GamerInterface r2 = com.netease.ntunisdk.base.SdkMgr.getInst()     // Catch: java.io.UnsupportedEncodingException -> L34
            java.lang.String r3 = "FULL_UIN"
            java.lang.String r2 = r2.getPropStr(r3, r1)     // Catch: java.io.UnsupportedEncodingException -> L34
            java.lang.String r2 = java.net.URLEncoder.encode(r2, r0)     // Catch: java.io.UnsupportedEncodingException -> L34
            com.netease.ntunisdk.base.GamerInterface r3 = com.netease.ntunisdk.base.SdkMgr.getInst()     // Catch: java.io.UnsupportedEncodingException -> L31
            java.lang.String r4 = "USERINFO_UID"
            java.lang.String r3 = r3.getPropStr(r4, r1)     // Catch: java.io.UnsupportedEncodingException -> L31
            java.lang.String r3 = java.net.URLEncoder.encode(r3, r0)     // Catch: java.io.UnsupportedEncodingException -> L31
            com.netease.ntunisdk.base.GamerInterface r4 = com.netease.ntunisdk.base.SdkMgr.getInst()     // Catch: java.io.UnsupportedEncodingException -> L2f
            java.lang.String r5 = "USERINFO_HOSTID"
            java.lang.String r4 = r4.getPropStr(r5, r1)     // Catch: java.io.UnsupportedEncodingException -> L2f
            java.lang.String r1 = java.net.URLEncoder.encode(r4, r0)     // Catch: java.io.UnsupportedEncodingException -> L2f
            goto L3a
        L2f:
            r0 = move-exception
            goto L37
        L31:
            r0 = move-exception
            r3 = r1
            goto L37
        L34:
            r0 = move-exception
            r2 = r1
            r3 = r2
        L37:
            r0.printStackTrace()
        L3a:
            java.lang.StringBuffer r0 = new java.lang.StringBuffer
            r0.<init>()
            android.net.Uri r4 = android.net.Uri.parse(r9)
            java.util.Set r4 = r4.getQueryParameterNames()
            java.lang.String r5 = "&server="
            java.lang.String r6 = "&role_id="
            java.lang.String r7 = "&uid="
            if (r4 == 0) goto L95
            int r8 = r4.size()
            if (r8 <= 0) goto L95
            java.lang.String r8 = "uid"
            boolean r8 = r4.contains(r8)
            if (r8 != 0) goto L6a
            boolean r8 = android.text.TextUtils.isEmpty(r2)
            if (r8 != 0) goto L6a
            java.lang.StringBuffer r7 = r0.append(r7)
            r7.append(r2)
        L6a:
            java.lang.String r2 = "role_id"
            boolean r2 = r4.contains(r2)
            if (r2 != 0) goto L7f
            boolean r2 = android.text.TextUtils.isEmpty(r3)
            if (r2 != 0) goto L7f
            java.lang.StringBuffer r2 = r0.append(r6)
            r2.append(r3)
        L7f:
            java.lang.String r2 = "server"
            boolean r2 = r4.contains(r2)
            if (r2 != 0) goto Lac
            boolean r2 = android.text.TextUtils.isEmpty(r1)
            if (r2 != 0) goto Lac
            java.lang.StringBuffer r2 = r0.append(r5)
            r2.append(r1)
            goto Lac
        L95:
            java.lang.StringBuffer r4 = r0.append(r7)
            java.lang.StringBuffer r2 = r4.append(r2)
            java.lang.StringBuffer r2 = r2.append(r6)
            java.lang.StringBuffer r2 = r2.append(r3)
            java.lang.StringBuffer r2 = r2.append(r5)
            r2.append(r1)
        Lac:
            java.lang.String r0 = r0.toString()
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            if (r1 != 0) goto L104
            java.lang.String r1 = "?"
            boolean r2 = r9.contains(r1)
            r3 = 1
            if (r2 == 0) goto Lee
            int r1 = r9.indexOf(r1)
            int r2 = r9.length()
            int r2 = r2 - r3
            if (r1 != r2) goto Lde
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r9)
            java.lang.String r9 = r0.substring(r3)
            r1.append(r9)
            java.lang.String r9 = r1.toString()
            goto L104
        Lde:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r9)
            r1.append(r0)
            java.lang.String r9 = r1.toString()
            goto L104
        Lee:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r9)
            r2.append(r1)
            java.lang.String r9 = r0.substring(r3)
            r2.append(r9)
            java.lang.String r9 = r2.toString()
        L104:
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.WebViewProxy.d(java.lang.String):java.lang.String");
    }

    public synchronized void closeWebView() {
        Context context = this.f;
        if (context != null) {
            ((Activity) context).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.WebViewProxy.8
                AnonymousClass8() {
                }

                @Override // java.lang.Runnable
                public final void run() {
                    synchronized (WebViewProxy.class) {
                        if (WebViewProxy.d && WebViewProxy.this.e()) {
                            WebViewProxy.this.g.loadUrl("about:blank");
                            WebViewProxy.this.g.destroy();
                            WebViewProxy.h(WebViewProxy.this);
                            WebViewProxy.this.h.cancel();
                        }
                    }
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.WebViewProxy$8 */
    final class AnonymousClass8 implements Runnable {
        AnonymousClass8() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            synchronized (WebViewProxy.class) {
                if (WebViewProxy.d && WebViewProxy.this.e()) {
                    WebViewProxy.this.g.loadUrl("about:blank");
                    WebViewProxy.this.g.destroy();
                    WebViewProxy.h(WebViewProxy.this);
                    WebViewProxy.this.h.cancel();
                }
            }
        }
    }

    /* renamed from: com.netease.ntunisdk.base.WebViewProxy$9 */
    final class AnonymousClass9 implements Runnable {
        AnonymousClass9() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            synchronized (WebViewProxy.class) {
                if (WebViewProxy.this.e()) {
                    WebViewProxy.this.g.destroy();
                    WebViewProxy.this.h.cancel();
                    WebViewProxy.h(WebViewProxy.this);
                    WebViewProxy.g(WebViewProxy.this);
                }
            }
        }
    }

    public synchronized void disposeWebView() {
        ((Activity) this.f).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.WebViewProxy.9
            AnonymousClass9() {
            }

            @Override // java.lang.Runnable
            public final void run() {
                synchronized (WebViewProxy.class) {
                    if (WebViewProxy.this.e()) {
                        WebViewProxy.this.g.destroy();
                        WebViewProxy.this.h.cancel();
                        WebViewProxy.h(WebViewProxy.this);
                        WebViewProxy.g(WebViewProxy.this);
                    }
                }
            }
        });
    }

    public boolean e() {
        return (this.h == null || this.g == null) ? false : true;
    }

    /* JADX WARN: Removed duplicated region for block: B:49:0x0040  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int b(java.lang.String r3) throws java.lang.Throwable {
        /*
            r0 = 0
            java.net.URL r1 = new java.net.URL     // Catch: java.lang.Throwable -> L2a java.lang.Exception -> L2f
            r1.<init>(r3)     // Catch: java.lang.Throwable -> L2a java.lang.Exception -> L2f
            java.net.URLConnection r3 = r1.openConnection()     // Catch: java.lang.Throwable -> L2a java.lang.Exception -> L2f
            java.net.HttpURLConnection r3 = (java.net.HttpURLConnection) r3     // Catch: java.lang.Throwable -> L2a java.lang.Exception -> L2f
            java.lang.String r0 = "GET"
            r3.setRequestMethod(r0)     // Catch: java.lang.Exception -> L28 java.lang.Throwable -> L3d
            int r0 = com.netease.ntunisdk.base.utils.NetUtil.CONNECTION_TIMEOUT     // Catch: java.lang.Exception -> L28 java.lang.Throwable -> L3d
            r3.setConnectTimeout(r0)     // Catch: java.lang.Exception -> L28 java.lang.Throwable -> L3d
            int r0 = com.netease.ntunisdk.base.utils.NetUtil.SO_TIMEOUT     // Catch: java.lang.Exception -> L28 java.lang.Throwable -> L3d
            r3.setReadTimeout(r0)     // Catch: java.lang.Exception -> L28 java.lang.Throwable -> L3d
            r3.connect()     // Catch: java.lang.Exception -> L28 java.lang.Throwable -> L3d
            int r0 = r3.getResponseCode()     // Catch: java.lang.Exception -> L28 java.lang.Throwable -> L3d
            if (r3 == 0) goto L3c
            r3.disconnect()
            goto L3c
        L28:
            r0 = move-exception
            goto L33
        L2a:
            r3 = move-exception
            r2 = r0
            r0 = r3
            r3 = r2
            goto L3e
        L2f:
            r3 = move-exception
            r2 = r0
            r0 = r3
            r3 = r2
        L33:
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L3d
            if (r3 == 0) goto L3b
            r3.disconnect()
        L3b:
            r0 = -1
        L3c:
            return r0
        L3d:
            r0 = move-exception
        L3e:
            if (r3 == 0) goto L43
            r3.disconnect()
        L43:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.WebViewProxy.b(java.lang.String):int");
    }

    public class CallbackInterface {
        public CallbackInterface() {
        }

        @JavascriptInterface
        public void nativeCall(String str, String str2) throws JSONException {
            UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [CallbackInterface] $CallbackInterface.nativeCall, action:" + str + ", data:" + str2);
            if (str.equals(ControlCmdType.CLOSE)) {
                WebViewProxy.this.closeWebView();
            } else if (str.equals(ClientLogConstant.LOG)) {
                try {
                    JSONObject jSONObject = new JSONObject(str2);
                    UniSdkUtils.i(jSONObject.getString(HeaderParameterNames.AUTHENTICATION_TAG), jSONObject.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (str.equals("toast")) {
                try {
                    Toast.makeText(WebViewProxy.this.f, new JSONObject(str2).getString("message"), 0).show();
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
            } else if (str.equals("echoes")) {
                try {
                    UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [CallbackInterface] echoes callback");
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put("methodId", "ntOpenEchoes");
                    jSONObject2.put("result", "0");
                    ((SdkBase) SdkMgr.getInst()).extendFuncCall(jSONObject2.toString());
                    String string = new JSONObject(str2).getString("message");
                    UniSdkUtils.d(WebViewProxy.b, "WebViewProxy [CallbackInterface]  echoes message=".concat(String.valueOf(string)));
                    SDKEchoes.getInstance().setmFeedbackMsg(string);
                    SDKEchoes.getInstance().echo2SA();
                } catch (JSONException e3) {
                    UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [CallbackInterface]  echoes JSONException=".concat(String.valueOf(e3)));
                    e3.printStackTrace();
                }
            }
            if (WebViewProxy.e != null) {
                SdkBase sdkBase = (SdkBase) SdkMgr.getInst();
                if (sdkBase.getPropInt(ConstProp.WEBVIEW_CALLER_THREAD, 1) == 2) {
                    sdkBase.runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.WebViewProxy.CallbackInterface.1

                        /* renamed from: a */
                        final /* synthetic */ String f1825a;
                        final /* synthetic */ String b;

                        AnonymousClass1(String str3, String str22) {
                            str = str3;
                            str = str22;
                        }

                        @Override // java.lang.Runnable
                        public final void run() {
                            UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [CallbackInterface]  OnWebViewListener.OnAction, current thread=" + Thread.currentThread().getId());
                            WebViewProxy.e.OnWebViewNativeCall(str, str);
                        }
                    });
                } else {
                    ((Activity) WebViewProxy.this.f).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.WebViewProxy.CallbackInterface.2

                        /* renamed from: a */
                        final /* synthetic */ String f1826a;
                        final /* synthetic */ String b;

                        AnonymousClass2(String str3, String str22) {
                            str = str3;
                            str = str22;
                        }

                        @Override // java.lang.Runnable
                        public final void run() {
                            UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [CallbackInterface] OnWebViewListener.OnAction, current thread=" + Thread.currentThread().getId());
                            WebViewProxy.e.OnWebViewNativeCall(str, str);
                        }
                    });
                }
            }
        }

        /* renamed from: com.netease.ntunisdk.base.WebViewProxy$CallbackInterface$1 */
        final class AnonymousClass1 implements Runnable {

            /* renamed from: a */
            final /* synthetic */ String f1825a;
            final /* synthetic */ String b;

            AnonymousClass1(String str3, String str22) {
                str = str3;
                str = str22;
            }

            @Override // java.lang.Runnable
            public final void run() {
                UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [CallbackInterface]  OnWebViewListener.OnAction, current thread=" + Thread.currentThread().getId());
                WebViewProxy.e.OnWebViewNativeCall(str, str);
            }
        }

        /* renamed from: com.netease.ntunisdk.base.WebViewProxy$CallbackInterface$2 */
        final class AnonymousClass2 implements Runnable {

            /* renamed from: a */
            final /* synthetic */ String f1826a;
            final /* synthetic */ String b;

            AnonymousClass2(String str3, String str22) {
                str = str3;
                str = str22;
            }

            @Override // java.lang.Runnable
            public final void run() {
                UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [CallbackInterface] OnWebViewListener.OnAction, current thread=" + Thread.currentThread().getId());
                WebViewProxy.e.OnWebViewNativeCall(str, str);
            }
        }
    }

    static /* synthetic */ void a(WebViewProxy webViewProxy, Context context) throws IllegalAccessException, IllegalArgumentException {
        webViewProxy.m = new RelativeLayout(context);
        webViewProxy.g = new WebView(context);
        if (Build.VERSION.SDK_INT >= 29) {
            UniSdkUtils.i(b, "WebViewProxy [setForceDarkAllowed]");
            webViewProxy.g.setForceDarkAllowed(false);
        }
        webViewProxy.l = SdkMgr.getInst().getPropStr(ConstProp.WEBVIEW_FULLFIT, "0");
        if ("1".equals(SdkMgr.getInst().getPropStr(ConstProp.WEBVIEW_BKCOLOR))) {
            webViewProxy.g.setBackgroundColor(0);
        }
        WebSettings settings = webViewProxy.g.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(2);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setUseWideViewPort(false);
        settings.setAllowFileAccess(false);
        settings.setAllowFileAccessFromFileURLs(false);
        settings.setAllowUniversalAccessFromFileURLs(false);
        settings.setJavaScriptEnabled(true);
        settings.setLightTouchEnabled(true);
        settings.setUserAgentString(settings.getUserAgentString() + (" Unisdk/1.1 NetType/" + NetConnectivity.getNetworkType(webViewProxy.f)));
        UniSdkUtils.i(b, "WebViewProxy [init] User Agent:" + settings.getUserAgentString());
        if (webViewProxy.i == null) {
            int identifier = context.getResources().getIdentifier("unisdk_webview_progressdialog", ResIdReader.RES_TYPE_LAYOUT, context.getPackageName());
            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.show();
            String propStr = SdkMgr.getInst().getPropStr(ConstProp.WEBVIEW_SUPPORT_BACK_KEY, "0");
            UniSdkUtils.d(b, "WebViewProxy [checkStatus] isSupportBackKey:".concat(String.valueOf(propStr)));
            progressDialog.setCancelable(false);
            if ("1".equals(propStr)) {
                progressDialog.setCancelable(true);
            }
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            progressDialog.setContentView(identifier);
            webViewProxy.i = progressDialog;
        }
        webViewProxy.g.setWebViewClient(new WebViewClient() { // from class: com.netease.ntunisdk.base.WebViewProxy.2
            AnonymousClass2() {
            }

            @Override // android.webkit.WebViewClient
            public final boolean shouldOverrideUrlLoading(WebView webView, String str2) throws JSONException {
                UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [init] [shouldOverrideUrlLoading] url=".concat(String.valueOf(str2)));
                if (str2.startsWith("weixin://")) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.setData(Uri.parse(str2));
                    try {
                        HookManager.startActivity(WebViewProxy.this.f, intent);
                    } catch (Exception unused) {
                        ((Activity) WebViewProxy.this.f).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.WebViewProxy.2.1
                            AnonymousClass1() {
                            }

                            @Override // java.lang.Runnable
                            public final void run() {
                                new AlertDialog.Builder(WebViewProxy.this.f).setMessage("\u672a\u68c0\u6d4b\u5230\u5ba2\u6237\u7aef\uff0c\u8bf7\u5b89\u88c5\u540e\u91cd\u8bd5\u3002").setPositiveButton(CommonTips.OK_BTN, new DialogInterface.OnClickListener() { // from class: com.netease.ntunisdk.base.WebViewProxy.2.1.1
                                    @Override // android.content.DialogInterface.OnClickListener
                                    public final void onClick(DialogInterface dialogInterface, int i) {
                                    }

                                    DialogInterfaceOnClickListenerC00711() {
                                    }
                                }).setNegativeButton("\u53d6\u6d88", (DialogInterface.OnClickListener) null).show();
                            }

                            /* renamed from: com.netease.ntunisdk.base.WebViewProxy$2$1$1 */
                            final class DialogInterfaceOnClickListenerC00711 implements DialogInterface.OnClickListener {
                                @Override // android.content.DialogInterface.OnClickListener
                                public final void onClick(DialogInterface dialogInterface, int i) {
                                }

                                DialogInterfaceOnClickListenerC00711() {
                                }
                            }
                        });
                    }
                    return true;
                }
                if (str2.startsWith("alipays://")) {
                    Intent intent2 = new Intent();
                    intent2.setAction("android.intent.action.VIEW");
                    intent2.setData(Uri.parse(str2));
                    try {
                        HookManager.startActivity(WebViewProxy.this.f, intent2);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [init] [shouldOverrideUrlLoading] Exception=" + e2.toString());
                        if (WebViewProxy.this.i != null && WebViewProxy.this.i.isShowing()) {
                            WebViewProxy.this.i.dismiss();
                        }
                    }
                    return true;
                }
                if (str2.contains("http://") || str2.contains("https://") || !str2.contains("://")) {
                    UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [init] [shouldOverrideUrlLoading] \u5c5e\u4e8ehttp/https\u534f\u8bae");
                    CachedThreadPoolUtil.getInstance().exec(new Runnable() { // from class: com.netease.ntunisdk.base.WebViewProxy.2.2

                        /* renamed from: a */
                        final /* synthetic */ String f1815a;

                        RunnableC00722(String str22) {
                            str = str22;
                        }

                        @Override // java.lang.Runnable
                        public final void run() throws Throwable {
                            int iB = WebViewProxy.b(str);
                            UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [init] [shouldOverrideUrlLoading] status=".concat(String.valueOf(iB)));
                            if (WebViewProxy.a(iB)) {
                                return;
                            }
                            UniSdkUtils.e(WebViewProxy.b, "WebViewProxy [init] [shouldOverrideUrlLoading] fail to open url:" + str + ", status:" + iB);
                            WebViewProxy.this.new CallbackInterface().nativeCall(ControlCmdType.CLOSE, "error");
                        }
                    });
                    return false;
                }
                UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [init] [shouldOverrideUrlLoading] \u5c5e\u4e8e\u81ea\u5b9a\u4e49\u534f\u8bae");
                String propStr2 = SdkMgr.getInst().getPropStr(ConstProp.WEBVIEW_UNKNOWN_PROTOCOL_HANDLE_MODE, "1");
                UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [init] [shouldOverrideUrlLoading] unknownProtocolHandleMode=".concat(String.valueOf(propStr2)));
                if (propStr2.equals("1")) {
                    Intent intent3 = new Intent();
                    intent3.setAction("android.intent.action.VIEW");
                    intent3.setData(Uri.parse(str22));
                    try {
                        HookManager.startActivity(WebViewProxy.this.f, intent3);
                    } catch (Exception e22) {
                        e22.printStackTrace();
                        UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [init] [shouldOverrideUrlLoading] Exception=" + e22.toString());
                        if (WebViewProxy.this.i != null && WebViewProxy.this.i.isShowing()) {
                            WebViewProxy.this.i.dismiss();
                        }
                    }
                } else if (propStr2.equals("0")) {
                    WebViewProxy.this.new CallbackInterface().nativeCall(ControlCmdType.CLOSE, "error");
                }
                return true;
            }

            /* renamed from: com.netease.ntunisdk.base.WebViewProxy$2$1 */
            final class AnonymousClass1 implements Runnable {
                AnonymousClass1() {
                }

                @Override // java.lang.Runnable
                public final void run() {
                    new AlertDialog.Builder(WebViewProxy.this.f).setMessage("\u672a\u68c0\u6d4b\u5230\u5ba2\u6237\u7aef\uff0c\u8bf7\u5b89\u88c5\u540e\u91cd\u8bd5\u3002").setPositiveButton(CommonTips.OK_BTN, new DialogInterface.OnClickListener() { // from class: com.netease.ntunisdk.base.WebViewProxy.2.1.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                        }

                        DialogInterfaceOnClickListenerC00711() {
                        }
                    }).setNegativeButton("\u53d6\u6d88", (DialogInterface.OnClickListener) null).show();
                }

                /* renamed from: com.netease.ntunisdk.base.WebViewProxy$2$1$1 */
                final class DialogInterfaceOnClickListenerC00711 implements DialogInterface.OnClickListener {
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                    }

                    DialogInterfaceOnClickListenerC00711() {
                    }
                }
            }

            /* renamed from: com.netease.ntunisdk.base.WebViewProxy$2$2 */
            final class RunnableC00722 implements Runnable {

                /* renamed from: a */
                final /* synthetic */ String f1815a;

                RunnableC00722(String str22) {
                    str = str22;
                }

                @Override // java.lang.Runnable
                public final void run() throws Throwable {
                    int iB = WebViewProxy.b(str);
                    UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [init] [shouldOverrideUrlLoading] status=".concat(String.valueOf(iB)));
                    if (WebViewProxy.a(iB)) {
                        return;
                    }
                    UniSdkUtils.e(WebViewProxy.b, "WebViewProxy [init] [shouldOverrideUrlLoading] fail to open url:" + str + ", status:" + iB);
                    WebViewProxy.this.new CallbackInterface().nativeCall(ControlCmdType.CLOSE, "error");
                }
            }

            @Override // android.webkit.WebViewClient
            public final void onReceivedError(WebView webView, int i, String str, String str2) throws JSONException {
                UniSdkUtils.e(WebViewProxy.b, "WebViewProxy [init] [onReceivedError], errorCode: " + str2 + ", description: " + str + ", failingUrl: " + str2);
                WebViewProxy.this.new CallbackInterface().nativeCall(ControlCmdType.CLOSE, "error");
            }

            @Override // android.webkit.WebViewClient
            public final void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [init] [onPageStarted]");
                WebViewProxy.this.i.show();
                CachedThreadPoolUtil.getInstance().exec(new Runnable() { // from class: com.netease.ntunisdk.base.WebViewProxy.2.3
                    AnonymousClass3() {
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        Message message = new Message();
                        message.arg1 = WebViewProxy.this.r;
                        message.what = 1;
                        WebViewProxy.this.f1810a.sendMessageDelayed(message, 6000L);
                    }
                });
            }

            /* renamed from: com.netease.ntunisdk.base.WebViewProxy$2$3 */
            final class AnonymousClass3 implements Runnable {
                AnonymousClass3() {
                }

                @Override // java.lang.Runnable
                public final void run() {
                    Message message = new Message();
                    message.arg1 = WebViewProxy.this.r;
                    message.what = 1;
                    WebViewProxy.this.f1810a.sendMessageDelayed(message, 6000L);
                }
            }

            @Override // android.webkit.WebViewClient
            public final void onPageFinished(WebView webView, String str) {
                if (WebViewProxy.this.i == null || !WebViewProxy.this.i.isShowing()) {
                    return;
                }
                WebViewProxy.this.i.dismiss();
            }
        });
        webViewProxy.g.setWebChromeClient(new WebChromeClient() { // from class: com.netease.ntunisdk.base.WebViewProxy.3
            AnonymousClass3() {
            }

            @Override // android.webkit.WebChromeClient
            public final boolean onJsAlert(WebView webView, String str, String str2, JsResult jsResult) {
                return super.onJsAlert(webView, str, str2, jsResult);
            }
        });
        webViewProxy.g.addJavascriptInterface(webViewProxy.new CallbackInterface(), "$CallbackInterface");
        webViewProxy.m.addView(webViewProxy.g, new RelativeLayout.LayoutParams(-1, -1));
        String propStr2 = SdkMgr.getInst().getPropStr(ConstProp.WEBVIEW_CLBTN, "0");
        UniSdkUtils.d(b, "WebViewProxy [init] isShowClostBtn=".concat(String.valueOf(propStr2)));
        if ("1".equals(propStr2)) {
            Button button = new Button(context);
            webViewProxy.k = button;
            button.setOnClickListener(new View.OnClickListener() { // from class: com.netease.ntunisdk.base.WebViewProxy.4
                AnonymousClass4() {
                }

                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    if (WebViewProxy.this.h == null || !WebViewProxy.this.h.isShowing()) {
                        return;
                    }
                    SdkMgr.getInst().setPropStr(ConstProp.WEBVIEW_FULLFIT, "0");
                    SdkMgr.getInst().setPropStr(ConstProp.WEBVIEW_CLBTN, "0");
                    WebViewProxy.this.closeWebView();
                }
            });
            int i = (int) ((context.getResources().getDisplayMetrics().density * 34.0f) + 0.5f);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(i, i);
            layoutParams.addRule(11);
            webViewProxy.k.setLayoutParams(layoutParams);
            webViewProxy.k.setBackgroundResource(context.getResources().getIdentifier("unisdk_webview_close", ResIdReader.RES_TYPE_DRAWABLE, context.getPackageName()));
            webViewProxy.m.addView(webViewProxy.k);
        }
        UniSdkUtils.d(b, "WebViewProxy [init] isFullFit:" + webViewProxy.l);
        if ("0".equals(webViewProxy.l)) {
            webViewProxy.h = new Dialog(context, context.getResources().getIdentifier("unisdk_webview_dialog", ResIdReader.RES_TYPE_STYLE, context.getPackageName()));
        } else {
            webViewProxy.h = new Dialog(context, R.style.Theme.Black.NoTitleBar.Fullscreen);
        }
        webViewProxy.m.setSystemUiVisibility(1536);
        webViewProxy.h.setContentView(webViewProxy.m);
        String propStr3 = SdkMgr.getInst().getPropStr(ConstProp.WEBVIEW_SUPPORT_BACK_KEY, "0");
        UniSdkUtils.d(b, "WebViewProxy [init] isSupportBackKey:".concat(String.valueOf(propStr3)));
        webViewProxy.h.setCancelable(false);
        if ("1".equals(propStr3)) {
            webViewProxy.h.setCancelable(true);
        }
        webViewProxy.h.setCanceledOnTouchOutside(false);
        webViewProxy.d();
        webViewProxy.h.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.netease.ntunisdk.base.WebViewProxy.5
            AnonymousClass5() {
            }

            @Override // android.content.DialogInterface.OnCancelListener
            public final void onCancel(DialogInterface dialogInterface) throws JSONException {
                boolean unused = WebViewProxy.d = false;
                WebViewProxy.g(WebViewProxy.this);
                WebViewProxy.h(WebViewProxy.this);
                WebViewProxy.i(WebViewProxy.this);
                try {
                    UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [init] [onCancel] [closeWebView] callback start");
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("methodId", "NtCloseWebView");
                    jSONObject.put("result", "0");
                    ((SdkBase) SdkMgr.getInst()).extendFuncCall(jSONObject.toString());
                } catch (Exception e2) {
                    UniSdkUtils.i(WebViewProxy.b, "WebViewProxy [init] [onCancel] [closeWebView] callback Exception=" + e2.toString());
                    e2.printStackTrace();
                }
            }
        });
    }
}