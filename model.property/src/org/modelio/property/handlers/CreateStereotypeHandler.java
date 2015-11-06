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

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.gproject.data.project.FragmentType;
import org.modelio.gproject.fragment.IProjectFragment;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.mda.ModuleComponent;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Profile;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.model.browser.views.BrowserView;
import org.modelio.property.stereotype.creator.StereotypeEditor;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("1c9a7d3f-812e-4ef1-8ab4-e248ae8bc9ed")
public class CreateStereotypeHandler {
    @objid ("6ff676f6-2ac4-40c7-8288-0943b937bc54")
    @Inject
    protected IProjectService projectService;

    @objid ("a9460d8b-94ff-4c94-ab9b-5254b0404987")
    @Inject
    @Optional
    private IMModelServices mmServices;

    /**
     * (non-Javadoc)
     * @see org.eclipse.core.commands.AbstractHandler#isEnabled()
     */
    @objid ("e868a21a-0fb4-488f-8797-6a504e793fb3")
    @CanExecute
    public final boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) final IStructuredSelection selection) {
        if (this.projectService.getSession() == null || this.mmServices==null || selection.isEmpty()) {
            return false;
        }
        if (this.projectService.getOpenedProject() == null) {
            return false;
        }
        if (selection.size() > 1) {
            return false;
        }
        Profile profile = null;
        profile = getSelectedProfile(selection);
        if (profile == null) {            
            IProjectFragment fragment = getSelectedFragment(selection); 
            if (fragment == null) {
                return false;
            }
            ModuleComponent module = getFirstModule(fragment);
            if (module == null) {
                return false;
            }
            profile = getProfileToCreateStereotype(module);
        }
        if (profile != null && !profile.isModifiable()) {
            return false;
        }
        return true;
    }

    @objid ("7780c845-aeac-4806-8149-911c37d5f4d4")
    private Profile getSelectedProfile(IStructuredSelection selection) {
        if (selection.size() == 1) {
            Object selectedObject = selection.getFirstElement();
            if (selectedObject instanceof Profile) {
                return (Profile) selectedObject;
            }
        }
        return null;
    }

    /**
     * (non-Javadoc)
     * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     */
    @objid ("640f3bd7-7072-400e-a9e5-68513d035a80")
    @Execute
    public final void execute(final MPart part, @Named(IServiceConstants.ACTIVE_SELECTION) final IStructuredSelection selection) {
        Profile profile = null;
        profile = getSelectedProfile(selection);
        List<ModelElement> elements = new ArrayList<>();
        IProjectFragment fragment = getSelectedFragment(selection); 
        if (profile == null) {
            elements = getSelectedModelElements(selection);
            ModuleComponent module = getFirstModule(fragment);
            profile = getProfileToCreateStereotype(module);
        }
        StereotypeEditor stereotypeEditor = new StereotypeEditor(this.projectService, this.mmServices);
        Stereotype newStecreotype = stereotypeEditor.create(profile, elements, fragment, true);
        if (newStecreotype != null) selectAndEditInBrowser(part, newStecreotype);
    }

    /**
     * @return
     */
    @objid ("f9939d89-2000-40dd-875e-889b2cc8d533")
    @SuppressWarnings("unchecked")
    private List<ModelElement> getSelectedModelElements(IStructuredSelection selection) {
        List<ModelElement> selectedElements = new ArrayList<>();
        List<Object> selectedObjects = selection.toList();
        for (Object selectedObject : selectedObjects) {
            if (selectedObject instanceof ModelElement && ((ModelElement) selectedObject).isModifiable()) {
                selectedElements.add((ModelElement) selectedObject);
            }
        }
        return selectedElements;
    }

    @objid ("fd122ee9-15df-4c87-93c3-7ca03d744bdc")
    private IProjectFragment getSelectedFragment(IStructuredSelection selection) {
        IProjectFragment fragment = null;
        if (selection.size() == 1) {
            Object selectedObject = selection.getFirstElement();
            if (selectedObject instanceof IProjectFragment) {
                if (((IProjectFragment) selectedObject).getType()== FragmentType.EXML 
                        || ((IProjectFragment) selectedObject).getType()== FragmentType.EXML_SVN) {
                    fragment = (IProjectFragment) selectedObject;
                }
            } else if (selectedObject instanceof ModelElement && ((ModelElement) selectedObject).isModifiable()) {
                fragment = this.projectService.getOpenedProject().getFragment((ModelElement) selectedObject);
            }            
        }
        return fragment;
    }

    @objid ("33937cb7-7c31-4f90-802a-baa37e124951")
    private ModuleComponent getFirstModule(IProjectFragment fragment) {
        for (MObject root : fragment.getRoots()) {
            if (root instanceof ModuleComponent) {
                return (ModuleComponent) root;
            }
        }
        return null;
    }

    @objid ("1a6c8283-8c2f-4064-aade-fccc429e9dcc")
    private Profile getProfileToCreateStereotype(ModuleComponent module) {
        if (!module.getOwnedProfile().isEmpty()) {            
            // Get LocalProfile if have
            for (Profile profile : module.getOwnedProfile()) {
                if (("LocalProfile").equals(profile.getName())) {                
                    return profile;
                }
            }
            return module.getOwnedProfile().get(0);
        }
        return null;
    }

    @objid ("b65ac2bf-4239-49e4-aae9-85292a37377e")
    private void selectAndEditInBrowser(MPart part, Element elementToSelect) {
        assert (part.getObject() instanceof BrowserView) : "Handler used on a part other than BrowserView!";
        BrowserView view = (BrowserView) part.getObject();
        if (view != null) {
            view.edit(elementToSelect);
        }
    }

}
