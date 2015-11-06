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
                                    

package org.modelio.core.ui.ktable.types.image;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import de.kupzog.ktable.KTableModel;
import de.kupzog.ktable.SWTX;
import de.kupzog.ktable.renderers.DefaultCellRenderer;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Text;
import org.modelio.metamodel.uml.infrastructure.Element;

/**
 * This renderer displays a single element as its name followed by a 'from'
 * clause. The 'from' clause shows the element owner's name.
 */
@objid ("8ddcdbfa-c068-11e1-8c0a-002564c97630")
public class ImageCellRenderer extends DefaultCellRenderer {
    @objid ("8ddcdbfc-c068-11e1-8c0a-002564c97630")
    protected Text m_text;

    @objid ("8ddcdbfd-c068-11e1-8c0a-002564c97630")
    private Image image = null;

    @objid ("8ddcdbfe-c068-11e1-8c0a-002564c97630")
    public ImageCellRenderer(int style, Class<? extends Element> metaclass, String dependencyName) {
        super(style);
        
        // TODO CHM image
        //this.image = StandardImageService.getMetaclassImage(dependencyName, metaclass);
    }

    /**
     * Creates an ElementCellRenderer.
     * <p>
     * 
     * <p>
     */
    @objid ("8dde6265-c068-11e1-8c0a-002564c97630")
    @Override
    public int getOptimalWidth(GC gc, int col, int row, Object content, boolean fixed, KTableModel model) {
        return SWTX.getCachedStringExtent(gc, content.toString()).x + 8;
    }

    /**
     * A default implementation that paints cells in a way that is more or less
     * Excel-like. Only the cell with focus looks very different.
     * @see de.kupzog.ktable.KTableCellRenderer#drawCell(GC, Rectangle, int,
     * int, Object, boolean, boolean, boolean, KTableModel)
     */
    @objid ("8dde6271-c068-11e1-8c0a-002564c97630")
    @Override
    public void drawCell(GC gc, final Rectangle initialRect, int col, int row, Object content, boolean focus, boolean fixed, boolean clicked, KTableModel model) {
        Rectangle rect = initialRect;
        
        applyFont(gc);
        
        String text = (String)content;
        
        // draw focus sign:
        if (focus && (this.m_Style & INDICATION_FOCUS) != 0) {
            // draw content:
            rect = drawDefaultSolidCellLine(gc, rect, COLOR_LINE_LIGHTGRAY, COLOR_LINE_LIGHTGRAY);
            drawCellContent(gc, rect, text, null, getForeground(), COLOR_BGFOCUS);
            gc.drawFocus(rect.x, rect.y, rect.width, rect.height);
        
        } else if (focus && (this.m_Style & INDICATION_FOCUS_ROW) != 0) {
            rect = drawDefaultSolidCellLine(gc, rect, COLOR_BGROWFOCUS, COLOR_BGROWFOCUS);
            // draw content:
            drawCellContent(gc, rect, text, null, COLOR_FGROWFOCUS, COLOR_BGROWFOCUS);
        
        } else {
            rect = drawDefaultSolidCellLine(gc, rect, COLOR_LINE_LIGHTGRAY, COLOR_LINE_LIGHTGRAY);
            // draw content:
            drawCellContent(gc, rect, text, null, getForeground(), getBackground());
        }
        
        if ((this.m_Style & INDICATION_COMMENT) != 0)
            drawCommentSign(gc, rect);
        
        drawImage(gc, rect);
        resetFont(gc);
    }

    /**
     * @param value If true, the comment sign is painted. Else it is omitted.
     */
    @objid ("8dde627f-c068-11e1-8c0a-002564c97630")
    public void setCommentIndication(boolean value) {
        if (value)
            this.m_Style = this.m_Style | INDICATION_COMMENT;
        else
            this.m_Style = this.m_Style & ~INDICATION_COMMENT;
    }

    /**
     * Paints a sign that a comment is present in the right upper corner!
     * @param gc The GC to use when painting.
     * @param rect The cell area where content should be added.
     */
    @objid ("8dde6283-c068-11e1-8c0a-002564c97630")
    protected final void drawImage(GC gc, Rectangle rect) {
        if (this.image != null) {
            Rectangle imageRect = this.image.getBounds();
            gc.drawImage(this.image, rect.x + rect.width - imageRect.width, rect.y);
        }
    }

}
