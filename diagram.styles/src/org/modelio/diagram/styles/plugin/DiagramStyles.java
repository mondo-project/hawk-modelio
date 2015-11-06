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
                                    

package org.modelio.diagram.styles.plugin;

import java.util.ResourceBundle;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.equinox.log.ExtendedLogService;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.modelio.diagram.styles.manager.StyleManager;
import org.modelio.log.writers.PluginLogger;
import org.modelio.ui.i18n.BundledMessages;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

@objid ("9dda9565-18fe-11e2-92d2-001ec947c8cc")
public class DiagramStyles implements BundleActivator {
    /**
     * ID of the ModuleBrowserCommands plugin.
     */
    @objid ("a67cb963-18fe-11e2-92d2-001ec947c8cc")
    public static final String PLUGIN_ID = "org.modelio.diagram.styles";

    @objid ("866f3e6a-1926-11e2-92d2-001ec947c8cc")
    public static final String PROJECT_STYLE_SUBDIR = "data/.config/styles";

    @objid ("866f3e6f-1926-11e2-92d2-001ec947c8cc")
    public static final String STYLE_FILE_EXTENSION = ".style";

    @objid ("866f3e74-1926-11e2-92d2-001ec947c8cc")
    public static final String DEFAULT_STYLE_NAME = "default";

    @objid ("daba525a-4f4f-4000-92e1-a14b0e0816e6")
    private static BundleContext context;

    @objid ("dc8db742-0291-4c50-80eb-8e52295233c5")
    public static final String RAMC_STYLE_NAME = "ramc";

    @objid ("9a6f0aa4-4dc4-43f3-9fdc-3705c7d69238")
    public static final String USECASE_STYLE_NAME = "use case";

    @objid ("e4546265-f81d-4d33-91bf-071f52ac7029")
    public static final String MAIN_STYLE_NAME = "main";

    @objid ("7da1e9fb-1560-40dd-b46b-c05c36ac68cd")
    public static final String EXTERN_STYLE_NAME = "extern";

    @objid ("880a0009-d362-4a4c-b49c-8ce555781eb1")
    public static final String INTERN_STYLE_NAME = "intern";

    @objid ("a67cb94d-18fe-11e2-92d2-001ec947c8cc")
    public static BundledMessages I18N = null;

    @objid ("a67cb94c-18fe-11e2-92d2-001ec947c8cc")
    public static PluginLogger LOG = null;

    @objid ("85453877-1926-11e2-92d2-001ec947c8cc")
    private static StyleManager styleManager = new StyleManager();

    @objid ("a67cb950-18fe-11e2-92d2-001ec947c8cc")
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        context = bundleContext;
        ServiceReference<ExtendedLogService> ref = bundleContext.getServiceReference(ExtendedLogService.class);
        ExtendedLogService service = bundleContext.getService(ref);
        LOG = new PluginLogger(service.getLogger(bundleContext.getBundle(), PLUGIN_ID));
        I18N = new BundledMessages(LOG, ResourceBundle.getBundle("diagramstyles"));
    }

    @objid ("a67cb954-18fe-11e2-92d2-001ec947c8cc")
    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        context = null;
    }

    @objid ("a67cb958-18fe-11e2-92d2-001ec947c8cc")
    public static BundleContext getContext() {
        return context;
    }

    @objid ("85453878-1926-11e2-92d2-001ec947c8cc")
    public static StyleManager getStyleManager() {
        return styleManager;
    }

    @objid ("85479ad0-1926-11e2-92d2-001ec947c8cc")
    public static ImageDescriptor getImageDescriptor(final String path) {
        return AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

}
