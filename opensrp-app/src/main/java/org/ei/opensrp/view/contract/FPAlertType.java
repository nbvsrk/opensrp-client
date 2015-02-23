package org.ei.opensrp.view.contract;

import org.ei.opensrp.AllConstants;

public enum FPAlertType {

    FOLLOW_UP("follow-up") {
        @Override
        public String getAlertType() {
            return "Follow Up";
        }

        @Override
        public String getFormName() {
            return AllConstants.FormNames.FP_FOLLOWUP;
        }
    },
    REFERRAL("referral") {
        @Override
        public String getAlertType() {
            return "Referral Follow Up";
        }

        @Override
        public String getFormName() {
            return AllConstants.FormNames.FP_REFERRAL_FOLLOWUP;
        }
    },
    REFILL("refill") {
        @Override
        public String getAlertType() {
            return "Refill";
        }

        @Override
        public String getFormName() {
            return AllConstants.FormNames.RENEW_FP_PRODUCT;
        }
    };

    private String alertType;

    FPAlertType(String alertType) {
        this.alertType = alertType;
    }

    @Override
    public String toString() {
        return this.alertType;
    }

    public abstract String getAlertType();

    public abstract String getFormName();

    public static FPAlertType from(String value) {
        if (value != null) {
            for (FPAlertType type : FPAlertType.values()) {
                if (value.equalsIgnoreCase(type.toString())) {
                    return type;
                }
            }
        }
        return null;

    }

}
