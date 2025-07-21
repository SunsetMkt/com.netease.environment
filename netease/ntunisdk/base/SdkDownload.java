package com.netease.ntunisdk.base;

import android.content.Context;
import android.text.TextUtils;
import com.netease.download.Const;
import com.netease.download.downloader.DownloadProxy;
import com.netease.download.listener.DownloadListener;
import com.xiaomi.onetrack.OneTrack;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class SdkDownload {
    private static Class c;
    private static Method d;

    /* renamed from: a, reason: collision with root package name */
    private Context f1791a = null;
    private DownloadListener b = null;

    public SdkDownload() {
        if (c == null) {
            try {
                c = Class.forName("com.netease.ntunisdk.orbitv3.compat.DownloadCompat");
                UniSdkUtils.i("SdkDownload", "orbit v3");
            } catch (ClassNotFoundException unused) {
                UniSdkUtils.i("SdkDownload", "orbit v2");
            }
        }
    }

    public void setContext(Context context) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        this.f1791a = context;
        Class cls = c;
        if (cls != null) {
            try {
                Method declaredMethod = cls.getDeclaredMethod("setContext", Context.class);
                declaredMethod.setAccessible(true);
                declaredMethod.invoke(null, context);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e2) {
                e2.printStackTrace();
            } catch (InvocationTargetException e3) {
                e3.printStackTrace();
            }
        }
    }

    public void setDownloadCallback(DownloadListener downloadListener) {
        this.b = downloadListener;
    }

    public String getOrbitSessionId() throws NoSuchMethodException, SecurityException {
        Class cls = c;
        String currentSessionId = null;
        if (cls != null) {
            try {
                Method declaredMethod = cls.getDeclaredMethod("getOrbitSessionId", new Class[0]);
                declaredMethod.setAccessible(true);
                return String.valueOf(declaredMethod.invoke(null, new Object[0]));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e2) {
                e2.printStackTrace();
            } catch (InvocationTargetException e3) {
                e3.printStackTrace();
            }
        }
        try {
            currentSessionId = DownloadProxy.getCurrentSessionId();
        } catch (Throwable th) {
            th.printStackTrace();
            UniSdkUtils.w("SdkDownload", "SdkDownload [getOrbitSessionId] Exception=" + th.toString());
        }
        return currentSessionId == null ? "" : currentSessionId;
    }

    public static String getChannel() {
        try {
            return SdkMgr.getInst().getChannel();
        } catch (Exception e) {
            UniSdkUtils.w("SdkDownload", "SdkDownload [getAppChannel] Exception=" + e.toString());
            return null;
        }
    }

    public static String getSDKVersion() {
        try {
            return ((SdkBase) SdkMgr.getInst()).getSDKVersion();
        } catch (Exception e) {
            UniSdkUtils.w("SdkDownload", "SdkDownload [getAppChanelVer] Exception=".concat(String.valueOf(e)));
            return null;
        }
    }

    public String getDownloadSDKVersion() throws NoSuchMethodException, SecurityException {
        Class cls = c;
        if (cls == null) {
            return Const.VERSION;
        }
        try {
            Method declaredMethod = cls.getDeclaredMethod("getDownloadSDKVersion", new Class[0]);
            declaredMethod.setAccessible(true);
            return String.valueOf(declaredMethod.invoke(null, new Object[0]));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return Const.VERSION;
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
            return Const.VERSION;
        } catch (InvocationTargetException e3) {
            e3.printStackTrace();
            return Const.VERSION;
        }
    }

    public void extendFunc(String str) throws JSONException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        JSONArray jSONArrayOptJSONArray;
        Class cls = c;
        if (cls != null) {
            try {
                if (d == null) {
                    Method declaredMethod = cls.getDeclaredMethod("extendFunc", String.class, DownloadListener.class);
                    d = declaredMethod;
                    declaredMethod.setAccessible(true);
                }
                d.invoke(null, str, this.b);
                return;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e2) {
                e2.printStackTrace();
            } catch (InvocationTargetException e3) {
                e3.printStackTrace();
            }
        }
        UniSdkUtils.i("SdkDownload", "downloadFunc..., param json:".concat(String.valueOf(str)));
        if (this.f1791a == null) {
            UniSdkUtils.i("SdkDownload", "SdkDownload Context is null");
            return;
        }
        if (TextUtils.isEmpty(str)) {
            UniSdkUtils.i("SdkDownload", "params error");
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.has("methodId")) {
                String string = jSONObject.getString("methodId");
                UniSdkUtils.i("SdkDownload", "methodId=".concat(String.valueOf(string)));
                UniSdkUtils.i("SdkDownload", "SdkDownload extendFunc methodId=".concat(String.valueOf(string)));
                if (!TextUtils.isEmpty(string)) {
                    boolean z = UniSdkUtils.isDebug;
                    UniSdkUtils.i("SdkDownload", "SdkDownload extendFunc debug = ".concat(String.valueOf(z)));
                    if (z) {
                        UniSdkUtils.i("SdkDownload", "SdkDownload extendFunc is debug");
                        if (jSONObject.has("logopen")) {
                            jSONObject.put("logopen", "true");
                        }
                    }
                    String sDKVersion = Const.VERSION;
                    try {
                        if (((SdkBase) SdkMgr.getInst()).sdkInstMap.containsKey(OneTrack.Event.DOWNLOAD)) {
                            sDKVersion = ((SdkBase) SdkMgr.getInst()).sdkInstMap.get(OneTrack.Event.DOWNLOAD).getSDKVersion();
                        }
                    } catch (Exception e4) {
                        UniSdkUtils.w("SdkDownload", "get Downloader version Exception=".concat(String.valueOf(e4)));
                    }
                    UniSdkUtils.i("SdkDownload", "Downloader version =".concat(String.valueOf(sDKVersion)));
                    if (sDKVersion.startsWith("1.1.8")) {
                        if (OneTrack.Event.DOWNLOAD.equals(string)) {
                            UniSdkUtils.i("SdkDownload", "Downloader old func asyncDownloadArray");
                            DownloadProxy.getInstance().asyncDownloadArray(this.f1791a, jSONObject, this.b);
                            return;
                        }
                        if ("downloadcancel".equals(string)) {
                            DownloadProxy.stopAll();
                            return;
                        }
                        if (!"cleancache".equals(string) || !jSONObject.has("downloadid") || (jSONArrayOptJSONArray = jSONObject.optJSONArray("downloadid")) == null || jSONArrayOptJSONArray.length() <= 0) {
                            return;
                        }
                        for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
                            String strOptString = jSONArrayOptJSONArray.optString(i);
                            if (!TextUtils.isEmpty(strOptString)) {
                                UniSdkUtils.i("SdkDownload", "downloadid=".concat(String.valueOf(strOptString)));
                                DownloadProxy.clearDownloadId(this.f1791a, strOptString);
                            }
                        }
                        return;
                    }
                    UniSdkUtils.i("SdkDownload", "Downloader new func downloadFunc");
                    DownloadProxy.getInstance().downloadFunc(this.f1791a, jSONObject, this.b);
                    return;
                }
                UniSdkUtils.i("SdkDownload", "SdkDownload extendFunc methodId error");
                return;
            }
            UniSdkUtils.i("SdkDownload", "SdkDownload extendFunc params error");
        } catch (Exception e5) {
            UniSdkUtils.i("SdkDownload", "SdkDownload extendFunc Exception =".concat(String.valueOf(e5)));
        }
    }
}