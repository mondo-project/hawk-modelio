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
import org.modelio.diagram.editor.deployment.elements.manifestation.GmManifestation;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.link.GmLink;
import org.modelio.diagram.elements.core.model.IGmLinkFactory;
import org.modelio.metamodel.uml.statik.Manifestation;
import org.modelio.metamodel.visitors.DefaultModelVisitor;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * The Deployment GmLink factory for Modelio diagrams.
 * <p>
 * Implementation of {@link IGmLinkFactory}.
 */
@objid ("972b26a1-55b6-11e2-877f-002564c97630")
public final class DeploymentDiagramGmLinkFactory implements IGmLinkFactory {
    @objid ("972b26a5-55b6-11e2-877f-002564c97630")
    private static final DeploymentDiagramGmLinkFactory INSTANCE = new DeploymentDiagramGmLinkFactory();

    /**
     * Creates the link factory for a diagram.
     * @param diagram
     * the diagram the factory will be attached to.
     */
    @objid ("972b26a7-55b6-11e2-877f-002564c97630")
    private DeploymentDiagramGmLinkFactory() {
    }

    @objid ("972b26aa-55b6-11e2-877f-002564c97630")
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
    @objid ("972b26b7-55b6-11e2-877f-002564c97630")
    @Override
    public void registerFactory(String id, IGmLinkFactory factory) {
        throw new UnsupportedOperationException();
    }

    /**
     * Remove a registered extension factory.
     * @param id The id used to register the extension factory.
     */
    @objid ("972b26be-55b6-11e2-877f-002564c97630")
    @Override
    public void unregisterFactory(String id) {
        throw new UnsupportedOperationException();
    }

    /**
     * @return the singleton instance of the link factory for Deployment diagram.
     */
    @objid ("972b26c2-55b6-11e2-877f-002564c97630")
    public static IGmLinkFactory getInstance() {
        return INSTANCE;
    }

    /**
     * visitor class for the implementation of the links.
     */
    @objid ("972b26c9-55b6-11e2-877f-002564c97630")
    private class ImplVisitor extends DefaultModelVisitor {
        @objid ("1cf183f5-55c2-11e2-9337-002564c97630")
        private GmAbstractDiagram diagram;

        @objid ("972b26d0-55b6-11e2-877f-002564c97630")
        public ImplVisitor(GmAbstractDiagram diagram) {
            this.diagram = diagram;
        }

        @objid ("972b26d5-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitManifestation(Manifestation theManifestation) {
            return new GmManifestation(this.diagram, theManifestation, new MRef(theManifestation));
        }

    }

}
