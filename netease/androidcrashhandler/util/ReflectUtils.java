package com.netease.androidcrashhandler.util;

import android.os.Build;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/* loaded from: classes4.dex */
public class ReflectUtils {
    private static final String TAG = "Trace_ReflectUtils";

    public static <T> T get(Class<?> cls, String str) throws Exception {
        return (T) new ReflectFiled(cls, str).get();
    }

    public static <T> T get(Class<?> cls, String str, Object obj) throws Exception {
        return (T) new ReflectFiled(cls, str).get(obj);
    }

    public static boolean set(Class<?> cls, String str, Object obj) throws Exception {
        return new ReflectFiled(cls, str).set(obj);
    }

    public static boolean set(Class<?> cls, String str, Object obj, Object obj2) throws Exception {
        return new ReflectFiled(cls, str).set(obj, obj2);
    }

    public static <T> T invoke(Class<?> cls, String str, Object obj, Object... objArr) throws Exception {
        return (T) new ReflectMethod(cls, str, new Class[0]).invoke(obj, objArr);
    }

    public static <T> T reflectObject(Object obj, String str, T t, boolean z) throws NoSuchFieldException {
        if (obj == null) {
            return t;
        }
        if (z) {
            try {
                Field field = (Field) Class.class.getDeclaredMethod("getDeclaredField", String.class).invoke(obj.getClass(), str);
                field.setAccessible(true);
                return (T) field.get(obj);
            } catch (Exception e) {
                LogUtils.e(TAG, e.toString() + "isHard=true" + e.getMessage());
            }
        } else {
            try {
                Field declaredField = obj.getClass().getDeclaredField(str);
                declaredField.setAccessible(true);
                return (T) declaredField.get(obj);
            } catch (Exception e2) {
                LogUtils.e(TAG, e2.toString() + "isHard=false" + e2.getMessage());
            }
        }
        return t;
    }

    public static <T> T reflectObject(Object obj, String str, T t) {
        return (T) reflectObject(obj, str, t, true);
    }

    public static Method reflectMethod(Object obj, boolean z, String str, Class<?>... clsArr) throws NoSuchMethodException, SecurityException {
        if (z) {
            try {
                Method method = (Method) Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class).invoke(obj.getClass(), str, clsArr);
                method.setAccessible(true);
                return method;
            } catch (Exception e) {
                LogUtils.e(TAG, e.toString() + "isHard=true" + e.getMessage());
                return null;
            }
        }
        try {
            Method declaredMethod = obj.getClass().getDeclaredMethod(str, clsArr);
            declaredMethod.setAccessible(true);
            return declaredMethod;
        } catch (Exception e2) {
            LogUtils.e(TAG, e2.toString() + "isHard=false" + e2.getMessage());
            return null;
        }
    }

    public static Method reflectMethod(Object obj, String str, Class<?>... clsArr) {
        return reflectMethod(obj, Build.VERSION.SDK_INT <= 29, str, clsArr);
    }
}