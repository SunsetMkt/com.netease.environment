package com.netease.ntunisdk.modules.ngwebviewgeneral.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import com.netease.ntunisdk.base.SdkBase;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.SdkU3d;
import com.netease.ntunisdk.base.WebViewProxy;
import com.netease.ntunisdk.modules.api.ModulesManager;
import com.netease.ntunisdk.modules.ngwebviewgeneral.IRemoteCallback;
import com.netease.ntunisdk.modules.ngwebviewgeneral.IRemoteService;
import com.netease.ntunisdk.modules.ngwebviewgeneral.log.NgWebviewLog;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivity;
import com.tencent.open.SocialConstants;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class IPCWebViewManager {
    private static final String TAG = "ng_webview#ipc";
    private IRemoteService iRemoteService;
    private ModulesManager modulesManager;
    private SdkBase sdkBase;
    private boolean isHasUnisdk = true;
    IRemoteCallback.Stub iRemoteCallback = new IRemoteCallback.Stub() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.aidl.IPCWebViewManager.1
        AnonymousClass1() {
        }

        @Override // com.netease.ntunisdk.modules.ngwebviewgeneral.IRemoteCallback
        public void call(String str) throws JSONException, ClassNotFoundException {
            NgWebviewLog.d(IPCWebViewManager.TAG, "call :  " + str, new Object[0]);
            try {
                Class.forName("com.netease.ntunisdk.base.SdkBase");
            } catch (ClassNotFoundException unused) {
                NgWebviewLog.e(IPCWebViewManager.TAG, "Didn't find unisdkClass , check the name again !");
                IPCWebViewManager.this.isHasUnisdk = false;
            }
            try {
                IPCWebViewManager.this.modulesManager = ModulesManager.getInst();
                if (IPCWebViewManager.this.isHasUnisdk) {
                    IPCWebViewManager.this.sdkBase = (SdkBase) SdkMgr.getInst();
                }
                if (IPCWebViewManager.this.modulesManager == null && IPCWebViewManager.this.sdkBase == null) {
                    return;
                }
                JSONObject jSONObject = new JSONObject(str);
                String strOptString = jSONObject.optString("methodId");
                String strOptString2 = jSONObject.optString(SocialConstants.PARAM_SOURCE);
                if (NgWebviewActivity.ACTION_NOTIFY_NATIVE.equals(strOptString)) {
                    String strOptString3 = jSONObject.optJSONObject("reqData").optString("methodId");
                    jSONObject.put("callback_id", jSONObject.optString("identifier"));
                    if (!TextUtils.isEmpty(strOptString3)) {
                        if (!TextUtils.isEmpty(strOptString2) || IPCWebViewManager.this.sdkBase == null) {
                            IPCWebViewManager.this.modulesManager.extendFunc(strOptString2, "ngWebViewGeneral", jSONObject.toString());
                            return;
                        } else {
                            IPCWebViewManager.this.sdkBase.ntExtendFunc(jSONObject.toString());
                            return;
                        }
                    }
                    if (!TextUtils.isEmpty(strOptString2) || IPCWebViewManager.this.sdkBase == null) {
                        IPCWebViewManager.this.modulesManager.callback(strOptString2, "ngWebViewGeneral", jSONObject.toString());
                        return;
                    } else {
                        IPCWebViewManager.this.sdkBase.extendFuncCall(jSONObject.toString());
                        return;
                    }
                }
                if (NgWebviewActivity.ACTION_EXECUTE_EXTEND_FUNC.equals(strOptString)) {
                    JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("reqData");
                    String strOptString4 = jSONObjectOptJSONObject.optString("methodId");
                    jSONObjectOptJSONObject.put("callback_id", jSONObject.optString("identifier"));
                    if (TextUtils.isEmpty(strOptString4)) {
                        return;
                    }
                    if (!TextUtils.isEmpty(strOptString2) || IPCWebViewManager.this.sdkBase == null) {
                        IPCWebViewManager.this.modulesManager.extendFunc(strOptString2, "ngWebViewGeneral", jSONObjectOptJSONObject.toString());
                        return;
                    } else {
                        IPCWebViewManager.this.sdkBase.ntExtendFunc(jSONObjectOptJSONObject.toString());
                        return;
                    }
                }
                if (SdkU3d.CALLBACKTYPE_OnWebViewNativeCall.equals(strOptString)) {
                    String strOptString5 = jSONObject.optString("action");
                    String strOptString6 = jSONObject.optString("data");
                    if (IPCWebViewManager.this.isHasUnisdk) {
                        WebViewProxy.getInstance().getCallbackInterface().nativeCall(strOptString5, strOptString6);
                        return;
                    }
                    return;
                }
                if (!TextUtils.isEmpty(strOptString2) || IPCWebViewManager.this.sdkBase == null) {
                    IPCWebViewManager.this.modulesManager.callback(strOptString2, "ngWebViewGeneral", str);
                } else {
                    IPCWebViewManager.this.sdkBase.extendFuncCall(str);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    private final ServiceConnection connection = new ServiceConnection() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.aidl.IPCWebViewManager.2
        AnonymousClass2() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) throws RemoteException {
            NgWebviewLog.d(IPCWebViewManager.TAG, "onServiceConnected", new Object[0]);
            IPCWebViewManager.this.iRemoteService = IRemoteService.Stub.asInterface(iBinder);
            try {
                IPCWebViewManager.this.iRemoteService.asBinder().linkToDeath(IPCWebViewManager.this.deathRecipient, 0);
                IPCWebViewManager.this.iRemoteService.register(IPCWebViewManager.this.iRemoteCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            NgWebviewLog.d(IPCWebViewManager.TAG, "onServiceDisconnected", new Object[0]);
            if (IPCWebViewManager.this.iRemoteService != null) {
                IPCWebViewManager.this.iRemoteService.asBinder().unlinkToDeath(IPCWebViewManager.this.deathRecipient, 0);
            }
            IPCWebViewManager.this.iRemoteService = null;
            IPCWebViewManager.this.iRemoteCallback = null;
        }
    };
    private final IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.aidl.IPCWebViewManager.3
        AnonymousClass3() {
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            NgWebviewLog.e(IPCWebViewManager.TAG, "binderDied");
        }
    };

    /* renamed from: com.netease.ntunisdk.modules.ngwebviewgeneral.aidl.IPCWebViewManager$1 */
    class AnonymousClass1 extends IRemoteCallback.Stub {
        AnonymousClass1() {
        }

        @Override // com.netease.ntunisdk.modules.ngwebviewgeneral.IRemoteCallback
        public void call(String str) throws JSONException, ClassNotFoundException {
            NgWebviewLog.d(IPCWebViewManager.TAG, "call :  " + str, new Object[0]);
            try {
                Class.forName("com.netease.ntunisdk.base.SdkBase");
            } catch (ClassNotFoundException unused) {
                NgWebviewLog.e(IPCWebViewManager.TAG, "Didn't find unisdkClass , check the name again !");
                IPCWebViewManager.this.isHasUnisdk = false;
            }
            try {
                IPCWebViewManager.this.modulesManager = ModulesManager.getInst();
                if (IPCWebViewManager.this.isHasUnisdk) {
                    IPCWebViewManager.this.sdkBase = (SdkBase) SdkMgr.getInst();
                }
                if (IPCWebViewManager.this.modulesManager == null && IPCWebViewManager.this.sdkBase == null) {
                    return;
                }
                JSONObject jSONObject = new JSONObject(str);
                String strOptString = jSONObject.optString("methodId");
                String strOptString2 = jSONObject.optString(SocialConstants.PARAM_SOURCE);
                if (NgWebviewActivity.ACTION_NOTIFY_NATIVE.equals(strOptString)) {
                    String strOptString3 = jSONObject.optJSONObject("reqData").optString("methodId");
                    jSONObject.put("callback_id", jSONObject.optString("identifier"));
                    if (!TextUtils.isEmpty(strOptString3)) {
                        if (!TextUtils.isEmpty(strOptString2) || IPCWebViewManager.this.sdkBase == null) {
                            IPCWebViewManager.this.modulesManager.extendFunc(strOptString2, "ngWebViewGeneral", jSONObject.toString());
                            return;
                        } else {
                            IPCWebViewManager.this.sdkBase.ntExtendFunc(jSONObject.toString());
                            return;
                        }
                    }
                    if (!TextUtils.isEmpty(strOptString2) || IPCWebViewManager.this.sdkBase == null) {
                        IPCWebViewManager.this.modulesManager.callback(strOptString2, "ngWebViewGeneral", jSONObject.toString());
                        return;
                    } else {
                        IPCWebViewManager.this.sdkBase.extendFuncCall(jSONObject.toString());
                        return;
                    }
                }
                if (NgWebviewActivity.ACTION_EXECUTE_EXTEND_FUNC.equals(strOptString)) {
                    JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("reqData");
                    String strOptString4 = jSONObjectOptJSONObject.optString("methodId");
                    jSONObjectOptJSONObject.put("callback_id", jSONObject.optString("identifier"));
                    if (TextUtils.isEmpty(strOptString4)) {
                        return;
                    }
                    if (!TextUtils.isEmpty(strOptString2) || IPCWebViewManager.this.sdkBase == null) {
                        IPCWebViewManager.this.modulesManager.extendFunc(strOptString2, "ngWebViewGeneral", jSONObjectOptJSONObject.toString());
                        return;
                    } else {
                        IPCWebViewManager.this.sdkBase.ntExtendFunc(jSONObjectOptJSONObject.toString());
                        return;
                    }
                }
                if (SdkU3d.CALLBACKTYPE_OnWebViewNativeCall.equals(strOptString)) {
                    String strOptString5 = jSONObject.optString("action");
                    String strOptString6 = jSONObject.optString("data");
                    if (IPCWebViewManager.this.isHasUnisdk) {
                        WebViewProxy.getInstance().getCallbackInterface().nativeCall(strOptString5, strOptString6);
                        return;
                    }
                    return;
                }
                if (!TextUtils.isEmpty(strOptString2) || IPCWebViewManager.this.sdkBase == null) {
                    IPCWebViewManager.this.modulesManager.callback(strOptString2, "ngWebViewGeneral", str);
                } else {
                    IPCWebViewManager.this.sdkBase.extendFuncCall(str);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.netease.ntunisdk.modules.ngwebviewgeneral.aidl.IPCWebViewManager$2 */
    class AnonymousClass2 implements ServiceConnection {
        AnonymousClass2() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) throws RemoteException {
            NgWebviewLog.d(IPCWebViewManager.TAG, "onServiceConnected", new Object[0]);
            IPCWebViewManager.this.iRemoteService = IRemoteService.Stub.asInterface(iBinder);
            try {
                IPCWebViewManager.this.iRemoteService.asBinder().linkToDeath(IPCWebViewManager.this.deathRecipient, 0);
                IPCWebViewManager.this.iRemoteService.register(IPCWebViewManager.this.iRemoteCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            NgWebviewLog.d(IPCWebViewManager.TAG, "onServiceDisconnected", new Object[0]);
            if (IPCWebViewManager.this.iRemoteService != null) {
                IPCWebViewManager.this.iRemoteService.asBinder().unlinkToDeath(IPCWebViewManager.this.deathRecipient, 0);
            }
            IPCWebViewManager.this.iRemoteService = null;
            IPCWebViewManager.this.iRemoteCallback = null;
        }
    }

    /* renamed from: com.netease.ntunisdk.modules.ngwebviewgeneral.aidl.IPCWebViewManager$3 */
    class AnonymousClass3 implements IBinder.DeathRecipient {
        AnonymousClass3() {
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            NgWebviewLog.e(IPCWebViewManager.TAG, "binderDied");
        }
    }

    public void bindService(Context context) {
        NgWebviewLog.d(TAG, "bindService", new Object[0]);
        context.bindService(new Intent(context, (Class<?>) NGRemoteService.class), this.connection, 1);
    }

    public void unbindService(Context context) {
        NgWebviewLog.d(TAG, "unbindService", new Object[0]);
        context.unbindService(this.connection);
        this.iRemoteService = null;
        this.iRemoteCallback = null;
    }

    public void send(String str) {
        IRemoteService iRemoteService = this.iRemoteService;
        if (iRemoteService != null) {
            try {
                iRemoteService.send(str);
                return;
            } catch (RemoteException e) {
                e.printStackTrace();
                return;
            }
        }
        NgWebviewLog.e(TAG, "iRemoteService is null");
    }
}