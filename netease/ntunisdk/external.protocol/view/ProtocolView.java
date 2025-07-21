package com.netease.ntunisdk.external.protocol.view;

import android.content.Context;
import android.graphics.Canvas;
import android.text.StaticLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.netease.ntunisdk.external.protocol.utils.L;

/* loaded from: classes.dex */
public class ProtocolView extends View implements View.OnTouchListener {
    private static final int MIN_MOVE_SIZE = 80;
    private static final String TAG = "V";
    private boolean isRTL;
    private OnPageListener mOnPageListener;
    private StaticLayout mPLayout;
    private int mPLayoutHeight;
    private float mTouchX;
    private float mTouchY;

    public interface OnPageListener {
        void nextPage();

        void openLink(int i);

        void prePage();
    }

    public ProtocolView(Context context) {
        super(context);
        this.isRTL = false;
        init();
    }

    public ProtocolView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.isRTL = false;
        init();
    }

    public ProtocolView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.isRTL = false;
        init();
    }

    public void setRTL(boolean z) {
        this.isRTL = z;
    }

    private void init() {
        setOnTouchListener(this);
    }

    public void addOnPageListener(OnPageListener onPageListener) {
        this.mOnPageListener = onPageListener;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        StaticLayout staticLayout = this.mPLayout;
        if (staticLayout != null) {
            staticLayout.draw(canvas, null, null, 0);
        }
        canvas.restore();
    }

    public StaticLayout getLayout() {
        return this.mPLayout;
    }

    public void setLayout(StaticLayout staticLayout) {
        this.mPLayout = staticLayout;
        this.mPLayoutHeight = staticLayout.getHeight();
        postInvalidate();
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        OnPageListener onPageListener;
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mTouchX = motionEvent.getRawX();
            this.mTouchY = motionEvent.getRawY();
        } else if (action == 1) {
            float rawX = motionEvent.getRawX() - this.mTouchX;
            float rawY = motionEvent.getRawY() - this.mTouchY;
            boolean z = Math.abs(rawX) > Math.abs(rawY);
            if (!z) {
                rawX = rawY;
            }
            if (Math.abs(rawX) < 80.0f) {
                int iFindTouchCharAt = findTouchCharAt((int) motionEvent.getX(), (int) motionEvent.getY());
                if (iFindTouchCharAt >= 0 && (onPageListener = this.mOnPageListener) != null) {
                    onPageListener.openLink(iFindTouchCharAt);
                }
            } else if (rawX > 0.0f) {
                OnPageListener onPageListener2 = this.mOnPageListener;
                if (onPageListener2 != null) {
                    if (this.isRTL && z) {
                        onPageListener2.nextPage();
                    } else {
                        onPageListener2.prePage();
                    }
                }
            } else {
                OnPageListener onPageListener3 = this.mOnPageListener;
                if (onPageListener3 != null) {
                    if (this.isRTL && z) {
                        onPageListener3.prePage();
                    } else {
                        onPageListener3.nextPage();
                    }
                }
            }
        }
        return true;
    }

    private int findTouchCharAt(int i, int i2) {
        L.d("V", String.format("Touch : [%d,%d]", Integer.valueOf(i), Integer.valueOf(i2)));
        if (i2 >= this.mPLayoutHeight) {
            return -1;
        }
        int lineForVertical = this.mPLayout.getLineForVertical(i2);
        L.d("V", "line : " + lineForVertical);
        int offsetForHorizontal = this.mPLayout.getOffsetForHorizontal(lineForVertical, (float) i);
        L.d("V", "off : " + offsetForHorizontal);
        CharSequence text = this.mPLayout.getText();
        if (offsetForHorizontal >= text.length()) {
            return -1;
        }
        L.d("V", "click   :  " + text.charAt(offsetForHorizontal));
        return offsetForHorizontal;
    }
}