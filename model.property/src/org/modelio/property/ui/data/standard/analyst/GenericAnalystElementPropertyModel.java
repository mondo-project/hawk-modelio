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
import org.modelio.metamodel.analyst.GenericAnalystElement;

/**
 * {@link GenericAnalystElement} data model.
 * <p>
 * This class provides the list of properties for the <i>GenericAnalystElement</i>
 * metaclass.
 */
@objid ("49f8ad73-f402-4efc-93de-2eb7bd6dee9a")
public class GenericAnalystElementPropertyModel extends AbstractAnalystElementPropertyModel<GenericAnalystElement> {
    /**
     * Create a new <i>GenericAnalystElement</i> data model from an <i>GenericAnalystElement</i>.
     * @param theEditedElement the edited GenericAnalystElement
     * @param modelService the model service
     * @param projectService the project service
     * @param activationService the activation service
     */
    @objid ("acaaadae-f4b2-4be2-a172-989cdf632867")
    public GenericAnalystElementPropertyModel(GenericAnalystElement theEditedElement, IMModelServices modelService, IProjectService projectService, IActivationService activationService) {
        super(theEditedElement, modelService, projectService, activationService);
    }

    @objid ("24a8c6f9-ac5c-449c-8b41-33ce60bf9f68")
    @Override
    protected String getRichTextType() {
        return "generic_analyst";
    }

    @objid ("d8a71ea5-5b6b-4c93-81d5-6f1a230fee97")
    @Override
    protected List<? extends AnalystElement> getOwnedAnalystElements() {
        return this.theEditedElement.getSubElement();
    }

}
