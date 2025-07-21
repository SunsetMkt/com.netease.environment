package com.netease.download.storage;

import android.content.Context;
import com.alipay.sdk.m.u.b;
import com.netease.download.Const;
import com.netease.download.downloader.DownloadProxy;
import com.netease.download.util.LogUtil;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/* loaded from: classes5.dex */
public class StorageToFileProxy {
    private static final String TAG = "StorageToFileProxy";
    private static StorageToFileProxy sStorageToFileProxy;
    private ExecutorService mExs = Executors.newSingleThreadExecutor();
    private ArrayList<Future<Integer>> mAl = new ArrayList<>();
    private BlockingQueue<String> mQueue = new ArrayBlockingQueue(2000);
    private File mFile = null;
    private BufferedWriter mOut = null;
    private boolean mIsStart = false;

    private StorageToFileProxy() {
    }

    public static StorageToFileProxy getInstances() {
        if (sStorageToFileProxy == null) {
            sStorageToFileProxy = new StorageToFileProxy();
        }
        return sStorageToFileProxy;
    }

    public void init(Context context) {
        StringBuilder sb;
        LogUtil.i(TAG, "StorageToFileProxy [init] start");
        if (DownloadProxy.mContext == null) {
            LogUtil.i(TAG, "StorageToFileProxy [init] param error");
            return;
        }
        File externalFilesDir = DownloadProxy.mContext.getExternalFilesDir(null);
        if (externalFilesDir == null || !externalFilesDir.exists()) {
            return;
        }
        File file = new File(externalFilesDir.getAbsolutePath() + "/android_download_log/download_result.txt");
        this.mFile = file;
        if (file.exists()) {
            this.mFile.delete();
        }
        if (!this.mFile.exists()) {
            try {
                this.mFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.mFile, true)));
                this.mOut = bufferedWriter;
                try {
                    bufferedWriter.flush();
                    this.mOut.close();
                } catch (IOException e2) {
                    e = e2;
                    sb = new StringBuilder("StorageToFileProxy [init] IOException=");
                    sb.append(e.toString());
                    LogUtil.i(TAG, sb.toString());
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e3) {
                LogUtil.i(TAG, "StorageToFileProxy [init] FileNotFoundException=" + e3.toString());
                e3.printStackTrace();
                BufferedWriter bufferedWriter2 = this.mOut;
                if (bufferedWriter2 != null) {
                    try {
                        bufferedWriter2.flush();
                        this.mOut.close();
                    } catch (IOException e4) {
                        e = e4;
                        sb = new StringBuilder("StorageToFileProxy [init] IOException=");
                        sb.append(e.toString());
                        LogUtil.i(TAG, sb.toString());
                        e.printStackTrace();
                    }
                }
            }
        } catch (Throwable th) {
            BufferedWriter bufferedWriter3 = this.mOut;
            if (bufferedWriter3 != null) {
                try {
                    bufferedWriter3.flush();
                    this.mOut.close();
                } catch (IOException e5) {
                    LogUtil.i(TAG, "StorageToFileProxy [init] IOException=" + e5.toString());
                    e5.printStackTrace();
                }
            }
            throw th;
        }
    }

    public void add(String str) {
        try {
            BlockingQueue<String> blockingQueue = this.mQueue;
            if (blockingQueue != null) {
                blockingQueue.put(str);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        LogUtil.i(TAG, "StorageToFileProxy [start] start");
        if (this.mIsStart) {
            return;
        }
        this.mIsStart = true;
        new Thread(new Runnable() { // from class: com.netease.download.storage.StorageToFileProxy.1
            AnonymousClass1() {
            }

            @Override // java.lang.Runnable
            public void run() throws IOException {
                while (true) {
                    try {
                        try {
                            String str = (String) StorageToFileProxy.this.mQueue.take();
                            if (!str.equals(Const.LOG_TYPE_STATE_FINISH)) {
                                try {
                                    StorageToFileProxy.this.mOut.write(str);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                StorageToFileProxy.this.mOut.close();
                                return;
                            }
                        } catch (IOException e2) {
                            e2.printStackTrace();
                            return;
                        }
                    } catch (InterruptedException e3) {
                        e3.printStackTrace();
                        return;
                    }
                }
            }
        }).start();
    }

    /* renamed from: com.netease.download.storage.StorageToFileProxy$1 */
    class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public void run() throws IOException {
            while (true) {
                try {
                    try {
                        String str = (String) StorageToFileProxy.this.mQueue.take();
                        if (!str.equals(Const.LOG_TYPE_STATE_FINISH)) {
                            try {
                                StorageToFileProxy.this.mOut.write(str);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            StorageToFileProxy.this.mOut.close();
                            return;
                        }
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        return;
                    }
                } catch (InterruptedException e3) {
                    e3.printStackTrace();
                    return;
                }
            }
        }
    }

    /* renamed from: com.netease.download.storage.StorageToFileProxy$2 */
    class AnonymousClass2 implements Runnable {
        AnonymousClass2() {
        }

        @Override // java.lang.Runnable
        public void run() throws InterruptedException {
            try {
                Thread.sleep(b.f1465a);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LogUtil.i(StorageToFileProxy.TAG, "\u4e0b\u8f7d\u8be6\u60c5\uff0c\u53d1\u8d77\u7ed3\u675f\u547d\u4ee4");
            try {
                if (StorageToFileProxy.this.mQueue.isEmpty()) {
                    return;
                }
                StorageToFileProxy.this.mQueue.put(Const.LOG_TYPE_STATE_FINISH);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void finish() {
        new Thread(new Runnable() { // from class: com.netease.download.storage.StorageToFileProxy.2
            AnonymousClass2() {
            }

            @Override // java.lang.Runnable
            public void run() throws InterruptedException {
                try {
                    Thread.sleep(b.f1465a);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                LogUtil.i(StorageToFileProxy.TAG, "\u4e0b\u8f7d\u8be6\u60c5\uff0c\u53d1\u8d77\u7ed3\u675f\u547d\u4ee4");
                try {
                    if (StorageToFileProxy.this.mQueue.isEmpty()) {
                        return;
                    }
                    StorageToFileProxy.this.mQueue.put(Const.LOG_TYPE_STATE_FINISH);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
        }).start();
    }
}