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
                                    

package org.modelio.xmi.gui.report;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.modelio.api.modelio.Modelio;

/**
 * This class is the controller of the XMI report windows.
 * 
 * It provides all needed services and manages the relations between the XMI report model and the XMI report dialog.
 * @author ebrosse
 */
@objid ("5dcb4d19-672f-46d0-93f0-b90325262551")
public class ReportManager {
    @objid ("72dcf311-b26a-403f-ae2c-13aff2d8ef39")
    private static ReportDialog dialog;

    /**
     * This method opens the XMI report dialog
     * @param report : the report model exposed in report dialog
     */
    @objid ("d287868e-137b-44cd-9cc7-c5df08abf99a")
    public static void showGenerationReport(final Shell shell, final ReportModel report) {
        if (report == null || report.isEmpty ()) {
            if (ReportManager.dialog != null &&
                    !ReportManager.dialog.isDisposed ()) {
                ReportManager.dialog.close ();
            }
        } else {
            
            // Get the current display
            Display display = Display.getCurrent();
               
            if (display == null) {
                display = Display.getDefault();
            }  
            
            if (ReportManager.dialog == null ||
                    ReportManager.dialog.isDisposed ()) {
                ReportManager.dialog = new ReportDialog (shell, Modelio.getInstance().getNavigationService());
            }
        
            ReportManager.dialog.setModel(report);
            
            if (ReportManager.dialog.open () == SWT.OK) {            
                shell.dispose();
            }
        }
    }

    /**
     * This method creates a new Report Model
     * @return the created Report Model
     */
    @objid ("fd6e5f07-72e9-42c3-864c-26c2c3e9ebfa")
    public static ReportModel getNewReport() {
        return new ReportModel ();
    }

}
