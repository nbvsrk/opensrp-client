package org.ei.opensrp.view.dialog;

import org.ei.opensrp.Context;
import org.ei.opensrp.view.contract.SmartRegisterClients;
import org.ei.opensrp.view.contract.SmartRegisterClient;

import java.util.Collections;

public class ChildHighRiskSort implements SortOption {
    @Override
    public String name() {
        return Context.getInstance().getStringResource(org.ei.opensrp.R.string.sort_by_child_hr);
    }

    @Override
    public SmartRegisterClients sort(SmartRegisterClients allClients) {
        Collections.sort(allClients, SmartRegisterClient.HR_COMPARATOR);
        return allClients;
    }
}
