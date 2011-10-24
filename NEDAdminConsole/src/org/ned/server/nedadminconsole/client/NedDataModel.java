package org.ned.server.nedadminconsole.client;

import org.ned.server.nedadminconsole.client.interfaces.NedLibrarySelector;
import org.ned.server.nedadminconsole.client.interfaces.NedModelListener;
import org.ned.server.nedadminconsole.shared.NedObject;

import com.google.gwt.user.client.ui.TreeItem;

import java.util.LinkedList;
import java.util.List;

public class NedDataModel implements NedLibrarySelector {
    private NedObject currentLibrary = null;
    private NedObject currentObject = null;

    private List<NedObject> nedLibraryContent = null;

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

    public List<NedObject> getNedLibrary() {
        return nedLibraryContent;
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

    public void libraryLoad(List<NedObject> libraryContent) {
        this.nedLibraryContent = libraryContent;
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
}
