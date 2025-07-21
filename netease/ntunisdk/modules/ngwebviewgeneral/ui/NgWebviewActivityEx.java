package com.netease.ntunisdk.modules.ngwebviewgeneral.ui;

import android.os.Process;

/* loaded from: classes.dex */
public class NgWebviewActivityEx extends NgWebviewActivity {
    @Override // com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        Process.killProcess(Process.myPid());
    }
}