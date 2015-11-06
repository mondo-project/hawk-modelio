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
                                    

package org.modelio.diagram.styles.editingsupport.key;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.modelio.core.ui.CoreColorRegistry;
import org.modelio.core.ui.CoreFontRegistry;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.diagram.styles.viewer.StyleViewer;

@objid ("85a6f8ea-1926-11e2-92d2-001ec947c8cc")
public class KeyLabelProvider extends StyledCellLabelProvider {
    @objid ("85a6f8eb-1926-11e2-92d2-001ec947c8cc")
    private StyleViewer viewer;

    @objid ("85a6f8ec-1926-11e2-92d2-001ec947c8cc")
    private static final RGB heritedColor = new RGB(64, 64, 64);

    @objid ("85a6f8ee-1926-11e2-92d2-001ec947c8cc")
    private static final RGB localColor = new RGB(0, 0, 0);

    @objid ("85a95b3a-1926-11e2-92d2-001ec947c8cc")
    private Font boldFont;

    @objid ("85a95b3b-1926-11e2-92d2-001ec947c8cc")
    public KeyLabelProvider(StyleViewer viewer) {
        this.viewer = viewer;
    }

    @objid ("85a95b3e-1926-11e2-92d2-001ec947c8cc")
    @Override
    public void update(ViewerCell cell) {
        Object element = cell.getElement();
        if (element instanceof String) {
            String cellText = (String) element;
            cell.setText(cellText);
            
            StyleRange styleRange = new StyleRange();
            styleRange.start = 0;
            styleRange.length = cellText.length();
            styleRange.foreground = CoreColorRegistry.getColor(heritedColor);
            styleRange.font = null;
            
            ITreeContentProvider contentProvider = (ITreeContentProvider)this.viewer.getTreeViewer().getContentProvider();
            for (Object obj : contentProvider.getChildren(element)) {
                StyleKey skey = (StyleKey) obj;
                if (this.viewer.getEditedStyle().isLocal(skey)) {
                    styleRange.foreground = CoreColorRegistry.getColor(localColor);
                    styleRange.font = this.getBoldFont(cell.getFont());
                    break;
                }
            }
            
            cell.setStyleRanges(new StyleRange[] { styleRange });
        } else {
        
            StyleKey skey = (StyleKey) cell.getElement();
            String cellText = skey.getLabel();
            cell.setText(cellText);
        
            StyleRange styleRange = new StyleRange();
            styleRange.start = 0;
            styleRange.length = cellText.length();
        
            if (this.viewer.getEditedStyle().isLocal(skey)) {
                styleRange.foreground = CoreColorRegistry.getColor(localColor);
                styleRange.font = this.getBoldFont(cell.getFont());
            } else {
                styleRange.foreground = CoreColorRegistry.getColor(heritedColor);
                styleRange.font = null;
            }
            cell.setStyleRanges(new StyleRange[] { styleRange });
        }
        return;
    }

    @objid ("85a95b43-1926-11e2-92d2-001ec947c8cc")
    private Font getBoldFont(Font font) {
        if (this.boldFont == null) {
            this.boldFont = CoreFontRegistry.getModifiedFont(font, SWT.BOLD);
        }
        return this.boldFont;
    }

    @objid ("85a95b48-1926-11e2-92d2-001ec947c8cc")
    @Override
    public void dispose() {
        this.boldFont = null;
        super.dispose();
    }

}
