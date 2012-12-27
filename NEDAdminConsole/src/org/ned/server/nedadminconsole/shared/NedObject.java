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
package org.ned.server.nedadminconsole.shared;

import java.io.Serializable;
import java.util.ArrayList;

public class NedObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6423723956840523605L;
	public String id;
    public String parentId;
    public String name;
    public String type;
    public String data;
    public String description;
    public int index;
    public String[] keywords;
    public String[] externalLinks;
    
    public ArrayList<NedObject> childes;
 
    public NedObject()
    {
    }
    
    public NedObject(String id, String parentId, String name, String type, String data, String description, int index, Object keywords, Object externalLinks)  {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.type = type;
        this.data = data;
        this.description = description;
        this.index = index;
        this.childes = new ArrayList<NedObject>();
        
        if (keywords != null) {
            this.keywords = (String[])keywords;
        }
        if (externalLinks != null) {
            this.externalLinks = (String[])externalLinks;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }

        //this used in list returns object childs
        return id.equals(((NedObject) obj).parentId);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 97 * hash + (this.parentId != null ? this.parentId.hashCode() : 0);
        hash = 97 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 97 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = 97 * hash + (this.data != null ? this.data.hashCode() : 0);
        return hash;
    }

    public boolean IsChildOf(NedObject other) {
        return other.equals(this);
    }


}
