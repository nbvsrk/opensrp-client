package org.ei.opensrp.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import org.acra.ACRA;
import org.ei.opensrp.sync.SyncAfterFetchListener;
import org.ei.opensrp.sync.SyncProgressIndicator;
import org.ei.opensrp.sync.UpdateActionsTask;
import org.ei.opensrp.view.InternationalizationContext;
import org.ei.opensrp.view.controller.UpdateController;
import org.ei.opensrp.util.Log;

import static android.webkit.ConsoleMessage.MessageLevel.ERROR;
import static java.text.MessageFormat.format;

public abstract class SecuredWebActivity extends SecuredActivity {
    protected WebView webView;
    protected UpdateController updateController;
    protected boolean shouldDismissProgressBarOnProgressComplete = true;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreation() {
        setActivityLayout();

        progressDialogInitialization();
        webViewInitialization(this);

        updateController = new UpdateController(webView);

        onInitialization();
    }

    protected void setActivityLayout() {
        setContentView(org.ei.opensrp.R.layout.html);
    }

    protected abstract void onInitialization();

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
        UpdateActionsTask updateActionsTask = new UpdateActionsTask(this, context.actionService(), context.formSubmissionSyncService(), new SyncProgressIndicator());
        updateActionsTask.updateFromServer(new SyncAfterFetchListener());
    }

    protected void closeDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    protected void onResumption() {
    }

    //    Added to fix the memory leak caused due to bug in android which stops activities with webview to be GCed.
    //    Refer to this link for more details - http://code.google.com/p/android/issues/detail?id=9375
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (webView != null) {
            webView.destroy();
            webView = null;
        }

        if (updateController != null)
            updateController.destroy();
    }

    private void progressDialogInitialization() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading ...");
        progressDialog.setMessage("Please wait");
        progressDialog.show();
    }

    private void webViewInitialization(final Activity activity) {
        webView = (WebView) findViewById(org.ei.opensrp.R.id.webview);

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                dismissProgressBarOnProgressComplete(progress, activity);
            }
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                String message = format("Javascript Log. Message: {0}, lineNumber: {1}, sourceId, {2}", consoleMessage.message(),
                        consoleMessage.lineNumber(), consoleMessage.sourceId());

                if (consoleMessage.messageLevel() == ERROR) {
                    Log.logError(message);
                    reportException(message);
                } else {
                    Log.logDebug(message);
                }
                return true;
            }
        });

        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.addJavascriptInterface(formController, "formContext");
        webView.addJavascriptInterface(navigationController, "navigationContext");
        webView.addJavascriptInterface(new InternationalizationContext(getResources()), "internationalizationContext");
    }

    private void dismissProgressBarOnProgressComplete(int progress, Activity activity) {
        if(shouldDismissProgressBarOnProgressComplete){
            activity.setProgress(progress * 1000);

            if (progress == 100 && progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }

    protected void reportException(String message) {
        ACRA.getErrorReporter().handleSilentException(new RuntimeException(message));
    }
}
