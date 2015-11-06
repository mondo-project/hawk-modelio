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
                                    

package org.modelio.diagram.elements.common.freezone;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.requests.ChangeBoundsRequest;

/**
 * Specialisation for most body zone: do not allow resized child to either become smaller than its minimum size nor
 * bigger than the available space.
 * 
 * @author fpoyer
 */
@objid ("7e37fc6c-1dec-11e2-8cad-001ec947c8cc")
public class DefaultFreeZoneLayoutEditPolicy extends BaseFreeZoneLayoutEditPolicy {
    /**
     * Overridden to prevent size from becoming smaller than min size of the resized child and from becoming larger than
     * the available area.
     */
    @objid ("7e37fc6e-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected Object getConstraintFor(ChangeBoundsRequest request, GraphicalEditPart child) {
        Rectangle rect = new PrecisionRectangle(child.getFigure().getBounds());
        Rectangle original = rect.getCopy();
        child.getFigure().translateToAbsolute(rect);
        rect = request.getTransformedRectangle(rect);
        child.getFigure().translateToRelative(rect);
        rect.translate(getLayoutOrigin().getNegated());
        
        if (request.getSizeDelta().width == 0 && request.getSizeDelta().height == 0) {
            Rectangle cons = getCurrentConstraintFor(child);
            if (cons != null) // Bug 86473 allows for unintended use of this
                // method
                rect.setSize(cons.width, cons.height);
        } else { // resize
            // TODO: override getMinimumSizeFor(child) so that it uses the
            // actual minimum size of the child's figure.
            // Not done yet, because labels returns as minimum size the full
            // label length.
            Dimension minSize = getMinimumSizeFor(child);
            if (rect.width < minSize.width) {
                rect.width = minSize.width;
                if (rect.x > (original.right() - minSize.width))
                    rect.x = original.right() - minSize.width;
            }
            if (rect.height < minSize.height) {
                rect.height = minSize.height;
                if (rect.y > (original.bottom() - minSize.height))
                    rect.y = original.bottom() - minSize.height;
            }
            Dimension maxSize = getHostFigure().getClientArea().getSize();
            if (rect.width > maxSize.width) {
                rect.width = maxSize.width;
                if (rect.x > (original.right() - maxSize.width))
                    rect.x = original.right() - maxSize.width;
            }
            if (rect.height > maxSize.height) {
                rect.height = maxSize.height;
                if (rect.y > (original.bottom() - maxSize.height))
                    rect.y = original.bottom() - maxSize.height;
            }
        }
        return getConstraintFor(rect);
    }

}
