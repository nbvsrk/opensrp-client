package org.ei.opensrp.view.dialog;

import org.ei.opensrp.provider.SmartRegisterClientsProvider;

import static org.ei.opensrp.Context.getInstance;

public class FPCondomServiceMode extends FPAllMethodsServiceMode {

    public FPCondomServiceMode(SmartRegisterClientsProvider provider) {
        super(provider);
    }

    @Override
    public String name() {
        return getInstance().getStringResource(org.ei.opensrp.R.string.fp_register_service_mode_condom);
    }
}
