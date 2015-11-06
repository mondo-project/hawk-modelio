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
                                    

//------------------------------------------------------------------------------
// All rights reserved. This program and the accompanying materials
// are made available under the terms of the Eclipse Public License v1.0
// which accompanies this distribution, and is available at
// http://www.eclipse.org/legal/epl-v10.html
//
// Contributors:
// IBM Corporation - initial implementation
//------------------------------------------------------------------------------
package org.modelio.edition.html.plugin;

import java.io.IOError;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.equinox.log.ExtendedLogService;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.modelio.log.writers.PluginLogger;
import org.modelio.ui.i18n.BundledMessages;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * The Rich Text plug-in class.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("7dd0cfbc-4c15-4212-8139-3366853de849")
public class HtmlTextPlugin implements BundleActivator {
    @objid ("01fbc7a8-bde0-43d7-8ae1-0f03b28cf95b")
    public static String PLUGIN_ID;

// The shared plug-in instance.
    @objid ("dcbe1cc7-bc28-45ea-abff-5e4de591c173")
    private static HtmlTextPlugin plugin;

    @objid ("bdb6d9d6-bcf3-46ec-a127-a8d956e3d2a4")
    public static PluginLogger LOG;

    @objid ("a30b33dd-5261-410c-be99-fdab56b7b85f")
    public static BundledMessages I18N;

    @objid ("08196d36-ec11-4df4-a362-1b28c228a144")
    private BundleContext bundleContext;

    @objid ("0ed4bf60-2d19-431f-b75c-4aaf8864acc1")
    private URL iconURL;

    @objid ("b5ff4530-5a11-4240-9939-054b72d0304d")
    private ImageRegistry sharedImages;

    /**
     * Default constructor.
     */
    @objid ("7c1a2b6f-d0fd-4773-bb79-cc53a4ee2159")
    public HtmlTextPlugin() {
        super();
        plugin = this;
    }

    @objid ("a475c693-8dca-419d-b958-426fe628ff5f")
    @Override
    public void start(BundleContext context) throws Exception {
        final Bundle bundle = context.getBundle();
        
        this.bundleContext = context;
        this.iconURL = bundle.getEntry("icons");
        this.sharedImages = new ImageRegistry(Display.getDefault());
        
        PLUGIN_ID = bundle.getSymbolicName();
        
        ServiceReference<ExtendedLogService> ref = context.getServiceReference(ExtendedLogService.class);
        ExtendedLogService service = context.getService(ref);
        LOG = new PluginLogger(service.getLogger(bundle, PLUGIN_ID));
        
        I18N = new BundledMessages(LOG, ResourceBundle.getBundle("Resources"));
        
        //setDebugging(true);
    }

    @objid ("d798b5aa-3f6c-44f9-a34b-05529e3c9d5b")
    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        this.sharedImages.dispose();
    }

    /**
     * @return the shared plug-in instance.
     */
    @objid ("e5697030-7888-4dd9-a51e-24d0872c8605")
    public static HtmlTextPlugin getDefault() {
        return plugin;
    }

    /**
     * @return the plugin log
     */
    @objid ("f04318d6-2f0c-4fb2-bb9c-804319abbf59")
    public PluginLogger getLogger() {
        return LOG;
    }

    /**
     * Returns the shared image given the relative path.
     * <p>
     * Note: The returned image will be automatically freed when the plug-in
     * shuts down.
     * @param relativePath The image's path relative to the plug-in's root.
     * @return The image.
     */
    @objid ("62fbdae8-8947-459a-a57f-29e1147ef2c8")
    public Image getSharedImage(String relativePath) {
        Image image = this.sharedImages.get(relativePath);
        if (image != null) {
            return image;
        }
        
        ImageDescriptor imageDescriptor = getImageDescriptor(relativePath);
        if (imageDescriptor != null) {
            image = imageDescriptor.createImage(false);
            if (image != null) {
                this.sharedImages.put(relativePath, image);
            }
        }
        return image;
    }

    /**
     * Returns the image descriptor given the relative path.
     * @param relativePath The image's path relative to the plug-in's root.
     * @return The image descriptor.
     */
    @objid ("7661543f-b2a4-4915-9302-6c145007885e")
    public ImageDescriptor getImageDescriptor(String relativePath) {
        try {
            URL url = new URL(this.iconURL, relativePath);
            return ImageDescriptor.createFromURL(url);
        } catch (MalformedURLException e) {
            return ImageDescriptor.getMissingImageDescriptor();
        }
    }

    @objid ("58707957-a4d8-4b9e-aa42-e3a3d9e625e0")
    public String getInstallPath() {
        URL url = this.bundleContext.getBundle().getEntry("/");
        try {
            url = FileLocator.toFileURL(url);
            return url.getPath();
        } catch (IOException e) {
            throw new IOError(e);
        }
    }

    @objid ("1739a29a-c8bb-4276-8395-4357c92432dd")
    public boolean isDebugging() {
        return LOG.isDebugEnabled();
    }

}
