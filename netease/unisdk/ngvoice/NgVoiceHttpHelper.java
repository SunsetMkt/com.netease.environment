package com.netease.unisdk.ngvoice;

import android.content.Context;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import com.netease.unisdk.ngvoice.log.NgLog;
import com.netease.unisdk.ngvoice.utils.FileUtil;
import java.io.File;
import java.io.InputStream;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/* loaded from: classes5.dex */
public class NgVoiceHttpHelper {
    private static final String TAG = "ng_voice HttpHelper";
    private OkHttpClient mClient = new OkHttpClient();
    private NgVoiceSettings mSettings;

    public void setVoiceSettings(NgVoiceSettings ngVoiceSettings) {
        this.mSettings = ngVoiceSettings;
    }

    public String upload(File file) {
        String uploadUrl = getUploadUrl(FileUtil.fileMD5(file.getAbsolutePath()));
        NgLog.i(TAG, "upload url = " + uploadUrl);
        try {
            String strString = this.mClient.newCall(new Request.Builder().url(uploadUrl).header("User-Agent", this.mSettings.useragent).post(new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("upload", "upload", RequestBody.create(MediaType.parse("application/octet-stream"), file)).build()).build()).execute().body().string();
            NgLog.i(TAG, "upload response = " + strString);
            if (TextUtils.isEmpty(strString) || '0' != strString.charAt(0)) {
                return null;
            }
            return strString.substring(2);
        } catch (Exception e) {
            NgLog.e(TAG, "upload Exception : " + e.getMessage());
            return null;
        }
    }

    private String getUploadUrl(String str) {
        StringBuilder sb = new StringBuilder(this.mSettings.url);
        if (this.mSettings.url.endsWith("/")) {
            sb.append("upload?");
        } else {
            sb.append("/upload?");
        }
        sb.append("md5=");
        sb.append(str);
        sb.append("&usernum=");
        sb.append(this.mSettings.uid);
        sb.append("&host=");
        sb.append(this.mSettings.host);
        sb.append("&tousers=");
        sb.append(this.mSettings.tousers);
        sb.append("&keep_type=");
        sb.append(this.mSettings.keep_type);
        return sb.toString();
    }

    public String getTranslation(String str) {
        String translationUrl = getTranslationUrl(str);
        NgLog.i(TAG, "getTranslation url = " + translationUrl);
        try {
            String strString = this.mClient.newCall(new Request.Builder().url(translationUrl).header("User-Agent", this.mSettings.useragent).build()).execute().body().string();
            if (TextUtils.isEmpty(strString)) {
                return null;
            }
            NgLog.i(TAG, "getTranslation response = " + strString);
            if ('0' == strString.charAt(0)) {
                return strString.substring(2);
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    private String getTranslationUrl(String str) {
        StringBuilder sb = new StringBuilder(this.mSettings.url);
        if (this.mSettings.url.endsWith("/")) {
            sb.append("get_translation?");
        } else {
            sb.append("/get_translation?");
        }
        sb.append("key=");
        sb.append(str);
        return sb.toString();
    }

    public InputStream downloadVoiceFile(String str) {
        String downloadVoiceFileUrl = getDownloadVoiceFileUrl(str);
        NgLog.i(TAG, "download file url = " + downloadVoiceFileUrl);
        try {
            return this.mClient.newCall(new Request.Builder().url(downloadVoiceFileUrl).header("User-Agent", this.mSettings.useragent).build()).execute().body().byteStream();
        } catch (Exception unused) {
            return null;
        }
    }

    private String getDownloadVoiceFileUrl(String str) {
        StringBuilder sb = new StringBuilder(this.mSettings.url);
        if (this.mSettings.url.endsWith("/")) {
            sb.append("getfile?");
        } else {
            sb.append("/getfile?");
        }
        sb.append("key=");
        sb.append(str);
        sb.append("&usernum=");
        sb.append(this.mSettings.uid);
        sb.append("&host=");
        sb.append(this.mSettings.host);
        return sb.toString();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null || connectivityManager.getActiveNetworkInfo() == null) {
            return false;
        }
        return connectivityManager.getActiveNetworkInfo().isAvailable();
    }
}