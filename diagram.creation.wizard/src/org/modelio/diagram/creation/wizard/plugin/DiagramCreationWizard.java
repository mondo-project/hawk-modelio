package org.modelio.diagram.creation.wizard.plugin;

import java.util.ResourceBundle;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.equinox.log.ExtendedLogService;
import org.modelio.log.writers.PluginLogger;
import org.modelio.ui.i18n.BundledMessages;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * This class represents the diagram.editor plugin.
 */
@objid ("f22d0c67-f1bd-4faa-b605-69a288a3bbc6")
public class DiagramCreationWizard implements BundleActivator {
    /**
     * ID of the DiagramEditor plugin.
     */
    @objid ("361a4590-1ce6-4d1a-b9f6-e84389c22064")
    public static final String PLUGIN_ID = "org.modelio.diagram.creation.wizard";

    @objid ("9e417917-778f-4f8a-8b3c-50b607e8937e")
    private static BundleContext context;

    @objid ("8b2a16db-7406-462b-b7a2-a1c2ebf329fb")
    public static BundledMessages I18N = null;

    @objid ("d9888eee-7897-42af-85f5-6b07f757d665")
    public static PluginLogger LOG = null;

    @objid ("703861fa-b64d-49ec-891a-06350e0f905b")
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        context = bundleContext;
        ServiceReference<ExtendedLogService> ref = bundleContext.getServiceReference(ExtendedLogService.class);
        ExtendedLogService service = bundleContext.getService(ref);
        LOG = new PluginLogger(service.getLogger(bundleContext.getBundle(), PLUGIN_ID));
        I18N = new BundledMessages(LOG, ResourceBundle.getBundle("diagramcreationwizard"));
    }

    @objid ("ba7bd556-8714-4476-87b7-038f4d1e1fd9")
    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        context = null;
    }

    @objid ("587ca078-a45d-4a90-8531-370eea7ff101")
    public static BundleContext getContext() {
        return context;
    }

}
