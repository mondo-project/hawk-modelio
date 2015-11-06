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
 * Move stereotype up handler.
 */
@objid ("80c7ab7c-1d76-42bb-b105-241b875be80a")
public class MoveStereotypeUpHandler {
    @objid ("3b1e4854-3125-432e-b171-c2870bdc7d62")
    @Inject
    protected IProjectService projectService;

    /**
     * @param active MPart.
     * @return true if the handler can be executed.
     */
    @objid ("98ec54d1-d469-4a48-97a7-23261f1a0bb0")
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
     * Move the selected stereotype(s) up.
     * @param active MPart.
     */
    @objid ("8f1e5ccf-8490-47bf-9bf3-a2957a2af962")
    @Execute
    public final void execute(@Named(IServiceConstants.ACTIVE_PART) final MPart part) {
        ICoreSession session = this.projectService.getSession();
        
        ModelPropertyPanelProvider propertyPanel = ((PropertyView) part.getObject()).getPanel();
        
        List<ModelElement> selectedElements = propertyPanel.getSelectedTypeItems();
        
        ModelElement parentElement = (ModelElement) propertyPanel.getInput();
        
        try (ITransaction transaction = session.getTransactionSupport().createTransaction("Move stereotype up")) {
            int nbToMove = 0;
            
            List<Stereotype> listToReorder = parentElement.getExtension();
        
            for (ModelElement element : selectedElements) {
        
                int index = getIndexUp((Stereotype)element, listToReorder);
        
                if (index != -1) {
                    nbToMove++;
                    listToReorder.remove(element);
                    listToReorder.add(index, (Stereotype)element);
                } else {
                    break;
                }
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

    @objid ("b3e29821-13ba-4925-9d28-7a36b161b023")
    private static int getIndexUp(Stereotype element, List<Stereotype> listToReorder) {
        int index = listToReorder.indexOf(element);
        
        if (index < 1) {
            return -1;
        }
        
        index--;
        
        // Iterate until we find an element of the same metaclass or until we find the begining of the list.
        while (index != -1 && listToReorder.get(index).getClass() != element.getClass()) {
            index--;
        }
        return index;
    }

    @objid ("66ab53e0-b506-4163-8af8-cbc000cdbc0e")
    static void reportException(Exception e) {
        // Show an error box
        String title = ModelProperty.I18N.getMessage("CannotMoveStereotypeUp");
        
        MessageDialog.openError(null, title, e.getLocalizedMessage());
        
        ModelProperty.LOG.error(e);
    }

}
