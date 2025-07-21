package com.netease.ntunisdk.unilogger.network;

import android.text.TextUtils;
import com.netease.ntunisdk.okhttp3.Request;
import com.netease.ntunisdk.okhttp3.RequestBody;
import com.netease.ntunisdk.okhttp3.Response;
import com.netease.ntunisdk.unilogger.global.Const;
import com.netease.ntunisdk.unilogger.global.GlobalPrarm;
import com.netease.ntunisdk.unilogger.utils.LogUtils;
import com.netease.ntunisdk.unilogger.utils.Utils;
import com.xiaomi.gamecenter.sdk.utils.RSASignature;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class ConfigRequest extends BaseRequest {
    @Override // com.netease.ntunisdk.unilogger.network.BaseRequest
    public RequestBody createRequestBody() throws Exception {
        return null;
    }

    @Override // com.netease.ntunisdk.unilogger.network.BaseRequest
    public Request createRequest(RequestBody requestBody) throws Exception {
        Request.Builder builder = new Request.Builder();
        StringBuilder sb = Utils.isOversea() ? new StringBuilder(Const.Network.CONFIG_URL_OVERSEA) : new StringBuilder(Const.Network.CONFIG_URL_MAINLAND);
        sb.append(GlobalPrarm.realGameId);
        sb.append(".json");
        return builder.url(sb.toString()).build();
    }

    @Override // com.netease.ntunisdk.unilogger.network.BaseRequest
    public void handleResponse(Response response) throws IOException {
        LogUtils.v_inner("ConfigRequest net [handleResponse] start");
        String string = "";
        if (response == null) {
            callback(-1, "");
            return;
        }
        int iCode = response.code();
        if (200 == iCode) {
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
            } catch (Exception e) {
                LogUtils.w_inner("ConfigRequest net [handleResponse] Exception=" + e.toString());
                e.printStackTrace();
            }
        } else {
            try {
                String strString = response.body().string();
                if (!TextUtils.isEmpty(strString)) {
                    LogUtils.i_inner("ConfigRequest net [handleResponse] response body=" + strString);
                    JSONObject jSONObject = new JSONObject(strString);
                    if (jSONObject.has("code")) {
                        if ("DEPO-DB-1001".equals(jSONObject.optString("code"))) {
                            try {
                                string = jSONObject.has("msg") ? jSONObject.optString("msg") : "unknown";
                                iCode = -2;
                            } catch (Exception e2) {
                                e = e2;
                                iCode = -2;
                                LogUtils.w_inner("ConfigRequest net [handleResponse] Exception2=" + e.toString());
                                e.printStackTrace();
                                LogUtils.i_inner("ConfigRequest net [handleResponse] config content=" + string + ", response content=" + response.toString());
                                callback(iCode, string);
                            }
                        }
                    }
                }
            } catch (Exception e3) {
                e = e3;
            }
        }
        LogUtils.i_inner("ConfigRequest net [handleResponse] config content=" + string + ", response content=" + response.toString());
        callback(iCode, string);
    }
}