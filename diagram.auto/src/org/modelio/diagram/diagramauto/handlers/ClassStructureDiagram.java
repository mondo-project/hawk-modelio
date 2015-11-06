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
                                    

package org.modelio.diagram.diagramauto.handlers;

import java.util.List;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.modelio.app.core.IModelioEventService;
import org.modelio.app.core.IModelioService;
import org.modelio.app.core.events.ModelioEvent;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.diagram.diagramauto.diagram.creator.ClassStructureCreator;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.vcore.session.api.transactions.ITransaction;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MStatus;

@objid ("327a99b7-f436-4891-9e5f-b02e0495f72f")
public class ClassStructureDiagram extends AbstractHandler {
    @objid ("edf74eaa-3217-4e79-a2d4-190dfdea5540")
    @Execute
    public void execute(@Named(IServiceConstants.ACTIVE_SELECTION) final Object selection, IProjectService projectService, IMModelServices modelServices, IModelioEventService eventService) {
        List<MObject> selectedElements = getSelection(selection);
        try (ITransaction transaction = projectService.getSession().getTransactionSupport()
                .createTransaction("ClassStructureDiagram");) {
        
            ClassStructureCreator csc = new ClassStructureCreator(modelServices);
            for (MObject selectedElement : selectedElements) {
                if (selectedElement instanceof Classifier) {
                    AbstractDiagram createDiagram = csc.createDiagram((Classifier) selectedElement);
        
                    eventService.postAsyncEvent(new IModelioService() {
                        @Override
                        public String getName() {
                            return "ClassStructureDiagram";
                        }
                    }, ModelioEvent.EDIT_ELEMENT, createDiagram);
                }
            }
            transaction.commit();
        }
    }

    @objid ("7d8ec1fc-0882-4b34-b05e-f839355c75e7")
    @CanExecute
    public boolean isEnabled(@Named(IServiceConstants.ACTIVE_SELECTION) final Object selection, IMModelServices modelServices) {
        ClassStructureCreator pc = new ClassStructureCreator(modelServices);
        
        List<MObject> selectedElements = getSelection(selection);
        for (MObject elt : selectedElements) {
            if ((elt instanceof Classifier)) {
                MStatus elementStatus = elt.getStatus();
                if (elt.getMClass().isCmsNode() && elementStatus.isCmsManaged()) {
                    if (elementStatus.isRamc()) {
                        return false;
                    }
                } else if (!elt.isModifiable()) {
                    return false;
                }
        
                // Deactivate if no context is found
                if (pc.getAutoDiagramContext((Classifier) elt) == null) {
                    return false;
                }
        
                AbstractDiagram existingdiagramauto = pc.getExistingAutoDiagram((Classifier) elt);
        
                // Unmodifiable diagram means the command is disabled
                if (existingdiagramauto != null && !existingdiagramauto.getStatus().isModifiable()) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return !selectedElements.isEmpty();
    }

}
