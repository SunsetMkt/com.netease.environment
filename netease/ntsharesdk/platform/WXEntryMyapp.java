package com.netease.ntsharesdk.platform;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.netease.ntsharesdk.Platform;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.ysdk.module.user.impl.wx.YSDKWXEntryActivity;
import java.io.IOException;
import java.io.InputStream;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/* loaded from: classes.dex */
public class WXEntryMyapp extends YSDKWXEntryActivity {
    private IWXAPI api;

    /* JADX WARN: Multi-variable type inference failed */
    public void onCreate(Bundle bundle) throws IOException {
        super.onCreate(bundle);
        String config = readConfig();
        this.api = WXAPIFactory.createWXAPI(this, config, false);
        this.api.registerApp(config);
        this.api.handleIntent(getIntent(), this);
    }

    private String readConfig() throws IOException {
        try {
            InputStream inputStreamOpen = getAssets().open("ntshare_data", 3);
            byte[] bArr = new byte[inputStreamOpen.available()];
            inputStreamOpen.read(bArr);
            String str = new String(bArr, "UTF-8");
            Platform.dLog("ntshare_data json:" + str);
            String strOptString = ((JSONObject) ((JSONObject) new JSONTokener(str).nextValue()).get(Platform.WEIXIN)).optString("app_id");
            Platform.dLog("read ntshare_data weixin appid :" + strOptString);
            return strOptString;
        } catch (IOException e) {
            Platform.dLog("read ntshare_data error :" + e.getMessage());
            return "";
        } catch (JSONException e2) {
            Platform.dLog("read ntshare_data error :" + e2.getMessage());
            return "";
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void onNewIntent(Intent intent) {
        Log.d("ntsharesdk", "onNewIntent");
        super.onNewIntent(intent);
        setIntent(intent);
        this.api.handleIntent(intent, this);
    }

    public void onReq(BaseReq baseReq) {
        Log.d("ntsharesdk", "onReq");
        super.onReq(baseReq);
        if (baseReq instanceof ShowMessageFromWX.Req) {
            WXMediaMessage wXMediaMessage = ((ShowMessageFromWX.Req) baseReq).message;
            Log.d("ntsharesdk", "WXMediaMessage: " + wXMediaMessage);
            String str = wXMediaMessage.messageExt;
            String str2 = baseReq.openId;
            Log.d("ntsharesdk", "messageExt: " + str + " ,openid: " + str2);
            if (TextUtils.isEmpty(str)) {
                str = "ntes://game.mobile/unisdk";
            } else if (!str.contains("://")) {
                str = "ntes://game.mobile/unisdk?" + str + "&wxOpenid=" + str2;
            }
            Log.d("ntsharesdk", "messageExtUri: " + str);
            Intent intent = new Intent();
            intent.setClassName(getPackageName(), "com.netease.ntunisdk.base.deeplink.UniDeepLinkActivity");
            intent.setData(Uri.parse(str));
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
        finish();
    }

    public void onResp(BaseResp baseResp) {
        Log.d("ntsharesdk", "onResp:" + baseResp.getType());
        super.onResp(baseResp);
        if (Weixin.getInst() != null) {
            Weixin.getInst().handleResponse(baseResp);
        } else {
            Log.d("ntsharesdk", "pf null");
        }
        finish();
    }
}