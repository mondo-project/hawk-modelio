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
import org.modelio.diagram.editor.plugin.DiagramEditor;
import org.modelio.diagram.elements.common.abstractdiagram.DiagramPersistence;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.model.IGmLinkFactory;
import org.modelio.diagram.elements.core.model.IGmNodeFactory;
import org.modelio.diagram.elements.core.model.ModelManager;
import org.modelio.diagram.elements.gmfactory.GmLinkFactory;
import org.modelio.diagram.elements.gmfactory.GmNodeFactory;
import org.modelio.diagram.persistence.PersistenceException;
import org.modelio.metamodel.diagrams.AbstractDiagram;

/**
 * Diagram editor input.
 * 
 * @author phv
 */
@objid ("65931995-33f7-11e2-95fe-001ec947c8cc")
public abstract class DiagramEditorInput {
    @objid ("65931997-33f7-11e2-95fe-001ec947c8cc")
    private AbstractDiagram diagram;

    @objid ("65931998-33f7-11e2-95fe-001ec947c8cc")
    private GmAbstractDiagram model;

    /**
     * Initialize the editor input.
     * <p>
     * Creates the diagram graphic model and load it from the diagram model element.
     * @param modelServices
     * @param moduleService
     * @param session a modeling session.
     * @param activationService the activation service, to open new editors.
     * @param diagram the diagram to edit.
     */
    @objid ("65931999-33f7-11e2-95fe-001ec947c8cc")
    public DiagramEditorInput(AbstractDiagram diagram, ModelManager modelManager) {
        super();
        this.diagram = diagram;
        
        this.model = createModel(modelManager, diagram);
        
        // Setup the node and link factories
        this.model.setGmNodeFactory(createGmNodeFactory(this.model));
        this.model.setGmLinkCreationFactory(createGmLinkFactory(this.model));
        
        // Make the diagram visible
        this.model.setVisible(true);
        
        // Read the model from saved string (and update from ObModel) .
        try {
            DiagramPersistence.loadDiagram(this.model);
        } catch (PersistenceException pe) {
            // Failed to read string, log error and undo everything.
            DiagramEditor.LOG.error(pe);
            this.model.dispose();
            this.model.delete();
            this.model = null;
        }
    }

    @objid ("659319b8-33f7-11e2-95fe-001ec947c8cc")
    @SuppressWarnings("rawtypes")
    public Object getAdapter(Class adapter) {
        if (adapter.isInstance(this.diagram)) {
            return this.diagram;
        }
        return null;
    }

    @objid ("659319be-33f7-11e2-95fe-001ec947c8cc")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof DiagramEditorInput)) {
            return false;
        }
        DiagramEditorInput other = (DiagramEditorInput) obj;
        if (this.diagram == null) {
            if (other.diagram != null) {
                return false;
            }
        } else if (!this.diagram.equals(other.diagram)) {
            return false;
        }
        return true;
    }

    /**
     * @return the edited diagram.
     */
    @objid ("65957bea-33f7-11e2-95fe-001ec947c8cc")
    public AbstractDiagram getDiagram() {
        return this.diagram;
    }

    @objid ("65957bef-33f7-11e2-95fe-001ec947c8cc")
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.diagram == null) ? 0 : this.diagram.hashCode());
        return result;
    }

    /**
     * Release all acquired resources.
     */
    @objid ("65957bf4-33f7-11e2-95fe-001ec947c8cc")
    public void dispose() {
        if (this.model != null) {
            this.model.dispose();
            this.model = null;
        }
    }

    /**
     * Creates a new diagram model.
     * @param modelManager A model manager to be used by the diagram model.
     * @param aDiagram The Ob diagram to open.
     * @return a new GmAbstractDiagram.
     */
    @objid ("65957bf7-33f7-11e2-95fe-001ec947c8cc")
    protected abstract GmAbstractDiagram createModel(ModelManager modelManager, AbstractDiagram aDiagram);

    /**
     * Instantiates the model links factory (use a standard factory). Derived classes can redefine this method to either:
     * <ul>
     * <li>provide another full-blown factory of their own</li>
     * <li>register an extension to the standard factory returned by the super method</li>
     * </ul>
     * @param gmDiagram The diagram model.
     * @return the model links factory.
     */
    @objid ("65957bfc-33f7-11e2-95fe-001ec947c8cc")
    protected IGmLinkFactory createGmLinkFactory(GmAbstractDiagram gmDiagram) {
        return GmLinkFactory.getInstance();
    }

    /**
     * Instantiates the model nodes factory.. Derived classes can redefine this method to either:
     * <ul>
     * <li>provide another full-blown factory of their own</li>
     * <li>register an extension to the standard factory returned by the super method</li>
     * </ul>
     * @param gmDiagram The diagram model.
     * @return the model nodes factory.
     */
    @objid ("65957c02-33f7-11e2-95fe-001ec947c8cc")
    protected IGmNodeFactory createGmNodeFactory(GmAbstractDiagram gmDiagram) {
        return GmNodeFactory.getInstance();
    }

    /**
     * @return the edited diagram graphic model.
     */
    @objid ("65957c08-33f7-11e2-95fe-001ec947c8cc")
    public GmAbstractDiagram getGmDiagram() {
        return this.model;
    }

}
