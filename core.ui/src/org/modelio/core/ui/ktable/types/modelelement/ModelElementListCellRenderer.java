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
                                    

package org.modelio.core.ui.ktable.types.modelelement;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import de.kupzog.ktable.KTableModel;
import de.kupzog.ktable.SWTX;
import de.kupzog.ktable.renderers.DefaultCellRenderer;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.ui.UIImages;

@objid ("8dde6299-c068-11e1-8c0a-002564c97630")
public class ModelElementListCellRenderer extends DefaultCellRenderer {
    @objid ("a54e4329-c068-11e1-8c0a-002564c97630")
    private static String RENDERER_LIGHTBULB = "renderer.lightbulb";

    @objid ("8dde629a-c068-11e1-8c0a-002564c97630")
    private Image indicatorImage = null;

    @objid ("8dde629c-c068-11e1-8c0a-002564c97630")
    public ModelElementListCellRenderer(int style) {
        super(style);
        this.indicatorImage = UIImages.INDICATOR;
    }

    /**
     * Creates an ElementCellRenderer.
     * <p>
     * 
     * <p>
     */
    @objid ("8dde62a0-c068-11e1-8c0a-002564c97630")
    @Override
    public int getOptimalWidth(GC gc, int col, int row, Object content, boolean fixed, KTableModel model) {
        return SWTX.getCachedStringExtent(gc, content.toString()).x + 8;
    }

    /**
     * A default implementation that paints cells in a way that is more or less Excel-like. Only the cell with focus
     * looks very different.
     * @see de.kupzog.ktable.KTableCellRenderer#drawCell(GC, Rectangle, int, int, Object, boolean, boolean, boolean,
     * KTableModel)
     */
    @objid ("8ddfe90d-c068-11e1-8c0a-002564c97630")
    @Override
    public void drawCell(GC gc, final Rectangle initialRect, int col, int row, Object content, boolean focus, boolean fixed, boolean clicked, KTableModel model) {
        Rectangle rect = initialRect;
        
        applyFont(gc);
        
        String text;
        
        if (content != null) {
            ModelElement me = (ModelElement) content;
        
            ModelElementLabelService labelService = new ModelElementLabelService();
            text = labelService.getLabel(me);
        } else {
            text = "null";
        }
        
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
        
        drawIndicator(gc, rect);
        resetFont(gc);
    }

    /**
     * @param value If true, the comment sign is painted. Else it is omitted.
     */
    @objid ("8ddfe91b-c068-11e1-8c0a-002564c97630")
    public void setCommentIndication(boolean value) {
        if (value)
            this.m_Style = this.m_Style | INDICATION_COMMENT;
        else
            this.m_Style = this.m_Style & ~INDICATION_COMMENT;
    }

    @objid ("8ddfe91f-c068-11e1-8c0a-002564c97630")
    protected final void drawIndicator(GC gc, Rectangle rect) {
        if (this.indicatorImage != null) {
            Rectangle imageRect = this.indicatorImage.getBounds();
            gc.drawImage(this.indicatorImage, rect.x + rect.width - imageRect.width, rect.y);
        }
    }

    @objid ("8ddfe923-c068-11e1-8c0a-002564c97630")
    @Override
    protected void finalize() throws Throwable {
        this.indicatorImage = null;
        super.finalize();
    }

}
