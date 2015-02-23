package org.ei.opensrp.view.dialog;

import org.ei.opensrp.Context;
import org.ei.opensrp.view.contract.SmartRegisterClients;
import org.ei.opensrp.view.contract.SmartRegisterClient;

import java.util.Collections;

public class STSort implements SortOption {
    @Override
    public String name() {
        return Context.getInstance().getStringResource(org.ei.opensrp.R.string.sort_by_st_label);
    }

    @Override
    public SmartRegisterClients sort(SmartRegisterClients allClients) {
        Collections.sort(allClients, SmartRegisterClient.ST_COMPARATOR);
        return allClients;
    }
}
