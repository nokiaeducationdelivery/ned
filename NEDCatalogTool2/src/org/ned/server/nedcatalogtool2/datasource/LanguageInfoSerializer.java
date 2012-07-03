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

import java.io.PrintWriter;
import java.util.List;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXException;

public class LanguageInfoSerializer {

    public void printXml( List<NedLanguage> list, PrintWriter writer ) throws TransformerConfigurationException, SAXException {
        StreamResult streamResult = new StreamResult( writer );
        SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
        TransformerHandler hd = tf.newTransformerHandler();
        Transformer serializer = hd.getTransformer();

        serializer.setOutputProperty( OutputKeys.ENCODING, "UTF-8" );
        serializer.setOutputProperty( OutputKeys.STANDALONE, "yes" );
        serializer.setOutputProperty( OutputKeys.INDENT, "yes" );
        hd.setResult( streamResult );

        hd.startDocument();

        if ( list != null && !list.isEmpty() ) {
            write( hd, list );
        }
        hd.endDocument();
    }

    private void write( TransformerHandler handler, List<NedLanguage> list ) throws SAXException {
        handler.startElement( "", "", StringRepository.TAG_LANGUAGES, null );
        for ( int i = 0; i < list.size(); i++ ) {
            writeElement( handler, list.get( i ) );
        }
        handler.endElement( "", "", StringRepository.TAG_LANGUAGES );
    }

    private void writeElement( TransformerHandler handler, NedLanguage current ) throws SAXException {
        if ( current.id == null || current.id.isEmpty()
             || current.file == null || current.file.isEmpty()
             || current.locale == null || current.locale.isEmpty() ) {
            return;
        }

        handler.startElement( "", "", StringRepository.TAG_LANGUAGE, null );

        handler.startElement( "", "", StringRepository.TAG_ID, null );
        handler.characters( current.id.toCharArray(), 0, current.id.length() );
        handler.endElement( "", "", StringRepository.TAG_ID );

        handler.startElement( "", "", StringRepository.TAG_LOCALE, null );
        handler.characters( current.locale.toCharArray(), 0, current.locale.length() );
        handler.endElement( "", "", StringRepository.TAG_LOCALE );

        handler.startElement( "", "", StringRepository.TAG_FILENAME, null );
        handler.characters( current.file.toCharArray(), 0, current.file.length() );
        handler.endElement( "", "", StringRepository.TAG_FILENAME );

        if ( current.name != null && !current.name.isEmpty() ) {
            handler.startElement( "", "", StringRepository.TAG_NAME, null );
            handler.characters( current.name.toCharArray(), 0, current.name.length() );
            handler.endElement( "", "", StringRepository.TAG_NAME );
        }

        handler.endElement( "", "", StringRepository.TAG_LANGUAGE );
    }
}
