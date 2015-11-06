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
                                    

package org.modelio.model.browser.views.treeview;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.modelio.api.ui.dnd.ModelElementTransfer;
import org.modelio.core.ui.dnd.MObjectViewerDragProvider;
import org.modelio.core.ui.dnd.MObjectViewerDropListener;
import org.modelio.core.ui.images.ElementDecoratedStyledLabelProvider;
import org.modelio.model.browser.views.BrowserView;
import org.modelio.ui.panel.IPanelProvider;
import org.modelio.vcore.session.api.ICoreSession;

/**
 * Provides reusable browser panels
 */
@objid ("27fb4d4e-d01b-11e1-9020-002564c97630")
public class ModelBrowserPanelProvider implements IPanelProvider {
    @objid ("345ab100-bede-11e1-b430-001ec947c8cc")
    private TreeViewer treeViewer;

    @objid ("57ccc2d6-d023-11e1-9020-002564c97630")
    private BrowserContentProvider contentProvider;

    @objid ("1fc49984-1de3-11e2-bcbe-002564c97630")
    private ElementNameModifier nameModifier;

    @objid ("1fc49985-1de3-11e2-bcbe-002564c97630")
    private ICoreSession modelingSession;

    @objid ("1fc49986-1de3-11e2-bcbe-002564c97630")
    private EditorActivationStrategy activationSupport;

    @objid ("72459257-4540-11e2-aeb7-002564c97630")
    private BrowserLabelProvider baseProvider;

    @objid ("bee44d7c-9a45-485c-8c1e-cffcb3010cbd")
    private MObjectViewerDragProvider dragListener;

    @objid ("5940c59f-a6da-49d2-b425-7929cbe064e3")
    private MObjectViewerDropListener dropListener;

    /**
     * Called to create the view and initialize it.
     * @return the browser tree viewer.
     */
    @objid ("57ccc2d7-d023-11e1-9020-002564c97630")
    @Override
    public TreeViewer createPanel(Composite parent) {
        this.treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        this.treeViewer.setUseHashlookup(true);
        
        // TODO FPO get content provider from current view point (if any) and/or
        // dialog settings for multiple instance of this view.
        // ModelBrowser.getInstance().getDialogSettings().
        this.contentProvider = new BrowserContentProvider();
        this.treeViewer.setContentProvider(this.contentProvider);
        
        this.baseProvider = new BrowserLabelProvider();
        this.treeViewer.setLabelProvider(new ElementDecoratedStyledLabelProvider(this.baseProvider, true, true));
        
        // this.treeViewer.setComparator(new BrowserElementComparator());
        
        initEditor();
        
        this.treeViewer.setInput(null);
        return this.treeViewer;
    }

    @objid ("57ccc2de-d023-11e1-9020-002564c97630")
    @Override
    public TreeViewer getPanel() {
        return this.treeViewer;
    }

    /**
     * Get the current element displayed by the view.
     * @return the model element whose content is listed in the model tree. May be null.
     */
    @objid ("57ccc2e3-d023-11e1-9020-002564c97630")
    @Override
    public Object getInput() {
        return this.treeViewer.getInput();
    }

    /**
     * Set the current element displayed by the view.
     * @param input the model element whose content is listed in the model tree panel. May be null.
     */
    @objid ("57ccc2e9-d023-11e1-9020-002564c97630")
    @Override
    public void setInput(Object input) {
        this.treeViewer.setInput(input);
    }

    @objid ("57ccc2ee-d023-11e1-9020-002564c97630")
    public List<Object> getLocalRoots() {
        return new ArrayList<>(this.contentProvider.getLocalRoots());
    }

    @objid ("57cf2428-d023-11e1-9020-002564c97630")
    public void setLocalRoots(List<Object> localRoots) {
        this.contentProvider.setLocalRoots(localRoots);
    }

    @objid ("aa8b3a26-16e1-11e2-aa0d-002564c97630")
    public void setSelection(List<Object> elements) {
        IStructuredSelection treeSelection = new StructuredSelection(elements);
        this.treeViewer.setSelection(treeSelection);
    }

    @objid ("002b00ca-78e3-107d-a016-001ec947cd2a")
    public void setLabelProvider(IBaseLabelProvider labelProvider) {
        this.treeViewer.setLabelProvider(labelProvider);
    }

    /**
     * Makes this view editable. <code>modelingSession</code> is mandatory otherwise edition cannot be supported.
     * 
     * To deactivate edition, call <code>activateEdition(null)</code>
     * @param newModelingSession the current edited modeling session.
     */
    @objid ("1fc49987-1de3-11e2-bcbe-002564c97630")
    public void activateEdition(ICoreSession newModelingSession) {
        this.modelingSession = newModelingSession;
        
        if (this.treeViewer != null) {
            this.nameModifier = new ElementNameModifier(this.modelingSession);
            this.treeViewer.setCellModifier(this.nameModifier);
        
            if (this.dragListener == null) {
                this.dragListener = new MObjectViewerDragProvider(this.treeViewer);
                this.treeViewer.addDragSupport(DND.DROP_MOVE | DND.DROP_COPY, new Transfer[] { ModelElementTransfer.getInstance() }, this.dragListener);
            }
        
            if (this.dropListener == null) {
                this.dropListener = new MObjectViewerDropListener(this.treeViewer, this.modelingSession);
                this.treeViewer.addDropSupport(DND.DROP_MOVE | DND.DROP_COPY, new Transfer[] { ModelElementTransfer.getInstance() }, this.dropListener);
            } else {
                this.dropListener.setCoreSession(newModelingSession);
            }
        }
    }

    @objid ("1fc4998b-1de3-11e2-bcbe-002564c97630")
    private void initEditor() {
        // Define cell editor
        TextCellEditor[] cellEditors = new TextCellEditor[1];
        TextCellEditor editor = new TextCellEditor(this.treeViewer.getTree(), SWT.NONE) {
            private Collection<String> activeContexts;
        
            @SuppressWarnings("synthetic-access")
            @Override
            public void activate() {
                // We must deactivate the active contexts during the edition, to avoid the editor's shortcuts to be triggered when entering an element's name... 
                // Store those contexts for further reactivation
                this.activeContexts = new ArrayList<>(BrowserView.getContextService().getActiveContextIds());
                for (String contextId : this.activeContexts) {
                    BrowserView.getContextService().deactivateContext(contextId);
                }
                ModelBrowserPanelProvider.this.contentProvider.isEditorActive = true;
        
                super.activate();
            }
        
            @SuppressWarnings("synthetic-access")
            @Override
            public void deactivate() {
                if (this.activeContexts != null) {
                    // Restore previously deactivated contexts
                    for (String contextId : this.activeContexts) {
                        BrowserView.getContextService().activateContext(contextId);
                    }
                    ModelBrowserPanelProvider.this.contentProvider.isEditorActive = false;
                    ModelBrowserPanelProvider.this.treeViewer.refresh(true);
                    
                    this.activeContexts = null;
                }
        
                super.deactivate();
            }
        };
        
        editor.getControl().addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent event) {
                // Nothing to do.
            }
        
            @Override
            public void keyReleased(KeyEvent event) {
                if ((event.stateMask &= SWT.MOD1) != 0 && event.keyCode == 'a') {
                    Object source = event.getSource();
                    if (source instanceof Text) {
                        Text text = (Text) source;
                        text.selectAll();
                    }
                }
            }
        });
        cellEditors[0] = editor;
        this.treeViewer.setCellEditors(cellEditors);
        
        // Define column properties
        String[] properties = new String[1];
        properties[0] = "name";
        this.treeViewer.setColumnProperties(properties);
        
        // Define editor activation strategy
        this.activationSupport = new EditorActivationStrategy(this.treeViewer);
        
        TreeViewerEditor.create(this.treeViewer, null, this.activationSupport, ColumnViewerEditor.TABBING_HORIZONTAL
                | ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR | ColumnViewerEditor.TABBING_VERTICAL
                | ColumnViewerEditor.KEYBOARD_ACTIVATION);
    }

    @objid ("7245925b-4540-11e2-aeb7-002564c97630")
    public boolean isShowVisibility() {
        return this.baseProvider.isShowVisibility();
    }

    @objid ("7245925f-4540-11e2-aeb7-002564c97630")
    public void setShowVisibility(boolean showVisibility) {
        this.baseProvider.setShowVisibility(showVisibility);
        this.treeViewer.refresh(true);
    }

    @objid ("72459262-4540-11e2-aeb7-002564c97630")
    public boolean isShowMdaModel() {
        return this.contentProvider.isShowMdaModel();
    }

    @objid ("72459266-4540-11e2-aeb7-002564c97630")
    public void setShowMdaModel(boolean showMdaModel) {
        this.contentProvider.setShowMdaModel(showMdaModel);
        this.treeViewer.refresh(true);
    }

    @objid ("72459269-4540-11e2-aeb7-002564c97630")
    public boolean isShowProjects() {
        return this.contentProvider.isShowProjects();
    }

    @objid ("7245926d-4540-11e2-aeb7-002564c97630")
    public void setShowProjects(boolean showProjects) {
        this.contentProvider.setShowProjects(showProjects);
        this.treeViewer.refresh(true);
    }

    @objid ("9688dc08-f70a-4742-851a-eb669c6af083")
    public void setShowAnalystModel(boolean showAnalystModel) {
        this.contentProvider.setShowAnalystModel(showAnalystModel);
        this.treeViewer.refresh(true);
    }

    @objid ("f7289fd8-7398-4b0a-bd88-7155ec02bdae")
    public boolean isShowAnalystModel() {
        return this.contentProvider.isShowAnalystModel();
    }

    @objid ("36b61724-7ca1-4fa6-9e92-1e77111502b0")
    @Override
    public boolean isRelevantFor(Object obj) {
        return true;
    }

    @objid ("a501c525-0679-447f-a363-4f381df2656d")
    @Override
    public String getHelpTopic() {
        return null;
    }

}
