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
                                    

package org.modelio.diagram.editor.bpmn.elements;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.modelio.diagram.editor.bpmn.elements.bpmnactivity.BpmnPortContainerEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmnadhocsubprocess.BpmnAdHocSubProcessEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmnadhocsubprocess.GmBpmnAdHocSubProcess;
import org.modelio.diagram.editor.bpmn.elements.bpmnadhocsubprocess.GmBpmnAdHocSubProcessPrimaryNode;
import org.modelio.diagram.editor.bpmn.elements.bpmnboundaryevent.BpmnBoundaryEventEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmnboundaryevent.GmBpmnBoundaryEvent;
import org.modelio.diagram.editor.bpmn.elements.bpmnboundaryevent.GmBpmnBoundaryEventPrimaryNode;
import org.modelio.diagram.editor.bpmn.elements.bpmnbusinessruletask.BpmnBusinessRuleTaskEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmnbusinessruletask.GmBpmnBusinessRuleTask;
import org.modelio.diagram.editor.bpmn.elements.bpmnbusinessruletask.GmBpmnBusinessRuleTaskPrimaryNode;
import org.modelio.diagram.editor.bpmn.elements.bpmncallactivity.BpmnCallActivityEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmncallactivity.BpmnCallActivityHeaderEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmncallactivity.GmBpmnCallActivity;
import org.modelio.diagram.editor.bpmn.elements.bpmncallactivity.GmBpmnCallActivityHeader;
import org.modelio.diagram.editor.bpmn.elements.bpmncallactivity.GmBpmnCallActivityPrimaryNode;
import org.modelio.diagram.editor.bpmn.elements.bpmncomplexgateway.BpmnComplexGatewayEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmncomplexgateway.GmBpmnComplexGateway;
import org.modelio.diagram.editor.bpmn.elements.bpmncomplexgateway.GmBpmnComplexGatewayPrimaryNode;
import org.modelio.diagram.editor.bpmn.elements.bpmndataassociation.BpmnDataAssociationEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmndataassociation.GmBpmnDataAssociation;
import org.modelio.diagram.editor.bpmn.elements.bpmndataobject.GmBpmnDataLabel;
import org.modelio.diagram.editor.bpmn.elements.bpmndataobject.datainput.BpmnDataInputEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmndataobject.datainput.GmBpmnDataInput;
import org.modelio.diagram.editor.bpmn.elements.bpmndataobject.datainput.GmBpmnDataInputLabel;
import org.modelio.diagram.editor.bpmn.elements.bpmndataobject.datainput.GmBpmnDataInputPrimaryNode;
import org.modelio.diagram.editor.bpmn.elements.bpmndataobject.dataobject.BpmnDataObjectEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmndataobject.dataobject.GmBpmnDataObject;
import org.modelio.diagram.editor.bpmn.elements.bpmndataobject.dataobject.GmBpmnDataObjectPrimaryNode;
import org.modelio.diagram.editor.bpmn.elements.bpmndataobject.dataoutput.BpmnDataOutputEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmndataobject.dataoutput.GmBpmnDataOutput;
import org.modelio.diagram.editor.bpmn.elements.bpmndataobject.dataoutput.GmBpmnDataOutputLabel;
import org.modelio.diagram.editor.bpmn.elements.bpmndataobject.dataoutput.GmBpmnDataOutputPrimaryNode;
import org.modelio.diagram.editor.bpmn.elements.bpmndataobject.datastore.BpmnDataStoreEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmndataobject.datastore.GmBpmnDataStore;
import org.modelio.diagram.editor.bpmn.elements.bpmndataobject.datastore.GmBpmnDataStorePrimaryNode;
import org.modelio.diagram.editor.bpmn.elements.bpmnendevent.BpmnEndEventEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmnendevent.GmBpmnEndEvent;
import org.modelio.diagram.editor.bpmn.elements.bpmnendevent.GmBpmnEndEventPrimaryNode;
import org.modelio.diagram.editor.bpmn.elements.bpmneventbasedgateway.BpmnEventBasedGatewayEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmneventbasedgateway.GmBpmnEventBasedGateway;
import org.modelio.diagram.editor.bpmn.elements.bpmneventbasedgateway.GmBpmnEventBasedGatewayPrimaryNode;
import org.modelio.diagram.editor.bpmn.elements.bpmnexclusivegateway.BpmnExclusiveGatewayEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmnexclusivegateway.GmBpmnExclusiveGateway;
import org.modelio.diagram.editor.bpmn.elements.bpmnexclusivegateway.GmBpmnExclusiveGatewayPrimaryNode;
import org.modelio.diagram.editor.bpmn.elements.bpmninclusivegateway.BpmnInclusiveGatewayEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmninclusivegateway.GmBpmnInclusiveGateway;
import org.modelio.diagram.editor.bpmn.elements.bpmninclusivegateway.GmBpmnInclusiveGatewayPrimaryNode;
import org.modelio.diagram.editor.bpmn.elements.bpmnintermediatecatchevent.BpmnIntermediateCatchEventEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmnintermediatecatchevent.GmBpmnIntermediateCatchEvent;
import org.modelio.diagram.editor.bpmn.elements.bpmnintermediatecatchevent.GmBpmnIntermediateCatchEventPrimaryNode;
import org.modelio.diagram.editor.bpmn.elements.bpmnintermediatethrowevent.BpmnIntermediateThrowEventEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmnintermediatethrowevent.GmBpmnIntermediateThrowEvent;
import org.modelio.diagram.editor.bpmn.elements.bpmnintermediatethrowevent.GmBpmnIntermediateThrowEventPrimaryNode;
import org.modelio.diagram.editor.bpmn.elements.bpmnlane.BpmnLaneEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmnlane.GmBpmnLane;
import org.modelio.diagram.editor.bpmn.elements.bpmnlane.header.BpmnLaneHeaderEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmnlane.header.GmBpmnLaneHeader;
import org.modelio.diagram.editor.bpmn.elements.bpmnlane.hibridcontainer.BodyHybridContainerEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmnlane.hibridcontainer.GmBodyHybridContainer;
import org.modelio.diagram.editor.bpmn.elements.bpmnlanesetcontainer.BpmnLaneSetContainerEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmnlanesetcontainer.GmBpmnLaneSetContainer;
import org.modelio.diagram.editor.bpmn.elements.bpmnmanualtask.BpmnManualTaskEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmnmanualtask.GmBpmnManualTask;
import org.modelio.diagram.editor.bpmn.elements.bpmnmanualtask.GmBpmnManualTaskPrimaryNode;
import org.modelio.diagram.editor.bpmn.elements.bpmnmessage.BpmnMessageEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmnmessage.GmBpmnMessage;
import org.modelio.diagram.editor.bpmn.elements.bpmnmessage.GmBpmnMessageLabel;
import org.modelio.diagram.editor.bpmn.elements.bpmnmessage.GmBpmnMessageLink;
import org.modelio.diagram.editor.bpmn.elements.bpmnmessage.GmBpmnMessagePrimaryNode;
import org.modelio.diagram.editor.bpmn.elements.bpmnmessageflow.BpmnMessageFlowEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmnmessageflow.GmBpmnMessageFlow;
import org.modelio.diagram.editor.bpmn.elements.bpmnnodefooter.BpmnNodeFooterEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmnnodefooter.GmBpmnNodeFooter;
import org.modelio.diagram.editor.bpmn.elements.bpmnnodeheader.BpmnNodeHeaderEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmnnodeheader.GmBpmnNodeHeader;
import org.modelio.diagram.editor.bpmn.elements.bpmnparallelgateway.BpmnParallelGatewayEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmnparallelgateway.GmBpmnParallelGateway;
import org.modelio.diagram.editor.bpmn.elements.bpmnparallelgateway.GmBpmnParallelGatewayPrimaryNode;
import org.modelio.diagram.editor.bpmn.elements.bpmnreceivetask.BpmnReceiveTaskEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmnreceivetask.GmBpmnReceiveTask;
import org.modelio.diagram.editor.bpmn.elements.bpmnreceivetask.GmBpmnReceiveTaskPrimaryNode;
import org.modelio.diagram.editor.bpmn.elements.bpmnreceivetask.GmBpmnReceiveTaskTypeLabel;
import org.modelio.diagram.editor.bpmn.elements.bpmnsendtask.BpmnSendTaskEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmnsendtask.GmBpmnSendTask;
import org.modelio.diagram.editor.bpmn.elements.bpmnsendtask.GmBpmnSendTaskPrimaryNode;
import org.modelio.diagram.editor.bpmn.elements.bpmnsendtask.GmBpmnSendTaskTypeLabel;
import org.modelio.diagram.editor.bpmn.elements.bpmnsequenceflow.BpmnSequenceFlowEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmnsequenceflow.GmBpmnEdgeGuard;
import org.modelio.diagram.editor.bpmn.elements.bpmnsequenceflow.GmBpmnSequenceFlow;
import org.modelio.diagram.editor.bpmn.elements.bpmnservicetask.BpmnServiceTaskEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmnservicetask.GmBpmnServiceTask;
import org.modelio.diagram.editor.bpmn.elements.bpmnservicetask.GmBpmnServiceTaskPrimaryNode;
import org.modelio.diagram.editor.bpmn.elements.bpmnservicetask.GmBpmnServiceTaskTypeLabel;
import org.modelio.diagram.editor.bpmn.elements.bpmnsripttask.BpmnScriptTaskEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmnsripttask.GmBpmnScriptTask;
import org.modelio.diagram.editor.bpmn.elements.bpmnsripttask.GmBpmnScriptTaskPrimaryNode;
import org.modelio.diagram.editor.bpmn.elements.bpmnstartevent.BpmnStartEventEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmnstartevent.GmBpmnStartEvent;
import org.modelio.diagram.editor.bpmn.elements.bpmnstartevent.GmBpmnStartEventPrimaryNode;
import org.modelio.diagram.editor.bpmn.elements.bpmnsubprocess.BpmnSubProcessEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmnsubprocess.GmBpmnSubProcess;
import org.modelio.diagram.editor.bpmn.elements.bpmnsubprocess.GmBpmnSubProcessPrimaryNode;
import org.modelio.diagram.editor.bpmn.elements.bpmntask.BpmnTaskEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmntask.GmBpmnTask;
import org.modelio.diagram.editor.bpmn.elements.bpmntask.GmBpmnTaskPrimaryNode;
import org.modelio.diagram.editor.bpmn.elements.bpmntransaction.BpmnTransactionEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmntransaction.GmBpmnTransaction;
import org.modelio.diagram.editor.bpmn.elements.bpmntransaction.GmBpmnTransactionPrimaryNode;
import org.modelio.diagram.editor.bpmn.elements.bpmnusertask.BpmnUserTaskEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmnusertask.GmBpmnUserTask;
import org.modelio.diagram.editor.bpmn.elements.bpmnusertask.GmBpmnUserTaskPrimaryNode;
import org.modelio.diagram.editor.bpmn.elements.diagrams.processcollaboration.BpmnProcessCollaborationDiagramEditPart;
import org.modelio.diagram.editor.bpmn.elements.diagrams.processcollaboration.GmBpmnProcessCollaborationDiagram;
import org.modelio.diagram.editor.bpmn.elements.diagrams.subprocess.BpmnSubProcessDiagramEditPart;
import org.modelio.diagram.editor.bpmn.elements.diagrams.subprocess.GmBpmnSubProcessDiagram;
import org.modelio.diagram.editor.bpmn.elements.diagrams.view.GmBpmnDiagramView;
import org.modelio.diagram.elements.common.freezone.GmBodyFreeZone;
import org.modelio.diagram.elements.common.freezone.GmFreeZoneEditPart;
import org.modelio.diagram.elements.common.header.ModelElementHeaderEditPart;
import org.modelio.diagram.elements.common.label.base.GmElementLabel;
import org.modelio.diagram.elements.common.label.base.GmElementLabelEditPart;
import org.modelio.diagram.elements.common.label.modelelement.ModelElementFlatHeaderEditPart;
import org.modelio.diagram.elements.common.portcontainer.PortContainerEditPart;
import org.modelio.diagram.elements.core.link.GmLinkEditPart;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.elements.umlcommon.diagramview.DiagramViewEditPart;

/**
 * The Scope EditPart factory for Modelio diagrams.
 * <p>
 * Implementation of {@link EditPartFactory}.
 */
@objid ("60da6d4c-55b6-11e2-877f-002564c97630")
public class BpmnEditPartFactory implements EditPartFactory {
    /**
     * the default factory to use when structured mode is requested.
     */
    @objid ("60da6d4e-55b6-11e2-877f-002564c97630")
    private StructuredModeEditPartFactory structuredModeEditPartFactory = new StructuredModeEditPartFactory();

    @objid ("60da6d50-55b6-11e2-877f-002564c97630")
    @Override
    public EditPart createEditPart(EditPart context, Object model) {
        EditPart editPart = null;
        
        if (model instanceof GmNodeModel) {
            // For node models, delegates according the representation model.
            GmNodeModel node = (GmNodeModel) model;
            switch (node.getRepresentationMode()) {
                case IMAGE:
                    break;
                case SIMPLE:
                    break;
                case STRUCTURED:
                    editPart = this.structuredModeEditPartFactory.createEditPart(context, model);
                    break;
                default:
                    break;
            }
        
            if (editPart != null)
                return editPart;
        
            return null;
        }
        // Link models are always in structured mode.
        editPart = this.structuredModeEditPartFactory.createEditPart(context, model);
        
        if (editPart != null)
            return editPart;
        return null;
    }

    /**
     * EditPart factory for scope node models in standard structured mode.
     * <p>
     * This is the default mode so the default factory.
     */
    @objid ("60da6d57-55b6-11e2-877f-002564c97630")
    public class StructuredModeEditPartFactory implements EditPartFactory {
        @objid ("60da6d59-55b6-11e2-877f-002564c97630")
        @Override
        public EditPart createEditPart(EditPart context, Object model) {
            EditPart editPart = null;
            
            if (model.getClass() == GmBpmnProcessCollaborationDiagram.class) {
                editPart = new BpmnProcessCollaborationDiagramEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnBusinessRuleTaskPrimaryNode.class) {
                editPart = new BpmnBusinessRuleTaskEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnBusinessRuleTask.class) {
                editPart = new BpmnPortContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnTaskPrimaryNode.class) {
                editPart = new BpmnTaskEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnTask.class) {
                editPart = new BpmnPortContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnSendTaskPrimaryNode.class) {
                editPart = new BpmnSendTaskEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnSendTaskTypeLabel.class) {
                editPart = new ModelElementHeaderEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnSendTask.class) {
                editPart = new BpmnPortContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnReceiveTaskPrimaryNode.class) {
                editPart = new BpmnReceiveTaskEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnReceiveTaskTypeLabel.class) {
                editPart = new ModelElementHeaderEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnReceiveTask.class) {
                editPart = new BpmnPortContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            if (model.getClass() == GmBpmnServiceTaskPrimaryNode.class) {
                editPart = new BpmnServiceTaskEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnServiceTaskTypeLabel.class) {
                editPart = new ModelElementHeaderEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnServiceTask.class) {
                editPart = new BpmnPortContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            if (model.getClass() == GmBpmnUserTaskPrimaryNode.class) {
                editPart = new BpmnUserTaskEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnUserTask.class) {
                editPart = new BpmnPortContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            if (model.getClass() == GmBpmnManualTaskPrimaryNode.class) {
                editPart = new BpmnManualTaskEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnManualTask.class) {
                editPart = new BpmnPortContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            if (model.getClass() == GmBpmnScriptTaskPrimaryNode.class) {
                editPart = new BpmnScriptTaskEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnScriptTask.class) {
                editPart = new BpmnPortContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnNodeHeader.class) {
                editPart = new BpmnNodeHeaderEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnNodeFooter.class) {
                editPart = new BpmnNodeFooterEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnSequenceFlow.class) {
                editPart = new BpmnSequenceFlowEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnStartEvent.class) {
                editPart = new PortContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnBoundaryEvent.class) {
                editPart = new PortContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            if (model.getClass() == GmBpmnIntermediateCatchEvent.class) {
                editPart = new PortContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            if (model.getClass() == GmBpmnEndEvent.class) {
                editPart = new PortContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            if (model.getClass() == GmBpmnIntermediateThrowEvent.class) {
                editPart = new PortContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            if (model.getClass() == GmBpmnExclusiveGateway.class) {
                editPart = new PortContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            if (model.getClass() == GmBpmnInclusiveGateway.class) {
                editPart = new PortContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            if (model.getClass() == GmBpmnComplexGateway.class) {
                editPart = new PortContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            if (model.getClass() == GmBpmnParallelGateway.class) {
                editPart = new PortContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            if (model.getClass() == GmBpmnEventBasedGateway.class) {
                editPart = new PortContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnStartEventPrimaryNode.class) {
                editPart = new BpmnStartEventEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnBoundaryEventPrimaryNode.class) {
                editPart = new BpmnBoundaryEventEditPart();
                editPart.setModel(model);
                return editPart;
            }
            if (model.getClass() == GmBpmnIntermediateCatchEventPrimaryNode.class) {
                editPart = new BpmnIntermediateCatchEventEditPart();
                editPart.setModel(model);
                return editPart;
            }
            if (model.getClass() == GmBpmnEndEventPrimaryNode.class) {
                editPart = new BpmnEndEventEditPart();
                editPart.setModel(model);
                return editPart;
            }
            if (model.getClass() == GmBpmnIntermediateThrowEventPrimaryNode.class) {
                editPart = new BpmnIntermediateThrowEventEditPart();
                editPart.setModel(model);
                return editPart;
            }
            if (model.getClass() == GmBpmnExclusiveGatewayPrimaryNode.class) {
                editPart = new BpmnExclusiveGatewayEditPart();
                editPart.setModel(model);
                return editPart;
            }
            if (model.getClass() == GmBpmnInclusiveGatewayPrimaryNode.class) {
                editPart = new BpmnInclusiveGatewayEditPart();
                editPart.setModel(model);
                return editPart;
            }
            if (model.getClass() == GmBpmnComplexGatewayPrimaryNode.class) {
                editPart = new BpmnComplexGatewayEditPart();
                editPart.setModel(model);
                return editPart;
            }
            if (model.getClass() == GmBpmnParallelGatewayPrimaryNode.class) {
                editPart = new BpmnParallelGatewayEditPart();
                editPart.setModel(model);
                return editPart;
            }
            if (model.getClass() == GmBpmnEventBasedGatewayPrimaryNode.class) {
                editPart = new BpmnEventBasedGatewayEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnCallActivityPrimaryNode.class) {
                editPart = new BpmnCallActivityEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnCallActivity.class) {
                editPart = new BpmnPortContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnAdHocSubProcess.class) {
                editPart = new BpmnPortContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnAdHocSubProcessPrimaryNode.class) {
                editPart = new BpmnAdHocSubProcessEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnAdHocSubProcess.class) {
                editPart = new BpmnPortContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnSubProcessPrimaryNode.class) {
                editPart = new BpmnSubProcessEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnSubProcess.class) {
                editPart = new BpmnPortContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnTransactionPrimaryNode.class) {
                editPart = new BpmnTransactionEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnTransaction.class) {
                editPart = new BpmnPortContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnAdHocSubProcessPrimaryNode.class) {
                editPart = new BpmnAdHocSubProcessEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBodyFreeZone.class) {
                editPart = new GmFreeZoneEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnLaneSetContainer.class) {
                editPart = new BpmnLaneSetContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnLane.class) {
                editPart = new BpmnLaneEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnLaneHeader.class) {
                editPart = new BpmnLaneHeaderEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBodyHybridContainer.class) {
                editPart = new BodyHybridContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnEdgeGuard.class) {
                editPart = new GmElementLabelEditPart((GmElementLabel) model);
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnMessageFlow.class) {
                editPart = new BpmnMessageFlowEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnMessagePrimaryNode.class) {
                editPart = new BpmnMessageEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnMessage.class) {
                editPart = new BpmnPortContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnMessageLink.class) {
                editPart = new GmLinkEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnDataInputPrimaryNode.class) {
                editPart = new BpmnDataInputEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnDataOutputPrimaryNode.class) {
                editPart = new BpmnDataOutputEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnDataStorePrimaryNode.class) {
                editPart = new BpmnDataStoreEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnDataObject.class) {
                editPart = new PortContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnDataInput.class) {
                editPart = new PortContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnDataOutput.class) {
                editPart = new PortContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnDataStore.class) {
                editPart = new PortContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnDataObjectPrimaryNode.class) {
                editPart = new BpmnDataObjectEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnDataAssociation.class) {
                editPart = new BpmnDataAssociationEditPart();
                editPart.setModel(model);
                return editPart;
            }
            if (model instanceof GmBpmnSubProcessDiagram) {
                editPart = new BpmnSubProcessDiagramEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnDiagramView.class) {
                editPart = new DiagramViewEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnDataLabel.class) {
                editPart = new ModelElementFlatHeaderEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnMessageLabel.class) {
                editPart = new ModelElementFlatHeaderEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnDataInputLabel.class) {
                editPart = new ModelElementFlatHeaderEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnDataOutputLabel.class) {
                editPart = new ModelElementFlatHeaderEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmBpmnCallActivityHeader.class) {
                editPart = new BpmnCallActivityHeaderEditPart();
                editPart.setModel(model);
                return editPart;
            }
            return null;
        }

    }

}
