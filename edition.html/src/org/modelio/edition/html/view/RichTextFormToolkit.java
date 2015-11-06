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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * A helper toolkit for creating a rich text control and editor that can be added to an Eclipse form.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("45af00c5-d937-48c1-bec7-b90a54cc57e0")
public class RichTextFormToolkit {
    /**
     * Creates a rich text control and adapts it to be used in a form.
     * @param toolkit the form toolkit
     * @param parent the parent control
     * @param text the initial text for the viewer
     * @param style the initial style for the viewer
     * @return a new <code>IRichText</code> instance
     */
    @objid ("70ac6a62-ed6d-47c3-aa82-b4563ce3fa66")
    public static IRichText createRichText(FormToolkit toolkit, Composite parent, String text, int style) {
        IRichText richText = new RichText(parent, toolkit.getBorderStyle() | style | toolkit.getOrientation());
        richText.getControl().setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
        if (text != null) {
            richText.setText(text);
        }
        return richText;
    }

    /**
     * Creates a rich text editor and adapts it to be used in a form.
     * @param toolkit the form toolkit
     * @param parent the parent control
     * @param text the initial text for the viewer
     * @param style the initial style for the viewer
     * @return a new <code>IRichText</code> instance
     */
    @objid ("a2882a43-6703-4612-b3a8-6d8f44a26ad4")
    public static IRichTextEditor createRichTextEditor(FormToolkit toolkit, Composite parent, String text, int style) {
        IRichTextEditor richTextEditor = new RichTextEditor(parent, toolkit.getBorderStyle() | style | toolkit.getOrientation());
        richTextEditor.getControl().setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
        if (text != null) {
            richTextEditor.setInitialText(text);
        }
        return richTextEditor;
    }

}
