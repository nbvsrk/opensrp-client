package org.ei.opensrp.sync;

import org.ei.opensrp.Context;
import org.ei.opensrp.view.ProgressIndicator;
import org.ei.opensrp.event.Event;

public class SyncProgressIndicator implements ProgressIndicator {
    @Override
    public void setVisible() {
        Context.getInstance().allSharedPreferences().saveIsSyncInProgress(true);
        Event.SYNC_STARTED.notifyListeners(true);
    }

    @Override
    public void setInvisible() {
        Context.getInstance().allSharedPreferences().saveIsSyncInProgress(false);
        Event.SYNC_COMPLETED.notifyListeners(true);
    }
}
