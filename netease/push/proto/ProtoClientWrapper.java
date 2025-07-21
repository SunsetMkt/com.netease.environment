package com.netease.push.proto;

import android.text.TextUtils;
import com.google.protobuf.nano.MessageNano;
import com.netease.ntunisdk.base.PatchPlaceholder;
import com.netease.push.proto.nano.ProtoClient;
import com.netease.push.utils.PushLog;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/* loaded from: classes2.dex */
public class ProtoClientWrapper {
    public static final byte GET_NEW_ID_TYPE = 1;
    public static final byte GOT_TIME_TYPE = 5;
    public static final short HB_FLAG = 2;
    public static final byte HB_TYPE = 3;
    public static final byte LOGIN_TYPE = 4;
    public static final byte NEW_ID_TYPE = 52;
    private static final int PACKET_HEAD_LEN = 4;
    public static final short PACKET_LEN_HEAD = 2;
    public static final byte PROTO_VER = 3;
    public static final byte PUSH_TYPE = 50;
    public static final short RC4_FLAG = 0;
    public static final byte REGISTER_TYPE = 6;
    public static final byte RESET_TYPE = 51;
    public static final byte SET_NEW_ID_TYPE = 2;
    private static final String TAG = "NGPush_" + ProtoClientWrapper.class.getSimpleName();
    public static final short TLS_FLAG = 1;
    public static final byte UNREGISTER_TYPE = 7;

    public interface DataMarshal {
        byte[] Marshal();
    }

    public static String getTypeName(byte b) {
        switch (b) {
            case 50:
                return "push";
            case 51:
                return "reset";
            case 52:
                return "newid";
            default:
                return "unknown";
        }
    }

    private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, PatchPlaceholder.class.getSimpleName());
    }

    public static void Uint16ToBytes(byte[] bArr, int i, int i2) {
        int i3 = i2 & 65535;
        bArr[i] = (byte) ((i3 >> 8) & 255);
        bArr[i + 1] = (byte) (i3 & 255);
    }

    public static int BytesToUint16(byte[] bArr, int i) {
        return ((bArr[i] & 255) << 8) + (bArr[i + 1] & 255);
    }

    public static final byte[] MarshalObject(byte b, DataMarshal dataMarshal) {
        byte[] bArrMarshal = dataMarshal.Marshal();
        if (bArrMarshal == null) {
            bArrMarshal = new byte[0];
        }
        int length = bArrMarshal.length + 4;
        byte[] bArr = new byte[length];
        Uint16ToBytes(bArr, 0, length);
        bArr[2] = 3;
        bArr[3] = b;
        System.arraycopy(bArrMarshal, 0, bArr, 4, bArrMarshal.length);
        return bArr;
    }

    public static Packet UnmarshalPacket(byte[] bArr) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (bArr.length < 4) {
            PushLog.e(TAG, "data error:" + bArr);
            return null;
        }
        Packet packet = new Packet();
        packet.length = BytesToUint16(bArr, 0);
        if (packet.length < 4 || packet.length > bArr.length) {
            PushLog.e(TAG, "packet length error:" + packet.length + " not in [4, " + bArr.length + "]");
            return null;
        }
        packet.version = bArr[2];
        packet.type = bArr[3];
        packet.data = new byte[packet.length - 4];
        if (packet.length > 4) {
            System.arraycopy(bArr, 4, packet.data, 0, packet.data.length);
        }
        return packet;
    }

    public static class Packet {
        public byte[] data;
        public int length;
        public byte type;
        public byte version;

        private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            PushLog.i(ProtoClientWrapper.TAG, PatchPlaceholder.class.getSimpleName());
        }

        public Packet() {
            this.length = 0;
        }

        public Packet(byte b) {
            this.length = 4;
            this.type = b;
            this.version = (byte) 3;
            this.data = null;
        }

        public Packet(byte b, byte[] bArr) {
            this.length = bArr.length + 4;
            this.type = b;
            this.version = (byte) 3;
            this.data = new byte[bArr.length];
            System.arraycopy(bArr, 0, this.data, 0, bArr.length);
        }

        public byte[] Marshal() {
            byte[] bArr = this.data;
            int length = bArr != null ? bArr.length + 4 : 4;
            byte[] bArr2 = new byte[length];
            ProtoClientWrapper.Uint16ToBytes(bArr2, 0, length);
            bArr2[2] = 3;
            bArr2[3] = this.type;
            byte[] bArr3 = this.data;
            if (bArr3 != null) {
                System.arraycopy(bArr3, 0, bArr2, 4, bArr3.length);
            }
            return bArr2;
        }

        public int UnmarshalPacket(byte[] bArr) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            if (bArr.length < 4) {
                PushLog.e("AndroidPush", "data error:" + bArr);
                return 0;
            }
            this.length = ProtoClientWrapper.BytesToUint16(bArr, 0);
            this.version = bArr[2];
            this.type = bArr[3];
            this.data = new byte[bArr.length - 4];
            System.arraycopy(bArr, 4, this.data, 0, bArr.length - 4);
            return 1;
        }
    }

    public static class DevInfo implements DataMarshal {
        public String model = "";
        public String screen = "";
        public String os = "";
        public String osver = "";
        public String mac = "";
        public String id = "";

        private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            PushLog.i(ProtoClientWrapper.TAG, PatchPlaceholder.class.getSimpleName());
        }

        @Override // com.netease.push.proto.ProtoClientWrapper.DataMarshal
        public byte[] Marshal() {
            ProtoClient.PbDevInfo pbDevInfo = new ProtoClient.PbDevInfo();
            pbDevInfo.model = this.model;
            pbDevInfo.screen = this.screen;
            pbDevInfo.os = this.os;
            pbDevInfo.osver = this.osver;
            pbDevInfo.mac = this.mac;
            pbDevInfo.id = this.id;
            return MessageNano.toByteArray(pbDevInfo);
        }

        public static DevInfo UnmarshalDevInfo(byte[] bArr) throws Exception {
            DevInfo devInfo = new DevInfo();
            try {
                ProtoClient.PbDevInfo from = ProtoClient.PbDevInfo.parseFrom(bArr);
                devInfo.model = from.model;
                devInfo.screen = from.screen;
                devInfo.os = from.os;
                devInfo.osver = from.osver;
                devInfo.mac = from.mac;
                devInfo.id = from.id;
                return devInfo;
            } catch (Exception e) {
                PushLog.e("AndroidPush", "parse data error:" + Arrays.toString(bArr));
                throw e;
            }
        }
    }

    public static class ServiceInfo {
        public String service;
        public long time;

        private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            PushLog.i(ProtoClientWrapper.TAG, PatchPlaceholder.class.getSimpleName());
        }
    }

    public static class DevServiceInfos implements DataMarshal {
        public String id;
        public String key;
        public ServiceInfo[] serviceInfos;
        public String ver;

        private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            PushLog.i(ProtoClientWrapper.TAG, PatchPlaceholder.class.getSimpleName());
        }

        @Override // com.netease.push.proto.ProtoClientWrapper.DataMarshal
        public byte[] Marshal() {
            ProtoClient.PbLoginInfo pbLoginInfo = new ProtoClient.PbLoginInfo();
            if (!TextUtils.isEmpty(this.id)) {
                pbLoginInfo.id = this.id;
            }
            if (!TextUtils.isEmpty(this.ver)) {
                pbLoginInfo.ver = this.ver;
            }
            if (!TextUtils.isEmpty(this.key)) {
                pbLoginInfo.key = this.key;
            }
            int length = this.serviceInfos.length;
            pbLoginInfo.serviceinfos = new ProtoClient.PbServiceInfo[length];
            for (int i = 0; i < length; i++) {
                pbLoginInfo.serviceinfos[i] = new ProtoClient.PbServiceInfo();
                pbLoginInfo.serviceinfos[i].service = new String(this.serviceInfos[i].service);
                pbLoginInfo.serviceinfos[i].time = this.serviceInfos[i].time;
            }
            return MessageNano.toByteArray(pbLoginInfo);
        }

        public static DevServiceInfos unmarshalDevServiceInfos(byte[] bArr) throws Exception {
            DevServiceInfos devServiceInfos = new DevServiceInfos();
            try {
                ProtoClient.PbLoginInfo from = ProtoClient.PbLoginInfo.parseFrom(bArr);
                devServiceInfos.id = from.id;
                devServiceInfos.ver = from.ver;
                devServiceInfos.key = from.key;
                int length = from.serviceinfos.length;
                devServiceInfos.serviceInfos = new ServiceInfo[length];
                for (int i = 0; i < length; i++) {
                    devServiceInfos.serviceInfos[i].service = from.serviceinfos[i].service;
                    devServiceInfos.serviceInfos[i].time = from.serviceinfos[i].time;
                }
                return devServiceInfos;
            } catch (Exception e) {
                PushLog.e("AndroidPush", "parse data devserviceinfos error:" + Arrays.toString(bArr));
                throw e;
            }
        }
    }

    public static class DevServiceInfo implements DataMarshal {
        public String id;
        public String service;
        public long time;

        private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            PushLog.i(ProtoClientWrapper.TAG, PatchPlaceholder.class.getSimpleName());
        }

        @Override // com.netease.push.proto.ProtoClientWrapper.DataMarshal
        public byte[] Marshal() {
            ProtoClient.PbDevServiceInfo pbDevServiceInfo = new ProtoClient.PbDevServiceInfo();
            pbDevServiceInfo.id = this.id;
            pbDevServiceInfo.service = this.service;
            pbDevServiceInfo.time = this.time;
            return MessageNano.toByteArray(pbDevServiceInfo);
        }

        public static DevServiceInfo unmarshalDevServiceInfo(byte[] bArr) throws Exception {
            DevServiceInfo devServiceInfo = new DevServiceInfo();
            try {
                ProtoClient.PbDevServiceInfo from = ProtoClient.PbDevServiceInfo.parseFrom(bArr);
                devServiceInfo.id = from.id;
                devServiceInfo.service = from.service;
                devServiceInfo.time = from.time;
                return devServiceInfo;
            } catch (Exception e) {
                PushLog.e("AndroidPush", "parse data devserviceinfo error:" + Arrays.toString(bArr));
                throw e;
            }
        }
    }

    public static class NewIdInfo implements DataMarshal {
        public String id;

        private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            PushLog.i(ProtoClientWrapper.TAG, PatchPlaceholder.class.getSimpleName());
        }

        @Override // com.netease.push.proto.ProtoClientWrapper.DataMarshal
        public byte[] Marshal() {
            ProtoClient.PbNewIdInfo pbNewIdInfo = new ProtoClient.PbNewIdInfo();
            pbNewIdInfo.id = this.id;
            return MessageNano.toByteArray(pbNewIdInfo);
        }

        public static NewIdInfo UnmarshalNewIdInfo(byte[] bArr) throws Exception {
            NewIdInfo newIdInfo = new NewIdInfo();
            try {
                newIdInfo.id = ProtoClient.PbNewIdInfo.parseFrom(bArr).id;
                return newIdInfo;
            } catch (Exception e) {
                PushLog.e("AndroidPush", "parse data error:" + Arrays.toString(bArr));
                throw e;
            }
        }
    }

    public static class Message implements DataMarshal {
        public String channelId;
        public String channelName;
        public String content;
        public String ext;
        public String ext2;
        public String groupId;
        public String groupName;
        public int mode;
        public int notifyid;
        public String packagename;
        public String reqid;
        public String service;
        public String sound;
        public long time;
        public String title;

        private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            PushLog.i(ProtoClientWrapper.TAG, PatchPlaceholder.class.getSimpleName());
        }

        @Override // com.netease.push.proto.ProtoClientWrapper.DataMarshal
        public byte[] Marshal() {
            ProtoClient.PbMessage pbMessage = new ProtoClient.PbMessage();
            pbMessage.content = this.content;
            pbMessage.time = this.time;
            pbMessage.service = this.service;
            pbMessage.packagename = this.packagename;
            pbMessage.mode = this.mode;
            pbMessage.ext = this.ext;
            pbMessage.ext2 = this.ext2;
            pbMessage.channelId = this.channelId;
            pbMessage.channelName = this.channelName;
            pbMessage.groupId = this.groupId;
            pbMessage.groupName = this.groupName;
            pbMessage.title = this.title;
            pbMessage.notifyid = this.notifyid;
            pbMessage.reqid = this.reqid;
            pbMessage.sound = this.sound;
            return MessageNano.toByteArray(pbMessage);
        }

        public static Message UnmarshalMessage(byte[] bArr) throws Exception {
            Message message = new Message();
            try {
                ProtoClient.PbMessage from = ProtoClient.PbMessage.parseFrom(bArr);
                message.content = from.content;
                message.time = from.time;
                message.service = from.service;
                message.packagename = from.packagename;
                message.mode = from.mode;
                message.ext = from.ext;
                message.ext2 = from.ext2;
                message.channelId = from.channelId;
                message.channelName = from.channelName;
                message.groupId = from.groupId;
                message.groupName = from.groupName;
                message.title = from.title;
                message.notifyid = from.notifyid;
                message.reqid = from.reqid;
                message.sound = from.sound;
                return message;
            } catch (Exception e) {
                PushLog.e("AndroidPush", "parse data error:" + Arrays.toString(bArr));
                throw e;
            }
        }
    }

    public static class MessageInfo implements DataMarshal {
        public String id;
        public Message[] messages;

        @Override // com.netease.push.proto.ProtoClientWrapper.DataMarshal
        public byte[] Marshal() {
            return new byte[0];
        }

        private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            PushLog.i(ProtoClientWrapper.TAG, PatchPlaceholder.class.getSimpleName());
        }

        public static MessageInfo unmarshalMessageInfo(byte[] bArr) throws Exception {
            MessageInfo messageInfo = new MessageInfo();
            try {
                ProtoClient.PbMessageInfo from = ProtoClient.PbMessageInfo.parseFrom(bArr);
                messageInfo.id = from.id;
                int length = from.messages.length;
                messageInfo.messages = new Message[length];
                for (int i = 0; i < length; i++) {
                    messageInfo.messages[i] = Message.UnmarshalMessage(from.messages[i]);
                }
                return messageInfo;
            } catch (Exception e) {
                PushLog.e("AndroidPush", "parse data devserviceinfos error:" + Arrays.toString(bArr));
                throw e;
            }
        }
    }
}