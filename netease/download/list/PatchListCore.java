package com.netease.download.list;

import android.content.Context;
import android.text.TextUtils;
import com.facebook.cache.disk.DefaultDiskStorage;
import com.netease.download.Const;
import com.netease.download.config.ConfigProxy;
import com.netease.download.dns.DnsCore;
import com.netease.download.dns.DnsParams;
import com.netease.download.downloader.DownloadParams;
import com.netease.download.downloader.FileHandle;
import com.netease.download.downloader.TaskHandleOp;
import com.netease.download.handler.Dispatcher;
import com.netease.download.listener.DownloadListenerProxy;
import com.netease.download.lvsip.Lvsip;
import com.netease.download.network.OkHttpProxy;
import com.netease.download.util.LogUtil;
import com.netease.download.util.Util;
import com.netease.ntsharesdk.Platform;
import com.netease.ntunisdk.okhttp3.Call;
import com.netease.ntunisdk.okhttp3.Callback;
import com.netease.ntunisdk.okhttp3.Request;
import com.netease.ntunisdk.okhttp3.Response;
import com.xiaomi.gamecenter.sdk.robust.Constants;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Callable;

/* loaded from: classes5.dex */
public class PatchListCore implements Callable<Integer> {
    private static final String TAG = "PatchListCore";
    private Context mContext = null;
    private String mUrlPath = null;
    private String mFileName = null;
    private String mMd5 = null;
    private String mFilePath = null;
    private String mHost = null;
    private String mFileId = null;
    private HashMap<String, String> mLogData = new HashMap<>();
    private Callback okhttpCallback = new Callback() { // from class: com.netease.download.list.PatchListCore.1
        @Override // com.netease.ntunisdk.okhttp3.Callback
        public void onResponse(Call call, Response response) throws IOException {
            long last;
            ByteBuffer byteBuffer;
            LogUtil.stepLog("PatchListCore [okhttpCallback] [onResponse] start");
            if (call == null || response == null) {
                return;
            }
            LogUtil.i(PatchListCore.TAG, "PatchListCore [okhttpCallback] [onResponse] Call Header=" + call.request().headers().toMultimap().toString() + ", request = " + call.request().toString());
            LogUtil.i(PatchListCore.TAG, "PatchListCore [okhttpCallback] [onResponse] Response Header=" + response.headers().toMultimap().toString() + ", hashCode=" + response.code() + ", resUrl=" + response.request().url() + ", protocol=" + response.protocol() + ", " + response.request().toString());
            FileOutputStream fileOutputStream = null;
            if (!TaskHandleOp.getInstance().getTaskHandle().isRammode()) {
                File file = new File(PatchListCore.this.mFilePath + DefaultDiskStorage.FileType.TEMP);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                fileOutputStream = new FileOutputStream(PatchListCore.this.mFilePath + DefaultDiskStorage.FileType.TEMP);
                byteBuffer = null;
            } else {
                FileHandle fileHandle = Dispatcher.getTaskParamsMap().get(PatchListCore.this.mFileId);
                DownloadParams downloadParams = fileHandle.getDownloadParams();
                if (0 == downloadParams.getLast()) {
                    last = downloadParams.getSize();
                } else {
                    last = downloadParams.getLast() - downloadParams.getStart();
                }
                LogUtil.i(PatchListCore.TAG, "PatchListCore [okhttpCallback] [onResponse] size111=" + last);
                fileHandle.getDownloadParams().createByteBuffer(last);
                byteBuffer = fileHandle.getDownloadParams().getByteBuffer();
            }
            byte[] bArr = new byte[1024];
            InputStream inputStreamByteStream = response.body().byteStream();
            LogUtil.stepLog("PatchListCore [okhttpCallback] [onResponse] contentLength=" + response.body().contentLength());
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStreamByteStream);
            int i = 0;
            if (!TaskHandleOp.getInstance().getTaskHandle().isRammode()) {
                while (true) {
                    int i2 = bufferedInputStream.read(bArr);
                    if (i2 == -1) {
                        break;
                    }
                    DownloadListenerProxy.getInstances();
                    DownloadListenerProxy.getDownloadListenerHandler().sendProgressMsg(TaskHandleOp.getInstance().getTaskHandle().getTaskMergeAllSizes(), i2, PatchListCore.this.mFileName, PatchListCore.this.mFilePath, TaskHandleOp.getInstance().getTaskHandle().getDownloadId(), TaskHandleOp.getInstance().getTaskHandle().getNtesOrbitId());
                    fileOutputStream.write(bArr, 0, i2);
                }
                fileOutputStream.flush();
                bufferedInputStream.close();
                fileOutputStream.close();
            } else {
                int i3 = 0;
                while (true) {
                    int i4 = bufferedInputStream.read(bArr);
                    if (i4 == -1) {
                        break;
                    }
                    DownloadListenerProxy.getInstances();
                    DownloadListenerProxy.getDownloadListenerHandler().sendProgressMsg(TaskHandleOp.getInstance().getTaskHandle().getTaskMergeAllSizes(), i4, PatchListCore.this.mFileName, PatchListCore.this.mFilePath, TaskHandleOp.getInstance().getTaskHandle().getDownloadId(), TaskHandleOp.getInstance().getTaskHandle().getNtesOrbitId());
                    byteBuffer.put(bArr, 0, i4);
                    i3 += i4;
                }
                bufferedInputStream.close();
                i = i3;
            }
            LogUtil.i(PatchListCore.TAG, "\u6587\u4ef6\u5b58\u50a8\u8def\u5f84  count=" + i);
        }

        @Override // com.netease.ntunisdk.okhttp3.Callback
        public void onFailure(Call call, IOException iOException) {
            LogUtil.stepLog("PatchListCore [okhttpCallback] [onFailure] start");
        }
    };

    public void init(Context context, String str, String str2, String str3, String str4, String str5) {
        this.mContext = context;
        this.mUrlPath = str;
        this.mMd5 = str2;
        this.mFilePath = str3;
        this.mFileName = str4;
        this.mFileId = str5;
    }

    public int start() {
        String str;
        this.mLogData.put("state", "start");
        this.mLogData.put("filetype", Platform.OTHER);
        DnsCore.getInstances().init(this.mUrlPath);
        ArrayList<DnsParams.Unit> arrayListStart = DnsCore.getInstances().start();
        LogUtil.i(TAG, "\u5217\u8868\u6587\u4ef6\u505aDNS\u89e3\u6790\uff0cDNS\u7ed3\u679c=" + arrayListStart.toString());
        if (arrayListStart != null && arrayListStart.size() > 0) {
            DownloadListenerProxy.getInstances();
            DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsg(0, 0L, 0L, "__DOWNLOAD_DNS_RESOLVED__", "__DOWNLOAD_DNS_RESOLVED__", "".getBytes(), TaskHandleOp.getInstance().getTaskHandle().getSessionid(), TaskHandleOp.getInstance().getTaskHandle().getNtesOrbitId());
        } else {
            DownloadListenerProxy.getInstances();
            DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsg(11, 0L, 0L, "__DOWNLOAD_DNS_RESOLVED__", "__DOWNLOAD_DNS_RESOLVED__", "".getBytes(), TaskHandleOp.getInstance().getTaskHandle().getSessionid(), TaskHandleOp.getInstance().getTaskHandle().getNtesOrbitId());
        }
        int iDownloadConfig = 11;
        if (arrayListStart == null || arrayListStart.size() <= 0) {
            str = null;
        } else {
            Iterator<DnsParams.Unit> it = arrayListStart.iterator();
            str = null;
            while (it.hasNext()) {
                DnsParams.Unit next = it.next();
                ArrayList<String> arrayList = next.ipArrayList;
                str = next.port;
                Iterator<String> it2 = arrayList.iterator();
                while (it2.hasNext()) {
                    String next2 = it2.next();
                    if (!TextUtils.isEmpty(str)) {
                        next2 = next2 + ":" + str;
                        LogUtil.i(TAG, "PatchListCore has port, final ip =" + next2);
                    }
                    iDownloadConfig = downloadConfig(this.mContext, this.mUrlPath, next2);
                    LogUtil.i(TAG, "downloadConfig result1=" + iDownloadConfig);
                    if (iDownloadConfig == 0) {
                        break;
                    }
                }
                LogUtil.i(TAG, "downloadConfig result2=" + iDownloadConfig);
                if (iDownloadConfig == 0) {
                    break;
                }
            }
        }
        if (iDownloadConfig != 0) {
            LogUtil.stepLog("\u91c7\u7528lvsip");
            if (!Lvsip.getInstance().isCteateIp()) {
                String[] lvsipArray = ConfigProxy.getInstances().getmConfigParams() != null ? ConfigProxy.getInstances().getmConfigParams().getLvsipArray() : null;
                if (lvsipArray == null || lvsipArray.length <= 0) {
                    String overSea = TaskHandleOp.getInstance().getTaskHandle().getOverSea();
                    if ("1".equals(overSea) || "2".equals(overSea)) {
                        lvsipArray = Const.REQ_IPS_WS_OVERSEA;
                    } else if ("0".equals(overSea) || "-1".equals(overSea)) {
                        lvsipArray = Const.REQ_IPS_WS_CHINA;
                    } else {
                        lvsipArray = Const.REQ_IPS_WS;
                    }
                }
                Lvsip.getInstance().init(lvsipArray);
                Lvsip.getInstance().createLvsip();
            }
            while (Lvsip.getInstance().hasNext() && iDownloadConfig != 0) {
                String newIpFromArray = Lvsip.getInstance().getNewIpFromArray();
                LogUtil.i(TAG, "\u5217\u8868\u8bf7\u6c42\u73af\u8282--\u91c7\u7528lvsip\uff0c\u5c06\u8981\u4f7f\u7528\u7684ip=" + newIpFromArray);
                if (!TextUtils.isEmpty(str)) {
                    newIpFromArray = newIpFromArray + ":" + str;
                    LogUtil.i(TAG, "PatchListCore has port, final ip =" + newIpFromArray);
                }
                if (!TextUtils.isEmpty(newIpFromArray)) {
                    iDownloadConfig = downloadConfig(this.mContext, this.mUrlPath, newIpFromArray);
                }
            }
        }
        File file = new File(this.mFilePath + DefaultDiskStorage.FileType.TEMP);
        LogUtil.i(TAG, "\u5217\u8868\u8bf7\u6c42\u73af\u8282--\u4e34\u65f6\u6587\u4ef6\u662f\u5426\u5b58\u5728=" + file.exists() + ", mFilePath=" + file.getAbsolutePath());
        if (iDownloadConfig == 0) {
            this.mLogData.put("state", Const.LOG_TYPE_STATE_FINISH);
            this.mLogData.put("filetype", "CFG");
            if (!TaskHandleOp.getInstance().getTaskHandle().isRammode()) {
                File file2 = new File(this.mFilePath);
                if (file.exists()) {
                    LogUtil.i(TAG, "\u5217\u8868\u8bf7\u6c42\u73af\u8282--\u4e0b\u8f7d\u6210\u529f\uff0c\u547d\u540d\u4e3a\u6b63\u5f0f\u6587\u4ef6");
                    file.renameTo(file2);
                }
            }
        } else {
            this.mLogData.put("state", "error");
            this.mLogData.put("filetype", "CFG");
            if (!TaskHandleOp.getInstance().getTaskHandle().isRammode() && file.exists()) {
                LogUtil.i(TAG, "\u5217\u8868\u8bf7\u6c42\u73af\u8282--\u4e0b\u8f7d\u5931\u8d25\uff0c\u5220\u9664\u4e34\u65f6\u6587\u4ef6");
                file.delete();
            }
        }
        return iDownloadConfig;
    }

    private int downloadConfig(Context context, String str, String str2) {
        boolean z;
        Integer num;
        LogUtil.stepLog("\u4e0b\u8f7d\u914d\u7f6e\u5217\u8868\u6587\u4ef6");
        String domainFromUrl = Util.getDomainFromUrl(str);
        if (!TextUtils.isEmpty(str2)) {
            LogUtil.i(TAG, "ipAddr=" + str2);
            String strReplaceDomainWithIpAddr = Util.replaceDomainWithIpAddr(str, str2, "/");
            if (Util.isIpv6(str2)) {
                str = Util.replaceDomainWithIpAddr(strReplaceDomainWithIpAddr, Constants.C + str2 + "]", "/");
            } else {
                str = Util.replaceDomainWithIpAddr(strReplaceDomainWithIpAddr, str2, "/");
            }
            new HashMap();
            this.mHost = domainFromUrl;
        }
        LogUtil.i(TAG, "configUrl=" + str + ", domain=" + domainFromUrl);
        if (Const.Not_MD5_BUT_LVSIP.equals(this.mMd5)) {
            LogUtil.i(TAG, "\u6ca1\u6709\u8bbe\u7f6eMD5\uff0c\u76f4\u63a5\u8d70lvsip");
            this.mMd5 = null;
            z = true;
        } else {
            z = false;
        }
        int iValueOf = 11;
        if (z) {
            num = iValueOf;
        } else {
            try {
                if (!TextUtils.isEmpty(str2)) {
                    Request.Builder builderUrl = new Request.Builder().url(str);
                    builderUrl.addHeader("Host", domainFromUrl);
                    iValueOf = Integer.valueOf(OkHttpProxy.getInstance().execute_syn(builderUrl, this.okhttpCallback));
                    LogUtil.i(TAG, "result=" + iValueOf + "\uff0cconfigUrl=" + str);
                }
                num = iValueOf;
            } catch (Exception e) {
                num = 11;
                e.printStackTrace();
            }
        }
        return num.intValue();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.concurrent.Callable
    public Integer call() throws Exception {
        int iStart = start();
        int i = 3;
        while (iStart != 0 && i > 0) {
            LogUtil.i(TAG, "\u5217\u8868\u6587\u4ef6\u91cd\u65b0\u4e0b\u8f7d,\u8fd8\u6709" + i + "\u6b21\u91cd\u8bd5\u673a\u4f1a");
            i += -1;
            Lvsip.getInstance().clean();
            iStart = start();
        }
        return Integer.valueOf(iStart);
    }
}