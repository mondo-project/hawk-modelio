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
                                    

package org.modelio.edition.notes.view.data;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.widgets.Control;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.session.api.ICoreSession;

@objid ("26ef6c37-186f-11e2-bc4e-002564c97630")
public interface INoteContent {
    @objid ("26ef6c38-186f-11e2-bc4e-002564c97630")
    void setInput(final ModelElement note, final ModelElement annotedElement);

    @objid ("26ef6c3d-186f-11e2-bc4e-002564c97630")
    void stop();

    @objid ("26ef6c3e-186f-11e2-bc4e-002564c97630")
    void start(ICoreSession modelingSession);

    @objid ("26ef6c40-186f-11e2-bc4e-002564c97630")
    Control getControl();

    @objid ("26ef6c42-186f-11e2-bc4e-002564c97630")
    ModelElement getNoteElement();

    @objid ("26ef6c44-186f-11e2-bc4e-002564c97630")
    ModelElement getAnnotedElement();

}
