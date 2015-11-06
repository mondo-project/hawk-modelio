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
                                    

package org.modelio.diagram.editor.statik.elements.internalstructure;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.modelio.diagram.elements.common.freezone.FreeZoneLayout;
import org.modelio.diagram.elements.common.freezone.GmFreeZoneEditPart;
import org.modelio.diagram.elements.core.figures.RectangularFigure;
import org.modelio.diagram.elements.core.model.IGmObject;

/**
 * {@link GmInternalStructureZone} edit part.
 * <p>
 * Installs a custom layout policy.
 */
@objid ("3595a7e1-55b7-11e2-877f-002564c97630")
public class InternalStructureZoneEditPart extends GmFreeZoneEditPart {
    @objid ("3595a7e5-55b7-11e2-877f-002564c97630")
    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new InstanceZoneLayoutEditPolicy());
    }

    @objid ("3595a7e8-55b7-11e2-877f-002564c97630")
    @Override
    public boolean isSelectable() {
        return true;
    }

    @objid ("3595a7ed-55b7-11e2-877f-002564c97630")
    @Override
    protected void refreshVisuals() {
        IGmObject model = (IGmObject) getModel();
        if (model.getLayoutData() != null) {
            getFigure().getParent().setConstraint(getFigure(), model.getLayoutData());
        }
    }

    @objid ("3595a7f0-55b7-11e2-877f-002564c97630")
    @Override
    protected IFigure createFigure() {
        RectangularFigure freeZoneFigure = new RectangularFigure();
        freeZoneFigure.setDrawTop(false);
        freeZoneFigure.setDrawLeft(false);
        freeZoneFigure.setDrawBottom(false);
        freeZoneFigure.setDrawRight(false);
        final FreeZoneLayout layout = new FreeZoneLayout();
        freeZoneFigure.setLayoutManager(layout);
        // Set style dependent properties
        refreshFromStyle(freeZoneFigure, getModelStyle());
        return freeZoneFigure;
    }

}
