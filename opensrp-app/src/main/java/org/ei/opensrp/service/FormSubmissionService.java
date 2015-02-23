package org.ei.opensrp.service;

import com.google.gson.Gson;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.ei.opensrp.domain.form.FormSubmission;
import org.ei.opensrp.repository.AllSettings;
import org.ei.opensrp.repository.FormDataRepository;
import org.ei.opensrp.AllConstants;
import org.ei.opensrp.domain.SyncStatus;
import org.ei.opensrp.util.EasyMap;
import org.ei.opensrp.util.Log;

import java.util.List;

import static java.text.MessageFormat.format;

public class FormSubmissionService {
    private ZiggyService ziggyService;
    private FormDataRepository formDataRepository;
    private AllSettings allSettings;

    public FormSubmissionService(ZiggyService ziggyService, FormDataRepository formDataRepository, AllSettings allSettings) {
        this.ziggyService = ziggyService;
        this.formDataRepository = formDataRepository;
        this.allSettings = allSettings;
    }

    public void processSubmissions(List<FormSubmission> formSubmissions) {
        for (FormSubmission submission : formSubmissions) {
            if (!formDataRepository.submissionExists(submission.instanceId())) {
                try {
                    ziggyService.saveForm(getParams(submission), submission.instance());
                } catch (Exception e) {
                    Log.logError(format("Form submission processing failed, with instanceId: {0}. Exception: {1}, StackTrace: {2}",
                            submission.instanceId(), e.getMessage(), ExceptionUtils.getStackTrace(e)));
                }
            }
            formDataRepository.updateServerVersion(submission.instanceId(), submission.serverVersion());
            allSettings.savePreviousFormSyncIndex(submission.serverVersion());
        }
    }

    private String getParams(FormSubmission submission) {
        return new Gson().toJson(
                EasyMap.create(AllConstants.INSTANCE_ID_PARAM, submission.instanceId())
                        .put(AllConstants.ENTITY_ID_PARAM, submission.entityId())
                        .put(AllConstants.FORM_NAME_PARAM, submission.formName())
                        .put(AllConstants.VERSION_PARAM, submission.version())
                        .put(AllConstants.SYNC_STATUS, SyncStatus.SYNCED.value())
                        .map());
    }
}
