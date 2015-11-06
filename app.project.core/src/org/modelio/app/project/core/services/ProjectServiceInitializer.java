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
                                    

package org.modelio.app.project.core.services;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;

/**
 * E4 model processor to add an {@link IProjectService} to the context.
 */
@objid ("0072e9ee-dcda-103c-9961-001ec947cd2a")
public class ProjectServiceInitializer {
    /**
     * Called by E4.
     * @param context the Eclipse context
     */
    @objid ("00022664-dcdb-103c-9961-001ec947cd2a")
    @Execute
    public static void initialize(IEclipseContext context) {
        context.set(IProjectService.class, new ProjectService(context));
    }

}
