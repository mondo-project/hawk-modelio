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
                                    

package org.modelio.diagram.elements.common.portcontainer;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.handles.HandleBounds;

/**
 * Specialised figure for port containment.
 * 
 * @author fpoyer
 */
@objid ("7ef6b8a6-1dec-11e2-8cad-001ec947c8cc")
public class PortContainerFigure extends Figure implements HandleBounds {
    @objid ("cd985cd2-5e2c-4385-a0e0-9b54bbbec1e9")
    private IFigure mainNodeFigure;

    /**
     * Returns the Rectangle around which handles are to be placed. The Rectangle should be in the same coordinate
     * system as the figure itself.
     * @return The rectangle used for handles
     */
    @objid ("7ef6b8af-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Rectangle getHandleBounds() {
        if (this.mainNodeFigure != null) {
            if (this.mainNodeFigure instanceof HandleBounds) {
                HandleBounds mainNodeHandleBounds = (HandleBounds) this.mainNodeFigure;
                return mainNodeHandleBounds.getHandleBounds();
            }
            // else
            return this.mainNodeFigure.getBounds();
        
        }
        // else
        return this.getBounds();
    }

    /**
     * Sets the main node figure.
     * @param mainNodeFigure the main node figure.
     */
    @objid ("7ef91ab9-1dec-11e2-8cad-001ec947c8cc")
    public void setMainNodeFigure(IFigure mainNodeFigure) {
        this.mainNodeFigure = mainNodeFigure;
    }

    @objid ("7ef91abf-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public boolean containsPoint(int x, int y) {
        for (Object childObj : getChildren()) {
            if (((IFigure) childObj).containsPoint(x, y))
                return true;
        }
        return false;
    }

    @objid ("7ef91ac6-1dec-11e2-8cad-001ec947c8cc")
    protected IFigure getMainNodeFigure() {
        return this.mainNodeFigure;
    }

}
