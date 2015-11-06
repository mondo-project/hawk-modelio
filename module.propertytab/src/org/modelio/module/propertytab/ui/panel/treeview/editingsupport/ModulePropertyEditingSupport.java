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
                                    

package org.modelio.module.propertytab.ui.panel.treeview.editingsupport;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.modelio.core.ui.treetable.combo.EnumComboBoxCellEditor;
import org.modelio.core.ui.treetable.combo.LabelsComboBoxCellEditor;
import org.modelio.core.ui.treetable.number.IntegerCellEditor;
import org.modelio.module.propertytab.model.ModuleProperty;
import org.modelio.module.propertytab.model.ModulePropertyModel;

/**
 * StyleEditingSupport provides EditingSupport implementation for the StyleViewer.
 * <p>
 * It must be able to provide a Label and a CellEditor for all the supported StyleKey value types. It must also be able to get and
 * set values during edition, again dealing with all the possible StyleKey value types.
 */
@objid ("c8956c70-1eba-11e2-9382-bc305ba4815c")
public class ModulePropertyEditingSupport extends EditingSupport {
    @objid ("c8956c72-1eba-11e2-9382-bc305ba4815c")
    private TreeViewer viewer;

    /**
     * Initialize the StylePropertyEditingSupport.
     * @param viewer The style viewer.
     */
    @objid ("c8959380-1eba-11e2-9382-bc305ba4815c")
    public ModulePropertyEditingSupport(TreeViewer viewer) {
        super(viewer);
        this.viewer = viewer;
    }

    @objid ("c8959384-1eba-11e2-9382-bc305ba4815c")
    @Override
    protected boolean canEdit(Object element) {
        ModuleProperty property = (ModuleProperty)element;
        return !property.isReadOnly();
    }

    @objid ("c895ba94-1eba-11e2-9382-bc305ba4815c")
    @Override
    protected CellEditor getCellEditor(Object element) {
        if (!(element instanceof ModuleProperty)) {
            return null;
        }
        
        final Class<?> type = ((ModuleProperty) element).getType();
        
        final Tree tree = this.viewer.getTree();
        
        if (type.equals(Boolean.class)) {
            return new org.eclipse.jface.viewers.CheckboxCellEditor();
        }
        if (type.equals(String.class)) {
            return new TextCellEditor(tree, SWT.SINGLE);
        }
        if (type.equals(Integer.class)) {
            return new IntegerCellEditor(tree, SWT.SINGLE);
        }
        
        if (type.equals(String[].class)) {
            return new LabelsComboBoxCellEditor(tree, ((ModuleProperty) element).getEnumValues(), SWT.SINGLE);
        }
        return null;
    }

    @objid ("c895e1a4-1eba-11e2-9382-bc305ba4815c")
    @Override
    protected Object getValue(Object element) {
        if (!(element instanceof ModuleProperty)) {
            return null;
        }
        ModuleProperty property = ((ModuleProperty) element);
        
        final Class<?> type = property.getType();
        
        if (type.equals(Boolean.class)) {
            if (property.getValue().equals("true")) {
                return new Boolean(true);
            } else {
                return new Boolean(false);
            }
        }
        if (type.equals(String.class)) {
            return property.getValue();
        }
        if (type.equals(Integer.class)) {
            return new Integer(property.getValue());
        }
        
        if (type.equals(String[].class)) {
            return property.getValue();
        }
        return null;
    }

    @objid ("c89608b4-1eba-11e2-9382-bc305ba4815c")
    @Override
    protected void setValue(Object element, Object value) {
        ModuleProperty property = ((ModuleProperty) element);
        final Class<?> type = property.getType();
        ModulePropertyModel model = (ModulePropertyModel)this.viewer.getInput();
        
        if (value != null) {
            // Boolean
            if (type.equals(Boolean.class)) {
                model.setValueAt(property, value);
            }
            // String
            if (type.equals(String.class)) {
                model.setValueAt(property, value);
        
            }
            // Integer
            if (type.equals(Integer.class)) {
                model.setValueAt(property, value);
            }
            // Enum
            if (type.equals(String[].class)) {
                model.setValueAt(property, value);
            }
        }
    }

}
