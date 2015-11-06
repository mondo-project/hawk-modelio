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
                                    

package org.modelio.linkeditor.rightdepth;

import javax.annotation.PostConstruct;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Spinner;
import org.modelio.linkeditor.plugin.LinkEditor;
import org.modelio.linkeditor.view.LinkEditorView;

@objid ("1b58208a-5e33-11e2-b81d-002564c97630")
public class RightDepthSpinner {
    @objid ("27143e7b-da01-4657-9e03-c1a157185dac")
    private Spinner spinner;

    @objid ("1b582091-5e33-11e2-b81d-002564c97630")
    @PostConstruct
    protected Control createControl(final Composite parent, MPart part) {
        if (!(part.getObject() instanceof LinkEditorView)) {
            return null;
        }
        final LinkEditorView linkEditorView = (LinkEditorView) part.getObject();
        
        this.spinner = new Spinner(parent, SWT.BORDER);
        this.spinner.setMinimum(0);
        this.spinner.setValues(linkEditorView.getOptions().getRightDepth(), 0, 10, 0, 1, 1);
        this.spinner.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                linkEditorView.getOptions().setRightDepth(getSpinner().getSelection());
                linkEditorView.refreshFromCurrentSelection();
            }
        });
        this.spinner.setToolTipText(LinkEditor.I18N.getString("RightDepthSpinner.tooltip"));
        this.spinner.pack();
        return this.spinner;
    }

    @objid ("ea5f3072-5bd4-473f-abd0-23bbcfacacdf")
    Spinner getSpinner() {
        return this.spinner;
    }

    @objid ("ef224486-774b-4f39-870d-9a99304ffe91")
    public void setSpinnerValue(final int depth) {
        Display.getDefault().asyncExec(new Runnable() {            
            @Override
            public void run() {
                getSpinner().setValues(depth, 0, 10, 0, 1, 1);
            }
        });
    }

}
