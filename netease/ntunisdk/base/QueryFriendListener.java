package com.netease.ntunisdk.base;

import java.util.List;

/* loaded from: classes2.dex */
public interface QueryFriendListener {
    void onApplyFriendFinished(boolean z);

    void onInviteFriendListFinished(List<String> list);

    void onInviterListFinished(List<AccountInfo> list);

    void onIsDarenUpdated(boolean z);

    void onQueryAvailablesInviteesFinished(List<AccountInfo> list);

    void onQueryFriendListFinished(List<AccountInfo> list);

    void onQueryFriendListInGameFinished(List<AccountInfo> list);

    void onQueryMyAccountFinished(AccountInfo accountInfo);
}