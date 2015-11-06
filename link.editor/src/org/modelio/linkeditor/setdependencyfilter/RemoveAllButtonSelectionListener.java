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
                                    

package org.modelio.linkeditor.setdependencyfilter;

import java.util.HashSet;
import java.util.Set;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.modelio.metamodel.mda.ModuleComponent;

@objid ("1b68ca30-5e33-11e2-b81d-002564c97630")
final class RemoveAllButtonSelectionListener extends SelectionAdapter {
    @objid ("1b68ca31-5e33-11e2-b81d-002564c97630")
    private final DialogView dialogView;

    @objid ("1b68ca33-5e33-11e2-b81d-002564c97630")
    public RemoveAllButtonSelectionListener(final DialogView dialogView) {
        super();
        this.dialogView = dialogView;
    }

    @objid ("1b68ca37-5e33-11e2-b81d-002564c97630")
    @Override
    public void widgetSelected(final SelectionEvent e) {
        Set<ModuleComponent> allMdacs = new HashSet<>(this.dialogView.getModel().getFilterStereotypes().keySet());
        for (ModuleComponent module : allMdacs) {
            this.dialogView.getModel().removeFromFilter(module);
        }
    }

}
