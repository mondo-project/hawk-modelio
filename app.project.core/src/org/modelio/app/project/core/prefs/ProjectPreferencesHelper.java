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
                                    

package org.modelio.app.project.core.prefs;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.preference.IPreferenceStore;
import org.modelio.app.project.core.plugin.AppProjectCore;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.metamodel.uml.statik.GeneralClass;
import org.modelio.metamodel.uml.statik.VisibilityMode;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

@objid ("c9b90a49-bee2-44b6-912a-05ec489fb0ec")
public class ProjectPreferencesHelper {
    @objid ("e1d27b68-4ab8-40cc-a8a2-09b6c817e4f4")
    public static GeneralClass getAttributeDefaultType(IProjectService projectService) {
        return getGeneralClass(projectService, ProjectPreferencesKeys.ATT_DEFAULT_TYPE_PREFKEY);
    }

    @objid ("b0a7d624-5399-4215-ba57-6fae3a3c4673")
    public static VisibilityMode getAttributeDefaultVisibility(IProjectService projectService) {
        IPreferenceStore prefs = projectService.getProjectPreferences(AppProjectCore.PLUGIN_ID);
        String value = prefs.getString(ProjectPreferencesKeys.ATT_DEFAULT_VIS_PREFKEY);
        return VisibilityMode.get(value);
    }

    @objid ("fffae056-a5e9-4dd4-abc8-389497e4fcc9")
    public static GeneralClass getParameterDefaultType(IProjectService projectService) {
        return getGeneralClass(projectService, ProjectPreferencesKeys.PARAM_DEFAULT_TYPE_PREFKEY);
    }

    @objid ("2ff59ce5-9c13-4061-8e5b-6e56e867d5c8")
    public static GeneralClass getReturnDefaultType(IProjectService projectService) {
        return getGeneralClass(projectService, ProjectPreferencesKeys.RETURN_DEFAULT_TYPE_PREFKEY);
    }

    @objid ("47912c53-2c9e-4159-99b1-562366fe9531")
    private static GeneralClass getGeneralClass(IProjectService projectService, String key) {
        IPreferenceStore prefs = projectService.getProjectPreferences(ProjectPreferencesKeys.NODE_ID);
        ICoreSession session = projectService.getOpenedProject().getSession();
        String value = prefs.getString(key);
        try {
            MRef mref = new MRef(value);
            MObject o = session.getModel().findByRef(mref);
            return (o instanceof GeneralClass)? (GeneralClass)o : null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

}
