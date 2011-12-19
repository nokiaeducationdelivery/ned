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
package org.ned.server.nedadminconsole.client.interfaces;

import org.ned.server.nedadminconsole.client.NedDataModel;

public interface NedModelListener {
    void libraryChanged(NedDataModel source);

    void libraryContentLoaded(NedDataModel source);

    void objectSelection(NedDataModel source);

    void objectChanged(NedDataModel source);

    void fileUploaded(NedDataModel source);

    void objectDeleted(NedDataModel source, String objectType);
}
