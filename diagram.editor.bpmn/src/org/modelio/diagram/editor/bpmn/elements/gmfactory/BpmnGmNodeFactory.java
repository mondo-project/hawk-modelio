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
                                    

package org.modelio.diagram.editor.bpmn.elements.gmfactory;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.commands.IParameter;
import org.modelio.diagram.editor.bpmn.elements.bpmnadhocsubprocess.GmBpmnAdHocSubProcess;
import org.modelio.diagram.editor.bpmn.elements.bpmnboundaryevent.GmBpmnBoundaryEvent;
import org.modelio.diagram.editor.bpmn.elements.bpmnbusinessruletask.GmBpmnBusinessRuleTask;
import org.modelio.diagram.editor.bpmn.elements.bpmncallactivity.GmBpmnCallActivity;
import org.modelio.diagram.editor.bpmn.elements.bpmncomplexgateway.GmBpmnComplexGateway;
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
import org.modelio.diagram.editor.bpmn.elements.bpmnparallelgateway.GmBpmnParallelGateway;
import org.modelio.diagram.editor.bpmn.elements.bpmnreceivetask.GmBpmnReceiveTask;
import org.modelio.diagram.editor.bpmn.elements.bpmnsendtask.GmBpmnSendTask;
import org.modelio.diagram.editor.bpmn.elements.bpmnservicetask.GmBpmnServiceTask;
import org.modelio.diagram.editor.bpmn.elements.bpmnsripttask.GmBpmnScriptTask;
import org.modelio.diagram.editor.bpmn.elements.bpmnstartevent.GmBpmnStartEvent;
import org.modelio.diagram.editor.bpmn.elements.bpmnsubprocess.GmBpmnSubProcess;
import org.modelio.diagram.editor.bpmn.elements.bpmntask.GmBpmnTask;
import org.modelio.diagram.editor.bpmn.elements.bpmntransaction.GmBpmnTransaction;
import org.modelio.diagram.editor.bpmn.elements.bpmnusertask.GmBpmnUserTask;
import org.modelio.diagram.editor.bpmn.elements.diagrams.view.GmBpmnDiagramView;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.group.GmGroup;
import org.modelio.diagram.elements.core.model.IGmNodeFactory;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.metamodel.bpmn.activities.BpmnAdHocSubProcess;
import org.modelio.metamodel.bpmn.activities.BpmnBusinessRuleTask;
import org.modelio.metamodel.bpmn.activities.BpmnCallActivity;
import org.modelio.metamodel.bpmn.activities.BpmnManualTask;
import org.modelio.metamodel.bpmn.activities.BpmnReceiveTask;
import org.modelio.metamodel.bpmn.activities.BpmnScriptTask;
import org.modelio.metamodel.bpmn.activities.BpmnSendTask;
import org.modelio.metamodel.bpmn.activities.BpmnServiceTask;
import org.modelio.metamodel.bpmn.activities.BpmnSubProcess;
import org.modelio.metamodel.bpmn.activities.BpmnTask;
import org.modelio.metamodel.bpmn.activities.BpmnTransaction;
import org.modelio.metamodel.bpmn.activities.BpmnUserTask;
import org.modelio.metamodel.bpmn.bpmnDiagrams.BpmnProcessCollaborationDiagram;
import org.modelio.metamodel.bpmn.bpmnDiagrams.BpmnSubProcessDiagram;
import org.modelio.metamodel.bpmn.events.BpmnBoundaryEvent;
import org.modelio.metamodel.bpmn.events.BpmnEndEvent;
import org.modelio.metamodel.bpmn.events.BpmnIntermediateCatchEvent;
import org.modelio.metamodel.bpmn.events.BpmnIntermediateThrowEvent;
import org.modelio.metamodel.bpmn.events.BpmnStartEvent;
import org.modelio.metamodel.bpmn.flows.BpmnMessage;
import org.modelio.metamodel.bpmn.gateways.BpmnComplexGateway;
import org.modelio.metamodel.bpmn.gateways.BpmnEventBasedGateway;
import org.modelio.metamodel.bpmn.gateways.BpmnExclusiveGateway;
import org.modelio.metamodel.bpmn.gateways.BpmnInclusiveGateway;
import org.modelio.metamodel.bpmn.gateways.BpmnParallelGateway;
import org.modelio.metamodel.bpmn.objects.BpmnDataInput;
import org.modelio.metamodel.bpmn.objects.BpmnDataObject;
import org.modelio.metamodel.bpmn.objects.BpmnDataOutput;
import org.modelio.metamodel.bpmn.objects.BpmnDataStore;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLane;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLaneSet;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.TagParameter;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
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
@objid ("620d211a-55b6-11e2-877f-002564c97630")
public class BpmnGmNodeFactory implements IGmNodeFactory {
    @objid ("620d211e-55b6-11e2-877f-002564c97630")
    private static final BpmnGmNodeFactory _instance = new BpmnGmNodeFactory();

    @objid ("620d2120-55b6-11e2-877f-002564c97630")
    @Override
    public GmNodeModel create(GmAbstractDiagram diagram, GmCompositeNode parent, MObject newElement, Object initialLayoutData) {
        if (parent instanceof GmGroup) {
            // Use the group element factory visitor
            final GroupElementFactoryVisitor v = new GroupElementFactoryVisitor(diagram);
        
            final GmNodeModel child = (GmNodeModel) newElement.accept(v);
            if (child != null)
                parent.addChild(child);
            return child;
        } else {
            // Use the node factory visitor
            final NodeFactoryVisitor v = new NodeFactoryVisitor(diagram, parent, initialLayoutData);
        
            final GmNodeModel child = (GmNodeModel) newElement.accept(v);
            if (child != null)
                parent.addChild(child);
            return child;
        }
    }

    @objid ("620ea787-55b6-11e2-877f-002564c97630")
    @Override
    public boolean isRepresentable(Class<? extends MObject> metaclass) {
        if (metaclass == TaggedValue.class ||
            metaclass == TagParameter.class ||
            metaclass == IParameter.class)
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
    @objid ("620ea78f-55b6-11e2-877f-002564c97630")
    @Override
    public void registerFactory(String id, IGmNodeFactory factory) {
        throw new UnsupportedOperationException();
    }

    /**
     * Remove a registered extension factory.
     * @param id The id used to register the extension factory.
     */
    @objid ("620ea796-55b6-11e2-877f-002564c97630")
    @Override
    public void unregisterFactory(String id) {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the singleton instance of the node factory.
     * @return The graphic node factory.
     */
    @objid ("620ea79a-55b6-11e2-877f-002564c97630")
    public static IGmNodeFactory getInstance() {
        return _instance;
    }

    @objid ("620ea7a1-55b6-11e2-877f-002564c97630")
    @Override
    public Class<?> getClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * Constructor.
     */
    @objid ("620ea7a8-55b6-11e2-877f-002564c97630")
    public BpmnGmNodeFactory() {
        // Nothing to do.
    }

    /**
     * Factory visitor that creates standard GmNodes.
     */
    @objid ("620ea7ab-55b6-11e2-877f-002564c97630")
    private class NodeFactoryVisitor extends DefaultModelVisitor {
        @objid ("620ea7af-55b6-11e2-877f-002564c97630")
        private Object initialLayoutData;

        @objid ("728f59ea-55c1-11e2-9337-002564c97630")
        private GmAbstractDiagram diagram;

        @objid ("728f59eb-55c1-11e2-9337-002564c97630")
        private GmCompositeNode parent;

        @objid ("620ea7b6-55b6-11e2-877f-002564c97630")
        public NodeFactoryVisitor(GmAbstractDiagram diagram, GmCompositeNode parent, Object initialLayoutData) {
            this.diagram = diagram;
            this.parent = parent;
            this.initialLayoutData = initialLayoutData;
        }

        @objid ("620ea7bf-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitElement(Element theElement) {
            // We don't know what to do with that element.
            return null;
        }

        @objid ("6214c21d-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitBpmnProcessCollaborationDiagram(final BpmnProcessCollaborationDiagram arg0) {
            final GmBpmnDiagramView node = new GmBpmnDiagramView(this.diagram, arg0, new MRef(arg0));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("6214c226-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitBpmnSubProcessDiagram(final BpmnSubProcessDiagram arg0) {
            final GmBpmnDiagramView node = new GmBpmnDiagramView(this.diagram, arg0, new MRef(arg0));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("f7cd5b2a-5a3f-11e2-9e33-00137282c51b")
        @Override
        public Object visitBpmnBusinessRuleTask(BpmnBusinessRuleTask element) {
            final GmBpmnBusinessRuleTask instanceNode = new GmBpmnBusinessRuleTask(this.diagram,
                                                                                   element,
                                                                                   new MRef(element));
            instanceNode.setLayoutData(this.initialLayoutData);
            return instanceNode;
        }

        @objid ("f7cd5b30-5a3f-11e2-9e33-00137282c51b")
        @Override
        public Object visitBpmnTask(BpmnTask element) {
            final GmBpmnTask instanceNode = new GmBpmnTask(this.diagram, element, new MRef(element));
            instanceNode.setLayoutData(this.initialLayoutData);
            return instanceNode;
        }

        @objid ("f7cfbd85-5a3f-11e2-9e33-00137282c51b")
        @Override
        public Object visitBpmnSendTask(BpmnSendTask element) {
            final GmBpmnSendTask instanceNode = new GmBpmnSendTask(this.diagram, element, new MRef(element));
            instanceNode.setLayoutData(this.initialLayoutData);
            return instanceNode;
        }

        @objid ("f7cfbd8b-5a3f-11e2-9e33-00137282c51b")
        @Override
        public Object visitBpmnReceiveTask(BpmnReceiveTask element) {
            final GmBpmnReceiveTask instanceNode = new GmBpmnReceiveTask(this.diagram,
                                                                         element,
                                                                         new MRef(element));
            instanceNode.setLayoutData(this.initialLayoutData);
            return instanceNode;
        }

        @objid ("f7cfbd91-5a3f-11e2-9e33-00137282c51b")
        @Override
        public Object visitBpmnServiceTask(BpmnServiceTask element) {
            final GmBpmnServiceTask instanceNode = new GmBpmnServiceTask(this.diagram,
                                                                         element,
                                                                         new MRef(element));
            instanceNode.setLayoutData(this.initialLayoutData);
            return instanceNode;
        }

        @objid ("f7cfbd97-5a3f-11e2-9e33-00137282c51b")
        @Override
        public Object visitBpmnUserTask(BpmnUserTask element) {
            final GmBpmnUserTask instanceNode = new GmBpmnUserTask(this.diagram, element, new MRef(element));
            instanceNode.setLayoutData(this.initialLayoutData);
            return instanceNode;
        }

        @objid ("f7cfbd9d-5a3f-11e2-9e33-00137282c51b")
        @Override
        public Object visitBpmnManualTask(BpmnManualTask element) {
            final GmBpmnManualTask instanceNode = new GmBpmnManualTask(this.diagram,
                                                                       element,
                                                                       new MRef(element));
            instanceNode.setLayoutData(this.initialLayoutData);
            return instanceNode;
        }

        @objid ("f7cfbda3-5a3f-11e2-9e33-00137282c51b")
        @Override
        public Object visitBpmnScriptTask(BpmnScriptTask element) {
            final GmBpmnScriptTask instanceNode = new GmBpmnScriptTask(this.diagram,
                                                                       element,
                                                                       new MRef(element));
            instanceNode.setLayoutData(this.initialLayoutData);
            return instanceNode;
        }

        @objid ("f7cfbda9-5a3f-11e2-9e33-00137282c51b")
        @Override
        public Object visitBpmnExclusiveGateway(BpmnExclusiveGateway element) {
            final GmBpmnExclusiveGateway instanceNode = new GmBpmnExclusiveGateway(this.diagram,
                                                                                   element,
                                                                                   new MRef(element));
            instanceNode.setLayoutData(this.initialLayoutData);
            return instanceNode;
        }

        @objid ("f7d21fde-5a3f-11e2-9e33-00137282c51b")
        @Override
        public Object visitBpmnInclusiveGateway(BpmnInclusiveGateway element) {
            final GmBpmnInclusiveGateway instanceNode = new GmBpmnInclusiveGateway(this.diagram,
                                                                                   element,
                                                                                   new MRef(element));
            instanceNode.setLayoutData(this.initialLayoutData);
            return instanceNode;
        }

        @objid ("f7d21fe4-5a3f-11e2-9e33-00137282c51b")
        @Override
        public Object visitBpmnComplexGateway(BpmnComplexGateway element) {
            final GmBpmnComplexGateway instanceNode = new GmBpmnComplexGateway(this.diagram,
                                                                               element,
                                                                               new MRef(element));
            instanceNode.setLayoutData(this.initialLayoutData);
            return instanceNode;
        }

        @objid ("f7d21fea-5a3f-11e2-9e33-00137282c51b")
        @Override
        public Object visitBpmnParallelGateway(BpmnParallelGateway element) {
            final GmBpmnParallelGateway instanceNode = new GmBpmnParallelGateway(this.diagram,
                                                                                 element,
                                                                                 new MRef(element));
            instanceNode.setLayoutData(this.initialLayoutData);
            return instanceNode;
        }

        @objid ("f7d21ff0-5a3f-11e2-9e33-00137282c51b")
        @Override
        public Object visitBpmnEventBasedGateway(BpmnEventBasedGateway element) {
            final GmBpmnEventBasedGateway instanceNode = new GmBpmnEventBasedGateway(this.diagram,
                                                                                     element,
                                                                                     new MRef(element));
            instanceNode.setLayoutData(this.initialLayoutData);
            return instanceNode;
        }

        @objid ("f7d21ff6-5a3f-11e2-9e33-00137282c51b")
        @Override
        public Object visitBpmnStartEvent(BpmnStartEvent element) {
            final GmBpmnStartEvent instanceNode = new GmBpmnStartEvent(this.diagram,
                                                                       element,
                                                                       new MRef(element));
            instanceNode.setLayoutData(this.initialLayoutData);
            return instanceNode;
        }

        @objid ("f7d21ffc-5a3f-11e2-9e33-00137282c51b")
        @Override
        public Object visitBpmnBoundaryEvent(BpmnBoundaryEvent element) {
            final GmBpmnBoundaryEvent instanceNode = new GmBpmnBoundaryEvent(this.diagram,
                                                                             element,
                                                                             new MRef(element));
            instanceNode.setLayoutData(this.initialLayoutData);
            return instanceNode;
        }

        @objid ("f7d22002-5a3f-11e2-9e33-00137282c51b")
        @Override
        public Object visitBpmnEndEvent(BpmnEndEvent element) {
            final GmBpmnEndEvent instanceNode = new GmBpmnEndEvent(this.diagram, element, new MRef(element));
            instanceNode.setLayoutData(this.initialLayoutData);
            return instanceNode;
        }

        @objid ("f7d48239-5a3f-11e2-9e33-00137282c51b")
        @Override
        public Object visitBpmnIntermediateThrowEvent(BpmnIntermediateThrowEvent element) {
            final GmBpmnIntermediateThrowEvent instanceNode = new GmBpmnIntermediateThrowEvent(this.diagram,
                                                                                               element,
                                                                                               new MRef(element));
            instanceNode.setLayoutData(this.initialLayoutData);
            return instanceNode;
        }

        @objid ("f7d4823f-5a3f-11e2-9e33-00137282c51b")
        @Override
        public Object visitBpmnIntermediateCatchEvent(BpmnIntermediateCatchEvent element) {
            final GmBpmnIntermediateCatchEvent instanceNode = new GmBpmnIntermediateCatchEvent(this.diagram,
                                                                                               element,
                                                                                               new MRef(element));
            instanceNode.setLayoutData(this.initialLayoutData);
            return instanceNode;
        }

        @objid ("f7d48245-5a3f-11e2-9e33-00137282c51b")
        @Override
        public Object visitBpmnCallActivity(BpmnCallActivity element) {
            final GmBpmnCallActivity instanceNode = new GmBpmnCallActivity(this.diagram,
                                                                           element,
                                                                           new MRef(element));
            instanceNode.setLayoutData(this.initialLayoutData);
            return instanceNode;
        }

        @objid ("f7d4824b-5a3f-11e2-9e33-00137282c51b")
        @Override
        public Object visitBpmnAdHocSubProcess(BpmnAdHocSubProcess element) {
            final GmBpmnAdHocSubProcess instanceNode = new GmBpmnAdHocSubProcess(this.diagram,
                                                                                 element,
                                                                                 new MRef(element));
            instanceNode.setLayoutData(this.initialLayoutData);
            return instanceNode;
        }

        @objid ("f7d48251-5a3f-11e2-9e33-00137282c51b")
        @Override
        public Object visitBpmnSubProcess(BpmnSubProcess element) {
            final GmBpmnSubProcess instanceNode = new GmBpmnSubProcess(this.diagram,
                                                                       element,
                                                                       new MRef(element));
            instanceNode.setLayoutData(this.initialLayoutData);
            return instanceNode;
        }

        @objid ("f7d48257-5a3f-11e2-9e33-00137282c51b")
        @Override
        public Object visitBpmnTransaction(BpmnTransaction element) {
            final GmBpmnTransaction instanceNode = new GmBpmnTransaction(this.diagram,
                                                                         element,
                                                                         new MRef(element));
            instanceNode.setLayoutData(this.initialLayoutData);
            return instanceNode;
        }

        @objid ("f7d4825d-5a3f-11e2-9e33-00137282c51b")
        @Override
        public Object visitBpmnLaneSet(BpmnLaneSet element) {
            final GmBpmnLaneSetContainer instanceNode = new GmBpmnLaneSetContainer(this.diagram,
                                                                                   element,
                                                                                   new MRef(element));
            instanceNode.setLayoutData(this.initialLayoutData);
            return instanceNode;
        }

        @objid ("f7d6e497-5a3f-11e2-9e33-00137282c51b")
        @Override
        public Object visitBpmnLane(BpmnLane element) {
            final GmBpmnLane instanceNode = new GmBpmnLane(this.diagram, element, new MRef(element));
            instanceNode.setLayoutData(this.initialLayoutData);
            return instanceNode;
        }

        @objid ("f7d6e49d-5a3f-11e2-9e33-00137282c51b")
        @Override
        public Object visitBpmnMessage(BpmnMessage element) {
            final GmBpmnMessage instanceNode = new GmBpmnMessage(this.diagram, element, new MRef(element));
            instanceNode.setLayoutData(this.initialLayoutData);
            return instanceNode;
        }

        @objid ("f7d6e4a3-5a3f-11e2-9e33-00137282c51b")
        @Override
        public Object visitBpmnDataInput(BpmnDataInput element) {
            final GmBpmnDataInput instanceNode = new GmBpmnDataInput(this.diagram,
                                                                     element,
                                                                     new MRef(element));
            instanceNode.setLayoutData(this.initialLayoutData);
            return instanceNode;
        }

        @objid ("f7d6e4a9-5a3f-11e2-9e33-00137282c51b")
        @Override
        public Object visitBpmnDataOutput(BpmnDataOutput element) {
            final GmBpmnDataOutput instanceNode = new GmBpmnDataOutput(this.diagram,
                                                                       element,
                                                                       new MRef(element));
            instanceNode.setLayoutData(this.initialLayoutData);
            return instanceNode;
        }

        @objid ("f7d6e4af-5a3f-11e2-9e33-00137282c51b")
        @Override
        public Object visitBpmnDataObject(BpmnDataObject element) {
            final GmBpmnDataObject instanceNode = new GmBpmnDataObject(this.diagram,
                                                                       element,
                                                                       new MRef(element));
            instanceNode.setLayoutData(this.initialLayoutData);
            return instanceNode;
        }

        @objid ("f7d6e4b5-5a3f-11e2-9e33-00137282c51b")
        @Override
        public Object visitBpmnDataStore(BpmnDataStore element) {
            final GmBpmnDataStore instanceNode = new GmBpmnDataStore(this.diagram,
                                                                     element,
                                                                     new MRef(element));
            instanceNode.setLayoutData(this.initialLayoutData);
            return instanceNode;
        }

    }

    /**
     * Factory visitor that creates instances to put into {@link GmGroup}.
     */
    @objid ("6214c22f-55b6-11e2-877f-002564c97630")
    private class GroupElementFactoryVisitor extends DefaultModelVisitor {
        @objid ("7296fb09-55c1-11e2-9337-002564c97630")
        private GmAbstractDiagram diagram;

        @objid ("6214c236-55b6-11e2-877f-002564c97630")
        public GroupElementFactoryVisitor(GmAbstractDiagram diagram) {
            this.diagram = diagram;
        }

        @objid ("62164899-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitElement(Element theElement) {
            // We don't know what to do with that element.
            return null;
        }

    }

}
