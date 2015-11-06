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
                                    

package org.modelio.core.ui.nattable.editors;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.convert.IDisplayConverter;
import org.eclipse.nebula.widgets.nattable.layer.cell.CellDisplayConversionUtils;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;
import org.eclipse.nebula.widgets.nattable.painter.cell.AbstractCellPainter;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.CellStyleUtil;
import org.eclipse.nebula.widgets.nattable.style.IStyle;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.modelio.core.ui.images.MetamodelImageService;
import org.modelio.metamodel.Metamodel;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Implements a NatTable cell painter for Model elements represented by a MRef
 * instance.<br/>
 * The painter displays the metaclass icon and the element name.<br/>
 * Null MRef , null metaclasses, null names are supported.
 * 
 * @author phv
 */
@objid ("cedf4bdf-a51e-4106-bbac-f378818e4758")
public class ElementPainter extends AbstractCellPainter {
    @objid ("729d9383-6be4-4d9a-9dbd-39898dabbd1b")
     boolean calculate = true;

    @objid ("eb274e49-a13d-4b88-b604-fcf876f92705")
    public static final String DOT3 = "...";

    @objid ("4916d4b3-614d-4ea2-90ab-4a0b31dd75d1")
    @Override
    public void paintCell(ILayerCell cell, GC gc, Rectangle bounds, IConfigRegistry configRegistry) {
        final IStyle cellStyle = CellStyleUtil.getCellStyle(cell, configRegistry);
        
        // Paint background
        paintBackground(cell, gc, bounds, configRegistry);
        
        // Paint Icon
        final Image image = getImage(cell);
        if (image != null) {
            final Rectangle imageBounds = image.getBounds();
        
            gc.drawImage(image, bounds.x + CellStyleUtil.getHorizontalAlignmentPadding(cellStyle, bounds, imageBounds.width),
                    bounds.y + CellStyleUtil.getVerticalAlignmentPadding(cellStyle, bounds, imageBounds.height));
        
            bounds.x += imageBounds.width + 1;
            bounds.width -= imageBounds.width + 1;
        }
        
        // Paint Text
        final String text = convertDataType(cell, configRegistry);
        if (text != null) {
            paintText(cellStyle, gc, bounds, text);
        }
    }

    @objid ("bdbbe086-3f4d-4ece-86c4-6a5ddb21c3d5")
    private void paintText(IStyle cellStyle, GC gc, Rectangle bounds, final String text) {
        String displayedText = text;
        setupGCFromConfig(gc, cellStyle);
        
        final Rectangle originalClipping = gc.getClipping();
        gc.setClipping(bounds.intersection(originalClipping));
        
        final int fontHeight = gc.getFontMetrics().getHeight();
        final int contentHeight = fontHeight * 1 /* one line */;
        final int contentWidth = Math.min(gc.textExtent(text).x, bounds.width);
        
        if (gc.textExtent(displayedText).x > bounds.width) {
            displayedText = truncateText(text, gc, bounds.width);
        }
        
        final int spacing = 20;
        
        gc.drawText(text, bounds.x + CellStyleUtil.getHorizontalAlignmentPadding(cellStyle, bounds, contentWidth + spacing),
                bounds.y + CellStyleUtil.getVerticalAlignmentPadding(cellStyle, bounds, contentHeight + spacing),
                SWT.DRAW_TRANSPARENT | SWT.DRAW_DELIMITER);
        
        final boolean underline = true;
        
        if (underline) {
            // start x of line = start x of text
            final int x = bounds.x + CellStyleUtil.getHorizontalAlignmentPadding(cellStyle, bounds, contentWidth + spacing);
            // y = start y of text
            final int y = bounds.y + CellStyleUtil.getVerticalAlignmentPadding(cellStyle, bounds, contentHeight + spacing);
        
            // check and draw underline and strikethrough separately so it is
            // possible to combine both
            if (underline) {
                // y = start y of text + font height
                // - half of the font descent so the underline is between the
                // baseline and the bottom
                final int underlineY = y + fontHeight - (gc.getFontMetrics().getDescent() / 2);
                gc.drawLine(x, underlineY, x + gc.textExtent(text).x, underlineY);
            }
        }
        gc.setClipping(originalClipping);
    }

    @objid ("dc597f0a-ee92-44af-8f37-b26f4bba7def")
    protected Color getBackgroundColour(ILayerCell cell, IConfigRegistry configRegistry) {
        return CellStyleUtil.getCellStyle(cell, configRegistry).getAttributeValue(CellStyleAttributes.BACKGROUND_COLOR);
    }

    @objid ("b2833aa0-3335-46b9-9183-5900c67f2400")
    protected void paintBackground(ILayerCell cell, GC gc, Rectangle bounds, IConfigRegistry configRegistry) {
        final Color backgroundColor = getBackgroundColour(cell, configRegistry);
        if (backgroundColor != null) {
            final Color originalBackground = gc.getBackground();
            gc.setBackground(backgroundColor);
            gc.fillRectangle(bounds);
            gc.setBackground(originalBackground);
        }
    }

    @objid ("368edcd5-9c67-4326-8356-757f37b3c106")
    @Override
    public int getPreferredWidth(ILayerCell cell, GC gc, IConfigRegistry configRegistry) {
        final Image image = getImage(cell);
        final int imageWidth = (image != null) ? image.getBounds().width : 0;
        
        setupGCFromConfig(gc, CellStyleUtil.getCellStyle(cell, configRegistry));
        final int textWidth = gc.textExtent(convertDataType(cell, configRegistry)).x;
        return Math.max(imageWidth, textWidth);
    }

    @objid ("d42708f7-7c8d-4640-8885-ab25c04e6230")
    @Override
    public int getPreferredHeight(ILayerCell cell, GC gc, IConfigRegistry configRegistry) {
        final Image image = getImage(cell);
        final int imageHeight = (image != null) ? image.getBounds().height : 0;
        
        setupGCFromConfig(gc, CellStyleUtil.getCellStyle(cell, configRegistry));
        final int textHeight = gc.textExtent(convertDataType(cell, configRegistry)).y;
        return Math.max(imageHeight, textHeight);
    }

    @objid ("7a4606e2-194d-4a76-bb2e-d793acce8e3f")
    private Image getImage(ILayerCell cell) {
        final MRef mref = (MRef) cell.getDataValue();
        if (mref != null) {
            if (mref.mc != null) {
                if (Metamodel.getMClass(mref.mc) != null) {
                    return MetamodelImageService.getIcon(Metamodel.getMClass(mref.mc));
                }
            }
        }
        return null;
    }

    /**
     * Convert the data value of the cell using the {@link IDisplayConverter}
     * from the {@link IConfigRegistry}
     */
    @objid ("05c9f673-02d3-4c67-851e-8607414f1856")
    protected String convertDataType(ILayerCell cell, IConfigRegistry configRegistry) {
        return CellDisplayConversionUtils.convertDataType(cell, configRegistry);
    }

    /**
     * Setup the GC by the values defined in the given cell style.
     * @param gc
     * @param cellStyle
     */
    @objid ("52142198-062e-464e-b85f-53c8b3116bed")
    public void setupGCFromConfig(GC gc, IStyle cellStyle) {
        final Color fg = cellStyle.getAttributeValue(CellStyleAttributes.FOREGROUND_COLOR);
        final Color bg = cellStyle.getAttributeValue(CellStyleAttributes.BACKGROUND_COLOR);
        final Font font = cellStyle.getAttributeValue(CellStyleAttributes.FONT);
        
        gc.setAntialias(GUIHelper.DEFAULT_ANTIALIAS);
        gc.setTextAntialias(GUIHelper.DEFAULT_TEXT_ANTIALIAS);
        gc.setFont(font);
        gc.setForeground(fg != null ? fg : GUIHelper.COLOR_LIST_FOREGROUND);
        gc.setBackground(bg != null ? bg : GUIHelper.COLOR_LIST_BACKGROUND);
    }

    /**
     * Checks if the given text is bigger than the available space. If not the
     * given text is simply returned without modification. If the text does not
     * fit into the available space, it will be modified by cutting and adding
     * three dots.
     * @param text the text to compute
     * @param gc the current GC
     * @param availableLength the available space
     * @return the modified text if it is bigger than the available space or the
     * text as it was given if it fits into the available space
     */
    @objid ("a0ab28f6-b5e6-4fbd-8aa0-58ccda4e80e0")
    private String truncateText(String text, GC gc, int availableLength) {
        String trialText = text;
        int textWidth = gc.textExtent(trialText).x;
        
        while (textWidth > availableLength) {
        
            // try an optimization: estimate average char width and adjust
            // accordingly
            final double avgCharWidth = textWidth / trialText.length();
            final int nbExtraChars = 1 + (int) ((textWidth - availableLength) / avgCharWidth);
        
            final int newLength = trialText.length() - nbExtraChars;
            if (newLength > 0) {
                trialText = trialText.substring(0, newLength);
                textWidth = gc.textExtent(trialText + DOT3).x;
            }
        }
        return trialText + DOT3;
    }

}
