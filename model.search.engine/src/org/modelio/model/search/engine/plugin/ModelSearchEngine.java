package org.modelio.model.search.engine.plugin;

import java.util.ResourceBundle;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.equinox.log.ExtendedLogService;
import org.modelio.log.writers.PluginLogger;
import org.modelio.ui.i18n.BundledMessages;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

@objid ("67b6228a-8b33-4b20-8b29-90d6cebd123a")
public class ModelSearchEngine implements BundleActivator {
    @objid ("9d550847-c6b7-4025-a79c-25b9660ab669")
    public static final String PLUGIN_ID = "org.modelio.model.search.engine";

    @objid ("fa875103-3f39-4451-96e9-24e3dc5949ea")
    private static BundleContext context;

    @objid ("dcf470a7-93fb-4cc2-af2a-eade4448c4e7")
    public static BundledMessages I18N;

    @objid ("15991694-78aa-4e07-877e-46494bd26113")
    public static PluginLogger LOG;

    @objid ("2a5338e6-1733-49b1-8779-0647ea868fb0")
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        context = bundleContext;
        ServiceReference<ExtendedLogService> ref = bundleContext.getServiceReference(ExtendedLogService.class);
        ExtendedLogService service = bundleContext.getService(ref);
        LOG = new PluginLogger(service.getLogger(null));
        I18N = new BundledMessages(LOG, ResourceBundle.getBundle("modelsearch"));
    }

    @objid ("8f1b14b3-ea91-4edf-9995-f1ef94a48012")
    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        context = null;
    }

    @objid ("72eb7b06-5643-4a77-83e1-f6bf51d2b29c")
    public static BundleContext getContext() {
        return context;
    }

}
