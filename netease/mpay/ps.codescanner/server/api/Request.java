package com.netease.mpay.ps.codescanner.server.api;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.browser.trusted.sharing.ShareTarget;
import com.netease.mc.mi.R;
import com.netease.mpay.ps.codescanner.Configs;
import com.netease.mpay.ps.codescanner.Consts;
import com.netease.mpay.ps.codescanner.net.BasicNameValuePair;
import com.netease.mpay.ps.codescanner.net.FetchUrl;
import com.netease.mpay.ps.codescanner.net.NameValuePair;
import com.netease.mpay.ps.codescanner.server.ApiCallException;
import com.netease.mpay.ps.codescanner.utils.DeviceUtils;
import com.netease.mpay.ps.codescanner.utils.Logging;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/* loaded from: classes5.dex */
public abstract class Request {
    String apiSuffix;
    int method;

    abstract ArrayList<NameValuePair> getDatas();

    public abstract Response getResponse();

    abstract Response parseContent(JSONObject jSONObject) throws JSONException;

    abstract void saveResponse(Response response);

    public Request(int i, String str) {
        this.method = i;
        this.apiSuffix = str;
    }

    public int getMethod() {
        return this.method;
    }

    public String getURL() {
        return Configs.getHost() + this.apiSuffix;
    }

    public HashMap<String, String> createHeaders(Context context) {
        HashMap<String, String> map = new HashMap<>();
        map.put("Content-type", ShareTarget.ENCODING_TYPE_URL_ENCODED);
        map.put("Accept-Language", DeviceUtils.getLocale());
        try {
            String strSubstring = Build.MODEL;
            String strValueOf = String.valueOf(Build.VERSION.SDK_INT);
            String str = AppInfo.getInstance(context).name + "/" + AppInfo.getInstance(context).version;
            String str2 = Consts.SDK_NAME + "/" + Consts.VERSION_CODE;
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            if (strSubstring.length() > 50) {
                strSubstring = strSubstring.substring(0, 50);
            }
            sb.append(strSubstring);
            sb.append(";");
            sb.append(strValueOf);
            sb.append(")");
            map.put("User-agent", str + " " + str2 + " " + sb.toString());
        } catch (Exception unused) {
            map.put("User-agent", "NeteaseMpayCodeScanner/a1.7.0");
        }
        return map;
    }

    public ArrayList<NameValuePair> createDatas(Context context, String str) {
        ArrayList<NameValuePair> datas = getDatas();
        if (datas == null) {
            datas = new ArrayList<>();
        }
        datas.add(new BasicNameValuePair("game_id", str));
        datas.add(new BasicNameValuePair("gv", "" + AppInfo.getInstance(context).version));
        datas.add(new BasicNameValuePair("gvn", "" + AppInfo.getInstance(context).versionName));
        datas.add(new BasicNameValuePair("cv", Consts.VERSION_CODE));
        return datas;
    }

    public void parseResp(Context context, FetchUrl.FetchUrlResponse fetchUrlResponse) throws ApiCallException {
        String string = context.getString(R.string.netease_mpay_ps_codescanner__network_err_data_parsing);
        try {
            filterApiError(context, fetchUrlResponse);
            saveResponse(parseContent((JSONObject) new JSONTokener(new String(fetchUrlResponse.content)).nextValue()));
        } catch (ClassCastException unused) {
            throw new ApiCallException(string);
        } catch (JSONException unused2) {
            throw new ApiCallException(string);
        }
    }

    private static class AppInfo {
        static AppInfo appInfoInstance;
        String name;
        String version;
        String versionName;

        static AppInfo getInstance(Context context) {
            synchronized (context) {
                if (appInfoInstance == null) {
                    appInfoInstance = new AppInfo(context);
                }
            }
            return appInfoInstance;
        }

        AppInfo(Context context) throws PackageManager.NameNotFoundException {
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                this.name = packageInfo.packageName.substring(packageInfo.packageName.lastIndexOf(".") + 1);
                this.version = String.valueOf(packageInfo.versionCode);
                this.versionName = String.valueOf(packageInfo.versionName);
            } catch (Exception e) {
                Logging.logStackTrace(e);
                this.name = "";
                this.version = "";
                this.versionName = "";
            }
        }
    }

    private static class ApiErrorInfo {
        static final int CONTENT_FORMAT_ERROR = -1;
        int code;
        String reason;

        public ApiErrorInfo(Context context) {
            this(-1, context.getString(R.string.netease_mpay_ps_codescanner__network_err_data_parsing));
        }

        ApiErrorInfo(int i, String str) {
            this.code = i;
            this.reason = str;
        }
    }

    private void filterApiError(Context context, FetchUrl.FetchUrlResponse fetchUrlResponse) throws ApiCallException {
        ApiErrorInfo apiErrorInfo;
        if (fetchUrlResponse.code == 200 || fetchUrlResponse.code == 201) {
            return;
        }
        ApiErrorInfo apiErrorInfo2 = new ApiErrorInfo(context);
        try {
            JSONObject jSONObject = (JSONObject) new JSONTokener(new String(fetchUrlResponse.content)).nextValue();
            apiErrorInfo = new ApiErrorInfo(Integer.valueOf(jSONObject.getString("code")).intValue(), jSONObject.getString("reason"));
        } catch (ClassCastException | NumberFormatException | JSONException unused) {
            apiErrorInfo = apiErrorInfo2;
        }
        throw new ApiCallException(apiErrorInfo.reason);
    }
}