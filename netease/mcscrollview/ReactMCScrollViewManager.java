package com.netease.mcscrollview;

import android.view.View;
import androidx.core.view.ViewCompat;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.RetryableMountingLayerException;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.PointerEvents;
import com.facebook.react.uimanager.ReactClippingViewGroupHelper;
import com.facebook.react.uimanager.ReactStylesDiffMap;
import com.facebook.react.uimanager.StateWrapper;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.ViewProps;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.annotations.ReactPropGroup;
import com.facebook.react.views.scroll.FpsListener;
import com.facebook.react.views.scroll.ReactScrollViewCommandHelper;
import com.facebook.react.views.scroll.ReactScrollViewHelper;
import com.facebook.react.views.scroll.ScrollEventType;
import com.facebook.yoga.YogaConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@ReactModule(name = ReactMCScrollViewManager.REACT_CLASS)
/* loaded from: classes4.dex */
public class ReactMCScrollViewManager extends ViewGroupManager<ReactMCScrollView> implements ReactScrollViewCommandHelper.ScrollCommandHandler<ReactMCScrollView> {
    public static final String REACT_CLASS = "RCTMCScrollView";
    private static final int[] SPACING_TYPES = {8, 0, 2, 1, 3};
    private FpsListener mFpsListener;

    @Override // com.facebook.react.uimanager.ViewManager, com.facebook.react.bridge.NativeModule
    public String getName() {
        return REACT_CLASS;
    }

    public ReactMCScrollViewManager() {
        this(null);
    }

    public ReactMCScrollViewManager(FpsListener fpsListener) {
        this.mFpsListener = fpsListener;
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public ReactMCScrollView createViewInstance(ThemedReactContext themedReactContext) {
        return new ReactMCScrollView(themedReactContext, this.mFpsListener);
    }

    @ReactProp(name = "MCRole")
    public void setMCRole(ReactMCScrollView reactMCScrollView, String str) {
        reactMCScrollView.setMCRole(str);
    }

    @ReactProp(name = "MCDeltH")
    public void setMCDeltH(ReactMCScrollView reactMCScrollView, String str) {
        reactMCScrollView.setMCDeltH(str);
    }

    @ReactProp(name = "MCId")
    public void setMCId(ReactMCScrollView reactMCScrollView, String str) {
        reactMCScrollView.setMCId(str);
    }

    @ReactProp(name = "MCActiveId")
    public void setMCActiveId(ReactMCScrollView reactMCScrollView, String str) {
        reactMCScrollView.setMCActiveId(str);
    }

    @ReactProp(defaultBoolean = true, name = "scrollEnabled")
    public void setScrollEnabled(ReactMCScrollView reactMCScrollView, boolean z) {
        reactMCScrollView.setScrollEnabled(z);
        reactMCScrollView.setFocusable(z);
    }

    @ReactProp(name = "showsVerticalScrollIndicator")
    public void setShowsVerticalScrollIndicator(ReactMCScrollView reactMCScrollView, boolean z) {
        reactMCScrollView.setVerticalScrollBarEnabled(z);
    }

    @ReactProp(name = "decelerationRate")
    public void setDecelerationRate(ReactMCScrollView reactMCScrollView, float f) {
        reactMCScrollView.setDecelerationRate(f);
    }

    @ReactProp(name = "disableIntervalMomentum")
    public void setDisableIntervalMomentum(ReactMCScrollView reactMCScrollView, boolean z) {
        reactMCScrollView.setDisableIntervalMomentum(z);
    }

    @ReactProp(name = "snapToInterval")
    public void setSnapToInterval(ReactMCScrollView reactMCScrollView, float f) {
        reactMCScrollView.setSnapInterval((int) (f * PixelUtil.getDisplayMetricDensity()));
    }

    @ReactProp(name = "snapToOffsets")
    public void setSnapToOffsets(ReactMCScrollView reactMCScrollView, ReadableArray readableArray) {
        if (readableArray == null || readableArray.size() == 0) {
            reactMCScrollView.setSnapOffsets(null);
            return;
        }
        float displayMetricDensity = PixelUtil.getDisplayMetricDensity();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < readableArray.size(); i++) {
            arrayList.add(Integer.valueOf((int) (readableArray.getDouble(i) * displayMetricDensity)));
        }
        reactMCScrollView.setSnapOffsets(arrayList);
    }

    @ReactProp(name = "snapToAlignment")
    public void setSnapToAlignment(ReactMCScrollView reactMCScrollView, String str) {
        reactMCScrollView.setSnapToAlignment(ReactScrollViewHelper.parseSnapToAlignment(str));
    }

    @ReactProp(name = "snapToStart")
    public void setSnapToStart(ReactMCScrollView reactMCScrollView, boolean z) {
        reactMCScrollView.setSnapToStart(z);
    }

    @ReactProp(name = "snapToEnd")
    public void setSnapToEnd(ReactMCScrollView reactMCScrollView, boolean z) {
        reactMCScrollView.setSnapToEnd(z);
    }

    @ReactProp(name = ReactClippingViewGroupHelper.PROP_REMOVE_CLIPPED_SUBVIEWS)
    public void setRemoveClippedSubviews(ReactMCScrollView reactMCScrollView, boolean z) {
        reactMCScrollView.setRemoveClippedSubviews(z);
    }

    @ReactProp(name = "sendMomentumEvents")
    public void setSendMomentumEvents(ReactMCScrollView reactMCScrollView, boolean z) {
        reactMCScrollView.setSendMomentumEvents(z);
    }

    @ReactProp(name = "scrollPerfTag")
    public void setScrollPerfTag(ReactMCScrollView reactMCScrollView, String str) {
        reactMCScrollView.setScrollPerfTag(str);
    }

    @ReactProp(name = "pagingEnabled")
    public void setPagingEnabled(ReactMCScrollView reactMCScrollView, boolean z) {
        reactMCScrollView.setPagingEnabled(z);
    }

    @ReactProp(customType = "Color", defaultInt = 0, name = "endFillColor")
    public void setBottomFillColor(ReactMCScrollView reactMCScrollView, int i) {
        reactMCScrollView.setEndFillColor(i);
    }

    @ReactProp(name = "overScrollMode")
    public void setOverScrollMode(ReactMCScrollView reactMCScrollView, String str) {
        reactMCScrollView.setOverScrollMode(ReactScrollViewHelper.parseOverScrollMode(str));
    }

    @ReactProp(name = "nestedScrollEnabled")
    public void setNestedScrollEnabled(ReactMCScrollView reactMCScrollView, boolean z) {
        ViewCompat.setNestedScrollingEnabled(reactMCScrollView, z);
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public Map<String, Integer> getCommandsMap() {
        return ReactScrollViewCommandHelper.getCommandsMap();
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public void receiveCommand(ReactMCScrollView reactMCScrollView, int i, ReadableArray readableArray) {
        ReactScrollViewCommandHelper.receiveCommand(this, reactMCScrollView, i, readableArray);
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public void receiveCommand(ReactMCScrollView reactMCScrollView, String str, ReadableArray readableArray) {
        ReactScrollViewCommandHelper.receiveCommand(this, reactMCScrollView, str, readableArray);
    }

    @Override // com.facebook.react.views.scroll.ReactScrollViewCommandHelper.ScrollCommandHandler
    public void flashScrollIndicators(ReactMCScrollView reactMCScrollView) {
        reactMCScrollView.flashScrollIndicators();
    }

    @Override // com.facebook.react.views.scroll.ReactScrollViewCommandHelper.ScrollCommandHandler
    public void scrollTo(ReactMCScrollView reactMCScrollView, ReactScrollViewCommandHelper.ScrollToCommandData scrollToCommandData) {
        reactMCScrollView.abortAnimation();
        if (scrollToCommandData.mAnimated) {
            reactMCScrollView.reactSmoothScrollTo(scrollToCommandData.mDestX, scrollToCommandData.mDestY);
        } else {
            reactMCScrollView.scrollTo(scrollToCommandData.mDestX, scrollToCommandData.mDestY);
        }
    }

    @ReactPropGroup(defaultFloat = Float.NaN, names = {ViewProps.BORDER_RADIUS, ViewProps.BORDER_TOP_LEFT_RADIUS, ViewProps.BORDER_TOP_RIGHT_RADIUS, ViewProps.BORDER_BOTTOM_RIGHT_RADIUS, ViewProps.BORDER_BOTTOM_LEFT_RADIUS})
    public void setBorderRadius(ReactMCScrollView reactMCScrollView, int i, float f) {
        if (!YogaConstants.isUndefined(f)) {
            f = PixelUtil.toPixelFromDIP(f);
        }
        if (i == 0) {
            reactMCScrollView.setBorderRadius(f);
        } else {
            reactMCScrollView.setBorderRadius(f, i - 1);
        }
    }

    @ReactProp(name = "borderStyle")
    public void setBorderStyle(ReactMCScrollView reactMCScrollView, String str) {
        reactMCScrollView.setBorderStyle(str);
    }

    @ReactPropGroup(defaultFloat = Float.NaN, names = {ViewProps.BORDER_WIDTH, ViewProps.BORDER_LEFT_WIDTH, ViewProps.BORDER_RIGHT_WIDTH, ViewProps.BORDER_TOP_WIDTH, ViewProps.BORDER_BOTTOM_WIDTH})
    public void setBorderWidth(ReactMCScrollView reactMCScrollView, int i, float f) {
        if (!YogaConstants.isUndefined(f)) {
            f = PixelUtil.toPixelFromDIP(f);
        }
        reactMCScrollView.setBorderWidth(SPACING_TYPES[i], f);
    }

    @ReactPropGroup(customType = "Color", names = {ViewProps.BORDER_COLOR, ViewProps.BORDER_LEFT_COLOR, ViewProps.BORDER_RIGHT_COLOR, ViewProps.BORDER_TOP_COLOR, ViewProps.BORDER_BOTTOM_COLOR})
    public void setBorderColor(ReactMCScrollView reactMCScrollView, int i, Integer num) {
        reactMCScrollView.setBorderColor(SPACING_TYPES[i], num == null ? Float.NaN : num.intValue() & ViewCompat.MEASURED_SIZE_MASK, num != null ? num.intValue() >>> 24 : Float.NaN);
    }

    @ReactProp(name = ViewProps.OVERFLOW)
    public void setOverflow(ReactMCScrollView reactMCScrollView, String str) {
        reactMCScrollView.setOverflow(str);
    }

    @Override // com.facebook.react.views.scroll.ReactScrollViewCommandHelper.ScrollCommandHandler
    public void scrollToEnd(ReactMCScrollView reactMCScrollView, ReactScrollViewCommandHelper.ScrollToEndCommandData scrollToEndCommandData) {
        View childAt = reactMCScrollView.getChildAt(0);
        if (childAt == null) {
            throw new RetryableMountingLayerException("scrollToEnd called on ScrollView without child");
        }
        int height = childAt.getHeight() + reactMCScrollView.getPaddingBottom();
        if (scrollToEndCommandData.mAnimated) {
            reactMCScrollView.reactSmoothScrollTo(reactMCScrollView.getScrollX(), height);
        } else {
            reactMCScrollView.scrollTo(reactMCScrollView.getScrollX(), height);
        }
    }

    @ReactProp(name = "persistentScrollbar")
    public void setPersistentScrollbar(ReactMCScrollView reactMCScrollView, boolean z) {
        reactMCScrollView.setScrollbarFadingEnabled(!z);
    }

    @ReactProp(name = "fadingEdgeLength")
    public void setFadingEdgeLength(ReactMCScrollView reactMCScrollView, int i) {
        if (i > 0) {
            reactMCScrollView.setVerticalFadingEdgeEnabled(true);
            reactMCScrollView.setFadingEdgeLength(i);
        } else {
            reactMCScrollView.setVerticalFadingEdgeEnabled(false);
            reactMCScrollView.setFadingEdgeLength(0);
        }
    }

    @ReactProp(customType = "Point", name = "contentOffset")
    public void setContentOffset(ReactMCScrollView reactMCScrollView, ReadableMap readableMap) {
        reactMCScrollView.setContentOffset(readableMap);
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public Object updateState(ReactMCScrollView reactMCScrollView, ReactStylesDiffMap reactStylesDiffMap, StateWrapper stateWrapper) {
        reactMCScrollView.getFabricViewStateManager().setStateWrapper(stateWrapper);
        return null;
    }

    @Override // com.facebook.react.uimanager.BaseViewManager, com.facebook.react.uimanager.ViewManager
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        Map<String, Object> exportedCustomDirectEventTypeConstants = super.getExportedCustomDirectEventTypeConstants();
        if (exportedCustomDirectEventTypeConstants == null) {
            exportedCustomDirectEventTypeConstants = new HashMap<>();
        }
        exportedCustomDirectEventTypeConstants.putAll(createExportedCustomDirectEventTypeConstants());
        return exportedCustomDirectEventTypeConstants;
    }

    public static Map<String, Object> createExportedCustomDirectEventTypeConstants() {
        return MapBuilder.builder().put(ScrollEventType.getJSEventName(ScrollEventType.SCROLL), MapBuilder.of("registrationName", "onScroll")).put(ScrollEventType.getJSEventName(ScrollEventType.BEGIN_DRAG), MapBuilder.of("registrationName", "onScrollBeginDrag")).put(ScrollEventType.getJSEventName(ScrollEventType.END_DRAG), MapBuilder.of("registrationName", "onScrollEndDrag")).put(ScrollEventType.getJSEventName(ScrollEventType.MOMENTUM_BEGIN), MapBuilder.of("registrationName", "onMomentumScrollBegin")).put(ScrollEventType.getJSEventName(ScrollEventType.MOMENTUM_END), MapBuilder.of("registrationName", "onMomentumScrollEnd")).build();
    }

    @ReactProp(name = ViewProps.POINTER_EVENTS)
    public void setPointerEvents(ReactMCScrollView reactMCScrollView, String str) {
        reactMCScrollView.setPointerEvents(PointerEvents.parsePointerEvents(str));
    }

    @ReactProp(name = "scrollEventThrottle")
    public void setScrollEventThrottle(ReactMCScrollView reactMCScrollView, int i) {
        reactMCScrollView.setScrollEventThrottle(i);
    }

    @ReactProp(name = "isInvertedVirtualizedList")
    public void setIsInvertedVirtualizedList(ReactMCScrollView reactMCScrollView, boolean z) {
        if (z) {
            reactMCScrollView.setVerticalScrollbarPosition(1);
        } else {
            reactMCScrollView.setVerticalScrollbarPosition(0);
        }
    }
}