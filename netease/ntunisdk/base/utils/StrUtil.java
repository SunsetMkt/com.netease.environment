package com.netease.ntunisdk.base.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Pair;
import android.widget.TextView;
import com.CCMsgSdk.WebSocketMessageCodeType;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class StrUtil {
    private static final int S_OFFSET = 76;
    private static final int S_SIZE = 62;
    private static String TAG = "SdkStrUtil";
    private static String s_key;
    private static Random s_rand = new Random(System.currentTimeMillis());

    public static Pair<Integer, String> parseIntFromStr(String str) {
        Integer numValueOf;
        Matcher matcher = Pattern.compile("\\d+").matcher(str);
        while (true) {
            if (!matcher.find()) {
                numValueOf = null;
                break;
            }
            try {
                numValueOf = Integer.valueOf(Integer.parseInt(matcher.group()));
                break;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        if (numValueOf != null) {
            String string = numValueOf.toString();
            if (str.indexOf(string) == 0) {
                return Pair.create(numValueOf, str.substring(string.length()));
            }
        }
        return Pair.create(null, null);
    }

    public static Map<String, String> jsonToStrMap(JSONObject jSONObject) throws JSONException {
        TreeMap treeMap = new TreeMap();
        Iterator<String> itKeys = jSONObject.keys();
        while (itKeys.hasNext()) {
            String next = itKeys.next();
            treeMap.put(next, jSONObject.optString(next));
        }
        return treeMap;
    }

    public static Map<String, String> jsonToStrMap(String str) throws JSONException {
        JSONObject jSONObject = new JSONObject(str);
        TreeMap treeMap = new TreeMap();
        Iterator<String> itKeys = jSONObject.keys();
        while (itKeys.hasNext()) {
            String next = itKeys.next();
            treeMap.put(next, jSONObject.optString(next));
        }
        return treeMap;
    }

    public static Map<String, Object> jsonToMapList(String str) throws JSONException {
        return toMap(new JSONObject(str), false);
    }

    public static Map<String, Object> jsonToMapSet(String str) throws JSONException {
        return toMap(new JSONObject(str), true);
    }

    public static Map<String, Object> toMap(JSONObject jSONObject, boolean z) throws JSONException {
        TreeMap treeMap = new TreeMap();
        Iterator<String> itKeys = jSONObject.keys();
        while (itKeys.hasNext()) {
            String next = itKeys.next();
            Object map = jSONObject.get(next);
            if (map instanceof JSONArray) {
                if (z) {
                    map = toSet((JSONArray) map);
                } else {
                    map = toList((JSONArray) map);
                }
            } else if (map instanceof JSONObject) {
                map = toMap((JSONObject) map, z);
            }
            treeMap.put(next, map);
        }
        return treeMap;
    }

    public static List<Object> toList(JSONArray jSONArray) throws JSONException {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < jSONArray.length(); i++) {
            Object map = jSONArray.get(i);
            if (map instanceof JSONArray) {
                map = toList((JSONArray) map);
            } else if (map instanceof JSONObject) {
                map = toMap((JSONObject) map, false);
            }
            arrayList.add(map);
        }
        return arrayList;
    }

    public static Set<Object> toSet(JSONArray jSONArray) throws JSONException {
        TreeSet treeSet = new TreeSet();
        for (int i = 0; i < jSONArray.length(); i++) {
            Object map = jSONArray.get(i);
            if (map instanceof JSONArray) {
                map = toSet((JSONArray) map);
            } else if (map instanceof JSONObject) {
                map = toMap((JSONObject) map, true);
            }
            treeSet.add(map);
        }
        return treeSet;
    }

    public static JSONObject mapToJson(Map<String, Object> map) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Map) {
                value = mapToJson((Map) value);
            } else if (value instanceof List) {
                value = listToJson((List) value);
            } else if (value instanceof Set) {
                value = setToJson((Set) value);
            }
            try {
                jSONObject.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jSONObject;
    }

    public static JSONObject mapStrToJson(Map<String, String> map) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            try {
                jSONObject.putOpt(entry.getKey(), entry.getValue());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jSONObject;
    }

    public static JSONArray listToJson(List<Object> list) throws JSONException {
        JSONArray jSONArray = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            Object toJson = list.get(i);
            if (toJson instanceof Map) {
                toJson = mapToJson((Map) toJson);
            } else if (toJson instanceof List) {
                toJson = listToJson((List) toJson);
            } else if (toJson instanceof Set) {
                toJson = setToJson((Set) toJson);
            }
            jSONArray.put(toJson);
        }
        return jSONArray;
    }

    public static JSONArray setToJson(Set<Object> set) throws JSONException {
        JSONArray jSONArray = new JSONArray();
        for (Object toJson : set) {
            if (toJson instanceof Map) {
                toJson = mapToJson((Map) toJson);
            } else if (toJson instanceof List) {
                toJson = listToJson((List) toJson);
            } else if (toJson instanceof Set) {
                toJson = setToJson((Set) toJson);
            }
            jSONArray.put(toJson);
        }
        return jSONArray;
    }

    public static String getAppendedJsonStr(String str, String str2, Object obj) throws JSONException {
        try {
            JSONObject jSONObject = new JSONObject(str);
            jSONObject.put(str2, obj);
            return jSONObject.toString();
        } catch (Exception unused) {
            return str;
        }
    }

    public static String getAppendedJsonStr(String str, String[] strArr, String[] strArr2) throws JSONException {
        try {
            JSONObject jSONObject = new JSONObject(str);
            for (int i = 0; i < strArr2.length; i++) {
                if (!TextUtils.isEmpty(strArr[i]) && !TextUtils.isEmpty(strArr2[i])) {
                    jSONObject.put(strArr[i], strArr2[i]);
                }
            }
            return jSONObject.toString();
        } catch (Exception unused) {
            return str;
        }
    }

    public static String createLinkString(Map<String, String> map, boolean z, boolean z2) throws UnsupportedEncodingException {
        ArrayList arrayList = new ArrayList(map.keySet());
        if (z) {
            Collections.sort(arrayList);
        }
        String str = "";
        for (int i = 0; i < arrayList.size(); i++) {
            String str2 = (String) arrayList.get(i);
            String strEncode = map.get(str2);
            if (z2) {
                try {
                    strEncode = URLEncoder.encode(strEncode, "UTF-8");
                } catch (UnsupportedEncodingException unused) {
                }
            }
            if (!TextUtils.isEmpty(str)) {
                str = str + com.alipay.sdk.m.s.a.l;
            }
            str = str + str2 + "=" + strEncode;
        }
        return str;
    }

    public static void showAlertDialog(Activity activity, String str, String str2) {
        showAlertDialog(activity, str, str2, null);
    }

    public static void showAlertDialog(final Activity activity, final String str, final String str2, final DialogInterface.OnClickListener onClickListener) {
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.utils.StrUtil.1
            @Override // java.lang.Runnable
            public final void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                if (!TextUtils.isEmpty(str)) {
                    builder.setTitle(str);
                }
                builder.setMessage(str2);
                builder.setCancelable(false);
                builder.setPositiveButton(WebSocketMessageCodeType.SUC, onClickListener);
                AlertDialog alertDialogCreate = builder.create();
                alertDialogCreate.show();
                TextView textView = (TextView) alertDialogCreate.findViewById(activity.getResources().getIdentifier("alertTitle", ResIdReader.RES_TYPE_ID, "android"));
                if (textView != null) {
                    textView.setGravity(17);
                }
                TextView textView2 = (TextView) alertDialogCreate.findViewById(activity.getResources().getIdentifier("message", ResIdReader.RES_TYPE_ID, "android"));
                if (textView2 != null) {
                    textView2.setGravity(17);
                }
            }
        });
    }

    public static byte[] append(byte[] bArr, byte[] bArr2) {
        byte[] bArr3 = new byte[bArr.length + bArr2.length];
        for (int i = 0; i < bArr.length; i++) {
            bArr3[i] = bArr[i];
        }
        for (int i2 = 0; i2 < bArr2.length; i2++) {
            bArr3[bArr.length + i2] = bArr2[i2];
        }
        return bArr3;
    }

    private static byte[] gs() {
        int i;
        int i2;
        int i3;
        new Random(System.currentTimeMillis());
        byte[] bArr = new byte[62];
        for (int i4 = 0; i4 < 10; i4++) {
            bArr[i4] = (byte) (i4 + 48);
        }
        for (int i5 = 0; i5 < 26; i5++) {
            bArr[i5 + 10] = (byte) (i5 + 97);
        }
        for (int i6 = 0; i6 < 26; i6++) {
            bArr[i6 + 36] = (byte) (i6 + 65);
        }
        int[] iArr = {0, 10, 16, 36, 42};
        int[] iArr2 = {9, 15, 35, 41, 61};
        for (int i7 = 0; i7 < 62; i7++) {
            while (true) {
                if (i >= 5) {
                    i2 = 62;
                    i3 = 0;
                    break;
                }
                i3 = iArr[i];
                i = (i7 < i3 || i7 > (i2 = iArr2[i])) ? i + 1 : 0;
            }
            int iRandInt = randInt(i3, i2);
            byte b = bArr[i7];
            bArr[i7] = bArr[iRandInt];
            bArr[iRandInt] = b;
        }
        return bArr;
    }

    public static String gt() {
        byte[] bArrGs = gs();
        byte[] bArrGs2 = gs();
        HashMap map = new HashMap();
        map.put((byte) 73, 1);
        map.put((byte) 77, 1);
        map.put((byte) 49, 1);
        map.put((byte) 48, 1);
        for (int i = 0; i < bArrGs.length; i++) {
            if (map.containsKey(Byte.valueOf(bArrGs[i]))) {
                int i2 = 0;
                while (true) {
                    if (i2 < bArrGs2.length) {
                        byte b = bArrGs[i];
                        byte b2 = bArrGs2[i2];
                        if (b == b2) {
                            byte b3 = bArrGs2[i];
                            bArrGs2[i] = b2;
                            bArrGs2[i2] = b3;
                            break;
                        }
                        i2++;
                    }
                }
            }
        }
        int length = bArrGs.length;
        byte[] bArr = new byte[length];
        for (int i3 = 0; i3 < length; i3++) {
            bArr[i3] = (byte) ((bArrGs2[i3] - bArrGs[i3]) + 76);
        }
        return Base64.encodeToString(append(bArr, bArrGs), 0);
    }

    public static void setKey(String str) {
        s_key = str;
    }

    public static String t(String str, String str2) {
        try {
            byte[] bArrDecode = Base64.decode(str, 0);
            if (bArrDecode.length != 124) {
                UniSdkUtils.e(TAG, String.format(Locale.US, "t size error: %d<>%d", Integer.valueOf(bArrDecode.length), 124));
                return str2;
            }
            byte[] bArrCopyOfRange = copyOfRange(bArrDecode, 0, 62);
            byte[] bArrCopyOfRange2 = copyOfRange(bArrDecode, 62, 124);
            HashMap map = new HashMap();
            for (int i = 0; i < 62; i++) {
                map.put(Byte.valueOf(bArrCopyOfRange2[i]), Byte.valueOf((byte) ((bArrCopyOfRange[i] - 76) + bArrCopyOfRange2[i])));
            }
            byte[] bytes = str2.getBytes();
            for (int i2 = 0; i2 < bytes.length; i2++) {
                byte b = bytes[i2];
                if (map.containsKey(Byte.valueOf(b))) {
                    bytes[i2] = ((Byte) map.get(Byte.valueOf(b))).byteValue();
                }
            }
            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return str2;
        }
    }

    public static String validate(String str) {
        if (TextUtils.isEmpty(s_key)) {
            return str;
        }
        try {
            byte[] bArrDecode = Base64.decode(s_key, 0);
            if (bArrDecode.length != 124) {
                UniSdkUtils.e(TAG, String.format(Locale.US, "f size error: %d<>%d", Integer.valueOf(bArrDecode.length), 124));
                return str;
            }
            byte[] bArrCopyOfRange = copyOfRange(bArrDecode, 0, 62);
            byte[] bArrCopyOfRange2 = copyOfRange(bArrDecode, 62, 124);
            HashMap map = new HashMap();
            for (int i = 0; i < 62; i++) {
                map.put(Byte.valueOf((byte) ((bArrCopyOfRange[i] - 76) + bArrCopyOfRange2[i])), Byte.valueOf(bArrCopyOfRange2[i]));
            }
            byte[] bytes = str.getBytes();
            for (int i2 = 0; i2 < bytes.length; i2++) {
                byte b = bytes[i2];
                if (map.containsKey(Byte.valueOf(b))) {
                    bytes[i2] = ((Byte) map.get(Byte.valueOf(b))).byteValue();
                }
            }
            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }

    public static int randInt(int i, int i2) {
        return s_rand.nextInt((i2 - i) + 1) + i;
    }

    public static boolean isBase64_(String str) {
        return Pattern.matches("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$", str);
    }

    public static boolean isBase64(String str) {
        try {
            return str.replaceAll("[\n]", "").equals(Base64.encodeToString(Base64.decode(str, 0), 0).replaceAll("[\n]", ""));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static byte[] copyOfRange(byte[] bArr, int i, int i2) {
        int i3 = i2 - i;
        if (i3 < 0) {
            return null;
        }
        byte[] bArr2 = new byte[i3];
        for (int i4 = 0; i4 < i3; i4++) {
            bArr2[i4] = bArr[i4 + i];
        }
        return bArr2;
    }
}