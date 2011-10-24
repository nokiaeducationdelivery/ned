package org.ned.server.nedadminconsole.client.callbacks;

import java.util.List;

import org.ned.server.nedadminconsole.client.NedDataModel;
import org.ned.server.nedadminconsole.client.NedRes;
import org.ned.server.nedadminconsole.client.dialogs.NedAlert;
import org.ned.server.nedadminconsole.shared.NedObject;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class NedFullListCallback implements AsyncCallback<List<NedObject>> {

	private NedDataModel model;
	
	public NedFullListCallback(NedDataModel model)
	{
		this.model = model;
	}
	
	@Override
	public void onFailure(Throwable caught) {
		NedAlert.showAlert(NedRes.instance().msgErrorConnection());
	}

	
	@Override
	public void onSuccess(List<NedObject> result) {
		model.libraryLoad(result);
	}
	


}
