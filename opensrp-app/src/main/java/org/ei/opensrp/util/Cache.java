package org.ei.opensrp.util;

import org.ei.opensrp.domain.FetchStatus;
import org.ei.opensrp.event.CapturedPhotoInformation;
import org.ei.opensrp.event.Listener;
import org.ei.opensrp.event.Event;

import java.util.HashMap;
import java.util.Map;

import static java.text.MessageFormat.format;
import static org.ei.opensrp.domain.FetchStatus.fetched;

public class Cache<T> {
    private Map<String, T> value = new HashMap<String, T>();
    private final Listener<String> formSubmittedListener;
    private final Listener<FetchStatus> actionsFetchedListener;
    private final Listener<CapturedPhotoInformation> photoCapturedListener;
    private final Listener<String> actionHandledListener;

    public Cache() {
        actionsFetchedListener = new Listener<FetchStatus>() {
            @Override
            public void onEvent(FetchStatus data) {
                if (fetched.equals(data)) {
                    Log.logWarn("List cache invalidated as new data was fetched from server.");
                    value.clear();
                }
            }
        };
        formSubmittedListener = new Listener<String>() {
            @Override
            public void onEvent(String reason) {
                Log.logWarn(format("List cache invalidated: {0}.", reason));
                value.clear();
            }
        };
        photoCapturedListener = new Listener<CapturedPhotoInformation>() {
            @Override
            public void onEvent(CapturedPhotoInformation data) {
                value.clear();
            }
        };
        actionHandledListener = new Listener<String>() {
            @Override
            public void onEvent(String data) {
                Log.logWarn(format("List cache invalidated as Action handled: {0}", data));
                value.clear();
            }
        };
        Event.ON_DATA_FETCHED.addListener(actionsFetchedListener);
        Event.FORM_SUBMITTED.addListener(formSubmittedListener);
        Event.ON_PHOTO_CAPTURED.addListener(photoCapturedListener);
        Event.ACTION_HANDLED.addListener(actionHandledListener);
    }

    public T get(String key, CacheableData<T> cacheableData) {
        if (value.get(key) != null) {
            return value.get(key);
        }
        T fetchedData = cacheableData.fetch();
        value.put(key, fetchedData);
        return fetchedData;
    }
}

