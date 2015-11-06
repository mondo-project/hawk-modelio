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
                                    

package org.modelio.diagram.elements.core.figures;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.modelio.diagram.styles.core.StyleKey.LinePattern;

/**
 * Base class for rectangle figure with gradient background.
 */
@objid ("7fa261af-1dec-11e2-8cad-001ec947c8cc")
public class GradientFigure extends Figure implements IBrushOptionsSupport, IPenOptionsSupport, HandleBounds {
    @objid ("7fa261b5-1dec-11e2-8cad-001ec947c8cc")
    protected BrushOptions brushOptions;

    @objid ("7fa261b9-1dec-11e2-8cad-001ec947c8cc")
    protected PenOptions penOptions;

    @objid ("6330be17-1e83-11e2-8cad-001ec947c8cc")
    private static Rectangle tempRect = new Rectangle();

    @objid ("7fa261ba-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Rectangle getHandleBounds() {
        return this.getBounds().getCopy();
    }

    /**
     * Creates a gradient figure.
     */
    @objid ("7fa261c1-1dec-11e2-8cad-001ec947c8cc")
    public GradientFigure() {
        this.brushOptions = new BrushOptions();
        this.penOptions = new PenOptions();
    }

    @objid ("7fa261c4-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void paintFigure(Graphics graphics) {
        if (this.isOpaque() && this.brushOptions.fillColor != null) {
            Color base = this.brushOptions.fillColor;
            Color gradientColor = this.brushOptions.useGradient ? this.computeGradientColor(base) : base;
            graphics.setBackgroundColor(gradientColor);
            graphics.setForegroundColor(base);
            graphics.setAlpha(this.brushOptions.alpha);
            
            tempRect = this.getBounds();
            if (this.brushOptions.useGradient) {
                graphics.fillGradient(tempRect, false);
                gradientColor.dispose();
            } else {
                graphics.fillRectangle(tempRect);
            }
        }
        
        graphics.restoreState();
    }

    @objid ("7fa261ca-1dec-11e2-8cad-001ec947c8cc")
    protected Color computeGradientColor(Color base) {
        //float[] hsb = base.getRGB().getHSB();
        // Color derivedColor = new Color(base.getDevice(), new RGB(hsb[0], hsb[1] * 0.1f, hsb[2] /*Math.min(hsb[2] * 1.1f, 1.0f)*/ ));
        Color derivedColor = new Color(base.getDevice(), new RGB(255, 255, 255));
        return derivedColor;
    }

    @objid ("7fa261cf-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setFillColor(Color fillColor) {
        if (this.brushOptions.fillColor != fillColor) {
            this.setBackgroundColor(fillColor);
            this.brushOptions.fillColor = fillColor;
            this.repaint();
        }
    }

    @objid ("7fa261d3-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Color getFillColor() {
        return this.brushOptions.fillColor;
    }

    @objid ("7fa261d8-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public boolean getUseGradient() {
        return this.brushOptions.useGradient;
    }

    @objid ("7fa261dd-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setUseGradient(boolean useGradient) {
        if (this.brushOptions.useGradient != useGradient) {
            this.brushOptions.useGradient = useGradient;
            this.repaint();
        }
    }

    @objid ("7fa261e1-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Color getLineColor() {
        return this.penOptions.lineColor;
    }

    @objid ("7fa261e6-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public int getLineWidth() {
        return this.penOptions.lineWidth;
    }

    @objid ("7fa261eb-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Color getTextColor() {
        return this.penOptions.textColor;
    }

    @objid ("7fa261f0-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Font getTextFont() {
        return this.penOptions.textFont;
    }

    @objid ("7fa261f5-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setLineColor(Color lineColor) {
        if (this.penOptions.lineColor != lineColor) {
            this.penOptions.lineColor = lineColor;
            this.repaint();
        }
    }

    @objid ("7fa4c40a-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setLineWidth(int lineWidth) {
        if (this.penOptions.lineWidth != lineWidth) {
            this.penOptions.lineWidth = lineWidth;
            this.repaint();
        }
    }

    @objid ("7fa4c40e-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setTextColor(Color textColor) {
        if (this.penOptions.textColor != textColor) {
            this.penOptions.textColor = textColor;
            this.repaint();
        }
    }

    @objid ("7fa4c412-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setTextFont(Font textFont) {
        if (this.penOptions.textFont != textFont) {
            this.penOptions.textFont = textFont;
            this.repaint();
        }
    }

    @objid ("7fa4c416-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public LinePattern getLinePattern() {
        return this.penOptions.linePattern;
    }

    @objid ("7fa4c41b-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setLinePattern(LinePattern lineStyle) {
        if (this.penOptions.linePattern != lineStyle) {
            this.penOptions.linePattern = lineStyle;
            this.repaint();
        }
    }

    @objid ("8e1447f4-a6ee-4e12-a9b2-70b54a996a7f")
    @Override
    public void setFillAlpha(int alpha) {
        if (this.brushOptions.alpha != alpha) {
            this.brushOptions.alpha = alpha;
            this.repaint();
        }
    }

    @objid ("d12ba4b3-4884-4030-8eeb-0a91ef43bbc8")
    @Override
    public int getFillAlpha() {
        return this.brushOptions.alpha;
    }

}
