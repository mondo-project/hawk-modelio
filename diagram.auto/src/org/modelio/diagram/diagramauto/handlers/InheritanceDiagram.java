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
import org.modelio.diagram.diagramauto.diagram.creator.InheritanceCreator;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.vcore.session.api.transactions.ITransaction;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MStatus;

@objid ("397bf5c8-994d-4f08-b555-4645f6573de9")
public class InheritanceDiagram extends AbstractHandler {
    @objid ("c09ca811-d8da-4220-9c81-3e7bccfdf23d")
    @Execute
    public void execute(@Named(IServiceConstants.ACTIVE_SELECTION) final Object selection, IProjectService projectService, IMModelServices modelServices, IModelioEventService eventService) {
        List<MObject> selectedElements = getSelection(selection);
        
        try (ITransaction transaction = projectService.getSession().getTransactionSupport().createTransaction("InheritanceDiagram");) {
            InheritanceCreator ic = new InheritanceCreator(modelServices);
            for (MObject selectedElement : selectedElements) {
                if (selectedElement instanceof Classifier) {
                    AbstractDiagram createDiagram = ic.createDiagram((Classifier) selectedElement);
                    eventService.postAsyncEvent(new IModelioService() {
                        @Override
                        public String getName() {
                            return "InheritanceDiagram";
                        }
                    }, ModelioEvent.EDIT_ELEMENT, createDiagram);
                }
            }
        
            transaction.commit();
        }
    }

    @objid ("fa3371d1-06fe-4a2f-b9df-c4563c94f265")
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
        
                AbstractDiagram existingdiagramauto = pc.getExistingAutoDiagram((Classifier) elt);
        
                // Deactivate if no context is found
                if (pc.getAutoDiagramContext((Classifier) elt) == null) {
                    return false;
                }
        
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
