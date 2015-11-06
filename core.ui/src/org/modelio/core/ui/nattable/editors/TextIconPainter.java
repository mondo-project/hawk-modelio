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
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.convert.IDisplayConverter;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;
import org.eclipse.nebula.widgets.nattable.painter.cell.AbstractCellPainter;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.CellStyleUtil;
import org.eclipse.nebula.widgets.nattable.style.HorizontalAlignmentEnum;
import org.eclipse.nebula.widgets.nattable.style.IStyle;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Implements a NatTable cell painter for Model elements represented by a MRef
 * instance.<br/>
 * The painter displays the metaclass icon and the element name.<br/>
 * Null MRef , null metaclasses, null names are supported.
 * 
 * @author phv
 */
@objid ("f3dd9ca9-97e8-48bd-bcdc-2f9a91438f2d")
public class TextIconPainter extends AbstractCellPainter {
    @objid ("84e5a2bf-7524-4cdd-96e5-a1f1436422e2")
     boolean calculate = true;

    @objid ("82ccd576-bf1d-4b53-b5a3-87d9749fe82f")
    private boolean underline;

    @objid ("c5fd6a26-2809-45ae-9947-5bbf05a355ab")
    public static final String DOT3 = "...";

    @objid ("c6db16b3-cf29-4f5a-bfec-94d935ac54cd")
    public TextIconPainter() {
        this(false);
    }

    @objid ("cd3ffb0b-be4e-4b52-88ec-b11ed4007c6f")
    public TextIconPainter(boolean underline) {
        this.underline = underline;
    }

    @objid ("d1fd988b-0aad-42c0-a62e-cc8a4eff4fcf")
    @Override
    public void paintCell(ILayerCell cell, GC gc, Rectangle bounds, IConfigRegistry configRegistry) {
        final IStyle cellStyle = CellStyleUtil.getCellStyle(cell, configRegistry);
        
        // Paint background
        paintBackground(cell, gc, bounds, configRegistry);
        
        final TextIcon textIcon = convertDataType(cell, configRegistry);
        final String text = textIcon.text;
        final Image icon = textIcon.icon;
        final Rectangle imageBounds = icon != null ? icon.getBounds() : new Rectangle(0, 0, 0, 0);
        
        // Compute x padding
        String displayedText = text;
        setupGCFromConfig(gc, cellStyle);
        
        int fontHeight = gc.getFontMetrics().getHeight();
        int contentHeight = fontHeight * 1 /* one line */;
        
        if (gc.textExtent(displayedText).x > bounds.width - imageBounds.width) {
            displayedText = truncateText(text, gc, bounds.width - imageBounds.width);
        }
        
        int x = bounds.x + CellStyleUtil.getHorizontalAlignmentPadding(cellStyle, bounds, imageBounds.width + gc.textExtent(displayedText).x);
        int y = bounds.y + CellStyleUtil.getVerticalAlignmentPadding(cellStyle, bounds, imageBounds.height);
        
        // Paint Icon
        if (icon != null) {
            gc.drawImage(icon, x, y);
        }
        
        // Paint Text
        x += imageBounds.width + 3;
        y = bounds.y + CellStyleUtil.getVerticalAlignmentPadding(cellStyle, bounds, contentHeight);
        bounds.width -= imageBounds.width + 1;
        
        // final Rectangle originalClipping = gc.getClipping();
        // gc.setClipping(bounds.intersection(originalClipping));
        
        gc.drawText(displayedText, x, y, SWT.DRAW_TRANSPARENT | SWT.DRAW_DELIMITER);
        if (this.underline) {
            // check and draw underline and strikethrough separately so it is
            // possible to combine both
            if (this.underline) {
                // y = start y of text + font height
                // - half of the font descent so the underline is between the
                // baseline and the bottom
                final int underlineY = y + fontHeight - (gc.getFontMetrics().getDescent() / 2);
                gc.drawLine(x, underlineY, x + gc.textExtent(text).x, underlineY);
            }
        }
        //
    }

    @objid ("7005659e-709e-417d-b461-38a9576746da")
    protected Color getBackgroundColour(ILayerCell cell, IConfigRegistry configRegistry) {
        return CellStyleUtil.getCellStyle(cell, configRegistry).getAttributeValue(CellStyleAttributes.BACKGROUND_COLOR);
    }

    @objid ("7a395f89-e0aa-4560-b999-bf5086931aba")
    protected void paintBackground(ILayerCell cell, GC gc, Rectangle bounds, IConfigRegistry configRegistry) {
        final Color backgroundColor = getBackgroundColour(cell, configRegistry);
        if (backgroundColor != null) {
            final Color originalBackground = gc.getBackground();
            gc.setBackground(backgroundColor);
            gc.fillRectangle(bounds);
            gc.setBackground(originalBackground);
        }
    }

    @objid ("70183a80-595e-4fcd-a12b-8d157b83cd79")
    @Override
    public int getPreferredWidth(ILayerCell cell, GC gc, IConfigRegistry configRegistry) {
        final IStyle cellStyle = CellStyleUtil.getCellStyle(cell, configRegistry);
        setupGCFromConfig(gc, cellStyle);
        
        final Image image = getImage(cell, configRegistry);
        final int imageWidth = (image != null) ? image.getBounds().width : 0;
        
        final int textWidth = gc.textExtent(convertDataType(cell, configRegistry).text).x;
        
        int spacing = 16;
        HorizontalAlignmentEnum horizontalAlignment = cellStyle.getAttributeValue(CellStyleAttributes.HORIZONTAL_ALIGNMENT);
        if (horizontalAlignment == HorizontalAlignmentEnum.CENTER) {
            spacing *= 2;
        }
        return imageWidth + textWidth + 3 + spacing;
    }

    @objid ("7eed7c00-04a0-43e3-a6ed-fc79a2038c72")
    private Image getImage(ILayerCell cell, IConfigRegistry configRegistry) {
        final TextIcon textIcon = convertDataType(cell, configRegistry);
        return textIcon.icon;
    }

    @objid ("7b4cf244-3406-4df0-8614-2e6a44b8c01b")
    @Override
    public int getPreferredHeight(ILayerCell cell, GC gc, IConfigRegistry configRegistry) {
        final Image image = getImage(cell, configRegistry);
        final int imageHeight = (image != null) ? image.getBounds().height : 0;
        
        setupGCFromConfig(gc, CellStyleUtil.getCellStyle(cell, configRegistry));
        int textHeight = gc.textExtent(convertDataType(cell, configRegistry).text).y;
        if (this.underline) {
            textHeight += (gc.getFontMetrics().getDescent() / 2) + 2;
        }
        return Math.max(imageHeight, textHeight);
    }

    /**
     * Convert the data value of the cell using the {@link IDisplayConverter}
     * from the {@link IConfigRegistry}
     */
    @objid ("89f1b72e-d628-4790-b85d-3752a311fd58")
    protected TextIcon convertDataType(ILayerCell cell, IConfigRegistry configRegistry) {
        Object canonicalValue = cell.getDataValue();
        Object displayValue;
        
        IDisplayConverter displayConverter = configRegistry.getConfigAttribute(
                CellConfigAttributes.DISPLAY_CONVERTER,
                cell.getDisplayMode(),
                cell.getConfigLabels().getLabels());
        
        if (displayConverter != null) {
            displayValue = displayConverter.canonicalToDisplayValue(cell, configRegistry, canonicalValue);
        } else {
            displayValue = canonicalValue;
        }
        
        if (displayValue instanceof TextIcon) {
            return (TextIcon) displayValue;
        } else {
            return new TextIcon(String.valueOf(displayValue), null); //$NON-NLS-1$
        }
    }

    /**
     * Setup the GC by the values defined in the given cell style.
     * @param gc
     * @param cellStyle
     */
    @objid ("ff0d7e4e-0cae-45a8-8089-e20e878a5040")
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
    @objid ("68d0a9b7-7c6c-4bc3-bd22-a79c9f6cbe59")
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
            } else {
                break;
            }
        }
        return trialText + DOT3;
    }

}
