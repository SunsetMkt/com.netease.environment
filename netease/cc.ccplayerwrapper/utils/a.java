package com.netease.cc.ccplayerwrapper.utils;

import com.netease.cc.ccplayerwrapper.UserInfo;
import com.netease.cc.ccplayerwrapper.VideoConfig;
import tv.danmaku.ijk.media.player.PlayerConfig;

/* compiled from: InnerHelper.java */
/* loaded from: classes5.dex */
public class a {
    public static void a(PlayerConfig playerConfig, UserInfo userInfo) {
        if (userInfo == null || playerConfig == null) {
            return;
        }
        playerConfig.ccid = userInfo.ccid;
        playerConfig.uid = (int) userInfo.uid;
        playerConfig.eid = userInfo.eid;
        playerConfig.urs = userInfo.urs;
        playerConfig.templateType = userInfo.templateType;
        playerConfig.roomId = userInfo.roomId;
        playerConfig.subId = userInfo.subId;
        playerConfig.gametype = userInfo.gametype;
        playerConfig.panorama = userInfo.panorama;
        playerConfig.context = userInfo.context;
        playerConfig.identity = userInfo.identity;
        playerConfig.udid = userInfo.udid;
        playerConfig.unisdkDeviceId = userInfo.unisdkDeviceId;
        playerConfig.sid = userInfo.sid;
        playerConfig.macAddr = userInfo.macAddr;
        playerConfig.version = userInfo.version;
        playerConfig.netType = userInfo.netType;
        playerConfig.isPortrait = userInfo.isPortrait;
        playerConfig.entrance = userInfo.entrance;
        playerConfig.strLogExtraInfo = userInfo.extraLog;
        playerConfig.extraDescOutJson = userInfo.extraDescOutJson;
        playerConfig.statStartAndIntervalJson = userInfo.statStartAndIntervalJson;
    }

    public static void a(PlayerConfig playerConfig, VideoConfig videoConfig) {
        if (playerConfig == null || videoConfig == null) {
            return;
        }
        try {
            if (playerConfig.anchorCCid == 0) {
                playerConfig.anchorCCid = Integer.valueOf(videoConfig.getAnchorCCid()).intValue();
            }
            playerConfig.src = videoConfig.getSrc();
            playerConfig.platform = videoConfig.getPlatform();
            playerConfig.osName = videoConfig.getOsName();
            playerConfig.clientType = videoConfig.getClientType();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}