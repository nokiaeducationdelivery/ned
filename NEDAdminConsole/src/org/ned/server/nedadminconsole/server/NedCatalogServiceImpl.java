/*******************************************************************************
* Copyright (c) 2011-2012 Nokia Corporation
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* Comarch team - initial API and implementation
*******************************************************************************/
package org.ned.server.nedadminconsole.server;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ned.server.nedadminconsole.client.NedCatalogService;
import org.ned.server.nedadminconsole.datasource.PostgresConnection;
import org.ned.server.nedadminconsole.shared.NedLanguage;
import org.ned.server.nedadminconsole.shared.NedObject;
import org.ned.server.nedadminconsole.shared.NedUser;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class NedCatalogServiceImpl extends RemoteServiceServlet implements
        NedCatalogService {

    /**
     * 
     */
    private static final long serialVersionUID = -2441261396189223536L;

    @Override
    public List<NedObject> getLibraryList() {
        List<NedObject> retval = null;
        PostgresConnection connection = new PostgresConnection();
        try {
            retval = connection.getLibraries();
        } catch (Exception ex) {
            Logger.getLogger(NedCatalogServiceImpl.class.getName()).log(
                    Level.SEVERE, ex.getMessage(), ex);
        } finally {
            connection.disconnect();
        }
        return retval;
    }

    @Override
    public NedObject getFullNode(String id) {
        NedObject retval = null;
        PostgresConnection connection = new PostgresConnection();
        try {
            retval = connection.GetFullNode(id);
        } catch (Exception ex) {
            Logger.getLogger(NedCatalogServiceImpl.class.getName()).log(
                    Level.SEVERE, ex.getMessage(), ex);
        }finally {
            connection.disconnect();
        }

        return retval;
    }

    @Override
    public boolean updateItem(NedObject item) {
        boolean retval = false;
        PostgresConnection connection = new PostgresConnection();
        try {
            connection.updateItem(item);
            retval = true;
        } catch (Exception ex) {
            Logger.getLogger(NedCatalogServiceImpl.class.getName()).log(
                    Level.SEVERE, ex.getMessage(), ex);
        } finally {
            connection.disconnect();
        }
        return retval;

    }

    @Override
    public boolean checkIdAvailable(String id) {
        boolean retval = true;
        PostgresConnection connection = new PostgresConnection();
        try {
            NedObject element = connection.getSingleElement(id);
            if (element != null) {
                retval = false;
            }
        } catch (Exception ex) {
            // ignore exception logging as it is excepted here
            retval = true;
        } finally {
            connection.disconnect();
        }
        return retval;
    }

    @Override
    public boolean addNewItem(NedObject newItem) {
        boolean retval = false;
        PostgresConnection connection = new PostgresConnection();
        try {
            connection.saveNewItem(newItem);
            retval = true;
        } catch (Exception ex) {
            Logger.getLogger(NedCatalogServiceImpl.class.getName()).log(
                    Level.SEVERE, ex.getMessage());
        } finally {
            connection.disconnect();
        }
        return retval;
    }

    @Override
    public boolean deleteItem(String id) {
        boolean retval = false;
        PostgresConnection connection = new PostgresConnection();
        try {
            connection.deleteItem(id);
            retval = true;
        } catch (Exception ex) {
            Logger.getLogger(NedCatalogServiceImpl.class.getName()).log(
                    Level.SEVERE, ex.getMessage(), ex);
        } finally {
            connection.disconnect();
        }
        return retval;
    }

    @Override
    public List<NedUser> getUserList() {
        List<NedUser> retval = null;
        PostgresConnection connection = new PostgresConnection();
        try {
            retval = connection.getUserList();
        } catch (Exception ex) {
            Logger.getLogger(NedCatalogServiceImpl.class.getName()).log(
                    Level.SEVERE, ex.getMessage(), ex);
        } finally {
            connection.disconnect();
        }
        return retval;
    }

    @Override
    public boolean updateUsers(List<NedUser> users) {
        boolean retval = false;
        PostgresConnection connection = new PostgresConnection();
        try {
            connection.connect();
            for (int i = 0; i < users.size(); i++) {
                NedUser entry = users.get(i);
                if ((entry.flags & NedUser.DELETE) != 0) {
                    connection.deleteUser(entry);
                } else if((entry.flags & NedUser.DATABASE_PRESENT) != 0)
                {
                    connection.updateUserPassword(entry);
                } else 
                {
                    connection.addUser(entry);
                }
                retval = true;
            }
        } catch (Exception ex) {
            Logger.getLogger(NedCatalogServiceImpl.class.getName()).log(
                    Level.SEVERE, ex.getMessage(), ex);
        } finally {
            connection.disconnect();
        }
        return retval;
    }

    @Override
    public boolean updateMotd(String message) {
        String currentMessage = null;
        PostgresConnection connection = new PostgresConnection();
        try {
            currentMessage = connection.getMotd();
        } catch (Exception ex) {
        }

        try {
            if (currentMessage == null) {
                connection.insertMotd(message);
            } else {
                connection.updateMotd(message);
            }
        } catch (Exception ex) {
            return false;
        } finally {
            connection.disconnect();
        }

        return true;
    }

    public String getMotd() {
        PostgresConnection connection = new PostgresConnection();
        String retVal = "";

        try {
            retVal = connection.getMotd();
        } catch (Exception ex) {
        } finally {
            connection.disconnect();
        }
        return retVal;
    }

    @Override
    public List<NedLanguage> getLanguageList() {
        List<NedLanguage> retval = null;
        PostgresConnection connection = new PostgresConnection();
        try {
            retval = connection.getLanguageList();
        } catch (Exception ex) {
            Logger.getLogger(NedCatalogServiceImpl.class.getName()).log(
                    Level.SEVERE, ex.getMessage(), ex);
        } finally {
            connection.disconnect();
        }
        return retval;
    }


	@Override
	public boolean updateItems(List<NedObject> itemList) {
		for( NedObject obj : itemList ){
			if( !updateItem(obj) ){
				return false;
			}
		}
		return true;
	}
}
