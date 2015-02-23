package org.ei.opensrp.view.activity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import org.ei.opensrp.Context;
import org.ei.opensrp.event.Listener;
import org.ei.opensrp.service.PendingFormSubmissionService;
import org.ei.opensrp.sync.SyncAfterFetchListener;
import org.ei.opensrp.sync.SyncProgressIndicator;
import org.ei.opensrp.sync.UpdateActionsTask;
import org.ei.opensrp.view.contract.HomeContext;
import org.ei.opensrp.view.controller.NativeAfterANMDetailsFetchListener;
import org.ei.opensrp.view.controller.NativeUpdateANMDetailsTask;
import org.ei.opensrp.event.Event;

import static java.lang.String.valueOf;

public class NativeHomeActivity extends SecuredActivity {
    private MenuItem updateMenuItem;
    private MenuItem remainingFormsToSyncMenuItem;
    private PendingFormSubmissionService pendingFormSubmissionService;

    private Listener<Boolean> onSyncStartListener = new Listener<Boolean>() {
        @Override
        public void onEvent(Boolean data) {
            if (updateMenuItem != null) {
                updateMenuItem.setActionView(org.ei.opensrp.R.layout.progress);
            }
        }
    };

    private Listener<Boolean> onSyncCompleteListener = new Listener<Boolean>() {
        @Override
        public void onEvent(Boolean data) {
            //#TODO: RemainingFormsToSyncCount cannot be updated from a back ground thread!!
            updateRemainingFormsToSyncCount();
            if (updateMenuItem != null) {
                updateMenuItem.setActionView(null);
            }
            updateRegisterCounts();
        }
    };

    private Listener<String> onFormSubmittedListener = new Listener<String>() {
        @Override
        public void onEvent(String instanceId) {
            updateRegisterCounts();
        }
    };

    private Listener<String> updateANMDetailsListener = new Listener<String>() {
        @Override
        public void onEvent(String data) {
            updateRegisterCounts();
        }
    };

    private TextView ecRegisterClientCountView;
    private TextView ancRegisterClientCountView;
    private TextView pncRegisterClientCountView;
    private TextView fpRegisterClientCountView;
    private TextView childRegisterClientCountView;

    @Override
    protected void onCreation() {
        setContentView(org.ei.opensrp.R.layout.smart_registers_home);
        setupViews();
        initialize();
    }

    private void setupViews() {
        findViewById(org.ei.opensrp.R.id.btn_ec_register).setOnClickListener(onRegisterStartListener);
        findViewById(org.ei.opensrp.R.id.btn_pnc_register).setOnClickListener(onRegisterStartListener);
        findViewById(org.ei.opensrp.R.id.btn_anc_register).setOnClickListener(onRegisterStartListener);
        findViewById(org.ei.opensrp.R.id.btn_fp_register).setOnClickListener(onRegisterStartListener);
        findViewById(org.ei.opensrp.R.id.btn_child_register).setOnClickListener(onRegisterStartListener);

        findViewById(org.ei.opensrp.R.id.btn_reporting).setOnClickListener(onButtonsClickListener);
        findViewById(org.ei.opensrp.R.id.btn_videos).setOnClickListener(onButtonsClickListener);

        ecRegisterClientCountView = (TextView) findViewById(org.ei.opensrp.R.id.txt_ec_register_client_count);
        pncRegisterClientCountView = (TextView) findViewById(org.ei.opensrp.R.id.txt_pnc_register_client_count);
        ancRegisterClientCountView = (TextView) findViewById(org.ei.opensrp.R.id.txt_anc_register_client_count);
        fpRegisterClientCountView = (TextView) findViewById(org.ei.opensrp.R.id.txt_fp_register_client_count);
        childRegisterClientCountView = (TextView) findViewById(org.ei.opensrp.R.id.txt_child_register_client_count);
    }

    private void initialize() {
        pendingFormSubmissionService = context.pendingFormSubmissionService();
        Event.SYNC_STARTED.addListener(onSyncStartListener);
        Event.SYNC_COMPLETED.addListener(onSyncCompleteListener);
        Event.FORM_SUBMITTED.addListener(onFormSubmittedListener);
        Event.ACTION_HANDLED.addListener(updateANMDetailsListener);
    }

    @Override
    protected void onResumption() {
        updateRegisterCounts();
        updateSyncIndicator();
        updateRemainingFormsToSyncCount();
    }

    private void updateRegisterCounts() {
        NativeUpdateANMDetailsTask task = new NativeUpdateANMDetailsTask(Context.getInstance().anmController());
        task.fetch(new NativeAfterANMDetailsFetchListener() {
            @Override
            public void afterFetch(HomeContext anmDetails) {
                updateRegisterCounts(anmDetails);
            }
        });
    }

    private void updateRegisterCounts(HomeContext homeContext) {
        ecRegisterClientCountView.setText(valueOf(homeContext.eligibleCoupleCount()));
        ancRegisterClientCountView.setText(valueOf(homeContext.ancCount()));
        pncRegisterClientCountView.setText(valueOf(homeContext.pncCount()));
        fpRegisterClientCountView.setText(valueOf(homeContext.fpCount()));
        childRegisterClientCountView.setText(valueOf(homeContext.childCount()));
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case org.ei.opensrp.R.id.updateMenuItem:
                updateFromServer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateFromServer() {
        UpdateActionsTask updateActionsTask = new UpdateActionsTask(
                this, context.actionService(), context.formSubmissionSyncService(), new SyncProgressIndicator());
        updateActionsTask.updateFromServer(new SyncAfterFetchListener());
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
            remainingFormsToSyncMenuItem.setTitle(valueOf(size) + " " + getString(org.ei.opensrp.R.string.unsynced_forms_count_message));
            remainingFormsToSyncMenuItem.setVisible(true);
        } else {
            remainingFormsToSyncMenuItem.setVisible(false);
        }
    }

    private View.OnClickListener onRegisterStartListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case org.ei.opensrp.R.id.btn_ec_register:
                    navigationController.startECSmartRegistry();
                    break;

                case org.ei.opensrp.R.id.btn_anc_register:
                    navigationController.startANCSmartRegistry();
                    break;

                case org.ei.opensrp.R.id.btn_pnc_register:
                    navigationController.startPNCSmartRegistry();
                    break;

                case org.ei.opensrp.R.id.btn_child_register:
                    navigationController.startChildSmartRegistry();
                    break;

                case org.ei.opensrp.R.id.btn_fp_register:
                    navigationController.startFPSmartRegistry();
                    break;
            }
        }
    };

    private View.OnClickListener onButtonsClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case org.ei.opensrp.R.id.btn_reporting:
                    navigationController.startReports();
                    break;

                case org.ei.opensrp.R.id.btn_videos:
                    navigationController.startVideos();
                    break;
            }
        }
    };
}
