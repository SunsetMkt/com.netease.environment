package com.netease.download.downloader;

import android.content.Context;
import android.text.TextUtils;
import com.netease.download.Const;
import com.netease.download.check.CheckTime;
import com.netease.download.config.ConfigParams;
import com.netease.download.dns.CdnIpController;
import com.netease.download.httpdns.HttpdnsProxy;
import com.netease.download.listener.DownloadListener;
import com.netease.download.listener.DownloadListenerProxy;
import com.netease.download.lvsip.Lvsip;
import com.netease.download.reporter.ReportProxy;
import com.netease.download.util.LogUtil;
import com.netease.download.util.Util;
import com.xiaomi.gamecenter.sdk.robust.Constants;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class DownloadParams {
    private static final Random RANDOM = new Random();
    private static final String TAG = "DownloadParams";
    private String mCdnUrl;
    private String mFileId;
    private String mFilePath;
    private String mHost;
    private String mHttpdnsIp;
    private boolean mIsPart;
    private boolean mIsUiCallback;
    private String mMd5;
    private String mOriginUrl;
    private long mRealFileSize;
    private boolean mRenew;
    private long mSize;
    private String mTargetUrl;
    private int mTotalWeight;
    private String mUrlResName;
    private String mUserFileName = null;
    private int mPartIndex = 0;
    private int mPartCount = 1;
    private long mPriSegmentStart = 0;
    private long mStart = 0;
    private long mLast = 0;
    private long mWoffset = -100;
    private String mOffsetTempFileName = null;
    private long mMergeOffset = 0;
    private boolean mWriteToExistFile = false;
    private int mDownloadResultCode = -100;
    private long mStartDownloadTime = -1;
    private float mSpeedLimit = 0.0f;
    private int mResult = -100;
    private ByteBuffer mByteBuffer = null;
    private Timer timer = null;
    private String mChannel = null;
    private ArrayList<Element> mElementList = new ArrayList<>();

    public void setConfigParam(ConfigParams configParams) {
    }

    private class DownloadSegmentChannel {
        String url;
        int weight;

        DownloadSegmentChannel(String str, int i) {
            this.url = str;
            this.weight = i;
        }

        public String toString() {
            return "DownloadSegmentChannel{, url='" + this.url + "', weight=" + this.weight + '}';
        }
    }

    public ArrayList<Element> getmElementList() {
        return this.mElementList;
    }

    public void setmElementList(ArrayList<Element> arrayList) {
        this.mElementList = arrayList;
    }

    public void addElement(String str, long j, long j2, String str2) {
        if (TextUtils.isEmpty(str) || this.mElementList == null) {
            return;
        }
        this.mElementList.add(new Element(str, j, j2, str2));
    }

    public String getmChannel() {
        return this.mChannel;
    }

    public void setmChannel(String str) {
        this.mChannel = str;
    }

    public void setTotalWeight(int i) {
        this.mTotalWeight = i;
    }

    public String getUrlResName() {
        return this.mUrlResName;
    }

    private String getUrl() {
        StringBuilder sb = new StringBuilder(this.mCdnUrl);
        if (!this.mCdnUrl.endsWith("/")) {
            sb.append("/");
        }
        if (this.mUrlResName.startsWith("/")) {
            sb.append(this.mUrlResName.substring(1));
        } else {
            sb.append(this.mUrlResName);
        }
        return sb.toString();
    }

    public String getDownloadUrl() {
        String str = this.mHttpdnsIp;
        if (str == null) {
            return getUrl();
        }
        if (Util.isIpv6(str)) {
            return Util.replaceDomainWithIpAddr(getUrl(), Constants.C + this.mHttpdnsIp + "]", "/");
        }
        return Util.replaceDomainWithIpAddr(getUrl(), this.mHttpdnsIp, "/");
    }

    public String getDownloadUrl(String str) {
        if (Util.isIpv6(str)) {
            return Util.replaceDomainWithIpAddr(getUrl(), Constants.C + str + "]", "/");
        }
        return Util.replaceDomainWithIpAddr(getUrl(), str, "/");
    }

    public String getDomainFromUrl() {
        return Util.getDomainFromUrl(getUrlPrefix());
    }

    public void setUrlPrefix(String str) {
        this.mCdnUrl = str;
    }

    public String getUrlPrefix() {
        return this.mCdnUrl;
    }

    public void setmUrlResName(String str) {
        this.mUrlResName = str;
    }

    public void setOriginUrl(String str) {
        this.mOriginUrl = str;
    }

    public String getOriginUrl() {
        return this.mOriginUrl;
    }

    public long getSize() {
        return this.mSize;
    }

    public void setSize(long j) {
        this.mSize = j;
    }

    public long getRealFileSize() {
        return this.mRealFileSize;
    }

    public void setRealFileSize(long j) {
        this.mRealFileSize = j;
    }

    public String getMd5() {
        if (TextUtils.isEmpty(this.mMd5)) {
            this.mMd5 = "12345678";
        }
        return this.mMd5;
    }

    public void setMd5(String str) {
        this.mMd5 = str;
    }

    public String getFilePath() {
        return this.mFilePath;
    }

    public void setFilePath(String str) {
        this.mFilePath = str;
    }

    public String getUserFileName() {
        return this.mUserFileName;
    }

    public void setUserFileName(String str) {
        this.mUserFileName = str;
    }

    public String getCallBackFileName() {
        if (!TextUtils.isEmpty(this.mUserFileName)) {
            return this.mUserFileName;
        }
        return this.mFilePath;
    }

    boolean isUiCallback() {
        return this.mIsUiCallback;
    }

    public void setIsUiCallback(boolean z) {
        this.mIsUiCallback = z;
    }

    public boolean isValid() {
        return (TextUtils.isEmpty(getUrlResName()) || TextUtils.isEmpty(getFilePath())) ? false : true;
    }

    public long getLast() {
        return this.mLast;
    }

    public void setLast(long j) {
        this.mLast = j;
    }

    public long getStart() {
        return this.mStart;
    }

    public void setStart(long j) {
        this.mStart = j;
    }

    public long getmPriSegmentStart() {
        return this.mPriSegmentStart;
    }

    public void setmPriSegmentStart(long j) {
        this.mPriSegmentStart = j;
    }

    public long getmWoffset() {
        return this.mWoffset;
    }

    public void setmWoffset(long j) {
        this.mWoffset = j;
    }

    public boolean ismWriteToExistFile() {
        return this.mWriteToExistFile;
    }

    public void setmWriteToExistFile(boolean z) {
        this.mWriteToExistFile = z;
    }

    public String getmOffsetTempFileName() {
        return this.mOffsetTempFileName;
    }

    public void setmOffsetTempFileName(String str) {
        this.mOffsetTempFileName = str;
    }

    public void setPartCount(int i) {
        this.mPartCount = i;
    }

    public int getPartCount() {
        return this.mPartCount;
    }

    private void setPartIndex(int i) {
        this.mPartIndex = i;
    }

    public int getPartIndex() {
        return this.mPartIndex;
    }

    public boolean isParted() {
        return this.mIsPart;
    }

    public void setIsParted(boolean z) {
        this.mIsPart = z;
    }

    public String getFileId() {
        return this.mFileId;
    }

    public void setFileId(String str) {
        this.mFileId = str;
    }

    public String getTargetUrl() {
        return this.mTargetUrl;
    }

    public void setTargetUrl(String str) {
        this.mTargetUrl = str;
    }

    public String getHost() {
        return this.mHost;
    }

    public void setHost(String str) {
        this.mHost = str;
    }

    public String getmHttpdnsIp() {
        return this.mHttpdnsIp;
    }

    public void setmHttpdnsIp(String str) {
        this.mHttpdnsIp = str;
    }

    public int getmDownloadResultCode() {
        return this.mDownloadResultCode;
    }

    public void setmDownloadResultCode(int i) {
        this.mDownloadResultCode = i;
    }

    public DownloadParams produceSegment(int i, long j, long j2, String str, float f) {
        return new DownloadParams(this, i, j, j2, str, f);
    }

    public long getmMergeOffset() {
        return this.mMergeOffset;
    }

    public void setmMergeOffset(long j) {
        this.mMergeOffset = j;
    }

    public long getmStartDownloadTime() {
        return this.mStartDownloadTime;
    }

    public void setmStartDownloadTime(long j) {
        this.mStartDownloadTime = j;
    }

    public int getmResult() {
        return this.mResult;
    }

    public void setmResult(int i) {
        this.mResult = i;
    }

    public float getmSpeedLimit() {
        return this.mSpeedLimit;
    }

    public void setmSpeedLimit(float f) {
        this.mSpeedLimit = f;
    }

    public void createByteBuffer(long j) {
        if (this.mByteBuffer == null) {
            int i = (int) j;
            if (i == 0) {
                i = 4194304;
            }
            this.mByteBuffer = ByteBuffer.allocate(i);
        }
    }

    public ByteBuffer getByteBuffer() {
        ByteBuffer byteBuffer = this.mByteBuffer;
        return byteBuffer == null ? ByteBuffer.allocate(1) : byteBuffer;
    }

    public void clearByteBuffer() {
        ByteBuffer byteBuffer = this.mByteBuffer;
        if (byteBuffer != null) {
            byteBuffer.clear();
            this.mByteBuffer = null;
        }
    }

    DownloadParams() {
    }

    public static List<DownloadParams> createParamsArray(Context context, JSONObject jSONObject, DownloadListener downloadListener) throws JSONException, NumberFormatException {
        String strOptString;
        long last;
        LogUtil.i(TAG, "\u4e0b\u8f7d\u5668\u5f00\u59cb");
        LogUtil.i(TAG, "create params array");
        DownloadListenerProxy.getInstances().init(downloadListener);
        DownloadListenerProxy.getInstances().clear();
        ReportProxy.getInstance().init(context);
        LogUtil.i(TAG, "DownloadParams [createParamsArray] \u4e0b\u8f7d\u524d\u671f\uff0c\u53d1\u9001\u65e5\u5fd7\uff08\u4e0a\u4e00\u6b21\u9057\u7559\u6587\u4ef6\uff09");
        boolean z = true;
        ReportProxy.getInstance().report(context, 1);
        HttpdnsProxy.getInstances().clean();
        CdnIpController.getInstances().clean();
        CheckTime.clean();
        Lvsip.getInstance().clean();
        ArrayList arrayList = new ArrayList();
        if (jSONObject == null) {
            return arrayList;
        }
        try {
            strOptString = jSONObject.optString("type");
        } catch (NumberFormatException unused) {
            strOptString = "error";
        }
        String str = strOptString;
        LogUtil.i(TAG, "downloadid =" + jSONObject.optString("downloadid"));
        LogUtil.i(TAG, "\u4ece\u6301\u4e45\u5316\u4e2d\u83b7\u53d6\u6570\u636e\uff0c\u8f6c\u4e3ajson=" + jSONObject.toString());
        JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("downfile");
        long j = 0;
        if (jSONArrayOptJSONArray != null && jSONArrayOptJSONArray.length() > 0) {
            boolean z2 = false;
            long j2 = 0;
            int i = 0;
            JSONObject jSONObject2 = null;
            while (i < jSONArrayOptJSONArray.length()) {
                DownloadParams downloadParams = new DownloadParams();
                try {
                    String strOptString2 = jSONObject.optString("threadnum");
                    if (!TextUtils.isEmpty(strOptString2)) {
                        Integer.parseInt(strOptString2);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                downloadParams.setIsParted(z2);
                downloadParams.setIsUiCallback(z);
                try {
                    jSONObject2 = jSONArrayOptJSONArray.getJSONObject(i);
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
                if (jSONObject2 != null) {
                    downloadParams.setTargetUrl(jSONObject2.optString("targeturl"));
                    downloadParams.setmChannel(Util.getCdnChannel(downloadParams.getTargetUrl()));
                    downloadParams.setFilePath(jSONObject2.optString("filepath"));
                    downloadParams.setmUrlResName(Util.getSuffixFromUrl(jSONObject2.optString("targeturl")));
                    downloadParams.setOriginUrl(Util.getPrefixFromUrl(jSONObject2.optString("targeturl")));
                    downloadParams.setUrlPrefix(Util.getPrefixFromUrl(jSONObject2.optString("targeturl")));
                    if (jSONObject2.has("first") && jSONObject2.has("last")) {
                        LogUtil.i(TAG, "\u53c2\u6570\u9009\u62e9first last\u65b9\u5f0f\uff0c\u5ffd\u7565size\u5b57\u6bb5");
                        try {
                            downloadParams.setStart(Integer.parseInt(jSONObject2.optString("first")));
                            downloadParams.setLast(Integer.parseInt(jSONObject2.optString("last")));
                            last = downloadParams.getLast() - downloadParams.getStart();
                        } catch (NumberFormatException | Exception unused2) {
                            last = -100;
                        }
                    } else {
                        LogUtil.i(TAG, "\u53c2\u6570\u9009\u62e9size\u65b9\u5f0f\uff0c\u5ffd\u7565first last\u5b57\u6bb5");
                        last = Integer.parseInt(jSONObject2.optString("size"));
                    }
                    LogUtil.i(TAG, "\u6700\u7ec8\u7684size\u4e3a=" + last + ", \u5934\u90e8=" + downloadParams.getStart() + ", \u5c3e\u90e8=" + downloadParams.getLast());
                    downloadParams.setSize(last);
                    downloadParams.setMd5(jSONObject2.optString(Const.KEY_MD5));
                    Const.TYPE_TARGET_NORMAL.equals(str);
                    j2 += last;
                }
                downloadParams.setFileId(downloadParams.hashCode() + "");
                StringBuilder sb = new StringBuilder("params=");
                sb.append(downloadParams.toString());
                LogUtil.i(TAG, sb.toString());
                arrayList.add(downloadParams);
                i++;
                z = true;
                z2 = false;
            }
            j = j2;
        }
        LogUtil.i(TAG, "allSize=" + j);
        TaskHandleOp.getInstance().getTaskHandle().setTaskAllSizes(j);
        DownloadListenerProxy.getInstances();
        DownloadListenerProxy.setmTotalSize(j);
        LogUtil.i(TAG, "\u6240\u6709\u6587\u4ef6\u603b\u5927\u5c0f=" + j);
        LogUtil.i(TAG, "\u5df2\u7ecf\u4e0b\u8f7d\u597d\u7684\u603b\u5927\u5c0f\u4e3a=" + Util.getDownloadedSize(arrayList));
        return arrayList;
    }

    private DownloadParams(DownloadParams downloadParams, int i, long j, long j2, String str, float f) {
        setmUrlResName(downloadParams.getUrlResName());
        setFilePath(downloadParams.getFilePath() + "_" + i);
        setUserFileName(downloadParams.getUserFileName());
        setmWriteToExistFile(downloadParams.ismWriteToExistFile());
        setIsUiCallback(false);
        setPartIndex(i + 1);
        setSize(downloadParams.getSize());
        setRealFileSize(downloadParams.getRealFileSize());
        setIsParted(true);
        setStart(j);
        setmPriSegmentStart(downloadParams.getmPriSegmentStart());
        setMd5(downloadParams.getMd5());
        setFileId(downloadParams.getFileId());
        setmWoffset(downloadParams.getmWoffset());
        setLast(j2);
        setOriginUrl(downloadParams.getOriginUrl());
        setPartCount(downloadParams.getPartCount());
        setUrlPrefix(Util.replaceDomainWithIpAddr(downloadParams.getUrlPrefix(), str, "/"));
        setmSpeedLimit(f);
    }

    public class Element {
        public String mElementFilePath;
        public long mElementLast;
        public String mElementMd5;
        public long mElementStart;

        public Element(String str, long j, long j2, String str2) {
            this.mElementFilePath = str;
            this.mElementStart = j;
            this.mElementLast = j2;
            this.mElementMd5 = str2;
        }

        public String getmElementFilePath() {
            return this.mElementFilePath;
        }

        public void setmElementFilePath(String str) {
            this.mElementFilePath = str;
        }

        public long getmElementStart() {
            return this.mElementStart;
        }

        public void setmElementStart(long j) {
            this.mElementStart = j;
        }

        public long getmElementLast() {
            return this.mElementLast;
        }

        public void setmElementLast(long j) {
            this.mElementLast = j;
        }

        public String getmElementMd5() {
            return this.mElementMd5;
        }

        public void setmElementMd5(String str) {
            this.mElementMd5 = str;
        }

        public String toString() {
            return "mElementFilePath=" + this.mElementFilePath + ", mElementStart=" + this.mElementStart + ", mElementLast=" + this.mElementLast + ", mElementMd5=" + this.mElementMd5;
        }
    }

    public String toString() {
        return "DownloadParams{mCdnUrl = '" + this.mCdnUrl + "', mOriginUrl = '" + this.mOriginUrl + "', mChannel = '" + this.mChannel + "', mUrlResName = '" + this.mUrlResName + "', mLocalPath = '" + this.mFilePath + "', mUserFileName=" + this.mUserFileName + "', mMd5 = '" + this.mMd5 + "', mSize = " + this.mSize + ", mRealFileSize = " + this.mRealFileSize + ", mRenew = " + this.mRenew + ", mIsUiCallback = " + this.mIsUiCallback + ", mCurPart = " + this.mPartIndex + ", mPartCount = " + this.mPartCount + ", mFileId = " + this.mFileId + ", mStart = " + this.mStart + ", mLast = " + this.mLast + ", mWoffset = " + this.mWoffset + "', mElementList = " + this.mElementList.toString() + "'}";
    }
}