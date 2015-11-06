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
                                    

package org.modelio.diagram.editor.state.elements.gmfactory;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.commands.IParameter;
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
import org.modelio.diagram.editor.state.elements.statediagramview.GmStateDiagramView;
import org.modelio.diagram.editor.state.elements.terminal.GmTerminal;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.group.GmGroup;
import org.modelio.diagram.elements.core.model.IGmNodeFactory;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.metamodel.diagrams.StateMachineDiagram;
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
import org.modelio.metamodel.uml.behavior.stateMachineModel.TerminatePseudoState;
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
@objid ("f53c4461-55b6-11e2-877f-002564c97630")
public class StateGmNodeFactory implements IGmNodeFactory {
    @objid ("f53c4465-55b6-11e2-877f-002564c97630")
    private static final StateGmNodeFactory _instance = new StateGmNodeFactory();

    @objid ("f53c4467-55b6-11e2-877f-002564c97630")
    @Override
    public GmNodeModel create(GmAbstractDiagram diagram, GmCompositeNode parent, MObject newElement, Object initialLayoutData) {
        if (parent instanceof GmGroup) {
            // Use the group element factory visitor
            final GroupElementFactoryVisitor v = new GroupElementFactoryVisitor(diagram, initialLayoutData);
        
            final GmNodeModel child = (GmNodeModel) newElement.accept(v);
            if (child != null)
                parent.addChild(child);
            return child;
        } else {
            // Use the node factory visitor
            final NodeFactoryVisitor v = new NodeFactoryVisitor(diagram, initialLayoutData);
        
            final GmNodeModel child = (GmNodeModel) newElement.accept(v);
            if (child != null)
                parent.addChild(child);
            return child;
        }
    }

    @objid ("f53c4477-55b6-11e2-877f-002564c97630")
    @Override
    public Class<?> getClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * Get the singleton instance of the node factory.
     * @return The graphic node factory.
     */
    @objid ("f53c447e-55b6-11e2-877f-002564c97630")
    public static IGmNodeFactory getInstance() {
        return _instance;
    }

    @objid ("f53c4485-55b6-11e2-877f-002564c97630")
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
    @objid ("f53dcadb-55b6-11e2-877f-002564c97630")
    @Override
    public void registerFactory(String id, IGmNodeFactory factory) {
        throw new UnsupportedOperationException();
    }

    /**
     * Remove a registered extension factory.
     * @param id The id used to register the extension factory.
     */
    @objid ("f53dcae2-55b6-11e2-877f-002564c97630")
    @Override
    public void unregisterFactory(String id) {
        throw new UnsupportedOperationException();
    }

    /**
     * Constructor.
     * @param gmDiagram
     * The diagram all nodes created by this factory will be unmasked in.
     */
    @objid ("f53dcae6-55b6-11e2-877f-002564c97630")
    private StateGmNodeFactory() {
        // Nothing to do.
    }

    /**
     * Factory visitor that creates standard GmNodes.
     */
    @objid ("f53dcae9-55b6-11e2-877f-002564c97630")
    private class NodeFactoryVisitor extends DefaultModelVisitor {
        @objid ("f53dcaf0-55b6-11e2-877f-002564c97630")
        private Object initialLayoutData;

        @objid ("81661169-55c2-11e2-9337-002564c97630")
        private GmAbstractDiagram diagram;

        @objid ("f53dcaf1-55b6-11e2-877f-002564c97630")
        public NodeFactoryVisitor(GmAbstractDiagram diagram, Object initialLayoutData) {
            this.diagram = diagram;
            this.initialLayoutData = initialLayoutData;
        }

        @objid ("f53dcaf7-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitChoicePseudoState(ChoicePseudoState theChoicePseudoState) {
            final GmChoice choice = new GmChoice(this.diagram,
                    theChoicePseudoState,
                    new MRef(theChoicePseudoState));
            choice.setLayoutData(this.initialLayoutData);
            return choice;
        }

        @objid ("f53dcaff-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitConnectionPointReference(ConnectionPointReference theConnectionPointReference) {
            final GmConnectionPoint connectionPoint = new GmConnectionPoint(this.diagram,
                    theConnectionPointReference,
                    new MRef(theConnectionPointReference));
            connectionPoint.setLayoutData(this.initialLayoutData);
            return connectionPoint;
        }

        @objid ("f53dcb07-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitDeepHistoryPseudoState(DeepHistoryPseudoState theDeepHistoryPseudoState) {
            final GmDeepHistory deepHistory = new GmDeepHistory(this.diagram,
                    theDeepHistoryPseudoState,
                    new MRef(theDeepHistoryPseudoState));
            deepHistory.setLayoutData(this.initialLayoutData);
            return deepHistory;
        }

        @objid ("f53dcb0f-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitElement(Element theElement) {
            // We don't know what to do with that element.
            return null;
        }

        @objid ("f53dcb17-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitEntryPointPseudoState(EntryPointPseudoState theEntryPointPseudoState) {
            final GmEntry entry = new GmEntry(this.diagram,
                    theEntryPointPseudoState,
                    new MRef(theEntryPointPseudoState));
            entry.setLayoutData(this.initialLayoutData);
            return entry;
        }

        @objid ("f53f517b-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitExitPointPseudoState(ExitPointPseudoState theExitPointPseudoState) {
            final GmExitPoint exit = new GmExitPoint(this.diagram,
                    theExitPointPseudoState,
                    new MRef(theExitPointPseudoState));
            exit.setLayoutData(this.initialLayoutData);
            return exit;
        }

        @objid ("f53f5183-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitFinalState(FinalState theFinalState) {
            final GmFinalState finalState = new GmFinalState(this.diagram,
                    theFinalState,
                    new MRef(theFinalState));
            finalState.setLayoutData(this.initialLayoutData);
            return finalState;
        }

        @objid ("f53f518b-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitForkPseudoState(ForkPseudoState theForkPseudoState) {
            final GmForkState fork = new GmForkState(this.diagram,
                    theForkPseudoState,
                    new MRef(theForkPseudoState));
            fork.setLayoutData(this.initialLayoutData);
            return fork;
        }

        @objid ("f53f5193-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitInitialPseudoState(InitialPseudoState theInitialPseudoState) {
            final GmInitialState initial = new GmInitialState(this.diagram,
                    theInitialPseudoState,
                    new MRef(theInitialPseudoState));
            initial.setLayoutData(this.initialLayoutData);
            return initial;
        }

        @objid ("f53f519b-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitJoinPseudoState(JoinPseudoState theJoinPseudoState) {
            final GmJoin join = new GmJoin(this.diagram, theJoinPseudoState, new MRef(theJoinPseudoState));
            join.setLayoutData(this.initialLayoutData);
            return join;
        }

        @objid ("f53f51a3-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitJunctionPseudoState(JunctionPseudoState theJunctionPseudoState) {
            final GmJunction junction = new GmJunction(this.diagram,
                    theJunctionPseudoState,
                    new MRef(theJunctionPseudoState));
            junction.setLayoutData(this.initialLayoutData);
            return junction;
        }

        @objid ("f53f51ab-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitRegion(Region theRegion) {
            final GmRegion region = new GmRegion(this.diagram, theRegion, new MRef(theRegion));
            region.setLayoutData(this.initialLayoutData);
            return region;
        }

        @objid ("f53f51b3-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitShallowHistoryPseudoState(ShallowHistoryPseudoState theShallowHistoryPseudoState) {
            final GmShallowHistory shallowHistory = new GmShallowHistory(this.diagram,
                    theShallowHistoryPseudoState,
                    new MRef(theShallowHistoryPseudoState));
            shallowHistory.setLayoutData(this.initialLayoutData);
            return shallowHistory;
        }

        @objid ("f53f51bb-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitState(State theState) {
            final GmState state = new GmState(this.diagram, theState, new MRef(theState));
            state.setLayoutData(this.initialLayoutData);
            return state;
        }

        @objid ("f540d81d-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitTerminatePseudoState(TerminatePseudoState theTerminatePseudoState) {
            final GmTerminal terminal = new GmTerminal(this.diagram,
                    theTerminatePseudoState,
                    new MRef(theTerminatePseudoState));
            terminal.setLayoutData(this.initialLayoutData);
            return terminal;
        }

        @objid ("f540d825-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitStateMachineDiagram(final StateMachineDiagram theStateMachineDiagram) {
            GmStateDiagramView ret = new GmStateDiagramView(this.diagram,
                    theStateMachineDiagram,
                    new MRef(theStateMachineDiagram));
            ret.setLayoutData(this.initialLayoutData);
            return ret;
        }

    }

    /**
     * Factory visitor that creates instances to put into {@link GmGroup}.
     * 
     * @author cmarin
     */
    @objid ("f540d82e-55b6-11e2-877f-002564c97630")
    private class GroupElementFactoryVisitor extends DefaultModelVisitor {
        @objid ("f540d835-55b6-11e2-877f-002564c97630")
        private Object initialLayoutData;

        @objid ("816945b9-55c2-11e2-9337-002564c97630")
        private GmAbstractDiagram diagram;

        @objid ("f540d836-55b6-11e2-877f-002564c97630")
        public GroupElementFactoryVisitor(GmAbstractDiagram diagram, Object initialLayoutData) {
            this.diagram = diagram;
            this.initialLayoutData = initialLayoutData;
        }

        @objid ("f540d83c-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitElement(Element theElement) {
            // We don't know what to do with that element.
            return null;
        }

        @objid ("198972d2-0438-4c2c-b56a-c5ca14908819")
        @Override
        public Object visitInternalTransition(InternalTransition theInternalTransition) {
            final GmInternalTransition region = new GmInternalTransition(this.diagram,
                    theInternalTransition,
                    new MRef(theInternalTransition));
            region.setLayoutData(this.initialLayoutData);
            return region;
        }

        @objid ("494d5692-df32-4fcb-a79e-fa2368e131cd")
        @Override
        public Object visitRegion(Region theRegion) {
            final GmRegion node = new GmRegion(this.diagram, theRegion, new MRef(theRegion));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

    }

}
