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
                                    

package org.modelio.model.browser.handlers;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.model.browser.views.BrowserView;
import org.modelio.vcore.session.api.transactions.ITransaction;

/**
 * Basic "rename element" handler.
 * Available only when the selected element is modifiable.
 */
@objid ("37bb2ea4-1de3-11e2-bcbe-002564c97630")
public class RenameElementHandler {
    @objid ("3ba08ec5-1de3-11e2-bcbe-002564c97630")
    @Inject
    protected IProjectService projectService;

    /**
     * Available only when the selected elements are modifiable.
     * @param selection the current modelio selection.
     * @return true if the handler can be executed.
     */
    @objid ("3ba08ec7-1de3-11e2-bcbe-002564c97630")
    @CanExecute
    public final boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) final Object selection) {
        // Sanity checks
        if (this.projectService.getSession() == null) {
            return false;
        }
        
        // Must select an element only
        List<Element> selectedElements = getSelectedElements(selection);
        if (selectedElements.size() != 1) {
            return false;
        }
        
        for (Element element : selectedElements) {
            if (!element.getStatus().isModifiable()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Rename the currently selected elements.
     * @param selection the current modelio selection.
     */
    @objid ("3ba2f028-1de3-11e2-bcbe-002564c97630")
    @Execute
    public final void execute(@Named(IServiceConstants.ACTIVE_SELECTION) final Object selection, @Named(IServiceConstants.ACTIVE_PART) final MPart part) {
        if (! (part.getObject() instanceof BrowserView)) {
            return;
        }
        
        BrowserView browserView = (BrowserView) part.getObject();
        
        // Sanity checks
        if (this.projectService.getSession() == null) {
            return;
        }
        
        // Must have at least an element
        List<Element> selectedElements = getSelectedElements(selection);
        if (selectedElements.isEmpty()) {
            return;
        }
        
        try (ITransaction t = this.projectService.getSession().getTransactionSupport().createTransaction("Rename element")) {
            Element editedElement = selectedElements.get(0);
            
            browserView.edit(editedElement);
            
            t.commit();
        }
    }

    @objid ("3ba2f032-1de3-11e2-bcbe-002564c97630")
    private static List<Element> getSelectedElements(final Object selection) {
        List<Element> selectedElements = new ArrayList<>();
        if (selection instanceof Element) {
            selectedElements.add((Element) selection);
        } else if (selection instanceof IStructuredSelection && ((IStructuredSelection) selection).size() >= 1) {
            Object[] elements = ((IStructuredSelection) selection).toArray();
            for (Object element : elements) {
                if (element instanceof Element) {
                    selectedElements.add((Element) element);
                } else if (element instanceof IAdaptable) {
                    final Element adapter = (Element) ((IAdaptable) element).getAdapter(Element.class);
                    if (adapter != null) {
                        selectedElements.add(adapter);
                    }
                }
            }
        }
        return selectedElements;
    }

}
