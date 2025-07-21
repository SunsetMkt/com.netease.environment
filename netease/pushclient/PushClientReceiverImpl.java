package com.netease.pushclient;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import com.facebook.common.util.UriUtil;
import com.netease.inner.pushclient.NativePushData;
import com.netease.inner.pushclient.firebase.Firebase;
import com.netease.inner.pushclient.flyme.Flyme;
import com.netease.inner.pushclient.gcm.GCM;
import com.netease.inner.pushclient.honor.Honor;
import com.netease.inner.pushclient.huawei.Huawei;
import com.netease.inner.pushclient.miui.MIUI;
import com.netease.inner.pushclient.oppo.OPPO;
import com.netease.inner.pushclient.vivo.Vivo;
import com.netease.ntunisdk.base.PatchPlaceholder;
import com.netease.push.utils.AppInfo;
import com.netease.push.utils.Crypto;
import com.netease.push.utils.Notifier;
import com.netease.push.utils.NotifyMessageImpl;
import com.netease.push.utils.PushConstantsImpl;
import com.netease.push.utils.PushLog;
import com.netease.push.utils.PushSetting;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.LinkedList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class PushClientReceiverImpl {
    public static final int DEFAULT_NO_REPEAT_INTERVAL = 300;
    private static final String MESSAGE_TYPE_DELETED = "deleted_messages";
    private static final String MESSAGE_TYPE_MESSAGE = "gcm";
    private static final String MESSAGE_TYPE_SEND_ERROR = "send_error";
    private static Object m_callbackObj;
    private boolean m_forceShowMsgOnFront = false;
    private static final String TAG = "NGPush_" + PushClientReceiverImpl.class.getSimpleName();
    private static LinkedList<NotifyMessageImpl> s_messageList = new LinkedList<>();

    private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, PatchPlaceholder.class.getSimpleName());
    }

    public PushClientReceiverImpl() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "PushClientReceiverImpl constructed");
    }

    public void setCallback(Object obj) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "setCallback callbackObj:" + obj);
        m_callbackObj = obj;
    }

    public void setForceShowMsgOnFront(boolean z) {
        Log.i(TAG, "setForceShowMsgOnFront:" + z);
        this.m_forceShowMsgOnFront = z;
    }

    public boolean getForceShowMsgOnFront() {
        return this.m_forceShowMsgOnFront;
    }

    public boolean isBackground(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses()) {
            if (runningAppProcessInfo.processName.equals(context.getPackageName())) {
                PushLog.i(TAG, "check background:" + runningAppProcessInfo.processName + " imp:" + runningAppProcessInfo.importance + " reason:" + runningAppProcessInfo.importanceReasonCode);
                return runningAppProcessInfo.importance > 200;
            }
        }
        PushLog.e(TAG, "check background no process found");
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x0084 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0085 A[RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean canMsgShow(android.content.Context r8) throws java.lang.IllegalAccessException, java.lang.ClassNotFoundException, java.lang.IllegalArgumentException, java.lang.reflect.InvocationTargetException {
        /*
            r7 = this;
            java.lang.String r8 = "appOnFront:"
            java.lang.String r0 = com.netease.pushclient.PushClientReceiverImpl.TAG
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "\u5f00\u59cbm_forceShowMsgOnFront"
            r1.append(r2)
            boolean r2 = r7.m_forceShowMsgOnFront
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            com.netease.push.utils.PushLog.i(r0, r1)
            boolean r0 = r7.m_forceShowMsgOnFront
            r1 = 1
            if (r0 == 0) goto L20
            return r1
        L20:
            r0 = 0
            java.lang.String r2 = "com.netease.pushclient.PushManager"
            java.lang.Class r2 = java.lang.Class.forName(r2)     // Catch: java.lang.Exception -> L4f
            java.lang.String r3 = "isOnFront"
            r4 = 0
            java.lang.reflect.Method r3 = r2.getMethod(r3, r4)     // Catch: java.lang.Exception -> L4f
            java.lang.Object r2 = r3.invoke(r2, r4)     // Catch: java.lang.Exception -> L4f
            java.lang.Boolean r2 = (java.lang.Boolean) r2     // Catch: java.lang.Exception -> L4f
            boolean r2 = r2.booleanValue()     // Catch: java.lang.Exception -> L4f
            java.lang.String r3 = com.netease.pushclient.PushClientReceiverImpl.TAG     // Catch: java.lang.Exception -> L4d
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L4d
            r4.<init>()     // Catch: java.lang.Exception -> L4d
            r4.append(r8)     // Catch: java.lang.Exception -> L4d
            r4.append(r2)     // Catch: java.lang.Exception -> L4d
            java.lang.String r4 = r4.toString()     // Catch: java.lang.Exception -> L4d
            com.netease.push.utils.PushLog.e(r3, r4)     // Catch: java.lang.Exception -> L4d
            goto L6e
        L4d:
            r3 = move-exception
            goto L51
        L4f:
            r3 = move-exception
            r2 = 0
        L51:
            r3.printStackTrace()
            java.lang.String r4 = com.netease.pushclient.PushClientReceiverImpl.TAG
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "init exception:"
            r5.append(r6)
            java.lang.String r3 = r3.getMessage()
            r5.append(r3)
            java.lang.String r3 = r5.toString()
            com.netease.push.utils.PushLog.e(r4, r3)
        L6e:
            java.lang.String r3 = com.netease.pushclient.PushClientReceiverImpl.TAG
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r4.append(r8)
            r4.append(r2)
            java.lang.String r8 = r4.toString()
            com.netease.push.utils.PushLog.e(r3, r8)
            if (r2 == 0) goto L85
            return r0
        L85:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.pushclient.PushClientReceiverImpl.canMsgShow(android.content.Context):boolean");
    }

    public void onReceive(Context context, Intent intent) throws IllegalAccessException, JSONException, NoSuchAlgorithmException, InstantiationException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
        String str;
        int uniqueInteger;
        int i;
        PushLog.e(TAG, "onReceive");
        PushLog.d(TAG, "intent:" + intent);
        String action = intent.getAction();
        PushLog.d(TAG, "action:" + action);
        if (PushConstantsImpl.CLIENT_ACTION_MESSAGE.equals(action)) {
            String stringExtra = intent.getStringExtra("message");
            PushLog.d(TAG, "sMessage:" + stringExtra);
            try {
                _onReceiveNotifyMessage(context, NotifyMessageImpl.readFromJsonString(stringExtra));
                long longExtra = intent.getLongExtra("lasttime", 0L);
                if (longExtra > PushSetting.getReceiveTime(context)) {
                    PushSetting.setReceiveTime(context, longExtra);
                    return;
                }
                return;
            } catch (Exception e) {
                PushLog.e(TAG, "Exception:" + e.getMessage());
                return;
            }
        }
        if (PushConstantsImpl.CLIENT_ACTION_MESSAGE_GCM.equals(action)) {
            Bundle extras = intent.getExtras();
            try {
                Object objInvoke = Class.forName("com.google.android.gms.gcm.GoogleCloudMessaging").getMethod("getInstance", Context.class).invoke(null, context);
                str = (String) objInvoke.getClass().getMethod("getMessageType", Intent.class).invoke(objInvoke, intent);
            } catch (Exception e2) {
                PushLog.e(TAG, "onReceive, GCM reflect error:" + e2.getMessage());
                str = MESSAGE_TYPE_SEND_ERROR;
            }
            PushLog.d(TAG, "messageType=" + str);
            PushLog.d(TAG, "extras=" + extras.toString());
            if (extras.isEmpty() || MESSAGE_TYPE_SEND_ERROR.equals(str) || MESSAGE_TYPE_DELETED.equals(str) || !"gcm".equals(str)) {
                return;
            }
            String string = extras.getString("title", "");
            String string2 = extras.getString("msg", "");
            String string3 = extras.getString("ext", "");
            String string4 = extras.getString("notify_id", "");
            String string5 = extras.getString("reqid", "");
            String string6 = extras.getString(PushConstantsImpl.NOTIFICATION_SOUND, "");
            PushLog.d(TAG, "title=" + string);
            PushLog.d(TAG, "msg=" + string2);
            PushLog.d(TAG, "ext=" + string3);
            PushLog.d(TAG, "strNotifyid=" + string4);
            PushLog.d(TAG, "reqid=" + string5);
            if (TextUtils.isEmpty(string) && TextUtils.isEmpty(string2) && TextUtils.isEmpty(string3)) {
                return;
            }
            if (TextUtils.isEmpty(string4)) {
                i = 0;
            } else {
                try {
                    uniqueInteger = Integer.parseInt(string4);
                } catch (Exception e3) {
                    PushLog.e(TAG, "parseInt exception:" + e3.toString());
                    uniqueInteger = Crypto.getUniqueInteger(string4);
                }
                i = uniqueInteger;
            }
            _onReceiveNotifyMessage(context, new NotifyMessageImpl(string, string2, string3, i, string5, string6, "gcm"));
            return;
        }
        if (PushConstantsImpl.CLIENT_ACTION_REFRESH_DEVID.equals(action)) {
            String devId = PushManagerImpl.getDevId(context);
            PushLog.d(TAG, "devID:" + devId);
            if (TextUtils.isEmpty(devId)) {
                return;
            }
            _onGetNewDevId(context, devId);
            String serviceType = PushManagerImpl.getServiceType(context);
            String registrationID = com.netease.inner.pushclient.PushManager.getInstance().getRegistrationID(context, serviceType);
            PushLog.d(TAG, "PushManagerImpl.setRegid:" + registrationID);
            PushLog.d(TAG, "type:" + serviceType);
            PushSetting.setKeyVaule(context, "type", serviceType);
            PushManagerImpl.setRegid(registrationID, serviceType);
            PushSetting.setFirstStart(context, context.getPackageName(), false);
            return;
        }
        if (PushConstantsImpl.CLIENT_ACTION_CHANNLE_NOTI_CLICK.equals(action)) {
            String stringExtra2 = intent.getStringExtra("message");
            PushLog.d(TAG, "sMessage:" + stringExtra2);
            try {
                _onChannelNotiClickMessage(context, NotifyMessageImpl.readFromJsonString(stringExtra2));
                return;
            } catch (Exception e4) {
                PushLog.e(TAG, "_onChannelNotiClickMessage Exception:" + e4.getMessage());
                return;
            }
        }
        if (PushConstantsImpl.CLIENT_ACTION_METHOD.equals(action)) {
            String stringExtra3 = intent.getStringExtra("method");
            PushLog.d(TAG, "method:" + stringExtra3);
            if ("nativenotify".equals(stringExtra3)) {
                String stringExtra4 = intent.getStringExtra(PushConstantsImpl.INTENT_PUSH_NAME);
                PushLog.d(TAG, "native push id:" + stringExtra4);
                NativePushData nativeNotification = PushSetting.getNativeNotification(context, stringExtra4);
                if (nativeNotification != null) {
                    _onReceiveNotifyMessage(context, nativeNotification.getNotifyMessage());
                    int repeatMode = nativeNotification.getRepeatMode();
                    PushLog.d(TAG, "native push repeat mode:" + repeatMode);
                    if (repeatMode != 0) {
                        nativeNotification.setDelayTriggerSec(360);
                        nativeNotification.startAlarm(context);
                        nativeNotification.setDelayTriggerSec(0);
                        return;
                    }
                    PushSetting.rmNativePushName(context, stringExtra4);
                    return;
                }
                return;
            }
            return;
        }
        if (PushConstantsImpl.CLIENT_ACTION_SAVE_TOKEN.equals(action)) {
            PushLog.d(TAG, "CLIENT_ACTION_SAVE_TOKEN");
            String stringExtra5 = intent.getStringExtra("token");
            String stringExtra6 = intent.getStringExtra("access_key");
            String stringExtra7 = intent.getStringExtra("type");
            PushLog.d(TAG, "token:" + stringExtra5);
            PushLog.d(TAG, "access_key:" + stringExtra6);
            PushLog.d(TAG, "type:" + stringExtra7);
            PushSetting.setKeyVaule(context, "token", stringExtra5);
            PushSetting.setKeyVaule(context, "access_key", stringExtra6);
            PushSetting.setKeyVaule(context, "type", stringExtra7);
            _onGetNewToken(context, stringExtra5);
            PushLog.d(TAG, "sendBroadcast Token to Mpay");
            try {
                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
                Intent intent2 = new Intent(PushConstantsImpl.CLIENT_ACTION_MPAY_MESSAGE);
                intent2.setPackage(context.getPackageName());
                intent2.putExtra("type", "token");
                intent2.putExtra("token", stringExtra5);
                localBroadcastManager.sendBroadcast(intent2);
                return;
            } catch (Exception e5) {
                PushLog.d(TAG, e5.getMessage());
                return;
            }
        }
        if (PushConstantsImpl.CLIENT_ACTION_CALLBACK_TOKEN.equals(action)) {
            PushLog.d(TAG, "CLIENT_ACTION_CALLBACK_TOKEN");
            _onGetNewToken(context, intent.getStringExtra("token"));
            return;
        }
        if (PushConstantsImpl.CLIENT_ACTION_LOGIN_CALLBACK.equals(action)) {
            PushLog.i(TAG, "CLIENT_ACTION_LOGIN_CALLBACK");
            String serviceType2 = PushSetting.getServiceType(context, context.getPackageName());
            PushLog.i(TAG, "serviceType: " + serviceType2);
            if ("gcm".equals(serviceType2)) {
                GCM.getInst().init(context);
            } else if ("miui".equals(serviceType2)) {
                MIUI.getInst().init(context);
            } else if ("huawei".equals(serviceType2) || "hms".equals(serviceType2)) {
                Huawei.getInst().init(context);
            } else if ("flyme".equals(serviceType2)) {
                Flyme.getInst().init(context);
            } else if ("fcm".equals(serviceType2)) {
                Firebase.getInst().init(context);
            } else if ("oppo".equals(serviceType2)) {
                OPPO.getInst().init(context);
            } else if ("vivo".equals(serviceType2)) {
                Vivo.getInst().init(context);
            } else if (PushConstantsImpl.HONOR.equals(serviceType2)) {
                Honor.getInst().init(context);
            }
            String stringExtra8 = intent.getStringExtra("resultDataJson");
            PushLog.d(TAG, "resultDataJson:" + stringExtra8);
            try {
                JSONObject jSONObject = new JSONObject(stringExtra8);
                int iOptInt = jSONObject.optInt("errorCode");
                if (iOptInt == 0) {
                    String vaule = PushSetting.getVaule(context, "token");
                    PushLog.d(TAG, "Call back Token:" + vaule);
                    jSONObject.put("token", vaule);
                    onLoginRequestSuccess(jSONObject.toString());
                } else {
                    onLoginRequestFailed(stringExtra8, iOptInt, jSONObject.optString("msg"));
                }
            } catch (JSONException e6) {
                e6.printStackTrace();
                onLoginRequestFailed(stringExtra8, 10000, "JSONException");
            }
        }
    }

    private static void onLoginRequestSuccess(String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "onLoginRequestSuccess");
        PushManagerImpl.nieLoginSuccess = true;
        try {
            Class.forName("com.netease.pushclient.PushManager").getMethod("onCallbackSuccess", String.class).invoke(null, str);
        } catch (Exception e) {
            PushLog.e(TAG, e.toString());
        }
    }

    private static void onLoginRequestFailed(String str, int i, String str2) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushManagerImpl.nieLoginSuccess = false;
        PushLog.i(TAG, "onLoginRequestSuccess");
        try {
            Class.forName("com.netease.pushclient.PushManager").getMethod("onCallbackFailed", String.class, Integer.TYPE, String.class).invoke(null, str, Integer.valueOf(i), str2);
        } catch (Exception e) {
            PushLog.e(TAG, e.toString());
        }
    }

    public void _onGetNewDevId(Context context, String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "_onGetNewDevId:" + context + ", devid:" + str);
        try {
            Class.forName("com.netease.pushclient.PushClientReceiver").getMethod("onGetNewDevId", Context.class, String.class).invoke(m_callbackObj, context, str);
        } catch (Exception e) {
            PushLog.e(TAG, e.toString());
        }
    }

    public void _onGetNewToken(Context context, String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "_onGetNewToken:" + context + ", token:" + str);
        try {
            PushManagerImpl.nieGetNewTokenSuccess = true;
            Class.forName("com.netease.pushclient.PushClientReceiver").getMethod("onGetNewToken", Context.class, String.class).invoke(m_callbackObj, context, str);
        } catch (Exception e) {
            PushLog.e(TAG, e.toString());
        }
    }

    public static void _onFunctionCallBack(String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "_onFunctionCallBack:" + str);
        try {
            Class.forName("com.netease.pushclient.PushClientReceiver").getMethod("onFunctionCallBack", String.class).invoke(m_callbackObj, str);
        } catch (Exception e) {
            PushLog.e(TAG, e.toString());
        }
    }

    public void onFunctionCallBack(String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "onFunctionCallBack:" + str);
    }

    public void onGetNewToken(Context context, String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "onGetNewToken:" + context + ", devid:" + str);
    }

    public void onGetNewDevId(Context context, String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "onGetNewDevId:" + context + ", devid:" + str);
    }

    public void _onReceiveNotifyMessage(Context context, NotifyMessageImpl notifyMessageImpl) throws IllegalAccessException, InstantiationException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "_onReceiveNotifyMessage:" + notifyMessageImpl);
        if (filterMessage(notifyMessageImpl)) {
            return;
        }
        try {
            Class<?> cls = Class.forName("com.netease.push.utils.NotifyMessage");
            Object objNewInstance = cls.newInstance();
            cls.getMethod("setImpl", Object.class).invoke(objNewInstance, notifyMessageImpl);
            Class.forName("com.netease.pushclient.PushClientReceiver").getMethod("onReceiveNotifyMessage", Context.class, cls).invoke(m_callbackObj, context, objNewInstance);
        } catch (Exception e) {
            PushLog.e(TAG, e.toString());
        }
    }

    public void _onChannelNotiClickMessage(Context context, NotifyMessageImpl notifyMessageImpl) throws IllegalAccessException, InstantiationException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "_onChannelNotiClickMessage:" + notifyMessageImpl);
        try {
            Class<?> cls = Class.forName("com.netease.push.utils.NotifyMessage");
            Object objNewInstance = cls.newInstance();
            cls.getMethod("setImpl", Object.class).invoke(objNewInstance, notifyMessageImpl);
            Class.forName("com.netease.pushclient.PushClientReceiver").getMethod("onChannelNotiClickMessage", Context.class, cls).invoke(m_callbackObj, context, objNewInstance);
        } catch (Exception e) {
            PushLog.e(TAG, e.toString());
        }
    }

    public void onReceiveNotifyMessage(Context context, Object obj) throws IllegalAccessException, JSONException, PackageManager.NameNotFoundException, InstantiationException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
        NotifyMessageImpl notifyMessageImpl = (NotifyMessageImpl) obj;
        String packageName = context.getPackageName();
        PushLog.i(TAG, "onReceiveNotifyMessage:" + notifyMessageImpl);
        PushLog.d(TAG, "packagename:" + packageName);
        String serviceType = notifyMessageImpl.getServiceType();
        PushLog.d(TAG, "service type:" + serviceType);
        if (!"niepush".equals(serviceType)) {
            PushLog.d(TAG, "not niepush return!");
            return;
        }
        Boolean bool = true;
        if (!"".equals(notifyMessageImpl.getReqid())) {
            JSONObject jSONObject = new JSONObject();
            new JSONObject();
            JSONArray jSONArray = new JSONArray();
            try {
                ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
                if (applicationInfo != null && applicationInfo.metaData != null) {
                    applicationInfo.metaData.getString("ngpush_project");
                }
                jSONObject.put("event_type", "show");
                jSONObject.put("plan_id", notifyMessageImpl.getPlan_id());
                jSONObject.put("push_id", notifyMessageImpl.getPush_id());
                jSONObject.put("receive_channel", "niepush");
                jSONObject.put("account", PushManagerImpl.subscriber);
            } catch (Exception e) {
                e.printStackTrace();
            }
            UploadUtil.SendRequest(jSONObject, context);
            _onShowNotifyMessage(context, notifyMessageImpl);
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObjectOptJSONObject = jSONArray.optJSONObject(i);
                if (jSONObjectOptJSONObject.optInt("code") == 1002) {
                    PushLog.d(TAG, "remove Notification!!!");
                    ((NotificationManager) context.getSystemService("notification")).cancelAll();
                } else if (jSONObjectOptJSONObject.optInt("code") == 1001) {
                    PushLog.d(TAG, "remove NativePush!!!");
                    NativePushManagerImpl.removeAllAlarms();
                }
            }
            if (notifyMessageImpl.fromMpay) {
                PushLog.d(TAG, "sendBroadcast to Mpay");
                try {
                    LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
                    Intent intent = new Intent(PushConstantsImpl.CLIENT_ACTION_MPAY_MESSAGE);
                    intent.setPackage(packageName);
                    intent.putExtra("type", UriUtil.LOCAL_CONTENT_SCHEME);
                    intent.putExtra("customContent", notifyMessageImpl.getPassJsonString());
                    localBroadcastManager.sendBroadcast(intent);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        if (notifyMessageImpl.isSilent()) {
            bool = false;
        }
        if (bool.booleanValue()) {
            String serviceType2 = PushSetting.getServiceType(context, packageName);
            PushLog.d(TAG, "service type:" + serviceType2);
            if (notifyMessageImpl.mTitle.length() == 0 && notifyMessageImpl.mMsg.length() == 0) {
                PushLog.e(TAG, "discard empty message");
                return;
            }
            PushLog.d(TAG, "canMsgShow");
            if (!canMsgShow(context)) {
                PushLog.w(TAG, "discard frontend message");
                return;
            }
            AppInfo appInfo = PushSetting.getAppInfo(context, packageName);
            if (appInfo == null) {
                appInfo = new AppInfo(packageName);
            }
            new Notifier(context).notify(notifyMessageImpl, appInfo);
        }
    }

    public void _onShowNotifyMessage(Context context, NotifyMessageImpl notifyMessageImpl) throws IllegalAccessException, InstantiationException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "_onShowNotifyMessage:" + notifyMessageImpl);
        if (filterMessage(notifyMessageImpl)) {
            return;
        }
        try {
            Class<?> cls = Class.forName("com.netease.push.utils.NotifyMessage");
            Object objNewInstance = cls.newInstance();
            cls.getMethod("setImpl", Object.class).invoke(objNewInstance, notifyMessageImpl);
            Class.forName("com.netease.pushclient.PushClientReceiver").getMethod("onShowNotifyMessage", Context.class, cls).invoke(m_callbackObj, context, objNewInstance);
        } catch (Exception e) {
            PushLog.e(TAG, e.toString());
        }
    }

    public static boolean filterMessage(NotifyMessageImpl notifyMessageImpl) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        boolean z;
        notifyMessageImpl.mTime = System.currentTimeMillis() / 1000;
        long j = notifyMessageImpl.mTime - 300;
        Iterator<NotifyMessageImpl> it = s_messageList.iterator();
        int i = 0;
        while (true) {
            if (!it.hasNext()) {
                z = false;
                break;
            }
            NotifyMessageImpl next = it.next();
            if (next.mTime >= j) {
                if (!TextUtils.isEmpty(notifyMessageImpl.mReqid) && !TextUtils.isEmpty(notifyMessageImpl.mServiceType) && notifyMessageImpl.mReqid.equals(next.mReqid) && !notifyMessageImpl.mServiceType.equals(next.mServiceType)) {
                    PushLog.w(TAG, "same message in different service type to be filtered:");
                    PushLog.w(TAG, "old:" + next);
                    PushLog.w(TAG, "new:" + notifyMessageImpl);
                    z = true;
                    break;
                }
            } else {
                i++;
            }
        }
        if (!z) {
            s_messageList.add(notifyMessageImpl);
        }
        for (int i2 = 0; i2 < i; i2++) {
            s_messageList.removeFirst();
        }
        return z;
    }
}