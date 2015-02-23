package org.ei.opensrp.view.activity;

import android.view.Menu;
import android.view.MenuItem;

import org.ei.opensrp.event.Listener;
import org.ei.opensrp.service.PendingFormSubmissionService;
import org.ei.opensrp.view.controller.HomeController;
import org.ei.opensrp.event.Event;

public class HomeActivity extends SecuredWebActivity {
    private MenuItem updateMenuItem;
    private MenuItem remainingFormsToSyncMenuItem;
    private PendingFormSubmissionService pendingFormSubmissionService;

    private Listener<Boolean> onSyncStartListener = new Listener<Boolean>() {
        @Override
        public void onEvent(Boolean data) {
            updateMenuItem.setActionView(org.ei.opensrp.R.layout.progress);
        }
    };

    private Listener<Boolean> onSyncCompleteListener = new Listener<Boolean>() {
        @Override
        public void onEvent(Boolean data) {
            //#TODO: RemainingFormsToSyncCount cannot be updated from a back ground thread!!
            updateRemainingFormsToSyncCount();
            updateMenuItem.setActionView(null);
        }
    };

    private Listener<String> onFormSubmittedListener = new Listener<String>() {
        @Override
        public void onEvent(String instanceId) {
            updateController.updateANMDetails();
        }
    };

    private Listener<String> updateANMDetailsListener = new Listener<String>() {
        @Override
        public void onEvent(String data) {
            updateController.updateANMDetails();
        }
    };

    @Override
    protected void onInitialization() {
        pendingFormSubmissionService = context.pendingFormSubmissionService();
        Event.SYNC_STARTED.addListener(onSyncStartListener);
        Event.SYNC_COMPLETED.addListener(onSyncCompleteListener);
        Event.FORM_SUBMITTED.addListener(onFormSubmittedListener);
        Event.ACTION_HANDLED.addListener(updateANMDetailsListener);

        webView.loadUrl("file:///android_asset/www/home.html");
        webView.addJavascriptInterface(new HomeController(updateController), "context");
    }

    @Override
    protected void onResumption() {
        updateSyncIndicator();
        updateRemainingFormsToSyncCount();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        updateMenuItem = menu.findItem(org.ei.opensrp.R.id.updateMenuItem);
        remainingFormsToSyncMenuItem = menu.findItem(org.ei.opensrp.R.id.remainingFormsToSyncMenuItem);

        updateSyncIndicator();
        updateRemainingFormsToSyncCount();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Event.SYNC_STARTED.removeListener(onSyncStartListener);
        Event.SYNC_COMPLETED.removeListener(onSyncCompleteListener);
        Event.FORM_SUBMITTED.removeListener(onFormSubmittedListener);
        Event.ACTION_HANDLED.removeListener(updateANMDetailsListener);
    }

    private void updateSyncIndicator() {
        if (updateMenuItem != null) {
            if (context.allSharedPreferences().fetchIsSyncInProgress()) {
                updateMenuItem.setActionView(org.ei.opensrp.R.layout.progress);
            } else
                updateMenuItem.setActionView(null);
        }
    }

    private void updateRemainingFormsToSyncCount() {
        if (remainingFormsToSyncMenuItem == null) {
            return;
        }

        long size = pendingFormSubmissionService.pendingFormSubmissionCount();
        if (size > 0) {
            remainingFormsToSyncMenuItem.setTitle(String.valueOf(size) + " " + getString(org.ei.opensrp.R.string.unsynced_forms_count_message));
            remainingFormsToSyncMenuItem.setVisible(true);
        } else {
            remainingFormsToSyncMenuItem.setVisible(false);
        }
    }
}
