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
import org.modelio.core.ui.ktable.types.IPropertyType;
import org.modelio.core.ui.ktable.types.hybrid.HybridType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.uml.behavior.stateMachineModel.ConnectionPointReference;
import org.modelio.metamodel.uml.behavior.stateMachineModel.EntryPointPseudoState;
import org.modelio.metamodel.uml.behavior.stateMachineModel.ExitPointPseudoState;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.impl.CoreSession;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * <i>ConnectionPointReference</i> data model.
 * <p>
 * This class provides the list of properties for the <i>ConnectionPointReference</i> metaclass.
 */
@objid ("8eef8485-c068-11e1-8c0a-002564c97630")
public class ConnectionPointReferencePropertyModel extends AbstractPropertyModel<ConnectionPointReference> {
    /**
     * Properties to display for <i>ConnectionPointReference</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a6bcd6e9-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] { "ConnectionPointReference", "Name", "EntryExit" };

    @objid ("fa20e101-c5d4-11e1-8f21-002564c97630")
    private StringType labelStringType;

    @objid ("fa20e102-c5d4-11e1-8f21-002564c97630")
    private StringType stringType;

    @objid ("fa20e103-c5d4-11e1-8f21-002564c97630")
    private EntryExitType entryExitType;

    /**
     * Create a new <i>ConnectionPointReference</i> data model from an <i>ConnectionPointReference</i>.
     */
    @objid ("8ef10aef-c068-11e1-8c0a-002564c97630")
    public ConnectionPointReferencePropertyModel(ConnectionPointReference theEditedElement) {
        super(theEditedElement);
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
        this.entryExitType = new EntryExitType(CoreSession.getSession(this.theEditedElement));
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8ef10af5-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8ef10afa-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return ConnectionPointReferencePropertyModel.PROPERTIES.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8ef10aff-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return ConnectionPointReferencePropertyModel.PROPERTIES[row];
        case 1: // col 1 is the property value
            switch (row) {
            case 0: // Header
                return "Value";
            case 1:
                return this.theEditedElement.getName();
            case 2:
                return getEntryExit();
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
    @objid ("8ef10b05-c068-11e1-8c0a-002564c97630")
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
                return this.entryExitType;
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
    @objid ("8ef10b0d-c068-11e1-8c0a-002564c97630")
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
                setEntryExit(this.theEditedElement, value);
                break;
            default:
                return;
            }
              break;
        default:
            return;
        }
    }

    @objid ("8ef10b13-c068-11e1-8c0a-002564c97630")
    private ModelElement getEntryExit() {
        ModelElement ret = this.theEditedElement.getEntry();
        if (ret != null)
            return ret;
        ret = this.theEditedElement.getExit();
        return ret;
    }

    @objid ("8ef10b19-c068-11e1-8c0a-002564c97630")
    private void setEntryExit(ConnectionPointReference theEditedElement, Object value) {
        // Erase old value or exit if old value is new value
        EntryPointPseudoState old1 = theEditedElement.getEntry();
        if (old1 != null) {
            if (old1.equals(value))
                return;
            theEditedElement.setEntry(null);
        } else {
            ExitPointPseudoState old2 = theEditedElement.getExit();
            if (old2 != null) {
                if (old2.equals(value))
                    return;
                theEditedElement.setExit(null);
            }
        }
                
        if (value != null) {
            // Set new value
            if (EntryPointPseudoState.class.isAssignableFrom(value.getClass()))
                theEditedElement.setEntry((EntryPointPseudoState) value);
            else if (ExitPointPseudoState.class.isAssignableFrom(value.getClass()))
                theEditedElement.setExit((ExitPointPseudoState) value);
        }
    }

    @objid ("8ef10b1f-c068-11e1-8c0a-002564c97630")
    public static class EntryExitType extends HybridType {
        @objid ("8ef10b21-c068-11e1-8c0a-002564c97630")
        private List<Class<? extends MObject>> t;

        @objid ("8ef10b26-c068-11e1-8c0a-002564c97630")
        public EntryExitType(ICoreSession session) {
            super(session);
            this.t = new ArrayList<>();
            this.t.add(EntryPointPseudoState.class);
            this.t.add(ExitPointPseudoState.class);
        }

        @objid ("8ef10b28-c068-11e1-8c0a-002564c97630")
        @Override
        public List<Class<? extends MObject>> getTypes() {
            return this.t;
        }

        @objid ("c789bbc9-b5b8-4aba-a308-b170573fdeb1")
        @Override
        public boolean acceptStringValue() {
            return false;
        }

    }

}
