package com.netease.environment;

import android.content.Context;
import android.text.TextUtils;
import com.netease.environment.OIIO0I.OIIO0OO;
import com.netease.environment.OIIO0II.OIIO;
import com.netease.environment.OIIO0II.OIIO0I;
import com.netease.environment.OIIO0II.OIIO0II;
import com.netease.environment.OIIO0II.OIIOO0O;
import com.netease.environment.OIIO0OO.OIIO00I;
import com.netease.environment.OIIO0OO.OIIO0O0;
import com.netease.environment.OIIO0OO.OIIO0OI;
import com.netease.environment.regex.Pattern;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class EnvManager {
    public static final String DEFAULT_REGULAR_VERSION = "regular_encode_sdk_default_1690513256_dcc8caa8d81457e438b6ab311153d927";
    private static final String TAG = "EnvManager";

    public static void enableLog(boolean z) {
        OIIO.OIIO00I(z);
        OIIO.OIIO0O0(TAG, "enable log : " + z);
    }

    public static long getCompileTime() {
        return OIIO0OI.OIIO0O0();
    }

    public static String getRegularVersion() {
        try {
            if (OIIO0OI.OIIO0OO() == null) {
                return "NULL";
            }
            String strOIIO00I = OIIO0O0.OIIO00I(OIIO0OI.OIIO0OO(), OIIO0OI.OIIO0I(), "");
            if (TextUtils.isEmpty(strOIIO00I)) {
                return DEFAULT_REGULAR_VERSION;
            }
            int iLastIndexOf = strOIIO00I.lastIndexOf("/");
            int iLastIndexOf2 = strOIIO00I.lastIndexOf(".");
            return (iLastIndexOf2 < 0 || iLastIndexOf < 0) ? DEFAULT_REGULAR_VERSION : strOIIO00I.substring(iLastIndexOf + 1, iLastIndexOf2);
        } catch (Exception unused) {
            return DEFAULT_REGULAR_VERSION;
        }
    }

    public static String getSdkVersion() {
        return "4.2.6";
    }

    public static void initSDK(Context context, String str, String str2, String str3) {
        try {
            long jCurrentTimeMillis = System.currentTimeMillis();
            String str4 = TAG;
            OIIO.OIIO0O0(str4, "int SDK");
            OIIO.OIIO0O0(str4, "SDK Version : " + getSdkVersion());
            if (context != null && str != null && str2 != null && str3 != null) {
                if (!str.isEmpty() && !str2.isEmpty() && !str3.isEmpty()) {
                    String lowerCase = str.toLowerCase();
                    String lowerCase2 = str2.toLowerCase();
                    OIIO0OI.OIIO00I(context);
                    OIIO0OI.OIIO0OI(lowerCase);
                    OIIO0OI.OIIO0I(lowerCase2);
                    OIIO0OI.OIIO0I0(str3);
                    OIIO0OI.OIIO0OO(Pattern.isPCRE());
                    OIIO.OIIO0O0(str4, "current pattern is PCRE : " + OIIO0OI.OIIOOO());
                    OIIO0OO.OIIO0IO(OIIO0OI.OIIO0OO());
                    OIIO.OIIO0O0(str4, "current regularVersion : " + getRegularVersion());
                    long jCurrentTimeMillis2 = System.currentTimeMillis() - jCurrentTimeMillis;
                    OIIO.OIIO0O0(str4, "init SDK local PatternMap cost time: " + jCurrentTimeMillis2 + "ms");
                    OIIO00I.OIIO00I("init", jCurrentTimeMillis2);
                    new com.netease.environment.OIIO0IO.OIIO00I(OIIO0OI.OIIO0OO()).execute(new Void[0]);
                    return;
                }
                OIIO.OIIO0O0(str4, "parameter is empty");
                return;
            }
            OIIO.OIIO0O0(str4, "parameter is null");
        } catch (Exception unused) {
            OIIO.OIIO00I(TAG, "fail to init SDK");
        }
    }

    public static void initSDKWithTestEnable(Context context, String str, String str2, String str3, boolean z) {
        OIIO0OI.OIIO00I(z);
        initSDK(context, str, str2, str3);
    }

    private static String removeMatchWords(String str) {
        try {
            if (!str.contains("match_words")) {
                return str;
            }
            JSONObject jSONObject = new JSONObject(str);
            jSONObject.remove("match_words");
            return jSONObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v2, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r5v10, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r5v16 */
    /* JADX WARN: Type inference failed for: r5v20 */
    /* JADX WARN: Type inference failed for: r5v21 */
    /* JADX WARN: Type inference failed for: r5v26 */
    /* JADX WARN: Type inference failed for: r5v27 */
    /* JADX WARN: Type inference failed for: r5v7, types: [com.netease.environment.OIIO0IO.OIIO0OO, java.util.concurrent.Callable] */
    /* JADX WARN: Type inference failed for: r5v8 */
    /* JADX WARN: Type inference failed for: r5v9 */
    private static String reviewNickname(String str) {
        String strOIIO00I;
        String simpleName = "exception";
        long jCurrentTimeMillis = System.currentTimeMillis();
        String str2 = TAG;
        OIIO.OIIO0O0(str2, "review nickname : " + str);
        if (OIIO0OI.OIIO0OO() == null) {
            OIIO.OIIO0O0(str2, "context is null");
            return OIIO0II.OIIO00I(100, "context is null", "-1", "N_1");
        }
        if (str == null || str.isEmpty()) {
            OIIO.OIIO0O0(str2, "parameter is null or empty");
            return OIIO0II.OIIO00I(100, "param is null or empty", "-1", "N_2");
        }
        if (!OIIOO0O.OIIO00I(str)) {
            OIIO.OIIO0O0(str2, "parameter code is not utf-8");
            return OIIO0II.OIIO00I(100, "parameter should be UTF-8 encode", "-1", "N_11");
        }
        if (!OIIO0O0.OIIO00I(OIIO0OI.OIIO0OO(), true)) {
            OIIO.OIIO0O0(str2, "sdk is disable");
            return OIIO0II.OIIO00I(200, "pass", "-1", "N_3");
        }
        ExecutorService executorServiceNewSingleThreadExecutor = Executors.newSingleThreadExecutor();
        ?? oiio0oo = new com.netease.environment.OIIO0IO.OIIO0OO(OIIO0OI.OIIO0OO(), str);
        Future futureSubmit = executorServiceNewSingleThreadExecutor.submit((Callable) oiio0oo);
        try {
        } catch (Exception e) {
            e = e;
        }
        try {
            try {
                try {
                    String str3 = (String) futureSubmit.get(OIIO0O0.OIIO00I(OIIO0OI.OIIO0OO(), 1000L), TimeUnit.MILLISECONDS);
                    executorServiceNewSingleThreadExecutor.shutdown();
                    oiio0oo = str3;
                } catch (InterruptedException e2) {
                    e = e2;
                    try {
                        futureSubmit.cancel(true);
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                    try {
                        simpleName = e.getClass().getSimpleName();
                    } catch (Exception e4) {
                        e4.printStackTrace();
                    }
                    strOIIO00I = OIIO0II.OIIO00I(100, simpleName, "-1", "N_5");
                    OIIO.OIIO0O0(TAG, e.toString());
                    executorServiceNewSingleThreadExecutor.shutdown();
                    oiio0oo = strOIIO00I;
                    String str4 = TAG;
                    OIIO.OIIO0O0(str4, "shut down executor");
                    OIIO.OIIO0O0(str4, "review nickname result : " + oiio0oo);
                    OIIO.OIIO0O0(str4, "it costs " + (System.currentTimeMillis() - jCurrentTimeMillis) + " ms to review nickname ");
                    return oiio0oo;
                } catch (ExecutionException e5) {
                    e = e5;
                    futureSubmit.cancel(true);
                    simpleName = e.getClass().getSimpleName();
                    strOIIO00I = OIIO0II.OIIO00I(100, simpleName, "-1", "N_5");
                    OIIO.OIIO0O0(TAG, e.toString());
                    executorServiceNewSingleThreadExecutor.shutdown();
                    oiio0oo = strOIIO00I;
                    String str42 = TAG;
                    OIIO.OIIO0O0(str42, "shut down executor");
                    OIIO.OIIO0O0(str42, "review nickname result : " + oiio0oo);
                    OIIO.OIIO0O0(str42, "it costs " + (System.currentTimeMillis() - jCurrentTimeMillis) + " ms to review nickname ");
                    return oiio0oo;
                } catch (TimeoutException e6) {
                    try {
                        futureSubmit.cancel(true);
                    } catch (Exception e7) {
                        e7.printStackTrace();
                    }
                    String strOIIO00I2 = OIIO0II.OIIO00I(100, "time out", "-1", "N_4");
                    OIIO.OIIO0O0(TAG, e6.toString());
                    executorServiceNewSingleThreadExecutor.shutdown();
                    oiio0oo = strOIIO00I2;
                } catch (Exception e8) {
                    try {
                        futureSubmit.cancel(true);
                    } catch (Exception e9) {
                        e9.printStackTrace();
                    }
                    try {
                        simpleName = e8.getClass().getSimpleName();
                    } catch (Exception e10) {
                        e10.printStackTrace();
                    }
                    strOIIO00I = OIIO0II.OIIO00I(100, simpleName, "-1", "N_6");
                    OIIO.OIIO0O0(TAG, e8.toString());
                    executorServiceNewSingleThreadExecutor.shutdown();
                    oiio0oo = strOIIO00I;
                    String str422 = TAG;
                    OIIO.OIIO0O0(str422, "shut down executor");
                    OIIO.OIIO0O0(str422, "review nickname result : " + oiio0oo);
                    OIIO.OIIO0O0(str422, "it costs " + (System.currentTimeMillis() - jCurrentTimeMillis) + " ms to review nickname ");
                    return oiio0oo;
                }
            } catch (Throwable th) {
                try {
                    executorServiceNewSingleThreadExecutor.shutdown();
                } catch (Exception e11) {
                    e11.printStackTrace();
                }
                OIIO.OIIO0O0(TAG, "shut down executor");
                throw th;
            }
        } catch (Exception e12) {
            oiio0oo = futureSubmit;
            e = e12;
            e.printStackTrace();
            String str4222 = TAG;
            OIIO.OIIO0O0(str4222, "shut down executor");
            OIIO.OIIO0O0(str4222, "review nickname result : " + oiio0oo);
            OIIO.OIIO0O0(str4222, "it costs " + (System.currentTimeMillis() - jCurrentTimeMillis) + " ms to review nickname ");
            return oiio0oo;
        }
        String str42222 = TAG;
        OIIO.OIIO0O0(str42222, "shut down executor");
        OIIO.OIIO0O0(str42222, "review nickname result : " + oiio0oo);
        OIIO.OIIO0O0(str42222, "it costs " + (System.currentTimeMillis() - jCurrentTimeMillis) + " ms to review nickname ");
        return oiio0oo;
    }

    public static String reviewNicknameV2(String str) throws JSONException {
        long jCurrentTimeMillis = System.currentTimeMillis();
        String str2 = TAG;
        OIIO.OIIO0O0(str2, "review nickname V2 : " + str);
        String strReviewNickname = reviewNickname(com.netease.environment.OIIO0I.OIIO00I.OIIO00I(str));
        long jCurrentTimeMillis2 = System.currentTimeMillis() - jCurrentTimeMillis;
        OIIO00I.OIIO0OI(str, strReviewNickname);
        String strOIIO00I = OIIO0I.OIIO00I(str, removeMatchWords(strReviewNickname));
        OIIO00I.OIIO00I(strOIIO00I);
        OIIO.OIIO0O0(str2, "it costs " + jCurrentTimeMillis2 + " ms to review nickname(v2)");
        return strOIIO00I;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v0, types: [com.netease.environment.OIIO0IO.OIIO0O0, java.util.concurrent.Callable] */
    /* JADX WARN: Type inference failed for: r4v1 */
    /* JADX WARN: Type inference failed for: r4v10 */
    /* JADX WARN: Type inference failed for: r4v11 */
    /* JADX WARN: Type inference failed for: r4v16 */
    /* JADX WARN: Type inference failed for: r4v17 */
    /* JADX WARN: Type inference failed for: r4v2 */
    /* JADX WARN: Type inference failed for: r4v3, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r4v7 */
    /* JADX WARN: Type inference failed for: r8v2, types: [java.lang.StringBuilder] */
    private static String reviewWords(String str, String str2, String str3) {
        String strOIIO00I;
        String simpleName = "exception";
        ExecutorService executorServiceNewSingleThreadExecutor = Executors.newSingleThreadExecutor();
        ?? oiio0o0 = new com.netease.environment.OIIO0IO.OIIO0O0(OIIO0OI.OIIO0OO(), str3, str, str2);
        Future futureSubmit = executorServiceNewSingleThreadExecutor.submit((Callable) oiio0o0);
        try {
        } catch (Exception e) {
            e = e;
        }
        try {
            try {
                try {
                    String str4 = (String) futureSubmit.get(OIIO0O0.OIIO00I(OIIO0OI.OIIO0OO(), 1000L), TimeUnit.MILLISECONDS);
                    executorServiceNewSingleThreadExecutor.shutdown();
                    oiio0o0 = str4;
                } catch (InterruptedException e2) {
                    e = e2;
                    try {
                        futureSubmit.cancel(true);
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                    try {
                        simpleName = e.getClass().getSimpleName();
                    } catch (Exception e4) {
                        e4.printStackTrace();
                    }
                    strOIIO00I = OIIO0II.OIIO00I(100, simpleName, "-1", "V_5");
                    OIIO.OIIO0O0(TAG, e.toString());
                    executorServiceNewSingleThreadExecutor.shutdown();
                    oiio0o0 = strOIIO00I;
                    String str5 = TAG;
                    OIIO.OIIO0O0(str5, "shut down executor");
                    OIIO.OIIO0O0(str5, "reviews words result : " + oiio0o0);
                    return oiio0o0;
                } catch (ExecutionException e5) {
                    e = e5;
                    futureSubmit.cancel(true);
                    simpleName = e.getClass().getSimpleName();
                    strOIIO00I = OIIO0II.OIIO00I(100, simpleName, "-1", "V_5");
                    OIIO.OIIO0O0(TAG, e.toString());
                    executorServiceNewSingleThreadExecutor.shutdown();
                    oiio0o0 = strOIIO00I;
                    String str52 = TAG;
                    OIIO.OIIO0O0(str52, "shut down executor");
                    OIIO.OIIO0O0(str52, "reviews words result : " + oiio0o0);
                    return oiio0o0;
                } catch (TimeoutException e6) {
                    try {
                        futureSubmit.cancel(true);
                    } catch (Exception e7) {
                        e7.printStackTrace();
                    }
                    String strOIIO00I2 = OIIO0II.OIIO00I(100, "time out", "-1", "V_4");
                    OIIO.OIIO0O0(TAG, e6.toString());
                    executorServiceNewSingleThreadExecutor.shutdown();
                    oiio0o0 = strOIIO00I2;
                } catch (Exception e8) {
                    try {
                        futureSubmit.cancel(true);
                    } catch (Exception e9) {
                        e9.printStackTrace();
                    }
                    try {
                        simpleName = e8.getClass().getSimpleName();
                    } catch (Exception e10) {
                        e10.printStackTrace();
                    }
                    strOIIO00I = OIIO0II.OIIO00I(100, simpleName, "-1", "V_6");
                    OIIO.OIIO0O0(TAG, e8.toString());
                    executorServiceNewSingleThreadExecutor.shutdown();
                    oiio0o0 = strOIIO00I;
                    String str522 = TAG;
                    OIIO.OIIO0O0(str522, "shut down executor");
                    OIIO.OIIO0O0(str522, "reviews words result : " + oiio0o0);
                    return oiio0o0;
                }
            } catch (Throwable th) {
                try {
                    executorServiceNewSingleThreadExecutor.shutdown();
                } catch (Exception e11) {
                    e11.printStackTrace();
                }
                OIIO.OIIO0O0(TAG, "shut down executor");
                throw th;
            }
        } catch (Exception e12) {
            oiio0o0 = futureSubmit;
            e = e12;
            e.printStackTrace();
            String str5222 = TAG;
            OIIO.OIIO0O0(str5222, "shut down executor");
            OIIO.OIIO0O0(str5222, "reviews words result : " + oiio0o0);
            return oiio0o0;
        }
        String str52222 = TAG;
        OIIO.OIIO0O0(str52222, "shut down executor");
        OIIO.OIIO0O0(str52222, "reviews words result : " + oiio0o0);
        return oiio0o0;
    }

    public static String reviewWordsV2(String str, String str2, String str3) throws JSONException {
        long jCurrentTimeMillis = System.currentTimeMillis();
        String str4 = TAG;
        OIIO.OIIO0O0(str4, "review words V2 : level=" + str + "_channel=" + str2 + "_content=" + str3);
        if (OIIO0OI.OIIO0OO() == null) {
            OIIO.OIIO0O0(str4, "context is null");
            return OIIO0II.OIIO00I(100, "context is null", "-1", "V_1");
        }
        if (str == null || str.isEmpty() || str2 == null || str2.isEmpty() || str3 == null || str3.isEmpty()) {
            OIIO.OIIO0O0(str4, "parameter is null or empty");
            return OIIO0II.OIIO00I(100, "param is null or empty", "-1", "V_2");
        }
        if (!OIIOO0O.OIIO00I(str3) || !OIIOO0O.OIIO00I(str) || !OIIOO0O.OIIO00I(str2)) {
            OIIO.OIIO0O0(str4, "parameter code is not utf-8");
            return OIIO0II.OIIO00I(100, "parameter should be UTF-8 encode", "-1", "V_8");
        }
        if (!OIIO0O0.OIIO00I(OIIO0OI.OIIO0OO(), true)) {
            OIIO.OIIO0O0(str4, "sdk is disable");
            return OIIO0II.OIIO00I(200, "pass", "-1", "V_3");
        }
        String strReviewWords = reviewWords(str, str2, str3);
        long jCurrentTimeMillis2 = System.currentTimeMillis() - jCurrentTimeMillis;
        OIIO00I.OIIO00I(str, str2, str3, strReviewWords);
        String strOIIO00I = OIIO0I.OIIO00I(str3, removeMatchWords(strReviewWords));
        OIIO00I.OIIO00I(strOIIO00I);
        OIIO.OIIO0O0(str4, "it costs " + jCurrentTimeMillis2 + " ms to review words(v2)");
        return strOIIO00I;
    }

    public static void setAndroidID(String str) {
        OIIO0OI.OIIO0O0(str);
    }

    public static String setExtraInfo(String str) {
        OIIO.OIIO0O0(TAG, "extraInfo: " + str);
        if (TextUtils.isEmpty(str)) {
            return "extraInfo is empty";
        }
        try {
            new JSONObject(str);
            OIIO0OI.OIIO00I(str);
            return "success";
        } catch (JSONException e) {
            e.printStackTrace();
            return "extraInfo is not json string";
        }
    }
}