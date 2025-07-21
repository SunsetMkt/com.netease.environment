package com.netease.ntunisdk.modules.ngwebviewgeneral.ui;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import com.netease.ntunisdk.modules.api.ModulesManager;

/* loaded from: classes.dex */
public class FloatWindow {
    private Button button;
    private int i = 0;
    private Context mContext;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowParams;

    public FloatWindow(Context context) {
        this.mContext = context;
        this.mWindowManager = (WindowManager) context.getSystemService("window");
        initFloatingWindow();
    }

    public Context getContext() {
        return this.mContext;
    }

    public void initFloatingWindow() {
        this.button = new Button(this.mContext);
        this.button.setText("extendfunc Floating");
        this.button.setBackgroundColor(-16776961);
        this.button.setOnTouchListener(new FloatingOnTouchListener());
        this.button.setOnClickListener(new FloatingOnClickListener());
        this.button.setOnLongClickListener(new FloatingOnLongClickListener());
        this.mWindowParams = new WindowManager.LayoutParams(-2, -2, 2, 1032, -3);
        WindowManager.LayoutParams layoutParams = this.mWindowParams;
        layoutParams.gravity = 17;
        layoutParams.width = 500;
        layoutParams.height = 100;
        layoutParams.x = 300;
        layoutParams.y = 300;
        this.mWindowManager.addView(this.button, layoutParams);
    }

    public void showFloatingWindow() {
        this.button.setVisibility(0);
    }

    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;

        private FloatingOnTouchListener() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            if (action == 0) {
                this.x = (int) motionEvent.getRawX();
                this.y = (int) motionEvent.getRawY();
                return false;
            }
            if (action != 2) {
                return false;
            }
            int rawX = (int) motionEvent.getRawX();
            int rawY = (int) motionEvent.getRawY();
            int i = rawX - this.x;
            int i2 = rawY - this.y;
            this.x = rawX;
            this.y = rawY;
            FloatWindow.this.mWindowParams.x += i;
            FloatWindow.this.mWindowParams.y += i2;
            FloatWindow.this.mWindowManager.updateViewLayout(view, FloatWindow.this.mWindowParams);
            return false;
        }
    }

    private class FloatingOnClickListener implements View.OnClickListener {
        private FloatingOnClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            ModulesManager.getInst().extendFunc("test", "ngWebViewGeneral", "{\"methodId\":\"NGWebViewControl\",\n\"action\":\"hidden\"}");
        }
    }

    private class FloatingOnLongClickListener implements View.OnLongClickListener {
        private FloatingOnLongClickListener() {
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            FloatWindow.this.mWindowManager.removeView(view);
            return true;
        }
    }
}