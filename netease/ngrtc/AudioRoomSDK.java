package com.netease.ngrtc;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.os.Build;
import android.os.CountDownTimer;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import androidx.core.app.ActivityCompat;
import com.CCMsgSdk.ControlCmdType;
import com.netease.ngrtc.ProtoClient;
import com.netease.ntunisdk.unilogger.global.Const;
import com.netease.pushclient.PushU3d;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import org.json.JSONObject;

/* loaded from: classes4.dex */
public class AudioRoomSDK implements NgRTCObserver {
    public static final int ERR_INIT_WEBRTC_FAILED = 2;
    public static final int ERR_NO_CALLBACK = 1;
    private static final String SDK_VERSION = "2.0.7";
    private static final int STATUS_CLOSED = 5;
    private static final int STATUS_CONNECTED = 3;
    private static final int STATUS_CONNECTING = 2;
    private static final int STATUS_DISCONNECTED = 4;
    private static final int STATUS_INITIALIZED = 1;
    private static final int STATUS_UNINIT = 0;
    private static final String TAG = "NGRTC_AudioRoomSDK";
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothHeadset mBluetoothHeadset;
    private BluetoothDevice mConnectedHeadset;
    private CountDownTimer mCountDown;
    private AudioManager m_audioManager;
    private int m_audioMode;
    private AudioRoomCallback m_cb;
    private Context m_ctx;
    private MusicIntentReceiver m_headsetReceiver;
    private String m_host;
    private int m_port;
    private String m_roomid;
    private String m_sessionid;
    private String m_uid;
    private static HashMap<Long, AudioRoomSDK> s_ngrtcCallbacks = new HashMap<>();
    private static int PERMISSION_REQ_CODE = 999;
    private long m_ngrtcInst = -1;
    private ReentrantLock m_lock = new ReentrantLock();
    private int m_status = 0;
    private boolean m_isMicrophoneMute = false;
    private boolean hasRecordGrante = true;
    private TaskSubmitter m_taskSubmitter = new TaskSubmitter();
    private List<Pair<String, Boolean>> m_permissions = new ArrayList();
    private boolean m_bWaitPermissionToJoin = false;
    private BluetoothProfile.ServiceListener mHeadsetProfileListener = new BluetoothProfile.ServiceListener() { // from class: com.netease.ngrtc.AudioRoomSDK.1
        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceDisconnected(int i) {
            Log.d(AudioRoomSDK.TAG, "Profile listener onServiceDisconnected");
            if (AudioRoomSDK.this.mBluetoothHeadset != null) {
                AudioRoomSDK.this.mBluetoothHeadset.stopVoiceRecognition(AudioRoomSDK.this.mConnectedHeadset);
                try {
                    AudioRoomSDK.this.m_ctx.unregisterReceiver(AudioRoomSDK.this.mHeadsetBroadcastReceiver);
                } catch (Exception e) {
                    Log.d(AudioRoomSDK.TAG, "exception msg:" + e.getMessage());
                }
                AudioRoomSDK.this.mBluetoothHeadset = null;
                AudioRoomSDK audioRoomSDK = AudioRoomSDK.this;
                audioRoomSDK.setSpeakerphoneOn(audioRoomSDK.m_speakerPhoneOn);
            }
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            String str;
            Log.d(AudioRoomSDK.TAG, "Profile listener onServiceConnected");
            AudioRoomSDK.this.mBluetoothHeadset = (BluetoothHeadset) bluetoothProfile;
            List<BluetoothDevice> connectedDevices = AudioRoomSDK.this.mBluetoothHeadset.getConnectedDevices();
            if (connectedDevices.size() > 0) {
                AudioRoomSDK.this.mConnectedHeadset = connectedDevices.get(0);
                if (AudioRoomSDK.this.mBluetoothHeadset.isAudioConnected(AudioRoomSDK.this.mConnectedHeadset)) {
                    str = "Profile listener audio already connected";
                } else {
                    str = AudioRoomSDK.this.mBluetoothHeadset.startVoiceRecognition(AudioRoomSDK.this.mConnectedHeadset) ? "Profile listener startVoiceRecognition returns true" : "Profile listener startVoiceRecognition returns false";
                }
                Log.d(AudioRoomSDK.TAG, str);
            }
            AudioRoomSDK.this.m_ctx.registerReceiver(AudioRoomSDK.this.mHeadsetBroadcastReceiver, new IntentFilter("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED"));
        }
    };
    private BroadcastReceiver mHeadsetBroadcastReceiver = new BroadcastReceiver() { // from class: com.netease.ngrtc.AudioRoomSDK.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            int intExtra;
            String str = "";
            if (intent.getAction().equals("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED")) {
                intExtra = intent.getIntExtra("android.bluetooth.profile.extra.STATE", 0);
                if (intExtra == 2) {
                    AudioRoomSDK.this.mConnectedHeadset = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                    if (AudioRoomSDK.this.mBluetoothHeadset.isAudioConnected(AudioRoomSDK.this.mConnectedHeadset)) {
                        str = "Headset connected audio already connected";
                    } else if (AudioRoomSDK.this.mBluetoothHeadset.startVoiceRecognition(AudioRoomSDK.this.mConnectedHeadset)) {
                        str = "Headset connected startVoiceRecognition returns true";
                    } else {
                        if (AudioRoomSDK.this.mCountDown == null) {
                            AudioRoomSDK.this.mCountDown = AudioRoomSDK.this.new MyCountDownTimer(10000L, 1000L);
                        }
                        AudioRoomSDK.this.mCountDown.start();
                        str = "Headset connected startVoiceRecognition returns false";
                    }
                } else if (intExtra == 0) {
                    AudioRoomSDK.this.mConnectedHeadset = null;
                    if (AudioRoomSDK.this.mCountDown != null) {
                        AudioRoomSDK.this.mCountDown.cancel();
                    }
                    AudioRoomSDK audioRoomSDK = AudioRoomSDK.this;
                    audioRoomSDK.setSpeakerphoneOn(audioRoomSDK.m_speakerPhoneOn);
                }
            } else {
                intExtra = -1;
            }
            Log.d(AudioRoomSDK.TAG, str + ", state:" + intExtra);
        }
    };
    private boolean m_speakerPhoneOn = true;
    private boolean isInRoom = false;

    private native long CreateNgRTC(NgRTCObserver ngRTCObserver, Context context, String str, int i);

    private native void GetParticipants(long j, int i);

    private native void JoinRoom(long j, String str, String str2, String str3);

    private native void LeaveRoom(long j);

    private native void MuteInput(long j, boolean z);

    private native void MuteInputUid(long j, boolean z, String str);

    private native void MuteOutput(long j, boolean z, String str);

    private native void ReleaseNgRTC(long j);

    private native void SetConfig(long j, String str);

    private native void SetOutputVolumeScaling(long j, float f);

    public String version() {
        return SDK_VERSION;
    }

    static {
        try {
            System.loadLibrary("c++_shared");
            System.loadLibrary("ngrtc_so");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AudioRoomSDK() {
        Log.i(TAG, "AudioRoomSDK constructed");
        this.m_permissions.add(Pair.create("android.permission.RECORD_AUDIO", false));
        this.m_permissions.add(Pair.create("android.permission.MODIFY_AUDIO_SETTINGS", false));
    }

    public void setAddr(String str, int i) {
        String str2 = TAG;
        Log.i(str2, "setAddr");
        Log.d(str2, "host=" + str);
        Log.d(str2, "port=" + i);
        this.m_host = str;
        this.m_port = i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSpeakerphoneOn(boolean z) {
        String str = TAG;
        Log.i(str, "setSpeakerphoneOn:" + z);
        this.m_speakerPhoneOn = z;
        String str2 = Build.MODEL;
        Log.i(str, "Build.MODEL:" + str2);
        if (str2 != null && (str2.equalsIgnoreCase("MIX 2S") || str2.equalsIgnoreCase("MI 8"))) {
            Log.i(str, "mix setMode(AudioManager.MODE_IN_COMMUNICATION)");
            this.m_audioManager.setMode(3);
        }
        if (str2 != null && !str2.startsWith("MI") && !str2.equalsIgnoreCase("G8342")) {
            Log.i(str, "setMode(AudioManager.MODE_IN_COMMUNICATION)");
            this.m_audioManager.setMode(3);
        }
        if (z) {
            this.m_audioManager.setSpeakerphoneOn(z);
        } else {
            this.m_audioManager.setSpeakerphoneOn(z);
        }
    }

    public boolean isSpeakerphoneOn() {
        return this.m_audioManager.isSpeakerphoneOn();
    }

    public void onResume() {
        Log.i(TAG, "onResume");
    }

    public void onPause() {
        Log.i(TAG, "onPause");
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        String str = TAG;
        Log.i(str, "onRequestPermissionsResult");
        Log.d(str, "reqCode:" + i);
        StringBuilder sb = new StringBuilder("permissions:");
        sb.append(strArr.length > 0 ? strArr[0] : "");
        Log.d(str, sb.toString());
        StringBuilder sb2 = new StringBuilder("grantResults:");
        sb2.append(iArr.length > 0 ? Integer.valueOf(iArr[0]) : "");
        Log.d(str, sb2.toString());
        if (this.m_bWaitPermissionToJoin && iArr.length > 0 && strArr.length > 0) {
            boolean z = iArr[0] == 0;
            Log.d(str, "permission granted for " + strArr[0] + ":" + z);
            onRequestPermissionsGranted(strArr[0], z);
        }
    }

    public boolean initialize(Context context, String str, int i, AudioRoomCallback audioRoomCallback) {
        String str2 = TAG;
        Log.i(str2, "initialize");
        Log.d(str2, "host=" + str);
        Log.d(str2, "port=" + i);
        this.m_ctx = context;
        this.m_host = str;
        this.m_port = i;
        this.m_cb = audioRoomCallback;
        if (audioRoomCallback == null) {
            onInitFailedCB(1, "AudioRoomCallback not specified");
            return false;
        }
        AudioManager audioManager = (AudioManager) context.getSystemService("audio");
        this.m_audioManager = audioManager;
        this.m_isMicrophoneMute = audioManager.isMicrophoneMute();
        this.m_audioMode = this.m_audioManager.getMode();
        Log.i(str2, "initialize m_audioMode:" + this.m_audioMode);
        Log.i(str2, "initialize m_speakerPhoneOn:" + this.m_speakerPhoneOn);
        setPhoneStateListener();
        this.m_headsetReceiver = new MusicIntentReceiver(this, null);
        long jCreateNgRTC = CreateNgRTC(this, this.m_ctx, this.m_host, this.m_port);
        this.m_ngrtcInst = jCreateNgRTC;
        if (-1 == jCreateNgRTC) {
            onInitFailedCB(2, "create ngrtc inst failed");
            return false;
        }
        s_ngrtcCallbacks.put(Long.valueOf(jCreateNgRTC), this);
        onInitSuccessCB();
        return true;
    }

    public void joinRoom(String str, String str2, String str3) {
        String str4 = TAG;
        Log.i(str4, "joinRoom from sdk user");
        Log.d(str4, "uid=" + str);
        Log.d(str4, "sessionid=" + str2);
        Log.d(str4, "roomid=" + str3);
        this.m_lock.lock();
        this.m_bWaitPermissionToJoin = true;
        this.m_uid = str;
        this.m_sessionid = str2;
        this.m_roomid = str3;
        this.m_lock.unlock();
        checkPermissions();
    }

    public void joinRoomAfterRequestPermission() {
        this.m_taskSubmitter.submit(new Runnable() { // from class: com.netease.ngrtc.AudioRoomSDK.3
            @Override // java.lang.Runnable
            public void run() {
                Log.i(AudioRoomSDK.TAG, "joinRoomAfterRequestPermission");
                Log.d(AudioRoomSDK.TAG, "m_uid=" + AudioRoomSDK.this.m_uid);
                Log.d(AudioRoomSDK.TAG, "m_sessionid=" + AudioRoomSDK.this.m_sessionid);
                Log.d(AudioRoomSDK.TAG, "m_roomid=" + AudioRoomSDK.this.m_roomid);
                if (!AudioRoomSDK.this.checkAudioRecordPermission("android.permission.RECORD_AUDIO")) {
                    Log.d(AudioRoomSDK.TAG, "permission RECORD_AUDIO not granted, muteInput true");
                    AudioRoomSDK.this.hasRecordGrante = false;
                    AudioRoomSDK.this.onErrorCB(ProtoClient.RTCError.ERR_PERMISSION_DENY, String.format("permission %s not granted", "android.permission.RECORD_AUDIO"));
                } else {
                    if (AudioRoomSDK.this.m_ctx.checkCallingOrSelfPermission("android.permission.MODIFY_AUDIO_SETTINGS") != 0) {
                        AudioRoomSDK.this.onErrorCB(ProtoClient.RTCError.ERR_PERMISSION_DENY, String.format("permission %s not granted", "android.permission.MODIFY_AUDIO_SETTINGS"));
                    }
                    AudioRoomSDK.this.m_lock.lock();
                    AudioRoomSDK.this.m_bWaitPermissionToJoin = false;
                    AudioRoomSDK.this.m_lock.unlock();
                    AudioRoomSDK.this._joinRoom(false);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void _joinRoom(boolean z) {
        String str = TAG;
        Log.i(str, "_joinRoom");
        Log.d(str, "bReconnect=" + z);
        Log.d(str, "uid=" + this.m_uid);
        Log.d(str, "sessionid=" + this.m_sessionid);
        Log.d(str, "roomid=" + this.m_roomid);
        setStatus(2);
        JoinRoom(this.m_ngrtcInst, this.m_uid, this.m_sessionid, this.m_roomid);
    }

    public void muteOutput(final boolean z, final String str) {
        this.m_taskSubmitter.submit(new Runnable() { // from class: com.netease.ngrtc.AudioRoomSDK.4
            @Override // java.lang.Runnable
            public void run() {
                Log.i(AudioRoomSDK.TAG, "muteOutput from sdk user");
                AudioRoomSDK.this._muteOutput(z, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void _muteOutput(boolean z, String str) {
        String str2 = TAG;
        Log.i(str2, "_muteOutput");
        Log.d(str2, "mute:" + z);
        Log.d(str2, "uid:" + str);
        Log.d(str2, "m_status:" + this.m_status);
        if (this.m_status >= 3) {
            MuteOutput(this.m_ngrtcInst, z, str);
            if (z && Const.CONFIG_KEY.ALL.equals(str)) {
                this.m_audioManager.setMode(this.m_audioMode);
                return;
            }
            if (this.isInRoom) {
                Log.i(str2, "_muteOutput isInRoom");
                setSpeakerphoneOn(this.m_speakerPhoneOn);
                if (this.mBluetoothAdapter == null || !this.m_audioManager.isBluetoothScoAvailableOffCall()) {
                    return;
                }
                this.mBluetoothAdapter.getProfileProxy(this.m_ctx, this.mHeadsetProfileListener, 1);
            }
        }
    }

    public void muteInput(final boolean z) {
        this.m_taskSubmitter.submit(new Runnable() { // from class: com.netease.ngrtc.AudioRoomSDK.5
            @Override // java.lang.Runnable
            public void run() {
                Log.i(AudioRoomSDK.TAG, "muteInput from sdk user");
                AudioRoomSDK.this._muteInput(z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void _muteInput(boolean z) {
        String str = TAG;
        Log.i(str, "_muteInput:" + z);
        Log.d(str, "m_status:" + this.m_status);
        if (this.m_status >= 3) {
            if (!z) {
                if (!checkAudioRecordPermission("android.permission.RECORD_AUDIO")) {
                    Log.d(str, "permission RECORD_AUDIO not granted, muteInput true");
                    this.hasRecordGrante = false;
                    return;
                } else {
                    Log.d(str, "permission RECORD_AUDIO granted, muteInput false");
                    this.hasRecordGrante = true;
                }
            }
            MuteInput(this.m_ngrtcInst, z);
        }
    }

    public void muteInput(final boolean z, final String str) {
        this.m_taskSubmitter.submit(new Runnable() { // from class: com.netease.ngrtc.AudioRoomSDK.6
            @Override // java.lang.Runnable
            public void run() {
                Log.i(AudioRoomSDK.TAG, "muteInput from sdk user");
                AudioRoomSDK.this._muteInput(z, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void _muteInput(boolean z, String str) {
        String str2 = TAG;
        Log.i(str2, "_muteInput:" + z + ", uid:" + str);
        StringBuilder sb = new StringBuilder("m_status:");
        sb.append(this.m_status);
        Log.d(str2, sb.toString());
        if (this.m_status >= 3) {
            if (!z) {
                if (!checkAudioRecordPermission("android.permission.RECORD_AUDIO")) {
                    Log.d(str2, "permission RECORD_AUDIO not granted, muteInput true");
                    this.hasRecordGrante = false;
                    return;
                } else {
                    Log.d(str2, "permission RECORD_AUDIO granted, muteInput false");
                    this.hasRecordGrante = true;
                }
            }
            MuteInputUid(this.m_ngrtcInst, z, str);
        }
    }

    public void getParticipants(final int i) {
        this.m_taskSubmitter.submit(new Runnable() { // from class: com.netease.ngrtc.AudioRoomSDK.7
            @Override // java.lang.Runnable
            public void run() {
                Log.i(AudioRoomSDK.TAG, "getParticipants from sdk user");
                AudioRoomSDK.this._getParticipants(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void _getParticipants(int i) {
        String str = TAG;
        Log.i(str, "_getParticipants, offset:" + i);
        Log.d(str, "m_status:" + this.m_status);
        if (this.m_status >= 3) {
            GetParticipants(this.m_ngrtcInst, i);
        }
    }

    public void leaveRoom() {
        this.m_taskSubmitter.submit(new Runnable() { // from class: com.netease.ngrtc.AudioRoomSDK.8
            @Override // java.lang.Runnable
            public void run() {
                Log.i(AudioRoomSDK.TAG, "leaveRoom from sdk user");
                AudioRoomSDK.this._leaveRoom();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void _leaveRoom() {
        Log.e(TAG, "_leaveRoom");
        LeaveRoom(this.m_ngrtcInst);
        this.m_audioManager.setMicrophoneMute(this.m_isMicrophoneMute);
        this.m_audioManager.setMode(this.m_audioMode);
        MusicIntentReceiver musicIntentReceiver = this.m_headsetReceiver;
        if (musicIntentReceiver != null) {
            try {
                this.m_ctx.unregisterReceiver(musicIntentReceiver);
            } catch (Exception unused) {
            }
        }
        BluetoothHeadset bluetoothHeadset = this.mBluetoothHeadset;
        if (bluetoothHeadset != null) {
            BluetoothDevice bluetoothDevice = this.mConnectedHeadset;
            if (bluetoothDevice != null) {
                bluetoothHeadset.stopVoiceRecognition(bluetoothDevice);
            }
            try {
                this.m_ctx.unregisterReceiver(this.mHeadsetBroadcastReceiver);
            } catch (Exception e) {
                Log.d(TAG, "exception msg:" + e.getMessage());
            }
            this.mBluetoothHeadset = null;
        }
        this.isInRoom = false;
    }

    public void close() {
        String str = TAG;
        Log.e(str, ControlCmdType.CLOSE);
        this.m_lock.lock();
        this.m_bWaitPermissionToJoin = false;
        if (this.m_status >= 5) {
            this.m_lock.unlock();
            Log.d(str, "close1");
            return;
        }
        this.m_status = 5;
        this.m_audioManager.setMicrophoneMute(this.m_isMicrophoneMute);
        this.m_audioManager.setMode(this.m_audioMode);
        s_ngrtcCallbacks.remove(Long.valueOf(this.m_ngrtcInst));
        this.m_lock.unlock();
        ReleaseNgRTC(this.m_ngrtcInst);
    }

    private void setStatus(int i) {
        this.m_lock.lock();
        this.m_status = i;
        this.m_lock.unlock();
    }

    public void setConfig(String str) {
        String str2 = TAG;
        Log.d(str2, "setConfig:" + str);
        if (this.m_status >= 3) {
            SetConfig(this.m_ngrtcInst, str);
        } else {
            Log.d(str2, "m_status < STATUS_CONNECTED");
        }
    }

    public void setOutputVolumeScaling(float f) {
        String str = TAG;
        Log.d(str, "setOutputVolumeScaling:" + f);
        if (this.m_status >= 3) {
            SetOutputVolumeScaling(this.m_ngrtcInst, f);
        } else {
            Log.d(str, "m_status < STATUS_CONNECTED");
        }
    }

    private void onInitSuccessCB() {
        Log.i(TAG, PushU3d.CALLBACKTYPE_onInitSuccess);
        setStatus(1);
        if (this.m_cb != null) {
            ((Activity) this.m_ctx).runOnUiThread(new Runnable() { // from class: com.netease.ngrtc.AudioRoomSDK.9
                @Override // java.lang.Runnable
                public void run() {
                    AudioRoomSDK.this.m_cb.onInitSuccess();
                }
            });
        }
    }

    private void onInitFailedCB(final int i, final String str) {
        Log.e(TAG, "onInitFailed, errCode:%v, errMsg:" + str);
        if (this.m_cb != null) {
            ((Activity) this.m_ctx).runOnUiThread(new Runnable() { // from class: com.netease.ngrtc.AudioRoomSDK.10
                @Override // java.lang.Runnable
                public void run() {
                    AudioRoomSDK.this.m_cb.onInitFailed(i, str);
                }
            });
        }
    }

    private void onSpeakBeginCB(final String str) {
        String str2 = TAG;
        Log.i(str2, "onSpeakBeginCB");
        Log.d(str2, "uid:" + str);
        if (this.m_cb != null) {
            ((Activity) this.m_ctx).runOnUiThread(new Runnable() { // from class: com.netease.ngrtc.AudioRoomSDK.11
                @Override // java.lang.Runnable
                public void run() {
                    AudioRoomSDK.this.m_cb.onSpeakBegin(str);
                }
            });
        }
    }

    private void onSpeakEndCB(final String str) {
        String str2 = TAG;
        Log.i(str2, "onSpeakEndCB");
        Log.d(str2, "uid:" + str);
        if (this.m_cb != null) {
            ((Activity) this.m_ctx).runOnUiThread(new Runnable() { // from class: com.netease.ngrtc.AudioRoomSDK.12
                @Override // java.lang.Runnable
                public void run() {
                    AudioRoomSDK.this.m_cb.onSpeakEnd(str);
                }
            });
        }
    }

    private void onConnectSuccessCB(final boolean z) {
        String str = TAG;
        Log.i(str, "onConnectSuccessCB");
        Log.d(str, "bReconnect=" + z);
        this.m_audioManager.setMicrophoneMute(false);
        ((Activity) this.m_ctx).setVolumeControlStream(0);
        if (this.m_cb != null) {
            ((Activity) this.m_ctx).runOnUiThread(new Runnable() { // from class: com.netease.ngrtc.AudioRoomSDK.13
                @Override // java.lang.Runnable
                public void run() {
                    AudioRoomSDK.this.isInRoom = true;
                    if (!AudioRoomSDK.this.hasRecordGrante) {
                        Log.d(AudioRoomSDK.TAG, "permission RECORD_AUDIO not granted, muteInput true");
                        AudioRoomSDK.this.muteInput(true);
                    }
                    AudioRoomSDK audioRoomSDK = AudioRoomSDK.this;
                    audioRoomSDK.setSpeakerphoneOn(audioRoomSDK.m_speakerPhoneOn);
                    if (AudioRoomSDK.this.m_headsetReceiver != null) {
                        AudioRoomSDK.this.m_ctx.registerReceiver(AudioRoomSDK.this.m_headsetReceiver, new IntentFilter("android.intent.action.HEADSET_PLUG"));
                    }
                    AudioRoomSDK.this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (AudioRoomSDK.this.mBluetoothAdapter != null && AudioRoomSDK.this.m_audioManager.isBluetoothScoAvailableOffCall()) {
                        AudioRoomSDK.this.mBluetoothAdapter.getProfileProxy(AudioRoomSDK.this.m_ctx, AudioRoomSDK.this.mHeadsetProfileListener, 1);
                    }
                    AudioRoomSDK.this.m_cb.onConnectSuccess(z);
                }
            });
        }
    }

    private void onReconnectCB(final ProtoClient.RTCError rTCError) {
        String str = TAG;
        Log.i(str, "onReconnectCB");
        Log.d(str, "errCode=" + rTCError);
        if (this.m_cb != null) {
            ((Activity) this.m_ctx).runOnUiThread(new Runnable() { // from class: com.netease.ngrtc.AudioRoomSDK.14
                @Override // java.lang.Runnable
                public void run() {
                    AudioRoomSDK.this.m_cb.onReconnect(rTCError);
                }
            });
        }
    }

    private void onDisconnectCB(final ProtoClient.RTCError rTCError, final String str) {
        String str2 = TAG;
        Log.i(str2, "onDisconnectCB");
        Log.d(str2, "errCode=" + rTCError);
        Log.d(str2, "reason=" + str);
        if (this.m_cb != null) {
            ((Activity) this.m_ctx).runOnUiThread(new Runnable() { // from class: com.netease.ngrtc.AudioRoomSDK.15
                @Override // java.lang.Runnable
                public void run() {
                    AudioRoomSDK.this.m_cb.onDisconnect(rTCError, str);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onErrorCB(final ProtoClient.RTCError rTCError, final String str) {
        String str2 = TAG;
        Log.i(str2, "onErrorCB");
        Log.d(str2, "errCode=" + rTCError);
        Log.d(str2, "errMsg=" + str);
        if (this.m_cb != null) {
            ((Activity) this.m_ctx).runOnUiThread(new Runnable() { // from class: com.netease.ngrtc.AudioRoomSDK.16
                @Override // java.lang.Runnable
                public void run() {
                    AudioRoomSDK.this.m_cb.onError(rTCError, str);
                }
            });
        }
    }

    private void onMemberJoinedCB(final ParticipantInfo participantInfo) {
        String str = TAG;
        Log.i(str, "onMemberJoinedCB");
        Log.d(str, "member:" + participantInfo);
        if (this.m_cb != null) {
            ((Activity) this.m_ctx).runOnUiThread(new Runnable() { // from class: com.netease.ngrtc.AudioRoomSDK.17
                @Override // java.lang.Runnable
                public void run() {
                    AudioRoomSDK.this.m_cb.onMemberJoined(participantInfo);
                }
            });
        }
    }

    private void onMemberMutedCB(final ParticipantInfo participantInfo) {
        String str = TAG;
        Log.i(str, "onMemberMutedCB");
        Log.d(str, "member:" + participantInfo);
        if (this.m_cb != null) {
            ((Activity) this.m_ctx).runOnUiThread(new Runnable() { // from class: com.netease.ngrtc.AudioRoomSDK.18
                @Override // java.lang.Runnable
                public void run() {
                    AudioRoomSDK.this.m_cb.onMemberMuted(participantInfo);
                }
            });
        }
    }

    private void onMemberLeavedCB(final ParticipantInfo participantInfo) {
        String str = TAG;
        Log.i(str, "onMemberLeavedCB");
        Log.d(str, "member:" + participantInfo);
        if (this.m_cb != null) {
            ((Activity) this.m_ctx).runOnUiThread(new Runnable() { // from class: com.netease.ngrtc.AudioRoomSDK.19
                @Override // java.lang.Runnable
                public void run() {
                    AudioRoomSDK.this.m_cb.onMemberLeaved(participantInfo);
                }
            });
        }
    }

    private void onParticipantListRefreshedCB(final ArrayList<ParticipantInfo> arrayList, final int i, final int i2, final int i3) {
        String str = TAG;
        Log.i(str, "onParticipantListRefreshedCB");
        Log.d(str, "list=" + arrayList);
        Log.d(str, "total=" + i);
        Log.d(str, "offset=" + i2);
        Log.d(str, "offsetnext=" + i3);
        if (this.m_cb != null) {
            ((Activity) this.m_ctx).runOnUiThread(new Runnable() { // from class: com.netease.ngrtc.AudioRoomSDK.20
                @Override // java.lang.Runnable
                public void run() {
                    AudioRoomSDK.this.m_cb.onParticipantListRefreshed(arrayList, i, i2, i3);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onHeadsetPluggedCB(final boolean z) {
        String str = TAG;
        Log.i(str, "onHeadsetPluggedCB");
        Log.d(str, "plugged:" + z);
        if (this.m_cb != null) {
            ((Activity) this.m_ctx).runOnUiThread(new Runnable() { // from class: com.netease.ngrtc.AudioRoomSDK.21
                @Override // java.lang.Runnable
                public void run() {
                    AudioRoomSDK.this.m_cb.onHeadsetPlugged(z);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkAudioRecordPermission(String str) throws IllegalStateException {
        int i;
        Log.i(TAG, "checkAudioRecordPermission");
        try {
            i = this.m_ctx.getPackageManager().getPackageInfo(this.m_ctx.getPackageName(), 0).applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            i = 0;
        }
        if (this.m_ctx.checkCallingOrSelfPermission(str) != 0) {
            Log.e(TAG, String.format("permission %s not granted", str));
            return false;
        }
        if (i >= 23) {
            return true;
        }
        try {
            AudioRecord audioRecord = new AudioRecord(1, 8000, 16, 2, AudioRecord.getMinBufferSize(8000, 16, 2));
            audioRecord.startRecording();
            int recordingState = audioRecord.getRecordingState();
            if (recordingState != 3) {
                Log.e(TAG, String.format("permission denied, record state:%d", Integer.valueOf(recordingState)));
                return false;
            }
            audioRecord.stop();
            audioRecord.release();
            return true;
        } catch (Exception e2) {
            e2.printStackTrace();
            Log.e(TAG, "permission denied");
            return false;
        }
    }

    private void checkPermissions() {
        Log.i(TAG, "checkPermissions");
        this.hasRecordGrante = true;
        for (int i = 0; i < this.m_permissions.size(); i++) {
            this.m_permissions.set(i, Pair.create((String) this.m_permissions.get(i).first, false));
        }
        requestPermission();
    }

    private void requestPermission() {
        String str;
        String str2 = TAG;
        Log.i(str2, "requestPermission");
        Log.d(str2, "m_ctx:" + this.m_ctx);
        Iterator<Pair<String, Boolean>> it = this.m_permissions.iterator();
        while (true) {
            if (!it.hasNext()) {
                str = "";
                break;
            }
            Pair<String, Boolean> next = it.next();
            if (!((Boolean) next.second).booleanValue()) {
                str = (String) next.first;
                break;
            }
        }
        String str3 = TAG;
        Log.d(str3, "permission:" + str);
        if (TextUtils.isEmpty(str)) {
            Log.e(str3, "onRequestPermissionsGranted over");
            joinRoomAfterRequestPermission();
            return;
        }
        if (this.m_ctx.checkCallingOrSelfPermission(str) != 0) {
            try {
                ActivityCompat.requestPermissions((Activity) this.m_ctx, new String[]{str}, PERMISSION_REQ_CODE);
                Log.i(str3, "requestPermissions " + str + " sent");
                return;
            } catch (Exception e) {
                Log.e(TAG, "requestPermissions " + str + " failed:" + e.toString());
                e.printStackTrace();
                onRequestPermissionsGranted(str, true);
                return;
            }
        }
        Log.d(str3, "has been granted permission:" + str);
        onRequestPermissionsGranted(str, true);
    }

    private void onRequestPermissionsGranted(String str, boolean z) {
        String str2 = TAG;
        Log.i(str2, "onRequestPermissionsGranted");
        Log.d(str2, "permission:" + str);
        Log.d(str2, "granted:" + z);
        if (!z) {
            Log.e(str2, "onRequestPermissionsGranted refused");
            joinRoomAfterRequestPermission();
            return;
        }
        int i = 0;
        while (true) {
            if (i >= this.m_permissions.size()) {
                break;
            }
            Pair<String, Boolean> pair = this.m_permissions.get(i);
            if (str.equalsIgnoreCase((String) pair.first)) {
                this.m_permissions.set(i, Pair.create((String) pair.first, true));
                break;
            }
            i++;
        }
        requestPermission();
    }

    private SharedPreferences getSharedPref() {
        Context context = this.m_ctx;
        return context.getSharedPreferences(String.valueOf(context.getPackageName()) + "_NGRTC", 0);
    }

    private class MusicIntentReceiver extends BroadcastReceiver {
        private MusicIntentReceiver() {
        }

        /* synthetic */ MusicIntentReceiver(AudioRoomSDK audioRoomSDK, MusicIntentReceiver musicIntentReceiver) {
            this();
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            Log.i(AudioRoomSDK.TAG, "setPhoneStateListener receive");
            if (intent.getAction().equals("android.intent.action.HEADSET_PLUG")) {
                int intExtra = intent.getIntExtra("state", -1);
                if (intExtra == 0) {
                    Log.d(AudioRoomSDK.TAG, "Headset is unplugged");
                    if (AudioRoomSDK.this.isInRoom) {
                        AudioRoomSDK.this.setSpeakerphoneOn(true);
                        AudioRoomSDK.this.onHeadsetPluggedCB(false);
                        return;
                    } else {
                        AudioRoomSDK.this.m_speakerPhoneOn = true;
                        return;
                    }
                }
                if (intExtra != 1) {
                    Log.d(AudioRoomSDK.TAG, "I have no idea what the headset state is");
                    return;
                }
                Log.d(AudioRoomSDK.TAG, "Headset is plugged");
                if (AudioRoomSDK.this.isInRoom) {
                    AudioRoomSDK.this.setSpeakerphoneOn(false);
                    AudioRoomSDK.this.onHeadsetPluggedCB(true);
                } else {
                    AudioRoomSDK.this.m_speakerPhoneOn = false;
                }
            }
        }
    }

    private void setPhoneStateListener() {
        Log.i(TAG, "setPhoneStateListener");
        Context context = this.m_ctx;
        if (context == null) {
            return;
        }
        ((Activity) context).runOnUiThread(new Runnable() { // from class: com.netease.ngrtc.AudioRoomSDK.22
            @Override // java.lang.Runnable
            public void run() {
                try {
                    ((TelephonyManager) AudioRoomSDK.this.m_ctx.getSystemService("phone")).listen(new PhoneStateListener() { // from class: com.netease.ngrtc.AudioRoomSDK.22.1
                        @Override // android.telephony.PhoneStateListener
                        public void onCallStateChanged(int i, String str) {
                            Log.e(AudioRoomSDK.TAG, String.format("onCallStateChanged, state:%d, incomingNumber:%s", Integer.valueOf(i), str));
                            if (i == 0) {
                                if (AudioRoomSDK.this.isInRoom) {
                                    AudioRoomSDK.this.setSpeakerphoneOn(AudioRoomSDK.this.m_speakerPhoneOn);
                                }
                                Log.d(AudioRoomSDK.TAG, "Hangup, m_speakerPhoneOn:" + AudioRoomSDK.this.m_speakerPhoneOn);
                                return;
                            }
                            if (i != 1) {
                                if (i != 2) {
                                    return;
                                }
                                Log.d(AudioRoomSDK.TAG, "Outgoing, m_speakerPhoneOn:" + AudioRoomSDK.this.m_speakerPhoneOn);
                                return;
                            }
                            if (AudioRoomSDK.this.isInRoom) {
                                AudioRoomSDK.this.m_speakerPhoneOn = AudioRoomSDK.this.isSpeakerphoneOn();
                            }
                            Log.d(AudioRoomSDK.TAG, "Incoming, m_speakerPhoneOn:" + AudioRoomSDK.this.m_speakerPhoneOn);
                        }
                    }, 32);
                } catch (Exception e) {
                    Log.i(AudioRoomSDK.TAG, "setPhoneStateListener exception:" + e.getMessage());
                }
            }
        });
    }

    public static void OnConnectSuccess(long j, boolean z) {
        Log.i(TAG, "OnConnectSuccess");
        AudioRoomSDK audioRoomSDK = s_ngrtcCallbacks.get(Long.valueOf(j));
        if (audioRoomSDK != null) {
            audioRoomSDK.OnConnectSuccess(z);
        }
    }

    @Override // com.netease.ngrtc.NgRTCObserver
    public void OnConnectSuccess(boolean z) {
        String str = TAG;
        Log.i(str, "OnConnectSuccess");
        Log.d(str, "bReconnect:" + z);
        setStatus(3);
        onConnectSuccessCB(z);
    }

    public static void OnReconnect(long j, int i) {
        Log.i(TAG, "OnReconnect");
        AudioRoomSDK audioRoomSDK = s_ngrtcCallbacks.get(Long.valueOf(j));
        if (audioRoomSDK != null) {
            audioRoomSDK.OnReconnect(i);
        }
    }

    @Override // com.netease.ngrtc.NgRTCObserver
    public void OnReconnect(int i) {
        String str = TAG;
        Log.i(str, "OnReconnect");
        Log.d(str, "errCode:" + i);
        onReconnectCB(ProtoClient.getRTCError(i));
    }

    public static void OnDisconnect(long j) {
        Log.i(TAG, "OnDisconnect");
        AudioRoomSDK audioRoomSDK = s_ngrtcCallbacks.get(Long.valueOf(j));
        if (audioRoomSDK != null) {
            audioRoomSDK.OnDisconnect();
        }
    }

    @Override // com.netease.ngrtc.NgRTCObserver
    public void OnDisconnect() {
        Log.i(TAG, "OnDisconnect");
        setStatus(4);
        onDisconnectCB(ProtoClient.RTCError.ERR_NONE, "leave room");
    }

    public static void OnMemberJoined(long j, String str) {
        Log.i(TAG, "OnMemberJoined");
        AudioRoomSDK audioRoomSDK = s_ngrtcCallbacks.get(Long.valueOf(j));
        if (audioRoomSDK != null) {
            audioRoomSDK.OnMemberJoined(str);
        }
    }

    @Override // com.netease.ngrtc.NgRTCObserver
    public void OnMemberJoined(String str) {
        String str2 = TAG;
        Log.i(str2, "OnMemberJoined");
        Log.d(str2, "member:" + str);
        ParticipantInfo participantInfo = new ParticipantInfo();
        participantInfo.unmarshal(str);
        onMemberJoinedCB(participantInfo);
    }

    public static void OnMemberMuted(long j, String str) {
        Log.i(TAG, "OnMemberMuted");
        AudioRoomSDK audioRoomSDK = s_ngrtcCallbacks.get(Long.valueOf(j));
        if (audioRoomSDK != null) {
            audioRoomSDK.OnMemberMuted(str);
        }
    }

    @Override // com.netease.ngrtc.NgRTCObserver
    public void OnMemberMuted(String str) {
        String str2 = TAG;
        Log.i(str2, "OnMemberMuted");
        Log.d(str2, "member:" + str);
        ParticipantInfo participantInfo = new ParticipantInfo();
        participantInfo.unmarshal(str);
        onMemberMutedCB(participantInfo);
    }

    public static void OnMemberLeaved(long j, String str) {
        Log.i(TAG, "OnMemberLeaved");
        AudioRoomSDK audioRoomSDK = s_ngrtcCallbacks.get(Long.valueOf(j));
        if (audioRoomSDK != null) {
            audioRoomSDK.OnMemberLeaved(str);
        }
    }

    @Override // com.netease.ngrtc.NgRTCObserver
    public void OnMemberLeaved(String str) {
        String str2 = TAG;
        Log.i(str2, "OnMemberLeaved");
        Log.d(str2, "member:" + str);
        ParticipantInfo participantInfo = new ParticipantInfo();
        participantInfo.unmarshal(str);
        onMemberLeavedCB(participantInfo);
    }

    public static void OnSpeakBegin(long j, String str) {
        Log.i(TAG, "OnSpeakBegin");
        AudioRoomSDK audioRoomSDK = s_ngrtcCallbacks.get(Long.valueOf(j));
        if (audioRoomSDK != null) {
            audioRoomSDK.OnSpeakBegin(str);
        }
    }

    @Override // com.netease.ngrtc.NgRTCObserver
    public void OnSpeakBegin(String str) {
        String str2 = TAG;
        Log.i(str2, "OnSpeakBegin");
        Log.d(str2, "uid:" + str);
        onSpeakBeginCB(str);
    }

    public static void OnSpeakEnd(long j, String str) {
        Log.i(TAG, "OnSpeakEnd");
        AudioRoomSDK audioRoomSDK = s_ngrtcCallbacks.get(Long.valueOf(j));
        if (audioRoomSDK != null) {
            audioRoomSDK.OnSpeakEnd(str);
        }
    }

    @Override // com.netease.ngrtc.NgRTCObserver
    public void OnSpeakEnd(String str) {
        String str2 = TAG;
        Log.i(str2, "OnSpeakEnd");
        Log.d(str2, "uid:" + str);
        onSpeakEndCB(str);
    }

    public static void OnParticipantListRefreshed(long j, String str, int i, int i2, int i3) {
        Log.i(TAG, "OnParticipantListRefreshed");
        AudioRoomSDK audioRoomSDK = s_ngrtcCallbacks.get(Long.valueOf(j));
        if (audioRoomSDK != null) {
            audioRoomSDK.OnParticipantListRefreshed(str, i, i2, i3);
        }
    }

    @Override // com.netease.ngrtc.NgRTCObserver
    public void OnParticipantListRefreshed(String str, int i, int i2, int i3) {
        String str2 = TAG;
        Log.i(str2, "OnParticipantListRefreshed");
        Log.d(str2, "participants:" + str);
        Log.d(str2, "totalmember:" + i);
        Log.d(str2, "offset:" + i2);
        Log.d(str2, "offsetnext:" + i3);
        try {
            JSONObject jSONObject = new JSONObject(str);
            onParticipantListRefreshedCB(ParticipantInfo.unmarshalArr(jSONObject.getJSONArray("members")), jSONObject.optInt("totalMember"), jSONObject.optInt("offset"), jSONObject.optInt("offsetNext"));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
    }

    public static void OnError(long j, int i, String str) {
        Log.i(TAG, "OnError");
        AudioRoomSDK audioRoomSDK = s_ngrtcCallbacks.get(Long.valueOf(j));
        if (audioRoomSDK != null) {
            audioRoomSDK.OnError(i, str);
        }
    }

    @Override // com.netease.ngrtc.NgRTCObserver
    public void OnError(int i, String str) {
        String str2 = TAG;
        Log.i(str2, "OnError");
        Log.d(str2, "errCode:" + i);
        Log.d(str2, "reason:" + str);
        onErrorCB(ProtoClient.getRTCError(i), str);
    }

    class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long j, long j2) {
            super(j, j2);
        }

        @Override // android.os.CountDownTimer
        public void onTick(long j) {
            String str;
            if (AudioRoomSDK.this.mBluetoothHeadset == null || !AudioRoomSDK.this.mBluetoothHeadset.isAudioConnected(AudioRoomSDK.this.mConnectedHeadset)) {
                str = (AudioRoomSDK.this.mBluetoothHeadset == null || !AudioRoomSDK.this.mBluetoothHeadset.startVoiceRecognition(AudioRoomSDK.this.mConnectedHeadset)) ? "\nonTick startVoiceRecognition returns false" : "\nonTick startVoiceRecognition returns true";
            } else {
                str = "\nonTick audio already connected";
            }
            Log.d(AudioRoomSDK.TAG, str);
        }

        @Override // android.os.CountDownTimer
        public void onFinish() {
            String str;
            if (AudioRoomSDK.this.mBluetoothHeadset == null || !AudioRoomSDK.this.mBluetoothHeadset.isAudioConnected(AudioRoomSDK.this.mConnectedHeadset)) {
                str = (AudioRoomSDK.this.mBluetoothHeadset == null || !AudioRoomSDK.this.mBluetoothHeadset.startVoiceRecognition(AudioRoomSDK.this.mConnectedHeadset)) ? "\nonFinish startVoiceRecognition returns false" : "\nonFinish startVoiceRecognition returns true";
            } else {
                str = "\nonFinish audio already connected";
            }
            Log.d(AudioRoomSDK.TAG, str);
        }
    }
}