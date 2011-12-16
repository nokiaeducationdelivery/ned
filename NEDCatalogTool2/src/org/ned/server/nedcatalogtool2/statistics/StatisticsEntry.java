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
package org.ned.server.nedcatalogtool2.statistics;

public class StatisticsEntry {

    public String mEventType;
    public String mDetails;
    public String mTime;

    public StatisticsEntry( String mEventType, String mDetails, String mTime) {
        this.mEventType = mEventType;
        this.mDetails = mDetails;
        this.mTime = mTime;
    }
}

