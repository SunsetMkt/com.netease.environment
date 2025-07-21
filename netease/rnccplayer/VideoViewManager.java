package com.netease.rnccplayer;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import java.util.Map;
import org.json.JSONException;

/* loaded from: classes4.dex */
public class VideoViewManager extends SimpleViewManager<VideoView> {
    public static final int COMMAND_QUIT_FULLSCREEN = 1;
    public static final String PROP_AUTO_HIDDEN_CONTROL_VIEW = "autoHiddenControlView";
    public static final String PROP_AUTO_PLAY = "autoPlay";
    public static final String PROP_CLIENT_TYPE = "clientType";
    public static final String PROP_EXTRA_LOG = "extraLog";
    public static final String PROP_FROM = "from";
    public static final String PROP_FULLSCREEN = "fullScreen";
    public static final String PROP_GAME_UID = "gameUid";
    public static final String PROP_LIVE = "live";
    public static final String PROP_MEDIA_HARD_DECODE = "hardDecode";
    public static final String PROP_NEED_FULL_SCREEN = "needFullScreen";
    public static final String PROP_NEED_SYSTEM_CONTROLLER = "needSystemController";
    public static final String PROP_PAUSED = "paused";
    public static final String PROP_PLAY = "play";
    public static final String PROP_RESIZE_MODE = "resizeMode";
    public static final String PROP_SEEK_TO_POSITION = "seekToPosition";
    public static final String PROP_SID = "sid";
    public static final String PROP_SRC = "src";
    public static final String PROP_STOP = "stop";
    public static final String PROP_URS = "urs";
    public static final String PROP_VBR = "vbr";
    public static final String REACT_CLASS = "RCTVideoView";
    private VideoView videoView;

    @Override // com.facebook.react.uimanager.ViewManager, com.facebook.react.bridge.NativeModule
    public String getName() {
        return REACT_CLASS;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.react.uimanager.ViewManager
    public VideoView createViewInstance(ThemedReactContext themedReactContext) {
        VideoView videoView = new VideoView(themedReactContext);
        this.videoView = videoView;
        return videoView;
    }

    @ReactProp(name = "muteAudio")
    public void setMuteAudio(VideoView videoView, boolean z) {
        videoView.setMuteAudio(z);
    }

    @ReactProp(name = PROP_SRC)
    public void setSrc(VideoView videoView, String str) {
        videoView.setSource(str);
    }

    @ReactProp(name = PROP_LIVE)
    public void setLive(VideoView videoView, boolean z) {
        videoView.setLive(z);
    }

    @ReactProp(name = "resizeMode")
    public void setResizeMode(VideoView videoView, int i) {
        videoView.setResizeMode(i);
    }

    @ReactProp(defaultBoolean = false, name = PROP_PAUSED)
    public void setPaused(VideoView videoView, boolean z) throws JSONException {
        videoView.setPausedModifier(z);
    }

    @ReactProp(defaultBoolean = false, name = PROP_FULLSCREEN)
    public void setFullScreen(VideoView videoView, boolean z) {
        videoView.setFullScreen(z);
    }

    @ReactProp(name = "vbr")
    public void setVbr(VideoView videoView, String str) throws JSONException {
        videoView.setVbr(str);
    }

    @ReactProp(name = "from")
    public void setFrom(VideoView videoView, String str) {
        videoView.setFrom(str);
    }

    @ReactProp(name = PROP_AUTO_HIDDEN_CONTROL_VIEW)
    public void setAutoHiddenControlView(VideoView videoView, boolean z) {
        videoView.setAutoHiddenControlView(Boolean.valueOf(z));
    }

    @ReactProp(name = PROP_MEDIA_HARD_DECODE)
    public void setMediaHardDecode(VideoView videoView, boolean z) {
        videoView.setMediaHardDecode(Boolean.valueOf(z));
    }

    @ReactProp(name = PROP_NEED_SYSTEM_CONTROLLER)
    public void setNeedSystemController(VideoView videoView, boolean z) {
        videoView.setNeedSystemController(Boolean.valueOf(z));
    }

    @ReactProp(name = PROP_NEED_FULL_SCREEN)
    public void setNeedFullScreen(VideoView videoView, boolean z) {
        videoView.setNeedFullScreen(Boolean.valueOf(z));
    }

    @ReactProp(name = PROP_SEEK_TO_POSITION)
    public void setSeekToPosition(VideoView videoView, String str) {
        try {
            videoView.seekTo(Long.valueOf(str).longValue());
        } catch (Exception unused) {
        }
    }

    @ReactProp(name = "seekAtStart")
    public void setSeekAtStart(VideoView videoView, String str) {
        try {
            videoView.setSeekAtStart(Long.valueOf(str).longValue());
        } catch (Exception unused) {
        }
    }

    @ReactProp(name = "play")
    public void setPlay(VideoView videoView, boolean z) throws JSONException {
        if (z) {
            videoView.play();
        }
    }

    @ReactProp(name = PROP_STOP)
    public void setStop(VideoView videoView, boolean z) throws JSONException {
        if (z) {
            videoView.stop();
        }
    }

    @ReactProp(name = PROP_AUTO_PLAY)
    public void setAutoPlay(VideoView videoView, boolean z) {
        videoView.setAutoPlay(z);
    }

    @ReactProp(name = "urs")
    public void setUrs(VideoView videoView, String str) {
        videoView.setUrs(str);
    }

    @ReactProp(name = PROP_EXTRA_LOG)
    public void setExtraLog(VideoView videoView, String str) {
        videoView.setExtraLog(str);
    }

    @ReactProp(name = PROP_CLIENT_TYPE)
    public void setClientType(VideoView videoView, int i) {
        videoView.setClientType(i);
    }

    @ReactProp(name = PROP_GAME_UID)
    public void setGameUid(VideoView videoView, double d) {
        videoView.setGameUid((long) d);
    }

    @ReactProp(name = PROP_SID)
    public void setSid(VideoView videoView, String str) {
        videoView.setSid(str);
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.of("quitFullScreen", 1);
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public void receiveCommand(VideoView videoView, int i, ReadableArray readableArray) {
        if (i != 1) {
            return;
        }
        videoView.quitFullScreen();
    }

    @Override // com.facebook.react.uimanager.BaseViewManager, com.facebook.react.uimanager.ViewManager
    public Map getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.of(VideoEvent.EVENT_NAME, MapBuilder.of("registrationName", "onVideoEvent"), VideoVbrEvent.EVENT_NAME, MapBuilder.of("registrationName", "onVbrEvent"));
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public Map getExportedViewConstants() {
        return MapBuilder.of("ScaleNone", (short) 2, "ScaleToFill", (short) 1, "ScaleAspectFit", (short) 2, "ScaleAspectFill", (short) 3);
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public void onDropViewInstance(VideoView videoView) {
        super.onDropViewInstance((VideoViewManager) videoView);
        videoView.onDestroy();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.react.uimanager.BaseViewManager, com.facebook.react.uimanager.ViewManager
    public void onAfterUpdateTransaction(VideoView videoView) throws JSONException {
        super.onAfterUpdateTransaction((VideoViewManager) videoView);
        videoView.replayWithoutRepeat();
    }
}