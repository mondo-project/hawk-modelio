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
                                    

package org.modelio.app.ui.login;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

@objid ("00448ab8-cc35-1ff2-a7f4-001ec947cd2a")
@Deprecated
public class Login {
    @objid ("48dd56f1-2d6b-4898-9de6-93c937a1cead")
    public String user;

    @objid ("ddd2de79-98d6-4a18-b1e3-ee0d2168a774")
    public String pass;

    @objid ("0048ed2e-cc35-1ff2-a7f4-001ec947cd2a")
    public void login(final IEclipseContext context, String user, String pass) {
        final Shell shell = new Shell(SWT.INHERIT_NONE | SWT.NO_TRIM | SWT.ON_TOP);
        
        final SplashLogin dialog = new SplashLogin(this, user, pass);
        
        if (dialog.open() != SWT.OK) {
            System.exit(0);
        }
    }

    @objid ("b0bb5e87-ae66-47d9-8bac-a03aaa09ac3e")
    public String getUser() {
        return this.user;
    }

    @objid ("91da9f41-8e5b-4b0a-a0f2-9d26f32fa6cf")
    public String getPass() {
        return this.pass;
    }

}
