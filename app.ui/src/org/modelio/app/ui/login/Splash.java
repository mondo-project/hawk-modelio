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

import java.net.URL;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.modelio.app.core.ModelioEnv;

/**
 * Provides a splash screen shell.
 */
@objid ("2ef50e57-0513-4a4e-8595-a2494608c10c")
public class Splash {
    @objid ("836e0343-84cc-4bed-baee-afc7dc310b79")
    protected Shell shell;

    @objid ("8cbb171f-4eb7-489e-b2a7-4bcae4a1aefd")
    private Label message;

    @objid ("2fec1ef4-28e3-4554-b5e3-ee823eb1ed33")
    public Splash() {
        this.shell = new Shell(SWT.INHERIT_NONE | SWT.NO_TRIM | SWT.ON_TOP);
        final URL url = FileLocator.find(Platform.getBundle("org.modelio.app.ui"), new Path("splash.png"), null);
        final ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL(url);
        final Image img = imageDescriptor.createImage();
        this.shell.setBackgroundImage(img);
        this.shell.setBackgroundMode(SWT.INHERIT_DEFAULT);
        this.shell.setSize(img.getImageData().width, img.getImageData().height);
        final FormLayout formLayout = new FormLayout();
        this.shell.setLayout(formLayout);
        
        createControls(this.shell);
    }

    @objid ("47fac1f4-7c5f-47e2-a145-06eff0e7b35c")
    public int open() {
        this.setCentered();
        this.shell.open();
        return SWT.OK;
    }

    @objid ("b299db31-c386-443f-a3fc-7a3fbe1b79a7")
    public void close() {
        this.shell.close();
        this.shell = null;
    }

    @objid ("47470b03-4559-433b-8cdd-1db53d0ff89f")
    protected void createControls(Shell shell) {
        // Display Modelio version
        final Label versionLabel = new Label(shell, SWT.NONE);
        versionLabel.setAlignment(SWT.RIGHT);
        versionLabel.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_GRAY));
        FormData formData = new FormData();
        formData.top = new FormAttachment(35, 0);
        formData.left = new FormAttachment(50, 10);
        // formData.right = new FormAttachment(100, -10);
        versionLabel.setLayoutData(formData);
        versionLabel.setText("Version: " + ModelioEnv.MODELIO_VERSION);
        
        message = new Label(shell, SWT.NONE);
        message.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_GRAY));
        // message.setAlignment(SWT.RIGHT);
        
        formData = new FormData();
        formData.top = new FormAttachment(55, 0);
        formData.left = new FormAttachment(50, 10);
        formData.right = new FormAttachment(100, -4);
        message.setLayoutData(formData);
        message.setText("Initializing...");
    }

    @objid ("1abc433f-7a2b-4800-80a7-f84428998616")
    protected void setCentered() {
        // Positioning in the center of the screen.
        final Rectangle bounds = Display.getCurrent().getPrimaryMonitor().getBounds();
        this.shell.setLocation((bounds.width - this.shell.getSize().x) / 2, (bounds.height - this.shell.getSize().y) / 2);
    }

    @objid ("5cef6e5a-cebe-4874-b06d-798afa0b1ed2")
    public void showMessage(String message) {
        this.message.setText(message);
        this.message.redraw();
        while (this.shell.getDisplay().readAndDispatch())
            ;
    }

}
