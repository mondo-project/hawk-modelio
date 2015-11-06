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
                                    

package org.modelio.diagram.editor.bpmn.elements.bpmnlane.header;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.modelio.core.ui.images.ElementImageService;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.header.GmDefaultModelElementHeader;
import org.modelio.diagram.elements.editpartFactory.ModelioEditPartFactory;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.ProxyStyle;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLane;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * <p>
 * Specialisation of the default model element header: uses a "special" style that returns a darker shade of the
 * partition background colour.
 * </p>
 * <p>
 * Also this class is needed so that the {@link ModelioEditPartFactory} instantiate the correct EditPart to have the
 * correct selection behaviours (very specific to partition: only header is "click-able" but clicking on it selects the
 * whole partition).
 * </p>
 */
@objid ("6129c5d4-55b6-11e2-877f-002564c97630")
public class GmBpmnLaneHeader extends GmDefaultModelElementHeader {
    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("6129c5d8-55b6-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("6129c5db-55b6-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    /**
     * Returns a specialization of the standard {@link ProxyStyle} that returns a darker shade for the background color.
     */
    @objid ("6129c5dd-55b6-11e2-877f-002564c97630")
    @Override
    protected IStyle createStyle(GmAbstractDiagram gmAbstractDiagram) {
        return new GmPartitionHeaderStyle(gmAbstractDiagram.getStyle());
    }

    /**
     * C'tor without args for deserialization.
     */
    @objid ("6129c5e8-55b6-11e2-877f-002564c97630")
    public GmBpmnLaneHeader() {
        // Nothing to do.
    }

    /**
     * C'tor.
     * @param diagram the owning diagram.
     */
    @objid ("6129c5eb-55b6-11e2-877f-002564c97630")
    public GmBpmnLaneHeader(GmAbstractDiagram diagram, MRef relatedRef) {
        super(diagram, relatedRef);
    }

    @objid ("612b4c5f-55b6-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmBpmnLaneHeader." + MINOR_VERSION_PROPERTY);
        int readVersion = versionProperty == null ? 0 : ((Integer) versionProperty).intValue();
        switch (readVersion) {
        case 0: {
            read_0(in);
            break;
        }
        default: {
            assert (false) : "version number not covered!";
            // reading as last handled version: 0
            read_0(in);
            break;
        }
        }
    }

    @objid ("612b4c65-55b6-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmBpmnLaneHeader." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("612b4c6b-55b6-11e2-877f-002564c97630")
    @Override
    public void refreshFromObModel() {
        BpmnLane partition = (BpmnLane) getRelatedElement();
        if (partition != null && partition.isValid()) {
            setShowMetaclassIcon(partition.getPartitionElement() != null);
        } else {
            setShowMetaclassIcon(false);
        }
        
        super.refreshFromObModel();
    }

    @objid ("612b4c6e-55b6-11e2-877f-002564c97630")
    @Override
    protected Image getMetaclassIcon() {
        BpmnLane partition = (BpmnLane) getRelatedElement();
        ModelElement represented = partition.getPartitionElement();
        if (represented != null) {
            return ElementImageService.getIcon(represented);
        } else {
            return ElementImageService.getIcon(partition);
        }
    }

    @objid ("612b4c72-55b6-11e2-877f-002564c97630")
    @Override
    protected String computeMainLabel() {
        BpmnLane partition = (BpmnLane) getRelatedElement();
        return LaneSymbolProvider.computeSimpleLabel(getDiagram().getModelManager().getModelServices().getElementNamer(), partition);
    }

    @objid ("612b4c76-55b6-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
    }

    @objid ("612b4c7b-55b6-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    /**
     * Specialization of {@link ProxyStyle} that returns a darker shade for the background color. </p>
     * <p>
     * This class <strong>MUST</strong> be static or deserialization will fail.
     * </p>
     */
    @objid ("612b4c80-55b6-11e2-877f-002564c97630")
    public static class GmPartitionHeaderStyle extends ProxyStyle {
        /**
         * Returns a darker shade for the background color.
         */
        @objid ("612b4c85-55b6-11e2-877f-002564c97630")
        @Override
        public Color getColor(StyleKey propertyKey) {
            return super.getColor(propertyKey);
        }

        /**
         * Empty c'tor needed for deserialization.
         */
        @objid ("612b4c8e-55b6-11e2-877f-002564c97630")
        public GmPartitionHeaderStyle() {
            super();
        }

        /**
         * C'tor.
         * @param cascadedStyle the style this style should cascade on.
         */
        @objid ("612b4c91-55b6-11e2-877f-002564c97630")
        public GmPartitionHeaderStyle(IStyle cascadedStyle) {
            super(cascadedStyle);
        }

    }

}
