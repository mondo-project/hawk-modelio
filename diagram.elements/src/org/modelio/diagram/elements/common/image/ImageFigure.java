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
                                    

package org.modelio.diagram.elements.common.image;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.Label;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.modelio.diagram.elements.core.figures.IPenOptionsSupport;
import org.modelio.diagram.styles.core.StyleKey.LinePattern;

/**
 * Figure that represents an element in image mode.
 * 
 * @author phv
 */
@objid ("7e86a9e7-1dec-11e2-8cad-001ec947c8cc")
public class ImageFigure extends Label implements IPenOptionsSupport {
    @objid ("7e86a9eb-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Color getLineColor() {
        return this.getForegroundColor();
    }

    @objid ("7e86a9f0-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public int getLineWidth() {
        return 0;
    }

    @objid ("7e86a9f5-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setLineColor(Color lineColor) {
        // Ignore line color
    }

    @objid ("7e86a9f9-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setLineWidth(int lineWidth) {
        // Ignore line width
    }

    @objid ("7e86a9fd-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Color getTextColor() {
        return this.getForegroundColor();
    }

    @objid ("7e86aa02-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Font getTextFont() {
        return this.getFont();
    }

    @objid ("7e86aa07-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setTextColor(Color textColor) {
        this.setForegroundColor(textColor);
    }

    @objid ("7e86aa0b-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setTextFont(Font textFont) {
        this.setFont(textFont);
    }

    /**
     * Set the label.
     * @param name the label
     */
    @objid ("7e86aa0f-1dec-11e2-8cad-001ec947c8cc")
    public void setLabel(String name) {
        this.setText(name);
    }

    /**
     * Set the image.
     * @param img the image.
     */
    @objid ("7e86aa13-1dec-11e2-8cad-001ec947c8cc")
    public void setImage(Image img) {
        this.setIcon(img);
    }

    @objid ("7e86aa17-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public LinePattern getLinePattern() {
        return LinePattern.LINE_SOLID;
    }

    @objid ("7e86aa1c-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setLinePattern(LinePattern linePattern) {
        // Ignore line pattern
    }

}
