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
                                    

package org.modelio.diagram.elements.core.commands;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.commands.Command;
import org.modelio.diagram.elements.core.model.GmAbstractObject;
import org.modelio.gproject.model.api.MTools;

/**
 * Command that updates the layoutdata of a GmAbstractObject.
 */
@objid ("7f456631-1dec-11e2-8cad-001ec947c8cc")
public class NodeChangeLayoutCommand extends Command {
    @objid ("7f47c855-1dec-11e2-8cad-001ec947c8cc")
    private GmAbstractObject model;

    @objid ("7f47c856-1dec-11e2-8cad-001ec947c8cc")
    private Object layoutData;

    @objid ("7f47c857-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void execute() {
        if (this.model != null)
            this.model.setLayoutData(this.layoutData);
    }

    /**
     * Set the constraint that will be the new layoutData.
     * @param constraint the new constraint.
     */
    @objid ("7f47c85a-1dec-11e2-8cad-001ec947c8cc")
    public void setConstraint(Object constraint) {
        this.layoutData = constraint;
    }

    /**
     * Set the model that will be updated.
     * @param model the model to update. Must be a {@link GmAbstractObject}
     */
    @objid ("7f47c85e-1dec-11e2-8cad-001ec947c8cc")
    public void setModel(Object model) {
        this.model = (GmAbstractObject) model;
    }

    @objid ("7f47c862-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public boolean canExecute() {
        return MTools.getAuthTool().canModify(this.model.getDiagram().getRelatedElement());
    }

}
