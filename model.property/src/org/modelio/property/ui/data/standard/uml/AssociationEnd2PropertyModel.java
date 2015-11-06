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
import org.modelio.core.ui.ktable.types.enumeration.EnumType;
import org.modelio.core.ui.ktable.types.ghost.GhostType;
import org.modelio.core.ui.ktable.types.list.EditableListType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.mda.ModuleComponent;
import org.modelio.metamodel.uml.statik.AggregationKind;
import org.modelio.metamodel.uml.statik.Association;
import org.modelio.metamodel.uml.statik.AssociationEnd;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.KindOfAccess;
import org.modelio.metamodel.uml.statik.VisibilityMode;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.model.IMObjectFilter;
import org.modelio.vcore.session.impl.CoreSession;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Binary association property model.
 */
@objid ("8ecdf296-c068-11e1-8c0a-002564c97630")
public class AssociationEnd2PropertyModel extends AbstractPropertyModel<AssociationEnd> {
    @objid ("a6730c4c-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] { "Association", "AssociationName", "IsNavigable", "RoleName", "RoleTarget", "AssociationType", "MultiplicityMin", "MultiplicityMax",
		"Visibility", "IsModifiable", "AccessMode", "IsAbstract", "IsClass", "IsOrdered", "IsUnique" };

    @objid ("8ecdf29e-c068-11e1-8c0a-002564c97630")
    private StringType labelStringType = null;

    @objid ("8ecdf29f-c068-11e1-8c0a-002564c97630")
    private StringType stringType = null;

    @objid ("8ecdf2a0-c068-11e1-8c0a-002564c97630")
    private BooleanType booleanType = null;

    @objid ("8ecdf2a1-c068-11e1-8c0a-002564c97630")
    private SingleElementType classifierType = null;

    @objid ("8ecdf2a2-c068-11e1-8c0a-002564c97630")
    private EnumType visibilityType = null;

    @objid ("8ecdf2a3-c068-11e1-8c0a-002564c97630")
    private EnumType kindOfAccessType = null;

    @objid ("8ecdf2a4-c068-11e1-8c0a-002564c97630")
    private EnumType kindOfAssociationType = null;

    @objid ("8ecdf2a5-c068-11e1-8c0a-002564c97630")
    private EditableListType cardinalityMinType = null;

    @objid ("8ecdf2a6-c068-11e1-8c0a-002564c97630")
    private EditableListType cardinalityMaxType = null;

    @objid ("ad621769-1d28-11e2-82de-002564c97630")
    private GhostType ghostType;

    @objid ("8ecdf2a7-c068-11e1-8c0a-002564c97630")
    public AssociationEnd2PropertyModel(AssociationEnd element) {
        super(element);
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
        this.booleanType = new BooleanType();
        
        this.classifierType = new SingleElementType(false, Classifier.class, CoreSession.getSession(this.theEditedElement));
        this.classifierType.setElementFilter(new ClassifierTypeFilter());
        
        this.visibilityType = new EnumType(VisibilityMode.class);
        this.kindOfAccessType = new EnumType(KindOfAccess.class);
        this.kindOfAssociationType = new EnumType(AggregationKind.class);
        this.ghostType = new GhostType();
        
        List<String> cardinalityMinValues = new ArrayList<>();
        cardinalityMinValues.add("0");
        cardinalityMinValues.add("1");
        this.cardinalityMinType = new EditableListType(true, cardinalityMinValues);
        
        List<String> cardinalityMaxValues = new ArrayList<>();
        cardinalityMaxValues.add("1");
        cardinalityMaxValues.add("*");
        this.cardinalityMaxType = new EditableListType(true, cardinalityMaxValues);
    }

    @objid ("8ecdf2ac-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property name
            if (row == 0) {
                return MessageFormat.format(MetamodelLabels.getString("Title.Association"), this.theEditedElement.getName());
            }
            // else
            return AssociationEnd2PropertyModel.PROPERTIES[row];
        case 1:
            return getPropertyValue(row, this.theEditedElement.getOpposite());
        case 2:
            if (row == 1) {
                return "";      // Association name display only in the second column
            }
            return getPropertyValue(row, this.theEditedElement);
        default:
            return null;
        }
    }

    @objid ("8ecdf2b1-c068-11e1-8c0a-002564c97630")
    @Override
    public void setValueAt(int row, int col, Object value) {
        switch (col) {
        case 0:
            return;
        case 1:
            setPropertyValue(row, this.theEditedElement.getOpposite(), value);
            return;
        case 2:
            setPropertyValue(row, this.theEditedElement, value);
            return;
        default:
            return;
        }
    }

    @objid ("8ecdf2b6-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 3;
    }

    @objid ("8ecdf2ba-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return AssociationEnd2PropertyModel.PROPERTIES.length;
    }

    @objid ("8ecdf2bf-c068-11e1-8c0a-002564c97630")
    @Override
    public IPropertyType getTypeAt(int row, int col) {
        // Non editable case
        if ((col == 1 && row > 7 && !this.theEditedElement.getOpposite().isNavigable()) ||
                (col == 2 && row > 7 && !this.theEditedElement.isNavigable())) {
            return this.ghostType;
        }
        
        // Get the standard types
        switch (col) {
        case 0: // col 0 is the property name
            return this.labelStringType;
        
        case 1:
        case 2:
            switch (row) {
            case 0: // Title
                return this.labelStringType;
            case 1: // Association Name
                return this.stringType;
            case 2: // Navigability
                return this.booleanType;
            case 3: // Role Name
                return this.stringType;
            case 4: // Type
                return this.classifierType;
            case 5: // Kind
                return this.kindOfAssociationType;
            case 6:
                return this.cardinalityMinType;
            case 7:
                return this.cardinalityMaxType;
            case 8:
                return this.visibilityType;
            case 9:
                return this.booleanType;
            case 10:
                return this.kindOfAccessType;
            case 11:
                return this.booleanType;
            case 12:
                return this.booleanType;
            case 13:
                return this.booleanType;
            case 14:
                return this.booleanType;
            default:
                return null;
            }
        default:
            return null;
        }
    }

    @objid ("8ecdf2c5-c068-11e1-8c0a-002564c97630")
    @Override
    public boolean isEditable(int row, int col) {
        if (col == 0) {
            // Labels are not editable
            return false;
        } else if (col == 1) {
            return this.theEditedElement.getOpposite().isModifiable();
        } else if (col == 2) {
            if (row == 1) {
                return false;      // Association name is only editable in the second column
            }
            return this.theEditedElement.isModifiable();
        }
        return false;
    }

    @objid ("8ecf7925-c068-11e1-8c0a-002564c97630")
    private Object getPropertyValue(int row, AssociationEnd associationEnd) {
        // Default value for non editable cells
        if (row > 7 && !associationEnd.isNavigable()) {
            return "N/A";
        }
        
        switch (row) {
        case 0: // Title
            Classifier type = associationEnd.getTarget() != null? associationEnd.getTarget():associationEnd.getOpposite().getSource();
            if (type != null) {
                if (associationEnd == this.theEditedElement) {
                    return MessageFormat.format(MetamodelLabels.getString("Title.to"), type.getName());
                } else {
                    return MessageFormat.format(MetamodelLabels.getString("Title.from"), type.getName());
                }
            }
            return "";
        case 1:
            final Association association = associationEnd.getAssociation();
            if (association != null)
                return association.getName();
            else
                return "<null>";
        case 2:
            return associationEnd.isNavigable();
        case 3:
            return associationEnd.getName();
        case 4:
            return associationEnd.getTarget();
        case 5:
            return associationEnd.getAggregation();
        case 6:
            return associationEnd.getMultiplicityMin();
        case 7:
            return associationEnd.getMultiplicityMax();
        case 8:
            return associationEnd.getVisibility();
        case 9:
            return associationEnd.isIsChangeable() ? Boolean.TRUE : Boolean.FALSE;
        case 10:
            return associationEnd.getChangeable();
        case 11:
            return associationEnd.isIsAbstract() ? Boolean.TRUE : Boolean.FALSE;
        case 12:
            return associationEnd.isIsClass() ? Boolean.TRUE : Boolean.FALSE;
        case 13:
            return associationEnd.isIsOrdered() ? Boolean.TRUE : Boolean.FALSE;
        case 14:
            return associationEnd.isIsUnique() ? Boolean.TRUE : Boolean.FALSE;
        default:
            return null;
        }
    }

    @objid ("8ecf7935-c068-11e1-8c0a-002564c97630")
    private void setPropertyValue(int row, AssociationEnd associationEnd, Object value) {
        switch (row) {
        case 0:
            return;
        case 1:
            associationEnd.getAssociation().setName(String.valueOf(value));
            break;
        case 2:
            associationEnd.setNavigable((Boolean) value);
            break;
        case 3:
            associationEnd.setName(String.valueOf(value));
            break;
        case 4:
            associationEnd.setTarget((Classifier) value, true);
            break;
        case 5:
            associationEnd.setAggregation((AggregationKind) value);
            break;
        case 6:
            associationEnd.setMultiplicityMin(String.valueOf(value));
            break;
        case 7:
            associationEnd.setMultiplicityMax(String.valueOf(value));
            break;
        case 8:
            associationEnd.setVisibility((VisibilityMode) value);
            break;
        case 9:
            associationEnd.setIsChangeable(((Boolean) value).booleanValue());
            break;
        case 10:
            associationEnd.setChangeable((KindOfAccess) value);
            break;
        case 11:
            associationEnd.setIsAbstract(((Boolean) value).booleanValue());
            break;
        case 12:
            associationEnd.setIsClass(((Boolean) value).booleanValue());
            break;
        case 13:
            associationEnd.setIsOrdered(((Boolean) value).booleanValue());
            break;
        case 14:
            associationEnd.setIsUnique(((Boolean) value).booleanValue());
            break;
        default:
            return;
        }
    }

    @objid ("8ecf793c-c068-11e1-8c0a-002564c97630")
    protected static class ClassifierTypeFilter implements IMObjectFilter {
//private PredefinedTypes predefinedTypes = null;
        @objid ("8ecf793d-c068-11e1-8c0a-002564c97630")
        public ClassifierTypeFilter() {
            /*
             * CoreSession modelingSession = ModelProperty.getInstance().getModelingSession();
             * 
             * if (modelingSession != null) { this.predefinedTypes = modelingSession.getModel().getPredefinedTypes(); }
             */
        }

        @objid ("8ecf7940-c068-11e1-8c0a-002564c97630")
        @Override
        public boolean accept(MObject el) {
            Classifier type = (Classifier) el;
            
            // TODO CHM predefined types
            if (type.equals(type.getName().equals("undefined"))) {
                return false;
            } else if (type instanceof ModuleComponent) {
                return false;
            } else {
                return true;
            }
        }

    }

}
