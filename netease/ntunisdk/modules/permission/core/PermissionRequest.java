package com.netease.ntunisdk.modules.permission.core;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import com.netease.ntunisdk.modules.base.utils.LogModule;
import com.netease.ntunisdk.modules.permission.PermissionContext;
import com.netease.ntunisdk.modules.permission.common.PermissionConstant;
import com.netease.ntunisdk.modules.permission.ui.PermissionFragment;
import com.netease.ntunisdk.modules.permission.utils.PermissionUtils;
import com.netease.ntunisdk.modules.personalinfolist.HookManager;
import java.lang.ref.WeakReference;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class PermissionRequest {
    private static final String TAG = "PermissionRequest";
    private final String[] PERMISSION_NEED;
    private final JSONObject json;
    private final PermissionListener permissionListener;
    private WeakReference<Activity> weakTarget;

    public PermissionRequest(JSONObject jSONObject, String[] strArr, PermissionContext permissionContext, PermissionListener permissionListener) {
        this.weakTarget = null;
        this.json = jSONObject;
        this.PERMISSION_NEED = strArr;
        this.permissionListener = permissionListener;
        try {
            if (permissionContext == null) {
                LogModule.d(TAG, "PermissionRequest target is null ");
                return;
            }
            Context context = permissionContext.getContext();
            if (context == null) {
                LogModule.d(TAG, "PermissionRequest context is null ");
                return;
            }
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                if (Build.VERSION.SDK_INT >= 17) {
                    if (activity.isDestroyed()) {
                        LogModule.d(TAG, "PermissionRequest activity has been destroy ");
                        return;
                    } else {
                        this.weakTarget = new WeakReference<>(activity);
                        return;
                    }
                }
                return;
            }
            LogModule.d(TAG, "PermissionRequest context is not activity ");
        } catch (Exception e) {
            LogModule.d(TAG, "PermissionRequest Exception " + e.getMessage());
        }
    }

    public Activity getActivity() {
        return this.weakTarget.get();
    }

    public void proceed(String str, Fragment fragment) {
        Activity activity;
        WeakReference<Activity> weakReference = this.weakTarget;
        if (weakReference != null) {
            activity = weakReference.get();
        } else {
            LogModule.d(TAG, "proceed weakTarget is null ");
            activity = null;
        }
        if (Build.VERSION.SDK_INT >= 17) {
            if (activity == null || activity.isDestroyed()) {
                LogModule.d(TAG, "proceed activity is null or activity is destroy");
                return;
            }
        } else if (activity == null) {
            LogModule.d(TAG, "proceed activity is null");
            return;
        }
        String[] strArr = this.PERMISSION_NEED;
        if (strArr == null || strArr.length < 1) {
            return;
        }
        if (strArr.length == 1 && (PermissionConstant.PERMISSION_WRITE_SETTINGS.equals(strArr[0]) || PermissionConstant.PERMISSION_SYSTEM_ALERT_WINDOW.equals(this.PERMISSION_NEED[0]))) {
            if (PermissionConstant.PERMISSION_WRITE_SETTINGS.equals(this.PERMISSION_NEED[0])) {
                if (Build.VERSION.SDK_INT >= 23) {
                    HookManager.startActivityForResult(activity, new Intent("android.settings.action.MANAGE_WRITE_SETTINGS", Uri.parse("package:" + activity.getPackageName())), PermissionConstant.REQUEST_CODE);
                    LogModule.i(TAG, "proceed: " + PermissionConstant.PERMISSION_WRITE_SETTINGS);
                    return;
                }
                return;
            }
            if (Build.VERSION.SDK_INT >= 23) {
                HookManager.startActivityForResult(activity, new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + activity.getPackageName())), PermissionConstant.REQUEST_CODE);
                LogModule.i(TAG, "proceed: " + PermissionConstant.PERMISSION_SYSTEM_ALERT_WINDOW);
                return;
            }
            return;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            fragment.requestPermissions(this.PERMISSION_NEED, PermissionConstant.REQUEST_CODE);
        }
        LogModule.i(TAG, "proceed: requestPermissions");
    }

    public void refuse(int[] iArr, PermissionHandler permissionHandler, PermissionFragment permissionFragment) {
        onPermissionResult(iArr, permissionHandler, permissionFragment);
    }

    public void onPermissionResult(int[] iArr, PermissionHandler permissionHandler, PermissionFragment permissionFragment) {
        if (this.permissionListener == null || iArr == null) {
            return;
        }
        LogModule.i(TAG, "onPermissionResult");
        this.permissionListener.onPermissionResult(PermissionUtils.arrayToString(this.PERMISSION_NEED), iArr, permissionHandler, permissionFragment);
    }
}