package com.netease.mpay.auth.plugins;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: classes.dex */
public class PluginExecutor implements Lifecycle {
    private static PluginExecutor sInstance;
    private Task mActive;
    private PluginResult mPrePluginResult;
    Bundle mSavedInstanceState;
    final ArrayDeque<Task> mTasks = new ArrayDeque<>();
    final CopyOnWriteArrayList<LifeCyclePlugin> mLifecycles = new CopyOnWriteArrayList<>();
    final CopyOnWriteArrayList<Runnable> mRunOnResumeTasks = new CopyOnWriteArrayList<>();
    int mLifecycleState = 0;

    private PluginExecutor() {
    }

    public static PluginExecutor getInstance() {
        if (sInstance == null) {
            synchronized (PluginExecutor.class) {
                if (sInstance == null) {
                    sInstance = new PluginExecutor();
                }
            }
        }
        return sInstance;
    }

    public synchronized void reset() {
        this.mActive = null;
        this.mTasks.clear();
        this.mLifecycles.clear();
        this.mRunOnResumeTasks.clear();
    }

    public synchronized void execute(Plugin plugin) {
        if (plugin == null) {
            return;
        }
        this.mTasks.offer(new Task(plugin, new PluginCallbackWrapper(plugin.getCallback()) { // from class: com.netease.mpay.auth.plugins.PluginExecutor.1
            @Override // com.netease.mpay.auth.plugins.PluginExecutor.PluginCallbackWrapper, com.netease.mpay.auth.plugins.PluginCallback
            public void onFinish(PluginResult pluginResult) {
                PluginExecutor.this.mPrePluginResult = pluginResult;
                super.onFinish(pluginResult);
                PluginExecutor.this.scheduleNext(pluginResult);
            }
        }));
        if (this.mActive == null) {
            scheduleNext(this.mPrePluginResult);
        }
        if (plugin instanceof LifeCyclePlugin) {
            this.mLifecycles.add((LifeCyclePlugin) plugin);
            if (this.mLifecycleState < 4 && this.mLifecycleState >= 1) {
                dispatchLifecycleEvent(this.mLifecycleState, this.mSavedInstanceState);
            }
        }
    }

    public synchronized void postRunOnResume(Runnable runnable) {
        if (runnable == null) {
            return;
        }
        this.mRunOnResumeTasks.add(runnable);
        if (this.mLifecycleState == 3) {
            dispatchOnResumeTask();
        }
    }

    public void dispatchOnResumeTask() {
        new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.netease.mpay.auth.plugins.PluginExecutor.2
            @Override // java.lang.Runnable
            public void run() {
                Iterator<Runnable> it = PluginExecutor.this.mRunOnResumeTasks.iterator();
                while (it.hasNext()) {
                    it.next().run();
                }
                PluginExecutor.this.mRunOnResumeTasks.clear();
            }
        });
    }

    public static class Task implements Runnable {
        private Plugin mPlugin;
        private PluginCallbackWrapper mPluginCallbackWrapper;

        public Task(Plugin plugin, PluginCallbackWrapper pluginCallbackWrapper) {
            this.mPlugin = plugin;
            this.mPluginCallbackWrapper = pluginCallbackWrapper;
        }

        public void execute(PluginResult pluginResult) {
            Log.d("Task", "execute:" + this.mPlugin.getName());
            this.mPlugin.addPreTaskResult(pluginResult);
            run();
        }

        @Override // java.lang.Runnable
        public void run() {
            this.mPlugin.addCallback(this.mPluginCallbackWrapper).execute();
        }
    }

    public static class PluginCallbackWrapper implements PluginCallback {
        private PluginCallback mCallback;

        public PluginCallbackWrapper(PluginCallback pluginCallback) {
            this.mCallback = pluginCallback;
        }

        @Override // com.netease.mpay.auth.plugins.PluginCallback
        public void onFinish(PluginResult pluginResult) {
            Log.d("PluginCallbackWrapper", "result:" + pluginResult.data.toString());
            PluginCallback pluginCallback = this.mCallback;
            if (pluginCallback != null) {
                pluginCallback.onFinish(pluginResult);
            }
        }
    }

    protected synchronized void scheduleNext(PluginResult pluginResult) {
        Task taskPoll = this.mTasks.poll();
        this.mActive = taskPoll;
        if (taskPoll != null) {
            if (this.mActive.mPlugin.isNeedSuccessBeforeExecute() && pluginResult != null && !pluginResult.isSuccess()) {
                this.mActive = null;
                this.mTasks.clear();
                this.mPrePluginResult = null;
            } else {
                if (pluginResult == null) {
                    if (this.mPrePluginResult == null) {
                        this.mPrePluginResult = PluginResult.newInstance(PluginResult.RESULT_SUCCESS, this.mActive.mPlugin);
                    }
                    pluginResult = this.mPrePluginResult;
                }
                this.mActive.execute(pluginResult);
            }
        }
    }

    @Override // com.netease.mpay.auth.plugins.Lifecycle
    public void onCreate(Bundle bundle) {
        if (this.mLifecycleState == 1) {
            return;
        }
        this.mSavedInstanceState = bundle;
        this.mLifecycleState = 1;
        dispatchLifecycleEvent(this.mLifecycleState, this.mSavedInstanceState);
    }

    @Override // com.netease.mpay.auth.plugins.Lifecycle
    public void onRestart() {
        Iterator<LifeCyclePlugin> it = this.mLifecycles.iterator();
        while (it.hasNext()) {
            it.next().onRestart();
        }
    }

    @Override // com.netease.mpay.auth.plugins.Lifecycle
    public void onStart() {
        this.mLifecycleState = 2;
        dispatchLifecycleEvent(this.mLifecycleState, this.mSavedInstanceState);
    }

    @Override // com.netease.mpay.auth.plugins.Lifecycle
    public void onResume() {
        this.mLifecycleState = 3;
        dispatchLifecycleEvent(this.mLifecycleState, this.mSavedInstanceState);
        dispatchOnResumeTask();
    }

    @Override // com.netease.mpay.auth.plugins.Lifecycle
    public void onPause() {
        this.mLifecycleState = 4;
        dispatchLifecycleEvent(this.mLifecycleState, this.mSavedInstanceState);
    }

    @Override // com.netease.mpay.auth.plugins.Lifecycle
    public void onStop() {
        this.mLifecycleState = 5;
        dispatchLifecycleEvent(this.mLifecycleState, this.mSavedInstanceState);
    }

    @Override // com.netease.mpay.auth.plugins.Lifecycle
    public void onDestroy() {
        this.mLifecycleState = 6;
        dispatchLifecycleEvent(this.mLifecycleState, this.mSavedInstanceState);
    }

    @Override // com.netease.mpay.auth.plugins.Lifecycle
    public void onNewIntent(Intent intent) {
        Iterator<LifeCyclePlugin> it = this.mLifecycles.iterator();
        while (it.hasNext()) {
            it.next().onNewIntent(intent);
        }
    }

    @Override // com.netease.mpay.auth.plugins.Lifecycle
    public void onConfigurationChanged(Configuration configuration) {
        Iterator<LifeCyclePlugin> it = this.mLifecycles.iterator();
        while (it.hasNext()) {
            it.next().onConfigurationChanged(configuration);
        }
    }

    @Override // com.netease.mpay.auth.plugins.Lifecycle
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        Iterator<LifeCyclePlugin> it = this.mLifecycles.iterator();
        while (it.hasNext()) {
            it.next().onRequestPermissionsResult(i, strArr, iArr);
        }
    }

    @Override // com.netease.mpay.auth.plugins.Lifecycle
    public void onActivityResult(int i, int i2, Intent intent) {
        Iterator<LifeCyclePlugin> it = this.mLifecycles.iterator();
        while (it.hasNext()) {
            it.next().onActivityResult(i, i2, intent);
        }
    }

    private void dispatchLifecycleEvent(int i, Bundle bundle) {
        Iterator<LifeCyclePlugin> it = this.mLifecycles.iterator();
        while (it.hasNext()) {
            executeLifecycleEvent(i, it.next(), bundle);
        }
    }

    private void executeLifecycleEvent(int i, LifeCyclePlugin lifeCyclePlugin, Bundle bundle) {
        if (i < 1 || lifeCyclePlugin == null) {
            return;
        }
        int lifecycleState = lifeCyclePlugin.getLifecycleState();
        while (lifecycleState < i) {
            lifecycleState++;
            switch (lifecycleState) {
                case 1:
                    lifeCyclePlugin.onCreate(bundle);
                    break;
                case 2:
                    lifeCyclePlugin.onStart();
                    break;
                case 3:
                    lifeCyclePlugin.onResume();
                    break;
                case 4:
                    lifeCyclePlugin.onPause();
                    break;
                case 5:
                    lifeCyclePlugin.onStop();
                    break;
                case 6:
                    lifeCyclePlugin.onDestroy();
                    break;
            }
        }
    }
}