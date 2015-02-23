package org.ei.opensrp.view.dialog;

import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.Context;

public class FPDMPAServiceMode extends FPAllMethodsServiceMode {

    public FPDMPAServiceMode(SmartRegisterClientsProvider provider) {
        super(provider);
    }

    @Override
    public String name() {
        return Context.getInstance().getStringResource(org.ei.opensrp.R.string.fp_register_service_mode_dmpa);
    }
}
