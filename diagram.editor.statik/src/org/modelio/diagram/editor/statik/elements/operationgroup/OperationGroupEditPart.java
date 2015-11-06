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
                                    

package org.modelio.diagram.editor.statik.elements.operationgroup;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.modelio.diagram.elements.common.group.GmGroupEditPart;
import org.modelio.diagram.elements.core.figures.borders.TLBRBorder;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.model.IGmObject;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.MetaKey;

/**
 * EditPart for {@link GmOperationGroup}.
 * <p>
 * All behavior is currently inherited from {@link GmGroupEditPart}. This class may be deleted if no more behavior is to
 * be added.
 */
@objid ("3601fe19-55b7-11e2-877f-002564c97630")
public class OperationGroupEditPart extends GmGroupEditPart {
    @objid ("3601fe1d-55b7-11e2-877f-002564c97630")
    @Override
    protected void refreshVisuals() {
        IGmObject model = (IGmObject) getModel();
        if (model.getLayoutData() != null) {
            getFigure().getParent().setConstraint(getFigure(), model.getLayoutData());
        }
    }

    @objid ("3601fe20-55b7-11e2-877f-002564c97630")
    @Override
    public boolean isSelectable() {
        // Already selected, return true
        if (getViewer().getSelectedEditParts().contains(this)) {
            return true;
        }
        
        // Allow selection only if the composition parent was already selected
        EditPart parent = getParent();
        while (parent != null) {
            if (parent.isSelectable()) {
                return (parent.getSelected() != EditPart.SELECTED_NONE);
            }
            parent = parent.getParent();
        }
        return false;
    }

    @objid ("3601fe25-55b7-11e2-877f-002564c97630")
    @Override
    protected void refreshFromStyle(final IFigure aFigure, final IStyle style) {
        super.refreshFromStyle(aFigure, style);
        final GmModel gmModel = (GmModel) getModel();
        TLBRBorder border = new TLBRBorder(true, false, false, false);
        if (gmModel.getStyleKey(MetaKey.LINECOLOR) != null) {
            border.setColor(style.getColor(gmModel.getStyleKey(MetaKey.LINECOLOR)));
        }
        if (gmModel.getStyleKey(MetaKey.LINEWIDTH) != null) {
            border.setWidth(style.getInteger(gmModel.getStyleKey(MetaKey.LINEWIDTH)));
        }
        aFigure.setBorder(border);
    }

}
