package com.netease.pushclient;

import android.content.Context;
import android.util.Log;
import com.netease.push.utils.PushConstantsImpl;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public final class Reflect {
    private static final String TAG = "NGPush_Reflect";

    public static Object refectCall(Context context, String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            String strOptString = jSONObject.optString("methodId");
            Log.d(TAG, "refectCall json:" + str);
            if ("reportNotificationOpened".equalsIgnoreCase(strOptString)) {
                PushManagerImpl.reportNotificationOpened(context, jSONObject.optString("reqId"), jSONObject.optString("extendJson"));
                return null;
            }
            if ("reportClickNotification".equalsIgnoreCase(strOptString)) {
                PushManagerImpl.reportClickNotification(context, jSONObject.optString("push_id"), jSONObject.optString("plan_id"), jSONObject.optString("receive_channel"));
                return null;
            }
            if ("setNotUsePushPlatform".equalsIgnoreCase(strOptString)) {
                PushManagerImpl.setNotUsePushPlatform(jSONObject.optString("type"), jSONObject.optBoolean("result"));
                return null;
            }
            if ("init".equalsIgnoreCase(strOptString)) {
                PushManagerImpl.init(context);
                return null;
            }
            if ("initWithProductId".equalsIgnoreCase(strOptString)) {
                Log.d(TAG, "initWithProductId json:" + str);
                PushManagerImpl.initWithProductId(context, jSONObject.optString("productid"), jSONObject.optString("clientkey"));
                return null;
            }
            if ("getContext".equalsIgnoreCase(strOptString)) {
                return PushManagerImpl.getContext();
            }
            if ("getSdkVersion".equalsIgnoreCase(strOptString)) {
                return PushManagerImpl.getSdkVersion();
            }
            if ("onRequestPermissionsResult".equalsIgnoreCase(strOptString)) {
                return null;
            }
            if ("startService".equalsIgnoreCase(strOptString)) {
                PushManagerImpl.startService();
                return null;
            }
            if ("stopService".equalsIgnoreCase(strOptString)) {
                PushManagerImpl.stopService();
                return null;
            }
            if ("terminatePush".equalsIgnoreCase(strOptString)) {
                PushManagerImpl.terminatePush();
                return null;
            }
            if ("getDevId".equalsIgnoreCase(strOptString)) {
                return PushManagerImpl.getDevId();
            }
            if ("enableMultiPackSupport".equalsIgnoreCase(strOptString)) {
                PushManagerImpl.enableMultiPackSupport(jSONObject.optBoolean("v"));
                return null;
            }
            if ("enableSound".equalsIgnoreCase(strOptString)) {
                PushManagerImpl.enableSound(jSONObject.optBoolean(PushConstantsImpl.INTENT_FLAG_NAME));
                return null;
            }
            if ("enableVibrate".equalsIgnoreCase(strOptString)) {
                PushManagerImpl.enableVibrate(jSONObject.optBoolean(PushConstantsImpl.INTENT_FLAG_NAME));
                return null;
            }
            if ("enableLight".equalsIgnoreCase(strOptString)) {
                PushManagerImpl.enableLight(jSONObject.optBoolean(PushConstantsImpl.INTENT_FLAG_NAME));
                return null;
            }
            if ("setEnableStartOtherService".equalsIgnoreCase(strOptString)) {
                PushManagerImpl.setEnableStartOtherService(jSONObject.optBoolean("enableStartOtherService"));
                return null;
            }
            if ("enableRepeatProtect".equalsIgnoreCase(strOptString)) {
                PushManagerImpl.enableRepeatProtect(jSONObject.optBoolean("enable"));
                return null;
            }
            if ("setRepeatProtectInterval".equalsIgnoreCase(strOptString)) {
                PushManagerImpl.setRepeatProtectInterval(jSONObject.optInt("interval"));
                return null;
            }
            if ("setSenderID".equalsIgnoreCase(strOptString)) {
                PushManagerImpl.setSenderID(jSONObject.optString("serviceType"), jSONObject.optString("senderID"));
                return null;
            }
            if ("getSenderID".equalsIgnoreCase(strOptString)) {
                return PushManagerImpl.getSenderID(jSONObject.optString("serviceType"));
            }
            if ("setAppID".equalsIgnoreCase(strOptString)) {
                PushManagerImpl.setAppID(jSONObject.optString("serviceType"), jSONObject.optString("appID"));
                return null;
            }
            if ("getAppID".equalsIgnoreCase(strOptString)) {
                return PushManagerImpl.getAppID(jSONObject.optString("serviceType"));
            }
            if ("setAppKey".equalsIgnoreCase(strOptString)) {
                PushManagerImpl.setAppKey(jSONObject.optString("serviceType"), jSONObject.optString("appKey"));
                return null;
            }
            if ("getAppKey".equalsIgnoreCase(strOptString)) {
                return PushManagerImpl.getAppKey(jSONObject.optString("serviceType"));
            }
            if ("setPushAddr".equalsIgnoreCase(strOptString)) {
                PushManagerImpl.setPushAddr(jSONObject.optString("addr"));
                return null;
            }
            if ("setNiepushMode".equalsIgnoreCase(strOptString)) {
                PushManagerImpl.setNiepushMode(jSONObject.optInt("mode"));
                return null;
            }
            if ("clearContext".equalsIgnoreCase(strOptString)) {
                PushManagerImpl.clearContext();
                return null;
            }
            if ("goToNotificationSetting".equalsIgnoreCase(strOptString)) {
                PushManagerImpl.goToNotificationSetting();
                return null;
            }
            if ("checkNotifySetting".equalsIgnoreCase(strOptString)) {
                return Boolean.valueOf(PushManagerImpl.checkNotifySetting());
            }
            if ("createPushChannel".equalsIgnoreCase(strOptString)) {
                PushManagerImpl.createPushChannel(jSONObject.optString("groupId"), jSONObject.optString("groupName"), jSONObject.optString("channelId"), jSONObject.optString("channelName"), jSONObject.optBoolean("enableVibration"), jSONObject.optBoolean("enableLights"), jSONObject.optBoolean("enableSound"), jSONObject.optString("uriString"));
                return null;
            }
            if ("subscribe".equalsIgnoreCase(strOptString)) {
                PushManagerImpl.subscribe(jSONObject.optString("services"), jSONObject.optString("aids"), jSONObject.optString("sdkuids"), jSONObject.optString("roleids"), jSONObject.optString("subscribers"));
                return null;
            }
            if ("bindAccount".equalsIgnoreCase(strOptString)) {
                PushManagerImpl.bindAccount(jSONObject.optString("account"), Boolean.valueOf(jSONObject.optBoolean("unbind")), Boolean.valueOf(jSONObject.optBoolean("cover")));
                return null;
            }
            if ("getToken".equalsIgnoreCase(strOptString)) {
                return PushManagerImpl.getToken();
            }
            if ("setNiePushAddr".equalsIgnoreCase(strOptString)) {
                PushManagerImpl.setNiePushAddr(jSONObject.optString("addr"));
                return null;
            }
            if ("setSdkgate".equalsIgnoreCase(strOptString)) {
                PushManagerImpl.setSdkgate(jSONObject.optString("sdkgate"));
                return null;
            }
            if ("getServiceType".equalsIgnoreCase(strOptString)) {
                return PushManagerImpl.getServiceType(context);
            }
            if ("find".equalsIgnoreCase(strOptString)) {
                return PushManagerImpl.find();
            }
            if ("setFeature".equalsIgnoreCase(strOptString)) {
                PushManagerImpl.setFeature(jSONObject.optBoolean("cover"), jSONObject.optBoolean("unset"), jSONObject.optString("features"));
                return null;
            }
            if ("setRefuseTime".equalsIgnoreCase(strOptString)) {
                PushManagerImpl.setRefuseTime(jSONObject.optString("timeZone"), jSONObject.optString("timeJson"));
                return null;
            }
            if ("setPermissionTips".equalsIgnoreCase(strOptString)) {
                PushManagerImpl.setPermissionTips(jSONObject.optString("permissionTips"));
                return null;
            }
            if ("setIsShowDefualtDialog".equalsIgnoreCase(strOptString)) {
                PushManagerImpl.setIsShowDefualtDialog(jSONObject.optBoolean("showDefualtDialog"));
                return null;
            }
            if (!"setPermissionPromptTimes".equalsIgnoreCase(strOptString)) {
                return null;
            }
            PushManagerImpl.setPermissionPromptTimes(context, jSONObject.optInt("timesLimit"));
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}