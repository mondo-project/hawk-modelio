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
                                    

/**
 * 
 */
package org.modelio.linkeditor.zoom;

import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.gef.editparts.ZoomManager;
import org.modelio.linkeditor.view.LinkEditorView;

/**
 * Handler that will reset zoom level to 1.0 in the link editor.
 * 
 * @author fpoyer
 */
@objid ("1bc0dcf5-5e33-11e2-b81d-002564c97630")
public class ZoomToDefaultHandler {
    @objid ("1bc0dcf7-5e33-11e2-b81d-002564c97630")
    @Execute
    public Object execute(@Named(IServiceConstants.ACTIVE_PART) final MPart part) {
        if (! (part.getObject() instanceof LinkEditorView)) {
            return null;
        }
        
        LinkEditorView editor = (LinkEditorView) part.getObject();
        
        ZoomManager zoomManager = editor.getAdapter(ZoomManager.class);
        
        if (zoomManager != null && zoomManager.getMinZoom() < 1.0 && 1.0 < zoomManager.getMaxZoom()) {
            zoomManager.setZoom(1.0);
        }
        return null;
    }

}
