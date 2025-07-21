package com.netease.ntunisdk.base;

import android.content.Context;
import com.facebook.hermes.intl.Constants;
import com.netease.pharos.Const;
import com.netease.pharos.PharosListener;
import com.netease.pharos.PharosProxy;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class SDKPharos {
    private static volatile SDKPharos c;

    /* renamed from: a, reason: collision with root package name */
    private Context f1626a = null;
    private PharosListener b = null;
    private String d = Constants.CASEFIRST_FALSE;
    private boolean e = true;

    public String getPharosSDKVersion() {
        return Const.VERSION;
    }

    private SDKPharos() {
    }

    public static SDKPharos getInstance() {
        if (c == null) {
            synchronized (SDKPharos.class) {
                if (c == null) {
                    SDKPharos sDKPharos = new SDKPharos();
                    c = sDKPharos;
                    try {
                        PharosProxy.getInstance();
                        sDKPharos.e = true;
                    } catch (Throwable unused) {
                        sDKPharos.e = false;
                    }
                    if (c.e) {
                        boolean z = UniSdkUtils.isDebug;
                        UniSdkUtils.d("SDKPharos", "SDKPharos [getInstance] debugMode=".concat(String.valueOf(z)));
                        PharosProxy.getInstance().setmEB(SdkMgr.getInst().hasFeature("EB"));
                        PharosProxy.getInstance().setDebug(z);
                        PharosProxy.getInstance().setmHasSet(true);
                    } else {
                        UniSdkUtils.d("SDKPharos", "SDKPharos has been removed!");
                    }
                }
            }
        }
        return c;
    }

    public void setContext(Context context) {
        this.f1626a = context;
    }

    public String getPharosid() {
        String str = this.e ? PharosProxy.getInstance().getmLinktestId() : null;
        return str == null ? "" : str;
    }

    public void setPharosListener(PharosListener pharosListener) {
        UniSdkUtils.d("SDKPharos", "SDKPharos [PharosListener] [setPharosListener] start");
        if (!this.e) {
            UniSdkUtils.d("SDKPharos", "SDKPharos has been removed!");
            return;
        }
        if (pharosListener != null) {
            this.b = pharosListener;
        }
        PharosProxy.getInstance().setmPharosListener(new PharosListener() { // from class: com.netease.ntunisdk.base.SDKPharos.1
            @Override // com.netease.pharos.PharosListener, com.netease.pharos.PharosListenerImpl
            public void onResult(JSONObject jSONObject) throws JSONException {
                UniSdkUtils.d("SDKPharos", "SDKPharos [PharosListener] \u5168\u90e8\u7ed3\u679c\u56de\u8c03");
                UniSdkUtils.d("SDKPharos", "SDKPharos [PharosListener] [onResult] data = " + jSONObject.toString());
                if (SDKPharos.this.b != null) {
                    UniSdkUtils.d("SDKPharos", "SDKPharos [PharosListener] mPharosListener callback");
                    SDKPharos.this.b.onResult(jSONObject);
                } else {
                    UniSdkUtils.d("SDKPharos", "SDKPharos [PharosListener] mPharosListener is null");
                }
                try {
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put("methodId", "pharosOnResult");
                    jSONObject2.put("result", jSONObject);
                    ((SdkBase) SdkMgr.getInst()).extendFuncCall(jSONObject2.toString());
                } catch (Exception e) {
                    UniSdkUtils.w("SDKPharos", "SDKPharos [PharosListener] [onResult] Exception = " + e.toString());
                }
            }

            @Override // com.netease.pharos.PharosListener, com.netease.pharos.PharosListenerImpl
            public void onPharosPolicy(JSONObject jSONObject) throws JSONException {
                UniSdkUtils.d("SDKPharos", "SDKPharos [PharosListener] \u8bbe\u5907\u63a2\u6d4b\u3001\u533a\u57df\u51b3\u7b56\u56de\u8c03");
                UniSdkUtils.d("SDKPharos", "SDKPharos [PharosListener] [onPharosPolicy] data = " + jSONObject.toString());
                if (SDKPharos.this.b != null) {
                    SDKPharos.this.b.onPharosPolicy(jSONObject);
                }
                try {
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put("methodId", "pharosOnPharosPolicy");
                    jSONObject2.put("result", jSONObject);
                    ((SdkBase) SdkMgr.getInst()).extendFuncCall(jSONObject2.toString());
                } catch (Exception e) {
                    UniSdkUtils.w("SDKPharos", "SDKPharos [PharosListener] [onPharosPolicy] Exception = " + e.toString());
                }
            }

            @Override // com.netease.pharos.PharosListener, com.netease.pharos.PharosListenerImpl
            public void onPharosQos(JSONObject jSONObject) throws JSONException {
                UniSdkUtils.d("SDKPharos", "SDKPharos [PharosListener] Qos\u6a21\u5757\u56de\u8c03");
                UniSdkUtils.d("SDKPharos", "SDKPharos [PharosListener] [onPharosQos] data = " + jSONObject.toString());
                if (SDKPharos.this.b != null) {
                    SDKPharos.this.b.onPharosQos(jSONObject);
                }
                try {
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put("methodId", "pharosOnPharosQos");
                    jSONObject2.put("result", jSONObject);
                    ((SdkBase) SdkMgr.getInst()).extendFuncCall(jSONObject2.toString());
                } catch (Exception e) {
                    UniSdkUtils.w("SDKPharos", "SDKPharos [PharosListener] [onPharosQos] Exception = " + e.toString());
                }
            }

            @Override // com.netease.pharos.PharosListener, com.netease.pharos.PharosListenerImpl
            public void onPharosServer(JSONObject jSONObject) throws JSONException {
                UniSdkUtils.d("SDKPharos", "SDKPharos [PharosListener] \u52a0\u901f\u5217\u8868\u6a21\u5757\u56de\u8c03");
                UniSdkUtils.d("SDKPharos", "SDKPharos [PharosListener] [onPharosServer] data = " + jSONObject.toString());
                if (SDKPharos.this.b != null) {
                    SDKPharos.this.b.onPharosServer(jSONObject);
                }
                try {
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put("methodId", "pharosOnPharosServer");
                    jSONObject2.put("result", jSONObject);
                    ((SdkBase) SdkMgr.getInst()).extendFuncCall(jSONObject2.toString());
                } catch (Exception e) {
                    UniSdkUtils.w("SDKPharos", "SDKPharos [PharosListener] [onPharosServer] Exception = " + e.toString());
                }
            }
        });
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x0084  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0098  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00a5  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00b0  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00b4  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00ba  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void extendFunc(java.lang.String r20) {
        /*
            Method dump skipped, instructions count: 617
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.SDKPharos.extendFunc(java.lang.String):void");
    }
}