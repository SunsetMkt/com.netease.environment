package com.netease.push.utils;

import com.netease.ntunisdk.base.PatchPlaceholder;
import com.netease.push.proto.ProtoClientWrapper;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.LinkedList;

/* loaded from: classes3.dex */
public class AppInfo {
    public static final boolean DEFAULT_FIRST_START = true;
    public static final boolean DEFAULT_LIGHT = false;
    public static final int DEFAULT_NO_REPEAT_INTERVAL = 300;
    public static final long DEFAULT_RECEIVE_TIME = 0;
    public static final boolean DEFAULT_REPEAT_PROTECT = false;
    public static final boolean DEFAULT_SOUND = false;
    public static final boolean DEFAULT_VIBREATE = true;
    private static final String TAG = "NGPush_" + AppInfo.class.getSimpleName();
    public long mLastReceiveTime;
    public int mNoRepeatInterval;
    public String mPackageName;
    public boolean mbEnableLight;
    public boolean mbEnableSound;
    public boolean mbEnableVibrate;
    public boolean mbFirstStart;
    public boolean mbRepeatProtect;
    private LinkedList<ProtoClientWrapper.Message> messageList;

    private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, PatchPlaceholder.class.getSimpleName());
    }

    public AppInfo() {
        this.mPackageName = "";
        this.mbEnableSound = false;
        this.mbEnableVibrate = true;
        this.mbEnableLight = false;
        this.mbRepeatProtect = false;
        this.mNoRepeatInterval = 300;
        this.mbFirstStart = true;
        this.mLastReceiveTime = 0L;
        this.messageList = new LinkedList<>();
        clear();
    }

    public AppInfo(String str) {
        this.mPackageName = "";
        this.mbEnableSound = false;
        this.mbEnableVibrate = true;
        this.mbEnableLight = false;
        this.mbRepeatProtect = false;
        this.mNoRepeatInterval = 300;
        this.mbFirstStart = true;
        this.mLastReceiveTime = 0L;
        this.messageList = new LinkedList<>();
        this.mPackageName = str;
        clear();
    }

    public void clear() {
        this.mbEnableSound = false;
        this.mbEnableVibrate = true;
        this.mbRepeatProtect = false;
        this.mbFirstStart = true;
        this.mLastReceiveTime = 0L;
        this.mbEnableLight = false;
    }

    public void enableRepeatProtect(boolean z) {
        this.mbRepeatProtect = z;
    }

    public void setRepeatProtectInterval(int i) {
        this.mNoRepeatInterval = i;
    }

    public boolean filterMessage(ProtoClientWrapper.Message message) {
        boolean z;
        if (!this.mbRepeatProtect) {
            return false;
        }
        long j = message.time - this.mNoRepeatInterval;
        Iterator<ProtoClientWrapper.Message> it = this.messageList.iterator();
        int i = 0;
        while (true) {
            if (!it.hasNext()) {
                z = false;
                break;
            }
            ProtoClientWrapper.Message next = it.next();
            if (next.time >= j) {
                if (next.content.equals(message.content) && next.title.equals(message.title)) {
                    z = true;
                    break;
                }
            } else {
                i++;
            }
        }
        if (!z) {
            this.messageList.add(message);
        }
        for (int i2 = 0; i2 < i; i2++) {
            this.messageList.removeFirst();
        }
        return z;
    }
}