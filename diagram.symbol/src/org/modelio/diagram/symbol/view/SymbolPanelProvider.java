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
                                    

package org.modelio.diagram.symbol.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.modelio.app.core.picking.IModelioPickingService;
import org.modelio.diagram.elements.core.model.IGmObject;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.IStyleChangeListener;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.diagram.styles.manager.StyleModelProvider;
import org.modelio.diagram.styles.viewer.StyleViewer;
import org.modelio.ui.UIColor;
import org.modelio.ui.panel.IPanelProvider;
import org.modelio.vcore.session.api.ICoreSession;

/**
 * Diagram symbol view panel provider.
 * <p>
 * Displays and allow edition of the selected graphic model properties.
 */
@objid ("eefb5c4a-58da-11e2-8bfd-001ec947ccaf")
public class SymbolPanelProvider implements IPanelProvider, IStyleChangeListener, PropertyChangeListener {
    @objid ("ac5129ed-55b7-11e2-877f-002564c97630")
    private IGmObject selectedSymbol;

    @objid ("ac5129f3-55b7-11e2-877f-002564c97630")
    private SashForm sash;

    @objid ("32561618-5991-11e2-8bfd-001ec947ccaf")
    private StyleViewer styleViewer;

    @objid ("ac5129f4-55b7-11e2-877f-002564c97630")
    private Text descriptionText;

    @objid ("727af078-1dfa-46db-a0f5-08ab79d6461b")
    private IModelioPickingService pickingService;

    /**
     * C'tor
     * @param pickingService the Modelio picking service
     */
    @objid ("3256161a-5991-11e2-8bfd-001ec947ccaf")
    public SymbolPanelProvider(IModelioPickingService pickingService) {
        this.pickingService = pickingService;
    }

    @objid ("3256161e-5991-11e2-8bfd-001ec947ccaf")
    @Override
    public Object createPanel(Composite parent) {
        this.sash = new SashForm(parent, SWT.VERTICAL);
        this.sash.setLayout(new FillLayout());
        
        this.styleViewer = new StyleViewer(this.sash, null, this.pickingService);
        
        //
        this.descriptionText = createHelpPanel(this.sash);
        this.sash.setWeights(new int[] { 80, 20 });
        
        this.styleViewer.getTreeViewer().addSelectionChangedListener(new DescriptionTextUpdater(this.descriptionText));
        return this.sash;
    }

    @objid ("32561625-5991-11e2-8bfd-001ec947ccaf")
    @Override
    public Object getPanel() {
        return this.sash;
    }

    @objid ("3256162e-5991-11e2-8bfd-001ec947ccaf")
    @Override
    public IGmObject getInput() {
        return this.selectedSymbol;
    }

    @objid ("ac52b08a-55b7-11e2-877f-002564c97630")
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(IGmObject.PROPERTY_DELETE)) {
            // The Gm displayed in the Symbol view has been deleted, reset the selection.
            setSelectedSymbol(null);
        }
    }

    @objid ("3256162a-5991-11e2-8bfd-001ec947ccaf")
    @Override
    public void setInput(Object input) {
        IGmObject gmObject = (IGmObject) Platform.getAdapterManager().getAdapter(input, IGmObject.class);
        if (gmObject != null)
            setSelectedSymbol(gmObject);
    }

    @objid ("ac52b07a-55b7-11e2-877f-002564c97630")
    @Override
    public void styleChanged(final StyleKey property, final Object newValue) {
        if (this.selectedSymbol == null) {
            return;
        }
        Collection<StyleKey> keyfilter = this.styleViewer.getModel().getKeyfilter();
        if (keyfilter == null) {
            return;
        }
        
        // Check if style keys changed.
        if (!keyfilter.equals(this.selectedSymbol.getStyleKeys())) {
            // Change the StyleViewer model provider
            // Instead of providing the symbol Style, we provide a StyleEditor proxy
            // that will be responsible for managing transactions in the model in case of modifications
            ICoreSession session = this.selectedSymbol.getDiagram().getModelManager().getModelingSession();
            final StyleModelProvider model = new StyleModelProvider(new StyleEditor(this.selectedSymbol), session,
                    this.selectedSymbol.getStyleKeys(), true);
        
            this.styleViewer.setModel(model);
            this.styleViewer.getTreeViewer().expandAll();
            this.styleViewer.getTreeViewer().refresh();
        
        }
    }

    @objid ("ac52b083-55b7-11e2-877f-002564c97630")
    @Override
    public void styleChanged(final IStyle changedStyle) {
        Collection<StyleKey> keyfilter = this.styleViewer.getModel().getKeyfilter();
        if (keyfilter == null) {
            return;
        }
        
        // Check if style keys changed.
        if (!keyfilter.equals(this.selectedSymbol.getStyleKeys())) {
            // Change the StyleViewer model provider
            // Instead of providing the symbol Style, we provide a StyleEditor proxy
            // that will be responsible for managing transactions in the model in case of modifications
            ICoreSession session = this.selectedSymbol.getDiagram().getModelManager().getModelingSession();
            StyleModelProvider model = new StyleModelProvider(new StyleEditor(this.selectedSymbol), session,
                    this.selectedSymbol.getStyleKeys(), true);
        
            this.styleViewer.setModel(model);
            this.styleViewer.getTreeViewer().expandAll();
            this.styleViewer.getTreeViewer().refresh();
        }
    }

    /**
     * Mask/unmask the text zone containing the help for the selected style key.
     */
    @objid ("ac52b067-55b7-11e2-877f-002564c97630")
    public void toggleHelpPanel() {
        if (!this.descriptionText.isDisposed()) {
            final boolean currentState = this.descriptionText.getVisible();
            if (currentState) {
                this.descriptionText.setVisible(false);
            } else {
                this.descriptionText.setVisible(true);
            }
            this.descriptionText.getParent().layout();
        }
    }

    /**
     * This method is called when the selected symbol in the diagram changes.
     * @param selectedSymbol the newly selected symbol (might be null)
     */
    @objid ("ac52b06a-55b7-11e2-877f-002564c97630")
    void setSelectedSymbol(IGmObject selectedSymbol) {
        if (this.selectedSymbol != null && this.selectedSymbol == selectedSymbol) {
            return;
        }
        if (this.selectedSymbol != null) {
            this.selectedSymbol.getStyle().removeListener(this);
            this.selectedSymbol.removePropertyChangeListener(this);
        }
        this.selectedSymbol = selectedSymbol;
        if (selectedSymbol != null) {
            boolean isEditable = selectedSymbol.isEditable();
            // Change the StyleViewer model provider
            // Instead of providing the symbol Style, we provide a StyleEditor proxy
            // that will be responsible for managing transactions in the model in case of modifications
            ICoreSession session = this.selectedSymbol.getDiagram().getModelManager().getModelingSession();
            final StyleModelProvider model = new StyleModelProvider(new StyleEditor(selectedSymbol), session,
                    selectedSymbol.getStyleKeys(), isEditable);
        
            this.styleViewer.setModel(model);
            this.styleViewer.getTreeViewer().expandAll();
            this.styleViewer.getTreeViewer().refresh();
        
            // Set the model listener
            this.selectedSymbol.getStyle().addListener(this);
            this.selectedSymbol.addPropertyChangeListener(this);
        } else {
            this.styleViewer.setModel(null);
            // this.styleViewer.getTreeViewer().refresh();
        }
    }

    @objid ("ac52b062-55b7-11e2-877f-002564c97630")
    private Text createHelpPanel(Composite parent) {
        final Text newText = new Text(parent, SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
        newText.setBackground(UIColor.TEXT_READONLY_BG);
        newText.setText("");
        newText.setVisible(false);
        return newText;
    }

    @objid ("b744288e-f16f-498d-b8a2-590ecaad93cd")
    @Override
    public boolean isRelevantFor(Object obj) {
        return true;
    }

    @objid ("e85a2870-933f-4f4a-8943-edb9734ac12a")
    @Override
    public String getHelpTopic() {
        return null;
    }

    /**
     * This class is the selection listener of the symbol tree view. When the selected property changes, it update sthe key
     * description area.
     */
    @objid ("ac52b08e-55b7-11e2-877f-002564c97630")
    private static class DescriptionTextUpdater implements ISelectionChangedListener {
        @objid ("ac52b091-55b7-11e2-877f-002564c97630")
        private final Text descriptionTextControl;

        @objid ("ac52b093-55b7-11e2-877f-002564c97630")
        public DescriptionTextUpdater(Text descriptionTextControl) {
            this.descriptionTextControl = descriptionTextControl;
        }

        @objid ("ac52b096-55b7-11e2-877f-002564c97630")
        @Override
        public void selectionChanged(SelectionChangedEvent event) {
            if (event.getSelection() instanceof IStructuredSelection) {
                final Object selectedElement = ((IStructuredSelection) event.getSelection()).getFirstElement();
                if (selectedElement instanceof StyleKey) {
                    final StyleKey selectedKey = (StyleKey) selectedElement;
                    String s = selectedKey.getTooltip();
                    if (s == null || s.isEmpty()) {
                        s = "missing tooltip resource for " + selectedKey.getId();
                    }
                    this.descriptionTextControl.setText(s);
                } else {
                    this.descriptionTextControl.setText("");
                }
            }
        }

    }

}
