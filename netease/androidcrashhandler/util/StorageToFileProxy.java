package com.netease.androidcrashhandler.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.netease.androidcrashhandler.util.CUtil;
import com.netease.download.Const;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/* loaded from: classes4.dex */
public class StorageToFileProxy {
    public static final String CRASHHUNTER_LOG_FILE_PATH = "crashHunter_log";
    private static StorageToFileProxy sStorageToFileProxy;
    private boolean mHasInit = false;
    private boolean mHasStart = false;
    private BlockingQueue<String> mQueue = null;
    private File mFile = null;
    private BufferedWriter mOut = null;

    private StorageToFileProxy() {
    }

    public static StorageToFileProxy getInstances() {
        if (sStorageToFileProxy == null) {
            sStorageToFileProxy = new StorageToFileProxy();
        }
        return sStorageToFileProxy;
    }

    public void init(Context context, int i, String str) throws IOException {
        if (context == null) {
            Log.w(LogUtils.TAG, "StorageToFileProxy [init] context is null, [init] fail");
            return;
        }
        if (this.mHasInit) {
            Log.i(LogUtils.TAG, "StorageToFileProxy [init] has init");
            return;
        }
        if (TextUtils.isEmpty(str)) {
            Log.i(LogUtils.TAG, "StorageToFileProxy [init] logFilePath is error");
            return;
        }
        try {
            File file = new File(str);
            this.mFile = file;
            if (file.exists()) {
                Log.i(LogUtils.TAG, "StorageToFileProxy [init] delete file");
                this.mFile.delete();
            }
            if (!this.mFile.exists()) {
                Log.i(LogUtils.TAG, "StorageToFileProxy [init] create file");
                try {
                    this.mFile.createNewFile();
                } catch (IOException e) {
                    LogUtils.w(LogUtils.TAG, "StorageToFileProxy [init] createNewFile =" + e);
                    e.printStackTrace();
                }
            }
            this.mOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.mFile, true)));
            this.mHasInit = true;
            Log.i(LogUtils.TAG, "StorageToFileProxy [init] mFile path =" + this.mFile.getAbsolutePath());
        } catch (Exception e2) {
            Log.w(LogUtils.TAG, "StorageToFileProxy [init] Exception=" + e2);
        }
        if (!this.mHasInit) {
            Log.w(LogUtils.TAG, "StorageToFileProxy [init] init fail");
            return;
        }
        if (i < 500) {
            i = 500;
        }
        if (this.mQueue == null) {
            this.mQueue = new ArrayBlockingQueue(i);
        }
    }

    public void add(String str) {
        try {
            if (!this.mHasInit) {
                Log.w(LogUtils.TAG, "StorageToFileProxy [add] not initialized yet");
                return;
            }
            if (this.mQueue == null) {
                this.mQueue = new ArrayBlockingQueue(500);
            }
            this.mQueue.put(str + "\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isInit() {
        return this.mHasInit;
    }

    public void start() {
        if (this.mFile == null) {
            Log.w(LogUtils.TAG, "StorageToFileProxy [start] mFile is null , [start] fail");
        } else if (!this.mHasInit) {
            Log.w(LogUtils.TAG, "StorageToFileProxy [start] not initialized yet");
        } else {
            CUtil.runOnNewChildThread(new CUtil.ThreadTask() { // from class: com.netease.androidcrashhandler.util.StorageToFileProxy.1
                AnonymousClass1() {
                }

                @Override // com.netease.androidcrashhandler.util.CUtil.ThreadTask
                public void run() throws IOException {
                    while (StorageToFileProxy.this.mQueue != null) {
                        try {
                            try {
                                String str = (String) StorageToFileProxy.this.mQueue.take();
                                if (str.equals(Const.LOG_TYPE_STATE_FINISH)) {
                                    break;
                                }
                                try {
                                    if (StorageToFileProxy.this.mOut != null) {
                                        StorageToFileProxy.this.mOut.write(str);
                                    }
                                } catch (IOException e) {
                                    Log.w(LogUtils.TAG, "StorageToFileProxy [start] IOException =" + e);
                                }
                            } catch (InterruptedException e2) {
                                Log.w(LogUtils.TAG, "StorageToFileProxy [start] InterruptedException =" + e2);
                                return;
                            }
                        } catch (IOException e3) {
                            e3.printStackTrace();
                            return;
                        }
                    }
                    StorageToFileProxy.this.mOut.close();
                    Log.w(LogUtils.TAG, "StorageToFileProxy [start] end");
                }
            }, null);
        }
    }

    /* renamed from: com.netease.androidcrashhandler.util.StorageToFileProxy$1 */
    class AnonymousClass1 implements CUtil.ThreadTask {
        AnonymousClass1() {
        }

        @Override // com.netease.androidcrashhandler.util.CUtil.ThreadTask
        public void run() throws IOException {
            while (StorageToFileProxy.this.mQueue != null) {
                try {
                    try {
                        String str = (String) StorageToFileProxy.this.mQueue.take();
                        if (str.equals(Const.LOG_TYPE_STATE_FINISH)) {
                            break;
                        }
                        try {
                            if (StorageToFileProxy.this.mOut != null) {
                                StorageToFileProxy.this.mOut.write(str);
                            }
                        } catch (IOException e) {
                            Log.w(LogUtils.TAG, "StorageToFileProxy [start] IOException =" + e);
                        }
                    } catch (InterruptedException e2) {
                        Log.w(LogUtils.TAG, "StorageToFileProxy [start] InterruptedException =" + e2);
                        return;
                    }
                } catch (IOException e3) {
                    e3.printStackTrace();
                    return;
                }
            }
            StorageToFileProxy.this.mOut.close();
            Log.w(LogUtils.TAG, "StorageToFileProxy [start] end");
        }
    }

    public void finish() {
        Log.w(LogUtils.TAG, "StorageToFileProxy [finish] start");
        if (!this.mHasInit) {
            Log.w(LogUtils.TAG, "StorageToFileProxy [finish] not initialized yet");
            return;
        }
        if (this.mFile == null) {
            Log.w(LogUtils.TAG, "StorageToFileProxy [finish] mFile is null , [finish] fail");
            return;
        }
        BlockingQueue<String> blockingQueue = this.mQueue;
        if (blockingQueue != null) {
            blockingQueue.add(Const.LOG_TYPE_STATE_FINISH);
        }
    }

    public void flush() throws IOException {
        Log.i(LogUtils.TAG, "StorageToFileProxy [flush] start");
        if (!this.mHasInit) {
            Log.w(LogUtils.TAG, "StorageToFileProxy [flush] not initialized yet");
            return;
        }
        BufferedWriter bufferedWriter = this.mOut;
        if (bufferedWriter == null) {
            Log.w(LogUtils.TAG, "StorageToFileProxy [flush] mOut is null");
            return;
        }
        try {
            bufferedWriter.flush();
        } catch (IOException e) {
            Log.w(LogUtils.TAG, "StorageToFileProxy [flush] IOException=" + e.toString());
            e.printStackTrace();
        }
        Log.i(LogUtils.TAG, "StorageToFileProxy [flush] start");
    }
}