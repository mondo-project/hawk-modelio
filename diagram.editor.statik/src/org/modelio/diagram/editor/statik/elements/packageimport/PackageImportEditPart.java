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
                                    

package org.modelio.diagram.editor.statik.elements.packageimport;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PolylineDecoration;
import org.modelio.diagram.elements.core.figures.decorations.DefaultPolylineDecoration;
import org.modelio.diagram.elements.core.link.GmLinkEditPart;

/**
 * Edit part for {@link GmPackageImport}.
 * 
 * @author cmarin
 */
@objid ("36099f49-55b7-11e2-877f-002564c97630")
public class PackageImportEditPart extends GmLinkEditPart {
    @objid ("36099f4d-55b7-11e2-877f-002564c97630")
    @Override
    protected IFigure createFigure() {
        // Create the link
        final PolylineConnection connection = (PolylineConnection) super.createFigure();
        
        // Set style independent properties
        
        // Navigability arrow toward target
        DefaultPolylineDecoration arrow = new DefaultPolylineDecoration();
        arrow.setTemplate(PolylineDecoration.TRIANGLE_TIP);
        arrow.setScale(9, 4);
        arrow.setOpaque(false);
        arrow.setBackgroundColor(null);
        arrow.setFill(false);
        connection.setTargetDecoration(arrow);
        
        // Set style dependent properties
        return connection;
    }

}
