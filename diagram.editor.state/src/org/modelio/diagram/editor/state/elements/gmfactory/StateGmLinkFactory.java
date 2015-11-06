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
import org.modelio.diagram.editor.state.elements.transition.GmTransition;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.link.GmLink;
import org.modelio.diagram.elements.core.model.IGmLinkFactory;
import org.modelio.metamodel.uml.behavior.stateMachineModel.InternalTransition;
import org.modelio.metamodel.uml.behavior.stateMachineModel.Transition;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.visitors.DefaultModelVisitor;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Implementation of {@link IGmLinkFactory}.
 */
@objid ("f53abd9a-55b6-11e2-877f-002564c97630")
public class StateGmLinkFactory implements IGmLinkFactory {
    @objid ("f53abd9e-55b6-11e2-877f-002564c97630")
    private static final StateGmLinkFactory _instance = new StateGmLinkFactory();

    /**
     * Creates the link factory for a diagram.
     * @param diagram
     * the diagram the factory will be attached to.
     */
    @objid ("f53abda0-55b6-11e2-877f-002564c97630")
    private StateGmLinkFactory() {
    }

    @objid ("f53abda3-55b6-11e2-877f-002564c97630")
    @Override
    public GmLink create(GmAbstractDiagram diagram, MObject linkElement) {
        return (GmLink) linkElement.accept(new ImplVisitor(diagram));
    }

    /**
     * Register an GmLink factory extension.
     * <p>
     * Extension GmLink factories are called first when looking for an GmLink.
     * @param id id for the GmLink extension factory
     * @param factory the edit part factory.
     */
    @objid ("f53abdb0-55b6-11e2-877f-002564c97630")
    @Override
    public void registerFactory(String id, IGmLinkFactory factory) {
        throw new UnsupportedOperationException();
    }

    /**
     * Remove a registered extension factory.
     * @param id The id used to register the extension factory.
     */
    @objid ("f53abdb7-55b6-11e2-877f-002564c97630")
    @Override
    public void unregisterFactory(String id) {
        throw new UnsupportedOperationException();
    }

    /**
     * @return the singleton instance of the link factory for State diagram.
     */
    @objid ("f53abdbb-55b6-11e2-877f-002564c97630")
    public static IGmLinkFactory getInstance() {
        return _instance;
    }

    /**
     * visitor class for the implementation of the links.
     */
    @objid ("f53abdc2-55b6-11e2-877f-002564c97630")
    private class ImplVisitor extends DefaultModelVisitor {
        @objid ("8115ce8e-55c2-11e2-9337-002564c97630")
        private GmAbstractDiagram diagram;

        @objid ("f53c443b-55b6-11e2-877f-002564c97630")
        public ImplVisitor(GmAbstractDiagram diagram) {
            this.diagram = diagram;
        }

        @objid ("f53c4440-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitElement(Element theElement) {
            // We don't know what to do with that element.
            return null;
        }

        @objid ("f53c4448-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitTransition(Transition theTransition) {
            final GmTransition transition = new GmTransition(this.diagram,
                                                             theTransition,
                                                             new MRef(theTransition));
            return transition;
        }

        @objid ("f53c4450-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitInternalTransition(InternalTransition theInternalTransition) {
            // Internal Transition is NOT a link, although it does inherit from Transition!
            return null;
        }

    }

}
