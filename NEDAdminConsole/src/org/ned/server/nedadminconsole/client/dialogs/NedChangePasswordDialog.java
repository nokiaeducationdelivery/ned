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
import org.ned.server.nedadminconsole.client.interfaces.NedUsernameReceiver;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;

public class NedChangePasswordDialog extends DialogBox {

    private PasswordTextBox textBoxPassword;
    private NedUsernameReceiver receiver;
    private Label labelError;
    private String userName;
    private PasswordTextBox textBoxRepeat;
    
    public NedChangePasswordDialog(String userName, NedUsernameReceiver receiver) 
    {
        this.userName = userName;
        setModal(true);
        setAutoHideEnabled(false);
        setWidth("350");
        this.receiver = receiver;
        setHTML(NedRes.instance().userDlgNewPassword());
        
        Grid grid = new Grid(3, 2);
        grid.setCellSpacing(5);
        setWidget(grid);
        grid.setSize("350", "100%");
        
        Label labelUsername = new Label(NedRes.instance().userDlgPassword() + ":");
        grid.setWidget(0, 0, labelUsername);
        labelUsername.setWidth("100%");
        grid.getCellFormatter().setWidth(0, 0, "30%");
        grid.getCellFormatter().setWidth(0, 1, "60%");
        
        textBoxPassword = new PasswordTextBox();
        grid.setWidget(0, 1, textBoxPassword);
        textBoxPassword.setWidth("97%");
        
        Label labelRepeat = new Label(NedRes.instance().userDlgRepeatPassword());
        grid.setWidget(1, 0, labelRepeat);
        labelRepeat.setWidth("100%");
        
        textBoxRepeat = new PasswordTextBox();
        textBoxRepeat.setFocus(true);
        grid.setWidget(1, 1, textBoxRepeat);
        textBoxRepeat.setWidth("97%");
        
        
        labelError = new Label("");
        labelError.setStyleName("gwt-Label-red");
        grid.setWidget(2, 0, labelError);
        labelError.setWidth("100%");
        grid.getCellFormatter().setWidth(2, 0, "30%");
        
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        grid.setWidget(2, 1, horizontalPanel);
        horizontalPanel.setWidth("100%");
        grid.getCellFormatter().setWidth(2, 1, "60%");
        
        Button buttonOk = new Button(NedRes.instance().userDlgNewPassword());
        horizontalPanel.add(buttonOk);
        horizontalPanel.setCellWidth(buttonOk, "50%");
        buttonOk.setText(NedRes.instance().ok());
        buttonOk.addClickHandler(new ClickHandlerOk());
        buttonOk.setWidth("90%");
        
        Button buttonCancel = new Button(NedRes.instance().cancel());
        buttonCancel.addClickHandler(new ClickHandlerCancel());
        horizontalPanel.add(buttonCancel);
        buttonCancel.setWidth("90%");
        horizontalPanel.setCellWidth(buttonCancel, "50%");
        
        center();
        textBoxPassword.setFocus(true);
    }
       
    private class ClickHandlerOk implements ClickHandler
    {
        @Override
        public void onClick(ClickEvent event) {
            
            if(textBoxPassword.getText().isEmpty() || textBoxRepeat.getText().isEmpty()){
                NedAlert.showAlert(NedRes.instance().userDlgPasswordEmpty()); 
            }else if(!textBoxPassword.getText().equals(textBoxRepeat.getText())){
                NedAlert.showAlert(NedRes.instance().userDlgPasswordIdentical());
            }else{
                receiver.receivePassword(userName, textBoxPassword.getText());
                hide();                
            }
        }
    }
    
    private class ClickHandlerCancel implements ClickHandler
    {
        @Override
        public void onClick(ClickEvent event) {
            hide();
            
        }
    }

}
