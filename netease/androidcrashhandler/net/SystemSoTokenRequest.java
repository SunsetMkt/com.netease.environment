package com.netease.androidcrashhandler.net;

import com.netease.androidcrashhandler.Const;
import com.netease.androidcrashhandler.NTCrashHunterKit;
import com.netease.androidcrashhandler.entity.param.ParamsInfo;
import com.netease.androidcrashhandler.util.CUtil;
import com.netease.androidcrashhandler.util.LogUtils;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import com.netease.ntunisdk.okhttp3.MediaType;
import com.netease.ntunisdk.okhttp3.Request;
import com.netease.ntunisdk.okhttp3.RequestBody;
import com.netease.ntunisdk.okhttp3.Response;
import com.xiaomi.gamecenter.sdk.utils.RSASignature;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class SystemSoTokenRequest extends BaseRequest {
    private JSONArray systemSoArray;

    public SystemSoTokenRequest(JSONArray jSONArray) {
        this.systemSoArray = jSONArray;
    }

    @Override // com.netease.androidcrashhandler.net.BaseRequest
    public RequestBody createRequestBody() throws Exception {
        JSONObject jSONObject;
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        ParamsInfo paramsInfo = NTCrashHunterKit.sharedKit().getmCurrentParamsInfo();
        JSONObject jSONObject2 = new JSONObject();
        if (paramsInfo != null && (jSONObject = paramsInfo.getmParamsJson()) != null && jSONObject.length() > 0) {
            Iterator<String> itKeys = jSONObject.keys();
            while (itKeys.hasNext()) {
                String next = itKeys.next();
                String strOptString = jSONObject.optString(next);
                if (next.equals("project") || next.equals("appkey") || next.equals("os_type") || next.equals(ClientLogConstant.TRANSID)) {
                    jSONObject2.put(next, strOptString);
                }
            }
            jSONObject2.put("os_type", "android");
            jSONObject2.put("crashhunter_version", "3.12.4");
            JSONArray jSONArray = this.systemSoArray;
            if (jSONArray != null && jSONArray.length() > 0) {
                jSONObject2.put("so_list", this.systemSoArray);
            }
            LogUtils.i(LogUtils.TAG, "SystemSoTokenRequest net [createRequestBody] paramsJson = " + jSONObject2.toString());
        }
        return RequestBody.create(mediaType, String.valueOf(jSONObject2));
    }

    @Override // com.netease.androidcrashhandler.net.BaseRequest
    public Request createRequest(RequestBody requestBody) throws Exception {
        LogUtils.i(LogUtils.TAG, "SystemSoTokenRequest net [createRequest] start");
        return new Request.Builder().url(CUtil.getSuitableUrl(Const.URL.DEFAULT_SYSTEM_SO_TOKEN_URL)).post(requestBody).build();
    }

    @Override // com.netease.androidcrashhandler.net.BaseRequest
    public void handleResponse(Response response) throws Exception {
        String string;
        LogUtils.i(LogUtils.TAG, "SystemSoTokenRequest net [handleResponse] start");
        if (response != null) {
            int iCode = response.code();
            LogUtils.i(LogUtils.TAG, "SystemSoTokenRequest net [handleResponse] code=" + iCode);
            InputStream inputStreamByteStream = response.body().byteStream();
            if (inputStreamByteStream != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStreamByteStream, RSASignature.c));
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
                LogUtils.i(LogUtils.TAG, "SystemSoTokenRequest net [handleResponse] \u8bf7\u6c42\u7ed3\u679c=" + string);
            } else {
                LogUtils.i(LogUtils.TAG, "SystemSoTokenRequest net [handleResponse] param error");
                string = "";
            }
            callback(iCode, string);
            return;
        }
        callback(-2, "EXCEPTION_ERROR");
    }
}