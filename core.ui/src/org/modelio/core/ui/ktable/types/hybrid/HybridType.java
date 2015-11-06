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
                                    

package org.modelio.core.ui.ktable.types.hybrid;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.SWTX;
import de.kupzog.ktable.renderers.DefaultCellRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.modelio.app.core.picking.IModelioPickingService;
import org.modelio.core.ui.ktable.types.PropertyType;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("f8849728-c5d4-11e1-8f21-002564c97630")
public abstract class HybridType extends PropertyType {
    @objid ("4685469b-f95e-4dfc-bac6-b6680353fd2b")
    private ICoreSession session;

    @objid ("397bfca5-c5d8-11e1-8f21-002564c97630")
    public HybridType(ICoreSession session) {
        super(true);
        
        this.session = session;
    }

    @objid ("397bfca7-c5d8-11e1-8f21-002564c97630")
    public abstract List<Class<? extends MObject>> getTypes();

    @objid ("397bfcad-c5d8-11e1-8f21-002564c97630")
    @Override
    public final boolean acceptNullValue() {
        return super.acceptNullValue();
    }

    /**
     * Renderer used to display hybrid fields
     */
    @objid ("0a84d3b9-cb5b-11e1-9165-002564c97630")
    @Override
    public DefaultCellRenderer getRenderer() {
        HybridCellRenderer hybridRenderer = new HybridCellRenderer(DefaultCellRenderer.INDICATION_FOCUS, true);
        hybridRenderer.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE));
        hybridRenderer.setAlignment(SWTX.ALIGN_HORIZONTAL_CENTER | SWTX.ALIGN_VERTICAL_CENTER);
        return hybridRenderer;
    }

    @objid ("0a84facc-cb5b-11e1-9165-002564c97630")
    @Override
    public KTableCellEditor getEditor(IModelioPickingService pickingService) {
        // Hybrid editor
        HybridCellEditor hybridElementEditor = new HybridCellEditor(acceptNullValue(), pickingService, this.session, acceptStringValue());
        hybridElementEditor.setTargetClasses(getTypes());
        return hybridElementEditor;
    }

    @objid ("71a35452-a58e-4ac3-8fc2-4ddf2ae5531b")
    public abstract boolean acceptStringValue();

}
