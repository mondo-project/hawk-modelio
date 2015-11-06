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
                                    

package org.modelio.diagram.editor.usecase.elements.usecasedependency;

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
import org.modelio.metamodel.uml.behavior.usecaseModel.UseCaseDependency;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

@objid ("5e7e1147-55b7-11e2-877f-002564c97630")
public class GmUseCaseDependency extends GmLink {
    @objid ("5e7e114d-55b7-11e2-877f-002564c97630")
    private UseCaseDependency theUseCaseDependency;

    @objid ("5e7e1150-55b7-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("5e7e1153-55b7-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    @objid ("7c12ba7c-5eff-11e2-b9cc-001ec947c8cc")
    private static final GmUseCaseDependencyStyleKeys styleKeyProvider = new GmUseCaseDependencyStyleKeys();

    @objid ("5e7e1155-55b7-11e2-877f-002564c97630")
    public GmUseCaseDependency(GmAbstractDiagram diagram, MObject usecasedependency, MRef ref) {
        super(diagram, ref);
        this.theUseCaseDependency = (UseCaseDependency) usecasedependency;
        
        GmDefaultFlatHeader extension = new GmDefaultFlatHeader(diagram, ref);
        extension.setShowLabel(false);
        addExtension(ExtensionLocation.MiddleNW, extension);
        
        GmExtensionPointLabel extensionPointLabel = new GmExtensionPointLabel(diagram, ref);
        addExtension(ExtensionLocation.MiddleSE, extensionPointLabel);
    }

    @objid ("5e7e1161-55b7-11e2-877f-002564c97630")
    public GmUseCaseDependency() {
    }

    @objid ("5e7e1164-55b7-11e2-877f-002564c97630")
    @Override
    public MObject getFromElement() {
        return this.theUseCaseDependency.getOrigin();
    }

    @objid ("5e7e116b-55b7-11e2-877f-002564c97630")
    @Override
    public MObject getRelatedElement() {
        return getRepresentedElement();
    }

    @objid ("5e7e1172-55b7-11e2-877f-002564c97630")
    @Override
    public MObject getRepresentedElement() {
        return this.theUseCaseDependency;
    }

    @objid ("5e7e1179-55b7-11e2-877f-002564c97630")
    @Override
    public StyleKey getStyleKey(MetaKey metakey) {
        return styleKeyProvider.getStyleKey(metakey);
    }

    @objid ("5e7f97d9-55b7-11e2-877f-002564c97630")
    @Override
    public List<StyleKey> getStyleKeys() {
        return styleKeyProvider.getStyleKeys();
    }

    @objid ("5e7f97e2-55b7-11e2-877f-002564c97630")
    @Override
    public MObject getToElement() {
        return this.theUseCaseDependency.getTarget();
    }

    @objid ("5e7f97e9-55b7-11e2-877f-002564c97630")
    @Override
    protected void readLink(IDiagramReader in) {
        super.readLink(in);
        
        this.theUseCaseDependency = (UseCaseDependency) resolveRef(getRepresentedRef());
    }

    @objid ("5e7f97ef-55b7-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmUseCaseDependency." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("5e7f97f5-55b7-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
