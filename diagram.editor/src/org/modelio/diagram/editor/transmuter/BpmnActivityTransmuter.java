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
                                    

package org.modelio.diagram.editor.transmuter;

import java.util.ArrayList;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.gproject.model.api.MTools;
import org.modelio.metamodel.bpmn.activities.BpmnActivity;
import org.modelio.metamodel.bpmn.activities.BpmnSubProcess;
import org.modelio.metamodel.bpmn.events.BpmnBoundaryEvent;
import org.modelio.metamodel.bpmn.flows.BpmnMessageFlow;
import org.modelio.metamodel.bpmn.flows.BpmnSequenceFlow;
import org.modelio.metamodel.bpmn.objects.BpmnDataAssociation;
import org.modelio.metamodel.bpmn.objects.BpmnDataInput;
import org.modelio.metamodel.bpmn.objects.BpmnDataOutput;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLane;
import org.modelio.metamodel.bpmn.rootElements.BpmnFlowElement;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.vcore.smkernel.mapi.MDependency;
import org.modelio.vcore.smkernel.mapi.MObject;

// isShell = false isRamc = false
/**
 * Transmuter of BpmnActivity
 */
@objid ("83c99cda-5c05-11e2-a156-00137282c51b")
public class BpmnActivityTransmuter implements ITransmuter {
    @objid ("288e4c9e-5e26-11e2-a8be-00137282c51b")
    private String metaclass;

    @objid ("83c99cdc-5c05-11e2-a156-00137282c51b")
    private MObject element;

    @objid ("83cbff34-5c05-11e2-a156-00137282c51b")
    public BpmnActivityTransmuter(final MObject element, final String metaclass) {
        this.element = element;
        this.metaclass = metaclass;
    }

    @objid ("83cbff3f-5c05-11e2-a156-00137282c51b")
    @Override
    public boolean canTransmute() {
        return true;
    }

    @objid ("2890aefa-5e26-11e2-a8be-00137282c51b")
    @Override
    public MObject transmute(IMModelServices modelServices) {
        MObject owner = this.element.getCompositionOwner();
        
        // Create the Element...
        final IModelFactory modelFactory = modelServices.getModelFactory();
        MObject newElement = modelFactory.createElement(this.metaclass);
        
        // The new element must be attached to its parent using the composition dependency 
        // provided by the context. 
        // If the context provides a null dependency, use the default dependency recommended by the metamodel
        MDependency effectiveDependency = MTools.getMetaTool().getDefaultCompositionDep(owner, newElement);
        // ... and attach it to its parent.
        
        try {
            owner.mGet(effectiveDependency).add(newElement);
        } catch (Exception e) {
            //TODO : use a more accurate Exception...
            // The dependency indicated in the context cannot be used: try
            // to find a valid one!
            MDependency compositionDep = MTools.getMetaTool().getDefaultCompositionDep(owner, newElement);
            if (compositionDep != null) {
                owner.mGet(compositionDep).add(newElement);
            } else {
                newElement.delete();
                return null;
            }
        }
        
        if (newElement instanceof BpmnFlowElement && owner instanceof BpmnLane) {
            BpmnLane lane = (BpmnLane) owner;
            BpmnFlowElement flowElement = (BpmnFlowElement) newElement;
            flowElement.getBpmnLaneRefs().add(lane);
            lane.getFlowElementRef().add(flowElement);
        }
        
        // Set default name
        newElement.setName(modelServices.getElementNamer().getUniqueName(newElement));
        
        if (this.element instanceof BpmnActivity && newElement instanceof BpmnActivity) {
            BpmnActivity newActivity = (BpmnActivity) newElement;
            BpmnActivity oldActivity = (BpmnActivity) this.element;
            newActivity.setName(oldActivity.getName());
        
            for (BpmnLane lane : new ArrayList<>(oldActivity.getBpmnLaneRefs())) {
                newActivity.getBpmnLaneRefs().add(lane);
            }
        
            for (BpmnBoundaryEvent event : new ArrayList<>(oldActivity.getBoundaryEventRef())) {
                newActivity.getBoundaryEventRef().add(event);
            }
        
            for (BpmnDataInput subelement : new ArrayList<>(oldActivity.getInputSpecification())) {
                newActivity.getInputSpecification().add(subelement);
            }
        
            for (BpmnDataOutput subelement : new ArrayList<>(oldActivity.getOutputSpecification())) {
                newActivity.getOutputSpecification().add(subelement);
            }
        
            for (BpmnDataAssociation subelement : new ArrayList<>(oldActivity.getDataInputAssociation())) {
                newActivity.getDataInputAssociation().add(subelement);
            }
        
            for (BpmnDataAssociation subelement : new ArrayList<>(oldActivity.getDataOutputAssociation())) {
                newActivity.getDataOutputAssociation().add(subelement);
            }
        
            for (BpmnSequenceFlow subelement : new ArrayList<>(oldActivity.getOutgoing())) {
                newActivity.getOutgoing().add(subelement);
            }
        
            for (BpmnSequenceFlow subelement : new ArrayList<>(oldActivity.getIncoming())) {
                newActivity.getIncoming().add(subelement);
            }
        
            for (BpmnLane subelement : new ArrayList<>(oldActivity.getLane())) {
                newActivity.getLane().add(subelement);
            }
        
            for (Dependency subelement : new ArrayList<>(oldActivity.getImpactedDependency())) {
                newActivity.getImpactedDependency().add(subelement);
            }
        
            for (Dependency subelement : new ArrayList<>(oldActivity.getDependsOnDependency())) {
                newActivity.getDependsOnDependency().add(subelement);
            }
        
            if (oldActivity.getDefaultFlow() != null) {
                newActivity.setDefaultFlow(oldActivity.getDefaultFlow());
            }
        
            if (oldActivity.getLoopCharacteristics() != null) {
                newActivity.setLoopCharacteristics(oldActivity.getLoopCharacteristics());
            }
        
            if (oldActivity.getLoopCharacteristics() != null) {
                newActivity.setLoopCharacteristics(oldActivity.getLoopCharacteristics());
            }
        
        
            if (oldActivity.getOutgoingFlow().size() >0) {
                for(BpmnMessageFlow flow : new ArrayList<>(oldActivity.getOutgoingFlow())){
                    newActivity.getOutgoingFlow().add(flow);
                }
            }
        
            if (oldActivity.getIncomingFlow().size() >0) {
                for(BpmnMessageFlow flow : new ArrayList<>(oldActivity.getIncomingFlow())){
                    newActivity.getIncomingFlow().add(flow);
                }
            }
        
            if (this.element instanceof BpmnSubProcess && newElement instanceof BpmnSubProcess) {
                BpmnSubProcess newSubProcess = (BpmnSubProcess) newElement;
                BpmnSubProcess oldSubProcss = (BpmnSubProcess) this.element;
        
                for (BpmnFlowElement subelement : new ArrayList<>(oldSubProcss.getFlowElement())) {
                    newSubProcess.getFlowElement().add(subelement);
                }
            }
        }
        return newElement;
    }

}
