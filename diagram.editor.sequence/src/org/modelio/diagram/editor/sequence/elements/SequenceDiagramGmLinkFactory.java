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
import org.modelio.diagram.editor.sequence.elements.message.GmMessage;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.link.GmLink;
import org.modelio.diagram.elements.core.model.IGmLinkFactory;
import org.modelio.metamodel.uml.behavior.interactionModel.Message;
import org.modelio.metamodel.visitors.DefaultModelVisitor;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * The GmLink factory for Sequence diagrams.
 * <p>
 * Implementation of {@link IGmLinkFactory}.
 */
@objid ("d986c2d6-55b6-11e2-877f-002564c97630")
public final class SequenceDiagramGmLinkFactory implements IGmLinkFactory {
    @objid ("d986c2da-55b6-11e2-877f-002564c97630")
    private static final SequenceDiagramGmLinkFactory INSTANCE = new SequenceDiagramGmLinkFactory();

    @objid ("d986c2dc-55b6-11e2-877f-002564c97630")
    @Override
    public GmLink create(GmAbstractDiagram diagram, MObject linkElement) {
        return (GmLink) linkElement.accept(new ImplVisitor(diagram));
    }

    /**
     * @return the singleton instance of the link factory for Sequence diagram.
     */
    @objid ("d986c2e9-55b6-11e2-877f-002564c97630")
    public static IGmLinkFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Register an GmLink factory extension.
     * <p>
     * Extension GmLink factories are called first when looking for an GmLink.
     * @param id id for the GmLink extension factory
     * @param factory the edit part factory.
     */
    @objid ("d986c2f0-55b6-11e2-877f-002564c97630")
    @Override
    public void registerFactory(String id, IGmLinkFactory factory) {
        throw new UnsupportedOperationException();
    }

    /**
     * Remove a registered extension factory.
     * @param id The id used to register the extension factory.
     */
    @objid ("d986c2f7-55b6-11e2-877f-002564c97630")
    @Override
    public void unregisterFactory(String id) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates the link factory for a diagram.
     * @param diagram
     * the diagram the factory will be attached to.
     */
    @objid ("d986c2fb-55b6-11e2-877f-002564c97630")
    private SequenceDiagramGmLinkFactory() {
    }

    /**
     * visitor class for the implementation of the links.
     */
    @objid ("d986c2fe-55b6-11e2-877f-002564c97630")
    private class ImplVisitor extends DefaultModelVisitor {
        @objid ("5068906a-55c2-11e2-9337-002564c97630")
        private GmAbstractDiagram diagram;

        @objid ("d988495e-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitMessage(Message theMessage) {
            final GmMessage msg = new GmMessage(this.diagram, theMessage, new MRef(theMessage));
            return msg;
        }

        @objid ("d9884966-55b6-11e2-877f-002564c97630")
        public ImplVisitor(final GmAbstractDiagram diagram) {
            this.diagram = diagram;
        }

    }

}
