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
                                    

package org.modelio.diagram.editor.state.editor;

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
@objid ("f4eb6557-55b6-11e2-877f-002564c97630")
public class StateDiagramEditor extends AbstractDiagramEditor {
    /**
     * Unique ID of all editors of State Diagrams.
     */
    @objid ("f4eb655b-55b6-11e2-877f-002564c97630")
    public static String ID = "org.modelio.diagram.editor.state.StateDiagramEditorID";

    @objid ("0b5e2a29-87e8-4da0-b2e4-8c38151dd116")
    private static final String POPUP_ID = "org.modelio.diagram.editor.state.menu.popupmenu";

    @objid ("f4ecebbc-55b6-11e2-877f-002564c97630")
    @Override
    public EditPartFactory getEditPartFactory() {
        // return an EditPart factory, here we can return the Modelio UML
        // standard EditPartFactory
        return ModelioEditPartFactory.getInstance();
    }

    /**
     * Overridden to add the "F2=>DIRECT_EDIT" functionality.
     */
    @objid ("f4ecebc1-55b6-11e2-877f-002564c97630")
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

    @objid ("cadd9659-5594-4e7c-aef7-08c7b88c371d")
    @Override
    protected String getPopupId() {
        return POPUP_ID;
    }

}
