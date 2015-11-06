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
                                    

package org.modelio.core.ui.ktable.types.text;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import de.kupzog.ktable.KTableModel;
import de.kupzog.ktable.renderers.TextCellRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.modelio.core.ui.plugin.CoreUi;

@objid ("8de603be-c068-11e1-8c0a-002564c97630")
public class TextNoValueCellRenderer extends TextCellRenderer {
    @objid ("8de78a26-c068-11e1-8c0a-002564c97630")
    @Override
    public int getOptimalWidth(GC gc, int col, int row, Object content, boolean fixed, KTableModel model) {
        String[] contentArray = (String[])content;
        StringBuffer text = new StringBuffer();
        
        for (int i = 0; i < contentArray.length; i++) {
            if (i > 0) {
                text.append(", ");
            }
            text.append(contentArray[i]);
        }
        return super.getOptimalWidth(gc, col, row, text.toString(), fixed, model);
    }

    /**
     * A default implementation that paints cells in a way that is more or less
     * Excel-like. Only the cell with focus looks very different.
     * @see de.kupzog.ktable.KTableCellRenderer#drawCell(GC, Rectangle, int,
     * int, Object, boolean, boolean, boolean, KTableModel)
     */
    @objid ("8de78a32-c068-11e1-8c0a-002564c97630")
    @Override
    public void drawCell(GC gc, final Rectangle initialRect, int col, int row, Object content, boolean focus, boolean fixed, boolean clicked, KTableModel model) {
        Rectangle rect = initialRect;
        String contentString = (String)content;
        if (contentString != null && contentString.length() == 0) {
            applyFont(gc);
        
            String text = CoreUi.I18N.getString("KTable.NoValue");
        
            // draw focus sign:
            if (focus && (this.m_Style & INDICATION_FOCUS)!=0) {
                // draw content:
                rect = drawDefaultSolidCellLine(gc, rect, COLOR_LINE_LIGHTGRAY, COLOR_LINE_LIGHTGRAY);
                drawCellContent(gc, rect, text, null, gc.getDevice().getSystemColor(SWT.COLOR_GRAY), COLOR_BGFOCUS);
                gc.drawFocus(rect.x, rect.y, rect.width, rect.height);
        
            } else if (focus && (this.m_Style & INDICATION_FOCUS_ROW)!=0) {
                rect = drawDefaultSolidCellLine(gc, rect, COLOR_BGROWFOCUS, COLOR_BGROWFOCUS);
                // draw content:
                drawCellContent(gc, rect, text, null, gc.getDevice().getSystemColor(SWT.COLOR_GRAY), COLOR_BGROWFOCUS);
        
            } else {
                rect = drawDefaultSolidCellLine(gc, rect, COLOR_LINE_LIGHTGRAY, COLOR_LINE_LIGHTGRAY);
                // draw content:
                drawCellContent(gc, rect, text, null, gc.getDevice().getSystemColor(SWT.COLOR_GRAY), getBackground());
            }
        
            if ((this.m_Style & INDICATION_COMMENT)!=0) {
                drawCommentSign(gc, rect);
            }
        
            resetFont(gc);
        } else {
            super.drawCell(gc, rect, col, row, content.toString(), focus, fixed, clicked, model);
        }
    }

    @objid ("8de78a40-c068-11e1-8c0a-002564c97630")
    public TextNoValueCellRenderer(int style) {
        super(style);
    }

}
