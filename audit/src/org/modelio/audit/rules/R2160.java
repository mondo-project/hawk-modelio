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
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Map;
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
import org.modelio.metamodel.analyst.AnalystItem;
import org.modelio.metamodel.analyst.Requirement;
import org.modelio.metamodel.analyst.RequirementContainer;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.vcore.session.api.model.change.IElementMovedEvent;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Rule implementation origin: NameChecker checkRequirementContainersElements
 */
@objid ("76166d2c-c79e-47b7-abbb-f0bbf1f5a0be")
public class R2160 extends AbstractRule {
    @objid ("52df6e11-8830-4465-bdc2-16241d8e41f2")
    private static final String RULEID = "R2160";

    /**
     * The checker unique instance. Remove it if you are not using a unique checker strategy.<br>
     * 
     * @see AbstractRule#getCreationControl(Element)
     * @see AbstractRule#getUpdateControl(Element)
     * @see AbstractRule#getMoveControl(ElementMovedEvent)
     */
    @objid ("a365eeef-4e38-4813-bf76-d1850b53a812")
    private CheckR2160 checkerInstance = null;

    @objid ("36769696-6090-4c52-a998-db8cbaae4f7d")
    @Override
    public String getRuleId() {
        return R2160.RULEID;
    }

    @objid ("303c34b3-4b8d-4d9a-bcf1-ae0c22981505")
    @Override
    public void autoRegister(IAuditPlan plan) {
        plan.registerRule(Metamodel.getMClass(RequirementContainer.class).getName(), this, AuditTrigger.CREATE |
                AuditTrigger.MOVE |
                AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Requirement.class).getName(), this, AuditTrigger.CREATE |
                AuditTrigger.MOVE |
                AuditTrigger.UPDATE);
    }

    /**
     * Default implementation is using a unique instance for the checker. An alternative implementation consists in
     * creating a new instance of the checker for each element to check. This allows for fine tuning of the check
     * depending on the element status or on some external conditions. Use the 'new instance' strategy only if fine
     * tuning of the check is required for each element, because this strategy creates many objects (performance
     * issues).
     */
    @objid ("95a9d549-7972-4fb0-8a97-697b57ff547f")
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
    @objid ("b4d27d2c-80ca-4a85-8ae9-68e4e21085d9")
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
    @objid ("e49ecd14-695f-4cc9-8fc9-b0b78ec65db4")
    @Override
    public IControl getUpdateControl(MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default constructor for R2160
     */
    @objid ("174e6cab-467d-4bc5-8b8c-f3cb9354eef4")
    public R2160() {
        this.checkerInstance = new CheckR2160(this);
    }

    @objid ("b60696e5-938f-4307-9436-ed4ef11d0053")
    public static class CheckR2160 extends AbstractControl {
        @objid ("b4c3d456-36bf-4751-8116-cd549e02488e")
        public CheckR2160(IRule rule) {
            super(rule);
        }

        @objid ("d23b28a3-89d1-4ce1-9695-988f1a211b6c")
        @Override
        public IDiagnosticCollector doRun(IDiagnosticCollector diagnostic, MObject element) {
            if (element instanceof RequirementContainer) {
                // Checking the RequirementContainer itself, in case it was updated from a move or a delete.
                final RequirementContainer reqContainer = (RequirementContainer) element;
                diagnostic.addEntry(checkR2160(reqContainer));
            
                // Checking the parent RequirementContainer, in case the element was created or moved in a new parent
                final RequirementContainer owner = reqContainer.getOwnerContainer();
                if (owner != null) {
                    diagnostic.addEntry(checkR2160(owner));
                }
            } else if (element instanceof Requirement) {
                final RequirementContainer containerOwner = ((Requirement) element).getOwnerContainer();
                if (containerOwner != null) {
                    diagnostic.addEntry(checkR2160(containerOwner));
                } else {
                    Requirement parentReq = ((Requirement) element).getParentRequirement();
                    if (parentReq != null)
                        diagnostic.addEntry(checkR2160(parentReq));
                }
            } else {
                Audit.LOG.warning(Audit.PLUGIN_ID,
                        "R2160: unsupported element type '%s'",
                        element.getMClass().getName());
            }
            return diagnostic;
        }

        @objid ("b7d65d36-ae70-4ca4-a216-1b4f4d702dc9")
        private IAuditEntry checkR2160(final RequirementContainer reqContainer) {
            final AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(),
                    AuditSeverity.AuditSuccess,
                    reqContainer,
                    null);
            
            final Map<String, List<AnalystItem>> duplicates = new HashMap<>();
            
            for (final RequirementContainer re : reqContainer.getOwnedContainer()) {
                final String name = re.getName();
                if (!duplicates.containsKey(name)) {
                    duplicates.put(name, new ArrayList<AnalystItem>());
                }
                duplicates.get(re.getName()).add(re);
            }
            
            for (final Requirement re : reqContainer.getOwnedRequirement()) {
                final String name = re.getName();
                if (!duplicates.containsKey(name)) {
                    duplicates.put(name, new ArrayList<AnalystItem>());
                }
                duplicates.get(re.getName()).add(re);
            }
            
            for (final Entry<String, List<AnalystItem>> entry : duplicates.entrySet()) {
                if (entry.getValue().size() > 1) {
            
                    //Rule failed
                    auditEntry.setSeverity(this.rule.getSeverity());
                    final ArrayList<Object> linkedObjects = new ArrayList<>();
                    linkedObjects.add(reqContainer);
                    linkedObjects.add(entry.getKey());
                    auditEntry.setLinkedInfos(linkedObjects);
                }
            }
            return auditEntry;
        }

        @objid ("027d25a0-3343-41fe-9eaf-32d7142db02e")
        private IAuditEntry checkR2160(final Requirement req) {
            final AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(), AuditSeverity.AuditSuccess, req, null);
            
            final Map<String, List<AnalystItem>> duplicates = new HashMap<>();
            
            for (final Requirement re : req.getSubRequirement()) {
                final String name = re.getName();
                if (!duplicates.containsKey(name)) {
                    duplicates.put(name, new ArrayList<AnalystItem>());
                }
                duplicates.get(re.getName()).add(re);
            }
            
            for (final Entry<String, List<AnalystItem>> entry : duplicates.entrySet()) {
                if (entry.getValue().size() > 1) {
                    // Rule failed
                    auditEntry.setSeverity(this.rule.getSeverity());
                    final ArrayList<Object> linkedObjects = new ArrayList<>();
                    linkedObjects.add(req);
                    linkedObjects.add(entry.getKey());
                    auditEntry.setLinkedInfos(linkedObjects);
                }
            }
            return auditEntry;
        }

    }

}
