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

import org.ned.server.nedadminconsole.client.widgets.NedMotdWidget;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class NedSelectMotdCallback implements AsyncCallback<String>{

    private NedMotdWidget widgetMotd;

    public NedSelectMotdCallback(NedMotdWidget widgetMotd) {
        this.widgetMotd = widgetMotd;
    }

    @Override
    public void onFailure(Throwable caught) {
    }
    
    @Override
    public void onSuccess(String result) {
        widgetMotd.loadOldMessage(result);
        
    }

}
