package com.netease.cc.ccplayerwrapper.a;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager;
import com.netease.cc.ccplayerwrapper.Constants;
import com.netease.cc.ccplayerwrapper.DecoderConfig;
import com.netease.cc.ccplayerwrapper.PlayerLayout;
import com.netease.cc.ccplayerwrapper.VideoConfig;
import com.netease.cc.ccplayerwrapper.utils.InfoView;
import com.netease.cc.ccplayerwrapper.utils.LogUtil;
import java.io.IOException;
import java.util.Map;
import org.apache.james.mime4j.util.CharsetUtil;
import org.json.JSONException;
import org.json.JSONObject;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.JitterBufferSetting;
import tv.danmaku.ijk.media.player.PlayerConfig;
import tv.danmaku.ijk.media.player.option.format.AvFormatOptionLong;
import tv.danmaku.ijk.media.player.tools.SwitcherConfig;

/* compiled from: CCPlayerImp.java */
/* loaded from: classes5.dex */
public class a extends Handler implements com.netease.cc.ccplayerwrapper.b.a, IMediaPlayer.OnPlayerEventListener {

    /* renamed from: a, reason: collision with root package name */
    private IjkMediaPlayer f1529a;
    private InfoView b;
    private PlayerLayout c;
    private com.netease.cc.ccplayerwrapper.a.b d;
    private com.netease.cc.ccplayerwrapper.b.b e;
    private com.netease.cc.ccplayerwrapper.a.c f;
    private IMediaPlayer.OnPlayerEventListener g;
    private Handler h;

    /* compiled from: CCPlayerImp.java */
    /* renamed from: com.netease.cc.ccplayerwrapper.a.a$a, reason: collision with other inner class name */
    class RunnableC0059a implements Runnable {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ boolean f1530a;

        RunnableC0059a(boolean z) {
            this.f1530a = z;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                a.this.b.setVisibility(this.f1530a ? 0 : 8);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* compiled from: CCPlayerImp.java */
    class b implements Runnable {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ int f1531a;
        final /* synthetic */ int b;
        final /* synthetic */ int c;
        final /* synthetic */ Object d;

        b(int i, int i2, int i3, Object obj) {
            this.f1531a = i;
            this.b = i2;
            this.c = i3;
            this.d = obj;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (a.this.g != null) {
                a.this.g.onEvent(this.f1531a, this.b, this.c, this.d);
            }
        }
    }

    /* compiled from: CCPlayerImp.java */
    static /* synthetic */ class c {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f1532a;
        static final /* synthetic */ int[] b;

        static {
            int[] iArr = new int[Constants.PLAY_STATE.values().length];
            b = iArr;
            try {
                iArr[Constants.PLAY_STATE.INIT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                b[Constants.PLAY_STATE.PREPARING.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                b[Constants.PLAY_STATE.PLAYING.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                b[Constants.PLAY_STATE.PAUSE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                b[Constants.PLAY_STATE.STOP.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                b[Constants.PLAY_STATE.RELEASE.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            int[] iArr2 = new int[VideoConfig.VIDEO_TYPE.values().length];
            f1532a = iArr2;
            try {
                iArr2[VideoConfig.VIDEO_TYPE.VOD_URL.ordinal()] = 1;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                f1532a[VideoConfig.VIDEO_TYPE.VOD_RECORDID.ordinal()] = 2;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                f1532a[VideoConfig.VIDEO_TYPE.LIVE_URL.ordinal()] = 3;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                f1532a[VideoConfig.VIDEO_TYPE.LIVE_MOBILE_URL.ordinal()] = 4;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                f1532a[VideoConfig.VIDEO_TYPE.LIVE_CCID.ordinal()] = 5;
            } catch (NoSuchFieldError unused11) {
            }
        }
    }

    public a(Context context, Looper looper, IMediaPlayer.OnPlayerEventListener onPlayerEventListener) {
        super(looper);
        this.b = null;
        this.c = null;
        com.netease.cc.ccplayerwrapper.a.b bVar = new com.netease.cc.ccplayerwrapper.a.b();
        this.d = bVar;
        this.e = new com.netease.cc.ccplayerwrapper.b.b(this, bVar);
        this.f = new com.netease.cc.ccplayerwrapper.a.c();
        this.g = null;
        this.h = null;
        this.d.f1533a = context;
        a(onPlayerEventListener, looper);
    }

    private void c() {
        if (a(Constants.PLAY_STATE.RELEASE)) {
            IjkMediaPlayer ijkMediaPlayer = this.f1529a;
            if (ijkMediaPlayer != null) {
                ijkMediaPlayer.release();
            }
            this.f = null;
            this.h = null;
        }
    }

    private void d() {
        IjkMediaPlayer ijkMediaPlayer = this.f1529a;
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer.resetNativePlayer();
        }
    }

    private boolean j() {
        com.netease.cc.ccplayerwrapper.b.b bVar;
        if (this.d.b.isResetWhenComplete()) {
            p();
        }
        if (!this.d.d || (bVar = this.e) == null) {
            return false;
        }
        boolean zA = bVar.a();
        if (zA) {
            a(Constants.PLAY_STATE.PREPARING);
            this.d.b.setVbrChooseByUser(0);
            this.e.c((String) null);
            b(200, 5, 0, 0);
        }
        return zA;
    }

    private void l() {
        com.netease.cc.ccplayerwrapper.a.b bVar = this.d;
        if (bVar.d) {
            if (bVar.b.isStatEnable()) {
                return;
            }
            this.f1529a.setOption(new AvFormatOptionLong(4, AvFormatOptionLong.AV_PLAYER_OPT_ENABLE_STAT, 0L));
            return;
        }
        this.f1529a.setSpeed(bVar.b.getPlaybackSpeed());
        this.f1529a.setOption(new AvFormatOptionLong(4, AvFormatOptionLong.AV_PLAYER_OPT_SEEK_AT_START, this.d.b.getSeekAtStartMs()));
        this.f1529a.setOption(new AvFormatOptionLong(4, "enable-accurate-seek", 1L));
        this.f1529a.setOption(new AvFormatOptionLong(4, AvFormatOptionLong.AV_PLAYER_OPT_SOUND_TOUCH, 1L));
        this.f1529a.setOption(new AvFormatOptionLong(4, AvFormatOptionLong.AV_PLAYER_OPT_DOWNLOAD_DURATION_MS, this.d.b.getMaxCacheMs()));
        if (this.d.b.getHeaders() == null || this.d.b.getHeaders().isEmpty()) {
            return;
        }
        Map<String, String> headers = this.d.b.getHeaders();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            sb.append(entry.getKey());
            sb.append(":");
            if (!TextUtils.isEmpty(entry.getValue())) {
                sb.append(entry.getValue());
            }
            sb.append(CharsetUtil.CRLF);
        }
        this.f1529a.setAvFormatOption("headers", sb.toString());
    }

    private void n() {
        if (this.d.d) {
            PlayerConfig playerConfigD = this.e.d();
            if (this.f1529a == null || playerConfigD == null) {
                return;
            }
            JitterBufferSetting jitterBufferSetting = playerConfigD.netType == 1 ? playerConfigD.wifiSetting : playerConfigD.cellSetting;
            if (jitterBufferSetting != null) {
                this.f1529a.setPlayControlParameters(jitterBufferSetting.canfwd, jitterBufferSetting.fwdnew, jitterBufferSetting.buffertime * 1000, ((int) jitterBufferSetting.fwdexttime) * 1000, jitterBufferSetting.firstjitter, jitterBufferSetting.minjitter, jitterBufferSetting.maxjitter);
            }
        }
    }

    private void q() throws JSONException {
        InfoView infoView = this.b;
        if (infoView == null || infoView.getVisibility() != 0) {
            return;
        }
        e();
        sendEmptyMessageDelayed(121, 1000L);
    }

    public void e(boolean z) {
        IjkMediaPlayer ijkMediaPlayer = this.f1529a;
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer.setScreenOnWhilePlaying(z);
        }
    }

    public final JSONObject f() {
        com.netease.cc.ccplayerwrapper.b.b bVar = this.e;
        if (bVar != null) {
            return bVar.c();
        }
        return null;
    }

    public IjkMediaPlayer g() {
        return this.f1529a;
    }

    public Constants.PLAY_STATE h() {
        return this.d.c;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) throws IllegalStateException, JSONException {
        Constants.PLAY_STATE play_state;
        com.netease.cc.ccplayerwrapper.a.b bVar = this.d;
        if (bVar == null || (play_state = bVar.c) == Constants.PLAY_STATE.RELEASE) {
            LogUtil.LOGE("CCPlayerImp", "Invalid Msg " + message.what);
            return;
        }
        switch (message.what) {
            case 100:
                b();
                break;
            case 102:
                c();
                break;
            case 104:
                a((String) message.obj);
                break;
            case 105:
                a((message.arg1 * 1.0f) / 100.0f, (message.arg2 * 1.0f) / 100.0f);
                break;
            case 106:
                c(((Boolean) message.obj).booleanValue());
                break;
            case 107:
                k();
                break;
            case 108:
                m();
                break;
            case 109:
                c(message.arg1);
                break;
            case 110:
                d();
                break;
            case 111:
                d(((Boolean) message.obj).booleanValue());
                break;
            case 112:
                a(((Boolean) message.obj).booleanValue());
                break;
            case 113:
                a(((Long) message.obj).longValue());
                break;
            case 114:
                b(((Integer) message.obj).intValue());
                break;
            case 115:
                b(((Boolean) message.obj).booleanValue());
                break;
            case 116:
                e(((Boolean) message.obj).booleanValue());
                break;
            case 117:
                b((JSONObject) message.obj);
                break;
            case 118:
                b((VideoConfig) message.obj);
                break;
            case 119:
                p();
                break;
            case 121:
                q();
                break;
            case 122:
                String[] strArr = (String[]) message.obj;
                String str = strArr[0];
                String str2 = strArr[1];
                if (play_state != Constants.PLAY_STATE.PREPARING || !bVar.f.equals(str)) {
                    LogUtil.LOGE("CCPlayerImp", "[mobile-url] MSG_MOBILE_URL_RESULT Fail state " + this.d.c + " mu:" + str + " pmu\uff1a" + this.d.f);
                    break;
                } else {
                    LogUtil.LOGF("CCPlayerImp", "[mobile-url] MSG_MOBILE_URL_RESULT OK state " + this.d.c + " mu:" + str + " pmu\uff1a" + this.d.f);
                    this.e.a(message.arg1, str2);
                    break;
                }
                break;
        }
    }

    public final JSONObject i() {
        com.netease.cc.ccplayerwrapper.b.b bVar = this.e;
        if (bVar != null) {
            return bVar.e();
        }
        return null;
    }

    public void k() throws IllegalStateException {
        a(Constants.PLAY_STATE.PAUSE);
        IjkMediaPlayer ijkMediaPlayer = this.f1529a;
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer.pause();
        }
    }

    public void m() throws IllegalStateException {
        if (this.d.c != Constants.PLAY_STATE.PAUSE) {
            return;
        }
        a(Constants.PLAY_STATE.PLAYING);
        IjkMediaPlayer ijkMediaPlayer = this.f1529a;
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer.start();
        }
    }

    public void o() {
        if (this.d.d) {
            PlayerConfig playerConfigD = this.e.d();
            if (this.f1529a == null || playerConfigD == null) {
                return;
            }
            com.netease.cc.ccplayerwrapper.utils.a.a(playerConfigD, this.d.b);
            com.netease.cc.ccplayerwrapper.utils.a.a(playerConfigD, this.d.b.getUserInfo());
            this.f1529a.setPlayerConfig(playerConfigD);
        }
    }

    @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnPlayerEventListener
    public boolean onEvent(int i, int i2, int i3, Object obj) {
        LogUtil.LOGF("CCPlayerImp", "player event what(" + i + ") arg1(" + i2 + ") arg2(" + i3 + ")");
        a(i, i2, i3, obj);
        return false;
    }

    public void p() {
        if (a(Constants.PLAY_STATE.STOP)) {
            d();
        }
    }

    private void a(IMediaPlayer.OnPlayerEventListener onPlayerEventListener, Looper looper) {
        this.g = onPlayerEventListener;
        if (this.f1529a == null) {
            IjkMediaPlayer.setStatBelongMp(true);
            this.f1529a = new d(this.d.f1533a, looper);
            LogUtil.LOGE("CCPlayerImp", "ijkplayer created");
        }
        this.f1529a.setOnPlayerEventListener(this);
    }

    private void b() {
        this.h = new Handler(Looper.getMainLooper());
    }

    public void f(boolean z) {
        InfoView infoView = this.b;
        if (infoView == null) {
            return;
        }
        infoView.post(new RunnableC0059a(z));
        removeMessages(121);
        if (z) {
            sendEmptyMessageDelayed(121, LooperMessageLoggingManager.LAG_TIME);
        }
    }

    private void e() throws JSONException {
        IjkMediaPlayer ijkMediaPlayer = this.f1529a;
        if (ijkMediaPlayer == null || this.b == null) {
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(ijkMediaPlayer.dumpStatInfo());
            jSONObject.put("cdn", this.d.j);
            jSONObject.put("quality", this.d.g);
            jSONObject.put("mediaCodec", this.f1529a.getCodecMode() == IjkMediaPlayer.HARDWAREDECODER);
            jSONObject.put("hd4", DecoderConfig.isMediaCodecEnable(this.d.f1533a));
            jSONObject.put("hd5", DecoderConfig.H265MediaCodecEnable(this.d.f1533a));
            jSONObject.put(tv.danmaku.ijk.media.player.DecoderConfig.KEY_REPORT_HD, SwitcherConfig.getSwitcherValueInt(this.d.f1533a, tv.danmaku.ijk.media.player.DecoderConfig.KEY_REPORT_HD));
            com.netease.cc.ccplayerwrapper.b.b bVar = this.e;
            if (bVar != null && bVar.d() != null) {
                jSONObject.put("url", this.e.d().videoUrl);
            }
            jSONObject.put("state", this.d.c.toString());
            this.b.a(jSONObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void b(VideoConfig videoConfig) {
        int iA = a();
        if (iA != 0) {
            b(Constants.MEDIA_ERROR_INVALID_PARAM, iA, 0, null);
            LogUtil.LOGE("CCPlayerImp", "param or state error");
            return;
        }
        LogUtil.LOGF("CCPlayerImp", "[mobile-url] play " + videoConfig.toString());
        com.netease.cc.ccplayerwrapper.a.b bVar = this.d;
        bVar.e = true;
        bVar.b.copy(videoConfig);
        a(this.d.b);
    }

    public void d(boolean z) {
        IjkMediaPlayer ijkMediaPlayer = this.f1529a;
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer.setDevMode(z);
        }
    }

    public void c(boolean z) {
        IjkMediaPlayer ijkMediaPlayer = this.f1529a;
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer.setMuteAudio(z);
        }
    }

    public void c(int i) {
        this.d.b.setScaleMode(i);
        IjkMediaPlayer ijkMediaPlayer = this.f1529a;
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer.setScaledMode(i, false);
        }
        PlayerLayout playerLayout = this.c;
        if (playerLayout != null) {
            playerLayout.setScaleMode(i);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0032, code lost:
    
        if (java.lang.Integer.valueOf(r3.d.b.getAnchorCCid()).intValue() == 0) goto L12;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int a() {
        /*
            r3 = this;
            com.netease.cc.ccplayerwrapper.a.b r0 = r3.d
            com.netease.cc.ccplayerwrapper.Constants$PLAY_STATE r0 = r0.c
            com.netease.cc.ccplayerwrapper.Constants$PLAY_STATE r1 = com.netease.cc.ccplayerwrapper.Constants.PLAY_STATE.STOP
            if (r0 == r1) goto L15
            com.netease.cc.ccplayerwrapper.Constants$PLAY_STATE r1 = com.netease.cc.ccplayerwrapper.Constants.PLAY_STATE.INIT
            if (r0 == r1) goto L15
            java.lang.String r0 = "CCPlayerImp"
            java.lang.String r1 = "stop play first !!!"
            com.netease.cc.ccplayerwrapper.utils.LogUtil.LOGE(r0, r1)
            r0 = -1
            return r0
        L15:
            com.netease.cc.ccplayerwrapper.a.b r0 = r3.d
            com.netease.cc.ccplayerwrapper.VideoConfig r0 = r0.b
            com.netease.cc.ccplayerwrapper.VideoConfig$VIDEO_TYPE r0 = r0.getType()
            com.netease.cc.ccplayerwrapper.VideoConfig$VIDEO_TYPE r1 = com.netease.cc.ccplayerwrapper.VideoConfig.VIDEO_TYPE.LIVE_CCID
            r2 = -2
            if (r0 != r1) goto L35
            com.netease.cc.ccplayerwrapper.a.b r0 = r3.d     // Catch: java.lang.Exception -> L34
            com.netease.cc.ccplayerwrapper.VideoConfig r0 = r0.b     // Catch: java.lang.Exception -> L34
            java.lang.String r0 = r0.getAnchorCCid()     // Catch: java.lang.Exception -> L34
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch: java.lang.Exception -> L34
            int r0 = r0.intValue()     // Catch: java.lang.Exception -> L34
            if (r0 != 0) goto L35
        L34:
            return r2
        L35:
            com.netease.cc.ccplayerwrapper.a.b r0 = r3.d
            com.netease.cc.ccplayerwrapper.VideoConfig r0 = r0.b
            com.netease.cc.ccplayerwrapper.VideoConfig$VIDEO_TYPE r0 = r0.getType()
            com.netease.cc.ccplayerwrapper.VideoConfig$VIDEO_TYPE r1 = com.netease.cc.ccplayerwrapper.VideoConfig.VIDEO_TYPE.LIVE_MOBILE_URL
            if (r0 != r1) goto L65
            com.netease.cc.ccplayerwrapper.a.b r0 = r3.d     // Catch: java.lang.Exception -> L64
            com.netease.cc.ccplayerwrapper.VideoConfig r0 = r0.b     // Catch: java.lang.Exception -> L64
            java.lang.String r0 = r0.getAnchorCCid()     // Catch: java.lang.Exception -> L64
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch: java.lang.Exception -> L64
            int r0 = r0.intValue()     // Catch: java.lang.Exception -> L64
            if (r0 != 0) goto L54
            return r2
        L54:
            com.netease.cc.ccplayerwrapper.a.b r0 = r3.d     // Catch: java.lang.Exception -> L64
            com.netease.cc.ccplayerwrapper.VideoConfig r0 = r0.b     // Catch: java.lang.Exception -> L64
            java.lang.String r0 = r0.getMobileurl()     // Catch: java.lang.Exception -> L64
            boolean r0 = r0.isEmpty()     // Catch: java.lang.Exception -> L64
            if (r0 == 0) goto L65
            r0 = -3
            return r0
        L64:
            return r2
        L65:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.cc.ccplayerwrapper.a.a.a():int");
    }

    public void b(int i) {
        IjkMediaPlayer ijkMediaPlayer = this.f1529a;
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer.setRadicalRealTimeFlag(i);
        }
    }

    public void b(boolean z) {
        this.d.b.setEnableMediaCodec(z);
    }

    public void b(JSONObject jSONObject) throws IllegalStateException {
        IjkMediaPlayer ijkMediaPlayer;
        int iOptInt = jSONObject.optInt(Constants.KEY_CMD, 0);
        if (iOptInt == 0) {
        }
        switch (iOptInt) {
            case 201:
                this.f.a(jSONObject);
                break;
            case 202:
                String strOptString = jSONObject.optString("vbr", "");
                LogUtil.LOGE("CCPlayerImp", "switch vbr, vbr " + strOptString);
                p();
                if (!strOptString.isEmpty() && a(Constants.PLAY_STATE.PREPARING)) {
                    this.d.b.setVbrChooseByUser(1);
                    this.d.b.setVbrValue(0);
                    this.e.a(this.d, strOptString);
                    break;
                } else {
                    LogUtil.LOGE("CCPlayerImp", "Error, switch vbr, invalid vbr " + strOptString);
                    break;
                }
                break;
            case 203:
                String strOptString2 = jSONObject.optString("cdn", "");
                LogUtil.LOGE("CCPlayerImp", "switch cdn, invalid vbr " + strOptString2);
                p();
                if (!strOptString2.isEmpty() && a(Constants.PLAY_STATE.PREPARING)) {
                    this.d.b.setVbrChooseByUser(0);
                    this.e.c(strOptString2);
                    break;
                } else {
                    LogUtil.LOGE("CCPlayerImp", "Error, switch cdn, invalid cdn " + strOptString2);
                    break;
                }
                break;
            case 204:
                boolean z = jSONObject.optInt("pause", 1) == 1;
                IjkMediaPlayer ijkMediaPlayer2 = this.f1529a;
                if (ijkMediaPlayer2 != null && this.d.c == Constants.PLAY_STATE.PLAYING) {
                    if (z) {
                        ijkMediaPlayer2.pauseVideoDisplay();
                        break;
                    } else {
                        ijkMediaPlayer2.resumeVideoDisplay();
                        break;
                    }
                }
                break;
            case 205:
                this.d.b.getUserInfo().updateFromJson(jSONObject);
                o();
                break;
            case 206:
                f(jSONObject.optInt("show", 0) == 1);
                break;
            case 207:
                int iOptInt2 = jSONObject.optInt(com.xiaomi.onetrack.b.a.d, 0);
                this.d.b.setRadicalSpeedLevel(iOptInt2);
                if (this.d.c == Constants.PLAY_STATE.PLAYING && (ijkMediaPlayer = this.f1529a) != null) {
                    ijkMediaPlayer.setRadicalRealTimeFlag(iOptInt2);
                    break;
                }
                break;
            case 208:
                c(jSONObject.optInt("mode"));
                break;
            case 209:
                boolean zOptBoolean = jSONObject.optBoolean("value", true);
                IjkMediaPlayer ijkMediaPlayer3 = this.f1529a;
                if (ijkMediaPlayer3 != null) {
                    ijkMediaPlayer3.setScreenOnWhilePlaying(zOptBoolean);
                    break;
                }
                break;
            case 210:
                this.d.b.setPlaybackSpeed((float) jSONObject.optDouble(Constants.KEY_SPEED, 1.0d));
                IjkMediaPlayer ijkMediaPlayer4 = this.f1529a;
                if (ijkMediaPlayer4 != null) {
                    ijkMediaPlayer4.setSpeed(this.d.b.getPlaybackSpeed());
                    break;
                }
                break;
        }
    }

    private void a(String str) {
        int i;
        if (a(Constants.PLAY_STATE.PLAYING) && this.f1529a != null) {
            try {
                n();
                o();
                l();
                this.f1529a.enableFileLog(true);
                this.f1529a.setRealtimePlay(this.d.d);
                this.f1529a.setRadicalRealTimeFlag(this.d.b.getRadicalSpeedLevel());
                this.f1529a.setMediaCodecEnabled(this.d.b.isEnableMediaCodec(), true);
                this.f1529a.setScaledMode(this.d.b.getScaleMode(), false);
                this.f.a(this.f1529a);
                this.f1529a.setDataSource(str);
                this.f1529a.prepareAsync();
            } catch (IOException | IllegalArgumentException | IllegalStateException | SecurityException e) {
                if (e instanceof IllegalArgumentException) {
                    i = IMediaPlayer.MEDIA_ERROR_PARAM_EXCEPTION;
                } else if (e instanceof IllegalStateException) {
                    i = IMediaPlayer.MEDIA_ERROR_STATE_EXCEPTION;
                } else {
                    i = e instanceof SecurityException ? IMediaPlayer.MEDIA_ERROR_SECURITY_EXCEPTION : IMediaPlayer.MEDIA_ERROR_START_PLAY_UNKNOW;
                }
                IjkMediaPlayer ijkMediaPlayer = this.f1529a;
                if (ijkMediaPlayer != null) {
                    ijkMediaPlayer.postMsg(100, i, 0, null);
                }
                e.printStackTrace();
            }
        }
    }

    public void a(float f, float f2) {
        IjkMediaPlayer ijkMediaPlayer = this.f1529a;
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer.setVolume(f, f2);
        }
    }

    public void a(long j) throws IllegalStateException {
        IjkMediaPlayer ijkMediaPlayer = this.f1529a;
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer.seekTo(j);
        }
    }

    public void a(boolean z) {
        IjkMediaPlayer ijkMediaPlayer = this.f1529a;
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer.enableLog(z);
        }
    }

    private void a(VideoConfig videoConfig) {
        if (a(Constants.PLAY_STATE.PREPARING)) {
            this.d.d = videoConfig.getType() == VideoConfig.VIDEO_TYPE.LIVE_MOBILE_URL || videoConfig.getType() == VideoConfig.VIDEO_TYPE.LIVE_CCID || videoConfig.getType() == VideoConfig.VIDEO_TYPE.LIVE_URL;
            int i = c.f1532a[videoConfig.getType().ordinal()];
            if (i == 1) {
                a(videoConfig.getPlayurl());
                return;
            }
            if (i == 3) {
                a(videoConfig.getPlayurl());
            } else if (i == 4 || i == 5) {
                this.e.a(this.d);
            }
        }
    }

    private boolean a(Constants.PLAY_STATE play_state) {
        com.netease.cc.ccplayerwrapper.a.b bVar = this.d;
        boolean z = true;
        if (!bVar.e) {
            return true;
        }
        int i = c.b[bVar.c.ordinal()];
        if (i == 1 ? !(play_state == Constants.PLAY_STATE.PREPARING || play_state == Constants.PLAY_STATE.STOP || play_state == Constants.PLAY_STATE.RELEASE) : !(i == 2 ? play_state == Constants.PLAY_STATE.PLAYING || play_state == Constants.PLAY_STATE.STOP || play_state == Constants.PLAY_STATE.RELEASE : i == 3 ? play_state == Constants.PLAY_STATE.STOP || ((!this.d.d && play_state == Constants.PLAY_STATE.PAUSE) || play_state == Constants.PLAY_STATE.RELEASE) : i == 4 ? play_state == Constants.PLAY_STATE.PLAYING || play_state == Constants.PLAY_STATE.STOP || play_state == Constants.PLAY_STATE.RELEASE : i == 5 && (play_state == Constants.PLAY_STATE.PREPARING || play_state == Constants.PLAY_STATE.RELEASE))) {
            z = false;
        }
        if (z) {
            LogUtil.LOGF("CCPlayerImp", "change state from " + this.d.c + " to " + play_state);
            this.d.c = play_state;
            b(Constants.MEDIA_STATE_CHANGE, 0, 0, play_state);
        } else {
            LogUtil.LOGE("CCPlayerImp", "cannot change state from " + this.d.c + " to " + play_state);
        }
        return z;
    }

    private void b(int i, String str) {
        LogUtil.LOGF("onPlayError " + i);
        p();
        b(100, i, 0, str);
    }

    private void b(int i, int i2, int i3, Object obj) {
        Handler handler = this.h;
        if (handler != null) {
            handler.post(new b(i, i2, i3, obj));
        }
    }

    @Override // com.netease.cc.ccplayerwrapper.b.a
    public void a(int i, String str, String str2) {
        com.netease.cc.ccplayerwrapper.a.b bVar = this.d;
        if (bVar == null || bVar.c == Constants.PLAY_STATE.RELEASE) {
            return;
        }
        obtainMessage(122, i, 0, new String[]{str2, str}).sendToTarget();
    }

    @Override // com.netease.cc.ccplayerwrapper.b.a
    public void a(int i, String str) {
        LogUtil.LOGF("CCPlayerImp", "onGetVideoUrl code " + i + " state " + this.d.c);
        if (this.d.c != Constants.PLAY_STATE.PREPARING) {
            b(Constants.MEDIA_ERROR_STATE, str);
        } else if (i == 200 && !str.isEmpty()) {
            this.d.b.setPlayurl(str);
            a(str);
        } else {
            b(i, str);
        }
    }

    @Override // com.netease.cc.ccplayerwrapper.b.a
    public void a(JSONObject jSONObject) {
        b(Constants.MEDIA_GET_VBR_INFO, 0, 0, jSONObject);
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x002d  */
    /* JADX WARN: Removed duplicated region for block: B:18:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void a(int r4, int r5, int r6, java.lang.Object r7) {
        /*
            r3 = this;
            r0 = 2
            r1 = 1
            if (r4 == r0) goto L26
            r0 = 5
            if (r4 == r0) goto L11
            r0 = 100
            if (r4 == r0) goto Lc
            goto L2b
        Lc:
            boolean r0 = r3.a(r5)
            goto L2a
        L11:
            com.netease.cc.ccplayerwrapper.PlayerLayout r0 = r3.c
            if (r0 == 0) goto L2b
            com.netease.cc.ccplayerwrapper.a.b r2 = r3.d
            com.netease.cc.ccplayerwrapper.VideoConfig r2 = r2.b
            int r2 = r2.getScaleMode()
            r0.setScaleMode(r2)
            com.netease.cc.ccplayerwrapper.PlayerLayout r0 = r3.c
            r0.updateLayout()
            goto L2b
        L26:
            boolean r0 = r3.j()
        L2a:
            r1 = r1 ^ r0
        L2b:
            if (r1 == 0) goto L30
            r3.b(r4, r5, r6, r7)
        L30:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.cc.ccplayerwrapper.a.a.a(int, int, int, java.lang.Object):void");
    }

    private boolean a(int i) {
        LogUtil.LOGF("CCPlayerImp", "handleMediaError " + i);
        return j();
    }

    public void a(IMediaPlayer.OnPlayerEventListener onPlayerEventListener) {
        this.g = onPlayerEventListener;
    }

    public void a(PlayerLayout playerLayout) {
        this.c = playerLayout;
        if (playerLayout != null) {
            this.b = playerLayout.getInfoView();
        }
    }
}