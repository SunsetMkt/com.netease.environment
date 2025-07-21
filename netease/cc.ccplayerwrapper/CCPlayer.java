package com.netease.cc.ccplayerwrapper;

import android.content.Context;
import android.os.HandlerThread;
import com.netease.cc.ccplayerwrapper.Constants;
import com.netease.cc.ccplayerwrapper.VideoConfig;
import com.netease.cc.ccplayerwrapper.utils.HttpUtils;
import com.netease.cc.ccplayerwrapper.utils.LogUtil;
import org.json.JSONObject;
import tv.danmaku.ijk.media.player.HttpCallback;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.PlayerUtil;

/* loaded from: classes4.dex */
public class CCPlayer {
    private HandlerThread mHandlerThread;
    private com.netease.cc.ccplayerwrapper.a.a mPlayerImp;
    private final String VERSION = "3.0.1";
    private final String THREAD_NAME = "CCPlayer";

    static class a implements PlayerUtil {

        /* renamed from: com.netease.cc.ccplayerwrapper.CCPlayer$a$a, reason: collision with other inner class name */
        class C0058a implements HttpUtils.b.a {

            /* renamed from: a, reason: collision with root package name */
            final /* synthetic */ HttpCallback f1518a;

            C0058a(a aVar, HttpCallback httpCallback) {
                this.f1518a = httpCallback;
            }

            @Override // com.netease.cc.ccplayerwrapper.utils.HttpUtils.b.a
            public void callback(int i, String str) {
                HttpCallback httpCallback = this.f1518a;
                if (httpCallback != null) {
                    httpCallback.callback(i, str);
                }
            }
        }

        a() {
        }

        @Override // tv.danmaku.ijk.media.player.PlayerUtil
        public void httpGet(String str, HttpCallback httpCallback) {
            HttpUtils.httpGet(str, new C0058a(this, httpCallback));
        }

        @Override // tv.danmaku.ijk.media.player.PlayerUtil
        public void log2File(String str, String str2) {
            LogUtil.LOGI(str, str2);
        }
    }

    static {
        HttpUtils.Init();
        Init(null, false, false, new a());
    }

    public CCPlayer(Context context, IMediaPlayer.OnPlayerEventListener onPlayerEventListener) {
        this.mHandlerThread = null;
        this.mPlayerImp = null;
        HandlerThread handlerThread = new HandlerThread("CCPlayer");
        this.mHandlerThread = handlerThread;
        handlerThread.start();
        this.mPlayerImp = new com.netease.cc.ccplayerwrapper.a.a(context, this.mHandlerThread.getLooper(), onPlayerEventListener);
    }

    public static void Init(Context context, boolean z, boolean z2, PlayerUtil playerUtil) throws NoSuchMethodException, SecurityException {
        IjkMediaPlayer.setPlayerUtil(playerUtil);
        DecoderConfig.Init(context, z, z2);
    }

    public static void Release() {
        HttpUtils.Release();
    }

    private void releasePlayer() {
        com.netease.cc.ccplayerwrapper.a.a aVar = this.mPlayerImp;
        if (aVar != null) {
            aVar.obtainMessage(102).sendToTarget();
        }
    }

    public void enableLog(boolean z) {
        LogUtil.setLogEnable(z);
        com.netease.cc.ccplayerwrapper.a.a aVar = this.mPlayerImp;
        if (aVar != null) {
            aVar.obtainMessage(112, Boolean.valueOf(z)).sendToTarget();
        }
    }

    public void enableMediaCodec(boolean z) {
        com.netease.cc.ccplayerwrapper.a.a aVar = this.mPlayerImp;
        if (aVar != null) {
            aVar.obtainMessage(115, Boolean.valueOf(z)).sendToTarget();
        }
    }

    public JSONObject getCdnInfo() {
        com.netease.cc.ccplayerwrapper.a.a aVar = this.mPlayerImp;
        if (aVar != null) {
            return aVar.f();
        }
        return null;
    }

    public long getCurrentPosition() {
        com.netease.cc.ccplayerwrapper.a.a aVar = this.mPlayerImp;
        if (aVar == null || aVar.g() == null) {
            return 0L;
        }
        return this.mPlayerImp.g().getCurrentPosition();
    }

    public long getDuration() {
        com.netease.cc.ccplayerwrapper.a.a aVar = this.mPlayerImp;
        if (aVar == null || aVar.g() == null) {
            return 0L;
        }
        return this.mPlayerImp.g().getDuration();
    }

    public IjkMediaPlayer getIjkPlayer() {
        com.netease.cc.ccplayerwrapper.a.a aVar = this.mPlayerImp;
        if (aVar != null) {
            return aVar.g();
        }
        return null;
    }

    public Constants.PLAY_STATE getPlayState() {
        com.netease.cc.ccplayerwrapper.a.a aVar = this.mPlayerImp;
        if (aVar != null) {
            return aVar.h();
        }
        return null;
    }

    public float[] getRenderRectRatio() {
        com.netease.cc.ccplayerwrapper.a.a aVar = this.mPlayerImp;
        if (aVar == null || aVar.g() == null) {
            return null;
        }
        return this.mPlayerImp.g().getRenderRectRatio();
    }

    public final JSONObject getVbrInfo() {
        com.netease.cc.ccplayerwrapper.a.a aVar = this.mPlayerImp;
        if (aVar != null) {
            return aVar.i();
        }
        return null;
    }

    public long getVideoCache() {
        com.netease.cc.ccplayerwrapper.a.a aVar = this.mPlayerImp;
        if (aVar == null || aVar.g() == null) {
            return 0L;
        }
        return this.mPlayerImp.g().getVideoCache();
    }

    public void muteAudio(boolean z) {
        com.netease.cc.ccplayerwrapper.a.a aVar = this.mPlayerImp;
        if (aVar != null) {
            aVar.obtainMessage(106, Boolean.valueOf(z)).sendToTarget();
        }
    }

    public void pause() {
        com.netease.cc.ccplayerwrapper.a.a aVar = this.mPlayerImp;
        if (aVar != null) {
            aVar.obtainMessage(107).sendToTarget();
        }
    }

    public void play(VideoConfig videoConfig) {
        com.netease.cc.ccplayerwrapper.a.a aVar = this.mPlayerImp;
        if (aVar != null) {
            aVar.obtainMessage(118, videoConfig).sendToTarget();
        }
    }

    public void release() {
        if (this.mHandlerThread != null) {
            releasePlayer();
            this.mPlayerImp = null;
            this.mHandlerThread.quitSafely();
            this.mHandlerThread = null;
            this.mPlayerImp = null;
        }
    }

    public void resume() {
        com.netease.cc.ccplayerwrapper.a.a aVar = this.mPlayerImp;
        if (aVar != null) {
            aVar.obtainMessage(108).sendToTarget();
        }
    }

    public void seekTo(long j) {
        com.netease.cc.ccplayerwrapper.a.a aVar = this.mPlayerImp;
        if (aVar != null) {
            aVar.obtainMessage(113, Long.valueOf(j)).sendToTarget();
        }
    }

    public void sendCmd(JSONObject jSONObject) {
        com.netease.cc.ccplayerwrapper.a.a aVar = this.mPlayerImp;
        if (aVar != null) {
            aVar.obtainMessage(117, jSONObject).sendToTarget();
        }
    }

    public void setDevMode(boolean z) {
        com.netease.cc.ccplayerwrapper.a.a aVar = this.mPlayerImp;
        if (aVar != null) {
            aVar.obtainMessage(111, Boolean.valueOf(z)).sendToTarget();
        }
    }

    public void setPlayerEventListener(IMediaPlayer.OnPlayerEventListener onPlayerEventListener) {
        com.netease.cc.ccplayerwrapper.a.a aVar = this.mPlayerImp;
        if (aVar != null) {
            aVar.a(onPlayerEventListener);
        }
    }

    public void setRadicalBuffer(int i) {
        com.netease.cc.ccplayerwrapper.a.a aVar = this.mPlayerImp;
        if (aVar != null) {
            aVar.obtainMessage(114, Integer.valueOf(i)).sendToTarget();
        }
    }

    public void setScaleMode(int i) {
        com.netease.cc.ccplayerwrapper.a.a aVar = this.mPlayerImp;
        if (aVar != null) {
            aVar.obtainMessage(109, i, 0, null).sendToTarget();
        }
    }

    public void setScreenOnWhilePlaying(boolean z) {
        com.netease.cc.ccplayerwrapper.a.a aVar = this.mPlayerImp;
        if (aVar != null) {
            aVar.obtainMessage(116, 0, 0, Boolean.valueOf(z)).sendToTarget();
        }
    }

    public void setVolume(float f, float f2) {
        com.netease.cc.ccplayerwrapper.a.a aVar = this.mPlayerImp;
        if (aVar != null) {
            aVar.obtainMessage(105, (int) (f * 100.0f), (int) (f2 * 100.0f), null).sendToTarget();
        }
    }

    @Deprecated
    public void startPlay(String str) {
        play(new VideoConfig.Builder().type(VideoConfig.VIDEO_TYPE.VOD_URL).playurl(str).build());
    }

    public void stop() {
        com.netease.cc.ccplayerwrapper.a.a aVar = this.mPlayerImp;
        if (aVar != null) {
            aVar.obtainMessage(119).sendToTarget();
        }
    }

    @Deprecated
    public void stopPlay() {
        stop();
    }

    @Deprecated
    public void startPlay(String str, String str2, String str3, String str4, int i, String str5, String str6, String str7, long j, String str8) {
        play(new VideoConfig.Builder().type(VideoConfig.VIDEO_TYPE.LIVE_MOBILE_URL).mobileurl(str, str2).withPlatform(str3, str4, str4, "android", i).withUserInfo(str5, str6, j, str8).vbr(str7).build());
    }

    public CCPlayer(Context context, PlayerView playerView, IMediaPlayer.OnPlayerEventListener onPlayerEventListener) {
        this.mHandlerThread = null;
        this.mPlayerImp = null;
        HandlerThread handlerThread = new HandlerThread("CCPlayer");
        this.mHandlerThread = handlerThread;
        handlerThread.start();
        this.mPlayerImp = new com.netease.cc.ccplayerwrapper.a.a(context, this.mHandlerThread.getLooper(), onPlayerEventListener);
        playerView.setupRender(getIjkPlayer());
        this.mPlayerImp.obtainMessage(100).sendToTarget();
    }

    public CCPlayer(Context context, PlayerLayout playerLayout, IMediaPlayer.OnPlayerEventListener onPlayerEventListener) {
        this.mHandlerThread = null;
        this.mPlayerImp = null;
        HandlerThread handlerThread = new HandlerThread("CCPlayer");
        this.mHandlerThread = handlerThread;
        handlerThread.start();
        com.netease.cc.ccplayerwrapper.a.a aVar = new com.netease.cc.ccplayerwrapper.a.a(context, this.mHandlerThread.getLooper(), onPlayerEventListener);
        this.mPlayerImp = aVar;
        aVar.a(playerLayout);
        playerLayout.setupRender(getIjkPlayer());
        this.mPlayerImp.obtainMessage(100).sendToTarget();
    }
}