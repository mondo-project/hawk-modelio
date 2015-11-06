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

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.modelio.model.browser.views.BrowserView;
import org.modelio.model.browser.views.SelectionHistory;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Implements the 'next selection' command in the model explorer.
 */
@objid ("396fa02b-4603-11e2-960d-002564c97630")
public class ForwardHandler {
    @objid ("47b1872c-4603-11e2-960d-002564c97630")
    @Execute
    public static void execute(final MPart part) {
        assert (part.getObject() instanceof BrowserView) : "Handler used on a part other than BrowserView!";
        BrowserView view = (BrowserView) part.getObject();
        
        SelectionHistory.getInstance().selectNextSelection(view);
    }

    @objid ("47b1ae3d-4603-11e2-960d-002564c97630")
    @CanExecute
    public static final boolean canExecute() {
        return SelectionHistory.getInstance().hasNext();
    }

    @objid ("47b1d54c-4603-11e2-960d-002564c97630")
    @Optional
    @Inject
    public static void update(@Named(IServiceConstants.ACTIVE_SELECTION) final IStructuredSelection selection) {
        // Retrieve the selected elements
        List<MObject> selectedElements = new ArrayList<>();
        if (selection != null) {
            for (Object selectedElement : selection.toList()) {
                if (selectedElement instanceof MObject) {
                    selectedElements.add((MObject) selectedElement);
                }
            }
        }
        
        SelectionHistory.getInstance().update(selectedElements);
    }

}
