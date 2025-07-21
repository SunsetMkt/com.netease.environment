package com.netease.cc.ccplayerwrapper.a;

import android.util.Log;
import com.netease.cc.ccplayerwrapper.Constants;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONObject;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.option.format.AvFormatOptionLong;

/* compiled from: PlayerSettingMgr.java */
/* loaded from: classes5.dex */
public class c {

    /* renamed from: a, reason: collision with root package name */
    public ArrayList<AvFormatOptionLong> f1534a = new ArrayList<>();

    public void a(JSONObject jSONObject) {
        int iOptInt = jSONObject.optInt(Constants.KEY_OPERATION, 0);
        Iterator<String> itKeys = jSONObject.keys();
        while (itKeys.hasNext()) {
            String next = itKeys.next();
            if (!next.isEmpty() && !next.equals(Constants.KEY_CMD) && !next.equals(Constants.KEY_OPERATION)) {
                if (iOptInt == 1) {
                    a(next, jSONObject.optLong(next, 0L));
                } else if (iOptInt == 2) {
                    a(next);
                } else {
                    Log.e("PlayerSettingMgr", "handleSettingCmd invalid operation " + iOptInt);
                }
            }
        }
    }

    private void a(String str, long j) {
        int size = this.f1534a.size();
        boolean z = false;
        int i = 0;
        while (true) {
            if (i < size) {
                AvFormatOptionLong avFormatOptionLong = this.f1534a.get(i);
                if (avFormatOptionLong != null && avFormatOptionLong.getName().equals(str)) {
                    avFormatOptionLong.setValue(j);
                    z = true;
                    break;
                }
                i++;
            } else {
                break;
            }
        }
        if (z) {
            return;
        }
        this.f1534a.add(new AvFormatOptionLong(4, str, j));
    }

    private void a(String str) {
        AvFormatOptionLong avFormatOptionLong;
        int size = this.f1534a.size();
        int i = 0;
        while (true) {
            if (i < size) {
                avFormatOptionLong = this.f1534a.get(i);
                if (avFormatOptionLong != null && avFormatOptionLong.getName().equals(str)) {
                    break;
                } else {
                    i++;
                }
            } else {
                avFormatOptionLong = null;
                break;
            }
        }
        if (avFormatOptionLong != null) {
            this.f1534a.remove(avFormatOptionLong);
            Log.e("PlayerSettingMgr", "delSetting " + avFormatOptionLong.getName() + " suc");
            return;
        }
        Log.e("PlayerSettingMgr", "delSetting " + str + " fail, not exist");
    }

    public void a(IjkMediaPlayer ijkMediaPlayer) {
        int size = this.f1534a.size();
        for (int i = 0; i < size; i++) {
            ijkMediaPlayer.setOption(this.f1534a.get(i));
        }
        ijkMediaPlayer.setOption(new AvFormatOptionLong(4, AvFormatOptionLong.AV_PLAYER_OPT_ENABLE_HEARTBEAT_STAT, 1L));
    }
}