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
                                    

package org.modelio.diagram.editor.activity.editor;

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
@objid ("296a3a1a-55b6-11e2-877f-002564c97630")
public class ActivityDiagramEditor extends AbstractDiagramEditor {
    /**
     * public ID of this editor.
     */
    @objid ("296d475b-55b6-11e2-877f-002564c97630")
    public static final String ID = "org.modelio.diagram.editor.activity.ActivityDiagramEditorID";

    @objid ("ca6b72be-ae21-415e-afdf-82cb1083e6b5")
    private static final String POPUP_ID = "org.modelio.diagram.editor.activity.menu.popupmenu";

    @objid ("296d4761-55b6-11e2-877f-002564c97630")
    @Override
    public EditPartFactory getEditPartFactory() {
        // return an EditPart factory, here we can return the Modelio UML
        // standard EditPartFactory
        return ModelioEditPartFactory.getInstance();
    }

    /**
     * C'tor.
     */
    @objid ("296d4766-55b6-11e2-877f-002564c97630")
    public ActivityDiagramEditor() {
        super();
    }

    @objid ("296d4769-55b6-11e2-877f-002564c97630")
    @Override
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        
        final GraphicalViewer viewer = this.getGraphicalViewer();
        
        MoveKeyHandler kh = new MoveKeyHandler(viewer);
        // Bind F2 key to direct edit
        kh.put(KeyStroke.getPressed(SWT.F2, 0), this.getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT));
        kh.setParent(viewer.getKeyHandler());
        viewer.setKeyHandler(kh);
        
        // Add the contextual menu
        this.menuService.registerContextMenu(viewer.getControl(), "org.modelio.diagram.editor.activity.menu.popupmenu");
    }

    @objid ("40679590-d911-4b51-abd5-623c05b6e33e")
    @Override
    protected String getPopupId() {
        return POPUP_ID;
    }

}
