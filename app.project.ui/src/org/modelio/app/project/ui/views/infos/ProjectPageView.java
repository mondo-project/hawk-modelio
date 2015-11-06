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
                                    

package org.modelio.app.project.ui.views.infos;

import java.beans.EventHandler;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * Project page view.
 */
@objid ("000814ac-ef92-1fc5-854f-001ec947cd2a")
public class ProjectPageView {
    /**
     * The ID of the view as specified by the extension.
     */
    @objid ("004bb78e-ef9d-1fc5-854f-001ec947cd2a")
    public static final String ID = "org.modelio.app.project.ui.part.projectpage";

    @objid ("005974d2-182b-1fd2-a931-001ec947cd2a")
    private String url;

    @objid ("abb66326-3230-11e2-85c2-001ec947ccaf")
    private Browser browser;

    /**
     * C'tor
     */
    @objid ("00084c92-ef92-1fc5-854f-001ec947cd2a")
    public ProjectPageView() {
    }

    /**
     * initialize the view.
     * <p>
     * Called by the E4 model.
     * @param parent the parent composite
     */
    @objid ("00085a5c-ef92-1fc5-854f-001ec947cd2a")
    @PostConstruct
    public void createControls(final Composite parent) {
        this.browser = new Browser(parent, SWT.NULL);
        this.browser.setText("");
        
        Listener listener = EventHandler.create(Listener.class, this, "onBrowserActivated");
        this.browser.addListener(SWT.Show , listener);
        //this.browser.addListener(SWT.Activate, listener);
    }

    /**
     * Test method.
     * @param args command line arguments
     */
    @objid ("0008c60e-ef92-1fc5-854f-001ec947cd2a")
    public static void main(final String[] args) {
        final Display d = new Display();
        
        Shell shell = new Shell(d);
        shell.setLayout(new FillLayout());
        ProjectPageView view = new ProjectPageView();
        view.setUrl("http://www.modelio.org");
        view.createControls(shell);
        shell.open();
        while (!shell.isDisposed()) {
            if (!d.readAndDispatch()) {
                d.sleep();
            }
        }
        d.dispose();
    }

    @objid ("003ed4b0-b10f-1fc8-b42e-001ec947cd2a")
    @PreDestroy
    private void dispose() {
        this.browser.dispose();
        this.browser = null;
    }

    @objid ("003c4100-6a48-1fcf-b5e2-001ec947cd2a")
    @Focus
    void setFocus() {
        this.browser.setFocus();
    }

    /**
     * Set the HTML browser URL.
     * @param viewUrl the URL to view.
     */
    @objid ("0084d8c0-bad6-1fd0-b5e2-001ec947cd2a")
    public void setUrl(final String viewUrl) {
        this.url = viewUrl;
        
        if (this.browser!=null && !this.browser.isDisposed() && this.browser.isVisible())
            onBrowserActivated();
    }

    @objid ("9da585ce-d6e3-4890-807d-51d60cba2295")
    public String getUrl() {
        return this.url;
    }

    /**
     * @see #createControls(Composite)
     */
    @objid ("53fd68cb-c396-44d1-af30-0f9820dec268")
    public void onBrowserActivated() {
        String browserUrl = this.browser.getUrl();
        if (browserUrl.equals(this.url) ||
                (this.url==null && this.browser.getUrl().isEmpty()))
            return;
        
        if (this.url != null)
            this.browser.setUrl(this.url);
        else
            this.browser.setText("");
    }


// @Optional
// @Inject
// @objid ("00088e0a-ef92-1fc5-854f-001ec947cd2a")
// public void update(final @Named(IServiceConstants.ACTIVE_SELECTION)
// GProject project) {
// if (browser == null)
// return;
//
// if (project == null) {
// this.browser.setText("");
// return;
// }
//
// String url = project.getProperties().getProperty("url");
// if (url != null) {
// this.browser.setUrl(url);
// } else {
// this.browser.setText("");
// }
// }
// @Optional
// @Inject
// @objid("003e8dd4-b10f-1fc8-b42e-001ec947cd2a")
// public void update(final @Named(IServiceConstants.ACTIVE_SELECTION)
// IModelFragment fragment) {
// if (browser == null || fragment == null)
// return;
//
// String url = fragment.getProperties().getProperty("url",
// "http://www.modelio.org/forum/index.html");
// if (url != null)
// this.browser.setUrl(url);
// else
// this.browser.setText("");
// }
}
