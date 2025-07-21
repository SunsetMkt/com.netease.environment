package com.netease.androidcrashhandler.net;

import com.netease.androidcrashhandler.Const;
import com.netease.androidcrashhandler.NTCrashHunterKit;
import com.netease.androidcrashhandler.entity.di.DiProxy;
import com.netease.androidcrashhandler.entity.param.ParamsInfo;
import com.netease.androidcrashhandler.init.InitProxy;
import com.netease.androidcrashhandler.thirdparty.deviceInfoModule.DeviceInfoProxy;
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
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class ConfigRequest extends BaseRequest {
    @Override // com.netease.androidcrashhandler.net.BaseRequest
    public RequestBody createRequestBody() throws Exception {
        LogUtils.i(LogUtils.TAG, "ConfigRequest net [createRequestBody]  start");
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        JSONObject jSONObject = new JSONObject();
        ParamsInfo paramsInfo = NTCrashHunterKit.sharedKit().getmCurrentParamsInfo();
        if (paramsInfo != null) {
            JSONObject jSONObject2 = paramsInfo.getmParamsJson();
            if (jSONObject2 != null && jSONObject2.length() > 0) {
                Iterator<String> itKeys = jSONObject2.keys();
                while (itKeys.hasNext()) {
                    String next = itKeys.next();
                    String strOptString = jSONObject2.optString(next);
                    if (next.equals("project") || next.equals("appkey") || next.equals("os_type") || next.equals(Const.ParamKey.CLIENT_V) || next.equals("channel")) {
                        jSONObject.put(next, strOptString);
                    }
                    if (next.equals(ClientLogConstant.TRANSID)) {
                        if (DeviceInfoProxy.checkTransidValid(strOptString)) {
                            jSONObject.put(next, strOptString);
                        } else {
                            DeviceInfoProxy.createTransid();
                            jSONObject.put(next, InitProxy.getInstance().getmTransid());
                        }
                    }
                }
            }
            if (!jSONObject.has(Const.ParamKey.CLIENT_V)) {
                jSONObject.put(Const.ParamKey.CLIENT_V, InitProxy.getInstance().getEngineVersion() + "(" + InitProxy.getInstance().getResVersion() + ")");
            }
            JSONObject diInfoJson = DiProxy.getInstance().getDiInfoJson();
            if (diInfoJson != null) {
                if (diInfoJson.has("model")) {
                    jSONObject.put("model", diInfoJson.optString("model"));
                }
                if (diInfoJson.has("cpu")) {
                    jSONObject.put("cpu", diInfoJson.optString("cpu"));
                }
                if (diInfoJson.has("gpu")) {
                    jSONObject.put("gpu", diInfoJson.optString("gpu"));
                }
                if (diInfoJson.has("bundle_version")) {
                    jSONObject.put("bundle_version", diInfoJson.optString("bundle_version"));
                }
                if (diInfoJson.has("version_code")) {
                    jSONObject.put("version_code", diInfoJson.optString("version_code"));
                }
                if (diInfoJson.has("channel")) {
                    jSONObject.put("channel", diInfoJson.optString("channel"));
                }
                if (diInfoJson.has("crashhunter_version")) {
                    jSONObject.put("crashhunter_version", diInfoJson.optString("crashhunter_version"));
                }
                if (diInfoJson.has("system_version")) {
                    jSONObject.put("system_version", diInfoJson.optString("system_version"));
                }
                if (diInfoJson.has("base_version")) {
                    jSONObject.put("base_version", diInfoJson.optString("base_version"));
                }
            }
            jSONObject.put("branch", InitProxy.getInstance().getmBranch());
        }
        return RequestBody.create(mediaType, String.valueOf(jSONObject));
    }

    @Override // com.netease.androidcrashhandler.net.BaseRequest
    public Request createRequest(RequestBody requestBody) throws Exception {
        LogUtils.i(LogUtils.TAG, "ConfigRequest net [createRequest] start");
        return new Request.Builder().url(CUtil.getSuitableUrl(InitProxy.getInstance().getConfigUrl())).post(requestBody).build();
    }

    @Override // com.netease.androidcrashhandler.net.BaseRequest
    public void handleResponse(Response response) throws Exception {
        String string;
        if (response != null) {
            int iCode = response.code();
            LogUtils.i(LogUtils.TAG, "ConfigRequest net [handleResponse] code=" + iCode);
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
                LogUtils.i(LogUtils.TAG, "ConfigRequest net [handleResponse] \u8bf7\u6c42\u7ed3\u679c=" + string);
            } else {
                LogUtils.i(LogUtils.TAG, "ConfigRequest net [handleResponse] param error");
                string = "";
            }
            callback(iCode, string);
            return;
        }
        callback(-2, "EXCEPTION_ERROR");
    }
}