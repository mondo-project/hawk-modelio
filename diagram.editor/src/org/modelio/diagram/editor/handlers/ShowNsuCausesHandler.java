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
                                    

package org.modelio.diagram.editor.handlers;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.modelio.app.core.navigate.IModelioNavigationService;
import org.modelio.core.ui.nsu.CauseAnalyser;
import org.modelio.diagram.elements.core.link.GmLinkEditPart;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.metamodel.uml.statik.NamespaceUse;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Handler for the "select in UML explorer" command.
 * 
 * @author phv
 */
@objid ("bc6e38b4-a215-4762-b168-6359d3135fdc")
public class ShowNsuCausesHandler {
    @objid ("1ab9186c-dff9-4cdf-898c-7d014de49232")
    @Execute
    public void execute(@Named(IServiceConstants.ACTIVE_SELECTION) IStructuredSelection iSelection, IModelioNavigationService navigationService) {
        for (MObject element : getSelection(iSelection)) {
            if (element instanceof NamespaceUse) {
               CauseAnalyser.showCauses((NamespaceUse)element, navigationService);
            }
        }
    }

    @objid ("42f5c05d-68e1-4335-ad1a-5f8d00b7a799")
    @CanExecute
    public boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) IStructuredSelection iSelection) {
        List<MObject> listSelection = getSelection(iSelection);
        return listSelection.size() == 1 && (listSelection.get(0) instanceof NamespaceUse);
    }

    @objid ("6b339d01-b4d8-4f20-b786-1c46706ab5ec")
    public List<MObject> getSelection(IStructuredSelection iSelection) {
        List<MObject> selectedElements = new ArrayList<>();
        for (Object selectedObject : iSelection.toList()) {
            if (selectedObject instanceof GmLinkEditPart) {
                GmLinkEditPart editPart = (GmLinkEditPart) selectedObject;
                final MObject elt = ((GmModel) editPart.getModel()).getRelatedElement();
                selectedElements.add(elt);
            }
        }
        return selectedElements;
    }

}
