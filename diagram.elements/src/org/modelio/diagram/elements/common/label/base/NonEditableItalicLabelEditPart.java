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
                                    

package org.modelio.diagram.elements.common.label.base;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.modelio.core.ui.CoreFontRegistry;
import org.modelio.diagram.styles.core.IStyle;

/**
 * Specialization of the GmElementLabelEditPart to remove the edition functionalities. Displays the text in italic.
 */
@objid ("7e9295c4-1dec-11e2-8cad-001ec947c8cc")
public class NonEditableItalicLabelEditPart extends GmElementLabelEditPart {
    /**
     * Default constructor.
     */
    @objid ("7e9295c6-1dec-11e2-8cad-001ec947c8cc")
    public NonEditableItalicLabelEditPart() {
    }

    @objid ("7e9295c9-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void performRequest(Request req) {
        // Overload performRequest to avoid being edited through the delayed click.
        return;
    }

    @objid ("7e9295d0-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        
        // operation label isn't meant to be editable
        removeEditPolicy(EditPolicy.DIRECT_EDIT_ROLE);
    }

    @objid ("7e9295d3-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected IFigure createFigure() {
        Label label = (Label) super.createFigure();
        label.setLabelAlignment(PositionConstants.CENTER);
        return label;
    }

    @objid ("7e9295da-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void refreshFromStyle(IFigure aFigure, IStyle style) {
        // Standard update from style
        super.refreshFromStyle(aFigure, style);
        
        // Force font to be italic.
        final FontData currentData = ((Label) aFigure).getFont().getFontData()[0];
        final int fontStyle = SWT.ITALIC | currentData.getStyle();
        final FontData newData = new FontData(currentData.getName(), currentData.getHeight(), fontStyle);
        final Font font = CoreFontRegistry.getFont(newData);
        
        ((Label) aFigure).setFont(font);
    }

    @objid ("7e9295e1-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public boolean isSelectable() {
        return false;
    }

}
