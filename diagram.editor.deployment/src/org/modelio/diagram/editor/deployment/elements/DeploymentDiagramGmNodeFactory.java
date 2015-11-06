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
                                    

package org.modelio.diagram.editor.deployment.elements;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.deployment.elements.artifact.GmArtifact;
import org.modelio.diagram.editor.deployment.elements.node.GmNode;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.group.GmGroup;
import org.modelio.diagram.elements.core.model.IGmNodeFactory;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.metamodel.uml.infrastructure.TagParameter;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.metamodel.uml.statik.Artifact;
import org.modelio.metamodel.uml.statik.Node;
import org.modelio.metamodel.uml.statik.Parameter;
import org.modelio.metamodel.visitors.DefaultModelVisitor;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/*Modelio diagrams.
 * <p>
 * Implementation of {@link IGmNodeFactory}.
 */
@objid ("972cad3a-55b6-11e2-877f-002564c97630")
public final class DeploymentDiagramGmNodeFactory implements IGmNodeFactory {
    @objid ("972cad3e-55b6-11e2-877f-002564c97630")
    private static final DeploymentDiagramGmNodeFactory INSTANCE = new DeploymentDiagramGmNodeFactory();

    /**
     * Private default constructor
     */
    @objid ("972cad40-55b6-11e2-877f-002564c97630")
    private DeploymentDiagramGmNodeFactory() {
        // Nothing to do
    }

    /**
     * @return the singleton instance of the node factory for Deployment diagram.
     */
    @objid ("972cad43-55b6-11e2-877f-002564c97630")
    public static DeploymentDiagramGmNodeFactory getInstance() {
        return INSTANCE;
    }

    @objid ("972cad48-55b6-11e2-877f-002564c97630")
    @Override
    public GmNodeModel create(GmAbstractDiagram diagram, GmCompositeNode parent, MObject newElement, Object initialLayoutData) {
        if (parent instanceof GmGroup) {
            // Use the label factory visitor
            final GmLabelFactoryVisitor v = new GmLabelFactoryVisitor(diagram, initialLayoutData);
        
            final GmNodeModel child = (GmNodeModel) newElement.accept(v);
            if (child != null)
                parent.addChild(child);
            return child;
        }
        // else Use the node factory visitor
        final NodeFactoryVisitor v = new NodeFactoryVisitor(diagram, initialLayoutData);
        
        final GmNodeModel child = (GmNodeModel) newElement.accept(v);
        if (child != null)
            parent.addChild(child);
        return child;
    }

    @objid ("972cad59-55b6-11e2-877f-002564c97630")
    @Override
    public boolean isRepresentable(Class<? extends MObject> metaclass) {
        if (metaclass == TaggedValue.class ||
                metaclass == TagParameter.class ||
                metaclass == Parameter.class)
            return false;
        return true;
    }

    @objid ("972cad61-55b6-11e2-877f-002564c97630")
    @Override
    public void registerFactory(String id, IGmNodeFactory factory) {
        throw new UnsupportedOperationException();
    }

    @objid ("972cad68-55b6-11e2-877f-002564c97630")
    @Override
    public void unregisterFactory(String id) {
        throw new UnsupportedOperationException();
    }

    @objid ("972cad6c-55b6-11e2-877f-002564c97630")
    @Override
    public Class<?> getClass(String className) {
        // Get the java class
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            // Not a Deployment class.
        }
        return clazz;
    }

    /**
     * Factory visitor that creates standard GmNodes.
     */
    @objid ("972cad74-55b6-11e2-877f-002564c97630")
    private class NodeFactoryVisitor extends DefaultModelVisitor {
        @objid ("972cad78-55b6-11e2-877f-002564c97630")
        private Object initialLayoutData;

        @objid ("1cc543cb-55c2-11e2-9337-002564c97630")
        private GmAbstractDiagram diagram;

        @objid ("972cad7c-55b6-11e2-877f-002564c97630")
        public NodeFactoryVisitor(GmAbstractDiagram diagram, Object initialLayoutData) {
            this.diagram = diagram;
            this.initialLayoutData = initialLayoutData;
        }

        @objid ("972e33d9-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitArtifact(Artifact theArtifact) {
            GmArtifact actor = new GmArtifact(this.diagram, theArtifact, new MRef(theArtifact));
            actor.setLayoutData(this.initialLayoutData);
            return actor;
        }

        @objid ("972e33e1-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitNode(Node theNode) {
            GmNode node = new GmNode(this.diagram, theNode, new MRef(theNode));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

    }

    /**
     * Factory visitor that creates only instances of GmLabels or derived classes.
     * 
     * @author chm
     */
    @objid ("972e33e9-55b6-11e2-877f-002564c97630")
    private class GmLabelFactoryVisitor extends DefaultModelVisitor {
        @objid ("972e33ed-55b6-11e2-877f-002564c97630")
        private Object initialLayoutData;

        @objid ("1cc6ca69-55c2-11e2-9337-002564c97630")
        private GmAbstractDiagram diagram;

        @objid ("972e33f1-55b6-11e2-877f-002564c97630")
        public GmLabelFactoryVisitor(GmAbstractDiagram diagram, Object initialLayoutData) {
            this.diagram = diagram;
            this.initialLayoutData = initialLayoutData;
        }


/*
        @objid ("972e33f7-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitArtifact(Artifact theArtifact) {
            final GmNameSpaceLabel node = new GmNameSpaceLabel(this.diagram,
                    theArtifact,
                    new MRef(theArtifact));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("972e33ff-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitNode(Node theNode) {
            final GmNameSpaceLabel node = new GmNameSpaceLabel(this.diagram, theNode, new MRef(theNode));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }
         */
    }

}
