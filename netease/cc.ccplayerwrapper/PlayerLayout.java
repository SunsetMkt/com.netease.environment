package com.netease.cc.ccplayerwrapper;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import cc.netease.com.ccplayerwrapper.R;
import com.netease.cc.ccplayerwrapper.utils.InfoView;
import com.netease.cc.ccplayerwrapper.utils.LogUtil;
import java.lang.ref.WeakReference;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/* loaded from: classes4.dex */
public class PlayerLayout extends FrameLayout implements GLSurfaceView.Renderer {

    /* renamed from: a, reason: collision with root package name */
    private InfoView f1523a;
    private int b;
    private GLSurfaceView c;
    private WeakReference<IjkMediaPlayer> d;
    private IMediaPlayer.OnRequestRedraw e;

    class a implements Runnable {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ FrameLayout.LayoutParams f1524a;

        a(FrameLayout.LayoutParams layoutParams) {
            this.f1524a = layoutParams;
        }

        @Override // java.lang.Runnable
        public void run() {
            LogUtil.LOGF("PlayerLayout", "size l-width:" + PlayerLayout.this.getWidth() + " l-height:" + PlayerLayout.this.getHeight() + " g-widht:" + this.f1524a.width + " g-height:" + this.f1524a.height);
            PlayerLayout.this.c.setLayoutParams(this.f1524a);
        }
    }

    class b implements IMediaPlayer.OnRequestRedraw {
        b() {
        }

        @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnRequestRedraw
        public void onRequestRedraw() {
            PlayerLayout.this.c.requestRender();
        }
    }

    public PlayerLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f1523a = null;
        this.b = -1;
        this.c = null;
        this.d = null;
        this.e = new b();
        View.inflate(context, R.layout.layout_cc_player, this);
        a();
    }

    private IjkMediaPlayer getPlayer() {
        WeakReference<IjkMediaPlayer> weakReference = this.d;
        if (weakReference != null) {
            return weakReference.get();
        }
        return null;
    }

    public GLSurfaceView getGlView() {
        return this.c;
    }

    public InfoView getInfoView() {
        return this.f1523a;
    }

    public float[] getRenderRectRatio() {
        if (getPlayer() != null) {
            return getPlayer().getRenderRectRatio();
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

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (this.b == 2) {
            updateLayout();
        }
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        LogUtil.LOGF("PlayerLayout", "onSurfaceChanged width:" + i + " height:" + i2);
        IjkMediaPlayer player = getPlayer();
        if (player != null) {
            player.glGLSurfaceChanged(i, i2);
        }
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        LogUtil.LOGF("PlayerLayout", "onSurfaceCreated");
        IjkMediaPlayer player = getPlayer();
        if (player != null) {
            player.glSurfaceCreated();
            player.setOnRedrawListener(this.e);
            player.setSurfaceHolder(this.c.getHolder());
        }
    }

    public void setScaleMode(int i) {
        if (this.b != i) {
            LogUtil.LOGF("PlayerLayout", "change scale mode " + i + " -> " + i);
            this.b = i;
            updateLayout();
        }
    }

    public void setupRender(IjkMediaPlayer ijkMediaPlayer) {
        LogUtil.LOGD("PlayerLayout", "setupRender");
        this.c.setEGLContextClientVersion(2);
        this.d = new WeakReference<>(ijkMediaPlayer);
        this.c.setRenderer(this);
        this.c.setRenderMode(0);
    }

    public void updateLayout() {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.c.getLayoutParams();
        layoutParams.width = -1;
        layoutParams.height = -1;
        layoutParams.gravity = 17;
        if (this.b == 2) {
            int width = getWidth();
            int height = getHeight();
            int videoWidth = getPlayer().getVideoWidth();
            int videoHeight = getPlayer().getVideoHeight();
            LogUtil.LOGF("PlayerLayout", "size l-width:" + width + " l-height:" + height + " v-widht:" + videoWidth + " v-height:" + videoHeight);
            if (width <= 0 || height <= 0 || videoWidth <= 0 || videoHeight <= 0) {
                LogUtil.LOGF("PlayerLayout", "invalid size l-width:" + width + " l-height:" + height + " v-widht:" + videoWidth + " v-height:" + videoHeight);
            } else if (videoHeight / videoWidth > height / width) {
                layoutParams.height = -1;
                layoutParams.width = (height * videoWidth) / videoHeight;
            } else {
                layoutParams.width = -1;
                layoutParams.height = (width * videoHeight) / videoWidth;
            }
        }
        post(new a(layoutParams));
    }

    private void a() {
        this.c = (GLSurfaceView) findViewById(R.id.gl_surface_view);
        this.f1523a = (InfoView) findViewById(R.id.info_view);
    }

    public PlayerLayout(Context context) {
        super(context, null);
        this.f1523a = null;
        this.b = -1;
        this.c = null;
        this.d = null;
        this.e = new b();
    }
}