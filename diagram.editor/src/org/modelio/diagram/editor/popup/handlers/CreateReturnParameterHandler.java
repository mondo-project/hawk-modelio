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
                                    

package org.modelio.diagram.editor.popup.handlers;

import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.jface.viewers.ISelection;
import org.modelio.metamodel.uml.statik.Operation;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * This specific handler creates a Return Parameter, giving it the default type defined in the project preferences.
 * 
 * @author fpoyer
 */
@objid ("668fd2e7-33f7-11e2-95fe-001ec947c8cc")
public class CreateReturnParameterHandler extends AbstractDiagramCreateHandler {
    @objid ("fecb9c36-5e25-11e2-a8be-00137282c51b")
    @Override
    @CanExecute
    public boolean canExecute(@Named("org.eclipse.ui.selection") ISelection selection) {
        if (super.canExecute(selection)) {
            // Now lets have a look a some specific things:
            MObject selectedElement = getSelectedElement();
        
            if (selectedElement instanceof Operation) {
                Operation op = (Operation) selectedElement;
                return op.getReturn() == null;
            }
        }
        return false;
    }

}
