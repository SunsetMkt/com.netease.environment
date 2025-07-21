package com.netease.cc.ccplayerwrapper.a;

import android.content.Context;
import com.netease.cc.ccplayerwrapper.Constants;
import com.netease.cc.ccplayerwrapper.VideoConfig;
import java.util.ArrayList;
import java.util.List;

/* compiled from: PlayItem.java */
/* loaded from: classes5.dex */
public class b {

    /* renamed from: a, reason: collision with root package name */
    public Context f1533a;
    public String f;
    public String g;
    public int h;
    public int i;
    public String j;
    public List<String> m = null;
    boolean e = false;
    public Constants.PLAY_STATE c = Constants.PLAY_STATE.INIT;
    public VideoConfig b = new VideoConfig.Builder().scaleMode(2).enableMediaCodec(true).build();
    boolean d = false;
    public List<String> k = new ArrayList();
    public List<String> l = new ArrayList();
}