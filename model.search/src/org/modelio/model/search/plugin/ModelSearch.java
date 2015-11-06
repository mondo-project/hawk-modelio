package org.modelio.model.search.plugin;

import java.util.ResourceBundle;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.equinox.log.ExtendedLogService;
import org.modelio.log.writers.PluginLogger;
import org.modelio.ui.i18n.BundledMessages;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

@objid ("00190168-9e11-10ab-8258-001ec947cd2a")
public class ModelSearch implements BundleActivator {
    @objid ("00190956-9e11-10ab-8258-001ec947cd2a")
    public static final String PLUGIN_ID = "org.modelio.model.search";

    @objid ("00190aa0-9e11-10ab-8258-001ec947cd2a")
    private static BundleContext context;

    @objid ("00190db6-9e11-10ab-8258-001ec947cd2a")
    public static BundledMessages I18N;

    @objid ("00190ee2-9e11-10ab-8258-001ec947cd2a")
    public static PluginLogger LOG;

    @objid ("00190bae-9e11-10ab-8258-001ec947cd2a")
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        context = bundleContext;
        ServiceReference<ExtendedLogService> ref = bundleContext.getServiceReference(ExtendedLogService.class);
        ExtendedLogService service = bundleContext.getService(ref);
        LOG = new PluginLogger(service.getLogger(null));
        I18N = new BundledMessages(LOG, ResourceBundle.getBundle("modelsearch"));
    }

    @objid ("00190c6c-9e11-10ab-8258-001ec947cd2a")
    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        context = null;
    }

    @objid ("00190d0c-9e11-10ab-8258-001ec947cd2a")
    public static BundleContext getContext() {
        return context;
    }

}
