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
import org.modelio.metamodel.analyst.GenericAnalystContainer;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyTableDefinition;
import org.modelio.vcore.session.api.model.IModel;
import org.modelio.vcore.session.impl.CoreSession;

/**
 * {@link GenericAnalystContainer} data model.
 * <p>
 * This class provides the list of properties for the
 * <i>GenericAnalystContainer</i> metaclass.
 * <p>
 */
@objid ("1ec2214f-3709-4faf-ac4f-c237dde0e3be")
public class GenericAnalystContainerPropertyModel extends AbstractAnalystContainerPropertyModel<GenericAnalystContainer> {
    /**
     * Create a new <i>GenericAnalystContainer</i> data model from an <i>analyst
     * container</i>.
     * @param theEditedElement the analyst container
     * @param modelService model service
     * @param model core model
     * @param projectService the project service
     * @param activationService activation service
     */
    @objid ("983d488a-8db9-44db-8268-b30205b9727c")
    public GenericAnalystContainerPropertyModel(GenericAnalystContainer theEditedElement, IMModelServices modelService, IModel model, IProjectService projectService, IActivationService activationService) {
        super(theEditedElement, modelService, projectService, activationService, model);
    }

    @objid ("3a07dceb-0ed9-4b8b-8190-6435965950aa")
    @Override
    protected IPropertyType getAvailableSets() {
        List<ModelElement> availableSets = new ArrayList<ModelElement>(this.model.findByClass(PropertyTableDefinition.class, IModel.ISVALID));
        ModelElementListType type = new ModelElementListType(false, PropertyTableDefinition.class, availableSets, CoreSession.getSession(this.theEditedElement));
        return type;
    }

    @objid ("9c27648e-0be5-4f8b-841b-997274e55b5f")
    @Override
    protected List<? extends AnalystElement> getOwnedAnalystElements() {
        return this.theEditedElement.getOwnedElement();
    }

}
