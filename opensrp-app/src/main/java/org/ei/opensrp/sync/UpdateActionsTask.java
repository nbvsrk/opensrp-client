package org.ei.opensrp.sync;

import android.content.Context;
import android.widget.Toast;
import org.ei.opensrp.domain.FetchStatus;
import org.ei.opensrp.service.ActionService;
import org.ei.opensrp.service.FormSubmissionSyncService;
import org.ei.opensrp.view.BackgroundAction;
import org.ei.opensrp.view.LockingBackgroundTask;
import org.ei.opensrp.view.ProgressIndicator;
import org.ei.opensrp.util.Log;

public class UpdateActionsTask {
    private final LockingBackgroundTask task;
    private ActionService actionService;
    private Context context;
    private FormSubmissionSyncService formSubmissionSyncService;

    public UpdateActionsTask(Context context, ActionService actionService, FormSubmissionSyncService formSubmissionSyncService, ProgressIndicator progressIndicator) {
        this.actionService = actionService;
        this.context = context;
        this.formSubmissionSyncService = formSubmissionSyncService;
        task = new LockingBackgroundTask(progressIndicator);
    }

    public void updateFromServer(final AfterFetchListener afterFetchListener) {
        if (org.ei.opensrp.Context.getInstance().IsUserLoggedOut()) {
            Log.logInfo("Not updating from server as user is not logged in.");
            return;
        }

        task.doActionInBackground(new BackgroundAction<FetchStatus>() {
            public FetchStatus actionToDoInBackgroundThread() {
                FetchStatus fetchStatusForForms = formSubmissionSyncService.sync();
                FetchStatus fetchStatusForActions = actionService.fetchNewActions();
                if(fetchStatusForActions == FetchStatus.fetched || fetchStatusForForms == FetchStatus.fetched)
                    return FetchStatus.fetched;
                return fetchStatusForForms;
            }

            public void postExecuteInUIThread(FetchStatus result) {
                if (result != null && context != null && result != FetchStatus.nothingFetched) {
                    Toast.makeText(context, result.displayValue(), Toast.LENGTH_SHORT).show();
                }
                afterFetchListener.afterFetch(result);
            }
        });
    }
}
