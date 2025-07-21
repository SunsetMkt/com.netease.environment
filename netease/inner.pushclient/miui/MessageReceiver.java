package com.netease.inner.pushclient.miui;

import android.content.Context;
import android.content.Intent;
import com.netease.inner.pushclient.PushClientReceiver;
import com.netease.inner.pushclient.PushManager;
import com.netease.ntunisdk.base.PatchPlaceholder;
import com.netease.push.utils.PushConstantsImpl;
import com.netease.push.utils.PushLog;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/* loaded from: classes5.dex */
public class MessageReceiver extends PushMessageReceiver {
    private static final String TAG = "NGPush_MIUI_" + MessageReceiver.class.getSimpleName();
    private String mAccount;
    private String mAlias;
    private String mEndTime;
    private String mRegId;
    private String mStartTime;
    private String mTopic;

    private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, PatchPlaceholder.class.getSimpleName());
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0031  */
    @Override // com.xiaomi.mipush.sdk.PushMessageReceiver
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void onReceivePassThroughMessage(android.content.Context r5, com.xiaomi.mipush.sdk.MiPushMessage r6) throws java.lang.IllegalAccessException, java.lang.IllegalArgumentException, java.lang.reflect.InvocationTargetException {
        /*
            r4 = this;
            java.lang.String r0 = com.netease.inner.pushclient.miui.MessageReceiver.TAG
            java.lang.String r1 = "onReceivePassThroughMessage"
            com.netease.push.utils.PushLog.i(r0, r1)
            java.lang.String r0 = r6.getContent()
            r6.getExtra()
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            if (r1 != 0) goto L31
            java.lang.String r1 = "\\|"
            java.lang.String[] r0 = android.text.TextUtils.split(r0, r1)
            int r1 = r0.length
            r2 = 2
            if (r1 < r2) goto L31
            int r1 = r0.length
            int r1 = r1 + (-1)
            r1 = r0[r1]
            r2 = 0
            int r3 = r0.length
            int r3 = r3 + (-1)
            java.lang.String[] r0 = com.netease.push.utils.StrUtil.copyOfRange(r0, r2, r3)
            java.lang.String r2 = "|"
            android.text.TextUtils.join(r2, r0)
            goto L33
        L31:
            java.lang.String r1 = ""
        L33:
            java.lang.String r0 = com.netease.inner.pushclient.miui.MessageReceiver.TAG
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "reqid="
            r2.append(r3)
            r2.append(r1)
            java.lang.String r1 = r2.toString()
            com.netease.push.utils.PushLog.d(r0, r1)
            r4.handleMessage(r5, r6)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.inner.pushclient.miui.MessageReceiver.onReceivePassThroughMessage(android.content.Context, com.xiaomi.mipush.sdk.MiPushMessage):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0049  */
    @Override // com.xiaomi.mipush.sdk.PushMessageReceiver
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void onNotificationMessageArrived(android.content.Context r5, com.xiaomi.mipush.sdk.MiPushMessage r6) throws java.lang.IllegalAccessException, java.lang.IllegalArgumentException, java.lang.reflect.InvocationTargetException {
        /*
            r4 = this;
            java.lang.String r0 = "ngpush"
            java.lang.String r1 = "onNotificationMessageArrived"
            com.netease.push.utils.PushLog.i(r0, r1)
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "message="
            r1.append(r2)
            java.lang.String r2 = r6.toString()
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            com.netease.push.utils.PushLog.i(r0, r1)
            java.lang.String r0 = r6.getContent()
            r6.getExtra()
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            if (r1 != 0) goto L49
            java.lang.String r1 = "\\|"
            java.lang.String[] r0 = android.text.TextUtils.split(r0, r1)
            int r1 = r0.length
            r2 = 2
            if (r1 < r2) goto L49
            int r1 = r0.length
            int r1 = r1 + (-1)
            r1 = r0[r1]
            r2 = 0
            int r3 = r0.length
            int r3 = r3 + (-1)
            java.lang.String[] r0 = com.netease.push.utils.StrUtil.copyOfRange(r0, r2, r3)
            java.lang.String r2 = "|"
            android.text.TextUtils.join(r2, r0)
            goto L4b
        L49:
            java.lang.String r1 = ""
        L4b:
            java.lang.String r0 = com.netease.inner.pushclient.miui.MessageReceiver.TAG
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "reqid="
            r2.append(r3)
            r2.append(r1)
            java.lang.String r1 = r2.toString()
            com.netease.push.utils.PushLog.d(r0, r1)
            r4.handleMessage(r5, r6)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.inner.pushclient.miui.MessageReceiver.onNotificationMessageArrived(android.content.Context, com.xiaomi.mipush.sdk.MiPushMessage):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x01a2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void handleMessage(android.content.Context r19, com.xiaomi.mipush.sdk.MiPushMessage r20) throws java.lang.IllegalAccessException, java.lang.IllegalArgumentException, java.lang.reflect.InvocationTargetException {
        /*
            Method dump skipped, instructions count: 598
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.inner.pushclient.miui.MessageReceiver.handleMessage(android.content.Context, com.xiaomi.mipush.sdk.MiPushMessage):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0123  */
    @Override // com.xiaomi.mipush.sdk.PushMessageReceiver
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void onNotificationMessageClicked(android.content.Context r18, com.xiaomi.mipush.sdk.MiPushMessage r19) throws java.lang.IllegalAccessException, java.lang.IllegalArgumentException, java.lang.reflect.InvocationTargetException {
        /*
            Method dump skipped, instructions count: 753
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.inner.pushclient.miui.MessageReceiver.onNotificationMessageClicked(android.content.Context, com.xiaomi.mipush.sdk.MiPushMessage):void");
    }

    @Override // com.xiaomi.mipush.sdk.PushMessageReceiver
    public void onCommandResult(Context context, MiPushCommandMessage miPushCommandMessage) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "onCommandResult");
        PushLog.i(TAG, "command message=" + miPushCommandMessage);
        String command = miPushCommandMessage.getCommand();
        List<String> commandArguments = miPushCommandMessage.getCommandArguments();
        String str = null;
        String str2 = (commandArguments == null || commandArguments.size() <= 0) ? null : commandArguments.get(0);
        if (commandArguments != null && commandArguments.size() > 1) {
            str = commandArguments.get(1);
        }
        PushLog.d(TAG, "command=" + command);
        PushLog.d(TAG, "cmdArg1=" + str2);
        PushLog.d(TAG, "cmdArg2=" + str);
        if ("register".equals(command)) {
            if (miPushCommandMessage.getResultCode() == 0) {
                this.mRegId = str2;
                PushLog.d(TAG, "mRegId=" + this.mRegId);
                return;
            }
            PushLog.e(TAG, "COMMAND_REGISTER failed");
            return;
        }
        if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
            if (miPushCommandMessage.getResultCode() == 0) {
                this.mAlias = str2;
                PushLog.d(TAG, "mAlias=" + this.mAlias);
                return;
            }
            PushLog.e(TAG, "COMMAND_SET_ALIAS failed");
            return;
        }
        if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
            if (miPushCommandMessage.getResultCode() == 0) {
                this.mAlias = str2;
                PushLog.d(TAG, "mAlias=" + this.mAlias);
                return;
            }
            PushLog.e(TAG, "COMMAND_UNSET_ALIAS failed");
            return;
        }
        if (MiPushClient.COMMAND_SET_ACCOUNT.equals(command)) {
            if (miPushCommandMessage.getResultCode() == 0) {
                this.mAccount = str2;
                PushLog.d(TAG, "mAccount=" + this.mAccount);
                return;
            }
            PushLog.e(TAG, "COMMAND_SET_ACCOUNT failed");
            return;
        }
        if (MiPushClient.COMMAND_UNSET_ACCOUNT.equals(command)) {
            if (miPushCommandMessage.getResultCode() == 0) {
                this.mAccount = str2;
                PushLog.d(TAG, "mAccount=" + this.mAccount);
                return;
            }
            PushLog.e(TAG, "COMMAND_UNSET_ACCOUNT failed");
            return;
        }
        if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
            if (miPushCommandMessage.getResultCode() == 0) {
                this.mTopic = str2;
                PushLog.d(TAG, "mTopic=" + this.mTopic);
                return;
            }
            PushLog.e(TAG, "COMMAND_SUBSCRIBE_TOPIC failed");
            return;
        }
        if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {
            if (miPushCommandMessage.getResultCode() == 0) {
                PushLog.d(TAG, "COMMAND_UNSUBSCRIBE_TOPIC success");
                return;
            } else {
                PushLog.e(TAG, "COMMAND_UNSUBSCRIBE_TOPIC failed");
                return;
            }
        }
        if (!MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)) {
            String reason = miPushCommandMessage.getReason();
            PushLog.d(TAG, "reason=" + reason);
            return;
        }
        if (miPushCommandMessage.getResultCode() == 0) {
            this.mStartTime = str2;
            this.mEndTime = str;
            PushLog.d(TAG, "mStartTime=" + this.mStartTime);
            PushLog.d(TAG, "mEndTime=" + this.mEndTime);
            return;
        }
        PushLog.e(TAG, "COMMAND_SET_ACCEPT_TIME failed");
    }

    @Override // com.xiaomi.mipush.sdk.PushMessageReceiver
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage miPushCommandMessage) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "onReceiveRegisterResult");
        PushLog.i(TAG, "command message=" + miPushCommandMessage);
        String command = miPushCommandMessage.getCommand();
        List<String> commandArguments = miPushCommandMessage.getCommandArguments();
        String str = (commandArguments == null || commandArguments.size() <= 0) ? null : commandArguments.get(0);
        PushLog.d(TAG, "command=" + command);
        PushLog.d(TAG, "cmdArg1=" + str);
        if (!"register".equals(command)) {
            String reason = miPushCommandMessage.getReason();
            PushLog.d(TAG, "reason=" + reason);
            return;
        }
        if (miPushCommandMessage.getResultCode() == 0) {
            this.mRegId = str;
            PushLog.d(TAG, "mRegId=" + this.mRegId);
            PushManager.getInstance().setRegistrationID(context, "miui", this.mRegId);
            broadcastRegid(context, this.mRegId);
            return;
        }
        PushLog.e(TAG, "COMMAND_REGISTER failed");
    }

    private void broadcastRegid(Context context, String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Intent intentCreateNewIDIntent = PushClientReceiver.createNewIDIntent();
        intentCreateNewIDIntent.putExtra(PushConstantsImpl.INTENT_DEVID_NAME, str);
        intentCreateNewIDIntent.setPackage(context.getPackageName());
        PushLog.d(TAG, "broadcast regid:" + str);
        context.sendBroadcast(intentCreateNewIDIntent, context.getPackageName() + ".permission.ngpush");
    }
}