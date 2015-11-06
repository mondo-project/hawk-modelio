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
                                    

package org.modelio.diagram.editor.statik.elements.interfaze;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.SWT;
import org.modelio.core.ui.CoreFontRegistry;
import org.modelio.diagram.elements.common.label.modelelement.ModelElementFlatHeaderEditPart;
import org.modelio.diagram.elements.core.figures.IPenOptionsSupport;
import org.modelio.diagram.styles.core.IStyle;

/**
 * Edit part adding the Italic style to the current label's style.
 */
@objid ("f2635d12-ffbd-4322-b856-0c30e1449bb8")
public class InterfaceFlatHeaderEditPart extends ModelElementFlatHeaderEditPart {
    @objid ("e988357a-7604-4a6e-9d5f-69338d2e2874")
    @Override
    protected void refreshFromStyle(IFigure aFigure, IStyle style) {
        super.refreshFromStyle(aFigure, style);
        
        IPenOptionsSupport headerFigure = (IPenOptionsSupport) aFigure;
        if (headerFigure.getTextFont() != null) {
            headerFigure.setTextFont(CoreFontRegistry.getModifiedFont(headerFigure.getTextFont(), SWT.ITALIC));
        }
    }

}
