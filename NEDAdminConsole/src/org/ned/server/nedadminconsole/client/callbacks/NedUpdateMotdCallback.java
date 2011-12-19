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

import org.ned.server.nedadminconsole.client.NedRes;
import org.ned.server.nedadminconsole.client.dialogs.NedAlert;
import org.ned.server.nedadminconsole.client.widgets.NedMotdWidget;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class NedUpdateMotdCallback implements AsyncCallback<Boolean> {
    
    NedMotdWidget dialog = null;
    
    public NedUpdateMotdCallback(NedMotdWidget dialog){
        this.dialog = dialog;
    }
    
    @Override
    public void onFailure(Throwable caught) {
        NedAlert.showAlert(NedRes.instance().msgCannotUpdateMotd());
    }

    @Override
    public void onSuccess(Boolean result) {
        if(result){
            NedAlert.showAlert(NedRes.instance().msgMotdUpdated());
            dialog.updateTextBoxes();
        }else{
            NedAlert.showAlert(NedRes.instance().msgCannotUpdateMotd());
        }
    }

}
