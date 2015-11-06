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

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.core.ui.ktable.types.IPropertyType;
import org.modelio.core.ui.ktable.types.element.SingleElementType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.metamodel.uml.infrastructure.NoteType;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.model.IMObjectFilter;
import org.modelio.vcore.session.impl.CoreSession;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("8f4fb54f-c068-11e1-8c0a-002564c97630")
public class NotePropertyModel extends AbstractPropertyModel<Note> {
    @objid ("a790b148-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] {"Note", "NoteType"};

    @objid ("8f4fb557-c068-11e1-8c0a-002564c97630")
    private StringType labelStringType = null;

    @objid ("8f4fb558-c068-11e1-8c0a-002564c97630")
    private SingleElementType noteType = null;

    /**
     * Instantiate the note type properties view.
     * @param model
     * @param theEditedElement the current note type.
     */
    @objid ("8f4fb559-c068-11e1-8c0a-002564c97630")
    public NotePropertyModel(final Note theEditedElement) {
        super(theEditedElement);
        
        this.labelStringType = new StringType(false);
        
        final ModelElement context = theEditedElement.getSubject();
        
        this.noteType = new SingleElementType(false, NoteType.class, CoreSession.getSession(this.theEditedElement));
        this.noteType.setElementFilter(new IMObjectFilter() {
        
            @Override
            public boolean accept(final MObject element) {
                // TODO implement filter
                return true;
        //                try {
        //                    return element != null && element.equals(ModelProperty.getInstance().getModelingSession().getModel().getMetamodelExtensions().getNoteType(context.getClass(), element.getName()));
        //                } catch (NoteTypeNotFoundException e) {
        //                    return false;
        //                }
            }
        });
    }

    /**
     * (non-Javadoc)
     * @see com.modeliosoft.modelio.edition.property.model.uml.PropertyModel#getColumnNumber()
     */
    @objid ("8f4fb560-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * (non-Javadoc)
     * @see com.modeliosoft.modelio.edition.property.model.uml.PropertyModel#getRowsNumber()
     */
    @objid ("8f4fb566-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return NotePropertyModel.PROPERTIES.length;
    }

    /**
     * (non-Javadoc)
     * @see com.modeliosoft.modelio.edition.property.model.uml.PropertyModel#getTypeAt(int, int)
     */
    @objid ("8f4fb56c-c068-11e1-8c0a-002564c97630")
    @Override
    public IPropertyType getTypeAt(final int row, final int col) {
        switch (col) {
            case 0: // col 0 is the property key type
                return this.labelStringType;
            case 1: // col 1 is the property value type
                switch (row) {
                    case 0: // Header
                        return this.labelStringType;
                    case 1:
                        return this.noteType;
                    default:
                        return null;
                }
            default:
                return null;
        }
    }

    /**
     * (non-Javadoc)
     * @see com.modeliosoft.modelio.edition.property.model.uml.PropertyModel#getValueAt(int, int)
     */
    @objid ("8f4fb575-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(final int row, final int col) {
        switch (col) {
            case 0: // col 0 is the property key
                return NotePropertyModel.PROPERTIES[row];
            case 1: // col 1 is the property value
                switch (row) {
                    case 0: // Header
                        return "Value";
                    case 1:
                        return this.theEditedElement.getModel();
                    default:
                        return null;
                }
            default:
                return null;
        }
    }

    /**
     * (non-Javadoc)
     * @see com.modeliosoft.modelio.edition.property.model.uml.PropertyModel#setValueAt(int, int, java.lang.Object)
     */
    @objid ("8f4fb57e-c068-11e1-8c0a-002564c97630")
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
                        this.theEditedElement.setModel((NoteType) value);
                        return;
                    default:
                        return;
                }
            default:
                return;
        }
    }

}
