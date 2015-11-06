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
                                    

package org.modelio.core.ui.nsu;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.widgets.Display;
import org.modelio.app.core.navigate.IModelioNavigationService;
import org.modelio.metamodel.uml.statik.NamespaceUse;

@objid ("2faded2d-9db4-4e70-83e2-2bb18c6b1b4d")
public class CauseAnalyser {
    @objid ("a8ade880-90ed-4e07-b5ea-ed1678c794ae")
    public static void showCauses(final NamespaceUse nsu) {
        showCauses(nsu, null);
    }

    /**
     * No instance.
     */
    @objid ("91f056b4-28cd-4aed-aef8-c85174e5b610")
    private CauseAnalyser() {
    }

    @objid ("9f67c8e6-6a2d-46a4-829b-18fd6fe2d8d2")
    public static void showCauses(final NamespaceUse nsu, IModelioNavigationService navigationService) {
        ReportDialog reportDialog = new ReportDialog(Display.getDefault().getActiveShell(), navigationService);
        reportDialog.setBlockOnOpen(false);
        reportDialog.setModel(nsu);
        reportDialog.open();
    }

}
