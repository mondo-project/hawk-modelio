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
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.Interface;
import org.modelio.metamodel.uml.statik.InterfaceRealization;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.Port;
import org.modelio.metamodel.uml.statik.ProvidedInterface;
import org.modelio.vcore.session.api.model.change.IElementMovedEvent;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Rule implementation origin:  PortChecker checkIsBehaviorImplementation
 */
@objid ("b6e1b857-415b-439c-bcf5-6d14fb34ed58")
public class R2550 extends AbstractRule {
    @objid ("17945ae7-f3d3-49c8-bc5d-e5bd6a539794")
    private static final String RULEID = "R2550";

    /**
     * The checker unique instance. Remove it if you are not using a unique checker strategy.<br>
     * @see AbstractRule#getCreationControl(Element)
     * @see AbstractRule#getUpdateControl(Element)
     * @see AbstractRule#getMoveControl(ElementMovedEvent)
     */
    @objid ("9b39a58f-2a6f-4a13-b114-a39961d4cfe8")
    private CheckR2550 checkerInstance = null;

    @objid ("b99f509d-f679-4dbe-a81b-608f56fe38a7")
    @Override
    public String getRuleId() {
        return R2550.RULEID;
    }

    @objid ("ba105df9-bcfc-4993-b6c5-f4540bae2c62")
    @Override
    public void autoRegister(IAuditPlan plan) {
        plan.registerRule(Metamodel.getMClass(Port.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(ProvidedInterface.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(InterfaceRealization.class).getName(), this, AuditTrigger.CREATE | AuditTrigger.MOVE | AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Class.class).getName(), this, AuditTrigger.UPDATE);
    }

    /**
     * Default implementation is using a unique instance for the checker. An alternative implementation consists in creating a new instance of the checker for each element to check. This allows for fine tuning of the check depending on the element status or on some external conditions. Use the 'new instance' strategy only if fine tuning of the check is required for each element, because this strategy creates many objects (performance issues).
     */
    @objid ("623632da-ae91-4a7f-9bb9-351ce9cf09a8")
    @Override
    public IControl getCreationControl(MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default implementation is using a unique instance for the checker. An alternative implementation consists in creating a new instance of the checker for each element to check. This allows for fine tuning of the check depending on the element status or on some external conditions. Use the 'new instance' strategy only if fine tuning of the check is required for each element, because this strategy creates many objects (performance issues).
     */
    @objid ("bc8dffe2-7ea7-4591-9301-d2398bd42521")
    @Override
    public IControl getMoveControl(IElementMovedEvent moveEvent) {
        return this.checkerInstance;
    }

    /**
     * Default implementation is using a unique instance for the checker. An alternative implementation consists in creating a new instance of the checker for each element to check. This allows for fine tuning of the check depending on the element status or on some external conditions. Use the 'new instance' strategy only if fine tuning of the check is required for each element, because this strategy creates many objects (performance issues).
     */
    @objid ("3794639b-3200-43a6-b4f2-35df911e3af7")
    @Override
    public IControl getUpdateControl(MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default constructor for R2550
     */
    @objid ("eef20406-1989-4625-8a29-fae595476e2f")
    public R2550() {
        this.checkerInstance = new CheckR2550(this);
    }

    @objid ("20c8219c-d0d7-4acd-ba60-cf9f18065521")
    public static class CheckR2550 extends AbstractControl {
        @objid ("0e705ad2-8d17-472f-a432-3c7b02d27b62")
        public CheckR2550(IRule rule) {
            super(rule);
        }

        @objid ("f304859d-1e22-43e7-8e6f-3dd0d818afbc")
        @Override
        public IDiagnosticCollector doRun(IDiagnosticCollector diagnostic, MObject element) {
            if(element instanceof Port)
                diagnostic.addEntry(checkR2550((Port) element));
            else if(element instanceof ProvidedInterface)
                diagnostic.addEntry(checkR2550(((ProvidedInterface)element).getProviding()));
            else if(element instanceof InterfaceRealization){
                NameSpace ns = ((InterfaceRealization)element).getImplementer();
                if(ns instanceof Class)
                for(Port port : ((Class)ns).getInternalStructure(Port.class))
                    diagnostic.addEntry(checkR2550(port));
            }
            else if(element instanceof Class)
                for(Port port : ((Class)element).getInternalStructure(Port.class))
                    diagnostic.addEntry(checkR2550(port));            
            else
                Audit.LOG.warning(Audit.PLUGIN_ID,
                            "R2550: unsupported element type '%s'",
                            element.getMClass().getName());
            return diagnostic;
        }

        @objid ("406ff970-d849-45a7-86b7-b06541d0aa50")
        private IAuditEntry checkR2550(final Port port) {
            AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(), AuditSeverity.AuditSuccess, port, null);
            
            if(port.isIsBehavior()){
                
                Classifier clazz = port.getInternalOwner();
                List<Interface> Interfaces = new ArrayList<>();
                
                // Fetches all .Interfaces implemented by the classifier
                for(InterfaceRealization ir : clazz.getRealized())
                    Interfaces.add(ir.getImplemented());
                
                // Checks that all provided .Interfaces are in the list
                for(ProvidedInterface pi : port.getProvided())
                    for(Interface interfaze : pi.getProvidedElement())
                        if(!Interfaces.contains(interfaze)){
                            
                            // Rule failed
                            
                            auditEntry.setSeverity(this.rule.getSeverity());
                            ArrayList<Object> linkedObjects = new ArrayList<>();
                            linkedObjects.add(port);
                            auditEntry.setLinkedInfos(linkedObjects);
                        }
            }
            return auditEntry;
        }

    }

}
