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
                                    

package org.modelio.diagram.editor.statik.editor;

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
@objid ("33d1feba-55b7-11e2-877f-002564c97630")
public class StaticDiagramEditor extends AbstractDiagramEditor {
    /**
     * Id of the static diagram editor for plugins.xml .
     */
    @objid ("33d1febe-55b7-11e2-877f-002564c97630")
    public static String ID = "org.modelio.diagram.editor.statik.StatikDiagramEditorID";

    @objid ("4b4af4cf-2cad-4b9f-a31b-7cc61f9fac09")
    private static final String POPUP_ID = "org.modelio.diagram.editor.statik.menu.popupmenu";

    @objid ("33d1fec3-55b7-11e2-877f-002564c97630")
    @Override
    public EditPartFactory getEditPartFactory() {
        // return an EditPart factory, here we can return the Modelio UML standard EditPartFactory
        return ModelioEditPartFactory.getInstance();
    }

    @objid ("33d1fecf-55b7-11e2-877f-002564c97630")
    @Override
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        
        final GraphicalViewer viewer = this.getGraphicalViewer();
        
        MoveKeyHandler kh = new MoveKeyHandler(viewer);
        // Bind F2 key to direct edit
        kh.put(KeyStroke.getPressed(SWT.F2, 0), this.getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT));
        kh.setParent(viewer.getKeyHandler());
        viewer.setKeyHandler(kh);
    }

    @objid ("5dc2d7b5-48d3-43ec-8062-cfdc2fd57a8a")
    @Override
    protected String getPopupId() {
        return POPUP_ID;
    }

}
