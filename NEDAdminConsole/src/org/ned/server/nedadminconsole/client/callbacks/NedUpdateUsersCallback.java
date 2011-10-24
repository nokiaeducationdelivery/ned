package org.ned.server.nedadminconsole.client.callbacks;

import org.ned.server.nedadminconsole.client.NedRes;
import org.ned.server.nedadminconsole.client.dialogs.NedAlert;
import org.ned.server.nedadminconsole.client.interfaces.NedUserListUpdater;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class NedUpdateUsersCallback implements AsyncCallback<Boolean>{

    private NedUserListUpdater updater;
    
    public NedUpdateUsersCallback(
            NedUserListUpdater updater) {
            this.updater = updater;
    }

    @Override
    public void onFailure(Throwable caught) {
        displayFailure();
    }

    @Override
    public void onSuccess(Boolean result) {
        if(result.booleanValue())
        {
            NedAlert.showAlert(NedRes.instance().msgUsersUpdated());
            updater.refreshUserList();
        }
        else
        {
            displayFailure();
        }
    }
    
    private void displayFailure()
    {
        NedAlert.showAlert(NedRes.instance().msgErrorUpdatingUser());
    }
}
