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
                                    

package org.modelio.diagram.api.plugin;

import java.util.Calendar;
import java.util.ResourceBundle;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.equinox.log.ExtendedLogService;
import org.modelio.log.writers.PluginLogger;
import org.modelio.ui.i18n.BundledMessages;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

@objid ("708000fe-a195-4a57-9480-888e6c62563b")
public class DiagramApi implements BundleActivator {
    @objid ("f52582fa-0137-47fc-9d49-8700fae62199")
    public static final String PLUGIN_ID = "org.modelio.diagram.api";

    @objid ("9879d70f-0a9d-4792-bfc7-f163d46c4c53")
    private static BundleContext context;

    @objid ("fec38e19-d9e4-45b7-b0c8-9b6e231876ae")
    public static BundledMessages I18N;

    @objid ("afab4201-9918-44d8-b2fd-40e30a937d6b")
    public static PluginLogger LOG = null;

    /**
     * (non-Javadoc)
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    @objid ("0caccc96-b6fa-4ff8-b840-8d1b4eb84b67")
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        assert (bundleContext.getBundle().getSymbolicName().equals(PLUGIN_ID));
        
        context = bundleContext;
        ServiceReference<ExtendedLogService> ref = bundleContext.getServiceReference(ExtendedLogService.class);
        ExtendedLogService service = bundleContext.getService(ref);
        LOG = new PluginLogger(service.getLogger(null));
        
        I18N = new BundledMessages(LOG, ResourceBundle.getBundle("diagramapi"));
        
        LOG.info( "Modelio/Diagram API " + 
                DiagramApi.getContext().getBundle().getVersion() + 
                " - Copyright 2013-" + 
                Calendar.getInstance().get(Calendar.YEAR) + 
                " ModelioSoft.");
    }

    /**
     * (non-Javadoc)
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    @objid ("fe663b14-2671-459e-a434-ed5b00253ac9")
    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        DiagramApi.context = null;
    }

    @objid ("a2f72bff-cd41-40fb-a667-cbc522993acf")
    public static BundleContext getContext() {
        return context;
    }

}
