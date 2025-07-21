package com.netease.ntunisdk.base.view;

import android.app.Dialog;
import android.content.Context;

/* loaded from: classes5.dex */
public class BaseDialog extends Dialog {
    public boolean isNeedShowingNavigationBar() {
        return false;
    }

    public BaseDialog(Context context) {
        super(context);
    }

    public BaseDialog(Context context, int i) {
        super(context, i);
    }

    @Override // android.app.Dialog
    public void show() {
        if (isShowing()) {
            return;
        }
        try {
            if (isNeedShowingNavigationBar()) {
                super.show();
                return;
            }
            ViewUtils.setDialogUnFocusable(getWindow());
            a.a(getContext(), getWindow());
            super.show();
            ViewUtils.clearDialogUnFocusable(getWindow());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        if (isShowing()) {
            try {
                super.dismiss();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (!z || isNeedShowingNavigationBar()) {
            return;
        }
        a.a(getContext(), getWindow());
    }
}