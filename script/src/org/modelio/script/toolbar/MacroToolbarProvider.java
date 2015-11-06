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
                                    

package org.modelio.script.toolbar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.modelio.script.handlers.RunMacroHandler;
import org.modelio.script.macro.IMacroService;
import org.modelio.script.macro.catalog.Macro;
import org.modelio.script.plugin.Script;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Class used to fill a dynamic macro tool bar from the catalog.
 * <p>Relies on a dynamic ToolControl in the e4 model.</p>
 */
@objid ("c915c271-1ed0-4b06-baf2-b2a6ef4badce")
public class MacroToolbarProvider {
    @objid ("3c1218c8-bf8a-42ec-8e64-27d9a88e89c7")
    @Inject
    private IMacroService macroService;

    @objid ("24949fca-15ce-44b5-8be1-f54399e2a013")
    @Inject
    private MApplication application;

    @objid ("d4748348-7487-4db1-b15d-daa8de66f8ce")
    @Inject
    protected EPartService partService;

    @objid ("a6210aed-3d18-4f14-937d-e5b3223a7be9")
    protected ToolBar macroToolbar;

    @objid ("db73dc38-c0f7-4d23-9688-61e4567362a7")
    @PostConstruct
    void createWidget(final Composite parent) {
        // Create a toolbar.
        this.macroToolbar = new ToolBar(parent,  SWT.FLAT | SWT.WRAP | SWT.RIGHT);
    }

    @objid ("a8d95c61-19da-4f32-af8b-3dc2fce0d571")
    @Inject
    @Optional
    void onSelectionChanged(@SuppressWarnings("unused")
@Named(IServiceConstants.ACTIVE_SELECTION) final IStructuredSelection selection) {
        deleteToolItems();
        
        createToolItems(this.macroService.getMacros(getSelectedElements()));
        
        // Layout the toolbar, as e4 is lost with it...
        this.macroToolbar.pack();
        this.macroToolbar.getShell().layout(new Control[] { this.macroToolbar }, SWT.DEFER);
    }

    /**
     * Create new tool items from a list of macros.
     * <p>Only macros shown in the toolbar are returned.</p>
     * @param entries the macros to build the toolbar.
     * @return a list of menu elements.
     */
    @objid ("15d21922-45b9-4216-9aa9-ecbeaef56daa")
    private List<ToolItem> createToolItems(List<Macro> entries) {
        List<ToolItem> items = new ArrayList<>();
        for (Macro entry : entries) {
            if (entry.shownInToolbar()) {
                items.add(createToolItem(entry));
            }
        }
        return items;
    }

    /**
     * Create a new tool item from a macro.
     * @param entry the macro to create the tool item from.
     * @return a new toolbar item.
     */
    @objid ("554daeb8-49f1-4a03-b6da-6ac75b6f7c56")
    private ToolItem createToolItem(final Macro entry) {
        // create a new handled item
        ToolItem item = new ToolItem(this.macroToolbar, SWT.PUSH);
        
        // compute label, tooltip and icon
        item.setToolTipText(entry.getDescription());
        if (entry.getIconPath() != null) {
            try {
                // Instanciate a new image: it will be disposed with the item itself, when selection changes
                item.setImage(new Image(null, entry.getIconPath().toString()));
            } catch (Exception e) {
                // invalid image, set the text instead 
                item.setText(entry.getName());
            }
        } else {
            // no image, set the text instead
            item.setText(entry.getName());
        }
        
        // Set the macro as data
        item.setData(entry);
        
        item.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                RunMacroHandler runner = new RunMacroHandler();
                runner.execute(MacroToolbarProvider.this.partService, MacroToolbarProvider.this.application.getSelectedElement(), entry.getScriptPath().toString());
            }
        
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // Nothing to do
            }
        });
        return item;
    }

    @objid ("30d24b1f-d930-4409-9716-0a409cc0a66b")
    private void deleteToolItems() {
        for (ToolItem item : this.macroToolbar.getItems()) {
            deleteToolItem(item);
        }
    }

    @objid ("74a41c21-7708-4b0d-a8df-9db5b352a637")
    private void deleteToolItem(ToolItem item) {
        final Image image = item.getImage();
        if (image != null) {
            item.setImage(null);
            image.dispose();
        }
        item.dispose();
    }

    /**
     * Get the currently selected elements, or an empty collection.
     * @return the selected elements.
     */
    @objid ("48671e7b-c7b0-411f-892f-35715639df9a")
    private Collection<MObject> getSelectedElements() {
        Collection<MObject> ret = new ArrayList<>();
        
        // Get the active selection from the application, to avoid context-related issues when opening the same diagram several times...
        IStructuredSelection selection = (IStructuredSelection) this.application.getContext().get(IServiceConstants.ACTIVE_SELECTION);
        if (selection != null) {
            for (Object obj : selection.toList()) {
                if (obj instanceof MObject) {
                    ret.add((MObject) obj);
                } else if (obj instanceof IAdaptable) {
                    IAdaptable adaptable = (IAdaptable) obj;
                    final MObject adapter = (MObject) adaptable.getAdapter(MObject.class);
                    if (adapter != null) {
                        ret.add(adapter);
                    }
                }
            }
        }
        return ret;
    }

}
