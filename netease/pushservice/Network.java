package com.netease.pushservice;

import android.content.Context;
import android.util.Base64;
import com.google.protobuf.InvalidProtocolBufferException;
import com.netease.push.proto.ProtoClientWrapper;
import com.netease.push.proto.nano.ClientSdkgate;
import com.netease.push.utils.PushLog;
import com.netease.push.utils.PushSetting;
import com.netease.rnccplayer.VideoViewManager;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes4.dex */
public class Network implements Runnable {
    private static final String TAG = "NGPush_" + Network.class.getSimpleName();
    private static Network network;
    private Timer mTimer;
    private Thread thread;
    private InetAddress inetAddr = null;
    private SocketAddress socketAddr = null;
    private Socket socket = null;
    private DataOutputStream socketWriter = null;
    private DataInputStream socketReader = null;
    private boolean mbConnected = false;
    private ReentrantLock mLock = new ReentrantLock();
    private boolean isEnable = false;
    private TimerTask heartBeatTask = null;
    private int HEART_BEAT_TIME = 60000;
    private int retryCount = 0;
    private long mHbRecvTime = 0;

    public static byte[] intToByteArray(int i) {
        return new byte[]{(byte) ((i >> 24) & 255), (byte) ((i >> 16) & 255), (byte) ((i >> 8) & 255), (byte) (i & 255)};
    }

    public static Network getInst() {
        if (network == null) {
            synchronized (Network.class) {
                if (network == null) {
                    network = new Network();
                }
            }
        }
        return network;
    }

    private Network() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        this.mTimer = null;
        this.mTimer = new Timer();
        PushLog.i(TAG, "Network constructed, this=" + this);
    }

    public void setHeartBeatTime(int i) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.d(TAG, "setHeartBeatTime:" + i);
        this.HEART_BEAT_TIME = i;
    }

    private int getRetrySecond() {
        if (this.retryCount > 7) {
            this.retryCount = 7;
        }
        int i = this.retryCount;
        int i2 = i * 36 * i;
        if (i2 <= 0) {
            i2 = 2;
        }
        this.retryCount++;
        return i2;
    }

    public void connectAuto(Context context) {
        PushLog.i(TAG, "connectAuto, this=" + this);
        PushServiceInfo notificationServiceInfo = PushServiceHelper.getInstance().getNotificationServiceInfo();
        PushLog.d(TAG, "niepushAddr--->" + PushSetting.getVaule(context, "niepushAddr"));
        String connectUrl = notificationServiceInfo.getConnectUrl(context);
        PushLog.d(TAG, "unipush addr:" + connectUrl);
        int iIndexOf = connectUrl.indexOf(":");
        if (iIndexOf != -1) {
            String strSubstring = connectUrl.substring(0, iIndexOf);
            String strSubstring2 = connectUrl.substring(iIndexOf + 1);
            int i = Integer.parseInt(strSubstring2);
            PushLog.d(TAG, String.format("connect to unipush %s:%s", strSubstring, strSubstring2));
            connect(strSubstring, i);
        }
    }

    public void connect(String str, int i) throws IllegalAccessException, IOException, IllegalArgumentException, InvocationTargetException {
        this.mLock.lock();
        PushLog.i(TAG, "connect");
        PushLog.d(TAG, "host:" + str);
        PushLog.d(TAG, "port:" + i);
        PushLog.d(TAG, "connect, this=" + this);
        if (this.mbConnected) {
            PushLog.w(TAG, "already connected");
            this.mLock.unlock();
            return;
        }
        if (!this.isEnable) {
            PushLog.w(TAG, "Disabled Network");
            this.mLock.unlock();
            return;
        }
        try {
            this.inetAddr = InetAddress.getByName(str);
            this.socketAddr = new InetSocketAddress(this.inetAddr, i);
            this.socket = new Socket();
            this.socket.setKeepAlive(true);
            this.socket.setSoTimeout(0);
            this.socket.connect(this.socketAddr, 5000);
            this.mbConnected = true;
            PushLog.i(TAG, "connect success, this=" + this);
            this.socket = upgradeToTls(this.socket, str);
            this.socketReader = new DataInputStream(this.socket.getInputStream());
            this.socketWriter = new DataOutputStream(this.socket.getOutputStream());
            this.thread = new Thread(this);
            this.thread.start();
            startHeartBeat();
            PushServiceHelper.getInstance().onConnectSuccess();
            this.retryCount = 0;
        } catch (Exception e) {
            PushLog.d(TAG, "connect exception:" + e.toString());
            this.mbConnected = false;
            e.printStackTrace();
        }
        if (!this.mbConnected) {
            disconnectRetry();
        }
        this.mLock.unlock();
    }

    public void disconnect() {
        this.mLock.lock();
        PushLog.i(TAG, "disconnect");
        try {
            if (this.socketReader != null) {
                this.socketReader.close();
            }
            if (this.socketWriter != null) {
                this.socketWriter.close();
            }
            if (this.socket != null) {
                this.socket.close();
            }
        } catch (IOException e) {
            PushLog.d(TAG, "IOException:" + e.getMessage());
        }
        this.socket = null;
        this.socketWriter = null;
        this.socketReader = null;
        this.mbConnected = false;
        PushLog.d(TAG, "disconnect, this=" + this);
        endHeartBeat();
        this.mTimer.purge();
        this.mLock.unlock();
    }

    public void stop() {
        PushLog.i(TAG, VideoViewManager.PROP_STOP);
        this.mLock.lock();
        disconnect();
        this.isEnable = false;
        network = null;
        this.mLock.unlock();
    }

    public void disconnectRetry() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "disconnectRetry");
        if (!this.isEnable) {
            PushLog.w(TAG, "connect not enable");
            return;
        }
        this.mHbRecvTime = 0L;
        disconnect();
        final int retrySecond = getRetrySecond();
        try {
            PushServiceHelper.getInstance().getTaskSubmitter().submit(new Runnable() { // from class: com.netease.pushservice.Network.1
                @Override // java.lang.Runnable
                public void run() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                    Network.this.connectRetry(retrySecond);
                }
            });
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void connectRetry(int i) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "connectRetry:" + i);
        this.mLock.lock();
        if (this.mbConnected) {
            PushLog.w(TAG, "already connected");
            this.mLock.unlock();
            return;
        }
        if (!this.isEnable) {
            PushLog.w(TAG, "connect not enable");
            this.mLock.unlock();
            return;
        }
        PushLog.d(TAG, "retry connect after:" + i);
        this.mTimer.schedule(new TimerTask() { // from class: com.netease.pushservice.Network.2
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                PushServiceHelper.getInstance().connect(false);
            }
        }, (long) (i * 1000));
        this.mLock.unlock();
    }

    private void startHeartBeat() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.d(TAG, "startHeartBeat");
        endHeartBeat();
        this.heartBeatTask = new TimerTask() { // from class: com.netease.pushservice.Network.3
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                PushLog.d(Network.TAG, "start sent a heart beat");
                if (0 == Network.this.mHbRecvTime) {
                    Network.this.mHbRecvTime = System.currentTimeMillis();
                }
                if (System.currentTimeMillis() - Network.this.mHbRecvTime > Network.this.HEART_BEAT_TIME * 3) {
                    Network.this.disconnectRetry();
                    return;
                }
                Network.this.sendSdkgateData(ClientSdkgate.HeartbeatRequest.newBuilder().build().toByteArray(), 2, 2);
                PushLog.d(Network.TAG, "end sent a heart beat");
            }
        };
        PushLog.d(TAG, "mTimer schedule");
        Timer timer = this.mTimer;
        TimerTask timerTask = this.heartBeatTask;
        int i = this.HEART_BEAT_TIME;
        timer.schedule(timerTask, i, i);
    }

    private void endHeartBeat() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.d(TAG, "endHeartBeat");
        TimerTask timerTask = this.heartBeatTask;
        if (timerTask != null) {
            timerTask.cancel();
        }
    }

    public void sendData(byte[] bArr) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        this.mLock.lock();
        if (!this.mbConnected) {
            PushLog.e(TAG, "not connected");
            this.mLock.unlock();
            return;
        }
        if (!this.socket.isConnected()) {
            PushLog.e(TAG, "socket not connected");
            this.mLock.unlock();
            return;
        }
        try {
            this.socketWriter.write(bArr);
        } catch (SocketException e) {
            PushLog.e(TAG, "SocketException:" + e.toString());
            disconnect();
        } catch (IOException e2) {
            PushLog.e(TAG, "IOException:" + e2.toString());
            disconnect();
        }
        this.mLock.unlock();
    }

    public static byte[] readAll(DataInputStream dataInputStream) throws IllegalAccessException, IOException, IllegalArgumentException, InvocationTargetException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[16384];
        while (true) {
            try {
                int i = dataInputStream.read(bArr, 0, bArr.length);
                if (i == -1) {
                    break;
                }
                PushLog.d(TAG, "in.read");
                byteArrayOutputStream.write(bArr, 0, i);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        dataInputStream.readFully(bArr, 0, 16384);
        PushLog.d(TAG, "buffer.toByteArray:" + Base64.encodeToString(bArr, 2));
        byteArrayOutputStream.write(bArr, 0, 16384);
        byteArrayOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }

    @Override // java.lang.Runnable
    public void run() throws IllegalAccessException, JSONException, IOException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "run");
        PushLog.d(TAG, "isEnable:" + this.isEnable);
        PushLog.d(TAG, "mbConnected:" + this.mbConnected);
        this.mLock.lock();
        if (!this.isEnable || !this.mbConnected) {
            this.mLock.unlock();
            return;
        }
        this.mLock.unlock();
        while (true) {
            try {
                byte[] bArr = new byte[4];
                this.socketReader.readFully(bArr, 0, 4);
                PushLog.d(TAG, "resultlength:" + byteArrayToInt(bArr));
                if (byteArrayToInt(bArr) > 104857600) {
                    break;
                }
                byte[] bArr2 = new byte[byteArrayToInt(bArr)];
                PushLog.d(TAG, "socketReader.readFully");
                this.socketReader.readFully(bArr2, 0, byteArrayToInt(bArr));
                byte b = bArr2[0];
                byte b2 = bArr2[1];
                byte[] bArr3 = {bArr2[4], bArr2[5]};
                int i = (bArr3[0] << 8) | (bArr3[1] & 255);
                PushLog.d(TAG, "mainCmd:" + ((int) b));
                PushLog.d(TAG, "subCmd:" + ((int) b2));
                PushLog.d(TAG, "code:" + i);
                byte[] bArr4 = new byte[byteArrayToInt(bArr) - 6];
                System.arraycopy(bArr2, 6, bArr4, 0, byteArrayToInt(bArr) - 6);
                PushLog.d(TAG, "onReceive");
                if (i == 0) {
                    if (b == 2 && b2 == 2) {
                        PushLog.d(TAG, "receive heartbeat reply");
                        this.mHbRecvTime = System.currentTimeMillis();
                    } else {
                        if (b == 2 && b2 == 1) {
                            PushLog.d(TAG, "loginRequest success reply");
                            JSONObject jSONObject = new JSONObject();
                            jSONObject.put("errorCode", 0);
                            jSONObject.put("msg", "success");
                            PushServiceHelper.getInstance().broadcastLoginCallback(jSONObject.toString());
                        }
                        if (b == 1 && b2 == 1) {
                            PushServiceHelper.getInstance().onReceive(ClientSdkgate.PreRegisterResponse.parseFrom(bArr4));
                        }
                        if (b == 1 && b2 == 2) {
                            PushServiceHelper.getInstance().onReceive(ClientSdkgate.RegisterResponse.parseFrom(bArr4));
                        }
                        if (b == 10 && b2 == 1) {
                            onReceive(bArr4);
                        }
                        if (b == 10 && b2 == 3) {
                            for (ClientSdkgate.Notification notification : ClientSdkgate.FindOfflineNotificationResponse.parseFrom(bArr4).getNotificationsList()) {
                                sendSdkgateData(ClientSdkgate.AckReceiveNotification.newBuilder().addPushIds(notification.getSystemContent().getPushId()).build().toByteArray(), 10, 2);
                                PushLog.d(TAG, "ackReceiveNotification PushId:" + notification.getSystemContent().getPushId());
                                PushServiceHelper.getInstance().onReceive(notification);
                            }
                        }
                    }
                } else if (i == 404) {
                    PushLog.d(TAG, "reget Token");
                    PushServiceHelper.regetToken = true;
                    PushServiceHelper.getInstance().onConnectSuccess();
                } else {
                    ClientSdkgate.ErrMsg from = ClientSdkgate.ErrMsg.parseFrom(bArr4);
                    PushLog.d(TAG, "loginRequest Failed reply:" + from.getErrMsg());
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put("errorCode", i);
                    jSONObject2.put("msg", from.getErrMsg());
                    PushServiceHelper.getInstance().broadcastLoginCallback(jSONObject2.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
                PushLog.d(TAG, "run, this=" + this);
                PushLog.e(TAG, "receive exception:" + e.toString());
            }
        }
        PushLog.d(TAG, "message length too long!");
        disconnectRetry();
    }

    public static int byteArrayToInt(byte[] bArr) {
        int i = 0;
        for (int i2 = 0; i2 < 4; i2++) {
            i += (bArr[i2] & 255) << ((3 - i2) * 8);
        }
        return i;
    }

    public void sendSdkgateData(byte[] bArr, int i, int i2) {
        PushLog.d(TAG, "sendSdkgateData");
        int length = bArr.length + 6;
        PushLog.d(TAG, "length:" + length);
        PushLog.d(TAG, "sdkmainCmd\uff1a" + i);
        PushLog.d(TAG, "sdksubCmd\uff1a" + i2);
        byte[] bArr2 = new byte[2];
        byte[] bArr3 = {(byte) i};
        byte[] bArr4 = {(byte) i2};
        byte[] bArr5 = {0, 0};
        byte[] bArr6 = new byte[length + 4];
        System.arraycopy(intToByteArray(length), 0, bArr6, 0, 4);
        System.arraycopy(bArr3, 0, bArr6, 4, bArr3.length);
        System.arraycopy(bArr4, 0, bArr6, bArr3.length + 4, bArr4.length);
        System.arraycopy(bArr2, 0, bArr6, bArr3.length + 4 + bArr4.length, bArr2.length);
        System.arraycopy(bArr5, 0, bArr6, bArr3.length + 4 + bArr4.length + bArr2.length, bArr5.length);
        System.arraycopy(bArr, 0, bArr6, bArr3.length + 4 + bArr4.length + bArr2.length + bArr5.length, bArr.length);
        String strEncodeToString = Base64.encodeToString(bArr6, 2);
        PushLog.i(TAG, "LoginRequest base64:" + strEncodeToString);
        sendData(bArr6);
    }

    public void sendData(ProtoClientWrapper.Packet packet) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        sendData(packet.Marshal());
    }

    public void sendData(byte b, ProtoClientWrapper.DataMarshal dataMarshal) {
        PushLog.i(TAG, "sendData, cmdType=" + ((int) b));
        sendData(ProtoClientWrapper.MarshalObject(b, dataMarshal));
    }

    private void onReceive(byte[] bArr) throws IllegalAccessException, JSONException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, String.format("OnReceive len=%d", Integer.valueOf(bArr.length)));
        try {
            ClientSdkgate.Notification from = ClientSdkgate.Notification.parseFrom(bArr);
            if (from != null) {
                PushLog.i(TAG, "notification:" + from.getContent());
                PushServiceHelper.getInstance().onReceive(from);
            }
            sendSdkgateData(ClientSdkgate.AckReceiveNotification.newBuilder().addPushIds(from.getSystemContent().getPushId()).build().toByteArray(), 10, 2);
            PushLog.d(TAG, "ackReceiveNotification PushId:" + from.getSystemContent().getPushId());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    protected void setEnable(boolean z) {
        PushLog.d(TAG, "setEnable:" + z);
        this.mLock.lock();
        this.isEnable = z;
        if (!this.isEnable) {
            disconnect();
        }
        this.mLock.unlock();
    }

    private Socket upgradeToTls(Socket socket, String str) throws IllegalAccessException, IOException, IllegalArgumentException, InvocationTargetException {
        try {
            SSLSocket sSLSocket = (SSLSocket) ((SSLSocketFactory) SSLSocketFactory.getDefault()).createSocket(socket, str, socket.getPort(), false);
            sSLSocket.setUseClientMode(true);
            sSLSocket.startHandshake();
            return sSLSocket;
        } catch (Exception e) {
            PushLog.e(TAG, "upgradeToTls Exception:" + e.toString());
            return socket;
        }
    }
}