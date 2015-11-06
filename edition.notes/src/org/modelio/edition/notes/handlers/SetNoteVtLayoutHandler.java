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
                                    

package org.modelio.edition.notes.handlers;

import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.modelio.edition.notes.view.NotesView;

/**
 * Handler for the setVerticalLayout command, using injection to execute on the {@link NotesView} MPart.
 * Changes the property view layout to vertical.
 * @see SetNoteHzLayoutHandler
 * @see SetNoteAutoLayoutHandler
 */
@objid ("26dec2f1-186f-11e2-bc4e-002564c97630")
public class SetNoteVtLayoutHandler {
    @objid ("26dec2f3-186f-11e2-bc4e-002564c97630")
    @Execute
    public void execute(@Named(IServiceConstants.ACTIVE_PART) final MPart e) {
        if (e.getObject() instanceof NotesView) {
            NotesView view = (NotesView) e.getObject();
        
            view.getNotesPanel().disableAutoLayout();
            view.getNotesPanel().setVerticalLayout();
        }
    }

}
