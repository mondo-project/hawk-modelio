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
                                    

package org.modelio.model.browser.handlers.bpmn;

import javax.inject.Inject;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.widgets.Display;
import org.modelio.app.core.IModelioEventService;
import org.modelio.app.core.IModelioService;
import org.modelio.app.core.events.ModelioEvent;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.bpmn.bpmnDiagrams.BpmnProcessCollaborationDiagram;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnCollaboration;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnProcess;
import org.modelio.metamodel.bpmn.rootElements.BpmnBehavior;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.Operation;
import org.modelio.metamodel.uml.statik.TemplateParameter;
import org.modelio.model.browser.handlers.CreateElementHandler;
import org.modelio.vcore.smkernel.mapi.MClass;
import org.modelio.vcore.smkernel.mapi.MDependency;

@objid ("8c975a0f-c9d8-11e1-b479-001ec947c8cc")
public class CreateBpmnProcessHandler extends CreateElementHandler {
    @objid ("11a28ee4-a493-49a6-9b33-ad73af971f01")
    @Inject
     IModelioEventService eventService;

    @objid ("4e57e2a9-ccde-11e1-97e5-001ec947c8cc")
    @Override
    protected void postCreationStep(Element createdElement) {
        IModelFactory mmFactory = this.mmServices.getModelFactory(createdElement);
        
        BpmnProcess process = (BpmnProcess) createdElement;
        BpmnBehavior behavior = process.getOwner();
        
        if (behavior.getRootElement(BpmnCollaboration.class).isEmpty()) {
            BpmnCollaboration collaboration = mmFactory.createBpmnCollaboration();
            behavior.getRootElement().add(collaboration);
        
            collaboration.setName("locals");
        }
        BpmnProcessCollaborationDiagram diagram = mmFactory.createBpmnProcessCollaborationDiagram();
        process.getProduct().add(diagram);
        
        diagram.setName(this.mmServices.getElementNamer().getUniqueName(diagram));
    }

    @objid ("bba1fa8c-ccfa-11e1-97e5-001ec947c8cc")
    private BpmnBehavior getEffectiveOwner(Element owner) {
        if (owner instanceof BpmnBehavior) {
            return (BpmnBehavior) owner;
        } else {
            IModelFactory mmFactory = this.mmServices.getModelFactory(owner);
            BpmnBehavior behavior = mmFactory.createBpmnBehavior();
        
            if (owner instanceof NameSpace) {
                behavior.setOwner((NameSpace) owner);
            } else if (owner instanceof Operation) {
                behavior.setOwnerOperation((Operation) owner);
            } else if (owner instanceof TemplateParameter) {
                behavior.setOwnerTemplateParameter((TemplateParameter) owner);
            }
        
            behavior.setName(this.mmServices.getElementNamer().getUniqueName(behavior));
        
            return behavior;
        }
    }

    @objid ("53a54878-ccff-11e1-97e5-001ec947c8cc")
    @Override
    protected void postCommit(MPart part, Element element) {
        final BpmnProcessCollaborationDiagram param = ((BpmnProcess) element).getProduct(BpmnProcessCollaborationDiagram.class).get(0);
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                CreateBpmnProcessHandler.this.eventService.postAsyncEvent(new IModelioService() {
                    @Override
                    public String getName() {
                        return "openEditor : AbstractDiagram";
                    }
                }, ModelioEvent.EDIT_ELEMENT, param);
            }
        });
    }

    @objid ("00567c64-cbbf-1006-9c1d-001ec947cd2a")
    @Override
    protected Element doCreate(Element owner, MClass metaclass, MDependency dependency, Stereotype stereotype) {
        BpmnBehavior effectiveOwner = getEffectiveOwner(owner);
        MDependency effectiveDep = effectiveOwner.getMClass().getDependency("RootElement");
        
        final MClass mClass = Metamodel.getMClass(BpmnProcess.class);
        return super.doCreate(effectiveOwner, mClass, effectiveDep, stereotype);
    }

}
