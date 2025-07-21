package com.netease.ntunisdk.modules.clientlog.utils;

import com.netease.ntunisdk.modules.base.utils.LogModule;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

/* loaded from: classes6.dex */
public class HttpUtil {
    public static boolean postData(String str, String str2, String str3, String str4, String str5) throws IOException {
        HttpsURLConnection httpsURLConnection;
        int responseCode;
        try {
            byte[] bytes = str2.getBytes(str3);
            httpsURLConnection = (HttpsURLConnection) new URL(str).openConnection();
            httpsURLConnection.setConnectTimeout(3000);
            httpsURLConnection.setReadTimeout(3000);
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setUseCaches(false);
            httpsURLConnection.setRequestProperty(str4, str5);
            httpsURLConnection.setRequestProperty("Content-Length", String.valueOf(bytes.length));
            httpsURLConnection.setRequestProperty("Accept-Charset", str3);
            httpsURLConnection.connect();
            OutputStream outputStream = httpsURLConnection.getOutputStream();
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
            responseCode = httpsURLConnection.getResponseCode();
        } catch (IOException | Exception unused) {
        }
        if (responseCode == 200) {
            streamToString(httpsURLConnection.getInputStream());
            return true;
        }
        LogModule.d(ClientLogConstant.TAG, "postData Failed! response: " + responseCode + " resultFailed: " + streamToString(httpsURLConnection.getInputStream()));
        return false;
    }

    private static String streamToString(InputStream inputStream) throws IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr = new byte[1024];
            while (true) {
                int i = inputStream.read(bArr);
                if (i != -1) {
                    byteArrayOutputStream.write(bArr, 0, i);
                } else {
                    byteArrayOutputStream.close();
                    inputStream.close();
                    return byteArrayOutputStream.toString();
                }
            }
        } catch (Exception e) {
            LogModule.d(ClientLogConstant.TAG, "streamToString Exception: " + e);
            return null;
        }
    }
}