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
                                    

package org.modelio.audit.rules;

import java.util.ArrayList;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.audit.engine.core.IAuditEntry;
import org.modelio.audit.engine.core.IAuditPlan;
import org.modelio.audit.engine.core.IControl;
import org.modelio.audit.engine.core.IRule;
import org.modelio.audit.engine.impl.AbstractControl;
import org.modelio.audit.engine.impl.AbstractRule;
import org.modelio.audit.engine.impl.AuditEntry;
import org.modelio.audit.engine.impl.AuditTrigger;
import org.modelio.audit.engine.impl.IDiagnosticCollector;
import org.modelio.audit.service.AuditSeverity;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.analyst.AnalystProject;
import org.modelio.metamodel.analyst.Dictionary;
import org.modelio.metamodel.analyst.Requirement;
import org.modelio.metamodel.analyst.RequirementContainer;
import org.modelio.metamodel.analyst.Term;
import org.modelio.metamodel.bpmn.bpmnDiagrams.BpmnProcessCollaborationDiagram;
import org.modelio.metamodel.bpmn.bpmnDiagrams.BpmnSubProcessDiagram;
import org.modelio.metamodel.diagrams.ActivityDiagram;
import org.modelio.metamodel.diagrams.ClassDiagram;
import org.modelio.metamodel.diagrams.CommunicationDiagram;
import org.modelio.metamodel.diagrams.DeploymentDiagram;
import org.modelio.metamodel.diagrams.ObjectDiagram;
import org.modelio.metamodel.diagrams.SequenceDiagram;
import org.modelio.metamodel.diagrams.StateMachineDiagram;
import org.modelio.metamodel.diagrams.UseCaseDiagram;
import org.modelio.metamodel.mda.Project;
import org.modelio.metamodel.uml.behavior.commonBehaviors.Event;
import org.modelio.metamodel.uml.behavior.commonBehaviors.Signal;
import org.modelio.metamodel.uml.behavior.stateMachineModel.StateMachine;
import org.modelio.metamodel.uml.infrastructure.NoteType;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.infrastructure.TagType;
import org.modelio.metamodel.uml.infrastructure.properties.EnumeratedPropertyType;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyDefinition;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyEnumerationLitteral;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyTableDefinition;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyType;
import org.modelio.metamodel.uml.statik.Artifact;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.AttributeLink;
import org.modelio.metamodel.uml.statik.Collaboration;
import org.modelio.metamodel.uml.statik.CollaborationUse;
import org.modelio.metamodel.uml.statik.Component;
import org.modelio.metamodel.uml.statik.DataType;
import org.modelio.metamodel.uml.statik.Enumeration;
import org.modelio.metamodel.uml.statik.EnumerationLiteral;
import org.modelio.metamodel.uml.statik.Interface;
import org.modelio.metamodel.uml.statik.Link;
import org.modelio.metamodel.uml.statik.Node;
import org.modelio.metamodel.uml.statik.Operation;
import org.modelio.metamodel.uml.statik.Parameter;
import org.modelio.vcore.session.api.model.change.IElementMovedEvent;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Rule implementation origin: NameChecker checkHasName
 */
@objid ("cd8504ed-2089-441b-b382-b1682d86bb14")
public class R2050 extends AbstractRule {
    @objid ("3e94a22a-6080-49ca-b074-704046a094cc")
    private static final String RULEID = "R2050";

    /**
     * The checker unique instance. Remove it if you are not using a unique checker strategy.<br>
     * 
     * @see AbstractRule#getCreationControl(IElement)
     * @see AbstractRule#getUpdateControl(IElement)
     * @see AbstractRule#getMoveControl(IElementMovedEvent)
     */
    @objid ("696b81b6-408d-450b-a387-bc42bead354c")
    private CheckR2050 checkerInstance = null;

    @objid ("536a9669-0726-40c7-9a99-153fb5ec96c4")
    @Override
    public String getRuleId() {
        return R2050.RULEID;
    }

    @objid ("b5db5129-0bb0-492c-bff9-4c2cb42aadc1")
    @Override
    public void autoRegister(IAuditPlan plan) {
        plan.registerRule(Metamodel.getMClass(AnalystProject.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Artifact.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Attribute.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(AttributeLink.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(org.modelio.metamodel.uml.statik.Class.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Collaboration.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(CollaborationUse.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Component.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(DataType.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Dictionary.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Enumeration.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(EnumerationLiteral.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(EnumeratedPropertyType.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Event.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Interface.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Link.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Node.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(NoteType.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Operation.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(org.modelio.metamodel.uml.statik.Package.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Parameter.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Project.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(PropertyDefinition.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(PropertyEnumerationLitteral.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(PropertyTableDefinition.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(PropertyType.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Requirement.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(RequirementContainer.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Signal.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(StateMachine.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Stereotype.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(TagType.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Term.class).getName(), this, AuditTrigger.UPDATE);
        
        //Diagram.Behavior
        plan.registerRule(Metamodel.getMClass(ActivityDiagram.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(BpmnProcessCollaborationDiagram.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(BpmnSubProcessDiagram.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(CommunicationDiagram.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(SequenceDiagram.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(StateMachineDiagram.class).getName(), this, AuditTrigger.UPDATE);
        
        //Diagram.Static
        plan.registerRule(Metamodel.getMClass(ClassDiagram.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(DeploymentDiagram.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(ObjectDiagram.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(UseCaseDiagram.class).getName(), this, AuditTrigger.UPDATE);
    }

    /**
     * Default implementation is using a unique instance for the checker. An alternative implementation consists in
     * creating a new instance of the checker for each element to check. This allows for fine tuning of the check
     * depending on the element status or on some external conditions. Use the 'new instance' strategy only if fine
     * tuning of the check is required for each element, because this strategy creates many objects (performance
     * issues).
     */
    @objid ("442d5018-4724-4286-8044-b98c91bf7392")
    @Override
    public IControl getCreationControl(MObject element) {
        return null;
    }

    /**
     * Default implementation is using a unique instance for the checker. An alternative implementation consists in
     * creating a new instance of the checker for each element to check. This allows for fine tuning of the check
     * depending on the element status or on some external conditions. Use the 'new instance' strategy only if fine
     * tuning of the check is required for each element, because this strategy creates many objects (performance
     * issues).
     */
    @objid ("bb70a777-0024-4432-ae67-dc9c457480d6")
    @Override
    public IControl getMoveControl(IElementMovedEvent moveEvent) {
        return null;
    }

    /**
     * Default implementation is using a unique instance for the checker. An alternative implementation consists in
     * creating a new instance of the checker for each element to check. This allows for fine tuning of the check
     * depending on the element status or on some external conditions. Use the 'new instance' strategy only if fine
     * tuning of the check is required for each element, because this strategy creates many objects (performance
     * issues).
     */
    @objid ("dcde7b63-a303-4b8b-b674-aada56f72d33")
    @Override
    public IControl getUpdateControl(MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default constructor for R2050
     */
    @objid ("ca74271d-4279-4f33-819a-b24e784033ab")
    public R2050() {
        this.checkerInstance = new CheckR2050(this);
    }

    @objid ("97fe712e-86db-42d0-bac1-c98b2db0d0bd")
    static class CheckR2050 extends AbstractControl {
        @objid ("99253c63-e4cb-40e1-a497-31432f81c36e")
        public CheckR2050(IRule rule) {
            super(rule);
        }

        @objid ("22f296bb-0e48-4304-b76d-6f8a7579f67a")
        @Override
        public IDiagnosticCollector doRun(IDiagnosticCollector diagnostic, MObject element) {
            diagnostic.addEntry(checkR2050(element));
            return diagnostic;
        }

        @objid ("56d37819-3625-47fc-b676-9cb34dbea08f")
        private IAuditEntry checkR2050(MObject element) {
            AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(),
                                                   AuditSeverity.AuditSuccess,
                                                   element,
                                                   null);
            
            // Case to ignore return parameters which don't have a name.
            if (element instanceof Parameter && ((Parameter) element).getReturned() != null)
                return auditEntry;
            
            if (element.getName().equals("")) {
                auditEntry.setSeverity(this.rule.getSeverity());
                ArrayList<Object> linkedObjects = new ArrayList<>();
                linkedObjects.add(element.getMClass().getName());
                auditEntry.setLinkedInfos(linkedObjects);
            }
            return auditEntry;
        }

    }

}
