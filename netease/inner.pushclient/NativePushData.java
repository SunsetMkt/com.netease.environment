package com.netease.inner.pushclient;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import androidx.core.app.NotificationCompat;
import com.CCMsgSdk.WebSocketMessageType;
import com.facebook.hermes.intl.Constants;
import com.netease.ntunisdk.base.PatchPlaceholder;
import com.netease.push.utils.NotifyMessageImpl;
import com.netease.push.utils.PushConstantsImpl;
import com.netease.push.utils.PushLog;
import com.netease.push.utils.PushSetting;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes4.dex */
public class NativePushData {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final int ONCE = 0;
    public static final int REPEAT_MONTH = 2;
    public static final int REPEAT_MONTH_BACKWARDS = 3;
    public static final int REPEAT_WEEK = 1;
    private static final String TAG = "NGPush_" + NativePushData.class.getSimpleName();
    private String mPushName;
    private NotifyMessageImpl mNotifyMessage = new NotifyMessageImpl();
    private int mHour = 0;
    private int mMinute = 0;
    private int mSecond = 0;
    private String mTimeZone = "";
    private int delayTriggerSec = 0;
    private int mRepeatMode = 0;
    private int mMode = 0;
    private int mYear = 0;
    private int mMonth = 0;
    private int mDay = 0;
    private int mPushID = 0;

    private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, PatchPlaceholder.class.getSimpleName());
    }

    public NotifyMessageImpl getNotifyMessage() {
        NotifyMessageImpl notifyMessageImpl = this.mNotifyMessage;
        notifyMessageImpl.mNative = true;
        return notifyMessageImpl;
    }

    public void setDelayTriggerSec(int i) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        this.delayTriggerSec = i;
        PushLog.i(TAG, "delay trigger second:" + i);
    }

    public int getRepeatMode() {
        return this.mRepeatMode;
    }

    public String getPushName() {
        return this.mPushName;
    }

    public NativePushData(String str) {
        this.mPushName = Constants.COLLATION_DEFAULT;
        clear();
        this.mPushName = str;
        this.mNotifyMessage.mNative = true;
    }

    public void clear() {
        this.mNotifyMessage.clear();
        this.mHour = 0;
        this.mMinute = 0;
        this.mSecond = 0;
        this.mMode = 0;
        this.mYear = 0;
        this.mMonth = 0;
        this.mDay = 0;
        this.mRepeatMode = 0;
        this.mPushName = Constants.COLLATION_DEFAULT;
        this.mPushID = 0;
    }

    public void setMessage(String str, String str2, String str3) {
        NotifyMessageImpl notifyMessageImpl = this.mNotifyMessage;
        notifyMessageImpl.mTitle = str;
        notifyMessageImpl.mMsg = str2;
        notifyMessageImpl.mExt = str3;
    }

    public void setMessage(String str, String str2, String str3, String str4) {
        NotifyMessageImpl notifyMessageImpl = this.mNotifyMessage;
        notifyMessageImpl.mTitle = str;
        notifyMessageImpl.mMsg = str2;
        notifyMessageImpl.mExt = str3;
        notifyMessageImpl.mSound = str4;
    }

    public void setMessage(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8) {
        NotifyMessageImpl notifyMessageImpl = this.mNotifyMessage;
        notifyMessageImpl.mTitle = str;
        notifyMessageImpl.mMsg = str2;
        notifyMessageImpl.mExt = str3;
        notifyMessageImpl.mSound = str4;
        notifyMessageImpl.mGroupId = str5;
        notifyMessageImpl.mGroupName = str6;
        notifyMessageImpl.mChannelId = str7;
        notifyMessageImpl.mChannelName = str8;
    }

    public void setTime(int i, int i2) {
        this.mHour = i;
        this.mMinute = i2;
        this.mSecond = 0;
    }

    public void setTime(int i, int i2, int i3) {
        this.mHour = i;
        this.mMinute = i2;
        this.mSecond = i3;
    }

    public void setTime(int i, int i2, String str) {
        this.mHour = i;
        this.mMinute = i2;
        this.mSecond = 0;
        this.mTimeZone = str;
    }

    public void setTime(int i, int i2, int i3, String str) {
        this.mHour = i;
        this.mMinute = i2;
        this.mSecond = i3;
        this.mTimeZone = str;
    }

    public void setWeekRepeat(int i) {
        this.mRepeatMode = 1;
        this.mMode = i;
    }

    public void setMonthRepeat(int i) {
        this.mRepeatMode = 2;
        this.mMode = i;
    }

    public void setMonthRepeatBackwards(int i) {
        this.mRepeatMode = 3;
        this.mMode = i;
    }

    public void setOnce(int i, int i2, int i3) {
        this.mRepeatMode = 0;
        this.mYear = i;
        this.mMonth = i2;
        this.mDay = i3;
    }

    public void setOnceUnixtime(long j) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(j * 1000));
        if (!TextUtils.isEmpty(this.mTimeZone)) {
            calendar.setTimeZone(TimeZone.getTimeZone(this.mTimeZone));
        } else {
            calendar.setTimeZone(TimeZone.getDefault());
        }
        this.mYear = calendar.get(1);
        this.mMonth = calendar.get(2);
        this.mDay = calendar.get(5);
        this.mHour = calendar.get(11);
        this.mMinute = calendar.get(12);
        this.mSecond = calendar.get(13);
        this.mRepeatMode = 0;
    }

    public void createPushID(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        this.mPushID = Math.abs((context.getPackageName() + this.mPushName).hashCode());
        PushLog.i(TAG, "createPushID=" + this.mPushID);
    }

    public Intent getNativeNotifyIntent(String str) {
        Intent intentCreateMethodIntent = PushClientReceiver.createMethodIntent();
        intentCreateMethodIntent.setPackage(str);
        intentCreateMethodIntent.putExtra("package", str);
        intentCreateMethodIntent.putExtra(PushConstantsImpl.INTENT_PUSH_NAME, this.mPushName);
        intentCreateMethodIntent.putExtra("method", "nativenotify");
        return intentCreateMethodIntent;
    }

    private long getTriggerAtMillis() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        long repeatNextTime;
        long jCurrentTimeMillis = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        if (!TextUtils.isEmpty(this.mTimeZone)) {
            calendar.setTimeZone(TimeZone.getTimeZone(this.mTimeZone));
        } else {
            calendar.setTimeZone(TimeZone.getDefault());
        }
        calendar.set(11, this.mHour);
        calendar.set(12, this.mMinute);
        calendar.set(13, this.mSecond);
        calendar.set(14, 0);
        int i = this.mRepeatMode;
        if (i == 0) {
            calendar.set(this.mYear, this.mMonth, this.mDay);
            repeatNextTime = calendar.getTimeInMillis();
        } else if (i == 1) {
            int i2 = this.mMode;
            if (i2 == 0) {
                return 0L;
            }
            repeatNextTime = getRepeatNextTime(calendar, 7, (((i2 & 64) != 0 ? 1 : 0) | (this.mMode << 1)) & 127);
        } else if (i == 2 || i == 3) {
            int i3 = this.mMode;
            if (i3 == 0) {
                return 0L;
            }
            repeatNextTime = getRepeatNextTime(calendar, 5, i3);
        } else {
            repeatNextTime = 0;
        }
        PushLog.i(TAG, String.format("%s next trigger time:%s, after %d sec", this.mPushName, calendar.getTime().toString(), Long.valueOf((repeatNextTime - jCurrentTimeMillis) / 1000)));
        if (repeatNextTime < jCurrentTimeMillis) {
            return 0L;
        }
        return (0 + repeatNextTime) - jCurrentTimeMillis;
    }

    private long getRepeatNextTime(Calendar calendar, int i, int i2) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        long timeInMillis = calendar.getTimeInMillis();
        long jCurrentTimeMillis = System.currentTimeMillis() + (this.delayTriggerSec * 1000);
        if (this.mRepeatMode != 3) {
            while (true) {
                if (timeInMillis >= jCurrentTimeMillis && ((1 << (calendar.get(i) - 1)) & i2) != 0) {
                    break;
                }
                calendar.add(5, 1);
                timeInMillis = calendar.getTimeInMillis();
            }
        } else {
            GregorianCalendar gregorianCalendar = new GregorianCalendar(calendar.get(1), calendar.get(2), calendar.get(5));
            while (true) {
                if (timeInMillis >= jCurrentTimeMillis && ((1 << (gregorianCalendar.getActualMaximum(5) - calendar.get(i))) & i2) != 0) {
                    break;
                }
                calendar.add(5, 1);
                timeInMillis = calendar.getTimeInMillis();
                gregorianCalendar.add(5, 1);
            }
        }
        PushLog.d(TAG, "getRepeatNextTime select:" + timeInMillis + " dalayTriggerSec:" + this.delayTriggerSec + " current:" + System.currentTimeMillis());
        return timeInMillis;
    }

    public void startAlarm(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        startAlarm(context, getNativeNotifyIntent(context.getPackageName()));
    }

    public void startAlarm(Context context, String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        startAlarm(context, getNativeNotifyIntent(str));
    }

    public void startAlarm(Context context, Intent intent) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "startAlarm mPushName=" + this.mPushName);
        PendingIntent broadcast = PendingIntent.getBroadcast(context, this.mPushID, intent, 67108864);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
        long triggerAtMillis = getTriggerAtMillis();
        if (triggerAtMillis > 0) {
            long jCurrentTimeMillis = triggerAtMillis + System.currentTimeMillis();
            PushLog.d(TAG, this.mPushName + " triggerAtmillis:" + jCurrentTimeMillis);
            if (Build.VERSION.SDK_INT >= 19) {
                try {
                    alarmManager.setExact(0, jCurrentTimeMillis, broadcast);
                    return;
                } catch (Exception e) {
                    PushLog.d(TAG, "setExact Exception use alarmManager.set" + e);
                    alarmManager.set(0, jCurrentTimeMillis, broadcast);
                    return;
                }
            }
            alarmManager.set(0, jCurrentTimeMillis, broadcast);
            return;
        }
        PushLog.d(TAG, "triggerAtmillis timeout");
        PushSetting.rmNativePushName(context, this.mPushName);
    }

    public void stopAlarm(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "stopAlarm mPushName=" + this.mPushName);
        PendingIntent broadcast = PendingIntent.getBroadcast(context, this.mPushID, getNativeNotifyIntent(context.getPackageName()), 67108864);
        ((AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM)).cancel(broadcast);
        broadcast.cancel();
    }

    public String writeToJsonString() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(WebSocketMessageType.NOTIFY, this.mNotifyMessage.writeToJsonString());
        jSONObject.put("repeat", this.mRepeatMode);
        jSONObject.put("mode", this.mMode);
        jSONObject.put("year", this.mYear);
        jSONObject.put("month", this.mMonth);
        jSONObject.put("day", this.mDay);
        jSONObject.put("hour", this.mHour);
        jSONObject.put("min", this.mMinute);
        jSONObject.put("sec", this.mSecond);
        jSONObject.put("pushid", this.mPushID);
        jSONObject.put("timeZone", this.mTimeZone);
        return jSONObject.toString();
    }

    public static NativePushData readFromJsonString(String str, String str2) throws JSONException {
        JSONObject jSONObject = new JSONObject(str2);
        NativePushData nativePushData = new NativePushData(str);
        nativePushData.mNotifyMessage = NotifyMessageImpl.readFromJsonString(jSONObject.getString(WebSocketMessageType.NOTIFY));
        nativePushData.mNotifyMessage.mNative = true;
        nativePushData.mRepeatMode = jSONObject.getInt("repeat");
        nativePushData.mMode = jSONObject.getInt("mode");
        nativePushData.mHour = jSONObject.getInt("hour");
        nativePushData.mMinute = jSONObject.getInt("min");
        nativePushData.mSecond = jSONObject.optInt("sec", 0);
        nativePushData.mPushID = jSONObject.getInt("pushid");
        nativePushData.mYear = jSONObject.getInt("year");
        nativePushData.mMonth = jSONObject.getInt("month");
        nativePushData.mDay = jSONObject.getInt("day");
        nativePushData.mTimeZone = jSONObject.optString("timeZone");
        return nativePushData;
    }
}