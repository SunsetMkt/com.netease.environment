package com.netease.mpay.ps.codescanner.server.api;

import android.text.TextUtils;
import android.util.Base64;
import com.netease.mpay.ps.codescanner.module.AppExtra;
import com.netease.mpay.ps.codescanner.net.BasicNameValuePair;
import com.netease.mpay.ps.codescanner.net.NameValuePair;
import com.netease.mpay.ps.codescanner.utils.DataUtils;
import com.netease.mpay.ps.codescanner.utils.Logging;
import com.netease.mpay.ps.codescanner.widget.Crypto;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.external.protocol.Const;
import com.tencent.connect.common.Constants;
import com.xiaomi.onetrack.OneTrack;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class ConfirmLoginReq extends Request {
    String channel;
    AppExtra extra;
    boolean isRemember;
    byte[] key;
    ConfirmLoginResp resp;
    String scene;
    String token;
    String uid;
    String uuid;

    public ConfirmLoginReq(String str, String str2, String str3, String str4, String str5, AppExtra appExtra, boolean z) {
        super(1, "/api/qrcode/confirm_login");
        this.uuid = str;
        this.uid = str3;
        this.token = str4;
        this.channel = str5;
        this.extra = appExtra;
        this.scene = str2;
        this.isRemember = z;
    }

    public ConfirmLoginReq addEncryptKey(byte[] bArr) {
        this.key = bArr;
        return this;
    }

    @Override // com.netease.mpay.ps.codescanner.server.api.Request
    public ConfirmLoginResp getResponse() {
        return this.resp;
    }

    @Override // com.netease.mpay.ps.codescanner.server.api.Request
    ArrayList<NameValuePair> getDatas() {
        ArrayList<NameValuePair> arrayList = new ArrayList<>();
        arrayList.add(new BasicNameValuePair("uuid", this.uuid));
        arrayList.add(new BasicNameValuePair("enc_uuid", DataUtils.hexlify(Crypto.ursEnc(this.uuid.getBytes(), this.key))));
        if (!TextUtils.isEmpty(this.scene)) {
            arrayList.add(new BasicNameValuePair("scene", this.scene));
        }
        arrayList.add(new BasicNameValuePair(OneTrack.Param.USER_ID, this.uid));
        arrayList.add(new BasicNameValuePair("token", this.token));
        arrayList.add(new BasicNameValuePair("login_channel", this.channel));
        AppExtra appExtra = this.extra;
        if (appExtra != null) {
            arrayList.add(new BasicNameValuePair("udid", appExtra.udid));
            arrayList.add(new BasicNameValuePair(Const.APP_CHANNEL, this.extra.channel));
            arrayList.add(new BasicNameValuePair("sdk_version", this.extra.version));
            if (!TextUtils.isEmpty(this.extra.sdkJsonData)) {
                try {
                    JSONObject jSONObject = new JSONObject(this.extra.sdkJsonData);
                    Iterator<String> itKeys = jSONObject.keys();
                    while (itKeys.hasNext()) {
                        String next = itKeys.next();
                        arrayList.add(new BasicNameValuePair(next, jSONObject.optString(next)));
                    }
                } catch (Exception e) {
                    Logging.logStackTrace(e);
                }
            }
            if (this.extra.extra != null) {
                arrayList.add(new BasicNameValuePair("extra_data", this.extra.extra));
            }
            if (this.extra.extraUniData != null) {
                arrayList.add(new BasicNameValuePair("extra_unisdk_data", this.extra.extraUniData));
            }
        }
        arrayList.add(new BasicNameValuePair("is_remember", this.isRemember ? "1" : "0"));
        return arrayList;
    }

    private String buildExtraUniData(String str) throws JSONException {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        String propStr = SdkMgr.getInst().getPropStr(ConstProp.UNISDK_JF_ACCESS_TOKEN);
        if (TextUtils.isEmpty(propStr)) {
            return str;
        }
        try {
            JSONObject jSONObject = new JSONObject(new String(Base64.decode(URLDecoder.decode(new JSONObject(str).optString(ConstProp.SAUTH_JSON), "UTF-8"), 0)));
            jSONObject.put(Constants.PARAM_ACCESS_TOKEN, propStr);
            return URLEncoder.encode(Base64.encodeToString(jSONObject.toString().getBytes(), 0), "UTF-8");
        } catch (Exception e) {
            Logging.logStackTrace(e);
            return str;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.netease.mpay.ps.codescanner.server.api.Request
    public ConfirmLoginResp parseContent(JSONObject jSONObject) throws JSONException {
        return new ConfirmLoginResp(jSONObject != null ? jSONObject.optString("redirect_url") : null, this.scene);
    }

    @Override // com.netease.mpay.ps.codescanner.server.api.Request
    void saveResponse(Response response) {
        if (response instanceof ConfirmLoginResp) {
            this.resp = (ConfirmLoginResp) response;
        }
    }
}