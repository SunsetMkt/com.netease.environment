package com.netease.environment.OIIO0I;

import android.content.Context;
import android.text.TextUtils;
import com.netease.environment.OIIO0II.OIIO;
import com.netease.environment.OIIO0II.OIIO0I0;
import com.netease.environment.OIIO0OO.OIIO0OI;
import com.netease.environment.regex.Pattern;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: RegexGetter.java */
/* loaded from: classes4.dex */
public class OIIO0OO {

    /* renamed from: OIIO00I, reason: collision with root package name */
    private static String f1557OIIO00I = "OIIO0OO";
    private static Map<String, Pattern> OIIO0I;
    private static Map<String, Pattern> OIIO0I0;
    private static Map<String, Pattern> OIIO0O0;
    private static Map<String, Pattern> OIIO0OI;
    private static Map<String, Pattern> OIIO0OO;

    /* compiled from: RegexGetter.java */
    class OIIO00I implements Comparator<String> {
        OIIO00I() {
        }

        @Override // java.util.Comparator
        /* renamed from: OIIO00I, reason: merged with bridge method [inline-methods] */
        public int compare(String str, String str2) {
            if (str == null || str2 == null) {
                return 0;
            }
            int iCompareTo = Integer.valueOf(str.length()).compareTo(Integer.valueOf(str2.length()));
            return iCompareTo != 0 ? iCompareTo : str.compareTo(str2);
        }
    }

    public static Map<String, Pattern> OIIO00I(Context context) {
        if (OIIO0OI == null) {
            if (OIIO0OI.OIIOOO()) {
                OIIO.OIIO0O0(f1557OIIO00I, "get sInterceptPatternMap pattern list from memory pcre");
                OIIO0IO(context);
            } else {
                OIIO.OIIO0O0(f1557OIIO00I, "get sInterceptPatternMap pattern list from memory jdk");
                OIIO0OI = OIIO00I(context, "intercept");
            }
        }
        return OIIO0OI;
    }

    public static Map<String, Pattern> OIIO0I(Context context) {
        if (OIIO0OO == null) {
            if (OIIO0OI.OIIOOO()) {
                OIIO.OIIO0O0(f1557OIIO00I, "get sShieldPatternMap pattern list from memory pcre");
                OIIO0IO(context);
            } else {
                OIIO.OIIO0O0(f1557OIIO00I, "get sShieldPatternMap pattern list from memory jdk");
                OIIO0OO = OIIO00I(context, "shield");
            }
        }
        return OIIO0OO;
    }

    public static Map<String, Pattern> OIIO0I0(Context context) {
        if (OIIO0I0 == null) {
            if (OIIO0OI.OIIOOO()) {
                OIIO.OIIO0O0(f1557OIIO00I, "get sReplacePatternMap pattern list from memory pcre");
                OIIO0IO(context);
            } else {
                OIIO.OIIO0O0(f1557OIIO00I, "get sReplacePatternMap pattern list from memory jdk");
                OIIO0I0 = OIIO00I(context, "replace");
            }
        }
        return OIIO0I0;
    }

    public static void OIIO0IO(Context context) {
        try {
            if (OIIO0OI.OIIOOO()) {
                OIIO.OIIO0O0(f1557OIIO00I, "set pattern list with file pcre");
                if (!Pattern.initLocalMaps(context, OIIO0I0.OIIO0O0(context), OIIO0OI.OIIOO0())) {
                    OIIO.OIIO0O0(f1557OIIO00I, "use default regex data");
                    OIIO0O0 oiio0o0 = new OIIO0O0();
                    Pattern.initMapsMemory(context, oiio0o0.OIIO00I(), oiio0o0.OIIO0O0());
                }
            } else {
                OIIO.OIIO0O0(f1557OIIO00I, "set pattern list with file jdk");
                JSONObject jSONObjectOIIO0OO = OIIO0OO(context);
                long jCurrentTimeMillis = System.currentTimeMillis();
                OIIO0O0 = OIIO00I(jSONObjectOIIO0OO, "nickname");
                OIIO0OO = OIIO00I(jSONObjectOIIO0OO, "shield");
                OIIO0OI = OIIO00I(jSONObjectOIIO0OO, "intercept");
                OIIO0I0 = OIIO00I(jSONObjectOIIO0OO, "replace");
                OIIO0I = OIIO00I(jSONObjectOIIO0OO, "remind", true);
                long jCurrentTimeMillis2 = System.currentTimeMillis() - jCurrentTimeMillis;
                com.netease.environment.OIIO0OO.OIIO00I.OIIO00I("compile_time", jCurrentTimeMillis2);
                OIIO.OIIO0O0(f1557OIIO00I, "init compile pattern time: " + jCurrentTimeMillis2);
                OIIO0OI.OIIO00I(jCurrentTimeMillis2);
            }
        } catch (Exception e) {
            OIIO.OIIO0O0(f1557OIIO00I, "fail to save pattern list on memory : " + e.getMessage());
        }
    }

    public static Map<String, Pattern> OIIO0O0(Context context) {
        if (OIIO0O0 == null) {
            if (OIIO0OI.OIIOOO()) {
                OIIO.OIIO0O0(f1557OIIO00I, "get sNicknamePatternMap pattern list from memory pcre");
                OIIO0IO(context);
            } else {
                OIIO.OIIO0O0(f1557OIIO00I, "get sNicknamePatternMap pattern list from memory jdk");
                OIIO0O0 = OIIO00I(context, "nickname");
            }
        }
        return OIIO0O0;
    }

    public static Map<String, Pattern> OIIO0OI(Context context) {
        if (OIIO0I == null) {
            if (OIIO0OI.OIIOOO()) {
                OIIO.OIIO0O0(f1557OIIO00I, "get RemindPatternMap pattern list from memory pcre");
                OIIO0IO(context);
            } else {
                OIIO.OIIO0O0(f1557OIIO00I, "get RemindPatternMap pattern list from memory jdk");
                OIIO0I = OIIO00I(OIIO0OO(context), "remind", true);
            }
        }
        return OIIO0I;
    }

    public static JSONObject OIIO0OO(Context context) throws BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        String strOIIO00I = com.netease.environment.OIIO0II.OIIO0OI.OIIO00I(OIIO0I0.OIIO00I(OIIO0I0.OIIO0O0(context)), OIIO0OI.OIIOO0());
        try {
        } catch (Exception e) {
            OIIO.OIIO0O0(f1557OIIO00I, "fail to parse json string : " + e.getMessage());
        }
        JSONObject jSONObject = !TextUtils.isEmpty(strOIIO00I) ? new JSONObject(strOIIO00I) : null;
        if (jSONObject == null) {
            OIIO.OIIO0O0(f1557OIIO00I, "use default regex data");
            jSONObject = new OIIO0O0().OIIO0OO();
        }
        return jSONObject.optJSONObject("regex");
    }

    private static Map<String, Pattern> OIIO00I(Context context, String str) {
        return OIIO00I(OIIO0OO(context), str);
    }

    private static Map<String, Pattern> OIIO00I(JSONObject jSONObject, String str) {
        return OIIO00I(jSONObject, str, false);
    }

    private static Map<String, Pattern> OIIO00I(JSONObject jSONObject, String str, boolean z) throws JSONException {
        Pattern patternCompile;
        TreeMap treeMap = new TreeMap(new OIIO00I());
        JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject(str);
        if (jSONObjectOptJSONObject != null) {
            Iterator<String> itKeys = jSONObjectOptJSONObject.keys();
            int i = z ? 514 : 2;
            while (itKeys.hasNext()) {
                String next = itKeys.next();
                String strOptString = jSONObjectOptJSONObject.optString(next);
                try {
                    patternCompile = Pattern.compile(strOptString, i);
                } catch (Exception e) {
                    com.netease.environment.OIIO0OO.OIIO00I.OIIO00I(str, next, "401");
                    com.netease.environment.OIIO0OO.OIIO00I.OIIO00I(e, "compile");
                    OIIO.OIIO0O0(f1557OIIO00I, "regex compile error : " + e.getMessage());
                    OIIO.OIIO0O0(f1557OIIO00I, "fail to compile pattern of name: " + str + " key: " + next + " regular:" + strOptString);
                    patternCompile = null;
                }
                if (patternCompile != null) {
                    treeMap.put(next, patternCompile);
                }
            }
            OIIO.OIIO0O0(f1557OIIO00I, "get " + str + " pattern list from file");
        }
        return treeMap;
    }

    public static void OIIO00I(JSONObject jSONObject) {
        try {
            OIIO.OIIO0O0(f1557OIIO00I, "set pattern list with json object");
            long jCurrentTimeMillis = System.currentTimeMillis();
            OIIO0O0 = OIIO00I(jSONObject, "nickname");
            OIIO0OO = OIIO00I(jSONObject, "shield");
            OIIO0OI = OIIO00I(jSONObject, "intercept");
            OIIO0I0 = OIIO00I(jSONObject, "replace");
            OIIO0I = OIIO00I(jSONObject, "remind", true);
            long jCurrentTimeMillis2 = System.currentTimeMillis() - jCurrentTimeMillis;
            OIIO.OIIO0O0(f1557OIIO00I, "compile pattern time: " + jCurrentTimeMillis2);
            OIIO0OI.OIIO00I(jCurrentTimeMillis2);
        } catch (Exception e) {
            OIIO.OIIO0O0(f1557OIIO00I, "fail to save pattern list on memory : " + e.getMessage());
        }
    }

    public static void OIIO00I(int i, Map<String, Pattern> map) {
        if (i == 0) {
            OIIO0O0 = map;
            OIIO.OIIO0O0(f1557OIIO00I, "initPatternPcre type:" + i + " sNicknamePatternMap:" + OIIO0O0.size());
            return;
        }
        if (i == 1) {
            OIIO0OO = map;
            OIIO.OIIO0O0(f1557OIIO00I, "initPatternPcre type:" + i + " sShieldPatternMap:" + OIIO0OO.size());
            return;
        }
        if (i == 2) {
            OIIO0OI = map;
            OIIO.OIIO0O0(f1557OIIO00I, "initPatternPcre type:" + i + " sInterceptPatternMap:" + OIIO0OI.size());
            return;
        }
        if (i == 3) {
            OIIO0I0 = map;
            OIIO.OIIO0O0(f1557OIIO00I, "initPatternPcre type:" + i + " sReplacePatternMap:" + OIIO0I0.size());
            return;
        }
        if (i != 4) {
            OIIO.OIIO0O0(f1557OIIO00I, "initPatternPcre unkonw type:" + i);
            return;
        }
        OIIO0I = map;
        OIIO.OIIO0O0(f1557OIIO00I, "initPatternPcre type:" + i + " sRemindPatternMap:" + OIIO0I.size());
    }
}