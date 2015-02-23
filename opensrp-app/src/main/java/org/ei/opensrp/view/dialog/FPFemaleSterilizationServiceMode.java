package org.ei.opensrp.view.dialog;

import org.ei.opensrp.provider.SmartRegisterClientsProvider;

import static org.ei.opensrp.Context.getInstance;

public class FPFemaleSterilizationServiceMode extends FPAllMethodsServiceMode {

    public FPFemaleSterilizationServiceMode(SmartRegisterClientsProvider provider) {
        super(provider);
    }

    @Override
    public String name() {
        return getInstance().getStringResource(org.ei.opensrp.R.string.fp_register_service_mode_female_sterilization);
    }
}
