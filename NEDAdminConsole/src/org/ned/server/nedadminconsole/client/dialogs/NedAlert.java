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
package org.ned.server.nedadminconsole.client.dialogs;

import org.ned.server.nedadminconsole.client.NedRes;
import org.ned.server.nedadminconsole.client.interfaces.ConfirmCallback;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class NedAlert extends DialogBox {
    
    private static final String TYPE_ALERT = "Info";
    private static final String TYPE_CONFIRM = "Confirm";
    private static final String TYPE_WAIT = "Wait";
    private static final String TYPE_CONFIRM_CANCEL = "ConfirmCancel";
    
    private Label lblMessage;    
    private HorizontalPanel horizontalPanel;
    
    private Button btnOk;
    private Button btnYes;
    private Button btnNo;
    private Button btnCancel;
    private String type;
    
    private ConfirmCallback callback = null;



    public NedAlert(boolean autohide, boolean modal, String message, String aType) {
        super(autohide, modal);
        this.type = aType;
        
        setHTML(NedRes.instance().message());
        
        AbsolutePanel absolutePanel = new AbsolutePanel();
        setWidget(absolutePanel);
        absolutePanel.setSize("319px", "106px");
        
        lblMessage = new Label(message);
        lblMessage.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        lblMessage.setStyleName("gwt-Label-message");
        lblMessage.setDirectionEstimator(false);
        absolutePanel.add(lblMessage, 10, 10);
        lblMessage.setSize("299px", "53px");
        
        horizontalPanel = new HorizontalPanel();
        horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        absolutePanel.add(horizontalPanel, 0, 69);
        horizontalPanel.setWidth("100%");
        
        
        if(type.equals(TYPE_ALERT)){
            btnOk = new Button();
            horizontalPanel.add(btnOk);
            horizontalPanel.setCellHorizontalAlignment(btnOk, HasHorizontalAlignment.ALIGN_CENTER);
            horizontalPanel.setCellWidth(btnOk, "50%");
            btnOk.setText(NedRes.instance().ok());
            btnOk.setWidth("50%");          
            btnOk.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    hide();
                }
            });            
        }
        
        if(type.equals(TYPE_CONFIRM) || type.equals(TYPE_CONFIRM_CANCEL)){
            String cellWidth = null;
            if(type.equals(TYPE_CONFIRM)){
                cellWidth = "50%";
            }else{
                cellWidth = "33%";
            }
            
            btnYes = new Button();
            horizontalPanel.add(btnYes);
            horizontalPanel.setCellHorizontalAlignment(btnYes, HasHorizontalAlignment.ALIGN_CENTER);
            horizontalPanel.setCellWidth(btnYes, cellWidth);
            btnYes.setText(NedRes.instance().yes()); 
            btnYes.setWidth("90%");
            btnYes.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if(callback != null){
                        callback.onYes();
                    }
                    hide();
                }
            });
            
            btnNo = new Button();
            horizontalPanel.add(btnNo);
            horizontalPanel.setCellHorizontalAlignment(btnNo, HasHorizontalAlignment.ALIGN_CENTER);
            horizontalPanel.setCellWidth(btnNo, cellWidth);
            btnNo.setText(NedRes.instance().no()); 
            btnNo.setWidth("90%");
            btnNo.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if(callback != null){
                        callback.onNo();
                    }
                    hide();
                }
            });
            
            
            if(type.equals(TYPE_CONFIRM_CANCEL)){
                btnCancel = new Button("New button");
                btnCancel.setText(NedRes.instance().cancel()); 
                horizontalPanel.add(btnCancel);
                horizontalPanel.setCellHorizontalAlignment(btnCancel, HasHorizontalAlignment.ALIGN_CENTER);
                btnCancel.setWidth("90%");
                horizontalPanel.setCellWidth(btnCancel, cellWidth);
                btnCancel.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) { 
                        if(callback != null){
                            callback.onCancel();
                        }
                        hide();
                    }
                });
            }
        }       
    }
    

    public void setCallback(ConfirmCallback callback) {
        this.callback = callback;
    }
    
    public static void showAlert(String alertMessage){
        NedAlert alert = new NedAlert(true, true, alertMessage, TYPE_ALERT);
        alert.center();
        alert.show();
    }
    
    public static NedAlert showInfo(String message){
        NedAlert alert = new NedAlert(false, true, message, TYPE_WAIT);
        alert.center();
        alert.show();
        return alert;
    }
    
    public static void showConfirmYesNo(String message, ConfirmCallback callback){
        NedAlert alert = new NedAlert(false, false, message, TYPE_CONFIRM);
        alert.setCallback(callback);
        alert.center();
        alert.show();
    }
    
    public static void showConfirmYesNoCancel(String message, ConfirmCallback callback){
        NedAlert alert = new NedAlert(false, false, message, TYPE_CONFIRM_CANCEL);
        alert.setCallback(callback);
        alert.center();
        alert.show();        
    }
}
