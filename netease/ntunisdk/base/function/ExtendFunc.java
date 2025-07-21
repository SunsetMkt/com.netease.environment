package com.netease.ntunisdk.base.function;

import android.content.Context;
import android.text.TextUtils;
import com.netease.ntunisdk.base.OrderInfo;
import com.netease.ntunisdk.base.SdkBase;
import java.util.HashMap;
import java.util.HashSet;
import org.json.JSONObject;

/* loaded from: classes4.dex */
public class ExtendFunc {
    private static final String TAG = "ExtendFunc";
    private static final HashMap<String, HashSet<SdkBase>> methodIdTable = new HashMap<>();

    public static void register(String str, SdkBase sdkBase) {
        if (TextUtils.isEmpty(str) || sdkBase == null) {
            return;
        }
        HashMap<String, HashSet<SdkBase>> map = methodIdTable;
        HashSet<SdkBase> hashSet = map.get(str);
        if (hashSet == null) {
            hashSet = new HashSet<>();
            map.put(str, hashSet);
        }
        hashSet.add(sdkBase);
    }

    public static HashSet<SdkBase> getInst(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return methodIdTable.get(str);
    }

    /* JADX WARN: Removed duplicated region for block: B:73:0x0210 A[PHI: r2
  0x0210: PHI (r2v17 java.lang.String) = (r2v16 java.lang.String), (r2v20 java.lang.String) binds: [B:54:0x01d0, B:71:0x020c] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0213 A[Catch: Exception -> 0x0229, TryCatch #0 {Exception -> 0x0229, blocks: (B:53:0x01ca, B:55:0x01d2, B:75:0x0213, B:76:0x0218, B:63:0x01e3, B:65:0x01eb, B:66:0x01f5, B:68:0x01fd), top: B:165:0x01ca }] */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0218 A[Catch: Exception -> 0x0229, TRY_LEAVE, TryCatch #0 {Exception -> 0x0229, blocks: (B:53:0x01ca, B:55:0x01d2, B:75:0x0213, B:76:0x0218, B:63:0x01e3, B:65:0x01eb, B:66:0x01f5, B:68:0x01fd), top: B:165:0x01ca }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean extendFuncForInner(com.netease.ntunisdk.base.SdkBase r17, android.content.Context r18, java.lang.String r19, org.json.JSONObject r20) {
        /*
            Method dump skipped, instructions count: 1244
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.function.ExtendFunc.extendFuncForInner(com.netease.ntunisdk.base.SdkBase, android.content.Context, java.lang.String, org.json.JSONObject):boolean");
    }

    public static boolean extendFuncForInner(SdkBase sdkBase, Context context, String str, JSONObject jSONObject, Object... objArr) throws Throwable {
        if (!"fromAiDetect".equalsIgnoreCase(str)) {
            return false;
        }
        if (objArr != null && 1 <= objArr.length) {
            Object obj = objArr[0];
            if (obj instanceof OrderInfo) {
                sdkBase.continueOrderSetting((OrderInfo) obj, jSONObject.optBoolean("autoRegProduct"));
            }
        }
        return true;
    }
}