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
import org.modelio.metamodel.analyst.Requirement;

/**
 * <i>Requirement</i> data model.
 * <p>
 * This class provides the list of properties for the <i>Requirement</i>
 * metaclass.
 */
@objid ("89024a5e-81b4-4653-839f-f6fc1c9876da")
public class RequirementPropertyModel extends AbstractAnalystElementPropertyModel<Requirement> {
    /**
     * Create a new <i>Requirement</i> data model from an <i>Requirement</i>.
     * @param modelService
     * @param activationService
     */
    @objid ("e33482fc-ff17-4cc9-9824-ff4fba6f51cd")
    public RequirementPropertyModel(Requirement theEditedElement, IMModelServices modelService, IProjectService projectService, IActivationService activationService) {
        super(theEditedElement, modelService, projectService, activationService);
    }

    @objid ("30461972-59ce-46e3-ab8b-faaef0b13d18")
    @Override
    protected String getRichTextType() {
        return "requirement";
    }

    @objid ("1dc1efa4-1fff-43c1-a602-0021bf9ed516")
    @Override
    protected List<? extends AnalystElement> getOwnedAnalystElements() {
        return this.theEditedElement.getSubRequirement();
    }

}
