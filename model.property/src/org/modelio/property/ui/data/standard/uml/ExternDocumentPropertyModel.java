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
import org.modelio.metamodel.uml.infrastructure.ExternDocument;
import org.modelio.metamodel.uml.infrastructure.ExternDocumentType;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.model.IMObjectFilter;
import org.modelio.vcore.session.api.model.IModel;
import org.modelio.vcore.session.impl.CoreSession;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("8f1a3de1-c068-11e1-8c0a-002564c97630")
public class ExternDocumentPropertyModel extends AbstractPropertyModel<ExternDocument> {
    @objid ("a714e9c8-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] {"ExternDocument", "ExternDocumentType"};

    @objid ("8f1a3de9-c068-11e1-8c0a-002564c97630")
    private StringType labelStringType = null;

    @objid ("8f1a3dea-c068-11e1-8c0a-002564c97630")
    private SingleElementType externDocumentType = null;

    /**
     * Instantiate the externDocument type properties view.
     * @param model
     * @param theEditedElement the current externDocument type.
     */
    @objid ("8f1a3deb-c068-11e1-8c0a-002564c97630")
    public ExternDocumentPropertyModel(final ExternDocument theEditedElement, IModel model) {
        super(theEditedElement);
        
        this.labelStringType = new StringType(false);
        
        final ModelElement context = theEditedElement.getSubject();
        
        this.externDocumentType = new SingleElementType(false, ExternDocumentType.class, CoreSession.getSession(this.theEditedElement));
        this.externDocumentType.setElementFilter(new IMObjectFilter() {
        
            @Override
            public boolean accept(final MObject element) {
                //TODO CHM
        //                try {
        //                    return element != null && element.equals(ModelProperty.getInstance().getModelingSession().getModel().getMetamodelExtensions().getExternDocumentType(context.getClass(), element.getName()));
        //                } catch (DocumentTypeNotFoundException e) {
        //                    return false;
        //                }
                return true;
            }
        });
    }

    /**
     * (non-Javadoc)
     * @see com.modeliosoft.modelio.edition.property.model.uml.PropertyModel#getColumnNumber()
     */
    @objid ("8f1a3df2-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * (non-Javadoc)
     * @see com.modeliosoft.modelio.edition.property.model.uml.PropertyModel#getRowsNumber()
     */
    @objid ("8f1a3df8-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return ExternDocumentPropertyModel.PROPERTIES.length;
    }

    /**
     * (non-Javadoc)
     * @see com.modeliosoft.modelio.edition.property.model.uml.PropertyModel#getTypeAt(int, int)
     */
    @objid ("8f1a3dfe-c068-11e1-8c0a-002564c97630")
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
                        return this.externDocumentType;
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
    @objid ("8f1bc465-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(final int row, final int col) {
        switch (col) {
            case 0: // col 0 is the property key
                return ExternDocumentPropertyModel.PROPERTIES[row];
            case 1: // col 1 is the property value
                switch (row) {
                    case 0: // Header
                        return "Value";
                    case 1:
                        return this.theEditedElement.getType();
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
    @objid ("8f1bc46e-c068-11e1-8c0a-002564c97630")
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
                        this.theEditedElement.setType((ExternDocumentType) value);
                        return;
                    default:
                        return;
                }
            default:
                return;
        }
    }

}
