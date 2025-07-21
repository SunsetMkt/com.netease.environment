package com.netease.pharos.netlag;

import android.util.Log;
import com.netease.download.Const;
import com.netease.pharos.network.NetUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class LogUploadTask {
    private static final String TAG = "pharos";
    private static final String UPLOAD_OVERSEA_URL = "https://sigma-netlag-impression.proxima.nie.easebar.com/lag";
    private static final String UPLOAD_URL = "https://sigma-netlag-impression.proxima.nie.netease.com/lag";
    private final int MAX_CAPACITY = 512;
    private BlockingQueue<JSONObject> mQueue = null;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    public void add(JSONObject jSONObject) {
        if (this.mQueue == null) {
            this.mQueue = new ArrayBlockingQueue(512);
        }
        this.mQueue.add(jSONObject);
    }

    public void start() {
        Log.i(TAG, "LogUpload [enter] LogThread  isRunning:" + this.isRunning.get());
        if (this.isRunning.get()) {
            return;
        }
        Log.i(TAG, "LogUpload [init] LogThread  start");
        Thread thread = new Thread(new Runnable() { // from class: com.netease.pharos.netlag.LogUploadTask.1
            @Override // java.lang.Runnable
            public void run() throws InterruptedException {
                JSONObject jSONObject;
                int size;
                LogUploadTask.this.isRunning.set(true);
                Log.i(LogUploadTask.TAG, "LogUpload [init] LogThread  start success!");
                StringBuffer stringBuffer = null;
                boolean z = false;
                int i = 0;
                int iMin = 0;
                while (LogUploadTask.this.mQueue != null && (jSONObject = (JSONObject) LogUploadTask.this.mQueue.take()) != null && !jSONObject.optBoolean(Const.LOG_TYPE_STATE_FINISH, false)) {
                    try {
                        if (!z) {
                            iMin = Math.min(20, LogUploadTask.this.mQueue.size());
                            stringBuffer = new StringBuffer();
                            z = true;
                            i = 0;
                        }
                        stringBuffer.append(jSONObject);
                        if (i < iMin) {
                            i++;
                        } else {
                            Log.i(LogUploadTask.TAG, "LogUpload [upload]--> cache size:" + iMin);
                            try {
                                try {
                                    HashMap map = new HashMap();
                                    map.put("post_content", jSONObject);
                                    String str = NetworkCheckProxy.getInstance().isOversea() ? LogUploadTask.UPLOAD_OVERSEA_URL : LogUploadTask.UPLOAD_URL;
                                    Integer num = (Integer) NetUtil.doHttpReq(str, map, "POST", new HashMap(), null);
                                    Log.i(LogUploadTask.TAG, "LogUpload [upload]url:" + str);
                                    Log.i(LogUploadTask.TAG, "LogUpload [upload]result:" + num + ",data-->" + jSONObject);
                                } finally {
                                    if (size < i) {
                                        try {
                                            Thread.sleep(20L);
                                        } catch (Exception unused) {
                                        }
                                    }
                                }
                            } catch (IOException e) {
                                Log.w(LogUploadTask.TAG, "LogUpload [Thread] IOException =" + e);
                                if (LogUploadTask.this.mQueue.size() < 256) {
                                }
                            }
                            if (LogUploadTask.this.mQueue.size() < 256) {
                                try {
                                    Thread.sleep(20L);
                                } catch (Exception unused2) {
                                }
                            }
                            z = false;
                        }
                    } catch (Exception e2) {
                        Log.w(LogUploadTask.TAG, "LogUpload [Thread] InterruptedException =" + e2);
                    }
                }
                LogUploadTask.this.mQueue = null;
                LogUploadTask.this.isRunning.set(false);
                Log.i(LogUploadTask.TAG, "LogUpload [init] LogThread  stopped");
            }
        });
        thread.setName("logThread");
        thread.start();
    }

    public void finish() {
        if (this.isRunning.get()) {
            this.isRunning.set(false);
            if (this.mQueue != null) {
                try {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put(Const.LOG_TYPE_STATE_FINISH, true);
                    this.mQueue.add(jSONObject);
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        }
    }
}