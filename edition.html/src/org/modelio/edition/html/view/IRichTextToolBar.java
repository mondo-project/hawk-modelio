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
package org.modelio.edition.html.view;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ToolBarManager;
import org.modelio.edition.html.view.actions.RichTextComboAction;

/**
 * The interface for a rich text editor toolbar.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("2cf917d2-0201-4340-9ff9-e3ed3f61ac34")
public interface IRichTextToolBar {
    /**
     * Adds a button action to the tool bar.
     * @param action the button action to add
     */
    @objid ("6537a9ae-4612-4c19-9650-bb3848479535")
    void addAction(IAction action);

    /**
     * Adds a combo action to the tool bar.
     * @param item the combo action to add
     */
    @objid ("5eb212d8-fe8c-41cf-91f3-04ec7722bdf0")
    void addAction(RichTextComboAction item);

    /**
     * Adds a separator to the tool bar.
     */
    @objid ("0dfc7725-ab62-4299-92af-94d1a649f76e")
    void addSeparator();

    /**
     * Updates the toolbar state.
     * <p>
     * Enables/disables actions depending on the currently selected
     * RichTextEditor tab (RichText vs. HTML)
     * @param enabled specifies whether to enable non-ReadOnly commands
     */
    @objid ("f5de263b-3c35-4a1f-a787-ccde30377836")
    void updateToolBar(boolean enabled);

    /**
     * @return the toolbar manager
     */
    @objid ("3bb04171-ba08-44e0-b110-2977e4e8d2e9")
    ToolBarManager getToolbarMgr();

}
