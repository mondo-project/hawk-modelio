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
                                    

package org.modelio.diagram.styles.editingsupport.checkbox;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Image;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.diagram.styles.plugin.DiagramStyles;
import org.modelio.diagram.styles.viewer.StyleViewer;

/**
 * Provide checkbox label
 */
@objid ("858cbf02-1926-11e2-92d2-001ec947c8cc")
public class CheckboxLabelProvider extends ColumnLabelProvider {
    @objid ("858cbf04-1926-11e2-92d2-001ec947c8cc")
    private StyleViewer viewer;

    @objid ("858cbf05-1926-11e2-92d2-001ec947c8cc")
    private static final Image CHECKED = DiagramStyles.getImageDescriptor("icons/checked.gif").createImage();

    @objid ("858cbf07-1926-11e2-92d2-001ec947c8cc")
    private static final Image UNCHECKED = DiagramStyles.getImageDescriptor("icons/unchecked.gif").createImage();

    @objid ("858cbf09-1926-11e2-92d2-001ec947c8cc")
    public CheckboxLabelProvider(StyleViewer viewer) {
        this.viewer = viewer;
    }

    @objid ("858cbf0c-1926-11e2-92d2-001ec947c8cc")
    @Override
    public Image getImage(Object element) {
        StyleKey skey = (StyleKey) element;
        
        if (this.viewer.getEditedStyle().getBoolean(skey))
            return CHECKED;
        else
            return UNCHECKED;
    }

    @objid ("858cbf11-1926-11e2-92d2-001ec947c8cc")
    @Override
    public void update(ViewerCell cell) {
        super.update(cell);
    }

    @objid ("858cbf15-1926-11e2-92d2-001ec947c8cc")
    @Override
    public String getText(Object element) {
        return null;
    }

}
