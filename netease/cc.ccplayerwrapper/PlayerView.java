package com.netease.cc.ccplayerwrapper;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import com.netease.cc.ccplayerwrapper.utils.LogUtil;
import java.lang.ref.WeakReference;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/* loaded from: classes4.dex */
public class PlayerView extends GLSurfaceView implements GLSurfaceView.Renderer {

    /* renamed from: a, reason: collision with root package name */
    WeakReference<IjkMediaPlayer> f1526a;
    private IMediaPlayer.OnRequestRedraw b;

    class a implements IMediaPlayer.OnRequestRedraw {
        a() {
        }

        @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnRequestRedraw
        public void onRequestRedraw() {
            PlayerView.this.requestRender();
        }
    }

    public PlayerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f1526a = null;
        this.b = new a();
    }

    private IjkMediaPlayer getPlayer() {
        WeakReference<IjkMediaPlayer> weakReference = this.f1526a;
        if (weakReference != null) {
            return weakReference.get();
        }
        return null;
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onDrawFrame(GL10 gl10) throws Throwable {
        IjkMediaPlayer player = getPlayer();
        if (player != null) {
            player.draw();
        }
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        LogUtil.LOGI("PlayerView", "onSurfaceChanged width:" + i + " height:" + i2);
        IjkMediaPlayer player = getPlayer();
        if (player != null) {
            player.glGLSurfaceChanged(i, i2);
        }
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        LogUtil.LOGI("PlayerView", "onSurfaceCreated");
        IjkMediaPlayer player = getPlayer();
        if (player != null) {
            player.glSurfaceCreated();
            player.setOnRedrawListener(this.b);
            player.setSurfaceHolder(getHolder());
        }
    }

    public void setupRender(IjkMediaPlayer ijkMediaPlayer) {
        LogUtil.LOGD("PlayerView", "setupRender");
        setEGLContextClientVersion(2);
        this.f1526a = new WeakReference<>(ijkMediaPlayer);
        setRenderer(this);
        setRenderMode(0);
    }

    @Override // android.opengl.GLSurfaceView, android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder surfaceHolder) throws IllegalStateException {
        super.surfaceCreated(surfaceHolder);
        LogUtil.LOGI("Render surfaceCreated");
        IjkMediaPlayer player = getPlayer();
        if (player != null) {
            player.onGLSurfaceCreated();
            player.resumeVideoDisplay();
        }
    }

    @Override // android.opengl.GLSurfaceView, android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) throws IllegalStateException {
        super.surfaceDestroyed(surfaceHolder);
        LogUtil.LOGI("Render surfaceDestroyed");
        IjkMediaPlayer player = getPlayer();
        if (player != null) {
            player.pauseVideoDisplay();
        }
    }
}