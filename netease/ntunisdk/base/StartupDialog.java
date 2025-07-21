package com.netease.ntunisdk.base;

import android.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;

/* loaded from: classes2.dex */
public class StartupDialog {

    /* renamed from: a, reason: collision with root package name */
    private static StartupDialog f1803a;
    private Handler b;
    private Dialog c;
    private RelativeLayout d;
    private Runnable e;

    public interface OnClickSplashFinishListener {
        void onClickSplash();
    }

    public interface StartupFinishListener {
        void finishListener();
    }

    /* synthetic */ StartupDialog(Context context, int i, int i2, int i3, StartupFinishListener startupFinishListener, byte b) {
        this(context, i, i2, i3, startupFinishListener);
    }

    public static void popStartup(Context context, StartupFinishListener startupFinishListener) {
        popStartup(context, startupFinishListener, SdkMgr.getInst().getPropInt(ConstProp.SPLASH_COLOR, -1));
    }

    public static void popStartup(Context context, StartupFinishListener startupFinishListener, int i) {
        popStartup(context, startupFinishListener, i, SdkMgr.getInst().getPropInt(ConstProp.SPLASH_TYPE, 1));
    }

    public static void popStartup(final Context context, final StartupFinishListener startupFinishListener, final int i, final int i2) {
        final int propInt = SdkMgr.getInst().getPropInt(ConstProp.SPLASH_TIME, 1);
        final int propInt2 = SdkMgr.getInst().getPropInt(ConstProp.SPLASH_STAY_TIME, 2);
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.StartupDialog.1
                @Override // java.lang.Runnable
                public final void run() {
                    new StartupDialog(context, i2, i, propInt, startupFinishListener, (byte) 0).popStartup(propInt2);
                }
            });
        } else {
            new StartupDialog(context, i2, i, propInt, startupFinishListener).popStartup(propInt2);
        }
        UniSdkUtils.i("UniSDK Base", "StartupDialog popStartup color: " + i + ", type=" + i2);
    }

    private StartupDialog(Context context, int i, int i2, int i3, final StartupFinishListener startupFinishListener, boolean z, final OnClickSplashFinishListener onClickSplashFinishListener) throws IllegalAccessException, IllegalArgumentException {
        int identifier;
        UniSdkUtils.i("UniSDK Base", "StartupDialog construct");
        this.c = new Dialog(context, R.style.Theme.Black.NoTitleBar.Fullscreen);
        RelativeLayout relativeLayout = new RelativeLayout(context.getApplicationContext());
        this.d = relativeLayout;
        relativeLayout.setBackgroundColor(i2);
        this.d.setSystemUiVisibility(1536);
        try {
            Window window = this.c.getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.getClass().getField("layoutInDisplayCutoutMode").setInt(attributes, 1);
            window.setAttributes(attributes);
        } catch (Exception unused) {
        }
        if (i == 1) {
            UniSdkUtils.i("UniSDK Base", "SPLASH_TYPE: " + i + " SPLASH_TYPE_PNG ");
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
            layoutParams.addRule(13);
            if (z) {
                identifier = context.getResources().getIdentifier("netease_splash", ResIdReader.RES_TYPE_DRAWABLE, context.getPackageName());
            } else {
                identifier = context.getResources().getIdentifier("sdk_startup_logo", ResIdReader.RES_TYPE_DRAWABLE, context.getPackageName());
            }
            ImageView imageView = new ImageView(context.getApplicationContext());
            if (1 == SdkMgr.getInst().getPropInt(ConstProp.SPLASH_PNG_SCALE_TYPE, 0)) {
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            }
            imageView.setImageResource(identifier);
            this.d.addView(imageView, layoutParams);
        } else if (i == 2) {
            UniSdkUtils.i("UniSDK Base", "SPLASH_TYPE: " + i + " SPLASH_TYPE_MEDIA ");
            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-1, -1);
            layoutParams2.addRule(12);
            layoutParams2.addRule(10);
            layoutParams2.addRule(9);
            layoutParams2.addRule(11);
            String str = "android.resource://" + context.getPackageName() + "/" + context.getResources().getIdentifier("startup", "raw", context.getPackageName());
            UniSdkUtils.i("UniSDK Base", "MEDIA PATH: ".concat(String.valueOf(str)));
            final VideoView videoView = new VideoView(context.getApplicationContext());
            videoView.setVideoURI(Uri.parse(str));
            videoView.requestFocus();
            videoView.setLayoutParams(layoutParams2);
            this.d.addView(videoView, layoutParams2);
            videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() { // from class: com.netease.ntunisdk.base.StartupDialog.2
                @Override // android.media.MediaPlayer.OnErrorListener
                public final boolean onError(MediaPlayer mediaPlayer, int i4, int i5) {
                    UniSdkUtils.i("UniSDK Base", "MediaPlayer error what : " + i4 + " extra : " + i5);
                    return true;
                }
            });
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.netease.ntunisdk.base.StartupDialog.3
                @Override // android.media.MediaPlayer.OnPreparedListener
                public final void onPrepared(MediaPlayer mediaPlayer) {
                    videoView.start();
                    UniSdkUtils.i("UniSDK Base", "SPLASH_TYPE_MEDIA start play");
                }
            });
        }
        this.d.setOnClickListener(new View.OnClickListener() { // from class: com.netease.ntunisdk.base.StartupDialog.4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                if (onClickSplashFinishListener != null) {
                    UniSdkUtils.d("UniSDK Base", "click splash");
                    onClickSplashFinishListener.onClickSplash();
                }
            }
        });
        this.c.setContentView(this.d);
        this.c.setCancelable(false);
        this.c.setCanceledOnTouchOutside(false);
        this.c.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.netease.ntunisdk.base.StartupDialog.5
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                UniSdkUtils.i("UniSDK Base", "StartupDialog onDismiss");
                StartupFinishListener startupFinishListener2 = startupFinishListener;
                if (startupFinishListener2 != null) {
                    startupFinishListener2.finishListener();
                }
                if (SdkMgr.getInst().getPropInt(ConstProp.CLEAR_FOCUS_WHEN_STARTUP, 0) != 0) {
                    StartupDialog.this.c.getWindow().clearFlags(8);
                }
            }
        });
        if (z) {
            return;
        }
        final AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(i3 * 1000);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.netease.ntunisdk.base.StartupDialog.6
            @Override // android.view.animation.Animation.AnimationListener
            public final void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public final void onAnimationStart(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public final void onAnimationEnd(Animation animation) {
                UniSdkUtils.i("UniSDK Base", "onAnimationEnd!");
                if (StartupDialog.this.c.isShowing()) {
                    StartupDialog.this.c.dismiss();
                }
            }
        });
        this.e = new Runnable() { // from class: com.netease.ntunisdk.base.StartupDialog.7
            @Override // java.lang.Runnable
            public final void run() {
                StartupDialog.this.d.startAnimation(alphaAnimation);
            }
        };
    }

    private StartupDialog(Context context, int i, int i2, int i3, StartupFinishListener startupFinishListener) {
        this(context, i, i2, i3, startupFinishListener, false, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void popStartup(int i) {
        if (SdkMgr.getInst().getPropInt(ConstProp.CLEAR_FOCUS_WHEN_STARTUP, 0) != 0) {
            this.c.getWindow().addFlags(8);
        }
        this.c.show();
        new Handler(Looper.getMainLooper()).postDelayed(this.e, i * 1000);
    }

    public static void popStartupSecond(Context context, OnClickSplashFinishListener onClickSplashFinishListener) {
        if (f1803a == null) {
            UniSdkUtils.d("UniSDK Base", "new startupDialogInst");
            f1803a = new StartupDialog(context, 1, SdkMgr.getInst().getPropInt(ConstProp.SPLASH_COLOR, -1), 1, null, true, onClickSplashFinishListener);
        }
        f1803a.c.show();
        f1803a.b = new Handler(Looper.getMainLooper());
        StartupDialog startupDialog = f1803a;
        Handler handler = startupDialog.b;
        Runnable runnable = new Runnable() { // from class: com.netease.ntunisdk.base.StartupDialog.8
            @Override // java.lang.Runnable
            public final void run() {
                if (StartupDialog.f1803a.c.isShowing()) {
                    StartupDialog.f1803a.c.dismiss();
                }
            }
        };
        startupDialog.e = runnable;
        handler.postDelayed(runnable, 30000L);
    }

    public static void ntCloseFlash() {
        if (f1803a == null) {
            UniSdkUtils.e("UniSDK Base", "startupDialogInst not instance");
            return;
        }
        UniSdkUtils.d("UniSDK Base", "call ntCloseFlash");
        StartupDialog startupDialog = f1803a;
        Handler handler = startupDialog.b;
        if (handler != null) {
            handler.removeCallbacks(startupDialog.e);
        }
        if (f1803a.c.isShowing()) {
            f1803a.c.dismiss();
        }
    }
}