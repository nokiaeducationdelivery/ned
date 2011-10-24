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
