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
package org.ned.server.nedadminconsole.datasource;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.ned.server.nedadminconsole.shared.NedObject;

public class NedObjectUtils {
	public static List<NedObject> GenerateSortedList(ResultSet sqlResults)
			throws SQLException {
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
				sqlResults.getString("parentid"), sqlResults.getString("name"),
				sqlResults.getString("type"), sqlResults.getString("data"),
				sqlResults.getString("description"), keywordsArray, linksArray);
		return newObject;
	}

}
