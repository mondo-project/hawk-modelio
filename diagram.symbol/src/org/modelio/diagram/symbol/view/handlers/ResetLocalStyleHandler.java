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

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.modelio.diagram.elements.core.model.IGmObject;
import org.modelio.diagram.symbol.view.StyleEditor;
import org.modelio.diagram.symbol.view.SymbolView;

/**
 * Handler that reset the style to default values.
 */
@objid ("ac4c95da-55b7-11e2-877f-002564c97630")
public class ResetLocalStyleHandler {
    /**
     * Execute the handler.
     * @param part the active part
     */
    @objid ("ac4c95dc-55b7-11e2-877f-002564c97630")
    @Execute
    public void execute(EPartService partService) {
        MPart part = getSymbolViewPart(partService);
        if (part != null) {
            SymbolView symbolView = (SymbolView) part.getObject();
        
            IGmObject gm = symbolView.getSelectedSymbol();
            if (gm != null) {
                new StyleEditor(gm).reset();
            }
        }
    }

    /**
     * Determine if this object's Execute method can be called.
     * @param part the active part.
     * @return <code>true</code> if the command can be executed, else <code>false</code>.
     */
    @objid ("ac4c95e2-55b7-11e2-877f-002564c97630")
    @CanExecute
    public boolean canExecute(EPartService partService) {
        MPart part = getSymbolViewPart(partService);
        if (part != null) {
            SymbolView symbolView = (SymbolView) part.getObject();
        
            IGmObject gm = symbolView.getSelectedSymbol();
            if (gm != null) {
                // The diagram must be editable to enable this command 
                return gm.isEditable();
            }
        }
        return false;
    }

    @objid ("a23baea7-170d-4da1-aa45-44f331daae35")
    private MPart getSymbolViewPart(EPartService partService) {
        MPart part = partService.findPart(SymbolView.VIEW_ID);
        if (part != null) {
            if (partService.isPartVisible(part)) {
                return part;
            }
        }
        return null;
    }

}
