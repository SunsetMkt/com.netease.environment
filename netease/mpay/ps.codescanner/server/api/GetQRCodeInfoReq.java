package com.netease.mpay.ps.codescanner.server.api;

import android.text.TextUtils;
import com.netease.mpay.ps.codescanner.CodeScannerConst;
import com.netease.mpay.ps.codescanner.net.BasicNameValuePair;
import com.netease.mpay.ps.codescanner.net.NameValuePair;
import com.netease.mpay.ps.codescanner.server.api.GetQRCodeInfoResp;
import com.netease.mpay.ps.codescanner.utils.Logging;
import com.netease.ntunisdk.external.protocol.Const;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class GetQRCodeInfoReq extends Request {
    private String mAppChannel;
    private String mLoginChannel;
    private String mSdkJsonData;
    private GetQRCodeInfoResp resp;
    private String uuid;

    public GetQRCodeInfoReq(String str, String str2, String str3, String str4) {
        super(0, "/api/qrcode/scan");
        this.uuid = str;
        this.mLoginChannel = str2;
        this.mAppChannel = str3;
        this.mSdkJsonData = str4;
    }

    @Override // com.netease.mpay.ps.codescanner.server.api.Request
    public GetQRCodeInfoResp getResponse() {
        return this.resp;
    }

    @Override // com.netease.mpay.ps.codescanner.server.api.Request
    ArrayList<NameValuePair> getDatas() {
        ArrayList<NameValuePair> arrayList = new ArrayList<>();
        arrayList.add(new BasicNameValuePair("uuid", this.uuid));
        arrayList.add(new BasicNameValuePair("login_channel", this.mLoginChannel));
        arrayList.add(new BasicNameValuePair(Const.APP_CHANNEL, this.mAppChannel));
        if (!TextUtils.isEmpty(this.mSdkJsonData)) {
            try {
                JSONObject jSONObject = new JSONObject(this.mSdkJsonData);
                if (jSONObject.has(CodeScannerConst.PAY_CHANNEL)) {
                    arrayList.add(new BasicNameValuePair(CodeScannerConst.PAY_CHANNEL, jSONObject.optString(CodeScannerConst.PAY_CHANNEL)));
                }
            } catch (JSONException e) {
                Logging.logStackTrace(e);
            } catch (Exception e2) {
                Logging.logStackTrace(e2);
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.netease.mpay.ps.codescanner.server.api.Request
    public GetQRCodeInfoResp parseContent(JSONObject jSONObject) throws JSONException {
        JSONObject jSONObject2 = jSONObject.getJSONObject("qrcode_info");
        JSONObject jSONObjectOptJSONObject = jSONObject2.optJSONObject("game");
        return new GetQRCodeInfoResp(jSONObject2.optString("uuid"), GetQRCodeInfoResp.QRCodeAction.convert(jSONObject2.optInt("action")), jSONObjectOptJSONObject != null ? jSONObjectOptJSONObject.optString(ResIdReader.RES_TYPE_ID) : null, jSONObjectOptJSONObject != null ? jSONObjectOptJSONObject.optString("name") : null, jSONObjectOptJSONObject != null ? jSONObjectOptJSONObject.optInt("web_token_persist_3party") : 0);
    }

    @Override // com.netease.mpay.ps.codescanner.server.api.Request
    void saveResponse(Response response) {
        if (response instanceof GetQRCodeInfoResp) {
            this.resp = (GetQRCodeInfoResp) response;
        }
    }
}