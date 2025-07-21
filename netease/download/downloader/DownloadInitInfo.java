package com.netease.download.downloader;

import android.content.Context;
import android.text.TextUtils;
import com.netease.download.reporter.ReportUtil;

/* loaded from: classes3.dex */
public class DownloadInitInfo {
    private static String TAG = "DownloadInitInfo";
    private static DownloadInitInfo sDownloadInitInfo;
    private String mLocalgateway = null;
    private Context mContext = null;
    private String mAppChannel = "";
    private String mProjectId = null;
    public String mOsName = null;
    public String mOsVer = null;
    public String mMobileType = null;
    public String mTimeZone = null;
    public String mAreaZone = null;
    public String mSplitThreshold = null;
    public String mUnisdkVersion = null;
    public String mUnisdkChannelVersion = null;
    public String mImei = null;
    public String mImsi = null;
    private String mUdid = null;
    private String mTransid = "";
    private String mUnisdkDeviceId = null;
    public String mMacAddr = null;
    private String mLocalIp = null;

    private DownloadInitInfo() {
    }

    public static DownloadInitInfo getInstances() {
        if (sDownloadInitInfo == null) {
            sDownloadInitInfo = new DownloadInitInfo();
        }
        return sDownloadInitInfo;
    }

    public void setContext(Context context) {
        if (this.mContext == null) {
            this.mContext = context;
        }
    }

    public void setProjectId(String str) {
        this.mProjectId = str;
    }

    public String getProjectId() {
        return this.mProjectId;
    }

    public void setLocalgateway(String str) {
        this.mLocalgateway = str;
    }

    private String intToIp(int i) {
        return (i & 255) + "." + ((i >> 8) & 255) + "." + ((i >> 16) & 255) + "." + ((i >> 24) & 255);
    }

    public String getmAppChannel() {
        return this.mAppChannel;
    }

    public void setmAppChannel(String str) {
        this.mAppChannel = str;
    }

    public String getmTransid() {
        return this.mTransid;
    }

    public void setmTransid(String str) {
        this.mTransid = str;
    }

    public String getmUdid() {
        return this.mUdid;
    }

    public void setmUdid(String str) {
        this.mUdid = str;
    }

    public String getmProjectId() {
        return this.mProjectId;
    }

    public void setmProjectId(String str) {
        this.mProjectId = str;
    }

    public String getmOsName() {
        if (TextUtils.isEmpty(this.mOsName)) {
            this.mOsName = ReportUtil.getInstances().getOsName();
        }
        return this.mOsName;
    }

    public String getmOsVer() {
        if (TextUtils.isEmpty(this.mOsVer)) {
            this.mOsVer = ReportUtil.getInstances().getOsVer();
        }
        return this.mOsVer;
    }

    public String getmMobileType() {
        if (TextUtils.isEmpty(this.mMobileType)) {
            this.mMobileType = ReportUtil.getInstances().getSystemModel();
        }
        return this.mMobileType;
    }

    public String getmTimeZone() {
        if (TextUtils.isEmpty(this.mTimeZone)) {
            this.mTimeZone = ReportUtil.getInstances().getTimeZone();
        }
        return this.mTimeZone;
    }

    public String getmAreaZone() {
        if (TextUtils.isEmpty(this.mAreaZone)) {
            this.mAreaZone = ReportUtil.getInstances().getAreaZone();
        }
        return this.mAreaZone;
    }

    public String getmSplitThreshold() {
        return this.mSplitThreshold;
    }

    public void setmSplitThreshold(String str) {
        this.mSplitThreshold = str;
    }

    public String getmUnisdkVersion() {
        return this.mUnisdkVersion;
    }

    public void setmUnisdkVersion(String str) {
        this.mUnisdkVersion = str;
    }

    public String getmUnisdkChannelVersion() {
        return this.mUnisdkChannelVersion;
    }

    public void setmUnisdkChannelVersion(String str) {
        this.mUnisdkChannelVersion = str;
    }

    public String getmMacAddr() {
        return this.mMacAddr;
    }

    public void setmMacAddr(String str) {
        this.mMacAddr = str;
    }

    public String getmImei() {
        return this.mImei;
    }

    public void setmImei(String str) {
        this.mImei = str;
    }

    public String getmImsi() {
        return this.mImsi;
    }

    public void setmImsi(String str) {
        this.mImsi = str;
    }

    public String getmUnisdkDeviceId() {
        return this.mUnisdkDeviceId;
    }

    public void setmUnisdkDeviceId(String str) {
        this.mUnisdkDeviceId = str;
    }

    public String getmLocalIp() {
        return this.mLocalIp;
    }

    public void setmLocalIp(String str) {
        this.mLocalIp = str;
    }
}