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
                                    

package org.modelio.diagram.elements.core.model;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.modelio.diagram.elements.plugin.DiagramElements;
import org.osgi.framework.Bundle;

/**
 * Service class used to get stereotype icon and image.
 * 
 * @author cmarin
 */
@objid ("8084e15a-1dec-11e2-8cad-001ec947c8cc")
public class ImageServices {
    /**
     * The image used when no stereotype image is available.
     */
    @objid ("8084e15c-1dec-11e2-8cad-001ec947c8cc")
    private static Image noImage = null;

    /**
     * Get the image used when no stereotype image is available.
     * @return The default image used when no stereotype image is available.
     */
    @objid ("8084e15e-1dec-11e2-8cad-001ec947c8cc")
    public static Image getNoImageImage() {
        // FIXME move this somewhere else...
        if (noImage == null) {
            Bundle bundle = Platform.getBundle(DiagramElements.PLUGIN_ID);
        
            ImageDescriptor image = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/no_image.png"), null));
            noImage = image.createImage();
        }
        return noImage;
    }

}
