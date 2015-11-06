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

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.e4.ui.bindings.EBindingService;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.bindings.keys.KeySequence;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.bindings.keys.SWTKeySupport;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextViewerExtension;
import org.eclipse.jface.text.IUndoManager;
import org.eclipse.jface.text.IUndoManagerExtension;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.text.TextViewerUndoManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.HTMLTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.operations.OperationHistoryActionHandler;
import org.eclipse.ui.texteditor.IReadOnlyDependent;
import org.eclipse.ui.texteditor.IUpdate;
import org.modelio.edition.html.epfcommon.IHTMLFormatter;
import org.modelio.edition.html.plugin.HtmlTextImages;
import org.modelio.edition.html.plugin.HtmlTextPlugin;
import org.modelio.edition.html.plugin.HtmlTextResources;
import org.modelio.edition.html.view.actions.AddColumnAction;
import org.modelio.edition.html.view.actions.AddImageAction;
import org.modelio.edition.html.view.actions.AddLinkAction;
import org.modelio.edition.html.view.actions.AddOrderedListAction;
import org.modelio.edition.html.view.actions.AddRowAction;
import org.modelio.edition.html.view.actions.AddTableAction;
import org.modelio.edition.html.view.actions.AddUnorderedListAction;
import org.modelio.edition.html.view.actions.BoldAction;
import org.modelio.edition.html.view.actions.ClearContentAction;
import org.modelio.edition.html.view.actions.CopyAction;
import org.modelio.edition.html.view.actions.CutAction;
import org.modelio.edition.html.view.actions.DeleteLastColumnAction;
import org.modelio.edition.html.view.actions.DeleteLastRowAction;
import org.modelio.edition.html.view.actions.FindReplaceAction;
import org.modelio.edition.html.view.actions.FontNameAction;
import org.modelio.edition.html.view.actions.FontSizeAction;
import org.modelio.edition.html.view.actions.FontStyleAction;
import org.modelio.edition.html.view.actions.IndentAction;
import org.modelio.edition.html.view.actions.ItalicAction;
import org.modelio.edition.html.view.actions.JustifyCenterAction;
import org.modelio.edition.html.view.actions.JustifyLeftAction;
import org.modelio.edition.html.view.actions.JustifyRightAction;
import org.modelio.edition.html.view.actions.OutdentAction;
import org.modelio.edition.html.view.actions.PasteAction;
import org.modelio.edition.html.view.actions.PastePlainTextAction;
import org.modelio.edition.html.view.actions.SubscriptAction;
import org.modelio.edition.html.view.actions.SuperscriptAction;
import org.modelio.edition.html.view.actions.TextColorAction;
import org.modelio.edition.html.view.actions.TextHighlightAction;
import org.modelio.edition.html.view.actions.TidyActionGroup;
import org.modelio.edition.html.view.actions.UnderlineAction;
import org.modelio.log.writers.PluginLogger;

/**
 * The default rich text editor implementation.
 * <p>
 * The default rich text editor uses XHTML as the underlying markup language for
 * the rich text content. It is implemented using a <code>ViewForm</code>
 * control with a tool bar at the top, a tab folder that contains a
 * <code>RichText</code> control for entering the rich text content, and a tab
 * foler that contains a <code>StyleText</code> control for viewing and
 * modifying the XHTML representation of the rich text content.
 * 
 * @author Kelvin Low
 * @author Jeff Hardy
 * @since 1.0
 */
@objid ("320289f1-a09e-41f6-8d54-683ea2992fed")
public class RichTextEditor implements IRichTextEditor {
    /**
     * The HTML tab name.
     */
    @objid ("68188481-6d0a-4f0b-94d4-d2a46f6f38bc")
    protected static final String HTML_TAB_NAME = HtmlTextResources.htmlTab_text;

    /**
     * If true, log debugging info.
     */
    @objid ("0764af06-66aa-434b-9494-bbb1072d953f")
    protected boolean debug;

    /**
     * The base path used for resolving links (<href>, <img>, etc.).
     */
    @objid ("f459b764-3dcb-419d-9021-9b549de0a05e")
    protected String basePath;

    /**
     * Has the HTML source been modified?
     */
    @objid ("5d649bb9-2320-4c75-9e28-ccc19df91d09")
    protected boolean sourceModified = false;

    /**
     * The editor's editable flag.
     */
    @objid ("f51d938f-4524-47dc-8a9f-bddfc7ba6756")
    protected boolean editable = true;

    /**
     * The key binding scopes of this editor.
     * @since 2.1
     */
    @objid ("3f8eeb57-695a-486c-b8af-cb3aa279bafe")
    private String[] fKeyBindingScopes;

    /**
     * The plug-in logger.
     */
    @objid ("bee5a368-f026-4719-9860-c9f978e0bd07")
    protected PluginLogger logger;

    /**
     * The editor form.
     */
    @objid ("fbb2fc1d-b013-4855-9b1a-a4ccdf969f0f")
    protected ViewForm form;

    /**
     * The editor tool bar.
     */
    @objid ("63e47d9d-4125-4796-94b7-49b49438f283")
    protected IRichTextToolBar toolBar;

    /**
     * The editor content.
     */
    @objid ("189ddc06-b158-4f35-a48f-5e428d343f16")
    protected Composite content;

    /**
     * The editor tab folder.
     */
    @objid ("d740b179-a09c-4304-9db1-adba2de61c46")
    protected CTabFolder tabFolder;

    /**
     * The rich text tab
     */
    @objid ("e1a36300-5bdf-4055-8f2e-a43c83ec108a")
    protected CTabItem richTextTab;

    /**
     * The HTML source tab
     */
    @objid ("4d9eb595-ca56-4ef3-8827-4388a800096a")
    protected CTabItem htmlTab;

    /**
     * The embedded rich text control.
     */
    @objid ("2fd11e57-24b5-4086-ae2d-6eedcf027ba0")
    protected RichText richText;

    /**
     * The underlying HTML text editor.
     */
    @objid ("7357d938-e9cb-41a7-84f2-7f8baa4e845e")
    protected TextViewer sourceViewer;

    @objid ("16166a1b-f281-4888-a8b9-7e934bd61ab3")
    protected IDocument currentDoc;

    /**
     * Drop support
     */
    @objid ("9fb924fd-d77f-4d5f-a62a-4ba92702b366")
    protected DropTarget sourceEditDropTarget;

    /**
     * HTML editor's context menu
     */
    @objid ("e353ecc3-daea-4c27-824b-9fd8f83c9b5c")
    protected Menu contextMenu;

    @objid ("6d0eebb9-34e9-42d0-a9ff-52d663c67e40")
    private OperationHistoryActionHandler undoAction;

    @objid ("baa65b50-ee86-4aad-9e4d-09aa338d90ea")
    private OperationHistoryActionHandler redoAction;

    /**
     * The actions registered with the editor.
     */
    @objid ("358856d6-448a-440b-9039-5cc98fcae699")
     Map<String, IAction> fActions = new HashMap<>(10);

    /**
     * The verify key listener for activation code triggering.
     */
    @objid ("7eb0573d-8ad3-4704-acc9-081d9e2fce9c")
    private ActivationCodeTrigger fActivationCodeTrigger = new ActivationCodeTrigger();

    @objid ("bf7fdccc-c717-4ed5-afa0-e0d3a5314603")
     final IUndoManager undoManager = new TextViewerUndoManager(10);

    @objid ("a29c1c9c-edd6-4c0f-8f3d-8dfb8310b2f5")
    protected IDocumentListener sourceEditDocumentListener = new IDocumentListener() {
		@Override
        public void documentAboutToBeChanged(DocumentEvent event) {
		    // ignore
		}
		@Override
        public void documentChanged(DocumentEvent event) {
			RichTextEditor.this.sourceModified = true;
			if (RichTextEditor.this.richText != null) {
				RichTextEditor.this.richText.notifyModifyListeners();
			}
		}
	};

// The deactivate listener for the sourceEdit control.
    @objid ("cb0c426c-1edf-4bea-9479-44c9c28210af")
    protected Listener sourceEditDeactivateListener = new Listener() {
		@SuppressWarnings("synthetic-access")
        @Override
        public void handleEvent(Event event) {
			if (RichTextEditor.this.sourceModified) {
				updateRichText(RichTextEditor.this.sourceViewer.getTextWidget().getText());
				setModified(true);
				RichTextEditor.this.sourceModified = false;
			}
		}
	};

// The key listener for the sourceEdit control.
    @objid ("023be3c0-068b-4d62-86e3-28a704684bd2")
    protected KeyListener sourceEditKeyListener = new KeyListener() {
		@Override
        public void keyPressed(KeyEvent event) {
		    int accel = SWTKeySupport
		            .convertEventToUnmodifiedAccelerator(event);
		    KeyStroke stroke = SWTKeySupport
		            .convertAcceleratorToKeyStroke(accel);
		    KeySequence seq = KeySequence.getInstance(stroke);
		    
		    System.err.println("Key sequence:"+seq.toString());
		    
            for (IAction action : RichTextEditor.this.fActions.values()) {
                if (action.getAccelerator() == event.keyCode) {
                    if (action instanceof IUpdate)
                        ((IUpdate) action).update();
                    if (!action.isEnabled() && action instanceof IReadOnlyDependent) {
                        IReadOnlyDependent dependent= (IReadOnlyDependent) action;
                        boolean writable= dependent.isEnabled(true);
                        if (writable) {
                            event.doit= false;
                            return;
                        }
                    } else if (action.isEnabled()) {
                        event.doit= false;
                        action.run();
                        return;
                    }
                }
            }
		    
		    //TODO
//		    EBindingService adapter ;
//			if (adapter != null) {
//				int accel = SWTKeySupport
//						.convertEventToUnmodifiedAccelerator(e);
//				KeyStroke stroke = SWTKeySupport
//						.convertAcceleratorToKeyStroke(accel);
//				KeySequence seq = KeySequence.getInstance(stroke);
//				Binding bind = adapter.getPerfectMatch(seq);
//				if (bind != null) {
//					ParameterizedCommand command = bind
//							.getParameterizedCommand();
//					if (command != null) {
//						String cmdId = command.getId();
//						if (cmdId != null
//								&& cmdId
//										.equals("org.eclipse.ui.edit.findReplace")) { //$NON-NLS-1$
//							RichTextEditor.this.richText.getFindReplaceAction().execute(RichTextEditor.this);
//						}
//					}
//				}
//			}
		}
		@Override
        public void keyReleased(KeyEvent e) {
		    // ignore
		}
	};

    /**
     * Focus listeners.
     */
    @objid ("3f64ca1b-f6d5-4cd4-9359-70f3e7a08474")
     List<FocusListener> focusListeners = new ArrayList<>();

    /**
     * Focus listener calling registered focus listeners.
     */
    @objid ("35f9ae8b-a59c-4167-ae3d-d60f93936ed5")
    private Listener richTextFocusInListener = new Listener() {
        
        @Override
        public void handleEvent(Event event) {
            FocusEvent fev = new FocusEvent(event);
            for (FocusListener fl : RichTextEditor.this.focusListeners)
                fl.focusGained(fev);
        }
    };

    /**
     * Focus listener calling registered focus listeners.
     */
    @objid ("56baf0df-6df6-4940-9e76-b012bfd30eef")
    private Listener richTextFocusOutListener = new Listener() {
        
        @Override
        public void handleEvent(Event event) {
            if (! event.widget.isDisposed()) {
                FocusEvent fev = new FocusEvent(event);
                for (FocusListener fl : RichTextEditor.this.focusListeners)
                    fl.focusLost(fev);
            }
        }
    };

    /**
     * Creates a new instance.
     * @see SWT#BORDER
     * @see SWT#FLAT
     * @see ViewForm#getStyle()
     * @param parent the parent composite
     * @param style the editor style
     */
    @objid ("65d861e2-9914-4d1f-863d-6b45669558fd")
    public RichTextEditor(Composite parent, int style) {
        this(parent, style, null);
    }

    /**
     * Creates a new instance.
     * @see SWT#BORDER
     * @see SWT#FLAT
     * @see ViewForm#getStyle()
     * @param parent the parent composite
     * @param style the editor style
     * @param basePath the base path used for resolving links
     */
    @objid ("0b8ab1ba-2429-4221-b150-7e7b5b794979")
    public RichTextEditor(Composite parent, int style, String basePath) {
        this.basePath = basePath;
        this.debug = HtmlTextPlugin.getDefault().isDebugging();
        this.logger = HtmlTextPlugin.getDefault().getLogger();
        init(parent, style);
    }

    /**
     * Adds the listener to the collection of listeners who will be notifed when
     * this editor is disposed.
     * @param listener the listener which should be notified
     */
    @objid ("23563a96-c706-4edc-80a0-822ffc8a3c70")
    @Override
    public void addDisposeListener(DisposeListener listener) {
        if (this.richText != null) {
            this.richText.addDisposeListener(listener);
        }
    }

    /**
     * Adds a listener to the collection of listeners who will be notified when
     * help events are generated for this editor.
     * @param listener the listener which should be notified
     */
    @objid ("4c7e6a1d-88f6-46cc-8135-a04764651cdf")
    @Override
    public void addHelpListener(HelpListener listener) {
        if (this.richText != null) {
            this.richText.addHelpListener(listener);
        }
    }

    /**
     * Inserts text at the selection (overwriting the selection).
     * @param text the HTML(?) text to add
     */
    @objid ("19942ccb-d993-43d4-b8fd-563b61858411")
    public void addHTML(String text) {
        if (text == null || text.length() == 0) 
            return;
        if (this.tabFolder.getSelection() == this.richTextTab) {
            //To avoid encoding of javascript
            String text2 = text;
            text2 = text2.replaceAll("&", "&amp;");  //$NON-NLS-1$//$NON-NLS-2$
            executeCommand(IRichTextCommands.ADD_HTML, text);
        } else if (this.tabFolder.getSelection() == this.htmlTab) {
            String oldHTML = getSourceEdit().getText();
            Point sel = this.sourceViewer.getSelectedRange();
            int selStartIndex = sel.x;
            int selEndIndex = sel.x + sel.y - 1;
            String newHTML = oldHTML.substring(0, selStartIndex) + text
                    + oldHTML.substring(selEndIndex + 1);
            removeModifyListeners();
            this.currentDoc.set(newHTML);
            addModifyListeners();
            updateRichText(newHTML);
        }
    }

    /**
     * Inserts an image at the selection (overwriting the selection).
     * @param imageURL image URL
     * @param height image height
     * @param width image width
     * @param altTag alternative text
     */
    @objid ("c122c990-bdaf-47bf-b826-ef9e3f805824")
    public void addImage(String imageURL, String height, String width, String altTag) {
        if (this.tabFolder.getSelection() == this.richTextTab) {
            executeCommand(
                    IRichTextCommands.ADD_IMAGE,
                    new String[] {
                            imageURL,
                            height, width, altTag });
        } else if (this.tabFolder.getSelection() == this.htmlTab) {
            StringBuffer imageLink = new StringBuffer();
            // order of these attributes is the same as JTidy'ed HTML
            imageLink.append("<img"); //$NON-NLS-1$
            if (height.length() > 0) {
                imageLink.append(" height=\"" + height + "\""); //$NON-NLS-1$ //$NON-NLS-2$
            }
            if (altTag.length() > 0) {
                imageLink.append(" alt=\"" + altTag + "\""); //$NON-NLS-1$ //$NON-NLS-2$
            }
            imageLink.append(" src=\"" + imageURL + "\""); //$NON-NLS-1$ //$NON-NLS-2$
            if (width.length() > 0) {
                imageLink.append(" width=\"" + width + "\""); //$NON-NLS-1$ //$NON-NLS-2$
            }
            imageLink.append(" />"); //$NON-NLS-1$
            String oldHTML = getSourceEdit().getText();
            Point sel = this.sourceViewer.getSelectedRange();
            int selStartIndex = sel.x;
            int selEndIndex = sel.x + sel.y - 1;
            String newHTML = oldHTML.substring(0, selStartIndex) + imageLink.toString()
                    + oldHTML.substring(selEndIndex + 1);
            removeModifyListeners();
            this.currentDoc.set(newHTML);
            addModifyListeners();
            updateRichText(newHTML);
        }
    }

    /**
     * Adds a listener to the collection of listeners who will be notified when
     * keys are pressed and released within this editor.
     * @param listener the listener which should be notified
     */
    @objid ("082bcc95-9fa7-4857-961f-0d07a302c304")
    @Override
    public void addKeyListener(KeyListener listener) {
        if (this.richText != null) {
            this.richText.addKeyListener(listener);
        }
    }

    /**
     * Adds the listener to the collection of listeners who will be notifed when
     * an event of the given type occurs within this editor.
     * @param eventType the type of event to listen for
     * @param listener the listener which should be notified when the event occurs
     */
    @objid ("90a80c31-8196-4641-a897-370acf016046")
    @Override
    public void addListener(int eventType, Listener listener) {
        if (this.richText != null) {
            this.richText.addListener(eventType, listener);
        }
    }

    /**
     * Adds a listener to the collection of listeners who will be notified when
     * the content of this editor is modified.
     * @param listener the listener which should be notified
     */
    @objid ("06e791cd-c6fa-4d68-81bc-42575b4279f0")
    @Override
    public void addModifyListener(ModifyListener listener) {
        if (this.richText != null) {
            this.richText.addModifyListener(listener);
        }
    }

/*
     * (non-Javadoc)
     * @see org.eclipse.epf.richtext.IRichText#checkModify()
     */
    @objid ("7b369521-9343-4cbc-b6f7-81cae2d7679a")
    @Override
    public void checkModify() {
        this.richText.checkModify();
        if (this.sourceModified) {
            notifyModifyListeners();
        }
        if (this.debug) {
            printDebugMessage("checkModify", "modified=" + this.sourceModified); //$NON-NLS-1$ //$NON-NLS-2$    
        }
    }

    /**
     * Disposes the operating system resources allocated by this editor.
     */
    @objid ("363e2f54-d0a8-4ea8-b90a-1aee574fdd7f")
    @Override
    public void dispose() {
        if (this.contextMenu != null && !this.contextMenu.isDisposed()) {
            this.contextMenu.dispose();
            this.contextMenu = null;
        }
        if (this.sourceEditDropTarget != null) {
            this.sourceEditDropTarget.dispose();
            this.sourceEditDropTarget = null;
        }
        if (this.fActivationCodeTrigger != null) {
            this.fActivationCodeTrigger.uninstall();
            this.fActivationCodeTrigger= null;
        }
        removeModifyListeners();
        if (getSourceEdit() != null) {
            getSourceEdit().removeListener(SWT.Deactivate, this.sourceEditDeactivateListener);
            getSourceEdit().removeKeyListener(this.sourceEditKeyListener);
            this.sourceEditDeactivateListener = null;
            this.sourceEditKeyListener = null;
        }
        
        if (this.sourceViewer != null) {
            this.sourceViewer= null;
        }
        
        if (this.fActions != null) {
            this.fActions.clear();
            this.fActions= null;
        }
        
        if (this.richText != null) {
            this.richText.dispose();
            this.richText = null;
        }
    }

    /**
     * Executes the given rich text command. The supported command strings are
     * defined in <code>RichTextCommand<code>.
     * @param    command        a rich text command string
     * @return a status code returned by the executed command
     */
    @objid ("cbe80378-29e4-4706-96c7-89d120f2e41d")
    @Override
    public int executeCommand(String command) {
        if (this.richText != null) {
            return this.richText.executeCommand(command);
        }
        return 0;
    }

    /**
     * Executes the given rich text command with a single parameter. The
     * supported command strings are defined in <code>RichTextCommand<code>.
     * @param    command        a rich text command string
     * @param    param        a parameter for the command or <code>null</code>
     * @return a status code returned by the executed command
     */
    @objid ("bf40f7ab-b349-4f20-b530-608270094aef")
    @Override
    public int executeCommand(String command, String param) {
        if (this.richText != null) {
            return this.richText.executeCommand(command, param);
        }
        return 0;
    }

    /**
     * Executes the given rich text command with an array of parameters. The
     * supported command strings are defined in <code>RichTextCommand<code>.
     * @param    command        a rich text command string
     * @param    params        an array of parameters for the command or <code>null</code>
     * @return a status code returned by the executed command
     */
    @objid ("48d83c2d-4e79-4892-8a5d-d01d7d8bf04f")
    @Override
    public int executeCommand(String command, String[] params) {
        if (this.richText != null) {
            return this.richText.executeCommand(command, params);
        }
        return 0;
    }

    /**
     * Fills the tool bar with action items.
     * @param atoolBar a tool bar contain rich text actions
     */
    @objid ("3a9fba3f-e5c2-40ef-bd20-7eef73182ec3")
    @Override
    public void fillToolBar(IRichTextToolBar atoolBar) {
        fillToolBar(atoolBar, getRichTextControl());
    }

    /**
     * Populate actions in the Toolbar to link with the RichText
     * @param aToolBar The IRichTextToolBar
     * @param aRichText The IRichText
     */
    @objid ("83eb7589-5817-47b8-976e-4b27ff3a1591")
    private void fillToolBar(IRichTextToolBar aToolBar, IRichText aRichText) {
        //aToolBar.addAction(new BlockTagAction(aRichText));
        aToolBar.addAction(new FontStyleAction(aRichText));
        aToolBar.addAction(new FontNameAction(aRichText));
        aToolBar.addAction(new FontSizeAction(aRichText));
        aToolBar.addSeparator();
        aToolBar.addAction(new CutAction(aRichText));
        aToolBar.addAction(new CopyAction(aRichText));
        aToolBar.addAction(new PasteAction(aRichText));
        aToolBar.addSeparator();
        aToolBar.addAction(new ClearContentAction(aRichText));
        aToolBar.addSeparator();
        aToolBar.addAction(new BoldAction(aRichText));
        aToolBar.addAction(new ItalicAction(aRichText));
        aToolBar.addAction(new UnderlineAction(aRichText));
        aToolBar.addSeparator();
        aToolBar.addAction(new TextColorAction(aRichText));
        aToolBar.addAction(new TextHighlightAction(aRichText));
        aToolBar.addSeparator();
        aToolBar.addAction(new SubscriptAction(aRichText));
        aToolBar.addAction(new SuperscriptAction(aRichText));
        aToolBar.addSeparator();
        aToolBar.addAction(new TidyActionGroup(this));
        aToolBar.addSeparator();
        aToolBar.addAction(new AddOrderedListAction(aRichText));
        aToolBar.addAction(new AddUnorderedListAction(aRichText));
        aToolBar.addSeparator();
        aToolBar.addAction(new OutdentAction(aRichText));
        aToolBar.addAction(new IndentAction(aRichText));
        aToolBar.addSeparator();
        aToolBar.addAction(new JustifyLeftAction(aRichText));
        aToolBar.addAction(new JustifyCenterAction(aRichText));
        aToolBar.addAction(new JustifyRightAction(aRichText));
        aToolBar.addSeparator();
        aToolBar.addAction(new FindReplaceAction(aRichText)
        {
            /**
             * @see FindReplaceAction#execute(IRichText)
             */
            @Override
            public void execute(IRichText rText)
            {
                rText.getFindReplaceAction().execute(rText);
            }
        });
        aToolBar.addSeparator();
        aToolBar.addAction(new AddLinkAction(aRichText));
        //toolBar.addAction(new AddTopcasedLinkAction(richText));
        aToolBar.addAction(new AddImageAction(aRichText));
        aToolBar.addSeparator();
        aToolBar.addAction(new AddTableAction(aRichText));
        
        // Only add these actions when IE is used to render the Browser
        //if (Platform.getOS().equals("win32")) {
            aToolBar.addAction(new AddColumnAction(aRichText));
            aToolBar.addAction(new DeleteLastColumnAction(aRichText));
            aToolBar.addAction(new AddRowAction(aRichText));
            aToolBar.addAction(new DeleteLastRowAction(aRichText));
        //}
    }

    /**
     * @see org.eclipse.ui.ITextEditor#getAction(String)
     */
    @objid ("2e57629f-a633-44eb-831d-201771cc9530")
    @SuppressWarnings("javadoc")
    public IAction getAction(String actionID) {
        assert actionID != null;
        IAction action= this.fActions.get(actionID);
        //TODO
        //        if (action == null) {
        //            action= findContributedAction(actionID);
        //            if (action != null)
        //                setAction(actionID, action);
        //        }
        return action;
    }

    /**
     * Returns the base path used for resolving text and image links.
     * @return the base path used for resolving links specified with <href>,
     * <img>, etc.
     */
    @objid ("17f67a04-7cb2-4b72-a445-aac4bf7f8b0f")
    @Override
    public String getBasePath() {
        return this.basePath;
    }

    /**
     * Returns the form control.
     * @return the form control
     */
    @objid ("af4af99f-feb9-4ff8-8a8e-4219aa5c5412")
    @Override
    public Control getControl() {
        return this.form;
    }

    /**
     * Returns the base URL of the rich text control whose content was last
     * copied to the clipboard.
     * @return the base URL of a rich text control
     */
    @objid ("73b0f073-86fa-4aa4-8e51-daa87d93db6a")
    @Override
    public URL getCopyURL() {
        if (this.richText != null) {
            return this.richText.getCopyURL();
        }
        return null;
    }

    /**
     * Returns an application specific property value.
     * @param key the name of the property
     * @return the value of the property or <code>null</code> if it has not
     * been set
     */
    @objid ("ddab0e38-72aa-4a6d-a666-1a6ae0d79b03")
    @Override
    public Object getData(String key) {
        if (this.richText != null) {
            return this.richText.getData(key);
        }
        return null;
    }

    /**
     * Returns the editable state.
     * @return <code>true</code> if the content can be edited
     */
    @objid ("4b26eb8c-894c-4670-ab01-2ac058bcad33")
    @Override
    public boolean getEditable() {
        return this.editable;
    }

    @objid ("6075d033-61e2-4b4b-b4d4-3047ad8cdf31")
    @Override
    public FindReplaceAction getFindReplaceAction() {
        return this.richText.getFindReplaceAction();
    }

    /**
     * Returns the layout data.
     * @return the editor's layout data
     */
    @objid ("65f19431-6fad-4755-a42a-a8f506886431")
    @Override
    public Object getLayoutData() {
        if (this.form != null) {
            return this.form.getLayoutData();
        }
        return null;
    }

    /**
     * Returns the event listeners attached to this editor.
     * @return an iterator for retrieving the event listeners attached to this
     * editor
     */
    @objid ("433f3533-cbfa-4b38-bb19-199e2c5d28e3")
    @Override
    public Iterator<RichTextListener> getListeners() {
        if (this.richText != null) {
            return this.richText.getListeners();
        }
        return null;
    }

    /**
     * Checks whether the content has been modified.
     * @return <code>true</code> if the content has been modified
     */
    @objid ("4dd15fa2-5fea-4f88-a517-cf8430a8b577")
    @Override
    public boolean getModified() {
        if (this.richText != null) {
            return this.richText.getModified();
        }
        return false;
    }

    /**
     * Returns the modify listeners attached to this editor.
     * @return an iterator for retrieving the modify listeners
     */
    @objid ("5e28605f-abf4-4c82-99d8-25c572ca7e94")
    @Override
    public Iterator<ModifyListener> getModifyListeners() {
        if (this.richText != null) {
            this.richText.getModifyListeners();
        }
        return null;
    }

    /**
     * Returns the rich text control embedded within this editor.
     * @return the rich text control.
     */
    @objid ("3c30baed-1835-498a-9105-baabefe6cd7d")
    public IRichText getRichTextControl() {
        return this.richText;
    }

/*
     * (non-Javadoc)
     * @see org.eclipse.epf.richtext.IRichText#getSelected()
     */
    @objid ("06338846-adbe-4630-baa6-e7d543b173f0")
    @Override
    public RichTextSelection getSelected() {
        if (this.tabFolder.getSelection() == this.htmlTab) {
            String HTMLsource = getSourceEdit().getText();
            Point sel = this.sourceViewer.getSelectedRange();
            int selStartIndex = sel.x;
            int selEndIndex = sel.x + sel.y - 1;
            this.richText.getSelected().clear();
            this.richText.getSelected().setText(HTMLsource.substring(selStartIndex, selEndIndex + 1));
        }
        return this.richText.getSelected();
    }

    /**
     * Returns the HTML source edit control.
     * @return a <code>StyleText</code> object.
     */
    @objid ("161d6d6f-0826-4477-a6fb-ce5aa69ebb6a")
    public StyledText getSourceEdit() {
        if (this.sourceViewer != null) {
            return this.sourceViewer.getTextWidget();
        }
        return null;
    }

    /**
     * Returns the rich text content.
     * @return the rich text content formatted in XHTML
     */
    @objid ("ebbedaf2-222a-4228-8725-5cc59a6ba046")
    @Override
    public String getText() {
        if (this.sourceModified) {
            setText(getSourceEdit().getText());
            setModified(true);
            this.sourceModified = false;
        }
        if (this.richText != null) {
            return this.richText.getText();
        }
        return ""; //$NON-NLS-1$
    }

    /**
     * Checks whether this editor has focus.
     * @return <code>true</code> if this editor has the user-interface focus
     */
    @objid ("77317202-e89f-4e97-9bc1-140c6b31b302")
    @Override
    public boolean hasFocus() {
        if (this.richText != null) {
            return this.richText.hasFocus();
        }
        return false;
    }

    /**
     * Checks whether this control has been disposed.
     * @return <code>true</code> if this control is disposed successfully
     */
    @objid ("d70cee91-8add-45d5-92eb-4cc86675924a")
    @Override
    public boolean isDisposed() {
        if (this.richText != null) {
            return this.richText.isDisposed();
        }
        return true;
    }

    /**
     * Checks whether the HTML tab is selected.
     * @return <code>true</code> if the HTML tab is selected.
     */
    @objid ("c6a2c032-8b1c-47b9-ae0f-e7c0f4912811")
    public boolean isHTMLTabSelected() {
        return (this.tabFolder.getSelection() == this.htmlTab);
    }

    /**
     * Notifies the modify listeners that the rich text editor content has
     * changed.
     */
    @objid ("a6d7931b-28a6-4ba4-873f-85c36c37a877")
    @Override
    public void notifyModifyListeners() {
        if (this.richText != null) {
            Event event = new Event();
            event.display = Display.getCurrent();
            event.widget = this.richText.getControl();
        
            for (Iterator<ModifyListener> i = getModifyListeners(); i != null && i.hasNext();) {
                ModifyListener listener = i.next();
                listener.modifyText(new ModifyEvent(event));
            }
        }
    }

    /**
     * Removes a listener from the collection of listeners who will be notified
     * when this editor is disposed.
     * @param listener the listener which should no longer be notified
     */
    @objid ("fd49317e-c779-4980-baf5-46bca7104c81")
    @Override
    public void removeDisposeListener(DisposeListener listener) {
        if (this.richText != null) {
            this.richText.removeDisposeListener(listener);
        }
    }

    /**
     * Removes a listener from the collection of listeners who will be notified
     * when help events are generated for this editor.
     * @param listener the listener which should no longer be notified
     */
    @objid ("3d6ab9d2-9ede-4cce-ae1c-0cbd7feb424e")
    @Override
    public void removeHelpListener(HelpListener listener) {
        if (this.richText != null) {
            this.richText.removeHelpListener(listener);
        }
    }

    /**
     * Removes a listener from the collection of listeners who will be notified
     * when keys are pressed and released within this editor.
     * @param listener the listener which should no longer be notified
     */
    @objid ("fd049a0b-ad5d-4c16-ad5e-0b222c20514f")
    @Override
    public void removeKeyListener(KeyListener listener) {
        if (this.richText != null) {
            this.richText.removeKeyListener(listener);
        }
    }

    /**
     * Removes the listener from the collection of listeners who will be notifed
     * when an event of the given type occurs within this editor.
     * @param eventType the type of event to listen for
     * @param listener the listener which should no longer be notified when the event
     * occurs
     */
    @objid ("62e22686-274c-4ba2-a3c2-e39f9e354e4a")
    @Override
    public void removeListener(int eventType, Listener listener) {
        if (this.richText != null) {
            this.richText.removeListener(eventType, listener);
        }
    }

    /**
     * Removes a listener from the collection of listeners who will be notified
     * when the content of this editor is modified.
     * @param listener the listener which should no longer be notified
     */
    @objid ("aa5992c2-6dfb-41c6-88d7-07e7c88c8172")
    @Override
    public void removeModifyListener(ModifyListener listener) {
        if (this.richText != null) {
            this.richText.removeModifyListener(listener);
        }
    }

    /**
     * Restores the rich text content back to the initial value.
     */
    @objid ("601ed7ff-678e-4a03-8732-7306720ec1b9")
    @Override
    public void restoreText() {
        if (this.richText != null) {
            this.richText.restoreText();
        }
    }

    /**
     * @see org.eclipse.ui.texteditor.ITextEditor#setAction(String, IAction)
     * @param actionID the action id
     * @param action the action, or null to clear it
     */
    @objid ("4e090127-ed59-424e-8f2d-6e0b530a83fd")
    public void setAction(String actionID, IAction action) {
        assert actionID != null;
        IAction action2 = action;
        if (action2 == null) {
            action2 = this.fActions.remove(actionID);
            if (action2 != null)
                this.fActivationCodeTrigger.unregisterActionFromKeyActivation(action2);
        } else {
            this.fActions.put(actionID, action2);
            this.fActivationCodeTrigger.registerActionForKeyActivation(action2);
        }
    }

    /**
     * Tells the control it does not have focus.
     */
    @objid ("7ffaf7ab-6e67-4f65-ac0b-3c0bb42d6040")
    @Override
    public void setBlur() {
        if (this.richText != null) {
            this.richText.setBlur();
        }
    }

    /**
     * Sets the base URL of the rich text control whose content was last copied
     * to the clipboard.
     */
    @objid ("3cda633c-5fb9-4b85-bd1e-3feac761e9cc")
    @Override
    public void setCopyURL() {
        if (this.richText != null) {
            this.richText.setCopyURL();
        }
    }

    /**
     * Sets an application specific property name and value.
     * @param key the name of the property
     * @param value the new value for the property
     */
    @objid ("ef452ce2-54e1-465a-8a15-98ae55244bcb")
    @Override
    public void setData(String key, Object value) {
        if (this.richText != null) {
            this.richText.setData(key, value);
        }
    }

    /**
     * Sets the editable state.
     * @param editable the editable state
     */
    @objid ("821e6d83-b64a-4352-aa68-8a751f2d40db")
    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
        if (this.toolBar != null && this.tabFolder != null) {
            this.toolBar.updateToolBar(editable);
        }
        if (this.richText != null) {
            this.richText.setEditable(editable);
        }
        if (this.sourceViewer != null) {
            this.sourceViewer.setEditable(editable);
        }
    }

    @objid ("9db0902e-eb85-4739-b7a6-61e90ac55519")
    @Override
    public void setFindReplaceAction(FindReplaceAction findReplaceAction) {
        if (this.richText != null) {
            this.richText.setFindReplaceAction(findReplaceAction);
            this.richText.getFindReplaceAction().setRichText(this);
        
        }
    }

    /**
     * Sets focus to this editor.
     */
    @objid ("debd86a8-c0f0-4e5f-a873-a2e026b2c7fd")
    @Override
    public void setFocus() {
        if (this.richText != null) {
            this.richText.setFocus();
        }
        setSelection(0);
        if (this.toolBar != null && this.tabFolder != null) {
            this.toolBar.updateToolBar(this.editable);
        }
    }

    @objid ("c157e2d5-d092-4765-abc4-9bb9742f6829")
    @Override
    public void setInitialText(String text) {
        if (this.richText != null) {
            this.richText.setInitialText(text);
        }
        if (getSourceEdit() != null) {
            removeModifyListeners();
            setDocument(new Document(text));
            addModifyListeners();
        }
    }

    /**
     * Sets the layout data.
     * @param layoutData the layout data to set
     */
    @objid ("6b029410-a5a4-4267-9ea8-1a3fe574030b")
    @Override
    public void setLayoutData(Object layoutData) {
        if (this.form != null) {
            this.form.setLayoutData(layoutData);
        }
    }

    /**
     * Sets the modified state.
     * @param modified the modified state
     */
    @objid ("2c336169-6f0c-4819-ba9a-fc1fa836ab21")
    @Override
    public void setModified(boolean modified) {
        if (this.richText != null) {
            this.richText.setModified(modified);
        }
    }

    /**
     * Selects the Rich Text or HTML tab.
     * @param index <code>0</code> for the Rich Text tab, <code>1</code> for
     * the HTML tab.
     */
    @objid ("35661312-74f0-4721-8674-c0d8425eb67c")
    @Override
    public void setSelection(int index) {
        if (this.tabFolder != null) {
            this.tabFolder.setSelection(index);
        }
    }

    /**
     * Sets the rich text content.
     * @param text the rich text content in XHTML format
     */
    @objid ("08cca149-b750-4c7d-96eb-a235c8a477ee")
    @Override
    public void setText(String text) {
        if (this.richText != null) {
            this.richText.setText(text);
        }
        
        this.sourceModified = false;
        
        if (this.tabFolder != null) {
            if (this.toolBar != null) {
                this.toolBar.updateToolBar(this.editable);
            }
            if (getSourceEdit() != null) {
                removeModifyListeners();
                this.currentDoc.set(text);
                addModifyListeners();
            }
        }
    }

    @objid ("16419097-f886-486f-a6e4-56ff0198c849")
    private void addDropSupportToStyledText() {
        // this function is based heavily on the example at:
        // http://www.eclipse.org/articles/Article-SWT-DND/DND-in-SWT.html
        
        // Allow data to be copied to the drop target
        int operations = DND.DROP_MOVE |  DND.DROP_COPY | DND.DROP_DEFAULT;
        this.sourceEditDropTarget = new DropTarget(getSourceEdit(), operations);
        
        // Receive data in Text or HTML format
        final TextTransfer textTransfer = TextTransfer.getInstance();
        final HTMLTransfer htmlTransfer = HTMLTransfer.getInstance();
        Transfer[] types = new Transfer[] {htmlTransfer, textTransfer};
        this.sourceEditDropTarget.setTransfer(types);
        
        this.sourceEditDropTarget.addDropListener(new DropTargetListener() {
            @Override
            public void dragEnter(DropTargetEvent event) {
                if (event.detail == DND.DROP_DEFAULT) {
                    if ((event.operations & DND.DROP_COPY) != 0) {
                        event.detail = DND.DROP_COPY;
                    } else {
                        event.detail = DND.DROP_NONE;
                    }
                }
                if (!getEditable()) {
                    event.detail = DND.DROP_NONE;
                }
                // will accept text but prefer to have HTML dropped
                for (int i = 0; i < event.dataTypes.length; i++) {
                    if (htmlTransfer.isSupportedType(event.dataTypes[i])){
                        event.currentDataType = event.dataTypes[i];
                        break;
                    }
                }
            }
            @Override
            public void dragLeave(DropTargetEvent event) {
                // ignore
            }
            @Override
            public void dragOperationChanged(DropTargetEvent event) {
                if (event.detail == DND.DROP_DEFAULT) {
                    if ((event.operations & DND.DROP_COPY) != 0) {
                        event.detail = DND.DROP_COPY;
                    } else {
                        event.detail = DND.DROP_NONE;
                    }
                }
            }
            @Override
            public void dragOver(DropTargetEvent event) {
                event.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_INSERT_AFTER | DND.FEEDBACK_SCROLL;
            }
            @Override
            public void drop(DropTargetEvent event) {
                if (textTransfer.isSupportedType(event.currentDataType) || 
                        htmlTransfer.isSupportedType(event.currentDataType)) {
                    String text = (String)event.data;
                    addHTML(text);
                }
            }
            @Override
            public void dropAccept(DropTargetEvent event) {
                // ignore
            }
        });
    }

    /**
     * from org.eclipse.ui.texteditor.AbstractTextEditor#getUndoContext()
     * Returns this editor's viewer's undo manager undo context.
     * @return the undo context or <code>null</code> if not available
     * @since 3.1
     */
    @objid ("6fc536aa-1e8e-4f54-860c-637426b32711")
    private IUndoContext getUndoContext() {
        if (this.sourceViewer != null) {
            IUndoManager iundoManager= this.sourceViewer.getUndoManager();
            if (iundoManager instanceof IUndoManagerExtension)
                return ((IUndoManagerExtension)iundoManager).getUndoContext();
        }
        return null;
    }

    /**
     * Initializes the activation code trigger.
     * @since 2.1
     */
    @objid ("554c1a73-3865-4569-93b5-9e656934e237")
    private void initializeActivationCodeTrigger() {
        this.fActivationCodeTrigger.install();
        this.fActivationCodeTrigger.setScopes(this.fKeyBindingScopes);
    }

    /**
     * Displays the given debug message to the console.
     */
    @objid ("feb7411a-71ae-413a-92d8-b19b137c0902")
    private void printDebugMessage(String method, String msg) {
        printDebugMessage(method, msg, null);
    }

    /**
     * Displays the given debug message to the console.
     */
    @objid ("d9971142-96d7-489c-971a-266580f9d083")
    private void printDebugMessage(String method, String msg, String text) {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("RichTextEditor[").append(this.richText.getControl().handle).append(']') //$NON-NLS-1$
                .append('.').append(method);
        if (msg != null && msg.length() > 0) {
            strBuf.append(": ").append(msg); //$NON-NLS-1$
        }
        if (text != null && text.length() > 0) {
            strBuf.append('\n').append(text);
        }
        System.out.println(strBuf);
    }

    /**
     * Registers the given undo/redo action under the given ID and
     * ensures that previously installed actions get disposed. It
     * also takes care of re-registering the new action with the
     * global action handler.
     * @param actionId the action id under which to register the action
     * @param action the action to register
     * @since 3.1
     */
    @objid ("72e3aa78-0af6-4785-be7a-b40d4ecedb25")
    private void registerAction(String actionId, IAction action) {
        IAction oldAction= getAction(actionId);
        if (oldAction instanceof OperationHistoryActionHandler)
            ((OperationHistoryActionHandler)oldAction).dispose();
        
        setAction(actionId, action);
        
        //TODO
        //        IActionBars actionBars= getEditorSite().getActionBars();
        //        if (actionBars != null)
        //            actionBars.setGlobalActionHandler(actionId, action);
    }

    @objid ("e66023c9-a845-44f8-8857-82213b2fa964")
    private void setDocument(IDocument doc) {
        IDocument doc2 = doc;
        if (doc2 == null) {
            doc2 = new Document();
        }
        
        // clean up old doc
        this.undoManager.disconnect();
        IDocument oldDoc = this.sourceViewer.getDocument();
        if (oldDoc != null) {
            oldDoc.removeDocumentListener(this.sourceEditDocumentListener);
        }
        
        // hook up new doc
        this.currentDoc = doc2;
        this.sourceViewer.setDocument(this.currentDoc);
        this.currentDoc.addDocumentListener(this.sourceEditDocumentListener);
        this.undoManager.connect(this.sourceViewer);
        if (this.undoAction != null) {
            this.undoAction.setContext(getUndoContext());
        }
        if (this.redoAction != null) {
            this.redoAction.setContext(getUndoContext());
        }
    }

    /**
     * Updates the content of the rich text control without updating the HTML
     * source editor.
     * <p>
     * This method should be called by the HTML source editor to sync up its
     * content with the rich text control.
     * @param text the rich text content in XHTML format
     */
    @objid ("9eb5e8a1-be2e-4435-838b-be72ca6b304c")
    private void updateRichText(String text) {
        if (this.richText != null) {
            this.richText.setText(text);
            this.richText.checkModify();
        }
        this.sourceModified = false;
        if (this.tabFolder != null) {
            if (this.toolBar != null) {
                this.toolBar.updateToolBar(this.editable);
            }
        }
    }

    @objid ("52514b77-d6be-4090-a844-31ff26d4918f")
    protected void addModifyListeners() {
        if (this.currentDoc != null) {
            this.currentDoc.addDocumentListener(this.sourceEditDocumentListener);
        }
    }

    @objid ("11dba1a5-ebce-409e-8840-bbcc085f8399")
    protected void createActions() {
        createUndoRedoActions();
        
        
        // select all
        Action selectAllAction = new Action() {
            @Override
            public void run() {
                getSourceEdit().selectAll();
            }
        };
        selectAllAction.setActionDefinitionId(IWorkbenchCommandConstants.EDIT_SELECT_ALL);
        registerAction(ActionFactory.SELECT_ALL.getId(), selectAllAction);
    }

    /**
     * Creates the editor tab folder.
     * @param parent the parent control
     * @param style the style for the control
     * @return a new editor toolbar
     */
    @objid ("11bca4da-80ee-4326-ab5b-52260252702a")
    protected CTabFolder createEditorTabFolder(Composite parent, int style) {
        CTabFolder folder = new CTabFolder(parent, SWT.FLAT | SWT.BOTTOM);
        folder.setLayout(new GridLayout(1, true));
        folder.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        Composite richTextComposite = new Composite(folder, SWT.FLAT);
        GridLayout richTextCompositeLayout = new GridLayout(1, false);
        richTextCompositeLayout.marginHeight = 0;
        richTextCompositeLayout.marginWidth = 0;
        richTextComposite.setLayout(richTextCompositeLayout);
        richTextComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        this.richText = createRichTextControl(richTextComposite, style, this.basePath);
        this.richText.setData(PROPERTY_NAME, this);
        this.richText.getFindReplaceAction().setRichText(this);
        
        this.richTextTab = new CTabItem(folder, SWT.FLAT);
        this.richTextTab.setText(HtmlTextResources.richTextTab_text);
        this.richTextTab.setToolTipText(HtmlTextResources.richTextTab_toolTipText);
        this.richTextTab.setControl(richTextComposite);
        
        Composite htmlComposite = new Composite(folder, SWT.FLAT);
        htmlComposite.setLayout(new FillLayout());
        
        this.sourceViewer = new TextViewer(htmlComposite, SWT.FLAT | SWT.MULTI
                | SWT.WRAP | SWT.V_SCROLL);
        this.sourceViewer.setUndoManager(this.undoManager);
        setDocument(null);
        addModifyListeners();
        getSourceEdit().addListener(SWT.Deactivate, this.sourceEditDeactivateListener);
        getSourceEdit().addKeyListener(this.sourceEditKeyListener);
        this.richText.addListener(SWT.FocusIn, this.richTextFocusInListener);
        this.richText.addListener(SWT.FocusOut, this.richTextFocusOutListener);
        getSourceEdit().addListener(SWT.FocusIn, this.richTextFocusInListener);
        getSourceEdit().addListener(SWT.FocusOut, this.richTextFocusOutListener);
        
        this.contextMenu = new Menu(parent.getShell(), SWT.POP_UP);
        getSourceEdit().setMenu(this.contextMenu);
        
        // FIXME! This opens up a can of worms, especially with DBCS characters.
        // See https://bugs.eclipse.org/bugs/show_bug.cgi?id=179432. 
        //addDropSupportToStyledText();
        
        fillContextMenu(this.contextMenu);
        
        
        this.htmlTab = new CTabItem(folder, SWT.NONE);
        this.htmlTab.setText(HTML_TAB_NAME);
        this.htmlTab.setToolTipText(HtmlTextResources.htmlTab_toolTipText); 
        this.htmlTab.setControl(htmlComposite);
        
        folder.addSelectionListener(new SelectionAdapter() {
            @SuppressWarnings("synthetic-access")
            @Override
            public void widgetSelected(SelectionEvent event) {
                CTabItem item = (CTabItem) event.item;
                if (item.getText().equals(HTML_TAB_NAME)) {
                    removeModifyListeners();
                    RichTextEditor.this.currentDoc.set(getText());
                    RichTextEditor.this.sourceModified = false;
                    addModifyListeners();
                    if (RichTextEditor.this.toolBar != null) {
                        RichTextEditor.this.toolBar.updateToolBar(RichTextEditor.this.editable);
                    }
                } else {
                    updateRichText(getSourceEdit().getText());
                    setModified(true);
                    if (RichTextEditor.this.toolBar != null) {
                        RichTextEditor.this.toolBar.updateToolBar(RichTextEditor.this.editable);
                    }
                }
            }
        });
        fillToolBar(this.toolBar);
        
        initializeActivationCodeTrigger();
        createActions();
        
        folder.setSelection(0);
        return folder;
    }

    /**
     * Creates the underlying rich text control.
     * @param parent the parent composite
     * @param style the style for the control
     * @param abasePath the path used for resolving links
     */
    @objid ("ee0879c9-702f-4421-b3cd-37f057bac65e")
    protected RichText createRichTextControl(Composite parent, int style, String abasePath) {
        return new RichText(parent, style, abasePath);
    }

/*
     * from org.eclipse.ui.texteditor.AbstractTextEditor#createUndoRedoActions()
     */
    @objid ("f443a95f-0e97-42e5-a710-d04d3149ed33")
    protected void createUndoRedoActions() {
        IUndoContext undoContext= getUndoContext();
        if (undoContext != null) {
            // TODO Use actions provided by global undo/redo
            /*
            // Create the undo action
            this.undoAction= new UndoActionHandler(getEditorSite(), undoContext);
            PlatformUI.getWorkbench().getHelpSystem().setHelp(this.undoAction, IAbstractTextEditorHelpContextIds.UNDO_ACTION);
            this.undoAction.setActionDefinitionId(IWorkbenchCommandConstants.UNDO);
            registerAction(ITextEditorActionConstants.UNDO, this.undoAction);
        
            // Create the redo action.
            this.redoAction= new RedoActionHandler(getEditorSite(), undoContext);
            PlatformUI.getWorkbench().getHelpSystem().setHelp(this.redoAction, IAbstractTextEditorHelpContextIds.REDO_ACTION);
            this.redoAction.setActionDefinitionId(IWorkbenchCommandConstants.EDIT_REDO);
            registerAction(ITextEditorActionConstants.REDO, this.redoAction);
        */    
            }
        
        Action undoAction = new Action() {
            
            public void run() {
                RichTextEditor.this.sourceViewer.doOperation(ITextOperationTarget.UNDO);
            };
        };
        undoAction.setAccelerator(SWT.CTRL | 'Z');
        undoAction.setActionDefinitionId(IWorkbenchCommandConstants.EDIT_UNDO);
        undoAction.setText("Undo");
        undoAction.setToolTipText("Undo text edition");
        //        undoAction.setImageDescriptor(this.sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_UNDO));
        //        undoAction.setDisabledImageDescriptor(this.sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_UNDO_DISABLED));
        registerAction(ActionFactory.UNDO.getId(), undoAction);
        
        Action redoAction = new Action() {
            
            public void run() {
                RichTextEditor.this.sourceViewer.doOperation(ITextOperationTarget.UNDO);
            };
            
            @Override
            public boolean isEnabled() {
                // TODO Auto-generated method stub
                return RichTextEditor.this.sourceViewer.canDoOperation(ITextOperationTarget.UNDO);
            }
        };
        redoAction.setAccelerator(SWT.CTRL | 'Z');
        redoAction.setActionDefinitionId(IWorkbenchCommandConstants.EDIT_REDO);
        redoAction.setText("Undo");
        redoAction.setToolTipText("Undo text edition");
        //        redoAction.setImageDescriptor(this.sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_REDO));
        //        redoAction.setDisabledImageDescriptor(this.sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_REDO_DISABLED));
        registerAction(ActionFactory.REDO.getId(), redoAction);
    }

    /**
     * Fills the context menu with menu items.
     * @param aContextMenu a context menu containing rich text actions
     */
    @objid ("6f85aecb-3740-46c2-8d85-e4b9e5cda10c")
    protected void fillContextMenu(Menu aContextMenu) {
        final MenuItem cutItem = new MenuItem(aContextMenu, SWT.PUSH);
        cutItem.setText(HtmlTextResources.cutAction_text);
        cutItem.setImage(HtmlTextImages.IMG_CUT);
        cutItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                CutAction action = new CutAction(RichTextEditor.this);
                action.execute(RichTextEditor.this);
            }
        });
        final MenuItem copyItem = new MenuItem(aContextMenu, SWT.PUSH);
        copyItem.setText(HtmlTextResources.copyAction_text); 
        copyItem.setImage(HtmlTextImages.IMG_COPY);
        copyItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                CopyAction action = new CopyAction(RichTextEditor.this);
                action.execute(RichTextEditor.this);
            }
        });
        final MenuItem pasteItem = new MenuItem(aContextMenu, SWT.PUSH);
        pasteItem.setText(HtmlTextResources.pasteAction_text); 
        pasteItem.setImage(HtmlTextImages.IMG_PASTE);
        pasteItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                PasteAction action = new PasteAction(RichTextEditor.this);
                action.execute(RichTextEditor.this);
            }
        });
        
        final MenuItem pastePlainTextItem = new MenuItem(aContextMenu, SWT.PUSH);
        pastePlainTextItem.setText(HtmlTextResources.pastePlainTextAction_text);
        pastePlainTextItem.setImage(HtmlTextImages.IMG_PASTE_PLAIN_TEXT);
        pastePlainTextItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                PastePlainTextAction action = new PastePlainTextAction(RichTextEditor.this);
                action.execute(RichTextEditor.this);
            }
        });
        
        aContextMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuHidden(MenuEvent e) {
                // ignore
            }
        
            @Override
            public void menuShown(MenuEvent e) {
                Clipboard clipboard = new Clipboard(Display.getCurrent());
                String html = (String) clipboard.getContents(HTMLTransfer
                        .getInstance());
                String text = (String) clipboard.getContents(TextTransfer
                        .getInstance());
                String selectedText = getSelected().getText();
                boolean selection = selectedText.length() > 0;
                cutItem.setEnabled(RichTextEditor.this.editable && selection);
                copyItem.setEnabled(selection);
                pasteItem.setEnabled(RichTextEditor.this.editable && (html != null));
                pastePlainTextItem.setEnabled(RichTextEditor.this.editable && (text != null));
            }
        });
    }

    /**
     * Initializes this editor.
     * @see SWT#BORDER
     * @see SWT#FLAT
     * @see ViewForm#getStyle()
     * @param parent the parent composite
     * @param style the editor style
     */
    @objid ("a54269d7-1a15-4f43-9f84-9913b5cf2708")
    protected void init(Composite parent, int style) {
        try {
            this.form = new ViewForm(parent, style);
            this.form.marginHeight = 0;
            this.form.marginWidth = 0;
        
            this.toolBar = new RichTextToolBar(this.form, SWT.FLAT, this);
        
            this.content = new Composite(this.form, SWT.FLAT);
            GridLayout layout = new GridLayout();
            layout.marginHeight = 0;
            layout.marginWidth = 0;
            this.content.setLayout(layout);
        
            this.tabFolder = createEditorTabFolder(this.content, style);
        
            this.form.setTopCenter(((RichTextToolBar)this.toolBar).getToolbarMgr().getControl());
            this.form.setTopLeft(((RichTextToolBar)this.toolBar).getToolbarMgrCombo().getControl());
            this.form.setContent(this.content);
        } catch (Exception e) {
            this.logger.error(e);
        }
    }

    @objid ("992edaf4-d33a-4d52-bb11-2e4eddecb0bd")
    protected void removeModifyListeners() {
        if (this.currentDoc != null) {
            this.currentDoc.removeDocumentListener(this.sourceEditDocumentListener);
        }
    }

    /**
     * Add a focus listener.
     * @param listener a focus listener.
     */
    @objid ("ea6899e6-da33-4663-9a84-2452699ff6c8")
    public void addFocusListener(final FocusListener listener) {
        this.focusListeners.add(listener);
    }

    /**
     * Remove a focus listener.
     * @param listener a focus listener.
     */
    @objid ("34a4e8ba-9e2d-4199-9238-bdd7787a83ca")
    public void removeFocusListener(FocusListener listener) {
        this.focusListeners.remove(listener);
    }

    @objid ("f035bd23-33fc-46e3-a5c1-4999480d9418")
    public void selectAll() {
        getSourceEdit().selectAll();
        if (this.richText != null)
            this.richText.executeCommand(IRichTextCommands.SELECT_ALL);
    }

    /**
     * @return the editor toolbar.
     */
    @objid ("0d198203-bacc-4354-842d-c07e1e8c658c")
    public IRichTextToolBar getToolBar() {
        return this.toolBar;
    }

    @objid ("bec3ac1e-d67f-438b-bdbd-f92f6c49466b")
    @Override
    public String getTidyText() {
        if (this.sourceModified) {
            setText(getSourceEdit().getText());
            setModified(true);
            this.sourceModified = false;
        }
        if (this.richText != null) {
            return this.richText.getTidyText();
        }
        return ""; //$NON-NLS-1$
    }

    /**
     * Internal key verify listener for triggering action activation codes.
     */
    @objid ("07a0e3b5-1898-4d96-98cf-b2e01a16bd7e")
    class ActivationCodeTrigger implements VerifyKeyListener {
        /**
         * Indicates whether this trigger has been installed.
         */
        @objid ("68ef8cb4-adbf-4c8a-8f1b-382124ddf1dd")
        private boolean fIsInstalled = false;

        /**
         * The key binding service to use.
         * @since 2.0
         */
        @objid ("5dd3b965-349a-4807-bcee-87478507c1de")
        private EBindingService fKeyBindingService;

        /**
         * Installs this trigger on the editor's text widget.
         * @since 2.0
         */
        @objid ("c8bb3c86-39e6-4bcd-966f-00ec31b3cc79")
        @SuppressWarnings("cast")
        public void install() {
            if (!this.fIsInstalled) {
            
                if (RichTextEditor.this.sourceViewer instanceof ITextViewerExtension) {
                    ITextViewerExtension e= RichTextEditor.this.sourceViewer;
                    e.prependVerifyKeyListener(this);
                } else {
                    StyledText text= RichTextEditor.this.sourceViewer.getTextWidget();
                    text.addVerifyKeyListener(this);
                }
            
                //TODO this.fKeyBindingService= getEditorSite().getKeyBindingService();
                this.fIsInstalled= true;
            }
        }

        /**
         * Registers the given action for key activation.
         * @param action the action to be registered
         * @since 2.0
         */
        @objid ("080dd4a2-63b3-4b3e-80e3-00ec4e19a760")
        public void registerActionForKeyActivation(IAction action) {
            //TODO
            //            if (action.getActionDefinitionId() != null)
            //                this.fKeyBindingService.registerAction(action);
        }

        /**
         * Sets the key binding scopes for this editor.
         * @param keyBindingScopes the key binding scopes
         * @since 2.1
         */
        @objid ("dc666312-9870-4f0a-aefb-f54b9087fcd3")
        public void setScopes(String[] keyBindingScopes) {
            //            if (keyBindingScopes != null && keyBindingScopes.length > 0)
            //                this.fKeyBindingService.setScopes(keyBindingScopes);
        }

        /**
         * Uninstalls this trigger from the editor's text widget.
         * @since 2.0
         */
        @objid ("4ec91cb0-85b6-42d0-b5f5-f8e1258ac795")
        @SuppressWarnings({ "cast", "synthetic-access" })
        public void uninstall() {
            if (this.fIsInstalled) {
            
                if (RichTextEditor.this.sourceViewer instanceof ITextViewerExtension) {
                    ITextViewerExtension e= RichTextEditor.this.sourceViewer;
                    e.removeVerifyKeyListener(this);
                } else if (RichTextEditor.this.sourceViewer != null) {
                    StyledText text= RichTextEditor.this.sourceViewer.getTextWidget();
                    if (text != null && !text.isDisposed())
                        text.removeVerifyKeyListener(RichTextEditor.this.fActivationCodeTrigger);
                }
            
                this.fIsInstalled= false;
                //this.fKeyBindingService= null;
            }
        }

        /**
         * The given action is no longer available for key activation
         * @param action the action to be unregistered
         * @since 2.0
         */
        @objid ("da5b64b2-5e75-4cd0-9a48-073c0461bdb3")
        public void unregisterActionFromKeyActivation(IAction action) {
            //TODO
            //            if (action.getActionDefinitionId() != null)
            //                this.fKeyBindingService.unregisterAction(action);
        }

        @objid ("f20ca18b-2258-4db8-b879-a25461fe2312")
        @Override
        public void verifyKey(VerifyEvent event) {
            for (IAction action : RichTextEditor.this.fActions.values()) {
                if (action.getAccelerator() == event.keyCode) {
                    if (action instanceof IUpdate)
                        ((IUpdate) action).update();
            
                    if (!action.isEnabled() && action instanceof IReadOnlyDependent) {
                        IReadOnlyDependent dependent= (IReadOnlyDependent) action;
                        boolean writable= dependent.isEnabled(true);
                        if (writable) {
                            event.doit= false;
                            return;
                        }
                    } else if (action.isEnabled()) {
                        event.doit= false;
                        action.run();
                        return;
                    }
                }
            }
        }

    }

}
