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
                                    

package org.modelio.core.help.system;

import java.net.URL;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.help.IContext;
import org.eclipse.help.internal.base.BaseHelpSystem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.modelio.core.help.plugin.CoreHelp;

@objid ("7aafb1f9-8193-4526-ac6f-e8811139860d")
public class ModelioHelpUi {
    @objid ("8236e2ae-04f2-48c2-8fdf-44019cb590ae")
    private static ModelioHelpUi instance;

    @objid ("377f3775-0e99-4112-adf3-c3fdd9edb892")
    private Browser browser;

    @objid ("da85e7dd-6d43-4a06-9849-1d4983270756")
    private Shell shellWindow;

    /**
     * Constructor.
     */
    @objid ("94dcc31c-55ee-461e-8e80-5e858a730079")
    public ModelioHelpUi() {
        instance = this;
    }

    @objid ("cb356d41-1a89-4952-8c75-cc70d8f0f311")
    private Browser getBrowser() {
        Shell parent = Display.getCurrent().getActiveShell();
        
        if (this.shellWindow == null || this.browser.isDisposed()) {
            createGui(Display.getCurrent().getActiveShell());
        } else {
            if (this.shellWindow.getParent() != parent) {
                this.shellWindow.close();
                this.shellWindow = null;
                createGui(parent);
            }
        }
        
        if (this.shellWindow.getMinimized())
            this.shellWindow.setMinimized(false);
        
        this.shellWindow.forceActive();
        return this.browser;
    }

    @objid ("93c20cf9-485d-45c1-b427-56c38e4c688c")
    public static ModelioHelpUi getInstance() {
        return instance;
    }

    /**
     * Displays help.
     */
    @objid ("78b0ff90-b749-4048-8f88-56ec78c2bac3")
    public void displayHelp() {
        URL url = BaseHelpSystem.resolve("../index.jsp", false);
        if (url != null)
            this.getBrowser().setUrl(url.toString());
        else
            CoreHelp.LOG.debug("help resource not found: " + "../index.jsp");
    }

    /**
     * Displays a help resource specified as a url.
     * <ul>
     * <li>a URL in a format that can be returned by {@link org.eclipse.help.IHelpResource#getHref() IHelpResource.getHref()}
     * <li>a URL query in the format format <em>key=value&amp;key=value ...</em> The valid keys are: "tab", "toc", "topic",
     * "contextId". For example, <em>toc="/myplugin/mytoc.xml"&amp;topic="/myplugin/references/myclass.html"</em> is valid.
     * </ul>
     */
    @objid ("99148ccc-ddee-458d-9dcb-f0796ac15217")
    public void displayHelpResource(String href) {
        URL url = BaseHelpSystem.resolve(href, false);
        if (url != null)
            this.getBrowser().setUrl(url.toString());
        else
            CoreHelp.LOG.debug("help resource not found: " + href);
    }

    /**
     * Displays search.
     */
    @objid ("bb1db676-0832-4eb7-ac3c-be3f8913dbcd")
    public void displaySearch() {
        throw new UnsupportedOperationException();
        // search(null);
    }

    /**
     * Displays dynamic help.
     */
    @objid ("1e4af48f-b7ea-44f2-91e3-5bc1ae97c2a3")
    public void displayDynamicHelp() {
        throw new UnsupportedOperationException();
        // IWorkbenchWindow window =
        // PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        // Shell activeShell = getActiveShell();
        // if (window != null && isActiveShell(activeShell, window)) {
        // setIntroStandby();
        //
        // IWorkbenchPage page = window.getActivePage();
        // Control c = activeShell.getDisplay().getFocusControl();
        // if (page != null) {
        // IWorkbenchPart activePart = page.getActivePart();
        // try {
        // IViewPart part = page.showView(HELP_VIEW_ID, null,
        // IWorkbenchPage.VIEW_ACTIVATE);
        // if (part != null) {
        // HelpView view = (HelpView) part;
        // view.showDynamicHelp(activePart, c);
        // }
        // } catch (PartInitException e) {
        // }
        // } else {
        // // check the dialog
        // Object data = activeShell.getData();
        // if (data instanceof TrayDialog) {
        // IContext context = ContextHelpPart.findHelpContext(c);
        // displayContextAsHelpTray(activeShell, context);
        // return;
        // }
        // warnNoOpenPerspective(window);
        // }
        // }
    }

    /**
     * Starts the search.
     */
    @objid ("887ba582-8a5d-4077-a2d6-7f2cd8702f7f")
    public void search(final String expression) {
        throw new UnsupportedOperationException();
        // IWorkbenchWindow window =
        // PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        // Shell activeShell = getActiveShell();
        // if (window != null && isActiveShell(activeShell, window)) {
        // setIntroStandby();
        //
        // IWorkbenchPage page = window.getActivePage();
        // if (page != null) {
        // boolean searchFromBrowser =
        // Platform.getPreferencesService().getBoolean(HelpBasePlugin.PLUGIN_ID,
        // IHelpBaseConstants.P_KEY_SEARCH_FROM_BROWSER, false, null);
        // if (searchFromBrowser) {
        //                    String parameters = "tab=search"; //$NON-NLS-1$
        // if (expression != null) {
        // parameters += '&';
        // parameters += expression;
        // }
        // BaseHelpSystem.getHelpDisplay().displayHelpResource(parameters,
        // false);
        // } else {
        // try {
        // IViewPart part = page.showView(HELP_VIEW_ID);
        // if (part != null) {
        // HelpView view = (HelpView) part;
        // view.startSearch(expression);
        // }
        // } catch (PartInitException e) {
        // }
        // }
        // } else {
        // // check the dialog
        // if (activeShell != null) {
        // Object data = activeShell.getData();
        // if (data instanceof TrayDialog) {
        // displayContextAsHelpTray(activeShell, null);
        // return;
        // } else {
        // // tried to summon help from a non-tray dialog
        // // not supported
        // return;
        // }
        // }
        // warnNoOpenPerspective(window);
        // }
        // }
    }

    @objid ("e3c78a9b-2d42-4590-aabd-9616adb27ad7")
    public static void showIndex() {
        throw new UnsupportedOperationException();
        // HelpView helpView = getHelpView();
        // if (helpView != null) {
        // helpView.showIndex();
        // }
    }

    @objid ("73c5660a-6abc-43dd-b385-32dd16d6b0b9")
    private static void setIntroStandby() {
        throw new UnsupportedOperationException();
        // IIntroManager introMng = PlatformUI.getWorkbench().getIntroManager();
        // IIntroPart intro = introMng.getIntro();
        // if (intro != null && !introMng.isIntroStandby(intro))
        // introMng.setIntroStandby(intro, true);
    }

    /**
     * Displays context-sensitive help for specified context
     * @param context the context to display
     * @param x int positioning information
     * @param y int positioning information
     */
    @objid ("2f6ce950-f717-41b0-94ad-84819a265edb")
    public void displayContext(IContext context, int x, int y) {
        throw new UnsupportedOperationException();
        // displayContext(context, x, y, false);
    }

    @objid ("4789984f-2f3a-4ba0-b89a-d7f9d3654c7a")
    void displayContext(IContext context, int x, int y, boolean noInfopop) {
        throw new UnsupportedOperationException();
        // if (context == null)
        // return;
        // boolean winfopop =
        // Platform.getPreferencesService().getBoolean(HelpBasePlugin.PLUGIN_ID,
        // IHelpBaseConstants.P_KEY_WINDOW_INFOPOP, false, null);
        // boolean dinfopop =
        // Platform.getPreferencesService().getBoolean(HelpBasePlugin.PLUGIN_ID,
        // IHelpBaseConstants.P_KEY_DIALOG_INFOPOP, false, null)
        // || FontUtils.isFontTooLargeForTray();
        //
        // IWorkbenchWindow window =
        // PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        // Shell activeShell = getActiveShell();
        // if (window != null && isActiveShell(activeShell, window)) {
        // IWorkbenchPage page = window.getActivePage();
        // if (page != null) {
        // if (!noInfopop && winfopop) {
        // Control c = window.getShell().getDisplay().getFocusControl();
        // displayContextAsInfopop(context, x, y, c);
        // return;
        // }
        // try {
        // /*
        // * If the context help has no description text and exactly
        // * one topic, go straight to the topic and skip context
        // * help.
        // */
        // String contextText = context.getText();
        // IHelpResource[] topics = context.getRelatedTopics();
        // boolean isSingleChoiceWithoutDescription = contextText == null &&
        // topics.length == 1;
        // String openMode =
        // Platform.getPreferencesService().getString(HelpBasePlugin.PLUGIN_ID,
        // IHelpBaseConstants.P_KEY_HELP_VIEW_OPEN_MODE,
        // IHelpBaseConstants.P_IN_PLACE, null);
        // if (isSingleChoiceWithoutDescription &&
        // IHelpBaseConstants.P_IN_EDITOR.equals(openMode)) {
        // showInWorkbenchBrowser(topics[0].getHref(), true);
        // } else if (isSingleChoiceWithoutDescription &&
        // IHelpBaseConstants.P_IN_BROWSER.equals(openMode)) {
        // BaseHelpSystem.getHelpDisplay().displayHelpResource(topics[0].getHref(),
        // true);
        // } else {
        // IWorkbenchPart activePart = page.getActivePart();
        // Control c = window.getShell().getDisplay().getFocusControl();
        // openingHelpView = true;
        // IViewPart part = page.showView(HELP_VIEW_ID);
        // openingHelpView = false;
        // if (part != null) {
        // HelpView view = (HelpView) part;
        // if (isSingleChoiceWithoutDescription) {
        // view.showHelp(topics[0].getHref());
        // } else {
        // view.displayContext(context, activePart, c);
        // }
        // }
        // }
        // return;
        // } catch (PartInitException e) {
        // // ignore the exception and let
        // // the code default to the context
        // // help dialog
        // }
        // }
        // }
        // // check the dialog
        // if (HelpTray.isAppropriateFor(activeShell) && (!dinfopop ||
        // noInfopop)) {
        // displayContextAsHelpTray(activeShell, context);
        // return;
        // }
        // // we are here either as a fallback or because of the user
        // preferences
        // displayContextAsInfopop(context, x, y, null);
    }

/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.help.AbstractHelpUI#resolve(java.lang.String, boolean)
     */
    @objid ("4946cad6-70a3-4919-b07b-e72a037dc768")
    public URL resolve(String href, boolean documentOnly) {
        return BaseHelpSystem.resolve(href, documentOnly);
    }

    @objid ("29fdd62b-c539-47b0-9631-25d0a257056f")
    public String unresolve(URL url) {
        return BaseHelpSystem.unresolve(url);
    }

    @objid ("e9e5ab45-645d-40dd-8c02-2d9cac971e3f")
    void displayContextAsInfopop(IContext context, int x, int y, Control c) {
        throw new UnsupportedOperationException();
        // if (f1Dialog != null) {
        // f1Dialog.close();
        // }
        //
        // if (context != null) {
        // /*
        // * If the context help has no description text and exactly one
        // * topic, go straight to the topic and skip context help.
        // */
        // IHelpResource[] topics = context.getRelatedTopics();
        // if (context.getText() == null && topics.length == 1) {
        // try {
        // PlatformUI.getWorkbench().getHelpSystem().displayHelpResource(topics[0].getHref());
        // } catch (Exception e) {
        // // should never happen
        // }
        // } else {
        // //f1Dialog = new ContextHelpDialog(context, x, y);
        // //f1Dialog.open();
        //
        // CoreHelp.LOG.debug("Missing ContextHelpDialog");
        // }
        // }
    }

    @objid ("5d036b03-aae2-4e82-99a8-abd822ffb80b")
    private void displayContextAsHelpTray(Shell activeShell, IContext context) {
        throw new UnsupportedOperationException();
        // Control controlInFocus = activeShell.getDisplay().getFocusControl();
        // TrayDialog dialog = (TrayDialog) activeShell.getData();
        //
        // DialogTray tray = dialog.getTray();
        // if (tray == null) {
        // tray = new HelpTray();
        // dialog.openTray(tray);
        // }
        // if (tray instanceof HelpTray) {
        // ReusableHelpPart helpPart = ((HelpTray) tray).getHelpPart();
        // if (context != null) {
        // IHelpResource[] topics = context.getRelatedTopics();
        // if (context.getText() == null && topics.length == 1) {
        // helpPart.showURL(topics[0].getHref());
        // } else {
        // helpPart.showPage(IHelpUIConstants.HV_CONTEXT_HELP_PAGE);
        // helpPart.update(null, context, null, controlInFocus, true);
        // }
        // } else {
        // helpPart.showPage(IHelpUIConstants.HV_FSEARCH_PAGE, true);
        // }
        // helpPart.setFocus();
        // } else {
        // // someone else was occupying the tray; not supported
        // }
    }

    /**
     * Returns <code>true</code> if the context-sensitive help window is currently being displayed, <code>false</code> if not.
     */
    @objid ("692a146f-03f4-4beb-a555-e77f83eff5b3")
    public boolean isContextHelpDisplayed() {
        // if (f1Dialog == null) {
        // return false;
        // }
        // return f1Dialog.isShowing();
        return false;
    }

/*
     * Used to indicate to the HelpView that we are about to pass in a context
     */
    @objid ("9c54b66b-f254-4c08-b922-4ef000bbaa76")
    public static boolean isOpeningHelpView() {
        return false;// return openingHelpView;
    }

    @objid ("42ca401c-8146-4475-a490-6047ff4daf9d")
    private void createGui(Shell parent) {
        this.shellWindow = new Shell(parent, SWT.SHELL_TRIM);
        this.shellWindow.setText(CoreHelp.I18N.getString("HelpWindow.title"));
        this.shellWindow.setLayout(new GridLayout());
        
        this.browser = new Browser(this.shellWindow, SWT.BORDER);
        this.browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        this.shellWindow.addDisposeListener(new DisposeListener() {
        
            @Override
            public void widgetDisposed(DisposeEvent e) {
                ModelioHelpUi.this.browser = null;
                ModelioHelpUi.this.shellWindow = null;
        
            }
        });
        
        this.shellWindow.setSize(800, 600);
        Monitor primary = this.shellWindow.getDisplay().getPrimaryMonitor();
        Rectangle bounds = primary.getBounds();
        Rectangle rect = this.shellWindow.getBounds();
        
        int x = bounds.x + (bounds.width - rect.width) / 2;
        int y = bounds.y + (bounds.height - rect.height) / 2;
        
        this.shellWindow.setLocation(x, y);
        this.shellWindow.open();
    }

}
