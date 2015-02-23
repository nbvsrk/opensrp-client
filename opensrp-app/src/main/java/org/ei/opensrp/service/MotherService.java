package org.ei.opensrp.service;

import org.ei.opensrp.AllConstants;
import org.ei.opensrp.domain.Mother;
import org.ei.opensrp.domain.ServiceProvided;
import org.ei.opensrp.domain.form.FormSubmission;
import org.ei.opensrp.repository.AllBeneficiaries;
import org.ei.opensrp.repository.AllEligibleCouples;
import org.ei.opensrp.repository.AllTimelineEvents;
import org.ei.opensrp.util.EasyMap;
import org.ei.opensrp.util.Log;
import static org.ei.opensrp.domain.TimelineEvent.*;

import static org.ei.opensrp.util.IntegerUtil.tryParse;

public class MotherService {
    public static final String MOTHER_ID = "motherId";
    private AllBeneficiaries allBeneficiaries;
    private AllTimelineEvents allTimelines;
    private AllEligibleCouples allEligibleCouples;
    private ServiceProvidedService serviceProvidedService;
    public static String submissionDate = "submissionDate";


    public MotherService(AllBeneficiaries allBeneficiaries, AllEligibleCouples allEligibleCouples,
                         AllTimelineEvents allTimelineEvents, ServiceProvidedService serviceProvidedService) {
        this.allBeneficiaries = allBeneficiaries;
        this.allTimelines = allTimelineEvents;
        this.allEligibleCouples = allEligibleCouples;
        this.serviceProvidedService = serviceProvidedService;
    }

    public void registerANC(FormSubmission submission) {
        addTimelineEventsForMotherRegistration(submission);
    }

    public void registerOutOfAreaANC(FormSubmission submission) {
        addTimelineEventsForMotherRegistration(submission);
    }

    public void ancVisit(FormSubmission submission) {
        allTimelines.add(
                forANCCareProvided(
                        submission.entityId(),
                        submission.getFieldValue(AllConstants.ANCVisitFields.ANC_VISIT_NUMBER),
                        submission.getFieldValue(AllConstants.ANCVisitFields.ANC_VISIT_DATE),
                        EasyMap.create(AllConstants.ANCVisitFields.BP_SYSTOLIC, submission.getFieldValue(AllConstants.ANCVisitFields.BP_SYSTOLIC))
                                .put(AllConstants.ANCVisitFields.BP_DIASTOLIC, submission.getFieldValue(AllConstants.ANCVisitFields.BP_DIASTOLIC))
                                .put(AllConstants.ANCVisitFields.TEMPERATURE, submission.getFieldValue(AllConstants.ANCVisitFields.TEMPERATURE))
                                .put(AllConstants.ANCVisitFields.WEIGHT, submission.getFieldValue(AllConstants.ANCVisitFields.WEIGHT))
                                .map()));
        serviceProvidedService.add(
                ServiceProvided.forANCCareProvided(
                        submission.entityId(),
                        submission.getFieldValue(AllConstants.ANCVisitFields.ANC_VISIT_NUMBER),
                        submission.getFieldValue(AllConstants.ANCVisitFields.ANC_VISIT_DATE),
                        submission.getFieldValue(AllConstants.ANCVisitFields.BP_SYSTOLIC),
                        submission.getFieldValue(AllConstants.ANCVisitFields.BP_DIASTOLIC),
                        submission.getFieldValue(AllConstants.ANCVisitFields.WEIGHT)));
    }

    public void close(FormSubmission submission) {
        close(submission.entityId(), submission.getFieldValue(AllConstants.ANCCloseFields.CLOSE_REASON_FIELD_NAME));
    }

    public void close(String entityId, String reason) {
        Mother mother = allBeneficiaries.findMotherWithOpenStatus(entityId);
        if (mother == null) {
            Log.logWarn("Tried to close non-existent mother. Entity ID: " + entityId);
            return;
        }

        allBeneficiaries.closeMother(entityId);
        if (AllConstants.ANCCloseFields.DEATH_OF_WOMAN_FIELD_VALUE.equalsIgnoreCase(reason)
                || AllConstants.PNCCloseFields.DEATH_OF_MOTHER_FIELD_VALUE.equalsIgnoreCase(reason)
                || AllConstants.ANCCloseFields.PERMANENT_RELOCATION_FIELD_VALUE.equalsIgnoreCase(reason)
                || AllConstants.PNCCloseFields.PERMANENT_RELOCATION_FIELD_VALUE.equalsIgnoreCase(reason)) {
            allEligibleCouples.close(mother.ecCaseId());
        }
    }

    public void ttProvided(FormSubmission submission) {
        allTimelines.add(forTTShotProvided(submission.entityId(), submission.getFieldValue(AllConstants.TTFields.TT_DOSE), submission.getFieldValue(AllConstants.TTFields.TT_DATE)));
        serviceProvidedService.add(ServiceProvided.forTTDose(submission.entityId(), submission.getFieldValue(AllConstants.TTFields.TT_DOSE), submission.getFieldValue(AllConstants.TTFields.TT_DATE)));
    }

    public void ifaTabletsGiven(FormSubmission submission) {
        String numberOfIFATabletsGiven = submission.getFieldValue(AllConstants.IFAFields.NUMBER_OF_IFA_TABLETS_GIVEN);
        if (tryParse(numberOfIFATabletsGiven, 0) > 0) {
            allTimelines.add(forIFATabletsGiven(submission.entityId(), numberOfIFATabletsGiven, submission.getFieldValue(AllConstants.IFAFields.IFA_TABLETS_DATE)));
            serviceProvidedService.add(ServiceProvided.forIFATabletsGiven(submission.entityId(), numberOfIFATabletsGiven, submission.getFieldValue(AllConstants.IFAFields.IFA_TABLETS_DATE)));
        }
    }

    private void addTimelineEventsForMotherRegistration(FormSubmission submission) {
        allTimelines.add(forStartOfPregnancy(submission.getFieldValue(MOTHER_ID), submission.getFieldValue(AllConstants.ANCRegistrationFields.REGISTRATION_DATE), submission.getFieldValue(AllConstants.ANCVisitFields.REFERENCE_DATE)));
        allTimelines.add(forStartOfPregnancyForEC(submission.entityId(), submission.getFieldValue(AllConstants.ANCVisitFields.THAYI_CARD_NUMBER), submission.getFieldValue(AllConstants.ANCRegistrationFields.REGISTRATION_DATE), submission.getFieldValue(AllConstants.ANCVisitFields.REFERENCE_DATE)));
    }

    public void hbTest(FormSubmission submission) {
        serviceProvidedService.add(
                ServiceProvided.forHBTest(submission.entityId(),
                        submission.getFieldValue(AllConstants.HbTestFields.HB_LEVEL),
                        submission.getFieldValue(AllConstants.HbTestFields.HB_TEST_DATE)));
    }

    public void deliveryOutcome(FormSubmission submission) {
        Mother mother = allBeneficiaries.findMotherWithOpenStatus(submission.entityId());
        if (mother == null) {
            Log.logWarn("Failed to handle delivery outcome for mother. Entity ID: " + submission.entityId());
            return;
        }
        if (AllConstants.BOOLEAN_FALSE.equals(submission.getFieldValue(AllConstants.DeliveryOutcomeFields.DID_WOMAN_SURVIVE)) || AllConstants.BOOLEAN_FALSE.equals(submission.getFieldValue(AllConstants.DeliveryOutcomeFields.DID_MOTHER_SURVIVE))) {
            allBeneficiaries.closeMother(submission.entityId());
            allEligibleCouples.close(mother.ecCaseId());
            return;
        }

        allBeneficiaries.switchMotherToPNC(submission.entityId());
    }

    public void pncVisitHappened(FormSubmission submission) {
        allTimelines.add(
                forMotherPNCVisit(
                        submission.entityId(),
                        submission.getFieldValue(AllConstants.PNCVisitFields.PNC_VISIT_DAY),
                        submission.getFieldValue(AllConstants.PNCVisitFields.PNC_VISIT_DATE),
                        submission.getFieldValue(AllConstants.PNCVisitFields.BP_SYSTOLIC),
                        submission.getFieldValue(AllConstants.PNCVisitFields.BP_DIASTOLIC),
                        submission.getFieldValue(AllConstants.PNCVisitFields.TEMPERATURE),
                        submission.getFieldValue(AllConstants.PNCVisitFields.HB_LEVEL)));
        serviceProvidedService.add(
                ServiceProvided.forMotherPNCVisit(
                        submission.entityId(),
                        submission.getFieldValue(AllConstants.PNCVisitFields.PNC_VISIT_DAY),
                        submission.getFieldValue(AllConstants.PNCVisitFields.PNC_VISIT_DATE)));

        String numberOfIFATabletsGiven = submission.getFieldValue(AllConstants.PNCVisitFields.NUMBER_OF_IFA_TABLETS_GIVEN);
        if (tryParse(numberOfIFATabletsGiven, 0) > 0) {
            allTimelines.add(forIFATabletsGiven(submission.entityId(), numberOfIFATabletsGiven, submission.getFieldValue(AllConstants.CommonFormFields.SUBMISSION_DATE)));
        }
    }

    public void deliveryPlan(FormSubmission submission) {
        allTimelines.add(
                forDeliveryPlan(
                        submission.entityId(),
                        submission.getFieldValue(AllConstants.DeliveryPlanFields.DELIVERY_FACILITY_NAME),
                        submission.getFieldValue(AllConstants.DeliveryPlanFields.TRANSPORTATION_PLAN),
                        submission.getFieldValue(AllConstants.DeliveryPlanFields.BIRTH_COMPANION),
                        submission.getFieldValue(AllConstants.DeliveryPlanFields.ASHA_PHONE_NUMBER),
                        submission.getFieldValue(AllConstants.DeliveryPlanFields.PHONE_NUMBER),
                        submission.getFieldValue(AllConstants.DeliveryPlanFields.REVIEWED_HRP_STATUS),
                        submission.getFieldValue(AllConstants.CommonFormFields.SUBMISSION_DATE)));

        serviceProvidedService.add(
                ServiceProvided.forDeliveryPlan(
                        submission.entityId(),
                        submission.getFieldValue(AllConstants.DeliveryPlanFields.DELIVERY_FACILITY_NAME),
                        submission.getFieldValue(AllConstants.DeliveryPlanFields.TRANSPORTATION_PLAN),
                        submission.getFieldValue(AllConstants.DeliveryPlanFields.BIRTH_COMPANION),
                        submission.getFieldValue(AllConstants.DeliveryPlanFields.ASHA_PHONE_NUMBER),
                        submission.getFieldValue(AllConstants.DeliveryPlanFields.PHONE_NUMBER),
                        submission.getFieldValue(AllConstants.DeliveryPlanFields.REVIEWED_HRP_STATUS),
                        submission.getFieldValue(AllConstants.CommonFormFields.SUBMISSION_DATE)
                ));
    }
}
