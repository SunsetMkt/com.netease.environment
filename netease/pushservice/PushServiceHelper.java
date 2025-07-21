package com.netease.pushservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;
import com.netease.inner.pushclient.NativePushData;
import com.netease.inner.pushclient.PushClientReceiver;
import com.netease.ntunisdk.base.PatchPlaceholder;
import com.netease.ntunisdk.modules.api.ModulesManager;
import com.netease.push.proto.ProtoClientWrapper;
import com.netease.push.proto.nano.ClientSdkgate;
import com.netease.push.utils.AppInfo;
import com.netease.push.utils.Notifier;
import com.netease.push.utils.NotifyMessageImpl;
import com.netease.push.utils.PushConstantsImpl;
import com.netease.push.utils.PushLog;
import com.netease.push.utils.PushSetting;
import com.netease.pushclient.PushManagerImpl;
import com.netease.pushclient.UnisdkDeviceUtil;
import com.netease.pushclient.UploadUtil;
import com.netease.rnccplayer.VideoViewManager;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes4.dex */
public class PushServiceHelper {
    private TaskSubmitter m_taskSubmitter;
    private static final String TAG = "NGPush_" + PushServiceHelper.class.getSimpleName();
    private static PushServiceHelper s_pushServiceHelper = new PushServiceHelper();
    public static boolean regetToken = false;
    private PushServiceInfo m_serviceInfo = new PushServiceInfo();
    private Network m_network = null;
    private PushService m_pushService = null;
    private long m_recvTimeError = 60;
    private boolean stopSend2gameIdFlag = false;
    public boolean mIsSupportedBade = true;

    private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, PatchPlaceholder.class.getSimpleName());
    }

    public static PushServiceHelper getInstance() {
        return s_pushServiceHelper;
    }

    public PushService getPushService() {
        return this.m_pushService;
    }

    public boolean init(PushService pushService) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "init");
        PushLog.i(TAG, "pushService:" + pushService);
        if (pushService == null) {
            return false;
        }
        PushLog.i(TAG, "ModulesManager init:");
        try {
            ModulesManager.getInst().init(pushService);
            ModulesManager.getInst().onDestroy();
        } catch (Throwable th) {
            th.printStackTrace();
        }
        this.m_taskSubmitter = new TaskSubmitter();
        this.m_network = Network.getInst();
        this.m_pushService = pushService;
        this.m_serviceInfo.mDevId = PushSetting.getDevId(this.m_pushService);
        PushService pushService2 = this.m_pushService;
        String serviceType = PushSetting.getServiceType(pushService2, pushService2.getPackageName());
        String registrationID = PushSetting.getRegistrationID(this.m_pushService, "gcm");
        String registrationID2 = PushSetting.getRegistrationID(this.m_pushService, "miui");
        String registrationID3 = PushSetting.getRegistrationID(this.m_pushService, "huawei");
        String registrationID4 = PushSetting.getRegistrationID(this.m_pushService, "hms");
        String registrationID5 = PushSetting.getRegistrationID(this.m_pushService, "flyme");
        PushLog.e(TAG, "serviceType=" + serviceType);
        PushLog.e(TAG, "regid_niepush:" + this.m_serviceInfo.mDevId);
        PushLog.e(TAG, "regid_gcm:" + registrationID);
        PushLog.e(TAG, "regid_miui:" + registrationID2);
        PushLog.e(TAG, "regid_huawei:" + registrationID3);
        PushLog.e(TAG, "regid_huawei_hms:" + registrationID4);
        PushLog.e(TAG, "regid_flyme:" + registrationID5);
        String packageName = this.m_pushService.getPackageName();
        PushLog.d(TAG, "contextpkg:" + packageName);
        PushLog.d(TAG, "contextver:" + PushConstantsImpl.JAR_VER_CODE);
        List<NativePushData> allOtherNativeNotifications = PushSetting.getAllOtherNativeNotifications(this.m_pushService, packageName);
        if (allOtherNativeNotifications != null) {
            for (NativePushData nativePushData : allOtherNativeNotifications) {
                PushLog.d(TAG, "startAlarm pushName:" + nativePushData.getPushName());
                nativePushData.startAlarm(this.m_pushService, packageName);
            }
        }
        connect(false);
        return true;
    }

    public void onReceive(ClientSdkgate.PreRegisterResponse preRegisterResponse) throws PackageManager.NameNotFoundException {
        if (preRegisterResponse != null) {
            try {
                PushLog.i(TAG, "preRegisterResponse:" + preRegisterResponse.getAuth());
                String serviceType = PushManagerImpl.getServiceType(this.m_pushService);
                PushLog.d(TAG, "type:" + serviceType);
                PushLog.d(TAG, "packageName:" + this.m_pushService.getPackageName());
                PackageInfo packageInfo = this.m_pushService.getPackageManager().getPackageInfo(this.m_pushService.getPackageName(), 0);
                String str = packageInfo.versionName + "(" + packageInfo.versionCode + ")";
                PushLog.d(TAG, "version:" + str);
                ClientSdkgate.RegisterRequest registerRequestBuild = ClientSdkgate.RegisterRequest.newBuilder().setAuth(preRegisterResponse.getAuth()).setChannel(serviceType).setAppVersion(str).setSdkVersion(PushConstantsImpl.SDK_VERSION).setSystemVersion(String.valueOf(Build.VERSION.SDK_INT)).setTimeZone(UnisdkDeviceUtil.getAreaZone()).setDeviceBrand(Build.BRAND).setDeviceModel(Build.MODEL).setPkg(this.m_pushService.getPackageName()).setRegid("").build();
                PushLog.d(TAG, "RegisterRequest");
                getNetwork().sendSdkgateData(registerRequestBuild.toByteArray(), 1, 2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onReceive(ClientSdkgate.RegisterResponse registerResponse) throws PackageManager.NameNotFoundException {
        if (registerResponse != null) {
            try {
                PushLog.i(TAG, "registerResponse:" + registerResponse.getToken());
                String token = registerResponse.getToken();
                String accessKey = registerResponse.getAccessKey();
                PushLog.d(TAG, "token:" + token);
                PushLog.d(TAG, "access_key:" + accessKey);
                PushLog.d(TAG, "isNotificationEnabled:" + PushManagerImpl.checkNotifySettingContext(this.m_pushService));
                PackageInfo packageInfo = this.m_pushService.getPackageManager().getPackageInfo(this.m_pushService.getPackageName(), 0);
                String str = packageInfo.versionName + "(" + packageInfo.versionCode + ")";
                PushLog.d(TAG, "version:" + str);
                broadcastToken(this.m_pushService, this.m_pushService.getPackageName(), token, accessKey, PushManagerImpl.getServiceType(this.m_pushService));
                ClientSdkgate.LoginRequest loginRequestBuild = ClientSdkgate.LoginRequest.newBuilder().setToken(token).setAccessKey(accessKey).setPermitNotice(PushManagerImpl.checkNotifySettingContext(this.m_pushService)).setAppVersion(str).setSdkVersion(PushConstantsImpl.SDK_VERSION).setSystemVersion(String.valueOf(Build.VERSION.SDK_INT)).setTimeZone(UnisdkDeviceUtil.getAreaZone()).setDeviceBrand(Build.BRAND).setDeviceModel(Build.MODEL).build();
                PushLog.d(TAG, "\u5f00\u59cbsendData");
                getNetwork().sendSdkgateData(loginRequestBuild.toByteArray(), 2, 1);
                PushLog.i(TAG, "findOfflineNotificationRequest");
                getNetwork().sendSdkgateData(ClientSdkgate.FindOfflineNotificationRequest.newBuilder().build().toByteArray(), 10, 3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setBadgeNum(Context context, int i) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "hms setBadgeNum:" + i);
        try {
            ActivityInfo[] activityInfoArr = context.getPackageManager().getPackageInfo(context.getPackageName(), 1).activities;
            PushLog.i(TAG, "Mainclass:" + activityInfoArr[0].name);
            Bundle bundle = new Bundle();
            bundle.putString("package", context.getPackageName());
            bundle.putString("class", activityInfoArr[0].name);
            bundle.putInt("badgenumber", i);
            context.getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", (String) null, bundle);
        } catch (Exception unused) {
            this.mIsSupportedBade = false;
        }
    }

    public void onReceive(ClientSdkgate.Notification notification) throws IllegalAccessException, JSONException, IllegalArgumentException, InvocationTargetException {
        String str;
        PushLog.i(TAG, "onReceive");
        new HashMap();
        PushLog.i(TAG, "ActionType:" + notification.getAndroid().getClickActionType());
        PushLog.i(TAG, "ActionParam:" + notification.getAndroid().getClickActionParam());
        PushLog.i(TAG, "got a message");
        PushLog.d(TAG, "title:" + notification.getTitle());
        PushLog.d(TAG, "content:" + notification.getContent());
        PushLog.d(TAG, "channelId:" + notification.getAndroid().getChannel().getChannelId());
        PushLog.d(TAG, "channelName:" + notification.getAndroid().getChannel().getChannelName());
        PushLog.d(TAG, "groupId:" + notification.getAndroid().getChannel().getChannelGroupId());
        PushLog.d(TAG, "groupName:" + notification.getAndroid().getChannel().getChannelGroupName());
        PushLog.d(TAG, "silent:" + notification.getSilent());
        String packageName = this.m_pushService.getPackageName();
        PushLog.d(TAG, "message.packagename:" + this.m_pushService.getPackageName());
        NotifyMessageImpl notifyMessageImpl = new NotifyMessageImpl(notification.getTitle(), notification.getContent(), "", notification.getAndroid().getNotifyId(), notification.getSystemContent().getPushId(), notification.getAndroid().getSoundResource(), "niepush", notification.getAndroid().getCustomContent(), notification.getSilent(), notification.getSystemContent().getPushId(), notification.getSystemContent().getPlanId());
        if (!TextUtils.isEmpty(notification.getAndroid().getChannel().getChannelId())) {
            notifyMessageImpl.setChannelId(notification.getAndroid().getChannel().getChannelId());
            notifyMessageImpl.setGroupId(notification.getAndroid().getChannel().getChannelGroupId());
            notifyMessageImpl.setChannelName(notification.getAndroid().getChannel().getChannelName());
            notifyMessageImpl.setGroupName(notification.getAndroid().getChannel().getChannelGroupName());
        }
        notifyMessageImpl.setActionType(notification.getAndroid().getClickActionType());
        notifyMessageImpl.setActionParam(notification.getAndroid().getClickActionParam());
        PushLog.d(TAG, "BigImageUrl:" + notification.getAndroid().getBigImageUrl());
        PushLog.d(TAG, "push_id:" + notification.getSystemContent().getPushId());
        PushLog.d(TAG, "plan_id:" + notification.getSystemContent().getPlanId());
        PushLog.d(TAG, "fromMpay:" + notification.getSystemContent().getFromMpay());
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("event_type", "receive");
            jSONObject.put("plan_id", notifyMessageImpl.getPlan_id());
            jSONObject.put("push_id", notifyMessageImpl.getPush_id());
            jSONObject.put("receive_channel", "niepush");
            jSONObject.put("account", PushSetting.getVaule(this.m_pushService, "account"));
            UploadUtil.SendRequest(jSONObject, this.m_pushService);
        } catch (Exception e) {
            e.printStackTrace();
        }
        notifyMessageImpl.fromMpay = notification.getSystemContent().getFromMpay();
        if (!TextUtils.isEmpty(notification.getAndroid().getBigImageUrl())) {
            notifyMessageImpl.setBig_image_url(notification.getAndroid().getBigImageUrl());
        }
        if (!TextUtils.isEmpty(notification.getAndroid().getSmallImageUrl())) {
            notifyMessageImpl.setSmall_image_url(notification.getAndroid().getSmallImageUrl());
        }
        try {
            String strWriteToJsonString = notifyMessageImpl.writeToJsonString();
            Intent intentCreateMessageIntent = PushClientReceiver.createMessageIntent();
            intentCreateMessageIntent.putExtra("message", strWriteToJsonString);
            PushLog.d(TAG, "service packageName:" + this.m_pushService.getPackageName());
            Iterator<ResolveInfo> it = this.m_pushService.getPackageManager().queryBroadcastReceivers(new Intent(PushConstantsImpl.CLIENT_ACTION_MESSAGE), 0).iterator();
            while (true) {
                if (!it.hasNext()) {
                    str = "";
                    break;
                }
                ResolveInfo next = it.next();
                String str2 = next.activityInfo.packageName;
                if (packageName.equalsIgnoreCase(str2)) {
                    str = next.activityInfo.name;
                    if (str.startsWith(".")) {
                        str = str2 + str;
                    }
                    PushLog.d(TAG, "sendBroadcast receiverName:" + str);
                }
            }
            if (!TextUtils.isEmpty(str)) {
                intentCreateMessageIntent.setComponent(new ComponentName(packageName, str));
            } else {
                intentCreateMessageIntent.setPackage(packageName);
            }
            PushLog.d(TAG, "handlePush, sendBroadcast");
            this.m_pushService.sendBroadcast(intentCreateMessageIntent, this.m_pushService.getPackageName() + ".permission.ngpush");
            int badge = (int) notification.getAndroid().getBadge();
            if (!this.mIsSupportedBade || badge <= 0) {
                return;
            }
            setBadgeNum(this.m_pushService, badge);
        } catch (Exception e2) {
            PushLog.e(TAG, "writeToJsonString exception:" + e2.getMessage());
        }
    }

    public void onReceive(ProtoClientWrapper.Packet packet) throws IllegalAccessException, JSONException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "onReceive");
        PushLog.d(TAG, "packet:" + packet);
        PushLog.e(TAG, "got cmd:" + ProtoClientWrapper.getTypeName(packet.type));
        if (50 == packet.type) {
            PushLog.e(TAG, "PUSH_TYPE from server");
            try {
                handlePush(packet);
                return;
            } catch (Exception e) {
                PushLog.d(TAG, "handlePush exception");
                e.printStackTrace();
                return;
            }
        }
        if (52 == packet.type) {
            PushLog.e(TAG, "NEW_ID_TYPE from server");
            try {
                onGetNewID(ProtoClientWrapper.NewIdInfo.UnmarshalNewIdInfo(packet.data).id);
                return;
            } catch (Exception e2) {
                PushLog.d(TAG, "onGetNewID exception:" + e2.getMessage());
                return;
            }
        }
        if (51 == packet.type) {
            PushLog.e(TAG, "RESET_TYPE from server");
            this.m_serviceInfo.resetUUID();
            setNewID();
            return;
        }
        PushLog.e(TAG, "error cmd");
    }

    /* JADX WARN: Removed duplicated region for block: B:72:0x0213  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void onConnectSuccess() throws java.lang.IllegalAccessException, android.content.pm.PackageManager.NameNotFoundException, java.lang.IllegalArgumentException, java.lang.reflect.InvocationTargetException {
        /*
            Method dump skipped, instructions count: 810
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.pushservice.PushServiceHelper.onConnectSuccess():void");
    }

    private void setNewID() throws IllegalAccessException, JSONException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "setNewID");
        String str = Build.MODEL;
        Display defaultDisplay = ((WindowManager) this.m_pushService.getSystemService("window")).getDefaultDisplay();
        int width = defaultDisplay.getWidth();
        int height = defaultDisplay.getHeight();
        String str2 = Build.VERSION.RELEASE + "_" + Build.VERSION.SDK;
        String macAddress = UnisdkDeviceUtil.getMacAddress(this.m_pushService);
        ProtoClientWrapper.DevInfo devInfo = new ProtoClientWrapper.DevInfo();
        devInfo.model = str;
        devInfo.screen = String.format("%d*%d", Integer.valueOf(width), Integer.valueOf(height));
        devInfo.os = "android";
        devInfo.osver = str2;
        devInfo.mac = macAddress;
        devInfo.id = this.m_serviceInfo.createUUID(this.m_pushService);
        PushLog.e(TAG, "sendData, SET_NEW_ID_TYPE");
        PushLog.d(TAG, "model:" + devInfo.model);
        PushLog.d(TAG, "screen" + devInfo.screen);
        PushLog.d(TAG, "os:" + devInfo.os);
        PushLog.d(TAG, "osver:" + devInfo.osver);
        PushLog.d(TAG, "mac:" + devInfo.mac);
        PushLog.d(TAG, "id:" + devInfo.id);
        getNetwork().sendData((byte) 2, devInfo);
    }

    private void onGetNewID(String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.e(TAG, "onGetNewID:" + str);
        PushLog.d(TAG, "PushManager.getContext():" + PushManagerImpl.getContext());
        PushServiceInfo pushServiceInfo = this.m_serviceInfo;
        pushServiceInfo.mDevId = str;
        if (TextUtils.isEmpty(pushServiceInfo.mDevId) || this.stopSend2gameIdFlag) {
            return;
        }
        PushSetting.setDevId(this.m_pushService, this.m_serviceInfo.mDevId);
        if (PushManagerImpl.getContext() != null) {
            PushSetting.setDevId(PushManagerImpl.getContext(), this.m_serviceInfo.mDevId);
        }
        this.stopSend2gameIdFlag = true;
        checkFirstStart(PushSetting.getAppInfo(this.m_pushService));
        login();
    }

    public void login() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "login");
        ProtoClientWrapper.DevServiceInfos devServiceInfos = new ProtoClientWrapper.DevServiceInfos();
        devServiceInfos.id = this.m_serviceInfo.mDevId;
        devServiceInfos.ver = "342";
        devServiceInfos.key = "";
        AppInfo appInfo = PushSetting.getAppInfo(this.m_pushService);
        ProtoClientWrapper.ServiceInfo serviceInfo = new ProtoClientWrapper.ServiceInfo();
        serviceInfo.service = appInfo.mPackageName;
        serviceInfo.time = appInfo.mLastReceiveTime;
        devServiceInfos.serviceInfos = new ProtoClientWrapper.ServiceInfo[]{serviceInfo};
        PushLog.e(TAG, "sendData, LOGIN_TYPE");
        getNetwork().sendData((byte) 4, devServiceInfos);
    }

    public void register(String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "register");
        PushLog.d(TAG, "packageName:" + str);
        PushLog.d(TAG, "m_serviceInfo.mDevId:" + this.m_serviceInfo.mDevId);
        if (TextUtils.isEmpty(str)) {
            return;
        }
        List<NativePushData> allOtherNativeNotifications = PushSetting.getAllOtherNativeNotifications(this.m_pushService, str);
        if (allOtherNativeNotifications != null) {
            Iterator<NativePushData> it = allOtherNativeNotifications.iterator();
            while (it.hasNext()) {
                it.next().startAlarm(this.m_pushService, str);
            }
        }
        if (TextUtils.isEmpty(this.m_serviceInfo.mDevId)) {
            return;
        }
        ProtoClientWrapper.DevServiceInfo devServiceInfo = new ProtoClientWrapper.DevServiceInfo();
        devServiceInfo.id = this.m_serviceInfo.mDevId;
        devServiceInfo.service = str;
        devServiceInfo.time = 0L;
        PushLog.e(TAG, "sendData, REGISTER_TYPE");
        getNetwork().sendData((byte) 6, devServiceInfo);
    }

    public void unRegister(String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "unRegister");
        PushLog.d(TAG, "packageName:" + str);
        PushLog.d(TAG, "pid:" + Process.myPid());
        AppInfo appInfo = PushSetting.getAppInfo(this.m_pushService);
        if (appInfo != null) {
            ProtoClientWrapper.DevServiceInfo devServiceInfo = new ProtoClientWrapper.DevServiceInfo();
            devServiceInfo.id = this.m_serviceInfo.mDevId;
            devServiceInfo.service = appInfo.mPackageName;
            devServiceInfo.time = appInfo.mLastReceiveTime;
            PushLog.e(TAG, "sendData, UNREGISTER_TYPE");
            getNetwork().sendData((byte) 7, devServiceInfo);
            return;
        }
        PushLog.d(TAG, "appinfo is null");
    }

    public void handlePush(ProtoClientWrapper.Packet packet) throws IllegalAccessException, JSONException, PackageManager.NameNotFoundException, IllegalArgumentException, InvocationTargetException {
        int i;
        String str;
        PushLog.i(TAG, "handlePush");
        HashMap map = new HashMap();
        try {
            ProtoClientWrapper.MessageInfo messageInfoUnmarshalMessageInfo = ProtoClientWrapper.MessageInfo.unmarshalMessageInfo(packet.data);
            if (!this.m_serviceInfo.mDevId.equals(messageInfoUnmarshalMessageInfo.id)) {
                PushLog.d(TAG, "deviceID mismatch:");
                PushLog.d(TAG, "got deviceID:" + messageInfoUnmarshalMessageInfo.id);
                PushLog.d(TAG, " my deviceID:" + this.m_serviceInfo.mDevId);
                return;
            }
            ProtoClientWrapper.Message[] messageArr = messageInfoUnmarshalMessageInfo.messages;
            int length = messageArr.length;
            for (int i2 = 0; i2 < length; i2 = i + 1) {
                ProtoClientWrapper.Message message = messageArr[i2];
                JSONObject jSONObject = new JSONObject();
                try {
                    ApplicationInfo applicationInfo = this.m_pushService.getPackageManager().getApplicationInfo(this.m_pushService.getPackageName(), 128);
                    String string = (applicationInfo == null || applicationInfo.metaData == null) ? "" : applicationInfo.metaData.getString("ngpush_project");
                    JSONObject jSONObject2 = new JSONObject(message.ext2);
                    PushLog.i(TAG, "PushManagerImpl.project:" + string);
                    jSONObject.put("eventType", "receiveMsg");
                    jSONObject.put("subscriber", PushManagerImpl.subscriber);
                    jSONObject.put("project", string);
                    jSONObject.put("reqId", message.reqid);
                    jSONObject.put("packageName", this.m_pushService.getPackageName());
                    jSONObject.put("notificationState", PushManagerImpl.checkNotifySettingContext(this.m_pushService));
                    jSONObject.put("msgType", "1");
                    jSONObject.put("channelType", "niepush");
                    jSONObject.put("platform", "ad");
                    jSONObject.put("timezone", UnisdkDeviceUtil.getAreaZone());
                    jSONObject.put("sdkversion", PushConstantsImpl.SDK_VERSION);
                    jSONObject.put("ngpush", jSONObject2.opt("ngpush"));
                    jSONObject.put("timestamp", (int) (System.currentTimeMillis() / 1000));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                UploadUtil.SendRequest(jSONObject, this.m_pushService);
                PushLog.i(TAG, "got a message");
                PushLog.d(TAG, "packagename:" + message.service);
                PushLog.d(TAG, "title:" + message.title);
                PushLog.d(TAG, "content:" + message.content);
                PushLog.d(TAG, "ext:" + message.ext);
                PushLog.d(TAG, "ext2:" + message.ext2);
                PushLog.d(TAG, "channelId:" + message.channelId);
                PushLog.d(TAG, "channelName:" + message.channelName);
                PushLog.d(TAG, "groupId:" + message.groupId);
                PushLog.d(TAG, "groupName:" + message.groupName);
                PushLog.d(TAG, "time:" + message.time);
                PushLog.d(TAG, "notifyid:" + message.notifyid);
                PushLog.d(TAG, "message.packagename:" + message.packagename);
                String str2 = message.service;
                if (TextUtils.isEmpty(str2)) {
                    PushLog.e(TAG, "packagename is empty");
                } else {
                    AppInfo appInfo = PushSetting.getAppInfo(this.m_pushService, str2);
                    if (appInfo == null) {
                        PushLog.e(TAG, "not registered packagename:" + str2);
                    } else {
                        i = i2;
                        if (message.time <= appInfo.mLastReceiveTime - this.m_recvTimeError) {
                            PushLog.e(TAG, "message is out of date:");
                            PushLog.e(TAG, "appInfo.mLastReceiveTime:" + appInfo.mLastReceiveTime);
                            PushLog.e(TAG, "message.time:" + message.time);
                        } else if (!TextUtils.isEmpty(message.packagename) && !message.packagename.equals(appInfo.mPackageName)) {
                            PushLog.e(TAG, "packagename mismatch:");
                            PushLog.e(TAG, "message.packagename:" + message.packagename);
                            PushLog.e(TAG, "appInfo.mPackageName:" + appInfo.mPackageName);
                        } else if (appInfo.filterMessage(message)) {
                            PushLog.w(TAG, "message is filtered");
                        } else {
                            if (!map.containsKey(str2)) {
                                map.put(str2, Long.valueOf(message.time));
                            } else if (message.time > ((Long) map.get(str2)).longValue()) {
                                map.put(str2, Long.valueOf(message.time));
                            }
                            NotifyMessageImpl notifyMessageImpl = new NotifyMessageImpl(message.title, message.content, message.ext, message.notifyid, message.reqid, message.sound, "niepush", message.ext2);
                            if (!TextUtils.isEmpty(message.channelId)) {
                                notifyMessageImpl.setChannelId(message.channelId);
                                notifyMessageImpl.setGroupId(message.groupId);
                                notifyMessageImpl.setChannelName(message.channelName);
                                notifyMessageImpl.setGroupName(message.groupName);
                            }
                            try {
                                String strWriteToJsonString = notifyMessageImpl.writeToJsonString();
                                Intent intentCreateMessageIntent = PushClientReceiver.createMessageIntent();
                                intentCreateMessageIntent.putExtra("message", strWriteToJsonString);
                                intentCreateMessageIntent.putExtra("lasttime", message.time);
                                PushLog.d(TAG, "service packageName:" + this.m_pushService.getPackageName());
                                Iterator<ResolveInfo> it = this.m_pushService.getPackageManager().queryBroadcastReceivers(new Intent(PushConstantsImpl.CLIENT_ACTION_MESSAGE), 0).iterator();
                                while (true) {
                                    if (!it.hasNext()) {
                                        str = "";
                                        break;
                                    }
                                    ResolveInfo next = it.next();
                                    String str3 = next.activityInfo.packageName;
                                    if (str2.equalsIgnoreCase(str3)) {
                                        str = next.activityInfo.name;
                                        if (str.startsWith(".")) {
                                            str = str3 + str;
                                        }
                                        PushLog.d(TAG, "sendBroadcast receiverName:" + str);
                                    }
                                }
                                if (!TextUtils.isEmpty(str)) {
                                    intentCreateMessageIntent.setComponent(new ComponentName(str2, str));
                                } else {
                                    intentCreateMessageIntent.setPackage(str2);
                                }
                                PushLog.d(TAG, "handlePush, sendBroadcast");
                                this.m_pushService.sendBroadcast(intentCreateMessageIntent, this.m_pushService.getPackageName() + ".permission.ngpush");
                            } catch (Exception e2) {
                                PushLog.e(TAG, "writeToJsonString exception:" + e2.getMessage());
                                return;
                            }
                        }
                    }
                }
                i = i2;
            }
            int i3 = 0;
            if (map.size() > 0) {
                ProtoClientWrapper.DevServiceInfos devServiceInfos = new ProtoClientWrapper.DevServiceInfos();
                devServiceInfos.id = this.m_serviceInfo.mDevId;
                devServiceInfos.ver = "342";
                devServiceInfos.serviceInfos = new ProtoClientWrapper.ServiceInfo[map.size()];
                for (Map.Entry entry : map.entrySet()) {
                    ProtoClientWrapper.ServiceInfo serviceInfo = new ProtoClientWrapper.ServiceInfo();
                    serviceInfo.service = (String) entry.getKey();
                    serviceInfo.time = ((Long) entry.getValue()).longValue();
                    PushLog.d(TAG, "service:" + serviceInfo.service);
                    PushLog.d(TAG, "latest push time:" + serviceInfo.time);
                    devServiceInfos.serviceInfos[i3] = serviceInfo;
                    i3++;
                    AppInfo appInfo2 = PushSetting.getAppInfo(this.m_pushService, serviceInfo.service);
                    if (appInfo2 != null) {
                        appInfo2.mLastReceiveTime = serviceInfo.time;
                    }
                }
                PushLog.e(TAG, "sendData, GOT_TIME_TYPE");
                getNetwork().sendData((byte) 5, devServiceInfos);
            }
        } catch (Exception e3) {
            PushLog.e(TAG, "unmarshalMessageInfo exception:" + e3.getMessage());
        }
    }

    public void processCommand(PushService pushService, Intent intent) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.e(TAG, "processCommand");
        PushLog.d(TAG, "pushService:" + pushService);
        PushLog.d(TAG, "intent:" + intent);
        if (intent == null) {
            return;
        }
        String stringExtra = intent.getStringExtra("method");
        String packageName = pushService.getPackageName();
        PushLog.d(TAG, "method:" + stringExtra);
        PushLog.d(TAG, "packageName:" + packageName);
        Boolean boolValueOf = Boolean.valueOf(PushSetting.getCurUseNiepush(this.m_pushService, true));
        PushLog.d(TAG, "useNiepush=" + boolValueOf);
        if (PushConstantsImpl.SERVICE_METHOD_SETVIBRATE.equals(stringExtra)) {
            enableVibrate(packageName, intent.getBooleanExtra(PushConstantsImpl.INTENT_FLAG_NAME, false));
            return;
        }
        if (PushConstantsImpl.SERVICE_METHOD_REPEATPROTECT.equals(stringExtra)) {
            enableRepeatProtect(packageName, intent.getBooleanExtra(PushConstantsImpl.INTENT_FLAG_NAME, false));
            return;
        }
        if (PushConstantsImpl.SERVICE_METHOD_REPEATPROTECT_INTERVAL.equals(stringExtra)) {
            setRepeatProtectInterval(packageName, intent.getIntExtra(PushConstantsImpl.INTENT_NO_REPEAT_INTERVAL_NAME, 300));
            return;
        }
        if (PushConstantsImpl.SERVICE_METHOD_NETWORKCONNECT.equals(stringExtra)) {
            if (boolValueOf.booleanValue()) {
                connect(false);
            }
        } else if (PushConstantsImpl.SERVICE_METHOD_NETWORKDISCONNECT.equals(stringExtra)) {
            if (boolValueOf.booleanValue()) {
                disconnect();
            }
        } else {
            PushLog.d(TAG, "not handled method:" + stringExtra);
        }
    }

    /* renamed from: com.netease.pushservice.PushServiceHelper$1 */
    class AnonymousClass1 implements Runnable {
        final /* synthetic */ String val$packageName;

        AnonymousClass1(String str) {
            str = str;
        }

        @Override // java.lang.Runnable
        public void run() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            PushLog.i(PushServiceHelper.TAG, "SERVICE_METHOD_REGISTER");
            PushServiceHelper.this.register(str);
        }
    }

    public void niepushRegister(String str) {
        this.m_taskSubmitter.submit(new Runnable() { // from class: com.netease.pushservice.PushServiceHelper.1
            final /* synthetic */ String val$packageName;

            AnonymousClass1(String str2) {
                str = str2;
            }

            @Override // java.lang.Runnable
            public void run() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                PushLog.i(PushServiceHelper.TAG, "SERVICE_METHOD_REGISTER");
                PushServiceHelper.this.register(str);
            }
        });
    }

    /* renamed from: com.netease.pushservice.PushServiceHelper$2 */
    class AnonymousClass2 implements Runnable {
        AnonymousClass2() {
        }

        @Override // java.lang.Runnable
        public void run() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            PushLog.d(PushServiceHelper.TAG, "stop+++");
            PushServiceHelper.this.getNetwork().stop();
            PushLog.d(PushServiceHelper.TAG, "stop---");
        }
    }

    public void stop() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, VideoViewManager.PROP_STOP);
        new Thread(new Runnable() { // from class: com.netease.pushservice.PushServiceHelper.2
            AnonymousClass2() {
            }

            @Override // java.lang.Runnable
            public void run() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                PushLog.d(PushServiceHelper.TAG, "stop+++");
                PushServiceHelper.this.getNetwork().stop();
                PushLog.d(PushServiceHelper.TAG, "stop---");
            }
        }).start();
        this.m_taskSubmitter.shutdown();
        this.m_taskSubmitter = null;
    }

    public void connect(boolean z) {
        PushLog.i(TAG, "connect, bSync:" + z);
        PushLog.d(TAG, "connect, this=" + this);
        PushLog.d(TAG, "connect, m_network=" + this.m_network);
        Boolean boolValueOf = Boolean.valueOf(PushSetting.getCurUseNiepush(this.m_pushService, true));
        PushLog.d(TAG, "useNiepush=" + boolValueOf);
        if (!boolValueOf.booleanValue()) {
            PushLog.e(TAG, "need not niepush");
            return;
        }
        if (!checkProtobuf()) {
            PushLog.e(TAG, "missing jar in libs: protobuf-javanano-3.0.0-alpha-5.jar");
        } else if (z) {
            getNetwork().setEnable(true);
            getNetwork().connectAuto(this.m_pushService);
        } else {
            this.m_taskSubmitter.submit(new Runnable() { // from class: com.netease.pushservice.PushServiceHelper.3
                AnonymousClass3() {
                }

                @Override // java.lang.Runnable
                public void run() {
                    PushServiceHelper.this.getNetwork().setEnable(true);
                    PushServiceHelper.this.getNetwork().connectAuto(PushServiceHelper.this.m_pushService);
                }
            });
        }
    }

    /* renamed from: com.netease.pushservice.PushServiceHelper$3 */
    class AnonymousClass3 implements Runnable {
        AnonymousClass3() {
        }

        @Override // java.lang.Runnable
        public void run() {
            PushServiceHelper.this.getNetwork().setEnable(true);
            PushServiceHelper.this.getNetwork().connectAuto(PushServiceHelper.this.m_pushService);
        }
    }

    /* renamed from: com.netease.pushservice.PushServiceHelper$4 */
    class AnonymousClass4 implements Runnable {
        AnonymousClass4() {
        }

        @Override // java.lang.Runnable
        public void run() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            PushLog.d(PushServiceHelper.TAG, "disconnect+++");
            PushServiceHelper.this.getNetwork().disconnect();
            PushLog.d(PushServiceHelper.TAG, "disconnect---");
        }
    }

    public void disconnect() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "disconnect...");
        this.m_taskSubmitter.submit(new Runnable() { // from class: com.netease.pushservice.PushServiceHelper.4
            AnonymousClass4() {
            }

            @Override // java.lang.Runnable
            public void run() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                PushLog.d(PushServiceHelper.TAG, "disconnect+++");
                PushServiceHelper.this.getNetwork().disconnect();
                PushLog.d(PushServiceHelper.TAG, "disconnect---");
            }
        });
    }

    public class TaskSubmitter {
        final ExecutorService m_executorService = Executors.newCachedThreadPool();

        public TaskSubmitter() {
        }

        public Future submit(Runnable runnable) {
            if (this.m_executorService.isTerminated() || this.m_executorService.isShutdown() || runnable == null) {
                return null;
            }
            return this.m_executorService.submit(runnable);
        }

        public void shutdown() {
            this.m_executorService.shutdown();
        }
    }

    public TaskSubmitter getTaskSubmitter() {
        return this.m_taskSubmitter;
    }

    public Network getNetwork() {
        return this.m_network;
    }

    private void checkFirstStart(AppInfo appInfo) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.d(TAG, "checkFirstStart");
        PushLog.d(TAG, "appInfo.mbFirstStart:" + appInfo.mbFirstStart);
        PushLog.d(TAG, "appInfo.mPackageName:" + appInfo.mPackageName);
        PushLog.d(TAG, "appInfo.mLastReceiveTime:" + appInfo.mLastReceiveTime);
        if (TextUtils.isEmpty(this.m_serviceInfo.mDevId)) {
            return;
        }
        if (appInfo.mbFirstStart) {
            appInfo.mbFirstStart = false;
            PushSetting.setFirstStart(this.m_pushService, appInfo.mPackageName, false);
        }
        broadcastRegid(this.m_pushService, appInfo.mPackageName, this.m_serviceInfo.mDevId);
    }

    private void broadcastRegid(Context context, String str, String str2) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Intent intentCreateNewIDIntent = PushClientReceiver.createNewIDIntent();
        intentCreateNewIDIntent.putExtra(PushConstantsImpl.INTENT_DEVID_NAME, str2);
        intentCreateNewIDIntent.setPackage(str);
        PushLog.d(TAG, "broadcast createNewIDIntent regid:" + str2);
        context.sendBroadcast(intentCreateNewIDIntent, context.getPackageName() + ".permission.ngpush");
    }

    private void broadcastToken(Context context, String str, String str2, String str3, String str4) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.d(TAG, "\u5f00\u59cb\u4fdd\u5b58token");
        Intent intent = new Intent(PushConstantsImpl.CLIENT_ACTION_SAVE_TOKEN);
        intent.addFlags(32);
        intent.putExtra("token", str2);
        intent.putExtra("access_key", str3);
        intent.putExtra("type", str4);
        intent.setPackage(str);
        context.sendBroadcast(intent, str + ".permission.ngpush");
    }

    public void broadcastLoginCallback(String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.d(TAG, "broadcastLoginCallback");
        Intent intent = new Intent(PushConstantsImpl.CLIENT_ACTION_LOGIN_CALLBACK);
        intent.addFlags(32);
        intent.putExtra("resultDataJson", str);
        intent.setPackage(this.m_pushService.getPackageName());
        this.m_pushService.sendBroadcast(intent, this.m_pushService.getPackageName() + ".permission.ngpush");
    }

    public void notifyMessage(String str, NotifyMessageImpl notifyMessageImpl) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        AppInfo appInfo;
        PushLog.i(TAG, "notifyMessage");
        PushLog.d(TAG, "packageName:" + str);
        PushLog.d(TAG, "notify:" + notifyMessageImpl);
        if (TextUtils.isEmpty(str) || notifyMessageImpl == null || (appInfo = PushSetting.getAppInfo(this.m_pushService, str)) == null) {
            return;
        }
        new Notifier(this.m_pushService).notify(notifyMessageImpl, appInfo);
    }

    private void enableSound(String str, boolean z) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.d(TAG, "enableSound");
        PushLog.d(TAG, "packageName:" + str);
        PushLog.d(TAG, "flag:" + z);
        PushLog.d(TAG, "pid:" + Process.myPid());
        AppInfo appInfo = PushSetting.getAppInfo(this.m_pushService, str);
        if (appInfo == null || appInfo.mbEnableSound == z) {
            return;
        }
        appInfo.mbEnableSound = z;
    }

    private void enableVibrate(String str, boolean z) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.d(TAG, "enableVibrate");
        PushLog.d(TAG, "packageName:" + str);
        PushLog.d(TAG, "flag:" + z);
        PushLog.d(TAG, "pid:" + Process.myPid());
        AppInfo appInfo = PushSetting.getAppInfo(this.m_pushService, str);
        if (appInfo == null || appInfo.mbEnableVibrate == z) {
            return;
        }
        appInfo.mbEnableVibrate = z;
    }

    private void enableLight(String str, boolean z) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.d(TAG, "enableLight");
        PushLog.d(TAG, "packageName:" + str);
        PushLog.d(TAG, "flag:" + z);
        PushLog.d(TAG, "pid:" + Process.myPid());
        AppInfo appInfo = PushSetting.getAppInfo(this.m_pushService, str);
        if (appInfo == null || appInfo.mbEnableLight == z) {
            return;
        }
        appInfo.mbEnableLight = z;
    }

    private void enableRepeatProtect(String str, boolean z) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.d(TAG, "enableRepeatProtect");
        PushLog.d(TAG, "packageName:" + str);
        PushLog.d(TAG, "enable:" + z);
        PushLog.d(TAG, "pid:" + Process.myPid());
        AppInfo appInfo = PushSetting.getAppInfo(this.m_pushService, str);
        if (appInfo != null) {
            appInfo.enableRepeatProtect(z);
        }
    }

    private void setRepeatProtectInterval(String str, int i) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.d(TAG, "setRepeatProtectInterval");
        PushLog.d(TAG, "packageName:" + str);
        PushLog.d(TAG, "interval:" + i);
        PushLog.d(TAG, "pid:" + Process.myPid());
        AppInfo appInfo = PushSetting.getAppInfo(this.m_pushService, str);
        if (appInfo != null) {
            appInfo.setRepeatProtectInterval(i);
        }
    }

    public PushServiceInfo getNotificationServiceInfo() {
        return this.m_serviceInfo;
    }

    public static Intent createServiceIntent() {
        Intent intent = new Intent(PushConstantsImpl.SERVICE_ACTION2);
        intent.addFlags(32);
        return intent;
    }

    public static Intent createMethodIntent() {
        Intent intentCreateActiveMethodIntent = createActiveMethodIntent();
        intentCreateActiveMethodIntent.addFlags(32);
        return intentCreateActiveMethodIntent;
    }

    public static Intent createActiveMethodIntent() {
        Intent intent = new Intent(PushConstantsImpl.SERVICE_ACTION_METHOD);
        intent.putExtra(PushConstantsImpl.METHOD_VER_NAME, 1);
        return intent;
    }

    public static void startPushService(Context context, Intent intent) {
        PushLog.i(TAG, "startPushService");
        if (!PushSetting.getCurUseNiepush(context, true)) {
            PushLog.e(TAG, "need not niepush");
            return;
        }
        try {
            intent.setClass(context, PushService.class);
            try {
                int i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.targetSdkVersion;
            } catch (PackageManager.NameNotFoundException e) {
                PushLog.e(TAG, "PackageManager.NameNotFoundException:" + e.toString());
            }
            intent.putExtra(IdCache.androidIdTag, UnisdkDeviceUtil.getAndroidId(context));
            intent.putExtra(IdCache.transIdTag, UnisdkDeviceUtil.getTransId());
            context.startService(intent);
        } catch (Exception unused) {
        }
    }

    public static void startActivePushService(Context context, Intent intent) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "startActivePushService");
        boolean curUseNiepush = PushSetting.getCurUseNiepush(context, true);
        PushLog.i(TAG, "useNiepush:" + curUseNiepush);
        if (!curUseNiepush) {
            PushLog.e(TAG, "need not niepush");
            return;
        }
        if (context != null) {
            try {
                intent.setClass(context, PushService.class);
                try {
                    int i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.targetSdkVersion;
                } catch (PackageManager.NameNotFoundException e) {
                    PushLog.e(TAG, "PackageManager.NameNotFoundException:" + e.toString());
                }
                intent.putExtra(IdCache.androidIdTag, UnisdkDeviceUtil.getAndroidId(context));
                intent.putExtra(IdCache.transIdTag, UnisdkDeviceUtil.getTransId());
                PushLog.i(TAG, "intent:" + intent);
                context.startService(intent);
            } catch (Exception unused) {
            }
        }
    }

    private boolean checkProtobuf() throws IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
        try {
            Class.forName("com.google.protobuf.nano.MessageNano");
            return true;
        } catch (ClassNotFoundException e) {
            PushLog.e(TAG, "ClassNotFoundException:" + e.toString());
            return false;
        }
    }
}