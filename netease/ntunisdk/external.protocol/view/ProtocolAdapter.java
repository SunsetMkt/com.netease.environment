package com.netease.ntunisdk.external.protocol.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.netease.ntunisdk.external.protocol.data.ProtocolInfo;
import com.netease.ntunisdk.protocollib.R;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class ProtocolAdapter extends RecyclerView.Adapter<ProtocolItemViewHolder> {
    private final Context mContext;
    private final OpenProtocolCallback mOpenProtocolCallback;
    private final ArrayList<ProtocolInfo.ConcreteSubProtocol> mSubProtocolInfos;

    public interface OpenProtocolCallback {
        void onOpen(ProtocolInfo.ConcreteSubProtocol concreteSubProtocol);
    }

    public ProtocolAdapter(Context context, ArrayList<ProtocolInfo.ConcreteSubProtocol> arrayList, OpenProtocolCallback openProtocolCallback) {
        this.mContext = context;
        this.mSubProtocolInfos = arrayList;
        this.mOpenProtocolCallback = openProtocolCallback;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ProtocolItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ProtocolItemViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.unisdk_protocol__item, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ProtocolItemViewHolder protocolItemViewHolder, int i) {
        protocolItemViewHolder.onBind(this.mSubProtocolInfos.get(i), this.mOpenProtocolCallback);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        ArrayList<ProtocolInfo.ConcreteSubProtocol> arrayList = this.mSubProtocolInfos;
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    public static class ProtocolItemViewHolder extends RecyclerView.ViewHolder {
        private final CheckBox mCheckBox;
        private final View mCheckBoxFrame;
        private final TextView mOpenView;
        private final View mRootView;
        private final TextView mTitleView;

        public ProtocolItemViewHolder(View view) {
            super(view);
            this.mRootView = view;
            this.mCheckBoxFrame = view.findViewById(R.id.unisdk_protocol_check_box_frame);
            this.mCheckBox = (CheckBox) view.findViewById(R.id.unisdk_protocol_check_box);
            this.mTitleView = (TextView) view.findViewById(R.id.unisdk_protocol_title);
            this.mOpenView = (TextView) view.findViewById(R.id.unisdk_protocol_open);
        }

        void onBind(final ProtocolInfo.ConcreteSubProtocol concreteSubProtocol, final OpenProtocolCallback openProtocolCallback) {
            this.mCheckBoxFrame.setVisibility(concreteSubProtocol.isCanAccept() ? 0 : 8);
            this.mCheckBox.setVisibility(concreteSubProtocol.isCanAccept() ? 0 : 8);
            this.itemView.setClickable(concreteSubProtocol.isCanAccept());
            View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.netease.ntunisdk.external.protocol.view.ProtocolAdapter.ProtocolItemViewHolder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (concreteSubProtocol.isCanAccept()) {
                        ProtocolItemViewHolder.this.mCheckBox.setChecked(!ProtocolItemViewHolder.this.mCheckBox.isChecked());
                        concreteSubProtocol.setChecked(ProtocolItemViewHolder.this.mCheckBox.isChecked());
                        concreteSubProtocol.setWarning(false);
                        ProtocolItemViewHolder.this.mRootView.setBackgroundResource(R.drawable.unisdk_protocol_protocol_item_background);
                    }
                }
            };
            this.mRootView.setOnClickListener(onClickListener);
            this.mCheckBoxFrame.setOnClickListener(onClickListener);
            this.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.netease.ntunisdk.external.protocol.view.ProtocolAdapter.ProtocolItemViewHolder.2
                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    concreteSubProtocol.setChecked(z);
                    if (z) {
                        concreteSubProtocol.setWarning(false);
                        ProtocolItemViewHolder.this.mRootView.setBackgroundResource(R.drawable.unisdk_protocol_protocol_item_background);
                    }
                }
            });
            this.mCheckBox.setChecked(concreteSubProtocol.isChecked());
            this.mTitleView.setText(concreteSubProtocol.getProtocolNamePrefix() + concreteSubProtocol.mSubProtocolInfo.mName);
            if (concreteSubProtocol.isWarning()) {
                this.mRootView.setBackgroundResource(R.drawable.unisdk_protocol_protocol_item_background_warning);
            } else {
                this.mRootView.setBackgroundResource(R.drawable.unisdk_protocol_protocol_item_background);
            }
            View.OnClickListener onClickListener2 = new View.OnClickListener() { // from class: com.netease.ntunisdk.external.protocol.view.ProtocolAdapter.ProtocolItemViewHolder.3
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    OpenProtocolCallback openProtocolCallback2 = openProtocolCallback;
                    if (openProtocolCallback2 != null) {
                        openProtocolCallback2.onOpen(concreteSubProtocol);
                    }
                }
            };
            this.mTitleView.setOnClickListener(onClickListener);
            this.mOpenView.setOnClickListener(onClickListener2);
        }
    }
}