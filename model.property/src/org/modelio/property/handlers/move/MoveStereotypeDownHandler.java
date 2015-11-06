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
                                    

package org.modelio.property.handlers.move;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.property.PropertyView;
import org.modelio.property.plugin.ModelProperty;
import org.modelio.property.ui.ModelPropertyPanelProvider;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.api.transactions.ITransaction;

/**
 * Move stereotype down handler.
 */
@objid ("f6d868f3-0745-4b12-b550-a73ad4aae3ad")
public class MoveStereotypeDownHandler {
    @objid ("7ba942e4-ea28-4c16-9124-19a473c5692f")
    @Inject
    protected IProjectService projectService;

    /**
     * @param active MPart.
     * @return true if the handler can be executed.
     */
    @objid ("ca1fa72c-ffc1-4c35-904c-75e11a0fd439")
    @CanExecute
    public final boolean canExecute(@Named(IServiceConstants.ACTIVE_PART) final MPart part) {
        if (!(part.getObject() instanceof PropertyView)) {
            return false;
        }
        
        // Sanity checks
        if (this.projectService.getSession() == null) {
            return false;
        }    
        
        ModelPropertyPanelProvider propertyPanel = ((PropertyView) part.getObject()).getPanel();
        if (propertyPanel == null) {
            return false;
        }
        Element parentElement = propertyPanel.getInput();
        if (parentElement == null || !parentElement.isModifiable()) {
            return false;
        }
        
        List<ModelElement> selectedElements = propertyPanel.getSelectedTypeItems();
        if (selectedElements.isEmpty()) return false;
        for (ModelElement element : selectedElements) {            
            if (!(element instanceof Stereotype)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Move the selected stereotype(s) down.
     * @param active MPart.
     */
    @objid ("1c9b6afb-823a-45a9-9270-5267b8faf76f")
    @Execute
    public final void execute(@Named(IServiceConstants.ACTIVE_PART) final MPart part) {
        ICoreSession session = this.projectService.getSession();
        
        ModelPropertyPanelProvider propertyPanel = ((PropertyView) part.getObject()).getPanel();
        
        List<ModelElement> selectedElements = propertyPanel.getSelectedTypeItems();
        
        ModelElement parentElement = (ModelElement) propertyPanel.getInput();
        
        try (ITransaction transaction = session.getTransactionSupport().createTransaction("Move stereotype down")) {
            int nbToMove = 0;
        
            List<Stereotype> listToReorder = parentElement.getExtension();
            // We first move down the Last selected element of the list; This way the positions of other
            // selected elements are not affected by the move of the current element.
            for (int i = selectedElements.size() - 1; i >= 0; --i) {
                Stereotype element = (Stereotype) selectedElements.get(i);
            
                // Retrieve the new index of the element
                int index = computeNewIndex(element, listToReorder);
        
                if (index == -1) {
                    // Invalid case, just exit
                    transaction.rollback();
                    return;
                }
        
                // Move the element in the list 
                nbToMove++;
                listToReorder.remove(element);
                listToReorder.add(index, element);
        
            }
        
            if (nbToMove > 0) {
                transaction.commit();
            } else {
                transaction.rollback();
            }
        } catch (Exception e) {
            // Should catch InvalidModelManipulationException to display a popup box, but it
            // is not a RuntimeException.
            reportException(e);
        }
    }

    @objid ("dd17caa9-91ef-4b3d-bdf8-c18d3e75a13a")
    private static int computeNewIndex(Stereotype element, List<Stereotype> listToReorder) {
        int index = listToReorder.indexOf(element) + 1;
        
        // Iterate until we find an element of the same metaclass or until we find the end of the list.
        while (index < listToReorder.size() && listToReorder.get(index).getClass() != element.getClass()) {
            index++;
        }
        
        // If that would move outside of the list, that means element is already the last one.
        if (index >= listToReorder.size()) {
            return -1;
        }
        return index;
    }

    @objid ("ca94544a-2b7c-42c8-9b66-132202d431f1")
    static void reportException(Exception e) {
        // Show an error box
        String title = ModelProperty.I18N.getMessage("CannotMoveStereotypeDown");
        
        MessageDialog.openError(null, title, e.getLocalizedMessage());
        
        ModelProperty.LOG.error(e);
    }

}
