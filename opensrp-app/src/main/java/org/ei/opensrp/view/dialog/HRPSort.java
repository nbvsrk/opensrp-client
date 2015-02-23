package org.ei.opensrp.view.dialog;

import org.ei.opensrp.Context;

public class HRPSort extends HighRiskSort {
    @Override
    public String name() {
        return Context.getInstance().getStringResource(org.ei.opensrp.R.string.sort_by_high_risk_pregnancy_label);
    }

}
