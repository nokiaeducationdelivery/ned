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
package org.ned.server.nedadminconsole.datasource;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.ned.server.nedadminconsole.shared.NedObject;

public class NedObjectUtils {
	

	
	public static NedObject GenerateSortedList(ResultSet sqlResults, String rootId)
			throws SQLException {
		List<NedObject> temp = new ArrayList<NedObject>();
		while (sqlResults.next()) {
			temp.add(GetSingleElement(sqlResults));
		}
		
		NedObject library = prepereNastedTree(temp, rootId); 
		
		return library;
	}
	
	public static boolean updateIndexes( NedObject library ){
		
		boolean updated  = false;
		NedObject child = null;
		for( int idx = 0; idx < library.childes.size(); idx++ ){
			child = library.childes.get(idx);
			if( updateIndexes( child ) ){
				updated = true;
			}
			if( child.index == 0 ){
				child.index = idx + 1;
				updated = true;
			}
		}
		
		return updated;
	}
	
	private  static NedObject prepereNastedTree( List<NedObject> source, String rootId ){
		NedObject library = null; 
		for(NedObject obj : source){
			if( obj.id.equals(rootId) ){
				library = obj;
				break;
			}
		}
		//update indexes
		//find childs 
		if( library != null ){
			addChildsForObject(library, source);			
		}
		return library;
	}
	
	private static void addChildsForObject(NedObject currentObj, List<NedObject> allObjects ){
		currentObj.childes = new ArrayList<NedObject>();
		for( NedObject obj : allObjects ){
			if( obj.parentId != null && obj.parentId.equals( currentObj.id ) ){
				currentObj.childes.add(obj);
				addChildsForObject(obj, allObjects);
			}
		}
	}


	public static NedObject GetSingleElement(ResultSet sqlResults)
			throws SQLException {
		Object keywordsArray = null;
		Object linksArray = null;
		Array resultArray = null;
		if ((resultArray = sqlResults.getArray("keywords")) != null) {
			keywordsArray = resultArray.getArray();
		}
		if ((resultArray = sqlResults.getArray("links")) != null) {
			linksArray = resultArray.getArray();
		}
		NedObject newObject = new NedObject(sqlResults.getString("elementid"),
				sqlResults.getString("parentid"), 
				sqlResults.getString("name"),
				sqlResults.getString("type"), 
				sqlResults.getString("data"),
				sqlResults.getString("description"),
				sqlResults.getInt("index"),
				keywordsArray,
				linksArray
				);
		return newObject;
	}

}
