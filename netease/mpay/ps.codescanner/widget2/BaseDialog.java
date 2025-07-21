package com.netease.mpay.ps.codescanner.widget2;

import android.app.Dialog;
import android.content.Context;
import com.netease.mpay.ps.codescanner.utils.Logging;
import com.netease.mpay.ps.codescanner.utils.ViewUtils;

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
            } else {
                ViewUtils.setDialogUnFocusable(getWindow());
                ViewUtils.setNavigationBar(getContext(), getWindow());
                super.show();
                ViewUtils.clearDialogUnFocusable(getWindow());
            }
        } catch (IllegalArgumentException e) {
            Logging.logStackTrace(e);
        } catch (Exception e2) {
            Logging.logStackTrace(e2);
        }
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        if (isShowing()) {
            try {
                super.dismiss();
            } catch (IllegalArgumentException e) {
                Logging.logStackTrace(e);
            } catch (Exception e2) {
                Logging.logStackTrace(e2);
            }
        }
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (!z || isNeedShowingNavigationBar()) {
            return;
        }
        ViewUtils.setNavigationBar(getContext(), getWindow());
    }
}