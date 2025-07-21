package com.netease.push.utils;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import com.alipay.sdk.m.x.d;
import java.lang.reflect.InvocationTargetException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class NotifyMessage {
    private static final String TAG = "NGPush_NotifyMessage";
    private Class<?> m_clazzImpl;
    private Object m_instImpl;

    public NotifyMessage() {
        Log.i(TAG, "NotifyMessage constructed");
    }

    public void setImpl(Object obj) throws ClassNotFoundException {
        Log.i(TAG, "setImpl obj:" + obj);
        try {
            Class<?> cls = Class.forName("com.netease.push.utils.NotifyMessageImpl");
            this.m_clazzImpl = cls;
            this.m_instImpl = cls.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, String.format("class %s not found:%s", "com.netease.push.utils.NotifyMessageImpl", e.toString()));
        }
        this.m_instImpl = obj;
    }

    public Object getImpl() {
        return this.m_instImpl;
    }

    public String toString() {
        try {
            return (String) this.m_clazzImpl.getMethod("toString", new Class[0]).invoke(this.m_instImpl, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "toString exception:" + e.getMessage());
            return "";
        }
    }

    public String getPassJsonString() {
        try {
            return (String) this.m_clazzImpl.getMethod("getPassJsonString", new Class[0]).invoke(this.m_instImpl, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getPassJsonString exception:" + e.getMessage());
            return "";
        }
    }

    public void setPassJsonString(String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            this.m_clazzImpl.getMethod("setPassJsonString", String.class).invoke(this.m_instImpl, str);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "setPassJsonString exception:" + e.getMessage());
        }
    }

    public JSONObject getNgpushJson() {
        try {
            return (JSONObject) this.m_clazzImpl.getMethod("getNgpushJson", new Class[0]).invoke(this.m_instImpl, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getPassJsonString exception:" + e.getMessage());
            return new JSONObject();
        }
    }

    public void setNgpushJson(JSONObject jSONObject) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            this.m_clazzImpl.getMethod("setNgpushJson", String.class).invoke(this.m_instImpl, jSONObject);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "setPassJsonString exception:" + e.getMessage());
        }
    }

    public String getMsg() {
        try {
            return (String) this.m_clazzImpl.getMethod("getMsg", new Class[0]).invoke(this.m_instImpl, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getMsg exception:" + e.getMessage());
            return "";
        }
    }

    public void setMsg(String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            this.m_clazzImpl.getMethod("setMsg", String.class).invoke(this.m_instImpl, str);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "setMsg exception:" + e.getMessage());
        }
    }

    public String getTitle() {
        try {
            return (String) this.m_clazzImpl.getMethod("getTitle", new Class[0]).invoke(this.m_instImpl, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getTitle exception:" + e.getMessage());
            return "";
        }
    }

    public void setTitle(String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            this.m_clazzImpl.getMethod(d.o, String.class).invoke(this.m_instImpl, str);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "setTitle exception:" + e.getMessage());
        }
    }

    public String getExt() {
        try {
            return (String) this.m_clazzImpl.getMethod("getExt", new Class[0]).invoke(this.m_instImpl, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getExt exception:" + e.getMessage());
            return "";
        }
    }

    public void setExt(String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            this.m_clazzImpl.getMethod("setExt", String.class).invoke(this.m_instImpl, str);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "setExt exception:" + e.getMessage());
        }
    }

    public int getIcon() {
        try {
            return ((Integer) this.m_clazzImpl.getMethod("getIcon", new Class[0]).invoke(this.m_instImpl, new Object[0])).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getIcon exception:" + e.getMessage());
            return -1;
        }
    }

    public void setIcon(int i) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            this.m_clazzImpl.getMethod("setIcon", Integer.TYPE).invoke(this.m_instImpl, Integer.valueOf(i));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "setIcon exception:" + e.getMessage());
        }
    }

    public String getNotifyActivityName() {
        try {
            return (String) this.m_clazzImpl.getMethod("getNotifyActivityName", new Class[0]).invoke(this.m_instImpl, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getNotifyActivityName exception:" + e.getMessage());
            return "";
        }
    }

    public void setNotifyActivityName(String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            this.m_clazzImpl.getMethod("setNotifyActivityName", String.class).invoke(this.m_instImpl, str);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "setNotifyActivityName exception:" + e.getMessage());
        }
    }

    public int getNotifyid() {
        try {
            return ((Integer) this.m_clazzImpl.getMethod("getNotifyid", new Class[0]).invoke(this.m_instImpl, new Object[0])).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getNotifyid exception:" + e.getMessage());
            return 0;
        }
    }

    public String getReqid() {
        try {
            return (String) this.m_clazzImpl.getMethod("getReqid", new Class[0]).invoke(this.m_instImpl, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getReqid exception:" + e.getMessage());
            return "";
        }
    }

    public void setSound(String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            this.m_clazzImpl.getMethod("setSound", String.class).invoke(this.m_instImpl, str);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "setSound exception:" + e.getMessage());
        }
    }

    public String getSound() {
        try {
            return (String) this.m_clazzImpl.getMethod("getSound", new Class[0]).invoke(this.m_instImpl, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getSound exception:" + e.getMessage());
            return "";
        }
    }

    public String getServiceType() {
        try {
            return (String) this.m_clazzImpl.getMethod("getServiceType", new Class[0]).invoke(this.m_instImpl, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getServiceType exception:" + e.getMessage());
            return "";
        }
    }

    public boolean getNative() {
        try {
            return ((Boolean) this.m_clazzImpl.getMethod("getNative", new Class[0]).invoke(this.m_instImpl, new Object[0])).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getNative exception:" + e.getMessage());
            return false;
        }
    }

    public String getGroupId() {
        try {
            return (String) this.m_clazzImpl.getMethod("getGroupId", new Class[0]).invoke(this.m_instImpl, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getGroupId exception:" + e.getMessage());
            return "";
        }
    }

    public void setGroupId(String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            this.m_clazzImpl.getMethod("setGroupId", String.class).invoke(this.m_instImpl, str);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "setGroupId exception:" + e.getMessage());
        }
    }

    public String getGroupName() {
        try {
            return (String) this.m_clazzImpl.getMethod("getGroupName", new Class[0]).invoke(this.m_instImpl, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getGroupName exception:" + e.getMessage());
            return "";
        }
    }

    public void setGroupName(String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            this.m_clazzImpl.getMethod("setGroupName", String.class).invoke(this.m_instImpl, str);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "setGroupName exception:" + e.getMessage());
        }
    }

    public String getChannelId() {
        try {
            return (String) this.m_clazzImpl.getMethod("getChannelId", new Class[0]).invoke(this.m_instImpl, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getChannelId exception:" + e.getMessage());
            return "";
        }
    }

    public void setChannelId(String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            this.m_clazzImpl.getMethod("setChannelId", String.class).invoke(this.m_instImpl, str);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "setChannelId exception:" + e.getMessage());
        }
    }

    public String getChannelName() {
        try {
            return (String) this.m_clazzImpl.getMethod("getChannelName", new Class[0]).invoke(this.m_instImpl, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getChannelName exception:" + e.getMessage());
            return "";
        }
    }

    public void setChannelName(String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            this.m_clazzImpl.getMethod("setChannelName", String.class).invoke(this.m_instImpl, str);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "setChannelName exception:" + e.getMessage());
        }
    }

    public String getPush_id() {
        try {
            return (String) this.m_clazzImpl.getMethod("getPush_id", new Class[0]).invoke(this.m_instImpl, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getPush_id exception:" + e.getMessage());
            return "";
        }
    }

    public void setPush_id(String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            this.m_clazzImpl.getMethod("setPush_id", String.class).invoke(this.m_instImpl, str);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "setPush_id exception:" + e.getMessage());
        }
    }

    public String getPlan_id() {
        try {
            return (String) this.m_clazzImpl.getMethod("getPlan_id", new Class[0]).invoke(this.m_instImpl, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getPlan_id exception:" + e.getMessage());
            return "";
        }
    }

    public void setPlan_id(String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            this.m_clazzImpl.getMethod("setPlan_id", String.class).invoke(this.m_instImpl, str);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "setPlan_id exception:" + e.getMessage());
        }
    }

    public static NotifyMessage getFrom(Intent intent) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Log.i(TAG, "getFrom, intent:" + intent);
        try {
            Object objInvoke = Class.forName("com.netease.push.utils.NotifyMessageImpl").getMethod("getFrom", Intent.class).invoke(null, intent);
            if (objInvoke == null) {
                return null;
            }
            NotifyMessage notifyMessage = new NotifyMessage();
            notifyMessage.setImpl(objInvoke);
            return notifyMessage;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, String.format("class %s not found:%s", "com.netease.push.utils.NotifyMessageImpl", e.toString()));
            return null;
        }
    }

    public static NotifyMessage getFrom(Activity activity) {
        return getFrom(activity.getIntent());
    }
}