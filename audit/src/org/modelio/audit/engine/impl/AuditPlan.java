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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Map;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IPath;
import org.modelio.audit.engine.core.IAuditPlan;
import org.modelio.audit.engine.core.IRule;

@objid ("6c0b5510-231d-4939-9c54-5140e1b0efd6")
public class AuditPlan implements IAuditPlan {
    @objid ("babe9213-ea4a-482d-b0d4-10907fddd266")
    private Map<String, ArrayList<IRule>> createRules = new HashMap<>();

    @objid ("c62e6db8-276b-4f59-84a8-2e03b78f921e")
    private Map<String, ArrayList<IRule>> updateRules = new HashMap<>();

    @objid ("57024f50-7fed-47ec-b80b-66b0b429ae19")
    private Map<String, ArrayList<IRule>> moveRules = new HashMap<>();

    @objid ("5342baed-0686-42a8-b899-e4997248e476")
    private Map<String, ArrayList<IRule>> deleteRules = new HashMap<>();

    @objid ("4f9230f5-fc5e-48cc-86c6-a2eda37c1afd")
    private Map<String, IRule> unspecifiedRule = new HashMap<>();

    @objid ("758101c6-377b-471b-9956-0ba4366323b4")
    @Override
    public void registerRule(String metaclass, IRule rule, int triggers) {
        if (AuditTrigger.isCreate(triggers)) {
            if (this.createRules.get(metaclass) == null)
                this.createRules.put(metaclass, new ArrayList<IRule>());
            this.createRules.get(metaclass).add(rule);
        }
        
        if (AuditTrigger.isUpdate(triggers)) {
            if (this.updateRules.get(metaclass) == null)
                this.updateRules.put(metaclass, new ArrayList<IRule>());
            this.updateRules.get(metaclass).add(rule);
        }
        if (AuditTrigger.isMove(triggers)) {
            if (this.moveRules.get(metaclass) == null)
                this.moveRules.put(metaclass, new ArrayList<IRule>());
            this.moveRules.get(metaclass).add(rule);
        }
        if (AuditTrigger.isDelete(triggers)) {
            if (this.deleteRules.get(metaclass) == null)
                this.deleteRules.put(metaclass, new ArrayList<IRule>());
            this.deleteRules.get(metaclass).add(rule);
        }
    }

    @objid ("3adf8b27-b071-4271-a6cc-1d6f3da54dab")
    @Override
    public List<IRule> getRules(String metaclass, int trigger) {
        List<IRule> results = null;
        
        if (AuditTrigger.isCreate(trigger))
            results = this.createRules.get(metaclass);
        else if (AuditTrigger.isUpdate(trigger))
            results = this.updateRules.get(metaclass);
        else if (AuditTrigger.isMove(trigger))
            results = this.moveRules.get(metaclass);
        else if (AuditTrigger.isDelete(trigger))
            results = this.deleteRules.get(metaclass);
        
        if (results != null)
            return results;
        else
            return Collections.emptyList();
    }

    @objid ("cc554dae-5194-4b00-9390-acf2b3e89556")
    public Map<String, ArrayList<IRule>> getRules(int trigger) {
        Map<String, ArrayList<IRule>> results = null;
        
        if (AuditTrigger.isCreate(trigger))
            results = this.createRules;
        else if (AuditTrigger.isUpdate(trigger))
            results = this.updateRules;
        else if (AuditTrigger.isMove(trigger))
            results = this.moveRules;
        else if (AuditTrigger.isDelete(trigger))
            results = this.deleteRules;
        
        if (results != null)
            return results;
        else
            return Collections.emptyMap();
    }

    @objid ("912cd2ab-a6f6-44e8-ad10-875c6e49e28e")
    public void dump(PrintStream out) {
        // dump plan
        out.println("-- AUDIT PLAN Composition --");
        out.println("ON CREATE");
        for (Entry<String, ArrayList<IRule>> entry : this.getRules(AuditTrigger.CREATE).entrySet()) {
            out.print(" - " + entry.getKey() + " = ");
            for (IRule rule : entry.getValue()) {
                out.print(rule.getRuleId() + " ");
            }
            out.println();
        }
        out.println("ON UPDATE");
        for (Entry<String, ArrayList<IRule>> entry : this.getRules(AuditTrigger.UPDATE).entrySet()) {
            out.print(" - " + entry.getKey() + " = ");
            for (IRule rule : entry.getValue()) {
                out.print(rule.getRuleId() + " ");
            }
            out.println();
        }
        
        out.println("ON MOVE");
        for (Entry<String, ArrayList<IRule>> entry : this.getRules(AuditTrigger.MOVE).entrySet()) {
            out.print(" - " + entry.getKey() + " = ");
            for (IRule rule : entry.getValue()) {
                out.print(rule.getRuleId() + " ");
            }
            out.println();
        }
        out.println("-- --");
    }

    @objid ("2bbf7b5c-e1b0-4e47-b69a-fc353bf482dd")
    public void dump(IPath iPath) {
        PrintStream fout;
        try {
            fout = new PrintStream(new FileOutputStream(iPath.toFile()));
            dump(fout);
            fout.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @objid ("df4f6fc0-5cbc-4cd8-9d5f-033769a42669")
    @Override
    public void registerRule(IRule rule) {
        this.unspecifiedRule.put(rule.getRuleId(),rule);
    }

    @objid ("63a0a96b-b650-4f25-84a1-c8cd6866c745")
    @Override
    public IRule getRuleById(String ruleId) {
        return this.unspecifiedRule.get(ruleId);
    }

}
