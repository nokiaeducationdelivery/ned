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

import java.util.List;

import org.ned.server.nedadminconsole.client.NedRes;
import org.ned.server.nedadminconsole.client.dialogs.NedAlert;
import org.ned.server.nedadminconsole.client.widgets.NedUserManagementWidget;
import org.ned.server.nedadminconsole.shared.NedUser;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class NedUserListCallback implements AsyncCallback<List<NedUser>>{

    private NedUserManagementWidget panelUsers;
    
    public NedUserListCallback(NedUserManagementWidget panelUsers)
    {
        this.panelUsers = panelUsers;
    }
    
    @Override
    public void onFailure(Throwable caught) {
        NedAlert.showAlert(NedRes.instance().msgErrorGettingUsers());        
    }

    @Override
    public void onSuccess(List<NedUser> result) {
        panelUsers.loadUsers(result);
        
    }

}
