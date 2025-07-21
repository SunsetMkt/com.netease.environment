package com.netease.ntunisdk.base;

import android.text.TextUtils;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class QueryRankInfo {
    public static final String QUERY_RANK_SCOPE_ALL = "QUERY_RANK_SCOPE_ALL";
    public static final String QUERY_RANK_SCOPE_FRIEND = "QUERY_RANK_SCOPE_FRIEND";
    public static final String QUERY_RANK_TYPE_GRADE = "QUERY_RANK_TYPE_GRADE";
    public static final String QUERY_RANK_TYPE_SCORE = "QUERY_RANK_TYPE_SCORE";

    /* renamed from: a, reason: collision with root package name */
    private String f1614a;
    private String b;
    private int c = 10;

    public String getQueryRankType() {
        return this.f1614a;
    }

    public void setQueryRankType(String str) {
        this.f1614a = str;
    }

    public String getQueryRankScope() {
        return this.b;
    }

    public void setQueryRankScope(String str) {
        this.b = str;
    }

    public int getQueryRankCount() {
        return this.c;
    }

    public void setQueryRankCount(int i) {
        this.c = i;
    }

    public static QueryRankInfo jsonStr2Obj(String str) {
        QueryRankInfo queryRankInfo = new QueryRankInfo();
        if (TextUtils.isEmpty(str)) {
            return queryRankInfo;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            String strOptString = jSONObject.optString("queryRankType");
            String strOptString2 = jSONObject.optString("queryRankScope");
            int iOptInt = jSONObject.optInt("queryRankCount");
            queryRankInfo.setQueryRankType(strOptString);
            queryRankInfo.setQueryRankScope(strOptString2);
            queryRankInfo.setQueryRankCount(iOptInt);
        } catch (Exception e) {
            UniSdkUtils.e("UniSDK QueryRankInfo", "jsonStr2Obj error");
            e.printStackTrace();
        }
        return queryRankInfo;
    }
}