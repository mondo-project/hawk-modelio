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
                                    

package org.modelio.diagram.editor.deployment.editor;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.swt.SWT;
import org.modelio.diagram.editor.AbstractDiagramEditor;
import org.modelio.diagram.editor.MoveKeyHandler;
import org.modelio.diagram.elements.editpartFactory.ModelioEditPartFactory;

/**
 * Graphical Editor for Diagrams.
 */
@objid ("970e28dd-55b6-11e2-877f-002564c97630")
public class DeploymentDiagramEditor extends AbstractDiagramEditor {
    /**
     * public ID of this editor.
     */
    @objid ("970e28e1-55b6-11e2-877f-002564c97630")
    public static final String ID = "org.modelio.diagram.editor.deployment.DeploymentDiagramEditorID";

    @objid ("400964e8-8d32-4993-8c38-ed78ee7ff557")
    private static final String POPUP_ID = "org.modelio.diagram.editor.deployment.menu.popupmenu";

    /**
     * C'tor.
     */
    @objid ("970e28e7-55b6-11e2-877f-002564c97630")
    public DeploymentDiagramEditor() {
        super();
    }

    @objid ("970e28ea-55b6-11e2-877f-002564c97630")
    @Override
    public EditPartFactory getEditPartFactory() {
        // return an EditPart factory, here we can return the Modelio UML
        // standard EditPartFactory
        return ModelioEditPartFactory.getInstance();
    }

    @objid ("970e28ef-55b6-11e2-877f-002564c97630")
    @Override
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        
        final GraphicalViewer viewer = this.getGraphicalViewer();
        
        MoveKeyHandler kh = new MoveKeyHandler(viewer);
        // Bind F2 key to direct edit
        kh.put(KeyStroke.getPressed(SWT.F2, 0),
               this.getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT));
        kh.setParent(viewer.getKeyHandler());
        viewer.setKeyHandler(kh);
    }

    @objid ("3a229f64-1ada-4d96-923c-a35471bda262")
    @Override
    protected String getPopupId() {
        return POPUP_ID;
    }

}
