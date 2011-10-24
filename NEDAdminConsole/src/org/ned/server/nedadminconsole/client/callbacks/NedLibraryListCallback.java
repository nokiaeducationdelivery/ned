package org.ned.server.nedadminconsole.client.callbacks;

import java.util.List;

import org.ned.server.nedadminconsole.client.NedRes;
import org.ned.server.nedadminconsole.client.dialogs.NedAlert;
import org.ned.server.nedadminconsole.client.widgets.NedLibrarySelectorWidget;
import org.ned.server.nedadminconsole.shared.NedObject;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class NedLibraryListCallback implements AsyncCallback<List<NedObject>> {

    private NedLibrarySelectorWidget dialog;
    

    public NedLibraryListCallback(NedLibrarySelectorWidget dialog) {
         this.dialog = dialog;
    }

    @Override
    public void onFailure(Throwable caught) {
        NedAlert.showAlert(NedRes.instance().msgErrorConnectingToDB());
    }

    @Override
    public void onSuccess(List<NedObject> result) {
        dialog.loadLibraryList(result);
    }

}