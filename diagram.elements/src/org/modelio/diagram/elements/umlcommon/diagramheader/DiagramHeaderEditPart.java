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
                                    

package org.modelio.diagram.elements.umlcommon.diagramheader;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.widgets.Display;
import org.modelio.diagram.elements.common.label.modelelement.ModelElementFlatHeaderEditPart;
import org.modelio.diagram.elements.core.figures.IPenOptionsSupport;
import org.modelio.diagram.elements.core.figures.IShaper;
import org.modelio.diagram.elements.core.figures.borders.ShapedBorder;
import org.modelio.diagram.styles.core.IStyle;

/**
 * Edit part for {@link GmDiagramHeader}.
 * <p>
 * Creates a flat label figure with a diagram header shaped border.
 * 
 * @author cmarin
 */
@objid ("812bc5b2-1dec-11e2-8cad-001ec947c8cc")
public class DiagramHeaderEditPart extends ModelElementFlatHeaderEditPart {
    @objid ("812bc5b4-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void refreshFromStyle(final IFigure headerFigure, final IStyle style) {
        super.refreshFromStyle(headerFigure, style);
        
        updateFigureBorder(headerFigure);
    }

    @objid ("812bc5bd-1dec-11e2-8cad-001ec947c8cc")
    private void updateFigureBorder(final IFigure aFigure) {
        final IPenOptionsSupport fig = (IPenOptionsSupport) aFigure;
        
        aFigure.setBorder(new ShapedBorder(fig.getLineColor(), fig.getLineWidth(), new DiagramHeaderShaper()));
    }

    @objid ("812bc5c3-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public boolean isSelectable() {
        return false;
    }

    /**
     * Diagram header shape.
     * <p>
     * Looks like:
     * <p>
     * 
     * <pre>
     * +-------+
     * |       |
     * |      /
     * +------
     * </pre>
     * 
     * @author cmarin
     */
    @objid ("812bc5c8-1dec-11e2-8cad-001ec947c8cc")
    private static class DiagramHeaderShaper implements IShaper {
        @objid ("812bc5cb-1dec-11e2-8cad-001ec947c8cc")
        public DiagramHeaderShaper() {
        }

        @objid ("812bc5cd-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Insets getShapeInsets(final Rectangle rect) {
            return new Insets(2, 2, 2, getFoldSize(rect));
        }

        /**
         * Computes the fold size for a rectangle.
         * @param rect a rectangle
         * @return the fold size for this rectangle.
         */
        @objid ("812bc5d8-1dec-11e2-8cad-001ec947c8cc")
        private int getFoldSize(final Rectangle rect) {
            return rect.height / 2;
        }

        @objid ("812bc5e1-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Path getShapePath(final Rectangle r) {
            final Path ret = new Path(Display.getCurrent());
            final int foldSize = getFoldSize(r);
            
            //ret.moveTo(r.x, r.y);
            ret.moveTo(r.right(), r.y);
            ret.lineTo(r.right(), r.y + foldSize);
            ret.lineTo(r.right() - foldSize, r.bottom());
            ret.lineTo(r.x, r.bottom());
            //ret.lineTo(r.x, r.y);
            return ret;
        }

    }

}
