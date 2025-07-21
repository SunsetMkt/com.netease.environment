package com.netease.inner.pushclient.gcm;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import com.netease.inner.pushclient.PushManager;
import com.netease.ntunisdk.base.PatchPlaceholder;
import com.netease.push.utils.PushLog;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes6.dex */
public class GCM {
    private static final int GCM_SUCCESS = 0;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "NGPush_" + GCM.class.getSimpleName();
    private static GCM s_inst = new GCM();
    private Context m_ctx;

    private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, PatchPlaceholder.class.getSimpleName());
    }

    public static GCM getInst() {
        return s_inst;
    }

    public void init(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "init");
        this.m_ctx = context;
        String senderID = PushManager.getInstance().getSenderID(context, "gcm");
        if (TextUtils.isEmpty(senderID)) {
            PushLog.e(TAG, "Sender ID is empty, call PushManager.setSenderID() first");
            return;
        }
        try {
            Class.forName("com.netease.inner.pushclient.gcm.PushClient").getMethod("registerPush", Context.class, String.class).invoke(null, this.m_ctx, senderID);
        } catch (Exception e) {
            PushLog.e(TAG, "GCM_SDK_Client jars not found:" + e.getMessage());
        }
    }

    public boolean isGooglePlayServicesAvailable(Context context) throws IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
        Class<?> cls;
        Object objInvoke;
        int iIntValue;
        try {
            cls = Class.forName("com.google.android.gms.common.GoogleApiAvailability");
            objInvoke = cls.getMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
            iIntValue = ((Integer) cls.getMethod("isGooglePlayServicesAvailable", Context.class).invoke(objInvoke, context)).intValue();
        } catch (Exception e) {
            PushLog.e(TAG, "checkPlayServices error:" + e.toString());
        }
        if (iIntValue == 0) {
            return true;
        }
        if (((Boolean) cls.getMethod("isUserResolvableError", Integer.TYPE).invoke(objInvoke, Integer.valueOf(iIntValue))).booleanValue()) {
            ((Dialog) cls.getMethod("getErrorDialog", Activity.class, Integer.TYPE, Integer.TYPE).invoke(objInvoke, (Activity) context, Integer.valueOf(iIntValue), 9000)).show();
        } else {
            PushLog.e(TAG, "Google Play Services is not available on this device");
        }
        return false;
    }
}