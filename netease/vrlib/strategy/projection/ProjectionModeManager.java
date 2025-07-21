package com.netease.vrlib.strategy.projection;

import android.app.Activity;
import android.graphics.RectF;
import com.netease.vrlib.MD360Director;
import com.netease.vrlib.MD360DirectorFactory;
import com.netease.vrlib.common.MDDirection;
import com.netease.vrlib.common.MDGLHandler;
import com.netease.vrlib.model.MDMainPluginBuilder;
import com.netease.vrlib.model.MDPosition;
import com.netease.vrlib.objects.MDAbsObject3D;
import com.netease.vrlib.plugins.MDAbsPlugin;
import com.netease.vrlib.strategy.ModeManager;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes5.dex */
public class ProjectionModeManager extends ModeManager<AbsProjectionStrategy> implements IProjectionMode {
    public static int[] sModes = {201, 202, 203};
    private MD360DirectorFactory mCustomDirectorFactory;
    private List<MD360Director> mDirectors;
    private MDAbsPlugin mMainPlugin;
    private MDMainPluginBuilder mMainPluginBuilder;
    private IMDProjectionFactory mProjectionFactory;
    private RectF mTextureSize;

    public static class Params {
        public MD360DirectorFactory directorFactory;
        public MDMainPluginBuilder mainPluginBuilder;
        public IMDProjectionFactory projectionFactory;
        public RectF textureSize;
    }

    public ProjectionModeManager(int i, MDGLHandler mDGLHandler, Params params) {
        super(i, mDGLHandler);
        this.mDirectors = new LinkedList();
        this.mTextureSize = params.textureSize;
        this.mCustomDirectorFactory = params.directorFactory;
        this.mProjectionFactory = params.projectionFactory;
        MDMainPluginBuilder mDMainPluginBuilder = params.mainPluginBuilder;
        this.mMainPluginBuilder = mDMainPluginBuilder;
        mDMainPluginBuilder.setProjectionModeManager(this);
    }

    public MDAbsPlugin getMainPlugin() {
        if (this.mMainPlugin == null) {
            this.mMainPlugin = getStrategy().buildMainPlugin(this.mMainPluginBuilder);
        }
        return this.mMainPlugin;
    }

    @Override // com.netease.vrlib.strategy.ModeManager
    public void switchMode(Activity activity, int i) {
        super.switchMode(activity, i);
    }

    @Override // com.netease.vrlib.strategy.ModeManager
    public void on(Activity activity) {
        super.on(activity);
        MDAbsPlugin mDAbsPlugin = this.mMainPlugin;
        if (mDAbsPlugin != null) {
            mDAbsPlugin.destroy();
            this.mMainPlugin = null;
        }
        this.mDirectors.clear();
        MD360DirectorFactory mD360DirectorFactoryHijackDirectorFactory = getStrategy().hijackDirectorFactory();
        if (mD360DirectorFactoryHijackDirectorFactory == null) {
            mD360DirectorFactoryHijackDirectorFactory = this.mCustomDirectorFactory;
        }
        for (int i = 0; i < 2; i++) {
            this.mDirectors.add(mD360DirectorFactoryHijackDirectorFactory.createDirector(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.netease.vrlib.strategy.ModeManager
    public AbsProjectionStrategy createStrategy(int i) {
        AbsProjectionStrategy absProjectionStrategyCreateStrategy;
        IMDProjectionFactory iMDProjectionFactory = this.mProjectionFactory;
        if (iMDProjectionFactory != null && (absProjectionStrategyCreateStrategy = iMDProjectionFactory.createStrategy(i)) != null) {
            return absProjectionStrategyCreateStrategy;
        }
        switch (i) {
            case 202:
                return new DomeProjection(this.mTextureSize, 180.0f, false);
            case 203:
                return new DomeProjection(this.mTextureSize, 230.0f, false);
            case 204:
                return new DomeProjection(this.mTextureSize, 180.0f, true);
            case 205:
                return new DomeProjection(this.mTextureSize, 230.0f, true);
            case 206:
            case 213:
                return new StereoSphereProjection(MDDirection.VERTICAL);
            case 207:
            case 208:
            case 209:
                return PlaneProjection.create(i, this.mTextureSize);
            case 210:
                return new MultiFishEyeProjection(1.0f, MDDirection.HORIZONTAL);
            case 211:
                return new MultiFishEyeProjection(1.0f, MDDirection.VERTICAL);
            case 212:
                return new StereoSphereProjection(MDDirection.HORIZONTAL);
            default:
                return new SphereProjection();
        }
    }

    @Override // com.netease.vrlib.strategy.ModeManager
    protected int[] getModes() {
        return sModes;
    }

    @Override // com.netease.vrlib.strategy.projection.IProjectionMode
    public MDPosition getModelPosition() {
        return getStrategy().getModelPosition();
    }

    @Override // com.netease.vrlib.strategy.projection.IProjectionMode
    public MDAbsObject3D getObject3D() {
        return getStrategy().getObject3D();
    }

    public List<MD360Director> getDirectors() {
        return this.mDirectors;
    }

    public void release() {
        List<MD360Director> list = this.mDirectors;
        if (list != null) {
            list.clear();
            this.mDirectors = null;
        }
        MDAbsPlugin mDAbsPlugin = this.mMainPlugin;
        if (mDAbsPlugin != null) {
            mDAbsPlugin.destroy();
        }
    }
}