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
                                    

package org.modelio.property.ui.data.standard.common;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.core.ui.ktable.IPropertyModel;
import org.modelio.core.ui.ktable.types.IPropertyType;
import org.modelio.core.ui.ktable.types.bool.BooleanType;
import org.modelio.core.ui.ktable.types.element.MultipleElementType;
import org.modelio.core.ui.ktable.types.element.SingleElementType;
import org.modelio.core.ui.ktable.types.enumeration.EnumType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.vcore.session.impl.CoreSession;
import org.modelio.vcore.smkernel.mapi.MAttribute;
import org.modelio.vcore.smkernel.mapi.MClass;
import org.modelio.vcore.smkernel.mapi.MDependency;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.meta.SmDependency;

/**
 * Generic data model designed for Dynamic metaclasses.
 */
@objid ("dc2aa3d2-5ca4-4f00-985a-7fa265d38be6")
@SuppressWarnings("unchecked")
public class DynamicPropertyModel implements IPropertyModel {
    @objid ("e45de383-4ab6-46c9-ba6f-0c1abcbe1e19")
    private StringType labelStringType;

    @objid ("9ed6d31c-d949-4601-9bc9-dd3519291c8d")
    private StringType stringType;

    @objid ("52d8707c-3256-4bed-92f0-1780ac318627")
    private BooleanType booleanType = new BooleanType();

    @objid ("044d5471-f094-4c79-af3f-a0be8e1ac952")
    private List<MAttribute> attributes;

    @objid ("5c43903c-5ecf-4c3f-bf32-c05f1e54cd6d")
    private List<MDependency> dependencies;

    @objid ("a261d144-477f-44a7-9074-fa5965be415a")
    private MObject theEditedElement;

    /**
     * Create a new data model from any MObject.
     */
    @objid ("c355d031-4aba-4c5c-8ec5-6c8ec28f446a")
    public DynamicPropertyModel(MObject theEditedElement) {
        this.theEditedElement = theEditedElement;
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
        
        this.attributes = new ArrayList<>();
        for (MAttribute mAtt : theEditedElement.getMClass().getAttributes(true)) {
            // Filter "status" attribute only
            if (!mAtt.getName().equals("status")) {
                this.attributes.add(0, mAtt);
            }
        }
        
        this.dependencies = new ArrayList<>();
        for (MDependency mDep : theEditedElement.getMClass().getDependencies(true)) {
            if (((SmDependency) mDep).isPartOf() && !mDep.isComposition() && !mDep.isSharedComposition()) {
                this.dependencies.add(0, mDep);
            }
        }
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("d5414005-5432-4e6c-84b7-865a09e18afd")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("93ae1f6d-25be-489a-9f7a-e6ae75d66d21")
    @Override
    public int getRowsNumber() {
        return 1 + this.dependencies.size() + this.attributes.size();
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("c9396334-28cf-422e-a4b2-bf9aee0d08c8")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            switch (row) {
            case 0: // Header
                return this.theEditedElement.getMClass().getName();
            default:
                if (row <= this.dependencies.size()) {
                    final MDependency mDep = this.dependencies.get(row - 1);
                    return mDep.getName();
                } else {
                    final MAttribute mAtt = this.attributes.get(row - this.dependencies.size() - 1);
                    return mAtt.getName();
                }
            }
        case 1: // col 1 is the property value
            switch (row) {
            case 0: // Header
                return "Value";
            default:
                if (row <= this.dependencies.size()) {
                    final MDependency mDep = this.dependencies.get(row - 1);
                    final List<MObject> values = this.theEditedElement.mGet(mDep);
                    if (mDep.getMaxCardinality() == 1) {
                        return values.isEmpty() ? null : values.get(0);
                    } else {
                        return values;
                    }
                } else {
                    final MAttribute mAtt = this.attributes.get(row - this.dependencies.size() - 1);
                    return this.theEditedElement.mGet(mAtt);
                }
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
    @objid ("7eb7b68a-41aa-4c67-9d21-cd6db89881fd")
    @Override
    public IPropertyType getTypeAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key type
            return this.labelStringType;
        case 1: // col 1 is the property value type
            switch (row) {
            case 0: // Header
                return this.labelStringType;
            default:
                if (row <= this.dependencies.size()) {
                    final MDependency mDep = this.dependencies.get(row - 1);
                    return getPropertyType(mDep);
                } else {
                    final MAttribute mAtt = this.attributes.get(row - this.dependencies.size() - 1);
                    return getPropertyType(mAtt);
                }
            }
        default:
            return null;
        }
    }

    @objid ("4cc424bb-f6dc-4e83-b7e6-f98affe2eb35")
    private IPropertyType getPropertyType(MDependency mDep) {
        MClass type = mDep.getTarget();
        
        // Get the correct PropertyDefinition:
        if (mDep.getMaxCardinality() == 1) {
            return new SingleElementType(mDep.getMinCardinality() == 0, Metamodel.getJavaInterface(type), CoreSession.getSession(this.theEditedElement));
        } else {
            return new MultipleElementType(mDep.getMinCardinality() == 0, (Element) this.theEditedElement, mDep.getName(), (Class<? extends Element>) Metamodel.getJavaInterface(type), CoreSession.getSession(this.theEditedElement).getModel());
        }
    }

    @objid ("75949b82-aa85-47b7-a439-ba6fd9be2808")
    private IPropertyType getPropertyType(MAttribute mAtt) {
        Class<?> type = mAtt.getType();
        
        // Get the correct PropertyDefinition:
        if (type.isEnum()) {
            return new EnumType((Class<? extends Enum<?>>) type);
        } else if (type == Boolean.class) {
            return this.booleanType;
        } else {
            // Unknown property type, treat as a text
            return this.stringType;
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
    @objid ("420ec776-a653-434e-b33e-99a041ab03a4")
    @Override
    public void setValueAt(int row, int col, Object value) {
        switch (col) {
        case 0: // Keys cannot be modified
            return;
        case 1: // col 1 is the property value
            switch (row) {
            case 0:
                return; // Header cannot be modified
            default:
                if (row <= this.dependencies.size()) {
                    final MDependency mDep = this.dependencies.get(row - 1);
                    final List<MObject> values = this.theEditedElement.mGet(mDep);
                    if (mDep.getMaxCardinality() == 1) {
                        values.clear();
                        if (value != null) {
                            values.add((MObject) value);
                        }
                    } else {
                        values.clear();
                        if (value != null) {
                            values.addAll((List<? extends MObject>)value);
                        }
                    }
                } else {
                    final MAttribute mAtt = this.attributes.get(row - this.dependencies.size() - 1);
                    if (mAtt.getType() == Integer.class) {
                        // There is no 'integer' type, cast the String instead
                        this.theEditedElement.mSet(mAtt, Integer.valueOf((String) value));
                    } else {
                        this.theEditedElement.mSet(mAtt, value);
                    }
                }
            }
            break;
        default:
            return;
        }
    }

    @objid ("896f8f2a-374c-43a5-bebd-6fce279ff3a0")
    @Override
    public boolean isEditable(int row, int col) {
        if (col == 0) {
            return false;
        }
        return this.theEditedElement.isModifiable();
    }

}
