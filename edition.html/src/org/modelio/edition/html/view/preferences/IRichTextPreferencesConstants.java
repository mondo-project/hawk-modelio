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
package org.modelio.edition.html.view.preferences;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Preferences for the rich text editor.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("d94c625d-9390-44ea-b46e-34b29556e0f0")
public interface IRichTextPreferencesConstants {
    /**
     * rich text Editor preference keys.
     */
    @objid ("acb46060-7069-497c-9523-49e73375c0d6")
    public static final String LINE_WIDTH = "htmlEditor.lineWidth"; // $NON-NLS-1$

    @objid ("d9525e79-ba7a-4fb1-b69d-c353b494ee3d")
    public static final String INDENT = "htmlEditor.indent"; // $NON-NLS-1$

    @objid ("0a061b03-ad1c-4696-8153-80ec6b28cbc3")
    public static final String INDENT_SIZE = "htmlEditor.indentSize"; // $NON-NLS-1$

}
