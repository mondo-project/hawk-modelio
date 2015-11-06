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
                                    

package org.modelio.core.ui.ktable.types.text;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.renderers.DefaultCellRenderer;
import org.modelio.app.core.picking.IModelioPickingService;
import org.modelio.metamodel.uml.infrastructure.ModelElement;

@objid ("8def2b64-c068-11e1-8c0a-002564c97630")
public class MultilineStringType extends StringType {
    @objid ("8def2b65-c068-11e1-8c0a-002564c97630")
    private ModelElement editedElement = null;

    @objid ("a57b7d4e-c068-11e1-8c0a-002564c97630")
    private String fieldName = null;

    @objid ("8def2b69-c068-11e1-8c0a-002564c97630")
    public MultilineStringType(final ModelElement editedElement, final String fieldName, final boolean acceptNullValue) {
        super(acceptNullValue);
        
        this.editedElement = editedElement;
        this.fieldName = fieldName;
    }

    @objid ("8def2b78-c068-11e1-8c0a-002564c97630")
    public String getFieldName() {
        return this.fieldName;
    }

    @objid ("8def2b7c-c068-11e1-8c0a-002564c97630")
    public ModelElement getEditedElement() {
        return this.editedElement;
    }

    @objid ("0ab88f3d-cb5b-11e1-9165-002564c97630")
    @Override
    public KTableCellEditor getEditor(IModelioPickingService pickingService) {
        return new MultilineTextCellEditor(getEditedElement(), getFieldName());
    }

    @objid ("0ab8b64f-cb5b-11e1-9165-002564c97630")
    @Override
    public DefaultCellRenderer getRenderer() {
        // TODO Auto-generated method stub
        return super.getRenderer();
    }

}
