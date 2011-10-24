package org.ned.server.nedadminconsole.client;

import org.ned.server.nedadminconsole.client.widgets.NedTreeItemWidget;
import org.ned.server.nedadminconsole.shared.NedObject;

import com.google.gwt.user.client.ui.TreeItem;

public class NedTreeItem extends TreeItem {
    private NedObject nedObject = null;
    private NedTreeItemWidget contentWidget = null;
    public NedTreeItem(NedObject nedItem) {

        this.nedObject = nedItem;
        fillItem();
    }

    public NedObject getNedObject() {
        return nedObject;
    }

    public void refresh(NedObject currentObject) {
        fillItem();
        
    }
    
    private void fillItem()
    {
        if (nedObject.type.equals("Audio")) {
            contentWidget = new NedTreeItemWidget("images/music_icon_32x32.png",
                    nedObject.name);
        } else if (nedObject.type.equals("Video")) {
            contentWidget = new NedTreeItemWidget("images/video_icon_32x32.png",
                    nedObject.name);
        } else if (nedObject.type.equals("Text")) {
            contentWidget = new NedTreeItemWidget("images/text_icon_32x32.png",
                    nedObject.name);
        } else if (nedObject.type.equals("Undefined")) {
            contentWidget = new NedTreeItemWidget("images/unknown_icon_32x32.png",
                    nedObject.name);
        } else if (nedObject.type.equals("Picture")) {
            contentWidget = new NedTreeItemWidget("images/picture_icon_32x32.png",
                    nedObject.name);
        }
        if(contentWidget != null)
        {
            setWidget(contentWidget);
        }
        else {
            setText(nedObject.name);
        }
    }
    
}
