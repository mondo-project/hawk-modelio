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
                                    

package org.modelio.core.ui.treetable.directory;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.modelio.core.ui.treetable.EditableDialogCellEditor;

/**
 * CellEditor to choose a directory.
 */
@objid ("cd1551b4-db02-4d28-af82-8fecc86f1280")
public class DirectoryCellEditor extends EditableDialogCellEditor {
    @objid ("0baaf198-4148-462b-b4a3-e4647b33368b")
    public DirectoryCellEditor(Composite parent) {
        super(parent);
    }

    @objid ("bdaaeefb-0612-4cd1-b8bb-551ca31d1145")
    @Override
    protected Object openDialogBox(Control cellEditorWindow) {
        DirectoryDialog dialog = new DirectoryDialog(Display.getDefault().getActiveShell());
        String directory = dialog.open();
        if (directory != null) {
            doSetValue(directory);
        }   // if cancel (directory is null), keep the last value
        return null;
    }

}
