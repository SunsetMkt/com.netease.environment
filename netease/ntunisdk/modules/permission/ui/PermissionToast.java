package com.netease.ntunisdk.modules.permission.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.netease.ntunisdk.modules.permission.R;

/* loaded from: classes.dex */
public class PermissionToast {
    private static final String TAG = "PermissionToast";

    public static void show(Context context, String str, int i) {
        Log.d(TAG, "show: start");
        View viewInflate = LayoutInflater.from(context).inflate(R.layout.netease_permissionkit_sdk__toast_layout, (ViewGroup) null);
        ((TextView) viewInflate.findViewById(R.id.netease_permissionkit_sdk__toast_content)).setText(str);
        Toast toast = new Toast(context);
        toast.setGravity(17, 0, 0);
        toast.setDuration(i);
        toast.setView(viewInflate);
        toast.show();
        Log.d(TAG, "show: success");
    }
}