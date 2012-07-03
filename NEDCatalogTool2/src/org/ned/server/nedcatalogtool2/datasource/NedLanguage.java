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
package org.ned.server.nedcatalogtool2.datasource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NedLanguage {

    public String id;
    public String name;
    public String locale;
    public String file;

    private NedLanguage( String id, String name, String locale, String file ) {
        this.id = id;
        this.name = name;
        this.locale = locale;
        this.file = file;
    }

    public static List<NedLanguage> GenerateSortedList( ResultSet sqlResults ) throws SQLException {
        List<NedLanguage> temp = new ArrayList<NedLanguage>();
        while ( sqlResults.next() ) {
            temp.add( GetSingleElement( sqlResults ) );
        }
        Collections.sort( temp, new Comparator<NedLanguage>() {

            public int compare( NedLanguage l1, NedLanguage l2 ) {
                return l1.name.compareToIgnoreCase( l2.name );
            }
        } );
        return temp;
    }

    public static NedLanguage GetSingleElement( ResultSet sqlResults ) throws SQLException {
        NedLanguage newObject = new NedLanguage(
                sqlResults.getString( "id" ),
                sqlResults.getString( "name" ),
                sqlResults.getString( "locale_string" ),
                sqlResults.getString( "translation_file" ) );
        return newObject;
    }
}
