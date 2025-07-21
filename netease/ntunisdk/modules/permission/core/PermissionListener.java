package com.netease.ntunisdk.modules.permission.core;

import com.netease.ntunisdk.modules.permission.ui.PermissionFragment;

/* loaded from: classes.dex */
public interface PermissionListener {
    void onPermissionResult(String str, int[] iArr, PermissionHandler permissionHandler, PermissionFragment permissionFragment);
}