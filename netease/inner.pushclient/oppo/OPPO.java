package com.netease.inner.pushclient.oppo;

import android.content.Context;
import com.netease.inner.pushclient.PushManager;
import com.netease.ntunisdk.base.PatchPlaceholder;
import com.netease.push.utils.PushLog;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes6.dex */
public class OPPO {
    private static final String TAG = "NGPush_" + OPPO.class.getSimpleName();
    private static OPPO s_inst = new OPPO();
    String m_appid = "";
    String m_appkey = "";
    private Context m_ctx;
    String m_regid;

    private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, PatchPlaceholder.class.getSimpleName());
    }

    public static OPPO getInst() {
        return s_inst;
    }

    public boolean checkSupportOPPOPush(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "init");
        this.m_ctx = context;
        try {
            return ((Boolean) Class.forName("com.netease.inner.pushclient.oppo.PushClient").getMethod("checkSupportOPPOPush", Context.class).invoke(null, this.m_ctx)).booleanValue();
        } catch (Exception e) {
            PushLog.e(TAG, "checkSupportOPPOPush, OPPO push jars(mcssdk-1.0.1.jar) not found:" + e.getMessage());
            return false;
        }
    }

    public void init(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i("\u8c03\u8bd5", "\u8fdb\u5165OPPO\u521d\u59cb\u5316");
        PushLog.i(TAG, "init");
        this.m_ctx = context;
        this.m_appid = PushManager.getInstance().getAppID(context, "oppo");
        this.m_appkey = PushManager.getInstance().getAppKey(context, "oppo");
        try {
            Class.forName("com.netease.inner.pushclient.oppo.PushClient").getMethod("registerPush", Context.class, String.class, String.class).invoke(null, this.m_ctx, this.m_appid, this.m_appkey);
        } catch (Exception e) {
            PushLog.e(TAG, "OppoPush_SDK_Client jars not found:" + e.getMessage());
        }
    }

    public void unregister(Context context, String str, String str2) {
        PushLog.d("\u8c03\u8bd5", "\u6ce8\u9500OPPO regid");
        try {
            Class.forName("com.netease.inner.pushclient.oppo.PushClient").getDeclaredMethod("unregisterPush", Context.class, String.class, String.class).invoke(null, context, str, str2);
        } catch (Exception e) {
            PushLog.e(TAG, "unregister -> e:" + e);
        }
    }
}