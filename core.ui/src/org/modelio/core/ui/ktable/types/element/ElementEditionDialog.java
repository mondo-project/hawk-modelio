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
                                    

package org.modelio.core.ui.ktable.types.element;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.modelio.app.core.picking.IModelioPickingService;
import org.modelio.app.core.picking.IPickingClient;
import org.modelio.app.core.picking.IPickingSession;
import org.modelio.core.ui.dialog.ModelioDialog;
import org.modelio.core.ui.images.MetamodelImageService;
import org.modelio.core.ui.plugin.CoreUi;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityEdge;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.ModelTree;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.vcore.session.api.model.IModel;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.osgi.framework.Bundle;

/**
 * This class displays the results of a search for model elements (see Searcher class).
 * <p>
 * The found elements are proposed to the end user in a list where he can select the element of his choice. Each time the user
 * selects a different element in the list a NAVIGATION event is fired. Along with the results, the dialog displays the current
 * regular expression that produced the results. This expression can be modified leading to an update of the displayed results. This
 * allows for search refinement when many elements have been found.
 */
@objid ("8dbb4a26-c068-11e1-8c0a-002564c97630")
public class ElementEditionDialog extends ModelioDialog implements IPickingClient {
// TODO CHM implements IEditorDropClient
    @objid ("8dbb4a29-c068-11e1-8c0a-002564c97630")
    private List<MObject> content;

    @objid ("a49236a1-c068-11e1-8c0a-002564c97630")
    private final String fieldName;

    @objid ("8dbcd0ba-c068-11e1-8c0a-002564c97630")
    private final MObject editedElement;

    @objid ("8dbcd0bf-c068-11e1-8c0a-002564c97630")
    private boolean dialogActive = false;

    @objid ("c1a6a682-e49c-462c-b862-b802d165ceb1")
    private static final String HELP_TOPIC = "/org.modelio.documentation.modeler/html/Index.html";

    @objid ("8dbcd0aa-c068-11e1-8c0a-002564c97630")
    private Text textfield = null;

    @objid ("8dbcd0ab-c068-11e1-8c0a-002564c97630")
    private final Searcher searcher;

    @objid ("8dbcd0ad-c068-11e1-8c0a-002564c97630")
    private TableViewer searchResultTree = null;

    @objid ("8dbcd0ae-c068-11e1-8c0a-002564c97630")
    private TableViewer contentTree = null;

    @objid ("8dbcd0af-c068-11e1-8c0a-002564c97630")
    private final Class<? extends MObject> targetClass;

    @objid ("8dbcd0b3-c068-11e1-8c0a-002564c97630")
    private final IEditionValidator editionValidator;

// TODO CHM d&d
// //private EditorDropListener dropListener;
    @objid ("8dbcd0b5-c068-11e1-8c0a-002564c97630")
    private Image upImage;

    @objid ("8dbcd0b7-c068-11e1-8c0a-002564c97630")
    private Image downImage;

    @objid ("8dbcd0be-c068-11e1-8c0a-002564c97630")
    private MultipleElementCellEditor editor = null;

    @objid ("15faa785-16da-11e2-aa0d-002564c97630")
    private final IModelioPickingService pickingService;

    @objid ("15faa786-16da-11e2-aa0d-002564c97630")
    private IPickingSession pickingSession;

    @objid ("7c84e279-2208-4729-a983-b311c3a8539c")
    protected static ElementEditionDialog instance;

    @objid ("8dbcd0c0-c068-11e1-8c0a-002564c97630")
    private ElementEditionDialog(Shell parentShell, IModel session, IModelioPickingService pickingService, MultipleElementCellEditor editor, List<MObject> initialContent, IEditionValidator editionValidator) {
        super(parentShell);
        this.editedElement = editor.getEditedElement();
        this.fieldName = editor.getFieldName();
        this.targetClass = editor.getTargetClass();
        this.searcher = new Searcher(session, this.targetClass, editor.getElementFilter());
        this.content = new ArrayList<>(initialContent);
        this.editor = editor;
        this.editionValidator = editionValidator;
        this.pickingService = pickingService;
        
        if (this.targetClass.isArray()) {
            throw new IllegalArgumentException("Target class cannot be an array");
        }
        
        // Hack the shell style uglily set by ObjingDialog so that
        // the dialog is not modal.
        int style = getShellStyle();
        style &= ~(SWT.PRIMARY_MODAL | SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL);
        style |= SWT.MODELESS;
        setShellStyle(style);
    }

    @objid ("8dbcd0cd-c068-11e1-8c0a-002564c97630")
    @Override
    public void addButtonsInButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CLOSE_LABEL, false);
    }

    @objid ("8dbcd0d2-c068-11e1-8c0a-002564c97630")
    @Override
    public boolean close() {
        if (this.pickingSession != null) {
            this.pickingService.stopPicking(this.pickingSession);
            this.pickingSession = null;
        }
        
        if (this.upImage != null) {
            this.upImage.dispose();
            this.upImage = null;
        }
        if (this.downImage != null) {
            this.downImage.dispose();
            this.downImage = null;
        }
        if (this.equals(instance)) { // should always be the case, could be an assert!
            instance = null;
        }
        return super.close();
    }

    @objid ("8dbcd0d7-c068-11e1-8c0a-002564c97630")
    @Override
    public Control createContentArea(Composite parent) {
        final Composite composite = new Composite(parent, 0);
        composite.setLayout(new FormLayout());
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        composite.setFont(parent.getFont());
        
        // Search criteria group
        final Group searchCriteriaGroup = new Group(composite, SWT.NONE);
        
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        searchCriteriaGroup.setLayout(gridLayout);
        
        final FormData fd_searchCriteriaGroup = new FormData();
        fd_searchCriteriaGroup.top = new FormAttachment(0, 5);
        fd_searchCriteriaGroup.right = new FormAttachment(100, -5);
        fd_searchCriteriaGroup.left = new FormAttachment(0, 5);
        searchCriteriaGroup.setLayoutData(fd_searchCriteriaGroup);
        
        searchCriteriaGroup.setText(CoreUi.I18N.getString("KTable.Search"));
        
        // Name label
        final Label nameLabel = new Label(searchCriteriaGroup, SWT.NONE);
        nameLabel.setText(CoreUi.I18N.getString("KTable.Pattern"));
        
        // Text field right to the Name: label
        this.textfield = new Text(searchCriteriaGroup, SWT.SINGLE | SWT.BORDER);
        this.textfield.setText(this.searcher.getExpression());
        this.textfield.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        // Prevent CR from going to the default button
        this.textfield.addTraverseListener(new TraverseListener() {
            @Override
            public void keyTraversed(TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_RETURN) {
                    e.doit = false;
                    e.detail = SWT.TRAVERSE_NONE;
                }
            }
        });
        
        this.textfield.addSelectionListener(new SelectionListener() {
            @SuppressWarnings("synthetic-access")
            @Override
            public void widgetDefaultSelected(SelectionEvent event) {
                ElementEditionDialog.this.searcher.setExpression(ElementEditionDialog.this.textfield.getText());
                ElementEditionDialog.this.searchResultTree.setInput(ElementEditionDialog.this.searcher.search());
            }
        
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                // Nothing to do
            }
        });
        
        final Label resultsLabel = new Label(searchCriteriaGroup, SWT.NONE);
        
        resultsLabel.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, true, 2, 1));
        
        resultsLabel.setText(CoreUi.I18N.getString("KTable.SearchResults"));
        
        this.searchResultTree = new TableViewer(searchCriteriaGroup, SWT.BORDER);
        
        this.searchResultTree.getTable().setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, true, 2, 1));
        
        this.searchResultTree.setContentProvider(new TreeContentProvider(null));
        this.searchResultTree.setLabelProvider(new LabelProvider());
        
        // Double click adds the element to the new content
        this.searchResultTree.addDoubleClickListener(new IDoubleClickListener() {
            @SuppressWarnings("synthetic-access")
            @Override
            public void doubleClick(DoubleClickEvent event) {
                final IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                final MObject element = (MObject) selection.getFirstElement();
                if (element != null && !ElementEditionDialog.this.content.contains(element)) {
                    ElementEditionDialog.this.content.add(element);
                    ElementEditionDialog.this.contentTree.setInput(ElementEditionDialog.this.content);
                }
            }
        });
        
        // <ctrl>+click navigates to the selected element
        this.searchResultTree.getTable().addMouseListener(new MouseListener() {
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                // Nothing to do
            }
        
            @Override
            public void mouseDown(MouseEvent e) {
                // Nothing to do
            }
        
            @SuppressWarnings("synthetic-access")
            @Override
            public void mouseUp(MouseEvent e) {
                if ((e.stateMask & SWT.CTRL) != 0) {
                    final IStructuredSelection selection = (IStructuredSelection) ElementEditionDialog.this.searchResultTree
                            .getSelection();
                    final MObject element = (MObject) selection.getFirstElement();
                    if (element != null) {
                        // TODO CHM fire model change
                        // ModelProperty.getInstance().getNavigateService().fireNavigate(element);
                    }
                }
            }
        });
        
        // Up down buttons
        final Button upButton = new Button(composite, SWT.ARROW | SWT.PUSH);
        final FormData fd_up = new FormData();
        fd_up.top = new FormAttachment(searchCriteriaGroup, 5, SWT.BOTTOM);
        fd_up.left = new FormAttachment(50, -25);
        upButton.setLayoutData(fd_up);
        
        this.upImage = getBundleImage("icons/up_16.png");
        upButton.setImage(this.upImage);
        
        final Button downButton = new Button(composite, SWT.PUSH);
        final FormData fd_down = new FormData();
        fd_down.top = new FormAttachment(upButton, 0, SWT.TOP);
        fd_down.left = new FormAttachment(upButton, 5, SWT.RIGHT);
        downButton.setLayoutData(fd_down);
        
        this.downImage = getBundleImage("icons/down_16.png");
        downButton.setImage(this.downImage);
        
        downButton.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // Nothing to do
            }
        
            @SuppressWarnings("synthetic-access")
            @Override
            public void widgetSelected(SelectionEvent e) {
                final IStructuredSelection selection = (IStructuredSelection) ElementEditionDialog.this.searchResultTree.getSelection();
                final MObject element = (MObject) selection.getFirstElement();
                if (element != null && !ElementEditionDialog.this.content.contains(element)) {
                    ElementEditionDialog.this.content.add(element);
                    ElementEditionDialog.this.contentTree.setInput(ElementEditionDialog.this.content);
                }
            }
        });
        
        upButton.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // Nothing to do
            }
        
            @SuppressWarnings("synthetic-access")
            @Override
            public void widgetSelected(SelectionEvent e) {
                final IStructuredSelection selection = (IStructuredSelection) ElementEditionDialog.this.contentTree.getSelection();
                final MObject element = (MObject) selection.getFirstElement();
                if (element != null) {
                    ElementEditionDialog.this.content.remove(element);
                    ElementEditionDialog.this.contentTree.setInput(ElementEditionDialog.this.content);
                    // Add the element in the search results to allow the user to undo its action
                    ElementEditionDialog.this.searchResultTree.remove(element);
                    ElementEditionDialog.this.searchResultTree.add(element);
                }
            }
        });
        
        // Content group
        final Group contentGroup = new Group(composite, SWT.SHADOW_NONE);
        
        final FormLayout contentLayout = new FormLayout();
        contentGroup.setLayout(contentLayout);
        
        final FormData fd_contentGroup = new FormData();
        fd_contentGroup.top = new FormAttachment(upButton, 5, SWT.BOTTOM);
        fd_contentGroup.right = new FormAttachment(100, -5);
        fd_contentGroup.left = new FormAttachment(0, 5);
        fd_contentGroup.bottom = new FormAttachment(100, -5);
        contentGroup.setLayoutData(fd_contentGroup);
        
        contentGroup.setText(CoreUi.I18N.getString("KTable.ChoosenElements"));
        
        // New content table
        this.contentTree = new TableViewer(contentGroup, SWT.BORDER);
        final FormData fd_contentTree = new FormData();
        fd_contentTree.top = new FormAttachment(0, 5);
        fd_contentTree.bottom = new FormAttachment(100, -5);
        fd_contentTree.right = new FormAttachment(100, -5);
        fd_contentTree.left = new FormAttachment(0, 5);
        this.contentTree.getTable().setLayoutData(fd_contentTree);
        
        this.contentTree.setContentProvider(new TreeContentProvider(this.content));
        this.contentTree.setLabelProvider(new LabelProvider());
        
        // <ctrl>+click navigates to the selected element
        this.contentTree.getTable().addMouseListener(new MouseListener() {
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                // Nothing to do
            }
        
            @Override
            public void mouseDown(MouseEvent e) {
                // Nothing to do
            }
        
            @SuppressWarnings("synthetic-access")
            @Override
            public void mouseUp(MouseEvent e) {
                if ((e.stateMask & SWT.CTRL) != 0) {
                    final IStructuredSelection selection = (IStructuredSelection) ElementEditionDialog.this.contentTree.getSelection();
                    final MObject element = (MObject) selection.getFirstElement();
                    if (element != null) {
                        // TODO CHM fire model change
                        // ModelProperty.getInstance().getNavigateService().fireNavigate(element);
                    }
                }
            }
        });
        
        // Double click removes the element
        this.contentTree.addDoubleClickListener(new IDoubleClickListener() {
            @SuppressWarnings("synthetic-access")
            @Override
            public void doubleClick(DoubleClickEvent event) {
                final IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                final MObject element = (MObject) selection.getFirstElement();
                if (element != null) {
                    // ModelProperty.getInstance().getNavigateService().fireNavigate(element);
                    ElementEditionDialog.this.content.remove(element);
                    ElementEditionDialog.this.contentTree.setInput(ElementEditionDialog.this.content);
                    // Add the element in the search results to allow the user to undo its action
                    ElementEditionDialog.this.searchResultTree.remove(element);
                    ElementEditionDialog.this.searchResultTree.add(element);
                }
            }
        });
        
        this.contentTree.setInput(this.content);
        
        // Handle picking when the window gains focus
        class PickingHandler implements ShellListener, DisposeListener {
        
            @Override
            public void shellActivated(ShellEvent e) {
                // Nothing to do
            }
        
            @SuppressWarnings("synthetic-access")
            @Override
            public void shellClosed(ShellEvent e) {
                // Never called when the dialog is close:
                // a dispose event is sent instead.
                if (ElementEditionDialog.this.dialogActive) {
                    getShell().removeShellListener(this);
                    getShell().removeDisposeListener(this);
                    close();
                    ElementEditionDialog.this.editor.closeEditor(true);
                    abort();
                    ElementEditionDialog.this.dialogActive = false;
                }
            }
        
            @Override
            public void shellDeactivated(ShellEvent e) {
                // Nothing to do
            }
        
            @Override
            public void shellDeiconified(ShellEvent e) {
                // Nothing to do
            }
        
            @Override
            public void shellIconified(ShellEvent e) {
                // Nothing to do
            }
        
            @SuppressWarnings("synthetic-access")
            @Override
            public void widgetDisposed(DisposeEvent e) {
                if (ElementEditionDialog.this.dialogActive) {
                    getShell().removeShellListener(this);
                    getShell().removeDisposeListener(this);
                    close();
                    ElementEditionDialog.this.editor.closeEditor(true);
                    abort();
                    ElementEditionDialog.this.dialogActive = false;
                }
            }
        }
        
        final PickingHandler p = new PickingHandler();
        getShell().addShellListener(p);
        getShell().addDisposeListener(p);
        
        // initDropTarget();
        return composite;
    }

    @objid ("8dbe5776-c068-11e1-8c0a-002564c97630")
    @Override
    public void init() {
        setLogoImage(null);
        
        String targetName;
        try {
            targetName = (String) this.targetClass.getDeclaredField("MetaclassName").get(this.targetClass);
        } catch (final Exception e) {
            targetName = "elements";
        }
        
        final String titleKey = this.editedElement.getMClass().getName() + this.fieldName + targetName;
        
        getShell().setText(CoreUi.I18N.getString(titleKey));
        setTitle(CoreUi.I18N.getString(titleKey));
        setMessage(CoreUi.I18N.getString("KTable.MultipleInputHelp"));
        
        this.dialogActive = true;
    }

    @objid ("8dbe5779-c068-11e1-8c0a-002564c97630")
    @Override
    public int open() {
        if (this.pickingService != null) {
            this.pickingSession = this.pickingService.startPicking(this);
        }
        return super.open();
    }

    @objid ("8dbe577e-c068-11e1-8c0a-002564c97630")
    public void setContent(List<MObject> newContent) {
        this.content = new ArrayList<>(newContent);
        this.contentTree.setInput(this.content);
    }

    @objid ("8dbfdde8-c068-11e1-8c0a-002564c97630")
    @Override
    protected void cancelPressed() {
        this.editor.closeEditor(false);
        
        abort();
        
        this.dialogActive = false;
        super.cancelPressed();
    }

    @objid ("8dbfddec-c068-11e1-8c0a-002564c97630")
    protected static Image getBundleImage(String relativePath) {
        final Bundle imageBundle = Platform.getBundle(CoreUi.PLUGIN_ID);
        final IPath bitmapPath = new Path(relativePath);
        final URL bitmapUrl = FileLocator.find(imageBundle, bitmapPath, null);
        final ImageDescriptor desc = ImageDescriptor.createFromURL(bitmapUrl);
        return desc.createImage();
    }

    @objid ("8dbfddf1-c068-11e1-8c0a-002564c97630")
    @Override
    protected void okPressed() {
        this.editionValidator.validate(this.content);
        this.dialogActive = false;
        super.okPressed();
    }

/*
     * @Override public boolean acceptDroppedElements(MObject[] target) { return target.length == 1 &&
     * this.targetClass.isAssignableFrom(target[0].getClass()); }
     */
    @objid ("15fb1cb4-16da-11e2-aa0d-002564c97630")
    @Override
    public boolean hover(MObject target) {
        return this.targetClass.isAssignableFrom(target.getClass());
    }

    @objid ("15fbb8f5-16da-11e2-aa0d-002564c97630")
    @Override
    public void abort() {
        // Close the box if the picking session is stolen.
        this.editor.closeEditor(true);
        this.dialogActive = false;
        
        if (this.pickingSession != null) {
            this.pickingService.stopPicking(this.pickingSession);
            this.pickingSession = null;
        }
        
        close();
    }

/*
     * @Override public void setDroppedElements(MObject[] target) { if (target != null && target.length == 1 &&
     * !this.content.contains(target[0])) { this.content.add(target[0]); this.contentTree.refresh(); } }
     */
    @objid ("15fc0714-16da-11e2-aa0d-002564c97630")
    @Override
    public boolean pick(MObject target) {
        if (hover(target)) {
            if (!this.content.contains(target)) {
                this.content.add(target);
                this.contentTree.setInput(this.content);
            }
        
            return true;
        } else {
            return false;
        }
    }

    @objid ("11d6bce6-675a-4981-be45-8097b1b3c796")
    @Override
    protected String getHelpId() {
        return HELP_TOPIC;
    }

    @objid ("b21b02a7-d5ce-494d-a276-817440bb637f")
    public static ElementEditionDialog getInstance(Shell parentShell, IModel session, IModelioPickingService pickingService, MultipleElementCellEditor editor, List<MObject> initialContent, IEditionValidator editionValidator) {
        if (parentShell == null)
            return null;
        
        if (instance != null) {
            return instance;
        }
        
        instance = new ElementEditionDialog(parentShell, session, pickingService, editor, initialContent, editionValidator);
        return instance;
    }

    @objid ("4491562f-9071-4a75-bf76-6dff7a2fbb13")
    public static void closeInstance() {
        if (instance != null) {
            Display.getDefault().asyncExec(new Runnable() {        
                @Override
                public void run() {
                    instance.close();
                }
            });
        }
    }

/*
     * private void initDropTarget() { this.dropListener = new EditorDropListener(this); final int operations = DND.DROP_MOVE |
     * DND.DROP_COPY; final Transfer[] types = new Transfer[] { ModelElementTransfer.getInstance(), PluginTransfer.getInstance() };
     * final DropTarget target = new DropTarget(this.contentTree.getControl(), operations); target.setTransfer(types);
     * target.addDropListener(this.dropListener); }
     */
    @objid ("8dbfddfb-c068-11e1-8c0a-002564c97630")
    class LabelProvider implements ILabelProvider {
        @objid ("8dbfddfe-c068-11e1-8c0a-002564c97630")
        @Override
        public void addListener(ILabelProviderListener listener) {
            // Nothing to do
        }

        @objid ("8dbfde04-c068-11e1-8c0a-002564c97630")
        @Override
        public void dispose() {
            // Nothing to do
        }

        @objid ("8dbfde07-c068-11e1-8c0a-002564c97630")
        @Override
        public Image getImage(Object obj) {
            final MObject element = (MObject) obj;
            return MetamodelImageService.getIcon(element.getMClass());
        }

        @objid ("8dbfde0f-c068-11e1-8c0a-002564c97630")
        @Override
        public String getText(Object obj) {
            String text = new String();
            
            if (obj instanceof ActivityEdge) {
                final ActivityEdge edge = (ActivityEdge) obj;
                final StringBuffer buffer = new StringBuffer();
                buffer.append(edge.getName());
                buffer.append(" (");
                buffer.append(edge.getSource().getName());
                buffer.append(" -> ");
                buffer.append(edge.getTarget().getName());
                buffer.append(")");
                text = buffer.toString();
            } else if (obj instanceof NameSpace) {
                final NameSpace ns = (NameSpace) obj;
                String parentStr = new String();
            
                ModelTree parent = ns.getOwner();
                while (parent != null) {
                    parentStr = parent.getName() + "." + parentStr;
                    parent = parent.getOwner();
                }
            
                text = parentStr + ns.getName();
            } else if (obj instanceof ModelElement) {
                final ModelElement element = (ModelElement) obj;
                text = element.getName();
            } else {
                text = obj.toString();
            }
            return text;
        }

        @objid ("8dbfde15-c068-11e1-8c0a-002564c97630")
        @Override
        public boolean isLabelProperty(Object arg0, String arg1) {
            return false;
        }

        @objid ("8dbfde1c-c068-11e1-8c0a-002564c97630")
        @Override
        public void removeListener(ILabelProviderListener listener) {
            // Nothing to do
        }

    }

    @objid ("8dbfde29-c068-11e1-8c0a-002564c97630")
    class TreeContentProvider implements IStructuredContentProvider {
        @objid ("8dbfde2c-c068-11e1-8c0a-002564c97630")
        private List<MObject> results = null;

        @objid ("8dc16488-c068-11e1-8c0a-002564c97630")
        public TreeContentProvider(List<MObject> results) {
            this.results = results;
        }

        @objid ("8dc1648f-c068-11e1-8c0a-002564c97630")
        @Override
        public void dispose() {
            // Nothing to do
        }

        @objid ("8dc16492-c068-11e1-8c0a-002564c97630")
        @Override
        public Object[] getElements(Object arg0) {
            return this.results.toArray();
        }

        @objid ("8dc1649a-c068-11e1-8c0a-002564c97630")
        public List<MObject> getResults() {
            // Automatically generated method. Please delete this comment before entering specific code.
            return this.results;
        }

        @objid ("8dc164a2-c068-11e1-8c0a-002564c97630")
        @Override
        @SuppressWarnings("unchecked")
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            this.results = (List<MObject>) newInput;
        }

    }

}
