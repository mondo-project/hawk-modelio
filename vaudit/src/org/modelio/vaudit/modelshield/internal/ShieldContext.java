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
                                    

package org.modelio.vaudit.modelshield.internal;

import java.util.HashSet;
import java.util.Set;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.vaudit.modelshield.IErrorReport;
import org.modelio.vaudit.modelshield.ModelShield;
import org.modelio.vaudit.modelshield.standard.checkers.IChecker;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Execution context for a check by the {@link ModelShield}
 */
@objid ("002804f8-0000-0623-0000-000000000000")
public class ShieldContext {
    @objid ("00593990-d6c7-1f60-8473-001ec947cd2a")
    private final Set<String> alreadyApplied = new HashSet<>();

    @objid ("002804f8-0000-062a-0000-000000000000")
    private final IErrorReport diagnostic;

    @objid ("01f40340-0000-6eee-0000-000000000000")
    public ShieldContext(IErrorReport diagnostic) {
        this.diagnostic = diagnostic;
    }

    @objid ("002804f8-0000-066d-0000-000000000000")
    public void addDiagnosticEntry(final ModelError anEntry) {
        this.diagnostic.addEntry(anEntry);
    }

    @objid ("002804f8-0000-0677-0000-000000000000")
    public void applyChecker(final IChecker checker, final MObject obj) {
        if (!this.alreadyApplied.contains(getEntryIndexKey(checker, obj))) {
            this.alreadyApplied.add(getEntryIndexKey(checker, obj));
            doApplyChecker(checker, obj);
        }
    }

    @objid ("002804f8-0000-0673-0000-000000000000")
    private void doApplyChecker(final IChecker checker, final MObject element) {
        if (!element.getStatus().isDeleted()) {
            checker.check(element, this.diagnostic);
        }
    }

    @objid ("008057a0-d6c6-1f60-8473-001ec947cd2a")
    private static String getEntryIndexKey(final IChecker checker, final MObject element) {
        return checker.toString() + element.getUuid().toString();
    }

}
