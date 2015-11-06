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
 * Remove stereotype.
 */
@objid ("89202659-ffc7-42e5-892c-070931441f75")
public class RemoveStereotypeHandler {
    @objid ("57432fb2-1415-460f-8703-6695475ddb6a")
    @Inject
    protected IProjectService projectService;

    /**
     * @param active MPart.
     * @return true if the handler can be executed.
     */
    @objid ("aeab4da4-a966-4dbc-b457-6839f0d34b6b")
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
     * Remove the currently selected stereotype(s).
     * @param active MPart.
     */
    @objid ("063b1a33-1c49-4899-be1b-436316250a30")
    @Execute
    public final void execute(@Named(IServiceConstants.ACTIVE_PART) final MPart part) {
        ICoreSession session = this.projectService.getSession();
        
        ModelPropertyPanelProvider propertyPanel = ((PropertyView) part.getObject()).getPanel();
        
        List<ModelElement> selectedElements = propertyPanel.getSelectedTypeItems();
        
        ModelElement parentElement = (ModelElement) propertyPanel.getInput();
        
        try (ITransaction transaction = session.getTransactionSupport().createTransaction("Remove stereotype")) {        
            parentElement.getExtension().removeAll(selectedElements);
        
            transaction.commit();
        } catch (Exception e) {
            // Should catch InvalidModelManipulationException to display a popup box, but it
            // is not a RuntimeException.
            reportException(e);
        }
    }

    @objid ("4b65fe58-a326-4889-8cbc-05c79e7df9df")
    static void reportException(Exception e) {
        // Show an error box
        String title = ModelProperty.I18N.getMessage("CannotRemoveStereotype");
        
        MessageDialog.openError(null, title, e.getLocalizedMessage());
        
        ModelProperty.LOG.error(e);
    }

}
