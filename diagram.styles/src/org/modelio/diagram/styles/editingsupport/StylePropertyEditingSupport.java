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
                                    

package org.modelio.diagram.styles.editingsupport;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Tree;
import org.modelio.app.core.picking.IModelioPickingService;
import org.modelio.core.ui.CoreColorRegistry;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.diagram.styles.editingsupport.color.ColorCellEditor2;
import org.modelio.diagram.styles.editingsupport.combo.EnumComboBoxCellEditor;
import org.modelio.diagram.styles.editingsupport.element.ElementCellEditor;
import org.modelio.diagram.styles.editingsupport.font.FontDialogEditor;
import org.modelio.diagram.styles.editingsupport.number.IntegerCellEditor;
import org.modelio.diagram.styles.viewer.StyleViewer;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * StyleEditingSupport provides EditingSupport implementation for the StyleViewer.
 * <p>
 * It must be able to provide a Label and a CellEditor for all the supported StyleKey value types. It must also be able
 * to get and set values during edition, again dealing with all the possible StyleKey value types.
 */
@objid ("85b2e4b2-1926-11e2-92d2-001ec947c8cc")
public class StylePropertyEditingSupport extends EditingSupport {
    @objid ("85b2e4b4-1926-11e2-92d2-001ec947c8cc")
    private final StyleViewer viewer;

    /**
     * Initialize the StylePropertyEditingSupport.
     * @param viewer The style viewer.
     */
    @objid ("85b2e4b6-1926-11e2-92d2-001ec947c8cc")
    public StylePropertyEditingSupport(StyleViewer viewer) {
        super(viewer.getTreeViewer());
        this.viewer = viewer;
    }

    @objid ("85b2e4ba-1926-11e2-92d2-001ec947c8cc")
    @Override
    protected boolean canEdit(Object element) {
        return (this.viewer.getModel() != null && this.viewer.getModel().isEditable());
    }

    @objid ("85b546fd-1926-11e2-92d2-001ec947c8cc")
    @Override
    protected CellEditor getCellEditor(Object element) {
        if (!(element instanceof StyleKey)) {
            return null;
        }
        
        final Class<?> stype = ((StyleKey) element).getType();
        
        final Tree tree = this.viewer.getTreeViewer().getTree();
        
        if (stype.equals(Boolean.class)) {
            return new org.eclipse.jface.viewers.CheckboxCellEditor();
        }
        if (stype.equals(String.class)) {
            return new TextCellEditor(tree, SWT.SINGLE);
        }
        if (stype.equals(Integer.class)) {
            return new IntegerCellEditor(tree, SWT.SINGLE);
        }
        if (stype.equals(Font.class)) {
            return new FontDialogEditor(tree, SWT.SINGLE);
        }
        if (stype.equals(Color.class)) {
            return new ColorCellEditor2(tree, SWT.SINGLE);
        }
        if (stype.equals(MRef.class)) {
            ICoreSession session = this.viewer.getModel().getSession();
            IModelioPickingService pickingService = this.viewer.getPickingService();
            
            return new ElementCellEditor(tree, session, pickingService);
        }
        if (stype.isEnum()) {
            return new EnumComboBoxCellEditor(tree, stype, SWT.SINGLE);
        }
        return null;
    }

    @objid ("85b54703-1926-11e2-92d2-001ec947c8cc")
    @Override
    protected Object getValue(Object element) {
        if (!(element instanceof StyleKey)) {
            return null;
        }
        
        final IStyle editedStyle = this.viewer.getEditedStyle();
        final StyleKey skey = (StyleKey) element;
        final Class<?> stype = ((StyleKey) element).getType();
        
        if (stype.equals(Boolean.class)) {
            return editedStyle.getBoolean(skey);
        }
        if (stype.equals(String.class)) {
            return editedStyle.getProperty(skey);
        }
        if (stype.equals(Integer.class)) {
            return editedStyle.getInteger(skey);
        }
        if (stype.equals(Font.class)) {
            return editedStyle.getProperty(skey);
        }
        if (stype.equals(Color.class)) {
            return editedStyle.getColor(skey).getRGB();
        }
        if (stype.equals(MRef.class)) {
            return editedStyle.getProperty(skey);
        }
        if (stype.isEnum()) {
            return editedStyle.getProperty(skey);
        }
        return null;
    }

    @objid ("85b7a956-1926-11e2-92d2-001ec947c8cc")
    @Override
    protected void setValue(Object element, Object value) {
        if (!(element instanceof StyleKey)) {
            return;
        }
        
        final IStyle editedStyle = this.viewer.getEditedStyle();
        final StyleKey skey = (StyleKey) element;
        final Class<?> stype = ((StyleKey) element).getType();
        
        if (value != null) {
            // Boolean
            if (stype.equals(Boolean.class)) {
                editedStyle.setProperty(skey, value);
            }
            // String
            if (stype.equals(String.class)) {
                editedStyle.setProperty(skey, value);
            }
            // Integer
            if (stype.equals(Integer.class)) {
                editedStyle.setProperty(skey, value);
            }
            // Font
            if (stype.equals(Font.class)) {
                editedStyle.setProperty(skey, value);
            }
            // Color
            if (stype.equals(Color.class)) {
                editedStyle.setProperty(skey, CoreColorRegistry.getColor((RGB) value));
            }
            // MRef
            if (stype.equals(MRef.class)) {
                editedStyle.setProperty(skey, value);
            }
            // Enum
            if (stype.isEnum()) {
                editedStyle.setProperty(skey, value);
            }
        } else {
            // MRef
            if (stype.equals(MRef.class)) {
                editedStyle.setProperty(skey, value);
            }
        }
    }

}
