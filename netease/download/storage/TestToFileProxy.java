package com.netease.download.storage;

import android.content.Context;
import com.alipay.sdk.m.u.b;
import com.netease.download.Const;
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
public class TestToFileProxy {
    private static final String TAG = "TestToFileProxy";
    private static TestToFileProxy sTestToFileProxy;
    private ExecutorService mExs = Executors.newSingleThreadExecutor();
    private ArrayList<Future<Integer>> mAl = new ArrayList<>();
    private BlockingQueue<String> mQueue = new ArrayBlockingQueue(2000);
    private File mFile = null;
    private BufferedWriter mOut = null;

    private TestToFileProxy() {
    }

    public static TestToFileProxy getInstances() {
        if (sTestToFileProxy == null) {
            sTestToFileProxy = new TestToFileProxy();
        }
        return sTestToFileProxy;
    }

    public void init(Context context) throws IOException {
        StringBuilder sb;
        File file = new File(context.getExternalCacheDir().getAbsolutePath() + "/download_test.txt");
        this.mFile = file;
        if (file.exists()) {
            this.mFile.delete();
        }
        if (!this.mFile.getParentFile().exists()) {
            this.mFile.getParentFile().mkdirs();
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
                    sb = new StringBuilder("TestToFileProxy [init] IOException=");
                    sb.append(e.toString());
                    LogUtil.w(TAG, sb.toString());
                    e.printStackTrace();
                }
            } catch (Throwable th) {
                BufferedWriter bufferedWriter2 = this.mOut;
                if (bufferedWriter2 != null) {
                    try {
                        bufferedWriter2.flush();
                        this.mOut.close();
                    } catch (IOException e3) {
                        LogUtil.w(TAG, "TestToFileProxy [init] IOException=" + e3.toString());
                        e3.printStackTrace();
                    }
                }
                throw th;
            }
        } catch (FileNotFoundException e4) {
            LogUtil.w(TAG, "TestToFileProxy [init] FileNotFoundException=" + e4.toString());
            e4.printStackTrace();
            BufferedWriter bufferedWriter3 = this.mOut;
            if (bufferedWriter3 != null) {
                try {
                    bufferedWriter3.flush();
                    this.mOut.close();
                } catch (IOException e5) {
                    e = e5;
                    sb = new StringBuilder("TestToFileProxy [init] IOException=");
                    sb.append(e.toString());
                    LogUtil.w(TAG, sb.toString());
                    e.printStackTrace();
                }
            }
        }
    }

    public void add(String str) throws InterruptedException {
        try {
            if (this.mQueue.isEmpty()) {
                return;
            }
            this.mQueue.put(str);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        new Thread(new Runnable() { // from class: com.netease.download.storage.TestToFileProxy.1
            @Override // java.lang.Runnable
            public void run() throws IOException {
                while (true) {
                    try {
                        try {
                            String str = (String) TestToFileProxy.this.mQueue.take();
                            if (!str.equals(Const.LOG_TYPE_STATE_FINISH)) {
                                try {
                                    TestToFileProxy.this.mOut.write(str);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                TestToFileProxy.this.mOut.close();
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

    public void finish() {
        new Thread(new Runnable() { // from class: com.netease.download.storage.TestToFileProxy.2
            @Override // java.lang.Runnable
            public void run() throws InterruptedException {
                try {
                    Thread.sleep(b.f1465a);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                LogUtil.i(TestToFileProxy.TAG, "\u5206\u7247\u4e0b\u8f7d\u8be6\u60c5\uff0c\u53d1\u8d77\u7ed3\u675f\u547d\u4ee4");
                try {
                    if (TestToFileProxy.this.mQueue.isEmpty()) {
                        return;
                    }
                    TestToFileProxy.this.mQueue.put(Const.LOG_TYPE_STATE_FINISH);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
        }).start();
    }
}