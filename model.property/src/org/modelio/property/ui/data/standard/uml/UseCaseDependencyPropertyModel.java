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
import org.modelio.core.ui.ktable.types.element.MultipleElementType;
import org.modelio.core.ui.ktable.types.element.SingleElementType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.uml.behavior.usecaseModel.ExtensionPoint;
import org.modelio.metamodel.uml.behavior.usecaseModel.UseCase;
import org.modelio.metamodel.uml.behavior.usecaseModel.UseCaseDependency;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.model.IModel;
import org.modelio.vcore.session.impl.CoreSession;

@objid ("8f9e42fa-c068-11e1-8c0a-002564c97630")
public class UseCaseDependencyPropertyModel extends AbstractPropertyModel<UseCaseDependency> {
    /**
     * Properties to display for <i>UseCaseDependency</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a88496c8-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] {"UseCaseDependency", "Target", "ExtensionLocation"};

    @objid ("8f9e4304-c068-11e1-8c0a-002564c97630")
    private StringType labelStringType = null;

    @objid ("8f9e4305-c068-11e1-8c0a-002564c97630")
    private SingleElementType targetType = null;

    @objid ("8f9e4306-c068-11e1-8c0a-002564c97630")
    private MultipleElementType extensionLocationType = null;

    /**
     * Create a new <i>UseCaseDependency</i> data model from an <i>UseCaseDependency</i>.
     */
    @objid ("8fa0a405-c068-11e1-8c0a-002564c97630")
    public UseCaseDependencyPropertyModel(UseCaseDependency theEditedElement, IModel model) {
        super(theEditedElement);
        
        this.labelStringType = new StringType(false);
        this.targetType = new SingleElementType(false, UseCase.class, CoreSession.getSession(this.theEditedElement));
        this.extensionLocationType = new MultipleElementType(true, theEditedElement, "ExtensionLocation", ExtensionPoint.class, model);
    }

    /**
     * The number of columns that the properties table must display.
     */
    @objid ("8fa0a40b-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     */
    @objid ("8fa0a410-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        for (Stereotype stereo : this.theEditedElement.getExtension()) {
            if (stereo.getName().equals("extend")) {
                return 3;
            }
        }
        return 2;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     */
    @objid ("8fa0a415-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return UseCaseDependencyPropertyModel.PROPERTIES[row];
        case 1: // col 1 is the property value
            switch (row) {
            case 0: // Header
                return "Value";
            case 1:
                return this.theEditedElement.getTarget();
            case 2:
                return this.theEditedElement.getExtensionLocation();
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
     */
    @objid ("8fa0a41b-c068-11e1-8c0a-002564c97630")
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
                return this.targetType;
            case 2:
                return this.extensionLocationType;
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
     */
    @objid ("8fa0a421-c068-11e1-8c0a-002564c97630")
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
                this.theEditedElement.setTarget((UseCase) value);
                break;
            case 2:
                for (ExtensionPoint e : this.theEditedElement.getExtensionLocation())
                    this.theEditedElement.getExtensionLocation().remove(e);
        
                List<ExtensionPoint> l  = (List<ExtensionPoint>) value;
                for (ExtensionPoint e : l)
                    this.theEditedElement.getExtensionLocation().add(e);
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
