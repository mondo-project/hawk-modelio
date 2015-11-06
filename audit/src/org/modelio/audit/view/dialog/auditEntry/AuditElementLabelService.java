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
                                    

package org.modelio.audit.view.dialog.auditEntry;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.audit.plugin.Audit;
import org.modelio.metamodel.uml.behavior.activityModel.AcceptCallEventAction;
import org.modelio.metamodel.uml.behavior.activityModel.AcceptSignalAction;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityEdge;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityNode;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityParameterNode;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityPartition;
import org.modelio.metamodel.uml.behavior.activityModel.CallBehaviorAction;
import org.modelio.metamodel.uml.behavior.activityModel.CallOperationAction;
import org.modelio.metamodel.uml.behavior.activityModel.Clause;
import org.modelio.metamodel.uml.behavior.activityModel.InstanceNode;
import org.modelio.metamodel.uml.behavior.activityModel.ObjectNode;
import org.modelio.metamodel.uml.behavior.activityModel.SendSignalAction;
import org.modelio.metamodel.uml.behavior.commonBehaviors.Behavior;
import org.modelio.metamodel.uml.behavior.commonBehaviors.BehaviorParameter;
import org.modelio.metamodel.uml.behavior.commonBehaviors.Event;
import org.modelio.metamodel.uml.behavior.commonBehaviors.Signal;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationMessage;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationNode;
import org.modelio.metamodel.uml.behavior.interactionModel.MessageSort;
import org.modelio.metamodel.uml.behavior.stateMachineModel.InternalTransition;
import org.modelio.metamodel.uml.behavior.stateMachineModel.StateVertex;
import org.modelio.metamodel.uml.behavior.stateMachineModel.Transition;
import org.modelio.metamodel.uml.behavior.usecaseModel.ExtensionPoint;
import org.modelio.metamodel.uml.behavior.usecaseModel.UseCase;
import org.modelio.metamodel.uml.behavior.usecaseModel.UseCaseDependency;
import org.modelio.metamodel.uml.informationFlow.DataFlow;
import org.modelio.metamodel.uml.informationFlow.InformationFlow;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.ModelTree;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.infrastructure.Usage;
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
import org.modelio.metamodel.uml.statik.ElementImport;
import org.modelio.metamodel.uml.statik.Feature;
import org.modelio.metamodel.uml.statik.GeneralClass;
import org.modelio.metamodel.uml.statik.Generalization;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.metamodel.uml.statik.Interface;
import org.modelio.metamodel.uml.statik.InterfaceRealization;
import org.modelio.metamodel.uml.statik.LinkEnd;
import org.modelio.metamodel.uml.statik.Manifestation;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.Operation;
import org.modelio.metamodel.uml.statik.Package;
import org.modelio.metamodel.uml.statik.PackageImport;
import org.modelio.metamodel.uml.statik.PackageMerge;
import org.modelio.metamodel.uml.statik.Parameter;
import org.modelio.metamodel.uml.statik.PassingMode;
import org.modelio.metamodel.uml.statik.Port;
import org.modelio.metamodel.uml.statik.ProvidedInterface;
import org.modelio.metamodel.uml.statik.RaisedException;
import org.modelio.metamodel.uml.statik.RequiredInterface;
import org.modelio.metamodel.uml.statik.TemplateBinding;
import org.modelio.metamodel.uml.statik.TemplateParameter;
import org.modelio.metamodel.uml.statik.TemplateParameterSubstitution;
import org.modelio.metamodel.uml.statik.VisibilityMode;
import org.modelio.metamodel.visitors.DefaultModelVisitor;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("5bc280e2-232b-4294-be6c-cf06c71e17ff")
public class AuditElementLabelService extends DefaultModelVisitor {
    @objid ("97424096-2730-435a-99bf-23da2f1bcbfb")
    private String elementLabel;

    @objid ("bf9e60b5-f569-4508-9846-f5092ad1b245")
    public AuditElementLabelService() {
        super();
        this.elementLabel = "";
    }

    @objid ("5704d1fb-1715-4aa1-a33b-ef671de510f2")
    public String getLabel(MObject element) {
        // Guard agains't null elements
        if (element == null) {
            return "<null>";
        }
        
        // reset the returned value
        this.elementLabel = "";
        
        // call the visitor
        element.accept(this);
        
        // return the value
        return this.elementLabel;
    }

    @objid ("106223e0-6ee2-432e-89a7-b33c21ca7361")
    @Override
    public Object visitAcceptCallEventAction(AcceptCallEventAction theAcceptCallEventAction) {
        StringBuffer symbol = new StringBuffer();
        
        String acceptCallEventActionName = theAcceptCallEventAction.getName();
        Operation operation = theAcceptCallEventAction.getCalled();
        
        if (operation != null && (acceptCallEventActionName.equals("Unnamed") || acceptCallEventActionName.equals(""))) {
            symbol.append(operation.getName());
        } else {
            symbol.append(acceptCallEventActionName);
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("7c14ba07-da58-49c7-bfab-0b508d7c29f5")
    @Override
    public Object visitAcceptSignalAction(AcceptSignalAction theAcceptSignalAction) {
        StringBuffer symbol = new StringBuffer();
        
        String acceptSignalActionName = theAcceptSignalAction.getName();
        List<Signal> signals = theAcceptSignalAction.getAccepted();
        
        if (signals.size() > 0 && (acceptSignalActionName.equals("Unnamed") || acceptSignalActionName.equals(""))) {
            for (int i = 0; i < signals.size(); i++) {
                if (i > 0) {
                    symbol.append(", ");
                }
                symbol.append(signals.get(i).getName());
            }
        } else {
            symbol.append(acceptSignalActionName);
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("b7141d16-66dc-45c9-a1c2-9590d9b952be")
    @Override
    public Object visitActivityEdge(ActivityEdge theActivityEdge) {
        StringBuffer symbol = new StringBuffer();
        
        ActivityNode target = theActivityEdge.getTarget();
        
        if (target != null) {
            AuditElementLabelService labelService = new AuditElementLabelService();
            symbol.append(labelService.getLabel(target));
        } else {
            String name = theActivityEdge.getName();
            symbol.append(name);
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("57fa8224-2afd-4ca8-a2fd-d18f96817711")
    @Override
    public Object visitActivityParameterNode(ActivityParameterNode theActivityParameterNode) {
        StringBuffer symbol = new StringBuffer();
        
        
        GeneralClass type = theActivityParameterNode.getType();
        
        BehaviorParameter behaviorParameter = theActivityParameterNode.getRepresentedRealParameter();
        
        Parameter theParameter = null;
        
        if (behaviorParameter != null) {
            theParameter = behaviorParameter.getMapped();
        }
        
        if (theParameter != null && theParameter.getComposed() != null) {
            PassingMode passingMode = theParameter.getParameterPassing();
        
            symbol.append(theActivityParameterNode.getName());
            symbol.append(" ");
        
            if (passingMode == PassingMode.IN) {
                symbol.append("In");
            }
            if (passingMode == PassingMode.OUT) {
                symbol.append("Out");
            }
            if (passingMode == PassingMode.INOUT) {
                symbol.append("Inout");
            }
        } else if (theParameter != null && theParameter.getReturned() != null) {
            symbol.append("Out");
        }
        
        symbol.append(": ");
        if (type != null) {
            symbol.append(type.getName());
        } else {
            symbol.append(Audit.I18N.getString("AuditEntryDialog.NoType"));
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("82e5bce5-8a9e-4f96-a324-d9bfc9c32c5c")
    @Override
    public Object visitActivityPartition(ActivityPartition theActivityPartition) {
        StringBuffer symbol = new StringBuffer();
        
        symbol.append(theActivityPartition.getName());
        
        ModelElement represented = theActivityPartition.getRepresented();
        if (represented != null) {
            symbol.append(":");
        
            if (represented instanceof ActivityPartition) {
                symbol.append(represented.getName());
            } else {
                AuditElementLabelService labelService = new AuditElementLabelService();
                symbol.append(labelService.getLabel(represented));
            }
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("bbe4ea9e-700c-4860-9ba0-536546b3d35c")
    @Override
    public Object visitAssociationEnd(AssociationEnd theAssociationEnd) {
        StringBuffer symbol = new StringBuffer();
        
        VisibilityMode visibility = theAssociationEnd.getVisibility();
        if (visibility == VisibilityMode.PUBLIC) {
            symbol.append("+");
        } else if (visibility == VisibilityMode.PROTECTED) {
            symbol.append("#");
        } else if (visibility == VisibilityMode.PRIVATE) {
            symbol.append("-");
        } else if (visibility == VisibilityMode.PACKAGEVISIBILITY) {
            symbol.append("~");
        }
        
        String associationEndName = theAssociationEnd.getName();
        if (associationEndName.equals("")) {
            symbol.append(Audit.I18N.getString("AuditEntryDialog.NoName"));
        } else {
            symbol.append(theAssociationEnd.getName());
        }
        
        symbol.append(": ");
        
        
        
        Classifier type = theAssociationEnd.getTarget();        
        if(type == null){
            type = theAssociationEnd.getOpposite().getSource();
        }
        
        if (type == null) {
            symbol.append(Audit.I18N.getString("AuditEntryDialog.NoType"));
        } else {
            symbol.append(type.getName());
        }
        
        // The cardinality
        symbol.append(getAssociationEndMultiplicity(theAssociationEnd));
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("005e6dd5-3bb3-4907-aa86-6023d2e9f10f")
    @Override
    public Object visitAttribute(Attribute theAttribute) {
        StringBuffer symbol = new StringBuffer();
        
        VisibilityMode visibility = theAttribute.getVisibility();
        if (visibility == VisibilityMode.PUBLIC) {
            symbol.append("+");
        } else if (visibility == VisibilityMode.PROTECTED) {
            symbol.append("#");
        } else if (visibility == VisibilityMode.PRIVATE) {
            symbol.append("-");
        } else if (visibility == VisibilityMode.PACKAGEVISIBILITY) {
            symbol.append("~");
        }
        
        symbol.append(theAttribute.getName());
        
        
        GeneralClass type = theAttribute.getType();
        
        symbol.append(" : ");
        if (type != null) {
            symbol.append(type.getName());
        } else {
            symbol.append(Audit.I18N.getString("AuditEntryDialog.NoType"));
        }
        
        symbol.append(getAttributeMultiplicity(theAttribute));
        
        String value = theAttribute.getValue();
        if (value != null && !value.equals("")) {
            symbol.append(" = ");
            symbol.append(value);
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("29fd40e4-d65d-4b72-8bde-67a85c463978")
    @Override
    public Object visitAttributeLink(AttributeLink theAttributeLink) {
        StringBuffer symbol = new StringBuffer();
        
        Attribute base = theAttributeLink.getBase();
        
        if (base != null) {
            symbol.append(base.getName());
        } else {
            symbol.append(theAttributeLink.getName());
        }
        
        String value = theAttributeLink.getValue();
        
        if (!value.equals("")) {
            symbol.append(" = ");
            symbol.append(value);
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("dd90eaaf-d0de-49e6-b7f1-0e819e977f94")
    @Override
    public Object visitBehaviorParameter(BehaviorParameter theBehaviorParameter) {
        Parameter theParameter = theBehaviorParameter.getMapped();
        
        StringBuffer symbol = new StringBuffer();
        
        PassingMode passingMode = theBehaviorParameter.getParameterPassing();
        
        GeneralClass type = theBehaviorParameter.getType();
        
        if (theParameter != null && theParameter.getComposed() != null) {
            symbol.append(theBehaviorParameter.getName());
            symbol.append(" ");
        
            if (passingMode == PassingMode.IN) {
                symbol.append("In");
            }
            if (passingMode == PassingMode.OUT) {
                symbol.append("Out");
            }
            if (passingMode == PassingMode.INOUT) {
                symbol.append("Inout");
            }
        } else if (theParameter != null && theParameter.getReturned() != null) {
            symbol.append("Out");
        }
        
        symbol.append(": ");
        if (type != null) {
            symbol.append(type.getName());
        } else {
            symbol.append(Audit.I18N.getString("AuditEntryDialog.NoType"));
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("e144cf34-b0ac-45f4-ac1e-0b518f9a6cf2")
    @Override
    public Object visitBinding(Binding theBinding) {
        StringBuffer symbol = new StringBuffer();
        
        ModelElement role = theBinding.getRole();
        ModelElement feature = theBinding.getRepresentedFeature();
        
        AuditElementLabelService labelService = new AuditElementLabelService();
        
        if (role != null) {
            symbol.append(labelService.getLabel(role));
        }
        symbol.append(" --> ");
        if (feature != null) {
            symbol.append(labelService.getLabel(feature));
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("cc70b158-e953-4d4e-9663-b69163fd9c11")
    @Override
    public Object visitCallBehaviorAction(CallBehaviorAction theCallBehaviorAction) {
        StringBuffer symbol = new StringBuffer();
        
        String callBehaviorActionName = theCallBehaviorAction.getName();
        Behavior behavior = theCallBehaviorAction.getCalled();
        
        if (behavior != null && (callBehaviorActionName.equals("Unnamed") || callBehaviorActionName.equals(""))) {
            symbol.append(behavior.getName());
        } else {
            symbol.append(callBehaviorActionName);
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("571a1bad-fcfc-41f7-b0d2-1d8f779aae75")
    @Override
    public Object visitCallOperationAction(CallOperationAction theCallOperationAction) {
        StringBuffer symbol = new StringBuffer();
        
        String callOperationActionName = theCallOperationAction.getName();
        Operation operation = theCallOperationAction.getCalled();
        
        if (operation != null && (callOperationActionName.equals("Unnamed") || callOperationActionName.equals(""))) {
            symbol.append(operation.getName());
        } else {
            symbol.append(callOperationActionName);
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("d2361c86-d606-479d-b09a-42ac22a613c5")
    @Override
    public Object visitClassAssociation(ClassAssociation theClassAssociation) {
        StringBuffer symbol = new StringBuffer();
        
        Class theClass = theClassAssociation.getClassPart();
        
        if (theClass != null) {
            symbol.append(theClass.getName());
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("0a874094-03a4-4149-8f15-e6e015c5848f")
    @Override
    public Object visitClause(Clause theClause) {
        StringBuffer symbol = new StringBuffer();
        
        symbol.append("[");
        
        String test = theClause.getTest();
        if (test.length() == 0) {
            symbol.append("Conditional clause");
        } else {
            symbol.append(test);
        }
        
        symbol.append("]");
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("e5157c06-0464-4dc5-805b-8b22aedfe755")
    @Override
    public Object visitCollaborationUse(CollaborationUse theCollaborationUse) {
        StringBuffer symbol = new StringBuffer();
        
        symbol.append(theCollaborationUse.getName());
        
        Collaboration base = theCollaborationUse.getType();
        symbol.append(": ");
        if (base != null) {
            symbol.append(base.getName());
        } else {
            symbol.append(Audit.I18N.getString("AuditEntryDialog.NoBase"));
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("c18b44c6-b991-4048-b23d-36790f27b5c5")
    @Override
    public Object visitCommunicationMessage(CommunicationMessage theCommunicationMessage) {
        StringBuffer symbol = new StringBuffer();
        
        String name = theCommunicationMessage.getName();
        MessageSort messageSort = theCommunicationMessage.getSortOfMessage();
        
        if (name.equals("")) {
            symbol.append(Audit.I18N.getString(messageSort.name()));
        } else {
            symbol.append(name);
        }
        
        if (messageSort != MessageSort.SYNCCALL && messageSort != MessageSort.CREATEMESSAGE) {
            symbol.append("(");
            symbol.append(theCommunicationMessage.getArgument());
            symbol.append(")");
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("6dca8056-bae4-4c66-8bc6-4c0d32f57c16")
    @Override
    public Object visitCommunicationNode(CommunicationNode theCommunicationNode) {
        StringBuffer symbol = new StringBuffer();
        
        Instance instance = theCommunicationNode.getRepresented();
        
        if (instance != null) {
            AuditElementLabelService labelService = new AuditElementLabelService();
            symbol.append(labelService.getLabel(instance));
        } else {
            symbol.append(theCommunicationNode.getName());
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("84ef8bfb-04fc-473f-8bd7-6aeb3c1fa302")
    @Override
    public Object visitDataFlow(DataFlow theDataFlow) {
        StringBuffer symbol = new StringBuffer();
        
        Signal signal = theDataFlow.getSModel();
        
        if (signal != null) {
            symbol.append(signal.getName());
        } else {
            symbol.append(theDataFlow.getName());
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("5631e0f3-da1b-41ed-a6a4-8a9c5006d357")
    @Override
    public Object visitDependency(Dependency theDependency) {
        StringBuffer symbol = new StringBuffer();
        
        ModelElement destination = theDependency.getDependsOn();
        
        if (destination != null) {
            symbol.append("Dependency ");
            symbol.append(destination.getName());
        
            ModelTree owner = null;
        
            if (destination instanceof ModelTree) {
                owner = ((ModelTree) destination).getOwner();
            }
        
            if (owner != null) {
                symbol.append(" (from ");
                symbol.append(owner.getName());
                symbol.append(")");
            }
        } else {
            symbol.append("<No destination>");
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("94b95313-b15a-42cd-8a3a-c9bc26dba7cd")
    @Override
    public Object visitElement(Element theElement) {
        this.elementLabel = theElement.getClass().getSimpleName();
        return null;
    }

    @objid ("bf1358a2-53b5-4f4e-b3b4-e786b29b892d")
    @Override
    public Object visitElementImport(ElementImport theElementImport) {
        StringBuffer symbol = new StringBuffer();
        
        NameSpace importedNamespace = theElementImport.getImportedElement();
        
        if (importedNamespace != null) {
            symbol.append("access ");
            symbol.append(importedNamespace.getName());
        
            ModelTree owner = importedNamespace.getOwner();
        
            if (owner != null) {
                symbol.append(" (from ");
                symbol.append(owner.getName());
                symbol.append(")");
            }
        } else {
            symbol.append("<No destination>");
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("80402e56-1b22-4b3a-b607-9ae8cce223f1")
    @Override
    public Object visitEvent(Event theEvent) {
        StringBuffer symbol = new StringBuffer();
        
        Operation operation = theEvent.getCalled();
        Signal signal = theEvent.getModel();
        
        if (operation != null) {
            symbol.append(operation.getName());
        } else if (signal != null) {
            symbol.append(signal.getName());
        } else if (theEvent.getName().equals("")) {
            symbol.append(theEvent.getExpression());
        } else {
            symbol.append(theEvent.getName());
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("7bf6f32c-8f59-444e-9352-abda9f74a74f")
    @Override
    public Object visitExtensionPoint(ExtensionPoint theExtensionPoint) {
        StringBuffer symbol = new StringBuffer();
        
        VisibilityMode visibility = theExtensionPoint.getVisibility();
        if (visibility == VisibilityMode.PUBLIC) {
            symbol.append("+");
        } else if (visibility == VisibilityMode.PROTECTED) {
            symbol.append("#");
        } else if (visibility == VisibilityMode.PRIVATE) {
            symbol.append("-");
        } else if (visibility == VisibilityMode.PACKAGEVISIBILITY) {
            symbol.append("~");
        }
        
        symbol.append(theExtensionPoint.getName());
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("c8511f04-6973-48a9-8caf-edc2c55e19e9")
    @Override
    public Object visitFeature(Feature theFeature) {
        StringBuffer symbol = new StringBuffer();
        
        VisibilityMode visibility = theFeature.getVisibility();
        if (visibility == VisibilityMode.PUBLIC) {
            symbol.append("+");
        } else if (visibility == VisibilityMode.PROTECTED) {
            symbol.append("#");
        } else if (visibility == VisibilityMode.PRIVATE) {
            symbol.append("-");
        } else if (visibility == VisibilityMode.PACKAGEVISIBILITY) {
            symbol.append("~");
        }
        
        symbol.append(theFeature.getName());
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("8ed53c58-4ec3-4606-aa57-7fae035dd09c")
    @Override
    public Object visitGeneralization(Generalization theGeneralization) {
        StringBuffer symbol = new StringBuffer();
        
        symbol.append("is_a ");
        
        AuditElementLabelService labelService = new AuditElementLabelService();
        NameSpace parent = theGeneralization.getSuperType();
        
        if (parent != null) {
            symbol.append(labelService.getLabel(parent));
        
            ModelTree owner = parent.getOwner();
        
            if (owner != null) {
                symbol.append(" (from ");
                symbol.append(owner.getName());
                symbol.append(")");
            }
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("895bcf8f-e435-483d-8f03-14edf90b92f0")
    @Override
    public Object visitInformationFlow(InformationFlow theInformationFlow) {
        StringBuffer symbol = new StringBuffer();
        
        List<Classifier> classifiers = theInformationFlow.getConveyed();
        
        if (classifiers.size() > 0) {
            for (int i = 0; i < classifiers.size(); i++) {
                if (i > 0) {
                    symbol.append(", ");
                }
                symbol.append(classifiers.get(i).getName());
            }
        } else {
            symbol.append(theInformationFlow.getName());
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("8c8335ed-8d82-41d3-9add-afe8fc0f82aa")
    @Override
    public Object visitInstance(Instance theInstance) {
        StringBuffer symbol = new StringBuffer();
        
        symbol.append(theInstance.getName());
        
        NameSpace base = theInstance.getBase();
        symbol.append(": ");
        if (base != null) {
            symbol.append(base.getName());
        } else {
            symbol.append(Audit.I18N.getString("AuditEntryDialog.NoBase"));
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("0f8c44e3-e420-42e5-9355-545c5b9c9264")
    @Override
    public Object visitInstanceNode(InstanceNode theInstanceNode) {
        StringBuffer symbol = new StringBuffer();
        
        Instance instance = theInstanceNode.getRepresented();
        Attribute attribut = theInstanceNode.getRepresentedAttribute();
        AssociationEnd assocEnd = theInstanceNode.getRepresentedRole();
        BehaviorParameter behaviorParameter = theInstanceNode.getRepresentedRealParameter();
        
        GeneralClass type = theInstanceNode.getType();
        
        
        if (type != null) {
            symbol.append(theInstanceNode.getName());
        
            symbol.append(": ");
            symbol.append(type.getName());
        
        } else if (instance != null) {
            AuditElementLabelService labelService = new AuditElementLabelService();
            symbol.append(labelService.getLabel(instance));
        } else if (attribut != null) {
            AuditElementLabelService labelService = new AuditElementLabelService();
            symbol.append(labelService.getLabel(attribut));
        } else if (assocEnd != null) {
            AuditElementLabelService labelService = new AuditElementLabelService();
            symbol.append(labelService.getLabel(assocEnd));
        } else if (behaviorParameter != null) {
            AuditElementLabelService labelService = new AuditElementLabelService();
            symbol.append(labelService.getLabel(behaviorParameter));
        } else {
            symbol.append(theInstanceNode.getName());
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("411baa6d-4fac-4362-bb93-474889352f30")
    @Override
    public Object visitInterfaceRealization(InterfaceRealization theInterfaceRealization) {
        StringBuffer symbol = new StringBuffer();
        
        // symbol.append("Realize ");
        
        AuditElementLabelService labelService = new AuditElementLabelService();
        Interface implemented = theInterfaceRealization.getImplemented();
        
        if (implemented != null) {
            symbol.append(labelService.getLabel(implemented));
        
            ModelTree owner = implemented.getOwner();
        
            if (owner != null) {
                symbol.append(" (from ");
                symbol.append(owner.getName());
                symbol.append(")");
            }
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("39d6152a-3462-4161-8826-7e4815345069")
    @Override
    public Object visitInternalTransition(InternalTransition theInternalTransition) {
        super.visitInternalTransition(theInternalTransition);
        if (this.elementLabel.equals("")) {
            this.elementLabel = "/";
        }
        return null;
    }

    @objid ("3e39a6d9-a4fd-4357-a6a4-feff4f9d134b")
    @Override
    public Object visitLinkEnd(LinkEnd theLinkEnd) {
        StringBuffer symbol = new StringBuffer();
        
        LinkEnd relatedLinkEnds = theLinkEnd.getOpposite();   
        String linkEndName = theLinkEnd.getName();
        
        
          
             
        
        if (relatedLinkEnds == null) {
            if (linkEndName.equals("")) {
                symbol.append(Audit.I18N.getString("AuditEntryDialog.NoName"));
            } else {
                symbol.append(linkEndName);
            }
        } else {
            if (linkEndName.equals("")) {
                symbol.append(Audit.I18N.getString("AuditEntryDialog.NoName"));
            } else {
                symbol.append(linkEndName);
            }
            Instance linked = relatedLinkEnds.getSource();
            if(linked == null){
                linked = theLinkEnd.getTarget();
            }
            if (linked != null) {
                symbol.append("::");
                symbol.append(linked.getName());
            }  
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("594e07b3-55dc-489b-8554-0dcba89a63b4")
    @Override
    public Object visitManifestation(Manifestation theManifestation) {
        StringBuffer symbol = new StringBuffer();
        
        ModelElement destination = theManifestation.getUtilizedElement();
        
        if (destination != null) {
            symbol.append("manifested ");
            symbol.append(destination.getName());
        
            ModelTree owner = null;
        
            if (destination instanceof ModelTree) {
                owner = ((ModelTree) destination).getOwner();
            }
        
            if (owner != null) {
                symbol.append(" (from ");
                symbol.append(owner.getName());
                symbol.append(")");
            }
        } else {
            symbol.append("<No destination>");
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("de41e026-19b6-4103-8cf2-f13eaaa58e93")
    @Override
    public Object visitModelElement(ModelElement theModelElement) {
        this.elementLabel = theModelElement.getName();
        return null;
    }

    @objid ("9c65ca94-9139-4cc4-a1d1-8175b03f248c")
    @Override
    public Object visitObjectNode(ObjectNode theObjectNode) {
        StringBuffer symbol = new StringBuffer();
        
        BehaviorParameter parameter = theObjectNode.getRepresentedRealParameter();
        Instance instance = theObjectNode.getRepresented();
        Attribute attribute = theObjectNode.getRepresentedAttribute();
        AssociationEnd role = theObjectNode.getRepresentedRole();
        
        AuditElementLabelService labelService = new AuditElementLabelService();
        
        if (parameter != null) {
            symbol.append(labelService.getLabel(parameter));
        } else if (instance != null) {
            symbol.append(labelService.getLabel(instance));
        } else if (attribute != null) {
            symbol.append(labelService.getLabel(attribute));
        } else if (role != null) {
            symbol.append(labelService.getLabel(role));
        } else {
            GeneralClass type = theObjectNode.getType();
        
            symbol.append(theObjectNode.getName());
        
            //ObObjectNodeOrderingKindEnum ordering = theObjectNode.getOrdering();
        
            String upperbound = theObjectNode.getUpperBound();
            if (upperbound.length() > 0 && !upperbound.equals("1")) {
                symbol.append("[");
                symbol.append(upperbound);
                symbol.append("]");
            }
        
            if (type != null) {
                symbol.append(" : ");
                symbol.append(type.getName());
            }
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("45776334-928d-41be-bcf2-8601611342df")
    @Override
    public Object visitOperation(Operation theOperation) {
        StringBuffer symbol = new StringBuffer();
        
        VisibilityMode visibility = theOperation.getVisibility();
        if (visibility == VisibilityMode.PUBLIC) {
            symbol.append("+");
        } else if (visibility == VisibilityMode.PROTECTED) {
            symbol.append("#");
        } else if (visibility == VisibilityMode.PRIVATE) {
            symbol.append("-");
        } else if (visibility == VisibilityMode.PACKAGEVISIBILITY) {
            symbol.append("~");
        }
        
        symbol.append(theOperation.getName());
        
        symbol.append("(");
        List<Parameter> parameters = theOperation.getIO();
        int parameterNumber = parameters.size();
        
        for (int i = 0; i < parameterNumber; i++) {
            symbol.append(getParameterSymbol(parameters.get(i)));
            if (i < parameterNumber - 1) {
                symbol.append(", ");
            }
        }
        
        symbol.append(")");
        
        Parameter returnParameter = theOperation.getReturn();
        if (returnParameter != null) {
            GeneralClass type = returnParameter.getType();
            symbol.append(":");
            symbol.append(" ");
            if (type != null) {
                symbol.append(type.getName());
            } else {
                symbol.append(Audit.I18N.getString("AuditEntryDialog.NoType"));
            }
            symbol.append(getParameterMultiplicity(returnParameter));
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("25eda3bf-378b-4eb6-b605-17d7ecfed83a")
    @Override
    public Object visitPackageImport(PackageImport thePackageImport) {
        StringBuffer symbol = new StringBuffer();
        
        NameSpace importedNamespace = thePackageImport.getImportedPackage();
        
        if (importedNamespace != null) {
            symbol.append("access all ");
            symbol.append(importedNamespace.getName());
        
            ModelTree owner = importedNamespace.getOwner();
        
            if (owner != null) {
                symbol.append(" (from ");
                symbol.append(owner.getName());
                symbol.append(")");
            }
        } else {
            symbol.append("<No destination>");
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("d821f4e5-667e-481d-b07f-1042c87adc93")
    @Override
    public Object visitPackageMerge(PackageMerge thePackageMerge) {
        StringBuffer symbol = new StringBuffer();
        
        Package mergedPackage = thePackageMerge.getMergedPackage();
        
        if (mergedPackage != null) {
            symbol.append("merge ");
            symbol.append(mergedPackage.getName());
        
            ModelTree owner = mergedPackage.getOwner();
        
            if (owner != null) {
                symbol.append(" (from ");
                symbol.append(owner.getName());
                symbol.append(")");
            }
        } else {
            symbol.append("<No destination>");
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("e1a68c0e-7a5f-4ef9-a167-e03e24e5f39f")
    @Override
    public Object visitParameter(Parameter theParameter) {
        this.elementLabel = getParameterSymbol(theParameter).toString();
        return null;
    }

    @objid ("9eb435b5-02a9-4b7c-9f6a-a1fa36ed5fb9")
    @Override
    public Object visitPort(Port thePort) {
        StringBuffer symbol = new StringBuffer();
        
        symbol.append(thePort.getName());
        
        NameSpace type = getType(thePort);
        if (type != null) {
            symbol.append(" : ");
            symbol.append(type.getName());
        }
        
        // If the name is empty, build a list of provided and required
        // .Interfaces as a workaround
        if (symbol.length() == 0) {
            List<RequiredInterface> requiredInterfaces = thePort.getRequired();
            if (requiredInterfaces.size() > 0) {
                symbol.append("R = ");
                for (int i = 0; i < requiredInterfaces.size(); i++) {
                    if (i > 0) {
                        symbol.append(", ");
                    }
        
                    AuditElementLabelService labelService = new AuditElementLabelService();
                    symbol.append(labelService.getLabel(requiredInterfaces.get(i)));
                }
            }
        
            List<ProvidedInterface> ProvidedInterfaces = thePort.getProvided();
            if (ProvidedInterfaces.size() > 0) {
                if (requiredInterfaces.size() > 0) {
                    symbol.append(", ");
                }
                symbol.append("P = ");
                for (int i = 0; i < ProvidedInterfaces.size(); i++) {
                    if (i > 0) {
                        symbol.append(", ");
                    }
        
                    AuditElementLabelService labelService = new AuditElementLabelService();
                    symbol.append(labelService.getLabel(ProvidedInterfaces.get(i)));
                }
            }
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("6ea983b2-7e50-4338-bb02-d4ea59c834c6")
    @Override
    public Object visitProvidedInterface(ProvidedInterface theProvidedInterface) {
        StringBuffer symbol = new StringBuffer();
        
        List<Interface> providedElements = theProvidedInterface.getProvidedElement();
        
        if (providedElements.size() > 0) {
            for (int i = 0; i < providedElements.size(); i++) {
                if (i > 0) {
                    symbol.append(", ");
                }
                AuditElementLabelService labelService = new AuditElementLabelService();
                symbol.append(labelService.getLabel(providedElements.get(i)));
            }
        } else {
            symbol.append("none");
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("b6b80fc1-a716-48f5-b692-d014f703a3c7")
    @Override
    public Object visitRaisedException(RaisedException theRaisedException) {
        StringBuffer symbol = new StringBuffer();
        
        symbol.append("throws ");
        
        AuditElementLabelService labelService = new AuditElementLabelService();
        Classifier thrownType = theRaisedException.getThrownType();
        
        if (thrownType != null) {
            symbol.append(labelService.getLabel(thrownType));
        
            ModelTree owner = thrownType.getOwner();
        
            if (owner != null) {
                symbol.append(" (from ");
                symbol.append(owner.getName());
                symbol.append(")");
            }
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("498edf99-bddc-448a-be1a-17374ae9c743")
    @Override
    public Object visitRequiredInterface(RequiredInterface theRequiredInterface) {
        StringBuffer symbol = new StringBuffer();
        
        List<Interface> requiredElements = theRequiredInterface.getRequiredElement();
        
        if (requiredElements.size() > 0) {
            for (int i = 0; i < requiredElements.size(); i++) {
                if (i > 0) {
                    symbol.append(", ");
                }
                AuditElementLabelService labelService = new AuditElementLabelService();
                symbol.append(labelService.getLabel(requiredElements.get(i)));
            }
        } else {
            symbol.append("none");
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("a34bebd6-9afa-4af8-bc53-4c884fdd4539")
    @Override
    public Object visitSendSignalAction(SendSignalAction theSendSignalAction) {
        StringBuffer symbol = new StringBuffer();
        
        String sendSignalActionName = theSendSignalAction.getName();
        Signal signal = theSendSignalAction.getSent();
        
        if (signal != null && (sendSignalActionName.equals("Unnamed") || sendSignalActionName.equals(""))) {
            symbol.append(signal.getName());
        } else {
            symbol.append(sendSignalActionName);
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("58baa2ce-38d8-4a5e-a098-bb845cbce6a3")
    @Override
    public Object visitTemplateBinding(TemplateBinding theTemplateBinding) {
        StringBuffer symbol = new StringBuffer();
        
        NameSpace namespace = theTemplateBinding.getInstanciatedTemplate();
        if (namespace != null) {
            symbol.append(namespace.getName());
        } else {
            Operation operation = theTemplateBinding.getInstanciatedTemplateOperation();
            if (operation != null) {
                symbol.append(operation.getName());
            }
        }
        
        symbol.append("<");
        
        List<TemplateParameterSubstitution> substitutions = theTemplateBinding.getParameterSubstitution();
        for (int i = 0; i < substitutions.size(); i++) {
            if (i != 0) {
                symbol.append(", ");
            }
        
            AuditElementLabelService labelService = new AuditElementLabelService();
            symbol.append(labelService.getLabel(substitutions.get(i)));
        }
        
        symbol.append(">");
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("fdf17475-f4cc-41a0-9032-7bc806f553c0")
    @Override
    public Object visitTemplateParameter(TemplateParameter theTemplateParameter) {
        StringBuffer symbol = new StringBuffer();
        
        symbol.append(theTemplateParameter.getName());
        
        ModelElement type = theTemplateParameter.getType();
        
        if (type != null) {
            if (theTemplateParameter.isIsValueParameter()) {
                symbol.append(":");
        
                AuditElementLabelService labelService = new AuditElementLabelService();
                symbol.append(labelService.getLabel(type));
        
                symbol.append(" expression");
            } else {
                boolean isConstrained = false;
        
                if (type instanceof NameSpace) {
                    NameSpace nsType = (NameSpace) type;
                    isConstrained = nsType.getSpecialization().size() > 0;
                }
        
                if (isConstrained) {
                    symbol.append(" > ");
        
                    AuditElementLabelService labelService = new AuditElementLabelService();
                    symbol.append(labelService.getLabel(type));
                } else if (type.getClass() != Class.class || type.getExtension().size() != 0) {
                    symbol.append(" : ");
                    symbol.append(type.getMClass().getName());
                }
            }
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("1ae425aa-d6cd-4d6d-b6f0-348eb85ca93c")
    @Override
    public Object visitTemplateParameterSubstitution(TemplateParameterSubstitution theTemplateParameterSubstitution) {
        StringBuffer symbol = new StringBuffer();
        
        AuditElementLabelService labelService = new AuditElementLabelService();
        
        TemplateParameter templateParameter = theTemplateParameterSubstitution.getFormalParameter();
        if (templateParameter != null) {
            symbol.append(labelService.getLabel(templateParameter));
        }
        
        String value = theTemplateParameterSubstitution.getValue();
        if (!value.equals("")) {
            symbol.append(" = ");
            symbol.append(value);
        } else {
            ModelElement actual = theTemplateParameterSubstitution.getActual();
            if (actual != null) {
                if (actual.equals(theTemplateParameterSubstitution.getOwner())) {
                    // Prevent infinite loop
                    symbol.append(actual.getName());
                } else {
                    symbol.append(labelService.getLabel(actual));
                }
            }
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("ee657338-2461-4291-9653-548a172b84d7")
    @Override
    public Object visitTransition(Transition theTransition) {
        StringBuffer symbol = new StringBuffer();
        
        StateVertex targetVertex = theTransition.getTarget();
        
        // Trigger
        Event trigger = theTransition.getTrigger();
        if (trigger != null) {
            symbol.append(trigger.getName());
        } else {
            symbol.append(theTransition.getReceivedEvents());
        }
        
        // Guard condition
        String condition = theTransition.getGuard();
        if (condition != null && !condition.equals("")) {
            symbol.append("[");
            symbol.append(condition);
            symbol.append("]");
        }
        
        // Action
        Operation operation = theTransition.getProcessed();
        if (operation != null) {
            symbol.append("/");
            symbol.append(operation.getName());
            symbol.append("()");
        } else {
            String effect = theTransition.getEffect();
            if (effect.length() > 0) {
                symbol.append("/");
                symbol.append(effect);
            }
        }
        
        // SentEvent
        Signal effects = theTransition.getEffects();
        if (effects != null) {
            symbol.append("^");
            symbol.append(effects.getName());
            symbol.append("()");
        } else {
            String sentEvents = theTransition.getSentEvents();
            if (sentEvents.length() > 0) {
                symbol.append("^");
                symbol.append(sentEvents);
            }
        }
        
        // postGard
        String postCondition = theTransition.getPostCondition();
        if (postCondition != null && !postCondition.equals("")) {
            symbol.append("{");
            symbol.append(postCondition);
            symbol.append("}");
        }
        
        if (symbol.length() == 0) {
            symbol.append(theTransition.getName());
            if (targetVertex != null) {
                symbol.append("::");
                symbol.append(targetVertex.getName());
            }
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("42f24191-73cc-4026-8af5-43492a72cb66")
    @Override
    public Object visitUsage(Usage theUsage) {
        StringBuffer symbol = new StringBuffer();
        
        ModelElement destination = theUsage.getDependsOn();
        
        if (destination != null) {
            symbol.append("usage ");
            symbol.append(destination.getName());
        
            ModelTree owner = null;
        
            if (destination instanceof ModelTree) {
                owner = ((ModelTree) destination).getOwner();
            }
        
            if (owner != null) {
                symbol.append(" (from ");
                symbol.append(owner.getName());
                symbol.append(")");
            }
        } else {
            symbol.append("<No destination>");
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("a2a9b1ff-8e7f-4b78-afac-783446fc3f49")
    private StringBuffer getAssociationEndMultiplicity(AssociationEnd theAssociationEnd) {
        StringBuffer multiplicity = new StringBuffer();
        
        String multiplicityMinStr = theAssociationEnd.getMultiplicityMin();
        String multiplicityMaxStr = theAssociationEnd.getMultiplicityMax();
        String separator = "";
        
        if (!multiplicityMinStr.equals("") || !multiplicityMaxStr.equals("")) {
            multiplicity.append(" [");
        
            if (multiplicityMinStr.equals(multiplicityMaxStr)) {
                multiplicity.append(multiplicityMinStr);
            } else if (multiplicityMinStr.equals("0") && multiplicityMaxStr.equals("*")) {
                multiplicity.append("*");
            } else {
                if (!multiplicityMinStr.equals("") && !multiplicityMaxStr.equals("")) {
                    separator = "..";
                }
        
                multiplicity.append(multiplicityMinStr);
                multiplicity.append(separator);
                multiplicity.append(multiplicityMaxStr);
            }
            multiplicity.append("]");
        }
        return multiplicity;
    }

    @objid ("999fe007-ce5f-41c8-8788-bc523d5f7077")
    private StringBuffer getAttributeMultiplicity(Attribute theAttribute) {
        StringBuffer multiplicity = new StringBuffer();
        
        String multiplicityMinStr = theAttribute.getMultiplicityMin();
        String multiplicityMaxStr = theAttribute.getMultiplicityMax();
        String separator = "";
        
        if (multiplicityMinStr.equals("1") && multiplicityMaxStr.equals("1")) {
            return multiplicity;
        }
        
        if (!multiplicityMinStr.equals("") || !multiplicityMaxStr.equals("")) {
            multiplicity.append(" [");
        
            if (multiplicityMinStr.equals(multiplicityMaxStr)) {
                multiplicity.append(multiplicityMinStr);
            } else if (multiplicityMinStr.equals("0") && multiplicityMaxStr.equals("*")) {
                multiplicity.append("*");
            } else {
                if (!multiplicityMinStr.equals("") && !multiplicityMaxStr.equals("")) {
                    separator = "..";
                }
        
                multiplicity.append(multiplicityMinStr);
                multiplicity.append(separator);
                multiplicity.append(multiplicityMaxStr);
            }
            multiplicity.append("]");
        }
        return multiplicity;
    }

    @objid ("acfb5e10-510e-4248-98ec-d5d48d754a6f")
    private StringBuffer getParameterMultiplicity(Parameter theParameter) {
        StringBuffer multiplicity = new StringBuffer();
        
        String multiplicityMinStr = theParameter.getMultiplicityMin();
        String multiplicityMaxStr = theParameter.getMultiplicityMax();
        String separator = "";
        
        if (multiplicityMinStr.equals("1") && multiplicityMaxStr.equals("1")) {
            return multiplicity;
        }
        
        if (!multiplicityMinStr.equals("") || !multiplicityMaxStr.equals("")) {
            multiplicity.append(" [");
            // multiplicity.append("[");
            if (multiplicityMinStr.equals(multiplicityMaxStr)) {
                multiplicity.append(multiplicityMinStr);
            } else if (multiplicityMinStr.equals("0") && multiplicityMaxStr.equals("*")) {
                multiplicity.append("*");
            } else {
                if (!multiplicityMinStr.equals("") && !multiplicityMaxStr.equals("")) {
                    separator = "..";
                }
        
                multiplicity.append(multiplicityMinStr);
                multiplicity.append(separator);
                multiplicity.append(multiplicityMaxStr);
            }
            multiplicity.append("]");
        }
        return multiplicity;
    }

    @objid ("9437bd3a-da97-4f9b-bdf6-d7545182d1ab")
    private StringBuffer getParameterSymbol(Parameter theParameter) {
        StringBuffer symbol = new StringBuffer();
        
        PassingMode passingMode = theParameter.getParameterPassing();
        
        GeneralClass type = theParameter.getType();
        
        if (theParameter.getReturned() != null) {
            symbol.append("out");
        
            symbol.append(" : ");
            if (type != null) {
                symbol.append(type.getName());
            } else {
                symbol.append(Audit.I18N.getString("AuditEntryDialog.NoType"));
            }
        
            symbol.append(getParameterMultiplicity(theParameter));
        } else if (theParameter.getComposed() != null) {
            symbol.append(theParameter.getName());
            symbol.append(" ");
        
            if (passingMode == PassingMode.IN) {
                symbol.append("in");
            }
            if (passingMode == PassingMode.OUT) {
                symbol.append("out");
            }
            if (passingMode == PassingMode.INOUT) {
                symbol.append("inout");
            }
        
            symbol.append(" : ");
            if (type != null) {
                symbol.append(type.getName());
            } else {
                symbol.append(Audit.I18N.getString("AuditEntryDialog.NoType"));
            }
        
            symbol.append(getParameterMultiplicity(theParameter));
        }
        return symbol;
    }

    @objid ("a5c1b63c-30fd-4a81-9f9b-38d16213704b")
    private NameSpace getType(Port thePort) {
        ModelElement represented = thePort.getRepresentedFeature();
        
        if (represented == null) {
            return thePort.getBase();
        } else if (hasTypeCycles(thePort)) {
            return null;
        } else if (represented instanceof Attribute) {
            return ((Attribute) represented).getType();
        } else if (represented instanceof AssociationEnd) {
            
            
            Classifier type = ((AssociationEnd)represented).getTarget();
            if(type == null){
                type = ((AssociationEnd)represented).getOpposite().getSource();
            }
                 
            return type;
        } else if (represented instanceof Instance) {
            return ((Instance) represented).getBase();
        } else if (represented instanceof Parameter) {
            return ((Parameter) represented).getType();
        }
        return null;
    }

    @objid ("4536b893-eca7-4d71-97b9-8a3939fdb4f7")
    private boolean hasTypeCycles(Port thePort) {
        BindableInstance currentInstance = thePort;
        boolean hasCycle = false;
        
        while (currentInstance != null && (!hasCycle)) {
            ModelElement currentRepresented = currentInstance.getRepresentedFeature();
            if (currentRepresented != null && currentRepresented instanceof BindableInstance) {
                currentInstance = (BindableInstance) currentRepresented;
                if (thePort.equals(currentInstance)) {
                    hasCycle = true;
                }
            } else {
                currentInstance = null;
            }
        
        }
        return hasCycle;
    }

    @objid ("34ce4a5b-a03e-41f5-a557-20d4b27bd99a")
    @Override
    public Object visitUseCaseDependency(UseCaseDependency theUseCaseDependency) {
        StringBuffer symbol = new StringBuffer();
        
        List<Stereotype> stereotypes = theUseCaseDependency.getExtension();
        
        if (stereotypes.size() > 0) {
            symbol.append("<<");
            symbol.append(stereotypes.get(0).getName());
            symbol.append(">>");
        }
        
        UseCase target = theUseCaseDependency.getTarget();
        
        if (target != null) {
            symbol.append(target.getName());
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("6490eae1-84d5-4ce0-bdb6-e652969ca1a6")
    public String getElementLabel() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.elementLabel;
    }

}
