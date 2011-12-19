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
import org.ned.server.nedadminconsole.client.NedConstant;
import org.ned.server.nedadminconsole.client.NedDataModel;
import org.ned.server.nedadminconsole.client.NedRes;
import org.ned.server.nedadminconsole.client.callbacks.NedDeleteItemCallback;
import org.ned.server.nedadminconsole.client.callbacks.NedUpdateItemCallback;
import org.ned.server.nedadminconsole.client.dialogs.NedAlert;
import org.ned.server.nedadminconsole.client.dialogs.NedUploadDialog;
import org.ned.server.nedadminconsole.client.interfaces.ConfirmCallback;
import org.ned.server.nedadminconsole.client.interfaces.NedModelListener;
import org.ned.server.nedadminconsole.shared.NedObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class NedItemEditor extends Composite implements NedModelListener {
    private NedDataModel model;
    private Label labelFile;
    private TextBox textBoxType;
    private TextBox textBoxTitle;
    private TextBox textBoxId;
    private TextBox textBoxIdParent;
    private TextBox textBoxKeywords;
    private TextBox textBoxFile;
    private TextArea textAreaDescription;
    private TextArea textAreaLinks;
    private String keywordsContent;
    private String linksContent;
    private HorizontalPanel horizontalPanelFile;
    private Label lblElementInfo;
    private Button buttonUpload;

    public NedItemEditor(NedDataModel model) {
        this.model = model;

        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        verticalPanel
                .setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        verticalPanel.setSpacing(8);
        verticalPanel.setBorderWidth(0);
        verticalPanel.setSize("400px", "100%");
        initWidget(verticalPanel);

        lblElementInfo = new Label(NedRes.instance().itemEditElementInfo());
        lblElementInfo.setStyleName("gwt-Label-element");
        verticalPanel.add(lblElementInfo);
        verticalPanel.setCellVerticalAlignment(lblElementInfo, HasVerticalAlignment.ALIGN_MIDDLE);
        verticalPanel.setCellHorizontalAlignment(lblElementInfo,
                HasHorizontalAlignment.ALIGN_CENTER);
        lblElementInfo.setSize("100%", "");

        Grid gridItemInfo = new Grid(8, 2);
        gridItemInfo.setCellPadding(10);
        gridItemInfo.setStyleName("NedItemEditorPanel");
        verticalPanel.add(gridItemInfo);
        verticalPanel.setCellVerticalAlignment(gridItemInfo, HasVerticalAlignment.ALIGN_MIDDLE);
        verticalPanel.setCellWidth(gridItemInfo, "300px");
        verticalPanel.setCellHorizontalAlignment(gridItemInfo,
                HasHorizontalAlignment.ALIGN_CENTER);
        gridItemInfo.setSize("392px", "470px");

        Label lblNewLabel_3 = new Label(NedRes.instance().id() + ":");
        gridItemInfo.setWidget(0, 0, lblNewLabel_3);
        gridItemInfo.getCellFormatter().setWidth(0, 0, "30%");

        textBoxId = new TextBox();
        gridItemInfo.setWidget(0, 1, textBoxId);
        textBoxId.setWidth("97%");
        gridItemInfo.getCellFormatter().setWidth(0, 1, "");
        textBoxId.setReadOnly(true);

        Label lblNewLabel_5 = new Label(NedRes.instance().itemEditParentId()
                + ":");
        gridItemInfo.setWidget(1, 0, lblNewLabel_5);
        gridItemInfo.getCellFormatter().setWidth(1, 0, "30%");

        textBoxIdParent = new TextBox();
        gridItemInfo.setWidget(1, 1, textBoxIdParent);
        gridItemInfo.getCellFormatter().setWidth(1, 1, "");
        textBoxIdParent.setMaxLength(20);
        textBoxIdParent.setReadOnly(true);
        textBoxIdParent.setWidth("97%");

        Label lblNewLabel = new Label(NedRes.instance().itemEditTitle() + ":");
        gridItemInfo.setWidget(2, 0, lblNewLabel);

        textBoxTitle = new TextBox();
        gridItemInfo.setWidget(2, 1, textBoxTitle);
        textBoxTitle.setMaxLength(40);
        textBoxTitle.setWidth("97%");

        Label lblNewLabel_1 = new Label(NedRes.instance().itemEditType() + ":");
        gridItemInfo.setWidget(3, 0, lblNewLabel_1);

        textBoxType = new TextBox();
        gridItemInfo.setWidget(3, 1, textBoxType);
        textBoxType.setReadOnly(true);
        textBoxType.setWidth("97%");

        Label lblNewLabel_2 = new Label(NedRes.instance().itemEditDescription()
                + ":");
        gridItemInfo.setWidget(4, 0, lblNewLabel_2);

        textAreaDescription = new TextArea();
        gridItemInfo.setWidget(4, 1, textAreaDescription);
        textAreaDescription.setVisibleLines(4);
        textAreaDescription.setCharacterWidth(30);
        textAreaDescription.setSize("97%", "");

        Label lblKeywords = new Label(NedRes.instance().itemEditKeywords()
                + ":");
        gridItemInfo.setWidget(5, 0, lblKeywords);

        textBoxKeywords = new TextBox();
        gridItemInfo.setWidget(5, 1, textBoxKeywords);
        textBoxKeywords.setVisibleLength(30);
        textBoxKeywords.setWidth("97%");

        Label lblExternalLinks = new Label(NedRes.instance()
                .itemEditExternalLinks() + ":");
        gridItemInfo.setWidget(6, 0, lblExternalLinks);

        textAreaLinks = new TextArea();
        gridItemInfo.setWidget(6, 1, textAreaLinks);
        textAreaLinks.setCharacterWidth(30);
        textAreaLinks.setWidth("97%");

        labelFile = new Label(NedRes.instance().itemFile() + ":");
        gridItemInfo.setWidget(7, 0, labelFile);

        horizontalPanelFile = new HorizontalPanel();
        horizontalPanelFile
                .setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        horizontalPanelFile
                .setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        gridItemInfo.setWidget(7, 1, horizontalPanelFile);
        horizontalPanelFile.setWidth("100%");
        gridItemInfo.getCellFormatter().setWidth(7, 1, "");

        textBoxFile = new TextBox();
        horizontalPanelFile.add(textBoxFile);
        horizontalPanelFile.setCellWidth(textBoxFile, "60%");
        textBoxFile.setReadOnly(true);
        textBoxFile.setWidth("147px");

        buttonUpload = new Button();
        horizontalPanelFile.add(buttonUpload);
        horizontalPanelFile.setCellHorizontalAlignment(buttonUpload,
                HasHorizontalAlignment.ALIGN_RIGHT);
        horizontalPanelFile.setCellWidth(buttonUpload, "40%");
        buttonUpload.setText(NedRes.instance().uploadDlgUpload());
        buttonUpload.addClickHandler(new CommandUploadFile());
        buttonUpload.setWidth("100%");
        gridItemInfo.getCellFormatter().setHorizontalAlignment(1, 0,
                HasHorizontalAlignment.ALIGN_LEFT);
        gridItemInfo.getCellFormatter().setHorizontalAlignment(2, 0,
                HasHorizontalAlignment.ALIGN_LEFT);
        gridItemInfo.getCellFormatter().setHorizontalAlignment(3, 0,
                HasHorizontalAlignment.ALIGN_LEFT);
        gridItemInfo.getCellFormatter().setHorizontalAlignment(4, 0,
                HasHorizontalAlignment.ALIGN_LEFT);
        gridItemInfo.getCellFormatter().setHorizontalAlignment(5, 0,
                HasHorizontalAlignment.ALIGN_LEFT);
        gridItemInfo.getCellFormatter().setHorizontalAlignment(6, 0,
                HasHorizontalAlignment.ALIGN_LEFT);
        gridItemInfo.getCellFormatter().setHorizontalAlignment(7, 0,
                HasHorizontalAlignment.ALIGN_LEFT);
        gridItemInfo.getCellFormatter().setHorizontalAlignment(0, 0,
                HasHorizontalAlignment.ALIGN_LEFT);
        gridItemInfo.getCellFormatter().setHorizontalAlignment(0, 1,
                HasHorizontalAlignment.ALIGN_LEFT);
        gridItemInfo.getCellFormatter().setHorizontalAlignment(4, 1,
                HasHorizontalAlignment.ALIGN_LEFT);
        textAreaLinks.getElement().setAttribute("wrap", "off");

        HorizontalPanel horizontalPanel_4 = new HorizontalPanel();
        horizontalPanel_4
                .setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        verticalPanel.add(horizontalPanel_4);
        verticalPanel.setCellVerticalAlignment(horizontalPanel_4,
                HasVerticalAlignment.ALIGN_MIDDLE);
        horizontalPanel_4.setWidth("100%");

        Button buttonSave = new Button("");
        buttonSave.setText(NedRes.instance().save());
        horizontalPanel_4.add(buttonSave);
        horizontalPanel_4.setCellWidth(buttonSave, "25%");
        buttonSave.addClickHandler(new UpdateItemCommand());
        buttonSave.setWidth("100%");

        Button buttonDelete = new Button("");
        buttonDelete.setText(NedRes.instance().delete());
        buttonDelete.addClickHandler(new CommandDeleteItem());
        horizontalPanel_4.add(buttonDelete);
        horizontalPanel_4.setCellHorizontalAlignment(buttonDelete,
                HasHorizontalAlignment.ALIGN_RIGHT);
        buttonDelete.setWidth("100%");
        horizontalPanel_4.setCellWidth(buttonDelete, "25%");

        // decoratorPanel.add(verticalPanel);
        setVisible(false);
    }

    @Override
    public void libraryChanged(NedDataModel source) {
        readElement(source.getCurrentLibrary());
    }

    @Override
    protected void onLoad() {
        model.addListener(this);
        if (model.getCurrentLibrary() != null) {
            readElement(model.getCurrentLibrary());
        }
    }

    @Override
    protected void onUnload() {
        model.removeListener(this);
    }

    private void readElement(NedObject element) {
        lblElementInfo.setText(NedRes.instance().itemEditElementInfo() + ": "
                + element.name);

        textBoxId.setText(element.id);
        if (element.parentId == null) {
            textBoxIdParent.setText(NedRes.instance().itemEditNoParent());
        } else {
            textBoxIdParent.setText(element.parentId);
        }
        textBoxTitle.setText(element.name);
        textBoxType.setText(element.type);
        textAreaDescription.setText(element.description);
        if (element.keywords != null) {
            textBoxKeywords
                    .setText(stringArrayToString(element.keywords, ", "));
            keywordsContent = textBoxKeywords.getText();
        } else {
            textBoxKeywords.setText(null);
        }
        if (element.externalLinks != null) {
            textAreaLinks.setText(stringArrayToString(element.externalLinks,
                    "\n"));
            linksContent = textAreaLinks.getText();
        } else {
            textAreaLinks.setText(null);
        }

        loadUploadPanel(element);
        setVisible(true);

    }

    private void loadUploadPanel(NedObject element) {
        if (element.type.equals(NedConstant.TYPE_LIBRARY)
                || element.type.equals(NedConstant.TYPE_CATEGORY)
                || element.type.equals("Catalog")) {

            buttonUpload.setEnabled(false);
        } else {

            buttonUpload.setEnabled(true);
            String fileName = element.data;
            if (fileName == null) {
                textBoxFile.setText(NedRes.instance().itemNoFile());
            } else {
                textBoxFile.setText(fileName);
            }
        }

    }

    private String stringArrayToString(String[] array, String separator) {
        StringBuilder resultStringBuilder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            resultStringBuilder.append(array[i]);
            resultStringBuilder.append(separator);
        }
        return resultStringBuilder.toString();
    }

    private class UpdateItemCommand implements ClickHandler {
        @Override
        public void onClick(ClickEvent event) {
            NedObject changed = changeElement();

            if (changed == null) {
                NedAlert.showAlert(NedRes.instance().itemEditNoChanges());
                return;
            } else if (changed.name.isEmpty()) {
                NedAlert.showAlert(NedRes.instance().msgErrorEmptyName());
                return;
            }

            NedCatalogServiceAsync service = (NedCatalogServiceAsync) GWT
                    .create(NedCatalogService.class);
            ServiceDefTarget serviceDef = (ServiceDefTarget) service;
            serviceDef.setServiceEntryPoint("NedCatalogServlet");
            NedUpdateItemCallback serviceCallback = new NedUpdateItemCallback(
                    model);
            service.updateItem(changed, serviceCallback);
        }

        NedObject changeElement() {
            boolean isChanged = false;
            NedObject current = model.getCurrentObject();
            if (!textBoxTitle.getText().equals(current.name)) {
                current.name = textBoxTitle.getText();
                isChanged = true;
            }
            if (!textAreaDescription.getText().equals(current.description)) {
                current.description = textAreaDescription.getText();
                isChanged = true;
            }
            if (!(linksContent == null && textAreaLinks.getText().isEmpty())
                    && !textAreaLinks.getText().equals(linksContent)) {
                current.externalLinks = createStringArray(
                        textAreaLinks.getText(), "\n");
                isChanged = true;
            }
            if (!(keywordsContent == null && textBoxKeywords.getText()
                    .isEmpty())
                    && !textBoxKeywords.getText().equals(keywordsContent)) {
                current.keywords = createStringArray(textBoxKeywords.getText()
                        .replace(" ", ""), ",");
                isChanged = true;
            }
            return isChanged ? current : null;
        }

        private String[] createStringArray(String text, String splitSymbol) {
            return text.split(splitSymbol);
        }
    }

    private class CommandDeleteItem implements ClickHandler {

        @Override
        public void onClick(ClickEvent event) {

            //TODO localize
            NedAlert.showConfirmYesNo("Do you want to delete item?", new ConfirmCallback() {
                @Override
                public void onYes() {
                    deleteItem();
                }
                @Override
                public void onCancel() {
                }
                @Override
                public void onNo() {
                }
                
            });
        }
        
        private void deleteItem(){
            NedCatalogServiceAsync service = (NedCatalogServiceAsync) GWT
                                                .create(NedCatalogService.class);
            ServiceDefTarget serviceDef = (ServiceDefTarget) service;
            serviceDef.setServiceEntryPoint("NedCatalogServlet");
            NedDeleteItemCallback serviceCallback = new NedDeleteItemCallback(model);
            service.deleteItem(model.getCurrentObject().id, serviceCallback);            
        }
    }

    private class CommandUploadFile implements ClickHandler {
        @Override
        public void onClick(ClickEvent event) {
            new NedUploadDialog(model.getCurrentLibrary().id, model).show();
        }
    }

    @Override
    public void libraryContentLoaded(NedDataModel source) {
    }

    @Override
    public void objectSelection(NedDataModel source) {
        readElement(source.getCurrentObject());
    }

    @Override
    public void objectChanged(NedDataModel source) {
        readElement(source.getCurrentObject());
    }

    @Override
    public void fileUploaded(NedDataModel source) {
        NedObject currentElement = source.getCurrentObject();
        textBoxType.setText(currentElement.type);
        loadUploadPanel(currentElement);
    }

    @Override
    public void objectDeleted(NedDataModel source, String objectType) {
        setVisible(false);
    }
}
