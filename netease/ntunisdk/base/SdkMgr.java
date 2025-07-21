package com.netease.ntunisdk.base;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.netease.ntsharesdk.Platform;
import com.netease.ntunisdk.base.utils.HTTPQueue;
import com.netease.ntunisdk.modules.api.ModulesManager;
import com.xiaomi.hy.dj.config.ResultCode;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes2.dex */
public class SdkMgr {
    public static final String TAG = "UniSDK Mgr";

    /* renamed from: a */
    private static Map<String, SdkBase> f1792a;
    private static Map<String, SdkBase> b;
    private static SdkBase c;
    private static volatile SdkDownload d;

    public static String getBaseSubVersion() {
        return "1";
    }

    public static String getBaseVersion() {
        return "1.9.3";
    }

    public static int getBaseVersionCode() {
        return ResultCode.REPOR_SZFPAY_FAIL;
    }

    public static void init(Context context) throws IOException, SecurityException {
        f1792a = new HashMap();
        b = new HashMap();
        if (c == null) {
            a(context);
            a(context, null);
        } else {
            Log.i(TAG, "setCtx again");
            c.setCtx(context);
            ModulesManager.getInst().reInit(context);
        }
    }

    public static void init(Context context, String str) throws IOException, SecurityException {
        f1792a = new HashMap();
        b = new HashMap();
        if (c == null) {
            a(context);
            a(context, str);
        } else {
            Log.i(TAG, "setCtx again");
            c.setCtx(context);
        }
    }

    public static GamerInterface getInst() {
        return c;
    }

    public static GamerInterface getInst(String str) {
        if (!TextUtils.isEmpty(str) && b.containsKey(str)) {
            SdkBase sdkBase = b.get(str);
            sdkBase.hasInit = c.hasInit;
            c.loginSdkInstMap.remove(str);
            c.loginSdkInstMap.put(c.getChannel(), c);
            sdkBase.b(c.loginSdkInstMap);
            c.loginSdkInstMap.clear();
            sdkBase.a(c.sdkInstMap);
            c.sdkInstMap.clear();
            b.put(c.getChannel(), c);
            c = sdkBase;
        }
        return c;
    }

    public static void destroyInst() {
        if (c.loginSdkInstMap != null) {
            c.loginSdkInstMap.clear();
        }
        if (c.sdkInstMap != null) {
            c.sdkInstMap.clear();
        }
        Map<String, SdkBase> map = b;
        if (map != null) {
            map.clear();
            b = null;
        }
        ModulesManager.getInst().unInit();
        c = null;
        HTTPQueue.clear();
    }

    /* JADX WARN: Removed duplicated region for block: B:219:0x0227  */
    /* JADX WARN: Removed duplicated region for block: B:228:0x025d  */
    /* JADX WARN: Removed duplicated region for block: B:237:0x02bc  */
    /* JADX WARN: Removed duplicated region for block: B:240:0x02c5  */
    /* JADX WARN: Removed duplicated region for block: B:247:0x02cc A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:277:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void a(android.content.Context r10, java.lang.String r11) throws java.io.IOException, java.lang.SecurityException {
        /*
            Method dump skipped, instructions count: 725
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.SdkMgr.a(android.content.Context, java.lang.String):void");
    }

    private static void a() {
        if (c == null) {
            if (f1792a.containsKey("playpark")) {
                c = f1792a.get("playpark");
                f1792a.remove("playpark");
                return;
            }
            Iterator<String> it = f1792a.keySet().iterator();
            if (it.hasNext()) {
                String next = it.next();
                c = f1792a.get(next);
                f1792a.remove(next);
            }
        }
    }

    private static void a(String str, Context context, String str2) throws SecurityException {
        try {
            if (str.contains("SdkUnisdkSingle")) {
                Log.i(TAG, "newInstanceByClass pass SdkUnisdkSingle");
                return;
            }
            Log.d(TAG, String.format("Class.forName(%s)", str));
            Constructor<?>[] constructors = Class.forName(str).getConstructors();
            if (constructors != null) {
                for (int i = 0; i < constructors.length; i++) {
                    Type[] genericParameterTypes = constructors[i].getGenericParameterTypes();
                    if (genericParameterTypes != null && genericParameterTypes.length == 1) {
                        SdkBase sdkBase = (SdkBase) constructors[i].newInstance(context);
                        sdkBase.setCtx(context);
                        String channel = sdkBase.getChannel();
                        if (!"common".equals(channel) || (c == null && f1792a.size() == 0)) {
                            SdkBase sdkBase2 = c;
                            if (sdkBase2 != null && "common".equals(sdkBase2.getChannel())) {
                                c = null;
                            }
                            Log.i(TAG, "new instance " + channel + ": " + str);
                            if (!channel.equals("cc_record") && !channel.equals("ngshare") && !channel.equals("nstore") && !channel.equals("mm_10086") && !channel.equals("wo_app") && !channel.equals("mdo_mm_10086") && ((!channel.equals("play_telecom") || str.endsWith("PlayLogin")) && ((!channel.equals("play") || str.endsWith(Platform.YIXIN)) && ((!channel.equals(ConstProp.NT_AUTH_NAME_YIXIN) || str.endsWith(Platform.YIXIN)) && !channel.equals("playpark") && !channel.equals("google_play") && !channel.equals("google_play_mto") && !channel.equals("google_play_ck") && !channel.equals("tstore_ck") && !channel.equals("nstore_ck") && !channel.equals("olleh_ck") && !channel.equals("lgustore") && ((!channel.equals(ConstProp.NT_AUTH_NAME_JOYBOMB) || !str.endsWith("SdkJoybombPay")) && !channel.equals("google_play_jn") && !channel.equals("xinyou") && !channel.equals("helpshift") && !channel.equals("gpGameSrv") && !channel.equals("swrve") && !channel.equals("nmarket") && !channel.equals("ngadvert") && sdkBase.getPropInt(ConstProp.INNER_MODE_SECOND_CHANNEL, 0) == 0))))) {
                                if (!channel.equals("g_10086") && !channel.equals("netease")) {
                                    if (str2 == null || channel.equals(str2)) {
                                        SdkBase sdkBase3 = c;
                                        if (sdkBase3 == null) {
                                            c = sdkBase;
                                            return;
                                        }
                                        if (!sdkBase3.getChannel().equals("g_10086") && !c.getChannel().equals("netease")) {
                                            Log.e(TAG, "dup common SdkBase, put into loginSdkMap");
                                            b.put(channel, sdkBase);
                                            return;
                                        }
                                        f1792a.put(c.getChannel(), c);
                                        c = sdkBase;
                                        return;
                                    }
                                    return;
                                }
                                if (c == null) {
                                    c = sdkBase;
                                    return;
                                } else {
                                    f1792a.put(channel, sdkBase);
                                    return;
                                }
                            }
                            Log.i(TAG, "tmpChannel=".concat(String.valueOf(channel)));
                            f1792a.put(channel, sdkBase);
                            return;
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
        } catch (InstantiationException e4) {
            e4.printStackTrace();
        } catch (InvocationTargetException e5) {
            e5.printStackTrace();
        }
    }

    private static void a(Context context) {
        ModulesManager.getInst().init(context);
        try {
            System.loadLibrary("ntunisdk");
        } catch (Throwable th) {
            UniSdkUtils.w(TAG, "loadUnisdkSo: " + th.getMessage());
        }
    }

    public static SdkDownload getDLInst() {
        if (d == null) {
            synchronized (SdkDownload.class) {
                if (d == null) {
                    d = new SdkDownload();
                }
            }
        }
        return d;
    }
}