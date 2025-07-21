package com.netease.androidcrashhandler.anr.messageQueue;

import android.text.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class HandleMessage implements Cloneable {
    private long mStartTime = 0;
    private long mEndTime = 0;
    private long mStartCpuTime = 0;
    private long mEndCpuTime = 0;
    private double mDuration = 0.0d;
    private long mCpuDuration = 0;
    private long mMessageCount = 0;
    private String mStartInfo = null;
    private String mEndInfo = null;
    private String mStackTrace = null;
    private String mNativeStaicTrace = null;
    private String mType = null;
    private long mActionTime = 0;

    public long getStartTime() {
        return this.mStartTime;
    }

    public void setStartTime(long j) {
        this.mStartTime = j;
    }

    public long getEndTime() {
        return this.mEndTime;
    }

    public void setEndTime(long j) {
        this.mEndTime = j;
    }

    public double getDuration() {
        return this.mDuration;
    }

    public void calculateDuration() {
        if (this.mEndTime > 0) {
            this.mDuration = r0 - this.mStartTime;
        } else {
            this.mDuration = 0.0d;
        }
        long j = this.mEndCpuTime;
        if (j > 0) {
            this.mCpuDuration = j - this.mStartCpuTime;
        } else {
            this.mCpuDuration = 0L;
        }
    }

    public long getMessageCount() {
        return this.mMessageCount;
    }

    public void addMessageCount() {
        this.mMessageCount++;
    }

    public void setMessageCount(long j) {
        this.mMessageCount = j;
    }

    public String getStartInfo() {
        return this.mStartInfo;
    }

    public void setStartInfo(String str) {
        this.mStartInfo = str;
    }

    public String getEndInfo() {
        return this.mEndInfo;
    }

    public void setEndInfo(String str) {
        this.mEndInfo = str;
    }

    public String getStackTrace() {
        return this.mStackTrace;
    }

    public void setStackTrace(String str) {
        this.mStackTrace = str;
    }

    public boolean hasStartTime() {
        return this.mStartTime != 0;
    }

    public String getType() {
        return this.mType;
    }

    public void setType(String str) {
        this.mType = str;
    }

    public long getRealTime() {
        return this.mActionTime;
    }

    public void setActionTime(long j) {
        this.mActionTime = j;
    }

    public long getStartCpuTime() {
        return this.mStartCpuTime;
    }

    public void setStartCpuTime(long j) {
        this.mStartCpuTime = j;
    }

    public long getEndCpuTime() {
        return this.mEndCpuTime;
    }

    public long getActionTime() {
        return this.mActionTime;
    }

    public void setEndCpuTime(long j) {
        if (j < this.mStartCpuTime) {
            j = -1;
        }
        this.mEndCpuTime = j;
    }

    public long getCpuDuration() {
        return this.mCpuDuration;
    }

    public void setCpuDuration(long j) {
        this.mCpuDuration = j;
    }

    public void setDuration(double d) {
        this.mDuration = d;
    }

    public String getNativeStaicTrace() {
        return this.mNativeStaicTrace;
    }

    public void setNativeStaicTrace(String str) {
        this.mNativeStaicTrace = str;
    }

    public JSONObject parse2Json() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("start_time", this.mStartTime);
            jSONObject.put("end_time", this.mEndTime);
            jSONObject.put("start_cpu_time", this.mStartCpuTime);
            jSONObject.put("end_cpu_time", this.mEndCpuTime);
            jSONObject.put("action_time", this.mActionTime);
            jSONObject.put("duration", this.mDuration / 1000000.0d);
            jSONObject.put("cpu_duration", this.mCpuDuration);
            jSONObject.put("message_count", this.mMessageCount);
            jSONObject.put("start_info", this.mStartInfo);
            jSONObject.put("end_info", this.mEndInfo);
            JSONArray jSONArray = new JSONArray();
            if (!TextUtils.isEmpty(this.mStackTrace)) {
                String[] strArrSplit = this.mStackTrace.split("#");
                if (strArrSplit.length > 0) {
                    for (int i = 0; i < strArrSplit.length; i++) {
                        if (!TextUtils.isEmpty(strArrSplit[i])) {
                            jSONArray.put(strArrSplit[i]);
                        }
                    }
                }
            }
            jSONObject.put("stack_info", jSONArray);
            JSONArray jSONArray2 = new JSONArray();
            if (!TextUtils.isEmpty(this.mNativeStaicTrace)) {
                String[] strArrSplit2 = this.mNativeStaicTrace.split("#");
                if (strArrSplit2.length > 0) {
                    for (int i2 = 0; i2 < strArrSplit2.length; i2++) {
                        if (!TextUtils.isEmpty(strArrSplit2[i2])) {
                            jSONArray2.put(strArrSplit2[i2]);
                        }
                    }
                }
            }
            jSONObject.put("native_stack_info", jSONArray2);
            jSONObject.put("type", this.mType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jSONObject;
    }

    public void clean() {
        this.mStartTime = 0L;
        this.mEndTime = 0L;
        this.mStartCpuTime = 0L;
        this.mEndCpuTime = 0L;
        this.mDuration = 0.0d;
        this.mCpuDuration = 0L;
        this.mMessageCount = 0L;
        this.mStartInfo = "";
        this.mEndInfo = "";
        this.mType = "";
        this.mActionTime = 0L;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public HandleMessage m514clone() throws CloneNotSupportedException {
        return (HandleMessage) super.clone();
    }

    public String toString() {
        return "HandleMessage{mStartTime=" + this.mStartTime + ", mEndTime=" + this.mEndTime + ", mStartCpuTime=" + this.mStartCpuTime + ", mEndCpuTime=" + this.mEndCpuTime + ", mDuration=" + this.mDuration + ", mCpuDuration=" + this.mCpuDuration + ", mMessageCount=" + this.mMessageCount + ", mStartInfo='" + this.mStartInfo + "', mEndInfo='" + this.mEndInfo + "', mType='" + this.mType + "', mActionTime=" + this.mActionTime + '}';
    }
}