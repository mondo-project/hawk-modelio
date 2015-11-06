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
                                    

package org.modelio.app.project.core.services;

import java.io.IOException;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.modelio.app.project.core.plugin.AppProjectCore;
import org.modelio.core.ui.progress.ModelioProgressAdapter;
import org.modelio.gproject.fragment.FragmentAuthenticationException;
import org.modelio.gproject.fragment.FragmentMigrationNeededException;
import org.modelio.gproject.fragment.IProjectFragment;
import org.modelio.gproject.gproject.GProject;
import org.modelio.vbasic.progress.SubProgress;

/**
 * Service to propose the user to migrate one or more fragments.
 * @author cmarin
 */
@objid ("16f8c376-8e39-4ad2-b7b6-e08070c1af49")
public class FragmentsMigrator {
    @objid ("6f0a469f-6b49-491d-bd4d-6032ade960d4")
    private GProject project;

    /**
     * C'tor
     * @param project the project to work on.
     */
    @objid ("042f317a-bf12-42ab-bbf5-467301437964")
    public FragmentsMigrator(GProject project) {
        this.project = project;
    }

    /**
     * Propose migration of all model fragments that may be migrated.
     * @param parentMon the progress monitor to use
     * @param allowedMonWork the maximum progress ticks this method may use
     */
    @objid ("2afc1535-052c-439e-84e7-d17d8de792eb")
    public void migrateFragments(SubMonitor parentMon, int allowedMonWork) {
        int migrationNeeded = 0;
        for (IProjectFragment f : this.project.getOwnFragments()) {
            if (f.getDownError() instanceof FragmentMigrationNeededException) {
                migrationNeeded++;
            }
        }
               
        Shell parentShell = null;
        if (migrationNeeded > 0 ) {
            String taskName = AppProjectCore.I18N.getMessage("FragmentsMigrator.monitor.begin", migrationNeeded);
            parentMon.setTaskName(taskName);
            parentMon.subTask(taskName);
            
            for (IProjectFragment f : this.project.getOwnFragments()) {
                migrateFragment( f, parentShell, parentMon, 1);
            }
        }
    }

    @objid ("82717dbf-6005-4fe9-a9bc-be5a19af828f")
    private boolean askForMigration(final FragmentMigrationNeededException migrInfo, final Shell parentShell) {
        final int [] ret = new int[1];
        Display.getDefault().syncExec(new Runnable() {
            
            @Override
            public void run() {
                ConfirmMigrationDialog dlg = new ConfirmMigrationDialog(parentShell, migrInfo);
                ret[0] = dlg.open();
            }
        });
        return ret[0] == IDialogConstants.OK_ID;
    }

    /**
     * Propose migration of a fragment.
     * @param f the fragment to migrate
     * @param parentShell a parent SWT shell
     * @param parentMon the progress monitor to use
     * @param allowedMonWork the maximum progress ticks this method may use
     */
    @objid ("50464582-98ef-465e-8abc-f9d4443e5562")
    public void migrateFragment(IProjectFragment f, Shell parentShell, SubMonitor parentMon, int allowedMonWork) {
        if (f.getDownError() instanceof FragmentMigrationNeededException) {
            
            boolean acceptMigration = askForMigration((FragmentMigrationNeededException) f.getDownError(), parentShell);
            if (acceptMigration) {
                try {
                    SubProgress mon = ModelioProgressAdapter.convert(parentMon.newChild(allowedMonWork), 2);
                    f.migrate(this.project, mon.newChild(1));
        
                    f.mount(this.project, mon.newChild(1));
                } catch (IOException | RuntimeException migrationFailExc) {
                    Throwable oldError = f.getDownError();
                    migrationFailExc.addSuppressed(oldError);
                    f.setDown(migrationFailExc);
                } catch (FragmentAuthenticationException authExc) {
                    // TODO open the auth data dialog then try again
                    Throwable oldError = f.getDownError();
                    authExc.addSuppressed(oldError);
                    f.setDown(authExc);
                }
            }
        }
    }

}
