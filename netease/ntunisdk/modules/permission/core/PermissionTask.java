package com.netease.ntunisdk.modules.permission.core;

/* loaded from: classes.dex */
public class PermissionTask {
    private final String key;
    private final PermissionHandler value;

    public PermissionTask(String str, PermissionHandler permissionHandler) {
        this.key = str;
        this.value = permissionHandler;
    }

    public String getKey() {
        return this.key;
    }

    public PermissionHandler getValue() {
        return this.value;
    }
}