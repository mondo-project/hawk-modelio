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
                                    

package org.modelio.diagram.editor.communication.elements;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.communication.elements.communicationdiagramview.GmCommunicationDiagramView;
import org.modelio.diagram.editor.communication.elements.communicationmessage.GmCommunicationMessageLabel;
import org.modelio.diagram.editor.communication.elements.communicationnode.GmCommunicationNode;
import org.modelio.diagram.editor.statik.elements.namespacelabel.GmNameSpaceLabel;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.group.GmGroup;
import org.modelio.diagram.elements.core.model.IGmNodeFactory;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.metamodel.diagrams.CommunicationDiagram;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationMessage;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationNode;
import org.modelio.metamodel.uml.infrastructure.TagParameter;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.metamodel.uml.statik.Artifact;
import org.modelio.metamodel.uml.statik.Parameter;
import org.modelio.metamodel.visitors.DefaultModelVisitor;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * The Communication GmNode factory for Modelio diagrams.
 * <p>
 * Implementation of {@link IGmNodeFactory}.
 */
@objid ("7a313e7b-55b6-11e2-877f-002564c97630")
public final class CommunicationDiagramGmNodeFactory implements IGmNodeFactory {
    @objid ("7a313e7f-55b6-11e2-877f-002564c97630")
    private static final CommunicationDiagramGmNodeFactory INSTANCE = new CommunicationDiagramGmNodeFactory();

    /**
     * Private default constructor
     */
    @objid ("7a313e81-55b6-11e2-877f-002564c97630")
    private CommunicationDiagramGmNodeFactory() {
        // Nothing to do
    }

    /**
     * @return the singleton instance of the node factory for Communication diagram.
     */
    @objid ("7a32c4da-55b6-11e2-877f-002564c97630")
    public static CommunicationDiagramGmNodeFactory getInstance() {
        return INSTANCE;
    }

    @objid ("7a32c4df-55b6-11e2-877f-002564c97630")
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

    @objid ("7a32c4f0-55b6-11e2-877f-002564c97630")
    @Override
    public boolean isRepresentable(Class<? extends MObject> metaclass) {
        if (metaclass == TaggedValue.class ||
                metaclass == TagParameter.class ||
                metaclass == Parameter.class)
            return false;
        return true;
    }

    @objid ("7a32c4f8-55b6-11e2-877f-002564c97630")
    @Override
    public void registerFactory(String id, IGmNodeFactory factory) {
        throw new UnsupportedOperationException();
    }

    @objid ("7a32c4ff-55b6-11e2-877f-002564c97630")
    @Override
    public void unregisterFactory(String id) {
        throw new UnsupportedOperationException();
    }

    @objid ("7a32c503-55b6-11e2-877f-002564c97630")
    @Override
    public Class<?> getClass(String className) {
        // Get the java class
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            // Not a Communication class.
        }
        return clazz;
    }

    /**
     * Factory visitor that creates standard GmNodes.
     */
    @objid ("7a32c50b-55b6-11e2-877f-002564c97630")
    private class NodeFactoryVisitor extends DefaultModelVisitor {
        @objid ("7a32c50f-55b6-11e2-877f-002564c97630")
        private Object initialLayoutData;

        @objid ("9c9b0db9-55c1-11e2-9337-002564c97630")
        private GmAbstractDiagram diagram;

        @objid ("7a32c513-55b6-11e2-877f-002564c97630")
        public NodeFactoryVisitor(GmAbstractDiagram diagram, Object initialLayoutData) {
            this.diagram = diagram;
            this.initialLayoutData = initialLayoutData;
        }

        @objid ("06a713e8-59a5-11e2-80d8-00137282c51b")
        @Override
        public Object visitCommunicationNode(CommunicationNode theCommunicationNode) {
            GmNodeModel node = new GmCommunicationNode(this.diagram,
                    theCommunicationNode,
                    new MRef(theCommunicationNode));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("06a713ee-59a5-11e2-80d8-00137282c51b")
        @Override
        public Object visitCommunicationDiagram(final CommunicationDiagram el) {
            GmNodeModel node = new GmCommunicationDiagramView(this.diagram, el, new MRef(el));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

    }

    /**
     * Factory visitor that creates only instances of GmLabels or derived classes.
     * 
     * @author chm
     */
    @objid ("7a344b81-55b6-11e2-877f-002564c97630")
    private class GmLabelFactoryVisitor extends DefaultModelVisitor {
        @objid ("7a344b85-55b6-11e2-877f-002564c97630")
        private Object initialLayoutData;

        @objid ("9c9ba9f9-55c1-11e2-9337-002564c97630")
        private GmAbstractDiagram diagram;

        @objid ("7a344b89-55b6-11e2-877f-002564c97630")
        public GmLabelFactoryVisitor(GmAbstractDiagram diagram, Object initialLayoutData) {
            this.diagram = diagram;
            this.initialLayoutData = initialLayoutData;
        }

        @objid ("8f8aa6e8-f15f-457e-8d05-6e632d771537")
        @Override
        public Object visitArtifact(Artifact theArtifact) {
            final GmNameSpaceLabel node = new GmNameSpaceLabel(this.diagram, theArtifact, new MRef(theArtifact));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

        @objid ("191a3277-5a8c-4e07-a450-cfa2bbdeee1b")
        @Override
        public Object visitCommunicationMessage(final CommunicationMessage theCommunicationMessage) {
            final GmCommunicationMessageLabel node = new GmCommunicationMessageLabel(this.diagram, theCommunicationMessage, new MRef(theCommunicationMessage));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

    }

}
