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
package org.modelio.edition.html.view.actions;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolItem;
import org.modelio.edition.html.view.IRichText;

/**
 * The abstract implementation of a rich text action.
 * 
 * @author Kelvin Low
 * @author Jeff Hardy
 * @since 1.0
 */
@objid ("2f9a07bc-c7bf-4417-a01c-036a7326ae21")
public abstract class RichTextAction extends Action {
    @objid ("9ebb73e3-afb9-4aba-9a54-6f6cf5b9051c")
    protected final IRichText mRichText;

    @objid ("8f25f535-be5a-46b5-960c-0349551fa3b1")
    protected ToolItem toolItem;

    @objid ("b170e56e-7229-48c8-a213-c855cf68cbba")
    private Collection<IAction> fMenuActions;

    @objid ("9033faf6-761e-4b55-b492-936d31cf93e4")
    protected IMenuCreator menuCreator = new IMenuCreator() {
    	private MenuManager dropDownMenuMgr;
        /**
         * Creates the menu manager for the drop-down.
         */
        private void createDropDownMenuMgr() {
            if (this.dropDownMenuMgr == null) {
                this.dropDownMenuMgr = new MenuManager();
                for (Iterator<IAction> iter = getMenuActions().iterator();iter.hasNext();) {
                	IAction item = iter.next();
                    this.dropDownMenuMgr.add(item);
                }
            }
        }
        @Override
        public Menu getMenu(Control parent) {
            createDropDownMenuMgr();
            return this.dropDownMenuMgr.createContextMenu(parent);
        }
        @Override
        public Menu getMenu(Menu parent) {
            createDropDownMenuMgr();
            Menu menu = new Menu(parent);
            IContributionItem[] items = this.dropDownMenuMgr.getItems();
            for (int i = 0; i < items.length; i++) {
                IContributionItem item = items[i];
                IContributionItem newItem = item;
                if (item instanceof ActionContributionItem) {
                    newItem = new ActionContributionItem(
                            ((ActionContributionItem) item).getAction());
                }
                newItem.fill(menu, -1);
            }
            return menu;
        }
        /* (non-Javadoc)
         * @see org.eclipse.jface.action.IMenuCreator#dispose()
         */
        @Override
        public void dispose() {
            if (this.dropDownMenuMgr != null) {
                this.dropDownMenuMgr.dispose();
                this.dropDownMenuMgr = null;
            }
        }
    };

    /**
     * Creates a new instance.
     * @param richText a rich text control
     * @param style style bits
     */
    @objid ("35f612ca-7583-4c61-bf1a-7bf333498d14")
    public RichTextAction(IRichText richText, int style) {
        super(null, style);
        this.mRichText = richText;
    }

    /**
     * Returns <code>true</code> if this action should be disabled when the
     * rich text editor is in readonly mode.
     * @return <code>true</code> if this action should be disabled when the
     * rich text editor is in readonly mode.
     */
    @objid ("19cc82b0-445b-4b48-8d5f-971bf0bbad09")
    public boolean disableInReadOnlyMode() {
        return true;
    }

    /**
     * @return <code>true</code> if this action should be disabled when the
     * rich text editor is in source edit mode.
     */
    @objid ("063b0e68-6a8c-4a0f-85f8-a742d70941a6")
    public boolean disableInSourceMode() {
        return true;
    }

    @objid ("0b44e55c-8fd1-43ef-b6cf-2227e1530ead")
    @Override
    public void run() {
        execute(this.mRichText);
    }

    @objid ("71b474b9-07d2-40a6-898d-0c968a147c7e")
    @Override
    public String getId() {
        return getClass().getCanonicalName();
    }

    @objid ("a151059a-d07e-49be-b7e0-3e76b6b3da4c")
    protected Collection<IAction> getMenuActions() {
        if (this.fMenuActions != null) 
            return this.fMenuActions;
        else
            return Collections.emptyList();
    }

    /**
     * Add an action to the sub/contextual menu
     * @param action action to add
     */
    @objid ("cdc77cbd-9a5f-4e61-8900-9d41aaadb770")
    public void addActionToMenu(IAction action) {
        if (this.fMenuActions == null)
            this.fMenuActions = new LinkedHashSet<>(3);
        this.fMenuActions.add(action);
    }

    /**
     * Execute this rich text action on the given rich text object.
     * @param richText the rich text object.
     */
    @objid ("edbb369a-065b-4ea4-b8ee-19af719d4d5e")
    public abstract void execute(IRichText richText);

}
