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
                                    

package org.modelio.app.ui.welcome;

import java.net.URL;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.help.internal.base.BaseHelpSystem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.help.IWorkbenchHelpSystem;
import org.modelio.app.ui.persp.IModelioUiService;

@objid ("a1b3a124-3052-4d98-910a-c5e700f002e2")
public class WelcomeView {
    @objid ("a1e27c88-6f6d-4589-861c-e4a8b90137d3")
    private static String WELCOME_HREF = "org.modelio.welcome/html/Index.html";

    @objid ("281c1056-98f2-4d70-bd02-1312296d011b")
    private Browser browser;

    @objid ("f61bf743-15b6-42c0-82af-f5d3d7675c99")
    @PostConstruct
    public void createControls(Composite parent, MApplication application, IWorkbenchHelpSystem help) {
        this.browser = new Browser(parent, SWT.BORDER);
        URL url = BaseHelpSystem.resolve(WELCOME_HREF, true);
        this.browser.setUrl(url.toString());
    }

    @objid ("6c6f0cc8-2e65-4b05-961d-684793abfcb6")
    @Focus
    public void onFocus(IWorkbenchHelpSystem help) {
        URL url = BaseHelpSystem.resolve(WELCOME_HREF, true);
        this.browser.setUrl(url.toString());
    }

    @objid ("7cf6fc1f-7918-44a6-8be1-170c05f9b51e")
    public static void setWelcomeHref(String href) {
        WELCOME_HREF = href;
    }

    @objid ("3b3cb234-c9a2-4860-a3a4-98e77992c129")
    @PreDestroy
    public void partClose(final IModelioUiService pm) {
        Display.getCurrent().asyncExec(new Runnable() {
            @Override
            public void run() {
                pm.switchToPerspective(null);
            }
        });
    }

}
