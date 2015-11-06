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
                                    

package org.modelio.mda.infra.plugin;

import java.util.ResourceBundle;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.equinox.log.ExtendedLogService;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.modelio.log.writers.PluginLogger;
import org.modelio.ui.i18n.BundledMessages;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * MdaInfra plugin singleton class.
 */
@objid ("b349f6a5-f11c-11e1-af52-001ec947c8cc")
public class MdaInfra implements BundleActivator {
    /**
     * ID of the MdaInfra plugin.
     */
    @objid ("b5e7eb43-f11c-11e1-af52-001ec947c8cc")
    public static final String PLUGIN_ID = "org.modelio.mda.infra";

    @objid ("b349f6aa-f11c-11e1-af52-001ec947c8cc")
    private static BundleContext context;

    @objid ("b349f6ab-f11c-11e1-af52-001ec947c8cc")
    public static BundledMessages I18N;

    @objid ("b349f6ad-f11c-11e1-af52-001ec947c8cc")
    public static PluginLogger LOG = null;

    @objid ("b349f6ae-f11c-11e1-af52-001ec947c8cc")
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        context = bundleContext;
        ServiceReference<ExtendedLogService> ref = bundleContext.getServiceReference(ExtendedLogService.class);
        ExtendedLogService service = bundleContext.getService(ref);
        LOG = new PluginLogger(service.getLogger(bundleContext.getBundle(), PLUGIN_ID));
        I18N = new BundledMessages(LOG, ResourceBundle.getBundle("mdainfra"));
    }

    @objid ("b349f6b2-f11c-11e1-af52-001ec947c8cc")
    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        context = null;
    }

    @objid ("b34c58d3-f11c-11e1-af52-001ec947c8cc")
    public static BundleContext getContext() {
        return context;
    }

    @objid ("5b88fa6b-b8b1-4d2b-b2e5-93ff3b845aa8")
    public static ImageDescriptor getImageDescriptor(final String path) {
        return AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

}
