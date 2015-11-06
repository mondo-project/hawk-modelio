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
                                    

package org.modelio.diagram.elements.core.link.path;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Build a serializable data model from a given {@link IConnectionHelper}.
 * 
 * @author cmarin
 */
@objid ("80494654-1dec-11e2-8cad-001ec947c8cc")
public class GmPathDataExtractor {
    /**
     * No instance.
     */
    @objid ("80494656-1dec-11e2-8cad-001ec947c8cc")
    private GmPathDataExtractor() {
    }

    /**
     * Build a serializable data model from a given {@link IConnectionHelper}.
     * @param connectionPath the connection path to serialize.
     * @return a data model.
     */
    @objid ("80494659-1dec-11e2-8cad-001ec947c8cc")
    public static Object extractDataModel(final IConnectionHelper connectionPath) {
        if (connectionPath instanceof RakeConnectionHelper) {
            RakeConnectionHelper p1 = (RakeConnectionHelper) connectionPath;
            return p1.getRoutingConstraint();
        }
        
        switch (connectionPath.getRoutingMode()) {
            case DIRECT:
                return null;
            case BENDPOINT:
                ObliqueConnectionHelper p = (ObliqueConnectionHelper) connectionPath;
                return p.getBendPoints();
            case ORTHOGONAL:
                OrthoConnectionHelper p1 = (OrthoConnectionHelper) connectionPath;
                return p1.getBendPoints();
        
        }
        return null;
    }

}
