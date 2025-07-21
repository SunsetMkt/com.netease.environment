package com.netease.rnccplayer;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.View;
import android.view.WindowInsets;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.ViewProps;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.netease.cc.ccplayerwrapper.CCPlayer;
import com.netease.cc.ccplayerwrapper.Constants;
import com.netease.cc.ccplayerwrapper.PlayerView;
import com.netease.cc.ccplayerwrapper.VideoConfig;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/* loaded from: classes4.dex */
public class VideoView extends RelativeLayout implements View.OnClickListener, LifecycleEventListener {
    public static final String EVENT_PROP_ORIENTATION = "orientation";
    public static final int MEDIA_PROGRESS = 501;
    public static final int MEDIA_STATE_DID_CHANGE = 400;
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private static final int SHOW_PROGRESS = 2;
    private String TAG;
    private ImageView backImage;
    private View backView;
    private View bottomView;
    private boolean canTouchToHideControlView;
    private int currentScreenType;
    private boolean isInit;
    private boolean isLive;
    private boolean isPlaying;
    private long lastClickPlayerViewTime;
    private long lastClickTime;
    private Boolean mAutoHiddenControlView;
    private Boolean mAutoPlay;
    private int mClientType;
    private ThemedReactContext mContext;
    private TextView mCurrentTime;
    private boolean mDragging;
    private long mDuration;
    private TextView mEndTime;
    private EventDispatcher mEventDispatcher;
    private String mExtraLog;
    private String mFrom;
    private ImageView mFullImage;
    private long mGameUid;
    private Handler mHandler;
    private boolean mHardDecode;
    private String mLastVideoPath;
    private boolean mMuteAudio;
    private Boolean mNeedFullScreen;
    private Boolean mNeedSystemController;
    private ImageView mPlayImage;
    private CCPlayer mPlayer;
    private ProgressBar mProgress;
    private int mScaleMode;
    private SeekBar.OnSeekBarChangeListener mSeekListener;
    private String mSid;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private String mUrs;
    private String mVideoPath;
    private View mView;
    private PlayerView playerView;
    private long seekAtStartTime;

    /* renamed from: com.netease.rnccplayer.VideoView$1 */
    class AnonymousClass1 extends Handler {
        AnonymousClass1() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) throws JSONException {
            if (message.what != 2) {
                return;
            }
            VideoView.this.setProgress(message.obj != null ? String.valueOf(message.obj) : null);
            if (VideoView.this.mDragging) {
                return;
            }
            sendMessageDelayed(obtainMessage(2), 1000L);
        }
    }

    public VideoView(ThemedReactContext themedReactContext) {
        super(themedReactContext);
        this.mPlayer = null;
        this.TAG = "BoBoPlayer";
        this.mLastVideoPath = null;
        this.mVideoPath = null;
        this.currentScreenType = 0;
        this.lastClickTime = 0L;
        this.lastClickPlayerViewTime = 0L;
        this.isLive = false;
        this.mFrom = "mc";
        this.mMuteAudio = false;
        this.seekAtStartTime = 0L;
        this.mHandler = new Handler() { // from class: com.netease.rnccplayer.VideoView.1
            AnonymousClass1() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) throws JSONException {
                if (message.what != 2) {
                    return;
                }
                VideoView.this.setProgress(message.obj != null ? String.valueOf(message.obj) : null);
                if (VideoView.this.mDragging) {
                    return;
                }
                sendMessageDelayed(obtainMessage(2), 1000L);
            }
        };
        this.isInit = false;
        this.isPlaying = false;
        this.canTouchToHideControlView = true;
        this.mClientType = 1204;
        this.mGameUid = 0L;
        this.mScaleMode = 2;
        this.mHardDecode = false;
        this.mSeekListener = new SeekBar.OnSeekBarChangeListener() { // from class: com.netease.rnccplayer.VideoView.3
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            }

            AnonymousClass3() {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (VideoView.this.mPlayer != null) {
                    VideoView.this.mDragging = true;
                    VideoView.this.mHandler.removeMessages(2);
                    VideoView.this.mPlayer.getCurrentPosition();
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (VideoView.this.mPlayer != null) {
                    VideoView.this.mPlayer.seekTo((VideoView.this.mDuration * seekBar.getProgress()) / 1000);
                    VideoView.this.mHandler.removeMessages(2);
                    VideoView.this.mDragging = false;
                    VideoView.this.mHandler.sendEmptyMessage(2);
                }
            }
        };
        init(themedReactContext);
    }

    public void setSource(String str) {
        this.mVideoPath = str;
    }

    private void init(ThemedReactContext themedReactContext) {
        this.mContext = themedReactContext;
        this.mAutoHiddenControlView = true;
        this.mNeedSystemController = true;
        this.mNeedFullScreen = true;
        this.mAutoPlay = true;
        this.mEventDispatcher = ((UIManagerModule) themedReactContext.getNativeModule(UIManagerModule.class)).getEventDispatcher();
        themedReactContext.addLifecycleEventListener(this);
        View viewInflate = inflate(getContext(), R.layout.view_video, null);
        this.mView = viewInflate;
        addView(viewInflate);
        initPlayer();
        initView();
    }

    private void initPlayer() {
        this.playerView = (PlayerView) this.mView.findViewById(R.id.player_view);
        CCPlayer cCPlayer = new CCPlayer(this.mContext, this.playerView, (IMediaPlayer.OnPlayerEventListener) null);
        this.mPlayer = cCPlayer;
        cCPlayer.enableLog(false);
        this.mPlayer.enableMediaCodec(true);
        this.mPlayer.setScaleMode(this.mScaleMode);
        this.mPlayer.setPlayerEventListener(new IMediaPlayer.OnPlayerEventListener() { // from class: com.netease.rnccplayer.VideoView.2
            AnonymousClass2() {
            }

            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnPlayerEventListener
            public boolean onEvent(int i, int i2, int i3, Object obj) throws JSONException {
                if (i == 1) {
                    Log.d(VideoView.this.TAG, "MEDIA_PREPARED");
                    if (VideoView.this.seekAtStartTime != 0) {
                        VideoView.this.mPlayer.seekTo(VideoView.this.seekAtStartTime);
                        VideoView.this.seekAtStartTime = 0L;
                    }
                    if (VideoView.this.mHandler != null) {
                        Message messageObtain = Message.obtain();
                        messageObtain.what = 2;
                        messageObtain.obj = String.valueOf(1);
                        VideoView.this.mHandler.sendMessage(messageObtain);
                    }
                } else if (i == 2) {
                    Log.d(VideoView.this.TAG, "MEDIA_PLAYBACK_COMPLETE");
                    VideoView.this.setPlayState(false, false);
                } else if (i == 3) {
                    Log.d(VideoView.this.TAG, "MEDIA_BUFFERING_UPDATE\uff1a" + i2);
                } else if (i == 100) {
                    Log.d(VideoView.this.TAG, "MEDIA_ERROR");
                } else if (i != 200) {
                    if (i == 20001 && VideoView.this.mView != null) {
                        VideoView.this.mView.postDelayed(new Runnable() { // from class: com.netease.rnccplayer.VideoView.2.1
                            final /* synthetic */ String val$msgStr;

                            AnonymousClass1(String str) {
                                str = str;
                            }

                            @Override // java.lang.Runnable
                            public void run() {
                                if (VideoView.this.mEventDispatcher != null) {
                                    VideoView.this.mEventDispatcher.dispatchEvent(new VideoEvent(VideoView.this.getId(), Constants.MEDIA_GET_VBR_INFO, str, (VideoView.this.mPlayer == null || VideoView.this.mPlayer.getIjkPlayer() == null || !VideoView.this.mPlayer.getIjkPlayer().isPlaying()) ? false : true));
                                }
                            }
                        }, 300L);
                        return false;
                    }
                } else if (i2 == 701) {
                    Log.d(VideoView.this.TAG, "MEDIA_INFO_BUFFERING_START");
                } else if (i2 == 702) {
                    Log.d(VideoView.this.TAG, "MEDIA_INFO_BUFFERING_END");
                }
                String strValueOf = "";
                if (obj != null) {
                    try {
                        strValueOf = String.valueOf(obj);
                    } catch (Exception unused) {
                    }
                }
                VideoView.this.mEventDispatcher.dispatchEvent(new VideoEvent(VideoView.this.getId(), i, strValueOf, (VideoView.this.mPlayer == null || VideoView.this.mPlayer.getIjkPlayer() == null || !VideoView.this.mPlayer.getIjkPlayer().isPlaying()) ? false : true));
                return false;
            }

            /* renamed from: com.netease.rnccplayer.VideoView$2$1 */
            class AnonymousClass1 implements Runnable {
                final /* synthetic */ String val$msgStr;

                AnonymousClass1(String str) {
                    str = str;
                }

                @Override // java.lang.Runnable
                public void run() {
                    if (VideoView.this.mEventDispatcher != null) {
                        VideoView.this.mEventDispatcher.dispatchEvent(new VideoEvent(VideoView.this.getId(), Constants.MEDIA_GET_VBR_INFO, str, (VideoView.this.mPlayer == null || VideoView.this.mPlayer.getIjkPlayer() == null || !VideoView.this.mPlayer.getIjkPlayer().isPlaying()) ? false : true));
                    }
                }
            }
        });
    }

    /* renamed from: com.netease.rnccplayer.VideoView$2 */
    class AnonymousClass2 implements IMediaPlayer.OnPlayerEventListener {
        AnonymousClass2() {
        }

        @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnPlayerEventListener
        public boolean onEvent(int i, int i2, int i3, Object obj) throws JSONException {
            if (i == 1) {
                Log.d(VideoView.this.TAG, "MEDIA_PREPARED");
                if (VideoView.this.seekAtStartTime != 0) {
                    VideoView.this.mPlayer.seekTo(VideoView.this.seekAtStartTime);
                    VideoView.this.seekAtStartTime = 0L;
                }
                if (VideoView.this.mHandler != null) {
                    Message messageObtain = Message.obtain();
                    messageObtain.what = 2;
                    messageObtain.obj = String.valueOf(1);
                    VideoView.this.mHandler.sendMessage(messageObtain);
                }
            } else if (i == 2) {
                Log.d(VideoView.this.TAG, "MEDIA_PLAYBACK_COMPLETE");
                VideoView.this.setPlayState(false, false);
            } else if (i == 3) {
                Log.d(VideoView.this.TAG, "MEDIA_BUFFERING_UPDATE\uff1a" + i2);
            } else if (i == 100) {
                Log.d(VideoView.this.TAG, "MEDIA_ERROR");
            } else if (i != 200) {
                if (i == 20001 && VideoView.this.mView != null) {
                    VideoView.this.mView.postDelayed(new Runnable() { // from class: com.netease.rnccplayer.VideoView.2.1
                        final /* synthetic */ String val$msgStr;

                        AnonymousClass1(String str) {
                            str = str;
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            if (VideoView.this.mEventDispatcher != null) {
                                VideoView.this.mEventDispatcher.dispatchEvent(new VideoEvent(VideoView.this.getId(), Constants.MEDIA_GET_VBR_INFO, str, (VideoView.this.mPlayer == null || VideoView.this.mPlayer.getIjkPlayer() == null || !VideoView.this.mPlayer.getIjkPlayer().isPlaying()) ? false : true));
                            }
                        }
                    }, 300L);
                    return false;
                }
            } else if (i2 == 701) {
                Log.d(VideoView.this.TAG, "MEDIA_INFO_BUFFERING_START");
            } else if (i2 == 702) {
                Log.d(VideoView.this.TAG, "MEDIA_INFO_BUFFERING_END");
            }
            String strValueOf = "";
            if (obj != null) {
                try {
                    strValueOf = String.valueOf(obj);
                } catch (Exception unused) {
                }
            }
            VideoView.this.mEventDispatcher.dispatchEvent(new VideoEvent(VideoView.this.getId(), i, strValueOf, (VideoView.this.mPlayer == null || VideoView.this.mPlayer.getIjkPlayer() == null || !VideoView.this.mPlayer.getIjkPlayer().isPlaying()) ? false : true));
            return false;
        }

        /* renamed from: com.netease.rnccplayer.VideoView$2$1 */
        class AnonymousClass1 implements Runnable {
            final /* synthetic */ String val$msgStr;

            AnonymousClass1(String str) {
                str = str;
            }

            @Override // java.lang.Runnable
            public void run() {
                if (VideoView.this.mEventDispatcher != null) {
                    VideoView.this.mEventDispatcher.dispatchEvent(new VideoEvent(VideoView.this.getId(), Constants.MEDIA_GET_VBR_INFO, str, (VideoView.this.mPlayer == null || VideoView.this.mPlayer.getIjkPlayer() == null || !VideoView.this.mPlayer.getIjkPlayer().isPlaying()) ? false : true));
                }
            }
        }
    }

    private void initView() {
        this.mPlayImage = (ImageView) this.mView.findViewById(R.id.iv_play);
        this.mFullImage = (ImageView) this.mView.findViewById(R.id.iv_fullscreen);
        this.backView = this.mView.findViewById(R.id.back_view);
        this.bottomView = this.mView.findViewById(R.id.bottom_view);
        this.mPlayImage.setOnClickListener(this);
        this.mFullImage.setOnClickListener(this);
        this.playerView.setOnClickListener(this);
        ImageView imageView = (ImageView) findViewById(R.id.iv_back);
        this.backImage = imageView;
        imageView.setOnClickListener(this);
        initProgressBar();
    }

    private void initProgressBar() {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.seekbar_video);
        this.mProgress = progressBar;
        if (progressBar != null) {
            if (progressBar instanceof SeekBar) {
                SeekBar seekBar = (SeekBar) progressBar;
                seekBar.setOnSeekBarChangeListener(this.mSeekListener);
                seekBar.setThumbOffset(1);
            }
            this.mProgress.setMax(1000);
        }
        this.mEndTime = (TextView) findViewById(R.id.text_all_time);
        this.mCurrentTime = (TextView) findViewById(R.id.text_current_time);
    }

    /* renamed from: com.netease.rnccplayer.VideoView$3 */
    class AnonymousClass3 implements SeekBar.OnSeekBarChangeListener {
        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        }

        AnonymousClass3() {
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStartTrackingTouch(SeekBar seekBar) {
            if (VideoView.this.mPlayer != null) {
                VideoView.this.mDragging = true;
                VideoView.this.mHandler.removeMessages(2);
                VideoView.this.mPlayer.getCurrentPosition();
            }
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (VideoView.this.mPlayer != null) {
                VideoView.this.mPlayer.seekTo((VideoView.this.mDuration * seekBar.getProgress()) / 1000);
                VideoView.this.mHandler.removeMessages(2);
                VideoView.this.mDragging = false;
                VideoView.this.mHandler.sendEmptyMessage(2);
            }
        }
    }

    private static String generateTime(long j) {
        int i = (int) (j / 1000);
        int i2 = i % 60;
        int i3 = (i / 60) % 60;
        int i4 = i / 3600;
        return i4 > 0 ? String.format(Locale.CHINA, "%02d:%02d:%02d", Integer.valueOf(i4), Integer.valueOf(i3), Integer.valueOf(i2)) : String.format(Locale.CHINA, "%02d:%02d", Integer.valueOf(i3), Integer.valueOf(i2));
    }

    public long setProgress(String str) throws JSONException {
        CCPlayer cCPlayer = this.mPlayer;
        if (cCPlayer == null || this.mDragging) {
            return 0L;
        }
        long currentPosition = cCPlayer.getCurrentPosition();
        long duration = this.mPlayer.getDuration();
        ProgressBar progressBar = this.mProgress;
        if (progressBar != null && duration > 0 && currentPosition < duration) {
            progressBar.setProgress((int) ((1000 * currentPosition) / duration));
        }
        this.mDuration = duration;
        TextView textView = this.mEndTime;
        if (textView != null) {
            textView.setText(generateTime(duration));
        }
        TextView textView2 = this.mCurrentTime;
        if (textView2 != null) {
            long j = this.mDuration;
            if (currentPosition < j) {
                textView2.setText(generateTime(currentPosition));
            } else {
                textView2.setText(generateTime(j));
            }
        }
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(ViewProps.POSITION, currentPosition);
            jSONObject.put("duration", duration);
            if (str != null) {
                jSONObject.put("type", str);
            }
            EventDispatcher eventDispatcher = this.mEventDispatcher;
            int id = getId();
            String string = jSONObject.toString();
            CCPlayer cCPlayer2 = this.mPlayer;
            eventDispatcher.dispatchEvent(new VideoEvent(id, MEDIA_PROGRESS, string, (cCPlayer2 == null || cCPlayer2.getIjkPlayer() == null || !this.mPlayer.getIjkPlayer().isPlaying()) ? false : true));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return currentPosition;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) throws JSONException {
        if (view.getId() == this.mPlayImage.getId()) {
            if (this.mPlayer == null || TextUtils.isEmpty(this.mVideoPath)) {
                return;
            }
            if (!this.isInit) {
                if (this.isLive) {
                    VideoConfig.Builder builderWithThirdPlatform = new VideoConfig.Builder().type(VideoConfig.VIDEO_TYPE.LIVE_MOBILE_URL).mobileurl(this.mVideoPath, "").withThirdPlatform(this.mFrom, this.mClientType);
                    String str = this.mUrs;
                    String deviceSid = this.mSid;
                    if (deviceSid == null) {
                        deviceSid = getDeviceSid(getContext());
                    }
                    this.mPlayer.play(builderWithThirdPlatform.withUserInfo(str, deviceSid, this.mGameUid, this.mExtraLog).build());
                } else {
                    this.mPlayer.play(new VideoConfig.Builder().type(VideoConfig.VIDEO_TYPE.VOD_URL).scaleMode(this.mScaleMode).maxCacheMs((this.mVideoPath.contains("http://") || this.mVideoPath.contains("https://")) ? 10000L : 0L).enableMediaCodec(this.mHardDecode).playurl(this.mVideoPath).build());
                }
                this.mPlayer.muteAudio(this.mMuteAudio);
                setPlayState(true, true);
                return;
            }
            if (!this.isPlaying) {
                this.mPlayer.resume();
                setPlayState(true, true);
                return;
            } else {
                this.mPlayer.pause();
                setPlayState(true, false);
                return;
            }
        }
        if (view.getId() == this.backImage.getId() || view.getId() == this.mFullImage.getId()) {
            long timeInMillis = Calendar.getInstance().getTimeInMillis();
            if (timeInMillis - this.lastClickTime <= 1000 || this.isLive) {
                return;
            }
            this.lastClickTime = timeInMillis;
            if (this.currentScreenType == 0) {
                this.currentScreenType = 1;
                sendRNFullScreen();
                switchTopBottomVisible(true);
                this.mFullImage.setImageResource(R.drawable.btn_fullscreen_narrow);
                return;
            }
            this.currentScreenType = 0;
            sendRNFullScreen();
            switchTopBottomVisible(true);
            this.mFullImage.setImageResource(R.drawable.btn_full_normal);
            return;
        }
        if (view.getId() != this.playerView.getId() || this.isLive) {
            return;
        }
        long timeInMillis2 = Calendar.getInstance().getTimeInMillis();
        if (timeInMillis2 - this.lastClickTime <= 1000 || timeInMillis2 - this.lastClickPlayerViewTime <= 500 || !this.canTouchToHideControlView) {
            return;
        }
        this.lastClickPlayerViewTime = timeInMillis2;
        switchTopBottomVisible(Boolean.valueOf(this.bottomView.getVisibility() == 8));
    }

    public void quitFullScreen() {
        this.lastClickTime = Calendar.getInstance().getTimeInMillis();
        setFullScreen(false);
    }

    public void switchTopBottomVisible(Boolean bool) {
        WindowInsets rootWindowInsets;
        DisplayCutout displayCutout;
        if (bool.booleanValue() && this.mNeedSystemController.booleanValue()) {
            if (this.currentScreenType == 1) {
                this.backView.setVisibility(0);
            } else {
                this.backView.setVisibility(8);
            }
            this.bottomView.setVisibility(0);
            Timer timer = this.mTimer;
            if (timer != null) {
                timer.cancel();
            }
            if (this.mAutoHiddenControlView.booleanValue()) {
                this.mTimer = new Timer();
                AnonymousClass4 anonymousClass4 = new TimerTask() { // from class: com.netease.rnccplayer.VideoView.4
                    AnonymousClass4() {
                    }

                    /* renamed from: com.netease.rnccplayer.VideoView$4$1 */
                    class AnonymousClass1 implements Runnable {
                        AnonymousClass1() {
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            VideoView.this.switchTopBottomVisible(false);
                        }
                    }

                    @Override // java.util.TimerTask, java.lang.Runnable
                    public void run() {
                        if (VideoView.this.bottomView != null) {
                            VideoView.this.bottomView.post(new Runnable() { // from class: com.netease.rnccplayer.VideoView.4.1
                                AnonymousClass1() {
                                }

                                @Override // java.lang.Runnable
                                public void run() {
                                    VideoView.this.switchTopBottomVisible(false);
                                }
                            });
                        }
                    }
                };
                this.mTimerTask = anonymousClass4;
                this.mTimer.schedule(anonymousClass4, 5000L);
            }
            if (Build.VERSION.SDK_INT >= 28 && (rootWindowInsets = this.mContext.getCurrentActivity().getWindow().getDecorView().getRootView().getRootWindowInsets()) != null && (displayCutout = rootWindowInsets.getDisplayCutout()) != null) {
                this.bottomView.setPadding(displayCutout.getSafeInsetTop(), 0, 0, 0);
            }
        } else {
            this.backView.setVisibility(8);
            this.bottomView.setVisibility(8);
            Timer timer2 = this.mTimer;
            if (timer2 != null) {
                timer2.cancel();
                this.mTimer = null;
            }
        }
        if (this.mNeedFullScreen.booleanValue()) {
            return;
        }
        this.mFullImage.setVisibility(8);
    }

    /* renamed from: com.netease.rnccplayer.VideoView$4 */
    class AnonymousClass4 extends TimerTask {
        AnonymousClass4() {
        }

        /* renamed from: com.netease.rnccplayer.VideoView$4$1 */
        class AnonymousClass1 implements Runnable {
            AnonymousClass1() {
            }

            @Override // java.lang.Runnable
            public void run() {
                VideoView.this.switchTopBottomVisible(false);
            }
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            if (VideoView.this.bottomView != null) {
                VideoView.this.bottomView.post(new Runnable() { // from class: com.netease.rnccplayer.VideoView.4.1
                    AnonymousClass1() {
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        VideoView.this.switchTopBottomVisible(false);
                    }
                });
            }
        }
    }

    public void setMuteAudio(boolean z) {
        this.mMuteAudio = z;
        CCPlayer cCPlayer = this.mPlayer;
        if (cCPlayer != null) {
            cCPlayer.muteAudio(z);
        }
    }

    public void setResizeMode(int i) {
        this.mScaleMode = i;
        this.mPlayer.setScaleMode(i);
    }

    public void setPausedModifier(boolean z) throws JSONException {
        if (z) {
            if (this.mPlayer.getIjkPlayer().isPlaying()) {
                this.mPlayer.pause();
                setPlayState(true, false);
                return;
            }
            return;
        }
        if (!this.isInit || this.mPlayer.getIjkPlayer().isPlaying()) {
            return;
        }
        this.mPlayer.resume();
        setPlayState(true, true);
    }

    public void setFullScreen(boolean z) {
        if (z) {
            if (this.currentScreenType == 0) {
                this.currentScreenType = 1;
                sendRNFullScreen();
                switchTopBottomVisible(true);
                this.mFullImage.setImageResource(R.drawable.btn_fullscreen_narrow);
                return;
            }
            return;
        }
        if (this.currentScreenType == 1) {
            this.currentScreenType = 0;
            sendRNFullScreen();
            switchTopBottomVisible(true);
            this.mFullImage.setImageResource(R.drawable.btn_full_normal);
        }
    }

    public void play() throws JSONException {
        onClick(this.mPlayImage);
    }

    public void stop() throws JSONException {
        CCPlayer cCPlayer = this.mPlayer;
        if (cCPlayer == null || !this.isInit) {
            return;
        }
        cCPlayer.stopPlay();
        setPlayState(false, false);
    }

    public void setAutoPlay(boolean z) {
        this.mAutoPlay = Boolean.valueOf(z);
    }

    public void replayWithoutRepeat() throws JSONException {
        String str = this.mVideoPath;
        if (str != null && !str.equals(this.mLastVideoPath)) {
            if (this.mAutoPlay.booleanValue()) {
                replay();
            }
            this.mLastVideoPath = this.mVideoPath;
        } else {
            if (this.mVideoPath != null || this.mLastVideoPath == null) {
                return;
            }
            if (this.mAutoPlay.booleanValue()) {
                replay();
            }
            this.mLastVideoPath = this.mVideoPath;
        }
    }

    public void replay() throws JSONException {
        stop();
        play();
    }

    public void setPlayState(boolean z, boolean z2) throws JSONException {
        int i;
        this.isInit = z;
        this.isPlaying = z2;
        boolean z3 = true;
        if (z && z2) {
            this.mPlayImage.setImageResource(R.drawable.btn_pause_normal);
            i = 1;
        } else {
            this.mPlayImage.setImageResource(R.drawable.btn_play_normal);
            i = 2;
        }
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("name", "MEDIA_STATE_DID_CHANGE");
            jSONObject.put("state", i);
            EventDispatcher eventDispatcher = this.mEventDispatcher;
            int id = getId();
            String string = jSONObject.toString();
            CCPlayer cCPlayer = this.mPlayer;
            if (cCPlayer == null || cCPlayer.getIjkPlayer() == null || !this.mPlayer.getIjkPlayer().isPlaying()) {
                z3 = false;
            }
            eventDispatcher.dispatchEvent(new VideoEvent(id, 400, string, z3));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setLive(boolean z) {
        this.isLive = z;
        if (z) {
            switchTopBottomVisible(false);
        }
    }

    public void setVbr(String str) throws JSONException {
        CCPlayer cCPlayer = this.mPlayer;
        if (cCPlayer == null || cCPlayer.getIjkPlayer() == null || !this.mPlayer.getIjkPlayer().isPlaying()) {
            return;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(Constants.KEY_CMD, 202);
            jSONObject.put("vbr", str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.mPlayer.sendCmd(jSONObject);
    }

    public void setFrom(String str) {
        this.mFrom = str;
    }

    public boolean isCurrentSource(String str) {
        return str != null && str.equals(this.mVideoPath);
    }

    public void setAutoHiddenControlView(Boolean bool) {
        this.mAutoHiddenControlView = bool;
    }

    public void setMediaHardDecode(Boolean bool) {
        this.mHardDecode = bool.booleanValue();
        CCPlayer cCPlayer = this.mPlayer;
        if (cCPlayer != null) {
            cCPlayer.enableMediaCodec(bool.booleanValue());
        }
    }

    public void setNeedSystemController(Boolean bool) {
        this.mNeedSystemController = bool;
    }

    public void setNeedFullScreen(Boolean bool) {
        this.mNeedFullScreen = bool;
        if (bool.booleanValue()) {
            return;
        }
        switchTopBottomVisible(true);
    }

    public void seekTo(long j) {
        CCPlayer cCPlayer = this.mPlayer;
        if (cCPlayer != null) {
            cCPlayer.seekTo(j);
        }
    }

    public void setSeekAtStart(long j) {
        this.seekAtStartTime = j;
        CCPlayer cCPlayer = this.mPlayer;
        if (cCPlayer != null) {
            cCPlayer.seekTo(j);
        }
    }

    private void sendRNFullScreen() {
        this.canTouchToHideControlView = false;
        WritableMap writableMapCreateMap = Arguments.createMap();
        writableMapCreateMap.putInt(EVENT_PROP_ORIENTATION, this.currentScreenType);
        sendEvent("video", writableMapCreateMap);
    }

    private void sendEvent(String str, WritableMap writableMap) {
        ThemedReactContext themedReactContext = this.mContext;
        if (themedReactContext != null) {
            ((DeviceEventManagerModule.RCTDeviceEventEmitter) themedReactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)).emit(str, writableMap);
        }
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.canTouchToHideControlView = true;
    }

    protected void onDestroy() {
        CCPlayer cCPlayer = this.mPlayer;
        if (cCPlayer != null) {
            cCPlayer.release();
            this.mPlayer = null;
        }
    }

    @Override // com.facebook.react.bridge.LifecycleEventListener
    public void onHostResume() {
        switchTopBottomVisible(true);
    }

    @Override // com.facebook.react.bridge.LifecycleEventListener
    public void onHostPause() throws JSONException {
        CCPlayer cCPlayer = this.mPlayer;
        if (cCPlayer != null) {
            cCPlayer.pause();
        }
        setPlayState(true, false);
        if (this.mNeedSystemController.booleanValue()) {
            if (this.currentScreenType == 1) {
                this.backView.setVisibility(0);
            } else {
                this.backView.setVisibility(8);
            }
            this.bottomView.setVisibility(0);
        }
        Timer timer = this.mTimer;
        if (timer != null) {
            timer.cancel();
            this.mTimer = null;
        }
    }

    @Override // com.facebook.react.bridge.LifecycleEventListener
    public void onHostDestroy() {
        CCPlayer cCPlayer = this.mPlayer;
        if (cCPlayer != null) {
            cCPlayer.release();
            this.mPlayer = null;
        }
        Timer timer = this.mTimer;
        if (timer != null) {
            timer.cancel();
            this.mTimer = null;
        }
    }

    public void setUrs(String str) {
        if (TextUtils.isEmpty(str)) {
            this.mUrs = getDeviceSid(getContext()) + "@ad.cc.163.com";
            return;
        }
        this.mUrs = str;
    }

    public void setExtraLog(String str) {
        this.mExtraLog = str;
    }

    public void setClientType(int i) {
        this.mClientType = i;
    }

    public void setGameUid(long j) {
        this.mGameUid = j;
    }

    public void setSid(String str) {
        this.mSid = str;
    }

    public static String getDeviceSid(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ccplayerDeviceID", 0);
        String string = sharedPreferences.getString("DeviceSid", null);
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        String randomUUID = getRandomUUID();
        SharedPreferences.Editor editorEdit = sharedPreferences.edit();
        editorEdit.putString("DeviceSid", randomUUID);
        editorEdit.commit();
        return randomUUID;
    }

    public static String getRandomUUID() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 15);
    }
}