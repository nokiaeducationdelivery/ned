package org.ned.server.nedadminconsole.client.callbacks;

import org.ned.server.nedadminconsole.client.NedDataModel;
import org.ned.server.nedadminconsole.client.NedRes;
import org.ned.server.nedadminconsole.client.dialogs.NedAlert;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class NedUpdateItemCallback implements AsyncCallback<Boolean> {

    private NedDataModel model = null;

    public NedUpdateItemCallback(NedDataModel model) {
        this.model = model;
    }

    @Override
    public void onFailure(Throwable caught) {
        displayFailure();
    }

    @Override
    public void onSuccess(Boolean result) {
        if (result.booleanValue()) {
            NedAlert.showAlert(NedRes.instance().msgItemUpdated());
            model.broadcastObjectUpdate();
        } else {
            displayFailure();
        }
    }

    private void displayFailure() {
        NedAlert.showAlert(NedRes.instance().msgItemNotUpdated());
    }

}
