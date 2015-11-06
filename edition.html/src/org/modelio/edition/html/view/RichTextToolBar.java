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

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolItem;
import org.modelio.edition.html.view.actions.IRichTextAction;
import org.modelio.edition.html.view.actions.RichTextAction;
import org.modelio.edition.html.view.actions.RichTextComboAction;

/**
 * The default rich text tool bar implementation.
 * 
 * Split into 2 toolbars (one for CCombos, one for buttons) for tabbing purposes.
 * 
 * @author Kelvin Low
 * @author Jeff Hardy
 * @since 1.0
 */
@objid ("c0c75de6-4e94-4961-a198-8d421f426850")
class RichTextToolBar implements IRichTextToolBar {
// If true, add a new tool bar.
    @objid ("d0dd7eb2-3734-4d6f-9b7a-f98e1e80d371")
    private boolean addToolBar = true;

// The parent rich text control.
    @objid ("7354d81c-3fdd-4d75-8371-4f387ecd6cea")
    private IRichText richText;

// The current tool bar manager used to populate the tool actions.
    @objid ("acdc3bd0-cfbc-4061-bd49-a01b08bacd62")
    private ToolBarManager toolbarMgr;

// The current tool bar manager used to populate the tool actions.
    @objid ("fad2d7a9-fc2d-45d3-a58f-069bcd0cf191")
    private ToolBarManager toolbarMgrCombo;

// The action items in the tool bar(s).
    @objid ("8cd68baf-40b6-4d62-a9c6-18ade2a0c7b8")
    private List<Object> actionItems = new ArrayList<>();

    @objid ("50a94b6d-1fc8-4b0b-9d10-3408b7ba1aa9")
    protected Composite parent;

    /**
     * Creates a new instance.
     * @param parent the parent composite
     * @param style the tool bar style
     * @param richText the parent rich text control
     */
    @objid ("e3bc1ab9-15b9-478b-8dd5-d442ae0a6c3a")
    public RichTextToolBar(Composite parent, int style, IRichText richText) {
        this.parent = parent;
        this.richText = richText;
        
        addToolBar();
    }

    @objid ("1cc636ad-6bd9-4ce2-ba93-27baf0005c1a")
    @Override
    public void addAction(final IAction action) {
        if (action != null) {
            ActionContributionItem item = new ActionContributionItem(action);
            this.toolbarMgr.add(item);
            this.toolbarMgr.update(true);
            this.actionItems.add(item);
        }
    }

    /**
     * Adds a combo action to the tool bar.
     * @param item the action to add
     */
    @objid ("258ed969-78a2-4bf8-ab0c-03bb481bfc50")
    @Override
    public void addAction(final RichTextComboAction item) {
        if (item != null) {
            item.init();
            this.toolbarMgrCombo.add(item);            
            this.toolbarMgrCombo.update(true);
            this.actionItems.add(item);           
        }
    }

    /**
     * Adds a separator to the tool bar.
     */
    @objid ("f6f61462-2b31-4aab-8798-d9ea0c689682")
    @Override
    public void addSeparator() {
        this.toolbarMgr.add(new Separator());
    }

    /**
     * Updates the toolbar state.
     * <p>
     * Enables/disables actions depending on the currently selected
     * RichTextEditor tab (RichText vs. HTML)
     * @param editable specifies whether to enable non-ReadOnly commands
     */
    @objid ("10847b2c-eb40-4a8b-9704-56d5d34d42a5")
    @Override
    public void updateToolBar(boolean editable) {
        boolean richTextMode = true;
        if (this.richText instanceof RichTextEditor
                && ((RichTextEditor) this.richText).isHTMLTabSelected()) {
            richTextMode = false;
        }
        for (Object item : this.actionItems) {
            if (item instanceof ToolItem) {
                boolean enabledToolItem = true;
                ToolItem toolItem = (ToolItem) item;
                IRichTextAction action = (IRichTextAction) toolItem.getData();
                if (action != null && !editable
                        && action.disableInReadOnlyMode()) {
                    enabledToolItem = false;
                }
                if (action != null && !richTextMode
                        && action.disableInSourceMode()) {
                    enabledToolItem = false;
                }
                toolItem.setEnabled(enabledToolItem);
            } else if (item instanceof Combo) {
                if (richTextMode && editable) {
                    ((Combo) item).setEnabled(true);
                } else {
                    ((Combo) item).setEnabled(false);
                }
            } else if (item instanceof ActionContributionItem) {
                boolean enabledToolItem = true;
                RichTextAction action = (RichTextAction) ((ActionContributionItem) item)
                        .getAction();
                if (action != null) {
                    if (!editable && action.disableInReadOnlyMode()) {
                        enabledToolItem = false;
                    }
                    if (!richTextMode && action.disableInSourceMode()) {
                        enabledToolItem = false;
                    }
                    action.setEnabled(enabledToolItem);
                }
            }
        }
    }

    /**
     * Adds a tool bar, if necessary, to contain a button action or separator.
     */
    @objid ("092a8e4d-db50-4ee9-aa9e-2174ce4c5c22")
    protected void addToolBar() {
        if (this.addToolBar) {
            this.toolbarMgrCombo = new ToolBarManager(SWT.WRAP | SWT.FLAT | this.parent.getStyle());
            this.toolbarMgrCombo.createControl(this.parent);
            this.toolbarMgr = new ToolBarManager(SWT.WRAP | SWT.FLAT | this.parent.getStyle());
            this.toolbarMgr.createControl(this.parent);
            this.addToolBar = false;
        }
    }

    @objid ("74e4527c-61ac-4b73-9a43-495306433d0b")
    @Override
    public ToolBarManager getToolbarMgr() {
        return this.toolbarMgr;
    }

    @objid ("e6414834-cfc0-4976-8dc9-4f365aecafc3")
    public ToolBarManager getToolbarMgrCombo() {
        return this.toolbarMgrCombo;
    }

}
