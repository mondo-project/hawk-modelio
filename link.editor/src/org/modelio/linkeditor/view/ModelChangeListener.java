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
                                    

package org.modelio.linkeditor.view;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.widgets.Display;
import org.modelio.vcore.session.api.model.change.IModelChangeEvent;
import org.modelio.vcore.session.api.model.change.IModelChangeListener;

/**
 * Model change listener that refreshes the link editor in the SWT thread.
 */
@objid ("1bab70ae-5e33-11e2-b81d-002564c97630")
class ModelChangeListener implements IModelChangeListener, Runnable {
    @objid ("1bab70b2-5e33-11e2-b81d-002564c97630")
    private LinkEditorView view;

    @objid ("1bab70b3-5e33-11e2-b81d-002564c97630")
    public ModelChangeListener(final LinkEditorView view) {
        this.view = view;
    }

    /**
     * Invoked when the model has changed.
     * 
     * Just force a refresh.
     * @param session The modeling session.
     * @param event Delta between the beginning and the end of the transaction.
     * @joni-public
     */
    @objid ("1bab70b7-5e33-11e2-b81d-002564c97630")
    @Override
    public void modelChanged(IModelChangeEvent event) {
        Display.getDefault().asyncExec(this);
    }

    /**
     * Just forces a refresh.
     */
    @objid ("1bab70bf-5e33-11e2-b81d-002564c97630")
    @Override
    public void run() {
        this.view.refreshFromCurrentSelection();
    }

}
