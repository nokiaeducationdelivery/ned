package org.ned.server.nedadminconsole.client.widgets;

import java.util.List;

import org.ned.server.nedadminconsole.client.NedCatalogService;
import org.ned.server.nedadminconsole.client.NedCatalogServiceAsync;
import org.ned.server.nedadminconsole.client.NedRes;
import org.ned.server.nedadminconsole.client.callbacks.NedLanguageListCallback;
import org.ned.server.nedadminconsole.client.dialogs.NedAddNewLanguageDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Button;
import org.ned.server.nedadminconsole.shared.NedLanguage;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

public class NedLanguageWidget extends Composite {
    private Grid gridLanguages;
    private List<NedLanguage> listLanguages;
    
    public NedLanguageWidget() {
        
        HorizontalPanel horizontalPanelOuter = new HorizontalPanel();
        horizontalPanelOuter.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        
        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        verticalPanel.setSpacing(10);
        initWidget(horizontalPanelOuter);
        horizontalPanelOuter.setWidth("100%");
        
        horizontalPanelOuter.add(verticalPanel);
        horizontalPanelOuter.setCellHorizontalAlignment(verticalPanel, HasHorizontalAlignment.ALIGN_CENTER);
        horizontalPanelOuter.setCellWidth(verticalPanel, "95%");
        verticalPanel.setSize("60%", "95px");
        
        Label labelUploadedLang = new Label(NedRes.instance().langUploaded());
        verticalPanel.add(labelUploadedLang);

        gridLanguages = new Grid(1, 4);
        gridLanguages.setCellSpacing(13);
        gridLanguages.setStyleName("NedUserManagementGrid");
        gridLanguages.setBorderWidth(1);
        verticalPanel.add(gridLanguages);
        verticalPanel.setCellHorizontalAlignment(gridLanguages, HasHorizontalAlignment.ALIGN_CENTER);
        gridLanguages.setWidth("100%");
        verticalPanel.setCellWidth(gridLanguages, "100%");
        
        Button buttonUploadLang = new Button(NedRes.instance().langAddLanguage());
        buttonUploadLang.addClickHandler(new ClickHandlerUploadLanguage());
        verticalPanel.add(buttonUploadLang);
        buttonUploadLang.setWidth("80");
    }
    
    private class ClickHandlerUploadLanguage implements ClickHandler
    {

        @Override
        public void onClick(ClickEvent event) {
            new NedAddNewLanguageDialog(NedLanguageWidget.this).show();
            
        }
    }
    
    protected void onLoad()
    {
        if(listLanguages == null)
        {
        NedCatalogServiceAsync service = (NedCatalogServiceAsync) GWT
        .create(NedCatalogService.class);
        ServiceDefTarget serviceDef = (ServiceDefTarget) service;
        serviceDef.setServiceEntryPoint("NedCatalogServlet");
        NedLanguageListCallback serviceCallback = new NedLanguageListCallback(this);
        service.getLanguageList(serviceCallback);
        }
    }

    public void reloadList()
    {
        if (listLanguages != null)
        {
            listLanguages.clear();
            listLanguages = null;
            gridLanguages.resize(0, 2);
            onLoad();
        }
    }

    public void loadLanguages(List<NedLanguage> result) {
        this.listLanguages = result;
        if(listLanguages != null && gridLanguages != null)
        {
            int gridRowCount = listLanguages.size() + 1;
            gridLanguages.resize(gridRowCount, 2);
            addLanguagesToList();
        }
        
    }

    private void addLanguagesToList() {
        for(int i = 0; i < listLanguages.size(); i++)
        {
            TextBox textBoxLangName = new TextBox();
            textBoxLangName.setText(listLanguages.get(i).name);
            textBoxLangName.setReadOnly(true);
            gridLanguages.setWidget(i, 0, textBoxLangName);
            gridLanguages.getCellFormatter().setWidth(i, 0, "50%");
            textBoxLangName.setWidth("90%");
            
            TextBox textBoxLocale = new TextBox();
            textBoxLocale.setText(listLanguages.get(i).locale_string);
            textBoxLocale.setReadOnly(true);
            gridLanguages.setWidget(i, 1, textBoxLocale);
            gridLanguages.getCellFormatter().setWidth(i, 1, "50%");
            textBoxLocale.setWidth("85%");

            
            gridLanguages.getCellFormatter().setHorizontalAlignment(i, 1, HasHorizontalAlignment.ALIGN_CENTER);
            gridLanguages.getCellFormatter().setHorizontalAlignment(i, 0, HasHorizontalAlignment.ALIGN_LEFT);
        }
    }

}
