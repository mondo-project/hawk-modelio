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
                                    

package org.modelio.diagram.elements.umlcommon.usage;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.label.modelelement.GmDefaultFlatHeader;
import org.modelio.diagram.elements.core.link.ExtensionLocation;
import org.modelio.diagram.elements.core.link.GmLink;
import org.modelio.diagram.elements.umlcommon.dependency.GmDependencyStyleKeys;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.uml.infrastructure.Usage;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Graphic model for {@link Usage}.
 * 
 * @author sbe
 */
@objid ("412c176f-a7f6-418a-889c-2bda2ab4bd10")
public class GmUsage extends GmLink {
    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("efc31afc-6457-420f-ae1b-7a5090d59bee")
    private final int minorVersion = 0;

    @objid ("18efb7cf-8daa-4769-88a7-1dbd6be35e59")
    private static final int MAJOR_VERSION = 0;

    @objid ("9262f896-e8b8-4b0b-8aa4-52e9b4c474a3")
    private Usage dependency;

    /**
     * Usage style keys.
     */
    @objid ("3b238851-5ed9-4e2c-a24e-8929b6313ed5")
    public static final GmDependencyStyleKeys styleKeyProvider = new GmDependencyStyleKeys("USAGE");

    /**
     * Initialize a control flow graphic model.
     * @param diagram The owning diagram
     * @param dependency The reference flow, may be null
     * @param ref The referenced flow reference, may not be null
     */
    @objid ("d723cdcd-97ca-45c4-b950-b5b2cf2e5442")
    public GmUsage(GmAbstractDiagram diagram, Usage dependency, MRef ref) {
        super(diagram, ref);
        this.dependency = dependency;
        
        final GmDefaultFlatHeader header = new GmUsageHeader(diagram, ref);
        addExtension(ExtensionLocation.MiddleNW, header);
    }

    @objid ("df7b8940-e6e8-4bc7-826c-814ba8745b44")
    @Override
    public StyleKey getStyleKey(MetaKey metakey) {
        return styleKeyProvider.getStyleKey(metakey);
    }

    @objid ("e455a62e-4875-4cfc-8b53-a6ae2af4c3c1")
    @Override
    public List<StyleKey> getStyleKeys() {
        return styleKeyProvider.getStyleKeys();
    }

    /**
     * For deserialization only.
     */
    @objid ("4c2c34af-0164-4c40-9384-58ca5471fc3b")
    public GmUsage() {
    }

    @objid ("a161ad01-50ff-48de-83c0-48bfaa560158")
    @Override
    protected void readLink(IDiagramReader in) {
        super.readLink(in);
        this.dependency = (Usage) resolveRef(this.getRepresentedRef());
    }

    @objid ("57c41311-31fe-4cd8-afb0-b34b115ccb83")
    @Override
    public MObject getFromElement() {
        return this.dependency.getImpacted();
    }

    @objid ("4a6967e0-fba2-4936-9782-7e88960c7066")
    @Override
    public MObject getToElement() {
        return this.dependency.getDependsOn();
    }

    @objid ("2225e793-8737-4273-b161-a6fa99d0a851")
    @Override
    public MObject getRepresentedElement() {
        return this.dependency;
    }

    @objid ("0a654229-841f-4182-823a-55016f099514")
    @Override
    public MObject getRelatedElement() {
        return getRepresentedElement();
    }

    @objid ("40bfbf68-6b4e-4ccd-af52-9746eaa9c982")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmUsage." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("87f7a6d4-342e-4f74-be0e-dee00802c6dc")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
