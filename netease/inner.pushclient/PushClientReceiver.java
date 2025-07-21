package com.netease.inner.pushclient;

import android.content.Intent;
import com.netease.ntunisdk.base.PatchPlaceholder;
import com.netease.push.utils.PushConstantsImpl;
import com.netease.push.utils.PushLog;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes4.dex */
public class PushClientReceiver {
    private static final String TAG = "NGPush_" + PushClientReceiver.class.getSimpleName();

    private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, PatchPlaceholder.class.getSimpleName());
    }

    public static Intent createMethodIntent() {
        Intent intent = new Intent(PushConstantsImpl.CLIENT_ACTION_METHOD);
        intent.addFlags(32);
        intent.putExtra(PushConstantsImpl.METHOD_VER_NAME, 1);
        return intent;
    }

    public static Intent createMessageIntent() {
        Intent intent = new Intent(PushConstantsImpl.CLIENT_ACTION_MESSAGE);
        intent.addFlags(32);
        intent.putExtra(PushConstantsImpl.MESSAGE_VER_NAME, 1);
        return intent;
    }

    public static Intent createNewIDIntent() {
        Intent intent = new Intent(PushConstantsImpl.CLIENT_ACTION_REFRESH_DEVID);
        intent.addFlags(32);
        intent.putExtra(PushConstantsImpl.NEWID_VER_NAME, 1);
        return intent;
    }

    public static Intent createChannelNotiClick() {
        Intent intent = new Intent(PushConstantsImpl.CLIENT_ACTION_CHANNLE_NOTI_CLICK);
        intent.addFlags(32);
        intent.putExtra(PushConstantsImpl.CHANNEL_NOTI_CLICK_VER_NAME, 1);
        return intent;
    }
}