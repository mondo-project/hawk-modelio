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
                                    

package org.modelio.diagram.editor.state.elements.transition;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.link.ExtensionLocation;
import org.modelio.diagram.elements.core.link.GmLink;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.uml.behavior.stateMachineModel.Transition;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Graphic model for {@link Transition}.
 * 
 * @author sbe
 */
@objid ("f5a89a9a-55b6-11e2-877f-002564c97630")
public class GmTransition extends GmLink {
    @objid ("f5aa213b-55b6-11e2-877f-002564c97630")
    private Transition element;

    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("f5aa2140-55b6-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("f5aa2143-55b6-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    @objid ("fe7a7c21-5a5b-11e2-9e33-00137282c51b")
    private static final GmTransitionStyleKeys styleKeyProvider = new GmTransitionStyleKeys();

    /**
     * Initialize a control flow graphic model.
     * @param diagram The owning diagram
     * @param transition The reference flow, may be null
     * @param ref The referenced flow reference, may not be null
     */
    @objid ("f5aa2145-55b6-11e2-877f-002564c97630")
    public GmTransition(GmAbstractDiagram diagram, MObject transition, MRef ref) {
        super(diagram, ref);
        
        this.element = (Transition) transition;
        
        addExtension(ExtensionLocation.MiddleNW, new GmTransitionMainLabel(diagram, ref));
        addExtension(ExtensionLocation.SourceSE, new GmTransitionGuardLabel(diagram, ref));
        addExtension(ExtensionLocation.TargetSE, new GmTransitionPostConditionLabel(diagram, ref));
    }

    /**
     * For deserialization only.
     */
    @objid ("f5aa2151-55b6-11e2-877f-002564c97630")
    public GmTransition() {
        // Nothing to do.
    }

    @objid ("f5aa2154-55b6-11e2-877f-002564c97630")
    @Override
    public MObject getFromElement() {
        return this.element.getSource();
    }

    @objid ("f5aa215b-55b6-11e2-877f-002564c97630")
    @Override
    public MObject getRelatedElement() {
        return getRepresentedElement();
    }

    @objid ("f5aa2162-55b6-11e2-877f-002564c97630")
    @Override
    public MObject getRepresentedElement() {
        return this.element;
    }

    @objid ("f5aa2169-55b6-11e2-877f-002564c97630")
    @Override
    public StyleKey getStyleKey(MetaKey metakey) {
        return styleKeyProvider.getStyleKey(metakey);
    }

    @objid ("f5aa2173-55b6-11e2-877f-002564c97630")
    @Override
    public List<StyleKey> getStyleKeys() {
        return styleKeyProvider.getStyleKeys();
    }

    @objid ("f5aba7da-55b6-11e2-877f-002564c97630")
    @Override
    public MObject getToElement() {
        return this.element.getTarget();
    }

    @objid ("f5aba7e1-55b6-11e2-877f-002564c97630")
    @Override
    protected void readLink(IDiagramReader in) {
        super.readLink(in);
        this.element = (Transition) resolveRef(this.getRepresentedRef());
    }

    @objid ("f5aba7e7-55b6-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmTransition." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("f5aba7ed-55b6-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
