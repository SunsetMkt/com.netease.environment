package com.netease.cc.ccplayerwrapper.a;

import android.content.Context;
import android.os.Looper;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.tools.PlayerHelper;

/* compiled from: VideoPlayer.java */
/* loaded from: classes5.dex */
public class d extends IjkMediaPlayer {
    public d(Context context, Looper looper) {
        super(context, looper);
    }

    @Override // tv.danmaku.ijk.media.player.IjkMediaPlayer
    public void reportHttpStatInfo(String str) {
        PlayerHelper.httpGet(str, null);
    }
}