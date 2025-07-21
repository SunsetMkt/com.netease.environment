package com.netease.pharos.util;

import android.content.Context;
import android.util.Log;
import com.netease.download.Const;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes5.dex */
public class LogFileProxy {
    private static final String TAG = "pharos";
    private static LogFileProxy sLogFileProxy;
    private static SimpleDateFormat simpleDateFormat;
    private final AtomicBoolean mSaveLog = new AtomicBoolean(false);
    private final int MAX_CAPACITY = 10240;
    private final AtomicBoolean mIsPrepared = new AtomicBoolean(false);
    private BlockingQueue<String> mQueue = null;
    private File mFile = null;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    private LogFileProxy() {
    }

    public static LogFileProxy getInstance() {
        if (sLogFileProxy == null) {
            synchronized (LogFileProxy.class) {
                if (sLogFileProxy == null) {
                    sLogFileProxy = new LogFileProxy();
                }
            }
        }
        return sLogFileProxy;
    }

    private static SimpleDateFormat getSdf() {
        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd HH:mm:ss.SSS ", Locale.getDefault());
        }
        return simpleDateFormat;
    }

    public synchronized void init(Context context, int i) {
        boolean zCreateNewFile;
        if (this.isRunning.get()) {
            return;
        }
        Log.w(TAG, "LogFileProxy [init] start");
        if (context == null) {
            Log.w(TAG, "LogFileProxy [init] context is null, [init] fail");
            return;
        }
        try {
            File externalFilesDir = context.getExternalFilesDir(null);
            if (externalFilesDir != null && externalFilesDir.exists()) {
                File file = new File(externalFilesDir, "pharos_log");
                if (!(!file.exists() ? file.mkdir() : true)) {
                    return;
                }
                File file2 = new File(file, "pharos_result.txt");
                this.mFile = file2;
                if (file2.exists()) {
                    this.mFile.delete();
                    zCreateNewFile = this.mFile.createNewFile();
                } else {
                    zCreateNewFile = this.mFile.createNewFile();
                }
                Log.i(TAG, "LogFileProxy [init] mFile path =" + this.mFile.getAbsolutePath() + ", isPrepared:" + zCreateNewFile);
                if (zCreateNewFile) {
                    if (this.mQueue == null) {
                        this.mQueue = new ArrayBlockingQueue(Math.max(10240, i));
                    }
                    this.mIsPrepared.set(zCreateNewFile);
                    start();
                }
            } else {
                this.mIsPrepared.set(false);
                Log.i(TAG, "LogFileProxy [init] Directory does not exist");
            }
        } catch (Throwable unused) {
            this.mIsPrepared.set(false);
            Log.i(TAG, "LogFileProxy [init] mFile does not exist");
        }
    }

    public void add(String str) {
        if (this.mIsPrepared.get()) {
            if (this.mQueue == null) {
                this.mQueue = new ArrayBlockingQueue(10240);
            }
            String str2 = getSdf().format(new Date());
            this.mQueue.add(str2 + str + "\n");
        }
    }

    private void start() {
        Log.i(TAG, "LogFileProxy [enter] LogThread   mIsPrepared:" + this.mIsPrepared.get() + ", isRunning:" + this.isRunning.get() + ",mSaveLog:" + this.mSaveLog.get());
        if (this.mIsPrepared.get() && !this.isRunning.get() && this.mSaveLog.get()) {
            Log.i(TAG, "LogFileProxy [init] LogThread  start");
            Thread thread = new Thread(new Runnable() { // from class: com.netease.pharos.util.LogFileProxy.1
                @Override // java.lang.Runnable
                public void run() throws Throwable {
                    Throwable th;
                    LogFileProxy.this.isRunning.set(true);
                    Log.i(LogFileProxy.TAG, "LogFileProxy [init] LogThread  start success!");
                    StringBuffer stringBuffer = null;
                    FileOutputStream fileOutputStream = null;
                    boolean z = false;
                    int i = 0;
                    int iMin = 0;
                    while (LogFileProxy.this.mSaveLog.get() && LogFileProxy.this.mQueue != null) {
                        try {
                            String str = (String) LogFileProxy.this.mQueue.take();
                            if (str.equals(Const.LOG_TYPE_STATE_FINISH)) {
                                break;
                            }
                            if (!z) {
                                iMin = Math.min(50, LogFileProxy.this.mQueue.size());
                                stringBuffer = new StringBuffer();
                                z = true;
                                i = 0;
                            }
                            stringBuffer.append(str);
                            if (i < iMin) {
                                i++;
                            } else {
                                Log.i(LogFileProxy.TAG, "LogFileProxy [write]--> cache size:" + iMin);
                                try {
                                    try {
                                        FileOutputStream fileOutputStream2 = new FileOutputStream(LogFileProxy.this.mFile, true);
                                        try {
                                            FileChannel channel = fileOutputStream2.getChannel();
                                            ByteBuffer byteBufferWrap = ByteBuffer.wrap(stringBuffer.toString().getBytes(StandardCharsets.UTF_8));
                                            channel.write(byteBufferWrap);
                                            byteBufferWrap.flip();
                                            fileOutputStream2.flush();
                                            channel.close();
                                            try {
                                                fileOutputStream2.close();
                                            } catch (IOException e) {
                                                Log.w(LogFileProxy.TAG, "LogFileProxy [Thread] finally IOException =" + e);
                                            }
                                            if (LogFileProxy.this.mQueue.size() < 5120) {
                                                try {
                                                    Thread.sleep(20L);
                                                } catch (Exception unused) {
                                                }
                                            }
                                            fileOutputStream = fileOutputStream2;
                                        } catch (IOException e2) {
                                            e = e2;
                                            fileOutputStream = fileOutputStream2;
                                            Log.w(LogFileProxy.TAG, "LogFileProxy [Thread] IOException =" + e);
                                            if (fileOutputStream != null) {
                                                try {
                                                    fileOutputStream.close();
                                                } catch (IOException e3) {
                                                    Log.w(LogFileProxy.TAG, "LogFileProxy [Thread] finally IOException =" + e3);
                                                }
                                            }
                                            if (LogFileProxy.this.mQueue.size() < 5120) {
                                                try {
                                                    Thread.sleep(20L);
                                                } catch (Exception unused2) {
                                                }
                                            }
                                            z = false;
                                        } catch (Throwable th2) {
                                            th = th2;
                                            fileOutputStream = fileOutputStream2;
                                            if (fileOutputStream != null) {
                                                try {
                                                    fileOutputStream.close();
                                                } catch (IOException e4) {
                                                    Log.w(LogFileProxy.TAG, "LogFileProxy [Thread] finally IOException =" + e4);
                                                }
                                            }
                                            if (LogFileProxy.this.mQueue.size() < 5120) {
                                                try {
                                                    Thread.sleep(20L);
                                                    throw th;
                                                } catch (Exception unused3) {
                                                    throw th;
                                                }
                                            }
                                            throw th;
                                        }
                                    } catch (Throwable th3) {
                                        th = th3;
                                    }
                                } catch (IOException e5) {
                                    e = e5;
                                }
                                z = false;
                            }
                        } catch (Exception e6) {
                            LogFileProxy.this.mIsPrepared.set(false);
                            Log.w(LogFileProxy.TAG, "LogFileProxy [Thread] InterruptedException =" + e6);
                        }
                    }
                    LogFileProxy.this.mQueue = null;
                    LogFileProxy.this.mIsPrepared.set(false);
                    LogFileProxy.this.isRunning.set(false);
                    Log.i(LogFileProxy.TAG, "LogFileProxy [init] LogThread  stopped");
                }
            });
            thread.setName("logThread");
            thread.start();
        }
    }

    public void finish() {
        this.mIsPrepared.set(false);
        if (this.mFile == null) {
            Log.w(TAG, "LogFileProxy [finish] mFile is null , [finish] fail");
            return;
        }
        BlockingQueue<String> blockingQueue = this.mQueue;
        if (blockingQueue != null) {
            blockingQueue.add(Const.LOG_TYPE_STATE_FINISH);
        }
    }

    public boolean isSaveLog() {
        return this.mSaveLog.get();
    }

    public synchronized void setSaveLog(boolean z) {
        this.mSaveLog.set(z);
        if (this.mSaveLog.get()) {
            start();
        }
    }
}