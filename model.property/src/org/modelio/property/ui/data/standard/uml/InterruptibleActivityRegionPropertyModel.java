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
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityEdge;
import org.modelio.metamodel.uml.behavior.activityModel.InterruptibleActivityRegion;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.model.IModel;

/**
 * <i>InterruptibleActivityRegion</i> data model.
 * <p>
 * This class provides the list of properties for the <i>InterruptibleActivityRegion</i> metaclass.
 */
@objid ("8f373bd0-c068-11e1-8c0a-002564c97630")
public class InterruptibleActivityRegionPropertyModel extends AbstractPropertyModel<InterruptibleActivityRegion> {
    /**
     * Properties to display for <i>InterruptibleActivityRegion</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a765d888-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] {"InterruptibleActivityRegion", "Name", "InterruptingEdge"};

    @objid ("8f373bdb-c068-11e1-8c0a-002564c97630")
    private StringType labelStringType = null;

    @objid ("8f373bdc-c068-11e1-8c0a-002564c97630")
    private StringType stringType = null;

    @objid ("8f373bdd-c068-11e1-8c0a-002564c97630")
    private MultipleElementType activityEdgeType = null;

    /**
     * Create a new <i>InterruptibleActivityRegion</i> data model from an <i>InterruptibleActivityRegion</i>.
     */
    @objid ("8f373bde-c068-11e1-8c0a-002564c97630")
    public InterruptibleActivityRegionPropertyModel(InterruptibleActivityRegion theEditedElement, IModel model) {
        super(theEditedElement);
                    
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
        this.activityEdgeType = new MultipleElementType(true, theEditedElement, "InterruptingEdge", ActivityEdge.class, model);
    }

    /**
     * The number of columns that the properties table must display.
     */
    @objid ("8f373be4-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     */
    @objid ("8f373be9-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return InterruptibleActivityRegionPropertyModel.PROPERTIES.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     */
    @objid ("8f38c246-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return InterruptibleActivityRegionPropertyModel.PROPERTIES[row];
        case 1: // col 1 is the property value
            switch (row) {
                case 0: // Header
                    return "Value";
                case 1:
                    return this.theEditedElement.getName();
                case 2:
                    return this.theEditedElement.getInterruptingEdge();
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
    @objid ("8f38c24c-c068-11e1-8c0a-002564c97630")
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
                    return this.activityEdgeType;
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
    @objid ("8f38c252-c068-11e1-8c0a-002564c97630")
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
                    
                    for (ActivityEdge e : this.theEditedElement.getInterruptingEdge())
                        this.theEditedElement.getInterruptingEdge().remove(e);
                    
                    List<ActivityEdge> l  = (List<ActivityEdge>) value;
                    for (ActivityEdge e : l)
                        this.theEditedElement.getInterruptingEdge().add(e);
                    
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
