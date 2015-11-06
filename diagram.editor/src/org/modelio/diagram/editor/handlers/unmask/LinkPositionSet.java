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
                                    

package org.modelio.diagram.editor.handlers.unmask;

import java.util.HashSet;
import java.util.Set;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.metamodel.analyst.AnalystElement;
import org.modelio.metamodel.analyst.AnalystProject;
import org.modelio.metamodel.analyst.Dictionary;
import org.modelio.metamodel.analyst.PropertyContainer;
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
import org.modelio.metamodel.bpmn.events.BpmnImplicitThrowEvent;
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
import org.modelio.metamodel.bpmn.rootElements.BpmnAssociation;
import org.modelio.metamodel.bpmn.rootElements.BpmnBaseElement;
import org.modelio.metamodel.bpmn.rootElements.BpmnBehavior;
import org.modelio.metamodel.bpmn.rootElements.BpmnFlowElement;
import org.modelio.metamodel.bpmn.rootElements.BpmnFlowNode;
import org.modelio.metamodel.bpmn.rootElements.BpmnGroup;
import org.modelio.metamodel.bpmn.rootElements.BpmnRootElement;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.diagrams.ActivityDiagram;
import org.modelio.metamodel.diagrams.BehaviorDiagram;
import org.modelio.metamodel.diagrams.ClassDiagram;
import org.modelio.metamodel.diagrams.CommunicationDiagram;
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
import org.modelio.metamodel.uml.behavior.activityModel.ActivityAction;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityEdge;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityFinalNode;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityGroup;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityNode;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityParameterNode;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityPartition;
import org.modelio.metamodel.uml.behavior.activityModel.CallAction;
import org.modelio.metamodel.uml.behavior.activityModel.CallBehaviorAction;
import org.modelio.metamodel.uml.behavior.activityModel.CallOperationAction;
import org.modelio.metamodel.uml.behavior.activityModel.CentralBufferNode;
import org.modelio.metamodel.uml.behavior.activityModel.Clause;
import org.modelio.metamodel.uml.behavior.activityModel.ConditionalNode;
import org.modelio.metamodel.uml.behavior.activityModel.ControlFlow;
import org.modelio.metamodel.uml.behavior.activityModel.ControlNode;
import org.modelio.metamodel.uml.behavior.activityModel.DataStoreNode;
import org.modelio.metamodel.uml.behavior.activityModel.DecisionMergeNode;
import org.modelio.metamodel.uml.behavior.activityModel.ExceptionHandler;
import org.modelio.metamodel.uml.behavior.activityModel.ExpansionNode;
import org.modelio.metamodel.uml.behavior.activityModel.ExpansionRegion;
import org.modelio.metamodel.uml.behavior.activityModel.FinalNode;
import org.modelio.metamodel.uml.behavior.activityModel.FlowFinalNode;
import org.modelio.metamodel.uml.behavior.activityModel.ForkJoinNode;
import org.modelio.metamodel.uml.behavior.activityModel.InitialNode;
import org.modelio.metamodel.uml.behavior.activityModel.InputPin;
import org.modelio.metamodel.uml.behavior.activityModel.InstanceNode;
import org.modelio.metamodel.uml.behavior.activityModel.InterruptibleActivityRegion;
import org.modelio.metamodel.uml.behavior.activityModel.LoopNode;
import org.modelio.metamodel.uml.behavior.activityModel.MessageFlow;
import org.modelio.metamodel.uml.behavior.activityModel.ObjectFlow;
import org.modelio.metamodel.uml.behavior.activityModel.ObjectNode;
import org.modelio.metamodel.uml.behavior.activityModel.OpaqueAction;
import org.modelio.metamodel.uml.behavior.activityModel.OutputPin;
import org.modelio.metamodel.uml.behavior.activityModel.Pin;
import org.modelio.metamodel.uml.behavior.activityModel.SendSignalAction;
import org.modelio.metamodel.uml.behavior.activityModel.StructuredActivityNode;
import org.modelio.metamodel.uml.behavior.activityModel.ValuePin;
import org.modelio.metamodel.uml.behavior.commonBehaviors.Behavior;
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
import org.modelio.metamodel.uml.behavior.interactionModel.InteractionFragment;
import org.modelio.metamodel.uml.behavior.interactionModel.InteractionOperand;
import org.modelio.metamodel.uml.behavior.interactionModel.InteractionUse;
import org.modelio.metamodel.uml.behavior.interactionModel.Lifeline;
import org.modelio.metamodel.uml.behavior.interactionModel.Message;
import org.modelio.metamodel.uml.behavior.interactionModel.MessageEnd;
import org.modelio.metamodel.uml.behavior.interactionModel.OccurrenceSpecification;
import org.modelio.metamodel.uml.behavior.interactionModel.PartDecomposition;
import org.modelio.metamodel.uml.behavior.interactionModel.StateInvariant;
import org.modelio.metamodel.uml.behavior.interactionModel.TerminateSpecification;
import org.modelio.metamodel.uml.behavior.stateMachineModel.AbstractPseudoState;
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
import org.modelio.metamodel.uml.behavior.stateMachineModel.Region;
import org.modelio.metamodel.uml.behavior.stateMachineModel.ShallowHistoryPseudoState;
import org.modelio.metamodel.uml.behavior.stateMachineModel.State;
import org.modelio.metamodel.uml.behavior.stateMachineModel.StateMachine;
import org.modelio.metamodel.uml.behavior.stateMachineModel.StateVertex;
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
import org.modelio.metamodel.uml.infrastructure.MetaclassReference;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.ModelTree;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.metamodel.uml.infrastructure.NoteType;
import org.modelio.metamodel.uml.infrastructure.Profile;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.infrastructure.Substitution;
import org.modelio.metamodel.uml.infrastructure.TagParameter;
import org.modelio.metamodel.uml.infrastructure.TagType;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.metamodel.uml.infrastructure.Usage;
import org.modelio.metamodel.uml.infrastructure.properties.EnumeratedPropertyType;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyEnumerationLitteral;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyTableDefinition;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyType;
import org.modelio.metamodel.uml.infrastructure.properties.TypedPropertyTable;
import org.modelio.metamodel.uml.statik.Artifact;
import org.modelio.metamodel.uml.statik.AssociationEnd;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.AttributeLink;
import org.modelio.metamodel.uml.statik.BindableInstance;
import org.modelio.metamodel.uml.statik.Binding;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.ClassAssociation;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.Collaboration;
import org.modelio.metamodel.uml.statik.CollaborationUse;
import org.modelio.metamodel.uml.statik.Component;
import org.modelio.metamodel.uml.statik.ConnectorEnd;
import org.modelio.metamodel.uml.statik.DataType;
import org.modelio.metamodel.uml.statik.ElementImport;
import org.modelio.metamodel.uml.statik.ElementRealization;
import org.modelio.metamodel.uml.statik.Enumeration;
import org.modelio.metamodel.uml.statik.EnumerationLiteral;
import org.modelio.metamodel.uml.statik.Feature;
import org.modelio.metamodel.uml.statik.GeneralClass;
import org.modelio.metamodel.uml.statik.Generalization;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.metamodel.uml.statik.Interface;
import org.modelio.metamodel.uml.statik.InterfaceRealization;
import org.modelio.metamodel.uml.statik.LinkEnd;
import org.modelio.metamodel.uml.statik.Manifestation;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.NamespaceUse;
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
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * This class aggregates several sets of links, computed from a root element.
 */
@objid ("65edb2f5-33f7-11e2-95fe-001ec947c8cc")
public class LinkPositionSet {
    @objid ("65edb2f7-33f7-11e2-95fe-001ec947c8cc")
    protected Set<MObject> leftLinks = new HashSet<>();

    @objid ("65f01544-33f7-11e2-95fe-001ec947c8cc")
    protected Set<MObject> rightLinks = new HashSet<>();

    @objid ("65f01547-33f7-11e2-95fe-001ec947c8cc")
    protected Set<MObject> topLinks = new HashSet<>();

    @objid ("65f0154a-33f7-11e2-95fe-001ec947c8cc")
    protected Set<MObject> bottomLinks = new HashSet<>();

    /**
     * Constructor building the position set from a root element.
     * @param theRoot The element to compute the links from.
     * @param unmaskStructuringLink Whether or not the computed links must be structural.
     * @param unmaskedElements Each computed link must start or target an element from this list. A <code>null</code>value indicates no restrictions: all links related to theRoot are accepted.
     */
    @objid ("65f0154d-33f7-11e2-95fe-001ec947c8cc")
    public LinkPositionSet(final MObject theRoot, final boolean unmaskStructuringLink, final Set<MObject> unmaskedElements) {
        theRoot.accept(new LinkFinderVisitor(unmaskStructuringLink, unmaskedElements));
    }

    @objid ("65f01558-33f7-11e2-95fe-001ec947c8cc")
    public Set<MObject> getBottomLinks() {
        return this.bottomLinks;
    }

    @objid ("65f0155e-33f7-11e2-95fe-001ec947c8cc")
    public Set<MObject> getLeftLinks() {
        return this.leftLinks;
    }

    @objid ("65f01564-33f7-11e2-95fe-001ec947c8cc")
    public Set<MObject> getRightLinks() {
        return this.rightLinks;
    }

    @objid ("65f0156a-33f7-11e2-95fe-001ec947c8cc")
    public Set<MObject> getTopLinks() {
        return this.topLinks;
    }

    @objid ("65f01570-33f7-11e2-95fe-001ec947c8cc")
    class LinkFinderVisitor extends DefaultModelVisitor {
        @objid ("65f01573-33f7-11e2-95fe-001ec947c8cc")
        private boolean unmaskStructuringLink;

        @objid ("65f01574-33f7-11e2-95fe-001ec947c8cc")
        private Set<MObject> unmaskedElements;

        @objid ("65f01577-33f7-11e2-95fe-001ec947c8cc")
        public LinkFinderVisitor(final boolean unmaskStructuringLink, final Set<MObject> unmaskedElements) {
            this.unmaskStructuringLink = unmaskStructuringLink;
            this.unmaskedElements = unmaskedElements;
        }

        @objid ("65f0157f-33f7-11e2-95fe-001ec947c8cc")
        private void addLeft(final MObject elt, final MObject target) {
            if (target != null) {
                if (this.unmaskedElements == null || this.unmaskedElements.contains(target)) {
                    LinkPositionSet.this.leftLinks.add(elt);
                }
            }
        }

        @objid ("65f01585-33f7-11e2-95fe-001ec947c8cc")
        private void addRight(final MObject elt, final MObject target) {
            if (target != null) {
                if (this.unmaskedElements == null || this.unmaskedElements.contains(target)) {
                    LinkPositionSet.this.rightLinks.add(elt);
                }
            }
        }

        @objid ("65f0158b-33f7-11e2-95fe-001ec947c8cc")
        private void addTop(final MObject elt, final MObject target) {
            if (target != null) {
                if (this.unmaskedElements == null || this.unmaskedElements.contains(target)) {
                    LinkPositionSet.this.topLinks.add(elt);
                }
            }
        }

        @objid ("65f01591-33f7-11e2-95fe-001ec947c8cc")
        private void addBottom(final MObject elt, final MObject target) {
            if (target != null) {
                if (this.unmaskedElements == null || this.unmaskedElements.contains(target)) {
                    LinkPositionSet.this.bottomLinks.add(elt);
                }
            }
        }

        /**
         * Visits composition relations for "AbstractDiagram" elements and its super classes.
         */
        @objid ("65f01597-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitAbstractDiagram(final AbstractDiagram child) {
            super.visitAbstractDiagram(child);
            return null;
        }

        /**
         * Visits composition relations for "Abstraction" elements and its super classes.
         */
        @objid ("65f2779f-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitAbstraction(final Abstraction child) {
            super.visitAbstraction(child);
            return null;
        }

        /**
         * Visits composition relations for "AbstractPseudoState" elements and its super classes.
         */
        @objid ("65f277a9-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitAbstractPseudoState(final AbstractPseudoState child) {
            super.visitAbstractPseudoState(child);
            return null;
        }

        /**
         * Visits composition relations for "AcceptCallEventAction" elements and its super classes.
         */
        @objid ("65f277b3-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitAcceptCallEventAction(final AcceptCallEventAction child) {
            super.visitAcceptCallEventAction(child);
            return null;
        }

        /**
         * Visits composition relations for "AcceptChangeEventAction" elements and its super classes.
         */
        @objid ("65f277bd-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitAcceptChangeEventAction(final AcceptChangeEventAction child) {
            super.visitAcceptChangeEventAction(child);
            return null;
        }

        /**
         * Visits composition relations for "AcceptSignalAction" elements and its super classes.
         */
        @objid ("65f277c7-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitAcceptSignalAction(final AcceptSignalAction child) {
            super.visitAcceptSignalAction(child);
            return null;
        }

        /**
         * Visits composition relations for "AcceptTimeEventAction" elements and its super classes.
         */
        @objid ("65f277d1-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitAcceptTimeEventAction(final AcceptTimeEventAction child) {
            super.visitAcceptTimeEventAction(child);
            return null;
        }

        /**
         * Visits composition relations for "Activity" elements and its super classes.
         */
        @objid ("65f277db-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitActivity(final Activity child) {
            super.visitActivity(child);
            return null;
        }

        /**
         * Visits composition relations for "ActivityAction" elements and its super classes.
         */
        @objid ("65f277e5-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitActivityAction(final ActivityAction child) {
            if (this.unmaskStructuringLink) {
                for (ExceptionHandler i : child.getHandler()) {
                    addRight(i, i);
                }
            }
            
            super.visitActivityAction(child);
            return null;
        }

        /**
         * Visits composition relations for "ActivityDiagram" elements and its super classes.
         */
        @objid ("65f277ef-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitActivityDiagram(final ActivityDiagram child) {
            super.visitActivityDiagram(child);
            return null;
        }

        /**
         * Visits composition relations for "ActivityEdge" elements and its super classes.
         */
        @objid ("65f4d9f7-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitActivityEdge(final ActivityEdge child) {
            super.visitActivityEdge(child);
            return null;
        }

        /**
         * Visits composition relations for "ActivityFinalNode" elements and its super classes.
         */
        @objid ("65f4da01-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitActivityFinalNode(final ActivityFinalNode child) {
            super.visitActivityFinalNode(child);
            return null;
        }

        /**
         * Visits composition relations for "ActivityGroup" elements and its super classes.
         */
        @objid ("65f4da0b-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitActivityGroup(final ActivityGroup child) {
            super.visitActivityGroup(child);
            return null;
        }

        /**
         * Visits composition relations for "ActivityNode" elements and its super classes.
         */
        @objid ("65f4da15-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitActivityNode(final ActivityNode child) {
            if (this.unmaskStructuringLink) {
                for (ActivityEdge i : child.getOutgoing()) {
                    addRight(i, i.getTarget());
                }
            }
            
            super.visitActivityNode(child);
            return null;
        }

        /**
         * Visits composition relations for "ActivityParameterNode" elements and its super classes.
         */
        @objid ("65f4da1f-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitActivityParameterNode(final ActivityParameterNode child) {
            super.visitActivityParameterNode(child);
            return null;
        }

        /**
         * Visits composition relations for "ActivityPartition" elements and its super classes.
         */
        @objid ("65f4da29-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitActivityPartition(final ActivityPartition child) {
            super.visitActivityPartition(child);
            return null;
        }

        /**
         * Visits composition relations for "Actor" elements and its super classes.
         */
        @objid ("65f4da33-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitActor(final Actor child) {
            super.visitActor(child);
            return null;
        }

        /**
         * Visits composition relations for "RequirementProject" elements and its super classes.
         */
        @objid ("65f4da3d-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitAnalystProject(final AnalystProject child) {
            super.visitAnalystProject(child);
            return null;
        }

        /**
         * Visits composition relations for "Artifact" elements and its super classes.
         */
        @objid ("65f4da47-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitArtifact(final Artifact child) {
            if (this.unmaskStructuringLink) {
                for (Manifestation i : child.getUtilized()) {
                    addRight(i, i.getUtilizedElement());
                }
            }
            
            super.visitArtifact(child);
            return null;
        }

        /**
         * Visits composition relations for "AssociationEnd" elements and its super classes.
         */
        @objid ("65f73c5d-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitAssociationEnd(final AssociationEnd child) {
            super.visitAssociationEnd(child);
            return null;
        }

        /**
         * Visits composition relations for "Attribute" elements and its super classes.
         */
        @objid ("65f73c67-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitAttribute(final Attribute child) {
            super.visitAttribute(child);
            return null;
        }

        /**
         * Visits composition relations for "AttributeLink" elements and its super classes.
         */
        @objid ("65f73c71-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitAttributeLink(final AttributeLink child) {
            super.visitAttributeLink(child);
            return null;
        }

        /**
         * Visits composition relations for "Behavior" elements and its super classes.
         */
        @objid ("65f73c7b-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBehavior(final Behavior child) {
            super.visitBehavior(child);
            return null;
        }

        /**
         * Visits composition relations for "BehaviorDiagram" elements and its super classes.
         */
        @objid ("65f73c85-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBehaviorDiagram(final BehaviorDiagram child) {
            super.visitBehaviorDiagram(child);
            return null;
        }

        /**
         * Visits composition relations for "BehaviorParameter" elements and its super classes.
         */
        @objid ("65f73c8f-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBehaviorParameter(final BehaviorParameter child) {
            super.visitBehaviorParameter(child);
            return null;
        }

        /**
         * Visits composition relations for "BindableInstance" elements and its super classes.
         */
        @objid ("65f73c99-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBindableInstance(final BindableInstance child) {
            super.visitBindableInstance(child);
            return null;
        }

        /**
         * Visits composition relations for "Binding" elements and its super classes.
         */
        @objid ("65f73ca3-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBinding(final Binding child) {
            super.visitBinding(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnActivity" elements and its super classes.
         */
        @objid ("65f99eb1-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnActivity(final BpmnActivity child) {
            super.visitBpmnActivity(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnAdHocSubProcess" elements and its super classes.
         */
        @objid ("65f99ebb-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnAdHocSubProcess(final BpmnAdHocSubProcess child) {
            super.visitBpmnAdHocSubProcess(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnArtifact" elements and its super classes.
         */
        @objid ("65f99ec5-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnArtifact(final BpmnArtifact child) {
            super.visitBpmnArtifact(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnAssociation" elements and its super classes.
         */
        @objid ("65f99ecf-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnAssociation(final BpmnAssociation child) {
            super.visitBpmnAssociation(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnBaseElement" elements and its super classes.
         */
        @objid ("65f99ed9-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnBaseElement(final BpmnBaseElement child) {
            if (this.unmaskStructuringLink) {
                for (BpmnMessageFlow i : child.getOutgoingFlow()) {
                    addRight(i, i.getTargetRef());
                }
            
                for (BpmnMessageFlow i : child.getIncomingFlow()) {
                    addLeft(i, i.getSourceRef());
                }
            }
            super.visitBpmnBaseElement(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnBehavior" elements and its super classes.
         */
        @objid ("65f99ee3-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnBehavior(final BpmnBehavior child) {
            super.visitBpmnBehavior(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnBoundaryEvent" elements and its super classes.
         */
        @objid ("65f99eed-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnBoundaryEvent(final BpmnBoundaryEvent child) {
            super.visitBpmnBoundaryEvent(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnBusinessRuleTask" elements and its super classes.
         */
        @objid ("65f99ef7-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnBusinessRuleTask(final BpmnBusinessRuleTask child) {
            super.visitBpmnBusinessRuleTask(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnCallActivity" elements and its super classes.
         */
        @objid ("65fc010b-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnCallActivity(final BpmnCallActivity child) {
            super.visitBpmnCallActivity(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnCancelEventDefinition" elements and its super classes.
         */
        @objid ("65fc0115-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnCancelEventDefinition(final BpmnCancelEventDefinition child) {
            super.visitBpmnCancelEventDefinition(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnCatchEvent" elements and its super classes.
         */
        @objid ("65fc011f-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnCatchEvent(final BpmnCatchEvent child) {
            if (this.unmaskStructuringLink) {
                for (BpmnDataAssociation i : child.getDataOutputAssociation()) {
                    addRight(i, i.getTargetRef());
                }
            }
            
            super.visitBpmnCatchEvent(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnCollaboration" elements and its super classes.
         */
        @objid ("65fc0129-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnCollaboration(final BpmnCollaboration child) {
            if (this.unmaskStructuringLink) {
                for (BpmnMessageFlow i : child.getMessageFlow()) {
                    addRight(i, i.getTargetRef());
                }
            }
            
            super.visitBpmnCollaboration(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnCompensateEventDefinition" elements and its super classes.
         */
        @objid ("65fc0133-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnCompensateEventDefinition(final BpmnCompensateEventDefinition child) {
            super.visitBpmnCompensateEventDefinition(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnComplexBehaviorDefinition" elements and its super classes.
         */
        @objid ("65fc013d-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnComplexBehaviorDefinition(final BpmnComplexBehaviorDefinition child) {
            super.visitBpmnComplexBehaviorDefinition(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnComplexGateway" elements and its super classes.
         */
        @objid ("65fc0147-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnComplexGateway(final BpmnComplexGateway child) {
            super.visitBpmnComplexGateway(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnConditionalEventDefinition" elements and its super classes.
         */
        @objid ("65fe635e-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnConditionalEventDefinition(final BpmnConditionalEventDefinition child) {
            super.visitBpmnConditionalEventDefinition(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnDataAssociation" elements and its super classes.
         */
        @objid ("65fe6368-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnDataAssociation(final BpmnDataAssociation child) {
            super.visitBpmnDataAssociation(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnDataInput" elements and its super classes.
         */
        @objid ("65fe6372-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnDataInput(final BpmnDataInput child) {
            super.visitBpmnDataInput(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnDataObject" elements and its super classes.
         */
        @objid ("65fe637c-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnDataObject(final BpmnDataObject child) {
            super.visitBpmnDataObject(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnDataOutput" elements and its super classes.
         */
        @objid ("65fe6386-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnDataOutput(final BpmnDataOutput child) {
            super.visitBpmnDataOutput(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnDataState" elements and its super classes.
         */
        @objid ("65fe6390-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnDataState(final BpmnDataState child) {
            super.visitBpmnDataState(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnDataStore" elements and its super classes.
         */
        @objid ("65fe639a-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnDataStore(final BpmnDataStore child) {
            super.visitBpmnDataStore(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnEndEvent" elements and its super classes.
         */
        @objid ("65fe63a4-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnEndEvent(final BpmnEndEvent child) {
            super.visitBpmnEndEvent(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnEndPoint" elements and its super classes.
         */
        @objid ("6600c5b8-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnEndPoint(final BpmnEndPoint child) {
            super.visitBpmnEndPoint(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnErrorEventDefinition" elements and its super classes.
         */
        @objid ("6600c5c2-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnErrorEventDefinition(final BpmnErrorEventDefinition child) {
            super.visitBpmnErrorEventDefinition(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnEscalationEventDefinition" elements and its super classes.
         */
        @objid ("6600c5cc-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnEscalationEventDefinition(final BpmnEscalationEventDefinition child) {
            super.visitBpmnEscalationEventDefinition(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnEvent" elements and its super classes.
         */
        @objid ("6600c5d6-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnEvent(final BpmnEvent child) {
            super.visitBpmnEvent(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnEventBasedGateway" elements and its super classes.
         */
        @objid ("6600c5e0-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnEventBasedGateway(final BpmnEventBasedGateway child) {
            super.visitBpmnEventBasedGateway(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnEventDefinition" elements and its super classes.
         */
        @objid ("6600c5ea-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnEventDefinition(final BpmnEventDefinition child) {
            super.visitBpmnEventDefinition(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnExclusiveGateway" elements and its super classes.
         */
        @objid ("6600c5f4-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnExclusiveGateway(final BpmnExclusiveGateway child) {
            super.visitBpmnExclusiveGateway(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnFlowElement" elements and its super classes.
         */
        @objid ("6600c5fe-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnFlowElement(final BpmnFlowElement child) {
            super.visitBpmnFlowElement(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnFlowNode" elements and its super classes.
         */
        @objid ("66032817-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnFlowNode(final BpmnFlowNode child) {
            if (this.unmaskStructuringLink) {
                for (BpmnSequenceFlow i : child.getOutgoing()) {
                    addRight(i, i.getTargetRef());
                }
            
                for (BpmnSequenceFlow i : child.getIncoming()) {
                    addLeft(i, i.getSourceRef());
                }
            }
            super.visitBpmnFlowNode(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnGateway" elements and its super classes.
         */
        @objid ("66032821-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnGateway(final BpmnGateway child) {
            super.visitBpmnGateway(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnGroup" elements and its super classes.
         */
        @objid ("6603282b-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnGroup(final BpmnGroup child) {
            super.visitBpmnGroup(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnImplicitThrowEvent" elements and its super classes.
         */
        @objid ("66032835-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnImplicitThrowEvent(final BpmnImplicitThrowEvent child) {
            super.visitBpmnImplicitThrowEvent(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnInclusiveGateway" elements and its super classes.
         */
        @objid ("6603283f-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnInclusiveGateway(final BpmnInclusiveGateway child) {
            super.visitBpmnInclusiveGateway(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnInterface" elements and its super classes.
         */
        @objid ("66032849-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnInterface(final BpmnInterface child) {
            super.visitBpmnInterface(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnIntermediateCatchEvent" elements and its super classes.
         */
        @objid ("66032853-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnIntermediateCatchEvent(final BpmnIntermediateCatchEvent child) {
            super.visitBpmnIntermediateCatchEvent(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnIntermediateThrowEvent" elements and its super classes.
         */
        @objid ("66058a6c-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnIntermediateThrowEvent(final BpmnIntermediateThrowEvent child) {
            super.visitBpmnIntermediateThrowEvent(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnItemAwareElement" elements and its super classes.
         */
        @objid ("66058a76-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnItemAwareElement(final BpmnItemAwareElement child) {
            super.visitBpmnItemAwareElement(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnItemDefinition" elements and its super classes.
         */
        @objid ("66058a80-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnItemDefinition(final BpmnItemDefinition child) {
            super.visitBpmnItemDefinition(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnLane" elements and its super classes.
         */
        @objid ("66058a8a-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnLane(final BpmnLane child) {
            super.visitBpmnLane(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnLaneSet" elements and its super classes.
         */
        @objid ("66058a94-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnLaneSet(final BpmnLaneSet child) {
            super.visitBpmnLaneSet(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnLinkEventDefinition" elements and its super classes.
         */
        @objid ("66058a9e-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnLinkEventDefinition(final BpmnLinkEventDefinition child) {
            super.visitBpmnLinkEventDefinition(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnLoopCharacteristics" elements and its super classes.
         */
        @objid ("66058aa8-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnLoopCharacteristics(final BpmnLoopCharacteristics child) {
            super.visitBpmnLoopCharacteristics(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnManualTask" elements and its super classes.
         */
        @objid ("66058ab2-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnManualTask(final BpmnManualTask child) {
            super.visitBpmnManualTask(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnMessage" elements and its super classes.
         */
        @objid ("6607ecce-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnMessage(final BpmnMessage child) {
            super.visitBpmnMessage(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnMessageEventDefinition" elements and its super classes.
         */
        @objid ("6607ecd8-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnMessageEventDefinition(final BpmnMessageEventDefinition child) {
            super.visitBpmnMessageEventDefinition(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnMessageFlow" elements and its super classes.
         */
        @objid ("6607ece2-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnMessageFlow(final BpmnMessageFlow child) {
            super.visitBpmnMessageFlow(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnMultiInstanceLoopCharacteristics" elements and its super classes.
         */
        @objid ("6607ecec-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnMultiInstanceLoopCharacteristics(final BpmnMultiInstanceLoopCharacteristics child) {
            super.visitBpmnMultiInstanceLoopCharacteristics(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnOperation" elements and its super classes.
         */
        @objid ("6607ecf6-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnOperation(final BpmnOperation child) {
            super.visitBpmnOperation(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnParallelGateway" elements and its super classes.
         */
        @objid ("6607ed00-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnParallelGateway(final BpmnParallelGateway child) {
            super.visitBpmnParallelGateway(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnParticipant" elements and its super classes.
         */
        @objid ("6607ed0a-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnParticipant(final BpmnParticipant child) {
            super.visitBpmnParticipant(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnProcess" elements and its super classes.
         */
        @objid ("660a4f27-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnProcess(final BpmnProcess child) {
            super.visitBpmnProcess(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnProcessCollaborationDiagram" elements and its super classes.
         */
        @objid ("660a4f31-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnProcessCollaborationDiagram(final BpmnProcessCollaborationDiagram child) {
            super.visitBpmnProcessCollaborationDiagram(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnReceiveTask" elements and its super classes.
         */
        @objid ("660a4f3b-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnReceiveTask(final BpmnReceiveTask child) {
            super.visitBpmnReceiveTask(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnResource" elements and its super classes.
         */
        @objid ("660a4f45-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnResource(final BpmnResource child) {
            super.visitBpmnResource(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnResourceParameter" elements and its super classes.
         */
        @objid ("660a4f4f-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnResourceParameter(final BpmnResourceParameter child) {
            super.visitBpmnResourceParameter(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnResourceParameterBinding" elements and its super classes.
         */
        @objid ("660a4f59-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnResourceParameterBinding(final BpmnResourceParameterBinding child) {
            super.visitBpmnResourceParameterBinding(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnResourceRole" elements and its super classes.
         */
        @objid ("660a4f63-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnResourceRole(final BpmnResourceRole child) {
            super.visitBpmnResourceRole(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnRootElement" elements and its super classes.
         */
        @objid ("660cb181-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnRootElement(final BpmnRootElement child) {
            super.visitBpmnRootElement(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnScriptTask" elements and its super classes.
         */
        @objid ("660cb18b-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnScriptTask(final BpmnScriptTask child) {
            super.visitBpmnScriptTask(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnSendTask" elements and its super classes.
         */
        @objid ("660cb195-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnSendTask(final BpmnSendTask child) {
            super.visitBpmnSendTask(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnSequenceFlow" elements and its super classes.
         */
        @objid ("660cb19f-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnSequenceFlow(final BpmnSequenceFlow child) {
            super.visitBpmnSequenceFlow(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnSequenceFlowDataAssociation" elements and its super classes.
         */
        @objid ("660cb1a9-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnSequenceFlowDataAssociation(final BpmnSequenceFlowDataAssociation child) {
            super.visitBpmnSequenceFlowDataAssociation(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnServiceTask" elements and its super classes.
         */
        @objid ("660cb1b3-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnServiceTask(final BpmnServiceTask child) {
            super.visitBpmnServiceTask(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnSignalEventDefinition" elements and its super classes.
         */
        @objid ("660f13d4-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnSignalEventDefinition(final BpmnSignalEventDefinition child) {
            super.visitBpmnSignalEventDefinition(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnStandardLoopCharacteristics" elements and its super classes.
         */
        @objid ("660f13de-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnStandardLoopCharacteristics(final BpmnStandardLoopCharacteristics child) {
            super.visitBpmnStandardLoopCharacteristics(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnStartEvent" elements and its super classes.
         */
        @objid ("660f13e8-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnStartEvent(final BpmnStartEvent child) {
            super.visitBpmnStartEvent(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnSubProcess" elements and its super classes.
         */
        @objid ("660f13f2-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnSubProcess(final BpmnSubProcess child) {
            super.visitBpmnSubProcess(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnSubProcessDiagram" elements and its super classes.
         */
        @objid ("660f13fc-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnSubProcessDiagram(final BpmnSubProcessDiagram child) {
            super.visitBpmnSubProcessDiagram(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnTask" elements and its super classes.
         */
        @objid ("660f1406-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnTask(final BpmnTask child) {
            super.visitBpmnTask(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnTerminateEventDefinition" elements and its super classes.
         */
        @objid ("660f1410-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnTerminateEventDefinition(final BpmnTerminateEventDefinition child) {
            super.visitBpmnTerminateEventDefinition(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnThrowEvent" elements and its super classes.
         */
        @objid ("6611762f-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnThrowEvent(final BpmnThrowEvent child) {
            if (this.unmaskStructuringLink) {
                for (BpmnDataAssociation i : child.getDataInputAssociation()) {
                    addRight(i, i.getTargetRef());
                }
            }
            
            super.visitBpmnThrowEvent(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnTimerEventDefinition" elements and its super classes.
         */
        @objid ("66117639-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnTimerEventDefinition(final BpmnTimerEventDefinition child) {
            super.visitBpmnTimerEventDefinition(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnTransaction" elements and its super classes.
         */
        @objid ("66117643-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnTransaction(final BpmnTransaction child) {
            super.visitBpmnTransaction(child);
            return null;
        }

        /**
         * Visits composition relations for "BpmnUserTask" elements and its super classes.
         */
        @objid ("6611764d-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitBpmnUserTask(final BpmnUserTask child) {
            super.visitBpmnUserTask(child);
            return null;
        }

        /**
         * Visits composition relations for "CallAction" elements and its super classes.
         */
        @objid ("66117657-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitCallAction(final CallAction child) {
            super.visitCallAction(child);
            return null;
        }

        /**
         * Visits composition relations for "CallBehaviorAction" elements and its super classes.
         */
        @objid ("66117661-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitCallBehaviorAction(final CallBehaviorAction child) {
            super.visitCallBehaviorAction(child);
            return null;
        }

        /**
         * Visits composition relations for "CallOperationAction" elements and its super classes.
         */
        @objid ("6611766b-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitCallOperationAction(final CallOperationAction child) {
            super.visitCallOperationAction(child);
            return null;
        }

        /**
         * Visits composition relations for "CentralBufferNode" elements and its super classes.
         */
        @objid ("6613d890-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitCentralBufferNode(final CentralBufferNode child) {
            super.visitCentralBufferNode(child);
            return null;
        }

        /**
         * Visits composition relations for "ChoicePseudoState" elements and its super classes.
         */
        @objid ("6613d89a-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitChoicePseudoState(final ChoicePseudoState child) {
            super.visitChoicePseudoState(child);
            return null;
        }

        /**
         * Visits composition relations for "Class" elements and its super classes.
         */
        @objid ("6613d8a4-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitClass(final Class child) {
            super.visitClass(child);
            return null;
        }

        /**
         * Visits composition relations for "ClassAssociation" elements and its super classes.
         */
        @objid ("6613d8ae-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitClassAssociation(final ClassAssociation child) {
            super.visitClassAssociation(child);
            return null;
        }

        /**
         * Visits composition relations for "ClassDiagram" elements and its super classes.
         */
        @objid ("6613d8b8-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitClassDiagram(final ClassDiagram child) {
            super.visitClassDiagram(child);
            return null;
        }

        /**
         * Visits composition relations for "Classifier" elements and its super classes.
         */
        @objid ("6613d8c2-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitClassifier(final Classifier child) {
            if (this.unmaskStructuringLink) {
                for (AssociationEnd i : child.getTargetingEnd()) {
                    addLeft(i, i.getTarget());
                }
            
                for (AssociationEnd i : child.getOwnedEnd()) {
                    addRight(i, i.getTarget());
                }
            }
            
            super.visitClassifier(child);
            return null;
        }

        /**
         * Visits composition relations for "Clause" elements and its super classes.
         */
        @objid ("66163ae4-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitClause(final Clause child) {
            super.visitClause(child);
            return null;
        }

        /**
         * Visits composition relations for "Collaboration" elements and its super classes.
         */
        @objid ("66163aee-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitCollaboration(final Collaboration child) {
            super.visitCollaboration(child);
            return null;
        }

        /**
         * Visits composition relations for "CollaborationUse" elements and its super classes.
         */
        @objid ("66163af8-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitCollaborationUse(final CollaborationUse child) {
            if (this.unmaskStructuringLink) {
                for (Binding i : child.getRoleBinding()) {
                    addRight(i, i);
                }
            }
            
            super.visitCollaborationUse(child);
            return null;
        }

        /**
         * Visits composition relations for "CombinedFragment" elements and its super classes.
         */
        @objid ("66163b02-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitCombinedFragment(final CombinedFragment child) {
            super.visitCombinedFragment(child);
            return null;
        }

        /**
         * Visits composition relations for "CommunicationDiagram" elements and its super classes.
         */
        @objid ("66163b16-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitCommunicationDiagram(final CommunicationDiagram child) {
            super.visitCommunicationDiagram(child);
            return null;
        }

        /**
         * Visits composition relations for "CommunicationInteraction" elements and its super classes.
         */
        @objid ("66189d3c-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitCommunicationInteraction(final CommunicationInteraction child) {
            super.visitCommunicationInteraction(child);
            return null;
        }

        /**
         * Visits composition relations for "CommunicationMessage" elements and its super classes.
         */
        @objid ("66189d46-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitCommunicationMessage(final CommunicationMessage child) {
            super.visitCommunicationMessage(child);
            return null;
        }

        /**
         * Visits composition relations for "CommunicationNode" elements and its super classes.
         */
        @objid ("66189d50-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitCommunicationNode(final CommunicationNode child) {
            if (this.unmaskStructuringLink) {
                for (CommunicationChannel i : child.getStarted()) {
                    addRight(i, i.getEnd());
                }
            
                for (CommunicationChannel i : child.getEnded()) {
                    addLeft(i, i.getStart());
                }
            }
            
            super.visitCommunicationNode(child);
            return null;
        }

        /**
         * Visits composition relations for "Component" elements and its super classes.
         */
        @objid ("66189d5a-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitComponent(final Component child) {
            super.visitComponent(child);
            return null;
        }

        /**
         * Visits composition relations for "ConditionalNode" elements and its super classes.
         */
        @objid ("66189d64-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitConditionalNode(final ConditionalNode child) {
            super.visitConditionalNode(child);
            return null;
        }

        /**
         * Visits composition relations for "ConfigParam" elements and its super classes.
         */
        @objid ("66189d6e-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitModuleParameter(final ModuleParameter child) {
            super.visitModuleParameter(child);
            return null;
        }

        /**
         * Visits composition relations for "ConnectionPointReference" elements and its super classes.
         */
        @objid ("66189d78-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitConnectionPointReference(final ConnectionPointReference child) {
            super.visitConnectionPointReference(child);
            return null;
        }

        /**
         * Visits composition relations for "ConnectorEnd" elements and its super classes.
         */
        @objid ("661affa9-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitConnectorEnd(final ConnectorEnd child) {
            super.visitConnectorEnd(child);
            return null;
        }

        /**
         * Visits composition relations for "Constraint" elements and its super classes.
         */
        @objid ("661affb3-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitConstraint(final Constraint child) {
            super.visitConstraint(child);
            return null;
        }

        /**
         * Visits composition relations for "ControlFlow" elements and its super classes.
         */
        @objid ("661affbd-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitControlFlow(final ControlFlow child) {
            super.visitControlFlow(child);
            return null;
        }

        /**
         * Visits composition relations for "ControlNode" elements and its super classes.
         */
        @objid ("661affc7-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitControlNode(final ControlNode child) {
            super.visitControlNode(child);
            return null;
        }

        /**
         * Visits composition relations for "DataFlow" elements and its super classes.
         */
        @objid ("661affd1-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitDataFlow(final DataFlow child) {
            super.visitDataFlow(child);
            return null;
        }

        /**
         * Visits composition relations for "DataStoreNode" elements and its super classes.
         */
        @objid ("661d61f7-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitDataStoreNode(final DataStoreNode child) {
            super.visitDataStoreNode(child);
            return null;
        }

        /**
         * Visits composition relations for "DataType" elements and its super classes.
         */
        @objid ("661d6201-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitDataType(final DataType child) {
            super.visitDataType(child);
            return null;
        }

        /**
         * Visits composition relations for "DecisionMergeNode" elements and its super classes.
         */
        @objid ("661d620b-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitDecisionMergeNode(final DecisionMergeNode child) {
            super.visitDecisionMergeNode(child);
            return null;
        }

        /**
         * Visits composition relations for "DeepHistoryPseudoState" elements and its super classes.
         */
        @objid ("661d6215-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitDeepHistoryPseudoState(final DeepHistoryPseudoState child) {
            super.visitDeepHistoryPseudoState(child);
            return null;
        }

        /**
         * Visits composition relations for "Dependency" elements and its super classes.
         */
        @objid ("661d621f-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitDependency(final Dependency child) {
            super.visitDependency(child);
            return null;
        }

        /**
         * Visits composition relations for "DeploymentDiagram" elements and its super classes.
         */
        @objid ("661d6229-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitDeploymentDiagram(final DeploymentDiagram child) {
            super.visitDeploymentDiagram(child);
            return null;
        }

        /**
         * Visits composition relations for "DiagramSet" elements and its super classes.
         */
        @objid ("661fc452-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitDiagramSet(final DiagramSet child) {
            super.visitDiagramSet(child);
            return null;
        }

        /**
         * Visits composition relations for "Dictionary" elements and its super classes.
         */
        @objid ("661fc45c-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitDictionary(final Dictionary child) {
            super.visitDictionary(child);
            return null;
        }

        @objid ("661fc466-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitAnalystElement(final AnalystElement child) {
            super.visitAnalystElement(child);
            return null;
        }

        /**
         * Visits composition relations for "DurationConstraint" elements and its super classes.
         */
        @objid ("661fc470-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitDurationConstraint(final DurationConstraint child) {
            super.visitDurationConstraint(child);
            return null;
        }

        /**
         * Visits composition relations for "MObject" elements and its super classes.
         */
        @objid ("661fc47a-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitElement(final Element child) {
            super.visitElement(child);
            return null;
        }

        /**
         * Visits composition relations for "ElementImport" elements and its super classes.
         */
        @objid ("661fc482-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitElementImport(final ElementImport child) {
            super.visitElementImport(child);
            return null;
        }

        /**
         * Visits composition relations for "ElementRealization" elements and its super classes.
         */
        @objid ("662226aa-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitElementRealization(final ElementRealization child) {
            super.visitElementRealization(child);
            return null;
        }

        /**
         * Visits composition relations for "EntryPointPseudoState" elements and its super classes.
         */
        @objid ("662226b4-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitEntryPointPseudoState(final EntryPointPseudoState child) {
            super.visitEntryPointPseudoState(child);
            return null;
        }

        /**
         * Visits composition relations for "EnumeratedPropertyType" elements and its super classes.
         */
        @objid ("662226be-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitEnumeratedPropertyType(final EnumeratedPropertyType child) {
            super.visitEnumeratedPropertyType(child);
            return null;
        }

        /**
         * Visits composition relations for "Enumeration" elements and its super classes.
         */
        @objid ("662226c8-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitEnumeration(final Enumeration child) {
            super.visitEnumeration(child);
            return null;
        }

        /**
         * Visits composition relations for "EnumerationLiteral" elements and its super classes.
         */
        @objid ("662226d2-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitEnumerationLiteral(final EnumerationLiteral child) {
            super.visitEnumerationLiteral(child);
            return null;
        }

        /**
         * Visits composition relations for "Event" elements and its super classes.
         */
        @objid ("662226dc-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitEvent(final Event child) {
            super.visitEvent(child);
            return null;
        }

        /**
         * Visits composition relations for "ExceptionHandler" elements and its super classes.
         */
        @objid ("66248906-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitExceptionHandler(final ExceptionHandler child) {
            super.visitExceptionHandler(child);
            return null;
        }

        /**
         * Visits composition relations for "ExecutionOccurenceSpecification" elements and its super classes.
         */
        @objid ("66248910-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitExecutionOccurenceSpecification(final ExecutionOccurenceSpecification child) {
            super.visitExecutionOccurenceSpecification(child);
            return null;
        }

        /**
         * Visits composition relations for "ExecutionSpecification" elements and its super classes.
         */
        @objid ("6624891a-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitExecutionSpecification(final ExecutionSpecification child) {
            super.visitExecutionSpecification(child);
            return null;
        }

        /**
         * Visits composition relations for "ExitPointPseudoState" elements and its super classes.
         */
        @objid ("66248924-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitExitPointPseudoState(final ExitPointPseudoState child) {
            super.visitExitPointPseudoState(child);
            return null;
        }

        /**
         * Visits composition relations for "ExpansionNode" elements and its super classes.
         */
        @objid ("6624892e-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitExpansionNode(final ExpansionNode child) {
            super.visitExpansionNode(child);
            return null;
        }

        /**
         * Visits composition relations for "ExpansionRegion" elements and its super classes.
         */
        @objid ("6626eb58-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitExpansionRegion(final ExpansionRegion child) {
            super.visitExpansionRegion(child);
            return null;
        }

        /**
         * Visits composition relations for "ExtensionPoint" elements and its super classes.
         */
        @objid ("6626eb62-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitExtensionPoint(final ExtensionPoint child) {
            super.visitExtensionPoint(child);
            return null;
        }

        /**
         * Visits composition relations for "Feature" elements and its super classes.
         */
        @objid ("6626eb6c-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitFeature(final Feature child) {
            super.visitFeature(child);
            return null;
        }

        /**
         * Visits composition relations for "FinalNode" elements and its super classes.
         */
        @objid ("6626eb76-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitFinalNode(final FinalNode child) {
            super.visitFinalNode(child);
            return null;
        }

        /**
         * Visits composition relations for "FinalState" elements and its super classes.
         */
        @objid ("6626eb80-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitFinalState(final FinalState child) {
            super.visitFinalState(child);
            return null;
        }

        /**
         * Visits composition relations for "FlowFinalNode" elements and its super classes.
         */
        @objid ("6626eb8a-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitFlowFinalNode(final FlowFinalNode child) {
            super.visitFlowFinalNode(child);
            return null;
        }

        /**
         * Visits composition relations for "ForkJoinNode" elements and its super classes.
         */
        @objid ("66294db6-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitForkJoinNode(final ForkJoinNode child) {
            super.visitForkJoinNode(child);
            return null;
        }

        /**
         * Visits composition relations for "ForkPseudoState" elements and its super classes.
         */
        @objid ("66294dc0-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitForkPseudoState(final ForkPseudoState child) {
            super.visitForkPseudoState(child);
            return null;
        }

        /**
         * Visits composition relations for "Gate" elements and its super classes.
         */
        @objid ("66294dca-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitGate(final Gate child) {
            super.visitGate(child);
            return null;
        }

        /**
         * Visits composition relations for "GeneralClass" elements and its super classes.
         */
        @objid ("66294dd4-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitGeneralClass(final GeneralClass child) {
            super.visitGeneralClass(child);
            return null;
        }

        /**
         * Visits composition relations for "Generalization" elements and its super classes.
         */
        @objid ("66294dde-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitGeneralization(final Generalization child) {
            super.visitGeneralization(child);
            return null;
        }

        /**
         * Visits composition relations for "GeneralOrdering" elements and its super classes.
         */
        @objid ("662bb00c-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitGeneralOrdering(final GeneralOrdering child) {
            super.visitGeneralOrdering(child);
            return null;
        }

        /**
         * Visits composition relations for "InformationFlow" elements and its super classes.
         */
        @objid ("662bb016-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitInformationFlow(final InformationFlow child) {
            super.visitInformationFlow(child);
            return null;
        }

        /**
         * Visits composition relations for "InformationItem" elements and its super classes.
         */
        @objid ("662bb020-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitInformationItem(final InformationItem child) {
            super.visitInformationItem(child);
            return null;
        }

        /**
         * Visits composition relations for "InitialNode" elements and its super classes.
         */
        @objid ("662bb02a-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitInitialNode(final InitialNode child) {
            super.visitInitialNode(child);
            return null;
        }

        /**
         * Visits composition relations for "InitialPseudoState" elements and its super classes.
         */
        @objid ("662bb034-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitInitialPseudoState(final InitialPseudoState child) {
            super.visitInitialPseudoState(child);
            return null;
        }

        /**
         * Visits composition relations for "InputPin" elements and its super classes.
         */
        @objid ("662bb03e-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitInputPin(final InputPin child) {
            super.visitInputPin(child);
            return null;
        }

        /**
         * Visits composition relations for "Instance" elements and its super classes.
         */
        @objid ("662e1267-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitInstance(final Instance child) {
            if (this.unmaskStructuringLink) {
                for (LinkEnd i : child.getOwnedEnd()) {
                    if (i.isNavigable()) {
                        addRight(i, i.getTarget());
                    } else {
                        addLeft(i, i.getTarget());
                    }
                }
            }
            super.visitInstance(child);
            return null;
        }

        /**
         * Visits composition relations for "InstanceNode" elements and its super classes.
         */
        @objid ("662e1271-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitInstanceNode(final InstanceNode child) {
            super.visitInstanceNode(child);
            return null;
        }

        /**
         * Visits composition relations for "Interaction" elements and its super classes.
         */
        @objid ("662e127b-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitInteraction(final Interaction child) {
            super.visitInteraction(child);
            return null;
        }

        /**
         * Visits composition relations for "InteractionFragment" elements and its super classes.
         */
        @objid ("662e1285-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitInteractionFragment(final InteractionFragment child) {
            super.visitInteractionFragment(child);
            return null;
        }

        /**
         * Visits composition relations for "InteractionOperand" elements and its super classes.
         */
        @objid ("662e128f-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitInteractionOperand(final InteractionOperand child) {
            super.visitInteractionOperand(child);
            return null;
        }

        /**
         * Visits composition relations for "InteractionUse" elements and its super classes.
         */
        @objid ("663074c0-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitInteractionUse(final InteractionUse child) {
            super.visitInteractionUse(child);
            return null;
        }

        /**
         * Visits composition relations for "Interface" elements and its super classes.
         */
        @objid ("663074ca-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitInterface(final Interface child) {
            if (this.unmaskStructuringLink) {
                for (InterfaceRealization i : child.getImplementedLink()) {
                    addTop(i, i.getImplementer());
                }
            }
            
            super.visitInterface(child);
            return null;
        }

        /**
         * Visits composition relations for "InterfaceRealization" elements and its super classes.
         */
        @objid ("663074d4-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitInterfaceRealization(final InterfaceRealization child) {
            super.visitInterfaceRealization(child);
            return null;
        }

        /**
         * Visits composition relations for "InternalTransition" elements and its super classes.
         */
        @objid ("663074de-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitInternalTransition(final InternalTransition child) {
            super.visitInternalTransition(child);
            return null;
        }

        /**
         * Visits composition relations for "InterruptibleActivityRegion" elements and its super classes.
         */
        @objid ("663074e8-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitInterruptibleActivityRegion(final InterruptibleActivityRegion child) {
            super.visitInterruptibleActivityRegion(child);
            return null;
        }

        /**
         * Visits composition relations for "JoinPseudoState" elements and its super classes.
         */
        @objid ("6632d71a-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitJoinPseudoState(final JoinPseudoState child) {
            super.visitJoinPseudoState(child);
            return null;
        }

        /**
         * Visits composition relations for "JunctionPseudoState" elements and its super classes.
         */
        @objid ("6632d724-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitJunctionPseudoState(final JunctionPseudoState child) {
            super.visitJunctionPseudoState(child);
            return null;
        }

        /**
         * Visits composition relations for "Lifeline" elements and its super classes.
         */
        @objid ("6632d72e-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitLifeline(final Lifeline child) {
            super.visitLifeline(child);
            return null;
        }

        /**
         * Visits composition relations for "LinkEnd" elements and its super classes.
         */
        @objid ("6632d742-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitLinkEnd(final LinkEnd child) {
            super.visitLinkEnd(child);
            return null;
        }

        /**
         * Visits composition relations for "LoopNode" elements and its super classes.
         */
        @objid ("66353982-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitLoopNode(final LoopNode child) {
            super.visitLoopNode(child);
            return null;
        }

        /**
         * Visits composition relations for "Manifestation" elements and its super classes.
         */
        @objid ("6635398c-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitManifestation(final Manifestation child) {
            super.visitManifestation(child);
            return null;
        }

        /**
         * Visits composition relations for "Message" elements and its super classes.
         */
        @objid ("66353996-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitMessage(final Message child) {
            super.visitMessage(child);
            return null;
        }

        /**
         * Visits composition relations for "MessageEnd" elements and its super classes.
         */
        @objid ("663539a0-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitMessageEnd(final MessageEnd child) {
            super.visitMessageEnd(child);
            return null;
        }

        /**
         * Visits composition relations for "MessageFlow" elements and its super classes.
         */
        @objid ("66379bce-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitMessageFlow(final MessageFlow child) {
            super.visitMessageFlow(child);
            return null;
        }

        /**
         * Visits composition relations for "MetaclassReference" elements and its super classes.
         */
        @objid ("66379bd8-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitMetaclassReference(final MetaclassReference child) {
            super.visitMetaclassReference(child);
            return null;
        }

        /**
         * Visits composition relations for "ModelElement" elements and its super classes.
         */
        @objid ("66379be2-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitModelElement(final ModelElement child) {
            if (!this.unmaskStructuringLink) {
                for (Dependency i : child.getDependsOnDependency()) {
                    addRight(i, i.getDependsOn());
                }
            
                for (InformationFlow i : child.getSentInfo()) {
                    // Ignore info flow with 0 or n > 1 targets
                    if (i.getInformationTarget().size() == 1) {
                        addRight(i, i.getInformationTarget().get(0));
                    }
                }
            }
            
            super.visitModelElement(child);
            return null;
        }

        /**
         * Visits composition relations for "ModelTree" elements and its super classes.
         */
        @objid ("66379bec-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitModelTree(final ModelTree child) {
            super.visitModelTree(child);
            return null;
        }

        /**
         * Visits composition relations for "Module" elements and its super classes.
         */
        @objid ("66379bf6-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitModuleComponent(final ModuleComponent child) {
            super.visitModuleComponent(child);
            return null;
        }

        /**
         * Visits composition relations for "NameSpace" elements and its super classes.
         */
        @objid ("6639fe28-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitNameSpace(final NameSpace child) {
            if (this.unmaskStructuringLink) {
                for (Generalization i : child.getParent()) {
                    addTop(i, i.getSuperType());
                }
            }
            
            if (this.unmaskStructuringLink) {
                for (Generalization i : child.getSpecialization()) {
                    addBottom(i, i.getSubType());
                }
            }
            
            if (this.unmaskStructuringLink) {
                for (InterfaceRealization i : child.getRealized()) {
                    addTop(i, i.getImplemented());
                }
            }
            
            if (!this.unmaskStructuringLink) {
                for (DataFlow i : child.getOwnedDataFlow()) {
                    addRight(i, i.getDestination());
                }
            }
            
            if (!this.unmaskStructuringLink) {
                for (PackageImport i : child.getOwnedPackageImport()) {
                    addRight(i, i.getImportingNameSpace());
                    addRight(i, i.getImportingOperation());
                }
            }
            
            if (!this.unmaskStructuringLink) {
                for (ElementImport i : child.getOwnedImport()) {
                    addRight(i, i.getImportedElement());
                }
            }
            
            if (this.unmaskStructuringLink) {
                for (TemplateBinding i : child.getTemplateInstanciation()) {
                    addRight(i, i.getInstanciatedTemplate());
                    addRight(i, i.getInstanciatedTemplateOperation());
                }
            }
            super.visitNameSpace(child);
            return null;
        }

        /**
         * Visits composition relations for "NamespaceUse" elements and its super classes.
         */
        @objid ("6639fe32-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitNamespaceUse(final NamespaceUse child) {
            super.visitNamespaceUse(child);
            return null;
        }

        /**
         * Visits composition relations for "Node" elements and its super classes.
         */
        @objid ("6639fe3c-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitNode(final Node child) {
            super.visitNode(child);
            return null;
        }

        /**
         * Visits composition relations for "Note" elements and its super classes.
         */
        @objid ("6639fe46-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitNote(final Note child) {
            super.visitNote(child);
            return null;
        }

        /**
         * Visits composition relations for "NoteType" elements and its super classes.
         */
        @objid ("6639fe50-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitNoteType(final NoteType child) {
            super.visitNoteType(child);
            return null;
        }

        /**
         * Visits composition relations for "ObjectDiagram" elements and its super classes.
         */
        @objid ("6639fe5a-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitObjectDiagram(final ObjectDiagram child) {
            super.visitObjectDiagram(child);
            return null;
        }

        /**
         * Visits composition relations for "ObjectFlow" elements and its super classes.
         */
        @objid ("663c608b-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitObjectFlow(final ObjectFlow child) {
            super.visitObjectFlow(child);
            return null;
        }

        /**
         * Visits composition relations for "ObjectNode" elements and its super classes.
         */
        @objid ("663c6095-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitObjectNode(final ObjectNode child) {
            super.visitObjectNode(child);
            return null;
        }

        /**
         * Visits composition relations for "OccurrenceSpecification" elements and its super classes.
         */
        @objid ("663c609f-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitOccurrenceSpecification(final OccurrenceSpecification child) {
            super.visitOccurrenceSpecification(child);
            return null;
        }

        /**
         * Visits composition relations for "OpaqueAction" elements and its super classes.
         */
        @objid ("663c60a9-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitOpaqueAction(final OpaqueAction child) {
            super.visitOpaqueAction(child);
            return null;
        }

        /**
         * Visits composition relations for "OpaqueBehavior" elements and its super classes.
         */
        @objid ("663ec2dc-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitOpaqueBehavior(final OpaqueBehavior child) {
            super.visitOpaqueBehavior(child);
            return null;
        }

        /**
         * Visits composition relations for "Operation" elements and its super classes.
         */
        @objid ("663ec2e6-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitOperation(final Operation child) {
            if (!this.unmaskStructuringLink) {
                for (ElementImport i : child.getOwnedImport()) {
                    addRight(i, i.getImportedElement());
                }
            }
            
            if (!this.unmaskStructuringLink) {
                for (PackageImport i : child.getOwnedPackageImport()) {
                    addRight(i, i.getImportedPackage());
                }
            }
            
            if (this.unmaskStructuringLink) {
                for (RaisedException i : child.getThrown()) {
                    addRight(i, i.getThrownType());
                }
            }
            
            if (this.unmaskStructuringLink) {
                for (TemplateBinding i : child.getTemplateInstanciation()) {
                    addRight(i, i.getInstanciatedTemplate());
                    addRight(i, i.getInstanciatedTemplateOperation());
                }
            }
            
            super.visitOperation(child);
            return null;
        }

        /**
         * Visits composition relations for "OutputPin" elements and its super classes.
         */
        @objid ("663ec2f0-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitOutputPin(final OutputPin child) {
            super.visitOutputPin(child);
            return null;
        }

        /**
         * Visits composition relations for "Package" elements and its super classes.
         */
        @objid ("663ec2fa-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitPackage(final Package child) {
            if (!this.unmaskStructuringLink) {
                for (PackageMerge i : child.getMerge()) {
                    addRight(i, i.getMergedPackage());
                }
            }
            
            super.visitPackage(child);
            return null;
        }

        /**
         * Visits composition relations for "PackageImport" elements and its super classes.
         */
        @objid ("663ec304-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitPackageImport(final PackageImport child) {
            super.visitPackageImport(child);
            return null;
        }

        /**
         * Visits composition relations for "PackageMerge" elements and its super classes.
         */
        @objid ("663ec30e-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitPackageMerge(final PackageMerge child) {
            super.visitPackageMerge(child);
            return null;
        }

        /**
         * Visits composition relations for "Parameter" elements and its super classes.
         */
        @objid ("6641253f-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitParameter(final Parameter child) {
            super.visitParameter(child);
            return null;
        }

        /**
         * Visits composition relations for "PartDecomposition" elements and its super classes.
         */
        @objid ("66412549-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitPartDecomposition(final PartDecomposition child) {
            super.visitPartDecomposition(child);
            return null;
        }

        /**
         * Visits composition relations for "Pin" elements and its super classes.
         */
        @objid ("66412553-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitPin(final Pin child) {
            super.visitPin(child);
            return null;
        }

        /**
         * Visits composition relations for "Port" elements and its super classes.
         */
        @objid ("6641255d-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitPort(final Port child) {
            if (this.unmaskStructuringLink) {
                for (ProvidedInterface i : child.getProvided()) {
                    addRight(i, i);
                }
            }
            
            if (this.unmaskStructuringLink) {
                for (RequiredInterface i : child.getRequired()) {
                    addRight(i, i);
                }
            }
            
            if (this.unmaskStructuringLink) {
                for (LinkEnd i : child.getTargetingEnd()) {
                    addLeft(i, i.getTarget());
                }
            
                for (LinkEnd i : child.getOwnedEnd()) {
                    addRight(i, i.getTarget());
                }
            }
            
            super.visitPort(child);
            return null;
        }

        /**
         * Visits composition relations for "Profile" elements and its super classes.
         */
        @objid ("66412567-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitProfile(final Profile child) {
            super.visitProfile(child);
            return null;
        }

        /**
         * Visits composition relations for "Project" elements and its super classes.
         */
        @objid ("66438799-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitProject(final Project child) {
            super.visitProject(child);
            return null;
        }

        /**
         * Visits composition relations for "PropertyContainer" elements and its super classes.
         */
        @objid ("664387ad-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitPropertyContainer(final PropertyContainer child) {
            super.visitPropertyContainer(child);
            return null;
        }

        /**
         * Visits composition relations for "PropertyEnumerationLitteral" elements and its super classes.
         */
        @objid ("664387b7-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitPropertyEnumerationLitteral(final PropertyEnumerationLitteral child) {
            super.visitPropertyEnumerationLitteral(child);
            return null;
        }

        /**
         * Visits composition relations for "PropertyTableDefinition " elements and its super classes.
         */
        @objid ("6645e9ea-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitPropertyTableDefinition(final PropertyTableDefinition child) {
            super.visitPropertyTableDefinition (child);
            return null;
        }

        /**
         * Visits composition relations for "PropertyType" elements and its super classes.
         */
        @objid ("6645e9f4-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitPropertyType(final PropertyType child) {
            super.visitPropertyType(child);
            return null;
        }

        /**
         * Visits composition relations for "TypedPropertyTable " elements and its super classes.
         */
        @objid ("6645ea08-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitTypedPropertyTable(final TypedPropertyTable child) {
            super.visitTypedPropertyTable (child);
            return null;
        }

        /**
         * Visits composition relations for "ProvidedInterface" elements and its super classes.
         */
        @objid ("6645ea12-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitProvidedInterface(final ProvidedInterface child) {
            super.visitProvidedInterface(child);
            return null;
        }

        /**
         * Visits composition relations for "RaisedException" elements and its super classes.
         */
        @objid ("66484c44-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitRaisedException(final RaisedException child) {
            super.visitRaisedException(child);
            return null;
        }

        /**
         * Visits composition relations for "Region" elements and its super classes.
         */
        @objid ("66484c4e-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitRegion(final Region child) {
            super.visitRegion(child);
            return null;
        }

        /**
         * Visits composition relations for "RequiredInterface" elements and its super classes.
         */
        @objid ("66484c58-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitRequiredInterface(final RequiredInterface child) {
            super.visitRequiredInterface(child);
            return null;
        }

        /**
         * Visits composition relations for "SendSignalAction" elements and its super classes.
         */
        @objid ("664aaea8-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitSendSignalAction(final SendSignalAction child) {
            super.visitSendSignalAction(child);
            return null;
        }

        /**
         * Visits composition relations for "SequenceDiagram" elements and its super classes.
         */
        @objid ("664aaeb2-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitSequenceDiagram(final SequenceDiagram child) {
            super.visitSequenceDiagram(child);
            return null;
        }

        /**
         * Visits composition relations for "ShallowHistoryPseudoState" elements and its super classes.
         */
        @objid ("664aaebc-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitShallowHistoryPseudoState(final ShallowHistoryPseudoState child) {
            super.visitShallowHistoryPseudoState(child);
            return null;
        }

        /**
         * Visits composition relations for "Signal" elements and its super classes.
         */
        @objid ("664aaec6-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitSignal(final Signal child) {
            super.visitSignal(child);
            return null;
        }

        /**
         * Visits composition relations for "State" elements and its super classes.
         */
        @objid ("664d10f8-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitState(final State child) {
            super.visitState(child);
            return null;
        }

        /**
         * Visits composition relations for "StateInvariant" elements and its super classes.
         */
        @objid ("664d1102-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitStateInvariant(final StateInvariant child) {
            super.visitStateInvariant(child);
            return null;
        }

        /**
         * Visits composition relations for "StateMachine" elements and its super classes.
         */
        @objid ("664d110c-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitStateMachine(final StateMachine child) {
            super.visitStateMachine(child);
            return null;
        }

        /**
         * Visits composition relations for "StateMachineDiagram" elements and its super classes.
         */
        @objid ("664d1116-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitStateMachineDiagram(final StateMachineDiagram child) {
            super.visitStateMachineDiagram(child);
            return null;
        }

        /**
         * Visits composition relations for "StateVertex" elements and its super classes.
         */
        @objid ("664d1120-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitStateVertex(final StateVertex child) {
            if (this.unmaskStructuringLink) {
                for (Transition i : child.getOutGoing()) {
                    if (!(i instanceof InternalTransition)) {
                        addRight(i, i.getTarget());
                    }
                }
            }
            
            super.visitStateVertex(child);
            return null;
        }

        /**
         * Visits composition relations for "StaticDiagram" elements and its super classes.
         */
        @objid ("664f7357-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitStaticDiagram(final StaticDiagram child) {
            super.visitStaticDiagram(child);
            return null;
        }

        /**
         * Visits composition relations for "Stereotype" elements and its super classes.
         */
        @objid ("664f7361-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitStereotype(final Stereotype child) {
            super.visitStereotype(child);
            return null;
        }

        /**
         * Visits composition relations for "StructuredActivityNode" elements and its super classes.
         */
        @objid ("664f736b-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitStructuredActivityNode(final StructuredActivityNode child) {
            super.visitStructuredActivityNode(child);
            return null;
        }

        /**
         * Visits composition relations for "Substitution" elements and its super classes.
         */
        @objid ("664f7375-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitSubstitution(final Substitution child) {
            super.visitSubstitution(child);
            return null;
        }

        /**
         * Visits composition relations for "TaggedValue" elements and its super classes.
         */
        @objid ("6651d5ac-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitTaggedValue(final TaggedValue child) {
            super.visitTaggedValue(child);
            return null;
        }

        /**
         * Visits composition relations for "TagParameter" elements and its super classes.
         */
        @objid ("6651d5b6-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitTagParameter(final TagParameter child) {
            super.visitTagParameter(child);
            return null;
        }

        /**
         * Visits composition relations for "TagType" elements and its super classes.
         */
        @objid ("6651d5c0-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitTagType(final TagType child) {
            super.visitTagType(child);
            return null;
        }

        /**
         * Visits composition relations for "TemplateBinding" elements and its super classes.
         */
        @objid ("6651d5ca-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitTemplateBinding(final TemplateBinding child) {
            super.visitTemplateBinding(child);
            return null;
        }

        /**
         * Visits composition relations for "TemplateParameter" elements and its super classes.
         */
        @objid ("6651d5d4-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitTemplateParameter(final TemplateParameter child) {
            super.visitTemplateParameter(child);
            return null;
        }

        /**
         * Visits composition relations for "TemplateParameterSubstitution" elements and its super classes.
         */
        @objid ("66543806-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitTemplateParameterSubstitution(final TemplateParameterSubstitution child) {
            super.visitTemplateParameterSubstitution(child);
            return null;
        }

        /**
         * Visits composition relations for "Term" elements and its super classes.
         */
        @objid ("66543810-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitTerm(final Term child) {
            super.visitTerm(child);
            return null;
        }

        /**
         * Visits composition relations for "TerminatePseudoState" elements and its super classes.
         */
        @objid ("6654381a-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitTerminatePseudoState(final TerminatePseudoState child) {
            super.visitTerminatePseudoState(child);
            return null;
        }

        /**
         * Visits composition relations for "TerminateSpecification" elements and its super classes.
         */
        @objid ("66543824-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitTerminateSpecification(final TerminateSpecification child) {
            super.visitTerminateSpecification(child);
            return null;
        }

        /**
         * Visits composition relations for "Transition" elements and its super classes.
         */
        @objid ("6654382e-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitTransition(final Transition child) {
            super.visitTransition(child);
            return null;
        }

        /**
         * Visits composition relations for "Usage" elements and its super classes.
         */
        @objid ("66569a68-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitUsage(final Usage child) {
            super.visitUsage(child);
            return null;
        }

        /**
         * Visits composition relations for "UseCase" elements and its super classes.
         */
        @objid ("66569a72-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitUseCase(final UseCase child) {
            super.visitUseCase(child);
            return null;
        }

        /**
         * Visits composition relations for "UseCaseDependency" elements and its super classes.
         */
        @objid ("66569a7c-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitUseCaseDependency(final UseCaseDependency child) {
            super.visitUseCaseDependency(child);
            return null;
        }

        /**
         * Visits composition relations for "UseCaseDiagram" elements and its super classes.
         */
        @objid ("66569a86-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitUseCaseDiagram(final UseCaseDiagram child) {
            super.visitUseCaseDiagram(child);
            return null;
        }

        /**
         * Visits composition relations for "ValuePin" elements and its super classes.
         */
        @objid ("6658fcba-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Object visitValuePin(final ValuePin child) {
            super.visitValuePin(child);
            return null;
        }

    }

}
