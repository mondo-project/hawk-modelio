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
                                    

package org.modelio.diagram.editor.sequence.elements.combinedfragment.primarynode;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.IFigure;
import org.modelio.diagram.elements.core.figures.RectangularFigure;

/**
 * "Hollow" figure.
 * 
 * @author fpoyer
 */
@objid ("d8c98d6d-55b6-11e2-877f-002564c97630")
public class CombinedFragmentPrimaryNodeFigure extends RectangularFigure {
    @objid ("d8cb13db-55b6-11e2-877f-002564c97630")
    @Override
    public boolean containsPoint(final int x, final int y) {
        if (!getChildren().isEmpty()) {
            for (Object childObj : getChildren()) {
                if (((IFigure) childObj).containsPoint(x, y)) {
                    return true;
                }
            }
        }
        return false;
    }

}
