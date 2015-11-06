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
                                    

package org.modelio.diagram.editor;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.modelio.diagram.editor.plugin.DiagramEditor;
import org.modelio.diagram.elements.common.abstractdiagram.DiagramPersistence;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.api.transactions.ITransaction;
import org.modelio.vcore.session.api.transactions.ITransactionSupport;
import org.modelio.vcore.smkernel.IllegalModelManipulationException;

/**
 * Redefine Eclipse command stack to use our transaction instead of Eclipse one;
 */
@objid ("6590b73a-33f7-11e2-95fe-001ec947c8cc")
public class DiagramCommandStack extends CommandStack {
    /**
     * Used by API to prevent unnecessary save when working on lots of modifications.
     */
    @objid ("6590b73e-33f7-11e2-95fe-001ec947c8cc")
    private boolean batchMode = false;

    @objid ("6590b73c-33f7-11e2-95fe-001ec947c8cc")
    private ITransactionSupport session;

    @objid ("6590b73d-33f7-11e2-95fe-001ec947c8cc")
    private GmAbstractDiagram diagram;

    /**
     * Initialize the command stack.
     * @param iCoreSession a modeling session
     * @param diagramModel The diagram model to handle.
     */
    @objid ("6590b740-33f7-11e2-95fe-001ec947c8cc")
    public DiagramCommandStack(ICoreSession iCoreSession, GmAbstractDiagram diagramModel) {
        this.session = iCoreSession.getTransactionSupport();
        this.diagram = diagramModel;
    }

    @objid ("6590b745-33f7-11e2-95fe-001ec947c8cc")
    @Override
    public boolean canRedo() {
        return this.session.hasRedo();
    }

    @objid ("6590b74a-33f7-11e2-95fe-001ec947c8cc")
    @Override
    public boolean canUndo() {
        return this.session.hasUndo();
    }

    @objid ("6590b74f-33f7-11e2-95fe-001ec947c8cc")
    @SuppressWarnings("deprecation")
    @Override
    public void undo() {
        Command command = null;
        
        notifyListeners(command, PRE_UNDO);
        try {
            this.session.undo();
            notifyListeners();
        } finally {
            notifyListeners(command, POST_UNDO);
        }
    }

    @objid ("6590b752-33f7-11e2-95fe-001ec947c8cc")
    @SuppressWarnings("deprecation")
    @Override
    public void redo() {
        if (!canRedo())
            return;
        Command command = null;
        notifyListeners(command, PRE_REDO);
        try {
            this.session.redo();
            notifyListeners();
        } finally {
            notifyListeners(command, POST_REDO);
        }
    }

    @objid ("6590b755-33f7-11e2-95fe-001ec947c8cc")
    @Override
    public int getUndoLimit() {
        return -1;
    }

    @objid ("6590b75a-33f7-11e2-95fe-001ec947c8cc")
    @Override
    public void flush() {
        throw new UnsupportedOperationException();
    }

    @objid ("6590b75d-33f7-11e2-95fe-001ec947c8cc")
    @Override
    public boolean isDirty() {
        return this.session.hasUndo();
    }

    @objid ("6590b762-33f7-11e2-95fe-001ec947c8cc")
    @SuppressWarnings("deprecation")
    @Override
    public void execute(Command command) {
        if (command == null || !command.canExecute())
            return;
        
        notifyListeners(command, PRE_EXECUTE);
        
        try (ITransaction t = this.session.createTransaction(command.getLabel())){
                command.execute();
                if (!this.batchMode) {
                    DiagramPersistence.saveDiagram(this.diagram);
                }
                t.commit();
                notifyListeners();
        } catch (IllegalModelManipulationException e) {
            DiagramEditor.LOG.error(e);
            this.diagram.refreshAllFromObModel();
        } finally {
            notifyListeners(command, POST_EXECUTE);
        }
    }

    @objid ("6590b766-33f7-11e2-95fe-001ec947c8cc")
    @Override
    public void markSaveLocation() {
        // Nothing to do.
    }

    /**
     * Sets the bacth mode on/off. Should only be used by API.
     * @param batchMode true if batch mode should be engaged (ie no more automatic save of the diagram).
     */
    @objid ("6590b769-33f7-11e2-95fe-001ec947c8cc")
    public void setBatchMode(boolean batchMode) {
        this.batchMode = batchMode;
    }

}
