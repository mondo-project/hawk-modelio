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
                                    

package org.modelio.diagram.symbol.view.handlers;

import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.modelio.diagram.symbol.view.SymbolView;

/**
 * Display the help.
 */
@objid ("ac4c95e9-55b7-11e2-877f-002564c97630")
public class SymbolHelpHandler {
    /**
     * Execute the command
     * @param part the current part, containing the symbol view
     */
    @objid ("ac4c95ea-55b7-11e2-877f-002564c97630")
    @Execute
    public void execute(@Named(IServiceConstants.ACTIVE_PART) MPart part) {
        if (part.getObject() instanceof SymbolView) {
            SymbolView symbolView = (SymbolView) part.getObject();
            if (symbolView != null) {
                symbolView.getPanel().toggleHelpPanel();
            }
        }
    }

}
