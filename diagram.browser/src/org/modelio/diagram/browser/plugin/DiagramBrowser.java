package org.modelio.diagram.browser.plugin;

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

@objid ("00693098-e064-10ce-896b-001ec947cd2a")
public class DiagramBrowser implements BundleActivator {
    @objid ("002d3aa2-e077-10ce-896b-001ec947cd2a")
    public static final String PLUGIN_ID = "org.modelio.diagram.browser";

    @objid ("002bd324-e077-10ce-896b-001ec947cd2a")
    private static BundleContext context;

    @objid ("002bda40-e077-10ce-896b-001ec947cd2a")
    public static BundledMessages I18N;

    @objid ("002be1e8-e077-10ce-896b-001ec947cd2a")
    public static PluginLogger LOG;

    @objid ("002be986-e077-10ce-896b-001ec947cd2a")
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        context = bundleContext;
        ServiceReference<ExtendedLogService> ref = bundleContext.getServiceReference(ExtendedLogService.class);
        ExtendedLogService service = bundleContext.getService(ref);
        LOG = new PluginLogger(service.getLogger(null));
        I18N = new BundledMessages(LOG, ResourceBundle.getBundle("diagrambrowser"));
    }

    @objid ("002c092a-e077-10ce-896b-001ec947cd2a")
    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        context = null;
    }

    @objid ("002c259a-e077-10ce-896b-001ec947cd2a")
    public static BundleContext getContext() {
        return context;
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in
     * relative path
     * @param path the path
     * @return the image descriptor
     */
    @objid ("2889f8e7-4ab5-11e2-a4d3-002564c97630")
    public static ImageDescriptor getImageDescriptor(final String path) {
        return AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

}
