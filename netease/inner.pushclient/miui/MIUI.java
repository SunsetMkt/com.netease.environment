package com.netease.inner.pushclient.miui;

import android.content.Context;
import android.text.TextUtils;
import com.netease.inner.pushclient.PushManager;
import com.netease.ntunisdk.base.PatchPlaceholder;
import com.netease.push.utils.PushLog;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes5.dex */
public class MIUI {
    private static final String TAG = "NGPush_" + MIUI.class.getSimpleName();
    private static MIUI s_inst = new MIUI();
    String m_appid = "";
    String m_appkey = "";
    private Context m_ctx;
    String m_regid;

    private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, PatchPlaceholder.class.getSimpleName());
    }

    public static MIUI getInst() {
        return s_inst;
    }

    public void init(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "init");
        this.m_ctx = context;
        this.m_appid = PushManager.getInstance().getAppID(context, "miui");
        this.m_appkey = PushManager.getInstance().getAppKey(context, "miui");
        if (TextUtils.isEmpty(this.m_appid)) {
            PushLog.e(TAG, "AppID is empty");
            return;
        }
        if (TextUtils.isEmpty(this.m_appkey)) {
            PushLog.e(TAG, "AppKey is empty");
            return;
        }
        try {
            Class.forName("com.netease.inner.pushclient.miui.PushClient").getMethod("registerPush", Context.class, String.class, String.class).invoke(null, this.m_ctx, this.m_appid, this.m_appkey);
        } catch (Exception e) {
            PushLog.e(TAG, "MiPush_SDK_Client jars not found:" + e.getMessage());
        }
    }
}