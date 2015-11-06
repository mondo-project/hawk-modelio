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
                                    

package org.modelio.property.ui.data.standard.bpmn;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.core.ui.ktable.types.IPropertyType;
import org.modelio.core.ui.ktable.types.bool.BooleanType;
import org.modelio.core.ui.ktable.types.element.MultipleElementType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.bpmn.activities.BpmnActivity;
import org.modelio.metamodel.bpmn.events.BpmnCatchEvent;
import org.modelio.metamodel.bpmn.events.BpmnThrowEvent;
import org.modelio.metamodel.bpmn.flows.BpmnSequenceFlow;
import org.modelio.metamodel.bpmn.gateways.BpmnComplexGateway;
import org.modelio.metamodel.bpmn.gateways.BpmnExclusiveGateway;
import org.modelio.metamodel.bpmn.gateways.BpmnInclusiveGateway;
import org.modelio.metamodel.bpmn.objects.BpmnDataAssociation;
import org.modelio.metamodel.bpmn.objects.BpmnDataObject;
import org.modelio.metamodel.bpmn.objects.BpmnItemAwareElement;
import org.modelio.metamodel.bpmn.objects.BpmnSequenceFlowDataAssociation;
import org.modelio.metamodel.bpmn.rootElements.BpmnFlowNode;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.model.IModel;

/**
 * <i>BpmnSequenceFlow</i> data model.
 * <p>
 * This class provides the list of properties for the <i>BpmnSequenceFlow</i> metaclass.
 */
@objid ("8e56edc5-c068-11e1-8c0a-002564c97630")
public class BpmnSequenceFlowPropertyModel extends AbstractPropertyModel<BpmnSequenceFlow> {
    /**
     * Properties to display for <i>BpmnSequenceFlow</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li>for the first row the value is the table header label (usually the metaclass name)
     * <li>for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a61fbc34-c068-11e1-8c0a-002564c97630")
    private static final String[] properties = new String[] { "SequenceFlow",
        "Name", "Default Flow", "Guard", "Immediate", "DataObject" };

    @objid ("aa1cd378-d004-11e1-9020-002564c97630")
    private IMModelServices modelService;

    @objid ("0701162d-16d1-11e2-aa0d-002564c97630")
    private IModel model;

    /**
     * Create a new <i>BpmnSequenceFlow</i> data model from an <i>BpmnSequenceFlow</i>.
     * @param theEditedElement the flow to edit.
     */
    @objid ("8e56edd1-c068-11e1-8c0a-002564c97630")
    public BpmnSequenceFlowPropertyModel(BpmnSequenceFlow theEditedElement, IMModelServices modelService, IModel model) {
        super(theEditedElement);
        this.modelService = modelService;
        this.model = model;
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8e56edd7-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8e56eddc-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return BpmnSequenceFlowPropertyModel.properties.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8e56ede1-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
            case 0: // col 0 is the property key
                return BpmnSequenceFlowPropertyModel.properties[row];
            case 1: // col 1 is the property value
                switch (row) {
                    case 0: // Header
                        return "Value";
                    case 1:
                        return this.theEditedElement.getName();
                    case 2:
                        return this.theEditedElement.getDefaultFrom() != null || 
                        this.theEditedElement.getDefaultOfExclusive() != null || 
                        this.theEditedElement.getDefaultOfInclusive() != null || 
                        this.theEditedElement.getDefaultOfComplex() != null;
                    case 3:
                        return this.theEditedElement.getConditionExpression();
                    case 4:
                        return this.theEditedElement.isIsImmediate()
                        ? Boolean.TRUE : Boolean.FALSE;
                    case 5:
                        final List<BpmnDataObject> element = new ArrayList<>();
                        for (final BpmnSequenceFlowDataAssociation assoc : this.theEditedElement.getConnector()) {
                            for (final BpmnDataAssociation dataassoc : assoc.getDataAssociation()) {
                                final BpmnItemAwareElement ita = dataassoc.getTargetRef();
                                if (ita != null &&
                                        ita instanceof BpmnDataObject) {
                                    element.add((BpmnDataObject) ita);
                                }
                            }
                        }
                        return element;
                    default:
                        return null;
                }
            default:
                return null;
        }
    }

    /**
     * Return the type of the element displayed at the specified row and column.
     * <p>
     * This type will be used to choose an editor and a renderer for each cell of the properties table.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the type of the element corresponding to the row and column
     */
    @objid ("8e56ede7-c068-11e1-8c0a-002564c97630")
    @Override
    public IPropertyType getTypeAt(int row, int col) {
        switch (col) {
            case 0: // col 0 is the property key type
                return new StringType(false);
            case 1: // col 1 is the property value type
                switch (row) {
                    case 0: // Header
                        return new StringType(false);
                    case 1:
                        return new StringType(true);
                    case 2:
                        return new BooleanType();
                    case 3:
                        return new StringType(true);
                    case 4:
                        return new BooleanType();
                    case 5:
                        return new MultipleElementType(true,
                                                       this.theEditedElement,
                                                       "DataObject",
                                                       BpmnDataObject.class,
                                                       this.model);
                    default:
                        return null;
                }
            default:
                return null;
        }
    }

    /**
     * Set value in the model for the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number.
     * @param col the column number.
     * @param value the value specified by the user.
     */
    @objid ("8e56eded-c068-11e1-8c0a-002564c97630")
    @Override
    @SuppressWarnings("unchecked")
    public void setValueAt(int row, int col, Object value) {
        switch (col) {
            case 0: // Keys cannot be modified
                return;
            case 1: // col 1 is the property value
                switch (row) {
                    case 0:
                        return; // Header cannot be modified
                    case 1:
                        this.theEditedElement.setName((String) value);
                        break;
                    case 2:
                        if((Boolean)value){
                            if (this.theEditedElement.getSourceRef() instanceof BpmnActivity) {
                                this.theEditedElement.setDefaultFrom((BpmnActivity) this.theEditedElement.getSourceRef());
                            } else if (this.theEditedElement.getSourceRef() instanceof BpmnExclusiveGateway) {
                                this.theEditedElement.setDefaultOfExclusive((BpmnExclusiveGateway) this.theEditedElement.getSourceRef());
                            } else if (this.theEditedElement.getSourceRef() instanceof BpmnInclusiveGateway) {
                                this.theEditedElement.setDefaultOfInclusive((BpmnInclusiveGateway) this.theEditedElement.getSourceRef());
                            } else if (this.theEditedElement.getSourceRef() instanceof BpmnComplexGateway) {
                                this.theEditedElement.setDefaultOfComplex((BpmnComplexGateway) this.theEditedElement.getSourceRef());
                            } 
                        }else{
                            this.theEditedElement.setDefaultFrom(null);
                            this.theEditedElement.setDefaultOfExclusive(null);
                            this.theEditedElement.setDefaultOfInclusive(null);
                            this.theEditedElement.setDefaultOfComplex(null);
                        }
        
                        break;
                    case 3:
                        this.theEditedElement.setConditionExpression((String) value);
                        break;
                    case 4:
                        this.theEditedElement.setIsImmediate((Boolean) value);
                        break;
                    case 5:
                        final List<BpmnDataObject> element = new ArrayList<>();
                        for (final BpmnSequenceFlowDataAssociation assoc : this.theEditedElement.getConnector()) {
                            for (final BpmnDataAssociation dataassoc : assoc.getDataAssociation()) {
                                final BpmnItemAwareElement ita = dataassoc.getTargetRef();
                                if (ita != null &&
                                        ita instanceof BpmnDataObject) {
                                    element.add((BpmnDataObject) ita);
                                }
                            }
                        }
        
                        final List<BpmnDataObject> newcontent = (List<BpmnDataObject>) value;
                        for (final BpmnDataObject s : element) {
                            if (!newcontent.contains(s)) {
                                removeDataObject(s);
                            }
                        }
        
                        for (final BpmnDataObject s : newcontent) {
                            if (!element.contains(s)) {
                                addDataObject(this.modelService, s);
                            }
                        }
        
                        break;
                    default:
                        return;
                }
                break;
            default:
                return;
        }
    }

    @objid ("8e587467-c068-11e1-8c0a-002564c97630")
    private void addDataObject(IMModelServices mmService, final BpmnDataObject dataobject) {
        final IModelFactory modelFactory = mmService.getModelFactory();
            
        final BpmnFlowNode source = this.theEditedElement.getSourceRef();
        final BpmnFlowNode target = this.theEditedElement.getTargetRef();
        
        final BpmnDataAssociation sourceAssociation = modelFactory.createBpmnDataAssociation();
        sourceAssociation.setStartingActivity((BpmnActivity) source);
        sourceAssociation.setTargetRef(dataobject);
        
        final BpmnDataAssociation targetAssociation = modelFactory.createBpmnDataAssociation();
        targetAssociation.setEndingActivity((BpmnActivity) target);
        targetAssociation.getSourceRef().add(dataobject);
        
        final BpmnSequenceFlowDataAssociation sequenceFlowAssociation = modelFactory.createBpmnSequenceFlowDataAssociation();
        sequenceFlowAssociation.setConnected(this.theEditedElement);
        sequenceFlowAssociation.getDataAssociation().add(sourceAssociation);
        sequenceFlowAssociation.getDataAssociation().add(targetAssociation);
    }

    @objid ("8e58746d-c068-11e1-8c0a-002564c97630")
    private void removeDataObject(final BpmnDataObject dataobject) {
        final List<BpmnDataAssociation> dataassociation = new ArrayList<>();
        dataassociation.addAll(dataobject.getSourceOfDataAssociation());
        dataassociation.addAll(dataobject.getTargetOfDataAssociation());
        
        for (final BpmnDataAssociation association : dataassociation) {
            for (final BpmnSequenceFlowDataAssociation sfda : association.getVisualShortCut()) {
                if (sfda.getConnected() != null) {
                    if (sfda.getConnected().equals(this.theEditedElement)) {
                        this.theEditedElement.getConnector().remove(sfda);
        
                        final BpmnActivity owneractivity = association.getStartingActivity();
                        if (owneractivity != null) {
                            owneractivity.getDataInputAssociation().remove(association);
                        }
        
                        final BpmnActivity ownerendactivity = association.getEndingActivity();
                        if (ownerendactivity != null) {
                            ownerendactivity.getDataInputAssociation().remove(association);
                        }
        
                        final BpmnThrowEvent ownerthrow = association.getStartingEvent();
                        if (ownerthrow != null) {
                            ownerthrow.getDataInputAssociation().remove(association);
                        }
        
                        final BpmnCatchEvent ownercatch = association.getEndingEvent();
                        if (ownercatch != null) {
                            ownercatch.getDataOutputAssociation().remove(association);
                        }
                    }
                }
            }
        }
    }

}
