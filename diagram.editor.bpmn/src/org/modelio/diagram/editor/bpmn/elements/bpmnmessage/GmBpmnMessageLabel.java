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
                                    

package org.modelio.diagram.editor.bpmn.elements.bpmnmessage;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.bpmn.elements.bpmndataobject.GmBpmnDataObjectStyleKeys;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.label.modelelement.GmModelElementFlatHeader;
import org.modelio.diagram.elements.core.model.IEditableText;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey.RepresentationMode;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.bpmn.flows.BpmnMessage;
import org.modelio.metamodel.bpmn.objects.BpmnItemDefinition;
import org.modelio.metamodel.uml.behavior.stateMachineModel.State;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.vcore.smkernel.mapi.MRef;

@objid ("615f2dda-55b6-11e2-877f-002564c97630")
public class GmBpmnMessageLabel extends GmModelElementFlatHeader {
    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("615f2ddd-55b6-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("615f2de0-55b6-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    /**
     * Create a model element label
     * @param diagram the diagram.
     * @param relatedRef a reference to the element this GmModel is related to.
     */
    @objid ("6160b43a-55b6-11e2-877f-002564c97630")
    public GmBpmnMessageLabel(final GmAbstractDiagram diagram, final MRef relatedRef) {
        super(diagram, relatedRef);
    }

    /**
     * For deserialization only.
     */
    @objid ("6160b445-55b6-11e2-877f-002564c97630")
    public GmBpmnMessageLabel() {
        // serialization
    }

    @objid ("6160b448-55b6-11e2-877f-002564c97630")
    @Override
    public List<Stereotype> filterStereotypes(final List<Stereotype> stereotypes) {
        // Check the current representation mode
        final StyleKey key = getStyleKey(MetaKey.REPMODE);
        if (key != null) {
            // For image mode, filter the first image stereotype
            if (getStyle().getProperty(key) == RepresentationMode.IMAGE) {
                for (Stereotype stereo : stereotypes) {
                    if (!stereo.getIcon().isEmpty()) {
                        List<Stereotype> ret = new ArrayList<>(stereotypes);
                        ret.remove(stereo);
                        return ret;
                    }
                }
            }
        }
        return stereotypes;
    }

    @objid ("6160b457-55b6-11e2-877f-002564c97630")
    @Override
    public List<TaggedValue> filterTags(final List<TaggedValue> taggedValues) {
        return taggedValues;
    }

    @objid ("6160b466-55b6-11e2-877f-002564c97630")
    @Override
    public IEditableText getEditableText() {
        if (getRelatedElement() == null)
            return null;
        return new IEditableText() {
            @Override
            public String getText() {
        return getRelatedElement().getName();
                    }
        
                    @Override
                    public void setText(String text) {
        getRelatedElement().setName(text);
                    }
                };
    }

    @objid ("6160b46d-55b6-11e2-877f-002564c97630")
    @Override
    public boolean isVisible() {
        final StyleKey key = getParent().getStyleKey(MetaKey.SHOWLABEL);
        if (key == null)
            return true;
        else
            return getStyle().getProperty(key);
    }

    @objid ("6160b471-55b6-11e2-877f-002564c97630")
    @Override
    protected String computeLabel() {
        String mlabel = null;
        String reference = null;
        
        if (getRelatedElement() != null && !getRelatedElement().getName().equals("")) {
            mlabel = getRelatedElement().getName();
        }
        
        BpmnMessage element = (BpmnMessage) getRelatedElement();
        if (element.getType() != null) {
            reference = element.getType().getName();
        } else if (element.getItemRef() != null) {
            BpmnItemDefinition item = element.getItemRef();
            if (item.getStructureRef() != null) {
                reference = item.getStructureRef().getName();
            } else {
                reference = item.getName();
            }
        }
        
        StringBuilder s = new StringBuilder();
        
        if (reference != null) {
            Boolean showrepresented = getStyle().getProperty(GmBpmnDataObjectStyleKeys.SHOWREPRESENTED);
            if (Boolean.TRUE.equals(showrepresented)) {
                // Begin with the element name if :
                // - the element has a name that does not begin with the default name 
                // - or the element represents no UML element.
                String basename = getDiagram().getModelManager().getModelServices().getElementNamer().getBaseName(element);
                if (mlabel != null && !mlabel.startsWith(basename)) {
                    s.append(mlabel);
                }
        
                // Append represented element name
                s.append(": ");
                s.append(reference);
            } else {
                s.append(mlabel);
            }
        
        } else if (mlabel != null) {
            // No represented element, just append the name
            s.append(mlabel);
        } 
        
        // Add state
        State inState = element.getInState();
        
        if (inState != null) {
            String stateName = inState.getName();
            s.append(" [");
            s.append(stateName);
            s.append("]");
        }
        return s.toString();
    }

    @objid ("6160b476-55b6-11e2-877f-002564c97630")
    @Override
    public void styleChanged(final IStyle changedStyle) {
        fireVisibilityChanged();
        super.styleChanged(changedStyle);
    }

    @objid ("61623ade-55b6-11e2-877f-002564c97630")
    @Override
    public void styleChanged(final StyleKey property, final Object newValue) {
        final StyleKey key = getParent().getStyleKey(MetaKey.SHOWLABEL);
        if (key != null && key.equals(property))
            fireVisibilityChanged();
        else
            super.styleChanged(property, newValue);
    }

    @objid ("61623ae7-55b6-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmBpmnMessageLabel." + MINOR_VERSION_PROPERTY);
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

    @objid ("61623aed-55b6-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmBpmnMessageLabel." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("61623af3-55b6-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
    }

    @objid ("61623af8-55b6-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
