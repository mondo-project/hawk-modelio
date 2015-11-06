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
                                    

package org.modelio.diagram.editor.bpmn.elements.bpmnmessageflow;

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
import org.modelio.metamodel.bpmn.flows.BpmnMessageFlow;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Graphic model for {@link BpmnMessageFlow}.
 */
@objid ("616ce94c-55b6-11e2-877f-002564c97630")
public class GmBpmnMessageFlow extends GmLink {
    @objid ("616ce952-55b6-11e2-877f-002564c97630")
    private BpmnMessageFlow element;

    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("616ce955-55b6-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("616ce958-55b6-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    @objid ("c52fb52e-59a6-11e2-ae45-002564c97630")
    private static final GmBpmnMessageFlowStyleKeys styleKeyProvider = new GmBpmnMessageFlowStyleKeys();

    /**
     * Initialize a control flow graphic model.
     * @param diagram The owning diagram
     * @param element The reference flow, may be null
     * @param ref The referenced flow reference, may not be null
     */
    @objid ("616ce95a-55b6-11e2-877f-002564c97630")
    public GmBpmnMessageFlow(GmAbstractDiagram diagram, MObject element, MRef ref) {
        super(diagram, ref);
        this.element = (BpmnMessageFlow) element;
        
        GmDefaultFlatHeader extension = new GmDefaultFlatHeader(diagram, ref);
        extension.setShowLabel(true);
        addExtension(ExtensionLocation.SourceSE, extension);
    }

    /**
     * For deserialization only.
     */
    @objid ("616ce966-55b6-11e2-877f-002564c97630")
    public GmBpmnMessageFlow() {
        // Nothing to do.
    }

    @objid ("616ce969-55b6-11e2-877f-002564c97630")
    @Override
    public StyleKey getStyleKey(MetaKey metakey) {
        return styleKeyProvider.getStyleKey(metakey);
    }

    @objid ("616ce973-55b6-11e2-877f-002564c97630")
    @Override
    public List<StyleKey> getStyleKeys() {
        return styleKeyProvider.getStyleKeys();
    }

    @objid ("616e6fd9-55b6-11e2-877f-002564c97630")
    @Override
    public MObject getFromElement() {
        return this.element.getSourceRef();
    }

    @objid ("616e6fe0-55b6-11e2-877f-002564c97630")
    @Override
    public MObject getToElement() {
        return this.element.getTargetRef();
    }

    @objid ("616e6fe7-55b6-11e2-877f-002564c97630")
    @Override
    public BpmnMessageFlow getRepresentedElement() {
        return this.element;
    }

    @objid ("616e6fee-55b6-11e2-877f-002564c97630")
    @Override
    public void readLink(IDiagramReader in) {
        super.readLink(in);
        this.element = (BpmnMessageFlow) resolveRef(this.getRepresentedRef());
    }

    @objid ("616e6ff4-55b6-11e2-877f-002564c97630")
    @Override
    public MObject getRelatedElement() {
        return this.element;
    }

    @objid ("616e6ffb-55b6-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmBpmnMessageFlow." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("616e7001-55b6-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
