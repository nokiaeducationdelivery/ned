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
