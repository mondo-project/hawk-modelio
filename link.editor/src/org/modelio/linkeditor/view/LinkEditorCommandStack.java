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
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.modelio.linkeditor.plugin.LinkEditor;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.api.transactions.ITransaction;

/**
 * Redefine GEF command stack to use our transaction.
 */
@objid ("1b9f89e0-5e33-11e2-b81d-002564c97630")
class LinkEditorCommandStack extends CommandStack {
    @objid ("1b9f89e4-5e33-11e2-b81d-002564c97630")
    private ICoreSession session;

    /**
     * Initialize the command stack.
     * @param session a modeling session
     */
    @objid ("1b9f89e5-5e33-11e2-b81d-002564c97630")
    public LinkEditorCommandStack(ICoreSession session) {
        this.session = session;
    }

    @objid ("1b9f89e9-5e33-11e2-b81d-002564c97630")
    @Override
    public boolean canRedo() {
        return this.session.getTransactionSupport().hasRedo();
    }

    @objid ("1b9f89ee-5e33-11e2-b81d-002564c97630")
    @Override
    public boolean canUndo() {
        return this.session.getTransactionSupport().hasUndo();
    }

    @objid ("1b9f89f3-5e33-11e2-b81d-002564c97630")
    @SuppressWarnings("deprecation")
    @Override
    public void undo() {
        Command command = null;
        
        notifyListeners(command, PRE_UNDO);
        try {
            this.session.getTransactionSupport().undo();
            notifyListeners();
        } finally {
            notifyListeners(command, POST_UNDO);
        }
    }

    @objid ("1b9f89f6-5e33-11e2-b81d-002564c97630")
    @SuppressWarnings("deprecation")
    @Override
    public void redo() {
        if (!canRedo())
            return;
        Command command = null;
        notifyListeners(command, PRE_REDO);
        try {
            this.session.getTransactionSupport().redo();
            notifyListeners();
        } finally {
            notifyListeners(command, POST_REDO);
        }
    }

    @objid ("1b9f89f9-5e33-11e2-b81d-002564c97630")
    @Override
    public int getUndoLimit() {
        return -1;
    }

    @objid ("1b9f89fe-5e33-11e2-b81d-002564c97630")
    @Override
    public void flush() {
        throw new UnsupportedOperationException();
    }

    @objid ("1b9f8a01-5e33-11e2-b81d-002564c97630")
    @Override
    public boolean isDirty() {
        return this.session.getTransactionSupport().hasUndo();
    }

    @objid ("1b9f8a06-5e33-11e2-b81d-002564c97630")
    @SuppressWarnings("deprecation")
    @Override
    public void execute(Command command) {
        if (command == null || !command.canExecute())
            return;
        
        notifyListeners(command, PRE_EXECUTE);
        
        try {
            final ICoreSession transactionManager = this.session;
            
            try(ITransaction t = transactionManager.getTransactionSupport().createTransaction(command.getLabel())) {
                command.execute();
                t.commit();
                notifyListeners();
            } catch (Exception e) {
                LinkEditor.LOG.error(e);
            }
        } finally {
            notifyListeners(command, POST_EXECUTE);
        }
    }

    @objid ("1b9f8a0c-5e33-11e2-b81d-002564c97630")
    @Override
    public void markSaveLocation() {
        // Nothing to do.
    }

}
