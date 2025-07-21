package com.netease.pushclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.netease.push.utils.NotifyMessage;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes3.dex */
public class PushClientReceiver extends BroadcastReceiver {
    private static final String TAG = "NGPush_PushClientReceiver";
    private Class<?> m_clazzImpl;
    private Object m_instImpl;

    public PushClientReceiver() throws IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
        try {
            Class<?> cls = Class.forName("com.netease.pushclient.PushClientReceiverImpl");
            this.m_clazzImpl = cls;
            this.m_instImpl = cls.newInstance();
            this.m_clazzImpl.getMethod("setCallback", Object.class).invoke(this.m_instImpl, this);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, String.format("class %s not found:%s", "com.netease.pushclient.PushClientReceiverImpl", e.toString()));
        }
        Log.i(TAG, "PushClientReceiver constructed");
    }

    protected void setForceShowMsgOnFront(boolean z) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Log.i(TAG, "setForceShowMsgOnFront:" + z);
        try {
            this.m_clazzImpl.getMethod("setForceShowMsgOnFront", Boolean.TYPE).invoke(this.m_instImpl, Boolean.valueOf(z));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "setForceShowMsgOnFront exception:" + e.getMessage());
        }
    }

    protected boolean getForceShowMsgOnFront() {
        try {
            return ((Boolean) this.m_clazzImpl.getMethod("getForceShowMsgOnFront", new Class[0]).invoke(this.m_instImpl, new Object[0])).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getForceShowMsgOnFront exception:" + e.getMessage());
            return false;
        }
    }

    protected boolean isBackground(Context context) {
        try {
            return ((Boolean) this.m_clazzImpl.getMethod("isBackground", Context.class).invoke(this.m_instImpl, context)).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "isBackground exception:" + e.getMessage());
            return false;
        }
    }

    protected boolean canMsgShow(Context context) {
        try {
            return ((Boolean) this.m_clazzImpl.getMethod("canMsgShow", Context.class).invoke(this.m_instImpl, context)).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "canMsgShow exception:" + e.getMessage());
            return true;
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Log.e(TAG, "onReceive, intent:" + intent);
        try {
            this.m_clazzImpl.getMethod("onReceive", Context.class, Intent.class).invoke(this.m_instImpl, context, intent);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "onReceive exception:" + e.getMessage());
        }
    }

    public void onGetNewDevId(Context context, String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            this.m_clazzImpl.getMethod("onGetNewDevId", Context.class, String.class).invoke(this.m_instImpl, context, str);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "onGetNewDevId exception:" + e.getMessage());
        }
    }

    public void onGetNewToken(Context context, String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            this.m_clazzImpl.getMethod("onGetNewToken", Context.class, String.class).invoke(this.m_instImpl, context, str);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "onGetNewToken exception:" + e.getMessage());
        }
    }

    public void onReceiveNotifyMessage(Context context, NotifyMessage notifyMessage) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Log.d(TAG, "onReceiveNotifyMessage:" + notifyMessage);
        try {
            this.m_clazzImpl.getMethod("onReceiveNotifyMessage", Context.class, Object.class).invoke(this.m_instImpl, context, notifyMessage.getImpl());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "onReceiveNotifyMessage exception:" + e.getMessage());
        }
    }

    public void onShowNotifyMessage(Context context, NotifyMessage notifyMessage) {
        Log.d(TAG, "onShowNotifyMessage:" + notifyMessage);
    }

    public void onChannelNotiClickMessage(Context context, NotifyMessage notifyMessage) {
        Log.d(TAG, "onChannelNotiClickMessage:" + notifyMessage);
    }
}