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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.ToolItem;
import org.modelio.edition.html.view.IRichText;

/**
 * The interface for a rich text button action.
 * 
 * @author Kelvin Low
 * @author Jeff Hardy
 * @since 1.0
 */
@objid ("3737d7a7-f374-444f-b6ad-eb5babd1d0be")
public interface IRichTextAction extends IBaseRichTextAction {
    /**
     * Returns the image for the action.
     * @return the image for the action
     */
    @objid ("d76bc0ba-c826-4973-85da-d3de1eac00ff")
    Image getImage();

    /**
     * Sets the image for the action.
     * @param image the image for the action
     */
    @objid ("97315b0a-bb81-4a55-b9b2-fd16dfe06e63")
    void setImage(Image image);

    /**
     * Returns the disabled image for the action.
     * @return the disabled image for the action
     */
    @objid ("d7038f3b-6f57-4cb3-a720-0d4e33092346")
    Image getDisabledImage();

    /**
     * Sets the disabled image for the action.
     * @param image the disabled image for the action
     */
    @objid ("a66d98b5-77a2-446b-a2d1-b3b3954f9d3c")
    void setDisabledImage(Image image);

    /**
     * @return <code>true</code> if this action should be disabled when the
     * rich text editor is in readonly mode.
     */
    @objid ("d0c748c9-bc12-4c7d-919e-dbf825f48c51")
    boolean disableInReadOnlyMode();

    /**
     * @return <code>true</code> if this action should be disabled when the
     * rich text editor is in source edit mode.
     */
    @objid ("e34061ef-da8c-428f-8eac-224e2cdf9ef9")
    boolean disableInSourceMode();

    /**
     * Executes the action.
     * @param richText a rich text control
     */
    @objid ("375fae7c-593d-4623-a81c-d7cd88a39ef7")
    void execute(IRichText richText);

    /**
     * Sets the action's ToolItem
     * @param toolItem a tool item
     */
    @objid ("26542988-f11d-4d24-ac72-02c15109df76")
    void setToolItem(ToolItem toolItem);

    /**
     * @return the SWT ToolItem Style to be used for this action
     */
    @objid ("8c12e94f-f2cc-4423-b5bc-2c9c8f0ff642")
    int getStyle();

}
