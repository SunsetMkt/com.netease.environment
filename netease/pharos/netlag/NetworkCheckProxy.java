package com.netease.pharos.netlag;

import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView;
import com.netease.pharos.OnNetLagCallback;
import com.netease.pharos.PharosProxy;
import com.netease.pharos.network.NetworkStatus;
import com.netease.pharos.util.LogUtil;
import com.netease.pharos.util.Util;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class NetworkCheckProxy {
    private static NetworkCheckProxy sInstance;
    private NetworkCheck mNetworkCheck;
    private NetworkCheckConfig mNetworkCheckConfig;
    private Timer mTimer;
    private LogUploadTask mUploadTask;
    private final AtomicBoolean mTaskIsRunning = new AtomicBoolean(false);
    private ScheduledExecutorService mExecutorService = null;
    private final AtomicInteger mTaskRepeatCount = new AtomicInteger(-1);
    private final OnNetLagCallback mPharosListener = new OnNetLagCallback() { // from class: com.netease.pharos.netlag.NetworkCheckProxy.1
        AnonymousClass1() {
        }

        @Override // com.netease.pharos.OnNetLagCallback
        public void onFinish(JSONObject jSONObject) {
            LogUtil.stepLog("NetLag result:" + jSONObject.toString());
            OnNetLagCallback onNetLagCallback = PharosProxy.getInstance().getOnNetLagCallback();
            if (onNetLagCallback != null) {
                onNetLagCallback.onFinish(jSONObject);
            }
        }

        @Override // com.netease.pharos.OnNetLagCallback
        public void onCancel(JSONObject jSONObject) {
            LogUtil.stepLog("NetLag result[cancel]:" + jSONObject.toString());
            OnNetLagCallback onNetLagCallback = PharosProxy.getInstance().getOnNetLagCallback();
            if (onNetLagCallback != null) {
                onNetLagCallback.onCancel(jSONObject);
            }
        }
    };

    /* renamed from: com.netease.pharos.netlag.NetworkCheckProxy$1 */
    class AnonymousClass1 implements OnNetLagCallback {
        AnonymousClass1() {
        }

        @Override // com.netease.pharos.OnNetLagCallback
        public void onFinish(JSONObject jSONObject) {
            LogUtil.stepLog("NetLag result:" + jSONObject.toString());
            OnNetLagCallback onNetLagCallback = PharosProxy.getInstance().getOnNetLagCallback();
            if (onNetLagCallback != null) {
                onNetLagCallback.onFinish(jSONObject);
            }
        }

        @Override // com.netease.pharos.OnNetLagCallback
        public void onCancel(JSONObject jSONObject) {
            LogUtil.stepLog("NetLag result[cancel]:" + jSONObject.toString());
            OnNetLagCallback onNetLagCallback = PharosProxy.getInstance().getOnNetLagCallback();
            if (onNetLagCallback != null) {
                onNetLagCallback.onCancel(jSONObject);
            }
        }
    }

    private NetworkCheckProxy() {
    }

    public static NetworkCheckProxy getInstance() {
        if (sInstance == null) {
            synchronized (NetworkCheckProxy.class) {
                if (sInstance == null) {
                    sInstance = new NetworkCheckProxy();
                }
            }
        }
        return sInstance;
    }

    public NetworkCheckConfig getConfig() {
        return this.mNetworkCheckConfig;
    }

    public boolean isOversea() {
        return Integer.parseInt(this.mNetworkCheckConfig.getArea()) != 0;
    }

    public synchronized void start(JSONObject jSONObject, OnNetLagCallback onNetLagCallback) {
        LogUtil.stepLog("NetLag start udp speed :" + jSONObject.toString());
        NetworkStatus.getInstance().setNetwork(Util.getSystemParams(UniWebView.ACTION_GETNETWORKTYPE));
        if (this.mTaskIsRunning.get()) {
            LogUtil.stepLog("NetLag is Running :" + this.mTaskRepeatCount.get());
            NetworkResult networkResultIsProcessing = NetworkResult.isProcessing(jSONObject);
            JSONObject result = networkResultIsProcessing.getResult();
            uploadResult(networkResultIsProcessing);
            this.mPharosListener.onFinish(result);
        } else {
            this.mTaskIsRunning.set(true);
            NetworkCheck networkCheck = new NetworkCheck();
            this.mNetworkCheck = networkCheck;
            try {
                networkCheck.init(jSONObject, new NetworkCheckListener() { // from class: com.netease.pharos.netlag.NetworkCheckProxy.2
                    AnonymousClass2() {
                    }

                    @Override // com.netease.pharos.netlag.NetworkCheckListener
                    public void callBack(NetworkResult networkResult) {
                        LogUtil.stepLog("NetLag onFinish");
                        NetworkCheckProxy.this.mPharosListener.onFinish(networkResult.getResult());
                        LogUtil.stepLog("NetLag left:" + NetworkCheckProxy.this.mTaskRepeatCount.get());
                        if (NetworkCheckProxy.this.mTaskRepeatCount.get() == 0) {
                            LogUtil.stepLog("NetLag stop network check");
                            if (NetworkCheckProxy.this.mTimer != null) {
                                NetworkCheckProxy.this.mTimer.cancel();
                                NetworkCheckProxy.this.mTimer = null;
                            }
                            NetworkCheckProxy.this.mTaskIsRunning.set(false);
                            NetworkCheckProxy.this.stopExecutorService();
                        }
                        NetworkCheckProxy.this.uploadResult(networkResult);
                    }
                });
                NetworkCheckConfig networkCheckConfig = this.mNetworkCheck.getNetworkCheckConfig();
                this.mNetworkCheckConfig = networkCheckConfig;
                if (networkCheckConfig.getRetryCount() != 0) {
                    this.mTaskRepeatCount.set(this.mNetworkCheckConfig.getRetryCount());
                }
                if (this.mNetworkCheckConfig.getRetryCount() == 0) {
                    this.mTaskRepeatCount.set(-1);
                    Timer timer = new Timer();
                    this.mTimer = timer;
                    timer.scheduleAtFixedRate(new TimerTask() { // from class: com.netease.pharos.netlag.NetworkCheckProxy.3
                        AnonymousClass3() {
                        }

                        @Override // java.util.TimerTask, java.lang.Runnable
                        public void run() {
                            NetworkCheckProxy.this.mNetworkCheck.exec();
                        }
                    }, 0L, this.mNetworkCheckConfig.getRetryInterval() * 1000);
                } else if (this.mNetworkCheckConfig.getRetryCount() == 1) {
                    this.mTaskRepeatCount.set(1);
                    this.mTimer = null;
                    this.mNetworkCheck.exec();
                } else {
                    this.mTaskRepeatCount.set(this.mNetworkCheckConfig.getRetryCount());
                    Timer timer2 = new Timer();
                    this.mTimer = timer2;
                    timer2.scheduleAtFixedRate(new TimerTask() { // from class: com.netease.pharos.netlag.NetworkCheckProxy.4
                        AnonymousClass4() {
                        }

                        @Override // java.util.TimerTask, java.lang.Runnable
                        public void run() {
                            LogUtil.stepLog("NetLag repeat:" + NetworkCheckProxy.this.mTaskRepeatCount.get());
                            if (NetworkCheckProxy.this.mTaskRepeatCount.get() == 0) {
                                if (NetworkCheckProxy.this.mTimer != null) {
                                    NetworkCheckProxy.this.mTimer.cancel();
                                }
                            } else {
                                LogUtil.stepLog("NetLag start:" + NetworkCheckProxy.this.mTaskRepeatCount.get());
                                NetworkCheckProxy.this.mNetworkCheck.exec();
                                NetworkCheckProxy.this.mTaskRepeatCount.decrementAndGet();
                            }
                        }
                    }, 0L, this.mNetworkCheckConfig.getRetryInterval() * 1000);
                }
            } catch (Exception unused) {
                this.mNetworkCheckConfig = this.mNetworkCheck.getNetworkCheckConfig();
                NetworkResult networkResultFail = NetworkResult.fail(1);
                uploadResult(networkResultFail);
                JSONObject result2 = networkResultFail.getResult();
                LogUtil.stepLog("NetLag result:" + result2.toString());
                this.mPharosListener.onFinish(result2);
                stopExecutorService();
            }
        }
    }

    /* renamed from: com.netease.pharos.netlag.NetworkCheckProxy$2 */
    class AnonymousClass2 implements NetworkCheckListener {
        AnonymousClass2() {
        }

        @Override // com.netease.pharos.netlag.NetworkCheckListener
        public void callBack(NetworkResult networkResult) {
            LogUtil.stepLog("NetLag onFinish");
            NetworkCheckProxy.this.mPharosListener.onFinish(networkResult.getResult());
            LogUtil.stepLog("NetLag left:" + NetworkCheckProxy.this.mTaskRepeatCount.get());
            if (NetworkCheckProxy.this.mTaskRepeatCount.get() == 0) {
                LogUtil.stepLog("NetLag stop network check");
                if (NetworkCheckProxy.this.mTimer != null) {
                    NetworkCheckProxy.this.mTimer.cancel();
                    NetworkCheckProxy.this.mTimer = null;
                }
                NetworkCheckProxy.this.mTaskIsRunning.set(false);
                NetworkCheckProxy.this.stopExecutorService();
            }
            NetworkCheckProxy.this.uploadResult(networkResult);
        }
    }

    /* renamed from: com.netease.pharos.netlag.NetworkCheckProxy$3 */
    class AnonymousClass3 extends TimerTask {
        AnonymousClass3() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            NetworkCheckProxy.this.mNetworkCheck.exec();
        }
    }

    /* renamed from: com.netease.pharos.netlag.NetworkCheckProxy$4 */
    class AnonymousClass4 extends TimerTask {
        AnonymousClass4() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            LogUtil.stepLog("NetLag repeat:" + NetworkCheckProxy.this.mTaskRepeatCount.get());
            if (NetworkCheckProxy.this.mTaskRepeatCount.get() == 0) {
                if (NetworkCheckProxy.this.mTimer != null) {
                    NetworkCheckProxy.this.mTimer.cancel();
                }
            } else {
                LogUtil.stepLog("NetLag start:" + NetworkCheckProxy.this.mTaskRepeatCount.get());
                NetworkCheckProxy.this.mNetworkCheck.exec();
                NetworkCheckProxy.this.mTaskRepeatCount.decrementAndGet();
            }
        }
    }

    public void uploadResult(NetworkResult networkResult) {
        if (this.mUploadTask == null) {
            this.mUploadTask = new LogUploadTask();
        }
        this.mUploadTask.start();
        this.mUploadTask.add(networkResult.getResult());
    }

    public synchronized void cancel(OnNetLagCallback onNetLagCallback) {
        LogUtil.stepLog("NetLag cancel udp speed ");
        NetworkCheck networkCheck = this.mNetworkCheck;
        if (networkCheck != null) {
            networkCheck.cancel();
        }
        Timer timer = this.mTimer;
        if (timer != null) {
            timer.cancel();
            this.mTimer = null;
        }
        this.mNetworkCheck = null;
        NetworkResult networkResultCancel = NetworkResult.cancel();
        this.mPharosListener.onCancel(networkResultCancel.getResult());
        stopExecutorService();
        uploadResult(networkResultCancel);
    }

    public synchronized void stopExecutorService() {
        LogUtil.i("NetLag", "stopExecutorService");
        ScheduledExecutorService scheduledExecutorService = this.mExecutorService;
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdownNow();
        }
        LogUploadTask logUploadTask = this.mUploadTask;
        if (logUploadTask != null) {
            logUploadTask.finish();
        }
        this.mTaskIsRunning.set(false);
        this.mExecutorService = null;
    }
}