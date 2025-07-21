package com.netease.push.proto.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;
import org.xbill.DNS.WKSRecord;

/* loaded from: classes2.dex */
public interface ProtoClient {

    public static final class PbDevInfo extends MessageNano {
        private static volatile PbDevInfo[] _emptyArray;
        public String id;
        public String mac;
        public String model;
        public String os;
        public String osver;
        public String screen;

        public static PbDevInfo[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new PbDevInfo[0];
                    }
                }
            }
            return _emptyArray;
        }

        public PbDevInfo() {
            clear();
        }

        public PbDevInfo clear() {
            this.model = "";
            this.screen = "";
            this.os = "";
            this.osver = "";
            this.mac = "";
            this.id = "";
            this.cachedSize = -1;
            return this;
        }

        @Override // com.google.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            if (!this.model.equals("")) {
                codedOutputByteBufferNano.writeString(1, this.model);
            }
            if (!this.screen.equals("")) {
                codedOutputByteBufferNano.writeString(2, this.screen);
            }
            if (!this.os.equals("")) {
                codedOutputByteBufferNano.writeString(3, this.os);
            }
            if (!this.osver.equals("")) {
                codedOutputByteBufferNano.writeString(4, this.osver);
            }
            if (!this.mac.equals("")) {
                codedOutputByteBufferNano.writeString(5, this.mac);
            }
            if (!this.id.equals("")) {
                codedOutputByteBufferNano.writeString(6, this.id);
            }
            super.writeTo(codedOutputByteBufferNano);
        }

        @Override // com.google.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            if (!this.model.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(1, this.model);
            }
            if (!this.screen.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(2, this.screen);
            }
            if (!this.os.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(3, this.os);
            }
            if (!this.osver.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(4, this.osver);
            }
            if (!this.mac.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(5, this.mac);
            }
            return !this.id.equals("") ? iComputeSerializedSize + CodedOutputByteBufferNano.computeStringSize(6, this.id) : iComputeSerializedSize;
        }

        @Override // com.google.protobuf.nano.MessageNano
        public PbDevInfo mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                int tag = codedInputByteBufferNano.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 10) {
                    this.model = codedInputByteBufferNano.readString();
                } else if (tag == 18) {
                    this.screen = codedInputByteBufferNano.readString();
                } else if (tag == 26) {
                    this.os = codedInputByteBufferNano.readString();
                } else if (tag == 34) {
                    this.osver = codedInputByteBufferNano.readString();
                } else if (tag == 42) {
                    this.mac = codedInputByteBufferNano.readString();
                } else if (tag != 50) {
                    if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, tag)) {
                        return this;
                    }
                } else {
                    this.id = codedInputByteBufferNano.readString();
                }
            }
        }

        public static PbDevInfo parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
            return (PbDevInfo) MessageNano.mergeFrom(new PbDevInfo(), bArr);
        }

        public static PbDevInfo parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new PbDevInfo().mergeFrom(codedInputByteBufferNano);
        }
    }

    public static final class PbDevServiceInfo extends MessageNano {
        private static volatile PbDevServiceInfo[] _emptyArray;
        public String id;
        public String service;
        public long time;

        public static PbDevServiceInfo[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new PbDevServiceInfo[0];
                    }
                }
            }
            return _emptyArray;
        }

        public PbDevServiceInfo() {
            clear();
        }

        public PbDevServiceInfo clear() {
            this.id = "";
            this.service = "";
            this.time = 0L;
            this.cachedSize = -1;
            return this;
        }

        @Override // com.google.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            if (!this.id.equals("")) {
                codedOutputByteBufferNano.writeString(1, this.id);
            }
            if (!this.service.equals("")) {
                codedOutputByteBufferNano.writeString(2, this.service);
            }
            long j = this.time;
            if (j != 0) {
                codedOutputByteBufferNano.writeInt64(3, j);
            }
            super.writeTo(codedOutputByteBufferNano);
        }

        @Override // com.google.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            if (!this.id.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(1, this.id);
            }
            if (!this.service.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(2, this.service);
            }
            long j = this.time;
            return j != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt64Size(3, j) : iComputeSerializedSize;
        }

        @Override // com.google.protobuf.nano.MessageNano
        public PbDevServiceInfo mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                int tag = codedInputByteBufferNano.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 10) {
                    this.id = codedInputByteBufferNano.readString();
                } else if (tag == 18) {
                    this.service = codedInputByteBufferNano.readString();
                } else if (tag != 24) {
                    if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, tag)) {
                        return this;
                    }
                } else {
                    this.time = codedInputByteBufferNano.readInt64();
                }
            }
        }

        public static PbDevServiceInfo parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
            return (PbDevServiceInfo) MessageNano.mergeFrom(new PbDevServiceInfo(), bArr);
        }

        public static PbDevServiceInfo parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new PbDevServiceInfo().mergeFrom(codedInputByteBufferNano);
        }
    }

    public static final class PbServiceInfo extends MessageNano {
        private static volatile PbServiceInfo[] _emptyArray;
        public String service;
        public long time;

        public static PbServiceInfo[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new PbServiceInfo[0];
                    }
                }
            }
            return _emptyArray;
        }

        public PbServiceInfo() {
            clear();
        }

        public PbServiceInfo clear() {
            this.service = "";
            this.time = 0L;
            this.cachedSize = -1;
            return this;
        }

        @Override // com.google.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            if (!this.service.equals("")) {
                codedOutputByteBufferNano.writeString(1, this.service);
            }
            long j = this.time;
            if (j != 0) {
                codedOutputByteBufferNano.writeInt64(2, j);
            }
            super.writeTo(codedOutputByteBufferNano);
        }

        @Override // com.google.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            if (!this.service.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(1, this.service);
            }
            long j = this.time;
            return j != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt64Size(2, j) : iComputeSerializedSize;
        }

        @Override // com.google.protobuf.nano.MessageNano
        public PbServiceInfo mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                int tag = codedInputByteBufferNano.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 10) {
                    this.service = codedInputByteBufferNano.readString();
                } else if (tag != 16) {
                    if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, tag)) {
                        return this;
                    }
                } else {
                    this.time = codedInputByteBufferNano.readInt64();
                }
            }
        }

        public static PbServiceInfo parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
            return (PbServiceInfo) MessageNano.mergeFrom(new PbServiceInfo(), bArr);
        }

        public static PbServiceInfo parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new PbServiceInfo().mergeFrom(codedInputByteBufferNano);
        }
    }

    public static final class PbLoginInfo extends MessageNano {
        private static volatile PbLoginInfo[] _emptyArray;
        public String id;
        public String key;
        public PbServiceInfo[] serviceinfos;
        public String ver;

        public static PbLoginInfo[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new PbLoginInfo[0];
                    }
                }
            }
            return _emptyArray;
        }

        public PbLoginInfo() {
            clear();
        }

        public PbLoginInfo clear() {
            this.id = "";
            this.serviceinfos = PbServiceInfo.emptyArray();
            this.ver = "";
            this.key = "";
            this.cachedSize = -1;
            return this;
        }

        @Override // com.google.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            if (!this.id.equals("")) {
                codedOutputByteBufferNano.writeString(1, this.id);
            }
            PbServiceInfo[] pbServiceInfoArr = this.serviceinfos;
            if (pbServiceInfoArr != null && pbServiceInfoArr.length > 0) {
                int i = 0;
                while (true) {
                    PbServiceInfo[] pbServiceInfoArr2 = this.serviceinfos;
                    if (i >= pbServiceInfoArr2.length) {
                        break;
                    }
                    PbServiceInfo pbServiceInfo = pbServiceInfoArr2[i];
                    if (pbServiceInfo != null) {
                        codedOutputByteBufferNano.writeMessage(2, pbServiceInfo);
                    }
                    i++;
                }
            }
            if (!this.ver.equals("")) {
                codedOutputByteBufferNano.writeString(3, this.ver);
            }
            if (!this.key.equals("")) {
                codedOutputByteBufferNano.writeString(4, this.key);
            }
            super.writeTo(codedOutputByteBufferNano);
        }

        @Override // com.google.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            if (!this.id.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(1, this.id);
            }
            PbServiceInfo[] pbServiceInfoArr = this.serviceinfos;
            if (pbServiceInfoArr != null && pbServiceInfoArr.length > 0) {
                int i = 0;
                while (true) {
                    PbServiceInfo[] pbServiceInfoArr2 = this.serviceinfos;
                    if (i >= pbServiceInfoArr2.length) {
                        break;
                    }
                    PbServiceInfo pbServiceInfo = pbServiceInfoArr2[i];
                    if (pbServiceInfo != null) {
                        iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(2, pbServiceInfo);
                    }
                    i++;
                }
            }
            if (!this.ver.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(3, this.ver);
            }
            return !this.key.equals("") ? iComputeSerializedSize + CodedOutputByteBufferNano.computeStringSize(4, this.key) : iComputeSerializedSize;
        }

        @Override // com.google.protobuf.nano.MessageNano
        public PbLoginInfo mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                int tag = codedInputByteBufferNano.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 10) {
                    this.id = codedInputByteBufferNano.readString();
                } else if (tag == 18) {
                    int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 18);
                    PbServiceInfo[] pbServiceInfoArr = this.serviceinfos;
                    int length = pbServiceInfoArr == null ? 0 : pbServiceInfoArr.length;
                    PbServiceInfo[] pbServiceInfoArr2 = new PbServiceInfo[repeatedFieldArrayLength + length];
                    if (length != 0) {
                        System.arraycopy(this.serviceinfos, 0, pbServiceInfoArr2, 0, length);
                    }
                    while (length < pbServiceInfoArr2.length - 1) {
                        pbServiceInfoArr2[length] = new PbServiceInfo();
                        codedInputByteBufferNano.readMessage(pbServiceInfoArr2[length]);
                        codedInputByteBufferNano.readTag();
                        length++;
                    }
                    pbServiceInfoArr2[length] = new PbServiceInfo();
                    codedInputByteBufferNano.readMessage(pbServiceInfoArr2[length]);
                    this.serviceinfos = pbServiceInfoArr2;
                } else if (tag == 26) {
                    this.ver = codedInputByteBufferNano.readString();
                } else if (tag != 34) {
                    if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, tag)) {
                        return this;
                    }
                } else {
                    this.key = codedInputByteBufferNano.readString();
                }
            }
        }

        public static PbLoginInfo parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
            return (PbLoginInfo) MessageNano.mergeFrom(new PbLoginInfo(), bArr);
        }

        public static PbLoginInfo parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new PbLoginInfo().mergeFrom(codedInputByteBufferNano);
        }
    }

    public static final class PbNewIdInfo extends MessageNano {
        private static volatile PbNewIdInfo[] _emptyArray;
        public String id;

        public static PbNewIdInfo[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new PbNewIdInfo[0];
                    }
                }
            }
            return _emptyArray;
        }

        public PbNewIdInfo() {
            clear();
        }

        public PbNewIdInfo clear() {
            this.id = "";
            this.cachedSize = -1;
            return this;
        }

        @Override // com.google.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            if (!this.id.equals("")) {
                codedOutputByteBufferNano.writeString(1, this.id);
            }
            super.writeTo(codedOutputByteBufferNano);
        }

        @Override // com.google.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            return !this.id.equals("") ? iComputeSerializedSize + CodedOutputByteBufferNano.computeStringSize(1, this.id) : iComputeSerializedSize;
        }

        @Override // com.google.protobuf.nano.MessageNano
        public PbNewIdInfo mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                int tag = codedInputByteBufferNano.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag != 10) {
                    if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, tag)) {
                        return this;
                    }
                } else {
                    this.id = codedInputByteBufferNano.readString();
                }
            }
        }

        public static PbNewIdInfo parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
            return (PbNewIdInfo) MessageNano.mergeFrom(new PbNewIdInfo(), bArr);
        }

        public static PbNewIdInfo parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new PbNewIdInfo().mergeFrom(codedInputByteBufferNano);
        }
    }

    public static final class PbMessage extends MessageNano {
        private static volatile PbMessage[] _emptyArray;
        public String channelId;
        public String channelName;
        public String content;
        public String ext;
        public String ext2;
        public String groupId;
        public String groupName;
        public int mode;
        public String msggroup;
        public int notifyid;
        public String packagename;
        public String reqid;
        public String service;
        public String sound;
        public long tTL;
        public long time;
        public String title;

        public static PbMessage[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new PbMessage[0];
                    }
                }
            }
            return _emptyArray;
        }

        public PbMessage() {
            clear();
        }

        public PbMessage clear() {
            this.content = "";
            this.time = 0L;
            this.service = "";
            this.packagename = "";
            this.mode = 0;
            this.ext = "";
            this.title = "";
            this.tTL = 0L;
            this.msggroup = "";
            this.notifyid = 0;
            this.reqid = "";
            this.sound = "";
            this.channelId = "";
            this.ext2 = "";
            this.channelName = "";
            this.groupId = "";
            this.groupName = "";
            this.cachedSize = -1;
            return this;
        }

        @Override // com.google.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            if (!this.content.equals("")) {
                codedOutputByteBufferNano.writeString(1, this.content);
            }
            long j = this.time;
            if (j != 0) {
                codedOutputByteBufferNano.writeInt64(2, j);
            }
            if (!this.service.equals("")) {
                codedOutputByteBufferNano.writeString(3, this.service);
            }
            if (!this.packagename.equals("")) {
                codedOutputByteBufferNano.writeString(4, this.packagename);
            }
            int i = this.mode;
            if (i != 0) {
                codedOutputByteBufferNano.writeInt32(5, i);
            }
            if (!this.ext.equals("")) {
                codedOutputByteBufferNano.writeString(6, this.ext);
            }
            if (!this.title.equals("")) {
                codedOutputByteBufferNano.writeString(7, this.title);
            }
            long j2 = this.tTL;
            if (j2 != 0) {
                codedOutputByteBufferNano.writeInt64(8, j2);
            }
            if (!this.msggroup.equals("")) {
                codedOutputByteBufferNano.writeString(9, this.msggroup);
            }
            int i2 = this.notifyid;
            if (i2 != 0) {
                codedOutputByteBufferNano.writeInt32(10, i2);
            }
            if (!this.reqid.equals("")) {
                codedOutputByteBufferNano.writeString(11, this.reqid);
            }
            if (!this.sound.equals("")) {
                codedOutputByteBufferNano.writeString(12, this.sound);
            }
            if (!this.channelId.equals("")) {
                codedOutputByteBufferNano.writeString(13, this.channelId);
            }
            if (!this.ext2.equals("")) {
                codedOutputByteBufferNano.writeString(14, this.ext2);
            }
            if (!this.channelName.equals("")) {
                codedOutputByteBufferNano.writeString(15, this.channelName);
            }
            if (!this.groupId.equals("")) {
                codedOutputByteBufferNano.writeString(16, this.groupId);
            }
            if (!this.groupName.equals("")) {
                codedOutputByteBufferNano.writeString(17, this.groupName);
            }
            super.writeTo(codedOutputByteBufferNano);
        }

        @Override // com.google.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            if (!this.content.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(1, this.content);
            }
            long j = this.time;
            if (j != 0) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(2, j);
            }
            if (!this.service.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(3, this.service);
            }
            if (!this.packagename.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(4, this.packagename);
            }
            int i = this.mode;
            if (i != 0) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(5, i);
            }
            if (!this.ext.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(6, this.ext);
            }
            if (!this.title.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(7, this.title);
            }
            long j2 = this.tTL;
            if (j2 != 0) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(8, j2);
            }
            if (!this.msggroup.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(9, this.msggroup);
            }
            int i2 = this.notifyid;
            if (i2 != 0) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(10, i2);
            }
            if (!this.reqid.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(11, this.reqid);
            }
            if (!this.sound.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(12, this.sound);
            }
            if (!this.channelId.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(13, this.channelId);
            }
            if (!this.ext2.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(14, this.ext2);
            }
            if (!this.channelName.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(15, this.channelName);
            }
            if (!this.groupId.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(16, this.groupId);
            }
            return !this.groupName.equals("") ? iComputeSerializedSize + CodedOutputByteBufferNano.computeStringSize(17, this.groupName) : iComputeSerializedSize;
        }

        @Override // com.google.protobuf.nano.MessageNano
        public PbMessage mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                int tag = codedInputByteBufferNano.readTag();
                switch (tag) {
                    case 0:
                        return this;
                    case 10:
                        this.content = codedInputByteBufferNano.readString();
                        break;
                    case 16:
                        this.time = codedInputByteBufferNano.readInt64();
                        break;
                    case 26:
                        this.service = codedInputByteBufferNano.readString();
                        break;
                    case 34:
                        this.packagename = codedInputByteBufferNano.readString();
                        break;
                    case 40:
                        this.mode = codedInputByteBufferNano.readInt32();
                        break;
                    case 50:
                        this.ext = codedInputByteBufferNano.readString();
                        break;
                    case 58:
                        this.title = codedInputByteBufferNano.readString();
                        break;
                    case 64:
                        this.tTL = codedInputByteBufferNano.readInt64();
                        break;
                    case 74:
                        this.msggroup = codedInputByteBufferNano.readString();
                        break;
                    case 80:
                        this.notifyid = codedInputByteBufferNano.readInt32();
                        break;
                    case 90:
                        this.reqid = codedInputByteBufferNano.readString();
                        break;
                    case 98:
                        this.sound = codedInputByteBufferNano.readString();
                        break;
                    case 106:
                        this.channelId = codedInputByteBufferNano.readString();
                        break;
                    case 114:
                        this.ext2 = codedInputByteBufferNano.readString();
                        break;
                    case 122:
                        this.channelName = codedInputByteBufferNano.readString();
                        break;
                    case 130:
                        this.groupId = codedInputByteBufferNano.readString();
                        break;
                    case WKSRecord.Service.NETBIOS_DGM /* 138 */:
                        this.groupName = codedInputByteBufferNano.readString();
                        break;
                    default:
                        if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, tag)) {
                            return this;
                        }
                        break;
                }
            }
        }

        public static PbMessage parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
            return (PbMessage) MessageNano.mergeFrom(new PbMessage(), bArr);
        }

        public static PbMessage parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new PbMessage().mergeFrom(codedInputByteBufferNano);
        }
    }

    public static final class PbMessageInfo extends MessageNano {
        private static volatile PbMessageInfo[] _emptyArray;
        public String id;
        public byte[][] messages;

        public static PbMessageInfo[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new PbMessageInfo[0];
                    }
                }
            }
            return _emptyArray;
        }

        public PbMessageInfo() {
            clear();
        }

        public PbMessageInfo clear() {
            this.id = "";
            this.messages = WireFormatNano.EMPTY_BYTES_ARRAY;
            this.cachedSize = -1;
            return this;
        }

        @Override // com.google.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            if (!this.id.equals("")) {
                codedOutputByteBufferNano.writeString(1, this.id);
            }
            byte[][] bArr = this.messages;
            if (bArr != null && bArr.length > 0) {
                int i = 0;
                while (true) {
                    byte[][] bArr2 = this.messages;
                    if (i >= bArr2.length) {
                        break;
                    }
                    byte[] bArr3 = bArr2[i];
                    if (bArr3 != null) {
                        codedOutputByteBufferNano.writeBytes(2, bArr3);
                    }
                    i++;
                }
            }
            super.writeTo(codedOutputByteBufferNano);
        }

        @Override // com.google.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            if (!this.id.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(1, this.id);
            }
            byte[][] bArr = this.messages;
            if (bArr == null || bArr.length <= 0) {
                return iComputeSerializedSize;
            }
            int i = 0;
            int iComputeBytesSizeNoTag = 0;
            int i2 = 0;
            while (true) {
                byte[][] bArr2 = this.messages;
                if (i >= bArr2.length) {
                    return iComputeSerializedSize + iComputeBytesSizeNoTag + (i2 * 1);
                }
                byte[] bArr3 = bArr2[i];
                if (bArr3 != null) {
                    i2++;
                    iComputeBytesSizeNoTag += CodedOutputByteBufferNano.computeBytesSizeNoTag(bArr3);
                }
                i++;
            }
        }

        @Override // com.google.protobuf.nano.MessageNano
        public PbMessageInfo mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                int tag = codedInputByteBufferNano.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 10) {
                    this.id = codedInputByteBufferNano.readString();
                } else if (tag != 18) {
                    if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, tag)) {
                        return this;
                    }
                } else {
                    int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 18);
                    byte[][] bArr = this.messages;
                    int length = bArr == null ? 0 : bArr.length;
                    byte[][] bArr2 = new byte[repeatedFieldArrayLength + length][];
                    if (length != 0) {
                        System.arraycopy(this.messages, 0, bArr2, 0, length);
                    }
                    while (length < bArr2.length - 1) {
                        bArr2[length] = codedInputByteBufferNano.readBytes();
                        codedInputByteBufferNano.readTag();
                        length++;
                    }
                    bArr2[length] = codedInputByteBufferNano.readBytes();
                    this.messages = bArr2;
                }
            }
        }

        public static PbMessageInfo parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
            return (PbMessageInfo) MessageNano.mergeFrom(new PbMessageInfo(), bArr);
        }

        public static PbMessageInfo parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new PbMessageInfo().mergeFrom(codedInputByteBufferNano);
        }
    }
}