/*
 * Copyright 2013 Modeliosoft
 *
 * This file is part of Modelio.
 *
 * Modelio is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Modelio is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Modelio.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */  
                                    

package org.modelio.model.browser.context;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@objid ("005584bc-7a19-1006-9c1d-001ec947cd2a")
public class Loader {
    @objid ("00559380-7a19-1006-9c1d-001ec947cd2a")
    public Loader() {
    }

    @objid ("0055a7a8-7a19-1006-9c1d-001ec947cd2a")
    public void loadXML(URL url) {
        try (InputStream xmlStream = url.openStream()) {
            // Create a DocumentBuilderFactory
            final DocumentBuilderFactory dbf = DocumentBuilderFactory
                    .newInstance();
            dbf.setNamespaceAware(true);
            dbf.setXIncludeAware(false);
            // dbf.setSchema(schema);
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, false);
        
            // Create a DocumentBuilder
            final DocumentBuilder db = dbf.newDocumentBuilder();
        
            // Parse
            // db.setErrorHandler(parserAPIUsage);
            final Document xmlDoc = db.parse(xmlStream);
            final Element rootElement = xmlDoc.getDocumentElement();
            parseMenus(rootElement);
            xmlStream.close();
        } catch (final ParserConfigurationException e) {
            e.printStackTrace();
        } catch (final SAXException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    @objid ("0055bb26-7a19-1006-9c1d-001ec947cd2a")
    private void parseMenus(Element rootElement) {
        // Explore nodes under the root node:
        final NodeList childNodes = rootElement.getChildNodes();
        
        final int nNodes = childNodes.getLength();
        for (int i = 0; i < nNodes; i++) {
            final Node node = childNodes.item(i);
            if (node.getNodeName().equals("popup")) {
                parsePopupNode(node);
            }
        }
    }

    @objid ("0055cec2-7a19-1006-9c1d-001ec947cd2a")
    private void parsePopupNode(Node popupNode) {
        // Get the metaclass name
        final NamedNodeMap attributes = popupNode.getAttributes();
        String metaclassName = "";
        String stereotypeName = "";
        
        // Get source metaclass and stereotype
        if (attributes != null) {
            final Node metaclassNode = attributes.getNamedItem("metaclass");
            if (metaclassNode != null) {
                metaclassName = metaclassNode.getTextContent();
            }
            final Node stereotypeNode = attributes.getNamedItem("stereotype");
            if (stereotypeNode != null) {
                stereotypeName = stereotypeNode.getTextContent();
            }
        }
        
        // Explore items and menus
        final NodeList menuNodes = popupNode.getChildNodes();
        final int nNodes = menuNodes.getLength();
        for (int i = 0; i < nNodes; i++) {
            final Node node = menuNodes.item(i);
        
            if (node.getNodeName().equals("command")) {
                EntryDescriptor entryDescriptor = parseCommandNode(node);
                entryDescriptor.sourceMetaclass = metaclassName;
                entryDescriptor.sourceStereotype = stereotypeName;
        
                ElementCreationDynamicMenuManager.register(entryDescriptor);
            } else if (node.getNodeName().equals("separator")) {
                ElementCreationDynamicMenuManager.registerSeparator(metaclassName);
            }
        }
    }

    @objid ("0055e2ae-7a19-1006-9c1d-001ec947cd2a")
    private static EntryDescriptor parseCommandNode(Node itemNode) {
        final EntryDescriptor entryDescriptor = new EntryDescriptor();
        
        final NamedNodeMap attributes = itemNode.getAttributes();
        String id = "";
        
        // get command id
        if (attributes != null) {
            final Node nameNode = attributes.getNamedItem("id");
            if (nameNode != null) {
        
                id = nameNode.getTextContent();
            }
        }
        entryDescriptor.commandId = id;
        
        // parse command parameters
        final NodeList childNodes = itemNode.getChildNodes();
        final int nNodes = childNodes.getLength();
        for (int i = 0; i < nNodes; i++) {
            final Node node = childNodes.item(i);
            if (node.getNodeName().equals("parameter")) {
                parseParameterNode(node, entryDescriptor);
            }
        }
        return entryDescriptor;
    }

    @objid ("00560388-7a19-1006-9c1d-001ec947cd2a")
    private static void parseParameterNode(Node parameterNode, EntryDescriptor entryDescriptor) {
        final NamedNodeMap attributes = parameterNode.getAttributes();
        String name = "";
        String value = "";
        
        
        if (attributes != null) {
            final Node nameNode = attributes.getNamedItem("name");
            if (nameNode != null) {
                name = nameNode.getTextContent();
            }
            final Node valueNode = attributes.getNamedItem("value");
            if (valueNode != null) {
                value = valueNode.getTextContent();
            }
            entryDescriptor.parameters.put(name, value);
        }
    }

}
