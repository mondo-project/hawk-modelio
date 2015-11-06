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
                                    

package org.modelio.diagram.editor.sequence.elements;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.sequence.elements.combinedfragment.GmCombinedFragment;
import org.modelio.diagram.editor.sequence.elements.executionoccurencespecification.GmExecutionOccurenceSpecification;
import org.modelio.diagram.editor.sequence.elements.executionspecification.GmExecutionSpecification;
import org.modelio.diagram.editor.sequence.elements.gate.GmGate;
import org.modelio.diagram.editor.sequence.elements.interactionoperand.GmInteractionOperand;
import org.modelio.diagram.editor.sequence.elements.interactionuse.GmInteractionUse;
import org.modelio.diagram.editor.sequence.elements.interactionuse.gate.GmGateOnInteractionUse;
import org.modelio.diagram.editor.sequence.elements.lifeline.GmLifeline;
import org.modelio.diagram.editor.sequence.elements.sequencediagramview.GmSequenceDiagramView;
import org.modelio.diagram.editor.sequence.elements.stateinvariant.GmStateInvariant;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.group.GmGroup;
import org.modelio.diagram.elements.core.model.IGmNodeFactory;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.metamodel.diagrams.SequenceDiagram;
import org.modelio.metamodel.uml.behavior.interactionModel.CombinedFragment;
import org.modelio.metamodel.uml.behavior.interactionModel.ExecutionOccurenceSpecification;
import org.modelio.metamodel.uml.behavior.interactionModel.ExecutionSpecification;
import org.modelio.metamodel.uml.behavior.interactionModel.Gate;
import org.modelio.metamodel.uml.behavior.interactionModel.InteractionOperand;
import org.modelio.metamodel.uml.behavior.interactionModel.InteractionUse;
import org.modelio.metamodel.uml.behavior.interactionModel.Lifeline;
import org.modelio.metamodel.uml.behavior.interactionModel.StateInvariant;
import org.modelio.metamodel.uml.infrastructure.TagParameter;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.metamodel.uml.statik.Parameter;
import org.modelio.metamodel.visitors.DefaultModelVisitor;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * The GmNode factory for Sequence diagrams.
 * <p>
 * Implementation of {@link IGmNodeFactory}.
 */
@objid ("d9884974-55b6-11e2-877f-002564c97630")
public final class SequenceDiagramGmNodeFactory implements IGmNodeFactory {
    @objid ("d9884978-55b6-11e2-877f-002564c97630")
    private static final SequenceDiagramGmNodeFactory INSTANCE = new SequenceDiagramGmNodeFactory();

    @objid ("d988497a-55b6-11e2-877f-002564c97630")
    @Override
    public GmNodeModel create(GmAbstractDiagram diagram, GmCompositeNode parent, MObject newElement, Object initialLayoutData) {
        if (parent instanceof GmGroup) {
            return null;
        }
        // else Use the node factory visitor
        final NodeFactoryVisitor v = new NodeFactoryVisitor(diagram, initialLayoutData);
        
        final GmNodeModel child = (GmNodeModel) newElement.accept(v);
        if (child != null)
            parent.addChild(child);
        return child;
    }

    @objid ("d988498b-55b6-11e2-877f-002564c97630")
    @Override
    public Class<?> getClass(String className) {
        // Get the java class
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            // Not a Sequence class.
        }
        return clazz;
    }

    /**
     * @return the singleton instance of the node factory for Sequence diagram.
     */
    @objid ("d9884993-55b6-11e2-877f-002564c97630")
    public static SequenceDiagramGmNodeFactory getInstance() {
        return INSTANCE;
    }

    @objid ("d9884998-55b6-11e2-877f-002564c97630")
    @Override
    public boolean isRepresentable(Class<? extends MObject> metaclass) {
        if (metaclass == TaggedValue.class ||
            metaclass == TagParameter.class ||
            metaclass == Parameter.class)
            return false;
        return true;
    }

    @objid ("d98849a0-55b6-11e2-877f-002564c97630")
    @Override
    public void registerFactory(String id, IGmNodeFactory factory) {
        throw new UnsupportedOperationException();
    }

    @objid ("d989cffa-55b6-11e2-877f-002564c97630")
    @Override
    public void unregisterFactory(String id) {
        throw new UnsupportedOperationException();
    }

    /**
     * Private default constructor
     */
    @objid ("d989cffe-55b6-11e2-877f-002564c97630")
    private SequenceDiagramGmNodeFactory() {
        // Nothing to do
    }

    /**
     * Factory visitor that creates standard GmNodes.
     */
    @objid ("d989d001-55b6-11e2-877f-002564c97630")
    private class NodeFactoryVisitor extends DefaultModelVisitor {
        @objid ("4fe556ab-55c2-11e2-9337-002564c97630")
        private GmAbstractDiagram diagram;

        @objid ("7063a87e-21c8-41c4-aeff-03c9cd315b64")
        private Object initialLayoutData;

        @objid ("d989d009-55b6-11e2-877f-002564c97630")
        public NodeFactoryVisitor(GmAbstractDiagram diagram, Object initialLayoutData) {
            this.diagram = diagram;
            this.initialLayoutData = initialLayoutData;
        }

        @objid ("d989d00f-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitExecutionSpecification(ExecutionSpecification theExecutionSpecification) {
            final GmExecutionSpecification node = new GmExecutionSpecification(this.diagram,
                                                                               theExecutionSpecification,
                                                                               new MRef(theExecutionSpecification));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("d989d017-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitLifeline(Lifeline theLifeline) {
            final GmLifeline node = new GmLifeline(this.diagram, theLifeline, new MRef(theLifeline));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("d989d01f-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitSequenceDiagram(final SequenceDiagram el) {
            final GmNodeModel node = new GmSequenceDiagramView(this.diagram, el, new MRef(el));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("d989d028-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitExecutionOccurenceSpecification(final ExecutionOccurenceSpecification theExecutionOccurenceSpecification) {
            final GmExecutionOccurenceSpecification node = new GmExecutionOccurenceSpecification(this.diagram,
                                                                                                 theExecutionOccurenceSpecification,
                                                                                                 new MRef(theExecutionOccurenceSpecification));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("d989d031-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitGate(final Gate theGate) {
            if (theGate.getOwnerUse() != null) {
                final GmGateOnInteractionUse node = new GmGateOnInteractionUse(this.diagram,
                                                                               theGate,
                                                                               new MRef(theGate));
                node.setLayoutData(this.initialLayoutData);
                return node;
            } else {
                final GmGate node = new GmGate(this.diagram, theGate, new MRef(theGate));
                node.setLayoutData(this.initialLayoutData);
                return node;
            }
        }

        @objid ("d989d039-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitInteractionUse(final InteractionUse theInteractionUse) {
            final GmInteractionUse node = new GmInteractionUse(this.diagram,
                                                               theInteractionUse,
                                                               new MRef(theInteractionUse));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("d98b569c-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitCombinedFragment(final CombinedFragment theCombinedFragment) {
            final GmCombinedFragment node = new GmCombinedFragment(this.diagram,
                                                                   theCombinedFragment,
                                                                   new MRef(theCombinedFragment));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("d98b56a5-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitInteractionOperand(final InteractionOperand theInteractionOperand) {
            final GmInteractionOperand node = new GmInteractionOperand(this.diagram,
                                                                       theInteractionOperand,
                                                                       new MRef(theInteractionOperand));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("d98b56ae-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitStateInvariant(final StateInvariant theStateInvariant) {
            final GmStateInvariant node = new GmStateInvariant(this.diagram,
                                                               theStateInvariant,
                                                               new MRef(theStateInvariant));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

    }

}
