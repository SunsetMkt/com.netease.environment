package com.netease.pushclient;

import android.content.Context;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import com.netease.push.utils.PushLog;
import com.netease.push.utils.PushSetting;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class UploadUtil {
    private static final String TAG = "UniSDK UploadUtil";
    private static String access_key = "";
    private static String requestBody = null;
    private static String token = "";
    private static String url = "https://sigma-statistics-push.proxima.nie.netease.com";

    public static String SendRequest(JSONObject jSONObject, Context context) {
        PushLog.i(TAG, "SendRequest");
        PushLog.d(TAG, "url=" + PushManagerImpl.getSdkgate(context));
        try {
            new Thread(new Runnable() { // from class: com.netease.pushclient.UploadUtil.1
                final /* synthetic */ Context val$ctx;
                final /* synthetic */ JSONObject val$kvPairs;

                AnonymousClass1(Context context2, JSONObject jSONObject2) {
                    context = context2;
                    jSONObject = jSONObject2;
                }

                @Override // java.lang.Runnable
                public void run() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                    try {
                        String unused = UploadUtil.url = PushManagerImpl.getSdkgate(context);
                        String unused2 = UploadUtil.requestBody = jSONObject.toString();
                        String unused3 = UploadUtil.token = PushSetting.getVaule(context, "token");
                        String unused4 = UploadUtil.access_key = PushSetting.getVaule(context, "access_key");
                        PushLog.d(UploadUtil.TAG, "requestBody=" + UploadUtil.requestBody);
                        HashMap map = new HashMap();
                        map.put("Content-Type", ClientLogConstant.NORMAL_TYPE_VALUE);
                        map.put("token", UploadUtil.token);
                        map.put("access_key", UploadUtil.access_key);
                        PushLog.d(UploadUtil.TAG, "content=" + NetUtil.doHttp(UploadUtil.url + "/statistic/notification_event", "POST", UploadUtil.requestBody, null, map));
                    } catch (Exception e) {
                        PushLog.d(UploadUtil.TAG, "err=" + e);
                        e.printStackTrace();
                    }
                }
            }).start();
            return "";
        } catch (Exception e) {
            PushLog.d(TAG, "err=" + e);
            e.printStackTrace();
            return "";
        }
    }

    /* renamed from: com.netease.pushclient.UploadUtil$1 */
    class AnonymousClass1 implements Runnable {
        final /* synthetic */ Context val$ctx;
        final /* synthetic */ JSONObject val$kvPairs;

        AnonymousClass1(Context context2, JSONObject jSONObject2) {
            context = context2;
            jSONObject = jSONObject2;
        }

        @Override // java.lang.Runnable
        public void run() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            try {
                String unused = UploadUtil.url = PushManagerImpl.getSdkgate(context);
                String unused2 = UploadUtil.requestBody = jSONObject.toString();
                String unused3 = UploadUtil.token = PushSetting.getVaule(context, "token");
                String unused4 = UploadUtil.access_key = PushSetting.getVaule(context, "access_key");
                PushLog.d(UploadUtil.TAG, "requestBody=" + UploadUtil.requestBody);
                HashMap map = new HashMap();
                map.put("Content-Type", ClientLogConstant.NORMAL_TYPE_VALUE);
                map.put("token", UploadUtil.token);
                map.put("access_key", UploadUtil.access_key);
                PushLog.d(UploadUtil.TAG, "content=" + NetUtil.doHttp(UploadUtil.url + "/statistic/notification_event", "POST", UploadUtil.requestBody, null, map));
            } catch (Exception e) {
                PushLog.d(UploadUtil.TAG, "err=" + e);
                e.printStackTrace();
            }
        }
    }

    public static String SendRequest(JSONObject jSONObject) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "SendRequest");
        requestBody = jSONObject.toString();
        new Thread(new Runnable() { // from class: com.netease.pushclient.UploadUtil.2
            AnonymousClass2() {
            }

            @Override // java.lang.Runnable
            public void run() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                try {
                    PushLog.d(UploadUtil.TAG, "requestBody=" + UploadUtil.requestBody);
                    HashMap map = new HashMap();
                    map.put("Content-Type", ClientLogConstant.NORMAL_TYPE_VALUE);
                    map.put("token", UploadUtil.token);
                    map.put("access_key", UploadUtil.access_key);
                    PushLog.d(UploadUtil.TAG, "content=" + NetUtil.doHttp(UploadUtil.url + "/statistic/notification_event", "POST", UploadUtil.requestBody, null, map));
                } catch (Exception e) {
                    PushLog.d(UploadUtil.TAG, "err=" + e);
                    e.printStackTrace();
                }
            }
        }).start();
        return "";
    }

    /* renamed from: com.netease.pushclient.UploadUtil$2 */
    class AnonymousClass2 implements Runnable {
        AnonymousClass2() {
        }

        @Override // java.lang.Runnable
        public void run() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            try {
                PushLog.d(UploadUtil.TAG, "requestBody=" + UploadUtil.requestBody);
                HashMap map = new HashMap();
                map.put("Content-Type", ClientLogConstant.NORMAL_TYPE_VALUE);
                map.put("token", UploadUtil.token);
                map.put("access_key", UploadUtil.access_key);
                PushLog.d(UploadUtil.TAG, "content=" + NetUtil.doHttp(UploadUtil.url + "/statistic/notification_event", "POST", UploadUtil.requestBody, null, map));
            } catch (Exception e) {
                PushLog.d(UploadUtil.TAG, "err=" + e);
                e.printStackTrace();
            }
        }
    }

    public static void setUrl(String str) {
        url = str;
    }
}