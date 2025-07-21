package com.netease.inner.pushclient.firebase;

import android.content.Context;
import com.netease.ntunisdk.base.PatchPlaceholder;
import com.netease.push.utils.PushLog;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes6.dex */
public class Firebase {
    private static final int GCM_SUCCESS = 0;
    private static final String TAG = "NGPush_" + Firebase.class.getSimpleName();
    private static Firebase s_inst = new Firebase();

    private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, PatchPlaceholder.class.getSimpleName());
    }

    public static Firebase getInst() {
        return s_inst;
    }

    public void init(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "init");
        try {
            Class.forName("com.netease.inner.pushclient.firebase.PushClient").getMethod("registerPush", Context.class).invoke(null, context);
        } catch (Exception e) {
            PushLog.e(TAG, "Firebase_SDK_Client jars not found:" + e.getMessage());
        }
    }

    public boolean isGooglePlayServicesAvailable(Context context) throws IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
        int iIntValue;
        try {
            Class<?> cls = Class.forName("com.google.android.gms.common.GoogleApiAvailability");
            iIntValue = ((Integer) cls.getMethod("isGooglePlayServicesAvailable", Context.class).invoke(cls.getMethod("getInstance", new Class[0]).invoke(null, new Object[0]), context)).intValue();
            PushLog.d(TAG, "isGooglePlayServicesAvailable:" + iIntValue);
        } catch (Exception e) {
            PushLog.e(TAG, "checkPlayServices error:" + e.toString());
            e.printStackTrace();
        }
        return iIntValue == 0;
    }
}