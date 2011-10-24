package org.ned.server.nedadminconsole.client.widgets;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class NedTreeItemWidget extends Composite {

    public NedTreeItemWidget(String imageUrl, String labelText) {
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        initWidget(horizontalPanel);
        horizontalPanel.setSize("90%", "100%");
        
        Image image = new Image(imageUrl);
        horizontalPanel.add(image);
        image.setSize("32", "32");
        
        Label labelSpacer = new Label("");
        horizontalPanel.add(labelSpacer);
        horizontalPanel.setCellWidth(labelSpacer, "10%");
        labelSpacer.setWidth("10");
        
        Label labelName = new Label(labelText);
        labelName.setWordWrap(false);
        horizontalPanel.add(labelName);
        horizontalPanel.setCellWidth(labelName, "90%");
        addStyleName("NedTreeItemWidget");
    }

}
