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
                                    

package org.modelio.diagram.editor.statik.elements.association;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.SWT;
import org.modelio.core.ui.CoreFontRegistry;
import org.modelio.diagram.elements.common.header.ModelElementHeaderEditPart;
import org.modelio.diagram.elements.common.header.WrappedHeaderFigure;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.metamodel.uml.statik.AssociationEnd;

/**
 * Edit part for {@link GmRoleNameLabel}.
 */
@objid ("33f209eb-55b7-11e2-877f-002564c97630")
public class RoleNameEditPart extends ModelElementHeaderEditPart {
    @objid ("33f209ef-55b7-11e2-877f-002564c97630")
    @Override
    protected void refreshVisuals() {
        super.refreshVisuals();
        
        refreshFromStyle(getFigure(), getModelStyle());
    }

    @objid ("33f209f2-55b7-11e2-877f-002564c97630")
    @Override
    protected void refreshFromStyle(final IFigure fig, final IStyle style) {
        super.refreshFromStyle(fig, style);
        refreshStaticAbstract((WrappedHeaderFigure) fig);
    }

    @objid ("33f209fb-55b7-11e2-877f-002564c97630")
    private void refreshStaticAbstract(final WrappedHeaderFigure fig) {
        GmRoleNameLabel gm = (GmRoleNameLabel) getModel();
        AssociationEnd el = (AssociationEnd) gm.getRelatedElement();
        
        // underline static
        fig.setUnderline(el.isIsClass());
        
        // italic abstract
        if (el.isIsAbstract()) {
            fig.setTextFont(CoreFontRegistry.getModifiedFont(fig.getTextFont(), SWT.ITALIC));
        }
    }

}
