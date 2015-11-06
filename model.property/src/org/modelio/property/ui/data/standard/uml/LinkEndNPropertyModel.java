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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.emf.common.util.EList;
import org.modelio.core.ui.MetamodelLabels;
import org.modelio.core.ui.ktable.types.IPropertyType;
import org.modelio.core.ui.ktable.types.bool.BooleanType;
import org.modelio.core.ui.ktable.types.element.SingleElementType;
import org.modelio.core.ui.ktable.types.list.EditableListType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.metamodel.uml.statik.NaryAssociation;
import org.modelio.metamodel.uml.statik.NaryLink;
import org.modelio.metamodel.uml.statik.NaryLinkEnd;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.impl.CoreSession;

/**
 * <i>NaryLinkEnd</i> data model.
 * <p>
 * This class provides the list of properties for the <i>NaryLinkEnd</i> metaclass.
 */
@objid ("8f416d56-c068-11e1-8c0a-002564c97630")
public class LinkEndNPropertyModel extends AbstractPropertyModel<NaryLinkEnd> {
    /**
     * Properties to display for <i>NaryLinkEnd</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li>for the first row the value is the table header label (usually the metaclass name)
     * <li>for other rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a7768228-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] { "NaryLinkEnd", "LinkName", "Link.Base", "Linked", "Name",
            "MultiplicityMin", "MultiplicityMax", "IsOrdered", "IsUnique" };

    @objid ("8f416d61-c068-11e1-8c0a-002564c97630")
    private final NaryLink theLink;

    @objid ("8f416d64-c068-11e1-8c0a-002564c97630")
    private List<NaryLinkEnd> displayedRoles;

    @objid ("8f416d69-c068-11e1-8c0a-002564c97630")
    private StringType labelStringType = null;

    @objid ("8f416d6a-c068-11e1-8c0a-002564c97630")
    private StringType stringType = null;

    @objid ("8f416d6b-c068-11e1-8c0a-002564c97630")
    private BooleanType booleanType = null;

    @objid ("8f416d6c-c068-11e1-8c0a-002564c97630")
    private EditableListType cardinalityMinType = null;

    @objid ("8f416d6d-c068-11e1-8c0a-002564c97630")
    private EditableListType cardinalityMaxType = null;

    @objid ("8f416d6e-c068-11e1-8c0a-002564c97630")
    private SingleElementType linkedType = null;

    @objid ("8f416d6f-c068-11e1-8c0a-002564c97630")
    private SingleElementType assocType = null;

    /**
     * Create a new <i>NaryLinkEnd</i> data model from an <i>NaryLinkEnd</i>.
     * @param model
     */
    @objid ("8f43ce65-c068-11e1-8c0a-002564c97630")
    public LinkEndNPropertyModel(NaryLinkEnd theLinkEnd) {
        super(theLinkEnd);
        this.theLink = theLinkEnd.getNaryLink();
        
        // Order the displayed roles as following:
        // - this role first for n-ary associations
        // - other roles next
        if (this.theLink != null) {
            final EList<NaryLinkEnd> roles = this.theLink.getNaryLinkEnd();
            this.displayedRoles = new ArrayList<>(roles.size());
            this.displayedRoles.add(this.theEditedElement);
            for (NaryLinkEnd r : roles) {
                if (!r.equals(this.theEditedElement)) {
                    this.displayedRoles.add(r);
                }
            }
        }
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
        this.booleanType = new BooleanType();
        ICoreSession session = CoreSession.getSession(this.theEditedElement);
        this.assocType = new SingleElementType(true, NaryAssociation.class, session);
        this.linkedType = new SingleElementType(false, Instance.class, session);
        
        List<String> cardinalityMinValues = new ArrayList<>();
        cardinalityMinValues.add("0");
        cardinalityMinValues.add("1");
        this.cardinalityMinType = new EditableListType(true, cardinalityMinValues);
        
        List<String> cardinalityMaxValues = new ArrayList<>();
        cardinalityMaxValues.add("1");
        cardinalityMaxValues.add("*");
        this.cardinalityMaxType = new EditableListType(true, cardinalityMaxValues);
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8f43ce6b-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return this.displayedRoles.size() + 1;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8f43ce70-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return LinkEndNPropertyModel.PROPERTIES.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8f43ce75-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        if (row == 1 || row == 2) {
            return getLinkPropertyValue(row, col);
        }
        
        // NaryLinkEnd rows
        switch (col) {
        
        case 0: // col 0 is the property name
            if (row == 0) {
                return MessageFormat.format(MetamodelLabels.getString("Title.NaryLinkEnd"), this.theEditedElement.getName());
            }
            // else
            return LinkEndNPropertyModel.PROPERTIES[row];
        
        default:
            return getPropertyValue(row, this.displayedRoles.get(col - 1));
        }
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
    @objid ("8f43ce7b-c068-11e1-8c0a-002564c97630")
    @Override
    public IPropertyType getTypeAt(int row, int col) {
        switch (col) {
        
        case 0: // col 0 is the property name
            return this.labelStringType;
        
        default:
            switch (row) {
            case 0: // Title
                return this.labelStringType;
        
            case 1: // Link name
                return this.stringType;
        
            case 2: // Link base Association
                if (col == 1) {
                    return this.assocType;
                }
                // else
                return this.stringType;
        
            case 3: // NaryLinkEnd Type
                return this.linkedType;
        
            case 4: // NaryLinkEnd Name
                return this.stringType;
            case 5:
                return this.cardinalityMinType;
            case 6:
                return this.cardinalityMaxType;
            case 7:
                return this.booleanType;
            case 8:
                return this.booleanType;
            default:
                return null;
            }
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
    @objid ("8f43ce81-c068-11e1-8c0a-002564c97630")
    @Override
    public void setValueAt(int row, int col, Object value) {
        switch (col) {
        
        case 0:
            return;
        
        default:
            setPropertyValue(row, this.displayedRoles.get(col - 1), value);
        
            return;
        }
    }

    @objid ("8f43ce87-c068-11e1-8c0a-002564c97630")
    @Override
    public boolean isEditable(int row, int col) {
        if (col == 0) {
            // Labels are not editable
            return false;
        } else if (row == 1 || row == 2) {
            // Link lines: only the first cell is editable
            if (col == 1) {
                if (this.theLink == null) {
                    return false;
                }
                if (!this.theLink.isModifiable()) {
                    return false;
                }
            } else {
                return false;
            }
        } else if (col == 1) {
            // Other cells
            NaryLinkEnd relatedEnd = this.displayedRoles.get(col - 1);
            if (!relatedEnd.isModifiable()) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return
     * @return
     */
    @objid ("8f43ce8e-c068-11e1-8c0a-002564c97630")
    private Object getLinkPropertyValue(int row, int col) {
        // Link rows
        if (col == 0) {
            return LinkEndNPropertyModel.PROPERTIES[row];
        } else if (col == 1) {
            return getPropertyValue(row, this.theEditedElement);
        } else {
            if (row == 1) {
                return ""; // Link name
            }
            // else
            return ""; // Link base Association
        }
    }

    @objid ("8f43ce94-c068-11e1-8c0a-002564c97630")
    private Object getPropertyValue(int row, NaryLinkEnd aLinkEnd) {
        switch (row) {
        case 0: // Title
        
            Instance type = aLinkEnd.getSource();
        
            if (type == null) {
                return "";
            }
        
            return MessageFormat.format(MetamodelLabels.getString("Title.from"), type.getName());
        
        case 1:
            // Link name
            if (this.theLink == null) {
                return MetamodelLabels.getString("NaryLinkEnd.NoLink");
            }
            // else
            return this.theLink.getName();
        
        case 2:
            // Link base association
            if (this.theLink == null) {
                return null;
            }
            // else
            return this.theLink.getModel();
        
        case 3:
            Instance relatedInstance = aLinkEnd.getSource();
            return relatedInstance;
        
        case 4:
            return aLinkEnd.getName();
        case 5:
            return aLinkEnd.getMultiplicityMin();
        case 6:
            return aLinkEnd.getMultiplicityMax();
        case 7:
            return aLinkEnd.isIsOrdered() ? Boolean.TRUE : Boolean.FALSE;
        case 8:
            return aLinkEnd.isIsUnique() ? Boolean.TRUE : Boolean.FALSE;
        default:
            return null;
        }
    }

    @objid ("8f43ce9b-c068-11e1-8c0a-002564c97630")
    private void setPropertyValue(int row, NaryLinkEnd linkEnd, Object value) {
        switch (row) {
        case 0:
            return;
        case 1:
            if (this.theLink != null) {
                this.theLink.setName((String) value);
            }
            break;
        
        case 2:
            if (this.theLink != null) {
                this.theLink.setModel((NaryAssociation) value);
            }
            break;
        case 3:
            linkEnd.setSource((Instance) value);
            break;
        case 4:
            linkEnd.setName(String.valueOf(value));
            break;
        case 5:
            linkEnd.setMultiplicityMin(String.valueOf(value));
            break;
        case 6:
            linkEnd.setMultiplicityMax(String.valueOf(value));
            break;
        case 8:
            linkEnd.setIsOrdered(((Boolean) value).booleanValue());
            break;
        case 9:
            linkEnd.setIsUnique(((Boolean) value).booleanValue());
            break;
        default:
            return;
        }
    }

}
