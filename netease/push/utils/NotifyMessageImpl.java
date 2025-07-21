package com.netease.push.utils;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import androidx.core.app.NotificationCompat;
import com.facebook.common.util.UriUtil;
import com.netease.ntunisdk.base.PatchPlaceholder;
import java.lang.reflect.InvocationTargetException;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class NotifyMessageImpl implements Parcelable {
    public String actionParam;
    public String actionType;
    public String big_image_url;
    public boolean fromMpay;
    public String mChannelId;
    public String mChannelName;
    public String mExt;
    public String mGroupId;
    public String mGroupName;
    public int mIcon;
    public String mMsg;
    public boolean mNative;
    public int mNotifyid;
    public String mReqid;
    public String mServiceType;
    public String mSound;
    public long mTime;
    public String mTitle;
    public JSONObject ngpushJson;
    public String passJsonString;
    public String plan_id;
    public String push_id;
    public boolean silent;
    public String small_image_url;
    private static final String TAG = "NGPush_" + NotifyMessageImpl.class.getSimpleName();
    public static String notifyActivityName = "";
    public static final Parcelable.Creator<NotifyMessageImpl> CREATOR = new Parcelable.Creator<NotifyMessageImpl>() { // from class: com.netease.push.utils.NotifyMessageImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NotifyMessageImpl[] newArray(int i) {
            return new NotifyMessageImpl[i];
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NotifyMessageImpl createFromParcel(Parcel parcel) {
            return new NotifyMessageImpl(parcel);
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, PatchPlaceholder.class.getSimpleName());
    }

    public NotifyMessageImpl() {
        this.mTitle = "";
        this.mMsg = "";
        this.mExt = "";
        this.mNotifyid = 0;
        this.mReqid = "";
        this.mSound = "";
        this.mServiceType = "niepush";
        this.mIcon = 0;
        this.mNative = false;
        this.mTime = 0L;
        this.passJsonString = "";
        this.ngpushJson = new JSONObject();
        this.mGroupId = "group_unisdk_ngpush_id";
        this.mGroupName = "group_unisdk_ngpush_name";
        this.mChannelId = "channel_unisdk_ngpush_id";
        this.mChannelName = "channel_unisdk_ngpush_name";
        this.silent = false;
        this.big_image_url = "";
        this.small_image_url = "";
        this.push_id = "";
        this.plan_id = "";
        this.fromMpay = false;
        this.actionType = "";
        this.actionParam = "";
        clear();
    }

    public NotifyMessageImpl(String str, String str2, String str3, int i, String str4, String str5) {
        this.mTitle = "";
        this.mMsg = "";
        this.mExt = "";
        this.mNotifyid = 0;
        this.mReqid = "";
        this.mSound = "";
        this.mServiceType = "niepush";
        this.mIcon = 0;
        this.mNative = false;
        this.mTime = 0L;
        this.passJsonString = "";
        this.ngpushJson = new JSONObject();
        this.mGroupId = "group_unisdk_ngpush_id";
        this.mGroupName = "group_unisdk_ngpush_name";
        this.mChannelId = "channel_unisdk_ngpush_id";
        this.mChannelName = "channel_unisdk_ngpush_name";
        this.silent = false;
        this.big_image_url = "";
        this.small_image_url = "";
        this.push_id = "";
        this.plan_id = "";
        this.fromMpay = false;
        this.actionType = "";
        this.actionParam = "";
        this.mTitle = str;
        this.mMsg = str2;
        this.mExt = str3;
        this.mNotifyid = i;
        this.mReqid = str4;
        this.mServiceType = str5;
    }

    public NotifyMessageImpl(String str, String str2, String str3, int i, String str4, String str5, String str6) {
        this.mTitle = "";
        this.mMsg = "";
        this.mExt = "";
        this.mNotifyid = 0;
        this.mReqid = "";
        this.mSound = "";
        this.mServiceType = "niepush";
        this.mIcon = 0;
        this.mNative = false;
        this.mTime = 0L;
        this.passJsonString = "";
        this.ngpushJson = new JSONObject();
        this.mGroupId = "group_unisdk_ngpush_id";
        this.mGroupName = "group_unisdk_ngpush_name";
        this.mChannelId = "channel_unisdk_ngpush_id";
        this.mChannelName = "channel_unisdk_ngpush_name";
        this.silent = false;
        this.big_image_url = "";
        this.small_image_url = "";
        this.push_id = "";
        this.plan_id = "";
        this.fromMpay = false;
        this.actionType = "";
        this.actionParam = "";
        this.mTitle = str;
        this.mMsg = str2;
        this.mExt = str3;
        this.mNotifyid = i;
        this.mReqid = str4;
        this.mSound = str5;
        this.mServiceType = str6;
    }

    public NotifyMessageImpl(String str, String str2, String str3, int i, String str4, String str5, String str6, String str7) {
        this.mTitle = "";
        this.mMsg = "";
        this.mExt = "";
        this.mNotifyid = 0;
        this.mReqid = "";
        this.mSound = "";
        this.mServiceType = "niepush";
        this.mIcon = 0;
        this.mNative = false;
        this.mTime = 0L;
        this.passJsonString = "";
        this.ngpushJson = new JSONObject();
        this.mGroupId = "group_unisdk_ngpush_id";
        this.mGroupName = "group_unisdk_ngpush_name";
        this.mChannelId = "channel_unisdk_ngpush_id";
        this.mChannelName = "channel_unisdk_ngpush_name";
        this.silent = false;
        this.big_image_url = "";
        this.small_image_url = "";
        this.push_id = "";
        this.plan_id = "";
        this.fromMpay = false;
        this.actionType = "";
        this.actionParam = "";
        this.mTitle = str;
        this.mMsg = str2;
        this.mExt = str3;
        this.mNotifyid = i;
        this.mReqid = str4;
        this.mSound = str5;
        this.mServiceType = str6;
        this.passJsonString = str7;
    }

    public NotifyMessageImpl(String str, String str2, String str3, int i, String str4, String str5, String str6, String str7, boolean z, String str8, String str9) {
        this.mTitle = "";
        this.mMsg = "";
        this.mExt = "";
        this.mNotifyid = 0;
        this.mReqid = "";
        this.mSound = "";
        this.mServiceType = "niepush";
        this.mIcon = 0;
        this.mNative = false;
        this.mTime = 0L;
        this.passJsonString = "";
        this.ngpushJson = new JSONObject();
        this.mGroupId = "group_unisdk_ngpush_id";
        this.mGroupName = "group_unisdk_ngpush_name";
        this.mChannelId = "channel_unisdk_ngpush_id";
        this.mChannelName = "channel_unisdk_ngpush_name";
        this.silent = false;
        this.big_image_url = "";
        this.small_image_url = "";
        this.push_id = "";
        this.plan_id = "";
        this.fromMpay = false;
        this.actionType = "";
        this.actionParam = "";
        this.mTitle = str;
        this.mMsg = str2;
        this.mExt = str3;
        this.mNotifyid = i;
        this.mReqid = str4;
        this.mSound = str5;
        this.mServiceType = str6;
        this.passJsonString = str7;
        this.silent = z;
        this.push_id = str8;
        this.plan_id = str9;
    }

    public NotifyMessageImpl(String str, String str2, String str3, int i, String str4, String str5, String str6, String str7, JSONObject jSONObject) {
        this.mTitle = "";
        this.mMsg = "";
        this.mExt = "";
        this.mNotifyid = 0;
        this.mReqid = "";
        this.mSound = "";
        this.mServiceType = "niepush";
        this.mIcon = 0;
        this.mNative = false;
        this.mTime = 0L;
        this.passJsonString = "";
        this.ngpushJson = new JSONObject();
        this.mGroupId = "group_unisdk_ngpush_id";
        this.mGroupName = "group_unisdk_ngpush_name";
        this.mChannelId = "channel_unisdk_ngpush_id";
        this.mChannelName = "channel_unisdk_ngpush_name";
        this.silent = false;
        this.big_image_url = "";
        this.small_image_url = "";
        this.push_id = "";
        this.plan_id = "";
        this.fromMpay = false;
        this.actionType = "";
        this.actionParam = "";
        this.mTitle = str;
        this.mMsg = str2;
        this.mExt = str3;
        this.mNotifyid = i;
        this.mReqid = str4;
        this.mSound = str5;
        this.mServiceType = str6;
        this.passJsonString = str7;
        this.ngpushJson = jSONObject;
    }

    public NotifyMessageImpl(Parcel parcel) {
        this.mTitle = "";
        this.mMsg = "";
        this.mExt = "";
        this.mNotifyid = 0;
        this.mReqid = "";
        this.mSound = "";
        this.mServiceType = "niepush";
        this.mIcon = 0;
        this.mNative = false;
        this.mTime = 0L;
        this.passJsonString = "";
        this.ngpushJson = new JSONObject();
        this.mGroupId = "group_unisdk_ngpush_id";
        this.mGroupName = "group_unisdk_ngpush_name";
        this.mChannelId = "channel_unisdk_ngpush_id";
        this.mChannelName = "channel_unisdk_ngpush_name";
        this.silent = false;
        this.big_image_url = "";
        this.small_image_url = "";
        this.push_id = "";
        this.plan_id = "";
        this.fromMpay = false;
        this.actionType = "";
        this.actionParam = "";
        readFromParcel(parcel);
    }

    public void clear() {
        this.mTitle = "";
        this.mMsg = "";
        this.mExt = "";
        this.mNotifyid = 0;
        this.mReqid = "";
        this.mSound = "";
        this.mServiceType = "niepush";
        this.mIcon = 0;
        this.mNative = false;
        this.passJsonString = "";
        this.ngpushJson = new JSONObject();
    }

    public String toString() {
        return "title:" + this.mTitle + ", msg:" + this.mMsg + ", ext:" + this.mExt + ", notifyid:" + this.mNotifyid + ", reqid:" + this.mReqid + ", sound:" + this.mSound + ", serviceType:" + this.mServiceType + ", native:" + this.mNative + ",groupId:" + this.mGroupId + ",groupName:" + this.mGroupName + ",channelId:" + this.mChannelId + ",channelName:" + this.mChannelName + ",passJsonString:" + this.passJsonString + ",ngpushJson:" + this.ngpushJson + ",big_image_url" + this.big_image_url + ",small_image_url" + this.small_image_url + ",silent" + this.silent;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setTitle(String str) {
        this.mTitle = str;
    }

    public String getMsg() {
        return this.mMsg;
    }

    public void setMsg(String str) {
        this.mMsg = str;
    }

    public String getExt() {
        return this.mExt;
    }

    public void setExt(String str) {
        this.mExt = str;
    }

    public int getIcon() {
        return this.mIcon;
    }

    public void setIcon(int i) {
        this.mIcon = i;
    }

    public int getNotifyid() {
        return this.mNotifyid;
    }

    public String getReqid() {
        return this.mReqid;
    }

    public String getSound() {
        return this.mSound;
    }

    public void setSound(String str) {
        this.mSound = str;
    }

    public String getServiceType() {
        return this.mServiceType;
    }

    public boolean getNative() {
        return this.mNative;
    }

    public String getGroupId() {
        return this.mGroupId;
    }

    public void setGroupId(String str) {
        this.mGroupId = str;
    }

    public String getGroupName() {
        return this.mGroupName;
    }

    public void setGroupName(String str) {
        this.mGroupName = str;
    }

    public String getChannelId() {
        return this.mChannelId;
    }

    public void setChannelId(String str) {
        this.mChannelId = str;
    }

    public String getChannelName() {
        return this.mChannelName;
    }

    public void setChannelName(String str) {
        this.mChannelName = str;
    }

    public String getPassJsonString() {
        return this.passJsonString;
    }

    public void setPassJsonString(String str) {
        this.passJsonString = str;
    }

    public String getNotifyActivityName() {
        return notifyActivityName;
    }

    public void setNotifyActivityName(String str) {
        notifyActivityName = str;
    }

    public boolean isSilent() {
        return this.silent;
    }

    public void setSilent(boolean z) {
        this.silent = z;
    }

    public JSONObject getNgpushJson() {
        return this.ngpushJson;
    }

    public void setNgpushJson(JSONObject jSONObject) {
        this.ngpushJson = jSONObject;
    }

    public String getBig_image_url() {
        return this.big_image_url;
    }

    public void setBig_image_url(String str) {
        this.big_image_url = str;
    }

    public String getSmall_image_url() {
        return this.small_image_url;
    }

    public void setSmall_image_url(String str) {
        this.small_image_url = str;
    }

    public String getPush_id() {
        return this.push_id;
    }

    public void setPush_id(String str) {
        this.push_id = str;
    }

    public String getPlan_id() {
        return this.plan_id;
    }

    public void setPlan_id(String str) {
        this.plan_id = str;
    }

    public boolean isFromMpay() {
        return this.fromMpay;
    }

    public void setFromMpay(boolean z) {
        this.fromMpay = z;
    }

    public String getActionType() {
        return this.actionType;
    }

    public void setActionType(String str) {
        this.actionType = str;
    }

    public String getActionParam() {
        return this.actionParam;
    }

    public void setActionParam(String str) {
        this.actionParam = str;
    }

    public String writeToJsonString() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("title", this.mTitle);
        jSONObject.put("msg", this.mMsg);
        jSONObject.put(UriUtil.LOCAL_CONTENT_SCHEME, this.mMsg);
        jSONObject.put("passJsonString", this.passJsonString);
        jSONObject.put("ngpushJson", this.ngpushJson);
        jSONObject.put("ext", this.mExt);
        jSONObject.put("icon", this.mIcon);
        jSONObject.put("notify_id", this.mNotifyid);
        jSONObject.put("reqid", this.mReqid);
        jSONObject.put(PushConstantsImpl.NOTIFICATION_SOUND, this.mSound);
        jSONObject.put("service_type", this.mServiceType);
        jSONObject.put(PushConstantsImpl.NOTIFICATION_GROUP_ID, this.mGroupId);
        jSONObject.put(PushConstantsImpl.NOTIFICATION_GROUP_NAME, this.mGroupName);
        jSONObject.put("channel_id", this.mChannelId);
        jSONObject.put(PushConstantsImpl.NOTIFICATION_CHANNEL_NAME, this.mChannelName);
        jSONObject.put("big_image_url", this.big_image_url);
        jSONObject.put("small_image_url", this.small_image_url);
        jSONObject.put("push_id", this.push_id);
        jSONObject.put("plan_id", this.plan_id);
        jSONObject.put("fromMpay", this.fromMpay);
        jSONObject.put(NotificationCompat.GROUP_KEY_SILENT, this.silent);
        jSONObject.put("actionType", this.actionType);
        jSONObject.put("actionParam", this.actionParam);
        return jSONObject.toString();
    }

    public static NotifyMessageImpl readFromJsonString(String str) throws IllegalAccessException, JSONException, IllegalArgumentException, InvocationTargetException {
        JSONObject jSONObject = new JSONObject(str);
        PushLog.d(TAG, "jsonObject:" + jSONObject.toString());
        String strOptString = jSONObject.optString("title");
        String strOptString2 = jSONObject.optString("msg");
        if (TextUtils.isEmpty(strOptString2)) {
            strOptString2 = jSONObject.optString(UriUtil.LOCAL_CONTENT_SCHEME);
        }
        String str2 = strOptString2;
        String strOptString3 = jSONObject.optString("ext");
        int iOptInt = jSONObject.optInt("icon", 0);
        int iOptInt2 = jSONObject.optInt("notify_id", 0);
        String strOptString4 = jSONObject.optString("reqid");
        String strOptString5 = jSONObject.optString(PushConstantsImpl.NOTIFICATION_SOUND);
        String strOptString6 = jSONObject.optString("service_type");
        String strOptString7 = jSONObject.optString("passJsonString");
        PushLog.d(TAG, "PassJsonString:" + strOptString7);
        JSONObject jSONObject2 = new JSONObject();
        if (strOptString7 != null) {
            try {
                jSONObject2 = new JSONObject(strOptString7).optJSONObject("ngpush");
            } catch (JSONException e) {
                PushLog.d(TAG, "ngpushJson parse error:" + e);
            }
        }
        JSONObject jSONObject3 = jSONObject2 == null ? new JSONObject() : jSONObject2;
        PushLog.d(TAG, "ngpushJson:" + jSONObject3);
        String strOptString8 = jSONObject.optString("channel_id");
        String strOptString9 = jSONObject.optString(PushConstantsImpl.NOTIFICATION_CHANNEL_NAME);
        String strOptString10 = jSONObject.optString(PushConstantsImpl.NOTIFICATION_GROUP_ID);
        String strOptString11 = jSONObject.optString(PushConstantsImpl.NOTIFICATION_GROUP_NAME);
        NotifyMessageImpl notifyMessageImpl = new NotifyMessageImpl(strOptString, str2, strOptString3, iOptInt2, strOptString4, strOptString5, strOptString6, strOptString7, jSONObject3);
        notifyMessageImpl.mGroupId = strOptString10;
        notifyMessageImpl.mGroupName = strOptString11;
        notifyMessageImpl.mChannelName = strOptString9;
        notifyMessageImpl.mChannelId = strOptString8;
        notifyMessageImpl.mIcon = iOptInt;
        notifyMessageImpl.big_image_url = jSONObject.optString("big_image_url");
        notifyMessageImpl.small_image_url = jSONObject.optString("small_image_url");
        notifyMessageImpl.push_id = jSONObject.optString("push_id");
        notifyMessageImpl.plan_id = jSONObject.optString("plan_id");
        notifyMessageImpl.fromMpay = jSONObject.optBoolean("fromMpay");
        notifyMessageImpl.silent = jSONObject.optBoolean(NotificationCompat.GROUP_KEY_SILENT);
        notifyMessageImpl.actionType = jSONObject.optString("actionType");
        notifyMessageImpl.actionParam = jSONObject.optString("actionParam");
        return notifyMessageImpl;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mTitle);
        parcel.writeString(this.mMsg);
        parcel.writeString(this.mExt);
        parcel.writeInt(this.mIcon);
        parcel.writeInt(this.mNotifyid);
        parcel.writeString(this.mReqid);
        parcel.writeString(this.mServiceType);
    }

    public void readFromParcel(Parcel parcel) {
        this.mTitle = parcel.readString();
        this.mMsg = parcel.readString();
        this.mExt = parcel.readString();
        this.mIcon = parcel.readInt();
        this.mNotifyid = parcel.readInt();
        this.mReqid = parcel.readString();
        this.mServiceType = parcel.readString();
    }

    /* JADX WARN: Removed duplicated region for block: B:50:0x02cb  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0371  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x037a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.netease.push.utils.NotifyMessageImpl getFrom(android.content.Intent r19) throws java.lang.IllegalAccessException, org.json.JSONException, java.lang.ClassNotFoundException, java.lang.IllegalArgumentException, java.lang.reflect.InvocationTargetException {
        /*
            Method dump skipped, instructions count: 976
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.push.utils.NotifyMessageImpl.getFrom(android.content.Intent):com.netease.push.utils.NotifyMessageImpl");
    }

    public static NotifyMessageImpl getFrom(Activity activity) {
        return getFrom(activity.getIntent());
    }
}