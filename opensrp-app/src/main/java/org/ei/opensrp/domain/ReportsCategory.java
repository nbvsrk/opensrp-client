package org.ei.opensrp.domain;

import java.util.List;

import static java.util.Arrays.asList;

public enum ReportsCategory {
    FPS("Family Planning Services", asList(ReportIndicator.IUD, ReportIndicator.CONDOM, ReportIndicator.CONDOM_QTY, ReportIndicator.OCP, ReportIndicator.OCP_SC, ReportIndicator.OCP_ST, ReportIndicator.OCP_C_OTHERS,
            ReportIndicator.MALE_STERILIZATION, ReportIndicator.FEMALE_STERILIZATION, ReportIndicator.FS_BPL, ReportIndicator.FS_APL)),

    ANC_SERVICES("ANC Services", asList(ReportIndicator.EARLY_ANC_REGISTRATIONS, ReportIndicator.LATE_ANC_REGISTRATIONS, ReportIndicator.TOTAL_ANC_REGISTRATIONS, ReportIndicator.SUB_TT, ReportIndicator.TT1, ReportIndicator.TT2, ReportIndicator.TTB, ReportIndicator.ANC4)),

    PREGNANCY_OUTCOMES("Pregnancy Outcomes", asList(ReportIndicator.LIVE_BIRTH, ReportIndicator.STILL_BIRTH, ReportIndicator.EARLY_ABORTIONS, ReportIndicator.LATE_ABORTIONS,
            ReportIndicator.SPONTANEOUS_ABORTION, ReportIndicator.DELIVERY, ReportIndicator.INSTITUTIONAL_DELIVERY, ReportIndicator.D_HOM, ReportIndicator.D_SC, ReportIndicator.D_PHC, ReportIndicator.D_CHC, ReportIndicator.D_SDH, ReportIndicator.D_DH, ReportIndicator.D_PRI,
            ReportIndicator.CESAREAN, ReportIndicator.CESAREAN_GOV, ReportIndicator.CESAREAN_PRI)),

    PNC_SERVICES("PNC Services", asList(ReportIndicator.PNC3)),

    CHILD_SERVICES("Child Services", asList(ReportIndicator.INFANT_BALANCE_ON_HAND, ReportIndicator.INFANT_REG, ReportIndicator.INFANT_BALANCE_TOTAL, ReportIndicator.INFANT_LEFT,
            ReportIndicator.INFANT_MORTALITY, ReportIndicator.INFANT_BALANCE_BALANCE, ReportIndicator.INFANT_BALANCE_OA_CHILDREN, ReportIndicator.INFANT_BALANCE_LESS_THAN_ONE_YEAR, ReportIndicator.INFANT_BALANCE_LESS_THAN_FIVE_YEAR,
            ReportIndicator.PENTAVALENT3_OR_OPV3, ReportIndicator.DPT_BOOSTER_OR_OPV_BOOSTER, ReportIndicator.DPT_BOOSTER2, ReportIndicator.HEP, ReportIndicator.OPV, ReportIndicator.MEASLES,
            ReportIndicator.PENTAVALENT_1, ReportIndicator.PENTAVALENT_2, ReportIndicator.PENTAVALENT_3,
            ReportIndicator.BCG, ReportIndicator.LBW, ReportIndicator.BF_POST_BIRTH, ReportIndicator.WEIGHED_AT_BIRTH,
            ReportIndicator.VIT_A_1, ReportIndicator.VIT_A_1_FOR_FEMALE_CHILD, ReportIndicator.VIT_A_1_FOR_MALE_CHILD,
            ReportIndicator.VIT_A_2, ReportIndicator.VIT_A_2_FOR_FEMALE_CHILD, ReportIndicator.VIT_A_2_FOR_MALE_CHILD,
            ReportIndicator.VIT_A_5_FOR_FEMALE_CHILD, ReportIndicator.VIT_A_5_FOR_MALE_CHILD, ReportIndicator.VIT_A_9_FOR_FEMALE_CHILD, ReportIndicator.VIT_A_9_FOR_MALE_CHILD,
            ReportIndicator.VIT_A_FOR_FEMALE, ReportIndicator.VIT_A_FOR_MALE,
            ReportIndicator.CHILD_DIARRHEA,
            ReportIndicator.CHILD_MORTALITY_DUE_TO_DIARRHEA,
            ReportIndicator.IB_1Y, ReportIndicator.OPV3, ReportIndicator.OPV_BOOSTER, ReportIndicator.JE, ReportIndicator.DPT_BOOSTER1)),

    MORTALITY("Mortality", asList(ReportIndicator.ENM, ReportIndicator.NM, ReportIndicator.LNM, ReportIndicator.INFANT_MORTALITY, ReportIndicator.CHILD_MORTALITY, ReportIndicator.MMA, ReportIndicator.MMD, ReportIndicator.MMP, ReportIndicator.MM)),

    BENEFICIARY_SCHEMES("Beneficiary Schemes", asList(ReportIndicator.ANCS_AND_PNCS_WITH_BPL));

    private String description;
    private List<ReportIndicator> indicators;

    ReportsCategory(String description, List<ReportIndicator> indicators) {
        this.description = description;
        this.indicators = indicators;
    }

    public List<ReportIndicator> indicators() {
        return indicators;
    }

    public String description() {
        return description;
    }

    public String value() {
        return name();
    }
}
