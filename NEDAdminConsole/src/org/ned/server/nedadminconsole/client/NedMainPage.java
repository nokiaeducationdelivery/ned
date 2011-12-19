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
package org.ned.server.nedadminconsole.client;

import org.ned.server.nedadminconsole.client.dialogs.NedAlert;
import org.ned.server.nedadminconsole.client.interfaces.NedModelListener;
import org.ned.server.nedadminconsole.client.widgets.NedItemEditor;
import org.ned.server.nedadminconsole.client.widgets.NedLibrarySelectorWidget;
import org.ned.server.nedadminconsole.client.widgets.NedLibraryTree;
import org.ned.server.nedadminconsole.client.widgets.NedMotdWidget;
import org.ned.server.nedadminconsole.client.widgets.NedStatisticsWidget;
import org.ned.server.nedadminconsole.client.widgets.NedUserManagementWidget;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.user.client.ui.VerticalPanel;

public class NedMainPage implements EntryPoint, NedModelListener {

    NedItemEditor itemEditor;
    NedLibraryTree treeLibrary;
    private NedDataModel model;
    private HorizontalPanel horizontalPanelMain;
    private FlowPanel flowPanel;
    private SimplePanel decoratorPanelFill;
    private TabPanel tabPanelMain;
    private VerticalPanel verticalPanel;
    private NedLibrarySelectorWidget librarySelector;

    @Override
    public void onModuleLoad() {
        model = new NedDataModel();
        itemEditor = new NedItemEditor(model);
        treeLibrary = new NedLibraryTree(model);

        RootPanel rootPanel = RootPanel.get();
        rootPanel.setStyleName("gwt-DecoratorPanel");

        flowPanel = new FlowPanel();
        flowPanel.setStyleName("NedMainPanel");
        rootPanel.add(flowPanel);
        flowPanel.setHeight("");

        Image imageHeader = new Image("images/header.png");
        imageHeader.setStyleName("Ned-Image");
        flowPanel.add(imageHeader);
        imageHeader.setSize("842px", "73px");

        tabPanelMain = new TabPanel();

        tabPanelMain.setStyleName("main-page-background");
        flowPanel.add(tabPanelMain);
        tabPanelMain.setSize("100%", "650px");

        librarySelector = new NedLibrarySelectorWidget(model);
        tabPanelMain.add(librarySelector, NedRes.instance()
                .libDlgSelectLibrary(), false);
        
        
        verticalPanel = new VerticalPanel();
        tabPanelMain.add(verticalPanel, NedRes.instance().mainTabLibraryManager(), false);
        verticalPanel
                .setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        verticalPanel.setSize("100%", "100%");

        horizontalPanelMain = new HorizontalPanel();
        verticalPanel.add(horizontalPanelMain);
        horizontalPanelMain.setSize("95%", "560px");
        horizontalPanelMain
                .setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        horizontalPanelMain.setStyleName("gwt-Ned-DecoratorPanel");

        horizontalPanelMain.add(treeLibrary);
        horizontalPanelMain.setCellWidth(treeLibrary, "50%");
        horizontalPanelMain.setCellHorizontalAlignment(treeLibrary,
                HasHorizontalAlignment.ALIGN_LEFT);

        horizontalPanelMain.add(itemEditor);
        horizontalPanelMain.setCellWidth(itemEditor, "50%");
        horizontalPanelMain.setCellHorizontalAlignment(itemEditor,
                HasHorizontalAlignment.ALIGN_RIGHT);
        

        Widget widgetUserManagement = new NedUserManagementWidget();
        tabPanelMain.add(widgetUserManagement, NedRes.instance()
                .userManagement(), false);

        Widget messageOfTheDay = new NedMotdWidget();
        tabPanelMain.add(messageOfTheDay, NedRes.instance().mainMOTD(), false);

        Widget widgetStatistics = new NedStatisticsWidget();
        tabPanelMain.add(widgetStatistics, NedRes.instance().mainStatistics(),
                false);

       
        decoratorPanelFill = new SimplePanel();
        flowPanel.add(decoratorPanelFill);
        decoratorPanelFill.setPixelSize(
                imageHeader.getWidth(),
                Window.getClientHeight() - imageHeader.getHeight()
                        - flowPanel.getOffsetHeight());
        decoratorPanelFill.setStyleName("gwt-DecoratorPanel-gradient");
        model.addListener(this);
        tabPanelMain.selectTab(0);

        tabPanelMain
                .addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
                    public void onBeforeSelection(
                            BeforeSelectionEvent<Integer> event) {
                        if (event.getItem().intValue() == 1
                                && model.getCurrentLibrary() == null) {
                            NedAlert.showAlert(NedRes.instance()
                                    .msgErrorNoLibrarySelected());
                            event.cancel();
                        }
                    }
                });
    }

    @Override
    public void libraryChanged(NedDataModel source) {
        tabPanelMain.selectTab(1);
    }

    @Override
    public void libraryContentLoaded(NedDataModel source) {
    }

    @Override
    public void objectSelection(NedDataModel source) {
        // do nothing
    }

    @Override
    public void objectChanged(NedDataModel source) {
        // no implementation required
    }

    public void fileUploaded(NedDataModel source) {
        // no implementation required
    }

    @Override
    public void objectDeleted(NedDataModel source, String objectType) {
        if (objectType.equals(NedConstant.TYPE_LIBRARY)) {
            tabPanelMain.selectTab(0);
            librarySelector.removeLibrary(model.getCurrentLibrary());
        }
    }
}
