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
                                    

package org.modelio.diagram.elements.common.text;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.modelio.diagram.elements.core.figures.GradientFigure;
import org.modelio.diagram.elements.core.figures.ToolbarLayoutWithGrab;

/**
 * Multi-line text figure.
 * <p>
 * 
 * @author cmarin
 */
@objid ("7f2d8e82-1dec-11e2-8cad-001ec947c8cc")
public class MultilineTextFigure extends GradientFigure {
    @objid ("eae8d9e4-7cb2-43e4-b66e-dfc83f7ea925")
    private FlowPage contents;

    @objid ("e08b429c-2896-478d-83d8-1034d963315e")
    private TextFlow contentsText;

    /**
     * Default constructor.
     * @param text The initial text content.
     */
    @objid ("7f2d8e8a-1dec-11e2-8cad-001ec947c8cc")
    public MultilineTextFigure(String text) {
        setLayoutManager(new MultilineTextLayout());
        
        // In the scroll: the note text, a FlowPage + a TextFlow
        // 
        this.contents = new FlowPage();
        this.contentsText = new TextFlow();
        this.contentsText.setText(text);
        this.contents.add(this.contentsText);
        //this.contents.setBorder(new MarginBorder(2));
        this.contents.setOpaque(false);
        this.contents.setHorizontalAligment(PositionConstants.LEFT);
        
        add(this.contents, BorderLayout.CENTER);
    }

    @objid ("7f2d8e8e-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Color getTextColor() {
        return this.contentsText.getForegroundColor();
    }

    @objid ("7f2d8e93-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Font getTextFont() {
        return this.contentsText.getFont();
    }

    /**
     * Sets the horizontal alignment of the text. Valid values are:
     * <UL>
     * <LI>{@link PositionConstants#NONE NONE} - (default) Alignment is inherited from parent. If a parent is not found
     * then LEFT is used.</LI>
     * <LI>{@link PositionConstants#LEFT} - Alignment is with leading edge</LI>
     * <LI>{@link PositionConstants#RIGHT} - Alignment is with trailing edge</LI>
     * <LI>{@link PositionConstants#CENTER}</LI>
     * <LI>{@link PositionConstants#ALWAYS_LEFT} - Left, irrespective of orientation</LI>
     * <LI>{@link PositionConstants#ALWAYS_RIGHT} - Right, irrespective of orientation</LI>
     * </UL>
     * @param value the alignment
     */
    @objid ("7f2d8e98-1dec-11e2-8cad-001ec947c8cc")
    public void setTextAlignment(int value) {
        this.contents.setHorizontalAligment(value);
    }

    @objid ("7f2d8e9c-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setTextColor(Color textColor) {
        this.contentsText.setForegroundColor(textColor);
    }

    @objid ("7f2d8ea0-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setTextFont(Font textFont) {
        this.contentsText.setFont(textFont);
    }

    /**
     * Set the displayed text.
     * @param text The text to display.
     */
    @objid ("7f2d8ea4-1dec-11e2-8cad-001ec947c8cc")
    public void setText(String text) {
        this.contentsText.setText(text);
    }

    /**
     * Note scroll pane layout.
     * <p>
     * <li>Its minimum size is its preferred size.
     * <li>Tries to have a 2/1 width/height ratio.
     */
    @objid ("7f2d8ea8-1dec-11e2-8cad-001ec947c8cc")
    private static final class MultilineTextLayout extends ToolbarLayoutWithGrab {
        @objid ("7f2d8eab-1dec-11e2-8cad-001ec947c8cc")
        MultilineTextLayout() {
            super(false);
        }

        /**
         * Called by {@link #layout(IFigure)}.
         * <p>
         * Should not resize automatically the note but super.calculatePreferredSize() returns the ideal size and it is
         * not wanted.
         */
        @objid ("7f2d8ead-1dec-11e2-8cad-001ec947c8cc")
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
        @objid ("7f2d8ebd-1dec-11e2-8cad-001ec947c8cc")
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
        @objid ("7f2ff0d2-1dec-11e2-8cad-001ec947c8cc")
        private Dimension calculateMinSize(final IFigure container, final int wHint, final int hHint) {
            Dimension ret = super.calculateMinimumSize(container, wHint, hHint);
            
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
        @objid ("7f2ff0e1-1dec-11e2-8cad-001ec947c8cc")
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

}
