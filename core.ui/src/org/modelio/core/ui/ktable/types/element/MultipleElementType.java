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
                                    

package org.modelio.core.ui.ktable.types.element;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.SWTX;
import de.kupzog.ktable.renderers.DefaultCellRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.modelio.app.core.picking.IModelioPickingService;
import org.modelio.core.ui.ktable.types.text.MultipleElementCellRenderer;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.vcore.session.api.model.IModel;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("8def2b84-c068-11e1-8c0a-002564c97630")
public class MultipleElementType extends ElementType {
    @objid ("8df0b1e5-c068-11e1-8c0a-002564c97630")
    private Element editedElement = null;

    @objid ("a57ddea8-c068-11e1-8c0a-002564c97630")
    private String fieldName = null;

    @objid ("16cb16e6-16da-11e2-aa0d-002564c97630")
    private IModel session;

    @objid ("8df0b1e9-c068-11e1-8c0a-002564c97630")
    public MultipleElementType(boolean acceptNullValue, Element editedElement, String fieldName, Class<? extends Element> allowedClass, IModel session) {
        super(acceptNullValue, allowedClass);
        this.editedElement = editedElement;
        this.fieldName = fieldName;
        this.session = session;
    }

    @objid ("8df0b1f8-c068-11e1-8c0a-002564c97630")
    public Element getEditedElement() {
        return this.editedElement;
    }

    @objid ("8df0b1fe-c068-11e1-8c0a-002564c97630")
    public String getFieldName() {
        return this.fieldName;
    }

    /**
     * Renderer used to display many model elements
     */
    @objid ("0a7039c2-cb5b-11e1-9165-002564c97630")
    @Override
    public DefaultCellRenderer getRenderer() {
        MultipleElementCellRenderer multipleElementRenderer = new MultipleElementCellRenderer(DefaultCellRenderer.INDICATION_FOCUS);
        multipleElementRenderer.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE));
        multipleElementRenderer.setAlignment(SWTX.ALIGN_HORIZONTAL_CENTER | SWTX.ALIGN_VERTICAL_CENTER);
        return multipleElementRenderer;
    }

    @objid ("0a7060d5-cb5b-11e1-9165-002564c97630")
    @Override
    public KTableCellEditor getEditor(IModelioPickingService pickingService) {
        MultipleElementCellEditor multipleEditor = new MultipleElementCellEditor(this.session, pickingService);
        
        List<Class<? extends MObject>> classes = this.getAllowedClasses();
        
        if (classes.size() == 1) {
            Class<?> target = classes.get(0);
            if (Element.class.isAssignableFrom(target)) {
                Class<? extends MObject> targetClass = classes.get(0);
                multipleEditor.init(getEditedElement(), getFieldName(), targetClass, getElementFilter());
                return multipleEditor;
            }
        }
        return null;
    }

}
