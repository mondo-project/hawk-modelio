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
                                    

package org.modelio.diagram.editor.composite.editor;

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
@objid ("b39b9dd9-7221-4040-bcd1-3c6844de956a")
public class CompositeDiagramEditor extends AbstractDiagramEditor {
    /**
     * public ID of this editor.
     */
    @objid ("05ad19b8-ab82-41f3-abea-11659d59ef6d")
    public static final String ID = "org.modelio.diagram.editor.composite.CompositeDiagramEditorID";

    @objid ("8f917e1d-bcee-4596-9ce0-c9c8a0369541")
    private static final String POPUP_ID = "org.modelio.diagram.editor.composite.menu.popupmenu";

    /**
     * C'tor.
     */
    @objid ("817b186f-37f3-4044-b9fc-7cc4ce5b80db")
    public CompositeDiagramEditor() {
        super();
    }

    @objid ("fd23324c-3cf0-4205-9177-d2c61b9684b2")
    @Override
    public EditPartFactory getEditPartFactory() {
        // return an EditPart factory, here we can return the Modelio UML
        // standard EditPartFactory
        return ModelioEditPartFactory.getInstance();
    }

    @objid ("c947857b-563c-4275-9f12-7f715b9f1ae0")
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

    @objid ("cb899238-65e0-402f-a21a-bc92888ffd55")
    @Override
    protected String getPopupId() {
        return POPUP_ID;
    }

}
