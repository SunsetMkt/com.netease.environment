package com.netease.ntunisdk.modules.permission.core;

import android.text.TextUtils;
import android.util.Log;
import com.netease.ntunisdk.modules.permission.PermissionContext;
import com.netease.ntunisdk.modules.permission.R;
import com.netease.ntunisdk.modules.permission.utils.PermissionTextUtils;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class PermissionHandler {
    private static final String TAG = "PermissionHandler";
    private String firstText;
    private boolean gotoSetting;
    private String gotoSettingReason;
    private String negativeText;
    private String[] permissionArray;
    private String permissionName;
    private PermissionRequest permissionRequest;
    private String positiveText;
    private String refuseTip;
    private boolean showDialog;
    private String tipSetting;

    public PermissionHandler(PermissionContext permissionContext, PermissionListener permissionListener, JSONObject jSONObject) {
        try {
            this.gotoSetting = true;
            this.firstText = jSONObject.getString("firstText");
            this.refuseTip = jSONObject.optString("refuseTip", PermissionTextUtils.getString(permissionContext.getContext(), R.string.netease_permissionkit_sdk__refuse_tip));
            this.showDialog = jSONObject.optBoolean("showDialog", true);
            if (jSONObject.has("positiveText") && !TextUtils.isEmpty(jSONObject.getString("positiveText"))) {
                this.positiveText = jSONObject.getString("positiveText");
            } else {
                this.positiveText = PermissionTextUtils.getString(permissionContext.getContext(), R.string.netease_permissionkit_sdk__continue);
            }
            if (jSONObject.has("negativeText") && !TextUtils.isEmpty(jSONObject.getString("negativeText"))) {
                this.negativeText = jSONObject.getString("negativeText");
            } else {
                this.negativeText = PermissionTextUtils.getString(permissionContext.getContext(), R.string.netease_permissionkit_sdk__cancel);
            }
            this.permissionName = jSONObject.optString("permissionName");
            this.permissionArray = this.permissionName.split(",");
            this.gotoSettingReason = jSONObject.optString("gotoSettingReason", "");
            this.permissionRequest = new PermissionRequest(jSONObject, this.permissionArray, permissionContext, permissionListener);
        } catch (JSONException e) {
            Log.d(TAG, "jsonException: " + e);
        }
    }

    public String getFirstText() {
        return this.firstText;
    }

    public String getPositiveText() {
        return this.positiveText;
    }

    public String getNegativeText() {
        return this.negativeText;
    }

    public String getPermissionName() {
        return this.permissionName;
    }

    public String[] getPermissionArray() {
        return this.permissionArray;
    }

    public boolean getGotoSetting() {
        return this.gotoSetting;
    }

    public String getGotoSettingReason() {
        return this.gotoSettingReason;
    }

    public boolean getShowDialog() {
        return this.showDialog;
    }

    public void setShowDialog(boolean z) {
        this.showDialog = z;
    }

    public PermissionRequest getPermissionRequestProxy() {
        return this.permissionRequest;
    }

    public String getRefuseTip() {
        return this.refuseTip;
    }

    public void setTipSetting(String str) {
        this.tipSetting = str;
    }

    public String getTipSetting() {
        return this.tipSetting;
    }
}