package com.netease.environment.OIIO0OO;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.core.app.NotificationCompat;
import com.facebook.common.util.UriUtil;
import com.netease.androidcrashhandler.Const;
import com.netease.environment.EnvManager;
import com.netease.environment.OIIO0II.OIIO;
import com.netease.environment.OIIO0II.OIIOO0;
import com.netease.ntunisdk.base.ConstProp;
import com.tencent.open.SocialConstants;
import com.xiaomi.onetrack.OneTrack;
import com.xiaomi.onetrack.b.a;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: LogConfig.java */
/* loaded from: classes5.dex */
public class OIIO00I {

    /* renamed from: OIIO00I, reason: collision with root package name */
    private static String f1573OIIO00I = "OIIO00I";
    private static ExecutorService OIIO0I0;
    private static SharedPreferences OIIO0OI;
    private static Object OIIO0O0 = new Object();
    private static Object OIIO0OO = new Object();

    /* compiled from: LogConfig.java */
    /* renamed from: com.netease.environment.OIIO0OO.OIIO00I$OIIO00I, reason: collision with other inner class name */
    private static class RunnableC0064OIIO00I implements Runnable {

        /* renamed from: OIIO00I, reason: collision with root package name */
        String f1574OIIO00I;

        public RunnableC0064OIIO00I(String str) {
            this.f1574OIIO00I = str;
        }

        @Override // java.lang.Runnable
        public void run() {
            OIIO00I.OIIO0OO(this.f1574OIIO00I, "u1");
        }
    }

    private static JSONObject OIIO0I(Context context) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        String strOIIO00I = OIIO00I(context, "init", "");
        if (!TextUtils.isEmpty(strOIIO00I)) {
            jSONObject.put("init", strOIIO00I);
        }
        String strOIIO00I2 = OIIO00I(context, "getUrl", "");
        if (!TextUtils.isEmpty(strOIIO00I2)) {
            jSONObject.put("getUrl", strOIIO00I2);
        }
        String strOIIO00I3 = OIIO00I(context, OneTrack.Event.DOWNLOAD, "");
        if (!TextUtils.isEmpty(strOIIO00I3)) {
            jSONObject.put(OneTrack.Event.DOWNLOAD, strOIIO00I3);
        }
        return jSONObject;
    }

    private static SharedPreferences OIIO0I0(Context context) {
        if (OIIO0OI == null) {
            OIIO0OI = context.getApplicationContext().getSharedPreferences("environment_preferences_log", 32768);
        }
        return OIIO0OI;
    }

    public static void OIIO0IO(Context context) {
        SharedPreferences.Editor editorEdit;
        if (context == null || (editorEdit = OIIO0I0(context).edit()) == null) {
            return;
        }
        editorEdit.clear();
        editorEdit.commit();
    }

    public static void OIIO0O0(Context context, String str, int i) {
        SharedPreferences.Editor editorEdit;
        if (context == null || (editorEdit = OIIO0I0(context).edit()) == null) {
            return;
        }
        editorEdit.putInt(str, i);
        editorEdit.apply();
    }

    public static void OIIO0OI(String str, String str2) {
        JSONObject jSONObject;
        int iOptInt;
        try {
            if (OIIO0OI.OIIOOOI() && (iOptInt = (jSONObject = new JSONObject(str2)).optInt("code")) != 100 && iOptInt != 200) {
                jSONObject.put("content_type", "nickName");
                String strOptString = jSONObject.optString("match_words");
                jSONObject.put(UriUtil.LOCAL_CONTENT_SCHEME, com.netease.environment.OIIO0II.OIIO00I.OIIO00I(str));
                jSONObject.put("match_words", com.netease.environment.OIIO0II.OIIO00I.OIIO00I(strOptString));
                jSONObject.put("channel", "");
                jSONObject.put(a.d, "");
                jSONObject.put("regularIds", "");
                OIIO00I(jSONObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String OIIO0OO(Context context) throws JSONException {
        if (context == null) {
            return null;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("gameid", OIIO0OI.OIIO0I());
            jSONObject2.put("deviceid", OIIO0O0(context));
            jSONObject2.put("version", "4.2.6");
            jSONObject2.put(NotificationCompat.CATEGORY_SYSTEM, "android");
            jSONObject2.put("devicename", OIIO00I());
            jSONObject2.put("ispcre", OIIO0OI.OIIOOO() ? "1" : "0");
            try {
                jSONObject2.put("network", OIIOO0.OIIO0O0(context));
            } catch (Exception e) {
                e.printStackTrace();
            }
            jSONObject.put(Const.ParamKey.INFO, jSONObject2);
            jSONObject.put("time", OIIO0I(context));
            JSONObject jSONObject3 = new JSONObject();
            JSONObject jSONObject4 = new JSONObject();
            Map<String, ?> mapOIIO00I = OIIO00I(context);
            if (mapOIIO00I != null && mapOIIO00I.size() > 0) {
                for (String str : mapOIIO00I.keySet()) {
                    if (str != null && !str.equals("init") && !str.equals("getUrl") && !str.equals(OneTrack.Event.DOWNLOAD) && !str.startsWith("new*")) {
                        if (str.equals("300") || str.equals("301") || str.equals("400") || str.equals("401")) {
                            jSONObject3.put(str, new JSONObject(String.valueOf(mapOIIO00I.get(str))));
                        } else if (str.matches("^\\d*?$")) {
                            jSONObject3.put(str, OIIO0O0(String.valueOf(mapOIIO00I.get(str))));
                        } else {
                            jSONObject4.put(str, mapOIIO00I.get(str));
                        }
                    }
                }
            }
            jSONObject.put("regexcode", jSONObject3);
            jSONObject.put("errors", jSONObject4);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return jSONObject.toString();
    }

    public static int OIIO00I(Context context, String str, int i) {
        return context == null ? i : OIIO0I0(context).getInt(str, i);
    }

    private static String OIIO00I(Context context, String str, String str2) {
        return context == null ? str2 : OIIO0I0(context).getString(str, str2);
    }

    public static Map<String, ?> OIIO00I(Context context) {
        if (context == null) {
            return null;
        }
        return OIIO0I0(context).getAll();
    }

    public static void OIIO00I(Exception exc) {
        OIIO00I(exc, (String) null);
    }

    private static void OIIO0O0(Context context, String str, String str2) {
        SharedPreferences.Editor editorEdit;
        if (context == null || (editorEdit = OIIO0I0(context).edit()) == null) {
            return;
        }
        editorEdit.putString(str, str2);
        editorEdit.apply();
    }

    public static void OIIO00I(Exception exc, String str) {
        if (exc == null || OIIO0OI.OIIO0OO() == null) {
            return;
        }
        try {
            String string = exc.toString();
            if (string == null || string.indexOf(":") <= 0) {
                return;
            }
            String strSubstring = string.substring(0, string.indexOf(":"));
            if (str != null && !str.isEmpty()) {
                strSubstring = str + "_" + strSubstring;
            }
            int iOIIO00I = OIIO00I(OIIO0OI.OIIO0OO(), strSubstring, 0);
            OIIO.OIIO0O0(f1573OIIO00I, "the count of exception log for key [" + strSubstring + "] is " + iOIIO00I);
            OIIO0O0(OIIO0OI.OIIO0OO(), strSubstring, iOIIO00I + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String OIIO0O0(Context context) {
        String strOIIO00I = OIIO0OI.OIIO00I();
        if (!TextUtils.isEmpty(strOIIO00I)) {
            return strOIIO00I;
        }
        if (context == null) {
            return "0000000000";
        }
        try {
            String string = Settings.Secure.getString(context.getContentResolver(), "android_id");
            OIIO0OI.OIIO0O0(string);
            return string;
        } catch (Exception e) {
            e.printStackTrace();
            OIIO.OIIO00I(f1573OIIO00I, "fail to get android id");
            return "0000000000";
        }
    }

    /* compiled from: LogConfig.java */
    private static class OIIO0O0 implements Runnable {

        /* renamed from: OIIO00I, reason: collision with root package name */
        JSONObject f1575OIIO00I;

        public OIIO0O0(JSONObject jSONObject) {
            this.f1575OIIO00I = jSONObject;
        }

        private Map<String, String> OIIO00I() throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
            HashMap map = new HashMap();
            try {
                Object objInvoke = Class.forName("com.netease.ntunisdk.base.SdkMgr").getDeclaredMethod("getInst", new Class[0]).invoke(null, new Object[0]);
                Method method = objInvoke.getClass().getMethod("getPropStr", String.class);
                String str = (String) method.invoke(objInvoke, ConstProp.USERINFO_HOSTID);
                String str2 = (String) method.invoke(objInvoke, ConstProp.USERINFO_UID);
                String str3 = (String) method.invoke(objInvoke, ConstProp.FULL_UID);
                map.put("server", str);
                map.put("role_id", str2);
                map.put("account_id", str3);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return map;
        }

        private String OIIO0O0() throws JSONException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
            if (this.f1575OIIO00I == null) {
                this.f1575OIIO00I = new JSONObject();
            }
            try {
                this.f1575OIIO00I.put("os_name", "android");
                this.f1575OIIO00I.put("udid", OIIO00I.OIIO0O0(OIIO0OI.OIIO0OO()));
                this.f1575OIIO00I.put("os_ver", Build.VERSION.RELEASE);
                this.f1575OIIO00I.put("extra_info", OIIO0OI.OIIO0I0());
                this.f1575OIIO00I.put("type", "match");
                this.f1575OIIO00I.put(SocialConstants.PARAM_SOURCE, "netease_p1");
                this.f1575OIIO00I.put("project", "dep87");
                this.f1575OIIO00I.put("time", String.valueOf(System.currentTimeMillis()));
                this.f1575OIIO00I.put("gameid", OIIO0OI.OIIO0I());
                this.f1575OIIO00I.put("regular_version", EnvManager.getRegularVersion());
                this.f1575OIIO00I.put("sdk_version", EnvManager.getSdkVersion());
                Map<String, String> mapOIIO00I = OIIO00I();
                this.f1575OIIO00I.put("account_id", OIIO00I(mapOIIO00I, "account_id"));
                this.f1575OIIO00I.put("server", OIIO00I(mapOIIO00I, "server"));
                this.f1575OIIO00I.put("role_id", OIIO00I(mapOIIO00I, "role_id"));
                try {
                    this.f1575OIIO00I.put("network", OIIOO0.OIIO0O0(OIIO0OI.OIIO0OO()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
            return this.f1575OIIO00I.toString();
        }

        @Override // java.lang.Runnable
        public void run() {
            OIIO00I.OIIO0OO(OIIO0O0(), "u2");
        }

        private String OIIO00I(Map<String, String> map, String str) {
            String str2 = map.get(str);
            return TextUtils.isEmpty(str2) ? "" : str2;
        }
    }

    public static void OIIO00I(int i, String str) {
        String strSubstring;
        boolean z;
        if (OIIO0OI.OIIO0OO() == null || str == null || str.equals("")) {
            return;
        }
        try {
            synchronized (OIIO0O0) {
                String strValueOf = String.valueOf(i);
                String strOIIO00I = OIIO00I(OIIO0OI.OIIO0OO(), strValueOf, "");
                OIIO.OIIO0O0(f1573OIIO00I, "before the count of review log for key [" + strValueOf + "] is " + strOIIO00I);
                if (TextUtils.isEmpty(strOIIO00I)) {
                    strSubstring = str + "-1";
                } else {
                    String[] strArrSplit = strOIIO00I.split(";");
                    if (strArrSplit.length != 0) {
                        int i2 = 0;
                        while (true) {
                            if (i2 >= strArrSplit.length) {
                                z = false;
                                break;
                            }
                            if (!TextUtils.isEmpty(strArrSplit[i2])) {
                                String[] strArrSplit2 = strArrSplit[i2].split("-");
                                if (strArrSplit2.length == 2 && TextUtils.equals(strArrSplit2[0], str)) {
                                    int iIntValue = Integer.valueOf(strArrSplit2[1]).intValue() + 1;
                                    OIIO.OIIO0O0(f1573OIIO00I, "the count of review log for reason [" + strArrSplit2[0] + "] is " + iIntValue);
                                    StringBuilder sb = new StringBuilder();
                                    sb.append(strArrSplit2[0]);
                                    sb.append("-");
                                    sb.append(iIntValue);
                                    strArrSplit[i2] = sb.toString();
                                    z = true;
                                    break;
                                }
                            }
                            i2++;
                        }
                        StringBuilder sb2 = new StringBuilder();
                        for (String str2 : strArrSplit) {
                            sb2.append(str2);
                            sb2.append(";");
                        }
                        String string = sb2.toString();
                        strSubstring = z ? string.substring(0, string.length() - 1) : string + str + "-1";
                    }
                    OIIO.OIIO0O0(f1573OIIO00I, "after the count of review log for key [" + strValueOf + "] is " + strOIIO00I);
                    OIIO0O0(OIIO0OI.OIIO0OO(), strValueOf, strOIIO00I);
                }
                strOIIO00I = strSubstring;
                OIIO.OIIO0O0(f1573OIIO00I, "after the count of review log for key [" + strValueOf + "] is " + strOIIO00I);
                OIIO0O0(OIIO0OI.OIIO0OO(), strValueOf, strOIIO00I);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static JSONObject OIIO0O0(String str) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            if (!TextUtils.isEmpty(str)) {
                for (String str2 : str.split(";")) {
                    String[] strArrSplit = str2.split("-");
                    if (strArrSplit.length == 2) {
                        jSONObject.put(strArrSplit[0], Integer.valueOf(strArrSplit[1]));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jSONObject;
    }

    private static String OIIO0OI(Context context) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            Map<String, ?> mapOIIO00I = OIIO00I(context);
            if (mapOIIO00I != null && mapOIIO00I.size() > 0) {
                for (String str : mapOIIO00I.keySet()) {
                    if (str != null) {
                        if (str.startsWith("new*")) {
                            jSONObject.put(str.substring(4), mapOIIO00I.get(str));
                        } else if (str.equals("300") || str.equals("301") || str.equals("400") || str.equals("401")) {
                            JSONObject jSONObject2 = new JSONObject(String.valueOf(mapOIIO00I.get(str)));
                            Iterator<String> itKeys = jSONObject2.keys();
                            int iOptInt = 0;
                            while (itKeys.hasNext()) {
                                iOptInt += jSONObject2.optInt(itKeys.next());
                            }
                            jSONObject.put(str, iOptInt);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jSONObject.keys().hasNext() ? jSONObject.toString() : "";
    }

    private static String OIIO0O0() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("os_name", "android");
            jSONObject.put("udid", OIIO0O0(OIIO0OI.OIIO0OO()));
            jSONObject.put("os_ver", Build.VERSION.RELEASE);
            jSONObject.put("type", "init");
            jSONObject.put("project", "dep87");
            jSONObject.put("time", String.valueOf(System.currentTimeMillis()));
            jSONObject.put("gameid", OIIO0OI.OIIO0I());
            jSONObject.put("sdk_version", EnvManager.getSdkVersion());
            jSONObject.put("device_model", Build.MODEL);
            jSONObject.put("use_time", OIIO0I(OIIO0OI.OIIO0OO()).toString());
            jSONObject.put("regex_code", OIIO0OI(OIIO0OI.OIIO0OO()));
            try {
                jSONObject.put("network", OIIOO0.OIIO0O0(OIIO0OI.OIIO0OO()));
            } catch (Exception unused) {
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject.toString();
    }

    public static void OIIO0OO() {
        try {
            if (OIIO0OI.OIIOOOI()) {
                if (OIIO0I0 == null) {
                    OIIO0I0 = Executors.newSingleThreadExecutor();
                }
                OIIO0I0.execute(new RunnableC0064OIIO00I(OIIO0O0()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void OIIO0O0(String str, String str2) throws JSONException {
        JSONObject jSONObject;
        OIIO.OIIO0O0(f1573OIIO00I, "drpf result is: " + str + " " + str2);
        String strOIIO00I = OIIO00I(OIIO0OI.OIIO0OO(), str2, "");
        if (TextUtils.isEmpty(strOIIO00I)) {
            jSONObject = new JSONObject();
        } else {
            jSONObject = new JSONObject(strOIIO00I);
        }
        jSONObject.put(str, jSONObject.optInt(str) + 1);
        OIIO0O0(OIIO0OI.OIIO0OO(), str2, jSONObject.toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void OIIO0OO(String str, String str2) {
        OIIO.OIIO0O0(f1573OIIO00I, "Loghub data :" + str);
        try {
            String strOIIO0OI = OIIO0OI.OIIO0OI();
            if (TextUtils.isEmpty(strOIIO0OI)) {
                OIIO.OIIO0O0(f1573OIIO00I, "drpf host is empty");
                return;
            }
            String str3 = "https://" + strOIIO0OI;
            OIIO.OIIO0O0(f1573OIIO00I, "drpf url is: " + str3);
            String strOIIO00I = com.netease.environment.OIIO0OI.OIIO0O0.OIIO00I(str3, str);
            if (!TextUtils.isEmpty(strOIIO00I) && strOIIO00I.equals("ok")) {
                OIIO0O0(str2, "300");
            } else {
                OIIO0O0(str2, "301");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void OIIO00I(String str, long j) {
        if (TextUtils.isEmpty(str) || OIIO0OI.OIIO0OO() == null) {
            return;
        }
        OIIO0O0(OIIO0OI.OIIO0OO(), str, String.valueOf(j));
    }

    private static String OIIO00I() {
        return Build.MODEL + "#" + Build.VERSION.RELEASE;
    }

    public static void OIIO00I(String str, String str2, String str3, String str4) {
        JSONObject jSONObject;
        int iOptInt;
        try {
            if (OIIO0OI.OIIOOOI() && (iOptInt = (jSONObject = new JSONObject(str4)).optInt("code")) != 100 && iOptInt != 200) {
                if (jSONObject.optJSONArray("regularIds") == null) {
                    jSONObject.put("regularIds", "");
                }
                if (TextUtils.isEmpty(jSONObject.optString("regularId"))) {
                    jSONObject.put("regularId", "");
                }
                jSONObject.put("content_type", "words");
                jSONObject.put(UriUtil.LOCAL_CONTENT_SCHEME, com.netease.environment.OIIO0II.OIIO00I.OIIO00I(str3));
                jSONObject.put("match_words", com.netease.environment.OIIO0II.OIIO00I.OIIO00I(jSONObject.optString("match_words")));
                jSONObject.put(a.d, str);
                jSONObject.put("channel", str2);
                OIIO00I(jSONObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void OIIO00I(JSONObject jSONObject) {
        if (OIIO0I0 == null) {
            OIIO0I0 = Executors.newSingleThreadExecutor();
        }
        OIIO0I0.execute(new OIIO0O0(jSONObject));
    }

    public static void OIIO00I(String str) {
        try {
            synchronized (OIIO0OO) {
                String str2 = "new*" + new JSONObject(str).optInt("code");
                int iOIIO00I = OIIO00I(OIIO0OI.OIIO0OO(), str2, 0);
                OIIO.OIIO0O0(f1573OIIO00I, "the count of code [" + str2 + "] is " + iOIIO00I);
                OIIO0O0(OIIO0OI.OIIO0OO(), str2, iOIIO00I + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            OIIO.OIIO00I(f1573OIIO00I, e.getMessage());
        }
    }

    public static void OIIO00I(String str, String str2, String str3) throws JSONException {
        JSONObject jSONObject;
        StringBuilder sb = new StringBuilder();
        if (str.equals("nickname")) {
            sb.append("N_");
        } else if (str.equals("intercept")) {
            sb.append("I_");
        } else if (str.equals("shield")) {
            sb.append("S_");
        } else if (str.equals("replace")) {
            sb.append("R_");
        } else if (str.equals("remind")) {
            sb.append("M_");
        }
        sb.append(str2);
        String string = sb.toString();
        OIIO.OIIO0O0(f1573OIIO00I, "saveCompileFailedLog result is: " + string + " " + str3);
        String strOIIO00I = OIIO00I(OIIO0OI.OIIO0OO(), str3, "");
        if (TextUtils.isEmpty(strOIIO00I)) {
            jSONObject = new JSONObject();
        } else {
            jSONObject = new JSONObject(strOIIO00I);
        }
        jSONObject.put(string, jSONObject.optInt(string) + 1);
        OIIO0O0(OIIO0OI.OIIO0OO(), str3, jSONObject.toString());
    }
}