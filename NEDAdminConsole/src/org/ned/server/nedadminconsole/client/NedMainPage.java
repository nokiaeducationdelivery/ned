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

import java.util.ArrayList;

import org.ned.server.nedadminconsole.client.callbacks.NedMoveItemCallback;
import org.ned.server.nedadminconsole.client.dialogs.NedAlert;
import org.ned.server.nedadminconsole.client.interfaces.NedModelListener;
import org.ned.server.nedadminconsole.client.widgets.NedItemEditor;
import org.ned.server.nedadminconsole.client.widgets.NedLanguageWidget;
import org.ned.server.nedadminconsole.client.widgets.NedLibrarySelectorWidget;
import org.ned.server.nedadminconsole.client.widgets.NedLibraryTree;
import org.ned.server.nedadminconsole.client.widgets.NedMotdWidget;
import org.ned.server.nedadminconsole.client.widgets.NedStatisticsWidget;
import org.ned.server.nedadminconsole.client.widgets.NedUserManagementWidget;
import org.ned.server.nedadminconsole.shared.NedObject;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class NedMainPage implements EntryPoint, NedModelListener {

    NedItemEditor itemEditor;
    NedLibraryTree treeLibrary;
    VerticalPanel moveButtonsPanel;
    private NedDataModel model;
    private HorizontalPanel horizontalPanelMain;
    private FlowPanel flowPanel;
    private SimplePanel decoratorPanelFill;
    private TabPanel tabPanelMain;
    private VerticalPanel verticalPanel;
    private NedLibrarySelectorWidget librarySelector;
    protected int currentTabIndex;

    @Override
    public void onModuleLoad() {
        model = new NedDataModel();
        itemEditor = new NedItemEditor(model);
        treeLibrary = new NedLibraryTree(model);
        moveButtonsPanel = new VerticalPanel();

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
        horizontalPanelMain.setCellWidth(treeLibrary, "40%");
        horizontalPanelMain.setCellHorizontalAlignment(treeLibrary,
                HasHorizontalAlignment.ALIGN_LEFT);
        
        
        PushButton moveUpButton = new PushButton(new Image("images/up_button_idle.png"),new Image("images/up_button_pressed.png"));
        moveButtonsPanel.add( moveUpButton );
        moveUpButton.setSize("52px", "48px");
        moveUpButton.setStylePrimaryName("NedDeleteButton");
        moveButtonsPanel.setCellVerticalAlignment(moveUpButton,HasVerticalAlignment.ALIGN_TOP);
        moveButtonsPanel.setCellHeight(moveUpButton, "60px");
        moveUpButton.addClickHandler(new MoveItemCommand( true ));
        
        PushButton moveDownButton = new PushButton(new Image("images/down_button_idle.png"),new Image("images/down_button_pressed.png"));
        moveButtonsPanel.add( moveDownButton );
        moveDownButton.setSize("52px", "48px");      
        moveDownButton.setStylePrimaryName("NedDeleteButton");
        moveButtonsPanel.setCellVerticalAlignment(moveDownButton,HasVerticalAlignment.ALIGN_TOP);
        moveButtonsPanel.setCellHeight(moveDownButton, "60px");
        
        moveDownButton.addClickHandler( new MoveItemCommand( false ) );
        

        horizontalPanelMain.add(moveButtonsPanel);
        horizontalPanelMain.setSize( "100%", "100%" );
        
        horizontalPanelMain.setCellWidth(moveButtonsPanel, "20%");
        horizontalPanelMain.setCellHorizontalAlignment(moveButtonsPanel,
                HasHorizontalAlignment.ALIGN_CENTER);
        

        horizontalPanelMain.add(itemEditor);
        horizontalPanelMain.setCellWidth(itemEditor, "40%");
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

        Widget widgetLanguage = new NedLanguageWidget();
        tabPanelMain.add(widgetLanguage, NedRes.instance().mainLanguage(), false); 

       
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
                        int selectedIndex = event.getItem().intValue();
                        if (selectedIndex == 1
                                && model.getCurrentLibrary() == null) {
                            NedAlert.showAlert(NedRes.instance()
                                    .msgErrorNoLibrarySelected());
                            event.cancel();
                        } else if(tabPanelMain.getWidget(currentTabIndex) instanceof NedUserManagementWidget)
                        {
                            NedUserManagementWidget userManagement = (NedUserManagementWidget) tabPanelMain.getWidget(currentTabIndex);
                            if(userManagement.isBlocked())
                            {
                                userManagement.tryPersist(selectedIndex, tabPanelMain);
                                event.cancel();
                            }
                        }
                    }
                });
        tabPanelMain.addSelectionHandler(new SelectionHandler<Integer>() {
            @Override
            public void onSelection(SelectionEvent<Integer> event) {
                currentTabIndex = event.getSelectedItem().intValue();
                if(tabPanelMain.getWidget(currentTabIndex) instanceof NedUserManagementWidget)
                {
                    ((NedUserManagementWidget) tabPanelMain.getWidget(currentTabIndex)).refreshBlockNavigation();
                }
            }
        });
    }
    
    private class MoveItemCommand implements ClickHandler{

    	boolean moveUp;
    	
    	public MoveItemCommand( boolean moveUp ){
    		this.moveUp = moveUp;
    	}
    	
		@Override
		public void onClick(ClickEvent event) {
			NedObject currentItem = model.getCurrentObject();
			NedObject secondItem = null;
			
			if( moveUp ){
				secondItem = model.findPreviousObject( currentItem );
			}else{
				secondItem = model.findNextObject( currentItem );
			}
			
			if( secondItem == null ){
				return;
			}

			if( moveUp ){
				currentItem.index--;
				secondItem.index++;				
			}else{
				currentItem.index++;
				secondItem.index--;
			}
			
			ArrayList<NedObject> updateItems = new ArrayList<NedObject>();
			updateItems.add(currentItem);
			updateItems.add(secondItem);
			
            NedCatalogServiceAsync service = (NedCatalogServiceAsync) GWT
                    .create(NedCatalogService.class);
            ServiceDefTarget serviceDef = (ServiceDefTarget) service;
            serviceDef.setServiceEntryPoint("NedCatalogServlet");
            NedMoveItemCallback serviceCallback = new NedMoveItemCallback( model, moveUp );
            service.updateItems(updateItems, serviceCallback);
		}
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

	@Override
	public void objectMoved(NedDataModel source, boolean moveUp) {
        // no implementation required
	}
}
