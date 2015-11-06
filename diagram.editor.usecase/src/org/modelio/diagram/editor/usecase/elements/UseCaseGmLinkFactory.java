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
import org.modelio.diagram.editor.usecase.elements.communicationlink.GmCommunicationLink;
import org.modelio.diagram.editor.usecase.elements.usecasedependency.GmUseCaseDependency;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.link.GmLink;
import org.modelio.diagram.elements.core.model.IGmLinkFactory;
import org.modelio.metamodel.uml.behavior.usecaseModel.Actor;
import org.modelio.metamodel.uml.behavior.usecaseModel.UseCase;
import org.modelio.metamodel.uml.behavior.usecaseModel.UseCaseDependency;
import org.modelio.metamodel.uml.statik.AssociationEnd;
import org.modelio.metamodel.visitors.DefaultModelVisitor;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

@objid ("5e8a467b-55b7-11e2-877f-002564c97630")
public final class UseCaseGmLinkFactory implements IGmLinkFactory {
    @objid ("5e8bccdb-55b7-11e2-877f-002564c97630")
    private static final UseCaseGmLinkFactory _instance = new UseCaseGmLinkFactory();

    @objid ("5e8bcce0-55b7-11e2-877f-002564c97630")
    @Override
    public GmLink create(GmAbstractDiagram diagram, MObject linkElement) {
        return (GmLink) linkElement.accept(new ImplVisitor(diagram));
    }

    @objid ("5e8bcced-55b7-11e2-877f-002564c97630")
    @Override
    public void registerFactory(String id, IGmLinkFactory factory) {
        throw new UnsupportedOperationException();
    }

    @objid ("5e8bccf4-55b7-11e2-877f-002564c97630")
    @Override
    public void unregisterFactory(String id) {
        throw new UnsupportedOperationException();
    }

    @objid ("5e8bccf8-55b7-11e2-877f-002564c97630")
    public static IGmLinkFactory getInstance() {
        return _instance;
    }

    @objid ("7b69b806-5eff-11e2-b9cc-001ec947c8cc")
    private UseCaseGmLinkFactory() {
    }

    @objid ("5e8bccff-55b7-11e2-877f-002564c97630")
    private class ImplVisitor extends DefaultModelVisitor {
        @objid ("d9d4ec57-55c2-11e2-9337-002564c97630")
        private GmAbstractDiagram diagram;

        @objid ("5e8bcd06-55b7-11e2-877f-002564c97630")
        public ImplVisitor(GmAbstractDiagram diagram) {
            this.diagram = diagram;
        }

        @objid ("5e8bcd0b-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitUseCaseDependency(UseCaseDependency theUseCaseDependency) {
            return new GmUseCaseDependency(this.diagram,
                    theUseCaseDependency,
                    new MRef(theUseCaseDependency));
        }

        @objid ("5e8bcd1a-55b7-11e2-877f-002564c97630")
        @Override
        public Object visitAssociationEnd(AssociationEnd role) {
            // Communication link are returned only for associations between actors or usecases.
            if (!(role.getSource() instanceof Actor) && !(role.getSource() instanceof UseCase)) {
                return null;
            }
            AssociationEnd otherEnd = role.getOpposite();
            if (!(otherEnd.getSource() instanceof Actor) && !(otherEnd.getSource() instanceof UseCase)) {
                return null;
            }
            return new GmCommunicationLink(this.diagram, role, new MRef(role), new MRef(role.getAssociation()));
        }

    }

}
