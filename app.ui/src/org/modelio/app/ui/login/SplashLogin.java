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
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.modelio.app.ui.plugin.AppUi;

/**
 * Provides a splash screen with login/password fields.
 * This class is deprecated as Modelio is no longer prompting for a user/pass in the splash screen.
 */
@objid ("a8c21d0a-4721-4eda-ae84-6ebdaf339a70")
@Deprecated
public class SplashLogin extends Splash {
    @objid ("a4daba6e-d0ed-4260-8b54-9e4dc7775ce8")
    private int returnedValue;

    @objid ("90b4bb27-1e83-41b2-832c-72499e33f24f")
    private final String initialUser;

    @objid ("0fb0731c-260a-4ee4-87cd-879464811c3f")
    private final String initialPassword;

    @objid ("a9b28f11-f84e-473b-986f-a04569b7ff4f")
    private final Login login;

    @objid ("fc14d36a-7181-4554-8d5f-a86484f18de1")
    private Text passwordText;

    @objid ("00c0a29b-19e3-4a23-9d70-ccb1dcb626b0")
    private Text userText;

    @objid ("b3fe0c11-8784-4c6b-93be-b7a26d88afed")
    public SplashLogin(Login login, String user, String password) {
        super();
        this.login = login;
        this.initialUser = user;
        this.initialPassword = password;
    }

    @objid ("fbcee8bb-009a-43c4-8221-975d48e9c9d9")
    @Override
    public int open() {
        super.open();
        
        while (!this.shell.isDisposed()) {
            if (!this.shell.getDisplay().readAndDispatch()) {
                this.shell.getDisplay().sleep();
            }
        }
        return this.returnedValue;
        // region.dispose();
    }

    @objid ("8d1b34ae-0a5a-4e9c-8442-a8d88fcc2626")
    @Override
    protected void createControls(Shell shell) {
        super.createControls(shell);
        
        // Textfield for the username
        this.userText = new Text(shell, SWT.BORDER);
        final FormData formData2 = new FormData();
        formData2.top = new FormAttachment(60, 0);
        formData2.left = new FormAttachment(70, 0);
        formData2.right = new FormAttachment(100, -10);
        this.userText.setLayoutData(formData2);
        this.userText.setToolTipText(AppUi.I18N.getString("SplashLogin.User.tooltip"));
        if (this.initialUser != null) {
            this.userText.setText(this.initialUser);
        } else {
            this.userText.setText(System.getProperty("user.name"));
        }
        
        // Label for the user
        final Label userLabel = new Label(shell, SWT.NONE);
        userLabel.setAlignment(SWT.RIGHT);
        final FormData formData = new FormData();
        formData.top = new FormAttachment(this.userText, 3, SWT.TOP);
        formData.bottom = new FormAttachment(this.userText, 0, SWT.BOTTOM);
        formData.right = new FormAttachment(this.userText, -10);
        userLabel.setLayoutData(formData);
        userLabel.setText(AppUi.I18N.getString("SplashLogin.User.label"));
        
        // Text field for the password
        this.passwordText = new Text(shell, SWT.BORDER);
        final FormData formData4 = new FormData();
        formData4.top = new FormAttachment(this.userText, 10);
        // formData.bottom = new FormAttachment(100, -5);
        formData4.left = new FormAttachment(70, 0);
        formData4.right = new FormAttachment(100, -10);
        this.passwordText.setLayoutData(formData4);
        this.passwordText.setEchoChar('*');
        if (this.initialPassword != null) {
            this.passwordText.setText(this.initialPassword);
        }this.passwordText.setToolTipText(AppUi.I18N.getString("SplashLogin.Password.tooltip"));
        
        // Label for the password
        final Label passwordLabel = new Label(shell, SWT.NONE);
        passwordLabel.setAlignment(SWT.RIGHT);
        final FormData formData3 = new FormData();
        formData3.top = new FormAttachment(this.passwordText, 3, SWT.TOP);
        // formData.bottom = new FormAttachment(100, -5);
        // formData3.left = new FormAttachment(65, 0);
        formData3.right = new FormAttachment(this.passwordText, -10);
        passwordLabel.setLayoutData(formData3);
        passwordLabel.setText(AppUi.I18N.getString("SplashLogin.Password.label"));
        
        // Button for login
        final Button loginButton = new Button(shell, SWT.FLAT);
        final FormData formData5 = new FormData();
        formData5.top = new FormAttachment(this.passwordText, 10);
        // formData5.bottom = new FormAttachment(100, -30);
        // formData5.left = new FormAttachment(100, -40);
        formData5.right = new FormAttachment(this.passwordText, 0, SWT.RIGHT);
        loginButton.setLayoutData(formData5);
        loginButton.setText(AppUi.I18N.getString("SplashLogin.Login.label"));
        
        loginButton.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event e) {
        
                SplashLogin.this.login.user = SplashLogin.this.userText.getText();
                SplashLogin.this.login.pass = SplashLogin.this.passwordText.getText();
        
                SplashLogin.this.shell.close();
                SplashLogin.this.returnedValue = SWT.OK;
            }
        });
        // Button for cancel
        final Button cancelButton = new Button(shell, SWT.FLAT);
        final FormData formData6 = new FormData();
        formData6.top = new FormAttachment(this.passwordText, 10);
        // formData5.bottom = new FormAttachment(100, -30);
        // formData5.left = new FormAttachment(100, -40);
        formData6.right = new FormAttachment(loginButton, 0);
        cancelButton.setLayoutData(formData6);
        cancelButton.setText(AppUi.I18N.getString("SplashLogin.Cancel.label"));
        
        cancelButton.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event e) {
        
                SplashLogin.this.login.user = null;
                SplashLogin.this.login.pass = null;
        
                SplashLogin.this.shell.close();
                SplashLogin.this.returnedValue = SWT.CANCEL;
            }
        });
        
        // Adding ability to move shell around
        final Listener l = new Listener() {
            Point origin;
        
            @Override
            public void handleEvent(Event e) {
                switch (e.type) {
                case SWT.MouseDown:
                    this.origin = new Point(e.x, e.y);
                    break;
                case SWT.MouseUp:
                    this.origin = null;
                    break;
                case SWT.MouseMove:
                    if (this.origin != null) {
                        final Point p = SplashLogin.this.shell.getDisplay().map(SplashLogin.this.shell, null, e.x, e.y);
                        SplashLogin.this.shell.setLocation(p.x - this.origin.x, p.y - this.origin.y);
                    }
                    break;
                }
            }
        };
        
        this.shell.addListener(SWT.MouseDown, l);
        this.shell.addListener(SWT.MouseUp, l);
        this.shell.addListener(SWT.MouseMove, l);
    }

}
