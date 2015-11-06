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
                                    

package org.modelio.diagram.editor.bpmn.elements.bpmndataobject;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.widgets.Display;
import org.modelio.diagram.elements.core.figures.GradientFigure;

/**
 * A note figure is is made of: <br>
 * <ul>
 * <li>a title (a label)</li>
 * <li>a content area (multiline text)</li>
 * </ul>
 */
@objid ("60aca68a-55b6-11e2-877f-002564c97630")
public class BpmnDataFigure extends GradientFigure {
    @objid ("60ae2d1a-55b6-11e2-877f-002564c97630")
    private static final int FOLDSIZE = 12;

    @objid ("2ed38e40-56e6-4acc-91c3-7f66a8350a7c")
    private static final PointList foldTemplate = new PointList(new int[] { 0, 0, 0, BpmnDataFigure.FOLDSIZE,
            BpmnDataFigure.FOLDSIZE, BpmnDataFigure.FOLDSIZE });

    @objid ("216fda40-0315-47ef-8183-e50806e0e4f6")
    protected Figure iconsArea;

    @objid ("187d3333-8d86-4f23-aeef-50e6cfd7e1cc")
    protected Figure iconsContainer;

    @objid ("966cb4e4-c631-4c7b-9bdc-b43b5dd28ae0")
    protected Figure botomContainer;

    @objid ("60ae2d1f-55b6-11e2-877f-002564c97630")
    @Override
    protected void paintFigure(Graphics graphics) {
        final Rectangle aBounds = getBounds().getCopy();
        final Rectangle originalClip = new Rectangle();
        graphics.getClip(originalClip);
        
        if (this.isOpaque()) {
            final Path path = new Path(Display.getCurrent());
            final Rectangle r1 = new Rectangle(aBounds.x, aBounds.y, aBounds.width - FOLDSIZE, aBounds.height);
            final Rectangle r2 = new Rectangle(aBounds.x + aBounds.width - FOLDSIZE,
                                               aBounds.y + FOLDSIZE,
                                               FOLDSIZE,
                                               aBounds.height);
            path.addRectangle(r1.x, r1.y, r1.width, r1.height);
            path.addRectangle(r2.x, r2.y, r2.width, r2.height);
            graphics.setClip(path);
            // now let the BoxFigure draw the gradient
            super.paintFigure(graphics);
        
            path.dispose();
            graphics.setClip(originalClip);
        
            // paint the fold background
            final PointList polygon = BpmnDataFigure.foldTemplate.getCopy();
            polygon.translate(aBounds.x + aBounds.width - FOLDSIZE, aBounds.y);
            graphics.fillPolygon(polygon);
        
        } // end is opaque
        
        // Draw outline
        
        if (this.penOptions.lineWidth % 2 == 1) {
            aBounds.width--;
            aBounds.height--;
        }
        aBounds.shrink(this.penOptions.lineWidth / 2, this.penOptions.lineWidth / 2);
        graphics.setLineWidth(this.penOptions.lineWidth);
        graphics.setForegroundColor(this.penOptions.lineColor);
        
        final Point p1 = new Point(aBounds.x + aBounds.width - FOLDSIZE, aBounds.y);
        final Point p2 = new Point(aBounds.x + aBounds.width - FOLDSIZE, aBounds.y + FOLDSIZE);
        final Point p3 = new Point(aBounds.x + aBounds.width, aBounds.y + FOLDSIZE);
        
        graphics.drawLine(aBounds.getTopLeft(), aBounds.getBottomLeft());
        graphics.drawLine(aBounds.getBottomLeft(), aBounds.getBottomRight());
        graphics.drawLine(aBounds.getBottomRight(), p3);
        graphics.drawLine(p3, p1);
        graphics.drawLine(p1, aBounds.getTopLeft());
        graphics.drawLine(p3, p2);
        graphics.drawLine(p2, p1);
        
        graphics.setLineStyle(SWT.LINE_DOT);
        ////        final Rectangle r = this.scrollPane.getBounds();
        //        graphics.drawLine(r.x - 8, r.y, r.x + r.width - 8, r.y);
    }

    @objid ("60ae2d23-55b6-11e2-877f-002564c97630")
    public BpmnDataFigure() {
        this.setLayoutManager(new BorderLayout());
        this.iconsArea = new Figure();
        this.iconsArea.setLayoutManager(new BorderLayout());
        this.add(this.iconsArea, BorderLayout.CENTER);
        
        this.iconsContainer = new Figure();
        BorderLayout centerIconAreaLayout = new BorderLayout();
        this.iconsContainer.setLayoutManager(centerIconAreaLayout);
        this.iconsContainer.setOpaque(false);
        this.iconsArea.add(this.iconsContainer, BorderLayout.CENTER);
        
        this.botomContainer = new Figure();
        BorderLayout botomIconAreaLayout = new BorderLayout();
        this.botomContainer.setLayoutManager(botomIconAreaLayout);
        this.botomContainer.setOpaque(false);
        this.iconsArea.add(this.botomContainer, BorderLayout.TOP);
    }

    @objid ("60ae2d25-55b6-11e2-877f-002564c97630")
    public void setCenterIcone(final List<Image> icons) {
        // remove existing labels
        this.iconsContainer.removeAll();
        // add new image figures
        for (Image img : icons) {
            ImageFigure imgFigure = new ImageFigure(img);
            this.iconsContainer.add(imgFigure, BorderLayout.CENTER);
        }
    }

    @objid ("60ae2d2b-55b6-11e2-877f-002564c97630")
    public void setTopIcone(final List<Image> icons) {
        // remove existing labels
        this.botomContainer.removeAll();
        // add new image figures
        for (Image img : icons) {
            ImageFigure imgFigure = new ImageFigure(img);
            imgFigure.setBorder(new MarginBorder(5, 5, 0, 0));
            this.botomContainer.add(imgFigure, BorderLayout.LEFT);
        }
    }

}
