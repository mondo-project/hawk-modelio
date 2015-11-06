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
                                    

package org.modelio.linkeditor.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("1bb4f63b-5e33-11e2-b81d-002564c97630")
class OptionsChangeListener implements PropertyChangeListener {
    @objid ("1bb4f63c-5e33-11e2-b81d-002564c97630")
    private LinkEditorView view;

    @objid ("1bb4f63d-5e33-11e2-b81d-002564c97630")
    public OptionsChangeListener(final LinkEditorView view) {
        this.view = view;
    }

    @objid ("1bb4f641-5e33-11e2-b81d-002564c97630")
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        // Handle property change from options by refreshing.
        if (evt.getSource() == this.view.getOptions()) {
            this.view.refreshFromCurrentSelection();
        }
    }

}
