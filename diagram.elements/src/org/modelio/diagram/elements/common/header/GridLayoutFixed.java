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
                                    

package org.modelio.diagram.elements.common.header;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.geometry.Dimension;

/**
 * A fixed version of the GridLayout, overriding the problem of the cached preferredSize.
 * 
 * @author fpoyer
 */
@objid ("7e6a0dbe-1dec-11e2-8cad-001ec947c8cc")
public class GridLayoutFixed extends GridLayout {
    @objid ("8c0b56b6-3c4a-45c2-8231-dd88a62f4ebb")
    private Dimension cachedPreferredHint = new Dimension(-1, -1);

    /**
     * Constructs a new instance of this class given the number of columns, and whether or not the columns should be
     * forced to have the same width.
     * @param numColumns the number of columns in the grid
     * @param makeColumnsEqualWidth whether or not the columns will have equal width
     */
    @objid ("7e6a0dc5-1dec-11e2-8cad-001ec947c8cc")
    public GridLayoutFixed(final int numColumns, final boolean makeColumnsEqualWidth) {
        super(numColumns, makeColumnsEqualWidth);
    }

}
