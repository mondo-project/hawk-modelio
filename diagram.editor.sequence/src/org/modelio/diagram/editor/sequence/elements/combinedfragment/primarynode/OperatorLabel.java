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
                                    

package org.modelio.diagram.editor.sequence.elements.combinedfragment.primarynode;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.widgets.Display;
import org.modelio.diagram.elements.core.figures.IPenOptionsSupport;
import org.modelio.diagram.elements.core.figures.PenOptions;
import org.modelio.diagram.styles.core.StyleKey.LinePattern;

/**
 * Specialised figure drawing additional decoration.
 * 
 * @author fpoyer
 */
@objid ("d8d2b51d-55b6-11e2-877f-002564c97630")
public class OperatorLabel extends Label implements IPenOptionsSupport {
    @objid ("4ff92cc9-55c2-11e2-9337-002564c97630")
    protected PenOptions penOptions;

    /**
     * Constructs and empty label.
     */
    @objid ("d8d2b524-55b6-11e2-877f-002564c97630")
    public OperatorLabel() {
        super();
        this.penOptions = new PenOptions();
    }

    /**
     * Construct a Label with passed String as its text.
     * @param s the label text
     */
    @objid ("d8d2b527-55b6-11e2-877f-002564c97630")
    public OperatorLabel(final String s) {
        super(s);
        this.penOptions = new PenOptions();
    }

    /**
     * Get the line color.
     * @return the line color.
     */
    @objid ("d8d2b52c-55b6-11e2-877f-002564c97630")
    @Override
    public Color getLineColor() {
        return this.penOptions.lineColor;
    }

    /**
     * Get the line width.
     * @return the line width.
     */
    @objid ("d8d2b532-55b6-11e2-877f-002564c97630")
    @Override
    public int getLineWidth() {
        return this.penOptions.lineWidth;
    }

    /**
     * Get the text color.
     * @return the text color.
     */
    @objid ("d8d2b538-55b6-11e2-877f-002564c97630")
    @Override
    public Color getTextColor() {
        return this.penOptions.textColor;
    }

    /**
     * Get the text font.
     * @return the text font.
     */
    @objid ("d8d2b53e-55b6-11e2-877f-002564c97630")
    @Override
    public Font getTextFont() {
        return this.penOptions.textFont;
    }

    /**
     * Set the line(s) color.
     * @param lineColor the line color.
     */
    @objid ("d8d43b9e-55b6-11e2-877f-002564c97630")
    @Override
    public void setLineColor(Color lineColor) {
        if (this.penOptions.lineColor != lineColor) {
            this.penOptions.lineColor = lineColor;
            this.repaint();
        }
    }

    /**
     * Set the line(s) width.
     * @param lineWidth the line(s) width.
     */
    @objid ("d8d43ba3-55b6-11e2-877f-002564c97630")
    @Override
    public void setLineWidth(int lineWidth) {
        if (this.penOptions.lineWidth != lineWidth) {
            this.penOptions.lineWidth = lineWidth;
            this.repaint();
        }
    }

    /**
     * Set the text color.
     * @param textColor the text color.
     */
    @objid ("d8d43ba8-55b6-11e2-877f-002564c97630")
    @Override
    public void setTextColor(Color textColor) {
        if (this.penOptions.textColor != textColor) {
            this.penOptions.textColor = textColor;
            this.repaint();
        }
    }

    /**
     * Set the text font.
     * @param textFont the text font.
     */
    @objid ("d8d43bad-55b6-11e2-877f-002564c97630")
    @Override
    public void setTextFont(Font textFont) {
        if (this.penOptions.textFont != textFont) {
            this.penOptions.textFont = textFont;
            this.repaint();
        }
    }

    /**
     * Get the line pattern
     * @return lineStyle the line style See {@link LinePattern}
     */
    @objid ("d8d43bb2-55b6-11e2-877f-002564c97630")
    @Override
    public LinePattern getLinePattern() {
        return this.penOptions.linePattern;
    }

    /**
     * Sets the line pattern to the argument, which must be one of the constants
     * 
     * {@link LinePattern}
     * @param lineStyle the new style
     */
    @objid ("d8d43bba-55b6-11e2-877f-002564c97630")
    @Override
    public void setLinePattern(LinePattern lineStyle) {
        if (this.penOptions.linePattern != lineStyle) {
            this.penOptions.linePattern = lineStyle;
            this.repaint();
        }
    }

    @objid ("d8d43bc1-55b6-11e2-877f-002564c97630")
    @Override
    protected void paintFigure(final Graphics graphics) {
        Rectangle boundsRect = getBounds();
        
        if (this.penOptions.textColor != null) {
            graphics.setForegroundColor(this.penOptions.textColor);
        }
        if (this.penOptions.textFont != null) {
            graphics.setFont(this.penOptions.textFont);
        }
        
        graphics.pushState();
        try {
            if (isOpaque()) {
                // Hack the clipping and handle the background drawing ourselves to "cut" the corner.
                final Rectangle originalClip = new Rectangle();
                graphics.getClip(originalClip);
                final Path path = new Path(Display.getCurrent());
                path.moveTo(boundsRect.x, boundsRect.y);
                path.lineTo(boundsRect.x, boundsRect.bottom());
                path.lineTo(boundsRect.right() - 6, boundsRect.bottom());
                path.lineTo(boundsRect.right(), boundsRect.bottom() - 6);
                path.lineTo(boundsRect.right(), boundsRect.y);
                path.lineTo(boundsRect.x, boundsRect.y);
                graphics.setClip(path);
                graphics.fillRectangle(getBounds());
                path.dispose();
                graphics.setClip(originalClip);
            }
        
            graphics.translate(boundsRect.x, boundsRect.y);
            if (getIcon() != null)
                graphics.drawImage(getIcon(), getIconLocation());
            if (!isEnabled()) {
                graphics.translate(1, 1);
                graphics.setForegroundColor(ColorConstants.buttonLightest);
                graphics.drawText(getSubStringText(), getTextLocation());
                graphics.translate(-1, -1);
                graphics.setForegroundColor(ColorConstants.buttonDarker);
            }
            graphics.drawText(getSubStringText(), getTextLocation());
            graphics.translate(-boundsRect.x, -boundsRect.y);
        
            // Draw additional border with cut corner
            if (this.penOptions.lineColor != null) {
                graphics.setForegroundColor(this.penOptions.lineColor);
            }
            if (this.penOptions.lineWidth > 0) {
                graphics.setLineWidth(this.penOptions.lineWidth);
            }
            if (this.penOptions.linePattern != null) {
                graphics.setLineStyle(this.penOptions.linePattern.toSWTConstant());
            }
            PointList polygon = new PointList(4);
            Rectangle rect = getBounds().getExpanded(-1, -1);
            polygon.addPoint(rect.getBottomLeft().getTranslated(-1, 0));
            polygon.addPoint(rect.getBottomRight().getTranslated(-5, 0));
            polygon.addPoint(rect.getBottomRight().getTranslated(0, -5));
            polygon.addPoint(rect.getTopRight().getTranslated(0, -1));
            graphics.drawPolyline(polygon);
        
        } finally {
            graphics.popState();
        }
    }

}
