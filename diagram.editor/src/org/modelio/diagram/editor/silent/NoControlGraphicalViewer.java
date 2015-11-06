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
                                    

package org.modelio.diagram.editor.silent;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.ui.parts.GraphicalViewerImpl;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

@objid ("66995c40-33f7-11e2-95fe-001ec947c8cc")
final class NoControlGraphicalViewer extends GraphicalViewerImpl {
    @objid ("66995c41-33f7-11e2-95fe-001ec947c8cc")
    @Override
    public Control createControl(final Composite composite) {
        if (getRootEditPart() != null)
            getRootEditPart().activate();
        getLightweightSystem().setControl(null);
        return null;
    }

    @objid ("66995c48-33f7-11e2-95fe-001ec947c8cc")
    @Override
    protected LightweightSystem createLightweightSystem() {
        return new LightweightSystemNoCanvas();
    }

    /**
     * Overridden to force activation of the new root edit part despite the fact we have no Control.
     */
    @objid ("66995c4d-33f7-11e2-95fe-001ec947c8cc")
    @Override
    public void setRootEditPart(final RootEditPart editpart) {
        super.setRootEditPart(editpart);
        editpart.activate();
    }

}
