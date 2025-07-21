package com.netease.ntunisdk.external.protocol.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.netease.ntunisdk.external.protocol.Const;
import com.netease.ntunisdk.external.protocol.data.SDKRuntime;
import com.netease.ntunisdk.external.protocol.utils.L;
import com.netease.ntunisdk.external.protocol.view.WebViewBridge;
import com.xiaomi.gamecenter.sdk.utils.RSASignature;
import com.xiaomi.gamecenter.sdk.web.webview.webkit.h;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Marker;

/* loaded from: classes.dex */
public class UniWebView extends WebView {
    public static final String CB_ACTION = "func";
    public static final String CB_NATIVE2H5 = "callback";
    public static final String CB_PARAMS = "params";
    private static final String NTWKJSBridgeJS = "ProtocolJSBridge.js";
    private static final String TAG = "Protocol UniWebView";
    public static final String URLPROXY_PREFIX = "nativerequest/";
    protected boolean isHTMLProtocol;
    private boolean isNeedShowButton;
    private boolean isSupportClearFocus;
    private String mBaseUrl;
    private String mErrorUrl;
    private OnShowListener mOnShowListener;
    private OnUrlLoadingListener mOnUrlLoadingListener;
    private Timer mTimer;
    private WebViewBridge mWebViewBridge;
    private final Context myCtx;

    public interface OnShowListener {
        void onShow();
    }

    public interface OnUrlLoadingListener {
        boolean shouldOverrideUrlLoading(String str);
    }

    public UniWebView(Context context) {
        super(context);
        this.isHTMLProtocol = false;
        this.isNeedShowButton = true;
        this.isSupportClearFocus = false;
        this.myCtx = context;
    }

    public UniWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.isHTMLProtocol = false;
        this.isNeedShowButton = true;
        this.isSupportClearFocus = false;
        this.myCtx = context;
    }

    public UniWebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.isHTMLProtocol = false;
        this.isNeedShowButton = true;
        this.isSupportClearFocus = false;
        this.myCtx = context;
    }

    public static void preload(Context context) {
        Config.getInstance().getLocalJS(context);
    }

    public static Config getConfig() {
        return Config.getInstance();
    }

    public void setNeedShowButton(boolean z) {
        this.isNeedShowButton = z;
    }

    public void setOnShowListener(OnShowListener onShowListener) {
        this.mOnShowListener = onShowListener;
    }

    public void setHTMLProtocol(boolean z) {
        this.isHTMLProtocol = z;
    }

    public void setOnUrlLoadingListener(OnUrlLoadingListener onUrlLoadingListener) {
        this.mOnUrlLoadingListener = onUrlLoadingListener;
    }

    @Override // android.webkit.WebView
    public void onResume() {
        if (Build.VERSION.SDK_INT >= 11) {
            super.onResume();
        }
        handleNativeNotify("{\"name\": \"onScreenUnlock\"}");
    }

    public void onStop() {
        handleNativeNotify("{\"name\": \"onScreenLock\"}");
    }

    public void handleNativeNotify(String str) {
        String str2 = "javascript:window.handleNativeNotify&&window.handleNativeNotify(" + str + ")";
        L.d(TAG, "handleNativeNotify js\uff1a" + str2);
        if (Build.VERSION.SDK_INT >= 19) {
            super.evaluateJavascript(str2, (ValueCallback<String>) null);
        } else {
            super.loadUrl(str2);
        }
    }

    public void initWebView() {
        Config.getInstance().getLocalJS(this.myCtx);
        this.mWebViewBridge = new WebViewBridge();
        setWebViewSetting();
        setDrawingCacheEnabled(false);
        setVerticalScrollBarEnabled(false);
        setWebViewClient(new WebViewClient() { // from class: com.netease.ntunisdk.external.protocol.view.UniWebView.1
            @Override // android.webkit.WebViewClient
            public void onPageCommitVisible(WebView webView, String str) {
                super.onPageCommitVisible(webView, str);
                L.d(UniWebView.TAG, "onPageCommitVisible url:" + str);
                webView.setVisibility(0);
            }

            @Override // android.webkit.WebViewClient
            public WebResourceResponse shouldInterceptRequest(WebView webView, String str) throws JSONException {
                L.d(UniWebView.TAG, "shouldInterceptRequest url:" + str);
                if (!UniWebView.this.isSupportClearFocus) {
                    webView.requestFocus();
                }
                UniWebView.this.mErrorUrl = null;
                String str2 = "nativerequest/";
                if (str.startsWith("http://")) {
                    str2 = "http://nativerequest/";
                } else if (str.startsWith("https://")) {
                    str2 = "https://nativerequest/";
                }
                if (str.startsWith(str2)) {
                    L.d(UniWebView.TAG, "WebResourceResponse url:" + str);
                    String strDecode = URLDecoder.decode(str.substring(str2.length()));
                    L.d(UniWebView.TAG, "decodeUrl url:" + str);
                    new JSONObject();
                    try {
                        JSONObject jSONObject = new JSONObject(strDecode);
                        String string = jSONObject.getString("method");
                        String string2 = jSONObject.getString("url");
                        String string3 = jSONObject.getString("mimeType");
                        InputStream inputStreamHttpGet = UniWebView.this.httpGet(string2, string, jSONObject.optJSONObject("data"));
                        if (inputStreamHttpGet != null && Build.VERSION.SDK_INT >= 21) {
                            WebResourceResponse webResourceResponse = new WebResourceResponse(string3, RSASignature.c, inputStreamHttpGet);
                            Map<String, String> responseHeaders = webResourceResponse.getResponseHeaders();
                            if (responseHeaders == null) {
                                responseHeaders = new HashMap<>();
                            }
                            responseHeaders.put("Access-Control-Allow-Origin", Marker.ANY_MARKER);
                            webResourceResponse.setResponseHeaders(responseHeaders);
                            return webResourceResponse;
                        }
                    } catch (Exception e) {
                        L.e(UniWebView.TAG, "shouldInterceptRequest exception:" + e.getMessage());
                    }
                }
                L.d(UniWebView.TAG, "shouldInterceptRequest load:" + str);
                return super.shouldInterceptRequest(webView, str);
            }

            @Override // android.webkit.WebViewClient
            public void onPageFinished(WebView webView, String str) {
                super.onPageFinished(webView, str);
                UniWebView.this.openHardWare();
                UniWebView.this.resetTimer();
                if (!UniWebView.this.isSupportClearFocus) {
                    webView.requestFocus();
                }
                L.d(UniWebView.TAG, "onPageFinished:" + str);
                if (UniWebView.this.mErrorUrl == null || !UniWebView.this.mErrorUrl.equals(str)) {
                    if (UniWebView.this.mOnShowListener != null) {
                        UniWebView.this.mOnShowListener.onShow();
                    }
                    StringBuilder sb = new StringBuilder();
                    if (Config.getInstance().loadLocalJS) {
                        String localJS = Config.getInstance().getLocalJS(UniWebView.this.myCtx);
                        if (TextUtils.isEmpty(localJS)) {
                            return;
                        }
                        sb.append(localJS);
                        if (!UniWebView.this.isSupportClearFocus) {
                            sb.append("\n");
                            sb.append(Const.HTML_FOCUS_SCRIPT);
                        }
                    }
                    int iMin = UniWebView.this.isHTMLProtocol ? Math.min(SDKRuntime.getInstance().getZoomSize(), 2) : SDKRuntime.getInstance().getZoomSize();
                    if (iMin > 1) {
                        sb.append("\n");
                        sb.append(String.format(Const.HTML_ZOOM_SCRIPT, Integer.valueOf(iMin)));
                    }
                    if (TextUtils.isEmpty(sb.toString())) {
                        return;
                    }
                    if (Build.VERSION.SDK_INT < 21) {
                        L.d("load js", sb.toString());
                        UniWebView.this.loadUrl("javascript:" + ((Object) sb));
                        return;
                    }
                    L.d("load js", sb.toString());
                    UniWebView.this.evaluateJavascript(sb.toString(), new ValueCallback<String>() { // from class: com.netease.ntunisdk.external.protocol.view.UniWebView.1.2
                        @Override // android.webkit.ValueCallback
                        public void onReceiveValue(String str2) {
                            L.d("onReceiveValue", str2);
                            if (UniWebView.this.isSupportClearFocus) {
                                return;
                            }
                            UniWebView.this.requestFocus();
                        }
                    });
                    return;
                }
                UniWebView.this.postDelayed(new Runnable() { // from class: com.netease.ntunisdk.external.protocol.view.UniWebView.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (UniWebView.this.canGoBack()) {
                            UniWebView.this.goBack();
                        } else {
                            UniWebView.this.clearHistory();
                            UniWebView.this.loadUrl(UniWebView.this.mBaseUrl);
                        }
                    }
                }, 200L);
            }

            @Override // android.webkit.WebViewClient
            public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                super.onPageStarted(webView, str, bitmap);
                UniWebView.this.closeHardWare();
                L.d(UniWebView.TAG, "onPageStarted:" + str);
                UniWebView.this.resetTimer();
                UniWebView.this.loadingAlarm(str);
                if (UniWebView.this.isSupportClearFocus) {
                    return;
                }
                webView.requestFocus();
            }

            @Override // android.webkit.WebViewClient
            public void onReceivedError(WebView webView, int i, String str, String str2) throws JSONException {
                super.onReceivedError(webView, i, str, str2);
                L.d(UniWebView.TAG, "onReceivedError# errorCode:" + i + ",description:" + str + ",failingUrl:" + str2);
                if (str2.endsWith(".ico") || str2.endsWith("css") || str2.endsWith("png") || str2.endsWith("jpg")) {
                    return;
                }
                try {
                    if (UniWebView.this.mErrorUrl == null || !UniWebView.this.mErrorUrl.equals(str2)) {
                        UniWebView.this.stopLoading();
                        UniWebView.this.resetTimer();
                        UniWebView.this.mErrorUrl = str2;
                        JSONObject jSONObject = new JSONObject();
                        L.d(UniWebView.TAG, "errorCode:" + i + ",description:" + str + ",failingUrl:" + str2);
                        jSONObject.put(Const.WEB_URL, str2);
                        UniWebView.this.runCallback(jSONObject, Const.ON_PAGE_ERROR);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override // android.webkit.WebViewClient
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) throws JSONException {
                super.onReceivedError(webView, webResourceRequest, webResourceError);
                String string = "";
                if (webResourceRequest != null && webResourceRequest.getUrl() != null) {
                    string = webResourceRequest.getUrl().toString();
                }
                if (string.endsWith(".ico") || string.endsWith("css") || string.endsWith("png") || string.endsWith("jpg")) {
                    return;
                }
                try {
                    if (UniWebView.this.mErrorUrl == null || !UniWebView.this.mErrorUrl.equals(string)) {
                        UniWebView.this.stopLoading();
                        UniWebView.this.resetTimer();
                        JSONObject jSONObject = new JSONObject();
                        L.d(UniWebView.TAG, "onReceivedError2# errorCode:" + webResourceError.getErrorCode() + ",description:" + ((Object) webResourceError.getDescription()) + ",failingUrl:" + webResourceRequest.getUrl());
                        jSONObject.put(Const.WEB_URL, webResourceRequest.getUrl());
                        UniWebView.this.runCallback(jSONObject, Const.ON_PAGE_ERROR);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override // android.webkit.WebViewClient
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) throws JSONException {
                super.onReceivedSslError(webView, sslErrorHandler, sslError);
                String url = sslError.getUrl();
                L.d(UniWebView.TAG, "onReceivedSslError,description:" + sslError + ",failingUrl:" + url);
                sslErrorHandler.cancel();
                try {
                    if (TextUtils.equals(url, UniWebView.this.mBaseUrl)) {
                        if (UniWebView.this.mErrorUrl == null || !UniWebView.this.mErrorUrl.equals(url)) {
                            UniWebView.this.stopLoading();
                            UniWebView.this.resetTimer();
                            JSONObject jSONObject = new JSONObject();
                            jSONObject.put(Const.WEB_URL, url);
                            UniWebView.this.runCallback(jSONObject, Const.ON_PAGE_ERROR);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override // android.webkit.WebViewClient
            public void onReceivedHttpError(WebView webView, WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) throws JSONException {
                int statusCode = webResourceResponse != null ? webResourceResponse.getStatusCode() : 404;
                String string = "";
                if (webResourceRequest != null && webResourceRequest.getUrl() != null) {
                    string = webResourceRequest.getUrl().toString();
                }
                if (string.endsWith(".ico") || string.endsWith("css") || string.endsWith("png") || string.endsWith("jpg")) {
                    return;
                }
                if (404 == statusCode || 500 == statusCode) {
                    try {
                        if (UniWebView.this.mErrorUrl == null || !UniWebView.this.mErrorUrl.equals(string)) {
                            UniWebView.this.stopLoading();
                            UniWebView.this.resetTimer();
                            JSONObject jSONObject = new JSONObject();
                            L.d(UniWebView.TAG, "onReceivedHttpError code = " + statusCode + ",failingUrl:" + string);
                            jSONObject.put(Const.WEB_URL, string);
                            UniWebView.this.runCallback(jSONObject, Const.ON_PAGE_ERROR);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                if (str == null || UniWebView.this.mOnUrlLoadingListener == null || !UniWebView.this.mOnUrlLoadingListener.shouldOverrideUrlLoading(str)) {
                    return super.shouldOverrideUrlLoading(webView, str);
                }
                return true;
            }
        });
        setWebChromeClient(new WebChromeClient() { // from class: com.netease.ntunisdk.external.protocol.view.UniWebView.2
            @Override // android.webkit.WebChromeClient
            public void onShowCustomView(View view, WebChromeClient.CustomViewCallback customViewCallback) {
                L.d(UniWebView.TAG, " onShowCustomView");
                super.onShowCustomView(view, customViewCallback);
            }

            @Override // android.webkit.WebChromeClient
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                L.d(UniWebView.TAG, " onProgressChanged:" + i);
            }

            @Override // android.webkit.WebChromeClient
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                L.d(UniWebView.TAG, consoleMessage.message());
                return super.onConsoleMessage(consoleMessage);
            }

            @Override // android.webkit.WebChromeClient
            public void onConsoleMessage(String str, int i, String str2) {
                L.d(UniWebView.TAG, str);
                super.onConsoleMessage(str, i, str2);
            }
        });
        setBackgroundColor(Color.parseColor("#00000000"));
        closeHardWare();
        clearFocus();
        this.mWebViewBridge.init(this);
    }

    public void setSupportClearFocus(boolean z) {
        this.isSupportClearFocus = z;
    }

    @Override // android.webkit.WebView, android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        L.d("event:" + keyEvent.getKeyCode() + ",action:" + keyEvent.getAction());
        if (this.isSupportClearFocus && keyEvent.getKeyCode() == 22) {
            clearFocus();
            return false;
        }
        return super.dispatchKeyEvent(keyEvent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeHardWare() {
        if (Build.VERSION.SDK_INT >= 27) {
            setLayerType(1, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openHardWare() {
        if (Build.VERSION.SDK_INT >= 27) {
            setLayerType(2, null);
        }
    }

    private void setWebViewSetting() {
        WebSettings settings = getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(Config.getInstance().isUseWideViewPort());
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(0);
        settings.setAllowFileAccess(false);
        if (Build.VERSION.SDK_INT >= 16) {
            settings.setAllowFileAccessFromFileURLs(false);
            settings.setAllowUniversalAccessFromFileURLs(false);
        }
        setUserAgent(settings);
        if (Build.VERSION.SDK_INT >= 17) {
            settings.setMediaPlaybackRequiresUserGesture(false);
        }
        try {
            settings.setPluginState(WebSettings.PluginState.ON);
            if (Build.VERSION.SDK_INT >= 21 || Build.VERSION.SDK_INT >= 21) {
                settings.setMixedContentMode(0);
            }
        } catch (Throwable unused) {
        }
        if (Build.VERSION.SDK_INT < 17) {
            removeJavascriptInterface("searchBoxJavaBridge_");
            removeJavascriptInterface("accessibility");
            removeJavascriptInterface("accessibilityTraversal");
        }
    }

    private void setUserAgent(WebSettings webSettings) {
        String userAgentString = webSettings.getUserAgentString();
        if (this.isNeedShowButton) {
            StringBuilder sb = new StringBuilder();
            if (userAgentString == null) {
                userAgentString = "";
            }
            sb.append(userAgentString);
            sb.append(" Unisdk/Mobile");
            webSettings.setUserAgentString(sb.toString());
        }
        try {
            L.d(TAG, "user_agent:" + webSettings.getUserAgentString());
        } catch (Throwable unused) {
        }
    }

    private Activity getActivityFromView(View view) {
        for (Context context = view.getContext(); context instanceof ContextWrapper; context = ((ContextWrapper) context).getBaseContext()) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
        }
        return null;
    }

    public void evaluateJavascript(String str, String str2) {
        L.d(TAG, "evaluateJavascript method:" + str + ",params:" + str2);
        if (!TextUtils.isEmpty(str)) {
            String str3 = "javascript:" + str + "()";
            if (!TextUtils.isEmpty(str2)) {
                str3 = "javascript:" + str + "(" + str2 + ")";
            }
            if (Build.VERSION.SDK_INT >= 19) {
                super.evaluateJavascript(str3, new ValueCallback<String>() { // from class: com.netease.ntunisdk.external.protocol.view.UniWebView.3
                    @Override // android.webkit.ValueCallback
                    public void onReceiveValue(String str4) {
                        L.d(UniWebView.TAG, "value:" + str4);
                    }
                });
                return;
            } else {
                super.loadUrl(str3);
                return;
            }
        }
        L.e(TAG, "evaluateJavascript method null");
    }

    public void setWebBridgeCallback(WebViewBridge.Callback callback) {
        WebViewBridge webViewBridge = this.mWebViewBridge;
        if (webViewBridge != null) {
            webViewBridge.addWebViewCallback(callback);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runCallback(JSONObject jSONObject, String str) {
        WebViewBridge webViewBridge = this.mWebViewBridge;
        if (webViewBridge != null) {
            webViewBridge.getDefaultCallback().callback(jSONObject, str);
        } else {
            L.e(TAG, "UniWebViewCallback null");
        }
    }

    public InputStream httpGet(String str, String str2, JSONObject jSONObject) throws IOException {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setInstanceFollowRedirects(true);
            httpURLConnection.setRequestProperty("accept", "*/*");
            httpURLConnection.setRequestProperty("connection", "Keep-Alive");
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setReadTimeout(30000);
            if (h.c.equalsIgnoreCase(str2)) {
                httpURLConnection.setRequestMethod("GET");
            } else {
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                if (jSONObject != null && !TextUtils.isEmpty(jSONObject.toString())) {
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    outputStream.write(jSONObject.toString().getBytes("UTF-8"));
                    outputStream.flush();
                    outputStream.close();
                }
            }
            httpURLConnection.connect();
            return httpURLConnection.getInputStream();
        } catch (Exception unused) {
            L.e(TAG, "\u8bf7\u6c42\u63d0\u4ea4\u5931\u8d25:" + str);
            return null;
        }
    }

    public void loadingAlarm(final String str) {
        Timer timer = new Timer();
        this.mTimer = timer;
        timer.schedule(new TimerTask() { // from class: com.netease.ntunisdk.external.protocol.view.UniWebView.4
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                if (UniWebView.this.mErrorUrl == null || !UniWebView.this.mErrorUrl.equals(str)) {
                    UniWebView.this.post(new Runnable() { // from class: com.netease.ntunisdk.external.protocol.view.UniWebView.4.1
                        @Override // java.lang.Runnable
                        public void run() throws JSONException {
                            try {
                                JSONObject jSONObject = new JSONObject();
                                jSONObject.put(Const.WEB_URL, str);
                                UniWebView.this.runCallback(jSONObject, Const.ON_PAGE_ERROR);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        }, 5000L);
    }

    public void setBaseUrl(String str) {
        this.mBaseUrl = str;
    }

    @Override // android.webkit.WebView
    public void destroy() {
        try {
            super.destroy();
        } catch (Throwable unused) {
        }
        try {
            resetTimer();
        } catch (Throwable unused2) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetTimer() {
        Timer timer = this.mTimer;
        if (timer != null) {
            timer.cancel();
            this.mTimer.purge();
            this.mTimer = null;
        }
    }

    public static class Config {
        private static volatile Config sInstance;
        boolean isUseWideViewPort = true;
        boolean loadLocalJS = false;
        private String mLocalJS = null;

        private Config() {
        }

        public static Config getInstance() {
            if (sInstance == null) {
                synchronized (Config.class) {
                    if (sInstance == null) {
                        sInstance = new Config();
                    }
                }
            }
            return sInstance;
        }

        /* JADX WARN: Removed duplicated region for block: B:54:0x0085  */
        /* JADX WARN: Removed duplicated region for block: B:65:0x0070 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:69:0x0087 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:76:0x007d A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:86:? A[Catch: all -> 0x0090, SYNTHETIC, TRY_LEAVE, TryCatch #5 {, blocks: (B:4:0x0003, B:14:0x003b, B:19:0x0045, B:22:0x004a, B:17:0x0040, B:37:0x0066, B:42:0x0070, B:45:0x0075, B:40:0x006b, B:50:0x007d, B:55:0x0087, B:59:0x008f, B:58:0x008c, B:53:0x0082), top: B:73:0x0003, inners: #1, #3, #4, #6, #8, #10 }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private static synchronized java.lang.String getJS(android.content.Context r6, java.lang.String r7) {
            /*
                java.lang.Class<com.netease.ntunisdk.external.protocol.view.UniWebView$Config> r0 = com.netease.ntunisdk.external.protocol.view.UniWebView.Config.class
                monitor-enter(r0)
                java.lang.String r1 = "Protocol UniWebView"
                java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L90
                r2.<init>()     // Catch: java.lang.Throwable -> L90
                java.lang.String r3 = "getJS, fileName="
                r2.append(r3)     // Catch: java.lang.Throwable -> L90
                r2.append(r7)     // Catch: java.lang.Throwable -> L90
                java.lang.String r2 = r2.toString()     // Catch: java.lang.Throwable -> L90
                com.netease.ntunisdk.external.protocol.utils.L.d(r1, r2)     // Catch: java.lang.Throwable -> L90
                r1 = 0
                android.content.res.AssetManager r6 = r6.getAssets()     // Catch: java.lang.Throwable -> L59 java.io.IOException -> L5e
                java.io.InputStream r6 = r6.open(r7)     // Catch: java.lang.Throwable -> L59 java.io.IOException -> L5e
                java.io.ByteArrayOutputStream r7 = new java.io.ByteArrayOutputStream     // Catch: java.lang.Throwable -> L51 java.io.IOException -> L56
                r7.<init>()     // Catch: java.lang.Throwable -> L51 java.io.IOException -> L56
                r2 = 2048(0x800, float:2.87E-42)
                byte[] r2 = new byte[r2]     // Catch: java.io.IOException -> L4f java.lang.Throwable -> L7a
            L2b:
                int r3 = r6.read(r2)     // Catch: java.io.IOException -> L4f java.lang.Throwable -> L7a
                r4 = -1
                if (r3 == r4) goto L37
                r4 = 0
                r7.write(r2, r4, r3)     // Catch: java.io.IOException -> L4f java.lang.Throwable -> L7a
                goto L2b
            L37:
                java.lang.String r1 = r7.toString()     // Catch: java.io.IOException -> L4f java.lang.Throwable -> L7a
                r7.close()     // Catch: java.io.IOException -> L3f java.lang.Throwable -> L90
                goto L43
            L3f:
                r7 = move-exception
                r7.printStackTrace()     // Catch: java.lang.Throwable -> L90
            L43:
                if (r6 == 0) goto L4d
                r6.close()     // Catch: java.io.IOException -> L49 java.lang.Throwable -> L90
                goto L4d
            L49:
                r6 = move-exception
                r6.printStackTrace()     // Catch: java.lang.Throwable -> L90
            L4d:
                monitor-exit(r0)
                return r1
            L4f:
                r2 = move-exception
                goto L61
            L51:
                r7 = move-exception
                r5 = r1
                r1 = r7
                r7 = r5
                goto L7b
            L56:
                r2 = move-exception
                r7 = r1
                goto L61
            L59:
                r6 = move-exception
                r7 = r1
                r1 = r6
                r6 = r7
                goto L7b
            L5e:
                r2 = move-exception
                r6 = r1
                r7 = r6
            L61:
                r2.printStackTrace()     // Catch: java.lang.Throwable -> L7a
                if (r7 == 0) goto L6e
                r7.close()     // Catch: java.io.IOException -> L6a java.lang.Throwable -> L90
                goto L6e
            L6a:
                r7 = move-exception
                r7.printStackTrace()     // Catch: java.lang.Throwable -> L90
            L6e:
                if (r6 == 0) goto L78
                r6.close()     // Catch: java.io.IOException -> L74 java.lang.Throwable -> L90
                goto L78
            L74:
                r6 = move-exception
                r6.printStackTrace()     // Catch: java.lang.Throwable -> L90
            L78:
                monitor-exit(r0)
                return r1
            L7a:
                r1 = move-exception
            L7b:
                if (r7 == 0) goto L85
                r7.close()     // Catch: java.io.IOException -> L81 java.lang.Throwable -> L90
                goto L85
            L81:
                r7 = move-exception
                r7.printStackTrace()     // Catch: java.lang.Throwable -> L90
            L85:
                if (r6 == 0) goto L8f
                r6.close()     // Catch: java.io.IOException -> L8b java.lang.Throwable -> L90
                goto L8f
            L8b:
                r6 = move-exception
                r6.printStackTrace()     // Catch: java.lang.Throwable -> L90
            L8f:
                throw r1     // Catch: java.lang.Throwable -> L90
            L90:
                r6 = move-exception
                monitor-exit(r0)
                goto L94
            L93:
                throw r6
            L94:
                goto L93
            */
            throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.external.protocol.view.UniWebView.Config.getJS(android.content.Context, java.lang.String):java.lang.String");
        }

        public boolean isUseWideViewPort() {
            return this.isUseWideViewPort;
        }

        public void setUseWideViewPort(boolean z) {
            this.isUseWideViewPort = z;
        }

        public boolean isLoadLocalJS() {
            return this.loadLocalJS;
        }

        public void setLoadLocalJS(boolean z) {
            this.loadLocalJS = z;
        }

        public synchronized String getLocalJS(Context context) {
            if (this.loadLocalJS && TextUtils.isEmpty(this.mLocalJS)) {
                this.mLocalJS = getJS(context, UniWebView.NTWKJSBridgeJS);
            }
            return this.mLocalJS;
        }
    }
}