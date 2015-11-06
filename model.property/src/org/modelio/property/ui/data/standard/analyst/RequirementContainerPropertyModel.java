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
import org.modelio.metamodel.analyst.RequirementContainer;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyTableDefinition;
import org.modelio.vcore.session.api.model.IModel;
import org.modelio.vcore.session.impl.CoreSession;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.meta.SmClass;

/**
 * <i>RequirementContainer</i> data model.
 * <p>
 * This class provides the list of properties for the
 * <i>RequirementContainer</i> metaclass.
 * <p>
 */
@objid ("c024b51b-2aa2-4cbc-8fae-15be730c7909")
public class RequirementContainerPropertyModel extends AbstractAnalystContainerPropertyModel<RequirementContainer> {
    /**
     * Create a new <i>RequirementContainer</i> data model from an <i>analyst
     * container</i>.
     * @param modelService
     * @param model
     * @param activationService
     * @param theEditedElement the analyst container
     */
    @objid ("5ef90dcb-1373-467c-b6c0-9597691d4ea3")
    public RequirementContainerPropertyModel(RequirementContainer theEditedElement, IMModelServices modelService, IModel model, IProjectService projectService, IActivationService activationService) {
        super(theEditedElement, modelService, projectService, activationService, model);
    }

    @objid ("81a9e858-0ff5-4bfc-8d10-828d3cdccd42")
    @Override
    protected IPropertyType getAvailableSets() {
        List<ModelElement> availableSets = new ArrayList<>();
        
        String stereotypeFilter = "requirement_propertyset";
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

    @objid ("fdc12280-4b4a-4a6a-85a9-3402190a53b7")
    @Override
    protected List<? extends AnalystElement> getOwnedAnalystElements() {
        return this.theEditedElement.getOwnedRequirement();
    }

}
