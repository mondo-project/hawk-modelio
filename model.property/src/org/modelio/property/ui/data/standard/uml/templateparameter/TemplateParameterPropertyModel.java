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
                                    

package org.modelio.property.ui.data.standard.uml.templateparameter;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.core.ui.ktable.IPropertyModel;
import org.modelio.core.ui.ktable.types.IPropertyType;
import org.modelio.metamodel.uml.statik.TemplateParameter;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.model.IModel;

/**
 * <i>TemplateParameter</i> data model.
 * <p>
 * This class provides the list of properties for the <i>TemplateParameter</i> metaclass.
 * <p>
 * This data model has been manually moved and updated.
 * <p>
 * It delegates each call to one of the otheEditedElement property model class dependeing on the template paraemter kind.
 */
@objid ("8f8ffa9a-c068-11e1-8c0a-002564c97630")
public class TemplateParameterPropertyModel extends AbstractPropertyModel<TemplateParameter> {
    /**
     * Specialized property model to which each call is delegated.
     */
    @objid ("8f8ffaa0-c068-11e1-8c0a-002564c97630")
    private IPropertyModel delegate;

    @objid ("16f24df4-16da-11e2-aa0d-002564c97630")
    private IModel model;

    /**
     * Create a new <i>TemplateParameter</i> data model from an <i>TemplateParameter</i>.
     * @param theEditedElement
     */
    @objid ("8f8ffaa2-c068-11e1-8c0a-002564c97630")
    public TemplateParameterPropertyModel(TemplateParameter theEditedElement, IModel model) {
        super(theEditedElement);
        this.model = model;
        this.initDelegate();
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8f8ffaa8-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return this.delegate.getColumnNumber();
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8f8ffaad-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return this.delegate.getRowsNumber();
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8f8ffab2-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        return this.delegate.getValueAt(row, col);
    }

    /**
     * Return the type of the element displayed at the specified row and column.
     * <p>
     * This type will be used to choose an editor and a renderer for each cell of the properties table.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the type of the element corresponding to the row and column
     */
    @objid ("8f8ffab9-c068-11e1-8c0a-002564c97630")
    @Override
    public IPropertyType getTypeAt(int row, int col) {
        return this.delegate.getTypeAt(row, col);
    }

    /**
     * Set value in the model for the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number.
     * @param col the column number.
     * @param value the value specified by the user.
     */
    @objid ("8f8ffac0-c068-11e1-8c0a-002564c97630")
    @Override
    public void setValueAt(int row, int col, Object value) {
        this.delegate.setValueAt(row, col, value);
        initDelegate();
    }

    @objid ("8f8ffac6-c068-11e1-8c0a-002564c97630")
    @Override
    public boolean isEditable(int row, int col) {
        return this.delegate.isEditable(row, col);
    }

    /**
     * Initialize the delegate PropertyModel.
     * @param theEditedElement
     */
    @objid ("8f8ffacd-c068-11e1-8c0a-002564c97630")
    private void initDelegate() {
        if (this.theEditedElement.getOwnedParameterElement() != null) {
            this.delegate = new ElementTemplateParameterPropertyModel(this.theEditedElement);
        } else if (this.theEditedElement.isIsValueParameter()) {
            this.delegate = new ValueTemplateParameterPropertyModel(this.theEditedElement, this.model);
        } else {
            this.delegate = new TypeTemplateParameterPropertyModel(this.theEditedElement);
        }
    }

}
