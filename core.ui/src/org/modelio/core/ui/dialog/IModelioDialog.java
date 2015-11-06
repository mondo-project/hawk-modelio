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
                                    

package org.modelio.core.ui.dialog;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

@objid ("0029a928-4a05-1fe0-bf4c-001ec947cd2a")
public interface IModelioDialog {
    @objid ("0017edf0-4a07-1fe0-bf4c-001ec947cd2a")
    Control createContentArea(Composite parent);

    @objid ("001819ba-4a07-1fe0-bf4c-001ec947cd2a")
    void addButtonsInButtonBar(Composite parent);

    @objid ("00182f54-4a07-1fe0-bf4c-001ec947cd2a")
    void init();

}
