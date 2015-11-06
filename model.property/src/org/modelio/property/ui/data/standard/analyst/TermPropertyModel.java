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

import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.app.core.activation.IActivationService;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.analyst.AnalystElement;
import org.modelio.metamodel.analyst.Term;

/**
 * <i>Term</i> data model.
 * <p>
 * This class provides the list of properties for the <i>Term</i> metaclass.
 */
@objid ("8f971ecc-c068-11e1-8c0a-002564c97630")
public class TermPropertyModel extends AbstractAnalystElementPropertyModel<Term> {
    /**
     * Constructor
     * @param modelService
     * @param activationService
     * @param theEditedElement the model to edit.
     */
    @objid ("8f997fe5-c068-11e1-8c0a-002564c97630")
    public TermPropertyModel(Term theEditedElement, IMModelServices modelService, IProjectService projectService, IActivationService activationService) {
        super(theEditedElement, modelService, projectService, activationService);
    }

    @objid ("a6a13380-e670-4e1a-9b6c-9889c44754f2")
    @Override
    protected List<? extends AnalystElement> getOwnedAnalystElements() {
        return Collections.emptyList();
    }

    @objid ("810eae7b-635b-4315-aa19-4cd2e0292de0")
    @Override
    protected String getRichTextType() {
        return "term";
    }

}
