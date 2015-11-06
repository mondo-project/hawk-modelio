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
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * A composite for laying out the default rich text editor in an Eclipse form.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("bf895e08-a583-4fa0-857a-4f1140231dab")
public class RichTextEditorForm extends ViewForm {
    /**
     * Creates a new instance.
     * @param parent the parent control
     * @param style the style for this control
     */
    @objid ("9b1ac855-67a3-4bc7-b620-35cff371df64")
    public RichTextEditorForm(Composite parent, int style) {
        super(parent, style);
    }

    /**
     * @see org.eclipse.swt.widgets#getData(String key)
     */
    @objid ("aee93270-08c7-455e-926e-6264ccf3bbc1")
    @Override
    public Object getData(String key) {
        if (key != null && key.equals(FormToolkit.KEY_DRAW_BORDER)) {
            // Return a text border to get the FormToolkit's BorderPainter to
            // paint a border for the rich text editor.
            return FormToolkit.TEXT_BORDER;
        }
        return super.getData();
    }

}
