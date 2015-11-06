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
                                    

package org.modelio.diagram.styles.editingsupport.element;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.modelio.core.ui.images.BasicModelElementLabelProvider;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.diagram.styles.viewer.StyleViewer;
import org.modelio.vcore.session.UnknownMetaclassException;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Provide MRef label
 */
@objid ("2969700f-927a-42e1-8721-2ee33c82f745")
public class ElementLabelProvider extends ColumnLabelProvider {
    @objid ("fcd2b69f-13c5-46f1-a6c9-f79865e0da4a")
    private StyleViewer viewer;

    @objid ("24b375e3-3c87-4730-9126-54f3e8eac8a7")
    private BasicModelElementLabelProvider baseProvider;

    @objid ("34cc9988-c668-4b22-89e2-b7054bcf9d30")
    private Color noElementColor;

    /**
     * Initialize a MRef label provider
     * @param viewer the viewer.
     */
    @objid ("1b086ef4-33ed-47dc-a289-a30175f5145d")
    public ElementLabelProvider(StyleViewer viewer) {
        this.viewer = viewer;
        this.baseProvider = new BasicModelElementLabelProvider();
        this.noElementColor = this.viewer.getTreeViewer().getControl().getDisplay().getSystemColor(SWT.COLOR_GRAY);
    }

    @objid ("41b79b0f-fc5b-4332-a27d-1116fa1def1f")
    @Override
    public Image getImage(Object element) {
        final MObject mObject = getMObject(element);
        if (mObject != null)
            return this.baseProvider.getImage(mObject);
        else
            return null;
    }

    @objid ("c5aaa18c-5b63-4feb-861b-e33dbc714637")
    @Override
    public void update(ViewerCell cell) {
        final MObject mObject = getMObject(cell.getElement());
        if (mObject != null)
            cell.setStyleRanges(this.baseProvider.getStyledText(mObject).getStyleRanges());
        else 
            cell.setStyleRanges(new StyleRange[0]);
        
        super.update(cell);
    }

    @objid ("a0aeda17-1cc3-4f47-b7a7-da892fd40fde")
    @Override
    public String getText(Object element) {
        final MObject mObject = getMObject(element);
        if (mObject != null)
            return this.baseProvider.getText(mObject);
        else
            return "<none>";
    }

    @objid ("5da1f760-5f19-44a2-bdb7-28f5be7decc6")
    @Override
    public Color getForeground(Object element) {
        final MObject mObject = getMObject(element);
        if (mObject == null) {
            return this.noElementColor;
        }
        return null;
    }

    @objid ("8ee5ff6b-813d-4865-bce1-ce60b3678542")
    private MObject getMObject(Object element) {
        return computeMObject(element);
    }

    @objid ("dcfbb0bf-b40e-4c05-8e94-af5c18f23553")
    private MObject computeMObject(Object element) throws UnknownMetaclassException {
        StyleKey skey = (StyleKey) element;
        MRef ref = (MRef) this.viewer.getEditedStyle().getProperty(skey);
        if (ref != null)
            return this.viewer.getModel().getSession().getModel().findByRef(ref);
        else 
            return null;
    }

}
