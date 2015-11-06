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
                                    

package org.modelio.vaudit.modelshield.standard.checkers;

import java.util.Collections;
import java.util.Objects;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.analyst.AnalystContainer;
import org.modelio.metamodel.analyst.AnalystElement;
import org.modelio.metamodel.analyst.AnalystItem;
import org.modelio.metamodel.analyst.AnalystPropertyTable;
import org.modelio.metamodel.analyst.BusinessRule;
import org.modelio.metamodel.analyst.Goal;
import org.modelio.metamodel.analyst.Requirement;
import org.modelio.metamodel.analyst.Term;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyTableDefinition;
import org.modelio.vaudit.modelshield.IErrorReport;
import org.modelio.vaudit.modelshield.internal.ModelError;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * E291:
 * <ul>
 * <li>desc = Analyst elements must be of the same type as their composition owner.</li>
 * <li>what = The type of ''{0}'' is not compatible with the type of its parent.</li>
 * </ul>
 */
@objid ("09d39c72-0f6a-4dc7-9344-af5fc9b37f02")
public class E291Checker implements IChecker {
    @objid ("a6c9d210-58ad-4c5c-96f4-bdcf41f0ac9b")
    private static final String ERRORID = "E291";

    /**
     * WARNING : Due to its registration in the plan this checker can be called
     * on any AnalystElement or on an AnalystPropertyTable
     */
    @objid ("d28b4edb-e8a9-41c7-9420-ac69c6e17d84")
    @Override
    public void check(final MObject object, final IErrorReport report) {
        if (object instanceof AnalystElement) {
            doCheckParent((AnalystElement) object, report);
            doCheckChildren((AnalystElement) object, report);
        } else if (object instanceof AnalystContainer) {
            // analyst container
            doCheckChildren((AnalystContainer) object, report);
        } else if (object instanceof AnalystPropertyTable) {
            AnalystItem owner = ((AnalystPropertyTable) object).getAnalystOwner();
            check(owner, report);
        }
    }

    @objid ("d5a2ea3b-e72c-4b7c-a19e-1a4bdda9df04")
    @Override
    public void register(final Plan plan) {
        plan.registerChecker(this, Metamodel.getMClass(Requirement.class), TriggerType.Update, "ParentRequirement");
        plan.registerChecker(this, Metamodel.getMClass(Requirement.class), TriggerType.Update, "OwnerContainer");
        
        plan.registerChecker(this, Metamodel.getMClass(Term.class), TriggerType.Update, "OwnerDictionary");
        
        plan.registerChecker(this, Metamodel.getMClass(Goal.class), TriggerType.Update, "OwnerContainer");
        plan.registerChecker(this, Metamodel.getMClass(Goal.class), TriggerType.Update, "ParentGoal");
        
        plan.registerChecker(this, Metamodel.getMClass(BusinessRule.class), TriggerType.Update, "OwnerContainer");
        plan.registerChecker(this, Metamodel.getMClass(BusinessRule.class), TriggerType.Update, "ParentRule");
        
        plan.registerChecker(this, Metamodel.getMClass(AnalystPropertyTable.class), TriggerType.Update, "Type");
    }

    @objid ("0224a55e-ff27-4aad-9bdc-bc6efe830d8b")
    private void reportError(final MObject object, final IErrorReport report) {
        report.addEntry(new ModelError(ERRORID, object, Collections.emptyList()));
    }

    /**
     * Check type equality while dealing with null values
     * @param parent
     * @param child
     * @param report
     */
    @objid ("7bb696c4-7bf6-4f71-87e0-eefaea3a2018")
    private boolean checkType(AnalystItem parent, AnalystItem child, final IErrorReport report) {
        AnalystPropertyTable childProperties = child.getAnalystProperties();
        if (childProperties == null) {
            // No analyst properties means the element isn't valid
            reportError(child, report);
            return false;
        } else {
            PropertyTableDefinition type = parent.getAnalystProperties().getType();
            if (!Objects.equals(type, childProperties.getType())) {
                reportError(child, report);
                return false;
            }
        }
        return true;
    }

    /**
     * Checks that 'element' is type-compatible with its parent
     */
    @objid ("f97cc4dd-4205-4c56-b202-a1c9c82af5ca")
    private void doCheckParent(AnalystElement element, final IErrorReport report) {
        AnalystItem parent = (AnalystItem) element.getCompositionOwner();
        if (parent != null) {
            checkType(parent, element, report);
        }
        // orphan treated by another checker
    }

    /**
     * Checks that the type of the children are compatible with 'item'
     */
    @objid ("2720ff22-6896-4389-a3b7-3d9ef53d046f")
    private void doCheckChildren(AnalystItem item, final IErrorReport report) {
        for (MObject child : item.getCompositionChildren()) {
            if (child instanceof AnalystElement) {
                // element
                boolean typeOk = checkType(item, (AnalystElement) child, report);
                if (typeOk)
                    doCheckChildren((AnalystElement) child, report);
            } // sub-container are not to be checked
        }
    }

}
