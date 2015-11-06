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

import javax.inject.Inject;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.factory.ElementNotUniqueException;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.model.browser.plugin.ModelBrowser;
import org.modelio.model.browser.views.BrowserView;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.api.transactions.ITransaction;
import org.modelio.vcore.smkernel.mapi.MClass;
import org.modelio.vcore.smkernel.mapi.MDependency;
import org.modelio.vcore.smkernel.meta.SmClass;

@objid ("003688c8-8902-1006-9c1d-001ec947cd2a")
public abstract class AbstractCreateElementHandler {
    @objid ("9ec3b983-ccde-11e1-97e5-001ec947c8cc")
    @Inject
    protected IProjectService projectService;

    @objid ("00653af6-9025-1006-9c1d-001ec947cd2a")
    @Inject
    @Optional
    protected IMModelServices mmServices;

    @objid ("9ec61c0b-ccde-11e1-97e5-001ec947c8cc")
    @CanExecute
    public boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) final Object selection, @Named("metaclass") final String metaclassName, @Named("dependency") final String dependencyName, @Optional
@Named("stereotype") final String stereotypeName) {
        // Sanity checks
        if (this.projectService.getSession() == null) {
            return false;
        }
        
        // Find metaclass
        MClass metaclass = getMetaclass(metaclassName);
        if (metaclass == null) {
            return false;
        }
        
        Element selectedOwner = getSelectedElement(selection, metaclass);
        if (selectedOwner == null) {
            return false;
        }
        
        // Find dependency
        MDependency dependency = getDependency(dependencyName, selectedOwner);
        if (dependency == null) {
            return false;
        }
        
        // Find stereotype
        Stereotype stereotype = null;
        if (stereotypeName!= null && !stereotypeName.isEmpty()) {            
            try {
                stereotype = this.mmServices.getStereotype("ModelerModule", stereotypeName, metaclass);
            } catch (ElementNotUniqueException e) {
                ModelBrowser.LOG.error("AbstractCreateElementHandler#execute : \nCreation failed: could not create an instance of " + metaclassName + " in dependency " + dependencyName + " under parent : \n" + selectedOwner);
                // Stereotype missing... deactivate the command
                return false;
            }
        }
        return doCanExecute(selectedOwner, metaclass, dependency, stereotype);
    }

    @objid ("0065e154-9025-1006-9c1d-001ec947cd2a")
    @Execute
    public final void execute(final MPart part, @Named(IServiceConstants.ACTIVE_SELECTION) final Object selection, @Named("metaclass") final String metaclassName, @Named("dependency") final String dependencyName, @Optional
@Named("stereotype") final String stereotypeName) {
        // Sanity checks
        if (this.projectService.getSession() == null) {
            return;
        }
        
        // Find metaclass
        MClass metaclass = getMetaclass(metaclassName);
        if (metaclass == null) {
            return;
        }
        
        Element selectedOwner = getSelectedElement(selection, metaclass);
        if (selectedOwner == null) {
            return;
        }
        
        // Find dependency
        MDependency dependency = getDependency(dependencyName, selectedOwner);
        if (dependency == null) {
            return;
        }
        
        // Find stereotype
        Stereotype stereotype = null;
        
        final ICoreSession session = this.projectService.getSession();
        if (stereotypeName!= null && !stereotypeName.isEmpty()) {            
            try {
                stereotype = this.mmServices.getStereotype("ModelerModule", stereotypeName, metaclass);
            } catch (ElementNotUniqueException e) {
                ModelBrowser.LOG.error("AbstractCreateElementHandler#execute : \nCreation failed: could not create an instance of "
                        + metaclassName + " in dependency " + dependencyName + " under parent : \n" + selectedOwner);
                return;
            }
        }
        try (ITransaction t = session.getTransactionSupport().createTransaction("create " + metaclassName)) {
        
            Element newElement = doCreate(selectedOwner, metaclass, dependency, stereotype);
        
            if (newElement != null) {
                postCreationStep(newElement);
                t.commit();
                postCommit(part, newElement);
                selectAndEditInBrowser(part, newElement);
            } else {
                ModelBrowser.LOG.error("AbstractCreateElementHandler#execute : \nCreation failed: could not create an instance of "
                        + metaclassName + " in dependency " + dependencyName + " under parent : \n" + selectedOwner);
            }
        } catch (Exception e) {
            ModelBrowser.LOG.error("AbstractCreateElementHandler#execute : \nCreation failed: could not create an instance of " + metaclassName + " in dependency " + dependencyName + " under parent : \n" + selectedOwner);
        }
    }

    @objid ("00667542-9025-1006-9c1d-001ec947cd2a")
    protected abstract boolean doCanExecute(Element owner, MClass metaclass, MDependency dependency, Stereotype stereotype);

    @objid ("006698f6-9025-1006-9c1d-001ec947cd2a")
    protected abstract Element doCreate(Element owner, MClass metaclass, MDependency dependency, Stereotype stereotype);

    @objid ("0066e702-9025-1006-9c1d-001ec947cd2a")
    protected MDependency getDependency(final String dependencyName, Element selectedOwner) {
        MDependency dependency = selectedOwner.getMClass().getDependency(dependencyName);
        assert (dependency != null) : "Unknown dependency " + dependencyName + " on " + selectedOwner.getClass().getName();
        return dependency;
    }

    @objid ("006716aa-9025-1006-9c1d-001ec947cd2a")
    protected MClass getMetaclass(final String metaclassName) {
        MClass metaclass = SmClass.getClass(metaclassName);
        assert (metaclass != null) : "Unknown metaclass: " + metaclassName;
        return metaclass;
    }

    @objid ("9ec61c13-ccde-11e1-97e5-001ec947c8cc")
    protected Element getSelectedElement(final Object selection, final MClass metaclass) {
        Element selectedElement = null;
        if (selection instanceof Element) {
            selectedElement = (Element) selection;
        } else if (selection instanceof IStructuredSelection && ((IStructuredSelection) selection).size() == 1) {
            Object first = ((IStructuredSelection) selection).getFirstElement();
            if (first instanceof Element) {
                selectedElement = (Element) first;
            } else if (first instanceof IAdaptable) {
                selectedElement = (Element) ((IAdaptable) first).getAdapter(Element.class);
            }
        }
        return selectedElement;
    }

    @objid ("0067631c-9025-1006-9c1d-001ec947cd2a")
    private static void selectAndEditInBrowser(MPart part, Element elementToSelect) {
        assert (part.getObject() instanceof BrowserView) : "Handler used on a part other than BrowserView!";
        BrowserView view = (BrowserView) part.getObject();
        if (view != null) {
            view.edit(elementToSelect);
        }
    }

    @objid ("00677cb2-9025-1006-9c1d-001ec947cd2a")
    protected void postCommit(MPart part, Element element) {
        // Empty default implementation.
    }

    @objid ("00679684-9025-1006-9c1d-001ec947cd2a")
    protected void postCreationStep(Element createdElement) {
        // Empty default implementation.
    }

}
