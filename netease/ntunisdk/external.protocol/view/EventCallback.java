package com.netease.ntunisdk.external.protocol.view;

import com.netease.ntunisdk.external.protocol.Situation;
import com.netease.ntunisdk.external.protocol.data.ProtocolInfo;
import org.json.JSONObject;

/* loaded from: classes.dex */
public interface EventCallback {
    void back(String str);

    void finish(int i, JSONObject jSONObject);

    void next(Situation situation, ProtocolInfo protocolInfo, int i);

    void openBrowser(String str);
}