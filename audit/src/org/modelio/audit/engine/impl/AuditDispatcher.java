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
                                    

package org.modelio.audit.engine.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.audit.engine.core.IRule;
import org.modelio.audit.rules.R3260;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.vcore.session.api.model.change.IElementDeletedEvent;
import org.modelio.vcore.session.api.model.change.IElementMovedEvent;
import org.modelio.vcore.session.api.model.change.IModelChangeEvent;
import org.modelio.vcore.session.api.model.change.IModelChangeListener;
import org.modelio.vcore.session.api.model.change.IStatusChangeEvent;
import org.modelio.vcore.session.api.model.change.IStatusChangeListener;
import org.modelio.vcore.smkernel.SmObjectImpl;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * The AuditDispatcher carries out the following task sequence: <li>listen to
 * model change events <li>analyze the incoming model change event <li>produce
 * the required controls by matching the model change event and the current
 * audit plan definitions <li>post the required controls into the CheckProgram
 */
@objid ("8554d7c2-33f3-43c6-a7cb-d121cc521abf")
public class AuditDispatcher implements IModelChangeListener, IStatusChangeListener {
    @objid ("f6edb03b-381d-43b8-a4fb-7a53b357d37f")
    private boolean active;

    @objid ("08778ba2-0e17-4b32-80f9-0c35dcfded81")
    private static String MODELCHANGE_JOB = "MODELCHANGE_JOB";

    @objid ("8ea36850-3a57-4510-8d80-b7a8b70eea95")
    public AuditPlan plan = null;

    @objid ("863552bc-dbfe-41e9-87df-98b9e4253439")
    public CheckProgram controlProgram = null;

    @objid ("afb6763a-e5c9-489c-96e9-ed855215c915")
    @Override
    public void modelChanged(IModelChangeEvent event) {
        // System.out.println("AuditDispatcher: model change");
        if (!this.active)
            return;
        
        for (MObject createdElement : event.getCreationEvents()) {
            for (IRule rule : getRules(createdElement, AuditTrigger.CREATE)) {
                this.controlProgram
                        .postControl(rule.getCreationControl(createdElement),
                                createdElement,MODELCHANGE_JOB);
            }
        }
        
        // Deleted Events are interpreted as an update of the deleted element's
        // old parent
        for (IElementDeletedEvent deletedEvent : event.getDeleteEvents()) {
            MObject oldParent = deletedEvent.getOldParent();
            if (oldParent.isValid()) {
                for (IRule rule : getRules(oldParent, AuditTrigger.UPDATE)) {
                    this.controlProgram.postControl(
                            rule.getUpdateControl(oldParent), oldParent,MODELCHANGE_JOB);
                }
                for (IRule rule : getRules(deletedEvent.getDeletedElement(),
                        AuditTrigger.DELETE)) {
                    this.controlProgram.postControl(
                            rule.getDeleteControl(oldParent), oldParent,MODELCHANGE_JOB);
                }
            }
        }
        
        for (MObject updatedElement : event.getUpdateEvents()) {
            for (IRule rule : getRules(updatedElement, AuditTrigger.UPDATE)) {
                this.controlProgram.postControl(
                        rule.getUpdateControl(updatedElement), updatedElement,MODELCHANGE_JOB);
            }
        }
        
        for (IElementMovedEvent moveEvent : event.getMoveEvents()) {
            // process a MOVE on the moved element
            for (IRule rule : getRules(moveEvent.getMovedElement(),
                    AuditTrigger.MOVE)) {
                this.controlProgram.postControl(rule.getMoveControl(moveEvent),
                        moveEvent.getMovedElement(),MODELCHANGE_JOB);
            }
            // simulate an UPDATE on the 'old' parent of the moved element
            // the 'new' parent is not considered here as it remains accessible
            // via the moved element
            for (IRule rule : getRules(moveEvent.getOldParent(),
                    AuditTrigger.UPDATE)) {
                this.controlProgram.postControl(
                        rule.getUpdateControl(moveEvent.getOldParent()),
                        moveEvent.getOldParent(),MODELCHANGE_JOB);
            }
        }
    }

    @objid ("3495341e-94af-457a-8053-59f40ae6c7ce")
    public AuditDispatcher(CheckProgram controlProgram) {
        this.controlProgram = controlProgram;
        this.plan = new AuditPlan(); // empty plan
    }

    @objid ("faadbc73-95db-4c5e-8383-798340550543")
    public synchronized void start() {
        this.active = true;
    }

    @objid ("7d25117e-0251-4f94-8d1a-43c79709688e")
    public synchronized void stop() {
        this.active = false;
    }

    @objid ("bbbf2e51-fee8-4d26-be9f-a6b8d6c252c8")
    public synchronized void setPlan(AuditPlan activePlan) {
        stop();
        this.plan = activePlan;
        start();
    }

    @objid ("10215047-1e6a-40ff-b323-8314090ce373")
    public void submitElement(MObject elementToCheck, String jobId) {
        // Do not audit ramcs
        if (elementToCheck.getStatus().isRamc())
            return ;
        
        // submit an element is simulated as both a creation + an update
        if(elementToCheck.isValid()){
            for (IRule rule : this.plan.getRules(elementToCheck.getMClass().getName(),
                    AuditTrigger.CREATE)) {
            
                this.controlProgram.postControl(
                        rule.getCreationControl(elementToCheck), elementToCheck,jobId);
            }
            
            for (IRule rule : this.plan.getRules(elementToCheck.getMClass().getName(),
                    AuditTrigger.UPDATE)) {
                this.controlProgram.postControl(
                        rule.getUpdateControl(elementToCheck), elementToCheck,jobId);
            }
        }else if(elementToCheck.isShell()){
            IRule rule =  this.plan.getRuleById("R3260");
            this.controlProgram.postControl(
                    rule.getUpdateControl(elementToCheck), elementToCheck,jobId);
        }
    }

    /**
     * Gets all the rules to apply on the element for the given trigger. If the
     * element has at least one stereotype, it returns all the rules concerned
     * by each stereotype. Else it returns all the rules concerned by the
     * element's metaclass.
     * @param element The element
     * @param trigger The type of event that happened to the element.
     * @return The list of rules to check.
     */
    @objid ("a8857f28-6876-43a6-9332-2da6a4b43ab3")
    private List<IRule> getRules(final MObject element, final int trigger) {
        List<IRule> returnedRules = new ArrayList<>();
        if(element.isValid()){
            // Do not audit ramcs
            if (element.getStatus().isRamc())
                return Collections.emptyList();
            
            // Only ModelElements can have stereotypes
            if (element instanceof ModelElement) {
                
                // Do not audit ramcs
                if (element.getStatus().isRamc())
                    return Collections.emptyList();
                ModelElement elt = (ModelElement) element;
            
                // Fetching the concerned rules for each stereotype of the element
                for (Stereotype stereotype : elt.getExtension())
                    returnedRules.addAll(this.plan.getRules(elt.getMClass().getName()
                            + stereotype.getName(), trigger));
            }
        }
               
        
        // No stereotypes were found, we get the rules concerned by the
        // metaclass
        if (returnedRules.isEmpty())
            return this.plan.getRules(element.getMClass().getName(), trigger);
        else
            return returnedRules;
    }

    @objid ("25ce9e58-934d-465a-86b4-333fa704d781")
    public void clearCheck() {
        this.controlProgram.clearCleck();
    }

    @objid ("e7704819-1918-4dbc-a7b3-05222cd69c7a")
    @Override
    public void statusChanged(IStatusChangeEvent event) {
        Collection<SmObjectImpl> elements = event.getShellStateChanged();      
        for(SmObjectImpl element : elements) {          
            IRule rule =  this.plan.getRuleById("R3260");
            this.controlProgram.postControl(rule.getUpdateControl(element), element,MODELCHANGE_JOB);   
        }
    }

}
