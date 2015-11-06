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
                                    

package org.modelio.linkeditor.setorientation;

import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.modelio.linkeditor.view.LinkEditorView;

/**
 * Toggles the orientation of layout.
 * 
 * @author fpoyer
 */
@objid ("1b6d8ce1-5e33-11e2-b81d-002564c97630")
public class SetOrientationHandler {
    @objid ("1b6d8ce3-5e33-11e2-b81d-002564c97630")
    @Execute
    public Object execute(@Named("LayoutOrientation") String layoutOrientation, MPart part) {
        if (part.getObject() instanceof LinkEditorView) {
            final LinkEditorView linkEditorView = (LinkEditorView) part.getObject();
            
            /*LinkEditorOptions options = LinkEditor.getOptions();
            options.setOrientation
            if (HandlerUtil.matchesRadioState(event)) {
                // we are already in the updated state - do nothing
                return null;
            }
            // update the state of the command to the new value.
            String newStateValue = event.getParameter(RadioState.PARAMETER_ID);
            HandlerUtil.updateRadioState(event.getCommand(), newStateValue);
            // Sent notification to whoever might be interested.
            LinkEditor.getOptions().changeLayoutOrientation();*/
            linkEditorView.getOptions().changeLayoutOrientation(layoutOrientation);
            linkEditorView.refreshFromCurrentSelection();
        }
        return null;
    }

}
