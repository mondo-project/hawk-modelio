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

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.app.core.activation.IActivationService;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.core.ui.ktable.types.IPropertyType;
import org.modelio.core.ui.ktable.types.modelelement.ModelElementListType;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.analyst.AnalystElement;
import org.modelio.metamodel.analyst.BusinessRuleContainer;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyTableDefinition;
import org.modelio.vcore.session.api.model.IModel;
import org.modelio.vcore.session.impl.CoreSession;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.meta.SmClass;

/**
 * <i>BusinessRuleContainer</i> data model.
 * <p>
 * This class provides the list of properties for the
 * <i>BusinessRuleContainer</i> metaclass.
 * <p>
 */
@objid ("2120f96a-90c0-4ac4-840f-a94b64bcdf08")
public class BusinessRuleContainerPropertyModel extends AbstractAnalystContainerPropertyModel<BusinessRuleContainer> {
    /**
     * Create a new <i>BusinessRuleContainer</i> data model from an <i>analyst
     * container</i>.
     * @param modelService
     * @param model
     * @param activationService
     * @param theEditedElement the analyst container
     */
    @objid ("d2f3d0a3-7b32-4708-a86f-49dc4364e668")
    public BusinessRuleContainerPropertyModel(BusinessRuleContainer theEditedElement, IMModelServices modelService, IModel model, IProjectService projectService, IActivationService activationService) {
        super(theEditedElement, modelService, projectService, activationService, model);
    }

    @objid ("b4215141-9eac-4cf6-9535-d40ac1322873")
    @Override
    protected IPropertyType getAvailableSets() {
        List<ModelElement> availableSets = new ArrayList<>();
        
        String stereotypeFilter = "business_rule_propertyset";
        for (MObject elt : this.model.findByClass(SmClass.getClass(PropertyTableDefinition.class), IModel.ISVALID)) {
            PropertyTableDefinition propertySet = (PropertyTableDefinition) elt;
        
            // Keep only property sets without stereotypes, or enforcing the
            if (propertySet.getExtension().isEmpty() || propertySet.isStereotyped("ModelerModule", stereotypeFilter)) {
                availableSets.add(propertySet);
            }
        }
        
        ModelElementListType type = new ModelElementListType(false, PropertyTableDefinition.class, availableSets, CoreSession.getSession(this.theEditedElement));
        return type;
    }

    @objid ("2b54511f-fe2e-4e58-b57f-230e7ce0cf5c")
    @Override
    protected List<? extends AnalystElement> getOwnedAnalystElements() {
        return this.theEditedElement.getOwnedRule();
    }

}
