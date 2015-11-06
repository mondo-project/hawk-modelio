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
                                    

package org.modelio.diagram.editor.bpmn.elements.gmfactory;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.visitors.DefaultModelVisitor;

@objid ("621648cc-55b6-11e2-877f-002564c97630")
class GroupElementFactoryVisitor extends DefaultModelVisitor {
    @objid ("7296fb0a-55c1-11e2-9337-002564c97630")
    private GmAbstractDiagram diagram;

    @objid ("621648d2-55b6-11e2-877f-002564c97630")
    public GroupElementFactoryVisitor(GmAbstractDiagram diagram) {
        this.diagram = diagram;
    }

    @objid ("621648d7-55b6-11e2-877f-002564c97630")
    @Override
    public Object visitElement(Element theElement) {
        // We don't know what to do with that element.
        return null;
    }

}
