package com.netease.download.list;

import android.content.Context;
import android.text.TextUtils;
import com.netease.download.downloader.DownloadParams;
import com.netease.download.downloader.FileHandle;
import com.netease.download.downloader.TaskHandleOp;
import com.netease.download.handler.Dispatcher;
import com.netease.download.listener.DownloadListenerProxy;
import com.netease.download.taskManager.TaskExecutor;
import com.netease.download.util.HashUtil;
import com.netease.download.util.LogUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/* loaded from: classes5.dex */
public class PatchListProxy {
    private static final String TAG = "PatchListProxy";
    public static long mStartTime;
    private static PatchListProxy sPatchListProxy;
    private PatchListCore mPatchListCore = null;
    private int mRetry = 3;
    private String mFilePath = null;
    private String mFileName = null;
    private DownloadParams mDownloadParams = null;

    private PatchListProxy() {
    }

    public static PatchListProxy getInstances() {
        if (sPatchListProxy == null) {
            sPatchListProxy = new PatchListProxy();
        }
        return sPatchListProxy;
    }

    public void init(Context context, DownloadParams downloadParams) {
        if (downloadParams == null) {
            return;
        }
        this.mDownloadParams = downloadParams;
        String targetUrl = downloadParams.getTargetUrl();
        this.mFilePath = downloadParams.getFilePath();
        this.mFileName = downloadParams.getCallBackFileName();
        PatchListCore patchListCore = new PatchListCore();
        this.mPatchListCore = patchListCore;
        patchListCore.init(context, targetUrl, null, this.mFilePath, this.mFileName, downloadParams.getFileId());
        Dispatcher.getTaskParamsMap().put(downloadParams.getFileId(), new FileHandle(downloadParams));
    }

    public int start() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        mStartTime = jCurrentTimeMillis;
        int iIntValue = 11;
        if (this.mDownloadParams == null) {
            LogUtil.i(TAG, "PatchListProxy [start] mDownloadParams is null");
            LogUtil.i(TAG, "list\u6587\u4ef6\u4e0b\u8f7d\u7ed3\u675f\uff0cresult=11, path=" + this.mFilePath);
            DownloadListenerProxy.getInstances();
            DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsg(11, TaskHandleOp.getInstance().getTaskHandle().getTaskAllSizes(), DownloadListenerProxy.getInstances().getHasDownloadSize(), this.mFileName, this.mFilePath, "".getBytes(), TaskHandleOp.getInstance().getTaskHandle().getSessionid(), TaskHandleOp.getInstance().getTaskHandle().getNtesOrbitId());
            return 11;
        }
        if (!needDownload()) {
            LogUtil.i(TAG, "PatchListProxy [start] \u4e0d\u9700\u8981\u91cd\u65b0\u4e0b\u8f7d");
            LogUtil.i(TAG, "list\u6587\u4ef6\u4e0b\u8f7d\u7ed3\u675f\uff0cresult=0, path=" + this.mFilePath);
            DownloadListenerProxy.getInstances();
            DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsg(0, TaskHandleOp.getInstance().getTaskHandle().getTaskAllSizes(), DownloadListenerProxy.getInstances().getHasDownloadSize(), this.mFileName, this.mFilePath, "".getBytes(), TaskHandleOp.getInstance().getTaskHandle().getSessionid(), TaskHandleOp.getInstance().getTaskHandle().getNtesOrbitId());
            return 0;
        }
        ArrayList arrayList = new ArrayList();
        try {
            arrayList.add(TaskExecutor.getInstance().getFixedThreadPool().submit(this.mPatchListCore));
        } catch (Exception e) {
            LogUtil.i(TAG, "list\u6587\u4ef6\u4e0b\u8f7d add Exception=" + e);
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            try {
                iIntValue = ((Integer) ((Future) it.next()).get()).intValue();
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            } catch (ExecutionException e3) {
                e3.printStackTrace();
            }
        }
        LogUtil.i(TAG, "list\u6587\u4ef6\u4e0b\u8f7d\u7ed3\u675f\uff0cresult=" + iIntValue + ", path=" + this.mFilePath);
        StringBuilder sb = new StringBuilder("list\u6587\u4ef6\u4e0b\u8f7d\u7ed3\u675f\uff0c\u8017\u65f6=");
        sb.append(System.currentTimeMillis() - jCurrentTimeMillis);
        LogUtil.i(TAG, sb.toString());
        if (!TaskHandleOp.getInstance().getTaskHandle().isRammode()) {
            LogUtil.i(TAG, "PatchListProxy [start] \u4e0b\u8f7d\u540e\u671f\uff0c\u53d1\u9001\u65e5\u5fd7\uff08List\u6587\u4ef6\uff09");
            DownloadListenerProxy.getInstances();
            DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsg(iIntValue, TaskHandleOp.getInstance().getTaskHandle().getTaskAllSizes(), DownloadListenerProxy.getInstances().getHasDownloadSize(), this.mFileName, this.mFilePath, "".getBytes(), TaskHandleOp.getInstance().getTaskHandle().getSessionid(), TaskHandleOp.getInstance().getTaskHandle().getNtesOrbitId());
        } else {
            LogUtil.i(TAG, "PatchListProxy [start] Rammode\u6a21\u5f0f  call sendFinishMsg");
            FileHandle fileHandle = Dispatcher.getTaskParamsMap().get(this.mDownloadParams.getFileId());
            DownloadListenerProxy.getInstances();
            DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsg(iIntValue, TaskHandleOp.getInstance().getTaskHandle().getTaskAllSizes(), DownloadListenerProxy.getInstances().getHasDownloadSize(), this.mFileName, this.mFilePath, fileHandle.getDownloadParams().getByteBuffer().array(), TaskHandleOp.getInstance().getTaskHandle().getSessionid(), TaskHandleOp.getInstance().getTaskHandle().getNtesOrbitId());
            fileHandle.getDownloadParams().clearByteBuffer();
        }
        LogUtil.i(TAG, "PatchListProxy [start] \u4e0b\u8f7d\u540e\u671f\uff0c\u53d1\u9001\u65e5\u5fd7\uff08List\u6587\u4ef6\uff09");
        return iIntValue;
    }

    public boolean needDownload() {
        if (this.mDownloadParams == null) {
            LogUtil.i(TAG, "PatchListProxy [needDownload] mDownloadParams is null");
            return false;
        }
        if (TaskHandleOp.getInstance().getTaskHandle().isRenew()) {
            LogUtil.i(TAG, "PatchListProxy [needDownload] \u7528\u6237\u53c2\u6570\u8bbe\u7f6e renew = true");
            return true;
        }
        String md5 = this.mDownloadParams.getMd5();
        String filePath = this.mDownloadParams.getFilePath();
        LogUtil.i(TAG, "PatchListProxy [needDownload] urlPath=" + filePath);
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        if (new File(filePath).exists()) {
            if ("NotMD5".equals(md5)) {
                return false;
            }
            String strCalculateFileHash = HashUtil.calculateFileHash(TaskHandleOp.getInstance().getTaskHandle().getEncryptionAlgorithm(), filePath);
            LogUtil.i(TAG, "PatchListProxy [needDownload] configFileMd5=" + strCalculateFileHash + ", md5=" + md5);
            if (!TextUtils.isEmpty(strCalculateFileHash) && !TextUtils.isEmpty(md5) && strCalculateFileHash.equals(md5)) {
                LogUtil.i(TAG, "PatchListProxy [needDownload] \u6587\u4ef6\u5b58\u5728\uff0c\u4f46\u662fmd5\u4e0d\u4e00\u6837\uff0c\u9700\u8981\u4e0b\u8f7d");
                return false;
            }
        } else {
            LogUtil.i(TAG, "PatchListProxy [needDownload] \u6587\u4ef6\u4e0d\u5b58\u5728\uff0c\u9700\u8981\u4e0b\u8f7d");
        }
        return true;
    }
}