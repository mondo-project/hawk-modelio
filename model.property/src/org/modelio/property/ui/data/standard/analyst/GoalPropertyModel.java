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
                                    

package org.modelio.property.ui.data.standard.analyst;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.app.core.activation.IActivationService;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.analyst.AnalystElement;
import org.modelio.metamodel.analyst.Goal;

/**
 * <i>Goal</i> data model.
 * <p>
 * This class provides the list of properties for the <i>Goal</i>
 * metaclass.
 */
@objid ("8f75cb63-c068-11e1-8c0a-002564c97630")
public class GoalPropertyModel extends AbstractAnalystElementPropertyModel<Goal> {
    /**
     * Create a new <i>Goal</i> data model from an <i>Goal</i>.
     * @param modelService
     * @param activationService
     */
    @objid ("8f75cb76-c068-11e1-8c0a-002564c97630")
    public GoalPropertyModel(Goal theEditedElement, IMModelServices modelService, IProjectService projectService, IActivationService activationService) {
        super(theEditedElement, modelService, projectService, activationService);
    }

    @objid ("1a21377f-9000-4f10-bf45-44f6cb41cffe")
    @Override
    protected List<? extends AnalystElement> getOwnedAnalystElements() {
        return this.theEditedElement.getSubGoal();
    }

    @objid ("0631909f-88cc-4236-b55a-acfe55293c46")
    @Override
    protected String getRichTextType() {
        return "goal";
    }

}
