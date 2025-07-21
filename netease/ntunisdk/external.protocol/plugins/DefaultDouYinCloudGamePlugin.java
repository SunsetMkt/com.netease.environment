package com.netease.ntunisdk.external.protocol.plugins;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import com.bytedance.cloudplay.gamesdk.api.ByteCloudGameSdk;
import com.bytedance.cloudplay.gamesdk.api.PluginListener;
import com.bytedance.cloudplay.gamesdk.api.base.InitCallBack;
import com.bytedance.cloudplay.gamesdk.api.scene.Scene;
import com.netease.ntunisdk.external.protocol.view.ProgressImpl;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class DefaultDouYinCloudGamePlugin implements Plugin {
    private boolean hasInit = false;
    private final boolean hasCheck = false;
    private boolean isSupport = false;
    private boolean isRunningCloud = false;

    @Override // com.netease.ntunisdk.external.protocol.plugins.Plugin
    public String getChannel() {
        return "douyin_cloudgame";
    }

    @Override // com.netease.ntunisdk.external.protocol.plugins.Plugin
    public synchronized boolean isSupport() {
        try {
            Class.forName("com.netease.ntunisdk.SdkDouYinCloud");
            Class.forName("com.bytedance.cloudplay.gamesdk.api.ByteCloudGameSdk");
            this.isSupport = (TextUtils.isEmpty(Const.DOUYIN_CLOUD_GAME_APP_CHANNEL) || TextUtils.isEmpty("")) ? false : true;
        } catch (Throwable unused) {
            this.isSupport = false;
        }
        return this.isSupport;
    }

    @Override // com.netease.ntunisdk.external.protocol.plugins.Plugin
    public boolean isSupportRunning() {
        return this.isRunningCloud;
    }

    @Override // com.netease.ntunisdk.external.protocol.plugins.Plugin
    public synchronized void init(final Context context, final Callback callback) {
        isSupport();
        if (this.isSupport) {
            ByteCloudGameSdk.initPlugin(context);
            if (ByteCloudGameSdk.isPluginLoaded()) {
                ByteCloudGameSdk.inst().init(context, "", Const.DOUYIN_CLOUD_GAME_APP_CHANNEL, new InitCallBack() { // from class: com.netease.ntunisdk.external.protocol.plugins.DefaultDouYinCloudGamePlugin.1
                    public void onResult(com.bytedance.cloudplay.gamesdk.api.base.Result result) {
                        DefaultDouYinCloudGamePlugin.this.isRunningCloud = ByteCloudGameSdk.inst().isRunningCloud();
                        DefaultDouYinCloudGamePlugin.this.hasInit = true;
                        Callback callback2 = callback;
                        if (callback2 != null) {
                            callback2.onFinish(new Result(new JSONObject()));
                        }
                    }
                });
            } else {
                ByteCloudGameSdk.loadPlugin(new PluginListener() { // from class: com.netease.ntunisdk.external.protocol.plugins.DefaultDouYinCloudGamePlugin.2
                    public void onLoadSuccess() {
                        ByteCloudGameSdk.inst().init(context, "", Const.DOUYIN_CLOUD_GAME_APP_CHANNEL, new InitCallBack() { // from class: com.netease.ntunisdk.external.protocol.plugins.DefaultDouYinCloudGamePlugin.2.1
                            public void onResult(com.bytedance.cloudplay.gamesdk.api.base.Result result) {
                                DefaultDouYinCloudGamePlugin.this.isRunningCloud = ByteCloudGameSdk.inst().isRunningCloud();
                                DefaultDouYinCloudGamePlugin.this.hasInit = true;
                                if (callback != null) {
                                    callback.onFinish(new Result(new JSONObject()));
                                }
                            }
                        });
                    }

                    public void onLoadFailed() {
                        Callback callback2 = callback;
                        if (callback2 != null) {
                            callback2.onFinish(new Result(new JSONObject()));
                        }
                    }
                });
            }
        }
    }

    @Override // com.netease.ntunisdk.external.protocol.plugins.Plugin
    public void exit(Context context, final Callback callback) {
        if (!this.isSupport) {
            if (callback != null) {
                callback.onFinish(new Result(1, "not exist!"));
            }
        } else {
            ProgressImpl.getInstance().showProgress((Activity) context);
            if (this.hasInit) {
                ByteCloudGameSdk.inst().exitCloud(new Scene.CallBack<com.bytedance.cloudplay.gamesdk.api.base.Result>() { // from class: com.netease.ntunisdk.external.protocol.plugins.DefaultDouYinCloudGamePlugin.3
                    public void onResult(com.bytedance.cloudplay.gamesdk.api.base.Result result) {
                        ProgressImpl.getInstance().dismissProgress();
                        if (result.isSuccess()) {
                            Callback callback2 = callback;
                            if (callback2 != null) {
                                callback2.onFinish(new Result(new JSONObject()));
                                return;
                            }
                            return;
                        }
                        int i = result.code;
                        String str = result.message;
                        Callback callback3 = callback;
                        if (callback3 != null) {
                            callback3.onFinish(new Result(i, str));
                        }
                    }
                });
            } else {
                init(context, new Callback() { // from class: com.netease.ntunisdk.external.protocol.plugins.DefaultDouYinCloudGamePlugin.4
                    @Override // com.netease.ntunisdk.external.protocol.plugins.Callback
                    public void onFinish(Result result) {
                        ProgressImpl.getInstance().dismissProgress();
                        if (!result.isSuccess() || !DefaultDouYinCloudGamePlugin.this.isRunningCloud) {
                            Callback callback2 = callback;
                            if (callback2 != null) {
                                callback2.onFinish(new Result(1, "not exist!"));
                                return;
                            }
                            return;
                        }
                        ByteCloudGameSdk.inst().exitCloud(new Scene.CallBack<com.bytedance.cloudplay.gamesdk.api.base.Result>() { // from class: com.netease.ntunisdk.external.protocol.plugins.DefaultDouYinCloudGamePlugin.4.1
                            public void onResult(com.bytedance.cloudplay.gamesdk.api.base.Result result2) {
                                ProgressImpl.getInstance().dismissProgress();
                                if (result2.isSuccess()) {
                                    if (callback != null) {
                                        callback.onFinish(new Result(new JSONObject()));
                                    }
                                } else {
                                    int i = result2.code;
                                    String str = result2.message;
                                    if (callback != null) {
                                        callback.onFinish(new Result(i, str));
                                    }
                                }
                            }
                        });
                    }
                });
            }
        }
    }

    @Override // com.netease.ntunisdk.external.protocol.plugins.Plugin
    public boolean hasInit() {
        return this.hasInit;
    }
}