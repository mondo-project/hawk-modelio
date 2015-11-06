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
                                    

package org.modelio.audit.checker;

import java.util.Date;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.swt.widgets.Shell;
import org.modelio.app.core.navigate.IModelioNavigationService;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.audit.service.IAuditService;
import org.modelio.gproject.model.IMModelServices;

@objid ("d27b6bff-89d5-41ff-a94a-36b28b7c56b8")
public class CheckElementHandler {
    @objid ("ee6f0fdb-5ffb-4404-84c4-0fd082241910")
    @Execute
    public void execute(@Named(IServiceConstants.ACTIVE_SELECTION) final Object selection, IAuditService auditService, Shell shell, @Optional IProjectService projectService, IMModelServices modelService, IModelioNavigationService navigationService, MApplication application, EModelService emService) {
        String jodId = getJobId();  
        CheckerView view = new CheckerView(shell,projectService.getSession(),selection, modelService, navigationService,application, emService,auditService,projectService,jodId);
        view.open();
    }

    @objid ("518c8ff3-bfd2-42c7-8c5c-54cad9fc2428")
    private String getJobId() {
        Date date = new Date();
        return date.toString();
    }

    @objid ("abe7bd0c-5df4-4e84-8f6c-20c72544832c")
    @CanExecute
    public boolean isEnabled() {
        return true;
    }

}
