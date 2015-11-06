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
                                    

package org.modelio.core.ui.ktable.types.header;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import de.kupzog.ktable.KTableModel;
import de.kupzog.ktable.renderers.FixedCellRenderer;
import org.eclipse.swt.graphics.GC;

@objid ("8ddb5546-c068-11e1-8c0a-002564c97630")
public class HeaderCellRenderer extends FixedCellRenderer {
    @objid ("8ddb5547-c068-11e1-8c0a-002564c97630")
    public HeaderCellRenderer(int style) {
        super(style);
    }

    @objid ("8ddb554a-c068-11e1-8c0a-002564c97630")
    @Override
    public int getOptimalWidth(GC gc, int col, int row, Object content, boolean fixed, KTableModel model) {
        return super.getOptimalWidth(gc, col, row, content, fixed, model) + 4;
    }

}
