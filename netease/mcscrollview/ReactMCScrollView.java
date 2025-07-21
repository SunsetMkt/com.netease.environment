package com.netease.mcscrollview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.OverScroller;
import android.widget.ScrollView;
import androidx.core.view.ViewCompat;
import com.facebook.common.logging.FLog;
import com.facebook.infer.annotation.Assertions;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.ReactConstants;
import com.facebook.react.uimanager.FabricViewStateManager;
import com.facebook.react.uimanager.MeasureSpecAssertions;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.PointerEvents;
import com.facebook.react.uimanager.ReactClippingViewGroup;
import com.facebook.react.uimanager.ReactClippingViewGroupHelper;
import com.facebook.react.uimanager.ReactOverflowViewWithInset;
import com.facebook.react.uimanager.ViewProps;
import com.facebook.react.uimanager.events.NativeGestureUtil;
import com.facebook.react.views.scroll.FpsListener;
import com.facebook.react.views.scroll.MaintainVisibleScrollPositionHelper;
import com.facebook.react.views.scroll.OnScrollDispatchHelper;
import com.facebook.react.views.scroll.ReactScrollViewAccessibilityDelegate;
import com.facebook.react.views.scroll.ReactScrollViewHelper;
import com.facebook.react.views.scroll.VelocityHelper;
import com.facebook.react.views.view.ReactViewBackgroundManager;
import com.netease.download.Const;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jose4j.jwk.EllipticCurveJsonWebKey;

/* loaded from: classes4.dex */
public class ReactMCScrollView extends ScrollView implements ReactClippingViewGroup, ViewGroup.OnHierarchyChangeListener, View.OnLayoutChangeListener, FabricViewStateManager.HasFabricViewStateManager, ReactOverflowViewWithInset, ReactScrollViewHelper.HasScrollState, ReactScrollViewHelper.HasFlingAnimator, ReactScrollViewHelper.HasScrollEventThrottle, ReactScrollViewHelper.HasSmoothScroll {
    private static final int INVALID_POINTER = -1;
    private static final int UNSET_CONTENT_OFFSET = -1;
    private static Field sScrollerField;
    private static boolean sTriedToGetScrollerField;
    private final ValueAnimator DEFAULT_FLING_ANIMATOR;
    private int mActivePointerId;
    private boolean mActivelyScrolling;
    private Rect mClippingRect;
    private View mContentView;
    private ReadableMap mCurrentContentOffset;
    private boolean mDisableIntervalMomentum;
    private boolean mDragging;
    private Drawable mEndBackground;
    private int mEndFillColor;
    private final FabricViewStateManager mFabricViewStateManager;
    private FpsListener mFpsListener;
    private float mLastMotionY;
    private long mLastScrollDispatchTime;
    private String mMCActiveId;
    private int mMCDeltH;
    private String mMCId;
    private String mMCRole;
    private MaintainVisibleScrollPositionHelper mMaintainVisibleContentPositionHelper;
    private float mMaxVelocity;
    private final OnScrollDispatchHelper mOnScrollDispatchHelper;
    private String mOverflow;
    private final Rect mOverflowInset;
    private boolean mPagingEnabled;
    private boolean mParentEnable;
    private PointerEvents mPointerEvents;
    private Runnable mPostTouchRunnable;
    private ReactViewBackgroundManager mReactBackgroundManager;
    private final ReactScrollViewHelper.ReactScrollViewScrollState mReactScrollViewScrollState;
    private final Rect mRect;
    private boolean mRemoveClippedSubviews;
    private boolean mScrollEnabled;
    private int mScrollEventThrottle;
    private String mScrollPerfTag;
    private final OverScroller mScroller;
    private boolean mSendMomentumEvents;
    private int mSnapInterval;
    private List<Integer> mSnapOffsets;
    private int mSnapToAlignment;
    private boolean mSnapToEnd;
    private boolean mSnapToStart;
    private ReactMCScrollView mTarget;
    ArrayList<ReactMCScrollView> mTargetsArr;
    private final Rect mTempRect;
    private final VelocityHelper mVelocityHelper;
    private int pendingContentOffsetX;
    private int pendingContentOffsetY;

    public ReactMCScrollView(Context context) {
        this(context, null);
    }

    public ReactMCScrollView(Context context, FpsListener fpsListener) {
        super(context);
        this.mOnScrollDispatchHelper = new OnScrollDispatchHelper();
        this.mVelocityHelper = new VelocityHelper();
        this.mRect = new Rect();
        this.mTempRect = new Rect();
        this.mOverflowInset = new Rect();
        this.mOverflow = ViewProps.HIDDEN;
        this.mPagingEnabled = false;
        this.mScrollEnabled = true;
        this.mFpsListener = null;
        this.mEndFillColor = 0;
        this.mDisableIntervalMomentum = false;
        this.mSnapInterval = 0;
        this.mSnapToStart = true;
        this.mSnapToEnd = true;
        this.mSnapToAlignment = 0;
        this.mCurrentContentOffset = null;
        this.pendingContentOffsetX = -1;
        this.pendingContentOffsetY = -1;
        this.mFabricViewStateManager = new FabricViewStateManager();
        this.mReactScrollViewScrollState = new ReactScrollViewHelper.ReactScrollViewScrollState(0);
        this.DEFAULT_FLING_ANIMATOR = ObjectAnimator.ofInt(this, "scrollY", 0, 0);
        this.mPointerEvents = PointerEvents.AUTO;
        this.mLastScrollDispatchTime = 0L;
        this.mScrollEventThrottle = 0;
        this.mMaintainVisibleContentPositionHelper = null;
        this.mTargetsArr = new ArrayList<>();
        this.mParentEnable = true;
        this.mActivePointerId = -1;
        this.mFpsListener = fpsListener;
        this.mReactBackgroundManager = new ReactViewBackgroundManager(this);
        this.mScroller = getOverScrollerFromParent();
        setOnHierarchyChangeListener(this);
        setScrollBarStyle(Const.DOWNLOAD_SEGMENT_THRESTHOD);
        ViewCompat.setAccessibilityDelegate(this, new ReactScrollViewAccessibilityDelegate());
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        String str = (String) getTag(com.facebook.react.R.id.react_test_id);
        if (str != null) {
            accessibilityNodeInfo.setViewIdResourceName(str);
        }
    }

    private OverScroller getOverScrollerFromParent() throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
        if (!sTriedToGetScrollerField) {
            sTriedToGetScrollerField = true;
            try {
                Field declaredField = ScrollView.class.getDeclaredField("mScroller");
                sScrollerField = declaredField;
                declaredField.setAccessible(true);
            } catch (NoSuchFieldException unused) {
                FLog.w(ReactConstants.TAG, "Failed to get mScroller field for ScrollView! This app will exhibit the bounce-back scrolling bug :(");
            }
        }
        Field field = sScrollerField;
        OverScroller overScroller = null;
        if (field != null) {
            try {
                Object obj = field.get(this);
                if (obj instanceof OverScroller) {
                    overScroller = (OverScroller) obj;
                } else {
                    FLog.w(ReactConstants.TAG, "Failed to cast mScroller field in ScrollView (probably due to OEM changes to AOSP)! This app will exhibit the bounce-back scrolling bug :(");
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to get mScroller from ScrollView!", e);
            }
        }
        return overScroller;
    }

    public void setDisableIntervalMomentum(boolean z) {
        this.mDisableIntervalMomentum = z;
    }

    public void setSendMomentumEvents(boolean z) {
        this.mSendMomentumEvents = z;
    }

    public void setScrollPerfTag(String str) {
        this.mScrollPerfTag = str;
    }

    public void setScrollEnabled(boolean z) {
        this.mScrollEnabled = z;
    }

    public boolean getScrollEnabled() {
        return this.mScrollEnabled;
    }

    public void setPagingEnabled(boolean z) {
        this.mPagingEnabled = z;
    }

    public void setDecelerationRate(float f) {
        getReactScrollViewScrollState().setDecelerationRate(f);
        OverScroller overScroller = this.mScroller;
        if (overScroller != null) {
            overScroller.setFriction(1.0f - f);
        }
    }

    public void abortAnimation() {
        OverScroller overScroller = this.mScroller;
        if (overScroller == null || overScroller.isFinished()) {
            return;
        }
        this.mScroller.abortAnimation();
    }

    public void setSnapInterval(int i) {
        this.mSnapInterval = i;
    }

    public void setSnapOffsets(List<Integer> list) {
        this.mSnapOffsets = list;
    }

    public void setSnapToStart(boolean z) {
        this.mSnapToStart = z;
    }

    public void setSnapToEnd(boolean z) {
        this.mSnapToEnd = z;
    }

    public void setSnapToAlignment(int i) {
        this.mSnapToAlignment = i;
    }

    public void flashScrollIndicators() {
        awakenScrollBars();
    }

    public void setOverflow(String str) {
        this.mOverflow = str;
        invalidate();
    }

    @Override // com.facebook.react.uimanager.ReactOverflowView
    public String getOverflow() {
        return this.mOverflow;
    }

    public int dip2px(float f) {
        return (int) ((f * getContext().getResources().getDisplayMetrics().density) + 0.5f);
    }

    public void setMCRole(String str) {
        this.mMCRole = str;
    }

    public void setMCDeltH(String str) {
        this.mMCDeltH = dip2px(Float.parseFloat(str));
    }

    public void setMCId(String str) {
        this.mMCId = str;
        setTag(R.id.mcchildscrollviewtag, this.mMCId);
    }

    public void setMCActiveId(String str) {
        this.mMCActiveId = str;
        findView(this);
    }

    public boolean findView(View view) {
        Boolean bool = false;
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            Boolean boolValueOf = bool;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View childAt = viewGroup.getChildAt(i);
                if (childAt instanceof ReactMCScrollView) {
                    if (childAt.getTag(R.id.mcchildscrollviewtag) != null && childAt.getTag(R.id.mcchildscrollviewtag).equals(this.mMCActiveId)) {
                        this.mTarget = (ReactMCScrollView) childAt;
                        Iterator<ReactMCScrollView> it = this.mTargetsArr.iterator();
                        while (it.hasNext()) {
                            ReactMCScrollView next = it.next();
                            if (next != null && this.mTarget.equals(next)) {
                                bool = true;
                            }
                        }
                        if (!bool.booleanValue()) {
                            this.mTargetsArr.add(this.mTarget);
                        }
                        bool = true;
                    }
                } else {
                    boolValueOf = Boolean.valueOf(findView(childAt));
                    if (boolValueOf.booleanValue()) {
                        break;
                    }
                }
            }
            bool = boolValueOf;
        }
        return bool.booleanValue();
    }

    public boolean canChildScrollUp() {
        return canScrollVertically(-1);
    }

    private void resetChildScrollToTop() {
        Iterator<ReactMCScrollView> it = this.mTargetsArr.iterator();
        while (it.hasNext()) {
            ReactMCScrollView next = it.next();
            if ((next instanceof ReactMCScrollView) && next.getScrollY() != 0) {
                next.scrollTo(getScrollX(), 0);
            }
        }
    }

    @Override // com.facebook.react.uimanager.ReactOverflowViewWithInset
    public void setOverflowInset(int i, int i2, int i3, int i4) {
        this.mOverflowInset.set(i, i2, i3, i4);
    }

    @Override // com.facebook.react.uimanager.ReactOverflowViewWithInset
    public Rect getOverflowInset() {
        return this.mOverflowInset;
    }

    @Override // android.widget.ScrollView, android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        MeasureSpecAssertions.assertExplicitMeasureSpec(i, i2);
        setMeasuredDimension(View.MeasureSpec.getSize(i), View.MeasureSpec.getSize(i2));
    }

    @Override // android.widget.ScrollView, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (isContentReady()) {
            int scrollX = this.pendingContentOffsetX;
            if (scrollX == -1) {
                scrollX = getScrollX();
            }
            int scrollY = this.pendingContentOffsetY;
            if (scrollY == -1) {
                scrollY = getScrollY();
            }
            scrollTo(scrollX, scrollY);
        }
        ReactScrollViewHelper.emitLayoutEvent(this);
    }

    @Override // android.widget.ScrollView, android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (this.mRemoveClippedSubviews) {
            updateClippingRect();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mRemoveClippedSubviews) {
            updateClippingRect();
        }
        MaintainVisibleScrollPositionHelper maintainVisibleScrollPositionHelper = this.mMaintainVisibleContentPositionHelper;
        if (maintainVisibleScrollPositionHelper != null) {
            maintainVisibleScrollPositionHelper.start();
        }
    }

    @Override // android.widget.ScrollView, android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        MaintainVisibleScrollPositionHelper maintainVisibleScrollPositionHelper = this.mMaintainVisibleContentPositionHelper;
        if (maintainVisibleScrollPositionHelper != null) {
            maintainVisibleScrollPositionHelper.stop();
        }
    }

    @Override // android.widget.ScrollView, android.view.ViewGroup, android.view.ViewParent
    public void requestChildFocus(View view, View view2) {
        if (view2 != null) {
            scrollToChild(view2);
        }
        super.requestChildFocus(view, view2);
    }

    private int getScrollDelta(View view) {
        view.getDrawingRect(this.mTempRect);
        offsetDescendantRectToMyCoords(view, this.mTempRect);
        return computeScrollDeltaToGetChildRectOnScreen(this.mTempRect);
    }

    public boolean isPartiallyScrolledInView(View view) {
        int scrollDelta = getScrollDelta(view);
        view.getDrawingRect(this.mTempRect);
        return scrollDelta != 0 && Math.abs(scrollDelta) < this.mTempRect.width();
    }

    private void scrollToChild(View view) {
        Rect rect = new Rect();
        view.getDrawingRect(rect);
        offsetDescendantRectToMyCoords(view, rect);
        int iComputeScrollDeltaToGetChildRectOnScreen = computeScrollDeltaToGetChildRectOnScreen(rect);
        if (iComputeScrollDeltaToGetChildRectOnScreen != 0) {
            scrollBy(0, iComputeScrollDeltaToGetChildRectOnScreen);
        }
    }

    @Override // android.view.View
    protected void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
        this.mActivelyScrolling = true;
        if (this.mOnScrollDispatchHelper.onScrollChanged(i, i2)) {
            if (this.mRemoveClippedSubviews) {
                updateClippingRect();
            }
            ReactScrollViewHelper.updateStateOnScrollChanged(this, this.mOnScrollDispatchHelper.getXFlingVelocity(), this.mOnScrollDispatchHelper.getYFlingVelocity());
        }
    }

    @Override // android.widget.ScrollView, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (!this.mScrollEnabled) {
            return false;
        }
        if (!PointerEvents.canChildrenBeTouchTarget(this.mPointerEvents)) {
            return true;
        }
        try {
            if (super.onInterceptTouchEvent(motionEvent)) {
                return handleInterceptedTouchEvent(motionEvent);
            }
        } catch (IllegalArgumentException e) {
            FLog.w(ReactConstants.TAG, "Error intercepting touch event.", e);
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:54:0x0098  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected boolean handleInterceptedTouchEvent(android.view.MotionEvent r7) {
        /*
            r6 = this;
            int r0 = r7.getAction()
            r0 = r0 & 255(0xff, float:3.57E-43)
            java.lang.String r1 = "parent"
            java.lang.String r2 = r6.mMCRole
            boolean r1 = r1.equals(r2)
            r2 = 1
            if (r1 == 0) goto Lac
            boolean r1 = r6.mParentEnable
            r3 = 0
            if (r1 != 0) goto L1e
            java.lang.String r7 = "zzzz!!!!"
            java.lang.String r0 = "\u7236\u7ea7\u5411\u4e0b\u4f20\u9012"
            android.util.Log.d(r7, r0)
            return r3
        L1e:
            if (r0 == 0) goto L9c
            if (r0 == r2) goto L98
            r1 = 2
            if (r0 == r1) goto L2a
            r1 = 3
            if (r0 == r1) goto L98
            goto Lac
        L2a:
            int r0 = r6.mActivePointerId
            int r0 = r7.findPointerIndex(r0)
            if (r0 >= 0) goto L3a
            java.lang.String r7 = "zzzzz"
            java.lang.String r0 = "Got ACTION_MOVE event but have an invalid active pointer id."
            android.util.Log.e(r7, r0)
            return r3
        L3a:
            float r0 = r7.getY(r0)
            float r1 = r6.mLastMotionY
            float r0 = r0 - r1
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r4 = "\u89e6\u53d1\u4e86\u524d\uff1a"
            r1.<init>(r4)
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            int r5 = r6.getScrollY()
            r4.append(r5)
            java.lang.String r5 = "mMCDeltH::"
            r4.append(r5)
            int r5 = r6.mMCDeltH
            java.lang.String r5 = java.lang.String.valueOf(r5)
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            java.lang.String r4 = java.lang.String.valueOf(r4)
            r1.append(r4)
            java.lang.String r1 = r1.toString()
            java.lang.String r4 = "zzzz666666"
            android.util.Log.d(r4, r1)
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 >= 0) goto Lac
            int r0 = r6.getScrollY()
            int r1 = r6.mMCDeltH
            if (r0 < r1) goto Lac
            java.lang.String r0 = "\u89e6\u53d1\u4e86\uff01\uff01\uff01"
            android.util.Log.d(r4, r0)
            r6.mParentEnable = r3
            int r0 = r7.getAction()
            r7.setAction(r3)
            r6.dispatchTouchEvent(r7)
            r7.setAction(r0)
            return r3
        L98:
            r6.resetChildScrollToTop()
            goto Lac
        L9c:
            int r0 = r7.getActionIndex()
            int r1 = r7.getPointerId(r0)
            r6.mActivePointerId = r1
            float r0 = r7.getY(r0)
            r6.mLastMotionY = r0
        Lac:
            com.facebook.react.uimanager.events.NativeGestureUtil.notifyNativeGestureStarted(r6, r7)
            com.facebook.react.views.scroll.ReactScrollViewHelper.emitScrollBeginDragEvent(r6)
            r6.mDragging = r2
            r6.enableFpsListener()
            android.animation.ValueAnimator r7 = r6.getFlingAnimator()
            r7.cancel()
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.mcscrollview.ReactMCScrollView.handleInterceptedTouchEvent(android.view.MotionEvent):boolean");
    }

    @Override // android.widget.ScrollView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.mScrollEnabled || !PointerEvents.canBeTouchTarget(this.mPointerEvents)) {
            return false;
        }
        this.mVelocityHelper.calculateVelocity(motionEvent);
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 1 && this.mDragging) {
            ReactScrollViewHelper.updateFabricScrollState(this);
            float xVelocity = this.mVelocityHelper.getXVelocity();
            float yVelocity = this.mVelocityHelper.getYVelocity();
            ReactScrollViewHelper.emitScrollEndDragEvent(this, xVelocity, yVelocity);
            NativeGestureUtil.notifyNativeGestureEnded(this, motionEvent);
            this.mDragging = false;
            handlePostTouchScrolling(Math.round(xVelocity), Math.round(yVelocity));
        }
        if (actionMasked == 0) {
            cancelPostTouchScrolling();
        }
        return super.onTouchEvent(motionEvent);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        String str;
        if ("parent".equals(this.mMCRole)) {
            int action = motionEvent.getAction() & 255;
            if (action == 0) {
                int actionIndex = motionEvent.getActionIndex();
                this.mActivePointerId = motionEvent.getPointerId(actionIndex);
                this.mLastMotionY = motionEvent.getY(actionIndex);
            } else if (action == 2) {
                int iFindPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                if (iFindPointerIndex < 0) {
                    Log.e("zzzzz", "Got ACTION_MOVE event but have an invalid active pointer id.");
                    return false;
                }
                float y = motionEvent.getY(iFindPointerIndex) - this.mLastMotionY;
                if (this.mParentEnable) {
                    StringBuilder sb = new StringBuilder("dispatchTouchEvent \u89e6\u53d1\u4e86\u524d\uff1a");
                    sb.append(String.valueOf(getScrollY() + "mMCDeltH::" + String.valueOf(this.mMCDeltH)));
                    Log.d("zzzz666666", sb.toString());
                    if (y < 0.0f && getScrollY() >= this.mMCDeltH) {
                        Log.d("zzzz666666", "dispatchTouchEvent\u89e6\u53d1\u4e86\uff01\uff01\uff01");
                        this.mParentEnable = false;
                        int action2 = motionEvent.getAction();
                        motionEvent.setAction(0);
                        dispatchTouchEvent(motionEvent);
                        motionEvent.setAction(action2);
                        return false;
                    }
                } else {
                    ReactMCScrollView reactMCScrollView = this.mTarget;
                    Boolean boolValueOf = Boolean.valueOf(reactMCScrollView != null || (reactMCScrollView == null && findView(this)));
                    StringBuilder sb2 = new StringBuilder("ACTION_MOVE \u6253\u5f00\u7236\u7ea7\u524d\uff01\uff01\uff01");
                    if (boolValueOf.booleanValue()) {
                        str = "getScrollY" + String.valueOf(this.mTarget.getScrollY());
                    } else {
                        str = "\u627e\u4e0d\u5230\u5b50\u96c6";
                    }
                    sb2.append(str);
                    Log.d("zzzzz dispatchTouchEvent", sb2.toString());
                    if (y > 0.0f && boolValueOf.booleanValue() && this.mTarget.getScrollY() == 0) {
                        Log.d("zzzzz dispatchTouchEvent", "\u6253\u5f00\u7236\u7ea7\uff01\uff01\uff01");
                        this.mParentEnable = true;
                        int action3 = motionEvent.getAction();
                        motionEvent.setAction(0);
                        dispatchTouchEvent(motionEvent);
                        motionEvent.setAction(action3);
                        return false;
                    }
                }
            }
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchGenericPointerEvent(MotionEvent motionEvent) {
        if (PointerEvents.canChildrenBeTouchTarget(this.mPointerEvents)) {
            return super.dispatchGenericPointerEvent(motionEvent);
        }
        return false;
    }

    @Override // android.widget.ScrollView
    public boolean executeKeyEvent(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        if (this.mScrollEnabled || !(keyCode == 19 || keyCode == 20)) {
            return super.executeKeyEvent(keyEvent);
        }
        return false;
    }

    @Override // com.facebook.react.uimanager.ReactClippingViewGroup
    public void setRemoveClippedSubviews(boolean z) {
        if (z && this.mClippingRect == null) {
            this.mClippingRect = new Rect();
        }
        this.mRemoveClippedSubviews = z;
        updateClippingRect();
    }

    @Override // com.facebook.react.uimanager.ReactClippingViewGroup
    public boolean getRemoveClippedSubviews() {
        return this.mRemoveClippedSubviews;
    }

    @Override // com.facebook.react.uimanager.ReactClippingViewGroup
    public void updateClippingRect() {
        if (this.mRemoveClippedSubviews) {
            Assertions.assertNotNull(this.mClippingRect);
            ReactClippingViewGroupHelper.calculateClippingRect(this, this.mClippingRect);
            KeyEvent.Callback childAt = getChildAt(0);
            if (childAt instanceof ReactClippingViewGroup) {
                ((ReactClippingViewGroup) childAt).updateClippingRect();
            }
        }
    }

    @Override // com.facebook.react.uimanager.ReactClippingViewGroup
    public void getClippingRect(Rect rect) {
        rect.set((Rect) Assertions.assertNotNull(this.mClippingRect));
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean getChildVisibleRect(View view, Rect rect, Point point) {
        return super.getChildVisibleRect(view, rect, point);
    }

    @Override // android.widget.ScrollView
    public void fling(int i) {
        int iCorrectFlingVelocityY = correctFlingVelocityY(i);
        if (this.mPagingEnabled) {
            flingAndSnap(iCorrectFlingVelocityY);
        } else if (this.mScroller != null) {
            this.mScroller.fling(getScrollX(), getScrollY(), 0, iCorrectFlingVelocityY, 0, 0, 0, Integer.MAX_VALUE, 0, ((getHeight() - getPaddingBottom()) - getPaddingTop()) / 2);
            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            super.fling(iCorrectFlingVelocityY);
        }
        handlePostTouchScrolling(0, iCorrectFlingVelocityY);
    }

    private int correctFlingVelocityY(int i) {
        if (Build.VERSION.SDK_INT != 28) {
            return i;
        }
        float fSignum = Math.signum(this.mOnScrollDispatchHelper.getYFlingVelocity());
        if (fSignum == 0.0f) {
            fSignum = Math.signum(i);
        }
        return (int) (Math.abs(i) * fSignum);
    }

    private void enableFpsListener() {
        if (isScrollPerfLoggingEnabled()) {
            Assertions.assertNotNull(this.mFpsListener);
            Assertions.assertNotNull(this.mScrollPerfTag);
            this.mFpsListener.enable(this.mScrollPerfTag);
        }
    }

    public void disableFpsListener() {
        if (isScrollPerfLoggingEnabled()) {
            Assertions.assertNotNull(this.mFpsListener);
            Assertions.assertNotNull(this.mScrollPerfTag);
            this.mFpsListener.disable(this.mScrollPerfTag);
        }
    }

    private boolean isScrollPerfLoggingEnabled() {
        String str;
        return (this.mFpsListener == null || (str = this.mScrollPerfTag) == null || str.isEmpty()) ? false : true;
    }

    private int getMaxScrollY() {
        return Math.max(0, this.mContentView.getHeight() - ((getHeight() - getPaddingBottom()) - getPaddingTop()));
    }

    @Override // android.widget.ScrollView, android.view.View
    public void draw(Canvas canvas) {
        if (this.mEndFillColor != 0) {
            View childAt = getChildAt(0);
            if (this.mEndBackground != null && childAt != null && childAt.getBottom() < getHeight()) {
                this.mEndBackground.setBounds(0, childAt.getBottom(), getWidth(), getHeight());
                this.mEndBackground.draw(canvas);
            }
        }
        getDrawingRect(this.mRect);
        String str = this.mOverflow;
        str.hashCode();
        if (!str.equals(ViewProps.VISIBLE)) {
            canvas.clipRect(this.mRect);
        }
        super.draw(canvas);
    }

    private void handlePostTouchScrolling(int i, int i2) {
        if (this.mPostTouchRunnable != null) {
            return;
        }
        if (this.mSendMomentumEvents) {
            enableFpsListener();
            ReactScrollViewHelper.emitScrollMomentumBeginEvent(this, i, i2);
        }
        this.mActivelyScrolling = false;
        AnonymousClass1 anonymousClass1 = new Runnable() { // from class: com.netease.mcscrollview.ReactMCScrollView.1
            private boolean mSnappingToPage = false;
            private boolean mRunning = true;
            private int mStableFrames = 0;

            AnonymousClass1() {
            }

            @Override // java.lang.Runnable
            public void run() {
                if (ReactMCScrollView.this.mActivelyScrolling) {
                    ReactMCScrollView.this.mActivelyScrolling = false;
                    this.mStableFrames = 0;
                    this.mRunning = true;
                } else {
                    ReactScrollViewHelper.updateFabricScrollState(ReactMCScrollView.this);
                    int i3 = this.mStableFrames + 1;
                    this.mStableFrames = i3;
                    this.mRunning = i3 < 3;
                    if (ReactMCScrollView.this.mPagingEnabled && !this.mSnappingToPage) {
                        this.mSnappingToPage = true;
                        ReactMCScrollView.this.flingAndSnap(0);
                        ViewCompat.postOnAnimationDelayed(ReactMCScrollView.this, this, 20L);
                    } else {
                        if (ReactMCScrollView.this.mSendMomentumEvents) {
                            ReactScrollViewHelper.emitScrollMomentumEndEvent(ReactMCScrollView.this);
                        }
                        ReactMCScrollView.this.disableFpsListener();
                    }
                }
                if (this.mRunning) {
                    ViewCompat.postOnAnimationDelayed(ReactMCScrollView.this, this, 20L);
                } else {
                    ReactMCScrollView.this.mPostTouchRunnable = null;
                }
            }
        };
        this.mPostTouchRunnable = anonymousClass1;
        ViewCompat.postOnAnimationDelayed(this, anonymousClass1, 20L);
    }

    /* renamed from: com.netease.mcscrollview.ReactMCScrollView$1 */
    class AnonymousClass1 implements Runnable {
        private boolean mSnappingToPage = false;
        private boolean mRunning = true;
        private int mStableFrames = 0;

        AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (ReactMCScrollView.this.mActivelyScrolling) {
                ReactMCScrollView.this.mActivelyScrolling = false;
                this.mStableFrames = 0;
                this.mRunning = true;
            } else {
                ReactScrollViewHelper.updateFabricScrollState(ReactMCScrollView.this);
                int i3 = this.mStableFrames + 1;
                this.mStableFrames = i3;
                this.mRunning = i3 < 3;
                if (ReactMCScrollView.this.mPagingEnabled && !this.mSnappingToPage) {
                    this.mSnappingToPage = true;
                    ReactMCScrollView.this.flingAndSnap(0);
                    ViewCompat.postOnAnimationDelayed(ReactMCScrollView.this, this, 20L);
                } else {
                    if (ReactMCScrollView.this.mSendMomentumEvents) {
                        ReactScrollViewHelper.emitScrollMomentumEndEvent(ReactMCScrollView.this);
                    }
                    ReactMCScrollView.this.disableFpsListener();
                }
            }
            if (this.mRunning) {
                ViewCompat.postOnAnimationDelayed(ReactMCScrollView.this, this, 20L);
            } else {
                ReactMCScrollView.this.mPostTouchRunnable = null;
            }
        }
    }

    private void cancelPostTouchScrolling() {
        Runnable runnable = this.mPostTouchRunnable;
        if (runnable != null) {
            removeCallbacks(runnable);
            this.mPostTouchRunnable = null;
            getFlingAnimator().cancel();
        }
    }

    private int predictFinalScrollPosition(int i) {
        if (getFlingAnimator() == this.DEFAULT_FLING_ANIMATOR) {
            return ReactScrollViewHelper.predictFinalScrollPosition(this, 0, i, 0, getMaxScrollY()).y;
        }
        return getFlingExtrapolatedDistance(i) + ReactScrollViewHelper.getNextFlingStartValue(this, getScrollY(), getReactScrollViewScrollState().getFinalAnimatedPositionScroll().y, i);
    }

    private View getContentView() {
        return getChildAt(0);
    }

    private void smoothScrollAndSnap(int i) {
        double snapInterval = getSnapInterval();
        double nextFlingStartValue = ReactScrollViewHelper.getNextFlingStartValue(this, getScrollY(), getReactScrollViewScrollState().getFinalAnimatedPositionScroll().y, i);
        double dPredictFinalScrollPosition = predictFinalScrollPosition(i);
        double d = nextFlingStartValue / snapInterval;
        int iFloor = (int) Math.floor(d);
        int iCeil = (int) Math.ceil(d);
        int iRound = (int) Math.round(d);
        int iRound2 = (int) Math.round(dPredictFinalScrollPosition / snapInterval);
        if (i > 0 && iCeil == iFloor) {
            iCeil++;
        } else if (i < 0 && iFloor == iCeil) {
            iFloor--;
        }
        if (i > 0 && iRound < iCeil && iRound2 > iFloor) {
            iRound = iCeil;
        } else if (i < 0 && iRound > iFloor && iRound2 < iCeil) {
            iRound = iFloor;
        }
        double d2 = iRound * snapInterval;
        if (d2 != nextFlingStartValue) {
            this.mActivelyScrolling = true;
            reactSmoothScrollTo(getScrollX(), (int) d2);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:204:0x0183  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void flingAndSnap(int r28) {
        /*
            Method dump skipped, instructions count: 501
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.mcscrollview.ReactMCScrollView.flingAndSnap(int):void");
    }

    private int getItemStartOffset(int i, int i2, int i3, int i4) {
        int i5;
        if (i == 1) {
            return i2;
        }
        if (i == 2) {
            i5 = (i4 - i3) / 2;
        } else {
            if (i != 3) {
                throw new IllegalStateException("Invalid SnapToAlignment value: " + this.mSnapToAlignment);
            }
            i5 = i4 - i3;
        }
        return i2 - i5;
    }

    private int getSnapInterval() {
        int i = this.mSnapInterval;
        return i != 0 ? i : getHeight();
    }

    public void setEndFillColor(int i) {
        if (i != this.mEndFillColor) {
            this.mEndFillColor = i;
            this.mEndBackground = new ColorDrawable(this.mEndFillColor);
        }
    }

    @Override // android.widget.ScrollView, android.view.View
    protected void onOverScrolled(int i, int i2, boolean z, boolean z2) {
        int maxScrollY;
        OverScroller overScroller = this.mScroller;
        if (overScroller != null && this.mContentView != null && !overScroller.isFinished() && this.mScroller.getCurrY() != this.mScroller.getFinalY() && i2 >= (maxScrollY = getMaxScrollY())) {
            this.mScroller.abortAnimation();
            i2 = maxScrollY;
        }
        super.onOverScrolled(i, i2, z, z2);
    }

    @Override // android.view.ViewGroup.OnHierarchyChangeListener
    public void onChildViewAdded(View view, View view2) {
        this.mContentView = view2;
        view2.addOnLayoutChangeListener(this);
    }

    @Override // android.view.ViewGroup.OnHierarchyChangeListener
    public void onChildViewRemoved(View view, View view2) {
        this.mContentView.removeOnLayoutChangeListener(this);
        this.mContentView = null;
    }

    public void setContentOffset(ReadableMap readableMap) {
        ReadableMap readableMap2 = this.mCurrentContentOffset;
        if (readableMap2 == null || !readableMap2.equals(readableMap)) {
            this.mCurrentContentOffset = readableMap;
            if (readableMap != null) {
                scrollTo((int) PixelUtil.toPixelFromDIP(readableMap.hasKey(EllipticCurveJsonWebKey.X_MEMBER_NAME) ? readableMap.getDouble(EllipticCurveJsonWebKey.X_MEMBER_NAME) : 0.0d), (int) PixelUtil.toPixelFromDIP(readableMap.hasKey(EllipticCurveJsonWebKey.Y_MEMBER_NAME) ? readableMap.getDouble(EllipticCurveJsonWebKey.Y_MEMBER_NAME) : 0.0d));
            } else {
                scrollTo(0, 0);
            }
        }
    }

    @Override // com.facebook.react.views.scroll.ReactScrollViewHelper.HasSmoothScroll
    public void reactSmoothScrollTo(int i, int i2) {
        ReactScrollViewHelper.smoothScrollTo(this, i, i2);
        setPendingContentOffsets(i, i2);
    }

    @Override // android.widget.ScrollView, android.view.View
    public void scrollTo(int i, int i2) {
        super.scrollTo(i, i2);
        ReactScrollViewHelper.updateFabricScrollState(this);
        setPendingContentOffsets(i, i2);
    }

    private boolean isContentReady() {
        View contentView = getContentView();
        return (contentView == null || contentView.getWidth() == 0 || contentView.getHeight() == 0) ? false : true;
    }

    private void setPendingContentOffsets(int i, int i2) {
        if (isContentReady()) {
            this.pendingContentOffsetX = -1;
            this.pendingContentOffsetY = -1;
        } else {
            this.pendingContentOffsetX = i;
            this.pendingContentOffsetY = i2;
        }
    }

    @Override // android.view.View.OnLayoutChangeListener
    public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        if (this.mContentView == null) {
            return;
        }
        MaintainVisibleScrollPositionHelper maintainVisibleScrollPositionHelper = this.mMaintainVisibleContentPositionHelper;
        if (maintainVisibleScrollPositionHelper != null) {
            maintainVisibleScrollPositionHelper.updateScrollPosition();
        }
        if (isShown() && isContentReady()) {
            int scrollY = getScrollY();
            int maxScrollY = getMaxScrollY();
            if (scrollY > maxScrollY) {
                scrollTo(getScrollX(), maxScrollY);
            }
        }
    }

    @Override // android.view.View
    public void setBackgroundColor(int i) {
        this.mReactBackgroundManager.setBackgroundColor(i);
    }

    public void setBorderWidth(int i, float f) {
        this.mReactBackgroundManager.setBorderWidth(i, f);
    }

    public void setBorderColor(int i, float f, float f2) {
        this.mReactBackgroundManager.setBorderColor(i, f, f2);
    }

    public void setBorderRadius(float f) {
        this.mReactBackgroundManager.setBorderRadius(f);
    }

    public void setBorderRadius(float f, int i) {
        this.mReactBackgroundManager.setBorderRadius(f, i);
    }

    public void setBorderStyle(String str) {
        this.mReactBackgroundManager.setBorderStyle(str);
    }

    public void setScrollAwayTopPaddingEnabledUnstable(int i) {
        int childCount = getChildCount();
        Assertions.assertCondition(childCount == 1, "React Native ScrollView always has exactly 1 child; a content View");
        if (childCount > 0) {
            for (int i2 = 0; i2 < childCount; i2++) {
                getChildAt(i2).setTranslationY(i);
            }
            setPadding(0, 0, 0, i);
        }
        updateScrollAwayState(i);
        setRemoveClippedSubviews(this.mRemoveClippedSubviews);
    }

    private void updateScrollAwayState(int i) {
        getReactScrollViewScrollState().setScrollAwayPaddingTop(i);
        ReactScrollViewHelper.forceUpdateState(this);
    }

    @Override // com.facebook.react.uimanager.FabricViewStateManager.HasFabricViewStateManager
    public FabricViewStateManager getFabricViewStateManager() {
        return this.mFabricViewStateManager;
    }

    @Override // com.facebook.react.views.scroll.ReactScrollViewHelper.HasScrollState
    public ReactScrollViewHelper.ReactScrollViewScrollState getReactScrollViewScrollState() {
        return this.mReactScrollViewScrollState;
    }

    @Override // com.facebook.react.views.scroll.ReactScrollViewHelper.HasFlingAnimator
    public void startFlingAnimator(int i, int i2) {
        this.DEFAULT_FLING_ANIMATOR.cancel();
        this.DEFAULT_FLING_ANIMATOR.setDuration(ReactScrollViewHelper.getDefaultScrollAnimationDuration(getContext())).setIntValues(i, i2);
        this.DEFAULT_FLING_ANIMATOR.start();
    }

    @Override // com.facebook.react.views.scroll.ReactScrollViewHelper.HasFlingAnimator
    public ValueAnimator getFlingAnimator() {
        return this.DEFAULT_FLING_ANIMATOR;
    }

    @Override // com.facebook.react.views.scroll.ReactScrollViewHelper.HasFlingAnimator
    public int getFlingExtrapolatedDistance(int i) {
        return ReactScrollViewHelper.predictFinalScrollPosition(this, 0, i, 0, getMaxScrollY()).y;
    }

    public void setPointerEvents(PointerEvents pointerEvents) {
        this.mPointerEvents = pointerEvents;
    }

    public PointerEvents getPointerEvents() {
        return this.mPointerEvents;
    }

    @Override // com.facebook.react.views.scroll.ReactScrollViewHelper.HasScrollEventThrottle
    public void setScrollEventThrottle(int i) {
        this.mScrollEventThrottle = i;
    }

    @Override // com.facebook.react.views.scroll.ReactScrollViewHelper.HasScrollEventThrottle
    public int getScrollEventThrottle() {
        return this.mScrollEventThrottle;
    }

    @Override // com.facebook.react.views.scroll.ReactScrollViewHelper.HasScrollEventThrottle
    public void setLastScrollDispatchTime(long j) {
        this.mLastScrollDispatchTime = j;
    }

    @Override // com.facebook.react.views.scroll.ReactScrollViewHelper.HasScrollEventThrottle
    public long getLastScrollDispatchTime() {
        return this.mLastScrollDispatchTime;
    }
}