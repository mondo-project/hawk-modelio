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
                                    

package org.modelio.linkeditor.options;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.modelio.linkeditor.view.LinkEditorView;

@objid ("6dbee69a-1e72-4d42-b3cc-4255af820d9a")
public class LinkEditorPropertyChangeListener implements IPropertyChangeListener {
    @objid ("a56ec22a-1db6-4f0d-a2ad-d64bd0178ff8")
    private LinkEditorView view;

    @objid ("61241f71-e4e6-4182-b6ba-4d7c5f8d37e6")
    public LinkEditorPropertyChangeListener(LinkEditorView view) {
        this.view = view;
    }

    @objid ("ee23f207-738b-4119-a4da-636712557439")
    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if(!event.getNewValue().equals(event.getOldValue()))
            this.view.refreshFromCurrentSelection();
    }

}
