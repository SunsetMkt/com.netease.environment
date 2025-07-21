package com.netease.ngrtc;

import android.util.Log;
import androidx.browser.customtabs.CustomTabsCallback;
import com.netease.ngrtc.utils.StrUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes4.dex */
public class ParticipantInfo {
    private static final String TAG = "NGRTC_ParticipantInfo";
    public long atime;
    public List<String> inputMuteMemberList = new ArrayList();
    public boolean inputMuted;
    public String name;
    public boolean online;
    public boolean outputMuted;
    public String uid;
    public long uidGateway;

    public String toJson() {
        try {
            return toJsonObj().toString();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            return "";
        }
    }

    public JSONObject toJsonObj() {
        try {
            HashMap map = new HashMap();
            map.put("uid", this.uid);
            map.put("uidGateway", Long.valueOf(this.uidGateway));
            map.put("name", this.name);
            map.put("inputMuted", Boolean.valueOf(this.inputMuted));
            map.put("outputMuted", Boolean.valueOf(this.outputMuted));
            map.put(CustomTabsCallback.ONLINE_EXTRAS_KEY, Boolean.valueOf(this.online));
            map.put("atime", Long.valueOf(this.atime));
            return StrUtil.mapToJson(map);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
            return null;
        }
    }

    public void unmarshal(String str) {
        try {
            unmarshalFromObj(new JSONObject(str));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
    }

    public void unmarshalFromObj(JSONObject jSONObject) {
        try {
            this.uid = jSONObject.optString("uid");
            this.uidGateway = jSONObject.optLong("uidGateway");
            this.name = jSONObject.optString("name");
            this.inputMuted = jSONObject.optBoolean("inputMuted");
            this.outputMuted = jSONObject.optBoolean("outputMuted");
            this.online = jSONObject.optBoolean(CustomTabsCallback.ONLINE_EXTRAS_KEY);
            this.atime = jSONObject.optLong("atime");
            JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("inputMutedMembers");
            if (jSONArrayOptJSONArray != null) {
                for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
                    this.inputMuteMemberList.add(jSONArrayOptJSONArray.optString(i));
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
    }

    public static ArrayList<ParticipantInfo> unmarshalArr(JSONArray jSONArray) {
        ArrayList<ParticipantInfo> arrayList = new ArrayList<>();
        for (int i = 0; i < jSONArray.length(); i++) {
            try {
                ParticipantInfo participantInfo = new ParticipantInfo();
                participantInfo.unmarshalFromObj(jSONArray.getJSONObject(i));
                arrayList.add(participantInfo);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
                e.printStackTrace();
            }
        }
        return arrayList;
    }

    public String toString() {
        return String.valueOf(String.valueOf("uid:" + this.uid) + " ssrc:" + this.uidGateway) + " name:" + this.name;
    }
}