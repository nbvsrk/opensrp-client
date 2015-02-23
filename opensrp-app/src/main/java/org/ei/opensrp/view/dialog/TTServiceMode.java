package org.ei.opensrp.view.dialog;

import android.view.View;
import android.widget.TextView;

import org.ei.opensrp.domain.ANCServiceType;
import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.view.contract.pnc.PNCSmartRegisterClient;
import org.ei.opensrp.AllConstants;
import org.ei.opensrp.Context;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;
import org.ei.opensrp.view.contract.ANCSmartRegisterClient;
import org.ei.opensrp.view.contract.AlertDTO;
import org.ei.opensrp.view.contract.AlertStatus;
import org.ei.opensrp.view.contract.ChildSmartRegisterClient;
import org.ei.opensrp.view.contract.FPSmartRegisterClient;
import org.ei.opensrp.view.contract.ServiceProvidedDTO;
import org.ei.opensrp.view.viewHolder.NativeANCSmartRegisterViewHolder;
import org.ei.opensrp.view.viewHolder.NativeChildSmartRegisterViewHolder;
import org.ei.opensrp.view.viewHolder.NativeFPSmartRegisterViewHolder;
import org.ei.opensrp.view.viewHolder.NativePNCSmartRegisterViewHolder;
import org.ei.opensrp.view.viewHolder.OnClickFormLauncher;

import static android.view.View.VISIBLE;

public class TTServiceMode extends ServiceModeOption {

    public TTServiceMode(SmartRegisterClientsProvider provider) {
        super(provider);
    }

    @Override
    public String name() {
        return Context.getInstance().getStringResource(org.ei.opensrp.R.string.anc_service_mode_tt);
    }

    @Override
    public SecuredNativeSmartRegisterActivity.ClientsHeaderProvider getHeaderProvider() {
        return new SecuredNativeSmartRegisterActivity.ClientsHeaderProvider() {
            @Override
            public int count() {
                return 6;
            }

            @Override
            public int weightSum() {
                return 100;
            }

            @Override
            public int[] weights() {
                return new int[]{21, 9, 12, 19, 19, 20, 20};
            }

            @Override
            public int[] headerTextResourceIds() {
                return new int[]{
                        org.ei.opensrp.R.string.header_name, org.ei.opensrp.R.string.header_id, org.ei.opensrp.R.string.header_anc_status,
                        org.ei.opensrp.R.string.header_tt_1, org.ei.opensrp.R.string.header_tt_2, org.ei.opensrp.R.string.header_tt_booster};
            }
        };
    }

    @Override
    public void setupListView(ChildSmartRegisterClient client, NativeChildSmartRegisterViewHolder viewHolder, View.OnClickListener clientSectionClickListener) {

    }

    @Override
    public void setupListView(ANCSmartRegisterClient client,
                              NativeANCSmartRegisterViewHolder viewHolder,
                              View.OnClickListener clientSectionClickListener) {
        viewHolder.serviceModeTTView().setVisibility(VISIBLE);

        setupTT1Layout(client, viewHolder);
        setupTT2Layout(client, viewHolder);
        setupTTBoosterLayout(client, viewHolder);
    }

    @Override
    public void setupListView(FPSmartRegisterClient client, NativeFPSmartRegisterViewHolder viewHolder, View.OnClickListener clientSectionClickListener) {

    }

    @Override
    public void setupListView(PNCSmartRegisterClient client, NativePNCSmartRegisterViewHolder viewHolder, View.OnClickListener clientSectionClickListener) {

    }

    public void setupTT1Layout(ANCSmartRegisterClient client,
                               NativeANCSmartRegisterViewHolder viewHolder) {
        AlertDTO ttAlert = client.getAlert(ANCServiceType.TT_1);
        ServiceProvidedDTO ttServiceProvided = client.getServiceProvidedDTO(ANCServiceType.TT_1.serviceName());
        viewHolder.hideViewsInTT1Layout();
        if (ttServiceProvided != null) {
            setServiceProvidedLayout(ttServiceProvided,
                    viewHolder.layoutTT1Alert(), viewHolder.txtTT1DoneTick(),
                    viewHolder.txtTT1Type(), viewHolder.txtTT1Date());
        } else if (ttAlert != AlertDTO.emptyAlert && ttAlert.name().equalsIgnoreCase(ANCServiceType.TT_1.serviceName())) {
            setAlertLayout(viewHolder.layoutTT1Alert(),
                    viewHolder.txtTT1Type(),
                    viewHolder.txtTT1Date(), client
                    , ttAlert);
        }
    }

    public void setupTT2Layout(ANCSmartRegisterClient client,
                               NativeANCSmartRegisterViewHolder viewHolder) {
        AlertDTO ttAlert = client.getAlert(ANCServiceType.TT_2);
        ServiceProvidedDTO ttServiceProvided = client.getServiceProvidedDTO(ANCServiceType.TT_2.serviceName());
        viewHolder.hideViewsInTT2Layout();
        if (ttServiceProvided != null) {
            setServiceProvidedLayout(ttServiceProvided,
                    viewHolder.layoutTT2Alert(), viewHolder.txtTT2DoneTick(),
                    viewHolder.txtTT2Type(), viewHolder.txtTT2Date());
        } else if (ttAlert != AlertDTO.emptyAlert && ttAlert.name().equalsIgnoreCase(ANCServiceType.TT_2.serviceName())) {
            setAlertLayout(viewHolder.layoutTT2Alert(),
                    viewHolder.txtTT2Type(),
                    viewHolder.txtTT2Date(), client
                    , ttAlert);
        }
    }

    public void setupTTBoosterLayout(ANCSmartRegisterClient client,
                                     NativeANCSmartRegisterViewHolder viewHolder) {
        AlertDTO ttAlert = client.getAlert(ANCServiceType.TT_BOOSTER);
        ServiceProvidedDTO ttServiceProvided = client.getServiceProvidedDTO(ANCServiceType.TT_BOOSTER.serviceName());
        viewHolder.hideViewsInTTBoosterLayout();
        if (ttServiceProvided != null) {
            setServiceProvidedLayout(ttServiceProvided,
                    viewHolder.layoutTTBoosterAlert(), viewHolder.txtTTBoosterDoneTick(),
                    viewHolder.txtTTBoosterType(), viewHolder.txtTTBoosterDate());
        } else if (ttAlert != AlertDTO.emptyAlert && ttAlert.name().equalsIgnoreCase(ANCServiceType.TT_BOOSTER.serviceName())) {
            setAlertLayout(viewHolder.layoutTTBoosterAlert(),
                    viewHolder.txtTTBoosterType(),
                    viewHolder.txtTTBoosterDate(), client
                    , ttAlert);
        }
    }

    private void setServiceProvidedLayout(ServiceProvidedDTO ttServiceProvided, View serviceProvidedLayout,
                                          TextView txtDoneTick, TextView txtTTType, TextView txtTTDate) {
        serviceProvidedLayout.setVisibility(View.VISIBLE);
        serviceProvidedLayout.setBackgroundResource(org.ei.opensrp.R.color.status_bar_text_almost_white);

        txtDoneTick.setVisibility(View.VISIBLE);

        txtTTType.setVisibility(VISIBLE);
        txtTTType.setText(ttServiceProvided.name());
        txtTTType.setTextColor(Context.getInstance().getColorResource(org.ei.opensrp.R.color.text_black));

        txtTTDate.setVisibility(VISIBLE);
        txtTTDate.setText("On " + ttServiceProvided.shortDate());
        txtTTDate.setTextColor(Context.getInstance().getColorResource(org.ei.opensrp.R.color.text_black));
    }

    private OnClickFormLauncher launchForm(String formName, ANCSmartRegisterClient client, AlertDTO alert) {
        return provider().newFormLauncher(formName, client.entityId(), "{\"entityId\":\"" + client.entityId() + "\",\"alertName\":\"" + alert.name() + "\"}");
    }

    private void setAlertLayout(View layout, TextView typeView,
                                TextView dateView, ANCSmartRegisterClient client, AlertDTO alert) {
        layout.setVisibility(View.VISIBLE);
        layout.setOnClickListener(launchForm(AllConstants.FormNames.TT, client, alert));
        typeView.setVisibility(View.VISIBLE);
        dateView.setVisibility(View.VISIBLE);
        typeView.setText(alert.name());
        dateView.setText("Due " + alert.shortDate());

        final AlertStatus alertStatus = alert.alertStatus();
        layout.setBackgroundResource(alertStatus.backgroundColorResourceId());
        typeView.setTextColor(alertStatus.fontColor());
        dateView.setTextColor(alertStatus.fontColor());
    }

}
