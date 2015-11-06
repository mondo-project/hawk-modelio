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

import javax.inject.Inject;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.property.stereotype.creator.StereotypeEditor;

@objid ("de04bf6f-2301-4a9c-8a0b-f058e689497e")
public class EditStereotypeHandler {
    @objid ("f49abc0b-2910-4c35-ad9d-a5d901848138")
    @Inject
    private IProjectService projectService;

    @objid ("c98e1e7a-8f53-48d5-a29e-a1e58540e97f")
    @Inject
    @Optional
    private IMModelServices mmServices;

    /**
     * (non-Javadoc)
     * @see org.eclipse.core.commands.AbstractHandler#isEnabled()
     */
    @objid ("1a358d1b-374e-482f-9616-c71bc6ff41cd")
    @CanExecute
    public final boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) final IStructuredSelection selection) {
        if (this.projectService.getSession() == null || this.mmServices==null || selection == null) {
            return false;
        }
        Stereotype selectedStereotype = getSelectedStereotype(selection);
        if (selectedStereotype == null || !selectedStereotype.getStatus().isModifiable()) {
            return false;
        }
        return true;
    }

    /**
     * (non-Javadoc)
     * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     */
    @objid ("d3f3a7f6-0b64-45e8-84a8-9b17fdb1dd27")
    @Execute
    public Object execute(@Named(IServiceConstants.ACTIVE_SELECTION) final IStructuredSelection selection) {
        StereotypeEditor stereotypeEditor = new StereotypeEditor(this.projectService, this.mmServices);
        stereotypeEditor.edit(getSelectedStereotype(selection));
        return null;
    }

    @objid ("521d943d-f35d-4960-b098-e3fa876c75dd")
    private Stereotype getSelectedStereotype(IStructuredSelection selection) {
        if (selection.size() == 1) {
            Object selectedObject = selection.getFirstElement();
            if (selectedObject instanceof Stereotype) {
                return (Stereotype) selectedObject;
            }
        }
        return null;
    }

}
