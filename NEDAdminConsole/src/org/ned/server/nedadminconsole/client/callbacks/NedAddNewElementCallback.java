package org.ned.server.nedadminconsole.client.callbacks;

import org.ned.server.nedadminconsole.client.NedRes;
import org.ned.server.nedadminconsole.client.NedTreeItem;
import org.ned.server.nedadminconsole.client.dialogs.NedAlert;
import org.ned.server.nedadminconsole.client.interfaces.NedLibraryListUpdater;
import org.ned.server.nedadminconsole.shared.NedObject;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.TreeItem;

public class NedAddNewElementCallback implements AsyncCallback<Boolean> {

    private DialogBox dialog;
    private TreeItem parentUiNode;
    private NedObject newObject;
    private NedLibraryListUpdater libraryUpdater;

    public NedAddNewElementCallback(DialogBox dialog, NedObject newObject,
            TreeItem parentUiNode, NedLibraryListUpdater libraryUpdater) {
        this.dialog = dialog;
        this.parentUiNode = parentUiNode;
        this.newObject = newObject;
        this.libraryUpdater = libraryUpdater;
    }

    public NedAddNewElementCallback(DialogBox dialog, NedObject newObject,
            TreeItem parentUiNode) {
        this(dialog, newObject, parentUiNode, null);
    }

    @Override
    public void onFailure(Throwable caught) {
        failureAction();
    }

    @Override
    public void onSuccess(Boolean result) {
        if (result.booleanValue()) {
            NedAlert.showAlert(constructNewElementMessage());
            dialog.hide();
            if (parentUiNode != null) {
                refreshTree();
            }
            if(libraryUpdater != null)
            {
                libraryUpdater.addNewLibrary(newObject);
            }
        } else {
            failureAction();
        }
    }

    private String constructNewElementMessage() {
        StringBuilder builder = new StringBuilder();
        builder.append(NedRes.instance().msgPartNew());
        builder.append(" ");
        if(newObject.type.equals("Undefinded"))
        {
            builder.append(NedRes.instance().mainMediaItem());
        }else {
        builder.append(newObject.type.toLowerCase()); }
        builder.append(" ");
        builder.append(NedRes.instance().msgPartAdded());
        return builder.toString();
    }

    private void refreshTree() {
        NedTreeItem newItem = new NedTreeItem(newObject);
        parentUiNode.addItem(newItem);
        parentUiNode.setState(true);
        newItem.getTree().setSelectedItem(newItem, true);

    }

    public void failureAction() {
        NedAlert.showAlert(NedRes.instance().msgErrorAddingItem());
    }

}
