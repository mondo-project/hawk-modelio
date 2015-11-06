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
                                    

package org.modelio.diagram.elements.core.link.extensions;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionEndpointLocator;

/**
 * Same as {@link ConnectionEndpointLocator} with isEnd() accessible.
 * 
 * @author cmarin
 */
@objid ("800dab9e-1dec-11e2-8cad-001ec947c8cc")
public class SidedConnectionEndpointLocator extends ConnectionEndpointLocator {
    /**
     * Copy of inaccessible isEnd attribute.
     */
    @objid ("800daba2-1dec-11e2-8cad-001ec947c8cc")
    private boolean isTargetEnd;

    /**
     * Constructs a ConnectionEndpointLocator using the given {@link Connection}. If <i>isEnd</i> is <code>true</code>,
     * the location is relative to the Connection's end (or target) point. If <i>isEnd</i> is <code>false</code>, the
     * location is relative to the Connection's start (or source) point.
     * @param c The Connection
     * @param isEnd <code>true</code> is location is relative to end point
     */
    @objid ("800daba4-1dec-11e2-8cad-001ec947c8cc")
    public SidedConnectionEndpointLocator(Connection c, boolean isEnd) {
        super(c, isEnd);
        this.isTargetEnd = isEnd;
    }

    /**
     * If <i>isEnd</i> is <code>true</code>, the location is relative to the Connection's end (or target) point. If
     * <i>isEnd</i> is <code>false</code> , the location is relative to the Connection's start (or source) point.
     * @return <code>true</code> is location is relative to target point.
     */
    @objid ("800dabab-1dec-11e2-8cad-001ec947c8cc")
    public boolean isEnd() {
        return this.isTargetEnd;
    }

}
