package com.netease.ntunisdk;

import android.text.TextUtils;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.cutout.RespUtil;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import com.xiaomi.gamecenter.sdk.utils.RSASignature;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
class HttpReqUtil {
    private static final String TAG = "HttpReqUtil";

    HttpReqUtil() {
    }

    static String doHttpReq(String str, String str2, String str3) throws IOException {
        return doHttpReq(str, str2, str3, null);
    }

    static String doHttpReq(String str, String str2, String str3, String str4) throws JSONException, IOException {
        boolean z;
        JSONObject jSONObject;
        UniSdkUtils.d(TAG, "url: " + str);
        UniSdkUtils.d(TAG, "token: " + str2);
        UniSdkUtils.d(TAG, "jsonToPost: " + str3);
        UniSdkUtils.d(TAG, "requestMethod: " + str4);
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
        if (TextUtils.isEmpty(str4)) {
            str4 = "POST";
        }
        httpURLConnection.setRequestMethod(str4);
        boolean z2 = true;
        httpURLConnection.setDoInput(true);
        httpURLConnection.setRequestProperty("Content-Type", ClientLogConstant.NORMAL_TYPE_VALUE);
        httpURLConnection.setRequestProperty(ConstProp.X_LBS_TOKEN, str2);
        if ("POST".equals(str4)) {
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            bufferedWriter.write(str3);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
        } else {
            UniSdkUtils.d(TAG, "output data is not required");
        }
        httpURLConnection.connect();
        int responseCode = httpURLConnection.getResponseCode();
        UniSdkUtils.d(TAG, "code=" + responseCode);
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream(), RSASignature.c);
        } catch (Exception unused) {
        }
        if (inputStreamReader == null) {
            inputStreamReader = new InputStreamReader(httpURLConnection.getErrorStream(), RSASignature.c);
            z = false;
        } else {
            z = true;
        }
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder sb = new StringBuilder();
        while (true) {
            String line = bufferedReader.readLine();
            if (line == null) {
                break;
            }
            sb.append(line);
        }
        String string = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(z ? "result: " : "error: ");
        sb2.append(string);
        UniSdkUtils.d(TAG, sb2.toString());
        bufferedReader.close();
        inputStreamReader.close();
        httpURLConnection.disconnect();
        try {
            new JSONObject(string);
        } catch (Exception unused2) {
            z2 = false;
        }
        try {
            if (!z2) {
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.putOpt("error", string);
                jSONObject2.putOpt(RespUtil.UniSdkField.RESP_MSG, string);
                jSONObject2.putOpt(RespUtil.UniSdkField.RESP_CODE, Integer.valueOf(responseCode));
                jSONObject = jSONObject2;
            } else {
                jSONObject = new JSONObject(string);
                jSONObject.putOpt(RespUtil.UniSdkField.RESP_MSG, "suc");
                jSONObject.putOpt(RespUtil.UniSdkField.RESP_CODE, 0);
            }
            return jSONObject.toString();
        } catch (Exception unused3) {
            return string;
        }
    }
}