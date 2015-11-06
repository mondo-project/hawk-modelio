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
                                    

package org.modelio.diagram.elements.gmfactory;

import java.util.HashMap;
import java.util.Map;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.group.GmGroup;
import org.modelio.diagram.elements.core.model.IGmNodeFactory;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.elements.umlcommon.constraint.GmConstraintBody;
import org.modelio.diagram.elements.umlcommon.diagramholder.GmDiagramHolder;
import org.modelio.diagram.elements.umlcommon.externdocument.GmExternDocument;
import org.modelio.diagram.elements.umlcommon.informationflowgroup.GmInformationFlowLabel;
import org.modelio.diagram.elements.umlcommon.note.GmNote;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.uml.informationFlow.InformationFlow;
import org.modelio.metamodel.uml.infrastructure.Constraint;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.ExternDocument;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.metamodel.uml.infrastructure.TagParameter;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.metamodel.uml.statik.Parameter;
import org.modelio.metamodel.visitors.DefaultModelVisitor;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Implementation of {@link IGmNodeFactory}.
 * <p>
 * This factory is dynamically enriched by diagram plugins, so that it ends by being able to process the complete UML metamodel.<br>
 * The GmNodeFactory actually delegates the 'create' request to its cascaded factories. No ordering can be assumed in the delegation
 * mechanism.
 */
@objid ("81033de7-1dec-11e2-8cad-001ec947c8cc")
public class GmNodeFactory implements IGmNodeFactory {
    /**
     * Cascaded factories catalog(if any).
     */
    @objid ("81033de9-1dec-11e2-8cad-001ec947c8cc")
    private Map<String, IGmNodeFactory> cascadedFactories = new HashMap<>();

    @objid ("81033dee-1dec-11e2-8cad-001ec947c8cc")
    private static final GmNodeFactory instance = new GmNodeFactory();

    /**
     * Get the singleton instance of the node factory.
     * @return The graphic node factory.
     */
    @objid ("81033df0-1dec-11e2-8cad-001ec947c8cc")
    public static IGmNodeFactory getInstance() {
        return instance;
    }

    /**
     * Constructor.
     * @param gmDiagram
     * The diagram all nodes created by this factory will be unmasked in.
     */
    @objid ("8105a011-1dec-11e2-8cad-001ec947c8cc")
    private GmNodeFactory() {
        // Nothing to do.
    }

    @objid ("8105a014-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public GmNodeModel create(GmAbstractDiagram diagram, GmCompositeNode parent, MObject newElement, Object initialLayoutData) {
        // First ask to the cascaded factories
        
        for (IGmNodeFactory factory : this.cascadedFactories.values()) {
            final GmNodeModel nodeModel = factory.create(diagram, parent, newElement, initialLayoutData);
            if (nodeModel != null)
                return nodeModel;
        }
        
        // Cascaded factories did not produce anything...
        if (parent instanceof GmGroup) {
            // Use the group element factory visitor
            final GroupElementFactoryVisitor v = new GroupElementFactoryVisitor(diagram, initialLayoutData);
        
            final GmNodeModel child = (GmNodeModel) newElement.accept(v);
            parent.addChild(child);
            return child;
        } else {
            // Use the node factory visitor
            final NodeFactoryVisitor v = new NodeFactoryVisitor(diagram, parent, initialLayoutData);
        
            final GmNodeModel child = (GmNodeModel) newElement.accept(v);
            parent.addChild(child);
            return child;
        }
    }

    @objid ("8105a01c-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Class<?> getClass(String className) {
        Class<?> ret = null;
        for (IGmNodeFactory f : this.cascadedFactories.values()) {
            ret = f.getClass(className);
            if (ret != null)
                return ret;
        }
        return null;
    }

    @objid ("8105a024-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public boolean isRepresentable(Class<? extends MObject> metaclass) {
        if (metaclass == TaggedValue.class || metaclass == TagParameter.class || metaclass == Parameter.class)
        
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
    @objid ("8105a02c-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void registerFactory(String id, IGmNodeFactory factory) {
        this.cascadedFactories.put(id, factory);
    }

    /**
     * Remove a registered extension factory.
     * @param id The id used to register the extension factory.
     */
    @objid ("8105a032-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void unregisterFactory(String id) {
        this.cascadedFactories.remove(id);
    }

    /**
     * Factory visitor that creates standard GmNodes.
     */
    @objid ("8105a037-1dec-11e2-8cad-001ec947c8cc")
    private class NodeFactoryVisitor extends DefaultModelVisitor {
        @objid ("8105a039-1dec-11e2-8cad-001ec947c8cc")
        private Object initialLayoutData;

        @objid ("8105a03a-1dec-11e2-8cad-001ec947c8cc")
        private GmCompositeNode parent;

        @objid ("8105a03b-1dec-11e2-8cad-001ec947c8cc")
        private GmAbstractDiagram diagram;

        /**
         * Creates the visitor.
         * @param diagram The diagram
         * @param parent The parent node
         * @param initialLayoutData The initial layout data.
         */
        @objid ("8105a03c-1dec-11e2-8cad-001ec947c8cc")
        public NodeFactoryVisitor(GmAbstractDiagram diagram, GmCompositeNode parent, Object initialLayoutData) {
            this.diagram = diagram;
            this.parent = parent;
            this.initialLayoutData = initialLayoutData;
        }

        @objid ("8105a042-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Object visitElement(Element theElement) {
            throw new UnsupportedOperationException(theElement.toString() + " not yet unmaskable.");
        }

        @objid ("8105a047-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Object visitNote(Note theNote) {
            final GmNote node = new GmNote(this.diagram, theNote, new MRef(theNote));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("8105a04d-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Object visitConstraint(final Constraint theConstraint) {
            final GmNodeModel node = new GmConstraintBody(this.diagram, theConstraint, new MRef(theConstraint));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("8105a054-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Object visitDependency(final Dependency dep) {
            if (dep.isStereotyped("ModelerModule", "related_diagram")) {
                final ModelElement target = dep.getDependsOn();
                // final ModelElement src = dep.getImpacted();
                if (target == null || target instanceof AbstractDiagram) {
                    // It is a related diagram link
                    GmDiagramHolder node = new GmDiagramHolder(this.diagram, dep, new MRef(dep));
                    node.setLayoutData(this.initialLayoutData);
            
                    // unmask the represented diagram.
                    if (target != null)
                        this.diagram.unmask(node, target, null);
            
                    return node;
                }
            }
            
            // Not handled
            return super.visitDependency(dep);
        }

        @objid ("8105a05b-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Object visitExternDocument(final ExternDocument theExternDocument) {
            final GmExternDocument node = new GmExternDocument(this.diagram, theExternDocument, new MRef(theExternDocument));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

    }

    /**
     * Factory visitor that creates instances to put into GmGroup.
     * 
     * @author cmarin
     */
    @objid ("8105a062-1dec-11e2-8cad-001ec947c8cc")
    private class GroupElementFactoryVisitor extends DefaultModelVisitor {
        @objid ("8105a064-1dec-11e2-8cad-001ec947c8cc")
        private Object initialLayoutData;

        @objid ("8105a065-1dec-11e2-8cad-001ec947c8cc")
        private GmAbstractDiagram diagram;

        @objid ("8105a066-1dec-11e2-8cad-001ec947c8cc")
        public GroupElementFactoryVisitor(GmAbstractDiagram diagram, Object initialLayoutData) {
            this.diagram = diagram;
            this.initialLayoutData = initialLayoutData;
        }

        @objid ("8105a06a-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Object visitElement(Element theElement) {
            throw new UnsupportedOperationException(theElement.toString() + " not unmaskable as a label.");
        }

        @objid ("8105a06f-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Object visitInformationFlow(final InformationFlow theInformationFlow) {
            GmInformationFlowLabel node = new GmInformationFlowLabel(this.diagram, theInformationFlow, new MRef(theInformationFlow));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

    }

}
