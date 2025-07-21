package com.netease.ntunisdk.modules.ngwebviewgeneral.ui;

import android.R;
import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.NgWebviewActivity;

/* loaded from: classes.dex */
public class AndroidBug5497Workaround {
    private FrameLayout.LayoutParams frameLayoutParams;
    private View mChildOfContent;
    private NgWebviewActivity.KeyboardListener mKeyboardListener;
    private int usableHeightPrevious;

    public static void assistActivity(Activity activity, NgWebviewActivity.KeyboardListener keyboardListener) {
        new AndroidBug5497Workaround(activity, keyboardListener);
    }

    private AndroidBug5497Workaround(Activity activity, NgWebviewActivity.KeyboardListener keyboardListener) {
        this.mKeyboardListener = keyboardListener;
        this.mChildOfContent = ((FrameLayout) activity.findViewById(R.id.content)).getChildAt(0);
        this.mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.AndroidBug5497Workaround.1
            AnonymousClass1() {
            }

            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                AndroidBug5497Workaround.this.possiblyResizeChildOfContent();
            }
        });
        this.frameLayoutParams = (FrameLayout.LayoutParams) this.mChildOfContent.getLayoutParams();
    }

    /* renamed from: com.netease.ntunisdk.modules.ngwebviewgeneral.ui.AndroidBug5497Workaround$1 */
    class AnonymousClass1 implements ViewTreeObserver.OnGlobalLayoutListener {
        AnonymousClass1() {
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            AndroidBug5497Workaround.this.possiblyResizeChildOfContent();
        }
    }

    public void possiblyResizeChildOfContent() {
        int iComputeUsableHeight = computeUsableHeight();
        if (iComputeUsableHeight != this.usableHeightPrevious) {
            int height = this.mChildOfContent.getRootView().getHeight();
            int i = height - iComputeUsableHeight;
            if (i > height / 4) {
                this.frameLayoutParams.height = height - i;
                this.mKeyboardListener.up();
            } else {
                this.frameLayoutParams.height = -1;
                this.mKeyboardListener.down();
            }
            this.mChildOfContent.requestLayout();
            this.usableHeightPrevious = iComputeUsableHeight;
        }
    }

    private int computeUsableHeight() {
        Rect rect = new Rect();
        this.mChildOfContent.getWindowVisibleDisplayFrame(rect);
        return rect.bottom - rect.top;
    }
}