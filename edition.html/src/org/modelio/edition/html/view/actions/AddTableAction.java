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
                                    

//------------------------------------------------------------------------------
// All rights reserved. This program and the accompanying materials
// are made available under the terms of the Eclipse Public License v1.0
// which accompanies this distribution, and is available at
// http://www.eclipse.org/legal/epl-v10.html
//
// Contributors:
// IBM Corporation - initial implementation
//------------------------------------------------------------------------------
package org.modelio.edition.html.view.actions;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.modelio.edition.html.plugin.HtmlTextImages;
import org.modelio.edition.html.plugin.HtmlTextResources;
import org.modelio.edition.html.view.IRichText;
import org.modelio.edition.html.view.IRichTextCommands;
import org.modelio.edition.html.view.dialogs.AddTableDialog;
import org.modelio.edition.html.view.html.Table;

/**
 * Adds a table to a rich text control.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("0fe5f150-668f-4467-bf8b-3c4a4cff5571")
public class AddTableAction extends RichTextAction {
    /**
     * Creates a new instance.
     * @param richText the rich text
     */
    @objid ("f97c7a9e-1579-4f98-b96b-811930bc6c73")
    public AddTableAction(IRichText richText) {
        super(richText, IAction.AS_PUSH_BUTTON);
        setImageDescriptor(HtmlTextImages.IMG_DESC_ADD_TABLE);
        setDisabledImageDescriptor(HtmlTextImages.DISABLED_IMG_DESC_ADD_TABLE);
        setToolTipText(HtmlTextResources.addTableAction_toolTipText);
    }

    /**
     * Executes the action.
     * @param richText a rich text control
     */
    @objid ("52a85a58-80cf-4d17-a49b-940d57b1683d")
    @Override
    public void execute(IRichText richText) {
        if (richText != null) {
            AddTableDialog dialog = new AddTableDialog(Display.getCurrent()
                    .getActiveShell());
            dialog.open();
            if (dialog.getReturnCode() == Window.OK) {
                Table table = dialog.getTable();
                int rows = table.getRows();
                int cols = table.getColumns();
                String width = table.getWidth();
                int tableheaders = table.getTableHeaders();
                String summary = table.getSummary();
                String caption = table.getCaption();
                if (rows > 0 && cols > 0) {
                    richText
                            .executeCommand(
                                    IRichTextCommands.ADD_TABLE,
                                    new String[] {
                                            "" + rows, "" + cols, "" + width, summary, caption, "" + tableheaders }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                }
            }
        }
    }

}
