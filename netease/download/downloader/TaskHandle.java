package com.netease.download.downloader;

import android.support.v4.media.session.PlaybackStateCompat;
import com.facebook.hermes.intl.Constants;
import com.netease.download.config.ConfigProxy;
import com.netease.download.listener.DownloadListenerProxy;
import com.netease.download.reporter.ReportUtil;
import com.netease.download.util.LogUtil;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;

/* loaded from: classes3.dex */
public class TaskHandle {
    private static final String TAG = "TaskHandle";
    private static TaskHandle sTaskHandle;
    public static boolean sWriteToLogFile;
    private ArrayList<DownloadParams> mFilesParamList = null;
    public int mTaskFileCount = 0;
    public int mTaskFileHasSuccessCount = 0;
    public int mTaskFileEnterDownloadProcessCoreCount = 0;
    public int mTaskFileEnterDownloadProcessCoreSuccessCount = 0;
    public int mTaskFileEnterDownloadProcessCoreFailCount = 0;
    public int mTaskFileEnterDownloadProcessCoreVerifySuccessCount = 0;
    public int mTaskFileEnterDownloadProcessCoreVerifyFailCount = 0;
    public long mTaskFileSuccessCount = 0;
    public volatile long mTaskFileFailCount = 0;
    public volatile int mTaskFileEnterDownloadProcessCoreMergeFileCount = 0;
    public volatile int mTaskFileEnterDownloadProcessCoreMergeFileVerifySuccessCount = 0;
    public volatile int mTaskFileEnterDownloadProcessCoreMergeFileVerifyFailCount = 0;
    public volatile int mTaskFileEnterDownloadProcessCoreMergeFileFailCount = 0;
    public HashMap<Integer, Long> mTaskFailCodeMap = new HashMap<>();
    public HashMap<Integer, JSONArray> mTaskFailFileInfoMap = new HashMap<>();
    public volatile int mTaskFailCancelCodeCount = 0;
    public long mHttpCount = 0;
    public long mHttpSuccessCount = 0;
    public long mHttpFailCount = 0;
    public HashMap<Integer, Long> mHttpFailMap = new HashMap<>();
    public HashMap<Integer, JSONArray> mHttpFailFileNameMap = new HashMap<>();
    public long mTotalSpeed = -1;
    public long mTotaNetlSpeed = -1;
    public HashMap<String, JSONArray> mChannelSpeedMap = new HashMap<>();
    public long mTaskAllSizes = 0;
    public long mTaskMergeAllSizes = 0;
    public long mTaskHasDownloadVerifySizes = 0;
    public long mTaskDownloadFileVerifySizes = 0;
    public long mTaskDownloadMergeFileVerifySizes = 0;
    public long mTaskCurTimeDownloadSizes = 0;
    private String mDownloadId = null;
    private String mSessionid = null;
    private String mNtesOrbitId = "";
    private boolean mIsRenew = false;
    private boolean mCheckFreeSpace = true;
    private int mBufferCount = 3;
    private long mCallBackInterval = 0;
    private String mOverSea = "-1";
    private boolean mLogOpen = false;
    public String mConfigurl = null;
    private String mNotUseCdn = Constants.CASEFIRST_FALSE;
    private boolean mAutoFree = true;
    private boolean mMergeMode = false;
    private long mMergeMax = PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE_ENABLED;
    private long mMergeSpace = 0;
    public String mType = null;
    public String mDownloadMode = null;
    private boolean mWifiOnly = true;
    private int mThreadnum = 3;
    private int mLogTest = 0;
    public String mChannel1 = null;
    public String mChannel2 = null;
    public float mMergeRate = 0.0f;
    public float mNetAllSpeedLimit = 0.0f;
    public int mPriority = -1;
    public boolean mPriorityTask = false;
    private boolean mRammode = false;
    private long mRamlimit = 33554432;
    private String mEncryptionAlgorithm = "MD5";
    private boolean mCheckfs = true;
    private long mFreeSpace = -1;
    public String mNetworkIsp = null;
    private int mNetworkLost = 0;
    private int mNetworkSwitch = 0;
    public long mTimeToStartTask = -1;
    public long mTimeToStartParseParams = -1;
    public long mTimeToFinishParseParams = -1;
    public long mTimeToStartHTTPDNS = -1;
    public long mTimeToFinishHTTPDNS = -1;
    public long mTimeToStartDownloadConfig = -1;
    public long mTimeToFinishDownloadConfig = -1;
    public long mTimeToStartDwonloadFile = -1;
    public long mTimeToFinishDwonloadFile = -1;
    public long mTimeToEndTask = -1;
    public HashMap<String, Long> mChannelDownloadCostTimeMap = new HashMap<>();
    public int mStatus = -100;
    public HashMap<String, Long> mChannelDnsCostTimeMap = new HashMap<>();
    public HashMap<String, JSONArray> mChannelDnsIpMap = new HashMap<>();
    public boolean mIsHttpdns = false;
    public HashMap<String, JSONArray> mChannelHttpdnsIpMap = new HashMap<>();
    public boolean mIsRemoveIp = false;
    public HashMap<String, JSONArray> mRemoveIpsMap = new HashMap<>();
    public boolean mSpaceNotEnough = false;

    public void clean() {
        sTaskHandle = null;
    }

    public long getTimeToStartTask() {
        return this.mTimeToStartTask;
    }

    public void setTimeToStartTask(long j) {
        this.mTimeToStartTask = j;
    }

    public long getTimeToStartParseParams() {
        return this.mTimeToStartParseParams;
    }

    public void setTimeToStartParseParams(long j) {
        this.mTimeToStartParseParams = j;
    }

    public long getTimeToFinishParseParams() {
        return this.mTimeToFinishParseParams;
    }

    public void setTimeToFinishParseParams(long j) {
        this.mTimeToFinishParseParams = j;
    }

    public long getTimeToStartHTTPDNS() {
        return this.mTimeToStartHTTPDNS;
    }

    public void setTimeToStartHTTPDNS(long j) {
        this.mTimeToStartHTTPDNS = j;
    }

    public long getTimeToFinishHTTPDNS() {
        return this.mTimeToFinishHTTPDNS;
    }

    public void setTimeToFinishHTTPDNS(long j) {
        this.mTimeToFinishHTTPDNS = j;
    }

    public long getTimeToStartDownloadConfig() {
        return this.mTimeToStartDownloadConfig;
    }

    public void setTimeToStartDownloadConfig(long j) {
        this.mTimeToStartDownloadConfig = j;
    }

    public long getTimeToFinishDownloadConfig() {
        return this.mTimeToFinishDownloadConfig;
    }

    public void setTimeToFinishDownloadConfig(long j) {
        this.mTimeToFinishDownloadConfig = j;
    }

    public long getTimeToStartDwonloadFile() {
        return this.mTimeToStartDwonloadFile;
    }

    public void setTimeToStartDwonloadFile(long j) {
        this.mTimeToStartDwonloadFile = j;
    }

    public long getTimeToFinishDwonloadFile() {
        return this.mTimeToFinishDwonloadFile;
    }

    public void setTimeToFinishDwonloadFile(long j) {
        this.mTimeToFinishDwonloadFile = j;
    }

    public long getTimeToEndTask() {
        return this.mTimeToEndTask;
    }

    public void setTimeToEndTask(long j) {
        this.mTimeToEndTask = j;
    }

    public String getChannel1() {
        return this.mChannel1;
    }

    public void setChannel1(String str) {
        this.mChannel1 = str;
    }

    public String getChannel2() {
        return this.mChannel2;
    }

    public void setChannel2(String str) {
        this.mChannel2 = str;
    }

    public String getType() {
        return this.mType;
    }

    public void setType(String str) {
        this.mType = str;
    }

    public String getDownloadMode() {
        return this.mDownloadMode;
    }

    public void setDownloadMode(String str) {
        this.mDownloadMode = str;
    }

    public boolean isRenew() {
        return this.mIsRenew;
    }

    public void setIsRenew(boolean z) {
        this.mIsRenew = z;
    }

    public boolean isCheckFreeSpace() {
        return this.mCheckFreeSpace;
    }

    public void setCheckFreeSpace(boolean z) {
        this.mCheckFreeSpace = z;
    }

    public boolean isWifiOnly() {
        return this.mWifiOnly;
    }

    public void setWifiOnly(boolean z) {
        this.mWifiOnly = z;
    }

    public int getThreadnum() {
        return this.mThreadnum;
    }

    public void setThreadnum(int i) {
        this.mThreadnum = i;
    }

    public int getBufferCount() {
        return this.mBufferCount;
    }

    public void setBufferCount(int i) {
        this.mBufferCount = i;
    }

    public long getCallBackInterval() {
        return this.mCallBackInterval;
    }

    public void setCallBackInterval(long j) {
        this.mCallBackInterval = j;
    }

    public String getOverSea() {
        return this.mOverSea;
    }

    public void setOverSea(String str) {
        this.mOverSea = str;
    }

    public boolean isLogOpen() {
        return this.mLogOpen;
    }

    public void setLogOpen(boolean z) {
        this.mLogOpen = z;
    }

    public String getConfigurl() {
        return this.mConfigurl;
    }

    public void setConfigurl(String str) {
        this.mConfigurl = str;
    }

    public long getTaskAllSizes() {
        return this.mTaskAllSizes;
    }

    public void setTaskAllSizes(long j) {
        this.mTaskAllSizes = j;
    }

    public long getTaskMergeAllSizes() {
        return this.mTaskMergeAllSizes;
    }

    public void setTaskMergeAllSizes(long j) {
        this.mTaskMergeAllSizes = j;
    }

    public String getNotUseCdn() {
        return this.mNotUseCdn;
    }

    public void setNotUseCdn(String str) {
        this.mNotUseCdn = str;
    }

    public boolean isAutoFree() {
        return this.mAutoFree;
    }

    public void setAutoFree(boolean z) {
        this.mAutoFree = z;
    }

    public boolean isMergeMode() {
        return this.mMergeMode;
    }

    public void setMergeMode(boolean z) {
        this.mMergeMode = z;
    }

    public long getMergeMax() {
        return this.mMergeMax;
    }

    public void setMergeMax(long j) {
        this.mMergeMax = j;
    }

    public long getMergeSpace() {
        return this.mMergeSpace;
    }

    public void setMergeSpace(long j) {
        this.mMergeSpace = j;
    }

    public float getMergeRate() {
        return this.mMergeRate;
    }

    public void setMergeRate(float f) {
        this.mMergeRate = f;
    }

    public String getDownloadId() {
        return this.mDownloadId;
    }

    public void setDownloadId(String str) {
        this.mDownloadId = str;
    }

    public String getSessionid() {
        return this.mSessionid;
    }

    public void setSessionid(String str) {
        this.mSessionid = str;
    }

    public String getNtesOrbitId() {
        return this.mNtesOrbitId;
    }

    public void setNtesOrbitId(String str) {
        this.mNtesOrbitId = str;
    }

    public int getLogTest() {
        return this.mLogTest;
    }

    public void setLogTest(int i) {
        this.mLogTest = i;
    }

    public int getStatus() {
        return this.mStatus;
    }

    public void setStatus(int i) {
        int i2 = this.mStatus;
        if (2 == i2 || 14 == i2) {
            return;
        }
        this.mStatus = i;
    }

    public String getNetworkIsp() {
        return this.mNetworkIsp;
    }

    public void setNetworkIsp(String str) {
        this.mNetworkIsp = str;
    }

    public int getNetworkLost() {
        return this.mNetworkLost;
    }

    public void setNetworkLost(int i) {
        this.mNetworkLost = i;
    }

    public int getNetworkSwitch() {
        return this.mNetworkSwitch;
    }

    public void setNetworkSwitch(int i) {
        this.mNetworkSwitch = i;
    }

    public float getNetAllSpeedLimit() {
        return this.mNetAllSpeedLimit;
    }

    public void setNetAllSpeedLimit(float f) {
        this.mNetAllSpeedLimit = f;
    }

    public float getNetThreadSpeedLimit() {
        int i = this.mTaskFileCount;
        int i2 = this.mThreadnum;
        if (i < i2) {
            return this.mNetAllSpeedLimit / i;
        }
        return this.mNetAllSpeedLimit / i2;
    }

    public int getPriority() {
        return this.mPriority;
    }

    public void setPriority(int i) {
        this.mPriority = i;
    }

    public boolean isPriorityTask() {
        return this.mPriorityTask;
    }

    public void setPriorityTask(boolean z) {
        this.mPriorityTask = z;
    }

    public boolean isRammode() {
        return this.mRammode;
    }

    public void setRammode(boolean z) {
        this.mRammode = z;
    }

    public long getRamlimit() {
        return this.mRamlimit;
    }

    public void setRamlimit(long j) {
        this.mRamlimit = j;
    }

    public String getEncryptionAlgorithm() {
        return this.mEncryptionAlgorithm;
    }

    public void setEncryptionAlgorithm(String str) {
        this.mEncryptionAlgorithm = str;
    }

    public boolean isCheckfs() {
        return this.mCheckfs;
    }

    public void setCheckfs(boolean z) {
        this.mCheckfs = z;
    }

    public long getFreeSpace() {
        return this.mFreeSpace;
    }

    public void setFreeSpace(long j) {
        this.mFreeSpace = j;
    }

    public int getTaskFileCount() {
        return this.mTaskFileCount;
    }

    public void setTaskFileCount(int i) {
        this.mTaskFileCount = i;
    }

    public boolean isIsHttpdns() {
        return this.mIsHttpdns;
    }

    public void setIsHttpdns(boolean z) {
        this.mIsHttpdns = z;
    }

    public long getTaskFileSuccessCount() {
        return this.mTaskFileSuccessCount;
    }

    public boolean isRemoveIp() {
        return this.mIsRemoveIp;
    }

    public void setIsRemoveIp(boolean z) {
        this.mIsRemoveIp = z;
    }

    public HashMap<String, JSONArray> getChannelDnsIpMap() {
        return this.mChannelDnsIpMap;
    }

    public HashMap<String, Long> getChannelDnsCostTimeMap() {
        return this.mChannelDnsCostTimeMap;
    }

    public HashMap<String, JSONArray> getChannelHttpdnsIpMap() {
        return this.mChannelHttpdnsIpMap;
    }

    public HashMap<String, Long> getChannelDownloadCostTimeMap() {
        return this.mChannelDownloadCostTimeMap;
    }

    public HashMap<String, JSONArray> getChannelSpeedMap() {
        return this.mChannelSpeedMap;
    }

    public int getTaskFileHasSuccessCount() {
        return this.mTaskFileHasSuccessCount;
    }

    public int getTaskFileEnterDownloadProcessCoreCount() {
        return this.mTaskFileEnterDownloadProcessCoreCount;
    }

    public int getTaskFileEnterDownloadProcessCoreSuccessCount() {
        return this.mTaskFileEnterDownloadProcessCoreSuccessCount;
    }

    public int getTaskFileEnterDownloadProcessCoreFailCount() {
        return this.mTaskFileEnterDownloadProcessCoreFailCount;
    }

    public int getTaskFileEnterDownloadProcessCoreVerifySuccessCount() {
        return this.mTaskFileEnterDownloadProcessCoreVerifySuccessCount;
    }

    public int getTaskFileEnterDownloadProcessCoreVerifyFailCount() {
        return this.mTaskFileEnterDownloadProcessCoreVerifyFailCount;
    }

    public int getTaskFileEnterDownloadProcessCoreMergeFileCount() {
        return this.mTaskFileEnterDownloadProcessCoreMergeFileCount;
    }

    public int getTaskFileEnterDownloadProcessCoreMergeFileVerifySuccessCount() {
        return this.mTaskFileEnterDownloadProcessCoreMergeFileVerifySuccessCount;
    }

    public int getTaskFileEnterDownloadProcessCoreMergeFileVerifyFailCount() {
        return this.mTaskFileEnterDownloadProcessCoreMergeFileVerifyFailCount;
    }

    public int getTaskFileEnterDownloadProcessCoreMergeFileFailCount() {
        return this.mTaskFileEnterDownloadProcessCoreMergeFileFailCount;
    }

    public HashMap<String, JSONArray> getRemoveIpsMap() {
        return this.mRemoveIpsMap;
    }

    public HashMap<Integer, Long> getTaskFailCodeMap() {
        return this.mTaskFailCodeMap;
    }

    public int getTaskFailCancelCodeCount() {
        return this.mTaskFailCancelCodeCount;
    }

    public HashMap<Integer, Long> getHttpFailMap() {
        return this.mHttpFailMap;
    }

    public long getHttpFailCount() {
        return this.mHttpFailCount;
    }

    public HashMap<Integer, JSONArray> getTaskFailFileInfoMap() {
        return this.mTaskFailFileInfoMap;
    }

    public HashMap<Integer, JSONArray> getHttpFailFileNameMap() {
        return this.mHttpFailFileNameMap;
    }

    public long getTaskHasDownloadSizes() {
        return this.mTaskHasDownloadVerifySizes;
    }

    public long getTaskDownloadFileVerifySizes() {
        return this.mTaskDownloadFileVerifySizes;
    }

    public long getTaskDownloadMergeFileVerifySizes() {
        return this.mTaskDownloadMergeFileVerifySizes;
    }

    public long getTaskCurTimeDownloadSizes() {
        return this.mTaskCurTimeDownloadSizes;
    }

    public boolean isSpaceNotEnough() {
        return this.mSpaceNotEnough;
    }

    public void setSpaceNotEnough(boolean z) {
        this.mSpaceNotEnough = z;
    }

    public void showTime() {
        LogUtil.i("TAG", "\u4efb\u52a1\u5f00\u59cb\u65f6\u95f4=" + this.mTimeToStartTask);
        LogUtil.i("TAG", "\u89e3\u6790\u53c2\u6570\u5f00\u59cb\u65f6\u95f4=" + this.mTimeToStartParseParams + ", \u76ee\u524d\u5df2\u82b1\u8d39\u65f6\u95f4=" + (this.mTimeToStartParseParams - this.mTimeToStartTask));
        LogUtil.i("TAG", "\u89e3\u6790\u53c2\u6570\u7ed3\u675f\u65f6\u95f4=" + this.mTimeToFinishParseParams + ", \u76ee\u524d\u5df2\u82b1\u8d39\u65f6\u95f4=" + (this.mTimeToFinishParseParams - this.mTimeToStartTask));
        LogUtil.i("TAG", "HTTPDNS\u5f00\u59cb\u65f6\u95f4=" + this.mTimeToStartHTTPDNS + ", \u76ee\u524d\u5df2\u82b1\u8d39\u65f6\u95f4=" + (this.mTimeToStartHTTPDNS - this.mTimeToStartTask));
        LogUtil.i("TAG", "HTTPDNS\u7ed3\u675f\u65f6\u95f4=" + this.mTimeToFinishHTTPDNS + ", \u76ee\u524d\u5df2\u82b1\u8d39\u65f6\u95f4=" + (this.mTimeToFinishHTTPDNS - this.mTimeToStartTask));
        LogUtil.i("TAG", "\u4e0b\u8f7d\u914d\u7f6e\u6587\u4ef6\u5f00\u59cb\u65f6\u95f4=" + this.mTimeToStartDownloadConfig + ", \u76ee\u524d\u5df2\u82b1\u8d39\u65f6\u95f4=" + (this.mTimeToStartDownloadConfig - this.mTimeToStartTask));
        LogUtil.i("TAG", "\u4e0b\u8f7d\u914d\u7f6e\u6587\u4ef6\u7ed3\u675f\u65f6\u95f4=" + this.mTimeToFinishDownloadConfig + ", \u76ee\u524d\u5df2\u82b1\u8d39\u65f6\u95f4=" + (this.mTimeToFinishDownloadConfig - this.mTimeToStartTask));
        LogUtil.i("TAG", "\u4e0b\u8f7d\u6587\u4ef6\u5f00\u59cb\u6587\u4ef6=" + this.mTimeToStartDwonloadFile + ", \u76ee\u524d\u5df2\u82b1\u8d39\u65f6\u95f4=" + (this.mTimeToStartDwonloadFile - this.mTimeToStartTask));
        LogUtil.i("TAG", "\u4e0b\u8f7d\u6587\u4ef6\u7ed3\u675f\u65f6\u95f4=" + this.mTimeToFinishDwonloadFile + ", \u76ee\u524d\u5df2\u82b1\u8d39\u65f6\u95f4=" + (this.mTimeToFinishDwonloadFile - this.mTimeToStartTask) + ", \u7eaf\u7f51\u7edc\u82b1\u8d39\u65f6\u95f4=" + (this.mTimeToFinishDwonloadFile - this.mTimeToStartDwonloadFile));
        StringBuilder sb = new StringBuilder("\u4efb\u52a1\u7ed3\u675f\u65f6\u95f4=");
        sb.append(this.mTimeToEndTask);
        sb.append(", \u76ee\u524d\u5df2\u82b1\u8d39\u65f6\u95f4=");
        sb.append(this.mTimeToEndTask - this.mTimeToStartTask);
        LogUtil.i("TAG", sb.toString());
    }

    public void showParams() {
        LogUtil.i("TAG", "mDownloadId=" + this.mDownloadId);
        LogUtil.i("TAG", "mSessionid=" + this.mSessionid);
        LogUtil.i("TAG", "mNtesOrbitId=" + this.mNtesOrbitId);
        LogUtil.i("TAG", "mTransid=" + DownloadInitInfo.getInstances().getmTransid());
        LogUtil.i("TAG", "mUdid=" + DownloadInitInfo.getInstances().getmUdid());
        LogUtil.i("TAG", "mIsRenew=" + this.mIsRenew);
        LogUtil.i("TAG", "mBufferCount=" + this.mBufferCount);
        LogUtil.i("TAG", "mCallBackInterval=" + this.mCallBackInterval);
        LogUtil.i("TAG", "mOverSea=" + this.mOverSea);
        LogUtil.i("TAG", "mLogOpen=" + this.mLogOpen);
        LogUtil.i("TAG", "mConfigurl=" + this.mConfigurl);
        LogUtil.i("TAG", "mNotUseCdn=" + this.mNotUseCdn);
        LogUtil.i("TAG", "mAutoFree=" + this.mAutoFree);
        LogUtil.i("TAG", "mMergeMode=" + this.mMergeMode);
        LogUtil.i("TAG", "mMergeMax=" + this.mMergeMax);
        LogUtil.i("TAG", "mMergeSpace=" + this.mMergeSpace);
        LogUtil.i("TAG", "mMergeRate=" + this.mMergeRate);
        LogUtil.i("TAG", "mType=" + this.mType);
        LogUtil.i("TAG", "mDownloadMode=" + this.mDownloadMode);
        LogUtil.i("TAG", "mWifiOnly=" + this.mWifiOnly);
        LogUtil.i("TAG", "mThreadnum=" + this.mThreadnum);
        LogUtil.i("TAG", "sWriteToLogFile=" + sWriteToLogFile);
        LogUtil.i("TAG", "mLogTest=" + this.mLogTest);
        LogUtil.i("TAG", "mChannel1=" + this.mChannel1);
        LogUtil.i("TAG", "mChannel2=" + this.mChannel2);
        LogUtil.i("TAG", "mSplitThreshold=" + ConfigProxy.getInstances().getmConfigParams().mSplitThreshold);
        LogUtil.i("TAG", "mStatus=" + this.mStatus);
        LogUtil.i("TAG", "mTaskAllSizes=" + this.mTaskAllSizes);
        LogUtil.i("TAG", "mTaskHasDownloadSizes=" + this.mTaskHasDownloadVerifySizes);
        LogUtil.i("TAG", "mTaskCurTimeDownloadSizes=" + (this.mTaskAllSizes - DownloadListenerProxy.mHasDownloadSize));
        LogUtil.i("TAG", "mHttpCount=" + this.mHttpCount);
        LogUtil.i("TAG", "mHttpSuccessCount=" + this.mHttpSuccessCount);
        LogUtil.i("TAG", "mHttpFailCount=" + this.mHttpFailCount);
        LogUtil.i("TAG", "mHttpFailMap=" + this.mHttpFailMap.toString());
        LogUtil.i("TAG", "mHttpFailFileNameMap=" + this.mHttpFailFileNameMap.toString());
        LogUtil.i("TAG", "mTaskFileCount=" + DownloadListenerProxy.getmTotalFileCount());
        LogUtil.i("TAG", "mTaskFileHasSuccessCount=" + this.mTaskFileHasSuccessCount);
        LogUtil.i("TAG", "mTaskFileEnterDownloadProcessCoreCount=" + this.mTaskFileEnterDownloadProcessCoreCount);
        LogUtil.i("TAG", "mTaskFileEnterDownloadProcessCoreSuccessCount=" + this.mTaskFileEnterDownloadProcessCoreSuccessCount);
        LogUtil.i("TAG", "mTaskFileEnterDownloadProcessCoreFailCount=" + this.mTaskFileEnterDownloadProcessCoreFailCount);
        LogUtil.i("TAG", "mTaskFileEnterDownloadProcessCoreVerifySuccessCount=" + this.mTaskFileEnterDownloadProcessCoreVerifySuccessCount);
        LogUtil.i("TAG", "mTaskFileEnterDownloadProcessCoreVerifyFailCount=" + this.mTaskFileEnterDownloadProcessCoreVerifyFailCount);
        LogUtil.i("TAG", "mTaskFileSuccessCount=" + this.mTaskFileSuccessCount);
        LogUtil.i("TAG", "mTaskFileFailCount=" + this.mTaskFileFailCount);
        LogUtil.i("TAG", "mTaskFailCodeMap=" + this.mTaskFailCodeMap.toString());
        LogUtil.i("TAG", "mTaskFailFileInfoMap=" + this.mTaskFailFileInfoMap.toString());
        new DecimalFormat("0.00");
        LogUtil.i("TAG", "mCompleteRate=" + ((this.mTaskFileSuccessCount / DownloadListenerProxy.getmTotalFileCount()) * 100.0f));
        LogUtil.i("TAG", "mChannelDownloadCostTimeMap=" + this.mChannelDownloadCostTimeMap.toString());
        LogUtil.i("TAG", "mChannelDnsCostTimeMap=" + this.mChannelDnsCostTimeMap.toString());
        LogUtil.i("TAG", "mChannelDnsIpMap=" + this.mChannelDnsIpMap.toString());
        LogUtil.i("TAG", "mIsHttpdns=" + this.mIsHttpdns);
        LogUtil.i("TAG", "mChannelHttpdnsIpMap=" + this.mChannelHttpdnsIpMap.toString());
        LogUtil.i("TAG", "mIsRemoveIp=" + this.mIsRemoveIp);
        LogUtil.i("TAG", "mRemoveIpsMap=" + this.mRemoveIpsMap.toString());
        LogUtil.i("TAG", "mTotalSpeed=" + (((((double) this.mTaskAllSizes) * 1.0d) / 1024.0d) / ((((double) (this.mTimeToEndTask - this.mTimeToStartTask)) * 1.0d) / 1000.0d)));
        LogUtil.i("TAG", "mTotalNetSpeed=" + (((((double) this.mTaskAllSizes) * 1.0d) / 1024.0d) / ((((double) (this.mTimeToFinishDwonloadFile - this.mTimeToStartDwonloadFile)) * 1.0d) / 1000.0d)));
        LogUtil.i("TAG", "mChannelSpeedMap=" + this.mChannelSpeedMap.toString());
    }

    public void showInitInfo() {
        LogUtil.i("TAG", "mOsName=" + DownloadInitInfo.getInstances().getmOsName());
        LogUtil.i("TAG", "mOsVer=" + DownloadInitInfo.getInstances().getmOsVer());
        LogUtil.i("TAG", "mMobileType=" + DownloadInitInfo.getInstances().getmMobileType());
        LogUtil.i("TAG", "mTimeZone=" + DownloadInitInfo.getInstances().getmTimeZone());
        LogUtil.i("TAG", "mAreaZone=" + DownloadInitInfo.getInstances().getmAreaZone());
        LogUtil.i("TAG", "mNetwork=" + ReportUtil.getInstances().getNetworkType());
        LogUtil.i("TAG", "mNetworkIsp=" + ReportUtil.getInstances().getSystemModel());
        LogUtil.i("TAG", "mNetworkSignal=" + ReportUtil.getInstances().getNetworkSignal());
        LogUtil.i("TAG", "mAppChannel=" + DownloadInitInfo.getInstances().getmAppChannel());
        LogUtil.i("TAG", "mUdtVer=2.8.2");
        LogUtil.i("TAG", "mProjectId=" + DownloadInitInfo.getInstances().getProjectId());
    }
}