package com.netease.ntunisdk.modules.ngwebviewgeneral.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.facebook.react.uimanager.ViewProps;
import com.netease.ntunisdk.modules.ngwebviewgeneral.IRemoteCallback;
import com.netease.ntunisdk.modules.ngwebviewgeneral.IRemoteService;
import com.netease.ntunisdk.modules.ngwebviewgeneral.log.NgWebviewLog;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivity;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class NGRemoteService extends Service {
    private static final String TAG = "ng_webview#ips";
    public static IRemoteCallback callback;
    IRemoteService.Stub stub = new IRemoteService.Stub() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.aidl.NGRemoteService.1
        AnonymousClass1() {
        }

        @Override // com.netease.ntunisdk.modules.ngwebviewgeneral.IRemoteService
        public void register(IRemoteCallback iRemoteCallback) {
            if (iRemoteCallback == null) {
                return;
            }
            NGRemoteService.callback = iRemoteCallback;
        }

        @Override // com.netease.ntunisdk.modules.ngwebviewgeneral.IRemoteService
        public void send(String str) throws JSONException {
            NgWebviewLog.d(NGRemoteService.TAG, "send :  " + str, new Object[0]);
            try {
                JSONObject jSONObject = new JSONObject(str);
                String string = jSONObject.getString("methodId");
                String strOptString = jSONObject.optString("callback_id");
                if ("NGWebViewClose".equals(string)) {
                    NgWebviewActivity ngWebviewActivity = NgWebviewActivity.getInstance();
                    if (ngWebviewActivity == null) {
                        NgWebviewLog.d(NGRemoteService.TAG, "NgWebviewActivity is null", new Object[0]);
                        return;
                    } else {
                        ngWebviewActivity.closeWebview("ntExtendFunc");
                        return;
                    }
                }
                if ("NGWebViewCallbackToWeb".equals(string)) {
                    NgWebviewActivity ngWebviewActivity2 = NgWebviewActivity.getInstance();
                    if (ngWebviewActivity2 == null) {
                        NgWebviewLog.d(NGRemoteService.TAG, "NgWebviewActivity is null", new Object[0]);
                        return;
                    } else {
                        ngWebviewActivity2.onJsCallback(strOptString, str);
                        return;
                    }
                }
                if ("NGWebViewControl".equalsIgnoreCase(string)) {
                    String strOptString2 = jSONObject.optString("action");
                    NgWebviewActivity ngWebviewActivity3 = NgWebviewActivity.getInstance();
                    if (ngWebviewActivity3 == null) {
                        NgWebviewLog.d(NGRemoteService.TAG, "NgWebviewActivity is null", new Object[0]);
                    } else if (ViewProps.HIDDEN.equals(strOptString2)) {
                        NgWebviewLog.d(NGRemoteService.TAG, "NgWebviewActivity moveTaskToBack", new Object[0]);
                        ngWebviewActivity3.moveTaskToBack(true);
                    }
                }
            } catch (Exception unused) {
            }
        }
    };

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.stub;
    }

    /* renamed from: com.netease.ntunisdk.modules.ngwebviewgeneral.aidl.NGRemoteService$1 */
    class AnonymousClass1 extends IRemoteService.Stub {
        AnonymousClass1() {
        }

        @Override // com.netease.ntunisdk.modules.ngwebviewgeneral.IRemoteService
        public void register(IRemoteCallback iRemoteCallback) {
            if (iRemoteCallback == null) {
                return;
            }
            NGRemoteService.callback = iRemoteCallback;
        }

        @Override // com.netease.ntunisdk.modules.ngwebviewgeneral.IRemoteService
        public void send(String str) throws JSONException {
            NgWebviewLog.d(NGRemoteService.TAG, "send :  " + str, new Object[0]);
            try {
                JSONObject jSONObject = new JSONObject(str);
                String string = jSONObject.getString("methodId");
                String strOptString = jSONObject.optString("callback_id");
                if ("NGWebViewClose".equals(string)) {
                    NgWebviewActivity ngWebviewActivity = NgWebviewActivity.getInstance();
                    if (ngWebviewActivity == null) {
                        NgWebviewLog.d(NGRemoteService.TAG, "NgWebviewActivity is null", new Object[0]);
                        return;
                    } else {
                        ngWebviewActivity.closeWebview("ntExtendFunc");
                        return;
                    }
                }
                if ("NGWebViewCallbackToWeb".equals(string)) {
                    NgWebviewActivity ngWebviewActivity2 = NgWebviewActivity.getInstance();
                    if (ngWebviewActivity2 == null) {
                        NgWebviewLog.d(NGRemoteService.TAG, "NgWebviewActivity is null", new Object[0]);
                        return;
                    } else {
                        ngWebviewActivity2.onJsCallback(strOptString, str);
                        return;
                    }
                }
                if ("NGWebViewControl".equalsIgnoreCase(string)) {
                    String strOptString2 = jSONObject.optString("action");
                    NgWebviewActivity ngWebviewActivity3 = NgWebviewActivity.getInstance();
                    if (ngWebviewActivity3 == null) {
                        NgWebviewLog.d(NGRemoteService.TAG, "NgWebviewActivity is null", new Object[0]);
                    } else if (ViewProps.HIDDEN.equals(strOptString2)) {
                        NgWebviewLog.d(NGRemoteService.TAG, "NgWebviewActivity moveTaskToBack", new Object[0]);
                        ngWebviewActivity3.moveTaskToBack(true);
                    }
                }
            } catch (Exception unused) {
            }
        }
    }
}