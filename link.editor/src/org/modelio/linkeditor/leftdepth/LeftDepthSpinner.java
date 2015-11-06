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
                                    

package org.modelio.linkeditor.leftdepth;

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

@objid ("1b49d867-5e33-11e2-b81d-002564c97630")
public class LeftDepthSpinner {
    @objid ("d422be42-f83e-4140-9b7d-bb8b9088222b")
    private Spinner spinner;

    @objid ("1b49d86e-5e33-11e2-b81d-002564c97630")
    @PostConstruct
    protected Control createControl(final Composite parent, MPart part) {
        if (!(part.getObject() instanceof LinkEditorView)) {
            return null;
        }
        
        final LinkEditorView linkEditorView = (LinkEditorView) part.getObject();
        
        this.spinner = new Spinner(parent, SWT.BORDER);
        this.spinner.setMinimum(0);
        this.spinner.setValues(linkEditorView.getOptions().getLeftDepth(), 0, 10, 0, 1, 1);
        this.spinner.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                linkEditorView.getOptions().setLeftDepth(getSpinner().getSelection());
                linkEditorView.refreshFromCurrentSelection();
            }
        });
        this.spinner.setToolTipText(LinkEditor.I18N.getString("LeftDepthSpinner.tooltip"));
        this.spinner.pack();
        return this.spinner;
    }

    @objid ("e18215a3-db3b-4bf3-b81d-cebd6d3c1357")
    Spinner getSpinner() {
        return this.spinner;
    }

    @objid ("b2a4ae9e-6daa-4eb9-a066-89558140c818")
    public void setSpinnerValue(final int depth) {
        Display.getDefault().asyncExec(new Runnable() {            
            @Override
            public void run() {
                getSpinner().setValues(depth, 0, 10, 0, 1, 1);
            }
        });
    }

}
