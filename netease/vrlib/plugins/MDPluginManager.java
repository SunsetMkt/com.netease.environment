package com.netease.vrlib.plugins;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: classes5.dex */
public class MDPluginManager {
    private static final String TAG = "MDPluginManager";
    private List<MDAbsPlugin> mList = new CopyOnWriteArrayList();

    public void add(MDAbsPlugin mDAbsPlugin) {
        this.mList.add(mDAbsPlugin);
    }

    public List<MDAbsPlugin> getPlugins() {
        return this.mList;
    }

    public void remove(MDAbsPlugin mDAbsPlugin) {
        if (mDAbsPlugin != null) {
            this.mList.remove(mDAbsPlugin);
        }
    }

    public void removeAll() {
        for (MDAbsPlugin mDAbsPlugin : this.mList) {
            if (mDAbsPlugin.removable()) {
                this.mList.remove(mDAbsPlugin);
            }
        }
    }
}