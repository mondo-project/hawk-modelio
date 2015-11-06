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
                                    

package org.modelio.app.project.conf.dialog.common;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.graphics.Image;
import org.modelio.api.module.IModule;
import org.modelio.core.ui.images.MetamodelImageService;
import org.modelio.core.ui.images.ModuleI18NService;
import org.modelio.gproject.module.GModule;
import org.modelio.mda.infra.service.IModuleService;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.mda.ModuleComponent;

@objid ("a7365ad0-33f6-11e2-a514-002564c97630")
public class ModuleHelper {
    @objid ("a7365ad1-33f6-11e2-a514-002564c97630")
    public static Image getIcon(Object element) {
        if (element instanceof GModule) {
            Image moduleImage = ModuleI18NService.getModuleImage(((GModule)element).getModuleElement());
            if (moduleImage != null) {
                return moduleImage;
            }
        }
        if (element instanceof IModule) {
            Image moduleImage = ((IModule)element).getModuleImage();
            if (moduleImage != null) {
                return moduleImage;
            }
        }
        return MetamodelImageService.getIcon(Metamodel.getMClass(ModuleComponent.class));
    }

    @objid ("a7365ad6-33f6-11e2-a514-002564c97630")
    public static String getName(Object element) {
        if (element instanceof GModule) {
            return ((GModule) element).getName();
        }
        if (element instanceof IModule) {
            return ((IModule)element).getName();
        }
        return "???";
    }

    @objid ("a7365adb-33f6-11e2-a514-002564c97630")
    public static String getLabel(Object element, IModuleService moduleService) {
        if (element instanceof GModule) {
            IModule iModule = moduleService.getIModule(((GModule) element).getModuleElement());
            if (iModule != null) { 
                return iModule.getLabel();
            }
            return ((GModule) element).getName();
        }
        if (element instanceof IModule) {
            return ((IModule)element).getLabel();
        }
        return "???";
    }

    @objid ("a7365ae0-33f6-11e2-a514-002564c97630")
    public static String getVersion(Object element) {
        if (element instanceof GModule) {
            return ((GModule) element).getVersion().toString();
        }
        if (element instanceof IModule) {
            return ((IModule)element).getVersion().toString();
        }
        return "";
    }

}
