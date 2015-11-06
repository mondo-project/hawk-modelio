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
                                    

package org.modelio.property.handlers;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MDirectMenuItem;
import org.modelio.property.PropertyView;

@objid ("78c07460-48dd-4c9b-8233-dbe1df478b38")
public class ShowHiddenAnnotationsHandler {
    @objid ("dc46d40e-dec3-4cd2-b709-f485732b0880")
    @Execute
    public static final void execute(MPart part, MDirectMenuItem button) {
        assert (part.getObject() instanceof PropertyView) : "Handler used on a part other than PropertyView!";
        //System.out.println("injected menu item="+button);
        PropertyView view = (PropertyView) part.getObject();
        view.setShowHiddenMdaElements(button.isSelected());
    }

    @objid ("3b8c7f15-83fc-420c-aec8-92dc0bf9a8af")
    @CanExecute
    public final boolean canExecute(MPart part) {
        if (!(part.getObject() instanceof PropertyView)) {
            return false;
        }
        return ((PropertyView) part.getObject()) != null;
    }

}
