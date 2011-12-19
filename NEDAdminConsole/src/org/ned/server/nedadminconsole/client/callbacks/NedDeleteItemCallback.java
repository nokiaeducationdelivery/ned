/*******************************************************************************
* Copyright (c) 2011 Nokia Corporation
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* Comarch team - initial API and implementation
*******************************************************************************/
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
