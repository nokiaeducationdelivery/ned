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

import java.util.List;

import org.ned.server.nedadminconsole.client.NedCatalogService;
import org.ned.server.nedadminconsole.client.NedCatalogServiceAsync;
import org.ned.server.nedadminconsole.client.NedConstant;
import org.ned.server.nedadminconsole.client.NedRes;
import org.ned.server.nedadminconsole.client.callbacks.NedLibraryListCallback;
import org.ned.server.nedadminconsole.client.dialogs.NedNewElementDialog;
import org.ned.server.nedadminconsole.client.interfaces.NedLibraryListUpdater;
import org.ned.server.nedadminconsole.client.interfaces.NedLibrarySelector;
import org.ned.server.nedadminconsole.shared.NedObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

public class NedLibrarySelectorWidget extends Composite implements
        NedLibraryListUpdater {

    private ListBox listBox = null;
    private List<NedObject> libraries = null;
    private ClickHandlerLibrarySelect clickHandler = null;
    private NedLibrarySelector librarySelector;
    private Button buttonSelectLibrary;
    private Button buttonNewLibrary;
    
    public NedLibrarySelectorWidget(NedLibrarySelector librarySelector) {

        this.librarySelector = librarySelector;
        HorizontalPanel horizontalPanelMain = new HorizontalPanel();
        horizontalPanelMain.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        initWidget(horizontalPanelMain);
        horizontalPanelMain.setSize("100%", "100%");

        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        verticalPanel.setSpacing(8);
        horizontalPanelMain.add(verticalPanel);
        horizontalPanelMain.setCellWidth(verticalPanel, "95%");
        verticalPanel.setSize("95%", "398px");

        Label labelTitle = new Label(NedRes.instance().libDlgChooseLibrary()
                + ":");
        labelTitle.setStyleName("gwt-Label-element");
        verticalPanel.add(labelTitle);
        verticalPanel.setCellVerticalAlignment(labelTitle, HasVerticalAlignment.ALIGN_MIDDLE);
        labelTitle.setWidth("100%");

        listBox = new ListBox();
        listBox.setVisibleItemCount(10);
        listBox.setName(NedRes.instance().libDlgSelectLibrary());
        verticalPanel.add(listBox);
        verticalPanel.setCellVerticalAlignment(listBox, HasVerticalAlignment.ALIGN_MIDDLE);
        listBox.setSize("100%", "307px");

        HorizontalPanel horizontalPanel = new HorizontalPanel();
        verticalPanel.add(horizontalPanel);
        verticalPanel.setCellVerticalAlignment(horizontalPanel, HasVerticalAlignment.ALIGN_MIDDLE);
        horizontalPanel.setWidth("100%");
        verticalPanel.setCellWidth(horizontalPanel, "100%");

        buttonSelectLibrary = new Button();
        buttonSelectLibrary.setEnabled(false);
        horizontalPanel.add(buttonSelectLibrary);
        horizontalPanel.setCellWidth(buttonSelectLibrary, "50%");
        buttonSelectLibrary.setText(NedRes.instance().select());
        buttonSelectLibrary.setWidth("100%");

        buttonNewLibrary = new Button();
        buttonNewLibrary.setEnabled(false);
        horizontalPanel.add(buttonNewLibrary);
        horizontalPanel.setCellWidth(buttonNewLibrary, "50%");
        buttonNewLibrary.setText(NedRes.instance().libDlgNewLibrary());
        buttonNewLibrary.setWidth("100%");
        buttonNewLibrary.addClickHandler(new ClickHandlerNewLibrary());

        listBox.addItem(NedRes.instance().libDlgLoading());
        listBox.setEnabled(false);
    }

    public void loadLibraryList(List<NedObject> libraries) {
        this.libraries = libraries;
        listBox.clear();
        for (int i = 0; i < libraries.size(); i++) {
            listBox.addItem(libraries.get(i).name, libraries.get(i).id);
        }
        buttonNewLibrary.setEnabled(true);
        buttonSelectLibrary.setEnabled(true);
        listBox.setEnabled(true);
    }

    protected void onLoad() {
        if(clickHandler == null)
        {
        clickHandler = new ClickHandlerLibrarySelect();
        buttonSelectLibrary.addClickHandler(clickHandler);
        listBox.addDoubleClickHandler(clickHandler);
        }
        if(libraries == null)
        {
            NedCatalogServiceAsync service = (NedCatalogServiceAsync) GWT
                    .create(NedCatalogService.class);
            ServiceDefTarget serviceDef = (ServiceDefTarget) service;
            serviceDef.setServiceEntryPoint("NedCatalogServlet");
            NedLibraryListCallback serviceCallback = new NedLibraryListCallback(this);
        service.getLibraryList(serviceCallback);
        }
        
    }

    protected void onUnload() {
        clickHandler = null;
    }

    private class ClickHandlerLibrarySelect implements DoubleClickHandler,
            ClickHandler {

        private ClickHandlerLibrarySelect() {
        }

        @Override
        public void onClick(ClickEvent event) {
            handleLibrarySelect();

        }

        @Override
        public void onDoubleClick(DoubleClickEvent event) {
            handleLibrarySelect();

        }

        private void handleLibrarySelect() {
            librarySelector.selectLibrary(libraries.get(listBox.getSelectedIndex()));
        }

    }

    private class ClickHandlerNewLibrary implements ClickHandler {

        @Override
        public void onClick(ClickEvent event) {
            new NedNewElementDialog(NedConstant.TYPE_LIBRARY, null, null,
                    NedLibrarySelectorWidget.this).show();

        }
    }

    @Override
    public void addNewLibrary(NedObject library) {
        libraries.add(library);
        listBox.addItem(library.name, library.id);
    }
    
    public void removeLibrary(NedObject library)
    {
        for(int i = 0; i < listBox.getItemCount(); i++)
        {
            if(listBox.getValue(i).equals(library.id))
            {
                listBox.removeItem(i);
                libraries.remove(i);
                if(listBox.getSelectedIndex() > -1)
                {
                    listBox.setItemSelected( listBox.getSelectedIndex(), false);
                }
                return;
            }
        }

    }
}
