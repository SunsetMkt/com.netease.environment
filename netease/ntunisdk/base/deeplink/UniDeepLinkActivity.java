package com.netease.ntunisdk.base.deeplink;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.SdkBase;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.UniSdkUtils;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes6.dex */
public class UniDeepLinkActivity extends Activity {
    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) throws JSONException {
        String str;
        String str2;
        super.onCreate(bundle);
        Intent intent = getIntent();
        Uri data = intent.getData();
        UniSdkUtils.d("UniDeepLink", "uri=".concat(String.valueOf(data)));
        boolean booleanExtra = intent.getBooleanExtra("shouldCallback", true);
        UniSdkUtils.i("UniDeepLink", "shouldCallback: ".concat(String.valueOf(booleanExtra)));
        b.a();
        if (data != null) {
            b.a(ConstProp.DEEP_LINK_WHOLE_URI, data.toString());
        }
        if (data != null && !TextUtils.isEmpty(data.getQuery())) {
            for (String str3 : data.getQuery().split(com.alipay.sdk.m.s.a.l)) {
                if (!TextUtils.isEmpty(str3) && !str3.endsWith("=") && !str3.startsWith("=")) {
                    String[] strArrSplit = str3.split("=");
                    if (2 == strArrSplit.length && (str = strArrSplit[0]) != null && (str2 = strArrSplit[1]) != null) {
                        b.a(str, str2);
                    }
                }
            }
        }
        JSONObject jSONObject = new JSONObject();
        try {
            for (Map.Entry<String, String> entry : b.b().entrySet()) {
                jSONObject.put(entry.getKey(), entry.getValue());
            }
            jSONObject.put("methodId", "fromDeepLink");
        } catch (Exception e) {
            e.printStackTrace();
        }
        b.a(intent);
        if (SdkMgr.getInst() == null) {
            UniSdkUtils.d("UniDeepLink", "null SdkMgr.getInst()");
            b.a(ConstProp.DEEP_LINK_FROM_LAUNCH, jSONObject.toString());
            Intent launchIntentForPackage = getPackageManager().getLaunchIntentForPackage(getPackageName());
            if (launchIntentForPackage != null) {
                launchIntentForPackage.setData(data);
                startActivity(launchIntentForPackage);
                overridePendingTransition(0, 0);
            }
        } else if (b.b() != null && booleanExtra) {
            UniSdkUtils.d("UniDeepLink", "valid SdkMgr.getInst()");
            for (Map.Entry<String, String> entry2 : b.b().entrySet()) {
                SdkMgr.getInst().setPropStr(entry2.getKey(), entry2.getValue());
            }
            ((SdkBase) SdkMgr.getInst()).extendFuncCall(jSONObject.toString());
        }
        finish();
    }
}