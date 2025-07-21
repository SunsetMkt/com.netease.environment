package com.netease.ntunisdk.external.protocol.plugins;

import android.content.Context;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class PluginManager {
    private static PluginManager sInstance;
    private Plugin douyinPlugin;
    private boolean hasInit = false;
    private final ConcurrentHashMap<String, Plugin> mPluginMap = new ConcurrentHashMap<>();

    private PluginManager() {
    }

    public static PluginManager getInstance() {
        if (sInstance == null) {
            synchronized (PluginManager.class) {
                if (sInstance == null) {
                    sInstance = new PluginManager();
                }
            }
        }
        return sInstance;
    }

    public synchronized void init(Context context) {
        if (this.hasInit) {
            return;
        }
        this.douyinPlugin = new DefaultDouYinCloudGamePlugin();
        this.hasInit = true;
    }

    public void exit(Context context, Callback callback) {
        init(context);
        Plugin plugin = this.douyinPlugin;
        if (plugin != null && plugin.isSupport()) {
            this.douyinPlugin.exit(context, callback);
        } else if (callback != null) {
            callback.onFinish(new Result(new JSONObject()));
        }
    }

    public void registerPlugin(String str, Plugin plugin) {
        this.mPluginMap.put(str, plugin);
    }

    public void unRegisterPlugin(String str) {
        this.mPluginMap.remove(str);
    }
}