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
import org.modelio.metamodel.analyst.BusinessRule;

/**
 * <i>BusinessRule</i> data model.
 * <p>
 * This class provides the list of properties for the <i>BusinessRule</i>
 * metaclass.
 */
@objid ("5f534ad9-2e1c-4953-8d01-922cf1b3de67")
public class BusinessRulePropertyModel extends AbstractAnalystElementPropertyModel<BusinessRule> {
    /**
     * Create a new <i>BusinessRule</i> data model from an <i>BusinessRule</i>.
     * @param modelService
     * @param activationService
     */
    @objid ("f2a1aa88-de4c-4727-99db-40e7cd3f8534")
    public BusinessRulePropertyModel(BusinessRule theEditedElement, IMModelServices modelService, IProjectService projectService, IActivationService activationService) {
        super(theEditedElement, modelService, projectService, activationService);
    }

    @objid ("291dc1fb-960f-4a25-a56f-7cc96b015202")
    @Override
    protected List<? extends AnalystElement> getOwnedAnalystElements() {
        return this.theEditedElement.getSubRule();
    }

    @objid ("29d10b81-7543-48d6-8010-433b6ccaf7ba")
    @Override
    protected String getRichTextType() {
        return "business_rule";
    }

}
