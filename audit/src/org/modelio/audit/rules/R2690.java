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
import org.modelio.audit.plugin.Audit;
import org.modelio.audit.service.AuditSeverity;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.uml.behavior.commonBehaviors.Signal;
import org.modelio.metamodel.uml.behavior.usecaseModel.Actor;
import org.modelio.metamodel.uml.behavior.usecaseModel.UseCase;
import org.modelio.metamodel.uml.informationFlow.InformationItem;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.statik.Artifact;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.Collaboration;
import org.modelio.metamodel.uml.statik.Component;
import org.modelio.metamodel.uml.statik.DataType;
import org.modelio.metamodel.uml.statik.Enumeration;
import org.modelio.metamodel.uml.statik.Interface;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.Node;
import org.modelio.metamodel.uml.statik.Operation;
import org.modelio.metamodel.uml.statik.Package;
import org.modelio.metamodel.uml.statik.TemplateBinding;
import org.modelio.metamodel.uml.statik.TemplateParameter;
import org.modelio.vcore.session.api.model.change.IElementMovedEvent;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Rule implementation origin: TemplateBindingChecker checkElementAutoBinding
 */
@objid ("944bbbdd-8805-4ed0-92c8-b0712b8b3b3e")
public class R2690 extends AbstractRule {
    @objid ("bbc9103c-1636-4c0c-a06a-674946c5281c")
    private static final String RULEID = "R2690";

    /**
     * The checker unique instance. Remove it if you are not using a unique checker strategy.<br>
     * 
     * @see AbstractRule#getCreationControl(Element)
     * @see AbstractRule#getUpdateControl(Element)
     * @see AbstractRule#getMoveControl(ElementMovedEvent)
     */
    @objid ("64eb00f6-ff0d-431a-88ca-d40a14878cb1")
    private CheckR2690 checkerInstance = null;

    @objid ("75f30d55-e87a-4101-b084-1e056b964199")
    @Override
    public String getRuleId() {
        return R2690.RULEID;
    }

    @objid ("2dc07f5a-fbec-49fd-8bc6-01b7ab02a802")
    @Override
    public void autoRegister(IAuditPlan plan) {
        plan.registerRule(Metamodel.getMClass(TemplateBinding.class).getName(), this, AuditTrigger.CREATE |
                                                                AuditTrigger.MOVE |
                                                                AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Operation.class).getName(), this, AuditTrigger.UPDATE);
        
        //Namespaces
        plan.registerRule(Metamodel.getMClass(Package.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Collaboration.class).getName(), this, AuditTrigger.UPDATE);
        
        //Namespaces.Classifiers
        plan.registerRule(Metamodel.getMClass(InformationItem.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Artifact.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(TemplateParameter.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Actor.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Class.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(DataType.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Enumeration.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Interface.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Signal.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(UseCase.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Node.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Component.class).getName(), this, AuditTrigger.UPDATE);
    }

    /**
     * Default implementation is using a unique instance for the checker. An alternative implementation consists in
     * creating a new instance of the checker for each element to check. This allows for fine tuning of the check
     * depending on the element status or on some external conditions. Use the 'new instance' strategy only if fine
     * tuning of the check is required for each element, because this strategy creates many objects (performance
     * issues).
     */
    @objid ("04780057-3f61-4a7f-b691-4c0e6168b26e")
    @Override
    public IControl getCreationControl(MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default implementation is using a unique instance for the checker. An alternative implementation consists in
     * creating a new instance of the checker for each element to check. This allows for fine tuning of the check
     * depending on the element status or on some external conditions. Use the 'new instance' strategy only if fine
     * tuning of the check is required for each element, because this strategy creates many objects (performance
     * issues).
     */
    @objid ("ed67aca8-cc6e-4669-8f2e-db6cd79149e9")
    @Override
    public IControl getMoveControl(IElementMovedEvent moveEvent) {
        return this.checkerInstance;
    }

    /**
     * Default implementation is using a unique instance for the checker. An alternative implementation consists in
     * creating a new instance of the checker for each element to check. This allows for fine tuning of the check
     * depending on the element status or on some external conditions. Use the 'new instance' strategy only if fine
     * tuning of the check is required for each element, because this strategy creates many objects (performance
     * issues).
     */
    @objid ("470e59e9-695a-4c11-a01e-7bf1a122673a")
    @Override
    public IControl getUpdateControl(MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default constructor for R2690
     */
    @objid ("b7f586fd-1107-4c36-b014-709c3171d061")
    public R2690() {
        this.checkerInstance = new CheckR2690(this);
    }

    @objid ("14660255-31d7-4344-953d-f00f15306cab")
    public static class CheckR2690 extends AbstractControl {
        @objid ("cbc7307e-cc1b-445e-9160-72b84a27f599")
        public CheckR2690(IRule rule) {
            super(rule);
        }

        @objid ("7af9d5f3-bdb3-4ed3-ab5c-4effc9864aa4")
        @Override
        public IDiagnosticCollector doRun(IDiagnosticCollector diagnostic, MObject element) {
            if(element instanceof NameSpace){
                NameSpace ns = (NameSpace)element;
                for(TemplateBinding tb : ns.getTemplateInstanciation())
                    diagnostic.addEntry(checkR2690(tb));
            }
            else if(element instanceof Operation){
                Operation op = (Operation) element;
                for(TemplateBinding tb : op.getTemplateInstanciation())
                    diagnostic.addEntry(checkR2690(tb));
            }
            else if (element instanceof TemplateBinding)
                diagnostic.addEntry(checkR2690((TemplateBinding) element));
            else
                Audit.LOG.warning(Audit.PLUGIN_ID,
                            "R2690: unsupported element type '%s'",
                            element.getMClass().getName());
            return diagnostic;
        }

        @objid ("e2e85b92-47ee-497b-800f-8cce2d5b1bf0")
        private IAuditEntry checkR2690(final TemplateBinding tBinding) {
            AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(),
                                                   AuditSeverity.AuditSuccess,
                                                   tBinding,
                                                   null);
            
            NameSpace nsOrigin = tBinding.getBoundElement();
            Operation opOrigin = tBinding.getBoundOperation();
            
            NameSpace nsTarget = tBinding.getInstanciatedTemplate();
            Operation opTarget = tBinding.getInstanciatedTemplateOperation();
            
            if (nsOrigin != null && nsTarget != null && nsOrigin.equals(nsTarget)) {
            
                // Rule failed
            
                auditEntry.setSeverity(this.rule.getSeverity());
                ArrayList<Object> linkedObjects = new ArrayList<>();
                linkedObjects.add(tBinding);
                linkedObjects.add(nsOrigin);
                auditEntry.setLinkedInfos(linkedObjects);
            
            } else if (opOrigin != null && opTarget != null && opOrigin.equals(opTarget)) {
            
                // Rule failed
            
                auditEntry.setSeverity(this.rule.getSeverity());
                ArrayList<Object> linkedObjects = new ArrayList<>();
                linkedObjects.add(tBinding);
                linkedObjects.add(opOrigin);
                auditEntry.setLinkedInfos(linkedObjects);
            }
            return auditEntry;
        }

    }

}
