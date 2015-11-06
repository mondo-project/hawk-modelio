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
                                    

package org.modelio.core.ui.ktable.types.modelelement;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.emf.common.util.EList;
import org.modelio.core.ui.plugin.CoreUi;
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
import org.modelio.metamodel.uml.behavior.stateMachineModel.AbstractPseudoState;
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
import org.modelio.metamodel.uml.statik.NaryLinkEnd;
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

/**
 * Service that provides a label for a model element.
 */
@objid ("8d675db2-c068-11e1-8c0a-002564c97630")
class ModelElementLabelService extends DefaultModelVisitor {
    @objid ("a4780771-c068-11e1-8c0a-002564c97630")
    private String elementLabel;

    /**
     * Constructor
     */
    @objid ("8d675db9-c068-11e1-8c0a-002564c97630")
    public ModelElementLabelService() {
        super();
        this.elementLabel = "";
    }

    /**
     * Get the label for an element.
     * @param element a model element.
     * @return the label.
     */
    @objid ("8d675dbc-c068-11e1-8c0a-002564c97630")
    public String getLabel(Element element) {
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

    @objid ("8d675dc4-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitAcceptCallEventAction(AcceptCallEventAction theAcceptCallEventAction) {
        StringBuilder symbol = new StringBuilder();
        
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

    @objid ("8d675dcc-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitAcceptSignalAction(AcceptSignalAction theAcceptSignalAction) {
        StringBuilder symbol = new StringBuilder();
        
        String acceptSignalActionName = theAcceptSignalAction.getName();
        EList<Signal> signals = theAcceptSignalAction.getAccepted();
        
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

    @objid ("8d675dd4-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitActivityEdge(ActivityEdge theActivityEdge) {
        StringBuilder symbol = new StringBuilder();
        
        ActivityNode target = theActivityEdge.getTarget();
        
        if (target != null) {
            ModelElementLabelService labelService = new ModelElementLabelService();
            symbol.append(labelService.getLabel(target));
        } else {
            String name = theActivityEdge.getName();
            symbol.append(name);
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("8d675ddc-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitActivityParameterNode(ActivityParameterNode theActivityParameterNode) {
        StringBuilder symbol = new StringBuilder();
        
        // PassingMode passingMode =
        // theActivityParameterNode.getParameterPassing();
        
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
            symbol.append(CoreUi.I18N.getString("KTable.NoType"));
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("8d68e445-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitActivityPartition(ActivityPartition theActivityPartition) {
        StringBuilder symbol = new StringBuilder();
        
        symbol.append(theActivityPartition.getName());
        
        ModelElement represented = theActivityPartition.getRepresented();
        if (represented != null) {
            symbol.append(":");
        
            ModelElementLabelService labelService = new ModelElementLabelService();
            symbol.append(labelService.getLabel(represented));
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("8d68e44d-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitAssociationEnd(AssociationEnd theAssociationEnd) {
        StringBuilder symbol = new StringBuilder();
        
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
            symbol.append(CoreUi.I18N.getString("KTable.NoName"));
        } else {
            symbol.append(theAssociationEnd.getName());
        }
        
        symbol.append(": ");
        
        Classifier ownerClassifier = theAssociationEnd.getTarget();
        
        // The type
        if (ownerClassifier != null) {
            symbol.append(ownerClassifier.getName());
        } else {
            symbol.append(CoreUi.I18N.getString("KTable.NoType"));
        }
        
        // The cardinality
        symbol.append(getAssociationEndMultiplicity(theAssociationEnd));
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("8d68e455-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitAttribute(Attribute theAttribute) {
        StringBuilder symbol = new StringBuilder();
        
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
            symbol.append(CoreUi.I18N.getString("KTable.NoType"));
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

    @objid ("8d68e45d-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitAttributeLink(AttributeLink theAttributeLink) {
        StringBuilder symbol = new StringBuilder();
        
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

    @objid ("8d68e465-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBehaviorParameter(BehaviorParameter theBehaviorParameter) {
        Parameter theParameter = theBehaviorParameter.getMapped();
        
        StringBuilder symbol = new StringBuilder();
        
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
            symbol.append(CoreUi.I18N.getString("KTable.NoType"));
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("8d68e46d-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitBinding(Binding theBinding) {
        StringBuilder symbol = new StringBuilder();
        
        ModelElement role = theBinding.getRole();
        ModelElement feature = theBinding.getRepresentedFeature();
        
        ModelElementLabelService labelService = new ModelElementLabelService();
        
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

    @objid ("8d68e475-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitCallBehaviorAction(CallBehaviorAction theCallBehaviorAction) {
        StringBuilder symbol = new StringBuilder();
        
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

    @objid ("8d68e47d-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitCallOperationAction(CallOperationAction theCallOperationAction) {
        StringBuilder symbol = new StringBuilder();
        
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

    @objid ("8d68e485-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitClassAssociation(ClassAssociation theClassAssociation) {
        StringBuilder symbol = new StringBuilder();
        
        Class theClass = theClassAssociation.getClassPart();
        
        if (theClass != null) {
            symbol.append(theClass.getName());
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("8d6a6aeb-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitClause(Clause theClause) {
        StringBuilder symbol = new StringBuilder();
        
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

    @objid ("8d6a6af3-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitCollaborationUse(CollaborationUse theCollaborationUse) {
        StringBuilder symbol = new StringBuilder();
        
        symbol.append(theCollaborationUse.getName());
        
        Collaboration base = theCollaborationUse.getType();
        symbol.append(": ");
        if (base != null) {
            symbol.append(base.getName());
        } else {
            symbol.append(CoreUi.I18N.getString("KTable.NoBase"));
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("8d6a6afb-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitCommunicationMessage(CommunicationMessage theCommunicationMessage) {
        StringBuilder symbol = new StringBuilder();
        
        String name = theCommunicationMessage.getName();
        MessageSort messageSort = theCommunicationMessage.getSortOfMessage();
        
        if (name.equals("")) {
            symbol.append(CoreUi.I18N.getString(messageSort.name()));
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

    @objid ("8d6a6b03-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitCommunicationNode(CommunicationNode theCommunicationNode) {
        StringBuilder symbol = new StringBuilder();
        
        Instance instance = theCommunicationNode.getRepresented();
        
        if (instance != null) {
            ModelElementLabelService labelService = new ModelElementLabelService();
            symbol.append(labelService.getLabel(instance));
        } else {
            symbol.append(theCommunicationNode.getName());
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("8d6a6b0b-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitDataFlow(DataFlow theDataFlow) {
        StringBuilder symbol = new StringBuilder();
        
        Signal signal = theDataFlow.getSModel();
        
        if (signal != null) {
            symbol.append(signal.getName());
        } else {
            symbol.append(theDataFlow.getName());
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("8d6a6b13-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitDependency(Dependency theDependency) {
        StringBuilder symbol = new StringBuilder();
        
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

    @objid ("8d6a6b1b-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitElement(Element theElement) {
        this.elementLabel = theElement.getClass().getSimpleName();
        return null;
    }

    @objid ("8d6bf185-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitElementImport(ElementImport theElementImport) {
        StringBuilder symbol = new StringBuilder();
        
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

    @objid ("8d6bf18d-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitEvent(Event theEvent) {
        StringBuilder symbol = new StringBuilder();
        
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

    @objid ("8d6bf195-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitExtensionPoint(ExtensionPoint theExtensionPoint) {
        StringBuilder symbol = new StringBuilder();
        
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

    @objid ("8d6bf19d-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitFeature(Feature theFeature) {
        StringBuilder symbol = new StringBuilder();
        
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

    @objid ("8d6bf1a5-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitGeneralization(Generalization theGeneralization) {
        StringBuilder symbol = new StringBuilder();
        
        symbol.append("is_a ");
        
        ModelElementLabelService labelService = new ModelElementLabelService();
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

    @objid ("8d6bf1ad-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitInformationFlow(InformationFlow theInformationFlow) {
        StringBuilder symbol = new StringBuilder();
        
        EList<Classifier> classifiers = theInformationFlow.getConveyed();
        
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

    @objid ("8d6bf1b5-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitInstance(Instance theInstance) {
        StringBuilder symbol = new StringBuilder();
        
        symbol.append(theInstance.getName());
        
        NameSpace base = theInstance.getBase();
        symbol.append(": ");
        if (base != null) {
            symbol.append(base.getName());
        } else {
            symbol.append(CoreUi.I18N.getString("KTable.NoBase"));
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("8d6bf1bd-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitInstanceNode(InstanceNode theInstanceNode) {
        StringBuilder symbol = new StringBuilder();
        
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
            ModelElementLabelService labelService = new ModelElementLabelService();
            symbol.append(labelService.getLabel(instance));
        } else if (attribut != null) {
            ModelElementLabelService labelService = new ModelElementLabelService();
            symbol.append(labelService.getLabel(attribut));
        } else if (assocEnd != null) {
            ModelElementLabelService labelService = new ModelElementLabelService();
            symbol.append(labelService.getLabel(assocEnd));
        } else if (behaviorParameter != null) {
            ModelElementLabelService labelService = new ModelElementLabelService();
            symbol.append(labelService.getLabel(behaviorParameter));
        } else {
            symbol.append(theInstanceNode.getName());
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("8d6d7826-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitInterfaceRealization(InterfaceRealization theInterfaceRealization) {
        StringBuilder symbol = new StringBuilder();
        
        // symbol.append("Realize ");
        
        ModelElementLabelService labelService = new ModelElementLabelService();
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

    @objid ("8d6d782e-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitInternalTransition(InternalTransition theInternalTransition) {
        if (this.elementLabel.equals("")) {
            this.elementLabel = "/";
        }
        return null;
    }

    @objid ("8d6d7836-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitLinkEnd(LinkEnd theLinkEnd) {
        StringBuilder symbol = new StringBuilder();
        
        String linkEndName = theLinkEnd.getName();
        
        if (theLinkEnd.getOpposite() != null) {
            if (linkEndName.equals("")) {
                symbol.append(CoreUi.I18N.getString("KTable.NoName"));
            } else {
                symbol.append(linkEndName);
            }
            Instance linked = theLinkEnd.getOpposite().getTarget();
            if (linked != null) {
                symbol.append("::");
                symbol.append(linked.getName());
            }
        } else {
            if (linkEndName.equals("")) {
                symbol.append(CoreUi.I18N.getString("KTable.NoName"));
            } else {
                symbol.append(linkEndName);
            }
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("8d6d783e-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitManifestation(Manifestation theManifestation) {
        StringBuilder symbol = new StringBuilder();
        
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

    @objid ("8d6d7846-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitModelElement(ModelElement theModelElement) {
        this.elementLabel = theModelElement.getName();
        return null;
    }

    @objid ("8d6d784e-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitObjectNode(ObjectNode theObjectNode) {
        StringBuilder symbol = new StringBuilder();
        
        BehaviorParameter parameter = theObjectNode.getRepresentedRealParameter();
        Instance instance = theObjectNode.getRepresented();
        Attribute attribute = theObjectNode.getRepresentedAttribute();
        AssociationEnd role = theObjectNode.getRepresentedRole();
        
        ModelElementLabelService labelService = new ModelElementLabelService();
        
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
        
            // jectNodeOrderingKind ordering = theObjectNode.getOrdering();
        
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

    @objid ("8d6d7856-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitOperation(Operation theOperation) {
        StringBuilder symbol = new StringBuilder();
        
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
        
        Classifier parentClassifier = theOperation.getOwner();
        if (parentClassifier != null) {
            symbol.append(parentClassifier.getName());
            symbol.append("::");
        }
        
        symbol.append(theOperation.getName());
        
        symbol.append("(");
        EList<Parameter> parameters = theOperation.getIO();
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
                symbol.append(CoreUi.I18N.getString("KTable.NoType"));
            }
            symbol.append(getParameterMultiplicity(returnParameter));
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("8d6efec5-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitPackageImport(PackageImport thePackageImport) {
        StringBuilder symbol = new StringBuilder();
        
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

    @objid ("8d6efecd-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitPackageMerge(PackageMerge thePackageMerge) {
        StringBuilder symbol = new StringBuilder();
        
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

    @objid ("8d6efed5-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitParameter(Parameter theParameter) {
        this.elementLabel = getParameterSymbol(theParameter).toString();
        return null;
    }

    @objid ("8d6efedd-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitPort(Port thePort) {
        StringBuilder symbol = new StringBuilder();
        
        symbol.append(thePort.getName());
        
        NameSpace type = getType(thePort);
        if (type != null) {
            symbol.append(" : ");
            symbol.append(type.getName());
        }
        
        // If the name is empty, build a list of provided and required
        // interfaces as a workaround
        if (symbol.length() == 0) {
            EList<RequiredInterface> requiredInterfaces = thePort.getRequired();
            if (requiredInterfaces.size() > 0) {
                symbol.append("R = ");
                for (int i = 0; i < requiredInterfaces.size(); i++) {
                    if (i > 0) {
                        symbol.append(", ");
                    }
        
                    ModelElementLabelService labelService = new ModelElementLabelService();
                    symbol.append(labelService.getLabel(requiredInterfaces.get(i)));
                }
            }
        
            EList<ProvidedInterface> providedInterfaces = thePort.getProvided();
            if (providedInterfaces.size() > 0) {
                if (requiredInterfaces.size() > 0) {
                    symbol.append(", ");
                }
                symbol.append("P = ");
                for (int i = 0; i < providedInterfaces.size(); i++) {
                    if (i > 0) {
                        symbol.append(", ");
                    }
        
                    ModelElementLabelService labelService = new ModelElementLabelService();
                    symbol.append(labelService.getLabel(providedInterfaces.get(i)));
                }
            }
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("8d6efee5-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitProvidedInterface(ProvidedInterface theProvidedInterface) {
        StringBuilder symbol = new StringBuilder();
        
        EList<Interface> providedElements = theProvidedInterface.getProvidedElement();
        
        if (providedElements.size() > 0) {
            for (int i = 0; i < providedElements.size(); i++) {
                if (i > 0) {
                    symbol.append(", ");
                }
                ModelElementLabelService labelService = new ModelElementLabelService();
                symbol.append(labelService.getLabel(providedElements.get(i)));
            }
        } else {
            symbol.append("none");
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("8d6efeed-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitRaisedException(RaisedException theRaisedException) {
        StringBuilder symbol = new StringBuilder();
        
        symbol.append("throws ");
        
        ModelElementLabelService labelService = new ModelElementLabelService();
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

    @objid ("8d6efef5-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitRequiredInterface(RequiredInterface theRequiredInterface) {
        StringBuilder symbol = new StringBuilder();
        
        EList<Interface> requiredElements = theRequiredInterface.getRequiredElement();
        
        if (requiredElements.size() > 0) {
            for (int i = 0; i < requiredElements.size(); i++) {
                if (i > 0) {
                    symbol.append(", ");
                }
                ModelElementLabelService labelService = new ModelElementLabelService();
                symbol.append(labelService.getLabel(requiredElements.get(i)));
            }
        } else {
            symbol.append("none");
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("8d6efefd-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitSendSignalAction(SendSignalAction theSendSignalAction) {
        StringBuilder symbol = new StringBuilder();
        
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

    @objid ("8d70856c-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitTemplateBinding(TemplateBinding theTemplateBinding) {
        StringBuilder symbol = new StringBuilder();
        
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
        
        EList<TemplateParameterSubstitution> substitutions = theTemplateBinding.getParameterSubstitution();
        for (int i = 0; i < substitutions.size(); i++) {
            if (i != 0) {
                symbol.append(", ");
            }
        
            ModelElementLabelService labelService = new ModelElementLabelService();
            symbol.append(labelService.getLabel(substitutions.get(i)));
        }
        
        symbol.append(">");
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("8d708574-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitTemplateParameter(TemplateParameter theTemplateParameter) {
        StringBuilder symbol = new StringBuilder();
        
        symbol.append(theTemplateParameter.getName());
        
        ModelElement type = theTemplateParameter.getType();
        
        if (type != null) {
            if (theTemplateParameter.isIsValueParameter()) {
                symbol.append(":");
        
                ModelElementLabelService labelService = new ModelElementLabelService();
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
        
                    ModelElementLabelService labelService = new ModelElementLabelService();
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

    @objid ("8d70857c-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitTemplateParameterSubstitution(TemplateParameterSubstitution theTemplateParameterSubstitution) {
        StringBuilder symbol = new StringBuilder();
        
        ModelElementLabelService labelService = new ModelElementLabelService();
        
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
                symbol.append(labelService.getLabel(actual));
            }
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("8d708584-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitTransition(Transition theTransition) {
        StringBuilder symbol = new StringBuilder();
        
        StateVertex targetVertex = theTransition.getTarget();
        boolean withEvent = true;
        
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
        if (effects != null && withEvent) {
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
            if (targetVertex instanceof AbstractPseudoState) {
                AbstractPseudoState target = ((AbstractPseudoState) targetVertex);
                ModelElementLabelService labelService = new ModelElementLabelService();
                symbol.append(labelService.getLabel(target));
            } else {
                symbol.append(theTransition.getName());
                if (targetVertex != null) {
                    symbol.append("::");
                    symbol.append(targetVertex.getName());
                }
            }
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

    @objid ("8d70858c-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitUsage(Usage theUsage) {
        StringBuilder symbol = new StringBuilder();
        
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

    @objid ("8d708594-c068-11e1-8c0a-002564c97630")
    @Override
    public Object visitUseCaseDependency(UseCaseDependency theUseCaseDependency) {
        StringBuilder symbol = new StringBuilder();
        
        EList<Stereotype> stereotypes = theUseCaseDependency.getExtension();
        
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

    @objid ("8d720c05-c068-11e1-8c0a-002564c97630")
    private static StringBuilder getAssociationEndMultiplicity(AssociationEnd theAssociationEnd) {
        StringBuilder multiplicity = new StringBuilder();
        
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

    @objid ("8d720c0c-c068-11e1-8c0a-002564c97630")
    private static StringBuilder getAttributeMultiplicity(Attribute theAttribute) {
        StringBuilder multiplicity = new StringBuilder();
        
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

    @objid ("8d720c13-c068-11e1-8c0a-002564c97630")
    private static StringBuilder getParameterMultiplicity(Parameter theParameter) {
        StringBuilder multiplicity = new StringBuilder();
        
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

    @objid ("8d720c1a-c068-11e1-8c0a-002564c97630")
    private static StringBuilder getParameterSymbol(Parameter theParameter) {
        StringBuilder symbol = new StringBuilder();
        
        PassingMode passingMode = theParameter.getParameterPassing();
        
        GeneralClass type = theParameter.getType();
        
        if (theParameter.getReturned() != null) {
            symbol.append("out");
        
            symbol.append(" : ");
            if (type != null) {
                symbol.append(type.getName());
            } else {
                symbol.append(CoreUi.I18N.getString("KTable.NoType"));
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
                symbol.append(CoreUi.I18N.getString("KTable.NoType"));
            }
        
            symbol.append(getParameterMultiplicity(theParameter));
        }
        return symbol;
    }

    @objid ("8d720c21-c068-11e1-8c0a-002564c97630")
    private static NameSpace getType(Port thePort) {
        ModelElement represented = thePort.getRepresentedFeature();
        
        if (represented == null) {
            return thePort.getBase();
        } else if (hasTypeCycles(thePort)) {
            return null;
        } else if (represented instanceof Attribute) {
            return ((Attribute) represented).getType();
        } else if (represented instanceof AssociationEnd) {
            return ((AssociationEnd) represented).getTarget();
        } else if (represented instanceof Instance) {
            return ((Instance) represented).getBase();
        } else if (represented instanceof Parameter) {
            return ((Parameter) represented).getType();
        }
        return null;
    }

    @objid ("8d720c2a-c068-11e1-8c0a-002564c97630")
    private static boolean hasTypeCycles(Port thePort) {
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

    @objid ("000d91a2-230d-10a1-ac17-001ec947cd2a")
    @Override
    public Object visitNaryLinkEnd(NaryLinkEnd theLinkEnd) {
        StringBuilder symbol = new StringBuilder();
        
        String linkEndName = theLinkEnd.getName();
        
        if (theLinkEnd.getNaryLink() != null) {
            for (int i = 0; i < theLinkEnd.getNaryLink().getNaryLinkEnd().size(); i++) {
                NaryLinkEnd end = theLinkEnd.getNaryLink().getNaryLinkEnd().get(i);
                if (i > 0) {
                    symbol.append(", ");
                }
        
                String endName = end.getName();
                if (endName.equals("")) {
                    symbol.append(CoreUi.I18N.getString("KTable.NoName"));
                } else {
                    symbol.append(endName);
                }
        
                Instance linked = end.getSource();
        
                if (linked != null) {
                    symbol.append("::");
                    String linkedName = linked.getName();
                    if (linkedName.equals("")) {
                        symbol.append(CoreUi.I18N.getString("KTable.NoName"));
                    } else {
                        symbol.append(linkedName);
                    }
                }
            }
        } else {
            if (linkEndName.equals("")) {
                symbol.append(CoreUi.I18N.getString("KTable.NoName"));
            } else {
                symbol.append(linkEndName);
            }
        }
        
        this.elementLabel = symbol.toString();
        return null;
    }

}
