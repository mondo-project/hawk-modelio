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
                                    

package org.modelio.diagram.editor.usecase.editor;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.swt.SWT;
import org.modelio.diagram.editor.AbstractDiagramEditor;
import org.modelio.diagram.editor.MoveKeyHandler;
import org.modelio.diagram.elements.editpartFactory.ModelioEditPartFactory;

@objid ("5e303f9c-55b7-11e2-877f-002564c97630")
public class UseCaseDiagramEditor extends AbstractDiagramEditor {
    @objid ("5e303fa0-55b7-11e2-877f-002564c97630")
    public static final String ID = "org.modelio.diagram.editor.usecase.UseCaseDiagramEditorID";

    @objid ("e5b650fd-425d-4772-ac89-e72eca9578cf")
    private static final String POPUP_ID = "org.modelio.diagram.editor.usecase.menu.popupmenu";

    @objid ("5e31c5fc-55b7-11e2-877f-002564c97630")
    public UseCaseDiagramEditor() {
        super();
    }

    @objid ("5e31c5ff-55b7-11e2-877f-002564c97630")
    @Override
    public EditPartFactory getEditPartFactory() {
        return ModelioEditPartFactory.getInstance();
    }

    @objid ("5e31c605-55b7-11e2-877f-002564c97630")
    @Override
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        
        final GraphicalViewer viewer = getGraphicalViewer();
        
        MoveKeyHandler kh = new MoveKeyHandler(viewer);
        // Bind F2 key to direct edit
        kh.put(KeyStroke.getPressed(SWT.F2, 0), getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT));
        kh.setParent(viewer.getKeyHandler());
        viewer.setKeyHandler(kh);
    }

    @objid ("3cf7e238-041d-4bda-9e60-8324c5623742")
    @Override
    protected String getPopupId() {
        return POPUP_ID;
    }

}
