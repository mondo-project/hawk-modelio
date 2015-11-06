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
import org.modelio.model.browser.views.BrowserView;
import org.modelio.model.browser.views.SelectionHistory;

/**
 * Implements the 'previous selection' command in the model explorer.
 */
@objid ("2c1f451b-4603-11e2-960d-002564c97630")
public class BackwardHandler {
    @objid ("47af8b5b-4603-11e2-960d-002564c97630")
    @Execute
    public static void execute(final MPart part) {
        assert (part.getObject() instanceof BrowserView) : "Handler used on a part other than BrowserView!";
        BrowserView view = (BrowserView) part.getObject();
        
        SelectionHistory.getInstance().selectPreviousSelection(view);
    }

    @objid ("47b0eaeb-4603-11e2-960d-002564c97630")
    @CanExecute
    public static final boolean canExecute() {
        return SelectionHistory.getInstance().hasPrevious();
    }

}
