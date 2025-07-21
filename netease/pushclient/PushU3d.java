package com.netease.pushclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.netease.pushclient.PushManager;
import java.lang.reflect.InvocationTargetException;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class PushU3d {
    public static final String CALLBACKTYPE_onInitFailed = "onInitFailed";
    public static final String CALLBACKTYPE_onInitSuccess = "onInitSuccess";
    private static final String TAG = "PushU3d";
    private static String U3DModuleName = "Main Camera";
    private static Class<?> UnityPlayerClass;

    private static Class<?> getUnityPlayerClass() throws ClassNotFoundException {
        if (UnityPlayerClass == null) {
            UnityPlayerClass = Class.forName("com.unity3d.player.UnityPlayer");
        }
        return UnityPlayerClass;
    }

    public static void init(Context context) throws JSONException {
        PushManager.applicationLifeListen(((Activity) context).getApplication());
        PushManager.init(context, new PushManager.PushManagerCallback() { // from class: com.netease.pushclient.PushU3d.1
            @Override // com.netease.pushclient.PushManager.PushManagerCallback
            public void onInitSuccess() throws JSONException {
                Log.i(PushU3d.TAG, PushU3d.CALLBACKTYPE_onInitSuccess);
                PushManager.setNiepushMode(1);
                PushManager.startService();
                PushManager.enableSound(true);
                PushManager.enableVibrate(false);
                PushU3d.callback(PushU3d.CALLBACKTYPE_onInitSuccess, 1, "");
            }

            @Override // com.netease.pushclient.PushManager.PushManagerCallback
            public void onInitFailed(String str) {
                Log.e(PushU3d.TAG, "onInitFailed:" + str);
                PushU3d.callback(PushU3d.CALLBACKTYPE_onInitFailed, 1, str);
            }
        });
    }

    public static void startService() throws JSONException {
        PushManager.startService();
    }

    public static void stopService() throws JSONException {
        PushManager.stopService();
    }

    public static String getToken() {
        return PushManager.getToken();
    }

    public static void setPushAddr(String str) throws JSONException {
        PushManager.setPushAddr(str);
    }

    public static void setNiePushAddr(String str) throws JSONException {
        PushManager.setNiePushAddr(str);
    }

    public static void setSdkgate(String str) throws JSONException {
        PushManager.setSdkgate("https://" + str);
    }

    public static void goToNotificationSetting() throws JSONException {
        PushManager.goToNotificationSetting();
    }

    public static boolean checkNotifySetting() {
        return PushManager.checkNotifySetting();
    }

    public static void createPushChannel(String str, String str2, String str3, String str4, boolean z, boolean z2, boolean z3, String str5) throws JSONException {
        PushManager.createPushChannel(str, str2, str3, str4, z, z2, z3, str5);
    }

    public static void bindAccount(String str, boolean z, boolean z2) throws JSONException {
        PushManager.bindAccount(str, Boolean.valueOf(z), Boolean.valueOf(z2));
    }

    public static void reportClickNotification(Context context, String str, String str2, String str3) throws JSONException {
        PushManager.reportClickNotification(context, str, str2, str3);
    }

    public static boolean isOnFront() {
        return PushManager.isOnFront();
    }

    public static void setOnFront(boolean z) {
        PushManager.setOnFront(z);
    }

    public static void setForceShowMsgOnFront(boolean z) {
        PushManager.setForceShowMsgOnFront(z);
    }

    public static void handleOnNewIntent(Context context, Intent intent) throws IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
        PushManager.autoClickReport(context, intent);
    }

    public static void unity3dSendMessage(String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            getUnityPlayerClass().getMethod("UnitySendMessage", String.class, String.class, String.class).invoke(getUnityPlayerClass(), U3DModuleName, "OnSdkMsgCallback", str);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
        } catch (NoSuchMethodException e4) {
            e4.printStackTrace();
        } catch (InvocationTargetException e5) {
            e5.printStackTrace();
        }
    }

    public static void callback(String str, int i, Object obj) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("callbackType", str);
            jSONObject.put("code", i);
            jSONObject.put("data", obj);
            unity3dSendMessage(jSONObject.toString());
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}