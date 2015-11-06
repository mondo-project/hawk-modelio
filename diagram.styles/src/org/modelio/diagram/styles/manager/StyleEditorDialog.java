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
                                    

package org.modelio.diagram.styles.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.modelio.app.core.picking.IModelioPickingService;
import org.modelio.core.ui.CoreFontRegistry;
import org.modelio.core.ui.dialog.ModelioDialog;
import org.modelio.diagram.styles.core.FactoryStyle;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.NamedStyle;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.diagram.styles.plugin.DiagramStyles;
import org.modelio.diagram.styles.viewer.StyleViewer;
import org.modelio.ui.UIColor;

/**
 * The style editor dialog is used to edit named styles.
 */
@objid ("85bc6e0a-1926-11e2-92d2-001ec947c8cc")
public class StyleEditorDialog extends ModelioDialog {
    @objid ("85bc6e0c-1926-11e2-92d2-001ec947c8cc")
     StyleViewer viewer;

    @objid ("85bc6e0d-1926-11e2-92d2-001ec947c8cc")
    private StyleModelProvider model;

    @objid ("85bed063-1926-11e2-92d2-001ec947c8cc")
    private SashForm mainSash;

    @objid ("85bed064-1926-11e2-92d2-001ec947c8cc")
    private Label descriptionText;

    @objid ("85bed065-1926-11e2-92d2-001ec947c8cc")
    private Button saveButton;

    @objid ("85bed066-1926-11e2-92d2-001ec947c8cc")
    private Button normButton;

    @objid ("85bed067-1926-11e2-92d2-001ec947c8cc")
    private Button restoreButton;

    @objid ("85bed068-1926-11e2-92d2-001ec947c8cc")
    private Font titleFont;

    @objid ("85bed069-1926-11e2-92d2-001ec947c8cc")
    private Label title;

    @objid ("28502f9a-33a9-4a20-8a1c-d1ce87d801e0")
    private IModelioPickingService pickingService;

    /**
     * C'tor.
     * @param parentShell the parent SWT shell
     * @param pickingService Modelio picking service
     */
    @objid ("85bed06b-1926-11e2-92d2-001ec947c8cc")
    public StyleEditorDialog(Shell parentShell, IModelioPickingService pickingService) {
        super(parentShell);
        this.pickingService = pickingService;
        
        final NamedStyle editedStyle = new NamedStyle("new style", FactoryStyle.getInstance());
        this.model = new StyleModelProvider(editedStyle, null, null, true);
        setBlockOnOpen(false);
    }

    @objid ("85bed06f-1926-11e2-92d2-001ec947c8cc")
    @Override
    public Control createContentArea(Composite parent) {
        // prepare a big Font for title
        FontData[] fontData = parent.getFont().getFontData();
        for (FontData data : fontData) {
            data.setHeight((int) (data.getHeight() * 1.4));
            data.setStyle(data.getStyle() | SWT.BOLD);
        }
        this.titleFont = CoreFontRegistry.getFont(fontData);
        
        // The content area is made of
        // - a tree viewer showing the style hierarchy, on the left
        // - a Style editor panel on the right
        // - a StyleViewer table
        // - a description area
        // - a toolbar
        //
        this.mainSash = new SashForm(parent, SWT.HORIZONTAL);
        this.mainSash.setLayout(new FillLayout());
        this.mainSash.setFont(parent.getFont());
        this.mainSash.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        // Create the TreeViewer
        createStyleTreeViewer(this.mainSash);
        
        // Create the StyleViewer
        createStyleEditorPanel(this.mainSash);
        
        //
        this.viewer.getTreeViewer().addSelectionChangedListener(new StyleViewerSelectionChangedListener(this));
        
        this.mainSash.setWeights(new int[] { 20, 80 });
        return this.mainSash;
    }

    @objid ("85c132be-1926-11e2-92d2-001ec947c8cc")
    private static StyleViewer createStyleViewer(Composite parent, final StyleModelProvider modelProvider, IModelioPickingService pickingService) {
        // Create table viewer
        return new StyleViewer(parent, modelProvider, pickingService);
    }

    @objid ("85c3951b-1926-11e2-92d2-001ec947c8cc")
    @Override
    public void init() {
        setTitle(DiagramStyles.I18N.getString("EditStylesDialog.SubTitle"));
        setMessage(DiagramStyles.I18N.getString("EditStylesDialog.Message"));
        getShell().setText(DiagramStyles.I18N.getString("EditStylesDialog.Title"));
    }

    @objid ("85c3951e-1926-11e2-92d2-001ec947c8cc")
    @Override
    public void addButtonsInButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.CLOSE_LABEL, false);
    }

    /**
     * Set the edited style
     * @param editedStyle the edited style
     */
    @objid ("85c39522-1926-11e2-92d2-001ec947c8cc")
    void setEditedStyle(IStyle editedStyle) {
        this.model = (editedStyle != null) ? new StyleModelProvider(editedStyle, null, null, true) : null;
        
        if (editedStyle != null) {
            this.title
                    .setText(DiagramStyles.I18N.getMessage("EditStylesDialog.CurrentStyle", ((NamedStyle) editedStyle).getName()));
        } else {
            this.title.setText(" ");
        }
        
        if (this.viewer != null) {
        
            final Object[] expandedCategories = this.viewer.getTreeViewer().getExpandedElements();
        
            // change the data model, this will collapse all categories which is not user friendly :(
            this.viewer.setModel(this.model);
        
            // try to expand the categories that were previously expanded => this is user friendly :)
            this.viewer.getTreeViewer().getTree().setRedraw(false);
            for (final Object o : expandedCategories) {
                this.viewer.getTreeViewer().setExpandedState(o, true);
            }
            this.viewer.getTreeViewer().getTree().setRedraw(true);
        
        }
    }

    @objid ("85c39525-1926-11e2-92d2-001ec947c8cc")
    @Override
    public boolean close() {
        setEditedStyle(null);
        this.titleFont = null;
        return super.close();
    }

    @objid ("85c3952a-1926-11e2-92d2-001ec947c8cc")
    @Override
    protected Point getInitialSize() {
        return new Point(800, 800);
    }

    /**
     * This method is called by the selection change listener (StyleViewerSelectionChangedListener) of the style viewer when the
     * selection changes in it. The method updates the buttons status (enabled/disabled) along with the description field displayed
     * text.
     */
    @objid ("85c3952f-1926-11e2-92d2-001ec947c8cc")
    protected void styleViewerSelectionChanged(final List<Object> selectedElements) {
        boolean onlyStyleKeys = true;
        
        for (final Object o : selectedElements) {
            onlyStyleKeys &= (o instanceof StyleKey);
        }
        
        if (onlyStyleKeys) {
            if (selectedElements.isEmpty()) {
                this.restoreButton.setEnabled(false);
                this.descriptionText.setText("");
            } else if (selectedElements.size() == 1) {
                this.restoreButton.setEnabled(true);
                this.descriptionText.setText(((StyleKey) selectedElements.get(0)).getTooltip());
            } else {
                this.restoreButton.setEnabled(true);
                this.descriptionText.setText("");
            }
        } else {
            this.restoreButton.setEnabled(false);
            this.descriptionText.setText("");
        }
    }

    @objid ("85c39536-1926-11e2-92d2-001ec947c8cc")
    private Composite createStyleEditorPanel(final Composite parent) {
        final Composite styleEditorPanel = new Composite(parent, SWT.BORDER);
        styleEditorPanel.setLayout(new FormLayout());
        
        this.title = new Label(styleEditorPanel, SWT.NONE);
        this.title.setText("");
        this.title.setFont(this.titleFont);
        final FormData fData0 = new FormData();
        fData0.top = new FormAttachment(0, 10);
        // fData0.bottom = new FormAttachment(100, -30);
        fData0.left = new FormAttachment(0, 2);
        fData0.right = new FormAttachment(100, -2);
        this.title.setLayoutData(fData0);
        
        final SashForm sash = new SashForm(styleEditorPanel, SWT.VERTICAL | SWT.BORDER);
        final FormData fData1 = new FormData();
        fData1.top = new FormAttachment(this.title, 10);
        fData1.bottom = new FormAttachment(100, -30);
        fData1.left = new FormAttachment(0, 2);
        fData1.right = new FormAttachment(100, -2);
        sash.setLayoutData(fData1);
        
        // Add the style viewer table
        this.viewer = createStyleViewer(sash, this.model, this.pickingService);
        
        // Add the description label:
        this.descriptionText = new Label(sash, SWT.WRAP | SWT.V_SCROLL);
        // moduleDescriptionText.setEditable(false);
        this.descriptionText.setForeground(UIColor.LABEL_TIP_FG);
        
        sash.setWeights(new int[] { 80, 20 });
        
        // Add the toolbar
        final Composite editionToolbar = createStyleEditorButtons(styleEditorPanel);
        final FormData fData2 = new FormData();
        fData2.top = new FormAttachment(sash, 2);
        fData2.bottom = new FormAttachment(100, -2);
        fData2.left = new FormAttachment(0, 5);
        fData2.right = new FormAttachment(100, -2);
        editionToolbar.setLayoutData(fData2);
        return styleEditorPanel;
    }

    @objid ("85c3953c-1926-11e2-92d2-001ec947c8cc")
    private TreeViewer createStyleTreeViewer(final Composite parent) {
        final TreeViewer treeView = new TreeViewer(parent, SWT.BORDER);
        treeView.setContentProvider(new StyleTreeContentProvider());
        treeView.setLabelProvider(new StyleTreeLabelProvider());
        treeView.setInput(DiagramStyles.getStyleManager());
        treeView.addSelectionChangedListener(new StyleTreeSelectionChangedListener(this));
        treeView.setAutoExpandLevel(2);
        
        final Transfer[] transferTypes = new Transfer[] { TextTransfer.getInstance() };
        
        treeView.addDragSupport(DND.DROP_MOVE, transferTypes, new DragSourceListener() {
            @Override
            public void dragFinished(DragSourceEvent event) {
                // nothing to do
            }
        
            @Override
            public void dragSetData(DragSourceEvent event) {
                final IStructuredSelection selection = (IStructuredSelection) treeView.getSelection();
                if (!selection.isEmpty()) {
                    final IStyle style = (IStyle) selection.getFirstElement();
                    if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
                        event.data = ((NamedStyle) style).getName();
                    }
                }
            }
        
            @Override
            public void dragStart(DragSourceEvent event) {
                final IStructuredSelection selection = (IStructuredSelection) treeView.getSelection();
                event.doit = (selection.size() == 1);
            }
        });
        treeView.addDropSupport(DND.DROP_MOVE, transferTypes, new ViewerDropAdapter(treeView) {
            @Override
            public boolean performDrop(Object data) {
                final StyleManager styleManager = DiagramStyles.getStyleManager();
                final IStyle target = (IStyle) getCurrentTarget();
                final IStyle dropped = styleManager.getStyle((String) data);
                if (dropped != null) {
                    dropped.setCascadedStyle(target);
                    treeView.refresh();
                    return true;
                }
                return false;
            }
        
            @Override
            public boolean validateDrop(Object target, int operation, TransferData transferType) {
                return true;
            }
        });
        
        // Tree popupmenu
        final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
        final Action createStyleAction = new Action() {
            @Override
            public void run() {
                super.run();
                final NamedStyle parentStyle = ((NamedStyle) ((StructuredSelection) treeView.getSelection()).getFirstElement());
                final IInputValidator validator = new IInputValidator() {
                    @Override
                    public String isValid(String newText) {
                        // Check name syntax must match "[a-zA-Z0-9 ]+"
                        if (!Pattern.matches("[a-zA-Z0-9]+", newText) || DiagramStyles.getStyleManager().getStyle(newText) != null) {
                            return DiagramStyles.I18N.getString("CreateStyleDialog.ErrorBadStyleName");
                        }
                        return null;
                    }
                };
        
                final InputDialog dlg = new InputDialog(parent.getShell(), DiagramStyles.I18N.getString("CreateStyleDialog.Title"),
                        DiagramStyles.I18N.getString("CreateStyleDialog.Prompt"),
                        DiagramStyles.I18N.getString("CreateStyleDialog.DefaultName"), validator);
        
                dlg.open();
                final String name = dlg.getValue();
                if (name == null) {
                    return;
                }
        
                final NamedStyle newStyle = DiagramStyles.getStyleManager().createStyle(name, parentStyle.getName());
                if (newStyle != null) {
                    treeView.refresh();
                    treeView.expandToLevel(parentStyle, 1);
                    final ArrayList<Object> selectionList = new ArrayList<>();
                    selectionList.add(newStyle);
                    treeView.setSelection(new StructuredSelection(selectionList), true);
                    setEditedStyle(newStyle);
                }
            }
        };
        
        createStyleAction.setText(DiagramStyles.I18N.getString("EditStylesDialog.DeriveCommand"));
        
        menuMgr.add(createStyleAction);
        
        final Menu menu = menuMgr.createContextMenu(treeView.getTree());
        treeView.getTree().setMenu(menu);
        
        // editing support
        return treeView;
    }

    @objid ("85c39542-1926-11e2-92d2-001ec947c8cc")
    private Composite createStyleEditorButtons(final Composite styleEditorPanel) {
        // Create editor toolbar and buttons
        // Catalog buttons
        final Composite editionToolbar = new Composite(styleEditorPanel, SWT.NONE);
        editionToolbar.setLayout(new RowLayout(SWT.HORIZONTAL));
        
        // GridDataFactory.defaultsFor(editionToolbar).align(SWT.END, SWT.BEGINNING) // align on the right
        // .grab(false, false).applyTo(editionToolbar);
        
        // "Save" button
        this.saveButton = new Button(editionToolbar, SWT.PUSH);
        this.saveButton.setText(DiagramStyles.I18N.getString("EditStylesDialog.SaveButton"));
        this.saveButton.setToolTipText("Save the current settings for the style");
        this.saveButton.addSelectionListener(new SelectionListener() {
        
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // do nothing
            }
        
            @Override
            public void widgetSelected(SelectionEvent e) {
                DiagramStyles.getStyleManager().save((NamedStyle) StyleEditorDialog.this.viewer.getEditedStyle());
            }
        
        });
        
        // "Normalize" button
        this.normButton = new Button(editionToolbar, SWT.PUSH);
        this.normButton.setText(DiagramStyles.I18N.getString("EditStylesDialog.NormalizeButton"));
        this.normButton.setToolTipText("Normalize the style definitions. Remove useless local properties definitions.");
        this.normButton.addSelectionListener(new SelectionListener() {
        
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // do nothing
            }
        
            @Override
            public void widgetSelected(SelectionEvent e) {
                StyleEditorDialog.this.viewer.getEditedStyle().normalize();
            }
        
        });
        
        // "Restore" button
        this.restoreButton = new Button(editionToolbar, SWT.PUSH);
        this.restoreButton.setText(DiagramStyles.I18N.getString("EditStylesDialog.RestoreButton"));
        
        this.restoreButton.setToolTipText("Restore the selected values to their inherited value");
        this.restoreButton.addSelectionListener(new RestoreButtonSelectionListener(this));
        return editionToolbar;
    }

    @objid ("85c39548-1926-11e2-92d2-001ec947c8cc")
    private static class StyleTreeContentProvider implements ITreeContentProvider {
        @objid ("85c3954a-1926-11e2-92d2-001ec947c8cc")
        public StyleTreeContentProvider() {
            super();
        }

        @objid ("85c3954c-1926-11e2-92d2-001ec947c8cc")
        @Override
        public Object[] getChildren(Object parentElement) {
            final ArrayList<NamedStyle> children = new ArrayList<>();
            final IStyle parentStyle = (IStyle) parentElement;
            final StyleManager sm = DiagramStyles.getStyleManager();
            for (final String s : sm.getAvailableStyles()) {
                if (sm.getStyle(s) != null && sm.getStyle(s).getCascadedStyle() == parentStyle) {
                    children.add(sm.getStyle(s));
                }
            }
            return children.toArray();
        }

        @objid ("85c5f771-1926-11e2-92d2-001ec947c8cc")
        @Override
        public Object[] getElements(Object inputElement) {
            final IStyle defaultStyle = DiagramStyles.getStyleManager().getDefaultStyle();
            return (defaultStyle != null) ? new Object[] { defaultStyle } : new Object[0];
        }

        @objid ("85c5f779-1926-11e2-92d2-001ec947c8cc")
        @Override
        public Object getParent(Object element) {
            return null;
        }

        @objid ("85c5f77f-1926-11e2-92d2-001ec947c8cc")
        @Override
        public boolean hasChildren(Object element) {
            final IStyle parentStyle = (IStyle) element;
            final StyleManager sm = DiagramStyles.getStyleManager();
            for (final String s : sm.getAvailableStyles()) {
                if (sm.getStyle(s) != null && sm.getStyle(s).getCascadedStyle() == parentStyle) {
                    return true;
                }
            }
            return false;
        }

        @objid ("85c5f785-1926-11e2-92d2-001ec947c8cc")
        @Override
        public void dispose() {
            // Nothing to dispose
        }

        @objid ("85c5f788-1926-11e2-92d2-001ec947c8cc")
        @Override
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            viewer.refresh();
        }

    }

    @objid ("85c5f78e-1926-11e2-92d2-001ec947c8cc")
    class StyleTreeLabelProvider extends LabelProvider {
        @objid ("85c5f78f-1926-11e2-92d2-001ec947c8cc")
        @Override
        public String getText(Object element) {
            if (element instanceof NamedStyle) {
                return ((NamedStyle) element).getName();
            } else {
                return super.getText(element);
            }
        }

    }

    @objid ("85c5f794-1926-11e2-92d2-001ec947c8cc")
    class StyleTreeSelectionChangedListener implements ISelectionChangedListener {
        @objid ("85c5f795-1926-11e2-92d2-001ec947c8cc")
        private final StyleEditorDialog styleEditorDialog;

        @objid ("85c5f797-1926-11e2-92d2-001ec947c8cc")
        public StyleTreeSelectionChangedListener(StyleEditorDialog styleEditorDialog) {
            this.styleEditorDialog = styleEditorDialog;
        }

        @objid ("85c5f79a-1926-11e2-92d2-001ec947c8cc")
        @Override
        public void selectionChanged(SelectionChangedEvent event) {
            final ISelection newSelection = event.getSelection();
            if (newSelection instanceof IStructuredSelection) {
                final Object selectedElement = ((IStructuredSelection) newSelection).getFirstElement();
                if (selectedElement instanceof IStyle) {
                    this.styleEditorDialog.setEditedStyle((IStyle) selectedElement);
                }
            }
        }

    }

    @objid ("85c5f79e-1926-11e2-92d2-001ec947c8cc")
    class StyleViewerSelectionChangedListener implements ISelectionChangedListener {
        @objid ("85c5f79f-1926-11e2-92d2-001ec947c8cc")
        private final StyleEditorDialog styleEditorDialog;

        @objid ("85c859cc-1926-11e2-92d2-001ec947c8cc")
        public StyleViewerSelectionChangedListener(final StyleEditorDialog styleEditorDialog) {
            this.styleEditorDialog = styleEditorDialog;
        }

        @objid ("85c859d0-1926-11e2-92d2-001ec947c8cc")
        @SuppressWarnings("unchecked")
        @Override
        public void selectionChanged(final SelectionChangedEvent event) {
            final ISelection newSelection = event.getSelection();
            if (newSelection instanceof IStructuredSelection) {
                final List<Object> selectedElements = ((IStructuredSelection) newSelection).toList();
                this.styleEditorDialog.styleViewerSelectionChanged(selectedElements);
            }
        }

    }

    @objid ("85cabc26-1926-11e2-92d2-001ec947c8cc")
    private static final class RestoreButtonSelectionListener implements SelectionListener {
        @objid ("85cabc28-1926-11e2-92d2-001ec947c8cc")
        private final StyleEditorDialog styleEditorDialog;

        @objid ("85cabc2a-1926-11e2-92d2-001ec947c8cc")
        public RestoreButtonSelectionListener(final StyleEditorDialog styleEditorDialog) {
            this.styleEditorDialog = styleEditorDialog;
        }

        @objid ("85cabc2e-1926-11e2-92d2-001ec947c8cc")
        @Override
        public void widgetDefaultSelected(final SelectionEvent e) {
            // do nothing
        }

        @objid ("85cabc33-1926-11e2-92d2-001ec947c8cc")
        @Override
        public void widgetSelected(final SelectionEvent event) {
            final IStyle editedStyle = this.styleEditorDialog.viewer.getEditedStyle();
            final ISelection newSelection = this.styleEditorDialog.viewer.getTreeViewer().getSelection();
            if (newSelection instanceof IStructuredSelection) {
                for (final Object o : ((IStructuredSelection) newSelection).toList()) {
                    if (o instanceof StyleKey) {
                        editedStyle.removeProperty((StyleKey) o);
                    }
                }
            }
        }

    }

}
