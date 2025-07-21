package com.netease.ntunisdk.modules.ngwebviewgeneral.ui;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.facebook.hermes.intl.Constants;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.netease.ntunisdk.external.protocol.Const;
import com.netease.ntunisdk.modules.api.ModulesManager;
import com.netease.ntunisdk.modules.ngwebviewgeneral.BitmapUtil;
import com.netease.ntunisdk.modules.ngwebviewgeneral.NgWebViewGeneralModule;
import com.netease.ntunisdk.modules.ngwebviewgeneral.NgWebviewFileProvider;
import com.netease.ntunisdk.modules.ngwebviewgeneral.R;
import com.netease.ntunisdk.modules.ngwebviewgeneral.WebviewParams;
import com.netease.ntunisdk.modules.ngwebviewgeneral.callback.RequestPermissionCallBack;
import com.netease.ntunisdk.modules.ngwebviewgeneral.log.NgWebviewLog;
import com.netease.ntunisdk.modules.ngwebviewgeneral.util.DownloadUtil;
import com.netease.ntunisdk.modules.permission.common.PermissionConstant;
import com.netease.ntunisdk.modules.personalinfolist.HookManager;
import com.xiaomi.gamecenter.sdk.utils.RSASignature;
import com.xiaomi.gamecenter.sdk.web.webview.webkit.h;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Marker;

/* loaded from: classes.dex */
public class UniWebView extends WebView {
    public static final String ACTION_GETNETWORKTYPE = "getNetworkType";
    public static final String ALIPAY_PREFIX = "alipays://platformapi/startApp?";
    public static final String CB_ACTION = "func";
    public static final String CB_NATIVE2H5 = "callback";
    public static final String CB_PARAMS = "params";
    public static final String HTTP_PREFIX = "http";
    public static final String IDV_PREFIX = "idvurl://";
    private static final String NTWKJSBridgeJS = "NTWKJSBridge.js";
    public static final int REQUEST_CODE_FILE_CHOOSER = 9527;
    private static final String TAG = "UniSDK UniWebView";
    public static final String UNIWEB_PREFIX = "uniweb://";
    public static final String UNI_JSBRIDGE = "unisdk-jsbridge";
    public static final String UNI_JSBRIDGE_PREFIX = "unisdk-jsbridge://";
    public static final String URLPROXY_PREFIX = "nativerequest/";
    NgWebviewActivity act;
    private UniWebViewCallback callback;
    private View errorView;
    private FrameLayout flVideoContainer;
    private boolean isHasPdfView;
    private View loadingView;
    private RelativeLayout mContentView;
    public PDFView mPdfView;
    public View mPdfViewRoot;
    private FrameLayout mUniWvContainer;
    private ValueCallback<Uri[]> mUploadCallBackAboveL;
    private ValueCallback<Uri> mUploadMessage;
    private Context myCtx;
    private int webViewIcon;

    public interface UniWebViewCallback {
        void callback(String str, String str2);
    }

    public UniWebView(Context context, FrameLayout frameLayout, WebviewParams webviewParams) {
        super(context);
        this.webViewIcon = -1;
        this.act = NgWebviewActivity.getInstance();
        this.myCtx = context;
        this.mUniWvContainer = (FrameLayout) frameLayout.findViewById(ResIdReader.getId(getContext(), "webview_Container"));
        this.mContentView = (RelativeLayout) frameLayout.findViewById(ResIdReader.getId(getContext(), "content_view"));
        this.flVideoContainer = (FrameLayout) frameLayout.findViewById(ResIdReader.getId(getContext(), "flVideoContainer"));
        loadingView(context);
        this.isHasPdfView = webviewParams.isHasPdfView();
        if (this.isHasPdfView) {
            initPDFView(context);
        }
        initWebView();
    }

    public UniWebView(Context context, FrameLayout frameLayout, WebviewParams webviewParams, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.webViewIcon = -1;
        this.act = NgWebviewActivity.getInstance();
        this.myCtx = context;
        loadingView(context);
        this.isHasPdfView = webviewParams.isHasPdfView();
        if (this.isHasPdfView) {
            initPDFView(context);
        }
        initWebView();
    }

    public UniWebView(Context context, FrameLayout frameLayout, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.webViewIcon = -1;
        this.myCtx = context;
        initWebView();
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

    public void onActivityResult(int i, int i2, Intent intent) {
        NgWebviewLog.d(TAG, "onActivityResult, requestCode=" + i + ", resultCode=" + i2 + ", data=" + intent, new Object[0]);
        if (i == 9527) {
            uploadResultMessage(intent);
        }
    }

    private void uploadResultMessage(Intent intent) {
        Uri data;
        NgWebviewLog.d(TAG, "uploadResultMessage", new Object[0]);
        String strCreateSuitableImgFile = (intent == null || (data = intent.getData()) == null) ? null : createSuitableImgFile(this.myCtx, data, 2097152);
        Uri fileUri = !TextUtils.isEmpty(strCreateSuitableImgFile) ? getFileUri(strCreateSuitableImgFile) : null;
        ValueCallback<Uri[]> valueCallback = this.mUploadCallBackAboveL;
        if (valueCallback != null) {
            if (fileUri != null) {
                valueCallback.onReceiveValue(new Uri[]{fileUri});
            } else {
                valueCallback.onReceiveValue(null);
            }
            this.mUploadCallBackAboveL = null;
            return;
        }
        this.mUploadMessage.onReceiveValue(fileUri);
        this.mUploadMessage = null;
    }

    public Uri getFileUri(String str) {
        Uri uriForFile = null;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        File file = new File(str);
        if (Build.VERSION.SDK_INT < 24) {
            try {
                return Uri.fromFile(file);
            } catch (Exception e) {
                NgWebviewLog.e(TAG, "fromFile Exception : " + e.getMessage());
                return null;
            }
        }
        String str2 = getAppVersionName(this.myCtx) + ".ngwebview.fileprovider";
        NgWebviewLog.d(TAG, "authority = " + str2, new Object[0]);
        try {
            uriForFile = NgWebviewFileProvider.getUriForFile(this.myCtx, str2, file);
        } catch (Exception e2) {
            NgWebviewLog.e(TAG, "getUriForFile Exception : " + e2.getMessage());
        }
        if (uriForFile != null) {
            return uriForFile;
        }
        try {
            return Uri.fromFile(file);
        } catch (Exception e3) {
            NgWebviewLog.e(TAG, "fromFile Exception : " + e3.getMessage());
            return uriForFile;
        }
    }

    private String createSuitableImgFile(Context context, Object obj, int i) throws IOException {
        Bitmap bitmapCreateBitmap = BitmapUtil.createBitmap(context, obj);
        if (obj instanceof String) {
            BitmapUtil.deleteFile((String) obj);
        }
        if (bitmapCreateBitmap == null) {
            NgWebviewLog.e(TAG, "can't create a bitmap");
            return null;
        }
        String imgSavePath = BitmapUtil.getImgSavePath(context);
        if (TextUtils.isEmpty(imgSavePath)) {
            NgWebviewLog.e(TAG, "can't get a save path");
            return null;
        }
        if (BitmapUtil.saveBitmap(bitmapCreateBitmap, new File(imgSavePath), i)) {
            return imgSavePath;
        }
        NgWebviewLog.e(TAG, "can't save bitmap");
        return null;
    }

    public void handleNativeNotify(String str) {
        String str2 = "javascript:window.handleNativeNotify&&window.handleNativeNotify(" + str + ")";
        NgWebviewLog.d(TAG, "handleNativeNotify js\uff1a" + str2, new Object[0]);
        if (Build.VERSION.SDK_INT >= 19) {
            super.evaluateJavascript(str2, (ValueCallback<String>) null);
        } else {
            super.loadUrl(str2);
        }
    }

    public void setUserAgent(String str, String str2, String str3, String str4) {
        setUserAgent(str, str2, str3, str4, "");
    }

    public void setUserAgent(String str, String str2, String str3, String str4, String str5) {
        String str6 = " uniweb/%s uniweb-apk-version/%s uniweb-script-version/%s uniweb-channel/%s";
        if (!TextUtils.isEmpty(str5)) {
            str6 = " uniweb/%s uniweb-apk-version/%s uniweb-script-version/%s uniweb-channel/%s " + str5;
        }
        WebSettings settings = getSettings();
        String userAgentString = settings.getUserAgentString();
        if (TextUtils.isEmpty(str3)) {
            str3 = getAppVersionName(getContext());
        }
        String str7 = String.format(str6, str, str2, str3, str4);
        NgWebviewLog.d(TAG, "extraInfo=" + str7, new Object[0]);
        NgWebviewLog.d(TAG, "userAgent=" + userAgentString + str7, new Object[0]);
        StringBuilder sb = new StringBuilder();
        sb.append(userAgentString);
        sb.append(str7);
        settings.setUserAgentString(sb.toString());
    }

    public void setLoadingIcon(int i) {
        this.webViewIcon = i;
        View view = this.loadingView;
        if (view != null) {
            ImageView imageView = (ImageView) view.findViewById(ResIdReader.getId(getContext(), "unisdk_webview_icon_iv"));
            imageView.setImageResource(i);
            imageView.setVisibility(0);
        }
    }

    @Override // android.webkit.WebView
    public void loadUrl(String str) {
        super.loadUrl(str);
        View view = this.loadingView;
        if (view != null) {
            view.setVisibility(0);
        }
        View view2 = this.errorView;
        if (view2 != null) {
            view2.setVisibility(8);
        }
    }

    private void initPDFView(Context context) {
        if (this.mPdfViewRoot == null) {
            this.mPdfViewRoot = LayoutInflater.from(context).inflate(ResIdReader.getLayoutId(getContext(), "unisdk_webview_pdf"), (ViewGroup) null);
            this.mPdfView = this.mPdfViewRoot.findViewById(ResIdReader.getId(getContext(), "pdfView"));
        }
        this.mUniWvContainer.addView(this.mPdfViewRoot, new ViewGroup.LayoutParams(-1, -1));
    }

    private void loadingView(Context context) {
        if (-1 != this.webViewIcon && this.loadingView == null) {
            this.loadingView = LayoutInflater.from(context).inflate(ResIdReader.getLayoutId(getContext(), "unisdk_webview_loading"), (ViewGroup) null);
            ImageView imageView = (ImageView) this.loadingView.findViewById(ResIdReader.getId(getContext(), "unisdk_webview_icon_iv"));
            int i = this.webViewIcon;
            if (-1 != i) {
                imageView.setImageResource(i);
                imageView.setVisibility(0);
            } else {
                imageView.setVisibility(8);
            }
            addView(this.loadingView, new ViewGroup.LayoutParams(-1, -1));
        }
    }

    public void errorView(Context context) {
        if (-1 == this.webViewIcon) {
            return;
        }
        View view = this.loadingView;
        if (view != null) {
            view.setVisibility(8);
        }
        View view2 = this.errorView;
        if (view2 == null) {
            this.errorView = LayoutInflater.from(context).inflate(ResIdReader.getLayoutId(getContext(), "unisdk_webview_error"), (ViewGroup) null);
            ImageView imageView = (ImageView) this.errorView.findViewById(ResIdReader.getId(getContext(), "unisdk_webview_icon_iv"));
            int i = this.webViewIcon;
            if (-1 != i) {
                imageView.setImageResource(i);
                imageView.setVisibility(0);
            } else {
                imageView.setVisibility(8);
            }
            ((Button) this.errorView.findViewById(ResIdReader.getId(getContext(), "unisdk_webview_reload_btn"))).setOnClickListener(new View.OnClickListener() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView.1
                AnonymousClass1() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view3) {
                    if (UniWebView.this.loadingView != null) {
                        UniWebView.this.loadingView.setVisibility(0);
                    }
                    if (UniWebView.this.errorView != null) {
                        UniWebView.this.errorView.setVisibility(8);
                    }
                    UniWebView.this.reload();
                }
            });
            addView(this.errorView, new ViewGroup.LayoutParams(-1, -1));
            return;
        }
        view2.setVisibility(0);
    }

    /* renamed from: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView$1 */
    class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view3) {
            if (UniWebView.this.loadingView != null) {
                UniWebView.this.loadingView.setVisibility(0);
            }
            if (UniWebView.this.errorView != null) {
                UniWebView.this.errorView.setVisibility(8);
            }
            UniWebView.this.reload();
        }
    }

    private void initWebView() {
        WebSettings settings = getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setDomStorageEnabled(true);
        settings.setGeolocationEnabled(true);
        if (Build.VERSION.SDK_INT >= 17) {
            settings.setMediaPlaybackRequiresUserGesture(false);
        }
        settings.setPluginState(WebSettings.PluginState.ON);
        if (Build.VERSION.SDK_INT >= 21) {
            settings.setMixedContentMode(0);
        }
        setWebViewClient(new WebViewClient() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView.2
            AnonymousClass2() {
            }

            @Override // android.webkit.WebViewClient
            public WebResourceResponse shouldInterceptRequest(WebView webView, String str) throws JSONException {
                NgWebviewLog.d(UniWebView.TAG, "shouldInterceptRequest url:" + str, new Object[0]);
                String str2 = "nativerequest/";
                if (str.startsWith("http://")) {
                    str2 = "http://nativerequest/";
                } else if (str.startsWith("https://")) {
                    str2 = "https://nativerequest/";
                }
                if (str.startsWith(str2)) {
                    NgWebviewLog.d(UniWebView.TAG, "WebResourceResponse url:" + str, new Object[0]);
                    String strDecode = URLDecoder.decode(str.substring(str2.length()));
                    NgWebviewLog.d(UniWebView.TAG, "decodeUrl url:" + str, new Object[0]);
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
                        NgWebviewLog.e(UniWebView.TAG, "shouldInterceptRequest exception:" + e.getMessage());
                    }
                }
                return super.shouldInterceptRequest(webView, str);
            }

            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                NgWebviewLog.d(UniWebView.TAG, "shouldOverrideUrlLoading url:" + str, new Object[0]);
                return UniWebView.this.interceptUrl(str);
            }

            @Override // android.webkit.WebViewClient
            public void onPageFinished(WebView webView, String str) throws JSONException {
                super.onPageFinished(webView, str);
                NgWebviewLog.d(UniWebView.TAG, "onPageFinished", new Object[0]);
                if (UniWebView.this.loadingView != null) {
                    UniWebView.this.loadingView.setVisibility(8);
                }
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.put("func", "goBack");
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put("result", UniWebView.this.canGoBack());
                    jSONObject.put("params", jSONObject2);
                } catch (JSONException e) {
                    NgWebviewLog.d(UniWebView.TAG, "JSONException:" + e.getMessage(), new Object[0]);
                }
                UniWebView.this.runCallback(jSONObject.toString(), jSONObject.optString("callback"));
                UniWebView uniWebView = UniWebView.this;
                StringBuilder sb = new StringBuilder(uniWebView.getJS(uniWebView.myCtx, UniWebView.NTWKJSBridgeJS));
                if (Build.VERSION.SDK_INT > 19) {
                    UniWebView.this.evaluateJavascript("typeof(window.postMsgToNative)", new ValueCallback<String>() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView.2.1
                        final /* synthetic */ StringBuilder val$builder;

                        AnonymousClass1(StringBuilder sb2) {
                            sb = sb2;
                        }

                        @Override // android.webkit.ValueCallback
                        public void onReceiveValue(String str2) {
                            NgWebviewLog.i("onPageFinished onReceiveValue", str2);
                            if ("function".equals(str2)) {
                                return;
                            }
                            if (Build.VERSION.SDK_INT <= 19) {
                                UniWebView.this.loadUrl("javascript:" + sb.toString());
                                return;
                            }
                            UniWebView.this.evaluateJavascript(sb.toString(), new ValueCallback<String>() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView.2.1.1
                                C00801() {
                                }

                                @Override // android.webkit.ValueCallback
                                public void onReceiveValue(String str3) {
                                    NgWebviewLog.i("onPageStarted onReceiveValue", str3);
                                }
                            });
                        }

                        /* renamed from: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView$2$1$1 */
                        class C00801 implements ValueCallback<String> {
                            C00801() {
                            }

                            @Override // android.webkit.ValueCallback
                            public void onReceiveValue(String str3) {
                                NgWebviewLog.i("onPageStarted onReceiveValue", str3);
                            }
                        }
                    });
                }
            }

            /* renamed from: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView$2$1 */
            class AnonymousClass1 implements ValueCallback<String> {
                final /* synthetic */ StringBuilder val$builder;

                AnonymousClass1(StringBuilder sb2) {
                    sb = sb2;
                }

                @Override // android.webkit.ValueCallback
                public void onReceiveValue(String str2) {
                    NgWebviewLog.i("onPageFinished onReceiveValue", str2);
                    if ("function".equals(str2)) {
                        return;
                    }
                    if (Build.VERSION.SDK_INT <= 19) {
                        UniWebView.this.loadUrl("javascript:" + sb.toString());
                        return;
                    }
                    UniWebView.this.evaluateJavascript(sb.toString(), new ValueCallback<String>() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView.2.1.1
                        C00801() {
                        }

                        @Override // android.webkit.ValueCallback
                        public void onReceiveValue(String str3) {
                            NgWebviewLog.i("onPageStarted onReceiveValue", str3);
                        }
                    });
                }

                /* renamed from: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView$2$1$1 */
                class C00801 implements ValueCallback<String> {
                    C00801() {
                    }

                    @Override // android.webkit.ValueCallback
                    public void onReceiveValue(String str3) {
                        NgWebviewLog.i("onPageStarted onReceiveValue", str3);
                    }
                }
            }

            @Override // android.webkit.WebViewClient
            public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                super.onPageStarted(webView, str, bitmap);
                UniWebView uniWebView = UniWebView.this;
                StringBuilder sb = new StringBuilder(uniWebView.getJS(uniWebView.myCtx, UniWebView.NTWKJSBridgeJS));
                if (Build.VERSION.SDK_INT <= 19) {
                    UniWebView.this.loadUrl("javascript:" + sb.toString());
                    return;
                }
                UniWebView.this.evaluateJavascript(sb.toString(), new ValueCallback<String>() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView.2.2
                    C00812() {
                    }

                    @Override // android.webkit.ValueCallback
                    public void onReceiveValue(String str2) {
                        NgWebviewLog.i("onPageStarted onReceiveValue", str2);
                    }
                });
            }

            /* renamed from: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView$2$2 */
            class C00812 implements ValueCallback<String> {
                C00812() {
                }

                @Override // android.webkit.ValueCallback
                public void onReceiveValue(String str2) {
                    NgWebviewLog.i("onPageStarted onReceiveValue", str2);
                }
            }

            @Override // android.webkit.WebViewClient
            public void onReceivedError(WebView webView, int i, String str, String str2) {
                super.onReceivedError(webView, i, str, str2);
                NgWebviewLog.d(UniWebView.TAG, "errorCode:" + i + ",description:" + str + ",failingUrl:" + str2, new Object[0]);
                if (str2.startsWith(UniWebView.UNIWEB_PREFIX) || str2.startsWith("http://nativerequest/") || str2.startsWith("https://nativerequest/")) {
                    return;
                }
                NgWebviewLog.d(UniWebView.TAG, "errorView...", new Object[0]);
                UniWebView uniWebView = UniWebView.this;
                uniWebView.errorView(uniWebView.getContext());
            }

            @Override // android.webkit.WebViewClient
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                super.onReceivedError(webView, webResourceRequest, webResourceError);
            }

            @Override // android.webkit.WebViewClient
            public void onReceivedHttpError(WebView webView, WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) throws JSONException {
                super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);
                int statusCode = webResourceResponse.getStatusCode();
                NgWebviewLog.d(UniWebView.TAG, "onReceivedHttpError code = " + statusCode, new Object[0]);
                if (404 == statusCode || 500 == statusCode) {
                    JSONObject jSONObject = new JSONObject();
                    try {
                        jSONObject.put("func", Const.ON_PAGE_ERROR);
                        UniWebView.this.runCallback(jSONObject.toString(), "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        setWebChromeClient(new WebChromeClient() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView.3
            private WebChromeClient.CustomViewCallback mCustomViewCallback;

            AnonymousClass3() {
            }

            @Override // android.webkit.WebChromeClient
            public void onShowCustomView(View view, WebChromeClient.CustomViewCallback customViewCallback) {
                NgWebviewLog.d(UniWebView.TAG, " onShowCustomView", new Object[0]);
                UniWebView.this.act.mFullscreenVideoViewShowing = true;
                view.setVisibility(0);
                NgWebviewLog.d(UniWebView.TAG, "isViewVisibility : " + view.getVisibility(), new Object[0]);
                NgWebviewLog.d(UniWebView.TAG, "videoView width : " + view.getWidth(), new Object[0]);
                NgWebviewLog.d(UniWebView.TAG, "videoView height : " + view.getHeight(), new Object[0]);
                UniWebView.this.mContentView.setVisibility(8);
                UniWebView.this.flVideoContainer.setVisibility(0);
                UniWebView.this.flVideoContainer.addView(view, new ViewGroup.LayoutParams(-1, -1));
                this.mCustomViewCallback = customViewCallback;
                super.onShowCustomView(view, customViewCallback);
            }

            @Override // android.webkit.WebChromeClient
            public void onHideCustomView() {
                NgWebviewLog.d(UniWebView.TAG, " onHideCustomView", new Object[0]);
                UniWebView.this.act.mFullscreenVideoViewShowing = false;
                UniWebView.this.mContentView.setVisibility(0);
                UniWebView.this.flVideoContainer.setVisibility(8);
                UniWebView.this.flVideoContainer.removeAllViews();
                super.onHideCustomView();
            }

            public void openFileChooser(ValueCallback<Uri> valueCallback, String str, String str2) {
                UniWebView.this.mUploadMessage = valueCallback;
                UniWebView.this.showFileChooser();
            }

            @Override // android.webkit.WebChromeClient
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                UniWebView.this.mUploadCallBackAboveL = valueCallback;
                UniWebView.this.showFileChooser();
                return true;
            }

            /* renamed from: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView$3$1 */
            class AnonymousClass1 implements RequestPermissionCallBack {
                final /* synthetic */ GeolocationPermissions.Callback val$callback;
                final /* synthetic */ String val$origin;

                AnonymousClass1(GeolocationPermissions.Callback callback, String str) {
                    callback = callback;
                    str = str;
                }

                @Override // com.netease.ntunisdk.modules.ngwebviewgeneral.callback.RequestPermissionCallBack
                public void result(boolean z) {
                    if (z) {
                        callback.invoke(str, true, false);
                    }
                }
            }

            @Override // android.webkit.WebChromeClient
            public void onGeolocationPermissionsShowPrompt(String str, GeolocationPermissions.Callback callback) throws JSONException {
                try {
                    UniWebView.this.act.setRPCallBack(new RequestPermissionCallBack() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView.3.1
                        final /* synthetic */ GeolocationPermissions.Callback val$callback;
                        final /* synthetic */ String val$origin;

                        AnonymousClass1(GeolocationPermissions.Callback callback2, String str2) {
                            callback = callback2;
                            str = str2;
                        }

                        @Override // com.netease.ntunisdk.modules.ngwebviewgeneral.callback.RequestPermissionCallBack
                        public void result(boolean z) {
                            if (z) {
                                callback.invoke(str, true, false);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.put("methodId", "requestPermission");
                    jSONObject.put("permissionName", "android.permission.ACCESS_COARSE_LOCATION,android.permission.ACCESS_FINE_LOCATION");
                    jSONObject.put("firstText", UniWebView.this.getResources().getString(R.string.ng_wv_position_description));
                    jSONObject.put("retryText", UniWebView.this.getResources().getString(R.string.ng_wv_position_description));
                    jSONObject.put("positiveText", UniWebView.this.getResources().getString(R.string.ng_wv_continue));
                    jSONObject.put("negativeText", UniWebView.this.getResources().getString(R.string.ng_wv_cancel));
                    jSONObject.put("firstTwoBtn", "true");
                    jSONObject.put("shouldRetry", Constants.CASEFIRST_FALSE);
                    jSONObject.put("showDialog", "true");
                    jSONObject.put("gotoSetting", "true");
                    jSONObject.put("gotoSettingReason", UniWebView.this.getResources().getString(R.string.ng_wv_open_position_tips));
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
                ModulesManager.getInst().extendFunc("ngWebViewGeneral", PermissionConstant.PERMISSION_KEY, jSONObject.toString());
                NgWebviewLog.d(UniWebView.TAG, " onGeolocationPermissionsShowPrompt...", new Object[0]);
            }
        });
        requestFocus();
    }

    /* renamed from: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView$2 */
    class AnonymousClass2 extends WebViewClient {
        AnonymousClass2() {
        }

        @Override // android.webkit.WebViewClient
        public WebResourceResponse shouldInterceptRequest(WebView webView, String str) throws JSONException {
            NgWebviewLog.d(UniWebView.TAG, "shouldInterceptRequest url:" + str, new Object[0]);
            String str2 = "nativerequest/";
            if (str.startsWith("http://")) {
                str2 = "http://nativerequest/";
            } else if (str.startsWith("https://")) {
                str2 = "https://nativerequest/";
            }
            if (str.startsWith(str2)) {
                NgWebviewLog.d(UniWebView.TAG, "WebResourceResponse url:" + str, new Object[0]);
                String strDecode = URLDecoder.decode(str.substring(str2.length()));
                NgWebviewLog.d(UniWebView.TAG, "decodeUrl url:" + str, new Object[0]);
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
                    NgWebviewLog.e(UniWebView.TAG, "shouldInterceptRequest exception:" + e.getMessage());
                }
            }
            return super.shouldInterceptRequest(webView, str);
        }

        @Override // android.webkit.WebViewClient
        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            NgWebviewLog.d(UniWebView.TAG, "shouldOverrideUrlLoading url:" + str, new Object[0]);
            return UniWebView.this.interceptUrl(str);
        }

        @Override // android.webkit.WebViewClient
        public void onPageFinished(WebView webView, String str) throws JSONException {
            super.onPageFinished(webView, str);
            NgWebviewLog.d(UniWebView.TAG, "onPageFinished", new Object[0]);
            if (UniWebView.this.loadingView != null) {
                UniWebView.this.loadingView.setVisibility(8);
            }
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("func", "goBack");
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("result", UniWebView.this.canGoBack());
                jSONObject.put("params", jSONObject2);
            } catch (JSONException e) {
                NgWebviewLog.d(UniWebView.TAG, "JSONException:" + e.getMessage(), new Object[0]);
            }
            UniWebView.this.runCallback(jSONObject.toString(), jSONObject.optString("callback"));
            UniWebView uniWebView = UniWebView.this;
            StringBuilder sb2 = new StringBuilder(uniWebView.getJS(uniWebView.myCtx, UniWebView.NTWKJSBridgeJS));
            if (Build.VERSION.SDK_INT > 19) {
                UniWebView.this.evaluateJavascript("typeof(window.postMsgToNative)", new ValueCallback<String>() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView.2.1
                    final /* synthetic */ StringBuilder val$builder;

                    AnonymousClass1(StringBuilder sb22) {
                        sb = sb22;
                    }

                    @Override // android.webkit.ValueCallback
                    public void onReceiveValue(String str2) {
                        NgWebviewLog.i("onPageFinished onReceiveValue", str2);
                        if ("function".equals(str2)) {
                            return;
                        }
                        if (Build.VERSION.SDK_INT <= 19) {
                            UniWebView.this.loadUrl("javascript:" + sb.toString());
                            return;
                        }
                        UniWebView.this.evaluateJavascript(sb.toString(), new ValueCallback<String>() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView.2.1.1
                            C00801() {
                            }

                            @Override // android.webkit.ValueCallback
                            public void onReceiveValue(String str3) {
                                NgWebviewLog.i("onPageStarted onReceiveValue", str3);
                            }
                        });
                    }

                    /* renamed from: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView$2$1$1 */
                    class C00801 implements ValueCallback<String> {
                        C00801() {
                        }

                        @Override // android.webkit.ValueCallback
                        public void onReceiveValue(String str3) {
                            NgWebviewLog.i("onPageStarted onReceiveValue", str3);
                        }
                    }
                });
            }
        }

        /* renamed from: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView$2$1 */
        class AnonymousClass1 implements ValueCallback<String> {
            final /* synthetic */ StringBuilder val$builder;

            AnonymousClass1(StringBuilder sb22) {
                sb = sb22;
            }

            @Override // android.webkit.ValueCallback
            public void onReceiveValue(String str2) {
                NgWebviewLog.i("onPageFinished onReceiveValue", str2);
                if ("function".equals(str2)) {
                    return;
                }
                if (Build.VERSION.SDK_INT <= 19) {
                    UniWebView.this.loadUrl("javascript:" + sb.toString());
                    return;
                }
                UniWebView.this.evaluateJavascript(sb.toString(), new ValueCallback<String>() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView.2.1.1
                    C00801() {
                    }

                    @Override // android.webkit.ValueCallback
                    public void onReceiveValue(String str3) {
                        NgWebviewLog.i("onPageStarted onReceiveValue", str3);
                    }
                });
            }

            /* renamed from: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView$2$1$1 */
            class C00801 implements ValueCallback<String> {
                C00801() {
                }

                @Override // android.webkit.ValueCallback
                public void onReceiveValue(String str3) {
                    NgWebviewLog.i("onPageStarted onReceiveValue", str3);
                }
            }
        }

        @Override // android.webkit.WebViewClient
        public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            super.onPageStarted(webView, str, bitmap);
            UniWebView uniWebView = UniWebView.this;
            StringBuilder sb = new StringBuilder(uniWebView.getJS(uniWebView.myCtx, UniWebView.NTWKJSBridgeJS));
            if (Build.VERSION.SDK_INT <= 19) {
                UniWebView.this.loadUrl("javascript:" + sb.toString());
                return;
            }
            UniWebView.this.evaluateJavascript(sb.toString(), new ValueCallback<String>() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView.2.2
                C00812() {
                }

                @Override // android.webkit.ValueCallback
                public void onReceiveValue(String str2) {
                    NgWebviewLog.i("onPageStarted onReceiveValue", str2);
                }
            });
        }

        /* renamed from: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView$2$2 */
        class C00812 implements ValueCallback<String> {
            C00812() {
            }

            @Override // android.webkit.ValueCallback
            public void onReceiveValue(String str2) {
                NgWebviewLog.i("onPageStarted onReceiveValue", str2);
            }
        }

        @Override // android.webkit.WebViewClient
        public void onReceivedError(WebView webView, int i, String str, String str2) {
            super.onReceivedError(webView, i, str, str2);
            NgWebviewLog.d(UniWebView.TAG, "errorCode:" + i + ",description:" + str + ",failingUrl:" + str2, new Object[0]);
            if (str2.startsWith(UniWebView.UNIWEB_PREFIX) || str2.startsWith("http://nativerequest/") || str2.startsWith("https://nativerequest/")) {
                return;
            }
            NgWebviewLog.d(UniWebView.TAG, "errorView...", new Object[0]);
            UniWebView uniWebView = UniWebView.this;
            uniWebView.errorView(uniWebView.getContext());
        }

        @Override // android.webkit.WebViewClient
        public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
            super.onReceivedError(webView, webResourceRequest, webResourceError);
        }

        @Override // android.webkit.WebViewClient
        public void onReceivedHttpError(WebView webView, WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) throws JSONException {
            super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);
            int statusCode = webResourceResponse.getStatusCode();
            NgWebviewLog.d(UniWebView.TAG, "onReceivedHttpError code = " + statusCode, new Object[0]);
            if (404 == statusCode || 500 == statusCode) {
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.put("func", Const.ON_PAGE_ERROR);
                    UniWebView.this.runCallback(jSONObject.toString(), "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* renamed from: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView$3 */
    class AnonymousClass3 extends WebChromeClient {
        private WebChromeClient.CustomViewCallback mCustomViewCallback;

        AnonymousClass3() {
        }

        @Override // android.webkit.WebChromeClient
        public void onShowCustomView(View view, WebChromeClient.CustomViewCallback customViewCallback) {
            NgWebviewLog.d(UniWebView.TAG, " onShowCustomView", new Object[0]);
            UniWebView.this.act.mFullscreenVideoViewShowing = true;
            view.setVisibility(0);
            NgWebviewLog.d(UniWebView.TAG, "isViewVisibility : " + view.getVisibility(), new Object[0]);
            NgWebviewLog.d(UniWebView.TAG, "videoView width : " + view.getWidth(), new Object[0]);
            NgWebviewLog.d(UniWebView.TAG, "videoView height : " + view.getHeight(), new Object[0]);
            UniWebView.this.mContentView.setVisibility(8);
            UniWebView.this.flVideoContainer.setVisibility(0);
            UniWebView.this.flVideoContainer.addView(view, new ViewGroup.LayoutParams(-1, -1));
            this.mCustomViewCallback = customViewCallback;
            super.onShowCustomView(view, customViewCallback);
        }

        @Override // android.webkit.WebChromeClient
        public void onHideCustomView() {
            NgWebviewLog.d(UniWebView.TAG, " onHideCustomView", new Object[0]);
            UniWebView.this.act.mFullscreenVideoViewShowing = false;
            UniWebView.this.mContentView.setVisibility(0);
            UniWebView.this.flVideoContainer.setVisibility(8);
            UniWebView.this.flVideoContainer.removeAllViews();
            super.onHideCustomView();
        }

        public void openFileChooser(ValueCallback<Uri> valueCallback, String str, String str2) {
            UniWebView.this.mUploadMessage = valueCallback;
            UniWebView.this.showFileChooser();
        }

        @Override // android.webkit.WebChromeClient
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, WebChromeClient.FileChooserParams fileChooserParams) {
            UniWebView.this.mUploadCallBackAboveL = valueCallback;
            UniWebView.this.showFileChooser();
            return true;
        }

        /* renamed from: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView$3$1 */
        class AnonymousClass1 implements RequestPermissionCallBack {
            final /* synthetic */ GeolocationPermissions.Callback val$callback;
            final /* synthetic */ String val$origin;

            AnonymousClass1(GeolocationPermissions.Callback callback2, String str2) {
                callback = callback2;
                str = str2;
            }

            @Override // com.netease.ntunisdk.modules.ngwebviewgeneral.callback.RequestPermissionCallBack
            public void result(boolean z) {
                if (z) {
                    callback.invoke(str, true, false);
                }
            }
        }

        @Override // android.webkit.WebChromeClient
        public void onGeolocationPermissionsShowPrompt(String str2, GeolocationPermissions.Callback callback2) throws JSONException {
            try {
                UniWebView.this.act.setRPCallBack(new RequestPermissionCallBack() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView.3.1
                    final /* synthetic */ GeolocationPermissions.Callback val$callback;
                    final /* synthetic */ String val$origin;

                    AnonymousClass1(GeolocationPermissions.Callback callback22, String str22) {
                        callback = callback22;
                        str = str22;
                    }

                    @Override // com.netease.ntunisdk.modules.ngwebviewgeneral.callback.RequestPermissionCallBack
                    public void result(boolean z) {
                        if (z) {
                            callback.invoke(str, true, false);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("methodId", "requestPermission");
                jSONObject.put("permissionName", "android.permission.ACCESS_COARSE_LOCATION,android.permission.ACCESS_FINE_LOCATION");
                jSONObject.put("firstText", UniWebView.this.getResources().getString(R.string.ng_wv_position_description));
                jSONObject.put("retryText", UniWebView.this.getResources().getString(R.string.ng_wv_position_description));
                jSONObject.put("positiveText", UniWebView.this.getResources().getString(R.string.ng_wv_continue));
                jSONObject.put("negativeText", UniWebView.this.getResources().getString(R.string.ng_wv_cancel));
                jSONObject.put("firstTwoBtn", "true");
                jSONObject.put("shouldRetry", Constants.CASEFIRST_FALSE);
                jSONObject.put("showDialog", "true");
                jSONObject.put("gotoSetting", "true");
                jSONObject.put("gotoSettingReason", UniWebView.this.getResources().getString(R.string.ng_wv_open_position_tips));
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
            ModulesManager.getInst().extendFunc("ngWebViewGeneral", PermissionConstant.PERMISSION_KEY, jSONObject.toString());
            NgWebviewLog.d(UniWebView.TAG, " onGeolocationPermissionsShowPrompt...", new Object[0]);
        }
    }

    public void showFileChooser() {
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setType("image/*");
        Activity activityFromView = getActivityFromView(this);
        if (activityFromView != null) {
            HookManager.startActivityForResult(activityFromView, intent, REQUEST_CODE_FILE_CHOOSER);
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
        NgWebviewLog.d(TAG, "evaluateJavascript method:" + str + ",params:" + str2, new Object[0]);
        if (!TextUtils.isEmpty(str)) {
            String str3 = "javascript:" + str + "()";
            if (!TextUtils.isEmpty(str2)) {
                str3 = "javascript:" + str + "(" + str2 + ")";
            }
            if (Build.VERSION.SDK_INT >= 19) {
                super.evaluateJavascript(str3, new ValueCallback<String>() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView.4
                    AnonymousClass4() {
                    }

                    @Override // android.webkit.ValueCallback
                    public void onReceiveValue(String str4) {
                        NgWebviewLog.d(UniWebView.TAG, "value:" + str4, new Object[0]);
                    }
                });
                return;
            } else {
                super.loadUrl(str3);
                return;
            }
        }
        NgWebviewLog.e(TAG, "evaluateJavascript method null");
    }

    /* renamed from: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView$4 */
    class AnonymousClass4 implements ValueCallback<String> {
        AnonymousClass4() {
        }

        @Override // android.webkit.ValueCallback
        public void onReceiveValue(String str4) {
            NgWebviewLog.d(UniWebView.TAG, "value:" + str4, new Object[0]);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:138:0x007f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:144:0x0089 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:156:? A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String getJS(android.content.Context r7, java.lang.String r8) throws java.lang.Throwable {
        /*
            r6 = this;
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "getJS, fileName="
            r0.append(r1)
            r0.append(r8)
            java.lang.String r0 = r0.toString()
            r1 = 0
            java.lang.Object[] r2 = new java.lang.Object[r1]
            java.lang.String r3 = "UniSDK UniWebView"
            com.netease.ntunisdk.modules.ngwebviewgeneral.log.NgWebviewLog.d(r3, r0, r2)
            r0 = 0
            android.content.res.AssetManager r7 = r7.getAssets()     // Catch: java.lang.Throwable -> L5c java.io.IOException -> L61
            java.io.InputStream r7 = r7.open(r8)     // Catch: java.lang.Throwable -> L5c java.io.IOException -> L61
            java.io.ByteArrayOutputStream r8 = new java.io.ByteArrayOutputStream     // Catch: java.lang.Throwable -> L54 java.io.IOException -> L59
            r8.<init>()     // Catch: java.lang.Throwable -> L54 java.io.IOException -> L59
            r2 = 2048(0x800, float:2.87E-42)
            byte[] r2 = new byte[r2]     // Catch: java.io.IOException -> L52 java.lang.Throwable -> L7c
        L2b:
            int r3 = r7.read(r2)     // Catch: java.io.IOException -> L52 java.lang.Throwable -> L7c
            r4 = -1
            if (r3 == r4) goto L36
            r8.write(r2, r1, r3)     // Catch: java.io.IOException -> L52 java.lang.Throwable -> L7c
            goto L2b
        L36:
            java.lang.String r1 = new java.lang.String     // Catch: java.io.IOException -> L52 java.lang.Throwable -> L7c
            byte[] r2 = r8.toByteArray()     // Catch: java.io.IOException -> L52 java.lang.Throwable -> L7c
            r1.<init>(r2)     // Catch: java.io.IOException -> L52 java.lang.Throwable -> L7c
            r8.close()     // Catch: java.io.IOException -> L43
            goto L47
        L43:
            r8 = move-exception
            r8.printStackTrace()
        L47:
            if (r7 == 0) goto L51
            r7.close()     // Catch: java.io.IOException -> L4d
            goto L51
        L4d:
            r7 = move-exception
            r7.printStackTrace()
        L51:
            return r1
        L52:
            r1 = move-exception
            goto L64
        L54:
            r8 = move-exception
            r5 = r0
            r0 = r8
            r8 = r5
            goto L7d
        L59:
            r1 = move-exception
            r8 = r0
            goto L64
        L5c:
            r7 = move-exception
            r8 = r0
            r0 = r7
            r7 = r8
            goto L7d
        L61:
            r1 = move-exception
            r7 = r0
            r8 = r7
        L64:
            r1.printStackTrace()     // Catch: java.lang.Throwable -> L7c
            if (r8 == 0) goto L71
            r8.close()     // Catch: java.io.IOException -> L6d
            goto L71
        L6d:
            r8 = move-exception
            r8.printStackTrace()
        L71:
            if (r7 == 0) goto L7b
            r7.close()     // Catch: java.io.IOException -> L77
            goto L7b
        L77:
            r7 = move-exception
            r7.printStackTrace()
        L7b:
            return r0
        L7c:
            r0 = move-exception
        L7d:
            if (r8 == 0) goto L87
            r8.close()     // Catch: java.io.IOException -> L83
            goto L87
        L83:
            r8 = move-exception
            r8.printStackTrace()
        L87:
            if (r7 == 0) goto L91
            r7.close()     // Catch: java.io.IOException -> L8d
            goto L91
        L8d:
            r7 = move-exception
            r7.printStackTrace()
        L91:
            goto L93
        L92:
            throw r0
        L93:
            goto L92
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView.getJS(android.content.Context, java.lang.String):java.lang.String");
    }

    @Override // android.webkit.WebView
    public boolean canGoBack() {
        return super.canGoBack();
    }

    @Override // android.webkit.WebView
    public void goBack() {
        super.goBack();
    }

    public boolean interceptUrl(String str) throws JSONException, Resources.NotFoundException, URISyntaxException {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        String strValueOf = String.valueOf(new char[]{'w', 'e', 'i', 'x', 'i', 'n', ':', '/', '/', 'w', 'a', 'p', '/', 'p', 'a', 'y', '?'});
        if (str.startsWith(UNIWEB_PREFIX)) {
            NgWebviewLog.d(TAG, "interceptUrl url:" + str, new Object[0]);
            String strDecode = URLDecoder.decode(str.substring(9));
            NgWebviewLog.d(TAG, "decodeUrl url:" + strDecode, new Object[0]);
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject = new JSONObject(strDecode);
            } catch (JSONException e) {
                NgWebviewLog.d(TAG, "JSONException:" + e.getMessage(), new Object[0]);
            }
            if (ACTION_GETNETWORKTYPE.equalsIgnoreCase(jSONObject.optString("func"))) {
                getNetworkType(jSONObject.optString("callback"));
            } else {
                runCallback(jSONObject.toString(), jSONObject.optString("callback"));
            }
        } else if (str.startsWith(strValueOf)) {
            if (isWxInstall()) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(str));
                HookManager.startActivity(this.myCtx, intent);
                return true;
            }
            String string = getResources().getString(R.string.ng_wv_wechat_not_installed);
            if (!TextUtils.isEmpty(string)) {
                Toast.makeText(this.myCtx, string, 0).show();
            }
            goBack();
        } else {
            if (str.startsWith("alipays://platformapi/startApp?")) {
                if (isAliPayInstalled()) {
                    startAlipayActivity(str);
                } else {
                    String string2 = getResources().getString(R.string.ng_wv_alipay_not_installed);
                    if (!TextUtils.isEmpty(string2)) {
                        Toast.makeText(this.myCtx, string2, 0).show();
                    }
                }
                return true;
            }
            if (str.startsWith(IDV_PREFIX) || str.startsWith(UNI_JSBRIDGE_PREFIX)) {
                JSONObject jSONObject2 = new JSONObject();
                try {
                    jSONObject2.put("func", "interceptUrl");
                    jSONObject2.put("params", str);
                    runCallback(jSONObject2.toString(), "");
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
                return true;
            }
            if (str.startsWith("intent://")) {
                NgWebviewLog.d(TAG, "interceptUrl: url.startsWith(\"intent://\")", new Object[0]);
                try {
                    Intent uri = Intent.parseUri(str, 1);
                    if (uri != null) {
                        if (this.myCtx.getPackageManager().resolveActivity(uri, 65536) != null) {
                            HookManager.startActivity(this.myCtx, uri);
                        } else {
                            if (!uri.getScheme().equals("https") && !uri.getScheme().equals("http")) {
                                return false;
                            }
                            loadUrl(uri.getStringExtra("browser_fallback_url"));
                        }
                        return true;
                    }
                } catch (Exception unused) {
                }
                return false;
            }
            if (!str.startsWith("http")) {
                Intent intent2 = new Intent("android.intent.action.VIEW", Uri.parse(str));
                intent2.setFlags(805306368);
                try {
                    HookManager.startActivity(this.myCtx, intent2);
                } catch (Exception e3) {
                    e3.printStackTrace();
                    NgWebviewLog.d(TAG, "startActivity failed with url=" + str, new Object[0]);
                    String string3 = getResources().getString(R.string.ng_wv_app_not_installed);
                    if (!TextUtils.isEmpty(string3)) {
                        Toast.makeText(this.myCtx, string3, 0).show();
                    }
                }
                return true;
            }
            if (str.endsWith(".pdf") && this.isHasPdfView) {
                NgWebviewLog.d(TAG, "open pdf with url=" + str, new Object[0]);
                String str2 = getContext().getCacheDir() + str.substring(str.lastIndexOf("/"));
                NgWebviewLog.d(TAG, "filePath = " + str2, new Object[0]);
                if (fileIsExists(str2)) {
                    NgWebviewLog.d(TAG, "filePath exist", new Object[0]);
                    this.mPdfView.fromUri(getFileUri(str2)).enableSwipe(true).defaultPage(0).onLoad(new OnLoadCompleteListener() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView.5
                        AnonymousClass5() {
                        }

                        public void loadComplete(int i) {
                            NgWebviewLog.d(UniWebView.TAG, "PdfView loadComplete", new Object[0]);
                        }
                    }).load();
                    this.mPdfViewRoot.bringToFront();
                    this.mPdfViewRoot.setVisibility(0);
                    this.mUniWvContainer.findViewById(ResIdReader.getId(getContext(), "ngwebview_close")).bringToFront();
                    setVisibility(8);
                } else {
                    NgWebviewLog.d(TAG, "filePath no exist", new Object[0]);
                    this.mUniWvContainer.findViewById(ResIdReader.getId(getContext(), "qst_loading_view")).bringToFront();
                    this.mUniWvContainer.findViewById(ResIdReader.getId(getContext(), "qst_loading_view")).setVisibility(0);
                    DownloadUtil.download(str, getContext().getCacheDir() + str.substring(str.lastIndexOf("/")), new DownloadUtil.OnDownloadListener() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView.6
                        final /* synthetic */ String val$filePath;

                        AnonymousClass6(String str22) {
                            str = str22;
                        }

                        @Override // com.netease.ntunisdk.modules.ngwebviewgeneral.util.DownloadUtil.OnDownloadListener
                        public void onDownloadSuccess(String str3) {
                            NgWebviewLog.d("MainActivity", "onDownloadSuccess: " + str3, new Object[0]);
                            ((Activity) UniWebView.this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView.6.1
                                AnonymousClass1() {
                                }

                                /* renamed from: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView$6$1$1 */
                                class C00821 implements OnLoadCompleteListener {
                                    C00821() {
                                    }

                                    public void loadComplete(int i) {
                                        NgWebviewLog.d(UniWebView.TAG, "PdfView loadComplete", new Object[0]);
                                    }
                                }

                                @Override // java.lang.Runnable
                                public void run() {
                                    UniWebView.this.mUniWvContainer.findViewById(ResIdReader.getId(UniWebView.this.getContext(), "qst_loading_view")).setVisibility(8);
                                    UniWebView.this.mPdfView.fromUri(UniWebView.this.getFileUri(str)).enableSwipe(true).defaultPage(0).onLoad(new OnLoadCompleteListener() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView.6.1.1
                                        C00821() {
                                        }

                                        public void loadComplete(int i) {
                                            NgWebviewLog.d(UniWebView.TAG, "PdfView loadComplete", new Object[0]);
                                        }
                                    }).load();
                                    UniWebView.this.mPdfViewRoot.bringToFront();
                                    UniWebView.this.mPdfViewRoot.setVisibility(0);
                                    UniWebView.this.mUniWvContainer.findViewById(ResIdReader.getId(UniWebView.this.getContext(), "ngwebview_close")).bringToFront();
                                    UniWebView.this.setVisibility(8);
                                }
                            });
                        }

                        /* renamed from: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView$6$1 */
                        class AnonymousClass1 implements Runnable {
                            AnonymousClass1() {
                            }

                            /* renamed from: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView$6$1$1 */
                            class C00821 implements OnLoadCompleteListener {
                                C00821() {
                                }

                                public void loadComplete(int i) {
                                    NgWebviewLog.d(UniWebView.TAG, "PdfView loadComplete", new Object[0]);
                                }
                            }

                            @Override // java.lang.Runnable
                            public void run() {
                                UniWebView.this.mUniWvContainer.findViewById(ResIdReader.getId(UniWebView.this.getContext(), "qst_loading_view")).setVisibility(8);
                                UniWebView.this.mPdfView.fromUri(UniWebView.this.getFileUri(str)).enableSwipe(true).defaultPage(0).onLoad(new OnLoadCompleteListener() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView.6.1.1
                                    C00821() {
                                    }

                                    public void loadComplete(int i) {
                                        NgWebviewLog.d(UniWebView.TAG, "PdfView loadComplete", new Object[0]);
                                    }
                                }).load();
                                UniWebView.this.mPdfViewRoot.bringToFront();
                                UniWebView.this.mPdfViewRoot.setVisibility(0);
                                UniWebView.this.mUniWvContainer.findViewById(ResIdReader.getId(UniWebView.this.getContext(), "ngwebview_close")).bringToFront();
                                UniWebView.this.setVisibility(8);
                            }
                        }

                        @Override // com.netease.ntunisdk.modules.ngwebviewgeneral.util.DownloadUtil.OnDownloadListener
                        public void onDownloading(int i) {
                            NgWebviewLog.d("MainActivity", "onDownloading: " + i, new Object[0]);
                        }

                        @Override // com.netease.ntunisdk.modules.ngwebviewgeneral.util.DownloadUtil.OnDownloadListener
                        public void onDownloadFailed(String str3) {
                            NgWebviewLog.d("MainActivity", "onDownloadFailed: " + str3, new Object[0]);
                        }
                    });
                }
                return true;
            }
        }
        return false;
    }

    /* renamed from: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView$5 */
    class AnonymousClass5 implements OnLoadCompleteListener {
        AnonymousClass5() {
        }

        public void loadComplete(int i) {
            NgWebviewLog.d(UniWebView.TAG, "PdfView loadComplete", new Object[0]);
        }
    }

    /* renamed from: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView$6 */
    class AnonymousClass6 implements DownloadUtil.OnDownloadListener {
        final /* synthetic */ String val$filePath;

        AnonymousClass6(String str22) {
            str = str22;
        }

        @Override // com.netease.ntunisdk.modules.ngwebviewgeneral.util.DownloadUtil.OnDownloadListener
        public void onDownloadSuccess(String str3) {
            NgWebviewLog.d("MainActivity", "onDownloadSuccess: " + str3, new Object[0]);
            ((Activity) UniWebView.this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView.6.1
                AnonymousClass1() {
                }

                /* renamed from: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView$6$1$1 */
                class C00821 implements OnLoadCompleteListener {
                    C00821() {
                    }

                    public void loadComplete(int i) {
                        NgWebviewLog.d(UniWebView.TAG, "PdfView loadComplete", new Object[0]);
                    }
                }

                @Override // java.lang.Runnable
                public void run() {
                    UniWebView.this.mUniWvContainer.findViewById(ResIdReader.getId(UniWebView.this.getContext(), "qst_loading_view")).setVisibility(8);
                    UniWebView.this.mPdfView.fromUri(UniWebView.this.getFileUri(str)).enableSwipe(true).defaultPage(0).onLoad(new OnLoadCompleteListener() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView.6.1.1
                        C00821() {
                        }

                        public void loadComplete(int i) {
                            NgWebviewLog.d(UniWebView.TAG, "PdfView loadComplete", new Object[0]);
                        }
                    }).load();
                    UniWebView.this.mPdfViewRoot.bringToFront();
                    UniWebView.this.mPdfViewRoot.setVisibility(0);
                    UniWebView.this.mUniWvContainer.findViewById(ResIdReader.getId(UniWebView.this.getContext(), "ngwebview_close")).bringToFront();
                    UniWebView.this.setVisibility(8);
                }
            });
        }

        /* renamed from: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView$6$1 */
        class AnonymousClass1 implements Runnable {
            AnonymousClass1() {
            }

            /* renamed from: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView$6$1$1 */
            class C00821 implements OnLoadCompleteListener {
                C00821() {
                }

                public void loadComplete(int i) {
                    NgWebviewLog.d(UniWebView.TAG, "PdfView loadComplete", new Object[0]);
                }
            }

            @Override // java.lang.Runnable
            public void run() {
                UniWebView.this.mUniWvContainer.findViewById(ResIdReader.getId(UniWebView.this.getContext(), "qst_loading_view")).setVisibility(8);
                UniWebView.this.mPdfView.fromUri(UniWebView.this.getFileUri(str)).enableSwipe(true).defaultPage(0).onLoad(new OnLoadCompleteListener() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView.6.1.1
                    C00821() {
                    }

                    public void loadComplete(int i) {
                        NgWebviewLog.d(UniWebView.TAG, "PdfView loadComplete", new Object[0]);
                    }
                }).load();
                UniWebView.this.mPdfViewRoot.bringToFront();
                UniWebView.this.mPdfViewRoot.setVisibility(0);
                UniWebView.this.mUniWvContainer.findViewById(ResIdReader.getId(UniWebView.this.getContext(), "ngwebview_close")).bringToFront();
                UniWebView.this.setVisibility(8);
            }
        }

        @Override // com.netease.ntunisdk.modules.ngwebviewgeneral.util.DownloadUtil.OnDownloadListener
        public void onDownloading(int i) {
            NgWebviewLog.d("MainActivity", "onDownloading: " + i, new Object[0]);
        }

        @Override // com.netease.ntunisdk.modules.ngwebviewgeneral.util.DownloadUtil.OnDownloadListener
        public void onDownloadFailed(String str3) {
            NgWebviewLog.d("MainActivity", "onDownloadFailed: " + str3, new Object[0]);
        }
    }

    private void startAlipayActivity(String str) throws URISyntaxException {
        try {
            Intent uri = Intent.parseUri(str, 1);
            uri.addCategory("android.intent.category.BROWSABLE");
            uri.setComponent(null);
            HookManager.startActivity(this.myCtx, uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isAliPayInstalled() {
        return new Intent("android.intent.action.VIEW", Uri.parse("alipays://platformapi/startApp")).resolveActivity(this.myCtx.getPackageManager()) != null;
    }

    private boolean isWxInstall() {
        return hasPackageInstalled(this.myCtx, "com.tencent.mm");
    }

    private boolean hasPackageInstalled(Context context, String str) throws PackageManager.NameNotFoundException {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(str, 128);
            NgWebviewLog.i(TAG, "hasPackageInstalled, info=" + packageInfo);
            return packageInfo != null;
        } catch (Exception e) {
            NgWebviewLog.w(TAG, "" + e.getMessage());
            return false;
        }
    }

    private void getNetworkType(String str) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("errno", 0);
            jSONObject.put("message", "");
            JSONObject jSONObject2 = new JSONObject();
            NgWebviewActivity ngWebviewActivity = this.act;
            jSONObject2.put("result", NgWebviewActivity.getNetworkType());
            jSONObject.putOpt("data", jSONObject2);
        } catch (JSONException e) {
            NgWebviewLog.d(TAG, "getNetworkType exception:" + e.getMessage(), new Object[0]);
            e.printStackTrace();
        }
        evaluateJavascript(str, jSONObject.toString());
    }

    public void setUniWebViewCallback(UniWebViewCallback uniWebViewCallback) {
        this.callback = uniWebViewCallback;
    }

    public void runCallback(String str, String str2) {
        UniWebViewCallback uniWebViewCallback = this.callback;
        if (uniWebViewCallback != null) {
            uniWebViewCallback.callback(str, str2);
        } else {
            NgWebviewLog.e(TAG, "UniWebViewCallback null");
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
            NgWebviewLog.e(TAG, "\u8bf7\u6c42\u63d0\u4ea4\u5931\u8d25:" + str);
            return null;
        }
    }

    public static String getAppVersionName(Context context) {
        if (context != null) {
            try {
                return context.getPackageManager().getPackageInfo(NgWebViewGeneralModule.getAppPackageName(context), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public void showWebview() {
        setVisibility(0);
    }

    public void hideWebview() {
        setVisibility(8);
    }

    private boolean fileIsExists(String str) {
        try {
            return new File(str).exists();
        } catch (Exception unused) {
            return false;
        }
    }
}