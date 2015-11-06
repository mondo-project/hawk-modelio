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
                                    

//------------------------------------------------------------------------------
// All rights reserved. This program and the accompanying materials
// are made available under the terms of the Eclipse Public License v1.0
// which accompanies this distribution, and is available at
// http://www.eclipse.org/legal/epl-v10.html
//
// Contributors:
// IBM Corporation - initial implementation
//------------------------------------------------------------------------------
package org.modelio.edition.html.view;

import java.io.File;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.HTMLTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.PlatformUI;
import org.modelio.edition.html.epfcommon.IHTMLFormatter;
import org.modelio.edition.html.epfcommon.html.DefaultHTMLFormatter;
import org.modelio.edition.html.epfcommon.utils.FileUtil;
import org.modelio.edition.html.epfcommon.utils.XMLUtil;
import org.modelio.edition.html.epfcommon.xml.XSLTProcessor;
import org.modelio.edition.html.plugin.HtmlTextImages;
import org.modelio.edition.html.plugin.HtmlTextPlugin;
import org.modelio.edition.html.plugin.HtmlTextResources;
import org.modelio.edition.html.view.actions.AddColumnAction;
import org.modelio.edition.html.view.actions.AddRowAction;
import org.modelio.edition.html.view.actions.CopyAction;
import org.modelio.edition.html.view.actions.CutAction;
import org.modelio.edition.html.view.actions.DeleteLastColumnAction;
import org.modelio.edition.html.view.actions.DeleteLastRowAction;
import org.modelio.edition.html.view.actions.FindReplaceAction;
import org.modelio.edition.html.view.actions.PasteAction;
import org.modelio.edition.html.view.actions.PastePlainTextAction;
import org.modelio.log.writers.PluginLogger;

/**
 * The default rich text control implementation.
 * <p>
 * The default rich text editor uses XHTML as the underlying markup language for
 * the rich text content. It is implemented using a SWT <code>Browser</code>
 * control and DHTML (HTML, CSS and JavaScript).
 * 
 * @author Kelvin Low
 * @author Jeff Hardy
 * @author Shi Jin
 * @since 1.0
 */
@objid ("3af85068-4a65-4ea6-90af-5c4488472ce6")
public class RichText implements IRichText {
// Encoded single quote. All single quotes need to be specially encoded to
// avoid JavaScript error when calling Browse.executeCommand().
    @objid ("d6734d60-c5a8-4571-8312-7addb83f8b13")
    private static final String ENCODED_SINGLE_QUOTE = "%sq%"; // $NON-NLS-1$

    @objid ("7093246e-c65f-4daa-a9a3-b683f24d622f")
    private static final String ENCODED_NEWLINE = "%EOL%"; // $NON-NLS-1$

    @objid ("784ac294-189e-4a37-8cde-13f8c84d1ded")
    protected static final String STATUS_PREFIX = "$$$"; // $NON-NLS-1$

    @objid ("bee693a0-fb4d-450f-9474-39b8aac66f70")
    protected static final int STATUS_PREFIX_LENGTH = STATUS_PREFIX.length();

    @objid ("942aca3f-79da-47f0-9a69-bf6734ac379a")
    protected static final int STATUS_NOP = 0;

    @objid ("cb92115a-e10f-4bad-9bcb-e515c436ecfa")
    protected static final int STATUS_INITIALIZED = 1;

    @objid ("828642bc-361e-4298-bdad-ba28d498d7e4")
    protected static final int STATUS_MODIFIED = 2;

    @objid ("66ed4924-35a8-4099-85e1-f1b073ccf307")
    protected static final int STATUS_GET_TEXT = 3;

    @objid ("9a75e012-8485-441e-8e15-8de8b4ddcbd4")
    protected static final int STATUS_KEY_DOWN = 4;

    @objid ("75915783-c957-42f0-ad13-282cf2d452d2")
    protected static final int STATUS_KEY_UP = 5;

    @objid ("f0781cbd-b63e-4a23-9505-2c23a8d9c965")
    protected static final int STATUS_SELECT_TEXT = 6;

    @objid ("9776213f-2970-47d8-8b7e-8687f3910853")
    protected static final int STATUS_SELECT_CONTROL = 7;

    @objid ("bbf35d26-caf0-4901-8ae5-75f179a4d073")
    protected static final int STATUS_SELECT_NONE = 8;

    @objid ("fafc0d2b-98c0-43e8-9031-53762052c683")
    protected static final int STATUS_EXEC_CMD = 9;

    @objid ("5f1ed905-8767-4e22-a538-2e81bf6575d8")
    protected static final int STATUS_REFORMAT_LINKS = 10;

    @objid ("70f6eeb9-3cee-4b22-aa42-81e23a22f1a6")
    protected static final int STATUS_FOCUS_GOT = 11;

    @objid ("8009f0bd-e962-4003-a1f3-ab4842a609e3")
    protected static final int STATUS_FOCUS_LOST = 12;

    @objid ("895dc44e-2f1c-4e4a-b777-8903911c0105")
    protected static final int STATUS_INITIALIZATION_FAILED = 13;

// The default base path used for resolving links (<href>, <img>, etc.)
    @objid ("eb78b333-c01f-4cf0-b5e8-8855108afad4")
    private static final String DEFAULT_BASE_PATH = System
			.getProperty("user.home") //$NON-NLS-1$
			+ System.getProperty("file.separator") + "rte"; // $NON-NLS-1$ //$NON-NLS-2$

// If true, log debugging info.
    @objid ("d9f81078-7e35-43c3-8181-3be0c0e6fd88")
    protected boolean debug;

// The folder that contains the supporting CSS and JavaScript files
    @objid ("6e8c360b-e165-4fae-b573-1400bec587c7")
    protected String rteFolder;

// The URL that points to the supporting CSS and JavaScript files.
    @objid ("81b14003-c9a9-422f-a7ae-75f9b8b81731")
    protected String rteURL;

// The base path used for resolving links (<href>, <img>, etc.)
    @objid ("0574be28-0c9c-4e73-8b56-7dd7acf2b6b7")
    protected String basePath;

// The DHTML initialization flag.
    @objid ("5f86ca10-ebbd-4dca-ab27-42421d0202a7")
    protected boolean initialized;

// The initial focus.
    @objid ("74a17077-bead-47f8-ab15-8efce0b62af3")
    protected boolean initializedWithFocus;

// The control's initial text. This is used to cache the HTML source passed
// in via setText()while the DHTML initialization is happening.
    @objid ("cfbe9bbb-8d9f-489c-a0ce-959af5864081")
    protected String initialText;

// The control's current text.
    @objid ("20e4da1e-e36c-48d2-ae20-8c1c3fb4218a")
    protected String currentText = ""; // $NON-NLS-1$

    @objid ("2e7c5d7d-ac8b-4fdb-8948-0466e86653ef")
    private String currentRawText = ""; // $NON-NLS-1$

// The control's editable flag.
    @objid ("0691f3f7-6d5e-4756-89cd-53c63b294851")
    protected boolean editable = true;

// The control's modification flag.
    @objid ("083c766d-80bb-49bc-9984-3b8aa05a9296")
    protected boolean modified;

// The control's text and object selection flag.
    @objid ("032a6e70-83b7-4698-9663-9328f21b0851")
    protected boolean hasSelection;

// JavaScript command execution status code.
    @objid ("68e2e7ff-277a-43f0-bece-92bc530c969e")
    protected int status = 0;

// The control's edit flag.
    @objid ("c6887492-bbd2-4f60-9def-949e8ccd4831")
    protected boolean notifyingModifyListeners = false;

// The controls's focus flag.
    @objid ("1675d598-6a01-425f-80d6-cf8154d5470a")
    protected boolean hasFocus = false;

// The controls's processing JavaScript event flag.
    @objid ("d7fc2529-091b-4944-93d8-9a2c193a6eb9")
    protected boolean processingJSEvent = false;

// The controls's processing MODIFIED JavaScript event flag.
    @objid ("eafd830c-acbe-4d05-8544-1a25ba2eebcd")
    protected boolean checkingModifyEvent = false;

// The control's IE flag
    @objid ("4136bec1-8278-4048-b01b-fed475ff13d2")
    protected boolean isIE = false;

    /**
     * A event type indicate control has been initialized
     */
    @objid ("c673ec9f-6386-49e2-bb63-7a9453a38cee")
    public static final int RICH_TEXT_INITIALIZED_WIN32 = 98979695;

    /**
     * A event type indicate control has been initialized
     */
    @objid ("7c737b22-52a5-40a0-8e25-d171d98465d9")
    public static final int RICH_TEXT_INITIALIZED_LINUX = 98979694;

    @objid ("4f8ba37a-3062-4464-8350-43075f55d166")
    protected static final int STATUS_SELECT_TABLE = 51;

// The table selection flag
    @objid ("01bf8ce4-e8f0-41cc-ab8a-c3bc3257a64d")
    protected boolean tableSelection;

// The plug-in logger.
    @objid ("6a1f6822-98b5-466a-9b24-68935819f809")
    protected PluginLogger logger;

// The underlying SWT Browser used for loading the JavaScript/DHTML editor.
    @objid ("a285826a-de0a-4660-875a-9f6ba9f4cdb0")
    protected Browser editor;

// The underlying OleControlSite for the SWT Browser (Win32 only).
    @objid ("1aca4d0d-3f6a-429a-934d-dd6c0df10215")
    protected Control editorControl;

// The base URL of the rich text control whose content was last
// copied to the clipboard.
    @objid ("bd9deccf-63f7-4d88-9a90-510645084108")
    protected static URL copyURL;

// The context menu associated with this control.
    @objid ("eab59028-220d-45b5-8e14-d9e4f9d1b95b")
    protected Menu contextMenu;

// The control's text selection
    @objid ("a7154d5d-0a15-4468-992d-765fc427c39e")
    protected RichTextSelection richTextSelection = new RichTextSelection();

// The HTML source formatter.
    @objid ("d42a40a8-dd7c-4cae-bdcc-f0e1452c495d")
    protected IHTMLFormatter htmlGetFormatter;

// The SWT event listeners.
    @objid ("c3fdd5d1-c382-4c4c-a903-2699e9ab65de")
    protected Map<Listener, RichTextListener> listeners;

// The modify listeners.
    @objid ("1b6f7d64-81d9-4497-a148-4c38b494c3d8")
    protected List<ModifyListener> modifyListeners;

// The control's find/replace text action
    @objid ("1fb028dc-1a39-46ee-89e9-938de7c42c7d")
    protected FindReplaceAction findReplaceAction;

    @objid ("15796fc4-3bcf-41fd-a625-c3588bc96d43")
    private StatusTextListener statusTextListener;

    @objid ("46290f49-8377-430d-bd37-d0e1161f5dc8")
    private SendFocusEventFunction jsHook1;

    @objid ("57653d79-16a3-433f-a080-47d15da7b029")
    private SendFocusEventFunction jsHook2;

    @objid ("1332d180-09dc-4570-9095-dd6ea78606f9")
    private DefaultHTMLFormatter htmlSetFormatter;

    /**
     * Creates a new instance.
     * @param parent the parent composite
     * @param style the style for this control
     * @param basePath the path used for resolving links
     */
    @objid ("094fb71c-a1c1-4e77-9422-fdb30544b106")
    public RichText(Composite parent, int style, String basePath) {
        //this.debug = RichTextPlugin.getDefault().isDebugging();
        this.logger = HtmlTextPlugin.getDefault().getLogger();
        this.debug = HtmlTextPlugin.getDefault().isDebugging();
        this.findReplaceAction = new FindReplaceAction(this);
        this.rteFolder = HtmlTextPlugin.getDefault().getInstallPath() + "rte/"; //$NON-NLS-1$        
        this.rteURL = XMLUtil.escape("file://" + this.rteFolder); //$NON-NLS-1$
        setBasePath(basePath);
        
        try {
            boolean enableMozilla = false;
            String enableMozillaProperty = System.getProperty("rte.enable.mozilla");
            if (enableMozillaProperty != null) {
                enableMozilla = Boolean.valueOf(enableMozillaProperty);
            }
            if (enableMozilla) {
                this.editor = new Browser(parent, SWT.MOZILLA);
            } else {
                this.editor = new Browser(parent, SWT.NONE);
            }
            if (this.debug) {
                printDebugMessage("RichText", "basePath=" + basePath); //$NON-NLS-1$ //$NON-NLS-2$
            }
            this.editor.setLayoutData(new GridData(GridData.FILL_BOTH));
            this.editor.setData(PROPERTY_NAME, this);
            init(parent, style);
        } catch (Exception e) {
            this.editor = null;
            String msg = "Failed to create RichText with basePath=" + basePath; //$NON-NLS-1$
            this.logger.error(msg, e);
            this.logger.error(e);
        }
        this.tableSelection = false;
    }

    /**
     * Creates a new instance.
     * @param parent the parent composite
     * @param style the style for this control
     */
    @objid ("b3bc5e5b-9e17-447d-a66b-e4cf72fd6b02")
    public RichText(Composite parent, int style) {
        this(parent, style, null);
    }

    /**
     * Sets the base path for resolving links.
     */
    @objid ("68649f73-70bc-4321-992e-793900d9a947")
    protected void setBasePath(String path) {
        if (path != null && path.length() > 0) {
            if (path.startsWith(FileUtil.UNC_PATH_PREFIX)) {
                this.basePath = FileUtil.UNC_PATH_PREFIX
                        + FileUtil.appendSeparator(path.substring(
                                FileUtil.UNC_PATH_PREFIX_LENGTH).replace('\\',
                                '/'), "/"); //$NON-NLS-1$
            } else {
                this.basePath = FileUtil.appendSeparator(path).replace('\\', '/');
            }
        } else {
            this.basePath = FileUtil.appendSeparator(DEFAULT_BASE_PATH).replace(
                    '\\', '/');
        }
    }

    /**
     * Initializes this control.
     * @param parent the parent composite
     * @param style the style for this control
     * @throws java.lang.Exception when an error has occurred while initialzing this control
     */
    @objid ("fa3083fd-dddd-4a19-871d-ac902c1cdf16")
    protected void init(Composite parent, int style) throws Exception {
        boolean ok = false;
        try {
            addStatusTextListener();
            if (this.debug) {
                printDebugMessage("init", "added status text listener"); //$NON-NLS-1$ //$NON-NLS-2$
            }
        
            String editorHTML = generateEditorHTML();
            if (this.debug) {
                printDebugMessage("init", "generated editor HTML"); //$NON-NLS-1$ //$NON-NLS-2$
                printDebugMessage("init", "HTML="+editorHTML); //$NON-NLS-1$ //$NON-NLS-2$
            }
        
            // Set Browser content
            if (!this.editor.setText(editorHTML))
                getLogger().error("Failed setting browser content to:\n"+editorHTML);
            else if (this.debug) {
                printDebugMessage("init", "Browser content set, loading should be in progress."); //$NON-NLS-1$ //$NON-NLS-2$
            }
            
            /*String testUrl = "/home/cmarin/work/phoenix/work/eclipse/edition.richtext/rte/cbrte/examples/demo.htm";
            if (!this.editor.setUrl(testUrl)) {
                getLogger().error("Failed setting browser content to:\n"+testUrl);
            } else if (this.debug) {
                printDebugMessage("init", "Browser url set, loading should be in progress."); //$NON-NLS-1$ //$NON-NLS-2$
            }*/
            
            this.editor.addProgressListener(new ProgressListener() {
                
                @Override
                public void completed(ProgressEvent event) {
                    if (RichText.this.debug) {
                        printDebugMessage("init", "HTML browser finished loading."); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                }
                
                @Override
                public void changed(ProgressEvent event) {
                    // ignore
                }
            });
            
            addResizeListener();
            
        
            this.contextMenu = new Menu(parent.getShell(), SWT.POP_UP);
            this.editor.setMenu(this.contextMenu);
            fillContextMenu(this.contextMenu);
            if (this.debug) {
                printDebugMessage("init", "added context menu"); //$NON-NLS-1$ //$NON-NLS-2$
            }
        
            this.jsHook1 = new SendFocusEventFunction(this.editor, "sendFocusGot", SWT.FocusIn);
            this.jsHook2 = new SendFocusEventFunction(this.editor, "sendFocusLost", SWT.FocusOut);
            
            addListeners();
            if (this.debug) {
                printDebugMessage("init", "added listeners"); //$NON-NLS-1$ //$NON-NLS-2$
            }
        
            this.htmlGetFormatter = new DefaultHTMLFormatter(132, false, 2);
            this.htmlSetFormatter = new DefaultHTMLFormatter(132, true, 4);
            
            if (this.debug) {
                printDebugMessage("init", "instantiated HTMLFormatter"); //$NON-NLS-1$ //$NON-NLS-2$
            }
            ok = true;
        } finally {
            if (!ok) {
                this.editor = null;
                dispose();
            }
        }
    }

    @objid ("1dd12999-4b3c-41ac-a787-e0f7312115a9")
    private void addResizeListener() {
        final Browser browser = this.editor;
        browser.addControlListener(new ControlAdapter() {
            @Override
            public void controlResized(ControlEvent e) {
                // Use Javascript to set the browser width and height
                browser.execute("document.getElementById('rte').style.width= "
                                + (browser.getSize().x - 2) + ";");
                browser.execute("document.getElementById('rte').style.height= "
                                + (browser.getSize().y - 2) + ";");
            }
        });
    }

    /**
     * Returns this rich text control.
     * @return this rich text control
     */
    @objid ("4abcc105-b948-48e8-bf94-65563b9a5cde")
    @Override
    public Control getControl() {
        return this.editor;
    }

    /**
     * Sets the layout data.
     * @param layoutData the layout data to set
     */
    @objid ("0aefeab2-4fb7-4a3c-bc48-b78588e0d858")
    @Override
    public void setLayoutData(Object layoutData) {
        if (this.editor != null) {
            this.editor.setLayoutData(layoutData);
        }
    }

    /**
     * Returns the layout data.
     * @return this control's layout data
     */
    @objid ("4b765a1a-6051-4adb-bb6e-4168593f71e3")
    @Override
    public Object getLayoutData() {
        if (this.editor != null) {
            return this.editor.getLayoutData();
        }
        return null;
    }

    /**
     * Sets focus to this control.
     */
    @objid ("aed5da3a-c6d7-4565-bd66-8eafbaa12129")
    @Override
    public void setFocus() {
        if (this.debug) {
            printDebugMessage("setFocus, editable=" + this.editable); //$NON-NLS-1$
        }
        if (this.editor != null) {
            if (this.initialized) {
                if (!this.editor.isFocusControl()) {
                    if (!Platform.getOS().equals("win32")) { //$NON-NLS-1$
                        // Workaround for Mozilla and Firefox rich text editor focus
                        // issue.
                        this.editor.setFocus();
                    }
                    executeCommand(IRichTextCommands.SET_FOCUS);
                }
                this.hasFocus = true;
            } else {
                this.initializedWithFocus = true;
            }
        }
    }

    /**
     * Tells the control it does not have focus.
     */
    @objid ("256d8925-df54-4c56-91d0-087329396500")
    @Override
    public void setBlur() {
        if (this.debug) {
            printDebugMessage("setBlur, editable=" + this.editable); //$NON-NLS-1$
        }
        if (this.editor != null) {
            if (this.initialized) {
                this.hasFocus = false;
            } else {
                this.initializedWithFocus = false;
            }
        }
    }

    /**
     * Checks whether this control has focus.
     * @return <code>true</code> if this control has the user-interface focus
     */
    @objid ("65361ce1-ce31-45f2-aaa4-ad623d15ffda")
    @Override
    public boolean hasFocus() {
        if (this.editor != null) {
            return this.hasFocus;
        }
        return false;
    }

    /**
     * Returns the base path used for resolving text and image links.
     * @return the base path used for resolving links in this control
     */
    @objid ("cafc12af-6f4f-4b14-bf50-339c3a6491f9")
    @Override
    public String getBasePath() {
        return this.basePath;
    }

    /**
     * Returns the base URL of the rich text control whose content was last
     * copied to the clipboard.
     * @return the base URL of a rich text control
     */
    @objid ("b797f0b5-f80e-4d83-8339-781baaa67f86")
    @Override
    public URL getCopyURL() {
        return copyURL;
    }

    /**
     * Sets the base URL of the rich text control whose content was last copied
     * to the clipboard.
     */
    @objid ("90e07550-3054-47a7-a520-747ea6196c44")
    @Override
    public void setCopyURL() {
        try {
            copyURL = new File(this.basePath).toURL();
        } catch (Exception e) {
            copyURL = null;
        }
    }

    /**
     * Returns the editable state.
     * @return <code>true</code> if the content is editable
     */
    @objid ("eefe6a0d-100d-4217-a627-892494c2ad5a")
    @Override
    public boolean getEditable() {
        return this.editable;
    }

    /**
     * Sets the editable state.
     * @param editable the editable state
     */
    @objid ("52d785f3-2297-46c3-ab3c-bfcbeb1ef156")
    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
        if (this.initialized) {
            executeCommand(IRichTextCommands.SET_EDITABLE, "" + editable); //$NON-NLS-1$
        }
    }

    /**
     * Checks whether the content has been modified.
     * @return <code>true</code> if the content has been modified
     */
    @objid ("bd37dd88-9061-49ab-99a1-64c526dfbefa")
    @Override
    public boolean getModified() {
        return this.modified;
    }

    /**
     * Sets the modified state.
     * @param modified the modified state
     */
    @objid ("55936254-581a-4589-8777-aa75a2ea405d")
    @Override
    public void setModified(boolean modified) {
        this.modified = modified;
    }

    /**
     * Returns the rich text content.
     * @return the rich text content formatted in a markup language
     */
    @objid ("e02bcaed-0f22-4fb7-a01c-9dcaba3b5549")
    @Override
    public String getText() {
        if (this.editor != null && this.initialized) {
            try {
                executeCommand(IRichTextCommands.GET_TEXT);
                if (this.currentText == null) {
                    this.currentText = ""; //$NON-NLS-1$
                }
        
                if (this.debug) {
                    //printDebugMessage("getText", "text=", this.currentText); //$NON-NLS-1$ //$NON-NLS-2$
                }
                return this.currentText;
            } catch (Exception e) {
                this.logger.error(e);
            }
        }
        return ""; //$NON-NLS-1$
    }

    /**
     * Format the given text using the given formatter.
     * <p>
     * Log the errors and return the initial text on failure unless the formatter
     * is configured to force the output.
     * @param text the text to format
     * @param formatter the formatter to use
     * @return the formatted text.
     */
    @objid ("61cb8b39-42aa-4a3f-94f7-527312605393")
    protected String formatHTML(String text, IHTMLFormatter formatter) {
        String formattedText;
        
        try {
            // Call JTidy to format the source to XHTML.
            formattedText = formatter.formatHTML(text);
            if (formatter.getLastErrorStr() != null) {
                this.logger.warning("RichText.formatHtml(): Failed formatting text:");
                this.logger.warning(text);
                this.logger.warning("Formatting error:");
                this.logger.warning(formatter.getLastErrorStr());
            } else {
                return formattedText;
            }
        } catch (UnsupportedEncodingException e) {
            this.logger.warning("RichText.formatHtml(): Failed formatting text:");
            this.logger.warning(text);
            this.logger.warning("Formatting error:");
            this.logger.warning(e);
        }
        return text;
    }

    /**
     * Sets the rich text content.
     * @param text the rich text content formatted in a markup language
     */
    @objid ("b66bfdf7-efe1-4762-9caf-dd3366e82bb4")
    @Override
    public void setText(String text) {
        setText(text, this.htmlSetFormatter);
    }

    /**
     * Restores the rich text content back to the initial value.
     */
    @objid ("45051b30-2b9e-4a0a-b051-f2d5d056d025")
    @Override
    public void restoreText() {
        setText(this.initialText);
        this.modified = false;
    }

    /**
     * Returns the currently selected text.
     * @return the selected text or <code>""</code> if there is no
     * hasSelection
     */
    @objid ("6cc8321f-6479-4532-b03f-8d5b6acf5e17")
    public String getSelectedText() {
        // executeCommand(RichTextCommand.GET_SELECTED_TEXT);
        return this.richTextSelection.getText();
    }

/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.epf.richtext.IRichText#getSelected()
     */
    @objid ("c35aee30-e9cb-490c-a590-df1d6c447dac")
    @Override
    public RichTextSelection getSelected() {
        return this.richTextSelection;
    }

    /**
     * Returns an application specific property value.
     * @param key the name of the property
     * @return the value of the property or <code>null</code> if it has not
     * been set
     */
    @objid ("b88292ee-2dae-4cf1-974c-1f2db040a672")
    @Override
    public Object getData(String key) {
        if (this.editor != null) {
            return this.editor.getData(key);
        }
        return null;
    }

    /**
     * Sets an application specific property name and value.
     * @param key the name of the property
     * @param value the property value
     */
    @objid ("210dd46c-a445-48f5-a248-4853744fc3c7")
    @Override
    public void setData(String key, Object value) {
        if (this.editor != null) {
            this.editor.setData(key, value);
        }
    }

    /**
     * Executes the given JavaScript.
     * @param script the JavaScript to execute
     * @return a status code returned by the executed script
     */
    @objid ("cf30b595-2665-4093-9188-58ba65a7e9f9")
    protected int execute(final String script) {
        this.status = 0;
        if (this.editor != null && script != null && script.length() > 0) {
            try {
                if (!this.isIE && this.processingJSEvent) {
                    Display.getCurrent().asyncExec(new Runnable() {
                        @Override
                        public void run() {
                            if (!isDisposed()) {
                                RichText.this.editor.execute(script);
                                if (!Platform.getOS().equals(Platform.OS_WIN32)) {
                                    if (script.startsWith(IRichTextCommands.SET_TEXT)) {
                                        notifyListeners(RichText.RICH_TEXT_INITIALIZED_LINUX, new Event());
                                    }
                                }
                            }
                        }
                    });
                } else {
                    this.editor.execute(script);
                }
                if (this.debug) {
                    printDebugMessage("execute", script); //$NON-NLS-1$                
                }
            } catch (Exception e) {
                String msg = "Failed to execute " + script; //$NON-NLS-1$
                this.logger.error(msg, e);
                this.logger.error(e);
                if (this.debug) {
                    printDebugMessage("execute", msg); //$NON-NLS-1$
                }
            }
        }
        return this.status;
    }

    /**
     * Executes the given rich text command. The supported command strings are
     * defined in <code>RichTextCommand<code>.
     * @param    command        a rich text command string.
     * @return a status code returned by the executed command
     */
    @objid ("2da63c77-a5ec-4d6c-98d0-15eeaad43869")
    @Override
    public int executeCommand(String command) {
        this.status = 0;
        if (command != null && command.equals(IRichTextCommands.CLEAR_CONTENT)) {
            String oldInitialText = this.initialText;
            setText(""); //$NON-NLS-1$
            this.initialText = oldInitialText;
            this.status = 1;
            this.modified = true;
            notifyModifyListeners();
        } else {
            this.status = execute(command + "();"); //$NON-NLS-1$
        }
        return this.status;
    }

    /**
     * Executes the given rich text command with a single parameter. The
     * supported command strings are defined in <code>RichTextCommand<code>.
     * @param    command        a rich text command string
     * @param    param        a parameter for the command or <code>null</code>
     * @return a status code returned by the executed command
     */
    @objid ("92d55cb1-c009-4e69-93f9-a1321b69e346")
    @Override
    public int executeCommand(String command, String param) {
        if (param == null) {
            return executeCommand(command);
        }
        return execute(command + "('" + formatText(param) + "');"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Executes the given rich text command with an array of parameters. The
     * supported command strings are defined in <code>RichTextCommand<code>.
     * @param    command        a rich text command string
     * @param    params        an array of parameters for the command or <code>null</code>
     * @return a status code returned by the executed command
     */
    @objid ("8fd3eb23-efa3-43f5-b51a-f8588bce0f25")
    @Override
    public int executeCommand(String command, String[] params) {
        if (params == null || params.length == 0) {
            return executeCommand(command);
        }
        StringBuffer sb = new StringBuffer();
        int paramsLength = params.length;
        for (int i = 0; i < paramsLength; i++) {
            sb.append('\'').append(formatText(params[i])).append('\'');
            if (i < paramsLength - 1) {
                sb.append(',');
            }
        }
        String param = sb.toString();
        return execute(command + "(" + param + ");"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Disposes the operating system resources allocated by the control.
     */
    @objid ("d0264516-2220-44c7-a06d-c296ff6c872f")
    @Override
    public void dispose() {
        if (this.contextMenu != null && !this.contextMenu.isDisposed()) {
            this.contextMenu.dispose();
            this.contextMenu = null;
        }
        if (this.listeners != null) {
            this.listeners.clear();
            this.listeners = null;
        }
        if (this.modifyListeners != null) {
            this.modifyListeners.clear();
            this.modifyListeners = null;
        }
        if (this.htmlGetFormatter != null) {
            this.htmlGetFormatter = null;
        }
        if (this.findReplaceAction != null) {
            this.findReplaceAction.dispose();
            this.findReplaceAction = null;
        }
        
        this.jsHook1.dispose();
        this.jsHook2.dispose();
        this.editor.removeStatusTextListener(this.statusTextListener);
    }

    /**
     * Checks whether this control has been disposed.
     * @return <code>true</code> if this control is disposed successfully
     */
    @objid ("c0217c5b-2a95-4f36-a98d-0820f3465e1a")
    @Override
    public boolean isDisposed() {
        return this.editor.isDisposed();
    }

    /**
     * Returns the modify listeners attached to this control.
     * @return an iterator for retrieving the modify listeners
     */
    @objid ("9f180ce9-4b3c-4135-bc5f-1014d2a6a71d")
    @Override
    public Iterator<ModifyListener> getModifyListeners() {
        return this.modifyListeners.iterator();
    }

    /**
     * Adds a listener to the collection of listeners who will be notified when
     * keys are pressed and released within this control.
     * @param listener the listener which should be notified
     */
    @objid ("71ba5734-d526-438d-b914-7b1fa75fa300")
    @Override
    public void addKeyListener(KeyListener listener) {
        if (this.editor != null) {
            this.editor.addKeyListener(listener);
        }
    }

    /**
     * Removes a listener from the collection of listeners who will be notified
     * when keys are pressed and released within this control.
     * @param listener the listener which should no longer be notified
     */
    @objid ("ab6e4942-0b5e-4de1-97e0-b4f98c87fefc")
    @Override
    public void removeKeyListener(KeyListener listener) {
        if (this.editor != null) {
            this.editor.removeKeyListener(listener);
        }
    }

    /**
     * Adds a listener to the collection of listeners who will be notified when
     * the content of this control is modified.
     * @param listener the listener which should be notified
     */
    @objid ("7afe9017-0b5c-40b8-8084-a580cfbf1c91")
    @Override
    public void addModifyListener(ModifyListener listener) {
        if (this.editor != null && listener != null
                && !this.modifyListeners.contains(listener)) {
            this.modifyListeners.add(listener);
        }
    }

    /**
     * Removes a listener from the collection of listeners who will be notified
     * when the content of this control is modified.
     * @param listener the listener which should no longer be notified
     */
    @objid ("ec7814c1-54da-4e77-982c-51e2fefc8e87")
    @Override
    public void removeModifyListener(ModifyListener listener) {
        if (this.editor != null && listener != null
                && this.modifyListeners.contains(listener)) {
            this.modifyListeners.remove(listener);
        }
    }

    /**
     * Adds the listener to the collection of listeners who will be notifed when
     * this control is disposed.
     * @param listener the listener which should be notified
     */
    @objid ("780c3b2e-70d1-407d-a0fd-96f426b8eda1")
    @Override
    public void addDisposeListener(DisposeListener listener) {
        if (this.editor != null) {
            this.editor.addDisposeListener(listener);
        }
    }

    /**
     * Removes a listener from the collection of listeners who will be notified
     * when this control is disposed.
     * @param listener the listener which should no longer be notified
     */
    @objid ("9b2a7280-9c54-4d5e-b80a-24deb6cc697e")
    @Override
    public void removeDisposeListener(DisposeListener listener) {
        if (this.editor != null) {
            this.editor.removeDisposeListener(listener);
        }
    }

    /**
     * Adds a listener to the collection of listeners who will be notified when
     * help events are generated for this control.
     * @param listener the listener which should be notified
     */
    @objid ("cc33bc2e-f00e-4175-95ff-4eaf19f873ff")
    @Override
    public void addHelpListener(HelpListener listener) {
        if (this.editor != null) {
            this.editor.addHelpListener(listener);
        }
    }

    /**
     * Removes a listener from the collection of listeners who will be notified
     * when help events are generated for this control.
     * @param listener the listener which should no longer be notified
     */
    @objid ("ab78edc8-99d8-4ffa-9941-fa496d1c9edf")
    @Override
    public void removeHelpListener(HelpListener listener) {
        if (this.editor != null) {
            this.editor.removeHelpListener(listener);
        }
    }

    /**
     * Adds the listener to the collection of listeners who will be notifed when
     * an event of the given type occurs within this control.
     * @param eventType the type of event to listen for
     * @param listener the listener which should be notified when the event occurs
     */
    @objid ("2351ddab-3809-4942-b0a3-3cec7f69c314")
    @Override
    public void addListener(int eventType, Listener listener) {
        if (this.editor != null && !this.listeners.containsKey(listener)) {
            if (eventType != SWT.Selection) {
                if (this.editorControl == null
                        || (eventType != SWT.Activate
                                && eventType != SWT.Deactivate
                                && eventType != SWT.FocusIn && eventType != SWT.FocusOut)) {
                    this.editor.addListener(eventType, listener);
                }
            }
            this.listeners.put(listener, new RichTextListener(eventType, listener));
        }
    }

    /**
     * Removes the listener from the collection of listeners who will be notifed
     * when an event of the given type occurs within this control.
     * @param eventType the type of event to listen for
     * @param listener the listener which should no longer be notified when the event
     * occurs
     */
    @objid ("7db59ed2-a333-4283-8698-b0196cbf2eef")
    @Override
    public void removeListener(int eventType, Listener listener) {
        if (this.editor != null && this.listeners.containsKey(listener)) {
            if (this.editorControl == null
                    || (eventType != SWT.Activate
                            && eventType != SWT.Deactivate
                            && eventType != SWT.FocusIn && eventType != SWT.FocusOut)) {
                this.editor.removeListener(eventType, listener);
            }
            this.listeners.remove(listener);
        }
    }

    /**
     * Returns the event listeners attached to this control.
     * @return an iterator for retrieving the event listeners attached to this
     * control
     */
    @objid ("4f631707-41a3-45a9-be07-04b9daeebe73")
    @Override
    public Iterator<RichTextListener> getListeners() {
        return this.listeners.values().iterator();
    }

    /**
     * Adds the listener to monitor events and status sent by the underlying
     * DHTML editor.
     */
    @objid ("5ce20eb3-528d-4ef2-aa9e-481fa562b3a2")
    protected void addStatusTextListener() {
        this.statusTextListener = new StatusTextListener() {
            @Override
            public void changed(StatusTextEvent event) {
                String eventText = event.text;
                int eventTextLength = eventText.length();
                if (eventText.startsWith(STATUS_PREFIX)
                        && eventTextLength > STATUS_PREFIX_LENGTH) {
                    try {
                        RichText.this.processingJSEvent = true;
                        int endStatusIndex = STATUS_PREFIX_LENGTH + 1;
                        if (eventText.length() > STATUS_PREFIX_LENGTH + 1
                                && Character.isDigit(eventText
                                        .charAt(endStatusIndex))) {
                            endStatusIndex++;
                        }
                        int statusType = Integer.parseInt(eventText.substring(
                                STATUS_PREFIX_LENGTH, endStatusIndex));
                        switch (statusType) {
                        case STATUS_NOP:
                            break;
                        case STATUS_INITIALIZED:
                            if (!RichText.this.initialized) {
                                RichText.this.initialized = true;
                                if (RichText.this.debug) {
                                    printDebugMessage(
                                            "statusTextListener", "STATUS_INITIALIZED"); //$NON-NLS-1$ //$NON-NLS-2$
                                }
                                if (!Platform.getOS().equals("win32")) { //$NON-NLS-1$
                                    // Workaround Mozilla'a IFRAME
                                    // height issue.
                                    executeCommand(IRichTextCommands.SET_HEIGHT,
                                            "" + RichText.this.editor.getBounds().height); //$NON-NLS-1$
                                }
                                executeCommand(IRichTextCommands.SET_TEXT,
                                        workaroundForObjectParamNode(RichText.this.currentText));
                                if (RichText.this.initializedWithFocus) {
                                    setFocus();
                                }
                                if (!RichText.this.editable) {
                                    executeCommand(
                                            IRichTextCommands.SET_EDITABLE,
                                            "" + RichText.this.editable); //$NON-NLS-1$
                                }                                
                                
                                if (Platform.getOS().equals(Platform.OS_WIN32)) {
                                    notifyListeners(RichText.RICH_TEXT_INITIALIZED_WIN32, new Event());
                                }
                                
                                // Ask for selection to initialize toolbar combos selection
                                getControl().getDisplay().asyncExec( new Runnable() {
                                    @Override
                                    public void run() {
                                        executeCommand(IRichTextCommands.GET_SELECTED_TEXT); //$NON-NLS-1$
                                    }
                                });
                                
                            }
                            break;
                        case STATUS_MODIFIED:
                            if (RichText.this.debug) {
                                printDebugMessage(
                                        "statusTextListener", "STATUS_MODIFIED"); //$NON-NLS-1$ //$NON-NLS-2$
                            }
                            checkModify();
                            break;
                        case STATUS_GET_TEXT:
                            if (eventTextLength >= STATUS_PREFIX_LENGTH + 2) {
                                RichText.this.currentText = eventText
                                        .substring(STATUS_PREFIX_LENGTH + 2);
                                
                                RichText.this.currentText = unWorkaroundForObjectParamNode(RichText.this.currentText); 
                            } else {
                                RichText.this.currentText = ""; //$NON-NLS-1$
                            }
                            if (RichText.this.debug) {
                                printDebugMessage(
                                        "statusTextListener", //$NON-NLS-1$
                                        "STATUS_GET_TEXT, currentText=", RichText.this.currentText); //$NON-NLS-1$
                            }
                            break;
                        case STATUS_KEY_DOWN:
                            if (eventTextLength >= STATUS_PREFIX_LENGTH + 2) {
                                String cmd = eventText
                                        .substring(STATUS_PREFIX_LENGTH + 2);
                                if (RichText.this.debug) {
                                    printDebugMessage("statusTextListener", //$NON-NLS-1$
                                            "STATUS_KEY_DOWN, cmd=" + cmd); //$NON-NLS-1$
                                }
                                if (cmd.equals(IRichTextCommands.COPY)) {
                                    setCopyURL();
                                } else if (cmd.equals(IRichTextCommands.CUT)) {
                                    setCopyURL();
                                    CutAction action = new CutAction(
                                            RichText.this);
                                    action.execute(RichText.this);
                                } else if (cmd
                                        .equals(IRichTextCommands.FIND_TEXT)) {
                                    getFindReplaceAction().execute(
                                            RichText.this);
                                } else if (cmd.equals(IRichTextCommands.PASTE)) {
                                    PasteAction action = new PasteAction(
                                            RichText.this);
                                    action.execute(RichText.this);
                                } else if (cmd.equals(IRichTextCommands.SAVE)) {
                                    PlatformUI.getWorkbench()
                                            .getActiveWorkbenchWindow()
                                            .getActivePage().getActiveEditor()
                                            .doSave(null);
                                } else if (cmd.equals(IRichTextCommands.SAVE_ALL)) {
                                    PlatformUI.getWorkbench()
                                            .getActiveWorkbenchWindow()
                                            .getActivePage().saveAllEditors(
                                                    false);
                                }
                            }
                            break;
                        case STATUS_KEY_UP:
                            if (RichText.this.debug) {
                                printDebugMessage("statusTextListener", //$NON-NLS-1$
                                        "STATUS_KEY_UP, modified=" + RichText.this.modified); //$NON-NLS-1$
                            }
                            checkModify();
                            break;
                        case STATUS_SELECT_TABLE:
                            RichText.this.tableSelection = true;
                            
                            if (hasFocus())
                                notifyListeners(SWT.Selection, new Event());
                            break;
                        case STATUS_SELECT_TEXT:
                            if (eventTextLength >= STATUS_PREFIX_LENGTH + 2) {
                                String[] strings = eventText.substring(
                                        STATUS_PREFIX_LENGTH + 2).split(
                                        "\\$", 5); //$NON-NLS-1$
                                try {
                                    RichText.this.richTextSelection.setFontName(strings[0]);
                                    RichText.this.richTextSelection.setFontSize(strings[1]);
                                    RichText.this.richTextSelection.setBlockStyle(strings[2]);
                                    RichText.this.richTextSelection.setFlags(Integer.parseInt(strings[3]));
                                    RichText.this.richTextSelection.setText(strings[4]);
                                } catch (NumberFormatException e) {
                                    RichText.this.logger.error(e);
                                }
                                if (RichText.this.debug) {
                                    // printDebugMessage(
                                    //        "selectionStatusListener", //$NON-NLS-1$
                                    //        "current selection is=" + RichText.this.richTextSelection); //$NON-NLS-1$
                                    printDebugMessage(
                                            "selectionStatusListener", //$NON-NLS-1$
                                            "current block style is=" + RichText.this.richTextSelection.blockStyle+", hasFocus="+hasFocus()+" f2="+RichText.this.editor.isFocusControl()); //$NON-NLS-1$
                                }
                                if(strings[4].length() == 0) {
                                    RichText.this.hasSelection = false;
                                } else {
                                    RichText.this.hasSelection = true;
                                }
                                
                                if (RichText.this.editor.isFocusControl())
                                    notifyListeners(SWT.Selection, new Event());
                                    
                            } else {
                                RichText.this.richTextSelection.setText(""); //$NON-NLS-1$
                                RichText.this.hasSelection = false;
                                if (RichText.this.debug) {
                                    printDebugMessage(
                                            "statusTextListener", //$NON-NLS-1$
                                            "STATUS_SELECT_TEXT, no selection"); //$NON-NLS-1$
                                }
                            }
                            if (RichText.this.debug) {
                                printDebugMessage(
                                        "statusTextListener", //$NON-NLS-1$
                                        "STATUS_SELECT_TEXT, selectedText=", RichText.this.richTextSelection.getText()); //$NON-NLS-1$
                            }
                            RichText.this.tableSelection = false;
                            break;
                        case STATUS_SELECT_CONTROL:
                            if (RichText.this.debug) {
                                printDebugMessage("statusTextListener", //$NON-NLS-1$
                                        "STATUS_SELECT_CONTROL, control selected"); //$NON-NLS-1$
                            }
                            RichText.this.hasSelection = true;
                            break;
                        case STATUS_SELECT_NONE:
                            if (RichText.this.debug) {
                                printDebugMessage("statusTextListener", //$NON-NLS-1$
                                        "STATUS_SELECT_NONE, no selection"); //$NON-NLS-1$
                            }
                            RichText.this.hasSelection = false;
                            RichText.this.tableSelection = false;
                            break;
                        case STATUS_EXEC_CMD:
                            if (eventTextLength >= STATUS_PREFIX_LENGTH + 3) {
                                try {
                                    RichText.this.status = Integer.parseInt(eventText
                                            .substring(
                                                    STATUS_PREFIX_LENGTH + 2,
                                                    STATUS_PREFIX_LENGTH + 3));
                                } catch (Exception e) {
                                    RichText.this.status = -1;
                                }
                            }
                            if (RichText.this.debug && RichText.this.status != 1) {
                                printDebugMessage("statusTextListener", //$NON-NLS-1$
                                        "STATUS_EXEC_CMD, status=" + RichText.this.status); //$NON-NLS-1$
                            }
                            break;
                        case STATUS_REFORMAT_LINKS:
                            if (RichText.this.debug) {
                                printDebugMessage(
                                        "statusTextListener", "STATUS_REFORMAT_LINKS"); //$NON-NLS-1$ //$NON-NLS-2$
                            }
                            if (Platform.getOS().equals("win32")) { //$NON-NLS-1$ 
                                // Workaround the drag and drop issue with DBCS
                                // characters.
        //                                if (modified) {
        //                                    setText(getText());
        //                                    modified = true;
        //                                }
                            }
                            checkModify();
                            break;
                        case STATUS_FOCUS_GOT:
                            // for debug purposes only
                            if (RichText.this.debug) {
                                printDebugMessage(
                                        "statusTextListener", "STATUS_FOCUS_GOT"); //$NON-NLS-1$ //$NON-NLS-2$
                            }
                            break;
                        case STATUS_FOCUS_LOST:
                            // for debug purposes only
                            if (RichText.this.debug) {
                                printDebugMessage(
                                        "statusTextListener", "STATUS_FOCUS_LOST"); //$NON-NLS-1$ //$NON-NLS-2$
                            }
                            break;
                        case STATUS_INITIALIZATION_FAILED:
                            getLogger().error("RichText initialization failed.");
                            if (RichText.this.debug) {
                                printDebugMessage(
                                        "statusTextListener", "STATUS_INITIALIZATION_FAILED"); //$NON-NLS-1$ //$NON-NLS-2$
                            }
                            break;
                        default:
                            printDebugMessage("statusTextListener"," ?'"+event.text+"'"); //$NON-NLS-1$ //$NON-NLS-2$
                            break;
                        }
                    } catch (Exception e) {
                        getLogger().warning(e);
                    } finally {
                        RichText.this.processingJSEvent = false;
                    }
                } else {
                    if (RichText.this.debug) {
                        printDebugMessage("statusTextListener","'"+event.text+"'"); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                }
            }
        };
        this.editor.addStatusTextListener(this.statusTextListener);
    }

    @objid ("d1bdbe99-71fa-4bbb-922a-1a6c7333eff5")
    PluginLogger getLogger() {
        return this.logger;
    }

    /**
     * Generates the HTML source for the editor.
     * @return the HTML source for the editor
     */
    @objid ("4c23242f-a461-4b06-aa7b-61c992c84e05")
    protected String generateEditorHTML() throws Exception {
        String escapedBasePath = this.basePath;
        if (escapedBasePath.startsWith(FileUtil.UNC_PATH_PREFIX))
            escapedBasePath = escapedBasePath.replaceFirst(
                    "^\\\\\\\\", "\\\\\\\\\\\\\\\\"); //$NON-NLS-1$ //$NON-NLS-2$
        escapedBasePath = XMLUtil
                .escape("file://" + escapedBasePath.replaceAll("'", "\\\\'")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        String escapedRteUTL = this.rteURL.replaceAll("&apos;", "%27"); //$NON-NLS-1$ //$NON-NLS-2$
        
        StringBuffer rteXML = new StringBuffer();
        rteXML.append("<rte id=\"").append("rte") //$NON-NLS-1$ //$NON-NLS-2$
                .append("\" css=\"").append(escapedRteUTL + "rte.css") //$NON-NLS-1$ //$NON-NLS-2$
                .append("\" js=\"").append(escapedRteUTL + "rte.js") //$NON-NLS-1$ //$NON-NLS-2$
                .append("\" baseURL=\"").append(escapedBasePath) //$NON-NLS-1$
                .append("\"/>"); //$NON-NLS-1$
        StringWriter result = new StringWriter();
        XSLTProcessor.transform(
                this.rteFolder + "rte.xsl", rteXML.toString(), result); //$NON-NLS-1$
        return result.toString();
    }

    /**
     * Fills the context menu with menu items.
     * @param aContextMenu a context menu containing rich text actions
     */
    @objid ("a30d82e4-8d9b-49af-b8cd-30760bff56ca")
    protected void fillContextMenu(Menu aContextMenu) {
        final MenuItem cutItem = new MenuItem(aContextMenu, SWT.PUSH);
        cutItem.setText(HtmlTextResources.cutAction_text);
        cutItem.setImage(HtmlTextImages.IMG_CUT);
        cutItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                CutAction action = new CutAction(RichText.this);
                action.execute(RichText.this);
            }
        });
        final MenuItem copyItem = new MenuItem(aContextMenu, SWT.PUSH);
        copyItem.setText(HtmlTextResources.copyAction_text);
        copyItem.setImage(HtmlTextImages.IMG_COPY);
        copyItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                CopyAction action = new CopyAction(RichText.this);
                action.execute(RichText.this);
            }
        });
        final MenuItem pasteItem = new MenuItem(aContextMenu, SWT.PUSH);
        pasteItem.setText(HtmlTextResources.pasteAction_text);
        pasteItem.setImage(HtmlTextImages.IMG_PASTE);
        pasteItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                PasteAction action = new PasteAction(RichText.this);
                action.execute(RichText.this);
            }
        });
        
        final MenuItem pastePlainTextItem = new MenuItem(aContextMenu, SWT.PUSH);
        pastePlainTextItem.setText(HtmlTextResources.pastePlainTextAction_text);
        pastePlainTextItem.setImage(HtmlTextImages.IMG_PASTE_PLAIN_TEXT);
        pastePlainTextItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                PastePlainTextAction action = new PastePlainTextAction(
                        RichText.this);
                action.execute(RichText.this);
            }
        });
        
        @SuppressWarnings("unused")
        final MenuItem sperate1 = new MenuItem(aContextMenu, SWT.SEPARATOR);
        
        final MenuItem addRowItem = new MenuItem(aContextMenu, SWT.PUSH);
        addRowItem.setText(HtmlTextResources.addRowAction_text);
        addRowItem.setImage(HtmlTextImages.IMG_ADD_ROW);
        addRowItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                AddRowAction action = new AddRowAction(RichText.this);
                action.execute(RichText.this);
            }
        });
        
        final MenuItem addColumnItem = new MenuItem(aContextMenu, SWT.PUSH);
        addColumnItem.setText(HtmlTextResources.addColumnAction_text);
        addColumnItem.setImage(HtmlTextImages.IMG_ADD_COLUMN);
        addColumnItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                AddColumnAction action = new AddColumnAction(RichText.this);
                action.execute(RichText.this);
            }
        });
        
        final MenuItem deleteLastRowItem = new MenuItem(aContextMenu, SWT.PUSH);
        deleteLastRowItem.setText(HtmlTextResources.deleteLastRowAction_text);
        deleteLastRowItem.setImage(HtmlTextImages.IMG_DELETE_ROW);
        deleteLastRowItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                DeleteLastRowAction action = new DeleteLastRowAction(RichText.this);
                action.execute(RichText.this);
            }
        });
        
        final MenuItem deleteLastColumnItem = new MenuItem(aContextMenu, SWT.PUSH);
        deleteLastColumnItem.setText(HtmlTextResources.deleteLastColumnAction_text);
        deleteLastColumnItem.setImage(HtmlTextImages.IMG_DELETE_COLUMN);
        deleteLastColumnItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                DeleteLastColumnAction action = new DeleteLastColumnAction(RichText.this);
                action.execute(RichText.this);
            }
        });
        
        aContextMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuHidden(MenuEvent e) {
               // Ignore 
            }
        
            @Override
            public void menuShown(MenuEvent e) {
                Clipboard clipboard = new Clipboard(Display.getCurrent());
                String html = (String) clipboard.getContents(HTMLTransfer
                        .getInstance());
                String text = (String) clipboard.getContents(TextTransfer
                        .getInstance());
                cutItem.setEnabled(RichText.this.editable && RichText.this.hasSelection);
                copyItem.setEnabled(RichText.this.hasSelection);
                pasteItem.setEnabled(RichText.this.editable && (html != null));
                pastePlainTextItem.setEnabled(RichText.this.editable && (text != null));
                addRowItem.setEnabled(RichText.this.tableSelection);
                addColumnItem.setEnabled(RichText.this.tableSelection);
                deleteLastRowItem.setEnabled(RichText.this.tableSelection);
                deleteLastColumnItem.setEnabled(RichText.this.tableSelection);
            }
        });
    }

    /**
     * Adds listeners to manage the activation and focus events.
     */
    @objid ("5ab028dc-fccc-4a6c-812b-45b4d0ce253b")
    protected void addListeners() {
        this.editorControl = getControlSite(this.editor);
        if (this.editorControl != null) {
            // only IE (win32) has the editorControl != null
            this.isIE = true;
        
            if (this.debug) {
                printDebugMessage(
                        "init", "editorControl=" + this.editorControl.getClass().getName()); //$NON-NLS-1$ //$NON-NLS-2$
            }
            
            
            this.editorControl.addListener(SWT.Activate, new Listener() {
                @Override
                public void handleEvent(Event event) {
                    if (RichText.this.debug) {
                        printDebugMessage("activateListener"); //$NON-NLS-1$
                    }
                    setFocus();
                    notifyListeners(SWT.Activate, event);
                }
            });
        
            this.editorControl.addListener(SWT.Deactivate, new Listener() {
                @Override
                public void handleEvent(Event event) {
                    if (RichText.this.debug) {
                        printDebugMessage("deactivateListener"); //$NON-NLS-1$
                    }
                    setBlur();
                    notifyListeners(SWT.Deactivate, event);
                }
            });
        
            this.editorControl.addListener(SWT.FocusIn, new Listener() {
                @Override
                public void handleEvent(Event event) {
                    if (RichText.this.debug) {
                        printDebugMessage("focusInListener"); //$NON-NLS-1$
                    }
                    executeCommand("updateSelection"); //$NON-NLS-1$                    
                    notifyListeners(SWT.FocusIn, event);
                }
            });
        
            this.editorControl.addKeyListener(new KeyAdapter() {
                   @Override
                public void keyReleased(KeyEvent event) {
                        int keyCode = event.keyCode;
                        int stateMask = event.stateMask;
                        if (RichText.this.debug) {
                            printDebugMessage(
                                    "keyUpListener", "keyCode=" + keyCode //$NON-NLS-1$ //$NON-NLS-2$
                                            + ", stateMask=" + stateMask + ", editable=" + RichText.this.editable); //$NON-NLS-1$ //$NON-NLS-2$
                        }
                        
                        if ( stateMask == SWT.CTRL && event.keyCode == 0x11 ) { //0x11 is for all Control key, such as ctrl-b, ctrl-I, ctrl-c, etc.. 
                            executeCommand("updateSelection");
                        } 
                        
                        if ((stateMask & SWT.CTRL) > 0
                                || (stateMask & SWT.ALT) > 0
                                || ((stateMask & SWT.SHIFT) > 0 && keyCode == stateMask)) {
                            return;
                        }
                        if (RichText.this.editable) {
                            switch (event.keyCode) {
                            case SWT.ARROW_DOWN:
                            case SWT.ARROW_LEFT:
                            case SWT.ARROW_RIGHT:
                            case SWT.ARROW_UP:
                            case SWT.END:
                            case SWT.HOME:
                            case SWT.PAGE_DOWN:
                            case SWT.PAGE_UP:
                            case SWT.TAB:
                                return;
                            default:
                                checkModify();
                                break;
                            }
                        }
                    }
                });
            
        //            editorControl.addListener(SWT.KeyUp, new Listener() {
        //                public void handleEvent(Event event) {
        //                    int keyCode = event.keyCode;
        //                    int stateMask = event.stateMask;
        //                    if (debug) {
        //                        printDebugMessage(
        //                                "keyUpListener", "keyCode=" + keyCode //$NON-NLS-1$ //$NON-NLS-2$
        //                                        + ", stateMask=" + stateMask + ", editable=" + editable); //$NON-NLS-1$ //$NON-NLS-2$
        //                    }
        //                    if ((stateMask & SWT.CTRL) > 0
        //                            || (stateMask & SWT.ALT) > 0
        //                            || ((stateMask & SWT.SHIFT) > 0 && keyCode == stateMask)) {
        //                        return;
        //                    }
        //                    if (editable) {
        //                        switch (event.keyCode) {
        //                        case SWT.ARROW_DOWN:
        //                        case SWT.ARROW_LEFT:
        //                        case SWT.ARROW_RIGHT:
        //                        case SWT.ARROW_UP:
        //                        case SWT.END:
        //                        case SWT.HOME:
        //                        case SWT.PAGE_DOWN:
        //                        case SWT.PAGE_UP:
        //                        case SWT.TAB:
        //                            return;
        //                        default:
        //                            checkModify();
        //                            break;
        //                        }
        //                    }
        //                }
        //            });
        
            this.editor.addLocationListener(new LocationAdapter() {
                @Override
                public void changing(LocationEvent event) {
                    // Deactivate the links in the content page in readonly
                    // mode.
                    event.doit = RichText.this.editable;
                }
            });
        } else {
            // Not Internet Explorer
            
            this.editor.addListener(SWT.Activate, new Listener() {
                @Override
                public void handleEvent(Event event) {
                    if (RichText.this.debug) {
                        printDebugMessage("activateListener"); //$NON-NLS-1$
                    }
                    setFocus();
                }
            });
        
            this.editor.addKeyListener(new KeyListener() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.keyCode == SWT.TAB) {
                        if ((e.stateMask & SWT.SHIFT) != 0) {
                            RichText.this.editor.traverse(SWT.TRAVERSE_TAB_PREVIOUS);
                        } else if ((e.stateMask & SWT.CTRL) == 0 ){
                            RichText.this.editor.traverse(SWT.TRAVERSE_TAB_NEXT);
                        }
                        return;
                    }
                    if (!RichText.this.editable) {
                        e.doit = false;
                    }
                }
        
                @Override
                public void keyReleased(KeyEvent e) {
                    if ((e.stateMask & SWT.CTRL) > 0
                            || (e.stateMask & SWT.ALT) > 0)
                        return;
                    if (RichText.this.editable) {
                        switch (e.keyCode) {
                        case SWT.ARROW_DOWN:
                        case SWT.ARROW_LEFT:
                        case SWT.ARROW_RIGHT:
                        case SWT.ARROW_UP:
                        case SWT.END:
                        case SWT.HOME:
                        case SWT.PAGE_DOWN:
                        case SWT.PAGE_UP:
                        case SWT.SHIFT:
                        case SWT.TAB:
                            break;
                        default:
                            checkModify();
                            break;
                        }
                    }
                }
            });
        }
        
        this.editor.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                if (RichText.this.debug) {
                    printDebugMessage("disposeListener"); //$NON-NLS-1$         
                    executeCommand("dispose");
                }
                dispose();
            }
        });
        
        Listener ll = new Listener() {
            @Override
            public void handleEvent(Event event) {
                notifyListeners(event.type, event);
            }
        };
        this.editor.addListener(SWT.FocusIn, ll);
        this.editor.addListener(SWT.FocusOut, ll);
        
        
        this.listeners = new Hashtable<>();
        this.modifyListeners = new ArrayList<>();
    }

    /**
     * Notifies the rich text event listeners.
     * @param eventType the event type
     * @param event the SWT event
     */
    @objid ("49b2941d-698e-44c6-9178-03a853d3bb94")
    protected void notifyListeners(int eventType, Event event) {
        if (this.notifyingModifyListeners) {
            return;
        }
        
        if (this.listeners != null) {
            event.display = Display.getCurrent();
            event.widget = this.editor;
        
            for (RichTextListener listener : this.listeners.values()) {
                if (listener.getEventType() == eventType) {
                    if (this.debug) {
                        printDebugMessage(
                                "notifyListeners", "notifying , " + listener ); //$NON-NLS-1$ //$NON-NLS-2$    
                    }
                    
                    listener.getListener().handleEvent(event);
                    
                    if (this.debug) {
                        printDebugMessage(
                                "notifyListeners", "notified " + listener); //$NON-NLS-1$ //$NON-NLS-2$    
                    }
                }
            }
        }
    }

    /**
     * Notifies the modify listeners that the rich text editor content has
     * changed.
     */
    @objid ("b4dba930-c8b9-44fb-92de-c20fe3baceeb")
    @Override
    public void notifyModifyListeners() {
        this.notifyingModifyListeners = true;
        
        Event event = new Event();
        event.display = Display.getCurrent();
        event.widget = this.editor;
        
        for (ModifyListener listener : this.modifyListeners) {
            if (this.debug) {
                printDebugMessage(
                        "notifyModifyListeners", "notifying listener, " + listener); //$NON-NLS-1$ //$NON-NLS-2$    
            }
            listener.modifyText(new ModifyEvent(event));
            if (this.debug) {
                printDebugMessage(
                        "notifyModifyListeners", "notified listener, " + listener); //$NON-NLS-1$ //$NON-NLS-2$    
            }
        }
        
        this.notifyingModifyListeners = false;
    }

    @objid ("9a421fe7-114c-4d94-8f6a-e8d587cdda21")
    @Override
    public void checkModify() {
        try {
            if (!this.checkingModifyEvent) {
                this.checkingModifyEvent = true;
                if (this.modified) {
                    notifyModifyListeners();
                } else {
                    if (!this.isIE && this.processingJSEvent) {
                        Display.getCurrent().asyncExec(new Runnable() {
                            @Override
                            public void run() {
                                if (!getText().equals(RichText.this.initialText)) {
                                    RichText.this.modified = true;
                                    notifyModifyListeners();
                                }
                            }
                        });
                    } else {
                        if (!getText().equals(this.initialText)) {
                            this.modified = true;
                            notifyModifyListeners();
                        }
                    }
                }
                if (this.debug) {
                    printDebugMessage("checkModify", "modified=" + this.modified); //$NON-NLS-1$ //$NON-NLS-2$    
                }
            }
        } finally {
            this.checkingModifyEvent = false;
        }
    }

    /**
     * Formats the text for consumption by the JavaScript/DHTML editor.
     * @param text rich text encoded in HTML format
     * @return the formatted text.
     */
    @objid ("87a8ad12-4636-4cd7-81d3-8c35e59eaab4")
    public String formatText(String text) {
        if (text == null || text.length() == 0) {
            return text;
        }
        StringBuffer result = new StringBuffer();
        int textSize = text.length();
        for (int i = 0; i < textSize; i++) {
            char ch = text.charAt(i);
            switch (ch) {
            case '\r':
                break;
            case '\t':
                result.append(' ');
                break;
            case '\n':
                result.append(ENCODED_NEWLINE);
                break;
            case '\'':
                result.append(ENCODED_SINGLE_QUOTE);
                break;
            case '\\':
                result.append("\\\\"); //$NON-NLS-1$
                break;
            default:
                result.append(ch);
            }
        }
        return result.toString();
    }

    /**
     * Returns the child <code>OleControlSite</code> contained within the
     * given <code>Composite</code>.
     * @param composite a <code>Composite</code> object, presumably a
     * <code>Browser</code>
     * @return an <code>OleControlSite</code> object
     */
    @objid ("796ba76a-ab6c-4b05-bebf-3b2a3eeaec27")
    protected Control getControlSite(Composite composite) {
        if (Platform.getOS().equals("win32")) { //$NON-NLS-1$
            Control[] controls = composite.getChildren();
            for (int i = 0; i < controls.length; i++) {
                String controlClass = controls[i].getClass().getName();
                if (controlClass.equals("org.eclipse.swt.browser.WebSite")) { //$NON-NLS-1$
                    return controls[i];
                } else if (controls[i] instanceof Composite) {
                    return getControlSite((Composite) controls[i]);
                }
            }
        }
        return null;
    }

    /**
     * Displays the given debug message to the console.
     */
    @objid ("94434794-0252-49ea-b1be-9ecbf8c37682")
    protected void printDebugMessage(String method, String msg, String text) {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("[")
        .append(this.getClass().getSimpleName())
        //.append(System.identityHashCode(this))
        .append(']') //$NON-NLS-1$
        .append('.')
        .append(method);
        
        if (msg != null && msg.length() > 0) {
            strBuf.append(": ").append(msg); //$NON-NLS-1$
        }
        if (text != null && text.length() > 0) {
            strBuf.append('\n').append(text);
        }
        getLogger().debug(strBuf.toString());
    }

    /**
     * Displays the given debug message to the console.
     */
    @objid ("4004eb45-de5d-47b5-a468-6bb32632e877")
    protected void printDebugMessage(String method, String msg) {
        printDebugMessage(method, msg, null);
    }

    /**
     * Displays the given debug message to the console.
     */
    @objid ("aeaf1257-90d2-48a9-a314-bc4962c0a775")
    protected void printDebugMessage(String method) {
        printDebugMessage(method, null);
    }

    @objid ("96f86711-f9dc-404e-a54e-7161bec81cf5")
    @Override
    public FindReplaceAction getFindReplaceAction() {
        return this.findReplaceAction;
    }

    @objid ("6eccb627-41b6-4dc9-93d0-dcc784af62f2")
    @Override
    public void setFindReplaceAction(FindReplaceAction findReplaceAction) {
        if (findReplaceAction != null) {
            if (this.findReplaceAction != null
                    && this.findReplaceAction != findReplaceAction) {
                this.findReplaceAction.dispose();
            }
            this.findReplaceAction = findReplaceAction;
            this.findReplaceAction.setRichText(this);
        }
    }

    @objid ("848321d7-7fae-4c08-a07f-8f0ae3d6453e")
    @Override
    public void setInitialText(String text) {
        setText(text);
        this.initialText = text == null ? "" : text; //$NON-NLS-1$
        this.modified = false;
    }

    /**
     * Escape 'param' HTML nodes to 'paramTemp' nodes.
     * @param html HTML text
     * @return the HTML text with 'param' nodes escaped.
     */
    @objid ("1033a53b-80ae-4bb5-bd5f-7e7d31e5d07f")
    public static String workaroundForObjectParamNode(String html) {
        String result = html.replaceAll("<param", "<paramTemp"); //$NON-NLS-1$ //$NON-NLS-2$
        return result;
    }

    /**
     * Unescape 'param' HTML nodes.
     * @param html HTML text
     * @return the HTML text with 'paramTemp' nodes changed to 'param'.
     */
    @objid ("90e274c4-8a29-4190-9a84-21cf6aaf0d67")
    String unWorkaroundForObjectParamNode(String html) {
        String result = html.replaceAll("<paramTemp", "<param"); //$NON-NLS-1$ //$NON-NLS-2$
        return result;
    }

    /**
     * @return The current raw text ?
     */
    @objid ("c64cea97-38c5-4c14-be4f-fa3f702c4315")
    public String getCurrentRawText() {
        return this.currentRawText;
    }

    @objid ("d67d9a0f-5c4b-40b4-a34a-ce15c710b48e")
    private void setCurrentRawText(String currentRawText) {
        this.currentRawText = currentRawText == null ? "" : currentRawText;    //$NON-NLS-1
    }

    @objid ("7d133fbe-8d2c-4f5b-9353-e054027e343a")
    @Override
    public String getTidyText() {
        String txt = getText();
        if (!txt.isEmpty()) {
            //this.currentText = this.currentText.replaceAll(
            //        "<P>&nbsp;</P>", "<br/>"); //$NON-NLS-1$ //$NON-NLS-2$            
            txt = formatHTML(txt, this.htmlGetFormatter);
            return txt;
        }
        return "";
    }

    @objid ("f43b033b-e25d-4f72-8aa0-53f7971661d6")
    private void setText(String text, IHTMLFormatter formatter) {
        if (this.editor != null) {
            if (this.debug) {
                printDebugMessage("setText", "text=", text); //$NON-NLS-1$ //$NON-NLS-2$
            }
        
            setCurrentRawText(text);
            
            String newText = text;
            if (newText != null) {
                // Call JTidy to format the source to XHTML.
                newText = formatHTML(newText, formatter);
            } else {
                newText = ""; //$NON-NLS-1$
            }
        
            if (this.initialized) {
                this.modified = !newText.equals(this.currentText);
            }
            this.initialText = newText;
            if (this.initialText.equals("") && !this.isIE) { //$NON-NLS-1$
                this.initialText = "<br />"; //$NON-NLS-1$
            }
        
            if (this.debug) {
                printDebugMessage(
                        "setText", "modified=" + this.modified + ", newText=", newText); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }
        
            if (this.initialized) {
                try {
                    executeCommand(IRichTextCommands.SET_TEXT, workaroundForObjectParamNode(newText));
                    executeCommand(IRichTextCommands.SET_EDITABLE, "" + this.editable); //$NON-NLS-1$
        
                    // Ask for selection to initialize toolbar combos selection
                    executeCommand(IRichTextCommands.GET_SELECTED_TEXT); //$NON-NLS-1$
                } catch (Exception e) {
                    this.logger.error(e);
                }
            }
        
            this.currentText = newText;
        }
    }

    /**
     * Function callable from JavaScript to send SWT focus event.
     * <p>
     * Used to workaround lacks of focus event when the browser get focus.
     * 
     * @author cmarin
     */
    @objid ("b9df77df-5669-44fb-bfe2-f3ba022b4df9")
    class SendFocusEventFunction extends BrowserFunction {
        @objid ("8c8d5ed1-02a0-4b12-b070-c2849c9d068a")
        private int eventType;

        @objid ("5248d80d-75ff-4594-b591-70c5eacb64f7")
        public SendFocusEventFunction(Browser browser, String name, int eventType) {
            super(browser, name);
            this.eventType = eventType;
        }

        @objid ("2d4a7f50-312f-468b-8f0b-3674384e65fe")
        @Override
        public Object function(Object[] arguments) {
            Event ev = new Event();
            ev.type = this.eventType;
            ev.item = getBrowser();
            
            notifyListeners(this.eventType, ev);
            return null;
        }

    }

}
