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
                                    

package org.modelio.diagram.styles.editingsupport.color;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.diagram.styles.viewer.StyleViewer;

/**
 * provide an image
 */
@objid ("859183d7-1926-11e2-92d2-001ec947c8cc")
public class ColorLabelProvider extends ColumnLabelProvider {
    @objid ("859183d9-1926-11e2-92d2-001ec947c8cc")
    private StyleViewer viewer;

    @objid ("859183da-1926-11e2-92d2-001ec947c8cc")
    @Override
    public Image getImage(Object element) {
        //        StyleKey skey = (StyleKey) element;
        //
        //        Color color = this.data.getColor(skey);
        //
        //        // draw the preview color
        //        Image img = new Image(Display.getCurrent(), 15, 15);
        //        //img.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
        //
        //        GC gc = new GC(img);
        //        gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
        //        gc.setBackground(color);
        //        gc.fillRectangle(0, 0, 15, 15);
        //        gc.dispose();
        //
        //        ImageData imgData = img.getImageData();
        //        imgData.transparentPixel = imgData.palette.getPixel(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE).getRGB());
        //        Image newImg = new Image(Display.getCurrent(), imgData);
        //        img.dispose();
        //        return newImg;
        return null;
    }

    @objid ("859183e0-1926-11e2-92d2-001ec947c8cc")
    public ColorLabelProvider(StyleViewer viewer) {
        this.viewer = viewer;
    }

    @objid ("859183e3-1926-11e2-92d2-001ec947c8cc")
    @Override
    public String getText(Object element) {
        //        StyleKey skey = (StyleKey) element;
        //        Color color = this.data.getColor(skey);
        //        return color.getRGB().toString();
        return null;
    }

    @objid ("859183e9-1926-11e2-92d2-001ec947c8cc")
    @Override
    public Color getBackground(Object element) {
        StyleKey skey = (StyleKey) element;
        Color color = this.viewer.getEditedStyle().getColor(skey);
        return color;
    }

}
