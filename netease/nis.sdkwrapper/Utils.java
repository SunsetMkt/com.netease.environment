package com.netease.nis.sdkwrapper;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import com.netease.ntunisdk.unilogger.global.Const;
import com.xiaomi.gamecenter.sdk.robust.Constants;
import com.xiaomi.onetrack.api.c;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/* loaded from: classes6.dex */
public class Utils {
    public static native Object rL(Object[] objArr);

    static {
        System.loadLibrary("secsdk");
    }

    private static String doTypeShort(String str) {
        if (str.startsWith(Constants.C)) {
            return str.replace(".", "/");
        }
        if (str.equals(Constants.Q)) {
            return Const.LEVEL.INFO;
        }
        if (str.equals(Constants.W)) {
            return "F";
        }
        if (str.equals(Constants.S)) {
            return "J";
        }
        if (str.equals(Constants.U)) {
            return Const.LEVEL.DEBUG;
        }
        if (str.equals(Constants.Y)) {
            return "S";
        }
        if (str.equals(Constants.ac)) {
            return "C";
        }
        if (str.equals(Constants.O)) {
            return "Z";
        }
        if (str.equals(Constants.aa)) {
            return c.f2654a;
        }
        return ("L" + str + ";").replace(".", "/");
    }

    public static String getFieldSCDesc(Class cls, String str, String str2) {
        while (cls != null) {
            String strVGetFieldSCDesc = vGetFieldSCDesc(cls, str, str2);
            if (strVGetFieldSCDesc != "") {
                return strVGetFieldSCDesc;
            }
            cls = cls.getSuperclass();
        }
        return "";
    }

    private static String vGetFieldSCDesc(Class cls, String str, String str2) {
        try {
            Field[] declaredFields = cls.getDeclaredFields();
            Field.setAccessible(declaredFields, true);
            for (Field field : declaredFields) {
                String strReplace = field.getType().toString().replace("class ", "").replace("interface ", "");
                if (Modifier.isStatic(field.getModifiers()) && field.getName().equals(str) && str2.equals(doTypeShort(strReplace))) {
                    return field.getDeclaringClass().getName().replace(".", "/");
                }
            }
            return "";
        } catch (NoClassDefFoundError unused) {
            return "NoClassDefFoundError";
        }
    }

    public static void showRiskMessage(Context context, String str) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Toast.makeText(context, str, 0).show();
            return;
        }
        Looper.prepare();
        Toast.makeText(context, str, 0).show();
        Looper.loop();
    }

    public static Object getStaticFO(String str, String str2) throws NoSuchFieldException {
        try {
            Field field = Class.forName(str.replace('/', '.')).getField(str2);
            Class<?> type = field.getType();
            if (type == Integer.TYPE) {
                return Integer.valueOf(field.getInt(null));
            }
            if (type == Float.TYPE) {
                return Float.valueOf(field.getFloat(null));
            }
            if (type == Long.TYPE) {
                return Long.valueOf(field.getLong(null));
            }
            if (type == Double.TYPE) {
                return Double.valueOf(field.getDouble(null));
            }
            if (type == Short.TYPE) {
                return Short.valueOf(field.getShort(null));
            }
            if (type == Character.TYPE) {
                return Character.valueOf(field.getChar(null));
            }
            if (type == Boolean.TYPE) {
                return Boolean.valueOf(field.getBoolean(null));
            }
            if (type == Byte.TYPE) {
                return Byte.valueOf(field.getByte(null));
            }
            return field.get(null);
        } catch (Exception e) {
            Log.e("Utils", e.toString());
            return null;
        }
    }
}