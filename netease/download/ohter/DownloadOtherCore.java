package com.netease.download.ohter;

import android.text.TextUtils;
import com.facebook.cache.disk.DefaultDiskStorage;
import com.netease.download.dns.CdnIpController;
import com.netease.download.dns.DnsCore;
import com.netease.download.dns.DnsParams;
import com.netease.download.downloader.DownloadParams;
import com.netease.download.downloader.TaskHandleOp;
import com.netease.download.listener.DownloadListenerProxy;
import com.netease.download.network.OkHttpProxy;
import com.netease.download.util.LogUtil;
import com.netease.download.util.Util;
import com.netease.ntunisdk.okhttp3.Call;
import com.netease.ntunisdk.okhttp3.Callback;
import com.netease.ntunisdk.okhttp3.Request;
import com.netease.ntunisdk.okhttp3.Response;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;

/* loaded from: classes5.dex */
public class DownloadOtherCore implements Callable<Integer> {
    private static final String TAG = "DownloadAllCore";
    private DownloadParams mDownloadParams = null;
    private String mHost = null;
    private Callback okhttpCallback = new Callback() { // from class: com.netease.download.ohter.DownloadOtherCore.1
        @Override // com.netease.ntunisdk.okhttp3.Callback
        public void onResponse(Call call, Response response) throws IOException {
            LogUtil.stepLog("DownloadOtherCore [okhttpCallback] [onResponse] start");
            if (call == null || response == null) {
                return;
            }
            LogUtil.i(DownloadOtherCore.TAG, "DownloadOtherCore [okhttpCallback] [onResponse] Call Header=" + call.request().headers().toMultimap().toString() + ", request = " + call.request().toString());
            LogUtil.i(DownloadOtherCore.TAG, "DownloadOtherCore [okhttpCallback] [onResponse] Response Header=" + response.headers().toMultimap().toString() + ", hashCode=" + response.code() + ", resUrl=" + response.request().url() + ", protocol=" + response.protocol() + ", " + response.request().toString());
            String filePath = DownloadOtherCore.this.mDownloadParams.getFilePath();
            StringBuilder sb = new StringBuilder("DownloadOtherCore [processContent] \u6587\u4ef6\u5b58\u50a8\u8def\u5f84=");
            sb.append(filePath);
            LogUtil.i(DownloadOtherCore.TAG, sb.toString());
            boolean zIsmWriteToExistFile = DownloadOtherCore.this.mDownloadParams.ismWriteToExistFile();
            StringBuilder sb2 = new StringBuilder("DownloadOtherCore [processContent] writeToExistFile=");
            sb2.append(zIsmWriteToExistFile);
            LogUtil.i(DownloadOtherCore.TAG, sb2.toString());
            if (zIsmWriteToExistFile) {
                LogUtil.i(DownloadOtherCore.TAG, "DownloadOtherCore [processContent] write to exist file");
                File file = new File(filePath);
                if (!file.exists()) {
                    return;
                }
                long length = file.length();
                long start = DownloadOtherCore.this.mDownloadParams.getStart();
                long last = DownloadOtherCore.this.mDownloadParams.getLast();
                long size = DownloadOtherCore.this.mDownloadParams.getSize();
                long j = DownloadOtherCore.this.mDownloadParams.getmWoffset();
                long j2 = j < 0 ? 0L : j;
                LogUtil.i(DownloadOtherCore.TAG, "DownloadOtherCore [processContent] fileSize=" + length + ", segmentStart=" + start + ", segmentEnd=" + last + ", segmentSize=" + size);
                if (start >= 0 && start <= last) {
                    RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "rwd");
                    randomAccessFile.seek(j2);
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(response.body().byteStream());
                    byte[] bArr = new byte[8192];
                    while (true) {
                        int i = bufferedInputStream.read(bArr);
                        if (i != -1) {
                            randomAccessFile.write(bArr, 0, i);
                        } else {
                            randomAccessFile.close();
                            return;
                        }
                    }
                } else {
                    LogUtil.i(DownloadOtherCore.TAG, "DownloadOtherCore [processContent] size error");
                }
            } else {
                LogUtil.i(DownloadOtherCore.TAG, "DownloadOtherCore [processContent] write to new file");
                File file2 = new File(filePath + DefaultDiskStorage.FileType.TEMP);
                if (!file2.getParentFile().exists()) {
                    file2.getParentFile().mkdirs();
                }
                if (!file2.exists()) {
                    try {
                        file2.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                FileOutputStream fileOutputStream = new FileOutputStream(filePath + DefaultDiskStorage.FileType.TEMP);
                byte[] bArr2 = new byte[1024];
                BufferedInputStream bufferedInputStream2 = new BufferedInputStream(response.body().byteStream());
                while (true) {
                    int i2 = bufferedInputStream2.read(bArr2);
                    if (i2 != -1) {
                        DownloadListenerProxy.getInstances();
                        DownloadListenerProxy.getDownloadListenerHandler().sendProgressMsg(TaskHandleOp.getInstance().getTaskHandle().getTaskMergeAllSizes(), i2, filePath, filePath, TaskHandleOp.getInstance().getTaskHandle().getDownloadId(), TaskHandleOp.getInstance().getTaskHandle().getNtesOrbitId());
                        fileOutputStream.write(bArr2, 0, i2);
                    } else {
                        fileOutputStream.flush();
                        bufferedInputStream2.close();
                        fileOutputStream.close();
                        return;
                    }
                }
            }
        }

        @Override // com.netease.ntunisdk.okhttp3.Callback
        public void onFailure(Call call, IOException iOException) {
            LogUtil.stepLog("DownloadOtherCore [okhttpCallback] [onFailure] start");
        }
    };

    public void init(DownloadParams downloadParams) {
        this.mDownloadParams = downloadParams;
    }

    public int start() {
        LogUtil.i(TAG, "mDownloadParams=" + this.mDownloadParams.toString());
        String domainFromUrl = Util.getDomainFromUrl(this.mDownloadParams.getTargetUrl());
        if (!CdnIpController.getInstances().containDomain(domainFromUrl)) {
            LogUtil.i(TAG, "DownloadOtherCore [start] dns\u73af\u8282 host=" + domainFromUrl);
            DnsCore.getInstances().init(this.mDownloadParams.getTargetUrl());
            ArrayList<DnsParams.Unit> arrayListStart = DnsCore.getInstances().start();
            if (arrayListStart != null && arrayListStart.size() > 0) {
                LogUtil.i(TAG, "DownloadOtherCore [start] \u5217\u8868\u6587\u4ef6\u505aDNS\u89e3\u6790\uff0cDNS\u7ed3\u679c=" + arrayListStart.toString());
                DownloadListenerProxy.getInstances();
                DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsg(0, 0L, 0L, "__DOWNLOAD_DNS_RESOLVED__", "__DOWNLOAD_DNS_RESOLVED__", "".getBytes(), TaskHandleOp.getInstance().getTaskHandle().getSessionid(), TaskHandleOp.getInstance().getTaskHandle().getNtesOrbitId());
            } else {
                DownloadListenerProxy.getInstances();
                DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsg(11, 0L, 0L, "__DOWNLOAD_DNS_RESOLVED__", "__DOWNLOAD_DNS_RESOLVED__", "".getBytes(), TaskHandleOp.getInstance().getTaskHandle().getSessionid(), TaskHandleOp.getInstance().getTaskHandle().getNtesOrbitId());
            }
            CdnIpController.getInstances().add(arrayListStart);
        }
        int i = 11;
        while (true) {
            if (!CdnIpController.getInstances().hasNextIp(domainFromUrl)) {
                break;
            }
            String strNextIp = CdnIpController.getInstances().nextIp(domainFromUrl);
            LogUtil.i(TAG, "DownloadOtherCore [start] download other ip=" + strNextIp);
            int iDownloadFile = downloadFile(this.mDownloadParams.getTargetUrl(), strNextIp);
            LogUtil.i(TAG, "DownloadOtherCore [start] download other file result1=" + iDownloadFile);
            if (iDownloadFile == 0) {
                i = iDownloadFile;
                break;
            }
            CdnIpController.getInstances().removeIp(domainFromUrl, strNextIp);
            i = iDownloadFile;
        }
        boolean zIsmWriteToExistFile = this.mDownloadParams.ismWriteToExistFile();
        LogUtil.i(TAG, "DownloadOtherCore [start] writeToExistFile=" + zIsmWriteToExistFile);
        if (!zIsmWriteToExistFile) {
            String filePath = this.mDownloadParams.getFilePath();
            File file = new File(filePath + DefaultDiskStorage.FileType.TEMP);
            LogUtil.i(TAG, "DownloadOtherCore [start] \u4e34\u65f6\u6587\u4ef6\u662f\u5426\u5b58\u5728=" + file.exists() + ", mFilePath=" + file.getAbsolutePath());
            if (i == 0) {
                File file2 = new File(filePath);
                if (file.exists()) {
                    LogUtil.i(TAG, "DownloadOtherCore [start] \u4e0b\u8f7d\u6210\u529f\uff0c\u547d\u540d\u4e3a\u6b63\u5f0f\u6587\u4ef6");
                    file.renameTo(file2);
                }
            } else if (file.exists()) {
                LogUtil.i(TAG, "DownloadOtherCore [start] \u4e0b\u8f7d\u5931\u8d25\uff0c\u5220\u9664\u4e34\u65f6\u6587\u4ef6");
                file.delete();
            }
        } else {
            LogUtil.i(TAG, "DownloadOtherCore [start] \u4e0b\u8f7d\u5230\u5df2\u6709\u6587\u4ef6\uff0c\u4e0b\u8f7d\u6210\u529f");
        }
        return i;
    }

    private int downloadFile(String str, String str2) {
        HashMap map;
        String strReplaceDomainWithIpAddr;
        LogUtil.stepLog("DownloadOtherCore [downloadFile]  \u4e0b\u8f7d\u5176\u4ed6\u6587\u4ef6");
        String domainFromUrl = Util.getDomainFromUrl(str);
        this.mHost = domainFromUrl;
        if (TextUtils.isEmpty(str2)) {
            map = null;
            strReplaceDomainWithIpAddr = str;
        } else {
            LogUtil.i(TAG, "DownloadOtherCore [downloadFile] ipAddr=" + str2);
            strReplaceDomainWithIpAddr = Util.replaceDomainWithIpAddr(str, str2, "/");
            map = new HashMap();
            map.put("Host", domainFromUrl);
        }
        LogUtil.i(TAG, "DownloadOtherCore [downloadFile] Url=" + strReplaceDomainWithIpAddr + ", domain=" + domainFromUrl);
        Request.Builder builderUrl = new Request.Builder().url(strReplaceDomainWithIpAddr);
        builderUrl.addHeader("Host", domainFromUrl);
        long start = this.mDownloadParams.getStart();
        long last = this.mDownloadParams.getLast() - 1;
        if (0 != this.mDownloadParams.getLast()) {
            map.put("START", start + "");
            map.put("END", last + "");
            if (builderUrl != null) {
                builderUrl.addHeader("START", start + "");
                builderUrl.addHeader("END", last + "");
            }
        }
        Integer numValueOf = 11;
        try {
            if (!TextUtils.isEmpty(str2)) {
                LogUtil.i(TAG, "DownloadOtherCore [downloadFile] use okhttp");
                numValueOf = Integer.valueOf(OkHttpProxy.getInstance().execute_syn(builderUrl, this.okhttpCallback));
                LogUtil.i(TAG, "DownloadOtherCore [downloadFile] result=" + numValueOf + "\uff0cUrl=" + strReplaceDomainWithIpAddr);
            }
        } catch (Exception e) {
            numValueOf = 11;
            e.printStackTrace();
        }
        return numValueOf.intValue();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.concurrent.Callable
    public Integer call() throws Exception {
        LogUtil.i(TAG, "DownloadOtherCore [call] start");
        int iStart = start();
        LogUtil.i(TAG, "DownloadOtherCore [call] result=" + iStart);
        DownloadListenerProxy.getInstances();
        DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsg(iStart, TaskHandleOp.getInstance().getTaskHandle().getTaskAllSizes(), DownloadListenerProxy.getInstances().getHasDownloadSize(), this.mDownloadParams.getCallBackFileName(), this.mDownloadParams.getFilePath(), "".getBytes(), TaskHandleOp.getInstance().getTaskHandle().getSessionid(), TaskHandleOp.getInstance().getTaskHandle().getNtesOrbitId());
        return Integer.valueOf(iStart);
    }
}