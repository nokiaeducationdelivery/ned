package org.ned.server.nedadminconsole.client.callbacks;

import org.ned.server.nedadminconsole.client.widgets.NedMotdWidget;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class NedSelectMotdCallback implements AsyncCallback<String>{

    private NedMotdWidget widgetMotd;

    public NedSelectMotdCallback(NedMotdWidget widgetMotd) {
        this.widgetMotd = widgetMotd;
    }

    @Override
    public void onFailure(Throwable caught) {
    }
    
    @Override
    public void onSuccess(String result) {
        widgetMotd.loadOldMessage(result);
        
    }

}
