package org.ned.server.nedadminconsole.client;

import java.util.List;

import org.ned.server.nedadminconsole.shared.NedObject;
import org.ned.server.nedadminconsole.shared.NedUser;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface NedCatalogServiceAsync {

	void getLibraryInfo(String id, AsyncCallback<NedObject> callback);

	void getLibraryList(AsyncCallback<List<NedObject>> callback);
	
	void getFullNode(String id, AsyncCallback<List<NedObject>> callback);

	void updateItem(NedObject item, AsyncCallback<Boolean> callback);

	void checkIdAvailable(String id, AsyncCallback<Boolean> callback);

	void addNewItem(NedObject newItem, AsyncCallback<Boolean> callback);

	void deleteItem(String id, AsyncCallback<Boolean> callback);
	
	void updateMotd(String message, AsyncCallback<Boolean> callback);

    void getMotd(AsyncCallback<String> callback);

    void getUserList(AsyncCallback<List<NedUser>> callback);

    void updateUsers(List<NedUser> users, AsyncCallback<Boolean> callback);

}
