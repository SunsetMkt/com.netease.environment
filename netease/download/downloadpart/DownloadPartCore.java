package com.netease.download.downloadpart;

import android.text.TextUtils;
import com.netease.download.Const;
import com.netease.download.UrlSwitcher.HttpdnsUrlSwitcherCore;
import com.netease.download.check.BackUpIpProxy;
import com.netease.download.config.ConfigParams;
import com.netease.download.config.ConfigProxy;
import com.netease.download.dns.CdnIpController;
import com.netease.download.dns.CdnUseTimeProxy;
import com.netease.download.downloader.DownloadParams;
import com.netease.download.downloader.TaskHandleOp;
import com.netease.download.handler.Dispatcher;
import com.netease.download.httpdns.HttpdnsProxy;
import com.netease.download.listener.DownloadListenerProxy;
import com.netease.download.network.NetController;
import com.netease.download.network.NetworkStatus;
import com.netease.download.network.OkHttpProxy;
import com.netease.download.util.LogUtil;
import com.netease.download.util.SpUtil;
import com.netease.download.util.Util;
import com.netease.ntunisdk.okhttp3.Call;
import com.netease.ntunisdk.okhttp3.Callback;
import com.netease.ntunisdk.okhttp3.Request;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/* loaded from: classes3.dex */
public class DownloadPartCore implements Callable<Integer> {
    private static final String TAG = "DownloadPartCore";
    private long mPartFileSize;
    private DownloadParams mDownloadParams = null;
    private String tmpFilePath = null;
    private Map<String, String> mHeader = null;
    private boolean mRestart = false;
    private String mIp = null;
    private String mHost = null;
    private boolean mIsLowSpeedRemove = false;
    private boolean mIsUseHistoryTopSpeedIp = false;
    private Callback okhttpCallback = new Callback() { // from class: com.netease.download.downloadpart.DownloadPartCore.1
        /* JADX WARN: Code restructure failed: missing block: B:40:0x0318, code lost:
        
            if (com.netease.download.downloader.TaskHandleOp.getInstance().getTaskHandle().isRammode() != false) goto L42;
         */
        /* JADX WARN: Code restructure failed: missing block: B:41:0x031a, code lost:
        
            r11 = r8;
            r7 = r0;
            r15.mark(r7);
            r35 = r9;
            r11.write(r2.array(), 0, r0);
            r15.calculate();
            com.netease.download.listener.DownloadListenerProxy.getInstances();
            com.netease.download.listener.DownloadListenerProxy.getDownloadListenerHandler().sendProgressMsg(com.netease.download.downloader.TaskHandleOp.getInstance().getTaskHandle().getTaskAllSizes(), r7, r45.this$0.mDownloadParams.getCallBackFileName(), r45.this$0.mDownloadParams.getFilePath(), com.netease.download.downloader.TaskHandleOp.getInstance().getTaskHandle().getDownloadId(), com.netease.download.downloader.TaskHandleOp.getInstance().getTaskHandle().getNtesOrbitId());
         */
        /* JADX WARN: Code restructure failed: missing block: B:42:0x0371, code lost:
        
            r11 = r8;
            r35 = r9;
            r15.calculate();
            com.netease.download.listener.DownloadListenerProxy.getInstances();
            com.netease.download.listener.DownloadListenerProxy.getDownloadListenerHandler().sendProgressMsg(com.netease.download.downloader.TaskHandleOp.getInstance().getTaskHandle().getTaskAllSizes(), r14, r45.this$0.mDownloadParams.getCallBackFileName(), r45.this$0.mDownloadParams.getFilePath(), com.netease.download.downloader.TaskHandleOp.getInstance().getTaskHandle().getDownloadId(), com.netease.download.downloader.TaskHandleOp.getInstance().getTaskHandle().getNtesOrbitId());
         */
        /* JADX WARN: Code restructure failed: missing block: B:43:0x03bc, code lost:
        
            r7 = r15.check(r45.this$0.mDownloadParams.getFileId(), r10, r45.this$0.mIp, r45.this$0.mHost, r45.this$0.mIsUseHistoryTopSpeedIp);
            r8 = java.lang.System.currentTimeMillis() - r12;
         */
        /* JADX WARN: Code restructure failed: missing block: B:44:0x03e8, code lost:
        
            if (r6 <= 0.0f) goto L52;
         */
        /* JADX WARN: Code restructure failed: missing block: B:45:0x03ea, code lost:
        
            r0 = ((r0 / r6) * 1000.0f) - r8;
         */
        /* JADX WARN: Code restructure failed: missing block: B:46:0x03f3, code lost:
        
            if (r0 <= 0.0f) goto L52;
         */
        /* JADX WARN: Code restructure failed: missing block: B:48:0x03f6, code lost:
        
            java.lang.Thread.sleep((long) r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:50:0x03fa, code lost:
        
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:51:0x03fb, code lost:
        
            r0.printStackTrace();
         */
        /* JADX WARN: Code restructure failed: missing block: B:60:0x0457, code lost:
        
            r11 = r8;
            r6 = false;
         */
        /* JADX WARN: Removed duplicated region for block: B:70:0x0524  */
        /* JADX WARN: Removed duplicated region for block: B:72:0x0527  */
        @Override // com.netease.ntunisdk.okhttp3.Callback
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void onResponse(com.netease.ntunisdk.okhttp3.Call r46, com.netease.ntunisdk.okhttp3.Response r47) throws java.lang.InterruptedException, java.lang.NumberFormatException, java.io.IOException {
            /*
                Method dump skipped, instructions count: 1885
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.netease.download.downloadpart.DownloadPartCore.AnonymousClass1.onResponse(com.netease.ntunisdk.okhttp3.Call, com.netease.ntunisdk.okhttp3.Response):void");
        }

        @Override // com.netease.ntunisdk.okhttp3.Callback
        public void onFailure(Call call, IOException iOException) {
            LogUtil.stepLog("DownloadPartCore [okhttpCallback] [onFailure] \u5206\u7247=" + DownloadPartCore.this.mDownloadParams.getPartIndex() + ", start");
            if (call == null) {
                return;
            }
            LogUtil.i(DownloadPartCore.TAG, "DownloadPartCore [okhttpCallback] [onFailure] \u5206\u7247=" + DownloadPartCore.this.mDownloadParams.getPartIndex() + ", Call Header=" + call.request().headers().toMultimap().toString() + ", request = " + call.request().toString());
            Object objTag = call.request().tag();
            int iIntValue = objTag != null ? ((Integer) objTag).intValue() : 1;
            String string = call.request().url().toString();
            LogUtil.i(DownloadPartCore.TAG, "DownloadPartCore [okhttpCallback] [onFailure] \u5206\u7247=" + DownloadPartCore.this.mDownloadParams.getPartIndex() + ", Code=" + iIntValue + ", resUrl=" + string);
            Util.getDomainFromUrl(string);
            if (Dispatcher.getTaskParamsMap().get(DownloadPartCore.this.mDownloadParams.getFileId()) != null) {
                Dispatcher.getTaskParamsMap().get(DownloadPartCore.this.mDownloadParams.getFileId()).getPartResultMap().put(Util.getCdnIndex(DownloadPartCore.this.mDownloadParams.getDownloadUrl()) + "retcode", Integer.valueOf(iIntValue));
            }
            LogUtil.i(DownloadPartCore.TAG, "[ORBIT] (" + Thread.currentThread().getId() + ") Download Params=\"" + DownloadPartCore.this.mDownloadParams.getUrlResName() + "\" Filepath=\"" + DownloadPartCore.this.mDownloadParams.getFilePath() + "\" Segment=" + DownloadPartCore.this.mDownloadParams.getPartIndex() + " Host=\"" + DownloadPartCore.this.mHost + "\" IP=" + DownloadPartCore.this.mIp + " Range=[" + DownloadPartCore.this.mDownloadParams.getStart() + "-" + DownloadPartCore.this.mDownloadParams.getLast() + "] Status=" + iIntValue);
        }
    };

    private void setPartFileSize(long j) {
        this.mPartFileSize = j;
    }

    private long getPartFileSize() {
        return this.mPartFileSize;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.concurrent.Callable
    public Integer call() throws Exception {
        return Integer.valueOf(start());
    }

    public void init(DownloadParams downloadParams) {
        this.mDownloadParams = downloadParams;
        TaskHandleOp.getInstance().getTaskHandle().getOverSea();
    }

    String tostr(byte[] bArr) {
        int i = 0;
        for (int i2 = 0; i2 < bArr.length; i2++) {
            try {
                if (bArr[i2] != 0) {
                    i = i2;
                }
            } catch (Exception unused) {
                return "";
            }
        }
        return new String(bArr, 0, i, "UTF-8");
    }

    public int start() {
        return downloadPart(this.mDownloadParams);
    }

    public static byte[] hexStringToBytes(String str) {
        if (str == null || str.equals("")) {
            return null;
        }
        String upperCase = str.toUpperCase();
        int length = upperCase.length() / 2;
        char[] charArray = upperCase.toCharArray();
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            bArr[i] = (byte) (charToByte(charArray[i2 + 1]) | (charToByte(charArray[i2]) << 4));
        }
        return bArr;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    private int checkPartCoreFile(File file, DownloadParams downloadParams) {
        LogUtil.i(TAG, "DownloadPartCore [checkPartCoreFile] \u5206\u7247=" + downloadParams.getPartIndex() + ", start");
        if (file == null || !file.exists() || downloadParams == null) {
            return 4;
        }
        if (file.length() == (downloadParams.getLast() - downloadParams.getStart()) + 1) {
            LogUtil.i(TAG, "DownloadPartCore [checkPartCoreFile] \u5206\u7247=" + downloadParams.getPartIndex() + ", \u5206\u7247\u6587\u4ef6\u5df2\u7ecf\u5b58\u5728\uff0c\u5e76\u4e14\u5927\u5c0f\u662f\u5bf9\u7684");
            return 0;
        }
        LogUtil.i(TAG, "DownloadPartCore [checkPartCoreFile] \u5206\u7247=" + downloadParams.getPartIndex() + ", \u5206\u7247\u6587\u4ef6\u5df2\u7ecf\u5b58\u5728\uff0c\u4f46\u662f\u5927\u5c0f\u662f\u4e0d\u5bf9");
        return 2;
    }

    private int handlePartCoreFile(File file, int i, DownloadParams downloadParams) {
        LogUtil.i(TAG, "DownloadPartCore [handlePartCoreFile] \u5206\u7247=" + downloadParams.getPartIndex() + ", start");
        if (file == null || downloadParams == null) {
            return 0;
        }
        if (i == 0) {
            LogUtil.i(TAG, "DownloadPartCore [handlePartCoreFile] \u5206\u7247=" + downloadParams.getPartIndex() + ", \u5206\u7247\u6587\u4ef6\u662f\u5408\u683c\u7684\uff0c \u76f4\u63a5\u8fd4\u56de\u5230\u4e0a\u5c42");
            TaskHandleOp.getInstance().addTaskHasDownloadVerifySizes(file.length());
            DownloadListenerProxy.getInstances();
            DownloadListenerProxy.getDownloadListenerHandler().addHasDownloadMag(file.length(), downloadParams.getFilePath(), downloadParams.getMd5(), downloadParams.getPartIndex());
            return 1;
        }
        if (2 == i) {
            LogUtil.i(TAG, "DownloadPartCore [handlePartCoreFile] \u5206\u7247=" + downloadParams.getPartIndex() + ", \u5220\u9664\u5206\u7247\u6587\u4ef6");
            file.delete();
            return 0;
        }
        if (4 != i) {
            return 0;
        }
        LogUtil.i(TAG, "DownloadPartCore [handlePartCoreFile] \u5206\u7247=" + downloadParams.getPartIndex() + ", \u65e0\u6cd5\u627e\u5230\u8be5\u5206\u7247\u6587\u4ef6");
        return 0;
    }

    private int checkPartTempFile(File file, DownloadParams downloadParams) {
        LogUtil.i(TAG, "DownloadPartCore [checkPartTempFile] \u5206\u7247=" + downloadParams.getPartIndex() + ", start");
        if (file == null || !file.exists() || downloadParams == null) {
            LogUtil.i(TAG, "DownloadPartCore [checkPartTempFile] \u5206\u7247=" + downloadParams.getPartIndex() + ", \u65e0\u6cd5\u627e\u5230\u8be5\u5206\u7247\u4e34\u65f6\u6587\u4ef6");
            return 4;
        }
        if (file.length() == (downloadParams.getLast() - downloadParams.getStart()) + 1) {
            LogUtil.i(TAG, "DownloadPartCore [checkPartTempFile] \u5206\u7247=" + downloadParams.getPartIndex() + ", \u5206\u7247\u4e34\u65f6\u6587\u4ef6\u5df2\u7ecf\u5b58\u5728\uff0c\u5e76\u4e14\u5927\u5c0f\u662f\u5bf9\u7684");
            return 0;
        }
        if (file.length() > (downloadParams.getLast() - downloadParams.getStart()) + 1) {
            LogUtil.i(TAG, "DownloadPartCore [checkPartTempFile] \u5206\u7247=" + downloadParams.getPartIndex() + ", \u5206\u7247\u4e34\u65f6\u6587\u4ef6\u5df2\u7ecf\u5b58\u5728\uff0c\u6587\u4ef6size\u5927\u4e8e\u53c2\u6570size");
            return 2;
        }
        LogUtil.i(TAG, "DownloadPartCore [checkPartTempFile] \u5206\u7247=" + downloadParams.getPartIndex() + ", \u5206\u7247\u4e34\u65f6\u6587\u4ef6\u5df2\u7ecf\u5b58\u5728\uff0c \u8be5\u6587\u4ef6\u672a\u4e0b\u8f7d\u5b8c\u5168");
        return 3;
    }

    private int handlePartTempFile(File file, File file2, int i, DownloadParams downloadParams) {
        LogUtil.i(TAG, "DownloadPartCore [handlePartTempFile] \u5206\u7247=" + downloadParams.getPartIndex() + ", start");
        if (file == null || downloadParams == null) {
            return 0;
        }
        if (i == 0) {
            file.renameTo(file2);
            File file3 = new File(downloadParams.getFilePath());
            DownloadListenerProxy.getInstances();
            DownloadListenerProxy.getDownloadListenerHandler().addHasDownloadMag(file.length(), this.tmpFilePath, downloadParams.getMd5(), downloadParams.getPartIndex());
            TaskHandleOp.getInstance().addTaskHasDownloadVerifySizes(file3.length());
            LogUtil.i(TAG, "DownloadPartCore [handlePartTempFile] \u5206\u7247=" + downloadParams.getPartIndex() + ", \u5206\u7247\u4e34\u65f6\u6587\u4ef6\u662f\u5408\u683c\u7684\uff0c\u66f4\u540d\u540e\uff0c  \u76f4\u63a5\u8fd4\u56de\u5230\u4e0a\u5c42");
            return 1;
        }
        if (2 == i) {
            LogUtil.i(TAG, "DownloadPartCore [handlePartTempFile] \u5206\u7247=" + downloadParams.getPartIndex() + ", \u6587\u4ef6SIZEERROR  \u5185\u90e8\u5220\u9664\u8be5\u5206\u7247\u4e34\u65f6\u6587\u4ef6");
            file.delete();
            return 0;
        }
        if (3 != i) {
            return 0;
        }
        LogUtil.i(TAG, "DownloadPartCore [handlePartTempFile] \u5206\u7247=" + downloadParams.getPartIndex() + ", \u6587\u4ef6NOTCOMPLETE  \u7ee7\u7eed\u4e0b\u8f7d");
        DownloadListenerProxy.getInstances();
        DownloadListenerProxy.getDownloadListenerHandler().addHasDownloadMag(file.length(), this.tmpFilePath, downloadParams.getMd5(), downloadParams.getPartIndex());
        return 0;
    }

    private int createTempFile(File file, DownloadParams downloadParams) throws IOException {
        if (!file.exists()) {
            try {
                if (!file.getParentFile().exists()) {
                    LogUtil.i(TAG, "DownloadPartCore [downloadPart] \u5206\u7247=" + downloadParams.getPartIndex() + ", \u751f\u6210\u7236\u76ee\u5f55=" + file.getParentFile().getAbsolutePath());
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            } catch (Exception e) {
                LogUtil.i(TAG, "DownloadPartCore [downloadPart] \u5206\u7247=" + downloadParams.getPartIndex() + ", Exception=" + e.toString());
                e.printStackTrace();
            }
            if (!file.exists()) {
                LogUtil.w(TAG, "DownloadPartCore [downloadPart] \u5206\u7247=" + downloadParams.getPartIndex() + ", \u6587\u4ef6\u751f\u6210\u5f02\u5e38\uff0c\u6587\u4ef6\u540d\u5b57=" + this.tmpFilePath);
                return 11;
            }
        }
        return 0;
    }

    private int downloadPart(DownloadParams downloadParams) throws IOException {
        File file;
        int iDownloadPart;
        LogUtil.i(TAG, "DownloadPartCore [downloadPart] \u5206\u7247\u4e0b\u8f7d\u5f00\u59cb, \u5206\u7247=" + downloadParams.getPartIndex());
        LogUtil.i(TAG, "DownloadPartCore [downloadPart] \u5206\u7247=" + downloadParams.getPartIndex() + ", \u53c2\u6570=" + downloadParams);
        if (downloadParams == null || !downloadParams.isValid()) {
            LogUtil.e(TAG, "DownloadPartCore [downloadPart] \u5206\u7247=" + downloadParams.getPartIndex() + ", invalid params");
            return 14;
        }
        if (downloadParams.getLast() < downloadParams.getStart()) {
            LogUtil.e(TAG, "DownloadPartCore [downloadPart] \u5206\u7247=" + downloadParams.getPartIndex() + ", invalid params end=" + downloadParams.getLast() + ", start=" + downloadParams.getStart());
            return 14;
        }
        int iHashCode = downloadParams.hashCode();
        if (NetController.getInstances().isInterrupted()) {
            LogUtil.w(TAG, "DownloadPartCore [downloadPart] \u5206\u7247=" + downloadParams.getPartIndex() + ", \u7f51\u7edc\u539f\u56e0\uff0c\u4e0b\u8f7d\u88ab\u4e2d\u65ad");
            if (13 == NetController.getInstances().getInterruptedCode()) {
                return 13;
            }
            if (12 == NetController.getInstances().getInterruptedCode()) {
                return 12;
            }
        }
        LogUtil.w(TAG, "DownloadPartCore [downloadPart] \u5206\u7247=" + downloadParams.getPartIndex() + ", TaskHandleOp.getInstance().getTaskHandle().isRammode()=" + TaskHandleOp.getInstance().getTaskHandle().isRammode());
        if (TaskHandleOp.getInstance().getTaskHandle().isRammode()) {
            file = null;
        } else {
            this.tmpFilePath = downloadParams.getFilePath() + "_tmp";
            file = new File(this.tmpFilePath);
            File file2 = new File(downloadParams.getFilePath());
            if (1 == handlePartCoreFile(file2, checkPartCoreFile(file2, downloadParams), downloadParams)) {
                return 0;
            }
            if (1 == handlePartTempFile(file, file2, checkPartTempFile(file, downloadParams), downloadParams)) {
                LogUtil.i(TAG, "DownloadAllCore [downloadPart] AVAILABLE  \u76f4\u63a5\u8fd4\u56de\u7ed9\u63a5\u5165\u65b9");
                return 0;
            }
            createTempFile(file, downloadParams);
        }
        this.mHeader = new HashMap();
        Request.Builder builderUrl = new Request.Builder().url(downloadParams.getDownloadUrl());
        long length = !TaskHandleOp.getInstance().getTaskHandle().isRammode() ? file.length() : 0L;
        LogUtil.i(TAG, "DownloadPartCore [downloadPart] \u5206\u7247=" + downloadParams.getPartIndex() + ", \u5206\u7247\u4e0b\u8f7d\uff0c\u6587\u4ef6\u540d=" + this.tmpFilePath + "\uff0c \u4e4b\u524d\u4e0b\u8f7d\u597d\u7684\u6587\u4ef6\u7684\u5927\u5c0f=" + length);
        SpUtil.getInstance().getLong(Integer.valueOf(iHashCode), "time", 0L);
        long start = length + downloadParams.getStart();
        if (builderUrl != null) {
            builderUrl.addHeader("START", start + "");
            builderUrl.addHeader("END", String.valueOf(downloadParams.getLast()));
        }
        TaskHandleOp.getInstance().addTaskCurTimeDownloadSizes((downloadParams.getLast() - start) + 1);
        LogUtil.i(TAG, "DownloadPartCore [downloadPart] \u5206\u7247=" + downloadParams.getPartIndex() + ", \u53c2\u6570\u4e2d\u7684 segmentEnd=" + downloadParams.getLast() + ", \u53c2\u6570\u4e2d\u7684segmentStart=" + downloadParams.getStart() + ", \u5b9e\u9645\u7684segmentStart=" + start);
        this.mHost = Util.getDomainFromUrl(downloadParams.getDownloadUrl());
        if (chooseIp()) {
            long jCurrentTimeMillis = System.currentTimeMillis();
            iDownloadPart = enterDonwload(builderUrl, this.mHeader);
            TaskHandleOp.getInstance().addChannleSpeedMap(this.mHost, downloadParams.getLast() - start, System.currentTimeMillis() - jCurrentTimeMillis);
            CdnUseTimeProxy.getInstance().finish(this.mHost);
            LogUtil.e(TAG, "DownloadPartCore [downloadPart] \u5206\u7247=" + downloadParams.getPartIndex() + ", \u8fd4\u56de\u7ed3\u679c=" + iDownloadPart + ", ip=" + this.mIp + ", host=" + this.mHost);
        } else {
            LogUtil.i(TAG, "DownloadPartCore [downloadPart] \u5206\u7247=" + downloadParams.getPartIndex() + "\uff0c\u6ca1\u6709\u9009\u4e2dip");
            iDownloadPart = 1;
        }
        if (iDownloadPart != 0 && !NetController.getInstances().isInterrupted() && NetworkStatus.getNetStatus() != 0) {
            LogUtil.i(TAG, "DownloadPartCore [downloadPart] \u5206\u7247=" + downloadParams.getPartIndex() + "\uff0c \u8fdb\u5165\u3010\u91cd\u65b0\u9009\u62e9\u4e0b\u4e00\u4e2a\u53ef\u7528ip\u73af\u8282\u3011");
            if (!this.mIsLowSpeedRemove && !TextUtils.isEmpty(this.mIp) && !TextUtils.isEmpty(this.mHost)) {
                LogUtil.i(TAG, "[ORBIT] Removed Ip=" + this.mIp + " Domain=" + this.mHost + " Speed=1 Timeout=1");
            }
            boolean zCorrectIp = correctIp();
            LogUtil.i(TAG, "DownloadPartCore [downloadPart] \u5206\u7247=" + downloadParams.getPartIndex() + "\uff0chasReloadCondition=" + zCorrectIp);
            if (zCorrectIp) {
                LogUtil.i(TAG, "DownloadPartCore [downloadPart] \u5206\u7247=" + downloadParams.getPartIndex() + "\u8fdb\u5165\u3010\u91cd\u65b0\u9009\u62e9\u4e0b\u4e00\u4e2a\u53ef\u7528ip\u73af\u8282\u3011 \uff0c \u8fdb\u5165\u91cd\u8bd5");
                this.mRestart = true;
                iDownloadPart = downloadPart(downloadParams);
            } else if (enterHttpdns()) {
                LogUtil.i(TAG, "DownloadPartCore [downloadPart] \u5206\u7247=" + downloadParams.getPartIndex() + "\u8fdb\u5165\u3010httpdns\u5904\u7406\u3011\u73af\u8282 \uff0c \u8fdb\u5165\u91cd\u8bd5");
                this.mRestart = true;
                iDownloadPart = downloadPart(downloadParams);
            } else if (!this.mIsUseHistoryTopSpeedIp) {
                LogUtil.i(TAG, "DownloadPartCore [downloadPart] \u5206\u7247=" + downloadParams.getPartIndex() + "\uff0c \u8fdb\u5165\u3010\u4f7f\u7528\u5386\u53f2\u8bb0\u5f55\u6700\u9ad8\u901fip\u3011\u73af\u8282");
                this.mRestart = true;
                this.mIsUseHistoryTopSpeedIp = true;
                iDownloadPart = downloadPart(downloadParams);
            } else {
                BackUpIpProxy.getInstances().setBackUpIpStatus(-1);
            }
            if (this.mRestart) {
                return iDownloadPart;
            }
        }
        LogUtil.i(TAG, "DownloadPartCore [downloadPart] \u5206\u7247=" + downloadParams.getPartIndex() + ", \u5206\u7247\u4e0b\u8f7d\uff0c\u6700\u540e\u7ed3\u679c =" + iDownloadPart);
        return iDownloadPart;
    }

    private boolean chooseIp() {
        LogUtil.i(TAG, "DownloadPartCore [chooseIp] [\u8be5\u57df\u540d\u4e0b\u9009\u62e9ip] \u5206\u7247=" + this.mDownloadParams.getPartIndex() + ", start");
        this.mIp = null;
        if (CdnIpController.getInstances().hasNextIp(this.mHost)) {
            this.mIp = CdnIpController.getInstances().nextIp(this.mHost);
            LogUtil.i(TAG, "DownloadPartCore [chooseIp] [\u8be5\u57df\u540d\u4e0b\u9009\u62e9ip] \u5206\u7247=" + this.mDownloadParams.getPartIndex() + ", \u8be5host\u4e0b\u8fd8\u6709\u672a\u4f7f\u7528\u7684ip\uff0c \u7ee7\u7eed\u4f7f\u7528host\uff1a" + this.mHost + ", \u65b0ip=" + this.mIp + ", \u6700\u7ec8\u751f\u6210\u7684\u8bf7\u6c42\u94fe\u63a5=" + this.mDownloadParams.getDownloadUrl(this.mIp));
            CdnUseTimeProxy.getInstance().start(this.mHost);
        } else if (CdnIpController.getInstances().hasNextUnit(this.mDownloadParams.getmChannel())) {
            LogUtil.i(TAG, "DownloadPartCore [chooseIp] [\u539fhost\u4e0b\u65e0\u53ef\u7528ip\uff0c\u5207\u6362cdn] \u5206\u7247=" + this.mDownloadParams.getPartIndex() + ", \u5207\u6362host\uff0c\u539fhost=" + this.mHost);
            this.mHost = CdnIpController.getInstances().nextUnit(this.mDownloadParams.getmChannel()).mDomain;
            this.mIp = CdnIpController.getInstances().nextIp(this.mHost);
            LogUtil.i(TAG, "DownloadPartCore [chooseIp] [\u539fhost\u4e0b\u65e0\u53ef\u7528ip\uff0c\u5207\u6362cdn] \u5206\u7247=" + this.mDownloadParams.getPartIndex() + ", \u65b0host=" + this.mHost + ", \u8be5host\u4e0b\u9009\u62e9\u7684ip=" + this.mIp + ", \u6700\u7ec8\u751f\u6210\u7684\u8bf7\u6c42\u94fe\u63a5=" + this.mDownloadParams.getDownloadUrl(this.mIp));
            CdnUseTimeProxy.getInstance().start(this.mHost);
        } else if (HttpdnsProxy.getInstances().hasNext(Const.HTTPDNS_CONFIG_CND)) {
            LogUtil.i(TAG, "DownloadPartCore [chooseIp] [cdn\u89e3\u6790\u7684ip\uff0c\u5df2\u65e0ip\u53ef\u7528\uff0c \u8fdb\u5165httpdns\u903b\u8f91] \u5206\u7247=" + this.mDownloadParams.getPartIndex() + ", \u9891\u9053=" + this.mDownloadParams.getmChannel());
            HttpdnsUrlSwitcherCore.HttpdnsUrlSwitcherCoreUnit next = HttpdnsProxy.getInstances().next(Const.HTTPDNS_CONFIG_CND, this.mDownloadParams.getmChannel());
            if (next != null) {
                this.mHost = next.host;
                this.mIp = next.ip;
                String port = CdnIpController.getInstances().getPort(this.mHost);
                if (TextUtils.isEmpty(port)) {
                    this.mIp = this.mIp;
                } else {
                    this.mIp += ":" + port;
                }
                LogUtil.i(TAG, "DownloadPartCore [chooseIp] [cdn\u89e3\u6790\u7684ip\uff0c\u5df2\u65e0ip\u53ef\u7528\uff0c \u8fdb\u5165httpdns\u903b\u8f91] \u5206\u7247=" + this.mDownloadParams.getPartIndex() + "\uff0c \u6240\u9009\u62e9\u7684host=" + this.mHost + ", \u8be5host\u4e0b\u9009\u62e9\u7684ip=" + this.mIp + ", \u6700\u7ec8\u751f\u6210\u7684\u8bf7\u6c42\u94fe\u63a5=" + this.mDownloadParams.getDownloadUrl(this.mIp));
                CdnUseTimeProxy.getInstance().start(this.mHost);
            }
        }
        boolean z = true;
        boolean z2 = (TextUtils.isEmpty(this.mIp) || TextUtils.isEmpty(this.mHost)) ? false : true;
        if (z2 || !this.mIsUseHistoryTopSpeedIp) {
            z = z2;
        } else {
            LogUtil.i(TAG, "DownloadPartCore [chooseIp] [\u4f7f\u7528\u5386\u53f2\u8bb0\u5f55\u6700\u9ad8\u901fip] \u5206\u7247=" + this.mDownloadParams.getPartIndex() + ", \u8be5\u57df\u540d\u4e0b\uff0c\u6240\u6709\u53ef\u5c1d\u8bd5\u7684ip\u5747\u5df2\u88ab\u79fb\u9664\uff0c\u76f4\u63a5\u4f7f\u7528\u5386\u53f2\u8bb0\u5f55\u4e2d\u6700\u9ad8\u901f\u7684ip\u8fdb\u884c\u4e0b\u8f7d");
            this.mIp = BackUpIpProxy.getInstances().getHistoryTopSpeedIp();
            this.mHost = BackUpIpProxy.getInstances().getHistoryTopSpeedHost();
            this.mIsUseHistoryTopSpeedIp = true;
            LogUtil.i(TAG, "DownloadPartCore [chooseIp] [\u4f7f\u7528\u5386\u53f2\u8bb0\u5f55\u6700\u9ad8\u901fip] \u5206\u7247=" + this.mDownloadParams.getPartIndex() + "\uff0c \u6240\u9009\u62e9\u7684host=" + this.mHost + ", \u8be5host\u4e0b\u9009\u62e9\u7684ip=" + this.mIp + ", \u6700\u7ec8\u751f\u6210\u7684\u8bf7\u6c42\u94fe\u63a5=" + this.mDownloadParams.getDownloadUrl(this.mIp));
            CdnUseTimeProxy.getInstance().start(this.mHost);
        }
        LogUtil.i(TAG, "DownloadPartCore [chooseIp] [\u8be5\u57df\u540d\u4e0b\u9009\u62e9ip] \u5206\u7247=" + this.mDownloadParams.getPartIndex() + ", result=" + z);
        return z;
    }

    private int enterDonwload(Request.Builder builder, Map<String, String> map) {
        LogUtil.i(TAG, "DownloadPartCore [enterDonwload] \u5206\u7247=" + this.mDownloadParams.getPartIndex() + ", ip=" + this.mIp + ", \u5f00\u59cb\u65f6\u95f4=" + System.currentTimeMillis());
        if (!BackUpIpProxy.getInstances().hasInitBackUpIp()) {
            BackUpIpProxy.getInstances().setBackUpInfo(this.mIp, this.mHost, 0L);
            BackUpIpProxy.getInstances().setBackUpIpStatus(1);
        }
        LogUtil.i(TAG, "DownloadPartCore [enterDonwload] \u5206\u7247=" + this.mDownloadParams.getPartIndex() + ", use okhttp");
        builder.url(this.mDownloadParams.getDownloadUrl(this.mIp));
        builder.addHeader("Host", this.mHost);
        builder.get();
        int iExecute_syn = OkHttpProxy.getInstance().execute_syn(builder, this.okhttpCallback);
        LogUtil.i(TAG, "DownloadPartCore [enterDonwload] \u5206\u7247=" + this.mDownloadParams.getPartIndex() + ", ip=" + this.mIp + ", \u7ed3\u675f\u65f6\u95f4=" + System.currentTimeMillis());
        return iExecute_syn;
    }

    private boolean correctIp() {
        boolean z;
        LogUtil.i(TAG, "DownloadPartCore [correctIp] \u5206\u7247=" + this.mDownloadParams.getPartIndex() + ", \u8fdb\u5165\u5207\u6362\u6d41\u7a0b\uff0chost=" + this.mHost + ", ip=" + this.mIp);
        LogUtil.i(TAG, "DownloadPartCore [correctIp] \u5206\u7247=" + this.mDownloadParams.getPartIndex() + ", \u5207\u6362\u5206\u7247\u4e4b\u524d\uff0chost\u4e3a=" + this.mHost + ", ip=" + this.mIp);
        LogUtil.i(TAG, "DownloadPartCore [correctIp] \u5206\u7247=" + this.mDownloadParams.getPartIndex() + ", CdnIpController.getInstances().hasNextIp(host)=" + CdnIpController.getInstances().hasNextIp(this.mHost) + ", CdnIpController.getInstances().hasNextUnit()=" + CdnIpController.getInstances().hasNextUnit(this.mDownloadParams.getmChannel()));
        StringBuilder sb = new StringBuilder("DownloadPartCore [correctIp] \u5206\u7247=");
        sb.append(this.mDownloadParams.getPartIndex());
        sb.append(", \u5220\u9664\u4e4b\u524d\uff0c CdnIpController \u603b\u89c8=");
        sb.append(CdnIpController.getInstances().mActualTimeMap.toString());
        LogUtil.i(TAG, sb.toString());
        if (CdnIpController.getInstances().hasNextIp(this.mHost)) {
            LogUtil.i(TAG, "DownloadPartCore [correctIp] \u5206\u7247=" + this.mDownloadParams.getPartIndex() + ", \u79fb\u9664ip=" + this.mIp + ", host=" + this.mHost + ",part=" + this.mDownloadParams.getPartIndex());
            CdnIpController.getInstances().removeIp(this.mHost, this.mIp);
            z = true;
        } else {
            z = false;
        }
        if (z || !CdnIpController.getInstances().hasNextUnit(this.mDownloadParams.getmChannel())) {
            return z;
        }
        LogUtil.i(TAG, "DownloadPartCore [correctIp] \u5206\u7247=" + this.mDownloadParams.getPartIndex() + ", \u8be5host=" + this.mHost + "\u6ca1\u6709\u5176\u4ed6ip\u53ef\u5207\u6362\uff0c\u5220\u9664\u8be5host ,part=" + this.mDownloadParams.getPartIndex());
        CdnIpController.getInstances().removeUnit(this.mHost);
        return true;
    }

    private boolean enterHttpdns() {
        boolean z;
        LogUtil.i(TAG, "DownloadPartCore [enterHttpdns] start\uff0c \u5206\u7247=" + this.mDownloadParams.getPartIndex());
        LogUtil.i(TAG, "DownloadPartCore [enterHttpdns] \u5206\u7247=" + this.mDownloadParams.getPartIndex() + ", \u5207\u6362httpdns");
        ConfigParams configParams = ConfigProxy.getInstances().getmConfigParams();
        if (!HttpdnsProxy.getInstances().containKey(Const.HTTPDNS_CONFIG_CND)) {
            LogUtil.i(TAG, "DownloadPartCore [enterHttpdns] \u5206\u7247=" + this.mDownloadParams.getPartIndex() + ", \u5206\u7247\u4e2d\uff0c\u5f00\u59cbhttpdns");
            HttpdnsProxy.getInstances().synStart(Const.HTTPDNS_CONFIG_CND, configParams.getmCdnMap());
        } else {
            LogUtil.i(TAG, "DownloadPartCore [enterHttpdns] \u5220\u9664httpdns ip=" + this.mIp + ", \u6240\u5c5ehost=" + this.mDownloadParams.getmChannel() + ", \u5206\u7247=" + this.mDownloadParams.getPartIndex() + ", \u5206\u7247\u4e2d\uff0c\u5f00\u59cbhttpdns");
            HttpdnsProxy.getInstances().remove(Const.HTTPDNS_CONFIG_CND, this.mIp, this.mDownloadParams.getmChannel());
        }
        if (HttpdnsProxy.getInstances().next(Const.HTTPDNS_CONFIG_CND, this.mDownloadParams.getmChannel()) != null) {
            LogUtil.i(TAG, "DownloadPartCore [enterHttpdns] httpdns ip\u4e2d\uff0c\u5b58\u5728\u672a\u4f7f\u7528ip, \u5206\u7247=" + this.mDownloadParams.getPartIndex() + ", \u5206\u7247\u4e2d\uff0c\u5f00\u59cbhttpdns");
            z = true;
        } else {
            LogUtil.i(TAG, "DownloadPartCore [enterHttpdns] httpdns ip\u4e2d\uff0c\u5df2\u65e0\u53ef\u7528ip");
            z = false;
        }
        LogUtil.stepLog("DownloadPartCore [enterHttpdns] end\uff0c httpdnSuccess=" + z);
        return z;
    }

    private long getContentLength(Map<String, List<String>> map) {
        if (map == null) {
            return 0L;
        }
        List<String> list = map.containsKey("Content-Length") ? map.get("Content-Length") : null;
        if (list != null && !list.isEmpty()) {
            String str = list.get(0);
            LogUtil.d(TAG, "DownloadPartCore [getContentLength] \u5206\u7247=" + this.mDownloadParams.getPartIndex() + ", processHeader, value=" + str);
            if (TextUtils.isDigitsOnly(str)) {
                return Long.valueOf(str).longValue();
            }
        }
        LogUtil.w(TAG, "DownloadPartCore [getContentLength] \u5206\u7247=" + this.mDownloadParams.getPartIndex() + ", no Content-Length found");
        return 0L;
    }
}