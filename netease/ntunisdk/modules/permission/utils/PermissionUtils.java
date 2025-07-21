package com.netease.ntunisdk.modules.permission.utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import com.netease.ntunisdk.modules.base.utils.LogModule;
import com.netease.ntunisdk.modules.permission.common.PermissionConstant;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class PermissionUtils {
    private static final String CLASS_NAME = "uikit_sdk_config";
    private static final String TAG = "PermissionUtils";
    private static SharedPreferences sp;

    public static boolean checkPermissionListValid(String str) {
        for (String str2 : str.split(",")) {
            if (!checkPermissionValid(str2)) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkPermissionValid(String str) {
        for (String str2 : PermissionConstant.PERMISSION_ARRAY) {
            if (TextUtils.equals(str2, str)) {
                return true;
            }
        }
        return false;
    }

    public static String checkParam(JSONObject jSONObject) throws JSONException {
        String strOptString = jSONObject.optString("permissionName");
        String strOptString2 = jSONObject.optString("firstText");
        try {
            if (!TextUtils.isEmpty(strOptString) && !TextUtils.isEmpty(strOptString2)) {
                if (!strOptString.startsWith("android.permission.") && !strOptString.startsWith("android.permission-group.")) {
                    LogModule.d(TAG, "requestPermission\u5f02\u5e38\uff0cpermissionName\u4e0d\u7b26\u5408\u683c\u5f0f: " + strOptString);
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put("status", "Parameter exception");
                    jSONObject.put("result", jSONObject2);
                    return jSONObject.toString();
                }
                if (checkPermissionListValid(strOptString)) {
                    return "";
                }
                LogModule.d(TAG, "requestPermission \u5f02\u5e38\uff0cpermissionName\u4e0d\u5408\u6cd5\uff0c\u8bf7\u68c0\u67e5: " + strOptString);
                JSONObject jSONObject3 = new JSONObject();
                jSONObject3.put("status", "Permission invalid exception");
                jSONObject.put("result", jSONObject3);
                return jSONObject.toString();
            }
            LogModule.d(TAG, "requestPermission\u5f02\u5e38\uff0cpermissionName,firstText\u5747\u4e0d\u80fd\u4e3a\u7a7a\uff0c\u8bf7\u68c0\u67e5");
            JSONObject jSONObject4 = new JSONObject();
            jSONObject4.put("status", "Parameter exception");
            jSONObject.put("result", jSONObject4);
            return jSONObject.toString();
        } catch (JSONException e) {
            LogModule.d(TAG, "formatCheck Json\u5b57\u6bb5\u4e0d\u7b26\u5408\u89c4\u8303 ,\u6743\u9650\u7533\u8bf7\u6d41\u7a0b\u4e2d\u65ad: throw: " + e.getMessage());
            return "";
        }
    }

    public static boolean verifyPermissions(int... iArr) {
        if (iArr.length == 0) {
            return false;
        }
        for (int i : iArr) {
            if (i != 0) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkPermissionExists(String str) {
        Integer num = PermissionConstant.MIN_SDK_PERMISSIONS.get(str);
        return num == null || Build.VERSION.SDK_INT >= num.intValue();
    }

    public static boolean hasAllPermissions(Context context, String... strArr) {
        for (String str : strArr) {
            if (checkPermissionExists(str) && !hasPermission(context, str)) {
                return false;
            }
        }
        return true;
    }

    public static int[] getGrantResults(Context context, String... strArr) {
        int[] iArr = new int[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            if (checkPermissionExists(strArr[i]) && hasPermission(context, strArr[i])) {
                iArr[i] = 0;
            } else {
                iArr[i] = -1;
            }
        }
        return iArr;
    }

    public static boolean hasAllPermissionUpdateAskAgain(Context context, String... strArr) {
        boolean z = true;
        for (String str : strArr) {
            if (checkPermissionExists(str) && !hasPermission(context, str)) {
                z = false;
            } else if (checkPermissionExists(str) && hasPermission(context, str)) {
                if (getBoolean(context, PermissionConstant.PERMISSION_NEVER_AGAIN + str, false)) {
                    putBoolean(context, PermissionConstant.PERMISSION_NEVER_AGAIN + str, false);
                }
            }
        }
        return z;
    }

    public static boolean hasPermission(Context context, String str) {
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                return ((Activity) context).checkSelfPermission(str) == 0;
            }
            return true;
        } catch (RuntimeException unused) {
            return false;
        }
    }

    public static boolean shouldShowRequestPermissionRationale(Activity activity, String... strArr) {
        for (String str : strArr) {
            if (Build.VERSION.SDK_INT < 23) {
                break;
            }
            if (activity.shouldShowRequestPermissionRationale(str)) {
                return true;
            }
        }
        return false;
    }

    public static void putAllAskAgain(Activity activity, int[] iArr, String... strArr) {
        for (int i = 0; i < strArr.length; i++) {
            if (Build.VERSION.SDK_INT < 23) {
                putBoolean(activity, PermissionConstant.PERMISSION_NEVER_AGAIN + strArr[i], false);
            } else if (!activity.shouldShowRequestPermissionRationale(strArr[i]) && iArr[i] == -1) {
                iArr[i] = -2;
                putBoolean(activity, PermissionConstant.PERMISSION_NEVER_AGAIN + strArr[i], true);
            }
        }
    }

    public static void goSetting(int i, String str, Activity activity, Fragment fragment) {
        try {
            Intent intent = new Intent();
            intent.putExtra(PermissionConstant.PERMISSION_KEY, str);
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
            fragment.startActivityForResult(intent, i);
        } catch (Exception e) {
            LogModule.d(TAG, "goSetting Exception: " + e);
        }
    }

    public static String arrayToString(String[] strArr) {
        StringBuilder sb = new StringBuilder();
        for (String str : strArr) {
            sb.append(str);
            sb.append(",");
        }
        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }

    public static void putBoolean(Context context, String str, boolean z) {
        if (sp == null) {
            sp = context.getSharedPreferences(CLASS_NAME, 0);
        }
        sp.edit().putBoolean(str, z).commit();
    }

    public static boolean getBoolean(Context context, String str, boolean z) {
        if (sp == null) {
            sp = context.getSharedPreferences(CLASS_NAME, 0);
        }
        return sp.getBoolean(str, z);
    }

    public static boolean getBooleanArray(Context context, String[] strArr, boolean z) {
        for (String str : strArr) {
            if (!getBoolean(context, PermissionConstant.PERMISSION_NEVER_AGAIN + str, z)) {
                return false;
            }
        }
        return true;
    }

    public static boolean grantOrNotAskAgain(Context context, String[] strArr) {
        int length = strArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                return true;
            }
            String str = strArr[i];
            boolean z = getBoolean(context, PermissionConstant.PERMISSION_NEVER_AGAIN + str, false);
            boolean z2 = checkPermissionExists(str) && hasPermission(context, str);
            if (!z && !z2) {
                return false;
            }
            i++;
        }
    }

    public static String joinString(int[] iArr) {
        StringBuilder sb = new StringBuilder();
        for (int i : iArr) {
            sb.append(i);
            sb.append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }

    public static void putBooleanArray(Context context, String[] strArr, boolean z) {
        for (String str : strArr) {
            putBoolean(context, PermissionConstant.PERMISSION_NEVER_AGAIN + str, z);
        }
    }
}