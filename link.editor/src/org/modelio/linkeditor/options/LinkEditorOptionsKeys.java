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
                                    

package org.modelio.linkeditor.options;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.PlatformUI;

/**
 * This class groups together the keys used to store some of the LinkEditor options. It also use a static block to
 * initialise their default values.
 * 
 * @author fpoyer
 */
@objid ("1b50fc8d-5e33-11e2-b81d-002564c97630")
public class LinkEditorOptionsKeys {
    @objid ("1b50fc8f-5e33-11e2-b81d-002564c97630")
    public static final String FROM_DEPTH = "org.modelio.linkeditor.fromDepth";

    @objid ("1b50fc91-5e33-11e2-b81d-002564c97630")
    public static final String TO_DEPTH = "org.modelio.linkeditor.toDepth";

    @objid ("1b50fc93-5e33-11e2-b81d-002564c97630")
    public static final String DEPENDENCY_FILTER = "org.modelio.linkeditor.dependencyFilter";

    /**
     * Initialise the default values for most options (only fonts are left to their system default value).
     */
    @objid ("1b50fc95-5e33-11e2-b81d-002564c97630")
    public static void initialiseDefaults() {
        IPreferenceStore store = PlatformUI.getPreferenceStore();
        store.setDefault(LinkEditorOptionsKeys.FROM_DEPTH, 1);
        store.setDefault(LinkEditorOptionsKeys.TO_DEPTH, 1);
        store.setDefault(LinkEditorOptionsKeys.DEPENDENCY_FILTER, "");
    }

}
