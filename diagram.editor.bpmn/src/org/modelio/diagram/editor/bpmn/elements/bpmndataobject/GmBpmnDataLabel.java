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
                                    

package org.modelio.diagram.editor.bpmn.elements.bpmndataobject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.label.modelelement.GmModelElementFlatHeader;
import org.modelio.diagram.elements.core.model.IEditableText;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey.RepresentationMode;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.bpmn.objects.BpmnDataState;
import org.modelio.metamodel.bpmn.objects.BpmnItemAwareElement;
import org.modelio.metamodel.bpmn.objects.BpmnItemDefinition;
import org.modelio.metamodel.uml.behavior.stateMachineModel.State;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Label model for all {@link BpmnItemAwareElement}.
 * <p>
 * This class has been subclassed for each {@link BpmnItemAwareElement} sub-class for nothing (yet).
 */
@objid ("60d452f9-55b6-11e2-877f-002564c97630")
public class GmBpmnDataLabel extends GmModelElementFlatHeader {
    @objid ("60d452ff-55b6-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("60d452fc-55b6-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    /**
     * Create a model element label
     * @param diagram the diagram.
     * @param relatedRef a reference to the element this GmModel is related to.
     */
    @objid ("60d45301-55b6-11e2-877f-002564c97630")
    public GmBpmnDataLabel(final GmAbstractDiagram diagram, final MRef relatedRef) {
        super(diagram, relatedRef);
    }

    /**
     * For deserialization only.
     */
    @objid ("60d5d963-55b6-11e2-877f-002564c97630")
    public GmBpmnDataLabel() {
        // serialization
    }

    @objid ("60d5d966-55b6-11e2-877f-002564c97630")
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

    @objid ("60d5d975-55b6-11e2-877f-002564c97630")
    @Override
    public List<TaggedValue> filterTags(final List<TaggedValue> taggedValues) {
        return taggedValues;
    }

    @objid ("60d5d984-55b6-11e2-877f-002564c97630")
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

    @objid ("60d76013-55b6-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    @objid ("60d5d98b-55b6-11e2-877f-002564c97630")
    @Override
    public boolean isVisible() {
        final StyleKey key = getParent().getStyleKey(MetaKey.SHOWLABEL);
        if (key == null)
            return true;
        else
            return getStyle().getProperty(key);
    }

    @objid ("60d76002-55b6-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmBpmnDataLabel." + MINOR_VERSION_PROPERTY);
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

    @objid ("60d5d994-55b6-11e2-877f-002564c97630")
    @Override
    public void styleChanged(final IStyle changedStyle) {
        fireVisibilityChanged();
        super.styleChanged(changedStyle);
    }

    @objid ("60d75ff9-55b6-11e2-877f-002564c97630")
    @Override
    public void styleChanged(final StyleKey property, final Object newValue) {
        final StyleKey key = getParent().getStyleKey(MetaKey.SHOWLABEL);
        if (key != null && key.equals(property))
            fireVisibilityChanged();
        else
            super.styleChanged(property, newValue);
    }

    @objid ("60d76008-55b6-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmBpmnDataLabel." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("60d5d98f-55b6-11e2-877f-002564c97630")
    @Override
    protected String computeLabel() {
        String mlabel = null;
        String reference = null;
        
        if (getRelatedElement() != null && !getRelatedElement().getName().isEmpty()) {
            mlabel = getRelatedElement().getName();
        }
        
        BpmnItemAwareElement element = (BpmnItemAwareElement) getRelatedElement();
        if (element.getRepresentedAttribute() != null) {
            reference = element.getRepresentedAttribute().getName();
        } else if (element.getRepresentedInstance() != null) {
            reference = element.getRepresentedInstance().getName();
        } else if (element.getRepresentedAssociationEnd() != null) {
            reference = element.getRepresentedAssociationEnd().getName();
        } else if (element.getRepresentedAssociationEnd() != null) {
            reference = element.getRepresentedAssociationEnd().getName();
        } else if (element.getType() != null) {
            reference = element.getType().getName();
        } else if (element.getItemSubjectRef() != null) {
            BpmnItemDefinition item = element.getItemSubjectRef();
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
                if (! isDefaultLabel(mlabel, element)) {
                    s.append(mlabel);
                }
        
                // Append represented element name
                s.append(": ");
                s.append(reference);
            } else if (mlabel!=null){
                s.append(mlabel);
            } else {
                // Fall back to represented element name
                s.append(": ");
                s.append(reference);
            }
        
        } else if (mlabel != null) {
            // No represented element, just append the name
            s.append(mlabel);
        } 
        
        // Add state 
        String stateName = getInStateName(element);
        
        if (stateName != null) {
            s.append(" [");
            s.append(stateName);
            s.append("]");
        }
        return s.toString();
    }

    @objid ("68066ea9-6d1a-464a-8f24-9a37eb7403c4")
    private String getInStateName(BpmnItemAwareElement element) {
        // a BPMN can reference a UML state by too much ways:
        // 1) with 'inState : State' relation
        // 2) with 'dataState:BpmnState' (not used anywhere, not visible in Modelio GUI)
        // 2.1) then BpmnState.inState : State
        // 2.2) No UML state on the BpmnState , use the BpmnState name
        State inState = element.getInState();
        BpmnDataState dataState = element.getDataState();
        String stateName = null;
        
        if (inState == null && dataState != null) {
            inState = dataState.getUmlState();
            stateName = dataState.getName();
        }
        
        if (inState != null) 
            stateName = inState.getName();
        return stateName;
    }

    @objid ("60d7600e-55b6-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
    }

    @objid ("8f44f865-ac67-44eb-b1d3-8837b3e91555")
    private boolean isDefaultLabel(String aLabel, MObject element) {
        String basename = getDiagram().getModelManager().getModelServices().getElementNamer().getBaseName(element);
        basename = Pattern.quote(basename);
        return aLabel == null || aLabel.matches(basename+"[0-9]*");
    }

}
