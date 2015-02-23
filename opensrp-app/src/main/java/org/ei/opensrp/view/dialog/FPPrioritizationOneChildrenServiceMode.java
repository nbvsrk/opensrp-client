package org.ei.opensrp.view.dialog;

import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.Context;

public class FPPrioritizationOneChildrenServiceMode extends FPPrioritizationAllECServiceMode {

    public FPPrioritizationOneChildrenServiceMode(SmartRegisterClientsProvider provider) {
        super(provider);
    }

    @Override
    public String name() {
        return Context.getInstance().getStringResource(org.ei.opensrp.R.string.fp_prioritization_one_child_service_mode);
    }
}
