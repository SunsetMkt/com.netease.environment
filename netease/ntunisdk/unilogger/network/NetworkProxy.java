package com.netease.ntunisdk.unilogger.network;

import com.netease.ntunisdk.unilogger.utils.LogUtils;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/* loaded from: classes5.dex */
public class NetworkProxy {
    private static BlockingQueue<BaseRequest> uploadQueue = new ArrayBlockingQueue(20);
    private static boolean isStart = false;

    public static boolean addToUploadQueue(BaseRequest baseRequest) {
        try {
            BlockingQueue<BaseRequest> blockingQueue = uploadQueue;
            if (blockingQueue != null) {
                return blockingQueue.add(baseRequest);
            }
            return false;
        } catch (Exception e) {
            if (baseRequest.netCallback != null) {
                baseRequest.netCallback.onNetCallback(-1, "\u7f51\u7edc\u6a21\u5757\uff0c\u961f\u5217\u6ee1\uff0c\u65e0\u6cd5\u53d1\u8d77\u8be5\u8bf7\u6c42\uff0c\u518d\u7a0d\u5fae\u91cd\u65b0\u53d1\u8d77");
            }
            LogUtils.w_inner("NetworkProxy net [addToUploadQueue] Exception = " + e.toString());
            e.printStackTrace();
            return false;
        }
    }

    public static void init() {
        LogUtils.v_inner("NetworkProxy net [init] start, isStart=" + isStart);
        if (isStart) {
            return;
        }
        isStart = true;
        new Thread(new Runnable() { // from class: com.netease.ntunisdk.unilogger.network.NetworkProxy.1
            AnonymousClass1() {
            }

            @Override // java.lang.Runnable
            public void run() {
                while (true) {
                    try {
                        BaseRequest baseRequest = (BaseRequest) NetworkProxy.uploadQueue.take();
                        if (baseRequest == null) {
                            return;
                        } else {
                            baseRequest.exec();
                        }
                    } catch (Exception e) {
                        LogUtils.w_inner("NetProxy net [init] Exception = " + e.toString());
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }, "NetworkProxy").start();
    }

    /* renamed from: com.netease.ntunisdk.unilogger.network.NetworkProxy$1 */
    class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public void run() {
            while (true) {
                try {
                    BaseRequest baseRequest = (BaseRequest) NetworkProxy.uploadQueue.take();
                    if (baseRequest == null) {
                        return;
                    } else {
                        baseRequest.exec();
                    }
                } catch (Exception e) {
                    LogUtils.w_inner("NetProxy net [init] Exception = " + e.toString());
                    e.printStackTrace();
                    return;
                }
            }
        }
    }
}