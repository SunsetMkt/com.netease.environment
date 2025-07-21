package com.netease.ntunisdk.base;

import com.wali.gamecenter.report.ReportOrigin;
import com.xiaomi.gamecenter.sdk.statistics.OneTrackParams;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class AccountInfo {
    public static final String UNKNOWN = "unknown";

    /* renamed from: a, reason: collision with root package name */
    private String f1600a;
    private String b;
    private String c;
    private String d;
    private String e;
    private boolean f;
    private double g;
    private long h;
    private String i;
    private String j;

    public AccountInfo() {
    }

    public AccountInfo(String str) {
        setAccountId(str);
        setIdType("unknown");
        setNickname("unknown");
        setIcon("unknown");
        setInGame(false);
        setRankScore(0.0d);
        setRank(0L);
        setRemark("");
    }

    public String getAccountId() {
        return this.f1600a;
    }

    public void setAccountId(String str) {
        this.f1600a = str;
    }

    public String getIdType() {
        return this.b;
    }

    public void setIdType(String str) {
        this.b = str;
    }

    public String getRelationType() {
        return this.c;
    }

    public void setRelationType(String str) {
        this.c = str;
    }

    public String getNickname() {
        return this.d;
    }

    public void setNickname(String str) {
        this.d = str;
    }

    public String getIcon() {
        return this.e;
    }

    public void setIcon(String str) {
        this.e = str;
    }

    public boolean isInGame() {
        return this.f;
    }

    public void setInGame(boolean z) {
        this.f = z;
    }

    public double getRankScore() {
        return this.g;
    }

    public void setRankScore(double d) {
        this.g = d;
    }

    public long getRank() {
        return this.h;
    }

    public void setRank(long j) {
        this.h = j;
    }

    public String getRemark() {
        return this.i;
    }

    public void setRemark(String str) {
        this.i = str;
    }

    public String getStatusMessage() {
        return this.j;
    }

    public void setStatusMessage(String str) {
        this.j = str;
    }

    public static String json2Str(AccountInfo accountInfo) {
        return obj2Json(accountInfo).toString();
    }

    public static JSONObject obj2Json(AccountInfo accountInfo) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        if (accountInfo == null) {
            return jSONObject;
        }
        try {
            jSONObject.put("accountId", accountInfo.getAccountId());
            jSONObject.put(OneTrackParams.XMSdkParams.MID, accountInfo.getAccountId());
            jSONObject.put("idType", accountInfo.getIdType());
            jSONObject.put("relationType", accountInfo.getRelationType());
            jSONObject.put("nickname", accountInfo.getNickname());
            jSONObject.put("displayName", accountInfo.getNickname());
            jSONObject.put("icon", accountInfo.getIcon());
            jSONObject.put("pictureUrl", accountInfo.getIcon());
            jSONObject.put("inGame", accountInfo.isInGame());
            jSONObject.put("rankScore", accountInfo.getRankScore());
            jSONObject.put(ReportOrigin.ORIGIN_RANK, accountInfo.getRank());
            jSONObject.put("remark", accountInfo.getRemark());
        } catch (JSONException e) {
            UniSdkUtils.e("UniSDK AccountInfo", "obj2Json error");
            e.printStackTrace();
        }
        return jSONObject;
    }
}