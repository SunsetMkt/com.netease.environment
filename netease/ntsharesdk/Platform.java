package com.netease.ntsharesdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/* loaded from: classes.dex */
public abstract class Platform extends Activity {
    public static final String OTHER = "Other";
    public static final String QQ = "QQ";
    public static final String Version = "1.6.1";
    public static final String WEIBO = "Weibo";
    public static final String WEIXIN = "Weixin";
    public static final String YIXIN = "Yixin";
    public static String mPackName = "";
    private HashMap<String, ShareArgs> cacheShare;
    public boolean callQQResult;
    public boolean callWeiboOauthResult;
    public boolean callWeiboResult;
    public boolean hasInit;
    protected HashMap<String, String> mConf;
    protected Context myCtx;
    protected OnShareEndListener shareEndListener;

    public abstract Boolean checkArgs(ShareArgs shareArgs);

    protected abstract Object genMessage(ShareArgs shareArgs);

    public abstract Object getAPIInst();

    protected abstract String getPlatformName();

    public void handleActivityResult(int i, int i2, Intent intent) {
    }

    public abstract void handleIntent(Intent intent);

    public void handleRequest(Object obj) {
    }

    public void handleResponse(Object obj) {
    }

    public boolean hasPlatformInstall(String str) {
        return false;
    }

    protected abstract void initSdk(Context context);

    public abstract void share(ShareArgs shareArgs);

    public abstract void updateApi(String str, Context context);

    public static void dLog(String str) {
        Log.d("ntsharesdk", "[1.6.1] " + str);
    }

    public void setShareEndListener(OnShareEndListener onShareEndListener) {
        this.shareEndListener = onShareEndListener;
    }

    public void share(ShareArgs shareArgs, Activity activity) {
        share(shareArgs);
    }

    private static void doConfigVal(HashMap<String, String> map, JSONObject jSONObject, String str) throws JSONException {
        String string;
        try {
        } catch (JSONException unused) {
            string = null;
        }
        if (jSONObject.has(str)) {
            string = jSONObject.getString(str);
            if (string == null || map.containsKey(str)) {
                return;
            }
            map.put(str, string);
        }
    }

    public static HashMap<String, String> readConfig(Context context, String str) throws JSONException, IOException {
        String str2;
        dLog("platfrom:" + str);
        try {
            InputStream inputStreamOpen = context.getAssets().open("ntshare_data", 3);
            byte[] bArr = new byte[inputStreamOpen.available()];
            inputStreamOpen.read(bArr);
            str2 = new String(bArr, "UTF-8");
        } catch (IOException e) {
            dLog("read ntshare_data error :" + e.getMessage());
            str2 = null;
        }
        dLog("ntshare_data json:" + str2);
        if (str2 == null) {
            return null;
        }
        mPackName = context.getPackageName();
        try {
            JSONObject jSONObject = (JSONObject) new JSONTokener(str2).nextValue();
            if (jSONObject.has(mPackName)) {
                jSONObject = jSONObject.getJSONObject(mPackName);
            }
            if (!jSONObject.has(str)) {
                dLog("conf.has(pf) false");
                return null;
            }
            JSONObject jSONObject2 = (JSONObject) jSONObject.get(str);
            HashMap<String, String> map = new HashMap<>();
            doConfigVal(map, jSONObject2, "app_id");
            doConfigVal(map, jSONObject2, "app_sec");
            doConfigVal(map, jSONObject2, "app_key");
            doConfigVal(map, jSONObject2, "app_url");
            doConfigVal(map, jSONObject2, "user_name");
            doConfigVal(map, jSONObject2, "path");
            return map;
        } catch (JSONException e2) {
            dLog("ntshare_data config parse to json error: " + e2.getMessage());
            return null;
        }
    }

    public String getConfig(String str, String str2) {
        return this.mConf.containsKey(str) ? this.mConf.get(str) : str2;
    }

    public void setConfig(String str, String str2) {
        this.mConf.put(str, str2);
    }

    public String getConfig(String str) {
        return getConfig(str, null);
    }

    public Platform() {
        this.hasInit = false;
        this.callWeiboResult = false;
        this.callWeiboOauthResult = false;
        this.callQQResult = false;
        this.shareEndListener = null;
        this.myCtx = null;
        this.mConf = null;
        this.cacheShare = new HashMap<>();
    }

    public Platform(Context context) {
        this.hasInit = false;
        this.callWeiboResult = false;
        this.callWeiboOauthResult = false;
        this.callQQResult = false;
        this.shareEndListener = null;
        this.myCtx = null;
        this.mConf = null;
        this.cacheShare = new HashMap<>();
        this.myCtx = context;
        this.mConf = readConfig(this.myCtx, getPlatformName());
    }

    protected void pushShareTranscation(String str, ShareArgs shareArgs) {
        this.cacheShare.put(str, shareArgs);
    }

    protected ShareArgs popShareTransaction(String str) {
        if (!this.cacheShare.containsKey(str)) {
            return null;
        }
        ShareArgs shareArgs = this.cacheShare.get(str);
        this.cacheShare.remove(str);
        return shareArgs;
    }

    protected Bitmap scaleBitmap(Bitmap bitmap) {
        dLog("scale ShareArgs.IMG_DATA to thumb");
        float fMin = Math.min(80.0f / bitmap.getWidth(), 80.0f / bitmap.getHeight());
        Bitmap bitmapCreateScaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * fMin), (int) (bitmap.getHeight() * fMin), true);
        dLog("thumb_data w:" + bitmapCreateScaledBitmap.getWidth() + ",h:" + bitmapCreateScaledBitmap.getHeight());
        return bitmapCreateScaledBitmap;
    }
}