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
                                    

package org.modelio.diagram.editor.usecase.elements;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.commands.IParameter;
import org.modelio.diagram.editor.usecase.elements.actor.GmActor;
import org.modelio.diagram.editor.usecase.elements.extensionpoint.GmExtensionPoint;
import org.modelio.diagram.editor.usecase.elements.usecase.GmUseCase;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.group.GmGroup;
import org.modelio.diagram.elements.core.model.IGmNodeFactory;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.metamodel.uml.behavior.usecaseModel.Actor;
import org.modelio.metamodel.uml.behavior.usecaseModel.ExtensionPoint;
import org.modelio.metamodel.uml.behavior.usecaseModel.UseCase;
import org.modelio.metamodel.uml.infrastructure.TagParameter;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.metamodel.visitors.DefaultModelVisitor;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

@objid ("5e8d5386-55b7-11e2-877f-002564c97630")
public final class UseCaseGmNodeFactory implements IGmNodeFactory {
    @objid ("5e8d538a-55b7-11e2-877f-002564c97630")
    private static final UseCaseGmNodeFactory INSTANCE = new UseCaseGmNodeFactory();

    @objid ("5e8d538f-55b7-11e2-877f-002564c97630")
    public static UseCaseGmNodeFactory getInstance() {
        return INSTANCE;
    }

    @objid ("5e8d5394-55b7-11e2-877f-002564c97630")
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

    @objid ("5e8d53a5-55b7-11e2-877f-002564c97630")
    @Override
    public boolean isRepresentable(Class<? extends MObject> metaclass) {
        if (metaclass == TaggedValue.class ||
            metaclass == TagParameter.class ||
            metaclass == IParameter.class)
            return false;
        return true;
    }

    @objid ("5e8d53ad-55b7-11e2-877f-002564c97630")
    @Override
    public void registerFactory(String id, IGmNodeFactory factory) {
        throw new UnsupportedOperationException();
    }

    @objid ("5e8d53b4-55b7-11e2-877f-002564c97630")
    @Override
    public void unregisterFactory(String id) {
        throw new UnsupportedOperationException();
    }

    @objid ("5e8d53b8-55b7-11e2-877f-002564c97630")
    @Override
    public Class<?> getClass(String className) {
        // Get the java class
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            // Not a UseCase class.
        }
        return clazz;
    }

    @objid ("7b8648b1-5eff-11e2-b9cc-001ec947c8cc")
    private UseCaseGmNodeFactory() {
        // Nothing to do
    }

    @objid ("5e8d53c0-55b7-11e2-877f-002564c97630")
    private class NodeFactoryVisitor extends DefaultModelVisitor {
        @objid ("d9db06c9-55c2-11e2-9337-002564c97630")
        private GmAbstractDiagram diagram;

        @objid ("6d89d451-5c07-4718-a899-ae8f302cd490")
        private Object initialLayoutData;

        @objid ("5e8eda1c-55b7-11e2-877f-002564c97630")
        public NodeFactoryVisitor(GmAbstractDiagram diagram, Object initialLayoutData) {
            this.diagram = diagram;
            this.initialLayoutData = initialLayoutData;
        }

        @objid ("5e8eda22-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitActor(Actor theActor) {
            GmActor actor = new GmActor(this.diagram, theActor, new MRef(theActor));
            actor.setLayoutData(this.initialLayoutData);
            return actor;
        }

        @objid ("5e8eda2a-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitUseCase(UseCase theUseCase) {
            GmUseCase useCase = new GmUseCase(this.diagram, theUseCase, new MRef(theUseCase));
            useCase.setLayoutData(this.initialLayoutData);
            return useCase;
        }

    }

    @objid ("5e8eda32-55b7-11e2-877f-002564c97630")
    private class GmLabelFactoryVisitor extends DefaultModelVisitor {
        @objid ("d9db06ca-55c2-11e2-9337-002564c97630")
        private GmAbstractDiagram diagram;

        @objid ("25f3879b-f00e-4bb0-8998-c227570d2aef")
        private Object initialLayoutData;

        @objid ("5e8eda3a-55b7-11e2-877f-002564c97630")
        public GmLabelFactoryVisitor(GmAbstractDiagram diagram, Object initialLayoutData) {
            this.diagram = diagram;
            this.initialLayoutData = initialLayoutData;
        }

        @objid ("5e8eda40-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitExtensionPoint(ExtensionPoint theExtensionPoint) {
            GmExtensionPoint extensionPoint = new GmExtensionPoint(this.diagram,
                                                                   theExtensionPoint,
                                                                   new MRef(theExtensionPoint));
            extensionPoint.setLayoutData(this.initialLayoutData);
            return extensionPoint;
        }

    }

}
