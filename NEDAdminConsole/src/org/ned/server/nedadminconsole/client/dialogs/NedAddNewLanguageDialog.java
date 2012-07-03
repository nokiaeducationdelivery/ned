package org.ned.server.nedadminconsole.client.dialogs;

import org.ned.server.nedadminconsole.client.NedRes;
import org.ned.server.nedadminconsole.client.widgets.NedLanguageWidget;
import org.ned.server.nedadminconsole.shared.NedServerResponses;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;

public class NedAddNewLanguageDialog extends DialogBox {
    private NedLanguageWidget parent;
    private FileUpload fileUpload;
    private final FormPanel uploadForm;
    private NedAlert uploadAlert;

    public NedAddNewLanguageDialog(NedLanguageWidget nedLanguageWidget) {
        
        this.parent = nedLanguageWidget;
        
        VerticalPanel verticalPanelMain = new VerticalPanel();
        
        setWidget(verticalPanelMain);
        setHTML(NedRes.instance().userDlgAddNewLanguage());
        
        uploadForm = new FormPanel();
        uploadForm.setMethod(FormPanel.METHOD_POST);
        uploadForm.setAction("NedFileUploadServlet"); //TODO correct upload servlet
        uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
        verticalPanelMain.add(uploadForm);
        
        Grid grid = new Grid(4, 2);
        uploadForm.setWidget(grid);
        grid.setSize("100%", "100%");
        
        Label labelName = new Label(NedRes.instance().langName());
        grid.setWidget(0, 0, labelName);
        
        TextBox textBoxName = new TextBox();
        textBoxName.setName("languageName");
        grid.setWidget(0, 1, textBoxName);
        
        Label labelLocale = new Label(NedRes.instance().langLocaleString());
        grid.setWidget(1, 0, labelLocale);
        
        TextBox textBoxLocale = new TextBox();
        textBoxLocale.setName("languageLocale");
        grid.setWidget(1, 1, textBoxLocale);
        
        Label labelTranslationFile = new Label(NedRes.instance().langTranslationFile());
        grid.setWidget(2, 0, labelTranslationFile);
        
        HorizontalPanel horizontalPanelUpload = new HorizontalPanel();
        grid.setWidget(2, 1, horizontalPanelUpload);
        
        fileUpload = new FileUpload();
        horizontalPanelUpload.add(fileUpload);
        fileUpload.setName("fileUpload");
        
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        grid.setWidget(3, 1, horizontalPanel);
        grid.getCellFormatter().setWidth(3, 1, "");
        horizontalPanel.setWidth("100%");
        
        Button buttonUpload = new Button(NedRes.instance().add());
        buttonUpload.addClickHandler(new ClickUploadHandler());
        horizontalPanel.add(buttonUpload);
        buttonUpload.setWidth("90%");
        horizontalPanel.setCellWidth(buttonUpload, "50%");
        
        Button buttonCancel = new Button(NedRes.instance().cancel());
        buttonCancel.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                hide();
            }
        });
        horizontalPanel.add(buttonCancel);
        buttonCancel.setWidth("90%");
        horizontalPanel.setCellWidth(buttonCancel, "50%");
        
        setPopupPosition(200, 200);

        uploadForm.addSubmitHandler(new SubmitHandlerUploadFile());

        uploadForm
                .addSubmitCompleteHandler(new SubmitCompleteHandlerUploadFile());
        center();

    }
        
    private class ClickUploadHandler implements ClickHandler
    {

        @Override
        public void onClick(ClickEvent event) {
            String filename = fileUpload.getFilename();            
            if (filename.length() == 0) {
                NedAlert.showAlert(NedRes.instance().langMsgNoFile());
            }  else {
                    uploadForm.submit();
            }
        }
    }

    private class SubmitHandlerUploadFile implements SubmitHandler {

        @Override
        public void onSubmit(SubmitEvent event) {
            showWaitDialog();
        }
    }

    private void showWaitDialog(){
        uploadAlert = NedAlert.showInfo(NedRes.instance().uploadingFile()); 
    }

    private class SubmitCompleteHandlerUploadFile implements
    SubmitCompleteHandler {

        @Override
        public void onSubmitComplete(SubmitCompleteEvent event) {
            String message = event.getResults();
            if(uploadAlert != null){
                uploadAlert.hide();
                parent.reloadList();
            }
            if (message.contains("Error")) {
                displayError(message);
            } else {
                //todo NedAlert.showAlert(NedRes.instance().uploadSuccess());
            }
            hide();
    }

    private void displayError(String errorMessage) {
        if (errorMessage.equals(NedServerResponses.ERROR_WRONG_FILE_TYPE)) {
            NedAlert.showAlert(NedRes.instance().uploadErrorWrongFileType());
        } else if (errorMessage
                .equals(NedServerResponses.ERROR_DATABASE_UPDATE)) {
            NedAlert.showAlert(NedRes.instance()
                    .uploadErrorDatabaseUpdate());
        } else if (errorMessage
                .equals(NedServerResponses.ERROR_MULTIPART_CONTENT)) {
            NedAlert.showAlert(NedRes.instance()
                    .uploadErrorMultipartContent());
        } else if (errorMessage
                .equals(NedServerResponses.ERROR_BAD_REQUEST)) {
            NedAlert.showAlert(NedRes.instance().uploadErrorBadRequest());
        }
    }
    }

}
