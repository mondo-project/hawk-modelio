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
                                    

package org.modelio.linkeditor.view.node;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.modelio.linkeditor.view.LinkEditorView;

@objid ("1bb03346-5e33-11e2-b81d-002564c97630")
public class FromSideAnchor extends AbstractConnectionAnchor {
    @objid ("1bb03349-5e33-11e2-b81d-002564c97630")
    public FromSideAnchor(final IFigure owner) {
        super(owner);
    }

    @objid ("1bb0334f-5e33-11e2-b81d-002564c97630")
    @Override
    public Point getLocation(final Point reference) {
        Point p = LinkEditorView.getOptions().isLayoutOrientationVertical() ? this.getOwner().getBounds().getBottom().getCopy() : this.getOwner()
                .getBounds().getLeft().getCopy();
        this.getOwner().translateToAbsolute(p);
        return p;
    }

}
