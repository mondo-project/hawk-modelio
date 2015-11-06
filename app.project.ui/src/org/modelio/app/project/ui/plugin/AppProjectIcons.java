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
                                    

package org.modelio.app.project.ui.plugin;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * @author phv
 */
@objid ("008b3012-44b3-1060-84ef-001ec947cd2a")
public final class AppProjectIcons {
    @objid ("00570080-b839-1061-b3c0-001ec947cd2a")
    public static final Image UNDEFINED = AbstractUIPlugin.imageDescriptorFromPlugin(AppProjectUi.PLUGIN_ID, "icons/sample.png")
            .createImage();

    @objid ("00570792-b839-1061-b3c0-001ec947cd2a")
    public static final Image EXML = AbstractUIPlugin.imageDescriptorFromPlugin(AppProjectUi.PLUGIN_ID, "icons/exmlfragment.png")
            .createImage();

    @objid ("00570de6-b839-1061-b3c0-001ec947cd2a")
    public static final Image RAMC = AbstractUIPlugin.imageDescriptorFromPlugin(AppProjectUi.PLUGIN_ID, "icons/ramcfragment.png")
            .createImage();

    @objid ("00571476-b839-1061-b3c0-001ec947cd2a")
    public static final Image HTTP = AbstractUIPlugin.imageDescriptorFromPlugin(AppProjectUi.PLUGIN_ID, "icons/httpfragment.png")
            .createImage();

    @objid ("00571b42-b839-1061-b3c0-001ec947cd2a")
    public static final Image MDA = AbstractUIPlugin.imageDescriptorFromPlugin(AppProjectUi.PLUGIN_ID, "icons/mdafragment.png")
            .createImage();

    @objid ("61a4e928-21cf-11e2-8fce-001ec947ccaf")
    public static final Image SVN = AbstractUIPlugin.imageDescriptorFromPlugin(AppProjectUi.PLUGIN_ID, "icons/svnfragment.png")
            .createImage();

    @objid ("0020ce02-4aef-1060-84ef-001ec947cd2a")
    private AppProjectIcons() {
        // no instances
    }

}
