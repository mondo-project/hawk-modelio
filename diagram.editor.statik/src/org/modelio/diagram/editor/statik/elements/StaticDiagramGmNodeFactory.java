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
                                    

package org.modelio.diagram.editor.statik.elements;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.statik.elements.activity.GmActivity;
import org.modelio.diagram.editor.statik.elements.attribute.GmAttribute;
import org.modelio.diagram.editor.statik.elements.binding.GmBindingLabel;
import org.modelio.diagram.editor.statik.elements.bpmnbehavior.GmBpmnBehavior;
import org.modelio.diagram.editor.statik.elements.bpmnprocess.GmBpmnProcess;
import org.modelio.diagram.editor.statik.elements.clazz.GmClass;
import org.modelio.diagram.editor.statik.elements.collab.GmCollaboration;
import org.modelio.diagram.editor.statik.elements.collabuse.GmCollaborationUse;
import org.modelio.diagram.editor.statik.elements.collabuse.GmCollaborationUseFlatLabel;
import org.modelio.diagram.editor.statik.elements.communicationinteraction.GmCommunicationInteraction;
import org.modelio.diagram.editor.statik.elements.component.GmComponent;
import org.modelio.diagram.editor.statik.elements.datatype.GmDataType;
import org.modelio.diagram.editor.statik.elements.enumeration.GmEnum;
import org.modelio.diagram.editor.statik.elements.enumliteral.GmEnumLitteral;
import org.modelio.diagram.editor.statik.elements.informationconveyed.GmConveyedClassifierLabel;
import org.modelio.diagram.editor.statik.elements.informationconveyed.GmConveyedClassifiersGroup;
import org.modelio.diagram.editor.statik.elements.informationconveyed.GmConveyedInformationItemLabel;
import org.modelio.diagram.editor.statik.elements.informationitem.GmInformationItem;
import org.modelio.diagram.editor.statik.elements.informationitem.GmInformationItemLabel;
import org.modelio.diagram.editor.statik.elements.instance.GmInstance;
import org.modelio.diagram.editor.statik.elements.instance.GmInstanceLabel;
import org.modelio.diagram.editor.statik.elements.interaction.GmInteraction;
import org.modelio.diagram.editor.statik.elements.interfaze.GmInterface;
import org.modelio.diagram.editor.statik.elements.namespacelabel.GmNameSpaceLabel;
import org.modelio.diagram.editor.statik.elements.naryassoc.GmNAssocNode;
import org.modelio.diagram.editor.statik.elements.naryconnector.GmNConnectorNode;
import org.modelio.diagram.editor.statik.elements.narylink.GmNLinkNode;
import org.modelio.diagram.editor.statik.elements.operation.GmOperation;
import org.modelio.diagram.editor.statik.elements.packaze.GmPackage;
import org.modelio.diagram.editor.statik.elements.ports.GmPort;
import org.modelio.diagram.editor.statik.elements.requiredinterface.GmLollipopConnection;
import org.modelio.diagram.editor.statik.elements.signal.GmSignal;
import org.modelio.diagram.editor.statik.elements.slot.GmSlot;
import org.modelio.diagram.editor.statik.elements.statemachine.GmStateMachine;
import org.modelio.diagram.editor.statik.elements.staticdiagramview.GmStaticDiagramView;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.group.GmGroup;
import org.modelio.diagram.elements.core.model.IGmNodeFactory;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnProcess;
import org.modelio.metamodel.bpmn.rootElements.BpmnBehavior;
import org.modelio.metamodel.diagrams.StaticDiagram;
import org.modelio.metamodel.uml.behavior.activityModel.Activity;
import org.modelio.metamodel.uml.behavior.commonBehaviors.Signal;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationInteraction;
import org.modelio.metamodel.uml.behavior.interactionModel.Interaction;
import org.modelio.metamodel.uml.behavior.stateMachineModel.StateMachine;
import org.modelio.metamodel.uml.informationFlow.InformationItem;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.TagParameter;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.AttributeLink;
import org.modelio.metamodel.uml.statik.Binding;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.Collaboration;
import org.modelio.metamodel.uml.statik.CollaborationUse;
import org.modelio.metamodel.uml.statik.Component;
import org.modelio.metamodel.uml.statik.DataType;
import org.modelio.metamodel.uml.statik.Enumeration;
import org.modelio.metamodel.uml.statik.EnumerationLiteral;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.metamodel.uml.statik.Interface;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.NaryAssociation;
import org.modelio.metamodel.uml.statik.NaryConnector;
import org.modelio.metamodel.uml.statik.NaryLink;
import org.modelio.metamodel.uml.statik.NaryLinkEnd;
import org.modelio.metamodel.uml.statik.Operation;
import org.modelio.metamodel.uml.statik.Package;
import org.modelio.metamodel.uml.statik.Parameter;
import org.modelio.metamodel.uml.statik.Port;
import org.modelio.metamodel.visitors.DefaultModelVisitor;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * State diagram specific implementation of {@link IGmNodeFactory}.
 * <p>
 * This particular implementation:
 * <ul>
 * <li>does not support cascading</li>
 * <li>only processes state diagram specific elements</li>
 * </ul>
 */
@objid ("36bdad3b-55b7-11e2-877f-002564c97630")
public class StaticDiagramGmNodeFactory implements IGmNodeFactory {
    @objid ("36bdad3f-55b7-11e2-877f-002564c97630")
    private static final StaticDiagramGmNodeFactory _instance = new StaticDiagramGmNodeFactory();

    /**
     * Constructor.
     * @param gmDiagram
     * The diagram all nodes created by this factory will be unmasked in.
     */
    @objid ("36bdad41-55b7-11e2-877f-002564c97630")
    private StaticDiagramGmNodeFactory() {
        // Nothing to do.
    }

    @objid ("36bdad44-55b7-11e2-877f-002564c97630")
    @Override
    public GmNodeModel create(GmAbstractDiagram diagram, GmCompositeNode parent, MObject newElement, Object initialLayoutData) {
        GmNodeModel child = null;
        
        if (parent instanceof GmConveyedClassifiersGroup) {
            // Conveyed classifier group factory
            child = createInformationItemGroupNode(diagram, newElement, initialLayoutData);
        } else if (parent instanceof GmGroup) {
            // Use the group element factory visitor
            final GroupElementFactoryVisitor v = new GroupElementFactoryVisitor(diagram, initialLayoutData);
        
            child = (GmNodeModel) newElement.accept(v);
        } else {
            // Use the node factory visitor
            final NodeFactoryVisitor v = new NodeFactoryVisitor(diagram, initialLayoutData);
        
            child = (GmNodeModel) newElement.accept(v);
        }
        
        if (child != null)
            parent.addChild(child);
        return child;
    }

    @objid ("36bf339f-55b7-11e2-877f-002564c97630")
    @Override
    public boolean isRepresentable(Class<? extends MObject> metaclass) {
        if (metaclass == TaggedValue.class ||
                metaclass == TagParameter.class ||
                metaclass == Parameter.class)
            return false;
        return true;
    }

    /**
     * Register an GmNode factory extension.
     * <p>
     * Extension GmNode factories are called first when looking for an GmNode.
     * @param id id for the extension factory
     * @param factory the edit part factory.
     */
    @objid ("36bf33a7-55b7-11e2-877f-002564c97630")
    @Override
    public void registerFactory(String id, IGmNodeFactory factory) {
        throw new UnsupportedOperationException();
    }

    /**
     * Remove a registered extension factory.
     * @param id The id used to register the extension factory.
     */
    @objid ("36bf33ae-55b7-11e2-877f-002564c97630")
    @Override
    public void unregisterFactory(String id) {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the singleton instance of the node factory.
     * @return The graphic node factory.
     */
    @objid ("36bf33b2-55b7-11e2-877f-002564c97630")
    public static IGmNodeFactory getInstance() {
        return _instance;
    }

    @objid ("36bf33b9-55b7-11e2-877f-002564c97630")
    @Override
    public java.lang.Class<?> getClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @objid ("36bf33c0-55b7-11e2-877f-002564c97630")
    private GmNodeModel createInformationItemGroupNode(final GmAbstractDiagram diagram, final MObject newElement, final Object initialLayoutData) {
        GmNodeModel node = null;
        if (newElement instanceof InformationItem) {
            node = new GmConveyedInformationItemLabel(diagram,
                    (InformationItem) newElement,
                    new MRef(newElement));
            node.setLayoutData(initialLayoutData);
        } else if (newElement instanceof Classifier) {
            node = new GmConveyedClassifierLabel(diagram, (Classifier) newElement, new MRef(newElement));
            node.setLayoutData(initialLayoutData);
        }
        return node;
    }

    /**
     * Factory visitor that creates standard GmNodes.
     */
    @objid ("36bf33d0-55b7-11e2-877f-002564c97630")
    private class NodeFactoryVisitor extends DefaultModelVisitor {
        @objid ("36bf33d4-55b7-11e2-877f-002564c97630")
        private Object initialLayoutData;

        @objid ("a7d4ef69-55c2-11e2-9337-002564c97630")
        private GmAbstractDiagram diagram;

        @objid ("36bf33d8-55b7-11e2-877f-002564c97630")
        public NodeFactoryVisitor(GmAbstractDiagram diagram, Object initialLayoutData) {
            this.diagram = diagram;
            this.initialLayoutData = initialLayoutData;
        }

        @objid ("36bf33de-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitClass(org.modelio.metamodel.uml.statik.Class theClass) {
            final GmClass node = new GmClass(this.diagram, theClass, new MRef(theClass));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("36c0ba3d-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitCollaboration(Collaboration theCollaboration) {
            final GmCollaboration node = new GmCollaboration(this.diagram,
                    theCollaboration,
                    new MRef(theCollaboration));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("36c0ba45-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitCollaborationUse(CollaborationUse theCollaborationUse) {
            final GmCollaborationUse node = new GmCollaborationUse(this.diagram,
                    theCollaborationUse,
                    new MRef(theCollaborationUse));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("36c0ba4d-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitDataType(DataType theDataType) {
            final GmDataType node = new GmDataType(this.diagram, theDataType, new MRef(theDataType));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("36c0ba55-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitElement(Element theElement) {
            // We don't know what to do with that element.
            return null;
        }

        @objid ("36c0ba5d-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitEnumeration(Enumeration theEnumeration) {
            final GmEnum node = new GmEnum(this.diagram, theEnumeration, new MRef(theEnumeration));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("36c0ba65-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitEnumerationLiteral(EnumerationLiteral theEnumerationLiteral) {
            final GmEnumLitteral node = new GmEnumLitteral(this.diagram,
                    theEnumerationLiteral,
                    new MRef(theEnumerationLiteral));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("36c0ba6d-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitInstance(Instance theInstance) {
            final GmInstance node = new GmInstance(this.diagram, theInstance, new MRef(theInstance));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("36c0ba75-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitInterface(Interface theInterface) {
            final GmInterface node = new GmInterface(this.diagram, theInterface, new MRef(theInterface));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("36c0ba7d-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitPackage(Package thePackage) {
            final GmPackage packaze = new GmPackage(this.diagram, thePackage, new MRef(thePackage));
            packaze.setLayoutData(this.initialLayoutData);
            return packaze;
        }

        @objid ("36c240de-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitPort(Port thePort) {
            final GmPort node = new GmPort(this.diagram, thePort, new MRef(thePort));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("36c240e6-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitSignal(Signal theSignal) {
            final GmSignal node = new GmSignal(this.diagram, theSignal, new MRef(theSignal));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("36c240ee-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitInformationItem(final InformationItem theItem) {
            GmNodeModel node = new GmInformationItem(this.diagram, theItem, new MRef(theItem));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("36c240f7-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitComponent(final Component theComponent) {
            final GmComponent node = new GmComponent(this.diagram, theComponent, new MRef(theComponent));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("36c24100-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitStaticDiagram(final StaticDiagram arg0) {
            final GmStaticDiagramView node = new GmStaticDiagramView(this.diagram, arg0, new MRef(arg0));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

/*
        @objid ("36c24109-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitAssociation(final Association theAssoc) {
            if (theAssoc.Connection().size() > 2) {
                // N-Ary association
                GmNAssocNode node = new GmNAssocNode(this.diagram, theAssoc, new MRef(theAssoc));
                node.setLayoutData(this.initialLayoutData);
                return node;
            }
            return null;
        }
        @objid ("36c24112-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitLink(final ILink theAssoc) {
            if (theAssoc.cardConnection() > 2) {
                // N-Ary association
                GmNodeModel node = new GmNLinkNode(this.diagram, theAssoc, new MRef(theAssoc));
                node.setLayoutData(this.initialLayoutData);
                return node;
            }
            return null;
        }
        @objid ("36c3c77a-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitConnector(final Connector theAssoc) {
            if (theAssoc.cardConnection() > 2) {
                // N-Ary association
                GmNodeModel node = new GmNConnectorNode(this.diagram, theAssoc, new MRef(theAssoc));
                node.setLayoutData(this.initialLayoutData);
                return node;
            }
            return null;
        }*/
        @objid ("36c3c783-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitActivity(final Activity theItem) {
            GmActivity node = new GmActivity(this.diagram, theItem, new MRef(theItem));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("36c3c78c-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitBpmnBehavior(final BpmnBehavior theItem) {
            GmBpmnBehavior node = new GmBpmnBehavior(this.diagram, theItem, new MRef(theItem));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("36c3c795-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitBpmnProcess(final BpmnProcess theItem) {
            GmBpmnProcess node = new GmBpmnProcess(this.diagram, theItem, new MRef(theItem));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("36c3c79e-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitInteraction(final Interaction theItem) {
            GmInteraction node = new GmInteraction(this.diagram, theItem, new MRef(theItem));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("36c3c7a7-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitCommunicationInteraction(final CommunicationInteraction theItem) {
            GmCommunicationInteraction node = new GmCommunicationInteraction(this.diagram,
                    theItem,
                    new MRef(theItem));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("36c3c7b0-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitStateMachine(final StateMachine theItem) {
            GmStateMachine node = new GmStateMachine(this.diagram, theItem, new MRef(theItem));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("c654c913-bbbe-42d6-a8a3-8705c046d4f3")
        @Override
        public Object visitNaryAssociation(NaryAssociation theNaryAssociation) {
            GmNAssocNode node = new GmNAssocNode(this.diagram, theNaryAssociation, new MRef(theNaryAssociation));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("52f4b320-7cff-4509-9ae7-d7e0db9b3fb6")
        @Override
        public Object visitNaryLink(NaryLink theNaryLink) {
            GmNLinkNode node = new GmNLinkNode(this.diagram, theNaryLink, new MRef(theNaryLink));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("4f8b9a84-619c-4260-8c1b-5a6d0e28aa8e")
        @Override
        public Object visitNaryConnector(NaryConnector theNaryConnector) {
            boolean isLollipopConnection = true;
            for (NaryLinkEnd end : theNaryConnector.getNaryLinkEnd()) {
                if (end.getProvider() == null && end.getConsumer() == null) {
                    isLollipopConnection = false;
                    break;
                }
            }
            if (isLollipopConnection) {
                GmLollipopConnection node = new GmLollipopConnection(this.diagram, theNaryConnector);
                node.setLayoutData(this.initialLayoutData);
                return node;
            } else {
                GmNConnectorNode node = new GmNConnectorNode(this.diagram, theNaryConnector, new MRef(theNaryConnector));
                node.setLayoutData(this.initialLayoutData);
                return node;
            }
        }

    }

    /**
     * Factory visitor that creates instances to put into {@link GmGroup}.
     * 
     * @author cmarin
     */
    @objid ("36c54e1a-55b7-11e2-877f-002564c97630")
    private class GroupElementFactoryVisitor extends DefaultModelVisitor {
        @objid ("36c54e1e-55b7-11e2-877f-002564c97630")
        private Object initialLayoutData;

        @objid ("a7de1729-55c2-11e2-9337-002564c97630")
        private GmAbstractDiagram diagram;

        @objid ("36c54e22-55b7-11e2-877f-002564c97630")
        public GroupElementFactoryVisitor(GmAbstractDiagram diagram, Object initialLayoutData) {
            this.diagram = diagram;
            this.initialLayoutData = initialLayoutData;
        }

        @objid ("36c54e28-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitElement(Element theElement) {
            // We don't know what to do with that element.
            return null;
        }

        @objid ("36c54e30-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitAttribute(Attribute theAttribute) {
            final GmAttribute node = new GmAttribute(this.diagram, theAttribute, new MRef(theAttribute));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("36c54e38-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitAttributeLink(AttributeLink theAttributeLink) {
            final GmSlot node = new GmSlot(this.diagram, theAttributeLink, new MRef(theAttributeLink));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("36c54e40-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitClass(org.modelio.metamodel.uml.statik.Class theClass) {
            final GmNameSpaceLabel node = new GmNameSpaceLabel(this.diagram, theClass, new MRef(theClass));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        /**
         * Use {@link GmNameSpaceLabel} for any {@link NameSpace namespace} .
         */
        @objid ("36c54e48-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitNameSpace(NameSpace theNs) {
            final GmNameSpaceLabel node = new GmNameSpaceLabel(this.diagram, theNs, new MRef(theNs));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("36c54e51-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitEnumerationLiteral(final EnumerationLiteral theLiteral) {
            final GmEnumLitteral node = new GmEnumLitteral(this.diagram, theLiteral, new MRef(theLiteral));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("36c54e5a-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitInstance(Instance theInstance) {
            final GmInstanceLabel node = new GmInstanceLabel(this.diagram,
                    theInstance,
                    new MRef(theInstance));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("36c6d4bc-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitOperation(Operation theOperation) {
            final GmOperation node = new GmOperation(this.diagram, theOperation, new MRef(theOperation));
            return node;
        }

        @objid ("36c6d4c4-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitCollaborationUse(CollaborationUse theCollaborationUse) {
            final GmCollaborationUseFlatLabel node = new GmCollaborationUseFlatLabel(this.diagram,
                    theCollaborationUse,
                    new MRef(theCollaborationUse));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("36c6d4cc-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitPackage(Package thePackage) {
            final GmNameSpaceLabel node = new GmNameSpaceLabel(this.diagram,
                    thePackage,
                    new MRef(thePackage));
            node.setLayoutData(this.initialLayoutData);
            node.setShowMetaclassIcon(true);
            return node;
        }

        @objid ("36c6d4d4-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitBinding(Binding theBinding) {
            final GmBindingLabel node = new GmBindingLabel(this.diagram, theBinding, new MRef(theBinding));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("36c6d4dc-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitInformationItem(final InformationItem theItem) {
            GmInformationItemLabel node = new GmInformationItemLabel(this.diagram,
                    theItem,
                    new MRef(theItem));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("36c6d4e5-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitActivity(final Activity theItem) {
            GmActivity node = new GmActivity(this.diagram, theItem, new MRef(theItem));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("36c6d4ee-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitBpmnBehavior(final BpmnBehavior theItem) {
            GmBpmnBehavior node = new GmBpmnBehavior(this.diagram, theItem, new MRef(theItem));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("36c6d4f7-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitBpmnProcess(final BpmnProcess theItem) {
            GmBpmnProcess node = new GmBpmnProcess(this.diagram, theItem, new MRef(theItem));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("36c85b5d-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitInteraction(final Interaction theItem) {
            GmInteraction node = new GmInteraction(this.diagram, theItem, new MRef(theItem));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("36c85b66-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitCommunicationInteraction(final CommunicationInteraction theItem) {
            GmCommunicationInteraction node = new GmCommunicationInteraction(this.diagram,
                    theItem,
                    new MRef(theItem));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("36c85b6f-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitStateMachine(final StateMachine theItem) {
            GmStateMachine node = new GmStateMachine(this.diagram, theItem, new MRef(theItem));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

    }

}
