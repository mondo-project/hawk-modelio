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
                                    

package org.modelio.diagram.elements.core.policies;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.Request;
import org.modelio.diagram.elements.common.freezone.BaseFreeZoneLayoutEditPolicy;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.uml.infrastructure.Constraint;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * This policy provides a basic XYLayout behavior that should be used as a base (if not used as is) for the layout of
 * all diagrams.
 * 
 * @author fpoyer
 */
@objid ("80d38eef-1dec-11e2-8cad-001ec947c8cc")
public class DiagramEditLayoutPolicy extends BaseFreeZoneLayoutEditPolicy {
    @objid ("80d38ef1-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected MObject getHostElement() {
        final AbstractDiagram diagram = (AbstractDiagram) super.getHostElement();
        return diagram.getOrigin();
    }

    @objid ("80d38ef6-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected boolean canHandle(Class<? extends MObject> metaclass) {
        // Any note or constraint figure may owned by the diagram figure.
        if (Note.class.isAssignableFrom(metaclass) || Constraint.class.isAssignableFrom(metaclass)) {
            return true;
        }
        return super.canHandle(metaclass);
    }

    /**
     * For diagram the default feedback provided by the base class is ugly because it has a background Specialize and
     * remove the background.
     */
    @objid ("80d38efe-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void showTargetFeedback(final Request request) {
        super.showTargetFeedback(request);
        if (this.highlight != null) {
            this.highlight.setOpaque(false);
            this.highlight.setBackgroundColor(null);
        }
    }

}
