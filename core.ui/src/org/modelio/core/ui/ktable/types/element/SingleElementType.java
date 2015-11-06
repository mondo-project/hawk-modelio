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
import org.modelio.core.ui.ktable.types.hybrid.HybridCellEditor;
import org.modelio.core.ui.ktable.types.hybrid.HybridCellRenderer;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("8df2388e-c068-11e1-8c0a-002564c97630")
public class SingleElementType extends ElementType {
    @objid ("8df2388f-c068-11e1-8c0a-002564c97630")
    private boolean displayOwner = true;

    @objid ("16c5e6c4-16da-11e2-aa0d-002564c97630")
    private ICoreSession session;

    @objid ("8df23890-c068-11e1-8c0a-002564c97630")
    public SingleElementType(boolean acceptNullValue, Class<? extends MObject> allowedClass, ICoreSession session) {
        super(acceptNullValue, allowedClass);
        this.session = session;
    }

    @objid ("8df2389b-c068-11e1-8c0a-002564c97630")
    public SingleElementType(boolean acceptNullValue, List<Class<? extends MObject>> allowedClasses) {
        super(acceptNullValue, allowedClasses);
    }

    @objid ("8df238a3-c068-11e1-8c0a-002564c97630")
    public boolean isDisplayOwner() {
        return this.displayOwner;
    }

    @objid ("8df238a7-c068-11e1-8c0a-002564c97630")
    public void setDisplayOwner(boolean displayOwner) {
        this.displayOwner = displayOwner;
    }

    /**
     * Renderer used to display hybrid fields
     */
    @objid ("0a72aad1-cb5b-11e1-9165-002564c97630")
    @Override
    public DefaultCellRenderer getRenderer() {
        if (isDisplayOwner()) {
            HybridCellRenderer hybridRendererWithOwner = new HybridCellRenderer(DefaultCellRenderer.INDICATION_FOCUS, true);
            hybridRendererWithOwner.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE));
            hybridRendererWithOwner.setAlignment(SWTX.ALIGN_HORIZONTAL_CENTER | SWTX.ALIGN_VERTICAL_CENTER);
            return hybridRendererWithOwner;
        } else {
            HybridCellRenderer hybridRendererWithoutOwner = new HybridCellRenderer(DefaultCellRenderer.INDICATION_FOCUS, false);
            hybridRendererWithoutOwner.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE));
            hybridRendererWithoutOwner.setAlignment(SWTX.ALIGN_HORIZONTAL_CENTER | SWTX.ALIGN_VERTICAL_CENTER);
            return hybridRendererWithoutOwner;
        }
    }

    @objid ("0a72d1e2-cb5b-11e1-9165-002564c97630")
    @Override
    public KTableCellEditor getEditor(IModelioPickingService pickingService) {
        // Hybrid editor
        HybridCellEditor editor = new HybridCellEditor(acceptNullValue(), pickingService, this.session, false);
        editor.setTargetClasses(getAllowedClasses());
        editor.setElementFilter(getElementFilter());
        return editor;
    }

}
