package com.netease.ntunisdk;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.HashSet;

/* loaded from: classes2.dex */
public class ApiRecorder {
    private static final String TAG = "ApiRecorder";
    private static WeakReference<SharedPreferences> spRef;
    static final HashSet<Type> apis = new HashSet<>();
    static final HashSet<Stage> stages = new HashSet<>();

    public enum Stage {
        enterGame,
        createRole,
        levelUp
    }

    public enum Type {
        init,
        login,
        inAppPurchase,
        manager,
        logout,
        uploadUserInfo,
        openExitView,
        exit,
        userProtocol,
        getAnnouncementInfo,
        onCreate,
        onPause,
        onResume,
        onStart,
        onRestart,
        onStop,
        onNewIntent,
        onActivityResult,
        onRequestPermissionsResult,
        onWindowFocusChanged,
        onSaveInstanceState
    }

    public static void init(SharedPreferences sharedPreferences) throws NumberFormatException {
        int i;
        int i2;
        spRef = new WeakReference<>(sharedPreferences);
        String string = sharedPreferences.getString("type", null);
        if (!TextUtils.isEmpty(string)) {
            Type[] typeArrValues = Type.values();
            for (String str : string.split(",")) {
                try {
                    i2 = Integer.parseInt(str);
                } catch (Exception unused) {
                    i2 = -1;
                }
                if (i2 >= 0) {
                    apis.add(typeArrValues[i2]);
                }
            }
        }
        String string2 = sharedPreferences.getString("stage", null);
        if (TextUtils.isEmpty(string2)) {
            return;
        }
        Stage[] stageArrValues = Stage.values();
        for (String str2 : string2.split(",")) {
            try {
                i = Integer.parseInt(str2);
            } catch (Exception unused2) {
                i = -1;
            }
            if (i >= 0) {
                stages.add(stageArrValues[i]);
            }
        }
    }

    public static void store(SharedPreferences sharedPreferences) {
        StringBuilder sb = new StringBuilder();
        for (Type type : Type.values()) {
            if (apis.contains(type)) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(type.ordinal());
            }
        }
        sharedPreferences.edit().putString("type", sb.toString()).commit();
        StringBuilder sb2 = new StringBuilder();
        for (Stage stage : Stage.values()) {
            if (stages.contains(stage)) {
                if (sb2.length() > 0) {
                    sb2.append(",");
                }
                sb2.append(stage.ordinal());
            }
        }
        sharedPreferences.edit().putString("stage", sb2.toString()).commit();
    }

    public static void append(Type type) {
        Log.i(TAG, "type: " + type);
        apis.add(type);
        WeakReference<SharedPreferences> weakReference = spRef;
        if (weakReference == null || weakReference.get() == null) {
            return;
        }
        store(spRef.get());
    }

    public static boolean has(Type type) {
        return apis.contains(type);
    }

    public static void append(Stage stage) {
        Log.i(TAG, "stage: " + stage);
        stages.add(stage);
    }

    public static boolean has(Stage stage) {
        return stages.contains(stage);
    }
}