package com.netease.download.woffset;

import android.text.TextUtils;
import com.netease.download.downloader.DownloadParams;
import com.netease.download.downloader.TaskHandleOp;
import com.netease.download.listener.DownloadListenerProxy;
import com.netease.download.util.LogUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/* loaded from: classes5.dex */
public class WoffsetProxy {
    private static final String TAG = "WoffsetProxy";
    private static WoffsetProxy mWoffsetProxy;
    private BlockingQueue<DownloadParams> mParamsQueue = null;
    private boolean mHasInit = false;

    public static WoffsetProxy getInstances() {
        if (mWoffsetProxy == null) {
            mWoffsetProxy = new WoffsetProxy();
        }
        return mWoffsetProxy;
    }

    public void reset() {
        LogUtil.i(TAG, "WoffsetProxy [reset] start");
        this.mHasInit = false;
        this.mParamsQueue = null;
    }

    public void init(int i) {
        LogUtil.i(TAG, "WoffsetProxy [init] start");
        if (this.mHasInit) {
            return;
        }
        this.mParamsQueue = new LinkedBlockingQueue(i);
        this.mHasInit = true;
        start();
    }

    public void put(DownloadParams downloadParams) {
        LogUtil.i(TAG, "WoffsetProxy [put] start");
        if (this.mParamsQueue != null) {
            LogUtil.i(TAG, "WoffsetProxy [put] call");
            this.mParamsQueue.add(downloadParams);
        } else {
            LogUtil.i(TAG, "WoffsetProxy [put] mCallQueue is null");
        }
    }

    private void start() {
        LogUtil.i(TAG, "WoffsetProxy [start] start");
        Thread thread = new Thread(new Runnable() { // from class: com.netease.download.woffset.WoffsetProxy.1
            @Override // java.lang.Runnable
            public void run() {
                DownloadParams downloadParams;
                while (WoffsetProxy.this.mParamsQueue != null && (downloadParams = (DownloadParams) WoffsetProxy.this.mParamsQueue.take()) != null) {
                    try {
                        LogUtil.i(WoffsetProxy.TAG, "WoffsetProxy [start] param=" + downloadParams.toString());
                        String filePath = downloadParams.getFilePath();
                        String str = downloadParams.getmOffsetTempFileName();
                        int i = downloadParams.getmResult();
                        if (i != 0) {
                            LogUtil.i(WoffsetProxy.TAG, "WoffsetProxy [start] \u6587\u4ef6\u672c\u8eab\u672a\u6210\u529f\u4e0b\u8f7d\uff0c\u56de\u8c03");
                            DownloadListenerProxy.getInstances();
                            DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsg(i, TaskHandleOp.getInstance().getTaskHandle().getTaskAllSizes(), DownloadListenerProxy.getInstances().getHasDownloadSize(), downloadParams.getCallBackFileName(), filePath, "".getBytes(), TaskHandleOp.getInstance().getTaskHandle().getSessionid(), TaskHandleOp.getInstance().getTaskHandle().getNtesOrbitId());
                        } else if (WoffsetProxy.this.write2FileOffset(filePath, str, downloadParams.getmWoffset())) {
                            LogUtil.i(WoffsetProxy.TAG, "WoffsetProxy [start] \u5199\u5165\u5230\u5df2\u6709\u6587\u4ef6\u6210\u529f\uff0c\u56de\u8c03");
                            LogUtil.i(WoffsetProxy.TAG, "[ORBIT] (" + Thread.currentThread().getId() + ") Download Params=\"" + downloadParams.getUrlResName() + "\" Filepath=\"" + downloadParams.getFilePath() + "\" Range=[" + downloadParams.getStart() + "-" + downloadParams.getLast() + "] Bytes=" + downloadParams.getSize() + " Cost=" + (System.currentTimeMillis() - downloadParams.getmStartDownloadTime()));
                            DownloadListenerProxy.getInstances();
                            DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsg(0, TaskHandleOp.getInstance().getTaskHandle().getTaskAllSizes(), DownloadListenerProxy.getInstances().getHasDownloadSize(), downloadParams.getCallBackFileName(), filePath, "".getBytes(), TaskHandleOp.getInstance().getTaskHandle().getSessionid(), TaskHandleOp.getInstance().getTaskHandle().getNtesOrbitId());
                        } else {
                            LogUtil.i(WoffsetProxy.TAG, "WoffsetProxy [start] \u5199\u5165\u5230\u5df2\u6709\u6587\u4ef6\u5931\u8d25\uff0c\u56de\u8c03 ");
                            LogUtil.i(WoffsetProxy.TAG, "[ORBIT] (" + Thread.currentThread().getId() + ") Download Params=\"" + downloadParams.getUrlResName() + "\" Filepath=\"" + downloadParams.getFilePath() + "\" Range=[" + downloadParams.getStart() + "-" + downloadParams.getLast() + "] Bytes=" + downloadParams.getSize() + " Cost=" + (System.currentTimeMillis() - downloadParams.getmStartDownloadTime()));
                            DownloadListenerProxy.getInstances();
                            DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsg(4, TaskHandleOp.getInstance().getTaskHandle().getTaskAllSizes(), DownloadListenerProxy.getInstances().getHasDownloadSize(), downloadParams.getCallBackFileName(), filePath, "".getBytes(), TaskHandleOp.getInstance().getTaskHandle().getSessionid(), TaskHandleOp.getInstance().getTaskHandle().getNtesOrbitId());
                        }
                    } catch (InterruptedException e) {
                        LogUtil.i(WoffsetProxy.TAG, "OtherProxy [start] InterruptedException=" + e.toString());
                        return;
                    }
                }
            }
        });
        thread.setName("donwloader woffset");
        thread.start();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r11v10, types: [java.nio.channels.FileChannel] */
    /* JADX WARN: Type inference failed for: r11v16 */
    /* JADX WARN: Type inference failed for: r11v19 */
    /* JADX WARN: Type inference failed for: r11v9 */
    /* JADX WARN: Type inference failed for: r5v19 */
    /* JADX WARN: Type inference failed for: r5v20 */
    /* JADX WARN: Type inference failed for: r5v5 */
    public boolean write2FileOffset(String str, String str2, long j) throws Throwable {
        FileChannel channel;
        StringBuilder sb;
        RandomAccessFile randomAccessFile;
        LogUtil.i(TAG, "WoffsetProxy [write2File] start");
        LogUtil.i(TAG, "WoffsetProxy [write2File] srcPath=" + str + ", desPath=" + str2 + ", woffset=" + j);
        boolean z = false;
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || j < 0) {
            LogUtil.i(TAG, "WoffsetProxy [write2File] param error");
            return false;
        }
        File file = new File(str);
        if (!file.exists()) {
            LogUtil.i(TAG, "WoffsetProxy [write2File] \u6e90\u6587\u4ef6\u4e0d\u5b58\u5728");
            return false;
        }
        File file2 = new File(str2);
        boolean zExists = file2.getParentFile().exists();
        boolean z2 = zExists;
        if (!zExists) {
            File parentFile = file2.getParentFile();
            parentFile.mkdirs();
            z2 = parentFile;
        }
        try {
            file2.createNewFile();
            channel = z2;
        } catch (IOException e) {
            String str3 = "WoffsetProxy [write2File] IOException1" + e.toString();
            LogUtil.i(TAG, str3);
            e.printStackTrace();
            channel = str3;
        }
        RandomAccessFile randomAccessFile2 = null;
        channel = null;
        channel = null;
        FileChannel channel2 = null;
        randomAccessFile2 = null;
        randomAccessFile2 = null;
        randomAccessFile2 = null;
        randomAccessFile2 = null;
        randomAccessFile2 = null;
        randomAccessFile2 = null;
        try {
            try {
                channel = new FileInputStream(str).getChannel();
                try {
                    ByteBuffer byteBufferAllocate = ByteBuffer.allocate(32768);
                    randomAccessFile = new RandomAccessFile(str2, "rwd");
                    try {
                        randomAccessFile.seek(j);
                        channel2 = randomAccessFile.getChannel();
                        while (channel.read(byteBufferAllocate) != -1) {
                            byteBufferAllocate.flip();
                            channel2.write(byteBufferAllocate);
                            byteBufferAllocate.clear();
                        }
                        if (file.exists()) {
                            LogUtil.i(TAG, "WoffsetProxy [write2File] \u5199\u5165\u6210\u529f\uff0c\u5220\u9664\u6587\u4ef6 = " + str);
                            file.delete();
                            z = true;
                        }
                    } catch (FileNotFoundException e2) {
                        e = e2;
                        str2 = channel2;
                        randomAccessFile2 = randomAccessFile;
                        channel = channel;
                        LogUtil.i(TAG, "WoffsetProxy [write2File] FileNotFoundException" + e.toString());
                        e.printStackTrace();
                        if (randomAccessFile2 != null) {
                            try {
                                randomAccessFile2.close();
                            } catch (IOException e3) {
                                e = e3;
                                sb = new StringBuilder("WoffsetProxy [write2File] IOException3");
                                sb.append(e.toString());
                                LogUtil.i(TAG, sb.toString());
                                e.printStackTrace();
                                return z;
                            }
                        }
                        if (channel != 0) {
                            channel.close();
                        }
                        if (str2 != 0) {
                            str2.close();
                        }
                        return z;
                    } catch (IOException e4) {
                        e = e4;
                        str2 = channel2;
                        randomAccessFile2 = randomAccessFile;
                        channel = channel;
                        LogUtil.i(TAG, "WoffsetProxy [write2File] IOException2" + e.toString());
                        e.printStackTrace();
                        if (randomAccessFile2 != null) {
                            try {
                                randomAccessFile2.close();
                            } catch (IOException e5) {
                                e = e5;
                                sb = new StringBuilder("WoffsetProxy [write2File] IOException3");
                                sb.append(e.toString());
                                LogUtil.i(TAG, sb.toString());
                                e.printStackTrace();
                                return z;
                            }
                        }
                        if (channel != 0) {
                            channel.close();
                        }
                        if (str2 != 0) {
                            str2.close();
                        }
                        return z;
                    } catch (Throwable th) {
                        th = th;
                        str2 = channel2;
                        randomAccessFile2 = randomAccessFile;
                        if (randomAccessFile2 != null) {
                            try {
                                randomAccessFile2.close();
                            } catch (IOException e6) {
                                LogUtil.i(TAG, "WoffsetProxy [write2File] IOException3" + e6.toString());
                                e6.printStackTrace();
                                throw th;
                            }
                        }
                        if (channel != 0) {
                            channel.close();
                        }
                        if (str2 != 0) {
                            str2.close();
                        }
                        throw th;
                    }
                } catch (FileNotFoundException e7) {
                    e = e7;
                    str2 = 0;
                    channel = channel;
                } catch (IOException e8) {
                    e = e8;
                    str2 = 0;
                    channel = channel;
                } catch (Throwable th2) {
                    th = th2;
                    str2 = 0;
                }
            } catch (FileNotFoundException e9) {
                e = e9;
                str2 = 0;
                channel = 0;
            } catch (IOException e10) {
                e = e10;
                str2 = 0;
                channel = 0;
            } catch (Throwable th3) {
                th = th3;
                str2 = 0;
                channel = 0;
            }
            try {
                randomAccessFile.close();
                if (channel != 0) {
                    channel.close();
                }
                if (channel2 != null) {
                    channel2.close();
                }
            } catch (IOException e11) {
                e = e11;
                sb = new StringBuilder("WoffsetProxy [write2File] IOException3");
                sb.append(e.toString());
                LogUtil.i(TAG, sb.toString());
                e.printStackTrace();
                return z;
            }
            return z;
        } catch (Throwable th4) {
            th = th4;
        }
    }
}