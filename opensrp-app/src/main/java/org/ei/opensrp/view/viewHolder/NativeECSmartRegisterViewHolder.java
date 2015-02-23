package org.ei.opensrp.view.viewHolder;

import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.ei.opensrp.view.customControls.ClientChildrenView;
import org.ei.opensrp.view.customControls.ClientFpMethodView;
import org.ei.opensrp.view.customControls.ClientGplsaView;
import org.ei.opensrp.view.customControls.ClientProfileView;
import org.ei.opensrp.view.customControls.ClientStatusView;

public class NativeECSmartRegisterViewHolder {
    private final ClientProfileView profileInfoLayout;
    private final TextView txtECNumberView;
    private final ClientGplsaView gplsaLayout;
    private final ClientFpMethodView fpMethodview;
    private final ClientChildrenView childrenView;
    private final ClientStatusView statusView;
    private final ImageButton editButton;


    public NativeECSmartRegisterViewHolder(ViewGroup itemView) {
        this.profileInfoLayout = (ClientProfileView) itemView.findViewById(org.ei.opensrp.R.id.profile_info_layout);
        this.profileInfoLayout.initialize();

        this.txtECNumberView = (TextView) itemView.findViewById(org.ei.opensrp.R.id.txt_ec_number);

        this.gplsaLayout = (ClientGplsaView) itemView.findViewById(org.ei.opensrp.R.id.gplsa_layout);
        this.gplsaLayout.initialize();

        fpMethodview = (ClientFpMethodView) itemView.findViewById(org.ei.opensrp.R.id.fp_method_layout);
        fpMethodview.initialize();


        childrenView = (ClientChildrenView) itemView.findViewById(org.ei.opensrp.R.id.children_layout);
        childrenView.initialize();

        statusView = (ClientStatusView) itemView.findViewById(org.ei.opensrp.R.id.status_layout);
        statusView.initialize();

        this.editButton = (ImageButton) itemView.findViewById(org.ei.opensrp.R.id.btn_edit);
    }

    public ClientProfileView profileInfoLayout() {
        return profileInfoLayout;
    }

    public TextView txtECNumberView() {
        return txtECNumberView;
    }

    public ClientGplsaView gplsaLayout() {
        return gplsaLayout;
    }

    public ClientFpMethodView fpMethodView() {
        return fpMethodview;
    }

    public ClientChildrenView childrenView() {
        return childrenView;
    }
    public ImageButton editButton() {
        return editButton;
    }

    public ClientStatusView statusView() {
        return statusView;
    }

}