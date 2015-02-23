package org.ei.opensrp.view.dialog;

import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.Context;

public class FPPrioritizationTwoPlusChildrenServiceMode extends FPPrioritizationAllECServiceMode {

    public FPPrioritizationTwoPlusChildrenServiceMode(SmartRegisterClientsProvider provider) {
        super(provider);
    }

    @Override
    public String name() {
        return Context.getInstance().getStringResource(org.ei.opensrp.R.string.fp_prioritization_two_plus_children_service_mode);
    }
}
