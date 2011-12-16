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

import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.sax.*;

public class ContentXmlGenerator {

    public void printContentXml(String contentId, PrintWriter writer) throws TransformerConfigurationException, SAXException {
        StreamResult streamResult = new StreamResult(writer);
        SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
        TransformerHandler hd = tf.newTransformerHandler();
        Transformer serializer = hd.getTransformer();

        serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        serializer.setOutputProperty(OutputKeys.STANDALONE, "no");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        hd.setResult(streamResult);

        hd.startDocument();

        PostgresConnection connection = new PostgresConnection();
        List<NedObject> fullList = null;
        try {
            fullList = connection.GetFullNode(contentId);
        } catch (Exception ex) {
            Logger.getLogger(ContentXmlGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            connection.disconnect();
        }
        if (fullList != null && !fullList.isEmpty()) {
            writeNedContent(hd, fullList, 0, writer);
        }

        hd.endDocument();
    }

    private int writeNedContent(TransformerHandler handler, List<NedObject> fullList, int currentIndex, PrintWriter debug) throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        NedObject current = fullList.get(currentIndex);

        int childCount = 0;
        for (int i = currentIndex + 1; i
                < fullList.size(); i++) {
            if (fullList.get(i).IsChildOf(current)) {
                childCount++;
            }
        }

        atts.addAttribute("", "", StringRepository.ATTRIBUTE_TYPE, "", current.type);
        atts.addAttribute("", "", StringRepository.ATTRIBUTE_ID, "", current.id);

        if (current.parentId != null) {
            atts.addAttribute("", "", StringRepository.ATTRIBUTE_PARENT, "", current.parentId);
        }
        if (current.data != null) {
            atts.addAttribute("", "", StringRepository.ATTRIBUTE_DATA, "", current.data);
        }
        handler.startElement("", "", StringRepository.TAG_NODE, atts);
        atts.clear();

        //print attributes here
        PrintElementData(handler, current);

        //print childs
        handler.startElement("", "", StringRepository.TAG_CHILDS, atts);
        for (int i = 0; i
                < childCount; i++) {
            currentIndex++;//recursive call can increase current index by more than 1 so need to update it after function returns
            currentIndex = writeNedContent(handler, fullList, currentIndex, debug);
        }
        handler.endElement("", "", StringRepository.TAG_CHILDS);

        handler.endElement("", "", StringRepository.TAG_NODE);
        return currentIndex;
    }

    private void PrintElementData(TransformerHandler handler, NedObject current) throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        handler.startElement("", "", StringRepository.TAG_TITLE, atts);
        handler.characters(current.name.toCharArray(), 0, current.name.length());
        handler.endElement("", "", StringRepository.TAG_TITLE);
        if (current.description != null && !current.description.isEmpty()) {
            handler.startElement("", "", StringRepository.TAG_DESCRIPTION, atts);
            handler.characters(current.description.toCharArray(), 0, current.description.length());
            handler.endElement("", "", StringRepository.TAG_DESCRIPTION);
        }
        if (current.keywords != null) {
            String[] keywordList = (String[]) current.keywords;
            for (int i = 0; i < keywordList.length; i++) {
                handler.startElement("", "", StringRepository.TAG_KEYWORDS, atts);
                handler.characters(keywordList[i].toCharArray(), 0, keywordList[i].length());
                handler.endElement("", "", StringRepository.TAG_KEYWORDS);
            }
        }
        if (current.externalLinks != null) {
            String[] linksList = (String[]) current.externalLinks;
            for (int i = 0; i < linksList.length; i++) {
                handler.startElement("", "", StringRepository.TAG_EXTERNAL_LINKS, atts);
                handler.characters(linksList[i].toCharArray(), 0, linksList[i].length());
                handler.endElement("", "", StringRepository.TAG_EXTERNAL_LINKS);
            }
        }
    }
}
