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
