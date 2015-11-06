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
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.SWT;
import org.modelio.edition.html.epfcommon.ui.actions.CComboContributionItem;
import org.modelio.edition.html.view.IRichText;

/**
 * The abstract implementation of a rich text combo action.
 * 
 * @author Kelvin Low
 * @author Jeff Hardy
 * @since 1.0
 */
@objid ("ca850e22-b73b-4d03-93ab-6b8b75dd5741")
public abstract class RichTextComboAction extends CComboContributionItem {
    @objid ("53b61913-7dfa-44a7-b938-62a4c14e35e9")
    protected String toolTipText;

    @objid ("e74b094a-f8e6-4354-8540-1fbcd991f20e")
    protected boolean enabled = true;

    @objid ("c9ea0bba-9703-4731-9fc0-a1d1103d1359")
    protected boolean notifyListeners = false;

    @objid ("d98ce77a-8eca-48da-9351-cfe6b4c898d5")
    protected IRichText richText;

    /**
     * Creates a new instance.
     * @param richText a rich text control
     */
    @objid ("b64da7ba-7eef-4daf-8ea9-ef54dc1b08f4")
    public RichTextComboAction(IRichText richText) {
        super(SWT.READ_ONLY | SWT.FLAT | SWT.BORDER);
        this.richText = richText;
    }

    /**
     * Creates a new instance.
     * @param richText a rich text control
     * @param style SWT style bits.
     */
    @objid ("579d3e4c-a0e0-4888-9153-646228d1c55e")
    public RichTextComboAction(IRichText richText, int style) {
        super(SWT.READ_ONLY | SWT.FLAT | SWT.BORDER | style);
        this.richText = richText;
    }

    /**
     * Executes the action.
     * @param aRichText a rich text control
     */
    @objid ("f3052971-5a46-4449-9b94-2361bae5a553")
    public abstract void execute(IRichText aRichText);

    /**
     * @return the combo content
     */
    @objid ("bde6b1f9-ca4a-4bac-afe0-81b2df90227b")
    public abstract Collection<String> getInput();

    /**
     * Initialize the combo.
     */
    @objid ("dcfbab0f-4d19-4cd8-b7b1-5ddc9d3ba30d")
    public void init() {
        setInput(getInput());
        setNotifyListeners(true);
    }

    @objid ("956dc392-79ac-46e4-887a-381748286497")
    protected String getCComboSelection() {
        if (getCCombo() != null) {
            int index = getSelectionIndex();
            return getCCombo().getItem(index);
        }
        return null;
    }

    /**
     * Returns the tool tip for the action.
     * @return the tool tip text
     */
    @objid ("5ea2e6f0-f6b1-4bcf-81a5-eff8fbaca2db")
    public String getToolTipText() {
        return this.toolTipText;
    }

    /**
     * Sets the tool tip for the action.
     * @param toolTipText the tool tip text
     */
    @objid ("b7d55f58-52b0-4bc6-804f-42af0fb444b5")
    public void setToolTipText(String toolTipText) {
        this.toolTipText = toolTipText;
    }

    /**
     * Returns the enabled status of the action.
     * @return <code>true</code> if enabled, <code>false</code> if not
     */
    @objid ("4342b8bf-ee1a-446d-a703-f800a475569c")
    public boolean getEnabled() {
        return this.enabled;
    }

    /**
     * Enables or disables the action.
     * @param enabled if <code>true</code>, enable the action. if
     * <code>false</code>, disable it.
     */
    @objid ("fb6abfc0-59ca-4443-bd73-c56f35f8a9a8")
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Notifies listeners of a selection changes, only if {@link #isNotifyListeners()} returns <i>true</i>.
     */
    @objid ("580bb21c-3a05-4bda-bf46-68ee2ec6be57")
    @Override
    protected void performSelectionChanged() {
        if (this.notifyListeners) {
            execute(this.richText);
        }
    }

    /**
     * @return whether the combo listeners are fired
     */
    @objid ("b4a898e5-e5e4-45af-ab5f-087ded2b5223")
    public boolean isNotifyListeners() {
        return this.notifyListeners;
    }

    /**
     * @param notifyListeners <i>true</i> to enable notification
     */
    @objid ("12ea60ab-841e-4005-9e53-b94667107557")
    public void setNotifyListeners(boolean notifyListeners) {
        this.notifyListeners = notifyListeners;
    }

}
