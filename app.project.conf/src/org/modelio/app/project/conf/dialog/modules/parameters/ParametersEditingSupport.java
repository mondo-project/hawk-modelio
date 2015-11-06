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
                                    

package org.modelio.app.project.conf.dialog.modules.parameters;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColorCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.modelio.api.module.IParameterModel;
import org.modelio.api.module.paramEdition.BoolParameterModel;
import org.modelio.api.module.paramEdition.ColorParameterModel;
import org.modelio.api.module.paramEdition.DirectoryParameterModel;
import org.modelio.api.module.paramEdition.EnumParameterModel;
import org.modelio.api.module.paramEdition.FileParameterModel;
import org.modelio.api.module.paramEdition.IntParameterModel;
import org.modelio.api.module.paramEdition.ParameterModel;
import org.modelio.api.module.paramEdition.PasswordParameterModel;
import org.modelio.api.module.paramEdition.StringParameterModel;
import org.modelio.app.project.conf.plugin.AppProjectConf;
import org.modelio.core.ui.treetable.combo.LabelsComboBoxCellEditor;
import org.modelio.core.ui.treetable.directory.DirectoryCellEditor;
import org.modelio.core.ui.treetable.file.FileCellEditor;
import org.modelio.core.ui.treetable.number.IntegerCellEditor;

/**
 * StyleEditingSupport provides EditingSupport implementation for the StyleViewer.
 * <p>
 * It must be able to provide a Label and a CellEditor for all the supported StyleKey value types. It must also be able to get and
 * set values during edition, again dealing with all the possible StyleKey value types.
 */
@objid ("e7a34fbf-3a39-11e2-90eb-002564c97630")
class ParametersEditingSupport extends EditingSupport {
    @objid ("c85d4aed-fb32-4e7a-a464-3bc221612ecd")
    private TreeViewer viewer;

    /**
     * Initialize the StylePropertyEditingSupport.
     * @param viewer The style viewer.
     */
    @objid ("e7a81272-3a39-11e2-90eb-002564c97630")
    public ParametersEditingSupport(TreeViewer viewer) {
        super(viewer);
        this.viewer = viewer;
    }

    @objid ("e7a81276-3a39-11e2-90eb-002564c97630")
    @Override
    protected boolean canEdit(Object element) {
        if (element instanceof IParameterModel) {
            return !((IParameterModel) element).isLocked();
        } else {
            return false;
        }
    }

    @objid ("e7a8127b-3a39-11e2-90eb-002564c97630")
    @Override
    protected CellEditor getCellEditor(Object element) {
        final Tree tree = this.viewer.getTree();
        
        IParameterModel property = ((IParameterModel) element);
        
        // Boolean
        if (property instanceof BoolParameterModel) {
            return new CheckboxCellEditor(tree);
        } else if (property instanceof ColorParameterModel) {
            return new ColorCellEditor(tree);
        } else if (property instanceof DirectoryParameterModel) {
            return new DirectoryCellEditor(tree);
        } else if (property instanceof EnumParameterModel) {
            return new LabelsComboBoxCellEditor(tree, ((EnumParameterModel) element).getLabels().toArray(new String[0]), SWT.SINGLE);
        } else if (property instanceof FileParameterModel) {
            FileParameterModel fileProperty = (FileParameterModel) property;
            return new FileCellEditor(tree, fileProperty.getAllowedExtensionLabels(), fileProperty.getAllowedExtensions());
        } else if (property instanceof IntParameterModel) {
            return new IntegerCellEditor(tree, SWT.SINGLE);
        } else if (property instanceof PasswordParameterModel) {
            return new TextCellEditor(tree, SWT.PASSWORD);
        } else if (property instanceof StringParameterModel) {
            return new TextCellEditor(tree);
        } else {
            AppProjectConf.LOG.error("Invalid parameter type"); //$NON-NLS-1$
        }
        return null;
    }

    @objid ("e7aa73d2-3a39-11e2-90eb-002564c97630")
    @Override
    protected Object getValue(Object element) {
        IParameterModel property = ((IParameterModel) element);
        
        // Boolean
        if (property instanceof BoolParameterModel) {
            return Boolean.valueOf(((ParameterModel) property).getStringValue());
        } else if (property instanceof ColorParameterModel) {
            // TODO
            return null;
        } else if (property instanceof DirectoryParameterModel) {
            return ((ParameterModel) property).getStringValue();
        } else if (property instanceof EnumParameterModel) {
            return ((EnumParameterModel) element).getLabel(((ParameterModel) property).getStringValue());
        } else if (property instanceof FileParameterModel) {
            return ((ParameterModel) property).getStringValue();
        } else if (property instanceof IntParameterModel) {
            return Integer.valueOf(((ParameterModel) property).getStringValue());
        } else if (property instanceof PasswordParameterModel) {
            return ((PasswordParameterModel) property).getPasswordValue();
        } else if (property instanceof StringParameterModel) {
            return ((ParameterModel) property).getStringValue();
        } else {
            AppProjectConf.LOG.error("Invalid parameter type"); //$NON-NLS-1$
        }
        return null;
    }

    @objid ("e7aa73d8-3a39-11e2-90eb-002564c97630")
    @Override
    protected void setValue(Object element, Object value) {
        IParameterModel property = ((IParameterModel) element);
        
        if (property instanceof EnumParameterModel) {
            property.setValue(((EnumParameterModel) element).getValue((String) value));
        } else {
            property.setValue(value);
        }
        this.viewer.refresh(element, true);
    }

}
