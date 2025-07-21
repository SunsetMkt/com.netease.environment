package com.netease.ntunisdk.modules.permission.common;

import com.netease.ntunisdk.SdkQRCode;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class PermissionConstant {
    public static final int PERMISSION_DENIED_FLAG = -1;
    public static final int PERMISSION_GRANTED_FLAG = 0;
    public static final String PERMISSION_KEY = "permission";
    public static final String PERMISSION_NEVER_AGAIN = "HAS_BEEN_SET_NEVER_ASK_AGAIN_";
    public static final int PERMISSION_NEVER_AGAIN_FLAG = -2;
    public static final String PERMISSION_SPLIT = ",";
    public static final int REQUEST_CODE = 1336;
    public static final String PERMISSION_WRITE_SETTINGS = "android.permission.WRITE_SETTINGS";
    public static final String PERMISSION_SYSTEM_ALERT_WINDOW = "android.permission.SYSTEM_ALERT_WINDOW";
    public static final String[] PERMISSION_ARRAY = {"android.permission.READ_CALENDAR", "android.permission.WRITE_CALENDAR", SdkQRCode.PERMISSION_CAMERA, "android.permission.READ_CONTACTS", "android.permission.WRITE_CONTACTS", "android.permission.GET_ACCOUNTS", "android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_BACKGROUND_LOCATION", "android.permission.ACCESS_MEDIA_LOCATION", "android.permission.RECORD_AUDIO", "android.permission.READ_PHONE_STATE", "android.permission.CALL_PHONE", "android.permission.READ_CALL_LOG", "android.permission.WRITE_CALL_LOG", "android.permission.ADD_VOICEMAIL", "android.permission.USE_SIP", "android.permission.PROCESS_OUTGOING_CALLS", "android.permission.READ_PHONE_NUMBERS", "android.permission.ANSWER_PHONE_CALLS", "android.permission.ACCEPT_HANDOVER", "android.permission.BODY_SENSORS", "android.permission.BODY_SENSORS_BACKGROUND", "android.permission.SEND_SMS", "android.permission.RECEIVE_SMS", "android.permission.RECEIVE_WAP_PUSH", "android.permission.RECEIVE_MMS", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACTIVITY_RECOGNITION", "android.permission.READ_MEDIA_AUDIO", "android.permission.READ_MEDIA_IMAGES", "android.permission.READ_MEDIA_VIDEO", "android.permission.BLUETOOTH_ADVERTISE", "android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN", "android.permission.NEARBY_WIFI_DEVICES", "android.permission.POST_NOTIFICATIONS", PERMISSION_WRITE_SETTINGS, PERMISSION_SYSTEM_ALERT_WINDOW, "android.permission.READ_SMS", "android.permission.READ_MEDIA_VISUAL_USER_SELECTED"};
    public static final Map<String, Integer> MIN_SDK_PERMISSIONS = new HashMap(12);

    static {
        MIN_SDK_PERMISSIONS.put("com.android.voicemail.permission.ADD_VOICEMAIL", 14);
        MIN_SDK_PERMISSIONS.put("android.permission.READ_CALL_LOG", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.READ_EXTERNAL_STORAGE", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.WRITE_CALL_LOG", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.BODY_SENSORS", 20);
        MIN_SDK_PERMISSIONS.put(PERMISSION_SYSTEM_ALERT_WINDOW, 23);
        MIN_SDK_PERMISSIONS.put(PERMISSION_WRITE_SETTINGS, 23);
        MIN_SDK_PERMISSIONS.put("android.permission.READ_PHONE_NUMBERS", 26);
        MIN_SDK_PERMISSIONS.put("android.permission.ANSWER_PHONE_CALLS", 26);
        MIN_SDK_PERMISSIONS.put("android.permission.ACCEPT_HANDOVER", 28);
        MIN_SDK_PERMISSIONS.put("android.permission.ACTIVITY_RECOGNITION", 29);
        MIN_SDK_PERMISSIONS.put("android.permission.ACCESS_MEDIA_LOCATION", 29);
        MIN_SDK_PERMISSIONS.put("android.permission.ACCESS_BACKGROUND_LOCATION", 29);
        MIN_SDK_PERMISSIONS.put("android.permission.BLUETOOTH_ADVERTISE", 31);
        MIN_SDK_PERMISSIONS.put("android.permission.BLUETOOTH_CONNECT", 31);
        MIN_SDK_PERMISSIONS.put("android.permission.BLUETOOTH_SCAN", 31);
        MIN_SDK_PERMISSIONS.put("android.permission.BODY_SENSORS_BACKGROUND", 33);
        MIN_SDK_PERMISSIONS.put("android.permission.READ_MEDIA_AUDIO", 33);
        MIN_SDK_PERMISSIONS.put("android.permission.READ_MEDIA_IMAGES", 33);
        MIN_SDK_PERMISSIONS.put("android.permission.READ_MEDIA_VIDEO", 33);
        MIN_SDK_PERMISSIONS.put("android.permission.NEARBY_WIFI_DEVICES", 33);
        MIN_SDK_PERMISSIONS.put("android.permission.POST_NOTIFICATIONS", 33);
        MIN_SDK_PERMISSIONS.put("android.permission.READ_MEDIA_VISUAL_USER_SELECTED", 34);
    }
}