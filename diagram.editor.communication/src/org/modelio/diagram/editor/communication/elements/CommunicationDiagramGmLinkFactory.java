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
import org.modelio.diagram.editor.communication.elements.communicationchannel.GmCommunicationChannel;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.link.GmLink;
import org.modelio.diagram.elements.core.model.IGmLinkFactory;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationChannel;
import org.modelio.metamodel.visitors.DefaultModelVisitor;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * The Communication GmLink factory for Modelio diagrams.
 * <p>
 * Implementation of {@link IGmLinkFactory}.
 */
@objid ("7a2fb7ec-55b6-11e2-877f-002564c97630")
public final class CommunicationDiagramGmLinkFactory implements IGmLinkFactory {
    @objid ("7a313e3b-55b6-11e2-877f-002564c97630")
    private static final CommunicationDiagramGmLinkFactory INSTANCE = new CommunicationDiagramGmLinkFactory();

    /**
     * Creates the link factory for a diagram.
     * @param diagram
     * the diagram the factory will be attached to.
     */
    @objid ("7a313e3d-55b6-11e2-877f-002564c97630")
    private CommunicationDiagramGmLinkFactory() {
    }

    @objid ("7a313e40-55b6-11e2-877f-002564c97630")
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
    @objid ("7a313e4d-55b6-11e2-877f-002564c97630")
    @Override
    public void registerFactory(String id, IGmLinkFactory factory) {
        throw new UnsupportedOperationException();
    }

    /**
     * Remove a registered extension factory.
     * @param id The id used to register the extension factory.
     */
    @objid ("7a313e54-55b6-11e2-877f-002564c97630")
    @Override
    public void unregisterFactory(String id) {
        throw new UnsupportedOperationException();
    }

    /**
     * @return the singleton instance of the link factory for Communication diagram.
     */
    @objid ("7a313e58-55b6-11e2-877f-002564c97630")
    public static IGmLinkFactory getInstance() {
        return INSTANCE;
    }

    /**
     * visitor class for the implementation of the links.
     */
    @objid ("7a313e5f-55b6-11e2-877f-002564c97630")
    private class ImplVisitor extends DefaultModelVisitor {
        @objid ("9c8a6be9-55c1-11e2-9337-002564c97630")
        private GmAbstractDiagram diagram;

        @objid ("7a313e66-55b6-11e2-877f-002564c97630")
        public ImplVisitor(GmAbstractDiagram diagram) {
            this.diagram = diagram;
        }

        @objid ("bf17df02-0ca1-46ff-801a-782f5b227846")
        @Override
        public Object visitCommunicationChannel(CommunicationChannel theCommunicationChannel) {
            return new GmCommunicationChannel(this.diagram, theCommunicationChannel, new MRef(theCommunicationChannel));
        }

    }

}
