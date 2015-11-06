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
                                    

package org.modelio.vaudit.nsuse;

import java.util.ArrayDeque;
import java.util.Deque;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.emf.common.util.EList;
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
import org.modelio.metamodel.uml.behavior.stateMachineModel.TerminatePseudoState;
import org.modelio.metamodel.uml.behavior.stateMachineModel.Transition;
import org.modelio.metamodel.uml.behavior.usecaseModel.Actor;
import org.modelio.metamodel.uml.behavior.usecaseModel.UseCaseDependency;
import org.modelio.metamodel.uml.informationFlow.DataFlow;
import org.modelio.metamodel.uml.informationFlow.InformationFlow;
import org.modelio.metamodel.uml.informationFlow.InformationItem;
import org.modelio.metamodel.uml.infrastructure.Abstraction;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.ExternDocument;
import org.modelio.metamodel.uml.infrastructure.ExternDocumentType;
import org.modelio.metamodel.uml.infrastructure.MetaclassReference;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Profile;
import org.modelio.metamodel.uml.infrastructure.Substitution;
import org.modelio.metamodel.uml.infrastructure.Usage;
import org.modelio.metamodel.uml.statik.Artifact;
import org.modelio.metamodel.uml.statik.Association;
import org.modelio.metamodel.uml.statik.AssociationEnd;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.BindableInstance;
import org.modelio.metamodel.uml.statik.Binding;
import org.modelio.metamodel.uml.statik.ClassAssociation;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.CollaborationUse;
import org.modelio.metamodel.uml.statik.Component;
import org.modelio.metamodel.uml.statik.Connector;
import org.modelio.metamodel.uml.statik.ConnectorEnd;
import org.modelio.metamodel.uml.statik.ElementImport;
import org.modelio.metamodel.uml.statik.ElementRealization;
import org.modelio.metamodel.uml.statik.Generalization;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.metamodel.uml.statik.InterfaceRealization;
import org.modelio.metamodel.uml.statik.LinkEnd;
import org.modelio.metamodel.uml.statik.Manifestation;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.NamespaceUse;
import org.modelio.metamodel.uml.statik.NaryAssociation;
import org.modelio.metamodel.uml.statik.NaryAssociationEnd;
import org.modelio.metamodel.uml.statik.NaryConnectorEnd;
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
import org.modelio.metamodel.visitors.AbstractModelVisitor;

/**
 * Analyses a model object and calls a {@link INsUseHandler handler}
 * each time a namespace usage is caused by the element.
 * <p>
 * Usage:
 * <li> instantiate with {@link NSUseAnalyser#NSUseAnalyser(INsUseHandler)} with a {@link INsUseHandler handler}
 * <li> call {@link #buildFor(Element)}
 */
@objid ("ceda606e-ea0c-4c63-b339-6ef9d56d5768")
class NSUseAnalyser extends AbstractModelVisitor {
    @objid ("1074c814-5571-4d45-980f-e03bbc865203")
    private INsUseHandler handler;

    /**
     * initialize the analyzer.
     * @param handler the namespace use handler.
     */
    @objid ("dc9ce97e-8fe1-41a7-9d7f-47895ef8c3a1")
    public NSUseAnalyser(INsUseHandler handler) {
        this.handler = handler;
    }

    @objid ("02c68b3b-3303-4aef-beeb-4de7e973009d")
    @Override
    public Object visitAbstraction(Abstraction obj) {
        return visitDependency(obj);
    }

    @objid ("0e023f37-bd94-4386-a0fa-f0994545296a")
    @Override
    public Object visitDependency(Dependency dep) {
        ModelElement src0 = dep.getImpacted();
        ModelElement dest0 = dep.getDependsOn();
        NameSpace src1;
        NameSpace dest1;
        src1 = NSUseUtils.getNameSpaceOwner(src0);
        dest1 = NSUseUtils.getNameSpaceOwner(dest0);
        addNSUses(src1, dest1, dep);
        return null;
    }

    @objid ("cc928f12-22b0-4e86-975b-9a513b6f5e46")
    @Override
    public Object visitProfile(Profile obj) {
        return null;
    }

    @objid ("ed516eec-e405-4eb7-a075-fbf9c320443b")
    @Override
    public Object visitSubstitution(Substitution subst) {
        NameSpace src = subst.getSubstitutingClassifier();
        NameSpace dest = subst.getContract();
        addNSUses(src, dest, subst);
        return null;
    }

    @objid ("f2e9d6a8-df73-47f2-8fb4-b9e51dc5523d")
    @Override
    public Object visitUsage(Usage obj) {
        return visitDependency(obj);
    }

    @objid ("1949cb31-6822-4fa0-9a10-96b1f6def541")
    @Override
    public Object visitMetaclassReference(MetaclassReference obj) {
        return null;
    }

    @objid ("129af507-8aa5-49d3-9f7a-166c34d2ad86")
    @Override
    public Object visitExternDocument(ExternDocument obj) {
        return null;
    }

    @objid ("1a4e41d1-7cda-49ee-92e4-8136d9450478")
    @Override
    public Object visitExternDocumentType(ExternDocumentType obj) {
        return null;
    }

    @objid ("167e7794-e06b-4db1-957a-a287567dc99f")
    @Override
    public Object visitArtifact(Artifact obj) {
        return visitClassifier(obj);
    }

    @objid ("d704cb3d-e1fd-40bd-bc0a-921941b7dedd")
    @Override
    public Object visitAssociationEnd(AssociationEnd end) {
        // Generate NSU if:
        // - non oriented association
        // - or bidirectional association.
        // In the other case generate only navigable roles
        if (end.isNavigable()) {
            NameSpace src = end.getOwner();
            NameSpace dest = end.getTarget();
            addNSUses(src, dest, end);
        } else {
            AssociationEnd opposite = end.getOpposite();
            if ((opposite != null) && (!opposite.isNavigable())) {
                NameSpace src = end.getOwner();
                NameSpace dest = opposite.getOwner();
                addNSUses(src, dest, end);
            }
        }
        return null;
    }

    @objid ("0d028aa4-02ae-48dd-8d69-82d59f90f947")
    @Override
    public Object visitAttribute(Attribute att) {
        Classifier type = att.getType();
        Classifier src = att.getOwner();
        if (src == null) {
            AssociationEnd role = att.getQualified();
            if (role != null)
                src = role.getOwner();
        }
        
        addNSUses(src, type, att);
        return null;
    }

    @objid ("edf4e065-c4be-4a22-a127-beeadac57212")
    @Override
    public Object visitBindableInstance(BindableInstance bi) {
        // Call inherited
        visitInstance(bi);
        
        // Take into account represented features
        NameSpace src = NSUseUtils.getNameSpaceOwner(bi);
        NameSpace dest = NSUseUtils.getNameSpaceOwner(bi.getRepresentedFeature());
        
        addNSUses(src, dest, bi);
        return null;
    }

    @objid ("1dec19b1-c978-440c-afd6-e557e81da2c9")
    @Override
    public Object visitBinding(Binding binding) {
        NameSpace src = NSUseUtils.getNameSpaceOwner(binding.getOwner());
        NameSpace dest = NSUseUtils.getNameSpaceOwner(binding.getRole());
        
        addNSUses(src, dest, binding);
        
        dest = NSUseUtils.getNameSpaceOwner(binding.getRepresentedFeature());
        addNSUses(src, dest, binding);
        return null;
    }

    @objid ("1e04e960-388a-497f-92c0-0d0a32ea15c5")
    @Override
    public Object visitClassAssociation(ClassAssociation theClassAssociation) {
        NameSpace dest = theClassAssociation.getClassPart();
        Association assoc = theClassAssociation.getAssociationPart();
        if (assoc != null) {
            int nbNavigable = 0;
            EList<AssociationEnd> end = assoc.getEnd();
            for (AssociationEnd role : end) {
                if (role.isValid() && role.isNavigable())
                    nbNavigable++;
            }
            // Generate all roles if:
            // - non oriented association (ie nbNavigable == 0)
            // - or bidirectional association (ie nbNavigable == 2)
            // In the other case generate only navigable roles
            boolean generateAll = (nbNavigable == 0) || (nbNavigable == 2);
        
            for (AssociationEnd role : end) {
                if (role.isValid() && (generateAll || role.isNavigable())) {
                    NameSpace src = role.getOwner();
                    addNSUses(src, dest, theClassAssociation);
                }
            }
        }
        
        NaryAssociation naryAssoc = theClassAssociation.getNaryAssociationPart();
        if (naryAssoc != null) {
            for (NaryAssociationEnd role : naryAssoc.getNaryEnd()) {
                if (role.isValid()) {
                    NameSpace src = role.getOwner();
                    addNSUses(src, dest, theClassAssociation);
                }
            }
        }
        return null;
    }

    @objid ("e4072098-0879-490d-829b-bf114f07d451")
    @Override
    public Object visitCollaborationUse(CollaborationUse theCollaborationUse) {
        NameSpace src = theCollaborationUse.getNRepresented();
        NameSpace dest = theCollaborationUse.getType();
        
        if (src == null) {
            Operation op = theCollaborationUse.getORepresented();
            if (op != null && op.isValid()) {
                src = op.getOwner();
            }
        }
        if (src != null && !src.isValid())
            src = null;
        
        if (dest != null && !dest.isValid())
            dest = null;
        
        addNSUses(src, dest, theCollaborationUse);
        return null;
    }

    @objid ("5fd4e1c4-1cc3-4208-91a5-41aa7b3e5dd1")
    @Override
    public Object visitComponent(Component obj) {
        return visitClass(obj);
    }

    @objid ("cd14e3f6-0480-4e78-b9a8-dfdab4a2f0e5")
    @Override
    public Object visitConnectorEnd(ConnectorEnd theConnectorEnd) {
        visitLinkEnd(theConnectorEnd);
        
        // Take into account represented features
        NameSpace src = NSUseUtils.getNameSpaceOwner(theConnectorEnd);
        ModelElement feat = theConnectorEnd.getRepresentedFeature();
        
        NameSpace dest = NSUseUtils.getNameSpaceOwner(feat);
        addNSUses(src, dest, theConnectorEnd);
        return null;
    }

    @objid ("40905d5c-975e-4217-91ab-92350d816cd5")
    @Override
    public Object visitElementImport(ElementImport theElementImport) {
        NameSpace src = theElementImport.getImportingNameSpace();
        NameSpace dest = theElementImport.getImportedElement();
        
        if (src == null) {
            Operation src2 = theElementImport.getImportingOperation();
            if (src2 != null)
                src = src2.getOwner();
        }
        
        addNSUses(src, dest, theElementImport);
        return null;
    }

    @objid ("9513c864-979d-44cb-9c50-f1b65cff481b")
    @Override
    public Object visitElementRealization(ElementRealization obj) {
        return visitDependency(obj);
    }

    @objid ("fa8c2a83-604b-4cf3-9c36-9b1184f73aa2")
    @Override
    public Object visitGeneralization(Generalization theGeneralization) {
        addNSUses(theGeneralization.getSubType(), theGeneralization.getSuperType(), theGeneralization);
        return null;
    }

    @objid ("bbbf5a01-804a-4cae-85e6-bd4473c7f685")
    @Override
    public Object visitInstance(Instance theInstance) {
        NameSpace src = NSUseUtils.getNameSpaceOwner(theInstance);
        NameSpace dest = theInstance.getBase();
        
        addNSUses(src, dest, theInstance);
        return null;
    }

    @objid ("26a61d79-7771-4972-8ba8-d6a03c081caa")
    @Override
    public Object visitInterfaceRealization(InterfaceRealization theInterfaceRealization) {
        addNSUses(theInterfaceRealization.getImplementer(), theInterfaceRealization.getImplemented(), theInterfaceRealization);
        return null;
    }

    @objid ("8cf1d046-53e5-4091-ac5f-02ab300f2e28")
    @Override
    public Object visitLinkEnd(LinkEnd theLinkEnd) {
        // Generate NSU if:
        // - non oriented link
        // - or bidirectional link.
        // In the other case generate only navigable roles
        if (theLinkEnd.isNavigable()) {
            NameSpace src = NSUseUtils.getNameSpaceOwner(theLinkEnd.getOwner());
            NameSpace dest = NSUseUtils.getNameSpaceOwner(theLinkEnd.getTarget());
            addNSUses(src, dest, theLinkEnd);
        } else {
            LinkEnd opposite = theLinkEnd.getOpposite();
            if (!opposite.isNavigable()) {
                NameSpace src = NSUseUtils.getNameSpaceOwner(theLinkEnd.getOwner());
                NameSpace dest = NSUseUtils.getNameSpaceOwner(opposite.getOwner());
                addNSUses(src, dest, theLinkEnd);
            }
        }
        return null;
    }

    @objid ("8dfe036a-6637-494b-b43b-1e9a653f9602")
    @Override
    public Object visitManifestation(Manifestation theManifestation) {
        NameSpace src = theManifestation.getOwner();
        ModelElement manifested = theManifestation.getUtilizedElement();
        
        NameSpace dest = NSUseUtils.getNameSpaceOwner(manifested);
        
        addNSUses(src, dest, theManifestation);
        return null;
    }

    @objid ("22ef1664-38bb-4d86-963c-6591dc088797")
    @Override
    public Object visitNamespaceUse(NamespaceUse theNamespaceUse) {
        NameSpace aSource = theNamespaceUse.getUser();
        NameSpace aDest = theNamespaceUse.getUsed();
        
        if (aSource != null && aDest != null && aSource.getCompositionOwner() != aDest.getCompositionOwner()) {
        
            Deque<NameSpace> sourceOwners = new ArrayDeque<>();
            Deque<NameSpace> destOwners = new ArrayDeque<>();
            NSUseUtils.getRelativePathsFromCommonRoot(aSource, aDest, sourceOwners, destOwners);
        
            // Re-add the last common
            sourceOwners.addLast(aSource);
            destOwners.addLast(aDest);
        
            if (sourceOwners.size() > 1 || destOwners.size() > 1) {
                // Get the 2 brothers
                NameSpace i1 = sourceOwners.getFirst();
                NameSpace i2 = destOwners.getFirst();
        
                // Add the uses between the 2 brothers
                if (i1 != i2) {
                    addNSUses(i1, i2, theNamespaceUse);
                }
            }
        }
        return null;
    }

    @objid ("90711653-93da-4087-91bb-b0887539415a")
    @Override
    public Object visitNode(Node obj) {
        return null; // no processing
    }

    @objid ("b6d13f3a-f102-43d3-8455-7ee7d85f5e44")
    @Override
    public Object visitOperation(Operation theOperation) {
        NameSpace src = theOperation.getOwner();
        Operation redefined = theOperation.getRedefines();
        
        if (redefined != null) {
            NameSpace dest = redefined.getOwner();
            addNSUses(src, dest, theOperation);
        }
        return null;
    }

    @objid ("e9acc6c7-6477-4a2f-8685-1e6119bfa50a")
    @Override
    public Object visitPackage(Package obj) {
        return null; // no processing
    }

    @objid ("f1297171-b89f-40ca-a2ba-4354585fd0e0")
    @Override
    public Object visitPackageImport(PackageImport thePackageImport) {
        NameSpace src = thePackageImport.getImportingNameSpace();
        NameSpace dest = thePackageImport.getImportedPackage();
        if (src == null) {
            Operation src2 = thePackageImport.getImportingOperation();
            if (src2 != null)
                src = src2.getOwner();
        }
        
        addNSUses(src, dest, thePackageImport);
        return null;
    }

    @objid ("248f7745-0556-4455-a10f-65516d7169b2")
    @Override
    public Object visitPackageMerge(PackageMerge thePackageMerge) {
        addNSUses(thePackageMerge.getReceivingPackage(), thePackageMerge.getMergedPackage(), thePackageMerge);
        return null;
    }

    @objid ("9304716c-6390-4d46-86f9-061708afecc4")
    @Override
    public Object visitParameter(Parameter theParameter) {
        addNSUses(NSUseUtils.getNameSpaceOwner(theParameter), theParameter.getType(), theParameter);
        return null;
    }

    @objid ("c4d3fa10-bbe4-441d-8fe1-a4e75388000d")
    @Override
    public Object visitPort(Port obj) {
        return visitBindableInstance(obj);
    }

    @objid ("06571ebf-3c1a-4862-8a0e-06b9c73efdea")
    @Override
    public Object visitProvidedInterface(ProvidedInterface theProvidedInterface) {
        NameSpace src = NSUseUtils.getNameSpaceOwner(theProvidedInterface.getProviding());
        for (NameSpace dest : theProvidedInterface.getProvidedElement()) {
            addNSUses(src, dest, theProvidedInterface);
        }
        return null;
    }

    @objid ("6e9cfba3-03b3-4f86-95e5-1d1d454871c9")
    @Override
    public Object visitRaisedException(RaisedException theRaisedException) {
        Operation op = theRaisedException.getThrower();
        if (op != null) {
            addNSUses(op.getOwner(), theRaisedException.getThrownType(), theRaisedException);
        }
        return null;
    }

    @objid ("19f129fd-37b3-40c1-bc1d-18fb59e36220")
    @Override
    public Object visitRequiredInterface(RequiredInterface theRequiredInterface) {
        NameSpace src = NSUseUtils.getNameSpaceOwner(theRequiredInterface.getRequiring());
        
        if (src != null) {
            for (NameSpace dest : theRequiredInterface.getRequiredElement()) {
                addNSUses(src, dest, theRequiredInterface);
            }
        }
        return null;
    }

    @objid ("c80c4673-60c6-4b18-b011-14ee7e4df950")
    @Override
    public Object visitTemplateBinding(TemplateBinding theTemplateBinding) {
        NameSpace src = theTemplateBinding.getBoundElement();
        if (src == null) {
            Operation op = theTemplateBinding.getBoundOperation();
            if (op != null)
                src = op.getOwner();
        }
        
        NameSpace dest = theTemplateBinding.getInstanciatedTemplate();
        if (dest == null) {
            Operation op = theTemplateBinding.getInstanciatedTemplateOperation();
            if (op != null)
                dest = op.getOwner();
        }
        
        addNSUses(src, dest, theTemplateBinding);
        return null;
    }

    @objid ("245d9c16-2786-4a3e-8555-33a2c0af8892")
    @Override
    public Object visitTemplateParameter(TemplateParameter theTemplateParameter) {
        NameSpace src = theTemplateParameter.getParameterized();
        if (src == null) {
            Operation op = theTemplateParameter.getParameterizedOperation();
            if (op != null)
                src = op.getOwner();
        }
        
        if (src != null) {
            NameSpace dest = NSUseUtils.getNameSpaceOwner(theTemplateParameter.getType());
            addNSUses(src, dest, theTemplateParameter);
        
            dest = NSUseUtils.getNameSpaceOwner(theTemplateParameter.getDefaultType());
            addNSUses(src, dest, theTemplateParameter);
        }
        return null;
    }

    @objid ("b062f6c5-a6b3-425b-9047-fef8034556ee")
    @Override
    public Object visitTemplateParameterSubstitution(TemplateParameterSubstitution theTemplateParameterSubstitution) {
        addNSUses(NSUseUtils.getNameSpaceOwner(theTemplateParameterSubstitution.getOwner()),
                NSUseUtils.getNameSpaceOwner(theTemplateParameterSubstitution.getActual()), theTemplateParameterSubstitution);
        return null;
    }

    @objid ("8961d242-689f-4aa9-ad7c-3ee0739e17a6")
    @Override
    public Object visitNaryAssociationEnd(NaryAssociationEnd end) {
        NameSpace src = end.getOwner();
        
        for (NaryAssociationEnd otherRole : end.getNaryAssociation().getNaryEnd()) {
            if (otherRole.isValid() && otherRole != end) {
                NameSpace dest = otherRole.getOwner();
                addNSUses(src, dest, end);
            }
        }
        return null;
    }

    @objid ("380a17a1-eca0-40fa-9b0c-b9adeb5a7f31")
    @Override
    public Object visitNaryConnectorEnd(NaryConnectorEnd theLinkEnd) {
        return visitNaryLinkEnd(theLinkEnd);
    }

    @objid ("f7815418-8b2b-4524-9736-f18e4c8f1449")
    @Override
    public Object visitNaryLinkEnd(NaryLinkEnd theLinkEnd) {
        NameSpace src = NSUseUtils.getNameSpaceOwner(theLinkEnd);
        
        for (NaryLinkEnd otherRole : theLinkEnd.getNaryLink().getNaryLinkEnd()) {
            if (otherRole.isValid() && otherRole != theLinkEnd) {
                NameSpace dest = NSUseUtils.getNameSpaceOwner(otherRole);
                addNSUses(src, dest, theLinkEnd);
            }
        }
        return null;
    }

    @objid ("81e6f862-2dc0-4a30-9222-663ae9c1fbe4")
    @Override
    public Object visitConnector(Connector theConnector) {
        return visitLink(theConnector);
    }

    @objid ("f840db23-86af-493c-bdf6-47506c679db9")
    @Override
    public Object visitAcceptCallEventAction(AcceptCallEventAction obj) {
        return null;
    }

    @objid ("79d1c268-9c21-4b89-b71c-3bdb1e4b494a")
    @Override
    public Object visitAcceptChangeEventAction(AcceptChangeEventAction obj) {
        return null;
    }

    @objid ("8d829d88-6314-4245-9dde-57a815bf39ef")
    @Override
    public Object visitAcceptSignalAction(AcceptSignalAction obj) {
        return null;
    }

    @objid ("58d16f9a-8853-4368-b1c6-61c02541281c")
    @Override
    public Object visitAcceptTimeEventAction(AcceptTimeEventAction obj) {
        return null;
    }

    @objid ("916f972f-4d88-4505-8eb9-3c408b03e3d5")
    @Override
    public Object visitActivity(Activity obj) {
        return visitBehavior(obj);
    }

    @objid ("c5557baf-8040-4b16-ba49-ca56811dee52")
    @Override
    public Object visitActivityAction(ActivityAction obj) {
        return null;
    }

    @objid ("9f5014fe-2081-428f-ab6f-c817e24811f6")
    @Override
    public Object visitActivityEdge(ActivityEdge obj) {
        return null;
    }

    @objid ("1d30365a-0e99-4d9a-b4c4-0f09c100b7e3")
    @Override
    public Object visitActivityFinalNode(ActivityFinalNode obj) {
        return null;
    }

    @objid ("369ec8f3-9da4-4c84-b311-0ea0c1a5f565")
    @Override
    public Object visitActivityGroup(ActivityGroup obj) {
        return null;
    }

    @objid ("b15d13d6-4a32-437d-ad75-f46005c65319")
    @Override
    public Object visitActivityNode(ActivityNode obj) {
        return null;
    }

    @objid ("88704974-33f6-46cb-9957-f74db10ccad0")
    @Override
    public Object visitActivityParameterNode(ActivityParameterNode obj) {
        return null;
    }

    @objid ("69629ab6-d606-44e8-b8b9-dc6903d8fefa")
    @Override
    public Object visitActivityPartition(ActivityPartition obj) {
        return null;
    }

    @objid ("4c88ace4-c0fb-4aac-a452-3e07463e28b8")
    @Override
    public Object visitCallAction(CallAction obj) {
        return null;
    }

    @objid ("f4dad762-6fe2-412a-b930-d221d52e91fe")
    @Override
    public Object visitCallBehaviorAction(CallBehaviorAction obj) {
        return null;
    }

    @objid ("1b06a4a0-4166-4eb0-8715-7ec79529fa24")
    @Override
    public Object visitCallOperationAction(CallOperationAction obj) {
        return null;
    }

    @objid ("9d706661-a89e-473d-bd27-ae66212e0fc8")
    @Override
    public Object visitCentralBufferNode(CentralBufferNode obj) {
        return null;
    }

    @objid ("31a16af4-e706-4269-a5b0-7fefded94182")
    @Override
    public Object visitClause(Clause obj) {
        return null;
    }

    @objid ("3f21a62c-3368-4167-899f-4e885543c657")
    @Override
    public Object visitConditionalNode(ConditionalNode obj) {
        return null;
    }

    @objid ("d5f18d35-9ac3-4564-89cf-66473e56378e")
    @Override
    public Object visitControlFlow(ControlFlow obj) {
        return null;
    }

    @objid ("d628040f-bf37-4341-b888-df91c9223596")
    @Override
    public Object visitControlNode(ControlNode obj) {
        return null;
    }

    @objid ("c191f4b8-667a-4872-8c6e-b83c8e99771b")
    @Override
    public Object visitDataStoreNode(DataStoreNode obj) {
        return null;
    }

    @objid ("4a06d791-8673-4db8-baf2-a13388872125")
    @Override
    public Object visitDecisionMergeNode(DecisionMergeNode obj) {
        return null;
    }

    @objid ("b0d7d852-d304-49c8-8d2f-dd91a7e34c17")
    @Override
    public Object visitExceptionHandler(ExceptionHandler obj) {
        return null;
    }

    @objid ("b3ed3fc5-1fb8-4fe4-8d99-a4a87a30bbb5")
    @Override
    public Object visitExpansionNode(ExpansionNode obj) {
        return null;
    }

    @objid ("ee7b5c84-09a3-4b8d-9e2f-b1839be04ea9")
    @Override
    public Object visitExpansionRegion(ExpansionRegion obj) {
        return null;
    }

    @objid ("30cc27f0-1ef2-44d5-bce5-dfd3f53fdc30")
    @Override
    public Object visitFinalNode(FinalNode obj) {
        return null;
    }

    @objid ("c1352af4-2527-4597-ae4b-ecac92fc3d12")
    @Override
    public Object visitFlowFinalNode(FlowFinalNode obj) {
        return null;
    }

    @objid ("a8281414-a3db-492b-895c-32f870afc4b7")
    @Override
    public Object visitForkJoinNode(ForkJoinNode obj) {
        return null;
    }

    @objid ("1b75b307-e57a-4045-9693-d114b373e891")
    @Override
    public Object visitInitialNode(InitialNode obj) {
        return null;
    }

    @objid ("4bde6e79-6a12-48bc-83d8-bdcaebfbe6af")
    @Override
    public Object visitInputPin(InputPin obj) {
        return null;
    }

    @objid ("fedb70ea-af4c-49ef-ba69-4c9bd024f46a")
    @Override
    public Object visitInstanceNode(InstanceNode obj) {
        return null;
    }

    @objid ("2ed8e519-1a59-415a-90b6-e8d7ef95e2bd")
    @Override
    public Object visitInterruptibleActivityRegion(InterruptibleActivityRegion obj) {
        return null;
    }

    @objid ("cb24adac-f62a-4ee1-90cb-37d4cfb198d4")
    @Override
    public Object visitLoopNode(LoopNode obj) {
        return null;
    }

    @objid ("524c991d-ccbe-4868-9bc4-b79fa39f5bed")
    @Override
    public Object visitMessageFlow(MessageFlow obj) {
        return null;
    }

    @objid ("d9ea1cfc-9996-4a4d-8155-00be6ac6bf69")
    @Override
    public Object visitObjectFlow(ObjectFlow obj) {
        return null;
    }

    @objid ("03ffd1ff-55e1-425d-a7c8-06e9c5dd2328")
    @Override
    public Object visitObjectNode(ObjectNode obj) {
        return null;
    }

    @objid ("2f27f466-39cd-4fc6-aaed-b77dd182b96c")
    @Override
    public Object visitOpaqueAction(OpaqueAction obj) {
        return null;
    }

    @objid ("4903cc5d-b6ce-41d7-b73a-074edd9fc458")
    @Override
    public Object visitOutputPin(OutputPin obj) {
        return null;
    }

    @objid ("845026ae-c755-4ac6-953e-4f30f84fbdba")
    @Override
    public Object visitPin(Pin obj) {
        return null;
    }

    @objid ("7631030c-96d2-450b-96fa-61f51be198d9")
    @Override
    public Object visitSendSignalAction(SendSignalAction obj) {
        return null;
    }

    @objid ("80bc4759-9253-494a-a614-5a4ed8f77cec")
    @Override
    public Object visitStructuredActivityNode(StructuredActivityNode obj) {
        return null;
    }

    @objid ("7eaa33c4-02f7-404d-b359-29cb4ecc7219")
    @Override
    public Object visitValuePin(ValuePin obj) {
        return null;
    }

    @objid ("797d517b-e1ef-4ae3-b91f-c140a1ab1c82")
    @Override
    public Object visitBehaviorParameter(BehaviorParameter obj) {
        return null;
    }

    @objid ("73cb3f69-06ba-419a-bbdd-5f7d0ac9e4c9")
    @Override
    public Object visitOpaqueBehavior(OpaqueBehavior obj) {
        return null;
    }

    @objid ("6697dd71-8c6e-4534-a8b0-d35c38196312")
    @Override
    public Object visitSignal(Signal theSignal) {
        NameSpace src = theSignal;
        NameSpace dest = theSignal.getBase();
        if (dest == null) {
            Operation op = theSignal.getOBase();
            if (op != null) {
                dest = op.getOwner();
            }
        }
        if (dest == null) {
            dest = NSUseUtils.getNameSpaceOwner(theSignal.getPBase());
        }
        
        addNSUses(src, dest, theSignal);
        return null;
    }

    @objid ("66f5234f-657c-4604-8ff3-13e098d44e3c")
    @Override
    public Object visitEvent(Event theEvent) {
        Behavior m = theEvent.getComposed();
        if (m != null) {
            NameSpace src = m.getOwner();
            if (src == null) {
                Operation o = m.getOwnerOperation();
                if (o != null) {
                    src = o.getOwner();
                }
            }
            if ((src != null)) {
                NameSpace dest = null;
        
                // called operation
                Operation called = theEvent.getCalled();
                if (called != null) {
                    dest = called.getOwner();
                    addNSUses(src, dest, theEvent);
                }
        
                // model signal
                dest = theEvent.getModel();
                addNSUses(src, dest, theEvent);
            }
        }
        return null;
    }

    @objid ("7db82df8-1b65-4e23-b6dd-5b4c2b4760c5")
    @Override
    public Object visitCombinedFragment(CombinedFragment obj) {
        return null;
    }

    @objid ("e8f400d7-a5e8-4a01-81ae-0ad2ce444842")
    @Override
    public Object visitDurationConstraint(DurationConstraint obj) {
        return null;
    }

    @objid ("a0cbbc42-fcbd-4493-8fa9-dbb60b734de4")
    @Override
    public Object visitExecutionOccurenceSpecification(ExecutionOccurenceSpecification obj) {
        return null;
    }

    @objid ("21e24882-c05f-4087-82ab-502873cc5123")
    @Override
    public Object visitExecutionSpecification(ExecutionSpecification obj) {
        return null;
    }

    @objid ("989762d5-3f86-405e-b90b-c3fb41e34051")
    @Override
    public Object visitGate(Gate obj) {
        return null;
    }

    @objid ("7025dc72-d7e0-4614-8d62-a57fd998048f")
    @Override
    public Object visitGeneralOrdering(GeneralOrdering obj) {
        return null;
    }

    @objid ("76399f70-b5dd-4437-9e20-f07d98a1f31f")
    @Override
    public Object visitInteractionFragment(InteractionFragment obj) {
        return null;
    }

    @objid ("1f19a9f6-9d05-4512-8ec3-91036a30a48f")
    @Override
    public Object visitInteractionOperand(InteractionOperand obj) {
        return null;
    }

    @objid ("9e35a492-38d8-4cad-a31b-641cdf9af719")
    @Override
    public Object visitInteractionUse(InteractionUse obj) {
        return null;
    }

    @objid ("ca196b27-f954-4da0-863c-d569dd9f9ff7")
    @Override
    public Object visitLifeline(Lifeline obj) {
        return null;
    }

    @objid ("ed5e1d68-8ccc-4229-a335-d598eb4e2cc2")
    @Override
    public Object visitMessage(Message theMessage) {
        NameSpace src = NSUseUtils.getNameSpaceOwner(theMessage);
        NameSpace dest;
        Operation op = theMessage.getInvoked();
        if ((src != null) && op != null) {
            dest = op.getOwner();
            addNSUses(src, dest, theMessage);
        }
        return null;
    }

    @objid ("5297066d-350f-4145-b003-1175b63cc0e7")
    @Override
    public Object visitMessageEnd(MessageEnd obj) {
        return null;
    }

    @objid ("269f9aaf-d5e8-4628-8749-f85c26f3b577")
    @Override
    public Object visitOccurrenceSpecification(OccurrenceSpecification obj) {
        return null;
    }

    @objid ("f13918b5-5665-456f-837b-1eb70f927be3")
    @Override
    public Object visitPartDecomposition(PartDecomposition obj) {
        return null;
    }

    @objid ("b91dbe69-fd5a-4649-be84-90cb76b66f32")
    @Override
    public Object visitStateInvariant(StateInvariant obj) {
        return null;
    }

    @objid ("fbab127e-93f5-4c55-b49a-bbbff6fd9b2a")
    @Override
    public Object visitTerminateSpecification(TerminateSpecification obj) {
        return null;
    }

    @objid ("70f5165d-0df4-4c06-bb25-1582e67fa660")
    @Override
    public Object visitAbstractPseudoState(AbstractPseudoState obj) {
        return null;
    }

    @objid ("a278a9d8-587f-4151-b965-5bbe6723b430")
    @Override
    public Object visitChoicePseudoState(ChoicePseudoState obj) {
        return null;
    }

    @objid ("ff08f9a1-b770-48e2-abf7-5fa99c2145a6")
    @Override
    public Object visitConnectionPointReference(ConnectionPointReference obj) {
        return null;
    }

    @objid ("7d5e631d-9d68-470a-a144-61caa375e1fd")
    @Override
    public Object visitDeepHistoryPseudoState(DeepHistoryPseudoState obj) {
        return null;
    }

    @objid ("e4669c2d-3fb5-48f6-b15c-7cddd52b2b77")
    @Override
    public Object visitEntryPointPseudoState(EntryPointPseudoState obj) {
        return null;
    }

    @objid ("94b9fe3d-45fd-4c32-ae31-1a06c377c0d1")
    @Override
    public Object visitExitPointPseudoState(ExitPointPseudoState obj) {
        return null;
    }

    @objid ("ed7a227e-a309-4e71-9fa0-ab514f904a86")
    @Override
    public Object visitForkPseudoState(ForkPseudoState obj) {
        return null;
    }

    @objid ("e3c36961-090e-4247-915a-76cfa75651b9")
    @Override
    public Object visitInitialPseudoState(InitialPseudoState obj) {
        return null;
    }

    @objid ("8f3f2dcd-eb9c-44e3-98dd-4f0833065096")
    @Override
    public Object visitInternalTransition(InternalTransition obj) {
        return visitTransition(obj);
    }

    @objid ("b2ed9481-9412-4e0e-b26c-d224d1daac5b")
    @Override
    public Object visitJoinPseudoState(JoinPseudoState obj) {
        return null;
    }

    @objid ("19e799cf-6653-4e68-8154-e7e74398582c")
    @Override
    public Object visitJunctionPseudoState(JunctionPseudoState obj) {
        return null;
    }

    @objid ("5efb344e-35ba-4ccb-a4a7-7e15fc01ac50")
    @Override
    public Object visitShallowHistoryPseudoState(ShallowHistoryPseudoState obj) {
        return null;
    }

    @objid ("d47a3475-d9e3-4c6e-8f0f-be78da008d2d")
    @Override
    public Object visitTerminatePseudoState(TerminatePseudoState obj) {
        return null;
    }

    @objid ("1584b534-42e0-4d12-b426-821c95db14c5")
    @Override
    public Object visitTransition(Transition theTransition) {
        NameSpace src = NSUseUtils.getNameSpaceOwner(theTransition);
        if ((src != null)) {
            NameSpace dest = theTransition.getEffects();
            addNSUses(src, dest, theTransition);
        
            dest = NSUseUtils.getNameSpaceOwner(theTransition.getTrigger());
            addNSUses(src, dest, theTransition);
        }
        return null;
    }

    @objid ("b322122e-d5d0-4a23-a43e-97aa6392a118")
    @Override
    public Object visitFinalState(FinalState obj) {
        return null;
    }

    @objid ("05c24bd7-d979-4f31-bd73-aa4819430cfb")
    @Override
    public Object visitRegion(Region obj) {
        return null;
    }

    @objid ("bb2e0c17-9858-4a4e-8c50-4d7a1e871f6d")
    @Override
    public Object visitActor(Actor obj) {
        return visitGeneralClass(obj);
    }

    @objid ("0e0f82dc-5dbd-426f-aa9b-bb2431648f57")
    @Override
    public Object visitUseCaseDependency(UseCaseDependency theUseCaseDependency) {
        NameSpace src = theUseCaseDependency.getOrigin();
        NameSpace dest = theUseCaseDependency.getTarget();
        
        addNSUses(src, dest, theUseCaseDependency);
        return null;
    }

    @objid ("c0457880-9f17-4fe0-b563-26905e4bbde1")
    @Override
    public Object visitInformationFlow(InformationFlow obj) {
        return null;
    }

    @objid ("38a9be88-1895-4708-911b-0b328761f368")
    @Override
    public Object visitInformationItem(InformationItem obj) {
        return null;
    }

    @objid ("9abdcbb5-205c-469e-88cd-e9cfcd079a54")
    @Override
    public Object visitDataFlow(DataFlow theDataFlow) {
        NameSpace src1 = theDataFlow.getOrigin();
        NameSpace dest1 = theDataFlow.getDestination();
        NameSpace dest2 = theDataFlow.getSModel();
        
        if (src1 != null && src1.isValid()) {
            if (dest1 != null && dest1.isValid()) {
                addNSUses(src1, dest1, theDataFlow);
            }
            if (dest2 != null && dest2.isValid()) {
                addNSUses(src1, dest2, theDataFlow);
            }
        }
        return null;
    }

    @objid ("003da631-1bbc-4004-b404-1f17b49dd91e")
    @Override
    public Object visitCommunicationInteraction(CommunicationInteraction obj) {
        return null;
    }

    @objid ("5079434e-a92b-47b2-b6d4-1f25bacb9cc8")
    @Override
    public Object visitCommunicationNode(CommunicationNode obj) {
        return null;
    }

    @objid ("87149165-cb41-4b31-9b54-48be5260f45b")
    @Override
    public Object visitCommunicationMessage(CommunicationMessage obj) {
        return null;
    }

    @objid ("2c50f315-baa7-43e6-b986-9f419180a9a6")
    @Override
    public Object visitCommunicationChannel(CommunicationChannel obj) {
        return null;
    }

    @objid ("ac201a78-3509-475c-910b-4723013b823b")
    private void addNSUses(NameSpace aSource, NameSpace aDest, Element aCause) {
        this.handler.addNSUses(aSource, aDest, aCause);
    }

    /**
     * Analyses the given model object.
     * @param e the model object to analyze.
     */
    @objid ("63efe89b-9e69-4695-9fc4-336358ae3864")
    public void buildFor(Element e) {
        // Generates nothing for library elements.
        if (e.getStatus().isRamc())
            return;
        
        e.accept(this);
    }

}
