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

import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.contexts.RunAndTrack;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.modelio.app.core.events.ModelioEventTopics;
import org.modelio.app.core.picking.IModelioPickingService;
import org.modelio.app.core.picking.IPickingSession;
import org.modelio.diagram.elements.core.model.IGmObject;
import org.modelio.diagram.symbol.plugin.DiagramSymbol;

/**
 * Diagram symbol view.
 * <p>
 * Displays and allow edition of the selected graphic model properties.
 */
@objid ("ac5129e6-55b7-11e2-877f-002564c97630")
public class SymbolView {
    /**
     * The ID of the view as specified by the extension.
     */
    @objid ("ac5129e9-55b7-11e2-877f-002564c97630")
    public static final String VIEW_ID = "org.modelio.diagram.symbol.SymbolViewID";

    @objid ("37c580f0-9ff6-46d3-8063-ceea59e60ecd")
    private volatile boolean isPinned;

    @objid ("325f9f43-5991-11e2-8bfd-001ec947ccaf")
    private SymbolPanelProvider symbolPanel;

    /**
     * The constructor.
     */
    @objid ("ac5129f5-55b7-11e2-877f-002564c97630")
    public SymbolView() {
    }

    @objid ("325f9f4f-5991-11e2-8bfd-001ec947ccaf")
    @Override
    protected void finalize() {
        if (this.symbolPanel != null)
            DiagramSymbol.LOG.warning("SymbolView.dispose() is not called, @PreDestroy not used, dispose() can be deleted.");
        dispose();
    }

    /**
     * Called by the framework to create the view and initialize it.
     * @param parent the composite the view must add its content into.
     * @param part the part model
     * @param svc the model service
     * @param pickingService the Modelio picking service
     */
    @objid ("325d3d02-5991-11e2-8bfd-001ec947ccaf")
    @PostConstruct
    public void createControls(Composite parent, MPart part, EModelService svc, IModelioPickingService pickingService) {
        this.symbolPanel = new SymbolPanelProvider(pickingService);
        this.symbolPanel.createPanel(parent);
        
        part.getContext().runAndTrack(new Updater());
    }

    /**
     * Activated when this controller is discarded.
     */
    @objid ("ac52b070-55b7-11e2-877f-002564c97630")
    @PreDestroy
    public void dispose() {
        this.symbolPanel = null;
    }

    /**
     * Get the symbol view panel.
     * @return the symbol view panel.
     */
    @objid ("325f9f57-5991-11e2-8bfd-001ec947ccaf")
    public SymbolPanelProvider getPanel() {
        return this.symbolPanel;
    }

    /**
     * Get the selected IGmObject.
     * @return the current element displayed in the symbol view.
     */
    @objid ("ac52b073-55b7-11e2-877f-002564c97630")
    public IGmObject getSelectedSymbol() {
        return this.symbolPanel.getInput();
    }

    /**
     * Called when the selection changes in the workbench.<br>
     * Its responsibility is to set the NotesView's current element.
     * @param sel the current modelio selection.
     */
    @objid ("325f9f52-5991-11e2-8bfd-001ec947ccaf")
    void update(IStructuredSelection sel) {
        // we ignore the selection when one of the following conditions is matched:
        // - it is 'null'
        // - we are pinned
        // - the selection is our own selection
        // - the selection is same selection
        if (sel == null || isPinned()) {
            return;
        }
        
        // currently the selection must be unique (one element) and it must be a GmObject
        final List<?> selectedElements = sel.toList();
        if (selectedElements.size() == 1 && selectedElements.get(0) instanceof IAdaptable) {
            final IAdaptable selectedAdapter = (IAdaptable) selectedElements.get(0);
            final IGmObject gmObject = (IGmObject) selectedAdapter.getAdapter(IGmObject.class);
            this.symbolPanel.setSelectedSymbol(gmObject);
        } else {
            this.symbolPanel.setSelectedSymbol(null);
        }
    }

    @objid ("ff46da9a-b36b-44a5-abae-3ff4c1f095b0")
    @Inject
    @Optional
    @SuppressWarnings("unused")
    void onPickingStart(@EventTopic(ModelioEventTopics.PICKING_START) final IPickingSession session) {
        // Temporary pin the view when picking is in progress
        setPinned(true);
    }

    @objid ("1900b032-4768-47ca-a74f-68f355ef0f14")
    @Inject
    @SuppressWarnings("unused")
    @Optional
    void onPickingSessionStop(@EventTopic(ModelioEventTopics.PICKING_STOP) final IPickingSession session) {
        // Unpit the view
        setPinned(false);
    }

    @objid ("849ed246-cf9e-4a14-b0b9-17cdeb6ad4a0")
    private boolean isPinned() {
        return this.isPinned;
    }

    @objid ("bc973ffd-17a7-4df3-a064-40c82726d0ac")
    private void setPinned(boolean val) {
        this.isPinned = val;
    }

    /**
     * Listener activated when the selection changes in the workbench.<br>
     * Its responsibility is to set the NotesView's current element.
     */
    @objid ("325f9f44-5991-11e2-8bfd-001ec947ccaf")
    private class Updater extends RunAndTrack {
        @objid ("325f9f46-5991-11e2-8bfd-001ec947ccaf")
        public Updater() {
            // NOOP
        }

        @objid ("325f9f48-5991-11e2-8bfd-001ec947ccaf")
        @SuppressWarnings("synthetic-access")
        @Override
        public boolean changed(IEclipseContext context) {
            if (SymbolView.this.symbolPanel == null)
                return false;
            
            final Object sel = context.get(IServiceConstants.ACTIVE_SELECTION);
            if (sel instanceof IStructuredSelection)
                update((IStructuredSelection) sel);
            return true;
        }

    }

}
