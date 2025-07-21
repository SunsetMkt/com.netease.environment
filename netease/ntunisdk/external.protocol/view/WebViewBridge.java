package com.netease.ntunisdk.external.protocol.view;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import com.netease.ntunisdk.external.protocol.utils.L;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class WebViewBridge {
    private static final String TAG = "WebViewBridge";
    private Callback mConcreteCallback;
    private final Callback mDefaultCallback = new Callback() { // from class: com.netease.ntunisdk.external.protocol.view.WebViewBridge.1
        @Override // com.netease.ntunisdk.external.protocol.view.WebViewBridge.Callback
        public void callback(JSONObject jSONObject, String str) {
            if (WebViewBridge.this.mConcreteCallback != null) {
                WebViewBridge.this.mConcreteCallback.callback(jSONObject, str);
            }
        }
    };

    public interface Callback {
        void callback(JSONObject jSONObject, String str);
    }

    WebViewBridge() {
    }

    public void init(WebView webView) {
        if (webView != null) {
            webView.addJavascriptInterface(this, "AndroidJSBridge");
            webView.addJavascriptInterface(this, "$CallbackInterface");
        }
    }

    public void addWebViewCallback(Callback callback) {
        this.mConcreteCallback = callback;
    }

    @JavascriptInterface
    public void nativeCall(String str, String str2) {
        L.d(TAG, "$CallbackInterface.nativeCall, action:" + str + ", data:" + str2);
    }

    @JavascriptInterface
    public void postMsgToNative(String str) {
        L.d(TAG, "postMsgToNative, json=" + str);
        try {
            JSONObject jSONObject = new JSONObject(str);
            String strOptString = jSONObject.optString("methodId");
            JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("reqData");
            jSONObject.optString("identifier");
            if (jSONObjectOptJSONObject == null) {
                jSONObjectOptJSONObject = new JSONObject();
            }
            this.mDefaultCallback.callback(jSONObjectOptJSONObject, strOptString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Callback getDefaultCallback() {
        return this.mDefaultCallback;
    }
}