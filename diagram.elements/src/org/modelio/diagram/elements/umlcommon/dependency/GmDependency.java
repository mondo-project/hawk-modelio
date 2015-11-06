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
                                    

package org.modelio.diagram.elements.umlcommon.dependency;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.label.modelelement.GmDefaultFlatHeader;
import org.modelio.diagram.elements.core.link.ExtensionLocation;
import org.modelio.diagram.elements.core.link.GmLink;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Graphic model for {@link Dependency}.
 * 
 * @author sbe
 */
@objid ("81249eb1-1dec-11e2-8cad-001ec947c8cc")
public class GmDependency extends GmLink {
    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("81249eb6-1dec-11e2-8cad-001ec947c8cc")
    private final int minorVersion = 0;

    @objid ("81249eb9-1dec-11e2-8cad-001ec947c8cc")
    private static final int MAJOR_VERSION = 1;

    @objid ("81249eb3-1dec-11e2-8cad-001ec947c8cc")
    private Dependency dependency;

    /**
     * Style keys for {@link GmDependency}.
     */
    @objid ("81249eb4-1dec-11e2-8cad-001ec947c8cc")
    public static final GmDependencyStyleKeys styleKeyProvider = new GmDependencyStyleKeys("DEPENDENCY");

    /**
     * Initialize a control flow graphic model.
     * @param diagram The owning diagram
     * @param dependency The reference flow, may be null
     * @param ref The referenced flow reference, may not be null
     */
    @objid ("81249ebb-1dec-11e2-8cad-001ec947c8cc")
    public GmDependency(GmAbstractDiagram diagram, Dependency dependency, MRef ref) {
        super(diagram, ref);
        this.dependency = dependency;
        
        addExtension(ExtensionLocation.MiddleNW, new GmDefaultFlatHeader(diagram, ref));
    }

    @objid ("81270101-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public StyleKey getStyleKey(MetaKey metakey) {
        return styleKeyProvider.getStyleKey(metakey);
    }

    @objid ("81270107-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public List<StyleKey> getStyleKeys() {
        return styleKeyProvider.getStyleKeys();
    }

    /**
     * For deserialization only.
     */
    @objid ("8127010e-1dec-11e2-8cad-001ec947c8cc")
    public GmDependency() {
    }

    @objid ("81270111-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void readLink(IDiagramReader in) {
        super.readLink(in);
        this.dependency = (Dependency) resolveRef(this.getRepresentedRef());
    }

    @objid ("81270115-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public MObject getFromElement() {
        return this.dependency.getImpacted();
    }

    @objid ("8127011a-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public MObject getToElement() {
        return this.dependency.getDependsOn();
    }

    @objid ("8127011f-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public MObject getRepresentedElement() {
        return this.dependency;
    }

    @objid ("81270124-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public MObject getRelatedElement() {
        return getRepresentedElement();
    }

    @objid ("81270129-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmDependency." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("8127012d-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
