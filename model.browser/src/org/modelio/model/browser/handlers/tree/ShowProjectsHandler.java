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
                                    

package org.modelio.model.browser.handlers.tree;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MDirectMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.modelio.model.browser.views.BrowserView;

@objid ("40e11f08-4540-11e2-aeb7-002564c97630")
public class ShowProjectsHandler {
    @objid ("610f8864-4540-11e2-aeb7-002564c97630")
    @Execute
    public static final void execute(MPart part, EModelService s) {
        assert (part.getObject() instanceof BrowserView) : "Handler used on a part other than BrowserView!";
        for (MMenu menu : part.getMenus()) {
            if ("org.modelio.model.browser.viewmenu".equals(menu.getElementId())) {
                MDirectMenuItem button = (MDirectMenuItem) s.find("org.modelio.model.browser.directmenuitem.projects", menu);
                if (button != null) {
                    BrowserView view = (BrowserView) part.getObject();
                    view.setShowProjects(button.isSelected());
                }
            }
        }
    }

    @objid ("9c9dfcfa-8667-4e14-8333-de1d14265b1a")
    @CanExecute
    public final boolean canExecute(MPart part) {
        if (!(part.getObject() instanceof BrowserView)) {
            return false;
        }
        return ((BrowserView) part.getObject()).getRoots().isEmpty();
    }

}
