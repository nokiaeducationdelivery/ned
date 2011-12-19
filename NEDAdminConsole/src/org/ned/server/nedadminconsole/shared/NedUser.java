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
package org.ned.server.nedadminconsole.shared;

import java.io.Serializable;

public class NedUser implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = 6666794405839971043L;
    public static final int DATABASE_PRESENT = 0x01;
    public static final int CHANGED = 0x02;
    public static final int DELETE = 0x04;
    public String username;
    public String password;
    public int flags;
    
    public NedUser(){
    }
    
    public NedUser(String username, String password, int flags) {
        this.flags = flags;
        this.username = username;
        this.password = password;
    }
}
