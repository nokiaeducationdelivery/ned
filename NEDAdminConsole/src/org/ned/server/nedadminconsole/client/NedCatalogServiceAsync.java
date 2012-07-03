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
package org.ned.server.nedadminconsole.client;

import java.util.List;

import org.ned.server.nedadminconsole.shared.NedLanguage;
import org.ned.server.nedadminconsole.shared.NedObject;
import org.ned.server.nedadminconsole.shared.NedUser;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface NedCatalogServiceAsync {

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

    void getLanguageList(AsyncCallback<List<NedLanguage>> callback);

}
