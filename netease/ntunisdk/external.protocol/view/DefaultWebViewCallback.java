package com.netease.ntunisdk.external.protocol.view;

import android.text.TextUtils;
import com.netease.ntunisdk.external.protocol.Const;
import com.netease.ntunisdk.external.protocol.ProtocolManager;
import com.netease.ntunisdk.external.protocol.Situation;
import com.netease.ntunisdk.external.protocol.data.ProtocolInfo;
import com.netease.ntunisdk.external.protocol.utils.L;
import com.netease.ntunisdk.external.protocol.utils.TextCompat;
import com.netease.ntunisdk.external.protocol.view.WebViewBridge;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivity;
import java.net.URL;
import org.json.JSONObject;

/* loaded from: classes.dex */
public abstract class DefaultWebViewCallback implements WebViewBridge.Callback, EventCallback {
    private final EventCallback mEventCallback;

    @Override // com.netease.ntunisdk.external.protocol.view.EventCallback
    public void next(Situation situation, ProtocolInfo protocolInfo, int i) {
    }

    public abstract void onPageError(String str);

    public DefaultWebViewCallback(EventCallback eventCallback) {
        this.mEventCallback = eventCallback;
    }

    @Override // com.netease.ntunisdk.external.protocol.view.WebViewBridge.Callback
    public void callback(JSONObject jSONObject, String str) {
        L.d("jsMethod:" + str + ", json:" + jSONObject);
        if (!NgWebviewActivity.ACTION_OPENBROWSER.equalsIgnoreCase(str)) {
            if ("baseprotocolConfirm".equalsIgnoreCase(str)) {
                finish(1, jSONObject);
                return;
            }
            if ("baseprotocolRefuse".equalsIgnoreCase(str)) {
                finish(2, jSONObject);
                return;
            }
            if ("baseprotocolBack".equalsIgnoreCase(str)) {
                back("");
                return;
            }
            if (!Const.ON_PAGE_ERROR.equalsIgnoreCase(str) || jSONObject == null) {
                return;
            }
            String strOptString = jSONObject.optString(Const.WEB_URL);
            if (TextUtils.isEmpty(strOptString)) {
                return;
            }
            onPageError(strOptString);
            return;
        }
        if (jSONObject != null) {
            String strOptString2 = jSONObject.optString(Const.WEB_URL);
            if (TextUtils.isEmpty(strOptString2)) {
                return;
            }
            try {
                strOptString2 = strOptString2.trim();
                String host = new URL(strOptString2).getHost();
                L.d("url host:" + host);
                if (ProtocolManager.getInstance().getCurrentBaseProtocol().addParamsHost.contains(host)) {
                    strOptString2 = TextCompat.wrapperUrl(strOptString2);
                }
            } catch (Throwable unused) {
            }
            L.d("openBrowser:" + strOptString2);
            openBrowser(strOptString2);
        }
    }

    @Override // com.netease.ntunisdk.external.protocol.view.EventCallback
    public void openBrowser(String str) {
        this.mEventCallback.openBrowser(str);
    }

    @Override // com.netease.ntunisdk.external.protocol.view.EventCallback
    public void back(String str) {
        this.mEventCallback.back(str);
    }

    @Override // com.netease.ntunisdk.external.protocol.view.EventCallback
    public void finish(int i, JSONObject jSONObject) {
        this.mEventCallback.finish(i, jSONObject);
    }
}