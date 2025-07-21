package com.netease.push.proto.nano;

import com.google.protobuf.AbstractMessageLite;
import com.google.protobuf.AbstractParser;
import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Descriptors;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Internal;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.LazyStringArrayList;
import com.google.protobuf.LazyStringList;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.Parser;
import com.google.protobuf.ProtocolStringList;
import com.google.protobuf.RepeatedFieldBuilderV3;
import com.google.protobuf.SingleFieldBuilderV3;
import com.google.protobuf.UnknownFieldSet;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes2.dex */
public final class ClientSdkgate {
    private static Descriptors.FileDescriptor descriptor;
    private static final Descriptors.Descriptor internal_static_proto_sdkgate_APNS_descriptor;
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_proto_sdkgate_APNS_fieldAccessorTable;
    private static final Descriptors.Descriptor internal_static_proto_sdkgate_AckReceiveNotification_descriptor;
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_proto_sdkgate_AckReceiveNotification_fieldAccessorTable;
    private static final Descriptors.Descriptor internal_static_proto_sdkgate_Android_descriptor;
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_proto_sdkgate_Android_fieldAccessorTable;
    private static final Descriptors.Descriptor internal_static_proto_sdkgate_Channel_descriptor;
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_proto_sdkgate_Channel_fieldAccessorTable;
    private static final Descriptors.Descriptor internal_static_proto_sdkgate_EmptyMsg_descriptor;
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_proto_sdkgate_EmptyMsg_fieldAccessorTable;
    private static final Descriptors.Descriptor internal_static_proto_sdkgate_ErrMsg_descriptor;
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_proto_sdkgate_ErrMsg_fieldAccessorTable;
    private static final Descriptors.Descriptor internal_static_proto_sdkgate_FindOfflineNotificationRequest_descriptor;
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_proto_sdkgate_FindOfflineNotificationRequest_fieldAccessorTable;
    private static final Descriptors.Descriptor internal_static_proto_sdkgate_FindOfflineNotificationResponse_descriptor;
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_proto_sdkgate_FindOfflineNotificationResponse_fieldAccessorTable;
    private static final Descriptors.Descriptor internal_static_proto_sdkgate_HeartbeatRequest_descriptor;
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_proto_sdkgate_HeartbeatRequest_fieldAccessorTable;
    private static final Descriptors.Descriptor internal_static_proto_sdkgate_LoginRequest_descriptor;
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_proto_sdkgate_LoginRequest_fieldAccessorTable;
    private static final Descriptors.Descriptor internal_static_proto_sdkgate_Notification_descriptor;
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_proto_sdkgate_Notification_fieldAccessorTable;
    private static final Descriptors.Descriptor internal_static_proto_sdkgate_PreRegisterRequest_descriptor;
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_proto_sdkgate_PreRegisterRequest_fieldAccessorTable;
    private static final Descriptors.Descriptor internal_static_proto_sdkgate_PreRegisterResponse_descriptor;
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_proto_sdkgate_PreRegisterResponse_fieldAccessorTable;
    private static final Descriptors.Descriptor internal_static_proto_sdkgate_RegisterRequest_descriptor;
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_proto_sdkgate_RegisterRequest_fieldAccessorTable;
    private static final Descriptors.Descriptor internal_static_proto_sdkgate_RegisterResponse_descriptor;
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_proto_sdkgate_RegisterResponse_fieldAccessorTable;
    private static final Descriptors.Descriptor internal_static_proto_sdkgate_SystemContent_descriptor;
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_proto_sdkgate_SystemContent_fieldAccessorTable;

    public interface APNSOrBuilder extends MessageOrBuilder {
        long getBadge();

        String getCategory();

        ByteString getCategoryBytes();

        String getCollapseId();

        ByteString getCollapseIdBytes();

        String getCustomContent();

        ByteString getCustomContentBytes();

        String getMediaUrl();

        ByteString getMediaUrlBytes();

        long getMutableContent();

        String getSound();

        ByteString getSoundBytes();

        String getThreadId();

        ByteString getThreadIdBytes();
    }

    public interface AckReceiveNotificationOrBuilder extends MessageOrBuilder {
        String getPushIds(int i);

        ByteString getPushIdsBytes(int i);

        int getPushIdsCount();

        List<String> getPushIdsList();
    }

    public interface AndroidOrBuilder extends MessageOrBuilder {
        String getAudioUrl();

        ByteString getAudioUrlBytes();

        long getBadge();

        String getBigImageUrl();

        ByteString getBigImageUrlBytes();

        Channel getChannel();

        ChannelOrBuilder getChannelOrBuilder();

        String getClickActionParam();

        ByteString getClickActionParamBytes();

        String getClickActionType();

        ByteString getClickActionTypeBytes();

        String getCustomContent();

        ByteString getCustomContentBytes();

        boolean getLight();

        int getNotifyId();

        String getSmallImageUrl();

        ByteString getSmallImageUrlBytes();

        boolean getSound();

        String getSoundResource();

        ByteString getSoundResourceBytes();

        boolean getVibrate();

        boolean hasChannel();
    }

    public interface ChannelOrBuilder extends MessageOrBuilder {
        String getChannelGroupId();

        ByteString getChannelGroupIdBytes();

        String getChannelGroupName();

        ByteString getChannelGroupNameBytes();

        String getChannelId();

        ByteString getChannelIdBytes();

        String getChannelName();

        ByteString getChannelNameBytes();
    }

    public interface EmptyMsgOrBuilder extends MessageOrBuilder {
    }

    public interface ErrMsgOrBuilder extends MessageOrBuilder {
        String getErrMsg();

        ByteString getErrMsgBytes();
    }

    public interface FindOfflineNotificationRequestOrBuilder extends MessageOrBuilder {
    }

    public interface FindOfflineNotificationResponseOrBuilder extends MessageOrBuilder {
        Notification getNotifications(int i);

        int getNotificationsCount();

        List<Notification> getNotificationsList();

        NotificationOrBuilder getNotificationsOrBuilder(int i);

        List<? extends NotificationOrBuilder> getNotificationsOrBuilderList();
    }

    public interface HeartbeatRequestOrBuilder extends MessageOrBuilder {
    }

    public interface LoginRequestOrBuilder extends MessageOrBuilder {
        String getAccessKey();

        ByteString getAccessKeyBytes();

        String getAppVersion();

        ByteString getAppVersionBytes();

        String getDeviceBrand();

        ByteString getDeviceBrandBytes();

        String getDeviceModel();

        ByteString getDeviceModelBytes();

        boolean getPermitNotice();

        String getSdkVersion();

        ByteString getSdkVersionBytes();

        String getSystemVersion();

        ByteString getSystemVersionBytes();

        String getTimeZone();

        ByteString getTimeZoneBytes();

        String getToken();

        ByteString getTokenBytes();

        String getTransid();

        ByteString getTransidBytes();
    }

    public interface NotificationOrBuilder extends MessageOrBuilder {
        Android getAndroid();

        AndroidOrBuilder getAndroidOrBuilder();

        APNS getApns();

        APNSOrBuilder getApnsOrBuilder();

        String getContent();

        ByteString getContentBytes();

        String getFeatureContent();

        ByteString getFeatureContentBytes();

        String getFeatureSubTitle();

        ByteString getFeatureSubTitleBytes();

        String getFeatureTitle();

        ByteString getFeatureTitleBytes();

        boolean getSilent();

        String getSubTitle();

        ByteString getSubTitleBytes();

        SystemContent getSystemContent();

        SystemContentOrBuilder getSystemContentOrBuilder();

        String getTitle();

        ByteString getTitleBytes();

        boolean hasAndroid();

        boolean hasApns();

        boolean hasSystemContent();
    }

    public interface PreRegisterRequestOrBuilder extends MessageOrBuilder {
        String getChannel();

        ByteString getChannelBytes();

        String getClientKey();

        ByteString getClientKeyBytes();

        String getPkg();

        ByteString getPkgBytes();

        String getProductId();

        ByteString getProductIdBytes();
    }

    public interface PreRegisterResponseOrBuilder extends MessageOrBuilder {
        String getAuth();

        ByteString getAuthBytes();
    }

    public interface RegisterRequestOrBuilder extends MessageOrBuilder {
        boolean getApnsProduction();

        String getAppVersion();

        ByteString getAppVersionBytes();

        String getAuth();

        ByteString getAuthBytes();

        String getChannel();

        ByteString getChannelBytes();

        String getDeviceBrand();

        ByteString getDeviceBrandBytes();

        String getDeviceModel();

        ByteString getDeviceModelBytes();

        boolean getPermitNotice();

        String getPkg();

        ByteString getPkgBytes();

        String getProductId();

        ByteString getProductIdBytes();

        String getRegid();

        ByteString getRegidBytes();

        String getSdkVersion();

        ByteString getSdkVersionBytes();

        String getSystemVersion();

        ByteString getSystemVersionBytes();

        String getTimeZone();

        ByteString getTimeZoneBytes();
    }

    public interface RegisterResponseOrBuilder extends MessageOrBuilder {
        String getAccessKey();

        ByteString getAccessKeyBytes();

        String getToken();

        ByteString getTokenBytes();
    }

    public interface SystemContentOrBuilder extends MessageOrBuilder {
        boolean getFromMpay();

        String getPlanId();

        ByteString getPlanIdBytes();

        String getPushId();

        ByteString getPushIdBytes();
    }

    public static void registerAllExtensions(ExtensionRegistryLite extensionRegistryLite) {
    }

    private ClientSdkgate() {
    }

    public static void registerAllExtensions(ExtensionRegistry extensionRegistry) {
        registerAllExtensions((ExtensionRegistryLite) extensionRegistry);
    }

    public static final class ErrMsg extends GeneratedMessageV3 implements ErrMsgOrBuilder {
        public static final int ERR_MSG_FIELD_NUMBER = 1;
        private static final long serialVersionUID = 0;
        private volatile Object errMsg_;
        private byte memoizedIsInitialized;
        private static final ErrMsg DEFAULT_INSTANCE = new ErrMsg();
        private static final Parser<ErrMsg> PARSER = new AbstractParser<ErrMsg>() { // from class: com.netease.push.proto.nano.ClientSdkgate.ErrMsg.1
            @Override // com.google.protobuf.Parser
            public ErrMsg parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return new ErrMsg(codedInputStream, extensionRegistryLite);
            }
        };

        private ErrMsg(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private ErrMsg() {
            this.memoizedIsInitialized = (byte) -1;
            this.errMsg_ = "";
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private ErrMsg(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            this();
            if (extensionRegistryLite == null) {
                throw new NullPointerException();
            }
            UnknownFieldSet.Builder builderNewBuilder = UnknownFieldSet.newBuilder();
            boolean z = false;
            while (!z) {
                try {
                    try {
                        int tag = codedInputStream.readTag();
                        if (tag != 0) {
                            if (tag == 10) {
                                this.errMsg_ = codedInputStream.readStringRequireUtf8();
                            } else if (!parseUnknownField(codedInputStream, builderNewBuilder, extensionRegistryLite, tag)) {
                            }
                        }
                        z = true;
                    } catch (InvalidProtocolBufferException e) {
                        throw e.setUnfinishedMessage(this);
                    } catch (IOException e2) {
                        throw new InvalidProtocolBufferException(e2).setUnfinishedMessage(this);
                    }
                } finally {
                    this.unknownFields = builderNewBuilder.build();
                    makeExtensionsImmutable();
                }
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return ClientSdkgate.internal_static_proto_sdkgate_ErrMsg_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return ClientSdkgate.internal_static_proto_sdkgate_ErrMsg_fieldAccessorTable.ensureFieldAccessorsInitialized(ErrMsg.class, Builder.class);
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.ErrMsgOrBuilder
        public String getErrMsg() {
            Object obj = this.errMsg_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.errMsg_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.ErrMsgOrBuilder
        public ByteString getErrMsgBytes() {
            Object obj = this.errMsg_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.errMsg_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            if (b == 1) {
                return true;
            }
            if (b == 0) {
                return false;
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            if (!getErrMsgBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 1, this.errMsg_);
            }
            this.unknownFields.writeTo(codedOutputStream);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public int getSerializedSize() {
            int i = this.memoizedSize;
            if (i != -1) {
                return i;
            }
            int iComputeStringSize = (getErrMsgBytes().isEmpty() ? 0 : 0 + GeneratedMessageV3.computeStringSize(1, this.errMsg_)) + this.unknownFields.getSerializedSize();
            this.memoizedSize = iComputeStringSize;
            return iComputeStringSize;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof ErrMsg)) {
                return super.equals(obj);
            }
            ErrMsg errMsg = (ErrMsg) obj;
            return getErrMsg().equals(errMsg.getErrMsg()) && this.unknownFields.equals(errMsg.unknownFields);
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int iHashCode = ((((((779 + getDescriptor().hashCode()) * 37) + 1) * 53) + getErrMsg().hashCode()) * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = iHashCode;
            return iHashCode;
        }

        public static ErrMsg parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString);
        }

        public static ErrMsg parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString, extensionRegistryLite);
        }

        public static ErrMsg parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(bArr);
        }

        public static ErrMsg parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(bArr, extensionRegistryLite);
        }

        public static ErrMsg parseFrom(InputStream inputStream) throws IOException {
            return (ErrMsg) GeneratedMessageV3.parseWithIOException(PARSER, inputStream);
        }

        public static ErrMsg parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (ErrMsg) GeneratedMessageV3.parseWithIOException(PARSER, inputStream, extensionRegistryLite);
        }

        public static ErrMsg parseDelimitedFrom(InputStream inputStream) throws IOException {
            return (ErrMsg) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, inputStream);
        }

        public static ErrMsg parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (ErrMsg) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, inputStream, extensionRegistryLite);
        }

        public static ErrMsg parseFrom(CodedInputStream codedInputStream) throws IOException {
            return (ErrMsg) GeneratedMessageV3.parseWithIOException(PARSER, codedInputStream);
        }

        public static ErrMsg parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (ErrMsg) GeneratedMessageV3.parseWithIOException(PARSER, codedInputStream, extensionRegistryLite);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(ErrMsg errMsg) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(errMsg);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Builder newBuilderForType(GeneratedMessageV3.BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements ErrMsgOrBuilder {
            private Object errMsg_;

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return ClientSdkgate.internal_static_proto_sdkgate_ErrMsg_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return ClientSdkgate.internal_static_proto_sdkgate_ErrMsg_fieldAccessorTable.ensureFieldAccessorsInitialized(ErrMsg.class, Builder.class);
            }

            private Builder() {
                this.errMsg_ = "";
                maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessageV3.BuilderParent builderParent) {
                super(builderParent);
                this.errMsg_ = "";
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                boolean unused = ErrMsg.alwaysUseFieldBuilders;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                this.errMsg_ = "";
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return ClientSdkgate.internal_static_proto_sdkgate_ErrMsg_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public ErrMsg getDefaultInstanceForType() {
                return ErrMsg.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public ErrMsg build() {
                ErrMsg errMsgBuildPartial = buildPartial();
                if (errMsgBuildPartial.isInitialized()) {
                    return errMsgBuildPartial;
                }
                throw newUninitializedMessageException((Message) errMsgBuildPartial);
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public ErrMsg buildPartial() {
                ErrMsg errMsg = new ErrMsg(this);
                errMsg.errMsg_ = this.errMsg_;
                onBuilt();
                return errMsg;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder
            /* renamed from: clone */
            public Builder mo375clone() {
                return (Builder) super.mo375clone();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setField(Descriptors.FieldDescriptor fieldDescriptor, Object obj) {
                return (Builder) super.setField(fieldDescriptor, obj);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder clearField(Descriptors.FieldDescriptor fieldDescriptor) {
                return (Builder) super.clearField(fieldDescriptor);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder clearOneof(Descriptors.OneofDescriptor oneofDescriptor) {
                return (Builder) super.clearOneof(oneofDescriptor);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, int i, Object obj) {
                return (Builder) super.setRepeatedField(fieldDescriptor, i, obj);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder addRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, Object obj) {
                return (Builder) super.addRepeatedField(fieldDescriptor, obj);
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(Message message) {
                if (message instanceof ErrMsg) {
                    return mergeFrom((ErrMsg) message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder mergeFrom(ErrMsg errMsg) {
                if (errMsg == ErrMsg.getDefaultInstance()) {
                    return this;
                }
                if (!errMsg.getErrMsg().isEmpty()) {
                    this.errMsg_ = errMsg.errMsg_;
                    onChanged();
                }
                mergeUnknownFields(errMsg.unknownFields);
                onChanged();
                return this;
            }

            /* JADX WARN: Removed duplicated region for block: B:16:0x0023  */
            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public com.netease.push.proto.nano.ClientSdkgate.ErrMsg.Builder mergeFrom(com.google.protobuf.CodedInputStream r3, com.google.protobuf.ExtensionRegistryLite r4) throws java.lang.Throwable {
                /*
                    r2 = this;
                    r0 = 0
                    com.google.protobuf.Parser r1 = com.netease.push.proto.nano.ClientSdkgate.ErrMsg.access$800()     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    java.lang.Object r3 = r1.parsePartialFrom(r3, r4)     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    com.netease.push.proto.nano.ClientSdkgate$ErrMsg r3 = (com.netease.push.proto.nano.ClientSdkgate.ErrMsg) r3     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    if (r3 == 0) goto L10
                    r2.mergeFrom(r3)
                L10:
                    return r2
                L11:
                    r3 = move-exception
                    goto L21
                L13:
                    r3 = move-exception
                    com.google.protobuf.MessageLite r4 = r3.getUnfinishedMessage()     // Catch: java.lang.Throwable -> L11
                    com.netease.push.proto.nano.ClientSdkgate$ErrMsg r4 = (com.netease.push.proto.nano.ClientSdkgate.ErrMsg) r4     // Catch: java.lang.Throwable -> L11
                    java.io.IOException r3 = r3.unwrapIOException()     // Catch: java.lang.Throwable -> L1f
                    throw r3     // Catch: java.lang.Throwable -> L1f
                L1f:
                    r3 = move-exception
                    r0 = r4
                L21:
                    if (r0 == 0) goto L26
                    r2.mergeFrom(r0)
                L26:
                    throw r3
                */
                throw new UnsupportedOperationException("Method not decompiled: com.netease.push.proto.nano.ClientSdkgate.ErrMsg.Builder.mergeFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):com.netease.push.proto.nano.ClientSdkgate$ErrMsg$Builder");
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.ErrMsgOrBuilder
            public String getErrMsg() {
                Object obj = this.errMsg_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.errMsg_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.ErrMsgOrBuilder
            public ByteString getErrMsgBytes() {
                Object obj = this.errMsg_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.errMsg_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setErrMsg(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.errMsg_ = str;
                onChanged();
                return this;
            }

            public Builder clearErrMsg() {
                this.errMsg_ = ErrMsg.getDefaultInstance().getErrMsg();
                onChanged();
                return this;
            }

            public Builder setErrMsgBytes(ByteString byteString) {
                if (byteString != null) {
                    ErrMsg.checkByteStringIsUtf8(byteString);
                    this.errMsg_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public final Builder setUnknownFields(UnknownFieldSet unknownFieldSet) {
                return (Builder) super.setUnknownFields(unknownFieldSet);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public final Builder mergeUnknownFields(UnknownFieldSet unknownFieldSet) {
                return (Builder) super.mergeUnknownFields(unknownFieldSet);
            }
        }

        public static ErrMsg getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<ErrMsg> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<ErrMsg> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public ErrMsg getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    public static final class EmptyMsg extends GeneratedMessageV3 implements EmptyMsgOrBuilder {
        private static final EmptyMsg DEFAULT_INSTANCE = new EmptyMsg();
        private static final Parser<EmptyMsg> PARSER = new AbstractParser<EmptyMsg>() { // from class: com.netease.push.proto.nano.ClientSdkgate.EmptyMsg.1
            @Override // com.google.protobuf.Parser
            public EmptyMsg parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return new EmptyMsg(codedInputStream, extensionRegistryLite);
            }
        };
        private static final long serialVersionUID = 0;
        private byte memoizedIsInitialized;

        private EmptyMsg(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private EmptyMsg() {
            this.memoizedIsInitialized = (byte) -1;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private EmptyMsg(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            this();
            if (extensionRegistryLite == null) {
                throw new NullPointerException();
            }
            UnknownFieldSet.Builder builderNewBuilder = UnknownFieldSet.newBuilder();
            boolean z = false;
            while (!z) {
                try {
                    try {
                        int tag = codedInputStream.readTag();
                        if (tag == 0 || !parseUnknownField(codedInputStream, builderNewBuilder, extensionRegistryLite, tag)) {
                            z = true;
                        }
                    } catch (InvalidProtocolBufferException e) {
                        throw e.setUnfinishedMessage(this);
                    } catch (IOException e2) {
                        throw new InvalidProtocolBufferException(e2).setUnfinishedMessage(this);
                    }
                } finally {
                    this.unknownFields = builderNewBuilder.build();
                    makeExtensionsImmutable();
                }
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return ClientSdkgate.internal_static_proto_sdkgate_EmptyMsg_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return ClientSdkgate.internal_static_proto_sdkgate_EmptyMsg_fieldAccessorTable.ensureFieldAccessorsInitialized(EmptyMsg.class, Builder.class);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            if (b == 1) {
                return true;
            }
            if (b == 0) {
                return false;
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            this.unknownFields.writeTo(codedOutputStream);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public int getSerializedSize() {
            int i = this.memoizedSize;
            if (i != -1) {
                return i;
            }
            int serializedSize = this.unknownFields.getSerializedSize() + 0;
            this.memoizedSize = serializedSize;
            return serializedSize;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof EmptyMsg) {
                return this.unknownFields.equals(((EmptyMsg) obj).unknownFields);
            }
            return super.equals(obj);
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int iHashCode = ((779 + getDescriptor().hashCode()) * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = iHashCode;
            return iHashCode;
        }

        public static EmptyMsg parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString);
        }

        public static EmptyMsg parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString, extensionRegistryLite);
        }

        public static EmptyMsg parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(bArr);
        }

        public static EmptyMsg parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(bArr, extensionRegistryLite);
        }

        public static EmptyMsg parseFrom(InputStream inputStream) throws IOException {
            return (EmptyMsg) GeneratedMessageV3.parseWithIOException(PARSER, inputStream);
        }

        public static EmptyMsg parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (EmptyMsg) GeneratedMessageV3.parseWithIOException(PARSER, inputStream, extensionRegistryLite);
        }

        public static EmptyMsg parseDelimitedFrom(InputStream inputStream) throws IOException {
            return (EmptyMsg) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, inputStream);
        }

        public static EmptyMsg parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (EmptyMsg) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, inputStream, extensionRegistryLite);
        }

        public static EmptyMsg parseFrom(CodedInputStream codedInputStream) throws IOException {
            return (EmptyMsg) GeneratedMessageV3.parseWithIOException(PARSER, codedInputStream);
        }

        public static EmptyMsg parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (EmptyMsg) GeneratedMessageV3.parseWithIOException(PARSER, codedInputStream, extensionRegistryLite);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(EmptyMsg emptyMsg) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(emptyMsg);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Builder newBuilderForType(GeneratedMessageV3.BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements EmptyMsgOrBuilder {
            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return ClientSdkgate.internal_static_proto_sdkgate_EmptyMsg_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return ClientSdkgate.internal_static_proto_sdkgate_EmptyMsg_fieldAccessorTable.ensureFieldAccessorsInitialized(EmptyMsg.class, Builder.class);
            }

            private Builder() {
                maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessageV3.BuilderParent builderParent) {
                super(builderParent);
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                boolean unused = EmptyMsg.alwaysUseFieldBuilders;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return ClientSdkgate.internal_static_proto_sdkgate_EmptyMsg_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public EmptyMsg getDefaultInstanceForType() {
                return EmptyMsg.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public EmptyMsg build() {
                EmptyMsg emptyMsgBuildPartial = buildPartial();
                if (emptyMsgBuildPartial.isInitialized()) {
                    return emptyMsgBuildPartial;
                }
                throw newUninitializedMessageException((Message) emptyMsgBuildPartial);
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public EmptyMsg buildPartial() {
                EmptyMsg emptyMsg = new EmptyMsg(this);
                onBuilt();
                return emptyMsg;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder
            /* renamed from: clone */
            public Builder mo375clone() {
                return (Builder) super.mo375clone();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setField(Descriptors.FieldDescriptor fieldDescriptor, Object obj) {
                return (Builder) super.setField(fieldDescriptor, obj);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder clearField(Descriptors.FieldDescriptor fieldDescriptor) {
                return (Builder) super.clearField(fieldDescriptor);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder clearOneof(Descriptors.OneofDescriptor oneofDescriptor) {
                return (Builder) super.clearOneof(oneofDescriptor);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, int i, Object obj) {
                return (Builder) super.setRepeatedField(fieldDescriptor, i, obj);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder addRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, Object obj) {
                return (Builder) super.addRepeatedField(fieldDescriptor, obj);
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(Message message) {
                if (message instanceof EmptyMsg) {
                    return mergeFrom((EmptyMsg) message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder mergeFrom(EmptyMsg emptyMsg) {
                if (emptyMsg == EmptyMsg.getDefaultInstance()) {
                    return this;
                }
                mergeUnknownFields(emptyMsg.unknownFields);
                onChanged();
                return this;
            }

            /* JADX WARN: Removed duplicated region for block: B:16:0x0023  */
            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public com.netease.push.proto.nano.ClientSdkgate.EmptyMsg.Builder mergeFrom(com.google.protobuf.CodedInputStream r3, com.google.protobuf.ExtensionRegistryLite r4) throws java.lang.Throwable {
                /*
                    r2 = this;
                    r0 = 0
                    com.google.protobuf.Parser r1 = com.netease.push.proto.nano.ClientSdkgate.EmptyMsg.access$1800()     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    java.lang.Object r3 = r1.parsePartialFrom(r3, r4)     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    com.netease.push.proto.nano.ClientSdkgate$EmptyMsg r3 = (com.netease.push.proto.nano.ClientSdkgate.EmptyMsg) r3     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    if (r3 == 0) goto L10
                    r2.mergeFrom(r3)
                L10:
                    return r2
                L11:
                    r3 = move-exception
                    goto L21
                L13:
                    r3 = move-exception
                    com.google.protobuf.MessageLite r4 = r3.getUnfinishedMessage()     // Catch: java.lang.Throwable -> L11
                    com.netease.push.proto.nano.ClientSdkgate$EmptyMsg r4 = (com.netease.push.proto.nano.ClientSdkgate.EmptyMsg) r4     // Catch: java.lang.Throwable -> L11
                    java.io.IOException r3 = r3.unwrapIOException()     // Catch: java.lang.Throwable -> L1f
                    throw r3     // Catch: java.lang.Throwable -> L1f
                L1f:
                    r3 = move-exception
                    r0 = r4
                L21:
                    if (r0 == 0) goto L26
                    r2.mergeFrom(r0)
                L26:
                    throw r3
                */
                throw new UnsupportedOperationException("Method not decompiled: com.netease.push.proto.nano.ClientSdkgate.EmptyMsg.Builder.mergeFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):com.netease.push.proto.nano.ClientSdkgate$EmptyMsg$Builder");
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public final Builder setUnknownFields(UnknownFieldSet unknownFieldSet) {
                return (Builder) super.setUnknownFields(unknownFieldSet);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public final Builder mergeUnknownFields(UnknownFieldSet unknownFieldSet) {
                return (Builder) super.mergeUnknownFields(unknownFieldSet);
            }
        }

        public static EmptyMsg getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<EmptyMsg> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<EmptyMsg> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public EmptyMsg getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    public static final class PreRegisterRequest extends GeneratedMessageV3 implements PreRegisterRequestOrBuilder {
        public static final int CHANNEL_FIELD_NUMBER = 3;
        public static final int CLIENT_KEY_FIELD_NUMBER = 2;
        private static final PreRegisterRequest DEFAULT_INSTANCE = new PreRegisterRequest();
        private static final Parser<PreRegisterRequest> PARSER = new AbstractParser<PreRegisterRequest>() { // from class: com.netease.push.proto.nano.ClientSdkgate.PreRegisterRequest.1
            @Override // com.google.protobuf.Parser
            public PreRegisterRequest parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return new PreRegisterRequest(codedInputStream, extensionRegistryLite);
            }
        };
        public static final int PKG_FIELD_NUMBER = 4;
        public static final int PRODUCT_ID_FIELD_NUMBER = 1;
        private static final long serialVersionUID = 0;
        private volatile Object channel_;
        private volatile Object clientKey_;
        private byte memoizedIsInitialized;
        private volatile Object pkg_;
        private volatile Object productId_;

        private PreRegisterRequest(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private PreRegisterRequest() {
            this.memoizedIsInitialized = (byte) -1;
            this.productId_ = "";
            this.clientKey_ = "";
            this.channel_ = "";
            this.pkg_ = "";
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private PreRegisterRequest(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            this();
            if (extensionRegistryLite == null) {
                throw new NullPointerException();
            }
            UnknownFieldSet.Builder builderNewBuilder = UnknownFieldSet.newBuilder();
            boolean z = false;
            while (!z) {
                try {
                    try {
                        int tag = codedInputStream.readTag();
                        if (tag != 0) {
                            if (tag == 10) {
                                this.productId_ = codedInputStream.readStringRequireUtf8();
                            } else if (tag == 18) {
                                this.clientKey_ = codedInputStream.readStringRequireUtf8();
                            } else if (tag == 26) {
                                this.channel_ = codedInputStream.readStringRequireUtf8();
                            } else if (tag == 34) {
                                this.pkg_ = codedInputStream.readStringRequireUtf8();
                            } else if (!parseUnknownField(codedInputStream, builderNewBuilder, extensionRegistryLite, tag)) {
                            }
                        }
                        z = true;
                    } catch (InvalidProtocolBufferException e) {
                        throw e.setUnfinishedMessage(this);
                    } catch (IOException e2) {
                        throw new InvalidProtocolBufferException(e2).setUnfinishedMessage(this);
                    }
                } finally {
                    this.unknownFields = builderNewBuilder.build();
                    makeExtensionsImmutable();
                }
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return ClientSdkgate.internal_static_proto_sdkgate_PreRegisterRequest_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return ClientSdkgate.internal_static_proto_sdkgate_PreRegisterRequest_fieldAccessorTable.ensureFieldAccessorsInitialized(PreRegisterRequest.class, Builder.class);
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.PreRegisterRequestOrBuilder
        public String getProductId() {
            Object obj = this.productId_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.productId_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.PreRegisterRequestOrBuilder
        public ByteString getProductIdBytes() {
            Object obj = this.productId_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.productId_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.PreRegisterRequestOrBuilder
        public String getClientKey() {
            Object obj = this.clientKey_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.clientKey_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.PreRegisterRequestOrBuilder
        public ByteString getClientKeyBytes() {
            Object obj = this.clientKey_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.clientKey_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.PreRegisterRequestOrBuilder
        public String getChannel() {
            Object obj = this.channel_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.channel_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.PreRegisterRequestOrBuilder
        public ByteString getChannelBytes() {
            Object obj = this.channel_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.channel_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.PreRegisterRequestOrBuilder
        public String getPkg() {
            Object obj = this.pkg_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.pkg_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.PreRegisterRequestOrBuilder
        public ByteString getPkgBytes() {
            Object obj = this.pkg_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.pkg_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            if (b == 1) {
                return true;
            }
            if (b == 0) {
                return false;
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            if (!getProductIdBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 1, this.productId_);
            }
            if (!getClientKeyBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 2, this.clientKey_);
            }
            if (!getChannelBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 3, this.channel_);
            }
            if (!getPkgBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 4, this.pkg_);
            }
            this.unknownFields.writeTo(codedOutputStream);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public int getSerializedSize() {
            int i = this.memoizedSize;
            if (i != -1) {
                return i;
            }
            int iComputeStringSize = getProductIdBytes().isEmpty() ? 0 : 0 + GeneratedMessageV3.computeStringSize(1, this.productId_);
            if (!getClientKeyBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(2, this.clientKey_);
            }
            if (!getChannelBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(3, this.channel_);
            }
            if (!getPkgBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(4, this.pkg_);
            }
            int serializedSize = iComputeStringSize + this.unknownFields.getSerializedSize();
            this.memoizedSize = serializedSize;
            return serializedSize;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof PreRegisterRequest)) {
                return super.equals(obj);
            }
            PreRegisterRequest preRegisterRequest = (PreRegisterRequest) obj;
            return getProductId().equals(preRegisterRequest.getProductId()) && getClientKey().equals(preRegisterRequest.getClientKey()) && getChannel().equals(preRegisterRequest.getChannel()) && getPkg().equals(preRegisterRequest.getPkg()) && this.unknownFields.equals(preRegisterRequest.unknownFields);
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int iHashCode = ((((((((((((((((((779 + getDescriptor().hashCode()) * 37) + 1) * 53) + getProductId().hashCode()) * 37) + 2) * 53) + getClientKey().hashCode()) * 37) + 3) * 53) + getChannel().hashCode()) * 37) + 4) * 53) + getPkg().hashCode()) * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = iHashCode;
            return iHashCode;
        }

        public static PreRegisterRequest parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString);
        }

        public static PreRegisterRequest parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString, extensionRegistryLite);
        }

        public static PreRegisterRequest parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(bArr);
        }

        public static PreRegisterRequest parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(bArr, extensionRegistryLite);
        }

        public static PreRegisterRequest parseFrom(InputStream inputStream) throws IOException {
            return (PreRegisterRequest) GeneratedMessageV3.parseWithIOException(PARSER, inputStream);
        }

        public static PreRegisterRequest parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (PreRegisterRequest) GeneratedMessageV3.parseWithIOException(PARSER, inputStream, extensionRegistryLite);
        }

        public static PreRegisterRequest parseDelimitedFrom(InputStream inputStream) throws IOException {
            return (PreRegisterRequest) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, inputStream);
        }

        public static PreRegisterRequest parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (PreRegisterRequest) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, inputStream, extensionRegistryLite);
        }

        public static PreRegisterRequest parseFrom(CodedInputStream codedInputStream) throws IOException {
            return (PreRegisterRequest) GeneratedMessageV3.parseWithIOException(PARSER, codedInputStream);
        }

        public static PreRegisterRequest parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (PreRegisterRequest) GeneratedMessageV3.parseWithIOException(PARSER, codedInputStream, extensionRegistryLite);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(PreRegisterRequest preRegisterRequest) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(preRegisterRequest);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Builder newBuilderForType(GeneratedMessageV3.BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements PreRegisterRequestOrBuilder {
            private Object channel_;
            private Object clientKey_;
            private Object pkg_;
            private Object productId_;

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return ClientSdkgate.internal_static_proto_sdkgate_PreRegisterRequest_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return ClientSdkgate.internal_static_proto_sdkgate_PreRegisterRequest_fieldAccessorTable.ensureFieldAccessorsInitialized(PreRegisterRequest.class, Builder.class);
            }

            private Builder() {
                this.productId_ = "";
                this.clientKey_ = "";
                this.channel_ = "";
                this.pkg_ = "";
                maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessageV3.BuilderParent builderParent) {
                super(builderParent);
                this.productId_ = "";
                this.clientKey_ = "";
                this.channel_ = "";
                this.pkg_ = "";
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                boolean unused = PreRegisterRequest.alwaysUseFieldBuilders;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                this.productId_ = "";
                this.clientKey_ = "";
                this.channel_ = "";
                this.pkg_ = "";
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return ClientSdkgate.internal_static_proto_sdkgate_PreRegisterRequest_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public PreRegisterRequest getDefaultInstanceForType() {
                return PreRegisterRequest.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public PreRegisterRequest build() {
                PreRegisterRequest preRegisterRequestBuildPartial = buildPartial();
                if (preRegisterRequestBuildPartial.isInitialized()) {
                    return preRegisterRequestBuildPartial;
                }
                throw newUninitializedMessageException((Message) preRegisterRequestBuildPartial);
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public PreRegisterRequest buildPartial() {
                PreRegisterRequest preRegisterRequest = new PreRegisterRequest(this);
                preRegisterRequest.productId_ = this.productId_;
                preRegisterRequest.clientKey_ = this.clientKey_;
                preRegisterRequest.channel_ = this.channel_;
                preRegisterRequest.pkg_ = this.pkg_;
                onBuilt();
                return preRegisterRequest;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder
            /* renamed from: clone */
            public Builder mo375clone() {
                return (Builder) super.mo375clone();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setField(Descriptors.FieldDescriptor fieldDescriptor, Object obj) {
                return (Builder) super.setField(fieldDescriptor, obj);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder clearField(Descriptors.FieldDescriptor fieldDescriptor) {
                return (Builder) super.clearField(fieldDescriptor);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder clearOneof(Descriptors.OneofDescriptor oneofDescriptor) {
                return (Builder) super.clearOneof(oneofDescriptor);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, int i, Object obj) {
                return (Builder) super.setRepeatedField(fieldDescriptor, i, obj);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder addRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, Object obj) {
                return (Builder) super.addRepeatedField(fieldDescriptor, obj);
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(Message message) {
                if (message instanceof PreRegisterRequest) {
                    return mergeFrom((PreRegisterRequest) message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder mergeFrom(PreRegisterRequest preRegisterRequest) {
                if (preRegisterRequest == PreRegisterRequest.getDefaultInstance()) {
                    return this;
                }
                if (!preRegisterRequest.getProductId().isEmpty()) {
                    this.productId_ = preRegisterRequest.productId_;
                    onChanged();
                }
                if (!preRegisterRequest.getClientKey().isEmpty()) {
                    this.clientKey_ = preRegisterRequest.clientKey_;
                    onChanged();
                }
                if (!preRegisterRequest.getChannel().isEmpty()) {
                    this.channel_ = preRegisterRequest.channel_;
                    onChanged();
                }
                if (!preRegisterRequest.getPkg().isEmpty()) {
                    this.pkg_ = preRegisterRequest.pkg_;
                    onChanged();
                }
                mergeUnknownFields(preRegisterRequest.unknownFields);
                onChanged();
                return this;
            }

            /* JADX WARN: Removed duplicated region for block: B:16:0x0023  */
            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public com.netease.push.proto.nano.ClientSdkgate.PreRegisterRequest.Builder mergeFrom(com.google.protobuf.CodedInputStream r3, com.google.protobuf.ExtensionRegistryLite r4) throws java.lang.Throwable {
                /*
                    r2 = this;
                    r0 = 0
                    com.google.protobuf.Parser r1 = com.netease.push.proto.nano.ClientSdkgate.PreRegisterRequest.access$3100()     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    java.lang.Object r3 = r1.parsePartialFrom(r3, r4)     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    com.netease.push.proto.nano.ClientSdkgate$PreRegisterRequest r3 = (com.netease.push.proto.nano.ClientSdkgate.PreRegisterRequest) r3     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    if (r3 == 0) goto L10
                    r2.mergeFrom(r3)
                L10:
                    return r2
                L11:
                    r3 = move-exception
                    goto L21
                L13:
                    r3 = move-exception
                    com.google.protobuf.MessageLite r4 = r3.getUnfinishedMessage()     // Catch: java.lang.Throwable -> L11
                    com.netease.push.proto.nano.ClientSdkgate$PreRegisterRequest r4 = (com.netease.push.proto.nano.ClientSdkgate.PreRegisterRequest) r4     // Catch: java.lang.Throwable -> L11
                    java.io.IOException r3 = r3.unwrapIOException()     // Catch: java.lang.Throwable -> L1f
                    throw r3     // Catch: java.lang.Throwable -> L1f
                L1f:
                    r3 = move-exception
                    r0 = r4
                L21:
                    if (r0 == 0) goto L26
                    r2.mergeFrom(r0)
                L26:
                    throw r3
                */
                throw new UnsupportedOperationException("Method not decompiled: com.netease.push.proto.nano.ClientSdkgate.PreRegisterRequest.Builder.mergeFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):com.netease.push.proto.nano.ClientSdkgate$PreRegisterRequest$Builder");
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.PreRegisterRequestOrBuilder
            public String getProductId() {
                Object obj = this.productId_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.productId_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.PreRegisterRequestOrBuilder
            public ByteString getProductIdBytes() {
                Object obj = this.productId_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.productId_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setProductId(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.productId_ = str;
                onChanged();
                return this;
            }

            public Builder clearProductId() {
                this.productId_ = PreRegisterRequest.getDefaultInstance().getProductId();
                onChanged();
                return this;
            }

            public Builder setProductIdBytes(ByteString byteString) {
                if (byteString != null) {
                    PreRegisterRequest.checkByteStringIsUtf8(byteString);
                    this.productId_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.PreRegisterRequestOrBuilder
            public String getClientKey() {
                Object obj = this.clientKey_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.clientKey_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.PreRegisterRequestOrBuilder
            public ByteString getClientKeyBytes() {
                Object obj = this.clientKey_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.clientKey_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setClientKey(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.clientKey_ = str;
                onChanged();
                return this;
            }

            public Builder clearClientKey() {
                this.clientKey_ = PreRegisterRequest.getDefaultInstance().getClientKey();
                onChanged();
                return this;
            }

            public Builder setClientKeyBytes(ByteString byteString) {
                if (byteString != null) {
                    PreRegisterRequest.checkByteStringIsUtf8(byteString);
                    this.clientKey_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.PreRegisterRequestOrBuilder
            public String getChannel() {
                Object obj = this.channel_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.channel_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.PreRegisterRequestOrBuilder
            public ByteString getChannelBytes() {
                Object obj = this.channel_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.channel_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setChannel(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.channel_ = str;
                onChanged();
                return this;
            }

            public Builder clearChannel() {
                this.channel_ = PreRegisterRequest.getDefaultInstance().getChannel();
                onChanged();
                return this;
            }

            public Builder setChannelBytes(ByteString byteString) {
                if (byteString != null) {
                    PreRegisterRequest.checkByteStringIsUtf8(byteString);
                    this.channel_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.PreRegisterRequestOrBuilder
            public String getPkg() {
                Object obj = this.pkg_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.pkg_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.PreRegisterRequestOrBuilder
            public ByteString getPkgBytes() {
                Object obj = this.pkg_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.pkg_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setPkg(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.pkg_ = str;
                onChanged();
                return this;
            }

            public Builder clearPkg() {
                this.pkg_ = PreRegisterRequest.getDefaultInstance().getPkg();
                onChanged();
                return this;
            }

            public Builder setPkgBytes(ByteString byteString) {
                if (byteString != null) {
                    PreRegisterRequest.checkByteStringIsUtf8(byteString);
                    this.pkg_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public final Builder setUnknownFields(UnknownFieldSet unknownFieldSet) {
                return (Builder) super.setUnknownFields(unknownFieldSet);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public final Builder mergeUnknownFields(UnknownFieldSet unknownFieldSet) {
                return (Builder) super.mergeUnknownFields(unknownFieldSet);
            }
        }

        public static PreRegisterRequest getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<PreRegisterRequest> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<PreRegisterRequest> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public PreRegisterRequest getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    public static final class PreRegisterResponse extends GeneratedMessageV3 implements PreRegisterResponseOrBuilder {
        public static final int AUTH_FIELD_NUMBER = 1;
        private static final PreRegisterResponse DEFAULT_INSTANCE = new PreRegisterResponse();
        private static final Parser<PreRegisterResponse> PARSER = new AbstractParser<PreRegisterResponse>() { // from class: com.netease.push.proto.nano.ClientSdkgate.PreRegisterResponse.1
            @Override // com.google.protobuf.Parser
            public PreRegisterResponse parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return new PreRegisterResponse(codedInputStream, extensionRegistryLite);
            }
        };
        private static final long serialVersionUID = 0;
        private volatile Object auth_;
        private byte memoizedIsInitialized;

        private PreRegisterResponse(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private PreRegisterResponse() {
            this.memoizedIsInitialized = (byte) -1;
            this.auth_ = "";
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private PreRegisterResponse(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            this();
            if (extensionRegistryLite == null) {
                throw new NullPointerException();
            }
            UnknownFieldSet.Builder builderNewBuilder = UnknownFieldSet.newBuilder();
            boolean z = false;
            while (!z) {
                try {
                    try {
                        int tag = codedInputStream.readTag();
                        if (tag != 0) {
                            if (tag == 10) {
                                this.auth_ = codedInputStream.readStringRequireUtf8();
                            } else if (!parseUnknownField(codedInputStream, builderNewBuilder, extensionRegistryLite, tag)) {
                            }
                        }
                        z = true;
                    } catch (InvalidProtocolBufferException e) {
                        throw e.setUnfinishedMessage(this);
                    } catch (IOException e2) {
                        throw new InvalidProtocolBufferException(e2).setUnfinishedMessage(this);
                    }
                } finally {
                    this.unknownFields = builderNewBuilder.build();
                    makeExtensionsImmutable();
                }
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return ClientSdkgate.internal_static_proto_sdkgate_PreRegisterResponse_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return ClientSdkgate.internal_static_proto_sdkgate_PreRegisterResponse_fieldAccessorTable.ensureFieldAccessorsInitialized(PreRegisterResponse.class, Builder.class);
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.PreRegisterResponseOrBuilder
        public String getAuth() {
            Object obj = this.auth_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.auth_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.PreRegisterResponseOrBuilder
        public ByteString getAuthBytes() {
            Object obj = this.auth_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.auth_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            if (b == 1) {
                return true;
            }
            if (b == 0) {
                return false;
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            if (!getAuthBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 1, this.auth_);
            }
            this.unknownFields.writeTo(codedOutputStream);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public int getSerializedSize() {
            int i = this.memoizedSize;
            if (i != -1) {
                return i;
            }
            int iComputeStringSize = (getAuthBytes().isEmpty() ? 0 : 0 + GeneratedMessageV3.computeStringSize(1, this.auth_)) + this.unknownFields.getSerializedSize();
            this.memoizedSize = iComputeStringSize;
            return iComputeStringSize;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof PreRegisterResponse)) {
                return super.equals(obj);
            }
            PreRegisterResponse preRegisterResponse = (PreRegisterResponse) obj;
            return getAuth().equals(preRegisterResponse.getAuth()) && this.unknownFields.equals(preRegisterResponse.unknownFields);
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int iHashCode = ((((((779 + getDescriptor().hashCode()) * 37) + 1) * 53) + getAuth().hashCode()) * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = iHashCode;
            return iHashCode;
        }

        public static PreRegisterResponse parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString);
        }

        public static PreRegisterResponse parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString, extensionRegistryLite);
        }

        public static PreRegisterResponse parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(bArr);
        }

        public static PreRegisterResponse parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(bArr, extensionRegistryLite);
        }

        public static PreRegisterResponse parseFrom(InputStream inputStream) throws IOException {
            return (PreRegisterResponse) GeneratedMessageV3.parseWithIOException(PARSER, inputStream);
        }

        public static PreRegisterResponse parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (PreRegisterResponse) GeneratedMessageV3.parseWithIOException(PARSER, inputStream, extensionRegistryLite);
        }

        public static PreRegisterResponse parseDelimitedFrom(InputStream inputStream) throws IOException {
            return (PreRegisterResponse) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, inputStream);
        }

        public static PreRegisterResponse parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (PreRegisterResponse) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, inputStream, extensionRegistryLite);
        }

        public static PreRegisterResponse parseFrom(CodedInputStream codedInputStream) throws IOException {
            return (PreRegisterResponse) GeneratedMessageV3.parseWithIOException(PARSER, codedInputStream);
        }

        public static PreRegisterResponse parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (PreRegisterResponse) GeneratedMessageV3.parseWithIOException(PARSER, codedInputStream, extensionRegistryLite);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(PreRegisterResponse preRegisterResponse) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(preRegisterResponse);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Builder newBuilderForType(GeneratedMessageV3.BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements PreRegisterResponseOrBuilder {
            private Object auth_;

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return ClientSdkgate.internal_static_proto_sdkgate_PreRegisterResponse_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return ClientSdkgate.internal_static_proto_sdkgate_PreRegisterResponse_fieldAccessorTable.ensureFieldAccessorsInitialized(PreRegisterResponse.class, Builder.class);
            }

            private Builder() {
                this.auth_ = "";
                maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessageV3.BuilderParent builderParent) {
                super(builderParent);
                this.auth_ = "";
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                boolean unused = PreRegisterResponse.alwaysUseFieldBuilders;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                this.auth_ = "";
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return ClientSdkgate.internal_static_proto_sdkgate_PreRegisterResponse_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public PreRegisterResponse getDefaultInstanceForType() {
                return PreRegisterResponse.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public PreRegisterResponse build() {
                PreRegisterResponse preRegisterResponseBuildPartial = buildPartial();
                if (preRegisterResponseBuildPartial.isInitialized()) {
                    return preRegisterResponseBuildPartial;
                }
                throw newUninitializedMessageException((Message) preRegisterResponseBuildPartial);
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public PreRegisterResponse buildPartial() {
                PreRegisterResponse preRegisterResponse = new PreRegisterResponse(this);
                preRegisterResponse.auth_ = this.auth_;
                onBuilt();
                return preRegisterResponse;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder
            /* renamed from: clone */
            public Builder mo375clone() {
                return (Builder) super.mo375clone();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setField(Descriptors.FieldDescriptor fieldDescriptor, Object obj) {
                return (Builder) super.setField(fieldDescriptor, obj);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder clearField(Descriptors.FieldDescriptor fieldDescriptor) {
                return (Builder) super.clearField(fieldDescriptor);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder clearOneof(Descriptors.OneofDescriptor oneofDescriptor) {
                return (Builder) super.clearOneof(oneofDescriptor);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, int i, Object obj) {
                return (Builder) super.setRepeatedField(fieldDescriptor, i, obj);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder addRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, Object obj) {
                return (Builder) super.addRepeatedField(fieldDescriptor, obj);
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(Message message) {
                if (message instanceof PreRegisterResponse) {
                    return mergeFrom((PreRegisterResponse) message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder mergeFrom(PreRegisterResponse preRegisterResponse) {
                if (preRegisterResponse == PreRegisterResponse.getDefaultInstance()) {
                    return this;
                }
                if (!preRegisterResponse.getAuth().isEmpty()) {
                    this.auth_ = preRegisterResponse.auth_;
                    onChanged();
                }
                mergeUnknownFields(preRegisterResponse.unknownFields);
                onChanged();
                return this;
            }

            /* JADX WARN: Removed duplicated region for block: B:16:0x0023  */
            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public com.netease.push.proto.nano.ClientSdkgate.PreRegisterResponse.Builder mergeFrom(com.google.protobuf.CodedInputStream r3, com.google.protobuf.ExtensionRegistryLite r4) throws java.lang.Throwable {
                /*
                    r2 = this;
                    r0 = 0
                    com.google.protobuf.Parser r1 = com.netease.push.proto.nano.ClientSdkgate.PreRegisterResponse.access$4500()     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    java.lang.Object r3 = r1.parsePartialFrom(r3, r4)     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    com.netease.push.proto.nano.ClientSdkgate$PreRegisterResponse r3 = (com.netease.push.proto.nano.ClientSdkgate.PreRegisterResponse) r3     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    if (r3 == 0) goto L10
                    r2.mergeFrom(r3)
                L10:
                    return r2
                L11:
                    r3 = move-exception
                    goto L21
                L13:
                    r3 = move-exception
                    com.google.protobuf.MessageLite r4 = r3.getUnfinishedMessage()     // Catch: java.lang.Throwable -> L11
                    com.netease.push.proto.nano.ClientSdkgate$PreRegisterResponse r4 = (com.netease.push.proto.nano.ClientSdkgate.PreRegisterResponse) r4     // Catch: java.lang.Throwable -> L11
                    java.io.IOException r3 = r3.unwrapIOException()     // Catch: java.lang.Throwable -> L1f
                    throw r3     // Catch: java.lang.Throwable -> L1f
                L1f:
                    r3 = move-exception
                    r0 = r4
                L21:
                    if (r0 == 0) goto L26
                    r2.mergeFrom(r0)
                L26:
                    throw r3
                */
                throw new UnsupportedOperationException("Method not decompiled: com.netease.push.proto.nano.ClientSdkgate.PreRegisterResponse.Builder.mergeFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):com.netease.push.proto.nano.ClientSdkgate$PreRegisterResponse$Builder");
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.PreRegisterResponseOrBuilder
            public String getAuth() {
                Object obj = this.auth_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.auth_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.PreRegisterResponseOrBuilder
            public ByteString getAuthBytes() {
                Object obj = this.auth_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.auth_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setAuth(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.auth_ = str;
                onChanged();
                return this;
            }

            public Builder clearAuth() {
                this.auth_ = PreRegisterResponse.getDefaultInstance().getAuth();
                onChanged();
                return this;
            }

            public Builder setAuthBytes(ByteString byteString) {
                if (byteString != null) {
                    PreRegisterResponse.checkByteStringIsUtf8(byteString);
                    this.auth_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public final Builder setUnknownFields(UnknownFieldSet unknownFieldSet) {
                return (Builder) super.setUnknownFields(unknownFieldSet);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public final Builder mergeUnknownFields(UnknownFieldSet unknownFieldSet) {
                return (Builder) super.mergeUnknownFields(unknownFieldSet);
            }
        }

        public static PreRegisterResponse getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<PreRegisterResponse> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<PreRegisterResponse> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public PreRegisterResponse getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    public static final class RegisterRequest extends GeneratedMessageV3 implements RegisterRequestOrBuilder {
        public static final int APNS_PRODUCTION_FIELD_NUMBER = 50;
        public static final int APP_VERSION_FIELD_NUMBER = 11;
        public static final int AUTH_FIELD_NUMBER = 1;
        public static final int CHANNEL_FIELD_NUMBER = 2;
        public static final int DEVICE_BRAND_FIELD_NUMBER = 15;
        public static final int DEVICE_MODEL_FIELD_NUMBER = 16;
        public static final int PERMIT_NOTICE_FIELD_NUMBER = 10;
        public static final int PKG_FIELD_NUMBER = 3;
        public static final int PRODUCT_ID_FIELD_NUMBER = 100;
        public static final int REGID_FIELD_NUMBER = 4;
        public static final int SDK_VERSION_FIELD_NUMBER = 12;
        public static final int SYSTEM_VERSION_FIELD_NUMBER = 13;
        public static final int TIME_ZONE_FIELD_NUMBER = 14;
        private static final long serialVersionUID = 0;
        private boolean apnsProduction_;
        private volatile Object appVersion_;
        private volatile Object auth_;
        private volatile Object channel_;
        private volatile Object deviceBrand_;
        private volatile Object deviceModel_;
        private byte memoizedIsInitialized;
        private boolean permitNotice_;
        private volatile Object pkg_;
        private volatile Object productId_;
        private volatile Object regid_;
        private volatile Object sdkVersion_;
        private volatile Object systemVersion_;
        private volatile Object timeZone_;
        private static final RegisterRequest DEFAULT_INSTANCE = new RegisterRequest();
        private static final Parser<RegisterRequest> PARSER = new AbstractParser<RegisterRequest>() { // from class: com.netease.push.proto.nano.ClientSdkgate.RegisterRequest.1
            @Override // com.google.protobuf.Parser
            public RegisterRequest parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return new RegisterRequest(codedInputStream, extensionRegistryLite);
            }
        };

        private RegisterRequest(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private RegisterRequest() {
            this.memoizedIsInitialized = (byte) -1;
            this.auth_ = "";
            this.channel_ = "";
            this.pkg_ = "";
            this.regid_ = "";
            this.appVersion_ = "";
            this.sdkVersion_ = "";
            this.systemVersion_ = "";
            this.timeZone_ = "";
            this.deviceBrand_ = "";
            this.deviceModel_ = "";
            this.productId_ = "";
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private RegisterRequest(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            this();
            if (extensionRegistryLite == null) {
                throw new NullPointerException();
            }
            UnknownFieldSet.Builder builderNewBuilder = UnknownFieldSet.newBuilder();
            boolean z = false;
            while (!z) {
                try {
                    try {
                        int tag = codedInputStream.readTag();
                        switch (tag) {
                            case 0:
                                z = true;
                            case 10:
                                this.auth_ = codedInputStream.readStringRequireUtf8();
                            case 18:
                                this.channel_ = codedInputStream.readStringRequireUtf8();
                            case 26:
                                this.pkg_ = codedInputStream.readStringRequireUtf8();
                            case 34:
                                this.regid_ = codedInputStream.readStringRequireUtf8();
                            case 80:
                                this.permitNotice_ = codedInputStream.readBool();
                            case 90:
                                this.appVersion_ = codedInputStream.readStringRequireUtf8();
                            case 98:
                                this.sdkVersion_ = codedInputStream.readStringRequireUtf8();
                            case 106:
                                this.systemVersion_ = codedInputStream.readStringRequireUtf8();
                            case 114:
                                this.timeZone_ = codedInputStream.readStringRequireUtf8();
                            case 122:
                                this.deviceBrand_ = codedInputStream.readStringRequireUtf8();
                            case 130:
                                this.deviceModel_ = codedInputStream.readStringRequireUtf8();
                            case 400:
                                this.apnsProduction_ = codedInputStream.readBool();
                            case 802:
                                this.productId_ = codedInputStream.readStringRequireUtf8();
                            default:
                                if (!parseUnknownField(codedInputStream, builderNewBuilder, extensionRegistryLite, tag)) {
                                    z = true;
                                }
                        }
                    } catch (InvalidProtocolBufferException e) {
                        throw e.setUnfinishedMessage(this);
                    } catch (IOException e2) {
                        throw new InvalidProtocolBufferException(e2).setUnfinishedMessage(this);
                    }
                } finally {
                    this.unknownFields = builderNewBuilder.build();
                    makeExtensionsImmutable();
                }
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return ClientSdkgate.internal_static_proto_sdkgate_RegisterRequest_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return ClientSdkgate.internal_static_proto_sdkgate_RegisterRequest_fieldAccessorTable.ensureFieldAccessorsInitialized(RegisterRequest.class, Builder.class);
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
        public String getAuth() {
            Object obj = this.auth_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.auth_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
        public ByteString getAuthBytes() {
            Object obj = this.auth_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.auth_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
        public String getChannel() {
            Object obj = this.channel_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.channel_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
        public ByteString getChannelBytes() {
            Object obj = this.channel_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.channel_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
        public String getPkg() {
            Object obj = this.pkg_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.pkg_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
        public ByteString getPkgBytes() {
            Object obj = this.pkg_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.pkg_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
        public String getRegid() {
            Object obj = this.regid_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.regid_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
        public ByteString getRegidBytes() {
            Object obj = this.regid_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.regid_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
        public boolean getPermitNotice() {
            return this.permitNotice_;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
        public String getAppVersion() {
            Object obj = this.appVersion_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.appVersion_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
        public ByteString getAppVersionBytes() {
            Object obj = this.appVersion_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.appVersion_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
        public String getSdkVersion() {
            Object obj = this.sdkVersion_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.sdkVersion_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
        public ByteString getSdkVersionBytes() {
            Object obj = this.sdkVersion_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.sdkVersion_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
        public String getSystemVersion() {
            Object obj = this.systemVersion_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.systemVersion_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
        public ByteString getSystemVersionBytes() {
            Object obj = this.systemVersion_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.systemVersion_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
        public String getTimeZone() {
            Object obj = this.timeZone_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.timeZone_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
        public ByteString getTimeZoneBytes() {
            Object obj = this.timeZone_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.timeZone_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
        public String getDeviceBrand() {
            Object obj = this.deviceBrand_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.deviceBrand_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
        public ByteString getDeviceBrandBytes() {
            Object obj = this.deviceBrand_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.deviceBrand_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
        public String getDeviceModel() {
            Object obj = this.deviceModel_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.deviceModel_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
        public ByteString getDeviceModelBytes() {
            Object obj = this.deviceModel_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.deviceModel_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
        public boolean getApnsProduction() {
            return this.apnsProduction_;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
        public String getProductId() {
            Object obj = this.productId_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.productId_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
        public ByteString getProductIdBytes() {
            Object obj = this.productId_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.productId_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            if (b == 1) {
                return true;
            }
            if (b == 0) {
                return false;
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            if (!getAuthBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 1, this.auth_);
            }
            if (!getChannelBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 2, this.channel_);
            }
            if (!getPkgBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 3, this.pkg_);
            }
            if (!getRegidBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 4, this.regid_);
            }
            boolean z = this.permitNotice_;
            if (z) {
                codedOutputStream.writeBool(10, z);
            }
            if (!getAppVersionBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 11, this.appVersion_);
            }
            if (!getSdkVersionBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 12, this.sdkVersion_);
            }
            if (!getSystemVersionBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 13, this.systemVersion_);
            }
            if (!getTimeZoneBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 14, this.timeZone_);
            }
            if (!getDeviceBrandBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 15, this.deviceBrand_);
            }
            if (!getDeviceModelBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 16, this.deviceModel_);
            }
            boolean z2 = this.apnsProduction_;
            if (z2) {
                codedOutputStream.writeBool(50, z2);
            }
            if (!getProductIdBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 100, this.productId_);
            }
            this.unknownFields.writeTo(codedOutputStream);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public int getSerializedSize() {
            int i = this.memoizedSize;
            if (i != -1) {
                return i;
            }
            int iComputeStringSize = getAuthBytes().isEmpty() ? 0 : 0 + GeneratedMessageV3.computeStringSize(1, this.auth_);
            if (!getChannelBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(2, this.channel_);
            }
            if (!getPkgBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(3, this.pkg_);
            }
            if (!getRegidBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(4, this.regid_);
            }
            boolean z = this.permitNotice_;
            if (z) {
                iComputeStringSize += CodedOutputStream.computeBoolSize(10, z);
            }
            if (!getAppVersionBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(11, this.appVersion_);
            }
            if (!getSdkVersionBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(12, this.sdkVersion_);
            }
            if (!getSystemVersionBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(13, this.systemVersion_);
            }
            if (!getTimeZoneBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(14, this.timeZone_);
            }
            if (!getDeviceBrandBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(15, this.deviceBrand_);
            }
            if (!getDeviceModelBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(16, this.deviceModel_);
            }
            boolean z2 = this.apnsProduction_;
            if (z2) {
                iComputeStringSize += CodedOutputStream.computeBoolSize(50, z2);
            }
            if (!getProductIdBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(100, this.productId_);
            }
            int serializedSize = iComputeStringSize + this.unknownFields.getSerializedSize();
            this.memoizedSize = serializedSize;
            return serializedSize;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof RegisterRequest)) {
                return super.equals(obj);
            }
            RegisterRequest registerRequest = (RegisterRequest) obj;
            return getAuth().equals(registerRequest.getAuth()) && getChannel().equals(registerRequest.getChannel()) && getPkg().equals(registerRequest.getPkg()) && getRegid().equals(registerRequest.getRegid()) && getPermitNotice() == registerRequest.getPermitNotice() && getAppVersion().equals(registerRequest.getAppVersion()) && getSdkVersion().equals(registerRequest.getSdkVersion()) && getSystemVersion().equals(registerRequest.getSystemVersion()) && getTimeZone().equals(registerRequest.getTimeZone()) && getDeviceBrand().equals(registerRequest.getDeviceBrand()) && getDeviceModel().equals(registerRequest.getDeviceModel()) && getApnsProduction() == registerRequest.getApnsProduction() && getProductId().equals(registerRequest.getProductId()) && this.unknownFields.equals(registerRequest.unknownFields);
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int iHashCode = ((((((((((((((((((((((((((((((((((((((((((((((((((((((779 + getDescriptor().hashCode()) * 37) + 1) * 53) + getAuth().hashCode()) * 37) + 2) * 53) + getChannel().hashCode()) * 37) + 3) * 53) + getPkg().hashCode()) * 37) + 4) * 53) + getRegid().hashCode()) * 37) + 10) * 53) + Internal.hashBoolean(getPermitNotice())) * 37) + 11) * 53) + getAppVersion().hashCode()) * 37) + 12) * 53) + getSdkVersion().hashCode()) * 37) + 13) * 53) + getSystemVersion().hashCode()) * 37) + 14) * 53) + getTimeZone().hashCode()) * 37) + 15) * 53) + getDeviceBrand().hashCode()) * 37) + 16) * 53) + getDeviceModel().hashCode()) * 37) + 50) * 53) + Internal.hashBoolean(getApnsProduction())) * 37) + 100) * 53) + getProductId().hashCode()) * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = iHashCode;
            return iHashCode;
        }

        public static RegisterRequest parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString);
        }

        public static RegisterRequest parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString, extensionRegistryLite);
        }

        public static RegisterRequest parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(bArr);
        }

        public static RegisterRequest parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(bArr, extensionRegistryLite);
        }

        public static RegisterRequest parseFrom(InputStream inputStream) throws IOException {
            return (RegisterRequest) GeneratedMessageV3.parseWithIOException(PARSER, inputStream);
        }

        public static RegisterRequest parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (RegisterRequest) GeneratedMessageV3.parseWithIOException(PARSER, inputStream, extensionRegistryLite);
        }

        public static RegisterRequest parseDelimitedFrom(InputStream inputStream) throws IOException {
            return (RegisterRequest) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, inputStream);
        }

        public static RegisterRequest parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (RegisterRequest) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, inputStream, extensionRegistryLite);
        }

        public static RegisterRequest parseFrom(CodedInputStream codedInputStream) throws IOException {
            return (RegisterRequest) GeneratedMessageV3.parseWithIOException(PARSER, codedInputStream);
        }

        public static RegisterRequest parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (RegisterRequest) GeneratedMessageV3.parseWithIOException(PARSER, codedInputStream, extensionRegistryLite);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(RegisterRequest registerRequest) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(registerRequest);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Builder newBuilderForType(GeneratedMessageV3.BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements RegisterRequestOrBuilder {
            private boolean apnsProduction_;
            private Object appVersion_;
            private Object auth_;
            private Object channel_;
            private Object deviceBrand_;
            private Object deviceModel_;
            private boolean permitNotice_;
            private Object pkg_;
            private Object productId_;
            private Object regid_;
            private Object sdkVersion_;
            private Object systemVersion_;
            private Object timeZone_;

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return ClientSdkgate.internal_static_proto_sdkgate_RegisterRequest_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return ClientSdkgate.internal_static_proto_sdkgate_RegisterRequest_fieldAccessorTable.ensureFieldAccessorsInitialized(RegisterRequest.class, Builder.class);
            }

            private Builder() {
                this.auth_ = "";
                this.channel_ = "";
                this.pkg_ = "";
                this.regid_ = "";
                this.appVersion_ = "";
                this.sdkVersion_ = "";
                this.systemVersion_ = "";
                this.timeZone_ = "";
                this.deviceBrand_ = "";
                this.deviceModel_ = "";
                this.productId_ = "";
                maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessageV3.BuilderParent builderParent) {
                super(builderParent);
                this.auth_ = "";
                this.channel_ = "";
                this.pkg_ = "";
                this.regid_ = "";
                this.appVersion_ = "";
                this.sdkVersion_ = "";
                this.systemVersion_ = "";
                this.timeZone_ = "";
                this.deviceBrand_ = "";
                this.deviceModel_ = "";
                this.productId_ = "";
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                boolean unused = RegisterRequest.alwaysUseFieldBuilders;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                this.auth_ = "";
                this.channel_ = "";
                this.pkg_ = "";
                this.regid_ = "";
                this.permitNotice_ = false;
                this.appVersion_ = "";
                this.sdkVersion_ = "";
                this.systemVersion_ = "";
                this.timeZone_ = "";
                this.deviceBrand_ = "";
                this.deviceModel_ = "";
                this.apnsProduction_ = false;
                this.productId_ = "";
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return ClientSdkgate.internal_static_proto_sdkgate_RegisterRequest_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public RegisterRequest getDefaultInstanceForType() {
                return RegisterRequest.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public RegisterRequest build() {
                RegisterRequest registerRequestBuildPartial = buildPartial();
                if (registerRequestBuildPartial.isInitialized()) {
                    return registerRequestBuildPartial;
                }
                throw newUninitializedMessageException((Message) registerRequestBuildPartial);
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public RegisterRequest buildPartial() {
                RegisterRequest registerRequest = new RegisterRequest(this);
                registerRequest.auth_ = this.auth_;
                registerRequest.channel_ = this.channel_;
                registerRequest.pkg_ = this.pkg_;
                registerRequest.regid_ = this.regid_;
                registerRequest.permitNotice_ = this.permitNotice_;
                registerRequest.appVersion_ = this.appVersion_;
                registerRequest.sdkVersion_ = this.sdkVersion_;
                registerRequest.systemVersion_ = this.systemVersion_;
                registerRequest.timeZone_ = this.timeZone_;
                registerRequest.deviceBrand_ = this.deviceBrand_;
                registerRequest.deviceModel_ = this.deviceModel_;
                registerRequest.apnsProduction_ = this.apnsProduction_;
                registerRequest.productId_ = this.productId_;
                onBuilt();
                return registerRequest;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder
            /* renamed from: clone */
            public Builder mo375clone() {
                return (Builder) super.mo375clone();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setField(Descriptors.FieldDescriptor fieldDescriptor, Object obj) {
                return (Builder) super.setField(fieldDescriptor, obj);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder clearField(Descriptors.FieldDescriptor fieldDescriptor) {
                return (Builder) super.clearField(fieldDescriptor);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder clearOneof(Descriptors.OneofDescriptor oneofDescriptor) {
                return (Builder) super.clearOneof(oneofDescriptor);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, int i, Object obj) {
                return (Builder) super.setRepeatedField(fieldDescriptor, i, obj);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder addRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, Object obj) {
                return (Builder) super.addRepeatedField(fieldDescriptor, obj);
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(Message message) {
                if (message instanceof RegisterRequest) {
                    return mergeFrom((RegisterRequest) message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder mergeFrom(RegisterRequest registerRequest) {
                if (registerRequest == RegisterRequest.getDefaultInstance()) {
                    return this;
                }
                if (!registerRequest.getAuth().isEmpty()) {
                    this.auth_ = registerRequest.auth_;
                    onChanged();
                }
                if (!registerRequest.getChannel().isEmpty()) {
                    this.channel_ = registerRequest.channel_;
                    onChanged();
                }
                if (!registerRequest.getPkg().isEmpty()) {
                    this.pkg_ = registerRequest.pkg_;
                    onChanged();
                }
                if (!registerRequest.getRegid().isEmpty()) {
                    this.regid_ = registerRequest.regid_;
                    onChanged();
                }
                if (registerRequest.getPermitNotice()) {
                    setPermitNotice(registerRequest.getPermitNotice());
                }
                if (!registerRequest.getAppVersion().isEmpty()) {
                    this.appVersion_ = registerRequest.appVersion_;
                    onChanged();
                }
                if (!registerRequest.getSdkVersion().isEmpty()) {
                    this.sdkVersion_ = registerRequest.sdkVersion_;
                    onChanged();
                }
                if (!registerRequest.getSystemVersion().isEmpty()) {
                    this.systemVersion_ = registerRequest.systemVersion_;
                    onChanged();
                }
                if (!registerRequest.getTimeZone().isEmpty()) {
                    this.timeZone_ = registerRequest.timeZone_;
                    onChanged();
                }
                if (!registerRequest.getDeviceBrand().isEmpty()) {
                    this.deviceBrand_ = registerRequest.deviceBrand_;
                    onChanged();
                }
                if (!registerRequest.getDeviceModel().isEmpty()) {
                    this.deviceModel_ = registerRequest.deviceModel_;
                    onChanged();
                }
                if (registerRequest.getApnsProduction()) {
                    setApnsProduction(registerRequest.getApnsProduction());
                }
                if (!registerRequest.getProductId().isEmpty()) {
                    this.productId_ = registerRequest.productId_;
                    onChanged();
                }
                mergeUnknownFields(registerRequest.unknownFields);
                onChanged();
                return this;
            }

            /* JADX WARN: Removed duplicated region for block: B:16:0x0023  */
            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public com.netease.push.proto.nano.ClientSdkgate.RegisterRequest.Builder mergeFrom(com.google.protobuf.CodedInputStream r3, com.google.protobuf.ExtensionRegistryLite r4) throws java.lang.Throwable {
                /*
                    r2 = this;
                    r0 = 0
                    com.google.protobuf.Parser r1 = com.netease.push.proto.nano.ClientSdkgate.RegisterRequest.access$6800()     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    java.lang.Object r3 = r1.parsePartialFrom(r3, r4)     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    com.netease.push.proto.nano.ClientSdkgate$RegisterRequest r3 = (com.netease.push.proto.nano.ClientSdkgate.RegisterRequest) r3     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    if (r3 == 0) goto L10
                    r2.mergeFrom(r3)
                L10:
                    return r2
                L11:
                    r3 = move-exception
                    goto L21
                L13:
                    r3 = move-exception
                    com.google.protobuf.MessageLite r4 = r3.getUnfinishedMessage()     // Catch: java.lang.Throwable -> L11
                    com.netease.push.proto.nano.ClientSdkgate$RegisterRequest r4 = (com.netease.push.proto.nano.ClientSdkgate.RegisterRequest) r4     // Catch: java.lang.Throwable -> L11
                    java.io.IOException r3 = r3.unwrapIOException()     // Catch: java.lang.Throwable -> L1f
                    throw r3     // Catch: java.lang.Throwable -> L1f
                L1f:
                    r3 = move-exception
                    r0 = r4
                L21:
                    if (r0 == 0) goto L26
                    r2.mergeFrom(r0)
                L26:
                    throw r3
                */
                throw new UnsupportedOperationException("Method not decompiled: com.netease.push.proto.nano.ClientSdkgate.RegisterRequest.Builder.mergeFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):com.netease.push.proto.nano.ClientSdkgate$RegisterRequest$Builder");
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
            public String getAuth() {
                Object obj = this.auth_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.auth_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
            public ByteString getAuthBytes() {
                Object obj = this.auth_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.auth_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setAuth(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.auth_ = str;
                onChanged();
                return this;
            }

            public Builder clearAuth() {
                this.auth_ = RegisterRequest.getDefaultInstance().getAuth();
                onChanged();
                return this;
            }

            public Builder setAuthBytes(ByteString byteString) {
                if (byteString != null) {
                    RegisterRequest.checkByteStringIsUtf8(byteString);
                    this.auth_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
            public String getChannel() {
                Object obj = this.channel_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.channel_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
            public ByteString getChannelBytes() {
                Object obj = this.channel_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.channel_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setChannel(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.channel_ = str;
                onChanged();
                return this;
            }

            public Builder clearChannel() {
                this.channel_ = RegisterRequest.getDefaultInstance().getChannel();
                onChanged();
                return this;
            }

            public Builder setChannelBytes(ByteString byteString) {
                if (byteString != null) {
                    RegisterRequest.checkByteStringIsUtf8(byteString);
                    this.channel_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
            public String getPkg() {
                Object obj = this.pkg_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.pkg_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
            public ByteString getPkgBytes() {
                Object obj = this.pkg_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.pkg_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setPkg(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.pkg_ = str;
                onChanged();
                return this;
            }

            public Builder clearPkg() {
                this.pkg_ = RegisterRequest.getDefaultInstance().getPkg();
                onChanged();
                return this;
            }

            public Builder setPkgBytes(ByteString byteString) {
                if (byteString != null) {
                    RegisterRequest.checkByteStringIsUtf8(byteString);
                    this.pkg_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
            public String getRegid() {
                Object obj = this.regid_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.regid_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
            public ByteString getRegidBytes() {
                Object obj = this.regid_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.regid_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setRegid(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.regid_ = str;
                onChanged();
                return this;
            }

            public Builder clearRegid() {
                this.regid_ = RegisterRequest.getDefaultInstance().getRegid();
                onChanged();
                return this;
            }

            public Builder setRegidBytes(ByteString byteString) {
                if (byteString != null) {
                    RegisterRequest.checkByteStringIsUtf8(byteString);
                    this.regid_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
            public boolean getPermitNotice() {
                return this.permitNotice_;
            }

            public Builder setPermitNotice(boolean z) {
                this.permitNotice_ = z;
                onChanged();
                return this;
            }

            public Builder clearPermitNotice() {
                this.permitNotice_ = false;
                onChanged();
                return this;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
            public String getAppVersion() {
                Object obj = this.appVersion_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.appVersion_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
            public ByteString getAppVersionBytes() {
                Object obj = this.appVersion_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.appVersion_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setAppVersion(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.appVersion_ = str;
                onChanged();
                return this;
            }

            public Builder clearAppVersion() {
                this.appVersion_ = RegisterRequest.getDefaultInstance().getAppVersion();
                onChanged();
                return this;
            }

            public Builder setAppVersionBytes(ByteString byteString) {
                if (byteString != null) {
                    RegisterRequest.checkByteStringIsUtf8(byteString);
                    this.appVersion_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
            public String getSdkVersion() {
                Object obj = this.sdkVersion_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.sdkVersion_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
            public ByteString getSdkVersionBytes() {
                Object obj = this.sdkVersion_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.sdkVersion_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setSdkVersion(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.sdkVersion_ = str;
                onChanged();
                return this;
            }

            public Builder clearSdkVersion() {
                this.sdkVersion_ = RegisterRequest.getDefaultInstance().getSdkVersion();
                onChanged();
                return this;
            }

            public Builder setSdkVersionBytes(ByteString byteString) {
                if (byteString != null) {
                    RegisterRequest.checkByteStringIsUtf8(byteString);
                    this.sdkVersion_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
            public String getSystemVersion() {
                Object obj = this.systemVersion_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.systemVersion_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
            public ByteString getSystemVersionBytes() {
                Object obj = this.systemVersion_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.systemVersion_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setSystemVersion(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.systemVersion_ = str;
                onChanged();
                return this;
            }

            public Builder clearSystemVersion() {
                this.systemVersion_ = RegisterRequest.getDefaultInstance().getSystemVersion();
                onChanged();
                return this;
            }

            public Builder setSystemVersionBytes(ByteString byteString) {
                if (byteString != null) {
                    RegisterRequest.checkByteStringIsUtf8(byteString);
                    this.systemVersion_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
            public String getTimeZone() {
                Object obj = this.timeZone_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.timeZone_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
            public ByteString getTimeZoneBytes() {
                Object obj = this.timeZone_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.timeZone_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setTimeZone(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.timeZone_ = str;
                onChanged();
                return this;
            }

            public Builder clearTimeZone() {
                this.timeZone_ = RegisterRequest.getDefaultInstance().getTimeZone();
                onChanged();
                return this;
            }

            public Builder setTimeZoneBytes(ByteString byteString) {
                if (byteString != null) {
                    RegisterRequest.checkByteStringIsUtf8(byteString);
                    this.timeZone_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
            public String getDeviceBrand() {
                Object obj = this.deviceBrand_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.deviceBrand_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
            public ByteString getDeviceBrandBytes() {
                Object obj = this.deviceBrand_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.deviceBrand_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setDeviceBrand(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.deviceBrand_ = str;
                onChanged();
                return this;
            }

            public Builder clearDeviceBrand() {
                this.deviceBrand_ = RegisterRequest.getDefaultInstance().getDeviceBrand();
                onChanged();
                return this;
            }

            public Builder setDeviceBrandBytes(ByteString byteString) {
                if (byteString != null) {
                    RegisterRequest.checkByteStringIsUtf8(byteString);
                    this.deviceBrand_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
            public String getDeviceModel() {
                Object obj = this.deviceModel_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.deviceModel_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
            public ByteString getDeviceModelBytes() {
                Object obj = this.deviceModel_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.deviceModel_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setDeviceModel(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.deviceModel_ = str;
                onChanged();
                return this;
            }

            public Builder clearDeviceModel() {
                this.deviceModel_ = RegisterRequest.getDefaultInstance().getDeviceModel();
                onChanged();
                return this;
            }

            public Builder setDeviceModelBytes(ByteString byteString) {
                if (byteString != null) {
                    RegisterRequest.checkByteStringIsUtf8(byteString);
                    this.deviceModel_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
            public boolean getApnsProduction() {
                return this.apnsProduction_;
            }

            public Builder setApnsProduction(boolean z) {
                this.apnsProduction_ = z;
                onChanged();
                return this;
            }

            public Builder clearApnsProduction() {
                this.apnsProduction_ = false;
                onChanged();
                return this;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
            public String getProductId() {
                Object obj = this.productId_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.productId_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterRequestOrBuilder
            public ByteString getProductIdBytes() {
                Object obj = this.productId_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.productId_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setProductId(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.productId_ = str;
                onChanged();
                return this;
            }

            public Builder clearProductId() {
                this.productId_ = RegisterRequest.getDefaultInstance().getProductId();
                onChanged();
                return this;
            }

            public Builder setProductIdBytes(ByteString byteString) {
                if (byteString != null) {
                    RegisterRequest.checkByteStringIsUtf8(byteString);
                    this.productId_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public final Builder setUnknownFields(UnknownFieldSet unknownFieldSet) {
                return (Builder) super.setUnknownFields(unknownFieldSet);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public final Builder mergeUnknownFields(UnknownFieldSet unknownFieldSet) {
                return (Builder) super.mergeUnknownFields(unknownFieldSet);
            }
        }

        public static RegisterRequest getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<RegisterRequest> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<RegisterRequest> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public RegisterRequest getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    public static final class RegisterResponse extends GeneratedMessageV3 implements RegisterResponseOrBuilder {
        public static final int ACCESS_KEY_FIELD_NUMBER = 2;
        private static final RegisterResponse DEFAULT_INSTANCE = new RegisterResponse();
        private static final Parser<RegisterResponse> PARSER = new AbstractParser<RegisterResponse>() { // from class: com.netease.push.proto.nano.ClientSdkgate.RegisterResponse.1
            @Override // com.google.protobuf.Parser
            public RegisterResponse parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return new RegisterResponse(codedInputStream, extensionRegistryLite);
            }
        };
        public static final int TOKEN_FIELD_NUMBER = 1;
        private static final long serialVersionUID = 0;
        private volatile Object accessKey_;
        private byte memoizedIsInitialized;
        private volatile Object token_;

        private RegisterResponse(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private RegisterResponse() {
            this.memoizedIsInitialized = (byte) -1;
            this.token_ = "";
            this.accessKey_ = "";
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private RegisterResponse(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            this();
            if (extensionRegistryLite == null) {
                throw new NullPointerException();
            }
            UnknownFieldSet.Builder builderNewBuilder = UnknownFieldSet.newBuilder();
            boolean z = false;
            while (!z) {
                try {
                    try {
                        try {
                            int tag = codedInputStream.readTag();
                            if (tag != 0) {
                                if (tag == 10) {
                                    this.token_ = codedInputStream.readStringRequireUtf8();
                                } else if (tag == 18) {
                                    this.accessKey_ = codedInputStream.readStringRequireUtf8();
                                } else if (!parseUnknownField(codedInputStream, builderNewBuilder, extensionRegistryLite, tag)) {
                                }
                            }
                            z = true;
                        } catch (InvalidProtocolBufferException e) {
                            throw e.setUnfinishedMessage(this);
                        }
                    } catch (IOException e2) {
                        throw new InvalidProtocolBufferException(e2).setUnfinishedMessage(this);
                    }
                } finally {
                    this.unknownFields = builderNewBuilder.build();
                    makeExtensionsImmutable();
                }
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return ClientSdkgate.internal_static_proto_sdkgate_RegisterResponse_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return ClientSdkgate.internal_static_proto_sdkgate_RegisterResponse_fieldAccessorTable.ensureFieldAccessorsInitialized(RegisterResponse.class, Builder.class);
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterResponseOrBuilder
        public String getToken() {
            Object obj = this.token_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.token_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterResponseOrBuilder
        public ByteString getTokenBytes() {
            Object obj = this.token_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.token_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterResponseOrBuilder
        public String getAccessKey() {
            Object obj = this.accessKey_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.accessKey_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterResponseOrBuilder
        public ByteString getAccessKeyBytes() {
            Object obj = this.accessKey_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.accessKey_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            if (b == 1) {
                return true;
            }
            if (b == 0) {
                return false;
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            if (!getTokenBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 1, this.token_);
            }
            if (!getAccessKeyBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 2, this.accessKey_);
            }
            this.unknownFields.writeTo(codedOutputStream);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public int getSerializedSize() {
            int i = this.memoizedSize;
            if (i != -1) {
                return i;
            }
            int iComputeStringSize = getTokenBytes().isEmpty() ? 0 : 0 + GeneratedMessageV3.computeStringSize(1, this.token_);
            if (!getAccessKeyBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(2, this.accessKey_);
            }
            int serializedSize = iComputeStringSize + this.unknownFields.getSerializedSize();
            this.memoizedSize = serializedSize;
            return serializedSize;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof RegisterResponse)) {
                return super.equals(obj);
            }
            RegisterResponse registerResponse = (RegisterResponse) obj;
            return getToken().equals(registerResponse.getToken()) && getAccessKey().equals(registerResponse.getAccessKey()) && this.unknownFields.equals(registerResponse.unknownFields);
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int iHashCode = ((((((((((779 + getDescriptor().hashCode()) * 37) + 1) * 53) + getToken().hashCode()) * 37) + 2) * 53) + getAccessKey().hashCode()) * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = iHashCode;
            return iHashCode;
        }

        public static RegisterResponse parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString);
        }

        public static RegisterResponse parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString, extensionRegistryLite);
        }

        public static RegisterResponse parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(bArr);
        }

        public static RegisterResponse parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(bArr, extensionRegistryLite);
        }

        public static RegisterResponse parseFrom(InputStream inputStream) throws IOException {
            return (RegisterResponse) GeneratedMessageV3.parseWithIOException(PARSER, inputStream);
        }

        public static RegisterResponse parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (RegisterResponse) GeneratedMessageV3.parseWithIOException(PARSER, inputStream, extensionRegistryLite);
        }

        public static RegisterResponse parseDelimitedFrom(InputStream inputStream) throws IOException {
            return (RegisterResponse) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, inputStream);
        }

        public static RegisterResponse parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (RegisterResponse) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, inputStream, extensionRegistryLite);
        }

        public static RegisterResponse parseFrom(CodedInputStream codedInputStream) throws IOException {
            return (RegisterResponse) GeneratedMessageV3.parseWithIOException(PARSER, codedInputStream);
        }

        public static RegisterResponse parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (RegisterResponse) GeneratedMessageV3.parseWithIOException(PARSER, codedInputStream, extensionRegistryLite);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(RegisterResponse registerResponse) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(registerResponse);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Builder newBuilderForType(GeneratedMessageV3.BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements RegisterResponseOrBuilder {
            private Object accessKey_;
            private Object token_;

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return ClientSdkgate.internal_static_proto_sdkgate_RegisterResponse_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return ClientSdkgate.internal_static_proto_sdkgate_RegisterResponse_fieldAccessorTable.ensureFieldAccessorsInitialized(RegisterResponse.class, Builder.class);
            }

            private Builder() {
                this.token_ = "";
                this.accessKey_ = "";
                maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessageV3.BuilderParent builderParent) {
                super(builderParent);
                this.token_ = "";
                this.accessKey_ = "";
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                boolean unused = RegisterResponse.alwaysUseFieldBuilders;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                this.token_ = "";
                this.accessKey_ = "";
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return ClientSdkgate.internal_static_proto_sdkgate_RegisterResponse_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public RegisterResponse getDefaultInstanceForType() {
                return RegisterResponse.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public RegisterResponse build() {
                RegisterResponse registerResponseBuildPartial = buildPartial();
                if (registerResponseBuildPartial.isInitialized()) {
                    return registerResponseBuildPartial;
                }
                throw newUninitializedMessageException((Message) registerResponseBuildPartial);
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public RegisterResponse buildPartial() {
                RegisterResponse registerResponse = new RegisterResponse(this);
                registerResponse.token_ = this.token_;
                registerResponse.accessKey_ = this.accessKey_;
                onBuilt();
                return registerResponse;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder
            /* renamed from: clone */
            public Builder mo375clone() {
                return (Builder) super.mo375clone();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setField(Descriptors.FieldDescriptor fieldDescriptor, Object obj) {
                return (Builder) super.setField(fieldDescriptor, obj);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder clearField(Descriptors.FieldDescriptor fieldDescriptor) {
                return (Builder) super.clearField(fieldDescriptor);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder clearOneof(Descriptors.OneofDescriptor oneofDescriptor) {
                return (Builder) super.clearOneof(oneofDescriptor);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, int i, Object obj) {
                return (Builder) super.setRepeatedField(fieldDescriptor, i, obj);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder addRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, Object obj) {
                return (Builder) super.addRepeatedField(fieldDescriptor, obj);
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(Message message) {
                if (message instanceof RegisterResponse) {
                    return mergeFrom((RegisterResponse) message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder mergeFrom(RegisterResponse registerResponse) {
                if (registerResponse == RegisterResponse.getDefaultInstance()) {
                    return this;
                }
                if (!registerResponse.getToken().isEmpty()) {
                    this.token_ = registerResponse.token_;
                    onChanged();
                }
                if (!registerResponse.getAccessKey().isEmpty()) {
                    this.accessKey_ = registerResponse.accessKey_;
                    onChanged();
                }
                mergeUnknownFields(registerResponse.unknownFields);
                onChanged();
                return this;
            }

            /* JADX WARN: Removed duplicated region for block: B:16:0x0023  */
            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public com.netease.push.proto.nano.ClientSdkgate.RegisterResponse.Builder mergeFrom(com.google.protobuf.CodedInputStream r3, com.google.protobuf.ExtensionRegistryLite r4) throws java.lang.Throwable {
                /*
                    r2 = this;
                    r0 = 0
                    com.google.protobuf.Parser r1 = com.netease.push.proto.nano.ClientSdkgate.RegisterResponse.access$9000()     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    java.lang.Object r3 = r1.parsePartialFrom(r3, r4)     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    com.netease.push.proto.nano.ClientSdkgate$RegisterResponse r3 = (com.netease.push.proto.nano.ClientSdkgate.RegisterResponse) r3     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    if (r3 == 0) goto L10
                    r2.mergeFrom(r3)
                L10:
                    return r2
                L11:
                    r3 = move-exception
                    goto L21
                L13:
                    r3 = move-exception
                    com.google.protobuf.MessageLite r4 = r3.getUnfinishedMessage()     // Catch: java.lang.Throwable -> L11
                    com.netease.push.proto.nano.ClientSdkgate$RegisterResponse r4 = (com.netease.push.proto.nano.ClientSdkgate.RegisterResponse) r4     // Catch: java.lang.Throwable -> L11
                    java.io.IOException r3 = r3.unwrapIOException()     // Catch: java.lang.Throwable -> L1f
                    throw r3     // Catch: java.lang.Throwable -> L1f
                L1f:
                    r3 = move-exception
                    r0 = r4
                L21:
                    if (r0 == 0) goto L26
                    r2.mergeFrom(r0)
                L26:
                    throw r3
                */
                throw new UnsupportedOperationException("Method not decompiled: com.netease.push.proto.nano.ClientSdkgate.RegisterResponse.Builder.mergeFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):com.netease.push.proto.nano.ClientSdkgate$RegisterResponse$Builder");
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterResponseOrBuilder
            public String getToken() {
                Object obj = this.token_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.token_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterResponseOrBuilder
            public ByteString getTokenBytes() {
                Object obj = this.token_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.token_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setToken(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.token_ = str;
                onChanged();
                return this;
            }

            public Builder clearToken() {
                this.token_ = RegisterResponse.getDefaultInstance().getToken();
                onChanged();
                return this;
            }

            public Builder setTokenBytes(ByteString byteString) {
                if (byteString != null) {
                    RegisterResponse.checkByteStringIsUtf8(byteString);
                    this.token_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterResponseOrBuilder
            public String getAccessKey() {
                Object obj = this.accessKey_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.accessKey_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.RegisterResponseOrBuilder
            public ByteString getAccessKeyBytes() {
                Object obj = this.accessKey_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.accessKey_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setAccessKey(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.accessKey_ = str;
                onChanged();
                return this;
            }

            public Builder clearAccessKey() {
                this.accessKey_ = RegisterResponse.getDefaultInstance().getAccessKey();
                onChanged();
                return this;
            }

            public Builder setAccessKeyBytes(ByteString byteString) {
                if (byteString != null) {
                    RegisterResponse.checkByteStringIsUtf8(byteString);
                    this.accessKey_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public final Builder setUnknownFields(UnknownFieldSet unknownFieldSet) {
                return (Builder) super.setUnknownFields(unknownFieldSet);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public final Builder mergeUnknownFields(UnknownFieldSet unknownFieldSet) {
                return (Builder) super.mergeUnknownFields(unknownFieldSet);
            }
        }

        public static RegisterResponse getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<RegisterResponse> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<RegisterResponse> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public RegisterResponse getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    public static final class LoginRequest extends GeneratedMessageV3 implements LoginRequestOrBuilder {
        public static final int ACCESS_KEY_FIELD_NUMBER = 2;
        public static final int APP_VERSION_FIELD_NUMBER = 11;
        public static final int DEVICE_BRAND_FIELD_NUMBER = 15;
        public static final int DEVICE_MODEL_FIELD_NUMBER = 16;
        public static final int PERMIT_NOTICE_FIELD_NUMBER = 10;
        public static final int SDK_VERSION_FIELD_NUMBER = 12;
        public static final int SYSTEM_VERSION_FIELD_NUMBER = 13;
        public static final int TIME_ZONE_FIELD_NUMBER = 14;
        public static final int TOKEN_FIELD_NUMBER = 1;
        public static final int TRANSID_FIELD_NUMBER = 50;
        private static final long serialVersionUID = 0;
        private volatile Object accessKey_;
        private volatile Object appVersion_;
        private volatile Object deviceBrand_;
        private volatile Object deviceModel_;
        private byte memoizedIsInitialized;
        private boolean permitNotice_;
        private volatile Object sdkVersion_;
        private volatile Object systemVersion_;
        private volatile Object timeZone_;
        private volatile Object token_;
        private volatile Object transid_;
        private static final LoginRequest DEFAULT_INSTANCE = new LoginRequest();
        private static final Parser<LoginRequest> PARSER = new AbstractParser<LoginRequest>() { // from class: com.netease.push.proto.nano.ClientSdkgate.LoginRequest.1
            @Override // com.google.protobuf.Parser
            public LoginRequest parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return new LoginRequest(codedInputStream, extensionRegistryLite);
            }
        };

        private LoginRequest(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private LoginRequest() {
            this.memoizedIsInitialized = (byte) -1;
            this.token_ = "";
            this.accessKey_ = "";
            this.appVersion_ = "";
            this.sdkVersion_ = "";
            this.systemVersion_ = "";
            this.timeZone_ = "";
            this.deviceBrand_ = "";
            this.deviceModel_ = "";
            this.transid_ = "";
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private LoginRequest(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            this();
            if (extensionRegistryLite == null) {
                throw new NullPointerException();
            }
            UnknownFieldSet.Builder builderNewBuilder = UnknownFieldSet.newBuilder();
            boolean z = false;
            while (!z) {
                try {
                    try {
                        try {
                            int tag = codedInputStream.readTag();
                            switch (tag) {
                                case 0:
                                    z = true;
                                case 10:
                                    this.token_ = codedInputStream.readStringRequireUtf8();
                                case 18:
                                    this.accessKey_ = codedInputStream.readStringRequireUtf8();
                                case 80:
                                    this.permitNotice_ = codedInputStream.readBool();
                                case 90:
                                    this.appVersion_ = codedInputStream.readStringRequireUtf8();
                                case 98:
                                    this.sdkVersion_ = codedInputStream.readStringRequireUtf8();
                                case 106:
                                    this.systemVersion_ = codedInputStream.readStringRequireUtf8();
                                case 114:
                                    this.timeZone_ = codedInputStream.readStringRequireUtf8();
                                case 122:
                                    this.deviceBrand_ = codedInputStream.readStringRequireUtf8();
                                case 130:
                                    this.deviceModel_ = codedInputStream.readStringRequireUtf8();
                                case 402:
                                    this.transid_ = codedInputStream.readStringRequireUtf8();
                                default:
                                    if (!parseUnknownField(codedInputStream, builderNewBuilder, extensionRegistryLite, tag)) {
                                        z = true;
                                    }
                            }
                        } catch (IOException e) {
                            throw new InvalidProtocolBufferException(e).setUnfinishedMessage(this);
                        }
                    } catch (InvalidProtocolBufferException e2) {
                        throw e2.setUnfinishedMessage(this);
                    }
                } finally {
                    this.unknownFields = builderNewBuilder.build();
                    makeExtensionsImmutable();
                }
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return ClientSdkgate.internal_static_proto_sdkgate_LoginRequest_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return ClientSdkgate.internal_static_proto_sdkgate_LoginRequest_fieldAccessorTable.ensureFieldAccessorsInitialized(LoginRequest.class, Builder.class);
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
        public String getToken() {
            Object obj = this.token_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.token_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
        public ByteString getTokenBytes() {
            Object obj = this.token_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.token_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
        public String getAccessKey() {
            Object obj = this.accessKey_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.accessKey_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
        public ByteString getAccessKeyBytes() {
            Object obj = this.accessKey_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.accessKey_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
        public boolean getPermitNotice() {
            return this.permitNotice_;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
        public String getAppVersion() {
            Object obj = this.appVersion_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.appVersion_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
        public ByteString getAppVersionBytes() {
            Object obj = this.appVersion_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.appVersion_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
        public String getSdkVersion() {
            Object obj = this.sdkVersion_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.sdkVersion_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
        public ByteString getSdkVersionBytes() {
            Object obj = this.sdkVersion_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.sdkVersion_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
        public String getSystemVersion() {
            Object obj = this.systemVersion_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.systemVersion_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
        public ByteString getSystemVersionBytes() {
            Object obj = this.systemVersion_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.systemVersion_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
        public String getTimeZone() {
            Object obj = this.timeZone_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.timeZone_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
        public ByteString getTimeZoneBytes() {
            Object obj = this.timeZone_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.timeZone_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
        public String getDeviceBrand() {
            Object obj = this.deviceBrand_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.deviceBrand_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
        public ByteString getDeviceBrandBytes() {
            Object obj = this.deviceBrand_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.deviceBrand_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
        public String getDeviceModel() {
            Object obj = this.deviceModel_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.deviceModel_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
        public ByteString getDeviceModelBytes() {
            Object obj = this.deviceModel_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.deviceModel_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
        public String getTransid() {
            Object obj = this.transid_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.transid_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
        public ByteString getTransidBytes() {
            Object obj = this.transid_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.transid_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            if (b == 1) {
                return true;
            }
            if (b == 0) {
                return false;
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            if (!getTokenBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 1, this.token_);
            }
            if (!getAccessKeyBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 2, this.accessKey_);
            }
            boolean z = this.permitNotice_;
            if (z) {
                codedOutputStream.writeBool(10, z);
            }
            if (!getAppVersionBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 11, this.appVersion_);
            }
            if (!getSdkVersionBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 12, this.sdkVersion_);
            }
            if (!getSystemVersionBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 13, this.systemVersion_);
            }
            if (!getTimeZoneBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 14, this.timeZone_);
            }
            if (!getDeviceBrandBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 15, this.deviceBrand_);
            }
            if (!getDeviceModelBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 16, this.deviceModel_);
            }
            if (!getTransidBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 50, this.transid_);
            }
            this.unknownFields.writeTo(codedOutputStream);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public int getSerializedSize() {
            int i = this.memoizedSize;
            if (i != -1) {
                return i;
            }
            int iComputeStringSize = getTokenBytes().isEmpty() ? 0 : 0 + GeneratedMessageV3.computeStringSize(1, this.token_);
            if (!getAccessKeyBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(2, this.accessKey_);
            }
            boolean z = this.permitNotice_;
            if (z) {
                iComputeStringSize += CodedOutputStream.computeBoolSize(10, z);
            }
            if (!getAppVersionBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(11, this.appVersion_);
            }
            if (!getSdkVersionBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(12, this.sdkVersion_);
            }
            if (!getSystemVersionBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(13, this.systemVersion_);
            }
            if (!getTimeZoneBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(14, this.timeZone_);
            }
            if (!getDeviceBrandBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(15, this.deviceBrand_);
            }
            if (!getDeviceModelBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(16, this.deviceModel_);
            }
            if (!getTransidBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(50, this.transid_);
            }
            int serializedSize = iComputeStringSize + this.unknownFields.getSerializedSize();
            this.memoizedSize = serializedSize;
            return serializedSize;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof LoginRequest)) {
                return super.equals(obj);
            }
            LoginRequest loginRequest = (LoginRequest) obj;
            return getToken().equals(loginRequest.getToken()) && getAccessKey().equals(loginRequest.getAccessKey()) && getPermitNotice() == loginRequest.getPermitNotice() && getAppVersion().equals(loginRequest.getAppVersion()) && getSdkVersion().equals(loginRequest.getSdkVersion()) && getSystemVersion().equals(loginRequest.getSystemVersion()) && getTimeZone().equals(loginRequest.getTimeZone()) && getDeviceBrand().equals(loginRequest.getDeviceBrand()) && getDeviceModel().equals(loginRequest.getDeviceModel()) && getTransid().equals(loginRequest.getTransid()) && this.unknownFields.equals(loginRequest.unknownFields);
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int iHashCode = ((((((((((((((((((((((((((((((((((((((((((779 + getDescriptor().hashCode()) * 37) + 1) * 53) + getToken().hashCode()) * 37) + 2) * 53) + getAccessKey().hashCode()) * 37) + 10) * 53) + Internal.hashBoolean(getPermitNotice())) * 37) + 11) * 53) + getAppVersion().hashCode()) * 37) + 12) * 53) + getSdkVersion().hashCode()) * 37) + 13) * 53) + getSystemVersion().hashCode()) * 37) + 14) * 53) + getTimeZone().hashCode()) * 37) + 15) * 53) + getDeviceBrand().hashCode()) * 37) + 16) * 53) + getDeviceModel().hashCode()) * 37) + 50) * 53) + getTransid().hashCode()) * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = iHashCode;
            return iHashCode;
        }

        public static LoginRequest parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString);
        }

        public static LoginRequest parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString, extensionRegistryLite);
        }

        public static LoginRequest parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(bArr);
        }

        public static LoginRequest parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(bArr, extensionRegistryLite);
        }

        public static LoginRequest parseFrom(InputStream inputStream) throws IOException {
            return (LoginRequest) GeneratedMessageV3.parseWithIOException(PARSER, inputStream);
        }

        public static LoginRequest parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (LoginRequest) GeneratedMessageV3.parseWithIOException(PARSER, inputStream, extensionRegistryLite);
        }

        public static LoginRequest parseDelimitedFrom(InputStream inputStream) throws IOException {
            return (LoginRequest) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, inputStream);
        }

        public static LoginRequest parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (LoginRequest) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, inputStream, extensionRegistryLite);
        }

        public static LoginRequest parseFrom(CodedInputStream codedInputStream) throws IOException {
            return (LoginRequest) GeneratedMessageV3.parseWithIOException(PARSER, codedInputStream);
        }

        public static LoginRequest parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (LoginRequest) GeneratedMessageV3.parseWithIOException(PARSER, codedInputStream, extensionRegistryLite);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(LoginRequest loginRequest) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(loginRequest);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Builder newBuilderForType(GeneratedMessageV3.BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements LoginRequestOrBuilder {
            private Object accessKey_;
            private Object appVersion_;
            private Object deviceBrand_;
            private Object deviceModel_;
            private boolean permitNotice_;
            private Object sdkVersion_;
            private Object systemVersion_;
            private Object timeZone_;
            private Object token_;
            private Object transid_;

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return ClientSdkgate.internal_static_proto_sdkgate_LoginRequest_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return ClientSdkgate.internal_static_proto_sdkgate_LoginRequest_fieldAccessorTable.ensureFieldAccessorsInitialized(LoginRequest.class, Builder.class);
            }

            private Builder() {
                this.token_ = "";
                this.accessKey_ = "";
                this.appVersion_ = "";
                this.sdkVersion_ = "";
                this.systemVersion_ = "";
                this.timeZone_ = "";
                this.deviceBrand_ = "";
                this.deviceModel_ = "";
                this.transid_ = "";
                maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessageV3.BuilderParent builderParent) {
                super(builderParent);
                this.token_ = "";
                this.accessKey_ = "";
                this.appVersion_ = "";
                this.sdkVersion_ = "";
                this.systemVersion_ = "";
                this.timeZone_ = "";
                this.deviceBrand_ = "";
                this.deviceModel_ = "";
                this.transid_ = "";
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                boolean unused = LoginRequest.alwaysUseFieldBuilders;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                this.token_ = "";
                this.accessKey_ = "";
                this.permitNotice_ = false;
                this.appVersion_ = "";
                this.sdkVersion_ = "";
                this.systemVersion_ = "";
                this.timeZone_ = "";
                this.deviceBrand_ = "";
                this.deviceModel_ = "";
                this.transid_ = "";
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return ClientSdkgate.internal_static_proto_sdkgate_LoginRequest_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public LoginRequest getDefaultInstanceForType() {
                return LoginRequest.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public LoginRequest build() {
                LoginRequest loginRequestBuildPartial = buildPartial();
                if (loginRequestBuildPartial.isInitialized()) {
                    return loginRequestBuildPartial;
                }
                throw newUninitializedMessageException((Message) loginRequestBuildPartial);
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public LoginRequest buildPartial() {
                LoginRequest loginRequest = new LoginRequest(this);
                loginRequest.token_ = this.token_;
                loginRequest.accessKey_ = this.accessKey_;
                loginRequest.permitNotice_ = this.permitNotice_;
                loginRequest.appVersion_ = this.appVersion_;
                loginRequest.sdkVersion_ = this.sdkVersion_;
                loginRequest.systemVersion_ = this.systemVersion_;
                loginRequest.timeZone_ = this.timeZone_;
                loginRequest.deviceBrand_ = this.deviceBrand_;
                loginRequest.deviceModel_ = this.deviceModel_;
                loginRequest.transid_ = this.transid_;
                onBuilt();
                return loginRequest;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder
            /* renamed from: clone */
            public Builder mo375clone() {
                return (Builder) super.mo375clone();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setField(Descriptors.FieldDescriptor fieldDescriptor, Object obj) {
                return (Builder) super.setField(fieldDescriptor, obj);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder clearField(Descriptors.FieldDescriptor fieldDescriptor) {
                return (Builder) super.clearField(fieldDescriptor);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder clearOneof(Descriptors.OneofDescriptor oneofDescriptor) {
                return (Builder) super.clearOneof(oneofDescriptor);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, int i, Object obj) {
                return (Builder) super.setRepeatedField(fieldDescriptor, i, obj);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder addRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, Object obj) {
                return (Builder) super.addRepeatedField(fieldDescriptor, obj);
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(Message message) {
                if (message instanceof LoginRequest) {
                    return mergeFrom((LoginRequest) message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder mergeFrom(LoginRequest loginRequest) {
                if (loginRequest == LoginRequest.getDefaultInstance()) {
                    return this;
                }
                if (!loginRequest.getToken().isEmpty()) {
                    this.token_ = loginRequest.token_;
                    onChanged();
                }
                if (!loginRequest.getAccessKey().isEmpty()) {
                    this.accessKey_ = loginRequest.accessKey_;
                    onChanged();
                }
                if (loginRequest.getPermitNotice()) {
                    setPermitNotice(loginRequest.getPermitNotice());
                }
                if (!loginRequest.getAppVersion().isEmpty()) {
                    this.appVersion_ = loginRequest.appVersion_;
                    onChanged();
                }
                if (!loginRequest.getSdkVersion().isEmpty()) {
                    this.sdkVersion_ = loginRequest.sdkVersion_;
                    onChanged();
                }
                if (!loginRequest.getSystemVersion().isEmpty()) {
                    this.systemVersion_ = loginRequest.systemVersion_;
                    onChanged();
                }
                if (!loginRequest.getTimeZone().isEmpty()) {
                    this.timeZone_ = loginRequest.timeZone_;
                    onChanged();
                }
                if (!loginRequest.getDeviceBrand().isEmpty()) {
                    this.deviceBrand_ = loginRequest.deviceBrand_;
                    onChanged();
                }
                if (!loginRequest.getDeviceModel().isEmpty()) {
                    this.deviceModel_ = loginRequest.deviceModel_;
                    onChanged();
                }
                if (!loginRequest.getTransid().isEmpty()) {
                    this.transid_ = loginRequest.transid_;
                    onChanged();
                }
                mergeUnknownFields(loginRequest.unknownFields);
                onChanged();
                return this;
            }

            /* JADX WARN: Removed duplicated region for block: B:16:0x0023  */
            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public com.netease.push.proto.nano.ClientSdkgate.LoginRequest.Builder mergeFrom(com.google.protobuf.CodedInputStream r3, com.google.protobuf.ExtensionRegistryLite r4) throws java.lang.Throwable {
                /*
                    r2 = this;
                    r0 = 0
                    com.google.protobuf.Parser r1 = com.netease.push.proto.nano.ClientSdkgate.LoginRequest.access$11100()     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    java.lang.Object r3 = r1.parsePartialFrom(r3, r4)     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    com.netease.push.proto.nano.ClientSdkgate$LoginRequest r3 = (com.netease.push.proto.nano.ClientSdkgate.LoginRequest) r3     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    if (r3 == 0) goto L10
                    r2.mergeFrom(r3)
                L10:
                    return r2
                L11:
                    r3 = move-exception
                    goto L21
                L13:
                    r3 = move-exception
                    com.google.protobuf.MessageLite r4 = r3.getUnfinishedMessage()     // Catch: java.lang.Throwable -> L11
                    com.netease.push.proto.nano.ClientSdkgate$LoginRequest r4 = (com.netease.push.proto.nano.ClientSdkgate.LoginRequest) r4     // Catch: java.lang.Throwable -> L11
                    java.io.IOException r3 = r3.unwrapIOException()     // Catch: java.lang.Throwable -> L1f
                    throw r3     // Catch: java.lang.Throwable -> L1f
                L1f:
                    r3 = move-exception
                    r0 = r4
                L21:
                    if (r0 == 0) goto L26
                    r2.mergeFrom(r0)
                L26:
                    throw r3
                */
                throw new UnsupportedOperationException("Method not decompiled: com.netease.push.proto.nano.ClientSdkgate.LoginRequest.Builder.mergeFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):com.netease.push.proto.nano.ClientSdkgate$LoginRequest$Builder");
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
            public String getToken() {
                Object obj = this.token_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.token_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
            public ByteString getTokenBytes() {
                Object obj = this.token_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.token_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setToken(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.token_ = str;
                onChanged();
                return this;
            }

            public Builder clearToken() {
                this.token_ = LoginRequest.getDefaultInstance().getToken();
                onChanged();
                return this;
            }

            public Builder setTokenBytes(ByteString byteString) {
                if (byteString != null) {
                    LoginRequest.checkByteStringIsUtf8(byteString);
                    this.token_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
            public String getAccessKey() {
                Object obj = this.accessKey_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.accessKey_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
            public ByteString getAccessKeyBytes() {
                Object obj = this.accessKey_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.accessKey_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setAccessKey(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.accessKey_ = str;
                onChanged();
                return this;
            }

            public Builder clearAccessKey() {
                this.accessKey_ = LoginRequest.getDefaultInstance().getAccessKey();
                onChanged();
                return this;
            }

            public Builder setAccessKeyBytes(ByteString byteString) {
                if (byteString != null) {
                    LoginRequest.checkByteStringIsUtf8(byteString);
                    this.accessKey_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
            public boolean getPermitNotice() {
                return this.permitNotice_;
            }

            public Builder setPermitNotice(boolean z) {
                this.permitNotice_ = z;
                onChanged();
                return this;
            }

            public Builder clearPermitNotice() {
                this.permitNotice_ = false;
                onChanged();
                return this;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
            public String getAppVersion() {
                Object obj = this.appVersion_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.appVersion_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
            public ByteString getAppVersionBytes() {
                Object obj = this.appVersion_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.appVersion_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setAppVersion(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.appVersion_ = str;
                onChanged();
                return this;
            }

            public Builder clearAppVersion() {
                this.appVersion_ = LoginRequest.getDefaultInstance().getAppVersion();
                onChanged();
                return this;
            }

            public Builder setAppVersionBytes(ByteString byteString) {
                if (byteString != null) {
                    LoginRequest.checkByteStringIsUtf8(byteString);
                    this.appVersion_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
            public String getSdkVersion() {
                Object obj = this.sdkVersion_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.sdkVersion_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
            public ByteString getSdkVersionBytes() {
                Object obj = this.sdkVersion_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.sdkVersion_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setSdkVersion(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.sdkVersion_ = str;
                onChanged();
                return this;
            }

            public Builder clearSdkVersion() {
                this.sdkVersion_ = LoginRequest.getDefaultInstance().getSdkVersion();
                onChanged();
                return this;
            }

            public Builder setSdkVersionBytes(ByteString byteString) {
                if (byteString != null) {
                    LoginRequest.checkByteStringIsUtf8(byteString);
                    this.sdkVersion_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
            public String getSystemVersion() {
                Object obj = this.systemVersion_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.systemVersion_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
            public ByteString getSystemVersionBytes() {
                Object obj = this.systemVersion_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.systemVersion_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setSystemVersion(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.systemVersion_ = str;
                onChanged();
                return this;
            }

            public Builder clearSystemVersion() {
                this.systemVersion_ = LoginRequest.getDefaultInstance().getSystemVersion();
                onChanged();
                return this;
            }

            public Builder setSystemVersionBytes(ByteString byteString) {
                if (byteString != null) {
                    LoginRequest.checkByteStringIsUtf8(byteString);
                    this.systemVersion_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
            public String getTimeZone() {
                Object obj = this.timeZone_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.timeZone_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
            public ByteString getTimeZoneBytes() {
                Object obj = this.timeZone_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.timeZone_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setTimeZone(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.timeZone_ = str;
                onChanged();
                return this;
            }

            public Builder clearTimeZone() {
                this.timeZone_ = LoginRequest.getDefaultInstance().getTimeZone();
                onChanged();
                return this;
            }

            public Builder setTimeZoneBytes(ByteString byteString) {
                if (byteString != null) {
                    LoginRequest.checkByteStringIsUtf8(byteString);
                    this.timeZone_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
            public String getDeviceBrand() {
                Object obj = this.deviceBrand_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.deviceBrand_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
            public ByteString getDeviceBrandBytes() {
                Object obj = this.deviceBrand_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.deviceBrand_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setDeviceBrand(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.deviceBrand_ = str;
                onChanged();
                return this;
            }

            public Builder clearDeviceBrand() {
                this.deviceBrand_ = LoginRequest.getDefaultInstance().getDeviceBrand();
                onChanged();
                return this;
            }

            public Builder setDeviceBrandBytes(ByteString byteString) {
                if (byteString != null) {
                    LoginRequest.checkByteStringIsUtf8(byteString);
                    this.deviceBrand_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
            public String getDeviceModel() {
                Object obj = this.deviceModel_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.deviceModel_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
            public ByteString getDeviceModelBytes() {
                Object obj = this.deviceModel_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.deviceModel_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setDeviceModel(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.deviceModel_ = str;
                onChanged();
                return this;
            }

            public Builder clearDeviceModel() {
                this.deviceModel_ = LoginRequest.getDefaultInstance().getDeviceModel();
                onChanged();
                return this;
            }

            public Builder setDeviceModelBytes(ByteString byteString) {
                if (byteString != null) {
                    LoginRequest.checkByteStringIsUtf8(byteString);
                    this.deviceModel_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
            public String getTransid() {
                Object obj = this.transid_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.transid_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.LoginRequestOrBuilder
            public ByteString getTransidBytes() {
                Object obj = this.transid_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.transid_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setTransid(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.transid_ = str;
                onChanged();
                return this;
            }

            public Builder clearTransid() {
                this.transid_ = LoginRequest.getDefaultInstance().getTransid();
                onChanged();
                return this;
            }

            public Builder setTransidBytes(ByteString byteString) {
                if (byteString != null) {
                    LoginRequest.checkByteStringIsUtf8(byteString);
                    this.transid_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public final Builder setUnknownFields(UnknownFieldSet unknownFieldSet) {
                return (Builder) super.setUnknownFields(unknownFieldSet);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public final Builder mergeUnknownFields(UnknownFieldSet unknownFieldSet) {
                return (Builder) super.mergeUnknownFields(unknownFieldSet);
            }
        }

        public static LoginRequest getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<LoginRequest> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<LoginRequest> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public LoginRequest getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    public static final class HeartbeatRequest extends GeneratedMessageV3 implements HeartbeatRequestOrBuilder {
        private static final HeartbeatRequest DEFAULT_INSTANCE = new HeartbeatRequest();
        private static final Parser<HeartbeatRequest> PARSER = new AbstractParser<HeartbeatRequest>() { // from class: com.netease.push.proto.nano.ClientSdkgate.HeartbeatRequest.1
            @Override // com.google.protobuf.Parser
            public HeartbeatRequest parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return new HeartbeatRequest(codedInputStream, extensionRegistryLite);
            }
        };
        private static final long serialVersionUID = 0;
        private byte memoizedIsInitialized;

        private HeartbeatRequest(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private HeartbeatRequest() {
            this.memoizedIsInitialized = (byte) -1;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private HeartbeatRequest(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            this();
            if (extensionRegistryLite == null) {
                throw new NullPointerException();
            }
            UnknownFieldSet.Builder builderNewBuilder = UnknownFieldSet.newBuilder();
            boolean z = false;
            while (!z) {
                try {
                    try {
                        int tag = codedInputStream.readTag();
                        if (tag == 0 || !parseUnknownField(codedInputStream, builderNewBuilder, extensionRegistryLite, tag)) {
                            z = true;
                        }
                    } catch (InvalidProtocolBufferException e) {
                        throw e.setUnfinishedMessage(this);
                    } catch (IOException e2) {
                        throw new InvalidProtocolBufferException(e2).setUnfinishedMessage(this);
                    }
                } finally {
                    this.unknownFields = builderNewBuilder.build();
                    makeExtensionsImmutable();
                }
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return ClientSdkgate.internal_static_proto_sdkgate_HeartbeatRequest_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return ClientSdkgate.internal_static_proto_sdkgate_HeartbeatRequest_fieldAccessorTable.ensureFieldAccessorsInitialized(HeartbeatRequest.class, Builder.class);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            if (b == 1) {
                return true;
            }
            if (b == 0) {
                return false;
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            this.unknownFields.writeTo(codedOutputStream);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public int getSerializedSize() {
            int i = this.memoizedSize;
            if (i != -1) {
                return i;
            }
            int serializedSize = this.unknownFields.getSerializedSize() + 0;
            this.memoizedSize = serializedSize;
            return serializedSize;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof HeartbeatRequest) {
                return this.unknownFields.equals(((HeartbeatRequest) obj).unknownFields);
            }
            return super.equals(obj);
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int iHashCode = ((779 + getDescriptor().hashCode()) * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = iHashCode;
            return iHashCode;
        }

        public static HeartbeatRequest parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString);
        }

        public static HeartbeatRequest parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString, extensionRegistryLite);
        }

        public static HeartbeatRequest parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(bArr);
        }

        public static HeartbeatRequest parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(bArr, extensionRegistryLite);
        }

        public static HeartbeatRequest parseFrom(InputStream inputStream) throws IOException {
            return (HeartbeatRequest) GeneratedMessageV3.parseWithIOException(PARSER, inputStream);
        }

        public static HeartbeatRequest parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (HeartbeatRequest) GeneratedMessageV3.parseWithIOException(PARSER, inputStream, extensionRegistryLite);
        }

        public static HeartbeatRequest parseDelimitedFrom(InputStream inputStream) throws IOException {
            return (HeartbeatRequest) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, inputStream);
        }

        public static HeartbeatRequest parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (HeartbeatRequest) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, inputStream, extensionRegistryLite);
        }

        public static HeartbeatRequest parseFrom(CodedInputStream codedInputStream) throws IOException {
            return (HeartbeatRequest) GeneratedMessageV3.parseWithIOException(PARSER, codedInputStream);
        }

        public static HeartbeatRequest parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (HeartbeatRequest) GeneratedMessageV3.parseWithIOException(PARSER, codedInputStream, extensionRegistryLite);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(HeartbeatRequest heartbeatRequest) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(heartbeatRequest);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Builder newBuilderForType(GeneratedMessageV3.BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements HeartbeatRequestOrBuilder {
            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return ClientSdkgate.internal_static_proto_sdkgate_HeartbeatRequest_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return ClientSdkgate.internal_static_proto_sdkgate_HeartbeatRequest_fieldAccessorTable.ensureFieldAccessorsInitialized(HeartbeatRequest.class, Builder.class);
            }

            private Builder() {
                maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessageV3.BuilderParent builderParent) {
                super(builderParent);
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                boolean unused = HeartbeatRequest.alwaysUseFieldBuilders;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return ClientSdkgate.internal_static_proto_sdkgate_HeartbeatRequest_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public HeartbeatRequest getDefaultInstanceForType() {
                return HeartbeatRequest.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public HeartbeatRequest build() {
                HeartbeatRequest heartbeatRequestBuildPartial = buildPartial();
                if (heartbeatRequestBuildPartial.isInitialized()) {
                    return heartbeatRequestBuildPartial;
                }
                throw newUninitializedMessageException((Message) heartbeatRequestBuildPartial);
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public HeartbeatRequest buildPartial() {
                HeartbeatRequest heartbeatRequest = new HeartbeatRequest(this);
                onBuilt();
                return heartbeatRequest;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder
            /* renamed from: clone */
            public Builder mo375clone() {
                return (Builder) super.mo375clone();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setField(Descriptors.FieldDescriptor fieldDescriptor, Object obj) {
                return (Builder) super.setField(fieldDescriptor, obj);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder clearField(Descriptors.FieldDescriptor fieldDescriptor) {
                return (Builder) super.clearField(fieldDescriptor);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder clearOneof(Descriptors.OneofDescriptor oneofDescriptor) {
                return (Builder) super.clearOneof(oneofDescriptor);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, int i, Object obj) {
                return (Builder) super.setRepeatedField(fieldDescriptor, i, obj);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder addRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, Object obj) {
                return (Builder) super.addRepeatedField(fieldDescriptor, obj);
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(Message message) {
                if (message instanceof HeartbeatRequest) {
                    return mergeFrom((HeartbeatRequest) message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder mergeFrom(HeartbeatRequest heartbeatRequest) {
                if (heartbeatRequest == HeartbeatRequest.getDefaultInstance()) {
                    return this;
                }
                mergeUnknownFields(heartbeatRequest.unknownFields);
                onChanged();
                return this;
            }

            /* JADX WARN: Removed duplicated region for block: B:16:0x0023  */
            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public com.netease.push.proto.nano.ClientSdkgate.HeartbeatRequest.Builder mergeFrom(com.google.protobuf.CodedInputStream r3, com.google.protobuf.ExtensionRegistryLite r4) throws java.lang.Throwable {
                /*
                    r2 = this;
                    r0 = 0
                    com.google.protobuf.Parser r1 = com.netease.push.proto.nano.ClientSdkgate.HeartbeatRequest.access$12900()     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    java.lang.Object r3 = r1.parsePartialFrom(r3, r4)     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    com.netease.push.proto.nano.ClientSdkgate$HeartbeatRequest r3 = (com.netease.push.proto.nano.ClientSdkgate.HeartbeatRequest) r3     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    if (r3 == 0) goto L10
                    r2.mergeFrom(r3)
                L10:
                    return r2
                L11:
                    r3 = move-exception
                    goto L21
                L13:
                    r3 = move-exception
                    com.google.protobuf.MessageLite r4 = r3.getUnfinishedMessage()     // Catch: java.lang.Throwable -> L11
                    com.netease.push.proto.nano.ClientSdkgate$HeartbeatRequest r4 = (com.netease.push.proto.nano.ClientSdkgate.HeartbeatRequest) r4     // Catch: java.lang.Throwable -> L11
                    java.io.IOException r3 = r3.unwrapIOException()     // Catch: java.lang.Throwable -> L1f
                    throw r3     // Catch: java.lang.Throwable -> L1f
                L1f:
                    r3 = move-exception
                    r0 = r4
                L21:
                    if (r0 == 0) goto L26
                    r2.mergeFrom(r0)
                L26:
                    throw r3
                */
                throw new UnsupportedOperationException("Method not decompiled: com.netease.push.proto.nano.ClientSdkgate.HeartbeatRequest.Builder.mergeFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):com.netease.push.proto.nano.ClientSdkgate$HeartbeatRequest$Builder");
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public final Builder setUnknownFields(UnknownFieldSet unknownFieldSet) {
                return (Builder) super.setUnknownFields(unknownFieldSet);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public final Builder mergeUnknownFields(UnknownFieldSet unknownFieldSet) {
                return (Builder) super.mergeUnknownFields(unknownFieldSet);
            }
        }

        public static HeartbeatRequest getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<HeartbeatRequest> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<HeartbeatRequest> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public HeartbeatRequest getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    public static final class Notification extends GeneratedMessageV3 implements NotificationOrBuilder {
        public static final int ANDROID_FIELD_NUMBER = 51;
        public static final int APNS_FIELD_NUMBER = 50;
        public static final int CONTENT_FIELD_NUMBER = 3;
        public static final int FEATURE_CONTENT_FIELD_NUMBER = 102;
        public static final int FEATURE_SUB_TITLE_FIELD_NUMBER = 101;
        public static final int FEATURE_TITLE_FIELD_NUMBER = 100;
        public static final int SILENT_FIELD_NUMBER = 4;
        public static final int SUB_TITLE_FIELD_NUMBER = 2;
        public static final int SYSTEM_CONTENT_FIELD_NUMBER = 80;
        public static final int TITLE_FIELD_NUMBER = 1;
        private static final long serialVersionUID = 0;
        private Android android_;
        private APNS apns_;
        private volatile Object content_;
        private volatile Object featureContent_;
        private volatile Object featureSubTitle_;
        private volatile Object featureTitle_;
        private byte memoizedIsInitialized;
        private boolean silent_;
        private volatile Object subTitle_;
        private SystemContent systemContent_;
        private volatile Object title_;
        private static final Notification DEFAULT_INSTANCE = new Notification();
        private static final Parser<Notification> PARSER = new AbstractParser<Notification>() { // from class: com.netease.push.proto.nano.ClientSdkgate.Notification.1
            @Override // com.google.protobuf.Parser
            public Notification parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return new Notification(codedInputStream, extensionRegistryLite);
            }
        };

        private Notification(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private Notification() {
            this.memoizedIsInitialized = (byte) -1;
            this.title_ = "";
            this.subTitle_ = "";
            this.content_ = "";
            this.featureTitle_ = "";
            this.featureSubTitle_ = "";
            this.featureContent_ = "";
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private Notification(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            this();
            if (extensionRegistryLite == null) {
                throw new NullPointerException();
            }
            UnknownFieldSet.Builder builderNewBuilder = UnknownFieldSet.newBuilder();
            boolean z = false;
            while (!z) {
                try {
                    try {
                        try {
                            int tag = codedInputStream.readTag();
                            switch (tag) {
                                case 0:
                                    z = true;
                                case 10:
                                    this.title_ = codedInputStream.readStringRequireUtf8();
                                case 18:
                                    this.subTitle_ = codedInputStream.readStringRequireUtf8();
                                case 26:
                                    this.content_ = codedInputStream.readStringRequireUtf8();
                                case 32:
                                    this.silent_ = codedInputStream.readBool();
                                case 402:
                                    APNS.Builder builder = this.apns_ != null ? this.apns_.toBuilder() : null;
                                    this.apns_ = (APNS) codedInputStream.readMessage(APNS.parser(), extensionRegistryLite);
                                    if (builder != null) {
                                        builder.mergeFrom(this.apns_);
                                        this.apns_ = builder.buildPartial();
                                    }
                                case 410:
                                    Android.Builder builder2 = this.android_ != null ? this.android_.toBuilder() : null;
                                    this.android_ = (Android) codedInputStream.readMessage(Android.parser(), extensionRegistryLite);
                                    if (builder2 != null) {
                                        builder2.mergeFrom(this.android_);
                                        this.android_ = builder2.buildPartial();
                                    }
                                case 642:
                                    SystemContent.Builder builder3 = this.systemContent_ != null ? this.systemContent_.toBuilder() : null;
                                    this.systemContent_ = (SystemContent) codedInputStream.readMessage(SystemContent.parser(), extensionRegistryLite);
                                    if (builder3 != null) {
                                        builder3.mergeFrom(this.systemContent_);
                                        this.systemContent_ = builder3.buildPartial();
                                    }
                                case 802:
                                    this.featureTitle_ = codedInputStream.readStringRequireUtf8();
                                case 810:
                                    this.featureSubTitle_ = codedInputStream.readStringRequireUtf8();
                                case 818:
                                    this.featureContent_ = codedInputStream.readStringRequireUtf8();
                                default:
                                    if (!parseUnknownField(codedInputStream, builderNewBuilder, extensionRegistryLite, tag)) {
                                        z = true;
                                    }
                            }
                        } catch (InvalidProtocolBufferException e) {
                            throw e.setUnfinishedMessage(this);
                        }
                    } catch (IOException e2) {
                        throw new InvalidProtocolBufferException(e2).setUnfinishedMessage(this);
                    }
                } finally {
                    this.unknownFields = builderNewBuilder.build();
                    makeExtensionsImmutable();
                }
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return ClientSdkgate.internal_static_proto_sdkgate_Notification_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return ClientSdkgate.internal_static_proto_sdkgate_Notification_fieldAccessorTable.ensureFieldAccessorsInitialized(Notification.class, Builder.class);
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
        public String getTitle() {
            Object obj = this.title_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.title_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
        public ByteString getTitleBytes() {
            Object obj = this.title_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.title_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
        public String getSubTitle() {
            Object obj = this.subTitle_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.subTitle_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
        public ByteString getSubTitleBytes() {
            Object obj = this.subTitle_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.subTitle_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
        public String getContent() {
            Object obj = this.content_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.content_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
        public ByteString getContentBytes() {
            Object obj = this.content_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.content_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
        public boolean getSilent() {
            return this.silent_;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
        public boolean hasApns() {
            return this.apns_ != null;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
        public APNS getApns() {
            APNS apns = this.apns_;
            return apns == null ? APNS.getDefaultInstance() : apns;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
        public APNSOrBuilder getApnsOrBuilder() {
            return getApns();
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
        public boolean hasAndroid() {
            return this.android_ != null;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
        public Android getAndroid() {
            Android android2 = this.android_;
            return android2 == null ? Android.getDefaultInstance() : android2;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
        public AndroidOrBuilder getAndroidOrBuilder() {
            return getAndroid();
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
        public boolean hasSystemContent() {
            return this.systemContent_ != null;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
        public SystemContent getSystemContent() {
            SystemContent systemContent = this.systemContent_;
            return systemContent == null ? SystemContent.getDefaultInstance() : systemContent;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
        public SystemContentOrBuilder getSystemContentOrBuilder() {
            return getSystemContent();
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
        public String getFeatureTitle() {
            Object obj = this.featureTitle_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.featureTitle_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
        public ByteString getFeatureTitleBytes() {
            Object obj = this.featureTitle_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.featureTitle_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
        public String getFeatureSubTitle() {
            Object obj = this.featureSubTitle_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.featureSubTitle_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
        public ByteString getFeatureSubTitleBytes() {
            Object obj = this.featureSubTitle_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.featureSubTitle_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
        public String getFeatureContent() {
            Object obj = this.featureContent_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.featureContent_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
        public ByteString getFeatureContentBytes() {
            Object obj = this.featureContent_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.featureContent_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            if (b == 1) {
                return true;
            }
            if (b == 0) {
                return false;
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            if (!getTitleBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 1, this.title_);
            }
            if (!getSubTitleBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 2, this.subTitle_);
            }
            if (!getContentBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 3, this.content_);
            }
            boolean z = this.silent_;
            if (z) {
                codedOutputStream.writeBool(4, z);
            }
            if (this.apns_ != null) {
                codedOutputStream.writeMessage(50, getApns());
            }
            if (this.android_ != null) {
                codedOutputStream.writeMessage(51, getAndroid());
            }
            if (this.systemContent_ != null) {
                codedOutputStream.writeMessage(80, getSystemContent());
            }
            if (!getFeatureTitleBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 100, this.featureTitle_);
            }
            if (!getFeatureSubTitleBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 101, this.featureSubTitle_);
            }
            if (!getFeatureContentBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 102, this.featureContent_);
            }
            this.unknownFields.writeTo(codedOutputStream);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public int getSerializedSize() {
            int i = this.memoizedSize;
            if (i != -1) {
                return i;
            }
            int iComputeStringSize = getTitleBytes().isEmpty() ? 0 : 0 + GeneratedMessageV3.computeStringSize(1, this.title_);
            if (!getSubTitleBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(2, this.subTitle_);
            }
            if (!getContentBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(3, this.content_);
            }
            boolean z = this.silent_;
            if (z) {
                iComputeStringSize += CodedOutputStream.computeBoolSize(4, z);
            }
            if (this.apns_ != null) {
                iComputeStringSize += CodedOutputStream.computeMessageSize(50, getApns());
            }
            if (this.android_ != null) {
                iComputeStringSize += CodedOutputStream.computeMessageSize(51, getAndroid());
            }
            if (this.systemContent_ != null) {
                iComputeStringSize += CodedOutputStream.computeMessageSize(80, getSystemContent());
            }
            if (!getFeatureTitleBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(100, this.featureTitle_);
            }
            if (!getFeatureSubTitleBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(101, this.featureSubTitle_);
            }
            if (!getFeatureContentBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(102, this.featureContent_);
            }
            int serializedSize = iComputeStringSize + this.unknownFields.getSerializedSize();
            this.memoizedSize = serializedSize;
            return serializedSize;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Notification)) {
                return super.equals(obj);
            }
            Notification notification = (Notification) obj;
            if (!getTitle().equals(notification.getTitle()) || !getSubTitle().equals(notification.getSubTitle()) || !getContent().equals(notification.getContent()) || getSilent() != notification.getSilent() || hasApns() != notification.hasApns()) {
                return false;
            }
            if ((hasApns() && !getApns().equals(notification.getApns())) || hasAndroid() != notification.hasAndroid()) {
                return false;
            }
            if ((!hasAndroid() || getAndroid().equals(notification.getAndroid())) && hasSystemContent() == notification.hasSystemContent()) {
                return (!hasSystemContent() || getSystemContent().equals(notification.getSystemContent())) && getFeatureTitle().equals(notification.getFeatureTitle()) && getFeatureSubTitle().equals(notification.getFeatureSubTitle()) && getFeatureContent().equals(notification.getFeatureContent()) && this.unknownFields.equals(notification.unknownFields);
            }
            return false;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int iHashCode = ((((((((((((((((779 + getDescriptor().hashCode()) * 37) + 1) * 53) + getTitle().hashCode()) * 37) + 2) * 53) + getSubTitle().hashCode()) * 37) + 3) * 53) + getContent().hashCode()) * 37) + 4) * 53) + Internal.hashBoolean(getSilent());
            if (hasApns()) {
                iHashCode = (((iHashCode * 37) + 50) * 53) + getApns().hashCode();
            }
            if (hasAndroid()) {
                iHashCode = (((iHashCode * 37) + 51) * 53) + getAndroid().hashCode();
            }
            if (hasSystemContent()) {
                iHashCode = (((iHashCode * 37) + 80) * 53) + getSystemContent().hashCode();
            }
            int iHashCode2 = (((((((((((((iHashCode * 37) + 100) * 53) + getFeatureTitle().hashCode()) * 37) + 101) * 53) + getFeatureSubTitle().hashCode()) * 37) + 102) * 53) + getFeatureContent().hashCode()) * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = iHashCode2;
            return iHashCode2;
        }

        public static Notification parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString);
        }

        public static Notification parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString, extensionRegistryLite);
        }

        public static Notification parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(bArr);
        }

        public static Notification parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(bArr, extensionRegistryLite);
        }

        public static Notification parseFrom(InputStream inputStream) throws IOException {
            return (Notification) GeneratedMessageV3.parseWithIOException(PARSER, inputStream);
        }

        public static Notification parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (Notification) GeneratedMessageV3.parseWithIOException(PARSER, inputStream, extensionRegistryLite);
        }

        public static Notification parseDelimitedFrom(InputStream inputStream) throws IOException {
            return (Notification) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, inputStream);
        }

        public static Notification parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (Notification) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, inputStream, extensionRegistryLite);
        }

        public static Notification parseFrom(CodedInputStream codedInputStream) throws IOException {
            return (Notification) GeneratedMessageV3.parseWithIOException(PARSER, codedInputStream);
        }

        public static Notification parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (Notification) GeneratedMessageV3.parseWithIOException(PARSER, codedInputStream, extensionRegistryLite);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(Notification notification) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(notification);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Builder newBuilderForType(GeneratedMessageV3.BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements NotificationOrBuilder {
            private SingleFieldBuilderV3<Android, Android.Builder, AndroidOrBuilder> androidBuilder_;
            private Android android_;
            private SingleFieldBuilderV3<APNS, APNS.Builder, APNSOrBuilder> apnsBuilder_;
            private APNS apns_;
            private Object content_;
            private Object featureContent_;
            private Object featureSubTitle_;
            private Object featureTitle_;
            private boolean silent_;
            private Object subTitle_;
            private SingleFieldBuilderV3<SystemContent, SystemContent.Builder, SystemContentOrBuilder> systemContentBuilder_;
            private SystemContent systemContent_;
            private Object title_;

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return ClientSdkgate.internal_static_proto_sdkgate_Notification_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return ClientSdkgate.internal_static_proto_sdkgate_Notification_fieldAccessorTable.ensureFieldAccessorsInitialized(Notification.class, Builder.class);
            }

            private Builder() {
                this.title_ = "";
                this.subTitle_ = "";
                this.content_ = "";
                this.featureTitle_ = "";
                this.featureSubTitle_ = "";
                this.featureContent_ = "";
                maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessageV3.BuilderParent builderParent) {
                super(builderParent);
                this.title_ = "";
                this.subTitle_ = "";
                this.content_ = "";
                this.featureTitle_ = "";
                this.featureSubTitle_ = "";
                this.featureContent_ = "";
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                boolean unused = Notification.alwaysUseFieldBuilders;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                this.title_ = "";
                this.subTitle_ = "";
                this.content_ = "";
                this.silent_ = false;
                if (this.apnsBuilder_ == null) {
                    this.apns_ = null;
                } else {
                    this.apns_ = null;
                    this.apnsBuilder_ = null;
                }
                if (this.androidBuilder_ == null) {
                    this.android_ = null;
                } else {
                    this.android_ = null;
                    this.androidBuilder_ = null;
                }
                if (this.systemContentBuilder_ == null) {
                    this.systemContent_ = null;
                } else {
                    this.systemContent_ = null;
                    this.systemContentBuilder_ = null;
                }
                this.featureTitle_ = "";
                this.featureSubTitle_ = "";
                this.featureContent_ = "";
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return ClientSdkgate.internal_static_proto_sdkgate_Notification_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public Notification getDefaultInstanceForType() {
                return Notification.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Notification build() {
                Notification notificationBuildPartial = buildPartial();
                if (notificationBuildPartial.isInitialized()) {
                    return notificationBuildPartial;
                }
                throw newUninitializedMessageException((Message) notificationBuildPartial);
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Notification buildPartial() {
                Notification notification = new Notification(this);
                notification.title_ = this.title_;
                notification.subTitle_ = this.subTitle_;
                notification.content_ = this.content_;
                notification.silent_ = this.silent_;
                SingleFieldBuilderV3<APNS, APNS.Builder, APNSOrBuilder> singleFieldBuilderV3 = this.apnsBuilder_;
                if (singleFieldBuilderV3 == null) {
                    notification.apns_ = this.apns_;
                } else {
                    notification.apns_ = (APNS) singleFieldBuilderV3.build();
                }
                SingleFieldBuilderV3<Android, Android.Builder, AndroidOrBuilder> singleFieldBuilderV32 = this.androidBuilder_;
                if (singleFieldBuilderV32 == null) {
                    notification.android_ = this.android_;
                } else {
                    notification.android_ = (Android) singleFieldBuilderV32.build();
                }
                SingleFieldBuilderV3<SystemContent, SystemContent.Builder, SystemContentOrBuilder> singleFieldBuilderV33 = this.systemContentBuilder_;
                if (singleFieldBuilderV33 == null) {
                    notification.systemContent_ = this.systemContent_;
                } else {
                    notification.systemContent_ = (SystemContent) singleFieldBuilderV33.build();
                }
                notification.featureTitle_ = this.featureTitle_;
                notification.featureSubTitle_ = this.featureSubTitle_;
                notification.featureContent_ = this.featureContent_;
                onBuilt();
                return notification;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder
            /* renamed from: clone */
            public Builder mo375clone() {
                return (Builder) super.mo375clone();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setField(Descriptors.FieldDescriptor fieldDescriptor, Object obj) {
                return (Builder) super.setField(fieldDescriptor, obj);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder clearField(Descriptors.FieldDescriptor fieldDescriptor) {
                return (Builder) super.clearField(fieldDescriptor);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder clearOneof(Descriptors.OneofDescriptor oneofDescriptor) {
                return (Builder) super.clearOneof(oneofDescriptor);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, int i, Object obj) {
                return (Builder) super.setRepeatedField(fieldDescriptor, i, obj);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder addRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, Object obj) {
                return (Builder) super.addRepeatedField(fieldDescriptor, obj);
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(Message message) {
                if (message instanceof Notification) {
                    return mergeFrom((Notification) message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder mergeFrom(Notification notification) {
                if (notification == Notification.getDefaultInstance()) {
                    return this;
                }
                if (!notification.getTitle().isEmpty()) {
                    this.title_ = notification.title_;
                    onChanged();
                }
                if (!notification.getSubTitle().isEmpty()) {
                    this.subTitle_ = notification.subTitle_;
                    onChanged();
                }
                if (!notification.getContent().isEmpty()) {
                    this.content_ = notification.content_;
                    onChanged();
                }
                if (notification.getSilent()) {
                    setSilent(notification.getSilent());
                }
                if (notification.hasApns()) {
                    mergeApns(notification.getApns());
                }
                if (notification.hasAndroid()) {
                    mergeAndroid(notification.getAndroid());
                }
                if (notification.hasSystemContent()) {
                    mergeSystemContent(notification.getSystemContent());
                }
                if (!notification.getFeatureTitle().isEmpty()) {
                    this.featureTitle_ = notification.featureTitle_;
                    onChanged();
                }
                if (!notification.getFeatureSubTitle().isEmpty()) {
                    this.featureSubTitle_ = notification.featureSubTitle_;
                    onChanged();
                }
                if (!notification.getFeatureContent().isEmpty()) {
                    this.featureContent_ = notification.featureContent_;
                    onChanged();
                }
                mergeUnknownFields(notification.unknownFields);
                onChanged();
                return this;
            }

            /* JADX WARN: Removed duplicated region for block: B:16:0x0023  */
            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public com.netease.push.proto.nano.ClientSdkgate.Notification.Builder mergeFrom(com.google.protobuf.CodedInputStream r3, com.google.protobuf.ExtensionRegistryLite r4) throws java.lang.Throwable {
                /*
                    r2 = this;
                    r0 = 0
                    com.google.protobuf.Parser r1 = com.netease.push.proto.nano.ClientSdkgate.Notification.access$14800()     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    java.lang.Object r3 = r1.parsePartialFrom(r3, r4)     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    com.netease.push.proto.nano.ClientSdkgate$Notification r3 = (com.netease.push.proto.nano.ClientSdkgate.Notification) r3     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    if (r3 == 0) goto L10
                    r2.mergeFrom(r3)
                L10:
                    return r2
                L11:
                    r3 = move-exception
                    goto L21
                L13:
                    r3 = move-exception
                    com.google.protobuf.MessageLite r4 = r3.getUnfinishedMessage()     // Catch: java.lang.Throwable -> L11
                    com.netease.push.proto.nano.ClientSdkgate$Notification r4 = (com.netease.push.proto.nano.ClientSdkgate.Notification) r4     // Catch: java.lang.Throwable -> L11
                    java.io.IOException r3 = r3.unwrapIOException()     // Catch: java.lang.Throwable -> L1f
                    throw r3     // Catch: java.lang.Throwable -> L1f
                L1f:
                    r3 = move-exception
                    r0 = r4
                L21:
                    if (r0 == 0) goto L26
                    r2.mergeFrom(r0)
                L26:
                    throw r3
                */
                throw new UnsupportedOperationException("Method not decompiled: com.netease.push.proto.nano.ClientSdkgate.Notification.Builder.mergeFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):com.netease.push.proto.nano.ClientSdkgate$Notification$Builder");
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
            public String getTitle() {
                Object obj = this.title_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.title_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
            public ByteString getTitleBytes() {
                Object obj = this.title_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.title_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setTitle(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.title_ = str;
                onChanged();
                return this;
            }

            public Builder clearTitle() {
                this.title_ = Notification.getDefaultInstance().getTitle();
                onChanged();
                return this;
            }

            public Builder setTitleBytes(ByteString byteString) {
                if (byteString != null) {
                    Notification.checkByteStringIsUtf8(byteString);
                    this.title_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
            public String getSubTitle() {
                Object obj = this.subTitle_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.subTitle_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
            public ByteString getSubTitleBytes() {
                Object obj = this.subTitle_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.subTitle_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setSubTitle(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.subTitle_ = str;
                onChanged();
                return this;
            }

            public Builder clearSubTitle() {
                this.subTitle_ = Notification.getDefaultInstance().getSubTitle();
                onChanged();
                return this;
            }

            public Builder setSubTitleBytes(ByteString byteString) {
                if (byteString != null) {
                    Notification.checkByteStringIsUtf8(byteString);
                    this.subTitle_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
            public String getContent() {
                Object obj = this.content_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.content_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
            public ByteString getContentBytes() {
                Object obj = this.content_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.content_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setContent(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.content_ = str;
                onChanged();
                return this;
            }

            public Builder clearContent() {
                this.content_ = Notification.getDefaultInstance().getContent();
                onChanged();
                return this;
            }

            public Builder setContentBytes(ByteString byteString) {
                if (byteString != null) {
                    Notification.checkByteStringIsUtf8(byteString);
                    this.content_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
            public boolean getSilent() {
                return this.silent_;
            }

            public Builder setSilent(boolean z) {
                this.silent_ = z;
                onChanged();
                return this;
            }

            public Builder clearSilent() {
                this.silent_ = false;
                onChanged();
                return this;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
            public boolean hasApns() {
                return (this.apnsBuilder_ == null && this.apns_ == null) ? false : true;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
            public APNS getApns() {
                SingleFieldBuilderV3<APNS, APNS.Builder, APNSOrBuilder> singleFieldBuilderV3 = this.apnsBuilder_;
                if (singleFieldBuilderV3 == null) {
                    APNS apns = this.apns_;
                    return apns == null ? APNS.getDefaultInstance() : apns;
                }
                return (APNS) singleFieldBuilderV3.getMessage();
            }

            public Builder setApns(APNS apns) {
                SingleFieldBuilderV3<APNS, APNS.Builder, APNSOrBuilder> singleFieldBuilderV3 = this.apnsBuilder_;
                if (singleFieldBuilderV3 != null) {
                    singleFieldBuilderV3.setMessage(apns);
                } else {
                    if (apns == null) {
                        throw new NullPointerException();
                    }
                    this.apns_ = apns;
                    onChanged();
                }
                return this;
            }

            public Builder setApns(APNS.Builder builder) {
                SingleFieldBuilderV3<APNS, APNS.Builder, APNSOrBuilder> singleFieldBuilderV3 = this.apnsBuilder_;
                if (singleFieldBuilderV3 == null) {
                    this.apns_ = builder.build();
                    onChanged();
                } else {
                    singleFieldBuilderV3.setMessage(builder.build());
                }
                return this;
            }

            public Builder mergeApns(APNS apns) {
                SingleFieldBuilderV3<APNS, APNS.Builder, APNSOrBuilder> singleFieldBuilderV3 = this.apnsBuilder_;
                if (singleFieldBuilderV3 == null) {
                    APNS apns2 = this.apns_;
                    if (apns2 != null) {
                        this.apns_ = APNS.newBuilder(apns2).mergeFrom(apns).buildPartial();
                    } else {
                        this.apns_ = apns;
                    }
                    onChanged();
                } else {
                    singleFieldBuilderV3.mergeFrom(apns);
                }
                return this;
            }

            public Builder clearApns() {
                if (this.apnsBuilder_ == null) {
                    this.apns_ = null;
                    onChanged();
                } else {
                    this.apns_ = null;
                    this.apnsBuilder_ = null;
                }
                return this;
            }

            public APNS.Builder getApnsBuilder() {
                onChanged();
                return (APNS.Builder) getApnsFieldBuilder().getBuilder();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
            public APNSOrBuilder getApnsOrBuilder() {
                SingleFieldBuilderV3<APNS, APNS.Builder, APNSOrBuilder> singleFieldBuilderV3 = this.apnsBuilder_;
                if (singleFieldBuilderV3 != null) {
                    return (APNSOrBuilder) singleFieldBuilderV3.getMessageOrBuilder();
                }
                APNS apns = this.apns_;
                return apns == null ? APNS.getDefaultInstance() : apns;
            }

            private SingleFieldBuilderV3<APNS, APNS.Builder, APNSOrBuilder> getApnsFieldBuilder() {
                if (this.apnsBuilder_ == null) {
                    this.apnsBuilder_ = new SingleFieldBuilderV3<>(getApns(), getParentForChildren(), isClean());
                    this.apns_ = null;
                }
                return this.apnsBuilder_;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
            public boolean hasAndroid() {
                return (this.androidBuilder_ == null && this.android_ == null) ? false : true;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
            public Android getAndroid() {
                SingleFieldBuilderV3<Android, Android.Builder, AndroidOrBuilder> singleFieldBuilderV3 = this.androidBuilder_;
                if (singleFieldBuilderV3 == null) {
                    Android android2 = this.android_;
                    return android2 == null ? Android.getDefaultInstance() : android2;
                }
                return (Android) singleFieldBuilderV3.getMessage();
            }

            public Builder setAndroid(Android android2) {
                SingleFieldBuilderV3<Android, Android.Builder, AndroidOrBuilder> singleFieldBuilderV3 = this.androidBuilder_;
                if (singleFieldBuilderV3 != null) {
                    singleFieldBuilderV3.setMessage(android2);
                } else {
                    if (android2 == null) {
                        throw new NullPointerException();
                    }
                    this.android_ = android2;
                    onChanged();
                }
                return this;
            }

            public Builder setAndroid(Android.Builder builder) {
                SingleFieldBuilderV3<Android, Android.Builder, AndroidOrBuilder> singleFieldBuilderV3 = this.androidBuilder_;
                if (singleFieldBuilderV3 == null) {
                    this.android_ = builder.build();
                    onChanged();
                } else {
                    singleFieldBuilderV3.setMessage(builder.build());
                }
                return this;
            }

            public Builder mergeAndroid(Android android2) {
                SingleFieldBuilderV3<Android, Android.Builder, AndroidOrBuilder> singleFieldBuilderV3 = this.androidBuilder_;
                if (singleFieldBuilderV3 == null) {
                    Android android3 = this.android_;
                    if (android3 != null) {
                        this.android_ = Android.newBuilder(android3).mergeFrom(android2).buildPartial();
                    } else {
                        this.android_ = android2;
                    }
                    onChanged();
                } else {
                    singleFieldBuilderV3.mergeFrom(android2);
                }
                return this;
            }

            public Builder clearAndroid() {
                if (this.androidBuilder_ == null) {
                    this.android_ = null;
                    onChanged();
                } else {
                    this.android_ = null;
                    this.androidBuilder_ = null;
                }
                return this;
            }

            public Android.Builder getAndroidBuilder() {
                onChanged();
                return (Android.Builder) getAndroidFieldBuilder().getBuilder();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
            public AndroidOrBuilder getAndroidOrBuilder() {
                SingleFieldBuilderV3<Android, Android.Builder, AndroidOrBuilder> singleFieldBuilderV3 = this.androidBuilder_;
                if (singleFieldBuilderV3 != null) {
                    return (AndroidOrBuilder) singleFieldBuilderV3.getMessageOrBuilder();
                }
                Android android2 = this.android_;
                return android2 == null ? Android.getDefaultInstance() : android2;
            }

            private SingleFieldBuilderV3<Android, Android.Builder, AndroidOrBuilder> getAndroidFieldBuilder() {
                if (this.androidBuilder_ == null) {
                    this.androidBuilder_ = new SingleFieldBuilderV3<>(getAndroid(), getParentForChildren(), isClean());
                    this.android_ = null;
                }
                return this.androidBuilder_;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
            public boolean hasSystemContent() {
                return (this.systemContentBuilder_ == null && this.systemContent_ == null) ? false : true;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
            public SystemContent getSystemContent() {
                SingleFieldBuilderV3<SystemContent, SystemContent.Builder, SystemContentOrBuilder> singleFieldBuilderV3 = this.systemContentBuilder_;
                if (singleFieldBuilderV3 == null) {
                    SystemContent systemContent = this.systemContent_;
                    return systemContent == null ? SystemContent.getDefaultInstance() : systemContent;
                }
                return (SystemContent) singleFieldBuilderV3.getMessage();
            }

            public Builder setSystemContent(SystemContent systemContent) {
                SingleFieldBuilderV3<SystemContent, SystemContent.Builder, SystemContentOrBuilder> singleFieldBuilderV3 = this.systemContentBuilder_;
                if (singleFieldBuilderV3 != null) {
                    singleFieldBuilderV3.setMessage(systemContent);
                } else {
                    if (systemContent == null) {
                        throw new NullPointerException();
                    }
                    this.systemContent_ = systemContent;
                    onChanged();
                }
                return this;
            }

            public Builder setSystemContent(SystemContent.Builder builder) {
                SingleFieldBuilderV3<SystemContent, SystemContent.Builder, SystemContentOrBuilder> singleFieldBuilderV3 = this.systemContentBuilder_;
                if (singleFieldBuilderV3 == null) {
                    this.systemContent_ = builder.build();
                    onChanged();
                } else {
                    singleFieldBuilderV3.setMessage(builder.build());
                }
                return this;
            }

            public Builder mergeSystemContent(SystemContent systemContent) {
                SingleFieldBuilderV3<SystemContent, SystemContent.Builder, SystemContentOrBuilder> singleFieldBuilderV3 = this.systemContentBuilder_;
                if (singleFieldBuilderV3 == null) {
                    SystemContent systemContent2 = this.systemContent_;
                    if (systemContent2 != null) {
                        this.systemContent_ = SystemContent.newBuilder(systemContent2).mergeFrom(systemContent).buildPartial();
                    } else {
                        this.systemContent_ = systemContent;
                    }
                    onChanged();
                } else {
                    singleFieldBuilderV3.mergeFrom(systemContent);
                }
                return this;
            }

            public Builder clearSystemContent() {
                if (this.systemContentBuilder_ == null) {
                    this.systemContent_ = null;
                    onChanged();
                } else {
                    this.systemContent_ = null;
                    this.systemContentBuilder_ = null;
                }
                return this;
            }

            public SystemContent.Builder getSystemContentBuilder() {
                onChanged();
                return (SystemContent.Builder) getSystemContentFieldBuilder().getBuilder();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
            public SystemContentOrBuilder getSystemContentOrBuilder() {
                SingleFieldBuilderV3<SystemContent, SystemContent.Builder, SystemContentOrBuilder> singleFieldBuilderV3 = this.systemContentBuilder_;
                if (singleFieldBuilderV3 != null) {
                    return (SystemContentOrBuilder) singleFieldBuilderV3.getMessageOrBuilder();
                }
                SystemContent systemContent = this.systemContent_;
                return systemContent == null ? SystemContent.getDefaultInstance() : systemContent;
            }

            private SingleFieldBuilderV3<SystemContent, SystemContent.Builder, SystemContentOrBuilder> getSystemContentFieldBuilder() {
                if (this.systemContentBuilder_ == null) {
                    this.systemContentBuilder_ = new SingleFieldBuilderV3<>(getSystemContent(), getParentForChildren(), isClean());
                    this.systemContent_ = null;
                }
                return this.systemContentBuilder_;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
            public String getFeatureTitle() {
                Object obj = this.featureTitle_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.featureTitle_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
            public ByteString getFeatureTitleBytes() {
                Object obj = this.featureTitle_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.featureTitle_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setFeatureTitle(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.featureTitle_ = str;
                onChanged();
                return this;
            }

            public Builder clearFeatureTitle() {
                this.featureTitle_ = Notification.getDefaultInstance().getFeatureTitle();
                onChanged();
                return this;
            }

            public Builder setFeatureTitleBytes(ByteString byteString) {
                if (byteString != null) {
                    Notification.checkByteStringIsUtf8(byteString);
                    this.featureTitle_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
            public String getFeatureSubTitle() {
                Object obj = this.featureSubTitle_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.featureSubTitle_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
            public ByteString getFeatureSubTitleBytes() {
                Object obj = this.featureSubTitle_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.featureSubTitle_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setFeatureSubTitle(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.featureSubTitle_ = str;
                onChanged();
                return this;
            }

            public Builder clearFeatureSubTitle() {
                this.featureSubTitle_ = Notification.getDefaultInstance().getFeatureSubTitle();
                onChanged();
                return this;
            }

            public Builder setFeatureSubTitleBytes(ByteString byteString) {
                if (byteString != null) {
                    Notification.checkByteStringIsUtf8(byteString);
                    this.featureSubTitle_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
            public String getFeatureContent() {
                Object obj = this.featureContent_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.featureContent_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.NotificationOrBuilder
            public ByteString getFeatureContentBytes() {
                Object obj = this.featureContent_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.featureContent_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setFeatureContent(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.featureContent_ = str;
                onChanged();
                return this;
            }

            public Builder clearFeatureContent() {
                this.featureContent_ = Notification.getDefaultInstance().getFeatureContent();
                onChanged();
                return this;
            }

            public Builder setFeatureContentBytes(ByteString byteString) {
                if (byteString != null) {
                    Notification.checkByteStringIsUtf8(byteString);
                    this.featureContent_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public final Builder setUnknownFields(UnknownFieldSet unknownFieldSet) {
                return (Builder) super.setUnknownFields(unknownFieldSet);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public final Builder mergeUnknownFields(UnknownFieldSet unknownFieldSet) {
                return (Builder) super.mergeUnknownFields(unknownFieldSet);
            }
        }

        public static Notification getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<Notification> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<Notification> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public Notification getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    public static final class APNS extends GeneratedMessageV3 implements APNSOrBuilder {
        public static final int BADGE_FIELD_NUMBER = 6;
        public static final int CATEGORY_FIELD_NUMBER = 4;
        public static final int COLLAPSE_ID_FIELD_NUMBER = 8;
        public static final int CUSTOM_CONTENT_FIELD_NUMBER = 7;
        public static final int MEDIA_URL_FIELD_NUMBER = 2;
        public static final int MUTABLE_CONTENT_FIELD_NUMBER = 1;
        public static final int SOUND_FIELD_NUMBER = 3;
        public static final int THREAD_ID_FIELD_NUMBER = 5;
        private static final long serialVersionUID = 0;
        private long badge_;
        private volatile Object category_;
        private volatile Object collapseId_;
        private volatile Object customContent_;
        private volatile Object mediaUrl_;
        private byte memoizedIsInitialized;
        private long mutableContent_;
        private volatile Object sound_;
        private volatile Object threadId_;
        private static final APNS DEFAULT_INSTANCE = new APNS();
        private static final Parser<APNS> PARSER = new AbstractParser<APNS>() { // from class: com.netease.push.proto.nano.ClientSdkgate.APNS.1
            @Override // com.google.protobuf.Parser
            public APNS parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return new APNS(codedInputStream, extensionRegistryLite);
            }
        };

        private APNS(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private APNS() {
            this.memoizedIsInitialized = (byte) -1;
            this.mediaUrl_ = "";
            this.sound_ = "";
            this.category_ = "";
            this.threadId_ = "";
            this.customContent_ = "";
            this.collapseId_ = "";
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private APNS(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            this();
            if (extensionRegistryLite == null) {
                throw new NullPointerException();
            }
            UnknownFieldSet.Builder builderNewBuilder = UnknownFieldSet.newBuilder();
            boolean z = false;
            while (!z) {
                try {
                    try {
                        try {
                            int tag = codedInputStream.readTag();
                            if (tag != 0) {
                                if (tag == 8) {
                                    this.mutableContent_ = codedInputStream.readInt64();
                                } else if (tag == 18) {
                                    this.mediaUrl_ = codedInputStream.readStringRequireUtf8();
                                } else if (tag == 26) {
                                    this.sound_ = codedInputStream.readStringRequireUtf8();
                                } else if (tag == 34) {
                                    this.category_ = codedInputStream.readStringRequireUtf8();
                                } else if (tag == 42) {
                                    this.threadId_ = codedInputStream.readStringRequireUtf8();
                                } else if (tag == 48) {
                                    this.badge_ = codedInputStream.readInt64();
                                } else if (tag == 58) {
                                    this.customContent_ = codedInputStream.readStringRequireUtf8();
                                } else if (tag == 66) {
                                    this.collapseId_ = codedInputStream.readStringRequireUtf8();
                                } else if (!parseUnknownField(codedInputStream, builderNewBuilder, extensionRegistryLite, tag)) {
                                }
                            }
                            z = true;
                        } catch (IOException e) {
                            throw new InvalidProtocolBufferException(e).setUnfinishedMessage(this);
                        }
                    } catch (InvalidProtocolBufferException e2) {
                        throw e2.setUnfinishedMessage(this);
                    }
                } finally {
                    this.unknownFields = builderNewBuilder.build();
                    makeExtensionsImmutable();
                }
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return ClientSdkgate.internal_static_proto_sdkgate_APNS_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return ClientSdkgate.internal_static_proto_sdkgate_APNS_fieldAccessorTable.ensureFieldAccessorsInitialized(APNS.class, Builder.class);
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.APNSOrBuilder
        public long getMutableContent() {
            return this.mutableContent_;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.APNSOrBuilder
        public String getMediaUrl() {
            Object obj = this.mediaUrl_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.mediaUrl_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.APNSOrBuilder
        public ByteString getMediaUrlBytes() {
            Object obj = this.mediaUrl_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.mediaUrl_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.APNSOrBuilder
        public String getSound() {
            Object obj = this.sound_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.sound_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.APNSOrBuilder
        public ByteString getSoundBytes() {
            Object obj = this.sound_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.sound_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.APNSOrBuilder
        public String getCategory() {
            Object obj = this.category_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.category_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.APNSOrBuilder
        public ByteString getCategoryBytes() {
            Object obj = this.category_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.category_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.APNSOrBuilder
        public String getThreadId() {
            Object obj = this.threadId_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.threadId_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.APNSOrBuilder
        public ByteString getThreadIdBytes() {
            Object obj = this.threadId_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.threadId_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.APNSOrBuilder
        public long getBadge() {
            return this.badge_;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.APNSOrBuilder
        public String getCustomContent() {
            Object obj = this.customContent_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.customContent_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.APNSOrBuilder
        public ByteString getCustomContentBytes() {
            Object obj = this.customContent_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.customContent_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.APNSOrBuilder
        public String getCollapseId() {
            Object obj = this.collapseId_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.collapseId_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.APNSOrBuilder
        public ByteString getCollapseIdBytes() {
            Object obj = this.collapseId_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.collapseId_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            if (b == 1) {
                return true;
            }
            if (b == 0) {
                return false;
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            long j = this.mutableContent_;
            if (j != 0) {
                codedOutputStream.writeInt64(1, j);
            }
            if (!getMediaUrlBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 2, this.mediaUrl_);
            }
            if (!getSoundBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 3, this.sound_);
            }
            if (!getCategoryBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 4, this.category_);
            }
            if (!getThreadIdBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 5, this.threadId_);
            }
            long j2 = this.badge_;
            if (j2 != 0) {
                codedOutputStream.writeInt64(6, j2);
            }
            if (!getCustomContentBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 7, this.customContent_);
            }
            if (!getCollapseIdBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 8, this.collapseId_);
            }
            this.unknownFields.writeTo(codedOutputStream);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public int getSerializedSize() {
            int i = this.memoizedSize;
            if (i != -1) {
                return i;
            }
            long j = this.mutableContent_;
            int iComputeInt64Size = j != 0 ? 0 + CodedOutputStream.computeInt64Size(1, j) : 0;
            if (!getMediaUrlBytes().isEmpty()) {
                iComputeInt64Size += GeneratedMessageV3.computeStringSize(2, this.mediaUrl_);
            }
            if (!getSoundBytes().isEmpty()) {
                iComputeInt64Size += GeneratedMessageV3.computeStringSize(3, this.sound_);
            }
            if (!getCategoryBytes().isEmpty()) {
                iComputeInt64Size += GeneratedMessageV3.computeStringSize(4, this.category_);
            }
            if (!getThreadIdBytes().isEmpty()) {
                iComputeInt64Size += GeneratedMessageV3.computeStringSize(5, this.threadId_);
            }
            long j2 = this.badge_;
            if (j2 != 0) {
                iComputeInt64Size += CodedOutputStream.computeInt64Size(6, j2);
            }
            if (!getCustomContentBytes().isEmpty()) {
                iComputeInt64Size += GeneratedMessageV3.computeStringSize(7, this.customContent_);
            }
            if (!getCollapseIdBytes().isEmpty()) {
                iComputeInt64Size += GeneratedMessageV3.computeStringSize(8, this.collapseId_);
            }
            int serializedSize = iComputeInt64Size + this.unknownFields.getSerializedSize();
            this.memoizedSize = serializedSize;
            return serializedSize;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof APNS)) {
                return super.equals(obj);
            }
            APNS apns = (APNS) obj;
            return getMutableContent() == apns.getMutableContent() && getMediaUrl().equals(apns.getMediaUrl()) && getSound().equals(apns.getSound()) && getCategory().equals(apns.getCategory()) && getThreadId().equals(apns.getThreadId()) && getBadge() == apns.getBadge() && getCustomContent().equals(apns.getCustomContent()) && getCollapseId().equals(apns.getCollapseId()) && this.unknownFields.equals(apns.unknownFields);
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int iHashCode = ((((((((((((((((((((((((((((((((((779 + getDescriptor().hashCode()) * 37) + 1) * 53) + Internal.hashLong(getMutableContent())) * 37) + 2) * 53) + getMediaUrl().hashCode()) * 37) + 3) * 53) + getSound().hashCode()) * 37) + 4) * 53) + getCategory().hashCode()) * 37) + 5) * 53) + getThreadId().hashCode()) * 37) + 6) * 53) + Internal.hashLong(getBadge())) * 37) + 7) * 53) + getCustomContent().hashCode()) * 37) + 8) * 53) + getCollapseId().hashCode()) * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = iHashCode;
            return iHashCode;
        }

        public static APNS parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString);
        }

        public static APNS parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString, extensionRegistryLite);
        }

        public static APNS parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(bArr);
        }

        public static APNS parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(bArr, extensionRegistryLite);
        }

        public static APNS parseFrom(InputStream inputStream) throws IOException {
            return (APNS) GeneratedMessageV3.parseWithIOException(PARSER, inputStream);
        }

        public static APNS parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (APNS) GeneratedMessageV3.parseWithIOException(PARSER, inputStream, extensionRegistryLite);
        }

        public static APNS parseDelimitedFrom(InputStream inputStream) throws IOException {
            return (APNS) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, inputStream);
        }

        public static APNS parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (APNS) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, inputStream, extensionRegistryLite);
        }

        public static APNS parseFrom(CodedInputStream codedInputStream) throws IOException {
            return (APNS) GeneratedMessageV3.parseWithIOException(PARSER, codedInputStream);
        }

        public static APNS parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (APNS) GeneratedMessageV3.parseWithIOException(PARSER, codedInputStream, extensionRegistryLite);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(APNS apns) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(apns);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Builder newBuilderForType(GeneratedMessageV3.BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements APNSOrBuilder {
            private long badge_;
            private Object category_;
            private Object collapseId_;
            private Object customContent_;
            private Object mediaUrl_;
            private long mutableContent_;
            private Object sound_;
            private Object threadId_;

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return ClientSdkgate.internal_static_proto_sdkgate_APNS_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return ClientSdkgate.internal_static_proto_sdkgate_APNS_fieldAccessorTable.ensureFieldAccessorsInitialized(APNS.class, Builder.class);
            }

            private Builder() {
                this.mediaUrl_ = "";
                this.sound_ = "";
                this.category_ = "";
                this.threadId_ = "";
                this.customContent_ = "";
                this.collapseId_ = "";
                maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessageV3.BuilderParent builderParent) {
                super(builderParent);
                this.mediaUrl_ = "";
                this.sound_ = "";
                this.category_ = "";
                this.threadId_ = "";
                this.customContent_ = "";
                this.collapseId_ = "";
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                boolean unused = APNS.alwaysUseFieldBuilders;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                this.mutableContent_ = 0L;
                this.mediaUrl_ = "";
                this.sound_ = "";
                this.category_ = "";
                this.threadId_ = "";
                this.badge_ = 0L;
                this.customContent_ = "";
                this.collapseId_ = "";
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return ClientSdkgate.internal_static_proto_sdkgate_APNS_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public APNS getDefaultInstanceForType() {
                return APNS.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public APNS build() {
                APNS apnsBuildPartial = buildPartial();
                if (apnsBuildPartial.isInitialized()) {
                    return apnsBuildPartial;
                }
                throw newUninitializedMessageException((Message) apnsBuildPartial);
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public APNS buildPartial() {
                APNS apns = new APNS(this);
                apns.mutableContent_ = this.mutableContent_;
                apns.mediaUrl_ = this.mediaUrl_;
                apns.sound_ = this.sound_;
                apns.category_ = this.category_;
                apns.threadId_ = this.threadId_;
                apns.badge_ = this.badge_;
                apns.customContent_ = this.customContent_;
                apns.collapseId_ = this.collapseId_;
                onBuilt();
                return apns;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder
            /* renamed from: clone */
            public Builder mo375clone() {
                return (Builder) super.mo375clone();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setField(Descriptors.FieldDescriptor fieldDescriptor, Object obj) {
                return (Builder) super.setField(fieldDescriptor, obj);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder clearField(Descriptors.FieldDescriptor fieldDescriptor) {
                return (Builder) super.clearField(fieldDescriptor);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder clearOneof(Descriptors.OneofDescriptor oneofDescriptor) {
                return (Builder) super.clearOneof(oneofDescriptor);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, int i, Object obj) {
                return (Builder) super.setRepeatedField(fieldDescriptor, i, obj);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder addRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, Object obj) {
                return (Builder) super.addRepeatedField(fieldDescriptor, obj);
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(Message message) {
                if (message instanceof APNS) {
                    return mergeFrom((APNS) message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder mergeFrom(APNS apns) {
                if (apns == APNS.getDefaultInstance()) {
                    return this;
                }
                if (apns.getMutableContent() != 0) {
                    setMutableContent(apns.getMutableContent());
                }
                if (!apns.getMediaUrl().isEmpty()) {
                    this.mediaUrl_ = apns.mediaUrl_;
                    onChanged();
                }
                if (!apns.getSound().isEmpty()) {
                    this.sound_ = apns.sound_;
                    onChanged();
                }
                if (!apns.getCategory().isEmpty()) {
                    this.category_ = apns.category_;
                    onChanged();
                }
                if (!apns.getThreadId().isEmpty()) {
                    this.threadId_ = apns.threadId_;
                    onChanged();
                }
                if (apns.getBadge() != 0) {
                    setBadge(apns.getBadge());
                }
                if (!apns.getCustomContent().isEmpty()) {
                    this.customContent_ = apns.customContent_;
                    onChanged();
                }
                if (!apns.getCollapseId().isEmpty()) {
                    this.collapseId_ = apns.collapseId_;
                    onChanged();
                }
                mergeUnknownFields(apns.unknownFields);
                onChanged();
                return this;
            }

            /* JADX WARN: Removed duplicated region for block: B:16:0x0023  */
            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public com.netease.push.proto.nano.ClientSdkgate.APNS.Builder mergeFrom(com.google.protobuf.CodedInputStream r3, com.google.protobuf.ExtensionRegistryLite r4) throws java.lang.Throwable {
                /*
                    r2 = this;
                    r0 = 0
                    com.google.protobuf.Parser r1 = com.netease.push.proto.nano.ClientSdkgate.APNS.access$17100()     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    java.lang.Object r3 = r1.parsePartialFrom(r3, r4)     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    com.netease.push.proto.nano.ClientSdkgate$APNS r3 = (com.netease.push.proto.nano.ClientSdkgate.APNS) r3     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    if (r3 == 0) goto L10
                    r2.mergeFrom(r3)
                L10:
                    return r2
                L11:
                    r3 = move-exception
                    goto L21
                L13:
                    r3 = move-exception
                    com.google.protobuf.MessageLite r4 = r3.getUnfinishedMessage()     // Catch: java.lang.Throwable -> L11
                    com.netease.push.proto.nano.ClientSdkgate$APNS r4 = (com.netease.push.proto.nano.ClientSdkgate.APNS) r4     // Catch: java.lang.Throwable -> L11
                    java.io.IOException r3 = r3.unwrapIOException()     // Catch: java.lang.Throwable -> L1f
                    throw r3     // Catch: java.lang.Throwable -> L1f
                L1f:
                    r3 = move-exception
                    r0 = r4
                L21:
                    if (r0 == 0) goto L26
                    r2.mergeFrom(r0)
                L26:
                    throw r3
                */
                throw new UnsupportedOperationException("Method not decompiled: com.netease.push.proto.nano.ClientSdkgate.APNS.Builder.mergeFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):com.netease.push.proto.nano.ClientSdkgate$APNS$Builder");
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.APNSOrBuilder
            public long getMutableContent() {
                return this.mutableContent_;
            }

            public Builder setMutableContent(long j) {
                this.mutableContent_ = j;
                onChanged();
                return this;
            }

            public Builder clearMutableContent() {
                this.mutableContent_ = 0L;
                onChanged();
                return this;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.APNSOrBuilder
            public String getMediaUrl() {
                Object obj = this.mediaUrl_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.mediaUrl_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.APNSOrBuilder
            public ByteString getMediaUrlBytes() {
                Object obj = this.mediaUrl_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.mediaUrl_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setMediaUrl(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.mediaUrl_ = str;
                onChanged();
                return this;
            }

            public Builder clearMediaUrl() {
                this.mediaUrl_ = APNS.getDefaultInstance().getMediaUrl();
                onChanged();
                return this;
            }

            public Builder setMediaUrlBytes(ByteString byteString) {
                if (byteString != null) {
                    APNS.checkByteStringIsUtf8(byteString);
                    this.mediaUrl_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.APNSOrBuilder
            public String getSound() {
                Object obj = this.sound_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.sound_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.APNSOrBuilder
            public ByteString getSoundBytes() {
                Object obj = this.sound_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.sound_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setSound(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.sound_ = str;
                onChanged();
                return this;
            }

            public Builder clearSound() {
                this.sound_ = APNS.getDefaultInstance().getSound();
                onChanged();
                return this;
            }

            public Builder setSoundBytes(ByteString byteString) {
                if (byteString != null) {
                    APNS.checkByteStringIsUtf8(byteString);
                    this.sound_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.APNSOrBuilder
            public String getCategory() {
                Object obj = this.category_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.category_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.APNSOrBuilder
            public ByteString getCategoryBytes() {
                Object obj = this.category_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.category_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setCategory(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.category_ = str;
                onChanged();
                return this;
            }

            public Builder clearCategory() {
                this.category_ = APNS.getDefaultInstance().getCategory();
                onChanged();
                return this;
            }

            public Builder setCategoryBytes(ByteString byteString) {
                if (byteString != null) {
                    APNS.checkByteStringIsUtf8(byteString);
                    this.category_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.APNSOrBuilder
            public String getThreadId() {
                Object obj = this.threadId_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.threadId_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.APNSOrBuilder
            public ByteString getThreadIdBytes() {
                Object obj = this.threadId_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.threadId_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setThreadId(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.threadId_ = str;
                onChanged();
                return this;
            }

            public Builder clearThreadId() {
                this.threadId_ = APNS.getDefaultInstance().getThreadId();
                onChanged();
                return this;
            }

            public Builder setThreadIdBytes(ByteString byteString) {
                if (byteString != null) {
                    APNS.checkByteStringIsUtf8(byteString);
                    this.threadId_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.APNSOrBuilder
            public long getBadge() {
                return this.badge_;
            }

            public Builder setBadge(long j) {
                this.badge_ = j;
                onChanged();
                return this;
            }

            public Builder clearBadge() {
                this.badge_ = 0L;
                onChanged();
                return this;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.APNSOrBuilder
            public String getCustomContent() {
                Object obj = this.customContent_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.customContent_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.APNSOrBuilder
            public ByteString getCustomContentBytes() {
                Object obj = this.customContent_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.customContent_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setCustomContent(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.customContent_ = str;
                onChanged();
                return this;
            }

            public Builder clearCustomContent() {
                this.customContent_ = APNS.getDefaultInstance().getCustomContent();
                onChanged();
                return this;
            }

            public Builder setCustomContentBytes(ByteString byteString) {
                if (byteString != null) {
                    APNS.checkByteStringIsUtf8(byteString);
                    this.customContent_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.APNSOrBuilder
            public String getCollapseId() {
                Object obj = this.collapseId_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.collapseId_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.APNSOrBuilder
            public ByteString getCollapseIdBytes() {
                Object obj = this.collapseId_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.collapseId_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setCollapseId(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.collapseId_ = str;
                onChanged();
                return this;
            }

            public Builder clearCollapseId() {
                this.collapseId_ = APNS.getDefaultInstance().getCollapseId();
                onChanged();
                return this;
            }

            public Builder setCollapseIdBytes(ByteString byteString) {
                if (byteString != null) {
                    APNS.checkByteStringIsUtf8(byteString);
                    this.collapseId_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public final Builder setUnknownFields(UnknownFieldSet unknownFieldSet) {
                return (Builder) super.setUnknownFields(unknownFieldSet);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public final Builder mergeUnknownFields(UnknownFieldSet unknownFieldSet) {
                return (Builder) super.mergeUnknownFields(unknownFieldSet);
            }
        }

        public static APNS getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<APNS> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<APNS> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public APNS getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    public static final class Android extends GeneratedMessageV3 implements AndroidOrBuilder {
        public static final int AUDIO_URL_FIELD_NUMBER = 3;
        public static final int BADGE_FIELD_NUMBER = 10;
        public static final int BIG_IMAGE_URL_FIELD_NUMBER = 2;
        public static final int CHANNEL_FIELD_NUMBER = 100;
        public static final int CLICK_ACTION_PARAM_FIELD_NUMBER = 5;
        public static final int CLICK_ACTION_TYPE_FIELD_NUMBER = 4;
        public static final int CUSTOM_CONTENT_FIELD_NUMBER = 11;
        public static final int LIGHT_FIELD_NUMBER = 9;
        public static final int NOTIFY_ID_FIELD_NUMBER = 12;
        public static final int SMALL_IMAGE_URL_FIELD_NUMBER = 1;
        public static final int SOUND_FIELD_NUMBER = 6;
        public static final int SOUND_RESOURCE_FIELD_NUMBER = 7;
        public static final int VIBRATE_FIELD_NUMBER = 8;
        private static final long serialVersionUID = 0;
        private volatile Object audioUrl_;
        private long badge_;
        private volatile Object bigImageUrl_;
        private Channel channel_;
        private volatile Object clickActionParam_;
        private volatile Object clickActionType_;
        private volatile Object customContent_;
        private boolean light_;
        private byte memoizedIsInitialized;
        private int notifyId_;
        private volatile Object smallImageUrl_;
        private volatile Object soundResource_;
        private boolean sound_;
        private boolean vibrate_;
        private static final Android DEFAULT_INSTANCE = new Android();
        private static final Parser<Android> PARSER = new AbstractParser<Android>() { // from class: com.netease.push.proto.nano.ClientSdkgate.Android.1
            @Override // com.google.protobuf.Parser
            public Android parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return new Android(codedInputStream, extensionRegistryLite);
            }
        };

        private Android(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private Android() {
            this.memoizedIsInitialized = (byte) -1;
            this.smallImageUrl_ = "";
            this.bigImageUrl_ = "";
            this.audioUrl_ = "";
            this.clickActionType_ = "";
            this.clickActionParam_ = "";
            this.soundResource_ = "";
            this.customContent_ = "";
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private Android(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            this();
            if (extensionRegistryLite == null) {
                throw new NullPointerException();
            }
            UnknownFieldSet.Builder builderNewBuilder = UnknownFieldSet.newBuilder();
            boolean z = false;
            while (!z) {
                try {
                    try {
                        int tag = codedInputStream.readTag();
                        switch (tag) {
                            case 0:
                                z = true;
                            case 10:
                                this.smallImageUrl_ = codedInputStream.readStringRequireUtf8();
                            case 18:
                                this.bigImageUrl_ = codedInputStream.readStringRequireUtf8();
                            case 26:
                                this.audioUrl_ = codedInputStream.readStringRequireUtf8();
                            case 34:
                                this.clickActionType_ = codedInputStream.readStringRequireUtf8();
                            case 42:
                                this.clickActionParam_ = codedInputStream.readStringRequireUtf8();
                            case 48:
                                this.sound_ = codedInputStream.readBool();
                            case 58:
                                this.soundResource_ = codedInputStream.readStringRequireUtf8();
                            case 64:
                                this.vibrate_ = codedInputStream.readBool();
                            case 72:
                                this.light_ = codedInputStream.readBool();
                            case 80:
                                this.badge_ = codedInputStream.readInt64();
                            case 90:
                                this.customContent_ = codedInputStream.readStringRequireUtf8();
                            case 96:
                                this.notifyId_ = codedInputStream.readInt32();
                            case 802:
                                Channel.Builder builder = this.channel_ != null ? this.channel_.toBuilder() : null;
                                this.channel_ = (Channel) codedInputStream.readMessage(Channel.parser(), extensionRegistryLite);
                                if (builder != null) {
                                    builder.mergeFrom(this.channel_);
                                    this.channel_ = builder.buildPartial();
                                }
                            default:
                                if (!parseUnknownField(codedInputStream, builderNewBuilder, extensionRegistryLite, tag)) {
                                    z = true;
                                }
                        }
                    } catch (InvalidProtocolBufferException e) {
                        throw e.setUnfinishedMessage(this);
                    } catch (IOException e2) {
                        throw new InvalidProtocolBufferException(e2).setUnfinishedMessage(this);
                    }
                } finally {
                    this.unknownFields = builderNewBuilder.build();
                    makeExtensionsImmutable();
                }
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return ClientSdkgate.internal_static_proto_sdkgate_Android_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return ClientSdkgate.internal_static_proto_sdkgate_Android_fieldAccessorTable.ensureFieldAccessorsInitialized(Android.class, Builder.class);
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
        public String getSmallImageUrl() {
            Object obj = this.smallImageUrl_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.smallImageUrl_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
        public ByteString getSmallImageUrlBytes() {
            Object obj = this.smallImageUrl_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.smallImageUrl_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
        public String getBigImageUrl() {
            Object obj = this.bigImageUrl_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.bigImageUrl_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
        public ByteString getBigImageUrlBytes() {
            Object obj = this.bigImageUrl_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.bigImageUrl_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
        public String getAudioUrl() {
            Object obj = this.audioUrl_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.audioUrl_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
        public ByteString getAudioUrlBytes() {
            Object obj = this.audioUrl_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.audioUrl_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
        public String getClickActionType() {
            Object obj = this.clickActionType_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.clickActionType_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
        public ByteString getClickActionTypeBytes() {
            Object obj = this.clickActionType_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.clickActionType_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
        public String getClickActionParam() {
            Object obj = this.clickActionParam_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.clickActionParam_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
        public ByteString getClickActionParamBytes() {
            Object obj = this.clickActionParam_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.clickActionParam_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
        public boolean getSound() {
            return this.sound_;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
        public String getSoundResource() {
            Object obj = this.soundResource_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.soundResource_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
        public ByteString getSoundResourceBytes() {
            Object obj = this.soundResource_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.soundResource_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
        public boolean getVibrate() {
            return this.vibrate_;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
        public boolean getLight() {
            return this.light_;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
        public long getBadge() {
            return this.badge_;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
        public String getCustomContent() {
            Object obj = this.customContent_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.customContent_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
        public ByteString getCustomContentBytes() {
            Object obj = this.customContent_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.customContent_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
        public int getNotifyId() {
            return this.notifyId_;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
        public boolean hasChannel() {
            return this.channel_ != null;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
        public Channel getChannel() {
            Channel channel = this.channel_;
            return channel == null ? Channel.getDefaultInstance() : channel;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
        public ChannelOrBuilder getChannelOrBuilder() {
            return getChannel();
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            if (b == 1) {
                return true;
            }
            if (b == 0) {
                return false;
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            if (!getSmallImageUrlBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 1, this.smallImageUrl_);
            }
            if (!getBigImageUrlBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 2, this.bigImageUrl_);
            }
            if (!getAudioUrlBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 3, this.audioUrl_);
            }
            if (!getClickActionTypeBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 4, this.clickActionType_);
            }
            if (!getClickActionParamBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 5, this.clickActionParam_);
            }
            boolean z = this.sound_;
            if (z) {
                codedOutputStream.writeBool(6, z);
            }
            if (!getSoundResourceBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 7, this.soundResource_);
            }
            boolean z2 = this.vibrate_;
            if (z2) {
                codedOutputStream.writeBool(8, z2);
            }
            boolean z3 = this.light_;
            if (z3) {
                codedOutputStream.writeBool(9, z3);
            }
            long j = this.badge_;
            if (j != 0) {
                codedOutputStream.writeInt64(10, j);
            }
            if (!getCustomContentBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 11, this.customContent_);
            }
            int i = this.notifyId_;
            if (i != 0) {
                codedOutputStream.writeInt32(12, i);
            }
            if (this.channel_ != null) {
                codedOutputStream.writeMessage(100, getChannel());
            }
            this.unknownFields.writeTo(codedOutputStream);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public int getSerializedSize() {
            int i = this.memoizedSize;
            if (i != -1) {
                return i;
            }
            int iComputeStringSize = getSmallImageUrlBytes().isEmpty() ? 0 : 0 + GeneratedMessageV3.computeStringSize(1, this.smallImageUrl_);
            if (!getBigImageUrlBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(2, this.bigImageUrl_);
            }
            if (!getAudioUrlBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(3, this.audioUrl_);
            }
            if (!getClickActionTypeBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(4, this.clickActionType_);
            }
            if (!getClickActionParamBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(5, this.clickActionParam_);
            }
            boolean z = this.sound_;
            if (z) {
                iComputeStringSize += CodedOutputStream.computeBoolSize(6, z);
            }
            if (!getSoundResourceBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(7, this.soundResource_);
            }
            boolean z2 = this.vibrate_;
            if (z2) {
                iComputeStringSize += CodedOutputStream.computeBoolSize(8, z2);
            }
            boolean z3 = this.light_;
            if (z3) {
                iComputeStringSize += CodedOutputStream.computeBoolSize(9, z3);
            }
            long j = this.badge_;
            if (j != 0) {
                iComputeStringSize += CodedOutputStream.computeInt64Size(10, j);
            }
            if (!getCustomContentBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(11, this.customContent_);
            }
            int i2 = this.notifyId_;
            if (i2 != 0) {
                iComputeStringSize += CodedOutputStream.computeInt32Size(12, i2);
            }
            if (this.channel_ != null) {
                iComputeStringSize += CodedOutputStream.computeMessageSize(100, getChannel());
            }
            int serializedSize = iComputeStringSize + this.unknownFields.getSerializedSize();
            this.memoizedSize = serializedSize;
            return serializedSize;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Android)) {
                return super.equals(obj);
            }
            Android android2 = (Android) obj;
            if (getSmallImageUrl().equals(android2.getSmallImageUrl()) && getBigImageUrl().equals(android2.getBigImageUrl()) && getAudioUrl().equals(android2.getAudioUrl()) && getClickActionType().equals(android2.getClickActionType()) && getClickActionParam().equals(android2.getClickActionParam()) && getSound() == android2.getSound() && getSoundResource().equals(android2.getSoundResource()) && getVibrate() == android2.getVibrate() && getLight() == android2.getLight() && getBadge() == android2.getBadge() && getCustomContent().equals(android2.getCustomContent()) && getNotifyId() == android2.getNotifyId() && hasChannel() == android2.hasChannel()) {
                return (!hasChannel() || getChannel().equals(android2.getChannel())) && this.unknownFields.equals(android2.unknownFields);
            }
            return false;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int iHashCode = ((((((((((((((((((((((((((((((((((((((((((((((((779 + getDescriptor().hashCode()) * 37) + 1) * 53) + getSmallImageUrl().hashCode()) * 37) + 2) * 53) + getBigImageUrl().hashCode()) * 37) + 3) * 53) + getAudioUrl().hashCode()) * 37) + 4) * 53) + getClickActionType().hashCode()) * 37) + 5) * 53) + getClickActionParam().hashCode()) * 37) + 6) * 53) + Internal.hashBoolean(getSound())) * 37) + 7) * 53) + getSoundResource().hashCode()) * 37) + 8) * 53) + Internal.hashBoolean(getVibrate())) * 37) + 9) * 53) + Internal.hashBoolean(getLight())) * 37) + 10) * 53) + Internal.hashLong(getBadge())) * 37) + 11) * 53) + getCustomContent().hashCode()) * 37) + 12) * 53) + getNotifyId();
            if (hasChannel()) {
                iHashCode = (((iHashCode * 37) + 100) * 53) + getChannel().hashCode();
            }
            int iHashCode2 = (iHashCode * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = iHashCode2;
            return iHashCode2;
        }

        public static Android parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString);
        }

        public static Android parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString, extensionRegistryLite);
        }

        public static Android parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(bArr);
        }

        public static Android parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(bArr, extensionRegistryLite);
        }

        public static Android parseFrom(InputStream inputStream) throws IOException {
            return (Android) GeneratedMessageV3.parseWithIOException(PARSER, inputStream);
        }

        public static Android parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (Android) GeneratedMessageV3.parseWithIOException(PARSER, inputStream, extensionRegistryLite);
        }

        public static Android parseDelimitedFrom(InputStream inputStream) throws IOException {
            return (Android) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, inputStream);
        }

        public static Android parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (Android) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, inputStream, extensionRegistryLite);
        }

        public static Android parseFrom(CodedInputStream codedInputStream) throws IOException {
            return (Android) GeneratedMessageV3.parseWithIOException(PARSER, codedInputStream);
        }

        public static Android parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (Android) GeneratedMessageV3.parseWithIOException(PARSER, codedInputStream, extensionRegistryLite);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(Android android2) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(android2);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Builder newBuilderForType(GeneratedMessageV3.BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements AndroidOrBuilder {
            private Object audioUrl_;
            private long badge_;
            private Object bigImageUrl_;
            private SingleFieldBuilderV3<Channel, Channel.Builder, ChannelOrBuilder> channelBuilder_;
            private Channel channel_;
            private Object clickActionParam_;
            private Object clickActionType_;
            private Object customContent_;
            private boolean light_;
            private int notifyId_;
            private Object smallImageUrl_;
            private Object soundResource_;
            private boolean sound_;
            private boolean vibrate_;

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return ClientSdkgate.internal_static_proto_sdkgate_Android_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return ClientSdkgate.internal_static_proto_sdkgate_Android_fieldAccessorTable.ensureFieldAccessorsInitialized(Android.class, Builder.class);
            }

            private Builder() {
                this.smallImageUrl_ = "";
                this.bigImageUrl_ = "";
                this.audioUrl_ = "";
                this.clickActionType_ = "";
                this.clickActionParam_ = "";
                this.soundResource_ = "";
                this.customContent_ = "";
                maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessageV3.BuilderParent builderParent) {
                super(builderParent);
                this.smallImageUrl_ = "";
                this.bigImageUrl_ = "";
                this.audioUrl_ = "";
                this.clickActionType_ = "";
                this.clickActionParam_ = "";
                this.soundResource_ = "";
                this.customContent_ = "";
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                boolean unused = Android.alwaysUseFieldBuilders;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                this.smallImageUrl_ = "";
                this.bigImageUrl_ = "";
                this.audioUrl_ = "";
                this.clickActionType_ = "";
                this.clickActionParam_ = "";
                this.sound_ = false;
                this.soundResource_ = "";
                this.vibrate_ = false;
                this.light_ = false;
                this.badge_ = 0L;
                this.customContent_ = "";
                this.notifyId_ = 0;
                if (this.channelBuilder_ == null) {
                    this.channel_ = null;
                } else {
                    this.channel_ = null;
                    this.channelBuilder_ = null;
                }
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return ClientSdkgate.internal_static_proto_sdkgate_Android_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public Android getDefaultInstanceForType() {
                return Android.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Android build() {
                Android androidBuildPartial = buildPartial();
                if (androidBuildPartial.isInitialized()) {
                    return androidBuildPartial;
                }
                throw newUninitializedMessageException((Message) androidBuildPartial);
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Android buildPartial() {
                Android android2 = new Android(this);
                android2.smallImageUrl_ = this.smallImageUrl_;
                android2.bigImageUrl_ = this.bigImageUrl_;
                android2.audioUrl_ = this.audioUrl_;
                android2.clickActionType_ = this.clickActionType_;
                android2.clickActionParam_ = this.clickActionParam_;
                android2.sound_ = this.sound_;
                android2.soundResource_ = this.soundResource_;
                android2.vibrate_ = this.vibrate_;
                android2.light_ = this.light_;
                android2.badge_ = this.badge_;
                android2.customContent_ = this.customContent_;
                android2.notifyId_ = this.notifyId_;
                SingleFieldBuilderV3<Channel, Channel.Builder, ChannelOrBuilder> singleFieldBuilderV3 = this.channelBuilder_;
                if (singleFieldBuilderV3 == null) {
                    android2.channel_ = this.channel_;
                } else {
                    android2.channel_ = (Channel) singleFieldBuilderV3.build();
                }
                onBuilt();
                return android2;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder
            /* renamed from: clone */
            public Builder mo375clone() {
                return (Builder) super.mo375clone();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setField(Descriptors.FieldDescriptor fieldDescriptor, Object obj) {
                return (Builder) super.setField(fieldDescriptor, obj);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder clearField(Descriptors.FieldDescriptor fieldDescriptor) {
                return (Builder) super.clearField(fieldDescriptor);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder clearOneof(Descriptors.OneofDescriptor oneofDescriptor) {
                return (Builder) super.clearOneof(oneofDescriptor);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, int i, Object obj) {
                return (Builder) super.setRepeatedField(fieldDescriptor, i, obj);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder addRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, Object obj) {
                return (Builder) super.addRepeatedField(fieldDescriptor, obj);
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(Message message) {
                if (message instanceof Android) {
                    return mergeFrom((Android) message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder mergeFrom(Android android2) {
                if (android2 == Android.getDefaultInstance()) {
                    return this;
                }
                if (!android2.getSmallImageUrl().isEmpty()) {
                    this.smallImageUrl_ = android2.smallImageUrl_;
                    onChanged();
                }
                if (!android2.getBigImageUrl().isEmpty()) {
                    this.bigImageUrl_ = android2.bigImageUrl_;
                    onChanged();
                }
                if (!android2.getAudioUrl().isEmpty()) {
                    this.audioUrl_ = android2.audioUrl_;
                    onChanged();
                }
                if (!android2.getClickActionType().isEmpty()) {
                    this.clickActionType_ = android2.clickActionType_;
                    onChanged();
                }
                if (!android2.getClickActionParam().isEmpty()) {
                    this.clickActionParam_ = android2.clickActionParam_;
                    onChanged();
                }
                if (android2.getSound()) {
                    setSound(android2.getSound());
                }
                if (!android2.getSoundResource().isEmpty()) {
                    this.soundResource_ = android2.soundResource_;
                    onChanged();
                }
                if (android2.getVibrate()) {
                    setVibrate(android2.getVibrate());
                }
                if (android2.getLight()) {
                    setLight(android2.getLight());
                }
                if (android2.getBadge() != 0) {
                    setBadge(android2.getBadge());
                }
                if (!android2.getCustomContent().isEmpty()) {
                    this.customContent_ = android2.customContent_;
                    onChanged();
                }
                if (android2.getNotifyId() != 0) {
                    setNotifyId(android2.getNotifyId());
                }
                if (android2.hasChannel()) {
                    mergeChannel(android2.getChannel());
                }
                mergeUnknownFields(android2.unknownFields);
                onChanged();
                return this;
            }

            /* JADX WARN: Removed duplicated region for block: B:16:0x0023  */
            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public com.netease.push.proto.nano.ClientSdkgate.Android.Builder mergeFrom(com.google.protobuf.CodedInputStream r3, com.google.protobuf.ExtensionRegistryLite r4) throws java.lang.Throwable {
                /*
                    r2 = this;
                    r0 = 0
                    com.google.protobuf.Parser r1 = com.netease.push.proto.nano.ClientSdkgate.Android.access$19900()     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    java.lang.Object r3 = r1.parsePartialFrom(r3, r4)     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    com.netease.push.proto.nano.ClientSdkgate$Android r3 = (com.netease.push.proto.nano.ClientSdkgate.Android) r3     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    if (r3 == 0) goto L10
                    r2.mergeFrom(r3)
                L10:
                    return r2
                L11:
                    r3 = move-exception
                    goto L21
                L13:
                    r3 = move-exception
                    com.google.protobuf.MessageLite r4 = r3.getUnfinishedMessage()     // Catch: java.lang.Throwable -> L11
                    com.netease.push.proto.nano.ClientSdkgate$Android r4 = (com.netease.push.proto.nano.ClientSdkgate.Android) r4     // Catch: java.lang.Throwable -> L11
                    java.io.IOException r3 = r3.unwrapIOException()     // Catch: java.lang.Throwable -> L1f
                    throw r3     // Catch: java.lang.Throwable -> L1f
                L1f:
                    r3 = move-exception
                    r0 = r4
                L21:
                    if (r0 == 0) goto L26
                    r2.mergeFrom(r0)
                L26:
                    throw r3
                */
                throw new UnsupportedOperationException("Method not decompiled: com.netease.push.proto.nano.ClientSdkgate.Android.Builder.mergeFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):com.netease.push.proto.nano.ClientSdkgate$Android$Builder");
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
            public String getSmallImageUrl() {
                Object obj = this.smallImageUrl_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.smallImageUrl_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
            public ByteString getSmallImageUrlBytes() {
                Object obj = this.smallImageUrl_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.smallImageUrl_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setSmallImageUrl(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.smallImageUrl_ = str;
                onChanged();
                return this;
            }

            public Builder clearSmallImageUrl() {
                this.smallImageUrl_ = Android.getDefaultInstance().getSmallImageUrl();
                onChanged();
                return this;
            }

            public Builder setSmallImageUrlBytes(ByteString byteString) {
                if (byteString != null) {
                    Android.checkByteStringIsUtf8(byteString);
                    this.smallImageUrl_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
            public String getBigImageUrl() {
                Object obj = this.bigImageUrl_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.bigImageUrl_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
            public ByteString getBigImageUrlBytes() {
                Object obj = this.bigImageUrl_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.bigImageUrl_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setBigImageUrl(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.bigImageUrl_ = str;
                onChanged();
                return this;
            }

            public Builder clearBigImageUrl() {
                this.bigImageUrl_ = Android.getDefaultInstance().getBigImageUrl();
                onChanged();
                return this;
            }

            public Builder setBigImageUrlBytes(ByteString byteString) {
                if (byteString != null) {
                    Android.checkByteStringIsUtf8(byteString);
                    this.bigImageUrl_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
            public String getAudioUrl() {
                Object obj = this.audioUrl_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.audioUrl_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
            public ByteString getAudioUrlBytes() {
                Object obj = this.audioUrl_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.audioUrl_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setAudioUrl(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.audioUrl_ = str;
                onChanged();
                return this;
            }

            public Builder clearAudioUrl() {
                this.audioUrl_ = Android.getDefaultInstance().getAudioUrl();
                onChanged();
                return this;
            }

            public Builder setAudioUrlBytes(ByteString byteString) {
                if (byteString != null) {
                    Android.checkByteStringIsUtf8(byteString);
                    this.audioUrl_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
            public String getClickActionType() {
                Object obj = this.clickActionType_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.clickActionType_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
            public ByteString getClickActionTypeBytes() {
                Object obj = this.clickActionType_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.clickActionType_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setClickActionType(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.clickActionType_ = str;
                onChanged();
                return this;
            }

            public Builder clearClickActionType() {
                this.clickActionType_ = Android.getDefaultInstance().getClickActionType();
                onChanged();
                return this;
            }

            public Builder setClickActionTypeBytes(ByteString byteString) {
                if (byteString != null) {
                    Android.checkByteStringIsUtf8(byteString);
                    this.clickActionType_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
            public String getClickActionParam() {
                Object obj = this.clickActionParam_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.clickActionParam_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
            public ByteString getClickActionParamBytes() {
                Object obj = this.clickActionParam_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.clickActionParam_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setClickActionParam(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.clickActionParam_ = str;
                onChanged();
                return this;
            }

            public Builder clearClickActionParam() {
                this.clickActionParam_ = Android.getDefaultInstance().getClickActionParam();
                onChanged();
                return this;
            }

            public Builder setClickActionParamBytes(ByteString byteString) {
                if (byteString != null) {
                    Android.checkByteStringIsUtf8(byteString);
                    this.clickActionParam_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
            public boolean getSound() {
                return this.sound_;
            }

            public Builder setSound(boolean z) {
                this.sound_ = z;
                onChanged();
                return this;
            }

            public Builder clearSound() {
                this.sound_ = false;
                onChanged();
                return this;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
            public String getSoundResource() {
                Object obj = this.soundResource_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.soundResource_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
            public ByteString getSoundResourceBytes() {
                Object obj = this.soundResource_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.soundResource_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setSoundResource(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.soundResource_ = str;
                onChanged();
                return this;
            }

            public Builder clearSoundResource() {
                this.soundResource_ = Android.getDefaultInstance().getSoundResource();
                onChanged();
                return this;
            }

            public Builder setSoundResourceBytes(ByteString byteString) {
                if (byteString != null) {
                    Android.checkByteStringIsUtf8(byteString);
                    this.soundResource_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
            public boolean getVibrate() {
                return this.vibrate_;
            }

            public Builder setVibrate(boolean z) {
                this.vibrate_ = z;
                onChanged();
                return this;
            }

            public Builder clearVibrate() {
                this.vibrate_ = false;
                onChanged();
                return this;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
            public boolean getLight() {
                return this.light_;
            }

            public Builder setLight(boolean z) {
                this.light_ = z;
                onChanged();
                return this;
            }

            public Builder clearLight() {
                this.light_ = false;
                onChanged();
                return this;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
            public long getBadge() {
                return this.badge_;
            }

            public Builder setBadge(long j) {
                this.badge_ = j;
                onChanged();
                return this;
            }

            public Builder clearBadge() {
                this.badge_ = 0L;
                onChanged();
                return this;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
            public String getCustomContent() {
                Object obj = this.customContent_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.customContent_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
            public ByteString getCustomContentBytes() {
                Object obj = this.customContent_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.customContent_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setCustomContent(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.customContent_ = str;
                onChanged();
                return this;
            }

            public Builder clearCustomContent() {
                this.customContent_ = Android.getDefaultInstance().getCustomContent();
                onChanged();
                return this;
            }

            public Builder setCustomContentBytes(ByteString byteString) {
                if (byteString != null) {
                    Android.checkByteStringIsUtf8(byteString);
                    this.customContent_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
            public int getNotifyId() {
                return this.notifyId_;
            }

            public Builder setNotifyId(int i) {
                this.notifyId_ = i;
                onChanged();
                return this;
            }

            public Builder clearNotifyId() {
                this.notifyId_ = 0;
                onChanged();
                return this;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
            public boolean hasChannel() {
                return (this.channelBuilder_ == null && this.channel_ == null) ? false : true;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
            public Channel getChannel() {
                SingleFieldBuilderV3<Channel, Channel.Builder, ChannelOrBuilder> singleFieldBuilderV3 = this.channelBuilder_;
                if (singleFieldBuilderV3 == null) {
                    Channel channel = this.channel_;
                    return channel == null ? Channel.getDefaultInstance() : channel;
                }
                return (Channel) singleFieldBuilderV3.getMessage();
            }

            public Builder setChannel(Channel channel) {
                SingleFieldBuilderV3<Channel, Channel.Builder, ChannelOrBuilder> singleFieldBuilderV3 = this.channelBuilder_;
                if (singleFieldBuilderV3 != null) {
                    singleFieldBuilderV3.setMessage(channel);
                } else {
                    if (channel == null) {
                        throw new NullPointerException();
                    }
                    this.channel_ = channel;
                    onChanged();
                }
                return this;
            }

            public Builder setChannel(Channel.Builder builder) {
                SingleFieldBuilderV3<Channel, Channel.Builder, ChannelOrBuilder> singleFieldBuilderV3 = this.channelBuilder_;
                if (singleFieldBuilderV3 == null) {
                    this.channel_ = builder.build();
                    onChanged();
                } else {
                    singleFieldBuilderV3.setMessage(builder.build());
                }
                return this;
            }

            public Builder mergeChannel(Channel channel) {
                SingleFieldBuilderV3<Channel, Channel.Builder, ChannelOrBuilder> singleFieldBuilderV3 = this.channelBuilder_;
                if (singleFieldBuilderV3 == null) {
                    Channel channel2 = this.channel_;
                    if (channel2 != null) {
                        this.channel_ = Channel.newBuilder(channel2).mergeFrom(channel).buildPartial();
                    } else {
                        this.channel_ = channel;
                    }
                    onChanged();
                } else {
                    singleFieldBuilderV3.mergeFrom(channel);
                }
                return this;
            }

            public Builder clearChannel() {
                if (this.channelBuilder_ == null) {
                    this.channel_ = null;
                    onChanged();
                } else {
                    this.channel_ = null;
                    this.channelBuilder_ = null;
                }
                return this;
            }

            public Channel.Builder getChannelBuilder() {
                onChanged();
                return (Channel.Builder) getChannelFieldBuilder().getBuilder();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.AndroidOrBuilder
            public ChannelOrBuilder getChannelOrBuilder() {
                SingleFieldBuilderV3<Channel, Channel.Builder, ChannelOrBuilder> singleFieldBuilderV3 = this.channelBuilder_;
                if (singleFieldBuilderV3 != null) {
                    return (ChannelOrBuilder) singleFieldBuilderV3.getMessageOrBuilder();
                }
                Channel channel = this.channel_;
                return channel == null ? Channel.getDefaultInstance() : channel;
            }

            private SingleFieldBuilderV3<Channel, Channel.Builder, ChannelOrBuilder> getChannelFieldBuilder() {
                if (this.channelBuilder_ == null) {
                    this.channelBuilder_ = new SingleFieldBuilderV3<>(getChannel(), getParentForChildren(), isClean());
                    this.channel_ = null;
                }
                return this.channelBuilder_;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public final Builder setUnknownFields(UnknownFieldSet unknownFieldSet) {
                return (Builder) super.setUnknownFields(unknownFieldSet);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public final Builder mergeUnknownFields(UnknownFieldSet unknownFieldSet) {
                return (Builder) super.mergeUnknownFields(unknownFieldSet);
            }
        }

        public static Android getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<Android> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<Android> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public Android getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    public static final class Channel extends GeneratedMessageV3 implements ChannelOrBuilder {
        public static final int CHANNEL_GROUP_ID_FIELD_NUMBER = 3;
        public static final int CHANNEL_GROUP_NAME_FIELD_NUMBER = 4;
        public static final int CHANNEL_ID_FIELD_NUMBER = 1;
        public static final int CHANNEL_NAME_FIELD_NUMBER = 2;
        private static final Channel DEFAULT_INSTANCE = new Channel();
        private static final Parser<Channel> PARSER = new AbstractParser<Channel>() { // from class: com.netease.push.proto.nano.ClientSdkgate.Channel.1
            @Override // com.google.protobuf.Parser
            public Channel parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return new Channel(codedInputStream, extensionRegistryLite);
            }
        };
        private static final long serialVersionUID = 0;
        private volatile Object channelGroupId_;
        private volatile Object channelGroupName_;
        private volatile Object channelId_;
        private volatile Object channelName_;
        private byte memoizedIsInitialized;

        private Channel(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private Channel() {
            this.memoizedIsInitialized = (byte) -1;
            this.channelId_ = "";
            this.channelName_ = "";
            this.channelGroupId_ = "";
            this.channelGroupName_ = "";
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private Channel(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            this();
            if (extensionRegistryLite == null) {
                throw new NullPointerException();
            }
            UnknownFieldSet.Builder builderNewBuilder = UnknownFieldSet.newBuilder();
            boolean z = false;
            while (!z) {
                try {
                    try {
                        int tag = codedInputStream.readTag();
                        if (tag != 0) {
                            if (tag == 10) {
                                this.channelId_ = codedInputStream.readStringRequireUtf8();
                            } else if (tag == 18) {
                                this.channelName_ = codedInputStream.readStringRequireUtf8();
                            } else if (tag == 26) {
                                this.channelGroupId_ = codedInputStream.readStringRequireUtf8();
                            } else if (tag == 34) {
                                this.channelGroupName_ = codedInputStream.readStringRequireUtf8();
                            } else if (!parseUnknownField(codedInputStream, builderNewBuilder, extensionRegistryLite, tag)) {
                            }
                        }
                        z = true;
                    } catch (InvalidProtocolBufferException e) {
                        throw e.setUnfinishedMessage(this);
                    } catch (IOException e2) {
                        throw new InvalidProtocolBufferException(e2).setUnfinishedMessage(this);
                    }
                } finally {
                    this.unknownFields = builderNewBuilder.build();
                    makeExtensionsImmutable();
                }
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return ClientSdkgate.internal_static_proto_sdkgate_Channel_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return ClientSdkgate.internal_static_proto_sdkgate_Channel_fieldAccessorTable.ensureFieldAccessorsInitialized(Channel.class, Builder.class);
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.ChannelOrBuilder
        public String getChannelId() {
            Object obj = this.channelId_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.channelId_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.ChannelOrBuilder
        public ByteString getChannelIdBytes() {
            Object obj = this.channelId_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.channelId_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.ChannelOrBuilder
        public String getChannelName() {
            Object obj = this.channelName_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.channelName_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.ChannelOrBuilder
        public ByteString getChannelNameBytes() {
            Object obj = this.channelName_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.channelName_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.ChannelOrBuilder
        public String getChannelGroupId() {
            Object obj = this.channelGroupId_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.channelGroupId_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.ChannelOrBuilder
        public ByteString getChannelGroupIdBytes() {
            Object obj = this.channelGroupId_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.channelGroupId_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.ChannelOrBuilder
        public String getChannelGroupName() {
            Object obj = this.channelGroupName_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.channelGroupName_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.ChannelOrBuilder
        public ByteString getChannelGroupNameBytes() {
            Object obj = this.channelGroupName_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.channelGroupName_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            if (b == 1) {
                return true;
            }
            if (b == 0) {
                return false;
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            if (!getChannelIdBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 1, this.channelId_);
            }
            if (!getChannelNameBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 2, this.channelName_);
            }
            if (!getChannelGroupIdBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 3, this.channelGroupId_);
            }
            if (!getChannelGroupNameBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 4, this.channelGroupName_);
            }
            this.unknownFields.writeTo(codedOutputStream);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public int getSerializedSize() {
            int i = this.memoizedSize;
            if (i != -1) {
                return i;
            }
            int iComputeStringSize = getChannelIdBytes().isEmpty() ? 0 : 0 + GeneratedMessageV3.computeStringSize(1, this.channelId_);
            if (!getChannelNameBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(2, this.channelName_);
            }
            if (!getChannelGroupIdBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(3, this.channelGroupId_);
            }
            if (!getChannelGroupNameBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(4, this.channelGroupName_);
            }
            int serializedSize = iComputeStringSize + this.unknownFields.getSerializedSize();
            this.memoizedSize = serializedSize;
            return serializedSize;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Channel)) {
                return super.equals(obj);
            }
            Channel channel = (Channel) obj;
            return getChannelId().equals(channel.getChannelId()) && getChannelName().equals(channel.getChannelName()) && getChannelGroupId().equals(channel.getChannelGroupId()) && getChannelGroupName().equals(channel.getChannelGroupName()) && this.unknownFields.equals(channel.unknownFields);
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int iHashCode = ((((((((((((((((((779 + getDescriptor().hashCode()) * 37) + 1) * 53) + getChannelId().hashCode()) * 37) + 2) * 53) + getChannelName().hashCode()) * 37) + 3) * 53) + getChannelGroupId().hashCode()) * 37) + 4) * 53) + getChannelGroupName().hashCode()) * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = iHashCode;
            return iHashCode;
        }

        public static Channel parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString);
        }

        public static Channel parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString, extensionRegistryLite);
        }

        public static Channel parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(bArr);
        }

        public static Channel parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(bArr, extensionRegistryLite);
        }

        public static Channel parseFrom(InputStream inputStream) throws IOException {
            return (Channel) GeneratedMessageV3.parseWithIOException(PARSER, inputStream);
        }

        public static Channel parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (Channel) GeneratedMessageV3.parseWithIOException(PARSER, inputStream, extensionRegistryLite);
        }

        public static Channel parseDelimitedFrom(InputStream inputStream) throws IOException {
            return (Channel) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, inputStream);
        }

        public static Channel parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (Channel) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, inputStream, extensionRegistryLite);
        }

        public static Channel parseFrom(CodedInputStream codedInputStream) throws IOException {
            return (Channel) GeneratedMessageV3.parseWithIOException(PARSER, codedInputStream);
        }

        public static Channel parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (Channel) GeneratedMessageV3.parseWithIOException(PARSER, codedInputStream, extensionRegistryLite);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(Channel channel) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(channel);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Builder newBuilderForType(GeneratedMessageV3.BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements ChannelOrBuilder {
            private Object channelGroupId_;
            private Object channelGroupName_;
            private Object channelId_;
            private Object channelName_;

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return ClientSdkgate.internal_static_proto_sdkgate_Channel_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return ClientSdkgate.internal_static_proto_sdkgate_Channel_fieldAccessorTable.ensureFieldAccessorsInitialized(Channel.class, Builder.class);
            }

            private Builder() {
                this.channelId_ = "";
                this.channelName_ = "";
                this.channelGroupId_ = "";
                this.channelGroupName_ = "";
                maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessageV3.BuilderParent builderParent) {
                super(builderParent);
                this.channelId_ = "";
                this.channelName_ = "";
                this.channelGroupId_ = "";
                this.channelGroupName_ = "";
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                boolean unused = Channel.alwaysUseFieldBuilders;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                this.channelId_ = "";
                this.channelName_ = "";
                this.channelGroupId_ = "";
                this.channelGroupName_ = "";
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return ClientSdkgate.internal_static_proto_sdkgate_Channel_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public Channel getDefaultInstanceForType() {
                return Channel.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Channel build() {
                Channel channelBuildPartial = buildPartial();
                if (channelBuildPartial.isInitialized()) {
                    return channelBuildPartial;
                }
                throw newUninitializedMessageException((Message) channelBuildPartial);
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Channel buildPartial() {
                Channel channel = new Channel(this);
                channel.channelId_ = this.channelId_;
                channel.channelName_ = this.channelName_;
                channel.channelGroupId_ = this.channelGroupId_;
                channel.channelGroupName_ = this.channelGroupName_;
                onBuilt();
                return channel;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder
            /* renamed from: clone */
            public Builder mo375clone() {
                return (Builder) super.mo375clone();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setField(Descriptors.FieldDescriptor fieldDescriptor, Object obj) {
                return (Builder) super.setField(fieldDescriptor, obj);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder clearField(Descriptors.FieldDescriptor fieldDescriptor) {
                return (Builder) super.clearField(fieldDescriptor);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder clearOneof(Descriptors.OneofDescriptor oneofDescriptor) {
                return (Builder) super.clearOneof(oneofDescriptor);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, int i, Object obj) {
                return (Builder) super.setRepeatedField(fieldDescriptor, i, obj);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder addRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, Object obj) {
                return (Builder) super.addRepeatedField(fieldDescriptor, obj);
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(Message message) {
                if (message instanceof Channel) {
                    return mergeFrom((Channel) message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder mergeFrom(Channel channel) {
                if (channel == Channel.getDefaultInstance()) {
                    return this;
                }
                if (!channel.getChannelId().isEmpty()) {
                    this.channelId_ = channel.channelId_;
                    onChanged();
                }
                if (!channel.getChannelName().isEmpty()) {
                    this.channelName_ = channel.channelName_;
                    onChanged();
                }
                if (!channel.getChannelGroupId().isEmpty()) {
                    this.channelGroupId_ = channel.channelGroupId_;
                    onChanged();
                }
                if (!channel.getChannelGroupName().isEmpty()) {
                    this.channelGroupName_ = channel.channelGroupName_;
                    onChanged();
                }
                mergeUnknownFields(channel.unknownFields);
                onChanged();
                return this;
            }

            /* JADX WARN: Removed duplicated region for block: B:16:0x0023  */
            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public com.netease.push.proto.nano.ClientSdkgate.Channel.Builder mergeFrom(com.google.protobuf.CodedInputStream r3, com.google.protobuf.ExtensionRegistryLite r4) throws java.lang.Throwable {
                /*
                    r2 = this;
                    r0 = 0
                    com.google.protobuf.Parser r1 = com.netease.push.proto.nano.ClientSdkgate.Channel.access$21900()     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    java.lang.Object r3 = r1.parsePartialFrom(r3, r4)     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    com.netease.push.proto.nano.ClientSdkgate$Channel r3 = (com.netease.push.proto.nano.ClientSdkgate.Channel) r3     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    if (r3 == 0) goto L10
                    r2.mergeFrom(r3)
                L10:
                    return r2
                L11:
                    r3 = move-exception
                    goto L21
                L13:
                    r3 = move-exception
                    com.google.protobuf.MessageLite r4 = r3.getUnfinishedMessage()     // Catch: java.lang.Throwable -> L11
                    com.netease.push.proto.nano.ClientSdkgate$Channel r4 = (com.netease.push.proto.nano.ClientSdkgate.Channel) r4     // Catch: java.lang.Throwable -> L11
                    java.io.IOException r3 = r3.unwrapIOException()     // Catch: java.lang.Throwable -> L1f
                    throw r3     // Catch: java.lang.Throwable -> L1f
                L1f:
                    r3 = move-exception
                    r0 = r4
                L21:
                    if (r0 == 0) goto L26
                    r2.mergeFrom(r0)
                L26:
                    throw r3
                */
                throw new UnsupportedOperationException("Method not decompiled: com.netease.push.proto.nano.ClientSdkgate.Channel.Builder.mergeFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):com.netease.push.proto.nano.ClientSdkgate$Channel$Builder");
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.ChannelOrBuilder
            public String getChannelId() {
                Object obj = this.channelId_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.channelId_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.ChannelOrBuilder
            public ByteString getChannelIdBytes() {
                Object obj = this.channelId_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.channelId_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setChannelId(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.channelId_ = str;
                onChanged();
                return this;
            }

            public Builder clearChannelId() {
                this.channelId_ = Channel.getDefaultInstance().getChannelId();
                onChanged();
                return this;
            }

            public Builder setChannelIdBytes(ByteString byteString) {
                if (byteString != null) {
                    Channel.checkByteStringIsUtf8(byteString);
                    this.channelId_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.ChannelOrBuilder
            public String getChannelName() {
                Object obj = this.channelName_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.channelName_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.ChannelOrBuilder
            public ByteString getChannelNameBytes() {
                Object obj = this.channelName_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.channelName_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setChannelName(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.channelName_ = str;
                onChanged();
                return this;
            }

            public Builder clearChannelName() {
                this.channelName_ = Channel.getDefaultInstance().getChannelName();
                onChanged();
                return this;
            }

            public Builder setChannelNameBytes(ByteString byteString) {
                if (byteString != null) {
                    Channel.checkByteStringIsUtf8(byteString);
                    this.channelName_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.ChannelOrBuilder
            public String getChannelGroupId() {
                Object obj = this.channelGroupId_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.channelGroupId_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.ChannelOrBuilder
            public ByteString getChannelGroupIdBytes() {
                Object obj = this.channelGroupId_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.channelGroupId_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setChannelGroupId(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.channelGroupId_ = str;
                onChanged();
                return this;
            }

            public Builder clearChannelGroupId() {
                this.channelGroupId_ = Channel.getDefaultInstance().getChannelGroupId();
                onChanged();
                return this;
            }

            public Builder setChannelGroupIdBytes(ByteString byteString) {
                if (byteString != null) {
                    Channel.checkByteStringIsUtf8(byteString);
                    this.channelGroupId_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.ChannelOrBuilder
            public String getChannelGroupName() {
                Object obj = this.channelGroupName_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.channelGroupName_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.ChannelOrBuilder
            public ByteString getChannelGroupNameBytes() {
                Object obj = this.channelGroupName_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.channelGroupName_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setChannelGroupName(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.channelGroupName_ = str;
                onChanged();
                return this;
            }

            public Builder clearChannelGroupName() {
                this.channelGroupName_ = Channel.getDefaultInstance().getChannelGroupName();
                onChanged();
                return this;
            }

            public Builder setChannelGroupNameBytes(ByteString byteString) {
                if (byteString != null) {
                    Channel.checkByteStringIsUtf8(byteString);
                    this.channelGroupName_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public final Builder setUnknownFields(UnknownFieldSet unknownFieldSet) {
                return (Builder) super.setUnknownFields(unknownFieldSet);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public final Builder mergeUnknownFields(UnknownFieldSet unknownFieldSet) {
                return (Builder) super.mergeUnknownFields(unknownFieldSet);
            }
        }

        public static Channel getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<Channel> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<Channel> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public Channel getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    public static final class SystemContent extends GeneratedMessageV3 implements SystemContentOrBuilder {
        public static final int FROM_MPAY_FIELD_NUMBER = 2;
        public static final int PLAN_ID_FIELD_NUMBER = 3;
        public static final int PUSH_ID_FIELD_NUMBER = 1;
        private static final long serialVersionUID = 0;
        private boolean fromMpay_;
        private byte memoizedIsInitialized;
        private volatile Object planId_;
        private volatile Object pushId_;
        private static final SystemContent DEFAULT_INSTANCE = new SystemContent();
        private static final Parser<SystemContent> PARSER = new AbstractParser<SystemContent>() { // from class: com.netease.push.proto.nano.ClientSdkgate.SystemContent.1
            @Override // com.google.protobuf.Parser
            public SystemContent parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return new SystemContent(codedInputStream, extensionRegistryLite);
            }
        };

        private SystemContent(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private SystemContent() {
            this.memoizedIsInitialized = (byte) -1;
            this.pushId_ = "";
            this.planId_ = "";
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private SystemContent(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            this();
            if (extensionRegistryLite == null) {
                throw new NullPointerException();
            }
            UnknownFieldSet.Builder builderNewBuilder = UnknownFieldSet.newBuilder();
            boolean z = false;
            while (!z) {
                try {
                    try {
                        int tag = codedInputStream.readTag();
                        if (tag != 0) {
                            if (tag == 10) {
                                this.pushId_ = codedInputStream.readStringRequireUtf8();
                            } else if (tag == 16) {
                                this.fromMpay_ = codedInputStream.readBool();
                            } else if (tag == 26) {
                                this.planId_ = codedInputStream.readStringRequireUtf8();
                            } else if (!parseUnknownField(codedInputStream, builderNewBuilder, extensionRegistryLite, tag)) {
                            }
                        }
                        z = true;
                    } catch (InvalidProtocolBufferException e) {
                        throw e.setUnfinishedMessage(this);
                    } catch (IOException e2) {
                        throw new InvalidProtocolBufferException(e2).setUnfinishedMessage(this);
                    }
                } finally {
                    this.unknownFields = builderNewBuilder.build();
                    makeExtensionsImmutable();
                }
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return ClientSdkgate.internal_static_proto_sdkgate_SystemContent_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return ClientSdkgate.internal_static_proto_sdkgate_SystemContent_fieldAccessorTable.ensureFieldAccessorsInitialized(SystemContent.class, Builder.class);
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.SystemContentOrBuilder
        public String getPushId() {
            Object obj = this.pushId_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.pushId_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.SystemContentOrBuilder
        public ByteString getPushIdBytes() {
            Object obj = this.pushId_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.pushId_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.SystemContentOrBuilder
        public boolean getFromMpay() {
            return this.fromMpay_;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.SystemContentOrBuilder
        public String getPlanId() {
            Object obj = this.planId_;
            if (obj instanceof String) {
                return (String) obj;
            }
            String stringUtf8 = ((ByteString) obj).toStringUtf8();
            this.planId_ = stringUtf8;
            return stringUtf8;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.SystemContentOrBuilder
        public ByteString getPlanIdBytes() {
            Object obj = this.planId_;
            if (obj instanceof String) {
                ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.planId_ = byteStringCopyFromUtf8;
                return byteStringCopyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            if (b == 1) {
                return true;
            }
            if (b == 0) {
                return false;
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            if (!getPushIdBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 1, this.pushId_);
            }
            boolean z = this.fromMpay_;
            if (z) {
                codedOutputStream.writeBool(2, z);
            }
            if (!getPlanIdBytes().isEmpty()) {
                GeneratedMessageV3.writeString(codedOutputStream, 3, this.planId_);
            }
            this.unknownFields.writeTo(codedOutputStream);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public int getSerializedSize() {
            int i = this.memoizedSize;
            if (i != -1) {
                return i;
            }
            int iComputeStringSize = getPushIdBytes().isEmpty() ? 0 : 0 + GeneratedMessageV3.computeStringSize(1, this.pushId_);
            boolean z = this.fromMpay_;
            if (z) {
                iComputeStringSize += CodedOutputStream.computeBoolSize(2, z);
            }
            if (!getPlanIdBytes().isEmpty()) {
                iComputeStringSize += GeneratedMessageV3.computeStringSize(3, this.planId_);
            }
            int serializedSize = iComputeStringSize + this.unknownFields.getSerializedSize();
            this.memoizedSize = serializedSize;
            return serializedSize;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof SystemContent)) {
                return super.equals(obj);
            }
            SystemContent systemContent = (SystemContent) obj;
            return getPushId().equals(systemContent.getPushId()) && getFromMpay() == systemContent.getFromMpay() && getPlanId().equals(systemContent.getPlanId()) && this.unknownFields.equals(systemContent.unknownFields);
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int iHashCode = ((((((((((((((779 + getDescriptor().hashCode()) * 37) + 1) * 53) + getPushId().hashCode()) * 37) + 2) * 53) + Internal.hashBoolean(getFromMpay())) * 37) + 3) * 53) + getPlanId().hashCode()) * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = iHashCode;
            return iHashCode;
        }

        public static SystemContent parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString);
        }

        public static SystemContent parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString, extensionRegistryLite);
        }

        public static SystemContent parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(bArr);
        }

        public static SystemContent parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(bArr, extensionRegistryLite);
        }

        public static SystemContent parseFrom(InputStream inputStream) throws IOException {
            return (SystemContent) GeneratedMessageV3.parseWithIOException(PARSER, inputStream);
        }

        public static SystemContent parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (SystemContent) GeneratedMessageV3.parseWithIOException(PARSER, inputStream, extensionRegistryLite);
        }

        public static SystemContent parseDelimitedFrom(InputStream inputStream) throws IOException {
            return (SystemContent) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, inputStream);
        }

        public static SystemContent parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (SystemContent) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, inputStream, extensionRegistryLite);
        }

        public static SystemContent parseFrom(CodedInputStream codedInputStream) throws IOException {
            return (SystemContent) GeneratedMessageV3.parseWithIOException(PARSER, codedInputStream);
        }

        public static SystemContent parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (SystemContent) GeneratedMessageV3.parseWithIOException(PARSER, codedInputStream, extensionRegistryLite);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(SystemContent systemContent) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(systemContent);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Builder newBuilderForType(GeneratedMessageV3.BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements SystemContentOrBuilder {
            private boolean fromMpay_;
            private Object planId_;
            private Object pushId_;

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return ClientSdkgate.internal_static_proto_sdkgate_SystemContent_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return ClientSdkgate.internal_static_proto_sdkgate_SystemContent_fieldAccessorTable.ensureFieldAccessorsInitialized(SystemContent.class, Builder.class);
            }

            private Builder() {
                this.pushId_ = "";
                this.planId_ = "";
                maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessageV3.BuilderParent builderParent) {
                super(builderParent);
                this.pushId_ = "";
                this.planId_ = "";
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                boolean unused = SystemContent.alwaysUseFieldBuilders;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                this.pushId_ = "";
                this.fromMpay_ = false;
                this.planId_ = "";
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return ClientSdkgate.internal_static_proto_sdkgate_SystemContent_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public SystemContent getDefaultInstanceForType() {
                return SystemContent.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public SystemContent build() {
                SystemContent systemContentBuildPartial = buildPartial();
                if (systemContentBuildPartial.isInitialized()) {
                    return systemContentBuildPartial;
                }
                throw newUninitializedMessageException((Message) systemContentBuildPartial);
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public SystemContent buildPartial() {
                SystemContent systemContent = new SystemContent(this);
                systemContent.pushId_ = this.pushId_;
                systemContent.fromMpay_ = this.fromMpay_;
                systemContent.planId_ = this.planId_;
                onBuilt();
                return systemContent;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder
            /* renamed from: clone */
            public Builder mo375clone() {
                return (Builder) super.mo375clone();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setField(Descriptors.FieldDescriptor fieldDescriptor, Object obj) {
                return (Builder) super.setField(fieldDescriptor, obj);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder clearField(Descriptors.FieldDescriptor fieldDescriptor) {
                return (Builder) super.clearField(fieldDescriptor);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder clearOneof(Descriptors.OneofDescriptor oneofDescriptor) {
                return (Builder) super.clearOneof(oneofDescriptor);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, int i, Object obj) {
                return (Builder) super.setRepeatedField(fieldDescriptor, i, obj);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder addRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, Object obj) {
                return (Builder) super.addRepeatedField(fieldDescriptor, obj);
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(Message message) {
                if (message instanceof SystemContent) {
                    return mergeFrom((SystemContent) message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder mergeFrom(SystemContent systemContent) {
                if (systemContent == SystemContent.getDefaultInstance()) {
                    return this;
                }
                if (!systemContent.getPushId().isEmpty()) {
                    this.pushId_ = systemContent.pushId_;
                    onChanged();
                }
                if (systemContent.getFromMpay()) {
                    setFromMpay(systemContent.getFromMpay());
                }
                if (!systemContent.getPlanId().isEmpty()) {
                    this.planId_ = systemContent.planId_;
                    onChanged();
                }
                mergeUnknownFields(systemContent.unknownFields);
                onChanged();
                return this;
            }

            /* JADX WARN: Removed duplicated region for block: B:16:0x0023  */
            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public com.netease.push.proto.nano.ClientSdkgate.SystemContent.Builder mergeFrom(com.google.protobuf.CodedInputStream r3, com.google.protobuf.ExtensionRegistryLite r4) throws java.lang.Throwable {
                /*
                    r2 = this;
                    r0 = 0
                    com.google.protobuf.Parser r1 = com.netease.push.proto.nano.ClientSdkgate.SystemContent.access$23500()     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    java.lang.Object r3 = r1.parsePartialFrom(r3, r4)     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    com.netease.push.proto.nano.ClientSdkgate$SystemContent r3 = (com.netease.push.proto.nano.ClientSdkgate.SystemContent) r3     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    if (r3 == 0) goto L10
                    r2.mergeFrom(r3)
                L10:
                    return r2
                L11:
                    r3 = move-exception
                    goto L21
                L13:
                    r3 = move-exception
                    com.google.protobuf.MessageLite r4 = r3.getUnfinishedMessage()     // Catch: java.lang.Throwable -> L11
                    com.netease.push.proto.nano.ClientSdkgate$SystemContent r4 = (com.netease.push.proto.nano.ClientSdkgate.SystemContent) r4     // Catch: java.lang.Throwable -> L11
                    java.io.IOException r3 = r3.unwrapIOException()     // Catch: java.lang.Throwable -> L1f
                    throw r3     // Catch: java.lang.Throwable -> L1f
                L1f:
                    r3 = move-exception
                    r0 = r4
                L21:
                    if (r0 == 0) goto L26
                    r2.mergeFrom(r0)
                L26:
                    throw r3
                */
                throw new UnsupportedOperationException("Method not decompiled: com.netease.push.proto.nano.ClientSdkgate.SystemContent.Builder.mergeFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):com.netease.push.proto.nano.ClientSdkgate$SystemContent$Builder");
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.SystemContentOrBuilder
            public String getPushId() {
                Object obj = this.pushId_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.pushId_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.SystemContentOrBuilder
            public ByteString getPushIdBytes() {
                Object obj = this.pushId_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.pushId_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setPushId(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.pushId_ = str;
                onChanged();
                return this;
            }

            public Builder clearPushId() {
                this.pushId_ = SystemContent.getDefaultInstance().getPushId();
                onChanged();
                return this;
            }

            public Builder setPushIdBytes(ByteString byteString) {
                if (byteString != null) {
                    SystemContent.checkByteStringIsUtf8(byteString);
                    this.pushId_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.SystemContentOrBuilder
            public boolean getFromMpay() {
                return this.fromMpay_;
            }

            public Builder setFromMpay(boolean z) {
                this.fromMpay_ = z;
                onChanged();
                return this;
            }

            public Builder clearFromMpay() {
                this.fromMpay_ = false;
                onChanged();
                return this;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.SystemContentOrBuilder
            public String getPlanId() {
                Object obj = this.planId_;
                if (!(obj instanceof String)) {
                    String stringUtf8 = ((ByteString) obj).toStringUtf8();
                    this.planId_ = stringUtf8;
                    return stringUtf8;
                }
                return (String) obj;
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.SystemContentOrBuilder
            public ByteString getPlanIdBytes() {
                Object obj = this.planId_;
                if (obj instanceof String) {
                    ByteString byteStringCopyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                    this.planId_ = byteStringCopyFromUtf8;
                    return byteStringCopyFromUtf8;
                }
                return (ByteString) obj;
            }

            public Builder setPlanId(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                this.planId_ = str;
                onChanged();
                return this;
            }

            public Builder clearPlanId() {
                this.planId_ = SystemContent.getDefaultInstance().getPlanId();
                onChanged();
                return this;
            }

            public Builder setPlanIdBytes(ByteString byteString) {
                if (byteString != null) {
                    SystemContent.checkByteStringIsUtf8(byteString);
                    this.planId_ = byteString;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public final Builder setUnknownFields(UnknownFieldSet unknownFieldSet) {
                return (Builder) super.setUnknownFields(unknownFieldSet);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public final Builder mergeUnknownFields(UnknownFieldSet unknownFieldSet) {
                return (Builder) super.mergeUnknownFields(unknownFieldSet);
            }
        }

        public static SystemContent getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<SystemContent> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<SystemContent> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public SystemContent getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    public static final class AckReceiveNotification extends GeneratedMessageV3 implements AckReceiveNotificationOrBuilder {
        private static final AckReceiveNotification DEFAULT_INSTANCE = new AckReceiveNotification();
        private static final Parser<AckReceiveNotification> PARSER = new AbstractParser<AckReceiveNotification>() { // from class: com.netease.push.proto.nano.ClientSdkgate.AckReceiveNotification.1
            @Override // com.google.protobuf.Parser
            public AckReceiveNotification parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return new AckReceiveNotification(codedInputStream, extensionRegistryLite);
            }
        };
        public static final int PUSH_IDS_FIELD_NUMBER = 1;
        private static final long serialVersionUID = 0;
        private byte memoizedIsInitialized;
        private LazyStringList pushIds_;

        private AckReceiveNotification(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private AckReceiveNotification() {
            this.memoizedIsInitialized = (byte) -1;
            this.pushIds_ = LazyStringArrayList.EMPTY;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private AckReceiveNotification(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            this();
            if (extensionRegistryLite == null) {
                throw new NullPointerException();
            }
            UnknownFieldSet.Builder builderNewBuilder = UnknownFieldSet.newBuilder();
            boolean z = false;
            boolean z2 = false;
            while (!z) {
                try {
                    try {
                        int tag = codedInputStream.readTag();
                        if (tag != 0) {
                            if (tag == 10) {
                                String stringRequireUtf8 = codedInputStream.readStringRequireUtf8();
                                if (!(z2 & true)) {
                                    this.pushIds_ = new LazyStringArrayList();
                                    z2 |= true;
                                }
                                this.pushIds_.add(stringRequireUtf8);
                            } else if (!parseUnknownField(codedInputStream, builderNewBuilder, extensionRegistryLite, tag)) {
                            }
                        }
                        z = true;
                    } catch (InvalidProtocolBufferException e) {
                        throw e.setUnfinishedMessage(this);
                    } catch (IOException e2) {
                        throw new InvalidProtocolBufferException(e2).setUnfinishedMessage(this);
                    }
                } finally {
                    if (z2 & true) {
                        this.pushIds_ = this.pushIds_.getUnmodifiableView();
                    }
                    this.unknownFields = builderNewBuilder.build();
                    makeExtensionsImmutable();
                }
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return ClientSdkgate.internal_static_proto_sdkgate_AckReceiveNotification_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return ClientSdkgate.internal_static_proto_sdkgate_AckReceiveNotification_fieldAccessorTable.ensureFieldAccessorsInitialized(AckReceiveNotification.class, Builder.class);
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.AckReceiveNotificationOrBuilder
        public ProtocolStringList getPushIdsList() {
            return this.pushIds_;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.AckReceiveNotificationOrBuilder
        public int getPushIdsCount() {
            return this.pushIds_.size();
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.AckReceiveNotificationOrBuilder
        public String getPushIds(int i) {
            return (String) this.pushIds_.get(i);
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.AckReceiveNotificationOrBuilder
        public ByteString getPushIdsBytes(int i) {
            return this.pushIds_.getByteString(i);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            if (b == 1) {
                return true;
            }
            if (b == 0) {
                return false;
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            for (int i = 0; i < this.pushIds_.size(); i++) {
                GeneratedMessageV3.writeString(codedOutputStream, 1, this.pushIds_.getRaw(i));
            }
            this.unknownFields.writeTo(codedOutputStream);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public int getSerializedSize() {
            int i = this.memoizedSize;
            if (i != -1) {
                return i;
            }
            int iComputeStringSizeNoTag = 0;
            for (int i2 = 0; i2 < this.pushIds_.size(); i2++) {
                iComputeStringSizeNoTag += computeStringSizeNoTag(this.pushIds_.getRaw(i2));
            }
            int size = 0 + iComputeStringSizeNoTag + (getPushIdsList().size() * 1) + this.unknownFields.getSerializedSize();
            this.memoizedSize = size;
            return size;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof AckReceiveNotification)) {
                return super.equals(obj);
            }
            AckReceiveNotification ackReceiveNotification = (AckReceiveNotification) obj;
            return getPushIdsList().equals(ackReceiveNotification.getPushIdsList()) && this.unknownFields.equals(ackReceiveNotification.unknownFields);
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int iHashCode = 779 + getDescriptor().hashCode();
            if (getPushIdsCount() > 0) {
                iHashCode = (((iHashCode * 37) + 1) * 53) + getPushIdsList().hashCode();
            }
            int iHashCode2 = (iHashCode * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = iHashCode2;
            return iHashCode2;
        }

        public static AckReceiveNotification parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString);
        }

        public static AckReceiveNotification parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString, extensionRegistryLite);
        }

        public static AckReceiveNotification parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(bArr);
        }

        public static AckReceiveNotification parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(bArr, extensionRegistryLite);
        }

        public static AckReceiveNotification parseFrom(InputStream inputStream) throws IOException {
            return (AckReceiveNotification) GeneratedMessageV3.parseWithIOException(PARSER, inputStream);
        }

        public static AckReceiveNotification parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (AckReceiveNotification) GeneratedMessageV3.parseWithIOException(PARSER, inputStream, extensionRegistryLite);
        }

        public static AckReceiveNotification parseDelimitedFrom(InputStream inputStream) throws IOException {
            return (AckReceiveNotification) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, inputStream);
        }

        public static AckReceiveNotification parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (AckReceiveNotification) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, inputStream, extensionRegistryLite);
        }

        public static AckReceiveNotification parseFrom(CodedInputStream codedInputStream) throws IOException {
            return (AckReceiveNotification) GeneratedMessageV3.parseWithIOException(PARSER, codedInputStream);
        }

        public static AckReceiveNotification parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (AckReceiveNotification) GeneratedMessageV3.parseWithIOException(PARSER, codedInputStream, extensionRegistryLite);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(AckReceiveNotification ackReceiveNotification) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(ackReceiveNotification);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Builder newBuilderForType(GeneratedMessageV3.BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements AckReceiveNotificationOrBuilder {
            private int bitField0_;
            private LazyStringList pushIds_;

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return ClientSdkgate.internal_static_proto_sdkgate_AckReceiveNotification_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return ClientSdkgate.internal_static_proto_sdkgate_AckReceiveNotification_fieldAccessorTable.ensureFieldAccessorsInitialized(AckReceiveNotification.class, Builder.class);
            }

            private Builder() {
                this.pushIds_ = LazyStringArrayList.EMPTY;
                maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessageV3.BuilderParent builderParent) {
                super(builderParent);
                this.pushIds_ = LazyStringArrayList.EMPTY;
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                boolean unused = AckReceiveNotification.alwaysUseFieldBuilders;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                this.pushIds_ = LazyStringArrayList.EMPTY;
                this.bitField0_ &= -2;
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return ClientSdkgate.internal_static_proto_sdkgate_AckReceiveNotification_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public AckReceiveNotification getDefaultInstanceForType() {
                return AckReceiveNotification.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public AckReceiveNotification build() {
                AckReceiveNotification ackReceiveNotificationBuildPartial = buildPartial();
                if (ackReceiveNotificationBuildPartial.isInitialized()) {
                    return ackReceiveNotificationBuildPartial;
                }
                throw newUninitializedMessageException((Message) ackReceiveNotificationBuildPartial);
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public AckReceiveNotification buildPartial() {
                AckReceiveNotification ackReceiveNotification = new AckReceiveNotification(this);
                if ((this.bitField0_ & 1) != 0) {
                    this.pushIds_ = this.pushIds_.getUnmodifiableView();
                    this.bitField0_ &= -2;
                }
                ackReceiveNotification.pushIds_ = this.pushIds_;
                onBuilt();
                return ackReceiveNotification;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder
            /* renamed from: clone */
            public Builder mo375clone() {
                return (Builder) super.mo375clone();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setField(Descriptors.FieldDescriptor fieldDescriptor, Object obj) {
                return (Builder) super.setField(fieldDescriptor, obj);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder clearField(Descriptors.FieldDescriptor fieldDescriptor) {
                return (Builder) super.clearField(fieldDescriptor);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder clearOneof(Descriptors.OneofDescriptor oneofDescriptor) {
                return (Builder) super.clearOneof(oneofDescriptor);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, int i, Object obj) {
                return (Builder) super.setRepeatedField(fieldDescriptor, i, obj);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder addRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, Object obj) {
                return (Builder) super.addRepeatedField(fieldDescriptor, obj);
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(Message message) {
                if (message instanceof AckReceiveNotification) {
                    return mergeFrom((AckReceiveNotification) message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder mergeFrom(AckReceiveNotification ackReceiveNotification) {
                if (ackReceiveNotification == AckReceiveNotification.getDefaultInstance()) {
                    return this;
                }
                if (!ackReceiveNotification.pushIds_.isEmpty()) {
                    if (this.pushIds_.isEmpty()) {
                        this.pushIds_ = ackReceiveNotification.pushIds_;
                        this.bitField0_ &= -2;
                    } else {
                        ensurePushIdsIsMutable();
                        this.pushIds_.addAll(ackReceiveNotification.pushIds_);
                    }
                    onChanged();
                }
                mergeUnknownFields(ackReceiveNotification.unknownFields);
                onChanged();
                return this;
            }

            /* JADX WARN: Removed duplicated region for block: B:16:0x0023  */
            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public com.netease.push.proto.nano.ClientSdkgate.AckReceiveNotification.Builder mergeFrom(com.google.protobuf.CodedInputStream r3, com.google.protobuf.ExtensionRegistryLite r4) throws java.lang.Throwable {
                /*
                    r2 = this;
                    r0 = 0
                    com.google.protobuf.Parser r1 = com.netease.push.proto.nano.ClientSdkgate.AckReceiveNotification.access$24700()     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    java.lang.Object r3 = r1.parsePartialFrom(r3, r4)     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    com.netease.push.proto.nano.ClientSdkgate$AckReceiveNotification r3 = (com.netease.push.proto.nano.ClientSdkgate.AckReceiveNotification) r3     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    if (r3 == 0) goto L10
                    r2.mergeFrom(r3)
                L10:
                    return r2
                L11:
                    r3 = move-exception
                    goto L21
                L13:
                    r3 = move-exception
                    com.google.protobuf.MessageLite r4 = r3.getUnfinishedMessage()     // Catch: java.lang.Throwable -> L11
                    com.netease.push.proto.nano.ClientSdkgate$AckReceiveNotification r4 = (com.netease.push.proto.nano.ClientSdkgate.AckReceiveNotification) r4     // Catch: java.lang.Throwable -> L11
                    java.io.IOException r3 = r3.unwrapIOException()     // Catch: java.lang.Throwable -> L1f
                    throw r3     // Catch: java.lang.Throwable -> L1f
                L1f:
                    r3 = move-exception
                    r0 = r4
                L21:
                    if (r0 == 0) goto L26
                    r2.mergeFrom(r0)
                L26:
                    throw r3
                */
                throw new UnsupportedOperationException("Method not decompiled: com.netease.push.proto.nano.ClientSdkgate.AckReceiveNotification.Builder.mergeFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):com.netease.push.proto.nano.ClientSdkgate$AckReceiveNotification$Builder");
            }

            private void ensurePushIdsIsMutable() {
                if ((this.bitField0_ & 1) == 0) {
                    this.pushIds_ = new LazyStringArrayList(this.pushIds_);
                    this.bitField0_ |= 1;
                }
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.AckReceiveNotificationOrBuilder
            public ProtocolStringList getPushIdsList() {
                return this.pushIds_.getUnmodifiableView();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.AckReceiveNotificationOrBuilder
            public int getPushIdsCount() {
                return this.pushIds_.size();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.AckReceiveNotificationOrBuilder
            public String getPushIds(int i) {
                return (String) this.pushIds_.get(i);
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.AckReceiveNotificationOrBuilder
            public ByteString getPushIdsBytes(int i) {
                return this.pushIds_.getByteString(i);
            }

            public Builder setPushIds(int i, String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                ensurePushIdsIsMutable();
                this.pushIds_.set(i, str);
                onChanged();
                return this;
            }

            public Builder addPushIds(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                ensurePushIdsIsMutable();
                this.pushIds_.add(str);
                onChanged();
                return this;
            }

            public Builder addAllPushIds(Iterable<String> iterable) {
                ensurePushIdsIsMutable();
                AbstractMessageLite.Builder.addAll((Iterable) iterable, (List) this.pushIds_);
                onChanged();
                return this;
            }

            public Builder clearPushIds() {
                this.pushIds_ = LazyStringArrayList.EMPTY;
                this.bitField0_ &= -2;
                onChanged();
                return this;
            }

            public Builder addPushIdsBytes(ByteString byteString) {
                if (byteString != null) {
                    AckReceiveNotification.checkByteStringIsUtf8(byteString);
                    ensurePushIdsIsMutable();
                    this.pushIds_.add(byteString);
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public final Builder setUnknownFields(UnknownFieldSet unknownFieldSet) {
                return (Builder) super.setUnknownFields(unknownFieldSet);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public final Builder mergeUnknownFields(UnknownFieldSet unknownFieldSet) {
                return (Builder) super.mergeUnknownFields(unknownFieldSet);
            }
        }

        public static AckReceiveNotification getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<AckReceiveNotification> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<AckReceiveNotification> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public AckReceiveNotification getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    public static final class FindOfflineNotificationRequest extends GeneratedMessageV3 implements FindOfflineNotificationRequestOrBuilder {
        private static final FindOfflineNotificationRequest DEFAULT_INSTANCE = new FindOfflineNotificationRequest();
        private static final Parser<FindOfflineNotificationRequest> PARSER = new AbstractParser<FindOfflineNotificationRequest>() { // from class: com.netease.push.proto.nano.ClientSdkgate.FindOfflineNotificationRequest.1
            @Override // com.google.protobuf.Parser
            public FindOfflineNotificationRequest parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return new FindOfflineNotificationRequest(codedInputStream, extensionRegistryLite);
            }
        };
        private static final long serialVersionUID = 0;
        private byte memoizedIsInitialized;

        private FindOfflineNotificationRequest(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private FindOfflineNotificationRequest() {
            this.memoizedIsInitialized = (byte) -1;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private FindOfflineNotificationRequest(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            this();
            if (extensionRegistryLite == null) {
                throw new NullPointerException();
            }
            UnknownFieldSet.Builder builderNewBuilder = UnknownFieldSet.newBuilder();
            boolean z = false;
            while (!z) {
                try {
                    try {
                        int tag = codedInputStream.readTag();
                        if (tag == 0 || !parseUnknownField(codedInputStream, builderNewBuilder, extensionRegistryLite, tag)) {
                            z = true;
                        }
                    } catch (InvalidProtocolBufferException e) {
                        throw e.setUnfinishedMessage(this);
                    } catch (IOException e2) {
                        throw new InvalidProtocolBufferException(e2).setUnfinishedMessage(this);
                    }
                } finally {
                    this.unknownFields = builderNewBuilder.build();
                    makeExtensionsImmutable();
                }
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return ClientSdkgate.internal_static_proto_sdkgate_FindOfflineNotificationRequest_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return ClientSdkgate.internal_static_proto_sdkgate_FindOfflineNotificationRequest_fieldAccessorTable.ensureFieldAccessorsInitialized(FindOfflineNotificationRequest.class, Builder.class);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            if (b == 1) {
                return true;
            }
            if (b == 0) {
                return false;
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            this.unknownFields.writeTo(codedOutputStream);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public int getSerializedSize() {
            int i = this.memoizedSize;
            if (i != -1) {
                return i;
            }
            int serializedSize = this.unknownFields.getSerializedSize() + 0;
            this.memoizedSize = serializedSize;
            return serializedSize;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof FindOfflineNotificationRequest) {
                return this.unknownFields.equals(((FindOfflineNotificationRequest) obj).unknownFields);
            }
            return super.equals(obj);
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int iHashCode = ((779 + getDescriptor().hashCode()) * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = iHashCode;
            return iHashCode;
        }

        public static FindOfflineNotificationRequest parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString);
        }

        public static FindOfflineNotificationRequest parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString, extensionRegistryLite);
        }

        public static FindOfflineNotificationRequest parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(bArr);
        }

        public static FindOfflineNotificationRequest parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(bArr, extensionRegistryLite);
        }

        public static FindOfflineNotificationRequest parseFrom(InputStream inputStream) throws IOException {
            return (FindOfflineNotificationRequest) GeneratedMessageV3.parseWithIOException(PARSER, inputStream);
        }

        public static FindOfflineNotificationRequest parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (FindOfflineNotificationRequest) GeneratedMessageV3.parseWithIOException(PARSER, inputStream, extensionRegistryLite);
        }

        public static FindOfflineNotificationRequest parseDelimitedFrom(InputStream inputStream) throws IOException {
            return (FindOfflineNotificationRequest) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, inputStream);
        }

        public static FindOfflineNotificationRequest parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (FindOfflineNotificationRequest) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, inputStream, extensionRegistryLite);
        }

        public static FindOfflineNotificationRequest parseFrom(CodedInputStream codedInputStream) throws IOException {
            return (FindOfflineNotificationRequest) GeneratedMessageV3.parseWithIOException(PARSER, codedInputStream);
        }

        public static FindOfflineNotificationRequest parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (FindOfflineNotificationRequest) GeneratedMessageV3.parseWithIOException(PARSER, codedInputStream, extensionRegistryLite);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(FindOfflineNotificationRequest findOfflineNotificationRequest) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(findOfflineNotificationRequest);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Builder newBuilderForType(GeneratedMessageV3.BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements FindOfflineNotificationRequestOrBuilder {
            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return ClientSdkgate.internal_static_proto_sdkgate_FindOfflineNotificationRequest_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return ClientSdkgate.internal_static_proto_sdkgate_FindOfflineNotificationRequest_fieldAccessorTable.ensureFieldAccessorsInitialized(FindOfflineNotificationRequest.class, Builder.class);
            }

            private Builder() {
                maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessageV3.BuilderParent builderParent) {
                super(builderParent);
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                boolean unused = FindOfflineNotificationRequest.alwaysUseFieldBuilders;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return ClientSdkgate.internal_static_proto_sdkgate_FindOfflineNotificationRequest_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public FindOfflineNotificationRequest getDefaultInstanceForType() {
                return FindOfflineNotificationRequest.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public FindOfflineNotificationRequest build() {
                FindOfflineNotificationRequest findOfflineNotificationRequestBuildPartial = buildPartial();
                if (findOfflineNotificationRequestBuildPartial.isInitialized()) {
                    return findOfflineNotificationRequestBuildPartial;
                }
                throw newUninitializedMessageException((Message) findOfflineNotificationRequestBuildPartial);
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public FindOfflineNotificationRequest buildPartial() {
                FindOfflineNotificationRequest findOfflineNotificationRequest = new FindOfflineNotificationRequest(this);
                onBuilt();
                return findOfflineNotificationRequest;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder
            /* renamed from: clone */
            public Builder mo375clone() {
                return (Builder) super.mo375clone();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setField(Descriptors.FieldDescriptor fieldDescriptor, Object obj) {
                return (Builder) super.setField(fieldDescriptor, obj);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder clearField(Descriptors.FieldDescriptor fieldDescriptor) {
                return (Builder) super.clearField(fieldDescriptor);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder clearOneof(Descriptors.OneofDescriptor oneofDescriptor) {
                return (Builder) super.clearOneof(oneofDescriptor);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, int i, Object obj) {
                return (Builder) super.setRepeatedField(fieldDescriptor, i, obj);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder addRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, Object obj) {
                return (Builder) super.addRepeatedField(fieldDescriptor, obj);
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(Message message) {
                if (message instanceof FindOfflineNotificationRequest) {
                    return mergeFrom((FindOfflineNotificationRequest) message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder mergeFrom(FindOfflineNotificationRequest findOfflineNotificationRequest) {
                if (findOfflineNotificationRequest == FindOfflineNotificationRequest.getDefaultInstance()) {
                    return this;
                }
                mergeUnknownFields(findOfflineNotificationRequest.unknownFields);
                onChanged();
                return this;
            }

            /* JADX WARN: Removed duplicated region for block: B:16:0x0023  */
            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public com.netease.push.proto.nano.ClientSdkgate.FindOfflineNotificationRequest.Builder mergeFrom(com.google.protobuf.CodedInputStream r3, com.google.protobuf.ExtensionRegistryLite r4) throws java.lang.Throwable {
                /*
                    r2 = this;
                    r0 = 0
                    com.google.protobuf.Parser r1 = com.netease.push.proto.nano.ClientSdkgate.FindOfflineNotificationRequest.access$25700()     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    java.lang.Object r3 = r1.parsePartialFrom(r3, r4)     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    com.netease.push.proto.nano.ClientSdkgate$FindOfflineNotificationRequest r3 = (com.netease.push.proto.nano.ClientSdkgate.FindOfflineNotificationRequest) r3     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    if (r3 == 0) goto L10
                    r2.mergeFrom(r3)
                L10:
                    return r2
                L11:
                    r3 = move-exception
                    goto L21
                L13:
                    r3 = move-exception
                    com.google.protobuf.MessageLite r4 = r3.getUnfinishedMessage()     // Catch: java.lang.Throwable -> L11
                    com.netease.push.proto.nano.ClientSdkgate$FindOfflineNotificationRequest r4 = (com.netease.push.proto.nano.ClientSdkgate.FindOfflineNotificationRequest) r4     // Catch: java.lang.Throwable -> L11
                    java.io.IOException r3 = r3.unwrapIOException()     // Catch: java.lang.Throwable -> L1f
                    throw r3     // Catch: java.lang.Throwable -> L1f
                L1f:
                    r3 = move-exception
                    r0 = r4
                L21:
                    if (r0 == 0) goto L26
                    r2.mergeFrom(r0)
                L26:
                    throw r3
                */
                throw new UnsupportedOperationException("Method not decompiled: com.netease.push.proto.nano.ClientSdkgate.FindOfflineNotificationRequest.Builder.mergeFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):com.netease.push.proto.nano.ClientSdkgate$FindOfflineNotificationRequest$Builder");
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public final Builder setUnknownFields(UnknownFieldSet unknownFieldSet) {
                return (Builder) super.setUnknownFields(unknownFieldSet);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public final Builder mergeUnknownFields(UnknownFieldSet unknownFieldSet) {
                return (Builder) super.mergeUnknownFields(unknownFieldSet);
            }
        }

        public static FindOfflineNotificationRequest getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<FindOfflineNotificationRequest> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<FindOfflineNotificationRequest> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public FindOfflineNotificationRequest getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    public static final class FindOfflineNotificationResponse extends GeneratedMessageV3 implements FindOfflineNotificationResponseOrBuilder {
        public static final int NOTIFICATIONS_FIELD_NUMBER = 1;
        private static final long serialVersionUID = 0;
        private byte memoizedIsInitialized;
        private List<Notification> notifications_;
        private static final FindOfflineNotificationResponse DEFAULT_INSTANCE = new FindOfflineNotificationResponse();
        private static final Parser<FindOfflineNotificationResponse> PARSER = new AbstractParser<FindOfflineNotificationResponse>() { // from class: com.netease.push.proto.nano.ClientSdkgate.FindOfflineNotificationResponse.1
            @Override // com.google.protobuf.Parser
            public FindOfflineNotificationResponse parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return new FindOfflineNotificationResponse(codedInputStream, extensionRegistryLite);
            }
        };

        private FindOfflineNotificationResponse(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private FindOfflineNotificationResponse() {
            this.memoizedIsInitialized = (byte) -1;
            this.notifications_ = Collections.emptyList();
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        /* JADX WARN: Multi-variable type inference failed */
        private FindOfflineNotificationResponse(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            this();
            if (extensionRegistryLite == null) {
                throw new NullPointerException();
            }
            UnknownFieldSet.Builder builderNewBuilder = UnknownFieldSet.newBuilder();
            boolean z = false;
            boolean z2 = false;
            while (!z) {
                try {
                    try {
                        try {
                            int tag = codedInputStream.readTag();
                            if (tag != 0) {
                                if (tag == 10) {
                                    if (!(z2 & true)) {
                                        this.notifications_ = new ArrayList();
                                        z2 |= true;
                                    }
                                    this.notifications_.add(codedInputStream.readMessage(Notification.parser(), extensionRegistryLite));
                                } else if (!parseUnknownField(codedInputStream, builderNewBuilder, extensionRegistryLite, tag)) {
                                }
                            }
                            z = true;
                        } catch (IOException e) {
                            throw new InvalidProtocolBufferException(e).setUnfinishedMessage(this);
                        }
                    } catch (InvalidProtocolBufferException e2) {
                        throw e2.setUnfinishedMessage(this);
                    }
                } finally {
                    if (z2 & true) {
                        this.notifications_ = Collections.unmodifiableList(this.notifications_);
                    }
                    this.unknownFields = builderNewBuilder.build();
                    makeExtensionsImmutable();
                }
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return ClientSdkgate.internal_static_proto_sdkgate_FindOfflineNotificationResponse_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return ClientSdkgate.internal_static_proto_sdkgate_FindOfflineNotificationResponse_fieldAccessorTable.ensureFieldAccessorsInitialized(FindOfflineNotificationResponse.class, Builder.class);
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.FindOfflineNotificationResponseOrBuilder
        public List<Notification> getNotificationsList() {
            return this.notifications_;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.FindOfflineNotificationResponseOrBuilder
        public List<? extends NotificationOrBuilder> getNotificationsOrBuilderList() {
            return this.notifications_;
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.FindOfflineNotificationResponseOrBuilder
        public int getNotificationsCount() {
            return this.notifications_.size();
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.FindOfflineNotificationResponseOrBuilder
        public Notification getNotifications(int i) {
            return this.notifications_.get(i);
        }

        @Override // com.netease.push.proto.nano.ClientSdkgate.FindOfflineNotificationResponseOrBuilder
        public NotificationOrBuilder getNotificationsOrBuilder(int i) {
            return this.notifications_.get(i);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            if (b == 1) {
                return true;
            }
            if (b == 0) {
                return false;
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            for (int i = 0; i < this.notifications_.size(); i++) {
                codedOutputStream.writeMessage(1, this.notifications_.get(i));
            }
            this.unknownFields.writeTo(codedOutputStream);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public int getSerializedSize() {
            int i = this.memoizedSize;
            if (i != -1) {
                return i;
            }
            int iComputeMessageSize = 0;
            for (int i2 = 0; i2 < this.notifications_.size(); i2++) {
                iComputeMessageSize += CodedOutputStream.computeMessageSize(1, this.notifications_.get(i2));
            }
            int serializedSize = iComputeMessageSize + this.unknownFields.getSerializedSize();
            this.memoizedSize = serializedSize;
            return serializedSize;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof FindOfflineNotificationResponse)) {
                return super.equals(obj);
            }
            FindOfflineNotificationResponse findOfflineNotificationResponse = (FindOfflineNotificationResponse) obj;
            return getNotificationsList().equals(findOfflineNotificationResponse.getNotificationsList()) && this.unknownFields.equals(findOfflineNotificationResponse.unknownFields);
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int iHashCode = 779 + getDescriptor().hashCode();
            if (getNotificationsCount() > 0) {
                iHashCode = (((iHashCode * 37) + 1) * 53) + getNotificationsList().hashCode();
            }
            int iHashCode2 = (iHashCode * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = iHashCode2;
            return iHashCode2;
        }

        public static FindOfflineNotificationResponse parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString);
        }

        public static FindOfflineNotificationResponse parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString, extensionRegistryLite);
        }

        public static FindOfflineNotificationResponse parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(bArr);
        }

        public static FindOfflineNotificationResponse parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(bArr, extensionRegistryLite);
        }

        public static FindOfflineNotificationResponse parseFrom(InputStream inputStream) throws IOException {
            return (FindOfflineNotificationResponse) GeneratedMessageV3.parseWithIOException(PARSER, inputStream);
        }

        public static FindOfflineNotificationResponse parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (FindOfflineNotificationResponse) GeneratedMessageV3.parseWithIOException(PARSER, inputStream, extensionRegistryLite);
        }

        public static FindOfflineNotificationResponse parseDelimitedFrom(InputStream inputStream) throws IOException {
            return (FindOfflineNotificationResponse) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, inputStream);
        }

        public static FindOfflineNotificationResponse parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (FindOfflineNotificationResponse) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, inputStream, extensionRegistryLite);
        }

        public static FindOfflineNotificationResponse parseFrom(CodedInputStream codedInputStream) throws IOException {
            return (FindOfflineNotificationResponse) GeneratedMessageV3.parseWithIOException(PARSER, codedInputStream);
        }

        public static FindOfflineNotificationResponse parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (FindOfflineNotificationResponse) GeneratedMessageV3.parseWithIOException(PARSER, codedInputStream, extensionRegistryLite);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(FindOfflineNotificationResponse findOfflineNotificationResponse) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(findOfflineNotificationResponse);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Builder newBuilderForType(GeneratedMessageV3.BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements FindOfflineNotificationResponseOrBuilder {
            private int bitField0_;
            private RepeatedFieldBuilderV3<Notification, Notification.Builder, NotificationOrBuilder> notificationsBuilder_;
            private List<Notification> notifications_;

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return ClientSdkgate.internal_static_proto_sdkgate_FindOfflineNotificationResponse_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return ClientSdkgate.internal_static_proto_sdkgate_FindOfflineNotificationResponse_fieldAccessorTable.ensureFieldAccessorsInitialized(FindOfflineNotificationResponse.class, Builder.class);
            }

            private Builder() {
                this.notifications_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessageV3.BuilderParent builderParent) {
                super(builderParent);
                this.notifications_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                if (FindOfflineNotificationResponse.alwaysUseFieldBuilders) {
                    getNotificationsFieldBuilder();
                }
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                RepeatedFieldBuilderV3<Notification, Notification.Builder, NotificationOrBuilder> repeatedFieldBuilderV3 = this.notificationsBuilder_;
                if (repeatedFieldBuilderV3 == null) {
                    this.notifications_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                } else {
                    repeatedFieldBuilderV3.clear();
                }
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return ClientSdkgate.internal_static_proto_sdkgate_FindOfflineNotificationResponse_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public FindOfflineNotificationResponse getDefaultInstanceForType() {
                return FindOfflineNotificationResponse.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public FindOfflineNotificationResponse build() {
                FindOfflineNotificationResponse findOfflineNotificationResponseBuildPartial = buildPartial();
                if (findOfflineNotificationResponseBuildPartial.isInitialized()) {
                    return findOfflineNotificationResponseBuildPartial;
                }
                throw newUninitializedMessageException((Message) findOfflineNotificationResponseBuildPartial);
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public FindOfflineNotificationResponse buildPartial() {
                FindOfflineNotificationResponse findOfflineNotificationResponse = new FindOfflineNotificationResponse(this);
                int i = this.bitField0_;
                RepeatedFieldBuilderV3<Notification, Notification.Builder, NotificationOrBuilder> repeatedFieldBuilderV3 = this.notificationsBuilder_;
                if (repeatedFieldBuilderV3 == null) {
                    if ((i & 1) != 0) {
                        this.notifications_ = Collections.unmodifiableList(this.notifications_);
                        this.bitField0_ &= -2;
                    }
                    findOfflineNotificationResponse.notifications_ = this.notifications_;
                } else {
                    findOfflineNotificationResponse.notifications_ = repeatedFieldBuilderV3.build();
                }
                onBuilt();
                return findOfflineNotificationResponse;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder
            /* renamed from: clone */
            public Builder mo375clone() {
                return (Builder) super.mo375clone();
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setField(Descriptors.FieldDescriptor fieldDescriptor, Object obj) {
                return (Builder) super.setField(fieldDescriptor, obj);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder clearField(Descriptors.FieldDescriptor fieldDescriptor) {
                return (Builder) super.clearField(fieldDescriptor);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder clearOneof(Descriptors.OneofDescriptor oneofDescriptor) {
                return (Builder) super.clearOneof(oneofDescriptor);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder setRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, int i, Object obj) {
                return (Builder) super.setRepeatedField(fieldDescriptor, i, obj);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public Builder addRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, Object obj) {
                return (Builder) super.addRepeatedField(fieldDescriptor, obj);
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(Message message) {
                if (message instanceof FindOfflineNotificationResponse) {
                    return mergeFrom((FindOfflineNotificationResponse) message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder mergeFrom(FindOfflineNotificationResponse findOfflineNotificationResponse) {
                if (findOfflineNotificationResponse == FindOfflineNotificationResponse.getDefaultInstance()) {
                    return this;
                }
                if (this.notificationsBuilder_ == null) {
                    if (!findOfflineNotificationResponse.notifications_.isEmpty()) {
                        if (this.notifications_.isEmpty()) {
                            this.notifications_ = findOfflineNotificationResponse.notifications_;
                            this.bitField0_ &= -2;
                        } else {
                            ensureNotificationsIsMutable();
                            this.notifications_.addAll(findOfflineNotificationResponse.notifications_);
                        }
                        onChanged();
                    }
                } else if (!findOfflineNotificationResponse.notifications_.isEmpty()) {
                    if (!this.notificationsBuilder_.isEmpty()) {
                        this.notificationsBuilder_.addAllMessages(findOfflineNotificationResponse.notifications_);
                    } else {
                        this.notificationsBuilder_.dispose();
                        this.notificationsBuilder_ = null;
                        this.notifications_ = findOfflineNotificationResponse.notifications_;
                        this.bitField0_ &= -2;
                        this.notificationsBuilder_ = FindOfflineNotificationResponse.alwaysUseFieldBuilders ? getNotificationsFieldBuilder() : null;
                    }
                }
                mergeUnknownFields(findOfflineNotificationResponse.unknownFields);
                onChanged();
                return this;
            }

            /* JADX WARN: Removed duplicated region for block: B:16:0x0023  */
            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public com.netease.push.proto.nano.ClientSdkgate.FindOfflineNotificationResponse.Builder mergeFrom(com.google.protobuf.CodedInputStream r3, com.google.protobuf.ExtensionRegistryLite r4) throws java.lang.Throwable {
                /*
                    r2 = this;
                    r0 = 0
                    com.google.protobuf.Parser r1 = com.netease.push.proto.nano.ClientSdkgate.FindOfflineNotificationResponse.access$26800()     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    java.lang.Object r3 = r1.parsePartialFrom(r3, r4)     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    com.netease.push.proto.nano.ClientSdkgate$FindOfflineNotificationResponse r3 = (com.netease.push.proto.nano.ClientSdkgate.FindOfflineNotificationResponse) r3     // Catch: java.lang.Throwable -> L11 com.google.protobuf.InvalidProtocolBufferException -> L13
                    if (r3 == 0) goto L10
                    r2.mergeFrom(r3)
                L10:
                    return r2
                L11:
                    r3 = move-exception
                    goto L21
                L13:
                    r3 = move-exception
                    com.google.protobuf.MessageLite r4 = r3.getUnfinishedMessage()     // Catch: java.lang.Throwable -> L11
                    com.netease.push.proto.nano.ClientSdkgate$FindOfflineNotificationResponse r4 = (com.netease.push.proto.nano.ClientSdkgate.FindOfflineNotificationResponse) r4     // Catch: java.lang.Throwable -> L11
                    java.io.IOException r3 = r3.unwrapIOException()     // Catch: java.lang.Throwable -> L1f
                    throw r3     // Catch: java.lang.Throwable -> L1f
                L1f:
                    r3 = move-exception
                    r0 = r4
                L21:
                    if (r0 == 0) goto L26
                    r2.mergeFrom(r0)
                L26:
                    throw r3
                */
                throw new UnsupportedOperationException("Method not decompiled: com.netease.push.proto.nano.ClientSdkgate.FindOfflineNotificationResponse.Builder.mergeFrom(com.google.protobuf.CodedInputStream, com.google.protobuf.ExtensionRegistryLite):com.netease.push.proto.nano.ClientSdkgate$FindOfflineNotificationResponse$Builder");
            }

            private void ensureNotificationsIsMutable() {
                if ((this.bitField0_ & 1) == 0) {
                    this.notifications_ = new ArrayList(this.notifications_);
                    this.bitField0_ |= 1;
                }
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.FindOfflineNotificationResponseOrBuilder
            public List<Notification> getNotificationsList() {
                RepeatedFieldBuilderV3<Notification, Notification.Builder, NotificationOrBuilder> repeatedFieldBuilderV3 = this.notificationsBuilder_;
                if (repeatedFieldBuilderV3 == null) {
                    return Collections.unmodifiableList(this.notifications_);
                }
                return repeatedFieldBuilderV3.getMessageList();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.FindOfflineNotificationResponseOrBuilder
            public int getNotificationsCount() {
                RepeatedFieldBuilderV3<Notification, Notification.Builder, NotificationOrBuilder> repeatedFieldBuilderV3 = this.notificationsBuilder_;
                if (repeatedFieldBuilderV3 == null) {
                    return this.notifications_.size();
                }
                return repeatedFieldBuilderV3.getCount();
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.FindOfflineNotificationResponseOrBuilder
            public Notification getNotifications(int i) {
                RepeatedFieldBuilderV3<Notification, Notification.Builder, NotificationOrBuilder> repeatedFieldBuilderV3 = this.notificationsBuilder_;
                if (repeatedFieldBuilderV3 == null) {
                    return this.notifications_.get(i);
                }
                return (Notification) repeatedFieldBuilderV3.getMessage(i);
            }

            public Builder setNotifications(int i, Notification notification) {
                RepeatedFieldBuilderV3<Notification, Notification.Builder, NotificationOrBuilder> repeatedFieldBuilderV3 = this.notificationsBuilder_;
                if (repeatedFieldBuilderV3 != null) {
                    repeatedFieldBuilderV3.setMessage(i, notification);
                } else {
                    if (notification == null) {
                        throw new NullPointerException();
                    }
                    ensureNotificationsIsMutable();
                    this.notifications_.set(i, notification);
                    onChanged();
                }
                return this;
            }

            public Builder setNotifications(int i, Notification.Builder builder) {
                RepeatedFieldBuilderV3<Notification, Notification.Builder, NotificationOrBuilder> repeatedFieldBuilderV3 = this.notificationsBuilder_;
                if (repeatedFieldBuilderV3 == null) {
                    ensureNotificationsIsMutable();
                    this.notifications_.set(i, builder.build());
                    onChanged();
                } else {
                    repeatedFieldBuilderV3.setMessage(i, builder.build());
                }
                return this;
            }

            public Builder addNotifications(Notification notification) {
                RepeatedFieldBuilderV3<Notification, Notification.Builder, NotificationOrBuilder> repeatedFieldBuilderV3 = this.notificationsBuilder_;
                if (repeatedFieldBuilderV3 != null) {
                    repeatedFieldBuilderV3.addMessage(notification);
                } else {
                    if (notification == null) {
                        throw new NullPointerException();
                    }
                    ensureNotificationsIsMutable();
                    this.notifications_.add(notification);
                    onChanged();
                }
                return this;
            }

            public Builder addNotifications(int i, Notification notification) {
                RepeatedFieldBuilderV3<Notification, Notification.Builder, NotificationOrBuilder> repeatedFieldBuilderV3 = this.notificationsBuilder_;
                if (repeatedFieldBuilderV3 != null) {
                    repeatedFieldBuilderV3.addMessage(i, notification);
                } else {
                    if (notification == null) {
                        throw new NullPointerException();
                    }
                    ensureNotificationsIsMutable();
                    this.notifications_.add(i, notification);
                    onChanged();
                }
                return this;
            }

            public Builder addNotifications(Notification.Builder builder) {
                RepeatedFieldBuilderV3<Notification, Notification.Builder, NotificationOrBuilder> repeatedFieldBuilderV3 = this.notificationsBuilder_;
                if (repeatedFieldBuilderV3 == null) {
                    ensureNotificationsIsMutable();
                    this.notifications_.add(builder.build());
                    onChanged();
                } else {
                    repeatedFieldBuilderV3.addMessage(builder.build());
                }
                return this;
            }

            public Builder addNotifications(int i, Notification.Builder builder) {
                RepeatedFieldBuilderV3<Notification, Notification.Builder, NotificationOrBuilder> repeatedFieldBuilderV3 = this.notificationsBuilder_;
                if (repeatedFieldBuilderV3 == null) {
                    ensureNotificationsIsMutable();
                    this.notifications_.add(i, builder.build());
                    onChanged();
                } else {
                    repeatedFieldBuilderV3.addMessage(i, builder.build());
                }
                return this;
            }

            public Builder addAllNotifications(Iterable<? extends Notification> iterable) {
                RepeatedFieldBuilderV3<Notification, Notification.Builder, NotificationOrBuilder> repeatedFieldBuilderV3 = this.notificationsBuilder_;
                if (repeatedFieldBuilderV3 == null) {
                    ensureNotificationsIsMutable();
                    AbstractMessageLite.Builder.addAll((Iterable) iterable, (List) this.notifications_);
                    onChanged();
                } else {
                    repeatedFieldBuilderV3.addAllMessages(iterable);
                }
                return this;
            }

            public Builder clearNotifications() {
                RepeatedFieldBuilderV3<Notification, Notification.Builder, NotificationOrBuilder> repeatedFieldBuilderV3 = this.notificationsBuilder_;
                if (repeatedFieldBuilderV3 == null) {
                    this.notifications_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                    onChanged();
                } else {
                    repeatedFieldBuilderV3.clear();
                }
                return this;
            }

            public Builder removeNotifications(int i) {
                RepeatedFieldBuilderV3<Notification, Notification.Builder, NotificationOrBuilder> repeatedFieldBuilderV3 = this.notificationsBuilder_;
                if (repeatedFieldBuilderV3 == null) {
                    ensureNotificationsIsMutable();
                    this.notifications_.remove(i);
                    onChanged();
                } else {
                    repeatedFieldBuilderV3.remove(i);
                }
                return this;
            }

            public Notification.Builder getNotificationsBuilder(int i) {
                return (Notification.Builder) getNotificationsFieldBuilder().getBuilder(i);
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.FindOfflineNotificationResponseOrBuilder
            public NotificationOrBuilder getNotificationsOrBuilder(int i) {
                RepeatedFieldBuilderV3<Notification, Notification.Builder, NotificationOrBuilder> repeatedFieldBuilderV3 = this.notificationsBuilder_;
                if (repeatedFieldBuilderV3 == null) {
                    return this.notifications_.get(i);
                }
                return (NotificationOrBuilder) repeatedFieldBuilderV3.getMessageOrBuilder(i);
            }

            @Override // com.netease.push.proto.nano.ClientSdkgate.FindOfflineNotificationResponseOrBuilder
            public List<? extends NotificationOrBuilder> getNotificationsOrBuilderList() {
                RepeatedFieldBuilderV3<Notification, Notification.Builder, NotificationOrBuilder> repeatedFieldBuilderV3 = this.notificationsBuilder_;
                if (repeatedFieldBuilderV3 != null) {
                    return repeatedFieldBuilderV3.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.notifications_);
            }

            public Notification.Builder addNotificationsBuilder() {
                return (Notification.Builder) getNotificationsFieldBuilder().addBuilder(Notification.getDefaultInstance());
            }

            public Notification.Builder addNotificationsBuilder(int i) {
                return (Notification.Builder) getNotificationsFieldBuilder().addBuilder(i, Notification.getDefaultInstance());
            }

            public List<Notification.Builder> getNotificationsBuilderList() {
                return getNotificationsFieldBuilder().getBuilderList();
            }

            private RepeatedFieldBuilderV3<Notification, Notification.Builder, NotificationOrBuilder> getNotificationsFieldBuilder() {
                if (this.notificationsBuilder_ == null) {
                    this.notificationsBuilder_ = new RepeatedFieldBuilderV3<>(this.notifications_, (this.bitField0_ & 1) != 0, getParentForChildren(), isClean());
                    this.notifications_ = null;
                }
                return this.notificationsBuilder_;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
            public final Builder setUnknownFields(UnknownFieldSet unknownFieldSet) {
                return (Builder) super.setUnknownFields(unknownFieldSet);
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public final Builder mergeUnknownFields(UnknownFieldSet unknownFieldSet) {
                return (Builder) super.mergeUnknownFields(unknownFieldSet);
            }
        }

        public static FindOfflineNotificationResponse getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<FindOfflineNotificationResponse> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<FindOfflineNotificationResponse> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public FindOfflineNotificationResponse getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    public static Descriptors.FileDescriptor getDescriptor() {
        return descriptor;
    }

    static {
        new String[1][0] = "\n/server/proto/proto_sdkgate/client.sdkgate.proto\u0012\rproto_sdkgate\"\u0019\n\u0006ErrMsg\u0012\u000f\n\u0007err_msg\u0018\u0001 \u0001(\t\"\n\n\bEmptyMsg\"Z\n\u0012PreRegisterRequest\u0012\u0012\n\nproduct_id\u0018\u0001 \u0001(\t\u0012\u0012\n\nclient_key\u0018\u0002 \u0001(\t\u0012\u000f\n\u0007channel\u0018\u0003 \u0001(\t\u0012\u000b\n\u0003pkg\u0018\u0004 \u0001(\t\"#\n\u0013PreRegisterResponse\u0012\f\n\u0004auth\u0018\u0001 \u0001(\t\"\u0091\u0002\n\u000fRegisterRequest\u0012\f\n\u0004auth\u0018\u0001 \u0001(\t\u0012\u000f\n\u0007channel\u0018\u0002 \u0001(\t\u0012\u000b\n\u0003pkg\u0018\u0003 \u0001(\t\u0012\r\n\u0005regid\u0018\u0004 \u0001(\t\u0012\u0015\n\rpermit_notice\u0018\n \u0001(\b\u0012\u0013\n\u000bapp_version\u0018\u000b \u0001(\t\u0012\u0013\n\u000bsdk_version\u0018\f \u0001(\t\u0012\u0016\n\u000esystem_version\u0018\r \u0001(\t\u0012\u0011\n\ttime_zone\u0018\u000e \u0001(\t\u0012\u0014\n\fdevice_brand\u0018\u000f \u0001(\t\u0012\u0014\n\fdevice_model\u0018\u0010 \u0001(\t\u0012\u0017\n\u000fapns_production\u00182 \u0001(\b\u0012\u0012\n\nproduct_id\u0018d \u0001(\t\"5\n\u0010RegisterResponse\u0012\r\n\u0005token\u0018\u0001 \u0001(\t\u0012\u0012\n\naccess_key\u0018\u0002 \u0001(\t\"\u00da\u0001\n\fLoginRequest\u0012\r\n\u0005token\u0018\u0001 \u0001(\t\u0012\u0012\n\naccess_key\u0018\u0002 \u0001(\t\u0012\u0015\n\rpermit_notice\u0018\n \u0001(\b\u0012\u0013\n\u000bapp_version\u0018\u000b \u0001(\t\u0012\u0013\n\u000bsdk_version\u0018\f \u0001(\t\u0012\u0016\n\u000esystem_version\u0018\r \u0001(\t\u0012\u0011\n\ttime_zone\u0018\u000e \u0001(\t\u0012\u0014\n\fdevice_brand\u0018\u000f \u0001(\t\u0012\u0014\n\fdevice_model\u0018\u0010 \u0001(\t\u0012\u000f\n\u0007transid\u00182 \u0001(\t\"\u0012\n\u0010HeartbeatRequest\"\u009e\u0002\n\fNotification\u0012\r\n\u0005title\u0018\u0001 \u0001(\t\u0012\u0011\n\tsub_title\u0018\u0002 \u0001(\t\u0012\u000f\n\u0007content\u0018\u0003 \u0001(\t\u0012\u000e\n\u0006silent\u0018\u0004 \u0001(\b\u0012!\n\u0004apns\u00182 \u0001(\u000b2\u0013.APNS\u0012'\n\u0007android\u00183 \u0001(\u000b2\u0016.Android\u00124\n\u000esystem_content\u0018P \u0001(\u000b2\u001c.proto_sdkgate.SystemContent\u0012\u0015\n\rfeature_title\u0018d \u0001(\t\u0012\u0019\n\u0011feature_sub_title\u0018e \u0001(\t\u0012\u0017\n\u000ffeature_content\u0018f \u0001(\t\"\u00a2\u0001\n\u0004APNS\u0012\u0017\n\u000fmutable_content\u0018\u0001 \u0001(\u0003\u0012\u0011\n\tmedia_url\u0018\u0002 \u0001(\t\u0012\r\n\u0005sound\u0018\u0003 \u0001(\t\u0012\u0010\n\bcategory\u0018\u0004 \u0001(\t\u0012\u0011\n\tthread_id\u0018\u0005 \u0001(\t\u0012\r\n\u0005badge\u0018\u0006 \u0001(\u0003\u0012\u0016\n\u000ecustom_content\u0018\u0007 \u0001(\t\u0012\u0013\n\u000bcollapse_id\u0018\b \u0001(\t\"\u00ad\u0002\n\u0007Android\u0012\u0017\n\u000fsmall_image_url\u0018\u0001 \u0001(\t\u0012\u0015\n\rbig_image_url\u0018\u0002 \u0001(\t\u0012\u0011\n\taudio_url\u0018\u0003 \u0001(\t\u0012\u0019\n\u0011click_action_type\u0018\u0004 \u0001(\t\u0012\u001a\n\u0012click_action_param\u0018\u0005 \u0001(\t\u0012\r\n\u0005sound\u0018\u0006 \u0001(\b\u0012\u0016\n\u000esound_resource\u0018\u0007 \u0001(\t\u0012\u000f\n\u0007vibrate\u0018\b \u0001(\b\u0012\r\n\u0005light\u0018\t \u0001(\b\u0012\r\n\u0005badge\u0018\n \u0001(\u0003\u0012\u0016\n\u000ecustom_content\u0018\u000b \u0001(\t\u0012\u0011\n\tnotify_id\u0018\f \u0001(\u0005\u0012'\n\u0007channel\u0018d \u0001(\u000b2\u0016.Channel\"i\n\u0007Channel\u0012\u0012\n\nchannel_id\u0018\u0001 \u0001(\t\u0012\u0014\n\fchannel_name\u0018\u0002 \u0001(\t\u0012\u0018\n\u0010channel_group_id\u0018\u0003 \u0001(\t\u0012\u001a\n\u0012channel_group_name\u0018\u0004 \u0001(\t\"D\n\rSystemContent\u0012\u000f\n\u0007push_id\u0018\u0001 \u0001(\t\u0012\u0011\n\tfrom_mpay\u0018\u0002 \u0001(\b\u0012\u000f\n\u0007plan_id\u0018\u0003 \u0001(\t\"*\n\u0016AckReceiveNotification\u0012\u0010\n\bpush_ids\u0018\u0001 \u0003(\t\" \n\u001eFindOfflineNotificationRequest\"U\n\u001fFindOfflineNotificationResponse\u00122\n\rnotifications\u0018\u0001 \u0003(\u000b2\u001b.proto_sdkgate.Notificationb\u0006proto3";
        internal_static_proto_sdkgate_ErrMsg_descriptor = getDescriptor().getMessageTypes().get(0);
        internal_static_proto_sdkgate_ErrMsg_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_proto_sdkgate_ErrMsg_descriptor, new String[]{"ErrMsg"});
        internal_static_proto_sdkgate_EmptyMsg_descriptor = getDescriptor().getMessageTypes().get(1);
        internal_static_proto_sdkgate_EmptyMsg_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_proto_sdkgate_EmptyMsg_descriptor, new String[0]);
        internal_static_proto_sdkgate_PreRegisterRequest_descriptor = getDescriptor().getMessageTypes().get(2);
        internal_static_proto_sdkgate_PreRegisterRequest_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_proto_sdkgate_PreRegisterRequest_descriptor, new String[]{"ProductId", "ClientKey", "Channel", "Pkg"});
        internal_static_proto_sdkgate_PreRegisterResponse_descriptor = getDescriptor().getMessageTypes().get(3);
        internal_static_proto_sdkgate_PreRegisterResponse_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_proto_sdkgate_PreRegisterResponse_descriptor, new String[]{"Auth"});
        internal_static_proto_sdkgate_RegisterRequest_descriptor = getDescriptor().getMessageTypes().get(4);
        internal_static_proto_sdkgate_RegisterRequest_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_proto_sdkgate_RegisterRequest_descriptor, new String[]{"Auth", "Channel", "Pkg", "Regid", "PermitNotice", "AppVersion", "SdkVersion", "SystemVersion", "TimeZone", "DeviceBrand", "DeviceModel", "ApnsProduction", "ProductId"});
        internal_static_proto_sdkgate_RegisterResponse_descriptor = getDescriptor().getMessageTypes().get(5);
        internal_static_proto_sdkgate_RegisterResponse_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_proto_sdkgate_RegisterResponse_descriptor, new String[]{"Token", "AccessKey"});
        internal_static_proto_sdkgate_LoginRequest_descriptor = getDescriptor().getMessageTypes().get(6);
        internal_static_proto_sdkgate_LoginRequest_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_proto_sdkgate_LoginRequest_descriptor, new String[]{"Token", "AccessKey", "PermitNotice", "AppVersion", "SdkVersion", "SystemVersion", "TimeZone", "DeviceBrand", "DeviceModel", "Transid"});
        internal_static_proto_sdkgate_HeartbeatRequest_descriptor = getDescriptor().getMessageTypes().get(7);
        internal_static_proto_sdkgate_HeartbeatRequest_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_proto_sdkgate_HeartbeatRequest_descriptor, new String[0]);
        internal_static_proto_sdkgate_Notification_descriptor = getDescriptor().getMessageTypes().get(8);
        internal_static_proto_sdkgate_Notification_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_proto_sdkgate_Notification_descriptor, new String[]{"Title", "SubTitle", "Content", "Silent", "Apns", "Android", "SystemContent", "FeatureTitle", "FeatureSubTitle", "FeatureContent"});
        internal_static_proto_sdkgate_APNS_descriptor = getDescriptor().getMessageTypes().get(9);
        internal_static_proto_sdkgate_APNS_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_proto_sdkgate_APNS_descriptor, new String[]{"MutableContent", "MediaUrl", "Sound", "Category", "ThreadId", "Badge", "CustomContent", "CollapseId"});
        internal_static_proto_sdkgate_Android_descriptor = getDescriptor().getMessageTypes().get(10);
        internal_static_proto_sdkgate_Android_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_proto_sdkgate_Android_descriptor, new String[]{"SmallImageUrl", "BigImageUrl", "AudioUrl", "ClickActionType", "ClickActionParam", "Sound", "SoundResource", "Vibrate", "Light", "Badge", "CustomContent", "NotifyId", "Channel"});
        internal_static_proto_sdkgate_Channel_descriptor = getDescriptor().getMessageTypes().get(11);
        internal_static_proto_sdkgate_Channel_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_proto_sdkgate_Channel_descriptor, new String[]{"ChannelId", "ChannelName", "ChannelGroupId", "ChannelGroupName"});
        internal_static_proto_sdkgate_SystemContent_descriptor = getDescriptor().getMessageTypes().get(12);
        internal_static_proto_sdkgate_SystemContent_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_proto_sdkgate_SystemContent_descriptor, new String[]{"PushId", "FromMpay", "PlanId"});
        internal_static_proto_sdkgate_AckReceiveNotification_descriptor = getDescriptor().getMessageTypes().get(13);
        internal_static_proto_sdkgate_AckReceiveNotification_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_proto_sdkgate_AckReceiveNotification_descriptor, new String[]{"PushIds"});
        internal_static_proto_sdkgate_FindOfflineNotificationRequest_descriptor = getDescriptor().getMessageTypes().get(14);
        internal_static_proto_sdkgate_FindOfflineNotificationRequest_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_proto_sdkgate_FindOfflineNotificationRequest_descriptor, new String[0]);
        internal_static_proto_sdkgate_FindOfflineNotificationResponse_descriptor = getDescriptor().getMessageTypes().get(15);
        internal_static_proto_sdkgate_FindOfflineNotificationResponse_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_proto_sdkgate_FindOfflineNotificationResponse_descriptor, new String[]{"Notifications"});
    }
}