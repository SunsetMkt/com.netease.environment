package com.netease.push.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.netease.ntunisdk.base.PatchPlaceholder;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

/* loaded from: classes3.dex */
public class MySharedPreferences implements SharedPreferences, SharedPreferences.Editor {
    public static final String TAG = "NGPush_" + MySharedPreferences.class.getSimpleName();
    private Context context;
    private String m_packagename;

    @Override // android.content.SharedPreferences.Editor
    public void apply() {
    }

    @Override // android.content.SharedPreferences.Editor
    public SharedPreferences.Editor clear() {
        return this;
    }

    @Override // android.content.SharedPreferences.Editor
    public boolean commit() {
        return false;
    }

    @Override // android.content.SharedPreferences
    public SharedPreferences.Editor edit() {
        return this;
    }

    @Override // android.content.SharedPreferences
    public Map<String, ?> getAll() {
        return null;
    }

    @Override // android.content.SharedPreferences
    public Set<String> getStringSet(String str, Set<String> set) {
        return null;
    }

    @Override // android.content.SharedPreferences.Editor
    public SharedPreferences.Editor putStringSet(String str, Set<String> set) {
        return this;
    }

    @Override // android.content.SharedPreferences
    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
    }

    @Override // android.content.SharedPreferences
    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
    }

    private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, PatchPlaceholder.class.getSimpleName());
    }

    public MySharedPreferences(Context context, String str) {
        this.context = context;
        this.m_packagename = str;
    }

    @Override // android.content.SharedPreferences
    public boolean contains(String str) {
        return FileUtils.exists(this.context, this.m_packagename + "." + str);
    }

    @Override // android.content.SharedPreferences
    public boolean getBoolean(String str, boolean z) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            z = Boolean.parseBoolean(FileUtils.read(this.context, this.m_packagename + "." + str, z + ""));
        } catch (Exception e) {
            PushLog.e(TAG, e.toString());
        }
        PushLog.d(TAG, "getBoolean, key:" + str + ", value:" + z);
        return z;
    }

    @Override // android.content.SharedPreferences
    public float getFloat(String str, float f) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            f = Float.parseFloat(FileUtils.read(this.context, this.m_packagename + "." + str, f + ""));
        } catch (Exception e) {
            PushLog.e(TAG, e.toString());
        }
        PushLog.d(TAG, "getFloat, key:" + str + ", value:" + f);
        return f;
    }

    @Override // android.content.SharedPreferences
    public int getInt(String str, int i) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            i = Integer.parseInt(FileUtils.read(this.context, this.m_packagename + "." + str, i + ""));
        } catch (Exception e) {
            PushLog.e(TAG, e.toString());
        }
        PushLog.d(TAG, "getInt, key:" + str + ", value:" + i);
        return i;
    }

    @Override // android.content.SharedPreferences
    public long getLong(String str, long j) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            j = Long.parseLong(FileUtils.read(this.context, this.m_packagename + "." + str, j + ""));
        } catch (Exception e) {
            PushLog.e(TAG, e.toString());
        }
        PushLog.d(TAG, "getLong, key:" + str + ", value:" + j);
        return j;
    }

    @Override // android.content.SharedPreferences
    public String getString(String str, String str2) throws IllegalAccessException, IOException, IllegalArgumentException, InvocationTargetException {
        String str3 = FileUtils.read(this.context, this.m_packagename + "." + str, str2 + "");
        if (str3 != null) {
            str2 = str3;
        }
        PushLog.d(TAG, "getString, key:" + str + ", value:" + str2);
        return str2;
    }

    @Override // android.content.SharedPreferences.Editor
    public SharedPreferences.Editor putBoolean(String str, boolean z) throws IllegalAccessException, IOException, IllegalArgumentException, InvocationTargetException {
        FileUtils.write(this.context, this.m_packagename + "." + str, z + "");
        PushLog.d(TAG, "putBoolean, key:" + str + ", value:" + z);
        return this;
    }

    @Override // android.content.SharedPreferences.Editor
    public SharedPreferences.Editor putFloat(String str, float f) throws IllegalAccessException, IOException, IllegalArgumentException, InvocationTargetException {
        FileUtils.write(this.context, this.m_packagename + "." + str, f + "");
        PushLog.d(TAG, "putFloat, key:" + str + ", value:" + f);
        return this;
    }

    @Override // android.content.SharedPreferences.Editor
    public SharedPreferences.Editor putInt(String str, int i) throws IllegalAccessException, IOException, IllegalArgumentException, InvocationTargetException {
        FileUtils.write(this.context, this.m_packagename + "." + str, i + "");
        PushLog.d(TAG, "putInt, key:" + str + ", value:" + i);
        return this;
    }

    @Override // android.content.SharedPreferences.Editor
    public SharedPreferences.Editor putLong(String str, long j) throws IllegalAccessException, IOException, IllegalArgumentException, InvocationTargetException {
        FileUtils.write(this.context, this.m_packagename + "." + str, j + "");
        PushLog.d(TAG, "putLong, key:" + str + ", value:" + j);
        return this;
    }

    @Override // android.content.SharedPreferences.Editor
    public SharedPreferences.Editor putString(String str, String str2) throws IllegalAccessException, IOException, IllegalArgumentException, InvocationTargetException {
        FileUtils.write(this.context, this.m_packagename + "." + str, str2 + "");
        PushLog.d(TAG, "putString, key:" + str + ", value:" + str2);
        return this;
    }

    @Override // android.content.SharedPreferences.Editor
    public SharedPreferences.Editor remove(String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        FileUtils.delete(this.context, this.m_packagename + "." + str);
        PushLog.d(TAG, "remove, key:" + str);
        return this;
    }
}