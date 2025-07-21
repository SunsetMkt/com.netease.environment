package com.netease.ntunisdk.base;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;
import com.netease.ntunisdk.base.StartupDialog;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;

@Deprecated
/* loaded from: classes2.dex */
public class StartupActivity extends Activity {

    /* renamed from: a, reason: collision with root package name */
    public static StartupFinishListener f1796a;

    public interface StartupFinishListener {
        void finishListener();
    }

    public static void popStartup(Context context, final StartupFinishListener startupFinishListener) {
        StartupDialog.popStartup(context, new StartupDialog.StartupFinishListener() { // from class: com.netease.ntunisdk.base.StartupActivity.1
            @Override // com.netease.ntunisdk.base.StartupDialog.StartupFinishListener
            public final void finishListener() {
                StartupFinishListener startupFinishListener2 = startupFinishListener;
                if (startupFinishListener2 != null) {
                    startupFinishListener2.finishListener();
                }
            }
        });
    }

    public static void popStartup(Context context, final StartupFinishListener startupFinishListener, int i) {
        StartupDialog.popStartup(context, new StartupDialog.StartupFinishListener() { // from class: com.netease.ntunisdk.base.StartupActivity.2
            @Override // com.netease.ntunisdk.base.StartupDialog.StartupFinishListener
            public final void finishListener() {
                StartupFinishListener startupFinishListener2 = startupFinishListener;
                if (startupFinishListener2 != null) {
                    startupFinishListener2.finishListener();
                }
            }
        }, i);
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        UniSdkUtils.i("UniSDK Base", "StartupActivity onCreate");
        final RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setBackgroundColor(getIntent().getIntExtra(ConstProp.SPLASH_COLOR, -1));
        int propInt = SdkMgr.getInst().getPropInt(ConstProp.SPLASH_TYPE, 1);
        UniSdkUtils.i("UniSDK Base", "SPLASH_TYPE:".concat(String.valueOf(propInt)));
        if (1 == propInt) {
            UniSdkUtils.i("UniSDK Base", "SPLASH_TYPE: " + propInt + " SPLASH_TYPE_PNG ");
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
            layoutParams.addRule(13);
            int identifier = getResources().getIdentifier("sdk_startup_logo", ResIdReader.RES_TYPE_DRAWABLE, getPackageName());
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(identifier);
            relativeLayout.addView(imageView, layoutParams);
        }
        if (2 == propInt) {
            UniSdkUtils.i("UniSDK Base", "SPLASH_TYPE: " + propInt + " SPLASH_TYPE_MEDIA ");
            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-1, -1);
            layoutParams2.addRule(12);
            layoutParams2.addRule(10);
            layoutParams2.addRule(9);
            layoutParams2.addRule(11);
            String str = "android.resource://" + getPackageName() + "/" + getResources().getIdentifier("startup", "raw", getPackageName());
            UniSdkUtils.i("UniSDK Base", "MEDIA PATH: ".concat(String.valueOf(str)));
            final VideoView videoView = new VideoView(this);
            videoView.setVideoURI(Uri.parse(str));
            videoView.requestFocus();
            videoView.setLayoutParams(layoutParams2);
            relativeLayout.addView(videoView, layoutParams2);
            videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() { // from class: com.netease.ntunisdk.base.StartupActivity.3
                @Override // android.media.MediaPlayer.OnErrorListener
                public final boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
                    UniSdkUtils.i("UniSDK Base", "MediaPlayer error what : " + i + " extra : " + i2);
                    return true;
                }
            });
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.netease.ntunisdk.base.StartupActivity.4
                @Override // android.media.MediaPlayer.OnPreparedListener
                public final void onPrepared(MediaPlayer mediaPlayer) {
                    videoView.start();
                    UniSdkUtils.i("UniSDK Base", "SPLASH_TYPE_MEDIA start play");
                }
            });
        }
        getWindow().setFlags(1024, 1024);
        requestWindowFeature(1);
        setContentView(relativeLayout);
        long propInt2 = SdkMgr.getInst().getPropInt(ConstProp.SPLASH_TIME, 1) * 1000;
        final AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(propInt2);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.netease.ntunisdk.base.StartupActivity.5
            @Override // android.view.animation.Animation.AnimationListener
            public final void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public final void onAnimationStart(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public final void onAnimationEnd(Animation animation) {
                UniSdkUtils.i("UniSDK Base", "onAnimationEnd!");
                relativeLayout.setVisibility(8);
                StartupActivity.this.finish();
            }
        });
        new Handler().postDelayed(new Runnable() { // from class: com.netease.ntunisdk.base.StartupActivity.6
            @Override // java.lang.Runnable
            public final void run() {
                relativeLayout.startAnimation(alphaAnimation);
            }
        }, 1000L);
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        StartupFinishListener startupFinishListener = f1796a;
        if (startupFinishListener != null) {
            startupFinishListener.finishListener();
        }
    }
}