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
                                    

package org.modelio.core.ui.images;

import java.net.URL;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.modelio.core.ui.plugin.CoreUi;
import org.modelio.vcore.smkernel.mapi.MClass;

/**
 * The MetamodelImageService provides icons and images for metaclasses.
 * The returned icons (or images) only represents the metaclass (no additional
 * decorations).
 * 
 * In some particular cases, a single metaclass can have different icons. In
 * this case, use the 'flavor' parameter to distinguish between them (otherwise
 * use null as 'flavor' value or call the methods without 'flavor' parameter).
 * 
 * @author phv
 */
@objid ("002642e2-5f24-100d-835d-001ec947cd2a")
public class MetamodelImageService {
    @objid ("0093f13e-a43d-100e-835d-001ec947cd2a")
    private static final String IMAGES_PATH = "icons/mmimages/";

    @objid ("00941ee8-a43d-100e-835d-001ec947cd2a")
    private static final String IMAGES_EXTENSION = ".png";

    @objid ("003a53fe-6af9-100d-835d-001ec947cd2a")
    private static final ImageRegistry registry = new ImageRegistry();

    @objid ("003a5c1e-6af9-100d-835d-001ec947cd2a")
    public static Image getIcon(MClass metaclass, String flavor) {
        final String key = getIconKey(metaclass, flavor);
        Image image = registry.get(key);
        
        if (image == null) {
            image = loadImage(key);
            if (image != null) {
                registry.put(key, image);
            } else {
                CoreUi.LOG.debug("MetamodelService: no icon file for '%s'", key);
            }
        }
        return image;
    }

    @objid ("003aa3f4-6af9-100d-835d-001ec947cd2a")
    public static Image getImage(MClass metaclass, String flavor) {
        final String key = getImageKey(metaclass, flavor);
        Image image = registry.get(key);
        
        if (image == null) {
            image = loadImage(key);
            if (image != null) {
                registry.put(key, image);
            } else {
                CoreUi.LOG.debug("MetamodelService: no image file for '%s'", key);
            }
        }
        return image;
    }

    @objid ("003ac9e2-6af9-100d-835d-001ec947cd2a")
    private static String getImageKey(MClass metaclass, String flavor) {
        return getIconKey(metaclass, flavor) + ".image";
    }

    @objid ("003aefee-6af9-100d-835d-001ec947cd2a")
    private static String getIconKey(MClass metaclass, String flavor) {
        final String className = metaclass.getName();
        if (flavor == null || flavor.isEmpty()) {
            return className;
        } else {
        
            final StringBuilder keyBuffer = new StringBuilder(className.length() + flavor.length());
            keyBuffer.append(className);
            keyBuffer.append(".");
            keyBuffer.append(flavor);
            return keyBuffer.toString();
        }
    }

    @objid ("003b0c4a-6af9-100d-835d-001ec947cd2a")
    private static Image loadImage(String key) {
        ImageDescriptor desc = null;
        Image image = null;
        
        // Get the relative file name
        final StringBuilder path = new StringBuilder(IMAGES_PATH);
        path.append(key.toLowerCase());
        path.append(IMAGES_EXTENSION);
        
        final IPath imagePath = new Path(path.toString());
        URL url = FileLocator.find(CoreUi.getContext().getBundle(), imagePath, null);
        
        if (url != null) {
            desc = ImageDescriptor.createFromURL(url);
            image = desc.createImage();
            assert (image != null);
        }
        return image;
    }

    @objid ("00526976-030e-106c-bf4f-001ec947cd2a")
    public static Image getIcon(MClass metaclass) {
        return getIcon(metaclass, null);
    }

    @objid ("0052ae04-030e-106c-bf4f-001ec947cd2a")
    public static Image getImage(MClass metaclass) {
        return getImage(metaclass, null);
    }

}
