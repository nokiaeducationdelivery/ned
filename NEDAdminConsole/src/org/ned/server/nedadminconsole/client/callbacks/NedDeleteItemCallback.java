package org.ned.server.nedadminconsole.client.callbacks;

import org.ned.server.nedadminconsole.client.NedDataModel;
import org.ned.server.nedadminconsole.client.NedRes;
import org.ned.server.nedadminconsole.client.dialogs.NedAlert;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class NedDeleteItemCallback implements AsyncCallback<Boolean> {

    private NedDataModel model;

    public NedDeleteItemCallback(NedDataModel model) {
        this.model = model;
    }

    @Override
    public void onFailure(Throwable caught) {
        displayFailure();
    }

    @Override
    public void onSuccess(Boolean result) {
        if (result.booleanValue()) {
            model.deleteCurrentItem();
        } else {
            displayFailure();
        }
    }

    private void displayFailure() {
        NedAlert.showAlert(NedRes.instance().msgItemNotDeleted());
    }

}
