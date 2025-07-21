package com.netease.vrlib;

import android.app.Activity;
import android.content.Context;
import android.graphics.RectF;
import android.hardware.SensorEventListener;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.widget.Toast;
import com.google.android.apps.muzei.render.GLTextureView;
import com.netease.vrlib.MD360DirectorFactory;
import com.netease.vrlib.common.GLUtil;
import com.netease.vrlib.common.MDGLHandler;
import com.netease.vrlib.common.MDMainHandler;
import com.netease.vrlib.common.VRUtil;
import com.netease.vrlib.model.BarrelDistortionConfig;
import com.netease.vrlib.model.MDMainPluginBuilder;
import com.netease.vrlib.model.MDPinchConfig;
import com.netease.vrlib.model.MDRay;
import com.netease.vrlib.plugins.IMDHotspot;
import com.netease.vrlib.plugins.MDAbsPlugin;
import com.netease.vrlib.plugins.MDPluginManager;
import com.netease.vrlib.strategy.display.DisplayModeManager;
import com.netease.vrlib.strategy.interactive.InteractiveModeManager;
import com.netease.vrlib.strategy.projection.IMDProjectionFactory;
import com.netease.vrlib.strategy.projection.ProjectionModeManager;
import com.netease.vrlib.texture.MD360BitmapTexture;
import com.netease.vrlib.texture.MD360Texture;
import com.netease.vrlib.texture.MD360VideoTexture;
import java.lang.ref.WeakReference;
import java.util.Iterator;

/* loaded from: classes3.dex */
public class MDVRLibrary {
    public static final int DISPLAY_MODE_GLASS = 102;
    public static final int DISPLAY_MODE_NORMAL = 101;
    public static final int INTERACTIVE_MODE_CARDBORAD_MOTION = 4;
    public static final int INTERACTIVE_MODE_CARDBORAD_MOTION_WITH_TOUCH = 5;
    public static final int INTERACTIVE_MODE_MOTION = 1;
    public static final int INTERACTIVE_MODE_MOTION_WITH_TOUCH = 3;
    public static final int INTERACTIVE_MODE_TOUCH = 2;
    public static final int PROJECTION_MODE_DOME180 = 202;
    public static final int PROJECTION_MODE_DOME180_UPPER = 204;
    public static final int PROJECTION_MODE_DOME230 = 203;
    public static final int PROJECTION_MODE_DOME230_UPPER = 205;
    public static final int PROJECTION_MODE_MULTI_FISH_EYE_HORIZONTAL = 210;
    public static final int PROJECTION_MODE_MULTI_FISH_EYE_VERTICAL = 211;
    public static final int PROJECTION_MODE_PLANE_CROP = 208;
    public static final int PROJECTION_MODE_PLANE_FIT = 207;
    public static final int PROJECTION_MODE_PLANE_FULL = 209;
    public static final int PROJECTION_MODE_SPHERE = 201;

    @Deprecated
    public static final int PROJECTION_MODE_STEREO_SPHERE = 206;
    public static final int PROJECTION_MODE_STEREO_SPHERE_HORIZONTAL = 212;
    public static final int PROJECTION_MODE_STEREO_SPHERE_VERTICAL = 213;
    private static final String TAG = "MDVRLibrary";
    public static final int sMultiScreenSize = 2;
    private DisplayModeManager mDisplayModeManager;
    private MDGLHandler mGLHandler;
    private InteractiveModeManager mInteractiveModeManager;
    private MDPickerManager mPickerManager;
    private MDPluginManager mPluginManager;
    private ProjectionModeManager mProjectionModeManager;
    private MDGLScreenWrapper mScreenWrapper;
    public MD360Texture mTexture;
    private RectF mTextureSize;
    private MDTouchHelper mTouchHelper;

    public interface ContentType {
        public static final int BITMAP = 1;
        public static final int DEFAULT = 0;
        public static final int VIDEO = 0;
    }

    interface IAdvanceGestureListener {
        void onDrag(float f, float f2);

        void onPinch(float f);
    }

    public interface IBitmapProvider {
        void onProvideBitmap(MD360BitmapTexture.Callback callback);
    }

    public interface IEyePickListener {
        void onHotspotHit(IMDHotspot iMDHotspot, long j);
    }

    public interface IGestureListener {
        void onClick(MotionEvent motionEvent);
    }

    public interface INotSupportCallback {
        void onNotSupport(int i);
    }

    public interface IOnSurfaceReadyCallback {
        void onSurfaceReady(Surface surface);
    }

    public interface ITouchPickListener {
        void onHotspotHit(IMDHotspot iMDHotspot, MDRay mDRay);
    }

    /* synthetic */ MDVRLibrary(Builder builder, AnonymousClass1 anonymousClass1) {
        this(builder);
    }

    private MDVRLibrary(Builder builder) {
        this.mTextureSize = new RectF(0.0f, 0.0f, 1024.0f, 1024.0f);
        MDMainHandler.init();
        this.mGLHandler = new MDGLHandler();
        initModeManager(builder);
        initPluginManager(builder);
        initOpenGL((Context) builder.refActivity.get(), builder.screenWrapper);
        this.mTexture = builder.texture;
        MDTouchHelper mDTouchHelper = new MDTouchHelper(((Activity) builder.refActivity.get()).getApplicationContext());
        this.mTouchHelper = mDTouchHelper;
        mDTouchHelper.addClickListener(builder.gestureListener);
        this.mTouchHelper.setPinchEnabled(builder.pinchEnabled);
        this.mTouchHelper.setAdvanceGestureListener(new IAdvanceGestureListener() { // from class: com.netease.vrlib.MDVRLibrary.1
            final /* synthetic */ UpdatePinchRunnable val$updatePinchRunnable;

            AnonymousClass1(UpdatePinchRunnable updatePinchRunnable) {
                updatePinchRunnable = updatePinchRunnable;
            }

            @Override // com.netease.vrlib.MDVRLibrary.IAdvanceGestureListener
            public void onDrag(float f, float f2) {
                MDVRLibrary.this.mInteractiveModeManager.handleDrag((int) f, (int) f2);
            }

            @Override // com.netease.vrlib.MDVRLibrary.IAdvanceGestureListener
            public void onPinch(float f) {
                updatePinchRunnable.setScale(f);
                MDVRLibrary.this.mGLHandler.post(updatePinchRunnable);
            }
        });
        this.mTouchHelper.setPinchConfig(builder.pinchConfig);
        this.mScreenWrapper.getView().setOnTouchListener(new View.OnTouchListener() { // from class: com.netease.vrlib.MDVRLibrary.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return MDVRLibrary.this.mTouchHelper.handleTouchEvent(motionEvent);
            }
        });
    }

    /* renamed from: com.netease.vrlib.MDVRLibrary$1 */
    class AnonymousClass1 implements IAdvanceGestureListener {
        final /* synthetic */ UpdatePinchRunnable val$updatePinchRunnable;

        AnonymousClass1(UpdatePinchRunnable updatePinchRunnable) {
            updatePinchRunnable = updatePinchRunnable;
        }

        @Override // com.netease.vrlib.MDVRLibrary.IAdvanceGestureListener
        public void onDrag(float f, float f2) {
            MDVRLibrary.this.mInteractiveModeManager.handleDrag((int) f, (int) f2);
        }

        @Override // com.netease.vrlib.MDVRLibrary.IAdvanceGestureListener
        public void onPinch(float f) {
            updatePinchRunnable.setScale(f);
            MDVRLibrary.this.mGLHandler.post(updatePinchRunnable);
        }
    }

    /* renamed from: com.netease.vrlib.MDVRLibrary$2 */
    class AnonymousClass2 implements View.OnTouchListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return MDVRLibrary.this.mTouchHelper.handleTouchEvent(motionEvent);
        }
    }

    private class UpdatePinchRunnable implements Runnable {
        private float scale;

        private UpdatePinchRunnable() {
        }

        /* synthetic */ UpdatePinchRunnable(MDVRLibrary mDVRLibrary, AnonymousClass1 anonymousClass1) {
            this();
        }

        public void setScale(float f) {
            this.scale = f;
        }

        @Override // java.lang.Runnable
        public void run() {
            Iterator<MD360Director> it = MDVRLibrary.this.mProjectionModeManager.getDirectors().iterator();
            while (it.hasNext()) {
                it.next().updateProjectionNearScale(this.scale);
            }
        }
    }

    private void initModeManager(Builder builder) {
        ProjectionModeManager.Params params = new ProjectionModeManager.Params();
        params.textureSize = this.mTextureSize;
        params.directorFactory = builder.directorFactory;
        params.projectionFactory = builder.projectionFactory;
        params.mainPluginBuilder = new MDMainPluginBuilder().setContentType(builder.contentType).setTexture(builder.texture);
        ProjectionModeManager projectionModeManager = new ProjectionModeManager(builder.projectionMode, this.mGLHandler, params);
        this.mProjectionModeManager = projectionModeManager;
        projectionModeManager.prepare((Activity) builder.refActivity.get(), builder.notSupportCallback);
        DisplayModeManager displayModeManager = new DisplayModeManager(builder.displayMode, this.mGLHandler);
        this.mDisplayModeManager = displayModeManager;
        displayModeManager.setBarrelDistortionConfig(builder.barrelDistortionConfig);
        this.mDisplayModeManager.setAntiDistortionEnabled(builder.barrelDistortionConfig.isDefaultEnabled());
        this.mDisplayModeManager.prepare((Activity) builder.refActivity.get(), builder.notSupportCallback);
        InteractiveModeManager.Params params2 = new InteractiveModeManager.Params();
        params2.projectionModeManager = this.mProjectionModeManager;
        params2.mMotionDelay = builder.motionDelay;
        params2.mSensorListener = builder.sensorListener;
        InteractiveModeManager interactiveModeManager = new InteractiveModeManager(builder.interactiveMode, this.mGLHandler, params2);
        this.mInteractiveModeManager = interactiveModeManager;
        interactiveModeManager.prepare((Activity) builder.refActivity.get(), builder.notSupportCallback);
    }

    private void initPluginManager(Builder builder) {
        this.mPluginManager = new MDPluginManager();
    }

    private void initOpenGL(Context context, MDGLScreenWrapper mDGLScreenWrapper) {
        if (GLUtil.supportsEs2(context)) {
            mDGLScreenWrapper.init(context);
            mDGLScreenWrapper.setRenderer(MD360Renderer.with(context).setGLHandler(this.mGLHandler).setPluginManager(this.mPluginManager).setProjectionModeManager(this.mProjectionModeManager).setDisplayModeManager(this.mDisplayModeManager).build());
            this.mScreenWrapper = mDGLScreenWrapper;
        } else {
            this.mScreenWrapper.getView().setVisibility(8);
            Toast.makeText(context, "OpenGLES2 not supported.", 0).show();
        }
    }

    public void switchInteractiveMode(Activity activity) {
        this.mInteractiveModeManager.switchMode(activity);
    }

    public void switchInteractiveMode(Activity activity, int i) {
        this.mInteractiveModeManager.switchMode(activity, i);
    }

    public void switchDisplayMode(Activity activity) {
        this.mDisplayModeManager.switchMode(activity);
    }

    public void switchDisplayMode(Activity activity, int i) {
        this.mDisplayModeManager.switchMode(activity, i);
    }

    public void switchProjectionMode(Activity activity, int i) {
        this.mProjectionModeManager.switchMode(activity, i);
    }

    /* renamed from: com.netease.vrlib.MDVRLibrary$3 */
    class AnonymousClass3 implements Runnable {
        AnonymousClass3() {
        }

        @Override // java.lang.Runnable
        public void run() {
            Iterator<MD360Director> it = MDVRLibrary.this.mProjectionModeManager.getDirectors().iterator();
            while (it.hasNext()) {
                it.next().reset();
            }
        }
    }

    public void resetTouch() {
        this.mGLHandler.post(new Runnable() { // from class: com.netease.vrlib.MDVRLibrary.3
            AnonymousClass3() {
            }

            @Override // java.lang.Runnable
            public void run() {
                Iterator<MD360Director> it = MDVRLibrary.this.mProjectionModeManager.getDirectors().iterator();
                while (it.hasNext()) {
                    it.next().reset();
                }
            }
        });
    }

    /* renamed from: com.netease.vrlib.MDVRLibrary$4 */
    class AnonymousClass4 implements Runnable {
        final /* synthetic */ float val$deltaX;
        final /* synthetic */ float val$deltaY;

        AnonymousClass4(float f, float f2) {
            f = f;
            f = f2;
        }

        @Override // java.lang.Runnable
        public void run() {
            for (MD360Director mD360Director : MDVRLibrary.this.mProjectionModeManager.getDirectors()) {
                mD360Director.setDeltaX(f);
                mD360Director.setDeltaY(f);
            }
        }
    }

    public void setDeltaXY(float f, float f2) {
        this.mGLHandler.post(new Runnable() { // from class: com.netease.vrlib.MDVRLibrary.4
            final /* synthetic */ float val$deltaX;
            final /* synthetic */ float val$deltaY;

            AnonymousClass4(float f3, float f22) {
                f = f3;
                f = f22;
            }

            @Override // java.lang.Runnable
            public void run() {
                for (MD360Director mD360Director : MDVRLibrary.this.mProjectionModeManager.getDirectors()) {
                    mD360Director.setDeltaX(f);
                    mD360Director.setDeltaY(f);
                }
            }
        });
    }

    public void resetPinch() {
        this.mTouchHelper.reset();
    }

    public void resetEyePick() {
        this.mPickerManager.resetEyePick();
    }

    public void setAntiDistortionEnabled(boolean z) {
        this.mDisplayModeManager.setAntiDistortionEnabled(z);
    }

    public boolean isAntiDistortionEnabled() {
        return this.mDisplayModeManager.isAntiDistortionEnabled();
    }

    public boolean isEyePickEnable() {
        return this.mPickerManager.isEyePickEnable();
    }

    public void setEyePickEnable(boolean z) {
        this.mPickerManager.setEyePickEnable(z);
    }

    public void setEyePickChangedListener(IEyePickListener iEyePickListener) {
        this.mPickerManager.setEyePickChangedListener(iEyePickListener);
    }

    public void setTouchPickListener(ITouchPickListener iTouchPickListener) {
        this.mPickerManager.setTouchPickListener(iTouchPickListener);
    }

    public void setPinchScale(float f) {
        this.mTouchHelper.scaleTo(f);
    }

    public void addPlugin(MDAbsPlugin mDAbsPlugin) {
        this.mPluginManager.add(mDAbsPlugin);
    }

    public void removePlugin(MDAbsPlugin mDAbsPlugin) {
        this.mPluginManager.remove(mDAbsPlugin);
    }

    public void removePlugins() {
        this.mPluginManager.removeAll();
    }

    public void onTextureResize(float f, float f2) {
        this.mTextureSize.set(0.0f, 0.0f, f, f2);
    }

    public void onOrientationChanged(Activity activity) {
        this.mInteractiveModeManager.onOrientationChanged(activity);
    }

    public void onResume(Context context) {
        this.mInteractiveModeManager.onResume(context);
        MDGLScreenWrapper mDGLScreenWrapper = this.mScreenWrapper;
        if (mDGLScreenWrapper != null) {
            mDGLScreenWrapper.onResume();
        }
    }

    public void onPause(Context context) {
        this.mInteractiveModeManager.onPause(context);
        MDGLScreenWrapper mDGLScreenWrapper = this.mScreenWrapper;
        if (mDGLScreenWrapper != null) {
            mDGLScreenWrapper.onPause();
        }
    }

    /* renamed from: com.netease.vrlib.MDVRLibrary$5 */
    class AnonymousClass5 implements Runnable {
        AnonymousClass5() {
        }

        @Override // java.lang.Runnable
        public void run() {
            MDVRLibrary.this.fireDestroy();
        }
    }

    public void onDestroy() {
        this.mGLHandler.post(new Runnable() { // from class: com.netease.vrlib.MDVRLibrary.5
            AnonymousClass5() {
            }

            @Override // java.lang.Runnable
            public void run() {
                MDVRLibrary.this.fireDestroy();
            }
        });
        this.mGLHandler.destroy();
    }

    public void fireDestroy() {
        MDPluginManager mDPluginManager = this.mPluginManager;
        if (mDPluginManager != null) {
            Iterator<MDAbsPlugin> it = mDPluginManager.getPlugins().iterator();
            while (it.hasNext()) {
                it.next().destroy();
            }
            this.mPluginManager = null;
        }
        ProjectionModeManager projectionModeManager = this.mProjectionModeManager;
        if (projectionModeManager != null) {
            projectionModeManager.release();
            this.mProjectionModeManager = null;
        }
        MD360Texture mD360Texture = this.mTexture;
        if (mD360Texture != null) {
            mD360Texture.destroy();
            this.mTexture.release();
            this.mTexture = null;
        }
        MDTouchHelper mDTouchHelper = this.mTouchHelper;
        if (mDTouchHelper != null) {
            mDTouchHelper.release();
            this.mTouchHelper = null;
        }
        MDGLScreenWrapper mDGLScreenWrapper = this.mScreenWrapper;
        if (mDGLScreenWrapper != null) {
            if (mDGLScreenWrapper.getView() != null) {
                this.mScreenWrapper.getView().setOnTouchListener(null);
            }
            this.mScreenWrapper = null;
        }
        MDMainHandler.release();
        this.mTextureSize = null;
        this.mInteractiveModeManager = null;
        this.mDisplayModeManager = null;
        this.mGLHandler = null;
    }

    public boolean handleTouchEvent(MotionEvent motionEvent) {
        this.mTouchHelper.handleTouchEvent(motionEvent);
        return false;
    }

    public int getInteractiveMode() {
        return this.mInteractiveModeManager.getMode();
    }

    public int getDisplayMode() {
        return this.mDisplayModeManager.getMode();
    }

    public int getProjectionMode() {
        return this.mProjectionModeManager.getMode();
    }

    public void notifyPlayerChanged() {
        MD360Texture mD360Texture = this.mTexture;
        if (mD360Texture != null) {
            mD360Texture.notifyChanged();
        }
    }

    public static Builder with(Activity activity) {
        return new Builder(activity);
    }

    public static class Builder {
        private BarrelDistortionConfig barrelDistortionConfig;
        private int contentType;
        private MD360DirectorFactory directorFactory;
        private int displayMode;
        private IEyePickListener eyePickChangedListener;
        private boolean eyePickEnabled;
        private IGestureListener gestureListener;
        private int interactiveMode;
        private int motionDelay;
        private INotSupportCallback notSupportCallback;
        private MDPinchConfig pinchConfig;
        private boolean pinchEnabled;
        private IMDProjectionFactory projectionFactory;
        private int projectionMode;
        private WeakReference<Activity> refActivity;
        private MDGLScreenWrapper screenWrapper;
        private SensorEventListener sensorListener;
        private MD360Texture texture;
        private ITouchPickListener touchPickChangedListener;

        /* synthetic */ Builder(Activity activity, AnonymousClass1 anonymousClass1) {
            this(activity);
        }

        private Builder(Activity activity) {
            this.displayMode = 101;
            this.interactiveMode = 1;
            this.projectionMode = 201;
            this.contentType = 0;
            this.eyePickEnabled = true;
            this.motionDelay = 1;
            this.refActivity = new WeakReference<>(activity);
        }

        public Builder displayMode(int i) {
            this.displayMode = i;
            return this;
        }

        public Builder interactiveMode(int i) {
            this.interactiveMode = i;
            return this;
        }

        public Builder projectionMode(int i) {
            this.projectionMode = i;
            return this;
        }

        public Builder ifNotSupport(INotSupportCallback iNotSupportCallback) {
            this.notSupportCallback = iNotSupportCallback;
            return this;
        }

        public Builder asVideo(IOnSurfaceReadyCallback iOnSurfaceReadyCallback) {
            this.texture = new MD360VideoTexture(iOnSurfaceReadyCallback);
            this.contentType = 0;
            return this;
        }

        public Builder asBitmap(IBitmapProvider iBitmapProvider) {
            VRUtil.notNull(iBitmapProvider, "bitmap Provider can't be null!");
            this.texture = new MD360BitmapTexture(iBitmapProvider);
            this.contentType = 1;
            return this;
        }

        @Deprecated
        public Builder gesture(IGestureListener iGestureListener) {
            this.gestureListener = iGestureListener;
            return this;
        }

        public Builder pinchEnabled(boolean z) {
            this.pinchEnabled = z;
            return this;
        }

        public Builder eyePickEanbled(boolean z) {
            this.eyePickEnabled = z;
            return this;
        }

        public Builder listenGesture(IGestureListener iGestureListener) {
            this.gestureListener = iGestureListener;
            return this;
        }

        public Builder listenEyePick(IEyePickListener iEyePickListener) {
            this.eyePickChangedListener = iEyePickListener;
            return this;
        }

        public Builder listenTouchPick(ITouchPickListener iTouchPickListener) {
            this.touchPickChangedListener = iTouchPickListener;
            return this;
        }

        public Builder motionDelay(int i) {
            this.motionDelay = i;
            return this;
        }

        public Builder sensorCallback(SensorEventListener sensorEventListener) {
            this.sensorListener = sensorEventListener;
            return this;
        }

        public Builder directorFactory(MD360DirectorFactory mD360DirectorFactory) {
            this.directorFactory = mD360DirectorFactory;
            return this;
        }

        public Builder projectionFactory(IMDProjectionFactory iMDProjectionFactory) {
            this.projectionFactory = iMDProjectionFactory;
            return this;
        }

        public Builder barrelDistortionConfig(BarrelDistortionConfig barrelDistortionConfig) {
            this.barrelDistortionConfig = barrelDistortionConfig;
            return this;
        }

        public Builder pinchConfig(MDPinchConfig mDPinchConfig) {
            this.pinchConfig = mDPinchConfig;
            return this;
        }

        public MDVRLibrary build(int i) {
            View viewFindViewById = this.refActivity.get().findViewById(i);
            if (viewFindViewById instanceof GLSurfaceView) {
                return build((GLSurfaceView) viewFindViewById);
            }
            if (viewFindViewById instanceof GLTextureView) {
                return build((GLTextureView) viewFindViewById);
            }
            throw new RuntimeException("Please ensure the glViewId is instance of GLSurfaceView or GLTextureView");
        }

        public MDVRLibrary build(GLSurfaceView gLSurfaceView) {
            return build(MDGLScreenWrapper.wrap(gLSurfaceView));
        }

        public MDVRLibrary build(GLTextureView gLTextureView) {
            return build(MDGLScreenWrapper.wrap(gLTextureView));
        }

        private MDVRLibrary build(MDGLScreenWrapper mDGLScreenWrapper) {
            VRUtil.notNull(this.texture, "You must call video/bitmap function before build");
            if (this.directorFactory == null) {
                this.directorFactory = new MD360DirectorFactory.DefaultImpl();
            }
            if (this.barrelDistortionConfig == null) {
                this.barrelDistortionConfig = new BarrelDistortionConfig();
            }
            if (this.pinchConfig == null) {
                this.pinchConfig = new MDPinchConfig();
            }
            this.screenWrapper = mDGLScreenWrapper;
            return new MDVRLibrary(this);
        }
    }
}