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
import org.eclipse.nebula.widgets.nattable.painter.cell.GraphicsUtils;
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
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

/**
 * Implements a NatTable cell painter for Model elements represented by a MRef
 * instance.<br/>
 * The painter displays the metaclass icon and the element name.<br/>
 * Null MRef , null metaclasses, null names are supported.
 * 
 * @author phv
 */
@objid ("19d667c4-c54a-437d-aa2b-b8b0612214db")
public class VerticalTextIconPainter extends AbstractCellPainter {
    @objid ("6f19ad0b-d736-4163-b051-60911f40e8ed")
     boolean calculate = true;

    @objid ("e59999f7-f99f-43ff-872f-5ea3b3e43967")
    private boolean underline;

    @objid ("3587614a-a3d9-4518-9493-e2af1b4cd9b9")
    public static final String DOT3 = "...";

    @objid ("5d745960-a64e-427f-b3b0-361be347c8df")
    public VerticalTextIconPainter() {
        this(false);
    }

    @objid ("eca61429-b4cc-45cb-904c-7266f4c0dd68")
    public VerticalTextIconPainter(boolean underline) {
        this.underline = underline;
    }

    @objid ("fa6708e1-c585-4c87-b128-6982cd1e0bfe")
    @Override
    public void paintCell(ILayerCell cell, GC gc, Rectangle bounds, IConfigRegistry configRegistry) {
        final IStyle cellStyle = CellStyleUtil.getCellStyle(cell, configRegistry);
        
        // Paint background
        //paintBackground(cell, gc, bounds, configRegistry);
        
        final TextIcon textIcon = convertDataType(cell, configRegistry);
        final String text = textIcon.text;
        final Image icon = textIcon.icon;
        final Rectangle imageBounds = icon != null ? icon.getBounds() : new Rectangle(0, 0, 0, 0);
        
        // Compute x padding
        String displayedText = text;
        setupGCFromConfig(gc, cellStyle);
        
        int fontHeight = gc.getFontMetrics().getHeight();
        int contentHeight = fontHeight * 1 /* one line */;
        
        if (gc.textExtent(displayedText).x > bounds.height - imageBounds.height - 3) {
            displayedText = truncateText(text, gc, bounds.height - imageBounds.height - 3);
        }
        
        int x = bounds.x + CellStyleUtil.getHorizontalAlignmentPadding(cellStyle, bounds, imageBounds.width);
        int y = bounds.y + bounds.height - imageBounds.height - 3 - CellStyleUtil.getVerticalAlignmentPadding(cellStyle, bounds, imageBounds.height + gc.textExtent(displayedText).x);
        
        // Paint Icon
        if (icon != null) {
            drawVerticalImage(icon, x, y, gc, SWT.UP);
        }
        
        // Paint Text
        x = bounds.x + CellStyleUtil.getHorizontalAlignmentPadding(cellStyle, bounds, contentHeight);
        y = y - gc.textExtent(displayedText).x - 3;
        bounds.width -= imageBounds.width + 1;
        
        // final Rectangle originalClipping = gc.getClipping();
        // gc.setClipping(bounds.intersection(originalClipping));
        
        GraphicsUtils.drawVerticalText(
                displayedText, 
                x,
                y,
                this.underline,
                false,
                true,
                gc, 
                SWT.UP | SWT.DRAW_TRANSPARENT | SWT.DRAW_DELIMITER);
        
        // gc.setClipping(originalClipping);
    }

    @objid ("ef8efe16-4a18-4916-8dc0-a88f004588ad")
    protected Color getBackgroundColour(ILayerCell cell, IConfigRegistry configRegistry) {
        return CellStyleUtil.getCellStyle(cell, configRegistry).getAttributeValue(CellStyleAttributes.BACKGROUND_COLOR);
    }

    @objid ("933ae799-61dd-4acb-a3dd-3a7c15b6cf39")
    protected void paintBackground(ILayerCell cell, GC gc, Rectangle bounds, IConfigRegistry configRegistry) {
        final Color backgroundColor = getBackgroundColour(cell, configRegistry);
        if (backgroundColor != null) {
            final Color originalBackground = gc.getBackground();
            gc.setBackground(backgroundColor);
            gc.fillRectangle(bounds);
            gc.setBackground(originalBackground);
        }
    }

    @objid ("b670dcfa-dbea-4545-8ff9-d91c16f1ac18")
    @Override
    public int getPreferredWidth(ILayerCell cell, GC gc, IConfigRegistry configRegistry) {
        final IStyle cellStyle = CellStyleUtil.getCellStyle(cell, configRegistry);
        setupGCFromConfig(gc, cellStyle);
        
        final Image image = getImage(cell, configRegistry);
        final int imageWidth = (image != null) ? image.getBounds().height : 0;
        
        final int textWidth = gc.textExtent(convertDataType(cell, configRegistry).text).y;
        
        int spacing = 16;
        HorizontalAlignmentEnum horizontalAlignment = cellStyle.getAttributeValue(CellStyleAttributes.HORIZONTAL_ALIGNMENT);
        if (horizontalAlignment == HorizontalAlignmentEnum.CENTER) {
            spacing *= 2;
        }
        return imageWidth + textWidth + 3 + spacing;
    }

    @objid ("b33cc79a-46d9-4b89-909f-a777246aeecc")
    private Image getImage(ILayerCell cell, IConfigRegistry configRegistry) {
        final TextIcon textIcon = convertDataType(cell, configRegistry);
        return textIcon.icon;
    }

    @objid ("70851a8e-29ea-4e16-8fb2-1591c32c1b1d")
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
    @objid ("9ccdd312-b7e2-4e73-85c0-8e8e4397a14e")
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
    @objid ("69d4059f-3e05-4eb5-b476-911db9081c58")
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
    @objid ("88c8a078-738a-4af3-89a9-898c78bd3ea9")
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

    /**
     * Draws an image vertically (rotates plus or minus 90 degrees)
     * <dl>
     * <dt><b>Styles: </b></dt>
     * <dd>UP, DOWN</dd>
     * </dl>
     * @param image the image to draw
     * @param x the x coordinate of the top left corner of the drawing rectangle
     * @param y the y coordinate of the top left corner of the drawing rectangle
     * @param gc the GC on which to draw the image
     * @param style the style (SWT.UP or SWT.DOWN)
     * <p>
     * Note: Only one of the style UP or DOWN may be specified.
     * </p>
     */
    @objid ("6151a654-e132-4fd5-8b8f-ca36342332f2")
    public static void drawVerticalImage(Image image, int x, int y, GC gc, int style) {
        // Get the current display
        Display display = Display.getCurrent();
        if (display == null) SWT.error(SWT.ERROR_THREAD_INVALID_ACCESS);
        
        // Use the image's data to create a rotated image's data
        ImageData sd = image.getImageData();
        ImageData dd = new ImageData(sd.height, sd.width, sd.depth, sd.palette);
        
        // Determine which way to rotate, depending on up or down
        boolean up = (style & SWT.UP) == SWT.UP;
        
        // Run through the horizontal pixels
        for (int sx = 0; sx < sd.width; sx++) {
          // Run through the vertical pixels
          for (int sy = 0; sy < sd.height; sy++) {
            // Determine where to move pixel to in destination image data
            int dx = up ? sy : sd.height - sy - 1;
            int dy = up ? sd.width - sx - 1 : sx;
        
            // Swap the x, y source data to y, x in the destination
            dd.setPixel(dx, dy, sd.getPixel(sx, sy));
            dd.setAlpha(dx, dy, sd.getAlpha(sx, sy));
          }
        }
        
        // Create the vertical image
        Image vertical = new Image(display, dd);
        
        // Draw the vertical image onto the original GC
        gc.drawImage(vertical, x, y);
        
        // Dispose the vertical image
        vertical.dispose();
    }

}
