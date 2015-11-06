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
import org.modelio.core.ui.ktable.types.enumeration.EnumType;
import org.modelio.core.ui.ktable.types.list.EditableListType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.mda.ModuleComponent;
import org.modelio.metamodel.uml.statik.AggregationKind;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.KindOfAccess;
import org.modelio.metamodel.uml.statik.NaryAssociation;
import org.modelio.metamodel.uml.statik.NaryAssociationEnd;
import org.modelio.metamodel.uml.statik.VisibilityMode;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.model.IMObjectFilter;
import org.modelio.vcore.session.impl.CoreSession;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * N-ary association property model.
 */
@objid ("8ecf794f-c068-11e1-8c0a-002564c97630")
public class AssociationEndNPropertyModel extends AbstractPropertyModel<NaryAssociationEnd> {
    @objid ("a6756da8-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] { "NaryAssociation", "AssociationName", "Class", "Role", "Visibility", "MultiplicityMin", "MultiplicityMax", "AccessMode", "IsAbstract", "IsClass", "IsOrdered", "IsUnique" };

    @objid ("8ecf7954-c068-11e1-8c0a-002564c97630")
    private List<NaryAssociationEnd> displayedRoles;

    @objid ("8ecf795c-c068-11e1-8c0a-002564c97630")
    private NaryAssociation theNaryAssociation;

    @objid ("8ecf795f-c068-11e1-8c0a-002564c97630")
    private StringType labelStringType = null;

    @objid ("8ecf7960-c068-11e1-8c0a-002564c97630")
    private StringType stringType = null;

    @objid ("8ecf7961-c068-11e1-8c0a-002564c97630")
    private BooleanType booleanType = null;

    @objid ("8ecf7962-c068-11e1-8c0a-002564c97630")
    private SingleElementType classifierType = null;

    @objid ("8ecf7963-c068-11e1-8c0a-002564c97630")
    private EnumType visibilityType = null;

    @objid ("8ecf7964-c068-11e1-8c0a-002564c97630")
    private EnumType kindOfAccessType = null;

    @objid ("8ecf7965-c068-11e1-8c0a-002564c97630")
    private EnumType kindOfAssociationType = null;

    @objid ("8ecf7966-c068-11e1-8c0a-002564c97630")
    private EditableListType cardinalityMinType = null;

    @objid ("8ecf7967-c068-11e1-8c0a-002564c97630")
    private EditableListType cardinalityMaxType = null;

    @objid ("8ecf7968-c068-11e1-8c0a-002564c97630")
    public AssociationEndNPropertyModel(NaryAssociationEnd element) {
        super(element);
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
        this.booleanType = new BooleanType();
        
        this.classifierType = new SingleElementType(false, Classifier.class, CoreSession.getSession(this.theEditedElement));
        this.classifierType.setElementFilter(new ClassifierTypeFilter());
        
        this.visibilityType = new EnumType(VisibilityMode.class);
        this.kindOfAccessType = new EnumType(KindOfAccess.class);
        this.kindOfAssociationType = new EnumType(AggregationKind.class);
        
        List<String> cardinalityMinValues = new ArrayList<>();
        cardinalityMinValues.add("0");
        cardinalityMinValues.add("1");
        this.cardinalityMinType = new EditableListType(true, cardinalityMinValues);
        
        List<String> cardinalityMaxValues = new ArrayList<>();
        cardinalityMaxValues.add("1");
        cardinalityMaxValues.add("*");
        this.cardinalityMaxType = new EditableListType(true, cardinalityMaxValues);
        
        // Order the displayed roles as following:
        // - this role first for n-ary associations
        this.theNaryAssociation = element.getNaryAssociation();
        if (this.theNaryAssociation != null) {
            final EList<NaryAssociationEnd> roles = this.theNaryAssociation.getNaryEnd();
            this.displayedRoles = new ArrayList<> (roles.size());
            this.displayedRoles.add(this.theEditedElement);
            for (NaryAssociationEnd r : roles) {
                if (!r.equals(this.theEditedElement))
                    this.displayedRoles.add(r);
            }
        }
    }

    @objid ("8ed0ffc7-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return this.displayedRoles.size() + 1;
    }

    @objid ("8ed0ffcb-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return AssociationEndNPropertyModel.PROPERTIES.length;
    }

    @objid ("8ed0ffd0-c068-11e1-8c0a-002564c97630")
    @Override
    public IPropertyType getTypeAt(int row, int col) {
        switch (col) {
        
        case 0: // col 0 is the property name
            return this.labelStringType;
        
        default:
            switch (row) {
            case 0: // Title
                return this.labelStringType;
            case 1: // NaryAssociation name
                return this.stringType;
            case 2: // Role owner
                return this.classifierType;
            case 3: // role name
                return this.stringType;
            case 4:
                return this.visibilityType;
            case 5:
                return this.cardinalityMinType;
            case 6:
                return this.cardinalityMaxType;
            case 7:
                return this.kindOfAccessType;
            case 8:
                return this.booleanType;
            case 9:
                return this.booleanType;
            case 10:
                return this.booleanType;
            case 11:
                return this.booleanType;
            default:
                return null;
            }
        }
    }

    @objid ("8ed0ffd5-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        if (row == 1 ) {
            // NaryAssociation name row
            if (col == 0)
                return AssociationEndNPropertyModel.PROPERTIES[row];
            else if (col == 1) 
                return this.theNaryAssociation.getName();
            else
                return "";
        }
        
        switch (col) {
        case 0: // col 0 is the property name
            return AssociationEndNPropertyModel.PROPERTIES[row];
        default:
            return getPropertyValue(row, this.displayedRoles.get(col-1));
        }
    }

    @objid ("8ed0ffda-c068-11e1-8c0a-002564c97630")
    @Override
    public boolean isEditable(int row, int col) {
        if (col == 0) {
            // Labels
            return false;
        } else if (row == 1) {
            // NaryAssociation name line
            if (col == 1) {
                return this.theNaryAssociation.isModifiable();
            } 
            // else
            return false;
        } else {
            // Other cells
            NaryAssociationEnd relatedEnd = this.displayedRoles.get(col - 1);
            if (!relatedEnd.isModifiable()) {
                return false;
            }
        }
        return true;
    }

    @objid ("8ed0ffe1-c068-11e1-8c0a-002564c97630")
    @Override
    public void setValueAt(int row, int col, Object value) {
        switch (col) {
        
        case 0:
            return;
        default:
            setPropertyValue(row, this.displayedRoles.get(col-1), value);
        return;
        }
    }

    @objid ("8ed0ffe6-c068-11e1-8c0a-002564c97630")
    private Object getPropertyValue(int row, NaryAssociationEnd NaryAssociationEnd) {
        switch (row) {
        case 0: // Title
            Classifier type = NaryAssociationEnd.getOwner();
            if (type == null) 
                return "";
            else
                return MessageFormat.format(MetamodelLabels.getString("Title.from"), type.getName());
        case 1:
            return this.theNaryAssociation.getName();
        case 2:
            // Type
            return NaryAssociationEnd.getOwner();
        case 3:
            // Role name
            return NaryAssociationEnd.getName();
        case 4:
            return NaryAssociationEnd.getVisibility();
        case 5:
            return NaryAssociationEnd.getMultiplicityMin();
        case 6:
            return NaryAssociationEnd.getMultiplicityMax();
        case 7:
            return NaryAssociationEnd.getChangeable();
        case 8:
            return NaryAssociationEnd.isIsAbstract()?Boolean.TRUE:Boolean.FALSE;
        case 9:
            return NaryAssociationEnd.isIsClass()?Boolean.TRUE:Boolean.FALSE;
        case 10:
            return NaryAssociationEnd.isIsOrdered()?Boolean.TRUE:Boolean.FALSE;
        case 11:
            return NaryAssociationEnd.isIsUnique()?Boolean.TRUE:Boolean.FALSE;
        default:
            return null;
        }
    }

    @objid ("8ed0ffed-c068-11e1-8c0a-002564c97630")
    private void setPropertyValue(int row, NaryAssociationEnd NaryAssociationEnd, Object value) {
        switch (row) {
        case 0:
            return;
        case 1:
            if (this.theNaryAssociation != null) {
                this.theNaryAssociation.setName((String)value);
            }
            break;
        case 2:
            NaryAssociationEnd.setOwner((Classifier) value);
            break;
        case 3:
            NaryAssociationEnd.setName(String.valueOf(value));
            break;
        case 4:
            NaryAssociationEnd.setVisibility((VisibilityMode) value);
            break;
        case 5:
            NaryAssociationEnd.setMultiplicityMin(String.valueOf(value));
            break;
        case 6:
            NaryAssociationEnd.setMultiplicityMax(String.valueOf(value));
            break;
        case 7:
            NaryAssociationEnd.setChangeable((KindOfAccess) value);
            break;
        case 8:
            NaryAssociationEnd.setIsAbstract(((Boolean) value).booleanValue());
            break;
        case 9:
            NaryAssociationEnd.setIsClass(((Boolean) value).booleanValue());
            break;
        case 10:
            NaryAssociationEnd.setIsOrdered(((Boolean) value).booleanValue());
            break;
        case 11:
            NaryAssociationEnd.setIsUnique(((Boolean) value).booleanValue());
            break;
        default:
            return;
        }
    }

    @objid ("8ed0fff4-c068-11e1-8c0a-002564c97630")
    protected static class ClassifierTypeFilter implements IMObjectFilter {
        @objid ("8ed0fff5-c068-11e1-8c0a-002564c97630")
        @Override
        public boolean accept(MObject el) {
            Classifier type = (Classifier) el;
            
            // TODO CHM predefined types
            if (type.getName().equals("undefined")) {
                return false;
            } else if (type instanceof ModuleComponent) {
                return false;
            } else {
                return true;
            }
        }

    }

}
