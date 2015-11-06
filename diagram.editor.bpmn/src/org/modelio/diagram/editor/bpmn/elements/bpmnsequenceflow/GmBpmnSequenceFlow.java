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
                                    

package org.modelio.diagram.editor.bpmn.elements.bpmnsequenceflow;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.bpmn.elements.bpmndataobject.dataobject.GmBpmnDataObject;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.label.modelelement.GmDefaultFlatHeader;
import org.modelio.diagram.elements.core.link.ExtensionLocation;
import org.modelio.diagram.elements.core.link.GmLink;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.bpmn.flows.BpmnSequenceFlow;
import org.modelio.metamodel.bpmn.objects.BpmnDataAssociation;
import org.modelio.metamodel.bpmn.objects.BpmnDataObject;
import org.modelio.metamodel.bpmn.objects.BpmnSequenceFlowDataAssociation;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Graphic model for {@link IControlFlow}.
 */
@objid ("619f43fe-55b6-11e2-877f-002564c97630")
public class GmBpmnSequenceFlow extends GmLink {
    @objid ("619f4404-55b6-11e2-877f-002564c97630")
    private BpmnSequenceFlow element;

    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("619f4407-55b6-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("619f440a-55b6-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    @objid ("c5ca6e8d-59a6-11e2-ae45-002564c97630")
    private static final GmBpmnSequenceFlowStyleKeys styleKeyProvider = new GmBpmnSequenceFlowStyleKeys();

    /**
     * Initialize a control flow graphic model.
     * @param diagram The owning diagram
     * @param element The reference flow, may be null
     * @param ref The referenced flow reference, may not be null
     */
    @objid ("619f440c-55b6-11e2-877f-002564c97630")
    public GmBpmnSequenceFlow(GmAbstractDiagram diagram, MObject element, MRef ref) {
        super(diagram, ref);
        
        this.element = (BpmnSequenceFlow) element;
        
        GmDefaultFlatHeader extension = new GmDefaultFlatHeader(diagram, ref);
        extension.setShowLabel(false);
        addExtension(ExtensionLocation.MiddleNW, extension);
        addExtension(ExtensionLocation.SourceNW, new GmBpmnEdgeGuard(diagram, ref));
    }

    /**
     * For deserialization only.
     */
    @objid ("619f4418-55b6-11e2-877f-002564c97630")
    public GmBpmnSequenceFlow() {
        // Nothing to do.
    }

    @objid ("619f441b-55b6-11e2-877f-002564c97630")
    @Override
    public StyleKey getStyleKey(MetaKey metakey) {
        return styleKeyProvider.getStyleKey(metakey);
    }

    @objid ("61a0ca7d-55b6-11e2-877f-002564c97630")
    @Override
    public List<StyleKey> getStyleKeys() {
        return styleKeyProvider.getStyleKeys();
    }

    @objid ("61a0ca86-55b6-11e2-877f-002564c97630")
    @Override
    public MObject getFromElement() {
        return this.element.getSourceRef();
    }

    @objid ("61a0ca8d-55b6-11e2-877f-002564c97630")
    @Override
    public MObject getToElement() {
        return this.element.getTargetRef();
    }

    @objid ("61a0ca94-55b6-11e2-877f-002564c97630")
    @Override
    public BpmnSequenceFlow getRepresentedElement() {
        return this.element;
    }

    @objid ("61a0ca9b-55b6-11e2-877f-002564c97630")
    @Override
    public void refreshFromObModel() {
        if (this.element != null) {
            List<MObject> list_model = new ArrayList<>();
            List<GmNodeModel> list_gm = new ArrayList<>();
            List<MObject> list_new = new ArrayList<>();
            for (GmModel model : getExtensions()) {
                if (model instanceof GmBpmnDataObject) {
                    list_model.add(model.getRelatedElement());
                    list_gm.add((GmNodeModel) model);
                }
            }
        
            for (BpmnSequenceFlowDataAssociation assoc : this.element.getConnector()) {
                for (BpmnDataAssociation data_assoc : assoc.getDataAssociation()) {
                    if (data_assoc.getSourceRef().size() > 0) {
                        MObject telement = data_assoc.getSourceRef().get(0);
                        list_new.add(telement);
                    }
                }
            }
        
            for (GmNodeModel gm : list_gm) {
                if (!list_new.contains(gm.getRelatedElement())) {
                    removeExtension(gm);
                }
            }
        
            for (MObject new_element : list_new) {
                if (!list_model.contains(new_element)) {
                    addExtension(ExtensionLocation.MiddleNW,
                                 new GmBpmnDataObject(getDiagram(),
                                                      (BpmnDataObject) new_element,
                                                      new MRef(new_element)));
                }
            }
        
            firePropertyChange(PROPERTY_LABEL, null, getRelatedElement().getName());
            firePropertyChange(PROPERTY_LAYOUTDATA, null, getRepresentedElement().getName());
        }
        super.refreshFromObModel();
    }

    @objid ("61a0ca9e-55b6-11e2-877f-002564c97630")
    @Override
    public void readLink(IDiagramReader in) {
        super.readLink(in);
        this.element = (BpmnSequenceFlow) resolveRef(this.getRepresentedRef());
    }

    @objid ("61a0caa4-55b6-11e2-877f-002564c97630")
    @Override
    public MObject getRelatedElement() {
        return this.element;
    }

    @objid ("61a0caab-55b6-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmBpmnSequenceFlow." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("61a0cab1-55b6-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
