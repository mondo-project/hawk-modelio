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
import org.modelio.diagram.diagramauto.diagram.creator.ClassArchitectureCreator;
import org.modelio.diagram.diagramauto.diagram.creator.ClassStructureCreator;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.vcore.session.api.transactions.ITransaction;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MStatus;

@objid ("84a28ca5-b3ec-47ce-8d63-717c12badb78")
public class ClassArchitectureDiagram extends AbstractHandler {
    @objid ("94e40a11-5cf8-435d-8ba3-d7747e4c2147")
    @Execute
    public void execute(@Named(IServiceConstants.ACTIVE_SELECTION) final Object selection, IProjectService projectService, IMModelServices modelServices, IModelioEventService eventService) {
        List<MObject> selectedElements = getSelection(selection);
        try (ITransaction transaction = projectService.getSession().getTransactionSupport()
                .createTransaction("ClassStructureDiagram");) {
        
            ClassArchitectureCreator csc = new ClassArchitectureCreator(modelServices);
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

    @objid ("9b15f578-5307-417c-864e-9d98574cd0ae")
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
