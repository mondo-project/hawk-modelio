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
                                    

package org.modelio.diagram.editor.commands;

import java.util.UUID;
import javax.inject.Inject;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.commands.AbstractParameterValueConverter;
import org.eclipse.core.commands.ParameterValueConversionException;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.diagrams.AbstractDiagram;

@objid ("a5ac9eb8-3896-11e2-95fe-001ec947c8cc")
public class IAbstractDiagramConverter extends AbstractParameterValueConverter {
    @objid ("a5b3c5c6-3896-11e2-95fe-001ec947c8cc")
    @Inject
     IMModelServices mModelServices;

    @objid ("a5baecd4-3896-11e2-95fe-001ec947c8cc")
    @Override
    public Object convertToObject(String parameterValue) throws ParameterValueConversionException {
        return this.mModelServices.findById(Metamodel.getMClass(AbstractDiagram.class), UUID.fromString(parameterValue));
    }

    @objid ("a5d061fe-3896-11e2-95fe-001ec947c8cc")
    @Override
    public String convertToString(Object parameterValue) throws ParameterValueConversionException {
        return ((AbstractDiagram)parameterValue).getUuid().toString();
    }

}
