package com.netease.download.downloadpart;

import android.text.TextUtils;
import com.facebook.hermes.intl.Constants;
import com.netease.download.Const;
import com.netease.download.check.BackUpIpProxy;
import com.netease.download.check.CheckTime;
import com.netease.download.dns.CdnIpController;
import com.netease.download.downloader.DownloadParams;
import com.netease.download.downloader.DownloadProxy;
import com.netease.download.downloader.FileHandle;
import com.netease.download.downloader.TaskHandleOp;
import com.netease.download.handler.Dispatcher;
import com.netease.download.httpdns.HttpdnsProxy;
import com.netease.download.network.NetController;
import com.netease.download.util.HashUtil;
import com.netease.download.util.LogUtil;
import com.netease.download.util.Util;
import com.netease.ntunisdk.okhttp3.Call;
import com.netease.ntunisdk.okhttp3.Callback;
import com.netease.ntunisdk.okhttp3.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes3.dex */
public class DownloadAllCore implements Callable<Integer> {
    private static final String TAG = "DownloadAllCore";
    private static long mUseTime;
    private CheckTime mCheckTime;
    private int mCode;
    private DownloadParams[] mPartParams;
    private long mTotalFileSize;
    private DownloadParams mDownloadParams = null;
    private String mHost = null;
    private int mRetry = 3;
    private int mMd5FailRetryDownloadCount = 2;
    private HashMap<String, String> mLogData = new HashMap<>();
    private boolean mIsUseHistoryTopSpeedIp = false;
    private DownloadCallBack mDownloadCallBack = null;
    private int mIndex = -1;

    public void init(DownloadParams downloadParams, DownloadCallBack downloadCallBack, int i) {
        this.mDownloadParams = downloadParams;
        this.mDownloadCallBack = downloadCallBack;
        this.mIndex = i;
    }

    public void initData(DownloadParams downloadParams) {
        String overSea = TaskHandleOp.getInstance().getTaskHandle().getOverSea();
        if ("-1".equals(overSea)) {
            return;
        }
        if ("0".equals(overSea)) {
            Const.setReqIpsForWs(Const.REQ_IPS_WS_CHINA);
            Const.REQ_IPS_FOR_LOG = Const.REQ_IPS_FOR_LOG_CHINA;
        } else if ("1".equals(overSea)) {
            Const.setReqIpsForWs(Const.REQ_IPS_WS_OVERSEA);
            Const.REQ_IPS_FOR_LOG = Const.REQ_IPS_FOR_LOG_OVERSEA;
        } else if ("2".equals(overSea)) {
            Const.setReqIpsForWs(Const.REQ_IPS_WS_OVERSEA);
            Const.REQ_IPS_FOR_LOG = Const.REQ_IPS_FOR_LOG_OVERSEA;
            Const.URL_LOG = "sigma-orbit-impression.proxima.nie.easebar.com";
        }
    }

    public int start() {
        long length;
        try {
            length = new File(this.mDownloadParams.getFilePath()).length();
        } catch (Exception e) {
            LogUtil.i(TAG, "DownloadAllCore [start] Exception=" + e.toString());
            e.printStackTrace();
            length = -1;
        }
        LogUtil.i(TAG, "[ORBIT] (" + Thread.currentThread().getId() + ") Download URL=\"" + this.mDownloadParams.getDownloadUrl() + "\" Size=" + this.mDownloadParams.getSize() + " DownloadedSize=" + length + " first=" + this.mDownloadParams.getStart() + " last=" + this.mDownloadParams.getLast() + " Md5=\"" + this.mDownloadParams.getMd5() + "\" Filepath=\"" + this.mDownloadParams.getFilePath() + "\"");
        if (NetController.getInstances().isInterrupted()) {
            LogUtil.i(TAG, "\u7f51\u7edc\u5f02\u5e38=" + NetController.getInstances().getInterruptedCode());
            if (13 == NetController.getInstances().getInterruptedCode()) {
                return 13;
            }
            if (12 == NetController.getInstances().getInterruptedCode()) {
                return 12;
            }
        }
        return download(this.mDownloadParams, Const.Stage.NORMAL, 0);
    }

    public int download(DownloadParams downloadParams, Const.Stage stage, int i) {
        LogUtil.i(TAG, "\u662f\u5426\u5b58\u5728httpdns_config_cnd=" + HttpdnsProxy.getInstances().containKey(Const.HTTPDNS_CONFIG_CND));
        LogUtil.i(TAG, "\u662f\u5426\u8fd8\u5b58\u5728\u6ca1\u6709\u4f7f\u7528\u7684ip=" + HttpdnsProxy.getInstances().hasNext(Const.HTTPDNS_CONFIG_CND));
        if (HttpdnsProxy.getInstances().containKey(Const.HTTPDNS_CONFIG_CND) && !HttpdnsProxy.getInstances().hasNext(Const.HTTPDNS_CONFIG_CND) && BackUpIpProxy.getInstances().neverUseBackUpIp()) {
            LogUtil.i(TAG, "\u505a\u4e86httpdns\u89e3\u6790\uff0c\u5df2\u7ecf\u6ca1\u6709ip\u53ef\u4ee5\u4f7f\u7528\u4e86");
            TaskHandleOp.getInstance().getTaskHandle().setStatus(0);
            DownloadProxy.stopAll();
        } else if (!CdnIpController.getInstances().hasNextIp() && BackUpIpProxy.getInstances().neverUseBackUpIp()) {
            LogUtil.i(TAG, "\u53ea\u505adns\u89e3\u6790\uff0c\u5df2\u7ecf\u6ca1\u6709ip\u53ef\u4ee5\u4f7f\u7528\u4e86");
            TaskHandleOp.getInstance().getTaskHandle().setStatus(0);
            DownloadProxy.stopAll();
        }
        this.mCode = downloadParams.hashCode();
        Dispatcher.getTaskParamsMap().put(downloadParams.getFileId(), new FileHandle(downloadParams));
        initData(downloadParams);
        this.mLogData.put("httpdns", Constants.CASEFIRST_FALSE);
        int iDownload_core = download_core(downloadParams, stage, i);
        LogUtil.i(TAG, "\u6587\u4ef6\u540d=" + downloadParams.getFilePath() + ", \u603b\u4e0b\u8f7d\u4e0b\u8f7d\u7ed3\u679c=" + iDownload_core);
        return iDownload_core;
    }

    public int check(DownloadParams downloadParams) {
        LogUtil.i(TAG, "DownloadAllCore [check] start");
        LogUtil.i(TAG, "DownloadAllCore [check] \u5c06\u8981\u4e0b\u8f7d\u6587\u4ef6\u7684\u5168\u8def\u5f84=" + downloadParams.getFilePath());
        File file = new File(downloadParams.getFilePath());
        if (!downloadParams.ismWriteToExistFile() && !TaskHandleOp.getInstance().getTaskHandle().isMergeMode()) {
            if (file.exists()) {
                String md5 = downloadParams.getMd5();
                long size = downloadParams.getSize();
                long length = file.length();
                if ("NotMD5".equals(md5)) {
                    if (size == length) {
                        LogUtil.i(TAG, "DownloadAllCore [check] \u6587\u4ef6\u5df2\u7ecf\u5b58\u5728\uff0c\u8bbe\u7f6e\u4e86NotMD5\uff0c\u6587\u4ef6\u5927\u5c0f\u662f\u5bf9\u7684");
                        return 0;
                    }
                    LogUtil.i(TAG, "DownloadAllCore [check] \u6587\u4ef6\u5df2\u7ecf\u5b58\u5728\uff0c\u8bbe\u7f6e\u4e86NotMD5\uff0c\u6587\u4ef6\u5927\u5c0f\u662f\u9519\u7684");
                    return 2;
                }
                if (md5.equals(HashUtil.calculateFileHash(TaskHandleOp.getInstance().getTaskHandle().getEncryptionAlgorithm(), file.getAbsolutePath()))) {
                    LogUtil.i(TAG, "DownloadAllCore [check] \u6587\u4ef6\u5df2\u7ecf\u5b58\u5728\uff0c\u8bbe\u7f6e\u4e86md5\uff0c\u4e14md5\u9a8c\u8bc1\u901a\u8fc7");
                    return 0;
                }
                LogUtil.i(TAG, "DownloadAllCore [check] \u6587\u4ef6\u5df2\u7ecf\u5b58\u5728\uff0c\u8bbe\u7f6e\u4e86md5\uff0c\u4f46\u662fmd5\u9a8c\u8bc1\u4e0d\u901a\u8fc7");
                return 1;
            }
            LogUtil.i(TAG, "DownloadAllCore [check] \u6587\u4ef6\u4e0d\u5b58\u5728");
            return 4;
        }
        LogUtil.i(TAG, "DownloadAllCore [check] \u76f4\u63a5\u63d2\u5165\u5230\u5df2\u6709\u6587\u4ef6\uff0c\u8be5\u6a21\u5f0f\u4e0b\uff0c\u4e0d\u9700\u8981\u5bf9\u5df2\u6709\u6587\u4ef6\u8fdb\u884c\u68c0\u9a8c\u6216\u8005\u5220\u9664\u5de5\u4f5c");
        return 5;
    }

    public int handleViaFileCheckStatus(int i, File file) {
        LogUtil.i(TAG, "DownloadAllCore [handleViaFileCheckStatus] start");
        LogUtil.i(TAG, "DownloadAllCore [handleViaFileCheckStatus] fileCheckStatus=" + i);
        if (i == 0) {
            TaskHandleOp.getInstance().addTaskFileHasSuccessCount();
            TaskHandleOp.getInstance().addTaskHasDownloadVerifySizes(file.length());
            LogUtil.i(TAG, "DownloadAllCore [handleViaFileCheckStatus] \u6587\u4ef6\u662f\u5408\u683c\u7684\uff0c \u76f4\u63a5\u8fd4\u56de\u7ed9\u63a5\u5165\u65b9");
            return 1;
        }
        if (1 == i) {
            if (!TaskHandleOp.getInstance().getTaskHandle().isRenew()) {
                LogUtil.i(TAG, "DownloadAllCore [handleViaFileCheckStatus] \u6ca1\u6709\u8bbe\u7f6eisRenew\uff0c \u76f4\u63a5\u8fd4\u56de\u7ed9\u63a5\u5165\u65b9");
                return 1;
            }
            LogUtil.i(TAG, "DownloadAllCore [handleViaFileCheckStatus] \u6587\u4ef6MD5ERROR\uff0c\u8bbe\u7f6e\u4e86isRenew\uff0c \u5185\u90e8\u5220\u9664\u8be5\u6587\u4ef6\uff0c\u91cd\u65b0\u4e0b\u8f7d");
            file.delete();
        } else if (2 == i) {
            if (!TaskHandleOp.getInstance().getTaskHandle().isRenew()) {
                LogUtil.i(TAG, "DownloadAllCore [handleViaFileCheckStatus] \u6ca1\u6709\u8bbe\u7f6eisRenew\uff0c \u76f4\u63a5\u8fd4\u56de\u7ed9\u63a5\u5165\u65b9");
                return 1;
            }
            LogUtil.i(TAG, "DownloadAllCore [handleViaFileCheckStatus] \u6587\u4ef6SIZEERROR \u8bbe\u7f6e\u4e86isRenew\uff0c \u5185\u90e8\u5220\u9664\u8be5\u6587\u4ef6\uff0c\u91cd\u65b0\u4e0b\u8f7d");
            file.delete();
        }
        return 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:376:0x0402  */
    /* JADX WARN: Removed duplicated region for block: B:378:0x0416  */
    /* JADX WARN: Type inference failed for: r4v1 */
    /* JADX WARN: Type inference failed for: r4v113 */
    /* JADX WARN: Type inference failed for: r4v37 */
    /* JADX WARN: Type inference failed for: r4v38 */
    /* JADX WARN: Type inference failed for: r4v42 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int download_core(com.netease.download.downloader.DownloadParams r26, com.netease.download.Const.Stage r27, int r28) {
        /*
            Method dump skipped, instructions count: 3034
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.download.downloadpart.DownloadAllCore.download_core(com.netease.download.downloader.DownloadParams, com.netease.download.Const$Stage, int):int");
    }

    /* renamed from: com.netease.download.downloadpart.DownloadAllCore$1 */
    class AnonymousClass1 implements Callback {
        final /* synthetic */ int val$code;
        final /* synthetic */ DownloadParams val$pParams;

        AnonymousClass1(int i, DownloadParams downloadParams) {
            i = i;
            downloadParams = downloadParams;
        }

        @Override // com.netease.ntunisdk.okhttp3.Callback
        public void onResponse(Call call, Response response) throws NumberFormatException, IOException {
            LogUtil.i(DownloadAllCore.TAG, "DownloadAllCore [okhttpCallback] [onResponse] start");
            if (call == null || response == null) {
                return;
            }
            LogUtil.i(DownloadAllCore.TAG, "DownloadAllCore [okhttpCallback] [onResponse] (" + i + ") Call processHeader=" + call.request().headers().toMultimap().toString() + ", protocol=" + response.protocol().toString() + ", request = " + response.request().toString());
            LogUtil.i(DownloadAllCore.TAG, "DownloadAllCore [okhttpCallback] [onResponse] (" + i + ") Response processHeader=" + response.headers().toMultimap().toString() + ", hashCode=" + response.code() + ", resUrl=" + response.request().url() + ", protocol=" + response.protocol() + ", " + response.request().toString());
            int iCode = response.code();
            String string = call.request().url().toString();
            StringBuilder sb = new StringBuilder("DownloadAllCore [okhttpCallback] [onResponse] pCode=");
            sb.append(iCode);
            sb.append(", resUrl=");
            sb.append(string);
            LogUtil.i(DownloadAllCore.TAG, sb.toString());
            long j = Long.parseLong(response.headers().get("content-length"));
            StringBuilder sb2 = new StringBuilder("DownloadAllCore [okhttpCallback] [onResponse] totalSize = ");
            sb2.append(j);
            LogUtil.i(DownloadAllCore.TAG, sb2.toString());
            DownloadAllCore.this.setTotalFileSize(j);
            LogUtil.i(DownloadAllCore.TAG, "DownloadAllCore [okhttpCallback] [onResponse] close");
        }

        @Override // com.netease.ntunisdk.okhttp3.Callback
        public void onFailure(Call call, IOException iOException) {
            LogUtil.i(DownloadAllCore.TAG, "DownloadAllCore [okhttpCallback] [onFailure] start");
            LogUtil.stepLog("ConfigCore [okhttpCallback] [onFailure] start");
            if (call == null) {
                return;
            }
            LogUtil.i(DownloadAllCore.TAG, "ConfigCore [okhttpCallback] [onFailure] Call Header=" + call.request().headers().toMultimap().toString() + ", request = " + call.request().toString());
            Object objTag = call.request().tag();
            int iIntValue = objTag != null ? ((Integer) objTag).intValue() : 1;
            String string = call.request().url().toString();
            Util.getDomainFromUrl(string);
            Util.replaceDomainWithIpAddr(string, DownloadAllCore.this.mHost, "/");
            if (200 != iIntValue) {
                FileHandle fileHandle = Dispatcher.getTaskParamsMap().get(downloadParams.getFileId());
                String errorcdn = fileHandle != null ? fileHandle.getErrorcdn() : "";
                String str = downloadParams.getmChannel();
                StringBuffer stringBuffer = new StringBuffer();
                if (errorcdn != null) {
                    stringBuffer.append(errorcdn).append(",");
                }
                stringBuffer.append(str);
                if (fileHandle != null) {
                    fileHandle.setErrorcdn(stringBuffer.toString());
                }
                ConcurrentHashMap<String, Integer> cdnerrorMap = fileHandle != null ? fileHandle.getCdnerrorMap() : null;
                if (cdnerrorMap == null || cdnerrorMap.containsKey(str)) {
                    return;
                }
                cdnerrorMap.put(str, Integer.valueOf(iIntValue));
            }
        }
    }

    private long getContentLength(Map<String, List<String>> map) {
        if (map == null) {
            return 0L;
        }
        List<String> list = map.containsKey("Content-Length") ? map.get("Content-Length") : null;
        if (list != null && !list.isEmpty()) {
            String str = list.get(0);
            LogUtil.d(TAG, "processHeader, value=" + str);
            if (TextUtils.isDigitsOnly(str)) {
                return Long.valueOf(str).longValue();
            }
        }
        LogUtil.w(TAG, "no Content-Length found");
        return 0L;
    }

    public long getTotalFileSize() {
        return this.mTotalFileSize;
    }

    public void setTotalFileSize(long j) {
        this.mTotalFileSize = j;
    }

    private DownloadParams[] produceSegmentParams(DownloadParams downloadParams, long j) {
        DownloadParams[] downloadParamsArr;
        int i;
        int i2;
        long last;
        int partCount = downloadParams.getPartCount();
        DownloadParams[] downloadParamsArr2 = new DownloadParams[partCount];
        int channelWeight = CdnIpController.getInstances().getChannelWeight(downloadParams.getmChannel());
        LogUtil.i(TAG, "\u603b\u6743\u91cd=" + channelWeight + ", \u5206\u7247\u6570=" + partCount + ", \u539f\u59cb\u94fe\u63a5=" + downloadParams.getOriginUrl());
        ArrayList<String> host = CdnIpController.getInstances().getHost(downloadParams.getmChannel());
        float netThreadSpeedLimit = TaskHandleOp.getInstance().getTaskHandle().getNetThreadSpeedLimit() / ((float) partCount);
        LogUtil.i(TAG, "DownloadAllCore [produceSegmentParams] netThreadSpeedLimit=" + TaskHandleOp.getInstance().getTaskHandle().getNetThreadSpeedLimit() + ", netSpeedLimit=" + netThreadSpeedLimit + ", num=" + partCount);
        String str = "";
        int i3 = 0;
        if (channelWeight != 0) {
            LogUtil.stepLog("\u6309\u6743\u91cd\u5206");
            ArrayList<Integer> weights = CdnIpController.getInstances().getWeights(downloadParams.getmChannel());
            long start = downloadParams.getStart();
            long last2 = downloadParams.getLast();
            if (host == null) {
                return downloadParamsArr2;
            }
            if (1 == partCount) {
                long last3 = downloadParams.getLast() == 0 ? j : downloadParams.getLast();
                LogUtil.i(TAG, "weight[i] pOriginalParams.getSegmentEnd()=" + downloadParams.getLast() + ", pTotalSize=" + j);
                i = 0;
                DownloadParams downloadParamsProduceSegment = downloadParams.produceSegment(0, start, last3 - 1, host.size() > 0 ? host.get(0) : "", netThreadSpeedLimit);
                downloadParamsArr2[0] = downloadParamsProduceSegment;
                downloadParamsProduceSegment.setmChannel(downloadParams.getmChannel());
                downloadParamsArr2[0].setmMergeOffset(0L);
            } else {
                i = 0;
                long j2 = start;
                while (i3 < weights.size()) {
                    String str2 = str;
                    LogUtil.i(TAG, "weight[i]=" + weights.get(i3));
                    long j3 = j2;
                    int i4 = partCount;
                    DownloadParams[] downloadParamsArr3 = downloadParamsArr2;
                    long jIntValue = (weights.get(i3).intValue() * j) / channelWeight;
                    long j4 = i3 != 0 ? last2 + 1 : j3;
                    if (i3 == weights.size() - 1) {
                        last = 0 == downloadParams.getLast() ? j - 1 : downloadParams.getLast() - 1;
                    } else {
                        last = (jIntValue + j4) - 1;
                    }
                    long j5 = last;
                    int i5 = channelWeight;
                    int i6 = i3;
                    DownloadParams downloadParamsProduceSegment2 = downloadParams.produceSegment(i3, j4, j5, host.size() > i3 ? host.get(i3) : str2, netThreadSpeedLimit);
                    downloadParamsArr3[i6] = downloadParamsProduceSegment2;
                    downloadParamsProduceSegment2.setmChannel(downloadParams.getmChannel());
                    downloadParamsArr3[i6].setmMergeOffset(j4 - start);
                    LogUtil.i(TAG, "\u5206\u7247\u53c2\u6570\u751f\u6210\uff0c\u5206\u7247=" + i6 + ", start=" + j4 + ", end=" + j5);
                    i3 = i6 + 1;
                    last2 = j5;
                    weights = weights;
                    channelWeight = i5;
                    j2 = j4;
                    partCount = i4;
                    str = str2;
                    downloadParamsArr2 = downloadParamsArr3;
                }
            }
            i2 = partCount;
            downloadParamsArr = downloadParamsArr2;
        } else {
            downloadParamsArr = downloadParamsArr2;
            i = 0;
            i2 = partCount;
            LogUtil.stepLog("\u5e73\u5747\u5206");
            long j6 = i2;
            long j7 = j / j6;
            long j8 = j - (j6 * j7);
            long start2 = downloadParams.getStart();
            long j9 = (j7 + j8) - 1;
            long j10 = start2;
            int i7 = 0;
            while (i7 != i2) {
                DownloadParams downloadParamsProduceSegment3 = downloadParams.produceSegment(i7, j10, j9, host.size() > i7 ? host.get(i7) : "", netThreadSpeedLimit);
                downloadParamsArr[i7] = downloadParamsProduceSegment3;
                downloadParamsProduceSegment3.setmMergeOffset(j10 - start2);
                j10 = j9 + 1;
                j9 = (j10 + j7) - 1;
                i7++;
            }
        }
        LogUtil.i(TAG, "\u5206\u7247\u53c2\u6570\u4e2a\u6570=" + i2);
        for (int i8 = i; i8 < i2; i8++) {
            LogUtil.i(TAG, "\u5206\u7247\u53c2\u6570=" + downloadParamsArr[i8].toString());
        }
        return downloadParamsArr;
    }

    private void setPartParams(DownloadParams[] downloadParamsArr) {
        this.mPartParams = downloadParamsArr;
    }

    private DownloadParams[] getPartParams() {
        return this.mPartParams;
    }

    private boolean mergeFiles(File file) {
        LogUtil.i(TAG, "\u5408\u5e76\u524d\u7684\u6587\u4ef6\u8def\u5f84=" + file.getAbsolutePath() + ", \u5927\u5c0f=" + file.length());
        boolean zRenameTo = false;
        if (1 == getPartParams().length) {
            LogUtil.i(TAG, "\u5408\u5e76\u524d\u7684\u6587\u4ef6\u8def\u5f84  getPartParams()11=" + getPartParams()[0].toString());
            String filePath = getPartParams()[0].getFilePath();
            LogUtil.i(TAG, "\u5408\u5e76\u524d\u7684\u6587\u4ef6\u8def\u5f84  getPartParams() filepath" + filePath);
            if (!TextUtils.isEmpty(filePath)) {
                File file2 = new File(filePath);
                if (file2.exists()) {
                    LogUtil.i(TAG, "\u5408\u5e76\u524d\u7684\u6587\u4ef6\u8def\u5f84  \u6587\u4ef6\u5927\u5c0f=" + file2.length());
                } else {
                    LogUtil.i(TAG, "\u5408\u5e76\u524d\u7684\u6587\u4ef6\u8def\u5f84  \u6587\u4ef6\u4e0d\u5b58\u5728");
                }
            } else {
                LogUtil.i(TAG, "\u5408\u5e76\u524d\u7684\u6587\u4ef6\u8def\u5f84  \u6587\u4ef6\u8def\u5f84\u4e0d\u5b58\u5728");
            }
            zRenameTo = new File(getPartParams()[0].getFilePath()).renameTo(file);
        } else {
            FileChannel channel = null;
            try {
                try {
                    channel = new FileOutputStream(file).getChannel();
                    for (DownloadParams downloadParams : getPartParams()) {
                        FileChannel channel2 = new FileInputStream(downloadParams.getFilePath()).getChannel();
                        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(32768);
                        while (channel2.read(byteBufferAllocate) != -1) {
                            byteBufferAllocate.flip();
                            channel.write(byteBufferAllocate);
                            byteBufferAllocate.clear();
                        }
                    }
                    if (channel != null) {
                        try {
                            channel.close();
                        } catch (IOException unused) {
                        }
                    }
                    zRenameTo = true;
                } catch (IOException e) {
                    e.printStackTrace();
                    if (channel != null) {
                        try {
                            channel.close();
                        } catch (IOException unused2) {
                        }
                    }
                }
            } catch (Throwable th) {
                if (channel != null) {
                    try {
                        channel.close();
                    } catch (IOException unused3) {
                    }
                }
                throw th;
            }
        }
        LogUtil.i(TAG, "\u5408\u5e76\u540e\u7684\u6587\u4ef6\u8def\u5f84=" + file.getAbsolutePath() + ", \u5927\u5c0f=" + file.length() + ", \u662f\u5426\u5408\u5e76\u6210\u529f=" + zRenameTo);
        return zRenameTo;
    }

    private boolean delFiles() {
        boolean z = true;
        for (DownloadParams downloadParams : getPartParams()) {
            File file = new File(downloadParams.getFilePath());
            z = z && (!file.exists() || file.delete());
        }
        return z;
    }

    private boolean isAllInterrupted(int[] iArr) {
        for (int i : iArr) {
            if (12 == i) {
                return true;
            }
        }
        return false;
    }

    @Override // java.util.concurrent.Callable
    public Integer call() throws Exception {
        int iStart;
        this.mDownloadParams.setmStartDownloadTime(System.currentTimeMillis());
        try {
            iStart = start();
        } catch (Exception e) {
            LogUtil.e(TAG, "DownloadAllCore Exception e=" + e);
            iStart = 11;
        }
        handleElement(this.mDownloadParams, iStart);
        LogUtil.i(TAG, "\u4e0b\u8f7d call\u7ed3\u675f\uff0c\u63a5\u4e0b\u6765\u5e94\u8be5\u8fd4\u56de\u5230\u7ebf\u7a0b\u6c60\u7684\u7ed3\u679c\u56de\u8c03 result=" + iStart);
        this.mDownloadCallBack.onFinish(iStart, this.mIndex, this.mDownloadParams);
        return Integer.valueOf(iStart);
    }

    /* JADX WARN: Removed duplicated region for block: B:229:0x0473  */
    /* JADX WARN: Removed duplicated region for block: B:241:0x04ff  */
    /* JADX WARN: Removed duplicated region for block: B:243:0x0518  */
    /* JADX WARN: Removed duplicated region for block: B:244:0x052b  */
    /* JADX WARN: Removed duplicated region for block: B:263:0x0453 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:267:0x05fc A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:281:? A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void handleElement(com.netease.download.downloader.DownloadParams r43, int r44) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 1855
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.download.downloadpart.DownloadAllCore.handleElement(com.netease.download.downloader.DownloadParams, int):void");
    }
}