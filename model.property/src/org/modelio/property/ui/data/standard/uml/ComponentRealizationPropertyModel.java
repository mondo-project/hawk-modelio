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
                                    

package org.modelio.property.ui.data.standard.uml;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.core.ui.ktable.types.IPropertyType;
import org.modelio.core.ui.ktable.types.element.SingleElementType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.Component;
import org.modelio.metamodel.uml.statik.ComponentRealization;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.impl.CoreSession;

/**
 * <i>ComponentRealization</i> data model.
 * <p>
 * This class provides the list of properties for the {@link ComponentRealization} metaclass.
 */
@objid ("dbd1ce5a-af36-4d65-8ba9-eac034a26141")
public class ComponentRealizationPropertyModel extends AbstractPropertyModel<ComponentRealization> {
    /**
     * Properties to display for <i>ComponentRealization</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("bbc2e3fc-613e-4650-978a-8714f32299ce")
    private static final String[] PROPERTIES = new String[] {"ComponentRealization", "Name", "Abstraction", "RealizingClassifier"};

    @objid ("1bf19b67-b851-4f17-a898-650bed39cc03")
    private StringType labelStringType;

    @objid ("b358202f-e34f-4946-80ae-8134983ae3e8")
    private StringType stringType;

    @objid ("7cea59dd-ac8b-4d87-90e2-952e2d12e006")
    private SingleElementType abstractionType;

    @objid ("79c0f41d-d4f0-4003-88f9-e4ddc8f27126")
    private SingleElementType realizerType;

    /**
     * Create a new <i>ComponentRealization</i> data model from an <i>ComponentRealization</i>.
     * @param theEditedElement edited element
     */
    @objid ("cc8f3aab-076e-43d5-b92a-828d9fd6ba44")
    public ComponentRealizationPropertyModel(ComponentRealization theEditedElement) {
        super(theEditedElement);
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
        this.realizerType = new SingleElementType(false, Classifier.class, CoreSession.getSession(this.theEditedElement));
        this.abstractionType = new SingleElementType(false, Component.class, CoreSession.getSession(this.theEditedElement));
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("6d38a34c-cd92-4416-896e-65396c3a9a3e")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("fe5aed5b-02d1-44d2-a47a-7d490f179c16")
    @Override
    public int getRowsNumber() {
        return ComponentRealizationPropertyModel.PROPERTIES.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("f44ecaf4-a5bc-4239-9657-68821a28052d")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return ComponentRealizationPropertyModel.PROPERTIES[row];
        case 1: // col 1 is the property value
            switch (row) {
                case 0: // Header
                    return "Value";
                case 1:
                    return this.theEditedElement.getName();
                case 2:
                    return this.theEditedElement.getAbstraction();
                case 3 :
                    return this.theEditedElement.getRealizingClassifier();
                default:
                    return null;
            }
        default:
            return null;
        }
    }

    /**
     * Return the type of the element displayed at the specified row and column.
     * <p>
     * This type will be used to choose an editor and a renderer for each cell
     * of the properties table.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the type of the element corresponding to the row and column
     */
    @objid ("37655ce8-36f8-4efe-bce3-3317f5a9a030")
    @Override
    public IPropertyType getTypeAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key type
            return this.labelStringType;
        case 1: // col 1 is the property value type
            switch (row) {
                case 0: // Header
                    return this.labelStringType;
                case 1:
                    return this.stringType;
                case 2:
                    return this.abstractionType;
                case 3 :
                    return this.realizerType;
                default:
                    return null;
            }
        default:
            return null;
        }
    }

    /**
     * Set value in the model for the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number.
     * @param col the column number.
     * @param value the value specified by the user.
     */
    @objid ("49241a78-30b1-4a45-916f-c006da33a98e")
    @Override
    public void setValueAt(int row, int col, Object value) {
        switch (col) {
        case 0: // Keys cannot be modified
            return;
        case 1: // col 1 is the property value
            switch (row) {
                case 0:
                    return; // Header cannot be modified
                case 1:
                    this.theEditedElement.setName((String) value);
                    break;
                case 2:
                    this.theEditedElement.setAbstraction((Component) value);
                    break;
                case 3 :
                    this.theEditedElement.setRealizingClassifier((Classifier) value);
                    break;
                default:
                    return;
            }
              break;
        default:
            return;
        }
    }

}
