package com.netease.push.utils;

/* loaded from: classes3.dex */
public class PushConstants {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final int EVERYDAY = 127;
    public static final String FIREBASE = "fcm";
    public static final String FLYME = "flyme";
    public static final int FRIDAY = 16;
    public static final String GCM = "gcm";
    public static final String HUAWEI = "huawei";
    public static final String HUAWEI_HMS = "hms";
    public static final int MAX_ALARM_COUNT = 500;
    public static final String MIUI = "miui";
    public static final int MONDAY = 1;
    public static final String NIEPUSH = "niepush";
    public static final int NIEPUSH_MODE_ALWAYS_ON = 2;
    public static final int NIEPUSH_MODE_DISABLED = 0;
    public static final int NIEPUSH_MODE_WHEN_NEED = 1;
    public static final String NOTIFICATION_EXT = "ext";
    public static final String NOTIFICATION_ICON = "icon";
    public static final String NOTIFICATION_MESSAGE = "msg";
    public static final String NOTIFICATION_NOTIFYID = "notify_id";
    public static final String NOTIFICATION_REQID = "reqid";
    public static final String NOTIFICATION_SERVICE_TYPE = "service_type";
    public static final String NOTIFICATION_TITLE = "title";
    public static final String NOTIFICATION_URI = "uri";
    public static final String OPPO = "oppo";
    public static final int SATURDAY = 32;
    public static final int SUNDAY = 64;
    private static final String TAG = "NGPush_PushConstants";
    public static final int THURSDAY = 8;
    public static final int TUESDAY = 2;
    public static final String VIVO = "vivo";
    public static final int WEDNESDAY = 4;
    public static final int WEEKEND = 96;
    public static final int WORKDAY = 31;

    public static int MONTH_DAY(int i) {
        return 1 << (i - 1);
    }

    public static int MONTH_DAY_RANGE(int i, int i2) {
        return (i2 == 31 ? Integer.MAX_VALUE : (1 << i2) - 1) - ((1 << (i - 1)) - 1);
    }
}