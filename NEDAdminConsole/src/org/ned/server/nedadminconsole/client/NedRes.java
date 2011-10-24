package org.ned.server.nedadminconsole.client;

import com.google.gwt.core.client.GWT;

public class NedRes {
    private static NedResource resource = null;
    
    public static NedResource instance(){
        if(resource == null){
            resource = (NedResource)GWT.create(NedResource.class);
        }
        return resource;
    }
}
