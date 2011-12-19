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
