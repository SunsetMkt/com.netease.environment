package com.netease.androidcrashhandler.anr.messageQueue;

import android.text.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class NonHandleMessage {
    private long mWhen = -1;
    private String mCallbackName = null;
    private long mWhat = -1;
    private String mTargetName = null;
    private long mArg1 = -1;
    private long mArg2 = -1;
    private long mBarrier = -1;

    public long getWhen() {
        return this.mWhen;
    }

    public void setWhen(long j) {
        this.mWhen = j;
    }

    public String getCallbackName() {
        return this.mCallbackName;
    }

    public void setCallbackName(String str) {
        this.mCallbackName = str;
    }

    public long getWhat() {
        return this.mWhat;
    }

    public void setWhat(long j) {
        this.mWhat = j;
    }

    public String getTargetName() {
        return this.mTargetName;
    }

    public void setTargetName(String str) {
        this.mTargetName = str;
    }

    public long getArg1() {
        return this.mArg1;
    }

    public void setArg1(long j) {
        this.mArg1 = j;
    }

    public long getArg2() {
        return this.mArg2;
    }

    public void setArg2(long j) {
        this.mArg2 = j;
    }

    public long getBarrier() {
        return this.mBarrier;
    }

    public void setBarrier(long j) {
        this.mBarrier = j;
    }

    public JSONObject parse2Json() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            if (!TextUtils.isEmpty(this.mTargetName)) {
                jSONObject.put("when", this.mWhen);
                jSONObject.put("callback_name", this.mCallbackName);
                jSONObject.put("what", this.mWhat);
                jSONObject.put("target_name", this.mTargetName);
                long j = this.mArg1;
                if (0 != j) {
                    jSONObject.put("arg1", j);
                }
                long j2 = this.mArg2;
                if (0 != j2) {
                    jSONObject.put("arg2", j2);
                }
            } else {
                jSONObject.put("barrier", this.mBarrier);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jSONObject;
    }

    public String toString() {
        return "NonHandleMessage{mWhen=" + this.mWhen + ", mCallbackName='" + this.mCallbackName + "', mWhat=" + this.mWhat + ", mTargetName='" + this.mTargetName + "', mArg1=" + this.mArg1 + ", mArg2=" + this.mArg2 + ", mBarrier=" + this.mBarrier + '}';
    }
}