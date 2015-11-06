/*
 * Copyright 2013 Modeliosoft
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *        
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */  
                                    

package org.modelio.admtool.plugin;

import java.util.ResourceBundle;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.equinox.log.ExtendedLogService;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.modelio.log.writers.PluginLogger;
import org.modelio.ui.i18n.BundledMessages;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

@objid ("33beb656-1c91-4a87-ae51-11ffff75a345")
public class AmdTool extends AbstractUIPlugin {
    @objid ("4a890383-81b5-4491-8712-d5579777f905")
    public static final String PLUGIN_ID = "org.modelio.admtool"; // $NON-NLS-1$

    @objid ("9fcdd022-2ad3-4ce1-8ca8-e5d21bbdc025")
    public static PluginLogger LOG;

    @objid ("9819a69b-38b6-46e2-aef8-6d02e6fb950e")
    public static BundledMessages I18N;

    @objid ("418ffdaa-7867-4312-8ca2-c66096e94634")
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        ServiceReference<ExtendedLogService> ref = bundleContext.getServiceReference(ExtendedLogService.class);
        ExtendedLogService service = bundleContext.getService(ref);
        LOG = new PluginLogger(service.getLogger(null));
        I18N = new BundledMessages(LOG, ResourceBundle.getBundle("admtool"));
    }

    @objid ("a5fef189-2227-40b0-8d45-c67df88b2ee1")
    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        // Nothing to do
    }

}
