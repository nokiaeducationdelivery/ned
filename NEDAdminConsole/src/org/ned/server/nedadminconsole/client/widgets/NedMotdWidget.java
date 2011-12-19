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

import org.ned.server.nedadminconsole.client.NedCatalogService;
import org.ned.server.nedadminconsole.client.NedCatalogServiceAsync;
import org.ned.server.nedadminconsole.client.NedRes;
import org.ned.server.nedadminconsole.client.callbacks.NedSelectMotdCallback;
import org.ned.server.nedadminconsole.client.callbacks.NedUpdateMotdCallback;
import org.ned.server.nedadminconsole.client.dialogs.NedAlert;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

public class NedMotdWidget extends Composite {
    private TextArea textArea;
    private TextArea textAreaOldMessage;

    public NedMotdWidget() {

        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        verticalPanel.setSpacing(8);
        initWidget(verticalPanel);
        verticalPanel.setWidth("100%");
        
                Label lblOldMessage = new Label(NedRes.instance().motdCurrentMessage());
                lblOldMessage.setStyleName("gwt-Label-Ned");
                verticalPanel.add(lblOldMessage);
                verticalPanel.setCellWidth(lblOldMessage, "50%");
                verticalPanel.setCellVerticalAlignment(lblOldMessage, HasVerticalAlignment.ALIGN_MIDDLE);
                verticalPanel.setCellHorizontalAlignment(lblOldMessage, HasHorizontalAlignment.ALIGN_CENTER);
                lblOldMessage.setDirectionEstimator(false);
                lblOldMessage.setSize("50%", "16px");
        
                textAreaOldMessage = new TextArea();
                verticalPanel.add(textAreaOldMessage);
                verticalPanel.setCellVerticalAlignment(textAreaOldMessage, HasVerticalAlignment.ALIGN_MIDDLE);
                verticalPanel.setCellHorizontalAlignment(textAreaOldMessage, HasHorizontalAlignment.ALIGN_CENTER);
                verticalPanel.setCellWidth(textAreaOldMessage, "50%");
                textAreaOldMessage.setReadOnly(true);
                textAreaOldMessage.setSize("50%", "44px");

        Label lblNewLabel = new Label(NedRes.instance().motdAddNewMessage());
        lblNewLabel.setStyleName("gwt-Label-Ned");
        verticalPanel.add(lblNewLabel);
        verticalPanel.setCellVerticalAlignment(lblNewLabel, HasVerticalAlignment.ALIGN_MIDDLE);
        verticalPanel.setCellHorizontalAlignment(lblNewLabel, HasHorizontalAlignment.ALIGN_CENTER);
        verticalPanel.setCellWidth(lblNewLabel, "50%");
        lblNewLabel.setSize("50%", "16px");

        textArea = new TextArea();
        verticalPanel.add(textArea);
        verticalPanel.setCellWidth(textArea, "50%");
        verticalPanel.setCellVerticalAlignment(textArea, HasVerticalAlignment.ALIGN_MIDDLE);
        verticalPanel.setCellHorizontalAlignment(textArea, HasHorizontalAlignment.ALIGN_CENTER);
        textArea.setSize("50%", "44px");
        
                Button btnUpdate = new Button(NedRes.instance().update());
                verticalPanel.add(btnUpdate);
                verticalPanel.setCellVerticalAlignment(btnUpdate, HasVerticalAlignment.ALIGN_MIDDLE);
                verticalPanel.setCellHorizontalAlignment(btnUpdate, HasHorizontalAlignment.ALIGN_CENTER);
                verticalPanel.setCellWidth(btnUpdate, "50%");
                btnUpdate.setSize("50%", "32px");
        btnUpdate.addClickHandler(new UpdateClickHandler());

    }

    protected void onLoad() {
        NedCatalogServiceAsync service = (NedCatalogServiceAsync) GWT
                .create(NedCatalogService.class);
        ServiceDefTarget serviceDef = (ServiceDefTarget) service;
        serviceDef.setServiceEntryPoint("NedCatalogServlet");
        NedSelectMotdCallback selectCallback = new NedSelectMotdCallback(this);
        service.getMotd(selectCallback);
    }

    public void loadOldMessage(String oldMessage) {
        textAreaOldMessage.setText(oldMessage);
    }

    private class UpdateClickHandler implements ClickHandler {

        @Override
        public void onClick(ClickEvent event) {
            if (textArea.getText().isEmpty()) {
                NedAlert.showAlert(NedRes.instance().motdEmptyTextArea());
                return;
            }

            NedCatalogServiceAsync service = (NedCatalogServiceAsync) GWT
                    .create(NedCatalogService.class);
            ServiceDefTarget serviceDef = (ServiceDefTarget) service;
            serviceDef.setServiceEntryPoint("NedCatalogServlet");
            NedUpdateMotdCallback updateCallback = new NedUpdateMotdCallback(
                    NedMotdWidget.this);
            service.updateMotd(textArea.getText(), updateCallback);
        }
    }

    public void updateTextBoxes() {
        textAreaOldMessage.setText(textArea.getText());
        textArea.setText("");
    }
}
