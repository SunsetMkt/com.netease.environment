package com.netease.androidcrashhandler.thirdparty.lifecycle;

import android.content.Context;
import android.util.ArrayMap;
import com.netease.androidcrashhandler.thirdparty.unilogger.CUniLogger;
import com.netease.androidcrashhandler.util.LogUtils;
import com.netease.ntunisdk.modules.api.ModulesCallback;
import com.netease.ntunisdk.modules.api.ModulesManager;
import com.netease.ntunisdk.modules.applicationlifecycle.ApplicationLifecycleModule;
import com.netease.rnccplayer.VideoViewManager;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class Lifecycle {
    private static final String TAG = "Lifecycle";
    private static final String mSource = "crashhunter0";
    private static Lifecycle sLifecycle;
    private boolean mForeground = true;

    private Lifecycle() {
    }

    public static Lifecycle getInstence() {
        if (sLifecycle == null) {
            sLifecycle = new Lifecycle();
        }
        return sLifecycle;
    }

    public void initLifecycle(Context context) {
        LogUtils.i(LogUtils.TAG, "Lifecycle [initLifecycle] start");
        addModuleCallback();
        startListen();
        if (!isRegister()) {
            register(context);
        }
        LogUtils.i(LogUtils.TAG, "Lifecycle [initLifecycle] end");
    }

    private void startListen() throws JSONException {
        LogUtils.i(LogUtils.TAG, "Lifecycle [startListen] start");
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("methodId", ApplicationLifecycleModule.ACTION_START_LISTEN);
            LogUtils.i(LogUtils.TAG, "Lifecycle [startListen] result=" + ModulesManager.getInst().extendFunc(mSource, ApplicationLifecycleModule.MODULE_NAME, jSONObject.toString()));
        } catch (Exception e) {
            LogUtils.w(LogUtils.TAG, "Lifecycle [startListen] JSONException=" + e.toString());
            e.printStackTrace();
        }
    }

    /* renamed from: com.netease.androidcrashhandler.thirdparty.lifecycle.Lifecycle$1 */
    class AnonymousClass1 implements ModulesCallback {
        AnonymousClass1() {
        }

        /* JADX WARN: Removed duplicated region for block: B:58:0x0042  */
        @Override // com.netease.ntunisdk.modules.api.ModulesCallback
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void extendFuncCallback(java.lang.String r3, java.lang.String r4, java.lang.String r5) {
            /*
                r2 = this;
                java.lang.StringBuilder r3 = new java.lang.StringBuilder
                java.lang.String r4 = "Lifecycle [addModuleCallback] json="
                r3.<init>(r4)
                r3.append(r5)
                java.lang.String r3 = r3.toString()
                java.lang.String r4 = "trace"
                com.netease.androidcrashhandler.util.LogUtils.i(r4, r3)
                org.json.JSONObject r3 = new org.json.JSONObject     // Catch: java.lang.Exception -> L9a
                r3.<init>(r5)     // Catch: java.lang.Exception -> L9a
                java.lang.String r4 = "methodId"
                java.lang.String r4 = r3.optString(r4)     // Catch: java.lang.Exception -> L9a
                int r5 = r4.hashCode()     // Catch: java.lang.Exception -> L9a
                r0 = -907354074(0xffffffffc9eae026, float:-1924100.8)
                r1 = 1
                if (r5 == r0) goto L38
                r0 = 54220321(0x33b5621, float:5.505323E-37)
                if (r5 == r0) goto L2e
                goto L42
            L2e:
                java.lang.String r5 = "app_foreground"
                boolean r4 = r4.equals(r5)     // Catch: java.lang.Exception -> L9a
                if (r4 == 0) goto L42
                r4 = r1
                goto L43
            L38:
                java.lang.String r5 = "life_model"
                boolean r4 = r4.equals(r5)     // Catch: java.lang.Exception -> L9a
                if (r4 == 0) goto L42
                r4 = 0
                goto L43
            L42:
                r4 = -1
            L43:
                if (r4 == 0) goto L68
                if (r4 == r1) goto L48
                goto L9e
            L48:
                com.netease.androidcrashhandler.thirdparty.lifecycle.Lifecycle r4 = com.netease.androidcrashhandler.thirdparty.lifecycle.Lifecycle.this     // Catch: java.lang.Exception -> L9a
                java.lang.String r5 = "foreground"
                boolean r3 = r3.optBoolean(r5)     // Catch: java.lang.Exception -> L9a
                com.netease.androidcrashhandler.thirdparty.lifecycle.Lifecycle.access$002(r4, r3)     // Catch: java.lang.Exception -> L9a
                com.netease.androidcrashhandler.entity.di.DiProxy r3 = com.netease.androidcrashhandler.entity.di.DiProxy.getInstance()     // Catch: java.lang.Exception -> L9a
                r3.updateDiInfoToLocalFile()     // Catch: java.lang.Exception -> L9a
                com.netease.androidcrashhandler.unknownCrash.CheckNormalExitManager r3 = com.netease.androidcrashhandler.unknownCrash.CheckNormalExitManager.getInstance()     // Catch: java.lang.Exception -> L9a
                com.netease.androidcrashhandler.thirdparty.lifecycle.Lifecycle r4 = com.netease.androidcrashhandler.thirdparty.lifecycle.Lifecycle.this     // Catch: java.lang.Exception -> L9a
                boolean r4 = com.netease.androidcrashhandler.thirdparty.lifecycle.Lifecycle.access$000(r4)     // Catch: java.lang.Exception -> L9a
                r3.isAppForeground(r4)     // Catch: java.lang.Exception -> L9a
                goto L9e
            L68:
                java.lang.String r4 = "life_model_int"
                int r3 = r3.optInt(r4)     // Catch: java.lang.Exception -> L9a
                if (r3 == r1) goto L92
                r4 = 2
                if (r3 == r4) goto L8a
                r4 = 3
                if (r3 == r4) goto L82
                r4 = 4
                if (r3 == r4) goto L7a
                goto L9e
            L7a:
                com.netease.androidcrashhandler.unknownCrash.CheckNormalExitManager r3 = com.netease.androidcrashhandler.unknownCrash.CheckNormalExitManager.getInstance()     // Catch: java.lang.Exception -> L9a
                r3.onActivityDestroy()     // Catch: java.lang.Exception -> L9a
                goto L9e
            L82:
                com.netease.androidcrashhandler.unknownCrash.CheckNormalExitManager r3 = com.netease.androidcrashhandler.unknownCrash.CheckNormalExitManager.getInstance()     // Catch: java.lang.Exception -> L9a
                r3.onActivityStop()     // Catch: java.lang.Exception -> L9a
                goto L9e
            L8a:
                com.netease.androidcrashhandler.unknownCrash.CheckNormalExitManager r3 = com.netease.androidcrashhandler.unknownCrash.CheckNormalExitManager.getInstance()     // Catch: java.lang.Exception -> L9a
                r3.onActivityStart()     // Catch: java.lang.Exception -> L9a
                goto L9e
            L92:
                com.netease.androidcrashhandler.unknownCrash.CheckNormalExitManager r3 = com.netease.androidcrashhandler.unknownCrash.CheckNormalExitManager.getInstance()     // Catch: java.lang.Exception -> L9a
                r3.onActivityCreate()     // Catch: java.lang.Exception -> L9a
                goto L9e
            L9a:
                r3 = move-exception
                r3.printStackTrace()
            L9e:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.netease.androidcrashhandler.thirdparty.lifecycle.Lifecycle.AnonymousClass1.extendFuncCallback(java.lang.String, java.lang.String, java.lang.String):void");
        }
    }

    private void addModuleCallback() {
        LogUtils.i(LogUtils.TAG, "Lifecycle [addModuleCallback] start");
        ModulesManager.getInst().addModuleCallback(mSource, ApplicationLifecycleModule.MODULE_NAME, new ModulesCallback() { // from class: com.netease.androidcrashhandler.thirdparty.lifecycle.Lifecycle.1
            AnonymousClass1() {
            }

            @Override // com.netease.ntunisdk.modules.api.ModulesCallback
            public void extendFuncCallback(String v, String v2, String v3) {
                /*
                    this = this;
                    java.lang.StringBuilder r3 = new java.lang.StringBuilder
                    java.lang.String r4 = "Lifecycle [addModuleCallback] json="
                    r3.<init>(r4)
                    r3.append(r5)
                    java.lang.String r3 = r3.toString()
                    java.lang.String r4 = "trace"
                    com.netease.androidcrashhandler.util.LogUtils.i(r4, r3)
                    org.json.JSONObject r3 = new org.json.JSONObject     // Catch: java.lang.Exception -> L9a
                    r3.<init>(r5)     // Catch: java.lang.Exception -> L9a
                    java.lang.String r4 = "methodId"
                    java.lang.String r4 = r3.optString(r4)     // Catch: java.lang.Exception -> L9a
                    int r5 = r4.hashCode()     // Catch: java.lang.Exception -> L9a
                    r0 = -907354074(0xffffffffc9eae026, float:-1924100.8)
                    r1 = 1
                    if (r5 == r0) goto L38
                    r0 = 54220321(0x33b5621, float:5.505323E-37)
                    if (r5 == r0) goto L2e
                    goto L42
                L2e:
                    java.lang.String r5 = "app_foreground"
                    boolean r4 = r4.equals(r5)     // Catch: java.lang.Exception -> L9a
                    if (r4 == 0) goto L42
                    r4 = r1
                    goto L43
                L38:
                    java.lang.String r5 = "life_model"
                    boolean r4 = r4.equals(r5)     // Catch: java.lang.Exception -> L9a
                    if (r4 == 0) goto L42
                    r4 = 0
                    goto L43
                L42:
                    r4 = -1
                L43:
                    if (r4 == 0) goto L68
                    if (r4 == r1) goto L48
                    goto L9e
                L48:
                    com.netease.androidcrashhandler.thirdparty.lifecycle.Lifecycle r4 = com.netease.androidcrashhandler.thirdparty.lifecycle.Lifecycle.this     // Catch: java.lang.Exception -> L9a
                    java.lang.String r5 = "foreground"
                    boolean r3 = r3.optBoolean(r5)     // Catch: java.lang.Exception -> L9a
                    com.netease.androidcrashhandler.thirdparty.lifecycle.Lifecycle.access$002(r4, r3)     // Catch: java.lang.Exception -> L9a
                    com.netease.androidcrashhandler.entity.di.DiProxy r3 = com.netease.androidcrashhandler.entity.di.DiProxy.getInstance()     // Catch: java.lang.Exception -> L9a
                    r3.updateDiInfoToLocalFile()     // Catch: java.lang.Exception -> L9a
                    com.netease.androidcrashhandler.unknownCrash.CheckNormalExitManager r3 = com.netease.androidcrashhandler.unknownCrash.CheckNormalExitManager.getInstance()     // Catch: java.lang.Exception -> L9a
                    com.netease.androidcrashhandler.thirdparty.lifecycle.Lifecycle r4 = com.netease.androidcrashhandler.thirdparty.lifecycle.Lifecycle.this     // Catch: java.lang.Exception -> L9a
                    boolean r4 = com.netease.androidcrashhandler.thirdparty.lifecycle.Lifecycle.access$000(r4)     // Catch: java.lang.Exception -> L9a
                    r3.isAppForeground(r4)     // Catch: java.lang.Exception -> L9a
                    goto L9e
                L68:
                    java.lang.String r4 = "life_model_int"
                    int r3 = r3.optInt(r4)     // Catch: java.lang.Exception -> L9a
                    if (r3 == r1) goto L92
                    r4 = 2
                    if (r3 == r4) goto L8a
                    r4 = 3
                    if (r3 == r4) goto L82
                    r4 = 4
                    if (r3 == r4) goto L7a
                    goto L9e
                L7a:
                    com.netease.androidcrashhandler.unknownCrash.CheckNormalExitManager r3 = com.netease.androidcrashhandler.unknownCrash.CheckNormalExitManager.getInstance()     // Catch: java.lang.Exception -> L9a
                    r3.onActivityDestroy()     // Catch: java.lang.Exception -> L9a
                    goto L9e
                L82:
                    com.netease.androidcrashhandler.unknownCrash.CheckNormalExitManager r3 = com.netease.androidcrashhandler.unknownCrash.CheckNormalExitManager.getInstance()     // Catch: java.lang.Exception -> L9a
                    r3.onActivityStop()     // Catch: java.lang.Exception -> L9a
                    goto L9e
                L8a:
                    com.netease.androidcrashhandler.unknownCrash.CheckNormalExitManager r3 = com.netease.androidcrashhandler.unknownCrash.CheckNormalExitManager.getInstance()     // Catch: java.lang.Exception -> L9a
                    r3.onActivityStart()     // Catch: java.lang.Exception -> L9a
                    goto L9e
                L92:
                    com.netease.androidcrashhandler.unknownCrash.CheckNormalExitManager r3 = com.netease.androidcrashhandler.unknownCrash.CheckNormalExitManager.getInstance()     // Catch: java.lang.Exception -> L9a
                    r3.onActivityCreate()     // Catch: java.lang.Exception -> L9a
                    goto L9e
                L9a:
                    r3 = move-exception
                    r3.printStackTrace()
                L9e:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.netease.androidcrashhandler.thirdparty.lifecycle.Lifecycle.AnonymousClass1.extendFuncCallback(java.lang.String, java.lang.String, java.lang.String):void");
            }
        });
    }

    private boolean isRegister() throws JSONException {
        LogUtils.i(LogUtils.TAG, "Lifecycle [isRegister] start");
        JSONObject jSONObject = new JSONObject();
        boolean z = false;
        try {
            jSONObject.put("methodId", ApplicationLifecycleModule.ACTION_IS_REGISTER);
            String strExtendFunc = ModulesManager.getInst().extendFunc(CUniLogger.source, ApplicationLifecycleModule.MODULE_NAME, jSONObject.toString());
            z = Boolean.parseBoolean(strExtendFunc);
            LogUtils.i(LogUtils.TAG, "Lifecycle [isRegister] res=" + strExtendFunc);
        } catch (Exception e) {
            LogUtils.i(LogUtils.TAG, "Lifecycle [isRegister] Exception=" + e.toString());
            e.printStackTrace();
        }
        LogUtils.i(LogUtils.TAG, "Lifecycle [isRegister] result=" + z);
        return z;
    }

    private void register(Context context) throws JSONException {
        LogUtils.i(LogUtils.TAG, "Lifecycle [register] start");
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("methodId", ApplicationLifecycleModule.ACTION_REGISTER_SYSTEM);
            LogUtils.i(LogUtils.TAG, "Lifecycle [register] res=" + ModulesManager.getInst().extendFunc("lifeCycle", ApplicationLifecycleModule.MODULE_NAME, jSONObject.toString(), context));
        } catch (Exception e) {
            LogUtils.i(LogUtils.TAG, "Lifecycle [register] Exception=" + e.toString());
            e.printStackTrace();
        }
    }

    public boolean isForeground() {
        return this.mForeground || hookActivityThreadCheckForeground();
    }

    private static boolean hookActivityThreadCheckForeground() throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
        ArrayMap arrayMap;
        try {
            Class<?> cls = Class.forName("android.app.ActivityThread");
            Object objInvoke = cls.getMethod("currentActivityThread", new Class[0]).invoke(null, new Object[0]);
            Field declaredField = cls.getDeclaredField("mActivities");
            declaredField.setAccessible(true);
            arrayMap = (ArrayMap) declaredField.get(objInvoke);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (arrayMap.size() < 1) {
            LogUtils.i(LogUtils.TAG, "Lifecycle [hookActivityThreadCheckForeground] isForeground:false");
            return false;
        }
        for (Object obj : arrayMap.values()) {
            Field declaredField2 = obj.getClass().getDeclaredField(VideoViewManager.PROP_PAUSED);
            declaredField2.setAccessible(true);
            if (!declaredField2.getBoolean(obj)) {
                LogUtils.i(LogUtils.TAG, "Lifecycle [hookActivityThreadCheckForeground] isForeground:true");
                return true;
            }
        }
        LogUtils.i(LogUtils.TAG, "Lifecycle [hookActivityThreadCheckForeground] isForeground:false");
        return false;
    }
}