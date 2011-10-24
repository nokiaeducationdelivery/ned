package org.ned.server.nedadminconsole.client.dialogs;

import java.util.List;

import org.ned.server.nedadminconsole.client.NedRes;
import org.ned.server.nedadminconsole.client.interfaces.NedUsernameReceiver;
import org.ned.server.nedadminconsole.shared.NedUser;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

public class NedAddNewUserDialog extends DialogBox {

    private List<NedUser> listUsers;
    private TextBox textBoxUsername;
    private NedUsernameReceiver receiver;
    private Label labelError;

    
    public NedAddNewUserDialog(List<NedUser> listUsers, NedUsernameReceiver receiver) 
    {
        setModal(true);
        setAutoHideEnabled(false);
        setWidth("350");
        this.listUsers = listUsers;
        this.receiver = receiver;
        setHTML(NedRes.instance().userDlgAddNewUser());
        
        Grid grid = new Grid(2, 2);
        grid.setCellSpacing(5);
        setWidget(grid);
        grid.setSize("350", "100%");
        
        Label labelUsername = new Label(NedRes.instance().username() + ":");
        grid.setWidget(0, 0, labelUsername);
        labelUsername.setWidth("100%");
        grid.getCellFormatter().setWidth(0, 0, "30%");
        grid.getCellFormatter().setWidth(0, 1, "60%");
        
        textBoxUsername = new TextBox();
        grid.setWidget(0, 1, textBoxUsername);
        textBoxUsername.setWidth("97%");
        
        
        labelError = new Label("");
        labelError.setStyleName("gwt-Label-red");
        grid.setWidget(1, 0, labelError);
        labelError.setWidth("100%");
        grid.getCellFormatter().setWidth(1, 0, "30%");
        
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        grid.setWidget(1, 1, horizontalPanel);
        horizontalPanel.setWidth("100%");
        grid.getCellFormatter().setWidth(1, 1, "60%");
        
        Button buttonOk = new Button(NedRes.instance().userDlgAddUser());
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
        textBoxUsername.setFocus(true);
    }
    
    private boolean checkUsernameAvailable(String username)
    {
        boolean retval = true;
        for(int i = 0; i < listUsers.size() && retval; i++)
        {
            if(listUsers.get(i).username.equals(username))
            {
                retval = false;
            }
        }
        return retval;
    }
    
    private class ClickHandlerOk implements ClickHandler
    {

        @Override
        public void onClick(ClickEvent event) {
            if(!textBoxUsername.getText().isEmpty() && checkUsernameAvailable(textBoxUsername.getText()))
            {
                receiver.receiveUsername(textBoxUsername.getText());
                hide();
            } else
            {
                labelError.setText(NedRes.instance().userDlgAlreadyInUse());
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
