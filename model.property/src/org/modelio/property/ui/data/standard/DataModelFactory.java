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
                                    

package org.modelio.property.ui.data.standard;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.app.core.activation.IActivationService;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.core.ui.ktable.IPropertyModel;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.analyst.AnalystProject;
import org.modelio.metamodel.analyst.BusinessRule;
import org.modelio.metamodel.analyst.BusinessRuleContainer;
import org.modelio.metamodel.analyst.Dictionary;
import org.modelio.metamodel.analyst.GenericAnalystContainer;
import org.modelio.metamodel.analyst.GenericAnalystElement;
import org.modelio.metamodel.analyst.Goal;
import org.modelio.metamodel.analyst.GoalContainer;
import org.modelio.metamodel.analyst.Requirement;
import org.modelio.metamodel.analyst.RequirementContainer;
import org.modelio.metamodel.analyst.Term;
import org.modelio.metamodel.bpmn.activities.BpmnActivity;
import org.modelio.metamodel.bpmn.activities.BpmnAdHocSubProcess;
import org.modelio.metamodel.bpmn.activities.BpmnBusinessRuleTask;
import org.modelio.metamodel.bpmn.activities.BpmnCallActivity;
import org.modelio.metamodel.bpmn.activities.BpmnComplexBehaviorDefinition;
import org.modelio.metamodel.bpmn.activities.BpmnLoopCharacteristics;
import org.modelio.metamodel.bpmn.activities.BpmnManualTask;
import org.modelio.metamodel.bpmn.activities.BpmnMultiInstanceLoopCharacteristics;
import org.modelio.metamodel.bpmn.activities.BpmnReceiveTask;
import org.modelio.metamodel.bpmn.activities.BpmnScriptTask;
import org.modelio.metamodel.bpmn.activities.BpmnSendTask;
import org.modelio.metamodel.bpmn.activities.BpmnServiceTask;
import org.modelio.metamodel.bpmn.activities.BpmnStandardLoopCharacteristics;
import org.modelio.metamodel.bpmn.activities.BpmnSubProcess;
import org.modelio.metamodel.bpmn.activities.BpmnTask;
import org.modelio.metamodel.bpmn.activities.BpmnTransaction;
import org.modelio.metamodel.bpmn.activities.BpmnUserTask;
import org.modelio.metamodel.bpmn.bpmnDiagrams.BpmnProcessCollaborationDiagram;
import org.modelio.metamodel.bpmn.bpmnDiagrams.BpmnSubProcessDiagram;
import org.modelio.metamodel.bpmn.bpmnService.BpmnEndPoint;
import org.modelio.metamodel.bpmn.bpmnService.BpmnInterface;
import org.modelio.metamodel.bpmn.bpmnService.BpmnOperation;
import org.modelio.metamodel.bpmn.events.BpmnBoundaryEvent;
import org.modelio.metamodel.bpmn.events.BpmnCancelEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnCatchEvent;
import org.modelio.metamodel.bpmn.events.BpmnCompensateEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnConditionalEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnEndEvent;
import org.modelio.metamodel.bpmn.events.BpmnErrorEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnEscalationEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnEvent;
import org.modelio.metamodel.bpmn.events.BpmnEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnIntermediateCatchEvent;
import org.modelio.metamodel.bpmn.events.BpmnIntermediateThrowEvent;
import org.modelio.metamodel.bpmn.events.BpmnLinkEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnMessageEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnSignalEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnStartEvent;
import org.modelio.metamodel.bpmn.events.BpmnTerminateEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnThrowEvent;
import org.modelio.metamodel.bpmn.events.BpmnTimerEventDefinition;
import org.modelio.metamodel.bpmn.flows.BpmnMessage;
import org.modelio.metamodel.bpmn.flows.BpmnMessageFlow;
import org.modelio.metamodel.bpmn.flows.BpmnSequenceFlow;
import org.modelio.metamodel.bpmn.gateways.BpmnComplexGateway;
import org.modelio.metamodel.bpmn.gateways.BpmnEventBasedGateway;
import org.modelio.metamodel.bpmn.gateways.BpmnExclusiveGateway;
import org.modelio.metamodel.bpmn.gateways.BpmnGateway;
import org.modelio.metamodel.bpmn.gateways.BpmnInclusiveGateway;
import org.modelio.metamodel.bpmn.gateways.BpmnParallelGateway;
import org.modelio.metamodel.bpmn.objects.BpmnDataAssociation;
import org.modelio.metamodel.bpmn.objects.BpmnDataInput;
import org.modelio.metamodel.bpmn.objects.BpmnDataObject;
import org.modelio.metamodel.bpmn.objects.BpmnDataOutput;
import org.modelio.metamodel.bpmn.objects.BpmnDataState;
import org.modelio.metamodel.bpmn.objects.BpmnDataStore;
import org.modelio.metamodel.bpmn.objects.BpmnItemAwareElement;
import org.modelio.metamodel.bpmn.objects.BpmnItemDefinition;
import org.modelio.metamodel.bpmn.objects.BpmnSequenceFlowDataAssociation;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnCollaboration;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLane;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLaneSet;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnParticipant;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnProcess;
import org.modelio.metamodel.bpmn.resources.BpmnResource;
import org.modelio.metamodel.bpmn.resources.BpmnResourceParameter;
import org.modelio.metamodel.bpmn.resources.BpmnResourceParameterBinding;
import org.modelio.metamodel.bpmn.resources.BpmnResourceRole;
import org.modelio.metamodel.bpmn.rootElements.BpmnArtifact;
import org.modelio.metamodel.bpmn.rootElements.BpmnBehavior;
import org.modelio.metamodel.bpmn.rootElements.BpmnFlowElement;
import org.modelio.metamodel.bpmn.rootElements.BpmnFlowNode;
import org.modelio.metamodel.bpmn.rootElements.BpmnGroup;
import org.modelio.metamodel.bpmn.rootElements.BpmnRootElement;
import org.modelio.metamodel.diagrams.ActivityDiagram;
import org.modelio.metamodel.diagrams.ClassDiagram;
import org.modelio.metamodel.diagrams.CommunicationDiagram;
import org.modelio.metamodel.diagrams.CompositeStructureDiagram;
import org.modelio.metamodel.diagrams.DeploymentDiagram;
import org.modelio.metamodel.diagrams.DiagramSet;
import org.modelio.metamodel.diagrams.ObjectDiagram;
import org.modelio.metamodel.diagrams.SequenceDiagram;
import org.modelio.metamodel.diagrams.StateMachineDiagram;
import org.modelio.metamodel.diagrams.StaticDiagram;
import org.modelio.metamodel.diagrams.UseCaseDiagram;
import org.modelio.metamodel.mda.ModuleComponent;
import org.modelio.metamodel.mda.ModuleParameter;
import org.modelio.metamodel.mda.Project;
import org.modelio.metamodel.uml.behavior.activityModel.AcceptCallEventAction;
import org.modelio.metamodel.uml.behavior.activityModel.AcceptChangeEventAction;
import org.modelio.metamodel.uml.behavior.activityModel.AcceptSignalAction;
import org.modelio.metamodel.uml.behavior.activityModel.AcceptTimeEventAction;
import org.modelio.metamodel.uml.behavior.activityModel.Activity;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityFinalNode;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityParameterNode;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityPartition;
import org.modelio.metamodel.uml.behavior.activityModel.CallBehaviorAction;
import org.modelio.metamodel.uml.behavior.activityModel.CallOperationAction;
import org.modelio.metamodel.uml.behavior.activityModel.CentralBufferNode;
import org.modelio.metamodel.uml.behavior.activityModel.Clause;
import org.modelio.metamodel.uml.behavior.activityModel.ConditionalNode;
import org.modelio.metamodel.uml.behavior.activityModel.ControlFlow;
import org.modelio.metamodel.uml.behavior.activityModel.DataStoreNode;
import org.modelio.metamodel.uml.behavior.activityModel.DecisionMergeNode;
import org.modelio.metamodel.uml.behavior.activityModel.ExceptionHandler;
import org.modelio.metamodel.uml.behavior.activityModel.ExpansionNode;
import org.modelio.metamodel.uml.behavior.activityModel.ExpansionRegion;
import org.modelio.metamodel.uml.behavior.activityModel.FlowFinalNode;
import org.modelio.metamodel.uml.behavior.activityModel.ForkJoinNode;
import org.modelio.metamodel.uml.behavior.activityModel.InitialNode;
import org.modelio.metamodel.uml.behavior.activityModel.InputPin;
import org.modelio.metamodel.uml.behavior.activityModel.InstanceNode;
import org.modelio.metamodel.uml.behavior.activityModel.InterruptibleActivityRegion;
import org.modelio.metamodel.uml.behavior.activityModel.LoopNode;
import org.modelio.metamodel.uml.behavior.activityModel.MessageFlow;
import org.modelio.metamodel.uml.behavior.activityModel.ObjectFlow;
import org.modelio.metamodel.uml.behavior.activityModel.OpaqueAction;
import org.modelio.metamodel.uml.behavior.activityModel.OutputPin;
import org.modelio.metamodel.uml.behavior.activityModel.SendSignalAction;
import org.modelio.metamodel.uml.behavior.activityModel.StructuredActivityNode;
import org.modelio.metamodel.uml.behavior.activityModel.ValuePin;
import org.modelio.metamodel.uml.behavior.commonBehaviors.BehaviorParameter;
import org.modelio.metamodel.uml.behavior.commonBehaviors.Event;
import org.modelio.metamodel.uml.behavior.commonBehaviors.OpaqueBehavior;
import org.modelio.metamodel.uml.behavior.commonBehaviors.Signal;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationChannel;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationInteraction;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationMessage;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationNode;
import org.modelio.metamodel.uml.behavior.interactionModel.CombinedFragment;
import org.modelio.metamodel.uml.behavior.interactionModel.DurationConstraint;
import org.modelio.metamodel.uml.behavior.interactionModel.ExecutionOccurenceSpecification;
import org.modelio.metamodel.uml.behavior.interactionModel.ExecutionSpecification;
import org.modelio.metamodel.uml.behavior.interactionModel.Gate;
import org.modelio.metamodel.uml.behavior.interactionModel.GeneralOrdering;
import org.modelio.metamodel.uml.behavior.interactionModel.Interaction;
import org.modelio.metamodel.uml.behavior.interactionModel.InteractionOperand;
import org.modelio.metamodel.uml.behavior.interactionModel.InteractionUse;
import org.modelio.metamodel.uml.behavior.interactionModel.Lifeline;
import org.modelio.metamodel.uml.behavior.interactionModel.Message;
import org.modelio.metamodel.uml.behavior.interactionModel.PartDecomposition;
import org.modelio.metamodel.uml.behavior.interactionModel.StateInvariant;
import org.modelio.metamodel.uml.behavior.stateMachineModel.ChoicePseudoState;
import org.modelio.metamodel.uml.behavior.stateMachineModel.ConnectionPointReference;
import org.modelio.metamodel.uml.behavior.stateMachineModel.DeepHistoryPseudoState;
import org.modelio.metamodel.uml.behavior.stateMachineModel.EntryPointPseudoState;
import org.modelio.metamodel.uml.behavior.stateMachineModel.ExitPointPseudoState;
import org.modelio.metamodel.uml.behavior.stateMachineModel.FinalState;
import org.modelio.metamodel.uml.behavior.stateMachineModel.ForkPseudoState;
import org.modelio.metamodel.uml.behavior.stateMachineModel.InitialPseudoState;
import org.modelio.metamodel.uml.behavior.stateMachineModel.InternalTransition;
import org.modelio.metamodel.uml.behavior.stateMachineModel.JoinPseudoState;
import org.modelio.metamodel.uml.behavior.stateMachineModel.JunctionPseudoState;
import org.modelio.metamodel.uml.behavior.stateMachineModel.ShallowHistoryPseudoState;
import org.modelio.metamodel.uml.behavior.stateMachineModel.State;
import org.modelio.metamodel.uml.behavior.stateMachineModel.StateMachine;
import org.modelio.metamodel.uml.behavior.stateMachineModel.TerminatePseudoState;
import org.modelio.metamodel.uml.behavior.stateMachineModel.Transition;
import org.modelio.metamodel.uml.behavior.usecaseModel.Actor;
import org.modelio.metamodel.uml.behavior.usecaseModel.ExtensionPoint;
import org.modelio.metamodel.uml.behavior.usecaseModel.UseCase;
import org.modelio.metamodel.uml.behavior.usecaseModel.UseCaseDependency;
import org.modelio.metamodel.uml.informationFlow.DataFlow;
import org.modelio.metamodel.uml.informationFlow.InformationFlow;
import org.modelio.metamodel.uml.informationFlow.InformationItem;
import org.modelio.metamodel.uml.infrastructure.Abstraction;
import org.modelio.metamodel.uml.infrastructure.Constraint;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.ExternDocument;
import org.modelio.metamodel.uml.infrastructure.MetaclassReference;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.metamodel.uml.infrastructure.NoteType;
import org.modelio.metamodel.uml.infrastructure.Profile;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.infrastructure.Substitution;
import org.modelio.metamodel.uml.infrastructure.TagType;
import org.modelio.metamodel.uml.infrastructure.Usage;
import org.modelio.metamodel.uml.infrastructure.matrix.MatrixDefinition;
import org.modelio.metamodel.uml.infrastructure.matrix.MatrixValueDefinition;
import org.modelio.metamodel.uml.infrastructure.matrix.QueryDefinition;
import org.modelio.metamodel.uml.infrastructure.properties.EnumeratedPropertyType;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyDefinition;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyEnumerationLitteral;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyTableDefinition;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyType;
import org.modelio.metamodel.uml.infrastructure.properties.TypedPropertyTable;
import org.modelio.metamodel.uml.statik.Artifact;
import org.modelio.metamodel.uml.statik.Association;
import org.modelio.metamodel.uml.statik.AssociationEnd;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.AttributeLink;
import org.modelio.metamodel.uml.statik.BindableInstance;
import org.modelio.metamodel.uml.statik.Binding;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.ClassAssociation;
import org.modelio.metamodel.uml.statik.Collaboration;
import org.modelio.metamodel.uml.statik.CollaborationUse;
import org.modelio.metamodel.uml.statik.Component;
import org.modelio.metamodel.uml.statik.ComponentRealization;
import org.modelio.metamodel.uml.statik.Connector;
import org.modelio.metamodel.uml.statik.ConnectorEnd;
import org.modelio.metamodel.uml.statik.DataType;
import org.modelio.metamodel.uml.statik.ElementImport;
import org.modelio.metamodel.uml.statik.ElementRealization;
import org.modelio.metamodel.uml.statik.Enumeration;
import org.modelio.metamodel.uml.statik.EnumerationLiteral;
import org.modelio.metamodel.uml.statik.Generalization;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.metamodel.uml.statik.Interface;
import org.modelio.metamodel.uml.statik.InterfaceRealization;
import org.modelio.metamodel.uml.statik.Link;
import org.modelio.metamodel.uml.statik.LinkEnd;
import org.modelio.metamodel.uml.statik.Manifestation;
import org.modelio.metamodel.uml.statik.NamespaceUse;
import org.modelio.metamodel.uml.statik.NaryAssociation;
import org.modelio.metamodel.uml.statik.NaryLink;
import org.modelio.metamodel.uml.statik.NaryLinkEnd;
import org.modelio.metamodel.uml.statik.Node;
import org.modelio.metamodel.uml.statik.Operation;
import org.modelio.metamodel.uml.statik.Package;
import org.modelio.metamodel.uml.statik.PackageImport;
import org.modelio.metamodel.uml.statik.PackageMerge;
import org.modelio.metamodel.uml.statik.Parameter;
import org.modelio.metamodel.uml.statik.Port;
import org.modelio.metamodel.uml.statik.ProvidedInterface;
import org.modelio.metamodel.uml.statik.RaisedException;
import org.modelio.metamodel.uml.statik.RequiredInterface;
import org.modelio.metamodel.uml.statik.TemplateBinding;
import org.modelio.metamodel.uml.statik.TemplateParameter;
import org.modelio.metamodel.uml.statik.TemplateParameterSubstitution;
import org.modelio.metamodel.visitors.DefaultModelVisitor;
import org.modelio.property.ui.data.standard.analyst.AnalystProjectPropertyModel;
import org.modelio.property.ui.data.standard.analyst.BusinessRuleContainerPropertyModel;
import org.modelio.property.ui.data.standard.analyst.BusinessRulePropertyModel;
import org.modelio.property.ui.data.standard.analyst.DictionaryPropertyModel;
import org.modelio.property.ui.data.standard.analyst.GenericAnalystContainerPropertyModel;
import org.modelio.property.ui.data.standard.analyst.GenericAnalystElementPropertyModel;
import org.modelio.property.ui.data.standard.analyst.GoalContainerPropertyModel;
import org.modelio.property.ui.data.standard.analyst.GoalPropertyModel;
import org.modelio.property.ui.data.standard.analyst.RequirementContainerPropertyModel;
import org.modelio.property.ui.data.standard.analyst.RequirementPropertyModel;
import org.modelio.property.ui.data.standard.analyst.TermPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnActivityPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnAdHocSubProcessPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnArtifactPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnBehaviorPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnBoundaryEventPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnBusinessRuleTaskPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnCallActivityPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnCancelEventDefinitionPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnCatchEventPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnCollaborationPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnCompensateEventDefinitionPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnComplexBehaviorDefinitionPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnComplexGatewayPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnConditionalEventDefinitionPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnDataAssociationPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnDataInputPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnDataObjectPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnDataOutputPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnDataStatePropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnDataStorePropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnEndEventPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnEndPointPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnErrorEventDefinitionPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnEscalationEventDefinitionPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnEventBasedGatewayPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnEventDefinitionPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnEventPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnExclusiveGatewayPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnFlowElementPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnFlowNodePropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnGatewayPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnGroupPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnInclusiveGatewayPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnInterfacePropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnIntermediateCatchEventPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnIntermediateThrowEventPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnItemAwareElementPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnItemDefinitionPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnLanePropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnLaneSetPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnLinkEventDefinitionPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnLoopCharacteristicsPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnManualTaskPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnMessageEventDefinitionPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnMessageFlowPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnMessagePropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnMultiInstanceLoopCharacteristicsPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnOperationPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnParallelGatewayPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnParticipantPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnProcessCollaborationDiagramPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnProcessPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnReceiveTaskPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnResourceParameterBindingPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnResourceParameterPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnResourcePropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnResourceRolePropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnRootElementPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnScriptTaskPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnSendTaskPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnSequenceFlowDataAssociationPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnSequenceFlowPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnServiceTaskPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnSignalEventDefinitionPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnStandardLoopCharacteristicsPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnStartEventPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnSubProcessDiagramPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnSubProcessPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnTaskPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnTerminateEventDefinitionPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnThrowEventPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnTimerEventDefinitionPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnTransactionPropertyModel;
import org.modelio.property.ui.data.standard.bpmn.BpmnUserTaskPropertyModel;
import org.modelio.property.ui.data.standard.uml.*;
import org.modelio.property.ui.data.standard.uml.templateparameter.TemplateParameterPropertyModel;
import org.modelio.vcore.session.api.model.IModel;
import org.modelio.vcore.smkernel.SmObjectImpl;

/**
 * Property view data model factory.
 * <p>
 * Provides the data model matching a given model element.
 */
@objid ("8e67b6eb-c068-11e1-8c0a-002564c97630")
class DataModelFactory extends DefaultModelVisitor {
    @objid ("aa96e6b4-d004-11e1-9020-002564c97630")
    private IModel model;

    @objid ("aa96e6b5-d004-11e1-9020-002564c97630")
    private IMModelServices modelService;

    @objid ("c2f9e4d4-7127-4121-a1fa-b1557754c54d")
    private IActivationService activationService;

    @objid ("5fe9b3de-6a50-4410-a9bc-f7028e4eee97")
    private IProjectService projectService;

    @objid ("aa96e6b6-d004-11e1-9020-002564c97630")
    public DataModelFactory(IMModelServices modelService, IProjectService projectService, IActivationService activationService, IModel model) {
        this.activationService = activationService;
        this.model = model;
        this.modelService = modelService;
        this.projectService = projectService;
    }

    /**
     * Provides the data model matching a given model element.
     * @param element The element to display in the property view.
     * @return The matching property model.
     */
    @objid ("8e693d48-c068-11e1-8c0a-002564c97630")
    public IPropertyModel getPropertyModel(Element element) {
        if (element != null) {
            // TODO handle dynamic metaclasses
            //if (element.getMClass().isDynamic) {
            //return new DynamicPropertyModel(element);
            //} else {
              return (IPropertyModel) ((SmObjectImpl)element).accept(this);
            //}
        }
        // No element means no property model
        return null;
    }

    @objid ("8e693d50-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitAbstraction(Abstraction theAbstraction) {
        return new AbstractionPropertyModel(theAbstraction);
    }

    @objid ("8e693d58-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitAcceptCallEventAction(AcceptCallEventAction theAcceptCallEventAction) {
        return new AcceptCallEventActionPropertyModel(theAcceptCallEventAction);
    }

    @objid ("8e693d60-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitAcceptChangeEventAction(AcceptChangeEventAction theAcceptChangeEventAction) {
        return new AcceptChangeEventActionPropertyModel(theAcceptChangeEventAction);
    }

    @objid ("8e693d68-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitAcceptSignalAction(AcceptSignalAction theAcceptSignalAction) {
        return new AcceptSignalActionPropertyModel(theAcceptSignalAction, this.model);
    }

    @objid ("8e693d70-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitAcceptTimeEventAction(AcceptTimeEventAction theAcceptTimeEventAction) {
        return new AcceptTimeEventActionPropertyModel(theAcceptTimeEventAction);
    }

    @objid ("8e693d78-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitActivity(Activity theActivity) {
        return new ActivityPropertyModel(theActivity);
    }

    @objid ("8e693d80-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitActivityDiagram(ActivityDiagram theActivityDiagram) {
        return new ActivityDiagramPropertyModel(theActivityDiagram);
    }

    @objid ("8e693d88-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitActivityFinalNode(ActivityFinalNode theActivityFinalNode) {
        return new ActivityFinalNodePropertyModel(theActivityFinalNode);
    }

    @objid ("8e6ac3ec-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitActivityParameterNode(ActivityParameterNode theActivityParameterNode) {
        return new ActivityParameterNodePropertyModel(theActivityParameterNode);
    }

    @objid ("8e6ac3f4-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitActivityPartition(ActivityPartition theActivityPartition) {
        return new ActivityPartitionPropertyModel(theActivityPartition);
    }

    @objid ("8e6ac3fc-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitActor(Actor theActor) {
        return new ActorPropertyModel(theActor);
    }

    @objid ("8e6ac404-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitAnalystProject(AnalystProject theAnalystProject) {
        return new AnalystProjectPropertyModel(theAnalystProject);
    }

    @objid ("8e6ac40c-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitArtifact(Artifact theArtifact) {
        return new ArtifactPropertyModel(theArtifact);
    }

    @objid ("8e6ac414-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitAssociationEnd(AssociationEnd theAssociationEnd) {
        return new AssociationEnd2PropertyModel(theAssociationEnd);
    }

    @objid ("8e6ac424-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitAttribute(Attribute theAttribute) {
        return new AttributePropertyModel(theAttribute);
    }

    @objid ("8e6c4a85-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitAttributeLink(AttributeLink theAttributeLink) {
        return new AttributeLinkPropertyModel(theAttributeLink, this.model);
    }

    @objid ("8e6c4a8d-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBehaviorParameter(BehaviorParameter theBehaviorParameter) {
        return new BehaviorParameterPropertyModel(theBehaviorParameter);
    }

    @objid ("8e6c4a95-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBindableInstance(BindableInstance theBindableInstance) {
        return new BindableInstancePropertyModel(theBindableInstance);
    }

    @objid ("8e6c4a9d-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBinding(Binding theBinding) {
        return new BindingPropertyModel(theBinding);
    }

    @objid ("8e6c4aa5-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnActivity(BpmnActivity theBpmnActivity) {
        return new BpmnActivityPropertyModel(theBpmnActivity);
    }

    @objid ("8e6c4aad-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnAdHocSubProcess(BpmnAdHocSubProcess theBpmnAdHocSubProcess) {
        return new BpmnAdHocSubProcessPropertyModel(theBpmnAdHocSubProcess, this.modelService, this.model);
    }

    @objid ("8e6c4ab5-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnArtifact(BpmnArtifact theBpmnArtifact) {
        return new BpmnArtifactPropertyModel(theBpmnArtifact);
    }

    @objid ("8e6c4abd-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnBehavior(BpmnBehavior theBpmnBehavior) {
        return new BpmnBehaviorPropertyModel(theBpmnBehavior);
    }

    @objid ("8e6c4ac5-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnBoundaryEvent(BpmnBoundaryEvent theBpmnBoundaryEvent) {
        return new BpmnBoundaryEventPropertyModel(theBpmnBoundaryEvent, this.modelService);
    }

    @objid ("8e6dd12c-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnBusinessRuleTask(BpmnBusinessRuleTask theBpmnBusinessRuleTask) {
        return new BpmnBusinessRuleTaskPropertyModel(theBpmnBusinessRuleTask, this.modelService, this.model);
    }

    @objid ("8e6dd134-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnCallActivity(BpmnCallActivity theBpmnCallActivity) {
        return new BpmnCallActivityPropertyModel(theBpmnCallActivity, this.modelService, this.model);
    }

    @objid ("8e6dd13c-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnCancelEventDefinition(BpmnCancelEventDefinition theBpmnCancelEventDefinition) {
        return new BpmnCancelEventDefinitionPropertyModel(theBpmnCancelEventDefinition);
    }

    @objid ("8e6dd144-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnCatchEvent(BpmnCatchEvent theBpmnCatchEvent) {
        return new BpmnCatchEventPropertyModel(theBpmnCatchEvent);
    }

    @objid ("8e6dd14c-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnCollaboration(BpmnCollaboration theBpmnCollaboration) {
        return new BpmnCollaborationPropertyModel(theBpmnCollaboration);
    }

    @objid ("8e6dd154-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnCompensateEventDefinition(BpmnCompensateEventDefinition theBpmnCompensateEventDefinition) {
        return new BpmnCompensateEventDefinitionPropertyModel(theBpmnCompensateEventDefinition, this.model);
    }

    @objid ("8e6dd15c-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnComplexBehaviorDefinition(BpmnComplexBehaviorDefinition theBpmnComplexBehaviorDefinition) {
        return new BpmnComplexBehaviorDefinitionPropertyModel(theBpmnComplexBehaviorDefinition);
    }

    @objid ("8e6f57c5-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnComplexGateway(BpmnComplexGateway theBpmnComplexGateway) {
        return new BpmnComplexGatewayPropertyModel(theBpmnComplexGateway);
    }

    @objid ("8e6f57cd-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnConditionalEventDefinition(BpmnConditionalEventDefinition theBpmnConditionalEventDefinition) {
        return new BpmnConditionalEventDefinitionPropertyModel(theBpmnConditionalEventDefinition);
    }

    @objid ("8e6f57d5-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnDataAssociation(BpmnDataAssociation theBpmnDataAssociation) {
        return new BpmnDataAssociationPropertyModel(theBpmnDataAssociation);
    }

    @objid ("8e6f57dd-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnDataInput(BpmnDataInput theBpmnDataInput) {
        return new BpmnDataInputPropertyModel(theBpmnDataInput, this.model);
    }

    @objid ("8e6f57e5-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnDataObject(BpmnDataObject theBpmnDataObject) {
        return new BpmnDataObjectPropertyModel(theBpmnDataObject, this.model);
    }

    @objid ("8e6f57ed-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnDataOutput(BpmnDataOutput theBpmnDataOutput) {
        return new BpmnDataOutputPropertyModel(theBpmnDataOutput, this.model);
    }

    @objid ("8e6f57f5-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnDataState(BpmnDataState theBpmnDataState) {
        return new BpmnDataStatePropertyModel(theBpmnDataState, this.model);
    }

    @objid ("8e6f57fd-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnDataStore(BpmnDataStore theBpmnDataStore) {
        return new BpmnDataStorePropertyModel(theBpmnDataStore, this.model);
    }

    @objid ("8e70de67-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnEndEvent(BpmnEndEvent theBpmnEndEvent) {
        return new BpmnEndEventPropertyModel(theBpmnEndEvent, this.modelService);
    }

    @objid ("8e70de6f-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnEndPoint(BpmnEndPoint theBpmnEndPoint) {
        return new BpmnEndPointPropertyModel(theBpmnEndPoint);
    }

    @objid ("8e70de77-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnErrorEventDefinition(BpmnErrorEventDefinition theBpmnErrorEventDefinition) {
        return new BpmnErrorEventDefinitionPropertyModel(theBpmnErrorEventDefinition);
    }

    @objid ("8e70de7f-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnEscalationEventDefinition(BpmnEscalationEventDefinition theBpmnEscalationEventDefinition) {
        return new BpmnEscalationEventDefinitionPropertyModel(theBpmnEscalationEventDefinition);
    }

    @objid ("8e70de87-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnEvent(BpmnEvent theBpmnEvent) {
        return new BpmnEventPropertyModel(theBpmnEvent);
    }

    @objid ("8e70de8f-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnEventBasedGateway(BpmnEventBasedGateway theBpmnEventBasedGateway) {
        return new BpmnEventBasedGatewayPropertyModel(theBpmnEventBasedGateway);
    }

    @objid ("8e70de97-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnEventDefinition(BpmnEventDefinition theBpmnEventDefinition) {
        return new BpmnEventDefinitionPropertyModel(theBpmnEventDefinition);
    }

    @objid ("8e726509-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnExclusiveGateway(BpmnExclusiveGateway theBpmnExclusiveGateway) {
        return new BpmnExclusiveGatewayPropertyModel(theBpmnExclusiveGateway);
    }

    @objid ("8e726511-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnFlowElement(BpmnFlowElement theBpmnFlowElement) {
        return new BpmnFlowElementPropertyModel(theBpmnFlowElement);
    }

    @objid ("8e726519-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnFlowNode(BpmnFlowNode theBpmnFlowNode) {
        return new BpmnFlowNodePropertyModel(theBpmnFlowNode);
    }

    @objid ("8e726521-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnGateway(BpmnGateway theBpmnGateway) {
        return new BpmnGatewayPropertyModel(theBpmnGateway);
    }

    @objid ("8e726529-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnGroup(BpmnGroup theBpmnGroup) {
        return new BpmnGroupPropertyModel(theBpmnGroup);
    }

    @objid ("8e726531-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnInclusiveGateway(BpmnInclusiveGateway theBpmnInclusiveGateway) {
        return new BpmnInclusiveGatewayPropertyModel(theBpmnInclusiveGateway);
    }

    @objid ("8e726539-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnInterface(BpmnInterface theBpmnInterface) {
        return new BpmnInterfacePropertyModel(theBpmnInterface, this.model);
    }

    @objid ("8e73eba9-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnIntermediateCatchEvent(BpmnIntermediateCatchEvent theBpmnDataObject) {
        return new BpmnIntermediateCatchEventPropertyModel(theBpmnDataObject, this.modelService);
    }

    @objid ("8e73ebb1-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnIntermediateThrowEvent(BpmnIntermediateThrowEvent theBpmnIntermediateThrowEvent) {
        return new BpmnIntermediateThrowEventPropertyModel(theBpmnIntermediateThrowEvent, this.modelService);
    }

    @objid ("8e73ebb9-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnItemAwareElement(BpmnItemAwareElement theBpmnItemAwareElement) {
        return new BpmnItemAwareElementPropertyModel(theBpmnItemAwareElement);
    }

    @objid ("8e73ebc1-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnItemDefinition(BpmnItemDefinition theBpmnItemDefinition) {
        return new BpmnItemDefinitionPropertyModel(theBpmnItemDefinition, this.model);
    }

    @objid ("8e73ebc9-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnLane(BpmnLane theBpmnLane) {
        return new BpmnLanePropertyModel(theBpmnLane, this.model);
    }

    @objid ("8e73ebd1-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnLaneSet(BpmnLaneSet theBpmnLaneSet) {
        return new BpmnLaneSetPropertyModel(theBpmnLaneSet);
    }

    @objid ("8e73ebd9-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnLinkEventDefinition(BpmnLinkEventDefinition theBpmnLinkEventDefinition) {
        return new BpmnLinkEventDefinitionPropertyModel(theBpmnLinkEventDefinition, this.model);
    }

    @objid ("8e75724a-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnLoopCharacteristics(BpmnLoopCharacteristics theBpmnLoopCharacteristics) {
        return new BpmnLoopCharacteristicsPropertyModel(theBpmnLoopCharacteristics);
    }

    @objid ("8e757252-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnManualTask(BpmnManualTask theBpmnManualTask) {
        return new BpmnManualTaskPropertyModel(theBpmnManualTask, this.modelService, this.model);
    }

    @objid ("8e75725a-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnMessage(BpmnMessage theBpmnMessage) {
        return new BpmnMessagePropertyModel(theBpmnMessage, this.model);
    }

    @objid ("8e757262-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnMessageEventDefinition(BpmnMessageEventDefinition theBpmnMessageEventDefinition) {
        return new BpmnMessageEventDefinitionPropertyModel(theBpmnMessageEventDefinition, this.model);
    }

    @objid ("8e75726a-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnMessageFlow(BpmnMessageFlow theBpmnMessageFlow) {
        return new BpmnMessageFlowPropertyModel(theBpmnMessageFlow, this.model);
    }

    @objid ("8e757272-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnMultiInstanceLoopCharacteristics(BpmnMultiInstanceLoopCharacteristics theBpmnMultiInstanceLoopCharacteristics) {
        return new BpmnMultiInstanceLoopCharacteristicsPropertyModel(theBpmnMultiInstanceLoopCharacteristics, this.model);
    }

    @objid ("8e76f8e5-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnOperation(BpmnOperation theBpmnOperation) {
        return new BpmnOperationPropertyModel(theBpmnOperation, this.model);
    }

    @objid ("8e76f8ed-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnParallelGateway(BpmnParallelGateway theBpmnParallelGateway) {
        return new BpmnParallelGatewayPropertyModel(theBpmnParallelGateway);
    }

    @objid ("8e76f8f5-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnParticipant(BpmnParticipant theBpmnParticipant) {
        return new BpmnParticipantPropertyModel(theBpmnParticipant);
    }

    @objid ("8e76f8fd-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnProcess(BpmnProcess theBpmnProcess) {
        return new BpmnProcessPropertyModel(theBpmnProcess);
    }

    @objid ("8e76f905-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnProcessCollaborationDiagram(BpmnProcessCollaborationDiagram theBpmnProcessCollaborationDiagram) {
        return new BpmnProcessCollaborationDiagramPropertyModel(theBpmnProcessCollaborationDiagram);
    }

    @objid ("8e76f90d-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnReceiveTask(BpmnReceiveTask theBpmnReceiveTask) {
        return new BpmnReceiveTaskPropertyModel(theBpmnReceiveTask, this.modelService, this.model);
    }

    @objid ("8e76f915-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnResource(BpmnResource theBpmnResource) {
        return new BpmnResourcePropertyModel(theBpmnResource);
    }

    @objid ("8e787f87-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnResourceParameter(BpmnResourceParameter theBpmnResourceParameter) {
        return new BpmnResourceParameterPropertyModel(theBpmnResourceParameter, this.model);
    }

    @objid ("8e787f8f-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnResourceParameterBinding(BpmnResourceParameterBinding theBpmnResourceParameterBinding) {
        return new BpmnResourceParameterBindingPropertyModel(theBpmnResourceParameterBinding, this.model);
    }

    @objid ("8e787f97-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnResourceRole(BpmnResourceRole theBpmnResourceRole) {
        return new BpmnResourceRolePropertyModel(theBpmnResourceRole, this.model);
    }

    @objid ("8e787f9f-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnRootElement(BpmnRootElement theBpmnRootElement) {
        return new BpmnRootElementPropertyModel(theBpmnRootElement);
    }

    @objid ("8e787fa7-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnScriptTask(BpmnScriptTask theBpmnScriptTask) {
        return new BpmnScriptTaskPropertyModel(theBpmnScriptTask, this.modelService, this.model);
    }

    @objid ("8e787faf-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnSendTask(BpmnSendTask theBpmnSendTask) {
        return new BpmnSendTaskPropertyModel(theBpmnSendTask, this.modelService, this.model);
    }

    @objid ("8e7a0625-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnSequenceFlow(BpmnSequenceFlow theBpmnSequenceFlow) {
        return new BpmnSequenceFlowPropertyModel(theBpmnSequenceFlow, this.modelService, this.model);
    }

    @objid ("8e7a062d-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnSequenceFlowDataAssociation(BpmnSequenceFlowDataAssociation theBpmnSequenceFlowDataAssociation) {
        return new BpmnSequenceFlowDataAssociationPropertyModel(theBpmnSequenceFlowDataAssociation);
    }

    @objid ("8e7a0635-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnServiceTask(BpmnServiceTask theBpmnServiceTask) {
        return new BpmnServiceTaskPropertyModel(theBpmnServiceTask, this.modelService, this.model);
    }

    @objid ("8e7a063d-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnSignalEventDefinition(BpmnSignalEventDefinition theBpmnSignalEventDefinition) {
        return new BpmnSignalEventDefinitionPropertyModel(theBpmnSignalEventDefinition);
    }

    @objid ("8e7a0645-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnStandardLoopCharacteristics(final BpmnStandardLoopCharacteristics theBpmnTerminateEventDefinition) {
        return new BpmnStandardLoopCharacteristicsPropertyModel(theBpmnTerminateEventDefinition);
    }

    @objid ("8e7a064e-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnStartEvent(BpmnStartEvent theBpmnStartEvent) {
        return new BpmnStartEventPropertyModel(theBpmnStartEvent, this.modelService);
    }

    @objid ("8e7b8cc5-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnSubProcess(BpmnSubProcess theBpmnSubProcess) {
        return new BpmnSubProcessPropertyModel(theBpmnSubProcess, this.modelService, this.model);
    }

    @objid ("8e7b8ccd-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnSubProcessDiagram(BpmnSubProcessDiagram theBpmnSubProcessDiagram) {
        return new BpmnSubProcessDiagramPropertyModel(theBpmnSubProcessDiagram);
    }

    @objid ("8e7b8cd5-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnTask(BpmnTask theBpmnDataObject) {
        return new BpmnTaskPropertyModel(theBpmnDataObject, this.modelService, this.model);
    }

    @objid ("8e7b8cdd-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnTerminateEventDefinition(BpmnTerminateEventDefinition theBpmnTerminateEventDefinition) {
        return new BpmnTerminateEventDefinitionPropertyModel(theBpmnTerminateEventDefinition);
    }

    @objid ("8e7b8ce5-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnThrowEvent(BpmnThrowEvent theBpmnThrowEvent) {
        return new BpmnThrowEventPropertyModel(theBpmnThrowEvent);
    }

    @objid ("8e7b8ced-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnTimerEventDefinition(BpmnTimerEventDefinition theBpmnTimerEventDefinition) {
        return new BpmnTimerEventDefinitionPropertyModel(theBpmnTimerEventDefinition);
    }

    @objid ("8e7d1365-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnTransaction(BpmnTransaction theBpmnTransaction) {
        return new BpmnTransactionPropertyModel(theBpmnTransaction, this.modelService, this.model);
    }

    @objid ("8e7d136d-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBpmnUserTask(BpmnUserTask theBpmnUserTask) {
        return new BpmnUserTaskPropertyModel(theBpmnUserTask, this.modelService, this.model);
    }

    @objid ("95b01793-fa5b-4bbc-82c1-45cac693ecea")
    @Override
    public Object visitBusinessRule(BusinessRule theRequirement) {
        return new BusinessRulePropertyModel(theRequirement, this.modelService, this.projectService, this.activationService);
    }

    @objid ("ca84f488-1de3-42d4-9d26-6d00925f40dc")
    @Override
    public Object visitBusinessRuleContainer(BusinessRuleContainer theRequirementContainer) {
        return new BusinessRuleContainerPropertyModel(theRequirementContainer, this.modelService, this.model, this.projectService, this.activationService);
    }

    @objid ("8e7d1375-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitCallBehaviorAction(CallBehaviorAction theCallBehaviorAction) {
        return new CallBehaviorActionPropertyModel(theCallBehaviorAction);
    }

    @objid ("8e7d137d-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitCallOperationAction(CallOperationAction theCallOperationAction) {
        return new CallOperationActionPropertyModel(theCallOperationAction);
    }

    @objid ("8e7d1385-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitCentralBufferNode(CentralBufferNode theCentralBufferNode) {
        return new CentralBufferNodePropertyModel(theCentralBufferNode, this.model);
    }

    @objid ("8e7d138d-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitChoicePseudoState(ChoicePseudoState theChoicePseudoState) {
        return new ChoicePseudoStatePropertyModel(theChoicePseudoState);
    }

    @objid ("8e7e9a05-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitClass(Class theClass) {
        return new ClassPropertyModel(theClass);
    }

    @objid ("8e7e9a0b-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitClassAssociation(ClassAssociation theClassAssociation) {
        return new ClassAssociationPropertyModel(theClassAssociation, this.model);
    }

    @objid ("8e7e9a13-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitClassDiagram(final ClassDiagram theClassDiagram) {
        return new ClassDiagramPropertyModel(theClassDiagram);
    }

    @objid ("8e7e9a1c-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitClause(Clause theClause) {
        return new ClausePropertyModel(theClause);
    }

    @objid ("8e7e9a24-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitCollaboration(Collaboration theCollaboration) {
        return new CollaborationPropertyModel(theCollaboration);
    }

    @objid ("8e7e9a2c-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitCollaborationUse(CollaborationUse theCollaborationUse) {
        return new CollaborationUsePropertyModel(theCollaborationUse);
    }

    @objid ("8e8020a9-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitCombinedFragment(CombinedFragment theCombinedFragment) {
        return new CombinedFragmentPropertyModel(theCombinedFragment);
    }

    @objid ("79b9260e-0699-403c-84bc-8cf7d2522de6")
    @Override
    public Object visitCommunicationChannel(CommunicationChannel theCommunicationChannel) {
        return new CommunicationChannelPropertyModel(theCommunicationChannel);
    }

    @objid ("8e8020b1-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitCommunicationDiagram(final CommunicationDiagram theCommunicationDiagram) {
        return new CommunicationDiagramPropertyModel(theCommunicationDiagram);
    }

    @objid ("8e8020ba-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitCommunicationInteraction(CommunicationInteraction theCommunicationInteraction) {
        return new CommunicationInteractionPropertyModel(theCommunicationInteraction);
    }

    @objid ("8e8020c2-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitCommunicationMessage(CommunicationMessage theCommunicationMessage) {
        return new CommunicationMessagePropertyModel(theCommunicationMessage);
    }

    @objid ("8e8020ca-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitCommunicationNode(CommunicationNode theCommunicationNode) {
        return new CommunicationNodePropertyModel(theCommunicationNode);
    }

    @objid ("8e81a745-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitComponent(Component theComponent) {
        return new ComponentPropertyModel(theComponent);
    }

    @objid ("8e81a74d-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitCompositeStructureDiagram(final CompositeStructureDiagram theCompositeStructureDiagram) {
        return new CompositeStructureDiagramPropertyModel(theCompositeStructureDiagram);
    }

    @objid ("8e81a756-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitConditionalNode(ConditionalNode theConditionalNode) {
        return new ConditionalNodePropertyModel(theConditionalNode);
    }

    @objid ("8e81a767-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitConnectionPointReference(ConnectionPointReference theConnectionPointReference) {
        return new ConnectionPointReferencePropertyModel(theConnectionPointReference);
    }

    @objid ("8e81a76f-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitConnectorEnd(ConnectorEnd theConnectorEnd) {
        return new ConnectorEnd2PropertyModel(theConnectorEnd);
    }

    @objid ("8e832deb-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitConstraint(Constraint theConstraint) {
        return new ConstraintPropertyModel(theConstraint);
    }

    @objid ("8e832df3-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitControlFlow(ControlFlow theControlFlow) {
        return new ControlFlowPropertyModel(theControlFlow);
    }

    @objid ("8e832dfb-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitDataFlow(DataFlow theDataFlow) {
        return new DataFlowPropertyModel(theDataFlow, this.model);
    }

    @objid ("8e832e03-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitDataStoreNode(DataStoreNode theDataStoreNode) {
        return new DataStoreNodePropertyModel(theDataStoreNode);
    }

    @objid ("8e832e0b-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitDataType(DataType theDataType) {
        return new DataTypePropertyModel(theDataType);
    }

    @objid ("8e84b485-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitDecisionMergeNode(DecisionMergeNode theDecisionMergeNode) {
        return new DecisionMergeNodePropertyModel(theDecisionMergeNode);
    }

    @objid ("8e84b48d-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitDeepHistoryPseudoState(DeepHistoryPseudoState theDeepHistoryPseudoState) {
        return new DeepHistoryPseudoStatePropertyModel(theDeepHistoryPseudoState);
    }

    @objid ("8e84b495-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitDependency(Dependency theDependency) {
        return new DependencyPropertyModel(theDependency);
    }

    @objid ("8e84b49d-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitDeploymentDiagram(final DeploymentDiagram theDeploymentDiagram) {
        return new DeploymentDiagramPropertyModel(theDeploymentDiagram);
    }

    @objid ("631d7b9d-0f64-44b7-9e67-4af6457697c1")
    @Override
    public Object visitDiagramSet(DiagramSet theDiagramSet) {
        return new DiagramSetPropertyModel(theDiagramSet);
    }

    @objid ("8e84b4a6-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitDictionary(Dictionary theDictionary) {
        return new DictionaryPropertyModel(theDictionary, this.modelService, this.model, this.projectService, this.activationService);
    }

    @objid ("8e863b25-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitDurationConstraint(DurationConstraint theDurationConstraint) {
        return new DurationConstraintPropertyModel(theDurationConstraint);
    }

    @objid ("2b07c5c7-cf59-11e1-80a9-002564c97630")
    @Override
    public Object visitElement(Element theElement) {
        return new EmptyDataModel(theElement);
    }

    @objid ("8e863b2d-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitElementImport(ElementImport theElementImport) {
        return new ElementImportPropertyModel(theElementImport);
    }

    @objid ("8e863b35-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitElementRealization(ElementRealization theElementRealization) {
        return new ElementRealizationPropertyModel(theElementRealization);
    }

    @objid ("8e863b3d-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitEntryPointPseudoState(EntryPointPseudoState theEntryPointPseudoState) {
        return new EntryPointPseudoStatePropertyModel(theEntryPointPseudoState);
    }

    @objid ("8e863b45-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitEnumeratedPropertyType(EnumeratedPropertyType theEnumeratedPropertyType) {
        return new EnumeratedPropertyTypePropertyModel(theEnumeratedPropertyType);
    }

    @objid ("8e863b4d-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitEnumeration(Enumeration theEnumeration) {
        return new EnumerationPropertyModel(theEnumeration);
    }

    @objid ("8e87c1cc-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitEnumerationLiteral(EnumerationLiteral theEnumerationLiteral) {
        return new EnumerationLiteralPropertyModel(theEnumerationLiteral);
    }

    @objid ("8e87c1d4-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitEvent(Event theEvent) {
        return new EventPropertyModel(theEvent);
    }

    @objid ("8e87c1dc-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitExceptionHandler(ExceptionHandler theExceptionHandler) {
        return new ExceptionHandlerPropertyModel(theExceptionHandler, this.model);
    }

    @objid ("8e87c1e4-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitExecutionOccurenceSpecification(ExecutionOccurenceSpecification theExecutionOccurenceSpecification) {
        return null;
    }

    @objid ("8e87c1ec-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitExecutionSpecification(ExecutionSpecification theExecutionSpecification) {
        return null;
    }

    @objid ("8e894869-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitExitPointPseudoState(ExitPointPseudoState theExitPointPseudoState) {
        return new ExitPointPseudoStatePropertyModel(theExitPointPseudoState);
    }

    @objid ("8e894871-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitExpansionNode(ExpansionNode theExpansionNode) {
        return new ExpansionNodePropertyModel(theExpansionNode);
    }

    @objid ("8e894879-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitExpansionRegion(ExpansionRegion theExpansionRegion) {
        return new ExpansionRegionPropertyModel(theExpansionRegion);
    }

    @objid ("8e894881-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitExtensionPoint(ExtensionPoint theExtensionPoint) {
        return new ExtensionPointPropertyModel(theExtensionPoint);
    }

    @objid ("8e894889-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitExternDocument(ExternDocument theExternDocument) {
        return new ExternDocumentPropertyModel(theExternDocument, this.model);
    }

    @objid ("8e8acf0b-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitFinalState(FinalState theFinalState) {
        return new FinalStatePropertyModel(theFinalState);
    }

    @objid ("8e8acf13-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitFlowFinalNode(FlowFinalNode theFlowFinalNode) {
        return new FlowFinalNodePropertyModel(theFlowFinalNode);
    }

    @objid ("8e8acf1b-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitForkJoinNode(ForkJoinNode theForkJoinNode) {
        return new ForkJoinNodePropertyModel(theForkJoinNode);
    }

    @objid ("8e8acf23-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitForkPseudoState(ForkPseudoState theForkPseudoState) {
        return new ForkPseudoStatePropertyModel(theForkPseudoState);
    }

    @objid ("8e8acf2b-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitGate(Gate theGate) {
        return new GatePropertyModel(theGate);
    }

    @objid ("8e8c55ab-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitGeneralOrdering(GeneralOrdering theGeneralOrdering) {
        return new GeneralOrderingPropertyModel(theGeneralOrdering);
    }

    @objid ("8e8c55b3-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitGeneralization(Generalization theGeneralization) {
        return new GeneralizationPropertyModel(theGeneralization, this.model);
    }

    @objid ("87693c23-a446-4b51-a111-c94731b8903e")
    @Override
    public Object visitGoal(Goal theRequirement) {
        return new GoalPropertyModel(theRequirement, this.modelService, this.projectService, this.activationService);
    }

    @objid ("67b4452e-249f-4a8c-aa39-35b6815cdb8e")
    @Override
    public Object visitGoalContainer(GoalContainer theRequirementContainer) {
        return new GoalContainerPropertyModel(theRequirementContainer, this.modelService, this.model, this.projectService, this.activationService);
    }

    @objid ("8e8c55bb-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitInformationFlow(InformationFlow theInformationFlow) {
        return new InformationFlowPropertyModel(theInformationFlow, this.model);
    }

    @objid ("8e8c55c3-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitInformationItem(InformationItem theInformationItem) {
        return new InformationItemPropertyModel(theInformationItem, this.model);
    }

    @objid ("8e8c55cb-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitInitialNode(InitialNode theInitialNode) {
        return new InitialNodePropertyModel(theInitialNode);
    }

    @objid ("8e8ddc4c-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitInitialPseudoState(InitialPseudoState theInitialPseudoState) {
        return new InitialPseudoStatePropertyModel(theInitialPseudoState);
    }

    @objid ("8e8ddc54-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitInputPin(InputPin theInputPin) {
        return new InputPinPropertyModel(theInputPin, this.model);
    }

    @objid ("8e8ddc5c-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitInstance(Instance theInstance) {
        return new InstancePropertyModel(theInstance);
    }

    @objid ("8e8ddc64-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitInstanceNode(InstanceNode theInstanceNode) {
        return new InstanceNodePropertyModel(theInstanceNode, this.model);
    }

    @objid ("8e8f62e5-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitInteraction(Interaction theInteraction) {
        return new InteractionPropertyModel(theInteraction);
    }

    @objid ("8e8f62ed-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitInteractionOperand(InteractionOperand theInteractionOperand) {
        return new InteractionOperandPropertyModel(theInteractionOperand);
    }

    @objid ("8e8f62f5-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitInteractionUse(InteractionUse theInteractionUse) {
        return new InteractionUsePropertyModel(theInteractionUse);
    }

    @objid ("8e8f62fd-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitInterface(Interface theInterface) {
        return new InterfacePropertyModel(theInterface);
    }

    @objid ("8e8f6305-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitInterfaceRealization(InterfaceRealization theInterfaceRealization) {
        return new InterfaceRealizationPropertyModel(theInterfaceRealization);
    }

    @objid ("8e90e985-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitInternalTransition(InternalTransition theInternalTransition) {
        return new InternalTransitionPropertyModel(theInternalTransition);
    }

    @objid ("8e90e98d-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitInterruptibleActivityRegion(InterruptibleActivityRegion theInterruptibleActivityRegion) {
        return new InterruptibleActivityRegionPropertyModel(theInterruptibleActivityRegion, this.model);
    }

    @objid ("8e90e995-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitJoinPseudoState(JoinPseudoState theJoinPseudoState) {
        return new JoinPseudoStatePropertyModel(theJoinPseudoState);
    }

    @objid ("8e90e99d-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitJunctionPseudoState(JunctionPseudoState theJunctionPseudoState) {
        return new JunctionPseudoStatePropertyModel(theJunctionPseudoState);
    }

    @objid ("8e90e9a5-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitLifeline(Lifeline theLifeline) {
        return new LifelinePropertyModel(theLifeline);
    }

    @objid ("8e927026-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitLinkEnd(LinkEnd theLinkEnd) {
        return new LinkEnd2PropertyModel(theLinkEnd);
    }

    @objid ("8e92702e-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitLoopNode(LoopNode theLoopNode) {
        return new LoopNodePropertyModel(theLoopNode);
    }

    @objid ("8e927036-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitManifestation(Manifestation theManifestation) {
        return new ManifestationPropertyModel(theManifestation);
    }

    @objid ("8e92703e-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitMessage(Message theMessage) {
        return new MessagePropertyModel(theMessage);
    }

    @objid ("8e927046-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitMessageFlow(MessageFlow theMessageFlow) {
        return new MessageFlowPropertyModel(theMessageFlow);
    }

    @objid ("8e93f6cc-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitMetaclassReference(MetaclassReference theMetaclassReference) {
        return new MetaclassReferencePropertyModel(theMetaclassReference);
    }

    @objid ("8e93f6d5-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitModuleComponent(ModuleComponent theModule) {
        return new ModuleComponentPropertyModel(theModule);
    }

    @objid ("8e81a75e-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitModuleParameter(ModuleParameter theConfigParam) {
        return new ConfigParamPropertyModel(theConfigParam);
    }

    @objid ("8e93f6de-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitNamespaceUse(NamespaceUse theNamespaceUse) {
        return new NamespaceUsePropertyModel(theNamespaceUse);
    }

    @objid ("8e6ac41c-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitNaryAssociation(NaryAssociation obj) {
        return new AssociationEndNPropertyModel(obj.getNaryEnd().get(0));
    }

    @objid ("d53c9390-0338-435d-8460-b8f9a0c00087")
    @Override
    public Object visitNaryLinkEnd(NaryLinkEnd theNaryLinkEnd) {
        return new LinkEndNPropertyModel(theNaryLinkEnd);
    }

    @objid ("8e93f6e6-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitNode(Node theNode) {
        return new NodePropertyModel(theNode);
    }

    @objid ("8e957d69-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitNote(Note theNote) {
        return new NotePropertyModel(theNote);
    }

    @objid ("8e957d71-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitNoteType(NoteType theNoteType) {
        return new NoteTypePropertyModel(theNoteType);
    }

    @objid ("8e957d79-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitObjectDiagram(final ObjectDiagram theObjectDiagram) {
        return new ObjectDiagramPropertyModel(theObjectDiagram);
    }

    @objid ("8e957d82-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitObjectFlow(ObjectFlow theObjectFlow) {
        return new ObjectFlowPropertyModel(theObjectFlow);
    }

    @objid ("8e970405-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitOpaqueAction(OpaqueAction theOpaqueAction) {
        return new OpaqueActionPropertyModel(theOpaqueAction);
    }

    @objid ("8e97040d-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitOpaqueBehavior(OpaqueBehavior theOpaqueBehavior) {
        return new OpaqueBehaviorPropertyModel(theOpaqueBehavior);
    }

    @objid ("8e970415-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitOperation(Operation theOperation) {
        return new OperationPropertyModel(theOperation);
    }

    @objid ("8e97041d-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitOutputPin(OutputPin theOutputPin) {
        return new OutputPinPropertyModel(theOutputPin);
    }

    @objid ("8e970425-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitPackage(Package thePackage) {
        return new PackagePropertyModel(thePackage);
    }

    @objid ("8e988aa9-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitPackageImport(PackageImport thePackageImport) {
        return new PackageImportPropertyModel(thePackageImport);
    }

    @objid ("8e988ab1-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitPackageMerge(PackageMerge thePackageMerge) {
        return new PackageMergePropertyModel(thePackageMerge);
    }

    @objid ("8e988ab9-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitParameter(Parameter theParameter) {
        if (theParameter.getReturned() != null) {
            return new ReturnParameterPropertyModel(theParameter);
        } else if (theParameter.getComposed() != null) {
            return new IOParameterPropertyModel(theParameter);
        } else {
            return null;
        }
    }

    @objid ("8e988ac0-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitPartDecomposition(PartDecomposition thePartDecomposition) {
        return new PartDecompositionPropertyModel(thePartDecomposition);
    }

    @objid ("8e9a1145-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitPort(Port thePort) {
        return new PortPropertyModel(thePort);
    }

    @objid ("8e9a114d-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitProfile(Profile theProfile) {
        return new ProfilePropertyModel(theProfile);
    }

    @objid ("8e9a1156-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitProject(Project theProject) {
        return new ProjectPropertyModel(theProject);
    }

    @objid ("8e9a115f-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitPropertyDefinition(PropertyDefinition theProperty) {
        return new PropertyPropertyModel(theProperty);
    }

    @objid ("8e9b97e5-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitPropertyEnumerationLitteral(PropertyEnumerationLitteral thePropertyEnumerationLitteral) {
        return new PropertyEnumerationLitteralPropertyModel(thePropertyEnumerationLitteral);
    }

    @objid ("8e9b97ed-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitPropertyTableDefinition(PropertyTableDefinition thePropertySet) {
        return new PropertySetPropertyModel(thePropertySet);
    }

    @objid ("8e9b97f5-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitPropertyType(PropertyType thePropertyType) {
        return new PropertyTypePropertyModel(thePropertyType);
    }

    @objid ("8e9d1e89-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitProvidedInterface(ProvidedInterface theProvidedInterface) {
        return new ProvidedInterfacePropertyModel(theProvidedInterface, this.model);
    }

    @objid ("8e9d1e91-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitRaisedException(RaisedException theRaisedException) {
        return new RaisedExceptionPropertyModel(theRaisedException);
    }

    @objid ("8e9d1e99-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitRequiredInterface(RequiredInterface theRequiredInterface) {
        return new RequiredInterfacePropertyModel(theRequiredInterface, this.model);
    }

    @objid ("8e9d1ea1-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitRequirement(Requirement theRequirement) {
        return new RequirementPropertyModel(theRequirement, this.modelService, this.projectService, this.activationService);
    }

    @objid ("8e9ea525-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitRequirementContainer(RequirementContainer theRequirementContainer) {
        return new RequirementContainerPropertyModel(theRequirementContainer, this.modelService, this.model, this.projectService, this.activationService);
    }

    @objid ("8e9ea52d-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitSendSignalAction(SendSignalAction theSendSignalAction) {
        return new SendSignalActionPropertyModel(theSendSignalAction);
    }

    @objid ("8e9ea535-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitSequenceDiagram(SequenceDiagram theSequenceDiagram) {
        return new SequenceDiagramPropertyModel(theSequenceDiagram);
    }

    @objid ("8e9ea53d-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitShallowHistoryPseudoState(ShallowHistoryPseudoState theShallowHistoryPseudoState) {
        return new ShallowHistoryPseudoStatePropertyModel(theShallowHistoryPseudoState);
    }

    @objid ("8ea02bc5-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitSignal(Signal theSignal) {
        return new SignalPropertyModel(theSignal);
    }

    @objid ("8ea02bcd-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitState(State theState) {
        return new StatePropertyModel(theState);
    }

    @objid ("8ea02bd5-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitStateInvariant(StateInvariant theStateInvariant) {
        return new StateInvariantPropertyModel(theStateInvariant);
    }

    @objid ("8ea02bdd-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitStateMachine(StateMachine theStateMachine) {
        return new StateMachinePropertyModel(theStateMachine);
    }

    @objid ("8ea1b265-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitStateMachineDiagram(final StateMachineDiagram theStateMachineDiagram) {
        return new StateMachineDiagramPropertyModel(theStateMachineDiagram);
    }

    @objid ("8ea1b26e-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitStaticDiagram(StaticDiagram theStaticDiagram) {
        return new StaticDiagramPropertyModel(theStaticDiagram);
    }

    @objid ("8ea1b276-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitStereotype(Stereotype theStereotype) {
        return new StereotypePropertyModel(theStereotype);
    }

    @objid ("8ea1b27f-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitStructuredActivityNode(StructuredActivityNode theStructuredActivityNode) {
        return new StructuredActivityNodePropertyModel(theStructuredActivityNode);
    }

    @objid ("8ea33905-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitSubstitution(Substitution theSubstitution) {
        return new SubstitutionPropertyModel(theSubstitution);
    }

    @objid ("8ea3390d-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitTagType(TagType theTagType) {
        return new TagTypePropertyModel(theTagType);
    }

    @objid ("8ea33915-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitTemplateBinding(TemplateBinding theTemplateBinding) {
        return new TemplateBindingPropertyModel(theTemplateBinding, this.modelService);
    }

    @objid ("8ea3391d-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitTemplateParameter(TemplateParameter theTemplateParameter) {
        return new TemplateParameterPropertyModel(theTemplateParameter, this.model);
    }

    @objid ("8ea4bfa5-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitTemplateParameterSubstitution(TemplateParameterSubstitution theTemplateParameterSubstitution) {
        return new TemplateParameterSubstitutionPropertyModel(theTemplateParameterSubstitution);
    }

    @objid ("8ea4bfad-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitTerm(Term theTerm) {
        return new TermPropertyModel(theTerm, this.modelService, this.projectService, this.activationService);
    }

    @objid ("8ea4bfb5-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitTerminatePseudoState(TerminatePseudoState theTerminatePseudoState) {
        return new TerminatePseudoStatePropertyModel(theTerminatePseudoState);
    }

    @objid ("8ea4bfbd-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitTransition(Transition theTransition) {
        return new TransitionPropertyModel(theTransition);
    }

    @objid ("8e9b9805-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitTypedPropertyTable(TypedPropertyTable thePropertyValueSet) {
        return new TypedPropertyTablePropertyModel(thePropertyValueSet, this.model);
    }

    @objid ("8ea64645-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitUsage(Usage theUsage) {
        return new UsagePropertyModel(theUsage);
    }

    @objid ("8ea6464d-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitUseCase(UseCase theUseCase) {
        return new UseCasePropertyModel(theUseCase);
    }

    @objid ("8ea64655-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitUseCaseDependency(UseCaseDependency theUseCaseDependency) {
        return new UseCaseDependencyPropertyModel(theUseCaseDependency, this.model);
    }

    @objid ("8ea6465d-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitUseCaseDiagram(final UseCaseDiagram theUseCaseDiagram) {
        return new UseCaseDiagramPropertyModel(theUseCaseDiagram);
    }

    @objid ("8ea7cce5-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitValuePin(ValuePin theValuePin) {
        return new ValuePinPropertyModel(theValuePin);
    }

    @objid ("61172982-c280-41b7-9471-7ced71fc05eb")
    @Override
    public Object visitLink(Link theLink) {
        return new LinkPropertyModel(theLink);
    }

    @objid ("a0e46c7a-ad1b-4318-87c8-6f345daa9d65")
    @Override
    public Object visitConnector(Connector theConnector) {
        return new ConnectorPropertyModel(theConnector);
    }

    @objid ("6c00d4a9-d2c0-4ee3-b901-4d18c0c2ebd9")
    @Override
    public Object visitAssociation(Association theAssociation) {
        return new AssociationPropertyModel(theAssociation);
    }

    @objid ("545403ad-ced3-4229-9a9c-d3176f45071c")
    @Override
    public Object visitQueryDefinition(QueryDefinition theQueryDefinition) {
        return new QueryDefinitionPropertyModel(theQueryDefinition, this.model);
    }

    @objid ("90284b61-c748-41ad-809d-60ffd82f4666")
    @Override
    public Object visitMatrixDefinition(MatrixDefinition theMatrixDefinition) {
        return new MatrixDefinitionPropertyModel(theMatrixDefinition);
    }

    @objid ("ef67f434-317d-4e07-92e7-2178eddb4034")
    @Override
    public Object visitMatrixValueDefinition(MatrixValueDefinition theMatrixValueDefinition) {
        return new MatrixValueDefinitionPropertyModel(theMatrixValueDefinition);
    }

    @objid ("5b48a8c8-4d09-4375-8637-2d514bac1cf1")
    @Override
    public Object visitGenericAnalystElement(GenericAnalystElement obj) {
        return new GenericAnalystElementPropertyModel(obj, 
                this.modelService, 
                this.projectService, 
                this.activationService);
    }

    @objid ("86bd513f-3a37-4b23-ab76-7083da703bbc")
    @Override
    public Object visitGenericAnalystContainer(GenericAnalystContainer obj) {
        return new GenericAnalystContainerPropertyModel(obj, 
                this.modelService, 
                this.model, 
                this.projectService, 
                this.activationService);
    }

    @objid ("959bc5f3-767c-4401-af7d-a5256a567c89")
    @Override
    public Object visitNaryLink(NaryLink obj) {
        return new LinkEndNPropertyModel(obj.getNaryLinkEnd().get(0));
    }

    @objid ("d9debde9-52b6-4b8a-bc70-0ec31fb51aaa")
    @Override
    public Object visitComponentRealization(ComponentRealization obj) {
        return new ComponentRealizationPropertyModel(obj);
    }

}
