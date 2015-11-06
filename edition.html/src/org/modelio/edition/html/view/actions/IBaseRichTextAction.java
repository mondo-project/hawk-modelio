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

/**
 * The base interface for a rich text action.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("2a2e2b5c-8fbb-4771-9f07-4483369bbd33")
public interface IBaseRichTextAction {
    /**
     * Returns the tool tip for the action.
     * @return the tool tip text
     */
    @objid ("ccd70651-1956-4ca0-886e-21b9fcf5272c")
    String getToolTipText();

    /**
     * Sets the tool tip for the action.
     * @param toolTipText the tool tip text
     */
    @objid ("de2c51c9-df8b-46bb-b64d-5ada7759a888")
    void setToolTipText(String toolTipText);

    /**
     * Returns the enabled status of the action.
     * @return <code>true</code> if enabled, <code>false</code> if not
     */
    @objid ("af67a3d0-52b6-4989-8d56-f38f21ece1eb")
    boolean getEnabled();

    /**
     * Enables or disables the action.
     * @param enabled If <code>true</code>, enable the action. if
     * <code>false</code>, disable it
     */
    @objid ("e79ace0a-9c92-4309-b527-b8600cabdd60")
    void setEnabled(boolean enabled);

}
