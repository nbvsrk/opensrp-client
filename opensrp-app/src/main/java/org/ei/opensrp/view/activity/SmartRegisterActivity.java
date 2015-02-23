package org.ei.opensrp.view.activity;

import org.ei.opensrp.event.CapturedPhotoInformation;
import org.ei.opensrp.event.Listener;
import org.ei.opensrp.event.Event;

public abstract class SmartRegisterActivity extends SecuredWebActivity {
    protected Listener<CapturedPhotoInformation> photoCaptureListener;

    @Override
    protected void onInitialization() {
        onSmartRegisterInitialization();

        photoCaptureListener = new Listener<CapturedPhotoInformation>() {
            @Override
            public void onEvent(CapturedPhotoInformation data) {
                if (webView != null) {
                    webView.loadUrl("javascript:pageView.reloadPhoto('" + data.entityId() + "', '" + data.photoPath() + "')");
                }
            }
        };
        Event.ON_PHOTO_CAPTURED.addListener(photoCaptureListener);
    }

    protected abstract void onSmartRegisterInitialization();

    @Override
    protected void onResumption() {
        webView.loadUrl("javascript:if(window.pageView) {window.pageView.reload();}");
    }
}
