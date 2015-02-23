package org.ei.opensrp.sync;

import org.ei.opensrp.domain.FetchStatus;
import org.ei.opensrp.event.Event;

public class SyncAfterFetchListener implements AfterFetchListener {
    public void afterFetch(FetchStatus status) {
        Event.ON_DATA_FETCHED.notifyListeners(status);
    }
}
