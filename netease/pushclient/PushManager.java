package com.netease.pushclient;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.alipay.sdk.m.x.d;
import com.netease.push.utils.ApplicationLifeListener;
import com.netease.push.utils.PushConstantsImpl;
import java.lang.reflect.InvocationTargetException;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public final class PushManager {
    public static final String NGPUSHLIB_VERSION = "3.0.3";
    private static final String TAG = "NGPush_PushManager";
    public static boolean forceShowMsgOnFront = false;
    private static ApplicationLifeListener listener = null;
    public static boolean onFront = true;
    private static PushManagerCallback s_callback;
    private static Class<?> s_clazzImpl;
    private static OnSubscriberListener subscriberListener;

    public interface PushManagerCallback {
        void onInitFailed(String str);

        void onInitSuccess();
    }

    public static void applicationLifeListen(Application application) {
        ApplicationLifeListener applicationLifeListener = new ApplicationLifeListener() { // from class: com.netease.pushclient.PushManager.1
            @Override // com.netease.push.utils.ApplicationLifeListener
            public void onEnterFront() {
                Log.i(PushManager.TAG, "onEnterFront");
                PushManager.onFront = true;
            }

            @Override // com.netease.push.utils.ApplicationLifeListener
            public void onEnterBackground() {
                Log.i(PushManager.TAG, "onEnterBackground");
                PushManager.onFront = false;
            }

            @Override // com.netease.push.utils.ApplicationLifeListener
            public void onExit() {
                Log.i(PushManager.TAG, d.r);
            }
        };
        listener = applicationLifeListener;
        applicationLifeListener.registerLifecycleCallback(application);
    }

    public static void init(Context context, PushManagerCallback pushManagerCallback) throws JSONException {
        String str = TAG;
        Log.i(str, "init, context:" + context);
        Log.i(str, "ngpush lib version:3.0.3");
        s_callback = pushManagerCallback;
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("methodId", "init");
            ReflectInterface.refectCall(context, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            onInitFailed(e.toString());
            Log.e(TAG, "init exception:" + e.getMessage());
        }
    }

    public static void onInitSuccess() {
        Log.i(TAG, PushU3d.CALLBACKTYPE_onInitSuccess);
        s_callback.onInitSuccess();
    }

    public static void onInitFailed(String str) {
        Log.i(TAG, "onInitFailed, reason:" + str);
        s_callback.onInitFailed(str);
    }

    public static Context getContext() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("methodId", "getContext");
            return (Context) ReflectInterface.refectCall(null, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "getContext exception:" + e.getMessage());
            return null;
        }
    }

    public static String getSdkVersion() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("methodId", "getSdkVersion");
            return (String) ReflectInterface.refectCall(null, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "getSdkVersion exception:" + e.getMessage());
            return "";
        }
    }

    public static void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) throws IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
        Log.i(TAG, "onRequestPermissionsResult");
        try {
            Class<?> cls = Class.forName("com.netease.pushclient.PushManagerImpl");
            s_clazzImpl = cls;
            cls.getMethod("onRequestPermissionsResult", Integer.TYPE, strArr.getClass(), iArr.getClass()).invoke(null, Integer.valueOf(i), strArr, iArr);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "onRequestPermissionsResult exception:" + e.getMessage());
        }
    }

    public static void startService() throws JSONException {
        Log.i(TAG, "startService");
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("methodId", "startService");
            ReflectInterface.refectCall(null, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "startService exception:" + e.getMessage());
        }
    }

    public static void stopService() throws JSONException {
        Log.i(TAG, "stopService");
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("methodId", "stopService");
            ReflectInterface.refectCall(null, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "stopService exception:" + e.getMessage());
        }
    }

    public static void terminatePush() throws JSONException {
        Log.i(TAG, "terminatePush");
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "terminatePush");
            ReflectInterface.refectCall(null, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "terminatePush exception:" + e.getMessage());
        }
    }

    public static String getDevId() throws JSONException {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "getDevId");
            return (String) ReflectInterface.refectCall(null, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "getDevId exception:" + e.getMessage());
            return "";
        }
    }

    public static String getToken() throws JSONException {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "getToken");
            return (String) ReflectInterface.refectCall(null, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "getDevId exception:" + e.getMessage());
            return "";
        }
    }

    public static void enableMultiPackSupport(boolean z) throws JSONException {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "enableMultiPackSupport");
            jSONObject.put("v", z);
            ReflectInterface.refectCall(null, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "enableMultiPackSupport exception:" + e.getMessage());
        }
    }

    public static void enableSound(boolean z) throws JSONException {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "enableSound");
            jSONObject.put(PushConstantsImpl.INTENT_FLAG_NAME, z);
            ReflectInterface.refectCall(null, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "enableSound exception:" + e.getMessage());
        }
    }

    public static void enableVibrate(boolean z) throws JSONException {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "enableVibrate");
            jSONObject.put(PushConstantsImpl.INTENT_FLAG_NAME, z);
            ReflectInterface.refectCall(null, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "enableVibrate exception:" + e.getMessage());
        }
    }

    public static void enableLight(boolean z) throws JSONException {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "enableLight");
            jSONObject.put(PushConstantsImpl.INTENT_FLAG_NAME, z);
            ReflectInterface.refectCall(null, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "enableLight exception:" + e.getMessage());
        }
    }

    public static void setEnableStartOtherService(boolean z) throws JSONException {
        Log.i(TAG, "setEnableStartOtherService");
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "setEnableStartOtherService");
            jSONObject.put("enableStartOtherService", z);
            ReflectInterface.refectCall(null, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "setEnableStartOtherService exception:" + e.getMessage());
        }
    }

    public static void enableRepeatProtect(boolean z) throws JSONException {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "enableRepeatProtect");
            jSONObject.put("enable", z);
            ReflectInterface.refectCall(null, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "enableRepeatProtect exception:" + e.getMessage());
        }
    }

    public static void setRepeatProtectInterval(int i) throws JSONException {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "setRepeatProtectInterval");
            jSONObject.put("interval", i);
            ReflectInterface.refectCall(null, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "setRepeatProtectInterval exception:" + e.getMessage());
        }
    }

    public static void setSenderID(String str, String str2) throws JSONException {
        String str3 = TAG;
        Log.i(str3, "setSenderID");
        Log.d(str3, "serviceType:" + str);
        Log.d(str3, "senderID:" + str2);
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "setSenderID");
            jSONObject.put("serviceType", str);
            jSONObject.put("senderID", str2);
            ReflectInterface.refectCall(null, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "setSenderID exception:" + e.getMessage());
        }
    }

    public static String getSenderID(String str) throws JSONException {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "getSenderID");
            jSONObject.put("serviceType", str);
            ReflectInterface.refectCall(null, jSONObject.toString());
            return "";
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "getSenderID exception:" + e.getMessage());
            return "";
        }
    }

    public static String getServiceType() throws JSONException {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "getServiceType");
            ReflectInterface.refectCall(null, jSONObject.toString());
            return "";
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "getSenderID exception:" + e.getMessage());
            return "";
        }
    }

    public static void setAppID(String str, String str2) throws JSONException {
        String str3 = TAG;
        Log.i(str3, "setAppID");
        Log.d(str3, "serviceType:" + str);
        Log.d(str3, "appID:" + str2);
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "setAppID");
            jSONObject.put("serviceType", str);
            jSONObject.put("appID", str2);
            ReflectInterface.refectCall(null, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "setAppID exception:" + e.getMessage());
        }
    }

    public static String getAppID(String str) throws JSONException {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "getAppID");
            jSONObject.put("serviceType", str);
            return (String) ReflectInterface.refectCall(null, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "getAppID exception:" + e.getMessage());
            return "";
        }
    }

    public static void setAppKey(String str, String str2) throws JSONException {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "setAppKey");
            jSONObject.put("serviceType", str);
            jSONObject.put("appKey", str2);
            ReflectInterface.refectCall(null, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "setAppKey exception:" + e.getMessage());
        }
    }

    public static String getAppKey(String str) throws JSONException {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "getAppKey");
            jSONObject.put("serviceType", str);
            return (String) ReflectInterface.refectCall(null, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "getAppKey exception:" + e.getMessage());
            return "";
        }
    }

    public static void setPushAddr(String str) throws JSONException {
        String str2 = TAG;
        Log.i(str2, "setPushAddr");
        Log.d(str2, "addr:" + str);
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "setPushAddr");
            jSONObject.put("addr", str);
            ReflectInterface.refectCall(null, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "setPushAddr exception:" + e.getMessage());
        }
    }

    public static void setNiePushAddr(String str) throws JSONException {
        String str2 = TAG;
        Log.i(str2, "setNiePushAddr");
        Log.d(str2, "addr:" + str);
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "setNiePushAddr");
            jSONObject.put("addr", str);
            ReflectInterface.refectCall(null, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "setNiePushAddr exception:" + e.getMessage());
        }
    }

    public static void setSdkgate(String str) throws JSONException {
        String str2 = TAG;
        Log.i(str2, "setSdkgate");
        Log.d(str2, "sdkgate:" + str);
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "setSdkgate");
            jSONObject.put("sdkgate", str);
            ReflectInterface.refectCall(null, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "setSdkgate exception:" + e.getMessage());
        }
    }

    public static void setNiepushMode(int i) throws JSONException {
        String str = TAG;
        Log.i(str, "setNiepushMode");
        Log.d(str, "mode:" + i);
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "setNiepushMode");
            jSONObject.put("mode", i);
            ReflectInterface.refectCall(null, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "setNiepushMode exception:" + e.getMessage());
        }
    }

    public static void clearContext() throws JSONException {
        Log.i(TAG, "clearContext");
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "clearContext");
            ReflectInterface.refectCall(null, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "clearContext exception:" + e.getMessage());
        }
    }

    public static void goToNotificationSetting() throws JSONException {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "goToNotificationSetting");
            ReflectInterface.refectCall(null, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "goToNotificationSetting exception:" + e.getMessage());
        }
    }

    public static boolean checkNotifySetting() throws JSONException {
        Log.i(TAG, "checkNotifySetting");
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "checkNotifySetting");
            return ((Boolean) ReflectInterface.refectCall(null, jSONObject.toString())).booleanValue();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "checkNotifySetting exception:" + e.getMessage());
            return false;
        }
    }

    public static void createPushChannel(String str, String str2, String str3, String str4, boolean z, boolean z2, boolean z3, String str5) throws JSONException {
        Log.i(TAG, "createPushChannel");
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "createPushChannel");
            jSONObject.put("groupId", str);
            jSONObject.put("groupName", str2);
            jSONObject.put("channelId", str3);
            jSONObject.put("channelName", str4);
            jSONObject.put("enableVibration", z);
            jSONObject.put("enableLights", z2);
            jSONObject.put("enableSound", z3);
            jSONObject.put("uriString", str5);
            ReflectInterface.refectCall(null, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "createPushChannel exception:" + e.getMessage());
        }
    }

    public static void subscribe(String str, String str2, String str3, String str4, String str5) throws IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
        Log.i(TAG, "subscribe");
        try {
            Class<?> cls = Class.forName("com.netease.pushclient.PushManagerImpl");
            s_clazzImpl = cls;
            cls.getMethod("subscribe", String.class, String.class, String.class, String.class, String.class).invoke(null, str, str2, str3, str4, str5);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "subscribe exception:" + e.getMessage());
        }
    }

    public static void bindAccount(String str, Boolean bool, Boolean bool2) throws JSONException {
        Log.i(TAG, "bindAccount");
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "bindAccount");
            jSONObject.put("account", str);
            jSONObject.put("unbind", bool);
            jSONObject.put("cover", bool2);
            ReflectInterface.refectCall(null, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "bindAccount exception:" + e.getMessage());
        }
    }

    public static void setSubscriberListener(OnSubscriberListener onSubscriberListener) {
        Log.i(TAG, "setSubscriberListener");
        subscriberListener = onSubscriberListener;
    }

    public static void subscribeFinish(String str) {
        Log.i(TAG, "SubscribeFinish");
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (subscriberListener != null) {
                if (jSONObject.optInt("code") == 0) {
                    subscriberListener.SubscriberDone(0, jSONObject.optString("err_msg"), jSONObject.optString("body"));
                } else {
                    subscriberListener.SubscriberDone(404, jSONObject.optString("err_msg"), jSONObject.optString("body"));
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "err=" + e);
            e.printStackTrace();
        }
    }

    public static void setNotUsePushPlatform(String str, boolean z) throws JSONException {
        Log.i(TAG, "setNotUsePushPlatform");
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "setNotUsePushPlatform");
            jSONObject.put("type", str);
            jSONObject.put("result", z);
            ReflectInterface.refectCall(null, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "setNotUsePushPlatform exception:" + e.getMessage());
        }
    }

    public static void reportClickNotification(Context context, String str, String str2, String str3) throws JSONException {
        Log.i(TAG, "reportClickNotification");
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "reportClickNotification");
            jSONObject.put("push_id", str);
            jSONObject.put("plan_id", str2);
            jSONObject.put("receive_channel", str3);
            ReflectInterface.refectCall(context, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "reportClickNotification exception:" + e.getMessage());
        }
    }

    public static void autoClickReport(Context context, Intent intent) throws IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
        Log.i(TAG, "autoClickReport");
        try {
            Class<?> cls = Class.forName("com.netease.pushclient.PushManagerImpl");
            s_clazzImpl = cls;
            cls.getMethod("autoClickReport", Context.class, Intent.class).invoke(null, context, intent);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "autoClickReport exception:" + e.getMessage());
        }
    }

    public static void reportNotificationOpened(Context context, String str) throws JSONException {
        Log.i(TAG, "reportNotificationOpened");
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "reportNotificationOpened");
            jSONObject.put("reqId", str);
            jSONObject.put("extendJson", "");
            ReflectInterface.refectCall(context, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "reportNotificationOpened exception:" + e.getMessage());
        }
    }

    public static void reportNotificationOpened(Context context, String str, String str2) throws JSONException {
        Log.i(TAG, "reportNotificationOpened");
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "reportNotificationOpened");
            jSONObject.put("reqId", str);
            jSONObject.put("extendJson", str2);
            ReflectInterface.refectCall(context, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "reportNotificationOpened exception:" + e.getMessage());
        }
    }

    public static void extendFunc(String str, String str2) throws JSONException {
        Log.i(TAG, "extendFunc");
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", str);
            jSONObject.put("extendJson", str2);
            ReflectInterface.refectCall(null, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "extendFunc exception:" + e.getMessage());
        }
    }

    public static boolean isOnFront() {
        Log.e(TAG, "get PushManager.onFront:" + onFront);
        return onFront;
    }

    public static void setOnFront(boolean z) {
        String str = TAG;
        Log.e(str, "setOnFront before PushManager.onFront:" + onFront);
        onFront = z;
        Log.e(str, "setOnFront after PushManager.onFront:" + onFront);
    }

    public static boolean isForceShowMsgOnFront() {
        Log.e(TAG, "isForceShowMsgOnFront:" + forceShowMsgOnFront);
        return forceShowMsgOnFront;
    }

    public static void setForceShowMsgOnFront(boolean z) {
        Log.e(TAG, "setForceShowMsgOnFront:" + z);
        forceShowMsgOnFront = z;
    }
}