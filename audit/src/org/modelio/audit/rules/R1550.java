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
import java.util.List;
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
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.AssociationEnd;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.BindableInstance;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.Port;
import org.modelio.vcore.session.api.model.change.IElementMovedEvent;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Rule implementation origin:  BindableInstanceChecker checkTypeIsCompatible
 */
@objid ("03490f60-ba43-42b1-853a-a93628288aca")
public class R1550 extends AbstractRule {
    @objid ("babbfd38-e484-48e0-a1c6-bed1b55887c1")
    private static final String RULEID = "R1550";

    /**
     * The checker unique instance. Remove it if you are not using a unique checker strategy.<br>
     * @see AbstractRule#getCreationControl(IElement)
     * @see AbstractRule#getUpdateControl(IElement)
     * @see AbstractRule#getMoveControl(IElementMovedEvent)
     */
    @objid ("f1dc0477-52ca-4422-a804-17d6c69b10c8")
    private CheckR1550 checkerInstance = null;

    @objid ("ebd0f59a-73fa-4d7c-b70a-40ffed676907")
    @Override
    public String getRuleId() {
        return R1550.RULEID;
    }

    @objid ("2650c1cb-bcd6-4161-a861-41e4c782545d")
    @Override
    public void autoRegister(IAuditPlan plan) {
        //Update the Base or the RepresentedFeature
        plan.registerRule(Metamodel.getMClass(BindableInstance.class).getName(), this, AuditTrigger.UPDATE);
        
        //Update of the type of a possible RepresentedFeatures
        plan.registerRule(Metamodel.getMClass(Attribute.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(AssociationEnd.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Port.class).getName(), this, AuditTrigger.UPDATE);
    }

    /**
     * Default implementation is using a unique instance for the checker. An alternative implementation consists in creating a new instance of the checker for each element to check. This allows for fine tuning of the check depending on the element status or on some external conditions. Use the 'new instance' strategy only if fine tuning of the check is required for each element, because this strategy creates many objects (performance issues).
     */
    @objid ("d6c0e8ae-05b2-401b-ab07-bb89c0c09ccb")
    @Override
    public IControl getCreationControl(MObject element) {
        return null;
    }

    /**
     * Default implementation is using a unique instance for the checker. An alternative implementation consists in creating a new instance of the checker for each element to check. This allows for fine tuning of the check depending on the element status or on some external conditions. Use the 'new instance' strategy only if fine tuning of the check is required for each element, because this strategy creates many objects (performance issues).
     */
    @objid ("cd6dd329-fcb8-4d3a-9c51-1304b77c6d12")
    @Override
    public IControl getMoveControl(IElementMovedEvent moveEvent) {
        return null;
    }

    /**
     * Default implementation is using a unique instance for the checker. An alternative implementation consists in creating a new instance of the checker for each element to check. This allows for fine tuning of the check depending on the element status or on some external conditions. Use the 'new instance' strategy only if fine tuning of the check is required for each element, because this strategy creates many objects (performance issues).
     */
    @objid ("fa411a4a-1396-405f-9598-3bf605fed2d4")
    @Override
    public IControl getUpdateControl(MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default constructor for R1550
     */
    @objid ("1637b6fa-20bf-4335-8498-fd5bcf4b9478")
    public R1550() {
        this.checkerInstance = new CheckR1550(this);
    }

    @objid ("9d86bc47-e5b7-4da0-b9db-39edc7e79bdd")
    static class CheckR1550 extends AbstractControl {
        @objid ("2a9a3a65-297a-4db5-8211-e36694b7ef77")
        public CheckR1550(IRule rule) {
            super(rule);
        }

        @objid ("f031144b-256a-41ef-a3fc-14577108c632")
        @Override
        public IDiagnosticCollector doRun(IDiagnosticCollector diagnostic, MObject element) {
            if (element instanceof BindableInstance)
                diagnostic.addEntry(checkR1550((BindableInstance) element));
            else if(element instanceof Attribute || element instanceof AssociationEnd || element instanceof Port)
                diagnostic.addEntries(checkR1550((ModelElement) element));
            else
                Audit.LOG.warning(Audit.PLUGIN_ID, "R1000: unsupported element type '%s'", element.getMClass().getName());
            return diagnostic;
        }

        @objid ("c06c9557-d368-497f-a3f9-b6f704826806")
        private IAuditEntry checkR1550(final BindableInstance instance) {
            AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(),
                                                   AuditSeverity.AuditSuccess,
                                                   instance,
                                                   null);
            ModelElement modelElement = instance.getRepresentedFeature();
            NameSpace type = instance.getBase();
            
            if (modelElement != null && type != null) {
            
                List<NameSpace> bindTypes = new ArrayList<>();
            
                if (modelElement instanceof Attribute)
                    bindTypes.add(((Attribute) modelElement).getType());
                else if (modelElement instanceof AssociationEnd){
                    AssociationEnd oposite = ((AssociationEnd) modelElement).getOpposite();
                    bindTypes.add(oposite.getSource() != null ? oposite.getSource() : oposite.getOpposite().getTarget());
                }
                else if (modelElement instanceof Port) {
                    bindTypes.add(((Port) modelElement).getBase());
                }
            
                for (NameSpace ns : bindTypes) {
                    if (!type.equals(ns)) {
            
                        //Rule failed
            
                        auditEntry.setSeverity(this.rule.getSeverity());
                        ArrayList<Object> linkedObjects = new ArrayList<>();
                        linkedObjects.add(instance);
                        linkedObjects.add(ns);
                        auditEntry.setLinkedInfos(linkedObjects);
                    }
                }
            }
            return auditEntry;
        }

        @objid ("b3c57f86-6b10-483d-aac1-466c73b698ef")
        private List<IAuditEntry> checkR1550(final ModelElement element) {
            List<IAuditEntry> auditEntries = new ArrayList<>();
            
            for(BindableInstance bi : element.getRepresentingInstance())
                auditEntries.add(checkR1550(bi));
            return auditEntries;
        }

    }

}
