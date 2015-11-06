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
package org.modelio.edition.html.epfcommon.ui.actions;

import java.util.Collection;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * Wraps a {@link CCombo} in a {@link ContributionItem} for use in a toolbar
 * <p>
 * Does not use a ComboViewer because of tabbing issues - see bug 78885
 * @author Jeff Hardy
 */
@objid ("9c470320-bb5c-45ce-8444-57e816b915a9")
public class CComboContributionItem extends ContributionItem {
    @objid ("65204f03-0436-428d-b48b-e0fea90cd31f")
    protected int style;

    @objid ("65da9ccf-24d2-408d-b6b3-37aad250397e")
    protected Collection<String> input;

    @objid ("df916a8b-9879-45a4-ae41-440c8be8bedf")
    protected CCombo CCombo;

    @objid ("4799e937-310e-4431-a429-ea3100ac2328")
    protected ToolItem toolItem;

    @objid ("6ba690e4-615c-4422-8da9-9bca74eb58c8")
    protected CoolItem coolItem;

    /**
     * Creates a new instance.
     * @param style style bits.
     */
    @objid ("39fa7e95-8ed5-4376-918f-8052832c533b")
    public CComboContributionItem(int style) {
        super();
        this.style = style;
    }

    @objid ("7b2c6fa6-25eb-43e4-adc8-ffc785e89e4f")
    @Override
    public void fill(ToolBar parent, int index) {
        this.toolItem = new ToolItem(parent, SWT.SEPARATOR);
        Control box = createControl(parent);
        this.toolItem.setControl(box);
        Point preferredSize = this.CCombo.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
        this.toolItem.setWidth(preferredSize.x);
    }

    @objid ("c9dd2c3d-5fc0-4431-9767-67bdda3596fd")
    @Override
    public void fill(CoolBar coolBar, int index) {
        Control box = createControl(coolBar);
        
        if (index >= 0) {
            this.coolItem = new CoolItem(coolBar, SWT.DROP_DOWN, index);
        } else {
            this.coolItem = new CoolItem(coolBar, SWT.DROP_DOWN);
        }
        
        // Set the back reference.
        this.coolItem.setData(this);
        
        // Add the toolbar to the CoolItem widget.
        this.coolItem.setControl(box);
        
        // If the toolbar item exists then adjust the size of the cool item.
        Point toolBarSize = box.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        
        // Set the preferred size to the size of the toolbar plus trim.
        this.coolItem.setMinimumSize(toolBarSize);
        this.coolItem.setPreferredSize(toolBarSize);
        this.coolItem.setSize(toolBarSize);
    }

    @objid ("c4f6bf7d-eb8f-47a2-8aaf-92f400986631")
    @Override
    public void fill(Composite parent) {
        createControl(parent);
    }

    /**
     * Creates the control.
     */
    @objid ("f243e0f9-b59d-4d53-b1c4-c9239862106b")
    protected Control createControl(final Composite parent) {
        this.CCombo = new CCombo(parent, this.style);
        this.CCombo.setVisibleItemCount(10);
        this.CCombo.setEnabled(true);
        this.CCombo.setItems(this.input.toArray(new String[0]));
        this.CCombo.addDisposeListener(
                new DisposeListener() {
                    @Override
                    public void widgetDisposed(DisposeEvent event) {
                        dispose();
                    }
                });
        
        this.CCombo.addSelectionListener(new SelectionListener() {
                    @Override
                    public void widgetDefaultSelected(SelectionEvent e) {
                        // ignore
                    }
        
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        performSelectionChanged();
                    }
                });
        return this.CCombo;
    }

    /**
     * Returns the currently selected method configuration
     */
    @objid ("47c743cb-812a-41a7-9003-6bc6890ef9a1")
    protected int getSelectionIndex() {
        return this.CCombo.getSelectionIndex();
    }

    @objid ("cf1dfc58-7c75-4423-8ad8-aac16dca3cfe")
    protected void setInput(Collection<String> input) {
        this.input = input;
    }

    /**
     * Called when the selection has changed.
     * <p>
     * Does nothing by default. May be redefined.
     */
    @objid ("82d86c47-0947-4733-a0fa-75a5a5e4783e")
    protected void performSelectionChanged() {
        // ignore
    }

    /**
     * @return the combo widget
     */
    @objid ("3edeca99-e649-43dd-8d87-98cb13d2c709")
    public CCombo getCCombo() {
        return this.CCombo;
    }

    /**
     * @return the toolbar item.
     */
    @objid ("3c838625-6344-479c-a157-14f0d58e4800")
    public ToolItem getToolItem() {
        return this.toolItem;
    }

    /**
     * @return the coolbar item.
     */
    @objid ("0a5f96e0-43f4-4c8f-b7b6-7783e504226a")
    public CoolItem getCoolItem() {
        return this.coolItem;
    }

}
