package com.netease.ntunisdk.lbs;

import android.location.Location;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.xiaomi.gamecenter.sdk.statistics.OneTrackParams;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class LocationReporter {
    private static final String TAG = "LocationReporter";
    private static Location secondaryLocation;

    public static void setSecondaryLocation(Location location) {
        secondaryLocation = location;
    }

    public static void reportLocation(String str, String str2, double d, double d2, boolean z) throws JSONException {
        if (z) {
            doReport(str, str2, d, d2);
        } else {
            Location location = secondaryLocation;
            if (location != null) {
                doReport(str, str2, location.getLongitude(), secondaryLocation.getLatitude());
            } else {
                UniSdkUtils.e(TAG, "reportLocation -> location is invalid");
            }
        }
        secondaryLocation = null;
    }

    private static void doReport(String str, String str2, double d, double d2) throws JSONException {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("name", "lbs");
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("longitude", d);
            jSONObject2.put("latitude", d2);
            jSONObject.put("spec", jSONObject2);
            JSONObject jSONObject3 = new JSONObject();
            jSONObject3.put(OneTrackParams.XMSdkParams.STEP, "extendFunc." + str2);
            jSONObject3.put("func", str);
            jSONObject3.put("upload_type", "unisdk");
            jSONObject3.put("channel", "personal_info_list");
            jSONObject3.put("methodId", "uploadPersonalInfoEvent");
            jSONObject3.put("event", jSONObject);
            SdkMgr.getInst().ntExtendFunc(jSONObject3.toString());
        } catch (Exception e) {
            if (UniSdkUtils.isDebug) {
                e.printStackTrace();
            }
            UniSdkUtils.e(TAG, "reportLocation -> e: " + e.getMessage());
        }
    }
}