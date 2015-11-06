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
                                    

package org.modelio.module.propertytab.propertytab;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.modelio.api.module.IModule;
import org.modelio.api.module.propertiesPage.IModulePropertyPage;
import org.modelio.app.core.picking.IModelioPickingService;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.mda.infra.service.IModuleService;
import org.modelio.module.propertytab.plugin.ModulePropertyTab;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("c884caa2-1eba-11e2-9382-bc305ba4815c")
public class ModulePropertyView {
    @objid ("c884f1b0-1eba-11e2-9382-bc305ba4815c")
    private ModulePanelProvider modulePanel;

    @objid ("c884f1b1-1eba-11e2-9382-bc305ba4815c")
    @PostConstruct
    public void createControls(final Composite parentComposite, IProjectService project, IModuleService modules, MPart part, @Optional IModelioPickingService pickingService, @Optional
@Named(IServiceConstants.ACTIVE_SELECTION) final IStructuredSelection selection) {
        // Create the view content
        IModulePropertyPage page = getModulePropertyPage(modules, part);
        this.modulePanel = new ModulePanelProvider(page);
        this.modulePanel.createPanel(parentComposite);
        this.modulePanel.activateEdition(project.getSession(), pickingService);
        if(selection != null)
            update(selection);
    }

    @objid ("c8853fd3-1eba-11e2-9382-bc305ba4815c")
    @Focus
    void setFocus() {
        if (this.modulePanel != null) {
            this.modulePanel.setFocus();
        }
    }

    /**
     * This listener is activated when the selection changes in the workbench.<br>
     * Its responsibility is to set the NotesView's current element.
     * @param selection the current modelio selection.
     */
    @objid ("c88566e1-1eba-11e2-9382-bc305ba4815c")
    @Optional
    @Inject
    public void update(@Named(IServiceConstants.ACTIVE_SELECTION) final IStructuredSelection selection) {
        try {
            // This method listen to the selection changes in the workbench.
            if (selection != null && this.modulePanel != null) {
                List<MObject> selectedElements = new ArrayList<>();
                for (Object object : selection.toList()) {
                    if (object instanceof MObject) {
                        selectedElements.add((MObject) object);
                    } else if (object instanceof IAdaptable) {
                        final MObject adapter = (MObject) ((IAdaptable) object).getAdapter(MObject.class);
                        if (adapter != null) {
                            selectedElements.add(adapter);
                        }
                    } 
                }
                this.modulePanel.setInput(selectedElements);
            }
        } catch (RuntimeException e) {
            // avoid runtime exceptions to go propagate, preventing other tabs to refresh
            ModulePropertyTab.LOG.error(e);
        }
    }

/*
     * Find IModulePropertyPage from Module and PropertyPage name MPart tags contains the Name of Module an index 1 and Name of
     * PropertyPage at index 2
     */
    @objid ("c8858df4-1eba-11e2-9382-bc305ba4815c")
    private IModulePropertyPage getModulePropertyPage(IModuleService modules, MPart part) {
        for (IModule module : modules.getModuleRegistry().getStartedModules()) {
            if (module.getName().equals(part.getTags().get(1))) {
                for (IModulePropertyPage page : module.getPropertyPages()) {
                    if (part.getTags().contains(page.getName())) {
                        return page;
                    }
                }
            }
        }
        return null;
    }

}
