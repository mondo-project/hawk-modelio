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
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.modelio.edition.html.plugin.HtmlTextPlugin;

/**
 * The rich text preferences initializer.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("1ad769a9-3e08-4603-a54d-876b515bd921")
public class RichTextPreferencesInitializer extends AbstractPreferenceInitializer implements IRichTextPreferencesConstants {
    /**
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
     */
    @objid ("bb47a9d4-61ac-4549-83da-369d55e9125c")
    public void initializeDefaultPreferences() {
        IEclipsePreferences node = DefaultScope.INSTANCE.getNode(HtmlTextPlugin.PLUGIN_ID);
        initializeDefaultPreferences(node);
    }

    /**
     * Initializes the default preferences.
     */
    @objid ("fc008bf7-176c-4cc5-9d93-32b2cc18d85f")
    public static void initializeDefaultPreferences(IEclipsePreferences node) {
        node.putInt(LINE_WIDTH, 120);
        node.putBoolean(INDENT, true);
        node.putInt(INDENT_SIZE, 4);
    }

}
