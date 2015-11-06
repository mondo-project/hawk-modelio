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
                                    

package org.modelio.diagram.api.dg;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.diagram.IDiagramGraphic;
import org.modelio.api.diagram.IDiagramLink;
import org.modelio.api.diagram.IDiagramNode;
import org.modelio.api.diagram.dg.IDiagramLayer;
import org.modelio.diagram.api.dg.activity.AcceptCallEventActionDG;
import org.modelio.diagram.api.dg.activity.AcceptChangeEventActionDG;
import org.modelio.diagram.api.dg.activity.AcceptSignalActionDG;
import org.modelio.diagram.api.dg.activity.AcceptTimeEventActionDG;
import org.modelio.diagram.api.dg.activity.ActivityDiagramDG;
import org.modelio.diagram.api.dg.activity.ActivityFinalNodeDG;
import org.modelio.diagram.api.dg.activity.ActivityPartitionDG;
import org.modelio.diagram.api.dg.activity.CallBehaviorActionDG;
import org.modelio.diagram.api.dg.activity.CallOperationActionDG;
import org.modelio.diagram.api.dg.activity.CentralBufferNodeDG;
import org.modelio.diagram.api.dg.activity.ClauseDG;
import org.modelio.diagram.api.dg.activity.ConditionalNodeDG;
import org.modelio.diagram.api.dg.activity.ControlFlowDG;
import org.modelio.diagram.api.dg.activity.DataStoreNodeDG;
import org.modelio.diagram.api.dg.activity.DecisionMergeNodeDG;
import org.modelio.diagram.api.dg.activity.DiagramPartitionContainerDG;
import org.modelio.diagram.api.dg.activity.ExceptionHandlerDG;
import org.modelio.diagram.api.dg.activity.ExpansionNodeDG;
import org.modelio.diagram.api.dg.activity.ExpansionRegionDG;
import org.modelio.diagram.api.dg.activity.FlowFinalNodeDG;
import org.modelio.diagram.api.dg.activity.ForkJoinNodeDG;
import org.modelio.diagram.api.dg.activity.InitialNodeDG;
import org.modelio.diagram.api.dg.activity.InputPinDG;
import org.modelio.diagram.api.dg.activity.InterruptibleActivityRegionDG;
import org.modelio.diagram.api.dg.activity.LoopNodeDG;
import org.modelio.diagram.api.dg.activity.ObjectFlowDG;
import org.modelio.diagram.api.dg.activity.ObjectNodeDG;
import org.modelio.diagram.api.dg.activity.OpaqueActionDG;
import org.modelio.diagram.api.dg.activity.OutputPinDG;
import org.modelio.diagram.api.dg.activity.SendSignalActionDG;
import org.modelio.diagram.api.dg.activity.StructuredActivityNodeDG;
import org.modelio.diagram.api.dg.activity.ValuePinDG;
import org.modelio.diagram.api.dg.bpmn.BpmnAdHocSubProcessDG;
import org.modelio.diagram.api.dg.bpmn.BpmnBoundaryEventDG;
import org.modelio.diagram.api.dg.bpmn.BpmnBusinessRuleTaskDG;
import org.modelio.diagram.api.dg.bpmn.BpmnCallActivityDG;
import org.modelio.diagram.api.dg.bpmn.BpmnComplexGatewayDG;
import org.modelio.diagram.api.dg.bpmn.BpmnDataAssociationDG;
import org.modelio.diagram.api.dg.bpmn.BpmnDataInputDG;
import org.modelio.diagram.api.dg.bpmn.BpmnDataObjectDG;
import org.modelio.diagram.api.dg.bpmn.BpmnDataOutputDG;
import org.modelio.diagram.api.dg.bpmn.BpmnDataStoreDG;
import org.modelio.diagram.api.dg.bpmn.BpmnEndEventDG;
import org.modelio.diagram.api.dg.bpmn.BpmnEventBasedGatewayDG;
import org.modelio.diagram.api.dg.bpmn.BpmnExclusiveGatewayDG;
import org.modelio.diagram.api.dg.bpmn.BpmnInclusiveGatewayDG;
import org.modelio.diagram.api.dg.bpmn.BpmnIntermediateCatchEventDG;
import org.modelio.diagram.api.dg.bpmn.BpmnIntermediateThrowEventDG;
import org.modelio.diagram.api.dg.bpmn.BpmnLaneDG;
import org.modelio.diagram.api.dg.bpmn.BpmnLaneSetContainerDG;
import org.modelio.diagram.api.dg.bpmn.BpmnManualTaskDG;
import org.modelio.diagram.api.dg.bpmn.BpmnMessageDG;
import org.modelio.diagram.api.dg.bpmn.BpmnMessageFlowDG;
import org.modelio.diagram.api.dg.bpmn.BpmnNodeFooterDG;
import org.modelio.diagram.api.dg.bpmn.BpmnNodeHeaderDG;
import org.modelio.diagram.api.dg.bpmn.BpmnParallelGatewayDG;
import org.modelio.diagram.api.dg.bpmn.BpmnProcessCollaborationDiagramDG;
import org.modelio.diagram.api.dg.bpmn.BpmnReceiveTaskDG;
import org.modelio.diagram.api.dg.bpmn.BpmnScriptTaskDG;
import org.modelio.diagram.api.dg.bpmn.BpmnSendTaskDG;
import org.modelio.diagram.api.dg.bpmn.BpmnSequenceFlowDG;
import org.modelio.diagram.api.dg.bpmn.BpmnServiceTaskDG;
import org.modelio.diagram.api.dg.bpmn.BpmnStartEventDG;
import org.modelio.diagram.api.dg.bpmn.BpmnSubProcessDG;
import org.modelio.diagram.api.dg.bpmn.BpmnSubProcessDiagramDG;
import org.modelio.diagram.api.dg.bpmn.BpmnTaskDG;
import org.modelio.diagram.api.dg.bpmn.BpmnTransactionDG;
import org.modelio.diagram.api.dg.bpmn.BpmnUserTaskDG;
import org.modelio.diagram.api.dg.common.ConstraintDG;
import org.modelio.diagram.api.dg.common.DependencyDG;
import org.modelio.diagram.api.dg.common.DiagramDrawingLayerDG;
import org.modelio.diagram.api.dg.common.DiagramHolderDG;
import org.modelio.diagram.api.dg.common.LabelDG;
import org.modelio.diagram.api.dg.common.NamespaceUseDG;
import org.modelio.diagram.api.dg.common.NoteDG;
import org.modelio.diagram.api.dg.common.PackageDG;
import org.modelio.diagram.api.dg.communication.CommunicationChannelDG;
import org.modelio.diagram.api.dg.communication.CommunicationDiagramDG;
import org.modelio.diagram.api.dg.communication.CommunicationNodeDG;
import org.modelio.diagram.api.dg.deployment.ArtifactDG;
import org.modelio.diagram.api.dg.deployment.DeploymentDiagramDG;
import org.modelio.diagram.api.dg.deployment.ManifestationDG;
import org.modelio.diagram.api.dg.deployment.NodeDG;
import org.modelio.diagram.api.dg.drawings.common.DiagramDrawingLinkDG;
import org.modelio.diagram.api.dg.drawings.common.DiagramDrawingNodeDG;
import org.modelio.diagram.api.dg.object.ObjectDiagramDG;
import org.modelio.diagram.api.dg.scope.BusinessRuleContainerDG;
import org.modelio.diagram.api.dg.scope.BusinessRuleDG;
import org.modelio.diagram.api.dg.scope.DictionaryDG;
import org.modelio.diagram.api.dg.scope.GoalContainerDG;
import org.modelio.diagram.api.dg.scope.GoalDG;
import org.modelio.diagram.api.dg.scope.ImpactDiagramDG;
import org.modelio.diagram.api.dg.scope.PropertyValueDG;
import org.modelio.diagram.api.dg.scope.RequirementContainerDG;
import org.modelio.diagram.api.dg.scope.RequirementDG;
import org.modelio.diagram.api.dg.scope.ScopeDiagramDG;
import org.modelio.diagram.api.dg.scope.TermDG;
import org.modelio.diagram.api.dg.sequence.CombinedFragmentDG;
import org.modelio.diagram.api.dg.sequence.ExecutionOccurenceSpecificationDG;
import org.modelio.diagram.api.dg.sequence.ExecutionSpecificationDG;
import org.modelio.diagram.api.dg.sequence.GateDG;
import org.modelio.diagram.api.dg.sequence.InteractionOperandDG;
import org.modelio.diagram.api.dg.sequence.InteractionUseDG;
import org.modelio.diagram.api.dg.sequence.LifelineDG;
import org.modelio.diagram.api.dg.sequence.MessageDG;
import org.modelio.diagram.api.dg.sequence.SequenceDiagramDG;
import org.modelio.diagram.api.dg.sequence.StateInvariantDG;
import org.modelio.diagram.api.dg.state.ChoicePseudoStateDG;
import org.modelio.diagram.api.dg.state.ConnectionPointReferenceDG;
import org.modelio.diagram.api.dg.state.DeepHistoryPseudoStateDG;
import org.modelio.diagram.api.dg.state.EntryPointPseudoStateDG;
import org.modelio.diagram.api.dg.state.ExitPointPseudoStateDG;
import org.modelio.diagram.api.dg.state.FinalStateDG;
import org.modelio.diagram.api.dg.state.ForkPseudoStateDG;
import org.modelio.diagram.api.dg.state.InitialPseudoStateDG;
import org.modelio.diagram.api.dg.state.InternalTransitionDG;
import org.modelio.diagram.api.dg.state.JoinPseudoStateDG;
import org.modelio.diagram.api.dg.state.JunctionPseudoStateDG;
import org.modelio.diagram.api.dg.state.RegionDG;
import org.modelio.diagram.api.dg.state.ShallowHistoryPseudoStateDG;
import org.modelio.diagram.api.dg.state.StateDG;
import org.modelio.diagram.api.dg.state.StateDiagramDG;
import org.modelio.diagram.api.dg.state.TerminatePseudoStateDG;
import org.modelio.diagram.api.dg.state.TransitionDG;
import org.modelio.diagram.api.dg.statik.ActivityDG;
import org.modelio.diagram.api.dg.statik.AssociationDG;
import org.modelio.diagram.api.dg.statik.AttributeDG;
import org.modelio.diagram.api.dg.statik.AttributeLinkDG;
import org.modelio.diagram.api.dg.statik.BindingDG;
import org.modelio.diagram.api.dg.statik.BindingLinkDG;
import org.modelio.diagram.api.dg.statik.BpmnBehaviorDG;
import org.modelio.diagram.api.dg.statik.BpmnProcessDG;
import org.modelio.diagram.api.dg.statik.ClassAssociationDG;
import org.modelio.diagram.api.dg.statik.ClassDG;
import org.modelio.diagram.api.dg.statik.CollaborationDG;
import org.modelio.diagram.api.dg.statik.CollaborationUseDG;
import org.modelio.diagram.api.dg.statik.CommunicationInteractionDG;
import org.modelio.diagram.api.dg.statik.ComponentDG;
import org.modelio.diagram.api.dg.statik.ConnectorDG;
import org.modelio.diagram.api.dg.statik.DataTypeDG;
import org.modelio.diagram.api.dg.statik.ElementImportDG;
import org.modelio.diagram.api.dg.statik.EnumerationDG;
import org.modelio.diagram.api.dg.statik.EnumerationLiteralDG;
import org.modelio.diagram.api.dg.statik.GeneralizationDG;
import org.modelio.diagram.api.dg.statik.InformationFlowDG;
import org.modelio.diagram.api.dg.statik.InformationItemDG;
import org.modelio.diagram.api.dg.statik.InstanceDG;
import org.modelio.diagram.api.dg.statik.InteractionDG;
import org.modelio.diagram.api.dg.statik.InterfaceDG;
import org.modelio.diagram.api.dg.statik.InterfaceRealizationDG;
import org.modelio.diagram.api.dg.statik.LinkDG;
import org.modelio.diagram.api.dg.statik.OperationDG;
import org.modelio.diagram.api.dg.statik.PackageImportDG;
import org.modelio.diagram.api.dg.statik.PackageMergeDG;
import org.modelio.diagram.api.dg.statik.PortDG;
import org.modelio.diagram.api.dg.statik.ProvidedInterfaceDG;
import org.modelio.diagram.api.dg.statik.RaisedExceptionDG;
import org.modelio.diagram.api.dg.statik.RequiredInterfaceDG;
import org.modelio.diagram.api.dg.statik.SignalDG;
import org.modelio.diagram.api.dg.statik.StateMachineDG;
import org.modelio.diagram.api.dg.statik.StaticDiagramDG;
import org.modelio.diagram.api.dg.statik.TemplateBindingDG;
import org.modelio.diagram.api.dg.usecase.ActorDG;
import org.modelio.diagram.api.dg.usecase.ExtensionPointDG;
import org.modelio.diagram.api.dg.usecase.SystemDG;
import org.modelio.diagram.api.dg.usecase.UseCaseDG;
import org.modelio.diagram.api.dg.usecase.UseCaseDependencyDG;
import org.modelio.diagram.api.dg.usecase.UseCaseDiagramDG;
import org.modelio.diagram.api.services.DiagramGraphic;
import org.modelio.diagram.api.services.DiagramHandle;
import org.modelio.diagram.api.services.DiagramLink;
import org.modelio.diagram.api.services.DiagramNode;
import org.modelio.diagram.editor.activity.elements.acceptsignal.GmAcceptSignal;
import org.modelio.diagram.editor.activity.elements.action.GmAction;
import org.modelio.diagram.editor.activity.elements.activitydiagram.GmActivityDiagram;
import org.modelio.diagram.editor.activity.elements.activityfinal.GmActivityFinal;
import org.modelio.diagram.editor.activity.elements.callbehavior.GmCallBehavior;
import org.modelio.diagram.editor.activity.elements.callevent.GmCallEvent;
import org.modelio.diagram.editor.activity.elements.calloperation.GmCallOperation;
import org.modelio.diagram.editor.activity.elements.centralbuffer.GmCentralBuffer;
import org.modelio.diagram.editor.activity.elements.changeevent.GmChangeEvent;
import org.modelio.diagram.editor.activity.elements.clause.GmClause;
import org.modelio.diagram.editor.activity.elements.conditional.GmConditional;
import org.modelio.diagram.editor.activity.elements.controlflow.GmControlFlow;
import org.modelio.diagram.editor.activity.elements.datastore.GmDataStore;
import org.modelio.diagram.editor.activity.elements.decisionmerge.GmDecisionMerge;
import org.modelio.diagram.editor.activity.elements.exceptionhandler.GmExceptionHandler;
import org.modelio.diagram.editor.activity.elements.expansionnode.GmExpansionNode;
import org.modelio.diagram.editor.activity.elements.expansionregion.GmExpansionRegion;
import org.modelio.diagram.editor.activity.elements.flowfinal.GmFlowFinal;
import org.modelio.diagram.editor.activity.elements.forkjoin.GmForkJoin;
import org.modelio.diagram.editor.activity.elements.initial.GmInitial;
import org.modelio.diagram.editor.activity.elements.inputpin.GmInputPin;
import org.modelio.diagram.editor.activity.elements.interruptible.GmInterruptible;
import org.modelio.diagram.editor.activity.elements.loopnode.GmLoopNode;
import org.modelio.diagram.editor.activity.elements.objectflow.GmObjectFlow;
import org.modelio.diagram.editor.activity.elements.objectnode.GmObjectNode;
import org.modelio.diagram.editor.activity.elements.outputpin.GmOutputPin;
import org.modelio.diagram.editor.activity.elements.partition.GmPartition;
import org.modelio.diagram.editor.activity.elements.partitioncontainer.GmDiagramPartitionContainer;
import org.modelio.diagram.editor.activity.elements.sendsignal.GmSendSignal;
import org.modelio.diagram.editor.activity.elements.structuredactivity.GmStructuredActivity;
import org.modelio.diagram.editor.activity.elements.timeevent.GmTimeEvent;
import org.modelio.diagram.editor.activity.elements.valuepin.GmValuePin;
import org.modelio.diagram.editor.bpmn.elements.bpmnadhocsubprocess.GmBpmnAdHocSubProcess;
import org.modelio.diagram.editor.bpmn.elements.bpmnboundaryevent.GmBpmnBoundaryEvent;
import org.modelio.diagram.editor.bpmn.elements.bpmnbusinessruletask.GmBpmnBusinessRuleTask;
import org.modelio.diagram.editor.bpmn.elements.bpmncallactivity.GmBpmnCallActivity;
import org.modelio.diagram.editor.bpmn.elements.bpmncomplexgateway.GmBpmnComplexGateway;
import org.modelio.diagram.editor.bpmn.elements.bpmndataassociation.GmBpmnDataAssociation;
import org.modelio.diagram.editor.bpmn.elements.bpmndataobject.datainput.GmBpmnDataInput;
import org.modelio.diagram.editor.bpmn.elements.bpmndataobject.dataobject.GmBpmnDataObject;
import org.modelio.diagram.editor.bpmn.elements.bpmndataobject.dataoutput.GmBpmnDataOutput;
import org.modelio.diagram.editor.bpmn.elements.bpmndataobject.datastore.GmBpmnDataStore;
import org.modelio.diagram.editor.bpmn.elements.bpmnendevent.GmBpmnEndEvent;
import org.modelio.diagram.editor.bpmn.elements.bpmneventbasedgateway.GmBpmnEventBasedGateway;
import org.modelio.diagram.editor.bpmn.elements.bpmnexclusivegateway.GmBpmnExclusiveGateway;
import org.modelio.diagram.editor.bpmn.elements.bpmninclusivegateway.GmBpmnInclusiveGateway;
import org.modelio.diagram.editor.bpmn.elements.bpmnintermediatecatchevent.GmBpmnIntermediateCatchEvent;
import org.modelio.diagram.editor.bpmn.elements.bpmnintermediatethrowevent.GmBpmnIntermediateThrowEvent;
import org.modelio.diagram.editor.bpmn.elements.bpmnlane.GmBpmnLane;
import org.modelio.diagram.editor.bpmn.elements.bpmnlanesetcontainer.GmBpmnLaneSetContainer;
import org.modelio.diagram.editor.bpmn.elements.bpmnmanualtask.GmBpmnManualTask;
import org.modelio.diagram.editor.bpmn.elements.bpmnmessage.GmBpmnMessage;
import org.modelio.diagram.editor.bpmn.elements.bpmnmessageflow.GmBpmnMessageFlow;
import org.modelio.diagram.editor.bpmn.elements.bpmnnodefooter.GmBpmnNodeFooter;
import org.modelio.diagram.editor.bpmn.elements.bpmnnodeheader.GmBpmnNodeHeader;
import org.modelio.diagram.editor.bpmn.elements.bpmnparallelgateway.GmBpmnParallelGateway;
import org.modelio.diagram.editor.bpmn.elements.bpmnreceivetask.GmBpmnReceiveTask;
import org.modelio.diagram.editor.bpmn.elements.bpmnsendtask.GmBpmnSendTask;
import org.modelio.diagram.editor.bpmn.elements.bpmnsequenceflow.GmBpmnSequenceFlow;
import org.modelio.diagram.editor.bpmn.elements.bpmnservicetask.GmBpmnServiceTask;
import org.modelio.diagram.editor.bpmn.elements.bpmnsripttask.GmBpmnScriptTask;
import org.modelio.diagram.editor.bpmn.elements.bpmnstartevent.GmBpmnStartEvent;
import org.modelio.diagram.editor.bpmn.elements.bpmnsubprocess.GmBpmnSubProcess;
import org.modelio.diagram.editor.bpmn.elements.bpmntask.GmBpmnTask;
import org.modelio.diagram.editor.bpmn.elements.bpmntransaction.GmBpmnTransaction;
import org.modelio.diagram.editor.bpmn.elements.bpmnusertask.GmBpmnUserTask;
import org.modelio.diagram.editor.bpmn.elements.diagrams.processcollaboration.GmBpmnProcessCollaborationDiagram;
import org.modelio.diagram.editor.bpmn.elements.diagrams.subprocess.GmBpmnSubProcessDiagram;
import org.modelio.diagram.editor.communication.elements.communicationchannel.GmCommunicationChannel;
import org.modelio.diagram.editor.communication.elements.communicationdiagram.GmCommunicationDiagram;
import org.modelio.diagram.editor.communication.elements.communicationnode.GmCommunicationNode;
import org.modelio.diagram.editor.deployment.elements.artifact.GmArtifact;
import org.modelio.diagram.editor.deployment.elements.deploymentdiagram.GmDeploymentDiagram;
import org.modelio.diagram.editor.deployment.elements.manifestation.GmManifestation;
import org.modelio.diagram.editor.deployment.elements.node.GmNode;
import org.modelio.diagram.editor.object.elements.objectdiagram.GmObjectDiagram;
import org.modelio.diagram.editor.sequence.elements.combinedfragment.GmCombinedFragment;
import org.modelio.diagram.editor.sequence.elements.executionoccurencespecification.GmExecutionOccurenceSpecification;
import org.modelio.diagram.editor.sequence.elements.executionspecification.GmExecutionSpecification;
import org.modelio.diagram.editor.sequence.elements.gate.GmGate;
import org.modelio.diagram.editor.sequence.elements.interactionoperand.GmInteractionOperand;
import org.modelio.diagram.editor.sequence.elements.interactionuse.GmInteractionUse;
import org.modelio.diagram.editor.sequence.elements.lifeline.GmLifeline;
import org.modelio.diagram.editor.sequence.elements.message.GmMessage;
import org.modelio.diagram.editor.sequence.elements.sequencediagram.GmSequenceDiagram;
import org.modelio.diagram.editor.sequence.elements.stateinvariant.GmStateInvariant;
import org.modelio.diagram.editor.state.elements.choice.GmChoice;
import org.modelio.diagram.editor.state.elements.connectionpoint.GmConnectionPoint;
import org.modelio.diagram.editor.state.elements.deephistory.GmDeepHistory;
import org.modelio.diagram.editor.state.elements.entry.GmEntry;
import org.modelio.diagram.editor.state.elements.exit.GmExitPoint;
import org.modelio.diagram.editor.state.elements.finalstate.GmFinalState;
import org.modelio.diagram.editor.state.elements.fork.GmForkState;
import org.modelio.diagram.editor.state.elements.initialstate.GmInitialState;
import org.modelio.diagram.editor.state.elements.internaltransition.GmInternalTransition;
import org.modelio.diagram.editor.state.elements.join.GmJoin;
import org.modelio.diagram.editor.state.elements.junction.GmJunction;
import org.modelio.diagram.editor.state.elements.region.GmRegion;
import org.modelio.diagram.editor.state.elements.shallowhistory.GmShallowHistory;
import org.modelio.diagram.editor.state.elements.state.GmState;
import org.modelio.diagram.editor.state.elements.statediagram.GmStateDiagram;
import org.modelio.diagram.editor.state.elements.terminal.GmTerminal;
import org.modelio.diagram.editor.state.elements.transition.GmTransition;
import org.modelio.diagram.editor.statik.elements.activity.GmActivity;
import org.modelio.diagram.editor.statik.elements.association.GmAssociation;
import org.modelio.diagram.editor.statik.elements.associationclass.GmClassAssociationLink;
import org.modelio.diagram.editor.statik.elements.attribute.GmAttribute;
import org.modelio.diagram.editor.statik.elements.binding.GmBindingLabel;
import org.modelio.diagram.editor.statik.elements.bindinglink.GmBindingLink;
import org.modelio.diagram.editor.statik.elements.bpmnbehavior.GmBpmnBehavior;
import org.modelio.diagram.editor.statik.elements.bpmnprocess.GmBpmnProcess;
import org.modelio.diagram.editor.statik.elements.clazz.GmClass;
import org.modelio.diagram.editor.statik.elements.collab.GmCollaboration;
import org.modelio.diagram.editor.statik.elements.collabuse.GmCollaborationUse;
import org.modelio.diagram.editor.statik.elements.communicationinteraction.GmCommunicationInteraction;
import org.modelio.diagram.editor.statik.elements.component.GmComponent;
import org.modelio.diagram.editor.statik.elements.connector.GmConnectorLink;
import org.modelio.diagram.editor.statik.elements.datatype.GmDataType;
import org.modelio.diagram.editor.statik.elements.elementimport.GmElementImport;
import org.modelio.diagram.editor.statik.elements.enumeration.GmEnum;
import org.modelio.diagram.editor.statik.elements.enumliteral.GmEnumLitteral;
import org.modelio.diagram.editor.statik.elements.generalization.GmGeneralization;
import org.modelio.diagram.editor.statik.elements.informationflowlink.GmInformationFlowLink;
import org.modelio.diagram.editor.statik.elements.informationitem.GmInformationItem;
import org.modelio.diagram.editor.statik.elements.instance.GmInstance;
import org.modelio.diagram.editor.statik.elements.instancelink.GmInstanceLink;
import org.modelio.diagram.editor.statik.elements.interaction.GmInteraction;
import org.modelio.diagram.editor.statik.elements.interfaze.GmInterface;
import org.modelio.diagram.editor.statik.elements.operation.GmOperation;
import org.modelio.diagram.editor.statik.elements.packageimport.GmPackageImport;
import org.modelio.diagram.editor.statik.elements.packagemerge.GmPackageMerge;
import org.modelio.diagram.editor.statik.elements.packaze.GmPackage;
import org.modelio.diagram.editor.statik.elements.ports.GmPort;
import org.modelio.diagram.editor.statik.elements.providedinterface.GmProvidedInterfaceLink;
import org.modelio.diagram.editor.statik.elements.raisedexception.GmRaisedException;
import org.modelio.diagram.editor.statik.elements.realization.GmInterfaceRealization;
import org.modelio.diagram.editor.statik.elements.requiredinterface.GmRequiredInterfaceLink;
import org.modelio.diagram.editor.statik.elements.signal.GmSignal;
import org.modelio.diagram.editor.statik.elements.slot.GmSlot;
import org.modelio.diagram.editor.statik.elements.statemachine.GmStateMachine;
import org.modelio.diagram.editor.statik.elements.staticdiagram.GmStaticDiagram;
import org.modelio.diagram.editor.statik.elements.templatebinding.GmTemplateBinding;
import org.modelio.diagram.editor.usecase.elements.actor.GmActor;
import org.modelio.diagram.editor.usecase.elements.extensionpoint.GmExtensionPoint;
import org.modelio.diagram.editor.usecase.elements.system.GmSystem;
import org.modelio.diagram.editor.usecase.elements.usecase.GmUseCase;
import org.modelio.diagram.editor.usecase.elements.usecasedependency.GmUseCaseDependency;
import org.modelio.diagram.editor.usecase.elements.usecasediagram.GmUseCaseDiagram;
import org.modelio.diagram.elements.common.label.base.GmElementLabel;
import org.modelio.diagram.elements.core.model.IGmLink;
import org.modelio.diagram.elements.core.model.IGmObject;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.elements.drawings.core.IGmDrawing;
import org.modelio.diagram.elements.drawings.core.IGmDrawingLayer;
import org.modelio.diagram.elements.drawings.core.IGmDrawingLink;
import org.modelio.diagram.elements.drawings.core.IGmNodeDrawing;
import org.modelio.diagram.elements.drawings.ellipse.GmEllipseDrawing;
import org.modelio.diagram.elements.drawings.line.GmLineDrawing;
import org.modelio.diagram.elements.drawings.rectangle.GmRectangleDrawing;
import org.modelio.diagram.elements.drawings.text.GmTextDrawing;
import org.modelio.diagram.elements.umlcommon.constraint.GmConstraintBody;
import org.modelio.diagram.elements.umlcommon.dependency.GmDependency;
import org.modelio.diagram.elements.umlcommon.diagramholder.GmDiagramHolder;
import org.modelio.diagram.elements.umlcommon.namespaceuse.GmNamespaceUse;
import org.modelio.diagram.elements.umlcommon.note.GmNote;

/**
 * Diagram graphic factory.
 */
@objid ("82b36ce4-3670-43aa-b785-f84474746a27")
public class DGFactory {
    @objid ("12fe75ad-ab5a-4ad8-b8c7-2dbad24eb70d")
    private static final DGFactory INSTANCE = new DGFactory();

    @objid ("dbe693bf-71c0-4df6-ad31-7324ef842dd5")
    private DGFactory() {
    }

    /**
     * @return the singleton instance.
     */
    @objid ("a6b65b4c-8083-4347-b1d9-d40d1a81dd50")
    public static DGFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Returns a DiagramGraphic for the given model. Can be either a DiagramNode or a DiagramLink.
     * @param diagramHandle a handle to the diagram
     * @param gmModel the model.
     * @return a {@link DiagramGraphic}
     */
    @objid ("ee0d0909-6811-4561-9079-4d28986cdd1a")
    public IDiagramGraphic getDiagramGraphic(DiagramHandle diagramHandle, IGmObject gmModel) {
        if (gmModel instanceof GmNodeModel)
            return this.getDiagramNode(diagramHandle, (GmNodeModel) gmModel);
        
        if (gmModel instanceof IGmLink)
            return this.getDiagramLink(diagramHandle, (IGmLink) gmModel);
        
        if (gmModel instanceof IGmDrawing)
            return getDiagramDrawingGraphic(diagramHandle, (IGmDrawing) gmModel);
        return null;
    }

    /**
     * Return a list of DiagramGraphics for each given model. Can be mixed {@link DiagramNode}s and {@link DiagramLink}s
     * @param diagramHandle a handle to the diagram
     * @param models the models
     * @return a list of {@link DiagramGraphic}
     */
    @objid ("01817c25-cee2-42ca-b74c-c3510518726e")
    public List<IDiagramGraphic> getDiagramGraphics(DiagramHandle diagramHandle, List<? extends IGmObject> models) {
        List<IDiagramGraphic> list = new ArrayList<>();
        for (IGmObject gm : models) {
            IDiagramGraphic diagramGraphic = this.getDiagramGraphic(diagramHandle, gm);
            if (diagramGraphic != null) {
                list.add(diagramGraphic);
            }
        }
        return list;
    }

    /**
     * Returns a DiagramLink from the given model.
     * @param diagramHandle a handle to the diagram.
     * @param gmLink the model.
     * @return a {@link DiagramLink}
     */
    @objid ("9ece0dd2-718f-486e-aa45-34e751fd9203")
    public IDiagramLink getDiagramLink(DiagramHandle diagramHandle, IGmLink gmLink) {
        IDiagramLink ret = getActivityLink(diagramHandle, gmLink);
        
        if (ret == null) {
            ret = getCommunicationLink(diagramHandle, gmLink);
        }
        
        if (ret == null) {
            ret = getDeploymentLink(diagramHandle, gmLink);
        }
        
        if (ret == null) {
            ret = getStateLink(diagramHandle, gmLink);
        }
        
        if (ret == null) {
            ret = getUseCaseLink(diagramHandle, gmLink);
        }
        
        if (ret == null) {
            ret = getStatikLink(diagramHandle, gmLink);
        }
        
        if (ret == null) {
            ret = getSequenceLink(diagramHandle, gmLink);
        }
        
        if (ret == null) {
            ret = getBpmnLink(diagramHandle, gmLink);
        }
        
        if (ret == null) {
            ret = getCommonLink(diagramHandle, gmLink);
        }
        return ret;
    }

    /**
     * Returns a Diagram Node for the given model.
     * @param diagramHandle the diagram in which the model is shown.
     * @param gmNodeModel the model.
     * @return a {@link DiagramNode}
     */
    @objid ("0c99b06f-a207-4b03-b535-a1f176b81570")
    public IDiagramNode getDiagramNode(DiagramHandle diagramHandle, GmNodeModel gmNodeModel) {
        if (!gmNodeModel.isVisible()) {
            return null;
        }
        
        IDiagramNode ret = getActivityNode(diagramHandle, gmNodeModel);
        
        if (ret == null) {
            ret = getCommunicationNode(diagramHandle, gmNodeModel);
        }
        
        if (ret == null) {
            ret = getDeploymentNode(diagramHandle, gmNodeModel);
        }
        
        if (ret == null) {
            ret = getObjectNode(diagramHandle, gmNodeModel);
        }
        
        if (ret == null) {
            ret = getStateNode(diagramHandle, gmNodeModel);
        }
        
        if (ret == null) {
            ret = getUseCaseNode(diagramHandle, gmNodeModel);
        }
        
        if (ret == null) {
            ret = getScopeNode(diagramHandle, gmNodeModel);
        }
        
        if (ret == null) {
            ret = getStatikNode(diagramHandle, gmNodeModel);
        }
        
        if (ret == null) {
            ret = getSequenceNode(diagramHandle, gmNodeModel);
        }
        
        if (ret == null) {
            ret = getBpmnNode(diagramHandle, gmNodeModel);
        }
        
        if (ret == null) {
            ret = getCommonNode(diagramHandle, gmNodeModel);
        }
        return ret;
    }

    /**
     * Return a list of DiagramNode for each given model.
     * @param diagramHandle a handle to the diagram
     * @param models the models
     * @return a list of {@link DiagramNode}
     */
    @objid ("91972eb5-e7d8-4f2c-8234-d681ee18cc04")
    public List<IDiagramNode> getDiagramNodes(DiagramHandle diagramHandle, List<GmNodeModel> models) {
        List<IDiagramNode> list = new ArrayList<>();
        for (GmNodeModel gm : models) {
            IDiagramNode diagramNode = this.getDiagramNode(diagramHandle, gm);
            if (diagramNode != null) {
                list.add(diagramNode);
            }
        }
        return list;
    }

    @objid ("7a4edece-23d1-4090-a7ac-2cf4bbc28d33")
    private IDiagramLink getActivityLink(final DiagramHandle diagramHandle, final IGmLink gmLink) {
        // GmControlFlow
        if (gmLink instanceof GmControlFlow) {
            return new ControlFlowDG(diagramHandle, gmLink);
        }
        
        // GmExceptionHandler
        if (gmLink instanceof GmExceptionHandler) {
            return new ExceptionHandlerDG(diagramHandle, gmLink);
        }
        
        // GmObjectFlow
        if (gmLink instanceof GmObjectFlow) {
            return new ObjectFlowDG(diagramHandle, gmLink);
        }
        
        // This is not an activity link
        return null;
    }

    @objid ("c4ccaf4c-c991-4090-8236-6e6905f82ba4")
    private IDiagramNode getActivityNode(final DiagramHandle diagramHandle, final GmNodeModel gmNodeModel) {
        // GmAcceptSignal
        if (gmNodeModel instanceof GmAcceptSignal) {
            return new AcceptSignalActionDG(diagramHandle, gmNodeModel);
        }
        
        // GmAction
        if (gmNodeModel instanceof GmAction) {
            return new OpaqueActionDG(diagramHandle, gmNodeModel);
        }
        
        // GmActivityDiagram
        if (gmNodeModel instanceof GmActivityDiagram) {
            return new ActivityDiagramDG(diagramHandle, gmNodeModel);
        }
        
        // GmActivityFinal
        if (gmNodeModel instanceof GmActivityFinal) {
            return new ActivityFinalNodeDG(diagramHandle, gmNodeModel);
        }
        
        // GmCallBehavior
        if (gmNodeModel instanceof GmCallBehavior) {
            return new CallBehaviorActionDG(diagramHandle, gmNodeModel);
        }
        
        // GmCallEvent
        if (gmNodeModel instanceof GmCallEvent) {
            return new AcceptCallEventActionDG(diagramHandle, gmNodeModel);
        }
        
        // GmCallOperation
        if (gmNodeModel instanceof GmCallOperation) {
            return new CallOperationActionDG(diagramHandle, gmNodeModel);
        }
        
        // GmCentralBuffer
        if (gmNodeModel instanceof GmCentralBuffer) {
            return new CentralBufferNodeDG(diagramHandle, gmNodeModel);
        }
        
        // GmChangeEvent
        if (gmNodeModel instanceof GmChangeEvent) {
            return new AcceptChangeEventActionDG(diagramHandle, gmNodeModel);
        }
        
        // GmClause
        if (gmNodeModel instanceof GmClause) {
            return new ClauseDG(diagramHandle, gmNodeModel);
        }
        
        // GmConditional
        if (gmNodeModel instanceof GmConditional) {
            return new ConditionalNodeDG(diagramHandle, gmNodeModel);
        }
        
        // GmDataStore
        if (gmNodeModel instanceof GmDataStore) {
            return new DataStoreNodeDG(diagramHandle, gmNodeModel);
        }
        
        // GmDecisionMerge
        if (gmNodeModel instanceof GmDecisionMerge) {
            return new DecisionMergeNodeDG(diagramHandle, gmNodeModel);
        }
        
        // GmDiagramPartitionContainer
        if (gmNodeModel instanceof GmDiagramPartitionContainer) {
            return new DiagramPartitionContainerDG(diagramHandle, gmNodeModel);
        }
        
        // GmExpansionNode
        if (gmNodeModel instanceof GmExpansionNode) {
            return new ExpansionNodeDG(diagramHandle, gmNodeModel);
        }
        
        // GmExpansionRegion
        if (gmNodeModel instanceof GmExpansionRegion) {
            return new ExpansionRegionDG(diagramHandle, gmNodeModel);
        }
        
        // GmFlowFinal
        if (gmNodeModel instanceof GmFlowFinal) {
            return new FlowFinalNodeDG(diagramHandle, gmNodeModel);
        }
        
        // GmForkJoin
        if (gmNodeModel instanceof GmForkJoin) {
            return new ForkJoinNodeDG(diagramHandle, gmNodeModel);
        }
        
        // GmInitial
        if (gmNodeModel instanceof GmInitial) {
            return new InitialNodeDG(diagramHandle, gmNodeModel);
        }
        
        // GmInputPin
        if (gmNodeModel instanceof GmInputPin) {
            return new InputPinDG(diagramHandle, gmNodeModel);
        }
        
        // GmInterruptible
        if (gmNodeModel instanceof GmInterruptible) {
            return new InterruptibleActivityRegionDG(diagramHandle, gmNodeModel);
        }
        
        // GmLoopNode
        if (gmNodeModel instanceof GmLoopNode) {
            return new LoopNodeDG(diagramHandle, gmNodeModel);
        }
        
        // GmObjectNode
        if (gmNodeModel instanceof GmObjectNode) {
            return new ObjectNodeDG(diagramHandle, gmNodeModel);
        }
        
        // GmOutputPin
        if (gmNodeModel instanceof GmOutputPin) {
            return new OutputPinDG(diagramHandle, gmNodeModel);
        }
        
        // GmPartition
        if (gmNodeModel instanceof GmPartition) {
            return new ActivityPartitionDG(diagramHandle, gmNodeModel);
        }
        
        // GmSendSignal
        if (gmNodeModel instanceof GmSendSignal) {
            return new SendSignalActionDG(diagramHandle, gmNodeModel);
        }
        
        // GmStructuredActivity
        if (gmNodeModel instanceof GmStructuredActivity) {
            return new StructuredActivityNodeDG(diagramHandle, gmNodeModel);
        }
        
        // GmTimeEvent
        if (gmNodeModel instanceof GmTimeEvent) {
            return new AcceptTimeEventActionDG(diagramHandle, gmNodeModel);
        }
        
        // GmValuePin
        if (gmNodeModel instanceof GmValuePin) {
            return new ValuePinDG(diagramHandle, gmNodeModel);
        }
        return null;
    }

    @objid ("3f9d7fd0-e733-479a-87f9-bbb9028428e0")
    private IDiagramLink getBpmnLink(final DiagramHandle diagramHandle, final IGmLink gmLink) {
        // GmBpmnDataAssociation
        if (gmLink instanceof GmBpmnDataAssociation) {
            return new BpmnDataAssociationDG(diagramHandle, gmLink);
        }
        
        // GmBpmnMessageFlow
        if (gmLink instanceof GmBpmnMessageFlow) {
            return new BpmnMessageFlowDG(diagramHandle, gmLink);
        }
        
        // GmBpmnSequenceFlow
        if (gmLink instanceof GmBpmnSequenceFlow) {
            return new BpmnSequenceFlowDG(diagramHandle, gmLink);
        }
        
        // This is not a bpmn link
        return null;
    }

    @objid ("9fe990eb-c5d9-462d-9853-3c31b36d5c4c")
    private IDiagramNode getBpmnNode(final DiagramHandle diagramHandle, final GmNodeModel gmNodeModel) {
        // GmBpmnAdHocSubProcess
        if (gmNodeModel instanceof GmBpmnAdHocSubProcess) {
            return new BpmnAdHocSubProcessDG(diagramHandle, gmNodeModel);
        }
        
        // GmBpmnBoundaryEvent
        if (gmNodeModel instanceof GmBpmnBoundaryEvent) {
            return new BpmnBoundaryEventDG(diagramHandle, gmNodeModel);
        }
        
        // GmBpmnBusinessRuleTask
        if (gmNodeModel instanceof GmBpmnBusinessRuleTask) {
            return new BpmnBusinessRuleTaskDG(diagramHandle, gmNodeModel);
        }
        
        // GmBpmnCallActivity
        if (gmNodeModel instanceof GmBpmnCallActivity) {
            return new BpmnCallActivityDG(diagramHandle, gmNodeModel);
        }
        
        // GmBpmnComplexGateway
        if (gmNodeModel instanceof GmBpmnComplexGateway) {
            return new BpmnComplexGatewayDG(diagramHandle, gmNodeModel);
        }
        
        // GmBpmnDataInput
        if (gmNodeModel instanceof GmBpmnDataInput) {
            return new BpmnDataInputDG(diagramHandle, gmNodeModel);
        }
        
        // GmBpmnDataObject
        if (gmNodeModel instanceof GmBpmnDataObject) {
            return new BpmnDataObjectDG(diagramHandle, gmNodeModel);
        }
        
        // GmBpmnDataOutput
        if (gmNodeModel instanceof GmBpmnDataOutput) {
            return new BpmnDataOutputDG(diagramHandle, gmNodeModel);
        }
        
        // GmBpmnDataStore
        if (gmNodeModel instanceof GmBpmnDataStore) {
            return new BpmnDataStoreDG(diagramHandle, gmNodeModel);
        }
        
        // GmBpmnEndEvent
        if (gmNodeModel instanceof GmBpmnEndEvent) {
            return new BpmnEndEventDG(diagramHandle, gmNodeModel);
        }
        
        // GmBpmnEventBasedGateway
        if (gmNodeModel instanceof GmBpmnEventBasedGateway) {
            return new BpmnEventBasedGatewayDG(diagramHandle, gmNodeModel);
        }
        
        // GmBpmnExclusiveGateway
        if (gmNodeModel instanceof GmBpmnExclusiveGateway) {
            return new BpmnExclusiveGatewayDG(diagramHandle, gmNodeModel);
        }
        
        // GmBpmnInclusiveGateway
        if (gmNodeModel instanceof GmBpmnInclusiveGateway) {
            return new BpmnInclusiveGatewayDG(diagramHandle, gmNodeModel);
        }
        
        // GmBpmnIntermediateCatchEvent
        if (gmNodeModel instanceof GmBpmnIntermediateCatchEvent) {
            return new BpmnIntermediateCatchEventDG(diagramHandle, gmNodeModel);
        }
        
        // GmBpmnIntermediateThrowEvent
        if (gmNodeModel instanceof GmBpmnIntermediateThrowEvent) {
            return new BpmnIntermediateThrowEventDG(diagramHandle, gmNodeModel);
        }
        
        // GmBpmnLane
        if (gmNodeModel instanceof GmBpmnLane) {
            return new BpmnLaneDG(diagramHandle, gmNodeModel);
        }
        
        // GmBpmnLaneSetContainer
        if (gmNodeModel instanceof GmBpmnLaneSetContainer) {
            return new BpmnLaneSetContainerDG(diagramHandle, gmNodeModel);
        }
        
        // GmBpmnManualTask
        if (gmNodeModel instanceof GmBpmnManualTask) {
            return new BpmnManualTaskDG(diagramHandle, gmNodeModel);
        }
        
        // GmBpmnMessage
        if (gmNodeModel instanceof GmBpmnMessage) {
            return new BpmnMessageDG(diagramHandle, gmNodeModel);
        }
        
        // GmBpmnNodeFooter
        if (gmNodeModel instanceof GmBpmnNodeFooter) {
            return new BpmnNodeFooterDG(diagramHandle, gmNodeModel);
        }
        
        // GmBpmnNodeHeader
        if (gmNodeModel instanceof GmBpmnNodeHeader) {
            return new BpmnNodeHeaderDG(diagramHandle, gmNodeModel);
        }
        
        // GmBpmnParallelGateway
        if (gmNodeModel instanceof GmBpmnParallelGateway) {
            return new BpmnParallelGatewayDG(diagramHandle, gmNodeModel);
        }
        
        // GmBpmnReceiveTask
        if (gmNodeModel instanceof GmBpmnReceiveTask) {
            return new BpmnReceiveTaskDG(diagramHandle, gmNodeModel);
        }
        
        // GmBpmnSendTask
        if (gmNodeModel instanceof GmBpmnSendTask) {
            return new BpmnSendTaskDG(diagramHandle, gmNodeModel);
        }
        
        // GmBpmnServiceTask
        if (gmNodeModel instanceof GmBpmnServiceTask) {
            return new BpmnServiceTaskDG(diagramHandle, gmNodeModel);
        }
        
        // GmBpmnSriptTask
        if (gmNodeModel instanceof GmBpmnScriptTask) {
            return new BpmnScriptTaskDG(diagramHandle, gmNodeModel);
        }
        
        // GmBpmnStartEvent
        if (gmNodeModel instanceof GmBpmnStartEvent) {
            return new BpmnStartEventDG(diagramHandle, gmNodeModel);
        }
        
        // GmBpmnSubProcess
        if (gmNodeModel instanceof GmBpmnSubProcess) {
            return new BpmnSubProcessDG(diagramHandle, gmNodeModel);
        }
        
        // GmBpmnTask
        if (gmNodeModel instanceof GmBpmnTask) {
            return new BpmnTaskDG(diagramHandle, gmNodeModel);
        }
        
        // GmBpmnTransaction
        if (gmNodeModel instanceof GmBpmnTransaction) {
            return new BpmnTransactionDG(diagramHandle, gmNodeModel);
        }
        
        // GmBpmnUserTask
        if (gmNodeModel instanceof GmBpmnUserTask) {
            return new BpmnUserTaskDG(diagramHandle, gmNodeModel);
        }
        
        // GmGmBpmnProcessCollaborationDiagram
        if (gmNodeModel instanceof GmBpmnProcessCollaborationDiagram) {
            return new BpmnProcessCollaborationDiagramDG(diagramHandle, gmNodeModel);
        }
        
        // GmGmBpmnSubProcessDiagram
        if (gmNodeModel instanceof GmBpmnSubProcessDiagram) {
            return new BpmnSubProcessDiagramDG(diagramHandle, gmNodeModel);
        }
        
        // This is not a bpmn node
        return null;
    }

    @objid ("093fc7a7-b692-4e8b-bace-394847d7b158")
    private IDiagramLink getCommonLink(final DiagramHandle diagramHandle, final IGmLink gmLink) {
        // GmDependency
        if (gmLink instanceof GmDependency) {
            return new DependencyDG(diagramHandle, gmLink);
        }
        
        // GmNamespaceUse
        if (gmLink instanceof GmNamespaceUse) {
            return new NamespaceUseDG(diagramHandle, gmLink);
        }
        
        // TODO GmUsage
        //    if (gmLink instanceof GmUsage) {
        //      return new UsageDG (diagramHandle, gmLink);
        //    }
        return null;
    }

    @objid ("d39e5ee5-9aba-4dfa-8017-bbf6607d3c20")
    private IDiagramNode getCommonNode(final DiagramHandle diagramHandle, final GmNodeModel gmNodeModel) {
        // GmConstraint
        if (gmNodeModel instanceof GmConstraintBody) {
            return new ConstraintDG(diagramHandle, gmNodeModel);
        }
        
        // GMNote
        if (gmNodeModel instanceof GmNote) {
            return new NoteDG(diagramHandle, gmNodeModel);
        }
        
        // GmElementLabel
        if (gmNodeModel instanceof GmElementLabel) {
            if (gmNodeModel.getRepresentedElement() != null) {
                return new LabelDG(diagramHandle, gmNodeModel);
            }
        }
        
        // GmDiagramHolder
        if (gmNodeModel instanceof GmDiagramHolder) {
            return new DiagramHolderDG(diagramHandle, gmNodeModel);
        }
        return null;
    }

    @objid ("b57ffcb1-a2e0-4122-91bd-8e2a359ffcee")
    private IDiagramLink getCommunicationLink(final DiagramHandle diagramHandle, final IGmLink gmLinkModel) {
        // GmCommunicationChannel
        if (gmLinkModel instanceof GmCommunicationChannel) {
            return new CommunicationChannelDG(diagramHandle, gmLinkModel);
        }
        return null;
    }

    @objid ("870f810f-0eef-4381-93d3-40bdfaafa72b")
    private IDiagramNode getCommunicationNode(final DiagramHandle diagramHandle, final GmNodeModel gmNodeModel) {
        // GmCommunicationDiagram
        if (gmNodeModel instanceof GmCommunicationDiagram) {
            return new CommunicationDiagramDG(diagramHandle, gmNodeModel);
        }
        
        // GmCommunicationNode
        if (gmNodeModel instanceof GmCommunicationNode) {
            return new CommunicationNodeDG(diagramHandle, gmNodeModel);
        }
        return null;
    }

    @objid ("d44449f7-3981-4bf8-b927-dc3b6c0f111b")
    private IDiagramLink getDeploymentLink(final DiagramHandle diagramHandle, final IGmLink gmLink) {
        // GmManifestation
        if (gmLink instanceof GmManifestation) {
            return new ManifestationDG(diagramHandle, gmLink);
        }
        return null;
    }

    @objid ("0742216e-7e97-4837-bc39-fce42a159cd0")
    private IDiagramNode getDeploymentNode(final DiagramHandle diagramHandle, final GmNodeModel gmNodeModel) {
        // GmDeploymentDiagram
        if (gmNodeModel instanceof GmDeploymentDiagram) {
            return new DeploymentDiagramDG(diagramHandle, gmNodeModel);
        }
        
        // GmArtifact
        if (gmNodeModel instanceof GmArtifact) {
            return new ArtifactDG(diagramHandle, gmNodeModel);
        }
        
        // GmNode
        if (gmNodeModel instanceof GmNode) {
            return new NodeDG(diagramHandle, gmNodeModel);
        }
        return null;
    }

    @objid ("0ae0f3c1-38ff-4afe-bc7c-0a6a61f7a898")
    private IDiagramNode getObjectNode(final DiagramHandle diagramHandle, final GmNodeModel gmNodeModel) {
        // GmCommunicationDiagram
        if (gmNodeModel instanceof GmObjectDiagram) {
            return new ObjectDiagramDG(diagramHandle, gmNodeModel);
        }
        return null;
    }

    @objid ("51b7a0c3-d471-49f7-83f4-2efc0498f1aa")
    private IDiagramNode getScopeNode(final DiagramHandle diagramHandle, final GmNodeModel gmNodeModel) {
        // Scope is a commercial plugin, we can't have a dependency towards it, so all tests are done with strings.
        
        // GmBusinessRule
        if (gmNodeModel.getClass()
                       .getName()
                       .equals("com.modeliosoft.modelio.scope.diagram.elements.businessrule.GmBusinessRule")) {
            return new BusinessRuleDG(diagramHandle, gmNodeModel);
        }
        
        // GmBusinessRuleContainer
        if (gmNodeModel.getClass()
                       .getName()
                       .equals("com.modeliosoft.modelio.scope.diagram.elements.businessrulecontainer.GmBusinessRuleContainer")) {
            return new BusinessRuleContainerDG(diagramHandle, gmNodeModel);
        }
        
        // GmDictionary
        if (gmNodeModel.getClass()
                       .getName()
                       .equals("com.modeliosoft.modelio.scope.diagram.elements.dictionary.GmDictionary")) {
            return new DictionaryDG(diagramHandle, gmNodeModel);
        }
        
        // GmGoal
        if (gmNodeModel.getClass()
                       .getName()
                       .equals("com.modeliosoft.modelio.scope.diagram.elements.goal.GmGoal")) {
            return new GoalDG(diagramHandle, gmNodeModel);
        }
        
        // GmGoalContainer
        if (gmNodeModel.getClass()
                       .getName()
                       .equals("com.modeliosoft.modelio.scope.diagram.elements.goalcontainer.GmGoalContainer")) {
            return new GoalContainerDG(diagramHandle, gmNodeModel);
        }
        
        // GmImpactDiagram
        if (gmNodeModel.getClass()
                       .getName()
                       .equals("com.modeliosoft.modelio.scope.diagram.elements.impactdiagram.GmImpactDiagram")) {
            return new ImpactDiagramDG(diagramHandle, gmNodeModel);
        }
        
        // GmPropertyValue
        if (gmNodeModel.getClass()
                       .getName()
                       .equals("com.modeliosoft.modelio.scope.diagram.elements.propertyvalue.GmPropertyValue")) {
            return new PropertyValueDG(diagramHandle, gmNodeModel);
        }
        
        // GmRequirement
        if (gmNodeModel.getClass()
                       .getName()
                       .equals("com.modeliosoft.modelio.scope.diagram.elements.requirement.GmRequirement")) {
            return new RequirementDG(diagramHandle, gmNodeModel);
        }
        
        // GmRequirementContainer
        if (gmNodeModel.getClass()
                       .getName()
                       .equals("com.modeliosoft.modelio.scope.diagram.elements.requirementcontainer.GmRequirementContainer")) {
            return new RequirementContainerDG(diagramHandle, gmNodeModel);
        }
        
        // GmScopeDiagram
        if (gmNodeModel.getClass()
                       .getName()
                       .equals("com.modeliosoft.modelio.scope.diagram.elements.scopediagram.GmScopeDiagram")) {
            return new ScopeDiagramDG(diagramHandle, gmNodeModel);
        }
        
        // GmTerm
        if (gmNodeModel.getClass()
                       .getName()
                       .equals("com.modeliosoft.modelio.scope.diagram.elements.term.GmTerm")) {
            return new TermDG(diagramHandle, gmNodeModel);
        }
        return null;
    }

    @objid ("6739bb53-62e3-4eba-a272-2ed1426b326b")
    private IDiagramLink getStateLink(final DiagramHandle diagramHandle, final IGmLink gmLinkModel) {
        // GmTransition
        if (gmLinkModel instanceof GmTransition) {
            return new TransitionDG(diagramHandle, gmLinkModel);
        }
        return null;
    }

    @objid ("70bb4dee-e334-41c2-a520-382ae0753e5d")
    private IDiagramNode getStateNode(final DiagramHandle diagramHandle, final GmNodeModel gmNodeModel) {
        // GmChoice
        if (gmNodeModel instanceof GmChoice) {
            return new ChoicePseudoStateDG(diagramHandle, gmNodeModel);
        }
        
        // GmConnectionPoint
        if (gmNodeModel instanceof GmConnectionPoint) {
            return new ConnectionPointReferenceDG(diagramHandle, gmNodeModel);
        }
        
        // GmDeepHistory
        if (gmNodeModel instanceof GmDeepHistory) {
            return new DeepHistoryPseudoStateDG(diagramHandle, gmNodeModel);
        }
        
        // GmEntry
        if (gmNodeModel instanceof GmEntry) {
            return new EntryPointPseudoStateDG(diagramHandle, gmNodeModel);
        }
        
        // GmExitPoint
        if (gmNodeModel instanceof GmExitPoint) {
            return new ExitPointPseudoStateDG(diagramHandle, gmNodeModel);
        }
        
        // GmFinalState
        if (gmNodeModel instanceof GmFinalState) {
            return new FinalStateDG(diagramHandle, gmNodeModel);
        }
        
        // GmForkState
        if (gmNodeModel instanceof GmForkState) {
            return new ForkPseudoStateDG(diagramHandle, gmNodeModel);
        }
        
        // GmInitialState
        if (gmNodeModel instanceof GmInitialState) {
            return new InitialPseudoStateDG(diagramHandle, gmNodeModel);
        }
        
        // GmInternalTransition
        if (gmNodeModel instanceof GmInternalTransition) {
            return new InternalTransitionDG(diagramHandle, gmNodeModel);
        }
        
        // GmJoin
        if (gmNodeModel instanceof GmJoin) {
            return new JoinPseudoStateDG(diagramHandle, gmNodeModel);
        }
        
        // GmJunction
        if (gmNodeModel instanceof GmJunction) {
            return new JunctionPseudoStateDG(diagramHandle, gmNodeModel);
        }
        
        // GmRegion
        if (gmNodeModel instanceof GmRegion) {
            return new RegionDG(diagramHandle, gmNodeModel);
        }
        
        // GmShallowHistory
        if (gmNodeModel instanceof GmShallowHistory) {
            return new ShallowHistoryPseudoStateDG(diagramHandle, gmNodeModel);
        }
        
        // GmState
        if (gmNodeModel instanceof GmState) {
            return new StateDG(diagramHandle, gmNodeModel);
        }
        
        // GmStateDiagram
        if (gmNodeModel instanceof GmStateDiagram) {
            return new StateDiagramDG(diagramHandle, gmNodeModel);
        }
        
        // GmTerminal
        if (gmNodeModel instanceof GmTerminal) {
            return new TerminatePseudoStateDG(diagramHandle, gmNodeModel);
        }
        return null;
    }

    @objid ("28d481c6-698f-464a-b4a5-5804d2012845")
    private IDiagramLink getStatikLink(final DiagramHandle diagramHandle, final IGmLink gmNodeModel) {
        // GmAssociation
        if (gmNodeModel instanceof GmAssociation) {
            return new AssociationDG(diagramHandle, gmNodeModel);
        }
        
        // GmBindingLink
        if (gmNodeModel instanceof GmBindingLink) {
            return new BindingLinkDG(diagramHandle, gmNodeModel);
        }
        
        // GmClassAssociation
        if (gmNodeModel instanceof GmClassAssociationLink) {
            return new ClassAssociationDG(diagramHandle, gmNodeModel);
        }
        
        // GmConnector
        if (gmNodeModel instanceof GmConnectorLink) {
            return new ConnectorDG(diagramHandle, gmNodeModel);
        }
        
        // GmElementImport
        if (gmNodeModel instanceof GmElementImport) {
            return new ElementImportDG(diagramHandle, gmNodeModel);
        }
        
        // GmGeneralization
        if (gmNodeModel instanceof GmGeneralization) {
            return new GeneralizationDG(diagramHandle, gmNodeModel);
        }
        
        // GmInformationFlowLink
        if (gmNodeModel instanceof GmInformationFlowLink) {
            return new InformationFlowDG(diagramHandle, gmNodeModel);
        }
        
        // GmInstanceLink
        if (gmNodeModel instanceof GmInstanceLink) {
            return new LinkDG(diagramHandle, gmNodeModel);
        }
        
        // GmInterfaceRealization
        if (gmNodeModel instanceof GmInterfaceRealization) {
            return new InterfaceRealizationDG(diagramHandle, gmNodeModel);
        }
        
        // GmPackageImport
        if (gmNodeModel instanceof GmPackageImport) {
            return new PackageImportDG(diagramHandle, gmNodeModel);
        }
        
        // GmPackageMerge
        if (gmNodeModel instanceof GmPackageMerge) {
            return new PackageMergeDG(diagramHandle, gmNodeModel);
        }
        
        // GmProvidedInterface
        if (gmNodeModel instanceof GmProvidedInterfaceLink) {
            return new ProvidedInterfaceDG(diagramHandle, gmNodeModel);
        }
        
        // GmRaisedException
        if (gmNodeModel instanceof GmRaisedException) {
            return new RaisedExceptionDG(diagramHandle, gmNodeModel);
        }
        
        // GmRequiredInterface
        if (gmNodeModel instanceof GmRequiredInterfaceLink) {
            return new RequiredInterfaceDG(diagramHandle, gmNodeModel);
        }
        
        // GmTemplateBinding
        if (gmNodeModel instanceof GmTemplateBinding) {
            return new TemplateBindingDG(diagramHandle, gmNodeModel);
        }
        return null;
    }

    @objid ("ec96cd33-f9e9-46d4-8ccd-2ea9a2413928")
    private IDiagramNode getStatikNode(final DiagramHandle diagramHandle, final GmNodeModel gmNodeModel) {
        // GmActivity
        if (gmNodeModel instanceof GmActivity) {
            return new ActivityDG(diagramHandle, gmNodeModel);
        }
        
        // GmAttribute
        if (gmNodeModel instanceof GmAttribute) {
            return new AttributeDG(diagramHandle, gmNodeModel);
        }
        
        // GmBindingLabel
        if (gmNodeModel instanceof GmBindingLabel) {
            return new BindingDG(diagramHandle, gmNodeModel);
        }
        
        // GmClass
        if (gmNodeModel instanceof GmClass) {
            return new ClassDG(diagramHandle, gmNodeModel);
        }
        
        // GmCollaboration
        if (gmNodeModel instanceof GmCollaboration) {
            return new CollaborationDG(diagramHandle, gmNodeModel);
        }
        
        // GmCollaborationUse
        if (gmNodeModel instanceof GmCollaborationUse) {
            return new CollaborationUseDG(diagramHandle, gmNodeModel);
        }
        
        if (gmNodeModel instanceof GmCommunicationInteraction) {
            return new CommunicationInteractionDG(diagramHandle, gmNodeModel);
        }
        
        // GmComponent
        if (gmNodeModel instanceof GmComponent) {
            return new ComponentDG(diagramHandle, gmNodeModel);
        }
        
        // GmDataType
        if (gmNodeModel instanceof GmDataType) {
            return new DataTypeDG(diagramHandle, gmNodeModel);
        }
        
        // TODO GmDelegateConnector
        //        if (gmNodeModel instanceof GmDelegateConnector) {
        //            return new DelegateConnectorDG(diagramHandle, gmNodeModel);
        //        }
        
        // GmEnum
        if (gmNodeModel instanceof GmEnum) {
            return new EnumerationDG(diagramHandle, gmNodeModel);
        }
        
        // GmEnumLitteral
        if (gmNodeModel instanceof GmEnumLitteral) {
            return new EnumerationLiteralDG(diagramHandle, gmNodeModel);
        }
        
        // GmInformationItem
        if (gmNodeModel instanceof GmInformationItem) {
            return new InformationItemDG(diagramHandle, gmNodeModel);
        }
        
        // GmInstance
        if (gmNodeModel instanceof GmInstance) {
            return new InstanceDG(diagramHandle, gmNodeModel);
        }
        
        if (gmNodeModel instanceof GmInteraction) {
            return new InteractionDG(diagramHandle, gmNodeModel);
        }
        
        // GmInterface
        if (gmNodeModel instanceof GmInterface) {
            return new InterfaceDG(diagramHandle, gmNodeModel);
        }
        
        // TODO GmModule
        //        if (gmNodeModel instanceof GmModule) {
        //            return new CollaborationUseDG(diagramHandle, gmNodeModel);
        //        }
        
        // GmOperation
        if (gmNodeModel instanceof GmOperation) {
            return new OperationDG(diagramHandle, gmNodeModel);
        }
        
        // GmPort
        if (gmNodeModel instanceof GmPort) {
            return new PortDG(diagramHandle, gmNodeModel);
        }
        
        // TODO GmProfile
        //        if (gmNodeModel instanceof GmProfile) {
        //            return new ProfileDG(diagramHandle, gmNodeModel);
        //        }
        
        // GmSignal
        if (gmNodeModel instanceof GmSignal) {
            return new SignalDG(diagramHandle, gmNodeModel);
        }
        
        // GmSlot
        if (gmNodeModel instanceof GmSlot) {
            return new AttributeLinkDG(diagramHandle, gmNodeModel);
        }
        
        // State Machine
        if (gmNodeModel instanceof GmStateMachine) {
            return new StateMachineDG(diagramHandle, gmNodeModel);
        }
        
        if (gmNodeModel instanceof GmBpmnProcess) {
            return new BpmnProcessDG(diagramHandle, gmNodeModel);
        }
        
        if (gmNodeModel instanceof GmBpmnBehavior) {
            return new BpmnBehaviorDG(diagramHandle, gmNodeModel);
        }
        
        // GmStaticDiagram
        if (gmNodeModel instanceof GmStaticDiagram) {
            return new StaticDiagramDG(diagramHandle, gmNodeModel);
        }
        
        // GmPackage
        if (gmNodeModel instanceof GmPackage) {
            return new PackageDG(diagramHandle, gmNodeModel);
        }
        return null;
    }

    @objid ("c57158de-9a84-495e-a93d-72dcb3fdf1fb")
    private IDiagramLink getUseCaseLink(final DiagramHandle diagramHandle, final IGmLink gmLinkModel) {
        // GmCommunicationChannel
        if (gmLinkModel instanceof GmCommunicationChannel) {
            return new AssociationDG(diagramHandle, gmLinkModel);
        }
        
        // GmUseCaseDependency
        if (gmLinkModel instanceof GmUseCaseDependency) {
            return new UseCaseDependencyDG(diagramHandle, gmLinkModel);
        }
        return null;
    }

    @objid ("4618b4cf-b433-4053-b16d-d20ef6832943")
    private IDiagramNode getUseCaseNode(final DiagramHandle diagramHandle, final GmNodeModel gmNodeModel) {
        // GmActor
        if (gmNodeModel instanceof GmActor) {
            return new ActorDG(diagramHandle, gmNodeModel);
        }
        
        // GmExtensionPoint
        if (gmNodeModel instanceof GmExtensionPoint) {
            return new ExtensionPointDG(diagramHandle, gmNodeModel);
        }
        
        // GmUseCase
        if (gmNodeModel instanceof GmUseCase) {
            return new UseCaseDG(diagramHandle, gmNodeModel);
        }
        
        // GmUseCaseDiagram
        if (gmNodeModel instanceof GmUseCaseDiagram) {
            return new UseCaseDiagramDG(diagramHandle, gmNodeModel);
        }
        
        // GmSystem
        if (gmNodeModel instanceof GmSystem) {
            return new SystemDG(diagramHandle, gmNodeModel);
        }
        return null;
    }

    @objid ("2b0ab9e9-7af4-4fe8-8a39-737de2ef721d")
    private IDiagramNode getSequenceNode(final DiagramHandle diagramHandle, final GmNodeModel gmNodeModel) {
        // GmLifeline
        if (gmNodeModel instanceof GmLifeline) {
            return new LifelineDG(diagramHandle, gmNodeModel);
        }
        
        // GmSequenceDiagram
        if (gmNodeModel instanceof GmSequenceDiagram) {
            return new SequenceDiagramDG(diagramHandle, gmNodeModel);
        }
        
        // GmExecutionOccurenceSpecification
        if (gmNodeModel instanceof GmExecutionOccurenceSpecification) {
            return new ExecutionOccurenceSpecificationDG(diagramHandle, gmNodeModel);
        }
        
        // GmExecutionSpecification
        if (gmNodeModel instanceof GmExecutionSpecification) {
            return new ExecutionSpecificationDG(diagramHandle, gmNodeModel);
        }
        
        // GmInteractionUse
        if (gmNodeModel instanceof GmInteractionUse) {
            return new InteractionUseDG(diagramHandle, gmNodeModel);
        }
        
        // GmGate
        if (gmNodeModel instanceof GmGate) {
            return new GateDG(diagramHandle, gmNodeModel);
        }
        
        // GmCombinedFragment
        if (gmNodeModel instanceof GmCombinedFragment) {
            return new CombinedFragmentDG(diagramHandle, gmNodeModel);
        }
        
        // GmInteractionOperand
        if (gmNodeModel instanceof GmInteractionOperand) {
            return new InteractionOperandDG(diagramHandle, gmNodeModel);
        }
        
        // GmStateInvariant
        if (gmNodeModel instanceof GmStateInvariant) {
            return new StateInvariantDG(diagramHandle, gmNodeModel);
        }
        return null;
    }

    @objid ("047f5a07-2a9f-41dd-9254-0067077aa4fa")
    private IDiagramLink getSequenceLink(final DiagramHandle diagramHandle, final IGmLink gmLinkModel) {
        // GmMessage
        if (gmLinkModel instanceof GmMessage) {
            return new MessageDG(diagramHandle, gmLinkModel);
        }
        return null;
    }

    /**
     * Returns a DiagramGraphic for the given model. Can be either a DiagramNode or a DiagramLink.
     * @param diagramHandle a handle to the diagram
     * @param gmModel the model.
     * @return a {@link DiagramGraphic}
     */
    @objid ("3ca91567-1b18-4cdf-96bc-9967012ecaa8")
    public IDiagramGraphic getDiagramDrawingGraphic(DiagramHandle diagramHandle, IGmDrawing gmModel) {
        // Drawing links
        if (gmModel instanceof GmLineDrawing)
            return getDiagramLink(diagramHandle, (IGmDrawingLink) gmModel);
        
        // Drawing nodes
        if (gmModel instanceof GmEllipseDrawing)
            return new DiagramDrawingNodeDG(diagramHandle, (IGmNodeDrawing) gmModel);
        
        if (gmModel instanceof GmRectangleDrawing)
            return new DiagramDrawingNodeDG(diagramHandle, (IGmNodeDrawing) gmModel);
        
        if (gmModel instanceof GmTextDrawing)
            return new DiagramDrawingNodeDG(diagramHandle, (IGmNodeDrawing) gmModel);
        
        if (gmModel instanceof IGmDrawingLayer)
            return new DiagramDrawingLayerDG(diagramHandle, (IGmDrawingLayer) gmModel);
        return null;
    }

    /**
     * Return a list of {@link IDiagramLayer} for each given model.
     * @param diagramHandle a handle to the diagram
     * @param models the models
     * @return a list of {@link IDiagramLayer}
     */
    @objid ("44fd8b16-ce0a-44ba-a7e7-6b4d240a9cb2")
    public List<IDiagramLayer> getDiagramLayers(DiagramHandle diagramHandle, List<IGmDrawingLayer> models) {
        List<IDiagramLayer> list = new ArrayList<>();
        for (IGmDrawingLayer gm : models) {
            IDiagramLayer diagramNode = this.getDiagramLayer(diagramHandle, gm);
            if (diagramNode != null) {
                list.add(diagramNode);
            }
        }
        return list;
    }

    /**
     * Returns a {@link IDiagramLayer} for the given model.
     * @param diagramHandle the diagram in which the model is shown.
     * @param gm the model.
     * @return a {@link IDiagramLayer}
     */
    @objid ("c5f5db05-572a-49e3-b7fc-c5de750a8245")
    public IDiagramLayer getDiagramLayer(DiagramHandle diagramHandle, IGmDrawingLayer gm) {
        return new DiagramDrawingLayerDG(diagramHandle, gm);
    }

    /**
     * Returns a {@link IDiagramLink} for the given drawing link model.
     * @param diagramHandle the diagram in which the model is shown.
     * @param gmLinkModel the drawing link model.
     * @return a {@link IDiagramLink}
     */
    @objid ("4cc1c832-4eba-44bb-839d-833a7fc978a6")
    public IDiagramLink getDiagramLink(DiagramHandle diagramHandle, IGmDrawingLink gmLinkModel) {
        if (gmLinkModel instanceof GmLineDrawing) {
            return new DiagramDrawingLinkDG(diagramHandle, gmLinkModel);
        }
        return null;
    }

}
