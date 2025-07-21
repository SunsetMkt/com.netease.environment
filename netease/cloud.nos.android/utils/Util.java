package com.netease.cloud.nos.android.utils;

import a.a.d.a.a.a;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Base64;
import com.netease.cloud.nos.android.constants.Code;
import com.netease.cloud.nos.android.constants.Constants;
import com.netease.cloud.nos.android.core.Callback;
import com.netease.cloud.nos.android.core.WanAccelerator;
import com.netease.cloud.nos.android.core.WanNOSObject;
import com.netease.cloud.nos.android.exception.InvalidParameterException;
import com.netease.cloud.nos.android.http.HttpResult;
import com.netease.cloud.nos.android.pipeline.PipelineHttpSession;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLException;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ConnectTimeoutException;
import org.jose4j.mac.MacUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class Util {
    private static final String LOGTAG = LogUtil.makeLogTag(Util.class);

    public static CountDownLatch acquireLock() {
        return new CountDownLatch(1);
    }

    public static void addHeaders(HttpPost httpPost, WanNOSObject wanNOSObject) {
        if (wanNOSObject.getContentType() != null && !wanNOSObject.getContentType().equals("")) {
            httpPost.addHeader("Content-Type", wanNOSObject.getContentType());
        }
        if (wanNOSObject.getUserMetadata() == null || wanNOSObject.getUserMetadata().size() <= 0) {
            return;
        }
        Map<String, String> userMetadata = wanNOSObject.getUserMetadata();
        for (String str : userMetadata.keySet()) {
            httpPost.addHeader("x-nos-meta-" + str, userMetadata.get(str));
        }
    }

    public static String buildLBSUrl(String str, String str2) {
        LogUtil.d(LOGTAG, "query lbs url: " + str);
        return str + "?version=1.0&bucketname=" + str2;
    }

    public static String buildPostDataUrl(String str, String str2, String str3, String str4, long j, boolean z) throws UnsupportedEncodingException {
        String str5;
        if (str4 != null) {
            str5 = encode(str2) + "/" + encode(str3) + "?version=1.0&context=" + str4 + "&offset=" + j + "&complete=" + z;
        } else {
            str5 = encode(str2) + "/" + encode(str3) + "?version=1.0&offset=" + j + "&complete=" + z;
        }
        LogUtil.d(LOGTAG, "post data url server: " + str + ", query string: " + str5);
        return str + "/" + str5;
    }

    public static String buildQueryUrl(String str, String str2, String str3, String str4) throws UnsupportedEncodingException {
        String str5;
        if (str4 != null) {
            str5 = encode(str2) + "/" + encode(str3) + "?uploadContext&version=1.0&context=" + str4;
        } else {
            str5 = encode(str2) + "/" + encode(str3) + "?uploadContext&version=1.0";
        }
        return str + "/" + str5;
    }

    public static void checkParameters(Context context, File file, Object obj, WanNOSObject wanNOSObject, Callback callback) throws InvalidParameterException {
        String uploadToken = wanNOSObject.getUploadToken();
        String nosBucketName = wanNOSObject.getNosBucketName();
        String nosObjectName = wanNOSObject.getNosObjectName();
        if (context == null || file == null || obj == null || wanNOSObject == null || callback == null || uploadToken == null || nosBucketName == null || nosObjectName == null) {
            throw new InvalidParameterException("parameters could not be null");
        }
    }

    public static void deleteTempFiles(Context context) {
        File[] fileArrListFiles;
        File file = new File(getSDPath(context).getPath() + Constants.TEMP_FILE);
        if (!file.exists() || (fileArrListFiles = file.listFiles()) == null) {
            return;
        }
        for (File file2 : fileArrListFiles) {
            file2.delete();
        }
    }

    private static String encode(String str) throws UnsupportedEncodingException {
        return URLEncoder.encode(str, WanAccelerator.getConf().getCharset());
    }

    public static FileInput fromInputStream(Context context, File file, String str) throws IOException {
        if (file == null) {
            return null;
        }
        try {
            return new FileInput(file, str);
        } catch (IOException e) {
            throw e;
        }
    }

    public static boolean getBooleanData(Context context, String str) {
        return getDefaultPreferences(context).getBoolean(str, false);
    }

    public static String getData(Context context, String str) {
        return getDefaultPreferences(context).getString(str, null);
    }

    private static SharedPreferences getDefaultPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static ExecutorService getExecutorService() {
        return Executors.newSingleThreadExecutor();
    }

    public static HttpClient getHttpClient(Context context) {
        return Http.getHttpClient(context);
    }

    public static int getHttpCode(HttpResult httpResult) {
        Exception exception;
        int statusCode = httpResult.getStatusCode();
        if (statusCode == 200 || (exception = httpResult.getException()) == null) {
            return statusCode;
        }
        if (exception instanceof ConnectTimeoutException) {
            LogUtil.d(LOGTAG, "connection timeout Exception:" + exception.getMessage());
            return 900;
        }
        if (exception instanceof SocketTimeoutException) {
            LogUtil.d(LOGTAG, "Read Socket Timeout Exception:" + exception.getMessage());
            return Code.SOCKET_TIMEOUT;
        }
        if (exception instanceof NoHttpResponseException) {
            LogUtil.d(LOGTAG, "No HttpResponse Exception:" + exception.getMessage());
            return Code.HTTP_NO_RESPONSE;
        }
        if (exception instanceof SSLException) {
            LogUtil.d(LOGTAG, "SSL Exception:" + exception.getMessage());
            return Code.SSL_FAILED;
        }
        if (exception instanceof SocketException) {
            LogUtil.d(LOGTAG, "Socket Exception" + exception.getMessage());
            String lowerCase = exception.getMessage().toLowerCase();
            if (lowerCase.contains("refused")) {
                return Code.CONNECTION_REFUSED;
            }
            if (lowerCase.contains("reset")) {
                return Code.CONNECTION_RESET;
            }
        } else if (exception instanceof JSONException) {
            LogUtil.d(LOGTAG, "JSON Exception" + exception.getMessage());
            return 701;
        }
        return statusCode;
    }

    public static String getIPAddress() throws SocketException {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                Enumeration<InetAddress> inetAddresses = networkInterfaces.nextElement().getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddressNextElement = inetAddresses.nextElement();
                    if (!inetAddressNextElement.isLoopbackAddress() && (inetAddressNextElement instanceof Inet4Address)) {
                        return inetAddressNextElement.getHostAddress().toString();
                    }
                }
            }
            return "";
        } catch (SocketException unused) {
            LogUtil.e(LOGTAG, "get ip address socket exception");
            return "";
        }
    }

    public static String getIPString(String str) {
        if (str == null || str.equals("")) {
            return "";
        }
        if (str.startsWith("https")) {
            str = str.replaceAll("https://", "");
        }
        if (str.startsWith("http")) {
            str = str.replaceAll("http://", "");
        }
        return str.replaceAll("^(\\d{1,3}(\\.\\d{1,3}){3}).*", "$1");
    }

    public static int getIntData(Context context, String str) {
        return getDefaultPreferences(context).getInt(str, 0);
    }

    public static HttpClient getLbsHttpClient(Context context) {
        return Http.getLbsHttpClient(context);
    }

    public static long getLongData(Context context, String str) {
        return getDefaultPreferences(context).getLong(str, 0L);
    }

    public static String getMonitorUrl(String str) {
        return str + "/stat/sdk?version=1.0";
    }

    public static String getResultString(HttpResult httpResult, String str) {
        if (httpResult != null && httpResult.getMsg() != null && httpResult.getMsg().has(str)) {
            try {
                return httpResult.getMsg().getString(str);
            } catch (JSONException e) {
                LogUtil.e(LOGTAG, "get result string parse json failed", e);
            }
        }
        return "";
    }

    public static File getSDPath(Context context) {
        return Environment.getExternalStorageState().equals("mounted") ? Environment.getExternalStorageDirectory() : context.getCacheDir();
    }

    public static String getToken(String str, String str2, long j, String str3, String str4, String str5, String str6) throws JSONException, NoSuchAlgorithmException, InvalidKeyException {
        JSONObject jSONObject = new JSONObject();
        if (str != null) {
            jSONObject.put("Bucket", str);
        }
        if (str2 != null) {
            jSONObject.put("Object", str2);
        }
        if (j != 0) {
            jSONObject.put("Expires", j);
        }
        if (str5 != null) {
            jSONObject.put("CallbackUrl", str5);
        }
        if (str6 != null) {
            jSONObject.put("CallbackBody", str6);
        }
        String str7 = new String(Base64.encode(jSONObject.toString().getBytes(), 2));
        SecretKeySpec secretKeySpec = new SecretKeySpec(str4.getBytes(), MacUtil.HMAC_SHA256);
        Mac mac = Mac.getInstance(MacUtil.HMAC_SHA256);
        mac.init(secretKeySpec);
        return "UPLOAD " + str3 + ":" + new String(Base64.encode(mac.doFinal(str7.getBytes()), 2)) + ":" + str7;
    }

    public static String[] getUploadServer(Context context, String str, boolean z) {
        StringBuilder sb;
        String str2;
        if (z) {
            sb = new StringBuilder();
            sb.append(str);
            str2 = Constants.HTTPS_UPLOAD_SERVER_KEY;
        } else {
            sb = new StringBuilder();
            sb.append(str);
            str2 = Constants.UPLOAD_SERVER_KEY;
        }
        sb.append(str2);
        String data = getData(context, sb.toString());
        if (data == null) {
            return null;
        }
        return data.split(";");
    }

    public static long ipToLong(String str) throws NumberFormatException {
        if (str == null || str.equals("")) {
            return 0L;
        }
        int iIndexOf = str.indexOf(".");
        int i = iIndexOf + 1;
        int iIndexOf2 = str.indexOf(".", i);
        int i2 = iIndexOf2 + 1;
        int iIndexOf3 = str.indexOf(".", i2);
        long j = Long.parseLong(str.substring(iIndexOf3 + 1));
        long[] jArr = {Long.parseLong(str.substring(0, iIndexOf)), Long.parseLong(str.substring(i, iIndexOf2)), Long.parseLong(str.substring(i2, iIndexOf3)), j};
        return (jArr[0] << 24) + (jArr[1] << 16) + (jArr[2] << 8) + j;
    }

    /* JADX WARN: Removed duplicated region for block: B:9:0x0014 A[PHI: r2
  0x0014: PHI (r2v3 java.lang.String) = (r2v1 java.lang.String), (r2v2 java.lang.String) binds: [B:8:0x0012, B:11:0x001f] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean isValidLbsIP(java.lang.String r4) {
        /*
            r0 = 0
            if (r4 == 0) goto L45
            java.lang.String r1 = ""
            boolean r2 = r4.equals(r1)
            if (r2 == 0) goto Lc
            goto L45
        Lc:
            java.lang.String r2 = "https://"
            boolean r3 = r4.startsWith(r2)
            if (r3 == 0) goto L19
        L14:
            java.lang.String r4 = r4.replaceFirst(r2, r1)
            goto L22
        L19:
            java.lang.String r2 = "http://"
            boolean r3 = r4.startsWith(r2)
            if (r3 == 0) goto L45
            goto L14
        L22:
            java.lang.String r2 = "/lbs"
            boolean r3 = r4.endsWith(r2)
            if (r3 != 0) goto L2b
            return r0
        L2b:
            java.lang.String r4 = r4.replaceFirst(r2, r1)
            java.lang.String r1 = "0.0.0.0"
            boolean r1 = r4.equals(r1)
            if (r1 != 0) goto L45
            java.lang.String r1 = "255.255.255.255"
            boolean r1 = r4.equals(r1)
            if (r1 == 0) goto L40
            goto L45
        L40:
            boolean r4 = com.netease.cloud.nos.android.utils.ValidIP.validate(r4)
            return r4
        L45:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.cloud.nos.android.utils.Util.isValidLbsIP(java.lang.String):boolean");
    }

    public static void netStateChange(Context context) {
        String str = LOGTAG;
        LogUtil.d(str, "network connection change");
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (connectivityManager == null || activeNetworkInfo == null || !activeNetworkInfo.isAvailable() || !activeNetworkInfo.isConnected()) {
            return;
        }
        NetworkType netWorkType = NetworkType.getNetWorkType(context);
        int intData = getIntData(context, Constants.BUCKET_NUMBER);
        LogUtil.d(str, "bucketNum =" + intData + ", netType = " + netWorkType.getNetworkType());
        for (int i = 0; i < intData; i++) {
            String data = getData(context, Constants.BUCKET_NAME + i);
            if (data != null) {
                setBooleanData(context, data + Constants.LBS_STATUS, false);
                setData(context, data + Constants.NET_TYPE, netWorkType.getNetworkType());
            }
        }
        PipelineHttpSession.reStart();
    }

    public static HttpGet newGet(String str) {
        return new HttpGet(str);
    }

    public static HttpPost newPost(String str) {
        return new HttpPost(str);
    }

    public static void pipeAddHeaders(a aVar, WanNOSObject wanNOSObject) {
        if (wanNOSObject.getContentType() != null && !wanNOSObject.getContentType().equals("")) {
            aVar.f().a("Content-Type", (Object) wanNOSObject.getContentType());
        }
        if (wanNOSObject.getUserMetadata() == null || wanNOSObject.getUserMetadata().size() <= 0) {
            return;
        }
        Map<String, String> userMetadata = wanNOSObject.getUserMetadata();
        for (String str : userMetadata.keySet()) {
            aVar.f().a("x-nos-meta-" + str, (Object) userMetadata.get(str));
        }
    }

    public static String pipeBuildPostDataUrl(String str, String str2, String str3, long j, boolean z) throws UnsupportedEncodingException {
        String str4;
        if (str3 != null) {
            str4 = encode(str) + "/" + encode(str2) + "?version=1.0&context=" + str3 + "&offset=" + j + "&complete=" + z;
        } else {
            str4 = encode(str) + "/" + encode(str2) + "?version=1.0&offset=" + j + "&complete=" + z;
        }
        return "/" + str4;
    }

    public static String pipeBuildQueryUrl(String str, String str2, String str3) throws UnsupportedEncodingException {
        String str4;
        if (str3 != null) {
            str4 = encode(str) + "/" + encode(str2) + "?uploadContext&version=1.0&context=" + str3;
        } else {
            str4 = encode(str) + "/" + encode(str2) + "?uploadContext&version=1.0";
        }
        return "/" + str4;
    }

    public static void releaseLock(CountDownLatch countDownLatch) {
        countDownLatch.countDown();
    }

    private static String replaceWithHttps(String str) {
        return str.replaceAll("http://", "https://");
    }

    public static void setBooleanData(Context context, String str, boolean z) {
        SharedPreferences.Editor editorEdit = getDefaultPreferences(context).edit();
        editorEdit.putBoolean(str, z);
        editorEdit.commit();
    }

    public static void setBucketName(Context context, String str) {
        SharedPreferences defaultPreferences = getDefaultPreferences(context);
        int i = defaultPreferences.getInt(Constants.BUCKET_NUMBER, 0);
        for (int i2 = 0; i2 < i; i2++) {
            if (defaultPreferences.getString(Constants.BUCKET_NAME + i2, null).equals(str)) {
                return;
            }
        }
        SharedPreferences.Editor editorEdit = defaultPreferences.edit();
        editorEdit.putString(Constants.BUCKET_NAME + i, str);
        editorEdit.putInt(Constants.BUCKET_NUMBER, i + 1);
        editorEdit.commit();
    }

    public static void setData(Context context, String str, String str2) {
        SharedPreferences.Editor editorEdit = getDefaultPreferences(context).edit();
        editorEdit.putString(str, str2);
        editorEdit.commit();
    }

    public static HttpRequestBase setHeader(HttpRequestBase httpRequestBase, Map<String, String> map) {
        if (map == null) {
            return httpRequestBase;
        }
        for (String str : map.keySet()) {
            httpRequestBase.addHeader(str, map.get(str));
        }
        return httpRequestBase;
    }

    public static HttpResult setLBSData(Context context, String str, JSONObject jSONObject) throws JSONException {
        try {
            String string = jSONObject.getString("lbs");
            String strTransformString = transformString(jSONObject.getJSONArray("upload"));
            String str2 = LOGTAG;
            LogUtil.d(str2, "lbsString: " + string);
            LogUtil.d(str2, "upload server string: " + strTransformString);
            if (string != null) {
                setData(context, str + Constants.LBS_KEY, string);
            }
            if (strTransformString != null) {
                String strReplaceWithHttps = replaceWithHttps(strTransformString);
                LogUtil.d(str2, "https servers: " + strReplaceWithHttps);
                setData(context, str + Constants.UPLOAD_SERVER_KEY, strTransformString);
                setData(context, str + Constants.HTTPS_UPLOAD_SERVER_KEY, strReplaceWithHttps);
                setLongData(context, str + Constants.LBS_TIME, Long.valueOf(System.currentTimeMillis()));
                setBooleanData(context, str + Constants.LBS_STATUS, true);
            }
            setBucketName(context, str);
            return new HttpResult(200, jSONObject, null);
        } catch (JSONException e) {
            LogUtil.e(LOGTAG, "get json array exception", e);
            return new HttpResult(700, jSONObject, null);
        }
    }

    public static void setLock(CountDownLatch countDownLatch) throws InterruptedException {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            LogUtil.e(LOGTAG, "set lock with interrupted exception", e);
        }
    }

    public static void setLongData(Context context, String str, Long l) {
        SharedPreferences.Editor editorEdit = getDefaultPreferences(context).edit();
        editorEdit.putLong(str, l.longValue());
        editorEdit.commit();
    }

    public static int transformCode(int i) {
        if (i == -5) {
            return Code.HTTP_NO_RESPONSE;
        }
        if (i == -4) {
            return Code.HTTP_EXCEPTION;
        }
        if (i != -3) {
            return i != -2 ? 999 : 403;
        }
        return 500;
    }

    private static String transformString(JSONArray jSONArray) {
        if (jSONArray == null || jSONArray.length() == 0) {
            return null;
        }
        String str = "";
        for (int i = 0; i < jSONArray.length(); i++) {
            try {
                str = str + jSONArray.getString(i);
                if (i != jSONArray.length() - 1) {
                    str = str + ";";
                }
            } catch (JSONException e) {
                LogUtil.e(LOGTAG, "get json string exception", e);
            }
        }
        return str;
    }
}