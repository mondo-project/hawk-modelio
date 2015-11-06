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
import org.modelio.core.ui.MetamodelLabels;
import org.modelio.core.ui.ktable.types.IPropertyType;
import org.modelio.core.ui.ktable.types.bool.BooleanType;
import org.modelio.core.ui.ktable.types.element.SingleElementType;
import org.modelio.core.ui.ktable.types.ghost.GhostType;
import org.modelio.core.ui.ktable.types.list.EditableListType;
import org.modelio.core.ui.ktable.types.modelelement.ModelElementListType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.AssociationEnd;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.metamodel.uml.statik.LinkEnd;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.impl.CoreSession;

/**
 * <i>LinkEnd</i> data model.
 * <p>
 * This class provides the list of properties for the <i>LinkEnd</i> metaclass.
 */
@objid ("8f3f0bfc-c068-11e1-8c0a-002564c97630")
public class LinkEnd2PropertyModel extends AbstractPropertyModel<LinkEnd> {
    /**
     * Properties to display for <i>LinkEnd</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for other rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a77420c8-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] {"LinkEnd", "LinkName", "Link.Base", "Linked", "Name", "MultiplicityMin", "MultiplicityMax", "IsNavigable", "IsOrdered", "IsUnique"};

    @objid ("8f416d09-c068-11e1-8c0a-002564c97630")
    private StringType labelStringType = null;

    @objid ("8f416d0a-c068-11e1-8c0a-002564c97630")
    private StringType stringType = null;

    @objid ("8f416d0b-c068-11e1-8c0a-002564c97630")
    private BooleanType booleanType = null;

    @objid ("8f416d0c-c068-11e1-8c0a-002564c97630")
    private EditableListType cardinalityMinType = null;

    @objid ("8f416d0d-c068-11e1-8c0a-002564c97630")
    private EditableListType cardinalityMaxType = null;

    @objid ("8f416d0e-c068-11e1-8c0a-002564c97630")
    private SingleElementType linkedType = null;

    @objid ("bb7603db-19f2-11e2-ad19-002564c97630")
    private BooleanType navigabilityType;

    @objid ("61f9a828-f167-4123-9e90-db32960129ab")
    private GhostType ghostType;

    /**
     * Create a new <i>LinkEnd</i> data model from an <i>LinkEnd</i>.
     * @param model
     */
    @objid ("8f416d0f-c068-11e1-8c0a-002564c97630")
    public LinkEnd2PropertyModel(LinkEnd theLinkEnd) {
        super(theLinkEnd);
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
        this.booleanType = new BooleanType();
        ICoreSession session = CoreSession.getSession(this.theEditedElement);
        this.linkedType = new SingleElementType(false, Instance.class, session);
        this.ghostType = new GhostType();
        
        List<String> cardinalityMinValues = new ArrayList<>();
        cardinalityMinValues.add("0");
        cardinalityMinValues.add("1");
        this.cardinalityMinType = new EditableListType(true, cardinalityMinValues);
        
        List<String> cardinalityMaxValues = new ArrayList<>();
        cardinalityMaxValues.add("1");
        cardinalityMaxValues.add("*");
        this.cardinalityMaxType = new EditableListType(true, cardinalityMaxValues);
        
        this.navigabilityType = new BooleanType();
    }

    /**
     * @return
     * @return
     */
    @objid ("8f416d15-c068-11e1-8c0a-002564c97630")
    private Object getLinkPropertyValue(int row, int col) {
        // Link rows
        if (col == 0) {
            return LinkEnd2PropertyModel.PROPERTIES[row];
        } else if (col == 1) {
            return getPropertyValue(row, this.theEditedElement);
        } else {
            if (row == 1)
                return ""; // Link name
        }
        return "";
    }

    @objid ("8f416d1b-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 3;
    }

    @objid ("8f416d1f-c068-11e1-8c0a-002564c97630")
    private Object getPropertyValue(int row, LinkEnd aLinkEnd) {
        // Default value for non editable cells
        if (row > 7 && ! aLinkEnd.isNavigable()) {
            return "N/A";
        }
        
        switch (row) {
        case 0: // Title
            Instance type = aLinkEnd.getTarget()!=null ? aLinkEnd.getTarget() : aLinkEnd.getOpposite().getSource();
            if (type != null) {
                if (aLinkEnd == this.theEditedElement) {
                    return MessageFormat.format(MetamodelLabels.getString("Title.to"), type.getName());
                } else {
                    return MessageFormat.format(MetamodelLabels.getString("Title.from"), type.getName());
                }
            }
            return "";
        case 1:
            return aLinkEnd.getLink().getName();
        case 2:
            return aLinkEnd.getModel();
        case 3:
            if (aLinkEnd != null) {
                Instance relatedInstance = aLinkEnd.getTarget();
                return relatedInstance;
            } 
            // else
            return null;
        case 4:
            return aLinkEnd.getName();
        case 5:
            return aLinkEnd.getMultiplicityMin();
        case 6:
            return aLinkEnd.getMultiplicityMax();
        case 7:
            return aLinkEnd.isNavigable();
        case 8 :
            return aLinkEnd.isIsOrdered()?Boolean.TRUE:Boolean.FALSE;
        case 9 :
            return aLinkEnd.isIsUnique()?Boolean.TRUE:Boolean.FALSE;
        default:
            return null;
        }
    }

    @objid ("8f416d2f-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return LinkEnd2PropertyModel.PROPERTIES.length;
    }

    @objid ("8f416d34-c068-11e1-8c0a-002564c97630")
    @Override
    public IPropertyType getTypeAt(int row, int col) {
        // Non editable case
        if ((col == 1 && row > 7 && !this.theEditedElement.getOpposite().isNavigable()) ||
                (col == 2 && row > 7 && !this.theEditedElement.isNavigable())) {
            return this.ghostType;
        }
        
        switch (col) {
        
        case 0: // col 0 is the property name
            return this.labelStringType;
        
        case 1:
            switch (row) {
            case 2: // Link base Association
                LinkEnd relatedEnd = this.theEditedElement.getOpposite();
                return getBaseAssociationType(relatedEnd);
            default:
            }
            //$FALL-THROUGH$
        case 2:
            switch (row) {
            case 0: // Title
                return this.labelStringType;
            case 1: // Link Name
                return this.stringType;
            case 2: // Link base Association
                return getBaseAssociationType(this.theEditedElement);
            case 3: // LinkEnd Type
                return this.linkedType;
            case 4: // LinkEnd Name
                return this.stringType;
            case 5:
                return this.cardinalityMinType;
            case 6:
                return this.cardinalityMaxType;
            case 7:
                return this.navigabilityType;
            case 8:
                return this.booleanType;
            case 9:
                return this.booleanType;
            default:
                return null;
            }
        default:
            return null;
        }
    }

    @objid ("8f416d3a-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        if (row == 1) {
            return getLinkPropertyValue(row, col);
        }
        
        // LinkEnd rows
        switch (col) {
        
        case 0: // col 0 is the property name
            if (row == 0)
                return MessageFormat.format(MetamodelLabels.getString("Title.LinkEnd"), this.theEditedElement.getName());
            // else
            return LinkEnd2PropertyModel.PROPERTIES[row];
        
        case 1:
            LinkEnd relatedEnd = this.theEditedElement.getOpposite();
            if (relatedEnd != null) {
                return getPropertyValue(row, relatedEnd);
            } 
            // else
            return null;
        
        case 2:
            if (row == 1) {
                return "";      // Link name display only in the second column
            }
            return getPropertyValue(row, this.theEditedElement);
        
        default:
            return null;
        }
    }

    @objid ("8f416d3f-c068-11e1-8c0a-002564c97630")
    @Override
    public boolean isEditable(int row, int col) {
        if (col == 0) {
            // Labels are not editable
            return false;
        } else if (col == 1) {
            LinkEnd relatedEnd = this.theEditedElement.getOpposite();
            return relatedEnd.isModifiable();
        } else if (col == 2) {
            if (row == 1) {
                return false;      // Link name is only editable in the second column
            }
            return this.theEditedElement.isModifiable();
        }
        return false;
    }

    @objid ("8f416d46-c068-11e1-8c0a-002564c97630")
    private void setPropertyValue(int row, LinkEnd linkEnd, Object value) {
        switch (row) {
        case 0:
            return;
        case 1:
            linkEnd.getLink().setName(String.valueOf(value));
            break;
        case 2:
            final AssociationEnd model = (AssociationEnd) value;
            linkEnd.setModel(model);
            linkEnd.getOpposite().setModel(model.getOpposite());
            linkEnd.getLink().setModel(model.getAssociation());
            break;
        case 3:
            linkEnd.setTarget((Instance) value, true);
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
        case 7:
            linkEnd.setNavigable((Boolean) value);
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

    @objid ("8f416d4d-c068-11e1-8c0a-002564c97630")
    @Override
    public void setValueAt(int row, int col, Object value) {
        switch (col) {
        
        case 0:
            return;
        
        case 1:
            LinkEnd relatedEnd = this.theEditedElement.getOpposite();
            if (relatedEnd != null) {
                setPropertyValue(row, relatedEnd, value);
            }
            return;
        case 2:
            setPropertyValue(row, this.theEditedElement, value);
            return;
        default:
            return;
        }
    }

    @objid ("8d0dd793-0e69-420b-a07b-6e9c589f74d4")
    private IPropertyType getBaseAssociationType(LinkEnd editedEnd) {
        List<ModelElement> availableEnds = new ArrayList<>();
        
        Instance source = editedEnd.getOwner();
        Instance target = editedEnd.getOpposite().getOwner();
        NameSpace sourceBase = source.getBase();
        NameSpace targetBase = target.getBase();
        if (sourceBase != null && sourceBase instanceof Classifier && targetBase != null && targetBase instanceof Classifier) {
            for (AssociationEnd end : ((Classifier)sourceBase).getOwnedEnd()) {
                if (end.getOpposite().getOwner().equals(targetBase)) {
                    if (!availableEnds.contains(end)) {
                        availableEnds.add(end);
                    }
                }
            }
        
            for (AssociationEnd end : ((Classifier)sourceBase).getTargetingEnd()) {
                if (end.getOwner().equals(targetBase)) {
                    if (!availableEnds.contains(end.getOpposite())) {
                        availableEnds.add(end.getOpposite());
                    }
                }
            }
        }
        
        ModelElementListType type = new ModelElementListType(true, AssociationEnd.class, availableEnds, CoreSession.getSession(this.theEditedElement));
        return type;
    }

}
