package com.netease.pushservice;

import android.telephony.PhoneStateListener;
import com.netease.ntunisdk.base.PatchPlaceholder;
import com.netease.push.utils.PushLog;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes4.dex */
public class PhoneStateChangeListener extends PhoneStateListener {
    private static final String TAG = "NGPush_" + PhoneStateChangeListener.class.getSimpleName();

    private String getState(int i) {
        return i != 0 ? i != 1 ? i != 2 ? i != 3 ? "state unknown" : "DATA_SUSPENDED" : "DATA_CONNECTED" : "DATA_CONNECTING" : "DATA_DISCONNECTED";
    }

    private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, PatchPlaceholder.class.getSimpleName());
    }

    @Override // android.telephony.PhoneStateListener
    public void onDataConnectionStateChanged(int i) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        super.onDataConnectionStateChanged(i);
        PushLog.d(TAG, "onDataConnectionStateChanged");
        PushLog.d(TAG, "state = " + i + ", " + getState(i));
        if (2 == i) {
            PushServiceHelper.getInstance().connect(false);
        }
    }
}