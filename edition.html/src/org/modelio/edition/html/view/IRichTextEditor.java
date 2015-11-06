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
                                    

package org.modelio.edition.html.view;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.edition.html.view.actions.FindReplaceAction;

/**
 * The interface for a rich text editor.
 * <p>
 * A rich text editor is a composite user interface object that includes a tool
 * bar, a tab folder for entering the rich text content, and a tab folder for
 * viewing and modifying the rich text content in a markup language.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("f9da037a-4c2e-4173-9b06-988d3d36860d")
public interface IRichTextEditor extends IRichText {
    /**
     * Fills the tool bar with rich text action items.
     * @param toolBar a rich text editor tool bar
     */
    @objid ("6a701210-90ee-41ee-97c9-253e3bc4357f")
    void fillToolBar(IRichTextToolBar toolBar);

    /**
     * Selects the rich text or the markup language tab.
     * @param index <code>0</code> for the rich text tab, <code>1</code> for
     * the markup language tab
     */
    @objid ("27f153ef-fa89-46c7-8ede-f7ced6462826")
    void setSelection(int index);

    /**
     * Sets the FindReplaceAction to use.
     * @param findReplaceAction the FindReplaceAction to use.
     */
    @objid ("ec25dece-37db-420d-a7cd-92584aaaeba8")
    @Override
    void setFindReplaceAction(FindReplaceAction findReplaceAction);

}
