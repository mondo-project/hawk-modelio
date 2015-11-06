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
                                    

package org.modelio.diagram.elements.umlcommon.note;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.ScrollPaneLayout;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.widgets.Display;
import org.modelio.diagram.elements.core.figures.GradientFigure;
import org.modelio.diagram.elements.core.figures.ToolbarLayoutWithGrab;

/**
 * A note figure is is made of: <br>
 * <ul>
 * <li>a title (a label)</li>
 * <li>a content area (multiline text)</li>
 * </ul>
 */
@objid ("818d863e-1dec-11e2-8cad-001ec947c8cc")
public class NoteFigure extends GradientFigure {
    @objid ("818d864a-1dec-11e2-8cad-001ec947c8cc")
    private static final int FOLDSIZE = 12;

    @objid ("687af5c7-1e83-11e2-8cad-001ec947c8cc")
    private static final PointList foldTemplate = new PointList(new int[] { 0, 0, 0, NoteFigure.FOLDSIZE,
            NoteFigure.FOLDSIZE, NoteFigure.FOLDSIZE });

    @objid ("687af5c9-1e83-11e2-8cad-001ec947c8cc")
    private Label title;

    @objid ("687af5ca-1e83-11e2-8cad-001ec947c8cc")
    private FlowPage contents;

    @objid ("687af5cb-1e83-11e2-8cad-001ec947c8cc")
    private TextFlow contentsText;

    @objid ("687af5cc-1e83-11e2-8cad-001ec947c8cc")
    private ScrollPane scrollPane;

    /**
     * Creates a note figure.
     */
    @objid ("818d8652-1dec-11e2-8cad-001ec947c8cc")
    public NoteFigure() {
        // The note figure is a container layouted as a vertical toolbar
        // Children are transparent without borders
        ToolbarLayout layout = new NoteLayout();
        layout.setStretchMinorAxis(true);
        setLayoutManager(layout);
        
        // this.setBorder(new NoteBorder(linePen.lineColor, linePen.lineWidth, FOLDSIZE));
        
        // First child: the title area
        this.title = new Label();
        //this.title.setOpaque(true);
        //this.title.setBackgroundColor(ColorConstants.green);
        this.title.setBorder(new MarginBorder(4));
        this.add(this.title);
        
        // The note text figure list is placed in a TRANSPARENT scroll pane
        this.scrollPane = new TransparentScrollPane();
        this.scrollPane.getViewport().setContentsTracksWidth(true);
        this.scrollPane.getViewport().setContentsTracksHeight(true);
        this.scrollPane.setLayoutManager(new ScrollPaneLayout());
        this.scrollPane.setVerticalScrollBarVisibility(ScrollPane.AUTOMATIC);
        this.scrollPane.setHorizontalScrollBarVisibility(ScrollPane.AUTOMATIC);
        this.scrollPane.setBorder(new MarginBorder(6, 2, 2, 2));
        
        // In the scroll: the note text, a FlowPage + a TextFlow
        // 
        this.contents = new FlowPage();
        this.contentsText = new TextFlow();
        this.contents.add(this.contentsText);
        this.contents.setBorder(new MarginBorder(2));
        this.contents.setOpaque(false);
        this.contents.setHorizontalAligment(PositionConstants.LEFT);
        
        this.scrollPane.setContents(this.contents);
        
        this.add(this.scrollPane);
    }

    /**
     * Get the note content figure.
     * @return The figure where the note content is displayed.
     */
    @objid ("818d8655-1dec-11e2-8cad-001ec947c8cc")
    public Figure getContentFigure() {
        return this.contents;
    }

    /**
     * Set the note text.
     * @param contents the note text.
     */
    @objid ("818d865c-1dec-11e2-8cad-001ec947c8cc")
    public void setContents(String contents) {
        this.contentsText.setText(contents);
    }

    @objid ("818d8660-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setTextColor(Color textColor) {
        this.title.setForegroundColor(textColor);
        this.contents.setForegroundColor(textColor);
        super.setTextColor(textColor);
    }

    @objid ("818d8664-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setTextFont(Font textFont) {
        this.title.setFont(textFont);
        this.title.setMinimumSize(this.title.getPreferredSize(-1, -1));
        this.contents.setFont(textFont);
        super.setTextFont(textFont);
    }

    /**
     * set the displayed note type.
     * @param type the note type.
     */
    @objid ("818d8668-1dec-11e2-8cad-001ec947c8cc")
    public void setType(String type) {
        this.title.setText(type);
        this.title.setMinimumSize(this.title.getPreferredSize(-1, -1));
    }

    @objid ("818d866c-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void paintFigure(Graphics graphics) {
        final Rectangle aBounds = getBounds().getCopy();
        final Rectangle originalClip = new Rectangle();
        graphics.getClip(originalClip);
        
        if (this.isOpaque()) {
            final Path path = new Path(Display.getCurrent());
            final Rectangle r1 = new Rectangle(aBounds.x, aBounds.y, aBounds.width - FOLDSIZE, aBounds.height);
            final Rectangle r2 = new Rectangle(aBounds.x + aBounds.width - FOLDSIZE, aBounds.y + FOLDSIZE, FOLDSIZE, aBounds.height - FOLDSIZE);
            path.addRectangle(r1.x, r1.y, r1.width, r1.height);
            path.addRectangle(r2.x, r2.y, r2.width, r2.height);
            graphics.setClip(path);
            // now let the BoxFigure draw the gradient
            super.paintFigure(graphics);
            graphics.setClip(originalClip);
            path.dispose();
        
            
            // paint the fold background
            final PointList polygon = NoteFigure.foldTemplate.getCopy();
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
        final Rectangle r = this.scrollPane.getBounds();
        graphics.drawLine(r.x + 4, r.y, r.x + r.width - 4, r.y);
    }

    /**
     * Note scroll pane layout.
     * <p>
     * <li>Its minimum size is its preferred size.
     * <li>Tries to have a 2/1 width/height ratio.
     */
    @objid ("818d8672-1dec-11e2-8cad-001ec947c8cc")
    private static final class NoteLayout extends ToolbarLayoutWithGrab {
        @objid ("818fe875-1dec-11e2-8cad-001ec947c8cc")
        NoteLayout() {
            super(false);
        }

        /**
         * Called by {@link #layout(IFigure)}.
         * <p>
         * Should not resize automatically the note but super.calculatePreferredSize() returns the ideal size and it is
         * not wanted.
         */
        @objid ("818fe877-1dec-11e2-8cad-001ec947c8cc")
        @Override
        protected Dimension calculatePreferredSize(final IFigure container, final int wHint, final int hHint) {
            // Called by layout().
            // Should not resize automatically the note.
            // super.calculatePreferredSize() returns the ideal size and it is not wanted
            Dimension ret = container.getSize();
            if (ret.width == 0)
                ret.width = -1;
            return calculateMinSize(container, Math.max(wHint, ret.width), Math.max(ret.height, hHint));
        }

        /**
         * Called by 'fit to content' command.
         * <p>
         * But super.calculateMinimumSize() does not take the content into account.
         */
        @objid ("818fe887-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Dimension calculateMinimumSize(final IFigure container, final int wHint, final int hHint) {
            // Called by 'fit to content' command.
            // But super.calculateMinimumSize() does not take the content into account.
            return calculateIdealSize(container, wHint, hHint);
        }

        /**
         * Calculate the minimum size a note should be.
         * @param container the note figure
         * @param wHint the width hint (the desired width of the container)
         * @param hHint the height hint (the desired height of the container)
         * @return
         */
        @objid ("818fe897-1dec-11e2-8cad-001ec947c8cc")
        private Dimension calculateMinSize(final IFigure container, final int wHint, final int hHint) {
            Dimension ret = ((NoteFigure) container).title.getPreferredSize(wHint, -1).getCopy();
            ret.width += FOLDSIZE * 2;
            
            ret.union(super.calculateMinimumSize(container, wHint, hHint));
            
            //if (ret.width < 60)
            //    ret.width = 60;
            
            if (ret.height < 60)
                ret.height = 60;
            
            if (ret.width / ret.height > 4) {
                ret = super.calculateMinimumSize(container, ret.height * 4, hHint);
            }
            return ret;
        }

        /**
         * Compute the ideal size of the note.
         * @param container the note figure
         * @param wHint the width hint (the desired width of the container)
         * @param hHint the height hint (the desired height of the container)
         * @return the ideal size.
         */
        @objid ("818fe8a6-1dec-11e2-8cad-001ec947c8cc")
        private Dimension calculateIdealSize(final IFigure container, final int wHint, final int hHint) {
            Dimension ret = super.calculatePreferredSize(container, wHint, hHint);
            
            if (ret.height < 60)
                ret.height = 60;
            
            if (ret.width < 40)
                ret.width = 40;
            
            if (ret.width / ret.height > 4) {
                ret = super.calculatePreferredSize(container, ret.height * 4, hHint);
            }
            return ret;
        }

    }

    /**
     * Transparent {@link ScrollPane}.
     * <p>
     * Exists because {@link ScrollPane#isOpaque()} always returns <code>true</code>.
     */
    @objid ("81924ad9-1dec-11e2-8cad-001ec947c8cc")
    private static final class TransparentScrollPane extends ScrollPane {
        @objid ("81924ade-1dec-11e2-8cad-001ec947c8cc")
        public TransparentScrollPane() {
            super();
        }

        @objid ("81924ae0-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public boolean isOpaque() {
            // Override because ScrollPane.isOpaque()
            // always return true.
            return false;
        }

    }

}
