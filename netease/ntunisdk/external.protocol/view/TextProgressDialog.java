package com.netease.ntunisdk.external.protocol.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import com.netease.ntunisdk.external.protocol.utils.SysHelper;
import com.netease.ntunisdk.protocollib.R;

/* loaded from: classes.dex */
public class TextProgressDialog extends Dialog implements Progress {
    public TextProgressDialog(Context context) {
        super(context);
    }

    public static TextProgressDialog newInstance(Context context, boolean z) {
        TextProgressDialog textProgressDialog = new TextProgressDialog(context);
        textProgressDialog.setCancelable(z);
        return textProgressDialog;
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(0));
            window.requestFeature(1);
            SysHelper.hideSystemUI(window);
        }
        setContentView(((Activity) ((ContextWrapper) getContext()).getBaseContext()).getLayoutInflater().inflate(R.layout.unisdk_protocol_loading, (ViewGroup) null, false), new ViewGroup.LayoutParams(-1, -1));
    }

    @Override // android.app.Dialog
    public void show() {
        SysHelper.clearDialogFocusable(getWindow());
        super.show();
        SysHelper.hideSystemUI(getWindow());
        SysHelper.resetDialogFocusable(getWindow());
    }

    @Override // com.netease.ntunisdk.external.protocol.view.Progress
    public void showProgress() {
        if (isShowing()) {
            return;
        }
        try {
            show();
        } catch (IllegalArgumentException | Exception unused) {
        }
    }

    @Override // com.netease.ntunisdk.external.protocol.view.Progress
    public void dismissProgress() {
        if (isShowing()) {
            try {
                dismiss();
            } catch (IllegalArgumentException | Exception unused) {
            }
        }
    }

    @Override // com.netease.ntunisdk.external.protocol.view.Progress
    public boolean isProgressShowing() {
        return isShowing();
    }
}