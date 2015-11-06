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
                                    

package org.modelio.diagram.editor.bpmn.elements.bpmnusertask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.graphics.Image;
import org.modelio.core.ui.images.ElementImageService;
import org.modelio.diagram.editor.bpmn.editor.BpmnSharedImages;
import org.modelio.diagram.editor.bpmn.elements.bpmnnodefooter.GmBpmnNodeFooter;
import org.modelio.diagram.editor.bpmn.elements.bpmnnodeheader.GmBpmnNodeHeader;
import org.modelio.diagram.editor.bpmn.plugin.DiagramEditorBpmn;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.label.modelelement.GmDefaultFlatHeader;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.GmNoStyleCompositeNode;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.elements.core.node.IImageableNode;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey.RepresentationMode;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.bpmn.activities.BpmnMultiInstanceLoopCharacteristics;
import org.modelio.metamodel.bpmn.activities.BpmnStandardLoopCharacteristics;
import org.modelio.metamodel.bpmn.activities.BpmnUserTask;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * This class represents the Gm of a BusinessRuleTask.
 */
@objid ("61e6fb4f-55b6-11e2-877f-002564c97630")
public class GmBpmnUserTaskPrimaryNode extends GmNoStyleCompositeNode implements IImageableNode {
    /**
     * Current version of this Gm.
     */
    @objid ("61e6fb58-55b6-11e2-877f-002564c97630")
    private final int minorVersion = 1;

    @objid ("61e6fb5b-55b6-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    /**
     * Header
     */
    @objid ("61e6fb55-55b6-11e2-877f-002564c97630")
    private GmBpmnNodeHeader header;

    @objid ("61e6fb57-55b6-11e2-877f-002564c97630")
    private GmBpmnNodeFooter footer;

    /**
     * Default constructor.
     * @param diagram the diagram in which this gm is unmasked.
     * @param relatedRef ref
     */
    @objid ("61e6fb5d-55b6-11e2-877f-002564c97630")
    public GmBpmnUserTaskPrimaryNode(GmAbstractDiagram diagram, MRef relatedRef) {
        super(diagram, relatedRef);
        this.header = new GmBpmnNodeHeader(diagram, relatedRef, false);
        this.footer = new GmBpmnNodeFooter(diagram, relatedRef);
        
        super.addChild(this.header);
        super.addChild(this.footer);
        
        List<Image> images = new ArrayList<>();
        images.add(DiagramEditorBpmn.getImageRegistry().getImage(BpmnSharedImages.USERTASKHEADER));
        this.header.addHeaderImage(images);
    }

    /**
     * Empty constructor, needed for serialisation.
     */
    @objid ("61e6fb66-55b6-11e2-877f-002564c97630")
    public GmBpmnUserTaskPrimaryNode() {
        // empty constructor for the serialization
    }

    @objid ("61e6fb69-55b6-11e2-877f-002564c97630")
    @Override
    public boolean canCreate(Class<? extends MObject> type) {
        return false;
    }

    @objid ("61e6fb71-55b6-11e2-877f-002564c97630")
    @Override
    public boolean canUnmask(MObject el) {
        return false;
    }

    @objid ("61e881da-55b6-11e2-877f-002564c97630")
    @Override
    public GmCompositeNode getCompositeFor(Class<? extends MObject> metaclass) {
        if (canCreate(metaclass))
            return this;
        //else
        return null;
    }

    @objid ("61e881e4-55b6-11e2-877f-002564c97630")
    @Override
    public Image getImage() {
        return ElementImageService.getImage(getRelatedElement());
    }

    @objid ("61e881e9-55b6-11e2-877f-002564c97630")
    @Override
    public RepresentationMode getRepresentationMode() {
        final StyleKey repModeKey = GmBpmnUserTask.STRUCTURED_KEYS.getStyleKey(MetaKey.REPMODE);
        return getStyle().getProperty(repModeKey);
    }

    @objid ("61e881f0-55b6-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmBpmnUserTaskPrimaryNode." + MINOR_VERSION_PROPERTY);
        int readVersion = versionProperty == null ? 0 : ((Integer) versionProperty).intValue();
        switch (readVersion) {
        case 0: {
            read_0(in);
            break;
        }
        case 1: {
            read_1(in);
            break;
        }
        default: {
            assert (false) : "version number not covered!";
            // reading as last handled version: 1
            read_1(in);
            break;
        }
        }
    }

    @objid ("61e881f6-55b6-11e2-877f-002564c97630")
    @Override
    public void refreshFromObModel() {
        super.refreshFromObModel();
        String oldLabel = this.header.getMainLabel();
        this.header.refreshFromObModel();
        firePropertyChange(PROPERTY_LABEL, oldLabel, this.header.getMainLabel());
        
        List<Image> images = new ArrayList<>();
        images.add(DiagramEditorBpmn.getImageRegistry().getImage(BpmnSharedImages.USERTASKHEADER));
        this.header.addHeaderImage(images);
        
        if (this.getRelatedElement() != null) {
            if (this.getRelatedElement().isIsForCompensation()) {
                this.footer.setCompensation(true);
            } else {
                this.footer.setCompensation(false);
            }
        
            if (this.getRelatedElement().getLoopCharacteristics() instanceof BpmnStandardLoopCharacteristics) {
                this.footer.setLoop(true);
            } else {
                this.footer.setLoop(false);
            }
        
            if (this.getRelatedElement().getLoopCharacteristics() instanceof BpmnMultiInstanceLoopCharacteristics) {
                BpmnMultiInstanceLoopCharacteristics loop = (BpmnMultiInstanceLoopCharacteristics) this.getRelatedElement()
                        .getLoopCharacteristics();
                if (loop.isIsSequencial()) {
                    this.footer.setParallel(false);
                    this.footer.setSequential(true);
                } else {
                    this.footer.setParallel(true);
                    this.footer.setSequential(false);
                }
            } else {
                this.footer.setParallel(false);
                this.footer.setSequential(false);
            }
        }
        
        this.footer.refreshFromObModel();
        // forcing visual refresh in case Image changed 
        firePropertyChange(PROPERTY_LAYOUTDATA, null, getLayoutData());
    }

    @objid ("61e881f9-55b6-11e2-877f-002564c97630")
    @Override
    public BpmnUserTask getRelatedElement() {
        return (BpmnUserTask) super.getRelatedElement();
    }

    @objid ("61e88200-55b6-11e2-877f-002564c97630")
    @Override
    public void obElementAdded(MObject movedEl) {
        super.obElementAdded(movedEl);
        refreshFromObModel();
    }

    @objid ("61e88206-55b6-11e2-877f-002564c97630")
    @Override
    public void obElementsUpdated() {
        super.obElementsUpdated();
        refreshFromObModel();
    }

    @objid ("61e88209-55b6-11e2-877f-002564c97630")
    @Override
    public List<GmNodeModel> getVisibleChildren() {
        // Returned result depends on current representation mode:
        List<GmNodeModel> ret;
        switch (this.getRepresentationMode()) {
        case IMAGE: {
            ret = Collections.emptyList();
            break;
        }
        default: {
            ret = super.getVisibleChildren();
            break;
        }
        }
        return ret;
    }

    @objid ("61e88212-55b6-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmBpmnUserTaskPrimaryNode." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("61e88218-55b6-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
        this.header = (GmBpmnNodeHeader) this.getChildren().get(0);
        this.footer = (GmBpmnNodeFooter) this.getChildren().get(1);
        
        GmDefaultFlatHeader imageModeHeader = (GmDefaultFlatHeader) this.getChildren().get(2);
        imageModeHeader.delete();
    }

    @objid ("61ea0879-55b6-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    @objid ("61ea087e-55b6-11e2-877f-002564c97630")
    private void read_1(final IDiagramReader in) {
        super.read(in);
        this.header = (GmBpmnNodeHeader) this.getChildren().get(0);
        this.footer = (GmBpmnNodeFooter) this.getChildren().get(1);
    }

}
