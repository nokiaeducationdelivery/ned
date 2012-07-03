/*******************************************************************************
* Copyright (c) 2012 Nokia Corporation
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* Comarch team - initial API and implementation
*******************************************************************************/
package org.ned.server.nedadminconsole.shared;

import java.io.Serializable;

public class NedLanguage implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -2006499114898693646L;

    public String name;
    public String locale_string;
    public String translation_file;

    public NedLanguage()
    {
        
    }
    
    public NedLanguage(String name, String locale_string, String translation_file)
    {
        this.name = name;
        this.locale_string = locale_string;
        this.translation_file = translation_file;
    }

}
