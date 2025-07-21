package com.netease.rnwebview;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.CCMsgSdk.WebSocketMessageCodeType;
import com.facebook.common.logging.FLog;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.common.ReactConstants;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.ContentSizeChangeEvent;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.TouchesHelper;
import com.facebook.react.views.scroll.ReactScrollViewHelper;
import com.tencent.open.SocialConstants;
import com.xiaomi.gamecenter.sdk.utils.RSASignature;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Nullable;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Marker;

/* loaded from: classes4.dex */
public class RNWebViewManager extends SimpleViewManager<WebView> {
    protected static final String BLANK_URL = "about:blank";
    protected static final String BRIDGE_NAME = "__REACT_WEB_VIEW_BRIDGE";
    public static final int COMMAND_GO_BACK = 1;
    public static final int COMMAND_GO_FORWARD = 2;
    public static final int COMMAND_INJECT_JAVASCRIPT = 6;
    public static final int COMMAND_POST_MESSAGE = 5;
    public static final int COMMAND_RELOAD = 3;
    public static final int COMMAND_STOP_LOADING = 4;
    protected static final String HTML_ENCODING = "UTF-8";
    protected static final String HTML_MIME_TYPE = "text/html";
    protected static final String HTTP_METHOD_POST = "POST";
    protected static final String REACT_CLASS = "RNWebViewAndroid";
    public static String whitePath;
    private RNWebViewPackage aPackage;

    @Nullable
    protected WebView.PictureListener mPictureListener;
    protected WebViewConfig mWebViewConfig;

    @Override // com.facebook.react.uimanager.ViewManager, com.facebook.react.bridge.NativeModule
    public String getName() {
        return REACT_CLASS;
    }

    @ReactProp(name = "allowUniversalAccessFromFileURLs")
    public void setAllowUniversalAccessFromFileURLs(WebView webView, boolean z) {
    }

    public static boolean isInWhitePath(String str) {
        return Uri.parse(str).getPath().startsWith(whitePath);
    }

    protected static class ReactWebViewClient extends WebViewClient {
        protected boolean mLastLoadFailed = false;

        @Nullable
        protected ReadableArray mUrlPrefixesForDefaultIntent;

        protected ReactWebViewClient() {
        }

        @Override // android.webkit.WebViewClient
        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);
            if (this.mLastLoadFailed) {
                return;
            }
            ReactWebView reactWebView = (ReactWebView) webView;
            reactWebView.callInjectedJavaScript();
            reactWebView.linkBridge();
            emitFinishEvent(webView, str);
        }

        @Override // android.webkit.WebViewClient
        public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            super.onPageStarted(webView, str, bitmap);
            this.mLastLoadFailed = false;
            RNWebViewManager.dispatchEvent(webView, new TopLoadingStartEvent(webView.getId(), createWebViewEvent(webView, str)));
        }

        @Override // android.webkit.WebViewClient
        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            boolean z;
            ReadableArray readableArray = this.mUrlPrefixesForDefaultIntent;
            if (readableArray == null || readableArray.size() <= 0) {
                z = false;
            } else {
                Iterator<Object> it = this.mUrlPrefixesForDefaultIntent.toArrayList().iterator();
                while (it.hasNext()) {
                    if (str.startsWith((String) it.next())) {
                        z = true;
                        break;
                    }
                }
                z = false;
            }
            if (!z && (str.startsWith("http://") || str.startsWith("https://") || str.startsWith("file://") || str.equals(RNWebViewManager.BLANK_URL))) {
                return false;
            }
            try {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
                intent.setFlags(268435456);
                webView.getContext().startActivity(intent);
            } catch (ActivityNotFoundException e) {
                FLog.w(ReactConstants.TAG, "activity not found to handle uri scheme for: " + str, e);
            }
            return true;
        }

        public String filterMCString(String str) {
            if (str.startsWith("https://mc79/")) {
                String strTrim = str.replace("https://mc79/", "").trim();
                if (RNWebViewManager.isInWhitePath(strTrim)) {
                    return strTrim;
                }
            }
            return "";
        }

        @Override // android.webkit.WebViewClient
        public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
            WebResourceResponse webResourceResponse;
            Uri url = webResourceRequest.getUrl();
            return (url == null || (webResourceResponse = getWebResourceResponse(url.toString())) == null) ? super.shouldInterceptRequest(webView, webResourceRequest) : webResourceResponse;
        }

        @Nullable
        private WebResourceResponse getWebResourceResponse(String str) {
            try {
                String strFilterMCString = filterMCString(str);
                if (strFilterMCString.length() <= 0) {
                    return null;
                }
                File file = new File(strFilterMCString);
                if (!file.exists()) {
                    return null;
                }
                FileInputStream fileInputStream = new FileInputStream(file);
                HashMap map = new HashMap();
                map.put("Access-Control-Allow-Origin", Marker.ANY_MARKER);
                map.put("Access-Control-Allow-Headers", "X-Requested-With");
                map.put("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
                map.put("Access-Control-Allow-Credentials", "true");
                return new WebResourceResponse("image/*", RSASignature.c, 200, WebSocketMessageCodeType.SUC, map, fileInputStream);
            } catch (Exception e) {
                Log.e("zzzzzzzzzzz webview", e.getMessage());
                e.printStackTrace();
                return null;
            }
        }

        @Override // android.webkit.WebViewClient
        public WebResourceResponse shouldInterceptRequest(WebView webView, String str) {
            WebResourceResponse webResourceResponse = getWebResourceResponse(str);
            return webResourceResponse != null ? webResourceResponse : super.shouldInterceptRequest(webView, str);
        }

        @Override // android.webkit.WebViewClient
        public void onReceivedError(WebView webView, int i, String str, String str2) {
            super.onReceivedError(webView, i, str, str2);
            this.mLastLoadFailed = true;
            emitFinishEvent(webView, str2);
            WritableMap writableMapCreateWebViewEvent = createWebViewEvent(webView, str2);
            writableMapCreateWebViewEvent.putDouble("code", i);
            writableMapCreateWebViewEvent.putString(SocialConstants.PARAM_COMMENT, str);
            Log.d("zzzzzzzzzzz webview", "onReceivedError description::" + str + "::: code" + i);
            RNWebViewManager.dispatchEvent(webView, new TopLoadingErrorEvent(webView.getId(), writableMapCreateWebViewEvent));
        }

        protected void emitFinishEvent(WebView webView, String str) {
            RNWebViewManager.dispatchEvent(webView, new TopLoadingFinishEvent(webView.getId(), createWebViewEvent(webView, str)));
        }

        protected WritableMap createWebViewEvent(WebView webView, String str) {
            WritableMap writableMapCreateMap = Arguments.createMap();
            writableMapCreateMap.putDouble(TouchesHelper.TARGET_KEY, webView.getId());
            writableMapCreateMap.putString("url", str);
            writableMapCreateMap.putBoolean("loading", (this.mLastLoadFailed || webView.getProgress() == 100) ? false : true);
            writableMapCreateMap.putString("title", webView.getTitle());
            writableMapCreateMap.putBoolean("canGoBack", webView.canGoBack());
            writableMapCreateMap.putBoolean("canGoForward", webView.canGoForward());
            return writableMapCreateMap;
        }

        public void setUrlPrefixesForDefaultIntent(ReadableArray readableArray) {
            this.mUrlPrefixesForDefaultIntent = readableArray;
        }
    }

    protected static class ReactWebView extends WebView implements LifecycleEventListener {

        @Nullable
        protected String injectedJS;

        @Nullable
        protected ReactWebViewClient mReactWebViewClient;
        protected boolean messagingEnabled;

        protected class ReactWebViewBridge {
            ReactWebView mContext;

            ReactWebViewBridge(ReactWebView reactWebView) {
                this.mContext = reactWebView;
            }

            @JavascriptInterface
            public void postMessage(String str) {
                this.mContext.onMessage(str);
            }
        }

        public ReactWebView(ThemedReactContext themedReactContext) {
            super(themedReactContext);
            this.messagingEnabled = false;
        }

        @Override // com.facebook.react.bridge.LifecycleEventListener
        public void onHostResume() {
            try {
                onResume();
            } catch (Throwable unused) {
            }
        }

        @Override // com.facebook.react.bridge.LifecycleEventListener
        public void onHostPause() {
            try {
                onPause();
            } catch (Throwable unused) {
            }
        }

        @Override // com.facebook.react.bridge.LifecycleEventListener
        public void onHostDestroy() {
            cleanupCallbacksAndDestroy();
        }

        @Override // android.webkit.WebView
        public void setWebViewClient(WebViewClient webViewClient) {
            super.setWebViewClient(webViewClient);
            this.mReactWebViewClient = (ReactWebViewClient) webViewClient;
        }

        @Nullable
        public ReactWebViewClient getReactWebViewClient() {
            return this.mReactWebViewClient;
        }

        public void setInjectedJavaScript(@Nullable String str) {
            this.injectedJS = str;
        }

        protected ReactWebViewBridge createReactWebViewBridge(ReactWebView reactWebView) {
            return new ReactWebViewBridge(reactWebView);
        }

        public void setMessagingEnabled(boolean z) {
            if (this.messagingEnabled == z) {
                return;
            }
            this.messagingEnabled = z;
            if (z) {
                addJavascriptInterface(createReactWebViewBridge(this), RNWebViewManager.BRIDGE_NAME);
                linkBridge();
            } else {
                removeJavascriptInterface(RNWebViewManager.BRIDGE_NAME);
            }
        }

        public void callInjectedJavaScript() {
            String str;
            if (!getSettings().getJavaScriptEnabled() || (str = this.injectedJS) == null || TextUtils.isEmpty(str)) {
                return;
            }
            loadUrl("javascript:(function() {\n" + this.injectedJS + ";\n})();");
        }

        public void linkBridge() {
            if (this.messagingEnabled) {
                loadUrl("javascript:(window.originalPostMessage = window.postMessage,window.postMessage = function(data) {__REACT_WEB_VIEW_BRIDGE.postMessage(String(data));})");
            }
        }

        public void onMessage(String str) {
            RNWebViewManager.dispatchEvent(this, new TopMessageEvent(getId(), str));
        }

        protected void cleanupCallbacksAndDestroy() {
            setWebViewClient(null);
            destroy();
        }
    }

    public RNWebViewManager(String str) {
        whitePath = str;
        setWebViewConfig();
    }

    public RNWebViewManager() {
        setWebViewConfig();
    }

    /* renamed from: com.netease.rnwebview.RNWebViewManager$1 */
    class AnonymousClass1 implements WebViewConfig {
        @Override // com.netease.rnwebview.WebViewConfig
        public void configWebView(WebView webView) {
        }

        AnonymousClass1() {
        }
    }

    public void setWebViewConfig() {
        this.mWebViewConfig = new WebViewConfig() { // from class: com.netease.rnwebview.RNWebViewManager.1
            @Override // com.netease.rnwebview.WebViewConfig
            public void configWebView(WebView webView) {
            }

            AnonymousClass1() {
            }
        };
    }

    public RNWebViewManager(WebViewConfig webViewConfig) {
        this.mWebViewConfig = webViewConfig;
    }

    protected ReactWebView createReactWebViewInstance(ThemedReactContext themedReactContext) {
        return new ReactWebView(themedReactContext);
    }

    /* renamed from: com.netease.rnwebview.RNWebViewManager$2 */
    class AnonymousClass2 extends WebChromeClient {
        @Override // android.webkit.WebChromeClient
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            return true;
        }

        AnonymousClass2() {
        }

        @Override // android.webkit.WebChromeClient
        public void onGeolocationPermissionsShowPrompt(String str, GeolocationPermissions.Callback callback) {
            callback.invoke(str, true, false);
        }
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public WebView createViewInstance(ThemedReactContext themedReactContext) {
        ReactWebView reactWebViewCreateReactWebViewInstance = createReactWebViewInstance(themedReactContext);
        reactWebViewCreateReactWebViewInstance.setWebChromeClient(new WebChromeClient() { // from class: com.netease.rnwebview.RNWebViewManager.2
            @Override // android.webkit.WebChromeClient
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                return true;
            }

            AnonymousClass2() {
            }

            @Override // android.webkit.WebChromeClient
            public void onGeolocationPermissionsShowPrompt(String str, GeolocationPermissions.Callback callback) {
                callback.invoke(str, true, false);
            }
        });
        themedReactContext.addLifecycleEventListener(reactWebViewCreateReactWebViewInstance);
        this.mWebViewConfig.configWebView(reactWebViewCreateReactWebViewInstance);
        reactWebViewCreateReactWebViewInstance.getSettings().setBuiltInZoomControls(true);
        reactWebViewCreateReactWebViewInstance.getSettings().setDisplayZoomControls(false);
        reactWebViewCreateReactWebViewInstance.getSettings().setDomStorageEnabled(true);
        reactWebViewCreateReactWebViewInstance.getSettings().setAllowFileAccess(false);
        reactWebViewCreateReactWebViewInstance.getSettings().setAllowFileAccessFromFileURLs(false);
        reactWebViewCreateReactWebViewInstance.getSettings().setAllowUniversalAccessFromFileURLs(false);
        CookieManager.getInstance().setAcceptThirdPartyCookies(reactWebViewCreateReactWebViewInstance, true);
        reactWebViewCreateReactWebViewInstance.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        return reactWebViewCreateReactWebViewInstance;
    }

    public void setPackage(RNWebViewPackage rNWebViewPackage) {
        this.aPackage = rNWebViewPackage;
    }

    @ReactProp(name = "javaScriptEnabled")
    public void setJavaScriptEnabled(WebView webView, boolean z) {
        webView.getSettings().setJavaScriptEnabled(z);
    }

    @ReactProp(name = "showsHorizontalScrollIndicator")
    public void setShowsHorizontalScrollIndicator(WebView webView, boolean z) {
        webView.setHorizontalScrollBarEnabled(z);
    }

    @ReactProp(name = "showsVerticalScrollIndicator")
    public void setShowsVerticalScrollIndicator(WebView webView, boolean z) {
        webView.setVerticalScrollBarEnabled(z);
    }

    @ReactProp(name = "textZoom")
    public void setTextZoom(WebView webView, int i) {
        webView.getSettings().setTextZoom(i);
    }

    @ReactProp(name = "androidHardwareAccelerationDisabled")
    public void setHardwareAccelerationDisabled(WebView webView, boolean z) {
        boolean z2 = (((ReactContext) webView.getContext()).getCurrentActivity().getWindow().getAttributes().flags & 16777216) != 0;
        if (z || !z2) {
            webView.setLayerType(1, null);
        } else {
            webView.setLayerType(2, null);
        }
    }

    @ReactProp(name = "thirdPartyCookiesEnabled")
    public void setThirdPartyCookiesEnabled(WebView webView, boolean z) {
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, z);
    }

    @ReactProp(name = "scalesPageToFit")
    public void setScalesPageToFit(WebView webView, boolean z) {
        webView.getSettings().setUseWideViewPort(!z);
    }

    @ReactProp(name = "domStorageEnabled")
    public void setDomStorageEnabled(WebView webView, boolean z) {
        webView.getSettings().setDomStorageEnabled(z);
    }

    @ReactProp(name = "userAgent")
    public void setUserAgent(WebView webView, @Nullable String str) {
        if (str != null) {
            webView.getSettings().setUserAgentString(str);
        }
    }

    @ReactProp(name = "mediaPlaybackRequiresUserAction")
    public void setMediaPlaybackRequiresUserAction(WebView webView, boolean z) {
        webView.getSettings().setMediaPlaybackRequiresUserGesture(z);
    }

    @ReactProp(name = "saveFormDataDisabled")
    public void setSaveFormDataDisabled(WebView webView, boolean z) {
        webView.getSettings().setSaveFormData(!z);
    }

    @ReactProp(name = "injectedJavaScript")
    public void setInjectedJavaScript(WebView webView, @Nullable String str) {
        ((ReactWebView) webView).setInjectedJavaScript(str);
    }

    @ReactProp(name = "messagingEnabled")
    public void setMessagingEnabled(WebView webView, boolean z) {
        ((ReactWebView) webView).setMessagingEnabled(z);
    }

    @ReactProp(name = "clearCache")
    public void setClearCache(WebView webView, boolean z) {
        if (z) {
            webView.clearCache(true);
        }
    }

    public void setFileAccess(WebView webView, boolean z) {
        webView.getSettings().setAllowFileAccess(z);
        webView.getSettings().setAllowFileAccessFromFileURLs(z);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(z);
    }

    @ReactProp(name = SocialConstants.PARAM_SOURCE)
    public void setSource(WebView webView, @Nullable ReadableMap readableMap) throws UnsupportedEncodingException {
        byte[] bytes;
        if (readableMap != null) {
            if (readableMap.hasKey("html")) {
                String string = readableMap.getString("html");
                setFileAccess(webView, false);
                if (readableMap.hasKey("baseUrl")) {
                    webView.loadDataWithBaseURL(readableMap.getString("baseUrl"), string, "text/html", "UTF-8", null);
                    return;
                } else {
                    webView.loadData(string, "text/html", "UTF-8");
                    return;
                }
            }
            if (readableMap.hasKey("uri")) {
                String string2 = readableMap.getString("uri");
                if (string2.startsWith("file://")) {
                    if (!isInWhitePath(string2)) {
                        setFileAccess(webView, false);
                        webView.loadUrl(BLANK_URL);
                        return;
                    }
                    setFileAccess(webView, true);
                } else {
                    setFileAccess(webView, false);
                }
                String url = webView.getUrl();
                if (url == null || !url.equals(string2)) {
                    if (readableMap.hasKey("method") && readableMap.getString("method").equals("POST")) {
                        if (readableMap.hasKey("body")) {
                            String string3 = readableMap.getString("body");
                            try {
                                bytes = string3.getBytes("UTF-8");
                            } catch (UnsupportedEncodingException unused) {
                                bytes = string3.getBytes();
                            }
                        } else {
                            bytes = null;
                        }
                        if (bytes == null) {
                            bytes = new byte[0];
                        }
                        webView.postUrl(string2, bytes);
                        return;
                    }
                    HashMap map = new HashMap();
                    if (readableMap.hasKey("headers")) {
                        ReadableMap map2 = readableMap.getMap("headers");
                        ReadableMapKeySetIterator readableMapKeySetIteratorKeySetIterator = map2.keySetIterator();
                        while (readableMapKeySetIteratorKeySetIterator.hasNextKey()) {
                            String strNextKey = readableMapKeySetIteratorKeySetIterator.nextKey();
                            if ("user-agent".equals(strNextKey.toLowerCase(Locale.ENGLISH))) {
                                if (webView.getSettings() != null) {
                                    webView.getSettings().setUserAgentString(map2.getString(strNextKey));
                                }
                            } else {
                                map.put(strNextKey, map2.getString(strNextKey));
                            }
                        }
                    }
                    webView.loadUrl(string2, map);
                    return;
                }
                return;
            }
        }
        setFileAccess(webView, false);
        webView.loadUrl(BLANK_URL);
    }

    @ReactProp(name = "onContentSizeChange")
    public void setOnContentSizeChange(WebView webView, boolean z) {
        if (z) {
            webView.setPictureListener(getPictureListener());
        } else {
            webView.setPictureListener(null);
        }
    }

    @ReactProp(name = "mixedContentMode")
    public void setMixedContentMode(WebView webView, @Nullable String str) {
        if (str == null || ReactScrollViewHelper.OVER_SCROLL_NEVER.equals(str)) {
            webView.getSettings().setMixedContentMode(1);
        } else if (ReactScrollViewHelper.OVER_SCROLL_ALWAYS.equals(str)) {
            webView.getSettings().setMixedContentMode(0);
        } else if ("compatibility".equals(str)) {
            webView.getSettings().setMixedContentMode(2);
        }
    }

    @ReactProp(name = "urlPrefixesForDefaultIntent")
    public void setUrlPrefixesForDefaultIntent(WebView webView, @Nullable ReadableArray readableArray) {
        ReactWebViewClient reactWebViewClient = ((ReactWebView) webView).getReactWebViewClient();
        if (reactWebViewClient == null || readableArray == null) {
            return;
        }
        reactWebViewClient.setUrlPrefixesForDefaultIntent(readableArray);
    }

    /* renamed from: com.netease.rnwebview.RNWebViewManager$3 */
    class AnonymousClass3 extends WebChromeClient {
        AnonymousClass3() {
        }

        @Override // android.webkit.WebChromeClient
        public boolean onJsAlert(WebView webView, String str, String str2, JsResult jsResult) {
            RNWebViewManager.this.aPackage.getModule().showAlert(str, str2, jsResult);
            return true;
        }

        public void openFileChooser(ValueCallback<Uri> valueCallback, String str, String str2) {
            RNWebViewManager.this.aPackage.getModule().startFileChooserIntent(valueCallback, str);
        }

        @Override // android.webkit.WebChromeClient
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, WebChromeClient.FileChooserParams fileChooserParams) {
            return RNWebViewManager.this.aPackage.getModule().startFileChooserIntent(valueCallback, fileChooserParams.createIntent());
        }
    }

    @ReactProp(name = "uploadEnabledAndroid")
    public void uploadEnabledAndroid(WebView webView, boolean z) {
        if (z) {
            webView.setWebChromeClient(new WebChromeClient() { // from class: com.netease.rnwebview.RNWebViewManager.3
                AnonymousClass3() {
                }

                @Override // android.webkit.WebChromeClient
                public boolean onJsAlert(WebView webView2, String str, String str2, JsResult jsResult) {
                    RNWebViewManager.this.aPackage.getModule().showAlert(str, str2, jsResult);
                    return true;
                }

                public void openFileChooser(ValueCallback<Uri> valueCallback, String str, String str2) {
                    RNWebViewManager.this.aPackage.getModule().startFileChooserIntent(valueCallback, str);
                }

                @Override // android.webkit.WebChromeClient
                public boolean onShowFileChooser(WebView webView2, ValueCallback<Uri[]> valueCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                    return RNWebViewManager.this.aPackage.getModule().startFileChooserIntent(valueCallback, fileChooserParams.createIntent());
                }
            });
        }
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public void addEventEmitters(ThemedReactContext themedReactContext, WebView webView) {
        webView.setWebViewClient(new ReactWebViewClient());
    }

    @Override // com.facebook.react.uimanager.ViewManager
    @Nullable
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.of("goBack", 1, "goForward", 2, "reload", 3, "stopLoading", 4, "postMessage", 5, "injectJavaScript", 6);
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public void receiveCommand(WebView webView, int i, @Nullable ReadableArray readableArray) throws JSONException {
        switch (i) {
            case 1:
                webView.goBack();
                return;
            case 2:
                webView.goForward();
                return;
            case 3:
                webView.reload();
                return;
            case 4:
                webView.stopLoading();
                return;
            case 5:
                try {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("data", readableArray.getString(0));
                    webView.loadUrl("javascript:(function () {var event;var data = " + jSONObject.toString() + ";try {event = new MessageEvent('message', data);} catch (e) {event = document.createEvent('MessageEvent');event.initMessageEvent('message', true, true, data.data, data.origin, data.lastEventId, data.source);}document.dispatchEvent(event);})();");
                    return;
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            case 6:
                webView.loadUrl("javascript:" + readableArray.getString(0));
                return;
            default:
                return;
        }
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public void onDropViewInstance(WebView webView) {
        super.onDropViewInstance((RNWebViewManager) webView);
        ThemedReactContext themedReactContext = (ThemedReactContext) webView.getContext();
        ReactWebView reactWebView = (ReactWebView) webView;
        themedReactContext.removeLifecycleEventListener(reactWebView);
        reactWebView.cleanupCallbacksAndDestroy();
    }

    /* renamed from: com.netease.rnwebview.RNWebViewManager$4 */
    class AnonymousClass4 implements WebView.PictureListener {
        AnonymousClass4() {
        }

        @Override // android.webkit.WebView.PictureListener
        public void onNewPicture(WebView webView, Picture picture) {
            RNWebViewManager.dispatchEvent(webView, new ContentSizeChangeEvent(webView.getId(), webView.getWidth(), webView.getContentHeight()));
        }
    }

    protected WebView.PictureListener getPictureListener() {
        if (this.mPictureListener == null) {
            this.mPictureListener = new WebView.PictureListener() { // from class: com.netease.rnwebview.RNWebViewManager.4
                AnonymousClass4() {
                }

                @Override // android.webkit.WebView.PictureListener
                public void onNewPicture(WebView webView, Picture picture) {
                    RNWebViewManager.dispatchEvent(webView, new ContentSizeChangeEvent(webView.getId(), webView.getWidth(), webView.getContentHeight()));
                }
            };
        }
        return this.mPictureListener;
    }

    protected static void dispatchEvent(WebView webView, Event event) {
        ((UIManagerModule) ((ReactContext) webView.getContext()).getNativeModule(UIManagerModule.class)).getEventDispatcher().dispatchEvent(event);
    }
}