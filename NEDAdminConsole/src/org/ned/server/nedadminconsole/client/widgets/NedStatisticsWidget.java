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
package org.ned.server.nedadminconsole.client.widgets;

import org.ned.server.nedadminconsole.client.NedRes;
import org.ned.server.nedadminconsole.client.dialogs.NedAlert;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;
import com.google.gwt.i18n.client.DateTimeFormat;

public class NedStatisticsWidget extends Composite {
    
    private Button buttonDownload;
    private RadioButton rdbtnAllStatistics;
    private RadioButton rdbtnFilterDate;
    private DateBox dateBoxFrom;
    private DateBox dateBoxTo;

    public NedStatisticsWidget() {
        
        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setSpacing(15);
        verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        initWidget(verticalPanel);
        verticalPanel.setSize("100%", "50px");
        
        buttonDownload = new Button();
        buttonDownload.setText(NedRes.instance().statisticsDownload());
        
        Grid grid = new Grid(4, 1);
        verticalPanel.add(grid);
        
        Label lblChooseMethod = new Label();
        lblChooseMethod.setText(NedRes.instance().statisticsChooseMethod());
        grid.setWidget(0, 0, lblChooseMethod);
        
        rdbtnAllStatistics = new RadioButton("statFilter");
        rdbtnAllStatistics.setText(NedRes.instance().downloadAllStats());
        grid.setWidget(1, 0, rdbtnAllStatistics);
        
        rdbtnFilterDate = new RadioButton("statFilter");
        rdbtnFilterDate.setText(NedRes.instance().downloadSelectDate());
        grid.setWidget(2, 0, rdbtnFilterDate);
        
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        grid.setWidget(3, 0, horizontalPanel);
        
        Label lblFrom = new Label();
        lblFrom.setText(NedRes.instance().statisticsFrom());
        horizontalPanel.add(lblFrom);
        
        dateBoxFrom = new DateBox();
        dateBoxFrom.setFormat(new DefaultFormat(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_MEDIUM)));
        horizontalPanel.add(dateBoxFrom);
        
        Label lblTo = new Label();
        lblTo.setText(NedRes.instance().statisticsTo());
        horizontalPanel.add(lblTo);
        
        dateBoxTo = new DateBox();
        dateBoxTo.setFormat(new DefaultFormat(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_MEDIUM)));
        horizontalPanel.add(dateBoxTo);
        verticalPanel.add(buttonDownload);
        buttonDownload.setSize("30%", "");
        
        buttonDownload.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                String queryString = "NedStatisticsFileServlet";
                if(rdbtnFilterDate.getValue().booleanValue())
                {
                    if(dateBoxFrom.getValue() == null || dateBoxTo.getValue() == null)
                    {
                        NedAlert.showAlert(NedRes.instance().statisticsWrongDate());
                        return;
                    }
                    queryString = queryString +  "?from=" + String.valueOf(dateBoxFrom.getValue().getTime()) + "&to=" + String.valueOf(dateBoxTo.getValue().getTime());
                }
                com.google.gwt.user.client.Window.Location.assign(GWT.getHostPageBaseURL() + queryString);
            }
        });
    }

}
