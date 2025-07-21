package com.netease.inner.pushclient.vivo;

import android.content.Context;
import android.text.TextUtils;
import com.netease.inner.pushclient.PushManager;
import com.netease.ntunisdk.base.PatchPlaceholder;
import com.netease.push.utils.PushLog;
import com.netease.pushclient.CheckAppIdKeyUtil;
import com.xiaomi.mipush.sdk.MiPushClient;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes6.dex */
public class Vivo {
    private static final String TAG = "NGPush_" + Vivo.class.getSimpleName();
    private static Vivo s_inst = new Vivo();
    String m_appid = "";
    String m_appkey = "";
    private Context m_ctx;
    String m_regid;

    private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, PatchPlaceholder.class.getSimpleName());
    }

    public static Vivo getInst() {
        return s_inst;
    }

    public boolean checkSupportVIVOPush(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "init");
        this.m_ctx = context;
        try {
            return ((Boolean) Class.forName("com.netease.inner.pushclient.vivo.PushClient").getMethod("checkSupportVIVOPush", Context.class).invoke(null, this.m_ctx)).booleanValue();
        } catch (Exception e) {
            PushLog.e(TAG, "checkSupportVIVOPush, VIVO push jars(pushsdk-v2.3.4.jar) not found:" + e.getMessage());
            return false;
        }
    }

    public void init(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "init");
        this.m_ctx = context;
        String appID = PushManager.getInstance().getAppID(context, "vivo");
        String appKey = PushManager.getInstance().getAppKey(context, "vivo");
        if (TextUtils.isEmpty(appID) || TextUtils.isEmpty(appKey)) {
            PushLog.e(TAG, "AppID or appKey is empty");
            return;
        }
        CheckAppIdKeyUtil.modifyMetaData(this.m_ctx, "com.vivo.push.app_id", appID);
        CheckAppIdKeyUtil.modifyMetaData(this.m_ctx, "com.vivo.push.api_key", appKey);
        try {
            Class.forName("com.netease.inner.pushclient.vivo.PushClient").getMethod("registerPush", Context.class).invoke(null, this.m_ctx);
        } catch (Exception e) {
            PushLog.e(TAG, "Vivo_SDK_Client jars not found:" + e.getMessage());
        }
    }

    public void unregister(Context context) {
        PushLog.i(TAG, MiPushClient.COMMAND_UNREGISTER);
        try {
            Class.forName("com.netease.inner.pushclient.vivo.PushClient").getDeclaredMethod("unregisterPush", Context.class).invoke(null, context);
        } catch (Exception e) {
            PushLog.e(TAG, "unregister -> e:" + e);
        }
    }
}