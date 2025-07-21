package com.netease.push.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import com.netease.ntunisdk.base.PatchPlaceholder;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/* loaded from: classes3.dex */
public class Notifier {
    private static final String TAG = "NGPush_" + Notifier.class.getSimpleName();
    private static final Random random = new Random(System.currentTimeMillis());
    private AppInfo appInfo;
    private PendingIntent contentIntent;
    private Context context;
    private NotificationManager notificationManager;
    private NotifyMessageImpl notifyMessage;
    private int notifyid;
    private Bitmap bmp = null;
    private String bmpUrl = "";
    private Bitmap smallbmp = null;
    private String smallbmpUrl = "";
    private boolean isAndroidO = false;

    private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, PatchPlaceholder.class.getSimpleName());
    }

    public Notifier(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService("notification");
    }

    /* JADX WARN: Removed duplicated region for block: B:70:0x0395  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x03ea  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void notify(com.netease.push.utils.NotifyMessageImpl r19, com.netease.push.utils.AppInfo r20) {
        /*
            Method dump skipped, instructions count: 1057
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.push.utils.Notifier.notify(com.netease.push.utils.NotifyMessageImpl, com.netease.push.utils.AppInfo):void");
    }

    private NotificationChannel createChannelId(String str, NotifyMessageImpl notifyMessageImpl, AppInfo appInfo) {
        int identifier;
        this.notificationManager.createNotificationChannelGroup(new NotificationChannelGroup(notifyMessageImpl.getGroupId(), notifyMessageImpl.getGroupName()));
        NotificationChannel notificationChannel = new NotificationChannel(str, notifyMessageImpl.getChannelName(), 4);
        notificationChannel.setGroup(notifyMessageImpl.getGroupId());
        notificationChannel.setLockscreenVisibility(0);
        if (appInfo.mbEnableSound && !TextUtils.isEmpty(notifyMessageImpl.getSound()) && (identifier = this.context.getResources().getIdentifier(notifyMessageImpl.getSound(), "raw", this.context.getPackageName())) != 0) {
            notificationChannel.setSound(Uri.parse("android.resource://" + this.context.getPackageName() + "/" + identifier), new AudioAttributes.Builder().setContentType(4).setUsage(6).build());
        }
        if (appInfo.mbEnableLight) {
            notificationChannel.enableLights(true);
        }
        this.notificationManager.createNotificationChannel(notificationChannel);
        return notificationChannel;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendNotification(NotifyMessageImpl notifyMessageImpl, int i, PendingIntent pendingIntent, AppInfo appInfo) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        int identifier;
        PushLog.d(TAG, "sendNotification");
        Notification.Builder builder = new Notification.Builder(this.context);
        if (notifyMessageImpl.getIcon() > 0) {
            builder.setSmallIcon(notifyMessageImpl.getIcon());
        } else {
            builder.setSmallIcon(this.context.getApplicationInfo().icon);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            builder.setGroup(notifyMessageImpl.getGroupId());
        }
        Bitmap bitmap = this.smallbmp;
        if (bitmap != null) {
            builder.setLargeIcon(bitmap);
        }
        builder.setContentTitle(notifyMessageImpl.getTitle());
        builder.setContentText(notifyMessageImpl.getMsg());
        int i2 = 0;
        if (appInfo.mbEnableSound) {
            if (TextUtils.isEmpty(notifyMessageImpl.getSound()) || (identifier = this.context.getResources().getIdentifier(notifyMessageImpl.getSound(), "raw", this.context.getPackageName())) == 0) {
                i2 = 1;
            } else {
                builder.setSound(Uri.parse("android.resource://" + this.context.getPackageName() + "/" + identifier));
            }
        }
        if (appInfo.mbEnableVibrate) {
            i2 |= 2;
        }
        if (appInfo.mbEnableLight) {
            i2 |= 4;
        }
        builder.setDefaults(i2);
        builder.setAutoCancel(true);
        builder.setTicker(notifyMessageImpl.getMsg());
        Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
        bigTextStyle.bigText(notifyMessageImpl.getMsg());
        builder.setStyle(bigTextStyle);
        builder.setContentIntent(pendingIntent);
        this.notificationManager.notify(i, builder.build());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendNotificationAndroidO(NotifyMessageImpl notifyMessageImpl, int i, PendingIntent pendingIntent, AppInfo appInfo) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String channelId = notifyMessageImpl.getChannelId();
        if ("channel_unisdk_ngpush_id".equalsIgnoreCase(notifyMessageImpl.getChannelId())) {
            channelId = channelId + notifyMessageImpl.getSound();
        }
        Notification.Builder builder = new Notification.Builder(this.context, channelId);
        builder.setContentTitle(notifyMessageImpl.getTitle()).setContentText(notifyMessageImpl.getMsg()).setAutoCancel(true);
        builder.setContentTitle(notifyMessageImpl.getTitle());
        builder.setContentText(notifyMessageImpl.getMsg());
        PushLog.d(TAG, "smallbmp:" + this.smallbmp);
        Bitmap bitmap = this.smallbmp;
        if (bitmap != null) {
            builder.setLargeIcon(bitmap);
        }
        if (notifyMessageImpl.getIcon() > 0) {
            builder.setSmallIcon(notifyMessageImpl.getIcon());
        } else {
            builder.setSmallIcon(this.context.getApplicationInfo().icon);
        }
        builder.setContentIntent(pendingIntent);
        createChannelId(channelId, notifyMessageImpl, appInfo).setShowBadge(true);
        builder.setGroup(notifyMessageImpl.getGroupId());
        int i2 = 0;
        if (appInfo.mbEnableSound && TextUtils.isEmpty(notifyMessageImpl.getSound())) {
            i2 = 1;
        }
        if (appInfo.mbEnableSound && !TextUtils.isEmpty(notifyMessageImpl.getSound()) && this.context.getResources().getIdentifier(notifyMessageImpl.getSound(), "raw", this.context.getPackageName()) == 0) {
            i2 |= 1;
        }
        if (appInfo.mbEnableVibrate) {
            i2 |= 2;
        }
        if (appInfo.mbEnableLight) {
            i2 |= 4;
        }
        builder.setDefaults(i2);
        builder.setAutoCancel(true);
        this.notificationManager.notify(i, builder.build());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendBigImageNotifyAndroidO(NotifyMessageImpl notifyMessageImpl, int i, PendingIntent pendingIntent, AppInfo appInfo) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.d(TAG, "sendBigImageNotifyAndroidO");
        String channelId = notifyMessageImpl.getChannelId();
        if ("channel_unisdk_ngpush_id".equalsIgnoreCase(notifyMessageImpl.getChannelId())) {
            channelId = channelId + notifyMessageImpl.getSound();
        }
        Notification.Builder builder = new Notification.Builder(this.context, channelId);
        builder.setContentTitle(notifyMessageImpl.getTitle()).setContentText(notifyMessageImpl.getMsg()).setAutoCancel(true);
        PushLog.d(TAG, "smallbmp:" + this.smallbmp);
        Bitmap bitmap = this.smallbmp;
        if (bitmap != null) {
            builder.setLargeIcon(bitmap);
        }
        Notification.BigPictureStyle bigPictureStyle = new Notification.BigPictureStyle();
        bigPictureStyle.bigPicture(this.bmp);
        bigPictureStyle.setBigContentTitle(notifyMessageImpl.getTitle());
        bigPictureStyle.setSummaryText(notifyMessageImpl.getMsg());
        builder.setStyle(bigPictureStyle);
        if (notifyMessageImpl.getIcon() > 0) {
            builder.setSmallIcon(notifyMessageImpl.getIcon());
        } else {
            builder.setSmallIcon(this.context.getApplicationInfo().icon);
        }
        builder.setContentIntent(pendingIntent);
        createChannelId(channelId, notifyMessageImpl, appInfo).setShowBadge(true);
        builder.setGroup(notifyMessageImpl.getGroupId());
        int i2 = 0;
        if (appInfo.mbEnableSound && TextUtils.isEmpty(notifyMessageImpl.getSound())) {
            i2 = 1;
        }
        if (appInfo.mbEnableSound && !TextUtils.isEmpty(notifyMessageImpl.getSound()) && this.context.getResources().getIdentifier(notifyMessageImpl.getSound(), "raw", this.context.getPackageName()) == 0) {
            i2 |= 1;
        }
        if (appInfo.mbEnableVibrate) {
            i2 |= 2;
        }
        if (appInfo.mbEnableLight) {
            i2 |= 4;
        }
        builder.setDefaults(i2);
        builder.setAutoCancel(true);
        this.notificationManager.notify(i, builder.build());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendBigImageNotify(NotifyMessageImpl notifyMessageImpl, int i, PendingIntent pendingIntent, AppInfo appInfo) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        int identifier;
        PushLog.d(TAG, "sendBigImageNotify");
        Notification.Builder builder = new Notification.Builder(this.context);
        Notification.BigPictureStyle bigPictureStyle = new Notification.BigPictureStyle();
        bigPictureStyle.bigPicture(this.bmp);
        bigPictureStyle.setBigContentTitle(notifyMessageImpl.getTitle());
        bigPictureStyle.setSummaryText(notifyMessageImpl.getMsg());
        bigPictureStyle.bigLargeIcon(this.smallbmp);
        builder.setStyle(bigPictureStyle);
        if (notifyMessageImpl.getIcon() > 0) {
            builder.setSmallIcon(notifyMessageImpl.getIcon());
        } else {
            builder.setSmallIcon(this.context.getApplicationInfo().icon);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            builder.setGroup(notifyMessageImpl.getGroupId());
        }
        builder.setContentTitle(notifyMessageImpl.getTitle());
        builder.setContentText(notifyMessageImpl.getMsg());
        int i2 = 0;
        if (appInfo.mbEnableSound) {
            if (TextUtils.isEmpty(notifyMessageImpl.getSound()) || (identifier = this.context.getResources().getIdentifier(notifyMessageImpl.getSound(), "raw", this.context.getPackageName())) == 0) {
                i2 = 1;
            } else {
                builder.setSound(Uri.parse("android.resource://" + this.context.getPackageName() + "/" + identifier));
            }
        }
        if (appInfo.mbEnableVibrate) {
            i2 |= 2;
        }
        if (appInfo.mbEnableLight) {
            i2 |= 4;
        }
        builder.setDefaults(i2);
        builder.setAutoCancel(true);
        builder.setTicker(notifyMessageImpl.getMsg());
        builder.setContentIntent(pendingIntent);
        this.notificationManager.notify(i, builder.build());
    }

    public void getBitmap(String str, String str2, NotifyMessageImpl notifyMessageImpl, int i, PendingIntent pendingIntent, AppInfo appInfo, boolean z) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.d(TAG, "getBitmap");
        this.bmpUrl = str;
        this.smallbmpUrl = str2;
        this.notifyMessage = notifyMessageImpl;
        this.notifyid = i;
        this.contentIntent = pendingIntent;
        this.appInfo = appInfo;
        this.isAndroidO = z;
        new Thread(new Runnable() { // from class: com.netease.push.utils.Notifier.1
            @Override // java.lang.Runnable
            public void run() throws IOException {
                try {
                    PushLog.d(Notifier.TAG, "getBitmap bmp url\uff1a" + Notifier.this.bmpUrl);
                    if (!TextUtils.isEmpty(Notifier.this.bmpUrl)) {
                        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(Notifier.this.bmpUrl).openConnection();
                        httpURLConnection.setConnectTimeout(6000);
                        httpURLConnection.setDoInput(true);
                        httpURLConnection.setUseCaches(false);
                        httpURLConnection.connect();
                        InputStream inputStream = httpURLConnection.getInputStream();
                        Notifier.this.bmp = BitmapFactory.decodeStream(inputStream);
                        inputStream.close();
                    }
                    PushLog.d(Notifier.TAG, "getBitmap smallbmpUrl\uff1a" + Notifier.this.smallbmpUrl);
                    if (!TextUtils.isEmpty(Notifier.this.smallbmpUrl)) {
                        HttpURLConnection httpURLConnection2 = (HttpURLConnection) new URL(Notifier.this.smallbmpUrl).openConnection();
                        httpURLConnection2.setConnectTimeout(6000);
                        httpURLConnection2.setDoInput(true);
                        httpURLConnection2.setUseCaches(false);
                        httpURLConnection2.connect();
                        InputStream inputStream2 = httpURLConnection2.getInputStream();
                        Notifier.this.smallbmp = BitmapFactory.decodeStream(inputStream2);
                        inputStream2.close();
                    }
                    PushLog.d(Notifier.TAG, "getBitmap bmp\uff1a" + Notifier.this.bmp);
                    PushLog.d(Notifier.TAG, "getBitmap smallbmp\uff1a" + Notifier.this.smallbmp);
                    if (Notifier.this.isAndroidO) {
                        if (!TextUtils.isEmpty(Notifier.this.bmpUrl)) {
                            Notifier.this.sendBigImageNotifyAndroidO(Notifier.this.notifyMessage, Notifier.this.notifyid, Notifier.this.contentIntent, Notifier.this.appInfo);
                            return;
                        } else {
                            Notifier.this.sendNotificationAndroidO(Notifier.this.notifyMessage, Notifier.this.notifyid, Notifier.this.contentIntent, Notifier.this.appInfo);
                            return;
                        }
                    }
                    if (!TextUtils.isEmpty(Notifier.this.bmpUrl)) {
                        Notifier.this.sendBigImageNotify(Notifier.this.notifyMessage, Notifier.this.notifyid, Notifier.this.contentIntent, Notifier.this.appInfo);
                    } else {
                        Notifier.this.sendNotification(Notifier.this.notifyMessage, Notifier.this.notifyid, Notifier.this.contentIntent, Notifier.this.appInfo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}