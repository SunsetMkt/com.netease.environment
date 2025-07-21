package com.netease.inner.pushclient.huawei;

import android.content.Context;
import android.text.TextUtils;
import com.netease.inner.pushclient.PushManager;
import com.netease.ntunisdk.base.PatchPlaceholder;
import com.netease.push.utils.PushLog;
import com.netease.pushclient.CheckAppIdKeyUtil;
import com.netease.pushclient.PushManagerImpl;
import com.xiaomi.mipush.sdk.Constants;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes6.dex */
public class Huawei {
    private static final String TAG = "NGPush_" + Huawei.class.getSimpleName();
    private static Huawei s_inst = new Huawei();
    private PushManagerImpl.HmsCallback callback;
    String m_appid = "";
    private Context m_ctx;
    String m_regid;

    private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, PatchPlaceholder.class.getSimpleName());
    }

    public static Huawei getInst() {
        return s_inst;
    }

    public void init(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "init");
        this.m_ctx = context;
        this.m_appid = PushManager.getInstance().getAppID(context, "huawei");
        PushLog.i(TAG, "m_appid:" + this.m_appid);
        if (TextUtils.isEmpty(this.m_appid)) {
            PushLog.e(TAG, "AppID is empty");
            return;
        }
        CheckAppIdKeyUtil.modifyMetaData(this.m_ctx, Constants.HUAWEI_HMS_CLIENT_APPID, this.m_appid);
        try {
            Class.forName("com.netease.inner.pushclient.huawei.PushClient").getMethod("registerPush", Context.class, String.class).invoke(null, this.m_ctx, this.m_appid);
        } catch (Exception e) {
            try {
                Class.forName("com.netease.inner.pushclient.huawei.PushClient").getMethod("registerPush", Context.class).invoke(null, this.m_ctx);
            } catch (Exception e2) {
                e2.printStackTrace();
                PushLog.e(TAG, "registerPush, error:" + e.getMessage());
            }
            e.printStackTrace();
        }
    }

    public void checkHms(Context context, PushManagerImpl.HmsCallback hmsCallback) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "init");
        this.m_ctx = context;
        this.callback = hmsCallback;
        try {
            Class.forName("com.netease.inner.pushclient.huawei.PushClient").getMethod("checkHms", Context.class).invoke(null, this.m_ctx);
        } catch (Exception e) {
            e.printStackTrace();
            PushLog.e(TAG, "checkHms, error" + e.getMessage());
            hmsFail();
        }
    }

    public void hmsSuccess() {
        PushManagerImpl.HmsCallback hmsCallback = this.callback;
        if (hmsCallback != null) {
            hmsCallback.hmsSuccess();
        }
    }

    public void hmsFail() {
        PushManagerImpl.HmsCallback hmsCallback = this.callback;
        if (hmsCallback != null) {
            hmsCallback.hmsFail();
        }
    }
}