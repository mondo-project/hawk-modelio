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
                                    

package org.modelio.app.project.conf.dialog.modules.list;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.modelio.api.module.IModule.ModuleRuntimeState;
import org.modelio.api.module.IModule;
import org.modelio.api.module.ModuleException;
import org.modelio.app.project.conf.plugin.AppProjectConf;
import org.modelio.gproject.module.GModule;
import org.modelio.mda.infra.service.IModuleService;
import org.modelio.metamodel.mda.ModuleComponent;

/**
 * StyleEditingSupport provides EditingSupport implementation for the StyleViewer.
 * <p>
 * It must be able to provide a Label and a CellEditor for all the supported StyleKey value types. It must also be able to get and
 * set values during edition, again dealing with all the possible StyleKey value types.
 */
@objid ("85ccd9a0-3ef9-11e2-9bd5-002564c97630")
class ModuleStateEditingSupport extends EditingSupport {
    @objid ("8aa2a299-3ef9-11e2-9bd5-002564c97630")
    private TableViewer viewer;

    @objid ("8aa2a29a-3ef9-11e2-9bd5-002564c97630")
    private IEclipseContext applicationContext;

    /**
     * Initialize the StylePropertyEditingSupport.
     * @param applicationContext
     * @param viewer The style viewer.
     */
    @objid ("8aa2a29b-3ef9-11e2-9bd5-002564c97630")
    public ModuleStateEditingSupport(TableViewer viewer, IEclipseContext applicationContext) {
        super(viewer);
        this.viewer = viewer;
        this.applicationContext = applicationContext;
    }

    @objid ("8aa2a2a0-3ef9-11e2-9bd5-002564c97630")
    @Override
    protected boolean canEdit(Object element) {
        return true;
    }

    @objid ("8aa503fb-3ef9-11e2-9bd5-002564c97630")
    @Override
    protected CellEditor getCellEditor(Object element) {
        return new CheckboxCellEditor(this.viewer.getTable());
    }

    @objid ("8aa50401-3ef9-11e2-9bd5-002564c97630")
    @Override
    protected Object getValue(Object element) {
        if (element instanceof GModule) {
            IModuleService moduleService = this.applicationContext.get(IModuleService.class);
            ModuleComponent moduleElement = ((GModule) element).getModuleElement();
            if (moduleElement != null) {
                IModule iModule = moduleService.getIModule(moduleElement);
                if (iModule != null && iModule.getState() == ModuleRuntimeState.Started) {
                    return true;
                }
            }
        }
        return false;
    }

    @objid ("8aa50407-3ef9-11e2-9bd5-002564c97630")
    @Override
    protected void setValue(Object element, Object value) {
        if (element instanceof GModule) {
            IModuleService moduleService = this.applicationContext.get(IModuleService.class);
            GModule gModule = (GModule) element;
        
            try {
                if ((Boolean) value) {
                    AppProjectConf.LOG.info("Starting module " + gModule.getName()); //$NON-NLS-1$
                    moduleService.activateModule(gModule);
                } else {
                    AppProjectConf.LOG.info("Stopping module " + gModule.getName()); //$NON-NLS-1$
                    moduleService.deactivateModule(gModule);
                }
            } catch (ModuleException e) {
                AppProjectConf.LOG.error(e);
            }
        }
            
        this.viewer.setSelection(new StructuredSelection(element));
    }

}
