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
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.ConnectorEnd;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.metamodel.uml.statik.LinkEnd;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.NaryAssociation;
import org.modelio.metamodel.uml.statik.NaryLink;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.impl.CoreSession;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * <i>ConnectorEnd</i> data model.
 * <p>
 * This class provides the list of properties for the <i>ConnectorEnd</i> metaclass.
 */
@objid ("8ef29195-c068-11e1-8c0a-002564c97630")
public class ConnectorEnd2PropertyModel extends AbstractPropertyModel<ConnectorEnd> {
    /**
     * Properties to display for <i>LinkEnd</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li>for the first row the value is the table header label (usually the metaclass name)
     * <li>for other rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a6c199a5-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] { "ConnectorEnd", "LinkName", "Base",
            "Linked", "Name", "ConnectorEndRepresentedFeature", "MultiplicityMin", "MultiplicityMax", "IsNavigable", "IsOrdered",
            "IsUnique" };

    @objid ("8ef291a3-c068-11e1-8c0a-002564c97630")
    private StringType labelStringType = null;

    @objid ("8ef291a4-c068-11e1-8c0a-002564c97630")
    private StringType stringType = null;

    @objid ("8ef291a5-c068-11e1-8c0a-002564c97630")
    private BooleanType booleanType = null;

    @objid ("8ef291a6-c068-11e1-8c0a-002564c97630")
    private EditableListType cardinalityMinType = null;

    @objid ("8ef291a7-c068-11e1-8c0a-002564c97630")
    private EditableListType cardinalityMaxType = null;

    @objid ("8ef291a8-c068-11e1-8c0a-002564c97630")
    private SingleElementType linkedType = null;

    @objid ("8ef291ab-c068-11e1-8c0a-002564c97630")
    private SingleElementType connectorEndRepresentedFeatureType = null;

    @objid ("bb791120-19f2-11e2-ad19-002564c97630")
    private final BooleanType navigabilityType;

    @objid ("1470a95f-3104-46e3-89cb-a24c5d4b9400")
    private GhostType ghostType;

    @objid ("8ef291ac-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 3;
    }

    @objid ("8ef291b1-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return PROPERTIES.length;
    }

    @objid ("8ef291b6-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        if (row == 1 || row == 2) {
            return getLinkPropertyValue(row, col);
        }
        
        // LinkEnd rows
        switch (col) {
        
        case 0: // col 0 is the property name
            if (row == 0) {
                return this.theEditedElement.getName() + " association";
            }
            // else
            return PROPERTIES[row];
        
        case 1:
            return getPropertyValue(row, this.theEditedElement.getOpposite());
        
        case 2:
            return getPropertyValue(row, this.theEditedElement);
        
        default:
            return null;
        }
    }

    @objid ("8ef291bc-c068-11e1-8c0a-002564c97630")
    @Override
    public IPropertyType getTypeAt(int row, int col) {
        // Non editable case
        if ((col == 1 && row > 8 && !this.theEditedElement.getOpposite().isNavigable()) ||
                (col == 2 && row > 8 && !this.theEditedElement.isNavigable())) {
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
            case 1:
                // Link name
                return this.stringType;       
            case 2: // Link base Association
                return getBaseAssociationType(this.theEditedElement);
            case 3: // LinkEnd Type
                return this.linkedType;
            case 4: // LinkEnd Name
                return this.stringType;
            case 5:
                return this.connectorEndRepresentedFeatureType;
            case 6:
                return this.cardinalityMinType;
            case 7:
                return this.cardinalityMaxType;
            case 8:
                return this.navigabilityType;
            case 9:
                return this.booleanType;
            case 10:
                return this.booleanType;
            default:
                return null;
            }
        default:
            return null;
        }
    }

    @objid ("8ef291c3-c068-11e1-8c0a-002564c97630")
    @Override
    public void setValueAt(int row, int col, Object value) {
        switch (col) {       
            case 0:
                return;       
            case 1:
                setPropertyValue(row, this.theEditedElement.getOpposite(), value);
                break;
            case 2:
                setPropertyValue(row, this.theEditedElement, value);
                return;
            default:
                return;
        }
    }

    @objid ("8ef291c9-c068-11e1-8c0a-002564c97630")
    @Override
    public boolean isEditable(int row, int col) {
        if (col == 0) {
            // Labels are not editable
            return false;
        } else if (col == 1) {
            return this.theEditedElement.getOpposite().isModifiable();
        } else if (col == 2) {
            if (row == 1 || row == 2) return false;
            return this.theEditedElement.isModifiable();
        }
        return true;
    }

    /**
     * Create a new <i>LinkEnd</i> data model from an <i>LinkEnd</i>.
     * @param model
     */
    @objid ("8ef4182b-c068-11e1-8c0a-002564c97630")
    public ConnectorEnd2PropertyModel(ConnectorEnd theConnectorEnd) {
        super(theConnectorEnd);
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
        this.booleanType = new BooleanType();
        this.ghostType = new GhostType();
        
        ICoreSession session = CoreSession.getSession(this.theEditedElement);
        this.linkedType = new SingleElementType(false, Instance.class, session);
        
        List<java.lang.Class<? extends MObject>> connectorRepresentedFeatureValues = new ArrayList<>();
        connectorRepresentedFeatureValues.add(Attribute.class);
        connectorRepresentedFeatureValues.add(NaryAssociation.class);
        connectorRepresentedFeatureValues.add(NaryLink.class);
        
        List<java.lang.Class<? extends MObject>> connectorEndRepresentedFeatureValues = new ArrayList<>();
        connectorEndRepresentedFeatureValues.add(Attribute.class);
        connectorEndRepresentedFeatureValues.add(AssociationEnd.class);
        connectorEndRepresentedFeatureValues.add(LinkEnd.class);
        this.connectorEndRepresentedFeatureType = new SingleElementType(true, connectorEndRepresentedFeatureValues);
        
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
    @objid ("8ef41831-c068-11e1-8c0a-002564c97630")
    private Object getLinkPropertyValue(int row, int col) {
        // Link rows
        if (col == 0) {
            return PROPERTIES[row];
        } else if (col == 1) {
            return getPropertyValue(row, this.theEditedElement);
        } else {
            if (row == 1) {
                return ""; // Link name
            }
            // else
            return null; // Link base Association
        }
    }

    @objid ("8ef41837-c068-11e1-8c0a-002564c97630")
    private Object getPropertyValue(int row, LinkEnd aLinkEnd) {
        // Default value for non editable cells
        if (row > 8 && ! aLinkEnd.isNavigable()) {
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
            // Link name
            return aLinkEnd.getLink().getName();
        case 2:
            // Link base association
            return aLinkEnd.getModel();
        case 3:
            return aLinkEnd.getTarget();
        case 4:
            return aLinkEnd.getName();
        case 5:
            return ((ConnectorEnd) aLinkEnd).getRepresentedFeature();
        case 6:
            return aLinkEnd.getMultiplicityMin();
        case 7:
            return aLinkEnd.getMultiplicityMax();
        case 8:
            return aLinkEnd.isNavigable();
        case 9:
            return aLinkEnd.isIsOrdered() ? Boolean.TRUE : Boolean.FALSE;
        case 10:
            return aLinkEnd.isIsUnique() ? Boolean.TRUE : Boolean.FALSE;
        default:
            return null;
        }
    }

    @objid ("8ef41847-c068-11e1-8c0a-002564c97630")
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
            ((ConnectorEnd) linkEnd).setRepresentedFeature((ModelElement) value);
            break;
        case 6:
            linkEnd.setMultiplicityMin(String.valueOf(value));
            break;
        case 7:
            linkEnd.setMultiplicityMax(String.valueOf(value));
            break;
        case 8:
            linkEnd.setNavigable((Boolean) value);
            break;
        case 9:
            linkEnd.setIsOrdered(((Boolean) value).booleanValue());
            break;
        case 10:
            linkEnd.setIsUnique(((Boolean) value).booleanValue());
            break;
        default:
            return;
        }
    }

    @objid ("bfc3ba25-8d46-45b3-ac73-9cc11c2c5c5f")
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
