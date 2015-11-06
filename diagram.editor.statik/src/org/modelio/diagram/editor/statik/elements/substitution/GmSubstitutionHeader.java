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
                                    

package org.modelio.diagram.editor.statik.elements.substitution;

import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.header.GmDefaultModelElementHeader;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * MObject import header displayed on the node link.
 * 
 * @author cmarin
 */
@objid ("0c4d1283-443b-475e-945f-a1bf59e82e16")
public class GmSubstitutionHeader extends GmDefaultModelElementHeader {
    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("2280bd65-0038-43a6-8be9-eced39f19001")
    private final int minorVersion = 0;

    @objid ("81f89ca9-4f5e-4b18-be70-33ff4188e536")
    private static final int MAJOR_VERSION = 0;

    /**
     * Constructor.
     * @param diagram the diagram
     * @param relatedRef a reference to the element this GmModel is related to, must not be null.
     */
    @objid ("e9c77344-97a7-4ede-9548-6dda5d407bd4")
    public GmSubstitutionHeader(GmAbstractDiagram diagram, MRef relatedRef) {
        super(diagram, relatedRef);
    }

    /**
     * For deserialization only.
     */
    @objid ("49ace891-9dca-4580-9b52-a3836522241c")
    public GmSubstitutionHeader() {
    }

    @objid ("671be1dd-a584-42ac-9807-5619fd508da0")
    @Override
    public List<Stereotype> filterStereotypes(List<Stereotype> stereotypes) {
        return stereotypes;
    }

    @objid ("062614ad-7656-4e54-b537-28ff8b18227e")
    @Override
    public List<StyleKey> getStyleKeys() {
        return Collections.emptyList();
    }

    @objid ("aaca4d01-f82f-41cb-a730-37e05ea9c1f3")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    @objid ("5f951c63-843c-40ee-acbb-5e4a02abe369")
    @Override
    protected String getMetaclassKeyword() {
        return "substitute";
    }

}
