package com.netease.vrlib;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import com.netease.vrlib.common.Fps;
import com.netease.vrlib.common.GLUtil;
import com.netease.vrlib.common.MDGLHandler;
import com.netease.vrlib.plugins.MDAbsLinePipe;
import com.netease.vrlib.plugins.MDAbsPlugin;
import com.netease.vrlib.plugins.MDBarrelDistortionLinePipe;
import com.netease.vrlib.plugins.MDPluginManager;
import com.netease.vrlib.strategy.display.DisplayModeManager;
import com.netease.vrlib.strategy.projection.ProjectionModeManager;
import com.xiaomi.gamecenter.sdk.pay.ReportCode;
import java.util.Iterator;
import java.util.List;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes3.dex */
public class MD360Renderer implements GLSurfaceView.Renderer {
    private static final String TAG = "MD360Renderer";
    private final Context mContext;
    private DisplayModeManager mDisplayModeManager;
    private Fps mFps;
    private MDGLHandler mGLHandler;
    private int mHeight;
    private MDAbsLinePipe mMainLinePipe;
    private MDPluginManager mPluginManager;
    private ProjectionModeManager mProjectionModeManager;
    private int mWidth;

    private MD360Renderer(Builder builder) {
        this.mFps = new Fps();
        this.mContext = builder.context;
        this.mDisplayModeManager = builder.displayModeManager;
        this.mProjectionModeManager = builder.projectionModeManager;
        this.mPluginManager = builder.pluginManager;
        this.mGLHandler = builder.glHandler;
        this.mMainLinePipe = new MDBarrelDistortionLinePipe(this.mDisplayModeManager);
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glEnable(2884);
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        this.mWidth = i;
        this.mHeight = i2;
        this.mGLHandler.dealMessage();
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onDrawFrame(GL10 gl10) {
        this.mGLHandler.dealMessage();
        GLES20.glClear(16640);
        GLUtil.glCheck("MD360Renderer onDrawFrame 1");
        int visibleSize = this.mDisplayModeManager.getVisibleSize();
        int i = (int) ((this.mWidth * 1.0f) / visibleSize);
        int i2 = this.mHeight;
        this.mMainLinePipe.setup(this.mContext);
        this.mMainLinePipe.takeOver(this.mWidth, this.mHeight, visibleSize);
        List<MD360Director> directors = this.mProjectionModeManager.getDirectors();
        MDAbsPlugin mainPlugin = this.mProjectionModeManager.getMainPlugin();
        if (mainPlugin != null) {
            mainPlugin.setup(this.mContext);
            mainPlugin.beforeRenderer(this.mWidth, this.mHeight);
        }
        for (MDAbsPlugin mDAbsPlugin : this.mPluginManager.getPlugins()) {
            mDAbsPlugin.setup(this.mContext);
            mDAbsPlugin.beforeRenderer(this.mWidth, this.mHeight);
        }
        if (directors != null) {
            for (int i3 = 0; i3 < visibleSize && i3 < directors.size(); i3++) {
                MD360Director mD360Director = directors.get(i3);
                int i4 = i * i3;
                GLES20.glViewport(i4, 0, i, i2);
                GLES20.glEnable(ReportCode.H);
                GLES20.glScissor(i4, 0, i, i2);
                if (mainPlugin != null) {
                    mainPlugin.renderer(i3, i, i2, mD360Director);
                }
                Iterator<MDAbsPlugin> it = this.mPluginManager.getPlugins().iterator();
                while (it.hasNext()) {
                    it.next().renderer(i3, i, i2, mD360Director);
                }
                GLES20.glDisable(ReportCode.H);
            }
        }
        MDAbsLinePipe mDAbsLinePipe = this.mMainLinePipe;
        if (mDAbsLinePipe != null) {
            mDAbsLinePipe.commit(this.mWidth, this.mHeight, visibleSize);
        }
    }

    public static Builder with(Context context) {
        Builder builder = new Builder();
        builder.context = context;
        return builder;
    }

    public static class Builder {
        private Context context;
        private DisplayModeManager displayModeManager;
        private MDGLHandler glHandler;
        private MDPluginManager pluginManager;
        private ProjectionModeManager projectionModeManager;

        private Builder() {
        }

        public MD360Renderer build() {
            return new MD360Renderer(this);
        }

        public Builder setGLHandler(MDGLHandler mDGLHandler) {
            this.glHandler = mDGLHandler;
            return this;
        }

        public Builder setPluginManager(MDPluginManager mDPluginManager) {
            this.pluginManager = mDPluginManager;
            return this;
        }

        public Builder setDisplayModeManager(DisplayModeManager displayModeManager) {
            this.displayModeManager = displayModeManager;
            return this;
        }

        public Builder setProjectionModeManager(ProjectionModeManager projectionModeManager) {
            this.projectionModeManager = projectionModeManager;
            return this;
        }
    }
}