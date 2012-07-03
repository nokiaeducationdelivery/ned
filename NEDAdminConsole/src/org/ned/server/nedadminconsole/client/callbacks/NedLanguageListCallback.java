/*******************************************************************************
* Copyright (c) 2012 Nokia Corporation
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* Comarch team - initial API and implementation
*******************************************************************************/
package org.ned.server.nedadminconsole.client.callbacks;

import com.google.gwt.user.client.rpc.AsyncCallback;

import org.ned.server.nedadminconsole.client.NedRes;
import org.ned.server.nedadminconsole.client.dialogs.NedAlert;
import org.ned.server.nedadminconsole.client.widgets.NedLanguageWidget;
import org.ned.server.nedadminconsole.shared.NedLanguage;
import java.util.List;

public class NedLanguageListCallback implements AsyncCallback<List<NedLanguage>> {

    private NedLanguageWidget languageWidget;
    
    public NedLanguageListCallback(NedLanguageWidget languageWidget)
    {
        this.languageWidget = languageWidget;
    }
    
    @Override
    public void onFailure(Throwable caught) {
        NedAlert.showAlert(NedRes.instance().msgErrorConnection());      
        
    }

    @Override
    public void onSuccess(List<NedLanguage> result) {
        languageWidget.loadLanguages(result);
        
    }

}
