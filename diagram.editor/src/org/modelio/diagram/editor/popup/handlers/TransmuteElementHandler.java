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

import javax.inject.Inject;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.diagram.editor.transmuter.ITransmuter;
import org.modelio.diagram.editor.transmuter.TransmuterRegistry;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.plugin.DiagramElements;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.api.transactions.ITransaction;
import org.modelio.vcore.session.api.transactions.ITransactionSupport;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * This specific handler creates an element of the requested metaclass and attaches it to its composition owner using
 * the requested dependency. If provided, the stereotype is applied to the created element.
 */
@objid ("668fd303-33f7-11e2-95fe-001ec947c8cc")
public class TransmuteElementHandler {
    @objid ("1542442e-5e26-11e2-a8be-00137282c51b")
    @Named(IServiceConstants.ACTIVE_SELECTION)
    @Inject
    private ISelection selection;

    @objid ("668fd305-33f7-11e2-95fe-001ec947c8cc")
    @CanExecute
    public boolean canExecute() {
        boolean singleSelection = false;
        if (this.selection instanceof IStructuredSelection) {
            singleSelection = ((IStructuredSelection) this.selection).size() == 1;
        }
        MObject selectedElement = getSelectedElement();
        return singleSelection && selectedElement != null && selectedElement.isModifiable();
    }

    @objid ("668fd30a-33f7-11e2-95fe-001ec947c8cc")
    @Execute
    public Object execute(IProjectService projectService, @Named("metaclass") String metaclass, IMModelServices modelServices) {
        // Get the value of the parameters
        //String metaclass = event.getParameter("metaclass");
        MObject element = getSelectedElement();
        
        // Get the modelling session and open a transaction
        ICoreSession modelingSession = projectService.getSession();
        
        ITransactionSupport transactionManager = modelingSession.getTransactionSupport();
               
        if (TransmuterRegistry.getInstance().canTransmute(element.getMClass().getName(), metaclass)) {
            try (ITransaction transaction = transactionManager.createTransaction("Transmute to " + metaclass)) {
                ITransmuter transmuter = TransmuterRegistry.getInstance().getTransmuter(element, metaclass);
                MObject result = transmuter.transmute(modelServices);
        
                GmModel model = (GmModel) getSelectedEditPart().getModel();
                model.getDiagram().unmask((GmCompositeNode) model.getParent(), result, model.getLayoutData());
        
                // Commit the transaction.
                transaction.commit();
            } catch (Exception e) {
                DiagramElements.LOG.debug(e);
            }
        
            try (ITransaction transaction = transactionManager.createTransaction("Delete " + metaclass)) {
                element.delete();
                transaction.commit();
            } catch (Exception e) {
                DiagramElements.LOG.debug(e);
            }
        }
        return null;
    }

    @objid ("668fd311-33f7-11e2-95fe-001ec947c8cc")
    protected MObject getSelectedElement() {
        if (this.selection instanceof IStructuredSelection) {
            if (((IStructuredSelection) this.selection).getFirstElement() instanceof IAdaptable) {
                IAdaptable adapter = (IAdaptable) ((IStructuredSelection) this.selection).getFirstElement();
                return (MObject) adapter.getAdapter(MObject.class);
            }
        }
        return null;
    }

    @objid ("668fd315-33f7-11e2-95fe-001ec947c8cc")
    protected EditPart getSelectedEditPart() {
        if (this.selection instanceof IStructuredSelection) {
            if (((IStructuredSelection) this.selection).getFirstElement() instanceof IAdaptable) {
                IAdaptable adapter = (IAdaptable) ((IStructuredSelection) this.selection).getFirstElement();
                return (EditPart) adapter.getAdapter(EditPart.class);
            }
        }
        return null;
    }

}
