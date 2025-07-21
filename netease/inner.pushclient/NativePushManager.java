package com.netease.inner.pushclient;

import android.content.Context;
import android.text.TextUtils;
import com.netease.ntunisdk.base.PatchPlaceholder;
import com.netease.push.utils.NotifyMessageImpl;
import com.netease.push.utils.PushLog;
import com.netease.push.utils.PushSetting;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/* loaded from: classes4.dex */
public class NativePushManager {
    private static final String TAG = "NGPush_" + NativePushManager.class.getSimpleName() + "_inner";
    private static NativePushManager nativePushManager = new NativePushManager();
    private Context mContext = null;
    private HashMap<String, NativePushData> mNativePushHashMap = new HashMap<>();
    public final String PUSH_NAME_PREFIX = "nn_";

    private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, PatchPlaceholder.class.getSimpleName());
    }

    private NativePushManager() {
    }

    public static NativePushManager getInstance() {
        return nativePushManager;
    }

    public void init(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "init, ctx:" + context + ", this:" + this);
        this.mContext = context;
        Set<String> nativePushNames = PushSetting.getNativePushNames(this.mContext);
        PushLog.d(TAG, "nativePushNameSet:" + nativePushNames);
    }

    public boolean newAlarm(String str, String str2, String str3, String str4) {
        return newAlarm(str, str2, str3, str4, "");
    }

    public boolean newAlarm(String str, String str2, String str3, String str4, String str5) {
        return newAlarm(str, str2, str3, str4, str5, "", "", "", "");
    }

    public boolean newAlarm(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String str10 = "nn_" + str;
        PushLog.i(TAG, "newAlarm alarmID:" + str + ", title:" + str2 + ", msg:" + str3 + ", ext:" + str4 + ", pushName:" + str10);
        Set<String> nativePushNames = PushSetting.getNativePushNames(this.mContext);
        String str11 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("nativePushNameSet:");
        sb.append(nativePushNames);
        PushLog.d(str11, sb.toString());
        if (nativePushNames.contains(str10)) {
            NativePushData nativeNotification = PushSetting.getNativeNotification(this.mContext, str10);
            if (nativeNotification != null) {
                this.mNativePushHashMap.put(str10, nativeNotification);
            } else {
                PushSetting.rmNativePushName(this.mContext, str10);
            }
        }
        if (this.mNativePushHashMap.containsKey(str10)) {
            if (TextUtils.isEmpty(str6) || TextUtils.isEmpty(str6) || TextUtils.isEmpty(str8) || TextUtils.isEmpty(str9)) {
                this.mNativePushHashMap.get(str10).setMessage(str2, str3, str4, str5);
                return true;
            }
            this.mNativePushHashMap.get(str10).setMessage(str2, str3, str4, str5, str6, str7, str8, str9);
            return true;
        }
        NativePushData nativePushData = new NativePushData(str10);
        if (TextUtils.isEmpty(str6) || TextUtils.isEmpty(str6) || TextUtils.isEmpty(str8) || TextUtils.isEmpty(str9)) {
            nativePushData.setMessage(str2, str3, str4, str5);
        } else {
            nativePushData.setMessage(str2, str3, str4, str5, str6, str7, str8, str9);
        }
        this.mNativePushHashMap.put(str10, nativePushData);
        return true;
    }

    public boolean setAlarmTime(String str, int i, int i2) {
        return setAlarmTime(str, i, i2, 0, "");
    }

    public boolean setAlarmTime(String str, int i, int i2, String str2) {
        return setAlarmTime(str, i, i2, 0, str2);
    }

    public boolean setAlarmTime(String str, int i, int i2, int i3, String str2) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String str3 = "nn_" + str;
        PushLog.i(TAG, "setAlarmTime");
        PushLog.d(TAG, "pushName:" + str3);
        PushLog.d(TAG, "alarmID:" + str);
        PushLog.d(TAG, "hour:" + i);
        PushLog.d(TAG, "minute:" + i2);
        PushLog.d(TAG, "second:" + i3);
        PushLog.d(TAG, "tz:" + str2);
        if (!this.mNativePushHashMap.containsKey(str3)) {
            return false;
        }
        this.mNativePushHashMap.get(str3).setTime(i, i2, i3, str2);
        return true;
    }

    public boolean setWeekRepeat(String str, int i) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String str2 = "nn_" + str;
        PushLog.i(TAG, "setWeekRepeat alarmID:" + str + ", weekMode:" + i + ", pushName:" + str2);
        if (i > 127 || i <= 0 || !this.mNativePushHashMap.containsKey(str2)) {
            return false;
        }
        this.mNativePushHashMap.get(str2).setWeekRepeat(i);
        return true;
    }

    public boolean setMonthRepeat(String str, int i) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String str2 = "nn_" + str;
        PushLog.i(TAG, "setMonthRepeat alarmID:" + str + ", monthMode:" + i + ", pushName:" + str2);
        if (i == 0 || !this.mNativePushHashMap.containsKey(str2)) {
            return false;
        }
        this.mNativePushHashMap.get(str2).setMonthRepeat(i);
        return true;
    }

    public boolean setMonthRepeatBackwards(String str, int i) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String str2 = "nn_" + str;
        PushLog.i(TAG, "setMonthRepeatBackwards alarmID:" + str + ", monthMode:" + i + ", pushName:" + str2);
        if (i == 0 || !this.mNativePushHashMap.containsKey(str2)) {
            return false;
        }
        this.mNativePushHashMap.get(str2).setMonthRepeatBackwards(i);
        return true;
    }

    public boolean setOnce(String str, int i, int i2, int i3) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String str2 = "nn_" + str;
        PushLog.i(TAG, "setOnce alarmID:" + str + ", year:" + i + ", month:" + i2 + ", day:" + i3 + ", pushName:" + str2);
        if (!this.mNativePushHashMap.containsKey(str2)) {
            return false;
        }
        this.mNativePushHashMap.get(str2).setOnce(i, i2, i3);
        return true;
    }

    public boolean setOnceUnixtime(String str, long j) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String str2 = "nn_" + str;
        PushLog.i(TAG, "setOnceUnixtime alarmID:" + str + ", ut:" + j + ", pushName:" + str2);
        if (!this.mNativePushHashMap.containsKey(str2)) {
            return false;
        }
        this.mNativePushHashMap.get(str2).setOnceUnixtime(j);
        return true;
    }

    public boolean startAlarm(String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String str2 = "nn_" + str;
        PushLog.i(TAG, "startAlarm alarmID:" + str + ", pushName:" + str2);
        if (!this.mNativePushHashMap.containsKey(str2)) {
            PushLog.e(TAG, "mNativePushHashMap does not contain pushName");
            return false;
        }
        NativePushData nativePushData = this.mNativePushHashMap.get(str2);
        nativePushData.createPushID(this.mContext);
        PushLog.d(TAG, "nativePushData.getPushName():" + nativePushData.getPushName());
        if (!str2.equals(nativePushData.getPushName())) {
            PushLog.e(TAG, "invalid nativePushData: inconsistent pushName");
            return false;
        }
        Set<String> nativePushNames = PushSetting.getNativePushNames(this.mContext);
        PushLog.d(TAG, "nativePushNameSet:" + nativePushNames);
        if (nativePushNames.contains(str2)) {
            stopPushWithPushName(str2);
        }
        if (!nativePushNames.contains(str2)) {
            if (nativePushNames.size() >= 500) {
                PushLog.e(TAG, "exceed max alarm count!");
                this.mNativePushHashMap.remove(str2);
                return false;
            }
            nativePushNames.add(str2);
            PushSetting.setNativePushNames(this.mContext, nativePushNames);
        }
        if (!PushSetting.setNativeNotification(this.mContext, nativePushData)) {
            PushLog.e(TAG, "PushSetting.setNativeNotification error");
            this.mNativePushHashMap.remove(str2);
            nativePushNames.remove(str2);
            PushSetting.rmNativePushName(this.mContext, str2);
            return false;
        }
        PushLog.d(TAG, "nativePushData.startAlarm");
        nativePushData.startAlarm(this.mContext);
        return true;
    }

    public boolean startAlarm(NativePushData nativePushData) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "startAlarm nativePushData:" + nativePushData + ", this:" + this);
        if (nativePushData == null) {
            PushLog.e(TAG, "nativePushData is null");
            return false;
        }
        String pushName = nativePushData.getPushName();
        PushLog.d(TAG, "pushName:" + pushName);
        Set<String> nativePushNames = PushSetting.getNativePushNames(this.mContext);
        PushLog.d(TAG, "nativePushNameSet:" + nativePushNames);
        if (nativePushNames.contains(pushName)) {
            NativePushData nativeNotification = PushSetting.getNativeNotification(this.mContext, pushName);
            if (nativeNotification != null) {
                this.mNativePushHashMap.put(pushName, nativeNotification);
            } else {
                nativePushNames.remove(pushName);
                PushSetting.rmNativePushName(this.mContext, pushName);
            }
        }
        NotifyMessageImpl notifyMessage = nativePushData.getNotifyMessage();
        if (this.mNativePushHashMap.containsKey(pushName)) {
            this.mNativePushHashMap.get(pushName).setMessage(notifyMessage.mTitle, notifyMessage.mMsg, notifyMessage.mExt, notifyMessage.mSound, notifyMessage.mGroupId, notifyMessage.mGroupName, notifyMessage.mChannelId, notifyMessage.mChannelName);
        } else {
            this.mNativePushHashMap.put(pushName, nativePushData);
        }
        NativePushData nativePushData2 = this.mNativePushHashMap.get(pushName);
        nativePushData2.createPushID(this.mContext);
        PushLog.d(TAG, "pushData.getPushName():" + nativePushData2.getPushName());
        if (!pushName.equals(nativePushData2.getPushName())) {
            PushLog.e(TAG, "invalid pushData: inconsistent pushName");
            return false;
        }
        if (nativePushNames.contains(pushName)) {
            stopPushWithPushName(pushName);
        }
        if (!nativePushNames.contains(pushName)) {
            if (nativePushNames.size() >= 500) {
                PushLog.e(TAG, "exceed max alarm count!");
                this.mNativePushHashMap.remove(pushName);
                return false;
            }
            nativePushNames.add(pushName);
            PushSetting.setNativePushNames(this.mContext, nativePushNames);
        }
        if (!PushSetting.setNativeNotification(this.mContext, nativePushData2)) {
            PushLog.e(TAG, "PushSetting.setNativeNotification error");
            this.mNativePushHashMap.remove(pushName);
            nativePushNames.remove(pushName);
            PushSetting.rmNativePushName(this.mContext, pushName);
            return false;
        }
        PushLog.d(TAG, "pushData.startAlarm");
        nativePushData2.startAlarm(this.mContext);
        return true;
    }

    public boolean stopPush(String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        NativePushData nativeNotification;
        String str2 = "nn_" + str;
        PushLog.i(TAG, "stopPush alarmID:" + str + ", pushName:" + str2);
        if (this.mNativePushHashMap.containsKey(str2)) {
            nativeNotification = this.mNativePushHashMap.get(str2);
        } else {
            nativeNotification = PushSetting.getNativeNotification(this.mContext, str2);
        }
        if (nativeNotification == null) {
            return false;
        }
        nativeNotification.stopAlarm(this.mContext);
        return true;
    }

    private boolean stopPushWithPushName(String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "stopPushWithPushName pushName:" + str);
        if (!this.mNativePushHashMap.containsKey(str)) {
            return false;
        }
        this.mNativePushHashMap.get(str).stopAlarm(this.mContext);
        return true;
    }

    public boolean removeAlarm(String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "removeAlarm alarmID:" + str);
        stopPush(str);
        String str2 = "nn_" + str;
        PushLog.d(TAG, "pushName:" + str2);
        this.mNativePushHashMap.remove(str2);
        PushSetting.rmNativePushName(this.mContext, str2);
        return true;
    }

    public boolean removeAllAlarms() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Set<String> nativePushNames = PushSetting.getNativePushNames(this.mContext);
        PushLog.i(TAG, "removeAllAlarms, nativePushNameSet=" + nativePushNames);
        Iterator<String> it = nativePushNames.iterator();
        while (it.hasNext()) {
            String strSubstring = it.next().substring(3);
            PushLog.i(TAG, "id=" + strSubstring);
            stopPush(strSubstring);
        }
        this.mNativePushHashMap.clear();
        PushSetting.rmAllNativePushNames(this.mContext);
        return true;
    }

    public String[] getAllAlarms() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Set<String> nativePushNames = PushSetting.getNativePushNames(this.mContext);
        PushLog.i(TAG, "getAllAlarms, nativePushNameSet=" + nativePushNames);
        String[] strArr = new String[nativePushNames.size()];
        Iterator<String> it = nativePushNames.iterator();
        int i = 0;
        while (it.hasNext()) {
            strArr[i] = it.next().substring(3);
            i++;
        }
        return strArr;
    }
}