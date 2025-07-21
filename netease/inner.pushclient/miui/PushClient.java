package com.netease.inner.pushclient.miui;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.netease.ntunisdk.base.PatchPlaceholder;
import com.netease.push.utils.NotifyMessageImpl;
import com.netease.push.utils.PushConstantsImpl;
import com.netease.push.utils.PushLog;
import com.netease.push.utils.StrUtil;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageHelper;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class PushClient {
    private static final String TAG = "NGPush_Miui" + PushClient.class.getSimpleName();

    private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, PatchPlaceholder.class.getSimpleName());
    }

    public PushClient() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "PushClient constructed");
    }

    public static void registerPush(Context context, String str, String str2) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, String.format("registerPush, appid:%s, appkey:%s", str, str2));
        MiPushClient.registerPush(context, str, str2);
    }

    public static NotifyMessageImpl getNotifyMessageFromIntent(Intent intent) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String strJoin;
        String str;
        String strOptString;
        String string;
        PushLog.i(TAG, "getNotifyMessageFromIntent");
        MiPushMessage miPushMessage = (MiPushMessage) intent.getSerializableExtra(PushMessageHelper.KEY_MESSAGE);
        if (miPushMessage == null) {
            return null;
        }
        String topic = miPushMessage.getTopic();
        String alias = miPushMessage.getAlias();
        String title = miPushMessage.getTitle();
        String description = miPushMessage.getDescription();
        String content = miPushMessage.getContent();
        int notifyId = miPushMessage.getNotifyId();
        new JSONObject();
        PushLog.d(TAG, "title=" + title);
        PushLog.d(TAG, "msg=" + description);
        PushLog.d(TAG, "ext=" + content);
        PushLog.d(TAG, "topic=" + topic);
        PushLog.d(TAG, "alias=" + alias);
        PushLog.d(TAG, "notifyid=" + notifyId);
        String[] strArrSplit = TextUtils.split(content, "\\|");
        String strOptString2 = "";
        if (strArrSplit.length >= 2) {
            str = strArrSplit[strArrSplit.length - 1];
            strJoin = TextUtils.join(PushConstantsImpl.COMMON_PARAMETER_SEPARATOR2, StrUtil.copyOfRange(strArrSplit, 0, strArrSplit.length - 1));
        } else {
            strJoin = content;
            str = "";
        }
        Map<String, String> extra = miPushMessage.getExtra();
        try {
            strOptString = "";
            string = strOptString;
            for (String str2 : extra.keySet()) {
                try {
                    PushLog.d(TAG, "MiPushMessage key=" + str2 + "MiPushMessage value=" + extra.get(str2));
                    if ("payload".equals(str2)) {
                        String str3 = extra.get(str2);
                        try {
                            JSONObject jSONObject = new JSONObject(str3);
                            PushLog.d(TAG, "passJsonString=" + str3);
                            PushLog.d(TAG, "passJson=" + jSONObject);
                            JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("ngpush");
                            PushLog.d(TAG, "ngpushJson=" + jSONObjectOptJSONObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if ("data".equals(str2)) {
                        PushLog.d(TAG, "payload value=" + extra.get(str2));
                        JSONObject jSONObject2 = new JSONObject(extra.get(str2));
                        string = jSONObject2.toString();
                        JSONObject jSONObjectOptJSONObject2 = jSONObject2.optJSONObject("system_content");
                        jSONObject2.optString("custom_content");
                        strOptString2 = jSONObjectOptJSONObject2.optString("plan_id");
                        strOptString = jSONObjectOptJSONObject2.optString("push_id");
                    }
                } catch (Exception e2) {
                    e = e2;
                    e.printStackTrace();
                    NotifyMessageImpl notifyMessageImpl = new NotifyMessageImpl(title, description, strJoin, notifyId, str, "", "miui", "", false, strOptString, strOptString2);
                    notifyMessageImpl.passJsonString = string;
                    return notifyMessageImpl;
                }
            }
        } catch (Exception e3) {
            e = e3;
            strOptString = "";
            string = strOptString;
        }
        NotifyMessageImpl notifyMessageImpl2 = new NotifyMessageImpl(title, description, strJoin, notifyId, str, "", "miui", "", false, strOptString, strOptString2);
        notifyMessageImpl2.passJsonString = string;
        return notifyMessageImpl2;
    }
}