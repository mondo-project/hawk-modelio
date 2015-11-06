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
                                    

package org.modelio.vaudit.modelshield.standard;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.vaudit.modelshield.CheckStatus;
import org.modelio.vaudit.modelshield.IErrorReport;
import org.modelio.vaudit.modelshield.IProtectionAgent;
import org.modelio.vaudit.modelshield.internal.ShieldContext;
import org.modelio.vaudit.modelshield.standard.checkers.E200Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E201Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E202Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E203Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E204Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E205Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E206Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E207Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E208Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E209Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E210Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E211Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E212Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E213Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E214Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E215Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E216Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E217Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E218Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E219Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E220Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E221Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E222Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E225Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E226Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E227Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E228Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E229Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E230Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E231Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E232Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E233Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E234Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E235Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E236Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E237Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E238Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E239Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E240Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E241Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E242Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E243Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E244Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E245Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E246Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E247Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E248Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E249Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E250Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E251Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E252Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E253Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E254Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E255Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E256Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E257Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E258Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E259Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E260Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E261Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E262Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E263Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E264Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E265Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E266Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E267Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E268Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E269Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E270Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E271Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E272Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E273Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E274Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E275Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E276Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E277Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E278Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E279Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E280Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E282Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E283Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E291Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E292Checker;
import org.modelio.vaudit.modelshield.standard.checkers.E293Checker;
import org.modelio.vaudit.modelshield.standard.checkers.tofix.E284Checker;
import org.modelio.vaudit.modelshield.standard.checkers.tofix.E285Checker;
import org.modelio.vaudit.modelshield.standard.checkers.tofix.E286Checker;
import org.modelio.vaudit.modelshield.standard.checkers.tofix.E287Checker;
import org.modelio.vaudit.modelshield.standard.checkers.tofix.E288Checker;
import org.modelio.vaudit.modelshield.standard.checkers.tofix.E289Checker;
import org.modelio.vaudit.modelshield.standard.checkers.tofix.E290Checker;
import org.modelio.vaudit.modelshield.standard.execution.ObjectProcessor;
import org.modelio.vaudit.modelshield.standard.execution.TransactionProcessor;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.session.impl.transactions.Transaction;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * The CoreProtectionAgent is in charge of checking the model against the
 * standard metamodel.
 * 
 * @author phv
 */
@objid ("007812d4-f0b6-1f4c-b2b8-001ec947cd2a")
public class CoreProtectionAgent implements IProtectionAgent {
    @objid ("00079e32-09c6-1f4d-b2b8-001ec947cd2a")
    private final Plan plan;

    @objid ("0008c9ec-09c6-1f4d-b2b8-001ec947cd2a")
    public CoreProtectionAgent() {
        this.plan = new Plan();
        initCheckers(this.plan);
    }

    @objid ("0008e1a2-09c6-1f4d-b2b8-001ec947cd2a")
    @Override
    public String getId() {
        return "CORE";
    }

    @objid ("00090a60-09c6-1f4d-b2b8-001ec947cd2a")
    @Override
    public Plan getPlan() {
        return this.plan;
    }

    @objid ("00095a60-09c6-1f4d-b2b8-001ec947cd2a")
    @Override
    public CheckStatus check(final Transaction theTransaction, final IErrorReport report) {
        ShieldContext context = new ShieldContext(report);
        
        TransactionProcessor v = new TransactionProcessor(this.plan);
        v.check(theTransaction, context);
        return report.getEntries().isEmpty() ? CheckStatus.Success : CheckStatus.Fail;
    }

    @objid ("00098cba-09c6-1f4d-b2b8-001ec947cd2a")
    @Override
    public CheckStatus check(final MObject obj, final IErrorReport report) {
        if (!(obj instanceof Element)) {
            // the core protection agent has nothing to say about non-Element objects
            return CheckStatus.Success;
        }
        
        ShieldContext context = new ShieldContext(report);
        
        ObjectProcessor v = new ObjectProcessor(context, this.plan);
        v.check(obj);
        return report.getEntries().isEmpty() ? CheckStatus.Success : CheckStatus.Fail;
    }

    @objid ("0009beb0-09c6-1f4d-b2b8-001ec947cd2a")
    @Override
    public boolean isInScope(final MObject object) {
        // FIXME: better implementation
        return true;
    }

    @objid ("00824da8-d6c6-1f60-8473-001ec947cd2a")
    private static void initCheckers(final Plan plan) {
        new E200Checker().register(plan);
        new E201Checker().register(plan);
        new E202Checker().register(plan);
        new E203Checker().register(plan);
        new E204Checker().register(plan);
        new E205Checker().register(plan);
        new E206Checker().register(plan);
        new E207Checker().register(plan);
        new E208Checker().register(plan);
        new E209Checker().register(plan);
        new E210Checker().register(plan);
        new E211Checker().register(plan);
        new E212Checker().register(plan);
        new E213Checker().register(plan);
        new E214Checker().register(plan);
        new E215Checker().register(plan);
        new E216Checker().register(plan);
        new E217Checker().register(plan);
        new E218Checker().register(plan);
        new E219Checker().register(plan);
        new E220Checker().register(plan);
        new E221Checker().register(plan);
        new E222Checker().register(plan);
        new E225Checker().register(plan);
        new E226Checker().register(plan);
        new E227Checker().register(plan);
        new E228Checker().register(plan);
        new E229Checker().register(plan);
        new E230Checker().register(plan);
        new E231Checker().register(plan);
        new E232Checker().register(plan);
        new E233Checker().register(plan);
        new E234Checker().register(plan);
        new E235Checker().register(plan);
        new E236Checker().register(plan);
        new E237Checker().register(plan);
        new E238Checker().register(plan);
        new E239Checker().register(plan);
        new E240Checker().register(plan);
        new E241Checker().register(plan);
        new E242Checker().register(plan);
        new E243Checker().register(plan);
        new E244Checker().register(plan);
        new E245Checker().register(plan);
        new E246Checker().register(plan);
        new E247Checker().register(plan);
        new E248Checker().register(plan);
        new E249Checker().register(plan);
        new E250Checker().register(plan);
        new E251Checker().register(plan);
        new E252Checker().register(plan);
        new E253Checker().register(plan);
        new E254Checker().register(plan);
        new E255Checker().register(plan);
        new E256Checker().register(plan);
        new E257Checker().register(plan);
        new E258Checker().register(plan);
        new E259Checker().register(plan);
        new E260Checker().register(plan);
        new E261Checker().register(plan);
        new E262Checker().register(plan);
        new E263Checker().register(plan);
        new E264Checker().register(plan);
        new E265Checker().register(plan);
        new E266Checker().register(plan);
        new E267Checker().register(plan);
        new E268Checker().register(plan);
        new E269Checker().register(plan);
        new E270Checker().register(plan);
        new E271Checker().register(plan);
        new E272Checker().register(plan);
        new E273Checker().register(plan);
        new E274Checker().register(plan);
        new E275Checker().register(plan);
        new E276Checker().register(plan);
        new E277Checker().register(plan);
        new E278Checker().register(plan);
        new E279Checker().register(plan);
        new E280Checker().register(plan);
        new E282Checker().register(plan);
        new E283Checker().register(plan);
        new E284Checker().register(plan);
        new E285Checker().register(plan);
        new E286Checker().register(plan);
        new E287Checker().register(plan);
        new E288Checker().register(plan);
        new E289Checker().register(plan);
        new E290Checker().register(plan);
        new E291Checker().register(plan);
        new E292Checker().register(plan);
        new E293Checker().register(plan);
    }

}
