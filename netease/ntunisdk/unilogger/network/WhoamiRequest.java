package com.netease.ntunisdk.unilogger.network;

import android.text.TextUtils;
import android.util.Base64;
import com.netease.ntunisdk.okhttp3.Request;
import com.netease.ntunisdk.okhttp3.RequestBody;
import com.netease.ntunisdk.okhttp3.Response;
import com.netease.ntunisdk.unilogger.global.Const;
import com.netease.ntunisdk.unilogger.utils.LogUtils;
import com.netease.ntunisdk.unilogger.utils.Utils;
import com.xiaomi.gamecenter.sdk.utils.RSASignature;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class WhoamiRequest extends BaseRequest {
    @Override // com.netease.ntunisdk.unilogger.network.BaseRequest
    public RequestBody createRequestBody() throws Exception {
        return null;
    }

    @Override // com.netease.ntunisdk.unilogger.network.BaseRequest
    public Request createRequest(RequestBody requestBody) throws Exception {
        char[] cArr = {'t', 'o', 'k', 'e', 'n', '.'};
        String[] strArr = {"e8s", "UKK", "Msw", "YmL"};
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            stringBuffer.append(cArr[i]);
        }
        for (int i2 = 0; i2 < 4; i2++) {
            stringBuffer.append(strArr[i2]);
        }
        return new Request.Builder().header("X-AUTH-PRODUCT", "impression").header("X-AUTH-TOKEN", stringBuffer.toString()).header("X-IPDB-LOCALE", "en").url(Utils.isOversea() ? Const.Network.WHOAMI_URL_OVERSEA : Const.Network.WHOAMI_URL_MAINLAND).build();
    }

    @Override // com.netease.ntunisdk.unilogger.network.BaseRequest
    public void handleResponse(Response response) throws IOException {
        LogUtils.v_inner("WhoamiRequest net [handleResponse] start");
        String string = "";
        if (response == null) {
            callback(-1, "");
            return;
        }
        int iCode = response.code();
        if (200 == iCode) {
            LogUtils.i_inner("WhoamiRequest net [handleResponse] response=" + response.toString());
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.body().byteStream(), RSASignature.c));
                StringBuilder sb = new StringBuilder();
                while (true) {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    } else {
                        sb.append(line);
                    }
                }
                string = sb.toString();
                LogUtils.i_inner("WhoamiRequest net [handleResponse] response \u54cd\u5e94\u5185\u5bb9=" + string);
            } catch (Exception e) {
                LogUtils.w_inner("WhoamiRequest net [handleResponse] Exception=" + e.toString());
                e.printStackTrace();
            }
        } else {
            try {
                LogUtils.i_inner("WhoamiRequest net [handleResponse] response body=" + response.body().string());
            } catch (Exception e2) {
                LogUtils.w_inner("WhoamiRequest net [handleResponse] Exception2=" + e2.toString());
                e2.printStackTrace();
            }
        }
        callback(iCode, getCountry(string));
    }

    private String getCountry(String str) throws JSONException {
        LogUtils.v_inner("WhoamiRequest net [parse] start");
        String string = null;
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                JSONObject jSONObject2 = new JSONObject(new String(Base64.decode((jSONObject.has("payload") ? jSONObject.getString("payload") : "").getBytes(), 0)));
                if (jSONObject2.has(com.netease.ntunisdk.external.protocol.Const.COUNTRY)) {
                    JSONObject jSONObject3 = jSONObject2.getJSONObject(com.netease.ntunisdk.external.protocol.Const.COUNTRY);
                    if (jSONObject3.has("names")) {
                        JSONObject jSONObject4 = jSONObject3.getJSONObject("names");
                        if (jSONObject4.has("en")) {
                            string = jSONObject4.getString("en");
                        }
                    }
                }
                LogUtils.i_inner("WhoamiRequest net [parse] country=" + string);
            } catch (Exception e) {
                LogUtils.w_inner("WhoamiRequest net [parse] Exception=" + e.toString());
                e.printStackTrace();
            }
        }
        return string;
    }
}