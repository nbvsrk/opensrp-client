package org.ei.opensrp.view.customControls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.apache.commons.lang3.StringUtils;
import org.ei.opensrp.view.contract.ECSmartRegisterClient;
import org.ei.opensrp.view.viewHolder.ViewStubInflater;
import org.ei.opensrp.util.DateUtil;
import org.ei.opensrp.view.controller.ECSmartRegisterController;

import java.util.HashMap;
import java.util.Map;

import static org.ei.opensrp.util.DateUtil.formatDate;

public class ClientStatusView extends FrameLayout {

    private Map<String, ViewStubInflater> statusLayouts;

    @SuppressWarnings("UnusedDeclaration")
    public ClientStatusView(Context context) {
        this(context, null, 0);
    }

    @SuppressWarnings("UnusedDeclaration")
    public ClientStatusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClientStatusView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void initialize() {
        this.statusLayouts = new HashMap<String, ViewStubInflater>();
        ViewStubInflater commonECAndFPLayout = new ViewStubInflater((ViewStub) findViewById(org.ei.opensrp.R.id.ec_and_fp_status_layout));

        this.statusLayouts
                .put(ECSmartRegisterController.EC_STATUS, commonECAndFPLayout);
        this.statusLayouts
                .put(ECSmartRegisterController.FP_STATUS, commonECAndFPLayout);
        this.statusLayouts
                .put(ECSmartRegisterController.ANC_STATUS, new ViewStubInflater((ViewStub) findViewById(org.ei.opensrp.R.id.anc_status_layout)));
        this.statusLayouts
                .put(ECSmartRegisterController.PNC_STATUS, new ViewStubInflater((ViewStub) findViewById(org.ei.opensrp.R.id.pnc_status_layout)));
        this.statusLayouts
                .put(ECSmartRegisterController.PNC_FP_STATUS, new ViewStubInflater((ViewStub) findViewById(org.ei.opensrp.R.id.pnc_and_fp_status_layout)));
    }

    public void bindData(ECSmartRegisterClient client) {
        hideAllLayout();

        if (client.status().containsKey(ECSmartRegisterController.STATUS_TYPE_FIELD)) {
            String statusType = client.status().get(ECSmartRegisterController.STATUS_TYPE_FIELD);
            String statusDate = DateUtil.formatDate(client.status().get(ECSmartRegisterController.STATUS_DATE_FIELD));

            ViewGroup statusViewGroup = statusLayout(statusType);
            statusViewGroup.setVisibility(View.VISIBLE);
            dateView(statusViewGroup).setText(statusDate);

            if (ECSmartRegisterController.EC_STATUS.equalsIgnoreCase(statusType) || ECSmartRegisterController.FP_STATUS.equalsIgnoreCase(statusType)) {
                typeView(statusViewGroup)
                        .setText(StringUtils.upperCase(statusType));
            } else if (ECSmartRegisterController.ANC_STATUS.equalsIgnoreCase(statusType)) {
                eddDateView(statusViewGroup)
                        .setText(DateUtil.formatDate(client.status().get(ECSmartRegisterController.STATUS_EDD_FIELD)));
            } else if (ECSmartRegisterController.PNC_FP_STATUS.equalsIgnoreCase(statusType)) {
                fpDateView(statusViewGroup)
                        .setText(DateUtil.formatDate(client.status().get(ECSmartRegisterController.FP_METHOD_DATE_FIELD)));
            }
        }
    }

    public ViewGroup statusLayout(String statusType) {
        return statusLayouts.get(statusType).get();
    }

    public void hideAllLayout() {
        for (String statusLayout : statusLayouts.keySet()) {
            statusLayouts.get(statusLayout).setVisibility(View.GONE);
        }
    }

    public TextView dateView(ViewGroup statusViewGroup) {
        return ((TextView) statusViewGroup.findViewById(org.ei.opensrp.R.id.txt_status_date));
    }

    public TextView typeView(ViewGroup statusViewGroup) {
        return ((TextView) statusViewGroup.findViewById(org.ei.opensrp.R.id.txt_status_type));
    }

    public TextView eddDateView(ViewGroup statusViewGroup) {
        return ((TextView) statusViewGroup.findViewById(org.ei.opensrp.R.id.txt_anc_status_edd_date));
    }

    public TextView fpDateView(ViewGroup statusViewGroup) {
        return ((TextView) statusViewGroup.findViewById(org.ei.opensrp.R.id.txt_fp_status_date));
    }
}
