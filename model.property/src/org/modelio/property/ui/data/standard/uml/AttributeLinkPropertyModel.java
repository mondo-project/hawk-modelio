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

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.emf.common.util.EList;
import org.modelio.core.ui.ktable.types.IPropertyType;
import org.modelio.core.ui.ktable.types.element.SingleElementType;
import org.modelio.core.ui.ktable.types.modelelement.ModelElementListType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.AttributeLink;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.Feature;
import org.modelio.metamodel.uml.statik.Generalization;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.model.IModel;
import org.modelio.vcore.session.impl.CoreSession;

/**
 * <i>AttributeLink</i> data model.
 * <p>
 * This class provides the list of properties for the <i>AttributeLink</i> metaclass.
 */
@objid ("8ed28689-c068-11e1-8c0a-002564c97630")
public class AttributeLinkPropertyModel extends AbstractPropertyModel<AttributeLink> {
    /**
     * Properties to display for <i>AttributeLink</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a67c91c8-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] {"AttributeLink", "Name", "Base", "Value"};

    @objid ("8ed28694-c068-11e1-8c0a-002564c97630")
    private StringType labelStringType = null;

    @objid ("8ed28695-c068-11e1-8c0a-002564c97630")
    private StringType stringType = null;

    @objid ("8ed28696-c068-11e1-8c0a-002564c97630")
    private SingleElementType defaultAttributeLinkBaseType = null;

    @objid ("15e01aa4-16da-11e2-aa0d-002564c97630")
    private IModel model;

    /**
     * Create a new <i>AttributeLink</i> data model from an <i>AttributeLink</i>.
     */
    @objid ("8ed28697-c068-11e1-8c0a-002564c97630")
    public AttributeLinkPropertyModel(AttributeLink theEditedElement, IModel model) {
        super(theEditedElement);
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
        this.defaultAttributeLinkBaseType = new SingleElementType(true, Attribute.class, CoreSession.getSession(this.theEditedElement));
        this.model = model;
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8ed2869d-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8ed286a2-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return AttributeLinkPropertyModel.PROPERTIES.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8ed40d05-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return AttributeLinkPropertyModel.PROPERTIES[row];
        case 1: // col 1 is the property value
            switch (row) {
            case 0: // Header
                return "Value";
            case 1:
                return this.theEditedElement.getName();
            case 2:
                return this.theEditedElement.getBase();
            case 3:
                return this.theEditedElement.getValue();
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
    @objid ("8ed40d0b-c068-11e1-8c0a-002564c97630")
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
                return getAttributeLinkBaseType();
            case 3:
                return this.stringType;
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
    @objid ("8ed40d11-c068-11e1-8c0a-002564c97630")
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
                this.theEditedElement.setBase((Attribute) value);
                break;
            case 3:
                this.theEditedElement.setValue((String) value);
                break;
            default:
                return;
            }
            break;
        default:
            return;
        }
    }

    @objid ("8ed40d17-c068-11e1-8c0a-002564c97630")
    private IPropertyType getAttributeLinkBaseType() {
        Instance parentInstance = this.theEditedElement.getAttributed();
        if (parentInstance == null) {
            return this.defaultAttributeLinkBaseType;
        }
        
        NameSpace parentBase = parentInstance.getBase();
        if (!(parentBase instanceof Classifier)) {
            return this.defaultAttributeLinkBaseType;
        }
        
        Classifier parentClassifierBase = (Classifier)parentBase;
        
        List<ModelElement> availableAttributes = getAvailableAttributes(parentClassifierBase);
        
        ModelElementListType type = new ModelElementListType(true, Attribute.class, availableAttributes, CoreSession.getSession(this.theEditedElement));
        return type;
    }

    @objid ("8ed40d1b-c068-11e1-8c0a-002564c97630")
    private List<ModelElement> getAvailableAttributes(Classifier classifier) {
        List<ModelElement> attributesList = new ArrayList<>(); 
        
        if (classifier != null) {
            EList<Attribute> classifierOperations = classifier.getOwnedAttribute();
            
            for (Feature feature : classifierOperations) {
                attributesList.add(feature);
            }
            
            EList<Generalization> generalizations = classifier.getParent();
            
            for (Generalization generalization : generalizations) {
                NameSpace parentNameSpace = generalization.getSuperType();
                if (parentNameSpace instanceof Classifier) {
                    attributesList.addAll(getAvailableAttributes((Classifier)parentNameSpace));
                }
            }
        }
        return attributesList;
    }

}
