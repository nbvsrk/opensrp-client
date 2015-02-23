package org.ei.opensrp.view.dialog;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;
import org.apache.commons.lang3.StringUtils;
import org.ei.opensrp.Context;
import org.ei.opensrp.domain.ANCServiceType;
import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.view.contract.AlertStatus;
import org.ei.opensrp.view.contract.pnc.PNCSmartRegisterClient;
import org.ei.opensrp.AllConstants;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;
import org.ei.opensrp.view.contract.ANCSmartRegisterClient;
import org.ei.opensrp.view.contract.AlertDTO;
import org.ei.opensrp.view.contract.ChildSmartRegisterClient;
import org.ei.opensrp.view.contract.FPSmartRegisterClient;
import org.ei.opensrp.view.contract.ServiceProvidedDTO;
import org.ei.opensrp.view.viewHolder.NativeANCSmartRegisterViewHolder;
import org.ei.opensrp.view.viewHolder.NativeChildSmartRegisterViewHolder;
import org.ei.opensrp.view.viewHolder.NativeFPSmartRegisterViewHolder;
import org.ei.opensrp.view.viewHolder.NativePNCSmartRegisterViewHolder;
import org.ei.opensrp.view.viewHolder.OnClickFormLauncher;

import static android.view.View.VISIBLE;
import static org.ei.opensrp.view.contract.AlertStatus.COMPLETE;
import static org.ei.opensrp.view.contract.AlertStatus.INPROCESS;

public class ANCOverviewServiceMode extends ServiceModeOption {

    private Drawable iconPencilDrawable;


    public ANCOverviewServiceMode(SmartRegisterClientsProvider provider) {
        super(provider);
    }

    @Override
    public String name() {
        return Context.getInstance().getStringResource(org.ei.opensrp.R.string.anc_service_mode_overview);
    }

    @Override
    public SecuredNativeSmartRegisterActivity.ClientsHeaderProvider getHeaderProvider() {
        return new SecuredNativeSmartRegisterActivity.ClientsHeaderProvider() {
            @Override
            public int count() {
                return 7;
            }

            @Override
            public int weightSum() {
                return 100;
            }

            @Override
            public int[] weights() {
                return new int[]{21, 9, 12, 12, 12, 12, 22};
            }

            @Override
            public int[] headerTextResourceIds() {
                return new int[]{
                        org.ei.opensrp.R.string.header_name, org.ei.opensrp.R.string.header_id, org.ei.opensrp.R.string.header_anc_status,
                        org.ei.opensrp.R.string.header_risk_factors, org.ei.opensrp.R.string.header_visits, org.ei.opensrp.R.string.header_tt, org.ei.opensrp.R.string.header_ifa};
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
        viewHolder.serviceModeOverviewView().setVisibility(VISIBLE);

        setupRiskFactorsView(client, viewHolder);
        setupANCVisitLayout(client, viewHolder);
        setupTTLayout(client, viewHolder);
        setupIFALayout(client, viewHolder);
        setupEditView(client, viewHolder, clientSectionClickListener);
    }

    private void setupRiskFactorsView(ANCSmartRegisterClient client, NativeANCSmartRegisterViewHolder viewHolder) {
        viewHolder.txtRiskFactors().setText(client.riskFactors());
    }

    @Override
    public void setupListView(FPSmartRegisterClient client, NativeFPSmartRegisterViewHolder viewHolder, View.OnClickListener clientSectionClickListener) {

    }

    @Override
    public void setupListView(PNCSmartRegisterClient client, NativePNCSmartRegisterViewHolder viewHolder, View.OnClickListener clientSectionClickListener) {

    }

    public void setupANCVisitLayout(ANCSmartRegisterClient client,
                                    NativeANCSmartRegisterViewHolder viewHolder) {
        if (client.isVisitsDone()) {
            viewHolder.txtANCVisitDoneOn().setVisibility(VISIBLE);
            viewHolder.txtANCVisitDoneOn().setText(client.visitDoneDateWithVisitName());
        } else {
            viewHolder.txtANCVisitDoneOn().setVisibility(View.INVISIBLE);
        }

        AlertDTO ancVisitAlert = client.getAlert(ANCServiceType.ANC_1);
        if (ancVisitAlert != AlertDTO.emptyAlert) {
            viewHolder.btnAncVisitView().setVisibility(View.INVISIBLE);
            viewHolder.layoutANCVisitAlert().setVisibility(VISIBLE);
            viewHolder.layoutANCVisitAlert().setOnClickListener(launchForm(client, ancVisitAlert, AllConstants.FormNames.ANC_VISIT));
            setAlertLayout(viewHolder.layoutANCVisitAlert(),
                    viewHolder.txtANCVisitDueType(),
                    ancVisitAlert);
            setAlertDateDetails(client, ancVisitAlert, viewHolder.txtANCVisitAlertDueOn());
        } else {
            viewHolder.layoutANCVisitAlert().setVisibility(View.INVISIBLE);
            viewHolder.btnAncVisitView().setVisibility(View.INVISIBLE);
            viewHolder.btnAncVisitView().setOnClickListener(launchForm(client, ancVisitAlert, AllConstants.FormNames.ANC_VISIT));
        }
    }

    private void setAlertDateDetails(ANCSmartRegisterClient client, AlertDTO alert, TextView dateView) {
        ServiceProvidedDTO servicesProvided = client.getServiceProvidedDTO(alert.name());
        if (isAlertStatusCompleteOrInProcess(alert) && servicesProvided != null) {
            setAlertDate(dateView, alert, servicesProvided.ancServicedOn());
        } else
            setAlertDate(dateView, alert, null);
    }

    private boolean isAlertStatusCompleteOrInProcess(AlertDTO ancVisitAlert) {
        return ancVisitAlert.status().equalsIgnoreCase(INPROCESS.name())
                || ancVisitAlert.status().equalsIgnoreCase(COMPLETE.name());
    }

    public void setupTTLayout(ANCSmartRegisterClient client,
                              NativeANCSmartRegisterViewHolder viewHolder) {
        if (client.isTTDone()) {
            viewHolder.txtTTDoneOn().setVisibility(VISIBLE);
            viewHolder.txtTTDoneOn().setText(client.ttDoneDate());
        } else {
            viewHolder.txtTTDoneOn().setVisibility(View.INVISIBLE);
        }

        AlertDTO ttAlert = client.getAlert(ANCServiceType.TT_1);
        if (ttAlert != AlertDTO.emptyAlert) {
            viewHolder.btnTTView().setVisibility(View.INVISIBLE);
            viewHolder.layoutTTAlert().setVisibility(VISIBLE);
            viewHolder.layoutTTAlert().setOnClickListener(launchForm(client, ttAlert, AllConstants.FormNames.TT));
            setAlertLayout(viewHolder.layoutTTAlert(),
                    viewHolder.txtTTDueType(),
                    ttAlert);
            setAlertDateDetails(client, ttAlert, viewHolder.txtTTDueOn());
        } else {
            viewHolder.layoutTTAlert().setVisibility(View.INVISIBLE);
            viewHolder.btnTTView().setVisibility(View.INVISIBLE);
            viewHolder.btnTTView().setOnClickListener(launchForm(client, ttAlert, AllConstants.FormNames.TT));
        }
    }

    public void setupIFALayout(ANCSmartRegisterClient client,
                               NativeANCSmartRegisterViewHolder viewHolder) {
        if (client.isIFADone()) {
            viewHolder.txtIFADoneOn().setVisibility(VISIBLE);
            viewHolder.txtIFADoneOn().setText(client.ifaDoneDate());
        } else {
            viewHolder.txtIFADoneOn().setVisibility(View.INVISIBLE);
        }

        AlertDTO ifaAlert = client.getAlert(ANCServiceType.IFA);
        if (ifaAlert != AlertDTO.emptyAlert) {
            viewHolder.btnIFAView().setVisibility(View.INVISIBLE);
            viewHolder.layoutIFAAlert().setVisibility(VISIBLE);
            viewHolder.layoutIFAAlert().setOnClickListener(launchForm(client, ifaAlert, AllConstants.FormNames.IFA));
            setAlertLayout(viewHolder.layoutIFAAlert(),
                    viewHolder.txtIFADueType(),
                    ifaAlert);
            viewHolder.txtIFADueType().setText(ifaAlert.name());
            setAlertDateDetails(client, ifaAlert, viewHolder.txtIFADueOn());
        } else {
            viewHolder.layoutIFAAlert().setVisibility(View.INVISIBLE);
            viewHolder.btnIFAView().setVisibility(View.INVISIBLE);
            viewHolder.btnIFAView().setOnClickListener(launchForm(client, ifaAlert, AllConstants.FormNames.IFA));
        }
    }

    private void setupEditView(ANCSmartRegisterClient client, NativeANCSmartRegisterViewHolder viewHolder, View.OnClickListener onClickListener) {
        if (iconPencilDrawable == null) {
            iconPencilDrawable = Context.getInstance().getDrawableResource(org.ei.opensrp.R.drawable.ic_pencil);
        }
        viewHolder.btnEditView().setImageDrawable(iconPencilDrawable);
        viewHolder.btnEditView().setOnClickListener(onClickListener);
        viewHolder.btnEditView().setTag(client);
    }

    private OnClickFormLauncher launchForm(ANCSmartRegisterClient client, AlertDTO alert, String formName) {
        return provider().newFormLauncher(formName, client.entityId(), "{\"entityId\":\"" + client.entityId() + "\",\"alertName\":\"" + alert.name() + "\"}");
    }

    private void setAlertLayout(View layout, TextView typeView,
                                AlertDTO alert) {

        typeView.setText(alert.ancServiceType().shortName());

        final AlertStatus alertStatus = alert.alertStatus();
        layout.setBackgroundResource(alertStatus.backgroundColorResourceId());
        typeView.setTextColor(alertStatus.fontColor());
    }

    private void setAlertDate(TextView dateView, AlertDTO alert, String serviceDate) {
        if (StringUtils.isNotEmpty(serviceDate))
            dateView.setText(serviceDate);
        else
            dateView.setText(Context.getInstance().getStringResource(org.ei.opensrp.R.string.str_due) + alert.shortDate());
        dateView.setTextColor(alert.alertStatus().fontColor());
    }

}
