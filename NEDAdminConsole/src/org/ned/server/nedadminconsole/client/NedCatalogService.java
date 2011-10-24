package org.ned.server.nedadminconsole.client;

import java.util.List;

import org.ned.server.nedadminconsole.shared.NedObject;
import org.ned.server.nedadminconsole.shared.NedUser;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("NedCatalogServlet")
public interface NedCatalogService extends RemoteService {
	List<NedObject> getLibraryList();
	NedObject getLibraryInfo(String id);
	List<NedObject> getFullNode(String id);
	boolean updateItem(NedObject item);
	boolean checkIdAvailable(String id);
	boolean addNewItem(NedObject newItem);
	boolean deleteItem(String id);
    boolean updateMotd(String message);
    String getMotd();
	List<NedUser> getUserList();
	boolean updateUsers(List<NedUser> users);
}
