package org.ned.server.nedadminconsole.client.dialogs;

import org.ned.server.nedadminconsole.client.NedDataModel;
import org.ned.server.nedadminconsole.client.NedRes;
import org.ned.server.nedadminconsole.client.interfaces.ConfirmCallback;
import org.ned.server.nedadminconsole.shared.NedServerResponses;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class NedUploadDialog extends DialogBox {

    
    private final FormPanel uploadForm;
    private FileUpload fileUpload;
    private Button closeBtn;

    private String filename;
    private NedDataModel model;
    private HorizontalPanel horizontalPanel_2;
    private Hidden hiddenLibId;
    private Hidden hiddenContentId;
    
    private NedAlert uploadAlert = null;

    public NedUploadDialog(String libId, NedDataModel model) {
        setSize("395px", "147px");

        this.model = model;
        setHTML(NedRes.instance().uploadDlgUploadFile());

        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        verticalPanel
                .setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        setWidget(verticalPanel);
        verticalPanel.setSize("386px", "100px");

        uploadForm = new FormPanel();
        uploadForm.setAction("NedFileUploadServlet");
        uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
        uploadForm.setMethod(FormPanel.METHOD_POST);
        verticalPanel.add(uploadForm);
        uploadForm.setSize("100%", "100%");

        VerticalPanel verticalPanel_1 = new VerticalPanel();
        uploadForm.setWidget(verticalPanel_1);
        verticalPanel_1.setSize("100%", "77px");

        horizontalPanel_2 = new HorizontalPanel();
        verticalPanel_1.add(horizontalPanel_2);
        horizontalPanel_2.setWidth("100%");

        fileUpload = new FileUpload();
        horizontalPanel_2.add(fileUpload);
        horizontalPanel_2.setCellWidth(fileUpload, "100%");
        fileUpload.setName("fileUpload");
        verticalPanel_1.setCellHorizontalAlignment(fileUpload,
                HasHorizontalAlignment.ALIGN_CENTER);
        verticalPanel_1.setCellVerticalAlignment(fileUpload,
                HasVerticalAlignment.ALIGN_MIDDLE);
        fileUpload.setSize("100%", "30px");

        hiddenLibId = new Hidden("libId");
        horizontalPanel_2.add(hiddenLibId);
        hiddenLibId.setValue(libId);

        hiddenContentId = new Hidden("contentId");
        horizontalPanel_2.add(hiddenContentId);
        hiddenContentId.setValue(model.getCurrentObject().id);

        HorizontalPanel horizontalPanel = new HorizontalPanel();
        verticalPanel_1.add(horizontalPanel);
        horizontalPanel.setWidth("100%");
        verticalPanel_1.setCellHorizontalAlignment(horizontalPanel,
                HasHorizontalAlignment.ALIGN_CENTER);

        Button uploadBtn = new Button(NedRes.instance().uploadDlgUpload());
        horizontalPanel.add(uploadBtn);
        uploadBtn.setWidth("90%");
        horizontalPanel.setCellWidth(uploadBtn, "50%");
        horizontalPanel.setCellHorizontalAlignment(uploadBtn,
                HasHorizontalAlignment.ALIGN_CENTER);
        horizontalPanel.setCellVerticalAlignment(uploadBtn,
                HasVerticalAlignment.ALIGN_MIDDLE);
        uploadBtn.addClickHandler(new UploadFileClickHandler());
        verticalPanel.setCellVerticalAlignment(uploadBtn,
                HasVerticalAlignment.ALIGN_MIDDLE);
        verticalPanel.setCellHorizontalAlignment(uploadBtn,
                HasHorizontalAlignment.ALIGN_CENTER);

        closeBtn = new Button(NedRes.instance().close());
        horizontalPanel.add(closeBtn);
        closeBtn.setWidth("90%");
        horizontalPanel.setCellWidth(closeBtn, "50%");
        horizontalPanel.setCellVerticalAlignment(closeBtn,
                HasVerticalAlignment.ALIGN_MIDDLE);
        horizontalPanel.setCellHorizontalAlignment(closeBtn,
                HasHorizontalAlignment.ALIGN_CENTER);
        closeBtn.addClickHandler(new CloseClickHandler());

        setPopupPosition(200, 200);

        uploadForm.addSubmitHandler(new SubmitHandlerUploadFile());

        uploadForm
                .addSubmitCompleteHandler(new SubmitCompleteHandlerUploadFile());

        center();
    }

    private class UploadFileClickHandler implements ClickHandler {
        @Override
        public void onClick(ClickEvent event) {
            filename = fileUpload.getFilename();            
            if (filename.length() == 0) {
                NedAlert.showAlert(NedRes.instance().uploadDlgMsgEmpty());
            } else if (filename.endsWith("wav")) {
                NedAlert.showConfirmYesNo(NedRes.instance().uploadDlgMsgWav(), new ConfirmCallback() {                    
                    @Override
                    public void onYes() {
                        uploadForm.submit();
                    }
                    @Override
                    public void onCancel() {
                    }
                    @Override
                    public void onNo() {
                    }
                    
                });
            } else {
                uploadForm.submit();
            }
        }
    }

    private class CloseClickHandler implements ClickHandler {
        @Override
        public void onClick(ClickEvent event) {
            hide();
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
    
    private void closeWaitDialog(){
        if(uploadAlert != null){
            uploadAlert.hide();
        }
        
    }

    private class SubmitCompleteHandlerUploadFile implements
            SubmitCompleteHandler {

        @Override
        public void onSubmitComplete(SubmitCompleteEvent event) {
            String message = event.getResults();
            closeWaitDialog();
            if (message.contains("Error")) {
                displayError(message);
            } else {
                NedAlert.showAlert(NedRes.instance().uploadSuccess());
                model.getCurrentObject().data = filename;
                model.getCurrentObject().type = message;
                model.broadcastFileUploaded();
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
