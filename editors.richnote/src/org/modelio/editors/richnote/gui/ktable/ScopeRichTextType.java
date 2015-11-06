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
                                    

package org.modelio.editors.richnote.gui.ktable;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.SWTX;
import de.kupzog.ktable.renderers.DefaultCellRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.modelio.app.core.activation.IActivationService;
import org.modelio.app.core.picking.IModelioPickingService;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.core.ui.ktable.types.PropertyType;
import org.modelio.metamodel.uml.infrastructure.ModelElement;

@objid ("8df0b216-c068-11e1-8c0a-002564c97630")
public class ScopeRichTextType extends PropertyType {
    @objid ("8df0b217-c068-11e1-8c0a-002564c97630")
    private ModelElement editedElement;

    /**
     * The Modelio activation service to use to open the rich note.
     */
    @objid ("b2da7cb0-f058-4e39-9bc0-0e103ea47b79")
    private IActivationService activationService;

    @objid ("98489572-6443-4962-9694-2f6c0b2e49c7")
    private IProjectService projectService;

    /**
     * Initialize a new KTable editor type.
     * @param editedElement the edited object
     * @param acceptNullValue <code>true</code> to accept null value.
     * @param activationService The Modelio activation service to use to open the rich note.
     */
    @objid ("8df0b21a-c068-11e1-8c0a-002564c97630")
    public ScopeRichTextType(final ModelElement editedElement, final boolean acceptNullValue, IProjectService projectService, IActivationService activationService) {
        super(acceptNullValue);
        this.editedElement = editedElement;
        this.activationService = activationService;
        this.projectService = projectService;
    }

    @objid ("0ab3ad1c-cb5b-11e1-9165-002564c97630")
    @Override
    public KTableCellEditor getEditor(IModelioPickingService pickingService) {
        return new ScopeRichTextCellEditor(this.editedElement, this.activationService, this.projectService);
    }

    /**
     * Renderer used to display scope rich text
     */
    @objid ("0ab3d42c-cb5b-11e1-9165-002564c97630")
    @Override
    public DefaultCellRenderer getRenderer() {
        ScopeRichTextCellRenderer scopeRichTextCellRenderer = new ScopeRichTextCellRenderer(DefaultCellRenderer.INDICATION_FOCUS);
        scopeRichTextCellRenderer.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE));
        scopeRichTextCellRenderer.setAlignment(SWTX.ALIGN_HORIZONTAL_CENTER | SWTX.ALIGN_VERTICAL_CENTER);
        return scopeRichTextCellRenderer;
    }

}
