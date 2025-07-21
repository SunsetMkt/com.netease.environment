package com.netease.ntunisdk.base;

import java.util.List;

/* loaded from: classes2.dex */
public interface QueryRankListener {
    void onQueryRankFinished(List<AccountInfo> list);

    void onUpdateAchievement(boolean z);

    void onUpdateRankFinished(boolean z);
}