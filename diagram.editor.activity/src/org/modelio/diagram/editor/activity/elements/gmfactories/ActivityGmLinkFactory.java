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
                                    

package org.modelio.diagram.editor.activity.elements.gmfactories;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.activity.elements.controlflow.GmControlFlow;
import org.modelio.diagram.editor.activity.elements.exceptionhandler.GmExceptionHandler;
import org.modelio.diagram.editor.activity.elements.objectflow.GmObjectFlow;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.link.GmLink;
import org.modelio.diagram.elements.core.model.IGmLinkFactory;
import org.modelio.metamodel.uml.behavior.activityModel.ControlFlow;
import org.modelio.metamodel.uml.behavior.activityModel.ExceptionHandler;
import org.modelio.metamodel.uml.behavior.activityModel.ObjectFlow;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.visitors.DefaultModelVisitor;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Implementation of {@link IGmLinkFactory}.
 */
@objid ("2a8dab7a-55b6-11e2-877f-002564c97630")
public class ActivityGmLinkFactory implements IGmLinkFactory {
    @objid ("2a8dab7e-55b6-11e2-877f-002564c97630")
    private static final ActivityGmLinkFactory _instance = new ActivityGmLinkFactory();

    /**
     * Creates the link factory for a diagram.
     * @param diagram
     * the diagram the factory will be attached to.
     */
    @objid ("2a8dab80-55b6-11e2-877f-002564c97630")
    private ActivityGmLinkFactory() {
    }

    @objid ("2a8dab83-55b6-11e2-877f-002564c97630")
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
    @objid ("2a8dab90-55b6-11e2-877f-002564c97630")
    @Override
    public void registerFactory(String id, IGmLinkFactory factory) {
        throw new UnsupportedOperationException();
    }

    /**
     * Remove a registered extension factory.
     * @param id The id used to register the extension factory.
     */
    @objid ("2a8dab97-55b6-11e2-877f-002564c97630")
    @Override
    public void unregisterFactory(String id) {
        throw new UnsupportedOperationException();
    }

    /**
     * @return the singleton instance of the link factory for State diagram.
     */
    @objid ("2a8dab9b-55b6-11e2-877f-002564c97630")
    public static IGmLinkFactory getInstance() {
        return _instance;
    }

    /**
     * visitor class for the implementation of the links.
     */
    @objid ("2a8daba2-55b6-11e2-877f-002564c97630")
    private class ImplVisitor extends DefaultModelVisitor {
        @objid ("d21050ae-55c0-11e2-9337-002564c97630")
        private GmAbstractDiagram diagram;

        @objid ("2a8f321b-55b6-11e2-877f-002564c97630")
        public ImplVisitor(GmAbstractDiagram diagram) {
            this.diagram = diagram;
        }

        @objid ("2a8f3220-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitElement(Element theElement) {
            // We don't know what to do with that element.
            return null;
        }

        @objid ("2a8f3228-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitControlFlow(ControlFlow theControlFlow) {
            final GmControlFlow controlFlow = new GmControlFlow(this.diagram,
                                                                theControlFlow,
                                                                new MRef(theControlFlow));
            return controlFlow;
        }

        @objid ("2a8f3230-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitObjectFlow(ObjectFlow theObjectFlow) {
            final GmObjectFlow objectFlow = new GmObjectFlow(this.diagram,
                                                             theObjectFlow,
                                                             new MRef(theObjectFlow));
            return objectFlow;
        }

        @objid ("2a8f3238-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitExceptionHandler(ExceptionHandler theExceptionHandler) {
            final GmExceptionHandler exception = new GmExceptionHandler(this.diagram,
                                                                        theExceptionHandler,
                                                                        new MRef(theExceptionHandler));
            return exception;
        }

    }

}
