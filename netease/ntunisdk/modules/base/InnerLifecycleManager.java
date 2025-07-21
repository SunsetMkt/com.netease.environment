package com.netease.ntunisdk.modules.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: classes.dex */
public abstract class InnerLifecycleManager {
    protected Context context;
    protected HashMap<String, BaseModules> modulesMap = new HashMap<>();

    public void onCreate(Bundle bundle) {
        Iterator<String> it = this.modulesMap.keySet().iterator();
        while (it.hasNext()) {
            this.modulesMap.get(it.next()).onCreate(bundle);
        }
    }

    public void onNewIntent(Intent intent) {
        Iterator<String> it = this.modulesMap.keySet().iterator();
        while (it.hasNext()) {
            this.modulesMap.get(it.next()).onNewIntent(intent);
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        Iterator<String> it = this.modulesMap.keySet().iterator();
        while (it.hasNext()) {
            this.modulesMap.get(it.next()).onSaveInstanceState(bundle);
        }
    }

    public void onRestoreInstanceState(Bundle bundle) {
        Iterator<String> it = this.modulesMap.keySet().iterator();
        while (it.hasNext()) {
            this.modulesMap.get(it.next()).onRestoreInstanceState(bundle);
        }
    }

    public void onRestoreInstanceState(Bundle bundle, PersistableBundle persistableBundle) {
        Iterator<String> it = this.modulesMap.keySet().iterator();
        while (it.hasNext()) {
            this.modulesMap.get(it.next()).onRestoreInstanceState(bundle, persistableBundle);
        }
    }

    public void onPause() {
        Iterator<String> it = this.modulesMap.keySet().iterator();
        while (it.hasNext()) {
            this.modulesMap.get(it.next()).onPause();
        }
    }

    public void onStart() {
        Iterator<String> it = this.modulesMap.keySet().iterator();
        while (it.hasNext()) {
            this.modulesMap.get(it.next()).onStart();
        }
    }

    public void onStop() {
        Iterator<String> it = this.modulesMap.keySet().iterator();
        while (it.hasNext()) {
            this.modulesMap.get(it.next()).onStop();
        }
    }

    public void onResume() {
        Iterator<String> it = this.modulesMap.keySet().iterator();
        while (it.hasNext()) {
            this.modulesMap.get(it.next()).onResume();
        }
    }

    public void onRestart() {
        Iterator<String> it = this.modulesMap.keySet().iterator();
        while (it.hasNext()) {
            this.modulesMap.get(it.next()).onRestart();
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        Iterator<String> it = this.modulesMap.keySet().iterator();
        while (it.hasNext()) {
            this.modulesMap.get(it.next()).onActivityResult(i, i2, intent);
        }
    }

    public void onWindowFocusChanged(boolean z) {
        Iterator<String> it = this.modulesMap.keySet().iterator();
        while (it.hasNext()) {
            this.modulesMap.get(it.next()).onWindowFocusChanged(z);
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        Iterator<String> it = this.modulesMap.keySet().iterator();
        while (it.hasNext()) {
            this.modulesMap.get(it.next()).onConfigurationChanged(configuration);
        }
    }

    public void onBackPressed() {
        Iterator<String> it = this.modulesMap.keySet().iterator();
        while (it.hasNext()) {
            this.modulesMap.get(it.next()).onBackPressed();
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        Iterator<String> it = this.modulesMap.keySet().iterator();
        while (it.hasNext()) {
            this.modulesMap.get(it.next()).onRequestPermissionsResult(i, strArr, iArr);
        }
    }

    public void onUserLeaveHint() {
        Iterator<String> it = this.modulesMap.keySet().iterator();
        while (it.hasNext()) {
            this.modulesMap.get(it.next()).onUserLeaveHint();
        }
    }

    public void onLowMemory() {
        Iterator<String> it = this.modulesMap.keySet().iterator();
        while (it.hasNext()) {
            this.modulesMap.get(it.next()).onLowMemory();
        }
    }

    public void onDestroy() {
        Iterator<String> it = this.modulesMap.keySet().iterator();
        while (it.hasNext()) {
            this.modulesMap.get(it.next()).onDestroy();
        }
    }
}