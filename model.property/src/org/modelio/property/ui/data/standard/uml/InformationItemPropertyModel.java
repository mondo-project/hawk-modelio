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

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.core.ui.ktable.types.IPropertyType;
import org.modelio.core.ui.ktable.types.bool.BooleanType;
import org.modelio.core.ui.ktable.types.element.MultipleElementType;
import org.modelio.core.ui.ktable.types.enumeration.EnumType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.uml.informationFlow.InformationItem;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.VisibilityMode;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.model.IModel;

/**
 * <i>InformationItem</i> data model.
 * <p>
 * This class provides the list of properties for the <i>InformationItem</i> metaclass.
 */
@objid ("8f24ec67-c068-11e1-8c0a-002564c97630")
public class InformationItemPropertyModel extends AbstractPropertyModel<InformationItem> {
    /**
     * Properties to display for <i>InformationItem</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a74223ee-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] {"InformationItem", "Name", "Visibility", "Represented", "IsAbstract", "IsLeaf", "IsRoot"};

    @objid ("8f2672cc-c068-11e1-8c0a-002564c97630")
    private StringType labelStringType = null;

    @objid ("8f2672cd-c068-11e1-8c0a-002564c97630")
    private StringType stringType = null;

    @objid ("8f2672ce-c068-11e1-8c0a-002564c97630")
    private BooleanType booleanType = null;

    @objid ("8f2672cf-c068-11e1-8c0a-002564c97630")
    private EnumType visibilityType = null;

    @objid ("8f2672d0-c068-11e1-8c0a-002564c97630")
    private MultipleElementType representedClassifiers = null;

    /**
     * Create a new <i>InformationItem</i> data model from an <i>InformationItem</i>.
     */
    @objid ("8f2672d1-c068-11e1-8c0a-002564c97630")
    public InformationItemPropertyModel(InformationItem theEditedElement, IModel model) {
        super(theEditedElement);
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
        this.booleanType = new BooleanType();
        this.visibilityType = new EnumType(VisibilityMode.class);
        this.representedClassifiers = new MultipleElementType(true, theEditedElement, "Represented", Classifier.class, model);
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8f2672d7-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8f2672dc-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return InformationItemPropertyModel.PROPERTIES.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8f2672e1-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return InformationItemPropertyModel.PROPERTIES[row];
        case 1: // col 1 is the property value
            switch (row) {
                case 0: // Header
                    return "Value";
                case 1:
                    return this.theEditedElement.getName();
                case 2:
                    return this.theEditedElement.getVisibility();
                case 3:
                    return this.theEditedElement.getRepresented();
                case 4:
                    return this.theEditedElement.isIsAbstract()?Boolean.TRUE:Boolean.FALSE;
                case 5:
                    // The logic here has been inverted to allow a positive logic: The displayed field is now can be inherited.
                    return (!this.theEditedElement.isIsLeaf())?Boolean.TRUE:Boolean.FALSE;
                case 6:
                    return this.theEditedElement.isIsRoot()?Boolean.TRUE:Boolean.FALSE;
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
    @objid ("8f2672e7-c068-11e1-8c0a-002564c97630")
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
                    return this.visibilityType;
                case 3:
                    return this.representedClassifiers;
                case 4:
                    return this.booleanType;
                case 5:
                    return this.booleanType;
                case 6:
                    return this.booleanType;
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
    @objid ("8f2672ed-c068-11e1-8c0a-002564c97630")
    @Override
    @SuppressWarnings("unchecked")
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
                    this.theEditedElement.setVisibility((VisibilityMode) value);
                    break;
                case 3:
                    for (Classifier s : this.theEditedElement.getRepresented())
                        this.theEditedElement.getRepresented().remove(s);
                    
                    List<Classifier> newcontent = (List<Classifier>)value;
                    for (Classifier s : newcontent)
                        this.theEditedElement.getRepresented().add(s);
                    break;
                case 4:
                    this.theEditedElement.setIsAbstract(((Boolean) value).booleanValue());
                    break;
                case 5:
                    // The logic here has been inverted to allow a positive logic: The displayed field is now can be inherited.
                    this.theEditedElement.setIsLeaf(!((Boolean) value).booleanValue());
                    break;
                case 6:
                    this.theEditedElement.setIsRoot(((Boolean) value).booleanValue());
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
