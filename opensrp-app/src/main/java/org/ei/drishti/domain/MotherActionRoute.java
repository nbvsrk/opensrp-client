package org.ei.drishti.domain;

import org.ei.drishti.Context;
import org.ei.drishti.dto.Action;
import org.ei.drishti.event.Event;

public enum MotherActionRoute {
    CLOSE("close") {
        @Override
        public void direct(Action action) {
            Context.getInstance().motherService().close(action.caseID(), action.get("reasonForClose"));
            Event.ACTION_HANDLED.notifyListeners("Mother closed");
        }
    };

    private String identifier;

    MotherActionRoute(String identifier) {
        this.identifier = identifier;
    }

    public String identifier() {
        return identifier;
    }

    public abstract void direct(Action action);
}