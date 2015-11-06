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
                                    

package org.modelio.diagram.editor.statik.elements.operation;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.SWT;
import org.modelio.core.ui.CoreFontRegistry;
import org.modelio.diagram.elements.common.groupitem.GroupItemEditPart;
import org.modelio.diagram.elements.common.label.modelelement.ModelElementFlatHeaderFigure;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.metamodel.uml.statik.Operation;

/**
 * EditPart for {@link GmOperation}.
 * <p>
 * Allows element import, package import, raised exception and dependency links.
 * 
 * @author cmarin
 */
@objid ("35fbe3ac-55b7-11e2-877f-002564c97630")
public class OperationEditPart extends GroupItemEditPart {
    @objid ("35fbe3b0-55b7-11e2-877f-002564c97630")
    @Override
    protected void refreshVisuals() {
        super.refreshVisuals();
        
        refreshFromStyle(getFigure(), getModelStyle());
    }

    @objid ("35fbe3b3-55b7-11e2-877f-002564c97630")
    @Override
    protected void refreshFromStyle(final IFigure fig, final IStyle style) {
        super.refreshFromStyle(fig, style);
        refreshStaticAbstract((ModelElementFlatHeaderFigure) fig);
    }

    @objid ("35fbe3bc-55b7-11e2-877f-002564c97630")
    private void refreshStaticAbstract(final ModelElementFlatHeaderFigure fig) {
        GmOperation gm = (GmOperation) getModel();
        Operation el = gm.getRelatedElement();
        
        // underline static
        fig.setUnderline(el.isIsClass());
        
        // italic abstract
        if (el.isIsAbstract()) {
            fig.setTextFont(CoreFontRegistry.getModifiedFont(fig.getTextFont(), SWT.ITALIC));
        }
    }

}
