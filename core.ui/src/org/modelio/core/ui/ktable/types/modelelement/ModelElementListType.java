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
                                    

package org.modelio.core.ui.ktable.types.modelelement;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.SWTX;
import de.kupzog.ktable.renderers.DefaultCellRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.modelio.app.core.picking.IModelioPickingService;
import org.modelio.core.ui.ktable.types.element.SingleElementType;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("8deda4d3-c068-11e1-8c0a-002564c97630")
public class ModelElementListType extends SingleElementType {
    @objid ("8deda4d4-c068-11e1-8c0a-002564c97630")
    private List<ModelElement> elements = null;

    @objid ("8deda4dd-c068-11e1-8c0a-002564c97630")
    public List<ModelElement> getElements() {
        return this.elements;
    }

    @objid ("8def2b4a-c068-11e1-8c0a-002564c97630")
    public ModelElementListType(boolean acceptNullValue, Class<? extends MObject> allowedClass, List<ModelElement> elements, ICoreSession session) {
        super(acceptNullValue, allowedClass, session);
        this.elements = elements;
    }

    @objid ("8def2b55-c068-11e1-8c0a-002564c97630")
    public ModelElementListType(boolean acceptNullValue, List<Class<? extends MObject>> allowedClasses, List<ModelElement> elements) {
        super(acceptNullValue, allowedClasses);
        this.elements = elements;
    }

    @objid ("0aa99ab9-cb5b-11e1-9165-002564c97630")
    @Override
    public KTableCellEditor getEditor(IModelioPickingService pickingService) {
        return new ModelElementListCellEditor(getAllowedClasses(), getElements(), acceptNullValue(), pickingService);
    }

    @objid ("0aa99abe-cb5b-11e1-9165-002564c97630")
    @Override
    public DefaultCellRenderer getRenderer() {
        ModelElementListCellRenderer modelElementRenderer = new ModelElementListCellRenderer(DefaultCellRenderer.INDICATION_FOCUS);
        modelElementRenderer.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE));
        modelElementRenderer.setAlignment(SWTX.ALIGN_HORIZONTAL_CENTER | SWTX.ALIGN_VERTICAL_CENTER);
        return modelElementRenderer;
    }

}
