package com.netease.cc.ccplayerwrapper;

import com.facebook.react.animated.InterpolationAnimatedNode;
import com.netease.rnccplayer.VideoViewManager;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes4.dex */
public class UserInfo {
    public int eid = 0;
    public long uid = 0;
    public int ccid = 0;
    public String urs = "";
    public int templateType = 0;
    public int roomId = 0;
    public int subId = 0;
    public int gametype = 0;
    public int panorama = 0;
    public int context = 0;
    public String identity = "";
    public String udid = "-2";
    public String sid = "-2";
    public String macAddr = "";
    public String version = "";
    public int netType = 0;
    public String unisdkDeviceId = "-2";
    public boolean isPortrait = false;
    public String entrance = "-2";
    public String extraLog = "{}";
    public String extraDescOutJson = "{}";
    public String statStartAndIntervalJson = "{}";

    public UserInfo() {
    }

    public void copy(UserInfo userInfo) {
        if (userInfo == null) {
            return;
        }
        this.eid = userInfo.eid;
        this.uid = userInfo.uid;
        this.ccid = userInfo.ccid;
        this.urs = userInfo.urs;
        this.templateType = userInfo.templateType;
        this.roomId = userInfo.roomId;
        this.subId = userInfo.subId;
        this.gametype = userInfo.gametype;
        this.panorama = userInfo.panorama;
        this.context = userInfo.context;
        this.identity = userInfo.identity;
        this.udid = userInfo.udid;
        this.unisdkDeviceId = userInfo.unisdkDeviceId;
        this.sid = userInfo.sid;
        this.macAddr = userInfo.macAddr;
        this.version = userInfo.version;
        this.netType = userInfo.netType;
        this.isPortrait = userInfo.isPortrait;
        this.entrance = userInfo.entrance;
        this.extraLog = userInfo.extraLog;
        this.extraDescOutJson = userInfo.extraDescOutJson;
        this.statStartAndIntervalJson = userInfo.statStartAndIntervalJson;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(Constants.KEY_EID, this.eid);
            jSONObject.put("uid", this.uid);
            jSONObject.put(Constants.KEY_CCID, this.ccid);
            jSONObject.put("urs", this.urs);
            jSONObject.put("templateType", this.templateType);
            jSONObject.put(Constants.KEY_ROOM_ID, this.roomId);
            jSONObject.put(Constants.KEY_CHANNEL_ID, this.subId);
            jSONObject.put(Constants.KEY_GAME_TYPE, this.gametype);
            jSONObject.put("panorama", this.panorama);
            jSONObject.put("context", this.context);
            jSONObject.put(InterpolationAnimatedNode.EXTRAPOLATE_TYPE_IDENTITY, this.identity);
            jSONObject.put("udid", this.udid);
            jSONObject.put("unisdk_device_id", this.unisdkDeviceId);
            jSONObject.put(VideoViewManager.PROP_SID, this.sid);
            jSONObject.put("macAddr", this.macAddr);
            jSONObject.put("version", this.version);
            jSONObject.put("netType", this.netType);
            jSONObject.put("isPortrait", this.isPortrait);
            jSONObject.put("entrance", this.entrance);
            jSONObject.put(VideoViewManager.PROP_EXTRA_LOG, this.extraLog);
            jSONObject.put("extraDescOutJson", this.extraDescOutJson);
            jSONObject.put("statStartAndIntervalJson", this.statStartAndIntervalJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jSONObject;
    }

    public String toString() {
        return "UserInfo{eid=" + this.eid + ", uid=" + this.uid + ", ccid=" + this.ccid + ", urs='" + this.urs + "', templateType=" + this.templateType + ", roomId=" + this.roomId + ", subId=" + this.subId + ", gametype=" + this.gametype + ", panorama=" + this.panorama + ", context=" + this.context + ", identity='" + this.identity + "', udid='" + this.udid + "', unisdk_device_id='" + this.unisdkDeviceId + "', sid='" + this.sid + "', macAddr='" + this.macAddr + "', version='" + this.version + "', netType=" + this.netType + ", isPortrait=" + this.isPortrait + ", entrance='" + this.entrance + "', extraLog='" + this.extraLog + "', extraDescOutJson='" + this.extraDescOutJson + "', statStartAndIntervalJson='" + this.statStartAndIntervalJson + "'}";
    }

    public void updateFromJson(JSONObject jSONObject) {
        if (jSONObject == null) {
            return;
        }
        Iterator<String> itKeys = jSONObject.keys();
        while (itKeys.hasNext()) {
            String next = itKeys.next();
            if (next.equals(Constants.KEY_EID)) {
                this.eid = jSONObject.optInt(next);
            } else if (next.equals("uid")) {
                this.uid = jSONObject.optInt(next);
            } else if (next.equals(Constants.KEY_CCID)) {
                this.ccid = jSONObject.optInt(next);
            } else if (next.equals("urs")) {
                this.urs = jSONObject.optString(next);
            } else if (next.equals("templateType")) {
                this.templateType = jSONObject.optInt(next);
            } else if (next.equals(Constants.KEY_ROOM_ID)) {
                this.roomId = jSONObject.optInt(next);
            } else if (next.equals(Constants.KEY_CHANNEL_ID)) {
                this.subId = jSONObject.optInt(next);
            } else if (next.equals(Constants.KEY_GAME_TYPE)) {
                this.gametype = jSONObject.optInt(next);
            } else if (next.equals("panorama")) {
                this.panorama = jSONObject.optInt(next);
            } else if (next.equals("context")) {
                this.context = jSONObject.optInt(next);
            } else if (next.equals(InterpolationAnimatedNode.EXTRAPOLATE_TYPE_IDENTITY)) {
                this.identity = jSONObject.optString(next);
            } else if (next.equals("udid")) {
                this.udid = jSONObject.optString(next);
            } else if (next.equals("unisdk_device_id")) {
                this.unisdkDeviceId = jSONObject.optString(next);
            } else if (next.equals(VideoViewManager.PROP_SID)) {
                this.sid = jSONObject.optString(next);
            } else if (next.equals("macAddr")) {
                this.macAddr = jSONObject.optString(next);
            } else if (next.equals("version")) {
                this.version = jSONObject.optString(next);
            } else if (next.equals("netType")) {
                this.netType = jSONObject.optInt(next);
            } else if (next.equals("isPortrait")) {
                this.isPortrait = jSONObject.optBoolean(next);
            } else if (next.equals("entrance")) {
                this.entrance = jSONObject.optString(next);
            } else if (next.equals(VideoViewManager.PROP_EXTRA_LOG)) {
                this.extraLog = jSONObject.optString(next);
            } else if (next.equals("extraDescOutJson")) {
                this.extraDescOutJson = jSONObject.optString(next);
            } else if (next.equals("statStartAndIntervalJson")) {
                this.statStartAndIntervalJson = jSONObject.optString(next);
            }
        }
    }

    public UserInfo(UserInfo userInfo) {
        copy(userInfo);
    }
}