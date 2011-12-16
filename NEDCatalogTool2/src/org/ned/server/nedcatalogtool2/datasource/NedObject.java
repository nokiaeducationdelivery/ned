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
package org.ned.server.nedcatalogtool2.datasource;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NedObject {

    public String id;
    public String parentId;
    public String name;
    public String type;
    public String data;
    public String description;
    public Object keywords;
    public Object externalLinks;
    public String version;

    private NedObject(String id, String parentId, String name, String type, String data, String description, Array keywords, Array externalLinks, String version) throws SQLException {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.type = type;
        this.data = data;
        this.description = description;
        if (keywords != null) {
            this.keywords = keywords.getArray();
        }
        if (externalLinks != null) {
            this.externalLinks = externalLinks.getArray();
        }
        this.version = version;
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

    public static List<NedObject> GenerateSortedList(ResultSet sqlResults) throws SQLException {
        List<NedObject> temp = new ArrayList<NedObject>();
        while (sqlResults.next()) {
            temp.add(GetSingleElement(sqlResults));
        }
        return PerformTreeSorting(temp);
    }

    private static List<NedObject> PerformTreeSorting(List<NedObject> source) {
        List<NedObject> retval = new ArrayList<NedObject>();
        int iterator = -1;
        while (!source.isEmpty()) {
            int childPosition = -1;
            if (iterator < 0) {
                retval.add(source.remove(0));
                iterator++;
            } else {
                if ((childPosition = source.indexOf(retval.get(iterator))) >= 0) {
                    retval.add(source.remove(childPosition));
                    iterator = retval.size() - 1;
                }
                if (childPosition == -1) {
                    iterator--;
                }
            }
        }
        return retval;
    }

    public static NedObject GetSingleElement(ResultSet sqlResults) throws SQLException {
        NedObject newObject = new NedObject(
                sqlResults.getString("elementid"),
                sqlResults.getString("parentid"),
                sqlResults.getString("name"),
                sqlResults.getString("type"),
                sqlResults.getString("data"),
                sqlResults.getString("description"),
                sqlResults.getArray("keywords"),
                sqlResults.getArray("links"),
                sqlResults.getString("version"));
        return newObject;
    }
}
