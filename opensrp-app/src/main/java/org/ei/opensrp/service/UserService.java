package org.ei.opensrp.service;

import org.ei.opensrp.DristhiConfiguration;
import org.ei.opensrp.domain.LoginResponse;
import org.ei.opensrp.repository.AllSettings;
import org.ei.opensrp.repository.AllSharedPreferences;
import org.ei.opensrp.repository.Repository;
import org.ei.opensrp.sync.SaveANMLocationTask;
import org.ei.opensrp.util.Session;
import org.ei.opensrp.event.Event;

import static org.ei.opensrp.AllConstants.*;

public class UserService {
    private final Repository repository;
    private final AllSettings allSettings;
    private final AllSharedPreferences allSharedPreferences;
    private HTTPAgent httpAgent;
    private Session session;
    private DristhiConfiguration configuration;
    private SaveANMLocationTask saveANMLocationTask;

    public UserService(Repository repository, AllSettings allSettings, AllSharedPreferences allSharedPreferences, HTTPAgent httpAgent, Session session,
                       DristhiConfiguration configuration, SaveANMLocationTask saveANMLocationTask) {
        this.repository = repository;
        this.allSettings = allSettings;
        this.allSharedPreferences = allSharedPreferences;
        this.httpAgent = httpAgent;
        this.session = session;
        this.configuration = configuration;
        this.saveANMLocationTask = saveANMLocationTask;
    }

    public boolean isValidLocalLogin(String userName, String password) {
        return allSharedPreferences.fetchRegisteredANM().equals(userName) && repository.canUseThisPassword(password);
    }

    public LoginResponse isValidRemoteLogin(String userName, String password) {
        String requestURL = configuration.dristhiBaseURL() + AUTHENTICATE_USER_URL_PATH + userName;
        return httpAgent.urlCanBeAccessWithGivenCredentials(requestURL, userName, password);
    }

    private void loginWith(String userName, String password) {
        setupContextForLogin(userName, password);
        allSettings.registerANM(userName, password);
    }

    public void localLogin(String userName, String password) {
        loginWith(userName, password);
    }

    public void remoteLogin(String userName, String password, String anmLocation) {
        loginWith(userName, password);
        saveANMLocationTask.save(anmLocation);
    }

    public boolean hasARegisteredUser() {
        return !allSharedPreferences.fetchRegisteredANM().equals("");
    }

    public void logout() {
        logoutSession();
        allSettings.registerANM("", "");
        allSettings.savePreviousFetchIndex("0");
        repository.deleteRepository();
    }

    public void logoutSession() {
        session().expire();
        Event.ON_LOGOUT.notifyListeners(true);
    }

    public boolean hasSessionExpired() {
        return session().hasExpired();
    }

    protected void setupContextForLogin(String userName, String password) {
        session().start(session().lengthInMilliseconds());
        session().setPassword(password);
    }

    protected Session session() {
        return session;
    }

    public String switchLanguagePreference() {
        String preferredLocale = allSharedPreferences.fetchLanguagePreference();
        if (ENGLISH_LOCALE.equals(preferredLocale)) {
            allSharedPreferences.saveLanguagePreference(KANNADA_LOCALE);
            return KANNADA_LANGUAGE;
        } else {
            allSharedPreferences.saveLanguagePreference(ENGLISH_LOCALE);
            return ENGLISH_LANGUAGE;
        }
    }
}
