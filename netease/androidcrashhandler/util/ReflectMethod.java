package com.netease.androidcrashhandler.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: classes4.dex */
public class ReflectMethod {
    private static final String TAG = "Trace_ReflectFiled";
    private Class<?> mClazz;
    private boolean mInit;
    private Method mMethod;
    private String mMethodName;
    private Class[] mParameterTypes;

    public ReflectMethod(Class<?> cls, String str, Class<?>... clsArr) {
        if (cls == null || str == null || str.length() == 0) {
            throw new IllegalArgumentException("Both of invoker and fieldName can not be null or nil.");
        }
        this.mClazz = cls;
        this.mMethodName = str;
        this.mParameterTypes = clsArr;
    }

    private synchronized void prepare() {
        if (this.mInit) {
            return;
        }
        for (Class<?> superclass = this.mClazz; superclass != null; superclass = superclass.getSuperclass()) {
            try {
                Method declaredMethod = superclass.getDeclaredMethod(this.mMethodName, this.mParameterTypes);
                declaredMethod.setAccessible(true);
                this.mMethod = declaredMethod;
                break;
            } catch (Exception unused) {
            }
        }
        this.mInit = true;
    }

    public synchronized <T> T invoke(Object obj, Object... objArr) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException, InvocationTargetException {
        return (T) invoke(obj, false, objArr);
    }

    public synchronized <T> T invoke(Object obj, boolean z, Object... objArr) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException, InvocationTargetException {
        prepare();
        Method method = this.mMethod;
        if (method != null) {
            return (T) method.invoke(obj, objArr);
        }
        if (!z) {
            throw new NoSuchFieldException("Method " + this.mMethodName + " is not exists.");
        }
        LogUtils.w(TAG, "Field %s is no exists" + this.mMethodName);
        return null;
    }

    public synchronized <T> T invokeWithoutThrow(Object obj, Object... objArr) {
        try {
            try {
                try {
                    try {
                        return (T) invoke(obj, true, objArr);
                    } catch (IllegalAccessException e) {
                        LogUtils.e(TAG, "invokeWithoutThrow, exception occur :" + e);
                        return null;
                    }
                } catch (InvocationTargetException e2) {
                    LogUtils.e(TAG, "invokeWithoutThrow, exception occur :" + e2);
                    return null;
                }
            } catch (NoSuchFieldException e3) {
                LogUtils.e(TAG, "invokeWithoutThrow, exception occur :" + e3);
                return null;
            }
        } catch (IllegalArgumentException e4) {
            LogUtils.e(TAG, "invokeWithoutThrow, exception occur :" + e4);
            return null;
        }
    }
}