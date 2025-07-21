package com.netease.inner.pushclient.honor;

import android.content.Context;
import android.text.TextUtils;
import com.netease.inner.pushclient.PushManager;
import com.netease.push.utils.PushConstantsImpl;
import com.netease.push.utils.PushLog;
import com.netease.pushclient.CheckAppIdKeyUtil;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes6.dex */
public class Honor {
    private static final String TAG = "NGPush_" + Honor.class.getSimpleName();

    public static class HonorInstHolder {
        private static final Honor inst = new Honor();
    }

    public static Honor getInst() {
        return HonorInstHolder.inst;
    }

    public boolean checkSupportHonorPush(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "checkSupportHonorPush");
        try {
            return Boolean.TRUE.equals((Boolean) Class.forName("com.netease.inner.pushclient.honor.PushClient").getMethod("checkSupportHonorPush", Context.class).invoke(null, context));
        } catch (Exception e) {
            PushLog.e(TAG, "checkSupportHonorPush, Honor push jars not found:" + e.getMessage());
            return false;
        }
    }

    public void init(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String appID = PushManager.getInstance().getAppID(context, PushConstantsImpl.HONOR);
        if (TextUtils.isEmpty(appID)) {
            PushLog.e(TAG, "AppID is empty");
            return;
        }
        CheckAppIdKeyUtil.modifyMetaData(context, "com.hihonor.push.app_id", appID);
        try {
            Class.forName("com.netease.inner.pushclient.honor.PushClient").getMethod("registerPush", Context.class).invoke(null, context);
        } catch (Exception e) {
            PushLog.e(TAG, "HonorPush_SDK_Client jars not found:" + e.getMessage());
        }
    }
}