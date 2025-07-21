package com.netease.ntunisdk.base.model;

import android.text.TextUtils;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.unilogger.global.Const;
import com.tencent.open.SocialConstants;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class SdkRole extends SdkModel {
    public String capability;
    public String gangid;
    public String gangname;
    public String hostid;
    public String hostname;
    public String menpaiid;
    public String menpainame;
    public String regionid;
    public String regionname;
    public String rolecreatetime;
    public String roleid;
    public String rolelevel;
    public String rolename;
    public String typeid;
    public String typename;
    public String viplevel;

    public SdkRole() {
    }

    public SdkRole(String str) {
        super(str);
    }

    @Override // com.netease.ntunisdk.base.model.SdkModel
    public void fromJson(JSONObject jSONObject) {
        this.roleid = jSONObject.optString(Const.CONFIG_KEY.ROLEID);
        this.rolename = jSONObject.optString("rolename");
        this.rolecreatetime = jSONObject.optString("rolecreatetime");
        this.hostid = jSONObject.optString("hostid");
        this.hostname = jSONObject.optString("hostname");
        this.rolelevel = jSONObject.optString("rolelevel");
        this.viplevel = jSONObject.optString("viplevel");
        this.typeid = jSONObject.optString(SocialConstants.PARAM_TYPE_ID);
        this.typename = jSONObject.optString("typename");
        this.menpaiid = jSONObject.optString("menpaiid");
        this.menpainame = jSONObject.optString("menpainame");
        this.capability = jSONObject.optString("capability");
        this.gangid = jSONObject.optString("gangid");
        this.gangname = jSONObject.optString("gangname");
        this.regionid = jSONObject.optString("regionid");
        this.regionname = jSONObject.optString("regionname");
    }

    @Override // com.netease.ntunisdk.base.model.SdkModel
    public Object wrapTo() {
        if (SdkMgr.getInst() == null) {
            return null;
        }
        if (!TextUtils.isEmpty(this.roleid)) {
            SdkMgr.getInst().setUserInfo(ConstProp.USERINFO_UID, this.roleid);
        }
        if (!TextUtils.isEmpty(this.rolename)) {
            SdkMgr.getInst().setUserInfo(ConstProp.USERINFO_NAME, this.rolename);
        }
        if (!TextUtils.isEmpty(this.rolecreatetime)) {
            SdkMgr.getInst().setUserInfo(ConstProp.USERINFO_ROLE_CTIME, this.rolecreatetime);
        }
        if (!TextUtils.isEmpty(this.hostid)) {
            SdkMgr.getInst().setUserInfo(ConstProp.USERINFO_HOSTID, this.hostid);
        }
        if (!TextUtils.isEmpty(this.hostname)) {
            SdkMgr.getInst().setUserInfo(ConstProp.USERINFO_HOSTNAME, this.hostname);
        }
        if (!TextUtils.isEmpty(this.rolelevel)) {
            SdkMgr.getInst().setUserInfo(ConstProp.USERINFO_GRADE, this.rolelevel);
        }
        if (!TextUtils.isEmpty(this.viplevel)) {
            SdkMgr.getInst().setUserInfo(ConstProp.USERINFO_VIP, this.viplevel);
        }
        if (!TextUtils.isEmpty(this.typeid)) {
            SdkMgr.getInst().setUserInfo(ConstProp.USERINFO_ROLE_TYPE_ID, this.typeid);
        }
        if (!TextUtils.isEmpty(this.typename)) {
            SdkMgr.getInst().setUserInfo(ConstProp.USERINFO_ROLE_TYPE_NAME, this.typename);
        }
        if (!TextUtils.isEmpty(this.menpaiid)) {
            SdkMgr.getInst().setUserInfo(ConstProp.USERINFO_MENPAI_ID, this.menpaiid);
        }
        if (!TextUtils.isEmpty(this.menpainame)) {
            SdkMgr.getInst().setUserInfo(ConstProp.USERINFO_MENPAI_NAME, this.menpainame);
            if (TextUtils.isEmpty(SdkMgr.getInst().getUserInfo(ConstProp.USERINFO_ORG))) {
                SdkMgr.getInst().setUserInfo(ConstProp.USERINFO_ORG, this.menpainame);
            }
        }
        if (!TextUtils.isEmpty(this.capability)) {
            SdkMgr.getInst().setUserInfo(ConstProp.USERINFO_CAPABILITY, this.capability);
        }
        if (!TextUtils.isEmpty(this.gangid)) {
            SdkMgr.getInst().setUserInfo(ConstProp.USERINFO_GANG_ID, this.gangid);
        }
        if (!TextUtils.isEmpty(this.gangname)) {
            SdkMgr.getInst().setUserInfo(ConstProp.USERINFO_GANG_NAME, this.gangname);
        }
        if (!TextUtils.isEmpty(this.regionid)) {
            SdkMgr.getInst().setUserInfo(ConstProp.USERINFO_REGION_ID, this.regionid);
        }
        if (TextUtils.isEmpty(this.regionname)) {
            return null;
        }
        SdkMgr.getInst().setUserInfo(ConstProp.USERINFO_REGION_NAME, this.regionname);
        return null;
    }

    @Override // com.netease.ntunisdk.base.model.SdkModel
    public void wrapFrom(Object obj) {
        this.roleid = SdkMgr.getInst().getUserInfo(ConstProp.USERINFO_UID);
        this.rolename = SdkMgr.getInst().getUserInfo(ConstProp.USERINFO_NAME);
        this.rolecreatetime = SdkMgr.getInst().getUserInfo(ConstProp.USERINFO_ROLE_CTIME);
        this.hostid = SdkMgr.getInst().getUserInfo(ConstProp.USERINFO_HOSTID);
        this.hostname = SdkMgr.getInst().getUserInfo(ConstProp.USERINFO_HOSTNAME);
        this.rolelevel = SdkMgr.getInst().getUserInfo(ConstProp.USERINFO_GRADE);
        this.viplevel = SdkMgr.getInst().getUserInfo(ConstProp.USERINFO_VIP);
        this.typeid = SdkMgr.getInst().getUserInfo(ConstProp.USERINFO_ROLE_TYPE_ID);
        this.typename = SdkMgr.getInst().getUserInfo(ConstProp.USERINFO_ROLE_TYPE_NAME);
        this.menpaiid = SdkMgr.getInst().getUserInfo(ConstProp.USERINFO_MENPAI_ID);
        this.menpainame = SdkMgr.getInst().getUserInfo(ConstProp.USERINFO_MENPAI_NAME);
        this.capability = SdkMgr.getInst().getUserInfo(ConstProp.USERINFO_CAPABILITY);
        this.gangid = SdkMgr.getInst().getUserInfo(ConstProp.USERINFO_GANG_ID);
        String userInfo = SdkMgr.getInst().getUserInfo(ConstProp.USERINFO_GANG_NAME);
        this.gangname = userInfo;
        if (TextUtils.isEmpty(userInfo)) {
            this.gangname = SdkMgr.getInst().getUserInfo(ConstProp.USERINFO_ORG);
        }
        this.regionid = SdkMgr.getInst().getUserInfo(ConstProp.USERINFO_REGION_ID);
        this.regionname = SdkMgr.getInst().getUserInfo(ConstProp.USERINFO_REGION_NAME);
    }

    @Override // com.netease.ntunisdk.base.model.SdkModel
    public JSONObject toJson() throws JSONException {
        JSONObject jSONObject;
        JSONException e;
        try {
            jSONObject = new JSONObject();
        } catch (JSONException e2) {
            jSONObject = null;
            e = e2;
        }
        try {
            jSONObject.putOpt(Const.CONFIG_KEY.ROLEID, this.roleid);
            jSONObject.putOpt("rolename", this.rolename);
            jSONObject.putOpt("rolecreatetime", this.rolecreatetime);
            jSONObject.putOpt("hostid", this.hostid);
            jSONObject.putOpt("hostname", this.hostname);
            jSONObject.putOpt("rolelevel", this.rolelevel);
            jSONObject.putOpt("viplevel", this.viplevel);
            jSONObject.putOpt(SocialConstants.PARAM_TYPE_ID, this.typeid);
            jSONObject.putOpt("typename", this.typename);
            jSONObject.putOpt("menpaiid", this.menpaiid);
            jSONObject.putOpt("menpainame", this.menpainame);
            jSONObject.putOpt("capability", this.capability);
            jSONObject.putOpt("gangid", this.gangid);
            jSONObject.putOpt("gangname", this.gangname);
            jSONObject.putOpt("regionid", this.regionid);
            jSONObject.putOpt("regionname", this.regionname);
        } catch (JSONException e3) {
            e = e3;
            e.printStackTrace();
            return jSONObject;
        }
        return jSONObject;
    }

    public boolean check() {
        return (TextUtils.isEmpty(this.roleid) || TextUtils.isEmpty(this.hostid)) ? false : true;
    }
}