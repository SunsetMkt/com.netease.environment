package com.netease.androidcrashhandler.anr.messageQueue;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.os.SystemClock;
import com.netease.androidcrashhandler.util.LogUtils;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/* loaded from: classes5.dex */
public class HookMessage {
    private static MessageQueue mQueue;
    private static ArrayList<NonHandleMessage> mNonHandleMessageArrayList = new ArrayList<>();
    private static Handler mHandler = null;

    public static MessageQueue hookMainMessageQueue(Handler handler) throws NoSuchFieldException {
        LogUtils.i(LogUtils.TAG, "HookMessage [hookMainMessageQueue] start");
        if (handler == null) {
            LogUtils.i(LogUtils.TAG, "HookMessage [hookMainMessageQueue] handler is null");
            return null;
        }
        MessageQueue messageQueue = mQueue;
        if (messageQueue != null) {
            return messageQueue;
        }
        try {
            Field declaredField = Class.forName("android.os.Handler").getDeclaredField("mQueue");
            declaredField.setAccessible(true);
            MessageQueue messageQueue2 = (MessageQueue) declaredField.get(handler);
            mQueue = messageQueue2;
            return messageQueue2;
        } catch (Exception e) {
            LogUtils.i(LogUtils.TAG, "HookMessage [hookMainMessageQueue] Exception=" + e.toString());
            e.printStackTrace();
            return null;
        }
    }

    public static Handler hookActivityThreadHandler() throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
        LogUtils.i(LogUtils.TAG, "HookMessage [hookActivityThreadHandler] start");
        Handler handler = mHandler;
        if (handler == null) {
            handler = null;
            try {
                Class<?> cls = Class.forName("android.app.ActivityThread");
                Object objInvoke = cls.getDeclaredMethod("currentActivityThread", new Class[0]).invoke(null, new Object[0]);
                Field declaredField = cls.getDeclaredField("mH");
                declaredField.setAccessible(true);
                Handler handler2 = (Handler) declaredField.get(objInvoke);
                mHandler = handler2;
                return handler2;
            } catch (Exception e) {
                LogUtils.i(LogUtils.TAG, "HookMessage [hookActivityThreadHandler] Exception=" + e.toString());
                e.printStackTrace();
            }
        }
        return handler;
    }

    public static MessageQueue getMessageQueue() {
        MessageQueue queue = Looper.getMainLooper().getQueue();
        if (queue == null) {
            LogUtils.i(LogUtils.TAG, "HookMessage [PrintAllMessage] queue is null");
        }
        return queue;
    }

    public static boolean isMainThreadBlocked(MessageQueue messageQueue) {
        boolean z = false;
        if (messageQueue != null) {
            try {
                Field declaredField = messageQueue.getClass().getDeclaredField("mMessages");
                declaredField.setAccessible(true);
                Message message = (Message) declaredField.get(messageQueue);
                if (message != null) {
                    LogUtils.i(LogUtils.TAG, "anrMessageString = " + message.toString());
                    long when = message.getWhen();
                    if (when != 0 && when - SystemClock.uptimeMillis() < -10000) {
                        z = true;
                    }
                } else {
                    LogUtils.i(LogUtils.TAG, "mMessage is null");
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        return z;
    }

    public static void getAllMessageFromMessageQueue(boolean z) throws NoSuchFieldException {
        Message nextMessage;
        LogUtils.i(LogUtils.TAG, "HookMessage [PrintAllMessage] start");
        MessageQueue messageQueue = getMessageQueue();
        if (messageQueue == null) {
            LogUtils.i(LogUtils.TAG, "HookMessage [PrintAllMessage] queue is null");
            return;
        }
        try {
            Field declaredField = Class.forName("android.os.MessageQueue").getDeclaredField("mMessages");
            declaredField.setAccessible(true);
            nextMessage = (Message) declaredField.get(messageQueue);
        } catch (Exception e) {
            e.printStackTrace();
            nextMessage = null;
        }
        if (nextMessage == null) {
            LogUtils.i(LogUtils.TAG, "HookMessage [PrintAllMessage] message is null");
        }
        synchronized (messageQueue) {
            if (!z) {
                int i = 0;
                while (nextMessage != null) {
                    i++;
                    LogUtils.i(LogUtils.TAG, "HookMessage [PrintAllMessage] \u961f\u5217\u4e2d\u672a\u5904\u7406\u7684\u6d88\u606f " + i + " = " + nextMessage.toString());
                    nextMessage = getNextMessage(nextMessage);
                    parseNonHandleMessage(nextMessage);
                }
            } else if (nextMessage != null) {
                LogUtils.i(LogUtils.TAG, "HookMessage [PrintAllMessage] Message info =" + nextMessage.toString());
            }
        }
    }

    private static void parseNonHandleMessage(Message message) {
        LogUtils.i(LogUtils.TAG, "HookMessage [parseNonHandleMessage] start");
        if (message == null) {
            LogUtils.i(LogUtils.TAG, "HookMessage [parseNonHandleMessage] params error");
            return;
        }
        NonHandleMessage nonHandleMessage = new NonHandleMessage();
        nonHandleMessage.setWhen(message.getWhen());
        if (message.getCallback() != null) {
            nonHandleMessage.setCallbackName(message.getCallback().getClass().getName());
        }
        nonHandleMessage.setWhat(message.what);
        if (message.getTarget() != null) {
            nonHandleMessage.setTargetName(message.getTarget().getClass().getName());
        }
        nonHandleMessage.setArg1(message.arg1);
        nonHandleMessage.setArg2(message.arg2);
        nonHandleMessage.setBarrier(message.arg1);
        mNonHandleMessageArrayList.add(nonHandleMessage);
    }

    private static Message getNextMessage(Message message) throws NoSuchFieldException {
        LogUtils.i(LogUtils.TAG, "HookMessage [getNextMessage] start");
        try {
            Field declaredField = Class.forName("android.os.Message").getDeclaredField("next");
            declaredField.setAccessible(true);
            return (Message) declaredField.get(message);
        } catch (Exception e) {
            LogUtils.i(LogUtils.TAG, "HookMessage [getNextMessage] Exception=" + e.toString());
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<NonHandleMessage> getNonHandleMessageArrayList() {
        return mNonHandleMessageArrayList;
    }
}