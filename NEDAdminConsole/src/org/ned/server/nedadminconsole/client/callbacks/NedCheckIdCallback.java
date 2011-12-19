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

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;

public class NedCheckIdCallback implements AsyncCallback<Boolean>{

	Label resultLabel = null;
	
	public NedCheckIdCallback(Label resultLabel) {
		this.resultLabel = resultLabel; 
	}

	@Override
	public void onFailure(Throwable caught) {
		NedAlert.showAlert(NedRes.instance().msgErrorConnectingToDB());
	}

	@Override
	public void onSuccess(Boolean result) {
		if(result.booleanValue())
		{
		    resultLabel.setText(NedRes.instance().msgIdIsFree());
		}
		else 
		{
			setLabelFailure();
		}
		
	}
	
	private void setLabelFailure()
	{
		resultLabel.setText(NedRes.instance().msgIdNotAvailable());
	}

}
