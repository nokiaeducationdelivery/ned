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
package org.ned.server.nedadminconsole.client;

import org.ned.server.nedadminconsole.client.interfaces.NedLibrarySelector;
import org.ned.server.nedadminconsole.client.interfaces.NedModelListener;
import org.ned.server.nedadminconsole.shared.NedObject;

import com.google.gwt.user.client.ui.TreeItem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class NedDataModel implements NedLibrarySelector {
    private NedObject currentLibrary = null;
    private NedObject currentObject = null;

    private NedObject nedLibrary;

    private List<NedModelListener> listeners = new LinkedList<NedModelListener>();
    private TreeItem currentTreeItem;

    @Override
    public void selectLibrary(NedObject library) {
        if (currentLibrary != library) {
            currentLibrary = library;
            currentObject = library;
            broadcastLibraryChanged();
        }
    }
    
    public NedObject findPreviousObject(NedObject item){   	
    	//find parent
    	NedObject parent = findObjectById(item.parentId);

    	if(parent == null){
    		return null;
    	}
    	
    	//iterate parent
    	for( NedObject obj : parent.childes ){
    		if(obj.index == ( item.index - 1 ) ){
    			return obj;
    		}
    	}

    	return null;
    }
    
    public NedObject findNextObject(NedObject item){
    	//find parent
    	NedObject parent = findObjectById(item.parentId);

    	if(parent == null){
    		return null;
    	}
    	
    	//iterate parent
    	for( NedObject obj : parent.childes ){
    		if(obj.index == ( item.index + 1 ) ){
    			return obj;
    		}
    	}

    	return null;    	
    }
    
    public NedObject findObjectById(String objId){
    	if( nedLibrary.id.equals( objId ) ){
    		return nedLibrary;
    	}else{
    		return findChildById(nedLibrary, objId);
    	}
    }
    
    public NedObject findChildById( NedObject current, String objId ){

    	NedObject searchedObj = null;
    	for( NedObject nedObj : current.childes ){
    		if( nedObj.id.equals( objId ) ){
    			return nedObj;
    		}else{
    			searchedObj = findChildById(nedObj, objId);
    			if( searchedObj != null ){
    				return searchedObj;
    			}
    		}
    	}
    	return searchedObj;
    }

    public NedObject getCurrentLibrary() {
        return currentLibrary;
    }

    public void addListener(NedModelListener newListener) {
        if (!listeners.contains(newListener)) {
            listeners.add(newListener);
        }
    }

    public void removeListener(NedModelListener listener) {
        int index = listeners.indexOf(listener);
        if (index >= 0) {
            listeners.remove(listener);
        }
    }

    private void broadcastLibraryChanged() {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).libraryChanged(this);
        }
    }

    public void broadcastFileUploaded() {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).fileUploaded(this);
        }
    }

    private void broadcastLibraryLoad() {
        for (NedModelListener listener : listeners) {
            listener.libraryContentLoaded(this);
        }
    }

    private void broadcastObjectSelection() {
        for (NedModelListener listener : listeners) {
            listener.objectSelection(this);
        }
    }

    public void broadcastObjectUpdate() {
        for (NedModelListener listener : listeners) {
            listener.objectChanged(this);
        }
    }
    
    private void boradcastObjectMoved(boolean moveUp){
    	for( NedModelListener listener : listeners ){
    		listener.objectMoved( this, moveUp );//TODO remove second argument
    	}
    }

    public NedObject getNedLibrary() {
    	return nedLibrary;
    }

    public NedObject getCurrentObject() {
        return currentObject;
    }

    public TreeItem getCurrentTreeItem() {
        return currentTreeItem;
    }

    public void clearSelections() {
        currentObject = null;
        currentTreeItem = null;
    }

    public void libraryLoad(NedObject libraryContent) {
        this.nedLibrary = libraryContent;
        broadcastLibraryLoad();
    }

    public void treeObjectSelection(NedObject obj, TreeItem source) {
        currentObject = obj;
        currentTreeItem = source;
        broadcastObjectSelection();
    }

    public void deleteCurrentItem() {
        broadcastItemDeleted();
        clearSelections();

    }

    private void broadcastItemDeleted() {
        for (NedModelListener listener : listeners) {
            listener.objectDeleted(this, currentObject.type);
        }
    }
    
    public void objectMoved(boolean moveUp){
    	boradcastObjectMoved(moveUp);
    }
}
